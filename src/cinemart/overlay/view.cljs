(ns cinemart.overlay.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cinemart.overlay.subs :as subs]
            ["react-transition-group" :refer [TransitionGroup CSSTransition]]
            [cinemart.overlay.events :as events]))

(defn overlay
  []
  (let [{:keys [component]} @(rf/subscribe [::subs/get])]
    (println component)
    [:> CSSTransition
     {:classNames "slide"
      :in true
      :appear true
      :timeout 250}
     [:div.absolute.w-32.h-32.top-0.right-0.bg-red-300
      (if (not (nil? component))
        (r/as-element component)
        "")]]))
