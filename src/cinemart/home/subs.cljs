(ns cinemart.home.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
  ::home
  (fn-traced [db]
             (:home db)))
