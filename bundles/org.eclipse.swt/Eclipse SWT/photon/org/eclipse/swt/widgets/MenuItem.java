package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class MenuItem extends Item {
	Menu parent, menu;
	
public MenuItem (Menu parent, int style) {
	this (parent, style, parent.getItemCount());
}

public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (index);
}

public void addArmListener (ArmListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}

public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

void createHandle (int index) {
	int count = parent.getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int parentHandle = parent.handle;
	if ((style & SWT.SEPARATOR) != 0) {
		handle = OS.PtCreateWidget (OS.PtSeparator (), parentHandle, 0, null);	
	} else if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int [] args = {
//			OS.Pt_ARG_FLAGS, OS.Pt_MENU_BUTTON | OS.Pt_SELECTABLE, OS.Pt_MENU_BUTTON | OS.Pt_SELECTABLE,
//			OS.Pt_ARG_FLAGS, OS.Pt_AUTOHIGHLIGHT, OS.Pt_AUTOHIGHLIGHT,
			OS.Pt_ARG_INDICATOR_TYPE, (style & SWT.CHECK) != 0 ? OS.Pt_N_OF_MANY : OS.Pt_ONE_OF_MANY, 0
		};
		handle = OS.PtCreateWidget (OS.PtToggleButton (), parentHandle, args.length / 3, args);	
	} else {
		handle = OS.PtCreateWidget (OS.PtMenuButton (), parentHandle, 0, null);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index != count) {
		int i = 0;
		int child = OS.PtWidgetChildBack (parentHandle);
		/*
		* Feature in Photon.  Menu bars have an extra widget which
		* is the parent of all menu items. PtValidParent() can not be
		* used, since it does not return that widget.
		*/
		if (child != 0  && (parent.style & SWT.BAR) != 0) child = OS.PtWidgetChildBack (child);
		while (i != index && child != 0) {
			child = OS.PtWidgetBrotherInFront (child);
			i++;
		}
		OS.PtWidgetInsert (topHandle (), child, 1);
	}
	if (OS.PtWidgetIsRealized (parentHandle)) {
		OS.PtRealizeWidget (topHandle ());
		if ((parent.style & SWT.BAR) == 0) {
			OS.PtExtentWidgetFamily (parentHandle);
			OS.PtPositionMenu (parentHandle, null);
		}
	}
}

public int getAccelerator () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE - NOT NEEDED
	return 0;
}

public Display getDisplay () {
	Menu parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_BLOCKED) == 0;
}

public Menu getMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menu;
}

String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}

public Menu getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

public boolean getSelection () {	
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_SET) != 0;
}

void hookEvents () {
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = getDisplay ().windowProc;
	if ((style & SWT.CASCADE) != 0) {
		OS.PtAddCallback (handle, OS.Pt_CB_ARM, windowProc, SWT.Show);
	}
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
}

public boolean isEnabled () {
	return getEnabled () && parent.isEnabled ();
}

int processSelection (int info) {
	if ((style & SWT.CASCADE) != 0) {
		int [] args = {OS.Pt_ARG_BUTTON_TYPE, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		if ((args [1] & (OS.Pt_MENU_RIGHT | OS.Pt_MENU_DOWN)) != 0) {
			return OS.Pt_CONTINUE;
		}
	}
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
}

void releaseChild () {
	super.releaseChild ();
	if (menu != null) menu.dispose ();
	menu = null;
//	parent.destroyItem (this);
}

void releaseWidget () {
	if (menu != null) {
		menu.releaseWidget ();
		menu.releaseHandle ();
	}
	menu = null;
	super.releaseWidget ();
//	if (accelerator != 0) {
//		parent.destroyAcceleratorTable ();
//	}
//	accelerator = 0;
//	Decorations shell = parent.parent;
//	shell.remove (this);
	parent = null;
}

int processShow (int damage) {
	if (menu != null) {		
		int menuHandle = menu.handle;
		OS.PtPositionMenu (menuHandle, null);
		OS.PtRealizeWidget (menuHandle);
	}
	return OS.Pt_CONTINUE;
}

public void removeArmListener (ArmListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
}

public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setAccelerator (int accelerator) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	//NOT DONE: remove previous - NEEDED NEEDED
//	
//	if (accelerator != 0) {
//		int keyMods = 0;
//		if ((accelerator & SWT.ALT) != 0) keyMods |= OS.Pk_KM_Alt;
//		if ((accelerator & SWT.SHIFT) != 0) keyMods |= OS.Pk_KM_Shift;
//		if ((accelerator & SWT.CONTROL) != 0) keyMods |= OS.Pk_KM_Ctrl;
//		int key = accelerator & ~(SWT.ALT | SWT.SHIFT | SWT.CONTROL);
//		//key = Display.untranslateKey(key);
//		key = 0x61;
//		System.out.println("key=" + Integer.toHexString(key));
//		OS.PtAddHotkeyHandler(handle, key, keyMods, (short)0, SWT.Selection, 0);
//	}
}

public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	
	//NOT DONE - The OS has support
}

public void setMenu (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.CASCADE) == 0) {
		error (SWT.ERROR_MENUITEM_NOT_CASCADE);
	}
	if (menu != null) {
		if ((menu.style & SWT.DROP_DOWN) == 0) {
			error (SWT.ERROR_MENU_NOT_DROP_DOWN);
		}
		if (menu.parent != parent.parent) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	this.menu = menu;
	if (oldMenu != null) {
		oldMenu.cascade = null;
		int menuHandle = oldMenu.handle;
		int shellHandle = oldMenu.parent.topHandle ();
		if ((parent.style & SWT.BAR) == 0) {
			int [] args = {OS.Pt_ARG_BUTTON_TYPE, 0, OS.Pt_MENU_RIGHT};
			OS.PtSetResources (handle, args.length / 3, args);
			args = new int [] {OS.Pt_ARG_MENU_FLAGS, 0, OS.Pt_MENU_CHILD};
			OS.PtSetResources (menuHandle, args.length / 3, args);
		}
		OS.PtReParentWidget (menuHandle, shellHandle);
	}
	if (menu != null) {
		menu.cascade = this;
		int menuHandle = menu.handle;
		if ((parent.style & SWT.BAR) == 0) {
			int [] args = {OS.Pt_ARG_BUTTON_TYPE, OS.Pt_MENU_RIGHT, OS.Pt_MENU_RIGHT};
			OS.PtSetResources (handle, args.length / 3, args);		
			args = new int [] {OS.Pt_ARG_MENU_FLAGS, OS.Pt_MENU_CHILD, OS.Pt_MENU_CHILD};
			OS.PtSetResources (menuHandle, args.length / 3, args);					
		}
		OS.PtReParentWidget (menuHandle, handle);
	}
}

public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int [] args = {OS.Pt_ARG_FLAGS, selected ? OS.Pt_SET : 0, OS.Pt_SET};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	boolean accel = false;
	int i=0, j=0;
	char mnemonic=0;
	while (i < text.length) {
		if (text [i] == '\t') {accel = true; break;};
		if ((text [j++] = text [i++]) == Mnemonic) {
			if (i == text.length) {continue;}
			if (text [i] == Mnemonic) {i++; continue;}
			if (mnemonic == 0) mnemonic = text [i];
			j--;
		}
	}	
	byte [] buffer2;
	if (accel && ++i < text.length) {
		char [] accelText = new char [text.length - i];
		System.arraycopy (text, i, accelText, 0, accelText.length);
		buffer2 = Converter.wcsToMbcs (null, accelText, true);
	} else {
		buffer2 = new byte [1];
	}
	while (j < text.length) text [j++] = 0;
	byte [] buffer1 = Converter.wcsToMbcs (null, text, true);
	int ptr = OS.malloc (buffer1.length);
	OS.memmove (ptr, buffer1, buffer1.length);
	int ptr3 = 0;
	if (mnemonic != 0) {
		byte [] buffer3 = Converter.wcsToMbcs (null, new char []{mnemonic}, true);
		ptr3 = OS.malloc (buffer3.length);
		OS.memmove (ptr3, buffer3, buffer3.length);
	}
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr, 0,
		OS.Pt_ARG_ACCEL_KEY, ptr3, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
	OS.free (ptr3);
	/*
	* Bug on Photon.  When a the text is set on a menu
	* item that is realized, the menu item does not resize
	* to show the new text.  The fix is to force the item
	* to recalculate the size.
	*/
	if (OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
}

}