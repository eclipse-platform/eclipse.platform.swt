# Makefile for creating SWT libraries on Linux
#
# The following environment variables are assumed to be defined:
#
# MAJOR_VER  - the major version number 
# MINOR_VER  - the minor version number 
# BUILD_NUM  - the build number

DLL_VERSION=$(MAJOR_VER)$(MINOR_VER)


# Define the installation directories for various products.
#    IVE_HOME   - IBM's version of Java (J9)
#    MOTIF_HOME - Motif includes and libraries
#    QT_HOME    - identifier namespace package (used by KDE)
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive/bin
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
QT_HOME    = /usr/lib/qt-2.2.0


# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
SWT_DLL      = lib$(SWT_PREFIX)$(DLL_VERSION).so
SWT_OBJ      = callback.o globals.o library.o structs.o swt.o
SWT_LIB      = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	       -x -shared -lX11 -lm -lXext -lXt -lXp -lXpm -ldl

GNOME_PREFIX = swt-gnome
GNOME_DLL    = lib$(GNOME_PREFIX)$(DLL_VERSION).so
GNOME_OBJ    = gnome.o 
GNOME_LIB    = -x -shared \
	       `gnome-config --libs gnome`

KDE_PREFIX   = swt-kde
KDE_DLL      = lib$(KDE_PREFIX)$(DLL_VERSION).so
KDE_OBJ      = kde.o
KDE_LIB      = -L/usr/lib  -L$(QT_HOME)/lib \
	       -shared -lksycoca -lkdecore -lq

#
# The following CFLAGS are for compiling both the SWT library and the GNOME
# library. The KDE library uses its own (C++) flags.
#
CFLAGS = -O -s \
	-DSWT_LIBRARY_MAJOR_VERSION=$(MAJOR_VER) \
	-DSWT_LIBRARY_MINOR_VERSION=$(MINOR_VER) \
	-DLINUX -DMOTIF -DGNOME -DXPM \
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
	rm -f $(SWT_OBJ) $(SWT_DLL) $(GNOME_OBJ) $(GNOME_DLL)
	rm -f $(KDE_OBJ) $(KDE_DLL)

