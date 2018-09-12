#*******************************************************************************
# Copyright (c) 2000, 2017 IBM Corporation and others.
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
#     Rolf Theunissen - SWT/GTK port on Windows
#*******************************************************************************

# Makefile for creating SWT libraries for win32 GTK

# rewrite backslashes to slashes in paths
JAVA_HOME := $(subst \,/,$(JAVA_HOME))
OUTPUT_DIR := $(subst \,/,$(OUTPUT_DIR))

#SWT_LIB_DEBUG=1     # to debug glue code in /bundles/org.eclipse.swt/bin/library. E.g os_custom.c:swt_fixed_forall(..)
# Can be set via environment like: export SWT_LIB_DEBUG=1
ifdef SWT_LIB_DEBUG
SWT_DEBUG = -O0 -g3 -ggdb3
NO_STRIP=1
endif

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)r$(rev)
GTK_VERSION?=2.0

# Define the various shared libraries to be build.
WS_PREFIX = gtk
SWT_PREFIX = swt
AWT_PREFIX = swt-awt
ifeq ($(GTK_VERSION), 3.0)
SWTPI_PREFIX = swt-pi3
else
SWTPI_PREFIX = swt-pi
endif
CAIRO_PREFIX = swt-cairo
ATK_PREFIX = swt-atk
# WEBKIT_PREFIX = swt-webkit

SWT_LIB = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
AWT_LIB = $(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
SWTPI_LIB = $(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
CAIRO_LIB = $(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
ATK_LIB = $(ATK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
# WEBKIT_LIB = $(WEBKIT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll

CAIROCFLAGS = `pkg-config --cflags cairo`
CAIROLIBS = `pkg-config --libs cairo`

GTKCFLAGS = `pkg-config --cflags gtk+-$(GTK_VERSION)`
ifeq ($(GTK_VERSION), 3.0)
GTKLIBS = `pkg-config --libs gtk+-$(GTK_VERSION) gthread-2.0` $(XLIB64)
else
GTKLIBS = `pkg-config --libs gtk+-$(GTK_VERSION) gthread-2.0` $(XLIB64)
endif

AWT_LFLAGS = -shared ${SWT_LFLAGS}
AWT_LIBS = -L$(AWT_LIB_PATH) -ljawt

ATKCFLAGS = `pkg-config --cflags atk gtk+-$(GTK_VERSION)`
ATKLIBS = `pkg-config --libs atk`

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

# WEBKITLIBS = `pkg-config --libs-only-l gio-2.0`
# WEBKITCFLAGS = `pkg-config --cflags gio-2.0`
# ifdef SWT_WEBKIT_DEBUG
# # don't use 'webkit2gtk-4.0' in production,  as some systems might not have those libs and we get crashes.
# WEBKITLIBS +=  `pkg-config --libs-only-l webkit2gtk-4.0`
# WEBKITCFLAGS +=  `pkg-config --cflags webkit2gtk-4.0`
# endif


SWT_OBJECTS = swt.o c.o c_stats.o callback.o
AWT_OBJECTS = swt_awt.o
SWTPI_OBJECTS = swt.o os.o os_structs.o os_custom.o os_stats.o
CAIRO_OBJECTS = swt.o cairo.o cairo_structs.o cairo_stats.o
ATK_OBJECTS = swt.o atk.o atk_structs.o atk_custom.o atk_stats.o
# WEBKIT_OBJECTS = swt.o webkitgtk.o webkitgtk_structs.o webkitgtk_stats.o webkitgtk_custom.o

CFLAGS = -O -Wall \
		-DSWT_VERSION=$(SWT_VERSION) \
		$(NATIVE_STATS) \
		$(SWT_DEBUG) \
		-DWIN32 -DGTK \
		-I$(JAVA_HOME)/include \
		-I$(JAVA_HOME)/include/win32 \
		-fPIC \
		${SWT_PTR_CFLAGS}
LFLAGS = -shared -fPIC ${SWT_LFLAGS}

ifndef NO_STRIP
	AWT_LFLAGS := $(AWT_LFLAGS) -s
	LFLAGS := $(LFLAGS) -s
endif

all: make_swt make_atk

#
# SWT libs
#
make_swt: $(SWT_LIB) $(SWTPI_LIB)

$(SWT_LIB): $(SWT_OBJECTS)
	$(CC) $(LFLAGS) -o $(SWT_LIB) $(SWT_OBJECTS)

callback.o: callback.c callback.h
	$(CC) $(CFLAGS) -DUSE_ASSEMBLER -c callback.c

$(SWTPI_LIB): $(SWTPI_OBJECTS)
	$(CC) $(LFLAGS) -o $(SWTPI_LIB) $(SWTPI_OBJECTS) $(GTKLIBS)

swt.o: swt.c swt.h
	$(CC) $(CFLAGS) -c swt.c
os.o: os.c os.h swt.h os_custom.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c os.c
os_structs.o: os_structs.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c os_structs.c 
os_custom.o: os_custom.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c os_custom.c
os_stats.o: os_stats.c os_structs.h os.h os_stats.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c os_stats.c

#
# CAIRO libs
#
make_cairo: $(CAIRO_LIB)

$(CAIRO_LIB): $(CAIRO_OBJECTS)
	$(CC) $(LFLAGS) -o $(CAIRO_LIB) $(CAIRO_OBJECTS) $(CAIROLIBS)

cairo.o: cairo.c cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo.c
cairo_structs.o: cairo_structs.c cairo_structs.h cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_structs.c
cairo_stats.o: cairo_stats.c cairo_structs.h cairo.h cairo_stats.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_stats.c

#
# AWT lib
#
# make_awt:$(AWT_LIB) # TODO [win32] solve gtk/x11 vs gtk/win32 native integration
make_awt: 

$(AWT_LIB): $(AWT_OBJECTS)
	$(CC) $(AWT_LFLAGS) -o $(AWT_LIB) $(AWT_OBJECTS) $(AWT_LIBS)

#
# Atk lib
#
make_atk: $(ATK_LIB)

$(ATK_LIB): $(ATK_OBJECTS)
	$(CC) $(LFLAGS) -o $(ATK_LIB) $(ATK_OBJECTS) $(ATKLIBS)

atk.o: atk.c atk.h
	$(CC) $(CFLAGS) $(ATKCFLAGS) -c atk.c
atk_structs.o: atk_structs.c atk_structs.h atk.h
	$(CC) $(CFLAGS) $(ATKCFLAGS) -c atk_structs.c
atk_custom.o: atk_custom.c atk_structs.h atk.h
	$(CC) $(CFLAGS) $(ATKCFLAGS) -c atk_custom.c
atk_stats.o: atk_stats.c atk_structs.h atk_stats.h atk.h
	$(CC) $(CFLAGS) $(ATKCFLAGS) -c atk_stats.c

# TODO check availability webkitgtk on win32
# #
# # WebKit lib
# #
# make_webkit: $(WEBKIT_LIB)
#
# $(WEBKIT_LIB): $(WEBKIT_OBJECTS)
#	$(CC) $(LFLAGS) -o $(WEBKIT_LIB) $(WEBKIT_OBJECTS) $(WEBKITLIBS)
#
# webkitgtk.o: webkitgtk.c webkitgtk_custom.h
#	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk.c
#
# webkitgtk_structs.o: webkitgtk_structs.c
#	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_structs.c
#
# webkitgtk_stats.o: webkitgtk_stats.c webkitgtk_stats.h
#	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_stats.c
#
# webkitgtk_custom.o: webkitgtk_custom.c
#	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_custom.c

#
# Install
#
install: all
	cp *.dll $(OUTPUT_DIR)

#
# Clean
#
clean:
	rm -f *.o *.dll
