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
package org.eclipse.swt.widgets;


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
 * <p>
 * Note: Only one of the styles CHECK, CASCADE, PUSH, RADIO and SEPARATOR
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a menu control which will be the parent of the new instance (cannot be null)
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
 * @see SWT#CHECK
 * @see SWT#CASCADE
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a menu control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#CHECK
 * @see SWT#CASCADE
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	if (index == OS.XmLAST_POSITION) error (SWT.ERROR_INVALID_RANGE);
	createWidget (index);
}
void addAccelerator () {
	if (accelerator == 0 || !getEnabled ()) return;
	if ((accelerator & SWT.COMMAND) != 0) return;
	/*
	* Bug in Solaris.  When accelerators are set more
	* than once in the same menu bar, the time it takes
	* to set the accelerator increases exponentially.
	* The fix is to implement our own accelerator table
	* on Solaris.
	*/
	if (OS.IsSunOS) return;
	String ctrl, alt, shift;
	ctrl = alt = shift = "";
	if ((accelerator & SWT.ALT) != 0) alt = "Meta ";
	if ((accelerator & SWT.SHIFT) != 0) shift = "Shift ";
	if ((accelerator & SWT.CONTROL) != 0) ctrl = "Ctrl ";
	int keysym = accelerator & SWT.KEY_MASK;
	int newKey = Display.untranslateKey (keysym);
	if (newKey != 0) {
		keysym = newKey;
	} else {
		keysym = Display.wcsToMbcs ((char) keysym);
	}
	/*
	* Feature in Motif.  Motif does not activate an accelerator
	* when the CapsLock, NumLock and NumLock+CapsLock keys are pressed.
	* In order to activate accelerators when these keys are pressed,
	* it is necessary to look for all of these key sequences.  The fix
	* is to add these modifiers to the accelerator.
	*/
	String key = ctrl + alt + shift + "<Key>" + keysymName (keysym);
	String allKeys = key + ",Lock " + key;
	String numLock = Display.numLock;
	if (numLock != null) {
		allKeys += "," + numLock + " " + key + ",Lock " + numLock + " " + key;
	}
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs (null, allKeys, true);		
	int ptr = OS.XtMalloc (buffer.length);
	if (ptr != 0) OS.memmove (ptr, buffer, buffer.length);

	int [] argList = {OS.XmNaccelerator, ptr};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (ptr != 0) OS.XtFree (ptr);
}
void addAccelerators () {
	addAccelerator ();
	if (menu != null) menu.addAccelerators ();
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
 * be notified when the menu item is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the stateMask field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the menu item is selected by the user
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
		int indicatorType = OS.XmONE_OF_MANY;
		if ((style & SWT.CHECK) != 0) indicatorType = OS.XmN_OF_MANY;
		int [] argList2 = {OS.XmNindicatorType, indicatorType};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
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
 * Returns the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 * The default value is zero, indicating that the menu item does
 * not have an accelerator.
 *
 * @return the accelerator or 0
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
/*public*/ Rectangle getBounds () {
	checkWidget();
	if (!OS.XtIsManaged (handle)) return new Rectangle (0, 0, 0, 0);
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5], argList [7]);
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu item is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
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
	return argList [1] != OS.XmUNSET;
}
void hookEvents () {
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = display.windowProc;
	OS.XtAddCallback (handle, OS.XmNhelpCallback, windowProc, HELP_CALLBACK);
	if ((style & SWT.CASCADE) != 0) {
		OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
		OS.XtAddCallback (handle, OS.XmNcascadingCallback, windowProc, CASCADING_CALLBACK);
	} else {
		OS.XtAddCallback (handle, OS.XmNarmCallback, windowProc, ARM_CALLBACK);
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
		} else {
			OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
		}
	}
}
boolean isAccelActive () {
	Menu menu = parent;
	while (menu != null && menu.cascade != null) {
		menu = menu.cascade.parent;
	}
	if (menu == null) return false;
	Decorations shell = menu.parent;
	return shell.menuBar == menu;
}
/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu item is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	return getEnabled () && parent.isEnabled ();
}
String keysymName (int keysym) {
	switch (keysym) {
		case SWT.BS: return "BackSpace";
		case SWT.TAB: return "Tab";
		/*
		* Bug in Motif. For some reason, the XmNaccelerator
		* resource will not accept XK_Linefeed and prints Xt
		* warnings.  The fix is to use XK_Return instead.
		*/
//		case SWT.LF:
//		case OS.XK_Linefeed: return "Linefeed";
		case SWT.LF:
		case OS.XK_Linefeed:
		case SWT.CR: return "Return";
		case SWT.ESC: return "Escape";
		case SWT.DEL: return "Delete";
	}
	if (('0' <= keysym && keysym <= '9') ||
		('a' <= keysym && keysym <= 'z') ||
		('A' <= keysym && keysym <= 'Z')) {
			return new String (new char [] {(char) keysym});
	}
	/*
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
void releaseChildren (boolean destroy) {
	if (menu != null) {
		menu.release (false);
		menu = null;
	}
	super.releaseChildren (destroy);
}
void releaseParent () {
	super.releaseParent ();
	if (menu != null) menu.dispose ();
	menu = null;
}
void releaseWidget () {
	super.releaseWidget ();
	accelerator = 0;
	if (this == parent.defaultItem) {
		parent.defaultItem = null;
	}
	parent = null;
}
void removeAccelerator () {
	if (accelerator == 0) return;
	/*
	* Bug in Solaris.  When accelerators are set more
	* than once in the same menu bar, the time it takes
	* to set the accelerator increases exponentially.
	* The fix is to implement our own accelerator table
	* on Solaris.
	*/
	if (OS.IsSunOS) return;
	int [] argList = {OS.XmNaccelerator, 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void removeAccelerators () {
	removeAccelerator ();
	if (menu != null) menu.removeAccelerators ();
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the arm events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
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
 * @param listener the listener which should no longer be notified
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
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
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
void selectRadio () {
	int index = 0;
	MenuItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}
/**
 * Sets the widget accelerator.  An accelerator is the bit-wise
 * OR of zero or more modifier masks and a key. Examples:
 * <code>SWT.MOD1 | SWT.MOD2 | 'T', SWT.MOD3 | SWT.F2</code>.
 * <code>SWT.CONTROL | SWT.SHIFT | 'T', SWT.ALT | SWT.F2</code>.
 * The default value is zero, indicating that the menu item does
 * not have an accelerator.
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
	if (this.accelerator == accelerator) return;
	this.accelerator = accelerator;
	if (isAccelActive ()) {
		if (accelerator != 0) {
			addAccelerator ();
		} else {
			removeAccelerator ();
		}
	}
}
/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu item is typically
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
	if (getEnabled () == enabled) return;
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (isAccelActive ()) {
		if (enabled) {
			addAccelerator ();
		} else {
			removeAccelerator ();
		}
	}
}
/**
 * Sets the receiver's pull down menu to the argument.
 * Only <code>CASCADE</code> menu items can have a
 * pull down menu. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pull down
 * menu is platform specific.
 * <p>
 * Note: Disposing of a menu item that has a pull down menu
 * will dispose of the menu.  To avoid this behavior, set the
 * menu to null before the menu item is disposed.
 * </p>
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
	
	/*
	* Bug in Motif.  When XmNsubMenuId is set and the
	* previous menu has accelerators, the time it takes
	* to add any new accelerators increases exponentially.
	* The fix is to remove the accelerators from the previous
	* menu before setting the new one.
	*/
	boolean isActive = isAccelActive ();
	if (isActive) removeAccelerators ();

	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;
	
	/* Set the new menu in the OS */
	int menuHandle = 0;
	if (menu != null) {
		menu.cascade = this;
		menuHandle = menu.handle;
	}
	int [] argList = {OS.XmNsubMenuId, menuHandle};
	OS.XtSetValues (handle, argList, argList.length / 2);
	
	if (isActive) addAccelerators ();
}
boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
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
	int [] argList = {OS.XmNset, selected ? OS.XmSET : OS.XmUNSET};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
 * Sets the receiver's text. The string may include
 * the mnemonic character and accelerator text.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 * <p>
 * Accelerator text is indicated by the '\t' character.
 * On platforms that support accelerator text, the text
 * that follows the '\t' character is displayed to the user,
 * typically indicating the key stroke that will cause
 * the item to become selected.  On most platforms, the
 * accelerator text appears right aligned in the menu.
 * Setting the accelerator text does not install the
 * accelerator key sequence. The accelerator key sequence
 * is installed using #setAccelerator.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setAccelerator
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	/*
	* Feature in Motif.  Motif does not optimize the case
	* when the same text is set into a menu item causing
	* it to flash.  The fix is to test for equality and
	* do nothing.
	*/
	if (text.equals (string)) return;
	super.setText (string);
	if ((style & (SWT.ARROW | SWT.SEPARATOR)) != 0) return;
	
	/*
	* Bug in Linux.  In certain contexts setting the label of a
	* CHECK or RADIO menu item to the empty string can cause a
	* GP.  The fix is to set the menu label to a space in such
	* cases since it displays equivalently.
	*/
	if (OS.IsLinux && (style & (SWT.CHECK | SWT.RADIO)) != 0) {
		if (string.length () == 0) string = " ";
	}
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	boolean accel = false;
	int i=0, j=0, mnemonic=0;
	while (i < text.length) {
		if (text [i] == '\t') {accel = true; break;}
		if ((text [j++] = text [i++]) == '&') {
			if (i == text.length) {continue;}
			if (text [i] == '&') {i++; continue;}
			if (mnemonic == 0) mnemonic = text [i];
			j--;
		}
	}
	int xmString2 = 0;
	if (accel && ++i < text.length) {
		char [] accelText = new char [text.length - i];
		System.arraycopy (text, i, accelText, 0, accelText.length);
		/* Use the character encoding for the default locale */
		byte [] buffer2 = Converter.wcsToMbcs (null, accelText, true);
		xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		if (xmString2 == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	} else {
		/*
		* Bug in linux.  In some versions of linux motif setting a menu item's
		* accelerator to NULL will cause a GP.  The workaround is to instead
		* set these accelerators to a functionally equivalent non-null value.
		*/
		xmString2 = OS.XmStringGenerate (new byte[1], null, OS.XmCHARSET_TEXT, null);
		if (xmString2 == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	}
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
		OS.XmNlabelString, xmString1,
		OS.XmNmnemonic, mnemonic,
		OS.XmNacceleratorText, xmString2,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (xmString1 != 0) OS.XmStringFree (xmString1);
	if (xmString2 != 0) OS.XmStringFree (xmString2);
}
boolean translateAccelerator (int accel, boolean doit) {
	if (!getEnabled ()) return false;
	if (menu != null) return menu.translateAccelerator (accel, doit);
	int accelerator = this.accelerator;
	if ((accelerator & SWT.KEYCODE_BIT) == 0) {
		int key = accelerator & SWT.KEY_MASK;
		if ('A' <= key && key <= 'Z') {
			key += 'a' - 'A';
		}
		int mods = accelerator & SWT.MODIFIER_MASK;
		accelerator = mods | key;
	}
	if (accelerator == accel) {
		if (doit) postEvent (SWT.Selection);
		return true;
	}
	return false;
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	if ((style & SWT.CASCADE) != 0) {
		sendEvent (SWT.Arm);
	}
	if (!isEnabled ()) return 0;
	XmAnyCallbackStruct struct = new XmAnyCallbackStruct ();
	OS.memmove (struct, call_data, XmAnyCallbackStruct.sizeof);
	Event event = new Event ();
	if (struct.event != 0) {
		XButtonEvent xEvent = new XButtonEvent ();
		OS.memmove (xEvent, struct.event, XButtonEvent.sizeof);
		event.time = xEvent.time;
		switch (xEvent.type) {
			case OS.ButtonPress:
			case OS.ButtonRelease:
			case OS.KeyPress:
			case OS.KeyRelease:
				setInputState (event, xEvent.state);
				break;
		}
	}
	postEvent (SWT.Selection, event);
	return 0;
}
int XmNarmCallback (int w, int client_data, int call_data) {
	sendEvent (SWT.Arm);
	return 0;
}
int XmNcascadingCallback (int w, int client_data, int call_data) {
	/*
	* Bug in Motif.  When XmNlabelString is set as a result of
	* an XmNcascadingCallback after the callback has returned,
	* Motif measures the new string properly but does not draw
	* it.  The fix is to send rather than post the SWT.Arm event.
	*/
	sendEvent (SWT.Arm);
	return 0;
}
int XmNhelpCallback (int w, int client_data, int call_data) {
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return 0;
	}
	parent.sendHelpEvent (call_data);
	return 0;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	if (!isEnabled ()) return 0;
	XmAnyCallbackStruct struct = new XmAnyCallbackStruct ();
	OS.memmove (struct, call_data, XmAnyCallbackStruct.sizeof);
	Event event = new Event ();
	if (struct.event != 0) {
		XButtonEvent xEvent = new XButtonEvent ();
		OS.memmove (xEvent, struct.event, XButtonEvent.sizeof);
		event.time = xEvent.time;
		switch (xEvent.type) {
			case OS.ButtonPress:
			case OS.ButtonRelease:
			case OS.KeyPress:
			case OS.KeyRelease:
				setInputState (event, xEvent.state);
				break;
		}
	}
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	postEvent (SWT.Selection, event);
	return 0;
}

}
