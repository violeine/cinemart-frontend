(ns cinemart.auth.signup
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]
            [cinemart.components.container :refer [container]]
            [cinemart.components.forms.user :refer [user-form]]
            [cinemart.auth.events :as events]))

(defn signup
  [{:keys [classes]}]
  [container {:classes ["flex" "flex-col" "justify-center"]}
       [user-form
        {:on-submit-fn (fn [payload]
                         (fn [e]
                           (.preventDefault e)
                           (dispatch [::events/signup payload])))}]
       ])


