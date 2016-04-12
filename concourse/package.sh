#!/bin/sh

set -e 
set -x 

cd static-creds-broker/
mvn package
cp manifest.tmpl.yml manifest.tmpl.yaml-config.yml manifest.tmpl.remote-config.yml application.tmpl.yml target/*.jar ../release/