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
package org.eclipse.swt.internal.gtk;


import org.eclipse.swt.internal.Library;

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
	public static final int ATK_ROLE_TABLE_COLUMN_HEADER = 56;
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
	public static final synchronized native int ATK_ACTION_GET_IFACE (int obj);
	public static final synchronized native int ATK_COMPONENT_GET_IFACE(int atkHandle);
	public static final synchronized native int ATK_OBJECT_FACTORY_CLASS (int klass);
	public static final synchronized native int ATK_SELECTION_GET_IFACE (int obj);
	public static final synchronized native int ATK_TEXT_GET_IFACE (int handle);
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
	public static final synchronized native void memmove (AtkActionIface dest, int src);
	public static final synchronized native void memmove (AtkComponentIface dest, int src);
	public static final synchronized native void memmove (AtkObjectClass dest, int src);
	public static final synchronized native void memmove (AtkObjectFactoryClass  dest, int src);
	public static final synchronized native void memmove (AtkSelectionIface dest, int src);	
	public static final synchronized native void memmove (AtkTextIface dest, int src);
	public static final synchronized native void memmove (int dest, AtkActionIface src);
	public static final synchronized native void memmove (int dest, AtkComponentIface src);
	public static final synchronized native void memmove (int dest, AtkObjectClass src);
	public static final synchronized native void memmove (int dest, AtkObjectFactoryClass src);
	public static final synchronized native void memmove (int dest, AtkSelectionIface src);
	public static final synchronized native void memmove (int dest, AtkTextIface src);
}
