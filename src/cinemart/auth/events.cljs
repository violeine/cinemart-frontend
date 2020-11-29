(ns cinemart.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cinemart.events :as events]
            [ajax.core :as ajax]
            [cinemart.config :refer [uri-interceptor]]
            [cinemart.notification.events :as noti]))

(reg-event-fx
 ::login
 (fn-traced [_ [_ payload]]
            {:http-xhrio {:method :post
                          :uri  "/login"
                          :params payload
                          :format (ajax/json-request-format)
                          :response-format (ajax/json-response-format {:keywords? true})
                          :interceptors [uri-interceptor]
                          :on-success [::login-success]
                          :on-failure [::login-failure]}}))

(reg-event-db
 ;;TODO switch to effect
 ::login-success
 ;;TODO save to localstorage refresh token and access token
 ;;TODO kick to previous page?
 ;;TODO set noti to success
 (fn-traced [db [_ result]]
            (-> db
                (assoc :auth? true)
                (assoc :user (:user result)))))

;; TODO implement refresh mechanism herre
(reg-event-db
 ::login-failure
 ;;TODO noti it's up
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))

;;TODO move log out logic to here

(reg-event-fx
 ::logout
 (fn-traced [db [_ result]]
            (println "logged out")))
