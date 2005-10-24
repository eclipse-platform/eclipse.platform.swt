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
		MAKEFILE=make_solaris.mak
		;;
	*)
		SWT_OS=`uname -s | tr -s '[:upper:]' '[:lower:]'`
		MAKEFILE=make_linux.mak
		;;
esac

# Determine which CPU type we are building for
if [ "${MODEL}" = "" ]; then
	if uname -p > /dev/null 2>&1; then
		MODEL=`uname -p`
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
if [ ${MODEL} = 'x86_64' -o ${MODEL} = 'ppc64' -o ${MODEL} = 'ia64' ]; then
	SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64
	export SWT_PTR_CFLAGS
	if [ -d /lib64 ]; then
		XLIB64=-L/usr/X11R6/lib64
		export XLIB64
	fi
fi

# Find AWT if available
if [ "${AWT_LIB_PATH}" = "" ]; then
	if [ -d ${JAVA_HOME}/jre/lib/${AWT_ARCH} ]; then
		AWT_LIB_PATH=${JAVA_HOME}/jre/lib/${AWT_ARCH}
		export AWT_LIB_PATH
	else
		AWT_LIB_PATH=${JAVA_HOME}/jre/bin
		export AWT_LIB_PATH
	fi
fi

# Announce our target
echo "Building SWT/GTK+ for $SWT_OS $SWT_ARCH"
OUTPUT_DIR=../../../org.eclipse.swt.gtk.${SWT_OS}.${SWT_ARCH}
export OUTPUT_DIR

make -f $MAKEFILE ${1} ${2} ${3} ${4} ${5} ${6} ${7} ${8} ${9}
