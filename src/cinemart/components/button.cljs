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
