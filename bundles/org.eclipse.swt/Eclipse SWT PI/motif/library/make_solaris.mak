#*******************************************************************************
# Copyright (c) 2000, 2005 IBM Corporation and others.
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
SWT_OBJS = swt.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME)/lib -L/usr/lib -R/usr/openwin/lib -G -lXm -lXt -lX11 -lXp -lXtst

CDE_PREFIX = swt-cde
CDE_LIB = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CDE_OBJS = swt.o cde.o cde_structs.o cde_stats.o
CDE_LIBS = -G -L$(CDE_HOME)/lib -R$(CDE_HOME)/lib -lDtSvc

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

install: all
	cp *.so $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl 
