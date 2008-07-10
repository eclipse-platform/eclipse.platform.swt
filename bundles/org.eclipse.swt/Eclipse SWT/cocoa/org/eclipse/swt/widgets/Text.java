/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
 * <dd>CANCEL, CENTER, LEFT, MULTI, PASSWORD, SEARCH, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified,
 * and only one of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Text extends Scrollable {
	int textLimit = LIMIT, tabs = 8;
	char echoCharacter;
	boolean doubleClick;
	String hiddenText, message;
	
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
	static final char PASSWORD = '\u2022';

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\r";
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
	if ((style & SWT.SEARCH) != 0) {
//		int inAttributesToSet = (style & SWT.CANCEL) != 0 ? OS.kHISearchFieldAttributesCancel : 0;
//		OS.HISearchFieldChangeAttributes (handle, inAttributesToSet, 0);
		/*
		* Ensure that SWT.CANCEL is set.
		* NOTE: CANCEL has the same value as H_SCROLL so it is
		* necessary to first clear these bits to avoid a scroll
		* bar and then reset the bit using the original style
		* supplied by the programmer.
		*/
		if ((style & SWT.CANCEL) != 0) this.style |= SWT.CANCEL;
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int charCount = getCharCount ();
		string = verifyText (string, charCount, charCount, null);
		if (string == null) return;
	}
	NSString str = NSString.stringWith(string);
	if ((style & SWT.SINGLE) != 0) {
//		new NSTextFieldCell(((NSTextField)view).cell()).title().
	} else {
		NSTextView widget = (NSTextView)view;
		NSMutableString mutableString = widget.textStorage().mutableString();
		mutableString.appendString(str);
		NSRange range = new NSRange();
		range.location = mutableString.length();
		widget.scrollRangeToVisible(range);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
}

static int checkStyle (int style) {
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~SWT.PASSWORD;
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
	Point selection = getSelection ();
	setSelection (selection.x);	
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget = (NSTextField)view;
		NSRect oldRect = widget.frame();
		widget.sizeToFit();
		NSRect newRect = widget.frame();
		widget.setFrame (oldRect);
		width = (int)newRect.width;
		height = (int)newRect.height;
	} else {
		NSTextView widget = (NSTextView)view;
		NSRect oldRect = widget.frame();
		widget.sizeToFit();
		NSRect newRect = widget.frame();
		widget.setFrame (oldRect);
		width = (int)newRect.width;
		height = (int)newRect.height;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	if (height <= 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;
	height = trim.height;
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
 */
public void copy () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		
	} else {
		((NSText)view).copy(view);
	}
}

void createHandle () {
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget;
		if ((style & SWT.PASSWORD) != 0) {
			widget = (NSTextField)new SWTSecureTextField().alloc();
		} else if ((style & SWT.SEARCH) != 0) {
			widget = (NSTextField)new SWTSearchField().alloc();
		} else {
			widget = (NSTextField)new SWTTextField().alloc();
		}
		widget.initWithFrame(new NSRect());
		widget.setSelectable(true);
		widget.setEditable((style & SWT.READ_ONLY) == 0);
		if ((style & SWT.BORDER) == 0) widget.setBordered(false);
		int align = OS.NSLeftTextAlignment;
		if ((style & SWT.CENTER) != 0) align = OS.NSCenterTextAlignment;
		if ((style & SWT.RIGHT) != 0) align = OS.NSRightTextAlignment;
		widget.setAlignment(align);
//		widget.setTarget(widget);
//		widget.setAction(OS.sel_sendSelection);
		view = widget;
	} else {
		NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
		scrollWidget.initWithFrame(new NSRect());
		scrollWidget.setHasVerticalScroller((style & SWT.VERTICAL) != 0);
		scrollWidget.setHasHorizontalScroller((style & SWT.HORIZONTAL) != 0);
		scrollWidget.setAutoresizesSubviews(true);
		if ((style & SWT.BORDER) != 0) scrollWidget.setBorderType(OS.NSBezelBorder);
		
		NSTextView widget = (NSTextView)new SWTTextView().alloc();
		widget.initWithFrame(new NSRect());
		widget.setEditable((style & SWT.READ_ONLY) == 0);
		if ((style & SWT.BORDER) == 0) widget.setFocusRingType(OS.NSFocusRingTypeNone);
		
		NSSize size = new NSSize();
		size.width = size.height = Float.MAX_VALUE;
		widget.setMaxSize(size);
		widget.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);

		if ((style & SWT.WRAP) == 0) {
			widget.setHorizontallyResizable(true);
			NSSize csize = new NSSize();
			csize.width = csize.height = Float.MAX_VALUE;
			widget.textContainer().setWidthTracksTextView(false);
			widget.textContainer().setContainerSize(csize);
		}

		int align = OS.NSLeftTextAlignment;
		if ((style & SWT.CENTER) != 0) align = OS.NSCenterTextAlignment;
		if ((style & SWT.RIGHT) != 0) align = OS.NSRightTextAlignment;
		widget.setAlignment(align);

//		widget.setTarget(widget);
//		widget.setAction(OS.sel_sendSelection);
		widget.setRichText(false);
		
		view = widget;
		scrollView = scrollWidget;
	}
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	message = "";
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
	if ((style & SWT.READ_ONLY) != 0) return;
	if ((style & SWT.SINGLE) != 0) {
		
	} else {
		((NSTextView)view).cut(null);
	}
//	boolean cut = true;
//	char [] oldText = null;
//	Point oldSelection = getSelection ();
//	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
//		if (oldSelection.x != oldSelection.y) {
//			oldText = getEditText (oldSelection.x, oldSelection.y - 1);
//			String newText = verifyText ("", oldSelection.x, oldSelection.y, null);
//			if (newText == null) return;
//			if (newText.length () != 0) {
//				copyToClipboard (oldText);
//				if (txnObject == 0) {
//					insertEditText (newText);
//				} else {
//					setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, newText);
//					OS.TXNShowSelection (txnObject, false);
//				}
//				cut = false;
//			}
//		}
//	}
//	if (cut) {
//		if (txnObject == 0) {
//			if (oldText == null) oldText = getEditText (oldSelection.x, oldSelection.y - 1);
//			copyToClipboard (oldText);
//			insertEditText ("");
//		} else {
//			OS.TXNCut (txnObject);
//	
//			/*
//			* Feature in the Macintosh.  When an empty string is set in the TXNObject,
//			* the font attributes are cleared.  The fix is to reset them.
//			*/
//			if (OS.TXNDataSize (txnObject) / 2 == 0) setFontStyle (font);
//		}
//	}
//	Point newSelection = getSelection ();
//	if (!cut || !oldSelection.equals (newSelection)) sendEvent (SWT.Modify);
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
}

boolean dragDetect (int x, int y, boolean filter, boolean [] consume) {
	if (filter) {
		Point selection = getSelection ();
		if (selection.x != selection.y) {
			int position = getPosition (x, y);
			if (selection.x <= position && position < selection.y) {
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
	if ((style & SWT.SINGLE) != 0) return 0;
    return (getTopPixel () + getCaretLocation ().y) / getLineHeight ();
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
	if ((style & SWT.SINGLE) != 0) {
		//TODO - caret location for unicode text
		return new Point (0, 0);
	}
//	NSText
	NSRange range = ((NSTextView)view).selectedRange();
	System.out.println(range.location + " " + range.length);
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
	if ((style & SWT.SINGLE) != 0) {
		//TODO
		return 0;
	} else {
		NSRange range = ((NSTextView)view).selectedRange();
		return range.location;
	}
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
	if ((style & SWT.SINGLE) != 0) {
		return new NSCell(((NSControl)view).cell()).title().length();
	} else {
		//TODO
		return 0;
	}
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
	return (style & SWT.READ_ONLY) == 0;
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
	return ((NSTextView)view).textStorage().paragraphs().count();
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
	checkWidget();
	//TODO
	return 16;
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
 * Returns the widget message. When the widget is created
 * with the style <code>SWT.SEARCH</code>, the message text
 * is displayed as a hint for the user, indicating the
 * purpose of the field.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not
 * supported on platforms that do not have this concept.
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

int getPosition (int x, int y) {
//	checkWidget ();
	//TODO 
	return 0;
}

/*public*/ int getPosition (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return getPosition (point.x, point.y);
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
	if ((style & SWT.SINGLE) != 0) {
//		new NSTextFieldCell(((NSTextField)view).cell()).title().
		return new Point(0, 0);
	} else {
		NSTextView widget = (NSTextView)view;
		NSRange range = widget.selectedRange();
		return new Point(range.location, range.location + range.length);
	}
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
	if ((style & SWT.SINGLE) != 0) {
//		new NSTextFieldCell(((NSTextField)view).cell()).title().
		return -1;
	} else {
		NSTextView widget = (NSTextView)view;
		NSRange range = widget.selectedRange();
		return range.length;
	}
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
	if ((style & SWT.SINGLE) != 0) {
		//TODO
		return "";
	} else {
		NSTextView widget = (NSTextView)view;
		NSRange range = widget.selectedRange();
		NSString str = widget.textStorage().string();
		char[] buffer = new char[range.length];
		str.getCharacters_range_(buffer, range);
		return new String(buffer);
	}
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
	NSString str;
	if ((style & SWT.SINGLE) != 0) {
		str = new NSTextFieldCell(((NSTextField)view).cell()).title();
	} else {
		str = ((NSTextView)view).textStorage().string();
	}
	if (str == null) return "";
	char[] buffer = new char[str.length()];
	str.getCharacters_(buffer);
	return new String(buffer);
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
	NSString str;
	if ((style & SWT.SINGLE) != 0) {
		str = new NSTextFieldCell(((NSTextField)view).cell()).title();
		 
	} else {
		str = null;
//		return getTXNText (OS.kTXNStartOffset, OS.kTXNEndOffset);
	}
	if (str == null) return "";
	char[] buffer = new char[str.length()];
	str.getCharacters_(buffer);
	return new String(buffer, start, end - start);
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
    return textLimit;
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
    return getTopPixel () / getLineHeight ();
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
	if ((style & SWT.SINGLE) != 0) return 0;
	//TODO
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		Point selection = getSelection ();
		string = verifyText (string, selection.x, selection.y, null);
		if (string == null) return;
	}
	if ((style & SWT.SINGLE) != 0) {
//		new NSTextFieldCell(((NSTextField)view).cell()).title().
	} else {
		//
		NSString str = NSString.stringWith(string);
		NSTextView widget = (NSTextView)view;
		NSRange range = widget.selectedRange();
		widget.textStorage().replaceCharactersInRange_withString_(range, str);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
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
	if ((style & SWT.READ_ONLY) != 0) return;
//	boolean paste = true;
//	String oldText = null;
//	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
//		oldText = getClipboardText ();
//		if (oldText != null) {
//			Point selection = getSelection ();
//			String newText = verifyText (oldText, selection.x, selection.y, null);
//			if (newText == null) return;
//			if (!newText.equals (oldText)) {
//				if (txnObject == 0) {
//					insertEditText (newText);
//				} else {
//					setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, newText);
//					OS.TXNShowSelection (txnObject, false);
//				}
//				paste = false;
//			}
//		}
//	}
//	if (paste) {
//		if (txnObject == 0) {
//			if (oldText == null) oldText = getClipboardText ();
//			insertEditText (oldText);
//		} else {
//			if (textLimit != LIMIT) {
//				if (oldText == null) oldText = getClipboardText ();
//				setTXNText (OS.kTXNUseCurrentSelection, OS.kTXNUseCurrentSelection, oldText);
//				OS.TXNShowSelection (txnObject, false);
//			} else {
//				OS.TXNPaste (txnObject);
//			}
//		}
//	}
//	sendEvent (SWT.Modify);
}

void releaseWidget () {
	super.releaseWidget ();
	hiddenText = message = null;
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
	if ((style & SWT.SINGLE) != 0) {
		setSelection (0, getCharCount ());
	} else {
		((NSTextView)view).selectAll(null);
	}
}

//boolean sendKeyEvent (int type, Event event) {
//	//TODO
//	if (!super.sendKeyEvent (type, event)) {
//		return false;
//	}
//	if (type != SWT.KeyDown) return true;
//	if ((style & SWT.READ_ONLY) != 0) return true;
//	if (event.character == 0) return true;
//	if ((event.stateMask & SWT.COMMAND) != 0) return true;
//	String oldText = "";
//	int charCount = getCharCount ();
//	Point selection = getSelection ();
//	int start = selection.x, end = selection.y;
//	switch (event.character) {
//		case SWT.BS:
//			if (start == end) {
//				if (start == 0) return true;
//				start = Math.max (0, start - 1);
//			}
//			break;
//		case SWT.DEL:
//			if (start == end) {
//				if (start == charCount) return true;
//				end = Math.min (end + 1, charCount);
//			}
//			break;
//		case SWT.CR:
//			if ((style & SWT.SINGLE) != 0) return true;
//			oldText = DELIMITER;
//			break;
//		default:
//			if (event.character != '\t' && event.character < 0x20) return true;
//			oldText = new String (new char [] {event.character});
//	}
//	String newText = verifyText (oldText, start, end, event);
//	if (newText == null) return false;
//	if (charCount - (end - start) + newText.length () > textLimit) {
//		return false;
//	}
//	boolean result = newText == oldText;
//	if (newText != oldText || hiddenText != null) {
////		if (txnObject == 0) {
////			String text = new String (getEditText (0, -1));
////			String leftText = text.substring (0, start);
////			String rightText = text.substring (end, text.length ());
////			setEditText (leftText + newText + rightText);
////			start += newText.length ();
////			setSelection (new Point (start, start));
////			result = false;
////		} else {
////			setTXNText (start, end, newText);
////		}
//	}
//	/*
//	* Post the modify event so that the character will be inserted
//	* into the widget when the modify event is delivered.  Normally,
//	* modify events are sent but it is safe to post the event here
//	* because this method is called from the event loop.
//	*/
//	postEvent (SWT.Modify);
//	return result;
//}

void setBackground (float [] color) {
	NSColor nsColor;
	if (color == null) {
		return;	// TODO reset to OS default
	} else {
		nsColor = NSColor.colorWithDeviceRed(color[0], color[1], color[2], 1);
	}
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField)view).setBackgroundColor(nsColor);
	} else {
		((NSTextView)view).setBackgroundColor(nsColor);
	}
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
	checkWidget();
	if ((style & SWT.MULTI) != 0) return;
//	if (txnObject == 0) {
//		if ((style & SWT.PASSWORD) == 0) {
//			Point selection = getSelection ();
//			String text = getText ();
//			echoCharacter = echo;
//			setEditText (text);
//			setSelection (selection);
//		}
//	} else {
//		OS.TXNEchoMode (txnObject, echo, OS.kTextEncodingMacUnicode, echo != '\0');
//	}
	echoCharacter = echo;
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
	if (editable) {
		style &= ~SWT.READ_ONLY;
	} else {
		style |= SWT.READ_ONLY;
	}
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField)view).setEditable(editable);
	} else {
		((NSTextView)view).setEditable(editable);
	}
}

void setFont(NSFont font) {
	if ((style & SWT.MULTI) !=  0) {
		((NSTextView) view).setFont_(font);
		return;
	}
	super.setFont(font);
}

void setForeground (float [] color) {
	NSColor nsColor;
	if (color == null) {
		return;	// TODO reset to OS default
	} else {
		nsColor = NSColor.colorWithDeviceRed(color[0], color[1], color[2], 1);
	}
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField)view).setTextColor(nsColor);
	} else {
		((NSTextView)view).setTextColor_(nsColor);
	}
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
 * Sets the widget message. When the widget is created
 * with the style <code>SWT.SEARCH</code>, the message text
 * is displayed as a hint for the user, indicating the
 * purpose of the field.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not
 * supported on platforms that do not have this concept.
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
//	if ((style & SWT.SEARCH) != 0) {
//		char [] buffer = new char [message.length ()];
//		message.getChars (0, buffer.length, buffer, 0);
//		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
//		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
//		OS.HISearchFieldSetDescriptiveText (handle, ptr);
//		OS.CFRelease (ptr);
//	}
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
	setSelection (start, start);
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
	if ((style & SWT.SINGLE) != 0) {
//		int length = getCharCount ();
//		ControlEditTextSelectionRec selection = new ControlEditTextSelectionRec ();
//		selection.selStart = (short) Math.min (Math.max (Math.min (start, end), 0), length);
//		selection.selEnd = (short) Math.min (Math.max (Math.max (start, end), 0), length);
//		if (hasFocus ()) {
//			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
//		} else {
//			this.selection = selection;
//		}
	} else {
		//TODO - range test
		NSRange range = new NSRange ();
		range.location = start;
		range.length = end - start + 1;
		((NSTextView)view).setSelectedRange (range);
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
	if (this.tabs == tabs) return;
//	if (txnObject == 0) return;
//	this.tabs = tabs;
//	TXNTab tab = new TXNTab ();
//	tab.value = (short) (textExtent (new char[]{' '}, 0).x * tabs);
//	int [] tags = new int [] {OS.kTXNTabSettingsTag};
//	int [] datas = new int [1];
//	OS.memmove (datas, tab, TXNTab.sizeof);
//	OS.TXNSetTXNObjectControls (txnObject, false, tags.length, tags, datas);
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, 0, getCharCount (), null);
		if (string == null) return;
	}
	NSString str = NSString.stringWith(string);
	if ((style & SWT.SINGLE) != 0) {
		new NSCell(((NSTextField)view).cell()).setTitle(str);
	} else {
		((NSTextView)view).setString(str);
	}
	sendEvent (SWT.Modify);
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
	textLimit = limit;
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
	//TODO no working
	NSTextView widget = (NSTextView)view;
	NSRange range = new NSRange();
	NSRect rect = widget.firstRectForCharacterRange(range);
	view.scrollRectToVisible(rect);
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
	if ((style & SWT.SINGLE) != 0)  {
		setSelection (getSelection());
	} else {
		NSTextView widget = (NSTextView)view;
		widget.scrollRangeToVisible(widget.selectedRange());
	}
}

int traversalCode (int key, NSEvent theEvent) {
	int bits = super.traversalCode (key, theEvent);
	if ((style & SWT.READ_ONLY) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == 48 /* Tab */ && theEvent != null) {
			int modifiers = theEvent.modifierFlags ();
			boolean next = (modifiers & OS.NSShiftKeyMask) == 0;
			if (next && (modifiers & OS.NSControlKeyMask) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
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
