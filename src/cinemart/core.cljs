(ns cinemart.core
  (:require
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [cinemart.events :as events]
    [cinemart.subs :as subs]
    [cinemart.config :as config]
    [cinemart.router :as rt]
    [cinemart.components.footer :refer [footer]]
    [cinemart.components.header :refer [header]]))

(defn hello-world []
  (let [current-route @(rf/subscribe [::subs/current-route])]
    [:div.mx-auto.flex.flex-col.justify-between.min-h-screen
     [header {:routes rt/router
              :current-route current-route}]
     [:section.bg-yellow-400.flex-1.lg:container.mx-auto
      (when current-route
        [(-> current-route :data :view)])]
     [footer]]))

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
