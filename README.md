# liberty-target-practice
Open Liberty web app for the sentry challenge
1. Download Open Liberty version 18.0.0.2 from https://openliberty.io/downloads/ . Use the latest Development builds as it's not officially released. Other version of Open Liberty like 18.0.0.1 or older will not work as we used latest JAVA EE8 feature in our code. Extract the zip package to your local drive.
2. Clone the repo: 'git clone git@github.com:fwji/liberty-target-practice.git'
3. Open pom.xml, and modify line 126 to your own Open Liberty 18.0.0.2 installation directory
4. Run 'mvn install'
5. Run 'mvn liberty:start-server'
6. visit http://localhost:9080/SentryTargetChallenge
