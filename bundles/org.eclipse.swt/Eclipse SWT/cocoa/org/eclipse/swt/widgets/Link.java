/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Conrad Groth - Bug 401015 - [CSS] Add support for styling hyperlinks in Links
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.Display.*;

/**
 * Instances of this class represent a selectable
 * user interface object that displays a text with
 * links.
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
	double [] linkForeground;
	NSColor defaultLinkColor;
	int focusIndex;
	boolean ignoreNextMouseUp;
	APPEARANCE lastAppAppearance;

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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;

	int width = 0, height = 0;
	NSLayoutManager layoutManager = (NSLayoutManager)new NSLayoutManager ().alloc ().init ();
	NSTextContainer textContainer = (NSTextContainer)new NSTextContainer ().alloc ();
	NSSize size = new NSSize ();
	size.width = size.height = OS.MAX_TEXT_CONTAINER_SIZE;
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

@Override
void createHandle () {
	state |= THEME_BACKGROUND;
	NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
	scrollWidget.init();
	scrollWidget.setDrawsBackground(false);
	scrollWidget.setAutoresizesSubviews (true);
	scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
	scrollWidget.setVerticalScrollElasticity(OS.NSScrollElasticityNone);

	NSTextView widget = (NSTextView)new SWTTextView().alloc();
	widget.init();
	widget.setEditable(false);
	NSSize size = new NSSize ();
	size.width = size.height = Float.MAX_VALUE;
	widget.setMaxSize (size);
	widget.setDisplaysLinkToolTips(false);
	widget.setDrawsBackground(false);
	widget.setDelegate(widget);
	widget.setAutoresizingMask (OS.NSViewWidthSizable | OS.NSViewHeightSizable);
	widget.textContainer().setLineFragmentPadding(2);
	widget.setFont(getFont().handle);
	widget.setAlignment (OS.NSTextAlignmentLeft);

	NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity(4);
	dict.setDictionary(widget.selectedTextAttributes());
	dict.removeObjectForKey(OS.NSBackgroundColorAttributeName);
	dict.setObject(NSCursor.arrowCursor(), OS.NSCursorAttributeName);
	widget.setSelectedTextAttributes(dict);

	scrollView = scrollWidget;
	view = widget;
}

@Override
void createWidget () {
	super.createWidget ();
	text = "";
	NSDictionary dict = ((NSTextView)view).linkTextAttributes();
	defaultLinkColor = new NSColor(dict.valueForKey(OS.NSForegroundColorAttributeName));
	offsets = new Point [0];
	ids = new String [0];
	mnemonics = new int [0];
	focusIndex = -1;
}

@Override
NSFont defaultNSFont () {
	return display.textFieldFont;
}

@Override
void deregister () {
	super.deregister ();
	if (scrollView != null) display.removeWidget (scrollView);
}

@Override
void drawBackground (long id, NSGraphicsContext context, NSRect rectangle) {
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

@Override
void drawRect(long id, long sel, NSRect rect) {
	updateThemeColors();
	super.drawRect(id, sel, rect);
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	NSTextView widget = (NSTextView) view;
	widget.setTextColor (getTextColor (enabled));
	setLinkColor (enabled);
	redrawWidget (view, false);
}

@Override
Cursor findCursor () {
	Cursor cursor = super.findCursor();
	if (cursor != null) return cursor;
	NSWindow window = view.window();
	NSTextView widget = (NSTextView) view;
	NSPoint point = view.convertPoint_fromView_(window.convertScreenToBase(NSEvent.mouseLocation()), null);
	if (widget.characterIndexForInsertionAtPoint(point) == widget.textStorage ().length ()) {
		return display.getSystemCursor (SWT.CURSOR_ARROW);
	}
	return null;
}

/**
 * Returns the link foreground color.
 *
 * @return the receiver's link foreground color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.105
 */
public Color getLinkForeground () {
	checkWidget ();
	return Color.cocoa_new (display, display.getNSColorRGB (getLinkForegroundColor ()));
}

NSColor getLinkForegroundColor () {
	if (linkForeground != null) {
		return NSColor.colorWithDeviceRed (linkForeground[0], linkForeground[1], linkForeground[2], linkForeground[3]);
	}
	return defaultLinkColor;
}

@Override
String getNameText () {
	return getText ();
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

	long rangePtr = C.malloc(NSRange.sizeof);
	NSRange lineRange = new NSRange();

	/* compute number of lines in the link */
	int numberOfLines = 0;
	long index = glyphRange.location;
	long glyphEndIndex = glyphRange.location + glyphRange.length;
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
	C.free(rangePtr);
	return result;
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

NSColor getTextColor (boolean enabled) {
	if (enabled) {
		if (foreground == null) {
			return NSColor.textColor ();
		}
		return NSColor.colorWithDeviceRed (foreground [0], foreground [1], foreground [2], foreground[3]);
	} else {
		return NSColor.disabledControlTextColor();
	}
}

@Override
void mouseUp(long id, long sel, long theEvent) {
	/*
	 * Feature in Cocoa: Link click notices are sent on mouseDown, but for some reason, Cocoa
	 * re-sends the mouseUp that follows the click on a link. Fix is to ignore the next mouseUp
	 * fired after a link selection.
	 */
	if (ignoreNextMouseUp) {
		ignoreNextMouseUp = false;
		return;
	}
	super.mouseUp(id, sel, theEvent);
}

String parse (String string) {
	int length = string.length ();
	offsets = new Point [length / 4];
	ids = new String [length / 4];
	mnemonics = new int [length / 4 + 1];
	StringBuilder result = new StringBuilder ();
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

int parseMnemonics (char[] buffer, int start, int end, StringBuilder result) {
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

@Override
void register () {
	super.register ();
	display.addWidget(scrollView, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (scrollView != null) scrollView.release();
	scrollView = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	offsets = null;
	ids = null;
	mnemonics = null;
	text = null;
	defaultLinkColor = null;
	linkForeground = null;
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

@Override
void scrollWheel(long id, long sel, long theEvent) {
	super.scrollWheel(id, sel, theEvent);
	parent.scrollWheel(parent.view.id, sel, theEvent);
}

@Override
void sendFocusEvent(int type) {
	if (focusIndex != -1) redrawWidget(view, false);
	super.sendFocusEvent(type);
}

@Override
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

@Override
boolean sendMouseEvent (NSEvent nsEvent, int type, boolean send) {
	if (type == SWT.MouseMove) {
		if (view.window().firstResponder().id != view.id) {
			mouseMoved(view.id, OS.sel_mouseMoved_, nsEvent.id);
		}
	}
	return super.sendMouseEvent(nsEvent, type, send);
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

@Override
void setBackgroundColor(NSColor nsColor) {
	setBackground(nsColor);
}

@Override
void setBackgroundImage(NSImage image) {
	((NSTextView) view).setDrawsBackground(image == null);
}

@Override
void setFont(NSFont font) {
	((NSTextView) view).setFont(font);
}

@Override
void setForeground (double [] color) {
	if (!getEnabled ()) return;
	((NSTextView) view).setTextColor (getTextColor (true));
}

void setLinkColor (boolean enabled) {
	NSTextView widget = (NSTextView) view;
	NSDictionary linkTextAttributes = widget.linkTextAttributes ();
	int count = (int)linkTextAttributes.count ();
	NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity (count);
	dict.setDictionary (linkTextAttributes);
	dict.setValue (enabled ? getLinkForegroundColor () : getTextColor (false), OS.NSForegroundColorAttributeName);
	widget.setLinkTextAttributes (dict);
}

/**
 * Sets the link foreground color to the color specified
 * by the argument, or to the default system color for the link
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.105
 */
public void setLinkForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	double [] linkForeground = color != null ? color.handle : null;
	if (equals (linkForeground, this.linkForeground)) return;
	this.linkForeground = linkForeground;
	if (getEnabled ()) {
		setLinkColor (true);
	}
	redrawWidget (view, false);
}

@Override
void setOrientation () {
	NSTextView widget = (NSTextView)view;
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	widget.setBaseWritingDirection(direction);
}

/**
 * Sets the receiver's text.
 * <p>
 * The string can contain both regular text and hyperlinks.  A hyperlink
 * is delimited by an anchor tag, &lt;a&gt; and &lt;/a&gt;.  Within an
 * anchor, a single HREF attribute is supported.  When a hyperlink is
 * selected, the text field of the selection event contains either the
 * text of the hyperlink or the value of its HREF, if one was specified.
 * In the rare case of identical hyperlinks within the same string, the
 * HREF attribute can be used to distinguish between them.  The string may
 * include the mnemonic character and line delimiters. The only delimiter
 * the HREF attribute supports is the quotation mark ("). Text containing
 * angle-bracket characters &lt; or &gt; may be escaped using \\, however
 * this operation is a hint and varies from platform to platform.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic. The receiver can have a
 * mnemonic in the text preceding each link. When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the link that follows the text. Mnemonics in links and in
 * the trailing text are ignored. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
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

@Override
void setZOrder () {
	super.setZOrder ();
	if (scrollView != null) scrollView.setDocumentView (view);
}

@Override
boolean shouldDrawInsertionPoint(long id, long sel) {
	return false;
}

@Override
boolean textView_clickOnLink_atIndex(long id, long sel, long textView, long link, long charIndex) {
	NSString str = new NSString (link);
	Event event = new Event ();
	event.text = str.getString();
	sendSelectionEvent (SWT.Selection, event, true);
	// Widget may be disposed at this point.
	if (isDisposed()) return true;
	for (int i = 0; i < offsets.length; i++) {
		if ((charIndex >= offsets[i].x) && (charIndex <= offsets[i].y)) {
			focusIndex = i;
			break;
		}
	}
	redrawWidget(view, false);
	ignoreNextMouseUp = true;
	return true;
}

@Override
NSView topView () {
	return scrollView;
}

@Override
int traversalCode (int key, NSEvent theEvent) {
	if (offsets.length == 0) return  0;
	int bits = super.traversalCode (key, theEvent);
	if (key == 48 /* Tab */ && theEvent != null) {
		long modifierFlags = theEvent.modifierFlags();
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

@Override
void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (scrollView == null) return;
	updateCursorRects (enabled, scrollView);
	NSClipView contentView = scrollView.contentView ();
	updateCursorRects (enabled, contentView);
	contentView.setDocumentCursor (enabled ? NSCursor.arrowCursor () : null);
}

void updateThemeColors() {
	/*
	 * On macOS 10.14 and 10.15, when application sets Dark appearance, NSTextView
	 * does not change the text color. In case of the link, this means that text
	 * outside <a></a> will be black-on-dark. Fix this by setting the text color
	 * explicitly. It seems that this is no longer needed on macOS 11.0. Note that
	 * there is 'setUsesAdaptiveColorMappingForDarkAppearance:' which causes
	 * NSTextView to adapt its colors, but it will also remap any colors used in
	 * .setBackground(), which makes it difficult to use. I wasn't able to find an
	 * event that colors changed, 'drawRect' seems to be the best option.
	 */

	// Avoid infinite loop of redraws
	if (lastAppAppearance == display.appAppearance) return;
	lastAppAppearance = display.appAppearance;
	// Only default colors are affected
	if (foreground != null) return;

	((NSTextView) view).setTextColor (getTextColor (getEnabled ()));
}

}

