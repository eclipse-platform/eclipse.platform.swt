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
extern int Cocoa_nativeFunctionCount;
extern int Cocoa_nativeFunctionCallCount[];
extern char* Cocoa_nativeFunctionNames[];
#define Cocoa_NATIVE_ENTER(env, that, func) Cocoa_nativeFunctionCallCount[func]++;
#define Cocoa_NATIVE_EXIT(env, that, func) 
#else
#define Cocoa_NATIVE_ENTER(env, that, func) 
#define Cocoa_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	HIWebViewCreate_FUNC,
	HIWebViewGetWebView_FUNC,
	WebInitForCarbon_FUNC,
	memcpy_FUNC,
	objc_1getClass_FUNC,
	objc_1msgSend__II_FUNC,
	objc_1msgSend__IIF_FUNC,
	objc_1msgSend__III_FUNC,
	objc_1msgSend__IIII_FUNC,
	objc_1msgSend__IIIII_FUNC,
	objc_1msgSend__IIIIII_FUNC,
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
	sel_1registerName_FUNC,
} Cocoa_FUNCS;
