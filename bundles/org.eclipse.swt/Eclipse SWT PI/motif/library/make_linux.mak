#*******************************************************************************
# Copyright (c) 2000, 2009 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# This makefile expects the following environment variables set:
#    JAVA_HOME  - The JDK > 1.3
#    MOTIF_HOME - Motif includes and libraries

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX = swt
WS_PREFIX = motif
SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS = swt.o c.o c_stats.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	           -rpath . -x -shared -lX11 -lm -lXext -lXt -lXp -ldl -lXinerama -lXtst

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

CFLAGS = -O -Wall -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) -DUSE_ASSEMBLER -DLINUX -DMOTIF  -fpic \
	-I$(JAVA_HOME)/include -I$(MOTIF_HOME)/include -I/usr/X11R6/include

# Do not use pkg-config to get libs because it includes unnecessary dependencies (i.e. pangoxft-1.0)
GNOME_PREFIX = swt-gnome
GNOME_LIB = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_OBJECTS = swt.o gnome.o gnome_structs.o gnome_stats.o
GNOME_CFLAGS = -O -Wall -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) -DLINUX -DGTK -I$(JAVA_HOME)/include `pkg-config --cflags gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0`
GNOME_LIBS = -shared -fpic -fPIC `pkg-config --libs-only-L gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0` -lgnomevfs-2 -lgnome-2 -lgnomeui-2

AWT_PREFIX = swt-awt
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJS = swt_awt.o
AWT_LIBS = -L$(JAVA_HOME)/jre/bin -ljawt -shared

GTK_PREFIX = swt-gtk
GTK_LIB = lib$(GTK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GTK_OBJS = swt.o gtk.o
GTK_CFLAGS = `pkg-config --cflags gtk+-2.0`
GTK_LIBS = -x -shared `pkg-config --libs-only-L gtk+-2.0` -lgtk-x11-2.0

CAIRO_PREFIX = swt-cairo
CAIRO_LIB = lib$(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CAIRO_OBJECTS = swt.o cairo.o cairo_structs.o cairo_stats.o
CAIROCFLAGS = `pkg-config --cflags cairo`
CAIROLIBS = -shared -fpic -fPIC `pkg-config --libs-only-L cairo` -lcairo

MOZILLA_PREFIX = swt-mozilla$(GCC_VERSION)
MOZILLA_LIB = lib$(MOZILLA_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
MOZILLA_OBJECTS = swt.o xpcom.o xpcom_custom.o xpcom_structs.o xpcom_stats.o
MOZILLACFLAGS = -O \
	-DMOZILLA_STRICT_API=1 \
	-fno-rtti \
	-fno-exceptions \
	-Wall \
	-DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) \
	-Wno-non-virtual-dtor \
	-fPIC \
	-I./ \
	-I$(JAVA_HOME)/include \
	-I$(JAVA_HOME)/include/linux \
	${SWT_PTR_CFLAGS}
MOZILLALIBS = -shared -Wl,--version-script=mozilla_exports -Bsymbolic
MOZILLAEXCLUDES = -DNO__1XPCOMGlueShutdown \
	-DNO__1XPCOMGlueStartup \
	-DNO__1XPCOMGlueLoadXULFunctions \
	-DNO_memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I \
	-DNO_nsDynamicFunctionLoad_1sizeof \
	-DNO__1Call__IIIIII \
	-DNO_nsDynamicFunctionLoad
XULRUNNEREXCLUDES = -DNO__1NS_1InitXPCOM2

XULRUNNER_PREFIX = swt-xulrunner
XULRUNNER_LIB = lib$(XULRUNNER_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
XULRUNNER_OBJECTS = swt.o xpcomxul.o xpcomxul_custom.o xpcomxul_structs.o xpcomxul_stats.o

XPCOMINIT_PREFIX = swt-xpcominit
XPCOMINIT_LIB = lib$(XPCOMINIT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
XPCOMINIT_OBJECTS = swt.o xpcominit.o xpcominit_structs.o xpcominit_stats.o

GLX_PREFIX = swt-glx
GLX_LIB = lib$(GLX_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GLX_OBJECTS = swt.o glx.o glx_structs.o glx_stats.o
GLXCFLAGS = 
GLXLIBS = -shared -fpic -fPIC -L/usr/X11R6/lib -lGL -lGLU -lm

ifndef NO_STRIP
	CFLAGS := $(CFLAGS) -s
	CAIROLIBS := $(CAIROLIBS) -s
	MOZILLALIBS := $(MOZILLALIBS) -s
endif

all: make_swt make_awt make_gnome make_gtk make_glx

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	$(LD) -o $@ $(SWT_OBJS) $(SWT_LIBS)

swt.o: swt.c swt.h
	$(CC) $(CFLAGS) -c swt.c
os.o: os.c os.h swt.h os_custom.h
	$(CC) $(CFLAGS) -c os.c
os_structs.o: os_structs.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) -c os_structs.c 
os_stats.o: os_stats.c os_structs.h os.h os_stats.h swt.h
	$(CC) $(CFLAGS) -c os_stats.c

make_gnome: $(GNOME_LIB)

$(GNOME_LIB): $(GNOME_OBJECTS)
	gcc -o $@ $(GNOME_OBJECTS) $(GNOME_LIBS)

gnome.o: gnome.c
	gcc $(GNOME_CFLAGS) -c -o gnome.o gnome.c

gnome_structs.o: gnome_structs.c
	gcc $(GNOME_CFLAGS) -c -o gnome_structs.o gnome_structs.c

gnome_stats.o: gnome_stats.c
	gcc $(GNOME_CFLAGS) -c -o gnome_stats.o gnome_stats.c

make_awt: $(AWT_LIB)

$(AWT_LIB): $(AWT_OBJS)
	ld -o $@ $(AWT_OBJS) $(AWT_LIBS)

make_gtk: $(GTK_LIB)

$(GTK_LIB): $(GTK_OBJS)
	ld -o $@ $(GTK_OBJS) $(GTK_LIBS)

gtk.o: gtk.c
	$(CC) $(CFLAGS) $(GTK_CFLAGS) -c -o gtk.o gtk.c

make_cairo: $(CAIRO_LIB)

$(CAIRO_LIB): $(CAIRO_OBJECTS)
	$(LD) $(CAIROLIBS) -o $(CAIRO_LIB) $(CAIRO_OBJECTS)

cairo.o: cairo.c cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo.c
cairo_custom.o: cairo_custom.c cairo_structs.h cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_custom.c
cairo_structs.o: cairo_structs.c cairo_structs.h cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_structs.c
cairo_stats.o: cairo_stats.c cairo_structs.h cairo.h cairo_stats.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_stats.c

make_mozilla:$(MOZILLA_LIB)

$(MOZILLA_LIB): $(MOZILLA_OBJECTS)
	$(CXX) -o $(MOZILLA_LIB) $(MOZILLA_OBJECTS) $(MOZILLALIBS) ${MOZILLA_LIBS}

xpcom.o: xpcom.cpp
	$(CXX) $(MOZILLACFLAGS) $(MOZILLAEXCLUDES) ${MOZILLA_INCLUDES} -c xpcom.cpp
xpcom_structs.o: xpcom_structs.cpp
	$(CXX) $(MOZILLACFLAGS) $(MOZILLAEXCLUDES) ${MOZILLA_INCLUDES} -c xpcom_structs.cpp
xpcom_custom.o: xpcom_custom.cpp
	$(CXX) $(MOZILLACFLAGS) $(MOZILLAEXCLUDES) ${MOZILLA_INCLUDES} -c xpcom_custom.cpp
xpcom_stats.o: xpcom_stats.cpp
	$(CXX) $(MOZILLACFLAGS) $(MOZILLAEXCLUDES) ${MOZILLA_INCLUDES} -c xpcom_stats.cpp

#
# XULRunner lib
#
make_xulrunner:$(XULRUNNER_LIB)

$(XULRUNNER_LIB): $(XULRUNNER_OBJECTS)
	$(CXX) -o $(XULRUNNER_LIB) $(XULRUNNER_OBJECTS) $(MOZILLALIBS) ${XULRUNNER_LIBS}

xpcomxul.o: xpcom.cpp
	$(CXX) -o xpcomxul.o $(MOZILLACFLAGS) $(XULRUNNEREXCLUDES) ${XULRUNNER_INCLUDES} -c xpcom.cpp

xpcomxul_structs.o: xpcom_structs.cpp
	$(CXX) -o xpcomxul_structs.o $(MOZILLACFLAGS) $(XULRUNNEREXCLUDES) ${XULRUNNER_INCLUDES} -c xpcom_structs.cpp
	
xpcomxul_custom.o: xpcom_custom.cpp
	$(CXX) -o xpcomxul_custom.o $(MOZILLACFLAGS) $(XULRUNNEREXCLUDES) ${XULRUNNER_INCLUDES} -c xpcom_custom.cpp

xpcomxul_stats.o: xpcom_stats.cpp
	$(CXX) -o xpcomxul_stats.o $(MOZILLACFLAGS) $(XULRUNNEREXCLUDES) ${XULRUNNER_INCLUDES} -c xpcom_stats.cpp


#
# XPCOMInit lib
#
make_xpcominit:$(XPCOMINIT_LIB)

$(XPCOMINIT_LIB): $(XPCOMINIT_OBJECTS)
	$(CXX) -o $(XPCOMINIT_LIB) $(XPCOMINIT_OBJECTS) $(MOZILLALIBS) ${XULRUNNER_LIBS}

xpcominit.o: xpcominit.cpp
	$(CXX) $(MOZILLACFLAGS) ${XULRUNNER_INCLUDES} -c xpcominit.cpp

xpcominit_structs.o: xpcominit_structs.cpp
	$(CXX) $(MOZILLACFLAGS) ${XULRUNNER_INCLUDES} -c xpcominit_structs.cpp
	
xpcominit_stats.o: xpcominit_stats.cpp
	$(CXX) $(MOZILLACFLAGS) ${XULRUNNER_INCLUDES} -c xpcominit_stats.cpp

make_glx: $(GLX_LIB)

$(GLX_LIB): $(GLX_OBJECTS)
	$(LD) $(LIBS) $(GLXLIBS) -o $(GLX_LIB) $(GLX_OBJECTS)

glx.o: glx.c 
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx.c

glx_structs.o: glx_structs.c 
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_structs.c
	
glx_stats.o: glx_stats.c glx_stats.h
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_stats.c


install: all
	cp *.so $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl 

