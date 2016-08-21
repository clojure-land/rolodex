(ns rolodex.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))

(defstylesheet screen
  {:output-to "resources/public/css/screen.css"}
  [:body
   {:font-family "sans-serif"
    :font-size (px 20)
    :line-height 1.5
    :background-color "#aaaaaa"}])
