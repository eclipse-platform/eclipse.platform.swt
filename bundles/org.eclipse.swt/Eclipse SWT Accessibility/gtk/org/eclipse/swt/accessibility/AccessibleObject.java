/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;

class AccessibleObject {
	int /*long*/ handle;
	int /*long*/ parentType;
	int index = -1, id = ACC.CHILDID_SELF;
	Accessible accessible;
	AccessibleObject parent;
	Hashtable children = new Hashtable (9);
	/*
	* a lightweight object does not correspond to a concrete gtk widget, but
	* to a logical child of a widget (eg.- a CTabItem, which is simply drawn)
	*/ 
	boolean isLightweight = false;

	static int /*long*/ actionNamePtr = -1;
	static int /*long*/ descriptionPtr = -1;
	static int /*long*/ keybindingPtr = -1;
	static int /*long*/ namePtr = -1;
	static final Hashtable AccessibleObjects = new Hashtable (9);
	static final int /*long*/ ATK_ACTION_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkAction", true));
	static final int /*long*/ ATK_COMPONENT_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkComponent", true));
	static final int /*long*/ ATK_HYPERTEXT_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkHypertext", true));
	static final int /*long*/ ATK_SELECTION_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkSelection", true));		
	static final int /*long*/ ATK_TEXT_TYPE = ATK.g_type_from_name (Converter.wcsToMbcs (null, "AtkText", true));
	static final boolean DEBUG = Display.DEBUG;

	AccessibleObject (int /*long*/ type, int /*long*/ widget, Accessible accessible, int /*long*/ parentType, boolean isLightweight) {
		super ();
		handle = ATK.g_object_new (type, 0);
		this.parentType = parentType;
		ATK.atk_object_initialize (handle, widget);
		this.accessible = accessible;
		this.isLightweight = isLightweight;
		AccessibleObjects.put (new LONG (handle), this);
		if (DEBUG) System.out.println("new AccessibleObject: " + handle);
	}

	void addChild (AccessibleObject child) {
		children.put (new LONG (child.handle), child);		
		child.setParent (this);
	}
	
	static int /*long*/ atkAction_get_keybinding (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) System.out.println ("-->atkAction_get_keybinding");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_ACTION_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (object.handle));
			AtkActionIface actionIface = new AtkActionIface ();
			ATK.memmove (actionIface, superType);
			if (actionIface.get_keybinding != 0) {
				parentResult = ATK.call (actionIface.get_keybinding, object.handle, index);
			}
		}
		AccessibleListener[] listeners = object.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;

		AccessibleEvent event = new AccessibleEvent (object.accessible);
		event.childID = object.id;
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

	static int /*long*/ atkAction_get_name (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) System.out.println ("-->atkAction_get_name");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_ACTION_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (object.handle));
			AtkActionIface actionIface = new AtkActionIface ();
			ATK.memmove (actionIface, superType);
			if (actionIface.get_name != 0) {
				parentResult = ATK.call (actionIface.get_name, object.handle, index);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;

		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
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

	static int /*long*/ atkComponent_get_extents (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ width, int /*long*/ height, int /*long*/ coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_extents");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		OS.memmove (x, new int[] {0}, 4);
		OS.memmove (y, new int[] {0}, 4);
		OS.memmove (width, new int[] {0}, 4);
		OS.memmove (height, new int[] {0}, 4);
		if (ATK.g_type_is_a (object.parentType, ATK_COMPONENT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (object.handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				ATK.call (componentIface.get_extents, object.handle, x, y, width, height, coord_type);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return 0;
		
		int[] parentX = new int [1], parentY = new int [1];
		int[] parentWidth = new int [1], parentHeight = new int [1];
		OS.memmove (parentX, x, 4);
		OS.memmove (parentY, y, 4);
		OS.memmove (parentWidth, width, 4);
		OS.memmove (parentHeight, height, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.x = parentX [0]; event.y = parentY [0];
		event.width = parentWidth [0]; event.height = parentHeight [0];
		if (coord_type == ATK.ATK_XY_WINDOW) {
			/* translate control -> display, for filling in event to be dispatched */
			int /*long*/ gtkAccessibleHandle = ATK.GTK_ACCESSIBLE (object.handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			ATK.memmove (gtkAccessible, gtkAccessibleHandle);
			int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		if (coord_type == ATK.ATK_XY_WINDOW) {
			/* translate display -> control, for answering to the OS */ 
			int /*long*/ gtkAccessibleHandle = ATK.GTK_ACCESSIBLE (object.handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			ATK.memmove (gtkAccessible, gtkAccessibleHandle);
			int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);
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

	static int /*long*/ atkComponent_get_position (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_position, object: " + atkObject + " x: " + x + " y: " + y + " coord: " + coord_type);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		OS.memmove (x, new int[] {0}, 4);
		OS.memmove (y, new int[] {0}, 4);
		if (ATK.g_type_is_a (object.parentType, ATK_COMPONENT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (object.handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				ATK.call (componentIface.get_position, object.handle, x, y, coord_type);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return 0;
		
		int[] parentX = new int [1], parentY = new int [1];
		OS.memmove (parentX, x, 4);
		OS.memmove (parentY, y, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.x = parentX [0]; event.y = parentY [0];
		if (coord_type == ATK.ATK_XY_WINDOW) {
			/* translate control -> display, for filling in event to be dispatched */
			int /*long*/ gtkAccessibleHandle = ATK.GTK_ACCESSIBLE (object.handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			ATK.memmove (gtkAccessible, gtkAccessibleHandle);
			int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		if (coord_type == ATK.ATK_XY_WINDOW) {
			/* translate display -> control, for answering to the OS */ 
			int /*long*/ gtkAccessibleHandle = ATK.GTK_ACCESSIBLE (object.handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			ATK.memmove (gtkAccessible, gtkAccessibleHandle);
			int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x -= topWindowX [0];
			event.y -= topWindowY [0];
		}
		OS.memmove (x, new int[] {event.x}, 4);
		OS.memmove (y, new int[] {event.y}, 4);
		return 0;
	}

	static int /*long*/ atkComponent_get_size (int /*long*/ atkObject, int /*long*/ width, int /*long*/ height, int /*long*/ coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_get_size");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		OS.memmove (width, new int[] {0}, 4);
		OS.memmove (height, new int[] {0}, 4);
		if (ATK.g_type_is_a (object.parentType, ATK_COMPONENT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (object.handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.get_extents != 0) {
				ATK.call (componentIface.get_size, object.handle, width, height, coord_type);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return 0;
		
		int[] parentWidth = new int [1], parentHeight = new int [1];
		OS.memmove (parentWidth, width, 4);
		OS.memmove (parentHeight, height, 4);
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.width = parentWidth [0]; event.height = parentHeight [0];
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getLocation (event);
		}
		OS.memmove (width, new int[] {event.width}, 4);
		OS.memmove (height, new int[] {event.height}, 4);
		return 0;
	}

	static int /*long*/ atkComponent_ref_accessible_at_point (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ coord_type) {
		if (DEBUG) System.out.println ("-->atkComponent_ref_accessible_at_point");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_COMPONENT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (object.handle));
			AtkComponentIface componentIface = new AtkComponentIface ();
			ATK.memmove (componentIface, superType);
			if (componentIface.ref_accessible_at_point != 0) {
				parentResult = ATK.call (componentIface.ref_accessible_at_point, object.handle, x, y, coord_type);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;
		
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.x = (int)/*64*/x; event.y = (int)/*64*/y;
		if (coord_type == ATK.ATK_XY_WINDOW) {
			/* translate control -> display, for filling in the event to be dispatched */
			int /*long*/ gtkAccessibleHandle = ATK.GTK_ACCESSIBLE (object.handle);
			GtkAccessible gtkAccessible = new GtkAccessible ();
			ATK.memmove (gtkAccessible, gtkAccessibleHandle);
			int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);				
			int[] topWindowX = new int [1], topWindowY = new int [1];
			OS.gdk_window_get_origin (window, topWindowX, topWindowY);
			event.x += topWindowX [0];
			event.y += topWindowY [0]; 
		}
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildAtPoint (event);				
		}
		if (event.childID == object.id) event.childID = ACC.CHILDID_SELF;
		AccessibleObject accObj = object.getChildByID (event.childID);
		if (accObj != null) {
			if (parentResult > 0) OS.g_object_unref (parentResult);
			OS.g_object_ref (accObj.handle);	
			return accObj.handle;
		}
		return parentResult;
	}	

	static int /*long*/ atkHypertext_get_link (int /*long*/ atkObject, int /*long*/ link_index) {
		if (DEBUG) System.out.println ("-->atkHypertext_get_link");
		return 0;
	}

	static int /*long*/ atkHypertext_get_n_links (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkHypertext_get_n_links");
		return 0;	/* read hyperlink's name */
	}

	static int /*long*/ atkHypertext_get_link_index (int /*long*/ atkObject, int /*long*/ char_index) {
		if (DEBUG) System.out.println ("-->atkHypertext_get_link_index");
		return 0;
	}

	static int /*long*/ atkObject_get_description (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_description != 0) {
			parentResult = ATK.call (objectClass.get_description, object.handle);
		}
		AccessibleListener[] listeners = object.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleEvent event = new AccessibleEvent (object.accessible);
		event.childID = object.id;
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

	static int /*long*/ atkObject_get_name (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_name: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_name != 0) {
			parentResult = ATK.call (objectClass.get_name, object.handle);
		}
		AccessibleListener[] listeners = object.getAccessibleListeners ();
		if (listeners.length == 0) return parentResult;
		
		AccessibleEvent event = new AccessibleEvent (object.accessible);
		event.childID = object.id;
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

	static int /*long*/ atkObject_get_n_children (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_n_children: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_n_children != 0) { 
			parentResult = ATK.call (objectClass.get_n_children, object.handle);
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.detail = (int)/*64*/parentResult;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildCount (event);
		} 
		return event.detail;
	}

	static int /*long*/ atkObject_get_index_in_parent (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObjectCB_get_index_in_parent.  ");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		if (object.index != -1) return object.index;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_index_in_parent == 0) return 0;
		return ATK.call (objectClass.get_index_in_parent,object. handle);
	}

	static int /*long*/ atkObject_get_parent (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_parent: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		if (object.parent != null) return object.parent.handle;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_parent == 0) return 0;
		return ATK.call (objectClass.get_parent, object.handle);
	}

	static int /*long*/ atkObject_get_role (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_get_role: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		if (object.getAccessibleListeners ().length != 0) {
			AccessibleControlListener[] listeners = object.getControlListeners ();
			AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
			event.childID = object.id;
			event.detail = -1;
			for (int i = 0; i < listeners.length; i++) {
				listeners [i].getRole (event);				
			} 
			if (event.detail != -1) {
				switch (event.detail) {
					/* Convert from win32 role values to atk role values */
					case ACC.ROLE_CHECKBUTTON: return ATK.ATK_ROLE_CHECK_BOX;
					case ACC.ROLE_CLIENT_AREA: return ATK.ATK_ROLE_DRAWING_AREA;
					case ACC.ROLE_COMBOBOX: return ATK.ATK_ROLE_COMBO_BOX;
					case ACC.ROLE_DIALOG: return ATK.ATK_ROLE_DIALOG;
					case ACC.ROLE_LABEL: return ATK.ATK_ROLE_LABEL;
					case ACC.ROLE_LINK: return ATK.ATK_ROLE_TEXT;
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
					case ACC.ROLE_TABLE: return ATK.ATK_ROLE_LIST;
					case ACC.ROLE_TABLECELL: return ATK.ATK_ROLE_LIST_ITEM;
					case ACC.ROLE_TABLECOLUMNHEADER: return ATK.ATK_ROLE_TABLE_COLUMN_HEADER;
					case ACC.ROLE_TABLEROWHEADER: return ATK.ATK_ROLE_TABLE_ROW_HEADER;
					case ACC.ROLE_TABFOLDER: return ATK.ATK_ROLE_PAGE_TAB_LIST;
					case ACC.ROLE_TABITEM: return ATK.ATK_ROLE_PAGE_TAB;
					case ACC.ROLE_TEXT: return ATK.ATK_ROLE_TEXT;
					case ACC.ROLE_TOOLBAR: return ATK.ATK_ROLE_TOOL_BAR;
					case ACC.ROLE_TOOLTIP: return ATK.ATK_ROLE_TOOL_TIP;
					case ACC.ROLE_TREE: return ATK.ATK_ROLE_TREE;
					case ACC.ROLE_TREEITEM: return ATK.ATK_ROLE_LIST_ITEM;
					case ACC.ROLE_RADIOBUTTON: return ATK.ATK_ROLE_RADIO_BUTTON;
					case ACC.ROLE_SPLITBUTTON: return ATK.ATK_ROLE_PUSH_BUTTON;
					case ACC.ROLE_WINDOW: return ATK.ATK_ROLE_WINDOW;
				}
			}
		} 
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.get_role == 0) return 0;
		return ATK.call (objectClass.get_role, object.handle);
	}

	static int /*long*/ atkObject_ref_child (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) System.out.println ("-->atkObject_ref_child: " + index + " of: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		object.updateChildren ();
		AccessibleObject accObject = object.getChildByIndex ((int)/*64*/index);	
		if (accObject != null) {
			OS.g_object_ref (accObject.handle);	
			return accObject.handle;
		}
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.ref_child == 0) return 0;
		return ATK.call (objectClass.ref_child, object.handle, index);
	}

	static int /*long*/ atkObject_ref_state_set (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkObject_ref_state_set");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, superType);
		if (objectClass.ref_state_set != 0) { 
			parentResult = ATK.call (objectClass.ref_state_set, object.handle);
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;

		int /*long*/ set = parentResult;
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		event.detail = -1;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getState (event);
		} 
		if (event.detail != -1) {
			/*	Convert from win32 state values to atk state values */
			int state = event.detail;
			if ((state & ACC.STATE_BUSY) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_BUSY);
			if ((state & ACC.STATE_CHECKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_CHECKED);
			if ((state & ACC.STATE_EXPANDED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EXPANDED);
			if ((state & ACC.STATE_FOCUSABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSABLE);
			if ((state & ACC.STATE_FOCUSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_FOCUSED);
			if ((state & ACC.STATE_HOTTRACKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ARMED);
			if ((state & ACC.STATE_INVISIBLE) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_VISIBLE);
			if ((state & ACC.STATE_MULTISELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_MULTISELECTABLE);
			if ((state & ACC.STATE_OFFSCREEN) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SHOWING);												
			if ((state & ACC.STATE_PRESSED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_PRESSED);
			if ((state & ACC.STATE_READONLY) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_EDITABLE);
			if ((state & ACC.STATE_SELECTABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTABLE);
			if ((state & ACC.STATE_SELECTED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_SELECTED);
			if ((state & ACC.STATE_SIZEABLE) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_RESIZABLE);
			/* Note: STATE_COLLAPSED, STATE_LINKED and STATE_NORMAL have no ATK equivalents */
		}
		return set;
	}

	static int /*long*/ atkSelection_is_child_selected (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) System.out.println ("-->atkSelection_is_child_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_SELECTION_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (object.handle));
			AtkSelectionIface selectionIface = new AtkSelectionIface ();
			ATK.memmove (selectionIface, superType);
			if (selectionIface.is_child_selected != 0) {
				parentResult = ATK.call (selectionIface.is_child_selected, object.handle, index);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelection (event);
		}
		AccessibleObject accessibleObject = object.getChildByID (event.childID);
		if (accessibleObject != null) { 
			return accessibleObject.index == index ? 1 : 0;
		}
		return parentResult;
	}

	static int /*long*/ atkSelection_ref_selection (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) System.out.println ("-->atkSelection_ref_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_SELECTION_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (object.handle));
			AtkSelectionIface selectionIface = new AtkSelectionIface ();
			ATK.memmove (selectionIface, superType);
			if (selectionIface.ref_selection != 0) {
				parentResult = ATK.call (selectionIface.ref_selection, object.handle, index);
			}
		}
		AccessibleControlListener[] listeners = object.getControlListeners ();
		if (listeners.length == 0) return parentResult;
			
		AccessibleControlEvent event = new AccessibleControlEvent (object.accessible);
		event.childID = object.id;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelection (event);
		} 
		AccessibleObject accObj = object.getChildByID (event.childID);
		if (accObj != null) {
			if (parentResult > 0) OS.g_object_unref (parentResult);
			OS.g_object_ref (accObj.handle);	
			return accObj.handle;
		}
		return parentResult;
	}

	static int /*long*/ atkText_get_caret_offset (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkText_get_caret_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (object.handle));
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_caret_offset != 0) {
				parentResult = ATK.call (textIface.get_caret_offset, object.handle);
			}
		}
		AccessibleTextListener[] listeners = object.getTextListeners ();
		if (listeners.length == 0) return parentResult;
		
		AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
		event.childID = object.id;
		event.offset = (int)/*64*/parentResult;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getCaretOffset (event);	
		} 
		return event.offset; 	
	}
	
	static int /*long*/ atkText_get_character_at_offset (int /*long*/ atkObject, int /*long*/ offset) {
		if (DEBUG) System.out.println ("-->atkText_get_character_at_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		String text = object.getText ();
		if (text != null) return text.charAt ((int)/*64*/offset); // TODO
		if (ATK.g_type_is_a (object.parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_character_at_offset != 0) {
				return ATK.call (textIface.get_character_at_offset, object.handle, offset);
			}
		}
		return 0;
	}

	static int /*long*/ atkText_get_character_count (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkText_get_character_count");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		String text = object.getText ();
		if (text != null) return text.length ();
		if (ATK.g_type_is_a (object.parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_class_peek (object.parentType);
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_character_count != 0) {
				return ATK.call (textIface.get_character_count, object.handle);
			}
		}
		return 0;
	}

	static int /*long*/ atkText_get_n_selections (int /*long*/ atkObject) {
		if (DEBUG) System.out.println ("-->atkText_get_n_selections");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int /*long*/ parentResult = 0;
		if (ATK.g_type_is_a (object.parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (object.handle));
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_n_selections != 0) {
				parentResult = ATK.call (textIface.get_n_selections, object.handle);
			}
		}
		AccessibleTextListener[] listeners = object.getTextListeners ();
		if (listeners.length == 0) return parentResult;

		AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
		event.childID = object.id;
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelectionRange (event);
		}
		return event.length == 0 ? parentResult : 1;
	}

	static int /*long*/ atkText_get_selection (int /*long*/ atkObject, int /*long*/ selection_num, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		OS.memmove (start_offset, new int[] {0}, 4);
		OS.memmove (end_offset, new int[] {0}, 4);
		if (ATK.g_type_is_a (object.parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (object.handle));
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			if (textIface.get_selection != 0) {
				ATK.call (textIface.get_selection, object.handle, selection_num, start_offset, end_offset);
			}
		}
		AccessibleTextListener[] listeners = object.getTextListeners ();
		if (listeners.length == 0) return 0;

		AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
		event.childID = object.id;
		int[] parentStart = new int [1];
		int[] parentEnd = new int [1];
		OS.memmove (parentStart, start_offset, 4);
		OS.memmove (parentEnd, end_offset, 4);
		event.offset = parentStart [0];
		event.length = parentEnd [0] - parentStart [0];
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getSelectionRange (event);
		}
		OS.memmove (start_offset, new int[] {event.offset}, 4);
		OS.memmove (end_offset, new int[] {event.offset + event.length}, 4);
		return 0;
	}

	static int /*long*/ atkText_get_text (int /*long*/ atkObject, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text: " + start_offset + "," + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		String text = object.getText ();
		if (text.length () > 0) {
			if (end_offset == -1) {
				end_offset = text.length ();
			} else {
				end_offset = Math.min (end_offset, text.length ());	
			}
			start_offset = Math.min (start_offset, end_offset);
			text = text.substring ((int)/*64*/start_offset, (int)/*64*/end_offset);
			byte[] bytes = Converter.wcsToMbcs (null, text, true);
			int /*long*/ result = OS.g_malloc (bytes.length);
			OS.memmove (result, bytes, bytes.length);
			return result;
		}
		return 0;
	}

	static int /*long*/ atkText_get_text_after_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_after_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int offset = (int)/*64*/offset_value;
		String text = object.getText ();
		if (text.length () > 0) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch ((int)/*64*/boundary_type) {
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
					int wordEnd1 = nextIndexOfChar (text, " !?.\n", (int)/*64*/offset);
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
			int /*long*/ result = OS.g_malloc (bytes.length);
			OS.memmove (result, bytes, bytes.length);
			return result;
		} 
		return 0;
	}

	static int /*long*/ atkText_get_text_at_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_at_offset: " + offset_value + " start: " + start_offset + " end: " + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int offset = (int)/*64*/offset_value;
		String text = object.getText ();
		if (text.length () > 0) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch ((int)/*64*/boundary_type) {
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
			int /*long*/ result = OS.g_malloc (bytes.length);
			OS.memmove (result, bytes, bytes.length);
			return result;
		} 
		return 0;
	}

	static int /*long*/ atkText_get_text_before_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) System.out.println ("-->atkText_get_text_before_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object == null) return 0;
		int offset = (int)/*64*/offset_value;
		String text = object.getText ();
		if (text.length () > 0) {
			int length = text.length ();
			offset = Math.min (offset, length - 1);
			int startBounds = offset;
			int endBounds = offset;
			switch ((int)/*64*/boundary_type) {
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
					int wordEnd1 =previousIndexOfChar (text, " !?.\n", offset);
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
			int /*long*/ result = OS.g_malloc (bytes.length);
			OS.memmove (result, bytes, bytes.length);
			return result;
		} 
		return 0;
	}

	AccessibleListener[] getAccessibleListeners () {
		if (accessible == null) return new AccessibleListener [0];
		AccessibleListener[] result = accessible.getAccessibleListeners ();
		return result != null ? result : new AccessibleListener [0];
	}

	static AccessibleObject getAccessibleObject (int /*long*/ atkObject) {
		return (AccessibleObject)AccessibleObjects.get (new LONG (atkObject));
	}
	
	AccessibleObject getChildByHandle (int /*long*/ handle) {
		return (AccessibleObject) children.get (new LONG (handle));	
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
	
	AccessibleControlListener[] getControlListeners () {
		if (accessible == null) return new AccessibleControlListener [0];
		AccessibleControlListener[] result = accessible.getControlListeners (); 
		return result != null ? result : new AccessibleControlListener [0];
	}

	String getText () {
		int /*long*/ parentResult = 0;
		String parentText = "";	//$NON-NLS-1$
		if (ATK.g_type_is_a (parentType, ATK_TEXT_TYPE)) {
			int /*long*/ superType = ATK.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (handle));
			AtkTextIface textIface = new AtkTextIface ();
			ATK.memmove (textIface, superType);
			int /*long*/ characterCount = 0;
			if (textIface.get_character_count != 0) {
				characterCount = ATK.call (textIface.get_character_count, handle);
			}
			if (characterCount > 0 && textIface.get_text != 0) {
				parentResult = ATK.call (textIface.get_text, handle, 0, characterCount);
				if (parentResult != 0) {
					int length = OS.strlen (parentResult);
					byte [] buffer = new byte [length];
					OS.memmove (buffer, parentResult, length);
					parentText = new String (Converter.mbcsToWcs (null, buffer));
				}
			}
		}
		AccessibleControlListener[] controlListeners = getControlListeners ();
		if (controlListeners.length == 0) return parentText;
		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		event.childID = id;
		event.result = parentText;
		for (int i = 0; i < controlListeners.length; i++) {
			controlListeners [i].getValue (event);				
		}
		return event.result;
	}
	
	AccessibleTextListener[] getTextListeners () {
		if (accessible == null) return new AccessibleTextListener [0];
		AccessibleTextListener[] result = accessible.getTextListeners (); 
		return result != null ? result : new AccessibleTextListener [0];
	}

	static int /*long*/ gObjectClass_finalize (int /*long*/ atkObject) {
		int /*long*/ superType = ATK.g_type_class_peek_parent (ATK.G_OBJECT_GET_CLASS (atkObject));
		int /*long*/ gObjectClass = ATK.G_OBJECT_CLASS (superType);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		ATK.call (objectClassStruct.finalize, atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			AccessibleObjects.remove (new LONG (atkObject));
			object.release ();
		}
		return 0;
	}
	
	static int nextIndexOfChar (String string, String searchChars, int startIndex) {
		int result = string.length ();
		for (int i = 0; i < searchChars.length (); i++) {
			char current = searchChars.charAt (i);
			int index = string.indexOf (current, startIndex);
			if (index != -1) result = Math.min (result, index);
		}
		return result;
	}

	static int nextIndexOfNotChar (String string, String searchChars, int startIndex) {
		int length = string.length ();
		int index = startIndex; 
		while (index < length) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break; 
			index++;
		}
		return index;
	}

	static int previousIndexOfChar (String string, String searchChars, int startIndex) {
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

	static int previousIndexOfNotChar (String string, String searchChars, int startIndex) {
		if (startIndex < 0) return -1;
		int index = startIndex - 1; 
		while (index >= 0) {
			char current = string.charAt (index);
			if (searchChars.indexOf (current) == -1) break; 
			index--;
		}
		return index;
	}

	void release () {
		if (DEBUG) System.out.println("AccessibleObject.release: " + handle);
		accessible = null;
		Enumeration elements = children.elements ();
		while (elements.hasMoreElements ()) {
			AccessibleObject child = (AccessibleObject) elements.nextElement ();
			if (child.isLightweight) OS.g_object_unref (child.handle);
		}
		if (parent != null) parent.removeChild (this, false);
	}
	
	void removeChild (AccessibleObject child, boolean unref) {
		children.remove (new LONG (child.handle));
		if (unref && child.isLightweight) OS.g_object_unref (child.handle);
	}
	
	void selectionChanged () {
		OS.g_signal_emit_by_name (handle, ATK.selection_changed);
	}
	
	void setFocus (int childID) {
		updateChildren ();
		AccessibleObject accObject = getChildByID (childID);
		if (accObject != null) {
			ATK.atk_focus_tracker_notify (accObject.handle);
		}
	}

	void setParent (AccessibleObject parent) {
		this.parent = parent;
	}
	
	void textCaretMoved(int index) {
		OS.g_signal_emit_by_name (handle, ATK.text_caret_moved, index);
	}

	void textChanged(int type, int startIndex, int length) {
		if (type == ACC.TEXT_DELETE) {
			OS.g_signal_emit_by_name (handle, ATK.text_changed_delete, startIndex, length);
		} else {
			OS.g_signal_emit_by_name (handle, ATK.text_changed_insert, startIndex, length);
		}
	}

	void textSelectionChanged() {
		OS.g_signal_emit_by_name (handle, ATK.text_selection_changed);
	}

	void updateChildren () {
		if (isLightweight) return;
		AccessibleControlListener[] listeners = getControlListeners ();
		if (listeners.length == 0) return;

		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		for (int i = 0; i < listeners.length; i++) {
			listeners [i].getChildren (event);
		}
		if (event.children != null && event.children.length > 0) {
			Vector idsToKeep = new Vector (children.size ());
			if (event.children [0] instanceof Integer) {
				/*	an array of child id's (Integers) was answered */
				int /*long*/ parentType = AccessibleFactory.getDefaultParentType ();
				for (int i = 0; i < event.children.length; i++) {
					AccessibleObject object = getChildByIndex (i);
					if (object == null) {
						int /*long*/ childType = AccessibleFactory.getChildType (accessible, i);
						object = new AccessibleObject (childType, 0, accessible, parentType, true);
						AccessibleObjects.put (new LONG (object.handle), object);
						addChild (object);
						object.index = i;
					}
					try {
						object.id = ((Integer)event.children[i]).intValue ();
					} catch (ClassCastException e) {
						/* a non-ID value was given so don't set the ID */
					}
					idsToKeep.addElement (new LONG (object.handle));
				}
			} else {
				/* an array of Accessible children was answered */
				int childIndex = 0;
				for (int i = 0; i < event.children.length; i++) {
					AccessibleObject object = null;
					try {
						object = ((Accessible)event.children [i]).accessibleObject;
					} catch (ClassCastException e) {
						/* a non-Accessible value was given so nothing to do here */ 
					}
					if (object != null) {
						object.index = childIndex++;
						idsToKeep.addElement (new LONG (object.handle));
					}
				}
			}
			/* remove old children that were not provided as children anymore */
			Enumeration ids = children.keys ();
			while (ids.hasMoreElements ()) {
				LONG id = (LONG)ids.nextElement ();
				if (!idsToKeep.contains (id)) {
					AccessibleObject object = (AccessibleObject) children.get (id);
					removeChild (object, true);
				}
			}
		}
	}
}
