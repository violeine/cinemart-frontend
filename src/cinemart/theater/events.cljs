(ns cinemart.theater.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [ajax.core :as ajax]
   [cinemart.config :refer [ backend-interceptor]]))


(rf/reg-event-fx
  ::fetch-theater
  (fn-traced [_ [_ id]]
             {:http-xhrio
              [{:method :get
                :uri (str "/theaters/" id )
                :response-format (ajax/json-response-format
                                   {:keywords? true})
                :interceptors [backend-interceptor]
                :on-success [:cinemart.events/insert [:theater]]
                :on-failure [:cinemart.auth.events/api-failure]}
               {:method :get
                :uri (str "/theaters/" id "/movies")
                :response-format (ajax/json-response-format
                                   {:keywords? true})
                :interceptors [backend-interceptor]
                :on-success [:cinemart.events/insert [:theater :movies]]
                :on-failure [:cinemart.auth.events/api-failure]}]}))






