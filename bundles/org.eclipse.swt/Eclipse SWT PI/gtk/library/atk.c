/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others. All rights reserved.
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

#ifndef ATK_NATIVE
#define ATK_NATIVE(func) Java_org_eclipse_swt_internal_accessibility_gtk_ATK_##func
#endif

#ifndef NO_ATK_1IS_1NO_1OP_1OBJECT_1FACTORY
JNIEXPORT jboolean JNICALL ATK_NATIVE(ATK_1IS_1NO_1OP_1OBJECT_1FACTORY)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1IS_1NO_1OP_1OBJECT_1FACTORY_FUNC);
	rc = (jboolean)ATK_IS_NO_OP_OBJECT_FACTORY(arg0);
	ATK_NATIVE_EXIT(env, that, ATK_1IS_1NO_1OP_1OBJECT_1FACTORY_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1ACTION
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1ACTION)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1ACTION_FUNC);
	rc = (jintLong)ATK_TYPE_ACTION;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1ACTION_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1COMPONENT
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1COMPONENT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1COMPONENT_FUNC);
	rc = (jintLong)ATK_TYPE_COMPONENT;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1COMPONENT_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1EDITABLE_1TEXT
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1EDITABLE_1TEXT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1EDITABLE_1TEXT_FUNC);
	rc = (jintLong)ATK_TYPE_EDITABLE_TEXT;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1EDITABLE_1TEXT_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1HYPERTEXT
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1HYPERTEXT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1HYPERTEXT_FUNC);
	rc = (jintLong)ATK_TYPE_HYPERTEXT;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1HYPERTEXT_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1OBJECT_1FACTORY
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1OBJECT_1FACTORY)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1OBJECT_1FACTORY_FUNC);
	rc = (jintLong)ATK_TYPE_OBJECT_FACTORY;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1OBJECT_1FACTORY_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1SELECTION
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1SELECTION)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1SELECTION_FUNC);
	rc = (jintLong)ATK_TYPE_SELECTION;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1SELECTION_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1TABLE
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1TABLE)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1TABLE_FUNC);
	rc = (jintLong)ATK_TYPE_TABLE;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1TABLE_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1TEXT
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1TEXT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1TEXT_FUNC);
	rc = (jintLong)ATK_TYPE_TEXT;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1TEXT_FUNC);
	return rc;
}
#endif

#ifndef NO_ATK_1TYPE_1VALUE
JNIEXPORT jintLong JNICALL ATK_NATIVE(ATK_1TYPE_1VALUE)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, ATK_1TYPE_1VALUE_FUNC);
	rc = (jintLong)ATK_TYPE_VALUE;
	ATK_NATIVE_EXIT(env, that, ATK_1TYPE_1VALUE_FUNC);
	return rc;
}
#endif

#ifndef NO_AtkAttribute_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkAttribute_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, AtkAttribute_1sizeof_FUNC);
	rc = (jint)AtkAttribute_sizeof();
	ATK_NATIVE_EXIT(env, that, AtkAttribute_1sizeof_FUNC);
	return rc;
}
#endif

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

#ifndef NO_AtkTextRange_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkTextRange_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, AtkTextRange_1sizeof_FUNC);
	rc = (jint)AtkTextRange_sizeof();
	ATK_NATIVE_EXIT(env, that, AtkTextRange_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_AtkTextRectangle_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkTextRectangle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, AtkTextRectangle_1sizeof_FUNC);
	rc = (jint)AtkTextRectangle_sizeof();
	ATK_NATIVE_EXIT(env, that, AtkTextRectangle_1sizeof_FUNC);
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

#ifndef NO__1ATK_1EDITABLE_1TEXT_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1EDITABLE_1TEXT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1EDITABLE_1TEXT_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_EDITABLE_TEXT_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1EDITABLE_1TEXT_1GET_1IFACE_FUNC);
	return rc;
}
#endif

#ifndef NO__1ATK_1HYPERTEXT_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1HYPERTEXT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1HYPERTEXT_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_HYPERTEXT_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1HYPERTEXT_1GET_1IFACE_FUNC);
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

#ifndef NO__1ATK_1TABLE_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1TABLE_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1TABLE_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_TABLE_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1TABLE_1GET_1IFACE_FUNC);
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

#ifndef NO__1ATK_1VALUE_1GET_1IFACE
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1ATK_1VALUE_1GET_1IFACE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1ATK_1VALUE_1GET_1IFACE_FUNC);
	rc = (jintLong)ATK_VALUE_GET_IFACE(arg0);
	ATK_NATIVE_EXIT(env, that, _1ATK_1VALUE_1GET_1IFACE_FUNC);
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

#ifndef NO__1atk_1object_1notify_1state_1change
JNIEXPORT void JNICALL ATK_NATIVE(_1atk_1object_1notify_1state_1change)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2)
{
	ATK_NATIVE_ENTER(env, that, _1atk_1object_1notify_1state_1change_FUNC);
	atk_object_notify_state_change((AtkObject *)arg0, arg1, arg2);
	ATK_NATIVE_EXIT(env, that, _1atk_1object_1notify_1state_1change_FUNC);
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

#ifndef NO__1atk_1role_1register
JNIEXPORT jint JNICALL ATK_NATIVE(_1atk_1role_1register)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1role_1register_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)atk_role_register((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	ATK_NATIVE_EXIT(env, that, _1atk_1role_1register_FUNC);
	return rc;
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

#ifndef NO__1atk_1text_1attribute_1get_1name
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1text_1attribute_1get_1name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1text_1attribute_1get_1name_FUNC);
	rc = (jintLong)atk_text_attribute_get_name(arg0);
	ATK_NATIVE_EXIT(env, that, _1atk_1text_1attribute_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1text_1attribute_1get_1value
JNIEXPORT jintLong JNICALL ATK_NATIVE(_1atk_1text_1attribute_1get_1value)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1text_1attribute_1get_1value_FUNC);
	rc = (jintLong)atk_text_attribute_get_value(arg0, arg1);
	ATK_NATIVE_EXIT(env, that, _1atk_1text_1attribute_1get_1value_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1text_1attribute_1register
JNIEXPORT jint JNICALL ATK_NATIVE(_1atk_1text_1attribute_1register)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	ATK_NATIVE_ENTER(env, that, _1atk_1text_1attribute_1register_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)atk_text_attribute_register((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	ATK_NATIVE_EXIT(env, that, _1atk_1text_1attribute_1register_FUNC);
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

#ifndef NO_g_1strdup
JNIEXPORT jintLong JNICALL ATK_NATIVE(g_1strdup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	ATK_NATIVE_ENTER(env, that, g_1strdup_FUNC);
	rc = (jintLong)g_strdup((char *)arg0);
	ATK_NATIVE_EXIT(env, that, g_1strdup_FUNC);
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

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	AtkAttribute _arg1, *lparg1=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getAtkAttributeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC);
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

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC);
#endif
	if (arg1) getAtkEditableTextIfaceFields(env, arg1, (AtkEditableTextIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC);
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

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC);
#endif
	if (arg1) getAtkTableIfaceFields(env, arg1, (AtkTableIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC);
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

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	AtkTextRange _arg1, *lparg1=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getAtkTextRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	AtkTextRectangle _arg1, *lparg1=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getAtkTextRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC);
#endif
	if (arg1) getAtkValueIfaceFields(env, arg1, (AtkValueIface *)arg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC);
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

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	AtkAttribute _arg0, *lparg0=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getAtkAttributeFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setAtkAttributeFields(env, arg0, lparg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI_FUNC);
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

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2J_FUNC);
#endif
	if (arg0) setAtkEditableTextIfaceFields(env, arg0, (AtkEditableTextIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2J_FUNC);
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

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J_FUNC);
#endif
	if (arg0) setAtkTableIfaceFields(env, arg0, (AtkTableIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J_FUNC);
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

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	AtkTextRange _arg0, *lparg0=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getAtkTextRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setAtkTextRangeFields(env, arg0, lparg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	AtkTextRectangle _arg0, *lparg0=NULL;
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getAtkTextRectangleFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setAtkTextRectangleFields(env, arg0, lparg0);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I_FUNC);
#else
	ATK_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J_FUNC);
#endif
	if (arg0) setAtkValueIfaceFields(env, arg0, (AtkValueIface *)arg1);
#ifndef JNI64
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I_FUNC);
#else
	ATK_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J_FUNC);
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

