(ns cinemart.config
  (:require [ajax.core :as ajax]
            ; [cinemart.env :refer [api-key] ]
            ))

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

(def alphabet (map char (range 97 123)))

(defn to-readable-seat
  [n r ]
  (if (= (mod n r) 0)
    (str
      (nth alphabet (- (quot n r) 1))
      r
      )
    (str
      (nth alphabet (quot n r))
      (mod n r))))


;;TODO change name to tmdb
; (def movie-interceptor
;   (ajax/to-interceptor
;    {:name "inject api key and append uri"
;     :request (fn [request]
;                (let [uri (:uri request)]
;                  (-> request
;                      (assoc-in
;                       [:headers
;                        :Authorization]
;                       (str "Bearer " api-key))
;                      (assoc-in
;                       [:uri]
;                       (str "https://api.themoviedb.org/3" uri)))))}))

(def backend-interceptor
  (ajax/to-interceptor
   {:name "append uri"
    :request (fn [request]
               (let [uri (:uri request)]
                 (-> request
                     (assoc-in
                      [:uri]
                      ;change localhost to your backend
                      (str "http://localhost:4000" uri)))))}))

(defn token-interceptor [token]
  (ajax/to-interceptor
   {:name "inject token to header"
    :request (fn [request]
               (-> request
                   (assoc-in
                    [:headers
                     :Authorization]
                    (str "Bearer " token))))}))

(defn delete-is-empty [{:keys [method] :as request}]
  (if (= method "DELETE")
    (reduced (assoc request :body nil))
    request))

(def app-engine-delete-interceptor
  (ajax/to-interceptor {:name "Google App Engine Delete Rule"
                        :request delete-is-empty}))

(swap! ajax/default-interceptors concat [app-engine-delete-interceptor])

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

(defn now
  []
  (let [n (new js/Date)
        pad #(if (< % 10 ) (str 0 %) %)]
    (str (.getFullYear n) "-"
         (+
          (.getMonth n) 1) "-" (.getDate n)
         "T"
         (pad (.getHours n))
         ":"
         (pad (.getMinutes n)))))

(defn today
  []
  (let [n (new js/Date)
        pad #(if (< % 10 ) (str 0 %) %)]
    (str (.getFullYear n) "-"
         (+
          (.getMonth n) 1) "-" (.getDate n)
         "T00:00:000Z")))

(defn iso-time
  [t]
  (.toISOString
    (new js/Date t)))

(defn to-vn-time [t]
  (.toLocaleString
                 (new js/Date t)
                 "vi-VN"))

(defn to-vnd [number]
  (.format
    (js/Intl.NumberFormat.
         "vi-VN" #js{:style "currency" :currency "VND"})
    number))
