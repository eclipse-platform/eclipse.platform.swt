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
#    JAVA_HOME  - The JDK > 1.3
#    MOTIF_HOME - Motif includes and libraries
#    QT_HOME    - identifier namespace package (used by KDE)
JAVA_HOME   = /bluebird/teamswt/swt-builddir/ive/bin
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
QT_HOME    = /usr/lib/qt3

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJ      = callback.o structs.o swt.o
SWT_LIB      = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	           -rpath . -x -shared -lX11 -lm -lXext -lXt -lXp -ldl

GNOME_PREFIX = swt-gnome
GNOME_DLL    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_OBJ    = gnome.o 

GNOME_CFLAGS = `pkg-config --cflags gnome-vfs-2.0`
GNOME_LIB = -x -shared `pkg-config --libs gnome-vfs-2.0`

KDE_PREFIX   = swt-kde
KDE_DLL      = lib$(KDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
KDE_OBJ      = kde.o
KDE_LIB      = -L/usr/lib  -L$(QT_HOME)/lib \
	           -shared  -lkdecore -lqt

#
# The following CFLAGS are for compiling both the SWT library and the GNOME
# library. The KDE library uses its own (C++) flags.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DLINUX -DMOTIF  \
	-fpic \
	-I./ \
	-I$(JAVA_HOME)/include \
	-I$(MOTIF_HOME)/include \
	-I/usr/X11R6/include 


all: make_swt make_gnome

kde: make_kde


make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -o $@ $(SWT_OBJ) $(SWT_LIB)


make_gnome: $(GNOME_DLL)

$(GNOME_DLL): $(GNOME_OBJ)
	ld -o $@ $(GNOME_OBJ) $(GNOME_LIB)

$(GNOME_OBJ): gnome.c 
	$(CC) $(CFLAGS) $(GNOME_CFLAGS) -c -o gnome.o gnome.c

make_kde: $(KDE_DLL)

$(KDE_DLL): $(KDE_OBJ)
	ld -o $@ $(KDE_OBJ) $(KDE_LIB)

$(KDE_OBJ): kde.cc
	g++  -c -O -I/usr/include/kde -I$(QT_HOME)/include -I./   \
	     -I../ -I$(JAVA_HOME)/include -fno-rtti -o kde.o kde.cc

clean:
	rm -f *.so *.o

