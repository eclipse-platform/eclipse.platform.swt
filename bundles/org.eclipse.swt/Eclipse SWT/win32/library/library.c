/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * library.c
 *
 * This file contains the implementation of the
 * shared libraries functions.
 *
 */

#include <windows.h>

unsigned int OpenLibrary(char *name)
{
#ifndef _WIN32_WCE
	UINT prevMode = SetErrorMode(SEM_NOOPENFILEERRORBOX | SEM_FAILCRITICALERRORS);
#endif
	HINSTANCE handle = LoadLibrary ((LPCSTR)name);
#ifndef _WIN32_WCE
	SetErrorMode(prevMode);
#endif
	return (unsigned int)handle;
}

unsigned int LibraryLookupName(unsigned int handle, char *name)
{
	if (handle == 0) return 0;
	return (unsigned int)GetProcAddress ((HINSTANCE)handle, (LPCSTR)name);
}

void CloseLibrary(unsigned int handle)
{
	if (handle != 0) FreeLibrary ((HINSTANCE)handle);
}