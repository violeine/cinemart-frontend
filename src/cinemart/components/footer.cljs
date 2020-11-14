(ns cinemart.components.footer
  (:require [cinemart.config :refer [powered-by]]))

(defn footer []
  [:footer.py-8.px-2.bg-gray-800
   [:div.xl:container.mx-auto.px-2.text-gray-400.text-md
    [:div.flex.flex-col.lg:flex-row
     [:p.flex.mt-4 {:class ["w-1/2"]}
      "Powered by"
      (for [{:keys [alt src href]}
            powered-by]
        ^{:key alt}
        [:a {:href href}
         [:img.ml-4.max-w-sm.h-12 {:src src
                                   :alt alt}]])]
     [:p.flex.mt-8.lg:mt-4
      {:class ["w-1/2"]}
      [:span.mr-2 "Written by "]
      [:a.mr-2 {:href "https://github.com/violeine/cinemart-frontend"
                :target "_blank"}
       [:img.w-12.rounded-full.h-12
        {:src "https://github.com/violeine.png"
         :alt "violeine"}]]
      [:a.mr-2 {:href "https://github.com/anhvuk13/cinemart-backend"
                :target "_blank"}
       [:img.w-12.rounded-full.h-12
        {:src "https://github.com/anhvuk13.png"}]]]]]])


