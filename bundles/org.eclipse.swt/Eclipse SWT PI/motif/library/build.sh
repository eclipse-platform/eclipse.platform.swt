#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2009 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#     Sridhar Bidigalu (ICS)
#     Sumit Sarkar (Hewlett-Packard)
#*******************************************************************************

cd `dirname $0`

if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
if [ "${MODEL}" = "" ]; then
	MODEL=`uname -m`
fi

case $OS in
	"AIX")
		case $MODEL in		
		*) 
			if [ "${JAVA_HOME}" = "" ]; then
				echo "Please set JAVA_HOME to point at a JRE."
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/usr
			fi
			if [ "${CDE_HOME}" = "" ]; then
				CDE_HOME=/usr/dt
			fi
			OUTPUT_DIR=../../../org.eclipse.swt.motif.aix.ppc
			makefile="make_aix.mak"
			echo "Building AIX motif ppc version of SWT"
		esac
		;;
	"Linux")
		case $MODEL in
		*)
			if [ "${JAVA_HOME}" = "" ]; then
				echo "Please set JAVA_HOME to point at a JRE."
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				echo "Please set MOTIF_HOME to point at a Motif dev path."
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
			OUTPUT_DIR=../../../org.eclipse.swt.motif.linux.x86
			makefile="make_linux.mak"
			echo "Building Linux motif x86 version of SWT"
		esac
		;;
	"SunOS")
		case $MODEL in
		*) 
			if [ "${JAVA_HOME}" = "" ]; then
				JAVA_HOME=/usr/java
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/usr/dt
			fi
			if [ "${CDE_HOME}" = "" ]; then
				CDE_HOME=/usr/dt		
			fi
			OUTPUT_DIR=../../../org.eclipse.swt.motif.solaris.sparc
			PATH=/usr/ccs/bin:/opt/SUNWspro/bin:$PATH
			export PATH
			makefile="make_solaris.mak"
			echo "Building Solaris motif sparc version of SWT"
		esac
		;;
	"HP-UX")
		case $MODEL in
			"ia64")
				if [ "${JAVA_HOME}" = "" ]; then
					echo "Please set JAVA_HOME to point at a JRE."
				fi
				if [ "${MOTIF_HOME}" = "" ]; then
					MOTIF_HOME=/usr
				fi
				if [ "${CDE_HOME}" = "" ]; then
					CDE_HOME=/usr/dt
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.motif.hpux.ia64_32
				makefile="make_hpux_ia64_32.mak"
				echo "Building HPUX motif 32 bit ia64 version of SWT"
				;;
			*)
				if [ "${JAVA_HOME}" = "" ]; then
					echo "Please set JAVA_HOME to point at a JRE."
				fi
				if [ "${MOTIF_HOME}" = "" ]; then
					MOTIF_HOME=/usr
				fi
				if [ "${CDE_HOME}" = "" ]; then
					CDE_HOME=/usr/dt
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.motif.hpux.PA_RISC
				makefile="make_hpux_PA_RISC.mak"
				echo "Building HPUX motif risc version of SWT"
				;;
		esac
		;;

	*)
		echo "Unknown OS -- build aborted"
		;;
esac

export JAVA_HOME MOTIF_HOME CDE_HOME MOZILLA_SDK MOZILLA_INCLUDES MOZILLA_LIBS XULRUNNER_SDK XULRUNNER_INCLUDES XULRUNNER_LIBS OUTPUT_DIR

make -f $makefile $1 $2 $3 $4 $5 $6 $7 $8 $9
