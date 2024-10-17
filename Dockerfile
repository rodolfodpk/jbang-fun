FROM alpine/java:21-jdk

# Install necessary dependencies: curl, bash, and zip
RUN apk add --no-cache bash curl zip

# Run java -version to verify Java installation
RUN $JAVA_HOME/bin/java -version

# Download and install jbang
RUN curl -s "https://get.sdkman.io" | bash
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install jbang"

# Verify jbang installation
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && jbang --version"

WORKDIR /scripts

COPY LargeFileGenerator.java LargeFileGenerator.java
COPY WordCounterScript1.java WordCounterScript1.java
COPY WordCounterScript2.java WordCounterScript2.java
COPY WordCounterScript3.java WordCounterScript3.java

RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && jbang export fatjar LargeFileGenerator.java"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && jbang export fatjar WordCounterScript1.java"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && jbang export fatjar WordCounterScript2.java"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && jbang export fatjar WordCounterScript3.java"

RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && ls -lh"

RUN java -jar LargeFileGenerator-fatjar.jar

