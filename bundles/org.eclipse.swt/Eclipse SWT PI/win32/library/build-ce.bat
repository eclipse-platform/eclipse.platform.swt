@echo off

rem Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
rem This file is made available under the terms of the Common Public License v1.0
rem which accompanies this distribution, and is available at
rem http://www.eclipse.org/legal/cpl-v10.html

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

:POCKETPC
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

:MAKE
Title Environment %OSVERSION% %PLATFORM%
nmake -f make_wince.mak %1 %2 %3 %4
