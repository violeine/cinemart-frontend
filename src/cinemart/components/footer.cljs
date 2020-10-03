(ns cinemart.components.footer
  (:require [cinemart.config :refer [powered-by]]))

(defn footer []
  [:footer.bg-gray-300.py-8.px-2
   [:div.xl:container.mx-auto
    [:p.text-xl "Powered by"
     (for [{:keys [alt src href]}
           powered-by]
       ^{:key alt}
       [:a {:href href}
        [:img.w-32.h-16.ml-4.inline {:src src
                                     :alt alt}]])]]])


