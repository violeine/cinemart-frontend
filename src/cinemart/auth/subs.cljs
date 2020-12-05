(ns cinemart.auth.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::role
 (fn-traced [db]
            (get-in db [:user :role])))

(rf/reg-sub
 ::auth?
 (fn-traced [db]
            (:auth? db)))
