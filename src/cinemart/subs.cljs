(ns cinemart.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::current-route
 (fn-traced [db]
            (:current-route db))) ; get current route out

(rf/reg-sub
 :auth?
 (fn-traced [db]
            (:auth? db)))

(rf/reg-sub
 ::http-result
 (fn-traced [db]
            (:http-result db)))

(rf/reg-sub
 ::http-failure
 (fn-traced [db]
            (:http-failure db)))
