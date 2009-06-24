/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifdef NATIVE_STATS
extern int AGL_nativeFunctionCount;
extern int AGL_nativeFunctionCallCount[];
extern char* AGL_nativeFunctionNames[];
#define AGL_NATIVE_ENTER(env, that, func) AGL_nativeFunctionCallCount[func]++;
#define AGL_NATIVE_EXIT(env, that, func) 
#else
#ifndef AGL_NATIVE_ENTER
#define AGL_NATIVE_ENTER(env, that, func) 
#endif
#ifndef AGL_NATIVE_EXIT
#define AGL_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	aglChoosePixelFormat_FUNC,
	aglCreateContext_FUNC,
	aglDescribePixelFormat_FUNC,
	aglDestroyContext_FUNC,
	aglDestroyPixelFormat_FUNC,
	aglEnable_FUNC,
	aglGetCurrentContext_FUNC,
	aglGetDrawable_FUNC,
	aglSetCurrentContext_FUNC,
	aglSetDrawable_FUNC,
	aglSetInteger__III_FUNC,
	aglSetInteger__II_3I_FUNC,
	aglSwapBuffers_FUNC,
	aglUpdateContext_FUNC,
} AGL_FUNCS;
