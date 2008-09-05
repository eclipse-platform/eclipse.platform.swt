/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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
#include "atk_stats.h"

#define ATK_NATIVE(func) Java_org_eclipse_swt_internal_accessibility_gtk_ATK_##func

#ifndef NO_AtkObjectFactoryClass_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkObjectFactoryClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, AtkObjectFactoryClass_1sizeof_FUNC);
	rc = (jint)AtkObjectFactoryClass_sizeof();
	ATK_NATIVE_EXIT(env, that, AtkObjectFactoryClass_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_AtkObjectFactory_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkObjectFactory_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, AtkObjectFactory_1sizeof_FUNC);
	rc = (jint)AtkObjectFactory_sizeof();
	ATK_NATIVE_EXIT(env, that, AtkObjectFactory_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1ACTION_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1ACTION_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1ACTION_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_ACTION_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1ACTION_1GET_1IFACE_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1COMPONENT_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1COMPONENT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1COMPONENT_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_COMPONENT_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1COMPONENT_1GET_1IFACE_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1OBJECT_1FACTORY_1CLASS
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1OBJECT_1FACTORY_1CLASS)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1OBJECT_1FACTORY_1CLASS_FUNC);
	rc = (jintLong)ATK_OBJECT_FACTORY_CLASS(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1OBJECT_1FACTORY_1CLASS_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1SELECTION_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1SELECTION_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1SELECTION_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_SELECTION_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1SELECTION_1GET_1IFACE_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1TEXT_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1TEXT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1TEXT_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_TEXT_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1TEXT_1GET_1IFACE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1ACCESSIBLE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1GTK_1ACCESSIBLE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1GTK_1ACCESSIBLE_FUNC);
	rc = (jintLong)GTK_ACCESSIBLE(arg0);
	ATK_NATIVE_EXIT(env, that, _1GTK_1ACCESSIBLE_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1focus_1tracker_1notify
JNIEXPORT void JNICALL ATK_NATIVE(_1atk_1focus_1tracker_1notify)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	ATK_NATIVE_ENTER(env, that, _1atk_1focus_1tracker_1notify_FUNC);
	atk_focus_tracker_notify((AtkObject *)arg0);
	ATK_NATIVE_EXIT(env, that, _1atk_1focus_1tracker_1notify_FUNC);
}
#endif

#ifndef NO__1atk_1get_1default_1registry
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1get_1default_1registry)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1get_1default_1registry_FUNC);
	rc = (jintLong)atk_get_default_registry();
	ATK_NATIVE_EXIT(env, that, _1atk_1get_1default_1registry_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1object_1factory_1create_1accessible
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1object_1factory_1create_1accessible)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1object_1factory_1create_1accessible_FUNC);
	rc = (jintLong)atk_object_factory_create_accessible((AtkObjectFactory *)arg0, (GObject *)arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1object_1factory_1create_1accessible_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1object_1factory_1get_1accessible_1type
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1object_1factory_1get_1accessible_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1object_1factory_1get_1accessible_1type_FUNC);
	rc = (jintLong)atk_object_factory_get_accessible_type((AtkObjectFactory *)arg0);
	ATK_NATIVE_EXIT(env, that, _1atk_1object_1factory_1get_1accessible_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1object_1initialize
JNIEXPORT void JNICALL ATK_NATIVE(_1atk_1object_1initialize)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	ATK_NATIVE_ENTER(env, that, _1atk_1object_1initialize_FUNC);
	atk_object_initialize((AtkObject *)arg0, (gpointer)arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1object_1initialize_FUNC);
}
#endif

#ifndef NO__1atk_1object_1ref_1relation_1set
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1object_1ref_1relation_1set)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1object_1ref_1relation_1set_FUNC);
	rc = (jintLong)atk_object_ref_relation_set((AtkObject *)arg0);
	ATK_NATIVE_EXIT(env, that, _1atk_1object_1ref_1relation_1set_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1registry_1get_1factory
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1registry_1get_1factory)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1registry_1get_1factory_FUNC);
	rc = (jintLong)atk_registry_get_factory((AtkRegistry *)arg0, (GType)arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1registry_1get_1factory_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1registry_1set_1factory_1type
JNIEXPORT void JNICALL ATK_NATIVE(_1atk_1registry_1set_1factory_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	ATK_NATIVE_ENTER(env, that, _1atk_1registry_1set_1factory_1type_FUNC);
	atk_registry_set_factory_type((AtkRegistry *)arg0, (GType)arg1, (GType)arg2);
	ATK_NATIVE_EXIT(env, that, _1atk_1registry_1set_1factory_1type_FUNC);
}
#endif

#ifndef NO__1atk_1relation_1set_1get_1n_1relations
JNIEXPORT jint JNICALL ATK_NATIVE(_1atk_1relation_1set_1get_1n_1relations)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1relation_1set_1get_1n_1relations_FUNC);
	rc = (jint)atk_relation_set_get_n_relations((AtkRelationSet *)arg0);
	ATK_NATIVE_EXIT(env, that, _1atk_1relation_1set_1get_1n_1relations_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1relation_1set_1get_1relation
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1relation_1set_1get_1relation)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1relation_1set_1get_1relation_FUNC);
	rc = (jintLong)atk_relation_set_get_relation((AtkRelationSet *)arg0, arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1relation_1set_1get_1relation_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1relation_1set_1remove
JNIEXPORT void JNICALL ATK_NATIVE(_1atk_1relation_1set_1remove)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	ATK_NATIVE_ENTER(env, that, _1atk_1relation_1set_1remove_FUNC);
	atk_relation_set_remove((AtkRelationSet *)arg0, (AtkRelation *)arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1relation_1set_1remove_FUNC);
}
#endif

#ifndef NO__1atk_1state_1set_1add_1state
JNIEXPORT jboolean JNICALL ATK_NATIVE(_1atk_1state_1set_1add_1state)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1state_1set_1add_1state_FUNC);
	rc = (jboolean)atk_state_set_add_state((AtkStateSet *)arg0, (AtkStateType)arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1state_1set_1add_1state_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1state_1set_1new
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1state_1set_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1state_1set_1new_FUNC);
	rc = (jintLong)atk_state_set_new();
	ATK_NATIVE_EXIT(env, that, _1atk_1state_1set_1new_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1call__II) && !defined(JNI64)) || (!defined(NO__1call__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__II_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__II_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1call__III) && !defined(JNI64)) || (!defined(NO__1call__JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__III_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__III_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1call__IIII) && !defined(JNI64)) || (!defined(NO__1call__JJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__IIII_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2, arg3);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__IIII_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1call__IIIII) && !defined(JNI64)) || (!defined(NO__1call__JJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__IIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__IIIII_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2, arg3, arg4);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__IIIII_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1call__IIIIII) && !defined(JNI64)) || (!defined(NO__1call__JJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__IIIIII_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__IIIIII_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1call__IIIIIII) && !defined(JNI64)) || (!defined(NO__1call__JJJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__IIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#else
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1call__JJJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, _1call__IIIIIII_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, _1call__JJJJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, _1call__IIIIIII_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, _1call__JJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC);
#endif
	if (arg1) getAtkActionIfaceFields(env, arg1, (AtkActionIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC);
#endif
	if (arg1) getAtkComponentIfaceFields(env, arg1, (AtkComponentIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC);
#endif
	if (arg1) getAtkHypertextIfaceFields(env, arg1, (AtkHypertextIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC);
#endif
	if (arg1) getAtkObjectClassFields(env, arg1, (AtkObjectClass *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC);
#endif
	if (arg1) getAtkObjectFactoryClassFields(env, arg1, (AtkObjectFactoryClass *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC);
#endif
	if (arg1) getAtkSelectionIfaceFields(env, arg1, (AtkSelectionIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC);
#endif
	if (arg1) getAtkTextIfaceFields(env, arg1, (AtkTextIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J_FUNC);
#endif
	if (arg0) setAtkActionIfaceFields(env, arg0, (AtkActionIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J_FUNC);
#endif
	if (arg0) setAtkComponentIfaceFields(env, arg0, (AtkComponentIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J_FUNC);
#endif
	if (arg0) setAtkHypertextIfaceFields(env, arg0, (AtkHypertextIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J_FUNC);
#endif
	if (arg0) setAtkObjectClassFields(env, arg0, (AtkObjectClass *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J_FUNC);
#endif
	if (arg0) setAtkObjectFactoryClassFields(env, arg0, (AtkObjectFactoryClass *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J_FUNC);
#endif
	if (arg0) setAtkSelectionIfaceFields(env, arg0, (AtkSelectionIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J_FUNC);
#endif
	if (arg0) setAtkTextIfaceFields(env, arg0, (AtkTextIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J_FUNC);
#endif
	if (arg0) setGtkAccessibleFields(env, arg0, (GtkAccessible *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J_FUNC);
#endif
}
#endif

