#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

cd `dirname $0`

OUTPUT_DIR=../../../org.eclipse.swt.carbon.macosx

export OUTPUT_DIR

export MACOSX_DEPLOYMENT_TARGET=10.3

make -f make_macosx.mak $1 $2 $3 $4 $5 $6 $7 $8 $9
