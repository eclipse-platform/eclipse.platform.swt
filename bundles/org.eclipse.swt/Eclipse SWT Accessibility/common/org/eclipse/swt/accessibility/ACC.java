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
	public static final int STATE_SYSTEM_NORMAL = 0x00000000;
	public static final int STATE_SYSTEM_SELECTED = 0x00000002;
	public static final int STATE_SYSTEM_SELECTABLE = 0x00200000;
	public static final int STATE_SYSTEM_MULTISELECTABLE = 0x1000000;
	public static final int STATE_SYSTEM_FOCUSED = 0x00000004;
	public static final int STATE_SYSTEM_FOCUSABLE = 0x00100000;
	public static final int STATE_SYSTEM_PRESSED = 0x8;
	public static final int STATE_SYSTEM_CHECKED = 0x10;
	public static final int STATE_SYSTEM_EXPANDED = 0x200;
	public static final int STATE_SYSTEM_COLLAPSED = 0x400;
	public static final int STATE_SYSTEM_HOTTRACKED = 0x80;
	public static final int STATE_SYSTEM_BUSY = 0x800;
	public static final int STATE_SYSTEM_READONLY = 0x40;
	public static final int STATE_SYSTEM_INVISIBLE = 0x8000;
	public static final int STATE_SYSTEM_OFFSCREEN = 0x10000;
	public static final int STATE_SYSTEM_SIZEABLE = 0x20000;

	public static final int ROLE_SYSTEM_MENUBAR = 0x2;
	public static final int ROLE_SYSTEM_SCROLLBAR = 0x3;
	public static final int ROLE_SYSTEM_ALERT = 0x8;
	public static final int ROLE_SYSTEM_WINDOW = 0x9;
	public static final int ROLE_SYSTEM_CLIENT = 0xa;
	public static final int ROLE_SYSTEM_MENUPOPUP = 0xb;
	public static final int ROLE_SYSTEM_MENUITEM = 0xc;
	public static final int ROLE_SYSTEM_TOOLTIP = 0xd;
	public static final int ROLE_SYSTEM_DIALOG = 0x12;
	public static final int ROLE_SYSTEM_SEPARATOR = 0x15;
	public static final int ROLE_SYSTEM_TOOLBAR = 0x16;
	public static final int ROLE_SYSTEM_TABLE = 0x18;
	public static final int ROLE_SYSTEM_COLUMNHEADER = 0x19;
	public static final int ROLE_SYSTEM_ROWHEADER = 0x1a;
	public static final int ROLE_SYSTEM_HELPBALLOON = 0x1f;
	public static final int ROLE_SYSTEM_LIST = 0x21;
	public static final int ROLE_SYSTEM_LISTITEM = 0x22;
	public static final int ROLE_SYSTEM_OUTLINE = 0x23;
	public static final int ROLE_SYSTEM_PAGETAB = 0x25;
	public static final int ROLE_SYSTEM_GRAPHIC = 0x28;
	public static final int ROLE_SYSTEM_STATICTEXT = 0x29;
	public static final int ROLE_SYSTEM_TEXT = 0x2a;
	public static final int ROLE_SYSTEM_PUSHBUTTON = 0x2b;
	public static final int ROLE_SYSTEM_CHECKBUTTON = 0x2c;
	public static final int ROLE_SYSTEM_RADIOBUTTON = 0x2d;
	public static final int ROLE_SYSTEM_COMBOBOX = 0x2e;
	public static final int ROLE_SYSTEM_PROGRESSBAR = 0x30;
	public static final int ROLE_SYSTEM_SLIDER = 0x33;
	public static final int ROLE_SYSTEM_PAGETABLIST = 0x3c;
	public static final int ROLE_SYSTEM_CLOCK = 0x3d;

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
