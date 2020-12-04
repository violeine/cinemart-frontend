(ns cinemart.notification.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))
(rf/reg-event-db
 ::kill-noti
 (fn-traced
  [db [_ uuid]]
  (let [notis (:noti db)]
    (assoc db :noti (dissoc notis uuid)))))

(rf/reg-event-fx
 ::notify
 (fn-traced
  [{:keys [db]} [_ prop]]
  (let [uuid (random-uuid)]
    {:db
     (assoc-in db [:noti uuid] (merge {:type :info}
                                      (assoc prop
                                             :uuid uuid)))
     :fx [[:dispatch-later {:ms 4000
                            :dispatch [::kill-noti uuid]}]]})))
