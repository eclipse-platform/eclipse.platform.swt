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

# Makefile for SWT libraries on Carbon/Mac

include make_common.mak

SWT_PREFIX=swt
SWTPI_PREFIX=swt-pi
SWTWEBKIT_PREFIX=swt-webkit
WS_PREFIX=carbon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
SWTPI_LIB=lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
WEBKIT_LIB=lib$(SWTWEBKIT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
CFLAGS = -c -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Headers
LFLAGS = -bundle -framework JavaVM -framework Carbon 
WEBKITCFLAGS = -c -xobjective-c -I /System/Library/Frameworks/JavaVM.framework/Headers -I /System/Library/Frameworks/Cocoa.framework/Headers -I /System/Library/Frameworks/WebKit.framework/Headers
WEBKITLFLAGS = $(LFLAGS) -framework WebKit -framework Cocoa
SWT_OBJECTS = swt.o callback.o
SWTPI_OBJECTS = swt.o os.o os_custom.o os_structs.o os_stats.o
WEBKIT_OBJECTS = webkit.o

all: $(SWT_LIB) $(SWTPI_LIB) $(WEBKIT_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)
	
webkit.o: webkit.c
	cc $(WEBKITCFLAGS) webkit.c
	
$(WEBKIT_LIB): $(WEBKIT_OBJECTS)
	cc -o $(WEBKIT_LIB) $(WEBKITLFLAGS) $(WEBKIT_OBJECTS)

install: all
	cp *.jnilib $(OUTPUT_DIR)

clean:
	rm -f *.jnilib *.o
