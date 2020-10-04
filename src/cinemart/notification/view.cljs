(ns cinemart.notification.view
  (:require [re-frame.core :as rf]
    [cinemart.notification.subs :as noti]
    [cinemart.notification.events :as notify]))

(def noti-class {
                 :noti ["yo"]
                 :danger ["yo"]
                 :error  "yo"})
(defn notification
  []
  (let [notis @(rf/subscribe [::noti/get-noti])]
    [:div.fixed.bottom-0.right-0.mb-32
     (for
      [{:keys [uuid title class]} notis
       :when (not (nil? uuid))]
       [:div.mb-2.px-3.py-5.w-64.h-16.rounded.bg-gray-100.shadow-md.transition.duration-300.ease-in-out.transform
        {:class class
         :on-click #(rf/dispatch [::notify/noti-close uuid])
         :key uuid}
        title uuid])]))
