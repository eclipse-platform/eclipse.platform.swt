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
#include "os_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_accessibility_gtk_ATK_##func

#ifndef NO_AtkObjectFactory_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(AtkObjectFactory_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactory_1sizeof\n")
	rc = (jint)sizeof(AtkObjectFactory);
	NATIVE_EXIT(env, that, "AtkObjectFactory_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_AtkObjectFactoryClass_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(AtkObjectFactoryClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactoryClass_1sizeof\n")
	rc = (jint)sizeof(AtkObjectFactoryClass);
	NATIVE_EXIT(env, that, "AtkObjectFactoryClass_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I\n")
	if (arg0) setAtkActionIfaceFields(env, arg0, (AtkActionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I\n")
	if (arg0) setAtkComponentIfaceFields(env, arg0, (AtkComponentIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I\n")
	if (arg0) setAtkObjectClassFields(env, arg0, (AtkObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I\n")
	if (arg0) setAtkObjectFactoryClassFields(env, arg0, (AtkObjectFactoryClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I\n")
	if (arg0) setAtkSelectionIfaceFields(env, arg0, (AtkSelectionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I\n")
	if (arg0) setAtkTextIfaceFields(env, arg0, (AtkTextIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2\n")
	if (arg1) getAtkActionIfaceFields(env, arg1, (AtkActionIface*)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2\n")
	if (arg1) getAtkComponentIfaceFields(env, arg1, (AtkComponentIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2\n")
	if (arg1) getAtkObjectClassFields(env, arg1, (AtkObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2\n")
	if (arg1) getAtkObjectFactoryClassFields(env, arg1, (AtkObjectFactoryClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2\n")
	if (arg1) getAtkSelectionIfaceFields(env, arg1, (AtkSelectionIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2\n")
	if (arg1) getAtkTextIfaceFields(env, arg1, (AtkTextIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2\n")
}
#endif

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

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I\n")
	if (arg0) setGObjectClassFields(env, arg0, (GObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I\n")
	if (arg0) setGtkAccessibleFields(env, arg0, (GtkAccessible *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2\n")
	if (arg1) getGObjectClassFields(env, arg1, (GObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2\n")
	if (arg1) getGtkAccessibleFields(env, arg1, (GtkAccessible *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2\n")
}
#endif
