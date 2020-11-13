(ns cinemart.components.footer
  (:require [cinemart.config :refer [powered-by]]))

(defn footer []
  [:footer.bg-gray-300.py-8.px-2
   [:div.xl:container.mx-auto
    [:div.flex
     [:p.text-xl {:class ["w-1/2"]}
      "Powered by"
      (for [{:keys [alt src href]}
            powered-by]
        ^{:key alt}
        [:a {:href href}
         [:img.w-32.h-16.ml-4.inline {:src src
                                      :alt alt}]])]
     [:p.text-xl.flex
      {:class ["w-1/2"]}
      [:span.mr-2 "Written by "]
      [:a.mr-2 {:href "https://github.com/violeine/cinemart-frontend"
           :target "_blank"}
       [:img.w-16.rounded-full.h-16
        {:src "https://github.com/violeine.png"
         :alt "violeine"}]]
      [:a.mr-2 {:href "https://github.com/anhvuk13/cinemart-backend"
           :target "_blank"}
       [:img.w-16.rounded-full.h-16
        {:src "https://github.com/anhvuk13.png"}]]]]]])


