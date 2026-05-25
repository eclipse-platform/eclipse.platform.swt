#!/bin/bash
# This script installs relevant system dependencies for ./build.sh to compile JNI bindings.
# Each linux distribution has a different package manager, so the script has to be adapted
# for each distribution.
# To add your distribution:
# 1) Identify your distribution ID via:
#    lsb_release -a
# 2) Add a case to the case statement below.
# 3) Add a function to handle your distribution.

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

func_echo_plus () {
	# Echo function that prints output in green to distinguish it from sub-shell output.
	echo -e "${GREEN}${@}${NC}"
}

func_echo_error () {
	# As above, but in red. Also pre-appends '***' to output.
	echo -e "${RED}*** ${@}${NC}"
}

func_configure_fedora () {

	func_echo_plus "Installing C Development Tools"
	sudo dnf -y groups install "C Development Tools and Libraries"

	func_echo_plus "Installing Java development packages that include jni.h for JNI bindings."
	sudo dnf -y install java-25-openjdk-devel

	func_echo_plus "Installing Gtk development packages"
	sudo dnf -y install gtk3-devel gtk4-devel

	func_echo_plus "Install Mesa (OpenGL headers)"
	# Deals with error: "/usr/bin/ld: cannot find -lGLU collect2: error: ld returned 1 exit status"
	sudo dnf -y install mesa-libGLU-devel

	func_echo_plus "Done"
}


if [ -f /etc/os-release ]; then
    . /etc/os-release
    LINUX_DISTRO="$ID"
else
    echo "Error: cannot identify distribution"
    exit 1
fi

DISTRO_NOT_KNOWN_MSG="
Currently the script doesn't know how to automatically configure your distro : $LINUX_DISTRO
Consider updating this script for your distribution.
In general, You should install the following packages:
 - C Development tools (usually comes in a 'group install')
 - java-*-openjdk-devel  (depending on current version of java)
 - gtk3-devel and gtk4-devel
 - mesa-libGLU-devel
"


case "$LINUX_DISTRO" in
"fedora")
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

