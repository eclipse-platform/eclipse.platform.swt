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
import org.eclipse.swt.widgets.*;

class AccessibleType {
	int handle, parentType;
	static Hashtable accessibleObjects = new Hashtable (9);
	static boolean DEBUG = Display.DEBUG;

	// AT callbacks
	static Callback atkActionCB_get_keybinding;
	static Callback atkActionCB_get_name;	
	static Callback atkComponentCB_get_extents;
	static Callback atkComponentCB_get_position;
	static Callback atkComponentCB_get_size;
	static Callback atkComponentCB_ref_accessible_at_point;
	static Callback atkObjectCB_get_description;
	static Callback atkObjectCB_get_index_in_parent;
	static Callback atkObjectCB_get_n_children;
	static Callback atkObjectCB_get_name;
	static Callback atkObjectCB_get_parent;	
	static Callback atkObjectCB_get_role;
	static Callback atkObjectCB_ref_child;
	static Callback atkObjectCB_ref_state_set;
	static Callback atkSelectionCB_is_child_selected;
	static Callback atkSelectionCB_ref_selection;	
	static Callback atkTextCB_get_text;
	static Callback atkTextCB_get_text_after_offset;
	static Callback atkTextCB_get_text_at_offset;
	static Callback atkTextCB_get_text_before_offset;
	static Callback atkTextCB_get_character_at_offset;
	static Callback atkTextCB_get_character_count;
	static Callback gObjectClass_finalize;
	// interface initialization callbacks
	static Callback initActionIfaceCB;		
	static Callback initComponentIfaceCB;		
	static Callback gTypeInfo_base_init;
	static Callback initSelectionIfaceCB;	
	static Callback initTextIfaceCB;
	// interface definitions
	static int actionIfaceDefinition;
	static int componentIfaceDefinition;
	static int objectDefinition;
	static int selectionIfaceDefinition;
	static int textIfaceDefinition;
	static int ATK_ACTION_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkAction", true));
	static int ATK_COMPONENT_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkComponent", true));
	static int ATK_SELECTION_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkSelection", true));		
	static int ATK_TEXT_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkText", true));
	static {
		atkActionCB_get_keybinding = new Callback (AccessibleType.class, "atkAction_get_keybinding", 2);
		atkActionCB_get_name = new Callback (AccessibleType.class, "atkAction_get_name", 2);
		atkComponentCB_get_extents = new Callback (AccessibleType.class, "atkComponent_get_extents", 6);
		atkComponentCB_get_position = new Callback (AccessibleType.class, "atkComponent_get_position", 4);
		atkComponentCB_get_size = new Callback (AccessibleType.class, "atkComponent_get_size", 4);
		atkComponentCB_ref_accessible_at_point = new Callback (AccessibleType.class, "atkComponent_ref_accessible_at_point", 4);
		atkObjectCB_get_name = new Callback (AccessibleType.class, "atkObject_get_name", 1);
		atkObjectCB_get_description = new Callback (AccessibleType.class, "atkObject_get_description", 1);
		atkObjectCB_get_n_children = new Callback (AccessibleType.class, "atkObject_get_n_children", 1);
		atkObjectCB_get_role = new Callback (AccessibleType.class, "atkObject_get_role", 1);
		atkObjectCB_get_parent = new Callback (AccessibleType.class, "atkObject_get_parent", 1);
		atkObjectCB_ref_state_set = new Callback (AccessibleType.class, "atkObject_ref_state_set", 1);
		atkObjectCB_get_index_in_parent = new Callback (AccessibleType.class, "atkObject_get_index_in_parent", 1);
		atkObjectCB_ref_child = new Callback (AccessibleType.class, "atkObject_ref_child", 2);
		gObjectClass_finalize = new Callback (AccessibleType.class, "gObjectClass_finalize", 1);
		atkSelectionCB_is_child_selected = new Callback (AccessibleType.class, "atkSelection_is_child_selected", 2);
		atkSelectionCB_ref_selection = new Callback (AccessibleType.class, "atkSelection_ref_selection", 2);
		atkTextCB_get_text = new Callback (AccessibleType.class, "atkText_get_text", 3);
		atkTextCB_get_text_after_offset = new Callback (AccessibleType.class, "atkText_get_text_after_offset", 5);
		atkTextCB_get_text_at_offset = new Callback (AccessibleType.class, "atkText_get_text_at_offset", 5);
		atkTextCB_get_text_before_offset = new Callback (AccessibleType.class, "atkText_get_text_before_offset", 5);
		atkTextCB_get_character_at_offset = new Callback (AccessibleType.class, "atkText_get_character_at_offset", 2);
		atkTextCB_get_character_count = new Callback (AccessibleType.class, "atkText_get_character_count", 1);
		gTypeInfo_base_init = new Callback (AccessibleType.class, "gTypeInfo_base_init", 1);
		// Action interface
		initActionIfaceCB = new Callback (AccessibleType.class, "initActionIfaceCB", 1);
		GInterfaceInfo interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = initActionIfaceCB.getAddress ();
		actionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (actionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Component interface
		initComponentIfaceCB = new Callback (AccessibleType.class, "initComponentIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = initComponentIfaceCB.getAddress ();
		componentIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);
		ATK.memmove (componentIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Selection interface
		initSelectionIfaceCB = new Callback (AccessibleType.class, "initSelectionIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = initSelectionIfaceCB.getAddress ();
		selectionIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (selectionIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
		// Text interface
		initTextIfaceCB = new Callback (AccessibleType.class, "initTextIfaceCB", 1);
		interfaceInfo = new GInterfaceInfo ();
		interfaceInfo.interface_init = initTextIfaceCB.getAddress ();
		textIfaceDefinition = OS.g_malloc (GInterfaceInfo.sizeof);  
		ATK.memmove (textIfaceDefinition, interfaceInfo, GInterfaceInfo.sizeof);
	}
	
	AccessibleType (byte[] typeName, int parentType) {
		super ();
		this.parentType = parentType;
		int queryPtr = OS.g_malloc (GTypeQuery.sizeof);
		ATK.g_type_query (parentType, queryPtr);
		GTypeQuery query = new GTypeQuery ();
		ATK.memmove (query, queryPtr, GTypeQuery.sizeof);
		OS.g_free (queryPtr);
		GTypeInfo typeInfo = new GTypeInfo ();
		typeInfo.base_init = gTypeInfo_base_init.getAddress ();
		typeInfo.class_size = (short) query.class_size;
		typeInfo.instance_size = (short) query.instance_size;
		objectDefinition = OS.g_malloc (GTypeInfo.sizeof); 
		ATK.memmove (objectDefinition, typeInfo, GTypeInfo.sizeof); 
		handle = ATK.g_type_register_static (parentType, typeName, objectDefinition, 0);
		ATK.g_type_add_interface_static (handle, ATK_COMPONENT_TYPE, componentIfaceDefinition);
		ATK.g_type_add_interface_static (handle, ATK_ACTION_TYPE, actionIfaceDefinition);
		ATK.g_type_add_interface_static (handle, ATK_SELECTION_TYPE, selectionIfaceDefinition);
		ATK.g_type_add_interface_static (handle, ATK_TEXT_TYPE, textIfaceDefinition);
	}

	static void addInstance (AccessibleObject object) {
		accessibleObjects.put (new Integer (object.handle), object);
	}
	
	static int atkAction_get_keybinding (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkAction_get_keybinding");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkAction_get_keybinding (index);
		}
		return 0;
	}
	
	static int atkAction_get_name (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkAction_get_name");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkAction_get_name (index);
		}
		return 0;
	}
	
	static int atkComponent_get_extents (int atkObject, int x, int y, int width, int height, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_extents");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkComponent_get_extents (x, y, width, height, coord_type);
		}
		return 0;
	}

	static int atkComponent_get_position (int atkObject, int x, int y, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_position, object: " + atkObject + " x: " + x + " y: " + y + " coord: " + coord_type);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkComponent_get_position (x, y, coord_type);
		}
		return 0;
	}

	static int atkComponent_get_size (int atkObject, int width, int height, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_size");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkComponent_get_size (width, height, coord_type);
		}
		return 0;
	}

	static int atkComponent_ref_accessible_at_point (int atkObject, int x, int y, int coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_ref_accessible_at_point");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkComponent_ref_accessible_at_point (x, y, coord_type);
		}
		return 0;
	}

	static int atkObject_get_description (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_description ();
		}
		return 0;
	}	

	static int atkObject_get_index_in_parent (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObjectCB_get_index_in_parent.  ");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_index_in_parent ();
		}
		return 0;
	}

	static int atkObject_get_n_children (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_n_children: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_n_children ();
		}
		return 0;
	}

	static int atkObject_get_name (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_name: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_name ();
		}
		return 0;
	}

	static int atkObject_get_parent (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_parent: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_parent ();
		}
		return 0;
	}

	static int atkObject_get_role (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_role: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_get_role ();
		}
		return 0;
	}

	static int atkObject_ref_child (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkObject_ref_child: " + index + " of: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_ref_child (index);
		}
		return 0;
	}

	static int atkObject_ref_state_set (int atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_ref_state_set");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkObject_ref_state_set ();
		}
		return 0;
	}

	static int atkSelection_is_child_selected (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkSelection_is_child_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkSelection_is_child_selected (index);
		}
		return 0;
	}

	static int atkSelection_ref_selection (int atkObject, int index) {
		if (DEBUG) System.out.println ("-->atkSelection_ref_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkSelection_ref_selection (index);
		}
		return 0;
	}
	
	static int atkText_get_text (int atkObject, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text: " + start_offset + "," + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_text (start_offset, end_offset);
		}
		return 0;
	}

	static int atkText_get_text_after_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_after_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_text_after_offset (offset, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	static int atkText_get_text_at_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_at_offset: " + offset + " start: " + start_offset + " end: " + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_text_at_offset (offset, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	static int atkText_get_text_before_offset (int atkObject, int offset, int boundary_type, int start_offset, int end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_before_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_text_before_offset (offset, boundary_type, start_offset, end_offset);
		}
		return 0;
	}

	static int atkText_get_character_at_offset (int atkObject, int offset) {
		if (DEBUG) System.out.println ("-->atkText_get_character_at_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_character_at_offset (offset);
		}
		return 0;
	}

	static int atkText_get_character_count (int atkObject) {
		if (DEBUG) System.out.println ("-->atkText_get_character_count");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			return object.atkText_get_character_count ();
		}
		return 0;
	}
	
	int createObject (Accessible accessible, int widget) {
		int type = handle;
		Accessible acc = accessible;
		if (acc == null) {
			// we don't care about this control, so create it with the parent's 
			// type so that its accessibility callbacks will not pass though here
			type = ATK.g_type_parent (type);
			int result = ATK.g_object_new (type, 0);
			ATK.atk_object_initialize (result, widget);
			return result;
		}
		AccessibleObject object = new AccessibleObject (type, widget, acc, parentType, false);
		accessibleObjects.put (new Integer (object.handle), object);
		acc.accessibleObject = object;
		return object.handle;
	}

	static AccessibleObject getAccessibleObject (int atkObject) {
		return (AccessibleObject)accessibleObjects.get (new Integer (atkObject));
	}

	static int gObjectClass_finalize (int atkObject) {
		int superType = ATK.g_type_class_peek_parent (ATK.G_OBJECT_GET_CLASS (atkObject));
		int gObjectClass = ATK.G_OBJECT_CLASS (superType);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		ATK.call (objectClassStruct.finalize, atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			accessibleObjects.remove (new Integer (atkObject));
			object.dispose ();
		}
		return 0;
	}

	static int gTypeInfo_base_init (int klass) {
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, klass);
		objectClass.get_name = atkObjectCB_get_name.getAddress ();
		objectClass.get_description = atkObjectCB_get_description.getAddress ();
		objectClass.get_n_children = atkObjectCB_get_n_children.getAddress ();
		objectClass.get_role = atkObjectCB_get_role.getAddress ();
		objectClass.get_parent = atkObjectCB_get_parent.getAddress ();
		objectClass.ref_state_set = atkObjectCB_ref_state_set.getAddress ();
		objectClass.get_index_in_parent = atkObjectCB_get_index_in_parent.getAddress ();
		objectClass.ref_child = atkObjectCB_ref_child.getAddress ();
		int gObjectClass = ATK.G_OBJECT_CLASS (klass);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		objectClassStruct.finalize = gObjectClass_finalize.getAddress ();
		ATK.memmove (gObjectClass, objectClassStruct); 
		ATK.memmove (klass, objectClass);
		return 0;
	}	

	static int initActionIfaceCB (int iface) {
		AtkActionIface actionIface = new AtkActionIface ();
		ATK.memmove (actionIface, iface);
		actionIface.get_keybinding = atkActionCB_get_keybinding.getAddress (); 
		actionIface.get_name = atkActionCB_get_name.getAddress ();
		ATK.memmove (iface, actionIface);
		return 0;
	}
	
	static int initComponentIfaceCB (int iface) {
		AtkComponentIface componentIface = new AtkComponentIface ();
		ATK.memmove (componentIface, iface);
		componentIface.get_extents = atkComponentCB_get_extents.getAddress ();
		componentIface.get_position = atkComponentCB_get_position.getAddress ();
		componentIface.get_size = atkComponentCB_get_size.getAddress ();
		componentIface.ref_accessible_at_point = atkComponentCB_ref_accessible_at_point.getAddress ();
		ATK.memmove (iface, componentIface);
		return 0;
	}

	static int initSelectionIfaceCB (int iface) {
		AtkSelectionIface selectionIface = new AtkSelectionIface ();
		ATK.memmove (selectionIface, iface);
		selectionIface.is_child_selected = atkSelectionCB_is_child_selected.getAddress ();
		selectionIface.ref_selection = atkSelectionCB_ref_selection.getAddress ();
		ATK.memmove (iface, selectionIface);
		return 0;
	}
		
	static int initTextIfaceCB (int iface) {
		AtkTextIface textInterface = new AtkTextIface ();
		ATK.memmove (textInterface, iface);
		textInterface.get_text = atkTextCB_get_text.getAddress ();
		textInterface.get_text_after_offset = atkTextCB_get_text_after_offset.getAddress ();
		textInterface.get_text_at_offset = atkTextCB_get_text_at_offset.getAddress ();
		textInterface.get_text_before_offset = atkTextCB_get_text_before_offset.getAddress ();
		textInterface.get_character_at_offset = atkTextCB_get_character_at_offset.getAddress ();
		textInterface.get_character_count = atkTextCB_get_character_count.getAddress ();
		ATK.memmove (iface, textInterface);
		return 0;
	}
}
