(ns cinemart.ticket.view
  (:require [cinemart.components.container :refer [container]]
            [cinemart.ticket.subs :as sub]
            [cinemart.ticket.events :as ticket-ev]
            [cinemart.config :refer [image-link json-string to-vn-time to-readable-seat to-vnd]]
            [cinemart.components.seatmap :refer [seatmap]]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def select-schedule (r/atom nil))

(def week-day ["Sun" "Mon" "Tue" "Wed" "Thu" "Fri" "Sat"])

(defn ticket []
  (let [sub-movie (rf/subscribe [:cinemart.movie.subs/movie])
        theaters (rf/subscribe [::sub/get :theaters])
        schedules (rf/subscribe [::sub/get-schedules])
        your-seat (r/atom [])
        seatname (r/atom [])]
    (fn []
      (let
        [{:keys [title overview backdrop_path runtime poster_path id]} @sub-movie ]
        [container {:classes ["flex" "flex-col"]}
         [:<>
          [:h3.text-3xl.font-bold.pl-4 "ticket"]
          [:div.flex
           [:div.mt-8.flex.flex-col.pl-2
            {:class ["w-10/12"]}
            [:div.relative
             [:div.absolute.z-20.top-8.left-8
              [:p.text-3xl.font-bold.mb-2 title]
              [:p.text-lg.text-gray-200.mb-4.ml-2 runtime [:span " mins"]]
              [:p.text-md.text-gray-100
               {:class ["w-1/2"]} overview]]
             [:img.object-cover.w-full.object-center.shadow-lg.h-96.rounded-lg.mb-8
              {:src backdrop_path
               :style {:filter "brightness(50%)"}}]]
            [:div.text-black.ml-8
             {:class ["xl:w-10/12 w-9/12"]}
             (if
               (not (empty?  @theaters))
               [:select.rounded.px-5.w-64
                {:on-change #(rf/dispatch [::ticket-ev/get-schedules (-> % .-target .-value) id])}
                [:option {:selected true
                          :disabled true
                          :hidden true} "Chose your theater"]
                (for [theater @theaters
                      :let [{:keys [id name]} theater]]
                  [:option {:key id
                            :value id}
                   name])]
               [:p.text-3xl.text-gray-100.mt-4 "No schedules for this movie yet :("])
             (when @schedules
               (if (not (empty? @schedules))
                 [:div.flex.bg-indigo-400.py-2.mt-4.justify-center.overflow-x-auto
                  (map-indexed (fn [idx {:keys [id time price]}]
                                 (let [local-time (new js/Date time)
                                       day (nth week-day (.getDay local-time))
                                       date (str (.getDate local-time) "-" (inc (.getMonth local-time)))
                                       t (str (.getHours local-time) ":" (.getMinutes local-time))]
                                   [:div.mr-2.bg-indigo-300.rounded.text-center.p-5
                                    {:key id
                                     :on-click #( reset! select-schedule (nth @schedules idx))}
                                    [:div day]
                                    [:div date]
                                    [:div t]
                                    [:div (to-vnd price)]
                                    ]))
                               (reverse
                                 @schedules))]
                 [:p.text-3xl.text-gray-100.mt-4 "Sorry, come back later!"]))

             (when @select-schedule
               (let [
                     {:keys [reserved_seats
                             nrow ncolumn]}
                     @select-schedule]
                 [
                  seatmap
                  {:reserved-seat reserved_seats
                   :nrow nrow
                   :ncolumn ncolumn
                   :your-seat @your-seat
                   :on-click-fn
                   (fn [d1 name-seat]
                     (swap! your-seat conj d1)
                     (swap! seatname conj name-seat))
                   :on-delete-fn (fn [d1 name-seat]
                                   (swap! seatname
                                          (partial filterv
                                                   #(not= name-seat %)))
                                   (swap! your-seat
                                          (partial filterv
                                                   #(not= d1 %))))
                   }])
               )
             ]
            ]
           ;;TICKET
           [:div.bg-gray-300.-ml-24.bg-opacity-20.transform.-translate-x-12.px-6.pt-4.shadow-lg.rounded-lg
            {:class ["w-1/4"]
             :style {:backdrop-filter "blur(40px)"}}
            [:div.mb-2
             [:img.w-full.shadow-lg.rounded-lg
              {:src poster_path}]]
            [:div.flex.mb-2
             [:h2.text-2xl.font-bold.mb-2.mr-auto "Your Ticket"]
             [:button.p-2.bg-indigo-600.text-white.rounded {:disabled (empty?  @your-seat)
                                                       :on-click
                                                       #(rf/dispatch
                                                          [::ticket-ev/book
                                                           {:booked_seats @your-seat
                                                            :seats_name @seatname :schedule (:id @select-schedule)}])}
              "book your ticket"]]
            [:div.flex.flex-col
             (for [ticket @seatname]
               [:div.mb-2.py-4.flex.bg-indigo-400.px-2.rounded.bg-opacity-70 {:key ticket}
                [:span.mr-auto
                 (str  "Seat " ticket)]])
             [:hr.mb-2]
             [:div.mb-2.py-4.flex.bg-green-400.px-2.rounded.bg-opacity-70
              [:span.mr-auto "Sums"]
              [:span
               [:span.mr-1
                (to-vnd
                  (*
                   (count @your-seat)
                   (:price @select-schedule)
                   ))]
               ]]]]]]]))))
