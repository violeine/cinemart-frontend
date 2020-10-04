(ns cinemart.notification.subs
  (:require [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
  ::get-noti
  (fn-traced [db]
             (map second (:noti db))))


