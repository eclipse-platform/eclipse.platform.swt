package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
*	A menu item is a selectable user interface object
* that issues notificiation when pressed and released. 
*
* <p>
* <b>Styles</b><br>
* <dd>CASCADE, CHECK, PUSH, RADIO, SEPARATOR<br>
* <b>Events</b><br>
* <dd>Selection<br>
*/

/* Class Definition */
public /*final*/ class MenuItem extends Item {
	int accelerator;
	Menu parent, menu;
/**
* Creates a new instance of the widget.
*/
public MenuItem (Menu parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (OS.XmLAST_POSITION);
}
/**
* Creates a new instance of the widget.
*/
public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	if (index == OS.XmLAST_POSITION) error (SWT.ERROR_INVALID_RANGE);
	createWidget (index);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addArmListener (ArmListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
	int [] argList1 = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
	OS.XtGetValues (parentHandle, argList1, argList1.length / 2);
	if (index != OS.XmLAST_POSITION && argList1 [1] != 0) {
		int [] handles = new int [argList1 [3]];
		OS.memmove (handles, argList1 [1], argList1 [3] * 4);
		int i = 0, count = 0;
		while (i < argList1 [3]) {
			if (OS.XtIsManaged (handles [i])) {
				if (index == count) break;
				count++;
			}
			i++;
		}
		if (index != count) error (SWT.ERROR_INVALID_RANGE);
		index = i;
	}
	if ((style & SWT.SEPARATOR) != 0) {
		int [] argList = {
			OS.XmNorientation, (parent.style & SWT.BAR) != 0 ? OS.XmVERTICAL : OS.XmHORIZONTAL,
			OS.XmNpositionIndex, index,
		};
		handle = OS.XmCreateSeparatorGadget (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int [] argList = {OS.XmNpositionIndex, index};
	if ((style & SWT.PUSH) != 0) {
		handle = OS.XmCreatePushButtonGadget (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		handle = OS.XmCreateToggleButtonGadget (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	handle = OS.XmCreateCascadeButtonGadget (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}
void destroyWidget () {
	/*
	* Feature in Motif.  When a popup menu item
	* is destroyed, the menu does not recompute
	* the menu size until the next time the menu
	* is managed.  This means that the user can
	* watch the menu get updated as new items are
	* added and old ones deleted.  The fix is to
	* unmanaged the item before destroying it to
	* force the menu to recompute the menu size.
	*/
	OS.XtUnmanageChild (handle);
	super.destroyWidget ();
}
/**
* Gets the accelerator.
* <p>
*   This method gets the widget accelerator.  An accelerator
* is the bit-wise OR of zero or more modifier masks and a key.
* Examples:  SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2.
*
* @param accelerator the accelerator
*
* @return the accelerator
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getAccelerator () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return accelerator;
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Menu parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
* Gets the enabled state.
* <p>
* A disabled widget is typically not selectable from
* the user interface and draws with an inactive or
* grayed look.
*
* @return a boolean that is the enabled state of the widget.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Gets the menu.
* <p>
*	For a cascade menu item, the menu is a pulldown menu
* that is displayed when the user selects the cascade item.
* For a window, the menu is always a popup menu, that is
* displayed when the user requests a popup menu for the
* window.  The sequence of key strokes or button presses
* that requests a menu is platform specific.
*
* @return the menu
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Menu getMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menu;
}
String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}
/**
* Gets the parent.
* <p>
* @return the parent
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Menu getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}
/**
* Gets the selection state.
* <p>
*	This method gets the widget selection state for a
* widgets with the style CHECK or RADIO.  It
* returns false for widgets that do not have one of
* these styles.
* true or false - for CHECK or RADIO.
* false - for all other widget styles
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	int [] argList = {OS.XmNset, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Get the button text.
*
* RETURNS
*
* 	A String that is the label of the button.
*
* REMARKS
*
*	This method returns the button label.  The label may
* include the mnemonic character but must not contain line
* delimiters.
*
**/
String getText2 () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.ARROW) != 0) return "";
	int [] argList = {OS.XmNlabelString, 0, OS.XmNmnemonic, 0, OS.XmNacceleratorText, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int xmString1 = argList [1], mnemonic = argList [3], xmString2 = argList [5];
	if (xmString1 == 0) error (SWT.ERROR_CANNOT_GET_TEXT);
	char [] result = null;	
	int address = OS.XmStringUnparse (
		xmString1,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		null,
		0,
		OS.XmOUTPUT_ALL);
	if (address != 0) {
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result = Converter.mbcsToWcs (null, buffer);
	}
	String accelText = "";
	if (xmString1 != 0) OS.XmStringFree (xmString1);
	if (xmString2 != 0) {
		address = OS.XmStringUnparse (
			xmString2,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (address != 0) {
			int length = OS.strlen (address);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, address, length);
			OS.XtFree (address);
			accelText = '\t' + new String (Converter.mbcsToWcs (null, buffer));
		}
	}
	if (xmString2 != 0) OS.XmStringFree (xmString2);
	if (result == null) return accelText;
	int count = 0;
	if (mnemonic != 0) count++;
	for (int i=0; i<result.length-1; i++)
		if (result [i] == Mnemonic) count++;
	char [] newResult = result;
	if ((count != 0) || (mnemonic != 0)) {
		newResult = new char [result.length + count];
		int i = 0, j = 0;
		while (i < result.length) {
			if ((mnemonic != 0) && (result [i] == mnemonic)) {
				if (j < newResult.length) newResult [j++] = Mnemonic;
				mnemonic = 0;
			}
			if ((newResult [j++] = result [i++]) == Mnemonic)
				if (j < newResult.length) newResult [j++] = Mnemonic;
		}
	}
	return new String (newResult) + accelText;
}
void hookEvents () {
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = parent.getShell ().getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNhelpCallback, windowProc, SWT.Help);
	if ((style & SWT.CASCADE) != 0) {
		OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, SWT.Arm);
		OS.XtAddCallback (handle, OS.XmNcascadingCallback, windowProc, SWT.Arm);
	} else {
		OS.XtAddCallback (handle, OS.XmNarmCallback, windowProc, SWT.Arm);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, SWT.Selection);
	} else {
		OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, SWT.Selection);
	}
}
/**
* Gets the enabled state.
* <p>
*	This method gets the enabled state of the widget
* in the widget hierarchy.  When an ancestor of the
* widget is disabled, the enabled state of the widget
* in the widget hierarchy is disabled regardless of
* the actual enabled state of the widget.
*
* @param enabled a boolean that is the enabled state.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean isEnabled () {
	return getEnabled ();
}
String keysymName (int keysym) {
	if (keysym == 8) return "BackSpace";
	if (keysym == 13) return "Return";
	if (keysym == 27) return "Escape";
	if (keysym == 127) return "Delete";
	if (33 <= keysym && keysym <= 126) {
		return new String (new char [] {(char) keysym});
	}
	/**
	 * Note that XKeysymToString returns a value in a static
	 * area which must not be modified or freed.
	 */
	int ptr = OS.XKeysymToString (keysym);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, buffer.length);
	return new String (Converter.mbcsToWcs (null, buffer));
}
void manageChildren () {
	OS.XtManageChild (handle);
}
int processArm (int callData) {
	postEvent (SWT.Arm);
	return 0;
}
int processHelp (int callData) {
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return 0;
	}
	parent.sendHelpEvent (callData);
	return 0;
}
int processSelection (int callData) {
	postEvent (SWT.Selection);
	return 0;
}
void releaseChild () {
	super.releaseChild ();
	if (menu != null) menu.dispose ();
	menu = null;
}
void releaseWidget () {
	if (menu != null && !menu.isDisposed()) {
		menu.releaseWidget ();
		menu.releaseHandle ();
	}
	menu = null;
	super.releaseWidget ();
	accelerator = 0;
	if (this == parent.defaultItem) {
		parent.defaultItem = null;
	}
	parent = null;
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeArmListener (ArmListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**
* Sets the accelerator.
*/
public void setAccelerator (int accelerator) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.accelerator = accelerator;
	int ptr = 0;
	if (accelerator != 0) {
		String ctrl, alt, shift;
		ctrl = alt = shift = "";
		if ((accelerator & SWT.ALT) != 0) alt = "Meta ";
		if ((accelerator & SWT.SHIFT) != 0) shift = "Shift ";
		if ((accelerator & SWT.CTRL) != 0) ctrl = "Ctrl ";
		int keysym = accelerator & ~(SWT.ALT | SWT.SHIFT | SWT.CTRL);
		int newKey = Display.untranslateKey (keysym);
		if (newKey != 0) {
			keysym = newKey;
		} else {
			keysym = wcsToMbcs ((char) keysym);
		}
		String key = "<Key>" + keysymName (keysym);
		byte [] buffer = Converter.wcsToMbcs (null, ctrl + alt + shift + key, true);
		ptr = OS.XtMalloc (buffer.length);
		if (ptr != 0) OS.memmove (ptr, buffer, buffer.length);
	}
	int [] argList = {OS.XmNaccelerator, ptr};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (ptr != 0) OS.XtFree (ptr);
}
/**
* Sets the enabled state.
* <p>
* A disabled widget is typically not selectable from
* the user interface and draws with an inactive or
* grayed look.
*
* @param enabled the new value of the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the menu.
* <p>
*	For a cascade menu item, the menu is a pulldown menu
* that is displayed when the user selects the cascade item.
* For a window, the menu is always a popup menu, that is
* displayed when the user requests a popup menu for the
* window.  The sequence of key strokes or button presses
* that requests a menu is platform specific.
*
* @param menu the menu
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setMenu (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	/* Check to make sure the new menu is valid */
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

	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;
	int menuHandle = 0;

	/* Set the new menu in the OS */
	if (menu != null) {
		menu.cascade = this;
		menuHandle = menu.handle;
	}
	int [] argList = {OS.XmNsubMenuId, menuHandle};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the selection state.
* <p>
*	This method sets the widget selection state for a
* widgets with the style CHECK or RADIO.  It
* returns false for widgets that do not have one of
* these styles.
*
* @param selected the new selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	int [] argList = {OS.XmNset, selected ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the widget text.
* <p>
* @param string the widget text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	if ((style & (SWT.ARROW | SWT.SEPARATOR)) != 0) return;
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	boolean accel = false;
	int i=0, j=0, mnemonic=0;
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
	int xmString2 = OS.XmStringParseText (
		buffer2,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	if (xmString2 == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	while (j < text.length) text [j++] = 0;
	byte [] buffer1 = Converter.wcsToMbcs (null, text, true);
	int xmString1 = OS.XmStringParseText (
		buffer1,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	if (xmString1 == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString1,
		OS.XmNmnemonic, mnemonic,
		OS.XmNacceleratorText, xmString2,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (xmString1 != 0) OS.XmStringFree (xmString1);
	if (xmString2 != 0) OS.XmStringFree (xmString2);
}
}
