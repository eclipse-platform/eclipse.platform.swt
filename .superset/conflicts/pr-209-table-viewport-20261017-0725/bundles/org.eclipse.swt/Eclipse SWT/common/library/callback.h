/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

#ifdef REDUCED_CALLBACKS
#define MAX_CALLBACKS 16
#else
#if (defined(USE_ASSEMBLER) || defined(GTK))
#define MAX_CALLBACKS 256
#else
#define MAX_CALLBACKS 128
#endif
#endif /* REDUCED_CALLBACKS */

#define MAX_ARGS 12

typedef struct CALLBACK_DATA {
	jobject callback;
	jmethodID methodID;
	jobject object;
	jboolean isStatic;
	jboolean isArrayBased; 
	jint argCount;
	jlong errorResult;

#if   defined(COCOA)
	int arg_Selector;
#elif defined(GTK)
	int arg_GObject;
	int arg_GdkEvent;
	int arg_SwtSignalID;
#endif
} CALLBACK_DATA;

#endif /* ifndef INC_callback_H */

