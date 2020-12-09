(ns cinemart.components.forms.schedule
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.config :refer [json-string]]))

(defn schedule []
  (let [form (r/atom {:row 5
                      :col 12
                      :time
                      (.replace
                       (.toISOString
                        (new js/Date))
                       "Z" "")
                      :seat-option ([2 "20000"]
                                    [3 "30000"]
                                    [5 "50000"])})]
    (fn []
      [:div
       [:form
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
       [:div
        [seatmap (dissoc @form :time)]]])))

(partition 2 1 [1 2 3])
