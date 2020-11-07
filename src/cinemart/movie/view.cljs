(ns cinemart.movie.view
  (:require [re-frame.core :as rf]
            [cinemart.movie.subs :as movie]
            [cinemart.components.container :refer [container]]))

(defn movie
  []
  (let [{:keys [title overview homepage genres release-date]}
        @(rf/subscribe [::movie/movie])]
    [container
     {}
     [:div
      [:p title]
      [:p overview]
      [:a {:href homepage} "homepage"]]]))
