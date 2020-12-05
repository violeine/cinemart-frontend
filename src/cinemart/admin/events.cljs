(ns cinemart.admin.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [ajax.core :as ajax]
            [cinemart.auth.events :as auth]
            [cinemart.notification.events :as noti]
            [day8.re-frame.async-flow-fx]
            [cinemart.config :refer [backend-interceptor token-interceptor]]))

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
                        :on-success [::success :admins]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/managers"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :managers]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/users"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success :users]
                        :on-failure [::failure]}]]
              {:http-xhrio req
               :db (assoc db :prev-req req)
               :fx [[:dispatch [::noti/notify {:text "admin"
                                               :type :success}]]]})))

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
 (fn-traced [db [_ failure]]
            (assoc db :api-failure failure)))

(rf/reg-event-db
 ::success
 (fn-traced [db [_ k result]]
            (-> db
                (assoc-in  [:admin k] (:response result)))))

(rf/reg-event-db
 ::save-request
 (fn-traced [db [_  req]]
            (assoc db :request (conj (:request db) req))))










