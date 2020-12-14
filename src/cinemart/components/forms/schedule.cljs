(ns cinemart.components.forms.schedule
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.config :refer [json-string now]]))

(defn schedule [{:keys [movie_id]}]
  (let [
        form (r/atom {:row 5
                      :col 12
                      :time (now)
                      :movie_id movie_id
                      :price 50000 })]
    (fn []
      [:div
       [:form
        [:label
         [:span "price"]
         [:input {:type "number"
                  :name "row"
                  :value (:price @form)
                  :min 50000
                  :max 100000
                  :on-change #(swap! form assoc :price
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "row"]
         [:input {:type "number"
                  :name "row"
                  :value (:row @form)
                  :min 5
                  :max 10
                  :on-change #(swap! form assoc :row
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "col"]
         [:input {:type "number"
                  :name "col"
                  :min 8
                  :max 15
                  :value (:col @form)
                  :on-change #(swap! form assoc :col
                                     (-> % .-target .-value int))}]]
        [:label
         [:span "time"]
         [:input {:name "time"
                  :type "datetime-local"
                  :value (:time @form)
                  :on-change #(swap! form assoc :time (-> % .-target .-value))}]]]
       [:a.px-2.py-2 {:on-click #(print @form)} "Create"]
       [:div
        [seatmap (dissoc @form :time)]]])))

