(ns cinemart.overlay.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.subs :as subs]
            ["react-transition-group" :refer [TransitionGroup CSSTransition]]
            [cinemart.components.icons :refer [i-x]]
            [cinemart.overlay.events :as events]))

(defn overlay
  []
  (let [{:keys [component class]} @(rf/subscribe [::subs/get])]
    [:div.fixed.w-screen.h-screen.top-0.right-0.bg-gray-800.bg-opacity-80
     {:on-click #(rf/dispatch [::events/close])
      :class [(when (nil? component) "hidden")]}
     (when (not (nil? component))
       [:> CSSTransition
        {:classNames "slide"
         :in true
         :appear true
         :timeout 250}
        [:div.absolute.transform.bg-red-300.p-5
         {:class
          (apply conj
                 ["-translate-x-1/2" "-translate-y-1/2"
                  "top-1/2" "left-1/2"]
                 class)
          :on-click (fn [e]
                      (.stopPropagation e)
                      (println "hi"))}
         [:div {:on-click #(rf/dispatch [::events/close])}
          [i-x {:class ["w-4" "absolute" "top-0" "right-0" "mr-1" "mt-1"]}]]
         [component]]])]))
