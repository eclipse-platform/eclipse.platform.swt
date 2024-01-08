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
@rem Typically it's not ran directly, instead it is reached by build.xml's build_libraries target found in eclipse.platform.swt\bundles\org.eclipse.swt.win32.win32.x86*

@echo off
echo
echo INFO Starting build of binaries. Detailed system setup instructions can be found in /Readme.Win32.md

@rem SWT_BUILDDIR defaults to "W:" on the SWT Windows native build infrastructure machine.
IF "x.%SWT_BUILDDIR%"=="x." set "SWT_BUILDDIR=W:"
echo SWT build dir: %SWT_BUILDDIR%

@rem Specify VisualStudio Edition: 'Community', 'Enterprise', 'Professional' etc.
IF "x.%MSVC_EDITION%"=="x." set "MSVC_EDITION=auto"

@rem Specify VisualStudio Version: '2022', '2019', '2017' etc.
IF "x.%MSVC_VERSION%"=="x." set "MSVC_VERSION=auto"

@rem Search for a usable Visual Studio
@rem ---------------------------------
IF "%MSVC_HOME%"=="" CALL :ECHO "'MSVC_HOME' was not provided, auto-searching for Visual Studio..."
@rem Bug 572308: Path used on older SWT build machines
IF "%MSVC_HOME%"=="" CALL :FindVisualStudio "%SWT_BUILDDIR%\Microsoft\Visual Studio\$MSVC_VERSION$"
@rem Bug 574007: Path used on Azure build machines
IF "%MSVC_HOME%"=="" CALL :FindVisualStudio "%ProgramFiles(x86)%\Microsoft Visual Studio\$MSVC_VERSION$\BuildTools"
@rem Bug 578519: Common installation paths; VisualStudio is installed in x64 ProgramFiles since VS2022
IF "%MSVC_HOME%"=="" CALL :FindVisualStudio      "%ProgramFiles%\Microsoft Visual Studio\$MSVC_VERSION$\$MSVC_EDITION$"
@rem Bug 578519: Common installation paths; VisualStudio is installed in x86 ProgramFiles before VS2022
IF "%MSVC_HOME%"=="" CALL :FindVisualStudio "%ProgramFiles(x86)%\Microsoft Visual Studio\$MSVC_VERSION$\$MSVC_EDITION$"
@rem Report
IF NOT EXIST "%MSVC_HOME%" (
	CALL :ECHO "WARNING: Microsoft Visual Studio was not found (for edition=%MSVC_EDITION% version=%MSVC_VERSION%)"
    CALL :ECHO "         Refer steps for SWT Windows native setup: https://www.eclipse.org/swt/swt_win_native.php"
) ELSE (
	CALL :ECHO "MSVC_HOME: %MSVC_HOME%"
)

@rem Check for a usable JDK
IF "%SWT_JAVA_HOME%"=="" CALL :ECHO "'SWT_JAVA_HOME' was not provided"
IF NOT EXIST "%SWT_JAVA_HOME%" (
    CALL :ECHO "WARNING: x64 Java JDK not found. Please set SWT_JAVA_HOME to the JDK directory containing the intended JDK native headers."
)

@rem -----------------------
IF NOT "x.%1"=="x.x86_64" (
	CALL :ECHO "ERROR: 32-bit builds are no longer supported."
	EXIT /B 1
)

set PROCESSOR_ARCHITECTURE=AMD64
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64

set CFLAGS=-DJNI64
call "%MSVC_HOME%\VC\Auxiliary\Build\vcvarsall.bat" x64
shift

@rem if call to vcvarsall.bat (which sets up environment) silently fails, then provide advice to user.
WHERE cl
if %ERRORLEVEL% NEQ 0 (
    CALL :ECHO "ERROR: cl (Microsoft C compiler) not found on path. Please install Microsoft Visual Studio."
    CALL :ECHO "         If already installed, try launching eclipse from the 'Developer Command Prompt for VS'"
    CALL :ECHO "         Refer steps for SWT Windows native setup: https://www.eclipse.org/swt/swt_win_native.php"
    CALL :ECHO "ERROR: exit 1"	
    EXIT 1
)
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
IF ERRORLEVEL 1 (	
    CALL :ECHO "ERROR: exit 1"	
    EXIT 1
)
GOTO :EOF

@rem Find Visual Studio
@rem %1 = path template with '$MSVC_VERSION$' and '$MSVC_EDITION$' tokens
:FindVisualStudio
	@rem Early return if already found
	IF NOT "%MSVC_HOME%"=="" GOTO :EOF

	IF "%MSVC_VERSION%"=="auto" (
		CALL :FindVisualStudio2 "%~1" "2022"
		CALL :FindVisualStudio2 "%~1" "2019"
		CALL :FindVisualStudio2 "%~1" "2017"
	) ELSE (
		CALL :FindVisualStudio2 "%~1" "%MSVC_VERSION%"
	)
GOTO :EOF

@rem Find Visual Studio
@rem %1 = path template with '$MSVC_VERSION$' and '$MSVC_EDITION$' tokens
@rem %2 = value for '$MSVC_VERSION$'
:FindVisualStudio2
	@rem Early return if already found
	IF NOT "%MSVC_HOME%"=="" GOTO :EOF

	IF "%MSVC_EDITION%"=="auto" (
		CALL :FindVisualStudio3 "%~1" "%~2" "Community"
		CALL :FindVisualStudio3 "%~1" "%~2" "Enterprise"
		CALL :FindVisualStudio3 "%~1" "%~2" "Professional"
	) ELSE (
		CALL :FindVisualStudio3 "%~1" "%~2" "%MSVC_EDITION%"
	)
GOTO :EOF

@rem Find Visual Studio
@rem %1 = path template with '$MSVC_VERSION$' and '$MSVC_EDITION$' tokens
@rem %2 = value for '$MSVC_VERSION$'
@rem %3 = value for '$MSVC_EDITION$'
:FindVisualStudio3
	@rem Early return if already found
	IF NOT "%MSVC_HOME%"=="" GOTO :EOF

	SET "TESTED_VS_PATH=%~1"
	@rem Substitute '$MSVC_VERSION$' and '$MSVC_EDITION$'
	CALL SET "TESTED_VS_PATH=%%TESTED_VS_PATH:$MSVC_VERSION$=%~2%%"
	CALL SET "TESTED_VS_PATH=%%TESTED_VS_PATH:$MSVC_EDITION$=%~3%%"

	@rem If the folder isn't there, then skip it without printing errors
	IF NOT EXIST "%TESTED_VS_PATH%" GOTO :EOF

	@rem Try this path
	CALL :TryToUseVisualStudio "%TESTED_VS_PATH%"
GOTO :EOF

@rem Test Visual Studio and set '%MSVC_HOME%' on success
@rem %1 = tested path
:TryToUseVisualStudio
	SET "TESTED_VS_PATH=%~1"
	IF NOT EXIST "%TESTED_VS_PATH%\VC\Auxiliary\Build\vcvarsall.bat" (
		CALL :ECHO "-- VisualStudio '%TESTED_VS_PATH%' is bad: 'vcvarsall.bat' not found"
		GOTO :EOF
	)
	CALL :ECHO "-- VisualStudio '%TESTED_VS_PATH%' looks good, selecting it"
	SET "MSVC_HOME=%TESTED_VS_PATH%"
GOTO :EOF

@rem Regular ECHO has trouble with special characters such as ().
@rem At the same time, if its argument is quoted, the quotes are printed literally.
@rem The workaround is to escape all special characters with ^
:ECHO
SET "ECHO_STRING=%~1"
SET "ECHO_STRING=%ECHO_STRING:<=^<%"
SET "ECHO_STRING=%ECHO_STRING:>=^>%"
SET "ECHO_STRING=%ECHO_STRING:(=^(%"
SET "ECHO_STRING=%ECHO_STRING:)=^)%"
ECHO %ECHO_STRING%
GOTO :EOF
