(ns cinemart.genres.events
  (:require
   [re-frame.core :as rf]
   [cinemart.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [cinemart.effects :as fx]
   [ajax.core :as ajax]
   [cinemart.config :refer [ backend-interceptor]]))


(rf/reg-event-fx
  ::fetch-genres
  (fn-traced [{:keys [db]} [_ id]]
             {:http-xhrio {:method :get
                           :uri (str "/movies/genres/one/" id )
                           :response-format (ajax/json-response-format
                                              {:keywords? true})
                           :interceptors [backend-interceptor]
                           :on-success [:cinemart.events/insert [:genres]]
                           :on-failure [:cinemart.auth.events/api-failure]}}))






