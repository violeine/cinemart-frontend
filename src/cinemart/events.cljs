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
            (println cofx)
            (let [auth?
                  {:auth? (not (nil?  (get-in cofx [:user :token])))}]
              {:db (merge db/default-db cofx auth?)})))

(rf/reg-event-fx
 ::navigate
 (fn-traced [db [_ & route]]
            {::fx/navigate! route}))
(rf/reg-event-db
 ::navigated
 (fn-traced [db [_ new-match]]
            (let [old-match (:current-route db)
                  path_params (get-in db [:current-route :path-params])
                  old_name (get-in db [:current-route :data :name] :cinemart.router/home)
                  new_name (get-in new-match [:data :name])
                  controllers (rfc/apply-controllers
                               (:controllers old-match)
                               new-match)]
               ;; not push same state or login/sign up to prev route
              (if
               (or
                (= old_name :cinemart.router/signup)
                (= old_name new_name)
                (= old_name :cinemart.router/login))
                (assoc db :current-route
                       (assoc new-match :controllers controllers))
                (-> db
                    (assoc  :current-route
                            (assoc new-match :controllers controllers))
                    (assoc :prev-route {:name old_name
                                        :path-params path_params}))))))
;"https://api.themoviedb.org/3/movie/76341"

(rf/reg-event-fx
 ::test-fetch
 (fn-traced [{:keys [db]} _]
            (println (.includes "Token invalid" "invalid"))
            (let [http-req {:method :get
                            :uri "/me/invoices"
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
  ::insert
  (fn-traced [db [_ k data]]
            (assoc-in db k data)))

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

(rf/reg-event-db
  ::remove-in
  (fn-traced [db [_ k]]
             (dissoc db k)))

(rf/reg-event-db
 ::nothing
 (fn [db _]
   db))

(rf/reg-event-fx
 ::back
 (fn [{:keys [db]} _]
   (let
    [prev-route (get-in db [:prev-route :name])
     prev-param (get-in db [:prev-route :path-params])]
     {:fx  [[:dispatch [::navigate prev-route prev-param]]]})))
