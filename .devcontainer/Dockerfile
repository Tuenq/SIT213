#-------------------------------------------------------------------------------------------------------------
# JAVA 11 + ANT Project
#-------------------------------------------------------------------------------------------------------------

FROM openjdk:11-jdk

#-------------------Configure apt-----------------------------------------------------------------------------
ENV DEBIAN_FRONTEND=noninteractive
RUN echo "##########> Install tools through apt <##########"
RUN apt-get update && apt-get -y upgrade \
    && apt-get -y install --no-install-recommends apt-utils dialog 2>&1 \
    && apt-get -y install git procps curl lsb-release man

#-------------------Install Apache Ant Tools------------------------------------------------------------------
ARG ANT_VERSION=1.10.7
ARG ANT_SHA=838ce70c7dbd2b53068ce17b169c0b3fbed5e0ab7be5c707f052418fb6a4a1620f2d4017ceca1379cd25edce3e46d70bb2b5de4e1c5c52e2e1275deec1228084
ENV ANT_HOME /usr/share/ant
ENV PATH="${PATH}:${ANT_HOME}/bin"
RUN echo "##########> Install Apache Ant Tools <##########" \
    # Download and verify Apache Ant archive
    && curl -fsSl -o /tmp/apache-ant.tar.gz https://archive.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz \
    && echo "${ANT_SHA} /tmp/apache-ant.tar.gz" | sha512sum -c - \
    # Extract to Ant home directory and clean archive
    && mkdir -p ${ANT_HOME} \
    && tar -xzf /tmp/apache-ant.tar.gz -C ${ANT_HOME} --strip-components=1 \
    && rm -f /tmp/apache-ant.tar.gz \
    # Fetch Ant library
    && ant -f ${ANT_HOME}/fetch.xml -Ddest=system
#-------------------------------------------------------------------------------------------------------------

#-------------------Clean up----------------------------------------------------------------------------------
RUN echo "##########> Clean up through apt <##########"
RUN apt-get autoremove -y \
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/*
ENV DEBIAN_FRONTEND=

#-------------------Allow for a consistant java home location for settings - image is changing over time------
RUN if [ ! -d "/docker-java-home" ]; then ln -s "${JAVA_HOME}" /docker-java-home; fi
