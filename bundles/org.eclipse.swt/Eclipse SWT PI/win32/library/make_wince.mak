# (c) Copyright IBM Corp., 2000, 2001
# All Rights Reserved.
#
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

CFLAGS = /nologo /c /W3 -DSWT_VERSION=$(SWT_VERSION) -DSWT_BUILD_NUM=$(bld_num) -DJ9WINCE -D_WIN32_WCE=300 -D"WIN32_PLATFORM_PSPC" /DUNDER_CE=300 /D"UNICODE" /D"_MBCS" /Zm200 -DARM -D_ARM_ -DFIXUP_UNALIGNED /I. /I$(JAVA_HOME)\include $*.c
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = $(dlllflags) /dll /entry:"_DllMainCRTStartup" /NODEFAULTLIB:libc.lib /nodefaultlib:oldnames.lib

SWT_OBJS = swt.obj structs.obj callback.obj # swtole.obj 

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

clean:
    del *.obj *.res *.dll *.lib *.exp
    
!ELSEIF "$(PLATFORM)" == "hpc2000"

CPU=ARM

LINK_LIBS =  corelibc.lib coredll.lib commdlg.lib commctrl.lib ceshell.lib

CFLAGS = /nologo /c /W3 -DSWT_VERSION=$(SWT_VERSION) -DSWT_BUILD_NUM=$(bld_num) -DJ9WINCE -D_WIN32_WCE=300 -D"WIN32_PLATFORM_HPC2000" /DUNDER_CE=300 /D"UNICODE" /D"_MBCS" /Zm200 -DARM -D_ARM_ -DFIXUP_UNALIGNED /I. /I$(JAVA_HOME)\include $*.c
RCFLAGS = -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)
LFLAGS = $(dlllflags) /dll /entry:"_DllMainCRTStartup" /NODEFAULTLIB:libc.lib /nodefaultlib:oldnames.lib

SWT_OBJS = swt.obj structs.obj callback.obj # swtole.obj 

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

clean:
    del *.obj *.res *.dll *.lib *.exp
    
!ENDIF