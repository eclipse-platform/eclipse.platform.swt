/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
* The contents of this file are made available under the terms
* of the GNU Lesser General Public License (LGPL) Version 2.1 that
* accompanies this distribution (lgpl-v21.txt).  The LGPL is also
* available at http://www.gnu.org/licenses/lgpl.html.  If the version
* of the LGPL at http://www.gnu.org is different to the version of
* the LGPL accompanying this distribution and there is any conflict
* between the two license versions, the terms of the LGPL accompanying
* this distribution shall govern.
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "atk_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_accessibility_gtk_ATK_##func

#ifndef SWT_PTR_SIZE_64

#ifndef NO_call__II
typedef jint (*call_P1) (jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "call__II\n")
	return ((call_P1)arg0)(arg1);
	NATIVE_EXIT(env, that, "call__II\n")
}
#endif

#ifndef NO_call__III
typedef jint (*call_P2) (jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "call__III\n")
	return ((call_P2)arg0)(arg1, arg2);
	NATIVE_EXIT(env, that, "call__III\n")
}
#endif

#ifndef NO_call__IIII
typedef jint (*call_P3) (jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "call__IIII\n")
	return ((call_P3)arg0)(arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "call__IIII\n")
}
#endif

#ifndef NO_call__IIIII
typedef jint (*call_P4) (jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "call__IIIII\n")
	return ((call_P4)arg0)(arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "call__IIIII\n")
}
#endif

#ifndef NO_call__IIIIII
typedef jint (*call_P5) (jint, jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "call__IIIIII\n")
	return ((call_P5)arg0)(arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "call__IIIIII\n")
}
#endif

#ifndef NO_call__IIIIIII
typedef jint (*call_P6) (jint, jint, jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "call__IIIIIII\n")
	return ((call_P6)arg0)(arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "call__IIIIIII\n")
}
#endif

#ifndef NO_call__IIIIIIII
typedef jint (*call_P7) (jint, jint, jint, jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NATIVE_ENTER(env, that, "call__IIIIIIII\n")
	return ((call_P7)arg0)(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "call__IIIIIIII\n")
}
#endif

#else

#ifndef NO_call__JJ
typedef jlong (*call_P1) (jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	NATIVE_ENTER(env, that, "call__JJ\n")
	return ((call_P1)arg0)(arg1);
	NATIVE_EXIT(env, that, "call__JJ\n")
}
#endif

#ifndef NO_call__JJJ
typedef jlong (*call_P2) (jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	NATIVE_ENTER(env, that, "call__JJJ\n")
	return ((call_P2)arg0)(arg1, arg2);
	NATIVE_EXIT(env, that, "call__JJJ\n")
}
#endif

#ifndef NO_call__JJJJ
typedef jlong (*call_P3) (jlong, jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jlong arg3)
{
	NATIVE_ENTER(env, that, "call__JJJJ\n")
	return ((call_P3)arg0)(arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "call__JJJJ\n")
}
#endif

#ifndef NO_call__JJJJJ
typedef jlong (*call_P4) (jlong, jlong, jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jlong arg3, jlong arg4)
{
	NATIVE_ENTER(env, that, "call__JJJJJ\n")
	return ((call_P4)arg0)(arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "call__JJJJJ\n")
}
#endif

#ifndef NO_call__JJJJJJ
typedef jlong (*call_P5) (jlong, jlong, jlong, jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJJJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5)
{
	NATIVE_ENTER(env, that, "call__JJJJJJ\n")
	return ((call_P5)arg0)(arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "call__JJJJJJ\n")
}
#endif

#ifndef NO_call__JJJJJJJ
typedef jlong (*call_P6) (jlong, jlong, jlong, jlong, jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJJJJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6)
{
	NATIVE_ENTER(env, that, "call__JJJJJJJ\n")
	return ((call_P6)arg0)(arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "call__JJJJJJJ\n")
}
#endif

#ifndef NO_call__JJJJJJJJ
typedef jlong (*call_P7) (jlong, jlong, jlong, jlong, jlong, jlong, jlong);
JNIEXPORT jlong JNICALL OS_NATIVE(call__JJJJJJJJ)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
{
	NATIVE_ENTER(env, that, "call__JJJJJJJJ\n")
	return ((call_P7)arg0)(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "call__JJJJJJJJ\n")
}
#endif

#endif /* SWT_PTR_SIZE_64 */
