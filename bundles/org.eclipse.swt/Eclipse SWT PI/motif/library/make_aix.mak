# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
# Makefile for creating SWT libraries on AIX

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

CC=cc_r

# Define the installation directories for various products.
#    IVE_HOME   - IBM's version of Java (J9)
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive/bin
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
CDE_HOME   = /usr/dt

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
OS_PREFIX    = aix
SWT_DLL      = lib$(SWT_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
SWT_OBJ      = callback.o structs.o swt.o 
SWT_LIB      = -L$(MOTIF_HOME) -G -bnoentry -lc_r -lC_r -lm -bexpall -lXm -lMrm -lXt -lX11 -lXext

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
CDE_OBJ      = cde.o
CDE_LIB      = -L$(CDE_HOME)/lib -G -bnoentry -bexpall -lDtSvc

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DAIX -DMOTIF -DCDE -DNO_XPRINTING_EXTENSIONS \
	-q mbcs -qlanglvl=extended -qarch=ppc -qtune=604 -qmaxmem=8192 \
	-I$(IVE_HOME)/include \
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
	rm -f *.o *.so
