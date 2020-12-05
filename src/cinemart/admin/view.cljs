(ns cinemart.admin.view
  (:require [cinemart.components.container :refer [container]]
            [re-frame.core :as rf]
            [cinemart.admin.subs :as sub]
            [cinemart.config :refer [json-string]]))

(defn admin []
  (let [admin @(rf/subscribe [::sub/admin])]
    [container {:classes ["flex" "flex-col"]}
     [:<>
      [:h2.text-lg "admin panel"]
      [:pre.bg-green-300.text-black (json-string admin)]]]))

