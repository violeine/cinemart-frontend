(ns cinemart.admin.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::admin
 (fn-traced [db]
            (:admin db)))
