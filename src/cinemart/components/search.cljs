(ns cinemart.components.search
  (:require [reagent.core :as r]
            [cinemart.components.input :refer [input select]]
            [cinemart.config :refer [json-string today]]
            [cinemart.components.moviediv :refer [movie-div manager-div]]))
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

(defn search-manager
  [li]
  (let [upcoming (filter #(> 0
                             (compare
                               (today) (:time %))) li)
        past (filter #(< 0
                         (compare
                           (today) (:time %))) li)
        chosen-list (r/atom upcoming)
        filtered-list (r/atom @chosen-list)]
    (fn [li]
      [:<>
       [:div.text-black.flex
        [select
         {:class ["w-1/6"]
          :on-change #(let [value (-> % .-target .-value)]
                        (if (= value "upcoming")
                          (do
                            (reset! chosen-list upcoming)
                            (reset! filtered-list upcoming))
                          (do
                            (reset! filtered-list past)
                            (reset! chosen-list past))))}
         [:option {
                   :value "upcoming"} "Upcoming Schedule"]
         [:option {
                   :value "past"} "Past Schedule"]]
        [:input.rounded.ml-2 {:type "text"
                 :class ["px-5" "w-5/6"]
                 :placeholder "type to search"
                 :on-change (fn [e]
                              (let [value (-> e .-target .-value)
                                    new-list (filter
                                               #(.includes
                                                  (.toLowerCase
                                                    (:movie_title %)) value)
                                               @chosen-list)]
                                (reset! filtered-list new-list)
                                ))}]]
       ; [:pre.text-black.bg-green-200 (json-string upcoming)]
       ; [:pre.bg-yellow-200.text-black (json-string past)]
       ^{:key @filtered-list}
       [:div
        (if (not (empty?  @filtered-list))
          (for [l @filtered-list
                :let [{:keys [id]} l
                      t (get-in li [0 :id])]]
            ^{:key (random-uuid)}
            [manager-div l])
          [:p.text-xl.text-white "Nothing found!"])]])))
