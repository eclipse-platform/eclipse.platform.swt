#*******************************************************************************
# Copyright (c) 2000, 2012 IBM Corporation and others.
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

# Makefile for SWT libraries on Cocoa/Mac

# assumes these variables are set in the environment from which make is run
#	SWT_JAVA_HOME
#	OUTPUT_DIR
#	AWT_LIB_PATH (only if build jawt shall be build)

include make_common.mak

SWT_PREFIX=swt
SWTPI_PREFIX=swt-pi
WS_PREFIX=cocoa
SWT_VERSION=$(maj_ver)$(min_ver)r$(rev)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

AWT_PREFIX = swt-awt
AWT_LIB    = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
AWT_OBJECTS   = swt_awt.o

#SWT_DEBUG = -g
CFLAGS = -c -xobjective-c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA -DATOMIC \
	-I $(SWT_JAVA_HOME)/include \
	-I $(SWT_JAVA_HOME)/include/darwin \
	-I /System/Library/Frameworks/Cocoa.framework/Headers \
	-I /System/Library/Frameworks/JavaScriptCore.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework Cocoa -framework WebKit -framework CoreServices -framework JavaScriptCore -framework Security -framework SecurityInterface
SWT_OBJECTS = swt.o c.o c_stats.o callback.o
SWTPI_OBJECTS = swt.o os.o os_structs.o os_stats.o os_custom.o

all: $(SWT_LIB) $(SWTPI_LIB) $(AWT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(AWT_LIB): $(AWT_OBJECTS)
	cc -o $(AWT_LIB) $(LFLAGS) $(AWT_OBJECTS)

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)

install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
