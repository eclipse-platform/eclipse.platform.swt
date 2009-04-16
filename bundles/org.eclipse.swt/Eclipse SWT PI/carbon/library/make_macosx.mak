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

# Makefile for SWT libraries on Carbon/Mac

include make_common.mak

SWT_PREFIX=swt
SWTPI_PREFIX=swt-pi
SWTCOCOA_PREFIX=swt-cocoa
SWTAGL_PREFIX=swt-agl
SWTXULRUNNER_PREFIX=swt-xulrunner
WS_PREFIX=carbon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
COCOA_LIB=lib$(SWTCOCOA_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
AGL_LIB=lib$(SWTAGL_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
XULRUNNER_LIB=lib$(SWTXULRUNNER_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
ARCHS = -arch i386 -arch ppc
CFLAGS = -c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Carbon 
COCOACFLAGS = $(CFLAGS) -xobjective-c -I /System/Library/Frameworks/Cocoa.framework/Headers -I /System/Library/Frameworks/WebKit.framework/Headers
COCOALFLAGS = $(LFLAGS) -framework WebKit -framework Cocoa
AGLLFLAGS = $(LFLAGS) -framework OpenGL -framework AGL
SWT_OBJECTS = swt.o c.o c_stats.o callback.o
SWTPI_OBJECTS = swt.o os.o os_custom.o os_structs.o os_stats.o
COCOA_OBJECTS = swt.o cocoa.o cocoa_custom.o cocoa_structs.o cocoa_stats.o 
AGL_OBJECTS = swt.o agl.o agl_stats.o
XULRUNNER_OBJECTS = swt.o xpcom.o xpcom_custom.o xpcom_structs.o xpcom_stats.o xpcominit.o xpcominit_structs.o xpcom_stats.o

XULRUNNER_SDK = /Users/Shared/xulrunner/1.8.0.1/mozilla/dist/i386/dist/sdk
#XULRUNNER_SDK = /Users/Shared/gecko-sdk
#XULRUNNER_LIBS = -L${XULRUNNER_SDK}/lib -lxpcomglue
XULRUNNER_LIBS = $(XULRUNNER_SDK)/lib/libxpcomglue.a $(XULRUNNER_SDK)/../../../ppc/dist/sdk/lib/libxpcomglue.a
XULRUNNERCFLAGS = $(CFLAGS) -Wno-non-virtual-dtor -include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include -DXULRUNNER
XULRUNNERLFLAGS = $(LFLAGS)

all: $(SWT_LIB) $(SWTPI_LIB) $(COCOA_LIB) #$(AGL_LIB) $(XULRUNNER_LIB)

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

$(XULRUNNER_LIB): $(XULRUNNER_OBJECTS)
	g++ -o $(XULRUNNER_LIB) $(XULRUNNERLFLAGS) $(XULRUNNER_LIBS) $(XULRUNNER_OBJECTS)

xpcom.o: xpcom.cpp
	g++ $(XULRUNNERCFLAGS) xpcom.cpp
xpcom_custom.o: xpcom_custom.cpp
	g++ $(XULRUNNERCFLAGS) xpcom_custom.cpp
xpcom_structs.o: xpcom_structs.cpp
	g++ $(XULRUNNERCFLAGS) xpcom_structs.cpp
xpcom_stats.o: xpcom_stats.cpp
	g++ $(XULRUNNERCFLAGS) xpcom_stats.cpp
xpcominit.o: xpcominit.cpp
	g++ $(XULRUNNERCFLAGS) xpcominit.cpp
xpcominit_structs.o: xpcominit_structs.cpp
	g++ $(XULRUNNERCFLAGS) xpcominit_structs.cpp
xpcominit_stats.o: xpcominit_stats.cpp
	g++ $(XULRUNNERCFLAGS) xpcominit_stats.cpp


install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
