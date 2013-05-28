/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
  
#ifndef INC_xpcom_H
#define INC_xpcom_H

#define NDEBUG
#define XPCOM_GLUE 1

#include "nsXPCOM.h"
#include "nsEmbedString.h"
#include "nsIInputStream.h"
#include "nsIMemory.h"
#include "nsISupportsUtils.h"
#include "nsXPCOMGlue.h"
#include "xpcom_custom.h"

#ifndef nsIScriptContext_h__
#include "nsIScriptContext.h"
#include "nsIScriptGlobalObject.h"
#include "nsIScriptContext_17.h"
#include "nsIScriptGlobalObject_17.h"
#endif /* nsIScriptContext_h__ */

#define XPCOM_LOAD_FUNCTION LOAD_FUNCTION

#ifdef _WIN32
#if !(defined(__i386__) || defined(_M_IX86) || defined(_X86_))
#define nsStaticModuleInfo void /* define on 64-bit win32 due to use of XULRunner 10 SDK */
#endif
#define STDMETHODCALLTYPE __stdcall
#define NO__1NS_1InitXPCOM2
#else /* _WIN32 */
#define STDMETHODCALLTYPE
#ifdef __APPLE__
#define NO__1NS_1InitXPCOM2
#endif /* __APPLE__ */
#endif /* _WIN32 */

#define SWT_XREInitEmbedding2 nsresult (*)(nsIFile *,nsIFile *,nsIDirectoryServiceProvider *)
#define SWT_XREInitEmbedding nsresult (*)(nsILocalFile *,nsILocalFile *,nsIDirectoryServiceProvider *,nsStaticModuleInfo const *,PRUint32)

#endif /* INC_xpcom_H */
