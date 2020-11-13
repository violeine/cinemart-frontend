(ns cinemart.components.review
  (:require [reagent.core :as r]
            [clojure.string :as s]))

(defn review
  [content author]
  (let [more (r/atom false)
        shorten (take 80 (s/split content " "))]
    (fn [content author]
      [:div.bg-indigo-300.py-2.px-2.rounded.shadow-md
       [:div.mb-2.text-gray-900.bg-indigo-500.z-50.inline-block.rounded.shadow.px-2.py-1.ml-1
        author]
       [:div.px-3.py-5.bg-indigo-100.rounded.shadow-lg.-mt-5.bg-opacity-50.text-sm.text-gray-900
        [:div.overflow-hidden
         (if (>= (count (s/split content " ")) 90)
           (if @more
             [:span
              content " "
              [:a.text-gray-700  {:on-click #(swap! more not)} "show less"]]
             [:span (apply str (interpose " " shorten)) "... "
              [:a.text-gray-700  {:on-click #(swap! more not)} "show more"]])
           content)]]])))

