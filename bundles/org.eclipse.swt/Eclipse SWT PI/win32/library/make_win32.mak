#*******************************************************************************
# Copyright (c) 2000, 2011 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for SWT libraries on Windows

# assumes these variables are set in the environment from which nmake is run
#	JAVA_HOME
#	OUTPUT_DIR

!include <make_common.mak>
!include <win32.mak>

SWT_PREFIX  = swt
WS_PREFIX   = win32
SWT_VERSION = $(maj_ver)$(min_ver)
SWT_LIB     = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
SWT_LIBS    = comctl32.lib shell32.lib imm32.lib oleacc.lib usp10.lib wininet.lib Crypt32.lib
SWT_OBJS    = swt.obj callback.obj c.obj c_stats.obj \
	os.obj os_structs.obj os_custom.obj os_stats.obj \
	com_structs.obj com.obj com_stats.obj com_custom.obj

GDIP_PREFIX  = swt-gdip
GDIP_LIB     = $(GDIP_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
GDIP_LIBS    = gdiplus.lib
GDIP_OBJS    = gdip.obj gdip_structs.obj gdip_stats.obj gdip_custom.obj

AWT_PREFIX = swt-awt
AWT_LIB    = $(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
AWT_LIBS   = "$(JAVA_HOME)\jre\bin\jawt.lib"
AWT_OBJS   = swt_awt.obj

WEBKIT_DIR 	  = S:\swt-builddir\webkit\WebKit-r72896
WEBKIT_SUPPORT_DIR = S:\swt-builddir\webkit\WebKitSupportLibrary
WEBKIT_PREFIX = swt-webkit
WEBKIT_LIB    = $(WEBKIT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
WEBKIT_LIBS   = $(WEBKIT_DIR)\lib\webkit.lib $(WEBKIT_SUPPORT_DIR)\win\lib\CFNetwork.lib $(WEBKIT_SUPPORT_DIR)\win\lib\CoreFoundation.lib
WEBKIT_OBJS   = webkit_win32.obj webkit_win32_stats.obj webkit_win32_custom.obj webkit_win32_structs.obj

WGL_PREFIX = swt-wgl
WGL_LIB    = $(WGL_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
WGL_LIBS   = opengl32.lib
WGL_OBJS   = wgl.obj wgl_structs.obj wgl_stats.obj

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

XULRUNNER_PREFIX = swt-xulrunner
XULRUNNER_LIB = $(XULRUNNER_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
XULRUNNER_LIBS = Advapi32.lib $(XULRUNNER_SDK)\lib\xpcomglue.lib
XULRUNNER_OBJS = xpcom.obj xpcom_custom.obj xpcom_structs.obj xpcom_stats.obj xpcominit.obj xpcominit_structs.obj xpcominit_stats.obj

MOZILLACFLAGS = -c \
	-Zc:wchar_t \
	-O1 \
	-DSWT_VERSION=$(SWT_VERSION) \
	$(NATIVE_STATS) \
	-MD \
	-DMOZILLA_STRICT_API=1 \
	-W3 \
	-I. \
	-I"$(JAVA_HOME)/include" \
	-I"$(JAVA_HOME)/include/win32" \
	-I"$(XULRUNNER_SDK)\include\mozilla-config.h" -I"$(XULRUNNER_SDK)\include"

WEBKITCFLAGS = -c -O1\
	-DSWT_VERSION=$(SWT_VERSION) \
	$(NATIVE_STATS) \
	-I"$(JAVA_HOME)\include" -I"$(JAVA_HOME)\include\win32" \
	-I"$(WEBKIT_DIR)" \
	-I"$(WEBKIT_DIR)\WebKit\win" \
	-I"$(WEBKIT_DIR)\JavaScriptCore\ForwardingHeaders" \
	-I"$(WEBKIT_SUPPORT_DIR)\win\include"

#CFLAGS = $(cdebug) $(cflags) $(cvarsmt) $(CFLAGS) \
CFLAGS = -O1 -DNDEBUG $(cflags) $(cvarsmt) $(CFLAGS) \
	-DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) -DUSE_ASSEMBLER \
	/I"$(JAVA_HOME)\include" /I"$(JAVA_HOME)\include\win32" /I.
RCFLAGS = $(rcflags) $(rcvars) $(RCFLAGS) -DSWT_FILE_VERSION=\"$(maj_ver).$(min_ver)\" -DSWT_COMMA_VERSION=$(comma_ver)

all: make_swt make_awt make_gdip make_wgl

webkit_win32_custom.obj: webkit_win32_custom.cpp
	cl $(WEBKITCFLAGS) webkit_win32_custom.cpp
webkit_win32_stats.obj: webkit_win32_stats.cpp
	cl $(WEBKITCFLAGS) webkit_win32_stats.cpp
webkit_win32_structs.obj: webkit_win32_structs.cpp
	cl $(WEBKITCFLAGS) webkit_win32_structs.cpp
webkit_win32.obj: webkit_win32.cpp
	cl $(WEBKITCFLAGS) webkit_win32.cpp
	
xpcom_custom.obj: xpcom_custom.cpp
	cl $(MOZILLACFLAGS) xpcom_custom.cpp
xpcom_stats.obj: xpcom_stats.cpp
	cl $(MOZILLACFLAGS) xpcom_stats.cpp
xpcom_structs.obj: xpcom_structs.cpp
	cl $(MOZILLACFLAGS) xpcom_structs.cpp
xpcom.obj: xpcom.cpp
	cl $(MOZILLACFLAGS) xpcom.cpp
xpcominit_stats.obj: xpcominit_stats.cpp
	cl $(MOZILLACFLAGS) xpcominit_stats.cpp
xpcominit_structs.obj: xpcominit_structs.cpp
	cl $(MOZILLACFLAGS) xpcominit_structs.cpp
xpcominit.obj: xpcominit.cpp
	cl $(MOZILLACFLAGS) xpcominit.cpp

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

make_webkit: $(WEBKIT_OBJS) swt_webkit.res
	echo $(ldebug) $(dlllflags) >templrf
	echo $(WEBKIT_LIBS) >>templrf
	echo $(WEBKIT_OBJS) >>templrf
	echo swt_webkit.res >>templrf
	echo -out:$(WEBKIT_LIB) >>templrf
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
	
make_xulrunner: $(XULRUNNER_OBJS) swt_xpcom.res
	echo $(ldebug) $(dlllflags) >templrf
	echo $(XULRUNNER_LIBS) >>templrf
	echo $(XULRUNNER_OBJS) >>templrf
	echo swt_xpcom.res >>templrf
	echo -out:$(XULRUNNER_LIB) >>templrf
	link @templrf
	del templrf
	
swt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(SWT_LIB)\" -r -fo swt.res swt.rc

swt_gdip.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(GDIP_LIB)\" -r -fo swt_gdip.res swt_gdip.rc

swt_awt.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(AWT_LIB)\" -r -fo swt_awt.res swt_awt.rc

swt_webkit.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(WEBKIT_LIB)\" -r -fo swt_webkit.res swt_webkit.rc

swt_wgl.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(WGL_LIB)\" -r -fo swt_wgl.res swt_wgl.rc

swt_xpcom.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(XULRUNNER_LIB)\" -r -fo swt_xpcom.res swt_xpcom.rc

swt_xpcominit.res:
	rc $(RCFLAGS) -DSWT_ORG_FILENAME=\"$(XPCOMINIT_LIB)\" -r -fo swt_xpcom.res swt_xpcom.rc

install:
	copy *.dll "$(OUTPUT_DIR)"

clean:
    del *.obj *.res *.dll *.lib *.exp
