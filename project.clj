(defproject reagent-example "0.1.0-SNAPSHOT"
 :description "FIXME: write description"
 :url "http://example.com/FIXME"
 :dependencies [[org.clojure/clojure "1.6.0"]
                [lib-noir "0.8.4"]
                [ring-server "0.3.1"]
                [selmer "0.6.8"]
                [com.taoensso/timbre "3.2.1"]
                [com.taoensso/tower "2.0.2"]
                [markdown-clj "0.9.44"]
                [environ "0.5.0"]
                [noir-exception "0.2.2"]
                [org.clojure/clojurescript "0.0-2280"]
                [reagent "0.4.2"]
                [cljs-ajax "0.2.6"]]

 :repl-options {:init-ns reagent-example.repl}
 :plugins [[lein-ring "0.8.10"]
           [lein-environ "0.5.0"]
           [lein-cljsbuild "1.0.3"]]
 :ring {:handler reagent-example.handler/app
        :init    reagent-example.handler/init
        :destroy reagent-example.handler/destroy}

 :cljsbuild
 {:builds
  [{:id "dev"
    :source-paths ["src-cljs"]
    :compiler
    {:optimizations :none
     :output-to "resources/public/js/app.js"
     :output-dir "resources/public/js/"
     :pretty-print true
     :source-map true}}
   {:id "release"
    :source-paths ["src-cljs"]
    :compiler
    {:output-to "resources/public/js/app.js"
     ;:source-map "resources/public/js/app.js.map"
     :optimizations :advanced
     :pretty-print false
     :output-wrapper false
     :closure-warnings {:non-standard-jsdoc :off}}}]}

 :profiles
 {:uberjar {:aot :all}
  :release {:ring {:open-browser? false
                      :stacktraces?  false
                      :auto-reload?  false}}
  :dev {:dependencies [[ring-mock "0.1.5"]
                       [ring/ring-devel "1.3.0"]
                       [pjstadig/humane-test-output "0.6.0"]]
        :injections [(require 'pjstadig.humane-test-output)
                     (pjstadig.humane-test-output/activate!)]
        :env {:dev true}}}
 :min-lein-version "2.0.0")
