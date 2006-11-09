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
  
#ifndef INC_xpcom_H
#define INC_xpcom_H

#define NDEBUG

#include "nsXPCOM.h"
#include "nsEmbedAPI.h"	
#include "nsEmbedString.h"
#include "nsIInputStream.h"
#include "nsISupportsUtils.h"
#include "prmem.h"
#include "prenv.h"

#ifdef _WIN32
#define STDMETHODCALLTYPE __stdcall
#else
#define STDMETHODCALLTYPE
#endif

#endif /* INC_xpcom_H */
