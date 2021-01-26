(ns pod.lib
  (:refer-clojure :exclude [read read-string])
  (:require [bencode.core :as bencode]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.walk :as walk])
  (:import [java.io PushbackInputStream]))

(def ^:private stdin (PushbackInputStream. System/in))

(defn- write [v]
  (bencode/write-bencode System/out v)
  (.flush System/out))

(defn- read-string [^"[B" v]
  (String. v))

(defn- read []
  (bencode/read-bencode stdin))

(def ^:private debug? false)

(defn debug [& strs]
  (when debug?
    (binding [*out* (io/writer System/err)]
      (apply println strs))))

(defn- message->var [message]
  (-> (get message "var")
      (read-string)
      (symbol)))

(defn- message->args [message]
  (-> message
      (get "args")
      (read-string)
      (edn/read-string)))

(defn- reply! [value id]
  (write
   {"value" value
    "id" id
    "status" ["done"]}))

(defn- reply-error! [message data id]
  (write
   {"ex-message" message
    "ex-data" (pr-str data)
    "id" id
    "status" ["done" "error"]}))

(defn- invoke [definitions id message]
  (try
    (let [var (message->var message)
          args (message->args message)]
      (if-let [f (definitions var)]
        (reply! (pr-str (apply f args)) id)
        (throw (ex-info (str "Var not found: " var) {}))))
    (catch Throwable e
      (debug e)
      (reply-error! (ex-message e)
                    (assoc (ex-data e) :type (class e))
                    id))))

(defn- stringify-describe-map [describe-map]
  (walk/postwalk
   (fn [v]
     (if (ident? v) (name v)
         v))
   describe-map))

(defn- message->op [message]
  (-> message
      (get "op")
      (read-string)
      (keyword)))

(defn- message->id [message]
  (or (some-> (get message "id")
              (read-string))
      "unknown"))

(defn- read-message []
  (try (read)
       (catch java.io.EOFException _
         ::EOF)))

(defn start-loop [{:keys [definitions describe-map]}]
  (let [describe-map (stringify-describe-map describe-map)]
    (loop []
      (let [message (read-message)]
        (when-not (identical? ::EOF message)
          (let [op (message->op message)
                id (message->id message)]
            (case op
              :describe (write describe-map)
              :invoke (invoke definitions id message)
              :shutdown (System/exit 0)
              (reply-error! "Unknown op" {:op op} id))
            (recur)))))))

(defn gen-definitions [ns]
  (reduce (fn [acc k]
            (assoc acc
                   (symbol (str ns) (str k))
                   (resolve (symbol (str ns) (str k)))))
          {}
          (keys (ns-publics ns))))


(defn gen-describe-map [ns]
  {:name ns
   :vars (mapv (fn [n] {:name n}) (keys (ns-publics ns)))})
