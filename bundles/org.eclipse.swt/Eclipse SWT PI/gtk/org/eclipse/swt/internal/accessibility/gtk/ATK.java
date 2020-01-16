/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class ATK extends OS {
	static {
		Library.loadLibrary("swt-atk");
	}

	/** Constants */
	public static final int ATK_RELATION_NULL = 0;
	public static final int ATK_RELATION_CONTROLLED_BY = 1;
	public static final int ATK_RELATION_CONTROLLER_FOR = 2;
	public static final int ATK_RELATION_LABEL_FOR = 3;
	public static final int ATK_RELATION_LABELLED_BY = 4;
	public static final int ATK_RELATION_MEMBER_OF = 5;
	public static final int ATK_RELATION_NODE_CHILD_OF = 6;
	public static final int ATK_RELATION_FLOWS_TO = 7;
	public static final int ATK_RELATION_FLOWS_FROM = 8;
	public static final int ATK_RELATION_SUBWINDOW_OF = 9;
	public static final int ATK_RELATION_EMBEDS = 10;
	public static final int ATK_RELATION_EMBEDDED_BY = 11;
	public static final int ATK_RELATION_POPUP_FOR = 12;
	public static final int ATK_RELATION_PARENT_WINDOW_OF = 13;
	public static final int ATK_RELATION_DESCRIBED_BY = 14;
	public static final int ATK_RELATION_DESCRIPTION_FOR = 15;
	public static final int ATK_ROLE_ALERT = 2;
	public static final int ATK_ROLE_ANIMATION = 3;
	public static final int ATK_ROLE_CALENDAR = 4;
	public static final int ATK_ROLE_CANVAS = 6;
	public static final int ATK_ROLE_CHECK_BOX = 7;
	public static final int ATK_ROLE_CHECK_MENU_ITEM = 8;
	public static final int ATK_ROLE_COMBO_BOX = 11;
	public static final int ATK_ROLE_DATE_EDITOR = 12;
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
	public static final int ATK_ROLE_PANEL = 38;
	public static final int ATK_ROLE_PROGRESS_BAR = 41;
	public static final int ATK_ROLE_PUSH_BUTTON = 42;
	public static final int ATK_ROLE_RADIO_BUTTON = 43;
	public static final int ATK_ROLE_RADIO_MENU_ITEM = 44;
	public static final int ATK_ROLE_SCROLL_BAR = 47;
	public static final int ATK_ROLE_SEPARATOR = 49;
	public static final int ATK_ROLE_SLIDER = 50;
	public static final int ATK_ROLE_SPIN_BUTTON = 52;
	public static final int ATK_ROLE_STATUSBAR = 53;
	public static final int ATK_ROLE_TABLE = 54;
	public static final int ATK_ROLE_TABLE_CELL = 55;
	public static final int ATK_ROLE_TABLE_COLUMN_HEADER = 56;
	public static final int ATK_ROLE_TABLE_ROW_HEADER = 57;
	public static final int ATK_ROLE_TEXT = 60;
	public static final int ATK_ROLE_TOOL_BAR = 62;
	public static final int ATK_ROLE_TOOL_TIP = 63;
	public static final int ATK_ROLE_TREE = 64;
	public static final int ATK_ROLE_HEADER = 69;
	public static final int ATK_ROLE_FOOTER = 70;
	public static final int ATK_ROLE_PARAGRAPH = 71;
	public static final int ATK_ROLE_FORM = 85;
	public static final int ATK_ROLE_HEADING = 81;
	public static final int ATK_ROLE_DOCUMENT_FRAME = 80;
	public static final int ATK_ROLE_TABLE_ROW = 88; //since atk 2.1.0
	public static final int ATK_ROLE_IMAGE = 26;
	public static final int ATK_ROLE_PAGE = 82;
	public static final int ATK_ROLE_SECTION = 83;
	public static final int ATK_ROLE_UNKNOWN = 66;
	public static final int ATK_STATE_ACTIVE = 1;
	public static final int ATK_STATE_ARMED = 2;
	public static final int ATK_STATE_BUSY = 3;
	public static final int ATK_STATE_CHECKED = 4;
	public static final int ATK_STATE_DEFUNCT = 5;
	public static final int ATK_STATE_EDITABLE = 6;
	public static final int ATK_STATE_ENABLED = 7;
	public static final int ATK_STATE_EXPANDED = 9;
	public static final int ATK_STATE_FOCUSABLE = 10;
	public static final int ATK_STATE_FOCUSED = 11;
	public static final int ATK_STATE_MULTI_LINE = 15;
	public static final int ATK_STATE_MULTISELECTABLE = 16;
	public static final int ATK_STATE_PRESSED = 18;
	public static final int ATK_STATE_RESIZABLE = 19;
	public static final int ATK_STATE_SELECTABLE = 20;
	public static final int ATK_STATE_SELECTED = 21;
	public static final int ATK_STATE_SHOWING = 23;
	public static final int ATK_STATE_SINGLE_LINE = 24;
	public static final int ATK_STATE_TRANSIENT = 26;
	public static final int ATK_STATE_REQUIRED = 32;
	public static final int ATK_STATE_INVALID_ENTRY = 33;
	public static final int ATK_STATE_SUPPORTS_AUTOCOMPLETION = 34;
	public static final int ATK_STATE_VISIBLE = 28;
	public static final int ATK_TEXT_BOUNDARY_CHAR = 0;
	public static final int ATK_TEXT_BOUNDARY_WORD_START = 1;
	public static final int ATK_TEXT_BOUNDARY_WORD_END = 2;
	public static final int ATK_TEXT_BOUNDARY_SENTENCE_START = 3;
	public static final int ATK_TEXT_BOUNDARY_SENTENCE_END = 4;
	public static final int ATK_TEXT_BOUNDARY_LINE_START = 5;
	public static final int ATK_TEXT_BOUNDARY_LINE_END = 6;
	/* TODO_a11y: use the ATK_TEXT_GRANULARITY_* constants once
	 * Orca is updated to use the new API.
	 */
	public static final int ATK_TEXT_GRANULARITY_CHAR = 0;
	public static final int ATK_TEXT_GRANULARITY_WORD = 1;
	public static final int ATK_TEXT_GRANULARITY_SENTENCE = 2;
	public static final int ATK_TEXT_GRANULARITY_LINE = 3;
	public static final int ATK_TEXT_GRANULARITY_PARAGRAPH = 4;
	public static final int ATK_TEXT_CLIP_NONE = 0;
	public static final int ATK_TEXT_CLIP_MIN = 1;
	public static final int ATK_TEXT_CLIP_MAX = 2;
	public static final int ATK_TEXT_CLIP_BOTH = 3;
	public static final int ATK_TEXT_ATTR_LEFT_MARGIN = 1;
	public static final int ATK_TEXT_ATTR_RIGHT_MARGIN = 2;
	public static final int ATK_TEXT_ATTR_INDENT = 3;
	public static final int ATK_TEXT_ATTR_INVISIBLE = 4;
	public static final int ATK_TEXT_ATTR_EDITABLE = 5;
	public static final int ATK_TEXT_ATTR_PIXELS_ABOVE_LINES = 6;
	public static final int ATK_TEXT_ATTR_PIXELS_BELOW_LINES = 7;
	public static final int ATK_TEXT_ATTR_PIXELS_INSIDE_WRAP = 8;
	public static final int ATK_TEXT_ATTR_BG_FULL_HEIGHT = 9;
	public static final int ATK_TEXT_ATTR_RISE = 10;
	public static final int ATK_TEXT_ATTR_UNDERLINE = 11;
	public static final int ATK_TEXT_ATTR_STRIKETHROUGH = 12;
	public static final int ATK_TEXT_ATTR_SIZE = 13;
	public static final int ATK_TEXT_ATTR_SCALE = 14;
	public static final int ATK_TEXT_ATTR_WEIGHT = 15;
	public static final int ATK_TEXT_ATTR_LANGUAGE = 16;
	public static final int ATK_TEXT_ATTR_FAMILY_NAME = 17;
	public static final int ATK_TEXT_ATTR_BG_COLOR = 18;
	public static final int ATK_TEXT_ATTR_FG_COLOR = 19;
	public static final int ATK_TEXT_ATTR_BG_STIPPLE = 20;
	public static final int ATK_TEXT_ATTR_FG_STIPPLE = 21;
	public static final int ATK_TEXT_ATTR_WRAP_MODE = 22;
	public static final int ATK__TEXT_ATTR_DIRECTION = 23;
	public static final int ATK_TEXT_ATTR_JUSTIFICATION = 24;
	public static final int ATK_TEXT_ATTR_STRETCH = 25;
	public static final int ATK_TEXT_ATTR_VARIANT = 26;
	public static final int ATK_TEXT_ATTR_STYLE = 27;
	public static final int ATK_XY_WINDOW = 1;

	/** Signals */
	public static final byte[] selection_changed = OS.ascii ("selection_changed");
	public static final byte[] text_changed_insert = OS.ascii ("text_changed::insert");
	public static final byte[] text_changed_delete = OS.ascii ("text_changed::delete");
	public static final byte[] text_caret_moved = OS.ascii ("text_caret_moved");
	public static final byte[] text_selection_changed = OS.ascii ("text_selection_changed");
	public static final byte[] load_complete = OS.ascii ("load-complete");
	public static final byte[] load_stopped = OS.ascii ("load-stopped");
	public static final byte[] reload = OS.ascii ("reload");
	public static final byte[] bounds_changed = OS.ascii ("bounds-changed");
	public static final byte[] link_activated = OS.ascii ("link-activated");
	public static final byte[] link_selected = OS.ascii ("link-selected");
	public static final byte[] attributes_changed = OS.ascii ("attributes-changed");
	public static final byte[] text_attributes_changed = OS.ascii ("text-attributes-changed");
	public static final byte[] column_deleted = OS.ascii ("column-deleted");
	public static final byte[] column_inserted = OS.ascii ("column-inserted");
	public static final byte[] row_deleted = OS.ascii ("row-deleted");
	public static final byte[] row_inserted = OS.ascii ("row-inserted");
	public static final byte[] focus_event = OS.ascii ("focus-event");

	/** Properties */
	public static final byte[] accessible_name = OS.ascii ("accessible-name");
	public static final byte[] accessible_description = OS.ascii ("accessible-description");
	public static final byte[] accessible_value = OS.ascii ("accessible-value");
	public static final byte[] end_index = OS.ascii ("end-index");
	public static final byte[] number_of_anchors = OS.ascii ("number-of-anchors");
	public static final byte[] selected_link = OS.ascii ("selected-link");
	public static final byte[] start_index = OS.ascii ("start-index");
	public static final byte[] accessible_hypertext_nlinks = OS.ascii ("accessible-hypertext-nlinks");
	public static final byte[] accessible_table_caption_object = OS.ascii ("accessible-table-caption-object");
	public static final byte[] accessible_table_column_description = OS.ascii ("accessible-table-column-description");
	public static final byte[] accessible_table_column_header = OS.ascii ("accessible-table-column-header");
	public static final byte[] accessible_table_row_description = OS.ascii ("accessible-table-row-description");
	public static final byte[] accessible_table_row_header = OS.ascii ("accessible-table-row-header");
	public static final byte[] accessible_table_summary = OS.ascii ("accessible-table-summary");

/** 64 bit */
public static final native int AtkAttribute_sizeof ();
public static final native int AtkTextRange_sizeof ();
public static final native int AtkTextRectangle_sizeof ();

/** Natives */

/** @method flags=const */
public static final native long ATK_TYPE_ACTION ();
/** @method flags=const */
public static final native long ATK_TYPE_COMPONENT ();
/** @method flags=const */
public static final native long ATK_TYPE_EDITABLE_TEXT ();
/** @method flags=const */
public static final native long ATK_TYPE_HYPERTEXT ();
/** @method flags=const */
public static final native long ATK_TYPE_SELECTION ();
/** @method flags=const */
public static final native long ATK_TYPE_TABLE ();
/** @method flags=const */
public static final native long ATK_TYPE_TEXT ();
/** @method flags=const */
public static final native long ATK_TYPE_VALUE ();
public static final native long ATK_ACTION_GET_IFACE(long obj);
public static final native long ATK_COMPONENT_GET_IFACE(long atkHandle);
public static final native long ATK_SELECTION_GET_IFACE(long obj);
public static final native long ATK_EDITABLE_TEXT_GET_IFACE(long handle);
public static final native long ATK_HYPERTEXT_GET_IFACE(long handle);
public static final native long ATK_TABLE_GET_IFACE(long handle);
public static final native long ATK_TEXT_GET_IFACE(long handle);
public static final native long ATK_VALUE_GET_IFACE(long handle);
/** @param accessible cast=(AtkObject *) */
public static final native void atk_object_notify_state_change(long accessible, int state, boolean value);
/**
 * @param object cast=(AtkObject *)
 * @param relationship cast=(AtkRelationType)
 * @param target cast=(AtkObject *)
 */
public static final native boolean atk_object_add_relationship(long object, int relationship, long target);
/**
 * @param object cast=(AtkObject *)
 * @param relationship cast=(AtkRelationType)
 * @param target cast=(AtkObject *)
 */
public static final native boolean atk_object_remove_relationship(long object, int relationship, long target);
/**
 * @param set cast=(AtkStateSet *)
 * @param type cast=(AtkStateType)
 */
public static final native boolean atk_state_set_add_state(long set, int type);
public static final native long atk_text_attribute_get_name(int attr);
public static final native long atk_text_attribute_get_value(int attr, int index);
public static final native long call(long function, long arg0);
public static final native long call(long function, long arg0, long arg1);
public static final native long call(long function, long arg0, long arg1, long arg2);
public static final native long call(long function, long arg0, long arg1, long arg2, long arg3, long arg4);
public static final native void memmove (AtkActionIface dest, long src);
public static final native void memmove (AtkComponentIface dest, long src);
public static final native void memmove (AtkEditableTextIface dest, long src);
public static final native void memmove (AtkHypertextIface dest, long src);
public static final native void memmove (AtkObjectClass dest, long src);
public static final native void memmove (AtkSelectionIface dest, long src);
public static final native void memmove (AtkTableIface dest, long src);
public static final native void memmove (AtkTextIface dest, long src);
public static final native void memmove (AtkValueIface dest, long src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, AtkTextRectangle src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (AtkTextRectangle dest, long src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, AtkTextRange src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (AtkTextRange dest, long src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, AtkAttribute src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (AtkAttribute dest, long src, int size);
}
