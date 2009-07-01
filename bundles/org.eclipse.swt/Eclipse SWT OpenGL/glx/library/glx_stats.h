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
extern int GLX_nativeFunctionCount;
extern int GLX_nativeFunctionCallCount[];
extern char* GLX_nativeFunctionNames[];
#define GLX_NATIVE_ENTER(env, that, func) GLX_nativeFunctionCallCount[func]++;
#define GLX_NATIVE_EXIT(env, that, func) 
#else
#ifndef GLX_NATIVE_ENTER
#define GLX_NATIVE_ENTER(env, that, func) 
#endif
#ifndef GLX_NATIVE_EXIT
#define GLX_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	XVisualInfo_1sizeof_FUNC,
	_1glGetIntegerv_FUNC,
	_1glViewport_FUNC,
	_1glXChooseVisual_FUNC,
	_1glXCopyContext_FUNC,
	_1glXCreateContext_FUNC,
	_1glXCreateGLXPixmap_FUNC,
	_1glXDestroyContext_FUNC,
	_1glXDestroyGLXPixmap_FUNC,
	_1glXGetClientString_FUNC,
	_1glXGetConfig_FUNC,
	_1glXGetCurrentContext_FUNC,
	_1glXGetCurrentDrawable_FUNC,
	_1glXIsDirect_FUNC,
	_1glXMakeCurrent_FUNC,
	_1glXQueryExtension_FUNC,
	_1glXQueryExtensionsString_FUNC,
	_1glXQueryServerString_FUNC,
	_1glXQueryVersion_FUNC,
	_1glXSwapBuffers_FUNC,
	_1glXWaitGL_FUNC,
	_1glXWaitX_FUNC,
	memmove_FUNC,
} GLX_FUNCS;
