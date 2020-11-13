(ns cinemart.movie.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::movie
 (fn-traced [db]
            (:movie db)))

(rf/reg-sub
 ::credit
 (fn-traced [db]
            (:credit db)))

(rf/reg-sub
 ::review
 (fn-traced [db]
            (:review db)))

(rf/reg-sub
 ::media
 (fn-traced [db]
            (:media db)))
