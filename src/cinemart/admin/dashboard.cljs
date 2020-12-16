(ns cinemart.admin.dashboard
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [cinemart.overlay.events :as overlay]
            [cinemart.admin.subs :as sub]
            [cinemart.admin.events :as admin-ev]
            [cinemart.config :refer [json-string to-vn-time]]))

(defn dashboard
  [{:keys [type arr order update-btn]} create-btn]
  [:div.p-2.mb-16
   [:div.flex.items-center.mb-8
    [:h2.mr-auto.text-3xl.text-gray-300.ml-2 (name type)]
    [:div.mr-32.bg-indigo-500
     create-btn]]
   (if (empty? arr)
     [:div "there's is none"]
     [:table.table-fixed.w-full.mt-2.text-left
      [:thead>tr
       [:th.w-16 "Index"]
       (for [col order]
         [:th  {:key (random-uuid)} (name col)])
       [:th "Action"]]
      [:tbody.align-baseline
       (map-indexed
        (fn [idx item]
          [:tr.border-t.border-gray-300.mb-8
           {:key (:id item)}
           [:td.py-2 idx]
           (for [col order]
             [:td {:key (random-uuid)}
              (if (= col :created_at)
                (to-vn-time (get item col))
                (get item col))])
           [:td.flex
            [:button.mr-2
             {:on-click #(rf/dispatch [::admin-ev/delete type (:id item)])} "Delete"]
            [:a.bg-indigo-600.block.p-1
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
                                    (rf/dispatch [::admin-ev/update type payload
                                                  {:id (:id item)
                                                   :idx idx}])))}])}])}
             (str "Update " (name type))]]])
        arr)]])])
