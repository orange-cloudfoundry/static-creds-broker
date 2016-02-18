FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y python
RUN apt-get update
RUN apt-get install -y python-pip
RUN pip install robotframework
RUN pip install cloudfoundry-client