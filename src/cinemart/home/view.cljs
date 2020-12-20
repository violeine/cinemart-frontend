(ns cinemart.home.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [cinemart.home.subs :as home]
            [reitit.frontend.easy :refer [href]]
            [cinemart.components.button :refer [button-a]]
            [cinemart.components.lists :refer [lists]]
            [cinemart.config :refer [json-string]]))

(defn home-page [{:keys [classes]}]
  (let [latest-db @(rf/subscribe [::home/get-home :latest-db])
        theaters @(rf/subscribe [::home/get-home :theaters])
        latest-schedule @(rf/subscribe [::home/get-home :latest-schedule])
        genres @(rf/subscribe [::home/get-home :genres])]
    [container {:classes ["flex" "flex-col" "justify-around"]}
     [:<>
      [:div
       [:h2.text-3xl.ml-5 "On Theaters"]
       (when
         (not (empty?  (:day latest-schedule)))
         [lists "Today" (:day latest-schedule)])
       (when (not (empty? (:week latest-schedule)))
         [lists "This Week" (:week latest-schedule)])
       (when (not (empty? (:month latest-schedule)))
         [lists "This Month" (:month latest-schedule)])]
      [:div
       [:h2.text-3xl.ml-5 "Theaters"]
       [:div.mx-8.px-3
        (for [theater theaters]
          [:div.inline-block.mr-2
           [button-a "text-indigo-400"
            {:href (href :cinemart.router/theater {:id (:id theater)})
             :class ["mt-2" "px-8" "py-3" "bg-indigo-700"  ]}
            (:name theater)]])]]
      [:div
       [:h2.text-3xl.ml-5 "By Genres"]
       (map-indexed
         (fn [idx {:keys [id name]}]
           [lists name (get-in genres [:movies idx]) id]
           )
         (:genres genres))]
      [:div
       [:h2.text-3xl.ml-5 "Latest in databases"]
       [lists "Database" latest-db]]
      ]]))
