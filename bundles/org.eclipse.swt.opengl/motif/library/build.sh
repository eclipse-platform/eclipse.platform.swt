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
#     Sridhar Bidigalu (ICS)
#*******************************************************************************

cd `dirname $0`

# Determine the operating system being built
makefile=""
OS=`uname -s`
MODEL=`uname -m`

case $OS in
	"AIX")
		JAVA_HOME=/bluebird/teamswt/swt-builddir/aixj9
		MOTIF_HOME=/usr
		CDE_HOME=/usr/dt
		makefile="make_aix.mak"
		echo "Building AIX version of SWT"
		;;
	"Linux")
		JAVA_HOME=/bluebird/teamswt/swt-builddir/IBMJava2-141
		MOTIF_HOME=/bluebird/teamswt/swt-builddir/motif21
		QT_HOME=$QTDIR
		makefile="make_linux.mak"
		echo "Building Linux version of SWT"
		;;
	"SunOS")
		JAVA_HOME=/usr/java
		MOTIF_HOME=/usr/dt
		CDE_HOME=/usr/dt
		PATH=/opt/SUNWspro/bin:$PATH
		export PATH		
		makefile="make_solaris.mak"
		echo "Building Solaris version of SWT"
		;;
	"HP-UX")
		JAVA_HOME=/opt/jdk14101
		MOTIF_HOME=/usr
		CDE_HOME=/usr/dt
		case $MODEL in
			"ia64")
				makefile="make_hpux_ia64.mak"
				echo "Building HPUX ia64 version of SWT"
				;;
			*)
				makefile="make_hpux_PA_RISC.mak"
				echo "Building HPUX risc version of SWT"
				;;
		esac
		;;

	*)
		echo "Unknown OS -- build aborted"
		;;
esac

export JAVA_HOME MOTIF_HOME CDE_HOME QT_HOME

make -f $makefile $1 $2 $3 $4