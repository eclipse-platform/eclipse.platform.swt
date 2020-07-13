#*******************************************************************************
# Copyright (c) 2000, 2018 IBM Corporation and others.
#
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Windows

# assumes these variables are set in the environment from which nmake is run
#	SWT_JAVA_HOME
#	OUTPUT_DIR

!include <make_common.mak>

SWT_PREFIX  = swt
WS_PREFIX   = win32
SWT_VERSION = $(maj_ver)$(min_ver)r$(rev)
SWT_LIB     = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
SWT_LIBS    = comctl32.lib shell32.lib imm32.lib oleacc.lib usp10.lib \
	wininet.lib Shlwapi.lib Uxtheme.lib Propsys.lib Urlmon.lib \
	Msimg32.lib
SWT_OBJS    = swt.obj callback.obj c.obj c_stats.obj \
	os.obj os_structs.obj os_custom.obj os_stats.obj \
	com_structs.obj com.obj com_stats.obj com_custom.obj

GDIP_PREFIX  = swt-gdip
GDIP_LIB     = $(GDIP_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
GDIP_LIBS    = gdiplus.lib
GDIP_OBJS    = gdip.obj gdip_structs.obj gdip_stats.obj gdip_custom.obj

AWT_PREFIX = swt-awt
AWT_LIB    = $(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
AWT_LIBS   = "$(SWT_JAVA_HOME)\lib\jawt.lib"
AWT_OBJS   = swt_awt.obj

CHROMIUM_PREFIX = swt-chromium
CHROMIUM_LIB    = $(CHROMIUM_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
CHROMIUM_LIBS   = $(CHROMIUM_TARGET)\chromium_swt_$(SWT_VERSION).dll.lib
CHROMIUM_OBJS   = chromiumlib.obj chromiumlib_stats.obj chromiumlib_custom.obj chromiumlib_structs.obj

WGL_PREFIX = swt-wgl
WGL_LIB    = $(WGL_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
WGL_LIBS   = opengl32.lib
WGL_OBJS   = wgl.obj wgl_structs.obj wgl_stats.obj

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#CFLAGS = $(cdebug) $(cflags) $(cvarsmt) $(CFLAGS) \
CFLAGS = -O1 -DNDEBUG -DUNICODE -D_UNICODE /c $(cflags) $(cvarsmt) $(CFLAGS) \
	-DSWT_VERSION=$(maj_ver)$(min_ver) -DSWT_REVISION=$(rev) $(NATIVE_STATS) -DUSE_ASSEMBLER \
	/I"$(SWT_JAVA_HOME)\include" /I"$(SWT_JAVA_HOME)\include\win32" /I.
	
CHROMIUMCFLAGS = $(CFLAGS) /I"$(CHROMIUM_HEADERS)"

RCFLAGS = $(rcflags) $(rcvars) $(RCFLAGS) -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver).$(rev).0\" -DSWT_COMMA_VERSION=$(comma_ver)
ldebug = /RELEASE  /INCREMENTAL:NO /NOLOGO
dlllflags = -dll
guilibsmt = kernel32.lib  ws2_32.lib mswsock.lib advapi32.lib bufferoverflowu.lib user32.lib gdi32.lib comdlg32.lib winspool.lib
olelibsmt = ole32.lib uuid.lib oleaut32.lib $(guilibsmt)

all: make_swt make_awt make_gdip make_wgl

chromiumlib_stats.obj: chromiumlib_stats.c
	cl $(CHROMIUMCFLAGS) chromiumlib_stats.c
chromiumlib_structs.obj: chromiumlib_structs.c
	cl $(CHROMIUMCFLAGS) chromiumlib_structs.c
chromiumlib_custom.obj: chromiumlib_custom.c
	cl $(CHROMIUMCFLAGS) chromiumlib_custom.c
chromiumlib.obj: chromiumlib.c
	cl $(CHROMIUMCFLAGS) chromiumlib.c

.c.obj:
	cl $(CFLAGS) $*.c

.cpp.obj:
	cl $(CFLAGS) $*.cpp

make_swt: $(SWT_OBJS) swt.res
	echo $(ldebug) $(dlllflags) $(olelibsmt) >templrf
	echo $(SWT_LIBS) >>templrf
	echo $(SWT_OBJS) >>templrf
	echo swt.res >>templrf
	echo -out:$(SWT_LIB) >>templrf
	link @templrf
	del templrf

make_gdip: $(GDIP_OBJS) swt_gdip.res
	echo $(ldebug) $(dlllflags) $(guilibsmt) >templrf
	echo $(GDIP_LIBS) >>templrf
	echo $(GDIP_OBJS) >>templrf
	echo swt_gdip.res >>templrf
	echo -out:$(GDIP_LIB) >>templrf
	link @templrf
	del templrf

make_awt: $(AWT_OBJS) swt_awt.res
	echo $(ldebug) $(dlllflags) $(guilibsmt) >templrf
	echo $(AWT_LIBS) >>templrf
	echo $(AWT_OBJS) >>templrf
	echo swt_awt.res >>templrf
	echo -out:$(AWT_LIB) >>templrf
	link @templrf
	del templrf

make_chromium: $(CHROMIUM_OBJS) swt_chromium.res
	echo $(ldebug) $(dlllflags) >templrf
	echo $(CHROMIUM_LIBS) >>templrf
	echo $(CHROMIUM_OBJS) >>templrf
	echo swt_chromium.res >>templrf
	echo -out:$(CHROMIUM_LIB) >>templrf
	link @templrf
	del templrf

make_wgl: $(WGL_OBJS) swt_wgl.res
	echo $(ldebug) $(dlllflags) $(guilibsmt) >templrf
	echo $(WGL_LIBS) >>templrf
	echo $(WGL_OBJS) >>templrf
	echo swt_wgl.res >>templrf
	echo -out:$(WGL_LIB) >>templrf
	link @templrf
	del templrf

swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

swt_gdip.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(GDIP_LIB)\" -r -fo swt_gdip.res swt_gdip.rc

swt_awt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(AWT_LIB)\" -r -fo swt_awt.res swt_awt.rc

swt_chromium.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(CHROMIUM_LIB)\" -r -fo swt_chromium.res swt_chromium.rc

swt_wgl.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(WGL_LIB)\" -r -fo swt_wgl.res swt_wgl.rc

install:
	copy *.dll "$(OUTPUT_DIR)"

chromium_cargo:
	cd chromium_subp && cargo build --release
	cd chromium_swt && cargo build --release
	mkdir $(CHROMIUM_OUTPUT_DIR)\chromium-$(cef_ver)
	copy chromium_subp\target\release\chromium_subp.exe $(CHROMIUM_OUTPUT_DIR)\chromium-$(cef_ver)\chromium_subp-$(SWT_VERSION).exe
	copy chromium_swt\target\release\chromium_swt_$(SWT_VERSION).dll $(CHROMIUM_OUTPUT_DIR)\chromium-$(cef_ver)
	
chromium_install: make_chromium
	copy $(CHROMIUM_PREFIX)*.dll "$(CHROMIUM_OUTPUT_DIR)\chromium-$(cef_ver)"

clean:
    del *.obj *.res *.dll *.lib *.exp
