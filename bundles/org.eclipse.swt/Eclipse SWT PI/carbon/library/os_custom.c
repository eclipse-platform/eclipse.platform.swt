/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "os_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_carbon_OS_##func

#ifndef NO_CGContextGetTextPosition
JNIEXPORT void JNICALL OS_NATIVE(CGContextGetTextPosition)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGPoint _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextGetTextPosition\n")
	if (arg1) lparg1 = getCGPointFields(env, arg1, &_arg1);
	*lparg1 = CGContextGetTextPosition((CGContextRef)arg0);
	if (arg1) setCGPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CGContextGetTextPosition\n")
}
#endif