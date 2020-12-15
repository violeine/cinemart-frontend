(ns cinemart.components.forms.schedule
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
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
        [:label
         [:span "room"]
         [:input {:type "number"
                  :name "room"
                  :value (:room @form)
                  :min 0
                  :max 10
                  :on-change #(swap! form assoc :room
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "price"]
         [:input {:type "number"
                  :name "price"
                  :value (:price @form)
                  :min 50000
                  :max 100000
                  :on-change #(swap! form assoc :price
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "row"]
         [:input {:type "number"
                  :name "row"
                  :value (:nrow @form)
                  :min 5
                  :max 10
                  :on-change #(swap! form assoc :nrow
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "col"]
         [:input {:type "number"
                  :name "col"
                  :min 8
                  :max 15
                  :value (:ncolumn @form)
                  :on-change #(swap! form assoc :ncolumn
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "time"]
         [:input {:name "time"
                  :type "datetime-local"
                  :value (:time @form)
                  :on-change #(swap! form assoc :time (-> % .-target .-value))}]]]
       [:a.px-2.py-2 {:on-click #(rf/dispatch [::manager-ev/create-schedule
                                               (-> @form
                                                   (dissoc :time)
                                                   (assoc :time (iso-time
                                                                  (:time @form))))])}
        "Create"]
       [:div
        [seatmap (dissoc @form :time)]]])))

