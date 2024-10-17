FROM alpine/java:21-jdk

# Install necessary dependencies: curl, bash, and zip
RUN apk add --no-cache bash curl zip

## Download and install Amazon Corretto JDK 21.0.5
#RUN curl -LO https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.tar.gz && \
#    mkdir -p /usr/local/java && \
#    tar -xzf amazon-corretto-21-x64-linux-jdk.tar.gz -C /usr/local/java
#
## Set JAVA_HOME and update PATH for all subsequent RUN steps
#ENV JAVA_HOME=/usr/local/java/amazon-corretto-21
#ENV PATH="$JAVA_HOME/bin:$PATH"

# Run java -version to verify Java installation
RUN $JAVA_HOME/bin/java -version

WORKDIR /scripts

COPY LargeFileGenerator-fatjar.jar LargeFileGenerator-fatjar.jar

RUN java -jar LargeFileGenerator-fatjar.jar

## Copy the scripts into the image
COPY WordCounterScript1-fatjar.jar WordCounterScript1-fatjar.jar
COPY WordCounterScript2-fatjar.jar WordCounterScript2-fatjar.jar
COPY WordCounterScript3-fatjar.jar WordCounterScript3-fatjar.jar

