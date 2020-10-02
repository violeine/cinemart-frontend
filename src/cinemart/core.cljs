(ns cinemart.core
  (:require
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [cinemart.events :as events]
    [cinemart.subs :as subs]
    [cinemart.config :as config]
    [cinemart.router :as rt]
    [cinemart.components.footer :refer [footer]]
    [cinemart.components.notification :refer [notification]]
    [cinemart.components.header :refer [header]]))

(defn hello-world []
  (let [current-route @(rf/subscribe [::subs/current-route])]
    [:div.flex.flex-col.justify-between.min-h-screen
     [header {:routes rt/router
              :current-route current-route}]
     (when current-route
       [(-> current-route :data :view) {:classes
                                        ["bg-gray-300"
                                         "xl:container"
                                         "w-screen"
                                         "flex-1"
                                         "mx-auto"]}])
     [footer]
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
