# Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
#
# The contents of this file are made available under the terms
# of the GNU Lesser General Public License (LGPL) Version 2.1 that
# accompanies this distribution (lgpl-v21.txt).  The LGPL is also
# available at http://www.gnu.org/licenses/lgpl.html.  If the version
# of the LGPL at http://www.gnu.org is different to the version of
# the LGPL accompanying this distribution and there is any conflict
# between the two license versions, the terms of the LGPL accompanying
# this distribution shall govern.
#
# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)


# Define the installation directories for various products.
# Your system may have these in a different place.
#    IVE_HOME   - IBM's version of Java (J9)
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive
#IVE_HOME   = /opt/IBMvame1.4/ive

JAVA_JNI=$(IVE_HOME)/bin/include
JAVAH=$(IVE_HOME)/bin/javah
LD_LIBRARY_PATH=$(IVE_HOME)/bin

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = gtk
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so

PI_PREFIX   = swt-pi
SWTPI_DLL   = lib$(PI_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so

GNOME_PREFIX = swt-gnome
GNOME_DLL    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_LIB    = -x -shared \
    -L/usr/lib \
   -lgnome -lglib \
    -lm -ldl

PIXBUF_PREFIX = swt-pixbuf
PIXBUF_DLL    = lib$(PIXBUF_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so


#
#  Target Rules
#

all: make_swt  make_pixbuf  # make_gnome

make_swt: $(SWT_DLL) $(SWTPI_DLL)

#make_gnome: $(GNOME_DLL)

make_pixbuf: $(PIXBUF_DLL)


# All about Linking

$(SWT_DLL): callback.o
	ld -x -shared \
	    -o $(SWT_DLL) callback.o
	    
# Note:  your setup may be different.  Consult `gtk-config --libs`
$(SWTPI_DLL): swt.o structs.o
	ld -x -shared \
	    -L/usr/lib -L/usr/X11R6/lib \
	    -lgtk -lgdk -lgmodule -lglib \
	    -ldl -lXi -lXext -lX11 -lm -lc \
	    -o $(SWTPI_DLL) swt.o structs.o

#$(GNOME_DLL): gnome.o
#	ld -o $@ gnome.o $(GNOME_LIB)

$(PIXBUF_DLL): pixbuf.o
	ld -x -shared \
	    -L/usr/lib -L/usr/X11R6/lib \
	    -lgdk_pixbuf \
	    -lgtk -lgdk -lgmodule -lglib \
	    -ldl -lXi -lXext -lX11 -lm -lc \
	    -o $(PIXBUF_DLL) pixbuf.o


# All about Compiling

SWT_C_FLAGS = -c -O -s \
	    -DSWT_VERSION=$(SWT_VERSION) \
	    -DLINUX -DGTK \
	    -fpic \
	    -I$(JAVA_JNI) \
	    `gtk-config --cflags`

SWT_PIXBUF_FLAGS = -c -O -s \
	    -DSWT_VERSION=$(SWT_VERSION) \
	    -DLINUX -DGTK \
	    -fpic \
	    -I$(JAVA_JNI) \
	    -I/usr/include/gdk-pixbuf \
	    `gtk-config --cflags`

SWT_GNOME_FLAGS = -c -O -s \
	    -DSWT_VERSION=$(SWT_VERSION) \
	    -DLINUX -DGTK \
	    -fpic \
	    -I$(JAVA_JNI) \
	    `gnome-config --cflags gnome`

swt.o: swt.c swt.h
	gcc $(SWT_C_FLAGS) swt.c

structs.o: structs.c
	gcc $(SWT_C_FLAGS) structs.c

callback.o: callback.c
	gcc $(SWT_C_FLAGS) callback.c

globals.o: globals.c
	gcc $(SWT_C_FLAGS) globals.c

library.o: library.c
	gcc $(SWT_C_FLAGS) library.c

pixbuf.o: pixbuf.c
	gcc $(SWT_PIXBUF_FLAGS) pixbuf.c

#gnome.o: gnome.c
#	gcc $(SWT_GNOME_FLAGS) gnome.c

clean:
	rm -f *.o *.so
