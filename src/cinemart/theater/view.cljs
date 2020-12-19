(ns cinemart.theater.view
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

(defn theater
  []
  (let [theater-movie @(rf/subscribe [:cinemart.subs/get-in
                                      [:theater :movies :response :all]])]
    [container
     {:classes ["flex" "flex-col" "px-5"]}
     [:<>
      [search theater-movie]
      ]]))



