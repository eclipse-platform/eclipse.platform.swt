/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#spinner">Spinner snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Spinner extends Composite {
	NSTextField textView;
	NSNumberFormatter textFormatter;
	NSStepper buttonView;
	int pageIncrement = 10;
	int digits = 0;
	int textLimit = LIMIT;
	static int GAP = 0;

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 *
	 * @since 3.4
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
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Spinner (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
boolean acceptsFirstResponder(long id, long sel) {
	if (id == view.id) return false;
	return super.acceptsFirstResponder (id, sel);
}

@Override
boolean accessibilityIsIgnored(long id, long sel) {
	if (id == view.id) return true;
	return super.accessibilityIsIgnored(id, sel);
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
	addTypedListener(listener, SWT.Modify);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
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
void addVerifyListener (VerifyListener listener) {
	addTypedListener(listener, SWT.Verify);
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	double width = 0, height = 0;
	String string = Double.toString (buttonView.maxValue ());
	Font font = Font.cocoa_new(display, textView.font ());
	NSAttributedString str = parent.createString(string, font, null, 0, false, true, false);
	NSSize size = str.size ();
	str.release ();
	width = (float)size.width;
	height = (float)size.height;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, (int)Math.ceil (width), (int)Math.ceil (height));
	if (hHint == SWT.DEFAULT) {
		size = buttonView.cell ().cellSize ();
		trim.height = Math.max (trim.height, (int)size.height);
	}
	return new Point (trim.width, trim.height);
}
@Override
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	NSRect frameRect = textView.frame();
	NSCell cell = new NSCell (textView.cell ());
	NSRect cellRect = cell.drawingRectForBounds(frameRect);
	width += frameRect.width - cellRect.width;
	height += frameRect.height - cellRect.height;
	width += GAP;
	NSSize size = buttonView.cell ().cellSize ();
	width += (int)size.width;
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
	checkWidget ();
	NSText fieldEditor = textView.currentEditor();
	if (fieldEditor != null) {
		fieldEditor.copy(null);
	} else {
		//TODO
	}
}

@Override
void createHandle () {
	NSView widget = (NSView)new SWTView().alloc();
	widget.init();
//	widget.setDrawsBackground(false);
	NSStepper buttonWidget = (NSStepper)new SWTStepper().alloc();
	buttonWidget.init();
	buttonWidget.setValueWraps((style & SWT.WRAP) != 0);
	buttonWidget.setTarget(buttonWidget);
	buttonWidget.setAction(OS.sel_sendSelection);
	buttonWidget.setMaxValue(100);
	NSTextField textWidget = (NSTextField)new SWTTextField().alloc();
	textWidget.init();
//	textWidget.setTarget(widget);
	textWidget.setEditable((style & SWT.READ_ONLY) == 0);
	if ((style & SWT.BORDER) == 0) {
		textWidget.setFocusRingType (OS.NSFocusRingTypeNone);
		textWidget.setBordered (false);
	}
	textFormatter = (NSNumberFormatter)new NSNumberFormatter().alloc();
	textFormatter.init();
	widget.addSubview(textWidget);
	widget.addSubview(buttonWidget);
	buttonView = buttonWidget;
	textView = textWidget;
	view = widget;
	setSelection (0, false, true, false);
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
	NSText fieldEditor = textView.currentEditor();
	if (fieldEditor != null) {
		fieldEditor.cut(null);
	} else {
		//TODO
	}
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget(enabled);
	buttonView.setEnabled(enabled);
	textView.setEnabled(enabled);
}

@Override
NSFont defaultNSFont () {
	return display.textFieldFont;
}

@Override
void deregister () {
	super.deregister ();
	if (textView != null) {
		display.removeWidget (textView);
		display.removeWidget (textView.cell());
	}

	if (buttonView != null) {
		display.removeWidget (buttonView);
		display.removeWidget (buttonView.cell());
	}
}

@Override
void drawBackground (long id, NSGraphicsContext context, NSRect rect) {
	if (backgroundImage == null) return;
	if (new NSView(id).isKindOfClass(OS.class_NSText)) {
		NSText text = new NSText(id);
		if (!text.isFieldEditor()) return;
	}
	fillBackground (view, context, rect, -1);
}

@Override
void drawInteriorWithFrame_inView(long id, long sel, NSRect cellFrame, long viewid) {
	Control control = findBackgroundControl();
	if (control == null) control = this;
	Image image = control.backgroundImage;
	if (image != null && !image.isDisposed()) {
		NSGraphicsContext context = NSGraphicsContext.currentContext();
		control.fillBackground (view, context, cellFrame, -1);
	}
	super.drawInteriorWithFrame_inView(id, sel, cellFrame, viewid);
}

@Override
Cursor findCursor () {
	Cursor cursor = super.findCursor ();
	if (cursor == null && (style & SWT.READ_ONLY) == 0 && OS.VERSION < OS.VERSION(10, 14, 0)) {
		cursor = display.getSystemCursor (SWT.CURSOR_IBEAM);
	}
	return cursor;
}

@Override
NSView focusView () {
	return textView;
}

/**
 * Returns the number of decimal places used by the receiver.
 *
 * @return the digits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDigits () {
	checkWidget ();
	return digits;
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed.
 *
 * @return the increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIncrement () {
	checkWidget ();
	return (int)buttonView.increment();
}

/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	return (int)buttonView.maxValue();
}

/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	return (int)buttonView.minValue();
}

/**
 * Returns the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed.
 *
 * @return the page increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getPageIncrement () {
	checkWidget ();
	return pageIncrement;
}

/**
 * Returns the <em>selection</em>, which is the receiver's position.
 *
 * @return the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();
	return (int)((NSStepper)buttonView).doubleValue();
}

int getSelectionText (boolean[] parseFail) {
	String string = textView.stringValue().getString();
	try {
		int value;
		if (digits > 0) {
			String decimalSeparator = textFormatter.decimalSeparator().getString();
			int index = string.indexOf (decimalSeparator);
			if (index != -1)  {
				int startIndex = string.startsWith ("+") || string.startsWith ("-") ? 1 : 0;
				String wholePart = startIndex != index ? string.substring (startIndex, index) : "0";
				String decimalPart = string.substring (index + 1);
				if (decimalPart.length () > digits) {
					decimalPart = decimalPart.substring (0, digits);
				} else {
					int i = digits - decimalPart.length ();
					for (int j = 0; j < i; j++) {
						decimalPart = decimalPart + "0";
					}
				}
				int wholeValue = Integer.parseInt (wholePart);
				int decimalValue = Integer.parseInt (decimalPart);
				for (int i = 0; i < digits; i++) wholeValue *= 10;
				value = wholeValue + decimalValue;
				if (string.startsWith ("-")) value = -value;
			} else {
				value = Integer.parseInt (string);
				for (int i = 0; i < digits; i++) value *= 10;
			}
		} else {
			value = Integer.parseInt (string);
		}
		int max = getMaximum();
		int min = getMinimum();
		if (min <= value && value <= max) return value;
	} catch (NumberFormatException e) {
	}
	parseFail [0] = true;
	return -1;
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
 *
 * @since 3.4
 */
public String getText () {
	checkWidget ();
	NSString str = new NSTextFieldCell (textView.cell ()).title ();
	return str.getString ();
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Spinner.LIMIT</code>.
 *
 * @return the text limit
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 *
 * @since 3.4
 */
public int getTextLimit () {
	checkWidget();
	return textLimit;
}

@Override
boolean handleIsAccessible(long id) {
	// All subviews of a Spinner can have their accessible properties overridden.
	// The top-level NSView is already ignored, so we don't need to test for that.
	return true;
}

@Override
boolean isEventView (long id) {
	return true;
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
	NSText fieldEditor = textView.currentEditor();
	if (fieldEditor != null) {
		fieldEditor.paste(null);
	} else {
		//TODO
	}
}

@Override
void register () {
	super.register ();
	if (textView != null) {
		display.addWidget (textView, this);
		display.addWidget (textView.cell(), this);
	}

	if (buttonView != null) {
		display.addWidget (buttonView, this);
		display.addWidget (buttonView.cell(), this);
	}
}

@Override
void releaseHandle () {
	super.releaseHandle();
	if (textFormatter != null) textFormatter.release();
	if (buttonView != null) buttonView.release();
	if (textView != null) textView.release();
	textFormatter = null;
	buttonView = null;
	textView = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (textView != null) textView.abortEditing();
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
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

@Override
void resized () {
	super.resized ();
	buttonView.sizeToFit();
	NSSize textSize = textView.cell ().cellSize ();
	NSRect buttonFrame = buttonView.bounds();
	NSRect frame = view.frame();
	buttonFrame.x = frame.width - buttonFrame.width;
	buttonFrame.y = (frame.height - buttonFrame.height) / 2;
	int textHeight = (int)Math.min(textSize.height, frame.height);
	frame.x = 0;
	frame.y = (frame.height - textHeight) / 2;
	frame.width -= buttonFrame.width + GAP;
	frame.height = textHeight;
	textView.setFrame(frame);
	buttonView.setFrame(buttonFrame);
}

@Override
boolean sendKeyEvent (NSEvent nsEvent, int type) {
	boolean result = super.sendKeyEvent (nsEvent, type);
	if (!result) return result;
	if (type != SWT.KeyDown) return result;
	int delta = 0;
	short keyCode = nsEvent.keyCode ();
	switch (keyCode) {
		case 76: /* KP Enter */
		case 36: { /* Return */
			sendSelectionEvent (SWT.DefaultSelection);
			return true;
		}

		case 116: delta = pageIncrement; break; /* Page Up */
		case 121: delta = -pageIncrement; break; /* Page Down */
		case 125: delta = -getIncrement(); break; /* Down arrow */
		case 126: delta = getIncrement(); break; /* Up arrow */
	}

	if (delta != 0) {
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (parseFail [0]) {
			value = (int)buttonView.doubleValue();
		}
		int newValue = value + delta;
		int max = (int)buttonView.maxValue();
		int min = (int)buttonView.minValue();
		if ((style & SWT.WRAP) != 0) {
			if (newValue > max) newValue = min;
			if (newValue < min) newValue = max;
		}
		newValue = Math.min (Math.max (min, newValue), max);
		if (value != newValue) setSelection (newValue, true, true, true);
		// Prevent the arrow or page up/down from being handled by the text field.
		result = false;
	} else {
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (!parseFail [0]) {
			int pos = (int)buttonView.doubleValue();
			if (pos != value) setSelection (value, true, false, true);
		}
	}

	return result;
}

@Override
void sendSelection () {
	setSelection (getSelection(), false, true, true);
}

@Override
void setBackgroundColor(NSColor nsColor) {
	((NSTextField) textView).setBackgroundColor (nsColor);
}

@Override
void setBackgroundImage(NSImage image) {
	NSTextField widget = (NSTextField) textView;
	widget.setDrawsBackground(image == null);
	NSText editor = widget.window().fieldEditor(true, widget);
	editor.setDrawsBackground(image == null);
}
/**
 * Sets the number of decimal places used by the receiver.
 * <p>
 * The digit setting is used to allow for floating point values in the receiver.
 * For example, to set the selection to a floating point value of 1.37 call setDigits() with
 * a value of 2 and setSelection() with a value of 137. Similarly, if getDigits() has a value
 * of 2 and getSelection() returns 137 this should be interpreted as 1.37. This applies to all
 * numeric APIs.
 * </p>
 *
 * @param value the new digits (must be greater than or equal to zero)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the value is less than zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDigits (int value) {
	checkWidget ();
	if (value < 0) error (SWT.ERROR_INVALID_ARGUMENT);
	if (value == digits) return;
	digits = value;
	int pos = (int)buttonView.doubleValue();
	setSelection (pos, false, true, false);
}

@Override
void setFont(NSFont font) {
	textView.setFont(font);
}

@Override
void setForeground (double [] color) {
	NSColor nsColor;
	if (color == null) {
		nsColor = NSColor.textColor ();
	} else {
		nsColor = NSColor.colorWithDeviceRed (color [0], color [1], color [2], 1);
	}
	((NSTextField) textView).setTextColor (nsColor);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed to
 * the argument, which must be at least one.
 *
 * @param value the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	buttonView.setIncrement(value);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is less than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than or equal to the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int min = getMinimum ();
	if (value < min) return;
	int pos = getSelection();
	buttonView.setMaxValue(value);
	if (pos > value) setSelection (value, true, true, false);
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is greater than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be less than or equal to the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int max = getMaximum();
	if (value > max) return;
	int pos = getSelection();
	buttonView.setMinValue(value);
	if (pos < value) setSelection (value, true, true, false);
}

/**
 * Sets the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed
 * to the argument, which must be at least one.
 *
 * @param value the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	pageIncrement = value;
}

@Override
void setOrientation () {
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	textView.setBaseWritingDirection(direction);
}

/**
 * Sets the <em>selection</em>, which is the receiver's
 * position, to the argument. If the argument is not within
 * the range specified by minimum and maximum, it will be
 * adjusted to fall within this range.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	int min = getMinimum();
	int max = getMaximum();
	value = Math.min (Math.max (min, value), max);
	setSelection (value, true, true, false);
}

void setSelection (int value, boolean setPos, boolean setText, boolean notify) {
	if (setPos) {
		((NSStepper)buttonView).setDoubleValue(value);
	}
	if (setText) {
		String string = String.valueOf (value);
		if (digits > 0) {
			String decimalSeparator = textFormatter.decimalSeparator().getString();
			int index = string.length () - digits;
			StringBuilder buffer = new StringBuilder ();
			if (index > 0) {
				buffer.append (string.substring (0, index));
				buffer.append (decimalSeparator);
				buffer.append (string.substring (index));
			} else {
				buffer.append ("0");
				buffer.append (decimalSeparator);
				while (index++ < 0) buffer.append ("0");
				buffer.append (string);
			}
			string = buffer.toString ();
		}
		NSCell cell = new NSCell(textView.cell());
		if (hooks (SWT.Verify) || filters (SWT.Verify)) {
			int length = (int)cell.title().length();
			string = verifyText (string, 0, length, null);
			if (string == null) return;
		}
		textView.setStringValue(NSString.stringWith(string));
		NSRange selection = new NSRange();
		selection.location = 0;
		selection.length = string.length();
		NSText fieldEditor = textView.currentEditor();
		if (fieldEditor != null) fieldEditor.setSelectedRange(selection);
		sendEvent (SWT.Modify);
	}
	if (notify) sendSelectionEvent (SWT.Selection);
}

@Override
void setSmallSize () {
	textView.cell ().setControlSize (OS.NSSmallControlSize);
	buttonView.cell ().setControlSize (OS.NSSmallControlSize);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Spinner.LIMIT)</code>.
 * Specifying a limit value larger than <code>Spinner.LIMIT</code> sets the
 * receiver's limit to <code>Spinner.LIMIT</code>.
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
 *
 * @since 3.4
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	textLimit = limit;
}

/**
 * Sets the receiver's selection, minimum value, maximum
 * value, digits, increment and page increment all at once.
 * <p>
 * Note: This is similar to setting the values individually
 * using the appropriate methods, but may be implemented in a
 * more efficient fashion on some platforms.
 * </p>
 *
 * @param selection the new selection value
 * @param minimum the new minimum value
 * @param maximum the new maximum value
 * @param digits the new digits value
 * @param increment the new increment value
 * @param pageIncrement the new pageIncrement value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setValues (int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
	checkWidget ();
	if (maximum < minimum) return;
	if (digits < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	selection = Math.min (Math.max (minimum, selection), maximum);
	this.pageIncrement = pageIncrement;
	this.digits = digits;
	buttonView.setIncrement(increment);
	buttonView.setMaxValue(maximum);
	buttonView.setMinValue(minimum);
	setSelection (selection, true, true, false);
}

@Override
boolean shouldChangeTextInRange_replacementString(long id, long sel, long affectedCharRange, long replacementString) {
	NSRange range = new NSRange();
	OS.memmove(range, affectedCharRange, NSRange.sizeof);
	boolean result = callSuperBoolean(id, sel, range, replacementString);
	if (hooks (SWT.Verify)) {
		String text = new NSString(replacementString).getString();
		NSEvent currentEvent = display.application.currentEvent();
		long type = currentEvent.type();
		if (type != OS.NSKeyDown && type != OS.NSKeyUp) currentEvent = null;
		String newText = verifyText(text, (int)range.location, (int)(range.location+range.length), currentEvent);
		if (newText == null) return false;
		if (text != newText) {
			int length = newText.length();
			NSText fieldEditor = textView.currentEditor ();
			if (fieldEditor != null) {
				NSRange selectedRange = fieldEditor.selectedRange();
				if (textLimit != LIMIT) {
					long charCount = fieldEditor.string().length();
					if (charCount - selectedRange.length + length > textLimit) {
						length = (int)(textLimit - charCount + selectedRange.length);
					}
				}
				char [] buffer = new char [length];
				newText.getChars (0, buffer.length, buffer, 0);
				NSString nsstring = NSString.stringWithCharacters (buffer, buffer.length);
				fieldEditor.replaceCharactersInRange (fieldEditor.selectedRange (), nsstring);
				result = false;
			}
		}
		if (!result) sendEvent (SWT.Modify);
	}
	return result;
}

@Override
void textDidChange (long id, long sel, long aNotification) {
	super.textDidChange (id, sel, aNotification);
	boolean [] parseFail = new boolean [1];
	int value = getSelectionText (parseFail);
	if (!parseFail [0]) {
		int pos = (int)buttonView.doubleValue();
		if (value != pos) {
			setSelection (value, true, false, true);
		}
	}
	postEvent (SWT.Modify);
}

@Override
NSRange textView_willChangeSelectionFromCharacterRange_toCharacterRange (long id, long sel, long aTextView, long oldSelectedCharRange, long newSelectedCharRange) {
	/* allow the selection change to proceed */
	NSRange result = new NSRange ();
	OS.memmove(result, newSelectedCharRange, NSRange.sizeof);
	return result;
}

@Override
void textDidEndEditing(long id, long sel, long aNotification) {
	boolean [] parseFail = new boolean [1];
	int value = getSelectionText (parseFail);
	if (parseFail [0]) {
		value = (int)buttonView.doubleValue();
		setSelection (value, false, true, false);
	}
	super.textDidEndEditing(id, sel, aNotification);
}

@Override
void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	updateCursorRects (enabled, textView);
	updateCursorRects (enabled, buttonView);
}

String verifyText (String string, int start, int end, NSEvent keyEvent) {
	Event event = new Event ();
	if (keyEvent != null) setKeyState(event, SWT.MouseDown, keyEvent);
	event.text = string;
	event.start = start;
	event.end = end;
	int index = 0;
	if (digits > 0) {
		String decimalSeparator = ".";//getDecimalSeparator ();
		index = string.indexOf (decimalSeparator);
		if (index != -1) {
			string = string.substring (0, index) + string.substring (index + 1);
		}
		index = 0;
	}
	while (index < string.length ()) {
		if (!Character.isDigit (string.charAt (index))) break;
		index++;
	}
	event.doit = index == string.length ();
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
