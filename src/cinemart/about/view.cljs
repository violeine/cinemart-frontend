(ns cinemart.about.view
  (:require [cinemart.components.container :refer [container]]
            [cinemart.components.card :refer [card]]
            [cinemart.notification.events :as noti]
            [cinemart.overlay.events :as overlay]
            [cinemart.notification.view :refer [noti-type]]
            [cinemart.events :as fetch]
            [reitit.frontend.easy :refer [href]]
            [re-frame.core :as rf]))

(defn about-page []
  [container
   {:classes ["p-5"]}
   [:<>
    [:p.mb-2 "This app is written in Clojurescript with reframe
             and written by me & homies"]
    [:div.mb-2
     [:p.mb-4 "Notification demo"]
     [:div.ml-2.flex.w-full
      (for [noti (keys noti-type)
            :let [css (get-in noti-type [noti :css])
                  icon (get-in noti-type [noti :icon])]]
        [:div.mr-5
         {:class ["w-1/3"]}
         [card
          "text-gray-400"
          [:a.py-2.px-3.flex.shadow-md.items-center
           {:class css
            :on-click #(rf/dispatch-sync [::noti/notify
                                          {:text (str "this is a " noti)
                                           :type noti}])}
           [icon {:class ["w-8" "mr-5"]}]
           (str "this is an " noti)]]])]]

    [:div.mb-2
     [:p.mb-4 "Card demo"]
     [:div.w-32.h-32
      [card "text-indigo-500" [:div.h-32.bg-blue-400]]]]
    [:div.flex.text-gray-50
     [:a.mt-3.py-2.px-3.bg-blue-300.rounded.mr-2
      {:on-click #(rf/dispatch [::fetch/test-fetch])}
      "fetch from backend"]
     [:a.mt-3.py-2.px-3.bg-blue-300.rounded.mr-2
      {:href (href :cinemart.router/movie {:id 335984})}
      "go to movie page"]
     [:a.mt-3.py-2.px-3.bg-blue-300.rounded.mr-2
      {:on-click #(rf/dispatch [::overlay/open {:component
                                                (fn
                                                  []
                                                  [card
                                                   "text-gray-400"
                                                   [:div.w-64.h-32.bg-indidgo-200 "hello"]])}])}
      "go to movie page"]]]])
