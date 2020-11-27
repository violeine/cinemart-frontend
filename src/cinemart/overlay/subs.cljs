(ns cinemart.overlay.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::get
 (fn-traced [db]
            (:overlay db)))

(rf/reg-sub
 ::hidden
 (fn-traced [db]
            (:overlay-open db)))

