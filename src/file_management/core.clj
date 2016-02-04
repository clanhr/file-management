(ns file-management.core
  (:require [aws.sdk.s3 :as s3]))

(defn build-avatar-key
  [user-id]
  (str user-id "-avatar"))

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
                 avatar-value))
