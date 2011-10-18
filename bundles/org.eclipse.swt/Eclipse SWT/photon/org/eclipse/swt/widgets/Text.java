/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
import org.eclipse.swt.internal.photon.*;
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
	char echoCharacter;
	boolean ignoreChange;
	String hiddenText, message;
	int tabs, lastModifiedText;
	PtTextCallback_t textVerify;
	
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
	static final char PASSWORD = '*';
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\n";
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
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	
	int [] args = new int [] {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	boolean wrap = (style & SWT.WRAP) != 0;
	if (wrap) {
		if (wHint == SWT.DEFAULT) {
			OS.PtSetResource (handle, OS.Pt_ARG_MULTITEXT_WRAP_FLAGS, OS.Pt_EMT_NEWLINE, ~0);
		} else {
			OS.PtSetResource (handle, OS.Pt_ARG_WIDTH, wHint, 0);
		}
	}
	int resizeFlags = OS.Pt_RESIZE_X_ALWAYS | OS.Pt_RESIZE_Y_ALWAYS;
	OS.PtSetResource (handle, OS.Pt_ARG_RESIZE_FLAGS, resizeFlags, OS.Pt_RESIZE_XY_BITS);
	if ((style & SWT.MULTI) != 0 || !OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	PhDim_t dim = new PhDim_t ();
	OS.PtWidgetPreferredSize (handle, dim);
	int width = dim.w, height = dim.h;
	OS.PtSetResource (handle, OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS);
	if (wrap) {
		if (wHint == SWT.DEFAULT) {
			int wrapFlags = OS.Pt_EMT_WORD | OS.Pt_EMT_CHAR | OS.Pt_EMT_NEWLINE;
			OS.PtSetResource (handle, OS.Pt_ARG_MULTITEXT_WRAP_FLAGS, wrapFlags, ~0);
		}
	}
	OS.PtSetResources (handle, args.length / 3, args);

	ScrollBar scroll;
	int scrollWidth = (scroll = getVerticalBar ()) != null ? scroll.getSize ().x : 0;
	int scrollHeight = (scroll = getHorizontalBar ()) != null ? scroll.getSize ().y : 0;
	width += scrollWidth;
	if (!wrap) height += scrollHeight;

	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		PhRect_t rect = new PhRect_t ();
		PhArea_t area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) width = area.size_w + scrollWidth;
		if (hHint != SWT.DEFAULT) height = area.size_h + scrollHeight;
	}
	return new Point(width, height);
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
	checkWidget();
	int [] position = {0};
	if ((style & SWT.SINGLE) != 0) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		position [0] = args [1];
	}
	OS.PtTextSetSelection (handle, position, position);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.parentingHandle ();
	boolean hasBorder = (style & SWT.BORDER) != 0;
	int textFlags = (style & SWT.READ_ONLY) != 0 ? 0 : OS.Pt_EDITABLE;
	if ((style & SWT.SINGLE) != 0) {
		int clazz = display.PtText;
		int [] args = {	
			OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
			OS.Pt_ARG_FLAGS, OS.Pt_CALLBACKS_ACTIVE, OS.Pt_CALLBACKS_ACTIVE,
			OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int clazz = display.PtMultiText;
	int wrapFlags = (style & SWT.WRAP) != 0 ? OS.Pt_EMT_WORD | OS.Pt_EMT_CHAR : 0;
	int [] args = {
		OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_FLAGS, OS.Pt_CALLBACKS_ACTIVE, OS.Pt_CALLBACKS_ACTIVE,
		OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
		OS.Pt_ARG_MULTITEXT_WRAP_FLAGS, wrapFlags, OS.Pt_EMT_WORD | OS.Pt_EMT_CHAR,
		OS.Pt_ARG_SCROLLBAR_X_DISPLAY, (style & SWT.H_SCROLL) != 0 ? OS.Pt_ALWAYS : OS.Pt_NEVER, 0,
		OS.Pt_ARG_SCROLLBAR_Y_DISPLAY, (style & SWT.V_SCROLL) != 0 ? OS.Pt_ALWAYS : OS.Pt_NEVER, 0,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	createStandardScrollBars ();
}

void createWidget (int index) {
	super.createWidget (index);
//	doubleClick = true;
	setTabStops (tabs = 8);
	hiddenText = message = "";
	if ((style & SWT.PASSWORD) != 0) setEchoChar (PASSWORD);
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
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget();
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	OS.PtTextModifyText (handle, 0, 0, -1, buffer, buffer.length);
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
	checkWidget();
	int [] start = new int [1], end = new int [1];
	int length = OS.PtTextGetSelection (handle, start, end);
	if (length <= 0) return;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	byte[] buffer = new byte[length + 1];
	OS.memmove (buffer, args [1] + start [0], length);
	int ig = OS.PhInputGroup (0);
	OS.PhClipboardCopyString((short)ig, buffer);
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
	checkWidget();
	int [] start = new int [1], end = new int [1];
	int length = OS.PtTextGetSelection (handle, start, end);
	if (length <= 0) return;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	byte[] buffer = new byte[length + 1];
	OS.memmove (buffer, args [1] + start [0], length);
	int ig = OS.PhInputGroup (0);
	OS.PhClipboardCopyString((short)ig, buffer);
	buffer = new byte[0];
	OS.PtTextModifyText (handle, start [0], end [0], start [0], buffer, buffer.length);
}

void deregister () {
	super.deregister ();
	
	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text. 
	*/
	if ((style & SWT.MULTI) == 0) return;
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.remove (child);
}

int defaultBackground () {
	return display.TEXT_BACKGROUND;
}

int defaultForeground () {
	return display.TEXT_FOREGROUND;
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
	checkWidget();
	//NOT DONE - NOT NEEDED
	return 0;
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
	checkWidget();
	//NOT DONE - NOT NEEDED
	return null;
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
	checkWidget();
	int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	checkWidget();
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return 0;
	return OS.strlen (args [1]);
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
	checkWidget();
	//NOT DONE - NOT NEEDED
	return false;
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
	checkWidget();
	return echoCharacter;
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
	checkWidget();
	int [] args = {OS.Pt_ARG_TEXT_FLAGS, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return (args [1] & OS.Pt_EDITABLE) != 0;
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 1;
	int [] args = {OS.Pt_ARG_MULTITEXT_NUM_LINES, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return args [1];
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
	checkWidget();
	return "\n";
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) {
		PhDim_t dim = new PhDim_t ();
		if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
		OS.PtWidgetPreferredSize (handle, dim);
		PhRect_t extent = new PhRect_t ();
		OS.PtWidgetExtent(handle, extent);
		PhRect_t canvas = new PhRect_t ();
		OS.PtWidgetCanvas (handle, canvas);
		int topBorder = canvas.ul_y - extent.ul_y;
		int bottomBorder = extent.lr_y - canvas.lr_y;
		return dim.h - topBorder - bottomBorder;
	}
	int ptr = OS.malloc (20);
	int [] args = {
		OS.Pt_ARG_MULTITEXT_QUERY_LINE, ptr, 1,
		OS.Pt_ARG_MULTITEXT_LINE_SPACING, 0, 0
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int [] line = new int [1];
	OS.memmove (line, args [1] + 8, 4);
	PhRect_t extent = new PhRect_t ();
	OS.memmove (extent, line [0] + 10, 8);
	OS.free(ptr);
	return extent.lr_y - extent.ul_y + 1 + args [4];
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
	return message;
}

String getNameText () {
	if ((style & SWT.SINGLE) != 0) return getText ();
	return getText (0, Math.min(getCharCount () - 1, 10));
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
	checkWidget();
	if (textVerify != null) {
		return new Point (textVerify.start_pos, textVerify.end_pos);
	}
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	return new Point (start [0], end [0]);
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
	checkWidget();
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
	checkWidget();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	Point selection = getSelection ();
	return getText ().substring (selection.x, selection.y);
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
	checkWidget();
	return tabs;
}

int getTabWidth (int tabs) {
	int [] args = new int [] {OS.Pt_ARG_TEXT_FONT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	PhRect_t rect = new PhRect_t ();
	int ptr = OS.malloc (1);
	OS.memmove (ptr, new byte [] {' '}, 1);
	OS.PfExtentText(rect, null, args [1], ptr, 1);
	OS.free (ptr);
	int width = rect.lr_x - rect.ul_x + 1;
	return width * tabs;
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
	int length = text.length ();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	//NOT DONE - use OS in SINGLE text
	return text.substring (start, end + 1);
}

/**
 * Returns the widget's text as a character array.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * a zero-length array if this has never been set.
 * </p>
 *
 * @return a character array that contains the widget's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTextChars(char[])
 *
 * @since 3.7
 */
public char [] getTextChars () {
	checkWidget();
	return new char [0];
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
	if (echoCharacter != '\0') return hiddenText;
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return 0;
	int [] args = {OS.Pt_ARG_MULTITEXT_TOP_LINE, 0, 0};
	OS.PtGetResources(handle, args.length / 3, args);
	return args [1] - 1;
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
	checkWidget();
	//NOT DONE - NOT NEEDED
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_MODIFY_VERIFY, windowProc, OS.Pt_CB_MODIFY_VERIFY); 
	OS.PtAddCallback (handle, OS.Pt_CB_TEXT_CHANGED, windowProc, OS.Pt_CB_TEXT_CHANGED);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	OS.PtTextModifyText (handle, start [0], end [0], start [0], buffer, buffer.length);
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
	checkWidget();
	int ig = OS.PhInputGroup (0);
	int ptr = OS.PhClipboardPasteString((short)ig);
	if (ptr == 0) return;
	int length = OS.strlen (ptr);
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	OS.PtTextModifyText (handle, start [0], end [0], end [0], ptr, length);
	OS.free(ptr);
}

int Ph_EV_BOUNDARY (int widget, int info) {
	/*
	* Bug in Photon.  PtMultiText reports boundary events for
	* the internal (hidden) child widget.  This makes it appear as
	* though the pointer leaves as soon as it enters the widget
	* area.  The fix is to filter out these theser events.
	*/
	if ((style & SWT.MULTI) != 0) {
		if (info == 0) return OS.Pt_END;
		PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
		OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
		if (cbinfo.event == 0) return OS.Pt_END;
		PhEvent_t ev = new PhEvent_t ();
		OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
		switch ((int) ev.subtype) {
			case OS.Ph_EV_PTR_ENTER_FROM_CHILD:
			case OS.Ph_EV_PTR_LEAVE_TO_CHILD:
				return OS.Pt_CONTINUE;
		}
	}
	return super.Ph_EV_BOUNDARY (widget, info);
}

int Pt_CB_GOT_FOCUS (int widget, int info) {
	
	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text and avoid multiple 
	* Pt_CB_LOST_FOCUS callbacks.
	*/
	if ((style & SWT.MULTI) != 0) {
		if (widget != handle) return OS.Pt_CONTINUE;
	}
	return super.Pt_CB_GOT_FOCUS (widget, info);
}

int Pt_CB_MODIFY_VERIFY (int widget, int info) {
	if (lastModifiedText != 0) {
		OS.free (lastModifiedText);
		lastModifiedText = 0;
	}
	if (echoCharacter == '\0' && !hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	PtTextCallback_t textVerify = new PtTextCallback_t ();
	OS.memmove (textVerify, cbinfo.cbdata, PtTextCallback_t.sizeof);
	byte [] buffer = new byte [textVerify.length];
	OS.memmove (buffer, textVerify.text, buffer.length);
	String text = new String (Converter.mbcsToWcs (null, buffer));
	String newText = text;
	if (!ignoreChange) {
		Event event = new Event ();
		event.start = textVerify.start_pos;
		event.end = textVerify.end_pos;
		event.doit = textVerify.doit != 0;
		event.text = text;
		if (cbinfo.event != 0) {
			int data = OS.PhGetData (cbinfo.event);
			if (data != 0) {
				PhKeyEvent_t ke = new PhKeyEvent_t ();
				OS.memmove (ke, data, PhKeyEvent_t.sizeof);
				if ((ke.key_flags & (OS.Pk_KF_Key_Down | OS.Pk_KF_Key_Repeat)) != 0) {
					setKeyState (event, SWT.Verify, ke);
				}
			}
		}
		sendEvent (SWT.Verify, event);
		newText = event.text;
		textVerify.doit = (event.doit && newText != null) ? 1 : 0;
	}
	if (newText != null) {
		if (echoCharacter != '\0' && (textVerify.doit != 0)) {
			String prefix = hiddenText.substring (0, textVerify.start_pos);
			String suffix = hiddenText.substring (textVerify.end_pos, hiddenText.length ());
			hiddenText = prefix + newText + suffix;
			char [] charBuffer = new char [newText.length ()];
			for (int i=0; i<charBuffer.length; i++) {
				charBuffer [i] = echoCharacter;
			}
			newText = new String (charBuffer);
		}
		if (newText != text) {
			byte [] buffer2 = Converter.wcsToMbcs (null, newText, true);
			int length = buffer2.length - 1;
			if (length == textVerify.length) {
				OS.memmove(textVerify.text, buffer2, length);
			} else {
				int ptr = OS.malloc (length);
				OS.memmove (ptr, buffer2, length);
				textVerify.new_insert += length - textVerify.length;
				textVerify.text = ptr;
				textVerify.length = length;
				lastModifiedText = ptr;
			}
		}
	}
	OS.memmove (cbinfo.cbdata, textVerify, PtTextCallback_t.sizeof);
	textVerify = null;
	return OS.Pt_CONTINUE;
}

int Pt_CB_TEXT_CHANGED (int widget, int info) {
	if (lastModifiedText != 0) {
		OS.free (lastModifiedText);
		lastModifiedText = 0;
	}
	if (!ignoreChange) sendEvent (SWT.Modify);
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();

	/*
	* Bug in Photon. Even though the Pt_CB_GOT_FOCUS callback
	* is added to the multi-line text, the widget parameter
	* in the callback is a child of the multi-line text. The fix
	* is to register that child so that the lookup in the widget
	* table will find the muti-line text. 
	*/
	if ((style & SWT.MULTI) == 0) return;
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.put (child, this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (lastModifiedText != 0) OS.free (lastModifiedText);
	lastModifiedText = 0;
	hiddenText = message = null;
	textVerify = null;
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
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget();
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
	checkWidget();
	OS.PtTextSetSelection (handle, new int [] {0}, new int [] {0x7FFFFFFF});
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
	checkWidget();
	if ((style & SWT.MULTI) != 0) return;
	if (echoCharacter == echo) return;
	String newText;
	if (echo == 0) {
		newText = hiddenText;
		hiddenText = "";
	} else {
		newText = hiddenText = getText();
	}
	echoCharacter = echo;
	Point selection = getSelection();
	boolean oldValue = ignoreChange;
	ignoreChange = true;
	setText(newText);
	setSelection(selection.x, selection.y);
	ignoreChange = oldValue;
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
	checkWidget();
	//NOT DONE - NOT NEEDED
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
	checkWidget();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY; 
	int flags = editable ? OS.Pt_EDITABLE : 0;
	OS.PtSetResource (handle, OS.Pt_ARG_TEXT_FLAGS, flags, OS.Pt_EDITABLE);
}

public void setFont (Font font) {
	super.setFont (font);
	setTabStops (tabs);
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
	this.message = message;
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
public void setSelection (int position) {
	checkWidget();
	OS.PtSetResource (handle, OS.Pt_ARG_CURSOR_POSITION, position, 0);

	/*
	* Feature in Photon. On a single-line text, the selection is
	* not cleared when setting the cursor position. The fix is to
	* set the selection start and end values to the specified
	* position.
	*/
	if ((style & SWT.SINGLE) != 0) {
		int [] selection = {position};
		OS.PtTextSetSelection (handle, selection, selection);
	}
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
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
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
	checkWidget();
	OS.PtTextSetSelection (handle, new int [] {start}, new int [] {end});
	
	/*
	* Feature in Photon. On a multi-line text, the caret position
	* is not changed with the selection start and end values are
	* the same. The fix is to detect this case and change the
	* cursor position.
	*/
	if ((style & SWT.MULTI) != 0 && start == end) {
		OS.PtSetResource (handle, OS.Pt_ARG_CURSOR_POSITION, start, 0);
	}
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
	checkWidget();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
	if ((style & SWT.SINGLE) != 0) return;
	int tabsWidth = getTabWidth (tabs);
	int ptr = OS.malloc (4);
	OS.memmove (ptr, new int [] {tabsWidth}, 4);
	OS.PtSetResource (handle, OS.Pt_ARG_MULTITEXT_TABS, ptr, 1);
	OS.free (ptr);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtSetResource (handle, OS.Pt_ARG_TEXT_STRING, ptr, 0);
	OS.free (ptr);
}
/**
 * Sets the contents of the receiver to the characters in the array. If the receiver
 * has style <code>SWT.SINGLE</code> and the argument contains multiple lines of text
 * then the result of this operation is undefined and may vary between platforms.
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
 *
 * @see #getTextChars()
 *
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
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.PtSetResource (handle, OS.Pt_ARG_MAX_LENGTH, limit, 0);
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	OS.PtSetResource (handle, OS.Pt_ARG_MULTITEXT_TOP_LINE, index + 1, 0);
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
	checkWidget();
	//NOT DONE - NOT NEEDED
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	int code = super.traversalCode (key_sym, ke);
	if ((style & SWT.READ_ONLY) != 0) return code;
	if ((style & SWT.MULTI) != 0) {
		code &= ~SWT.TRAVERSE_RETURN;
		if (key_sym == OS.Pk_Tab && ke != null) {
			if ((ke.key_mods & OS.Pk_KM_Ctrl) == 0) {
				code &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return code;
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	boolean translated = super.translateTraversal (key_sym, phEvent);
	if ((style & SWT.SINGLE) != 0 && !translated && key_sym == OS.Pk_Return) {
		postEvent (SWT.DefaultSelection);
		return false;
	}
	return translated;
}

int widgetClass () {
	if ((style & SWT.SINGLE) != 0) return OS.PtText ();
	return OS.PtMultiText ();
}

}
