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

CC=gcc
LD=gcc
MOZILLA_HOME=/mozilla/mozilla/1.6/linux_gtk2/mozilla/dist
MODEL=`uname -m`

case $MODEL in
	"x86_64")
		JAVA_HOME=/bluebird/teamswt/swt-builddir/jdk1.5.0
		AWT_LIB_PATH=$JAVA_HOME/jre/lib/amd64
		XTEST_LIB_PATH=/usr/X11R6/lib64
		SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64
		;;
	*)
		JAVA_HOME=/bluebird/teamswt/swt-builddir/IBMJava2-141
		AWT_LIB_PATH=$JAVA_HOME/jre/bin
		XTEST_LIB_PATH=/usr/X11R6/lib
		;;
esac

export CC LD JAVA_HOME AWT_LIB_PATH XTEST_LIB_PATH MOZILLA_HOME SWT_PTR_CFLAGS

make -f make_gtk.mak ${1} ${2} ${3} ${4}
