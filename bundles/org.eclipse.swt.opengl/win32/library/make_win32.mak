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

# assumes JAVA_HOME is set in the environment from which nmake is run

APPVER=5.0
!include <win32.mak>

pgm_ver_str="SWT $(maj_ver).$(min_ver) for Windows"
timestamp_str=__DATE__\" \"__TIME__\" (EST)\"
copyright = "Copyright (C) 1999, 2003 IBM Corp.  All rights reserved."

SWT_PREFIX=gl
WS_PREFIX=win32
SWT_LIB     = $(SWT_PREFIX)-$(WS_PREFIX).dll

LINK_LIBS = opengl32.lib glu32.lib gdi32.lib

# note: thoroughly test all examples after changing any optimization flags
SWT_CDEBUG = #-Zi -Odi
SWT_LDEBUG = #/DEBUG /DEBUGTYPE:both
CFLAGS = -c -W3 -G6 -GD -O1 $(SWT_CDEBUG) -nologo -D_X86_=1 -D_WIN32 -D_WIN95 -D_WIN32_WINDOWS=0x0400 -D_MT -MT -DWIN32 -D_WIN32_DCOM /I$(JAVA_HOME)\include /I.
RCFLAGS = -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = /INCREMENTAL:NO /PDB:NONE /RELEASE /NOLOGO $(SWT_LDEBUG) -entry:_DllMainCRTStartup@12 -dll /BASE:0x10000000 /comment:$(pgm_ver_str) /comment:$(copyright) /DLL

SWT_OBJS = swt.obj gl.obj glu.obj glw.obj structs.obj

all: $(SWT_LIB)

.c.obj:
	cl $(CFLAGS) $*.c

$(SWT_LIB): $(SWT_OBJS) swt.res
	echo $(LFLAGS) >templrf
	echo $(LINK_LIBS) >>templrf
	echo -machine:IX86 >>templrf
	echo -subsystem:windows >>templrf
	echo -out:$(SWT_LIB) >>templrf
	echo $(SWT_OBJS) >>templrf
	echo swt.res >>templrf
	link @templrf
	del templrf
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc
	
clean:
    del *.obj *.res *.dll *.lib *.exp
