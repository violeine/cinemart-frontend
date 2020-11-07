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
            {:http-xhrio {:method :get
                          :uri (str "/movie/" id)
                          :response-format (ajax/json-response-format
                                            {:keywords? true})
                          :interceptors [api-interceptor]
                          :on-success [::success]
                          :on-failure [::failure]}}))

(rf/reg-event-db
 ::success
 (fn-traced [db [_ result]]
            (assoc db :movie result)))

(rf/reg-event-db
 ::failure
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))



