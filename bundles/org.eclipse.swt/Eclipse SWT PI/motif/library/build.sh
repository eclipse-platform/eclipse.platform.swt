#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2006 IBM Corporation and others.
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
				JAVA_HOME=/bluebird/teamswt/swt-builddir/aixj9
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
				JAVA_HOME=/bluebird/teamswt/swt-builddir/IBMJava2-141
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/bluebird/teamswt/swt-builddir/motif21
			fi
			if [ "${GECKO_SDK}" = "" ]; then
				GECKO_SDK=/bluebird/teamswt/swt-builddir/mozilla/1.4/linux_gtk2/mozilla/dist/sdk
				GECKO_INCLUDES="-include ${GECKO_SDK}/mozilla-config.h -I${GECKO_SDK}/../include/xpcom -I${GECKO_SDK}/../include/nspr -I${GECKO_SDK}/../include/embed_base -I${GECKO_SDK}/../include/embedstring -I${GECKO_SDK}/../include/string -I${GECKO_SDK}/../include/profdirserviceprovider"
				GECKO_LIBS="${GECKO_SDK}/../lib/libembed_base_s.a ${GECKO_SDK}/../lib/libprofdirserviceprovider_s.a ${GECKO_SDK}/../lib/libembedstring.a ${GECKO_SDK}/../lib/libxpcomglue_s.a -L${GECKO_SDK}/../bin -L${GECKO_SDK}/../lib/ -lxpcom -lnspr4 -lplds4 -lplc4"
				PROFILE14_INCLUDES="-include ${GECKO_SDK}/mozilla-config.h -I${GECKO_SDK}/../include/xpcom -I${GECKO_SDK}/../include/nspr -I${GECKO_SDK}/../include/string -I${GECKO_SDK}/../include/profdirserviceprovider"
				PROFILE14_LIBS="${GECKO_SDK}/../lib/libembed_base_s.a ${GECKO_SDK}/../lib/libprofdirserviceprovider_s.a -L${GECKO_SDK}/../lib/ -lxpcom"
			fi
			if [ "${GECKO17_SDK}" = "" ]; then
				GECKO17_SDK=/bluebird/teamswt/swt-builddir/mozilla/1.7/linux_gtk2/mozilla/dist/sdk
				PROFILE17_INCLUDES="-include ${GECKO17_SDK}/include/mozilla-config.h -I${GECKO17_SDK}/../include/xpcom -I${GECKO17_SDK}/../include/nspr -I${GECKO17_SDK}/../include/string -I${GECKO17_SDK}/../include/profdirserviceprovider"
				PROFILE17_LIBS="${GECKO17_SDK}/../lib/libembed_base_s.a ${GECKO17_SDK}/../lib/libprofdirserviceprovider_s.a -L${GECKO17_SDK}/../lib/ -lxpcom"
			fi
			if [ "${GECKO18_SDK}" = "" ]; then
				GECKO18_SDK=/bluebird/teamswt/swt-builddir/mozilla/1.8/linux_gtk2/mozilla/dist/sdk
				PROFILE18_INCLUDES="-include ${GECKO18_SDK}/include/mozilla-config.h -I${GECKO18_SDK}/../include/xpcom -I${GECKO18_SDK}/../include/nspr -I${GECKO18_SDK}/../include/string -I${GECKO18_SDK}/../include/profdirserviceprovider"
				PROFILE18_LIBS="${GECKO18_SDK}/../lib/libembed_base_s.a ${GECKO18_SDK}/../lib/libprofdirserviceprovider_s.a -L${GECKO18_SDK}/../lib/ -lxpcom"
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
					JAVA_HOME=/opt/jdk14101
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
					JAVA_HOME=/opt/jdk14101
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

export JAVA_HOME MOTIF_HOME CDE_HOME GECKO_SDK GECKO17_SDK GECKO18_SDK GECKO_INCLUDES GECKO_LIBS OUTPUT_DIR PROFILE14_INCLUDES PROFILE17_INCLUDES PROFILE18_INCLUDES PROFILE14_LIBS PROFILE17_LIBS PROFILE18_LIBS

make -f $makefile $1 $2 $3 $4
