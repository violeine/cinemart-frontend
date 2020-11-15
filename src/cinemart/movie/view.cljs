(ns cinemart.movie.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.movie.subs :as movie]
            [cinemart.components.container :refer [container]]
            [cinemart.components.review :refer [review]]
            [cinemart.config :refer [media-queries css]]
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
     {:classes ["bg-gray-800"]}
     [:<>
      [:div.px-3
       [:img.object-cover.w-full.h-96.object-center.shadow-lg
        {:src (image-link [:backdrop :lg] backdrop_path)
         :style {:filter "brightness(65%)"}}]]
      [:div.flex.p-3.justify-center.flex-col.md:flex-row
       [:div
        {:class (css ["w-2/3" "-mt-48" "mx-auto"
                      (media-queries "md:" ["-mt-32" "w-auto" "ml-8"])
                      (media-queries "lg:" ["-mt-48"])])}
        [card
         "red-500"
         [:img
          {:class (css ["max-w-none" "w-full"
                        (media-queries "md:" ["max-w-xs" "w-auto"])
                        (media-queries "lg:" ["max-w-md"])])
           :src (image-link [:poster :lg] (if (nil? @poster-custom)
                                            poster_path
                                            @poster-custom))}]]]
       [:div.w-full.text-gray-200.flex.flex-col.px-2.justify-center
        {:class (css [(media-queries "md:" ["ml-8"])
                      (media-queries "xl:" ["mx-16"])])}
        [:p.font-bold.text-3xl.mt-6 title]
        [:div.text-gray-500.mt-2
         [:span.mr-6.text-lg (str runtime " mins")]
         (for [genre (interpose "," genres)]
           [:span {:key (random-uuid)} (:name genre ", ")])]
        [:p.mt-6.text-gray-400 {:class ["lg:w-10/12"
                                        "w-full"]} overview]
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
               [:span.bg-indigo-400.inline-block.m-0.px-1.font-bold name]]
              [:div
               [:span.bg-indigo-400.inline-block.m-0.px-1.text-indigo-200 "as"]]
              [:div
               [:span.bg-indigo-400.inline-block.m-0.px-1 character]]]]]])]]
      [:div.md:flex.w-full
       [:div.px-1.py-5
        {:class ["w-full"
                 "lg:w-3/5"]}
        [:div.text-indigo-300.text-2xl.ml-8.mb-4 "Media"]
        [:div.flex.flex-wrap.ml-8
         (for [poster (take 12 posters)
               :let [path (:file_path poster)]]
           [:div.px-3.pb-3
            {:class ["w-1/3"]}
            [card
             "red-400"
             [:img.w-full {:on-click #(reset! poster-custom path)
                           :src
                           (image-link [:poster :og] path)}]]])]]
       [:div.pl-8.py-5.pr-5
        {:class ["w-full"
                 "lg:w-2/5"]}
        [:div.text-indigo-300.text-2xl.mb-4 "Reviews"]
        [:div.flex.flex-col
         (for [rev (take 5 results)
               :let [{:keys [content author id]} rev]]
           [:div.mb-3
            {:key id}
            [review content author]])]]]]]))



