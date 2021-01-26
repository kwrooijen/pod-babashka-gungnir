(ns pod.babashka.gungnir-test
  (:require [babashka.pods :as pods]
            [clojure.test :refer [deftest is testing]]))

(pods/load-pod (if (= "native" (System/getenv "POD_TEST_ENV"))
                 "./pod-babashka-hsqldb"
                 ["lein" "run" "-m" "pod.babashka.gungnir"]))
