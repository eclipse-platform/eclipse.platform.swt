@echo off
rem *******************************************************************************
rem  Copyright (c) 2000, 2007 IBM Corporation and others.
rem  All rights reserved. This program and the accompanying materials
rem  are made available under the terms of the Eclipse Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/epl-v10.html
rem 
rem  Contributors:
rem      IBM Corporation - initial API and implementation
rem *******************************************************************************

@echo off

IF "%1"=="x86_64" GOTO X86_64
IF "%1"=="IA64" GOTO IA64

:X86

IF "%DEV_TOOLS%"=="" set DEV_TOOLS=K:\dev\PRODUCTS
call %DEV_TOOLS%\msvc60\vc98\bin\vcvars32.bat
IF "%MSSDK%"=="" set MSSDK=%DEV_TOOLS%\platsdk\feb2003
call %MSSDK%\setenv /XP32 /RETAIL
IF "%OUTPUT_DIR%"=="" set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86
IF "%JAVA_HOME%"=="" set JAVA_HOME=j:\teamswt\swt-builddir\ibm-jdk1.4.1
IF "%XULRUNNER_SDK%"=="" set XULRUNNER_SDK=j:\teamswt\swt-builddir\gecko-sdk
set XULRUNNER_MAKE=make_xulrunner make_xpcominit
IF "%1"=="X86" shift
GOTO MAKE

:X86_64

IF "%MSSDK%"=="" set MSSDK=c:\sdk
call %MSSDK%\setenv /X64 /RETAIL
IF "%OUTPUT_DIR%"=="" set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86_64
IF "%JAVA_HOME%"=="" set JAVA_HOME=C:\jdk150-64
set CFLAGS=-DSWT_PTR_SIZE_64
shift
GOTO MAKE


:IA64

IF "%MSSDK%"=="" set MSSDK=c:\sdk
call %MSSDK%\setenv /SRV64 /RETAIL
IF "%OUTPUT_DIR%"=="" set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.ia64
IF "%JAVA_HOME%"=="" set JAVA_HOME=C:\jdk142_ia64
set CFLAGS=-DSWT_PTR_SIZE_64
shift
GOTO MAKE

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4 %5 %6 %7 %8 %9
