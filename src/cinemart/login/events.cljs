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
      {:db (assoc-in db [:error :email]
                     "Wrong email")
       :fx [[:dispatch [::noti/notify
                        {:title "Wrong password or email"}]]]}
      (not correct-pass?)
      {:db (assoc-in db [:error :email]
                     "Wrong password")
       :fx [[:dispatch [::noti/notify {:title "Wrong password or email"}]]]}
      correct-pass?
      {:db (-> db
               (assoc :auth? true)
               (update-in [:error] dissoc :email))
       :fx [[:dispatch [::events/navigate
                        :cinemart.router/profile]]
            [:dispatch [::noti/notify
                        {:title "logged-in"}]]]}))))

(reg-event-fx
 ::logout
 (fn-traced
  [{:keys [db]} [_ _]]
  {:db (assoc db :auth? false)
   :fx [[:dispatch [::events/navigate
                    :cinemart.router/home]]
        [:dispatch [::noti/notify
                    {:title "logged out"
                     :type :info}]]]}))



