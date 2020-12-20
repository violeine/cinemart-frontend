(ns cinemart.ticket.subs
  (:require [re-frame.core :as rf]
            [cinemart.config :refer [today]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))


(rf/reg-sub
  ::get
  (fn-traced [db [_ k]]
             (get-in db [:ticket k])))

(rf/reg-sub
  ::get-schedules
  (fn-traced [db [_ _]]
             (let [schedule (get-in db [:ticket :schedules])]
               (when schedule
                 (filter #(> 0
                             (compare
                               (today) (:time %))) schedule)))))

