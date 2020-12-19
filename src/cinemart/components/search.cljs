(ns cinemart.components.search
  (:require [reagent.core :as r]
            [cinemart.components.input :refer [input]]
            [cinemart.components.moviediv :refer [movie-div]]))

(defn search
  [li]
  (let [filtered-list (r/atom li)]
    (fn []
      [:<>
       [input {:type "text"
               :class ["w-full" "px-5"]
               :on-change (fn [e]
                            (let [value (-> e .-target .-value)
                                  new-list (filter
                                             #(.includes
                                                (.toLowerCase
                                                  (:title %)) value)
                                             li)]
                              (reset! filtered-list new-list)
                              ))}]
       (if
         (not
         (empty?
           @filtered-list))
         (for [l @filtered-list
               :let [{:keys [id]} l]]
           ^{:key id}
           [movie-div l])
         [:p.text-xl.text-white "Nothing found!"])])))
