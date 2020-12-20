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
            [cinemart.components.moviediv :refer [manager-div]]
            [cinemart.manager.subs :as sub]
            [cinemart.config :refer [json-string today to-vn-time image-link]]))

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
            [{:keys [reserved_seats nrow ncolumn time
                     movie_title movie_poster_path id price]} schedule
             max-seats (* nrow ncolumn)
             booked_seat (if (= reserved_seats [nil]) []
                           reserved_seats)
             ]]
        ^{:key id}
        [manager-div schedule]
        )
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
      [admin-form {:type :manager
                   :init-data me
                   :on-submit-fn (fn [payload]
                                   (fn [e]
                                     (.preventDefault e)
                                     (rf/dispatch
                                       [:cinemart.auth.events/update-me payload])))}]]]))




