# Download latest Open Liberty from DockerHub
FROM open-liberty:latest

# Create a Liberty server instance called "SentryTargetChallengeServer"
RUN /opt/ol/wlp/bin/server create SentryTargetChallengeServer

# Copy over the customized server.xml to the Liberty server
COPY src/main/liberty/config/server.xml /opt/ol/wlp/usr/servers/SentryTargetChallengeServer/

# Copy over the jvm.options to the Liberty server
COPY src/main/liberty/config/resources/jvm.options /opt/ol/wlp/usr/servers/SentryTargetChallengeServer/

#Copy over the bootstrap.properties to the Liberty Server
COPY target/liberty/wlp/usr/servers/SentryTargetChallengeServer/bootstrap.properties /opt/ol/wlp/usr/servers/SentryTargetChallengeServer/

# Copy the SentryTargetChallenge-1.0-SNAPSHOT.war  WAR application to the Liberty server
COPY target/SentryTargetChallenge*.war /opt/ol/wlp/usr/servers/SentryTargetChallengeServer/apps/SentryTargetChallenge.war

# Set Path Shortcuts, redirect all Liberty logs to "/logs"
ENV LOG_DIR=/logs

# Start the Liberty server
CMD ["/opt/ol/wlp/bin/server", "run", "SentryTargetChallengeServer"]
