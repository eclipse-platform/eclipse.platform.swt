@rem ***************************************************************************
@rem Copyright (c) 2000, 2017 IBM Corporation and others.
@rem All rights reserved. This program and the accompanying materials
@rem are made available under the terms of the Eclipse Public License v1.0
@rem which accompanies this distribution, and is available at
@rem http://www.eclipse.org/legal/epl-v10.html
@rem
@rem Contributors:
@rem      IBM Corporation - initial API and implementation
@rem ***************************************************************************

@echo off

IF EXIST C:\BUILD\swt-builddir set "SWT_BUILDDIR=C:\BUILD\swt-builddir"
IF "x.%SWT_BUILDDIR%"=="x." set "SWT_BUILDDIR=S:\swt-builddir"
echo SWT build dir: %SWT_BUILDDIR%

IF "x.%MSSDK%"=="x." set "MSSDK=%SWT_BUILDDIR%\MSSDKs\Windows Server 2003 SP1 SDK"
IF "x.%WEBKIT_DIR%"=="x." set "WEBKIT_DIR=%SWT_BUILDDIR%\webkit\WebKit-r72896"
IF "x.%WEBKIT_SUPPORT_DIR%"=="x." set "WEBKIT_SUPPORT_DIR=%SWT_BUILDDIR%\webkit\WebKitSupportLibrary"
IF "x.%MSVC_HOME%"=="x." set "MSVC_HOME=%SWT_BUILDDIR%\MSVCs\Microsoft Visual Studio 9.0"

IF x.%1==x.x86 GOTO X86
IF x.%1==x.x86_64 GOTO X86_64

:X86
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86
IF "x.%SWT_JAVA_HOME%"=="x." set "SWT_JAVA_HOME=%SWT_BUILDDIR%\ibm-java-sdk-80-win-i386\sdk"
call "%MSVC_HOME%\Common7\Tools\vsvars32.bat"
call "%MSSDK%\setenv" /XP32 /RETAIL
IF x.%1==x.x86 shift
GOTO MAKE

:X86_64
set PROCESSOR_ARCHITECTURE=AMD64
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64
IF "x.%SWT_JAVA_HOME%"=="x." set "SWT_JAVA_HOME=%SWT_BUILDDIR%\ibm-java-sdk-80-win-x86_64\sdk"
set CFLAGS=-DJNI64
call "%MSSDK%\setenv" /X64 /RETAIL
shift
GOTO MAKE

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
