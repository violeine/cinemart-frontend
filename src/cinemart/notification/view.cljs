(ns cinemart.notification.view
  (:require [re-frame.core :as rf]
    [cinemart.notification.subs :as noti]
    [cinemart.notification.events :as notify]))

(defn notification
  []
  (let [notis @(rf/subscribe [::noti/get-noti])]
    [:div.fixed.bottom-0.right-0.mb-32
     (map-indexed
       (fn
         [i {:keys [uuid title class]}]
         [:div.mb-2.px-3.py-5.w-64.h-16.rounded.bg-gray-100.shadow-md.transition.duration-300.ease-in-out.transform
          {:class class
           :key uuid
           :on-click #(rf/dispatch-sync
                        [::notify/close-noti i])}
          i title uuid])
       notis)]))
