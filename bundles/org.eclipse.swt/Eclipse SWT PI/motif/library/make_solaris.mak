# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
# Makefile for SWT libraries on Solaris

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)


# Define the installation directories for various products.
#    JAVA_HOME  - Sun's version of Java (JDK2)
#    MOTIF_HOME - Motif includes and libraries
#    CDE_HOME - CDE includes and libraries
JAVA_HOME  = /tools/java1.3
MOTIF_HOME = /usr/dt
CDE_HOME   = /usr/dt


# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
OS_PREFIX    = solaris
SWT_DLL      = lib$(SWT_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
SWT_OBJ      = callback.o structs.o swt.o 
SWT_LIB      = -L$(MOTIF_HOME)/lib -L/usr/lib  \
	       -G -lXm -lXt -lX11 -lm

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(OS_PREFIX)-$(SWT_VERSION).so
CDE_OBJ      = cde.o
CDE_LIB      = -G -L$(CDE_HOME)/lib -lDtSvc


#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
# Note:
#   The flag -xarch=generic ensure the compiled modules will be targeted
#   for 32-bit architectures. If this flag is not
#
CFLAGS = -O -s \
	-xarch=generic \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DSOLARIS -DMOTIF -DCDE \
	-KPIC \
	-I./ \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/solaris \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include


all: make_swt make_cde

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -o $@ $(SWT_OBJ) $(SWT_LIB)


make_cde: $(CDE_DLL)

$(CDE_DLL): $(CDE_OBJ)
	ld -o $@ $(CDE_OBJ) $(CDE_LIB)


clean:
	rm -f *.so *.o

