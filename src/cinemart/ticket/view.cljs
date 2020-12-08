(ns cinemart.ticket.view
  (:require [cinemart.components.container :refer [container]]
            [cinemart.config :refer [image-link json-string]]
            [cinemart.components.seatmap :refer [seatmap]]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn ticket []
  (let [sub (rf/subscribe [:cinemart.movie.subs/movie])
        your-seat (r/atom [])]
    (fn []
      (let
       [{:keys [title overview backdrop_path runtime poster_path]} @sub]
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
             [:img.object-cover.w-full.object-center.shadow-lg.h-96.relative.rounded-lg.mb-8
              {:src (image-link [:backdrop :lg] backdrop_path)
               :style {:filter "brightness(50%)"}}]]
            [:div.bg-yellow-200.text-black.ml-8
             {:class ["w-10/12"]}

             [seatmap {:row 8
                       :col 12
                       :reserved-seat [1 5 8 10 23]
                       :your-seat @your-seat
                       :on-click-fn #(swap! your-seat conj %)
                       :on-delete-fn (fn [d1]
                                       (swap! your-seat
                                              (partial filterv
                                                       #(not= d1 %))))}]]]
        ;;TICKET


           [:div.bg-gray-300.-ml-24.bg-opacity-20.transform.-translate-x-12.px-6.pt-4.shadow-lg.rounded-lg
            {:class ["w-1/4"]
             :style {:backdrop-filter "blur(40px)"}}
            [:div.mb-2
             [:img.w-full.shadow-lg.rounded-lg
              {:src (image-link [:poster :lg] poster_path)}]]
            [:h2.text-2xl.font-bold.mb-2 "Your Ticket"]
            [:div.flex.flex-col
             (for [ticket @your-seat]
               [:div.mb-2.py-4.flex.bg-indigo-400.px-2.rounded.bg-opacity-70 {:key ticket}
                [:span.mr-auto
                 (str  "Seats " ticket)]])
             [:hr.mb-2]
             [:div.mb-2.py-4.flex.bg-green-400.px-2.rounded.bg-opacity-70
              [:span.mr-auto "Sums"] [:span "100$"]]]]]]]))))
