(defproject siili-core/blurhash "0.0.5"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/siili-core/blurhash"
  :license {:name "MIT License"
            :url "https://github.com/siili-core/blurhash/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 ;; This version of rrb-vector needs to be required for now, otherwise cljsbuild will fail.
                 ;; See the issue CRRBV-19.
                 [org.clojure/core.rrb-vector "0.0.14"]]
  :aliases {"analyze" ["eastwood" "{:linters [:unused-namespaces]}"]
            "test-browser" ["doo" "chrome-headless" "test"]
            "test-advanced" ["doo" "chrome-headless" "advanced-test"]
            "test-node" ["doo" "node" "node-test"]}
  :plugins [[jonase/eastwood "0.3.5"]
            [lein-shell "0.5.0"]
            [lein-doo "0.1.10"]
            [lein-cljsbuild "1.1.7"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass
                                    :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["shell" "git" "commit" "-am" "Version ${:version} [ci skip]"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["shell" "git" "commit" "-am" "Version ${:version} [ci skip]"]
                  ["vcs" "push"]]
  :repl-options {:init-ns blurhash.core}
  :test-paths ["test/clj" "test/cljc"]
  :doo {:paths {:karma "./node_modules/.bin/karma"}
        :karma {:config {"plugins" ["karma-junit-reporter"]
                         "reporters" ["progress", "junit"]
                         "junitReporter" {"outputDir" "target/results/cljs"}}}}

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler {:output-to "target/out/test.js"
                                   :output-dir "target/out"
                                   :main blurhash.runner
                                   :optimizations :none}}
                       {:id "advanced-test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler {:output-to "target/advanced_out/test.js"
                                   :output-dir "target/advanced_out"
                                   :main blurhash.runner
                                   :optimizations :advanced}}
                       {:id "node-test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler {:output-to "target/node_out/test.js"
                                   :output-dir "target/node_out"
                                   :main blurhash.runner
                                   :optimizations :none
                                   :target :nodejs}}]})
