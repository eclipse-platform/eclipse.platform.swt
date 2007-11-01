/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifdef NATIVE_STATS
extern int OS_nativeFunctionCount;
extern int OS_nativeFunctionCallCount[];
extern char* OS_nativeFunctionNames[];
#define OS_NATIVE_ENTER(env, that, func) OS_nativeFunctionCallCount[func]++;
#define OS_NATIVE_EXIT(env, that, func) 
#else
#ifndef OS_NATIVE_ENTER
#define OS_NATIVE_ENTER(env, that, func) 
#endif
#ifndef OS_NATIVE_EXIT
#define OS_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	GetCurrentProcess_FUNC,
	NSBitsPerPixelFromDepth_FUNC,
	SetFrontProcess_FUNC,
	TransformProcessType_FUNC,
	class_1addMethods_FUNC,
	memmove__ILorg_eclipse_swt_internal_cocoa_objc_1class_2I_FUNC,
	memmove__ILorg_eclipse_swt_internal_cocoa_objc_1method_1list_2I_FUNC,
	memmove__ILorg_eclipse_swt_internal_cocoa_objc_1method_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_cocoa_objc_1class_2II_FUNC,
	memmove__Lorg_eclipse_swt_internal_cocoa_objc_1method_1list_2II_FUNC,
	objc_1addClass_FUNC,
	objc_1getClass_FUNC,
	objc_1lookUpClass_FUNC,
	objc_1msgSend__II_FUNC,
	objc_1msgSend__IIF_FUNC,
	objc_1msgSend__III_FUNC,
	objc_1msgSend__IIII_FUNC,
	objc_1msgSend__IIIII_FUNC,
	objc_1msgSend__IIIIII_FUNC,
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
	objc_1msgSend__II_3CI_FUNC,
	objc_1msgSend__II_3IIIIIIIIIII_FUNC,
	objc_1msgSend_1stret_FUNC,
	sel_1registerName_FUNC,
} OS_FUNCS;
