(ns cinemart.movie.subs
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))

(rf/reg-sub
 ::movie
 (fn-traced [db]
            (:movie db)))
