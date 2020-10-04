(ns cinemart.db)

(def default-db
  {:name "re-frame"
   :current-route nil
   :auth? false
   :noti {}
   :users {"john@doe.com" {:email "john@doe.com"
                           :password "password"}}})
