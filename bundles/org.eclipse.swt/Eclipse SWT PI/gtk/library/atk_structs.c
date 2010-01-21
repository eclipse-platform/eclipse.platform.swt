/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
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
	AtkActionIfaceFc.do_action = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "do_action", I_J);
	AtkActionIfaceFc.get_n_actions = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_n_actions", I_J);
	AtkActionIfaceFc.get_description = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_description", I_J);
	AtkActionIfaceFc.get_name = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_name", I_J);
	AtkActionIfaceFc.get_keybinding = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "get_keybinding", I_J);
	AtkActionIfaceFc.set_description = (*env)->GetFieldID(env, AtkActionIfaceFc.clazz, "set_description", I_J);
	AtkActionIfaceFc.cached = 1;
}

AtkActionIface *getAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct)
{
	if (!AtkActionIfaceFc.cached) cacheAtkActionIfaceFields(env, lpObject);
	lpStruct->do_action = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.do_action);
	lpStruct->get_n_actions = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.get_n_actions);
	lpStruct->get_description = (G_CONST_RETURN gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.get_description);
	lpStruct->get_name = (G_CONST_RETURN gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.get_name);
	lpStruct->get_keybinding = (G_CONST_RETURN gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.get_keybinding);
	lpStruct->set_description = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkActionIfaceFc.set_description);
	return lpStruct;
}

void setAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct)
{
	if (!AtkActionIfaceFc.cached) cacheAtkActionIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.do_action, (jintLong)lpStruct->do_action);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.get_n_actions, (jintLong)lpStruct->get_n_actions);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.get_description, (jintLong)lpStruct->get_description);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.get_name, (jintLong)lpStruct->get_name);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.get_keybinding, (jintLong)lpStruct->get_keybinding);
	(*env)->SetIntLongField(env, lpObject, AtkActionIfaceFc.set_description, (jintLong)lpStruct->set_description);
}
#endif

#ifndef NO_AtkAttribute
typedef struct AtkAttribute_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID name, value;
} AtkAttribute_FID_CACHE;

AtkAttribute_FID_CACHE AtkAttributeFc;

void cacheAtkAttributeFields(JNIEnv *env, jobject lpObject)
{
	if (AtkAttributeFc.cached) return;
	AtkAttributeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkAttributeFc.name = (*env)->GetFieldID(env, AtkAttributeFc.clazz, "name", I_J);
	AtkAttributeFc.value = (*env)->GetFieldID(env, AtkAttributeFc.clazz, "value", I_J);
	AtkAttributeFc.cached = 1;
}

AtkAttribute *getAtkAttributeFields(JNIEnv *env, jobject lpObject, AtkAttribute *lpStruct)
{
	if (!AtkAttributeFc.cached) cacheAtkAttributeFields(env, lpObject);
	lpStruct->name = (char *)(*env)->GetIntLongField(env, lpObject, AtkAttributeFc.name);
	lpStruct->value = (char *)(*env)->GetIntLongField(env, lpObject, AtkAttributeFc.value);
	return lpStruct;
}

void setAtkAttributeFields(JNIEnv *env, jobject lpObject, AtkAttribute *lpStruct)
{
	if (!AtkAttributeFc.cached) cacheAtkAttributeFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkAttributeFc.name, (jintLong)lpStruct->name);
	(*env)->SetIntLongField(env, lpObject, AtkAttributeFc.value, (jintLong)lpStruct->value);
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
	AtkComponentIfaceFc.add_focus_handler = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "add_focus_handler", I_J);
	AtkComponentIfaceFc.contains = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "contains", I_J);
	AtkComponentIfaceFc.ref_accessible_at_point = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "ref_accessible_at_point", I_J);
	AtkComponentIfaceFc.get_extents = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_extents", I_J);
	AtkComponentIfaceFc.get_position = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_position", I_J);
	AtkComponentIfaceFc.get_size = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_size", I_J);
	AtkComponentIfaceFc.grab_focus = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "grab_focus", I_J);
	AtkComponentIfaceFc.remove_focus_handler = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "remove_focus_handler", I_J);
	AtkComponentIfaceFc.set_extents = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_extents", I_J);
	AtkComponentIfaceFc.set_position = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_position", I_J);
	AtkComponentIfaceFc.set_size = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "set_size", I_J);
	AtkComponentIfaceFc.get_layer = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_layer", I_J);
	AtkComponentIfaceFc.get_mdi_zorder = (*env)->GetFieldID(env, AtkComponentIfaceFc.clazz, "get_mdi_zorder", I_J);
	AtkComponentIfaceFc.cached = 1;
}

AtkComponentIface *getAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct)
{
	if (!AtkComponentIfaceFc.cached) cacheAtkComponentIfaceFields(env, lpObject);
	lpStruct->add_focus_handler = (guint (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.add_focus_handler);
	lpStruct->contains = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.contains);
	lpStruct->ref_accessible_at_point = (AtkObject *(*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.ref_accessible_at_point);
	lpStruct->get_extents = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.get_extents);
	lpStruct->get_position = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.get_position);
	lpStruct->get_size = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.get_size);
	lpStruct->grab_focus = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.grab_focus);
	lpStruct->remove_focus_handler = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.remove_focus_handler);
	lpStruct->set_extents = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.set_extents);
	lpStruct->set_position = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.set_position);
	lpStruct->set_size = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.set_size);
	lpStruct->get_layer = (AtkLayer (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.get_layer);
	lpStruct->get_mdi_zorder = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkComponentIfaceFc.get_mdi_zorder);
	return lpStruct;
}

void setAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct)
{
	if (!AtkComponentIfaceFc.cached) cacheAtkComponentIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.add_focus_handler, (jintLong)lpStruct->add_focus_handler);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.contains, (jintLong)lpStruct->contains);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.ref_accessible_at_point, (jintLong)lpStruct->ref_accessible_at_point);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.get_extents, (jintLong)lpStruct->get_extents);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.get_position, (jintLong)lpStruct->get_position);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.get_size, (jintLong)lpStruct->get_size);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.grab_focus, (jintLong)lpStruct->grab_focus);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.remove_focus_handler, (jintLong)lpStruct->remove_focus_handler);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.set_extents, (jintLong)lpStruct->set_extents);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.set_position, (jintLong)lpStruct->set_position);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.set_size, (jintLong)lpStruct->set_size);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.get_layer, (jintLong)lpStruct->get_layer);
	(*env)->SetIntLongField(env, lpObject, AtkComponentIfaceFc.get_mdi_zorder, (jintLong)lpStruct->get_mdi_zorder);
}
#endif

#ifndef NO_AtkHypertextIface
typedef struct AtkHypertextIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID get_link, get_n_links, get_link_index;
} AtkHypertextIface_FID_CACHE;

AtkHypertextIface_FID_CACHE AtkHypertextIfaceFc;

void cacheAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkHypertextIfaceFc.cached) return;
	AtkHypertextIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkHypertextIfaceFc.get_link = (*env)->GetFieldID(env, AtkHypertextIfaceFc.clazz, "get_link", I_J);
	AtkHypertextIfaceFc.get_n_links = (*env)->GetFieldID(env, AtkHypertextIfaceFc.clazz, "get_n_links", I_J);
	AtkHypertextIfaceFc.get_link_index = (*env)->GetFieldID(env, AtkHypertextIfaceFc.clazz, "get_link_index", I_J);
	AtkHypertextIfaceFc.cached = 1;
}

AtkHypertextIface *getAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct)
{
	if (!AtkHypertextIfaceFc.cached) cacheAtkHypertextIfaceFields(env, lpObject);
	lpStruct->get_link = (AtkHyperlink *(*)())(*env)->GetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_link);
	lpStruct->get_n_links = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_n_links);
	lpStruct->get_link_index = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_link_index);
	return lpStruct;
}

void setAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct)
{
	if (!AtkHypertextIfaceFc.cached) cacheAtkHypertextIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_link, (jintLong)lpStruct->get_link);
	(*env)->SetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_n_links, (jintLong)lpStruct->get_n_links);
	(*env)->SetIntLongField(env, lpObject, AtkHypertextIfaceFc.get_link_index, (jintLong)lpStruct->get_link_index);
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
	AtkObjectClassFc.get_name = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_name", I_J);
	AtkObjectClassFc.get_description = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_description", I_J);
	AtkObjectClassFc.get_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_parent", I_J);
	AtkObjectClassFc.get_n_children = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_n_children", I_J);
	AtkObjectClassFc.ref_child = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_child", I_J);
	AtkObjectClassFc.get_index_in_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_index_in_parent", I_J);
	AtkObjectClassFc.ref_relation_set = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_relation_set", I_J);
	AtkObjectClassFc.get_role = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_role", I_J);
	AtkObjectClassFc.get_layer = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_layer", I_J);
	AtkObjectClassFc.get_mdi_zorder = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "get_mdi_zorder", I_J);
	AtkObjectClassFc.ref_state_set = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "ref_state_set", I_J);
	AtkObjectClassFc.set_name = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_name", I_J);
	AtkObjectClassFc.set_description = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_description", I_J);
	AtkObjectClassFc.set_parent = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_parent", I_J);
	AtkObjectClassFc.set_role = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "set_role", I_J);
	AtkObjectClassFc.connect_property_change_handler = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "connect_property_change_handler", I_J);
	AtkObjectClassFc.remove_property_change_handler = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "remove_property_change_handler", I_J);
	AtkObjectClassFc.initialize = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "initialize", I_J);
	AtkObjectClassFc.children_changed = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "children_changed", I_J);
	AtkObjectClassFc.focus_event = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "focus_event", I_J);
	AtkObjectClassFc.property_change = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "property_change", I_J);
	AtkObjectClassFc.state_change = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "state_change", I_J);
	AtkObjectClassFc.visible_data_changed = (*env)->GetFieldID(env, AtkObjectClassFc.clazz, "visible_data_changed", I_J);
	AtkObjectClassFc.cached = 1;
}

AtkObjectClass *getAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct)
{
	if (!AtkObjectClassFc.cached) cacheAtkObjectClassFields(env, lpObject);
	lpStruct->get_name = (G_CONST_RETURN gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_name);
	lpStruct->get_description = (G_CONST_RETURN gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_description);
	lpStruct->get_parent = (AtkObject *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_parent);
	lpStruct->get_n_children = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_n_children);
	lpStruct->ref_child = (AtkObject *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.ref_child);
	lpStruct->get_index_in_parent = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_index_in_parent);
	lpStruct->ref_relation_set = (AtkRelationSet *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.ref_relation_set);
	lpStruct->get_role = (AtkRole (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_role);
	lpStruct->get_layer = (AtkLayer (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_layer);
	lpStruct->get_mdi_zorder = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.get_mdi_zorder);
	lpStruct->ref_state_set = (AtkStateSet *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.ref_state_set);
	lpStruct->set_name = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.set_name);
	lpStruct->set_description = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.set_description);
	lpStruct->set_parent = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.set_parent);
	lpStruct->set_role = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.set_role);
	lpStruct->connect_property_change_handler = (guint (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.connect_property_change_handler);
	lpStruct->remove_property_change_handler = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.remove_property_change_handler);
	lpStruct->initialize = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.initialize);
	lpStruct->children_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.children_changed);
	lpStruct->focus_event = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.focus_event);
	lpStruct->property_change = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.property_change);
	lpStruct->state_change = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.state_change);
	lpStruct->visible_data_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectClassFc.visible_data_changed);
	return lpStruct;
}

void setAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct)
{
	if (!AtkObjectClassFc.cached) cacheAtkObjectClassFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_name, (jintLong)lpStruct->get_name);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_description, (jintLong)lpStruct->get_description);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_parent, (jintLong)lpStruct->get_parent);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_n_children, (jintLong)lpStruct->get_n_children);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.ref_child, (jintLong)lpStruct->ref_child);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_index_in_parent, (jintLong)lpStruct->get_index_in_parent);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.ref_relation_set, (jintLong)lpStruct->ref_relation_set);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_role, (jintLong)lpStruct->get_role);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_layer, (jintLong)lpStruct->get_layer);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.get_mdi_zorder, (jintLong)lpStruct->get_mdi_zorder);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.ref_state_set, (jintLong)lpStruct->ref_state_set);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.set_name, (jintLong)lpStruct->set_name);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.set_description, (jintLong)lpStruct->set_description);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.set_parent, (jintLong)lpStruct->set_parent);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.set_role, (jintLong)lpStruct->set_role);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.connect_property_change_handler, (jintLong)lpStruct->connect_property_change_handler);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.remove_property_change_handler, (jintLong)lpStruct->remove_property_change_handler);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.initialize, (jintLong)lpStruct->initialize);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.children_changed, (jintLong)lpStruct->children_changed);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.focus_event, (jintLong)lpStruct->focus_event);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.property_change, (jintLong)lpStruct->property_change);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.state_change, (jintLong)lpStruct->state_change);
	(*env)->SetIntLongField(env, lpObject, AtkObjectClassFc.visible_data_changed, (jintLong)lpStruct->visible_data_changed);
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
	AtkObjectFactoryClassFc.create_accessible = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "create_accessible", I_J);
	AtkObjectFactoryClassFc.invalidate = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "invalidate", I_J);
	AtkObjectFactoryClassFc.get_accessible_type = (*env)->GetFieldID(env, AtkObjectFactoryClassFc.clazz, "get_accessible_type", I_J);
	AtkObjectFactoryClassFc.cached = 1;
}

AtkObjectFactoryClass *getAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct)
{
	if (!AtkObjectFactoryClassFc.cached) cacheAtkObjectFactoryClassFields(env, lpObject);
	lpStruct->create_accessible = (AtkObject *(*)())(*env)->GetIntLongField(env, lpObject, AtkObjectFactoryClassFc.create_accessible);
	lpStruct->invalidate = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectFactoryClassFc.invalidate);
	lpStruct->get_accessible_type = (GType (*)())(*env)->GetIntLongField(env, lpObject, AtkObjectFactoryClassFc.get_accessible_type);
	return lpStruct;
}

void setAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct)
{
	if (!AtkObjectFactoryClassFc.cached) cacheAtkObjectFactoryClassFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkObjectFactoryClassFc.create_accessible, (jintLong)lpStruct->create_accessible);
	(*env)->SetIntLongField(env, lpObject, AtkObjectFactoryClassFc.invalidate, (jintLong)lpStruct->invalidate);
	(*env)->SetIntLongField(env, lpObject, AtkObjectFactoryClassFc.get_accessible_type, (jintLong)lpStruct->get_accessible_type);
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
	AtkSelectionIfaceFc.add_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "add_selection", I_J);
	AtkSelectionIfaceFc.clear_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "clear_selection", I_J);
	AtkSelectionIfaceFc.ref_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "ref_selection", I_J);
	AtkSelectionIfaceFc.get_selection_count = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "get_selection_count", I_J);
	AtkSelectionIfaceFc.is_child_selected = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "is_child_selected", I_J);
	AtkSelectionIfaceFc.remove_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "remove_selection", I_J);
	AtkSelectionIfaceFc.select_all_selection = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "select_all_selection", I_J);
	AtkSelectionIfaceFc.selection_changed = (*env)->GetFieldID(env, AtkSelectionIfaceFc.clazz, "selection_changed", I_J);
	AtkSelectionIfaceFc.cached = 1;
}

AtkSelectionIface *getAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct)
{
	if (!AtkSelectionIfaceFc.cached) cacheAtkSelectionIfaceFields(env, lpObject);
	lpStruct->add_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.add_selection);
	lpStruct->clear_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.clear_selection);
	lpStruct->ref_selection = (AtkObject *(*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.ref_selection);
	lpStruct->get_selection_count = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.get_selection_count);
	lpStruct->is_child_selected = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.is_child_selected);
	lpStruct->remove_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.remove_selection);
	lpStruct->select_all_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.select_all_selection);
	lpStruct->selection_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkSelectionIfaceFc.selection_changed);
	return lpStruct;
}

void setAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct)
{
	if (!AtkSelectionIfaceFc.cached) cacheAtkSelectionIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.add_selection, (jintLong)lpStruct->add_selection);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.clear_selection, (jintLong)lpStruct->clear_selection);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.ref_selection, (jintLong)lpStruct->ref_selection);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.get_selection_count, (jintLong)lpStruct->get_selection_count);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.is_child_selected, (jintLong)lpStruct->is_child_selected);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.remove_selection, (jintLong)lpStruct->remove_selection);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.select_all_selection, (jintLong)lpStruct->select_all_selection);
	(*env)->SetIntLongField(env, lpObject, AtkSelectionIfaceFc.selection_changed, (jintLong)lpStruct->selection_changed);
}
#endif

#ifndef NO_AtkTableIface
typedef struct AtkTableIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ref_at, get_index_at, get_column_at_index, get_row_at_index, get_n_columns, get_n_rows, get_column_extent_at, get_row_extent_at, get_caption, get_column_description, get_column_header, get_row_description, get_row_header, get_summary, set_caption, set_column_description, set_column_header, set_row_description, set_row_header, set_summary, get_selected_columns, get_selected_rows, is_column_selected, is_row_selected, is_selected, add_row_selection, remove_row_selection, add_column_selection, remove_column_selection, row_inserted, column_inserted, row_deleted, column_deleted, row_reordered, column_reordered, model_changed;
} AtkTableIface_FID_CACHE;

AtkTableIface_FID_CACHE AtkTableIfaceFc;

void cacheAtkTableIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkTableIfaceFc.cached) return;
	AtkTableIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkTableIfaceFc.ref_at = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "ref_at", I_J);
	AtkTableIfaceFc.get_index_at = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_index_at", I_J);
	AtkTableIfaceFc.get_column_at_index = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_column_at_index", I_J);
	AtkTableIfaceFc.get_row_at_index = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_row_at_index", I_J);
	AtkTableIfaceFc.get_n_columns = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_n_columns", I_J);
	AtkTableIfaceFc.get_n_rows = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_n_rows", I_J);
	AtkTableIfaceFc.get_column_extent_at = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_column_extent_at", I_J);
	AtkTableIfaceFc.get_row_extent_at = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_row_extent_at", I_J);
	AtkTableIfaceFc.get_caption = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_caption", I_J);
	AtkTableIfaceFc.get_column_description = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_column_description", I_J);
	AtkTableIfaceFc.get_column_header = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_column_header", I_J);
	AtkTableIfaceFc.get_row_description = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_row_description", I_J);
	AtkTableIfaceFc.get_row_header = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_row_header", I_J);
	AtkTableIfaceFc.get_summary = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_summary", I_J);
	AtkTableIfaceFc.set_caption = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_caption", I_J);
	AtkTableIfaceFc.set_column_description = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_column_description", I_J);
	AtkTableIfaceFc.set_column_header = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_column_header", I_J);
	AtkTableIfaceFc.set_row_description = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_row_description", I_J);
	AtkTableIfaceFc.set_row_header = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_row_header", I_J);
	AtkTableIfaceFc.set_summary = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "set_summary", I_J);
	AtkTableIfaceFc.get_selected_columns = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_selected_columns", I_J);
	AtkTableIfaceFc.get_selected_rows = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "get_selected_rows", I_J);
	AtkTableIfaceFc.is_column_selected = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "is_column_selected", I_J);
	AtkTableIfaceFc.is_row_selected = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "is_row_selected", I_J);
	AtkTableIfaceFc.is_selected = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "is_selected", I_J);
	AtkTableIfaceFc.add_row_selection = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "add_row_selection", I_J);
	AtkTableIfaceFc.remove_row_selection = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "remove_row_selection", I_J);
	AtkTableIfaceFc.add_column_selection = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "add_column_selection", I_J);
	AtkTableIfaceFc.remove_column_selection = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "remove_column_selection", I_J);
	AtkTableIfaceFc.row_inserted = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "row_inserted", I_J);
	AtkTableIfaceFc.column_inserted = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "column_inserted", I_J);
	AtkTableIfaceFc.row_deleted = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "row_deleted", I_J);
	AtkTableIfaceFc.column_deleted = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "column_deleted", I_J);
	AtkTableIfaceFc.row_reordered = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "row_reordered", I_J);
	AtkTableIfaceFc.column_reordered = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "column_reordered", I_J);
	AtkTableIfaceFc.model_changed = (*env)->GetFieldID(env, AtkTableIfaceFc.clazz, "model_changed", I_J);
	AtkTableIfaceFc.cached = 1;
}

AtkTableIface *getAtkTableIfaceFields(JNIEnv *env, jobject lpObject, AtkTableIface *lpStruct)
{
	if (!AtkTableIfaceFc.cached) cacheAtkTableIfaceFields(env, lpObject);
	lpStruct->ref_at = (AtkObject* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.ref_at);
	lpStruct->get_index_at = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_index_at);
	lpStruct->get_column_at_index = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_at_index);
	lpStruct->get_row_at_index = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_at_index);
	lpStruct->get_n_columns = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_n_columns);
	lpStruct->get_n_rows = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_n_rows);
	lpStruct->get_column_extent_at = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_extent_at);
	lpStruct->get_row_extent_at = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_extent_at);
	lpStruct->get_caption = (AtkObject* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_caption);
	lpStruct->get_column_description = (G_CONST_RETURN gchar* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_description);
	lpStruct->get_column_header = (AtkObject* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_header);
	lpStruct->get_row_description = (G_CONST_RETURN gchar* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_description);
	lpStruct->get_row_header = (AtkObject* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_header);
	lpStruct->get_summary = (AtkObject* (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_summary);
	lpStruct->set_caption = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_caption);
	lpStruct->set_column_description = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_column_description);
	lpStruct->set_column_header = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_column_header);
	lpStruct->set_row_description = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_row_description);
	lpStruct->set_row_header = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_row_header);
	lpStruct->set_summary = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.set_summary);
	lpStruct->get_selected_columns = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_selected_columns);
	lpStruct->get_selected_rows = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.get_selected_rows);
	lpStruct->is_column_selected = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.is_column_selected);
	lpStruct->is_row_selected = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.is_row_selected);
	lpStruct->is_selected = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.is_selected);
	lpStruct->add_row_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.add_row_selection);
	lpStruct->remove_row_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.remove_row_selection);
	lpStruct->add_column_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.add_column_selection);
	lpStruct->remove_column_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.remove_column_selection);
	lpStruct->row_inserted = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.row_inserted);
	lpStruct->column_inserted = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.column_inserted);
	lpStruct->row_deleted = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.row_deleted);
	lpStruct->column_deleted = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.column_deleted);
	lpStruct->row_reordered = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.row_reordered);
	lpStruct->column_reordered = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.column_reordered);
	lpStruct->model_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTableIfaceFc.model_changed);
	return lpStruct;
}

void setAtkTableIfaceFields(JNIEnv *env, jobject lpObject, AtkTableIface *lpStruct)
{
	if (!AtkTableIfaceFc.cached) cacheAtkTableIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.ref_at, (jintLong)lpStruct->ref_at);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_index_at, (jintLong)lpStruct->get_index_at);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_at_index, (jintLong)lpStruct->get_column_at_index);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_at_index, (jintLong)lpStruct->get_row_at_index);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_n_columns, (jintLong)lpStruct->get_n_columns);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_n_rows, (jintLong)lpStruct->get_n_rows);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_extent_at, (jintLong)lpStruct->get_column_extent_at);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_extent_at, (jintLong)lpStruct->get_row_extent_at);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_caption, (jintLong)lpStruct->get_caption);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_description, (jintLong)lpStruct->get_column_description);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_column_header, (jintLong)lpStruct->get_column_header);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_description, (jintLong)lpStruct->get_row_description);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_row_header, (jintLong)lpStruct->get_row_header);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_summary, (jintLong)lpStruct->get_summary);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_caption, (jintLong)lpStruct->set_caption);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_column_description, (jintLong)lpStruct->set_column_description);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_column_header, (jintLong)lpStruct->set_column_header);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_row_description, (jintLong)lpStruct->set_row_description);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_row_header, (jintLong)lpStruct->set_row_header);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.set_summary, (jintLong)lpStruct->set_summary);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_selected_columns, (jintLong)lpStruct->get_selected_columns);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.get_selected_rows, (jintLong)lpStruct->get_selected_rows);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.is_column_selected, (jintLong)lpStruct->is_column_selected);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.is_row_selected, (jintLong)lpStruct->is_row_selected);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.is_selected, (jintLong)lpStruct->is_selected);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.add_row_selection, (jintLong)lpStruct->add_row_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.remove_row_selection, (jintLong)lpStruct->remove_row_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.add_column_selection, (jintLong)lpStruct->add_column_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.remove_column_selection, (jintLong)lpStruct->remove_column_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.row_inserted, (jintLong)lpStruct->row_inserted);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.column_inserted, (jintLong)lpStruct->column_inserted);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.row_deleted, (jintLong)lpStruct->row_deleted);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.column_deleted, (jintLong)lpStruct->column_deleted);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.row_reordered, (jintLong)lpStruct->row_reordered);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.column_reordered, (jintLong)lpStruct->column_reordered);
	(*env)->SetIntLongField(env, lpObject, AtkTableIfaceFc.model_changed, (jintLong)lpStruct->model_changed);
}
#endif

#ifndef NO_AtkTextIface
typedef struct AtkTextIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID get_text, get_text_after_offset, get_text_at_offset, get_character_at_offset, get_text_before_offset, get_caret_offset, get_run_attributes, get_default_attributes, get_character_extents, get_character_count, get_offset_at_point, get_n_selections, get_selection, add_selection, remove_selection, set_selection, set_caret_offset, text_changed, text_caret_moved, text_selection_changed, get_range_extents, get_bounded_ranges;
} AtkTextIface_FID_CACHE;

AtkTextIface_FID_CACHE AtkTextIfaceFc;

void cacheAtkTextIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkTextIfaceFc.cached) return;
	AtkTextIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkTextIfaceFc.get_text = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text", I_J);
	AtkTextIfaceFc.get_text_after_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_after_offset", I_J);
	AtkTextIfaceFc.get_text_at_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_at_offset", I_J);
	AtkTextIfaceFc.get_character_at_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_at_offset", I_J);
	AtkTextIfaceFc.get_text_before_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_text_before_offset", I_J);
	AtkTextIfaceFc.get_caret_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_caret_offset", I_J);
	AtkTextIfaceFc.get_run_attributes = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_run_attributes", I_J);
	AtkTextIfaceFc.get_default_attributes = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_default_attributes", I_J);
	AtkTextIfaceFc.get_character_extents = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_extents", I_J);
	AtkTextIfaceFc.get_character_count = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_character_count", I_J);
	AtkTextIfaceFc.get_offset_at_point = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_offset_at_point", I_J);
	AtkTextIfaceFc.get_n_selections = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_n_selections", I_J);
	AtkTextIfaceFc.get_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_selection", I_J);
	AtkTextIfaceFc.add_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "add_selection", I_J);
	AtkTextIfaceFc.remove_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "remove_selection", I_J);
	AtkTextIfaceFc.set_selection = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "set_selection", I_J);
	AtkTextIfaceFc.set_caret_offset = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "set_caret_offset", I_J);
	AtkTextIfaceFc.text_changed = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_changed", I_J);
	AtkTextIfaceFc.text_caret_moved = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_caret_moved", I_J);
	AtkTextIfaceFc.text_selection_changed = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "text_selection_changed", I_J);
	AtkTextIfaceFc.get_range_extents = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_range_extents", I_J);
	AtkTextIfaceFc.get_bounded_ranges = (*env)->GetFieldID(env, AtkTextIfaceFc.clazz, "get_bounded_ranges", I_J);
	AtkTextIfaceFc.cached = 1;
}

AtkTextIface *getAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct)
{
	if (!AtkTextIfaceFc.cached) cacheAtkTextIfaceFields(env, lpObject);
	lpStruct->get_text = (gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_text);
	lpStruct->get_text_after_offset = (gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_after_offset);
	lpStruct->get_text_at_offset = (gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_at_offset);
	lpStruct->get_character_at_offset = (gunichar (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_at_offset);
	lpStruct->get_text_before_offset = (gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_before_offset);
	lpStruct->get_caret_offset = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_caret_offset);
	lpStruct->get_run_attributes = (AtkAttributeSet *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_run_attributes);
	lpStruct->get_default_attributes = (AtkAttributeSet *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_default_attributes);
	lpStruct->get_character_extents = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_extents);
	lpStruct->get_character_count = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_count);
	lpStruct->get_offset_at_point = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_offset_at_point);
	lpStruct->get_n_selections = (gint (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_n_selections);
	lpStruct->get_selection = (gchar *(*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_selection);
	lpStruct->add_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.add_selection);
	lpStruct->remove_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.remove_selection);
	lpStruct->set_selection = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.set_selection);
	lpStruct->set_caret_offset = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.set_caret_offset);
	lpStruct->text_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.text_changed);
	lpStruct->text_caret_moved = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.text_caret_moved);
	lpStruct->text_selection_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.text_selection_changed);
	lpStruct->get_range_extents = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_range_extents);
	lpStruct->get_bounded_ranges = (AtkTextRange** (*)())(*env)->GetIntLongField(env, lpObject, AtkTextIfaceFc.get_bounded_ranges);
	return lpStruct;
}

void setAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct)
{
	if (!AtkTextIfaceFc.cached) cacheAtkTextIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_text, (jintLong)lpStruct->get_text);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_after_offset, (jintLong)lpStruct->get_text_after_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_at_offset, (jintLong)lpStruct->get_text_at_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_at_offset, (jintLong)lpStruct->get_character_at_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_text_before_offset, (jintLong)lpStruct->get_text_before_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_caret_offset, (jintLong)lpStruct->get_caret_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_run_attributes, (jintLong)lpStruct->get_run_attributes);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_default_attributes, (jintLong)lpStruct->get_default_attributes);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_extents, (jintLong)lpStruct->get_character_extents);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_character_count, (jintLong)lpStruct->get_character_count);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_offset_at_point, (jintLong)lpStruct->get_offset_at_point);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_n_selections, (jintLong)lpStruct->get_n_selections);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_selection, (jintLong)lpStruct->get_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.add_selection, (jintLong)lpStruct->add_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.remove_selection, (jintLong)lpStruct->remove_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.set_selection, (jintLong)lpStruct->set_selection);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.set_caret_offset, (jintLong)lpStruct->set_caret_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.text_changed, (jintLong)lpStruct->text_changed);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.text_caret_moved, (jintLong)lpStruct->text_caret_moved);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.text_selection_changed, (jintLong)lpStruct->text_selection_changed);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_range_extents, (jintLong)lpStruct->get_range_extents);
	(*env)->SetIntLongField(env, lpObject, AtkTextIfaceFc.get_bounded_ranges, (jintLong)lpStruct->get_bounded_ranges);
}
#endif

#ifndef NO_AtkTextRange
typedef struct AtkTextRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bounds, start_offset, end_offset, content;
} AtkTextRange_FID_CACHE;

AtkTextRange_FID_CACHE AtkTextRangeFc;

void cacheAtkTextRangeFields(JNIEnv *env, jobject lpObject)
{
	if (AtkTextRangeFc.cached) return;
	AtkTextRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkTextRangeFc.bounds = (*env)->GetFieldID(env, AtkTextRangeFc.clazz, "bounds", "Lorg/eclipse/swt/internal/accessibility/gtk/AtkTextRectangle;");
	AtkTextRangeFc.start_offset = (*env)->GetFieldID(env, AtkTextRangeFc.clazz, "start_offset", "I");
	AtkTextRangeFc.end_offset = (*env)->GetFieldID(env, AtkTextRangeFc.clazz, "end_offset", "I");
	AtkTextRangeFc.content = (*env)->GetFieldID(env, AtkTextRangeFc.clazz, "content", I_J);
	AtkTextRangeFc.cached = 1;
}

AtkTextRange *getAtkTextRangeFields(JNIEnv *env, jobject lpObject, AtkTextRange *lpStruct)
{
	if (!AtkTextRangeFc.cached) cacheAtkTextRangeFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, AtkTextRangeFc.bounds);
	if (lpObject1 != NULL) getAtkTextRectangleFields(env, lpObject1, &lpStruct->bounds);
	}
	lpStruct->start_offset = (*env)->GetIntField(env, lpObject, AtkTextRangeFc.start_offset);
	lpStruct->end_offset = (*env)->GetIntField(env, lpObject, AtkTextRangeFc.end_offset);
	lpStruct->content = (gchar *)(*env)->GetIntLongField(env, lpObject, AtkTextRangeFc.content);
	return lpStruct;
}

void setAtkTextRangeFields(JNIEnv *env, jobject lpObject, AtkTextRange *lpStruct)
{
	if (!AtkTextRangeFc.cached) cacheAtkTextRangeFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, AtkTextRangeFc.bounds);
	if (lpObject1 != NULL) setAtkTextRectangleFields(env, lpObject1, &lpStruct->bounds);
	}
	(*env)->SetIntField(env, lpObject, AtkTextRangeFc.start_offset, (jint)lpStruct->start_offset);
	(*env)->SetIntField(env, lpObject, AtkTextRangeFc.end_offset, (jint)lpStruct->end_offset);
	(*env)->SetIntLongField(env, lpObject, AtkTextRangeFc.content, (jintLong)lpStruct->content);
}
#endif

#ifndef NO_AtkTextRectangle
typedef struct AtkTextRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} AtkTextRectangle_FID_CACHE;

AtkTextRectangle_FID_CACHE AtkTextRectangleFc;

void cacheAtkTextRectangleFields(JNIEnv *env, jobject lpObject)
{
	if (AtkTextRectangleFc.cached) return;
	AtkTextRectangleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkTextRectangleFc.x = (*env)->GetFieldID(env, AtkTextRectangleFc.clazz, "x", "I");
	AtkTextRectangleFc.y = (*env)->GetFieldID(env, AtkTextRectangleFc.clazz, "y", "I");
	AtkTextRectangleFc.width = (*env)->GetFieldID(env, AtkTextRectangleFc.clazz, "width", "I");
	AtkTextRectangleFc.height = (*env)->GetFieldID(env, AtkTextRectangleFc.clazz, "height", "I");
	AtkTextRectangleFc.cached = 1;
}

AtkTextRectangle *getAtkTextRectangleFields(JNIEnv *env, jobject lpObject, AtkTextRectangle *lpStruct)
{
	if (!AtkTextRectangleFc.cached) cacheAtkTextRectangleFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, AtkTextRectangleFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, AtkTextRectangleFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, AtkTextRectangleFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, AtkTextRectangleFc.height);
	return lpStruct;
}

void setAtkTextRectangleFields(JNIEnv *env, jobject lpObject, AtkTextRectangle *lpStruct)
{
	if (!AtkTextRectangleFc.cached) cacheAtkTextRectangleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AtkTextRectangleFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, AtkTextRectangleFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, AtkTextRectangleFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, AtkTextRectangleFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_AtkValueIface
typedef struct AtkValueIface_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID get_current_value, get_maximum_value, get_minimum_value, set_current_value;
} AtkValueIface_FID_CACHE;

AtkValueIface_FID_CACHE AtkValueIfaceFc;

void cacheAtkValueIfaceFields(JNIEnv *env, jobject lpObject)
{
	if (AtkValueIfaceFc.cached) return;
	AtkValueIfaceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AtkValueIfaceFc.get_current_value = (*env)->GetFieldID(env, AtkValueIfaceFc.clazz, "get_current_value", I_J);
	AtkValueIfaceFc.get_maximum_value = (*env)->GetFieldID(env, AtkValueIfaceFc.clazz, "get_maximum_value", I_J);
	AtkValueIfaceFc.get_minimum_value = (*env)->GetFieldID(env, AtkValueIfaceFc.clazz, "get_minimum_value", I_J);
	AtkValueIfaceFc.set_current_value = (*env)->GetFieldID(env, AtkValueIfaceFc.clazz, "set_current_value", I_J);
	AtkValueIfaceFc.cached = 1;
}

AtkValueIface *getAtkValueIfaceFields(JNIEnv *env, jobject lpObject, AtkValueIface *lpStruct)
{
	if (!AtkValueIfaceFc.cached) cacheAtkValueIfaceFields(env, lpObject);
	lpStruct->get_current_value = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkValueIfaceFc.get_current_value);
	lpStruct->get_maximum_value = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkValueIfaceFc.get_maximum_value);
	lpStruct->get_minimum_value = (void (*)())(*env)->GetIntLongField(env, lpObject, AtkValueIfaceFc.get_minimum_value);
	lpStruct->set_current_value = (gboolean (*)())(*env)->GetIntLongField(env, lpObject, AtkValueIfaceFc.set_current_value);
	return lpStruct;
}

void setAtkValueIfaceFields(JNIEnv *env, jobject lpObject, AtkValueIface *lpStruct)
{
	if (!AtkValueIfaceFc.cached) cacheAtkValueIfaceFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, AtkValueIfaceFc.get_current_value, (jintLong)lpStruct->get_current_value);
	(*env)->SetIntLongField(env, lpObject, AtkValueIfaceFc.get_maximum_value, (jintLong)lpStruct->get_maximum_value);
	(*env)->SetIntLongField(env, lpObject, AtkValueIfaceFc.get_minimum_value, (jintLong)lpStruct->get_minimum_value);
	(*env)->SetIntLongField(env, lpObject, AtkValueIfaceFc.set_current_value, (jintLong)lpStruct->set_current_value);
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
	GtkAccessibleFc.widget = (*env)->GetFieldID(env, GtkAccessibleFc.clazz, "widget", I_J);
	GtkAccessibleFc.cached = 1;
}

GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct)
{
	if (!GtkAccessibleFc.cached) cacheGtkAccessibleFields(env, lpObject);
	lpStruct->widget = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkAccessibleFc.widget);
	return lpStruct;
}

void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct)
{
	if (!GtkAccessibleFc.cached) cacheGtkAccessibleFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkAccessibleFc.widget, (jintLong)lpStruct->widget);
}
#endif

