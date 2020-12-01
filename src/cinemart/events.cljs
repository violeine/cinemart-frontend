(ns cinemart.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [cinemart.config :refer [backend-interceptor token-interceptor]]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [cinemart.auth.events :as auth]
   [ajax.core :as ajax]
   [reitit.frontend.controllers :as rfc]))

(rf/reg-event-fx
 ::init-db
 [(rf/inject-cofx ::fx/init-storage)]
 (fn-traced [cofx _]
            {:db (merge db/default-db cofx)}))

(rf/reg-event-fx
 ::navigate
 (fn-traced [db [_ & route]]
            {::fx/navigate! route}))
(rf/reg-event-db
 ::navigated
 (fn-traced [db [_ new-match]]
            (let [old-match (:current-route db)
                  controllers (rfc/apply-controllers
                               (:controllers old-match)
                               new-match)]
              (assoc db :current-route
                     (assoc new-match :controllers controllers)))))
;"https://api.themoviedb.org/3/movie/76341"

(rf/reg-event-fx
 ::test-fetch
 (fn-traced [{:keys [db]} _]
            (println (.includes "Token invalid" "invalid"))
            (let [http-req {:method :get
                            :uri "/me"
                            :interceptors [backend-interceptor (token-interceptor
                                                                (get-in db [:user :token]))]
                            :response-format (ajax/json-response-format
                                              {:keywords? true})
                            :on-success [::good-http-result]
                            :on-failure [::auth/api-failure]}]
              {:http-xhrio http-req
               :db (assoc db :prev-req http-req)})))

(rf/reg-event-db
 ::good-http-result
 (fn-traced [db [_ result]]
            (assoc db :http-result result)))

(rf/reg-event-db
 ::bad-http-result
 (fn-traced [db [_ result]]
            (assoc db :http-failure result)))

(rf/reg-event-db
 ::clear-http-result
 (fn-traced [db _]
            (-> db
                (dissoc :http-result)
                (dissoc :http-failure))))
