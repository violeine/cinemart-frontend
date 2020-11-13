(ns cinemart.movie.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.movie.subs :as movie]
            [cinemart.components.container :refer [container]]
            [cinemart.components.review :refer [review]]
            [clojure.string :as s]
            [cinemart.components.card :refer [card]]
            [cinemart.config :refer [image-link]]))

(def poster-custom (r/atom nil))
(defn movie
  []
  (let [{:keys [title overview runtime genres release-date poster_path backdrop_path vote_average]}
        @(rf/subscribe [::movie/movie])
        {:keys [cast]} @(rf/subscribe [::movie/credit])
        {:keys [results]} @(rf/subscribe [::movie/review])
        {:keys [posters]} @(rf/subscribe [::movie/media])]
    [container
     {:classes ["bg-gray-800" "h-screen"]}
     [:<>
      [:div.px-3
       [:img.object-cover.w-full.h-96.object-center.shadow-lg
        {:src (image-link [:backdrop :lg] backdrop_path)
         :style {:filter "brightness(65%)"}}]]
      [:div.flex.p-3.justify-center
       [:div.ml-8.-mt-48
        {:class ["w-1/2"]}
        [card
         "red-500"
         [:img.w-full.shadow-lg {:src (image-link [:poster :lg] (if (nil? @poster-custom)
                                                                  poster_path
                                                                  @poster-custom))}]]]
       [:div.ml-16.mr-16.w-full.text-gray-200.flex.flex-col
        {:class ["w-1/2"]}
        [:p.font-bold.text-3xl.mt-6 title]
        [:div.text-gray-500.mt-2
         [:span.mr-6.text-lg (str runtime " mins")]
         (for [genre (interpose "," genres)]
           [:span {:key (random-uuid)} (:name genre ", ")])]
        [:p.mt-6.text-gray-400 {:class ["w-10/12"]} overview]
        [:div.text-xl.mt-8
         [:span.mr-2 "Rating:"]
         [:span.text-2xl.font-bold vote_average]]
        [:div.mt-12
         [:div.inline-block
          [card
           "indigo-400"
           [:a.bg-indigo-600.text-lg.px-8.py-4.flex "buy ticket"]]]]]]
      [:div.px-3.py-5
       [:div.text-indigo-300.text-2xl.ml-8 "Casts"]
       [:div.flex.overflow-x-scroll.overflow-y-visible.mx-8.pt-3.pb-1.text-gray-700.track-current
        (for [dv cast
              :let [{:keys [character name profile_path id]} dv]
              :when (not (nil? profile_path))]
          [:div.w-full.mr-5
           {:key id}
           [card
            "indigo-400"
            [:a.relative.text-indigo-100.text-lg.block
             {:href (str "https://www.themoviedb.org/person/"
                         (apply str
                                (interpose
                                 "-"
                                 (apply conj [id] (s/split name  " ")))))
              :target "_blank"}
             [:img.max-w-lg {:src (image-link [:profile :md] profile_path)}]
             [:div.absolute.bottom-0.left-0.inline-flex.flex-col
              [:div
               [:span.bg-indigo-400.inline-block.m-0.px-1 name]]
              [:div
               [:span.bg-indigo-400.inline-block.m-0.px-1 "as"]]
              [:div
               [:span.bg-indigo-400.inline-block.m-0.px-1.font-bold character]]]]]])]]
      [:div.flex.w-full.flex-1
       [:div.px-1.py-5
        {:class ["w-3/5"]}
        [:div.text-indigo-300.text-2xl.ml-8.mb-4 "Media"]
        [:div.flex.flex-wrap.ml-8
         (for [poster (take 12 posters)
               :let [path (:file_path poster)]]
           [:div.mr-4.mb-4
            [card
             "red-400"
             [:img.max-w-none.w-48 {:on-click #(reset! poster-custom path)
                                    :src
                                    (image-link [:poster :og] path)}]]])]]
       [:div.pl-8.py-5.pr-5
        {:class ["w-2/5"]}
        [:div.text-indigo-300.text-2xl.mb-4 "Reviews"]
        [:div.flex.flex-col
         (for [rev (take 5 results)
               :let [{:keys [content author id]} rev]]
           [:div.mb-3
            {:key id}
            [review content author]])]]]]]))



