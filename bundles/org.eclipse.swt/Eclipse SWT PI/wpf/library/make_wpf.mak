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

# Makefile for SWT libraries on Windows

# assumes JAVA_HOME is set in the environment from which nmake is run

!include <make_common.mak>

SWT_PREFIX = swt
WS_PREFIX = wpf
SWT_VERSION = $(maj_ver)$(min_ver)
SWT_LIB = $(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).dll
SWT_WIN32_LIB = $(SWT_PREFIX)-win32-$(WS_PREFIX)-$(SWT_VERSION).dll
SWT_C_OBJS = swt.obj c.obj c_stats.obj
SWT_CPP_OBJS = os.obj os_custom.obj os_stats.obj os_structs.obj
SWT_WIN32_OBJS = win32.obj win32_stats.obj win32_structs.obj  

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

# Uncomment for try/catch exceptions
TRYCATCH = -DTRYCATCH

WPF_HOME = C:\Program Files\Reference Assemblies\Microsoft\Framework\v3.0
DOTNET_HOME = C:\WINDOWS\Microsoft.NET\Framework\v2.0.50727
CFLAGS = -c -W2 -D_WIN32_DCOM -O2 -DVISTA -DSWT_VERSION=$(SWT_VERSION) -DNO_getenv $(NATIVE_STATS) $(TRYCATCH) /I"$(JAVA_HOME)\include" /I"$(JAVA_HOME)\include\win32" /I.
CPPFLAGS = -clr /FU"$(WPF_HOME)\PresentationCore.dll" /FU"$(WPF_HOME)\PresentationFramework.dll" /FU$(DOTNET_HOME)\System.Data.dll /FU$(DOTNET_HOME)\System.dll /FU$(DOTNET_HOME)\System.Xml.dll /FU"$(WPF_HOME)\UIAutomationProvider.dll" /FU"$(WPF_HOME)\UIAutomationTypes.dll" /FU"$(WPF_HOME)\WindowsBase.dll" /FU$(DOTNET_HOME)\System.Drawing.dll /FU$(DOTNET_HOME)\System.Windows.Forms.dll  /FU"$(WPF_HOME)\WindowsFormsIntegration.dll"
LFLAGS = -DLL -NODEFAULTLIB:"LIBCMT.LIB" -CLRTHREADATTRIBUTE:STA 
WIN32LFLAGS = -DLL ole32.lib user32.lib gdi32.lib shell32.lib

all: $(SWT_LIB) $(SWT_WIN32_LIB)

.c.obj:
	cl $(CFLAGS) $*.c

.cpp.obj:
	cl $(CPPFLAGS) $(CFLAGS)  $*.cpp

$(SWT_LIB): $(SWT_C_OBJS) $(SWT_CPP_OBJS)
	link $(LFLAGS) -OUT:$(SWT_LIB) $(SWT_C_OBJS) $(SWT_CPP_OBJS)
	mt.exe -manifest $(SWT_LIB).manifest -outputresource:$(SWT_LIB);2
	
$(SWT_WIN32_LIB): $(SWT_C_OBJS) $(SWT_WIN32_OBJS)
	link $(WIN32LFLAGS) -OUT:$(SWT_WIN32_LIB) $(SWT_C_OBJS) $(SWT_WIN32_OBJS)

install: all
	copy *.dll "$(OUTPUT_DIR)"

clean:
    del *.obj *.res *.dll *.lib *.exp *.manifest
