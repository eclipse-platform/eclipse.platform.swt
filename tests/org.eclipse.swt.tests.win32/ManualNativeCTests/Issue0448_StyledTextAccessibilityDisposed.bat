@ECHO OFF
REM Run me from "Developer command prompt for VS"
REM (Install Visual Studio and find it in Start menu)

CD /D "%~dp0"
SET SRC_PATH=%~n0.cpp
SET EXE_PATH=%~n0.exe

SET CFLAGS_DEBUG=/Od /Zi /DEBUG /Fd"%SRC_PATH%.compiler.pdb"
SET CFLAGS_COMMON=/W3 /EHsc /MDd /D "_DEBUG" /D "_WINDOWS" /D "_UNICODE" /D "UNICODE"
SET LFLAGS_COMMON=kernel32.lib user32.lib gdi32.lib

cl "%SRC_PATH%" %CFLAGS_DEBUG% %CFLAGS_COMMON% %LFLAGS_COMMON% /link /out:"%EXE_PATH%"
IF ERRORLEVEL 1 (
	ECHO Compilation failed
	GOTO :EOF
)

"%EXE_PATH%"
