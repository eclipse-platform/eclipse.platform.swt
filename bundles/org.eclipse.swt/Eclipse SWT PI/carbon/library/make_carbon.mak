#**********************************************************************
# Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
# This file is made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
#**********************************************************************
#
# Makefile for SWT libraries on Carbon/Mac

include make_common.mak

SWT_PREFIX=swt
WS_PREFIX=carbon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

DEBUG =  
CFLAGS = -c -DSWT_VERSION=$(SWT_VERSION) $(DEBUG) -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Headers
LFLAGS = -bundle -framework JavaVM -framework Carbon 

SWT_OBJS = swt.o structs.o callback.o

all: $(SWT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJS)
	cc -o $(SWT_LIB)  $(LFLAGS) $(SWT_OBJS)

clean:
	rm -f *.jnilib *.o