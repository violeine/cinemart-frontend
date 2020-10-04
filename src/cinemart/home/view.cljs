(ns cinemart.home.view
  (:require [cinemart.components.container :refer [container]]))

(defn home-page [{:keys [classes]}]
  [container {:classes ["flex" "flex-col" "justify-around"]}
   [:p.p-16.bg-blue-300.mb-2 "this is a home pages"]])
