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
