# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
# Makefile for creating SWT libraries on AIX

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

CC=cc_r

# Define the installation directories for various products.
#    JAVA_HOME   - IBM's version of Java
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries
JAVA_HOME   = /usr/java131
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
CDE_HOME   = /usr/dt

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
SWT_OBJ      = callback.o structs.o swt.o 
SWT_LIB      = -L$(MOTIF_HOME) -G -bnoentry -lc_r -lC_r -lm -bexpall -lXm -lMrm -lXt -lX11 -lXext -liconv

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
CDE_OBJ      = cde.o
CDE_LIB      = -L$(CDE_HOME)/lib -bnoentry -bexpall -lDtSvc -lc

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DAIX -DMOTIF -DCDE -DNO_XPRINTING_EXTENSIONS \
	-q mbcs -qlanglvl=extended -qmaxmem=8192 \
	-I$(JAVA_HOME)/include \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include

all: make_swt

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld $(SWT_LIB) -o $(SWT_DLL) $(SWT_OBJ)

make_cde: $(CDE_DLL)

$(CDE_DLL): $(CDE_OBJ)
	ld -o $@ $(CDE_OBJ) $(CDE_LIB)


clean:
	rm -f *.o *.so *.a
