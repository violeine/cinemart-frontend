(ns cinemart.components.card)

(defn card [colors children]
  [:div.relative.accent-effect {:class [colors "w-full"]}
   [:div.absolute.accent-effect__corner.accent-effect__corner-top.left-0.top-0]
   [:div.absolute.accent-effect__corner.accent-effect__corner-bottom.right-0.bottom-0]
   [:div.absolute.accent-effect__edge.accent-effect__edge-left.left-0.top-0.bottom-0]
   [:div.absolute.accent-effect__edge.accent-effect__edge-bottom.left-0.right-0.bottom-0]
   [:div.accent-effect__children.text-white
    children]])
