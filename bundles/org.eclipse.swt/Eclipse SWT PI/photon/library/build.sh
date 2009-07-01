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
#*******************************************************************************

cd `dirname $0`

OUTPUT_DIR=../../../org.eclipse.swt.photon.qnx.x86
IVE_HOME=~/ive/bin

export IVE_HOME OUTPUT_DIR

make -f make_qnx.mak $1 $2 $3 $4