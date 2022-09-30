(defproject tvirolai/blurhash "0.0.7-SNAPSHOT"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/tvirolai/blurhash"
  :license {:name "MIT License"
            :url  "https://github.com/tvirolai/blurhash/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.11.4"]
                 [org.clojure/core.rrb-vector "0.1.2"]]
  :aliases {"analyze"       ["eastwood" "{:linters [:unused-namespaces]}"]
            "test-browser"  ["doo" "chrome-headless" "test"]
            "test-advanced" ["doo" "chrome-headless" "advanced-test"]
            "test-node"     ["doo" "node" "node-test"]}
  :plugins [[jonase/eastwood "1.2.3"]
            [lein-doo "0.1.11"]
            [lein-shell "0.5.0"]
            [lein-cljsbuild "1.1.8"]]
  :doo {:build "test"
        :alias {:browsers [:chrome-headless :firefox]
                :all      [:browsers]}
        :paths {:karma "./node_modules/karma/bin/karma --port 3452 --log-level=error"}}
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :creds         :gpg
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
  :test-paths ["test/clj" "test/cljc" "test/cljs"]
  :cljsbuild {:builds [{:id           "test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler     {:output-to     "target/out/test.js"
                                       :output-dir    "target/out"
                                       :main          runner
                                       :optimizations :none}}
                       ;; {:id           "advanced-test"
                       ;;  :source-paths ["src" "test/cljc" "test/cljs"]
                       ;;  :compiler     {:output-to     "target/advanced_out/test.js"
                       ;;                 :output-dir    "target/advanced_out"
                       ;;                 :main          runner
                       ;;                 :optimizations :advanced}}
                       {:id           "node-test"
                        :source-paths ["src" "test/cljc" "test/cljs"]
                        :compiler     {:output-to     "target/node_out/test.js"
                                       :output-dir    "target/node_out"
                                       :main          runner
                                       :optimizations :none
                                       :target        :nodejs}}]})
