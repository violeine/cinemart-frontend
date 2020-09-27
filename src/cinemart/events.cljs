(ns cinemart.events
  (:require
    [re-frame.core :as rf]
    [cinemart.db :as db]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [reitit.frontend.controllers :as rfc]))

(rf/reg-event-db
  ::init-db
  (fn-traced [_ _]
             db/default-db))

(rf/reg-event-fx
  ::navigate
  (fn-traced [db [_ & route]]
             {::navigate! route}))

(rf/reg-event-db
  ::navigated
  (fn-traced [db [_ new-match]]
             (let [old-match (:current-route db)
                   controllers (rfc/apply-controllers
                                 (:controllers old-match)
                                 new-match)]
               (assoc db :current-route
                      (assoc new-match :controllers controllers)))))


