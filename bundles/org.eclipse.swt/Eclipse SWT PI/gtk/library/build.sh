#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2008 IBM Corporation and others.
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

if [ "${JAVA_HOME}" = "" ]; then
	echo "Please set JAVA_HOME to point at a JRE."
fi
if [ "${CC}" = "" ]; then
	CC=gcc
	export CC
fi

# Determine which OS we are on
if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
case $OS in
	"SunOS")
		SWT_OS=solaris
		PROC=`uname -i`
		MAKEFILE=make_solaris.mak
		if uname -p > /dev/null 2>&1; then
			MODEL=`uname -p`
		fi
		if [ "${MODEL}" = "i386"]; then
			MAKEFILE=make_solaris_x86.mak
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

# For 64-bit CPUs, we have a switch
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64' -o ${MODEL} = 'ia64' -o ${MODEL} = 's390x' ]; then
	SWT_PTR_CFLAGS=-DJNI64
	export SWT_PTR_CFLAGS
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
fi

if [ x`pkg-config --exists gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0 && echo YES` = "xYES" ]; then
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

if [ -z "${MOZILLA_INCLUDES}" -a -z "${MOZILLA_LIBS}" ]; then
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
	if [ -d ${JAVA_HOME}/jre/lib/${AWT_ARCH} ]; then
		AWT_LIB_PATH=${JAVA_HOME}/jre/lib/${AWT_ARCH}
		export AWT_LIB_PATH
	else
		AWT_LIB_PATH=${JAVA_HOME}/jre/bin
		export AWT_LIB_PATH
	fi
fi

if [ -f ${AWT_LIB_PATH}/libjawt.so ]; then
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
	make -f $MAKEFILE clean
else
	make -f $MAKEFILE all $MAKE_GNOME $MAKE_CAIRO $MAKE_AWT $MAKE_MOZILLA ${1} ${2} ${3} ${4} ${5} ${6} ${7} ${8} ${9}
fi
