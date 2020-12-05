(ns cinemart.effects
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [reitit.frontend.easy :as rfe]))

(rf/reg-fx
 ::navigate!
 (fn-traced [route]
            (apply rfe/push-state route)))

(rf/reg-fx
 ::back!
 (fn-traced []
            (.go js/window.history -1)))

(rf/reg-fx
 ::save-storage!
 (fn-traced [[key val]]
            (.setItem (.-localStorage js/window) key val)))

(rf/reg-fx
 ::get-storage!
 (fn-traced [key]
            (.getItem (.-localStorage js/window) key)))

(rf/reg-fx
 ::remove-key-storage!
 (fn-traced [key]
            (.removeItem (.-localStorage js/window) key)))

(rf/reg-fx
 ::clear-storage!
 (fn-traced []
            (.clear (.-localStorage js/window))))

(rf/reg-cofx
 ::init-storage
 (fn-traced
  []
  (let
   [user-string (.getItem (.-localStorage js/window) "user")
    user (js->clj (.parse js/JSON user-string) :keywordize-keys true)]
    {:user user})))

