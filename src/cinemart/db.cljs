(ns cinemart.db)

(def default-db
  {:name "re-frame"
   :current-route nil
   :auth? false
   :noti {}
   :prev-route {:name :cinemart.router/home}})
