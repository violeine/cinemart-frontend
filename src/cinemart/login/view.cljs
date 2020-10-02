(ns cinemart.login.view
  (:require [reagent.core :as r]
    [re-frame.core :refer [dispatch]]
    [cinemart.login.events :as events]))

;TODO check input
(defn check-input [] "to")
(defn login
  [{:keys [classes]}]
  (let [values (r/atom {:email ""
                        :password ""})]
    (fn []
      [:div.flex.flex-col.justify-center {:class classes}
       [:div.m-auto.bg-gray-100.shadow.rounded.px-5.py-3
        [:p.text-xl.text-center.mb-8.text-gray-800 "Welcome Back"]
        [:form
         [:div.mb-3
          [:label.block.text-gray-700.font-bold.mb-1 {:for "email"}
           "Email Address:"]
          [:input#email.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
           {:type "text"
            :name "email"
            :required true
            :value (:email @values)
            :on-change #(swap! values assoc :email (-> % .-target .-value))
            :placeholder "john@doe.com"}]]
         [:div.mb-6
          [:label.block.text-gray-700.font-bold.mb-1 {:for "password"}
           "Password:"]
          [:input#password.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
           {:type "password"
            :placeholder "**********"
            :on-change #(swap! values assoc :password (-> % .-target .-value))
            :value (:password @values)}]]
         [:div.mb-3
          [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
           {:on-click (fn [e]
                        (.preventDefault e)
                        (dispatch [::events/login @values]))}
           "Login"]
          [:a.text-sm.text-gray-500
           {:href "/"}
           "Don't have account? Sign Up Now"]]]]])))


