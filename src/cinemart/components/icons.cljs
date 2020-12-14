(ns cinemart.components.icons)

(defn i-film [{:keys [class]}]
  [:svg {:xmlns "http://www.w3.org/2000/svg" :fill "none"
         :viewBox "0 0 24 24" :stroke "currentColor"
         :class class}
   [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth 2
           :d "M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z"}]])

(defn i-info-circle [{:keys [class]}]
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :viewBox "0 0 20 20"
         :fill "currentColor"
         :class class}
   [:path
    {:fillRule "evenodd"
     :d
     "M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
     :clipRule "evenodd"}]])

(defn i-check-circle [{:keys [class]}]
  [:svg
   {:xmlns "http://www.w3.org/2000/svg"
    :viewBox "0 0 20 20"
    :fill "currentColor"
    :class class}
   [:path
    {:fillRule "evenodd"
     :d
     "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
     :clipRule "evenodd"}]])

(defn i-exclaimation [{:keys [class]}]
  [:svg
   {:xmlns "http://www.w3.org/2000/svg"
    :viewBox "0 0 20 20"
    :fill "currentColor"
    :class class}
   [:path
    {:fill-rule "evenodd"
     :d
     "M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
     :clip-rule "evenodd"}]])

(defn i-shield-exclaimation [{:keys [class]}]
  [:svg
   {:xmlns "http://www.w3.org/2000/svg"
    :viewBox "0 0 20 20"
    :fill "currentColor"
    :class class}
   [:path
    {:fillRule "evenodd"
     :d
     "M10 1.944A11.954 11.954 0 012.166 5C2.056 5.649 2 6.319 2 7c0 5.225 3.34 9.67 8 11.317C14.66 16.67 18 12.225 18 7c0-.682-.057-1.35-.166-2.001A11.954 11.954 0 0110 1.944zM11 14a1 1 0 11-2 0 1 1 0 012 0zm0-7a1 1 0 10-2 0v3a1 1 0 102 0V7z"
     :clipRule "evenodd"}]])

(defn i-x [{:keys [class]}]
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor"
    :class class}
   [:path
    {:fillRule "evenodd",
     :d
     "M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z",
     :clipRule "evenodd"}]])

