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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_ATK_##func

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

