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
	UINT prevMode = SetErrorMode(SEM_NOOPENFILEERRORBOX | SEM_FAILCRITICALERRORS);
	HINSTANCE handle = LoadLibrary ((LPCSTR)name);
	SetErrorMode(prevMode);
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