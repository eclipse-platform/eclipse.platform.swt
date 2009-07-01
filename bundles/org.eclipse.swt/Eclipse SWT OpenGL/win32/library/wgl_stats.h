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
extern int WGL_nativeFunctionCount;
extern int WGL_nativeFunctionCallCount[];
extern char* WGL_nativeFunctionNames[];
#define WGL_NATIVE_ENTER(env, that, func) WGL_nativeFunctionCallCount[func]++;
#define WGL_NATIVE_EXIT(env, that, func) 
#else
#ifndef WGL_NATIVE_ENTER
#define WGL_NATIVE_ENTER(env, that, func) 
#endif
#ifndef WGL_NATIVE_EXIT
#define WGL_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	ChoosePixelFormat_FUNC,
	DescribePixelFormat_FUNC,
	GetPixelFormat_FUNC,
	SetPixelFormat_FUNC,
	SwapBuffers_FUNC,
	wglCopyContext_FUNC,
	wglCreateContext_FUNC,
	wglCreateLayerContext_FUNC,
	wglDeleteContext_FUNC,
	wglDescribeLayerPlane_FUNC,
	wglGetCurrentContext_FUNC,
	wglGetCurrentDC_FUNC,
	wglGetLayerPaletteEntries_FUNC,
	wglGetProcAddress_FUNC,
	wglMakeCurrent_FUNC,
	wglRealizeLayerPalette_FUNC,
	wglSetLayerPaletteEntries_FUNC,
	wglShareLists_FUNC,
	wglSwapLayerBuffers_FUNC,
} WGL_FUNCS;
