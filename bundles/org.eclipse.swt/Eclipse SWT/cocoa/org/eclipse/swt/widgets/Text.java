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
import org.eclipse.swt.internal.cocoa.*;

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
	int textLimit = LIMIT, tabs = 8;
	char echoCharacter;
	boolean doubleClick, receivingFocus;
	char [] hiddenText;
	String message;
	NSRange selectionRange;
	id targetSearch, targetCancel;
	int /*long*/ actionSearch, actionCancel;
	
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
	if ((style & SWT.SEARCH) != 0) {
		/*
		* Ensure that SWT.ICON_CANCEL and ICON_SEARCH are set.
		* NOTE: ICON_CANCEL has the same value as H_SCROLL and
		* ICON_SEARCH has the same value as V_SCROLL so it is
		* necessary to first clear these bits to avoid a scroll
		* bar and then reset the bit using the original style
		* supplied by the programmer.
		*/
		NSSearchFieldCell cell = new NSSearchFieldCell (((NSSearchField) view).cell ());
		if ((style & SWT.ICON_CANCEL) != 0) {
			this.style |= SWT.ICON_CANCEL;
			NSButtonCell cancelCell = cell.cancelButtonCell();
			targetCancel = cancelCell.target();
			actionCancel = cancelCell.action();
			cancelCell.setTarget (view);
			cancelCell.setAction (OS.sel_sendCancelSelection);
		} else {
			cell.setCancelButtonCell (null);
		}
		if ((style & SWT.ICON_SEARCH) != 0) {
			this.style |= SWT.ICON_SEARCH;
			NSButtonCell searchCell = cell.searchButtonCell();
			targetSearch = searchCell.target();
			actionSearch = searchCell.action();
			searchCell.setTarget (view);
			searchCell.setAction (OS.sel_sendSearchSelection);
		} else {
			cell.setSearchButtonCell (null);
		}
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
public void addSelectionListener(SelectionListener listener) {
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int charCount = getCharCount ();
		string = verifyText (string, charCount, charCount, null);
		if (string == null) return;
	}
	NSString str = NSString.stringWith (string);
	if ((style & SWT.SINGLE) != 0) {
		setSelection (getCharCount ());
		insertEditText (string);
	} else {
		NSTextView widget = (NSTextView) view;
		NSTextStorage storage = widget.textStorage ();
		NSRange range = new NSRange();
		range.location = storage.length();
		storage.replaceCharactersInRange (range, str);
		range.location = storage.length();
		widget.scrollRangeToVisible (range);
		widget.setSelectedRange(range);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
}

boolean becomeFirstResponder (int /*long*/ id, int /*long*/ sel) {
	receivingFocus = true;
	boolean result = super.becomeFirstResponder (id, sel);
	receivingFocus = false;
	return result;
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
	checkWidget ();
	Point selection = getSelection ();
	setSelection (selection.x);	
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if ((style & SWT.SINGLE) != 0) {
		NSCell cell = ((NSTextField) view).cell ();
		NSSize size = cell.cellSize ();
		NSString str = ((NSTextField) view).stringValue();
		if (str.length () > 0) {
			width = (int)Math.ceil (size.width);
		}
		height = (int)Math.ceil (size.height);

		Point border = null;
		if ((style & SWT.BORDER) != 0 && (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT)) {
			/* determine the size of the cell without its border */
			NSRect insets = cell.titleRectForBounds (new NSRect ());
			border = new Point (-(int)Math.ceil (insets.width), -(int)Math.ceil (insets.height));
			width -= border.x;
			height -= border.y;
		}
		if (width <= 0) width = DEFAULT_WIDTH;
		if (height <= 0) height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		if (border != null) {
			/* re-add the border size (if any) now that wHint/hHint is taken */
			width += border.x;
			height += border.y;
		}
	} else {
		NSLayoutManager layoutManager = (NSLayoutManager)new NSLayoutManager ().alloc ().init ();
		NSTextContainer textContainer = (NSTextContainer)new NSTextContainer ().alloc ();
		NSSize size = new NSSize ();
		size.width = size.height = OS.MAX_TEXT_CONTAINER_SIZE;
		if ((style & SWT.WRAP) != 0) {
			if (wHint != SWT.DEFAULT) size.width = wHint;
			if (hHint != SWT.DEFAULT) size.height = hHint;
		}
		textContainer.initWithContainerSize (size);
		layoutManager.addTextContainer (textContainer);

		NSTextStorage textStorage = (NSTextStorage)new NSTextStorage ().alloc ().init ();
		textStorage.setAttributedString (((NSTextView)view).textStorage ());
		layoutManager.setTextStorage (textStorage);
		layoutManager.glyphRangeForTextContainer (textContainer);

		NSRect rect = layoutManager.usedRectForTextContainer (textContainer);
		width = layoutManager.numberOfGlyphs () == 0 ? DEFAULT_WIDTH : (int)Math.ceil (rect.width);
		height = (int)Math.ceil (rect.height);
		textStorage.release ();
		textContainer.release ();
		layoutManager.release ();

		if (width <= 0) width = DEFAULT_WIDTH;
		if (height <= 0) height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		Rectangle trim = computeTrim (0, 0, width, height);
		width = trim.width;
		height = trim.height;
	}
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	Rectangle result = super.computeTrim (x, y, width, height);
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget = (NSTextField) view;
		if ((style & SWT.SEARCH) != 0) {
			NSSearchFieldCell cell = new NSSearchFieldCell (widget.cell ());
			int testWidth = 100;
			NSRect rect = new NSRect ();
			rect.width = testWidth;
			rect = cell.searchTextRectForBounds (rect);
			int leftIndent = (int)rect.x;
			int rightIndent = testWidth - leftIndent - (int)Math.ceil (rect.width);
			result.x -= leftIndent;
			result.width += leftIndent + rightIndent;
		}
		NSRect inset = widget.cell ().titleRectForBounds (new NSRect ());
		result.x -= inset.x;
		result.y -= inset.y;
		result.width -= inset.width;
		result.height -= inset.height;
	}
	return result;
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
		Point selection = getSelection ();
		if (selection.x == selection.y) return;
		copyToClipboard (getEditText (selection.x, selection.y - 1));
	} else {
		NSText text = (NSText) view;
		if (text.selectedRange ().length == 0) return;
		text.copy (null);
	}
}

void createHandle () {
	if ((style & SWT.READ_ONLY) != 0) {
		if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			state |= THEME_BACKGROUND;
		}
	}
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget;
		if ((style & SWT.PASSWORD) != 0) {
			widget = (NSTextField) new SWTSecureTextField ().alloc ();
		} else if ((style & SWT.SEARCH) != 0) {
			widget = (NSTextField) new SWTSearchField ().alloc ();
		} else {
			widget = (NSTextField) new SWTTextField ().alloc ();
		}
		widget.init ();
		widget.setSelectable (true);
		widget.setEditable((style & SWT.READ_ONLY) == 0);
		if ((style & SWT.BORDER) == 0) {
			widget.setFocusRingType (OS.NSFocusRingTypeNone);
			widget.setBordered (false);
		}
		int align = OS.NSLeftTextAlignment;
		if ((style & SWT.CENTER) != 0) align = OS.NSCenterTextAlignment;
		if ((style & SWT.RIGHT) != 0) align = OS.NSRightTextAlignment;
		widget.setAlignment (align);
		NSCell cell = widget.cell();
		cell.setWraps(false);
		cell.setScrollable(true);
//		widget.setTarget(widget);
//		widget.setAction(OS.sel_sendSelection);
		view = widget;
	} else {
		NSScrollView scrollWidget = (NSScrollView) new SWTScrollView ().alloc ();
		scrollWidget.init ();
		scrollWidget.setHasVerticalScroller ((style & SWT.VERTICAL) != 0);
		scrollWidget.setHasHorizontalScroller ((style & SWT.HORIZONTAL) != 0);
		scrollWidget.setAutoresizesSubviews (true);
		if ((style & SWT.BORDER) != 0) scrollWidget.setBorderType (OS.NSBezelBorder);
		
		NSTextView widget = (NSTextView) new SWTTextView ().alloc ();
		widget.init ();
		widget.setEditable ((style & SWT.READ_ONLY) == 0);
		
		NSSize size = new NSSize ();
		size.width = size.height = Float.MAX_VALUE;
		widget.setMaxSize (size);
		widget.setAutoresizingMask (OS.NSViewWidthSizable | OS.NSViewHeightSizable);

		if ((style & SWT.WRAP) == 0) {
			NSTextContainer textContainer = widget.textContainer ();
			widget.setHorizontallyResizable (true);
			textContainer.setWidthTracksTextView (false);
			NSSize csize = new NSSize ();
			csize.width = csize.height = Float.MAX_VALUE;
			textContainer.setContainerSize (csize);
		}

		int align = OS.NSLeftTextAlignment;
		if ((style & SWT.CENTER) != 0) align = OS.NSCenterTextAlignment;
		if ((style & SWT.RIGHT) != 0) align = OS.NSRightTextAlignment;
		widget.setAlignment (align);
//		widget.setTarget(widget);
//		widget.setAction(OS.sel_sendSelection);
		widget.setRichText (false);
		widget.setDelegate(widget);
		widget.setFont (display.getSystemFont ().handle);
		widget.setUsesFontPanel(false);

		view = widget;
		scrollView = scrollWidget;
	}
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.PASSWORD) != 0) {
		NSText fieldEditor = view.window().fieldEditor(true, view);
		int /*long*/ nsSecureTextViewClass = OS.objc_lookUpClass("NSSecureTextView");
		if (fieldEditor != null && nsSecureTextViewClass != 0 && fieldEditor.isKindOfClass(nsSecureTextViewClass)) {
			int /*long*/ editorClass = OS.objc_getClass("SWTSecureEditorView");
			OS.object_setClass(fieldEditor.id, editorClass);
		}
	}
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
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	boolean cut = true;
	char [] oldText = null;
	Point oldSelection = getSelection ();
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		if (oldSelection.x != oldSelection.y) {
			oldText = getEditText (oldSelection.x, oldSelection.y - 1);
			String newText = verifyText ("", oldSelection.x, oldSelection.y, null);
			if (newText == null) return;
			if (newText.length () != 0) {
				copyToClipboard (oldText);
				if ((style & SWT.SINGLE) != 0) {
					insertEditText (newText);
				} else {
					NSTextView widget = (NSTextView) view;
					widget.replaceCharactersInRange (widget.selectedRange (), NSString.stringWith (newText));
				}
				cut = false;
			}
		}
	}
	if (cut) {
		if ((style & SWT.SINGLE) != 0) {
			if (oldText == null) oldText = getEditText (oldSelection.x, oldSelection.y - 1);
			copyToClipboard (oldText);
			insertEditText ("");
		} else {
			((NSTextView) view).cut (null);
		}
	}
	Point newSelection = getSelection ();
	if (!cut || !oldSelection.equals (newSelection)) sendEvent (SWT.Modify);
}

Color defaultBackground () {
	return display.getWidgetColor (SWT.COLOR_LIST_BACKGROUND);
}

NSFont defaultNSFont () {
	if ((style & SWT.MULTI) != 0) return display.textViewFont;
	if ((style & SWT.SEARCH) != 0) return display.searchFieldFont;
	if ((style & SWT.PASSWORD) != 0) return display.secureTextFieldFont;
	return display.textFieldFont;
}

Color defaultForeground () {
	return display.getWidgetColor (SWT.COLOR_LIST_FOREGROUND);
}

void deregister() {
	super.deregister();
	
	if ((style & SWT.SINGLE) != 0) {
		display.removeWidget(((NSControl)view).cell());
	}
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	if ((style & SWT.SINGLE) != 0) {
		if (backgroundImage == null) return;
		if (new NSView(id).isKindOfClass(OS.class_NSText)) {
			NSText text = new NSText(id);
			if (!text.isFieldEditor()) return;
		}
	}
	if ((style & SWT.MULTI) != 0 && id != scrollView.id) return;
	fillBackground (view, context, rect, -1);
}

void drawInteriorWithFrame_inView(int id, int sel, NSRect cellFrame, int viewid) {
	Control control = findBackgroundControl();
	if (control == null) control = this;
	Image image = control.backgroundImage;
	if (image != null && !image.isDisposed()) {
		NSGraphicsContext context = NSGraphicsContext.currentContext();
 	 	control.fillBackground (view, context, cellFrame, -1);
	}
	super.drawInteriorWithFrame_inView(id, sel, cellFrame, viewid);
}


boolean dragDetect (int x, int y, boolean filter, boolean [] consume) {
	Point selection = getSelection ();
	if (selection.x != selection.y) {
		int /*long*/ position = getPosition (x, y);
		if (selection.x <= position && position < selection.y) {
			if (super.dragDetect (x, y, filter, consume)) {
				if (consume != null) consume [0] = true;
				return true;
			}
		}
	}
	return false;
}

void enableWidget(boolean enabled) {
	super.enableWidget(enabled);
	
	if ((style & SWT.MULTI) != 0) {
		setForeground(this.foreground);
	}
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
    return (getTopPixel () + getCaretLocation ().y) / getLineHeight ();
}

boolean acceptsFirstResponder(int /*long*/ id, int /*long*/ sel) {
	if ((style & SWT.READ_ONLY) != 0) return true;
	return super.acceptsFirstResponder(id, sel);
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
	if ((style & SWT.SINGLE) != 0) {
		//TODO - caret location for single text
		return new Point (0, 0);
	}
	NSTextView widget = (NSTextView)view;
	NSLayoutManager layoutManager = widget.layoutManager();
	NSTextContainer container = widget.textContainer();
	NSRange range = widget.selectedRange();
	int /*long*/ [] rectCount = new int /*long*/ [1];
	int /*long*/ pArray = layoutManager.rectArrayForCharacterRange(range, range, container, rectCount);
	NSRect rect = new NSRect();
	if (rectCount[0] > 0) OS.memmove(rect, pArray, NSRect.sizeof);
	return new Point((int)rect.x, (int)rect.y);
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
	if ((style & SWT.SINGLE) != 0) {
		return selectionRange != null ? (int)/*64*/selectionRange.location : 0;
	} else {
		NSRange range = ((NSTextView)view).selectedRange();
		return (int)/*64*/range.location;
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
		return (int)((NSControl) view).stringValue().length ();
	} else {
		return (int)/*64*/((NSTextView) view).textStorage ().length ();
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
	checkWidget ();
	return (style & SWT.READ_ONLY) == 0;
}

char [] getEditText () {
	if (hiddenText != null) {
		char [] text = new char [hiddenText.length];
		System.arraycopy (hiddenText, 0, text, 0, text.length);
		return text;
	}
	NSString str = null;
	if ((style & SWT.SINGLE) != 0) {
		str = ((NSTextField) view).stringValue();
	} else {
		str = ((NSTextView)view).textStorage().string();
	}

	int length = (int)/*64*/str.length ();
	char [] buffer = new char [length];
	NSRange range = new NSRange ();
	range.length = length;
	str.getCharacters (buffer, range);
	return buffer;
}

char [] getEditText (int start, int end) {
	NSString str = null;
	if ((style & SWT.SINGLE) != 0) {
		str = ((NSTextField) view).stringValue();
	} else {
		str = ((NSTextView)view).textStorage().string();
	}

	int length = (int)/*64*/str.length ();
	end = Math.min (end, length - 1);
	if (start > end) return new char [0];
	start = Math.max (0, start);
	NSRange range = new NSRange ();
	range.location = start;
	range.length = Math.max (0, end - start + 1);
	char [] buffer = new char [(int)/*64*/range.length];
	if (hiddenText != null) {
		System.arraycopy (hiddenText, (int)/*64*/range.location, buffer, 0, buffer.length);
	} else {
		str.getCharacters (buffer, range);
	}
	return buffer;
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
	NSTextStorage storage = ((NSTextView) view).textStorage ();
	int count = (int)/*64*/storage.paragraphs ().count ();
	NSString string = storage.string();
	int /*long*/ length = string.length(), c;
	if (length == 0 || (c = string.characterAtIndex(length - 1)) == '\n' || c == '\r') {
		count++;
	}
	return count;
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
	Font font = this.font != null ? this.font : defaultFont();
	if ((style & SWT.SINGLE) != 0) {
		NSDictionary dict = NSDictionary.dictionaryWithObject(font.handle, OS.NSFontAttributeName);
		NSString str = NSString.stringWith(" ");
		NSAttributedString attribStr = ((NSAttributedString)new NSAttributedString().alloc()).initWithString(str, dict);
		NSSize size = attribStr.size();
		attribStr.release();
		return (int) size.height;
	} else {
		NSTextView widget = (NSTextView)view;
		return (int)Math.ceil(widget.layoutManager().defaultLineHeightForFont(font.handle));
	}
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
	checkWidget ();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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

int /*long*/ getPosition (int /*long*/ x, int /*long*/ y) {
//	checkWidget ();
	if ((style & SWT.MULTI) != 0) {
		NSTextView widget = (NSTextView) view;
		NSPoint viewLocation = new NSPoint();
		viewLocation.x = x;
		viewLocation.y = y;
		return widget.characterIndexForInsertionAtPoint(viewLocation);
	} else {
		//TODO 
		return 0;
	}
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
	if ((style & SWT.SINGLE) != 0) {
		if (selectionRange == null) {
			NSString str = ((NSTextField) view).stringValue();
			return new Point((int)/*64*/str.length (), (int)/*64*/str.length ());
		}
		return new Point ((int)/*64*/selectionRange.location, (int)/*64*/(selectionRange.location + selectionRange.length));
	} else {
		NSTextView widget = (NSTextView) view;
		NSRange range = widget.selectedRange ();
		return new Point ((int)/*64*/range.location, (int)/*64*/(range.location + range.length));
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		return selectionRange != null ? (int)/*64*/selectionRange.length : 0;
	} else {
		NSTextView widget = (NSTextView) view;
		NSRange range = widget.selectedRange ();
		return (int)/*64*/range.length;
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		Point selection = getSelection ();
		if (selection.x == selection.y) return "";
		return new String (getEditText (selection.x, selection.y - 1));
	} else {
		NSTextView widget = (NSTextView) view;
		NSRange range = widget.selectedRange ();
		NSString str = widget.textStorage ().string ();
		char[] buffer = new char [(int)/*64*/range.length];
		str.getCharacters (buffer, range);
		return new String (buffer);
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
	checkWidget ();
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
	checkWidget ();
	NSString str;
	if ((style & SWT.SINGLE) != 0) {
		return new String (getEditText ());
	} else {
		str = ((NSTextView)view).textStorage ().string ();
	}
	return str.getString();
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
	if (!(start <= end && 0 <= end)) return ""; //$NON-NLS-1$
	if ((style & SWT.SINGLE) != 0) {
		return new String (getEditText (start, end));
	}
	NSTextStorage storage = ((NSTextView) view).textStorage ();
	end = Math.min (end, (int)/*64*/storage.length () - 1);
	if (start > end) return ""; //$NON-NLS-1$
	start = Math.max (0, start);
	NSRange range = new NSRange ();
	range.location = start;
	range.length = end - start + 1;
	NSAttributedString substring = storage.attributedSubstringFromRange (range);
	NSString string = substring.string ();
	return string.getString();
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
public char[] getTextChars () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		return getEditText ();
	} else {
		NSString str = ((NSTextView)view).textStorage ().string ();
		int length = (int)/*64*/str.length ();
		char [] buffer = new char [length];
		NSRange range = new NSRange ();
		range.length = length;
		str.getCharacters (buffer, range);
		return buffer;
	}
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
	checkWidget ();
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
	return (int)scrollView.contentView().bounds().y;
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
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		Point selection = getSelection ();
		string = verifyText (string, selection.x, selection.y, null);
		if (string == null) return;
	}
	if ((style & SWT.SINGLE) != 0) {
		insertEditText (string);
	} else {
		NSString str = NSString.stringWith (string);
		NSTextView widget = (NSTextView) view;
		NSRange range = widget.selectedRange ();
		widget.textStorage ().replaceCharactersInRange (range, str);
	}
	if (string.length () != 0) sendEvent (SWT.Modify);
}

void insertEditText (String string) {
	int length = string.length ();
	Point selection = getSelection ();
	if (hasFocus () && hiddenText == null) {
		if (textLimit != LIMIT) {
			int charCount = getCharCount();
			if (charCount - (selection.y - selection.x) + length > textLimit) {
				length = textLimit - charCount + (selection.y - selection.x);
				length = Math.max(0, length);
			}
		}
		char [] buffer = new char [length];
		string.getChars (0, buffer.length, buffer, 0);
		NSString nsstring = NSString.stringWithCharacters (buffer, buffer.length);
		NSText fieldEditor = ((NSTextField) view).currentEditor ();
		if (fieldEditor != null) fieldEditor.replaceCharactersInRange (fieldEditor.selectedRange (), nsstring);
		selectionRange = null;
	} else {
		String oldText = getText ();
		if (textLimit != LIMIT) {
			int charCount = oldText.length ();
			if (charCount - (selection.y - selection.x) + length > textLimit) {
				string = string.substring(0, textLimit - charCount + (selection.y - selection.x));
			}
		}
		String newText = oldText.substring (0, selection.x) + string + oldText.substring (selection.y);
		setEditText (newText);
		setSelection (selection.x + string.length ());
	}
}

boolean isEventView (int /*long*/ id) {
	if ((style & SWT.MULTI) != 0) return super.isEventView (id);
	return true;
}

boolean isNeeded(ScrollBar scrollbar) {
	boolean result = false;
	if ((style & SWT.MULTI) != 0) {
		NSRect docFrame = scrollView.documentView().frame();
		NSRect contentFrame = scrollView.contentView().frame();
		if ((scrollbar.style & SWT.VERTICAL) != 0) {
			result = docFrame.height > contentFrame.height;
		} else {
			result = docFrame.width > contentFrame.width;
		}
	}
	return result;
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
	if ((style & SWT.READ_ONLY) != 0) return;
	boolean paste = true;
	String oldText = null;
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		oldText = getClipboardText ();
		if (oldText != null) {
			Point selection = getSelection ();
			String newText = verifyText (oldText, selection.x, selection.y, null);
			if (newText == null) return;
			if (!newText.equals (oldText)) {
				if ((style & SWT.SINGLE) != 0) {
					insertEditText (newText);
				} else {
					NSTextView textView = (NSTextView) view;
					textView.replaceCharactersInRange (textView.selectedRange (), NSString.stringWith (newText));
				}
				paste = false;
			}
		}
	}
	if (paste) {
		if ((style & SWT.SINGLE) != 0) {
			if (oldText == null) oldText = getClipboardText ();
			if (oldText == null) return;
			insertEditText (oldText);
		} else {
			//TODO check text limit
			((NSTextView) view).paste (null);
		}
	}
	sendEvent (SWT.Modify);
}

void register() {
	super.register();
	
	if ((style & SWT.SINGLE) != 0) {
		display.addWidget(((NSControl)view).cell(), this);
	}
}

void releaseWidget () {
	super.releaseWidget ();
	if ((style & SWT.SINGLE) != 0) ((NSControl)view).abortEditing();
	hiddenText = null;
	message = null;
	selectionRange = null;
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
public void removeSelectionListener(SelectionListener listener) {
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
	if ((style & SWT.SINGLE) != 0) {
		setSelection (0, getCharCount ());
	} else {
		((NSTextView) view).selectAll (null);
	}
}

boolean sendKeyEvent (NSEvent nsEvent, int type) {
	boolean result = super.sendKeyEvent (nsEvent, type);
	if (!result) return result;
	if (type != SWT.KeyDown) return result;
	int stateMask = 0;
	int /*long*/ modifierFlags = nsEvent.modifierFlags();
	if ((modifierFlags & OS.NSAlternateKeyMask) != 0) stateMask |= SWT.ALT;
	if ((modifierFlags & OS.NSShiftKeyMask) != 0) stateMask |= SWT.SHIFT;
	if ((modifierFlags & OS.NSControlKeyMask) != 0) stateMask |= SWT.CONTROL;
	if ((modifierFlags & OS.NSCommandKeyMask) != 0) stateMask |= SWT.COMMAND;
	if (stateMask == SWT.COMMAND) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 7: /* X */
				if ((style & SWT.PASSWORD) == 0) cut ();
				return false;
			case 8: /* C */
				if ((style & SWT.PASSWORD) == 0) copy ();
				return false;
			case 9: /* V */
				paste ();
				return false;
		}
	}
	if ((style & SWT.SINGLE) != 0) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 76: /* KP Enter */
			case 36: /* Return */
				sendSelectionEvent (SWT.DefaultSelection);
		}
	}
	return result;
}

void sendSearchSelection () {
	if (targetSearch != null) {
		((NSSearchField)view).sendAction(actionSearch, targetSearch);
	}
	Event event = new Event ();
	event.detail = SWT.ICON_SEARCH;
	sendSelectionEvent (SWT.DefaultSelection, event, false);
}

void sendCancelSelection () {
	if (targetCancel != null) {
		((NSSearchField)view).sendAction(actionCancel, targetCancel);
	}
	Event event = new Event ();
	event.detail = SWT.ICON_CANCEL;
	sendSelectionEvent (SWT.DefaultSelection, event, false);
}

void setBackgroundColor(NSColor nsColor) {
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField) view).setBackgroundColor (nsColor);
	} else {
		((NSTextView) view).setBackgroundColor (nsColor);
	}
}

void setBackgroundImage(NSImage image) {
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget = (NSTextField) view;
		widget.setDrawsBackground(image == null);
	} else {
		((NSTextView) view).setDrawsBackground(image == null);
		scrollView.setDrawsBackground(image == null);
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
	if ((style & SWT.MULTI) != 0) return;
	if ((style & SWT.PASSWORD) == 0) {
		Point selection = getSelection ();
		char [] text = getTextChars ();
		echoCharacter = echo;
		setEditText (text);
		setSelection (selection);
	}
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
	checkWidget ();
	if (editable) {
		style &= ~SWT.READ_ONLY;
	} else {
		style |= SWT.READ_ONLY;
	}
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField) view).setEditable (editable);
	} else {
		((NSTextView) view).setEditable (editable);
	}
}

void setEditText (String string) {
	char [] text = new char [string.length()];
	string.getChars(0, text.length, text, 0);
	setEditText (text);
}

void setEditText (char[] text) {
	char [] buffer;
	int length = Math.min(text.length, textLimit);
	if ((style & SWT.PASSWORD) == 0 && echoCharacter != '\0') {
		hiddenText = new char [length];
		buffer = new char [length];
		for (int i = 0; i < length; i++) {
			hiddenText [i] = text [i];
			buffer [i] = echoCharacter;
		}
	} else {
		hiddenText = null;
		buffer = text;
	}
	NSTextField widget = (NSTextField)view;
	NSString nsstring = NSString.stringWithCharacters (buffer, length);
	widget.setStringValue(nsstring);
	NSText fieldEditor = widget.currentEditor();
	if (fieldEditor != null) {
		fieldEditor.setString(nsstring);
	}
	selectionRange = null;
}

void setFrameSize(int /*long*/ id, int /*long*/ sel, NSSize size) {
	super.setFrameSize (id, sel, size);
	/*
	* Bug in Cocoa.  When a search field is resized while having
	* focus, its editor is not properly positioned within the
	* widget.   The fix is to reposition the editor.
	*/
	if ((style & SWT.SEARCH) != 0) {
		NSSearchField widget = (NSSearchField)view;
		NSText editor = widget.currentEditor ();
		if (editor != null) {
			NSArray subviews = widget.subviews ();
			if (subviews.count () > 0) {
				NSRect rect = widget.cell ().drawingRectForBounds (widget.bounds ());
				new NSView (subviews.objectAtIndex (0)).setFrame (rect);
			}
		}
	}
}

void setFont(NSFont font) {
	if ((style & SWT.MULTI) !=  0) {
		((NSTextView) view).setFont (font);
		return;
	}
	super.setFont (font);
}

void setForeground (float /*double*/ [] color) {
	NSColor nsColor;
	if (color == null) {
		nsColor = NSColor.textColor ();
		if ((style & SWT.MULTI) != 0 && !isEnabled()) nsColor = NSColor.disabledControlTextColor();
	} else {
		float /*double*/ alpha = 1;
		if ((style & SWT.MULTI) != 0 && !isEnabled()) alpha = 0.5f;
		nsColor = NSColor.colorWithCalibratedRed (color [0], color [1], color [2], alpha);
	}
	if ((style & SWT.SINGLE) != 0) {
		((NSTextField) view).setTextColor (nsColor);
	} else {
		((NSTextView) view).setTextColor (nsColor);
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
	checkWidget ();
}

void setOrientation () {
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	if ((style & SWT.SINGLE) != 0) {
		NSTextField widget = (NSTextField)view;
		widget.setBaseWritingDirection(direction);
	} else {
		NSTextView widget = (NSTextView)view;
		widget.setBaseWritingDirection(direction);
	}
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
	if ((style & SWT.SINGLE) != 0) {
		NSString str = NSString.stringWith (message);
		NSTextFieldCell cell = new NSTextFieldCell (((NSTextField) view).cell ());
		cell.setPlaceholderString (str);
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
	checkWidget ();
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		NSString str = ((NSTextField) view).stringValue();
		int length = (int)/*64*/str.length ();
		int selStart = Math.min (Math.max (Math.min (start, end), 0), length);
		int selEnd = Math.min (Math.max (Math.max (start, end), 0), length);
		selectionRange = new NSRange ();
		selectionRange.location = selStart;
		selectionRange.length = selEnd - selStart;
		NSText fieldEditor = ((NSControl)view).currentEditor();
		if (fieldEditor != null) {
			fieldEditor.setSelectedRange (selectionRange);
			fieldEditor.scrollRangeToVisible (selectionRange);
		}
	} else {
		int length = (int)/*64*/((NSTextView) view).textStorage ().length ();
		int selStart = Math.min (Math.max (Math.min (start, end), 0), length);
		int selEnd = Math.min (Math.max (Math.max (start, end), 0), length);
		NSRange range = new NSRange ();
		range.location = selStart;
		range.length = selEnd - selStart;
		NSTextView widget = (NSTextView) view;
		widget.setSelectedRange (range);
		widget.scrollRangeToVisible (range);
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
	if (this.tabs == tabs) return;
	this.tabs = tabs;
	if ((style & SWT.SINGLE) != 0) return;
	float /*double*/ size = textExtent("s").width * tabs;
	NSTextView widget = (NSTextView)view;
	NSParagraphStyle defaultStyle = widget.defaultParagraphStyle();
	NSMutableParagraphStyle paragraphStyle = new NSMutableParagraphStyle(defaultStyle.mutableCopy());
	paragraphStyle.setTabStops(NSArray.array());
	NSTextTab tab = (NSTextTab)new NSTextTab().alloc();
	tab = tab.initWithType(OS.NSLeftTabStopType, size);
	paragraphStyle.addTabStop(tab);
	tab.release();
	paragraphStyle.setDefaultTabInterval(size);
	widget.setDefaultParagraphStyle(paragraphStyle);
	paragraphStyle.release();
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
		string = verifyText (string, 0, getCharCount (), null);
		if (string == null) return;
	}
	if ((style & SWT.SINGLE) != 0) {
		setEditText (string);
		NSText fieldEditor = ((NSControl)view).currentEditor();
		if (fieldEditor != null) {
			NSRange range = new NSRange();
			fieldEditor.setSelectedRange (range);
			fieldEditor.scrollRangeToVisible (range);
		}
	} else {
		NSTextView widget = (NSTextView)view;
		char[] buffer = new char [Math.min(string.length (), textLimit)];
		string.getChars (0, buffer.length, buffer, 0);
		NSString str = NSString.stringWithCharacters(buffer, buffer.length);
		widget.setString (str);
		widget.setSelectedRange(new NSRange());
	}
	sendEvent (SWT.Modify);
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
public void setTextChars (char[] text) {
	checkWidget ();
	if (text == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		String string = verifyText (new String (text), 0, getCharCount (), null);
		if (string == null) return;
		text = new char [string.length()];
		string.getChars (0, text.length, text, 0);
	}
	if ((style & SWT.SINGLE) != 0) {
		setEditText (text);
		NSText fieldEditor = ((NSControl)view).currentEditor();
		if (fieldEditor != null) {
			NSRange range = new NSRange();
			fieldEditor.setSelectedRange (range);
			fieldEditor.scrollRangeToVisible (range);
		}
	} else {
		NSTextView widget = (NSTextView)view;
		int length = Math.min(text.length, textLimit);
		NSString str = NSString.stringWithCharacters(text, length);
		widget.setString (str);
		widget.setSelectedRange(new NSRange());
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
	checkWidget ();
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	int row = Math.max(0, Math.min(index, getLineCount() - 1));
	NSPoint pt = new NSPoint();
	pt.x = scrollView.contentView().bounds().x;
	pt.y = getLineHeight() * row;
	view.scrollPoint(pt);
}

boolean shouldChangeTextInRange_replacementString(int /*long*/ id, int /*long*/ sel, int /*long*/ affectedCharRange, int /*long*/ replacementString) {
	NSRange range = new NSRange();
	OS.memmove(range, affectedCharRange, NSRange.sizeof);
	boolean result = callSuperBoolean(id, sel, range, replacementString);
	NSString nsString = new NSString(replacementString);
	if (!hooks(SWT.Verify) && ((echoCharacter =='\0') || (style & SWT.PASSWORD) != 0)) {
		if (!result || (getCharCount() - range.length + nsString.length() > textLimit)) {
			return false;
		}
		return true;
	}
	String text = nsString.getString();
	String newText = text;
	if (hooks (SWT.Verify)) {
		NSEvent currentEvent = display.application.currentEvent();
		int /*long*/ type = currentEvent.type();
		if (type != OS.NSKeyDown && type != OS.NSKeyUp) currentEvent = null;
		newText = verifyText(text, (int)/*64*/range.location, (int)/*64*/(range.location+range.length),  currentEvent);
	}
	if (newText == null) return false;
	if (getCharCount() - range.length + newText.length() > textLimit) {
		return false;
	}
	if ((style & SWT.SINGLE) != 0) {
		if (text != newText || echoCharacter != '\0') {
			 //handle backspace and delete
			if (range.length == 1) {
				NSText editor = new NSText(id);
				editor.setSelectedRange (range);
			}
			insertEditText(newText);
			result = false;
		}
	} else {
		if (text != newText) {
			NSTextView widget = (NSTextView) view;
			NSRange selRange = new NSRange();
			Point selection = getSelection();
			selRange.location = selection.x;
			selRange.length = selection.x + selection.y;
			widget.textStorage ().replaceCharactersInRange (selRange, NSString.stringWith(newText));
			result = false;
		}
	}
	if (!result) sendEvent (SWT.Modify);
	return result;
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
	if ((style & SWT.SINGLE) != 0)  {
		setSelection (getSelection ());
	} else {
		NSTextView widget = (NSTextView) view;
		widget.scrollRangeToVisible (widget.selectedRange ());
	}
}

void textViewDidChangeSelection(int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	NSNotification notification = new NSNotification (aNotification);
	NSText editor = new NSText (notification.object ().id);
	selectionRange = editor.selectedRange ();
}

void textDidChange (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	if ((style & SWT.SINGLE) != 0) super.textDidChange (id, sel, aNotification);
	postEvent (SWT.Modify);
}

NSRange textView_willChangeSelectionFromCharacterRange_toCharacterRange (int /*long*/ id, int /*long*/ sel, int /*long*/ aTextView, int /*long*/ oldSelectedCharRange, int /*long*/ newSelectedCharRange) {
	/*
	* If the selection is changing as a result of the receiver getting focus
	* then return the receiver's last selection range, otherwise the full
	* text will be automatically selected.
	*/
	if (receivingFocus && selectionRange != null) return selectionRange;

	/* allow the selection change to proceed */
	NSRange result = new NSRange ();
	OS.memmove(result, newSelectedCharRange, NSRange.sizeof);
	return result;
}

int traversalCode (int key, NSEvent theEvent) {
	int bits = super.traversalCode (key, theEvent);
	if ((style & SWT.READ_ONLY) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == 48 /* Tab */ && theEvent != null) {
			int /*long*/ modifiers = theEvent.modifierFlags ();
			boolean next = (modifiers & OS.NSShiftKeyMask) == 0;
			if (next && (modifiers & OS.NSControlKeyMask) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}

void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (scrollView == null) return;
	NSClipView contentView = scrollView.contentView ();
	contentView.setDocumentCursor (enabled ? NSCursor.IBeamCursor () : null);
}

String verifyText (String string, int start, int end, NSEvent keyEvent) {
	Event event = new Event ();
	if (keyEvent != null) setKeyState(event, SWT.MouseDown, keyEvent);
	event.text = string;
	event.start = start;
	event.end = end;
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
