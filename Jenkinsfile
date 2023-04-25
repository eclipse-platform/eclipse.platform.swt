def nativeBuildAgent(String platform, Closure body) {
	def final nativeBuildStageName = 'Build SWT-native binaries'
	if(platform == 'gtk.linux.x86_64') {
		return podTemplate(yaml: '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: "swtbuild"
    image: "eclipse/platformreleng-centos-swt-build:8"
    imagePullPolicy: "Always"
    resources:
      limits:
        memory: "4096Mi"
        cpu: "2000m"
      requests:
        memory: "512Mi"
        cpu: "1000m"
    command:
    - cat
    tty: true
    volumeMounts:
    - name: tools
      mountPath: /opt/tools
  volumes:
  - name: tools
    persistentVolumeClaim:
      claimName: tools-claim-jiro-releng
''') { node(POD_LABEL) { stage(nativeBuildStageName) { container('swtbuild') { body() } } } }
	} else {
		return node('swt.natives-' + platform) { stage(nativeBuildStageName) { body() } }
	}
}

pipeline {
	options {
		skipDefaultCheckout() // Specialiced checkout is performed below
		timestamps()
		timeout(time: 180, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr:'5'))
		disableConcurrentBuilds(abortPrevious: true)
	}
	agent {
		label 'centos-latest'
	}
	environment {
		MAVEN_OPTS = "-Xmx4G"
		PR_VALIDATION_BUILD = "true"
	}
	parameters {
		booleanParam(name: 'forceNativeBuilds', defaultValue: false, description: 'Forces to run the native builds of swt\'s binaries. Useful in debugging.')
		booleanParam(name: 'skipCommit', defaultValue: false, description: 'Stops committing to swt and swt binaries repo at the end. Useful in debugging.')
	}
	stages {
		stage('Checkout swt git repos') {
			steps {
				dir ('eclipse.platform.swt') {
					checkout scm
					script {
						def authorMail = sh(script: 'git log -1 --pretty=format:"%ce" HEAD', returnStdout: true)
						echo 'HEAD commit author: ' + authorMail
						if ('eclipse-releng-bot@eclipse.org'.equals(authorMail)) {
							// Prevent endless build-loops due to self triggering because of a previous automated build of SWT-natives and the associated updates.
							currentBuild.result = 'ABORTED'
							error('Abort build only triggered by automated SWT-natives update.')
						}
					}
					sh '''
						git fetch --all --tags --quiet
						git remote set-url --push origin git@github.com:eclipse-platform/eclipse.platform.swt.git
					'''
				}
				dir ('eclipse.platform.swt.binaries') {
					checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/master']],
						extensions: [[$class: 'CloneOption', timeout: 120, noTags: false ]],
						userRemoteConfigs: [[url: 'https://github.com/eclipse-platform/eclipse.platform.swt.binaries.git']]
					])
					sh 'git remote set-url --push origin git@github.com:eclipse-platform/eclipse.platform.swt.binaries.git'
				}
			}
		}
		stage('Check if SWT-binaries build is needed') {
			steps {
				withAnt(installation: 'apache-ant-latest', jdk: 'openjdk-jdk11-latest') { // nashorn javascript-engine required in ant-scripts
					sh'''
						java -version
						git config --global user.email 'eclipse-releng-bot@eclipse.org'
						git config --global user.name 'Eclipse Releng Bot'
						
						ant -f eclipse.platform.swt/bundles/org.eclipse.swt/buildSWT.xml check_compilation -DTAG=HEAD
						ant -f eclipse.platform.swt/bundles/org.eclipse.swt/buildSWT.xml new_build_with_create_file -DTAG=HEAD
					'''
				}
			}
		}
		stage('Build SWT-binaries, if needed') {
			when {
				anyOf {
					expression { return params.forceNativeBuilds }
					expression { return fileExists ('tmp/build_changed.txt') && fileExists ('tmp/natives_changed.txt') }
				}
			}
			matrix {
				axes {
					axis {
						name 'PLATFORM'
						values 'cocoa.macosx.aarch64' , 'cocoa.macosx.x86_64', 'gtk.linux.aarch64', 'gtk.linux.ppc64le', 'gtk.linux.x86_64', 'win32.win32.x86_64'
					}
				}
				stages {
					stage("Collect SWT-native's sources") {
						steps {
							dir('eclipse.platform.swt.binaries/bundles'){
								withAnt(installation: 'apache-ant-latest', jdk: 'openjdk-jdk11-latest') { // nashorn javascript-engine required in ant-scripts
									sh '''
										pfSpec=(${PLATFORM//"."/ })
										ant -f binaries-parent/build.xml copy_library_src_and_create_zip -Dws=${pfSpec[0]} -Dos=${pfSpec[1]} -Darch=${pfSpec[2]}
									'''
								}
								dir("org.eclipse.swt.${PLATFORM}/tmpdir") {
									stash name:"swt.binaries.sources.${PLATFORM}",	includes: "org.eclipse.swt.${PLATFORM}.master.zip"
								}
							}
						}
					}
					stage('Build SWT-natives') {
						options {
							timeout(time: 120, unit: 'MINUTES') // Some build agents are rare and it might take awhile until they are available.
						}
						steps {
							script {
								nativeBuildAgent("${PLATFORM}") {
									cleanWs() // Workspace is not cleaned up by default, so we do it explicitly
									unstash "swt.binaries.sources.${PLATFORM}"
									
									def (ws, os, arch) = env.PLATFORM.split('\\.')
									echo "OS: ${os}"
									echo "ARCH: ${arch}"
									def javaHome = env.JAVA_HOME
									// Some of the native-build agents don't have their JAVA_HOME properly set. Actually that should be done in the agents
									//TODO: ask the infra-team to fix the setup. This is a infra-setup detail and should not be handled in the pipeline.
									if(os =='linux' && arch == 'aarch64'){
										def armJDK = '/usr/lib/jvm/java-11-openjdk-arm64'
										javaHome = fileExists(armJDK) ? armJDK : '/usr/lib/jvm/java-11-openjdk'
									} else if (os =='linux' && arch == 'ppc64le') {
										javaHome = '/usr/lib/jvm/java-11-openjdk-11.0.15.0.10-3.el8.ppc64le'
									} else if (os =='linux' && arch == 'x86_64') {
										javaHome = tool(type:'jdk', name:'temurin-jdk11-latest')
									} else if(os == 'macosx') {
										javaHome = tool(type:'jdk', name:'temurin-jdk11-latest')
									}
									echo 'JAVA_HOME: ' + javaHome
									// TODO: don't zip the sources and just (un)stash them unzipped! That safes the unzipping and removal of the the zip
									withEnv(['MODEL=' + arch, "OUTPUT_DIR=${WORKSPACE}/libs", 'JAVA_HOME=' + javaHome]) {
										if(isUnix()){
											sh '''
												unzip -aa org.eclipse.swt.${PLATFORM}.master.zip
												rm org.eclipse.swt.${PLATFORM}.master.zip
												mkdir libs
												
												#TODO: unify build script arguments?!
												if [[ ${PLATFORM} == cocoa.macosx.* ]]; then
													sw_vers -productVersion
													sh build.sh install
												elif [[ ${PLATFORM} == gtk.linux.* ]]; then
													sh build.sh clean install
												fi
												ls -1R libs
											'''
										} else {
											withEnv(['PATH=C:\\tools\\cygwin\\bin;' + env.PATH]) {
												bat '''
													unzip org.eclipse.swt.%PLATFORM%.master.zip
													rm org.eclipse.swt.%PLATFORM%.master.zip
													mkdir libs
													
													@rem rustup show
													cmd /c build.bat x86_64 all install
													ls -1R libs
												'''
											}
										} 
									}
									dir('libs') {
										stash "swt.binaries.${PLATFORM}"
									}
								}
							}
						}
					}
					stage('Collect and sign binaries') {
						steps {
							dir("libs/${PLATFORM}") {
								unstash "swt.binaries.${PLATFORM}"
								
								sh '''#!/bin/bash -x
									fn-sign-files()
									{
										extension=$1
										signingServiceAddress=$2
										if [[ "${BRANCH_NAME}" == master ]] || [[ "${BRANCH_NAME}" =~ R[0-9]+_[0-9]+(_[0-9]+)?_maintenance ]]; then
											for filename in $(ls *.${extension})
											do
												mv ${filename} unsigned-${filename}
												curl -f -o ${filename} -F file=@unsigned-${filename} $signingServiceAddress
												if [[ $? != 0 ]]; then
													echo "Signing of ${filename} failed"
													exit 1
												else
													rm unsigned-${filename}
												fi
											done
										fi
									}
									
									binaryFragmentsRoot=${WORKSPACE}/eclipse.platform.swt.binaries/bundles
									
									if [[ ${PLATFORM} == cocoa.macosx.* ]]; then
										#TODO: Instead use (with adjusted URL): https://github.com/eclipse-cbi/org.eclipse.cbi/tree/main/maven-plugins/eclipse-winsigner-plugin
										# The mac-signer mojo only works for Mac-applications and not for jnilib files, but the winsigner-plugin with adjusted URL seems to do what we want.
										echo "Sign ${PLATFORM} libraries"
										fn-sign-files jnilib https://cbi.eclipse.org/macos/codesign/sign
		
										cp *.jnilib ${binaryFragmentsRoot}/org.eclipse.swt.${PLATFORM}/
									
									elif [[ ${PLATFORM} == gtk.linux.* ]]; then
										#TODO: can the webkit handling be removed?!
										#echo "Removing existing webkitextensions"
										#rm -r ${binaryFragmentsRoot}/org.eclipse.swt.${PLATFORM}/webkit*/
										#cp -r webkitextensions* ${binaryFragmentsRoot}/org.eclipse.swt.${PLATFORM}/
										
										cp *.so ${binaryFragmentsRoot}/org.eclipse.swt.${PLATFORM}/
									
									elif [[ ${PLATFORM} == win32.win32.* ]]; then
										#TODO: Instead use: https://github.com/eclipse-cbi/org.eclipse.cbi/tree/main/maven-plugins/eclipse-winsigner-plugin
										echo "Sign ${PLATFORM} libraries"
										fn-sign-files dll https://cbi.eclipse.org/authenticode/sign
										
										cp *.dll ${binaryFragmentsRoot}/org.eclipse.swt.${PLATFORM}/
									fi
								'''
							}
						}
					}
				}
			}
		}
		stage('Commit SWT-native binaries, if build') {
			when {
				expression { return params.forceNativeBuilds || fileExists ('tmp/build_changed.txt') }
			}
			steps {
				withAnt(installation: 'apache-ant-latest', jdk: 'openjdk-jdk11-latest') { // nashorn javascript-engine required in ant-scripts
					//The maven build reads the git-history so we should have to commit the native-binaries before building
					sh '''
						pushd eclipse.platform.swt.binaries
						git add --all *
						echo "git status after add"
						git status
						popd

						ant -f eclipse.platform.swt/bundles/org.eclipse.swt/buildInternal.xml write_qualifier -Dlib.dir=${WORKSPACE} -Dbuild_changed=true
						ant -f eclipse.platform.swt/bundles/org.eclipse.swt/buildSWT.xml commit_poms_and_binaries
						ant -f eclipse.platform.swt/bundles/org.eclipse.swt/buildSWT.xml tag_projects
	
						pushd eclipse.platform.swt
						git status
						git log -p -2
						popd
						
						pushd eclipse.platform.swt.binaries
						git status
						git log -p -1
						popd
					'''
				}
			}	
		}
		stage('Build') {
			tools {
				// Define tools only in this stage to not interfere with default environemts of SWT-natives build-agents
				jdk 'openjdk-jdk17-latest'
				maven 'apache-maven-latest'
			}
			steps {
				xvnc(useXauthority: true) {
					dir ('eclipse.platform.swt.binaries') {
						sh '''
							mvn install \
								--batch-mode -Pbuild-individual-bundles -DforceContextQualifier=zzz \
								-Dcompare-version-with-baselines.skip=true -Dmaven.compiler.failOnWarning=true
						'''
					}
					dir ('eclipse.platform.swt') {
						sh '''
							mvn clean verify \
								--batch-mode -DcheckAllWS=true -DforkCount=0 \
								-Dcompare-version-with-baselines.skip=false -Dmaven.compiler.failOnWarning=true \
								-Dmaven.test.failure.ignore=true -Dmaven.test.error.ignore=true
						'''
					}
				}
			}
			post {
				always {
					junit 'eclipse.platform.swt/tests/*.test*/target/surefire-reports/*.xml'
					archiveArtifacts artifacts: '**/*.log,**/*.html,**/target/*.jar,**/target/*.zip'
					discoverGitReferenceBuild referenceJob: 'eclipse.platform.swt/master'
					recordIssues publishAllIssues: true, tools: [java(), mavenConsole(), javaDoc()]
				}
			}
		}
		stage('Push SWT-native binaries, if build') {
			when {
				expression { return params.forceNativeBuilds || fileExists ('tmp/build_changed.txt') }
			}
			steps {
				sshagent(['github-bot-ssh']) {
					script {
						def newSWTNativesTag = null;
						dir ('eclipse.platform.swt.binaries') {
							newSWTNativesTag = sh(script: 'git describe --abbrev=0 --tags --match v[0-9][0-9][0-9][0-9]*', returnStdout: true).strip()
						}
						echo "newSWTNativesTag: ${newSWTNativesTag}"
						sh """
							# Check for the master-branch as late as possible to have as much of the same behaviour as possible
							if [[ '${BRANCH_NAME}' == master ]] || [[ '${BRANCH_NAME}' =~ R[0-9]+_[0-9]+(_[0-9]+)?_maintenance ]]; then
								if [[ ${params.skipCommit} != true ]]; then
									
									#Don't rebase and just fail in case another commit has been pushed to the master/maintanance branch in the meantime
									
									pushd eclipse.platform.swt
									git push origin HEAD:refs/heads/${BRANCH_NAME}
									git push origin refs/tags/${newSWTNativesTag}
									popd
									
									pushd eclipse.platform.swt.binaries
									git push origin HEAD:refs/heads/${BRANCH_NAME}
									git push origin refs/tags/${newSWTNativesTag}
									popd
									
									exit 0
								else
									echo Committing is skipped
								fi
							else
								echo Skip pushing changes of native-binaries for branch '${BRANCH_NAME}'
							fi
							# The commits are not pushed. At least list them, so one can check if the result is as expected.
							pushd eclipse.platform.swt
							git log -n 2
							popd
							pushd eclipse.platform.swt.binaries
							git log -n 2
							popd
						"""
					}
				}
			}
		}
	}
}
