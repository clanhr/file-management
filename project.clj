(defproject clanhr/file-management "0.6.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies.edn "https://raw.githubusercontent.com/clanhr/dependencies/master/dependencies.edn"

  :dependency-sets [:clojure :common]
  :dependencies [[clj-aws-s3 "0.3.10"]
                 [commons-codec/commons-codec "1.9"]]

  :plugins [[clanhr/shared-deps "0.2.6"]])
