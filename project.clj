(defproject siili-core/blurhash "0.0.1"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/siili-core/blurhash"
  :license {:name "MIT Licence"
            :url "https://github.com/siili-core/blurhash/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :aliases {"analyze" ["eastwood" "{:linters [:unused-namespaces]}"]}
  :plugins [[jonase/eastwood "0.3.5"]]
  :jvm-opts ["-Dclojure.server.repl={:port 5555 :accept clojure.core.server/repl}"] ; notice that the map is not quoted.
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                  :username :env/clojars_username
                                  :password :env/clojars_password
                                  :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]]
  :repl-options {:init-ns blurhash.core})
