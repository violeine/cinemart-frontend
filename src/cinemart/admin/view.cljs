(ns cinemart.admin.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.admin.subs :as sub]
            [cinemart.components.forms.user :refer [user-form]]
            [cinemart.admin.dashboard :refer [dashboard]]
            [cinemart.overlay.events :as overlay]
            [cinemart.admin.events :as admin-ev]
            [cinemart.config :refer [json-string]]))

(defn admin []
  (let [{:keys [theaters admins users managers] :as admin} @(rf/subscribe [::sub/admin])]
    [container {:classes ["flex" "flex-col"]}
     [:<>
      [:h2.text-lg "admin panel"]
      [:pre.bg-green-300.text-black (json-string admin)]
      [dashboard {:type :users
                  :arr users
                  :order [:id :mail :fullname :dob :username :created_at]
                  :update-btn (fn [prop]
                                [user-form prop])}
       [:a.bg-indigo-600.text-md.px-2.py-2
        {:on-click #(rf/dispatch
                     [::overlay/open
                      {:component (fn []
                                    [user-form
                                     {:on-submit-fn (fn [payload]
                                                      (fn [e]
                                                        (.preventDefault e)
                                                        (rf/dispatch
                                                          [::admin-ev/create :users payload])))}])}])}
        "Create Users"]]
      [dashboard {:type :admins
                  :arr admins
                  :order [:id :mail :fullname :dob :username :created_at]}
       [:a.bg-indigo-600.text-md.px-2.py-2
        "Create Users"]]
      [dashboard {:type :managers
                  :arr managers
                  :order [:id :mail :fullname :dob :username :created_at]}

       [:a.bg-indigo-600.text-md.px-2.py-2
        "Update User"]]
      [dashboard {:type :theaters
                  :arr theaters
                  :order [:id :mail :fullname :dob :username :created_at]}
       [:a.bg-indigo-600.text-md.px-2.py-2
        "Create Users"]]]]))


