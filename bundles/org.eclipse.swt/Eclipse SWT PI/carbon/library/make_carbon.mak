#*******************************************************************************
# Copyright (c) 2000, 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Carbon/Mac

include make_common.mak

SWT_PREFIX=swt
WS_PREFIX=carbon
SWT_VERSION=$(maj_ver)$(min_ver)
SWT_LIB=lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

MOZILLA_PREFIX=swt-mozilla
MOZILLA_LIB=lib$(MOZILLA_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).jnilib

#  mozilla dist folder
MOZILLA_HOME = /mozilla/mozilla/1.4/carbon/mozilla/dist

DEBUG =  
CFLAGS = -c -DSWT_VERSION=$(SWT_VERSION) $(DEBUG) -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Versions/1.3.1/Headers
LFLAGS = -bundle -framework JavaVM -framework Carbon 

MOZILLALDLIBS = -L$(MOZILLA_HOME)/bin -lxpcom -L$(MOZILLA_HOME)/lib -lplds4 -lplc4 -lnspr4 -lpthread  -lgkgfx $(MOZILLA_HOME)/lib/libembed_base_s.a $(MOZILLA_HOME)/lib/libembedstring.a

MOZILLA_INCLUDES =  -I$(MOZILLA_HOME)/include \
	-I$(MOZILLA_HOME)/include/xpcom \
	-I$(MOZILLA_HOME)/include/string \
	-I$(MOZILLA_HOME)/include/nspr \
	-I$(MOZILLA_HOME)/include/embed_base \
	-I$(MOZILLA_HOME)/include/gfx
MOZILLACFLAGS = -DXPCOM_GLUE  -DOSTYPE=\"Darwin6.0\" -DOSARCH=\"Darwin\" \
	-fPIC -fno-rtti -fno-exceptions -Wall -Wconversion -Wpointer-arith \
	-Wcast-align -Woverloaded-virtual -Wsynth -Wno-ctor-dtor-privacy \
	-Wno-long-long -fpascal-strings -traditional-cpp -fno-common \
	-I/Developer/Headers/FlatCarbon -F/System/Library/Frameworks \
	-include $(MOZILLA_HOME)/include/mozilla-config.h \
	-fshort-wchar -pipe  -DNDEBUG -DTRIMMED -O -DMOZILLA_CLIENT \
	$(MOZILLA_INCLUDES) \
	-DCARBON \
	-I /System/Library/Frameworks/JavaVM.framework/Versions/1.3.1/Headers
MOZILLALDFLAGS = -fno-rtti -fno-exceptions -Wall -Wconversion -Wpointer-arith -Wcast-align \
	-Woverloaded-virtual -Wsynth -Wno-ctor-dtor-privacy -Wno-long-long -fpascal-strings \
	-traditional-cpp -fno-common \
	-I/Developer/Headers/FlatCarbon -F/System/Library/Frameworks \
	-fshort-wchar -pipe  -DNDEBUG -DTRIMMED -O -fPIC -arch ppc \
	-framework Carbon /System/Library/Frameworks/Carbon.framework/Carbon \
	-lm -bundle -framework JavaVM

SWT_OBJS = os.o os_custom.o os_structs.o callback.o

all: $(SWT_LIB) #$(MOZILLA_LIB)

.c.o:
	cc $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJS)
	cc -o $(SWT_LIB)  $(LFLAGS) $(SWT_OBJS)

$(MOZILLA_LIB): xpcom.o
	c++ $(MOZILLALDFLAGS) -o $@ $^ $(MOZILLALDLIBS)

xpcom.o: xpcom.cpp
	c++ -c $(MOZILLACFLAGS) $< -o $@
	
clean:
	rm -f *.jnilib *.o