#!/bin/sh

set -e 
set -x 

export binary_directory=$PWD/release/
robot --pythonpath static-creds-broker/acceptance/classes/ --outputdir ./report/ static-creds-broker/acceptance/