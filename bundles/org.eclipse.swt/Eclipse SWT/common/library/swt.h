/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
/**
 * swt.h
 *
 * This file contains the global macro declarations for the
 * SWT library.
 *
 */

#ifndef INC_swt_H
#define INC_swt_H

#include "jni.h"

/* For debugging */
#define DEBUG_PRINTF(x)
/*#define DEBUG_PRINTF(x) printf x; */

/* define this to print out debug statements */
/* #define DEBUG_CALL_PRINTS */
/* #define DEBUG_CHECK_NULL_EXCEPTIONS */

#ifdef DEBUG_CALL_PRINTS
#define DEBUG_CALL(func) fprintf(stderr, func);
#else
#define DEBUG_CALL(func)
#endif

#ifdef DEBUG_CHECK_NULL_EXCEPTIONS
#define DEBUG_CHECK_NULL(env, address) \
	if (address == 0) { \
		jclass clazz = (*env)->FindClass(env, "org/eclipse/swt/SWTError"); \
		if (clazz != NULL) { \
			(*env)->ThrowNew(env, clazz, "Argument cannot be NULL"); \
		} \
		return; \
	}
#else
#define DEBUG_CHECK_NULL(env, address)
#endif

#define DECL_GLOB(pSym)
#define PGLOB(x) x

#endif /* ifndef INC_swt_H */
