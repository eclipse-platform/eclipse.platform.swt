/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;


import org.eclipse.swt.internal.Library;
import org.eclipse.swt.internal.gtk.OS;

public class ATK extends OS {
	static {
		Library.loadLibrary("swt-atk");
	}
	
	/** Constants */
	public static final int ATK_ROLE_CHECK_BOX = 7;
	public static final int ATK_ROLE_COMBO_BOX = 11;
	public static final int ATK_ROLE_DIALOG = 16;
	public static final int ATK_ROLE_DRAWING_AREA = 18;
	public static final int ATK_ROLE_WINDOW = 68;
	public static final int ATK_ROLE_LABEL = 28;
	public static final int ATK_ROLE_LIST = 30;
	public static final int ATK_ROLE_LIST_ITEM = 31;
	public static final int ATK_ROLE_MENU = 32;
	public static final int ATK_ROLE_MENU_BAR = 33;
	public static final int ATK_ROLE_MENU_ITEM = 34;
	public static final int ATK_ROLE_PAGE_TAB = 36;
	public static final int ATK_ROLE_PAGE_TAB_LIST = 37;
	public static final int ATK_ROLE_PROGRESS_BAR = 41;
	public static final int ATK_ROLE_PUSH_BUTTON = 42;
	public static final int ATK_ROLE_RADIO_BUTTON = 43;
	public static final int ATK_ROLE_SCROLL_BAR = 47;
	public static final int ATK_ROLE_SEPARATOR = 49;
	public static final int ATK_ROLE_SLIDER = 50;
	public static final int ATK_ROLE_TABLE = 54;
	public static final int ATK_ROLE_TABLE_CELL = 55;
	public static final int ATK_ROLE_TABLE_COLUMN_HEADER = 56;
	public static final int ATK_ROLE_TABLE_ROW_HEADER = 57;
	public static final int ATK_ROLE_TEXT = 60;
	public static final int ATK_ROLE_TOOL_BAR = 62;
	public static final int ATK_ROLE_TOOL_TIP = 63;
	public static final int ATK_ROLE_TREE = 64;
	public static final int ATK_STATE_ARMED = 2;
	public static final int ATK_STATE_BUSY = 3;
	public static final int ATK_STATE_CHECKED = 4;
	public static final int ATK_STATE_DEFUNCT = 5;
	public static final int ATK_STATE_EDITABLE = 6;
	public static final int ATK_STATE_ENABLED = 7;
	public static final int ATK_STATE_EXPANDED = 9;
	public static final int ATK_STATE_FOCUSABLE = 10;
	public static final int ATK_STATE_FOCUSED = 11;
	public static final int ATK_STATE_MULTISELECTABLE = 16;
	public static final int ATK_STATE_PRESSED = 18;
	public static final int ATK_STATE_RESIZABLE = 19;
	public static final int ATK_STATE_SELECTABLE = 20;
	public static final int ATK_STATE_SELECTED = 21;
	public static final int ATK_STATE_SHOWING = 23;
	public static final int ATK_STATE_TRANSIENT = 26;
	public static final int ATK_STATE_VISIBLE = 28;
	public static final int ATK_TEXT_BOUNDARY_CHAR = 0;
	public static final int ATK_TEXT_BOUNDARY_WORD_START = 1;
	public static final int ATK_TEXT_BOUNDARY_WORD_END = 2;
	public static final int ATK_TEXT_BOUNDARY_SENTENCE_START = 3;
	public static final int ATK_TEXT_BOUNDARY_SENTENCE_END = 4;
	public static final int ATK_TEXT_BOUNDARY_LINE_START = 5;
	public static final int ATK_TEXT_BOUNDARY_LINE_END = 6;
	public static final int ATK_XY_WINDOW = 1;
	
	/** Signals */
	public static final byte[] selection_changed = OS.signal ("selection_changed");
	public static final byte[] text_changed_insert = OS.signal ("text_changed::insert");
	public static final byte[] text_changed_delete = OS.signal ("text_changed::delete");
	public static final byte[] text_caret_moved = OS.signal ("text_caret_moved");
	public static final byte[] text_selection_changed = OS.signal ("text_selection_changed");
	
	/** Native methods */
	public static final synchronized native int ATK_ACTION_GET_IFACE (int obj);
	public static final synchronized native int ATK_COMPONENT_GET_IFACE(int atkHandle);
	public static final synchronized native int ATK_OBJECT_FACTORY_CLASS (int klass);
	public static final synchronized native int ATK_SELECTION_GET_IFACE (int obj);
	public static final synchronized native int ATK_TEXT_GET_IFACE (int handle);
	public static final synchronized native int G_OBJECT_CLASS (int klass);
	public static final synchronized native int G_OBJECT_GET_CLASS (int object);
	public static final synchronized native int G_TYPE_FROM_INSTANCE (int instance);
	public static final synchronized native int GTK_ACCESSIBLE (int handle);
	public static final synchronized native short AtkObjectFactory_sizeof ();
	public static final synchronized native short AtkObjectFactoryClass_sizeof ();
	public static final synchronized native void atk_focus_tracker_notify (int object);
	public static final synchronized native int atk_get_default_registry ();
	public static final synchronized native int atk_object_factory_get_accessible_type (int factory);
	public static final synchronized native void atk_object_initialize (int accessible, int data);
	public static final synchronized native int atk_registry_get_factory (int registry, int type);
	public static final synchronized native void atk_registry_set_factory_type (int registry, int type, int factory_type);
	public static final synchronized native boolean atk_state_set_add_state (int set, int type);
	public static final synchronized native int atk_state_set_new ();
	public static final synchronized native int call (int function, int arg0);
	public static final synchronized native int call (int function, int arg0, int arg1);
	public static final synchronized native int call (int function, int arg0, int arg1, int arg2);
	public static final synchronized native int call (int function, int arg0, int arg1, int arg2, int arg3);
	public static final synchronized native int call (int function, int arg0, int arg1, int arg2, int arg3, int arg4);
	public static final synchronized native int call (int function, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);
	public static final synchronized native int g_object_new (int type, int first_property_name);
	public static final synchronized native void g_type_add_interface_static (int instance_type, int interface_type, int info);
	public static final synchronized native int g_type_class_peek (int g_class);
	public static final synchronized native int g_type_class_peek_parent (int g_class);
	public static final synchronized native int g_type_from_name (byte[] name);
	public static final synchronized native int g_type_interface_peek_parent (int iface);
	public static final synchronized native boolean g_type_is_a (int type, int is_a_type);
	public static final synchronized native int g_type_name (int handle);
	public static final synchronized native int g_type_parent (int type);
	public static final synchronized native void g_type_query (int type, int query);
	public static final synchronized native int g_type_register_static (int parent_type, byte[] type_name, int info, int flags);
	public static final synchronized native int gtk_widget_get_toplevel (int widget);
	public static final synchronized native void memmove (AtkActionIface dest, int src);
	public static final synchronized native void memmove (AtkComponentIface dest, int src);
	public static final synchronized native void memmove (AtkObjectClass dest, int src);
	public static final synchronized native void memmove (AtkObjectFactoryClass  dest, int src);
	public static final synchronized native void memmove (AtkSelectionIface dest, int src);	
	public static final synchronized native void memmove (AtkTextIface dest, int src);
	public static final synchronized native void memmove (GtkAccessible  dest, int src);
	public static final synchronized native void memmove (GObjectClass  dest, int src);
	public static final synchronized native void memmove (GTypeQuery dest, int src, int size);	
	public static final synchronized native void memmove (int dest, AtkActionIface src);
	public static final synchronized native void memmove (int dest, AtkComponentIface src);
	public static final synchronized native void memmove (int dest, AtkObjectClass src);
	public static final synchronized native void memmove (int dest, AtkObjectFactoryClass src);
	public static final synchronized native void memmove (int dest, AtkSelectionIface src);
	public static final synchronized native void memmove (int dest, AtkTextIface src);
	public static final synchronized native void memmove (int dest, GInterfaceInfo src, int size);
	public static final synchronized native void memmove (int dest, GObjectClass src);
	public static final synchronized native void memmove (int dest, GTypeInfo src, int size);
}
