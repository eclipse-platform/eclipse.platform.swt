#!/bin/sh

#
# Place of the SWT dll. 
#
SWT_DLL="/Users/weinand/tmp/eclipse/export/libswt-carbon-????.jnilib"

#
# In order to build an application bundle under MacOS X we need
# a small stub that reads the various artefacts of a bundle and
# starts the Java VM. We copy the stub from the JavaVM framework.
#
JAVASTUB="/System/Library/Frameworks/JavaVM.framework/Versions/A/Resources/MacOS/JavaApplicationStub"

#
# Where to build the temporary application bundle
#
TMP_APP_DIR="/tmp/swt_stubs"

#
# We remember the current working directory
# so that we can later define the property "com.apple.mrj.application.workingdirectory"
#
CURRENT_DIR="$PWD"

#
# Process command line arguments until we see the main class...
#
while [ $# -gt 0 ]; do
	case "$1" in
		-classpath | -cp )
			CLASS_PATH="$2"
			shift;
			;;	
		-Xbootclasspath* )
			#echo "ignoring Xbootclasspath"
			;;
		-* )
			VM_OPTIONS="$VM_OPTIONS $1"
			;;
		* )
			MAIN_CLASS="$1"
			shift;
			break;
			;;
	esac
	shift
done

#
# All options and arguments following the main class name
# are passed as a single property "com.apple.mrj.application.parameters"
#
PARAMETERS="$*"

#
# Uncomment when debugging
#
#echo "Working Directory: $PWD"
#echo "VM Options: $VM_OPTIONS"
#echo "Main Class: $MAIN_CLASS"
#echo "Parameters: $PARAMETERS"

#
# Application name is name of main class without package prefix 
#
APP_NAME=`echo $MAIN_CLASS | awk -F. '{ print $(NF) }' `
LAUNCHER="$APP_NAME"

#
# Create the parent directory for the application bundle 
#
mkdir -p $TMP_APP_DIR
cd $TMP_APP_DIR

#
# Create the application bundle 
#
rm -rf $APP_NAME.app
mkdir -p $APP_NAME.app/Contents/MacOS

cd $APP_NAME.app/Contents

#
# Copy the JavaAppLauncher into the bundle 
#
cp $JAVASTUB MacOS/$APP_NAME

#
# Create a symbolic link to the SWT dll (*.jnilib)
#
mkdir -p Resources/Java
ln -s $SWT_DLL Resources/Java

#
# Create the PkgInfo file.
#
echo "APPL????" > PkgInfo

#
# Create the Info.plist file.
#
cat > Info.plist <<End_Of_Input
<?xml version="1.0" encoding="UTF-8"?>
<plist version="0.9">
  <dict>
	<key>CFBundleExecutable</key><string>$APP_NAME</string>
  </dict>
</plist>
End_Of_Input

#
# Create an old-style MRJApp.properties containing the relevant options.
# We cannot put the properties in the Info.plist file because a SWT app
# will crash on startup.
#
cat > Resources/MRJApp.properties <<End_Of_Input
com.apple.mrj.application.apple.menu.about.name= $APP_NAME
com.apple.mrj.application.classpath= $CLASS_PATH
com.apple.mrj.application.workingdirectory= $CURRENT_DIR
com.apple.mrj.application.vm.options= $VM_OPTIONS
com.apple.mrj.application.main= $MAIN_CLASS
com.apple.mrj.application.parameters= $PARAMETERS
End_Of_Input

#
# Start the JavaAppLauncher by replacing this shell script
# to ensure that the process id is preserved.
#
exec $TMP_APP_DIR/$APP_NAME.app/Contents/MacOS/$APP_NAME

echo "never reached unless there is an error"