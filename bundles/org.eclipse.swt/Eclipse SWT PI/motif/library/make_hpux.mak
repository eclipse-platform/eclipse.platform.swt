#*******************************************************************************
# Copyright (c) 2000, 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Sridhar Bidigalu (ICS)
#*******************************************************************************

# Makefile for SWT libraries on HP-UX

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# Define the installation directories for various products.
#    JAVA_HOME  - Sun's version of Java (JDK2)
#    MOTIF_HOME - Motif includes and libraries
#    CDE_HOME - CDE includes and libraries
JAVA_HOME  = /opt/jdk14101
MOTIF_HOME = /usr
CDE_HOME   = /usr/dt

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).sl
SWT_OBJ      = callback.o os.o os_structs.o os_custom.o
SWT_LIB      = -L$(MOTIF_HOME)/lib -L/usr/lib  \
	       -G -lXm -lXt -lX11 -lc -ldld -lm -lXp

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).sl
CDE_OBJ      = cde.o
CDE_LIB      = -G -L$(CDE_HOME)/lib -lDtSvc

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -Ae +z \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DNO_XINERAMA_EXTENSIONS \
	-D_HPUX -D_POSIX_C_SOURCE=199506L -DMOTIF -DCDE \
	-I./ \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/hp-ux \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include \
	+DAportable

all: make_swt make_cde

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -b -z -o $@ $(SWT_OBJ) $(SWT_LIB)

make_cde: $(CDE_DLL)

$(CDE_DLL): $(CDE_OBJ)
	ld -b -z -o $@ $(CDE_OBJ) $(CDE_LIB)

clean:
	rm -f *.sl *.o

