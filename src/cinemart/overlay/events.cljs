(ns cinemart.overlay.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-event-db
 ::kill
 (fn-traced
  [db _]
  (dissoc db :overlay-open)))

(rf/reg-event-fx
 ::close
 (fn-traced
  [{:keys [db]} _]
  {:db (dissoc db :overlay)
   :fx [[:dispatch-later {:ms 300
                          :dispatch [::kill]}]]}))

(rf/reg-event-db
 ::open
 (fn-traced
  [db [_ overlay]]
  (-> db
      (assoc :overlay overlay)
      (assoc :overlay-open true))))
