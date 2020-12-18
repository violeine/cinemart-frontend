(ns cinemart.home.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [cinemart.home.subs :as home]
            [cinemart.config :refer [json-string]]))

(defn home-page [{:keys [classes]}]
  (let [home @(rf/subscribe [::home/home])]
    [container {:classes ["flex" "flex-col" "justify-around"]}
     [:pre.text-white
      (json-string home)]]))
