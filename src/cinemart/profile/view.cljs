(ns cinemart.profile.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.forms.user :refer [user-form]]
            [cinemart.profile.subs :as sub]
            [cinemart.config :refer [json-string to-vn-time image-link]]))

(defn profile
  []
  (let [tickets @(rf/subscribe [::sub/get-ticket])
        me @(rf/subscribe [:cinemart.auth.subs/me])]
    [container
     {:classes ["flex" "flex-col"]}
     [:<>
      [:div "profile"]
      [user-form  {:init-data me
                   :on-submit-fn (fn [payload]
                                   (fn [e]
                                     (.preventDefault e)
                                     (rf/dispatch
                                       [:cinemart.auth.events/update-me payload])))}]
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

