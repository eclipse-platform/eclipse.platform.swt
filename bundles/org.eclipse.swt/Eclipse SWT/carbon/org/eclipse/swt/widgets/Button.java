/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
 */
public class Button extends Control {
	String text = "";
	Image image;
	int cIcon;
	boolean isImage;
	
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
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected.
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
	postEvent (SWT.Selection);
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
	} else {
		int [] ptr = new int [1];
		OS.CopyControlTitleAsCFString (handle, ptr);
		if (ptr [0] != 0) {
			org.eclipse.swt.internal.carbon.Point ioBounds = new org.eclipse.swt.internal.carbon.Point ();
			if (font == null) {
				OS.GetThemeTextDimensions (ptr [0], (short)OS.kThemePushButtonFont, OS.kThemeStateActive, false, ioBounds, null);
			} else {
				int [] currentPort = new int [1];
				OS.GetPort (currentPort);
				OS.SetPortWindowPort (OS.GetControlOwner (handle));
				OS.TextFont (font.id);
				OS.TextFace (font.style);
				OS.TextSize (font.size);
				OS.GetThemeTextDimensions (ptr [0], (short) OS.kThemeCurrentPortFont, OS.kThemeStateActive, false, ioBounds, null);
				OS.SetPort (currentPort [0]);
			}
			width = ioBounds.h;
			height = ioBounds.v;
			OS.CFRelease (ptr [0]);
		} else {
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
			width += 10;
			height += 10;
		} else {
			width += 28;
			height += 8;
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
	int border = (style & SWT.PUSH) != 0 ? 2 : 0;
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
				
	if ((style & SWT.ARROW) != 0) {
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureRight; // NEEDS WORK
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
	
	if ((style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) != 0) {
		int textAlignment = 0;
		int graphicAlignment = 0;
		if ((style & SWT.LEFT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
			graphicAlignment = OS.kControlBevelButtonAlignLeft;
		}
		if ((style & SWT.CENTER) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextCenter;
			graphicAlignment = OS.kControlBevelButtonAlignCenter;
		}
		if ((style & SWT.RIGHT) != 0) {
			textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
			graphicAlignment = OS.kControlBevelButtonAlignRight;
		}
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	}
}

int defaultThemeFont () {	
	return OS.kThemePushButtonFont;
}

void drawWidget (int control, int damageRgn, int visibleRgn, int theEvent) {
	if (isImage && image != null && (style & SWT.PUSH) != 0 && (style & SWT.FLAT) == 0) {
		Rect rect = new Rect();
		OS.GetControlBounds (handle, rect);
		int width = OS.CGImageGetWidth (image.handle);
		int height = OS.CGImageGetHeight (image.handle);
		int x = Math.max (0, (rect.right - rect.left - width) / 2);
		int y = Math.max (0, (rect.bottom - rect.top - height) / 2);
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		GC gc = GC.carbon_new (this, data);
		gc.drawImage (image, x, y);
		gc.dispose ();
	}
	super.drawWidget (control, damageRgn, visibleRgn, theEvent);
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

Rect getInset () {
	if ((style & SWT.PUSH) == 0) return super.getInset();
	return display.buttonInset;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	postEvent (SWT.Selection);
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if ((style & SWT.PUSH) != 0) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		menuShell ().setDefaultButton ((part [0] != 0) ? this : null, false);	
	}
	return result;
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
 * be notified when the control is selected.
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
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		int orientation = OS.kThemeDisclosureRight;
		if ((style & SWT.UP) != 0) orientation = OS.kThemeDisclosureRight; // NEEDS WORK
		if ((style & SWT.DOWN) != 0) orientation = OS.kThemeDisclosureDown;
		if ((style & SWT.LEFT) != 0) orientation = OS.kThemeDisclosureLeft;
		OS.SetControl32BitValue (handle, orientation);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int textAlignment = 0;
	int graphicAlignment = 0;
	if ((style & SWT.LEFT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushLeft;
		graphicAlignment = OS.kControlBevelButtonAlignLeft;
	}
	if ((style & SWT.CENTER) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextCenter;
		graphicAlignment = OS.kControlBevelButtonAlignCenter;
	}
	if ((style & SWT.RIGHT) != 0) {
		textAlignment = OS.kControlBevelButtonAlignTextFlushRight;
		graphicAlignment = OS.kControlBevelButtonAlignRight;
	}
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonTextAlignTag, 2, new short [] {(short)textAlignment});
	OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlBevelButtonGraphicAlignTag, 2, new short [] {(short)graphicAlignment});
	redraw ();
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	/* Bug in MacOS X. When setting the height of a bevel button 
	 * to a value less than 20, the button is drawn incorrectly.
	 * The fix is to force the height to be greater than or equal to 20.
	 */
	if ((style & SWT.ARROW) == 0) {
		height = Math.max (20, height);
	}
	super.setBounds (x, y, width, height);
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	int window = OS.GetControlOwner (handle);
	OS.SetWindowDefaultButton (window, value ? handle : 0);
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
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

	cIcon = createCIcon (image);
	ControlButtonContentInfo inContent = new ControlButtonContentInfo ();
	inContent.contentType = (short)OS.kControlContentCIconHandle;
	inContent.iconRef = cIcon;
	OS.SetBevelButtonContentInfo (handle, inContent);
	redraw ();
}

boolean setRadioSelection (boolean value){
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
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
	OS.SetControl32BitValue (handle, selected ? 1 : 0);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp' can be
 * escaped by doubling it in the string, causing a single
 *'&amp' to be displayed.
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	if (isImage) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
		inContent.contentType = (short)OS.kControlContentTextOnly;
		OS.SetBevelButtonContentInfo(handle, inContent);
	}
	isImage = false;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
	redraw ();
}

int traversalCode (int key, int theEvent) {
	int code = super.traversalCode (key, theEvent);
	if ((style & SWT.PUSH) != 0) return code;
	return code | SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
}

}
