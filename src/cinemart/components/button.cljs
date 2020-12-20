(ns cinemart.components.button
  (:require [cinemart.components.card :refer [card]]))


(defn button-a
  [accent prop title]
  [:div.inline-block
   [card
    accent
    [:a.flex
     prop
     title]]])

(defn button-outline
  [prop title]
  [:a.rounded.border-solid.border-current.border-2.p-2
   prop
   title])


(defn button-n
  [prop title]
  [:a.rounded.px-5.py-2
   prop
   title])


