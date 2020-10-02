(ns cinemart.home.view
  )

(defn home-page [{:keys [classes]}]
  [:div.flex.flex-col.justify-around {:class classes}
   (for [x (range 10)]
     ^{:key x} [:p.p-16.bg-blue-300.mb-2 "this is a home pages"])])
