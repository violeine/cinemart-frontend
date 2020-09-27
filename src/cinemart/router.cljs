(ns cinemart.router
  (:require
    [reitit.coercion.spec :as rss]
    [reitit.frontend :as rf]
    [reitit.frontend.easy :as rfe]
    [cinemart.events :as events]
    [cinemart.home.view :refer [home-page]]
    [cinemart.about.view :refer [about-page]]
    [re-frame.core :refer [dispatch]]))

(defn href
  "Return relative url for given route, url can be html links"
  ([k]
    (href k nil nil))
  ([k params]
    (href k params nil))
  ([k params query]
    (rfe/href k params query)))

(def routes
  ["/"
   [""
    {:name      ::home
     :link-text "Home"
     :view home-page
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params] (js/console.log "Entering home page"))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving home page"))}]}]
   ["about"
    {:name      ::about
     :link-text "About"
     :view about-page
     :controllers
     [{:start (fn [params] (js/console.log params))
       :stop  (fn [params] (js/console.log "Leaving sub-page 1"))}]}]
   ["login"
    {:name      ::login
     :link-text "Log in"
     :view about-page
     :auth? false
     :controllers
     [{:params (fn [match]
                 (js/console.log match)
                 (get-in match [:data :auth?]))
       :start (fn [auth?] (js/console.log auth?))
       :stop  (fn [& params] (js/console.log "Leaving sub-page login"))}]}]
   ["profile"
    {:name      ::profile
     :link-text "Profile"
     :view about-page
     :auth? true
     :controllers
     [{:start (fn [& params] (js/console.log "Entering sub-page 1"))
       :stop  (fn [& params] (js/console.log "Leaving sub-page 1"))}]}]])

(defn on-navigate [new-match]
  (when new-match
    (dispatch [::events/navigated new-match])))

(def router (rf/router routes {:data {:coercion rss/coercion}}))

(defn init-routes!
  []
  (println "init routes")
  (rfe/start! router on-navigate {:use-fragment false}))

