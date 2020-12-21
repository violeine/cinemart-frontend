(ns cinemart.components.moviediv
  (:require [cinemart.config :refer [json-string]]
            [re-frame.core :as rf]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.button :refer [button-n]]
            [cinemart.components.seatmap :refer [seatmap]]
            [cinemart.config :refer [to-vn-time to-vnd]]
            [reitit.frontend.easy :refer [href]]))

(defn movie-div
  [{:keys [id runtime genres overview poster_path backdrop_path title]}]
  [:div.px-3.mx-5.h-64.mb-4.py-2.relative
   [:div.absolute.h-60.mr-2.w-full.z-10.my-2.mx-2.flex
    [:img.h-60.rounded.shadow-md {:src poster_path
                                  :on-click #(rf/dispatch [:cinemart.events/navigate :cinemart.router/movie {:id id}])}]
    [:div.ml-16
     [:a.text-xl.font-bold.text-white {:href (href :cinemart.router/movie {:id id})} title]
     [:div.text-white.mt-2
      [:span.mr-6.text-lg (str runtime " mins")]
      (for [genre (interpose "," genres)]
        [:span {:key (random-uuid)} (:name genre ", ")])]
     [:p.mt-6.text-gray-100.overflow-y-hidden.h-32.overflow-ellipsis {:class ["lg:w-10/12"
                                     "w-full"]} overview]]]
   [:img.object-cover.object-center.rounded-lg.h-64.w-full
    {:style {:filter "brightness(50%)"}
    :src backdrop_path}]])



(defn manager-div
  [{:keys [movie_id movie_runtime movie_genres
           movie_overview movie_poster_path
           movie_backdrop_path movie_title
           reserved_seats nrow ncolumn time
           reserved price]}]
  (let [booked_seat (if (= reserved_seats [nil]) []
                      reserved_seats)
        total-seat (* nrow ncolumn)]
    [:div.px-3.mx-5.h-64.mb-4.py-2.relative
     [:div.absolute.h-60.mr-2.w-full.z-10.my-2.mx-2.flex
      [:img.h-60.rounded.shadow-md {:src movie_poster_path
                                    :on-click #(rf/dispatch
                                                 [:cinemart.events/navigate
                                                  :cinemart.router/movie {:id movie_id}])}]
      [:div.ml-16
       [:a.text-xl.font-bold.text-white {:href (href :cinemart.router/movie {:id movie_id})} movie_title]
       [:div.text-white.mt-2
        [:span.mr-6.text-lg (str movie_runtime " mins")]
        (for [movie_genre (interpose "," movie_genres)]
          [:span {:key (random-uuid)} (:name movie_genre ", ")])]
       [:p.text-xl "Show time:"
        [:span.ml-2
        (to-vn-time time)]]
       [:p.text-lg.mt-2 "Ticket price:" [:span.ml-2 (to-vnd price)]]
       [:p.mt-2 "Reserved seats:"
        [:span.ml-2
         reserved [:span "/"] total-seat]]
       [button-n {:class ["bg-indigo-400" "mt-8" "inline-block"]
                  :on-click #(rf/dispatch
                               [::overlay/open
                                {:component
                                 (fn []
                                   [seatmap
                                    {:nrow nrow
                                     :ncolumn ncolumn
                                     :reserved-seat booked_seat}])}])}
        "show booked seat"]]]
     [:img.object-cover.object-center.rounded-lg.h-64.w-full
      {:style {:filter "brightness(50%)"}
       :src movie_backdrop_path}]]))

(defn user-div
  [{:keys [movie_id movie_runtime movie_genres
           movie_overview movie_poster_path schedule_room
           movie_backdrop_path movie_title seats_name
           schedule_nrow schedule_ncolumn schedule_time
           seats cost theater_name theater_address]}]
  (let [total-seat (* schedule_nrow schedule_ncolumn)]
    [:div.px-3.mx-5.h-64.mb-4.py-2.relative
     [:div.absolute.h-60.mr-2.w-full.z-10.my-2.mx-2.flex
      [:img.h-60.rounded.shadow-md {:src movie_poster_path
                                    :on-click #(rf/dispatch
                                                 [:cinemart.events/navigate
                                                  :cinemart.router/movie {:id movie_id}])}]
      [:div.ml-16
       [:div.flex.items-baseline
        [:a.text-xl.font-bold.text-white.mr-8 {:href (href :cinemart.router/movie {:id movie_id})} movie_title]
        [:div.text-white
         [:span.mr-6.text-lg (str movie_runtime " mins")]
         ]]
       [:p.text-xl "Show time:"
        [:span.ml-2
         (to-vn-time schedule_time)]]
       [:p.text-xl "Your seats: " (str seats_name)]
       [:div.flex.mt-2
        [:p.text-xl "Location: "
         [:span theater_name]]
        [:p.text-xl.ml-4 "Address: "
         [:span theater_address]]
        [:p.text-xl.ml-4 "Room "
         [:span schedule_room]]]
       [:p.text-lg.mt-2 "Total price:" [:span.ml-2 (to-vnd cost)]]
       [button-n {:class ["bg-indigo-400" "mt-8" "inline-block"]
                  :on-click #(rf/dispatch
                               [::overlay/open
                                {:component
                                 (fn []
                                   [seatmap
                                    {:nrow schedule_nrow
                                     :ncolumn schedule_ncolumn
                                     :your-seat seats}])}])}
        "show booked seat"]]]
     [:img.object-cover.object-center.rounded-lg.h-64.w-full
      {:style {:filter "brightness(50%)"}
       :src movie_backdrop_path}]]))
