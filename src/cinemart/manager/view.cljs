(ns cinemart.manager.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.events :as overlay]
            [cinemart.manager.events :as manager-ev]
            [cinemart.components.forms.theater :refer [theater-form]]
            [cinemart.manager.subs :as sub]
            [cinemart.config :refer [json-string]]))

(defn manager []
  (let [theater-data @(rf/subscribe [::sub/theater])]
    [container {:classes ["flex" "flex-col"]}
     [:<>
      [:h2.text-3xl.ml-6.mb-16 "managers panel"]
      [theater-form {:init-data {:theater (-> theater-data
                                              (dissoc :id)
                                              (dissoc :created_at)
                                              )}
                     :on-submit-fn (fn [payload]
                                     (fn [e]
                                       (.preventDefault e)
                                       (rf/dispatch [::manager-ev/update :theater
                                                     payload
                                                     {:id (:id theater-data)}])))}]]]))





