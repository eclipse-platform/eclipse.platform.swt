/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;


import java.util.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;

class AccessibleFactory {
	int /*long*/ handle;
	int /*long*/ objectParentType;
	int /*long*/ widgetTypeName;
	Callback atkObjectFactoryCB_create_accessible;
	Callback gTypeInfo_base_init_factory;
	Hashtable accessibles = new Hashtable (9);
	
	static final Hashtable Types = new Hashtable (9);
	static final Hashtable Factories = new Hashtable (9);	
	static final int /*long*/ DefaultParentType = ATK.g_type_from_name (Converter.wcsToMbcs (null, "GtkAccessible", true));
	static final byte[] FACTORY_PARENTTYPENAME = Converter.wcsToMbcs (null, "AtkObjectFactory", true);
	static final byte[] SWT_TYPE_PREFIX = Converter.wcsToMbcs (null, "SWT", false);
	static final byte[] CHILD_TYPENAME = Converter.wcsToMbcs (null, "Child", false);
	static final byte[] FACTORY_TYPENAME = Converter.wcsToMbcs (null, "SWTFactory", true);
	static final int[] actionRoles = {
		ACC.ROLE_CHECKBUTTON, ACC.ROLE_COMBOBOX, ACC.ROLE_LINK,
		ACC.ROLE_MENUITEM, ACC.ROLE_PUSHBUTTON, ACC.ROLE_RADIOBUTTON,
	};
	static final int[] hypertextRoles = {ACC.ROLE_LINK};
	static final int[] selectionRoles = {
		ACC.ROLE_LIST, ACC.ROLE_TABFOLDER, ACC.ROLE_TABLE, ACC.ROLE_TREE,
	};
	static final int[] textRoles = {
		ACC.ROLE_COMBOBOX, ACC.ROLE_LINK, ACC.ROLE_LABEL, ACC.ROLE_TEXT,
	};

	/* AT callbacks*/
	static final Callback AtkActionCB_get_keybinding;
	static final Callback AtkActionCB_get_name;	
	static final Callback AtkComponentCB_get_extents;
	static final Callback AtkComponentCB_get_position;
	static final Callback AtkComponentCB_get_size;
	static final Callback AtkComponentCB_ref_accessible_at_point;
	static final Callback AtkHypertextCB_get_link;
	static final Callback AtkHypertextCB_get_n_links;
	static final Callback AtkHypertextCB_get_link_index;
	static final Callback AtkObjectCB_get_description;
	static final Callback AtkObjectCB_get_index_in_parent;
	static final Callback AtkObjectCB_get_n_children;
	static final Callback AtkObjectCB_get_name;
	static final Callback AtkObjectCB_get_parent;	
	static final Callback AtkObjectCB_get_role;
	static final Callback AtkObjectCB_ref_child;
	static final Callback AtkObjectCB_ref_state_set;
	static final Callback AtkSelectionCB_is_child_selected;
	static final Callback AtkSelectionCB_ref_selection;
	static final Callback AtkTextCB_get_caret_offset;
	static final Callback AtkTextCB_get_n_selections;
	static final Callback AtkTextCB_get_selection;
	static final Callback AtkTextCB_get_text;
	static final Callback AtkTextCB_get_text_after_offset;
	static final Callback AtkTextCB_get_text_at_offset;
	static final Callback AtkTextCB_get_text_before_offset;
	static final Callback AtkTextCB_get_character_at_offset;
	static final Callback AtkTextCB_get_character_count;
	static final Callback GObjectClass_finalize;
	/* interface initialization callbacks */
	static final Callback InitActionIfaceCB;		
	static final Callback InitComponentIfaceCB;
	static final Callback InitHypertextIfaceCB;
	static final Callback GTypeInfo_base_init_type;
	static final Callback InitSelectionIfaceCB;
	static final Callback InitTextIfaceCB;
	/* interface definitions */
	static int /*long*/ ObjectIfaceDefinition;
	static final int /*long*/ ActionIfaceDefinition;
	static final int /*long*/ ComponentIfaceDefinition;
	static final int /*long*/ HypertextIfaceDefinition;
	static final int /*long*/ SelectionIfaceDefinition;
	static final int /*long*/ TextIfaceDefinition;
	static {
		AtkActionCB_get_keybinding = new Callback (AccessibleObject.class, "atkAction_get_keybinding", 2);
		AtkActionCB_get_name = new Callback (AccessibleObject.class, "atkAction_get_name", 2);
		AtkComponentCB_get_extents = new Callback (AccessibleObject.class, "atkComponent_get_extents", 6);
		AtkComponentCB_get_position = new Callback (AccessibleObject.class, "atkComponent_get_position", 4);
		AtkComponentCB_get_size = new Callback (AccessibleObject.class, "atkComponent_get_size", 4);
		AtkComponentCB_ref_accessible_at_point = new Callback (AccessibleObject.class, "atkComponent_ref_accessible_at_point", 4);
		AtkHypertextCB_get_link = new Callback (AccessibleObject.class, "atkHypertext_get_link", 2);
		AtkHypertextCB_get_n_links = new Callback (AccessibleObject.class, "atkHypertext_get_n_links", 1);
		AtkHypertextCB_get_link_index = new Callback (AccessibleObject.class, "atkHypertext_get_link_index", 2);
		AtkObjectCB_get_name = new Callback (AccessibleObject.class, "atkObject_get_name", 1);
		AtkObjectCB_get_description = new Callback (AccessibleObject.class, "atkObject_get_description", 1);
		AtkObjectCB_get_n_children = new Callback (AccessibleObject.class, "atkObject_get_n_children", 1);
		AtkObjectCB_get_role = new Callback (AccessibleObject.class, "atkObject_get_role", 1);
		AtkObjectCB_get_parent = new Callback (AccessibleObject.class, "atkObject_get_parent", 1);
		AtkObjectCB_ref_state_set = new Callback (AccessibleObject.class, "atkObject_ref_state_set", 1);
		AtkObjectCB_get_index_in_parent = new Callback (AccessibleObject.class, "atkObject_get_index_in_parent", 1);
		AtkObjectCB_ref_child = new Callback (AccessibleObject.class, "atkObject_ref_child", 2);
		AtkSelectionCB_is_child_selected = new Callback (AccessibleObject.class, "atkSelection_is_child_selected", 2);
		AtkSelectionCB_ref_selection = new Callback (AccessibleObject.class, "atkSelection_ref_selection", 2);
		AtkTextCB_get_caret_offset = new Callback (AccessibleObject.class, "atkText_get_caret_offset", 1);
		AtkTextCB_get_n_selections = new Callback (AccessibleObject.class, "atkText_get_n_selections", 1);
		AtkTextCB_get_selection = new Callback (AccessibleObject.class, "atkText_get_selection", 4);
		AtkTextCB_get_text = new Callback (AccessibleObject.class, "atkText_get_text", 3);
		AtkTextCB_get_text_after_offset = new Callback (AccessibleObject.class, "atkText_get_text_after_offset", 5);
		AtkTextCB_get_text_at_offset = new Callback (AccessibleObject.class, "atkText_get_text_at_offset", 5);
		AtkTextCB_get_text_before_offset = new Callback (AccessibleObject.class, "atkText_get_text_before_offset", 5);
		AtkTextCB_get_character_at_offset = new Callback (AccessibleObject.class, "atkText_get_character_at_offset", 2);
		AtkTextCB_get_character_count = new Callback (AccessibleObject.class, "atkText_get_character_count", 1);
		GObjectClass_finalize = new Callback (AccessibleObject.class, "gObjectClass_finalize", 1);
		GTypeInfo_base_init_type = new Callback (AccessibleFactory.class, "gTypeInfo_base_init_type", 1);
		/* Action interface */
		InitActionIfaceCB = new Callback (AccessibleFactory.class, "initActionIfaceCB", 1);
		GInterfaceInfo interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitActionIfaceCB.getAddress ();
		ActionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (ActionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Component interface */
		InitComponentIfaceCB = new Callback (AccessibleFactory.class, "initComponentIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitComponentIfaceCB.getAddress ();
		ComponentIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);
		ATK.memmove (ComponentIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Hypertext interface */
		InitHypertextIfaceCB = new Callback (AccessibleFactory.class, "initHypertextIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitHypertextIfaceCB.getAddress ();
		HypertextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (HypertextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Selection interface */
		InitSelectionIfaceCB = new Callback (AccessibleFactory.class, "initSelectionIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitSelectionIfaceCB.getAddress ();
		SelectionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (SelectionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		/* Text interface */
		InitTextIfaceCB = new Callback (AccessibleFactory.class, "initTextIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitTextIfaceCB.getAddress ();
		TextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (TextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
	}

	private AccessibleFactory (int /*long*/ widgetType) {
		super ();
		/* If DefaultParentType is 0 then OS accessibility is not active */
		if (DefaultParentType == 0) return;
		widgetTypeName = ATK.g_type_name (widgetType);
		int widgetTypeNameLength = OS.strlen (widgetTypeName) + 1;
		byte[] buffer = new byte [widgetTypeNameLength];
		OS.memmove (buffer, widgetTypeName, widgetTypeNameLength);
		byte[] factoryName = new byte [FACTORY_TYPENAME.length + widgetTypeNameLength];
		System.arraycopy (FACTORY_TYPENAME, 0, factoryName, 0, FACTORY_TYPENAME.length);
		System.arraycopy (buffer, 0, factoryName, FACTORY_TYPENAME.length, widgetTypeNameLength);
		if (ATK.g_type_from_name (factoryName) == 0) {
			/* register the factory */
			int /*long*/ registry = ATK.atk_get_default_registry ();
			int /*long*/ previousFactory = ATK.atk_registry_get_factory (registry, widgetType);
			objectParentType = ATK.atk_object_factory_get_accessible_type (previousFactory);
			if (objectParentType == 0) objectParentType = DefaultParentType;
			int /*long*/ factoryParentType = ATK.g_type_from_name (FACTORY_PARENTTYPENAME);
			gTypeInfo_base_init_factory  = new Callback (this, "gTypeInfo_base_init_factory", 1);
			GTypeInfo typeInfo = new GTypeInfo ();
			typeInfo.base_init = gTypeInfo_base_init_factory.getAddress ();
			typeInfo.class_size = (short)ATK.AtkObjectFactoryClass_sizeof ();
			typeInfo.instance_size = (short)ATK.AtkObjectFactory_sizeof ();
			handle = OS.g_malloc (GTypeInfo.sizeof); 
			ATK.memmove (handle, typeInfo, GTypeInfo.sizeof); 
			int /*long*/ swtFactory = ATK.g_type_register_static (factoryParentType, factoryName, handle, 0);
			ATK.atk_registry_set_factory_type (registry, widgetType, swtFactory);
		}
	}
	
	void addAccessible (Accessible accessible) {
		int /*long*/ controlHandle = accessible.getControlHandle ();
		accessibles.put (new LONG (controlHandle), accessible);
	}

	int /*long*/ atkObjectFactory_create_accessible (int /*long*/ widget) {
		Accessible accessible = (Accessible) accessibles.get (new LONG (widget));
		if (accessible == null) {
			/*
			* we don't care about this control, so create it with the parent's
			* type so that its accessibility callbacks will not pass though here 
			*/  
			int /*long*/ result = ATK.g_object_new (objectParentType, 0);
			ATK.atk_object_initialize (result, widget);
			return result;
		}
		int typeNameLength = OS.strlen (widgetTypeName);
		byte[] buffer = new byte [typeNameLength];
		OS.memmove (buffer, widgetTypeName, typeNameLength);
		int /*long*/ type = getType (buffer, accessible, objectParentType, ACC.CHILDID_SELF);
		AccessibleObject object = new AccessibleObject (type, widget, accessible, objectParentType, false);
		accessible.accessibleObject = object;
		return object.handle;
	}
	
	static int /*long*/ getChildType (Accessible accessible, int childIndex) {
		return getType (CHILD_TYPENAME, accessible, DefaultParentType, childIndex);
	}

	static int /*long*/ getDefaultParentType () {
		return DefaultParentType;
	}

	static int /*long*/ getType (byte[] widgetTypeName, Accessible accessible, int /*long*/ parentType, int childId) {
		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		event.childID = childId;
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getRole (event);
		}
		boolean action = false, hypertext = false, selection = false, text = false;
		if (event.detail != 0) {	/* a role was specified */
			for (int i = 0; i < actionRoles.length; i++) {
				if (event.detail == actionRoles [i]) {
					action = true;
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
			for (int i = 0; i < textRoles.length; i++) {
				if (event.detail == textRoles [i]) {
					text = true;
					break;
				}
			}
		} else {
			action = hypertext = selection = text = true;
		}
		String swtTypeName = new String (SWT_TYPE_PREFIX);
		swtTypeName += new String (widgetTypeName);
		if (action) swtTypeName += "Action";
		if (hypertext) swtTypeName += "Hypertext";
		if (selection) swtTypeName += "Selection";
		if (text) swtTypeName += "Text";

		int /*long*/ type = 0;
		LONG typeInt = (LONG)Types.get (swtTypeName);
		if (typeInt != null) {
			type = typeInt.value;
		} else {
			/* define the type */
			int /*long*/ queryPtr = OS.g_malloc (GTypeQuery.sizeof);
			ATK.g_type_query (parentType, queryPtr);
			GTypeQuery query = new GTypeQuery ();
			ATK.memmove (query, queryPtr, GTypeQuery.sizeof);
			OS.g_free (queryPtr);
			GTypeInfo typeInfo = new GTypeInfo ();
			typeInfo.base_init = GTypeInfo_base_init_type.getAddress ();
			typeInfo.class_size = (short) query.class_size;
			typeInfo.instance_size = (short) query.instance_size;
			ObjectIfaceDefinition = OS.g_malloc (GTypeInfo.sizeof); 
			ATK.memmove (ObjectIfaceDefinition, typeInfo, GTypeInfo.sizeof);
			byte[] nameBytes = new byte [swtTypeName.length () + 1];
			System.arraycopy(swtTypeName.getBytes (), 0, nameBytes, 0, swtTypeName.length ()); 
			type = ATK.g_type_register_static (parentType, nameBytes, ObjectIfaceDefinition, 0);
			ATK.g_type_add_interface_static (type, AccessibleObject.ATK_COMPONENT_TYPE, ComponentIfaceDefinition);
			if (action) ATK.g_type_add_interface_static (type, AccessibleObject.ATK_ACTION_TYPE, ActionIfaceDefinition);
			if (hypertext) ATK.g_type_add_interface_static (type, AccessibleObject.ATK_HYPERTEXT_TYPE, HypertextIfaceDefinition);
			if (selection) ATK.g_type_add_interface_static (type, AccessibleObject.ATK_SELECTION_TYPE, SelectionIfaceDefinition);
			if (text) ATK.g_type_add_interface_static (type, AccessibleObject.ATK_TEXT_TYPE, TextIfaceDefinition);
			Types.put (swtTypeName, new LONG (type));
		}
		return type;
	}

	int /*long*/ gTypeInfo_base_init_factory (int /*long*/ klass) {
		int /*long*/ atkObjectFactoryClass = ATK.ATK_OBJECT_FACTORY_CLASS (klass);
		AtkObjectFactoryClass objectFactoryClassStruct = new AtkObjectFactoryClass ();
		ATK.memmove (objectFactoryClassStruct, atkObjectFactoryClass);
		atkObjectFactoryCB_create_accessible = new Callback (this, "atkObjectFactory_create_accessible", 1);
		objectFactoryClassStruct.create_accessible = atkObjectFactoryCB_create_accessible.getAddress ();
		ATK.memmove (atkObjectFactoryClass, objectFactoryClassStruct); 
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
		int /*long*/ gObjectClass = ATK.G_OBJECT_CLASS (klass);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		objectClassStruct.finalize = GObjectClass_finalize.getAddress ();
		ATK.memmove (gObjectClass, objectClassStruct); 
		ATK.memmove (klass, objectClass);
		return 0;
	}
	
	static int /*long*/ initActionIfaceCB (int /*long*/ iface) {
		AtkActionIface actionIface = new AtkActionIface ();
		ATK.memmove (actionIface, iface);
		actionIface.get_keybinding = AtkActionCB_get_keybinding.getAddress (); 
		actionIface.get_name = AtkActionCB_get_name.getAddress ();
		ATK.memmove (iface, actionIface);
		return 0;
	}
	
	static int /*long*/ initComponentIfaceCB (int /*long*/ iface) {
		AtkComponentIface componentIface = new AtkComponentIface ();
		ATK.memmove (componentIface, iface);
		componentIface.get_extents = AtkComponentCB_get_extents.getAddress ();
		componentIface.get_position = AtkComponentCB_get_position.getAddress ();
		componentIface.get_size = AtkComponentCB_get_size.getAddress ();
		componentIface.ref_accessible_at_point = AtkComponentCB_ref_accessible_at_point.getAddress ();
		ATK.memmove (iface, componentIface);
		return 0;
	}

	static int /*long*/ initHypertextIfaceCB (int /*long*/ iface) {
		AtkHypertextIface hypertextIface = new AtkHypertextIface ();
		ATK.memmove (hypertextIface, iface);
		hypertextIface.get_link = AtkHypertextCB_get_link.getAddress (); 
		hypertextIface.get_link_index = AtkHypertextCB_get_link_index.getAddress ();
		hypertextIface.get_n_links = AtkHypertextCB_get_n_links.getAddress ();
		ATK.memmove (iface, hypertextIface);
		return 0;
	}

	static int /*long*/ initSelectionIfaceCB (int /*long*/ iface) {
		AtkSelectionIface selectionIface = new AtkSelectionIface ();
		ATK.memmove (selectionIface, iface);
		selectionIface.is_child_selected = AtkSelectionCB_is_child_selected.getAddress ();
		selectionIface.ref_selection = AtkSelectionCB_ref_selection.getAddress ();
		ATK.memmove (iface, selectionIface);
		return 0;
	}

	static int /*long*/ initTextIfaceCB (int /*long*/ iface) {
		AtkTextIface textInterface = new AtkTextIface ();
		ATK.memmove (textInterface, iface);
		textInterface.get_caret_offset = AtkTextCB_get_caret_offset.getAddress ();
		textInterface.get_character_at_offset = AtkTextCB_get_character_at_offset.getAddress ();
		textInterface.get_character_count = AtkTextCB_get_character_count.getAddress ();
		textInterface.get_n_selections = AtkTextCB_get_n_selections.getAddress ();
		textInterface.get_selection = AtkTextCB_get_selection.getAddress ();
		textInterface.get_text = AtkTextCB_get_text.getAddress ();
		textInterface.get_text_after_offset = AtkTextCB_get_text_after_offset.getAddress ();
		textInterface.get_text_at_offset = AtkTextCB_get_text_at_offset.getAddress ();
		textInterface.get_text_before_offset = AtkTextCB_get_text_before_offset.getAddress ();
		ATK.memmove (iface, textInterface);
		return 0;
	}

	static void registerAccessible (Accessible accessible) {
		int /*long*/ controlHandle = accessible.getControlHandle ();
		int /*long*/ widgetType = ATK.G_OBJECT_TYPE (controlHandle);
		AccessibleFactory factory = (AccessibleFactory) Factories.get (new LONG (widgetType));
		if (factory == null) {
			factory = new AccessibleFactory (widgetType);
			Factories.put (new LONG (widgetType), factory);
		}
		factory.addAccessible (accessible);
	}
	
	void removeAccessible (Accessible accessible) {
		accessibles.remove (new LONG (accessible.getControlHandle ()));
	}
	
	static void unregisterAccessible (Accessible accessible) {
		int /*long*/ controlHandle = accessible.getControlHandle ();
		int /*long*/ widgetType = ATK.G_OBJECT_TYPE (controlHandle);
		AccessibleFactory factory = (AccessibleFactory) Factories.get (new LONG (widgetType));
		if (factory != null) {
			factory.removeAccessible (accessible);
		}
	}
}
