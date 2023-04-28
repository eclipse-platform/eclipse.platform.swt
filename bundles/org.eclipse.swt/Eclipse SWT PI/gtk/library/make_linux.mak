#*******************************************************************************
# Copyright (c) 2000, 2016 IBM Corporation and others.
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

# Makefile for creating SWT libraries for Linux GTK

# assumes these variables are set in the environment from which make is run
#	SWT_JAVA_HOME
#	OUTPUT_DIR

# SWT debug flags for various SWT components.
#SWT_WEBKIT_DEBUG = -DWEBKIT_DEBUG

#SWT_LIB_DEBUG=1     # to debug glue code in /bundles/org.eclipse.swt/bin/library. E.g os_custom.c:swt_fixed_forall(..)
# Can be set via environment like: export SWT_LIB_DEBUG=1
ifdef SWT_LIB_DEBUG
SWT_DEBUG = -O0 -g3 -ggdb3
NO_STRIP=1
endif

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)r$(rev)
GTK_VERSION?=3.0

# Define the various shared libraries to be build.
WS_PREFIX = gtk
SWT_PREFIX = swt
AWT_PREFIX = swt-awt
ifeq ($(GTK_VERSION), 4.0)
SWTPI_PREFIX = swt-pi4
else
SWTPI_PREFIX = swt-pi3
endif
CAIRO_PREFIX = swt-cairo
ATK_PREFIX = swt-atk
WEBKIT_PREFIX = swt-webkit
GLX_PREFIX = swt-glx

SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWTPI_LIB = lib$(SWTPI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CAIRO_LIB = lib$(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
ATK_LIB = lib$(ATK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GLX_LIB = lib$(GLX_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
WEBKIT_LIB = lib$(WEBKIT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
ALL_SWT_LIBS = $(SWT_LIB) $(AWT_LIB) $(SWTPI_LIB) $(CAIRO_LIB) $(ATK_LIB) $(GLX_LIB) $(WEBKIT_LIB)

CAIROCFLAGS = `pkg-config --cflags cairo`
CAIROLIBS = `pkg-config --libs-only-L cairo` -lcairo

# Do not use pkg-config to get libs because it includes unnecessary dependencies (i.e. pangoxft-1.0)
ifeq ($(GTK_VERSION), 4.0)
GTKCFLAGS = `pkg-config --cflags gtk4 gtk4-x11 gtk4-unix-print`
GTKLIBS = `pkg-config --libs-only-L gtk4 gtk4-x11 gthread-2.0` $(XLIB64) -L/usr/X11R6/lib -lgtk-4 -lcairo -lgthread-2.0
ATKCFLAGS = `pkg-config --cflags atk gtk4 gtk4-unix-print`
else
GTKCFLAGS = `pkg-config --cflags gtk+-$(GTK_VERSION) gtk+-unix-print-$(GTK_VERSION)`
GTKLIBS = `pkg-config --libs-only-L gtk+-$(GTK_VERSION) gthread-2.0` $(XLIB64) -L/usr/X11R6/lib -lgtk-3 -lgdk-3 -lcairo -lgthread-2.0
ATKCFLAGS = `pkg-config --cflags atk gtk+-$(GTK_VERSION) gtk+-unix-print-$(GTK_VERSION)`
endif

AWT_LFLAGS = -shared ${SWT_LFLAGS} 
AWT_LIBS = -L$(AWT_LIB_PATH) -ljawt

ATKLIBS = `pkg-config --libs-only-L atk` -latk-1.0 

GLXLIBS = -lGL -lGLU -lm

WEBKITLIBS = `pkg-config --libs-only-l gio-2.0`
WEBKITCFLAGS = `pkg-config --cflags gio-2.0`

ifdef SWT_WEBKIT_DEBUG
# don't use 'webkit2gtk-4.0' in production,  as some systems might not have those libs and we get crashes.
WEBKITLIBS +=  `pkg-config --libs-only-l webkit2gtk-4.0`
WEBKITCFLAGS +=  `pkg-config --cflags webkit2gtk-4.0`
endif

SWT_OBJECTS = swt.o c.o c_stats.o callback.o
AWT_OBJECTS = swt_awt.o
ifeq ($(GTK_VERSION), 4.0)
GTKX_OBJECTS = gtk4.o gtk4_stats.o gtk4_structs.o
else
GTKX_OBJECTS = gtk3.o gtk3_stats.o gtk3_structs.o
endif
SWTPI_OBJECTS = swt.o os.o os_structs.o os_custom.o os_stats.o $(GTKX_OBJECTS)
CAIRO_OBJECTS = swt.o cairo.o cairo_structs.o cairo_stats.o
ATK_OBJECTS = swt.o atk.o atk_structs.o atk_custom.o atk_stats.o
WEBKIT_OBJECTS = swt.o webkitgtk.o webkitgtk_structs.o webkitgtk_stats.o webkitgtk_custom.o
GLX_OBJECTS = swt.o glx.o glx_structs.o glx_stats.o

CFLAGS := $(CFLAGS) \
		-DSWT_VERSION=$(SWT_VERSION) \
		$(SWT_DEBUG) \
		$(SWT_WEBKIT_DEBUG) \
		-DLINUX -DGTK \
		-I$(SWT_JAVA_HOME)/include \
		-I$(SWT_JAVA_HOME)/include/linux \
		${SWT_PTR_CFLAGS}
LFLAGS = -shared -fPIC ${SWT_LFLAGS}

# Treat all warnings as errors. If your new code produces a warning, please
# take time to properly understand and fix/silence it as necessary.
CFLAGS += -Werror

ifndef NO_STRIP
	# -s = Remove all symbol table and relocation information from the executable.
	#      i.e, more efficent code, but removes debug information. Should not be used if you want to debug.
	#      https://gcc.gnu.org/onlinedocs/gcc/Link-Options.html#Link-Options
	#      http://stackoverflow.com/questions/14175040/effects-of-removing-all-symbol-table-and-relocation-information-from-an-executab
	AWT_LFLAGS := $(AWT_LFLAGS) -s
	LFLAGS := $(LFLAGS) -s
endif

all: make_swt make_atk make_glx make_webkit

#
# SWT libs
#
make_swt: $(SWT_LIB) $(SWTPI_LIB)

$(SWT_LIB): $(SWT_OBJECTS)
	$(CC) $(LFLAGS) -o $(SWT_LIB) $(SWT_OBJECTS)

callback.o: callback.c callback.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -DUSE_ASSEMBLER -c callback.c

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

gtk3.o: gtk3.c gtk3.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk3.c
gtk3_structs.o: gtk3_structs.c gtk3_structs.h gtk3.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk3_structs.c
gtk3_stats.o: gtk3_stats.c gtk3_structs.h gtk3.h gtk3_stats.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk3_stats.c

gtk4.o: gtk4.c gtk4.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk4.c
gtk4_structs.o: gtk4_structs.c gtk4_structs.h gtk4.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk4_structs.c
gtk4_stats.o: gtk4_stats.c gtk4_structs.h gtk4.h gtk4_stats.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) -c gtk4_stats.c

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
make_awt:$(AWT_LIB)

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

#
# WebKit lib
#
make_webkit: $(WEBKIT_LIB)

$(WEBKIT_LIB): $(WEBKIT_OBJECTS)
	$(CC) $(LFLAGS) -o $(WEBKIT_LIB) $(WEBKIT_OBJECTS) $(WEBKITLIBS)

webkitgtk.o: webkitgtk.c webkitgtk_custom.h
	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk.c

webkitgtk_structs.o: webkitgtk_structs.c
	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_structs.c

webkitgtk_stats.o: webkitgtk_stats.c webkitgtk_stats.h
	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_stats.c

webkitgtk_custom.o: webkitgtk_custom.c
	$(CC) $(CFLAGS) $(WEBKITCFLAGS) -c webkitgtk_custom.c

#
# GLX lib
#
make_glx: $(GLX_LIB)

$(GLX_LIB): $(GLX_OBJECTS)
	$(CC) $(LFLAGS) -o $(GLX_LIB) $(GLX_OBJECTS) $(GLXLIBS)

glx.o: glx.c 
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx.c

glx_structs.o: glx_structs.c 
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_structs.c
	
glx_stats.o: glx_stats.c glx_stats.h
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_stats.c

#
# Install
#
# Note on syntax because below might be confusing even for bash-Gods:
# @    do not print command
# -    do not stop if there is a fail..  => @-  is combination of both.
# [ COND ] && CMD     only run CMD if condition is true like 'if COND then CMD'. Single line to be makefile compatible.
# -d   test if file exists and is a directory.
# $$(val)    in makefile, you have to escape $(..) into $$(..)
# I hope there are no spaces in the path :-).
install: all
	cp $(ALL_SWT_LIBS) $(OUTPUT_DIR)

#
# Clean
#
clean:
	rm -f *.o *.so
