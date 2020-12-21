(ns cinemart.components.forms.movie
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.input :refer [input select]]
            [cinemart.components.moviediv :refer [movie-div]]
            [cinemart.components.button :refer [button-a button-n]]
            [cinemart.admin.events :as admin-ev]
            [cinemart.config :refer [json-string now iso-time]]))

(defn remove-by-id [v id]
  (filterv #(not= id (-> % :id))
           v))

(defn movie-form [mov]
  (let [
        form (r/atom
               (merge
                 {:runtime 5
                  :genres []
                  :overview "Adipisicing accusamus lorem recusandae perspiciatis neque sequi? Totam nisi obcaecati porro nisi reiciendis, quis Perspiciatis quam reiciendis fuga quisquam unde. Iure quidem quas saepe perferendis adipisci Excepturi in doloribus vitae?"
                  :backdrop_path "https://image.tmdb.org/t/p/w1280/aUVCJ0HkcJIBrTJYPnTXta8h9Co.jpg"
                  :poster_path "https://image.tmdb.org/t/p/w780/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg"
                  :title "Abc" }
                 mov))
        genres @(rf/subscribe [:cinemart.subs/get-in [:admin :genres]])]
    (fn []
      [:div
       [:form
        [:div.px-5
         [:div.flex
          [:div.mr-8
           [input {:type "text"
                   :name "title"
                   :value (:title @form)
                   :on-change #(swap! form assoc :title
                                      (-> % .-target .-value))}
            {:title
             "Enter Title: "
             :class ["inline-block" "w-40" "mr-2"]}]]
          [input
           {:type "number"
            :name "Runtime"
            :class "w-20"
            :value (:runtime @form)
            :on-change #(swap! form assoc :runtime
                               (-> % .-target .-value int))}
           {:title "Runtime: "
            :class [ "inline-block" "mr-2"]}]
          [:div.px-5.ml-8
           (if mov
             [button-a
              "text-indigo-400"
              {:class ["bg-indigo-600" "text-md" "px-2" "py-2" ]
               :on-click
               #(rf/dispatch [::admin-ev/update-movie
                              :movie
                              (-> @form
                                  (dissoc :id)
                                  (dissoc :created_at))
                              (:id @form)])}
              "Update movie"
              ]
             [button-a
              "text-indigo-400"
              {:class ["bg-indigo-600" "text-md" "px-2" "py-2" ]
               :on-click
               #(rf/dispatch [::admin-ev/create :movie @form])}
              "Create movie"
              ])]]
         [:div.flex.items-center.mb-4
          [:label.text-gray-700.font-bold.mr-2.w-40 "Genres:"]
          [select
           {:class ["mr-2"]
            :on-change (fn [e]
                         (let [val (-> e .-target .-value int)
                               pay (nth genres val)
                               gen-set (set (:genres @form))
                               new-set (conj gen-set pay)]
                           (swap! form assoc :genres (vec new-set))))}
           (map-indexed
             (fn [idx itm]
               [:option
                {:value idx
                 :key (:id itm)}
                (:name itm)])
             genres)]
          [:div.flex.flex-nowrap
           (for [genre (:genres @form)]
             [button-n
              {:class ["bg-blue-300" "mr-2" "rounded" "text-gray-700" "inline-block"]
               :on-click #(swap! form assoc :genres (remove-by-id
                                                      (:genres @form)
                                                      (:id genre)))}

              (:name genre)])]]
         [:div.mr-4
          [input
           {:type "text"
            :name "poster_path"
            :class "w-4/6"
            :value (:poster_path @form)
            :on-change #(swap! form assoc :poster_path
                               (-> % .-target .-value ))}
           {:title "Enter Poster url: "
            :class ["inline-block" "mr-2" "w-40"]}]]
         [:div
          [input
           {:type "text"
            :name "backdrop_path"
            :class "w-4/6"
            :value (:backdrop_path @form)
            :on-change #(swap! form assoc :backdrop_path
                               (-> % .-target .-value ))}
           {:title "Enter backdrop url:"
            :class ["inline-block" "mr-2" "w-40"]} ]]
         [:div.flex
          [:label.text-gray-700.font-bold.w-40.mr-2 "Overview:"]
          [:textarea.h-32.rounded {:value (:overview @form)
                                   :class ["w-5/6"]
                                   :on-change #(swap! form assoc :overview
                                                      (-> % .-target .-value))}]]
         ]
        ]

       [:div
        [movie-div @form]]])))
