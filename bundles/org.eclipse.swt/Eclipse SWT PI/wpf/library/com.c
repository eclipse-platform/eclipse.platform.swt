/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "com_structs.h"
#include "com_stats.h"

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_wpf_COM_##func

#ifndef NO_OleInitialize
JNIEXPORT jint JNICALL COM_NATIVE(OleInitialize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleInitialize_FUNC);
	rc = (jint)OleInitialize((LPVOID)arg0);
	COM_NATIVE_EXIT(env, that, OleInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO_OleUninitialize
JNIEXPORT void JNICALL COM_NATIVE(OleUninitialize)
	(JNIEnv *env, jclass that)
{
	COM_NATIVE_ENTER(env, that, OleUninitialize_FUNC);
	OleUninitialize();
	COM_NATIVE_EXIT(env, that, OleUninitialize_FUNC);
}
#endif

