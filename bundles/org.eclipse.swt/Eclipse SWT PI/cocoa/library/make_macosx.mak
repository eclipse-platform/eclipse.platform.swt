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

# Makefile for SWT libraries on Cocoa/Mac

include make_common.mak

SWT_PREFIX=swt
SWTPI_PREFIX=swt-pi
WS_PREFIX=cocoa
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
ARCHS = #-arch i386 -arch ppc
CFLAGS = -c -xobjective-c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA \
	-I /System/Library/Frameworks/JavaVM.framework/Headers \
	-I /System/Library/Frameworks/Cocoa.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Cocoa 
SWT_OBJECTS = swt.o c.o c_stats.o callback.o
SWTPI_OBJECTS = swt.o os.o os_structs.o os_stats.o


all: $(SWT_LIB) $(SWTPI_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)


install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
