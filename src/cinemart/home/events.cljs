(ns cinemart.home.events
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
 ::fetch-home
 (fn-traced [{:keys [db]} _]
            (let [token (get-in db [:user :token])
                  req [
                       {:method :get
                        :uri "/movies/latest/10"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor ]
                        :on-success [::success :latest-db]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/movies/latest/from/now"
                        :response-format (ajax/json-response-format
                                          {:keywords? true})
                        :interceptors [backend-interceptor ]
                        :on-success [::success :latest-schedule]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/movies/genres/movies"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor ]
                        :on-success [::success :genres]
                        :on-failure [::failure]}
                       {:method :get
                        :uri "/theaters"
                        :response-format (ajax/json-response-format
                                           {:keywords? true})
                        :interceptors [backend-interceptor ]
                        :on-success [::success :theaters]
                        :on-failure [::failure]}
                       ]]
              {:http-xhrio req
               :db (assoc db :prev-req req)})))
(rf/reg-event-fx
 ::init-home
 (fn-traced [{:keys [db]} _]
            {:async-flow {:id ::async-init-home
                          :first-dispatch [::fetch-home]
                          :rules [{:when :seen?
                                   :events ::failure
                                   :dispatch-fn (fn [[e result]]
                                                  [[::auth/api-failure result]])
                                   :halt? true}]}}))

(rf/reg-event-db
 ::success
 (fn-traced [db [_ k result]]
            (-> db
                (assoc-in [:home k] (:response result)))))
