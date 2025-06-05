/*******************************************************************************
 * Copyright (c) 2021, 2024 Red Hat Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - initial API and implementation
 *     Hannes Wellmann - Build SWT-natives as part of master- and verification-builds
 *     Hannes Wellmann - Move SWT native binaries in this repository using Git-LFS
 *     Hannes Wellmann - Streamline entire SWT build and replace ANT-scripts by Maven, Jenkins-Pipeline and single-source Java scripts
  *******************************************************************************/

def runOnNativeBuildAgent(String platform, Closure body) {
	def final nativeBuildStageName = 'Build SWT-native binaries'
	def dockerImage = null
	switch (platform) {
		case 'gtk.linux.x86_64':
			dockerImage = 'eclipse/platformreleng-debian-swtgtk3nativebuild:10'
			break
		case 'gtk4.linux.x86_64':
			dockerImage = 'eclipse/platformreleng-debian-swtnativebuild:12'
			break
	}
	if (dockerImage != null) {
		podTemplate(inheritFrom: 'basic' /* inherit general configuration */, containers: [
			containerTemplate(name: 'swtbuild', image: dockerImage,
				resourceRequestCpu:'1000m', resourceRequestMemory:'512Mi',
				resourceLimitCpu:'2000m', resourceLimitMemory:'4096Mi',
				alwaysPullImage: true, command: 'cat', ttyEnabled: true)
		]) {
			node(POD_LABEL) { stage(nativeBuildStageName) { container('swtbuild') { body() } } }
		}
	} else {
		// See the Definition of the RelEng Jenkins instance in
		// https://github.com/eclipse-cbi/jiro/tree/master/instances/eclipse.platform.releng
		node('native.builder-' + platform) { stage(nativeBuildStageName) { body() } }
	}
}

/** Returns the download URL of the JDK against whose C headers (in the 'include/' folder) and native libraries the SWT natives are compiled.*/
def getNativeJdkUrl(String os, String arch) { // To update the used JDK version update the URL template below
	if ('win32'.equals(os) && 'aarch64'.equals(arch)) {
		// Temporary workaround until there are official Temurin GA releases for Windows on ARM that can be consumed through JustJ
		dir("${WORKSPACE}/repackage-win32.aarch64-jdk") {
			sh """
				curl -L 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk17u-2024-02-07-14-14-beta/OpenJDK17U-jdk_aarch64_windows_hotspot_2024-02-07-14-14.zip' > jdk.zip
				unzip -q jdk.zip jdk-17.0.11+1/include/** jdk-17.0.11+1/lib/**
				cd jdk-17.0.11+1
				tar -czf ../jdk.tar.gz include/ lib/
			"""
		}
		return "file://${WORKSPACE}/repackage-win32.aarch64-jdk/jdk.tar.gz"
	} else if ('linux'.equals(os) && 'riscv64'.equals(arch)) {
		// Downloading jdk and renew it for riscv64 architecture on Linux
		dir("${WORKSPACE}/repackage-linux.riscv64-jdk") {
				sh """
				curl -L 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.12%2B7/OpenJDK17U-jdk_riscv64_linux_hotspot_17.0.12_7.tar.gz' > jdk.tar.gz
				tar -xzf jdk.tar.gz jdk-17.0.12+7/include/ jdk-17.0.12+7/lib/
				cd jdk-17.0.12+7
				tar -czf ../jdk.tar.gz include/ lib/
				"""
		}
		return "file://${WORKSPACE}/repackage-linux.riscv64-jdk/jdk.tar.gz"
	}
	return "https://download.eclipse.org/justj/jres/17/downloads/20230428_1804/org.eclipse.justj.openjdk.hotspot.jre.minimal.stripped-17.0.7-${os}-${arch}.tar.gz"
}

def getLatestGitTag() {
	return sh(script: 'git describe --abbrev=0 --tags --match v[0-9][0-9][0-9][0-9]*', returnStdout: true).trim()
}

def getSWTVersions() { // must be called from the repository root
	def props = readProperties(file: 'bundles/org.eclipse.swt/Eclipse SWT/common/library/make_common.mak')
	props['new_rev'] = props['rev'].toInteger() + 1
	props['swt_version'] = props['maj_ver'] + props['min_ver'] + 'r' + props['rev']
	props['new_version'] = props['maj_ver'] + props['min_ver'] + 'r' + props['new_rev']
	return props
}

def Set NATIVES_CHANGED = []

pipeline {
	options {
		skipDefaultCheckout() // Specialized checkout is performed below
		timestamps()
		timeout(time: 180, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr: 'master'.equals(env.BRANCH_NAME) ? '20' : '5', artifactNumToKeepStr: 'master'.equals(env.BRANCH_NAME) ? '3' : '1' ))
		disableConcurrentBuilds(abortPrevious: true)
	}
	agent {
		label 'ubuntu-latest'
	}
	tools {
		jdk 'temurin-jdk21-latest'
		maven 'apache-maven-latest'
	}
	environment {
		PR_VALIDATION_BUILD = "true"
	}
	parameters {
		booleanParam(name: 'forceNativeBuilds-cocoa', defaultValue: false, description: 'Enforce a re-build of SWT\'s native binaries for Mac OS X. Will push the built binaries to the master branch, unless \'skipCommit\' is set.')
		booleanParam(name: 'forceNativeBuilds-gtk', defaultValue: false, description: 'Enforce a re-build of SWT\'s native binaries for Linux. Will push the built binaries to the master branch, unless \'skipCommit\' is set.')
		booleanParam(name: 'forceNativeBuilds-win32', defaultValue: false, description: 'Enforce a re-build of SWT\'s native binaries for Windows. Will push the built binaries to the master branch, unless \'skipCommit\' is set.')
		booleanParam(name: 'skipCommit', defaultValue: false, description: 'Stops committing to swt and swt binaries repo at the end. Useful in debugging.')
	}
	stages {
		stage('Checkout SCM') {
			steps {
				dir('eclipse.platform.swt') {
					checkout scm
					script {
						def authorMail = sh(script: 'git log -1 --pretty=format:"%ce" HEAD', returnStdout: true)
						echo 'HEAD commit author: ' + authorMail
						def buildBotMail = 'platform-bot@eclipse.org'
						if (buildBotMail.equals(authorMail) && !params.forceNativeBuilds) {
							// Prevent endless build-loops due to self triggering because of a previous automated build of natives and the associated updates.
							currentBuild.result = 'ABORTED'
							error('Abort build only triggered by automated SWT-natives update.')
						}
						sh """
						java -version
						git version
						git lfs version
						git config --global user.email '${buildBotMail}'
						git config --global user.name 'Eclipse Platform Bot'
						git config --unset core.hooksPath # Jenkins disables hooks by default as security feature, but we need the hooks for LFS
						git lfs update # Install Git LFS hooks in repository, which has been skipped due to the initially nulled hookspath
						git lfs pull
						git fetch --all --tags --quiet
						git remote set-url --push origin git@github.com:eclipse-platform/eclipse.platform.swt.git
						"""
					}
				}
			}
		}
		stage('Check if SWT-binaries build is needed') {
			steps {
				dir('eclipse.platform.swt') {
					script {
						def allWS = ['cocoa', 'gtk', 'win32']
						def libraryFilePattern = [ 'cocoa' : 'libswt-*.jnilib', 'gtk' : 'libswt-*.so', 'win32' : 'swt-*.dll' ]
						def swtTag = getLatestGitTag()
						echo "Current tag=${swtTag}."
						dir('bundles/org.eclipse.swt') {
							// Verify preprocessing is completed
							sh '''
								if grep -R --include='*.java' --line-number --fixed-strings -e 'int /*long*/' -e 'float /*double*/' -e 'int[] /*long[]*/' -e 'float[] /*double[]*/' .; then
									echo There are files with the wrong long /*int*/ preprocessing.
									exit 1
								fi
							'''
							def sourceFoldersProps = readProperties(file: 'nativeSourceFolders.properties')
							def sources = sourceFoldersProps.collectEntries{ k, src -> [ k, src.split(',').collect{ f -> '\'' + f + '\''}.join(' ') ] }
							for (ws in allWS) {
								def diff = sh(script: "git diff HEAD ${swtTag} ${sources.src_common} ${sources['src_' + ws]}", returnStdout: true)
								if (!diff.trim().isEmpty()) {
									NATIVES_CHANGED += ws
								}
							}
							echo "Natives changed since ${swtTag}: ${NATIVES_CHANGED.isEmpty() ? 'None': NATIVES_CHANGED}"
						}
						NATIVES_CHANGED += allWS.findAll{ ws -> params[ 'forceNativeBuilds-' + ws] }
						if (!NATIVES_CHANGED.isEmpty()) {
							def versions = getSWTVersions()
								sh """
									echo "Incrementing version from ${versions.swt_version} to ${versions.new_version}"
									sed -i -e "s/REVISION = ${versions.rev}/REVISION = ${versions.new_rev}/g" \
										'bundles/org.eclipse.swt/Eclipse SWT PI/common/org/eclipse/swt/internal/Library.java'
									sed -i -e "s/rev=${versions.rev}/rev=${versions.new_rev}/g" \
										'bundles/org.eclipse.swt/Eclipse SWT/common/library/make_common.mak'
								"""
							for (ws in allWS) {
								if (NATIVES_CHANGED.contains(ws)) {
								sh """
									# Delete native binaries to be replaced by subsequent binaries build
									rm binaries/org.eclipse.swt.${ws}.*/${libraryFilePattern[ws]}
								"""
								} else {
									// Just rename existing native library files to new revision instead of rebuilding them
									sh """
										for f in binaries/org.eclipse.swt.${ws}.*/${libraryFilePattern[ws]}; do
											mv "\$f" "\$(echo \$f | sed 's/-${ws}-${versions.swt_version}/-${ws}-${versions.new_version}/g')"
										done
									"""
								}
							}
							// Collect SWT-native's sources
							dir('bundles/org.eclipse.swt') {
								for (ws in NATIVES_CHANGED) {
									sh "java -Dws=${ws} build-scripts/CollectSources.java -nativeSources 'target/natives-build-temp/${ws}'"
									dir("target/natives-build-temp/${ws}") {
										stash(name:"swt.binaries.sources.${ws}")
									}
								}
							}
						}
					}
				}
			}
		}
		stage('Build SWT-binaries, if needed') {
			when {
				expression { NATIVES_CHANGED }
			}
			matrix {
				axes {
					axis {
						name 'PLATFORM'
						values \
						'cocoa.macosx.aarch64',\
						'cocoa.macosx.x86_64',\
						'gtk.linux.aarch64',\
						'gtk.linux.ppc64le',\
						'gtk.linux.riscv64',\
						'gtk.linux.x86_64',\
						'gtk4.linux.x86_64',\
						'win32.win32.aarch64',\
						'win32.win32.x86_64'
					}
				}
				when {
					expression {
						def (ws, os, arch) = env.PLATFORM.split('\\.')
						return NATIVES_CHANGED.any{ w -> ws.startsWith(w)} // handle also dedicated gtk4 build
					}
				}
				stages {
					stage('Initiate native build') {
						options {
							timeout(time: 120, unit: 'MINUTES') // Some build agents are rare and it might take awhile until they are available.
						}
						steps {
							script {
								def (ws, os, arch) = env.PLATFORM.split('\\.')
								dir("jdk-download-${ws}.${arch}") {
									// Fetch the JDK, which provides the C header files and shared native libraries against which the native code is built.
									sh "curl ${getNativeJdkUrl(os, arch)} | tar -xzf - include/ lib/"
									stash name:"jdk.resources.${ws}.${arch}", includes: "include/,lib/"
									deleteDir()
								}
								runOnNativeBuildAgent("${PLATFORM}") {
									cleanWs() // Workspace is not cleaned up by default, so we do it explicitly
									echo "OS: ${os}, ARCH: ${arch}"
									unstash "swt.binaries.sources.${ws == 'gtk4' ? 'gtk' : ws }"
									dir('jdk.resources') {
										unstash "jdk.resources.${ws}.${arch}"
									}
									withEnv(['MODEL=' + arch, "OUTPUT_DIR=${WORKSPACE}/libs", "SWT_JAVA_HOME=${WORKSPACE}/jdk.resources"]) {
										if (isUnix()) {
											sh '''#!/bin/bash -x
												mkdir libs
												if [[ ${PLATFORM} == gtk.linux.* ]]; then
													sh build.sh -gtk3 checklibs install
												elif [[ ${PLATFORM} == gtk4.linux.* ]]; then
													# We build both 3 + 4, but we only keep libswt-pi4-gtk
													# We build both to help catch build errors as this
													# build runs against more modern gcc/libs and helps
													# with verification
													sh build.sh -gtk3 checklibs
													sh build.sh clean
													sh build.sh -gtk4 checklibs install-pi-only
												elif [[ ${PLATFORM} == cocoa.macosx.* ]]; then
													sh build.sh install 
												else
													echo "Unexpected build platform ${PLATFORM}"
													exit 1
												fi
												ls -1R libs
											'''
										} else {
											bat '''
												mkdir libs
												cmd /c build.bat install
											'''
										}
									}
									dir('libs') {
										stash "swt.binaries.${PLATFORM}"
									}
									cleanWs() // workspace not cleaned by default
								}
							}
						}
					}
					stage('Collect and sign binaries') {
						steps {
							dir("libs/${PLATFORM}") {
								unstash "swt.binaries.${PLATFORM}"
								sh '''#!/bin/bash -x
									if [[ ${PLATFORM} == cocoa.macosx.* ]]; then
										binariesExtension='jnilib'
										signerUrl='https://cbi.eclipse.org/macos/codesign/sign'
									elif [[ ${PLATFORM} == gtk.linux.* || ${PLATFORM} == gtk4.linux.* ]]; then
										binariesExtension='so'
										# Replace 'gtk4' by 'gtk' to copy the built gtk4 library into gtk fragment
										PLATFORM=${PLATFORM/#gtk4/gtk}
									elif [[ ${PLATFORM} == win32.win32.* ]]; then
										binariesExtension='dll'
										signerUrl='https://cbi.eclipse.org/authenticode/sign'
									fi
									if [[ -n "$signerUrl" ]]; then
										echo "Sign ${PLATFORM} libraries"
										if [[ "${BRANCH_NAME}" == master ]] || [[ "${BRANCH_NAME}" =~ R[0-9]+_[0-9]+(_[0-9]+)?_maintenance ]]; then
											for file in *.${binariesExtension}; do
												mv $file unsigned-$file
												curl --fail --form "file=@unsigned-$file" --output "$file" "$signerUrl"
												rm unsigned-$file
											done
										fi
									fi
									cp *.$binariesExtension "${WORKSPACE}/eclipse.platform.swt/binaries/org.eclipse.swt.${PLATFORM}/"
								'''
							}
						}
					}
				}
			}
		}
		stage('Commit SWT-native binaries, if build') {
			when {
				expression { NATIVES_CHANGED }
			}
			steps {
				dir('eclipse.platform.swt') {
					withEnv(["swt_version=${getSWTVersions()['swt_version']}"]) { // versions are read from updated file
						sh '''
							find binaries -name "*${swt_version}*" -type f -exec chmod 755 {} +
							
							git add --all *
							git status
							git commit -m "v${swt_version}"
							git tag "v${swt_version}"
							
							git status
							git log --patch -2
						'''
					}
				}
			}
		}
		stage('Build') {
			steps {
				xvnc(useXauthority: true) {
					dir('eclipse.platform.swt') {
						sh '''
							mvn clean verify \
								--batch-mode --threads 1C -V -U -e \
								-Pbree-libs -Papi-check -Pjavadoc \
								-Dcompare-version-with-baselines.skip=false \
								-Dorg.eclipse.swt.tests.junit.disable.test_isLocal=true \
								-Dmaven.test.failure.ignore=true -Dmaven.test.error.ignore=true
						'''
					}
				}
			}
			post {
				always {
					junit allowEmptyResults: true, testResults: 'eclipse.platform.swt/tests/*.test*/target/surefire-reports/*.xml'
					archiveArtifacts allowEmptyArchive: true, artifacts: '**/*.log,*/binaries/*/target/*.jar', excludes: '**/*-sources.jar'
					discoverGitReferenceBuild referenceJob: 'eclipse.platform.swt/master'
					// To accept unstable builds (test errors or new warnings introduced by third party changes) as reference using "ignoreQualityGate:true"
					recordIssues enabledForFailure: true, publishAllIssues: true, ignoreQualityGate: true, tools: [
							eclipse(name: 'Compiler', pattern: '**/target/compilelogs/*.xml'),
							issues(name: 'API Tools', id: 'apitools', pattern: '**/target/apianalysis/*.xml'),
							javaDoc()
						], qualityGates: [[threshold: 1, type: 'DELTA', unstable: true]]
					recordIssues enabledForFailure: true, publishAllIssues: true, ignoreQualityGate:true, tool: mavenConsole(), qualityGates: [[threshold: 1, type: 'DELTA_ERROR', unstable: true]]
				}
			}
		}
		stage('Push SWT-native binaries, if build') {
			when {
				expression { NATIVES_CHANGED }
			}
			steps {
				sshagent(['github-bot-ssh']) {
					dir('eclipse.platform.swt') {
						sh """
							# Check for the master-branch as late as possible to have as much of the same behavior as possible
							if [[ '${BRANCH_NAME}' == master ]] || [[ '${BRANCH_NAME}' =~ R[0-9]+_[0-9]+(_[0-9]+)?_maintenance ]]; then
								if [[ ${params.skipCommit} != true ]]; then
									
									# Don't rebase and just fail in case another commit has been pushed to the master/maintenance branch in the meantime
									git push origin HEAD:refs/heads/${BRANCH_NAME}
									git push origin refs/tags/${getLatestGitTag()}
									
									exit 0
								else
									echo Committing is skipped
								fi
							else
								echo Skip pushing changes of native-binaries for branch '${BRANCH_NAME}'
							fi
						"""
					}
				}
			}
		}
	}
}
