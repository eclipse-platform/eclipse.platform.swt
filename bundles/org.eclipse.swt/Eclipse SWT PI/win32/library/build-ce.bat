@echo off

rem  (c) Copyright IBM Corp., 2000, 2001
rem  All Rights Reserved.

IF NOT "%JAVA_HOME%"=="" GOTO MAKE

rem *****
rem Javah
rem *****
set JAVA_HOME=j:\teamswt\swt-builddir\ive\bin
set path=%JAVA_HOME%;%path%

rem ********
rem MSVC 6.0
rem ********
rem call k:\dev\products\msvc60\vc98\bin\vcvars32.bat

rem ****** 
rem MS-SDK
rem ******
rem set Mssdk=j:\teamswt\swt-builddir\mssdk
rem call %mssdk%\setenv.bat

call j:\j9\tools\config\ppcarm.bat

:MAKE
nmake -f make_wince.mak %1 %2 %3 %4
