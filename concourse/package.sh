#!/bin/sh

set -e 
set -x 

mvn package
mkdir release/
cp manifest.tmpl.yml manifest.tmpl.yaml-config.yml manifest.tmpl.remote-config.yml application.tmpl.yml target/*.jar release/