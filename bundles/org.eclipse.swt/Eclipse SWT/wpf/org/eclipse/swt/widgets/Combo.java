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


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.OS;

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
 * <dd>DefaultSelection, Modify, Selection, Verify, OrientationChange</dd>
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
	int textHandle;
	boolean ignoreSelection;
	
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
	/* This code is intentionally commented */
	//if ((style & SWT.H_SCROLL) != 0) this.style |= SWT.H_SCROLL;
	this.style |= SWT.H_SCROLL;
}

Control [] _getChildren () {
	return new Control [0];
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemHandle = OS.gcnew_ComboBoxItem ();
	int strPtr = createDotNetString (string, false);
	OS.ContentControl_Content (itemHandle, strPtr);
	OS.GCHandle_Free (strPtr);
	int itemCollection = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Add (itemCollection, itemHandle);
	OS.GCHandle_Free (itemCollection);
	OS.GCHandle_Free (itemHandle);
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index < 0) error (SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (index > count) error (SWT.ERROR_INVALID_ARGUMENT);
	int itemHandle = OS.gcnew_ComboBoxItem ();
	int strPtr = createDotNetString (string, false);
	OS.ContentControl_Content (itemHandle, strPtr);
	OS.GCHandle_Free (strPtr);
	int itemCollection = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Insert (itemCollection, index, itemHandle);
	OS.GCHandle_Free (itemCollection);
	OS.GCHandle_Free (itemHandle);
}

void addChild (Control control) {
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
	checkWidget ();
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
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

void addWidget () {
	super.addWidget ();
	if ((style & SWT.READ_ONLY) == 0) {
		updateLayout (handle);
		int name = createDotNetString ("PART_EditableTextBox", false);
		int template = OS.Control_Template (handle);
		textHandle = OS.FrameworkTemplate_FindName (template, name, handle);
		OS.GCHandle_Free (name);
		OS.GCHandle_Free (template);
	}
}

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) == 0) {
		OS.TextBox_Select (textHandle, 0, 0);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	ignoreSelection = true;
	int itemCount = getItemCount();
	int text = 0;
	if (textHandle != 0) {
		text = OS.TextBox_Text (textHandle);
	}
	int selectedIndex = OS.Selector_SelectedIndex (handle);
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	width = Math.max (0, width);
	height = Math.max (0, height);
	int availSize = OS.gcnew_Size ((double) width, (double) height);
	if (availSize == 0) error (SWT.ERROR_NO_HANDLES);
	double requestWidth = OS.FrameworkElement_Width (handle);
	double requestHeight = OS.FrameworkElement_Height (handle);
	int widthDP = OS.FrameworkElement_WidthProperty ();
	int heightDP = OS.FrameworkElement_HeightProperty ();
	OS.DependencyObject_ClearValue (handle, widthDP);
	OS.DependencyObject_ClearValue (handle, heightDP);
	OS.UIElement_Measure (handle, availSize);
	int size = OS.UIElement_DesiredSize (handle);
	width = (int) OS.Size_Width (size);
	height = (int) OS.Size_Height (size);
	OS.GCHandle_Free (size);
	for (int i = 0; i < itemCount; i++) {
		OS.Selector_SelectedIndex (handle, i);
		updateLayout (handle);
		OS.UIElement_Measure (handle, availSize);
		size = OS.UIElement_DesiredSize (handle);
		width = Math.max (width, (int) OS.Size_Width (size));
		height = Math.max (height, (int) OS.Size_Height (size));
		OS.GCHandle_Free (size);
	}
	OS.GCHandle_Free (availSize);
	OS.FrameworkElement_Width (handle, requestWidth);
	OS.FrameworkElement_Height (handle, requestHeight);
	OS.Selector_SelectedIndex (handle, selectedIndex);
	if (textHandle != 0) {
		OS.TextBox_Text (textHandle, text);
		OS.GCHandle_Free (text);
	}
	OS.GCHandle_Free (widthDP);
	OS.GCHandle_Free (heightDP);
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	ignoreSelection = false;
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
	if ((style & SWT.READ_ONLY) != 0) return; //TODO
	OS.TextBoxBase_Copy (textHandle);
}

void createHandle () {
	handle = OS.gcnew_ComboBox ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.READ_ONLY) == 0) {
		OS.ComboBox_IsEditable (handle, true);
		OS.ItemsControl_IsTextSearchEnabled (handle, false);
	}
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.TextBoxBase_Cut (textHandle);
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
	checkWidget ();
	ignoreSelection = true;	
	int selected = OS.Selector_SelectedIndex (handle);
	if (selected == index) OS.Selector_SelectedIndex (handle, -1);
	ignoreSelection = false;
//	FIXME: May need to send modify event here. 
//	if ((style & SWT.READ_ONLY) == 0) {
//		sendEvent (SWT.Modify);
//	}
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
	checkWidget ();
	ignoreSelection = true;
	OS.Selector_SelectedIndex (handle, -1);
	ignoreSelection = false;
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
	checkWidget ();
	int count = getItemCount ();
	if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
	int itemCollection = OS.ItemsControl_Items (handle);
	int comboBoxItem = OS.ItemCollection_GetItemAt (itemCollection, index);
	OS.GCHandle_Free (itemCollection);
	int content = OS.ContentControl_Content (comboBoxItem);
	String string = createJavaString(content);
	OS.GCHandle_Free (comboBoxItem);
	OS.GCHandle_Free (content);
	return string;
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
	checkWidget ();
	int itemCollection = OS.ItemsControl_Items (handle);
	int count = OS.ItemCollection_Count (itemCollection);
	OS.GCHandle_Free (itemCollection);
	return count;
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
	checkWidget ();
	//todo
	int result = 0;
//	if (result == OS.CB_ERR) error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	return result;
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
	checkWidget ();
	int count = getItemCount ();
	String [] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
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
	return OS.ComboBox_IsDropDownOpen (handle);
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
	checkWidget ();
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return new Point (0,0);
	int start = OS.TextBox_SelectionStart (textHandle);
	int length = OS.TextBox_SelectionLength (textHandle);
	return new Point (start, start + length);
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
	checkWidget ();
	return OS.Selector_SelectedIndex (handle);
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
	checkWidget ();
	String text;
	if ((style & SWT.READ_ONLY) != 0) {
		int item = OS.ComboBox_SelectionBoxItem (handle);
		text = createJavaString (item);
		OS.GCHandle_Free (item);
	} else {
		int content = OS.TextBox_Text (textHandle);
		text = createJavaString (content);
		OS.GCHandle_Free (content);
	}
	return text;
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
	checkWidget ();
	if (textHandle != 0) {
		return (int) OS.FrameworkElement_ActualHeight(textHandle);
	}
	return 0;
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return LIMIT;
	return OS.TextBox_MaxLength (textHandle);
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
	return 0;
}

void HandleSelectionChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	if (!ignoreSelection) postEvent (SWT.Selection);
}

void HandlePreviewExecutedRoutedEvent (int sender, int e) {
	if (!checkEvent (e)) return;
	int command = OS.ExecutedRoutedEventArgs_Command (e);
	boolean doVerify = false;
	String input = null;
	int paste = OS.ApplicationCommands_Paste ();
	int cut = OS.ApplicationCommands_Cut ();
	int redo = OS.ApplicationCommands_Redo ();
	int undo = OS.ApplicationCommands_Undo ();
	int backspace = OS.EditingCommands_Backspace ();
	int delete = OS.EditingCommands_Delete ();
	int deleteNext = OS.EditingCommands_DeleteNextWord ();
	int deletePrevious = OS.EditingCommands_DeletePreviousWord ();
	if (OS.Object_Equals (command, paste)) {
		doVerify = true;
		int clipboardText = OS.Clipboard_GetText ();
		input = createJavaString(clipboardText);
		OS.GCHandle_Free(clipboardText);
	} else if (OS.Object_Equals (command, cut)){
		doVerify = true;
		int content = OS.TextBox_SelectedText (textHandle);
		input = createJavaString (content);
		OS.GCHandle_Free (content);
	} else if (OS.Object_Equals(command, redo)) {
		//FIXME
		//doVerify = true;
		OS.ExecutedRoutedEventArgs_Handled (e, true);
	} else if (OS.Object_Equals(command, undo)) {
		//FIXME
		//doVerify = true;
		OS.ExecutedRoutedEventArgs_Handled (e, true);
	} else if (OS.Object_Equals (command, backspace)) {
		doVerify = true;
		input = "";
	} else if (OS.Object_Equals (command, delete)) {
		doVerify = true;
		input = "";
	} else if (OS.Object_Equals(command, deleteNext)) {
		//FIXME
		//doVerify = true;
		OS.ExecutedRoutedEventArgs_Handled (e, true);
	} else if (OS.Object_Equals(command, deletePrevious)) {
		//FIXME
		//doVerify = true;
		OS.ExecutedRoutedEventArgs_Handled (e, true);
	}
	OS.GCHandle_Free (paste);
	OS.GCHandle_Free (cut);
	OS.GCHandle_Free (redo);
	OS.GCHandle_Free (undo);
	OS.GCHandle_Free (backspace);
	OS.GCHandle_Free (delete);
	OS.GCHandle_Free (deleteNext);
	OS.GCHandle_Free (deletePrevious);
	OS.GCHandle_Free(command);
	/*
	* FIXME - should do this first, but for now we want to swallow
	* all Redo, Undo, DeleteNextWord and DeletePreviousWord to 
	* prevent those from changing the TextBox w/o Verify events
	*/
	if (!hooks (SWT.Verify)) return;
	if (!doVerify) return;
	String text = verifyText (input);
	if (text != null && !text.equals (input)) {
		int strPtr = createDotNetString (text, false);
		OS.TextBox_SelectedText (textHandle, strPtr);
		OS.GCHandle_Free (strPtr);
		int start = OS.TextBox_SelectionStart (textHandle);
		int length = OS.TextBox_SelectionLength (textHandle);
		OS.TextBox_Select (textHandle, start+length, 0);
		OS.TextBox_SelectionLength (textHandle, 0);
		text = null;
	}
	if (text == null) OS.ExecutedRoutedEventArgs_Handled (e, true);
}

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
	int key = OS.KeyEventArgs_Key (e);
	if (key == OS.Key_Return) postEvent (SWT.DefaultSelection);
}

void HandlePreviewTextInput (int sender, int e) {
	super.HandlePreviewTextInput (sender, e);
	if (!checkEvent (e)) return;
	int textPtr = OS.TextCompositionEventArgs_Text (e);
	String input = createJavaString(textPtr);
	OS.GCHandle_Free (textPtr);
	String text = verifyText (input);
	if (text != null && !text.equals (input)) {
		textPtr = createDotNetString (text, false);
		OS.TextBox_SelectedText (textHandle, textPtr);
		OS.GCHandle_Free (textPtr);
		int start = OS.TextBox_SelectionStart (textHandle);
		int length = OS.TextBox_SelectionLength (textHandle);
		OS.TextBox_Select (textHandle, start+length, 0);
		OS.TextBox_SelectionLength (textHandle, 0);
		text = null;
	}
	if (text == null) OS.TextCompositionEventArgs_Handled (e, true);
}

void HandleTextChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	sendEvent (SWT.Modify);
}

void hookEvents() {
	super.hookEvents ();
	int handler = OS.gcnew_SelectionChangedEventHandler (jniRef, "HandleSelectionChanged");
	OS.Selector_SelectionChanged (handle, handler);
	OS.GCHandle_Free (handler);
	if (textHandle != 0) {
		handler = OS.gcnew_ExecutedRoutedEventHandler (jniRef, "HandlePreviewExecutedRoutedEvent");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.CommandManager_AddPreviewExecutedHandler (textHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_TextChangedEventHandler (jniRef, "HandleTextChanged");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.TextBoxBase_TextChanged (textHandle, handler);
		OS.GCHandle_Free (handler);
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
	return indexOf (string, 0);
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount ();
	if (start < 0 || start >= count) return -1;
	start = Math.max (start, 0);
	int strPtr = createDotNetString (string, false);
	int itemCollection = OS.ItemsControl_Items (handle);
	int index = -1;
	while (start < count && index == -1) {
		int item = OS.ItemCollection_GetItemAt (itemCollection, start);
		int itemContent = OS.ContentControl_Content (item);
		OS.GCHandle_Free (item);
		if (itemContent != 0 && OS.Object_Equals (itemContent, strPtr)) index = start; 
		OS.GCHandle_Free (itemContent);
		start ++;
	}
	OS.GCHandle_Free (strPtr);
	OS.GCHandle_Free (itemCollection);
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.TextBoxBase_Paste (textHandle);
}

void releaseHandle () {
	super.releaseHandle ();
	if (textHandle != 0) OS.GCHandle_Free (textHandle);
	textHandle = 0;
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
	checkWidget ();
	if (index < 0 || index >= getItemCount ()) error (SWT.ERROR_INVALID_RANGE);
	int itemCollection = OS.ItemsControl_Items(handle);
	OS.ItemCollection_RemoveAt(itemCollection, index);
	OS.GCHandle_Free(itemCollection);
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
	checkWidget ();
	int count = getItemCount ();
	if (start < 0 || start >= count) error (SWT.ERROR_INVALID_RANGE);
	if (end < 0 || end >= count) error (SWT.ERROR_INVALID_RANGE);
	int itemCollection = OS.ItemsControl_Items(handle);
	for (int i = end; i >= start; i--) {
		OS.ItemCollection_RemoveAt(itemCollection, i);
	}
	OS.GCHandle_Free(itemCollection);
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
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
	checkWidget ();
	int itemCollection = OS.ItemsControl_Items (handle);
	ignoreSelection = true;
	OS.ItemCollection_Clear (itemCollection);
	ignoreSelection = false;
	OS.GCHandle_Free (itemCollection);
}

void removeChild(Control control) {
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
	checkWidget ();
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
	checkWidget ();
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
	checkWidget ();
	int itemCollection = OS.ItemsControl_Items(handle);
	int count = OS.ItemCollection_Count(itemCollection);
	OS.GCHandle_Free(itemCollection);
	if (0 <= index && index < count) {
//		int selection = OS.Selector_SelectedIndex (handle);
		ignoreSelection = true;
		OS.Selector_SelectedIndex(handle, index);
		ignoreSelection = false;
//		int code = OS.Selector_SelectedIndex(handle);
//		if (code != -1 && code != selection) {
//			sendEvent (SWT.Modify);
//		}
	}
}

void setForegroundBrush (int brush) {
	if (brush != 0) {
		OS.Control_Foreground (handle, brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index < 0 || index >= getItemCount()) error (SWT.ERROR_INVALID_RANGE);
	int itemCollection = OS.ItemsControl_Items(handle);
	int item = OS.ItemCollection_GetItemAt(itemCollection, index);
	OS.GCHandle_Free(itemCollection);
	int strPtr = createDotNetString (string, false);
	OS.ContentControl_Content(item, strPtr);
	OS.GCHandle_Free(strPtr);
	OS.GCHandle_Free(item);
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
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int itemCollection = OS.ItemsControl_Items(handle);
	ignoreSelection = true;
	OS.ItemCollection_Clear(itemCollection);
	for (int i = 0; i < items.length; i++) {
		int itemHandle = OS.gcnew_ComboBoxItem ();
		int strPtr = createDotNetString (items [i], false);
		OS.ContentControl_Content (itemHandle, strPtr);
		OS.GCHandle_Free (strPtr);
		OS.ItemCollection_Add (itemCollection, itemHandle);
		OS.GCHandle_Free (itemHandle);
	}
	ignoreSelection = false;
	OS.GCHandle_Free(itemCollection);
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
	OS.ComboBox_IsDropDownOpen (handle, visible);
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
	int flowDirection = orientation == SWT.RIGHT_TO_LEFT ? OS.FlowDirection_RightToLeft  : OS.FlowDirection_LeftToRight;
	OS.FrameworkElement_FlowDirection (handle, flowDirection);
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	int start = selection.x, length = selection.y - selection.x;
	OS.TextBox_SelectionLength (textHandle, length);
	OS.TextBox_SelectionStart (textHandle, start);
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index != -1) select (index);
		return;
	}
	int strPtr = createDotNetString (string, false);
	OS.TextBox_Text (textHandle, strPtr);
	OS.GCHandle_Free (strPtr);
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
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.TextBox_MaxLength (textHandle, limit);
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
	return;
}

String verifyText (String text) {
	int start = OS.TextBox_SelectionStart (textHandle);
	int length = OS.TextBox_SelectionLength (textHandle);
	Event event = new Event ();
	event.text = text;
	event.start = start;
	/*
	* FIXME: end can be greater than start+length+1
	* when Deleting special characters. 
	* Note that backspace deletes one character at 
	* a time. 
	*/
	event.end = start+length+1;
	sendEvent (SWT.Verify, event);
	if (!event.doit) return null;
	return event.text;
}

}
