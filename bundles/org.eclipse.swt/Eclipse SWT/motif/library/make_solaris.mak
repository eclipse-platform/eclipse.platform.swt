# Makefile for creating SWT libraries on Solaris

# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.

# The following environment variables are assumed to be defined:
#
# MAJOR_VER  - the major version number 
# MINOR_VER  - the minor version number 
# BUILD_NUM  - the build number

DLL_VERSION=$(MAJOR_VER)$(MINOR_VER)


# Define the installation directories for various products.
#    JAVA_HOME  - Sun's version of Java (JDK2)
#    MOTIF_HOME - Motif includes and libraries
#    CDE_HOME - CDE includes and libraries
JAVA_HOME  = /tools/java1.3
MOTIF_HOME = /usr/dt
CDE_HOME   = /usr/dt


# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
OS_PREFIX	 = solaris
SWT_DLL      = lib$(SWT_PREFIX)-$(OS_PREFIX)-$(DLL_VERSION).so
SWT_OBJ      = callback.o globals.o library.o structs.o swt.o 
SWT_LIB      = -L$(MOTIF_HOME)/lib -L/usr/lib  \
	       -G -lXm -lXt -lX11 -lm

CDE_PREFIX   = swt-cde
CDE_DLL      = lib$(CDE_PREFIX)-$(OS_PREFIX)-$(DLL_VERSION).so
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
CFLAGS = -O -s -v \
	-xarch=generic \
	-DSWT_LIBRARY_MAJOR_VERSION=$(MAJOR_VER) \
	-DSWT_LIBRARY_MINOR_VERSION=$(MINOR_VER) \
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
	rm -f $(SWT_OBJ) $(SWT_DLL) $(CDE_OBJ) $(CDE_DLL)
