#*******************************************************************************
# Copyright (c) 2003 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials 
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Define the installation directories for various products.
# Your system may have these in a different place.
#    IVE_HOME   - IBM's version of Java (J9)
IVE_HOME   = /bluebird/teamswt/swt-builddir/ive
JAVA_HOME=$(IVE_HOME)/bin/include

#  mozilla dist folder
MOZ_HOME = /Users/stephennorthover/Desktop/chrix/1.4/mozilla/dist

# Define the shared library to be made.
MOZ_LIB	      = libswtxpcom.jnilib
#MOZ_REQ_LIB = -lstring_s -lstring_obsolete_s -lembed_base_s -lunicharutil_s -lnspr4 -lxpcom -lplds4	-lplc4
#LDLIBS = -L$(MOZ_HOME)/bin $(MOZ_REQ_LIB) -L$(MOZ_HOME)/lib
LDLIBS = -L$(MOZ_HOME)/bin -lxpcom -L$(MOZ_HOME)/lib -lplds4 -lplc4 -lnspr4 -lpthread  -lgkgfx $(MOZ_HOME)/lib/libembed_base_s.a $(MOZ_HOME)/lib/libembedstring.a


MOZ_INCLUDES =  -I$(MOZ_HOME)/include \
	-I$(MOZ_HOME)/include/xpcom \
	-I$(MOZ_HOME)/include/string \
	-I$(MOZ_HOME)/include/nspr \
	-I$(MOZ_HOME)/include/embed_base

CFLAGS = -DXPCOM_GLUE  -DOSTYPE=\"Darwin6.0\" -DOSARCH=\"Darwin\" \
	-fPIC -fno-rtti -fno-exceptions -Wall -Wconversion -Wpointer-arith \
	-Wcast-align -Woverloaded-virtual -Wsynth -Wno-ctor-dtor-privacy \
	-Wno-long-long -fpascal-strings -traditional-cpp -fno-common \
	-I/Developer/Headers/FlatCarbon -F/System/Library/Frameworks \
	-include $(MOZ_HOME)/include/mozilla-config.h \
	-fshort-wchar -pipe  -DNDEBUG -DTRIMMED -O -DMOZILLA_CLIENT \
	$(MOZ_INCLUDES) \
	-DCARBON \
	-I /System/Library/Frameworks/JavaVM.framework/Versions/1.3.1/Headers

LDFLAGS = -fno-rtti -fno-exceptions -Wall -Wconversion -Wpointer-arith -Wcast-align \
	-Woverloaded-virtual -Wsynth -Wno-ctor-dtor-privacy -Wno-long-long -fpascal-strings \
	-traditional-cpp -fno-common \
	-I/Developer/Headers/FlatCarbon -F/System/Library/Frameworks \
	-fshort-wchar -pipe  -DNDEBUG -DTRIMMED -O -fPIC -arch ppc \
	-framework Carbon /System/Library/Frameworks/Carbon.framework/Carbon \
	-lm -bundle -framework JavaVM

all: $(MOZ_LIB)

$(MOZ_LIB): xpcom.o
	c++ $(LDFLAGS) -o $@ $^ $(LDLIBS)

xpcom.o: xpcom.cpp
	c++ -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o *.so
	