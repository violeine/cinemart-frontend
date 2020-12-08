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
                                             :type :success}]]]}))

