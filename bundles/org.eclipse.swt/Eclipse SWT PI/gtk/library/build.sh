#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2011 IBM Corporation and others.
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

cd `dirname $0`

MAKE_TYPE=make

# Check if we have to compile external.xpt from external.idl
COMPONENTS_DIR=`pwd`/../../components
if test ! -f ${COMPONENTS_DIR}/external.xpt; then
	if test ! -f ${COMPONENTS_DIR}/external.idl; then
		echo "Can't find ${COMPONENTS_DIR}/external.idl"
	else
		IDLDIR=`pkg-config --variable=idldir libxul | sed 's@/stable$@@'`/unstable
		if test ! -d ${IDLDIR}; then
			IDLDIR=`pkg-config --variable=idldir libxul`
		fi
		XPIDL=`pkg-config --variable=sdkdir libxul`/bin/xpidl
		echo "${XPIDL} -m typelib -I ${IDLDIR} -e ${COMPONENTS_DIR}/external.xpt ${COMPONENTS_DIR}/external.idl"
		${XPIDL} -m typelib -I ${IDLDIR} -e ${COMPONENTS_DIR}/external.xpt ${COMPONENTS_DIR}/external.idl
	fi
fi

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
			if uname -p > /dev/null 2>&1; then
				MODEL=`uname -p`
			fi
		fi
		case $MODEL in
			"i386" | "x86")
				MAKEFILE=make_solaris_x86.mak
				MAKE_TYPE=gmake
				;;
		esac
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
	"sparc64")
		SWT_ARCH=$MODEL
		AWT_ARCH=sparcv9
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
echo "Building SWT OS=${SWT_OS} SWT ARCH=${SWT_ARCH}"

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
		;;
	"linux.x86_64")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/x86_64/jdk1.5.0"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/lib64/pkgconfig"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/bluebird/teamswt/swt-builddir/mozilla/1.7/amd64/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.0.1/amd64/mozilla/dist/sdk/"
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
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/PPC64/ibm-java2-ppc64-50"
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
	"solaris.x86")
		if [ "${CC}" = "" ]; then
			export CC="cc"
		fi
		if [ "${CXX}" = "" ]; then
			export CXX="CC"
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/x86/ibm-java2-i386-50"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/bluebird/teamswt/bog/mozilla/solaris_x86/1.7/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/bluebird/teamswt/swt-builddir/xulrunner/1.8.0.1/solaris-x86/mozilla/dist/sdk"
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
		;;
	"solaris.sparc64")
#		export PATH="/export/home/SUNWspro/bin:/usr/ccs/bin:/usr/bin"
		if [ "${CC}" = "" ]; then
			export CC="cc"
		fi
		if [ "${CXX}" = "" ]; then
			export CXX="CC"
		fi
		if [ "${CDE_HOME}" = "" ]; then
			export CDE_HOME="/usr/dt"
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/bluebird/teamswt/swt-builddir/JDKs/SOLARIS/SPARC64/jdk1.5.0_22"
		fi
#		if [ "${PKG_CONFIG_PATH}" = "" ]; then
#			export PKG_CONFIG_PATH="/usr/local/cairo-1.4.10/lib/pkgconfig"
#		fi
#		if [ "${MOZILLA_SDK}" = "" ]; then
#			export MOZILLA_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/1.4/gecko-sdk"
#		fi
#		if [ "${MOZILLA_INCLUDES}" = "" ]; then
#			export MOZILLA_INCLUDES="-I${MOZILLA_SDK} -I${MOZILLA_SDK}/xpcom/include -I${MOZILLA_SDK}/nspr/include -I${MOZILLA_SDK}/embed_base/include -I${MOZILLA_SDK}/embedstring/include -I${MOZILLA_SDK}/string/include"
#		fi
#		if [ "${MOZILLA_LIBS}" = "" ]; then
#			export MOZILLA_LIBS="${MOZILLA_SDK}/embedstring/bin/libembedstring.a -L${MOZILLA_SDK}/xpcom/bin -L${MOZILLA_SDK}/nspr/bin -lxpcom -lnspr4 -lplds4 -lplc4"
#		fi
		;;
	"solaris.sparc")
		PATH="/export/home/SUNWspro/bin:/usr/ccs/bin:/usr/bin:$PATH"
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
			JAVA_HOME="/usr/j2se"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			PKG_CONFIG_PATH="/usr/local/cairo-1.4.10/lib/pkgconfig/"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			MOZILLA_SDK="/bluebird/teamswt/swt-builddir/geckoSDK/1.4/gecko-sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			MOZILLA_INCLUDES="-I${MOZILLA_SDK} -I${MOZILLA_SDK}/xpcom/include -I${MOZILLA_SDK}/nspr/include -I${MOZILLA_SDK}/embed_base/include -I${MOZILLA_SDK}/embedstring/include -I${MOZILLA_SDK}/string/include"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			MOZILLA_LIBS="${MOZILLA_SDK}/embedstring/bin/libembedstring.a -L${MOZILLA_SDK}/xpcom/bin -L${MOZILLA_SDK}/nspr/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		export PATH CC CXX CDE_HOME JAVA_HOME PKG_CONFIG_PATH MOZILLA_SDK MOZILLA_INCLUDES MOZILLA_LIBS;
		;;
	"linux.s390")
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/home/swtbuild/java5/s390/ibm-java2-s390-50"
		fi
		if [ "${MOZILLA_SDK}" = "" ]; then
			export MOZILLA_SDK="/home/swtbuild/mozilla-1.7.13/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/home/swtbuild/xulrunner-1.8.0.1/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/local/lib/pkgconfig"
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
			export MOZILLA_SDK="/home/swtbuild/mozilla1.7.13/mozilla/dist/sdk"
		fi
		if [ "${MOZILLA_INCLUDES}" = "" ]; then
			export MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/include/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
		fi
		if [ "${MOZILLA_LIBS}" = "" ]; then
			export MOZILLA_LIBS="-L${MOZILLA_SDK}/lib -L${MOZILLA_SDK}/bin -lxpcom -lnspr4 -lplds4 -lplc4"
		fi
		if [ "${XULRUNNER_SDK}" = "" ]; then
			export XULRUNNER_SDK="/home/swtbuild/xulrunner-1.8.0.1/mozilla/dist/sdk"
		fi
		if [ "${XULRUNNER_INCLUDES}" = "" ]; then
			export XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
		fi
		if [ "${XULRUNNER_LIBS}" = "" ]; then
			export XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/usr/local/cairo64/lib/pkgconfig"
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
	"hpux.ia64_32")
		export PATH="/opt/hp-gcc/bin:/opt/gtk2.6/bin:/opt/${PATH}"
		if [ "${CC}" = "" ]; then
			export CC=gcc
		fi
		if [ "${JAVA_HOME}" = "" ]; then
			export JAVA_HOME="/opt/java1.5"
		fi
		if [ "${AWT_LIB_PATH}" = "" ]; then
			export AWT_LIB_PATH="/opt/java1.5/jre/lib/IA64N/"
		fi
		if [ "${PKG_CONFIG_PATH}" = "" ]; then
			export PKG_CONFIG_PATH="/opt/gtk2.6/lib/pkgconfig"
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
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64' -o ${MODEL} = 'ia64' -o ${MODEL} = 'sparc64'  -o ${MODEL} = 's390x' ]; then
	SWT_PTR_CFLAGS=-DJNI64
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
	if [ ${MODEL} = 'ppc64' ]; then
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
	if [ ${MODEL} = 'sparc64' ]; then
			SWT_PTR_CFLAGS="${SWT_PTR_CFLAGS} -xarch=v9"
			SWT_LFLAGS="-xarch=v9"
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

if [ x`pkg-config --exists gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0 && echo YES` = "xYES"  -a ${MODEL} != "sparc64" -a ${MODEL} != 'ia64' ]; then
	echo "libgnomeui-2.0 found, compiling SWT program support using GNOME"
	MAKE_GNOME=make_gnome
else
	echo "libgnome-2.0 and libgnomeui-2.0 not found:"
	echo "    *** SWT Program support for GNOME will not be compiled."
fi

if [ x`pkg-config --exists cairo && echo YES` = "xYES" ]; then
	echo "Cairo found, compiling SWT support for the cairo graphics library."
	MAKE_CAIRO=make_cairo
else
	echo "Cairo not found:"
	echo "    *** Advanced graphics support using cairo will not be compiled."
fi

if [ -z "${MOZILLA_INCLUDES}" -a -z "${MOZILLA_LIBS}" -a ${MODEL} != 'sparc64' ]; then
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
		echo "None of the following libraries were found:  Mozilla/XPCOM, Firefox/XPCOM, or XULRunner/XPCOM"
		echo "    *** Mozilla embedding support will not be compiled."
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
	echo "libjawt.so found, the SWT/AWT integration library will be compiled."
	MAKE_AWT=make_awt
else
	echo "libjawt.so not found, the SWT/AWT integration library will not be compiled."
fi

# Announce our target
echo "Building SWT/GTK+ for $SWT_OS $SWT_ARCH"
if [ "x${OUTPUT_DIR}" = "x" ]; then
	OUTPUT_DIR=../../../org.eclipse.swt.gtk.${SWT_OS}.${SWT_ARCH}
	export OUTPUT_DIR
fi

if [ "x${1}" = "xclean" ]; then
	${MAKE_TYPE} -f $MAKEFILE clean
else
	${MAKE_TYPE} -f $MAKEFILE all $MAKE_GNOME $MAKE_CAIRO $MAKE_AWT $MAKE_MOZILLA ${1} ${2} ${3} ${4} ${5} ${6} ${7} ${8} ${9}
fi
