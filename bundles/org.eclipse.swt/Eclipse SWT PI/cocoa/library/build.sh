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

sw_vers -productVersion

cd `dirname $0`

if [ "x${MODEL}" = "xx86_64" ]; then
	export ARCHS="-arch x86_64"
	if [ "x${OUTPUT_DIR}" = "x" ]; then
		export OUTPUT_DIR=../../../org.eclipse.swt.cocoa.macosx.x86_64
	fi
elif [ "x${MODEL}" = "xarm64" ] || [ "x${MODEL}" = "xaarch64" ]; then
	export ARCHS="-arch arm64"
	if [ "x${OUTPUT_DIR}" = "x" ]; then
		export OUTPUT_DIR=../../../org.eclipse.swt.cocoa.macosx.aarch64
	fi
fi

#TODO: increment this?
export MACOSX_DEPLOYMENT_TARGET=10.15
export CLANG_ENABLE_OBJC_WEAK = YES
export CLANG_WARN_UNGUARDED_AVAILABILITY = YES
export GCC_TREAT_WARNINGS_AS_ERRORS = YES


make -f make_macosx.mak $1 $2 $3 $4 $5 $6 $7 $8 $9