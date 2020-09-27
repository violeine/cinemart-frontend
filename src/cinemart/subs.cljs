(ns cinemart.subs
  (:require [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
  ::current-route
  (fn-traced [db]
             (:current-route db))) ; get current route out

