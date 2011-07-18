#*******************************************************************************
# Copyright (c) 2000, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Windows CE

# assumes JAVA_HOME is set in the environment from which nmake is run

!include <make_common.mak>

SWT_PREFIX  = swt
WS_PREFIX   = win32
SWT_VERSION = $(maj_ver)$(min_ver)
SWT_LIB     = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll

!IF "$(PLATFORM)" == "MS Pocket PC"

CPU=ARM

LINK_LIBS =  aygshell.lib corelibc.lib coredll.lib commdlg.lib commctrl.lib ceshell.lib

CFLAGS = /nologo /c /W3 -O1 -DSWT_VERSION=$(SWT_VERSION) -DREDUCED_CALLBACKS -DJ9WINCE -D_WIN32_WCE=300 -D"WIN32_PLATFORM_PSPC" /DUNDER_CE=300 /D"UNICODE" /D"_MBCS" /Zm200 -DARM -D_ARM_ -DFIXUP_UNALIGNED /I. /I$(JAVA_HOME)\include $*.c
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver) -D"WIN32_PLATFORM_PSPC"
LFLAGS = $(dlllflags) /dll /entry:"_DllMainCRTStartup" /NODEFAULTLIB:libc.lib /nodefaultlib:oldnames.lib

SWT_OBJS = swt.obj c.obj c_stats.obj os.obj os_structs.obj os_custom.obj callback.obj

all: $(SWT_LIB)

.c.obj:
	clarm $(CFLAGS) $*.c
	
$(SWT_LIB): $(SWT_OBJS) swt.res
	link $(LFLAGS) $(LINK_LIBS) \
	-machine:$(CPU) \
	-subsystem:windowsce,3.00 \
	-out:$(SWT_LIB) \
	$(SWT_OBJS) swt.res
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

install: all
	copy *.dll $(OUTPUT_DIR)

clean:
    del *.obj *.res *.dll *.lib *.exp
    
!ELSEIF "$(PLATFORM)" == "hpc2000"

CPU=ARM

LINK_LIBS =  corelibc.lib coredll.lib commdlg.lib commctrl.lib ceshell.lib

CFLAGS = /nologo /c /W3 -O1 -DSWT_VERSION=$(SWT_VERSION) -DREDUCED_CALLBACKS -DJ9WINCE -D_WIN32_WCE=300 -D"WIN32_PLATFORM_HPC2000" /DUNDER_CE=300 /D"UNICODE" /D"_MBCS" /Zm200 -DARM -D_ARM_ -DFIXUP_UNALIGNED /I. /I$(JAVA_HOME)\include $*.c
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver) -D"WIN32_PLATFORM_HPC2000"
LFLAGS = $(dlllflags) /dll /entry:"_DllMainCRTStartup" /NODEFAULTLIB:libc.lib /nodefaultlib:oldnames.lib

SWT_OBJS = swt.obj structs.obj callback.obj

all: $(SWT_LIB)

.c.obj:
	clarm $(CFLAGS) $*.c
	
$(SWT_LIB): $(SWT_OBJS) swt.res
	link $(LFLAGS) $(LINK_LIBS) \
	-machine:$(CPU) \
	-subsystem:windowsce,3.00 \
	-out:$(SWT_LIB) \
	$(SWT_OBJS) swt.res
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

install: all
	copy *.dll $(OUTPUT_DIR)

clean:
    del *.obj *.res *.dll *.lib *.exp
    
!ELSEIF "$(PLATFORM)" == "SmartPhone 2002"

CPU=ARM

LINK_LIBS =  aygshell.lib corelibc.lib coredll.lib commctrl.lib ceshell.lib
CFLAGS = /nologo /c /W3 -O1 -DSWT_VERSION=$(SWT_VERSION) -DREDUCED_CALLBACKS -DJ9WINCE -D_WIN32_WCE=300 -D"WIN32_PLATFORM_WFSP=100" /DUNDER_CE=300 /D"UNICODE" /D"_MBCS" /Zm200 -DARM -D_ARM_ -DFIXUP_UNALIGNED /I. /I$(JAVA_HOME)\include $*.c
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver) -D"WIN32_PLATFORM_WFSP=100"
LFLAGS = $(dlllflags) /dll /entry:"_DllMainCRTStartup" /NODEFAULTLIB:libc.lib /nodefaultlib:oldnames.lib

SWT_OBJS = swt.obj structs.obj callback.obj 

all: $(SWT_LIB)

.c.obj:
	clarm $(CFLAGS) $*.c
	
$(SWT_LIB): $(SWT_OBJS) swt.res
	link $(LFLAGS) $(LINK_LIBS) \
	-machine:$(CPU) \
	-subsystem:windowsce,3.00 \
	-out:$(SWT_LIB) \
	$(SWT_OBJS) swt.res
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

install: all
	copy *.dll "$(OUTPUT_DIR)"

clean:
    del *.obj *.res *.dll *.lib *.exp
    
!ENDIF
