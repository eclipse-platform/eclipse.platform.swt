#*******************************************************************************
# Copyright (c) 2000, 2012 IBM Corporation and others.
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
SWTXULRUNNER_PREFIX=swt-xulrunner
SWTXULRUNNER24_PREFIX=swt-xulrunner24
WS_PREFIX=cocoa
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
XULRUNNER_LIB=lib$(SWTXULRUNNER_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
XULRUNNER24_LIB=lib$(SWTXULRUNNER24_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

AWT_PREFIX = swt-awt
AWT_LIB    = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
AWT_LIBS   = /System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Libraries/libjawt.dylib"
AWT_OBJECTS   = swt_awt.o

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
CFLAGS = -c -xobjective-c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA -DATOMIC \
	-I /System/Library/Frameworks/JavaVM.framework/Headers \
	-I /System/Library/Frameworks/Cocoa.framework/Headers \
	-I /System/Library/Frameworks/JavaScriptCore.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Cocoa -framework WebKit -framework CoreServices -framework JavaScriptCore -framework Security -framework SecurityInterface
SWT_OBJECTS = swt.o c.o c_stats.o callback.o
SWTPI_OBJECTS = swt.o os.o os_structs.o os_stats.o os_custom.o
XULRUNNER_OBJECTS = swt.o xpcom.o xpcom_custom.o xpcom_structs.o xpcom_stats.o xpcominit.o xpcominit_structs.o xpcominit_stats.o
XULRUNNER24_OBJECTS = xpcom24_custom.o

XULRUNNERCFLAGS = -c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA -I /System/Library/Frameworks/JavaVM.framework/Headers \
	-Wno-non-virtual-dtor -include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include
XULRUNNER24CFLAGS = -c -Wall $(XULRUNNER24_ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA -I /System/Library/Frameworks/JavaVM.framework/Headers -Wno-non-virtual-dtor
XULRUNNERLFLAGS = $(LFLAGS)
XULRUNNER24LFLAGS = -bundle $(XULRUNNER24_ARCHS) -framework JavaVM

all: $(SWT_LIB) $(SWTPI_LIB) $(AWT_LIB) $(XULRUNNER_LIB) $(XULRUNNER24_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(AWT_LIB): $(AWT_OBJECTS)
	cc -o $(AWT_LIB) $(LFLAGS) $(AWT_OBJECTS)

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)

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

$(XULRUNNER24_LIB): $(XULRUNNER24_OBJECTS)
	g++ -o $(XULRUNNER24_LIB) $(XULRUNNER24LFLAGS) $(XULRUNNER24_LIBS) $(XULRUNNER24_OBJECTS)

xpcom24_custom.o: xpcom24_custom.cpp
	g++ $(XULRUNNER24CFLAGS) xpcom24_custom.cpp

install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
