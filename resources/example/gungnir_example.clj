(require '[babashka.pods :as pods])
(pods/load-pod "./pod-bb-gungnir.sh")

(ns main
  (:require
   [gungnir.model]
   [gungnir.changeset]
   [gungnir.database]
   [gungnir.migration]
   [gungnir.query]
   [gungnir.transaction]))

(def db-spec
  {:adapter       "postgresql"
   :username      "postgres"
   :password      "postgres"
   :database-name "postgres"
   :server-name   "localhost"
   :port-number   5432})

(def migrations
  [{:id :users
    :up [[:table/create {:table :users}
          [:column/add
           [:email {:unique true} :string]
           [:password :string]
           :gungnir/timestamps]]]
    :down [[:table/drop :user]]}])

(def models
  {:user
   [:map
    {:table :users}
    [:user/email {:before-save [:string/lower-case]
                  :before-read [:string/lower-case]}
     :string]
    [:user/password
     {:before-save [:encrypt]}
     :string]]})

(gungnir.database/make-datasource! db-spec)

(gungnir.model/register! models)

(gungnir.migration/migrate! migrations)

;; Doesn't work
;; (defmethod gungnir.model/before-save :encrypt [_ v]
;;   "ENCRYPTED")

;; Doesn't work

(gungnir.transaction/execute!
 (fn []
   (-> {:user/email "anon@mail.com"
        :user/password "some-password"}
       (gungnir.changeset/create)
       (gungnir.query/save!))))

;; (-> {:user/email "user@mail.com"
;;      :user/password "some-password"}
;;     (gungnir.changeset/create)
;;     (gungnir.query/save!)
;;     (println))

(gungnir.query/all! :user)
