(ns cinemart.movie.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [reitit.frontend.easy :refer [href]]
            [cinemart.movie.subs :as movie]
            [cinemart.overlay.events :as overlay]
            [cinemart.movie.events :as movie-ev]
            [cinemart.components.container :refer [container]]
            [cinemart.components.review :refer [review]]
            [cinemart.components.forms.schedule :refer [schedule]]
            [clojure.string :as s]
            [cinemart.components.card :refer [card]]
            [cinemart.config :refer [image-link]]))

(defn movie
  []
  (let [{:keys [title overview runtime genres release-date poster_path backdrop_path vote_average id]}
        @(rf/subscribe [::movie/movie])
        role @(rf/subscribe [:cinemart.auth.subs/role])
        {:keys [cast]} @(rf/subscribe [::movie/credit])
        {:keys [results]} @(rf/subscribe [::movie/review])
        {:keys [posters]} @(rf/subscribe [::movie/media])]
    [container
     {:classes ["bg-gray-800" "min-h-screen"]}
     [:<>
      ;; backdrop
      [:div.px-3
       [:img.object-cover.w-full.h-96.object-center.shadow-lg
        {:src backdrop_path
         :style {:filter "brightness(65%)"}}]]
      [:div.flex.p-3.justify-center.flex-col.md:flex-row
       [:div
        {:class ["w-2/3" "-mt-48" "mx-auto"
                 "md:-mt-32" "md:w-auto" "md:ml-8"
                 "lg:-mt-48"]}
        [card
         "text-red-500"
         [:img
          {:class ["max-w-none" "w-full"
                   "md:max-w-xs" "md:w-auto"
                   "lg:max-w-md"]
           :src poster_path}]]]
       ;; movie and description section
       [:div.w-full.text-gray-200.flex.flex-col.px-2.justify-center
        {:class ["md:ml-8",
                 "xl:mx-16"]}
        [:p.font-bold.text-3xl.mt-6 title]
        [:div.text-gray-500.mt-2
         [:span.mr-6.text-lg (str runtime " mins")]
         (for [genre (interpose "," genres)]
           [:span {:key (random-uuid)} (:name genre ", ")])]
        [:p.mt-6.text-gray-400 {:class ["lg:w-10/12"
                                        "w-full"]} overview]
        ; [:div.text-xl.mt-8
        ;  [:span.mr-2 "Rating:"]
        ;  [:span.text-2xl.font-bold vote_average]]
        [:div.mt-12
         [:div.inline-block.mr-2
          (when (= role "manager")
            [card
             "text-indigo-400"
             [:a.bg-indigo-600.text-lg.px-8.py-4.flex
              {:on-click #(rf/dispatch [::overlay/open {:component
                                                        (fn
                                                          []
                                                          [schedule {:movie_id id}])}])}
              "Create Schedule"]])]
         [:div.inline-block.mr-2
          [card
           "text-indigo-400"
           [:a.bg-indigo-600.text-lg.px-8.py-4.flex
            {:href (href :cinemart.router/ticket {:id id})}
            "buy ticket"]]]
         ; [:div.inline-block
         ;  [card
         ;   "text-indigo-400"
         ;   [:a.bg-indigo-600.text-lg.px-8.py-4.flex
         ;    {:on-click #(rf/dispatch [::movie-ev/post-movie {:id id
         ;                                                     :runtime runtime
         ;                                                     :overview overview
         ;                                                     :genres genres
         ;                                                     :title title
         ;                                                     :poster_path (image-link [:poster :lg] poster_path)
         ;                                                     :backdrop_path (image-link [:backdrop :lg] backdrop_path)}])}
         ;    "create movie"]]]
         ]]]
      ; Casts & optional
      ; [:div.px-3.py-5
      ;  [:div.text-indigo-300.text-2xl.ml-8 "Casts"]
      ;  [:div.flex.overflow-x-scroll.overflow-y-visible.mx-8.pt-3.pb-1.text-gray-700.track-current
      ;   (for [dv cast
      ;         :let [{:keys [character name profile_path id]} dv]
      ;         :when (not (nil? profile_path))]
      ;     [:div.w-full.mr-5
      ;      {:key id}
      ;      [card
      ;       "text-indigo-400"
      ;       [:a.relative.text-indigo-100.text-lg.block
      ;        {:href (str "https://www.themoviedb.org/person/"
      ;                    (apply str
      ;                           (interpose
      ;                             "-"
      ;                             (apply conj [id] (s/split name  " ")))))
      ;         :target "_blank"}
      ;        [:img.max-w-lg {:src (image-link [:profile :md] profile_path)}]
      ;        [:div.absolute.bottom-0.left-0.inline-flex.flex-col
      ;         [:div
      ;          [:span.bg-indigo-400.inline-block.m-0.px-1.font-bold name]]
      ;         [:div
      ;          [:span.bg-indigo-400.inline-block.m-0.px-1.text-indigo-200 "as"]]
      ;         [:div
      ;          [:span.bg-indigo-400.inline-block.m-0.px-1 character]]]]]])]]
      ;Media and Reviews
      ; [:div.md:flex.w-full
      ;  [:div.px-1.py-5
      ;   {:class ["w-full"
      ;            "lg:w-3/5"]}
      ;   [:div.text-indigo-300.text-2xl.ml-8.mb-4 "Media"]
      ;   [:div.flex.flex-wrap.ml-8
      ;    (for [poster (take 12 posters)
      ;          :let [path (:file_path poster)]]
      ;      [:div.px-3.pb-3
      ;       {:class ["w-1/3"]}
      ;       [card
      ;        "text-red-400"
      ;        [:img.w-full {:on-click #((rf/dispatch [::overlay/open
      ;                                                {:component
      ;                                                 (fn []
      ;                                                   [:div.shadow-md
      ;                                                    [:img
      ;                                                     {:src (image-link [:poster :og] path)}]])
      ;                                                 :class ["max-w-md"]}]))
      ;                      :src
      ;                      (image-link [:poster :og] path)}]]])]]
      ;  [:div.pl-8.py-5.pr-5
      ;   {:class ["w-full"
      ;            "lg:w-2/5"]}
      ;   [:div.text-indigo-300.text-2xl.mb-4 "Reviews"]
      ;   [:div.flex.flex-col
      ;    (for [rev (take 5 results)
      ;          :let [{:keys [content author id]} rev]]
      ;      [:div.mb-3
      ;       {:key id}
      ;       [review content author]])]]]
]]))


