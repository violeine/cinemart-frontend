(ns cinemart.login.events
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [cinemart.events :as events]
    [cinemart.notification.events :as noti]))

(reg-event-fx
  ::login
  (fn-traced
    [{:keys [db]} [_ {:keys [email password]}]]
    (.log js/console email password)
    (let [user (get-in db [:users email])
          correct-pass? (= password (:password user))]
      (cond
        (not user)
          ;TODO notification
        {:db (assoc-in db [:error :email]
                       "Wrong email")}
        (not correct-pass?)
        {:db (assoc-in db [:error :email]
                       "Wrong password")}
        correct-pass?
        {:db (-> db
                 (assoc :auth? true)
                 (update-in [:error] dissoc :email))
         :fx [[:dispatch [::events/navigate
                          :cinemart.router/profile]]
              [:dispatch [::noti/notify
                          {:title "logged-in"}]]]}))))

(reg-event-db
  ::logout
  (fn-traced
    [db [_ _]]
    (assoc db :auth? false)))



