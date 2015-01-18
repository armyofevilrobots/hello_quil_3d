(defproject hello-quil "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main hello_quil.webserver
  :aot [hello_quil.webserver]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [quil "2.2.4"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.1"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [http-kit "2.1.16"]
                 [ring/ring-devel "1.3.1"] 
                 [markdown-clj "0.9.44"]
                 [selmer "0.7.0"]
                 [stencil "0.3.5"]
                 [org.clojure/clojurescript "0.0-2665"]
                 ]
  :plugins [[lein-cljsbuild "1.0.4"]]
  :hooks [leiningen.cljsbuild]
  :native-dependencies [[CGAL  :classifier "natives-osx"]]
  :jvm-opts ["-Djava.library.path=target/native/"]
  :cljsbuild {
              :builds [{
                        ; The path to the top-level ClojureScript source directory:
                        :source-paths ["src-cljsi"]
                        ; The standard ClojureScript compiler options:
                        ; (See the ClojureScript compiler documentation for details.)
                        :compiler {
                                   :output-dir "resources/public/js/development"
                                   :output-to "resources/public/js/hello_quil_3d.js"  ; default: target/cljsbuild-main.js
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  )
