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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;

class AccessibleObject {
	int /*long*/ handle;
	int index = -1, id = ACC.CHILDID_SELF;
	Accessible accessible;
	AccessibleObject parent;
	AccessibleObject[] children;
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
	static final boolean DEBUG = Device.DEBUG;
	
	static final int ROW_ROLE, COLUMN_ROLE;
	static {
		ROW_ROLE = ATK.atk_role_register(Converter.wcsToMbcs(null, "row", true));
		COLUMN_ROLE = ATK.atk_role_register(Converter.wcsToMbcs(null, "column", true));
	}

	AccessibleObject (int /*long*/ type, int /*long*/ widget, Accessible accessible, boolean isLightweight) {
		super ();
		handle = ATK.g_object_new (type, 0);
		ATK.atk_object_initialize (handle, widget);
		this.accessible = accessible;
		this.isLightweight = isLightweight;
		AccessibleObjects.put (new LONG (handle), this);
		if (DEBUG) print("new AccessibleObject: " + handle + " control=" + accessible.control + " lw=" + isLightweight);
	}
	
	static void print (String str) {
		System.out.println (str);
	}
	
	static AtkActionIface getActionIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_ACTION())) {
			AtkActionIface iface = new AtkActionIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_ACTION_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	
	static int /*long*/ atkAction_do_action (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkAction_do_action");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleActionListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)/*64*/index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = (AccessibleActionListener) listeners.elementAt(i);
					listener.doAction(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkActionIface iface = getActionIface (atkObject);
		if (iface != null && iface.do_action != 0) {
			parentResult = ATK.call (iface.do_action, atkObject, index);
		}
		return parentResult; 	
	}

	static int /*long*/ atkAction_get_n_actions (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkAction_get_n_actions");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleActionListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = (AccessibleActionListener) listeners.elementAt(i);
					listener.getActionCount(event);
				}
				return event.count;
			}
		}
		int /*long*/ parentResult = 0;
		AtkActionIface iface = getActionIface (atkObject);
		if (iface != null && iface.get_n_actions != 0) {
			parentResult = ATK.call (iface.get_n_actions, atkObject);
		}
		return parentResult; 	
	}
	
	static int /*long*/ atkAction_get_description (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkAction_get_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleActionListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)/*64*/index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = (AccessibleActionListener) listeners.elementAt(i);
					listener.getDescription(event);
				}
				if (event.result == null) return 0;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		int /*long*/ parentResult = 0;
		AtkActionIface iface = getActionIface (atkObject);
		if (iface != null && iface.get_description != 0) {
			parentResult = ATK.call (iface.get_description, atkObject, index);
		}
		return parentResult; 	
	}
	
	static int /*long*/ atkAction_get_keybinding (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkAction_get_keybinding");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkActionIface iface = getActionIface (atkObject);
		if (iface != null && iface.get_keybinding != 0) {
			parentResult = ATK.call (iface.get_keybinding, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleActionListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)/*64*/index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = (AccessibleActionListener) listeners.elementAt(i);
					listener.getKeyBinding(event);
				}
				if (event.result != null) {
					if (keybindingPtr != -1) OS.g_free (keybindingPtr);
					return keybindingPtr = getStringPtr (event.result); 
				}
			}
			listeners = accessible.accessibleListeners;
			length = listeners.size();
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = (AccessibleListener) listeners.elementAt(i);
					listener.getKeyboardShortcut (event);				
				} 
				if (event.result != null) {
					if (keybindingPtr != -1) OS.g_free (keybindingPtr);
					return keybindingPtr = getStringPtr (event.result); 
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkAction_get_name (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkAction_get_name");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkActionIface iface = getActionIface (atkObject);
		if (iface != null && iface.get_name != 0) {
			parentResult = ATK.call (iface.get_name, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleActionListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleActionEvent event = new AccessibleActionEvent(accessible);
				event.index = (int)/*64*/index;
				for (int i = 0; i < length; i++) {
					AccessibleActionListener listener = (AccessibleActionListener) listeners.elementAt(i);
					listener.getName(event);
				}
				if (event.result != null) {
					if (actionNamePtr != -1) OS.g_free (actionNamePtr);
					return actionNamePtr = getStringPtr (event.result);
				}
			}
			if (index == 0) {
				listeners = accessible.accessibleControlListeners;
				length = listeners.size();
				if (length > 0) {
					AccessibleControlEvent event = new AccessibleControlEvent (accessible);
					event.childID = object.id;
					if (parentResult != 0) event.result = getString (parentResult);
					for (int i = 0; i < length; i++) {
						AccessibleControlListener listener = (AccessibleControlListener) listeners.elementAt(i);
						listener.getDefaultAction (event);				
					} 
					if (event.result != null) {
						if (actionNamePtr != -1) OS.g_free (actionNamePtr);
						return actionNamePtr = getStringPtr (event.result);
					}
				}
			}
		}
		return parentResult;
	}
	
	static AtkComponentIface getComponentIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_COMPONENT())) {
			AtkComponentIface iface = new AtkComponentIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_COMPONENT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}

	static int /*long*/ atkComponent_get_extents (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ width, int /*long*/ height, int /*long*/ coord_type) {
		if (DEBUG) print ("-->atkComponent_get_extents: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		OS.memmove (x, new int[] {0}, 4);
		OS.memmove (y, new int[] {0}, 4);
		OS.memmove (width, new int[] {0}, 4);
		OS.memmove (height, new int[] {0}, 4);
		AtkComponentIface iface = getComponentIface (atkObject);
		if (iface != null && iface.get_extents != 0) {
			ATK.call (iface.get_extents, atkObject, x, y, width, height, coord_type);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				int[] parentX = new int [1], parentY = new int [1];
				int[] parentWidth = new int [1], parentHeight = new int [1];
				OS.memmove (parentX, x, 4);
				OS.memmove (parentY, y, 4);
				OS.memmove (parentWidth, width, 4);
				OS.memmove (parentHeight, height, 4);
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.x = parentX [0]; event.y = parentY [0];
				event.width = parentWidth [0]; event.height = parentHeight [0];
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0]; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getLocation (event);
				}
				if (coord_type == ATK.ATK_XY_WINDOW) {
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				OS.memmove (x, new int[] {event.x}, 4);
				OS.memmove (y, new int[] {event.y}, 4);
				OS.memmove (width, new int[] {event.width}, 4);
				OS.memmove (height, new int[] {event.height}, 4);
				if (DEBUG) print("--->" + event.x + "," + event.y + "," + event.width + "x" + event.height);
			}
		}
		return 0;
	}

	static int /*long*/ atkComponent_get_position (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ coord_type) {
		if (DEBUG) print ("-->atkComponent_get_position, object: " + atkObject + " x: " + x + " y: " + y + " coord: " + coord_type);
		AccessibleObject object = getAccessibleObject (atkObject);
		OS.memmove (x, new int[] {0}, 4);
		OS.memmove (y, new int[] {0}, 4);
		AtkComponentIface iface = getComponentIface (atkObject);
		if (iface != null && iface.get_position != 0) {
			ATK.call (iface.get_position, atkObject, x, y, coord_type);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				int[] parentX = new int [1], parentY = new int [1];
				OS.memmove (parentX, x, 4);
				OS.memmove (parentY, y, 4);
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.x = parentX [0]; event.y = parentY [0];
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0]; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getLocation (event);
				}
				if (coord_type == ATK.ATK_XY_WINDOW) {
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				OS.memmove (x, new int[] {event.x}, 4);
				OS.memmove (y, new int[] {event.y}, 4);
			}
		}
		return 0;
	}

	static int /*long*/ atkComponent_get_size (int /*long*/ atkObject, int /*long*/ width, int /*long*/ height, int /*long*/ coord_type) {
		if (DEBUG) print ("-->atkComponent_get_size");
		AccessibleObject object = getAccessibleObject (atkObject);
		OS.memmove (width, new int[] {0}, 4);
		OS.memmove (height, new int[] {0}, 4);
		AtkComponentIface iface = getComponentIface (atkObject);
		if (iface != null && iface.get_size != 0) {
			ATK.call (iface.get_size, atkObject, width, height, coord_type);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				int[] parentWidth = new int [1], parentHeight = new int [1];
				OS.memmove (parentWidth, width, 4);
				OS.memmove (parentHeight, height, 4);
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.width = parentWidth [0]; event.height = parentHeight [0];
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getLocation (event);
				}
				OS.memmove (width, new int[] {event.width}, 4);
				OS.memmove (height, new int[] {event.height}, 4);
			}
		}
		return 0;
	}

	static int /*long*/ atkComponent_ref_accessible_at_point (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ coord_type) {
		if (DEBUG) print ("-->atkComponent_ref_accessible_at_point: " + atkObject + " " + x + "," + y);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.x = (int)/*64*/x; event.y = (int)/*64*/y;
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0]; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getChildAtPoint (event);				
				}
				if (event.childID == object.id) event.childID = ACC.CHILDID_SELF;
				Accessible result = event.accessible;
				AccessibleObject accObj = result != null ? result.getAccessibleObject() : object.getChildByID (event.childID);
				if (accObj != null) {
					return OS.g_object_ref (accObj.handle);	
				}
			}
		}
		int /*long*/ parentResult = 0;
		AtkComponentIface iface = getComponentIface (atkObject);
		if (iface != null && iface.ref_accessible_at_point != 0) {
			parentResult = ATK.call (iface.ref_accessible_at_point, atkObject, x, y, coord_type);
		}
		return parentResult;
	}	

	static AtkHypertextIface getHypertextIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_HYPERTEXT())) {
			AtkHypertextIface iface = new AtkHypertextIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_HYPERTEXT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	
	static int /*long*/ atkHypertext_get_link (int /*long*/ atkObject, int /*long*/ link_index) {
		if (DEBUG) print ("-->atkHypertext_get_link");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)/*int*/link_index;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getHyperlink(event);
				}
				Accessible result = event.accessible;
				return result != null ? result.getAccessibleObject().handle : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkHypertextIface iface = getHypertextIface (atkObject);
		if (iface != null && iface.get_link != 0) {
			parentResult = ATK.call (iface.get_link, atkObject, link_index);
		}
		return parentResult;
	}

	static int /*long*/ atkHypertext_get_n_links (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkHypertext_get_n_links");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getHyperlinkCount(event);
				}
				return event.count;
			}
		}
		int /*long*/ parentResult = 0;
		AtkHypertextIface iface = getHypertextIface (atkObject);
		if (iface != null && iface.get_n_links != 0) {
			parentResult = ATK.call (iface.get_n_links, atkObject);
		}
		return parentResult;
	}

	static int /*long*/ atkHypertext_get_link_index (int /*long*/ atkObject, int /*long*/ char_index) {
		if (DEBUG) print ("-->atkHypertext_get_link_index");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.offset = (int)/*int*/char_index;
				event.index = -1;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getHyperlinkIndex(event);
				}
				return event.index;
			}
		}
		int /*long*/ parentResult = 0;
		AtkHypertextIface iface = getHypertextIface (atkObject);
		if (iface != null && iface.get_link != 0) {
			parentResult = ATK.call (iface.get_link, atkObject, char_index);
		}
		return parentResult;
	}
	
	static AtkObjectClass getObjectClass (int /*long*/ atkObject) {
		AtkObjectClass objectClass = new AtkObjectClass ();
		ATK.memmove (objectClass, ATK.g_type_class_peek (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject))));
		return objectClass;
	}

	static int /*long*/ atkObject_get_description (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_description: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_description != 0) {
			parentResult = ATK.call (objectClass.get_description, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = (AccessibleListener)listeners.elementAt (i);
					listener.getDescription (event);
				}
				if (DEBUG) print ("---> " + event.result);
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	static int /*long*/ atkObject_get_name (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_name: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_name != 0) {
			parentResult = ATK.call (objectClass.get_name, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleEvent event = new AccessibleEvent (accessible);
				event.childID = object.id;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleListener listener = (AccessibleListener)listeners.elementAt (i);
					listener.getName (event);				
				}
				if (DEBUG) print ("---> " + event.result);
				if (event.result == null) return parentResult;
				if (namePtr != -1) OS.g_free (namePtr);
				return namePtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}	

	static int /*long*/ atkObject_get_n_children (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_n_children: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_n_children != 0) { 
			parentResult = ATK.call (objectClass.get_n_children, atkObject);
		}
		if (object != null && object.id == ACC.CHILDID_SELF) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = (int)/*64*/parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getChildCount (event);
				}
				if (DEBUG) print ("--->" + event.detail);
				return event.detail;
			}
		}
		return parentResult;
	}

	static int /*long*/ atkObject_get_index_in_parent (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_index_in_parent: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			AccessibleControlEvent event = new AccessibleControlEvent(accessible);
			event.childID = ACC.CHILDID_CHILD_INDEX;
			event.detail = -1;
			for (int i = 0; i < listeners.size(); i++) {
				AccessibleControlListener listener = (AccessibleControlListener) listeners.elementAt(i);
				listener.getChild(event);
			}
			if (event.detail != -1) {
				if (DEBUG) print ("---> " + object.index);
				return event.detail;
			}
			if (object.index != -1) {
				if (DEBUG) print ("---> " + object.index);
				return object.index;
			}
		}
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_index_in_parent == 0) return 0;
		int /*long*/ result = ATK.call (objectClass.get_index_in_parent, atkObject);
		if (DEBUG) print ("---*> " + result);
		return result;
	}

	static int /*long*/ atkObject_get_parent (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_parent: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			if (object.parent != null) {
				if (DEBUG) print ("---> " + object.parent.accessible.accessibleObject.handle);
				return object.parent.handle;
			}
		}
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_parent == 0) return 0;
		int /*long*/ parentResult = ATK.call (objectClass.get_parent, atkObject);
		if (DEBUG) print ("---> " + parentResult);
		return parentResult;
	}

	static int /*long*/ atkObject_get_role (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_get_role: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = -1;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getRole (event);				
				}
				if (DEBUG) print ("---> " + event.detail);
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
						case ACC.ROLE_TABLE: return ATK.ATK_ROLE_TABLE;
						case ACC.ROLE_TABLECELL: return ATK.ATK_ROLE_TABLE_CELL;
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
						case ACC.ROLE_ROW: return ROW_ROLE;
						case ACC.ROLE_COLUMN: return COLUMN_ROLE;
						case ACC.ROLE_ALERT: return ATK.ATK_ROLE_ALERT;
						case ACC.ROLE_ANIMATION: return ATK.ATK_ROLE_ANIMATION;
						case ACC.ROLE_CANVAS: return ATK.ATK_ROLE_CANVAS;
						case ACC.ROLE_GROUP: return ATK.ATK_ROLE_PANEL;
						case ACC.ROLE_SPINBUTTON: return ATK.ATK_ROLE_SPIN_BUTTON;
						case ACC.ROLE_STATUSBAR: return ATK.ATK_ROLE_STATUSBAR;
						case ACC.ROLE_CHECKMENUITEM: return ATK.ATK_ROLE_CHECK_MENU_ITEM;
						case ACC.ROLE_RADIOMENUITEM: return ATK.ATK_ROLE_RADIO_MENU_ITEM;
						case ACC.ROLE_CLOCK: return ATK.ATK_ROLE_UNKNOWN;
						case ACC.ROLE_CALENDAR: return ATK.ATK_ROLE_CALENDAR;
						case ACC.ROLE_DATETIME: return ATK.ATK_ROLE_DATE_EDITOR;
						case ACC.ROLE_FOOTER: return ATK.ATK_ROLE_FOOTER;
						case ACC.ROLE_FORM: return ATK.ATK_ROLE_FORM;
						case ACC.ROLE_HEADER: return ATK.ATK_ROLE_HEADER;
						case ACC.ROLE_HEADING: return ATK.ATK_ROLE_HEADING;
						case ACC.ROLE_PAGE: return ATK.ATK_ROLE_PAGE;
						case ACC.ROLE_PARAGRAPH: return ATK.ATK_ROLE_PARAGRAPH;
						case ACC.ROLE_SECTION: return ATK.ATK_ROLE_SECTION;
						case ACC.ROLE_DOCUMENT: return ATK.ATK_ROLE_DOCUMENT_FRAME;
						case ACC.ROLE_GRAPHIC: return ATK.ATK_ROLE_IMAGE;
					}
				}
			}
		}
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.get_role == 0) return 0;
		return ATK.call (objectClass.get_role, atkObject);
	}

	static int /*long*/ atkObject_ref_child (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkObject_ref_child: " + index + " of: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null && object.id == ACC.CHILDID_SELF) {
			object.updateChildren ();
			AccessibleObject accObject = object.getChildByIndex ((int)/*64*/index);	
			if (accObject != null) {
				return OS.g_object_ref (accObject.handle);	
			}
		}
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.ref_child == 0) return 0;
		return ATK.call (objectClass.ref_child, atkObject, index);
	}

	static int /*long*/ atkObject_ref_state_set (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkObject_ref_state_set: " + atkObject);
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkObjectClass objectClass = getObjectClass (atkObject);
		if (objectClass.ref_state_set != 0) { 
			parentResult = ATK.call (objectClass.ref_state_set, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {
				int /*long*/ set = parentResult;
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				event.detail = -1;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getState (event);
				} 
				if (event.detail != -1) {
					/*	Convert from win32 state values to atk state values */
					int state = event.detail;
					if ((state & ACC.STATE_BUSY) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_BUSY);
					if ((state & ACC.STATE_CHECKED) != 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_CHECKED);
					if ((state & ACC.STATE_DISABLED) == 0) ATK.atk_state_set_add_state (set, ATK.ATK_STATE_ENABLED);
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
		}
		return parentResult;
	}
	
	static AtkSelectionIface getSelectionIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_SELECTION())) {
			AtkSelectionIface iface = new AtkSelectionIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_SELECTION_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	
	static int /*long*/ atkSelection_is_child_selected (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkSelection_is_child_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkSelectionIface iface = getSelectionIface (atkObject);
		if (iface != null && iface.is_child_selected != 0) {
			parentResult = ATK.call (iface.is_child_selected, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {	
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getSelection (event);
				}
				Accessible result = event.accessible;
				AccessibleObject accessibleObject = result != null ? result.getAccessibleObject() : object.getChildByID (event.childID);
				if (accessibleObject != null) { 
					return accessibleObject.index == index ? 1 : 0;
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkSelection_ref_selection (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkSelection_ref_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkSelectionIface iface = getSelectionIface (atkObject);
		if (iface != null && iface.ref_selection != 0) {
			parentResult = ATK.call (iface.ref_selection, atkObject, index);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleControlListeners;
			int length = listeners.size();
			if (length > 0) {	
				AccessibleControlEvent event = new AccessibleControlEvent (accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
					listener.getSelection (event);
				} 
				AccessibleObject accObj = object.getChildByID (event.childID);
				if (accObj != null) {
					if (parentResult != 0) OS.g_object_unref (parentResult);
					OS.g_object_ref (accObj.handle);	
					return accObj.handle;
				}
			}
		}
		return parentResult;
	}

	static AtkTableIface getTableIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_TABLE())) {
			AtkTableIface iface = new AtkTableIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_TABLE_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	
	static int /*long*/ atkTable_ref_at (int /*long*/ atkObject, int /*long*/ row, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_ref_at");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					AccessibleObject accessibleObject = result.getAccessibleObject();
					OS.g_object_ref(accessibleObject.handle);
					return accessibleObject.handle;
				}
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.ref_at != 0) {
			parentResult = ATK.call (iface.ref_at, atkObject, row, column);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_index_at (int /*long*/ atkObject, int /*long*/ row, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_get_index_at");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result == null) return -1;
				event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getColumnCount(event);
				}
				return row * event.count + column;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_index_at != 0) {
			parentResult = ATK.call (iface.get_index_at, atkObject, row, column);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_column_at_index (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkTable_get_column_at_index: " + atkObject + " " + index);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getColumnCount(event);
				}
				int /*long*/ result = event.count == 0 ? -1 : index % event.count;
				if (DEBUG) print ("---> " + result);
				return result;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_column_at_index != 0) {
			parentResult = ATK.call (iface.get_column_at_index, atkObject, index);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_row_at_index (int /*long*/ atkObject, int /*long*/ index) {
		if (DEBUG) print ("-->atkTable_get_row_at_index: " + atkObject + " " + index);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getColumnCount(event);
				}
				int /*long*/ result = event.count == 0 ? -1 : index / event.count;
				if (DEBUG) print ("---> " + result);
				return result;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_row_at_index != 0) {
			parentResult = ATK.call (iface.get_row_at_index, atkObject, index);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_n_columns (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkTable_get_n_columns");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_n_columns != 0) {
			parentResult = ATK.call (iface.get_n_columns, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.count = (int)/*64*/parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getColumnCount(event);
					parentResult = event.count;
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_n_rows (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkTable_get_n_rows");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_n_rows != 0) {
			parentResult = ATK.call (iface.get_n_rows, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.count = (int)/*64*/parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getRowCount(event);
					parentResult = event.count;
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_column_extent_at (int /*long*/ atkObject, int /*long*/ row, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_get_column_extent_at");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_column_extent_at != 0) {
			parentResult = ATK.call (iface.get_column_extent_at, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					listeners = result.accessibleTableCellListeners;
					length = listeners.size();
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.count = (int)/*64*/parentResult;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = (AccessibleTableCellListener) listeners.elementAt(i);
							listener.getColumnSpan(cellEvent);
						}
						return cellEvent.count;
					}
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_row_extent_at (int /*long*/ atkObject, int /*long*/ row, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_get_row_extent_at");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_row_extent_at != 0) {
			parentResult = ATK.call (iface.get_row_extent_at, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					listeners = result.accessibleTableCellListeners;
					length = listeners.size();
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.count = (int)/*64*/parentResult;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = (AccessibleTableCellListener) listeners.elementAt(i);
							listener.getRowSpan(cellEvent);
						}
						return cellEvent.count;
					}
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_caption (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkTable_get_caption");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCaption(event);
				}
				Accessible result = event.accessible;
				if (result != null) return result.getAccessibleObject().handle;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_caption != 0) {
			parentResult = ATK.call (iface.get_caption, atkObject);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_summary (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkTable_get_summary");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getSummary(event);
				}
				Accessible result = event.accessible;
				if (result != null) return result.getAccessibleObject().handle;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_summary != 0) {
			parentResult = ATK.call (iface.get_summary, atkObject);
		}
		return parentResult;
	}
	
	static int /*long*/ atkTable_get_column_description (int /*long*/ atkObject, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_get_column_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_column_description != 0) {
			parentResult = ATK.call (iface.get_column_description, atkObject, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)/*64*/column;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getColumnDescription(event);
				}
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}
	
	static int /*long*/ atkTable_get_column_header (int /*long*/ atkObject, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_get_column_header");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getRowHeaderCells(event);
				}
				Accessible[] accessibles = event.accessibles;
				if (accessibles != null) {
					if (0 <= column && column < accessibles.length) {
						return accessibles[(int)/*64*/column].getAccessibleObject().handle;
					}
				}
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_column_header != 0) {
			parentResult = ATK.call (iface.get_column_header, atkObject, column);
		}
		return parentResult;
	}
	
	static int /*long*/ atkTable_get_row_description (int /*long*/ atkObject, int /*long*/ row) {
		if (DEBUG) print ("-->atkTable_get_row_description");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_row_description != 0) {
			parentResult = ATK.call (iface.get_row_description, atkObject, row);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				if (parentResult != 0) event.result = getString (parentResult);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getRowDescription(event);
				}
				if (event.result == null) return parentResult;
				if (descriptionPtr != -1) OS.g_free (descriptionPtr);
				return descriptionPtr = getStringPtr (event.result);
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_row_header (int /*long*/ atkObject, int /*long*/ row) {
		if (DEBUG) print ("-->atkTable_get_row_header");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getRowHeaderCells(event);
				}
				Accessible[] accessibles = event.accessibles;
				if (accessibles != null) {
					if (0 <= row && row < accessibles.length) {
						return accessibles[(int)/*64*/row].getAccessibleObject().handle;
					}
				}
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_row_header != 0) {
			parentResult = ATK.call (iface.get_row_header, atkObject, row);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_selected_columns (int /*long*/ atkObject, int /*long*/ selected) {
		if (DEBUG) print ("-->atkTable_get_selected_columns");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getSelectedColumns(event);
				}
				int count = event.selected != null ? event.selected.length : 0;
				int /*long*/ result = OS.g_malloc(count * 4);
				if (event.selected != null) OS.memmove(result, event.selected, count * 4);
				if (selected != 0) OS.memmove(selected, new int /*long*/[]{result}, C.PTR_SIZEOF);
				return count;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_selected_columns != 0) {
			parentResult = ATK.call (iface.get_selected_columns, atkObject, selected);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_get_selected_rows (int /*long*/ atkObject, int /*long*/ selected) {
		if (DEBUG) print ("-->atkTable_get_selected_rows");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getSelectedRows(event);
				}
				int count = event.selected != null ? event.selected.length : 0;
				int /*long*/ result = OS.g_malloc(count * 4);
				if (event.selected != null) OS.memmove(result, event.selected, count * 4);
				if (selected != 0) OS.memmove(selected, new int /*long*/[]{result}, C.PTR_SIZEOF);
				return count;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.get_selected_rows != 0) {
			parentResult = ATK.call (iface.get_selected_rows, atkObject, selected);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_is_column_selected (int /*long*/ atkObject, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_is_column_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.is_column_selected != 0) {
			parentResult = ATK.call (iface.is_column_selected, atkObject, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.isSelected = parentResult != 0;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.isColumnSelected(event);
				}
				return event.isSelected ? 1 : 0;
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_is_row_selected (int /*long*/ atkObject, int /*long*/ row) {
		if (DEBUG) print ("-->atkTable_is_row_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.is_row_selected != 0) {
			parentResult = ATK.call (iface.is_row_selected, atkObject, row);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.isSelected = parentResult != 0;
				event.row = (int)/*64*/row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.isRowSelected(event);
				}
				return event.isSelected ? 1 : 0;
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_is_selected (int /*long*/ atkObject, int /*long*/ row, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_is_selected");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.is_selected != 0) {
			parentResult = ATK.call (iface.is_selected, atkObject, row, column);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.getCell(event);
				}
				Accessible result = event.accessible;
				if (result != null) {
					listeners = result.accessibleTableCellListeners;
					length = listeners.size();
					if (length > 0) {
						AccessibleTableCellEvent cellEvent = new AccessibleTableCellEvent(result);
						cellEvent.isSelected = parentResult != 0;
						for (int i = 0; i < length; i++) {
							AccessibleTableCellListener listener = (AccessibleTableCellListener) listeners.elementAt(i);
							listener.isSelected(cellEvent);
						}
						return cellEvent.isSelected ? 1 : 0;
					}
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkTable_add_row_selection (int /*long*/ atkObject, int /*long*/ row) {
		if (DEBUG) print ("-->atkTable_add_row_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.selectRow(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.add_row_selection != 0) {
			parentResult = ATK.call (iface.add_row_selection, atkObject, row);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_remove_row_selection (int /*long*/ atkObject, int /*long*/ row) {
		if (DEBUG) print ("-->atkTable_remove_row_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.row = (int)/*64*/row;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.deselectRow(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.remove_row_selection != 0) {
			parentResult = ATK.call (iface.remove_row_selection, atkObject, row);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_add_column_selection (int /*long*/ atkObject, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_add_column_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.selectColumn(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.add_column_selection != 0) {
			parentResult = ATK.call (iface.add_column_selection, atkObject, column);
		}
		return parentResult;
	}

	static int /*long*/ atkTable_remove_column_selection (int /*long*/ atkObject, int /*long*/ column) {
		if (DEBUG) print ("-->atkTable_remove_column_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTableListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTableEvent event = new AccessibleTableEvent(accessible);
				event.column = (int)/*64*/column;
				for (int i = 0; i < length; i++) {
					AccessibleTableListener listener = (AccessibleTableListener) listeners.elementAt(i);
					listener.deselectColumn(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTableIface iface = getTableIface (atkObject);
		if (iface != null && iface.remove_column_selection != 0) {
			parentResult = ATK.call (iface.remove_column_selection, atkObject, column);
		}
		return parentResult;
	}

	static AtkTextIface getTextIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_TEXT())) {
			AtkTextIface iface = new AtkTextIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_TEXT_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	
	static int /*long*/ atkText_get_character_extents (int /*long*/ atkObject, int /*long*/ offset, int /*long*/ x, int /*long*/ y, int /*long*/ width, int /*long*/ height, int /*long*/ coords) {
		if (DEBUG) print ("-->atkText_get_character_extents");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)/*64*/offset;
				event.end = (int)/*64*/(offset + 1);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getTextBounds(event);
				}
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coords == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				OS.memmove (x, new int[]{event.x}, 4);
				OS.memmove (y, new int[]{event.y}, 4);
				OS.memmove (width, new int[]{event.width}, 4);
				OS.memmove (height, new int[]{event.height}, 4);				
				return 0;
			}
		}
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_character_extents != 0) {
			ATK.call (iface.get_character_extents, atkObject, offset, x, y, width, height, coords);
		}
		return 0;
	}
	
	static String getString (int /*long*/ strPtr) {
		int length = OS.strlen (strPtr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, strPtr, length);
		return new String (Converter.mbcsToWcs (null, buffer));
	}
	
	static int /*long*/ getStringPtr (String str) {
		byte [] buffer = Converter.wcsToMbcs(null, str != null ? str : "", true); 
		int /*long*/ ptr = OS.g_malloc(buffer.length);
		OS.memmove(ptr, buffer, buffer.length);
		return ptr;
	}
	
	static int /*long*/ atkText_get_range_extents (int /*long*/ atkObject, int /*long*/ start_offset, int /*long*/ end_offset, int /*long*/ coord_type, int /*long*/ rect) {
		if (DEBUG) print ("-->atkText_get_range_extents");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)/*64*/start_offset;
				event.end = (int)/*64*/end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getTextBounds(event);
				}
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coord_type == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x -= topWindowX [0];
					event.y -= topWindowY [0];
				}
				AtkTextRectangle atkRect = new AtkTextRectangle();
				atkRect.x = event.x;
				atkRect.y = event.y;
				atkRect.width = event.width;
				atkRect.height = event.height;
				ATK.memmove (rect, atkRect, AtkTextRectangle.sizeof);
				return 0;
			}
		}
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_range_extents != 0) {
			ATK.call (iface.get_range_extents, atkObject, start_offset, end_offset, coord_type, rect);
		}
		return 0;
	}
	
	static int /*long*/ atkText_get_run_attributes (int /*long*/ atkObject, int /*long*/ offset, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_run_attributes");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleAttributeListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextAttributeEvent event = new AccessibleTextAttributeEvent(accessible);
				event.offset = (int)/*64*/offset;
				for (int i = 0; i < length; i++) {
					AccessibleAttributeListener listener = (AccessibleAttributeListener) listeners.elementAt(i);
					listener.getTextAttributes(event);
				}
				OS.memmove (start_offset, new int []{event.start}, 4);
				OS.memmove (end_offset, new int []{event.end}, 4);
				TextStyle style = event.textStyle;
				if (style == null) return 0;
				int /*long*/ result = 0;
				AtkAttribute attr = new AtkAttribute();
				if (style.rise != 0) {
					int /*long*/ attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_RISE));
					attr.value = getStringPtr (String.valueOf(style.rise));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				if (style.underline) {
					int /*long*/ attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_UNDERLINE));
					String str = "none";
					switch (style.underlineStyle) {
						case SWT.UNDERLINE_DOUBLE: str = "double"; break;
						case SWT.UNDERLINE_SINGLE: str = "single"; break;
						case SWT.UNDERLINE_ERROR: str = "error"; break;
						case SWT.UNDERLINE_SQUIGGLE: str = "squiggle"; break;
					}
					attr.value = getStringPtr (str);
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				if (style.strikeout) {
					int /*long*/ attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRIKETHROUGH));
					attr.value = getStringPtr ("1");
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				Font font = style.font;
				if (font != null && !font.isDisposed()) {
					//TODO language and direction
					int /*long*/ attrPtr;
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FAMILY_NAME));
					attr.value = ATK.g_strdup (OS.pango_font_description_get_family (font.handle));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
					
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_SIZE));
					attr.value = getStringPtr (String.valueOf (OS.pango_font_description_get_size(font.handle) / OS.PANGO_SCALE));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
					
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STYLE));
					attr.value = ATK.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_STYLE, OS.pango_font_description_get_style(font.handle)));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
					
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_VARIANT));
					attr.value = ATK.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_VARIANT, OS.pango_font_description_get_variant(font.handle)));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
					
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_STRETCH));
					attr.value = ATK.g_strdup (ATK.atk_text_attribute_get_value(ATK.ATK_TEXT_ATTR_STRETCH, OS.pango_font_description_get_stretch(font.handle)));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
					
					attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_WEIGHT));
					attr.value = getStringPtr (String.valueOf (OS.pango_font_description_get_weight(font.handle)));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				Color color = style.foreground;
				if (color != null && !color.isDisposed()) {
					int /*long*/ attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_FG_COLOR));
					attr.value = getStringPtr ((color.handle.red & 0xFFFF) + "," + (color.handle.blue & 0xFFFF) + "," + (color.handle.blue & 0xFFFF));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				color = style.background;
				if (color != null && !color.isDisposed()) {
					int /*long*/ attrPtr = OS.g_malloc(AtkAttribute.sizeof);
					attr.name = ATK.g_strdup (ATK.atk_text_attribute_get_name(ATK.ATK_TEXT_ATTR_BG_COLOR));
					attr.value = getStringPtr ((color.handle.red & 0xFFFF) + "," + (color.handle.blue & 0xFFFF) + "," + (color.handle.blue & 0xFFFF));
					ATK.memmove(attrPtr, attr, AtkAttribute.sizeof);
					result = OS.g_list_append(result, attrPtr);
				}
				return result;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_run_attributes != 0) {
			parentResult = ATK.call (iface.get_run_attributes, atkObject, offset, start_offset, end_offset);
		}
		return parentResult;
	}

	static int /*long*/ atkText_get_offset_at_point (int /*long*/ atkObject, int /*long*/ x, int /*long*/ y, int /*long*/ coords) {
		if (DEBUG) print ("-->atkText_get_offset_at_point");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.x = (int)/*64*/x;
				event.y = (int)/*64*/y;
				int[] topWindowX = new int [1], topWindowY = new int [1];
				if (coords == ATK.ATK_XY_WINDOW) {
					windowPoint (object, topWindowX, topWindowY);
					event.x += topWindowX [0];
					event.y += topWindowY [0]; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getOffsetAtPoint(event);
				}
				return event.offset;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_offset_at_point != 0) {
			parentResult = ATK.call (iface.get_offset_at_point, atkObject, x, y, coords);
		}
		return parentResult;
	}

	static int /*long*/ atkText_add_selection (int /*long*/ atkObject, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_add_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)/*64*/start_offset;
				event.end = (int)/*64*/end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.addSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.add_selection != 0) {
			parentResult = ATK.call (iface.add_selection, atkObject, start_offset, end_offset);
		}
		return parentResult;
	}

	static int /*long*/ atkText_remove_selection (int /*long*/ atkObject, int /*long*/ selection_num) {
		if (DEBUG) print ("-->atkText_remove_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)/*64*/selection_num;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.removeSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.remove_selection != 0) {
			parentResult = ATK.call (iface.remove_selection, atkObject, selection_num);
		}
		return parentResult;
	}
	
	static int /*long*/ atkText_set_caret_offset (int /*long*/ atkObject, int /*long*/ offset) {
		if (DEBUG) print ("-->atkText_gset_character_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.offset = (int)/*64*/offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.setCaretOffset(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_character_at_offset != 0) {
			return ATK.call (iface.get_character_at_offset, atkObject, offset);
		}
		return 0;
	}

	static int /*long*/ atkText_set_selection (int /*long*/ atkObject, int /*long*/ selection_num, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_set_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)/*64*/selection_num;
				event.start = (int)/*64*/start_offset;
				event.end = (int)/*64*/end_offset;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.setSelection(event);
				}
				return ACC.OK.equals(event.result) ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.set_selection != 0) {
			parentResult = ATK.call (iface.set_selection, atkObject, selection_num, start_offset, end_offset);
		}
		return parentResult;
	}

	static int /*long*/ atkText_get_caret_offset (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkText_get_caret_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_caret_offset != 0) {
			parentResult = ATK.call (iface.get_caret_offset, atkObject);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getCaretOffset (event);
				}
				return event.offset;
			}
			listeners = accessible.accessibleTextListeners;
			length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
				event.childID = object.id;
				event.offset = (int)/*64*/parentResult;
				for (int i = 0; i < length; i++) {
					AccessibleTextListener listener = (AccessibleTextListener) listeners.elementAt(i);
					listener.getCaretOffset (event);	
				}
				return event.offset;
			}
		}
		return parentResult; 	
	}
	
	static int /*long*/ atkText_get_bounded_ranges (int /*long*/ atkObject, int /*long*/ rect, int /*long*/ coord_type, int /*long*/ x_clip_type, int /*long*/ y_clip_type) {
		if (DEBUG) print ("-->atkText_get_bounded_ranges");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				AtkTextRectangle atkRect = new AtkTextRectangle();
				ATK.memmove (atkRect, rect, AtkTextRectangle.sizeof);
				event.x = atkRect.x;
				event.y = atkRect.y;
				event.width = atkRect.width;
				event.height = atkRect.height;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getRanges (event);
				}
				int [] ranges = event.ranges;
				int size = ranges == null ? 1 : ranges.length / 2;
				int /*long*/ result = OS.malloc(size * AtkTextRange.sizeof);
				AtkTextRange range = new AtkTextRange();
				for (int j = 0, end = (ranges != null ? ranges.length / 2 : 1); j < end; j++) {
					if (ranges != null) {
						int index = j * 2;
						event.start = ranges[index];
						event.end = ranges[index+1];
					}
					event.count = 0;
					event.type = ACC.TEXT_BOUNDARY_ALL;
					for (int i = 0; i < length; i++) {
						AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
						listener.getText(event);
					}
					range.start_offset = event.start;
					range.end_offset = event.end;
					range.content = getStringPtr (event.result);
					event.result = null;
					event.count = event.type = event.x = event.y = event.width = event.height = 0;
					for (int i = 0; i < length; i++) {
						AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
						listener.getTextBounds(event);
					}
					range.bounds.x = event.x;
					range.bounds.y = event.y;
					range.bounds.width = event.width;
					range.bounds.height = event.height;
					ATK.memmove(result + j * AtkTextRange.sizeof, range, AtkTextRange.sizeof);
				}
				return result;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_bounded_ranges != 0) {
			parentResult = ATK.call (iface.get_bounded_ranges, atkObject);
		}
		return parentResult;
	}
	
	static int /*long*/ atkText_get_character_at_offset (int /*long*/ atkObject, int /*long*/ offset) {
		if (DEBUG) print ("-->atkText_get_character_at_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)/*64*/offset;
				event.end = (int)/*64*/(offset + 1);
				event.type = ACC.TEXT_BOUNDARY_CHAR;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				String text = event.result;
				return text != null && text.length() > 0 ? text.charAt(0) : 0;
			}
			String text = object.getText ();
			if (text != null && text.length() > offset) return text.charAt ((int)/*64*/offset);
		}
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_character_at_offset != 0) {
			return ATK.call (iface.get_character_at_offset, atkObject, offset);
		}
		return 0;
	}

	static int /*long*/ atkText_get_character_count (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkText_get_character_count");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getCharacterCount(event);
				}
				return event.count;
			}
			String text = object.getText ();
			if (text != null) return text.length ();
		}
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_character_count != 0) {
			return ATK.call (iface.get_character_count, atkObject);
		}
		return 0;
	}

	static int /*long*/ atkText_get_n_selections (int /*long*/ atkObject) {
		if (DEBUG) print ("-->atkText_get_n_selections");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getSelectionCount (event);
				}
				return event.count;
			}
			listeners = accessible.accessibleTextListeners;
			length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent (object.accessible);
				event.childID = object.id;
				for (int i = 0; i < length; i++) {
					AccessibleTextListener listener = (AccessibleTextListener) listeners.elementAt(i);
					listener.getSelectionRange (event);
				}
				if (event.length > 0) return 1;
			}
		}
		int /*long*/ parentResult = 0;
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_n_selections != 0) {
			parentResult = ATK.call (iface.get_n_selections, atkObject);
		}
		return parentResult;
	}

	static int /*long*/ atkText_get_selection (int /*long*/ atkObject, int /*long*/ selection_num, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_selection");
		AccessibleObject object = getAccessibleObject (atkObject);
		int /*long*/ parentResult = 0;
		OS.memmove (start_offset, new int[] {0}, 4);
		OS.memmove (end_offset, new int[] {0}, 4);
		AtkTextIface iface = getTextIface (atkObject);
		if (iface != null && iface.get_selection != 0) {
			parentResult = ATK.call (iface.get_selection, atkObject, selection_num, start_offset, end_offset);
		}
		if (object != null) {
			int[] parentStart = new int [1];
			int[] parentEnd = new int [1];
			OS.memmove (parentStart, start_offset, 4);
			OS.memmove (parentEnd, end_offset, 4);
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.index = (int)/*64*/selection_num;
				event.start = parentStart[0];
				event.end = parentEnd[0];
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getSelection (event);
				}
				parentStart [0] = event.start;
				parentEnd [0] = event.end;
				OS.memmove (start_offset, parentStart, 4);
				OS.memmove (end_offset, parentEnd, 4);
				event.count = event.index = 0;
				event.type = ACC.TEXT_BOUNDARY_ALL;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				return getStringPtr (event.result);
			}
			if (selection_num == 0) {
				listeners = accessible.accessibleTextListeners;
				length = listeners.size();
				if (length > 0) {
					AccessibleTextEvent event = new AccessibleTextEvent (accessible);
					event.childID = object.id;
					event.offset = parentStart [0];
					event.length = parentEnd [0] - parentStart [0];
					for (int i = 0; i < length; i++) {
						AccessibleTextListener listener = (AccessibleTextListener) listeners.elementAt(i);
						listener.getSelectionRange (event);
					}
					OS.memmove (start_offset, new int[] {event.offset}, 4);
					OS.memmove (end_offset, new int[] {event.offset + event.length}, 4);
					//TODO return the selected text, should we free parent result
					return 0;
				}
			}
		}
		return parentResult;
	}

	static int /*long*/ atkText_get_text (int /*long*/ atkObject, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_text: " + atkObject + " " + start_offset + "," + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = (int)/*64*/start_offset;
				event.end = (int)/*64*/end_offset;
				event.type = ACC.TEXT_BOUNDARY_ALL;
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				return getStringPtr (event.result);
			}
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				if (end_offset == -1) {
					end_offset = text.length ();
				} else {
					end_offset = Math.min (end_offset, text.length ());	
				}
				start_offset = Math.min (start_offset, end_offset);
				text = text.substring ((int)/*64*/start_offset, (int)/*64*/end_offset);
				return getStringPtr (text);
			}
		}
		return 0;
	}

	static int /*long*/ atkText_get_text_after_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_text_after_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)/*64*/offset_value;
				event.count = 1;
				switch ((int)/*64*/boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				OS.memmove (start_offset, new int[] {event.start}, 4);
				OS.memmove (end_offset, new int[] {event.end}, 4);
				return getStringPtr (event.result);
			}
			int offset = (int)/*64*/offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
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
				return getStringPtr (text);
			} 
		}
		return 0;
	}

	static int /*long*/ atkText_get_text_at_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_text_at_offset: " + offset_value + " start: " + start_offset + " end: " + end_offset);
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)/*64*/offset_value;
				event.count = 0;
				switch ((int)/*64*/boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				OS.memmove (start_offset, new int[] {event.start}, 4);
				OS.memmove (end_offset, new int[] {event.end}, 4);
				return getStringPtr (event.result);
			}
			int offset = (int)/*64*/offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
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
				return getStringPtr (text);
			} 
		}
		return 0;
	}

	static int /*long*/ atkText_get_text_before_offset (int /*long*/ atkObject, int /*long*/ offset_value, int /*long*/ boundary_type, int /*long*/ start_offset, int /*long*/ end_offset) {
		if (DEBUG) print ("-->atkText_get_text_before_offset");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleTextExtendedListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleTextEvent event = new AccessibleTextEvent(accessible);
				event.start = event.end = (int)/*64*/offset_value;
				event.count = -1;
				switch ((int)/*64*/boundary_type) {
					case ATK.ATK_TEXT_BOUNDARY_CHAR: event.type = ACC.TEXT_BOUNDARY_CHAR; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_START: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_WORD_END: event.type = ACC.TEXT_BOUNDARY_WORD; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_START: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_SENTENCE_END: event.type = ACC.TEXT_BOUNDARY_SENTENCE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_START: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
					case ATK.ATK_TEXT_BOUNDARY_LINE_END: event.type = ACC.TEXT_BOUNDARY_LINE; break; 
				}
				for (int i = 0; i < length; i++) {
					AccessibleTextExtendedListener listener = (AccessibleTextExtendedListener) listeners.elementAt(i);
					listener.getText(event);
				}
				OS.memmove (start_offset, new int[] {event.start}, 4);
				OS.memmove (end_offset, new int[] {event.end}, 4);
				return getStringPtr (event.result);
			}
			int offset = (int)/*64*/offset_value;
			String text = object.getText ();
			if (text != null && text.length () > 0) {
				length = text.length ();
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
				return getStringPtr (text);
			}
		}
		return 0;
	}
	
	static void setGValue (int /*long*/ value, Number number) {
		if (number == null) return;
		OS.g_value_unset(value);
		if (number instanceof Double) {
			OS.g_value_init(value, OS.G_TYPE_DOUBLE());
			OS.g_value_set_double(value, number.doubleValue());
		} else if (number instanceof Float) {
			OS.g_value_init(value, OS.G_TYPE_FLOAT());
			OS.g_value_set_float(value, number.floatValue());
		} else if (number instanceof Long) {
			OS.g_value_init(value, OS.G_TYPE_INT64());
			OS.g_value_set_int64(value, number.longValue());
		} else {
			OS.g_value_init(value, OS.G_TYPE_INT());
			OS.g_value_set_int(value, number.intValue());
		}
	}

	static Number getGValue (int /*long*/ value) {
		int /*long*/ type = OS.G_VALUE_TYPE(value);
		if (type == 0) return null;
		if (type == OS.G_TYPE_DOUBLE()) return new Double(OS.g_value_get_double(value));
		if (type == OS.G_TYPE_FLOAT()) return new Float(OS.g_value_get_float(value));
		if (type == OS.G_TYPE_INT64()) return new Long(OS.g_value_get_int64(value));
		return new Integer(OS.g_value_get_int(value));
	}
	
	static AtkValueIface getValueIface (int /*long*/ atkObject) {
		if (ATK.g_type_is_a (OS.g_type_parent (OS.G_OBJECT_TYPE (atkObject)), ATK.ATK_TYPE_VALUE())) {
			AtkValueIface iface = new AtkValueIface ();
			ATK.memmove (iface, ATK.g_type_interface_peek_parent (ATK.ATK_VALUE_GET_IFACE (atkObject)));
			return iface;
		}
		return null;
	}
	static int /*long*/ atkValue_get_current_value (int /*long*/ atkObject, int /*long*/ value) {
		if (DEBUG) print ("-->atkValue_get_current_value");
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getValueIface (atkObject);
		if (iface != null && iface.get_current_value != 0) {
			ATK.call (iface.get_current_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleValueListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = (AccessibleValueListener) listeners.elementAt(i);
					listener.getCurrentValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	static int /*long*/ atkValue_get_maximum_value (int /*long*/ atkObject, int /*long*/ value) {
		if (DEBUG) print ("-->atkValue_get_maximum_value");
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getValueIface (atkObject);
		if (iface != null && iface.get_maximum_value != 0) {
			ATK.call (iface.get_maximum_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleValueListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = (AccessibleValueListener) listeners.elementAt(i);
					listener.getMaximumValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	static int /*long*/ atkValue_get_minimum_value (int /*long*/ atkObject, int /*long*/ value) {
		if (DEBUG) print ("-->atkValue_get_minimum_value");
		AccessibleObject object = getAccessibleObject (atkObject);
		AtkValueIface iface = getValueIface (atkObject);
		if (iface != null && iface.get_minimum_value != 0) {
			ATK.call (iface.get_minimum_value, atkObject, value);
		}
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleValueListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = (AccessibleValueListener) listeners.elementAt(i);
					listener.getMinimumValue(event);
				}
				setGValue(value, event.value);
			}
		}
		return 0;
	}

	static int /*long*/ atkValue_set_current_value (int /*long*/ atkObject, int /*long*/ value) {
		if (DEBUG) print ("-->atkValue_set_current_value");
		AccessibleObject object = getAccessibleObject (atkObject);
		if (object != null) {
			Accessible accessible = object.accessible;
			Vector listeners = accessible.accessibleValueListeners;
			int length = listeners.size();
			if (length > 0) {
				AccessibleValueEvent event = new AccessibleValueEvent(accessible);
				event.value = getGValue(value);
				for (int i = 0; i < length; i++) {
					AccessibleValueListener listener = (AccessibleValueListener) listeners.elementAt(i);
					listener.setCurrentValue(event);
				}
				return event.value != null ? 1 : 0;
			}
		}
		int /*long*/ parentResult = 0;
		AtkValueIface iface = getValueIface (atkObject);
		if (iface != null && iface.set_current_value != 0) {
			parentResult = ATK.call (iface.set_current_value, atkObject, value);
		}
		return parentResult;
	}

	static AccessibleObject getAccessibleObject (int /*long*/ atkObject) {
		AccessibleObject object = (AccessibleObject)AccessibleObjects.get (new LONG (atkObject));
		if (object == null) return null;
		if (object.accessible == null) return null;
		Control control = object.accessible.control;
		if (control == null || control.isDisposed()) return null;
		return object;
	}
	
	AccessibleObject getChildByID (int childId) {
		if (childId == ACC.CHILDID_SELF) return this;
		if (childId == ACC.CHILDID_NONE || childId == ACC.CHILDID_MULTIPLE) return null;
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && children[i].id == childId) return children [0];
			}
		}
		return null;
	}
	
	AccessibleObject getChildByIndex (int childIndex) {
		if (children != null && childIndex < children.length) return children [childIndex];
		return null;
	}
	
	String getText () {
		int /*long*/ parentResult = 0;
		String parentText = "";	//$NON-NLS-1$
		AtkTextIface iface = getTextIface (handle);
		if (iface != null && iface.get_character_count != 0) {
			int /*long*/ characterCount = ATK.call (iface.get_character_count, handle);
			if (characterCount > 0 && iface.get_text != 0) {
				parentResult = ATK.call (iface.get_text, handle, 0, characterCount);
				if (parentResult != 0) {
					parentText = getString (parentResult);
					OS.g_free(parentResult);
				}
			}
		}
		Vector listeners = accessible.accessibleControlListeners;
		int length = listeners.size();
		if (length > 0) {
			AccessibleControlEvent event = new AccessibleControlEvent (accessible);
			event.childID = id;
			event.result = parentText;
			for (int i = 0; i < length; i++) {
				AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
				listener.getValue (event);				
			}
			return event.result;
		}
		return null;
	}

	static int /*long*/ gObjectClass_finalize (int /*long*/ atkObject) {
		if (DEBUG) print ("-->gObjectClass_finalize: " + atkObject);
		int /*long*/ superType = ATK.g_type_class_peek_parent (ATK.G_OBJECT_GET_CLASS (atkObject));
		int /*long*/ gObjectClass = ATK.G_OBJECT_CLASS (superType);
		GObjectClass objectClassStruct = new GObjectClass ();
		ATK.memmove (objectClassStruct, gObjectClass);
		ATK.call (objectClassStruct.finalize, atkObject);
		AccessibleObject object = (AccessibleObject)AccessibleObjects.get (new LONG (atkObject));
		if (object != null) {
			AccessibleObjects.remove (new LONG (atkObject));
		}
		return 0;
	}

	static int toATKRelation (int relation) {
		switch (relation) {
			case ACC.RELATION_CONTROLLED_BY: return ATK.ATK_RELATION_CONTROLLED_BY;
			case ACC.RELATION_CONTROLLER_FOR: return ATK.ATK_RELATION_CONTROLLER_FOR;
			case ACC.RELATION_DESCRIBED_BY: return ATK.ATK_RELATION_DESCRIBED_BY;
			case ACC.RELATION_DESCRIPTION_FOR: return ATK.ATK_RELATION_DESCRIPTION_FOR;
			case ACC.RELATION_EMBEDDED_BY: return ATK.ATK_RELATION_EMBEDDED_BY;
			case ACC.RELATION_EMBEDS: return ATK.ATK_RELATION_EMBEDS;
			case ACC.RELATION_FLOWS_FROM: return ATK.ATK_RELATION_FLOWS_FROM;
			case ACC.RELATION_FLOWS_TO: return ATK.ATK_RELATION_FLOWS_TO;
			case ACC.RELATION_LABEL_FOR: return ATK.ATK_RELATION_LABEL_FOR;
			case ACC.RELATION_LABELLED_BY: return ATK.ATK_RELATION_LABELLED_BY;
			case ACC.RELATION_MEMBER_OF: return ATK.ATK_RELATION_MEMBER_OF;
			case ACC.RELATION_NODE_CHILD_OF: return ATK.ATK_RELATION_NODE_CHILD_OF;
			case ACC.RELATION_PARENT_WINDOW_OF: return ATK.ATK_RELATION_PARENT_WINDOW_OF;
			case ACC.RELATION_POPUP_FOR: return ATK.ATK_RELATION_POPUP_FOR;
			case ACC.RELATION_SUBWINDOW_OF: return ATK.ATK_RELATION_SUBWINDOW_OF;
		}
		return 0;
	}
	
	static void windowPoint (AccessibleObject object, int [] x, int [] y) {
		GtkAccessible gtkAccessible = new GtkAccessible ();
		ATK.memmove (gtkAccessible, object.handle);
		while (gtkAccessible.widget == 0 && object.parent != null) {
			object = object.parent;
			ATK.memmove (gtkAccessible, object.handle);
		}
		if (gtkAccessible.widget == 0) return;
		int /*long*/ topLevel = ATK.gtk_widget_get_toplevel (gtkAccessible.widget);
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (topLevel);				
		OS.gdk_window_get_origin (window, x, y);
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
	
	void addRelation (int type, Accessible target) {
		OS.atk_object_add_relationship(handle, toATKRelation(type), target.getAccessibleObject().handle);
	}

	void release () {
		if (DEBUG) print("AccessibleObject.release: " + handle);
		accessible = null;
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				AccessibleObject child = children [i];
				if (child != null) OS.g_object_unref(child.handle);
			}
			children = null;
		}
		// TODO remove from children from parent?
		if (isLightweight) {
			OS.g_object_unref(handle);
		}
	}
	
	void removeRelation (int type, Accessible target) {
		OS.atk_object_remove_relationship (handle, toATKRelation(type), target.getAccessibleObject().handle);
	}
	
	void selectionChanged () {
		OS.g_signal_emit_by_name (handle, ATK.selection_changed);
	}
	
	void sendEvent(int event, Object eventData) {
		switch (event) {
			case ACC.EVENT_SELECTION_CHANGED:
				OS.g_signal_emit_by_name (handle, ATK.selection_changed);
				break;
			case ACC.EVENT_TEXT_SELECTION_CHANGED:
				OS.g_signal_emit_by_name (handle, ATK.text_selection_changed);
				break;
			case ACC.EVENT_STATE_CHANGED: {
				if (!(eventData instanceof int[])) break;
				int[] array = (int[])eventData;
				int state =  array[0];
				int value = array[1];
				int atkState = -1;
				switch (state) {
					case ACC.STATE_SELECTED: atkState = ATK.ATK_STATE_SELECTED; break;
					case ACC.STATE_SELECTABLE: atkState = ATK.ATK_STATE_SELECTABLE; break;
					case ACC.STATE_MULTISELECTABLE: atkState = ATK.ATK_STATE_MULTISELECTABLE; break;
					case ACC.STATE_FOCUSED: atkState = ATK.ATK_STATE_FOCUSED; break;
					case ACC.STATE_FOCUSABLE: atkState = ATK.ATK_STATE_FOCUSABLE; break;
					case ACC.STATE_PRESSED: atkState = ATK.ATK_STATE_PRESSED; break;
					case ACC.STATE_CHECKED: atkState = ATK.ATK_STATE_CHECKED; break;
					case ACC.STATE_EXPANDED: atkState = ATK.ATK_STATE_EXPANDED; break;
					case ACC.STATE_COLLAPSED: atkState = ATK.ATK_STATE_EXPANDED; break;
					case ACC.STATE_HOTTRACKED: atkState = ATK.ATK_STATE_ARMED; break;
					case ACC.STATE_BUSY: atkState = ATK.ATK_STATE_BUSY; break;
					case ACC.STATE_READONLY: atkState = ATK.ATK_STATE_EDITABLE; break;
					case ACC.STATE_INVISIBLE: atkState = ATK.ATK_STATE_VISIBLE; break;
					case ACC.STATE_OFFSCREEN: atkState = ATK.ATK_STATE_SHOWING; break;
					case ACC.STATE_SIZEABLE: atkState = ATK.ATK_STATE_RESIZABLE; break;
					case ACC.STATE_LINKED: break;
					case ACC.STATE_DISABLED: atkState = ATK.ATK_STATE_ENABLED; break;
					case ACC.STATE_ACTIVE: atkState = ATK.ATK_STATE_ACTIVE; break;
					case ACC.STATE_SINGLELINE: atkState = ATK.ATK_STATE_SINGLE_LINE; break;
					case ACC.STATE_MULTILINE: atkState = ATK.ATK_STATE_MULTI_LINE; break;
					case ACC.STATE_REQUIRED: atkState = ATK.ATK_STATE_REQUIRED; break;
					case ACC.STATE_INVALID_ENTRY: atkState = ATK.ATK_STATE_INVALID_ENTRY; break;
					case ACC.STATE_SUPPORTS_AUTOCOMPLETION: atkState = ATK.ATK_STATE_SUPPORTS_AUTOCOMPLETION; break;
				}
				if (atkState == -1) break;
				ATK.atk_object_notify_state_change(handle, atkState, value != 0);
				break;
			}
			case ACC.EVENT_LOCATION_CHANGED: {
				Vector listeners = accessible.accessibleControlListeners;
				int length = listeners.size();
				GdkRectangle rect = new GdkRectangle();
				if (length > 0) {
					AccessibleControlEvent e = new AccessibleControlEvent (accessible);
					e.childID = id;
					for (int i = 0; i < length; i++) {
						AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
						listener.getLocation (e);
					}
					rect.x = e.x;
					rect.y = e.y;
					rect.width = e.width;
					rect.height = e.height;
				}
				OS.g_signal_emit_by_name (handle, ATK.bounds_changed, rect);
				break;
			}
			case ACC.EVENT_NAME_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_name);
				break;
			case ACC.EVENT_DESCRIPTION_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_description);
				break;
			case ACC.EVENT_VALUE_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_value);
				break;
			case ACC.EVENT_DOCUMENT_LOAD_COMPLETE:
				OS.g_signal_emit_by_name (handle, ATK.load_complete);
				break;
			case ACC.EVENT_DOCUMENT_LOAD_STOPPED:
				OS.g_signal_emit_by_name (handle, ATK.load_stopped);
				break;
			case ACC.EVENT_DOCUMENT_RELOAD:
				OS.g_signal_emit_by_name (handle, ATK.reload);
				break;
			case ACC.EVENT_PAGE_CHANGED:
				break;
			case ACC.EVENT_SECTION_CHANGED:
				break;
			case ACC.EVENT_ACTION_CHANGED:
				break;
			case ACC.EVENT_HYPERLINK_END_INDEX_CHANGED:
				OS.g_object_notify(handle, ATK.end_index);
				break;
			case ACC.EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED:
				OS.g_object_notify(handle, ATK.number_of_anchors);
				break;
			case ACC.EVENT_HYPERLINK_SELECTED_LINK_CHANGED:
				OS.g_object_notify(handle, ATK.selected_link);
				break;
			case ACC.EVENT_HYPERLINK_START_INDEX_CHANGED:
				OS.g_object_notify(handle, ATK.start_index);
				break;
			case ACC.EVENT_HYPERTEXT_LINK_ACTIVATED:
				OS.g_signal_emit_by_name (handle, ATK.link_activated);
				break;
			case ACC.EVENT_HYPERTEXT_LINK_SELECTED:
				//TODO needs the index the link that was selected
//				OS.g_signal_emit_by_name (handle, ATK.link_selected);
				break;
			case ACC.EVENT_HYPERTEXT_LINK_COUNT_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_hypertext_nlinks);
				break;
			case ACC.EVENT_ATTRIBUTE_CHANGED:
				OS.g_signal_emit_by_name (handle, ATK.attributes_changed);
				break;
			case ACC.EVENT_TABLE_CAPTION_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_caption_object);
				break;
			case ACC.EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_column_description);
				break;
			case ACC.EVENT_TABLE_COLUMN_HEADER_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_column_header);
				break;
			case ACC.EVENT_TABLE_CHANGED: {
				if (!(eventData instanceof int[])) break;
				int[] array = (int[])eventData;
				int type =  array[0];
				int rowStart = array[1];
				int rowCount = array[2];
				int columnStart = array[3];
				int columnCount = array[4];
				switch (type) {
					case ACC.DELETE:
						if (rowCount > 0) OS.g_signal_emit_by_name (handle, ATK.row_deleted, rowStart, rowCount);
						if (columnCount > 0) OS.g_signal_emit_by_name (handle, ATK.column_deleted, columnStart, columnCount);
						break;
					case ACC.INSERT:
						if (rowCount > 0) OS.g_signal_emit_by_name (handle, ATK.row_inserted, rowStart, rowCount);
						if (columnCount > 0) OS.g_signal_emit_by_name (handle, ATK.column_inserted, columnStart, columnCount);
						break;
				}
				break;
			}
			case ACC.EVENT_TABLE_ROW_DESCRIPTION_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_row_description);
				break;
			case ACC.EVENT_TABLE_ROW_HEADER_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_row_header);
				break;
			case ACC.EVENT_TABLE_SUMMARY_CHANGED:
				OS.g_object_notify(handle, ATK.accessible_table_summary);
				break;
			case ACC.EVENT_TEXT_ATTRIBUTE_CHANGED:
				OS.g_signal_emit_by_name (handle, ATK.text_attributes_changed);
				break;
			case ACC.EVENT_TEXT_CARET_MOVED:
			case ACC.EVENT_TEXT_COLUMN_CHANGED: {
				int offset = 0;
				Vector listeners = accessible.accessibleTextExtendedListeners;
				int length = listeners.size();
				AccessibleTextEvent e = new AccessibleTextEvent (accessible);
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						AccessibleTextListener listener = (AccessibleTextListener) listeners.elementAt(i);
						listener.getCaretOffset (e);
					}
				} else {
					listeners = accessible.accessibleTextListeners;
					length = listeners.size();
					if (length > 0) {
						e.childID = id;
						for (int i = 0; i < length; i++) {
							AccessibleTextListener listener = (AccessibleTextListener) listeners.elementAt(i);
							listener.getCaretOffset (e);	
						}
					}
				}
				offset = e.offset;
				OS.g_signal_emit_by_name (handle, ATK.text_caret_moved, offset);
				break;
			}
			case ACC.EVENT_TEXT_CHANGED: {
				if (!(eventData instanceof Object[])) break;
				Object[] data = (Object[])eventData;
				int type = ((Integer)data[0]).intValue();
				int start = ((Integer)data[1]).intValue();
				int end = ((Integer)data[2]).intValue();
				switch (type) {
					case ACC.DELETE:
						OS.g_signal_emit_by_name (handle, ATK.text_changed_delete, start, end -start);
						break;
					case ACC.INSERT:
						OS.g_signal_emit_by_name (handle, ATK.text_changed_insert, start, end -start);
						break;
				}
				break;
			}
		}
	}
	
	void setFocus (int childID) {
		updateChildren ();
		AccessibleObject accObject = getChildByID (childID);
		if (accObject != null) {
			ATK.atk_focus_tracker_notify (accObject.handle);
		}
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
		Vector listeners = accessible.accessibleControlListeners;
		int length = listeners.size();
		AccessibleControlEvent event = new AccessibleControlEvent (accessible);
		event.childID = id;
		for (int i = 0; i < length; i++) {
			AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (i);
			listener.getChildren (event);
		}
		Object[] children = event.children;
		AccessibleObject[] oldChildren = this.children;
		int count = children != null ? children.length : 0;
		AccessibleObject[] newChildren = new AccessibleObject[count];
		for (int i = 0; i < count; i++) {
			Object child = children [i];
			AccessibleObject object = null;
			if (child instanceof Integer) {
				int id = ((Integer)child).intValue();
				object = oldChildren != null && i < oldChildren.length ? oldChildren [i] : null;
				if (object == null || object.id != id) {
					event = new AccessibleControlEvent (accessible);
					event.childID = id;
					for (int j = 0; j < length; j++) {
						AccessibleControlListener listener = (AccessibleControlListener)listeners.elementAt (j);
						listener.getChild (event);
					}
					if (event.accessible != null) {
						object = event.accessible.getAccessibleObject();
						if (object != null)	OS.g_object_ref(object.handle);
					} else {
						object = AccessibleFactory.createChildAccessible (accessible, id);
					}
					object.id = id;
				} else {
					OS.g_object_ref(object.handle);
				}
			} else if (child instanceof Accessible) {
				object = ((Accessible)child).getAccessibleObject();
				if (object != null)	OS.g_object_ref(object.handle);
			}
			if (object != null) {
				object.index = i;
				object.parent = this;
				newChildren[i] = object;
			}
		}
		if (oldChildren != null) {
			for (int i = 0; i < oldChildren.length; i++) {
				AccessibleObject object = oldChildren [i];
				if (object != null) OS.g_object_unref(object.handle);
			}
		}
		this.children = newChildren;
	}

}
