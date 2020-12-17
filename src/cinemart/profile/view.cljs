(ns cinemart.profile.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.profile.subs :as sub]
            [cinemart.config :refer [json-string to-vn-time image-link]]))

(defn profile
  []
  (let [tickets @(rf/subscribe [::sub/get-ticket])]
    [container
     {:classes ["flex" "flex-col"]}
     [:<>
      [:div "profile"]
      [:pre.bg-yellow-200.text-black (json-string tickets)]
      (for [ticket tickets
          :let [{:keys [seats]} ticket]]
        [:a.mt-3.py-2.px-3.bg-blue-300.rounded.mr-2
         {:on-click #(rf/dispatch [::overlay/open {:component
                                                   (fn
                                                     []
                                                     [seatmap
                                                      {:nrow 8
                                                       :ncolumn 12
                                                       :your-seat seats}])}])}
         "You are here"]
      )]]))

