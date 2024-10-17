FROM alpine/java:21-jdk

# Install necessary dependencies: curl, bash, and zip
RUN apk add --no-cache bash curl zip

# Run java -version to verify Java installation
RUN $JAVA_HOME/bin/java -version

WORKDIR /scripts

COPY LargeFileGenerator-fatjar.jar LargeFileGenerator-fatjar.jar

RUN java -jar LargeFileGenerator-fatjar.jar

## Copy the scripts into the image
COPY WordCounterScript1-fatjar.jar WordCounterScript1-fatjar.jar
COPY WordCounterScript2-fatjar.jar WordCounterScript2-fatjar.jar
COPY WordCounterScript3-fatjar.jar WordCounterScript3-fatjar.jar

