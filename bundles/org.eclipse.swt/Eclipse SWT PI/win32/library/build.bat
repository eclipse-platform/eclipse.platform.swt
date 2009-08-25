@echo off
rem *******************************************************************************
rem  Copyright (c) 2000, 2009 IBM Corporation and others.
rem  All rights reserved. This program and the accompanying materials
rem  are made available under the terms of the Eclipse Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/epl-v10.html
rem 
rem  Contributors:
rem      IBM Corporation - initial API and implementation
rem *******************************************************************************

@echo off

IF x.%SWT_BUILDDIR%==x. set SWT_BUILDDIR=S:\swt-builddir
IF x.%MSSDK%==x. set MSSDK="%SWT_BUILDDIR%\MSSDKs\Windows Server 2003 SP1 SDK"

IF x.%1==x.x86 GOTO X86
IF x.%1==x.x86_64 GOTO X86_64
IF x.%1==x.ia64 GOTO IA64

:X86

call "%SWT_BUILDDIR%\MSVCs\Microsoft Visual Studio 8\Common7\Tools\vsvars32.bat"
call %MSSDK%\setenv /XP32 /RETAIL
IF x.%OUTPUT_DIR%==x. set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86
IF x.%JAVA_HOME%==x. set JAVA_HOME=%SWT_BUILDDIR%\ibm-jdk1.4.1
IF x.%XULRUNNER_SDK%==x. set XULRUNNER_SDK=%SWT_BUILDDIR%\gecko-sdk
set XULRUNNER_MAKE=make_xulrunner
IF x.%1==x.x86 shift
GOTO MAKE

:X86_64

call %MSSDK%\setenv /X64 /RETAIL
IF x.%OUTPUT_DIR%==x. set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64
IF x.%JAVA_HOME%==x. set JAVA_HOME=%SWT_BUILDDIR%\ibm-sdk50-x86_64
set CFLAGS=-DJNI64
shift
GOTO MAKE

:IA64

call %MSSDK%\setenv /SRV64 /RETAIL
IF x.%OUTPUT_DIR%==x. set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.ia64
IF x.%JAVA_HOME%==x. set JAVA_HOME=%SWT_BUILDDIR%\ibm-sdk142-ia64
set CFLAGS=-DJNI64
shift
GOTO MAKE

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
