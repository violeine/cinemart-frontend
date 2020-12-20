(ns cinemart.manager.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.events :as overlay]
            [cinemart.manager.events :as manager-ev]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.components.forms.theater :refer [theater-form]]
            [cinemart.components.forms.admin :refer [admin-form]]
            [cinemart.components.search :refer [search-manager]]
            [cinemart.components.button :refer [button-n]]
            [cinemart.manager.subs :as sub]
            [cinemart.config :refer [json-string today to-vn-time image-link]]))

(def tab
  [[:schedule "Your Schedules"]
   [:theater "Update Theater"]
   [:me "Update Profile"]])

(def page (r/atom 0))



(defn manager []
  (let [theater-data @(rf/subscribe [::sub/theater])
        schedules @(rf/subscribe [::sub/schedules])
        me @(rf/subscribe [:cinemart.auth.subs/me])]
    [container {:classes ["flex" "flex-col"]}
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
      ; [:pre (json-string schedules)]
      (case (get-in tab [@page 0])
        :schedule [:div.px-5
                   ^{:key schedules}[search-manager schedules]]
        :theater
        ^{:key theater-data}
        [theater-form {:init-data {:theater (-> theater-data
                                                (dissoc :id)
                                                (dissoc :created_at)
                                                )}
                       :on-submit-fn (fn [payload]
                                       (fn [e]
                                         (.preventDefault e)
                                         (rf/dispatch [::manager-ev/update :theater
                                                       payload
                                                       {:id (:id theater-data)}])))}]
        :me
        [admin-form {:type :manager
                     :init-data me
                     :on-submit-fn (fn [payload]
                                     (fn [e]
                                       (.preventDefault e)
                                       (rf/dispatch
                                         [:cinemart.auth.events/update-me payload])))}])]]))




