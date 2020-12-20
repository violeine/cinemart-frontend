(ns cinemart.auth.login
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [cinemart.components.container :refer [container]]
            [reitit.frontend.easy :refer [href]]
            [cinemart.auth.events :as events]))

(defn login
  [{:keys [classes]}]
  (let [values (r/atom
                {:payload
                 {:mail ""
                  :password ""}
                 :role ""})]
    (fn []
      [container {:classes ["flex" "flex-col" "justify-center"]}
       [:div.m-auto.bg-gray-100.shadow.rounded.px-5.py-3
        [:p.text-xl.text-center.mb-8.text-gray-800 "Welcome Back"]
        [:form
         [:div.mb-3
          [:label.block.text-gray-700.font-bold.mb-1 {:for "mail"}
           "Mail Address:"]
          [:input#mail.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
           {:type "text"
            :name "mail"
            :required true
            :value (-> @values :payload :mail)
            :on-change #(swap! values assoc-in [:payload :mail] (-> % .-target .-value))
            :placeholder "john@doe"}]]
         [:div.mb-6
          [:label.block.text-gray-700.font-bold.mb-1 {:for "password"}
           "Password:"]
          [:input#password.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
           {:type "password"
            :placeholder "password"
            :on-change #(swap! values assoc-in [:payload :password] (-> % .-target .-value))
            :value (-> @values :payload :password)}]]
         [:select.rounded.px-5.w-64.text-gray-700.mb-4
          {:on-change #(swap! values assoc-in [:role] (-> % .-target .-value))}
          [:option {:value ""} "Users"]
          [:option {:value "/manager"} "Managers"]
          [:option {:value "/admin"} "admins"]
          ]

         [:div.mb-3
          [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
           {:on-click (fn [e]
                        (.preventDefault e)
                        (dispatch [::events/login @values]))}
           "Login"]
          [:a.text-sm.text-gray-500
           {:href (href :cinemart.router/signup)}
           "Don't have account? Sign Up Now"]]]]])))


