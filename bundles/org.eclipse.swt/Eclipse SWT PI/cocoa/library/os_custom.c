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

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_OS_##func

extern id objc_msgSend(id, SEL, ...);

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);	
	rc = ((jint (*) (id, SEL, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
	return rc;
}
#endif

