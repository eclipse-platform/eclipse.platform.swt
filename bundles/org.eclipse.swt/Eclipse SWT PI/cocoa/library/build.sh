#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2011 IBM Corporation and others.
#
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
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
	if [ -d "chromium_subp/cef_macosx" ]; then
		export CHROMIUM_HEADERS=./chromium_subp/cef_macosx
	else
		export CHROMIUM_HEADERS=$CHROMIUM_OUTPUT_DIR/../../../../eclipse.platform.swt/bundles/org.eclipse.swt.browser.chromium/common/rust-library/chromium_subp/cef_macosx
	fi
elif [ "x${MODEL}" = "xarm64" ] || [ "x${MODEL}" = "xaarch64" ]; then
	export ARCHS="-arch arm64"
	if [ "x${OUTPUT_DIR}" = "x" ]; then
		export OUTPUT_DIR=../../../org.eclipse.swt.cocoa.macosx.aarch64
	fi
fi

export MACOSX_DEPLOYMENT_TARGET=10.10

make -f make_macosx.mak $1 $2 $3 $4 $5 $6 $7 $8 $9