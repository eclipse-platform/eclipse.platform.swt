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
 * Instances of this class represent a selectable
 * user interface object that displays a text with 
 * links.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#link">Link snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Link extends Control {
	NSScrollView scrollView;
	String text;
	Point [] offsets;
	String [] ids;
	int [] mnemonics;
	NSColor linkColor;
	int focusIndex;
	
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Link (Composite parent, int style) {
	super (parent, style);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

boolean textView_clickOnLink_atIndex(int /*long*/ id, int /*long*/ sel, int /*long*/ textView, int /*long*/ link, int /*long*/ charIndex) {
	NSString str = new NSString (link);
	Event event = new Event ();
	event.text = str.getString();
	sendSelectionEvent (SWT.Selection, event, true);
	for (int i = 0; i < offsets.length; i++) {
		if ((charIndex >= offsets[i].x) && (charIndex <= offsets[i].y)) {
			focusIndex = i;
			break;
		}
	}
	redrawWidget(view, false);
	return true;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;

	int width = 0, height = 0;
	NSLayoutManager layoutManager = (NSLayoutManager)new NSLayoutManager ().alloc ().init ();
	NSTextContainer textContainer = (NSTextContainer)new NSTextContainer ().alloc ();
	NSSize size = new NSSize ();
	size.width = size.height = Float.MAX_VALUE;
	if (wHint != SWT.DEFAULT) size.width = wHint;
	if (hHint != SWT.DEFAULT) size.height = hHint;
	textContainer.initWithContainerSize (size);
	textContainer.setLineFragmentPadding(2);
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

	// Accommodate any border.
	size.width = width;
	size.height = height;
	int border = hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder;
	size = NSScrollView.frameSizeForContentSize(size, false, false, border);
	width = (int)size.width;
	height = (int)size.height;

	if (!hasBorder()) {
		width += 2;
		height += 2;
	}
	return new Point (width, height);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
	scrollWidget.init();
	scrollWidget.setDrawsBackground(false);
	scrollWidget.setAutoresizesSubviews (true);
	scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);

	NSTextView widget = (NSTextView)new SWTTextView().alloc();
	widget.init();
	widget.setEditable(false);
	NSSize size = new NSSize ();
	size.width = size.height = Float.MAX_VALUE;
	widget.setMaxSize (size);
	widget.setDrawsBackground(false);
	widget.setDelegate(widget);
	widget.setAutoresizingMask (OS.NSViewWidthSizable | OS.NSViewHeightSizable);
	widget.textContainer().setLineFragmentPadding(2);
	widget.setFont(getFont().handle);
	widget.setAlignment (OS.NSLeftTextAlignment);

	NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity(4);
	dict.setDictionary(widget.selectedTextAttributes());
	dict.removeObjectForKey(OS.NSBackgroundColorAttributeName);
	dict.setObject(NSCursor.arrowCursor(), OS.NSCursorAttributeName);
	widget.setSelectedTextAttributes(dict);
	
	scrollView = scrollWidget;
	view = widget;
}

void createWidget () {
	super.createWidget ();
	text = "";
	NSDictionary dict = ((NSTextView)view).linkTextAttributes();
	linkColor = new NSColor(dict.valueForKey(OS.NSForegroundColorAttributeName));
	offsets = new Point [0];
	ids = new String [0];
	mnemonics = new int [0]; 
	focusIndex = -1;
}

NSFont defaultNSFont () {
	return display.textFieldFont;
}

void deregister () {
	super.deregister ();
	if (scrollView != null) display.removeWidget (scrollView);
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rectangle) {
	fillBackground (view, context, rectangle, -1);
	if (!hasFocus() || focusIndex == -1) return;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
	outMetric[0]--;
	CGRect r = new CGRect();
	NSRect[] rect = getRectangles(focusIndex);
	if (rect == null) return;
	for (int i = 0; i < rect.length && rect[i] != null; i++) {
		r.origin.x = rect[i].x + outMetric[0];
		r.origin.y = rect[i].y + outMetric[0];
		/*
		 * sometimes the rect[i].width is smaller than 2 * outMetric and subtracting
		 * it makes r.size.width < 0
		 */
		r.size.width = rect[i].width - outMetric[0];
		r.size.height = rect[i].height - (2 * outMetric[0]);
		OS.HIThemeDrawFocusRect(r, true, context.graphicsPort(), OS.kHIThemeOrientationNormal);
	}
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	NSColor nsColor = null; 
	if (enabled) {
		if (foreground == null) {
			nsColor = NSColor.textColor ();
		} else {
			nsColor = NSColor.colorWithDeviceRed (foreground [0], foreground [1], foreground [2], foreground[3]);
		}
	} else {
		nsColor = NSColor.disabledControlTextColor();
	}
	NSTextView widget = (NSTextView)view;
	widget.setTextColor(nsColor);
	NSDictionary linkTextAttributes = widget.linkTextAttributes();
	int count = (int)/*64*/linkTextAttributes.count();
	NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity(count);
	dict.setDictionary(linkTextAttributes);
	dict.setValue(enabled ? linkColor : nsColor, OS.NSForegroundColorAttributeName);
	widget.setLinkTextAttributes(dict);
	redrawWidget(view, false);
}

NSRect[] getRectangles(int linkIndex) {
	/*
	 * Returns the focus rectangles to be drawn for a link. Number of
	 * rectangles is > 1 when the link has multiple lines.
	 */
	if (linkIndex == -1) return null;
	
	NSTextView widget = ((NSTextView)view);
	NSLayoutManager layoutManager = widget.layoutManager();
	NSRange range = new NSRange();
	range.location = offsets[linkIndex].x;
	range.length = offsets[linkIndex].y - offsets[linkIndex].x + 1;
	NSRange glyphRange = layoutManager.glyphRangeForCharacterRange(range, 0);

	int /*long*/ rangePtr = OS.malloc(NSRange.sizeof);
	NSRange lineRange = new NSRange();
	
	/* compute number of lines in the link */
	int numberOfLines = 0;
	int /*long*/ index = glyphRange.location;
	int /*long*/ glyphEndIndex = glyphRange.location + glyphRange.length;
	while (index < glyphEndIndex) {
		numberOfLines++;
		layoutManager.lineFragmentUsedRectForGlyphAtIndex(index, rangePtr, true);
		OS.memmove(lineRange, rangePtr, NSRange.sizeof);
		index = lineRange.location + lineRange.length;
	}
	
	/* compute the enclosing rectangle(s) for the link*/
	NSRect [] result = new NSRect[numberOfLines];
	index = glyphRange.location;
	for (int i = 0; index < glyphEndIndex && i < numberOfLines; i++) {
		NSRect usedRect = layoutManager.lineFragmentUsedRectForGlyphAtIndex(index, rangePtr, true);
		OS.memmove(lineRange, rangePtr, NSRange.sizeof);
		index = lineRange.location + lineRange.length;
		
		if (lineRange.location < glyphRange.location) {
			lineRange.length = index - glyphRange.location;
			lineRange.location = glyphRange.location;
		}
		if (index > glyphEndIndex) lineRange.length = glyphEndIndex - lineRange.location;
		NSRect boundsRect = layoutManager.boundingRectForGlyphRange(lineRange, widget.textContainer());
		result[i] = new NSRect();
		OS.NSIntersectionRect(result[i], usedRect, boundsRect);
	}
	OS.free(rangePtr);
	return result;
}

String getNameText () {
	return getText ();
}


/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
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
	return text;
}

boolean shouldDrawInsertionPoint(int /*long*/ id, int /*long*/ sel) {
	return false;
}

void register () {
	super.register ();
	display.addWidget(scrollView, this);
}

void releaseWidget () {
	super.releaseWidget ();
	offsets = null;
	ids = null;
	mnemonics = null;
	text = null;
	linkColor = null;
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
	eventTable.unhook (SWT.DefaultSelection, listener);	
}

String parse (String string) {
	int length = string.length ();
	offsets = new Point [length / 4];
	ids = new String [length / 4];
	mnemonics = new int [length / 4 + 1];
	StringBuffer result = new StringBuffer ();
	char [] buffer = new char [length];
	string.getChars (0, string.length (), buffer, 0);
	int index = 0, state = 0, linkIndex = 0;
	int start = 0, tagStart = 0, linkStart = 0, endtagStart = 0, refStart = 0;
	while (index < length) {
		char c = Character.toLowerCase (buffer [index]);
		switch (state) {
			case 0: 
				if (c == '<') {
					tagStart = index;
					state++;
				}
				break;
			case 1:
				if (c == 'a') state++;
				break;
			case 2:
				switch (c) {
					case 'h':
						state = 7;
						break;
					case '>':
						linkStart = index  + 1;
						state++;
						break;
					default:
						if (Character.isWhitespace(c)) break;
						else state = 13;
				}
				break;
			case 3:
				if (c == '<') {
					endtagStart = index;
					state++;
				}
				break;
			case 4:
				state = c == '/' ? state + 1 : 3;
				break;
			case 5:
				state = c == 'a' ? state + 1 : 3;
				break;
			case 6:
				if (c == '>') {
					mnemonics [linkIndex] = parseMnemonics (buffer, start, tagStart, result);
					int offset = result.length ();
					parseMnemonics (buffer, linkStart, endtagStart, result);
					offsets [linkIndex] = new Point (offset, result.length () - 1);
					if (ids [linkIndex] == null) {
						ids [linkIndex] = new String (buffer, linkStart, endtagStart - linkStart);
					}
					linkIndex++;
					start = tagStart = linkStart = endtagStart = refStart = index + 1;
					state = 0;
				} else {
					state = 3;
				}
				break;
			case 7:
				state = c == 'r' ? state + 1 : 0;
				break;
			case 8:
				state = c == 'e' ? state + 1 : 0;
				break;
			case 9:
				state = c == 'f' ? state + 1 : 0;
				break;
			case 10:
				state = c == '=' ? state + 1 : 0;
				break;
			case 11:
				if (c == '"') {
					state++;
					refStart = index + 1;
				} else {
					state = 0;
				}
				break;
			case 12:
				if (c == '"') {
					ids[linkIndex] = new String (buffer, refStart, index - refStart);
					state = 2;
				}
				break;
			case 13:
				if (Character.isWhitespace (c)) {
					state = 0;
				} else if (c == '='){
					state++;
				}
				break;
			case 14:
				state = c == '"' ? state + 1 : 0;
				break;
			case 15:
				if (c == '"') state = 2;
				break;
			default:
				state = 0;
				break;
		}
		index++;
	}
	if (start < length) {
		int tmp = parseMnemonics (buffer, start, tagStart, result);
		int mnemonic = parseMnemonics (buffer, Math.max (tagStart, linkStart), length, result);
		if (mnemonic == -1) mnemonic = tmp;
		mnemonics [linkIndex] = mnemonic;
	} else {
		mnemonics [linkIndex] = -1;
	}
	if (offsets.length != linkIndex) {
		Point [] newOffsets = new Point [linkIndex];
		System.arraycopy (offsets, 0, newOffsets, 0, linkIndex);
		offsets = newOffsets;
		String [] newIDs = new String [linkIndex];
		System.arraycopy (ids, 0, newIDs, 0, linkIndex);
		ids = newIDs;
		int [] newMnemonics = new int [linkIndex + 1];
		System.arraycopy (mnemonics, 0, newMnemonics, 0, linkIndex + 1);
		mnemonics = newMnemonics;		
	}
	return result.toString ();
}

int parseMnemonics (char[] buffer, int start, int end, StringBuffer result) {
	int mnemonic = -1, index = start;
	while (index < end) {
		if (buffer [index] == '&') {
			if (index + 1 < end && buffer [index + 1] == '&') {
				result.append (buffer [index]);
				index++;
			} else {
				mnemonic = result.length();
			}
		} else {
			result.append (buffer [index]);
		}
		index++;
	}
	return mnemonic;
}

void sendFocusEvent(int type) {
	if (focusIndex != -1) redrawWidget(view, false);
	super.sendFocusEvent(type);
}

boolean sendKeyEvent(int type, Event event) {
	boolean result = super.sendKeyEvent (type, event);
	if (!result) return result;
	if (focusIndex == -1) return result;
	if (type != SWT.KeyDown)  return result;
	
	int keyCode = event.keyCode;
	switch (keyCode) {
	case SWT.CR:
	case SWT.KEYPAD_CR:
	case 32: /* Space */
		Event event1 = new Event ();
		event1.text = ids [focusIndex];
		sendEvent (SWT.Selection, event1);
		break;
	case SWT.TAB:
		int modifierFlags = event.stateMask;
		boolean next = (modifierFlags & SWT.SHIFT) == 0;
		if (next) {
			if (focusIndex < offsets.length - 1) {
				focusIndex++;
				redraw ();
				return false;
			}
		} else {
			if (focusIndex > 0) {
				focusIndex--;
				redraw ();
				return false;
			} 
		}
		break;
	}
	return result;
}

void setBackgroundColor(NSColor nsColor) {
	setBackground(nsColor);
}

void setBackgroundImage(NSImage image) {
	((NSTextView) view).setDrawsBackground(image == null);
}

void setBackground(NSColor nsColor) {
	NSTextView widget = (NSTextView)view;
	if (nsColor == null) {
		widget.setDrawsBackground(false);
	} else {
		widget.setDrawsBackground(true);
		widget.setBackgroundColor (nsColor);
	}
}

void setFont(NSFont font) {
	((NSTextView) view).setFont(font);
}

void setForeground (float /*double*/ [] color) {
	if (!getEnabled ()) return;
	NSColor nsColor;
	if (color == null) {
		nsColor = NSColor.textColor ();
	} else {
		nsColor = NSColor.colorWithDeviceRed (color [0], color [1], color [2], 1);
	}
	((NSTextView) view).setTextColor (nsColor);
}

void setOrientation () {
	NSTextView widget = (NSTextView)view;
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	widget.setBaseWritingDirection(direction);
}

/**
 * Sets the receiver's text.
 * <p>
 * The string can contain both regular text and hyperlinks.  A hyperlink
 * is delimited by an anchor tag, &lt;A&gt; and &lt;/A&gt;.  Within an
 * anchor, a single HREF attribute is supported.  When a hyperlink is
 * selected, the text field of the selection event contains either the
 * text of the hyperlink or the value of its HREF, if one was specified.
 * In the rare case of identical hyperlinks within the same string, the
 * HREF attribute can be used to distinguish between them.  The string may
 * include the mnemonic character and line delimiters. The only delimiter
 * the HREF attribute supports is the quotation mark (").
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
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals (text)) return;
	text = string;
	NSTextView widget = (NSTextView)view;
	widget.setString(NSString.stringWith(parse(string)));
	focusIndex = offsets.length > 0 ? 0 : -1 ;
	NSTextStorage textStorage = widget.textStorage();
	NSRange range = new NSRange();
	range.length = textStorage.length();
	textStorage.removeAttribute(OS.NSLinkAttributeName, range);
	textStorage.addAttribute(OS.NSCursorAttributeName, NSCursor.arrowCursor(), range);
	for (int i = 0; i < offsets.length; i++) {
		range.location = offsets[i].x;
		range.length = offsets[i].y - offsets[i].x + 1;
		textStorage.addAttribute(OS.NSLinkAttributeName, NSString.stringWith(ids[i]), range);
	}
}

public void setToolTipText(String string) {
	((NSTextView)view).setDisplaysLinkToolTips(string == null);
	super.setToolTipText(string);
}

void setZOrder () {
	super.setZOrder ();
	if (scrollView != null) scrollView.setDocumentView (view);
}

NSView topView () {
	return scrollView;
}

int traversalCode (int key, NSEvent theEvent) {
	if (offsets.length == 0) return  0;
	int bits = super.traversalCode (key, theEvent);
	if (key == 48 /* Tab */ && theEvent != null) {
		int /*long*/ modifierFlags = theEvent.modifierFlags();
		boolean next = (modifierFlags & OS.NSShiftKeyMask) == 0;
		if (next && focusIndex < offsets.length - 1) {
			return bits & ~ SWT.TRAVERSE_TAB_NEXT;
		}
		if (!next && focusIndex > 0) {
			return bits & ~ SWT.TRAVERSE_TAB_PREVIOUS;
		}
	}
	return bits;
}

void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (scrollView == null) return;
	updateCursorRects (enabled, scrollView);	
	NSClipView contentView = scrollView.contentView ();
	updateCursorRects (enabled, contentView);
	contentView.setDocumentCursor (enabled ? NSCursor.IBeamCursor () : null);
}

}

