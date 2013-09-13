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

#include <dlfcn.h>

#include "nsIScriptContext.h"
#include "nsIScriptGlobalObject.h"
#include "nsCxPusher.h"

#ifndef XPCOM_NATIVE_ENTER
#define XPCOM_NATIVE_ENTER(env, that, func) 
#endif
#ifndef XPCOM_NATIVE_EXIT
#define XPCOM_NATIVE_EXIT(env, that, func) 
#endif
