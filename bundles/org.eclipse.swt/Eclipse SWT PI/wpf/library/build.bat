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

rem @echo off

IF NOT "%JAVA_HOME%"=="" GOTO MAKE

rem *****
rem Javah
rem *****
rem set JAVA_HOME=j:\teamswt\swt-builddir\ibm-jdk1.4.1
set JAVA_HOME=D:\VM\jdk1.4.2
rem set path=%JAVA_HOME%;%path%

rem ****** 
rem MS-SDK
rem ******
rem cmd /E:ON /V:ON /K "D:\Microsoft SDKs\Windows\v6.0\Bin\setenv.cmd"
call "D:\Microsoft SDKs\Windows\v6.0\Bin\setenv.cmd" /vista

set OUTPUT_DIR=..\..\..\org.eclipse.swt.win32.wpf.x86

:MAKE
nmake -f make_wpf.mak %1 %2 %3 %4
