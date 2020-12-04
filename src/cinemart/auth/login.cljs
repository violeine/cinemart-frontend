(ns cinemart.auth.login
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [cinemart.components.container :refer [container]]
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
           "mail Address:"]
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
         [:div.mb-6
          [:label.block.text-gray-700.font-bold.mb-1 {:for "role"}
           "Role:"]
          [:input#password.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
           {:type "text"
            :placeholder "role"
            :on-change #(swap! values assoc :role (-> % .-target .-value))
            :value (-> @values :role)}]]
         [:div.mb-3
          [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
           {:on-click (fn [e]
                        (.preventDefault e)
                        (dispatch [::events/login @values]))}
           "Login"]
          [:a.text-sm.text-gray-500
           {:href "/"}
           "Don't have account? Sign Up Now"]]]]])))


