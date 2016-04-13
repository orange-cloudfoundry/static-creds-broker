#!/bin/sh

set -e 
set -x 

robot --pythonpath static-creds-broker/acceptance/classes/ static-creds-broker/acceptance/
cp output.xml report.html log.html ./report/