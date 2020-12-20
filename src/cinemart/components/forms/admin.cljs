(ns cinemart.components.forms.admin
  (:require [reagent.core :as r]
            [cinemart.config :refer [json-string]]
            [cinemart.components.input :refer [input submit select]]))

(defn admin-form
  [{:keys [init-data on-submit-fn theaters type]}]
  (let [values (r/atom
                 (merge
                   {:mail ""
                    :password nil}
                   (if theaters {:theater
                                 (:id
                                   (first theaters))})
                   init-data))
        form-title (if init-data
                     (str "Update " (name type))
                     (str "Create " (name type)))]
    (fn []
      [:div.mx-auto.bg-gray-100.shadow.rounded.px-5.py-3
       [:p.text-xl.text-center.mb-8.text-gray-800
        form-title]
       [:form
        {:auto-complete "off"}
        [input {:type "text"
                :name "email"
                :required true
                :value (:mail @values)
                :on-change #(swap! values assoc :mail
                                   (-> % .-target .-value))
                :placeholder "john@doe.com"}
         {:title "Email Address:"
          :class ["block"]}]
        [input {:type "password"
                :placeholder "password"
                :on-change #(swap! values assoc :password (-> % .-target .-value))
                :value (:password @values)}
         {:title "Password:"
          :class ["block"]}]
        (when theaters
          [select {:class ["mb-2"]
                   :value (:theater @values)
                   :on-change #(swap! values assoc :theater
                                      (-> % .-target .-value))}
           (for [theater theaters
                 :let [{:keys [id name]} theater]]
             [:option {:key id
                      :value id} name])])
        [:div.mb-3
         [submit
          {:class ["w-full" "bg-blue-300" "hover:bg-blue-400" "text-gray-100"]
           :on-click (on-submit-fn @values)}
          form-title]
         ]]])))
