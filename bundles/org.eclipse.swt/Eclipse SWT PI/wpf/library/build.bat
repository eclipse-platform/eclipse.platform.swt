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

rem @echo off

rem *****
rem Java
rem *****
set JAVA_HOME=S:\swt-builddir\ibm-jdk1.4.1

rem ****** 
rem MS-SDK
rem ******
call "S:\swt-builddir\MSSDKs\Microsoft SDK 6.0 Vista\Bin\setenv.cmd" /x86 /vista

rem ****** 
rem OUTPUT DIR
rem ******
IF "x.%OUTPUT_DIR%"=="x." set OUTPUT_DIR=..\..\..\org.eclipse.swt.wpf.win32.x86

nmake -f make_wpf.mak %1 %2 %3 %4
