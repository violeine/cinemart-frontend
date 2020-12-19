(ns cinemart.components.moviediv
  (:require [cinemart.config :refer [json-string]]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]))

(defn movie-div
  [{:keys [id runtime genres overview poster_path backdrop_path title]}]
  [:div.px-3.mx-5.h-64.mb-4.py-2.relative
   [:div.absolute.h-60.mr-2.w-full.z-20.my-2.mx-2.flex
    [:img.h-60.rounded.shadow-md {:src poster_path
                                  :on-click #(rf/dispatch [:cinemart.events/navigate :cinemart.router/movie {:id id}])}]
    [:div.ml-16
     [:a.text-xl.font-bold.text-white {:href (href :cinemart.router/movie {:id id})} title]
     [:div.text-white.mt-2
      [:span.mr-6.text-lg (str runtime " mins")]
      (for [genre (interpose "," genres)]
        [:span {:key (random-uuid)} (:name genre ", ")])]
     [:p.mt-6.text-gray-100.overflow-y-hidden.h-32.overflow-ellipsis {:class ["lg:w-10/12"
                                     "w-full"]} overview]]]
   [:img.object-cover.object-center.rounded-lg.h-64.w-full
    {:style {:filter "brightness(50%)"}
     :src backdrop_path}]])


