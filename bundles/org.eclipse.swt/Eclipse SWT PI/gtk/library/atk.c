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

#define ATK_NATIVE(func) Java_org_eclipse_swt_internal_accessibility_gtk_ATK_##func

#ifndef NO_ATK_1ACTION_1GET_1IFACE
JNIEXPORT jint JNICALL ATK_NATIVE(ATK_1ACTION_1GET_1IFACE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATK_1ACTION_1GET_1IFACE\n")
	rc = (jint)ATK_ACTION_GET_IFACE(arg0);
	NATIVE_EXIT(env, that, "ATK_1ACTION_1GET_1IFACE\n")
	return rc;
}
#endif

#ifndef NO_ATK_1COMPONENT_1GET_1IFACE
JNIEXPORT jint JNICALL ATK_NATIVE(ATK_1COMPONENT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATK_1COMPONENT_1GET_1IFACE\n")
	rc = (jint)ATK_COMPONENT_GET_IFACE(arg0);
	NATIVE_EXIT(env, that, "ATK_1COMPONENT_1GET_1IFACE\n")
	return rc;
}
#endif

#ifndef NO_ATK_1OBJECT_1FACTORY_1CLASS
JNIEXPORT jint JNICALL ATK_NATIVE(ATK_1OBJECT_1FACTORY_1CLASS)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATK_1OBJECT_1FACTORY_1CLASS\n")
	rc = (jint)ATK_OBJECT_FACTORY_CLASS(arg0);
	NATIVE_EXIT(env, that, "ATK_1OBJECT_1FACTORY_1CLASS\n")
	return rc;
}
#endif

#ifndef NO_ATK_1SELECTION_1GET_1IFACE
JNIEXPORT jint JNICALL ATK_NATIVE(ATK_1SELECTION_1GET_1IFACE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATK_1SELECTION_1GET_1IFACE\n")
	rc = (jint)ATK_SELECTION_GET_IFACE(arg0);
	NATIVE_EXIT(env, that, "ATK_1SELECTION_1GET_1IFACE\n")
	return rc;
}
#endif

#ifndef NO_ATK_1TEXT_1GET_1IFACE
JNIEXPORT jint JNICALL ATK_NATIVE(ATK_1TEXT_1GET_1IFACE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATK_1TEXT_1GET_1IFACE\n")
	rc = (jint)ATK_TEXT_GET_IFACE(arg0);
	NATIVE_EXIT(env, that, "ATK_1TEXT_1GET_1IFACE\n")
	return rc;
}
#endif

#ifndef NO_AtkObjectFactoryClass_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkObjectFactoryClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactoryClass_1sizeof\n")
	rc = (jint)AtkObjectFactoryClass_sizeof();
	NATIVE_EXIT(env, that, "AtkObjectFactoryClass_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_AtkObjectFactory_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(AtkObjectFactory_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactory_1sizeof\n")
	rc = (jint)AtkObjectFactory_sizeof();
	NATIVE_EXIT(env, that, "AtkObjectFactory_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GInterfaceInfo_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(GInterfaceInfo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GInterfaceInfo_1sizeof\n")
	rc = (jint)GInterfaceInfo_sizeof();
	NATIVE_EXIT(env, that, "GInterfaceInfo_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GTK_1ACCESSIBLE
JNIEXPORT jint JNICALL ATK_NATIVE(GTK_1ACCESSIBLE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1ACCESSIBLE\n")
	rc = (jint)GTK_ACCESSIBLE(arg0);
	NATIVE_EXIT(env, that, "GTK_1ACCESSIBLE\n")
	return rc;
}
#endif

#ifndef NO_GTypeInfo_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(GTypeInfo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTypeInfo_1sizeof\n")
	rc = (jint)GTypeInfo_sizeof();
	NATIVE_EXIT(env, that, "GTypeInfo_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GTypeQuery_1sizeof
JNIEXPORT jint JNICALL ATK_NATIVE(GTypeQuery_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTypeQuery_1sizeof\n")
	rc = (jint)GTypeQuery_sizeof();
	NATIVE_EXIT(env, that, "GTypeQuery_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_G_1OBJECT_1CLASS
JNIEXPORT jint JNICALL ATK_NATIVE(G_1OBJECT_1CLASS)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "G_1OBJECT_1CLASS\n")
	rc = (jint)G_OBJECT_CLASS(arg0);
	NATIVE_EXIT(env, that, "G_1OBJECT_1CLASS\n")
	return rc;
}
#endif

#ifndef NO_G_1OBJECT_1GET_1CLASS
JNIEXPORT jint JNICALL ATK_NATIVE(G_1OBJECT_1GET_1CLASS)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "G_1OBJECT_1GET_1CLASS\n")
	rc = (jint)G_OBJECT_GET_CLASS(arg0);
	NATIVE_EXIT(env, that, "G_1OBJECT_1GET_1CLASS\n")
	return rc;
}
#endif

#ifndef NO_G_1TYPE_1FROM_1INSTANCE
JNIEXPORT jint JNICALL ATK_NATIVE(G_1TYPE_1FROM_1INSTANCE)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "G_1TYPE_1FROM_1INSTANCE\n")
	rc = (jint)G_TYPE_FROM_INSTANCE(arg0);
	NATIVE_EXIT(env, that, "G_1TYPE_1FROM_1INSTANCE\n")
	return rc;
}
#endif

#ifndef NO_atk_1focus_1tracker_1notify
JNIEXPORT void JNICALL ATK_NATIVE(atk_1focus_1tracker_1notify)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "atk_1focus_1tracker_1notify\n")
	atk_focus_tracker_notify((AtkObject *)arg0);
	NATIVE_EXIT(env, that, "atk_1focus_1tracker_1notify\n")
}
#endif

#ifndef NO_atk_1get_1default_1registry
JNIEXPORT jint JNICALL ATK_NATIVE(atk_1get_1default_1registry)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "atk_1get_1default_1registry\n")
	rc = (jint)atk_get_default_registry();
	NATIVE_EXIT(env, that, "atk_1get_1default_1registry\n")
	return rc;
}
#endif

#ifndef NO_atk_1object_1factory_1get_1accessible_1type
JNIEXPORT jint JNICALL ATK_NATIVE(atk_1object_1factory_1get_1accessible_1type)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "atk_1object_1factory_1get_1accessible_1type\n")
	rc = (jint)atk_object_factory_get_accessible_type((AtkObjectFactory *)arg0);
	NATIVE_EXIT(env, that, "atk_1object_1factory_1get_1accessible_1type\n")
	return rc;
}
#endif

#ifndef NO_atk_1object_1initialize
JNIEXPORT void JNICALL ATK_NATIVE(atk_1object_1initialize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "atk_1object_1initialize\n")
	atk_object_initialize((AtkObject *)arg0, (gpointer)arg1);
	NATIVE_EXIT(env, that, "atk_1object_1initialize\n")
}
#endif

#ifndef NO_atk_1registry_1get_1factory
JNIEXPORT jint JNICALL ATK_NATIVE(atk_1registry_1get_1factory)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "atk_1registry_1get_1factory\n")
	rc = (jint)atk_registry_get_factory((AtkRegistry *)arg0, (GType)arg1);
	NATIVE_EXIT(env, that, "atk_1registry_1get_1factory\n")
	return rc;
}
#endif

#ifndef NO_atk_1registry_1set_1factory_1type
JNIEXPORT void JNICALL ATK_NATIVE(atk_1registry_1set_1factory_1type)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "atk_1registry_1set_1factory_1type\n")
	atk_registry_set_factory_type((AtkRegistry *)arg0, (GType)arg1, (GType)arg2);
	NATIVE_EXIT(env, that, "atk_1registry_1set_1factory_1type\n")
}
#endif

#ifndef NO_atk_1state_1set_1add_1state
JNIEXPORT jboolean JNICALL ATK_NATIVE(atk_1state_1set_1add_1state)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "atk_1state_1set_1add_1state\n")
	rc = (jboolean)atk_state_set_add_state((AtkStateSet *)arg0, (AtkStateType)arg1);
	NATIVE_EXIT(env, that, "atk_1state_1set_1add_1state\n")
	return rc;
}
#endif

#ifndef NO_atk_1state_1set_1new
JNIEXPORT jint JNICALL ATK_NATIVE(atk_1state_1set_1new)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "atk_1state_1set_1new\n")
	rc = (jint)atk_state_set_new();
	NATIVE_EXIT(env, that, "atk_1state_1set_1new\n")
	return rc;
}
#endif

#ifndef NO_call__II
JNIEXPORT jint JNICALL ATK_NATIVE(call__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__II\n")
	rc = (jint)((jint (*)())arg0)(arg1);
	NATIVE_EXIT(env, that, "call__II\n")
	return rc;
}
#endif

#ifndef NO_call__III
JNIEXPORT jint JNICALL ATK_NATIVE(call__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__III\n")
	rc = (jint)((jint (*)())arg0)(arg1, arg2);
	NATIVE_EXIT(env, that, "call__III\n")
	return rc;
}
#endif

#ifndef NO_call__IIII
JNIEXPORT jint JNICALL ATK_NATIVE(call__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__IIII\n")
	rc = (jint)((jint (*)())arg0)(arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "call__IIII\n")
	return rc;
}
#endif

#ifndef NO_call__IIIII
JNIEXPORT jint JNICALL ATK_NATIVE(call__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__IIIII\n")
	rc = (jint)((jint (*)())arg0)(arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "call__IIIII\n")
	return rc;
}
#endif

#ifndef NO_call__IIIIII
JNIEXPORT jint JNICALL ATK_NATIVE(call__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__IIIIII\n")
	rc = (jint)((jint (*)())arg0)(arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "call__IIIIII\n")
	return rc;
}
#endif

#ifndef NO_call__IIIIIII
JNIEXPORT jint JNICALL ATK_NATIVE(call__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc;
	NATIVE_ENTER(env, that, "call__IIIIIII\n")
	rc = (jint)((jint (*)())arg0)(arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "call__IIIIIII\n")
	return rc;
}
#endif

#ifndef NO_g_1object_1new
JNIEXPORT jint JNICALL ATK_NATIVE(g_1object_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1object_1new\n")
	rc = (jint)g_object_new((GType)arg0, (const gchar *)arg1);
	NATIVE_EXIT(env, that, "g_1object_1new\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1add_1interface_1static
JNIEXPORT void JNICALL ATK_NATIVE(g_1type_1add_1interface_1static)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "g_1type_1add_1interface_1static\n")
	g_type_add_interface_static((GType)arg0, (GType)arg1, (const GInterfaceInfo *)arg2);
	NATIVE_EXIT(env, that, "g_1type_1add_1interface_1static\n")
}
#endif

#ifndef NO_g_1type_1class_1peek
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1class_1peek)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1class_1peek\n")
	rc = (jint)g_type_class_peek((GType)arg0);
	NATIVE_EXIT(env, that, "g_1type_1class_1peek\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1class_1peek_1parent
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1class_1peek_1parent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1class_1peek_1parent\n")
	rc = (jint)g_type_class_peek_parent((gpointer)arg0);
	NATIVE_EXIT(env, that, "g_1type_1class_1peek_1parent\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1from_1name
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1from_1name)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1from_1name\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)g_type_from_name(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "g_1type_1from_1name\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1interface_1peek_1parent
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1interface_1peek_1parent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1interface_1peek_1parent\n")
	rc = (jint)g_type_interface_peek_parent((gpointer)arg0);
	NATIVE_EXIT(env, that, "g_1type_1interface_1peek_1parent\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1is_1a
JNIEXPORT jboolean JNICALL ATK_NATIVE(g_1type_1is_1a)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "g_1type_1is_1a\n")
	rc = (jboolean)g_type_is_a((GType)arg0, (GType)arg1);
	NATIVE_EXIT(env, that, "g_1type_1is_1a\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1name
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1name\n")
	rc = (jint)g_type_name((GType)arg0);
	NATIVE_EXIT(env, that, "g_1type_1name\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1parent
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1parent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1parent\n")
	rc = (jint)g_type_parent((GType)arg0);
	NATIVE_EXIT(env, that, "g_1type_1parent\n")
	return rc;
}
#endif

#ifndef NO_g_1type_1query
JNIEXPORT void JNICALL ATK_NATIVE(g_1type_1query)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "g_1type_1query\n")
	g_type_query((GType)arg0, (GTypeQuery *)arg1);
	NATIVE_EXIT(env, that, "g_1type_1query\n")
}
#endif

#ifndef NO_g_1type_1register_1static
JNIEXPORT jint JNICALL ATK_NATIVE(g_1type_1register_1static)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "g_1type_1register_1static\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)g_type_register_static((GType)arg0, lparg1, (const GTypeInfo *)arg2, (GTypeFlags)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "g_1type_1register_1static\n")
	return rc;
}
#endif

#ifndef NO_gtk_1widget_1get_1toplevel
JNIEXPORT jint JNICALL ATK_NATIVE(gtk_1widget_1get_1toplevel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gtk_1widget_1get_1toplevel\n")
	rc = (jint)gtk_widget_get_toplevel((GtkWidget *)arg0);
	NATIVE_EXIT(env, that, "gtk_1widget_1get_1toplevel\n")
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2\n")
	if (arg1) getAtkActionIfaceFields(env, arg1, (AtkActionIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2\n")
	if (arg1) getAtkComponentIfaceFields(env, arg1, (AtkComponentIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2\n")
	if (arg1) getAtkObjectClassFields(env, arg1, (AtkObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2\n")
	if (arg1) getAtkObjectFactoryClassFields(env, arg1, (AtkObjectFactoryClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2\n")
	if (arg1) getAtkSelectionIfaceFields(env, arg1, (AtkSelectionIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2\n")
	if (arg1) getAtkTextIfaceFields(env, arg1, (AtkTextIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GInterfaceInfo_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GInterfaceInfo_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GInterfaceInfo _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GInterfaceInfo_2I\n")
	if (arg1) lparg1 = getGInterfaceInfoFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GInterfaceInfo_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2\n")
	if (arg1) getGObjectClassFields(env, arg1, (GObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GTypeInfo_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GTypeInfo_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GTypeInfo _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GTypeInfo_2I\n")
	if (arg1) lparg1 = getGTypeInfoFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GTypeInfo_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I\n")
	if (arg0) setAtkActionIfaceFields(env, arg0, (AtkActionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I\n")
	if (arg0) setAtkComponentIfaceFields(env, arg0, (AtkComponentIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I\n")
	if (arg0) setAtkObjectClassFields(env, arg0, (AtkObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I\n")
	if (arg0) setAtkObjectFactoryClassFields(env, arg0, (AtkObjectFactoryClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I\n")
	if (arg0) setAtkSelectionIfaceFields(env, arg0, (AtkSelectionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I\n")
	if (arg0) setAtkTextIfaceFields(env, arg0, (AtkTextIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I\n")
	if (arg0) setGObjectClassFields(env, arg0, (GObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GTypeQuery_2II
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GTypeQuery_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GTypeQuery _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GTypeQuery_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGTypeQueryFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GTypeQuery_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I
JNIEXPORT void JNICALL ATK_NATIVE(memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I\n")
	if (arg0) setGtkAccessibleFields(env, arg0, (GtkAccessible *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I\n")
}
#endif

