#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#*******************************************************************************

cd `dirname $0`

if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
if [ "${MODEL}" = "" ]; then
	MODEL=`uname -m`
fi
QT_HOME=$QTDIR

case $OS in
	"Linux")
		case $MODEL in
			"x86_64")
				CC=gcc
				LD=gcc
				if [ "${JAVA_HOME}" = "" ]; then
					JAVA_HOME=/bluebird/teamswt/swt-builddir/jdk1.5.0
				fi
				AWT_LIB_PATH=$JAVA_HOME/jre/lib/amd64
				XTEST_LIB_PATH=/usr/X11R6/lib64
				KDE_LIB_PATH=/usr/lib64
				KDE_INCLUDE_PATH=/usr/include/kde
				SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64
				OUTPUT_DIR=../../../org.eclipse.swt.gtk64/os/linux/amd64
				makefile="make_linux.mak"
				echo "Building Linux GTK AMD64 version of SWT"
				;;
			i?86)
				CC=gcc
				LD=gcc
				if [ "${JAVA_HOME}" = "" ]; then
					JAVA_HOME=/bluebird/teamswt/swt-builddir/IBMJava2-141
				fi
				AWT_LIB_PATH=$JAVA_HOME/jre/bin
				XTEST_LIB_PATH=/usr/X11R6/lib
				KDE_LIB_PATH=/usr/lib
				KDE_INCLUDE_PATH=/usr/include/kde
				if [ "${GECKO_SDK}" = "" ]; then
					GECKO_SDK=/mozilla/mozilla/1.4/linux_gtk2/mozilla/dist/sdk
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.gtk/os/linux/x86
				makefile="make_linux.mak"
				echo "Building Linux GTK x86 version of SWT"
				;;
			"ppc")
				CC=gcc
				LD=gcc
				if [ "${JAVA_HOME}" = "" ]; then
					echo "JAVA_HOME is missing. Build will probably fail.
				fi
				AWT_LIB_PATH=$JAVA_HOME/jre/bin
				XTEST_LIB_PATH=/usr/X11R6/lib
				KDE_LIB_PATH=/opt/kde3/lib
				KDE_INCLUDE_PATH=/opt/kde3/include
				OUTPUT_DIR=../../../org.eclipse.swt.gtk64/os/linux/ppc
				;;
			"ppc64")
				CC=gcc
				LD=gcc
				if [ "${JAVA_HOME}" = "" ]; then
					echo "JAVA_HOME is missing. Build will probably fail.
				fi
				AWT_LIB_PATH=$JAVA_HOME/jre/bin
				XTEST_LIB_PATH=/usr/X11R6/lib
				KDE_LIB_PATH=/opt/kde3/lib
				KDE_INCLUDE_PATH=/opt/kde3/include
				SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64
				OUTPUT_DIR=../../../org.eclipse.swt.gtk64/os/linux/ppc64
				;;
			*)
				echo "*** Unknown MODEL <${MODEL}>"
				;;	
		esac
		;;
	"SunOS")
		CC=gcc
		LD=gcc
		if [ "${JAVA_HOME}" = "" ]; then
			JAVA_HOME=/usr/j2se
		fi
		AWT_LIB_PATH=$JAVA_HOME/jre/lib/sparc
		PATH=/usr/ccs/bin:/usr/local/bin:$PATH
		export PATH
		XTEST_LIB_PATH=/usr/openwin/lib
#		if [ "${GECKO_SDK}" = "" ]; then
#			GECKO_SDK=/mozilla/mozilla/1.4/linux_gtk2/mozilla/dist/sdk
#		fi
		OUTPUT_DIR=../../../org.eclipse.swt.gtk/os/solaris/sparc
		makefile="make_solaris.mak"
		echo "Building Solaris GTK version of SWT"
		;;
	*)

	echo "*** Unknown OS <${OS}>"
	;;
esac

export CC LD JAVA_HOME QT_HOME AWT_LIB_PATH XTEST_LIB_PATH GECKO_SDK SWT_PTR_CFLAGS KDE_LIB_PATH KDE_INCLUDE_PATH OUTPUT_DIR

make -f $makefile ${1} ${2} ${3} ${4}
