#*******************************************************************************
# Copyright (c) 2000, 2009 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#*******************************************************************************

# Makefile for SWT libraries on Solaris

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# This makefile expects the following environment variables set:
#    JAVA_HOME  - The JDK > 1.3
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX = swt
WS_PREFIX = motif
SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS = swt.o c.o c_stats.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME)/lib -L/usr/lib -R/usr/openwin/lib -G -lXm -lXt -lX11 -lXp -lXtst

CDE_PREFIX = swt-cde
CDE_LIB = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CDE_OBJS = swt.o cde.o cde_structs.o cde_stats.o
CDE_LIBS = -G -L$(CDE_HOME)/lib -R$(CDE_HOME)/lib -lDtSvc

GLX_PREFIX = swt-glx
GLX_LIB = lib$(GLX_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GLX_OBJS = swt.o glx.o glx_structs.o glx_stats.o
GLX_LIBS = -G -L/usr/X11R6/lib -lGL -lGLU -lm

AWT_PREFIX = swt-awt
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJS = swt_awt.o
AWT_LIBS = -G -L$(JAVA_HOME)/jre/lib/sparc -ljawt

CAIRO_PREFIX = swt-cairo
CAIRO_LIB = lib$(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CAIRO_OBJS = swt.o cairo.o cairo_structs.o cairo_stats.o
CAIROCFLAGS = `pkg-config --cflags cairo`
CAIRO_LIBS = -G `pkg-config --libs-only-L cairo` -lcairo

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
# Note:
#   The flag -xarch=generic ensure the compiled modules will be targeted
#   for 32-bit architectures. If this flag is not
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) \
	-DNO_XINERAMA_EXTENSIONS \
	-DDTACTION_WARNING_DISABLED \
	-DSOLARIS -DMOTIF -DCDE \
	-I./ \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/solaris \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include

all: make_swt make_awt make_glx make_cde

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	ld -o $@ $(SWT_OBJS) $(SWT_LIBS)

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
	ld -o $@ $(AWT_OBJS) $(AWT_LIBS)

make_glx: $(GLX_LIB)

$(GLX_LIB): $(GLX_OBJS)
	ld -o $@ $(GLX_OBJS) $(GLX_LIBS)

glx.o: glx.c
	$(CC)   $(CFLAGS) -c glx.c
glx_structs.o: glx_structs.c
	$(CC)  $(CFLAGS) -c glx_structs.c
glx_stats.o: glx_stats.c glx_stats.h
	$(CC)  $(CFLAGS) -c glx_stats.c

install: all
	cp *.so $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl 
