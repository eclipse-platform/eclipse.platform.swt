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

# Makefile for creating SWT libraries for Linux GTK

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

# Define the various DLL (shared) libraries to be made.
SWT_PREFIX   = swt
WS_PREFIX    = gtk
GNOME_PREFIX = swt-gnome
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWTPI_DLL    = lib$(SWT_PREFIX)-pi-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_DLL    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so

GNOME_CFLAGS = `pkg-config --cflags gnome-vfs-2.0`
GNOME_LIB = `pkg-config --libs gnome-vfs-2.0`

GTKCFLAGS = `pkg-config --cflags gtk+-2.0`
GTKLIBS = `pkg-config --libs gtk+-2.0 gthread-2.0`

CFLAGS = -shared -O -DSWT_VERSION=$(SWT_VERSION) \
		-DLINUX -DGTK \
		-fpic -fPIC \
		-I$(JAVA_JNI)

#
#  Target Rules
#

all: make_swt  make_gnome

make_swt: $(SWT_DLL) $(SWTPI_DLL)

make_gnome: $(GNOME_DLL)

$(GNOME_DLL): gnome.c 
	gcc $(CFLAGS) $(GNOME_CFLAGS) $(GNOME_LIB)  -o $(GNOME_DLL) gnome.c

$(SWT_DLL): callback.c callback.h
	gcc  $(CFLAGS) -o $(SWT_DLL) callback.c

$(SWTPI_DLL): os_structs.c os_structs.h os.c swt.h os_custom.c
	gcc  $(CFLAGS) $(GTKCFLAGS) $(GTKLIBS) -o $(SWTPI_DLL) os_structs.c os.c os_custom.c

clean:
	rm -f *.o *.so
