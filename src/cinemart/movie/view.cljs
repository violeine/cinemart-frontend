(ns cinemart.movie.view
  (:require [re-frame.core :as rf]
            [cinemart.movie.subs :as movie]
            [cinemart.components.container :refer [container]]
            [cinemart.components.card :refer [card]]
            [cinemart.config :refer [image-link]]))

(defn movie
  []
  (let [{:keys [title overview runtime genres release-date poster_path backdrop_path vote_average]}
        @(rf/subscribe [::movie/movie])]
    [container
     {:classes ["bg-gray-800" "h-screen"]}
     [:<>
      [:div.px-3
       [:img.object-cover.w-full.h-96.object-center.shadow-lg
        {:src (image-link [:backdrop :lg] backdrop_path)
         :style {:filter "brightness(65%)"}}]]

      [:div.flex.p-3.justify-center
       [:div.ml-8.transform.-translate-y-48.shadow-2xl
        [card
         "red-500"
         [:img.w-full {:src (image-link [:poster :lg] poster_path)}]]]
       [:div.ml-32.mr-16.w-full.text-gray-200
        [:p.font-bold.text-3xl.mt-6 title]
        [:div.text-gray-500.mt-2
         [:span.mr-6.text-lg (str runtime " mins")]
         (for [genre (interpose "," genres)]
           [:span (:name genre ", ")])]
        [:p.mt-6.text-gray-400 {:class ["w-10/12"]} overview]
        [:div.text-xl.mt-8
         [:span.mr-2 "Rating:"]
         [:span.text-2xl.font-bold vote_average]]
        [:div.mt-12
         [:div.inline-block
          [card
           "indigo-400"
           [:a.bg-indigo-600.text-lg.px-8.py-4.flex "buy ticket"]]]]]]]]))
