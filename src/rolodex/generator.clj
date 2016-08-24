(ns rolodex.generator
  (:require [clojure.string :as str]))

(defn random-uuid []
  (java.util.UUID/randomUUID))

(def names  ["Brock Samson"
             "Lana Kane"
             "Jake the Dog"
             "Todd Chavez"
             "The Monarch"
             "Thaddeous Venture"
             "Sergeant Hatred"
             "Princess Carolyn"
             "Princess Bubblegum"
             "Beemo"
             "Diane Nguyen"
             "Dean Venture"
             "Hank Venture"
             "Sarah Lynn"
             "Pam Poovey"
             "Ray Gillette"
             "Marceline Abadeer"])

(defn- gen-twitter [name]
  (str "@" (rand-nth [(str/replace name #" " "")
                      (-> name
                          (str/replace #" .*" "")
                          (str/lower-case)
                          (str (int (rand 100))))
                      (-> name
                          (str/lower-case)
                          (str/replace #" .*" "")
                          (str "_ebooks"))
                      (-> name
                          (str/lower-case)
                          (str/replace #".* " "horse_"))
                      (str (apply str (take 4 (-> name
                                                  (str/lower-case)
                                                  (str/replace #" " ""))))
                           "inator")
                      ])))

(defn- gen-email [name]
  (str/lower-case
   (str (rand-nth [(str/replace name #" " ".")
                   (str/replace name #" " "_")
                   (str/replace name #" " "")]) "@"
        (rand-nth ["gmail.com"
                   "hotmail.com"
                   "yahoo.com"
                   "lambdaisland.com"]))))

(defn gen-contact [name]
  {:id (random-uuid)
   :full-name name
   :email (gen-email name)
   :twitter (gen-twitter name)})

(def contacts (map gen-contact names))
