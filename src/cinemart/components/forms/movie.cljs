(ns cinemart.components.forms.movie
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.input :refer [input]]
            [cinemart.components.moviediv :refer [movie-div]]
            [cinemart.components.button :refer [button-a]]
            [cinemart.manager.events :as manager-ev]
            [cinemart.config :refer [json-string now iso-time]]))

(defn movie-form [mov]
  (let [
        form (r/atom
               (merge
                 {:runtime 5
                  :genres [{:id 37, :name "Western"}]
                  :overview "Adipisicing accusamus lorem recusandae perspiciatis neque sequi? Totam nisi obcaecati porro nisi reiciendis, quis Perspiciatis quam reiciendis fuga quisquam unde. Iure quidem quas saepe perferendis adipisci Excepturi in doloribus vitae?"
                  :backdrop_path "https://image.tmdb.org/t/p/w1280/aUVCJ0HkcJIBrTJYPnTXta8h9Co.jpg"
                  :poster_path "https://image.tmdb.org/t/p/w780/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg"
                  :title "Abc" }
                 mov))]
    (fn []
      [:div
       [:form
        [:div.px-5
         [:div
          [input {:type "text"
                  :name "title"
                  :value (:title @form)
                  :on-change #(swap! form assoc :title
                                     (-> % .-target .-value))}
           {:title
            "Enter Title: " }]]
         [:div
          [input
           {:type "text"
            :name "backdrop_path"
            :value (:backdrop_path @form)
            :on-change #(swap! form assoc :backdrop_path
                               (-> % .-target .-value ))}
           {:title "Enter backdrop url:"} ]]

         [:div.mr-4
          [input
           {:type "text"
            :name "poster_path"
            :value (:poster_path @form)
            :on-change #(swap! form assoc :poster_path
                               (-> % .-target .-value ))}
           {:title "Enter Poster url:"
            :class ["inline-block"]}]]
         [input
          {:type "number"
           :name "Runtime"
           :value (:runtime @form)
           :on-change #(swap! form assoc :runtime
                              (-> % .-target .-value int))}
          {:title "Runtime: "
           :class [ "inline-block"]}]
         [:textarea.w-full.h-32 {:value (:overview @form)
                                 :on-change #(swap! form assoc :overview
                                                    (-> % .-target .-value))}]
         ]
        ]
       [:div.px-5
        [button-a
         "text-indigo-400"
         {:class ["bg-indigo-600" "text-md" "px-2" "py-2" ]
          :on-click
          #(rf/dispatch [::manager-ev/create-schedule
                         (-> @form
                             (dissoc :time)
                             (assoc :time (iso-time
                                            (:time @form))))])}
         "Create schedule"
         ]]
       [:div
        [movie-div @form]]])))
