package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class MenuItem extends Item {
	Menu parent, menu;
	int accelerator;
	boolean enabled = true;
	
public MenuItem (Menu parent, int style) {
	this (parent, style, parent.getItemCount());
}

public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (index);
}

public void addArmListener (ArmListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}

public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget();
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
	state |= HANDLE;
	int count = parent.getItemCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int parentHandle = parent.handle;
	if ((style & SWT.SEPARATOR) != 0) {
		handle = OS.PtCreateWidget (OS.PtSeparator (), parentHandle, 0, null);	
	} else if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int [] args = {
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
	}
}

public int getAccelerator () {
	checkWidget();
	return accelerator;
}

public Display getDisplay () {
	Menu parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	/*
	* Bug in Photon. The Pt_BLOCKED flag of a menu item is cleared
	* when its parent menu is realized. The fix is to remember
	* the menu item state and reset it when the menu item is
	* realized.
	*/
//	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
//	OS.PtGetResources (handle, args.length / 3, args);
//	return (args [1] & OS.Pt_BLOCKED) == 0;
	return enabled;
}

public Menu getMenu () {
	checkWidget();
	return menu;
}

String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}

public Menu getParent () {
	checkWidget();
	return parent;
}

public boolean getSelection () {	
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] args = {OS.Pt_ARG_FLAGS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return (args [1] & OS.Pt_SET) != 0;
}

void hookEvents () {
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = getDisplay ().windowProc;
	if ((style & SWT.CASCADE) != 0) {
		OS.PtAddCallback (handle, OS.Pt_CB_ARM, windowProc, SWT.Arm);
	}
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.Selection);
	if ((parent.style & SWT.BAR) == 0) {
		OS.PtAddCallback (handle, OS.Pt_CB_REALIZED, windowProc, SWT.Show);
	}
}

public boolean isEnabled () {
	return getEnabled () && parent.isEnabled ();
}

int processActivate (int info) {
	showMenu ();
	return OS.Pt_CONTINUE;
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

int processShow (int info) {
	/*
	* Bug in Photon. The Pt_BLOCKED flag of a menu item is cleared
	* when its parent menu is realized. The fix is to remember
	* the menu item state and reset it when the menu item is
	* realized.
	*/
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	return OS.Pt_CONTINUE;
}

int processArm(int info) {
	postEvent (SWT.Arm);
	showMenu ();
	return OS.Pt_CONTINUE;
}

void releaseChild () {
	super.releaseChild ();
	if (menu != null) menu.dispose ();
	menu = null;
}

void releaseWidget () {
	if (menu != null && !menu.isDisposed ()) {
		menu.releaseWidget ();
		menu.releaseHandle ();
	}
	menu = null;
	super.releaseWidget ();
	if (accelerator != 0) removeAccelerator ();
	accelerator = 0;
	parent = null;
}

public void removeArmListener (ArmListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
}

public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

void removeAccelerator () {
	if (accelerator == 0) return;

	int keyMods = 0;
	if ((accelerator & SWT.ALT) != 0) keyMods |= OS.Pk_KM_Alt;
	if ((accelerator & SWT.SHIFT) != 0) keyMods |= OS.Pk_KM_Shift;
	if ((accelerator & SWT.CONTROL) != 0) keyMods |= OS.Pk_KM_Ctrl;
	int key = (accelerator & ~(SWT.ALT | SWT.SHIFT | SWT.CONTROL));
	Display display = getDisplay ();
	int keyCode = display.untranslateKey (key);
	if (keyCode != 0) key = keyCode;
	else key = Character.toLowerCase ((char)key);
	Shell shell = parent.getShell ();
	OS.PtRemoveHotkeyHandler(shell.shellHandle, key, keyMods, (short)0, handle, display.hotkeyProc);
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setAccelerator (int accelerator) {
	checkWidget();
	removeAccelerator ();

	this.accelerator = accelerator;		
	if (accelerator == 0) return;

	int keyMods = 0;
	if ((accelerator & SWT.ALT) != 0) keyMods |= OS.Pk_KM_Alt;
	if ((accelerator & SWT.SHIFT) != 0) keyMods |= OS.Pk_KM_Shift;
	if ((accelerator & SWT.CONTROL) != 0) keyMods |= OS.Pk_KM_Ctrl;
	int key = (accelerator & ~(SWT.ALT | SWT.SHIFT | SWT.CONTROL));
	Display display = getDisplay ();
	int keyCode = display.untranslateKey (key);
	if (keyCode != 0) key = keyCode;
	else key = Character.toLowerCase ((char)key);
	Shell shell = parent.getShell ();
	OS.PtAddHotkeyHandler(shell.shellHandle, key, keyMods, (short)0, handle, display.hotkeyProc);
}

public void setEnabled (boolean enabled) {
	checkWidget();
	this.enabled = enabled;
	int [] args = {
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_BLOCKED, OS.Pt_BLOCKED,
		OS.Pt_ARG_FLAGS, enabled ? 0 : OS.Pt_GHOST, OS.Pt_GHOST,
	};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	
	//NOT DONE - The OS has support
}

public void setMenu (Menu menu) {
	checkWidget();
	if ((style & SWT.CASCADE) == 0) {
		error (SWT.ERROR_MENUITEM_NOT_CASCADE);
	}
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
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
		if ((parent.style & SWT.BAR) == 0) {
			int [] args = {OS.Pt_ARG_BUTTON_TYPE, OS.Pt_MENU_TEXT, 0};
			OS.PtSetResources (handle, args.length / 3, args);
		}
	}
	if (menu != null) {
		menu.cascade = this;
		if ((parent.style & SWT.BAR) == 0) {
			int [] args = {OS.Pt_ARG_BUTTON_TYPE, OS.Pt_MENU_RIGHT, 0};
			OS.PtSetResources (handle, args.length / 3, args);		
		}
	}
}

public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	int [] args = {OS.Pt_ARG_FLAGS, selected ? OS.Pt_SET : 0, OS.Pt_SET};
	OS.PtSetResources (handle, args.length / 3, args);
}

public void setText (String string) {
	checkWidget();
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
	int keyMods = 0; 
	byte [] buffer2 = new byte [1];
	if (accel && ++i < text.length) {
		int start = i;
//		while (i < text.length) {
//			if (text [i] == '+') {
//				String str = new String (text, start, i - start);
//				if (str.equals ("Ctrl")) keyMods |= OS.Pk_KM_Ctrl;
//				if (str.equals ("Shift")) keyMods |= OS.Pk_KM_Shift;
//				if (str.equals ("Alt")) keyMods |= OS.Pk_KM_Alt;
//				start = i + 1;
//			}
//			i++;
//		}
		if (start < text.length) {
			char [] accelText = new char [text.length - start];
			System.arraycopy (text, start, accelText, 0, accelText.length);
			buffer2 = Converter.wcsToMbcs (null, accelText, true);
		}
	}
	while (j < text.length) text [j++] = 0;
	byte [] buffer1 = Converter.wcsToMbcs (null, text, true);
	int ptr1 = OS.malloc (buffer1.length);
	OS.memmove (ptr1, buffer1, buffer1.length);
	int ptr2 = OS.malloc (buffer2.length);
	OS.memmove (ptr2, buffer2, buffer2.length);
	int ptr3 = 0;
	if (mnemonic != 0) {
		byte [] buffer3 = Converter.wcsToMbcs (null, new char []{mnemonic}, true);
		ptr3 = OS.malloc (buffer3.length);
		OS.memmove (ptr3, buffer3, buffer3.length);
	}
	if ((parent.style & SWT.BAR) != 0) {
		replaceMnemonic (mnemonic, false, true);
	}
	int [] args = {
		OS.Pt_ARG_TEXT_STRING, ptr1, 0,
		OS.Pt_ARG_ACCEL_TEXT, ptr2, 0,
		OS.Pt_ARG_MODIFIER_KEYS, keyMods, keyMods,
		OS.Pt_ARG_ACCEL_KEY, ptr3, 0,
	};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr1);
	OS.free (ptr2);
	OS.free (ptr3);
	/*
	* Bug on Photon.  When a the text is set on a menu
	* item that is realized, the menu item does not resize
	* to show the new text.  The fix is to force the item
	* to recalculate the size.
	*/
	if (OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
}

void showMenu() {
	if (menu == null)  return;
	int menuHandle = menu.handle;
	if (!OS.PtWidgetIsRealized (menuHandle)) {
		if ((parent.style & SWT.BAR) == 0) {
			int [] args = {OS.Pt_ARG_MENU_FLAGS, OS.Pt_MENU_CHILD, OS.Pt_MENU_CHILD};
			OS.PtSetResources (menuHandle, args.length / 3, args);
		}
		OS.PtReParentWidget (menuHandle, handle);
		
		/*
		* Bug in Photon. PtPositionMenu does not position the menu
		* properly when the menu is a direct child a menu bar item.
		* The fix is to position the menu ourselfs.
		*/
		if ((parent.style & SWT.BAR) != 0) {
			PhPoint_t pt = new PhPoint_t ();
			short [] x = new short [1], y = new short [1];
			OS.PtGetAbsPosition (handle, x, y);
			pt.x = x [0];
			pt.y = y [0];
			int [] args = {OS.Pt_ARG_HEIGHT, 0, 0};
			OS.PtGetResources (handle, args.length / 3, args);
			pt.y += args [1];
			int ptr = OS.malloc (PhPoint_t.sizeof);
			OS.memmove (ptr, pt, PhPoint_t.sizeof);
			args = new int [] {OS.Pt_ARG_POS, ptr, 0};
			OS.PtSetResources (menuHandle, args.length / 3, args);
			OS.free (ptr);
		} else {
			OS.PtPositionMenu (menuHandle, null);
		}
		
		menu.sendEvent (SWT.Show);
		OS.PtRealizeWidget (menuHandle);
	}
}

}
