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

# Makefile for Mozilla SWT libraries on Windows

# assumes JAVA_HOME is set in the environment from which nmake is run
# assumes MOZ_HOME is set to dist folder of Mozilla
APPVER=5.0
!include <make_common.mak>
!include <win32.mak>

pgm_ver_str="SWT Mozilla $(maj_ver).$(min_ver) for Windows"
timestamp_str=__DATE__\" \"__TIME__\" (EST)\"
copyright = "Copyright (C) 2003 IBM Corp.  All rights reserved."

SWT_PREFIX=swt-mozilla
WS_PREFIX=win32
SWT_VERSION = $(maj_ver)$(min_ver)
MOZ_LIB     = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll

MOZ_REQ_LIB = nspr4.lib xpcom.lib embed_base_s.lib plds4.lib unicharutil_s.lib
LINK_LIBS = $(MOZ_REQ_LIB)

MOZ_CDEBUG = #-Zi -Odi
MOZ_LDEBUG = #/DEBUG /DEBUGTYPE:both
MOZ_INCLUDES = -I$(MOZ_HOME)/include \
	-I$(MOZ_HOME)/include/xpcom \
	-I$(MOZ_HOME)/include/string \
	-I$(MOZ_HOME)/include/nspr \
	-I$(MOZ_HOME)/include/embed_base \
	-I$(MOZ_HOME)/include/gfx
	
MOZ_CFLAGS = -DXP_WIN -DXP_WIN32
CFLAGS = -c -W3 -G6 -GD -O1 -DSWT_VERSION=$(SWT_VERSION) -DSWT_BUILD_NUM=$(bld_num) $(MOZ_CDEBUG) $(MOZ_CFLAGS) -nologo -D_X86_=1 -D_WIN32 -D_WIN95 -D_WIN32_WINDOWS=0x0400 -D_MT -MT -DWIN32 -D_WIN32_DCOM /I$(JAVA_HOME)\include /I. $(MOZ_INCLUDES)
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = /INCREMENTAL:NO /PDB:NONE /RELEASE /NOLOGO $(SWTMOZ_LDEBUG) -entry:_DllMainCRTStartup@12 /NODEFAULTLIB:MSVCRT.lib -dll /BASE:0x10000000 /comment:$(pgm_ver_str) /comment:$(copyright) /DLL /libpath:"$(MOZ_HOME)\lib"

MOZ_OBJS = xpcom.obj 

all: $(MOZ_LIB)

.cpp.obj:
	cl $(CFLAGS) $*.cpp

$(MOZ_LIB): $(MOZ_OBJS) xpcom.res
	echo $(LFLAGS) >templrf
	echo $(LINK_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(MOZ_LIB) >>templrf
	echo $(MOZ_OBJS) >>templrf
	echo xpcom.res >>templrf
	link @templrf
	del templrf

xpcom.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo xpcom.res xpcom.rc
	
clean:
    del *.obj *.res *.dll *.lib *.exp
