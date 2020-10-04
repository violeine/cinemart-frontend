(ns cinemart.notification.events
  (:require [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn remove-indexed [v n]
  (into (subvec v 0 n) (subvec v (inc n))))

(rf/reg-event-fx
  ::noti-animated-end
  (fn-traced
    [{:keys [db]} [_ uuid]]
    (let [noti (get-in db [:noti uuid])]
      {:db
       (assoc-in db [:noti uuid]
                 (assoc noti
                        :class ["translate-x-0" "mr-3"]))
       :fx [[:dispatch-later {:ms 4000 :dispatch [::noti-close uuid]}]]})))
(rf/reg-event-fx
  ::noti-close
  (fn-traced
    [{:keys [db]} [_ uuid]]
    (let [noti (get-in db [:noti uuid])]
      {:db
       (assoc-in db [:noti uuid]
                 (assoc noti
                        :class "translate-x-full"))
       :fx [[:dispatch-later {:ms 300 :dispatch [::kill-noti uuid]}]]})))

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
      {:db (assoc-in db [:noti uuid] (assoc prop
                                            :uuid uuid
                                            :class "translate-x-full"))
       :fx [[:dispatch [::noti-animated-end uuid]]]})))


