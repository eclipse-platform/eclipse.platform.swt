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

# User configuration

# Define the installation directories for various products.
# Your system may have these in a different place.

# JAVA_JNI - Depending on your version of JDK, and where
# it is installed, your jni.h may be located differently.
#JAVA_JNI = /bluebird/teamswt/swt-builddir/ive/bin/include
JAVA_JNI = /opt/IBMvame1.4/ive/bin/include

# Whether we want GTK over X or FB
GTKTARGET = gtk+-2.0
#GTKTARGET = gtk+-linux-fb-2.0

CC = gcc
LD = ld

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
OS_PREFIX    = linux
SWT_DLL      = lib$(SWT_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
SWTPI_DLL    = lib$(SWT_PREFIX)-pi-$(OS_PREFIX)-$(SWT_VERSION).so

#GNOME_PREFIX = swt-gnome
#GNOME_DLL    = lib$(GNOME_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
#GNOME_LIB    = -x -shared \
#    -L/usr/lib \
#    -lgnome -lglib \
#    -lm -ldl


# Compile and link options from pkg-config
GTKCFLAGS = `pkg-config --cflags $(GTKTARGET)` `pkg-config --cflags pango`
GTKLIBS = `pkg-config --libs $(GTKTARGET)`


#
#  Target Rules
#

all: make_swt  # make_gnome

make_swt: $(SWT_DLL) $(SWTPI_DLL)

#make_gnome: $(GNOME_DLL)


# All about Linking

PI_OBJECTS = swt.o swt-gdk.o swt-gdkpixbuf.o \
             swt-gtkcontainers.o swt-gtkcontrols.o swt-gtklists.o swt-gtkmenu.o \
	     swt-gtkwidget.o swt-gtkwindow.o swt-pango.o swt-memmove.o \
	     eclipsefixed.o

$(SWT_DLL): callback.o
	$(LD) -x -shared \
	    -o $(SWT_DLL) callback.o
	    
$(SWTPI_DLL): $(PI_OBJECTS) structs.o
	$(LD) -x -shared \
	    $(GTKLIBS) \
	    -o $(SWTPI_DLL) $(PI_OBJECTS) structs.o

#$(GNOME_DLL): gnome.o
#	$(LD) -o $@ gnome.o $(GNOME_LIB)


# All about Compiling

CFLAGS = -c -O -s \
	    -DSWT_VERSION=$(SWT_VERSION) \
	    -DLINUX -DGTK \
	    -fpic -fPIC \
	    $(GTKCFLAGS) \
	    -I$(JAVA_JNI)

callback.o: callback.c
	$(CC) $(CFLAGS) callback.c

library.o: library.c
	$(CC) $(CFLAGS) library.c

swt.o: swt.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt.c

swt-gdk.o: swt-gdk.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gdk.c

swt-gdkpixbuf.o: swt-gdkpixbuf.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gdkpixbuf.c

swt-gtkcontainers.o: swt-gtkcontainers.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtkcontainers.c

swt-gtkcontrols.o: swt-gtkcontrols.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtkcontrols.c

swt-gtklists.o: swt-gtklists.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtklists.c

swt-gtkmenu.o: swt-gtkmenu.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtkmenu.c

swt-gtkwidget.o: swt-gtkwidget.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtkwidget.c

swt-gtkwindow.o: swt-gtkwindow.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-gtkwindow.c

swt-memmove.o: swt-memmove.c swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) swt-memmove.c

eclipsefixed.o: eclipsefixed.c eclipsefixed.h swt.h
	$(CC) $(CFLAGS) $(GTKCFLAGS) eclipsefixed.c

structs.o: structs.c
	$(CC) $(CFLAGS) $(GTKCFLAGS) structs.c

globals.o: globals.c
	g$(CC)cc $(CFLAGS) $(GTKCFLAGS) globals.c



clean:
	rm -f *.o *.so
