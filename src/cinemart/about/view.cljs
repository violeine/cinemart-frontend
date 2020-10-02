(ns cinemart.about.view)

(defn about-page [{:keys [classes]}]
  [:div {:class classes}
   [:p "This app is written in Clojurescript with reframe
       and written by me & homies"]])
