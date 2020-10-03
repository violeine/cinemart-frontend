(ns cinemart.about.view
  (:require [cinemart.components.container :refer [container]]
    [cinemart.notification.events :as events]
    [re-frame.core :as rf]))

(defn about-page []
  [container
   {}
   [:<>
    [:p "This app is written in Clojurescript with reframe
       and written by me & homies"]
    [:button.mt-3.py-2.px-3.bg-blue-300.rounded.ml-2
     {:on-click #(rf/dispatch-sync [::events/notify
                                    {:title "yo"}])}
     "Open notification"]]])
