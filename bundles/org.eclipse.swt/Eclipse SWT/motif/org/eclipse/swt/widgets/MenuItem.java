package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object
 * that issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CHECK, CASCADE, PUSH, RADIO, SEPARATOR</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Arm, Help, Selection</dd>
 * </dl>
 *<p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class MenuItem extends Item {
	int accelerator;
	Menu parent, menu;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public MenuItem (Menu parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	createWidget (OS.XmLAST_POSITION);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	if (index == OS.XmLAST_POSITION) error (SWT.ERROR_INVALID_RANGE);
	createWidget (index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the arm events are generated for the control, by sending
 * it one of the messages defined in the <code>ArmListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ArmListener
 * @see #removeArmListener
 */
public void addArmListener (ArmListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the help events are generated for the control, by sending
 * it one of the messages defined in the <code>HelpListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the stateMask field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
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
			OS.XmNancestorSensitive, 1,
			OS.XmNorientation, (parent.style & SWT.BAR) != 0 ? OS.XmVERTICAL : OS.XmHORIZONTAL,
			OS.XmNpositionIndex, index,
		};
		handle = OS.XmCreateSeparatorGadget (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int [] argList = {
		OS.XmNancestorSensitive, 1,
		OS.XmNpositionIndex, index,
	};
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
 * Return the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 *
 * @return the accelerator
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAccelerator () {
	checkWidget();
	return accelerator;
}
public Display getDisplay () {
	Menu parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget();
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
 * Returns the receiver's cascade menu if it has one or null
 * if it does not. Only <code>CASCADE</code> menu items can have
 * a pull down menu. The sequence of key strokes, button presses 
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget();
	return menu;
}
String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}
/**
 * Returns the receiver's parent, which must be a <code>Menu</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParent () {
	checkWidget();
	return parent;
}
/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	int [] argList = {OS.XmNset, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Returns the button label.  The label may
* include the mnemonic character but must not contain line
* delimiters.
*/
String getText2 () {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return "";
	int [] argList = {OS.XmNlabelString, 0, OS.XmNmnemonic, 0, OS.XmNacceleratorText, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int xmString1 = argList [1], xmString2 = argList [5];
	int mnemonic = argList [3];
	if (mnemonic == OS.XK_VoidSymbol) mnemonic = 0;
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
		/* Use the character encoding for the default locale */
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
			/* Use the character encoding for the default locale */
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
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isEnabled () {
	return getEnabled ();
}
String keysymName (int keysym) {
	switch (keysym) {
		case 8: return "BackSpace";
		case 9: return "Tab";
		/*
		* Bug in Motif. For some reason, the XmNaccelerator
		* resource will not accept Linefeed and prints Xt
		* warnings.  The fix is to use Return instead.
		*/
//		case 10: return "Linefeed";
		case 10:
		case 13: return "Return";
		case 27: return "Escape";
		case 127: return "Delete";
	}
	if (('0' <= keysym && keysym <= '9') ||
		('a' <= keysym && keysym <= 'z') ||
		('A' <= keysym && keysym <= 'Z')) {
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
	/* Use the character encoding for the default locale */
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
	XmAnyCallbackStruct struct = new XmAnyCallbackStruct ();
	OS.memmove (struct, callData, XmAnyCallbackStruct.sizeof);
	Event event = new Event ();
	if (struct.event != 0) {
		XButtonEvent xEvent = new XButtonEvent ();
		OS.memmove (xEvent, struct.event, XAnyEvent.sizeof);
		event.time = xEvent.time;
		switch (xEvent.type) {
			case OS.ButtonPress:
			case OS.ButtonRelease:
			case OS.KeyPress:
			case OS.KeyRelease:
				setInputState (event, xEvent);
				break;
		}
	}
	postEvent (SWT.Selection, event);
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
 * Removes the listener from the collection of listeners who will
 * be notified when the arm events are generated for the control.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ArmListener
 * @see #addArmListener
 */
public void removeArmListener (ArmListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**
 * Sets the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 *
 * @param accelerator an integer that is the bit-wise OR of masks and a key
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAccelerator (int accelerator) {
	checkWidget();
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
		/*
		* Feature in Motif.  Motif does not activate an accelerator
		* when the CapsLoc, NumLoc and NumLock+CapsLoc keys are pressed.
		* In order to activate accelerators when these keys are pressed,
		* it is necessary to look for all of these key sequences.
		*/
		String key = ctrl + alt + shift + "<Key>" + keysymName (keysym);
		String allKeys = key + ",Lock " + key + ",Mod2 " + key + ",Lock Mod2 " + key;
		/* Use the character encoding for the default locale */
		byte [] buffer = Converter.wcsToMbcs (null, allKeys, true);		
		ptr = OS.XtMalloc (buffer.length);
		if (ptr != 0) OS.memmove (ptr, buffer, buffer.length);
	}
	int [] argList = {OS.XmNaccelerator, ptr};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (ptr != 0) OS.XtFree (ptr);
}
/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
 * Sets the receiver's pull down menu to the argument.
 * Only <code>CASCADE</code> menu items can have a
 * pull down menu. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 *
 * @param menu the new pull down menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_DROP_DOWN - if the menu is not a drop down menu</li>
 *    <li>ERROR_MENUITEM_NOT_CASCADE - if the menu item is not a <code>CASCADE</code></li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget();
	/* Check to make sure the new menu is valid */
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
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
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
	checkWidget();
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
		/* Use the character encoding for the default locale */
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
	/* Use the character encoding for the default locale */
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
	if (mnemonic == 0) mnemonic = OS.XK_VoidSymbol;
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
