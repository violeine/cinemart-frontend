(ns cinemart.components.forms.theater
  (:require [reagent.core :as r]))

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
      [:div.m-auto.bg-gray-100.shadow.rounded.px-5.py-3
       [:p.text-xl.text-center.mb-8.text-gray-800
        form-title]
       [:form
        {:auto-complete "off"}
        [:div.mb-3
         [:label.block.text-gray-700.font-bold.mb-1 {:for "name"}
          "Name:"]
         [:input.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
          {:type "text"
           :name "name"
           :required true
           :value (get-in @values [:theater :name])
           :on-change #(swap! values assoc-in [:theater :name] (-> % .-target .-value))
           :placeholder "Cinemarit"}]]
        [:div.mb-6
         [:label.block.text-gray-700.font-bold.mb-1 {:for "address"}
          "address:"]
         [:input.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
          {:type "text"
           :placeholder "address"
           :on-change #(swap! values assoc-in  [:theater :address] (-> % .-target .-value))
           :value (get-in @values [:theater :address])}]]
        (when (:manager init-data)
          [:<>
           [:div.mb-3
            [:label.block.text-gray-700.font-bold.mb-1 {:for "email"}
             "Email"]
            [:input.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
             {:type "text"
              :name "name"
              :required true
              :value (get-in @values [:manager :mail])
              :on-change #(swap! values assoc-in [:manager :mail] (-> % .-target .-value))
              :placeholder "email"}]]
           [:div.mb-6
            [:label.block.text-gray-700.font-bold.mb-1 {:for "password"}
             "password"]
            [:input.border.border-gray-300.rounded.px-3.py-2.w-64.leading-tight.text-gray-700
             {:type "password"
              :name "password"
              :placeholder "password"
              :on-change #(swap! values assoc-in  [:manager :password] (-> % .-target .-value))
              :value (get-in @values [:manager :password])}]]])
        [:div.mb-3
         [:button.w-full.bg-blue-300.py-2.rounded-lg.shadow-lg.mb-2.text-gray-100.font-bold.focus:outline-none.focus:shadow-outline.hover:bg-blue-400
          {:on-click (on-submit-fn (if (:manager init-data)
                                     @values
                                     (:theater @values)))}
          form-title]]]])))


