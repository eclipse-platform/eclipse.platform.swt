/**
 * globals.c
 *
 * This file contains the global struct allocation routines for the
 * SWT library.
 *
 */

#include <malloc.h>
#include <assert.h>
#include <memory.h>
#include <jni.h>
#include "globals.h"
#include "library.h"

static void CreateLibGlobals(JNIEnv * env);
static void DestroyLibGlobals(JNIEnv * env);
static GLOBAL_TABLE * JNICALL GetGlobals(void);

/* Place holder for all globals */
GLOBALS globals = {0};
GLOBAL_TABLE globals_table = {{
	0, 0, 0, 0, 0, 0, 0, 0, /* 8 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 16 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 24 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 32 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 40 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 48 */
	0, 0, 0, 0, 0, 0, 0, 0, /* 56 */
	0, 0, 0, 0, 0, 0, 0, &globals, /* 64 */
}};
GetGlobalsFunc getGlobals = &GetGlobals;
unsigned int vm_library = 0;

/*
 * Hook the library load.
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
	JNIEnv * env;

	(*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_2);
	vm_library = OpenLibrary("jvm");
	DEBUG_PRINTF(("VM Shared library -> %d\n", vm_library));

	if (vm_library != 0) {
		unsigned int func = LibraryLookupName(vm_library, "MdCurrentJNIGlobals");
		if (func == 0) func = LibraryLookupName(vm_library, "_MdCurrentJNIGlobals@0");

		DEBUG_PRINTF(("Function address -> %x\n", func));
		if (func != 0) getGlobals = (GetGlobalsFunc)func;
	}
	CreateLibGlobals(env);
	return JNI_VERSION_1_2;
}

/*
 * Hook the library unload.
 */
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
	JNIEnv * env;

	(*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_2);
	DestroyLibGlobals(env);
	CloseLibrary(vm_library);
}

/*
 * Allocate and initialize a SWT GLOBALS struct.
 */
static void CreateLibGlobals(JNIEnv * env)
{
	GLOBALS * globs = GET_GLOBALS();
	if (globs != NULL && globs == &globals) return;
	
	globs = (GLOBALS *)malloc(sizeof(GLOBALS));
	if (!globs)
	{
		jclass hExcClass = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
		assert(hExcClass);
		(*env)->ThrowNew(env, hExcClass, "Unable to allocate globals in onLoad");
		return;
	}
	memset(globs, 0, sizeof(GLOBALS));

	/* Start of initialization code. */
	globs->multidata = 1;
	globs->vajava = vm_library != 0 && 
		(LibraryLookupName(vm_library, "_MdCurrentJavaVM@0") != 0 ||
		LibraryLookupName(vm_library, "MdCurrentJavaVM") != 0);
	/* End of initialization code. */

	SET_GLOBALS(globs);
}

/* 
 * Free a SWT GLOBALS struct.
 */
static void DestroyLibGlobals(JNIEnv * env)
{
	GLOBALS * globs = GET_GLOBALS();
	if (globs && (globs != &globals)) 
	{
		/* Start of cleanup code. */
		/* End of cleanup code. */

		free(globs);

		SET_GLOBALS(NULL);
	}
}

/* Note that the function below do not make
 * the vm reentrant but allow the multidata code
 * to work in non reentrant VMs.
 */
/* Answer the table of globals associated with the current. */
static GLOBAL_TABLE * JNICALL GetGlobals(void)
{
	return &globals_table;
}
