(ns cinemart.db)

(def default-db
  {:name "re-frame"
   :current-route nil
   :auth? false
   :users {"john@doe.com" {:email "john@doe.com"
                           :password "password"}}})
