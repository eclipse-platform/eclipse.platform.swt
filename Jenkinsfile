pipeline {
	options {
		timeout(time: 90, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr:'5'))
	}
  agent {
    kubernetes {
      label 'swtbuild-pod'
      defaultContainer 'container'
      yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    resources:
      requests:
        memory: "512Mi"
        cpu: "100m"
      limits:
        memory: "512Mi"
        cpu: "500m"  
  - name: container
    image: akurtakov/swtbuild@sha256:2cd3dfdea1f250597c9e3797512953c03e2182344e86976c1b60117c5dd36a9e
    tty: true
    command: [ "uid_entrypoint", "cat" ]
    resources:
      requests:
        memory: "4Gi"
        cpu: "1"
      limits:
        memory: "4Gi"
        cpu: "1"
    volumeMounts:
    - name: "settings-xml"
      mountPath: "/home/jenkins/.m2/settings.xml"
      subPath: "settings.xml"
      readOnly: true
    - name: "toolchains-xml"
      mountPath: "/home/jenkins/.m2/toolchains.xml"
      subPath: "toolchains.xml"
      readOnly: true
    - name: "settings-security-xml"
      mountPath: "/home/jenkins/.m2/settings-security.xml"
      subPath: "settings-security.xml"
      readOnly: true
    - name: m2-repo
      mountPath: /home/jenkins/.m2/repository
    - name: "tools"
      mountPath: "/opt/tools"
  volumes:
  - name: settings-xml
    secret:
      secretName: m2-secret-dir
      items:
      - key: settings.xml
        path: settings.xml
  - name: toolchains-xml
    configMap:
      name: m2-dir
      items:
      - key: toolchains.xml
        path: toolchains.xml
  - name: settings-security-xml
    secret:
      secretName: m2-secret-dir
      items:
      - key: settings-security.xml
        path: settings-security.xml
  - name: m2-repo
    emptyDir: {}
  - name: tools
    persistentVolumeClaim:
      claimName: tools-claim-jiro-platform
"""
    }
  }
  environment {
        MAVEN_OPTS = "-Xmx4G"
  }
	stages {
		stage('Prepare-environment') {
			steps {
				container('container') {
					sh 'mutter --replace --sm-disable &'
					dir ('eclipse.platform.swt') {
						checkout scm
					}
					dir ('eclipse.platform.swt.binaries') {
					    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', timeout: 120]], submoduleCfg: [], userRemoteConfigs: [[url: 'https://git.eclipse.org/r/platform/eclipse.platform.swt.binaries.git']]])
					}
				}
			}
		}
		stage('Build') {
			steps {
				container('container') {
					wrap([$class: 'Xvnc', useXauthority: true]) {
					    withEnv(["JAVA_HOME=${ tool 'openjdk-jdk11-latest' }"]) {
					        dir ('eclipse.platform.swt.binaries') {
					    	    sh '/opt/tools/apache-maven/latest/bin/mvn --batch-mode -Pbuild-individual-bundles -DforceContextQualifier=zzz -Dnative=gtk.linux.x86_64 -DskipJni -DskipRust -Dcompare-version-with-baselines.skip=true -Dmaven.compiler.failOnWarning=true install '
					        }
					        dir ('eclipse.platform.swt') {
					    	    sh '/opt/tools/apache-maven/latest/bin/mvn --batch-mode -Pbuild-individual-bundles -DcheckAllWS=true -DforkCount=0 -Dcompare-version-with-baselines.skip=false -Dmaven.compiler.failOnWarning=true clean verify '
					        }
					    }
				    }
				}
			}
			post {
				always {
					junit '**/*.test*/target/surefire-reports/*.xml'
					archiveArtifacts artifacts: '**/*.log,**/*.html,**/target/*.jar,**/target/*.zip'
					publishIssues issues:[scanForIssues(tool: java()), scanForIssues(tool: mavenConsole())]
				}
				unstable {
					gerritReview labels: [Verified: -1], message: "Build UNSTABLE (test failures) $BUILD_URL"
				}
				failure {
					gerritReview labels: [Verified: -1], message: "Build FAILED $BUILD_URL"
				}
			}
		}
		stage('Check freeze period') {
			when {
				not {
					branch 'master'
				}
			}
			steps {
				container('jnlp') {
					sh "wget https://git.eclipse.org/c/platform/eclipse.platform.releng.aggregator.git/plain/scripts/verifyFreezePeriod.sh"
					sh "chmod +x verifyFreezePeriod.sh"
					withCredentials([string(credentialsId: 'google-api-key', variable: 'GOOGLE_API_KEY')]) {
						sh './verifyFreezePeriod.sh'
					}
				}
			}
			post {
				failure {
					gerritReview labels: [Verified: -1], message: "Build and test are OK, but Eclipse project is currently in a code freeze period.\nPlease wait for end of code freeze period before merging.\n $BUILD_URL"
				}
			}
		}
	}
	post {
		success {
			gerritReview labels: [Verified: 1], message: "Build Succcess $BUILD_URL"
		}
	}
}