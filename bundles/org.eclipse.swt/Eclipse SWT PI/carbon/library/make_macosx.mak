#*******************************************************************************
# Copyright (c) 2000, 2006 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Carbon/Mac

include make_common.mak

SWT_PREFIX=swt
SWTPI_PREFIX=swt-pi
SWTCOCOA_PREFIX=swt-cocoa
SWTAGL_PREFIX=swt-agl
WS_PREFIX=carbon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
COCOA_LIB=lib$(SWTCOCOA_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
AGL_LIB=lib$(SWTAGL_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
ARCHS = -arch i386 -arch ppc
CFLAGS = -c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Carbon 
COCOACFLAGS = $(CFLAGS) -xobjective-c -I /System/Library/Frameworks/Cocoa.framework/Headers -I /System/Library/Frameworks/WebKit.framework/Headers
COCOALFLAGS = $(LFLAGS) -framework WebKit -framework Cocoa
AGLLFLAGS = $(LFLAGS) -framework OpenGL -framework AGL
SWT_OBJECTS = swt.o callback.o
SWTPI_OBJECTS = swt.o os.o os_custom.o os_structs.o os_stats.o
COCOA_OBJECTS = swt.o cocoa.o cocoa_custom.o cocoa_structs.o cocoa_stats.o 
AGL_OBJECTS = swt.o agl.o agl_stats.o

all: $(SWT_LIB) $(SWTPI_LIB) $(COCOA_LIB) $(AGL_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)

cocoa.o: cocoa.c
	cc $(COCOACFLAGS) cocoa.c
	
cocoa_custom.o: cocoa_custom.c
	cc $(COCOACFLAGS) cocoa_custom.c
	
cocoa_structs.o: cocoa_structs.c
	cc $(COCOACFLAGS) cocoa_structs.c
		
$(COCOA_LIB): $(COCOA_OBJECTS)
	cc -o $(COCOA_LIB) $(COCOALFLAGS) $(COCOA_OBJECTS)

$(AGL_LIB): $(AGL_OBJECTS)
	cc -o $(AGL_LIB) $(AGLLFLAGS) $(AGL_OBJECTS)

install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
