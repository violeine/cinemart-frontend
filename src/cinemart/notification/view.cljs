(ns cinemart.notification.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.notification.subs :as noti]
            [cinemart.components.icons :refer [i-info-circle i-exclaimation
                                               i-shield-exclaimation i-check-circle i-x]]
            ["react-transition-group" :refer [TransitionGroup CSSTransition]]
            [cinemart.notification.events :as notify]))

(def noti-type {:danger {:css ["bg-red-500" "text-red-50"]
                         :icon i-shield-exclaimation}
                :warning {:css ["bg-yellow-500" "text-yellow-50"]
                          :icon i-exclaimation}
                :success {:css ["bg-green-500" "text-green-50"]
                          :icon i-check-circle}
                :info {:css ["bg-blue-500" "text-blue-100"]
                       :icon  i-info-circle}})

(defn notification
  []
  (let [notis @(rf/subscribe [::noti/get-noti])]
    [:> TransitionGroup
     {:component "div"
      :className (apply str (interpose " " ["fixed"
                                            "bottom-0"
                                            "right-0"
                                            "mb-32"]))}
     (for
      [{:keys [uuid text class type]} notis
       :let [{{:keys [css icon]} type} noti-type]]
       ^{:key uuid}
       [:> CSSTransition
        {:classNames "slide"
         :in true
         :appear true
         :timeout 250}
        [:div.mb-2.w-64.rounded.shadow-md.mr-5
         {:class class}
         [:p.text-white.px-3.py-2.min-w-full.flex.justify-between.rounded.shadow-md.items-center {:class css}
          [icon {:class ["w-8" "mr-5"]}]
          [:span.mr-5.flex-grow text]
          [:a {:on-click #(rf/dispatch [::notify/kill-noti uuid])}
           [i-x {:class ["w-4"]}]]]]])]))
