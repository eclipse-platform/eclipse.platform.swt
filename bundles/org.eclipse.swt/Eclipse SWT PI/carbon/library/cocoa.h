/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_cocoa_H
#define INC_cocoa_H

#include <Carbon/Carbon.h>
#include <WebKit/WebKit.h>
#include <WebKit/HIWebView.h>
#include <WebKit/CarbonUtils.h>

#include "cocoa_custom.h"

#ifdef __i386__
#define STRUCT_SIZE_LIMIT 8
#elif __ppc__
#define STRUCT_SIZE_LIMIT 4
#elif __x86_64__
#define STRUCT_SIZE_LIMIT 16
#endif

#endif /* INC_cocoa_H */
