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
#*******************************************************************************

cd `dirname $0`

if [ -d /System/Library/Frameworks/JavaVM.framework/Headers ]; then
	export CFLAGS_JAVA_VM="-I /System/Library/Frameworks/JavaVM.framework/Headers"
else
	export CFLAGS_JAVA_VM="-I $(/usr/libexec/java_home)/include -I $(/usr/libexec/java_home)/include/darwin"
fi

if [ "x${MODEL}" = "xx86_64" ]; then
	export ARCHS="-arch x86_64"
	if [ "x${OUTPUT_DIR}" = "x" ]; then
		export OUTPUT_DIR=../../../org.eclipse.swt.cocoa.macosx.x86_64
	fi
	if [ "x${XULRUNNER_SDK}" = "x" ]; then
		export XULRUNNER_SDK="/Users/Shared/xulrunner/64-bit/mozilla/dist"
	fi
	if [ "x${XULRUNNER_LIBS}" = "x" ]; then
		export XULRUNNER_LIBS="${XULRUNNER_SDK}/lib/libxpcomglue.a"
	fi
fi

export MACOSX_DEPLOYMENT_TARGET=10.7

make -f make_macosx.mak $1 $2 $3 $4 $5 $6 $7 $8 $9
