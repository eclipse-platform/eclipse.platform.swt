/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * globals.h
 *
 * This file contains the global struct declaration for the
 * SWT library.
 *
 */

#ifndef INC_globals_H
#define INC_globals_H

#include "jni.h"
#include "structs.h"
#include "callback.h"

/* For debugging */
#define DEBUG_PRINTF(x)
/*#define DEBUG_PRINTF(x) printf x; */

/* define this to print out debug statements */
/* #define DEBUG_CALL_PRINTS */

/* Define the current lib ID. */
/* A unique constant is required for each lib.*/
#define ID_LIB_SWT              63
#define ID_CURR_LIB ID_LIB_SWT

typedef struct
{
	int multidata;
	int vajava;

	/* Shared globals */
	SWT_CALLBACKINFO dllCallbackInfo[MAX_CALLBACKS];
	jfieldID objectID;
	jfieldID addressID;
	jfieldID methodID;
	jfieldID signatureID;
	jfieldID isStaticID;
	jfieldID argCountID;
	jfieldID isArrayBasedID;
	int callbackCached;
	int initialized;
	int counter;

	FID_CACHE_GLOBALS
} 
GLOBALS;

/* 
 * The GLOB_VAR_TABLE contains the global vars for an entire VM.  Each library has
 * a unique predefined slot within it.
 */
typedef struct
{
	void * table[64];		/* The size is ignored. There is a page of memory to play with. */
}
GLOBAL_TABLE;
typedef GLOBAL_TABLE * (JNICALL *GetGlobalsFunc)();
extern GetGlobalsFunc getGlobals;
/* 
 * Any function which uses the global variables in a lib can use 
 * GET_GLOBALS to get the global data.
 */
#define GET_GLOBALS() ((*getGlobals)()->table[ID_CURR_LIB])
#define SET_GLOBALS(pMem) (*getGlobals)()->table[ID_CURR_LIB] = (void *)pMem
#define DECL_GLOB(pSym) GLOBALS * pSym = GET_GLOBALS();

#define PGLOB(x) pGlob->x

#endif /* ifndef INC_globals_H */
