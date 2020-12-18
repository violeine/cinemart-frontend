(ns cinemart.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cinemart.effects :as fx]
            [ajax.core :as ajax]
            [cinemart.config :refer [backend-interceptor token-interceptor]]
            [cinemart.notification.events :as noti]))

(reg-event-fx
 ::login
 (fn-traced [_ [_ {:keys [payload role]}]]
            {:http-xhrio {:method :post
                          :uri (str "/login" role)
                          :params payload
                          :format (ajax/json-request-format)
                          :response-format (ajax/json-response-format {:keywords? true})
                          :interceptors [backend-interceptor]
                          :on-success [::login-success]
                          :on-failure [::login-failure]}}))
(reg-event-fx
 ::signup
 (fn-traced [_ [_ payload]]
            {:http-xhrio {:method :post
                          :uri  "/register"
                          :params payload
                          :format (ajax/json-request-format)
                          :response-format (ajax/json-response-format {:keywords? true})
                          :interceptors [backend-interceptor]
                          :on-success [::login-success]
                          :on-failure [::login-failure]}}))
(reg-event-fx
  ::get-me
  (fn-traced [{:keys [db]} [_ payload]]
             (let [token (get-in db [:user :token])]
               {:http-xhrio {:method :get
                             :uri  "/me"
                             :params payload
                             :format (ajax/json-request-format)
                             :response-format (ajax/json-response-format {:keywords? true})
                             :interceptors [backend-interceptor (token-interceptor token)]
                             :on-success [::me-success]
                             :on-failure [::api-failure]}})))
(reg-event-fx
  ::update-me
  (fn-traced [{:keys [db]} [_ payload]]
             (let [token (get-in db [:user :token])
                   pl    (if (nil? (:password payload))
                           (dissoc payload :password)
                           payload)]
               {:http-xhrio {:method :put
                             :uri  "/me"
                             :params pl
                             :format (ajax/json-request-format)
                             :response-format (ajax/json-response-format {:keywords? true})
                             :interceptors [backend-interceptor (token-interceptor token)]
                             :on-success [::me-success]
                             :on-failure [::api-failure]}})))
(reg-event-db
  ::me-success
  (fn-traced [db [_ result]]
             (let [old (:user db)
                   new-me (merge old (:response result))]
               (assoc db :user new-me))))

(reg-event-fx
 ::login-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [ref-token (get-in result [:response :refresh-token])
                  token (get-in result [:response :token])]
              {:db
               (-> db
                   (assoc :auth? true)
                   (assoc :http-result result)
                   (assoc :user (:response result)))
               :fx [[:dispatch [:cinemart.events/back]]
                    [::fx/save-storage! ["refresh-token" ref-token]]
                    [::fx/save-storage! ["token" token]]
                    [::fx/save-storage! ["user" (.stringify js/JSON (clj->js (:response result)))]]
                    [:dispatch [::noti/notify {:text "Login successfully"
                                               :type :success}]]]})))

(reg-event-fx
 ::login-failure
 (fn-traced [{:keys [db]} [_ result]]
            (let [status (:status result)
                  response (get-in result [:response :error] "Something very wrong happened!")
                  ref-token (get-in db [:user :refresh-token])]
              {:db (assoc db :http-failure result)
               :fx [[:dispatch [::noti/notify {:text response
                                               :type :warning}]]]})))

(reg-event-fx
 ::api-failure
 (fn-traced [{:keys [db]} [_ result]]
            (let [status (:status result)
                  response (get-in result [:response :error] " something very wrong happened!")
                  ref-token (get-in db [:user :refresh-token])]
              (if (= 401 status)
                ;; unauthorized
                (if (.includes response "expired")
                  ;; expired -> attempt to refresh
                  {:http-xhrio {:method :post
                                :uri "/refresh-token"
                                :response-format (ajax/json-response-format {:keywords? true})
                                :format (ajax/json-request-format)
                                :interceptors [backend-interceptor
                                               (token-interceptor ref-token)]
                                :on-success [::refresh-success]
                                :on-failure [::refresh-failure]}}
                  ;; invalid -> goto login
                  {:fx [[:dispatch [::refresh-failure]]]})
                ;; another error code
                {:db (assoc db :http-failure result)
                 :fx [[:dispatch [::noti/notify {:text (str status response)
                                                 :type :danger}]]]}))))

;; if success, re request all the cached request
(reg-event-fx
 ::refresh-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [ref-token (get-in result [:response :refresh-token])
                  token (get-in result [:response :token])
                  prev-req (:prev-req db)
                  new-req
                  (if (vector? prev-req)
                    ; insert new token
                    (map #(merge %  {:interceptors [backend-interceptor (token-interceptor token)]}) prev-req)
                    (merge prev-req {:interceptors [backend-interceptor (token-interceptor token)]}))]
              {:db
               (-> db
                   (assoc :auth? true)
                   (dissoc :prev-req)
                   (assoc :user (:response result)))
               :fx [[::fx/save-storage! ["user" (.stringify js/JSON (clj->js (:response result)))]]
                    [:http-xhrio new-req]
                    [:dispatch [::noti/notify {:text "refresh token successfully"
                                               :type :success}]]]})))

;; if failure, log user out and kick to logged in
(reg-event-fx
 ::refresh-failure
 (fn-traced [{:keys [db]} [_ result]]
            {:fx [[:dispatch [::logout]]
                  [:dispatch [::noti/notify {:text "token invalid"
                                             :type :danger}]]
                  [:dispatch [:cinemart.events/navigate :cinemart.router/login]]]}))

(reg-event-db
 ::remove-user
 (fn-traced [db _]
            (-> db
                (assoc :auth? false)
                (dissoc :user))))

(reg-event-fx
 ::logout
 (fn-traced [{:keys [db]} [_ result]]
            (let [request {:method :post
                           :uri  "/logout"
                           :format (ajax/json-request-format)
                           :response-format (ajax/json-response-format {:keywords? true})
                           :interceptors [backend-interceptor (token-interceptor
                                                               (get-in db [:user :token]))]
                           :on-success [:cinemart.events/nothing]
                           :on-failure [:cinemart.events/nothing]}]
              {:fx [[::fx/clear-storage!]
                    [:http-xhrio request]
                    [:dispatch [::remove-user]]
                    [:dispatch [:cinemart.events/navigate :cinemart.router/home]]
                    [:dispatch [::noti/notify {:text "Logged out"
                                               :type :info}]]]})))

(reg-event-fx
 ::guard
 (fn-traced [{:keys [db]} [_ {:keys [next route-match]}]]
            (let [role (get-in db [:user :role])
                  auth? (:auth? db)
                  auth-route (get-in route-match [:data :auth?])
                  role-route (get-in route-match [:data :role])]
              (if  (= auth? auth-route)
                (if auth-route
                  (if (= role role-route)
                    {:fx [[:dispatch next]]}
                    {:fx [[:dispatch [:cinemart.events/back]]
                          [:dispatch [::noti/notify {:text "should not be here"}]]]})
                  {:fx [[:dispatch next]]})
                (if auth-route
                  {:fx [[:dispatch [::noti/notify {:text "login first"}]]
                        [:dispatch [:cinemart.events/navigate :cinemart.router/login]]]}
                  {:fx [[:dispatch [:cinemart.events/back]]
                        [:dispatch [::noti/notify {:text "should not be here"}]]]})))))
