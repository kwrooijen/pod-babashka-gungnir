(ns pod.babashka.gungnir
  (:require
   [gungnir.changeset]
   [gungnir.database]
   [gungnir.factory]
   [gungnir.migration]
   [gungnir.model]
   [gungnir.query]
   [gungnir.transaction]
   [pod.lib])
  (:gen-class))

(def describe-map
  {:format :edn
   :namespaces
   [(pod.lib/gen-describe-map 'gungnir.changeset)
    (pod.lib/gen-describe-map 'gungnir.database)
    (pod.lib/gen-describe-map 'gungnir.factory)
    (pod.lib/gen-describe-map 'gungnir.migration)
    (pod.lib/gen-describe-map 'gungnir.model)
    (pod.lib/gen-describe-map 'gungnir.query)
    (pod.lib/gen-describe-map 'gungnir.transaction)]
   :opts {:shutdown {}}})

(def ^:private definitions
  (merge (pod.lib/gen-definitions 'gungnir.changeset)
         (pod.lib/gen-definitions 'gungnir.database)
         (pod.lib/gen-definitions 'gungnir.factory)
         (pod.lib/gen-definitions 'gungnir.migration)
         (pod.lib/gen-definitions 'gungnir.model)
         (pod.lib/gen-definitions 'gungnir.query)
         (pod.lib/gen-definitions 'gungnir.transaction)))

(defn -main [& args]
  (pod.lib/debug describe-map)
  (pod.lib/start-loop
   {:definitions definitions
    :describe-map describe-map
    :args args}))
