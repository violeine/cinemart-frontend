(ns cinemart.notification.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.notification.subs :as noti]
            ["react-transition-group" :refer [TransitionGroup CSSTransition]]
            [cinemart.notification.events :as notify]))

(def noti-type {:danger {:css ["bg-red-500"]
                         :icon "d"}
                :warning {:css ["bg-yellow-500"]
                          :icon "w"}
                :success {:css ["bg-green-500"]
                          :icon "s"}
                :info {:css ["bg-blue-500"]
                       :icon "i"}})

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
         [:p.text-white.px-3.py-2.min-w-full {:class css}
          [:i.mr-4 icon]
          [:span.mr-5 text]
          [:a {:on-click #(rf/dispatch [::notify/kill-noti uuid])}
           "x"]]]])]))
