(ns cinemart.profile.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [cinemart.config :refer [backend-interceptor token-interceptor]]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [cinemart.auth.events :as auth]
   [ajax.core :as ajax]
   [cinemart.notification.events :as noti]
   [reitit.frontend.controllers :as rfc]))

(rf/reg-event-fx
 ::fetch-profile
 (fn-traced [{:keys [db]} _]
            (let [token (get-in db [:user :token])
                  req [
                       {:method :get
                        :uri "/me/invoices"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success [:users :ticket]]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/me"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor (token-interceptor token)]
                        :on-success [::success [:users]]
                        :on-failure [::failure]}
                       ]]
              {:http-xhrio req
               :db (assoc db :prev-req req)
               :fx [[:dispatch [::noti/notify {:text "profile"
                                               :type :success}]]]})))

(rf/reg-event-fx
 ::init-profile
 (fn-traced [{:keys [db]} _]
            {:async-flow {:id ::async-init-profile
                          :first-dispatch [::fetch-profile]
                          :rules [{:when :seen?
                                   :events ::failure
                                   :dispatch-fn (fn [[e result]]
                                                  [[::auth/api-failure result]])
                                   :halt? true}]}}))
(rf/reg-event-db
 ::success
 (fn-traced [db [_ k result]]
            (assoc-in db k (:response result))))
