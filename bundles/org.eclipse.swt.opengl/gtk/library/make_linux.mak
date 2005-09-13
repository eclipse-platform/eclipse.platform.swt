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
CC=gcc
LD=gcc

# Define the various shared libraries to be made.
WS_PREFIX   = gtk
GL_PREFIX   = gl
GL_DLL      = lib$(GL_PREFIX)-$(WS_PREFIX).so
GL_OBJ      = swt.o gl.o glu.o structs.o glx.o
GL_LIB      = -shared -L/usr/X11R6/lib -lGL -lGLU -lm

CFLAGS = -O2 -Wall -I.

all: make_gl

make_gl: $(GL_DLL)
	$(LD) $(GL_LIB) -o $(GL_DLL) $(GL_OBJ)

$(GL_DLL): $(GL_OBJ)
	$(CC) $(CFLAGS) -c glx.c

clean:
	rm -f *.so *.o
