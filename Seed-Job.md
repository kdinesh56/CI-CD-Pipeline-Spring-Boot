# Seed Job to configure a CICD Job in Jenkins

Seed Job is something which uses the Job DSL plugin and generates new Jenkins job based on the Groovy script.

This case a config.xml of a Jenkins job is passed on to create a new jenkins job which can perform the CICD.

## Download and install the below plugins

Jenkins Dashboard --> Manage Jenkins --> Manage Plugins

1. Job DSL
2. Sonar Scanner
3. Deploy to container
4. Gogs plugin
5. Maven

After the install restart Jenkins

## Configure the tools in jenkins Configure System.

Jenkins Dashboard --> Manage Jenkins --> Configure System

1. Maven
2. Sonarqube Server


## Create a new job of type free stype in Jenkins

Scroll down to Build Section

Select Use the Provided DSL Script and paste the content mentioned in myjenkinsfile.groovy

Then save the changes and Build the job which will produce a new Job which is the new CICD Pipeline job. That new job can be build and verify the output in Tomcat or the Application URL.

