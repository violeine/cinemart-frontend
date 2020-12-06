(ns cinemart.components.forms.user
  (:require [reagent.core :as r]))

(defn user-form
  [{:keys [init-data on-submit-fn]}]
  (let [values (r/atom
                (merge
                 {:mail ""
                  :password nil
                  :username "john"
                  :dob "1/1/1970"
                  :fullname "John Doe"}
                 init-data))
        form-title (if init-data
                     "Update Users"
                     "Create Users")]
    (fn []
      [:div.m-auto.bg-gray-100.shadow.rounded.px-5.py-3
       [:p.text-xl.text-center.mb-8.text-gray-800
        form-title]
       [:form
        [:div.mb-3
         [:label.block.text-gray-700.font-bold.mb-1 {:for "email"}
          "Email Address:"]
         [:input#email.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
          {:type "text"
           :name "email"
           :required true
           :value (:mail @values)
           :on-change #(swap! values assoc :mail (-> % .-target .-value))
           :placeholder "john@doe.com"}]]
        [:div.mb-6
         [:label.block.text-gray-700.font-bold.mb-1 {:for "password"}
          "Password:"]
         [:input#password.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
          {:type "password"
           :placeholder "password"
           :on-change #(swap! values assoc :password (-> % .-target .-value))
           :value (:password @values)}]]
        [:div.mb-3
         [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
          {:on-click (on-submit-fn @values)}
          form-title]]]])))
