(ns cinemart.components.header
  (:require [reitit.core :as r]
            [cinemart.router :as rt]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]
            [cinemart.login.events :as events]
            [cinemart.components.icons :refer [i-film]]))

(defn header
  [{:keys [routes current-route]}]
  (let [auth? @(rf/subscribe [:auth?])]
    [:header.bg-gray-800
     [:nav.xl:container.mx-auto.flex.py-2.px-3
      [:a.mr-auto.text-3xl.text-gray-400.font-bold.flex {:href (href ::rt/home)}
       [i-film {:class ["w-8" "mr-3"]}] "cinemart"]
      [:div.my-auto
       (for [route-name (r/route-names routes)
             :let [route (r/match-by-name routes route-name)
                   text (-> route :data :link-text)
                   auth-route (get-in route [:data :auth?] :always)
                   hidden (get-in route [:data :hidden])]
             :when (and (or (= auth-route auth?) (= auth-route :always))
                        (not hidden))]
         (cond
           (= route-name ::rt/signup) [:a.mr-2.text-xl.rounded.border-solid.border-gray-500.text-gray-500.border-2.p-2
                                       {:href (href route-name)
                                        :key route-name
                                        :class (when (= route-name
                                                        (-> current-route :data :name))
                                                 ["text-purple-500" "border-purple-500"])}
                                       text]
           :else [:a.mr-2.text-xl.text-gray-500 {:class (when (= route-name
                                                                 (-> current-route :data :name))
                                                          ["text-purple-500"])
                                                 :href (href route-name)
                                                 :key route-name}
                  text]))
       (when auth?
         [:a.mr-2.text-xl.rounded.border-solid.border-gray-500.text-gray-500.border-2.p-2 {:href "#"
                                                                                           :class "text-red-500"
                                                                                           :on-click #(rf/dispatch [::events/logout])} "Log out"])]]]))
