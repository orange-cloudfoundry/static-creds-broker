#!/bin/sh

if [ ! -z "$APPLICATION_YML" ]
then
  echo "APPLICATION_YML environment variable is set. Will use it to create /home/vcap/app/application.yml."
  echo "${APPLICATION_YML}" > /home/vcap/app/application.yml
  echo "/home/vcap/app/application.yml content is :"
  cat /home/vcap/app/application.yml
fi