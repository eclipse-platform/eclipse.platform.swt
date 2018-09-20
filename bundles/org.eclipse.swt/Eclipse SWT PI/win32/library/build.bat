@rem ***************************************************************************
@rem Copyright (c) 2000, 2018 IBM Corporation and others.
@rem
@rem This program and the accompanying materials
@rem are made available under the terms of the Eclipse Public License 2.0
@rem which accompanies this distribution, and is available at
@rem https://www.eclipse.org/legal/epl-2.0/
@rem
@rem SPDX-License-Identifier: EPL-2.0
@rem
@rem Contributors:
@rem      IBM Corporation - initial API and implementation
@rem ***************************************************************************

@rem The original build.bat source is located in /org.eclipse.swt/Eclipse SWT PI/win32/library/build.bat. It is copied during various build(s).
@rem Typically it's not ran directly, instead it is reached by build.xml's build_libraries target found in eclipse.platform.swt.binaries\bundles\org.eclipse.swt.win32.win32.x86*

@echo off
echo
echo INFO Starting build of binaries. Detailed system setup instructions can be found in /Readme.Win32.md

@rem builddir used for 32 bit building with webkit. Not needed on 64 bit builds.
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
IF EXIST "%MSVC_HOME%" (
	echo "Microsoft Visual Studio 2017 dir: %MSVC_HOME%"
) ELSE (
	echo "WARNING: Microsoft Visual Studio 2017 was not found."
)

IF "x.%1"=="x.x86" GOTO X86
IF "x.%1"=="x.x86_64" GOTO X86_64

:X86
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86
IF "x.%SWT_JAVA_HOME%"=="x." set "SWT_JAVA_HOME=%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x86"
IF "x.%SWT_JAVA_HOME%"=="x." (
    echo "WARNING: x86 Java JDK not found. Please set SWT_JAVA_HOME to your JDK directory."
) ELSE (
    echo "SWT_JAVA_HOME x86: %SWT_JAVA_HOME%"
)
call "%MSVC_HOME%\Community\VC\Auxiliary\Build\vcvarsall.bat" x86
IF x.%1==x.x86 shift
GOTO MAKE

:X86_64
set PROCESSOR_ARCHITECTURE=AMD64
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64

IF "x.%SWT_JAVA_HOME%"=="x." (
    IF exist "%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x64" (
        set "SWT_JAVA_HOME=%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x64"
    ) ELSE (
        IF exist "C:\Program Files\Java\jdk*" (
            echo "Found Java in C:\Program Files\Java\jdk, automatically setting SWT_JAVA_HOME to latest java"
            for /d %%i in ("\Program Files\Java\jdk*") do set SWT_JAVA_HOME=%%i
        )
    )
)
IF "x.%SWT_JAVA_HOME%"=="x." (
    echo "WARNING: x64 Java JDK not found. Please set SWT_JAVA_HOME to your JDK directory."
) ELSE (
    echo "SWT_JAVA_HOME x64: %SWT_JAVA_HOME%"
)

set CFLAGS=-DJNI64
call "%MSVC_HOME%\Community\VC\Auxiliary\Build\vcvarsall.bat" x64
shift
GOTO MAKE

:MAKE
@rem if call to vcvarsall.bat (which sets up environment) silently fails, then provide advice to user.
WHERE cl
if %ERRORLEVEL% NEQ 0 (
    echo "WARNING: cl (Microsoft C compiler) not found on path. Please install Microsoft Visual Studio."
    echo "     If already installed, try launching eclipse from the 'Developer Command Prompt for VS' "
)
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9