(ns cinemart.ticket.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cinemart.effects :as fx]
            [ajax.core :as ajax]
            [cinemart.config :refer [backend-interceptor token-interceptor]]
            [cinemart.notification.events :as noti]))

(reg-event-fx
  ::init-ticket
  (fn-traced [{:keys [db]} [_ id]]
             {:fx [[:dispatch [:cinemart.movie.events/fetch-movie id]]
                   [:dispatch [::noti/notify {:text "Ticket"
                                              :type :success}]]]
              :http-xhrio {:method :get
                           :uri (str "/movies/" id "/screening/theaters")
                           :response-format (ajax/json-response-format
                                              {:keywords? true})
                           :interceptors [backend-interceptor ]
                           :on-success [::success :theaters]
                           :on-failure [::failure]}}))

(reg-event-fx
  ::get-schedules
  (fn-traced [{:keys [db]} [_ theater movie]]
             { :db (update-in db [:ticket] dissoc :schedules)
              :http-xhrio {:method :get
                           :uri (str "/theaters/" theater "/schedules/" movie)
                           :response-format (ajax/json-response-format
                                              {:keywords? true})
                           :interceptors [backend-interceptor ]
                           :on-success [::success :schedules]
                           :on-failure [::failure]}}))

(reg-event-db
  ::success
  (fn-traced [db [_ k result]]
             (assoc-in db [:ticket k] (:response result))))

(reg-event-db
 ::failure
 (fn-traced [db [_ failure]] (assoc db :api-failure failure)))


(reg-event-fx
  ::book
  (fn-traced [{:keys [db]} [_ payload]]
             (let [user-id (get-in db [:user :id])
                   token (get-in db [:user :token])]
               (if token
                 {:http-xhrio {:method :post
                               :uri "/me/invoices"
                               :params (merge payload {:user user-id})
                               :format (ajax/json-request-format)
                               :response-format (ajax/json-response-format {:keywords? true})
                               :interceptors [backend-interceptor (token-interceptor token)]
                               :on-success [::create-success]
                               :on-failure [:cinemart.auth.events/api-failure]}}
                 {:fx [[:dispatch
                        [:cinemart.events/navigate :cinemart.router/login]]
                       [:dispatch
                        [::noti/notify {:text "login first"
                                        :type :info}]]]})
               )))

(reg-event-fx
  ::create-success
  (fn-traced []
             {:fx [[:dispatch
                    [:cinemart.events/navigate :cinemart.router/profile]]
                   [:dispatch
                    [::noti/notify {:text "Booked"
                                     :type :success}]]]}))

(reg-event-db
  ::remove-ticket
  (fn-traced [db [_ _]]
             (dissoc db :ticket)))
