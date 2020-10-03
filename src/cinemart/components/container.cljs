(ns cinemart.components.container)

(defn container
  [{:keys [classes]}  children]
  [:div.bg-gray-300.flex-1.mx-auto.w-full.xl:container
   {:class classes}
   children])
