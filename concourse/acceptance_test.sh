#!/bin/sh

set -e 
set -x 

export JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:///dev/urandom"
export binary_directory=$PWD/release/
robot --pythonpath static-creds-broker/acceptance/classes/ --outputdir ./report/ static-creds-broker/acceptance/