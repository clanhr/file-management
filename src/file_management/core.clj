(ns file-management.core
  (use clojure.java.io)
  (:import (org.apache.commons.codec.binary Base64))
  (:require [result.core :as result]
            [aws.sdk.s3 :as s3]
            [clojure.core.async :refer [thread]]))

(defn build-avatar-key
  [user-id]
  (str user-id "-avatar"))

(defn get-url
  [credentials file-key]
  (str "https://" (:bucket credentials) ".s3.amazonaws.com/" file-key))

(defn get-avatar-url
  [credentials user-id]
  (get-url credentials (build-avatar-key user-id)))

(defn get-default-avatar-url
  [credentials]
  (get-avatar-url credentials "default"))

(defn get-file-url
  [credentials file-key]
  (get-url credentials file-key))

(defn get-user-avatar
  "get user avatar url or default avatar"
  [credentials user-id]
  (try
    (s3/get-object credentials (:bucket credentials) (build-avatar-key user-id))
    (get-avatar-url credentials user-id)
    (catch Exception e
      (get-default-avatar-url credentials))))

(defn get-img-stream
  [src]
  (let [src-parsed (clojure.string/replace src #"^data:image/png;base64," "")
        data (Base64/decodeBase64 src-parsed)]
    (input-stream data)))

(defn delete-user-avatar
  [credentials user-id]
  (s3/delete-object credentials (:bucket credentials) (build-avatar-key user-id)))

(defn put-user-avatar
  [credentials user-id avatar-value]
  {:pre (some? (:access-key credentials))
        (some? (:secret-key credentials))
        (some? (:bucket credentials))
        (some? user-id)
        (some? avatar-value)}
    (s3/put-object credentials
                   (:bucket credentials)
                   (build-avatar-key user-id)
                   (get-img-stream avatar-value)
                   {:content-type "image/png"}
                   (s3/grant :all-users :read))
    (get-avatar-url credentials user-id))

(defn gets-file-key
  [file-key]
  (str file-key "_" (java.util.UUID/randomUUID)))

(defn put-file
  ([credentials file]
    (put-file credentials nil file))
  ([credentials file-key file]
  "Inputs: - credentials {:bucket
                          :access-key
                          :secret}
           - file-key We ensure an unique file key by concating an uuid,
           if nil we generate an unique file-key
           - file-value {:value ;file content
                         :content-type }
   Returns: file url to s3"
  (let [file-key (gets-file-key file-key)]
    (try
      (s3/put-object credentials
                     (:bucket credentials)
                     file-key
                     (:value file)
                     {:content-type (:content-type file)}
                     (s3/grant :all-users :read))
      (result/success (get-file-url credentials file-key))
      (catch Exception e
        (result/exception e))))))

(defn async-put-file
  ([credentials file]
   (async-put-file credentials nil file))
  ([credentials file-key file]
    (thread (put-file credentials file-key file))))
