(ns datomic-demo.core
  (:require [datomic.client.api :as d]))

(def cfg {:server-type :peer-server
          :access-key "myaccesskey"
          :secret "mysecret"
          :endpoint "localhost:8998"
          :validate-hostnames false})
(def client (d/client cfg))
(def conn (d/connect client {:db-name "hello"}))

(
 (fn []
   (let [movie-schema [{:db/ident       :movie/title
                        :db/valueType   :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/doc         "Movie Title"}
                       {:db/ident       :movie/genre
                        :db/cardinality :db.cardinality/one
                        :db/valueType   :db.type/string
                        :db/doc         "Genre of the fuckin Movie"}
                       {:db/ident       :movie/release-year
                        :db/cardinality :db.cardinality/one
                        :db/valueType   :db.type/long
                        :db/doc         "Year of release"}]]
     (d/transact conn {:tx-data movie-schema})
     )
   )
 )

(defn query-data []
  (let [q '[:find ?title ?release-year ?genre
            :where [?x :movie/title ?title]
                   [?x :movie/release-year ?release-year]
                   [?x :movie/genre ?genre]]
        db (d/db conn)]
    (d/q q db)
    )
  )

(defn read-value [msg]
  (println (str "please enter " msg ": "))
  (read-line)
  )
(defn read-int [msg]
  (Integer/parseInt (read-value msg))
  )

(defn assert-movie []
  (let [data [{:movie/title (read-value "title")
               :movie/release-year (read-int "release year")
               :movie/genre (read-value "genre")}]
        db (d/db conn)]
    (d/transact conn {:tx-data data})
    )
  )


