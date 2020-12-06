(ns cinemart.components.forms.admin
  (:require [reagent.core :as r]
            [cinemart.config :refer [json-string]]))

(defn admin-form
  [{:keys [init-data on-submit-fn theaters type]}]
  (let [values (r/atom
                (merge
                 {:mail ""
                  :password nil}
                 (if theaters {:theater 1})
                 init-data))
        form-title (if init-data
                     (str "Update " (name type))
                     (str "Create " (name type)))]
    (fn []
      [:div.m-auto.bg-gray-100.shadow.rounded.px-5.py-3
       [:p.text-xl.text-center.mb-8.text-gray-800
        form-title]
       [:form
        {:auto-complete "off"}
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
        (when theaters
          [:select {:value (:theater @values)
                    :on-change #(swap! values assoc :theater (-> % .-target .-value))}
           (for [theater theaters
                 :let [{:keys [id name]} theater]]
             [:option {:key id
                       :value (int id)} name])])
        [:div.mb-3
         [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
          {:on-click (on-submit-fn @values)}
          form-title]]]])))
