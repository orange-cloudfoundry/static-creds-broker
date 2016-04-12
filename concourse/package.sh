#!/bin/sh

set -e 
set -x 

mvn package
zip -j static-creds-broker manifest.tmpl.yml manifest.tmpl.yaml-config.yml manifest.tmpl.remote-config.yml application.tmpl.yml target/*.jar