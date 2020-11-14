(ns cinemart.components.container)

(defn container
  [{:keys [classes]}  children]
  [:div.bg-gray-800.flex-1.mx-auto.w-full.xl:container.shadow-md.text-gray-100.pt-5
   {:class classes}
   children])
