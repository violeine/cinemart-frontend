(ns cinemart.movie.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [ajax.core :as ajax]
   [cinemart.config :refer [ backend-interceptor]]
   [reitit.frontend.controllers :as rfc]))

(rf/reg-event-fx
 ::fetch-movie
 (fn-traced [_ [_ id]]
            {:http-xhrio [{:method :get
                           :uri (str "/movies/" id)
                           :response-format (ajax/json-response-format
                                             {:keywords? true})
                           :interceptors [backend-interceptor]
                           :on-success [::success-movie]
                           :on-failure [::failure]}
                          ; {:method :get
                          ;  :uri (str "/movie/" id "/credits")
                          ;  :response-format (ajax/json-response-format
                          ;                     {:keywords? true})
                          ;  :interceptors [movie-interceptor]
                          ;  :on-success [::success-credit]
                          ;  :on-failure [::failure]}
                          ; {:method :get
                          ;  :uri (str "/movie/" id "/reviews")
                          ;  :response-format (ajax/json-response-format
                          ;                     {:keywords? true})
                          ;  :interceptors [movie-interceptor]
                          ;  :on-success [::success-review]
                          ;  :on-failure [::failure]}
                          ; {:method :get
                          ;  :uri (str "/movie/" id "/images?language=en")
                          ;  :response-format (ajax/json-response-format
                          ;                     {:keywords? true})
                          ;  :interceptors [movie-interceptor]
                          ;  :on-success [::success-media]
                          ;  :on-failure [::failure]}
                          ]}))

(rf/reg-event-fx
  ::post-movie
  (fn-traced [_ [_ payload]]
             {:http-xhrio {:method :patch
                           :uri "/movies"
                           :params payload
                           :response-format (ajax/json-response-format)
                           :format (ajax/json-request-format)
                           :interceptors [backend-interceptor]
                           :on-success [:cinemart.events/nothing]
                           :on-failure [:cinemart.events/nothing]}}))

(rf/reg-event-db
 ::success-movie
 (fn-traced [db [_ result]]
            (-> db
                (assoc :movie (:response result))
                (assoc :http-result result))))

(rf/reg-event-db
 ::success-credit
 (fn-traced [db [_ result]]
            (assoc db :credit result)))

(rf/reg-event-db
 ::success-review
 (fn-traced [db [_ result]]
            (assoc db :review result)))

(rf/reg-event-db
 ::success-media
 (fn-traced [db [_ result]]
            (assoc db :media result)))

(rf/reg-event-db
 ::failure
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))



