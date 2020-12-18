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
            [cinemart.manager.subs :as sub]
            [cinemart.config :refer [json-string to-vn-time image-link]]))

(defn manager []
  (let [theater-data @(rf/subscribe [::sub/theater])
        schedules @(rf/subscribe [::sub/schedules])
        me @(rf/subscribe [:cinemart.auth.subs/me])]
    [container {:classes ["flex" "flex-col"]}
     [:<>
      [:h2.text-3xl.ml-6.mb-16 "managers panel"]
      ; [:pre (json-string schedules)]
      (for [schedule schedules
            :let
            [{:keys
              [ reserved_seats nrow ncolumn time
               movie_title movie_poster_path id price ]}
             schedule
             max-seats (* nrow ncolumn)
             booked_seat (if (= reserved_seats [nil]) []
                           reserved_seats)
             ]]
        [:div.flex
         {:key id}
         [:img {:src (image-link [:poster :xsm] movie_poster_path)}]
         [:span.text-white.mr-2 movie_title]
         [:span.text-white.mr-2 max-seats]
         [:span.text-white.mr-2 price]
         [:span.text-white.mr-2 (to-vn-time time)]
         [:div
          [:a.p-2.bg-green-300 {:on-click
                                #(rf/dispatch
                                   [::overlay/open
                                    {:component
                                     (fn []
                                       [seatmap
                                        {:nrow nrow
                                         :ncolumn ncolumn
                                         :reserved-seat booked_seat
                                         }])}])}
           "view booked seats"]]])
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
      [admin-form {:type :manager
                   :init-data me
                   :on-submit-fn (fn [payload]
                                   (fn [e]
                                     (.preventDefault e)
                                     (rf/dispatch
                                       [:cinemart.auth.events/update-me payload])))}]]]))




