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

#ifndef NO_AtkActionIface
typedef struct AtkActionIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID do_action, get_n_actions, get_description, get_name, get_keybinding, set_description;
} AtkActionIface_FID_CACHE;

AtkActionIface_FID_CACHE AtkActionIfaceFc;

void cacheAtkActionIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkActionIfaceFc.cached) return;
	AtkActionIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkActionIfaceFc.do_action = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "do_action", "I");
	AtkActionIfaceFc.get_n_actions = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_n_actions", "I");
	AtkActionIfaceFc.get_description = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_description", "I");
	AtkActionIfaceFc.get_name = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_name", "I");
	AtkActionIfaceFc.get_keybinding = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_keybinding", "I");
	AtkActionIfaceFc.set_description = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "set_description", "I");
	AtkActionIfaceFc.cached = 1;
}

AtkActionIface *getAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct)
{
	if (!AtkActionIfaceFc.cached) cacheAtkActionIfaceFields(env, lpObject);
	lpStruct->do_action = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.do_action);
	lpStruct->get_n_actions = (gint (*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.get_n_actions);
	lpStruct->get_description = (G_CONST_RETURN gchar *(*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.get_description);
	lpStruct->get_name = (G_CONST_RETURN gchar *(*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.get_name);
	lpStruct->get_keybinding = (G_CONST_RETURN gchar *(*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.get_keybinding);
	lpStruct->set_description = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkActionIfaceFc.set_description);
	return lpStruct;
}

void setAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct)
{
	if (!AtkActionIfaceFc.cached) cacheAtkActionIfaceFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.do_action, (jint)lpStruct->do_action);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.get_n_actions, (jint)lpStruct->get_n_actions);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.get_description, (jint)lpStruct->get_description);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.get_name, (jint)lpStruct->get_name);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.get_keybinding, (jint)lpStruct->get_keybinding);
	(*env)->SetIntField(env, lpObject, AtkActionIfaceFc.set_description, (jint)lpStruct->set_description);
}
#endif

#ifndef NO_AtkComponentIface
typedef struct AtkComponentIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID add_focus_handler, contains, ref_accessible_at_point, get_extents, get_position, get_size, grab_focus, remove_focus_handler, set_extents, set_position, set_size, get_layer, get_mdi_zorder;
} AtkComponentIface_FID_CACHE;

AtkComponentIface_FID_CACHE AtkComponentIfaceFc;

void cacheAtkComponentIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkComponentIfaceFc.cached) return;
	AtkComponentIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkComponentIfaceFc.add_focus_handler = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "add_focus_handler", "I");
	AtkComponentIfaceFc.contains = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "contains", "I");
	AtkComponentIfaceFc.ref_accessible_at_point = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "ref_accessible_at_point", "I");
	AtkComponentIfaceFc.get_extents = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_extents", "I");
	AtkComponentIfaceFc.get_position = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_position", "I");
	AtkComponentIfaceFc.get_size = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_size", "I");
	AtkComponentIfaceFc.grab_focus = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "grab_focus", "I");
	AtkComponentIfaceFc.remove_focus_handler = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "remove_focus_handler", "I");
	AtkComponentIfaceFc.set_extents = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_extents", "I");
	AtkComponentIfaceFc.set_position = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_position", "I");
	AtkComponentIfaceFc.set_size = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_size", "I");
	AtkComponentIfaceFc.get_layer = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_layer", "I");
	AtkComponentIfaceFc.get_mdi_zorder = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_mdi_zorder", "I");
	AtkComponentIfaceFc.cached = 1;
}

AtkComponentIface *getAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct)
{
	if (!AtkComponentIfaceFc.cached) cacheAtkComponentIfaceFields(env, lpObject);
	lpStruct->add_focus_handler = (guint (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.add_focus_handler);
	lpStruct->contains = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.contains);
	lpStruct->ref_accessible_at_point = (AtkObject *(*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.ref_accessible_at_point);
	lpStruct->get_extents = (void (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.get_extents);
	lpStruct->get_position = (void (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.get_position);
	lpStruct->get_size = (void (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.get_size);
	lpStruct->grab_focus = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.grab_focus);
	lpStruct->remove_focus_handler = (void (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.remove_focus_handler);
	lpStruct->set_extents = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.set_extents);
	lpStruct->set_position = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.set_position);
	lpStruct->set_size = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.set_size);
	lpStruct->get_layer = (AtkLayer (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.get_layer);
	lpStruct->get_mdi_zorder = (gint (*)())(*env)->GetIntField(env, lpObject, AtkComponentIfaceFc.get_mdi_zorder);
	return lpStruct;
}

void setAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct)
{
	if (!AtkComponentIfaceFc.cached) cacheAtkComponentIfaceFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.add_focus_handler, (jint)lpStruct->add_focus_handler);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.contains, (jint)lpStruct->contains);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.ref_accessible_at_point, (jint)lpStruct->ref_accessible_at_point);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.get_extents, (jint)lpStruct->get_extents);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.get_position, (jint)lpStruct->get_position);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.get_size, (jint)lpStruct->get_size);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.grab_focus, (jint)lpStruct->grab_focus);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.remove_focus_handler, (jint)lpStruct->remove_focus_handler);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.set_extents, (jint)lpStruct->set_extents);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.set_position, (jint)lpStruct->set_position);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.set_size, (jint)lpStruct->set_size);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.get_layer, (jint)lpStruct->get_layer);
	(*env)->SetIntField(env, lpObject, AtkComponentIfaceFc.get_mdi_zorder, (jint)lpStruct->get_mdi_zorder);
}
#endif

#ifndef NO_AtkObjectClass
typedef struct AtkObjectClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID get_name, get_description, get_parent, get_n_children, ref_child, get_index_in_parent, ref_relation_set, get_role, get_layer, get_mdi_zorder, ref_state_set, set_name, set_description, set_parent, set_role, connect_property_change_handler, remove_property_change_handler, initialize, children_changed, focus_event, property_change, state_change, visible_data_changed;
} AtkObjectClass_FID_CACHE;

AtkObjectClass_FID_CACHE AtkObjectClassFc;

void cacheAtkObjectClassFields(JNIEnv *env, jobject lpObject)
{
	if (AtkObjectClassFc.cached) return;
	AtkObjectClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkObjectClassFc.get_name = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_name", "I");
	AtkObjectClassFc.get_description = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_description", "I");
	AtkObjectClassFc.get_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_parent", "I");
	AtkObjectClassFc.get_n_children = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_n_children", "I");
	AtkObjectClassFc.ref_child = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_child", "I");
	AtkObjectClassFc.get_index_in_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_index_in_parent", "I");
	AtkObjectClassFc.ref_relation_set = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_relation_set", "I");
	AtkObjectClassFc.get_role = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_role", "I");
	AtkObjectClassFc.get_layer = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_layer", "I");
	AtkObjectClassFc.get_mdi_zorder = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_mdi_zorder", "I");
	AtkObjectClassFc.ref_state_set = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_state_set", "I");
	AtkObjectClassFc.set_name = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_name", "I");
	AtkObjectClassFc.set_description = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_description", "I");
	AtkObjectClassFc.set_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_parent", "I");
	AtkObjectClassFc.set_role = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_role", "I");
	AtkObjectClassFc.connect_property_change_handler = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "connect_property_change_handler", "I");
	AtkObjectClassFc.remove_property_change_handler = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "remove_property_change_handler", "I");
	AtkObjectClassFc.initialize = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "initialize", "I");
	AtkObjectClassFc.children_changed = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "children_changed", "I");
	AtkObjectClassFc.focus_event = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "focus_event", "I");
	AtkObjectClassFc.property_change = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "property_change", "I");
	AtkObjectClassFc.state_change = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "state_change", "I");
	AtkObjectClassFc.visible_data_changed = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "visible_data_changed", "I");
	AtkObjectClassFc.cached = 1;
}

AtkObjectClass *getAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct)
{
	if (!AtkObjectClassFc.cached) cacheAtkObjectClassFields(env, lpObject);
	lpStruct->get_name = (G_CONST_RETURN gchar *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_name);
	lpStruct->get_description = (G_CONST_RETURN gchar *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_description);
	lpStruct->get_parent = (AtkObject *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_parent);
	lpStruct->get_n_children = (gint (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_n_children);
	lpStruct->ref_child = (AtkObject *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.ref_child);
	lpStruct->get_index_in_parent = (gint (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_index_in_parent);
	lpStruct->ref_relation_set = (AtkRelationSet *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.ref_relation_set);
	lpStruct->get_role = (AtkRole (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_role);
	lpStruct->get_layer = (AtkLayer (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_layer);
	lpStruct->get_mdi_zorder = (gint (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.get_mdi_zorder);
	lpStruct->ref_state_set = (AtkStateSet *(*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.ref_state_set);
	lpStruct->set_name = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.set_name);
	lpStruct->set_description = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.set_description);
	lpStruct->set_parent = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.set_parent);
	lpStruct->set_role = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.set_role);
	lpStruct->connect_property_change_handler = (guint (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.connect_property_change_handler);
	lpStruct->remove_property_change_handler = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.remove_property_change_handler);
	lpStruct->initialize = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.initialize);
	lpStruct->children_changed = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.children_changed);
	lpStruct->focus_event = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.focus_event);
	lpStruct->property_change = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.property_change);
	lpStruct->state_change = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.state_change);
	lpStruct->visible_data_changed = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectClassFc.visible_data_changed);
	return lpStruct;
}

void setAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct)
{
	if (!AtkObjectClassFc.cached) cacheAtkObjectClassFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_name, (jint)lpStruct->get_name);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_description, (jint)lpStruct->get_description);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_parent, (jint)lpStruct->get_parent);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_n_children, (jint)lpStruct->get_n_children);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.ref_child, (jint)lpStruct->ref_child);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_index_in_parent, (jint)lpStruct->get_index_in_parent);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.ref_relation_set, (jint)lpStruct->ref_relation_set);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_role, (jint)lpStruct->get_role);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_layer, (jint)lpStruct->get_layer);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.get_mdi_zorder, (jint)lpStruct->get_mdi_zorder);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.ref_state_set, (jint)lpStruct->ref_state_set);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.set_name, (jint)lpStruct->set_name);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.set_description, (jint)lpStruct->set_description);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.set_parent, (jint)lpStruct->set_parent);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.set_role, (jint)lpStruct->set_role);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.connect_property_change_handler, (jint)lpStruct->connect_property_change_handler);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.remove_property_change_handler, (jint)lpStruct->remove_property_change_handler);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.initialize, (jint)lpStruct->initialize);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.children_changed, (jint)lpStruct->children_changed);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.focus_event, (jint)lpStruct->focus_event);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.property_change, (jint)lpStruct->property_change);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.state_change, (jint)lpStruct->state_change);
	(*env)->SetIntField(env, lpObject, AtkObjectClassFc.visible_data_changed, (jint)lpStruct->visible_data_changed);
}
#endif

#ifndef NO_AtkObjectFactoryClass
typedef struct AtkObjectFactoryClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID create_accessible, invalidate, get_accessible_type;
} AtkObjectFactoryClass_FID_CACHE;

AtkObjectFactoryClass_FID_CACHE AtkObjectFactoryClassFc;

void cacheAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject)
{
	if (AtkObjectFactoryClassFc.cached) return;
	AtkObjectFactoryClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkObjectFactoryClassFc.create_accessible = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "create_accessible", "I");
	AtkObjectFactoryClassFc.invalidate = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "invalidate", "I");
	AtkObjectFactoryClassFc.get_accessible_type = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "get_accessible_type", "I");
	AtkObjectFactoryClassFc.cached = 1;
}

AtkObjectFactoryClass *getAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct)
{
	if (!AtkObjectFactoryClassFc.cached) cacheAtkObjectFactoryClassFields(env, lpObject);
	lpStruct->create_accessible = (AtkObject *(*)())(*env)->GetIntField(env, lpObject, AtkObjectFactoryClassFc.create_accessible);
	lpStruct->invalidate = (void (*)())(*env)->GetIntField(env, lpObject, AtkObjectFactoryClassFc.invalidate);
	lpStruct->get_accessible_type = (GType (*)())(*env)->GetIntField(env, lpObject, AtkObjectFactoryClassFc.get_accessible_type);
	return lpStruct;
}

void setAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct)
{
	if (!AtkObjectFactoryClassFc.cached) cacheAtkObjectFactoryClassFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkObjectFactoryClassFc.create_accessible, (jint)lpStruct->create_accessible);
	(*env)->SetIntField(env, lpObject, AtkObjectFactoryClassFc.invalidate, (jint)lpStruct->invalidate);
	(*env)->SetIntField(env, lpObject, AtkObjectFactoryClassFc.get_accessible_type, (jint)lpStruct->get_accessible_type);
}
#endif

#ifndef NO_AtkSelectionIface
typedef struct AtkSelectionIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID add_selection, clear_selection, ref_selection, get_selection_count, is_child_selected, remove_selection, select_all_selection, selection_changed;
} AtkSelectionIface_FID_CACHE;

AtkSelectionIface_FID_CACHE AtkSelectionIfaceFc;

void cacheAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkSelectionIfaceFc.cached) return;
	AtkSelectionIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkSelectionIfaceFc.add_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "add_selection", "I");
	AtkSelectionIfaceFc.clear_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "clear_selection", "I");
	AtkSelectionIfaceFc.ref_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "ref_selection", "I");
	AtkSelectionIfaceFc.get_selection_count = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "get_selection_count", "I");
	AtkSelectionIfaceFc.is_child_selected = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "is_child_selected", "I");
	AtkSelectionIfaceFc.remove_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "remove_selection", "I");
	AtkSelectionIfaceFc.select_all_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "select_all_selection", "I");
	AtkSelectionIfaceFc.selection_changed = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "selection_changed", "I");
	AtkSelectionIfaceFc.cached = 1;
}

AtkSelectionIface *getAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct)
{
	if (!AtkSelectionIfaceFc.cached) cacheAtkSelectionIfaceFields(env, lpObject);
	lpStruct->add_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.add_selection);
	lpStruct->clear_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.clear_selection);
	lpStruct->ref_selection = (AtkObject *(*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.ref_selection);
	lpStruct->get_selection_count = (gint (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.get_selection_count);
	lpStruct->is_child_selected = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.is_child_selected);
	lpStruct->remove_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.remove_selection);
	lpStruct->select_all_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.select_all_selection);
	lpStruct->selection_changed = (void (*)())(*env)->GetIntField(env, lpObject, AtkSelectionIfaceFc.selection_changed);
	return lpStruct;
}

void setAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct)
{
	if (!AtkSelectionIfaceFc.cached) cacheAtkSelectionIfaceFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.add_selection, (jint)lpStruct->add_selection);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.clear_selection, (jint)lpStruct->clear_selection);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.ref_selection, (jint)lpStruct->ref_selection);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.get_selection_count, (jint)lpStruct->get_selection_count);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.is_child_selected, (jint)lpStruct->is_child_selected);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.remove_selection, (jint)lpStruct->remove_selection);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.select_all_selection, (jint)lpStruct->select_all_selection);
	(*env)->SetIntField(env, lpObject, AtkSelectionIfaceFc.selection_changed, (jint)lpStruct->selection_changed);
}
#endif

#ifndef NO_AtkTextIface
typedef struct AtkTextIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID get_text, get_text_after_offset, get_text_at_offset, get_character_at_offset, get_text_before_offset, get_caret_offset, get_run_attributes, get_default_attributes, get_character_extents, get_character_count, get_offset_at_point, get_n_selections, get_selection, add_selection, remove_selection, set_selection, set_caret_offset, text_changed, text_caret_moved, text_selection_changed;
} AtkTextIface_FID_CACHE;

AtkTextIface_FID_CACHE AtkTextIfaceFc;

void cacheAtkTextIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkTextIfaceFc.cached) return;
	AtkTextIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkTextIfaceFc.get_text = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text", "I");
	AtkTextIfaceFc.get_text_after_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_after_offset", "I");
	AtkTextIfaceFc.get_text_at_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_at_offset", "I");
	AtkTextIfaceFc.get_character_at_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_at_offset", "I");
	AtkTextIfaceFc.get_text_before_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_before_offset", "I");
	AtkTextIfaceFc.get_caret_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_caret_offset", "I");
	AtkTextIfaceFc.get_run_attributes = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_run_attributes", "I");
	AtkTextIfaceFc.get_default_attributes = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_default_attributes", "I");
	AtkTextIfaceFc.get_character_extents = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_extents", "I");
	AtkTextIfaceFc.get_character_count = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_count", "I");
	AtkTextIfaceFc.get_offset_at_point = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_offset_at_point", "I");
	AtkTextIfaceFc.get_n_selections = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_n_selections", "I");
	AtkTextIfaceFc.get_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_selection", "I");
	AtkTextIfaceFc.add_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "add_selection", "I");
	AtkTextIfaceFc.remove_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "remove_selection", "I");
	AtkTextIfaceFc.set_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "set_selection", "I");
	AtkTextIfaceFc.set_caret_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "set_caret_offset", "I");
	AtkTextIfaceFc.text_changed = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_changed", "I");
	AtkTextIfaceFc.text_caret_moved = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_caret_moved", "I");
	AtkTextIfaceFc.text_selection_changed = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_selection_changed", "I");
	AtkTextIfaceFc.cached = 1;
}

AtkTextIface *getAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct)
{
	if (!AtkTextIfaceFc.cached) cacheAtkTextIfaceFields(env, lpObject);
	lpStruct->get_text = (gchar *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_text);
	lpStruct->get_text_after_offset = (gchar *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_text_after_offset);
	lpStruct->get_text_at_offset = (gchar *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_text_at_offset);
	lpStruct->get_character_at_offset = (gunichar (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_character_at_offset);
	lpStruct->get_text_before_offset = (gchar *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_text_before_offset);
	lpStruct->get_caret_offset = (gint (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_caret_offset);
	lpStruct->get_run_attributes = (AtkAttributeSet *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_run_attributes);
	lpStruct->get_default_attributes = (AtkAttributeSet *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_default_attributes);
	lpStruct->get_character_extents = (void (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_character_extents);
	lpStruct->get_character_count = (gint (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_character_count);
	lpStruct->get_offset_at_point = (gint (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_offset_at_point);
	lpStruct->get_n_selections = (gint (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_n_selections);
	lpStruct->get_selection = (gchar *(*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.get_selection);
	lpStruct->add_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.add_selection);
	lpStruct->remove_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.remove_selection);
	lpStruct->set_selection = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.set_selection);
	lpStruct->set_caret_offset = (gboolean (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.set_caret_offset);
	lpStruct->text_changed = (void (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.text_changed);
	lpStruct->text_caret_moved = (void (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.text_caret_moved);
	lpStruct->text_selection_changed = (void (*)())(*env)->GetIntField(env, lpObject, AtkTextIfaceFc.text_selection_changed);
	return lpStruct;
}

void setAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct)
{
	if (!AtkTextIfaceFc.cached) cacheAtkTextIfaceFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_text, (jint)lpStruct->get_text);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_text_after_offset, (jint)lpStruct->get_text_after_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_text_at_offset, (jint)lpStruct->get_text_at_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_character_at_offset, (jint)lpStruct->get_character_at_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_text_before_offset, (jint)lpStruct->get_text_before_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_caret_offset, (jint)lpStruct->get_caret_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_run_attributes, (jint)lpStruct->get_run_attributes);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_default_attributes, (jint)lpStruct->get_default_attributes);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_character_extents, (jint)lpStruct->get_character_extents);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_character_count, (jint)lpStruct->get_character_count);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_offset_at_point, (jint)lpStruct->get_offset_at_point);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_n_selections, (jint)lpStruct->get_n_selections);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.get_selection, (jint)lpStruct->get_selection);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.add_selection, (jint)lpStruct->add_selection);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.remove_selection, (jint)lpStruct->remove_selection);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.set_selection, (jint)lpStruct->set_selection);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.set_caret_offset, (jint)lpStruct->set_caret_offset);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.text_changed, (jint)lpStruct->text_changed);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.text_caret_moved, (jint)lpStruct->text_caret_moved);
	(*env)->SetIntField(env, lpObject, AtkTextIfaceFc.text_selection_changed, (jint)lpStruct->text_selection_changed);
}
#endif

#ifndef NO_GInterfaceInfo
typedef struct GInterfaceInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID interface_init, interface_finalize, interface_data;
} GInterfaceInfo_FID_CACHE;

GInterfaceInfo_FID_CACHE GInterfaceInfoFc;

void cacheGInterfaceInfoFields(JNIEnv *env, jobject lpObject)
{
	if (GInterfaceInfoFc.cached) return;
	GInterfaceInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GInterfaceInfoFc.interface_init = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_init", "I");
	GInterfaceInfoFc.interface_finalize = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_finalize", "I");
	GInterfaceInfoFc.interface_data = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_data", "I");
	GInterfaceInfoFc.cached = 1;
}

GInterfaceInfo *getGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct)
{
	if (!GInterfaceInfoFc.cached) cacheGInterfaceInfoFields(env, lpObject);
	lpStruct->interface_init = (GInterfaceInitFunc)(*env)->GetIntField(env, lpObject, GInterfaceInfoFc.interface_init);
	lpStruct->interface_finalize = (GInterfaceFinalizeFunc)(*env)->GetIntField(env, lpObject, GInterfaceInfoFc.interface_finalize);
	lpStruct->interface_data = (gpointer)(*env)->GetIntField(env, lpObject, GInterfaceInfoFc.interface_data);
	return lpStruct;
}

void setGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct)
{
	if (!GInterfaceInfoFc.cached) cacheGInterfaceInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GInterfaceInfoFc.interface_init, (jint)lpStruct->interface_init);
	(*env)->SetIntField(env, lpObject, GInterfaceInfoFc.interface_finalize, (jint)lpStruct->interface_finalize);
	(*env)->SetIntField(env, lpObject, GInterfaceInfoFc.interface_data, (jint)lpStruct->interface_data);
}
#endif

#ifndef NO_GObjectClass
typedef struct GObjectClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID constructor, set_property, get_property, dispose, finalize, dispatch_properties_changed, notify;
} GObjectClass_FID_CACHE;

GObjectClass_FID_CACHE GObjectClassFc;

void cacheGObjectClassFields(JNIEnv *env, jobject lpObject)
{
	if (GObjectClassFc.cached) return;
	GObjectClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GObjectClassFc.constructor = (*env)->GetFieldID(env, GObjectClassFc.clazz, "constructor", "I");
	GObjectClassFc.set_property = (*env)->GetFieldID(env, GObjectClassFc.clazz, "set_property", "I");
	GObjectClassFc.get_property = (*env)->GetFieldID(env, GObjectClassFc.clazz, "get_property", "I");
	GObjectClassFc.dispose = (*env)->GetFieldID(env, GObjectClassFc.clazz, "dispose", "I");
	GObjectClassFc.finalize = (*env)->GetFieldID(env, GObjectClassFc.clazz, "finalize", "I");
	GObjectClassFc.dispatch_properties_changed = (*env)->GetFieldID(env, GObjectClassFc.clazz, "dispatch_properties_changed", "I");
	GObjectClassFc.notify = (*env)->GetFieldID(env, GObjectClassFc.clazz, "notify", "I");
	GObjectClassFc.cached = 1;
}

GObjectClass *getGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct)
{
	if (!GObjectClassFc.cached) cacheGObjectClassFields(env, lpObject);
	lpStruct->constructor = (GObject *(*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.constructor);
	lpStruct->set_property = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.set_property);
	lpStruct->get_property = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.get_property);
	lpStruct->dispose = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.dispose);
	lpStruct->finalize = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.finalize);
	lpStruct->dispatch_properties_changed = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.dispatch_properties_changed);
	lpStruct->notify = (void (*)())(*env)->GetIntField(env, lpObject, GObjectClassFc.notify);
	return lpStruct;
}

void setGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct)
{
	if (!GObjectClassFc.cached) cacheGObjectClassFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.constructor, (jint)lpStruct->constructor);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.set_property, (jint)lpStruct->set_property);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.get_property, (jint)lpStruct->get_property);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.dispose, (jint)lpStruct->dispose);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.finalize, (jint)lpStruct->finalize);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.dispatch_properties_changed, (jint)lpStruct->dispatch_properties_changed);
	(*env)->SetIntField(env, lpObject, GObjectClassFc.notify, (jint)lpStruct->notify);
}
#endif

#ifndef NO_GTypeInfo
typedef struct GTypeInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID class_size, base_init, base_finalize, class_init, class_finalize, class_data, instance_size, n_preallocs, instance_init, value_table;
} GTypeInfo_FID_CACHE;

GTypeInfo_FID_CACHE GTypeInfoFc;

void cacheGTypeInfoFields(JNIEnv *env, jobject lpObject)
{
	if (GTypeInfoFc.cached) return;
	GTypeInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GTypeInfoFc.class_size = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_size", "S");
	GTypeInfoFc.base_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "base_init", "I");
	GTypeInfoFc.base_finalize = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "base_finalize", "I");
	GTypeInfoFc.class_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_init", "I");
	GTypeInfoFc.class_finalize = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_finalize", "I");
	GTypeInfoFc.class_data = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_data", "I");
	GTypeInfoFc.instance_size = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "instance_size", "S");
	GTypeInfoFc.n_preallocs = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "n_preallocs", "S");
	GTypeInfoFc.instance_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "instance_init", "I");
	GTypeInfoFc.value_table = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "value_table", "I");
	GTypeInfoFc.cached = 1;
}

GTypeInfo *getGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct)
{
	if (!GTypeInfoFc.cached) cacheGTypeInfoFields(env, lpObject);
	lpStruct->class_size = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.class_size);
	lpStruct->base_init = (GBaseInitFunc)(*env)->GetIntField(env, lpObject, GTypeInfoFc.base_init);
	lpStruct->base_finalize = (GBaseFinalizeFunc)(*env)->GetIntField(env, lpObject, GTypeInfoFc.base_finalize);
	lpStruct->class_init = (GClassInitFunc)(*env)->GetIntField(env, lpObject, GTypeInfoFc.class_init);
	lpStruct->class_finalize = (GClassFinalizeFunc)(*env)->GetIntField(env, lpObject, GTypeInfoFc.class_finalize);
	lpStruct->class_data = (gconstpointer)(*env)->GetIntField(env, lpObject, GTypeInfoFc.class_data);
	lpStruct->instance_size = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.instance_size);
	lpStruct->n_preallocs = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.n_preallocs);
	lpStruct->instance_init = (GInstanceInitFunc)(*env)->GetIntField(env, lpObject, GTypeInfoFc.instance_init);
	lpStruct->value_table = (GTypeValueTable *)(*env)->GetIntField(env, lpObject, GTypeInfoFc.value_table);
	return lpStruct;
}

void setGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct)
{
	if (!GTypeInfoFc.cached) cacheGTypeInfoFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.class_size, (jshort)lpStruct->class_size);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.base_init, (jint)lpStruct->base_init);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.base_finalize, (jint)lpStruct->base_finalize);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.class_init, (jint)lpStruct->class_init);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.class_finalize, (jint)lpStruct->class_finalize);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.class_data, (jint)lpStruct->class_data);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.instance_size, (jshort)lpStruct->instance_size);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.n_preallocs, (jshort)lpStruct->n_preallocs);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.instance_init, (jint)lpStruct->instance_init);
	(*env)->SetIntField(env, lpObject, GTypeInfoFc.value_table, (jint)lpStruct->value_table);
}
#endif

#ifndef NO_GTypeQuery
typedef struct GTypeQuery_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, type_name, class_size, instance_size;
} GTypeQuery_FID_CACHE;

GTypeQuery_FID_CACHE GTypeQueryFc;

void cacheGTypeQueryFields(JNIEnv *env, jobject lpObject)
{
	if (GTypeQueryFc.cached) return;
	GTypeQueryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GTypeQueryFc.type = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "type", "I");
	GTypeQueryFc.type_name = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "type_name", "I");
	GTypeQueryFc.class_size = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "class_size", "I");
	GTypeQueryFc.instance_size = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "instance_size", "I");
	GTypeQueryFc.cached = 1;
}

GTypeQuery *getGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct)
{
	if (!GTypeQueryFc.cached) cacheGTypeQueryFields(env, lpObject);
	lpStruct->type = (GType)(*env)->GetIntField(env, lpObject, GTypeQueryFc.type);
	lpStruct->type_name = (const gchar *)(*env)->GetIntField(env, lpObject, GTypeQueryFc.type_name);
	lpStruct->class_size = (guint)(*env)->GetIntField(env, lpObject, GTypeQueryFc.class_size);
	lpStruct->instance_size = (guint)(*env)->GetIntField(env, lpObject, GTypeQueryFc.instance_size);
	return lpStruct;
}

void setGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct)
{
	if (!GTypeQueryFc.cached) cacheGTypeQueryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.type_name, (jint)lpStruct->type_name);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.class_size, (jint)lpStruct->class_size);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.instance_size, (jint)lpStruct->instance_size);
}
#endif

#ifndef NO_GdkColor
typedef struct GdkColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pixel, red, green, blue;
} GdkColor_FID_CACHE;

GdkColor_FID_CACHE GdkColorFc;

void cacheGdkColorFields(JNIEnv *env, jobject lpObject)
{
	if (GdkColorFc.cached) return;
	GdkColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkColorFc.pixel = (*env)->GetFieldID(env, GdkColorFc.clazz, "pixel", "I");
	GdkColorFc.red = (*env)->GetFieldID(env, GdkColorFc.clazz, "red", "S");
	GdkColorFc.green = (*env)->GetFieldID(env, GdkColorFc.clazz, "green", "S");
	GdkColorFc.blue = (*env)->GetFieldID(env, GdkColorFc.clazz, "blue", "S");
	GdkColorFc.cached = 1;
}

GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	lpStruct->pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkColorFc.pixel);
	lpStruct->red = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.red);
	lpStruct->green = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.green);
	lpStruct->blue = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.blue);
	return lpStruct;
}

void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkColorFc.pixel, (jint)lpStruct->pixel);
	(*env)->SetShortField(env, lpObject, GdkColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, GdkColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, GdkColorFc.blue, (jshort)lpStruct->blue);
}
#endif

#ifndef NO_GdkDragContext
typedef struct GdkDragContext_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID protocol, is_source, source_window, dest_window, targets, actions, suggested_action, action, start_time;
} GdkDragContext_FID_CACHE;

GdkDragContext_FID_CACHE GdkDragContextFc;

void cacheGdkDragContextFields(JNIEnv *env, jobject lpObject)
{
	if (GdkDragContextFc.cached) return;
	GdkDragContextFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkDragContextFc.protocol = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "protocol", "I");
	GdkDragContextFc.is_source = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "is_source", "Z");
	GdkDragContextFc.source_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "source_window", "I");
	GdkDragContextFc.dest_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "dest_window", "I");
	GdkDragContextFc.targets = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "targets", "I");
	GdkDragContextFc.actions = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "actions", "I");
	GdkDragContextFc.suggested_action = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "suggested_action", "I");
	GdkDragContextFc.action = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "action", "I");
	GdkDragContextFc.start_time = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "start_time", "I");
	GdkDragContextFc.cached = 1;
}

GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	if (!GdkDragContextFc.cached) cacheGdkDragContextFields(env, lpObject);
	lpStruct->protocol = (GdkDragProtocol)(*env)->GetIntField(env, lpObject, GdkDragContextFc.protocol);
	lpStruct->is_source = (gboolean)(*env)->GetBooleanField(env, lpObject, GdkDragContextFc.is_source);
	lpStruct->source_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.source_window);
	lpStruct->dest_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.dest_window);
	lpStruct->targets = (GList *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.targets);
	lpStruct->actions = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.actions);
	lpStruct->suggested_action = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.suggested_action);
	lpStruct->action = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.action);
	lpStruct->start_time = (guint32)(*env)->GetIntField(env, lpObject, GdkDragContextFc.start_time);
	return lpStruct;
}

void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	if (!GdkDragContextFc.cached) cacheGdkDragContextFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.protocol, (jint)lpStruct->protocol);
	(*env)->SetBooleanField(env, lpObject, GdkDragContextFc.is_source, (jboolean)lpStruct->is_source);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.source_window, (jint)lpStruct->source_window);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.dest_window, (jint)lpStruct->dest_window);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.targets, (jint)lpStruct->targets);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.actions, (jint)lpStruct->actions);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.suggested_action, (jint)lpStruct->suggested_action);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.action, (jint)lpStruct->action);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.start_time, (jint)lpStruct->start_time);
}
#endif

#ifndef NO_GdkEvent
typedef struct GdkEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type;
} GdkEvent_FID_CACHE;

GdkEvent_FID_CACHE GdkEventFc;

void cacheGdkEventFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventFc.cached) return;
	GdkEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventFc.type = (*env)->GetFieldID(env, GdkEventFc.clazz, "type", "I");
	GdkEventFc.cached = 1;
}

GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	if (!GdkEventFc.cached) cacheGdkEventFields(env, lpObject);
	lpStruct->type = (GdkEventType)(*env)->GetIntField(env, lpObject, GdkEventFc.type);
	return lpStruct;
}

void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	if (!GdkEventFc.cached) cacheGdkEventFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkEventFc.type, (jint)lpStruct->type);
}
#endif

#ifndef NO_GdkEventButton
typedef struct GdkEventButton_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, x, y, axes, state, button, device, x_root, y_root;
} GdkEventButton_FID_CACHE;

GdkEventButton_FID_CACHE GdkEventButtonFc;

void cacheGdkEventButtonFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventButtonFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventButtonFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventButtonFc.window = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "window", "I");
	GdkEventButtonFc.send_event = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "send_event", "B");
	GdkEventButtonFc.time = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "time", "I");
	GdkEventButtonFc.x = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x", "D");
	GdkEventButtonFc.y = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y", "D");
	GdkEventButtonFc.axes = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "axes", "I");
	GdkEventButtonFc.state = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "state", "I");
	GdkEventButtonFc.button = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "button", "I");
	GdkEventButtonFc.device = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "device", "I");
	GdkEventButtonFc.x_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x_root", "D");
	GdkEventButtonFc.y_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y_root", "D");
	GdkEventButtonFc.cached = 1;
}

GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventButtonFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y);
	lpStruct->axes = (gdouble *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.axes);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.state);
	lpStruct->button = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.button);
	lpStruct->device = (GdkDevice *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y_root);
	return lpStruct;
}

void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventButtonFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.y, (jdouble)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.axes, (jint)lpStruct->axes);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.button, (jint)lpStruct->button);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.device, (jint)lpStruct->device);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.y_root, (jdouble)lpStruct->y_root);
}
#endif

#ifndef NO_GdkEventCrossing
typedef struct GdkEventCrossing_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, subwindow, time, x, y, x_root, y_root, mode, detail, focus, state;
} GdkEventCrossing_FID_CACHE;

GdkEventCrossing_FID_CACHE GdkEventCrossingFc;

void cacheGdkEventCrossingFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventCrossingFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventCrossingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventCrossingFc.window = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "window", "I");
	GdkEventCrossingFc.send_event = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "send_event", "B");
	GdkEventCrossingFc.subwindow = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "subwindow", "I");
	GdkEventCrossingFc.time = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "time", "I");
	GdkEventCrossingFc.x = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "x", "D");
	GdkEventCrossingFc.y = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "y", "D");
	GdkEventCrossingFc.x_root = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "x_root", "D");
	GdkEventCrossingFc.y_root = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "y_root", "D");
	GdkEventCrossingFc.mode = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "mode", "I");
	GdkEventCrossingFc.detail = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "detail", "I");
	GdkEventCrossingFc.focus = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "focus", "Z");
	GdkEventCrossingFc.state = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "state", "I");
	GdkEventCrossingFc.cached = 1;
}

GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	if (!GdkEventCrossingFc.cached) cacheGdkEventCrossingFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventCrossingFc.send_event);
	lpStruct->subwindow = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, GdkEventCrossingFc.time);
	lpStruct->x = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.x);
	lpStruct->y = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.y);
	lpStruct->x_root = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.x_root);
	lpStruct->y_root = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.y_root);
	lpStruct->mode = (GdkCrossingMode)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.mode);
	lpStruct->detail = (GdkNotifyType)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.detail);
	lpStruct->focus = (gboolean)(*env)->GetBooleanField(env, lpObject, GdkEventCrossingFc.focus);
	lpStruct->state = (*env)->GetIntField(env, lpObject, GdkEventCrossingFc.state);
	return lpStruct;
}

void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	if (!GdkEventCrossingFc.cached) cacheGdkEventCrossingFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventCrossingFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.y, (jdouble)lpStruct->y);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.y_root, (jdouble)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.detail, (jint)lpStruct->detail);
	(*env)->SetBooleanField(env, lpObject, GdkEventCrossingFc.focus, (jboolean)lpStruct->focus);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_GdkEventExpose
typedef struct GdkEventExpose_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, area_x, area_y, area_width, area_height, region, count;
} GdkEventExpose_FID_CACHE;

GdkEventExpose_FID_CACHE GdkEventExposeFc;

void cacheGdkEventExposeFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventExposeFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventExposeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventExposeFc.window = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "window", "I");
	GdkEventExposeFc.send_event = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "send_event", "B");
	GdkEventExposeFc.area_x = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_x", "I");
	GdkEventExposeFc.area_y = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_y", "I");
	GdkEventExposeFc.area_width = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_width", "I");
	GdkEventExposeFc.area_height = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_height", "I");
	GdkEventExposeFc.region = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "region", "I");
	GdkEventExposeFc.count = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "count", "I");
	GdkEventExposeFc.cached = 1;
}

GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventExposeFc.send_event);
	lpStruct->area.x = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_x);
	lpStruct->area.y = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_y);
	lpStruct->area.width = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_width);
	lpStruct->area.height = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_height);
	lpStruct->region = (GdkRegion *)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.region);
	lpStruct->count = (gint)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.count);
	return lpStruct;
}

void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventExposeFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_x, (jint)lpStruct->area.x);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_y, (jint)lpStruct->area.y);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_width, (jint)lpStruct->area.width);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_height, (jint)lpStruct->area.height);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.region, (jint)lpStruct->region);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.count, (jint)lpStruct->count);
}
#endif

#ifndef NO_GdkEventFocus
typedef struct GdkEventFocus_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, in;
} GdkEventFocus_FID_CACHE;

GdkEventFocus_FID_CACHE GdkEventFocusFc;

void cacheGdkEventFocusFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventFocusFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventFocusFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventFocusFc.window = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "window", "I");
	GdkEventFocusFc.send_event = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "send_event", "B");
	GdkEventFocusFc.in = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "in", "S");
	GdkEventFocusFc.cached = 1;
}

GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventFocusFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventFocusFc.send_event);
	lpStruct->in = (gint16)(*env)->GetShortField(env, lpObject, GdkEventFocusFc.in);
	return lpStruct;
}

void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventFocusFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventFocusFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetShortField(env, lpObject, GdkEventFocusFc.in, (jshort)lpStruct->in);
}
#endif

#ifndef NO_GdkEventKey
typedef struct GdkEventKey_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, state, keyval, length, string, hardware_keycode, group;
} GdkEventKey_FID_CACHE;

GdkEventKey_FID_CACHE GdkEventKeyFc;

void cacheGdkEventKeyFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventKeyFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventKeyFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventKeyFc.window = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "window", "I");
	GdkEventKeyFc.send_event = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "send_event", "B");
	GdkEventKeyFc.time = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "time", "I");
	GdkEventKeyFc.state = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "state", "I");
	GdkEventKeyFc.keyval = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "keyval", "I");
	GdkEventKeyFc.length = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "length", "I");
	GdkEventKeyFc.string = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "string", "I");
	GdkEventKeyFc.hardware_keycode = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "hardware_keycode", "S");
	GdkEventKeyFc.group = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "group", "B");
	GdkEventKeyFc.cached = 1;
}

GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.time);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.state);
	lpStruct->keyval = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.keyval);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.length);
	lpStruct->string = (gchar *)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.string);
	lpStruct->hardware_keycode = (guint16)(*env)->GetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode);
	lpStruct->group = (guint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.group);
	return lpStruct;
}

void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.keyval, (jint)lpStruct->keyval);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.string, (jint)lpStruct->string);
	(*env)->SetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode, (jshort)lpStruct->hardware_keycode);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.group, (jbyte)lpStruct->group);
}
#endif

#ifndef NO_GdkEventWindowState
typedef struct GdkEventWindowState_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, changed_mask, new_window_state;
} GdkEventWindowState_FID_CACHE;

GdkEventWindowState_FID_CACHE GdkEventWindowStateFc;

void cacheGdkEventWindowStateFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventWindowStateFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventWindowStateFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventWindowStateFc.window = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "window", "I");
	GdkEventWindowStateFc.send_event = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "send_event", "B");
	GdkEventWindowStateFc.changed_mask = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "changed_mask", "I");
	GdkEventWindowStateFc.new_window_state = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "new_window_state", "I");
	GdkEventWindowStateFc.cached = 1;
}

GdkEventWindowState *getGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.window);
	lpStruct->send_event = (*env)->GetByteField(env, lpObject, GdkEventWindowStateFc.send_event);
	lpStruct->changed_mask = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.changed_mask);
	lpStruct->new_window_state = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.new_window_state);
	return lpStruct;
}

void setGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventWindowStateFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.changed_mask, (jint)lpStruct->changed_mask);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.new_window_state, (jint)lpStruct->new_window_state);
}
#endif

#ifndef NO_GdkGCValues
typedef struct GdkGCValues_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID foreground_pixel, foreground_red, foreground_green, foreground_blue, background_pixel, background_red, background_green, background_blue, font, function, fill, tile, stipple, clip_mask, subwindow_mode, ts_x_origin, ts_y_origin, clip_x_origin, clip_y_origin, graphics_exposures, line_width, line_style, cap_style, join_style;
} GdkGCValues_FID_CACHE;

GdkGCValues_FID_CACHE GdkGCValuesFc;

void cacheGdkGCValuesFields(JNIEnv *env, jobject lpObject)
{
	if (GdkGCValuesFc.cached) return;
	GdkGCValuesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkGCValuesFc.foreground_pixel = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_pixel", "I");
	GdkGCValuesFc.foreground_red = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_red", "S");
	GdkGCValuesFc.foreground_green = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_green", "S");
	GdkGCValuesFc.foreground_blue = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_blue", "S");
	GdkGCValuesFc.background_pixel = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_pixel", "I");
	GdkGCValuesFc.background_red = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_red", "S");
	GdkGCValuesFc.background_green = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_green", "S");
	GdkGCValuesFc.background_blue = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_blue", "S");
	GdkGCValuesFc.font = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "font", "I");
	GdkGCValuesFc.function = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "function", "I");
	GdkGCValuesFc.fill = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "fill", "I");
	GdkGCValuesFc.tile = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "tile", "I");
	GdkGCValuesFc.stipple = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "stipple", "I");
	GdkGCValuesFc.clip_mask = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_mask", "I");
	GdkGCValuesFc.subwindow_mode = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "subwindow_mode", "I");
	GdkGCValuesFc.ts_x_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "ts_x_origin", "I");
	GdkGCValuesFc.ts_y_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "ts_y_origin", "I");
	GdkGCValuesFc.clip_x_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_x_origin", "I");
	GdkGCValuesFc.clip_y_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_y_origin", "I");
	GdkGCValuesFc.graphics_exposures = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "graphics_exposures", "I");
	GdkGCValuesFc.line_width = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "line_width", "I");
	GdkGCValuesFc.line_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "line_style", "I");
	GdkGCValuesFc.cap_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "cap_style", "I");
	GdkGCValuesFc.join_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "join_style", "I");
	GdkGCValuesFc.cached = 1;
}

GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	if (!GdkGCValuesFc.cached) cacheGdkGCValuesFields(env, lpObject);
	lpStruct->foreground.pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.foreground_pixel);
	lpStruct->foreground.red = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_red);
	lpStruct->foreground.green = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_green);
	lpStruct->foreground.blue = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_blue);
	lpStruct->background.pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.background_pixel);
	lpStruct->background.red = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_red);
	lpStruct->background.green = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_green);
	lpStruct->background.blue = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_blue);
	lpStruct->font = (GdkFont *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.font);
	lpStruct->function = (GdkFunction)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.function);
	lpStruct->fill = (GdkFill)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.fill);
	lpStruct->tile = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.tile);
	lpStruct->stipple = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.stipple);
	lpStruct->clip_mask = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_mask);
	lpStruct->subwindow_mode = (GdkSubwindowMode)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.subwindow_mode);
	lpStruct->ts_x_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.ts_x_origin);
	lpStruct->ts_y_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.ts_y_origin);
	lpStruct->clip_x_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_x_origin);
	lpStruct->clip_y_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_y_origin);
	lpStruct->graphics_exposures = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.graphics_exposures);
	lpStruct->line_width = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.line_width);
	lpStruct->line_style = (GdkLineStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.line_style);
	lpStruct->cap_style = (GdkCapStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.cap_style);
	lpStruct->join_style = (GdkJoinStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.join_style);
	return lpStruct;
}

void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	if (!GdkGCValuesFc.cached) cacheGdkGCValuesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.foreground_pixel, (jint)lpStruct->foreground.pixel);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_red, (jshort)lpStruct->foreground.red);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_green, (jshort)lpStruct->foreground.green);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_blue, (jshort)lpStruct->foreground.blue);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.background_pixel, (jint)lpStruct->background.pixel);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_red, (jshort)lpStruct->background.red);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_green, (jshort)lpStruct->background.green);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_blue, (jshort)lpStruct->background.blue);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.font, (jint)lpStruct->font);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.function, (jint)lpStruct->function);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.fill, (jint)lpStruct->fill);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.tile, (jint)lpStruct->tile);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.stipple, (jint)lpStruct->stipple);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_mask, (jint)lpStruct->clip_mask);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.subwindow_mode, (jint)lpStruct->subwindow_mode);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.ts_x_origin, (jint)lpStruct->ts_x_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.ts_y_origin, (jint)lpStruct->ts_y_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_x_origin, (jint)lpStruct->clip_x_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_y_origin, (jint)lpStruct->clip_y_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.graphics_exposures, (jint)lpStruct->graphics_exposures);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.line_width, (jint)lpStruct->line_width);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.line_style, (jint)lpStruct->line_style);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.cap_style, (jint)lpStruct->cap_style);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.join_style, (jint)lpStruct->join_style);
}
#endif

#ifndef NO_GdkImage
typedef struct GdkImage_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, visual, byte_order, width, height, depth, bpp, bpl, bits_per_pixel, mem, colormap, windowing_data;
} GdkImage_FID_CACHE;

GdkImage_FID_CACHE GdkImageFc;

void cacheGdkImageFields(JNIEnv *env, jobject lpObject)
{
	if (GdkImageFc.cached) return;
	GdkImageFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkImageFc.type = (*env)->GetFieldID(env, GdkImageFc.clazz, "type", "I");
	GdkImageFc.visual = (*env)->GetFieldID(env, GdkImageFc.clazz, "visual", "I");
	GdkImageFc.byte_order = (*env)->GetFieldID(env, GdkImageFc.clazz, "byte_order", "I");
	GdkImageFc.width = (*env)->GetFieldID(env, GdkImageFc.clazz, "width", "I");
	GdkImageFc.height = (*env)->GetFieldID(env, GdkImageFc.clazz, "height", "I");
	GdkImageFc.depth = (*env)->GetFieldID(env, GdkImageFc.clazz, "depth", "S");
	GdkImageFc.bpp = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpp", "S");
	GdkImageFc.bpl = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpl", "S");
	GdkImageFc.bits_per_pixel = (*env)->GetFieldID(env, GdkImageFc.clazz, "bits_per_pixel", "S");
	GdkImageFc.mem = (*env)->GetFieldID(env, GdkImageFc.clazz, "mem", "I");
	GdkImageFc.colormap = (*env)->GetFieldID(env, GdkImageFc.clazz, "colormap", "I");
	GdkImageFc.windowing_data = (*env)->GetFieldID(env, GdkImageFc.clazz, "windowing_data", "I");
	GdkImageFc.cached = 1;
}

GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	lpStruct->type = (GdkImageType)(*env)->GetIntField(env, lpObject, GdkImageFc.type);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntField(env, lpObject, GdkImageFc.visual);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, GdkImageFc.byte_order);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.height);
	lpStruct->depth = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.depth);
	lpStruct->bpp = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpp);
	lpStruct->bpl = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpl);
	lpStruct->bits_per_pixel = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bits_per_pixel);
	lpStruct->mem = (gpointer)(*env)->GetIntField(env, lpObject, GdkImageFc.mem);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntField(env, lpObject, GdkImageFc.colormap);
	lpStruct->windowing_data = (gpointer)(*env)->GetIntField(env, lpObject, GdkImageFc.windowing_data);
	return lpStruct;
}

void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkImageFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GdkImageFc.visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, GdkImageFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, GdkImageFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkImageFc.height, (jint)lpStruct->height);
	(*env)->SetShortField(env, lpObject, GdkImageFc.depth, (jshort)lpStruct->depth);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpp, (jshort)lpStruct->bpp);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpl, (jshort)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bits_per_pixel, (jshort)lpStruct->bits_per_pixel);
	(*env)->SetIntField(env, lpObject, GdkImageFc.mem, (jint)lpStruct->mem);
	(*env)->SetIntField(env, lpObject, GdkImageFc.colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, GdkImageFc.windowing_data, (jint)lpStruct->windowing_data);
}
#endif

#ifndef NO_GdkRectangle
typedef struct GdkRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GdkRectangle_FID_CACHE;

GdkRectangle_FID_CACHE GdkRectangleFc;

void cacheGdkRectangleFields(JNIEnv *env, jobject lpObject)
{
	if (GdkRectangleFc.cached) return;
	GdkRectangleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkRectangleFc.x = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "x", "I");
	GdkRectangleFc.y = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "y", "I");
	GdkRectangleFc.width = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "width", "I");
	GdkRectangleFc.height = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "height", "I");
	GdkRectangleFc.cached = 1;
}

GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	if (!GdkRectangleFc.cached) cacheGdkRectangleFields(env, lpObject);
	lpStruct->x = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.x);
	lpStruct->y = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.y);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.height);
	return lpStruct;
}

void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	if (!GdkRectangleFc.cached) cacheGdkRectangleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GdkVisual
typedef struct GdkVisual_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, depth, byte_order, colormap_size, bits_per_rgb, red_mask, red_shift, red_prec, green_mask, green_shift, green_prec, blue_mask, blue_shift, blue_prec;
} GdkVisual_FID_CACHE;

GdkVisual_FID_CACHE GdkVisualFc;

void cacheGdkVisualFields(JNIEnv *env, jobject lpObject)
{
	if (GdkVisualFc.cached) return;
	GdkVisualFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkVisualFc.type = (*env)->GetFieldID(env, GdkVisualFc.clazz, "type", "I");
	GdkVisualFc.depth = (*env)->GetFieldID(env, GdkVisualFc.clazz, "depth", "I");
	GdkVisualFc.byte_order = (*env)->GetFieldID(env, GdkVisualFc.clazz, "byte_order", "I");
	GdkVisualFc.colormap_size = (*env)->GetFieldID(env, GdkVisualFc.clazz, "colormap_size", "I");
	GdkVisualFc.bits_per_rgb = (*env)->GetFieldID(env, GdkVisualFc.clazz, "bits_per_rgb", "I");
	GdkVisualFc.red_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_mask", "I");
	GdkVisualFc.red_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_shift", "I");
	GdkVisualFc.red_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_prec", "I");
	GdkVisualFc.green_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_mask", "I");
	GdkVisualFc.green_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_shift", "I");
	GdkVisualFc.green_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_prec", "I");
	GdkVisualFc.blue_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_mask", "I");
	GdkVisualFc.blue_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_shift", "I");
	GdkVisualFc.blue_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_prec", "I");
	GdkVisualFc.cached = 1;
}

GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	if (!GdkVisualFc.cached) cacheGdkVisualFields(env, lpObject);
	lpStruct->type = (GdkVisualType)(*env)->GetIntField(env, lpObject, GdkVisualFc.type);
	lpStruct->depth = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.depth);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, GdkVisualFc.byte_order);
	lpStruct->colormap_size = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.colormap_size);
	lpStruct->bits_per_rgb = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.bits_per_rgb);
	lpStruct->red_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_mask);
	lpStruct->red_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_shift);
	lpStruct->red_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_prec);
	lpStruct->green_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_mask);
	lpStruct->green_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_shift);
	lpStruct->green_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_prec);
	lpStruct->blue_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_mask);
	lpStruct->blue_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_shift);
	lpStruct->blue_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_prec);
	return lpStruct;
}

void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	if (!GdkVisualFc.cached) cacheGdkVisualFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.colormap_size, (jint)lpStruct->colormap_size);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.bits_per_rgb, (jint)lpStruct->bits_per_rgb);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_shift, (jint)lpStruct->red_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_prec, (jint)lpStruct->red_prec);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_shift, (jint)lpStruct->green_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_prec, (jint)lpStruct->green_prec);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_shift, (jint)lpStruct->blue_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_prec, (jint)lpStruct->blue_prec);
}
#endif

#ifndef NO_GtkAccessible
typedef struct GtkAccessible_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID widget;
} GtkAccessible_FID_CACHE;

GtkAccessible_FID_CACHE GtkAccessibleFc;

void cacheGtkAccessibleFields(JNIEnv *env, jobject lpObject)
{
	if (GtkAccessibleFc.cached) return;
	GtkAccessibleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkAccessibleFc.widget = (*env)->GetFieldID(env, GtkAccessibleFc.clazz, "widget", "I");
	GtkAccessibleFc.cached = 1;
}

GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct)
{
	if (!GtkAccessibleFc.cached) cacheGtkAccessibleFields(env, lpObject);
	lpStruct->widget = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkAccessibleFc.widget);
	return lpStruct;
}

void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct)
{
	if (!GtkAccessibleFc.cached) cacheGtkAccessibleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkAccessibleFc.widget, (jint)lpStruct->widget);
}
#endif

#ifndef NO_GtkAdjustment
typedef struct GtkAdjustment_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lower, upper, value, step_increment, page_increment, page_size;
} GtkAdjustment_FID_CACHE;

GtkAdjustment_FID_CACHE GtkAdjustmentFc;

void cacheGtkAdjustmentFields(JNIEnv *env, jobject lpObject)
{
	if (GtkAdjustmentFc.cached) return;
	GtkAdjustmentFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkAdjustmentFc.lower = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "lower", "D");
	GtkAdjustmentFc.upper = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "upper", "D");
	GtkAdjustmentFc.value = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "value", "D");
	GtkAdjustmentFc.step_increment = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "step_increment", "D");
	GtkAdjustmentFc.page_increment = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "page_increment", "D");
	GtkAdjustmentFc.page_size = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "page_size", "D");
	GtkAdjustmentFc.cached = 1;
}

GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	if (!GtkAdjustmentFc.cached) cacheGtkAdjustmentFields(env, lpObject);
	lpStruct->lower = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.lower);
	lpStruct->upper = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.upper);
	lpStruct->value = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.value);
	lpStruct->step_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.step_increment);
	lpStruct->page_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.page_increment);
	lpStruct->page_size = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.page_size);
	return lpStruct;
}

void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	if (!GtkAdjustmentFc.cached) cacheGtkAdjustmentFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.lower, (jdouble)lpStruct->lower);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.upper, (jdouble)lpStruct->upper);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.value, (jdouble)lpStruct->value);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.step_increment, (jdouble)lpStruct->step_increment);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.page_increment, (jdouble)lpStruct->page_increment);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.page_size, (jdouble)lpStruct->page_size);
}
#endif

#ifndef NO_GtkAllocation
typedef struct GtkAllocation_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GtkAllocation_FID_CACHE;

GtkAllocation_FID_CACHE GtkAllocationFc;

void cacheGtkAllocationFields(JNIEnv *env, jobject lpObject)
{
	if (GtkAllocationFc.cached) return;
	GtkAllocationFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkAllocationFc.x = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "x", "I");
	GtkAllocationFc.y = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "y", "I");
	GtkAllocationFc.width = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "width", "I");
	GtkAllocationFc.height = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "height", "I");
	GtkAllocationFc.cached = 1;
}

GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	if (!GtkAllocationFc.cached) cacheGtkAllocationFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, GtkAllocationFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, GtkAllocationFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GtkAllocationFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GtkAllocationFc.height);
	return lpStruct;
}

void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	if (!GtkAllocationFc.cached) cacheGtkAllocationFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GtkColorSelectionDialog
typedef struct GtkColorSelectionDialog_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID colorsel, ok_button, cancel_button, help_button;
} GtkColorSelectionDialog_FID_CACHE;

GtkColorSelectionDialog_FID_CACHE GtkColorSelectionDialogFc;

void cacheGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject)
{
	if (GtkColorSelectionDialogFc.cached) return;
	GtkColorSelectionDialogFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkColorSelectionDialogFc.colorsel = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "colorsel", "I");
	GtkColorSelectionDialogFc.ok_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "ok_button", "I");
	GtkColorSelectionDialogFc.cancel_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "cancel_button", "I");
	GtkColorSelectionDialogFc.help_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "help_button", "I");
	GtkColorSelectionDialogFc.cached = 1;
}

GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	lpStruct->colorsel = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.colorsel);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.help_button);
	return lpStruct;
}

void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.colorsel, (jint)lpStruct->colorsel);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.ok_button, (jint)lpStruct->ok_button);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.cancel_button, (jint)lpStruct->cancel_button);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.help_button, (jint)lpStruct->help_button);
}
#endif

#ifndef NO_GtkCombo
typedef struct GtkCombo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID entry, list;
} GtkCombo_FID_CACHE;

GtkCombo_FID_CACHE GtkComboFc;

void cacheGtkComboFields(JNIEnv *env, jobject lpObject)
{
	if (GtkComboFc.cached) return;
	GtkComboFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkComboFc.entry = (*env)->GetFieldID(env, GtkComboFc.clazz, "entry", "I");
	GtkComboFc.list = (*env)->GetFieldID(env, GtkComboFc.clazz, "list", "I");
	GtkComboFc.cached = 1;
}

GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	lpStruct->entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkComboFc.entry);
	lpStruct->list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkComboFc.list);
	return lpStruct;
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkComboFc.entry, (jint)lpStruct->entry);
	(*env)->SetIntField(env, lpObject, GtkComboFc.list, (jint)lpStruct->list);
}
#endif

#ifndef NO_GtkFileSelection
typedef struct GtkFileSelection_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dir_list, file_list, selection_entry, selection_text, main_vbox, ok_button, cancel_button, help_button, history_pulldown, history_menu, history_list, fileop_dialog, fileop_entry, fileop_file, cmpl_state, fileop_c_dir, fileop_del_file, fileop_ren_file, button_area, action_area;
} GtkFileSelection_FID_CACHE;

GtkFileSelection_FID_CACHE GtkFileSelectionFc;

void cacheGtkFileSelectionFields(JNIEnv *env, jobject lpObject)
{
	if (GtkFileSelectionFc.cached) return;
	GtkFileSelectionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkFileSelectionFc.dir_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "dir_list", "I");
	GtkFileSelectionFc.file_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "file_list", "I");
	GtkFileSelectionFc.selection_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_entry", "I");
	GtkFileSelectionFc.selection_text = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_text", "I");
	GtkFileSelectionFc.main_vbox = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "main_vbox", "I");
	GtkFileSelectionFc.ok_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "ok_button", "I");
	GtkFileSelectionFc.cancel_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cancel_button", "I");
	GtkFileSelectionFc.help_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "help_button", "I");
	GtkFileSelectionFc.history_pulldown = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_pulldown", "I");
	GtkFileSelectionFc.history_menu = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_menu", "I");
	GtkFileSelectionFc.history_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_list", "I");
	GtkFileSelectionFc.fileop_dialog = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_dialog", "I");
	GtkFileSelectionFc.fileop_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_entry", "I");
	GtkFileSelectionFc.fileop_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_file", "I");
	GtkFileSelectionFc.cmpl_state = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cmpl_state", "I");
	GtkFileSelectionFc.fileop_c_dir = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_c_dir", "I");
	GtkFileSelectionFc.fileop_del_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_del_file", "I");
	GtkFileSelectionFc.fileop_ren_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_ren_file", "I");
	GtkFileSelectionFc.button_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "button_area", "I");
	GtkFileSelectionFc.action_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "action_area", "I");
	GtkFileSelectionFc.cached = 1;
}

GtkFileSelection *getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	lpStruct->dir_list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.dir_list);
	lpStruct->file_list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.file_list);
	lpStruct->selection_entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.selection_entry);
	lpStruct->selection_text = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.selection_text);
	lpStruct->main_vbox = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.main_vbox);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.help_button);
	lpStruct->history_pulldown = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_pulldown);
	lpStruct->history_menu = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_menu);
	lpStruct->history_list = (GList *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_list);
	lpStruct->fileop_dialog = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_dialog);
	lpStruct->fileop_entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_entry);
	lpStruct->fileop_file = (gchar *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_file);
	lpStruct->cmpl_state = (gpointer)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.cmpl_state);
	lpStruct->fileop_c_dir = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_c_dir);
	lpStruct->fileop_del_file = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_del_file);
	lpStruct->fileop_ren_file = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_ren_file);
	lpStruct->button_area = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.button_area);
	lpStruct->action_area = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.action_area);
	return lpStruct;
}

void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.dir_list, (jint)lpStruct->dir_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.file_list, (jint)lpStruct->file_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.selection_entry, (jint)lpStruct->selection_entry);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.selection_text, (jint)lpStruct->selection_text);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.main_vbox, (jint)lpStruct->main_vbox);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.ok_button, (jint)lpStruct->ok_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.cancel_button, (jint)lpStruct->cancel_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.help_button, (jint)lpStruct->help_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_pulldown, (jint)lpStruct->history_pulldown);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_menu, (jint)lpStruct->history_menu);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_list, (jint)lpStruct->history_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_dialog, (jint)lpStruct->fileop_dialog);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_entry, (jint)lpStruct->fileop_entry);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_file, (jint)lpStruct->fileop_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.cmpl_state, (jint)lpStruct->cmpl_state);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_c_dir, (jint)lpStruct->fileop_c_dir);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_del_file, (jint)lpStruct->fileop_del_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_ren_file, (jint)lpStruct->fileop_ren_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.button_area, (jint)lpStruct->button_area);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.action_area, (jint)lpStruct->action_area);
}
#endif

#ifndef NO_GtkFixed
typedef struct GtkFixed_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID children;
} GtkFixed_FID_CACHE;

GtkFixed_FID_CACHE GtkFixedFc;

void cacheGtkFixedFields(JNIEnv *env, jobject lpObject)
{
	if (GtkFixedFc.cached) return;
	GtkFixedFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkFixedFc.children = (*env)->GetFieldID(env, GtkFixedFc.clazz, "children", "I");
	GtkFixedFc.cached = 1;
}

GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	lpStruct->children = (GList *)(*env)->GetIntField(env, lpObject, GtkFixedFc.children);
	return lpStruct;
}

void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkFixedFc.children, (jint)lpStruct->children);
}
#endif

#ifndef NO_GtkRequisition
typedef struct GtkRequisition_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} GtkRequisition_FID_CACHE;

GtkRequisition_FID_CACHE GtkRequisitionFc;

void cacheGtkRequisitionFields(JNIEnv *env, jobject lpObject)
{
	if (GtkRequisitionFc.cached) return;
	GtkRequisitionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkRequisitionFc.width = (*env)->GetFieldID(env, GtkRequisitionFc.clazz, "width", "I");
	GtkRequisitionFc.height = (*env)->GetFieldID(env, GtkRequisitionFc.clazz, "height", "I");
	GtkRequisitionFc.cached = 1;
}

GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	if (!GtkRequisitionFc.cached) cacheGtkRequisitionFields(env, lpObject);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GtkRequisitionFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GtkRequisitionFc.height);
	return lpStruct;
}

void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	if (!GtkRequisitionFc.cached) cacheGtkRequisitionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkRequisitionFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GtkRequisitionFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GtkSelectionData
typedef struct GtkSelectionData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID selection, target, type, format, data, length;
} GtkSelectionData_FID_CACHE;

GtkSelectionData_FID_CACHE GtkSelectionDataFc;

void cacheGtkSelectionDataFields(JNIEnv *env, jobject lpObject)
{
	if (GtkSelectionDataFc.cached) return;
	GtkSelectionDataFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkSelectionDataFc.selection = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "selection", "I");
	GtkSelectionDataFc.target = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "target", "I");
	GtkSelectionDataFc.type = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "type", "I");
	GtkSelectionDataFc.format = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "format", "I");
	GtkSelectionDataFc.data = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "data", "I");
	GtkSelectionDataFc.length = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "length", "I");
	GtkSelectionDataFc.cached = 1;
}

GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	lpStruct->selection = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.selection);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.target);
	lpStruct->type = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.type);
	lpStruct->format = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.format);
	lpStruct->data = (guchar *)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.data);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.length);
	return lpStruct;
}

void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.selection, (jint)lpStruct->selection);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.format, (jint)lpStruct->format);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.length, (jint)lpStruct->length);
}
#endif

#ifndef NO_GtkStyle
typedef struct GtkStyle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fg0_pixel, fg0_red, fg0_green, fg0_blue, fg1_pixel, fg1_red, fg1_green, fg1_blue, fg2_pixel, fg2_red, fg2_green, fg2_blue, fg3_pixel, fg3_red, fg3_green, fg3_blue, fg4_pixel, fg4_red, fg4_green, fg4_blue, bg0_pixel, bg0_red, bg0_green, bg0_blue, bg1_pixel, bg1_red, bg1_green, bg1_blue, bg2_pixel, bg2_red, bg2_green, bg2_blue, bg3_pixel, bg3_red, bg3_green, bg3_blue, bg4_pixel, bg4_red, bg4_green, bg4_blue, light0_pixel, light0_red, light0_green, light0_blue, light1_pixel, light1_red, light1_green, light1_blue, light2_pixel, light2_red, light2_green, light2_blue, light3_pixel, light3_red, light3_green, light3_blue, light4_pixel, light4_red, light4_green, light4_blue, dark0_pixel, dark0_red, dark0_green, dark0_blue, dark1_pixel, dark1_red, dark1_green, dark1_blue, dark2_pixel, dark2_red, dark2_green, dark2_blue, dark3_pixel, dark3_red, dark3_green, dark3_blue, dark4_pixel, dark4_red, dark4_green, dark4_blue, mid0_pixel, mid0_red, mid0_green, mid0_blue, mid1_pixel, mid1_red, mid1_green, mid1_blue, mid2_pixel, mid2_red, mid2_green, mid2_blue, mid3_pixel, mid3_red, mid3_green, mid3_blue, mid4_pixel, mid4_red, mid4_green, mid4_blue, text0_pixel, text0_red, text0_green, text0_blue, text1_pixel, text1_red, text1_green, text1_blue, text2_pixel, text2_red, text2_green, text2_blue, text3_pixel, text3_red, text3_green, text3_blue, text4_pixel, text4_red, text4_green, text4_blue, base0_pixel, base0_red, base0_green, base0_blue, base1_pixel, base1_red, base1_green, base1_blue, base2_pixel, base2_red, base2_green, base2_blue, base3_pixel, base3_red, base3_green, base3_blue, base4_pixel, base4_red, base4_green, base4_blue, black_pixel, black_red, black_green, black_blue, white_pixel, white_red, white_green, white_blue, font_desc, xthickness, ythickness, fg_gc0, fg_gc1, fg_gc2, fg_gc3, fg_gc4, bg_gc0, bg_gc1, bg_gc2, bg_gc3, bg_gc4, light_gc0, light_gc1, light_gc2, light_gc3, light_gc4, dark_gc0, dark_gc1, dark_gc2, dark_gc3, dark_gc4, mid_gc0, mid_gc1, mid_gc2, mid_gc3, mid_gc4, text_gc0, text_gc1, text_gc2, text_gc3, text_gc4, base_gc0, base_gc1, base_gc2, base_gc3, base_gc4, black_gc, white_gc, bg_pixmap0, bg_pixmap1, bg_pixmap2, bg_pixmap3, bg_pixmap4;
} GtkStyle_FID_CACHE;

GtkStyle_FID_CACHE GtkStyleFc;

void cacheGtkStyleFields(JNIEnv *env, jobject lpObject)
{
	if (GtkStyleFc.cached) return;
	GtkStyleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkStyleFc.fg0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg0_pixel", "I");
	GtkStyleFc.fg0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg0_red", "S");
	GtkStyleFc.fg0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg0_green", "S");
	GtkStyleFc.fg0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg0_blue", "S");
	GtkStyleFc.fg1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg1_pixel", "I");
	GtkStyleFc.fg1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg1_red", "S");
	GtkStyleFc.fg1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg1_green", "S");
	GtkStyleFc.fg1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg1_blue", "S");
	GtkStyleFc.fg2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg2_pixel", "I");
	GtkStyleFc.fg2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg2_red", "S");
	GtkStyleFc.fg2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg2_green", "S");
	GtkStyleFc.fg2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg2_blue", "S");
	GtkStyleFc.fg3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg3_pixel", "I");
	GtkStyleFc.fg3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg3_red", "S");
	GtkStyleFc.fg3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg3_green", "S");
	GtkStyleFc.fg3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg3_blue", "S");
	GtkStyleFc.fg4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg4_pixel", "I");
	GtkStyleFc.fg4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg4_red", "S");
	GtkStyleFc.fg4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg4_green", "S");
	GtkStyleFc.fg4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg4_blue", "S");
	GtkStyleFc.bg0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg0_pixel", "I");
	GtkStyleFc.bg0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg0_red", "S");
	GtkStyleFc.bg0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg0_green", "S");
	GtkStyleFc.bg0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg0_blue", "S");
	GtkStyleFc.bg1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg1_pixel", "I");
	GtkStyleFc.bg1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg1_red", "S");
	GtkStyleFc.bg1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg1_green", "S");
	GtkStyleFc.bg1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg1_blue", "S");
	GtkStyleFc.bg2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg2_pixel", "I");
	GtkStyleFc.bg2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg2_red", "S");
	GtkStyleFc.bg2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg2_green", "S");
	GtkStyleFc.bg2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg2_blue", "S");
	GtkStyleFc.bg3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg3_pixel", "I");
	GtkStyleFc.bg3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg3_red", "S");
	GtkStyleFc.bg3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg3_green", "S");
	GtkStyleFc.bg3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg3_blue", "S");
	GtkStyleFc.bg4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg4_pixel", "I");
	GtkStyleFc.bg4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg4_red", "S");
	GtkStyleFc.bg4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg4_green", "S");
	GtkStyleFc.bg4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg4_blue", "S");
	GtkStyleFc.light0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light0_pixel", "I");
	GtkStyleFc.light0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light0_red", "S");
	GtkStyleFc.light0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light0_green", "S");
	GtkStyleFc.light0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light0_blue", "S");
	GtkStyleFc.light1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light1_pixel", "I");
	GtkStyleFc.light1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light1_red", "S");
	GtkStyleFc.light1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light1_green", "S");
	GtkStyleFc.light1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light1_blue", "S");
	GtkStyleFc.light2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light2_pixel", "I");
	GtkStyleFc.light2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light2_red", "S");
	GtkStyleFc.light2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light2_green", "S");
	GtkStyleFc.light2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light2_blue", "S");
	GtkStyleFc.light3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light3_pixel", "I");
	GtkStyleFc.light3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light3_red", "S");
	GtkStyleFc.light3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light3_green", "S");
	GtkStyleFc.light3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light3_blue", "S");
	GtkStyleFc.light4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light4_pixel", "I");
	GtkStyleFc.light4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light4_red", "S");
	GtkStyleFc.light4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light4_green", "S");
	GtkStyleFc.light4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light4_blue", "S");
	GtkStyleFc.dark0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark0_pixel", "I");
	GtkStyleFc.dark0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark0_red", "S");
	GtkStyleFc.dark0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark0_green", "S");
	GtkStyleFc.dark0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark0_blue", "S");
	GtkStyleFc.dark1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark1_pixel", "I");
	GtkStyleFc.dark1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark1_red", "S");
	GtkStyleFc.dark1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark1_green", "S");
	GtkStyleFc.dark1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark1_blue", "S");
	GtkStyleFc.dark2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark2_pixel", "I");
	GtkStyleFc.dark2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark2_red", "S");
	GtkStyleFc.dark2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark2_green", "S");
	GtkStyleFc.dark2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark2_blue", "S");
	GtkStyleFc.dark3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark3_pixel", "I");
	GtkStyleFc.dark3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark3_red", "S");
	GtkStyleFc.dark3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark3_green", "S");
	GtkStyleFc.dark3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark3_blue", "S");
	GtkStyleFc.dark4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark4_pixel", "I");
	GtkStyleFc.dark4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark4_red", "S");
	GtkStyleFc.dark4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark4_green", "S");
	GtkStyleFc.dark4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark4_blue", "S");
	GtkStyleFc.mid0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid0_pixel", "I");
	GtkStyleFc.mid0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid0_red", "S");
	GtkStyleFc.mid0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid0_green", "S");
	GtkStyleFc.mid0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid0_blue", "S");
	GtkStyleFc.mid1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid1_pixel", "I");
	GtkStyleFc.mid1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid1_red", "S");
	GtkStyleFc.mid1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid1_green", "S");
	GtkStyleFc.mid1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid1_blue", "S");
	GtkStyleFc.mid2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid2_pixel", "I");
	GtkStyleFc.mid2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid2_red", "S");
	GtkStyleFc.mid2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid2_green", "S");
	GtkStyleFc.mid2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid2_blue", "S");
	GtkStyleFc.mid3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid3_pixel", "I");
	GtkStyleFc.mid3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid3_red", "S");
	GtkStyleFc.mid3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid3_green", "S");
	GtkStyleFc.mid3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid3_blue", "S");
	GtkStyleFc.mid4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid4_pixel", "I");
	GtkStyleFc.mid4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid4_red", "S");
	GtkStyleFc.mid4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid4_green", "S");
	GtkStyleFc.mid4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid4_blue", "S");
	GtkStyleFc.text0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text0_pixel", "I");
	GtkStyleFc.text0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text0_red", "S");
	GtkStyleFc.text0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text0_green", "S");
	GtkStyleFc.text0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text0_blue", "S");
	GtkStyleFc.text1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text1_pixel", "I");
	GtkStyleFc.text1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text1_red", "S");
	GtkStyleFc.text1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text1_green", "S");
	GtkStyleFc.text1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text1_blue", "S");
	GtkStyleFc.text2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text2_pixel", "I");
	GtkStyleFc.text2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text2_red", "S");
	GtkStyleFc.text2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text2_green", "S");
	GtkStyleFc.text2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text2_blue", "S");
	GtkStyleFc.text3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text3_pixel", "I");
	GtkStyleFc.text3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text3_red", "S");
	GtkStyleFc.text3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text3_green", "S");
	GtkStyleFc.text3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text3_blue", "S");
	GtkStyleFc.text4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text4_pixel", "I");
	GtkStyleFc.text4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text4_red", "S");
	GtkStyleFc.text4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text4_green", "S");
	GtkStyleFc.text4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text4_blue", "S");
	GtkStyleFc.base0_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base0_pixel", "I");
	GtkStyleFc.base0_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base0_red", "S");
	GtkStyleFc.base0_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base0_green", "S");
	GtkStyleFc.base0_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base0_blue", "S");
	GtkStyleFc.base1_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base1_pixel", "I");
	GtkStyleFc.base1_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base1_red", "S");
	GtkStyleFc.base1_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base1_green", "S");
	GtkStyleFc.base1_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base1_blue", "S");
	GtkStyleFc.base2_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base2_pixel", "I");
	GtkStyleFc.base2_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base2_red", "S");
	GtkStyleFc.base2_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base2_green", "S");
	GtkStyleFc.base2_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base2_blue", "S");
	GtkStyleFc.base3_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base3_pixel", "I");
	GtkStyleFc.base3_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base3_red", "S");
	GtkStyleFc.base3_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base3_green", "S");
	GtkStyleFc.base3_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base3_blue", "S");
	GtkStyleFc.base4_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base4_pixel", "I");
	GtkStyleFc.base4_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base4_red", "S");
	GtkStyleFc.base4_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base4_green", "S");
	GtkStyleFc.base4_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base4_blue", "S");
	GtkStyleFc.black_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "black_pixel", "I");
	GtkStyleFc.black_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "black_red", "S");
	GtkStyleFc.black_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "black_green", "S");
	GtkStyleFc.black_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "black_blue", "S");
	GtkStyleFc.white_pixel = (*env)->GetFieldID(env, GtkStyleFc.clazz, "white_pixel", "I");
	GtkStyleFc.white_red = (*env)->GetFieldID(env, GtkStyleFc.clazz, "white_red", "S");
	GtkStyleFc.white_green = (*env)->GetFieldID(env, GtkStyleFc.clazz, "white_green", "S");
	GtkStyleFc.white_blue = (*env)->GetFieldID(env, GtkStyleFc.clazz, "white_blue", "S");
	GtkStyleFc.font_desc = (*env)->GetFieldID(env, GtkStyleFc.clazz, "font_desc", "I");
	GtkStyleFc.xthickness = (*env)->GetFieldID(env, GtkStyleFc.clazz, "xthickness", "I");
	GtkStyleFc.ythickness = (*env)->GetFieldID(env, GtkStyleFc.clazz, "ythickness", "I");
	GtkStyleFc.fg_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg_gc0", "I");
	GtkStyleFc.fg_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg_gc1", "I");
	GtkStyleFc.fg_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg_gc2", "I");
	GtkStyleFc.fg_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg_gc3", "I");
	GtkStyleFc.fg_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "fg_gc4", "I");
	GtkStyleFc.bg_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_gc0", "I");
	GtkStyleFc.bg_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_gc1", "I");
	GtkStyleFc.bg_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_gc2", "I");
	GtkStyleFc.bg_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_gc3", "I");
	GtkStyleFc.bg_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_gc4", "I");
	GtkStyleFc.light_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light_gc0", "I");
	GtkStyleFc.light_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light_gc1", "I");
	GtkStyleFc.light_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light_gc2", "I");
	GtkStyleFc.light_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light_gc3", "I");
	GtkStyleFc.light_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "light_gc4", "I");
	GtkStyleFc.dark_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark_gc0", "I");
	GtkStyleFc.dark_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark_gc1", "I");
	GtkStyleFc.dark_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark_gc2", "I");
	GtkStyleFc.dark_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark_gc3", "I");
	GtkStyleFc.dark_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "dark_gc4", "I");
	GtkStyleFc.mid_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid_gc0", "I");
	GtkStyleFc.mid_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid_gc1", "I");
	GtkStyleFc.mid_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid_gc2", "I");
	GtkStyleFc.mid_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid_gc3", "I");
	GtkStyleFc.mid_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "mid_gc4", "I");
	GtkStyleFc.text_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text_gc0", "I");
	GtkStyleFc.text_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text_gc1", "I");
	GtkStyleFc.text_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text_gc2", "I");
	GtkStyleFc.text_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text_gc3", "I");
	GtkStyleFc.text_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "text_gc4", "I");
	GtkStyleFc.base_gc0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base_gc0", "I");
	GtkStyleFc.base_gc1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base_gc1", "I");
	GtkStyleFc.base_gc2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base_gc2", "I");
	GtkStyleFc.base_gc3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base_gc3", "I");
	GtkStyleFc.base_gc4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "base_gc4", "I");
	GtkStyleFc.black_gc = (*env)->GetFieldID(env, GtkStyleFc.clazz, "black_gc", "I");
	GtkStyleFc.white_gc = (*env)->GetFieldID(env, GtkStyleFc.clazz, "white_gc", "I");
	GtkStyleFc.bg_pixmap0 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_pixmap0", "I");
	GtkStyleFc.bg_pixmap1 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_pixmap1", "I");
	GtkStyleFc.bg_pixmap2 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_pixmap2", "I");
	GtkStyleFc.bg_pixmap3 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_pixmap3", "I");
	GtkStyleFc.bg_pixmap4 = (*env)->GetFieldID(env, GtkStyleFc.clazz, "bg_pixmap4", "I");
	GtkStyleFc.cached = 1;
}

GtkStyle *getGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct)
{
	if (!GtkStyleFc.cached) cacheGtkStyleFields(env, lpObject);
	lpStruct->fg[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.fg0_pixel);
	lpStruct->fg[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg0_red);
	lpStruct->fg[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg0_green);
	lpStruct->fg[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg0_blue);
	lpStruct->fg[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.fg1_pixel);
	lpStruct->fg[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg1_red);
	lpStruct->fg[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg1_green);
	lpStruct->fg[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg1_blue);
	lpStruct->fg[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.fg2_pixel);
	lpStruct->fg[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg2_red);
	lpStruct->fg[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg2_green);
	lpStruct->fg[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg2_blue);
	lpStruct->fg[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.fg3_pixel);
	lpStruct->fg[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg3_red);
	lpStruct->fg[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg3_green);
	lpStruct->fg[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg3_blue);
	lpStruct->fg[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.fg4_pixel);
	lpStruct->fg[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg4_red);
	lpStruct->fg[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg4_green);
	lpStruct->fg[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.fg4_blue);
	lpStruct->bg[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.bg0_pixel);
	lpStruct->bg[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg0_red);
	lpStruct->bg[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg0_green);
	lpStruct->bg[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg0_blue);
	lpStruct->bg[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.bg1_pixel);
	lpStruct->bg[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg1_red);
	lpStruct->bg[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg1_green);
	lpStruct->bg[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg1_blue);
	lpStruct->bg[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.bg2_pixel);
	lpStruct->bg[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg2_red);
	lpStruct->bg[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg2_green);
	lpStruct->bg[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg2_blue);
	lpStruct->bg[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.bg3_pixel);
	lpStruct->bg[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg3_red);
	lpStruct->bg[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg3_green);
	lpStruct->bg[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg3_blue);
	lpStruct->bg[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.bg4_pixel);
	lpStruct->bg[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg4_red);
	lpStruct->bg[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg4_green);
	lpStruct->bg[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.bg4_blue);
	lpStruct->light[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.light0_pixel);
	lpStruct->light[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.light0_red);
	lpStruct->light[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.light0_green);
	lpStruct->light[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.light0_blue);
	lpStruct->light[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.light1_pixel);
	lpStruct->light[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.light1_red);
	lpStruct->light[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.light1_green);
	lpStruct->light[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.light1_blue);
	lpStruct->light[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.light2_pixel);
	lpStruct->light[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.light2_red);
	lpStruct->light[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.light2_green);
	lpStruct->light[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.light2_blue);
	lpStruct->light[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.light3_pixel);
	lpStruct->light[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.light3_red);
	lpStruct->light[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.light3_green);
	lpStruct->light[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.light3_blue);
	lpStruct->light[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.light4_pixel);
	lpStruct->light[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.light4_red);
	lpStruct->light[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.light4_green);
	lpStruct->light[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.light4_blue);
	lpStruct->dark[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.dark0_pixel);
	lpStruct->dark[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark0_red);
	lpStruct->dark[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark0_green);
	lpStruct->dark[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark0_blue);
	lpStruct->dark[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.dark1_pixel);
	lpStruct->dark[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark1_red);
	lpStruct->dark[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark1_green);
	lpStruct->dark[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark1_blue);
	lpStruct->dark[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.dark2_pixel);
	lpStruct->dark[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark2_red);
	lpStruct->dark[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark2_green);
	lpStruct->dark[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark2_blue);
	lpStruct->dark[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.dark3_pixel);
	lpStruct->dark[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark3_red);
	lpStruct->dark[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark3_green);
	lpStruct->dark[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark3_blue);
	lpStruct->dark[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.dark4_pixel);
	lpStruct->dark[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark4_red);
	lpStruct->dark[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark4_green);
	lpStruct->dark[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.dark4_blue);
	lpStruct->mid[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.mid0_pixel);
	lpStruct->mid[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid0_red);
	lpStruct->mid[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid0_green);
	lpStruct->mid[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid0_blue);
	lpStruct->mid[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.mid1_pixel);
	lpStruct->mid[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid1_red);
	lpStruct->mid[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid1_green);
	lpStruct->mid[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid1_blue);
	lpStruct->mid[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.mid2_pixel);
	lpStruct->mid[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid2_red);
	lpStruct->mid[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid2_green);
	lpStruct->mid[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid2_blue);
	lpStruct->mid[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.mid3_pixel);
	lpStruct->mid[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid3_red);
	lpStruct->mid[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid3_green);
	lpStruct->mid[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid3_blue);
	lpStruct->mid[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.mid4_pixel);
	lpStruct->mid[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid4_red);
	lpStruct->mid[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid4_green);
	lpStruct->mid[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.mid4_blue);
	lpStruct->text[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.text0_pixel);
	lpStruct->text[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.text0_red);
	lpStruct->text[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.text0_green);
	lpStruct->text[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.text0_blue);
	lpStruct->text[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.text1_pixel);
	lpStruct->text[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.text1_red);
	lpStruct->text[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.text1_green);
	lpStruct->text[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.text1_blue);
	lpStruct->text[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.text2_pixel);
	lpStruct->text[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.text2_red);
	lpStruct->text[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.text2_green);
	lpStruct->text[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.text2_blue);
	lpStruct->text[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.text3_pixel);
	lpStruct->text[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.text3_red);
	lpStruct->text[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.text3_green);
	lpStruct->text[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.text3_blue);
	lpStruct->text[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.text4_pixel);
	lpStruct->text[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.text4_red);
	lpStruct->text[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.text4_green);
	lpStruct->text[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.text4_blue);
	lpStruct->base[0].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.base0_pixel);
	lpStruct->base[0].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.base0_red);
	lpStruct->base[0].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.base0_green);
	lpStruct->base[0].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.base0_blue);
	lpStruct->base[1].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.base1_pixel);
	lpStruct->base[1].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.base1_red);
	lpStruct->base[1].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.base1_green);
	lpStruct->base[1].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.base1_blue);
	lpStruct->base[2].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.base2_pixel);
	lpStruct->base[2].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.base2_red);
	lpStruct->base[2].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.base2_green);
	lpStruct->base[2].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.base2_blue);
	lpStruct->base[3].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.base3_pixel);
	lpStruct->base[3].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.base3_red);
	lpStruct->base[3].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.base3_green);
	lpStruct->base[3].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.base3_blue);
	lpStruct->base[4].pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.base4_pixel);
	lpStruct->base[4].red = (*env)->GetShortField(env, lpObject, GtkStyleFc.base4_red);
	lpStruct->base[4].green = (*env)->GetShortField(env, lpObject, GtkStyleFc.base4_green);
	lpStruct->base[4].blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.base4_blue);
	lpStruct->black.pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.black_pixel);
	lpStruct->black.red = (*env)->GetShortField(env, lpObject, GtkStyleFc.black_red);
	lpStruct->black.green = (*env)->GetShortField(env, lpObject, GtkStyleFc.black_green);
	lpStruct->black.blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.black_blue);
	lpStruct->white.pixel = (*env)->GetIntField(env, lpObject, GtkStyleFc.white_pixel);
	lpStruct->white.red = (*env)->GetShortField(env, lpObject, GtkStyleFc.white_red);
	lpStruct->white.green = (*env)->GetShortField(env, lpObject, GtkStyleFc.white_green);
	lpStruct->white.blue = (*env)->GetShortField(env, lpObject, GtkStyleFc.white_blue);
	lpStruct->font_desc = (PangoFontDescription *)(*env)->GetIntField(env, lpObject, GtkStyleFc.font_desc);
	lpStruct->xthickness = (*env)->GetIntField(env, lpObject, GtkStyleFc.xthickness);
	lpStruct->ythickness = (*env)->GetIntField(env, lpObject, GtkStyleFc.ythickness);
	lpStruct->fg_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.fg_gc0);
	lpStruct->fg_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.fg_gc1);
	lpStruct->fg_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.fg_gc2);
	lpStruct->fg_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.fg_gc3);
	lpStruct->fg_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.fg_gc4);
	lpStruct->bg_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_gc0);
	lpStruct->bg_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_gc1);
	lpStruct->bg_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_gc2);
	lpStruct->bg_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_gc3);
	lpStruct->bg_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_gc4);
	lpStruct->light_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.light_gc0);
	lpStruct->light_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.light_gc1);
	lpStruct->light_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.light_gc2);
	lpStruct->light_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.light_gc3);
	lpStruct->light_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.light_gc4);
	lpStruct->dark_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.dark_gc0);
	lpStruct->dark_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.dark_gc1);
	lpStruct->dark_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.dark_gc2);
	lpStruct->dark_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.dark_gc3);
	lpStruct->dark_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.dark_gc4);
	lpStruct->mid_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.mid_gc0);
	lpStruct->mid_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.mid_gc1);
	lpStruct->mid_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.mid_gc2);
	lpStruct->mid_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.mid_gc3);
	lpStruct->mid_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.mid_gc4);
	lpStruct->text_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.text_gc0);
	lpStruct->text_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.text_gc1);
	lpStruct->text_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.text_gc2);
	lpStruct->text_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.text_gc3);
	lpStruct->text_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.text_gc4);
	lpStruct->base_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.base_gc0);
	lpStruct->base_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.base_gc1);
	lpStruct->base_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.base_gc2);
	lpStruct->base_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.base_gc3);
	lpStruct->base_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.base_gc4);
	lpStruct->black_gc = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.black_gc);
	lpStruct->white_gc = (GdkGC *)(*env)->GetIntField(env, lpObject, GtkStyleFc.white_gc);
	lpStruct->bg_pixmap[0] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_pixmap0);
	lpStruct->bg_pixmap[1] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_pixmap1);
	lpStruct->bg_pixmap[2] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_pixmap2);
	lpStruct->bg_pixmap[3] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_pixmap3);
	lpStruct->bg_pixmap[4] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GtkStyleFc.bg_pixmap4);
	return lpStruct;
}

void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct)
{
	if (!GtkStyleFc.cached) cacheGtkStyleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg0_pixel, (jint)lpStruct->fg[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg0_red, (jshort)lpStruct->fg[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg0_green, (jshort)lpStruct->fg[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg0_blue, (jshort)lpStruct->fg[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg1_pixel, (jint)lpStruct->fg[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg1_red, (jshort)lpStruct->fg[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg1_green, (jshort)lpStruct->fg[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg1_blue, (jshort)lpStruct->fg[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg2_pixel, (jint)lpStruct->fg[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg2_red, (jshort)lpStruct->fg[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg2_green, (jshort)lpStruct->fg[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg2_blue, (jshort)lpStruct->fg[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg3_pixel, (jint)lpStruct->fg[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg3_red, (jshort)lpStruct->fg[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg3_green, (jshort)lpStruct->fg[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg3_blue, (jshort)lpStruct->fg[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg4_pixel, (jint)lpStruct->fg[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg4_red, (jshort)lpStruct->fg[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg4_green, (jshort)lpStruct->fg[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.fg4_blue, (jshort)lpStruct->fg[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg0_pixel, (jint)lpStruct->bg[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg0_red, (jshort)lpStruct->bg[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg0_green, (jshort)lpStruct->bg[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg0_blue, (jshort)lpStruct->bg[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg1_pixel, (jint)lpStruct->bg[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg1_red, (jshort)lpStruct->bg[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg1_green, (jshort)lpStruct->bg[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg1_blue, (jshort)lpStruct->bg[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg2_pixel, (jint)lpStruct->bg[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg2_red, (jshort)lpStruct->bg[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg2_green, (jshort)lpStruct->bg[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg2_blue, (jshort)lpStruct->bg[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg3_pixel, (jint)lpStruct->bg[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg3_red, (jshort)lpStruct->bg[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg3_green, (jshort)lpStruct->bg[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg3_blue, (jshort)lpStruct->bg[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg4_pixel, (jint)lpStruct->bg[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg4_red, (jshort)lpStruct->bg[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg4_green, (jshort)lpStruct->bg[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.bg4_blue, (jshort)lpStruct->bg[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light0_pixel, (jint)lpStruct->light[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light0_red, (jshort)lpStruct->light[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light0_green, (jshort)lpStruct->light[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light0_blue, (jshort)lpStruct->light[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light1_pixel, (jint)lpStruct->light[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light1_red, (jshort)lpStruct->light[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light1_green, (jshort)lpStruct->light[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light1_blue, (jshort)lpStruct->light[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light2_pixel, (jint)lpStruct->light[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light2_red, (jshort)lpStruct->light[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light2_green, (jshort)lpStruct->light[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light2_blue, (jshort)lpStruct->light[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light3_pixel, (jint)lpStruct->light[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light3_red, (jshort)lpStruct->light[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light3_green, (jshort)lpStruct->light[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light3_blue, (jshort)lpStruct->light[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light4_pixel, (jint)lpStruct->light[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light4_red, (jshort)lpStruct->light[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light4_green, (jshort)lpStruct->light[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.light4_blue, (jshort)lpStruct->light[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark0_pixel, (jint)lpStruct->dark[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark0_red, (jshort)lpStruct->dark[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark0_green, (jshort)lpStruct->dark[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark0_blue, (jshort)lpStruct->dark[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark1_pixel, (jint)lpStruct->dark[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark1_red, (jshort)lpStruct->dark[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark1_green, (jshort)lpStruct->dark[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark1_blue, (jshort)lpStruct->dark[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark2_pixel, (jint)lpStruct->dark[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark2_red, (jshort)lpStruct->dark[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark2_green, (jshort)lpStruct->dark[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark2_blue, (jshort)lpStruct->dark[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark3_pixel, (jint)lpStruct->dark[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark3_red, (jshort)lpStruct->dark[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark3_green, (jshort)lpStruct->dark[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark3_blue, (jshort)lpStruct->dark[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark4_pixel, (jint)lpStruct->dark[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark4_red, (jshort)lpStruct->dark[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark4_green, (jshort)lpStruct->dark[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.dark4_blue, (jshort)lpStruct->dark[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid0_pixel, (jint)lpStruct->mid[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid0_red, (jshort)lpStruct->mid[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid0_green, (jshort)lpStruct->mid[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid0_blue, (jshort)lpStruct->mid[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid1_pixel, (jint)lpStruct->mid[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid1_red, (jshort)lpStruct->mid[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid1_green, (jshort)lpStruct->mid[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid1_blue, (jshort)lpStruct->mid[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid2_pixel, (jint)lpStruct->mid[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid2_red, (jshort)lpStruct->mid[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid2_green, (jshort)lpStruct->mid[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid2_blue, (jshort)lpStruct->mid[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid3_pixel, (jint)lpStruct->mid[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid3_red, (jshort)lpStruct->mid[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid3_green, (jshort)lpStruct->mid[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid3_blue, (jshort)lpStruct->mid[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid4_pixel, (jint)lpStruct->mid[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid4_red, (jshort)lpStruct->mid[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid4_green, (jshort)lpStruct->mid[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.mid4_blue, (jshort)lpStruct->mid[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text0_pixel, (jint)lpStruct->text[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text0_red, (jshort)lpStruct->text[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text0_green, (jshort)lpStruct->text[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text0_blue, (jshort)lpStruct->text[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text1_pixel, (jint)lpStruct->text[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text1_red, (jshort)lpStruct->text[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text1_green, (jshort)lpStruct->text[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text1_blue, (jshort)lpStruct->text[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text2_pixel, (jint)lpStruct->text[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text2_red, (jshort)lpStruct->text[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text2_green, (jshort)lpStruct->text[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text2_blue, (jshort)lpStruct->text[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text3_pixel, (jint)lpStruct->text[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text3_red, (jshort)lpStruct->text[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text3_green, (jshort)lpStruct->text[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text3_blue, (jshort)lpStruct->text[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text4_pixel, (jint)lpStruct->text[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text4_red, (jshort)lpStruct->text[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text4_green, (jshort)lpStruct->text[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.text4_blue, (jshort)lpStruct->text[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base0_pixel, (jint)lpStruct->base[0].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base0_red, (jshort)lpStruct->base[0].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base0_green, (jshort)lpStruct->base[0].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base0_blue, (jshort)lpStruct->base[0].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base1_pixel, (jint)lpStruct->base[1].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base1_red, (jshort)lpStruct->base[1].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base1_green, (jshort)lpStruct->base[1].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base1_blue, (jshort)lpStruct->base[1].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base2_pixel, (jint)lpStruct->base[2].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base2_red, (jshort)lpStruct->base[2].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base2_green, (jshort)lpStruct->base[2].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base2_blue, (jshort)lpStruct->base[2].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base3_pixel, (jint)lpStruct->base[3].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base3_red, (jshort)lpStruct->base[3].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base3_green, (jshort)lpStruct->base[3].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base3_blue, (jshort)lpStruct->base[3].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base4_pixel, (jint)lpStruct->base[4].pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base4_red, (jshort)lpStruct->base[4].red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base4_green, (jshort)lpStruct->base[4].green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.base4_blue, (jshort)lpStruct->base[4].blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.black_pixel, (jint)lpStruct->black.pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.black_red, (jshort)lpStruct->black.red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.black_green, (jshort)lpStruct->black.green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.black_blue, (jshort)lpStruct->black.blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.white_pixel, (jint)lpStruct->white.pixel);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.white_red, (jshort)lpStruct->white.red);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.white_green, (jshort)lpStruct->white.green);
	(*env)->SetShortField(env, lpObject, GtkStyleFc.white_blue, (jshort)lpStruct->white.blue);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.font_desc, (jint)lpStruct->font_desc);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.xthickness, (jint)lpStruct->xthickness);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.ythickness, (jint)lpStruct->ythickness);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg_gc0, (jint)lpStruct->fg_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg_gc1, (jint)lpStruct->fg_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg_gc2, (jint)lpStruct->fg_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg_gc3, (jint)lpStruct->fg_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.fg_gc4, (jint)lpStruct->fg_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_gc0, (jint)lpStruct->bg_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_gc1, (jint)lpStruct->bg_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_gc2, (jint)lpStruct->bg_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_gc3, (jint)lpStruct->bg_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_gc4, (jint)lpStruct->bg_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light_gc0, (jint)lpStruct->light_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light_gc1, (jint)lpStruct->light_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light_gc2, (jint)lpStruct->light_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light_gc3, (jint)lpStruct->light_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.light_gc4, (jint)lpStruct->light_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark_gc0, (jint)lpStruct->dark_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark_gc1, (jint)lpStruct->dark_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark_gc2, (jint)lpStruct->dark_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark_gc3, (jint)lpStruct->dark_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.dark_gc4, (jint)lpStruct->dark_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid_gc0, (jint)lpStruct->mid_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid_gc1, (jint)lpStruct->mid_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid_gc2, (jint)lpStruct->mid_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid_gc3, (jint)lpStruct->mid_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.mid_gc4, (jint)lpStruct->mid_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text_gc0, (jint)lpStruct->text_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text_gc1, (jint)lpStruct->text_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text_gc2, (jint)lpStruct->text_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text_gc3, (jint)lpStruct->text_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.text_gc4, (jint)lpStruct->text_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base_gc0, (jint)lpStruct->base_gc[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base_gc1, (jint)lpStruct->base_gc[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base_gc2, (jint)lpStruct->base_gc[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base_gc3, (jint)lpStruct->base_gc[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.base_gc4, (jint)lpStruct->base_gc[4]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.black_gc, (jint)lpStruct->black_gc);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.white_gc, (jint)lpStruct->white_gc);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_pixmap0, (jint)lpStruct->bg_pixmap[0]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_pixmap1, (jint)lpStruct->bg_pixmap[1]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_pixmap2, (jint)lpStruct->bg_pixmap[2]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_pixmap3, (jint)lpStruct->bg_pixmap[3]);
	(*env)->SetIntField(env, lpObject, GtkStyleFc.bg_pixmap4, (jint)lpStruct->bg_pixmap[4]);
}
#endif

#ifndef NO_GtkTargetEntry
typedef struct GtkTargetEntry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, flags, info;
} GtkTargetEntry_FID_CACHE;

GtkTargetEntry_FID_CACHE GtkTargetEntryFc;

void cacheGtkTargetEntryFields(JNIEnv *env, jobject lpObject)
{
	if (GtkTargetEntryFc.cached) return;
	GtkTargetEntryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkTargetEntryFc.target = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "target", "I");
	GtkTargetEntryFc.flags = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "flags", "I");
	GtkTargetEntryFc.info = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "info", "I");
	GtkTargetEntryFc.cached = 1;
}

GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	lpStruct->target = (gchar *)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.info);
	return lpStruct;
}

void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.info, (jint)lpStruct->info);
}
#endif

#ifndef NO_GtkTargetPair
typedef struct GtkTargetPair_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, flags, info;
} GtkTargetPair_FID_CACHE;

GtkTargetPair_FID_CACHE GtkTargetPairFc;

void cacheGtkTargetPairFields(JNIEnv *env, jobject lpObject)
{
	if (GtkTargetPairFc.cached) return;
	GtkTargetPairFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkTargetPairFc.target = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "target", "I");
	GtkTargetPairFc.flags = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "flags", "I");
	GtkTargetPairFc.info = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "info", "I");
	GtkTargetPairFc.cached = 1;
}

GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.info);
	return lpStruct;
}

void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.info, (jint)lpStruct->info);
}
#endif

