(ns cinemart.components.input
  (:require [reagent.core :as r]))

(defn input
  [prop {:keys [title class]}]
  [:label.block.text-gray-700.mb-3
   [:span.font-bold.mb-1.mr-2
    {:class class} title]
   [:input.border.border-gray-300.rounded.px-3.py-2.leading-tight.text-gray-700
    prop]])


