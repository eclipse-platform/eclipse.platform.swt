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

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#SWT_DEBUG = -g
CFLAGS = -c -xobjective-c -Wall $(ARCHS) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCOCOA -DATOMIC \
	$(CFLAGS_JAVA_VM) \
	-I /System/Library/Frameworks/Cocoa.framework/Headers \
	-I /System/Library/Frameworks/JavaScriptCore.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework Cocoa -framework WebKit -framework CoreServices -framework JavaScriptCore -framework Security -framework SecurityInterface
SWT_OBJECTS = swt.o c.o c_stats.o callback.o
SWTPI_OBJECTS = swt.o os.o os_structs.o os_stats.o os_custom.o

CHROMIUM_PREFIX = swt-chromium
CHROMIUM_LIB    = lib$(CHROMIUM_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib
CHROMIUM_OBJECTS   = chromiumlib.o chromiumlib_structs.o chromiumlib_custom.o chromiumlib_stats.o
CHROMIUM_CFLAGS = -I $(CHROMIUM_HEADERS)
CHROMIUM_LFLAGS = -bundle $(ARCHS) -lchromium_swt_${SWT_VERSION} -L$(CHROMIUM_OUTPUT_DIR)/chromium-$(cef_ver)

all: $(SWT_LIB) $(SWTPI_LIB) $(AWT_LIB)

make_chromium: $(CHROMIUM_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(AWT_LIB): $(AWT_OBJECTS)
	cc -o $(AWT_LIB) $(LFLAGS) $(AWT_OBJECTS)

$(SWT_LIB): $(SWT_OBJECTS)
	cc -o $(SWT_LIB) $(LFLAGS) $(SWT_OBJECTS)

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	cc -o $(SWTPI_LIB) $(LFLAGS) $(SWTPI_OBJECTS)

chromiumlib.o:
	cc $(CFLAGS) $(CHROMIUM_CFLAGS) chromiumlib.c

chromiumlib_structs.o:
	cc $(CFLAGS) $(CHROMIUM_CFLAGS) chromiumlib_structs.c

chromiumlib_custom.o:
	cc $(CFLAGS) $(CHROMIUM_CFLAGS) chromiumlib_custom.c

chromiumlib_stats.o:
	cc $(CFLAGS) $(CHROMIUM_CFLAGS) chromiumlib_stats.c

$(CHROMIUM_LIB): $(CHROMIUM_OBJECTS)
	cc -o $(CHROMIUM_LIB) $(CHROMIUM_LFLAGS) $(CHROMIUM_OBJECTS)

install: all
	cp *.jnilib $(OUTPUT_DIR)

chromium_cargo:
	cd chromium_subp && cargo build --release
	cd chromium_swt && cargo build --release
	mkdir -p $(CHROMIUM_OUTPUT_DIR)/chromium-$(cef_ver)/chromium_subp-$(SWT_VERSION).app/Contents/MacOS/
	install_name_tool -change '@rpath/Frameworks/Chromium Embedded Framework.framework/Chromium Embedded Framework' '@loader_path/../../../Chromium Embedded Framework.framework/Chromium Embedded Framework' chromium_subp/target/release/chromium_subp
	install_name_tool -change '@rpath/Frameworks/Chromium Embedded Framework.framework/Chromium Embedded Framework' '@loader_path/Chromium Embedded Framework.framework/Chromium Embedded Framework' chromium_swt/target/release/libchromium_swt_$(SWT_VERSION).dylib
	install_name_tool -id '@loader_path/libchromium_swt_$(SWT_VERSION).dylib' chromium_swt/target/release/libchromium_swt_$(SWT_VERSION).dylib
	strip -r -u chromium_swt/target/release/libchromium_swt_$(SWT_VERSION).dylib
	cp chromium_subp/target/release/chromium_subp $(CHROMIUM_OUTPUT_DIR)/chromium-$(cef_ver)/chromium_subp-$(SWT_VERSION).app/Contents/MacOS/
	cp chromium_swt/target/release/libchromium_swt_$(SWT_VERSION).dylib $(CHROMIUM_OUTPUT_DIR)/chromium-$(cef_ver)
chromium_install: make_chromium
	strip -r -u $(CHROMIUM_LIB)
	cp $(CHROMIUM_LIB) "$(CHROMIUM_OUTPUT_DIR)/chromium-$(cef_ver)"

clean:
	rm -f *.jnilib *.o
