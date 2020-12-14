(ns cinemart.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [cinemart.events :as events]
            [cinemart.subs :as subs]
            [cinemart.config :as config]
            [cinemart.router :as rt]
            [day8.re-frame.http-fx]
            [cinemart.notification.view :refer [notification]]
            [cinemart.overlay.view :refer [overlay]]
            [cinemart.components.footer :refer [footer]]
            [cinemart.components.header :refer [header]]))
(defn hello-world []
  (let [current-route @(rf/subscribe [::subs/current-route])]
    [:div.flex.flex-col.justify-between.min-h-screen.bg-purple-900
     [header {:routes rt/router
              :current-route current-route}]
     (when current-route
       [(-> current-route :data :view)])
     [footer]
     [overlay]
     [notification]]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (rt/init-routes!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [hello-world] root-el)))

(defn ^:export init []
  (rf/dispatch-sync [::events/init-db])
  (dev-setup)
  (mount-root))
