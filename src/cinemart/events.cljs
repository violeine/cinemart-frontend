(ns cinemart.events
  (:require
    [re-frame.core :as re-frame]
    [cinemart.db :as db]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
  ::init-db
  (fn-traced [_ _]
             db/default-db))

