/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "atk_structs.h"

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

