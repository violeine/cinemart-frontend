(ns cinemart.notification.events
  (:require [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn remove-indexed [v n]
  (into (subvec v 0 n) (subvec v (inc n))))

(rf/reg-event-fx
  ::noti-animated-end
  (fn-traced
    [{:keys [db]} [_ id]]
    (let [noti (get-in db [:noti id])]
      {:db
       (assoc-in db [:noti id]
                 (assoc noti
                        :class ["translate-x-0"
                                "mr-3"]))
       :fx [[:dispatch-later {:ms 4000
                              :dispatch [::close-noti id]}]]})))

(rf/reg-event-fx
  ::close-noti
  (fn-traced
    [{:keys [db]} [_ id]]
    (let [noti (get-in db [:noti id])]
      {:db (assoc-in db [:noti id]
                     (assoc noti
                            :class "translate-x-full"))
       :fx [[:dispatch-later {:ms 400
                              :dispatch [::kill-noti id]}]]})))

(rf/reg-event-db
  ::kill-noti
  (fn-traced
    [db [_ id]]
    (let [noti (:noti db)]
      (assoc db :noti (remove-indexed noti id)))))

(rf/reg-event-fx
  ::notify
  (fn-traced
    [{:keys [db]} [_ prop]]
    (let [noti (:noti db)
          id (count noti)]
      {:db (assoc db :noti (conj noti (assoc prop
                                             :uuid (random-uuid)
                                             :class "translate-x-full")))
       :fx [[:dispatch [::noti-animated-end id]]]})))


