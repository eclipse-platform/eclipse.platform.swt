FROM eclipsecbi/fedora-gtk3-mutter:39-gtk3.24@sha256:48d01b985e3fe95563f28c56d0446dcb3ebf36a3d69a62f5266d3e5147d99852

# Back to root for install
USER 0

ARG REMOTING_VERSION=3.27

# https://github.com/jenkinsci/docker-jnlp-slave/
ARG JNLP_AGENT_SCRIPT_VERSION=3.27-1

RUN curl --create-dirs -sSLo /usr/share/jenkins/slave.jar https://repo.jenkins-ci.org/public/org/jenkins-ci/main/remoting/${REMOTING_VERSION}/remoting-${REMOTING_VERSION}.jar \
  && chmod 755 /usr/share/jenkins \
  && chmod 644 /usr/share/jenkins/slave.jar

ADD https://raw.githubusercontent.com/jenkinsci/docker-jnlp-slave/${JNLP_AGENT_SCRIPT_VERSION}/jenkins-slave /usr/local/bin/jenkins-slave
RUN chmod 555 /usr/local/bin/jenkins-slave && \
  chgrp 0 /usr/local/bin/jenkins-slave && \
  mkdir -p /home/jenkins/.jenkins && \
  mkdir -p /home/jenkins/agent

ENTRYPOINT [ "uid_entrypoint", "jenkins-slave" ]

RUN dnf -y update && dnf -y install \
    java-openjdk maven webkit2gtk3-devel \
    make gcc mesa-libGLU-devel mesa-libGL-devel \
    libXt-devel procps-ng \
    && dnf clean all

#Back to named user
USER 1000

ENV LANG C.UTF-8
ENV JENKINS_AGENT_WORKDIR=${HOME}/agent
ENV JAVA_OPTS=""
ENV JNLP_PROTOCOL_OPTS="-XshowSettings:vm -Xmx256m -Djdk.nativeCrypto=false -Dsun.zip.disableMemoryMapping=true -Dorg.jenkinsci.remoting.engine.JnlpProtocol3.disabled=true"
