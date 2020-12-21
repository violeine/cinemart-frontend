(ns cinemart.admin.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [ajax.core :as ajax]
            [cinemart.auth.events :as auth]
            [cinemart.notification.events :as noti]
            [cinemart.overlay.events :as overlay]
            [day8.re-frame.async-flow-fx]
            [cinemart.config :refer [backend-interceptor token-interceptor]]))

(def link {:users "/users"
           :theaters "/theaters"
           :admins "/admins"
           :managers "/managers"
           :movie "/movies"})

(rf/reg-event-fx
 ::fetch-admin
 (fn-traced [{:keys [db]} _]
            (let [token (get-in db [:user :token])
                  req [{:method :get
                        :uri "/theaters"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :theaters]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/admins"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :admins] :on-failure [::failure]}
                       {:method :get
                        :uri "/movies"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :movies]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/movies/genres/all"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :genres]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/managers"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :managers]
                        :on-failure [::failure]} {:method :get
                        :uri "/users"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :users]
                        :on-failure [::failure]}]]
              {:http-xhrio req
               :db (assoc db :prev-req req)})))

(rf/reg-event-fx
 ::init-admin
 (fn-traced [{:keys [db]} _]
            {:async-flow {:id ::async-init-admin
                          :first-dispatch [::fetch-admin]
                          :rules [{:when :seen?
                                   :events ::failure
                                   :dispatch-fn (fn [[e result]]
                                                  [[::auth/api-failure result]])
                                   :halt? true}]}}))

(rf/reg-event-db
 ::failure
 (fn-traced [db [_ failure]] (assoc db :api-failure failure)))

(rf/reg-event-db
 ::success
 (fn-traced [db [_ k result]]
            (-> db
                (assoc-in  [:admin k] (:response result)))))

(rf/reg-event-fx
  ::create
  (fn-traced [{:keys [db]} [_ k payload]]
             (let [token (get-in db [:user :token])]
               {:http-xhrio {:method :post
                             :uri  (get link k)
                             :params payload
                             :format (ajax/json-request-format)
                             :response-format (ajax/json-response-format {:keywords? true})
                             :interceptors [backend-interceptor (token-interceptor token)]
                             :on-success (if (= k :movie)
                                           [::movie-success]
                                           [::create-success k])
                             :on-failure [::crud-failure]}})))

(rf/reg-event-fx
 ::create-theaters
 (fn-traced [{:keys [db]} [_ payload]]
            (let [token (get-in db [:user :token])]
              {:http-xhrio {:method :post
                            :uri (:theaters link)
                            :params payload
                            :format (ajax/json-request-format)
                            :response-format (ajax/json-response-format {:keywords? true})
                            :interceptors [backend-interceptor (token-interceptor token)]
                            :on-success [::create-theater-success]
                            :on-failure [::crud-failure]}})))

(rf/reg-event-fx
 ::create-theater-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [old-theaters (get-in db [:admin :theaters])
                  old-managers (get-in db [:admin :managers])]
              {:fx [[:dispatch [::overlay/close]]
                    [:dispatch [::noti/notify {:text "create success"}]]]
               :db (-> db
                       (assoc-in [:admin :theaters] (conj old-theaters (get-in result [:response :theater])))
                       (assoc-in [:admin :managers] (conj old-managers (get-in result [:response :manager]))))})))

(rf/reg-event-fx
 ::get
 (fn-traced [{:keys [db]} [_ k]]
            (let [token (get-in db [:user :token])]
              {:http-xhrio {:method :get
                            :uri (get link k)
                            :response-format (ajax/json-response-format {:keywords? true})
                            :interceptors [backend-interceptor (token-interceptor token)]
                            :on-success [::get-success k]
                            :on-failure [::crud-failure]}})))

(rf/reg-event-fx
  ::delete
  (fn-traced [{:keys [db]} [_ k payload]]
             (let [token (get-in db [:user :token])]
               {:http-xhrio {:method :delete
                             :uri (str (get link k) "/" payload)
                             :format (ajax/json-request-format)
                             :response-format (ajax/json-response-format {:keywords? true})
                             :interceptors [backend-interceptor (token-interceptor token)]
                             :on-success
                             (if (= k :movie)
                               [::movie-success]
                               [::delete-success k])
                             :on-failure [::crud-failure]}})))

(rf/reg-event-fx
 ::update
 (fn-traced [{:keys [db]} [_ k payload {:keys [id idx]}]]
            (let [token (get-in db [:user :token])
                  pl (if (nil? (:password payload))
                       (dissoc payload :password)
                       payload)]
              {:http-xhrio {:method :put
                            :uri (str (get link k) "/" id)
                            :format (ajax/json-request-format)
                            :params pl
                            :response-format (ajax/json-response-format {:keywords? true})
                            :interceptors [backend-interceptor (token-interceptor token)]
                            :on-success [::update-success k idx]
                            :on-failure [::crud-failure]}})))

(rf/reg-event-fx
 ::update-movie
 (fn-traced [{:keys [db]} [_ k payload id]]
            (let [token (get-in db [:user :token])
                  pl (if (nil? (:password payload))
                       (dissoc payload :password)
                       payload)]
              {:http-xhrio {:method :put
                            :uri (str (get link k) "/" id)
                            :format (ajax/json-request-format)
                            :params pl
                            :response-format (ajax/json-response-format {:keywords? true})
                            :interceptors [backend-interceptor (token-interceptor token)]
                            :on-success [::movie-success]
                            :on-failure [::crud-failure]}})))

(rf/reg-event-fx
  ::movie-success
  (fn-traced [{:keys [db]} [_ result]]
             {:fx [[:dispatch [::overlay/close]]
                   [:dispatch [::noti/notify {:text "update success"}]]]
              :http-xhrio
              {:method :get
               :uri "/movies"
               :response-format (ajax/json-response-format
                                  {:keywords? true})
               :interceptors [backend-interceptor]
               :on-success [::success :movies]
               :on-failure [::failure]}}))

(rf/reg-event-fx
 ::crud-failure
 (fn-traced [{:keys [db]} [_ result]]
            (let [status (:status result)
                  response (get-in result [:response :error])]
              (if (= 401 status)
                {:fx [[:dispatch [::auth/api-failure result]]]}
                {:fx [[:dispatch [::noti/notify {:text response
                                                 :type :warning}]]]}))))

(rf/reg-event-fx
 ::create-success
 (fn-traced [{:keys [db]} [_ k result]]
            (let [old (get-in db [:admin k])]
              {:fx [[:dispatch [::overlay/close]]
                    [:dispatch [::noti/notify {:text "create success"}]]]
               :db (assoc-in db [:admin k] (conj old (:response result)))})))

(rf/reg-event-db
 ::get-success
 (fn-traced [db [_ k result]]
            (assoc-in db [:admin k] (:response result))))

(rf/reg-event-fx
 ::update-success
 (fn-traced [{:keys [db]} [_ k idx result]]
            (let [old (get-in db [:admin k])
                  new-data (get-in result [:response :after-updated])]
              {:db (assoc-in db [:admin k] (assoc old idx new-data))
               :fx [[:dispatch [::overlay/close]]
                    [:dispatch [::noti/notify {:text "update success"}]]]})))
(rf/reg-event-fx
 ::delete-success
 (fn-traced [{:keys [db]} [_ k result]]
            (let [old (get-in db [:admin k])
                  deleted-id (get-in result [:response :before-deleted :id])]
              {:db (assoc-in db [:admin k] (filterv #(not= deleted-id (:id %)) old))
               :fx [[:dispatch [::noti/notify {:text "delete success"}]]]})))
