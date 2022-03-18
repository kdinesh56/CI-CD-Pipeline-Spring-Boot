def configstring = """

<project>
<actions/>
<description/>
<keepDependencies>false</keepDependencies>
<properties>
<org.jenkinsci.plugins.gogs.GogsProjectProperty plugin="gogs-webhook@1.0.15">
<gogsSecret>{AQAAABAAAAAQDzrue1cMipWIXyRe1x/02zjRHKJfsrZAviE6gPCZJ00=}</gogsSecret>
<gogsUsePayload>false</gogsUsePayload>
<gogsBranchFilter/>
</org.jenkinsci.plugins.gogs.GogsProjectProperty>
</properties>
<scm class="hudson.plugins.git.GitSCM" plugin="git@4.10.3">
<configVersion>2</configVersion>
<userRemoteConfigs>
<hudson.plugins.git.UserRemoteConfig>
<url>http://192.168.1.9:10880/dinesh/springboot-jsp.git</url>
<credentialsId>gogscred</credentialsId>
</hudson.plugins.git.UserRemoteConfig>
</userRemoteConfigs>
<branches>
<hudson.plugins.git.BranchSpec>
<name>*/master</name>
</hudson.plugins.git.BranchSpec>
</branches>
<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
<submoduleCfg class="empty-list"/>
<extensions/>
</scm>
<canRoam>true</canRoam>
<disabled>false</disabled>
<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
<triggers/>
<concurrentBuild>false</concurrentBuild>
<builders>
<hudson.tasks.Maven>
<targets>#SONAR_MAVEN_GOAL clean install</targets>
<mavenName>maven</mavenName>
<usePrivateRepository>false</usePrivateRepository>
<settings class="jenkins.mvn.DefaultSettingsProvider"/>
<globalSettings class="jenkins.mvn.DefaultGlobalSettingsProvider"/>
<injectBuildVariables>false</injectBuildVariables>
</hudson.tasks.Maven>
</builders>
<publishers>
<hudson.plugins.deploy.DeployPublisher plugin="deploy@1.16">
<adapters>
<hudson.plugins.deploy.tomcat.Tomcat9xAdapter>
<credentialsId>tomcatcred</credentialsId>
<url>http://192.168.1.9:8081/</url>
<path/>
</hudson.plugins.deploy.tomcat.Tomcat9xAdapter>
</adapters>
<contextPath>$BUILD_NUMBER</contextPath>
<war>*/*.war</war>
<onFailure>false</onFailure>
</hudson.plugins.deploy.DeployPublisher>
</publishers>
<buildWrappers>
<hudson.plugins.sonar.SonarBuildWrapper plugin="sonar@2.14">
<credentialsId>sonarsecret</credentialsId>
<envOnly>false</envOnly>
</hudson.plugins.sonar.SonarBuildWrapper>
</buildWrappers>
</project>

"""


def jobconfignode = new XmlParser().parseText(configstring)

job('replace-me-jobdsl') {
    configure { node ->
        // node represents <project>
        jobconfignode.each { child ->

          def name = child.name()

          def existingChild = node.get(name)
          if(existingChild){
            node.remove(existingChild)
          }

          node << child
        }
    }
}