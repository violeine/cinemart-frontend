(ns cinemart.notification.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))
(rf/reg-event-db
 ::kill-noti
 (fn-traced
  [db [_ uuid]]
  (let [notis (:noti db)]
    (assoc db :noti (dissoc notis uuid)))))

(rf/reg-event-db
 ::notify
 (fn-traced
  [db [_ prop]]
  (let [uuid (random-uuid)]
    (assoc-in db [:noti uuid] (assoc prop
                                     :uuid uuid)))))

