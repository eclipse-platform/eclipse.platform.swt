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
JAVA_HOME   = /bluebird/teamswt/swt-builddir/ive/bin

# Define the various shared libraries to be made.
SWT_PREFIX   = gl
WS_PREFIX    = motif
SWT_DLL      = lib$(SWT_PREFIX)-$(WS_PREFIX).so
SWT_OBJ      = swt.o gl.o glu.o structs.o glx.o
SWT_LIB      = -G -L/usr/lib -lm -lGL -lGLU

#
# The following CFLAGS are for compiling the SWT OpenGL library.
#
# Note:
#   The flag -xarch=generic ensure the compiled modules will be targeted
#   for 32-bit architectures.
#
CFLAGS = -O -s \
	-xarch=generic \
	-KPIC \
	-I./ \
	-I$(JAVA_HOME)/include

all: make_swt

make_swt: $(SWT_DLL)

$(SWT_DLL): $(SWT_OBJ)
	ld -o $@ $(SWT_OBJ) $(SWT_LIB)

clean:
	rm -f *.so *.o


