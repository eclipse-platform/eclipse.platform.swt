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

public class AccessibleObject {
	int handle, parentType, index = -1, id = ACC.CHILDID_SELF;
	Accessible accessible;
	AccessibleObject parent;
	Hashtable children = new Hashtable (9);
	boolean isLightweight = false;
	int actionNamePtr = -1;
	int descriptionPtr = -1;
	int keybindingPtr = -1;
	int namePtr = -1;
	int textPtr = -1;
	static boolean DEBUG = Display.DEBUG;

	AccessibleObject (int type, int widget, Accessible accessible, int parentType, boolean isLightweight) {
		super ();
		handle = OS.g_object_new (type, 0);
		this.parentType = parentType;
		ATK.atk_object_initialize (handle, widget);
		this.accessible = accessible;
		this.isLightweight = isLightweight;
		if (DEBUG) System.out.println("new AccessibleObject: " + handle);
	}

	void addChild (AccessibleObject child) {
		children.put (new Integer (child.handle), child);
		child.setParent (this);
	}
	
	int atkAction_get_keybinding (int index) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_ACTION_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (handle));
			AtkActionIface actionIface = new AtkActionIface ();
			ATK.memmove (actionIface, superType);
			if (actionIface.get_keybinding != 0) {
				parentResult = OS.call (actionIface.get_keybinding, handle, index);
			}
		}
		AccessibleListener[] listeners = accessible.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;

		AccessibleEvent event = new AccessibleEvent (this);
		event.childID = id;
		if (parentResult != 0) {
			int length = OS.strlen (parentResult);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, parentResult, length);
			event.result = new String (Converter.mbcsToWcs (null, buffer));
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getKeyboardShortcut (event);	
		} 
		if (event.result == null) return parentResult;
		if (keybindingPtr != -1) OS.g_free (keybindingPtr);
		byte[] name = Converter.wcsToMbcs (null, event.result, true);
		keybindingPtr = OS.g_malloc (name.length);
		OS.memmove (keybindingPtr, name, name.length);
		return keybindingPtr; 	
	}

	int atkAction_get_name (int index) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_ACTION_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (handle));
			AtkActionIface actionIface = new AtkActionIface ();
			ATK.memmove (actionIface, superType);
			if (actionIface.get_name != 0) {
				parentResult = OS.call (actionIface.get_name, handle, index);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;

		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		if (parentResult != 0) {
			int length = OS.strlen (parentResult);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, parentResult, length);
			event.result = new String (Converter.mbcsToWcs (null, buffer));
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getDefaultAction (event);				
		} 
		if (event.result == null) return parentResult;
		if (actionNamePtr != -1) OS.g_free (actionNamePtr);
		byte[] name = Converter.wcsToMbcs (null, event.result, true);
		actionNamePtr = OS.g_malloc (name.length);
		OS.memmove (actionNamePtr, name, name.length);
		return actionNamePtr;
	}	

	int atkComponent_get_extents (int x, int y, int width, int height, int coord_type) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_COMPONENT_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				parentResult = OS.call (componentIface.get_extents, x, y, width, height, coord_type);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
		
		int[] parentX = new int [1], parentY = new int [1];
		int[] parentWidth = new int [1], parentHeight = new int [1];
		OS.memmove (parentX, x, 4);
		OS.memmove (parentY, y, 4);
		OS.memmove (parentWidth, width, 4);
		OS.memmove (parentHeight, height, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.x = parentX [0]; event.y = parentY [0];
		event.width = parentWidth [0]; event.height = parentHeight [0];
		if (coord_type == ATK.ATK_XY_WINDOW) {
			// translate control -> display, for filling in the event to be dispatched
			int gtkAccessibleHandle = OS.GTK_ACCESSIBLE (handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			OS.memmove (gtkAccessible, gtkAccessibleHandle);
			int topLevel = OS.gtk_widget_get_toplevel (gtkAccessible.widget);
			int window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		if (coord_type == ATK.ATK_XY_WINDOW) {
			// translate display -> control, for answering to the OS 
			int gtkAccessibleHandle = OS.GTK_ACCESSIBLE (handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			OS.memmove (gtkAccessible, gtkAccessibleHandle);
			int topLevel = OS.gtk_widget_get_toplevel (gtkAccessible.widget);
			int window = OS.GTK_WIDGET_WINDOW (topLevel);
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x -= topWindowX [0];
			event.y -= topWindowY [0];
		}
		OS.memmove (x, new int[] {event.x}, 4);
		OS.memmove (y, new int[] {event.y}, 4);
		OS.memmove (width, new int[] {event.width}, 4);
		OS.memmove (height, new int[] {event.height}, 4);
		return 0;
	}

	int atkComponent_get_position (int x, int y, int coord_type) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_COMPONENT_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				parentResult = OS.call (componentIface.get_position, x, y, coord_type);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
		
		int[] parentX = new int [1], parentY = new int [1];
		OS.memmove (parentX, x, 4);
		OS.memmove (parentY, y, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.x = parentX [0]; event.y = parentY [0];
		if (coord_type == ATK.ATK_XY_WINDOW) {
			// translate control -> display, for filling in the event to be dispatched
			int gtkAccessibleHandle = OS.GTK_ACCESSIBLE (handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			OS.memmove (gtkAccessible, gtkAccessibleHandle);
			int topLevel = OS.gtk_widget_get_toplevel (gtkAccessible.widget);
			int window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		if (coord_type == ATK.ATK_XY_WINDOW) {
			// translate display -> control, for answering to the OS 
			int gtkAccessibleHandle = OS.GTK_ACCESSIBLE (handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			OS.memmove (gtkAccessible, gtkAccessibleHandle);
			int topLevel = OS.gtk_widget_get_toplevel (gtkAccessible.widget);
			int window = OS.GTK_WIDGET_WINDOW (topLevel);
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x -= topWindowX [0];
			event.y -= topWindowY [0];
		}
		OS.memmove (x, new int[] {event.x}, 4);
		OS.memmove (y, new int[] {event.y}, 4);
		return 0;
	}

	int atkComponent_get_size (int width, int height, int coord_type) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_COMPONENT_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				parentResult = OS.call (componentIface.get_size, width, height, coord_type);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
		
		int[] parentWidth = new int [1], parentHeight = new int [1];
		OS.memmove (parentWidth, width, 4);
		OS.memmove (parentHeight, height, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.width = parentWidth [0]; event.height = parentHeight [0];
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		OS.memmove (width, new int[] {event.width}, 4);
		OS.memmove (height, new int[] {event.height}, 4);
		return 0;
	}

	int atkComponent_ref_accessible_at_point (int x, int y, int coord_type) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_COMPONENT_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.ref_accessible_at_point != 0) {
				parentResult = OS.call (componentIface.ref_accessible_at_point, handle, x, y, coord_type);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
		
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.x = x; event.y = y;
		if (coord_type == ATK.ATK_XY_WINDOW) {
			// translate control -> display, for filling in the event to be dispatched
			int gtkAccessibleHandle = OS.GTK_ACCESSIBLE (handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			OS.memmove (gtkAccessible, gtkAccessibleHandle);
			int topLevel = OS.gtk_widget_get_toplevel (gtkAccessible.widget);
			int window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildAtPoint (event);				
		}
		if (event.childID == id) event.childID = ACC.CHILDID_SELF;
		AccessibleObject accObj = getChildByID (event.childID);
		if (accObj != null) {
			if (parentResult > 0) OS.g_object_unref (parentResult);
			OS.g_object_ref (accObj.handle);	
			return accObj.handle;
		}
		return parentResult;
	}	

	int atkObject_get_description () {
		int parentResult = 0;
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_description != 0) {
			parentResult = OS.call (objectClass.get_description, handle);
		}
		AccessibleListener[] listeners = accessible.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleEvent event = new AccessibleEvent (this);
		event.childID = id;
		if (parentResult != 0) {
			int length = OS.strlen (parentResult);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, parentResult, length);
			event.result = new String (Converter.mbcsToWcs (null, buffer));
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getDescription (event);				
		} 
		if (event.result == null) return parentResult;
		if (descriptionPtr != -1) OS.g_free (descriptionPtr);
		byte[] name = Converter.wcsToMbcs (null, event.result, true);
		descriptionPtr = OS.g_malloc (name.length);
		OS.memmove (descriptionPtr, name, name.length);
		return descriptionPtr; 
	}

	int atkObject_get_name () {
		int parentResult = 0;
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_name != 0) {
			parentResult = OS.call (objectClass.get_name, handle);
		}
		AccessibleListener[] listeners = accessible.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;
		
		AccessibleEvent event = new AccessibleEvent (this);
		event.childID = id;
		if (parentResult != 0) {
			int length = OS.strlen (parentResult);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, parentResult, length);
			event.result = new String (Converter.mbcsToWcs (null, buffer));
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getName (event);				
		} 
		if (event.result == null) return parentResult;
		if (namePtr != -1) OS.g_free (namePtr);
		byte[] name = Converter.wcsToMbcs (null, event.result, true);
		namePtr = OS.g_malloc (name.length);
		OS.memmove (namePtr, name, name.length);
		return namePtr; 
	}	

	int atkObject_get_n_children () {
		int parentResult = 0;
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_n_children != 0) { 
			parentResult = OS.call (objectClass.get_n_children, handle);
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.detail = parentResult;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildCount (event);
		} 
		return event.detail;
	}

	int atkObject_get_index_in_parent () {
		if (index != -1) return index;
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_index_in_parent == 0) return 0;
		return OS.call (objectClass.get_index_in_parent, handle);
	}

	int atkObject_get_parent () {
		if (parent != null) return parent.handle;
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_parent == 0) return 0;
		return OS.call (objectClass.get_parent, handle);
	}

	int atkObject_get_role () {
		if (accessible.getAccessibleListeners ().length != 0) {
			AccessibleControlListener[] listeners = accessible.getControlListeners ();
			AccessibleControlEvent event = new AccessibleControlEvent (this);
			event.childID = id;
			event.detail = -1;
			for (int i = 0; i < listeners.length; i++) {
				listeners [i].getRole (event);				
			} 
			if (event.detail != -1) {
				switch (event.detail) {
					// Convert from win32 role values to atk role values
					case ACC.ROLE_CHECKBUTTON: return ATK.ATK_ROLE_CHECK_BOX;
					case ACC.ROLE_CLIENT_AREA: return ATK.ATK_ROLE_DRAWING_AREA;
					case ACC.ROLE_COMBOBOX: return ATK.ATK_ROLE_COMBO_BOX;
					case ACC.ROLE_DIALOG: return ATK.ATK_ROLE_DIALOG;
					case ACC.ROLE_LABEL: return ATK.ATK_ROLE_LABEL;
					case ACC.ROLE_LIST: return ATK.ATK_ROLE_LIST;
					case ACC.ROLE_LISTITEM: return ATK.ATK_ROLE_LIST_ITEM;
					case ACC.ROLE_MENU: return ATK.ATK_ROLE_MENU;
					case ACC.ROLE_MENUBAR: return ATK.ATK_ROLE_MENU_BAR;
					case ACC.ROLE_MENUITEM: return ATK.ATK_ROLE_MENU_ITEM;
					case ACC.ROLE_PROGRESSBAR: return ATK.ATK_ROLE_PROGRESS_BAR;
					case ACC.ROLE_PUSHBUTTON: return ATK.ATK_ROLE_PUSH_BUTTON;
					case ACC.ROLE_SCROLLBAR: return ATK.ATK_ROLE_SCROLL_BAR;
					case ACC.ROLE_SEPARATOR: return ATK.ATK_ROLE_SEPARATOR;
					case ACC.ROLE_SLIDER: return ATK.ATK_ROLE_SLIDER;
					case ACC.ROLE_TABLE: return ATK.ATK_ROLE_TABLE;
					case ACC.ROLE_TABFOLDER: return ATK.ATK_ROLE_PAGE_TAB_LIST;
					case ACC.ROLE_TABLECOLUMN: return ATK.ATK_ROLE_TABLE_COLUMN_HEADER; // closest match
					case ACC.ROLE_TABITEM: return ATK.ATK_ROLE_PAGE_TAB;
					case ACC.ROLE_TEXT: return ATK.ATK_ROLE_TEXT;
					case ACC.ROLE_TOOLBAR: return ATK.ATK_ROLE_TOOL_BAR;
					case ACC.ROLE_TOOLTIP: return ATK.ATK_ROLE_TOOL_TIP;
					case ACC.ROLE_TREE: return ATK.ATK_ROLE_TREE;
					case ACC.ROLE_RADIOBUTTON: return ATK.ATK_ROLE_RADIO_BUTTON;
					case ACC.ROLE_WINDOW: return ATK.ATK_ROLE_WINDOW;
				}
			}
		} 
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_role == 0) return 0;
		return OS.call (objectClass.get_role, handle);
	}

	int atkObject_ref_child (int index) {
		updateChildren ();
		AccessibleObject accObject = getChildByIndex (index);	
		if (accObject != null) {
			OS.g_object_ref (accObject.handle);	
			return accObject.handle;
		}
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.ref_child == 0) return 0;
		return OS.call (objectClass.ref_child, handle, index);
	}

	int atkObject_ref_state_set  () {
		if (accessible.getControlListeners ().length != 0) {
			int set = ATK.atk_state_set_new ();
			AccessibleControlListener[] listeners = accessible.getControlListeners ();
			AccessibleControlEvent event = new AccessibleControlEvent (this);
			event.childID = id;
			event.detail = -1;
			for (int i = 0; i < listeners.length; i++) {
				listeners [i].getState (event);
			} 
			if (event.detail != -1) {
				//	Convert from win32 state values to atk state values
				int state = event.detail;
				if ((state & ACC.STATE_BUSY) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_BUSY);
				if ((state & ACC.STATE_CHECKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_CHECKED);
				if ((state & ACC.STATE_EXPANDED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EXPANDED);
				if ((state & ACC.STATE_FOCUSABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSABLE);
				if ((state & ACC.STATE_FOCUSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSED);
				if ((state & ACC.STATE_HOTTRACKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ARMED); ;
				if ((state & ACC.STATE_INVISIBLE) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_VISIBLE);
				if ((state & ACC.STATE_MULTISELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_MULTISELECTABLE);
				if ((state & ACC.STATE_OFFSCREEN) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SHOWING);												
				if ((state & ACC.STATE_PRESSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_PRESSED);
				if ((state & ACC.STATE_READONLY) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EDITABLE);
				if ((state & ACC.STATE_SELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTABLE);
				if ((state & ACC.STATE_SELECTED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTED);
				if ((state & ACC.STATE_SIZEABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_RESIZABLE);
				// Note: STATE_COLLAPSED and STATE_NORMAL have no ATK equivalents
				return set;
			}	
		}
		int superType = OS.g_type_class_peek (parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.ref_state_set == 0) return 0;
		return OS.call (objectClass.ref_state_set, handle);
	}

	int atkSelection_is_child_selected (int index) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_SELECTION_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (handle));
			AtkSelectionIface selectionIface = new AtkSelectionIface ();
			ATK.memmove (selectionIface, superType);
			if (selectionIface.is_child_selected != 0) {
				parentResult = OS.call (selectionIface.is_child_selected, handle, index);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelection (event);
		}
		AccessibleObject accessibleObject = getChildByID (event.childID);
		if (accessibleObject != null) { 
			return accessibleObject.index == index ? 1 : 0;
		}
		return parentResult;
	}

	int atkSelection_ref_selection (int index) {
		int parentResult = 0;
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_SELECTION_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (handle));
			AtkSelectionIface selectionIface = new AtkSelectionIface ();
			ATK.memmove (selectionIface, superType);
			if (selectionIface.ref_selection != 0) {
				parentResult = OS.call (selectionIface.ref_selection, handle, index);
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelection (event);
		} 
		AccessibleObject accObj = getChildByID (event.childID);
		if (accObj != null) {
			if (parentResult > 0) OS.g_object_unref (parentResult);
			OS.g_object_ref (accObj.handle);	
			return accObj.handle;
		}
		return parentResult;
	}

	int atkText_get_character_at_offset (int offset) {
		String text = getText ();
		if (text != null) return (int)text.charAt (offset); // TODO
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_character_at_offset != 0) {
				return OS.call (textIface.get_character_at_offset, handle, offset);
			}
		}
		return 0;
	}

	int atkText_get_character_count () {
		String text = getText ();
		if (text != null) return text.length ();
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_character_count != 0) {
				return OS.call (textIface.get_character_count, handle);
			}
		}
		return 0;
	}

	int atkText_get_text (int start_offset, int end_offset) {
		String text = getText ();
		if (text != null) {
			if (end_offset == -1) {
				end_offset = text.length ();
			} else {
				end_offset = Math.min (end_offset, text.length ());	
			}
			start_offset = Math.min (start_offset, end_offset);
			text = text.substring (start_offset, end_offset);
			byte[] bytes = Converter.wcsToMbcs (null, text, true);
//			TODO gnopernicus bug? freeing previous string can cause gp
//			if (textPtr != -1) OS.g_free (textPtr);
			textPtr = OS.g_malloc (bytes.length);
			OS.memmove (textPtr, bytes, bytes.length);
			return textPtr;
		}
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_text != 0) {
				return OS.call (textIface.get_text, handle, start_offset, end_offset);
			}
		}
		return 0;
	}

	int atkText_get_text_after_offset (int offset, int boundary_type, int start_offset, int end_offset) {
		String text = getText ();
		if (text != null) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch (boundary_type) {
				case ATK.ATK_TEXT_BOUNDARY_CHAR: {
					if (length > offset) endBounds++;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
					int wordStart1 = nextIndexOfChar (text, " !?.\n", offset - 1);
					if (wordStart1 == -1) {
						startBounds = endBounds = length;
						break;
					}
					wordStart1 = nextIndexOfNotChar (text, " !?.\n", wordStart1);
					if (wordStart1 == length) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = wordStart1;
					int wordStart2 = nextIndexOfChar (text, " !?.\n", wordStart1);
					if (wordStart2 == -1) {
						endBounds = length;
						break;
					}
					endBounds = nextIndexOfNotChar (text, " !?.\n", wordStart2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
					int previousWordEnd = previousIndexOfNotChar (text, " \n", offset); 
					if (previousWordEnd == -1 || previousWordEnd != offset - 1) {
						offset = nextIndexOfNotChar (text, " \n", offset);
					}
					if (offset == -1) {
						startBounds = endBounds = length;
						break;
					}
					int wordEnd1 = nextIndexOfChar (text, " !?.\n", offset);
					if (wordEnd1 == -1) {
						startBounds = endBounds = length;
						break;
					}
					wordEnd1 = nextIndexOfNotChar (text, "!?.", wordEnd1);
					if (wordEnd1 == length) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = wordEnd1;
					int wordEnd2 = nextIndexOfNotChar (text, " \n", wordEnd1);
					if (wordEnd2 == length) {
						startBounds = endBounds = length;
						break;
					}
					wordEnd2 = nextIndexOfChar (text, " !?.\n", wordEnd2);
					if (wordEnd2 == -1) {
						endBounds = length;
						break;
					}
					endBounds = nextIndexOfNotChar (text, "!?.", wordEnd2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
					int previousSentenceEnd = previousIndexOfChar (text, "!?.", offset);
					int previousText = previousIndexOfNotChar (text, " !?.\n", offset);
					int sentenceStart1 = 0;
					if (previousSentenceEnd >= previousText) {
						sentenceStart1 = nextIndexOfNotChar (text, " !?.\n", offset);
					} else {
						sentenceStart1 = nextIndexOfChar (text, "!?.", offset);
						if (sentenceStart1 == -1) {
							startBounds = endBounds = length;
							break;
						}
						sentenceStart1 = nextIndexOfNotChar (text, " !?.\n", sentenceStart1);
					}
					if (sentenceStart1 == length) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = sentenceStart1;
					int sentenceStart2 = nextIndexOfChar (text, "!?.", sentenceStart1);
					if (sentenceStart2 == -1) {
						endBounds = length;
						break;
					}
					endBounds = nextIndexOfNotChar (text, " !?.\n", sentenceStart2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
					int sentenceEnd1 = nextIndexOfChar (text, "!?.", offset);
					if (sentenceEnd1 == -1) {
						startBounds = endBounds = length;
						break;
					}
					sentenceEnd1 = nextIndexOfNotChar (text, "!?.", sentenceEnd1);
					if (sentenceEnd1 == length) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = sentenceEnd1;
					int sentenceEnd2 = nextIndexOfNotChar (text, " \n", sentenceEnd1);
					if (sentenceEnd2 == length) {
						startBounds = endBounds = length;
						break;
					}
					sentenceEnd2 = nextIndexOfChar (text, "!?.", sentenceEnd2);
					if (sentenceEnd2 == -1) {
						endBounds = length;
						break;
					}
					endBounds = nextIndexOfNotChar (text, "!?.", sentenceEnd2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
					int lineStart1 = text.indexOf ('\n', offset - 1);
					if (lineStart1 == -1) {
						startBounds = endBounds = length;
						break;
					}
					lineStart1 = nextIndexOfNotChar (text, "\n", lineStart1);
					if (lineStart1 == length) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = lineStart1;
					int lineStart2 = text.indexOf ('\n', lineStart1);
					if (lineStart2 == -1) {
						endBounds = length;
						break;
					}
					lineStart2 = nextIndexOfNotChar (text, "\n", lineStart2);
					endBounds = lineStart2;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
					int lineEnd1 = nextIndexOfChar (text, "\n", offset);
					if (lineEnd1 == -1) {
						startBounds = endBounds = length;
						break;
					}
					startBounds = lineEnd1;
					if (startBounds == length) {
						endBounds = length;
						break;
					}
					int lineEnd2 = nextIndexOfChar (text, "\n", lineEnd1 + 1);
					if (lineEnd2 == -1) {
						endBounds = length;
						break;
					}
					endBounds = lineEnd2;
					break;
				}
			}
			OS.memmove (start_offset, new int[] {startBounds}, 4);
			OS.memmove (end_offset, new int[] {endBounds}, 4);
			text = text.substring (startBounds, endBounds);
			byte[] bytes = Converter.wcsToMbcs (null, text, true);
//			TODO gnopernicus bug? freeing previous string can cause gp
//			if (textPtr != -1) OS.g_free (textPtr);
			textPtr = OS.g_malloc (bytes.length);
			OS.memmove (textPtr, bytes, bytes.length);
			return textPtr;
		} 
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_text_after_offset != 0) {
				return OS.call (textIface.get_text_after_offset, handle, offset, boundary_type, start_offset, end_offset);
			}
		}
		return 0;
	}

	int atkText_get_text_at_offset (int offset, int boundary_type, int start_offset, int end_offset) {
		String text = getText ();
		if (text != null) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch (boundary_type) {
				case ATK.ATK_TEXT_BOUNDARY_CHAR: {
					if (length > offset) endBounds++;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
					int wordStart1 = previousIndexOfNotChar (text, " !?.\n", offset);
					if (wordStart1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					wordStart1 = previousIndexOfChar (text, " !?.\n", wordStart1) + 1;
					if (wordStart1 == -1) {
						startBounds = 0;
						break;
					}
					startBounds = wordStart1;
					int wordStart2 = nextIndexOfChar (text, " !?.\n", wordStart1);
					endBounds = nextIndexOfNotChar (text, " !?.\n", wordStart2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
					int wordEnd1 = previousIndexOfNotChar (text, "!?.", offset + 1);
					wordEnd1 = previousIndexOfChar (text, " !?.\n", wordEnd1);
					wordEnd1 = previousIndexOfNotChar (text, " \n", wordEnd1 + 1);
					if (wordEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					startBounds = wordEnd1 + 1;
					int wordEnd2 = nextIndexOfNotChar (text, " \n", startBounds);
					if (wordEnd2 == length) {
						endBounds = startBounds;
						break;
					}
					wordEnd2 = nextIndexOfChar (text, " !?.\n", wordEnd2);
					if (wordEnd2 == -1) {
						endBounds = startBounds;
						break;
					}
					endBounds = nextIndexOfNotChar (text, "!?.", wordEnd2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
					int sentenceStart1 = previousIndexOfNotChar (text, " !?.\n", offset + 1);
					if (sentenceStart1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					sentenceStart1 = previousIndexOfChar (text, "!?.", sentenceStart1) + 1;
					startBounds = nextIndexOfNotChar (text, " \n", sentenceStart1);
					int sentenceStart2 = nextIndexOfChar (text, "!?.", startBounds);
					endBounds = nextIndexOfNotChar (text, " !?.\n", sentenceStart2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
					int sentenceEnd1 = previousIndexOfNotChar (text, "!?.", offset + 1);
					sentenceEnd1 = previousIndexOfChar (text, "!?.", sentenceEnd1);
					sentenceEnd1 = previousIndexOfNotChar (text, " \n", sentenceEnd1 + 1);
					if (sentenceEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					startBounds = sentenceEnd1 + 1;
					int sentenceEnd2 = nextIndexOfNotChar (text, " \n", startBounds);
					if (sentenceEnd2 == length) {
						endBounds = startBounds;
						break;
					}
					sentenceEnd2 = nextIndexOfChar (text, "!?.", sentenceEnd2);
					if (sentenceEnd2 == -1) {
						endBounds = startBounds;
						break;
					}
					endBounds = nextIndexOfNotChar (text, "!?.", sentenceEnd2);
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
					startBounds = previousIndexOfChar (text, "\n", offset) + 1;
					int lineEnd2 = nextIndexOfChar (text, "\n", startBounds);
					if (lineEnd2 < length) lineEnd2++;
					endBounds = lineEnd2;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
					int lineEnd1 = previousIndexOfChar (text, "\n", offset);
					if (lineEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					startBounds = lineEnd1;
					endBounds = nextIndexOfChar (text, "\n", lineEnd1 + 1);
				}
			}
			OS.memmove (start_offset, new int[] {startBounds}, 4);
			OS.memmove (end_offset, new int[] {endBounds}, 4);
			text = text.substring (startBounds, endBounds);
			byte[] bytes = Converter.wcsToMbcs (null, text, true);
//			TODO gnopernicus bug? freeing previous string can cause gp
//			if (textPtr != -1) OS.g_free (textPtr);
			textPtr = OS.g_malloc (bytes.length);
			OS.memmove (textPtr, bytes, bytes.length);
			return textPtr;
		} 
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_text_after_offset != 0) {
				return OS.call (textIface.get_text_after_offset, handle, offset, boundary_type, start_offset, end_offset);
			}
		}
		return 0;
	}

	int atkText_get_text_before_offset (int offset, int boundary_type, int start_offset, int end_offset) {
		String text = getText ();
		if (text != null) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch (boundary_type) {
				case ATK.ATK_TEXT_BOUNDARY_CHAR: {
					if (length >= offset && offset > 0) startBounds--;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_START: {
					int wordStart1 = previousIndexOfChar (text, " !?.\n", offset - 1);
					if (wordStart1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					int wordStart2 = previousIndexOfNotChar (text, " !?.\n", wordStart1);
					if (wordStart2 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = wordStart1 + 1;
					startBounds = previousIndexOfChar (text, " !?.\n", wordStart2) + 1;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_WORD_END: {
					int wordEnd1 = previousIndexOfChar (text, " !?.\n", offset);
					if (wordEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					wordEnd1 = previousIndexOfNotChar (text, " \n", wordEnd1 + 1);
					if (wordEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = wordEnd1 + 1;
					int wordEnd2 = previousIndexOfNotChar (text, " !?.\n", endBounds);
					wordEnd2 = previousIndexOfChar (text, " !?.\n", wordEnd2);
					if (wordEnd2 == -1) {
						startBounds = 0;
						break;
					}
					startBounds = previousIndexOfNotChar (text, " \n", wordEnd2 + 1) + 1;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: {
					int sentenceStart1 = previousIndexOfChar (text, "!?.", offset);
					if (sentenceStart1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					int sentenceStart2 = previousIndexOfNotChar (text, "!?.", sentenceStart1);
					if (sentenceStart2 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = sentenceStart1 + 1;
					startBounds = previousIndexOfChar (text, "!?.", sentenceStart2) + 1;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: {
					int sentenceEnd1 = previousIndexOfChar (text, "!?.", offset);
					if (sentenceEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					sentenceEnd1 = previousIndexOfNotChar (text, " \n", sentenceEnd1 + 1);
					if (sentenceEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = sentenceEnd1 + 1;
					int sentenceEnd2 = previousIndexOfNotChar (text, "!?.", endBounds);
					sentenceEnd2 = previousIndexOfChar (text, "!?.", sentenceEnd2);
					if (sentenceEnd2 == -1) {
						startBounds = 0;
						break;
					}
					startBounds = previousIndexOfNotChar (text, " \n", sentenceEnd2 + 1) + 1;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_START: {
					int lineStart1 = previousIndexOfChar (text, "\n", offset);
					if (lineStart1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = lineStart1 + 1;
					startBounds = previousIndexOfChar (text, "\n", lineStart1) + 1;
					break;
				}
				case ATK.ATK_TEXT_BOUNDARY_LINE_END: {
					int lineEnd1 = previousIndexOfChar (text, "\n", offset);
					if (lineEnd1 == -1) {
						startBounds = endBounds = 0;
						break;
					}
					endBounds = lineEnd1;
					startBounds = previousIndexOfChar (text, "\n", lineEnd1);
					if (startBounds == -1) startBounds = 0;
					break;
				}
			}
			OS.memmove (start_offset, new int[] {startBounds}, 4);
			OS.memmove (end_offset, new int[] {endBounds}, 4);
			text = text.substring (startBounds, endBounds);
			byte[] bytes = Converter.wcsToMbcs (null, text, true);
//			TODO gnopernicus bug? freeing previous string can cause gp
//			if (textPtr != -1) OS.g_free (textPtr);
			textPtr = OS.g_malloc (bytes.length);
			OS.memmove (textPtr, bytes, bytes.length);
			return textPtr;
		} 
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_class_peek (parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_text_before_offset != 0) {
				return OS.call (textIface.get_text_before_offset, handle, offset, boundary_type, start_offset, end_offset);
			}
		}
		return 0;
	}

	void dispose () {
		if (DEBUG) System.out.println("AccessibleObject.dispose: " + handle);
		Enumeration elements = children.elements ();
		while (elements.hasMoreElements ()) {
			AccessibleObject child = (AccessibleObject) elements.nextElement ();
			if (child.isLightweight) OS.g_object_unref (child.handle);
		}
		if (parent != null) parent.removeChild (this, false);
		if (namePtr != -1) OS.g_free (namePtr);
		namePtr = -1;
		if (descriptionPtr != -1) OS.g_free (descriptionPtr);
		descriptionPtr = -1;
		if (keybindingPtr != -1) OS.g_free (keybindingPtr);
		keybindingPtr = -1;
		if (actionNamePtr != -1) OS.g_free (actionNamePtr);
		actionNamePtr = -1;
		if (textPtr != -1) OS.g_free (textPtr);
		textPtr = -1;
	}

	AccessibleObject getChildByHandle (int handle) {
		return (AccessibleObject) children.get (new Integer (handle));	
	}	

	AccessibleObject getChildByID (int childId) {
		if (childId == ACC.CHILDID_SELF) return this;
		Enumeration elements = children.elements ();
		while (elements.hasMoreElements ()) {
			AccessibleObject object = (AccessibleObject) elements.nextElement ();
			if (object.id == childId) return object;
		}
		return null;
	}
	
	AccessibleObject getChildByIndex (int childIndex) {
		Enumeration elements = children.elements ();
		while (elements.hasMoreElements ()) {
			AccessibleObject object = (AccessibleObject) elements.nextElement ();
			if (object.index == childIndex) return object;
		}
		return null;
	}

	String getText () {
		int parentResult = 0;
		String parentText = "";
		if (OS.g_type_is_a (parentType, AccessibleType.ATK_TEXT_TYPE)) {
			int superType = OS.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (handle));
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			int characterCount = 0;
			if (textIface.get_character_count != 0) {
				characterCount = OS.call (textIface.get_character_count, handle);
			}
			if (characterCount > 0 && textIface.get_text != 0) {
				parentResult = OS.call (textIface.get_text, handle, 0, characterCount);
				if (parentResult != 0) {
					int length = OS.strlen (parentResult);
					byte [] buffer = new byte [length];
					OS.memmove (buffer, parentResult, length);
					parentText = new String (Converter.mbcsToWcs (null, buffer));
				}
			}
		}
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return parentText;
		
		AccessibleControlEvent event = new AccessibleControlEvent (this);
		event.childID = id;
		event.result = parentText;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getValue (event);				
		} 
		return event.result;
	}

	int nextIndexOfChar (String string, String searchChars, int startIndex) {
		int result = string.length ();
		for (int i = 0; i < searchChars.length (); i++) {
			char current = searchChars.charAt (i);
			int index = string.indexOf (current, startIndex);
			if (index != -1) result = Math.min (result, index);
		}
		return result;
	}

	int nextIndexOfNotChar (String string, String searchChars, int startIndex) {
		int length = string.length ();
		int index = startIndex; 
		while (index < length) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break; 
			index++;
		}
		return index;
	}

	int previousIndexOfChar (String string, String searchChars, int startIndex) {
		int result = -1;
		if (startIndex < 0) return result;
		string = string.substring (0, startIndex);
		for (int i = 0; i < searchChars.length (); i++) {
			char current = searchChars.charAt (i);
			int index = string.lastIndexOf (current);
			if (index != -1) result = Math.max (result, index);
		}
		return result;
	}

	int previousIndexOfNotChar (String string, String searchChars, int startIndex) {
		if (startIndex < 0) return -1;
		int index = startIndex - 1; 
		while (index >= 0) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break; 
			index--;
		}
		return index;
	}

	void removeChild (AccessibleObject child, boolean unref) {
		children.remove (new Integer (child.handle));
		if (unref && child.isLightweight) OS.g_object_unref (child.handle);
	}

	void setFocusToChild (int childId) {
		updateChildren ();
		AccessibleObject accObject = getChildByID (childId);
		if (accObject != null) {
			ATK.atk_focus_tracker_notify (accObject.handle);
		}
	}
	
	void setParent (AccessibleObject parent) {
		this.parent = parent;
	}
	
	void updateChildren () {
		AccessibleControlListener[] listeners = accessible.getControlListeners ();
		if (listeners.length == 0) return;

		AccessibleControlEvent event = new AccessibleControlEvent (this);
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildren (event);
		}
		if (event.children != null && event.children.length > 0) {
			Hashtable childrenCopy = (Hashtable)children.clone ();
			if (event.children [0] instanceof Integer) {
				//	an array of child id's (Integers) was answered
				AccessibleType childType = AccessibleFactory.getChildType ();
				for (int i = 0; i < event.children.length; i++) {
					AccessibleObject object = getChildByIndex (i);
					if (object == null) {
						object = new AccessibleObject (childType.handle, 0, accessible, parentType, true);
						AccessibleType.addInstance (object);
						addChild (object);
						object.index = i;
					}
					try {
						object.id = ((Integer)event.children[i]).intValue ();
					} catch (ClassCastException e) {
						// a non-ID value was given so don't set the ID
					}
					childrenCopy.remove (new Integer (object.handle));
				}
			} else {
				// an array of Accessible children was answered
				for (int i = 0; i < event.children.length; i++) {
					AccessibleObject object = null;
					try {
						object = ((Accessible)event.children [i]).accessibleObject;
					} catch (ClassCastException e) {
						// a non-Accessible value was given so nothing to do here 
					}
					object.index = i;
					childrenCopy.remove (new Integer (object.handle));
				}
			}
			// remove previous children of self which were not answered
			Enumeration childrenToRemove = childrenCopy.elements ();
			while (childrenToRemove.hasMoreElements ()) {
				AccessibleObject object = (AccessibleObject) childrenToRemove.nextElement (); 
				removeChild (object, true);
			}
		}
	}
}
