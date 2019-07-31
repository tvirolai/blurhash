(defproject blurhash "0.0.1"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/siili-core/blurhash"
  :license {:name "MIT Licence"
            :url "https://github.com/siili-core/blurhash/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :aliases {"analyze" ["eastwood" "{:linters [:unused-namespaces]}"]}
  :plugins [[jonase/eastwood "0.3.5"]]
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env
                                     :password :env
                                     :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]]
  :repl-options {:init-ns blurhash.core})
