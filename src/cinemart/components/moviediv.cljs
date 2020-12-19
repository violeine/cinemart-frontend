(ns cinemart.components.moviediv
  (:require [cinemart.config :refer [json-string]]))

(defn movie-div
  [{:keys [id runtime genres overview poster_path backdrop_path title]}]
  [:div.px-3.mx-5.h-64.mb-4.py-2.relative
   [:div.absolute.h-60.mr-2.w-full.z-20.my-2.mx-2.flex
    [:img.h-60.rounded {:src poster_path}]
    [:div.ml-16
     [:p.text-xl.font-bold.text-white title]
     [:div.text-white.mt-2
         [:span.mr-6.text-lg (str runtime " mins")]
         (for [genre (interpose "," genres)]
           [:span {:key (random-uuid)} (:name genre ", ")])]
     [:p.mt-6.text-gray-100 {:class ["lg:w-10/12"
                                        "w-full"]} overview]]]
   [:img.object-cover.object-center.rounded-lg.h-64.w-full
    {:style {:filter "brightness(50%)"}
     :src backdrop_path}]])


