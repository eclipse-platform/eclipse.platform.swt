# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)


# Define the installation directories for various products.
#    IVE_HOME   - IBM's version of Java (J9)
#    MOTIF_HOME - Motif includes and libraries
#    QT_HOME    - identifier namespace package (used by KDE)
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive/bin
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
QT_HOME    = /usr/lib/qt-2.3.0


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
GNOME_LIB    = -x -shared \
	           `gnome-config --libs gnome`

KDE_PREFIX   = swt-kde
KDE_DLL      = lib$(KDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
KDE_OBJ      = kde.o
KDE_LIB      = -L/usr/lib  -L$(QT_HOME)/lib \
	           -shared -lksycoca -lkdecore -lqt

#
# The following CFLAGS are for compiling both the SWT library and the GNOME
# library. The KDE library uses its own (C++) flags.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DLINUX -DMOTIF -DGNOME \
	-fpic \
	-I./ \
	-I$(IVE_HOME)/include \
	-I$(MOTIF_HOME)/include \
	-I/usr/X11R6/include \
	`gnome-config --cflags gnome gnomeui`


all: make_swt make_gnome

kde: make_kde


make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -o $@ $(SWT_OBJ) $(SWT_LIB)


make_gnome: $(GNOME_DLL)

$(GNOME_DLL): $(GNOME_OBJ)
	ld -o $@ $(GNOME_OBJ) $(GNOME_LIB)


make_kde: $(KDE_DLL)

$(KDE_DLL): $(KDE_OBJ)
	ld -o $@ $(KDE_OBJ) $(KDE_LIB)

$(KDE_OBJ): kde.cc
	g++  -c -O -I/usr/include/kde -I$(QT_HOME)/include -I./   \
	     -I../ -I$(IVE_HOME)/include -fno-rtti -o kde.o kde.cc

clean:
	rm -f *.so *.o

