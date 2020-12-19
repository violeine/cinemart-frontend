(ns cinemart.genres.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [reitit.frontend.easy :refer [href]]
            [cinemart.movie.subs :as movie]
            [cinemart.overlay.events :as overlay]
            [cinemart.movie.events :as movie-ev]
            [cinemart.components.container :refer [container]]
            [cinemart.components.review :refer [review]]
            [cinemart.components.forms.schedule :refer [schedule]]
            [cinemart.components.search :refer [search]]
            [cinemart.subs]
            [cinemart.config :refer [image-link json-string]]))

(defn genres
  []
  (let [theater-movie @(rf/subscribe [:cinemart.subs/get-in
                                     [:genres :response :movies]])
        {:keys [name ]} @(rf/subscribe [:cinemart.subs/get-in
                                        [:genres :response :genre]])]
    [container
       {:classes ["flex" "flex-col" "px-5"]}
       [:div.text-white
        [:div.mb-6.flex.items-baseline
         [:p.text-3xl.font-bold.text-indigo-500.mr-auto name] ]
        [:div
         [:h3.text-lg.text-indigo-200.mb-2
          "In this genres"]]
        ^{:key theater-movie}
        [search theater-movie]
        ]]))



