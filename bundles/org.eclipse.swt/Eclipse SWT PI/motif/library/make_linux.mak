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
JAVA_HOME   = /bluebird/teamswt/swt-builddir/IBMJava2-141
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
# Redhat 9
QT_HOME    = /usr/lib/qt-3.1
# SuSE 8.2
#QT_HOME    = /usr/lib/qt3

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJ      = callback.o os.o os_structs.o os_custom.o
SWT_LIB      = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	           -rpath . -x -shared -lX11 -lm -lXext -lXt -lXp -ldl -lXinerama

GNOME_PREFIX = swt-gnome
GNOME_DLL    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_OBJ    = gnome.o 
GNOME_CFLAGS = `pkg-config --cflags gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0`
GNOME_LIB = -shared -fpic -fPIC `pkg-config --libs gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0`

KDE_PREFIX   = swt-kde
KDE_DLL      = lib$(KDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
KDE_OBJ      = kde.o
KDE_LIB      = -L/usr/lib  -L$(QT_HOME)/lib -shared  -lkdecore -lqt
KDE_CFLAGS   = -fno-rtti -c -O -I/usr/include/kde -I$(QT_HOME)/include -I$(JAVA_HOME)/include

AWT_PREFIX   = swt-awt
AWT_DLL      = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJ      = swt_awt.o
AWT_LIB      = -L$(JAVA_HOME)/jre/bin -ljawt -shared

GTK_PREFIX  = swt-gtk
GTK_DLL     = lib$(GTK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GTK_OBJ     = gtk.o
GTK_CFLAGS  = `pkg-config --cflags gtk+-2.0`
GTK_LIB     = -x -shared `pkg-config --libs-only-l --libs-only-L gtk+-2.0`
	
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

all: make_swt make_awt make_gnome make_gtk

kde: make_kde

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -o $@ $(SWT_OBJ) $(SWT_LIB)

make_gnome: $(GNOME_DLL)

$(GNOME_DLL): $(GNOME_OBJ)
	gcc -o $@ $(GNOME_OBJ) $(GNOME_LIB)

$(GNOME_OBJ): gnome.c 
	gcc -O -Wall -DSWT_VERSION=$(SWT_VERSION) -DLINUX -DGTK -I$(JAVA_HOME)/include $(GNOME_CFLAGS) -c -o gnome.o gnome.c

make_kde: $(KDE_DLL)

$(KDE_DLL): $(KDE_OBJ)
	ld -o $@ $(KDE_OBJ) $(KDE_LIB)

$(KDE_OBJ): kde.cc
	g++ $(KDE_CFLAGS) -o kde.o kde.cc

make_awt: $(AWT_DLL)

$(AWT_DLL): $(AWT_OBJ)
	ld -o $@ $(AWT_OBJ) $(AWT_LIB)

make_gtk: $(GTK_DLL)

$(GTK_DLL): $(GTK_OBJ)
	ld -o $@ $(GTK_OBJ) $(GTK_LIB)

$(GTK_OBJ): gtk.c
	$(CC) $(CFLAGS) $(GTK_CFLAGS) -c -o gtk.o gtk.c
		
clean:
	rm -f *.so *.o

