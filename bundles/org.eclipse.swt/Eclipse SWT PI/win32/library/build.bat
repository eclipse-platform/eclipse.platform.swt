@rem ***************************************************************************
@rem Copyright (c) 2000, 2018 IBM Corporation and others.
@rem All rights reserved. This program and the accompanying materials
@rem are made available under the terms of the Eclipse Public License v1.0
@rem which accompanies this distribution, and is available at
@rem http://www.eclipse.org/legal/epl-v10.html
@rem
@rem Contributors:
@rem      IBM Corporation - initial API and implementation
@rem ***************************************************************************

@echo off

IF "x.%SWT_BUILDDIR%"=="x." set "SWT_BUILDDIR=W:"
echo SWT build dir: %SWT_BUILDDIR%

IF "x.%MSSDK%"=="x." set "MSSDK=%ProgramFiles(x86)%\Windows Kits\10"
echo Microsoft Windows10 SDK dir: %MSSDK%
IF "x.%WEBKIT_DIR%"=="x." set "WEBKIT_DIR=%SWT_BUILDDIR%\WebKit\r72896"
echo Webkit dir: %WEBKIT_DIR%
IF "x.%WEBKIT_SUPPORT_DIR%"=="x." set "WEBKIT_SUPPORT_DIR=%SWT_BUILDDIR%\WebKit\SupportLibrary"
echo Webkit SupportLibrary dir: %WEBKIT_SUPPORT_DIR%
IF "x.%MSVC_HOME%"=="x." set "MSVC_HOME=%SWT_BUILDDIR%\Microsoft\Visual Studio\2017\"
IF NOT EXIST "%MSVC_HOME%" set "MSVC_HOME=%ProgramFiles(x86)%\Microsoft Visual Studio\2017"
echo Microsoft Visual Studio 2017 dir: %MSVC_HOME%

IF x.%1==x.x86 GOTO X86
IF x.%1==x.x86_64 GOTO X86_64

:X86
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86
IF "x.%SWT_JAVA_HOME%"=="x." set "SWT_JAVA_HOME=%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x86"
echo SWT_JAVA_HOME x86: %SWT_JAVA_HOME%
call "%MSVC_HOME%\Community\VC\Auxiliary\Build\vcvarsall.bat" x86
IF x.%1==x.x86 shift
GOTO MAKE

:X86_64
set PROCESSOR_ARCHITECTURE=AMD64
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64
IF "x.%SWT_JAVA_HOME%"=="x." set "SWT_JAVA_HOME=%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x64"
echo SWT_JAVA_HOME x64: %SWT_JAVA_HOME%
set CFLAGS=-DJNI64
call "%MSVC_HOME%\Community\VC\Auxiliary\Build\vcvarsall.bat" x64
shift
GOTO MAKE

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
