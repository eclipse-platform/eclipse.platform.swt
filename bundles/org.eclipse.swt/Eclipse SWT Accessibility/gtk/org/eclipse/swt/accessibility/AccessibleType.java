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
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class AccessibleType {
	int handle;
	Hashtable accessibleObjects = new Hashtable (9);

	// AT callbacks
	Callback atkActionCB_get_keybinding = null;
	Callback atkActionCB_get_name = null;	
	Callback atkComponentCB_get_extents = null;
	Callback atkComponentCB_get_position = null;
	Callback atkComponentCB_get_size = null;
	Callback atkComponentCB_ref_accessible_at_point = null;
	Callback atkObjectCB_get_description = null;
	Callback atkObjectCB_get_index_in_parent = null;
	Callback atkObjectCB_get_n_children = null;
	Callback atkObjectCB_get_name = null;
	Callback atkObjectCB_get_parent = null;	
	Callback atkObjectCB_get_role = null;
	Callback atkObjectCB_ref_child = null;
	Callback atkObjectCB_ref_state_set = null;
	Callback atkSelectionCB_is_child_selected = null;
	Callback atkSelectionCB_ref_selection = null;	
	Callback atkTextCB_get_text = null;
	Callback atkTextCB_get_text_after_offset = null;
	Callback atkTextCB_get_text_at_offset = null;
	Callback atkTextCB_get_text_before_offset = null;
	Callback atkTextCB_get_character_at_offset = null;
	Callback atkTextCB_get_character_count = null;
	Callback gObjectClass_finalize = null;

	// interface initialization callbacks
	Callback initActionIfaceCB = null;		
	Callback initComponentIfaceCB = null;		
	Callback gTypeInfo_base_init = null;
	Callback initSelectionIfaceCB = null;	
	Callback initTextIfaceCB = null;

	// interface definitions
	int actionIfaceDefinition = -1;
	int componentIfaceDefinition = -1;
	int objectDefinition = -1;	
	int selectionIfaceDefinition = -1;
	int textIfaceDefinition = -1;

	static AccessibleType instance;
	static boolean DEBUG = Display.DEBUG;
	static final String ACCESSIBLE_TYPE_NAME = "SWTAccessible";
	static final String PARENT_TYPE_NAME = "GtkAccessible";
	static final int ATK_ACTION_TYPE = OS.g_type_from_name (Converter.wcsToMbcs (null, "AtkAction", true));
	static final int ATK_COMPONENT_TYPE = OS.g_type_from_name (Converter.wcsToMbcs (null, "AtkComponent", true));
	static final int ATK_SELECTION_TYPE = OS.g_type_from_name (Converter.wcsToMbcs (null, "AtkSelection", true));		
	static final int ATK_TEXT_TYPE = OS.g_type_from_name (Converter.wcsToMbcs (null, "AtkText", true));
	
	static {
		 instance = new AccessibleType ();
	}
	
	private AccessibleType () {
		super ();
		int parentType = OS.g_type_from_name (Converter.wcsToMbcs (null, PARENT_TYPE_NAME, true));
		int queryPtr = OS.g_malloc (GTypeQuery.sizeof);
		OS.g_type_query (parentType, queryPtr);
		GTypeQuery query = new GTypeQuery ();
		OS.memmove (query, queryPtr, GTypeQuery.sizeof);
		OS.g_free (queryPtr);
		gTypeInfo_base_init = new Callback (this, "gTypeInfo_base_init", 1);
		GTypeInfo typeInfo = new GTypeInfo ();
		typeInfo.base_init = gTypeInfo_base_init.getAddress ();
		typeInfo.class_size = (short) query.class_size;
		typeInfo.instance_size = (short) query.instance_size;
		objectDefinition = OS.g_malloc (GTypeInfo.sizeof); 
		OS.memmove (objectDefinition, typeInfo, GTypeInfo.sizeof);
		byte[] name = Converter.wcsToMbcs (null, ACCESSIBLE_TYPE_NAME, true);
		handle = OS.g_type_register_static (parentType, name, objectDefinition, 0);
		// add Action interface
		GInterfaceInfo interfaceInfo = new GInterfaceInfo ();
		initActionIfaceCB = new Callback (this, "initActionIfaceCB", 1);
		interfaceInfo.interface_init = initActionIfaceCB.getAddress ();
		actionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (actionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		OS.g_type_add_interface_static (handle, ATK_ACTION_TYPE, actionIfaceDefinition);
		// add Component interface
		interfaceInfo = new GInterfaceInfo ();
		initComponentIfaceCB = new Callback (this, "initComponentIfaceCB", 1);
		interfaceInfo.interface_init = initComponentIfaceCB.getAddress ();
		componentIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);
		OS.memmove (componentIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		OS.g_type_add_interface_static (handle, ATK_COMPONENT_TYPE, componentIfaceDefinition);
		// add Selection interface
		interfaceInfo = new GInterfaceInfo ();
		initSelectionIfaceCB = new Callback (this, "initSelectionIfaceCB", 1);
		interfaceInfo.interface_init = initSelectionIfaceCB.getAddress ();
		selectionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (selectionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		OS.g_type_add_interface_static (handle, ATK_SELECTION_TYPE, selectionIfaceDefinition);
		// add Text interface
		interfaceInfo = new GInterfaceInfo ();
		initTextIfaceCB = new Callback (this, "initTextIfaceCB", 1);
		interfaceInfo.interface_init = initTextIfaceCB.getAddress ();
		textIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		OS.memmove (textIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		OS.g_type_add_interface_static (handle, ATK_TEXT_TYPE, textIfaceDefinition);
	}

	void addInstance (AccessibleObject object) {
		accessibleObjects.put (new Integer (object.handle), object);
	}
	
	int atkAction_get_keybinding (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkAction_get_keybinding");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkAction_get_keybinding (index);
	}
	
	int atkAction_get_name (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkAction_get_name");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkAction_get_name (index);
	}
	
	int atkComponent_get_extents (int atkObject, int x, int y, int width, int height, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_extents");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkComponent_get_extents (x, y, width, height, coord_type);
	}

	int atkComponent_get_position (int atkObject, int x, int y, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_position, object: " + atkObject + " x: " + x + " y: " + y + " coord: " + coord_type);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkComponent_get_position (x, y, coord_type);
	}

	int atkComponent_get_size (int atkObject, int width, int height, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_size");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkComponent_get_size (width, height, coord_type);
	}

	int atkComponent_ref_accessible_at_point (int atkObject, int x, int y, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_ref_accessible_at_point");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkComponent_ref_accessible_at_point (x, y, coord_type);
	}

	int atkObject_get_description (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_description ();
	}	

	int atkObject_get_index_in_parent (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObjectCB_get_index_in_parent.  ");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_index_in_parent ();
	}

	int atkObject_get_n_children (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_n_children: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_n_children ();
	}

	int atkObject_get_name (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_name: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_name ();
	}

	int atkObject_get_parent (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_parent: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_parent ();
	}

	int atkObject_get_role (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_role: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_get_role ();
	}

	int atkObject_ref_child (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkObject_ref_child: " + index + " of: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_ref_child (index);
	}

	int atkObject_ref_state_set (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_ref_state_set");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkObject_ref_state_set ();
	}

	int atkSelection_is_child_selected (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkSelection_is_child_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkSelection_is_child_selected (index);
	}

	int atkSelection_ref_selection (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkSelection_ref_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkSelection_ref_selection (index);
	}
	
	int atkText_get_text (int atkObject, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text: " + start_offset + "," + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_text (start_offset, end_offset);
	}

	int atkText_get_text_after_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_after_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_text_after_offset (offset, boundary_type, start_offset, end_offset);
	}

	int atkText_get_text_at_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_at_offset: " + offset + " start: " + start_offset + " end: " + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_text_at_offset (offset, boundary_type, start_offset, end_offset);
	}

	int atkText_get_text_before_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_before_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_text_before_offset (offset, boundary_type, start_offset, end_offset);
	}

	int atkText_get_character_at_offset (int atkObject, int offset) {
		if (DEBUG) System.out.println ("-->atkText_get_character_at_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_character_at_offset (offset);
	}

	int atkText_get_character_count (int atkObject) {
		if (DEBUG) System.out.println ("-->atkText_get_character_count");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		return object.atkText_get_character_count ();
	}
	
	int createObject (Accessible accessible, int widget, int parentType) {
		int type = handle;
		Accessible acc = accessible;
		if (acc == null) {
			// we don't care about this control, so create it with the parent's 
			// type so that its accessibility callbacks will not pass though here
			type = OS.g_type_parent (type);
			int result = OS.g_object_new (type, 0);
			ATK.atk_object_initialize (result, widget);
			return result;
		}
		AccessibleObject object = new AccessibleObject (type, widget, acc, parentType);
		accessibleObjects.put (new Integer (object.handle), object);
		acc.accessibleObject = object;
		return object.handle;
	}

	AccessibleObject getAccessibleObject (int atkObject) {
		return (AccessibleObject)accessibleObjects.get (new Integer (atkObject));
	}

	static AccessibleType getInstance () {
		return instance;
	}
	
	int gObjectClass_finalize (int atkObject) {
		int superType = OS.g_type_class_peek_parent (OS.G_OBJECT_GET_CLASS (atkObject));
		int gObjectClass = OS.G_OBJECT_CLASS (superType);
		GObjectClass objectClassStruct = new GObjectClass ();
		OS.memmove (objectClassStruct, gObjectClass);
		OS.call (objectClassStruct.finalize, atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			accessibleObjects.remove (new Integer (atkObject));
			object.dispose ();
		}
		return 0;
	}

	int gTypeInfo_base_init (int klass) {
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, klass);
		atkObjectCB_get_name = new Callback (this, "atkObject_get_name", 1);
		objectClass.get_name = atkObjectCB_get_name.getAddress ();
		atkObjectCB_get_description = new Callback (this, "atkObject_get_description", 1);
		objectClass.get_description = atkObjectCB_get_description.getAddress ();
		atkObjectCB_get_n_children = new Callback (this, "atkObject_get_n_children", 1);
		objectClass.get_n_children = atkObjectCB_get_n_children.getAddress ();
		atkObjectCB_get_role = new Callback (this, "atkObject_get_role", 1);
		objectClass.get_role = atkObjectCB_get_role.getAddress ();
		atkObjectCB_get_parent = new Callback (this, "atkObject_get_parent", 1);
		objectClass.get_parent = atkObjectCB_get_parent.getAddress ();
		atkObjectCB_ref_state_set = new Callback (this, "atkObject_ref_state_set", 1);
		objectClass.ref_state_set = atkObjectCB_ref_state_set.getAddress ();
		atkObjectCB_get_index_in_parent = new Callback (this, "atkObject_get_index_in_parent", 1);
		objectClass.get_index_in_parent = atkObjectCB_get_index_in_parent.getAddress ();
		atkObjectCB_ref_child = new Callback (this, "atkObject_ref_child", 2);
		objectClass.ref_child = atkObjectCB_ref_child.getAddress ();
		int gObjectClass = OS.G_OBJECT_CLASS (klass);
		GObjectClass objectClassStruct = new GObjectClass ();
		OS.memmove (objectClassStruct, gObjectClass);
		gObjectClass_finalize = new Callback (this, "gObjectClass_finalize", 1);
		objectClassStruct.finalize = gObjectClass_finalize.getAddress ();
		OS.memmove (gObjectClass, objectClassStruct); 
		ATK.memmove (klass, objectClass);
		return 0;
	}	

	int initActionIfaceCB (int iface) {
		AtkActionIface actionIface = new AtkActionIface ();
		ATK.memmove (actionIface, iface);
		atkActionCB_get_keybinding = new Callback (this, "atkAction_get_keybinding", 2);
		actionIface.get_keybinding = atkActionCB_get_keybinding.getAddress (); 
		atkActionCB_get_name = new Callback (this, "atkAction_get_name", 2);
		actionIface.get_name = atkActionCB_get_name.getAddress ();
		ATK.memmove (iface, actionIface);
		return 0;
	}
	
	int initComponentIfaceCB (int iface) {
		AtkComponentIface componentIface = new AtkComponentIface ();
		ATK.memmove (componentIface, iface);
		atkComponentCB_get_extents = new Callback (this, "atkComponent_get_extents", 6);
		componentIface.get_extents = atkComponentCB_get_extents.getAddress ();
		atkComponentCB_get_position = new Callback (this, "atkComponent_get_position", 4);
		componentIface.get_position = atkComponentCB_get_position.getAddress ();
		atkComponentCB_get_size = new Callback (this, "atkComponent_get_size", 4);
		componentIface.get_size = atkComponentCB_get_size.getAddress ();
		atkComponentCB_ref_accessible_at_point = new Callback (this, "atkComponent_ref_accessible_at_point", 4);
		componentIface.ref_accessible_at_point = atkComponentCB_ref_accessible_at_point.getAddress ();
		ATK.memmove (iface, componentIface);
		return 0;
	}

	int initSelectionIfaceCB (int iface) {
		AtkSelectionIface selectionIface = new AtkSelectionIface ();
		ATK.memmove (selectionIface, iface);
		atkSelectionCB_is_child_selected = new Callback (this, "atkSelection_is_child_selected", 2);
		selectionIface.is_child_selected = atkSelectionCB_is_child_selected.getAddress ();
		atkSelectionCB_ref_selection = new Callback (this, "atkSelection_ref_selection", 2);
		selectionIface.ref_selection = atkSelectionCB_ref_selection.getAddress ();
		ATK.memmove (iface, selectionIface);
		return 0;
	}
		
	int initTextIfaceCB (int iface) {
		AtkTextIface textInterface = new AtkTextIface ();
		ATK.memmove (textInterface, iface);
		atkTextCB_get_text = new Callback (this, "atkText_get_text", 3);
		textInterface.get_text = atkTextCB_get_text.getAddress ();
		atkTextCB_get_text_after_offset = new Callback (this, "atkText_get_text_after_offset", 5);
		textInterface.get_text_after_offset = atkTextCB_get_text_after_offset.getAddress ();
		atkTextCB_get_text_at_offset = new Callback (this, "atkText_get_text_at_offset", 5);
		textInterface.get_text_at_offset = atkTextCB_get_text_at_offset.getAddress ();
		atkTextCB_get_text_before_offset = new Callback (this, "atkText_get_text_before_offset", 5);
		textInterface.get_text_before_offset = atkTextCB_get_text_before_offset.getAddress ();
		atkTextCB_get_character_at_offset = new Callback (this, "atkText_get_character_at_offset", 2);
		textInterface.get_character_at_offset = atkTextCB_get_character_at_offset.getAddress ();
		atkTextCB_get_character_count = new Callback (this, "atkText_get_character_count", 1);
		textInterface.get_character_count = atkTextCB_get_character_count.getAddress ();
		ATK.memmove (iface, textInterface);
		return 0;
	}
}
