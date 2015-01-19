(ns hello-quil.hello_quil_3d
  ;(:gen-class)
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [hello-quil.cljsdetect :as cd]
            ))

(def ROTPERSEC 1.0)
(def TOL 0.0001)


(defn img-loc [img]
  (if (cd/is-cljs?)
    (str "img/" img)
    (str "resources/public/img/" img)))

(defn setup 
  "This is a docstring I guess."
  []
  (do
    (q/frame-rate 120)
    (q/color-mode :rgb)
    (q/lights)
    {:x (/ (q/width) 2)
     :y (/ (q/height) 2)
     :z 0.0
     :throb 0.0
     :fps 120.0
     :texture (q/load-image (img-loc "earth_texture_web.jpg")) } ))

(defn update 
  "Functional update of the state, done per frame"
  [state]
  (assoc state 
         :throb 
         (let [newthrob (+ (:throb state) 
                           (/ (* 2 Math/PI) 
                              (* (q/current-frame-rate) ROTPERSEC)))]
           (if (> (* 2 Math/PI) (:throb state))
             newthrob
             (- newthrob (* 2 Math/PI))))
         :fps (q/current-frame-rate)
         ))



(defn text-circle-coords
  "Helper to get the tesselation of the sphere."
  [xt yt]
  (for [x (map 
            #(Math/sin %)
            (range (- (/ Math/PI 2)) (+ (/ Math/PI 2) TOL) (/ Math/PI xt)))
        y (map 
            #(Math/sin %)
            (range (- (/ Math/PI 2)) (+ (/ Math/PI 2) TOL) (/ Math/PI yt)))]
    [x y ]))

(defn tex-sphere 
  "Draws a sphere, with a texture applied
  extra:
  :tessel-u 20 ; tesselation count horiz
  :tessel-v 20 ; tesselation count vert
  "
  [x y radius texture & {:as args}]
  (let [coords (text-circle-coords (:tessel-u args 10) (:tessel-v args 10))]
    (do
      (q/begin-shape)
      (q/texture texture)
      (q/end-shape :close)
      )))

(defn draw 
  "Draws the state"
  [state]
  (do
    (q/stroke-weight 2)
    (q/stroke 0)
    (q/fill 255 255 255)
    (q/background 45.0)
    (q/lights)
    (q/ambient-light 100 100 150)
    (q/translate 
      (:x state) 
      (:y state) 
      (+ (:z state) (* 25.0 (Math/sin (:throb state)))))
    (q/rotate-y (*  (/ 
                  (- (- (/ (q/width) 2) (q/mouse-x))) 
                  (q/width)) Math/PI))
    (q/rotate-x (* (/ 
                  (- (/ (q/height) 2) (q/mouse-y)) 
                  (q/height)) Math/PI))
    ;(q/rect -50 -50 100 100)
    ;(q/box 100
    (q/sphere (/ 100 (Math/sqrt 2)))
    (q/begin-shape)
    ;(q/texture (:texture state))
    ;(q/vertex -100, -100, -100 0 0)
    ;(q/vertex  100, -100, -100 8192 0)
    ;(q/vertex    0,    0,  100 4096 4096)

    (q/end-shape :close)
    (q/stroke 128 0 0)
    (q/fill 128 0 0)
    (q/text-align :center :center)
    (q/text (str "FPS " (subs (str (:fps state)) 0 5) 
            (if (cd/is-cljs?) 
              (str "\nNginxProxy: " js/host_ip 
                   "\nJettySrv: " js/server_ip)
              "\nclojure-jvm")) 0 -100 0)
  )
)

(defn clamp [min- max+ value]
  (min max+ (max min- value))
  )

(defn mouse-wheel 
  "Receives a state and scroll event amount and turns that
  into a change in the Z state of the view."
  [state event]
  ;(println (str "Got event change " event "offset from :z " (:z state)))
  (assoc state :z (clamp -900 900 (- (:z state) (* 2.0 event)))))



(q/defsketch hello-quil-3d
  :title "Some 3d right here"
  ;:size :fullscreen
  :size [800 800]
  :setup setup
  :update update
  :draw draw
  :host "helloquilcanvas"
  :renderer :p3d
  :mouse-wheel mouse-wheel
  :no-start true
  :features [ :resizable ] ;:present
  :middleware [m/fun-mode])

