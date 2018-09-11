#!/bin/bash
# This script installs relevant system dependencies for ./build.sh to compile JNI bindings.
# Each linux distribution has a different package manager, so the script has to be adapted
# for each distribution.
# To add your distribution:
# 1) Identify your distribution ID via:
#    lsb_release -a
# 2) Add a case to the case statment below.
# 3) Add a function to handle your distribution.


func_echo_plus () {
	# Echo function that prints output in green to distinguish it from sub-shell output.
	GREEN='\033[0;32m'
	NC='\033[0m' # No Color
	echo -e "${GREEN}${@}${NC}"
}

func_echo_error () {
	# As above, but in red. Also pre-appends '***' to output.
	RED='\033[0;31m'
	NC='\033[0m' # No Color
echo -e "${RED}*** ${@}${NC}"
}

func_configure_fedora () {
	FEDORA_VERSION=$(cat /etc/system-release | cut -f3 -d" ")
	if [ "$FEDORA_VERSION" -lt "21" ]; then
		INSTALL_CMD="yum"
	else
		INSTALL_CMD="dnf"
	fi


	func_echo_plus "Installing C Development Tools"
	set -x
	sudo $INSTALL_CMD -y groups install "C Development Tools and Libraries"

	func_echo_plus "Installing Java 8 development packages that include jni.h for JNI bindings. Update this script to '9' when java 9 comes out"
	sudo $INSTALL_CMD -y install java-1.8.0-openjdk-devel.x86_64

	func_echo_plus "Installing Gtk3 development packages"
	sudo $INSTALL_CMD -y install gtk3-devel

	func_echo_plus "Installing X11 Development libraries. Someday when wayland takes over these will not be needed..."
	# Deals with error: "#include <X11/Intrinsic.h>, #include <X11/extensions/XTest.h>" build errors)
	sudo $INSTALL_CMD -y install libXt-devel

	func_echo_plus "Install Mesa (OpenGL headers)"
	# Deals with error: "/usr/bin/ld: cannot find -lGLU collect2: error: ld returned 1 exit status"
	sudo $INSTALL_CMD -y install mesa-libGLU-devel

	func_echo_plus "Done"
}


LINUX_DISTRO=$(cat /etc/system-release | cut -f1 -d" ")
if [ "$LINUX_DISTRO" = "" ]; then
	func_echo_error "Error, could not identify your distribution"
	exit
fi

DISTRO_NOT_KNOWN_MSG="
Currently the script doesn't know how to automatically configure your distro : $LINUX_DISTRO
Consider updating this script for your distribution.
In general, You should install the following packages:
 - C Development tools (usually comes in a 'group install')
 - java-*-openjdk-devel  (depending on current version of java)
 - gtk3-devel
 - libXt-devel
 - mesa-libGLU-devel
"


case "$LINUX_DISTRO" in
"Fedora")
	func_echo_plus "Fedora found. Installing packages..."
	func_configure_fedora
	;;
"YOUR_DISTRIBUTION")
	echo "YOUR_DISTRIBUTION HERE"
	;;
*)
	func_echo_error "$DISTRO_NOT_KNOWN_MSG"
	;;
esac


# check if .classpath exists in swt project.
if [ -a "../org.eclipse.swt/.classpath" ]; then 
	func_echo_plus ".classpath found, you are good to go";
else 
	func_echo_error "Warning: ../org.eclipse.swt/.classpath not found. Normally you rename ../org.eclipse.swt/.classpath_gtk to ../*/.classpath manually"
fi
