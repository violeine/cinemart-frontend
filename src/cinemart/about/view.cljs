(ns cinemart.about.view
  (:require [cinemart.components.container :refer [container]]
            [cinemart.notification.events :as noti]
            [cinemart.notification.view :refer [noti-type]]
            [cinemart.events :as fetch]
            [re-frame.core :as rf]))

(defn about-page []
  [container
   {}
   [:<>
    [:p "This app is written in Clojurescript with reframe
       and written by me & homies"]
    (for [noti (keys noti-type)
          :let [css (get-in noti-type [noti :css])
                icon (get-in noti-type [noti :icon])]]
      [:a.mt-3.py-2.px-3.rounded.mx-2.w-64.flex.text-white
       {:class css
        :on-click #(rf/dispatch-sync [::noti/notify
                                      {:text (str "this is an " noti)
                                       :type noti}])}
       [icon {:class ["w-8" "mr-5"]}]
       (str "this is an " noti)])
    [:button.mt-3.py-2.px-3.bg-blue-300.rounded
     {:on-click #(rf/dispatch [::fetch/test-fetch])}
     "Fetch something"]]])
