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


import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * </p><p>
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
	int textHandle, buttonHandle;
	int textVisibleRgn, buttonVisibleRgn;
	int increment = 1;
	int pageIncrement = 10;
	int digits = 0;
	int textLimit = LIMIT;
	static int GAP = 3;
	
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

int actionProc (int theControl, int partCode) {
	int result = super.actionProc (theControl, partCode);
	if (result == OS.noErr) return result;
	if (theControl == buttonHandle) {
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (parseFail [0]) {
			value = OS.GetControl32BitValue (buttonHandle);
		}
		int newValue = value;
	    switch (partCode) {
		    case OS.kControlUpButtonPart:
				newValue += increment;
		        break;
		    case OS.kControlDownButtonPart:
				newValue -= increment;
		        break;
		}
		int max = OS.GetControl32BitMaximum (buttonHandle);
		int min = OS.GetControl32BitMinimum (buttonHandle);
		if ((style & SWT.WRAP) != 0) {
			if (newValue > max) newValue = min;
			if (newValue < min) newValue = max;
		}
		newValue = Math.min (Math.max (min, newValue), max);
		if (value != newValue) setSelection (newValue, true, true, true);
	}
	return result;
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
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
void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	int max = OS.GetControl32BitMaximum (buttonHandle);
	String string = String.valueOf (max);
	if (digits > 0) {
		StringBuffer buffer = new StringBuffer ();
		buffer.append (string);
		buffer.append (getDecimalSeparator ());
		int count = digits - string.length ();
		while (count >= 0) {
			buffer.append ("0");
			count--;
		}
		string = buffer.toString ();
	}
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	Point size = textExtent (ptr, 0);
	if (ptr != 0) OS.CFRelease (ptr);
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	int [] metric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricEditTextWhitespace, metric);
	width += metric [0] * 2;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	OS.GetThemeMetric (OS.kThemeMetricLittleArrowsHeight, metric);
	trim.height = Math.max (trim.height, metric [0]);
	return new Point (trim.width, trim.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rect inset = inset ();
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricLittleArrowsWidth, outMetric);
	width += outMetric [0] + GAP;
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
	short [] selection = new short [2];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null) != OS.noErr) return;
	if (selection [0] == selection [1]) return;
	int [] actualSize = new int [1];
	int [] ptr = new int [1];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) != OS.noErr) return;
	CFRange range = new CFRange ();
	range.location = selection [0];
	range.length = selection [1] - selection [0];
	char [] buffer= new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	copyToClipboard (buffer);
}

void createHandle () {
	int window = OS.GetControlOwner (parent.handle);
	int actionProc = display.actionProc;
	int features = OS.kControlSupportsEmbedding | OS.kControlSupportsFocus;
	int [] outControl = new int [1];
	OS.CreateUserPaneControl (window, null, features, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	
	OS.CreateLittleArrowsControl (window, null, 0, 0, 100, 1, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	buttonHandle = outControl [0];
	OS.SetControlAction (buttonHandle, actionProc);
	
	OS.CreateEditUnicodeTextControl (window, null, 0, false, null, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	textHandle = outControl [0];
	OS.SetControlData (textHandle, OS.kControlEntireControl, OS.kControlEditTextSingleLineTag, 1, new byte [] {1});
	if ((style & SWT.READ_ONLY) != 0) {
		OS.SetControlData (textHandle, OS.kControlEntireControl, OS.kControlEditTextLockedTag, 1, new byte [] {1});
	}
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
	short [] selection = new short [2];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null) != OS.noErr) return;
	if (selection [0] == selection [1]) return;
	char [] buffer = setText ("", selection [0], selection [1], true);
	if (buffer != null) {
		copyToClipboard (buffer);
	}
}

void deregister () {
	super.deregister ();
	display.removeWidget(textHandle);
	display.removeWidget(buttonHandle);
}

void drawBackground (int control, int context) {
	if (control == textHandle) {
		fillBackground (control, context, null);
	} else {
		parent.fillBackground (control, context, null);
	}
}

int focusHandle () {
	return textHandle;
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

String getDecimalSeparator () {
	int locale = OS.CFLocaleCopyCurrent ();
	int formatter = OS.CFNumberFormatterCreate (OS.kCFAllocatorDefault, locale, OS.kCFNumberFormatterDecimalStyle);
	int key = OS.kCFNumberFormatterDecimalSeparator ();
	int result = OS.CFNumberFormatterCopyProperty (formatter, key);
	CFRange range = new CFRange ();
	range.location = 0;
	range.length = OS.CFStringGetLength (result);
	char [] buffer= new char [range.length];
	OS.CFStringGetCharacters (result, range, buffer);
	OS.CFRelease (result);
	OS.CFRelease (formatter);
	OS.CFRelease (locale);
	return new String (buffer);
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
	return increment;
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
	return OS.GetControl32BitMaximum (buttonHandle);
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
	return OS.GetControl32BitMinimum (buttonHandle);
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
	return OS.GetControl32BitValue (buttonHandle);
}

int getSelectionText (boolean [] parseFail) {
	int [] actualSize = new int [1];
	int [] ptr = new int [1];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) == OS.noErr) {
		CFRange range = new CFRange ();
		range.location = 0;
		range.length = OS.CFStringGetLength (ptr [0]);
		char [] buffer= new char [range.length];
		OS.CFStringGetCharacters (ptr [0], range, buffer);
		OS.CFRelease (ptr [0]);
		String string = new String (buffer);
		try {
			int value;
			if (digits > 0) {
				String decimalSeparator = getDecimalSeparator ();
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
			int max = OS.GetControl32BitMaximum (buttonHandle);
			int min = OS.GetControl32BitMinimum (buttonHandle);
			if (min <= value && value <= max) return value;
		} catch (NumberFormatException e) {
		}
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
public String getText() {
	int [] ptr = new int [1];
	int [] actualSize = new int [1];
	int result = OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize);
	if (result != OS.noErr) return "";
	CFRange range = new CFRange ();
	range.length = OS.CFStringGetLength (ptr [0]);
	char [] buffer= new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	OS.CFRelease (ptr [0]);
	return new String (buffer);
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

int getVisibleRegion (int control, boolean clipChildren) {
	if (control == textHandle) {
		if (!clipChildren) return super.getVisibleRegion (control, clipChildren);
		if (textVisibleRgn == 0) {
			textVisibleRgn = OS.NewRgn ();
			calculateVisibleRegion (control, textVisibleRgn, clipChildren);
		}
		int result = OS.NewRgn ();
		OS.CopyRgn (textVisibleRgn, result);
		return result;
	}
	if (control == buttonHandle) {
		if (!clipChildren) return super.getVisibleRegion (control, clipChildren);
		if (buttonVisibleRgn == 0) {
			buttonVisibleRgn = OS.NewRgn ();
			calculateVisibleRegion (control, buttonVisibleRgn, clipChildren);
		}
		int result = OS.NewRgn ();
		OS.CopyRgn (buttonVisibleRgn, result);
		return result;
	}
	return super.getVisibleRegion (control, clipChildren);
}

void hookEvents () {
	super.hookEvents ();
	int controlProc = display.controlProc;
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlSetFocusPart,
		OS.kEventClassControl, OS.kEventControlTrack,
		OS.kEventClassControl, OS.kEventControlGetClickActivation,
	};
	int controlTarget = OS.GetControlEventTarget (textHandle);
	OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, handle, null);
	controlTarget = OS.GetControlEventTarget (buttonHandle);
	OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, handle, null);
}

Rect inset () {
	return display.editTextInset;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	int [] stringRef = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
	int length = 0;
	if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
	char [] buffer = new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (stringRef [0], range, buffer);
	String attributeName = new String(buffer);
	if (attributeName.equals (OS.kAXRoleAttribute) || attributeName.equals (OS.kAXRoleDescriptionAttribute)) {
		String roleText = OS.kAXGroupRole;
		buffer = new char [roleText.length ()];
		roleText.getChars (0, buffer.length, buffer, 0);
		stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (stringRef [0] != 0) {
			if (attributeName.equals (OS.kAXRoleAttribute)) {
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
			} else { // kAXRoleDescriptionAttribute
				int stringRef2 = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
				OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef2});
				OS.CFRelease(stringRef2);
			}
			OS.CFRelease(stringRef [0]);
			code = OS.noErr;
		}
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (result == OS.noErr) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		if (part [0] == OS.kControlFocusNoPart) {
			boolean [] parseFail = new boolean [1];
			int value = getSelectionText (parseFail);
			if (parseFail [0]) {
				value = OS.GetControl32BitValue (buttonHandle);
				setSelection (value, false, true, false);
			}
		}
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	int [] modifiers = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	if (modifiers [0] == OS.cmdKey) {
		switch (keyCode [0]) {
			case 7: /* X */
				cut ();
				return OS.noErr;
			case 8: /* C */
				copy ();
				return OS.noErr;
			case 9: /* V */
				paste ();
				return OS.noErr;
		}
	}
	int delta = 0;
	switch (keyCode [0]) {
		case 76: /* KP Enter */
		case 36: /* Return */
			postEvent (SWT.DefaultSelection);
			return OS.noErr;
		case 116: /* Page Up */ delta = pageIncrement; break;
		case 121: /* Page Down */ delta = -pageIncrement; break;
		case 125: /* Down */ delta = -increment; break;
		case 126: /* Up */ delta = increment; break;
	}
	if (delta != 0) {
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (parseFail [0]) {
			value = OS.GetControl32BitValue (buttonHandle);
		}
		int newValue = value + delta;
		int max = OS.GetControl32BitMaximum (buttonHandle);
		int min = OS.GetControl32BitMinimum (buttonHandle);
		if ((style & SWT.WRAP) != 0) {
			if (newValue > max) newValue = min;
			if (newValue < min) newValue = max;
		}
		newValue = Math.min (Math.max (min, newValue), max);
		if (value != newValue) setSelection (newValue, true, true, true);
		return OS.noErr;
	} else {
		result = OS.CallNextEventHandler (nextHandler, theEvent);
		boolean [] parseFail = new boolean [1];
		int value = getSelectionText (parseFail);
		if (!parseFail [0]) {
			int pos = OS.GetControl32BitValue (buttonHandle);
			if (pos != value) setSelection (value, true, false, true);
		}
	}
	return result;
}

int kEventTextInputUpdateActiveInputArea (int nextHandler, int theEvent, int userData) {
	int [] length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendText, OS.typeUnicodeText, null, 0, length, (char [])null);
	int [] fixed_length = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendFixLen, OS.typeLongInteger, null, 4, null, fixed_length);
	if (fixed_length [0] == -1 || fixed_length [0] == length [0]) {
		postEvent (SWT.Modify);
	}
	return OS.eventNotHandledErr;
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
	String text = getClipboardText ();
	short [] selection = new short [2];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null) != OS.noErr) return;
	setText (text, selection [0], selection [1], true);
}

boolean pollTrackEvent() {
	return true;
}

public void redraw () {
	checkWidget();
	super.redraw ();
	redrawWidget (textHandle, false);
	redrawWidget (buttonHandle, false);
}

void register () {
	super.register ();
	display.addWidget (textHandle, this);
	display.addWidget (buttonHandle, this);
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

void resetVisibleRegion (int control) {
	if (textVisibleRgn != 0) {
		OS.DisposeRgn (textVisibleRgn);
		textVisibleRgn = 0;
	}
	if (buttonVisibleRgn != 0) {
		OS.DisposeRgn (buttonVisibleRgn);
		buttonVisibleRgn = 0;
	}
	super.resetVisibleRegion (control);
}

void resizeClientArea () {
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricLittleArrowsWidth, outMetric);
	int buttonWidth = outMetric [0] + GAP;
	OS.GetThemeMetric (OS.kThemeMetricLittleArrowsHeight, outMetric);
	int buttonHeight = outMetric [0];	
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	Rect inset = inset ();
	int width = Math.max (0, rect.right - rect.left - inset.left - inset.right - buttonWidth);
	int height = Math.max (0, rect.bottom - rect.top - inset.top - inset.bottom);
	buttonHeight = Math.min (buttonHeight, rect.bottom - rect.top);
	setBounds (textHandle, inset.left, inset.top, width, height, true, true, false);
	setBounds (buttonHandle, inset.left + inset.right + width + GAP, inset.top + (height - buttonHeight) / 2, buttonWidth, buttonHeight, true, true, false);
}

boolean sendKeyEvent (int type, Event event) {
	if (!super.sendKeyEvent (type, event)) {
		return false;
	}
	if (type != SWT.KeyDown) return true;
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (event.character == 0) return true;
	if ((event.stateMask & SWT.COMMAND) != 0) return true;
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return true;
	String oldText = "", newText = "";
	int [] actualSize = new int [1];
	int [] ptr = new int [1];
	int charCount = 0;
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) == OS.noErr) {
		charCount = OS.CFStringGetLength (ptr [0]);
		OS.CFRelease (ptr [0]);
	} 
	short [] selection = new short [2];
	OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection, null);
	int start = selection [0], end = selection [1];
	switch (event.character) {
		case SWT.BS:
			if (start == end) {
				if (start == 0) return true;
				start = Math.max (0, start - 1);
			}
			break;
		case SWT.DEL:
			if (start == end) {
				if (start == charCount) return true;
				end = Math.min (end + 1, charCount);
			}
			break;
		case SWT.CR:
			return true;
		default:
			if (event.character != '\t' && event.character < 0x20) return true;
			oldText = new String (new char [] {event.character});
	}
	newText = verifyText (oldText, start, end, event);
	if (newText == null) return false;
	if (charCount - (end - start) + newText.length () > textLimit) {
		return false;
	}
	if (newText != oldText) {
		setText (newText, start, end, false);
		start += newText.length ();
		selection = new short [] {(short)start, (short)start};
		OS.SetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
	}
	/*
	* Post the modify event so that the character will be inserted
	* into the widget when the modify event is delivered.  Normally,
	* modify events are sent but it is safe to post the event here
	* because this method is called from the event loop.
	*/
	postEvent (SWT.Modify);
	return newText == oldText;	
}

void setBackground (float [] color) {
	super.setBackground (color);
	setBackground (textHandle, color);
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
	int pos = OS.GetControl32BitValue (buttonHandle);	
	setSelection (pos, false, true, false);
}

void setFontStyle (Font font) {
	super.setFontStyle (font);
	setFontStyle (textHandle, font);
}

void setForeground (float [] color) {
	super.setForeground (color);
	setForeground (textHandle, color);
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
	increment = value;
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is not greater than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int min = OS.GetControl32BitMinimum (buttonHandle);
	if (value < min) return;
	int pos = OS.GetControl32BitValue (buttonHandle);
	OS.SetControl32BitMaximum (buttonHandle, value);
	if (pos > value) setSelection (value, true, true, false);	
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int max = OS.GetControl32BitMaximum (buttonHandle);
	if (value > max) return;
	int pos = OS.GetControl32BitValue (buttonHandle);
	OS.SetControl32BitMinimum (buttonHandle, value);
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
	int min = OS.GetControl32BitMinimum (buttonHandle);
	int max = OS.GetControl32BitMaximum (buttonHandle);
	value = Math.min (Math.max (min, value), max);
	setSelection (value, true, true, false);
}

void setSelection (int value, boolean setPos, boolean setText, boolean notify) {
	if (setPos) {
		OS.SetControl32BitValue (buttonHandle, value);
	}
	if (setText) {
		String string;
		if (digits == 0) {
			string = String.valueOf (value);
		} else {
			string = String.valueOf (Math.abs (value));
			String decimalSeparator = getDecimalSeparator ();
			int index = string.length () - digits;
			StringBuffer buffer = new StringBuffer ();
			if (value < 0) buffer.append ("-");
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
		if (hooks (SWT.Verify) || filters (SWT.Verify)) {
			int [] actualSize = new int [1];
			int [] ptr = new int [1];
			int length = 0;
			if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) == OS.noErr) {
				length = OS.CFStringGetLength (ptr [0]);
				OS.CFRelease (ptr [0]);
			}
			string = verifyText (string, 0, length, null);
			if (string == null) return;
		}
		char [] buffer = new char [string.length ()];
		string.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetControlData (textHandle, OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, new int[] {ptr});
		OS.CFRelease (ptr);
		short [] selection = new short [] {0, (short)string.length ()};
		OS.SetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextSelectionTag, 4, selection);
		sendEvent (SWT.Modify);
	}
	if (notify) postEvent (SWT.Selection);
}

char [] setText (String string, int start, int end, boolean notify) {
	if (notify) {
		if (hooks (SWT.Verify) || filters (SWT.Verify)) {
			string = verifyText (string, start, end, null);
			if (string == null) return null;
		}
	}
	int [] actualSize = new int [1];
	int [] ptr = new int [1];
	if (OS.GetControlData (textHandle, (short)OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr, actualSize) != OS.noErr) return null;
	int charCount = OS.CFStringGetLength (ptr [0]);
	int length = string.length ();
	if (textLimit != LIMIT) {
		if (charCount - (end - start) + length > textLimit) {
			length = textLimit - charCount + (end - start);
		}
	}
	char [] text = new char [charCount - (end - start) + length];
	CFRange range = new CFRange ();
	range.location = 0;
	range.length = start;
	char [] buffer = new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	System.arraycopy (buffer, 0, text, 0, range.length);
	string.getChars (0, length, text, start);
	range.location = end;
	range.length = charCount - end;
	buffer = new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);
	System.arraycopy (buffer, 0, text, start + length, range.length);
	
	/* Copying the return value to buffer */
	range.location = start;
	range.length = end - start;
	buffer = new char [range.length];
	OS.CFStringGetCharacters (ptr [0], range, buffer);	
	OS.CFRelease (ptr [0]);
	
	ptr [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, text, text.length);
	if (ptr [0] == 0) error (SWT.ERROR_CANNOT_SET_TEXT);			
	OS.SetControlData (textHandle, OS.kControlEntireControl, OS.kControlEditTextCFStringTag, 4, ptr);
	OS.CFRelease (ptr [0]);
	if (notify) sendEvent (SWT.Modify);
	return buffer;
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
	this.increment = increment;
	this.pageIncrement = pageIncrement;
	this.digits = digits;
	OS.SetControl32BitMaximum (buttonHandle, maximum);
	OS.SetControl32BitMinimum (buttonHandle, minimum);
	setSelection (selection, true, true, false);
}

void setZOrder () {
	super.setZOrder ();
	if (textHandle != 0) OS.HIViewAddSubview (handle, textHandle);
	if (buttonHandle != 0) OS.HIViewAddSubview (handle, buttonHandle);
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
	int index = 0;
	if (digits > 0) {
		String decimalSeparator = getDecimalSeparator ();
		index = string.indexOf (decimalSeparator);
		if (index != -1) {
			string = string.substring (0, index) + string.substring (index + 1);
		}
		index = 0;
	}
	if (string.length() > 0) {
		int minimum = OS.GetControl32BitMinimum (buttonHandle);
		if (minimum < 0 && string.charAt (0) == '-') index++;
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
