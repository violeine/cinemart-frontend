(ns cinemart.components.forms.schedule
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.input :refer [input]]
            [cinemart.components.button :refer [button-a]]
            [cinemart.manager.events :as manager-ev]
            [cinemart.config :refer [json-string now iso-time]]))

(defn schedule [{:keys [movie_id]}]
  (let [
        form (r/atom {:nrow 5
                      :ncolumn 12
                      :room 10
                      :time (now)
                      :movie movie_id
                      :price 50000 })]
    (fn []
      [:div
       [:form
        [:div.px-5
         [:div.flex
          [:div
           {:class ["w-1/2"]}
           [input {:type "number"
                   :name "room"
                   :value (:room @form)
                   :min 0
                   :max 10
                   :on-change #(swap! form assoc :room
                                      (-> % .-target .-value int))}
            {:title
             "Enter Room Number:"
             :class [ "inline-block"]}]]
          [:div
           {:class ["w-1/2"]}
           [input
            {:type "number"
             :name "price"
             :value (:price @form)
             :min 50000
             :max 100000
             :on-change #(swap! form assoc :price
                                (-> % .-target .-value int))}
            {:title "Enter Price:"
             :class [ "w-24" "inline-block"]
             } ]]
          ]
         [:div.flex
          [:div.flex
           {:class ["w-1/2"]}
           [:div.mr-4
            [input
             {:type "number"
              :name "row"
              :value (:nrow @form)
              :min 5
              :max 10
              :on-change #(swap! form assoc :nrow
                                 (-> % .-target .-value int))}
             {:title "Row:"
              :class ["inline-block"]}]]
           [input
            {:type "number"
             :name "col"
             :min 8
             :max 15
             :value (:ncolumn @form)
             :on-change #(swap! form assoc :ncolumn
                                (-> % .-target .-value int))}
            {:title "Column:"
             :class [ "inline-block"]}]]
          [:div
           {:class ["w-1/2"]}
           [input
            {:name "time"
             :type "datetime-local"
             :value (:time @form)
             :on-change #(swap! form assoc :time (-> % .-target .-value))}
            {:title "Time:"
             :class ["w-24" "inline-block"]}
            ]]]]
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
        [seatmap (dissoc @form :time)]]])))

