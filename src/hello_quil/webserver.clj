(ns hello_quil.webserver
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.handler ]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [run-server]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response]
            [ring.middleware.reload :refer :all]
            [stencil.core :refer [render-string]
            ]
            )
  )


(defn read-template [template]
  (slurp (clojure.java.io/resource template)))


(defroutes hello_quil
  (GET "/" request 
       (render-string (read-template "templates/canvas.html") {:ip (.getHostAddress (java.net.InetAddress/getLocalHost)) :req request}))
  (GET "/foo" [] "This is the foo page")
  (resources "/")
  )

(def my-wrap (-> hello_quil compojure.handler/site))

(defn -main []
  ;(run-jetty hello_quil {:port 5000}))
  (run-jetty (wrap-reload #'my-wrap '(hello_quil)) {:host "0.0.0.0" :port 5000}))
