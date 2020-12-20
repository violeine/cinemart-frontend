(ns cinemart.profile.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.forms.user :refer [user-form]]
            [cinemart.components.moviediv :refer [user-div]]
            [cinemart.components.search :refer [search-user]]
            [cinemart.components.button :refer [button-n]]
            [cinemart.profile.subs :as sub]
            [cinemart.config :refer [json-string to-vn-time image-link]]))
(def tab
  [[:schedule "Your Tickets"]
   [:me "Update Profile"]])

(def page (r/atom 0))

(defn profile
  []
  (let [tickets @(rf/subscribe [::sub/get-ticket])
        me @(rf/subscribe [:cinemart.auth.subs/me])]
    [container
     {:classes ["flex" "flex-col"]}
     [:<>
      [:nav.px-5.flex.items-center.mb-2
       [:h2.text-3xl.ml-6.mb-4.mr-auto (get-in tab [@page 1])]
       [:div.mr-5
        (map-indexed
          (fn [idx itm]
            [button-n {:class [(if (= idx @page)
                                 "bg-indigo-400"
                                 "bg-indigo-600") "mr-2" "text-indigo-50"]
                       :on-click #(reset! page idx)}
             (name (first itm))])
          tab
          )]
       ]
      (case (get-in tab [@page 0])
        :me [user-form  {:init-data me
                         :on-submit-fn (fn [payload]
                                         (fn [e]
                                           (.preventDefault e)
                                           (rf/dispatch
                                             [:cinemart.auth.events/update-me payload])))}]
        :schedule
        [:div.px-5
         ^{:key tickets}[search-user tickets]])
      ]]))

