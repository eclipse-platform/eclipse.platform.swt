#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

#!/bin/sh

make -f make_gtk.mak SWT_PTR_CFLAGS=-DSWT_PTR_SIZE_64 ${1} ${2} ${3} ${4}
