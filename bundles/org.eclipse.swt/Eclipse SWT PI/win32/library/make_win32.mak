#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Windows

# assumes JAVA_HOME is set in the environment from which nmake is run

APPVER=5.0
!include <make_common.mak>
!include <win32.mak>

pgm_ver_str="SWT $(maj_ver).$(min_ver) for Windows"
timestamp_str=__DATE__\" \"__TIME__\" (EST)\"
copyright = "Copyright (C) 1999, 2004 IBM Corp.  All rights reserved."

SWT_PREFIX  = swt
WS_PREFIX   = win32
SWT_VERSION = $(maj_ver)$(min_ver)
SWT_LIB     = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
SWT_LIBS    = ole32.lib comctl32.lib user32.lib gdi32.lib comdlg32.lib kernel32.lib shell32.lib oleaut32.lib advapi32.lib imm32.lib winspool.lib oleacc.lib usp10.lib
SWT_OBJS    = swt.obj os.obj os_structs.obj os_custom.obj os_stats.obj callback.obj com_structs.obj com.obj 

AWT_PREFIX = swt-awt
AWT_LIB    = $(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
AWT_LIBS   = $(JAVA_HOME)\jre\bin\jawt.lib
AWT_OBJS   = swt_awt.obj

MOZILLA_PREFIX	= swt-mozilla
MOZILLA_LIB     = $(MOZILLA_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
MOZILLA_LIBS	= nspr4.lib xpcom.lib embed_base_s.lib
MOZILLA_OBJS	= xpcom.obj

# note: thoroughly test all examples after changing any optimization flags
SWT_CDEBUG = #-Zi -Odi
SWT_LDEBUG = #/DEBUG /DEBUGTYPE:both
CFLAGS = -c -W3 -G6 -GD -O1 $(SWT_CDEBUG) -DSWT_VERSION=$(SWT_VERSION) -DSWT_BUILD_NUM=$(bld_num) -nologo -D_X86_=1 -D_WIN32 -D_WIN95 -D_WIN32_WINDOWS=0x0400 -D_MT -MT -DWIN32 -D_WIN32_DCOM /I$(JAVA_HOME)\include /I$(JAVA_HOME)\include\win32 /I.
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = /INCREMENTAL:NO /PDB:NONE /RELEASE /NOLOGO $(SWT_LDEBUG) -entry:_DllMainCRTStartup@12 -dll /BASE:0x10000000 /comment:$(pgm_ver_str) /comment:$(copyright) /DLL

# Uncomment for native stats
#CFLAGS = $(CFLAGS) -DNATIVE_STATS

MOZILLA_INCLUDES = -I$(MOZILLA_HOME)\include \
	-I$(MOZILLA_HOME)\include\xpcom \
	-I$(MOZILLA_HOME)\include\string \
	-I$(MOZILLA_HOME)\include\nspr \
	-I$(MOZILLA_HOME)\include\embed_base \
	-I$(MOZILLA_HOME)\include\gfx
MOZILLACFLAGS = $(CFLAGS) -DXP_WIN -DXP_WIN32 $(MOZILLA_INCLUDES)
MOZILLALFLAGS = $(LFLAGS) /NODEFAULTLIB:MSVCRT.lib /libpath:"$(MOZILLA_HOME)\lib"

all: $(SWT_LIB) $(AWT_LIB) #$(MOZILLA_LIB)

.c.obj:
	cl $(CFLAGS) $*.c

.cpp.obj:
	cl $(MOZILLACFLAGS) $*.cpp

$(SWT_LIB): $(SWT_OBJS) swt.res
	echo $(LFLAGS) >templrf
	echo $(SWT_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(SWT_LIB) >>templrf
	echo $(SWT_OBJS) >>templrf
	echo swt.res >>templrf
	link @templrf
	del templrf

$(AWT_LIB): $(AWT_OBJS) swt_awt.res
	echo $(LFLAGS) >templrf
	echo $(AWT_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(AWT_LIB) >>templrf
	echo $(AWT_OBJS) >>templrf
	echo swt_awt.res >>templrf
	link @templrf
	del templrf

$(MOZILLA_LIB): $(MOZILLA_OBJS) xpcom.res
	echo $(MOZILLALFLAGS) >templrf
	echo $(MOZILLA_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(MOZILLA_LIB) >>templrf
	echo $(MOZILLA_OBJS) >>templrf
	echo xpcom.res >>templrf
	link @templrf
	del templrf
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

swt_awt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(AWT_LIB)\" -r -fo swt_awt.res swt_awt.rc

xpcom.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(MOZILLA_LIB)\" -r -fo xpcom.res xpcom.rc
	

clean:
    del *.obj *.res *.dll *.lib *.exp
