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

rem ****** 
rem MS-SDK
rem ******
set Mssdk=j:\teamswt\swt-builddir\mssdk
call %mssdk%\setenv.bat

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4
