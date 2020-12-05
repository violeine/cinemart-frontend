(ns cinemart.config
  (:require [ajax.core :as ajax]
            [cinemart.env :refer [api-key]]))

(def debug?
  ^boolean goog.DEBUG)

(def powered-by [{:src "https://www.themoviedb.org/assets/2/v4/logos/v2/blue_square_2-d537fb228cf3ded904ef09b136fe3fec72548ebc1fea3fbbd1ad9e36364db38b.svg"
                  :alt "tmdb"
                  :href "https://www.themoviedb.org/"}
                 {:src "https://tailwindcss.com/_next/static/media/tailwindcss-mark.ce301590451472adad5301c69f9054af.svg"
                  :alt "tailwind"
                  :href "https://tailwindcss.com/"}
                 {:src "https://raw.githubusercontent.com/day8/re-frame/master/docs/images/logo/re-frame-white.svg"
                  :alt "re-frame"
                  :href "https://day8.github.io/re-frame/"}])

;;TODO change name to tmdb
(def movie-interceptor
  (ajax/to-interceptor
   {:name "inject api key and append uri"
    :request (fn [request]
               (let [uri (:uri request)]
                 (-> request
                     (assoc-in
                      [:headers
                       :Authorization]
                      (str "Bearer " api-key))
                     (assoc-in
                      [:uri]
                      (str "https://api.themoviedb.org/3" uri)))))}))

(def backend-interceptor
  (ajax/to-interceptor
   {:name "append uri"
    :request (fn [request]
               (let [uri (:uri request)]
                 (-> request
                     (assoc-in
                      [:uri]
                      (str "https://violeine.duckdns.org" uri)))))}))

(defn token-interceptor [token]
  (ajax/to-interceptor
   {:name "inject token to header"
    :request (fn [request]
               (-> request
                   (assoc-in
                    [:headers
                     :Authorization]
                    (str "Bearer " token))))}))

(def image-base-url "https://image.tmdb.org/t/p/")

(def image-type {:backdrop {:sm "w300"
                            :md "w780"
                            :lg "w1280"
                            :og "original"}
                 :logo {:sm "w185"
                        :md "w300"
                        :lg "w500"
                        :og "original"}
                 :poster {:xsm "w185"
                          :sm "w342"
                          :md "w500"
                          :lg "w780"
                          :og "original"}
                 :profile {:sm "w45"
                           :md "w185"
                           :lg "h632"
                           :og "original"}})

(defn image-link [t url]
  (str image-base-url (get-in image-type t) url))

(defn media-queries
  [size vect]
  (map #(str size %) vect))

(defn css
  [vect]
  (vec (flatten vect)))

(defn json-string [m]
  (.stringify js/JSON (clj->js m) nil 2))
