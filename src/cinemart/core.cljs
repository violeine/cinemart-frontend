(ns cinemart.core
  (:require
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [cinemart.events :as events]
    [cinemart.config :as config]))

(defn hello-world []
  [:div
   [:h1.text-4xl.font-bold.text-center.text-indigo-500 "Hello World!"]])

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [hello-world] root-el)))

(defn ^:export init []
  (rf/dispatch-sync [::events/init-db])
  (dev-setup)
  (mount-root))
