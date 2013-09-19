/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef nsIScriptContext_24_h__
#include "nsIScriptContext_24.h"
#include "nsIScriptGlobalObject_24.h"
#endif /* nsIScriptContext_24_h__ */

#ifndef XPCOM_NATIVE_ENTER
#define XPCOM_NATIVE_ENTER(env, that, func) 
#endif
#ifndef XPCOM_NATIVE_EXIT
#define XPCOM_NATIVE_EXIT(env, that, func) 
#endif

#ifdef _WIN32
#define XP_WIN
#define UINT32_MAX  (0xffffffff)
#include <windows.h>

/*
* This class is defined locally for win32 in order to avoid compiling against the XULRunner 24 SDK directly.
* This is done because XULRuner 24  uses various C++ features that are not supported by VC++6.0, which SWT
* uses for compilation of its xulrunner libraries.  SWT uses VC++6.0 for compiling its xulrunner libraries
* because newer VC++ releases introduce runtime dependencies on the VS C++ runtime (eg.- msvcr80.dll), which
* the user may not have installed.
*/
struct JSContext;
class JSObject;
class JSScript;
class JSCompartment;
class JSAutoCompartment {
	JSContext *cx_;
	JSCompartment *oldCompartment_;
  public:
	JSAutoCompartment(JSContext *cx, JSObject *target);
	JSAutoCompartment(JSContext *cx, JSScript *target);
	~JSAutoCompartment();
};

#else
#include "nsCxPusher.h"
#endif /* _WIN32 */
