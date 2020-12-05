(ns cinemart.router
  (:require
   [reitit.coercion.spec :as rss]
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]
   [cinemart.events :as events]
   [cinemart.auth.events :as auth]
   [cinemart.movie.events :as movie-events]
   [cinemart.home.view :refer [home-page]]
   [cinemart.about.view :refer [about-page]]
   [cinemart.admin.view :refer [admin]]
   [cinemart.movie.view :refer [movie]]
   [cinemart.auth.login :refer [login]]
   [cinemart.notification.events :as noti]
   [cinemart.admin.events :as admin-ev]
   [cinemart.auth.signup :refer [signup]]
   [re-frame.core :refer [dispatch]]))

(def routes
  ["/"
   [""
    {:name      ::home
     :link-text "home"
     :view home-page
     :hidden false
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params] (js/console.log "Entering home page"))
       ;; Teardown can be done here.
       :stop  (fn [& params] (js/console.log "Leaving home page"))}]}] ["about" {:name      ::about
                                                                                 :link-text "about"
                                                                                 :view about-page
                                                                                 :hidden false
                                                                                 :controllers
                                                                                 [{:start (fn [params] (js/console.log params))
                                                                                   :stop  (fn [params] (js/console.log "Leaving sub-page 1"))}]}]
   ["movie/:id"
    {:name ::movie
     :link-text "movie"
     :view movie
     :hidden true
     :controllers [{:parameters {:path [:id]}
                    :start (fn [params] (dispatch
                                         [::movie-events/fetch-movie
                                          (-> params :path :id)]))}]}]
   ["login"
    {:name      ::login
     :link-text "log in"
     :view login
     :auth? false
     :hidden false
     :controllers
     [{:identity identity
       :start (fn [match] (dispatch [::auth/guard {:next [::noti/notify {:text "login"
                                                                         :type :success}]
                                                   :route-match match}]))
       :stop  (fn [& params] (js/console.log "Leaving sub-page login"))}]}]
   ["signup"
    {:name      ::signup
     :link-text "sign up"
     :view signup
     :auth? false
     :hidden false
     :controllers
     [{:identity identity
       :start (fn [match] (dispatch [::auth/guard {:next [::noti/notify {:text "signup"
                                                                         :type :success}]
                                                   :route-match match}]))}]}]
   ["profile"
    {:name      ::profile
     :link-text "profile"
     :view about-page
     :auth? true
     :role "user"
     :hidden false
     :controllers
     [{:identity identity
       :start (fn [match] (dispatch [::auth/guard {:next [::noti/notify {:text "profile"
                                                                         :type :success}]
                                                   :route-match match}]))
       :stop  (fn [& params] (js/console.log "Leaving sub-page 1"))}]}]
   ["admin"
    {:name      ::admin
     :link-text "admin"
     :view admin
     :auth? true
     :role "admin"
     :hidden false
     :controllers
     [{:identity identity
       :start (fn [match] (dispatch [::auth/guard {:next [::admin-ev/init-admin]
                                                   :route-match match}]))
       :stop  (fn [& params] (js/console.log "Leaving sub-page 1"))}]}]])

(defn on-navigate [new-match]
  (when new-match
    (dispatch [::events/navigated new-match])))

(def router (rf/router routes {:data {:coercion rss/coercion}}))

(defn init-routes!
  []
  (println "init routes")
  (rfe/start! router on-navigate {:use-fragment false}))

