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
JAVA_HOME = /bluebird/teamswt/swt-builddir/ive/bin

# Define the various shared libraries to be made.
WS_PREFIX    = gtk
GL_PREFIX   = gl
GL_DLL      = lib$(GL_PREFIX)-$(WS_PREFIX).so
GL_OBJ      = swt.o gl.o glu.o structs.o glx.o
GL_LIB      = -shared -L/usr/X11R6/lib -lGL -lGLU -lm
XGTK_PREFIX   = xgtk
XGTK_DLL      = lib$(XGTK_PREFIX)-$(WS_PREFIX).so
XGTK_OBJ      = xgtk.o
XGTK_LIB      = -shared  `pkg-config --libs gtk+-2.0 gthread-2.0`

#
# The following CFLAGS are for compiling the SWT OpenGL libraries.
#
CFLAGS =  -O -Wall \
	-I./ \
	-I$(JAVA_HOME)/include \
	`pkg-config --cflags gtk+-2.0`

all: make_gl make_xgtk

make_gl: $(GL_DLL)
	$(LD) $(GL_LIB) -o $(GL_DLL) $(GL_OBJ)

$(GL_DLL): $(GL_OBJ)
	$(CC) $(CFLAGS) -c glx.c

make_xgtk: $(XGTK_DLL)
	$(LD) $(XGTK_LIB) -o $(XGTK_DLL) $(XGTK_OBJ)

$(XGTK_DLL): $(XGTK_OBJ)
	$(CC) $(CFLAGS) -c xgtk.c

clean:
	rm -f *.so *.o
