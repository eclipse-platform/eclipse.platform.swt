# Makefile for creating SWT libraries on Linux

# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.

# The following environment variables are assumed to be defined:
#
# MAJOR_VER  - the major version number 
# MINOR_VER  - the minor version number 
# BUILD_NUM  - the build number

DLL_VERSION=$(MAJOR_VER)$(MINOR_VER)

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
SWT_DLL      = lib$(SWT_PREFIX)-$(OS_PREFIX)-$(DLL_VERSION).so
SWT_OBJ      = callback.o globals.o library.o structs.o swt.o 
SWT_LIB      = -L$(MOTIF_HOME) -G -bnoentry -lc_r -lC_r -lm -bexpall -lXm -lMrm -lXt -lX11 -lXext

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(OS_PREFIX)-$(DLL_VERSION).so
CDE_OBJ      = cde.o
CDE_LIB      = -L$(CDE_HOME)/lib -G -bnoentry -bexpall -lDtSvc

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -O -s \
	-DSWT_LIBRARY_MAJOR_VERSION=$(MAJOR_VER) \
	-DSWT_LIBRARY_MINOR_VERSION=$(MINOR_VER) \
	-DAIX -DMOTIF -DCDE -DNO_XPRINTING_EXTENSIONS \
	-q mbcs -qlanglvl=extended -qarch=ppc -qtune=604 \
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
	rm -f $(SWT_OBJ) $(SWT_DLL) $(CDE_OBJ) $(CDE_DLL)




