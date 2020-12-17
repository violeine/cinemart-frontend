(ns cinemart.components.forms.user
  (:require [reagent.core :as r]
            [cinemart.components.input :refer [input submit]]))

(defn user-form
  [{:keys [init-data on-submit-fn]}]
  (let [values (r/atom
                 (merge
                   {:mail ""
                    :password ""
                    :username ""
                    :dob "2011-10-05T14:48:00.000Z"
                    :fullname ""}
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
         [input {:type "text"
                 :name "email"
                 :required true
                 :value (:mail @values)
                 :on-change #(swap! values assoc :mail (-> % .-target .-value))
                 :placeholder "john@doe.com"}
          {:title "Email Address:"
           :class ["block"]}]
         ]
        [:div.mb-3
         [input {:type "text"
                 :name "username"
                 :required true
                 :value (:username @values)
                 :on-change #(swap! values assoc :username (-> % .-target .-value))
                 :placeholder "johndoe"}
          {:title "Username:"
           :class ["block"]}]
         ]
        [:div.mb-3
         [input {:type "text"
                 :name "fullname"
                 :required true
                 :value (:fullname @values)
                 :on-change #(swap! values assoc :fullname (-> % .-target .-value))
                 :placeholder "johndoe"}
          {:title "Full name:"
           :class ["block"]}]
         ]
        [:div.mb-6
         [input
          {:type "password"
           :placeholder "password"
           :on-change #(swap! values assoc :password (-> % .-target .-value))
           :value (:password @values)}
          {:title "Password:"
           :class ["block"]}]
         ]
        [:div.mb-3
         [submit
          {:class ["w-full" "bg-blue-300" "hover:bg-blue-400" "text-gray-100"]
           :on-click (on-submit-fn @values)}
          form-title]
         ]]])))
