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

# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# Define the installation directories for various products.
# Your system may have these in a different place.

# Define the installation directories for various products.
# Your system may have these in a different place.
#    IVE_HOME   - IBM's version of Java (J9)
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive
#IVE_HOME   = /opt/IBMvame1.4/ive

JAVA_JNI=$(IVE_HOME)/bin/include
JAVAH=$(IVE_HOME)/bin/javah
LD_LIBRARY_PATH=$(IVE_HOME)/bin

# Whether we want GTK over X or FB
GTKTARGET = gtk+-2.0
#GTKTARGET = gtk+-linux-fb-2.0

CC = gcc
LD = ld

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = gtk
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWTPI_DLL    = lib$(SWT_PREFIX)-pi-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_PREFIX = swt-gnome
GNOME_DLL    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_OBJ    = gnome.o 

GNOME_CFLAGS = `pkg-config --cflags gnome-vfs-2.0`
GNOME_LIB = -x -shared `pkg-config --libs gnome-vfs-2.0`


# Compile and link options from pkg-config
GTKCFLAGS = `pkg-config --cflags $(GTKTARGET)` `pkg-config --cflags pango`

# TEMPORARY CODE
#
# Note: pkg-config is not being used because it generates flags that are dependent
# on the machine setup. Some machines do not have free type fonts support. The line
# below was obtained by running pkg-config and removing "-lpangoxft-1.0" from
# the result.
#
GTKLIBS = `pkg-config --libs gthread-2.0` -L/usr/local/lib -lgtk-x11-2.0 -lgdk-x11-2.0 -latk-1.0 -lgdk_pixbuf-2.0 -lm -lpangox-1.0 -lpango-1.0 -lgobject-2.0 -lgmodule-2.0 -ldl -lglib-2.0
#GTKLIBS = `pkg-config --libs $(GTKTARGET)`


#
#  Target Rules
#

all: make_swt  make_gnome

make_swt: $(SWT_DLL) $(SWTPI_DLL)

make_gnome: $(GNOME_DLL)

$(GNOME_DLL): gnome.o
	ld -o $@ $(GNOME_OBJ) $(GNOME_LIB)

$(GNOME_OBJ): gnome.c 
	$(CC) $(CFLAGS) $(GNOME_CFLAGS) -c -o gnome.o gnome.c

# All about Linking

PI_OBJECTS = swt.o structs.o

$(SWT_DLL): callback.o
	$(LD) -x -shared \
	    -o $(SWT_DLL) callback.o
	    
$(SWTPI_DLL): $(PI_OBJECTS) structs.o
	$(LD) -x -shared \
	    $(GTKLIBS) \
	    -o $(SWTPI_DLL) $(PI_OBJECTS)

#$(GNOME_DLL): gnome.o
#	$(LD) -o $@ gnome.o $(GNOME_LIB)


# All about Compiling

SWT_WARNINGS = #-Wimplicit-function-declaration
CFLAGS = -c -O -s \
	    -DSWT_VERSION=$(SWT_VERSION) \
	    -DLINUX -DGTK \
		$(SWT_WARNINGS) \
	    -fpic -fPIC \
	    $(GTKCFLAGS) \
	    -I$(JAVA_JNI)

callback.o: callback.c
	$(CC) $(CFLAGS) callback.c

swt.o: swt.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt.c

structs.o: structs.c
	$(CC) $(CFLAGS) $(GTKCFLAGS) structs.c

clean:
	rm -f *.o *.so
