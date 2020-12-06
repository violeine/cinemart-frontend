(ns cinemart.admin.dashboard
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [cinemart.overlay.events :as overlay]
            [cinemart.admin.subs :as sub]
            [cinemart.admin.events :as admin-ev]
            [cinemart.config :refer [json-string]]))

(defn dashboard
  [{:keys [type arr order update-btn]} create-btn]
  [:div
   [:div.flex
    [:p.mr-auto (name type)]
    create-btn]
   (if (empty? arr)
     [:div "there's is none"]
     [:<>
      [:div.flex
       [:div.mr-2.w-32 "idx"]
       (for [col order]
         [:div.mr-2.w-48  {:key (random-uuid)} (name col)])]
      (map-indexed
       (fn [idx item]
         [:div.flex
          {:key (:id item)}
          [:div.mr-2.w-32 idx]
          (for [col order]
            [:div.mr-2.w-48 {:key (random-uuid)}  (get item col)])
          [:button
           {:on-click #(rf/dispatch [::admin-ev/delete type (:id item)])} "delete this"]
          [:a.bg-indigo-600.text-md.px-2.py-2
           {:on-click #(rf/dispatch
                        [::overlay/open
                         {:component
                          (fn []
                            [update-btn
                             {:type type
                              :init-data
                              (if (= type :theaters)
                                {:theater (-> item
                                              (dissoc :id)
                                              (dissoc :created_at)
                                              (dissoc :theater_id))}
                                (-> item
                                    (dissoc :id)
                                    (dissoc :theater_id)
                                    (dissoc :theater_name)
                                    (dissoc :created_at)))
                              :on-submit-fn
                              (fn [payload]
                                (fn [e]
                                  (.preventDefault e)
                                  (rf/dispatch [::admin-ev/update type payload {:id (:id item)
                                                                                :idx idx}])))}])}])}
           (str "Update " (name type))]])
       arr)])])
