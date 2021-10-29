@rem ***************************************************************************
@rem Copyright (c) 2000, 2021 IBM Corporation and others.
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

@rem SWT_BUILDDIR defaults to "W:" on the SWT Windows native build infrastructure machine.
IF "x.%SWT_BUILDDIR%"=="x." set "SWT_BUILDDIR=W:"
echo SWT build dir: %SWT_BUILDDIR%

@rem Specify VisualStudio Edition: 'Community', 'Enterprise', 'Professional' etc.
IF "x.%MSVC_EDITION%"=="x." set "MSVC_EDITION=Community"

@rem Specify VisualStudio Version: '2017' or newer '2019'
IF "x.%MSVC_VERSION%"=="x." set "MSVC_VERSION=2019"

IF "x.%MSVC_HOME%"=="x." set "MSVC_HOME=%SWT_BUILDDIR%\Microsoft\Visual Studio\%MSVC_VERSION%\"
IF NOT EXIST "%MSVC_HOME%" set "MSVC_HOME=%ProgramFiles(x86)%\Microsoft Visual Studio\%MSVC_VERSION%\BuildTools"
IF NOT EXIST "%MSVC_HOME%" set "MSVC_HOME=%ProgramFiles(x86)%\Microsoft Visual Studio\%MSVC_VERSION%\%MSVC_EDITION%"
IF EXIST "%MSVC_HOME%" (
	echo "Microsoft Visual Studio %MSVC_VERSION% dir: %MSVC_HOME%"
) ELSE (
	echo "WARNING: Microsoft Visual Studio %MSVC_VERSION% was not found."
    echo "     Refer steps for SWT Windows native setup: https://www.eclipse.org/swt/swt_win_native.php"
)

IF NOT "x.%1"=="x.x86_64" (
	ECHO 32-bit builds are no longer supported.
	EXIT /B 1
)

set PROCESSOR_ARCHITECTURE=AMD64
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64

:: Search for a usable JDK
:: -----------------------
IF "%SWT_JAVA_HOME%"=="" ECHO 'SWT_JAVA_HOME' was not provided, auto-searching for JDK...
:: Bug 572733: JDK path used on Azure build machines
IF "%SWT_JAVA_HOME%"=="" CALL :TryToUseJdk "%ProgramFiles%\AdoptOpenJDK\jdk-8.0.292.10-hotspot"
:: Bug 526802: Probably some kind of legacy build machine path
IF "%SWT_JAVA_HOME%"=="" CALL :TryToUseJdk "%SWT_BUILDDIR%\Java\Oracle\jdk1.8.0-latest\x64"
:: Search for generic JDKs so that user can build with little configuration
:: Note that first found JDK wins, so sort them by order of preference.
IF "%SWT_JAVA_HOME%"=="" CALL :TryToUseJdk "%ProgramFiles%\Java\jdk*"
IF "%SWT_JAVA_HOME%"=="" CALL :TryToUseJdk "%ProgramFiles%\AdoptOpenJDK\jdk*"
IF NOT EXIST "%SWT_JAVA_HOME%" (
    echo "WARNING: x64 Java JDK not found. Please set SWT_JAVA_HOME to your JDK directory."
    echo "     Refer steps for SWT Windows native setup: https://www.eclipse.org/swt/swt_win_native.php"
) ELSE (
    echo "SWT_JAVA_HOME x64: %SWT_JAVA_HOME%"
)

set CFLAGS=-DJNI64
call "%MSVC_HOME%\VC\Auxiliary\Build\vcvarsall.bat" x64
shift

@rem if call to vcvarsall.bat (which sets up environment) silently fails, then provide advice to user.
WHERE cl
if %ERRORLEVEL% NEQ 0 (
    echo "WARNING: cl (Microsoft C compiler) not found on path. Please install Microsoft Visual Studio."
    echo "     If already installed, try launching eclipse from the 'Developer Command Prompt for VS' "
    echo "     Refer steps for SWT Windows native setup: https://www.eclipse.org/swt/swt_win_native.php"
)
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
GOTO :EOF

:TryToUseJdk
SET TESTED_JDK_PATH_MASK=%~1
:: Loop over all directories matching mask.
:: Note that directories are iterated in alphabetical order and *last* hit will
:: be selected in hopes to select the highest available JDK version.
FOR /D %%I IN ("%TESTED_JDK_PATH_MASK%") DO (
	IF NOT EXIST "%%~I" (
		ECHO -- JDK '%%~I' doesn't exist on disk
		GOTO :EOF
	)
	IF NOT EXIST "%%~I\include\jni.h" (
		ECHO -- JDK '%%~I' is bad: no jni.h
		GOTO :EOF
	)
	ECHO -- JDK '%%~I' looks good, selecting it
	SET SWT_JAVA_HOME=%%~I
)
GOTO :EOF
