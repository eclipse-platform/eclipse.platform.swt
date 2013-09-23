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

/*
* This class is defined locally in order to avoid compiling against the XULRunner 24 SDK directly.  XULRunner
* 24 uses various C++ features that are not supported by the VC++ and gcc versions used by SWT for compilation
* of its xulrunner libraries (to ensure that dependencies such as msvcr80.dll are not introduced).
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

#ifdef _WIN32
#define XP_WIN
#define UINT32_MAX  (0xffffffff)
#include <windows.h>
#else
#include <stdint.h>
class JSPrincipals;
namespace JS {
	class Value;
};
typedef uint16_t jschar;
bool JS_EvaluateUCScriptForPrincipals(JSContext *cx, JSObject *obj,
                                 JSPrincipals *principals,
                                 const uint16_t *chars, unsigned length,
                                 const char *filename, unsigned lineno,
                                 JS::Value *rval);
#endif /* _WIN32 */
