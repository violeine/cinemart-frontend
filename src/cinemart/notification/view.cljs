(ns cinemart.notification.view
  (:require [re-frame.core :as rf]
            [cinemart.notification.subs :as noti]
            [cinemart.notification.events :as notify]))

(def noti-type {:danger {:css ["bg-red-500"]
                         :icon "d"}
                :warning {:css ["bg-yellow-500"]
                          :icon "w"}
                :success {:css ["bg-green-500"]
                          :icon "s"}
                :info {:css ["bg-blue-500"]
                       :icon "i"}})

(defn notification
  []
  (let [notis @(rf/subscribe [::noti/get-noti])]
    [:div.fixed.bottom-0.right-0.mb-32
     (for
      [{:keys [uuid text class type]} notis
       :when (not (nil? uuid))
       :let [{{:keys [css icon]} type} noti-type]]
       [:div.mb-2.w-64.rounded.shadow-md.transition.duration-300.ease-in-out.transform
        {:class class

         :key uuid}
        [:p.text-white.px-3.py-2 {:class css}
         [:i.mr-4 icon]
         [:span.mr-5 text]
         [:a {:on-click #(rf/dispatch [::notify/noti-close uuid])}
          "x"]]])]))
