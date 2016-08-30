(ns rolodex.generator
  (:require [clojure.string :as str]))

(defn random-uuid []
  (java.util.UUID/randomUUID))

(def names  ["Trish Turtle"
             "Master Sammy"
             "Brock Samson"
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
  (let [fname (str/replace name #" .*" "")
        lname (str/replace name #".* " "")
        nospaces (str/replace name #" " "")]
    (str "@" (rand-nth [nospaces
                        (-> (rand-nth [fname lname])
                            (str/lower-case)
                            (str (rand-int 100)))
                        (-> (rand-nth [fname lname])
                            (str/lower-case)
                            (str "_ebooks"))
                        (-> (rand-nth [fname lname])
                            (str/lower-case)
                            (#(str "horse_" %)))
                        (-> (rand-nth [fname lname])
                            (str/lower-case)
                            (#(str "_" % "_")))
                        (apply str (take (+ 3 (rand-int 5)) (repeatedly #(rand-nth (vec nospaces)))))

                        (str (apply str (take 4 (-> nospaces
                                                    (str/lower-case))))
                             "inator")]))))

(defn- gen-email [name]
  (str/lower-case
   (str (rand-nth [(str/replace name #" " ".")
                   (str/replace name #" " "_")
                   (str/replace name #" " "")]) "@"
        (rand-nth ["gmail.com"
                   "hotmail.com"
                   "yahoo.com"
                   "lambdaisland.com"]))))

(defn contact [name]
  {:id (random-uuid)
   :full-name name
   :email (gen-email name)
   :twitter (gen-twitter name)})
