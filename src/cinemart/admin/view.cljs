(ns cinemart.admin.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.admin.subs :as sub]
            [cinemart.components.forms.user :refer [user-form]]
            [cinemart.components.forms.admin :refer [admin-form]]
            [cinemart.components.forms.theater :refer [theater-form]]
            [cinemart.admin.dashboard :refer [dashboard]]
            [cinemart.overlay.events :as overlay]
            [cinemart.components.search :refer [search-admin]]
            [cinemart.admin.events :as admin-ev]
            [cinemart.config :refer [json-string]]))
(def btn-state (r/atom :admins))

(def arr [[:me "Me"]
          [:admins "Admin"]
          [:theaters "Theater"]
          [:users "User"]
          [:movie "Movie"]
          [:managers "Manager"]])

(defn admin []
  (let [{:keys [theaters admins users managers movies] :as admin} @(rf/subscribe [::sub/admin])
        me @(rf/subscribe [:cinemart.auth.subs/me])]
    [container {:classes ["flex" "flex-col"]}
     [:<>
      [:div.flex.p-2
       (doall
         (for [btn arr]
           [:a.p-2.mr-2 {:key (first btn)
                         :on-click (fn []
                                     (reset! btn-state (first btn))
                                     (if (= :me @btn-state)
                                       (rf/dispatch [:cinemart.auth.events/get-me])
                                       (rf/dispatch [::admin-ev/get @btn-state])))
                         :class [(if (= (first btn) @btn-state)
                                   "bg-indigo-300"
                                   "bg-indigo-600")]}
            (second btn)]))]
      ; [:pre.bg-green-300.text-black (json-string admin)]
      (case @btn-state
        :me [admin-form {:type :admin
                         :init-data me
                         :on-submit-fn (fn [payload]
                                         (fn [e]
                                           (.preventDefault e)
                                           (rf/dispatch
                                             [:cinemart.auth.events/update-me payload])))}]
        :users [dashboard {:type :users
                           :arr users
                           :order [:id :mail :fullname :username :created_at]
                           :update-btn (fn [prop]
                                         [user-form prop])}
                [:a.text-md.px-2.py-2.block
                 {:on-click #(rf/dispatch
                               [::overlay/open
                                {:component (fn []
                                              [user-form
                                               {:on-submit-fn
                                                (fn [payload]
                                                  (fn [e]
                                                    (.preventDefault e)
                                                    (rf/dispatch
                                                      [::admin-ev/create :users payload])))}])}])}
                 "Create Users"]]
        :admins [dashboard {:type :admins
                            :arr admins
                            :order [:id :mail :created_at]
                            :update-btn (fn [prop]
                                          [admin-form prop])}
                 [:a.bg-indigo-600.text-md.px-2.py-2
                  {:on-click #(rf/dispatch
                                [::overlay/open
                                 {:component (fn []
                                               [admin-form
                                                {:type :admin
                                                 :on-submit-fn (fn [payload]
                                                                 (fn [e]
                                                                   (.preventDefault e)
                                                                   (rf/dispatch
                                                                     [::admin-ev/create :admins payload])))}])}])}
                  "Create Admins"]]
        :theaters [dashboard {:type :theaters
                              :arr theaters
                              :update-btn (fn [prop]
                                            [theater-form prop])
                              :order [:id  :name :address :created_at]}
                   [:a.bg-indigo-600.text-md.px-2.py-2
                    {:on-click #(rf/dispatch
                                  [::overlay/open
                                   {:component (fn []
                                                 [theater-form
                                                  {:init-data {:manager {:mail ""
                                                                         :password ""}}
                                                   :on-submit-fn (fn [payload]
                                                                   (fn [e]
                                                                     (.preventDefault e)
                                                                     (rf/dispatch
                                                                       [::admin-ev/create-theaters payload])))}])}])}
                    "Create Theater"]]
        :managers [dashboard {:type :managers
                              :arr managers
                              :update-btn (fn [prop]
                                            [admin-form prop])
                              :order [:id :mail :theater_name :created_at]}
                   [:a.bg-indigo-600.text-md.px-2.py-2
                    {:on-click #(rf/dispatch
                                  [::overlay/open
                                   {:component (fn []
                                                 [admin-form
                                                  {:type :managers
                                                   :theaters theaters
                                                   :on-submit-fn (fn [payload]
                                                                   (fn [e]
                                                                     (.preventDefault e)
                                                                     (rf/dispatch
                                                                       [::admin-ev/create :managers payload])))}])}])}
                    "Create Manager"]]
        :movie
        [:div.px-5
         ^{:key movies}
         [search-admin movies]]
        )]]))



