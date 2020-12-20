(ns cinemart.profile.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))


(rf/reg-sub
  ::get-ticket
  (fn-traced [db]
             (get-in db [:users :ticket])))
