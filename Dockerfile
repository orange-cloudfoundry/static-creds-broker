FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y python
RUN apt-get update
RUN apt-get install -y python-pip
RUN pip install robotframework
RUN curl -L "https://cli.run.pivotal.io/stable?release=linux64-binary&source=github" | tar -zx -C /usr/local/bin