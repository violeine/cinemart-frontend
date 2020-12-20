(ns cinemart.overlay.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.subs :as subs]
            ["react-transition-group" :refer [TransitionGroup CSSTransition]]
            [cinemart.components.icons :refer [i-x]]
            [cinemart.overlay.events :as events]))

(defn overlay
  []
  (let [{:keys [component class]} @(rf/subscribe [::subs/get])
        open @(rf/subscribe [::subs/hidden])]
    [:> TransitionGroup
     {:on-click #(rf/dispatch [::events/close])
      :component "div"
      :className
      (apply str (interpose " "
                            ["fixed" "z-50" "w-screen" "h-screen" "top-0" "right-0" "bg-gray-800" " bg-opacity-80"
                             (if open "" "hidden")]))}
     (when (not (nil? component))
       [:> CSSTransition
        {:classNames "overlay"
         :in true
         :appear true
         :timeout 250}
        [:div.absolute.bg-red-300.p-5.z-50
         {:class
          (apply conj
                 ["top-1/2" "left-1/2" "transform" "-translate-x-50" "-translate-y-50"]
                 class)
          :on-click (fn [e]
                      (.stopPropagation e)
                      (println "hi"))}
         [:div {:on-click #(rf/dispatch [::events/close])}
          [i-x {:class ["w-4" "absolute" "top-0" "right-0" "mr-1" "mt-1"]}]]
         [component]]])]))


