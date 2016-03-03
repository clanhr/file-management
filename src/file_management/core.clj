(ns file-management.core
  (use clojure.java.io)
  (:import (org.apache.commons.codec.binary Base64))
  (:require [aws.sdk.s3 :as s3]))

(defn build-avatar-key
  [user-id]
  (str user-id "-avatar"))

(defn get-avatar-url
  [credentials user-id]
  (str "https://" (:bucket credentials) ".s3.amazonaws.com/" (build-avatar-key user-id)))

(defn get-default-avatar-url
  [credentials]
  (get-avatar-url credentials "default"))

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
