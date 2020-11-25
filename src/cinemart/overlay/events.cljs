(ns cinemart.overlay.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))
(rf/reg-event-db
 ::close
 (fn-traced
  [db [_ uuid]]
  (let [notis (:noti db)]
    (assoc db :noti (dissoc notis uuid)))))

(rf/reg-event-db
 ::open
 (fn-traced
  [db [_ overlay]]
  (assoc db :overlay overlay)))
