(ns cinemart.manager.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
  ::theater
  (fn-traced [db]
             (:theater db)))

(rf/reg-sub
  ::schedules
  (fn-traced [db]
             (-> db :schedules :all)))
