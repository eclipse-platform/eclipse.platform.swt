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
 
/**
 * Callback implementation.
 */
#ifndef INC_callback_H
#define INC_callback_H

#include "swt.h"

#if defined (_WIN32) || defined (_WIN32_WCE)
#include "windows.h"
#define RETURN_TYPE LRESULT CALLBACK
#define RETURN_CAST (LRESULT)
#endif

#ifndef RETURN_TYPE
#define RETURN_TYPE SWT_PTR
#endif

#ifndef RETURN_CAST
#define RETURN_CAST
#endif

#ifdef REDUCED_CALLBACKS
#define MAX_CALLBACKS 16
#else
#define MAX_CALLBACKS 128
#endif /* REDUCED_CALLBACKS */

#define MAX_ARGS 12

typedef struct CALLBACK_DATA {
    jobject callback;
    jmethodID methodID;
  	jobject object;
	jboolean isStatic;
	jboolean isArrayBased; 
	jint argCount;
	SWT_PTR errorResult;
} CALLBACK_DATA;

#endif /* ifndef INC_callback_H */

