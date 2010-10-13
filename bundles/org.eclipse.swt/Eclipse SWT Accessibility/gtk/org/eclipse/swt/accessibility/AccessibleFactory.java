/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;


import java.util.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;

class AccessibleFactory {
	static final Hashtable Accessibles = new Hashtable (9);
	static final Hashtable Factories = new Hashtable (9);	
	static final String SWT_TYPE_PREFIX = "SWTAccessible"; //$NON-NLS-1$
	static final String CHILD_TYPENAME = "Child"; //$NON-NLS-1$
	static final String FACTORY_TYPENAME = "SWTFactory"; //$NON-NLS-1$
	static final int[] actionRoles = {
		ACC.ROLE_CHECKBUTTON, ACC.ROLE_COMBOBOX, ACC.ROLE_LINK,
		ACC.ROLE_MENUITEM, ACC.ROLE_PUSHBUTTON, ACC.ROLE_RADIOBUTTON,
		ACC.ROLE_SPLITBUTTON, ACC.ROLE_SPINBUTTON,
		ACC.ROLE_CHECKMENUITEM, ACC.ROLE_RADIOMENUITEM,
	};
	static final int[] editableTextRoles = {
		ACC.ROLE_TEXT, ACC.ROLE_COMBOBOX, ACC.ROLE_PARAGRAPH, ACC.ROLE_DOCUMENT,
	};
	static final int[] hypertextRoles = {
		ACC.ROLE_TEXT, ACC.ROLE_LINK, ACC.ROLE_PARAGRAPH,
	};
	static final int[] selectionRoles = {
		ACC.ROLE_LIST, ACC.ROLE_TABFOLDER, ACC.ROLE_TABLE, ACC.ROLE_TREE,
	};
	static final int[] textRoles = {
		ACC.ROLE_COMBOBOX, ACC.ROLE_LINK, ACC.ROLE_LABEL, ACC.ROLE_TEXT, ACC.ROLE_STATUSBAR,
		ACC.ROLE_PARAGRAPH, ACC.ROLE_DOCUMENT,
	};
	static final int[] tableRoles = {
		ACC.ROLE_TABLE, ACC.ROLE_TREE,
	};
	static final int[] valueRoles = {
		ACC.ROLE_SCROLLBAR, ACC.ROLE_SPINBUTTON, ACC.ROLE_PROGRESSBAR,
	};

	/* Action callbacks */
	static final Callback AtkActionCB_do_action;
	static final Callback AtkActionCB_get_n_actions;	
	static final Callback AtkActionCB_get_description;
	static final Callback AtkActionCB_get_keybinding;
	static final Callback AtkActionCB_get_name;	
	
	/* Component callbacks */
	static final Callback AtkComponentCB_get_extents;
	static final Callback AtkComponentCB_get_position;
	static final Callback AtkComponentCB_get_size;
	static final Callback AtkComponentCB_ref_accessible_at_point;
	
	/* EditableText callbacks */
	static final Callback AtkEditableTextCB_set_run_attributes;
	static final Callback AtkEditableTextCB_set_text_contents;
	static final Callback AtkEditableTextCB_insert_text;
	static final Callback AtkEditableTextCB_copy_text;
	static final Callback AtkEditableTextCB_cut_text;
	static final Callback AtkEditableTextCB_delete_text;
	static final Callback AtkEditableTextCB_paste_text;

	/* Hypertext callbacks */
	static final Callback AtkHypertextCB_get_link;
	static final Callback AtkHypertextCB_get_n_links;
	static final Callback AtkHypertextCB_get_link_index;
	
	/* Object callbacks */
	static final Callback AtkObjectCB_get_description;
	static final Callback AtkObjectCB_get_index_in_parent;
	static final Callback AtkObjectCB_get_n_children;
	static final Callback AtkObjectCB_get_name;
	static final Callback AtkObjectCB_get_parent;	
	static final Callback AtkObjectCB_get_role;
	static final Callback AtkObjectCB_ref_child;
	static final Callback AtkObjectCB_ref_state_set;
	static final Callback AtkObjectCB_get_attributes;
	
	/* Selection callbacks */
	static final Callback AtkSelectionCB_is_child_selected;
	static final Callback AtkSelectionCB_ref_selection;

	/* Table callbacks */
	static final Callback AtkTableCB_ref_at;
	static final Callback AtkTableCB_get_index_at;
	static final Callback AtkTableCB_get_column_at_index;
	static final Callback AtkTableCB_get_row_at_index;
	static final Callback AtkTableCB_get_n_columns;
	static final Callback AtkTableCB_get_n_rows;
	static final Callback AtkTableCB_get_column_extent_at;
	static final Callback AtkTableCB_get_row_extent_at;
	static final Callback AtkTableCB_get_summary;
	static final Callback AtkTableCB_get_caption;
	static final Callback AtkTableCB_get_column_description;
	static final Callback AtkTableCB_get_row_description;
	static final Callback AtkTableCB_get_column_header;
	static final Callback AtkTableCB_get_row_header;
	static final Callback AtkTableCB_get_selected_columns;
	static final Callback AtkTableCB_get_selected_rows;
	static final Callback AtkTableCB_is_column_selected;
	static final Callback AtkTableCB_is_row_selected;
	static final Callback AtkTableCB_is_selected;
	static final Callback AtkTableCB_add_column_selection;
	static final Callback AtkTableCB_add_row_selection;
	static final Callback AtkTableCB_remove_column_selection;
	static final Callback AtkTableCB_remove_row_selection;
	
	/* Text callbacks */
	static final Callback AtkTextCB_get_character_extents;
	static final Callback AtkTextCB_get_range_extents;
	static final Callback AtkTextCB_get_run_attributes;
	static final Callback AtkTextCB_get_offset_at_point;
	static final Callback AtkTextCB_add_selection;
	static final Callback AtkTextCB_remove_selection;
	static final Callback AtkTextCB_set_selection;
	static final Callback AtkTextCB_get_caret_offset;
	static final Callback AtkTextCB_set_caret_offset;
	static final Callback AtkTextCB_get_n_selections;
	static final Callback AtkTextCB_get_selection;
	static final Callback AtkTextCB_get_text;
	static final Callback AtkTextCB_get_text_after_offset;
	static final Callback AtkTextCB_get_text_at_offset;
	static final Callback AtkTextCB_get_text_before_offset;
	static final Callback AtkTextCB_get_character_at_offset;
	static final Callback AtkTextCB_get_character_count;
	static final Callback AtkTextCB_get_bounded_ranges;
	
	/* Value callbacks */
	static final Callback AtkValueCB_get_current_value;
	static final Callback AtkValueCB_get_maximum_value;
	static final Callback AtkValueCB_get_minimum_value;
	static final Callback AtkValueCB_set_current_value;
	
	static final Callback GObjectClass_finalize;
	static final Callback AtkObjectFactoryCB_create_accessible;
	
	/* interface initialization callbacks */
	static final Callback InitActionIfaceCB;		
	static final Callback InitComponentIfaceCB;
	static final Callback InitEditableTextIfaceCB;
	static final Callback InitHypertextIfaceCB;
	static final Callback GTypeInfo_base_init_type;
	static final Callback InitSelectionIfaceCB;
	static final Callback InitTableIfaceCB;
	static final Callback InitTextIfaceCB;
	static final Callback InitValueIfaceCB;
	static final Callback GTypeInfo_base_init_factory;
	/* interface definitions */
	static final int /*long*/ ActionIfaceDefinition;
	static final int /*long*/ ComponentIfaceDefinition;
	static final int /*long*/ EditableTextIfaceDefinition;
	static final int /*long*/ HypertextIfaceDefinition;
	static final int /*long*/ SelectionIfaceDefinition;
	static final int /*long*/ TableIfaceDefinition;
	static final int /*long*/ TextIfaceDefinition;
	static final int /*long*/ ValueIfaceDefinition;
	static {
		AtkActionCB_do_action = newCallback (AccessibleObject.class, "atkAction_do_action", 2); //$NON-NLS-1$
		AtkActionCB_get_n_actions = newCallback (AccessibleObject.class, "atkAction_get_n_actions", 1); //$NON-NLS-1$
		AtkActionCB_get_description = newCallback (AccessibleObject.class, "atkAction_get_description", 2); //$NON-NLS-1$
		AtkActionCB_get_keybinding = newCallback (AccessibleObject.class, "atkAction_get_keybinding", 2); //$NON-NLS-1$
		AtkActionCB_get_name = newCallback (AccessibleObject.class, "atkAction_get_name", 2); //$NON-NLS-1$
		AtkComponentCB_get_extents = newCallback (AccessibleObject.class, "atkComponent_get_extents", 6); //$NON-NLS-1$
		AtkComponentCB_get_position = newCallback (AccessibleObject.class, "atkComponent_get_position", 4); //$NON-NLS-1$
		AtkComponentCB_get_size = newCallback (AccessibleObject.class, "atkComponent_get_size", 4); //$NON-NLS-1$
		AtkComponentCB_ref_accessible_at_point = newCallback (AccessibleObject.class, "atkComponent_ref_accessible_at_point", 4); //$NON-NLS-1$
		AtkEditableTextCB_set_run_attributes = newCallback (AccessibleObject.class, "atkEditableText_set_run_attributes", 4); //$NON-NLS-1$
		AtkEditableTextCB_set_text_contents = newCallback (AccessibleObject.class, "atkEditableText_set_text_contents", 2); //$NON-NLS-1$
		AtkEditableTextCB_insert_text = newCallback (AccessibleObject.class, "atkEditableText_insert_text", 4); //$NON-NLS-1$
		AtkEditableTextCB_copy_text = newCallback (AccessibleObject.class, "atkEditableText_copy_text", 3); //$NON-NLS-1$
		AtkEditableTextCB_cut_text = newCallback (AccessibleObject.class, "atkEditableText_cut_text", 3); //$NON-NLS-1$
		AtkEditableTextCB_delete_text = newCallback (AccessibleObject.class, "atkEditableText_delete_text", 3); //$NON-NLS-1$
		AtkEditableTextCB_paste_text = newCallback (AccessibleObject.class, "atkEditableText_paste_text", 2); //$NON-NLS-1$
		AtkHypertextCB_get_link = newCallback (AccessibleObject.class, "atkHypertext_get_link", 2); //$NON-NLS-1$
		AtkHypertextCB_get_n_links = newCallback (AccessibleObject.class, "atkHypertext_get_n_links", 1); //$NON-NLS-1$
		AtkHypertextCB_get_link_index = newCallback (AccessibleObject.class, "atkHypertext_get_link_index", 2); //$NON-NLS-1$
		AtkObjectCB_get_name = newCallback (AccessibleObject.class, "atkObject_get_name", 1); //$NON-NLS-1$
		AtkObjectCB_get_description = newCallback (AccessibleObject.class, "atkObject_get_description", 1); //$NON-NLS-1$
		AtkObjectCB_get_n_children = newCallback (AccessibleObject.class, "atkObject_get_n_children", 1); //$NON-NLS-1$
		AtkObjectCB_get_role = newCallback (AccessibleObject.class, "atkObject_get_role", 1); //$NON-NLS-1$
		AtkObjectCB_get_parent = newCallback (AccessibleObject.class, "atkObject_get_parent", 1); //$NON-NLS-1$
		AtkObjectCB_ref_state_set = newCallback (AccessibleObject.class, "atkObject_ref_state_set", 1); //$NON-NLS-1$
		AtkObjectCB_get_index_in_parent = newCallback (AccessibleObject.class, "atkObject_get_index_in_parent", 1); //$NON-NLS-1$
		AtkObjectCB_ref_child = newCallback (AccessibleObject.class, "atkObject_ref_child", 2); //$NON-NLS-1$
		AtkObjectCB_get_attributes = newCallback (AccessibleObject.class, "atkObject_get_attributes", 1); //$NON-NLS-1$
		AtkSelectionCB_is_child_selected = newCallback (AccessibleObject.class, "atkSelection_is_child_selected", 2); //$NON-NLS-1$
		AtkSelectionCB_ref_selection = newCallback (AccessibleObject.class, "atkSelection_ref_selection", 2); //$NON-NLS-1$
		AtkTableCB_ref_at = newCallback (AccessibleObject.class, "atkTable_ref_at", 3); //$NON-NLS-1$
		AtkTableCB_get_index_at = newCallback (AccessibleObject.class, "atkTable_get_index_at", 3); //$NON-NLS-1$
		AtkTableCB_get_column_at_index = newCallback (AccessibleObject.class, "atkTable_get_column_at_index", 2); //$NON-NLS-1$
		AtkTableCB_get_row_at_index = newCallback (AccessibleObject.class, "atkTable_get_row_at_index", 2); //$NON-NLS-1$
		AtkTableCB_get_n_columns = newCallback (AccessibleObject.class, "atkTable_get_n_columns", 1); //$NON-NLS-1$
		AtkTableCB_get_n_rows = newCallback (AccessibleObject.class, "atkTable_get_n_rows", 1); //$NON-NLS-1$
		AtkTableCB_get_column_extent_at = newCallback (AccessibleObject.class, "atkTable_get_column_extent_at", 3); //$NON-NLS-1$
		AtkTableCB_get_row_extent_at = newCallback (AccessibleObject.class, "atkTable_get_row_extent_at", 3); //$NON-NLS-1$
		AtkTableCB_get_caption = newCallback (AccessibleObject.class, "atkTable_get_caption", 1); //$NON-NLS-1$
		AtkTableCB_get_summary = newCallback (AccessibleObject.class, "atkTable_get_summary", 1); //$NON-NLS-1$
		AtkTableCB_get_column_description = newCallback (AccessibleObject.class, "atkTable_get_column_description", 2); //$NON-NLS-1$
		AtkTableCB_get_row_description = newCallback (AccessibleObject.class, "atkTable_get_row_description", 2); //$NON-NLS-1$
		AtkTableCB_get_column_header = newCallback (AccessibleObject.class, "atkTable_get_column_header", 2); //$NON-NLS-1$
		AtkTableCB_get_row_header = newCallback (AccessibleObject.class, "atkTable_get_row_header", 2); //$NON-NLS-1$
		AtkTableCB_get_selected_columns = newCallback (AccessibleObject.class, "atkTable_get_selected_columns", 2); //$NON-NLS-1$
		AtkTableCB_get_selected_rows = newCallback (AccessibleObject.class, "atkTable_get_selected_rows", 2); //$NON-NLS-1$
		AtkTableCB_is_column_selected = newCallback (AccessibleObject.class, "atkTable_is_column_selected", 2); //$NON-NLS-1$
		AtkTableCB_is_row_selected = newCallback (AccessibleObject.class, "atkTable_is_row_selected", 2); //$NON-NLS-1$
		AtkTableCB_is_selected = newCallback (AccessibleObject.class, "atkTable_is_selected", 3); //$NON-NLS-1$
		AtkTableCB_add_column_selection = newCallback (AccessibleObject.class, "atkTable_add_column_selection", 2); //$NON-NLS-1$
		AtkTableCB_add_row_selection = newCallback (AccessibleObject.class, "atkTable_add_row_selection", 2); //$NON-NLS-1$
		AtkTableCB_remove_column_selection = newCallback (AccessibleObject.class, "atkTable_remove_column_selection", 2); //$NON-NLS-1$
		AtkTableCB_remove_row_selection = newCallback (AccessibleObject.class, "atkTable_remove_row_selection", 2); //$NON-NLS-1$
		AtkTextCB_get_character_extents = newCallback (AccessibleObject.class, "atkText_get_character_extents", 7); //$NON-NLS-1$
		AtkTextCB_get_range_extents = newCallback (AccessibleObject.class, "atkText_get_range_extents", 5); //$NON-NLS-1$
		AtkTextCB_get_run_attributes = newCallback (AccessibleObject.class, "atkText_get_run_attributes", 4); //$NON-NLS-1$
		AtkTextCB_get_offset_at_point = newCallback (AccessibleObject.class, "atkText_get_offset_at_point", 4); //$NON-NLS-1$
		AtkTextCB_add_selection = newCallback (AccessibleObject.class, "atkText_add_selection", 3); //$NON-NLS-1$
		AtkTextCB_remove_selection = newCallback (AccessibleObject.class, "atkText_remove_selection", 2); //$NON-NLS-1$
		AtkTextCB_set_selection = newCallback (AccessibleObject.class, "atkText_set_selection", 4); //$NON-NLS-1$
		AtkTextCB_get_caret_offset = newCallback (AccessibleObject.class, "atkText_get_caret_offset", 1); //$NON-NLS-1$
		AtkTextCB_set_caret_offset = newCallback (AccessibleObject.class, "atkText_set_caret_offset", 2); //$NON-NLS-1$
		AtkTextCB_get_n_selections = newCallback (AccessibleObject.class, "atkText_get_n_selections", 1); //$NON-NLS-1$
		AtkTextCB_get_selection = newCallback (AccessibleObject.class, "atkText_get_selection", 4); //$NON-NLS-1$
		AtkTextCB_get_text = newCallback (AccessibleObject.class, "atkText_get_text", 3); //$NON-NLS-1$
		AtkTextCB_get_text_after_offset = newCallback (AccessibleObject.class, "atkText_get_text_after_offset", 5); //$NON-NLS-1$
		AtkTextCB_get_text_at_offset = newCallback ( AccessibleObject.class, "atkText_get_text_at_offset", 5); //$NON-NLS-1$
		AtkTextCB_get_text_before_offset = newCallback (AccessibleObject.class, "atkText_get_text_before_offset", 5); //$NON-NLS-1$
		AtkTextCB_get_character_at_offset = newCallback (AccessibleObject.class, "atkText_get_character_at_offset", 2); //$NON-NLS-1$
		AtkTextCB_get_character_count = newCallback (AccessibleObject.class, "atkText_get_character_count", 1); //$NON-NLS-1$
		AtkTextCB_get_bounded_ranges = newCallback (AccessibleObject.class, "atkText_get_bounded_ranges", 5); //$NON-NLS-1$
		AtkValueCB_get_current_value = newCallback (AccessibleObject.class, "atkValue_get_current_value", 2); //$NON-NLS-1$
		AtkValueCB_get_maximum_value = newCallback (AccessibleObject.class, "atkValue_get_maximum_value", 2); //$NON-NLS-1$
		AtkValueCB_get_minimum_value = newCallback (AccessibleObject.class, "atkValue_get_minimum_value", 2); //$NON-NLS-1$
		AtkValueCB_set_current_value = newCallback (AccessibleObject.class, "atkValue_set_current_value", 2); //$NON-NLS-1$
		GObjectClass_finalize = newCallback (AccessibleObject.class, "gObjectClass_finalize", 1); //$NON-NLS-1$
		GTypeInfo_base_init_type = newCallback (AccessibleFactory.class, "gTypeInfo_base_init_type", 1); //$NON-NLS-1$
		GTypeInfo_base_init_factory = newCallback (AccessibleFactory.class, "gTypeInfo_base_init_factory", 1); //$NON-NLS-1$
		AtkObjectFactoryCB_create_accessible = newCallback (AccessibleFactory.class, "atkObjectFactory_create_accessible", 1); //$NON-NLS-1$
		/* Action interface */
		InitActionIfaceCB = newCallback (AccessibleFactory.class, "initActionIfaceCB", 1); //$NON-NLS-1$
		GInterfaceInfo interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitActionIfaceCB.getAddress ();
		ActionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (ActionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Component interface */
		InitComponentIfaceCB = newCallback (AccessibleFactory.class, "initComponentIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitComponentIfaceCB.getAddress ();
		ComponentIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);
		OS.memmove (ComponentIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* EditableText interface */
		InitEditableTextIfaceCB = newCallback (AccessibleFactory.class, "initEditableTextIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitEditableTextIfaceCB.getAddress ();
		EditableTextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (EditableTextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Hypertext interface */
		InitHypertextIfaceCB = newCallback (AccessibleFactory.class, "initHypertextIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitHypertextIfaceCB.getAddress ();
		HypertextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (HypertextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Selection interface */
		InitSelectionIfaceCB = newCallback (AccessibleFactory.class, "initSelectionIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitSelectionIfaceCB.getAddress ();
		SelectionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (SelectionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Text interface */
		InitTableIfaceCB = newCallback (AccessibleFactory.class, "initTableIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitTableIfaceCB.getAddress ();
		TableIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (TableIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Text interface */
		InitTextIfaceCB = newCallback (AccessibleFactory.class, "initTextIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitTextIfaceCB.getAddress ();
		TextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (TextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Value interface */
		InitValueIfaceCB = newCallback (AccessibleFactory.class, "initValueIfaceCB", 1); //$NON-NLS-1$
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitValueIfaceCB.getAddress ();
		ValueIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (ValueIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
	}

	static private Callback newCallback (Object object, String method, int argCount) {
		Callback callback = new Callback (object, method, argCount);
		if (callback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		return callback;
	}

	static String getTypeName (int /*long*/ type) {
		int /*long*/ typeName = OS.g_type_name (type);
		int widgetTypeNameLength = OS.strlen (typeName);
		byte[] buffer = new byte [widgetTypeNameLength];
		OS.memmove (buffer, typeName, widgetTypeNameLength);
		return new String(Converter.mbcsToWcs(null, buffer));
	}
	
	static int /*long*/ getParentType (int /*long*/ widgetType) {
		LONG type = null;
		while (widgetType != 0 && (type = (LONG)Factories.get(new LONG(widgetType))) == null) {
			widgetType = OS.g_type_parent (widgetType);
		}
		if (type == null) return 0;
		return ((LONG)type).value;
	}

	static int /*long*/ atkObjectFactory_create_accessible (int /*long*/ widget) {
		Accessible accessible = (Accessible) Accessibles.get (new LONG (widget));
		if (accessible == null) {
			/*
			* we don't care about this control, so create it with the parent's
			* type so that its accessibility callbacks will not pass though here 
			*/  
			int /*long*/ result = OS.g_object_new (getParentType(OS.G_OBJECT_TYPE (widget)), 0);
			ATK.atk_object_initialize (result, widget);
			return result;
		}
		/* if an atk object has already been created for this widget then just return it */
		if (accessible.accessibleObject != null) {
			return accessible.accessibleObject.handle;
		}
		int /*long*/ widgetType = OS.G_OBJECT_TYPE (widget);
		int /*long*/ parentType = getParentType (widgetType);
		if (parentType == 0) parentType = ATK.GTK_TYPE_ACCESSIBLE();
		int /*long*/ type = getType (getTypeName(widgetType), accessible, parentType, ACC.CHILDID_SELF);
		AccessibleObject object = new AccessibleObject (type, widget, accessible, false);
		accessible.accessibleObject = object;
		accessible.addRelations ();
		return object.handle;
	}
	
	static AccessibleObject createChildAccessible (Accessible accessible, int childId) {
		int /*long*/ childType = getType (CHILD_TYPENAME, accessible, ATK.GTK_TYPE_ACCESSIBLE(), childId);
		return new AccessibleObject(childType, 0, accessible, true);
	}
	
	static void createAccessible (Accessible accessible) {
		int /*long*/ controlHandle = accessible.getControlHandle ();
		OS.gtk_widget_get_accessible(controlHandle);
	}

	static int /*long*/ getType (String widgetTypeName, Accessible accessible, int /*long*/ parentType, int childId) {
		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		event.childID = childId;
		Vector listeners = accessible.accessibleControlListeners;
		for (int i = 0, length = listeners.size(); i < length; i++) {
			AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
			listener.getRole (event);
		}
		boolean action = false, editableText = false, hypertext = false, selection = false, table = false, text = false, value = false;
		if (event.detail != 0) {	/* a role was specified */
			for (int i = 0; i < actionRoles.length; i++) {
				if (event.detail == actionRoles [i]) {
					action = true;
					break;
				}
			}
			for (int i = 0; i < editableTextRoles.length; i++) {
				if (event.detail == editableTextRoles [i]) {
					editableText = true;
					break;
				}
			}
			for (int i = 0; i < hypertextRoles.length; i++) {
				if (event.detail == hypertextRoles [i]) {
					hypertext = true;
					break;
				}
			}
			for (int i = 0; i < selectionRoles.length; i++) {
				if (event.detail == selectionRoles [i]) {
					selection = true;
					break;
				}
			}
			for (int i = 0; i < tableRoles.length; i++) {
				if (event.detail == tableRoles [i]) {
					table = true;
					break;
				}
			}
			for (int i = 0; i < textRoles.length; i++) {
				if (event.detail == textRoles [i]) {
					text = true;
					break;
				}
			}
			for (int i = 0; i < valueRoles.length; i++) {
				if (event.detail == valueRoles [i]) {
					value = true;
					break;
				}
			}
		} else {
			action = editableText = hypertext = selection = table = text = value = true;
		}
		String swtTypeName = SWT_TYPE_PREFIX + widgetTypeName;
		if (action) swtTypeName += "Action"; //$NON-NLS-1$
		if (editableText) swtTypeName += "EditableText"; //$NON-NLS-1$
		if (hypertext) swtTypeName += "Hypertext"; //$NON-NLS-1$
		if (selection) swtTypeName += "Selection"; //$NON-NLS-1$
		if (table) swtTypeName += "Table"; //$NON-NLS-1$
		if (text) swtTypeName += "Text"; //$NON-NLS-1$
		if (value) swtTypeName += "Value"; //$NON-NLS-1$

		byte[] nameBytes = Converter.wcsToMbcs(null, swtTypeName, true);
		int /*long*/ type = OS.g_type_from_name(nameBytes);
		if (type == 0) {
			if (AccessibleObject.DEBUG) AccessibleObject.print("-->New Type=" + swtTypeName); //$NON-NLS-1$
			/* define the type */
			int /*long*/ queryPtr = OS.g_malloc (GTypeQuery.sizeof);
			OS.g_type_query (parentType, queryPtr);
			GTypeQuery query = new GTypeQuery ();
			OS.memmove (query, queryPtr, GTypeQuery.sizeof);
			OS.g_free (queryPtr);
			GTypeInfo typeInfo = new GTypeInfo ();
			typeInfo.base_init = GTypeInfo_base_init_type.getAddress ();
			typeInfo.class_size = (short) query.class_size;
			typeInfo.instance_size = (short) query.instance_size;
			int /*long*/ definition = OS.g_malloc (GTypeInfo.sizeof); 
			OS.memmove (definition, typeInfo, GTypeInfo.sizeof);
			type = OS.g_type_register_static (parentType, nameBytes, definition, 0);
			OS.g_type_add_interface_static (type, ATK.ATK_TYPE_COMPONENT(), ComponentIfaceDefinition);
			if (action) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_ACTION(), ActionIfaceDefinition);
			if (editableText) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_EDITABLE_TEXT(), EditableTextIfaceDefinition);
			if (hypertext) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_HYPERTEXT(), HypertextIfaceDefinition);
			if (selection) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_SELECTION(), SelectionIfaceDefinition);
			if (table) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_TABLE(), TableIfaceDefinition);
			if (text) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_TEXT(), TextIfaceDefinition);
			if (value) OS.g_type_add_interface_static (type, ATK.ATK_TYPE_VALUE(), ValueIfaceDefinition);
		}
		return type;
	}

	static int /*long*/ gTypeInfo_base_init_factory (int /*long*/ klass) {
		AtkObjectFactoryClass objectClass = new AtkObjectFactoryClass ();
		ATK.memmove (objectClass, klass);
		objectClass.create_accessible = AtkObjectFactoryCB_create_accessible.getAddress ();
		ATK.memmove (klass, objectClass); 
		return 0;
	}
	
	static int /*long*/ gTypeInfo_base_init_type (int /*long*/ klass) {
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, klass);
		objectClass.get_name = AtkObjectCB_get_name.getAddress ();
		objectClass.get_description = AtkObjectCB_get_description.getAddress ();
		objectClass.get_n_children = AtkObjectCB_get_n_children.getAddress ();
		objectClass.get_role = AtkObjectCB_get_role.getAddress ();
		objectClass.get_parent = AtkObjectCB_get_parent.getAddress ();
		objectClass.ref_state_set = AtkObjectCB_ref_state_set.getAddress ();
		objectClass.get_index_in_parent = AtkObjectCB_get_index_in_parent.getAddress ();
		objectClass.ref_child = AtkObjectCB_ref_child.getAddress ();
		objectClass.get_attributes = AtkObjectCB_get_attributes.getAddress ();
		int /*long*/ gObjectClass = OS.G_OBJECT_CLASS (klass);
		GObjectClass objectClassStruct = new GObjectClass ();
		OS.memmove (objectClassStruct, gObjectClass);
		objectClassStruct.finalize = GObjectClass_finalize.getAddress ();
		OS.memmove (gObjectClass, objectClassStruct); 
		ATK.memmove (klass, objectClass);
		return 0;
	}
	
	static int /*long*/ initActionIfaceCB (int /*long*/ iface) {
		AtkActionIface inter = new AtkActionIface ();
		ATK.memmove (inter, iface);
		inter.do_action = AtkActionCB_do_action.getAddress (); 
		inter.get_n_actions = AtkActionCB_get_n_actions.getAddress ();
		inter.get_description = AtkActionCB_get_description.getAddress ();
		inter.get_keybinding = AtkActionCB_get_keybinding.getAddress (); 
		inter.get_name = AtkActionCB_get_name.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}
	
	static int /*long*/ initComponentIfaceCB (int /*long*/ iface) {
		AtkComponentIface inter = new AtkComponentIface ();
		ATK.memmove (inter, iface);
		inter.get_extents = AtkComponentCB_get_extents.getAddress ();
		inter.get_position = AtkComponentCB_get_position.getAddress ();
		inter.get_size = AtkComponentCB_get_size.getAddress ();
		inter.ref_accessible_at_point = AtkComponentCB_ref_accessible_at_point.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initEditableTextIfaceCB (int /*long*/ iface) {
		AtkEditableTextIface inter = new AtkEditableTextIface ();
		ATK.memmove (inter, iface);
		inter.set_run_attributes = AtkEditableTextCB_set_run_attributes.getAddress ();
		inter.set_text_contents = AtkEditableTextCB_set_text_contents.getAddress ();
		inter.insert_text = AtkEditableTextCB_insert_text.getAddress ();
		inter.copy_text = AtkEditableTextCB_copy_text.getAddress ();
		inter.cut_text = AtkEditableTextCB_cut_text.getAddress ();
		inter.delete_text = AtkEditableTextCB_delete_text.getAddress ();
		inter.paste_text = AtkEditableTextCB_paste_text.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initHypertextIfaceCB (int /*long*/ iface) {
		AtkHypertextIface inter = new AtkHypertextIface ();
		ATK.memmove (inter, iface);
		inter.get_link = AtkHypertextCB_get_link.getAddress (); 
		inter.get_link_index = AtkHypertextCB_get_link_index.getAddress ();
		inter.get_n_links = AtkHypertextCB_get_n_links.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initSelectionIfaceCB (int /*long*/ iface) {
		AtkSelectionIface inter = new AtkSelectionIface ();
		ATK.memmove (inter, iface);
		inter.is_child_selected = AtkSelectionCB_is_child_selected.getAddress ();
		inter.ref_selection = AtkSelectionCB_ref_selection.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initTableIfaceCB (int /*long*/ iface) {
		AtkTableIface inter = new AtkTableIface ();
		ATK.memmove (inter, iface);
		inter.ref_at = AtkTableCB_ref_at.getAddress();
		inter.get_index_at = AtkTableCB_get_index_at.getAddress();
		inter.get_column_at_index = AtkTableCB_get_column_at_index.getAddress();
		inter.get_row_at_index = AtkTableCB_get_row_at_index.getAddress();
		inter.get_n_columns = AtkTableCB_get_n_columns.getAddress();
		inter.get_n_rows = AtkTableCB_get_n_rows.getAddress();
		inter.get_column_extent_at = AtkTableCB_get_column_extent_at.getAddress();
		inter.get_row_extent_at = AtkTableCB_get_row_extent_at.getAddress();
		inter.get_caption = AtkTableCB_get_caption.getAddress();
		inter.get_summary = AtkTableCB_get_summary.getAddress();
		inter.get_column_description = AtkTableCB_get_column_description.getAddress();
		inter.get_row_description = AtkTableCB_get_row_description.getAddress();
		inter.get_column_header = AtkTableCB_get_column_header.getAddress();
		inter.get_row_header = AtkTableCB_get_row_header.getAddress();
		inter.get_selected_columns = AtkTableCB_get_selected_columns.getAddress();
		inter.get_selected_rows = AtkTableCB_get_selected_rows.getAddress();
		inter.is_column_selected = AtkTableCB_is_column_selected.getAddress();
		inter.is_row_selected = AtkTableCB_is_row_selected.getAddress();
		inter.is_selected = AtkTableCB_is_selected.getAddress();
		inter.add_column_selection = AtkTableCB_add_column_selection.getAddress();
		inter.add_row_selection = AtkTableCB_add_row_selection.getAddress();
		inter.remove_column_selection = AtkTableCB_remove_column_selection.getAddress();
		inter.remove_row_selection = AtkTableCB_remove_row_selection.getAddress();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initTextIfaceCB (int /*long*/ iface) {
		AtkTextIface inter = new AtkTextIface ();
		ATK.memmove (inter, iface);
		inter.get_range_extents = AtkTextCB_get_range_extents.getAddress ();
		inter.get_character_extents = AtkTextCB_get_character_extents.getAddress ();
		inter.get_run_attributes= AtkTextCB_get_run_attributes.getAddress ();
		inter.get_offset_at_point = AtkTextCB_get_offset_at_point.getAddress ();
		inter.add_selection = AtkTextCB_add_selection.getAddress ();
		inter.remove_selection = AtkTextCB_remove_selection.getAddress ();
		inter.set_selection = AtkTextCB_set_selection.getAddress ();
		inter.get_caret_offset = AtkTextCB_get_caret_offset.getAddress ();
		inter.set_caret_offset = AtkTextCB_set_caret_offset.getAddress ();
		inter.get_character_at_offset = AtkTextCB_get_character_at_offset.getAddress ();
		inter.get_character_count = AtkTextCB_get_character_count.getAddress ();
		inter.get_n_selections = AtkTextCB_get_n_selections.getAddress ();
		inter.get_selection = AtkTextCB_get_selection.getAddress ();
		inter.get_text = AtkTextCB_get_text.getAddress ();
		inter.get_text_after_offset = AtkTextCB_get_text_after_offset.getAddress ();
		inter.get_text_at_offset = AtkTextCB_get_text_at_offset.getAddress ();
		inter.get_text_before_offset = AtkTextCB_get_text_before_offset.getAddress ();
		inter.get_bounded_ranges = AtkTextCB_get_bounded_ranges.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static int /*long*/ initValueIfaceCB (int /*long*/ iface) {
		AtkValueIface inter = new AtkValueIface ();
		ATK.memmove (inter, iface);
		inter.get_current_value = AtkValueCB_get_current_value.getAddress ();
		inter.get_maximum_value = AtkValueCB_get_maximum_value.getAddress ();
		inter.get_minimum_value = AtkValueCB_get_minimum_value.getAddress ();
		inter.set_current_value = AtkValueCB_set_current_value.getAddress ();
		ATK.memmove (iface, inter);
		return 0;
	}

	static void registerAccessible (Accessible accessible) {
		int /*long*/ widget = accessible.getControlHandle ();
		int /*long*/ widgetType = OS.G_OBJECT_TYPE (widget);
		int /*long*/ registry = ATK.atk_get_default_registry ();
		int /*long*/ factory = ATK.atk_registry_get_factory (registry, widgetType);
		/* If NO_OP factory is registered then OS accessibility is not active */
		if (ATK.ATK_IS_NO_OP_OBJECT_FACTORY(factory)) return;
		String name = FACTORY_TYPENAME + getTypeName(widgetType);
		byte[] factoryName = Converter.wcsToMbcs(null, name, true);
		if (OS.g_type_from_name (factoryName) == 0) {
			if (AccessibleObject.DEBUG) AccessibleObject.print("-->New Factory=" + name); //$NON-NLS-1$
			/* register the factory */
			GTypeInfo typeInfo = new GTypeInfo ();
			typeInfo.base_init = GTypeInfo_base_init_factory.getAddress ();
			typeInfo.class_size = (short)ATK.AtkObjectFactoryClass_sizeof ();
			typeInfo.instance_size = (short)ATK.AtkObjectFactory_sizeof ();
			int /*long*/ info = OS.g_malloc (GTypeInfo.sizeof); 
			OS.memmove (info, typeInfo, GTypeInfo.sizeof); 
			int /*long*/ swtFactoryType = OS.g_type_register_static (ATK.ATK_TYPE_OBJECT_FACTORY(), factoryName, info, 0);
			int /*long*/ parentType = ATK.atk_object_factory_get_accessible_type(factory);
			ATK.atk_registry_set_factory_type (registry, widgetType, swtFactoryType);
			Factories.put (new LONG (widgetType), new LONG (parentType));
		}
		if (AccessibleObject.DEBUG) AccessibleObject.print("-->Register=" + accessible.control + " " + widget); //$NON-NLS-1$
		Accessibles.put (new LONG (widget), accessible);
	}
	
	static void unregisterAccessible (Accessible accessible) {
		int /*long*/ widget = accessible.getControlHandle ();
		Accessibles.remove (new LONG (widget));
		if (AccessibleObject.DEBUG) AccessibleObject.print("-->Deregister=" + accessible.control + " " + widget); //$NON-NLS-1$
	}
}
