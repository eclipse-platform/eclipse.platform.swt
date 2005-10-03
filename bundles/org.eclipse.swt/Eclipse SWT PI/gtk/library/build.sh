#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2005 IBM Corporation and others.
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

if [ "${CC}" = "" ]; then
	CC=gcc
fi

if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
if [ "${MODEL}" = "" ]; then
	MODEL=`uname -p`
fi

# Fix the pointer size for AMD64
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64' -o ${MODEL} = 'ia64' ]; then
	SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64
	export SWT_PTR_CFLAGS
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
fi

case $OS in
	"Linux")
		case $MODEL in
			"x86_64")
				if [ "${GECKO_SDK}" = "" ]; then
					GECKO_SDK=/mozilla/mozilla/1.7/amd64/gtk2/mozilla/dist/sdk
					GECKO_INCLUDES="-include ${GECKO_SDK}/include/mozilla-config.h -I${GECKO_SDK}/include"
					GECKO_LIBS="-L${GECKO_SDK}/lib -L${GECKO_SDK}/bin -lembed_base_s -lxpcomglue_s -lxpcom -lnspr4 -lplds4 -lplc4"
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.gtk.linux.x86_64
				makefile="make_linux.mak"
				echo "Building Linux GTK AMD64 version of SWT"
				;;
			i?86)
				if [ "${GECKO_SDK}" = "" ]; then
					GECKO_SDK=/mozilla/mozilla/1.4/linux_gtk2/mozilla/dist/sdk
					GECKO_INCLUDES="-include ${GECKO_SDK}/mozilla-config.h -I${GECKO_SDK}/nspr/include -I${GECKO_SDK}/xpcom/include -I${GECKO_SDK}/string/include -I${GECKO_SDK}/embed_base/include -I${GECKO_SDK}/embedstring/include"
					GECKO_LIBS="-L${GECKO_SDK}/embedstring/bin -lembedstring -L${GECKO_SDK}/embed_base/bin -lembed_base_s -L${GECKO_SDK}/xpcom/bin -lxpcomglue_s -lxpcom -L${GECKO_SDK}/nspr/bin -lnspr4 -lplds4 -lplc4"
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.gtk.linux.x86
				makefile="make_linux.mak"
				echo "Building Linux GTK x86 version of SWT"
				;;
			"ppc")
				OUTPUT_DIR=../../../org.eclipse.swt.gtk.linux.ppc
				makefile="make_linux.mak"
				echo "Building Linux GTK PowerPC 32 bit version of SWT"
				;;
			"ppc64")
				if [ "${JAVA_HOME}" = "" ]; then
					echo "JAVA_HOME is missing. Build will probably fail."
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.gtk.linux.ppc64
				makefile="make_linux.mak"
				;;
			"ia64")
				if [ "${JAVA_HOME}" = "" ]; then
					echo "JAVA_HOME is missing. Build will probably fail."
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.gtk.linux.ia64
				makefile="make_linux.mak"
				;;
			*)
				echo "*** Unknown MODEL <${MODEL}>"
				;;	
		esac
		;;
	"SunOS")
		if [ "${JAVA_HOME}" = "" ]; then
			JAVA_HOME=/usr/j2se
		fi
		if [ "${CDE_HOME}" = "" ]; then
			CDE_HOME=/usr/dt
		fi
		PATH=/usr/ccs/bin:/usr/local/bin:$PATH
		export PATH
		OUTPUT_DIR=../../../org.eclipse.swt.gtk.solaris.sparc
		makefile="make_solaris.mak"
		echo "Building Solaris GTK version of SWT"
		;;
	*)

	echo "*** Unknown OS <${OS}>"
	;;
esac

# Find AWT if available
case $MODEL in
	"x86_64") AWT_ARCH=amd64 ;;
	i?86) AWT_ARCH=i386 ;;
	*) AWT_ARCH=$MODEL ;;
esac
if [ -d ${JAVA_HOME}/jre/lib/${AWT_ARCH} ]; then
	AWT_LIB_PATH=${JAVA_HOME}/jre/lib/${AWT_ARCH}
	export AWT_LIB_PATH
else
	AWT_LIB_PATH=${JAVA_HOME}/jre/bin
	export AWT_LIB_PATH
fi

export CC LD JAVA_HOME AWT_LIB_PATH GECKO_SDK GECKO_INCLUDES GECKO_LIBS CDE_HOME OUTPUT_DIR

make -f $makefile ${1} ${2} ${3} ${4} ${5} ${6} ${7} ${8} ${9}
