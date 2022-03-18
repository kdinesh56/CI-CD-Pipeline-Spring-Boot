# CI-CD-Pipeline-Spring-Boot
CICD using Gogs Jenkins Sonarqube and Tomcat

In this project CI-CD Pipeline is implemented using the tools Gogs (For Git), Jenkins, SonarQube and Tomcat.

## Pre-Requistes:

A machine installed with Ubunutu 20.04 LTS.

***
## Install SSH
***
`sudo apt update`

`sudo apt install openssh-server`

`sudo systemctl status ssh`



***
## Install Docker
***
 `sudo apt-get update`
 
 `sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release`

`curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg`

 `echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null`
  
 `sudo apt-get update`

 `sudo apt-get install docker-ce docker-ce-cli containerd.io`




***
## Install GOGS
***
Gogs is a SCM tool like Git but has the GUI like GitHub.

### Update repository
`sudo apt update`

### Pull the image from DockerHub
`sudo docker pull gogs/gogs`

### Create a Volume for mounting and that will ensure the persistence of data
`sudo docker volume create gogsvolume`

### Create a common bridge network and attach to the Gogs, Jenkins and Tomcat
`sudo docker network create -d bridge my-bridge-network`

### Execute the below command to up a gogs container with the required port mappings and volumes mounted
`sudo docker run -d --name=gogs -p 10022:22 -p 10880:3000 -v gogsvolume:/data --network=my-bridge-network gogs/gogs`

### Now access the Gogs using host address at the port 10880
http://localhost:10880

### Now the initial configuration needs to be done.

select SQL DB as SQLLITE and then install
Register as new user
Login to the GOGS




***
## Install Jenkins
***

### Update the repository
`sudo apt update`

### Pull the official latest image of Jenkins from DockerHub
`sudo docker pull jenkins/jenkins`

### Create a seperate volume for Jenkins to enable data persistency
`sudo docker volume create jenkinsvolume`

### Execute the below command to run the Jenkins container with required port mappings and volumes mounted
`sudo docker run -d --name=jenkins -p 8080:8080 -p 50000:50000 -v jenkinsvolume:/var/jenkins_home  --network=my-bridge-network jenkins/jenkins`

### Now access Jenkins from the port 8080
http://localhost:8080




***
## Install Tomcat
***

### Update repository
`sudo apt update`

### Pull the official image of Tomcat from DockerHub
`sudo sudo docker pull tomcat:9.0`

### Execute the below command to run the Tomcat container with required port mappings.  
`sudo docker run -d --name=tomcat -p 8081:8080 --network=my-bridge-network tomcat:9.0`


***
## Install SonarQube
***

### Setup the Limits 

Configure the resource usage limts, Open 99-limits.conf file and paste the configurations listed below

`sudo nano /etc/security/limits.d/99-limits.conf`

```
#<domain>      <type>  <item>         <value>
#

*               hard    nofile            131072
*               hard    nproc             8192
```

The equivalent command line for the above mentioned configuration are listed below. This is just for understanding.
```
ulimit -n 131072 # Maximum number of Open files
ulimit -u 8192 # Maximum User processess limit
```

Now restart the machine to make the necessary configuration to get effect.

Next setup the system limts for the map count and file max.

`sudo nano /etc/sysctl.d/99-sysctl.conf`

```
vm.max_map_count=524288
fs.file-max=131072
```

### Restart the system control to load the configuration changes
`sysctl --system`


### Create a new Dockerfile and paste the below content

```
version: "3"

services:
  sonarqube:
    image: sonarqube:community	
    depends_on:
      - db
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_extensions:/opt/sonarqube/extensions
      - sonarqube_logs:/opt/sonarqube/logs
    ports:
      - "9000:9000"
  db:
    image: postgres:12
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    volumes:
      - postgresql:/var/lib/postgresql
      - postgresql_data:/var/lib/postgresql/data

volumes:
  sonarqube_data:
  sonarqube_extensions:
  sonarqube_logs:
  postgresql:
  postgresql_data:
  ```


  Then Up the Sonarqube service using below command.

  `docker-compose up sonarqube .`
  




