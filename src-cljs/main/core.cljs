(ns main.core
 (:require [reagent.core :as reagent :refer [atom]]
           [ajax.core :refer [POST]]))

(def state (atom {:doc {} :saved? false}))

(defn set-value! [id value]
  (swap! state assoc :saved? false)
  (swap! state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @state [:doc id]))

(defn row [label & body]
  [:div.row
   [:div.col-md-2 [:span label]]
   [:div.col-md-3 body]])

(defn text-input [id label]
  [row label
   [:input {:type "text"
            :class "form-control"
            :value (get-value id)
            :on-change #(set-value! id (-> % .-target .-value))}]])

(defn list-item [id k v selections]
  (letfn [(handle-click! []
            (swap! selections update-in [k] not)                         
            (set-value! id (->> @selections (filter second) (map first))))]
    [:li {:class (str "list-group-item" (if (k @selections) " active"))
          :on-click handle-click!}
      v]))

(defn selection-list [id label & items]
  (let [selections (->> items (map (fn [[k]] [k false])) (into {}) atom)]    
    (fn []
      [:div.row
       [:div.col-md-2 [:span label]]
       [:div.col-md-5
        [:div.row
         (for [[k v] items]
          [list-item id k v selections])]]])))

(defn save-doc []
  (POST (str js/context "/save")
        {:params (:doc @state)
         :handler (fn [_] (swap! state assoc :saved? true))}))

(defn home []
  [:div
    [:div.page-header [:h1 "Reagent Form"]]

    [text-input :first-name "First name"]
    [text-input :last-name "Last name"]
    [selection-list :favorite-drinks "Favorite drinks"
     [:coffee "Coffee"] [:beer "Beer"] [:crab-juice "Crab juice"]]

   (if (:saved? @state)
     [:p "Saved"]
     [:button {:type "submit"
              :class "btn btn-default"
              :on-click save-doc}
     "Submit"])])

;;start the app
(reagent/render-component [home] (.getElementById js/document "app"))
