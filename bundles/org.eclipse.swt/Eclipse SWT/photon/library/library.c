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

#include <dlfcn.h>
#include <stdio.h>

unsigned int OpenLibrary(char *name)
{
	void * handle = dlopen (name, RTLD_LAZY | RTLD_GLOBAL);
	if (handle == NULL) {
		char buf[512];
		sprintf(buf, "lib%s.so", name);
		handle = dlopen (buf, 1);
	}
	return (unsigned int)handle;
}

unsigned int LibraryLookupName(unsigned int handle, char *name)
{
	if (handle == 0) return 0;
	return (unsigned int)dlsym ((void *)handle, name);
}

void CloseLibrary(unsigned int handle)
{
	if (handle != 0) dlclose ((void *)handle);
}