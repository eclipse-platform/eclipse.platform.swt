/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

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

#define NATIVE_ENTER(env,clazz,func)
#define NATIVE_EXIT(env,clazz,func)

#endif /* ifndef INC_swt_H */
