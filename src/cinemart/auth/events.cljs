(ns cinemart.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cinemart.effects :as fx]
            [ajax.core :as ajax]
            [cinemart.config :refer [backend-interceptor token-interceptor]]
            [cinemart.notification.events :as noti]))

(reg-event-fx
 ::login
 (fn-traced [_ [_ payload]]
            {:http-xhrio {:method :post
                          :uri  "/login"
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
 ::login-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [ref-token (get-in result [:user :refresh-token])
                  token (get-in result [:user :token])
                  prev-route (get-in db [:prev-route :name])
                  prev-param (get-in db [:prev-route :path-params])]
              {:db
               (-> db
                   (assoc :auth? true)
                   (assoc :user (:user result)))
               :fx [[:dispatch [:cinemart.events/navigate prev-route prev-param]]
                    [::fx/save-storage! ["refresh-token" ref-token]]
                    [::fx/save-storage! ["token" token]]
                    [:dispatch [::noti/notify {:text "Login successfully"
                                               :type :success}]]]})))

(reg-event-fx
 ::login-failure
 ;;TODO noti it's up
 (fn-traced [{:keys [db]} [_ result]]
            (let [status (:status result)
                  response (get-in result [:response :error])
                  ref-token (get-in db [:user :refresh-token])]
              {:db (assoc db :http-failure result)
               :fx [[:dispatch [::noti/notify {:text response
                                               :type :warning}]]]})))

;; TODO implement refresh mechanism herre
(reg-event-fx
 ::api-failure
 (fn-traced [{:keys [db]} [_ result]]
            (let [status (:status result)
                  response (get-in result [:response :error])
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
                {:fx [[:dispatch [::noti/notify {:text (str status response)}]]]}))))

;; if success, re request all the cached request
(reg-event-fx
 ::refresh-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [ref-token (get-in result [:user :refresh-token])
                  token (get-in result [:user :token])
                  prev-req (:prev-req db)
                  new-req
                  (if (vector? prev-req)
                    ; insert new token
                    (map #(merge %  {:interceptors [backend-interceptor token-interceptor token]}))
                    (merge prev-req {:interceptors [backend-interceptor (token-interceptor token)]}))]
              {:db
               (-> db
                   (assoc :auth? true)
                   (dissoc :prev-req)
                   (assoc :user (:user result)))
               :fx [[::fx/save-storage! ["refresh-token" ref-token]]
                    [::fx/save-storage! ["token" token]]
                    [:http-xhrio new-req]
                    [:dispatch [::noti/notify {:text "refresh token successfully"
                                               :type :success}]]]})))

;; if failure, log user out and kick to logged in
(reg-event-fx
 ::refresh-failure
 (fn-traced [{:keys [db]} [_ result]]
            {:fx [[:dispatch [::remove-user]]
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
                    [:dispatch [::noti/notify {:text "Logged out"
                                               :type :info}]]]})))

