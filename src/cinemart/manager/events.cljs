(ns cinemart.manager.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [ajax.core :as ajax]
            [cinemart.auth.events :as auth]
            [cinemart.notification.events :as noti]
            [cinemart.overlay.events :as overlay]
            [day8.re-frame.async-flow-fx]
            [cinemart.config :refer [backend-interceptor token-interceptor]]))

(def link {:users "/users"
           :theater "/theaters"
           :admins "/admins"
           :managers "/managers"})

(rf/reg-event-fx
 ::fetch-manager
 (fn-traced [{:keys [db]} _]
            (let [token (get-in db [:user :token])
                  theater-id (get-in db [:user :theater_id])
                  req [{:method :get
                        :uri (str "/theaters/" theater-id)
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :theater]
                        :on-failure [::failure]}
                       {:method :get
                        :uri (str "/theaters/" theater-id "/schedules")
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :schedules]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/me"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :users]
                        :on-failure [::failure]}
                       ]]
              {:http-xhrio req
               :db (assoc db :prev-req req)
               :fx [[:dispatch [::noti/notify {:text "manager"
                                               :type :success}]]]})))
(rf/reg-event-fx
 ::init-manager
 (fn-traced [{:keys [db]} _]
            {:async-flow {:id ::async-init-manager596161
                          :first-dispatch [::fetch-manager]
                          :rules [{:when :seen?
                                   :events ::failure
                                   :dispatch-fn (fn [[e result]]
                                                  [[::auth/api-failure result]])
                                   :halt? true}]}}))

(rf/reg-event-db
 ::success
 (fn-traced [db [_ k result]]
            (-> db
                (assoc k (:response result)))))

(rf/reg-event-db
 ::failure
 (fn-traced [db [_ failure]] (assoc db :api-failure failure)))

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
                            :on-success [::update-success k]
                            :on-failure [:cinemart.admin.events/crud-failure]}})))

(rf/reg-event-fx
  ::create-schedule
  (fn-traced [{:keys [db]} [_ payload]]
             (let [token (get-in db [:user :token])
                   theater (get-in db [:user :theater_id])]
               {:http-xhrio {:method :post
                             :uri (str "/theaters/" theater "/schedules")
                             :params payload
                             :format (ajax/json-request-format)
                             :response-format (ajax/json-response-format {:keywords? true})
                             :interceptors [backend-interceptor (token-interceptor token)]
                             :on-success [::create-success]
                             :on-failure [:cinemart.admin.events/crud-failure]
                             }})))

(rf/reg-event-fx
  ::create-success
  (fn-traced [_ _]
             {:fx [[:dispatch [::overlay/close]]
                   [:dispatch [::noti/notify {:text "Create Success"}]]]}))

(rf/reg-event-fx
 ::update-success
 (fn-traced [{:keys [db]} [_ k result]]
            (let [old (get-in db [:admin k])
                  new-data (get-in result [:response :after-updated])]
              {:db (assoc db  k new-data)
               :fx [[:dispatch [::overlay/close]]
                    [:dispatch [::noti/notify {:text "update success"}]]]})))
