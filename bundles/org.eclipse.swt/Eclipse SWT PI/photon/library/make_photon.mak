#*******************************************************************************
# Copyright (c) 2000, 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Photon

include make_common.mak

#assumes IVE_HOME is set in the environment

SWT_PREFIX=swt
WS_PREFIX=photon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so

DEBUG =  
CFLAGS = -c -shared -O2 -DSWT_VERSION=$(SWT_VERSION) -w8 $(DEBUG) -DPHOTON -I$(IVE_HOME)/include
LFLAGS = -shared -lph -lphrender -lPtWeb

SWT_OBJS = os.o os_structs.o os_custom.o callback.o

all: $(SWT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJS)
	cc -o $(SWT_LIB)  $(LFLAGS) $(SWT_OBJS)

clean:
	rm -f *.so *.o