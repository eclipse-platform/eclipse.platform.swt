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
 * <dd>DefaultSelection, Modify, Verify</dd>
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
	int drawCount;
	Color disabledColor;
	
	static final boolean IsGB18030;
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
	
	static final RGB MSG_FOREGROUND = new RGB (172, 168, 153);
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\n";
		IsGB18030 = Converter.defaultCodePage ().endsWith ("18030");
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
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
	int position = OS.XmTextGetLastPosition (handle);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetInsertionPosition (handle, position);
	OS.XmTextInsert (handle, position, buffer);
	position = OS.XmTextGetLastPosition (handle);
	OS.XmTextSetInsertionPosition (handle, position);
	display.setWarnings(warnings);
}
static int checkStyle (int style) {
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~SWT.PASSWORD;
		/* 
		* NOTE: ICON_CANCEL has the same value as H_SCROLL and
		* ICON_SEARCH has the same value as V_SCROLL so they are
		* cleared because SWT.SINGLE is set. 
		*/
	}
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
	checkWidget();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextClearSelection (handle, OS.XtLastTimestampProcessed (xDisplay));
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = wHint;
	int height = hHint;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int ptr = OS.XmTextGetString (handle);
		if (ptr == 0) return new Point (0, 0);
		int size = OS.strlen (ptr);
		if (size == 0) {
			if (hHint == SWT.DEFAULT) {
				if ((style & SWT.SINGLE) != 0) {
					height = getLineHeight ();
				} else {
					height = DEFAULT_HEIGHT;
				}
			}
			if (wHint == SWT.DEFAULT) {
				width = DEFAULT_WIDTH;
			}
		} else {
			byte [] buffer = new byte [size + 1];
			OS.memmove (buffer, ptr, size);
			boolean wrap = (style & SWT.MULTI) != 0 && (style & SWT.WRAP) != 0;
			if (wrap && wHint != SWT.DEFAULT) {
				char[] chars = Converter.mbcsToWcs (getCodePage (), buffer);
				String text = new String (chars);
				String wrapped = display.wrapText (text, font, wHint);
				buffer = Converter.wcsToMbcs (getCodePage (), wrapped, true);
			}
			int xmString;
			if ((style & SWT.SINGLE) != 0) {
				xmString = OS.XmStringParseText (
					buffer,
					0,
					OS.XmFONTLIST_DEFAULT_TAG,
					OS.XmCHARSET_TEXT,
					null,
					0,
					0);
			} else {
				xmString = OS.XmStringGenerate (
					buffer,
					OS.XmFONTLIST_DEFAULT_TAG,
					OS.XmCHARSET_TEXT,
					null);
			}
			int fontList = font.handle;
			if (hHint == SWT.DEFAULT) {
				if ((style & SWT.SINGLE) != 0) {
					height = getLineHeight ();
				} else {
					height = OS.XmStringHeight (fontList, xmString);
				}
			}
			if (wHint == SWT.DEFAULT) width = OS.XmStringWidth (fontList, xmString);
			OS.XmStringFree (xmString);
		}
		OS.XtFree (ptr);
		if ((style & SWT.SINGLE) != 0 && message.length () > 0) {
			if (wHint == SWT.DEFAULT) {
				byte [] buffer = Converter.wcsToMbcs (getCodePage (), message, true);
				int xmString = OS.XmStringGenerate (
						buffer,
						OS.XmFONTLIST_DEFAULT_TAG,
						OS.XmCHARSET_TEXT,
						null);
				int fontList = font.handle;
				width = Math.max (width, OS.XmStringWidth (fontList, xmString));
				OS.XmStringFree (xmString);
			}
		}
	}
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	if (horizontalBar != null) {
		int [] argList1 = {OS.XmNheight, 0};
		OS.XtGetValues (horizontalBar.handle, argList1, argList1.length / 2);
		height += argList1 [1] + 4;
	}
	if (verticalBar != null) {
		int [] argList1 = {OS.XmNwidth, 0};
		OS.XtGetValues (verticalBar.handle, argList1, argList1.length / 2);
		width += argList1 [1] + 4;
	}
	if ((style & SWT.MULTI) != 0) height+=4;
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (handle, rect);
	x -= rect.x;
	y -= rect.y;
	width += rect.x * 2;
	height += rect.y * 2;
	int shadow = 0, highlight = 0;
	if ((style & SWT.MULTI) != 0 || (style & SWT.BORDER) != 0) {
		int [] argList = new int [] {
			OS.XmNshadowThickness, 0,
			OS.XmNhighlightThickness, 0,
		};
		OS.XtGetValues (handle, argList, argList.length / 2);
		shadow = argList [1];
		highlight = argList [3];
	}
	x -= shadow + highlight;
	y -= shadow + highlight;
	width += (shadow + highlight) * 2;
	height += (shadow + highlight) * 2;	
	return new Rectangle (x, y, width, height);
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
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextCopy (handle, OS.XtLastTimestampProcessed (xDisplay));
}
void createHandle (int index) {
	int [] argList1 = {
		OS.XmNverifyBell, 0,
		OS.XmNeditMode, (style & SWT.SINGLE) != 0 ? OS.XmSINGLE_LINE_EDIT : OS.XmMULTI_LINE_EDIT,
		OS.XmNscrollHorizontal, (style & SWT.H_SCROLL) != 0 ? 1 : 0,
		OS.XmNscrollVertical, (style & SWT.V_SCROLL) != 0 ? 1 : 0,
		OS.XmNwordWrap, !IsGB18030 && (style & SWT.WRAP) != 0 ? 1: 0,
		OS.XmNeditable, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
		OS.XmNcursorPositionVisible, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
//		OS.XmNmarginWidth, 3,
//		OS.XmNmarginHeight, 1,
		OS.XmNancestorSensitive, 1,
	};
	int parentHandle = parent.handle;
	if ((style & SWT.SINGLE) != 0) {	
		handle = OS.XmCreateTextField (parentHandle, null, argList1, argList1.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		int [] argList2 = new int [] {OS.XmNcursorPositionVisible, 0};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	} else {
		handle = OS.XmCreateScrolledText (parentHandle, new byte [1], argList1, argList1.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.XtParent (handle);
	}
	if ((style & SWT.BORDER) == 0) {
		int [] argList3 = new int [] {
			/*
			* Bug in Motif.  Setting the margin width to zero for
			* a single line text field causes the field to draw
			* garbage when the caret is placed at the start of
			* the widget.  The fix is to not set the margin width.
			*/
//			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNshadowThickness, 0,
		};
		OS.XtSetValues (handle, argList3, argList3.length / 2);
	}
	/*
	* Feature in Motif.  The Text widget is created with a default
	* drop target.  This is inconsistent with other platforms.
	* To be consistent, disable the default drop target.
	*/
	OS.XmDropSiteUnregister (handle);
}
ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}
void createWidget (int index) {
	super.createWidget (index);	
	hiddenText = message = "";
	if ((style & SWT.PASSWORD) != 0) setEchoChar ('*');
	if ((style & SWT.SINGLE) != 0) {
		disabledColor = new Color (display, MSG_FOREGROUND);
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
 */
public void cut () {
	checkWidget();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	OS.XmTextCut (handle, OS.XtLastTimestampProcessed (xDisplay));
}
int defaultBackground () {
	return display.textBackground;
}
Font defaultFont () {
	return display.textFont;
}
int defaultForeground () {
	return display.textForeground;
}
boolean dragDetect (int x, int y, boolean filter, boolean [] consume) {
	if (filter) {
		int [] start = new int [1], end = new int [1];
		OS.XmTextGetSelectionPosition (handle, start, end);
		if (start [0] != end [0]) {
			int pos = OS.XmTextXYToPos(handle, (short) x, (short) y);
			if (start [0] <= pos && pos < end [0]) {
				if (super.dragDetect (x, y, filter, consume)) {
					if (consume != null) consume [0] = true;
					return true;
				}
			}
		}
		return false;
	}
	return super.dragDetect (x, y, filter, consume);
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
	return getLineNumber (OS.XmTextGetInsertionPosition (handle));
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
	int position;
	position = OS.XmTextGetInsertionPosition (handle);
	short [] x = new short [1], y = new short [1];
	OS.XmTextPosToXY (handle, position, x, y);
	return new Point (x [0], y [0] - getFontAscent (font.handle));
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
	return OS.XmTextGetInsertionPosition (handle);
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
	return OS.XmTextGetLastPosition (handle);
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
	int [] argList = {OS.XmNselectionArrayCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 1;
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
	/*
	* Bug in MOTIF.  For some reason, when XmTextGetEditable () is called
	* from inside an XmNvalueChangedCallback or XmNModifyVerifyCallback,
	* it always returns TRUE.  Calls to XmTextGetEditable () outside of
	* these callbacks return the correct value.  The fix is to query the
	* resource directly instead of using the convenience function.
	*/
	int [] argList = {OS.XmNeditable, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
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
	int lastChar = echoCharacter != '\0' ? hiddenText.length () : OS.XmTextGetLastPosition (handle);
	return getLineNumber (lastChar) + 1;
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
	return getFontHeight (font.handle);
}
int getLineNumber (int position) {
	if (position == 0) return 0;
	int count = 0, start = 0, page = 1024;
	char [] buffer = new char [page + 1];
	/*
	* Bug in Linux.  For some reason, XmTextGetSubstringWcs () does
	* not copy wchar_t characters into the buffer.  Instead, it 
	* copies 4 bytes per character.  This does not happen on other
	* platforms such as AIX.  The fix is to call XmTextGetSubstring ()
	* instead on Linux and rely on the fact that Metrolink Motif 1.2
	* does not support multibyte locales.
	*/
	byte [] buffer1 = null;
	if (OS.IsLinux) buffer1 = new byte [page + 1];
	int end = ((position + page - 1) / page) * page;
	while (start < end) {
		int length = page;
		if (start + page > position) length = position - start;
		if (echoCharacter != '\0') {
			hiddenText.getChars (start, start + length, buffer, 0);
		} else {
			if (OS.IsLinux) {
				OS.XmTextGetSubstring (handle, start, length, buffer1.length, buffer1);
				for (int i=0; i<length; i++) buffer [i] = (char) buffer1 [i];
			} else {
				OS.XmTextGetSubstringWcs (handle, start, length, buffer.length, buffer);
			}
		}
		for (int i=0; i<length; i++) {
			if (buffer [i] == '\n') count++;
		}
		start += page;
	}
	return count;
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
int getNavigationType () {
	/*
	* Bug in Motif.  On Solaris only, the implementation
	* of XtGetValues for XmText does not check for a zero
	* pointer in the arg list and GP's.  The fix is to
	* allocate and free memory for the arg list value.
	*/
	if ((style & SWT.SINGLE) != 0) {
		return super.getNavigationType ();
	}
	int ptr = OS.XtMalloc (4);
	if (ptr == 0) return OS.XmNONE;
	int [] argList = {OS.XmNnavigationType, ptr};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int [] buffer = new int [1];
	OS.memmove (buffer, ptr, 4);
	OS.XtFree (ptr);
	return buffer [0];
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
/*public*/ int getPosition (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return OS.XmTextXYToPos(handle, (short) point.x, (short) point.y);
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
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (handle);
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
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	return end [0] - start [0];
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
	if (echoCharacter != '\0') {
		Point selection = getSelection ();
		return getText (selection.x, selection.y);
	}
	int ptr = OS.XmTextGetSelection (handle);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return new String (Converter.mbcsToWcs (getCodePage (), buffer));
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
	/* Tabs are not supported in MOTIF. */
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
	if (echoCharacter != '\0') return hiddenText;
	int ptr = OS.XmTextGetString (handle);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return new String (Converter.mbcsToWcs (getCodePage (), buffer));
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
	checkWidget();
	if (!(start <= end && 0 <= end)) return "";
	boolean hasEcho = echoCharacter != '\0';
	int length = hasEcho ? hiddenText.length () : OS.XmTextGetLastPosition (handle);
	if (length == 0) return "";
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	if (hasEcho) return hiddenText.substring (start, end + 1);
	int numChars = end - start + 1;
	int bufLength = numChars * OS.MB_CUR_MAX () + 1;
	byte [] buffer = new byte [bufLength];
	int code = OS.XmTextGetSubstring (handle, start, numChars, bufLength, buffer);
	switch (code) {
		case OS.XmCOPY_FAILED:
		case OS.XmCOPY_TRUNCATED:
			error (SWT.ERROR_CANNOT_GET_TEXT);
	}
	char [] unicode = Converter.mbcsToWcs (getCodePage (), buffer);
	return new String (unicode, 0, numChars);
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
	return OS.XmTextGetMaxLength (handle);
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
	if (scrolledHandle == 0) return 0;
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return 0;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	return argList2 [1];
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
	return getTopIndex () * getLineHeight ();
}
boolean hasIMSupport() {
	return true;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (handle, OS.XmNmodifyVerifyCallback, windowProc, MODIFY_VERIFY_CALLBACK);
}
int inputContext () {
	/* Answer zero.  The text widget uses the default MOTIF input context.  */
	return 0;
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
	int [] start = new int [1], end = new int [1];
	OS.XmTextGetSelectionPosition (handle, start, end);
	if (start [0] == end [0]) {
		start [0] = end [0] = OS.XmTextGetInsertionPosition (handle);
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextReplace (handle, start [0], end [0], buffer);
	int position = start [0] + Math.max (0, buffer.length - 1);
	OS.XmTextSetInsertionPosition (handle, position);
	display.setWarnings (warnings);
}
void overrideTranslations () {
	if ((style & SWT.SINGLE) != 0) {
		OS.XtOverrideTranslations (handle, display.tabTranslations);
	}
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
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	/*
	* Bug in Motif.  Despite the fact that the documentation
	* claims that XmText functions work for XmTextFields, when
	* a text field is passed to XmTextPaste, Motif segment faults.
	* The fix is to call XmTextFieldPaste instead.
	*/
	if ((style & SWT.SINGLE) != 0) {
		OS.XmTextFieldPaste (handle);
	} else {
		OS.XmTextPaste (handle);
	}
	display.setWarnings (warnings);
}
void releaseWidget () {
	super.releaseWidget ();
	hiddenText = message = null;
	if (disabledColor != null) disabledColor.dispose ();
	disabledColor = null;
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
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
boolean sendIMKeyEvent (int type, XKeyEvent xEvent) {
	return super.sendIMKeyEvent (type, xEvent, handle);
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
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (handle);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);

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
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (handle, 0, position, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	OS.XmTextSetInsertionPosition (handle, 0);
	display.setWarnings (warnings);
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	/*
	* Bug in Motif.  For some reason an Xm warning is
	* output whenever a Text widget's caret is beyond
	* the visible region during a resize.  The fix is
	* to temporarily turn off warnings below.
	*/
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	display.setWarnings(warnings);
	return changed;
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
	int [] argList = {OS.XmNselectionArrayCount, doubleClick ? 4 : 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
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
	setSelection(selection);
	ignoreChange = oldValue;
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
	OS.XmTextSetEditable (handle, editable);
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
	if ((style & SWT.MULTI) != 0) return;
	int [] argList = {OS.XmNcursorPositionVisible, editable && hasFocus () ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
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
	redrawHandle (0, 0, 0, 0, true, handle);
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
public void setRedraw (boolean redraw) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	if (redraw) {
		if (--drawCount == 0) OS.XmTextEnableRedisplay(handle);
	} else {
		if (drawCount++ == 0) OS.XmTextDisableRedisplay(handle);
	}
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
	checkWidget();
	/* Clear the selection and highlight before moving the i-beam. */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int position = OS.XmTextGetLastPosition (handle);
	int nStart = Math.min (Math.max (start, 0), position);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);
	OS.XmTextClearSelection (handle, OS.XtLastTimestampProcessed (xDisplay));

	/* Set the i-beam position. */
	OS.XmTextSetInsertionPosition (handle, nStart);
	display.setWarnings (warnings);
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
	/* Clear the highlight before setting the selection. */
	int position = OS.XmTextGetLastPosition (handle);
//	OS.XmTextSetHighlight (handle, 0, position, OS.XmHIGHLIGHT_NORMAL);

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
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	start = Math.min (Math.max (start, 0), position);
	end = Math.min (Math.max (end, 0), position);
	int nStart = Math.min (start, end), nEnd = Math.max (start, end);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetSelection (handle, nStart, nEnd, OS.XtLastTimestampProcessed (xDisplay));

	/* Force the i-beam to follow the highlight/selection. */
	if (start > end) {
		OS.XmTextSetInsertionPosition (handle, nStart);
	} else {
		OS.XmTextSetInsertionPosition (handle, nEnd);
	}
	display.setWarnings (warnings);
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
	/* Do nothing.  Tabs are not supported in MOTIF. */
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
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.XmTextSetString (handle, buffer);
	OS.XmTextSetInsertionPosition (handle, 0);
	display.setWarnings(warnings);
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
	OS.XmTextSetMaxLength (handle, limit);
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
	if (scrolledHandle == 0) return;
	int [] argList1 = {OS.XmNverticalScrollBar, 0};
	OS.XtGetValues (scrolledHandle, argList1, argList1.length / 2);
	if (argList1 [1] == 0) return;
	int [] argList2 = {OS.XmNvalue, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	OS.XmTextScroll (handle, index - argList2 [1]);
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
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int position = OS.XmTextGetInsertionPosition (handle);
	OS.XmTextShowPosition (handle, position);
	display.setWarnings (warnings);
}
int traversalCode (int key, XKeyEvent xEvent) {
	int bits = super.traversalCode (key, xEvent);
	if ((style & SWT.READ_ONLY) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == OS.XK_Tab && xEvent != null) {
			boolean next = (xEvent.state & OS.ShiftMask) == 0;
			if (next && (xEvent.state & OS.ControlMask) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	if ((style & SWT.SINGLE) != 0 && message.length () > 0) { 
		if (!hasFocus () && OS.XmTextGetLastPosition (handle) == 0) {
			/* 
			* Feature in Motif. XmText fills its background during exposure 
			* without respecting the damage clipping. This erases all previous
			* paints. The fix is always to draw the entire content ignoring
			* the damage.
			*/
			int [] argList = new int [] {
				OS.XmNmarginWidth, 0,
				OS.XmNmarginHeight, 0,
				OS.XmNshadowThickness, 0,
				OS.XmNhighlightThickness, 0,
				OS.XmNwidth, 0,
				OS.XmNheight, 0,
			};
			OS.XtGetValues (handle, argList, argList.length / 2);
			int marginWidth = argList [1] + argList[5] + argList[7];
			int marginHeight = argList [3] + argList[5] + argList[7];
			Rectangle rect = new Rectangle (marginWidth, marginHeight, argList [9] - 2 * marginWidth, argList [11] - 2 * marginHeight);
			GCData data = new GCData ();
			GC gc = GC.motif_new (this, data);
			gc.setForeground (disabledColor);
			gc.setClipping (rect);
			gc.drawString (message, rect.x, rect.y, true);
			gc.dispose ();
		}
	}
	return super.XExposure (w, client_data, call_data, continue_to_dispatch);
}
int xFocusIn (XFocusChangeEvent xEvent) {
	super.xFocusIn (xEvent);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & (SWT.READ_ONLY | SWT.SINGLE)) != 0) {
		int [] argList = {OS.XmNcursorPositionVisible, 1};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	if ((style & SWT.SEARCH) != 0) {
		redrawHandle (0, 0, 0, 0, true, handle);
	}
	return 0;
}
int xFocusOut (XFocusChangeEvent xEvent) {
	super.xFocusOut (xEvent);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & (SWT.READ_ONLY | SWT.SINGLE)) != 0) {
		int [] argList = {OS.XmNcursorPositionVisible, 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	if ((style & SWT.SEARCH) != 0) {
		redrawHandle (0, 0, 0, 0, true, handle);
	}
	return 0;
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	postEvent (SWT.DefaultSelection);
	return 0;
}
int XmNmodifyVerifyCallback (int w, int client_data, int call_data) {
	int result = super.XmNmodifyVerifyCallback (w, client_data, call_data);
	if (result != 0) return result;
	if (echoCharacter == '\0' && !hooks (SWT.Verify) && !filters (SWT.Verify)) return result;
	XmTextVerifyCallbackStruct textVerify = new XmTextVerifyCallbackStruct ();
	OS.memmove (textVerify, call_data, XmTextVerifyCallbackStruct.sizeof);
	XmTextBlockRec textBlock = new XmTextBlockRec ();
	OS.memmove (textBlock, textVerify.text, XmTextBlockRec.sizeof);
	byte [] buffer = new byte [textBlock.length];
	OS.memmove (buffer, textBlock.ptr, textBlock.length);
	String codePage = getCodePage ();
	String text = new String (Converter.mbcsToWcs (codePage, buffer));
	String newText = text;
	if (!ignoreChange) {
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
		newText = event.text;
		textVerify.doit = (byte) ((event.doit && newText != null) ? 1 : 0);
	}
	if (newText != null) {
		if (echoCharacter != '\0' && (textVerify.doit != 0)) {
			String prefix = hiddenText.substring (0, textVerify.startPos);
			String suffix = hiddenText.substring (textVerify.endPos, hiddenText.length ());
			hiddenText = prefix + newText + suffix;
			char [] charBuffer = new char [newText.length ()];
			for (int i=0; i<charBuffer.length; i++) {
				charBuffer [i] = echoCharacter;
			}
			newText = new String (charBuffer);
		}
		if (newText != text) {
			OS.XtFree(textBlock.ptr);
			byte [] buffer2 = Converter.wcsToMbcs (codePage, newText, true);
			int length = buffer2.length;
			int ptr = OS.XtMalloc (length);
			OS.memmove (ptr, buffer2, length);
			textBlock.ptr = ptr;
			textBlock.length = buffer2.length - 1;
			OS.memmove (textVerify.text, textBlock, XmTextBlockRec.sizeof);
		}
	}
	OS.memmove (call_data, textVerify, XmTextVerifyCallbackStruct.sizeof);
	return result;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	if (!ignoreChange) sendEvent (SWT.Modify);
	return 0;
}
}
