#*******************************************************************************
# Copyright (c) 2000, 2008 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#*******************************************************************************

# Makefile for creating SWT libraries on AIX

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

CC=gcc

# This makefile expects the following environment variables set:
#    JAVA_HOME  - The JDK > 1.3
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX = swt
WS_PREFIX = motif
SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
SWT_OBJS = swt.o c.o c_stats.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME) -G -bnoentry -lc_r -lC_r -lm -bexpall -lXm -lMrm -lXt -lX11 -lXext -liconv -lXtst

CDE_PREFIX = swt-cde
CDE_LIB = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
CDE_OBJS = swt.o cde.o cde_structs.o cde_stats.o
CDE_LIBS = -L$(CDE_HOME)/lib -bnoentry -bexpall -lDtSvc -lc -lXt -lX11

CAIRO_PREFIX = swt-cairo
CAIRO_LIB = lib$(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
CAIRO_OBJS = swt.o cairo.o cairo_structs.o cairo_stats.o
CAIROCFLAGS = `pkg-config --cflags cairo`
CAIRO_LIBS = -G -bnoentry -bexpall -lc `pkg-config --libs-only-L cairo` -lcairo

AWT_PREFIX = swt-awt
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
AWT_OBJS = swt_awt.o
AWT_LIBS = -L$(AWT_HOME) -ljawt -G -bnoentry -bexpall -lc

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) \
	-D_MSGQSUPPORT \
	-DAIX -DMOTIF -DCDE \
	-DNO_XPRINTING_EXTENSIONS -DNO_XINERAMA_EXTENSIONS \
	-I$(JAVA_HOME)/include \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include

all: make_swt make_awt make_cde make_cairo

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	ld $(SWT_LIBS) -o $(SWT_LIB) $(SWT_OBJS)

make_cde: $(CDE_LIB)

$(CDE_LIB): $(CDE_OBJS)
	ld -o $@ $(CDE_OBJS) $(CDE_LIBS)

make_cairo: $(CAIRO_LIB)

$(CAIRO_LIB): $(CAIRO_OBJS)
	ld -o $@ $(CAIRO_OBJS) $(CAIRO_LIBS)

cairo.o: cairo.c cairo.h swt.h
	$(CC)  $(CAIROCFLAGS)  $(CFLAGS) -c cairo.c
cairo_structs.o: cairo_structs.c cairo_structs.h cairo.h swt.h
	$(CC)  $(CAIROCFLAGS)  $(CFLAGS) -c cairo_structs.c
cairo_stats.o: cairo_stats.c cairo_structs.h cairo.h cairo_stats.h swt.h
	$(CC)  $(CAIROCFLAGS) $(CFLAGS) -c cairo_stats.c

make_awt: $(AWT_LIB)

$(AWT_LIB): $(AWT_OBJS)
	ld -o $(AWT_LIB) $(AWT_OBJS) $(AWT_LIBS)

install: all
	cp *.a $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl 
