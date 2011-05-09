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


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * Text controls can be either single or multi-line.
 * When a text control is created with a border, the
 * operating system includes a platform specific inset
 * around the contents of the control.  When created
 * without a border, an effort is made to remove the
 * inset such that the preferred size of the control
 * is the same size as the contents.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified,
 * and only one of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination with SEARCH.
 * When the platform supports the hint, the text control shows these icons.  When an icon
 * is selected, a default selection event is sent with the detail field set to one of
 * ICON_CANCEL or ICON_SEARCH.  Normally, application code does not need to check the
 * detail.  In the case of ICON_CANCEL, the text is cleared before the default selection
 * event is sent causing the application to search for an empty string.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Text extends Scrollable {
	boolean doubleClick;
	
	/**
	* The maximum number of characters that can be entered
	* into a text widget.
	* <p>
	* Note that this value is platform dependent, based upon
	* the native widget implementation.
	* </p>
	*/
	public static final int LIMIT;
	
	/**
	* The delimiter used by multi-line text widgets.  When text
	* is queried and from the widget, it will be delimited using
	* this delimiter.
	*/
	public static final String DELIMITER;
	
	/*
	* This code is intentionally commented.
	*/
//	static final char PASSWORD;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\r\n";
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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see SWT#PASSWORD
 * @see SWT#SEARCH
 * @see SWT#ICON_SEARCH
 * @see SWT#ICON_CANCEL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
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
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text,
 * or when ENTER is pressed in a search text. If the receiver has the <code>SWT.SEARCH | SWT.CANCEL</code> style
 * and the user cancels the search, the event object detail field contains the value <code>SWT.CANCEL</code>.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
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
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

/**
 * Appends a string.
 * <p>
 * The new text is appended to the text at
 * the end of the widget.
 * </p>
 *
 * @param string the string to be appended
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBox_SelectionStart (handle, getCharCount ());
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int start = OS.TextBox_SelectionStart (handle);
		int end = start + OS.TextBox_SelectionLength (handle);
		string = verifyText (string, start, end, false);
		if (string == null) return;
	}
	int strPtr = createDotNetString (string, false);
	OS.TextBoxBase_AppendText (handle, strPtr);
	OS.TextBox_SelectionLength (handle, 0);
	OS.GCHandle_Free (strPtr);
}

static int checkStyle (int style) {
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~SWT.PASSWORD;
	}
	style &= ~SWT.SEARCH;
	if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
		style &= ~SWT.MULTI;
	}
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) {
		style |= SWT.MULTI;
		style &= ~SWT.H_SCROLL;
	}
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	int start = OS.TextBox_SelectionStart (handle);
	int len = OS.TextBox_SelectionLength (handle);
	OS.TextBox_SelectionStart (handle, start + len);
	OS.TextBox_SelectionLength (handle, 0); 
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
 */
public void copy () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBoxBase_Copy (handle);
}

void createHandle () {
	if ((style & SWT.PASSWORD) != 0) {
		handle = OS.gcnew_PasswordBox ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	handle = OS.gcnew_TextBox ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.MULTI) != 0) {
		OS.TextBoxBase_AcceptsReturn (handle, true);
		OS.TextBoxBase_AcceptsTab (handle, true);
		if ((style & SWT.WRAP) != 0) OS.TextBox_TextWrapping (handle, OS.TextWrapping_Wrap);
	}
	if ((style & SWT.BORDER) == 0) {
		int thickness = OS.gcnew_Thickness (0, 0, 0, 0);
		OS.Control_BorderThickness (handle, thickness);
		OS.Control_Padding(handle, thickness);
		OS.GCHandle_Free (thickness);
	}
	
	if ((style & SWT.READ_ONLY) != 0) OS.TextBoxBase_IsReadOnly (handle, true);
	if ((style & SWT.CENTER) != 0) OS.Control_HorizontalContentAlignment (handle, OS.HorizontalAlignment_Center);
	if ((style & SWT.RIGHT) != 0) OS.Control_HorizontalContentAlignment (handle, OS.HorizontalAlignment_Right);
	if ((style & SWT.V_SCROLL) != 0) OS.TextBoxBase_VerticalScrollBarVisibility (handle, OS.ScrollBarVisibility_Visible);
	if ((style & SWT.H_SCROLL) != 0) OS.TextBoxBase_HorizontalScrollBarVisibility (handle, OS.ScrollBarVisibility_Visible);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
//	setTabStops (8);
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
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBoxBase_Cut (handle);
}

/**
 * Returns the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0; 
	if ((style & SWT.PASSWORD) != 0) return 0;
	int caretIndex = OS.TextBox_CaretIndex (handle);
	return OS.TextBox_GetLineIndexFromCharacterIndex (handle, caretIndex); 
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
 * <p>
 * The location of the caret is returned.
 * </p>
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getCaretLocation () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return new Point (0, 0);
	int caretIndex = OS.TextBox_CaretIndex (handle);
	int rect = OS.TextBox_GetRectFromCharacterIndex (handle, caretIndex);
	Point result = new Point ((int)OS.Rect_X (rect), (int)OS.Rect_Y (rect));
	OS.GCHandle_Free (rect);
	return result;
}

/**
 * Returns the character position of the caret.
 * <p>
 * Indexing is zero based.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretPosition () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return 0;
	return OS.TextBox_CaretIndex (handle);
}

/**
 * Returns the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget ();
	int text;
	if ((style & SWT.PASSWORD) != 0) {
		text = OS.PasswordBox_Password (handle);
	} else {
		text = OS.TextBox_Text (handle);
	}
	int length = OS.String_Length (text);
	OS.GCHandle_Free (text);
	return length;
}

/**
 * Returns the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @return whether or not double click is enabled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget ();
	return doubleClick;
}

/**
 * Returns the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 * 
 * @return the echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setEchoChar
 */
public char getEchoChar () {
	checkWidget ();
	if ((style & SWT.PASSWORD) == 0) return 0;
	return OS.PasswordBox_PasswordChar (handle);
}

/**
 * Returns the editable state.
 *
 * @return whether or not the receiver is editable
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return true;
	return !OS.TextBoxBase_IsReadOnly (handle);
}

/**
 * Returns the number of lines.
 *
 * @return the number of lines in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 1;
	if ((style & SWT.PASSWORD) != 0) return 1;
	int lines = OS.TextBox_LineCount (handle);
	if (lines == -1) {
		updateLayout (handle);
		lines = OS.TextBox_LineCount (handle);
	}
	return lines;
}

/**
 * Returns the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #DELIMITER
 */
public String getLineDelimiter () {
	checkWidget ();
	return DELIMITER;
}

/**
 * Returns the height of a line.
 *
 * @return the height of a row of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineHeight () {
	checkWidget ();
	int family = OS.Control_FontFamily (handle);
	double lineSpacing = OS.FontFamily_LineSpacing (family);
	OS.GCHandle_Free (family);
	double size = OS.Control_FontSize (handle);
	return (int) (lineSpacing * size);
}

/**
 * Returns the widget message.  The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @return the widget message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public String getMessage () {
	checkWidget ();
	return "";
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
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
 * character position representing the start of the selected
 * text, and whose y coordinate is the character position
 * representing the end of the selection. An "empty" selection
 * is indicated by the x and y coordinates having the same value.
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
	if ((style & SWT.PASSWORD) != 0) return new Point (0, 0);
	int start = OS.TextBox_SelectionStart (handle);
	int length = OS.TextBox_SelectionLength (handle);
	return new Point (start, start+length);
}

/**
 * Returns the number of selected characters.
 *
 * @return the number of selected characters.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	Point selection = getSelection ();
	return selection.y - selection.x;
}

/**
 * Gets the selected text, or an empty string if there is no current selection.
 *
 * @return the selected text
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return "";
	int content = OS.TextBox_SelectedText (handle);
	String string = createJavaString (content);
	OS.GCHandle_Free (content);
	return string;
}

/**
 * Returns the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @return the number of tab characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTabs () {
	checkWidget ();
	//FIXME
	return 8;
}

/**
 * Returns the widget text.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * an empty string if this has never been set.
 * </p>
 *
 * @return the widget text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	int text;
	if ((style & SWT.PASSWORD) != 0) {
		text = OS.PasswordBox_Password (handle);
	} else {
		text = OS.TextBox_Text (handle);
	}
	String string = createJavaString (text);
	OS.GCHandle_Free (text);
	return string;
}

/**
 * Returns a range of text.  Returns an empty string if the
 * start of the range is greater than the end.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N-1 where N is
 * the number of characters in the widget.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 * @return the range of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	String text = getText ();
	int length = text.length();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/	
	return text.substring (start, end + 1);
}

/**
 * Returns the widget's text as a character array.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * a zero length array if this has never been set.
 * </p>
 *
 * @return a character array that contains the widget's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #setTextChars(char[])
 * @since 3.7
 */
public char [] getTextChars () {
	checkWidget();
	return new char [0];
}

/**
 * Returns the maximum number of characters that the receiver is capable of holding. 
 * <p>
 * If this has not been changed by <code>setTextLimit()</code>,
 * it will be the constant <code>Text.LIMIT</code>.
 * </p>
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
	if ((style & SWT.PASSWORD) != 0) return OS.PasswordBox_MaxLength (handle);
	return OS.TextBox_MaxLength (handle);
}

/**
 * Returns the zero-relative index of the line which is currently
 * at the top of the receiver.
 * <p>
 * This index can change when lines are scrolled or new lines are added or removed.
 * </p>
 *
 * @return the index of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
	if ((style & SWT.PASSWORD) != 0) return 0;
	return OS.TextBox_GetFirstVisibleLineIndex (handle);
}

/**
 * Returns the top pixel.
 * <p>
 * The top pixel is the pixel position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * pixels instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top pixel changes when the widget is scrolled.
 * The top pixel does not include the widget trimming.
 * </p>
 *
 * @return the pixel position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget ();
	int name = createDotNetString ("PART_ContentHost", false);
	int template = OS.Control_Template (handle);
	int scroller = OS.FrameworkTemplate_FindName (template, name, handle);
	if (scroller == 0) {
		updateLayout (handle);
		scroller = OS.FrameworkTemplate_FindName (template, name, handle);
	}
	OS.GCHandle_Free (name);
	OS.GCHandle_Free (template);
	if (scroller == 0) return 0;
	double vertOffset = OS.ScrollViewer_VerticalOffset (scroller);
	OS.GCHandle_Free (scroller);
	return (int) vertOffset;
}

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
	if ((style & SWT.SINGLE) != 0) {
		int key = OS.KeyEventArgs_Key (e);
		if (key == OS.Key_Return) postEvent (SWT.DefaultSelection);
	}
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
		input = getSelectionText ();
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
	OS.GCHandle_Free (command);
	/*
	* FIXME - should do this first, but for now we want to swallow
	* all Redo, Undo, DeleteNextWord and DeletePreviousWord to 
	* prevent those from changing the TextBox w/o Verify events
	*/
	if (!hooks (SWT.Verify)) return;
	if (!doVerify) return;
	int start = OS.TextBox_SelectionStart (handle);
	int end = start + OS.TextBox_SelectionLength (handle);
	String text = verifyText (input, start, end, true);
	if (text != null && !text.equals (input)) {
		int strPtr = createDotNetString (text, false);
		OS.TextBox_SelectedText (handle, strPtr);
		OS.GCHandle_Free (strPtr);
		start = OS.TextBox_SelectionStart (handle);
		int length = OS.TextBox_SelectionLength (handle);
		OS.TextBox_Select (handle, start+length, 0);
		OS.TextBox_SelectionLength (handle, 0);
		text = null;
	}
	if (text == null) OS.ExecutedRoutedEventArgs_Handled (e, true);
}

void HandlePreviewTextInput (int sender, int e) {
	super.HandlePreviewTextInput (sender, e);
	if (!checkEvent (e)) return;
	if ((style & SWT.PASSWORD) != 0) return;
	int textPtr = OS.TextCompositionEventArgs_Text (e);
	String input = createJavaString(textPtr);
	OS.GCHandle_Free (textPtr);
	int start = OS.TextBox_SelectionStart (handle);
	int end = start + OS.TextBox_SelectionLength (handle);
	String text = verifyText (input, start, end, true);
	if (text != null && !text.equals (input)) {
		textPtr = createDotNetString (text, false);
		OS.TextBox_SelectedText (handle, textPtr);
		OS.GCHandle_Free (textPtr);
		start = OS.TextBox_SelectionStart (handle);
		int length = OS.TextBox_SelectionLength (handle);
		OS.TextBox_Select (handle, start+length, 0);
		OS.TextBox_SelectionLength (handle, 0);
		text = null;
	}
	if (text == null) OS.TextCompositionEventArgs_Handled (e, true);
}

void HandleTextChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	sendEvent (SWT.Modify);
}

void hookEvents () {
	super.hookEvents();
	if ((style & SWT.PASSWORD) != 0) {
		int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleTextChanged");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.PasswordBox_PasswordChanged (handle, handler);
		OS.GCHandle_Free(handler);
		return;
	}
	
	int handler = OS.gcnew_ExecutedRoutedEventHandler (jniRef, "HandlePreviewExecutedRoutedEvent");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.CommandManager_AddPreviewExecutedHandler (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_TextChangedEventHandler (jniRef, "HandleTextChanged");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.TextBoxBase_TextChanged (handle, handler);
	OS.GCHandle_Free (handler);
}

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is <code>null</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.PASSWORD) != 0) return;
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int start = OS.TextBox_SelectionStart (handle);
		string = verifyText (string, start, start, false);
		if (string == null) return;
	}
	int strPtr = createDotNetString (string, false);
	OS.TextBox_SelectionLength (handle, 0);
	OS.TextBox_SelectedText (handle, strPtr);
	OS.GCHandle_Free (strPtr);
	int end = OS.TextBox_SelectionStart (handle) + OS.TextBox_SelectionLength (handle);
	OS.TextBox_Select (handle, end, 0);
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
 */
public void paste () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) {
		OS.PasswordBox_Paste (handle);
		return;
	}
	OS.TextBoxBase_Paste (handle);
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
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

/**
 * Selects all the text in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBoxBase_SelectAll (handle);
}

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p><p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 * 
 * @param doubleClick the new double click flag
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget ();
	this.doubleClick = doubleClick;
}

/**
 * Sets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer. Setting
 * the echo character to '\0' clears the echo
 * character and redraws the original text.
 * If for any reason the echo character is invalid,
 * or if the platform does not allow modification
 * of the echo character, the default echo character
 * for the platform is used.
 * </p>
 *
 * @param echo the new echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEchoChar (char echo) {
	checkWidget ();
	if ((style & SWT.PASSWORD) == 0) return;
	OS.PasswordBox_PasswordChar (handle, echo);
}

/**
 * Sets the editable state.
 *
 * @param editable the new editable state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEditable (boolean editable) {
	checkWidget ();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBoxBase_IsReadOnly (handle, !editable);
}

void setFont (int font, double size) {
	super.setFont (font, size);
	//FIXME
//	setTabStops (tabs);
}

/**
 * Sets the widget message. The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @param message the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the message is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public void setMessage (String message) {
	checkWidget ();
	if (message == null) error (SWT.ERROR_NULL_ARGUMENT);
	//TODO
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
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
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	//FIXME FrameworkElement.FlowDirection ??
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * regular array indexing rules.
 * </p>
 *
 * @param start new caret position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	OS.TextBox_CaretIndex (handle, start);
}

/**
 * Sets the selection to the range specified
 * by the given start and end indices.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	int first = Math.max (Math.min (start, end), 0);
	int last = Math.min (Math.max (start, end), getCharCount ());
	OS.TextBox_Select (handle, first, last-first);
}

/**
 * Sets the selection to the range specified
 * by the given point, where the x coordinate
 * represents the start index and the y coordinate
 * represents the end index.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param selection the point
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
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

/**
 * Sets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @param tabs the number of tabs
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabs (int tabs) {
	checkWidget ();
	if (tabs < 0) return;
	//FIXME - no equivalent API in WPF TextBox
//	setTabStops (this.tabs = tabs);
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, 0, getCharCount (), false);
		if (string == null) return;
	}
	int ptr = createDotNetString (string, false);
	if ((style & SWT.PASSWORD) != 0) {
		OS.PasswordBox_Password (handle, ptr);
	} else {
		OS.TextBox_Text (handle, ptr);
	}
	OS.GCHandle_Free (ptr);
}

/**
 * Sets the contents of the receiver to the characters in the array. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 *
 * @param text a character array that contains the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #getTextChars()
 * @since 3.7
 */
public void setTextChars (char [] text) {
	checkWidget();
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * Specifying a limit value larger than <code>Text.LIMIT</code> sets the
 * receiver's limit to <code>Text.LIMIT</code>.
 * </p>
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
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	if (limit < 0) limit = LIMIT;
	limit = Math.min (limit, LIMIT);
	if ((style & SWT.PASSWORD) != 0) {
		OS.PasswordBox_MaxLength (handle, limit);
	} else {
		OS.TextBox_MaxLength (handle, limit);
	}
}

/**
 * Sets the zero-relative index of the line which is currently
 * at the top of the receiver. This index can change when lines
 * are scrolled or new lines are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {	
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	if ((style & SWT.PASSWORD) != 0) return;
	int count = OS.TextBox_LineCount (handle);
	if (count == -1) {
		updateLayout (handle);
		count = OS.TextBox_LineCount (handle);
	}
	index = Math.max(0, Math.min (index, count-1));
	int family = OS.Control_FontFamily (handle);
	double lineSpacing = OS.FontFamily_LineSpacing (family);
	OS.GCHandle_Free (family);
	double size = OS.Control_FontSize (handle);
	double lineHeight = lineSpacing * size;
	int name = createDotNetString ("PART_ContentHost", false);
	int template = OS.Control_Template (handle);
	int scroller = OS.FrameworkTemplate_FindName (template, name, handle);
	OS.GCHandle_Free (name);
	OS.GCHandle_Free (template);
	if (scroller == 0) return;
	double vertOffset = index*lineHeight;
	OS.ScrollViewer_ScrollToVerticalOffset (scroller, vertOffset);
	OS.GCHandle_Free (scroller);
	updateLayout(handle);
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	if ((style & SWT.PASSWORD) != 0) return;
	double offset = OS.TextBoxBase_VerticalOffset (handle);
	OS.TextBoxBase_ScrollToVerticalOffset (handle, offset);
}

int traversalCode (int key, int event) {
	int bits = super.traversalCode (key, event);
	if ((style & SWT.READ_ONLY) != 0)  return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == OS.Key_Tab && event != 0) {
			int keyboardDevice = OS.KeyboardEventArgs_KeyboardDevice(event);
			int modifiers = OS.KeyboardDevice_Modifiers(keyboardDevice);
			OS.GCHandle_Free(keyboardDevice);
			boolean next = (modifiers & OS.ModifierKeys_Shift) == 0;
			if (next && (modifiers & OS.ModifierKeys_Control) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}

String verifyText (String string, int start, int end, boolean keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent && string.length () == 1) {
		event.character = string.charAt (0);
		setInputState (event, SWT.KeyDown, 0, 0);
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the verify
	* event.  If this happens, answer null to cancel
	* the operation.
	*/
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

}
