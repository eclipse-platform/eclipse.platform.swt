#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2016 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#*******************************************************************************

HELP="
Build Gtk2 or Gtk3 bindings and (optionally) copy them to binary repository.
Paramaters (specified in this order):
clean    - delete *.o and *.so files from current folder. If this is the only paramater, do nothing else.
			But if other paramaters are given and this is the first one, then continue with other actions.

One of the following 3:
-gtk2   : Build bindings with GTK2.
-gtk3   : Build bindings with GTK3.
-gtk-all : Build bindings with GTK2 as well as GTK3. Note, this flag triggers cleanups before each build
			because a cleanup is required when buliding different GTK versions for linking to be done correctly.
			During active development, if you only want to compile updated files, use -gtk2/-gtk3 flags instead,
			however do not forget to do a cleanup in between gtk2/gtk3.

install  - copy *.so libraries to binary repository.

-- Examples:
Most commonly used:
./build.sh -gtk-all install
This will clean everything in your repository, build GTK2 and GTK3, then copy .so files to binary repository.

Also:
./build.sh     	  - only build .so files, do not copy them across. Build according to what GTK_VERSION is set to.
./build.sh clean  	  - clean working directory of *.o and *.so files.
./build.sh install	  - build.so files and copy to binary repository
./build.sh -gtk3 install  - Build only updated files (or all), copy *.so libs to binary repository.

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

# No longer necessary, but may be useful in future if we want to compile swt.idl rather than using a static one
#
# COMPONENTS_DIR=`pwd`/../../components
# if test ! -f ${COMPONENTS_DIR}/external.xpt; then
#	if test ! -f ${COMPONENTS_DIR}/external.idl; then
#		echo "Can't find ${COMPONENTS_DIR}/external.idl"
#	else
#		IDLDIR=`pkg-config --variable=idldir libxul | sed 's@/stable$@@'`/unstable
#		if test ! -d ${IDLDIR}; then
#			IDLDIR=`pkg-config --variable=idldir libxul`
#		fi
#		XPIDL=`pkg-config --variable=sdkdir libxul`/bin/xpidl
#		echo "${XPIDL} -m typelib -I ${IDLDIR} -e ${COMPONENTS_DIR}/external.xpt ${COMPONENTS_DIR}/external.idl"
#		${XPIDL} -m typelib -I ${IDLDIR} -e ${COMPONENTS_DIR}/external.xpt ${COMPONENTS_DIR}/external.idl
#	fi
# fi

# Determine which OS we are on
if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
case $OS in
	"AIX")
		SWT_OS=aix
		MAKEFILE=make_aix.mak
		;;
	"HP-UX")
		SWT_OS=hpux
		MAKEFILE=make_hpux.mak
		;;
	"SunOS")
		SWT_OS=solaris
		PROC=`uname -i`
		MAKEFILE=make_solaris.mak
		if [ "${MODEL}" = "" ]; then
			MODEL=`isainfo -k`
			if [ "${MODEL}" = "amd64" ]; then
				MODEL=x86_64
				MAKEFILE=make_solaris_x86_64.mak
				MAKE_TYPE=gmake
			fi
		fi
		;;
	"FreeBSD")
		SWT_OS=freebsd
		MAKEFILE=make_freebsd.mak
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
	i?86)
		SWT_ARCH=x86
		AWT_ARCH=i386
		;;
	*)
		SWT_ARCH=$MODEL
		AWT_ARCH=$MODEL
		;;
esac

case $SWT_OS.$SWT_ARCH in
	"linux.x86")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/x86/ibm-java2-i386-50"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib/pkgconfig:/bluebird/teamswt/swt-builddir/cairo_1.0.2/linux_x86/lib/pkgconfig"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/bluebird/teamswt/swt-builddir/mozilla/1.4/linux_gtk2/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/1.8.0.4/gecko-sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="${MOZILLA_SDK}/../lib/libembedstring.a -L${MOZILLA_SDK}/../bin -L${MOZILLA_SDK}/../lib/ -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
#		if [ "${XULRUNNER31_SDK}" = "" ]; then
#			export XULRUNNER31_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/31/x86"
#		fi
		;;
	"linux.x86_64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			# Validate that we set JAVA_HOME to an existing directory. If not try to look for a better one.
			BLUEBIRD_JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/x86_64/jdk1.5.0"
			if [ -d "$BLUEBIRD_JAVA_HOME" ]; then
				func_echo_plus "JAVA_HOME not set, configured to: $BLUEBIRD_JAVA_HOME"
				export JAVA_HOME="$BLUEBIRD_JAVA_HOME"
			else
				# Cross-platform method of finding JAVA_HOME.
				# Tested on Fedora 24 and Ubuntu 16
				DYNAMIC_JAVA_HOME=`readlink -f /usr/bin/java | sed "s:jre/bin/java::"`
				if [ -a "${DYNAMIC_JAVA_HOME}include/jni.h" ]; then
                	func_echo_plus "JAVA_HOME not set, but jni.h found, dynamically configured to $DYNAMIC_JAVA_HOME"
            		export JAVA_HOME="$DYNAMIC_JAVA_HOME"
                else
                	func_echo_error "JAVA_HOME not set and jni.h could not be located. You might get a compile error about include 'jni.h'. You should install 'java-*-openjdk-devel' package or if you have it installed already, find jni.h and  set JAVA_HOME manually to base of 'include' folder"
                fi
			fi
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/bluebird/teamswt/swt-builddir/mozilla/1.7/amd64/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/include"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.0.1/amd64/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
#		if [ "${XULRUNNER31_SDK}" = "" ]; then
#			export XULRUNNER31_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/31/x86_64"
#		fi
		;;
	"linux.ppc")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/PPC/ibm-java2-ppc-50"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK=" /bluebird/teamswt/swt-builddir/mozilla/1.7/ppc/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.1.1/ppc/mozilla/dist/sdk/"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/include"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/bluebird/teamswt/swt-builddir/cairo_1.0.2/linux_ppc/lib/pkgconfig/"
		fi
		;;
	"linux.ppc64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/PPC64/jre5u10"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK=" /bluebird/teamswt/swt-builddir/mozilla/1.7/ppc64/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-m64 -L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/include"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.1.1/ppc64/mozilla/dist/sdk/"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-m64 -L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig/"
		fi
		;;
	"linux.ppc64le")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/PPC64LE/jre5u10"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK=" /bluebird/teamswt/swt-builddir/mozilla/1.7/ppc64le/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-m64 -L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/include"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.1.1/ppc64le/mozilla/dist/sdk/"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-m64 -L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig/"
		fi
		;;
	"solaris.x86_64")
		if [ "${CC}" = "" ]; then
			export CC="cc"
		fi
		if [ "${CXX}" = "" ]; then
			export CXX="CC"
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/build/JRE/Solaris_x64/jdk1.8.0_71"
		fi
#		if [ "${MOZILLA_SDK}" = "" ]; then
#			export MOZILLA_SDK="/bluebird/teamswt/bog/mozilla/solaris_x86/1.7/mozilla/dist/sdk"
#		fi
#		if [ "${XULRUNNER_SDK}" = "" ]; then
#			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.0.1/solaris-x86/mozilla/dist/sdk"
#		fi
#		if [ "${MOZILLA_INCLUDES}" = "" ]; then
#			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/include"
#		fi
#		if [ "${MOZILLA_LIBS}" = "" ]; then
#			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
#		fi
#		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
#			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
#		fi
#		if [ "${XULRUNNER_LIBS}" = "" ]; then
#			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
#		fi
 		;;
	"solaris.sparcv9")
		PATH="/usr/ccs/bin:/opt/csw/bin:$PATH"
		if [ "${CC}" = "" ]; then
			CC="cc"
		fi	
		if [ "${CXX}" = "" ]; then
			CXX="CC"
		fi
		if [ "${CDE_HOME}" = "" ]; then
			CDE_HOME="/usr/dt"
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/SOLARIS/SPARC64/jdk1.5.0_22"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			PKG_CONFIG_PATH="/opt/csw/lib/pkgconfig"
		fi
#		if [ "${MOZILLA_SDK}" = "" ]; then
#			export MOZILLA_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/1.4/gecko-sdk"
#		fi
#		if [ "${MOZILLA_INCLUDES}" = "" ]; then
#			export MOZILLA_INCLUDES="-I${MOZILLA_SDK} -I${MOZILLA_SDK}/xpcom/include -I${MOZILLA_SDK}/nspr/include -I${MOZILLA_SDK}/embed_base/include -I${MOZILLA_SDK}/embedstring/include -I${MOZILLA_SDK}/string/include"
#		fi
#		if [ "${MOZILLA_LIBS}" = "" ]; then
#			export MOZILLA_LIBS="${MOZILLA_SDK}/embedstring/bin/libembedstring.a -L${MOZILLA_SDK}/xpcom/bin -L${MOZILLA_SDK}/nspr/bin -lxpcom -lnspr4 -lplds4 -lplc4"
#		fi
		export PATH CC CXX CDE_HOME JAVA_HOME PKG_CONFIG_PATH;
		;;
	"linux.s390")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/home/swtbuild/java5/s390/ibm-java2-s390-50"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/home/swtbuild/mozilla/s390/mozilla-1.7.13/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/home/swtbuild/mozilla/s390/xulrunner-1.8.0.1/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib/pkgconfig"
		fi
		;;
	"linux.s390x")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/home/swtbuild/java5/s390x/ibm-java2-s390x-50"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/home/swtbuild/mozilla/s390x/mozilla1.7.13/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/home/swtbuild/mozilla/s390x/xulrunner-1.8.0.1/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig"
		fi
		;;
	"aix.ppc")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/aixj9_r5"
		fi
		;;
	"aix.ppc64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/AIX/PPC64/j564/sdk"
		fi
		;;
	"hpux.ia64")
		export PATH="/opt/hp-gcc/bin:/opt/gtk_64bit/bin:/opt/${PATH}"
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/opt/java1.5"
		fi
		if [ "${AWT_LIB_PATH}" = "" ]; then
			export AWT_LIB_PATH="/opt/java1.5/jre/lib/IA64W/"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/opt/gtk_64bit/lib/hpux64/pkgconfig"
		fi
		;;
esac	


# For 64-bit CPUs, we have a switch
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64' -o ${MODEL} = 'ia64' -o ${MODEL} = 'sparcv9'  -o ${MODEL} = 's390x' -o ${MODEL} = 'ppc64le' -o ${MODEL} = 'aarch64' ]; then
	SWT_PTR_CFLAGS=-DJNI64
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
	if [ ${MODEL} = 'ppc64' -o ${MODEL} = 'ppc64le' ]; then
		if [ ${OS} = 'AIX' ]; then
			SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -maix64"
			SWT_LFLAGS=-maix64
			export SWT_LFLAGS
		else
			SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -m64"
			XLIB64="${XLIB64} -L/usr/lib64"
			SWT_LFLAGS=-m64
			export SWT_LFLAGS
		fi
	fi
	if [ ${MODEL} = 'ia64' ]; then
		if [ ${OS} = 'HP-UX' ]; then
			SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -mlp64"
			SWT_LFLAGS=-mlp64
			export SWT_LFLAGS
		fi
	fi
	if [ ${OS} = 'SunOS' ]; then
			SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -m64"
			SWT_LFLAGS=-m64
			SWT_CDE_64SUFFIX="/64"
			export SWT_LFLAGS SWT_CDE_64SUFFIX
	fi
	export SWT_PTR_CFLAGS
fi
if [ ${MODEL} = 's390' ]; then
	SWT_PTR_CFLAGS="-m31"
	SWT_LFLAGS=-m31
	export SWT_LFLAGS SWT_PTR_CFLAGS
fi
if [ ${MODEL} = 'x86' -a ${SWT_OS} = 'linux' ]; then
	SWT_PTR_CFLAGS="-m32"
	SWT_LFLAGS=-m32
	export SWT_LFLAGS SWT_PTR_CFLAGS
fi


if [ x`pkg-config --exists cairo && echo YES` = "xYES" ]; then
	func_echo_plus "Cairo found, compiling SWT support for the cairo graphics library."
	MAKE_CAIRO=make_cairo
else
	func_echo_error "Cairo not found: Advanced graphics support using cairo will not be compiled."
fi

if [ -z "${MOZILLA_INCLUDES}" -a -z "${MOZILLA_LIBS}" -a ${SWT_OS} != 'solaris' ]; then
	if [ x`pkg-config --exists mozilla-xpcom && echo YES` = "xYES" ]; then
		MOZILLA_INCLUDES=`pkg-config --cflags mozilla-xpcom`
		MOZILLA_LIBS=`pkg-config --libs mozilla-xpcom`
		export MOZILLA_INCLUDES
		export MOZILLA_LIBS
		MAKE_MOZILLA=make_mozilla
	elif [ x`pkg-config --exists firefox-xpcom && echo YES` = "xYES" ]; then
		MOZILLA_INCLUDES=`pkg-config --cflags firefox-xpcom`
		MOZILLA_LIBS=`pkg-config --libs firefox-xpcom`
		export MOZILLA_INCLUDES
		export MOZILLA_LIBS
		MAKE_MOZILLA=make_mozilla
	elif [ x`pkg-config --exists libxul && echo YES` = "xYES" ]; then
		XULRUNNER_INCLUDES=`pkg-config --cflags libxul`
		XULRUNNER_LIBS=`pkg-config --libs libxul`
		export XULRUNNER_INCLUDES
		export XULRUNNER_LIBS
		MAKE_MOZILLA=make_xulrunner
	else
		func_echo_error "None of the following libraries were found:  Mozilla/XPCOM, Firefox/XPCOM, or XULRunner/XPCOM:"
		func_echo_error "   >> Mozilla embedding support will not be compiled."
	fi
fi

# Find AWT if available
if [ -z "${AWT_LIB_PATH}" ]; then
	if [ -f ${JAVA_HOME}/jre/lib/${AWT_ARCH}/libjawt.* ]; then
		AWT_LIB_PATH=${JAVA_HOME}/jre/lib/${AWT_ARCH}
		export AWT_LIB_PATH
	else
		AWT_LIB_PATH=${JAVA_HOME}/jre/bin
		export AWT_LIB_PATH
	fi
fi

if [ -f ${AWT_LIB_PATH}/libjawt.* ]; then
	func_echo_plus "libjawt.so found, the SWT/AWT integration library will be compiled."
	MAKE_AWT=make_awt
else
	func_echo_error "libjawt.so not found, the SWT/AWT integration library will not be compiled."
fi


func_configue_MAKE_GNOME () {
	# Prerequisite: This function should only be called under gtk2.
	if [ x`pkg-config --exists gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0 && echo YES` = "xYES"  -a ${MODEL} != "sparcv9" -a ${MODEL} != 'ia64' ]; then
		if [ "${SWT_OS}" != "solaris" -o "${MODEL}" != "x86_64" ]; then
			func_echo_plus "libgnomeui-2.0 found, compiling SWT program with GNOME 2.0 support"
			MAKE_GNOME=make_gnome
			fi
	else
		func_echo_error "libgnome-2.0 and libgnomeui-2.0 not found:"
		func_echo_error "  >>> SWT Program support for GNOME will not be compiled."
	fi
}




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
		func_echo_error "1) Maybe you forgot to checkout SWT binaries? See: https://git.eclipse.org/c/platform/eclipse.platform.swt.binaries.git/"
		func_echo_error "2) SWT and SWT binary git repos have to be in the same folder, (usually ~/git/...). Maybe you put them in different folders?"
		func_echo_error "Exit with failure"
		exit 1
		fi
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

func_build_gtk3 () {
	export GTK_VERSION=3.0
	func_echo_plus "Building GTK3 bindings:"
	${MAKE_TYPE} -f $MAKEFILE all $MAKE_GNOME $MAKE_CAIRO $MAKE_AWT $MAKE_MOZILLA "${@}"
	RETURN_VALUE=$?   #make can return 1 or 2 if it fails. Thus need to cache it in case it's used programmatically somewhere.
	if [ "$RETURN_VALUE" -eq 0 ]; then
		func_echo_plus "GTK3 Build succeeded"
	else
		func_echo_error "GTK3 Build failed, aborting further actions.."
		exit $RETURN_VALUE
	fi
}

func_build_gtk2 () {
	func_echo_plus "Building GTK2 bindings:"
	func_configue_MAKE_GNOME
	export GTK_VERSION=2.0
	${MAKE_TYPE} -f $MAKEFILE all $MAKE_GNOME $MAKE_CAIRO $MAKE_AWT $MAKE_MOZILLA "$@"
	RETURN_VALUE=$?
	if [ "$RETURN_VALUE" -eq 0 ]; then
		func_echo_plus "GTK2 Build succeeded"
	else
		func_echo_error "GTK2 Build failed."
		exit $RETURN_VALUE
	fi
}


if [ "$1" = "-gtk-all" ]; then
	shift
	func_echo_plus "Note: When building multiple GTK versions, a cleanup is required (and automatically performed) between them."
	func_clean_up
	func_build_gtk3 "$@"
	func_clean_up
	func_build_gtk2 "$@"
elif [ "$1" = "-gtk3" ]; then
	shift
	func_build_gtk3 "$@"
elif [ "$1" = "-gtk2" ]; then
	shift
	func_build_gtk2 "$@"
elif [ "${GTK_VERSION}" = "3.0" ]; then
	func_build_gtk3 "$@"
elif [ "${GTK_VERSION}" = "2.0" -o "${GTK_VERSION}" = "" ]; then
	export GTK_VERSION="2.0"
	func_build_gtk2 "$@"
fi