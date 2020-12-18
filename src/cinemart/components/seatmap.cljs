(ns cinemart.components.seatmap)

(def alphabet (map char (range 97 123)))

(defn in? [arr i]
  (some #(= i %) arr))

(defn seatmap [{:keys [on-click-fn your-seat reserved-seat nrow ncolumn on-delete-fn]}]
  [:div.flex.justify-center.p-4
   [:div.inline-block
    (map-indexed
     (fn [idx itm]
       [:div.flex.mb-2.text-black.justify-between
        {:key idx}
        (for [c (range 1 (inc ncolumn))
              :let [d1 (+ (* idx ncolumn) c)
                    name-seat (.toUpperCase (str itm c))]]
          [:div.mr-2.text-center.lg:w-10.lg:h-10.w-auto.xl:w-16.xl:h-16
           {:key c
            :on-click
            (when (not (in? reserved-seat d1))
              (if
               (in? your-seat d1)
                #(on-delete-fn d1 name-seat)
                #(on-click-fn d1 name-seat)))
            :class [(cond
                      (in? reserved-seat d1) "bg-gray-300"
                      (in? your-seat d1) "bg-blue-300"
                      :else "bg-green-200")]}
           [:span.block
            name-seat]
           ])])
     (take nrow alphabet))]])
