#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2020 IBM Corporation and others.
#
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#*******************************************************************************

HELP="
Build GTK4 or GTK3 bindings and (optionally) copy them to binary repository.
Paramaters (specified in this order):
clean    - delete *.o and *.so files from current folder. If this is the only paramater, do nothing else.
			But if other paramaters are given and this is the first one, then continue with other actions.

One of the following 3:
-gtk3   : Build bindings with GTK3.
-gtk4   : Build bindings with GTK4.
-gtk-all : Build bindings with GTK3 as well as GTK4. Note, this flag triggers cleanups before each build
			because a cleanup is required when building different GTK versions for linking to be done correctly.
			During active development, if you only want to compile updated files, use -gtk3/-gtk4 flags instead,
			however do not forget to do a cleanup in between gtk3/gtk4.

install  - copy *.so libraries to binary repository.

--print-outputdir-and-exit - This simply echos the OUTPUT folder where .so libs are copied to,
							 then exits the script. Used by external

-- Examples:
Most commonly used:
./build.sh -gtk-all install
This will clean everything in your repository, build GTK3 and GTK4, then copy .so files to binary repository.

Also:
./build.sh     	  - only build .so files, do not copy them across. Build according to what GTK_VERSION is set to.
./build.sh clean  	  - clean working directory of *.o and *.so files.
./build.sh -gtk4 install	 - build.so files and copy to binary repository

Also note:
Sometimes you might have to cleanup the binary repository manually as old *.so files are not automatically removed
by clean command. Navigate to binary repository and:
git clean -xdf #If new files were added to repo.
git reset --hard  #If existing binary was overwritten.
"

if [ "$1" = "-h" -o "$1" = "--help" ]; then
	echo "$HELP"
	exit
fi

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

cd `dirname $0`

MAKE_TYPE=make

export CFLAGS='-O -Wall -fPIC'

# Determine which OS we are on
if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
case $OS in
	"Windows_NT")
		SWT_OS=win32
		MAKEFILE=make_win32.mak
		;;
	*)
		SWT_OS=`uname -s | tr -s '[:upper:]' '[:lower:]'`
		MAKEFILE=make_linux.mak
		;;
esac

# Determine which CPU type we are building for
if [ "${MODEL}" = "" ]; then
	if uname -i > /dev/null 2>&1; then
		MODEL=`uname -i`
		if [ ${MODEL} = 'unknown' ]; then
		  MODEL=`uname -m`
		fi
	else
		MODEL=`uname -m`
	fi
fi
case $MODEL in
	"x86_64")
		SWT_ARCH=x86_64
		AWT_ARCH=amd64
		;;
	*)
		SWT_ARCH=$MODEL
		AWT_ARCH=$MODEL
		;;
esac

case $SWT_OS.$SWT_ARCH in
	"linux.x86_64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			# Cross-platform method of finding JAVA_HOME.
			# Tested on Fedora 24 and Ubuntu 16
			DYNAMIC_JAVA_HOME=`readlink -f /usr/bin/java | sed "s:jre/::" | sed "s:bin/java::"`
			if [ -e "${DYNAMIC_JAVA_HOME}include/jni.h" ]; then
				func_echo_plus "JAVA_HOME not set, but jni.h found, dynamically configured to $DYNAMIC_JAVA_HOME"
				export JAVA_HOME="$DYNAMIC_JAVA_HOME"
			else
				func_echo_error "JAVA_HOME not set and jni.h could not be located. You might get a compile error about include 'jni.h'. You should install 'java-*-openjdk-devel' package or if you have it installed already, find jni.h and  set JAVA_HOME manually to base of 'include' folder"
			fi
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig"
		fi
		;;
	"linux.ppc64le")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME=`readlink -f /usr/bin/java | sed "s:jre/bin/java::"`
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig/"
		fi
		;;
	"linux.loongarch64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME=`readlink -f /usr/bin/java | sed "s:jre/bin/java::"`
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig/"
		fi
		;;
esac


# For 64-bit CPUs, we have a switch
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64le' -o ${MODEL} = 'aarch64' -o ${MODEL} = 'loongarch64' ]; then
	SWT_PTR_CFLAGS=-DJNI64
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
	if [ ${MODEL} = 'ppc64le' ]; then
		SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -m64"
		XLIB64="${XLIB64} -L/usr/lib64"
		SWT_LFLAGS=-m64
		export SWT_LFLAGS
	fi
	export SWT_PTR_CFLAGS
fi

if [ x`pkg-config --exists cairo && echo YES` = "xYES" ]; then
	func_echo_plus "Cairo found, compiling SWT support for the cairo graphics library."
	MAKE_CAIRO=make_cairo
else
	func_echo_error "Cairo not found: Advanced graphics support using cairo will not be compiled."
fi

# Find AWT if available
if [ ${SWT_OS} = 'win32' ]; then
	AWT_LIB_EXPR="jawt.dll"
else
	AWT_LIB_EXPR="libjawt.so"
fi

if [ -z "${AWT_LIB_PATH}" ]; then
	if [ -f ${JAVA_HOME}/jre/lib/${AWT_ARCH}/${AWT_LIB_EXPR} ]; then
		AWT_LIB_PATH=${JAVA_HOME}/jre/lib/${AWT_ARCH}
		export AWT_LIB_PATH
	elif [ -f ${JAVA_HOME}/lib/${AWT_LIB_EXPR} ]; then
		AWT_LIB_PATH=${JAVA_HOME}/lib
		export AWT_LIB_PATH
	else
		AWT_LIB_PATH=${JAVA_HOME}/jre/bin
		export AWT_LIB_PATH
	fi
fi

if [ -f ${AWT_LIB_PATH}/${AWT_LIB_EXPR} ]; then
	func_echo_plus "libjawt.so found, the SWT/AWT integration library will be compiled."
	MAKE_AWT=make_awt
else
	func_echo_error "libjawt.so not found, the SWT/AWT integration library will not be compiled."
fi

## Interaction(s) with makefile(s) below:

# Configure OUTPUT_DIR
if [ "x${OUTPUT_DIR}" = "x" ]; then
	OUTPUT_DIR=../../../../../eclipse.platform.swt.binaries/bundles/org.eclipse.swt.gtk.${SWT_OS}.${SWT_ARCH}
	if [ -d "$OUTPUT_DIR" ]; then
		export OUTPUT_DIR
	fi
fi

# Safety check:
# If "install" was given as target, check that OUTPUT_DIR is a valid directory.
for i in "$@"; do  # loop over all input paramaters
	if [ "$i" = "install" ]; then
		if [ ! -d "${OUTPUT_DIR}" ]; then   # if directory not valid.
		func_echo_error "ERROR: 'install' was passed in as paramater, but OUTPUT_DIR :"
		func_echo_error "(${OUTPUT_DIR}) "
		func_echo_error "is not a valid directory."
		func_echo_error "1) Maybe you forgot to checkout SWT binaries? See: https://github.com/eclipse-platform/eclipse.platform.swt.binaries/"
		func_echo_error "2) SWT and SWT binary git repos have to be in the same folder, (usually ~/git/...). Maybe you put them in different folders?"
		func_echo_error "Exit with failure"
		exit 1
		fi
	fi
done

for i in "$@"; do  # loop over all input paramaters
	if [ "$i" = "--print-outputdir-and-exit" ]; then
		# used by external scripts to find binary folder
		echo "OUTPUT_DIR=${OUTPUT_DIR}"
		exit 0 # success
	fi
done

func_clean_up () {
	func_echo_plus "Cleaning up..."
	${MAKE_TYPE} -f $MAKEFILE clean
}

if [ "x${1}" = "xclean" ]; then
	func_clean_up
	shift

	# if there are no more other parameters, exit.
	# don't exit if there are more paramaters. Useful for one-liners like: ./build.sh clean -gtk-all install
	if [ "$1" = "" ]; then
		exit $?
	fi
fi


# Announce our target
func_echo_plus "Building SWT/GTK+ for Architectures: $SWT_OS $SWT_ARCH"

func_build_gtk4 () {
	export GTK_VERSION=4.0

	func_echo_plus "Building GTK4 bindings:"
	${MAKE_TYPE} -f $MAKEFILE all $MAKE_CAIRO $MAKE_AWT "${@}"
	RETURN_VALUE=$?   #make can return 1 or 2 if it fails. Thus need to cache it in case it's used programmatically somewhere.
	if [ "$RETURN_VALUE" -eq 0 ]; then
		func_echo_plus "GTK4 Build succeeded"
	else
		func_echo_error "GTK4 Build failed, aborting further actions.."
		exit $RETURN_VALUE
	fi
}

func_build_gtk3 () {
	export GTK_VERSION=3.0

	func_echo_plus "Building GTK3 bindings:"
	${MAKE_TYPE} -f $MAKEFILE all $MAKE_CAIRO $MAKE_AWT "${@}"
	RETURN_VALUE=$?   #make can return 1 or 2 if it fails. Thus need to cache it in case it's used programmatically somewhere.
	if [ "$RETURN_VALUE" -eq 0 ]; then
		func_echo_plus "GTK3 Build succeeded"
	else
		func_echo_error "GTK3 Build failed, aborting further actions.."
		exit $RETURN_VALUE
	fi
}

if [ "$1" = "-gtk-all" ]; then
	shift
	func_echo_plus "Note: When building multiple GTK versions, a cleanup is required (and automatically performed) between them."
	func_clean_up
	func_build_gtk4 "$@"
	func_clean_up
	func_build_gtk3 "$@"
elif [ "$1" = "-gtk4" ]; then
	shift
	func_build_gtk4 "$@"
elif [ "$1" = "-gtk3" ]; then
	shift
	func_build_gtk3 "$@"
elif [ "${GTK_VERSION}" = "4.0" ]; then
	func_build_gtk4 "$@"
elif [ "${GTK_VERSION}" = "3.0" -o "${GTK_VERSION}" = "" ]; then
	export GTK_VERSION="3.0"
	func_build_gtk3 "$@"
fi
