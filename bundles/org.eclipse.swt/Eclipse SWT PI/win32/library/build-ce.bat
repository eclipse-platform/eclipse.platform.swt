@echo off
rem *******************************************************************************
rem  Copyright (c) 2000, 2005 IBM Corporation and others.
rem  All rights reserved. This program and the accompanying materials
rem  are made available under the terms of the Eclipse Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/epl-v10.html
rem  
rem  Contributors:
rem      IBM Corporation - initial API and implementation
rem *******************************************************************************

@echo off

IF NOT "%JAVA_HOME%"=="" GOTO MAKE

rem *****
rem Javah
rem *****
set JAVA_HOME=j:\teamswt\swt-builddir\ive\bin
set path=%JAVA_HOME%;%path%

rem ********
rem MSVC 6.0
rem ********
call k:\dev\products\msvc60\vc98\bin\vcvars32.bat

rem *****************
rem MS-SDK WinCE
rem *****************
set WCEROOT=k:\dev\products\wince.sdk

rem **********************************************************
rem By default, build library for ARM Pocket PC
rem 'Experiment' with other targets: an example is provided
rem for ARM HPC 2000. Set the flag CFG to the value "hpc2000"
rem to build for ARM HPC 2000.
rem **********************************************************
IF "%CFG%"=="hpc2000" GOTO HPC2000
IF "%CFG%"=="SmartPhone 2002" GOTO SMARTPHONE

:POCKETPC
set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.wce_ppc.arm
set OSVERSION=WCE300
set PLATFORM=MS Pocket PC
set PATH=%WCEROOT%\bin;%WCEROOT%\%OSVERSION%\bin;%path%
set INCLUDE=%WCEROOT%\%OSVERSION%\%PLATFORM%\include;%WCEROOT%\%OSVERSION%\%PLATFORM%\MFC\include;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\include;
set LIB=%WCEROOT%\%OSVERSION%\%PLATFORM%\lib\arm;%WCEROOT%\%OSVERSION%\%PLATFORM%\MFC\lib\arm;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\lib\arm;
GOTO MAKE

:HPC2000
set OSVERSION=WCE300
set PLATFORM=hpc2000
set PATH=%WCEROOT%\bin;%WCEROOT%\%OSVERSION%\bin;%path%
set INCLUDE=%WCEROOT%\%OSVERSION%\%PLATFORM%\include;%WCEROOT%\%OSVERSION%\%PLATFORM%\MFC\include;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\include;
set LIB=%WCEROOT%\%OSVERSION%\%PLATFORM%\lib\arm;%WCEROOT%\%OSVERSION%\%PLATFORM%\MFC\lib\arm;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\lib\arm;
GOTO MAKE

:SMARTPHONE
set OSVERSION=WCE300
set PLATFORM=SmartPhone 2002
set PATH=%WCEROOT%\bin;%WCEROOT%\%OSVERSION%\bin;%path%
set INCLUDE=%WCEROOT%\%OSVERSION%\%PLATFORM%\include;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\include;
set LIB=%WCEROOT%\%OSVERSION%\%PLATFORM%\lib\arm;%WCEROOT%\%OSVERSION%\%PLATFORM%\ATL\lib\arm;
GOTO MAKE

:MAKE
Title Environment %OSVERSION% %PLATFORM%
nmake -f make_wince.mak %1 %2 %3 %4
