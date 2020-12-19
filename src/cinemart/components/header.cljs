(ns cinemart.components.header
  (:require [reitit.core :as r]
            [cinemart.router :as rt]
            [re-frame.core :as rf]
            [reitit.frontend.easy :refer [href]]
            [cinemart.auth.events :as events]
            [cinemart.auth.subs :as auth]
            [cinemart.components.button :refer [button-outline]]

            [cinemart.components.icons :refer [i-film]]))

(defn header
  [{:keys [routes current-route]}]
  (let [auth? @(rf/subscribe [::auth/auth?])
        role @(rf/subscribe [::auth/role])]
    [:header.bg-gray-800.shadow-md
     [:nav.xl:container.mx-auto.flex.py-2.px-3
      [:div.mr-auto.text-3xl.text-gray-400.font-bold.flex.items-center
       [:a {:href (href ::rt/home)}
        [i-film {:class ["w-8" "mr-3"]}]]
       [:a
        (if (= (-> current-route :data :link-text) "home")
          "cinemart"
          (-> current-route :data :link-text)
          )]]
      [:div.my-auto
       (for [route-name (r/route-names routes)
             :let [route (r/match-by-name routes route-name)
                   text (-> route :data :link-text)
                   auth-route (get-in route [:data :auth?] :always)
                   role-route (get-in route [:data :role])
                   hidden (get-in route [:data :hidden])]
             :when (and (or (= role-route role) (= auth-route :always))
                        (not hidden))]
         (cond
           (= route-name ::rt/signup) [button-outline
                                       {:href (href route-name)
                                        :key route-name
                                        :class ["mr-2" "text-xl"
                                                (if (= route-name
                                                       (-> current-route :data :name))
                                                  "text-purple-500"
                                                  "text-gray-500"
                                                  )]}
                                       text]
           :else [:a.mr-2.text-xl.text-gray-500 {:class (when (= route-name
                                                                 (-> current-route :data :name))
                                                          ["text-purple-500"])
                                                 :href (href route-name)
                                                 :key route-name}
                  text]))
       (when auth?
         [ button-outline
          {:href "#"
           :class ["text-red-500" "text-xl" "mr-2"]
           :on-click #(rf/dispatch [::events/logout])} "Log out"])]]]))
