(ns cinemart.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [ajax.core :as ajax]
   [reitit.frontend.controllers :as rfc]))

(rf/reg-event-db
 ::init-db
 (fn-traced [_ _]
            db/default-db))
(rf/reg-event-fx
 ::navigate
 (fn-traced [db [_ & route]]
            {::fx/navigate! route}))
(rf/reg-event-db
 ::navigated
 (fn-traced [db [_ new-match]]
            (let [old-match (:current-route db)
                  controllers (rfc/apply-controllers
                               (:controllers old-match)
                               new-match)]
              (assoc db :current-route
                     (assoc new-match :controllers controllers)))))
;"https://api.themoviedb.org/3/movie/76341"

(rf/reg-event-fx
 ::test-fetch
 (fn-traced [_ _]
            {:http-xhrio {:method :get
                          :uri "https://violeine.duckdns.org/ping"
                          :response-format (ajax/json-response-format
                                            {:keywords? true})
                          :on-success [::good-http-result]
                          :on-failure [::bad-http-result]}}))

(rf/reg-event-db
 ::good-http-result
 (fn-traced [db [_ result]]
            (assoc db :http-result result)))

(rf/reg-event-db
 ::bad-http-result
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))


