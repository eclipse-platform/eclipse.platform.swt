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

# Makefile for creating SWT Mozilla libraries for Linux GTK

include make_common.mak

SWT_VERSION = $(maj_ver)$(min_ver)

# Define the installation directories for various products.
# Your system may have these in a different place.
# JAVA_HOME   - IBM's version of Java (J9)
JAVA_HOME =/bluebird/teamswt/swt-builddir/ive/bin

#  mozilla dist folder
MOZ_HOME = /usr/local/mozilla2swt/mozilla1.4/mozilla/dist

# Define the shared library to be made.
WS_PREFIX = gtk
MOZ_PREFIX = swt-mozilla
MOZ_LIB = lib$(MOZ_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so

MOZ_REQ_LIB = -lstring_s -lstring_obsolete_s -lembed_base_s -lunicharutil_s -lnspr4 -lxpcom -lplds4	
LDLIBS = -L$(MOZ_HOME)/lib $(MOZ_REQ_LIB)

MOZ_INCLUDES =  -I$(MOZ_HOME)/include \
	-I$(MOZ_HOME)/include/xpcom \
	-I$(MOZ_HOME)/include/string \
	-I$(MOZ_HOME)/include/nspr \
	-I$(MOZ_HOME)/include/embed_base

CFLAGS = -O \
	-fPIC \
	-fno-rtti	\
	-Wall	\
	-I./ \
	-I$(JAVA_HOME)	\
	-include $(MOZ_HOME)/include/mozilla-config.h \
	$(MOZ_INCLUDES)

# Default location of mozilla libraries for supported versions
LDFLAGS_DEFAULT_MOZILLA_PATH = -Xlinker -rpath -Xlinker /usr/lib/mozilla-1.4

LDFLAGS = -s -shared -fPIC $(LDFLAGS_DEFAULT_MOZILLA_PATH)

all: $(MOZ_LIB)

$(MOZ_LIB): xpcom.o
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

xpcom.o: xpcom.cpp
	$(CXX) -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o *.so
	