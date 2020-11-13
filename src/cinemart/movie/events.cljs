(ns cinemart.movie.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [ajax.core :as ajax]
   [cinemart.config :refer [api-interceptor]]
   [reitit.frontend.controllers :as rfc]))

(rf/reg-event-fx
 ::fetch-movie
 (fn-traced [_ [_ id]]
            {:http-xhrio [{:method :get
                           :uri (str "/movie/" id)
                           :response-format (ajax/json-response-format
                                             {:keywords? true})
                           :interceptors [api-interceptor]
                           :on-success [::success-movie]
                           :on-failure [::failure]}
                          {:method :get
                           :uri (str "/movie/" id "/credits")
                           :response-format (ajax/json-response-format
                                             {:keywords? true})
                           :interceptors [api-interceptor]
                           :on-success [::success-credit]
                           :on-failure [::failure]}]}))

(rf/reg-event-db
 ::success-movie
 (fn-traced [db [_ result]]
            (assoc db :movie result)))

(rf/reg-event-db
 ::success-credit
 (fn-traced [db [_ result]]
            (assoc db :credit result)))

(rf/reg-event-db
 ::failure
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))



