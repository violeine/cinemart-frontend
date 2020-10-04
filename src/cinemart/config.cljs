(ns cinemart.config
  (:require [ajax.core :as ajax]
    [cinemart.env :refer [api-key]]))

(def debug?
  ^boolean goog.DEBUG)

(def powered-by [{:src "https://www.themoviedb.org/assets/2/v4/logos/v2/blue_square_2-d537fb228cf3ded904ef09b136fe3fec72548ebc1fea3fbbd1ad9e36364db38b.svg"
                  :alt "tmdb"
                  :href "https://www.themoviedb.org/"}
                 {:src "https://tailwindcss.com/_next/static/media/tailwindcss-mark.6ea76c3b72656960a6ae5ad8b85928d0.svg"
                  :alt "tailwind"
                  :href "https://tailwindcss.com/"}
                 {:src "https://github.com/day8/re-frame/raw/master/docs/images/logo/re-frame-colour.png?raw=true"
                  :alt "re-frame"
                  :href "https://day8.github.io/re-frame/"}])

(def api-interceptor
  (ajax/to-interceptor
    {:name "inject api key and append uri"
     :request (fn [request]
                (let [uri (:uri request)]
                  (-> request
                      (assoc-in
                        [:params
                         :api_key]
                        api-key)
                      (assoc-in
                        [:uri]
                        (str "https://api.themoviedb.org/3" uri)))))}))
