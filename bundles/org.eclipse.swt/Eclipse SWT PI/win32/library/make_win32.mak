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
SWT_OBJS    = swt.obj os.obj os_structs.obj os_custom.obj os_stats.obj callback.obj com_structs.obj com.obj com_stats.obj

GDIP_PREFIX  = swt-gdip
GDIP_LIB     = $(GDIP_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
GDIP_LIBS    = gdi32.lib gdiplus.lib
GDIP_OBJS    = gdip.obj gdip_structs.obj gdip_stats.obj gdip_custom.obj

AWT_PREFIX = swt-awt
AWT_LIB    = $(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
AWT_LIBS   = $(JAVA_HOME)\jre\bin\jawt.lib
AWT_OBJS   = swt_awt.obj

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

# Uncomment for debug flags
#SWT_CDEBUG = -Zi -Odi
#SWT_LDEBUG = /DEBUG /DEBUGTYPE:both

# note: thoroughly test all examples after changing any optimization flags
SWT_WINDOWS_SDK = -DWINVER=0x0500 -D_WIN32_WINDOWS=0x0400 -D_WIN32_WINNT=0x501 -D_WIN32_IE=0x0500
CFLAGS = -c -W3 -G6 -GD -O1 $(SWT_CDEBUG) -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) $(SWT_WINDOWS_SDK) -DVC_EXTRALEAN -nologo -MT -D_X86_=1 -DWIN32 -D_WIN32 -D_WIN32_DCOM /I$(JAVA_HOME)\include /I$(JAVA_HOME)\include\win32 /I.
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = /INCREMENTAL:NO /PDB:NONE /RELEASE /NOLOGO $(SWT_LDEBUG) -entry:_DllMainCRTStartup@12 -dll /BASE:0x10000000 /comment:$(pgm_ver_str) /comment:$(copyright) /DLL

all: $(SWT_LIB) $(AWT_LIB) $(GDIP_LIB)

.c.obj:
	cl $(CFLAGS) $*.c

.cpp.obj:
	cl $(CFLAGS) $*.cpp

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

$(GDIP_LIB): $(GDIP_OBJS) swt_gdip.res
	echo $(LFLAGS) >templrf
	echo $(GDIP_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(GDIP_LIB) >>templrf
	echo $(GDIP_OBJS) >>templrf
	echo swt_gdip.res >>templrf
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
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

swt_gdip.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(GDIP_LIB)\" -r -fo swt_gdip.res swt_gdip.rc

swt_awt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(AWT_LIB)\" -r -fo swt_awt.res swt_awt.rc

install: all
	copy *.dll $(OUTPUT_DIR)

clean:
    del *.obj *.res *.dll *.lib *.exp
