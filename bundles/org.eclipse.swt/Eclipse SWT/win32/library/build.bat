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

rem ****** 
rem MS-SDK - TEMPORARY UNTIL TRENT INSTALLS NEW SDK
rem ******
set Mssdk=j:\teamswt\swt-builddir\mssdk
call %mssdk%\setenv.bat

:MAKE
nmake makefile.mak
