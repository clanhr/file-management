(defproject file-management "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies.edn "https://raw.githubusercontent.com/clanhr/dependencies/master/dependencies.edn"

  :dependency-sets [:clojure :common]
  :dependencies [[clj-aws-s3 "0.3.10"]]

  :plugins [[clanhr/shared-deps "0.2.6"]])
