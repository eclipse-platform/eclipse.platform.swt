package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * Class ACC contains all the constants used in defining an
 * Accessible object.
 * 
 * @since 2.0
 */
public class ACC {
	public static final int STATE_NORMAL = 0x00000000;
	public static final int STATE_SELECTED = 0x00000002;
	public static final int STATE_SELECTABLE = 0x00200000;
	public static final int STATE_MULTISELECTABLE = 0x1000000;
	public static final int STATE_FOCUSED = 0x00000004;
	public static final int STATE_FOCUSABLE = 0x00100000;
	public static final int STATE_PRESSED = 0x8;
	public static final int STATE_CHECKED = 0x10;
	public static final int STATE_EXPANDED = 0x200;
	public static final int STATE_COLLAPSED = 0x400;
	public static final int STATE_HOTTRACKED = 0x80;
	public static final int STATE_BUSY = 0x800;
	public static final int STATE_READONLY = 0x40;
	public static final int STATE_INVISIBLE = 0x8000;
	public static final int STATE_OFFSCREEN = 0x10000;
	public static final int STATE_SIZEABLE = 0x20000;

	public static final int ROLE_MENUBAR = 0x2;
	public static final int ROLE_SCROLLBAR = 0x3;
	public static final int ROLE_ALERT = 0x8;
	public static final int ROLE_WINDOW = 0x9;
	public static final int ROLE_CLIENT = 0xa;
	public static final int ROLE_MENUPOPUP = 0xb;
	public static final int ROLE_MENUITEM = 0xc;
	public static final int ROLE_TOOLTIP = 0xd;
	public static final int ROLE_DIALOG = 0x12;
	public static final int ROLE_SEPARATOR = 0x15;
	public static final int ROLE_TOOLBAR = 0x16;
	public static final int ROLE_TABLE = 0x18;
	public static final int ROLE_COLUMNHEADER = 0x19;
	public static final int ROLE_ROWHEADER = 0x1a;
	public static final int ROLE_HELPBALLOON = 0x1f;
	public static final int ROLE_LIST = 0x21;
	public static final int ROLE_LISTITEM = 0x22;
	public static final int ROLE_OUTLINE = 0x23;
	public static final int ROLE_PAGETAB = 0x25;
	public static final int ROLE_GRAPHIC = 0x28;
	public static final int ROLE_STATICTEXT = 0x29;
	public static final int ROLE_TEXT = 0x2a;
	public static final int ROLE_PUSHBUTTON = 0x2b;
	public static final int ROLE_CHECKBUTTON = 0x2c;
	public static final int ROLE_RADIOBUTTON = 0x2d;
	public static final int ROLE_COMBOBOX = 0x2e;
	public static final int ROLE_PROGRESSBAR = 0x30;
	public static final int ROLE_SLIDER = 0x33;
	public static final int ROLE_PAGETABLIST = 0x3c;

	public static final int NAVDIR_UP = 0x1;
	public static final int NAVDIR_DOWN = 0x2;
	public static final int NAVDIR_LEFT = 0x3;
	public static final int NAVDIR_RIGHT = 0x4;
	public static final int NAVDIR_NEXT = 0x5;
	public static final int NAVDIR_PREVIOUS = 0x6;
	public static final int NAVDIR_FIRSTCHILD = 0x7;
	public static final int NAVDIR_LASTCHILD = 0x8;
	
	public static final int CHILDID_SELF = -1;
	public static final int CHILDID_NONE = -2;
	public static final int CHILDID_MULTIPLE = -3; // look for a better solution...
}
