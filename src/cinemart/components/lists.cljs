(ns cinemart.components.lists
  (:require [cinemart.components.card :refer [card]]
            [reitit.frontend.easy :refer [href]]
            [cinemart.config :refer [json-string]]))

(defn lists
  [n l]
  [:div.px-3.py-5
   [:div.text-indigo-300.text-2xl.ml-8 n]
   [:div.flex.overflow-x-scroll.overflow-y-visible.mx-8.pt-3.pb-1.text-gray-700.track-current
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
           [:div
            [:span.bg-indigo-400.inline-block.m-0.px-1.text-indigo-200 "as"]]
           [:div
            [:span.bg-indigo-400.inline-block.m-0.px-1 runtime]]]]]])]])
