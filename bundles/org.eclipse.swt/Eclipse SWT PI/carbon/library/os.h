/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_os_H
#define INC_os_H

/*#define NDEBUG*/

#include <Carbon/Carbon.h>
#include <sys/types.h>
#include <unistd.h>

#include <JavaScriptCore/JSBase.h>
#include <JavaScriptCore/JSStringRef.h>

#include "os_custom.h"

extern jint CPSEnableForegroundOperation(jint *, jint, jint, jint, jint);
extern jint CPSSetProcessName(jint *, jbyte *);

#endif /* INC_os_H */
