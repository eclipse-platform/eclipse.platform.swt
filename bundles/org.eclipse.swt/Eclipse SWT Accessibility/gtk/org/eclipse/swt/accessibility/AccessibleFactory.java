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
package org.eclipse.swt.accessibility;


import java.util.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;

class AccessibleFactory {
	int handle, objectType, objectParentType;
	Callback atkObjectFactoryCB_create_accessible;
	Callback gTypeInfo_base_init_factory;
	Hashtable accessibles = new Hashtable (9);

	static final Hashtable Factories = new Hashtable (9);	
	static int DefaultChildType;
	static final int DefaultParentType = ATK.g_type_from_name (Converter.wcsToMbcs (null, "GtkAccessible", true));
	static final byte[] DEFAULT_CHILDTYPENAME = Converter.wcsToMbcs (null, "SWTChild", true);
	static final byte[] FACTORY_PARENTTYPENAME = Converter.wcsToMbcs (null, "AtkObjectFactory", true);
	static final byte[] SWT_TYPE_PREFIX = Converter.wcsToMbcs (null, "SWT", true);
	static final byte[] FACTORY_TYPENAME = Converter.wcsToMbcs (null, "SWTFactory", true);
	// AT callbacks
	static final Callback AtkActionCB_get_keybinding;
	static final Callback AtkActionCB_get_name;	
	static final Callback AtkComponentCB_get_extents;
	static final Callback AtkComponentCB_get_position;
	static final Callback AtkComponentCB_get_size;
	static final Callback AtkComponentCB_ref_accessible_at_point;
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
	// interface initialization callbacks
	static final Callback InitActionIfaceCB;		
	static final Callback InitComponentIfaceCB;		
	static final Callback GTypeInfo_base_init_type;
	static final Callback InitSelectionIfaceCB;
	static final Callback InitTextIfaceCB;
	// interface definitions
	static int ObjectIfaceDefinition;
	static final int ActionIfaceDefinition;
	static final int ComponentIfaceDefinition;
	static final int SelectionIfaceDefinition;
	static final int TextIfaceDefinition;
	static {
		AtkActionCB_get_keybinding = new Callback (AccessibleObject.class, "atkAction_get_keybinding", 2);
		AtkActionCB_get_name = new Callback (AccessibleObject.class, "atkAction_get_name", 2);
		AtkComponentCB_get_extents = new Callback (AccessibleObject.class, "atkComponent_get_extents", 6);
		AtkComponentCB_get_position = new Callback (AccessibleObject.class, "atkComponent_get_position", 4);
		AtkComponentCB_get_size = new Callback (AccessibleObject.class, "atkComponent_get_size", 4);
		AtkComponentCB_ref_accessible_at_point = new Callback (AccessibleObject.class, "atkComponent_ref_accessible_at_point", 4);
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
		// Action interface
		InitActionIfaceCB = new Callback (AccessibleFactory.class, "initActionIfaceCB", 1);
		GInterfaceInfo interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitActionIfaceCB.getAddress ();
		ActionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (ActionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Component interface
		InitComponentIfaceCB = new Callback (AccessibleFactory.class, "initComponentIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitComponentIfaceCB.getAddress ();
		ComponentIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);
		ATK.memmove (ComponentIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Selection interface
		InitSelectionIfaceCB = new Callback (AccessibleFactory.class, "initSelectionIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitSelectionIfaceCB.getAddress ();
		SelectionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (SelectionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Text interface
		InitTextIfaceCB = new Callback (AccessibleFactory.class, "initTextIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = InitTextIfaceCB.getAddress ();
		TextIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (TextIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
	}

	private AccessibleFactory (int widgetType) {
		super ();
		/* If DefaultParentType is 0 then OS accessibility is not active */
		if (DefaultParentType == 0) return;
		int widgetTypeName = ATK.g_type_name (widgetType);
		int widgetTypeNameLength = OS.strlen (widgetTypeName) + 1;
		byte[] buffer = new byte [widgetTypeNameLength];
		OS.memmove (buffer, widgetTypeName, widgetTypeNameLength);
		byte[] factoryName = new byte [FACTORY_TYPENAME.length + widgetTypeNameLength];
		System.arraycopy (FACTORY_TYPENAME, 0, factoryName, 0, FACTORY_TYPENAME.length);
		System.arraycopy (buffer, 0, factoryName, FACTORY_TYPENAME.length, widgetTypeNameLength);
		if (ATK.g_type_from_name (factoryName) == 0) {
			// register the factory
			int registry = ATK.atk_get_default_registry ();
			int previousFactory = ATK.atk_registry_get_factory (registry, widgetType);
			objectParentType = ATK.atk_object_factory_get_accessible_type (previousFactory);
			if (objectParentType == 0) objectParentType = DefaultParentType;
			int factoryParentType = ATK.g_type_from_name (FACTORY_PARENTTYPENAME);
			gTypeInfo_base_init_factory  = new Callback (this, "gTypeInfo_base_init_factory", 1);
			GTypeInfo typeInfo = new GTypeInfo ();
			typeInfo.base_init = gTypeInfo_base_init_factory.getAddress ();
			typeInfo.class_size = ATK.AtkObjectFactoryClass_sizeof ();
			typeInfo.instance_size = ATK.AtkObjectFactory_sizeof ();
			handle = OS.g_malloc (GTypeInfo.sizeof); 
			ATK.memmove (handle, typeInfo, GTypeInfo.sizeof); 
			int swtFactory = ATK.g_type_register_static (factoryParentType, factoryName, handle, 0);
			ATK.atk_registry_set_factory_type (registry, widgetType, swtFactory);
			byte[] newTypeName = new byte [SWT_TYPE_PREFIX.length + widgetTypeNameLength];
			System.arraycopy (SWT_TYPE_PREFIX, 0, newTypeName, 0, SWT_TYPE_PREFIX.length);
			System.arraycopy (buffer, 0, newTypeName, SWT_TYPE_PREFIX.length, widgetTypeNameLength);
			objectType = defineType (newTypeName, objectParentType);
		}
	}
	
	void addAccessible (Accessible accessible) {
		int controlHandle = accessible.getControlHandle ();
		accessibles.put (new Integer (controlHandle), accessible);
	}

	int atkObjectFactory_create_accessible (int widget) {
		Accessible accessible = (Accessible) accessibles.get (new Integer (widget));
		if (accessible == null) {
			// we don't care about this control, so create it with the parent's 
			// type so that its accessibility callbacks will not pass though here
			int result = ATK.g_object_new (objectParentType, 0);
			ATK.atk_object_initialize (result, widget);
			return result;
		}
		AccessibleObject object = new AccessibleObject (objectType, widget, accessible, objectParentType, false);
		accessible.accessibleObject = object;
		return object.handle;
	}
	
	static int defineType (byte[] typeName, int parentType) {
		int queryPtr = OS.g_malloc (GTypeQuery.sizeof);
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
		int result = ATK.g_type_register_static (parentType, typeName, ObjectIfaceDefinition, 0);
		ATK.g_type_add_interface_static (result, AccessibleObject.ATK_COMPONENT_TYPE, ComponentIfaceDefinition);
		ATK.g_type_add_interface_static (result, AccessibleObject.ATK_ACTION_TYPE, ActionIfaceDefinition);
		ATK.g_type_add_interface_static (result, AccessibleObject.ATK_SELECTION_TYPE, SelectionIfaceDefinition);
		ATK.g_type_add_interface_static (result, AccessibleObject.ATK_TEXT_TYPE, TextIfaceDefinition);
		return result;
	}
	
	static int getDefaultChildType () {
		if (DefaultChildType == 0) {
			DefaultChildType = defineType (DEFAULT_CHILDTYPENAME, DefaultParentType);
		}
		return DefaultChildType;
	}

	static int getDefaultParentType () {
		return DefaultParentType;
	}

	int gTypeInfo_base_init_factory (int klass) {
		int atkObjectFactoryClass = ATK.ATK_OBJECT_FACTORY_CLASS (klass);
		AtkObjectFactoryClass objectFactoryClassStruct = new AtkObjectFactoryClass ();
		ATK.memmove (objectFactoryClassStruct, atkObjectFactoryClass);
		atkObjectFactoryCB_create_accessible = new Callback (this, "atkObjectFactory_create_accessible", 1);
		objectFactoryClassStruct.create_accessible = atkObjectFactoryCB_create_accessible.getAddress ();
		ATK.memmove (atkObjectFactoryClass, objectFactoryClassStruct); 
		return 0;
	}
	
	static int gTypeInfo_base_init_type (int klass) {
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
		int gObjectClass = ATK.G_OBJECT_CLASS (klass);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		objectClassStruct.finalize = GObjectClass_finalize.getAddress ();
		ATK.memmove (gObjectClass, objectClassStruct); 
		ATK.memmove (klass, objectClass);
		return 0;
	}
	
	static int initActionIfaceCB (int iface) {
		AtkActionIface actionIface = new AtkActionIface ();
		ATK.memmove (actionIface, iface);
		actionIface.get_keybinding = AtkActionCB_get_keybinding.getAddress (); 
		actionIface.get_name = AtkActionCB_get_name.getAddress ();
		ATK.memmove (iface, actionIface);
		return 0;
	}
	
	static int initComponentIfaceCB (int iface) {
		AtkComponentIface componentIface = new AtkComponentIface ();
		ATK.memmove (componentIface, iface);
		componentIface.get_extents = AtkComponentCB_get_extents.getAddress ();
		componentIface.get_position = AtkComponentCB_get_position.getAddress ();
		componentIface.get_size = AtkComponentCB_get_size.getAddress ();
		componentIface.ref_accessible_at_point = AtkComponentCB_ref_accessible_at_point.getAddress ();
		ATK.memmove (iface, componentIface);
		return 0;
	}

	static int initSelectionIfaceCB (int iface) {
		AtkSelectionIface selectionIface = new AtkSelectionIface ();
		ATK.memmove (selectionIface, iface);
		selectionIface.is_child_selected = AtkSelectionCB_is_child_selected.getAddress ();
		selectionIface.ref_selection = AtkSelectionCB_ref_selection.getAddress ();
		ATK.memmove (iface, selectionIface);
		return 0;
	}

	static int initTextIfaceCB (int iface) {
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
		int controlHandle = accessible.getControlHandle ();
		int widgetType = ATK.G_TYPE_FROM_INSTANCE (controlHandle);
		AccessibleFactory factory = (AccessibleFactory) Factories.get (new Integer (widgetType));
		if (factory == null) {
			factory = new AccessibleFactory (widgetType);
			Factories.put (new Integer (widgetType), factory);
		}
		factory.addAccessible (accessible);
	}
	
	void removeAccessible (Accessible accessible) {
		accessibles.remove (new Integer (accessible.getControlHandle ()));
	}
	
	static void unregisterAccessible (Accessible accessible) {
		int controlHandle = accessible.getControlHandle ();
		int widgetType = ATK.G_TYPE_FROM_INSTANCE (controlHandle);
		AccessibleFactory factory = (AccessibleFactory) Factories.get (new Integer (widgetType));
		if (factory != null) {
			factory.removeAccessible (accessible);
		}
	}
}
