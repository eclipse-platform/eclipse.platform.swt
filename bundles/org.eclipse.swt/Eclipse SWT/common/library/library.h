/**
 * library.h
 *
 * This file contains helpers to open, close and lookup
 * symbol addresses in shared librares. 
 *
 */

#ifndef INC_library_H
#define INC_library_H

unsigned int OpenLibrary(char *name);

unsigned int LibraryLookupName(unsigned int handle, char *name);

void CloseLibrary(unsigned int handle);

#endif /* INC_library_H */
