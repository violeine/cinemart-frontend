(ns cinemart.ticket.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))


(rf/reg-sub
  ::get
  (fn-traced [db [_ k]]
             (get-in db [:ticket k])))

