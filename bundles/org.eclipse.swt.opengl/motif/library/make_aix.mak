#*******************************************************************************
# Copyright (c) 2000, 2005 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at 
# http://www.eclipse.org/legal/epl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Define the installation directories for various products.
#    JAVA_HOME  - The JDK > 1.3
JAVA_HOME   = /usr/java131

# Define the various shared libraries to be made.
SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX).so
SWT_OBJ      = swt.o gl.o glu.o structs.o glx.o
SWT_LIB      = -G -bnoentry -lc_r -lC_r -lm -bexpall -lMrm -lX11 -lXext -liconv -lGL -lGLU

#
# The following CFLAGS are for compiling the SWT OpenGL library.
#
CFLAGS = -O -s \
	-DAIX \
	-DNO_XINERAMA_EXTENSIONS \
	-q mbcs -qlanglvl=extended -qmaxmem=8192 \
	-I$(JAVA_HOME)/include

all: make_swt

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld $(SWT_LIB) -o $(SWT_DLL) $(SWT_OBJ)

clean:
	rm -f *.o *.so *.a
