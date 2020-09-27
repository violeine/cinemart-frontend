(ns cinemart.components.header
  (:require [reitit.core :as r]
    [cinemart.router :as rt]))

(defn header
  [{:keys [routes current-route]}]
  [:header.bg-indigo-400.text-red-500.sticky.top-0.shadow-2xl
   [:nav.container.bg-red-300.mx-auto.py-3.px-2.flex.content-center
    [:p.mr-auto "CINEMART"]
    [:div
     (for [route-name (r/route-names routes)
           :let [route (r/match-by-name routes route-name)
                 text (-> route :data :link-text)
                 auth? (get-in route [:data :auth?] :always)]]
       (when (or (= auth? false)  (= auth? :always))
         [:a.mr-2 {:class (when (= route-name
                                   (-> current-route :data :name))
                            "text-red-700")
                   :href (rt/href route-name)
                   :key route-name}
          text]))]]])
