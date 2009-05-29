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
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally 
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to 
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 * @see <a href="http://www.eclipse.org/swt/snippets/#combo">Combo snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Combo extends Composite {
	int visibleCount = 5;

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static final int LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}

	boolean ignoreSelect;
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#DROP_DOWN
 * @see SWT#READ_ONLY
 * @see SWT#SIMPLE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmComboBoxAddItem(handle, xmString, -1, false);
	OS.XmStringFree (xmString);
}
/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	
	/*
	* Feature in Motif.  When an index is out of range,
	* the list widget adds the item at the end.  This
	* behavior is not wrong but it is unwanted.  The
	* fix is to check the range before adding the item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index <= argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmComboBoxAddItem(handle, xmString, index + 1, false);
	OS.XmStringFree (xmString);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
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
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the combo's list selection.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
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
 * @see VerifyListener
 * @see #removeVerifyListener
 * 
 * @since 3.1
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}
static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a combo box that has a border using Windows style
	* bits.  All combo boxes draw their own border and
	* do not use the standard Windows border styles.
	* Therefore, no matter what style bits are specified,
	* clear the BORDER bits so that the SWT style will
	* match the Windows widget.
	*
	* The Windows behavior is currently implemented on
	* all platforms.
	*/
	style &= ~SWT.BORDER;
	
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
	if ((style & SWT.SIMPLE) != 0) return style & ~SWT.READ_ONLY;
	return style;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list, 
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
public void clearSelection () {
	checkWidget();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextClearSelection (argList[1], OS.XtLastTimestampProcessed (xDisplay));
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int [] argList = {
		OS.XmNlist, 0, 
		OS.XmNtextField, 0,
		OS.XmNitemCount, 0,
		OS.XmNmarginWidth, 0,
		OS.XmNshadowThickness, 0, 
		OS.XmNhighlightThickness, 0,
		OS.XmNarrowSize, 0, 
		OS.XmNarrowSpacing, 0,
	};
	OS.XtGetValues(handle, argList, argList.length / 2);	
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth;	
	OS.XtQueryGeometry (argList[1], null, result);
	int width = result.width, height = getTextHeight();
	int[] argList2 = {OS.XmNmarginWidth, 0, OS.XmNshadowThickness, 0};
	OS.XtGetValues(argList[3], argList2, argList2.length / 2);
	if ((style & SWT.READ_ONLY) == 0) width += (2 * argList[7]);
	if ((style & SWT.DROP_DOWN) != 0) {
		width += argList[13] + argList[15];
	} else {
		int itemCount = (argList[5] == 0) ? 5 : argList[5];
		height += (getItemHeight () * itemCount);
	}
	width += (2 * argList[9]) 
		+ (2 * argList[11])
		+ (2 * argList2[1])
		+ (2 * argList2[3]);
	if (argList[5] == 0) width = DEFAULT_WIDTH;
	if (hHint != SWT.DEFAULT) height = hHint;
	if (wHint != SWT.DEFAULT) width = wHint; 
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}
/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void copy () {
	checkWidget();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextCopy (argList [1], OS.XtLastTimestampProcessed (xDisplay));
}
void createHandle (int index) {
	/*
	* Feature in Motif.  When items are added or removed
	* from a combo, it may request and be granted, a new
	* preferred size.  This behavior is unwanted.  The fix
	* is to create a parent for the list that will disallow
	* geometry requests.
	*/
	int parentHandle = parent.handle;
	int [] argList1 = {OS.XmNancestorSensitive, 1};
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int comboBoxType = OS.XmDROP_DOWN_COMBO_BOX;
	if ((style & SWT.SIMPLE) != 0) {
		comboBoxType = OS.XmCOMBO_BOX;
	} else if ((style & SWT.READ_ONLY) != 0) {
		comboBoxType = OS.XmDROP_DOWN_LIST;
	}
	int [] argList2 = {
		OS.XmNcomboBoxType, comboBoxType,
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
		OS.XmNresizable, 0,
	};
	handle = OS.XmCreateComboBox (formHandle, null, argList2, argList2.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int [] argList3 = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList3, argList3.length / 2);
	int textHandle = argList3 [1];
	int [] argList4 = {OS.XmNverifyBell, 0};
	OS.XtSetValues (textHandle, argList4, argList4.length / 2);
	/*
	* Feature in Motif.  The Combo widget is created with a default
	* drop target.  This is inconsistent with other platforms.
	* To be consistent, disable the default drop target.
	*/
	OS.XmDropSiteUnregister (textHandle);
}
/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void cut () {
	checkWidget();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextCut (argList [1], OS.XtLastTimestampProcessed (xDisplay));
}
/**
 * Deselects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget();
	if (index == -1) return;
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	if (OS.XmListPosSelected (argList[3], index + 1)) {
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		display.setWarnings (warnings);	
		OS.XmListDeselectAllItems (argList[3]);
	}
}
/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
public void deselectAll () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	display.setWarnings(warnings);	
	OS.XmListDeselectAllItems (argList[3]);
}
/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget();
	int [] argList = {OS.XmNitemCount, 0, OS.XmNitems, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (argList [3] == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int ptr = argList [3] + (index * 4);
	int [] buffer1 = new int [1]; 
	OS.memmove (buffer1, ptr, 4);
	ptr = buffer1 [0];
	int [] table = new int [] {display.tabMapping, display.crMapping};
	int address = OS.XmStringUnparse (
		ptr,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		table,
		table.length,
		OS.XmOUTPUT_ALL);
	if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	OS.XtFree (address);
	return decodeString(new String (Converter.mbcsToWcs (getCodePage (), buffer)));
}
/**
 * Returns the number of items contained in the receiver's list.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget();
	int [] listHandleArgs = {OS.XmNlist, 0};
	OS.XtGetValues (handle, listHandleArgs, listHandleArgs.length / 2);
	int [] argList = {OS.XmNlistSpacing, 0, OS.XmNhighlightThickness, 0, OS.XmNfontList, 0};
	OS.XtGetValues (listHandleArgs[1], argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3], fontList = argList [5];
	/* Result is from empirical analysis on Linux and AIX */
	return getFontHeight (fontList) + spacing + (2 * highlight);
}
/**
 * Returns a (possibly empty) array of <code>String</code>s which are
 * the items in the receiver's list. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget();
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
	String codePage = getCodePage ();
	for (int i = 0; i < itemCount; i++) {
		OS.memmove (buffer1, items, 4);
		int ptr = buffer1 [0];
		int [] table = new int [] {display.tabMapping, display.crMapping};
		int address = OS.XmStringUnparse (
			ptr,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			table,
			table.length,
			OS.XmOUTPUT_ALL);
		if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result[i] = decodeString(new String (Converter.mbcsToWcs (codePage, buffer)));
		items += 4;
	}
	return result;
}
/**
 * Returns <code>true</code> if the receiver's list is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's list's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean getListVisible () {
	checkWidget ();
	int[] argList1 = new int[] {OS.XmNlist, 0, OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int xtParent = OS.XtParent (argList1 [1]);
	while (xtParent != 0 && !OS.XtIsSubclass (xtParent, OS.shellWidgetClass ())) {
		xtParent = OS.XtParent (xtParent);
	}
	if (xtParent != 0) {
		int xDisplay = OS.XtDisplay (xtParent);
		if (xDisplay == 0) return false;
		int xWindow = OS.XtWindow (xtParent);
		if (xWindow == 0) return false;
		XWindowAttributes attributes = new XWindowAttributes ();
		OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
		return attributes.map_state == OS.IsViewable;
	}
	return false;
}
int getMinimumHeight () {
	return getTextHeight ();
}
String getNameText () {
	return getText ();
}
/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}
/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selection
 * in the receiver's text field, and whose y coordinate is the
 * character position representing the end of the selection.
 * An "empty" selection is indicated by the x and y coordinates
 * having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget();
	int [] start = new int [1], end = new int [1];
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextGetSelectionPosition (argList[1], start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (argList[1]);
	}
	return new Point (start [0], end [0]);
}
/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int index = OS.XmListGetKbdItemPos (argList[1]);
	if (OS.XmListPosSelected (argList[1], index)) return index - 1;
	int [] count = new int [1], positions = new int [1];
	if (!OS.XmListGetSelectedPos (argList[1], positions, count)) return -1;
	if (count [0] == 0) return -1;
	int address = positions [0];
	int [] indices = new int [1];
	OS.memmove (indices, address, 4);
	OS.XtFree (address);
	return indices [0] - 1;
}
/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int ptr = OS.XmTextGetString (argList[1]);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return decodeString(new String (Converter.mbcsToWcs (getCodePage (), buffer)));
}
/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextHeight () {
	checkWidget();
	if ((style & SWT.DROP_DOWN) != 0) {
		/*
		* Bug in MOTIF.  For some reason, XtQueryGeometry ()
		* returns the wrong height when the combo is not realized.
		* The fix is to force the combo to be realized by forcing
		* the shell to be realized. 
		*/
		if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();
		XtWidgetGeometry result = new XtWidgetGeometry ();
		result.request_mode = OS.CWHeight;	
		OS.XtQueryGeometry (handle, null, result);
		return result.height;
	} else {
		/* Calculate text field height. */
		int [] argList = {OS.XmNtextField, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);	
		int [] argList2 = {OS.XmNmarginHeight, 0};
		OS.XtGetValues (argList[1], argList2, argList2.length / 2);	
		int height = getFontHeight (font.handle);
		XRectangle rect = new XRectangle ();
		OS.XmWidgetGetDisplayRect (argList[1], rect);
		height += (rect.y * 2) + (2 * argList2[1]);
		
		/* Add in combo box margins. */
		int [] argList3 = {OS.XmNmarginHeight, 0, OS.XmNshadowThickness, 0, OS.XmNhighlightThickness, 0};
		OS.XtGetValues(handle, argList3, argList3.length / 2);	
		height += (2 * argList3[1]) + (2 * argList3[3]) + (2 * argList3[5]);
		
		return height;
	}
}
/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 */
public int getTextLimit () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return OS.XmTextGetMaxLength (argList[1]);
}
/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public int getVisibleItemCount () {
	checkWidget ();
	if ((style & SWT.SIMPLE) != 0) return visibleCount;
	int [] argList = new int [] {OS.XmNvisibleItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	int [] argList = {OS.XmNlist, 0, OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int listHandle = argList [1];
	int textHandle = argList [3];
	OS.XtAddCallback (listHandle, OS.XmNbrowseSelectionCallback, windowProc, BROWSE_SELECTION_CALLBACK);
	OS.XtAddEventHandler (listHandle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
	OS.XtAddEventHandler (listHandle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
	OS.XtAddCallback (textHandle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNmodifyVerifyCallback, windowProc, MODIFY_VERIFY_CALLBACK);
	OS.XtAddEventHandler (textHandle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
	OS.XtAddEventHandler (textHandle, OS.ButtonReleaseMask, false, windowProc, BUTTON_RELEASE);
	OS.XtAddEventHandler (textHandle, OS.EnterWindowMask, false, windowProc, ENTER_WINDOW);
	OS.XtAddEventHandler (textHandle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
	OS.XtAddEventHandler (textHandle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
	OS.XtAddEventHandler (textHandle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
	OS.XtInsertEventHandler (textHandle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
}
/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) return -1;
	
	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int index = OS.XmListItemPos (argList[1], xmString);
	OS.XmStringFree (xmString);
	return index - 1;
}
/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to begin the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	if (!((0 <= start) && (start < itemCount))) return -1;
	byte [] buffer1 = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer1);
	if (xmString == 0) return -1;
	int index = start;
	items += start * 4;
	int [] buffer2 = new int [1];
	while (index < itemCount) {
		OS.memmove (buffer2, items, 4);
		if (OS.XmStringCompare (buffer2 [0], xmString)) break;
		items += 4;  index++;
	}
	OS.XmStringFree (xmString);
	if (index == itemCount) return -1;
	return index;
}
/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void paste () {
	checkWidget();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextFieldPaste (argList [1]);
	display.setWarnings (warnings);
}
/**
 * Removes the item from the receiver's list at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	/*
	* Feature in Motif.  An index out of range is handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (argList [1] == 1) {
		removeAll ();
	} else {
		OS.XmComboBoxDeletePos (handle, index + 1);
	}
}
/**
 * Removes the items from the receiver's list which are
 * between the given zero-relative start and end 
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= start && start <= end && end < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (argList [1] == (end - start + 1)) {
		removeAll ();
	} else {
		for (int i = start; i <= end; i++) {
			OS.XmComboBoxDeletePos (handle, start + 1);	
		}
	}
}
void register () {
	super.register ();
	int [] argList = {OS.XmNlist, 0, OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	display.addWidget (argList[1], this);
	display.addWidget (argList[3], this);
}
void releaseWidget () {
	super.releaseWidget ();
	if (display.focusedCombo == this) display.focusedCombo = null;
	/*
	* Bug in Motif.  Disposing a Combo while its list is visible
	* causes Motif to crash.  The fix is to hide the drop down
	* list before disposing the Combo. 
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		int[] argList = new int[] {OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int xtParent = OS.XtParent (argList [1]);
		while (xtParent != 0 && !OS.XtIsSubclass (xtParent, OS.shellWidgetClass ())) {
			xtParent = OS.XtParent (xtParent);
		}
		if (xtParent != 0) OS.XtPopdown (xtParent);
	}
}
/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int [] argList = {OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int index = OS.XmListItemPos (argList[1], xmString);
	OS.XmStringFree (xmString);	
	if (index == 0) error (SWT.ERROR_INVALID_ARGUMENT);
	if (argList [3] == 1) {
		removeAll ();
	} else {
		OS.XmComboBoxDeletePos (handle, index);
	}
}
/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	display.setWarnings(warnings);	
	OS.XmListDeselectAllItems (argList[3]);
	for (int i = 0; i < argList[5]; i++) {
		OS.XmComboBoxDeletePos(handle, 1);
	}
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
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
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 * 
 * @since 3.1
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}
/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget();
	if (index == -1) {
		int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		display.setWarnings (warnings);	
		OS.XmListDeselectAllItems (argList[3]);
	} else {
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		if (!(0 <= index && index < argList [1])) return;
		int [] argList2 = {OS.XmNselectedPosition, index};
		ignoreSelect = true;
		OS.XtSetValues(handle, argList2, argList2.length / 2);
		ignoreSelect = false;
	}
}
void sendFocusEvent (int type) {
	Display display = this.display;
	Control focusedCombo = display.focusedCombo;
	if (type == SWT.FocusIn && focusedCombo != this) {
		super.sendFocusEvent (type);
		if (!isDisposed ()) display.focusedCombo = this;
	}
}
boolean sendIMKeyEvent (int type, XKeyEvent xEvent) {
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return super.sendIMKeyEvent (type, xEvent, argList [1]);
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	int [] argList1 = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int text = argList1 [1], list = argList1 [3];
	int [] argList2 = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (text, argList2, argList2.length / 2);
	OS.XmChangeColor (text, pixel);
	OS.XtSetValues (text, argList2, argList2.length / 2);
	int [] argList3 = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (list, argList3, argList3.length / 2);
	OS.XmChangeColor (list, pixel);
	OS.XtSetValues (list, argList3, argList3.length / 2);
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	checkWidget();
	int newHeight = (resize && (style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	return super.setBounds (x, y, width, newHeight, move, resize);
}
public void setFont (Font font) {
	checkWidget();
	
	/*
	* Bug in Motif. Setting the font in a combo widget that does
	* not have any items causes a GP on UTF-8 locale.
	* The fix is to add an item, change the font, then
	* remove the added item at the end. 
	*/
	int [] argList1 = {OS.XmNitems, 0, OS.XmNitemCount, 0,};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	boolean fixString = OS.IsDBLocale && argList1 [3] == 0;
	if (fixString) {
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), "string", true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		OS.XmComboBoxAddItem (handle, xmString, -1, false);
		OS.XmStringFree (xmString);
	}
	super.setFont (font);
	if (fixString) OS.XtSetValues (handle, argList1, argList1.length / 2);	

	/*
	* Bug in Motif.  When a font is set in a combo box after the widget
	* is realized, the combo box does not lay out properly.  For example,
	* the drop down arrow may be positioned in the middle of the text
	* field or may be invisible, positioned outside the bounds of the
	* widget.  The fix is to detect these cases and force the combo box
	* to be laid out properly by temporarily growing and then shrinking
	* the widget.
	* 
	* NOTE: This problem also occurs for simple combo boxes.
	*/
	if (OS.XtIsRealized (handle)) {
		int [] argList2 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
		OS.XtGetValues (handle, argList2, argList2.length / 2);
		OS.XtResizeWidget (handle, argList2 [1], argList2 [3] + 1, argList2 [5]);
		OS.XtResizeWidget (handle, argList2 [1], argList2 [3], argList2 [5]);
	}
}
void setForegroundPixel (int pixel) {
	int [] argList1 = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNforeground, pixel};
	OS.XtSetValues (argList1 [1], argList2, argList2.length / 2);
	OS.XtSetValues (argList1 [3], argList2, argList2.length / 2);
	super.setForegroundPixel (pixel);
}
/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int [] argList = {OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [3])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	boolean isSelected = OS.XmListPosSelected (argList[1], index + 1);
	OS.XmListReplaceItemsPosUnselected (argList[1], new int [] {xmString}, 1, index + 1);
	if (isSelected) OS.XmListSelectPos (argList[1], index + 1, false);
	OS.XmStringFree (xmString);
}
/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}

	if (items.length == 0) {
		removeAll();
		return;
	}
	
	int index = 0;
	int [] table = new int [items.length];
	String codePage = getCodePage ();
	while (index < items.length) {
		String string = items [index];
		byte [] buffer = Converter.wcsToMbcs (codePage, encodeString(string), true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) break;
		table [index++] = xmString;
	}
	int ptr = OS.XtMalloc (index * 4);
	OS.memmove (ptr, table, index * 4);
	int [] argList1 = {OS.XmNitems, ptr, OS.XmNitemCount, index};
	OS.XtSetValues (handle, argList1, argList1.length / 2);
	for (int i=0; i<index; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
	
	int [] argList2 = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList2, argList2.length / 2);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList2[1], new byte[1]);
	display.setWarnings(warnings);
	int [] argList3 = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList3, argList3.length / 2);
	int [] argList4 = {OS.XmNselectedItemCount, 0, OS.XmNselectedItems, 0};
	OS.XtSetValues (argList3 [1], argList4, argList4.length / 2);
}
/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setListVisible (boolean visible) {
	checkWidget ();
	if ((style & SWT.DROP_DOWN) != 0) {
		int[] argList1 = new int[] {OS.XmNlist, 0, OS.XmNtextField, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int xtParent = OS.XtParent (argList1 [1]);
		while (xtParent != 0 && !OS.XtIsSubclass (xtParent, OS.shellWidgetClass ())) {
			xtParent = OS.XtParent (xtParent);
		}
		if (xtParent != 0) {
			if (visible) {
				int [] argList2 = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
				OS.XtGetValues (argList1 [3], argList2, argList2.length / 2);
				int x = argList2 [1], y = argList2 [3] + argList2 [7] + argList2 [9];
				short [] root_x = new short [1], root_y = new short [1];
				OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
				OS.XtMoveWidget (xtParent, root_x [0], root_y [0]);
				OS.XtPopup (xtParent, OS.XtGrabNone);
			} else {
				// This code is intentionally commented
				//OS.XtPopdown (xtParent);
			}
		}
	}
}
/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
}
/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param selection a point representing the new selection start and end
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (argList[1]);

	/*
	* Bug in MOTIF.  XmTextSetSelection () fails to set the
	* selection when the receiver is not realized.  The fix
	* is to force the receiver to be realized by forcing the
	* shell to be realized.  If the receiver is realized before
	* the shell, MOTIF fails to draw the text widget and issues
	* lots of X BadDrawable errors.
	*/
	if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();

	/* Set the selection. */
	int xDisplay = OS.XtDisplay (argList[1]);
	if (xDisplay == 0) return;
	int nStart = Math.min (Math.max (Math.min (selection.x, selection.y), 0), position);
	int nEnd = Math.min (Math.max (Math.max (selection.x, selection.y), 0), position);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (argList[1], nStart, nEnd, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	OS.XmTextSetInsertionPosition (argList[1], nEnd);
	display.setWarnings(warnings);
}
/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * This call is ignored when the receiver is read only and 
 * the given string is not in the receiver's list.
 * </p>
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

	/*
	* The read-only and non-read-only cases must be handled
	* separately here because the platform will allow the
	* text of a read-only combo to be set to any value,
	* regardless of whether it appears in the combo's item
	* list or not.
	*/
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index != -1) select (index);
	} else {
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) return;
		int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int index = OS.XmListItemPos (argList[3], xmString);
		if (index > 0) {
			/* The list contains the item. */
			OS.XmComboBoxSelectItem(handle, xmString);
		} else {
			/* The list does not contain the item. */
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			OS.XmTextSetString (argList[1], buffer);
			OS.XmTextSetInsertionPosition (argList[1], 0);
			display.setWarnings(warnings);
		}
		OS.XmStringFree (xmString);
	}
}
/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Combo.LIMIT)</code>.
 * Specifying a limit value larger than <code>Combo.LIMIT</code> sets the
 * receiver's limit to <code>Combo.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextSetMaxLength (argList[1], limit);
}
/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setVisibleItemCount (int count) {
	checkWidget ();
	if (count < 0) return;
	this.visibleCount = count;
	/*
	* But in Motif.  Setting the XmNvisibleItemCount resource
	* for the combo box after it has been realized causes the
	* widget to layout badly, sometimes moving the drop down
	* arrow part of the combo box outside of the bounds.
	* The fix is to set the XmNvisibleItemCount resource on
	* the list instead.
	*/
	int [] argList1 = new int [] {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNvisibleItemCount, count};
	OS.XtSetValues (argList1 [1], argList2, argList2.length / 2);
}
void deregister () {
	super.deregister ();
	int [] argList = {OS.XmNlist, 0, OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	display.removeWidget (argList[1]);
	display.removeWidget (argList[3]);
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	int [] argList = {
		OS.XmNlist, 0, 
		OS.XmNtextField, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	enableHandle (enabled, argList [1]);
	enableHandle (enabled, argList [3]);
}
/**
 * Bug in Motif.
 * Empty strings in the combo will cause GPFs if a) they
 * are the only items in the combo or b) if they are
 * included in an array which is set as the value of the
 * combo's items resource. To protect against these GPFs,
 * make sure that no empty strings are added to the combo.
 * The solution is to add a space to empty strings or 
 * strings which are all spaces. This space is removed
 * when answering the text of items which are all spaces.
 */
String encodeString(String string) {
	for (int i = 0; i < string.length(); i++) {
		if (string.charAt(i) != ' ') {
			return string;
		}
	}
	return string + ' ';
}
/**
 * Bug in Motif.
 * Empty strings in the combo will cause GPFs if a) they
 * are the only items in the combo or b) if they are
 * included in an array which is set as the value of the
 * combo's items resource. To protect against these GPFs,
 * make sure that no empty strings are added to the combo.
 * The solution is to add a space to empty strings or 
 * strings which include only spaces. A space is removed
 * when answering the text of items which are all spaces.
 */
String decodeString(String string) {
	if (string.length() == 0) return string;
	
	for (int i = 0; i < string.length(); i++) {
		if (string.charAt(i) != ' ') {
			return string;
		}
	}
	return string.substring(0, string.length() - 1);
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	postEvent (SWT.DefaultSelection);
	return 0;
}
int XmNbrowseSelectionCallback (int w, int client_data, int call_data) {
	/*
	* Bug in MOTIF.  If items have been added and removed from a
	* combo then users are able to select an empty drop-down item
	* in the combo once and force a resulting callback.  In such
	* cases we want to eat this callback so that listeners are not
	* notified.
	*/
	if (ignoreSelect || getSelectionIndex() == -1) return 0;
	postEvent (SWT.Selection);
	return 0;
}
int XmNmodifyVerifyCallback (int w, int client_data, int call_data) {
	int result = super.XmNmodifyVerifyCallback (w, client_data, call_data);
	if (result != 0) return result;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return result;
	XmTextVerifyCallbackStruct textVerify = new XmTextVerifyCallbackStruct ();
	OS.memmove (textVerify, call_data, XmTextVerifyCallbackStruct.sizeof);
	XmTextBlockRec textBlock = new XmTextBlockRec ();
	OS.memmove (textBlock, textVerify.text, XmTextBlockRec.sizeof);
	byte [] buffer = new byte [textBlock.length];
	OS.memmove (buffer, textBlock.ptr, textBlock.length);
	String codePage = getCodePage ();
	String text = new String (Converter.mbcsToWcs (codePage, buffer));
	Event event = new Event ();
	if (textVerify.event != 0) {
		XKeyEvent xEvent = new XKeyEvent ();
		OS.memmove (xEvent, textVerify.event, XKeyEvent.sizeof);
		event.time = xEvent.time;
		setKeyState (event, xEvent);
	}
	event.start = textVerify.startPos;
	event.end = textVerify.endPos;
	event.doit = textVerify.doit == 1;
	event.text = text;
	sendEvent (SWT.Verify, event);
	String newText = event.text;
	textVerify.doit = (byte) ((event.doit && newText != null) ? 1 : 0);
	if (newText != null && newText != text) {
		OS.XtFree(textBlock.ptr);
		byte [] buffer2 = Converter.wcsToMbcs (codePage, newText, true);
		int length = buffer2.length;
		int ptr = OS.XtMalloc (length);
		OS.memmove (ptr, buffer2, length);
		textBlock.ptr = ptr;
		textBlock.length = buffer2.length - 1;
		OS.memmove (textVerify.text, textBlock, XmTextBlockRec.sizeof);
	}
	OS.memmove (call_data, textVerify, XmTextVerifyCallbackStruct.sizeof);
	return result;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	sendEvent (SWT.Modify);
	return 0;
}
}
