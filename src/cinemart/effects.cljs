(ns cinemart.effects
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [reitit.frontend.easy :as rfe]))

(rf/reg-fx
 ::navigate!
 (fn-traced [route]
            (apply rfe/push-state route)))
