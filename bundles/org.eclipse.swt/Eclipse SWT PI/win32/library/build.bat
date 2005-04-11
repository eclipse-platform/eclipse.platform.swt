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
set JAVA_HOME=j:\teamswt\swt-builddir\ibm-jdk1.4.1
set path=%JAVA_HOME%;%path%

rem ********
rem MSVC 6.0
rem ********
call k:\dev\products\msvc60\vc98\bin\vcvars32.bat

rem ****** 
rem MS-SDK
rem ******
set Mssdk=K:\dev\PRODUCTS\PLATSDK\feb2003
call %mssdk%\setenv.bat

set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.win32.x86

:MAKE
nmake -f make_win32.mak %1 %2 %3 %4
