#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#*******************************************************************************

# Makefile for SWT libraries on Solaris

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# Define the installation directories for various products.
#    JAVA_HOME  - The JDK > 1.3
#    MOTIF_HOME - Motif includes and libraries
#    CDE_HOME - CDE includes and libraries
JAVA_HOME  = /usr/java
MOTIF_HOME = /usr/dt
CDE_HOME   = /usr/dt

# Compiler (Solaris 9, using GNU GCC)
CC = gcc
PATH = /bin:/usr/ccs/bin/:/usr/ucb/:/usr/local/bin

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_LIB      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS      = swt.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS      = -L$(MOTIF_HOME)/lib -L/usr/lib -R/usr/openwin/lib  \
	       -G -lXm -lXt -lX11 -lm -lXp -lXtst

CDE_PREFIX   = swt-cde
CDE_LIB      = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CDE_OBJS      = cde.o
CDE_LIBS      = -G -L$(CDE_HOME)/lib -R$(CDE_HOME)/lib -lDtSvc


#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
# Note:
#   The flag -xarch=generic ensure the compiled modules will be targeted
#   for 32-bit architectures. If this flag is not
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DNO_XINERAMA_EXTENSIONS \
	-DSOLARIS -DMOTIF -DCDE \
	-I./ \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/solaris \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include


all: make_swt make_cde

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	ld -o $@ $(SWT_OBJS) $(SWT_LIBS)


make_cde: $(CDE_LIB)

$(CDE_LIB): $(CDE_OBJS)
	ld -o $@ $(CDE_OBJS) $(CDE_LIBS)


clean:
	rm -f *.so *.o

