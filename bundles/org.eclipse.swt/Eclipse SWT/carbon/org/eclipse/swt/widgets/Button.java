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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.Compatibility;
import org.eclipse.swt.internal.carbon.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE 
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends Control {
	String text = "";
	Image image;
	int cIcon;
	boolean isImage, grayed;
	
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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
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
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

void click () {
	sendSelectionEvent (SWT.Selection);
}

int callPaintEventHandler (int control, int damageRgn, int visibleRgn, int theEvent, int nextHandler) {
	int [] context = null;
	if ((style & SWT.ARROW) != 0) {
		boolean invert = false;
		if (OS.VERSION < 0x1050) {
			invert = (style & SWT.UP) != 0;
		} else {
			invert = (style & SWT.UP) != 0 || (style & SWT.LEFT) != 0;
		}
		if (invert) {
			context = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, context);
			OS.CGContextSaveGState (context[0]);
			CGRect rect = new CGRect();
			OS.HIViewGetBounds (handle, rect);
			OS.CGContextRotateCTM (context[0], (float)Compatibility.PI);
			OS.CGContextTranslateCTM (context[0], -rect.width, -rect.height);
		}
	}
	int result = super.callPaintEventHandler (control, damageRgn, visibleRgn, theEvent, nextHandler);
	if (context != null) OS.CGContextRestoreGState (context[0]);
	return result;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	// NEEDS WORK - empty string
	if ((style & SWT.ARROW) != 0) {
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricDisclosureTriangleHeight, outMetric);
		int width = outMetric [0], height = outMetric [0];
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point (width, height);
	}

	int width = 0, height = 0;

	if (isImage && image != null) {
		Rectangle bounds = image.getBounds ();
		width = bounds.width;
		height = bounds.height;
	}
	int [] ptr = new int [1];
	OS.CopyControlTitleAsCFString (handle, ptr);
	if (ptr [0] != 0) {
		Point size = textExtent (ptr [0], 0);
		width += size.x;
		height = Math.max (height, size.y);
		OS.CFRelease (ptr [0]);
		if (image != null && isImage) width += 3;
	} else {
		if (image == null) {
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
		}
	}

	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		int [] outMetric = new int [1];
		int metric = ((style & SWT.CHECK) != 0) ? OS.kThemeMetricCheckBoxWidth : OS.kThemeMetricRadioButtonWidth;
		OS.GetThemeMetric (metric, outMetric);	
 		width += outMetric [0] + 3; // +3 for gap between button and text/image
		height = Math.max (outMetric [0], height);
	} else {
		if ((style & SWT.FLAT) != 0 || (style & SWT.TOGGLE) != 0) {
			width += 8;
			height += 8;
		} else {
			width += 28;
			int [] outMetric = new int [1];
			OS.GetThemeMetric (OS.kThemeMetricPushButtonHeight, outMetric);
			height = Math.max (height, outMetric [0]);
		}
	}
	
	Rect inset = getInset ();
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	
	/*
	 * Feature in Mac OS X. Setting the width of a bevel button
	 * widget to less than 20 will fail.  This means you can not 
	 * make a button very small.  By forcing the width to be greater
	 * than or equal to 20, the height of the button can be made
	 * very small, even 0.
	 */
	width = Math.max(20, width);
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	if ((style & SWT.PUSH) == 0) state |= THEME_BACKGROUND;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
				
	if ((style & SWT.ARROW) != 0) {
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.DOWN) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.LEFT) != 0) orientation = OS.kThemeDisclosureLeft;
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)(OS.kThemeDisclosureButton)});
		OS.SetControl32BitMaximum (handle, 2);
		OS.SetControl32BitValue (handle, orientation);
	}
	
	if ((style & SWT.CHECK) != 0) {
		//OS.CreateCheckBoxControl (window, null, 0, 0 /*initially off*/, true, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeCheckBox});
	}
	
	if ((style & SWT.RADIO) != 0) {
		//OS.CreateRadioButtonControl(window, null, 0, 0 /*initially off*/, true, outControl);
		OS.CreateBevelButtonControl(window, null, 0, (short)0, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeRadioButton});
	}
	
	if ((style & SWT.TOGGLE) != 0) {
		OS.CreateBevelButtonControl(window, null, 0, (short)OS.kControlBevelButtonNormalBevel, (short)OS.kControlBehaviorToggles, 0, (short)0, (short)0, (short)0, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		if ((style & SWT.FLAT) == 0 ) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemeRoundedBevelButton});
		}
	}
	
	if ((style & SWT.PUSH) != 0) {
		if ((style & SWT.FLAT) != 0) {
			OS.CreateBevelButtonControl(window, null, 0, (short)2, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		} else {
			OS.CreatePushButtonControl (window, null, 0, outControl);
			//OS.CreateBevelButtonControl(window, null, 0, (short)2, (short)OS.kControlBehaviorPushbutton, 0, (short)0, (short)0, (short)0, outControl);
		}
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
		if ((style & SWT.FLAT) == 0 ) {
			OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonKindTag, 2, new short [] {(short)OS.kThemePushButton});
		}
	}

	ControlFontStyleRec fontRec = new ControlFontStyleRec();
	fontRec.flags = (short) OS.kControlUseThemeFontIDMask;
	fontRec.font = (short) defaultThemeFont ();
	OS.SetControlFontStyle (handle, fontRec);
	
	if ((style & SWT.ARROW) != 0) return;
	_setAlignment (style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER));
}

int defaultThemeFont () {
	if (display.smallFonts) return OS.kThemeSmallSystemFont;
	return OS.kThemePushButtonFont;
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if (OS.VERSION < 0x1040 && isImage && image != null && (style & SWT.PUSH) != 0 && (style & SWT.FLAT) == 0) {
		Rect bounds = new Rect(), content = new Rect();
		OS.GetControlBounds (handle, bounds);
		ThemeButtonDrawInfo drawInfo = new ThemeButtonDrawInfo();
		if (OS.IsControlEnabled (handle)) {
			drawInfo.state = OS.IsControlActive (handle) ? OS.kThemeStateActive : OS.kThemeStateInactive;
		} else {
			drawInfo.state = OS.IsControlActive (handle) ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
		}
		drawInfo.adornment = OS.kThemeAdornmentDefault;
		OS.GetThemeButtonContentBounds (bounds, OS.kThemePushButton, drawInfo, content);
		int width = image == null ? 0 : OS.CGImageGetWidth (image.handle);
		int height = image == null ? 0 : OS.CGImageGetHeight (image.handle);
		int x = (bounds.right - bounds.left - width) / 2;
		int y = (content.bottom - content.top - height) / 2;
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		GC gc = GC.carbon_new (this, data);
		gc.drawImage (image, x, y);
		gc.dispose ();
	}
	super.drawWidget (control, context, damageRgn, visibleRgn, theEvent);
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the widget does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean getGrayed () {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}
String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
    return OS.GetControl32BitValue(handle) != 0;
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * an <code>ARROW</code> button.
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

Rect getInset () {
	if ((style & SWT.PUSH) == 0) return super.getInset();
	return display.buttonInset;
}

boolean isDescribedByLabel () {
	return false;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	if ((style & SWT.RADIO) != 0) {
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
			String roleText = OS.kAXRadioButtonRole;
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
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	} else {
		if ((style & SWT.CHECK) != 0) {
			if (grayed) {
				switch (OS.GetControl32BitValue (handle)) {
					case 0: 
						OS.SetControl32BitMaximum (handle, 2);
						OS.SetControl32BitValue (handle, 2);
						break;
					case 1:
					case 2:
						OS.SetControl32BitMaximum (handle, 0);
						OS.SetControl32BitValue (handle, 0);
						break;
				}
			}
		}
	}
	sendSelectionEvent (SWT.Selection);
	return OS.eventNotHandledErr;
}

void releaseWidget () {
	super.releaseWidget ();
	if (cIcon != 0) {
		destroyCIcon (cIcon);
		cIcon = 0;
	}
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

void selectRadio () {
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	_setAlignment (alignment);
	redraw ();
}
	
void _setAlignment (int alignment) {
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.DOWN) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.LEFT) != 0) orientation = OS.kThemeDisclosureLeft;
		OS.SetControl32BitValue (handle, orientation);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	/* Alignment not honoured when image and text is visible */
	boolean bothVisible = text != null && text.length () > 0 && image != null;
	if (bothVisible) {
		if ((style & (SWT.RADIO | SWT.CHECK)) != 0) alignment = SWT.LEFT;
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) alignment = SWT.CENTER;
	}
	int textAlignment = 0;
	int graphicAlignment = 0;
	if ((alignment & SWT.LEFT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
		graphicAlignment = OS.kControlBevelButtonAlignLeft;
	}
	if ((alignment & SWT.CENTER) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextCenter;
		graphicAlignment = OS.kControlBevelButtonAlignCenter;
	}
	if ((alignment & SWT.RIGHT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
		graphicAlignment = OS.kControlBevelButtonAlignRight;
	}
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	if (bothVisible) {
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextPlaceTag, 2, new short [] {(short)OS.kControlBevelButtonPlaceToRightOfGraphic});
	}
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	/* 
	* Bug in MacOS X. When setting the height of a bevel button 
	* to a value less than 20, the button is drawn incorrectly.
	* The fix is to force the height to be greater than or equal to 20.
	*/
	if (resize && (style & SWT.ARROW) == 0) {
		height = Math.max (20, height);
	}
	return super.setBounds (x, y, width, height, move, resize, events);
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	int window = OS.GetControlOwner (handle);
	OS.SetWindowDefaultButton (window, value ? handle : 0);
}

/**
 * Sets the grayed state of the receiver.  This state change 
 * only applies if the control was created with the SWT.CHECK
 * style.
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setGrayed (boolean grayed) {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	if (grayed) {
		if (OS.GetControl32BitValue (handle) != 0) {
			OS.SetControl32BitMaximum (handle, 2);
			OS.SetControl32BitValue (handle, 2);
		} else {
			OS.SetControl32BitMaximum (handle, 0);
			OS.SetControl32BitValue (handle, 0);
		}
	} else {
		if (OS.GetControl32BitValue (handle) != 0) {
			OS.SetControl32BitValue (handle, 1);
		}
		OS.SetControl32BitMaximum (handle, 1);
	}
}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 * <p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p>
 * @param image the image to display on the receiver (may be <code>null</code>)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return;
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (cIcon != 0) {
		destroyCIcon(cIcon);
		cIcon = 0;
	}
	this.image = image;
	isImage = true;
	if (OS.VERSION < 0x1040) {
		if ((style & SWT.PUSH) != 0 && (style & SWT.FLAT) == 0) {			
			if (image == null) {
				setText (text);
				return;
			}
			if (text.length () > 0) {
				int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, null, 0);
				if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
				OS.SetControlTitleWithCFString (handle, ptr);
				OS.CFRelease (ptr);
			}
		}
	}
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	if (image != null) {
		if (OS.VERSION < 0x1040) {
			cIcon = createCIcon (image);
			inContent.contentType = (short)OS.kControlContentCIconHandle;
			inContent.iconRef = cIcon;
		} else {
			inContent.contentType = (short)OS.kControlContentCGImageRef;
			inContent.iconRef = image.handle;
		}
	} else {
		inContent.contentType = (short)OS.kControlContentTextOnly;
	}
	OS.SetBevelButtonContentInfo (handle, inContent);
	_setAlignment (style);
	redraw ();
}

boolean setRadioSelection (boolean value){
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>, 
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	if ((style & SWT.CHECK) != 0) {
		if (grayed) {
			if (selected) {
				OS.SetControl32BitMaximum (handle, 2);
				OS.SetControl32BitValue (handle, 2);
			} else {
				OS.SetControl32BitMaximum (handle, 0);
				OS.SetControl32BitValue (handle, 0);
			}
			return;
		}
		OS.SetControl32BitMaximum (handle, 1);
	}
	OS.SetControl32BitValue (handle, selected ? 1 : 0);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasized in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p>
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	if (OS.VERSION < 0x1040) {
		if ((style & SWT.PUSH) != 0 && (style & SWT.FLAT) == 0) {
			if (isImage) {
				ControlButtonContentInfo inContent = new ControlButtonContentInfo();
				inContent.contentType = (short)OS.kControlContentTextOnly;
				OS.SetBevelButtonContentInfo(handle, inContent);
			}
			isImage = false;
		}
	}
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
	_setAlignment (style);
	redraw ();
}

int traversalCode (int key, int theEvent) {
	int code = super.traversalCode (key, theEvent);
	if ((style & SWT.ARROW) != 0) code &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
	if ((style & SWT.RADIO) != 0) code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	return code;
}

}
