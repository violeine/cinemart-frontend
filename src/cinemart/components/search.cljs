(ns cinemart.components.search
  (:require [reagent.core :as r]
            [cinemart.components.input :refer [input]]
            [cinemart.components.moviediv :refer [movie-div]]))
(def forc (r/atom 0))
(defn search
  [li]
  (let [filtered-list (r/atom li)]
    (fn [li]
      [:<>
       [input {:type "text"
               :class ["w-full" "px-5"]
               :placeholder "type to search"
               :on-change (fn [e]
                            (let [value (-> e .-target .-value)
                                  new-list (filter
                                             #(.includes
                                                (.toLowerCase
                                                  (:title %)) value)
                                             li)]
                              (reset! filtered-list new-list)
                              ))}]
       (if (not (empty?  @filtered-list))
         (for [l @filtered-list
               :let [{:keys [id]} l
                     t (get-in li [0 :id])]]
           ^{:key (random-uuid)}
           [movie-div l])
         [:p.text-xl.text-white "Nothing found!"])])))
