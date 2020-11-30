(ns cinemart.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cinemart.effects :as fx]
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

(reg-event-fx
 ::login-success
 (fn-traced [{:keys [db]} [_ result]]
            (let [ref-token (get-in result [:user :refresh-token])
                  token (get-in result [:user :token])]
              (println ref-token)
              {:db
               (-> db
                   (assoc :auth? true)
                   (assoc :user (:user result)))
               :fx [[::fx/save-storage! ["refresh-token" ref-token]]
                    [::fx/save-storage! ["token" token]]
                    [::fx/back!]
                    [:dispatch [::noti/notify {:text "Login successfully"
                                               :type :success}]]]})))

;; TODO implement refresh mechanism herre
(reg-event-db
 ::login-failure
 ;;TODO noti it's up
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))

;;TODO move log out logic to here

(reg-event-fx
 ::logout
 (fn-traced [{:keys [db]} [_ result]]
            ;;TODO clear storage!
            ;;TODO dissoc auth and user
            ;;TODO notify logged out
            (println "logged out")
            {:db
             (-> db
                 (assoc :auth? false)
                 (dissoc :user))
             :fx [[::fx/clear-storage!]
                  [:dispatch [::noti/notify {:text "Logged out"
                                             :type :info}]]]}))

