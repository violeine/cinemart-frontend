(ns cinemart.components.lists
  (:require [cinemart.components.card :refer [card]]
            [reitit.frontend.easy :refer [href]]
            [cinemart.config :refer [json-string]]))

(defn lists
  ([n l b]
   [:div.px-3.py-5
    (if b
      [:div.flex.items-baseline
       [:div.text-indigo-300.text-2xl.ml-8.mr-auto n]
       [:a.text-indigo-200.mr-16.text-lg
        {:href (href :cinemart.router/genres {:id b})}
        "See more"]]
      [:div.text-indigo-300.text-2xl.ml-8 n])
    [:div.flex.overflow-x-auto.overflow-y-visible.mx-8.pt-3.pb-1.text-gray-700
     (for [dv l
           :let [{:keys [runtime title poster_path id]} dv]]

       [:div.mr-5
        {:key id}
        [card
         "text-indigo-400"
         [:a.relative.text-indigo-100.text-lg.block
          {:href (href :cinemart.router/movie {:id id})
           :class ["w-64"] }
          [:img.w-64 {:src poster_path}]
          [:div.absolute.bottom-0.left-0.inline-flex.flex-col
           [:div
            [:span.bg-indigo-400.inline-block.m-0.px-1.font-bold title]]
           ]]]])]])
  ([n l]
   [ lists n l false ]))
