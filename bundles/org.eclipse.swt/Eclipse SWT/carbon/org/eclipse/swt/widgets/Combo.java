package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

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
 * <dd>DefaultSelection, Modify, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */
public /*final*/ class Combo extends Composite {

	// AW
	private int fMenuHandle;
	private int fTextLimit= LIMIT;
	private int fPopupButton;
	private int fTX;
	private int fFrameID;
	private Rectangle fFrameRect;
	
	private static int fgCommandID= 6000;
	private static final int FOCUS_BORDER= 3;
	private static final int BUTTON_WIDTH= 22;
	// AW
	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static int LIMIT;

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
	}

	/* AW
	boolean ignoreSelect;
	*/
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (OS.AppendMenuItemTextWithCFString(fMenuHandle, sHandle, 0, fgCommandID++, null) != OS.kNoErr)
			error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));	
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
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
    /* AW
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
    */
	
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (OS.InsertMenuItemTextWithCFString(fMenuHandle, sHandle, (short)index, 0, fgCommandID++) != OS.kNoErr)
			error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));	
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
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
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the combo's list selection changes.
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
static int checkStyle (int style) {

	// AW only READ_ONLY is implemented
	style &= ~SWT.DROP_DOWN;
	style &= ~SWT.SIMPLE;
	// AW
	
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
    /* AW
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextClearSelection (argList[1], OS.XtLastTimestampProcessed (xDisplay));
    */
	if (fTX != 0)
		OS.TXNSetSelection(fTX, OS.kTXNStartOffset, OS.kTXNStartOffset);	// AW: wrong
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
    /* AW
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
    */
	
	Point e= MacUtil.computeSize(fPopupButton);
	int width= e.x;
	int height= e.y;
	if ((style & SWT.READ_ONLY) == 0) {
		int[] textBounds= new int[4];
		OS.TXNGetRectBounds(fTX, null, null, textBounds);
		height= textBounds[2]-textBounds[0];
	}
	
	width+= 2*FOCUS_BORDER;
	height+= 2*FOCUS_BORDER;
	
	if (hHint != SWT.DEFAULT) height = hHint;
	if (wHint != SWT.DEFAULT) width = wHint;

	return new Point (width, height);
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
	checkWidget ();
	OS.TXNCopy(fTX);
}
void createHandle (int index) {
	state |= HANDLE;

	/*
	* Feature in Motif.  When items are added or removed
	* from a combo, it may request and be granted, a new
	* preferred size.  This behavior is unwanted.  The fix
	* is to create a parent for the list that will disallow
	* geometry requests.
	*/
	int parentHandle = parent.handle;
    /* AW
	int [] argList1 = {OS.XmNancestorSensitive, 1};
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int comboBoxType = OS.XmDROP_DOWN_COMBO_BOX;
	if ((style & SWT.SIMPLE) != 0) {
		comboBoxType = OS.XmCOMBO_BOX;
	} else if ((style & SWT.READ_ONLY) != 0) {
		comboBoxType = OS.XmDROP_DOWN_LIST;
	}
    */

	handle= MacUtil.createDrawingArea(parentHandle, 0, 0, 0);		
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	fPopupButton= MacUtil.newControl(handle, (short)0, (short)-12345, (short)-1, (short)(OS.kControlPopupButtonProc+1));
	if (fPopupButton == 0) error (SWT.ERROR_NO_HANDLES);
	
	int[] menuRef= new int[1];
	OS.CreateNewMenu(20000, 0, menuRef);
	fMenuHandle= menuRef[0];
	if (fMenuHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SetControlPopupMenuHandle(fPopupButton, fMenuHandle);

	if ((style & SWT.READ_ONLY) == 0) {
		int frameOptions= OS.kTXNDontDrawCaretWhenInactiveMask;
		frameOptions |= OS.kTXNSingleLineOnlyMask;
		
		int wHandle= OS.GetControlOwner(handle);
		MacRect bounds= new MacRect();
		OS.GetControlBounds(handle, bounds.getData());
		int frameType= OS.kTXNTextEditStyleFrameType;
		int iFileType= OS.kTXNUnicodeTextFile;
		int iPermanentEncoding= OS.kTXNSystemDefaultEncoding;
		int[] tnxObject= new int[1];
		int[] frameID= new int[1];
				
		int status= OS.TXNNewObject(0, wHandle, bounds.getData(), frameOptions, frameType, iFileType, iPermanentEncoding,
							tnxObject, frameID, 0);
		if (status == 0) {		 
			fTX= tnxObject[0];
			fFrameID= frameID[0];
			OS.TXNActivate(fTX, fFrameID, OS.kScrollBarsSyncWithFocus);
		}
	}
}
/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void cut () {
	checkWidget ();
	OS.TXNCut(fTX);
}
void deregister () {
	super.deregister ();
	if (fPopupButton != 0) WidgetTable.remove (fPopupButton);
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
    /* AW
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);

	if (OS.XmListPosSelected (argList[3], index + 1)) {
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		OS.XmTextSetInsertionPosition (argList[1], 0);
		display.setWarnings (warnings);
		OS.XmListDeselectAllItems (argList[3]);
	}
    */
	System.out.println("Combo.deselect: nyi");
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
    /* AW
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	OS.XmTextSetInsertionPosition (argList[1], 0);
	display.setWarnings(warnings);
	OS.XmListDeselectAllItems (argList[3]);
    */
	System.out.println("Combo.deselectAll: nyi");
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget();
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (!(0 <= index && index < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int[] sHandle= new int[1];
	OS.CopyMenuItemTextAsCFString(fMenuHandle, (short)(index+1), sHandle);
	return MacUtil.getStringAndRelease(sHandle[0]);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	return OS.CountMenuItems(fMenuHandle);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget();
    /* AW
	int [] listHandleArgs = {OS.XmNlist, 0};
	OS.XtGetValues (handle, listHandleArgs, listHandleArgs.length / 2);
	int [] argList = {OS.XmNlistSpacing, 0, OS.XmNhighlightThickness, 0};
	OS.XtGetValues (listHandleArgs[1], argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3];
    */
	/* Result is from empirical analysis on Linux and AIX */
    /* AW
	return getFontHeight () + spacing + (2 * highlight);
    */
    return MacUtil.computeSize(fPopupButton).y;
}
/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver's list. 
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget();
	int itemCount= OS.CountMenuItems(fMenuHandle);
	String [] result = new String [itemCount];
	int[] sHandle= new int[1];
	for (int i= 0; i < itemCount; i++) {
		OS.CopyMenuItemTextAsCFString(fMenuHandle, (short)(i+1), sHandle);
		result[i]= MacUtil.getStringAndRelease(sHandle[0]);
	}
	return result;
}
/**
 * Returns a <code>Point</code> whose x coordinate is the start
 * of the selection in the receiver's text field, and whose y
 * coordinate is the end of the selection. The returned values
 * are zero-relative. An "empty" selection as indicated by
 * the the x and y coordinates having the same value.
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
    /* AW
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextGetSelectionPosition (argList[1], start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (argList[1]);
	}
    */
	System.out.println("Combo.getSelection: nyi");
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
    return OS.GetControlValue(fPopupButton)-1;
}
/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field.
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
    /* AW
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);

	int ptr = OS.XmTextGetString (argList[1]);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return decodeString(new String (Converter.mbcsToWcs (getCodePage (), buffer)));
    */
	
	String s= "";
	if (fTX != 0) {
		int[] dataHandle= new int[1];
		OS.TXNGetData(fTX, OS.kTXNStartOffset, OS.kTXNEndOffset, dataHandle);
		int length= OS.GetHandleSize(dataHandle[0]);
		if (length <= 0)
			return s;
		char[] chars= new char[length/2];
		OS.getHandleData(dataHandle[0], chars);
		OS.DisposeHandle(dataHandle[0]);
		return new String(chars);
	}	
	int index= getSelectionIndex();
	if (index >= 0)
		s= getItem(index);
    return s;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
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
        /* AW
		if (!OS.XtIsRealized (handle)) getShell ().realizeWidget ();
		XtWidgetGeometry result = new XtWidgetGeometry ();
		result.request_mode = OS.CWHeight;
		OS.XtQueryGeometry (handle, null, result);
		return result.height;
        */
        return 26;
	} else {
		/* Calculate text field height. */
        /* AW
		int [] argList = {OS.XmNtextField, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int [] argList2 = {OS.XmNmarginHeight, 0};
		OS.XtGetValues (argList[1], argList2, argList2.length / 2);
		int height = getFontHeight ();
		XRectangle rect = new XRectangle ();
		OS.XmWidgetGetDisplayRect (argList[1], rect);
		height += (rect.y * 2) + (2 * argList2[1]);
        */

		/* Add in combo box margins. */
        /* AW
		int [] argList3 = {OS.XmNmarginHeight, 0, OS.XmNshadowThickness, 0, OS.XmNhighlightThickness, 0};
		OS.XtGetValues(handle, argList3, argList3.length / 2);
		height += (2 * argList3[1]) + (2 * argList3[3]) + (2 * argList3[5]);

		return height;
        */
        return 26;
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
 */
public int getTextLimit () {
	checkWidget();
    return fTextLimit;
}
void hookEvents () {
	super.hookEvents ();
    /* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNselectionCallback, windowProc, SWT.Selection);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtAddCallback (argList[1], OS.XmNactivateCallback, windowProc, SWT.DefaultSelection);
	OS.XtAddCallback (argList[1], OS.XmNvalueChangedCallback, windowProc, SWT.Modify);
    */
	Display display= getDisplay();		
	if (fTX != 0) {
		OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
		OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneHitTestProcTag, display.fUserPaneHitTestProc);
	}
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
	int itemCount= OS.CountMenuItems(fMenuHandle);
	int[] sHandle= new int[1];
	for (int i= 0; i < itemCount; i++) {
		OS.CopyMenuItemTextAsCFString(fMenuHandle, (short)(i+1), sHandle);
		String s= MacUtil.getStringAndRelease(sHandle[0]);
		if (s != null && string.equals(s))
			return i;
	}
	return -1;
}
/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
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
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (!((0 <= start) && (start < itemCount))) return -1;
	int[] sHandle= new int[1];
	for (int i= start; i < itemCount; i++) {
		OS.CopyMenuItemTextAsCFString(fMenuHandle, (short)(i+1), sHandle);
		String s= MacUtil.getStringAndRelease(sHandle[0]);
		if (string.equals(s))
			return i;
	}
	return -1;
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
	checkWidget ();
	OS.TXNPaste(fTX);
}
int processFocusIn () {
	super.processFocusIn ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if (fTX != 0) {
		OS.TXNFocus(fTX, true);
		//Text.fgTextInFocus= this;
		drawFrame();
	}
	return 0;
}
int processFocusOut () {
	super.processFocusOut ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if (fTX != 0) {
		//Text.fgTextInFocus= null;
		OS.TXNFocus(fTX, false);
		drawFrame();
	}
	return 0;
}
int processMouseDown (Object callData) {
	if (callData instanceof MacEvent) {
		MacEvent me= (MacEvent) callData;
		int macEvent[]= me.toOldMacEvent();	
		if (macEvent != null)
			OS.TXNClick(fTX, macEvent);
	}
	return 0;
}
int processPaint (Object callData) {
	syncBounds();
	drawFrame();
	return 0;
}
int processSelection (Object callData) {
	int index= getSelectionIndex();
	if (/* AW ignoreSelect || */ index == -1)
		return 0;

	if ((style & SWT.READ_ONLY) == 0) {
		String string= getItem(index);
		int l= string.length();
		char[] chars= new char[l];
		string.getChars(0, l, chars, 0); 
		OS.TXNSetData(fTX, chars, OS.kTXNStartOffset, OS.kTXNEndOffset);
		OS.TXNSetSelection(fTX, OS.kTXNStartOffset, OS.kTXNEndOffset);
		//sendEvent(SWT.Modify);
	}
	
	return super.processSelection(callData);
}
void register () {
	super.register ();
	if (fPopupButton != 0) WidgetTable.put (fPopupButton, this);
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (!(0 <= index && index < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	/* AW
	OS.XmComboBoxDeletePos (handle, index + 1);
    */
	OS.DeleteMenuItems(fMenuHandle, (short)(index+1), 1);
	OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));	
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (!(0 <= start && start < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int newEnd = Math.min (end, itemCount - 1);
	/* AW
	for (int i = start; i <= newEnd; i++) {
		OS.XmComboBoxDeletePos (handle, start + 1);
	}
	if (end >= argList [1]) error (SWT.ERROR_INVALID_RANGE);
    */
	OS.DeleteMenuItems(fMenuHandle, (short)(start+1), newEnd-start+1);
	OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));	
}
/* AW
void register () {
	super.register ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	WidgetTable.put(argList[1], this);
}
*/
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);

    /* AW
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), encodeString(string), true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);

	int [] argList = {OS.XmNlist, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int index = OS.XmListItemPos (argList[1], xmString);

	OS.XmStringFree (xmString);
	if (index == 0) error (SWT.ERROR_INVALID_ARGUMENT);
	OS.XmComboBoxDeletePos (handle, index);
    */
	int itemCount= OS.CountMenuItems(fMenuHandle);
	int[] sHandle= new int[1];
	for (int i= 0; i < itemCount; i++) {
		OS.CopyMenuItemTextAsCFString(fMenuHandle, (short)(i+1), sHandle);
		String s= MacUtil.getStringAndRelease(sHandle[0]);
		if (s != null && string.equals(s)) {
			OS.DeleteMenuItems(fMenuHandle, (short)(i+1), 1);
			OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));	
			return;
		}
	}
	error (SWT.ERROR_INVALID_ARGUMENT);
}
/**
 * Removes all of the items from the receiver's list.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);

	Display display = getDisplay ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (argList[1], new byte[1]);
	OS.XmTextSetInsertionPosition (argList[1], 0);
	display.setWarnings(warnings);
	OS.XmListDeselectAllItems (argList[3]);

	for (int i = 0; i < argList[5]; i++) {
		OS.XmComboBoxDeletePos(handle, 1);
	}
    */
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (itemCount > 0) {
		OS.DeleteMenuItems(fMenuHandle, (short)1, itemCount);
		OS.SetControl32BitMaximum(fPopupButton, OS.CountMenuItems(fMenuHandle));
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
 * be notified when the receiver's selection changes.
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
    /* AW
	if (index == -1) {
		int [] argList = {OS.XmNtextField, 0, OS.XmNlist, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList[1], new byte[1]);
		OS.XmTextSetInsertionPosition (argList[1], 0);
		display.setWarnings (warnings);
		OS.XmListDeselectAllItems (argList[3]);
	} else {
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		if (!(0 <= index && index < argList [1])) {
			error (SWT.ERROR_INVALID_RANGE);
		}
		int [] argList2 = {OS.XmNselectedPosition, index};
		ignoreSelect = true;
		OS.XtSetValues(handle, argList2, argList2.length / 2);
		ignoreSelect = false;
	}
    */
	int itemCount= OS.CountMenuItems(fMenuHandle);
	if (!(0 <= index && index < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	
	OS.SetControl32BitValue(fPopupButton, index+1);
	
	if ((style & SWT.READ_ONLY) == 0) {
		String string= getItem(index);
		int l= string.length();
		char[] chars= new char[l];
		string.getChars(0, l, chars, 0); 
		OS.TXNSetData(fTX, chars, OS.kTXNStartOffset, OS.kTXNEndOffset);
		OS.TXNSetSelection(fTX, OS.kTXNStartOffset, OS.kTXNEndOffset);
		OS.TXNShowSelection(fTX, false);
		OS.TXNForceUpdate(fTX);
		//sendEvent(SWT.Modify);
	}
}
/**
* Sets the widget bounds.
*/
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	super.setBounds (x, y, width, newHeight);
}
/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
 *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
    /* AW
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
    */
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		if (OS.SetMenuItemTextWithCFString(fMenuHandle, (short)(index+1), sHandle) != OS.kNoErr)
			error (SWT.ERROR_ITEM_NOT_ADDED);
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
}
/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);

	if (items.length == 0) {
		removeAll();
		return;
	}

    /* AW
	int index = 0;
	int [] table = new int [items.length];
	String codePage = getCodePage ();
	while (index < items.length) {
		String string = items [index];
		if (string == null) break;
		byte [] buffer = Converter.wcsToMbcs (codePage, encodeString(string), true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) break;
		table [index++] = xmString;
	}
	int ptr = OS.XtMalloc (index * 4);
	OS.memmove (ptr, table, index * 4);
	int [] argList = {OS.XmNitems, ptr, OS.XmNitemCount, index};
	OS.XtSetValues (handle, argList, argList.length / 2);
	for (int i=0; i<index; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
    */
	
	for (int i= 0; i < items.length; i++) {
		String string= items[i];
		if (string == null)
			break;
		int sHandle= 0;
		try {
			sHandle= OS.CFStringCreateWithCharacters(string);
			if (OS.AppendMenuItemTextWithCFString(fMenuHandle, sHandle, 0, fgCommandID++, null) != OS.kNoErr)
				error (SWT.ERROR_ITEM_NOT_ADDED);
		} finally {
			if (sHandle != 0)
				OS.CFRelease(sHandle);
		}
	}
	OS.SetControl32BitMaximum(fPopupButton, items.length);
}
/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param a point representing the new selection start and end
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
	if (fTX != 0)
		OS.TXNSetSelection(fTX, selection.x, selection.y);
}
/**
* Sets the widget size.
*/
public void setSize (int width, int height) {
	checkWidget();
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight () : height;
	super.setSize (width, newHeight);
}
/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param text the new text
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

	int index= indexOf (string);
	if (index != -1) {
		select(index);
	} else {
		if ((style & SWT.READ_ONLY) == 0) {
			int l= string.length();
			char[] chars= new char[l];
			string.getChars(0, l, chars, 0); 
			OS.TXNSetData(fTX, chars, OS.kTXNStartOffset, OS.kTXNEndOffset);
			sendEvent(SWT.Modify);
		}
	}
}
/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	fTextLimit= limit;
}
/* AW
void deregister () {
	super.deregister ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	WidgetTable.remove (argList[1]);
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
*/

////////////////////////////////////////////////////////
// Mac stuff
////////////////////////////////////////////////////////

	private void syncBounds() {
		
		if (fTX == 0)
			return;
	
		MacRect b= new MacRect();
		OS.GetControlBounds(handle, b.getData());
	
		int[] textBounds= new int[4];
		OS.TXNGetRectBounds(fTX, null, null, textBounds);
		int h= textBounds[2]-textBounds[0];
		int x= b.getX() + FOCUS_BORDER;
		int y= b.getY() + (b.getHeight()-h)/2;
		int w= b.getWidth()-BUTTON_WIDTH-2*FOCUS_BORDER-3;
	
		Rectangle oldRect= fFrameRect;
		fFrameRect= new Rectangle(x, y, w, h);
		if (oldRect == null || !oldRect.equals(fFrameRect)) {
			OS.TXNSetFrameBounds(fTX, y, x, y+h, x+w, fFrameID);
		}
	
		OS.TXNDraw(fTX, 0);
	}
	
	/**
	 * Overridden from Control. Takes care of shadow
	 * x and y are relative to window!
	 */
	void handleResize(int hndl, int x, int y, int width, int height) {
		super.handleResize(hndl, x, y, width, height);
		if (fTX != 0) {
			x= x+width-BUTTON_WIDTH;
			y= y+(height-BUTTON_WIDTH)/2;
			OS.SetControlBounds(fPopupButton, new MacRect(x, y, BUTTON_WIDTH, BUTTON_WIDTH).getData());
		} else
			OS.SetControlBounds(fPopupButton, new MacRect(x, y, width, height).getData());
			
		syncBounds();
	}
	
	int sendKeyEvent(int type, int nextHandler, int eRefHandle) {

		MacEvent mEvent= new MacEvent(eRefHandle);
		int kind= mEvent.getKind();
		if ((kind == OS.kEventRawKeyDown || kind == OS.kEventRawKeyRepeat) && (mEvent.getModifiers() & OS.cmdKey) != 0) {
			int code= mEvent.getKeyCode();
			switch (code) {
			case 0:
				OS.TXNSetSelection(fTX, OS.kTXNStartOffset, OS.kTXNEndOffset);
				break;
			case 7:
				cut();
				break;
			case 8:
				copy();
				break;
			case 9:
				paste();
				break;
			default:
				//System.out.println("key code: " + code);
				break;
			}
			return OS.kNoErr;
		}

		int status= OS.CallNextEventHandler(nextHandler, eRefHandle);
		sendEvent (SWT.Modify);
		return status;
	}

	private void drawFrame() {
		if (fFrameRect != null) {
			MacRect bounds= new MacRect(fFrameRect);
			short[] b= bounds.getData();
			b[0]--;
			b[1]--;
			b[2]++;
			b[3]++;
			OS.DrawThemeEditTextFrame(b, OS.kThemeStateActive);
			Control focus= getDisplay().getFocusControl();
			OS.DrawThemeFocusRect(b, focus == this);
		}
	}
}
