(ns cinemart.components.forms.theater
  (:require [reagent.core :as r]
            [cinemart.components.input :refer [submit input]]))

(defn theater-form
  [{:keys [init-data on-submit-fn]}]
  (let [values (r/atom
                 (merge
                   {:theater {:name    ""
                              :address ""}}
                   init-data))
        form-title (if (:manager init-data)
                     "Create Theater"
                     "Update Theater")]
    (fn []
      [:div.mx-auto.bg-gray-100.shadow.rounded.px-5.py-3
       [:p.text-xl.text-center.mb-8.text-gray-800
        form-title]
       [:form
        {:auto-complete "off"}
        [:div.mb-3
         [input {:type "text"
                 :name "name"
                 :required true
                 :value (get-in @values [:theater :name])
                 :on-change #(swap! values assoc-in [:theater :name] (-> % .-target .-value))
                 :placeholder "Cinemarit"}
          {:class ["block"]
           :title
           "Theater Name:"}]
         ]
        [:div.mb-6
         [input {:type "text"
                 :placeholder "address"
                 :on-change #(swap! values assoc-in  [:theater :address] (-> % .-target .-value))
                 :value (get-in @values [:theater :address])}
          {:title "Theater Address:"
           :class ["block"]}]
         ]
        (when (:manager init-data)
          [:<>
           [:p.text-xl.text-center.text-gray-800.text-gray-800.mb-2
            "Manager"]
           [:div.mb-3
            [input {:type "text"
                    :name "name"
                    :required true
                    :value (get-in @values [:manager :mail])
                    :on-change #(swap! values assoc-in [:manager :mail] (-> % .-target .-value))
                    :placeholder "email"}
             {:title "Manager Email:"
              :class ["block"]}
             ]]
           [:div.mb-6
            [input {:type "password"
                    :name "password"
                    :placeholder "password"
                    :on-change #(swap! values assoc-in  [:manager :password] (-> % .-target .-value))
                    :value (get-in @values [:manager :password])}
             {:title "Password:"
              :class ["block"]}]
            ]])
        [:div.mb-3
         [submit
          {:class ["w-full" "bg-blue-300" "hover:bg-blue-400" "text-gray-100"]
           :on-click (on-submit-fn (if (:manager init-data)
                                     @values
                                     (:theater @values)))
           }
          form-title]
         ]]])))


