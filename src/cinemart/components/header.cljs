(ns cinemart.components.header
  (:require [reitit.core :as r]
    [cinemart.router :as rt]
    [re-frame.core :as rf]))

(defn header
  [{:keys [routes current-route]}]
  (let [auth? @(rf/subscribe [:auth?])]
    [:header.bg-indigo-400.text-red-500.sticky.w-full.top-0.shadow-2xl
     [:nav.xl:container.mx-auto.py-3.px-2.flex.content-center
      [:a.mr-auto {:href "/"} "CINEMART"]
      [:div
       (for [route-name (r/route-names routes)
             :let [route (r/match-by-name routes route-name)
                   text (-> route :data :link-text)
                   auth-route (get-in route [:data :auth?] :always)]]
         (when (or (= auth-route auth?)  (= auth-route :always))
           [:a.mr-2 {:class (when (= route-name
                                     (-> current-route :data :name))
                              "text-red-700")
                     :href (rt/href route-name)
                     :key route-name}
            text]))
       (when auth?
         [:a {:href "#"
              :on-click #(rf/dispatch [:cinemart.login.events/logout])}
          "Logout"])]]]))
