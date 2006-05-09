/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
#define GLX_NATIVE_ENTER(env, that, func) 
#define GLX_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	XVisualInfo_1sizeof_FUNC,
	glGetIntegerv_FUNC,
	glViewport_FUNC,
	glXChooseVisual_FUNC,
	glXCopyContext_FUNC,
	glXCreateContext_FUNC,
	glXCreateGLXPixmap_FUNC,
	glXDestroyContext_FUNC,
	glXDestroyGLXPixmap_FUNC,
	glXGetClientString_FUNC,
	glXGetConfig_FUNC,
	glXGetCurrentContext_FUNC,
	glXGetCurrentDrawable_FUNC,
	glXIsDirect_FUNC,
	glXMakeCurrent_FUNC,
	glXQueryExtension_FUNC,
	glXQueryExtensionsString_FUNC,
	glXQueryServerString_FUNC,
	glXQueryVersion_FUNC,
	glXSwapBuffers_FUNC,
	glXWaitGL_FUNC,
	glXWaitX_FUNC,
	memmove_FUNC,
} GLX_FUNCS;
