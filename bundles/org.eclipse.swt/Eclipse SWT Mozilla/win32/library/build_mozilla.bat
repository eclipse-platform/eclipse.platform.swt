rem *******************************************************************************
rem  Copyright (c) 2003 IBM Corporation and others.
rem  All rights reserved. This program and the accompanying materials 
rem  are made available under the terms of the Common Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/cpl-v10.html
rem  
rem  Contributors:
rem      IBM Corporation - initial API and implementation
rem *******************************************************************************
@echo off

rem *****
rem Javah
rem *****
set JAVA_HOME=j:\teamswt\swt-builddir\ive\bin
set path=%JAVA_HOME%;%path%

rem *****
rem mozilla dist folder
rem *****
set MOZ_HOME=D:\xp\mozilla2swt\mozilla1.4\mozilla\dist

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
nmake -f make_mozilla_win32.mak %1 %2 %3 %4