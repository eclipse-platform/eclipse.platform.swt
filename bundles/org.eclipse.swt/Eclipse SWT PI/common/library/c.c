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

#include "swt.h"
#include "c_structs.h"
#include "c_stats.h"

#define C_NATIVE(func) Java_org_eclipse_swt_internal_C_##func

#ifndef NO_PTR_1sizeof
JNIEXPORT jint JNICALL C_NATIVE(PTR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, PTR_1sizeof_FUNC);
	rc = (jint)PTR_sizeof();
	C_NATIVE_EXIT(env, that, PTR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_free
JNIEXPORT void JNICALL C_NATIVE(free)
	(JNIEnv *env, jclass that, jint arg0)
{
	C_NATIVE_ENTER(env, that, free_FUNC);
	free((void *)arg0);
	C_NATIVE_EXIT(env, that, free_FUNC);
}
#endif

#ifndef NO_malloc
JNIEXPORT jint JNICALL C_NATIVE(malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, malloc_FUNC);
	rc = (jint)malloc(arg0);
	C_NATIVE_EXIT(env, that, malloc_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL C_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((char *)arg0);
	C_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif

