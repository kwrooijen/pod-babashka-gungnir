(defproject babashka/pod-babashka-gungnir
  #=(clojure.string/trim
     #=(slurp "resources/POD_BABASHKA_GUNGNIR_VERSION"))
  :description "Babashka pod for Gungnir"
  :url "https://github.com/kwrooijen/pod-babashka-gungnir"
  :scm {:name "git"
        :url "https://github.com/kwrooijen/pod-babashka-gungnir"}
  :license {:name "Eclipse Public License 1.0"
            :url "http://opensource.org/licenses/eclipse-1.0.php"}
  :source-paths ["src"]
  :resource-paths ["resources"]
  :dependencies [[org.clojure/clojure "1.10.2-rc3"]
                 [nrepl/bencode "1.1.0"]
                 [kwrooijen/gungnir "0.0.1-20210126.145919-15"
                  :exclusions [com.zaxxer/HikariCP org.slf4j/slf4j-api]]
                 [com.zaxxer/HikariCP "3.3.2-graal-RC4"]]

  :profiles {:uberjar {:global-vars {*assert* false}
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                                  "-Dclojure.spec.skip-macros=true"]
                       :aot :all
                       :main pod.babashka.gungnir}}
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass
                                    :sign-releases false}]])
