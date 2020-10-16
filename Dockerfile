# Dockerfile to build container for unit testing.
#
# To build the image, run the following from this directory:
#   docker build -t testing .
#
# To run the tests, use
#   docker run testing
#

FROM openjdk:11

# Install Apache Ant
RUN apt-get update && apt-get install -y ant

ENV USER root

# Ant build fails if the repo dir isn't named beast-outcore
RUN mkdir /root/beast-outcore
WORKDIR /root/beast-outcore

ADD . ./

CMD ant travis
