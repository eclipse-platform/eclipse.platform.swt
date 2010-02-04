/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifdef NATIVE_STATS
extern int Cocoa_nativeFunctionCount;
extern int Cocoa_nativeFunctionCallCount[];
extern char* Cocoa_nativeFunctionNames[];
#define Cocoa_NATIVE_ENTER(env, that, func) Cocoa_nativeFunctionCallCount[func]++;
#define Cocoa_NATIVE_EXIT(env, that, func) 
#else
#ifndef Cocoa_NATIVE_ENTER
#define Cocoa_NATIVE_ENTER(env, that, func) 
#endif
#ifndef Cocoa_NATIVE_EXIT
#define Cocoa_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	HICocoaViewCreate_FUNC,
	HIJavaViewCreateWithCocoaView_FUNC,
	HIWebViewCreate_FUNC,
	HIWebViewGetWebView_FUNC,
	NSDeviceRGBColorSpace_FUNC,
	NSSearchPathForDirectoriesInDomains_FUNC,
	WebInitForCarbon_FUNC,
	class_1getClassMethod_FUNC,
	memcpy_FUNC,
	memmove_FUNC,
	objc_1getClass_FUNC,
	objc_1getMetaClass_FUNC,
	objc_1msgSend__II_FUNC,
	objc_1msgSend__IID_FUNC,
	objc_1msgSend__IIF_FUNC,
	objc_1msgSend__IIFF_FUNC,
	objc_1msgSend__III_FUNC,
	objc_1msgSend__IIII_FUNC,
	objc_1msgSend__IIIII_FUNC,
	objc_1msgSend__IIIIII_FUNC,
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
	objc_1msgSend__II_3BI_FUNC,
	objc_1msgSend__II_3C_FUNC,
	objc_1msgSend__II_3IIIIIIIIIII_FUNC,
	objc_1msgSend_1fpret_FUNC,
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC,
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	sel_1registerName_FUNC,
} Cocoa_FUNCS;
