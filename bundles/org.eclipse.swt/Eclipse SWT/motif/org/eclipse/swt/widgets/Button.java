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
	Image image, bitmap, disabled;
	boolean grayed;
	static final byte [] ARM_AND_ACTIVATE;
	static {
		String name = "ArmAndActivate";
		int length = name.length();
		char [] unicode = new char [length];
		name.getChars (0, length, unicode, 0);
		byte [] buffer = new byte [length + 1];
		for (int i = 0; i < length; i++) {
			buffer[i] = (byte) unicode[i];
		}
		ARM_AND_ACTIVATE = buffer;
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
void _setImage (Image image) {
	int labelPixmap = OS.XmUNSPECIFIED_PIXMAP;
	int labelInsensitivePixmap = OS.XmUNSPECIFIED_PIXMAP;
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	bitmap = disabled = null;
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		switch (image.type) {
			case SWT.BITMAP:
				ImageData data;
				if (image.mask == 0	&& (data = image.getImageData ()).alpha == -1 && data.alphaData == null && data.transparentPixel == -1) {
					labelPixmap = image.pixmap;
					disabled = new Image (display, image, SWT.IMAGE_DISABLE);
					labelInsensitivePixmap = disabled.pixmap;
					break;
				}
				//FALL THROUGH
			case SWT.ICON:
				Rectangle rect = image.getBounds ();
				bitmap = new Image (display, rect.width, rect.height);
				GC gc = new GC (bitmap);
				gc.setBackground (getBackground ());
				gc.fillRectangle (rect);
				gc.drawImage (image, 0, 0);
				gc.dispose ();
				labelPixmap = bitmap.pixmap;
				disabled = new Image (display, bitmap, SWT.IMAGE_DISABLE);
				labelInsensitivePixmap = disabled.pixmap;
				break;
			default:
				error (SWT.ERROR_NOT_IMPLEMENTED);
		}
	}
	int [] argList = {
		OS.XmNlabelType, image == null ? OS.XmSTRING : OS.XmPIXMAP,
		OS.XmNlabelPixmap, labelPixmap,
		OS.XmNlabelInsensitivePixmap, labelInsensitivePixmap,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void _setText (String string) {
	char [] text = new char [string.length ()];
	string.getChars (0, text.length, text, 0);
	int mnemonic = fixMnemonic (text);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), text, true);
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);	
	if (xmString == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	if (mnemonic == 0) mnemonic = OS.XK_VoidSymbol;
	int [] argList = {
		OS.XmNlabelType, OS.XmSTRING,
		OS.XmNlabelString, xmString,
		OS.XmNmnemonic, mnemonic,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (xmString != 0) OS.XmStringFree (xmString);
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
	int event = OS.XtMalloc (XEvent.sizeof);
	OS.XtCallActionProc (handle, ARM_AND_ACTIVATE, event, null, 0);
	OS.XtFree (event);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.ARROW) != 0) {
		width += display.scrolledMarginX;
		height += display.scrolledMarginY;
		if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
		return new Point (width, height);
	}
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth | OS.CWHeight;
	int [] argList2 = {OS.XmNrecomputeSize, 1};
	OS.XtSetValues(handle, argList2, argList2.length / 2);
	OS.XtQueryGeometry (handle, null, result);
	int [] argList3 = {OS.XmNrecomputeSize, 0};
	OS.XtSetValues(handle, argList3, argList3.length / 2);
	width += result.width;
	height += result.height;
	/*
	 * Feature in Motif. If a button's labelType is XmSTRING but it
	 * has no label set into it yet, recomputing the size will
	 * not take into account the height of the font, as we would
	 * like it to. Take care of this case.
	 */
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmSTRING) {
		int [] argList1 = {OS.XmNlabelString, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int xmString = argList1 [1];
		if (xmString != 0) {
			if (OS.XmStringEmpty (xmString)) {
				int xmString2 = OS.XmStringCreateLocalized (new byte[]{' ', '\0'});
				if (xmString2 != 0) {
					height += OS.XmStringHeight (font.handle, xmString2);
					OS.XmStringFree(xmString2);
				}
			}
			OS.XmStringFree (xmString);
		}
	}
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {	
		int [] argList4 = new int [] {
			OS.XmNmarginLeft, 0,
			OS.XmNmarginRight, 0,
			OS.XmNmarginTop, 0,
			OS.XmNmarginBottom, 0,
		};
		OS.XtGetValues (handle, argList4, argList4.length / 2);
		if (wHint != SWT.DEFAULT) width = wHint + argList4 [1] + argList4 [3] + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + argList4 [5] + argList4 [7] + (border * 2);
	}	
	return new Point (width, height);
}
void createHandle (int index) {
	if ((style & SWT.PUSH) == 0) state |= THEME_BACKGROUND;
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	int parentHandle = parent.handle;
	
	/* ARROW button */
	if ((style & SWT.ARROW) != 0) {
		int alignment = OS.XmARROW_UP;
		if ((style & SWT.UP) != 0) alignment = OS.XmARROW_UP;
		if ((style & SWT.DOWN) != 0) alignment = OS.XmARROW_DOWN;
		if ((style & SWT.LEFT) != 0) alignment = OS.XmARROW_LEFT;
		if ((style & SWT.RIGHT) != 0) alignment = OS.XmARROW_RIGHT;
		int [] argList = {
			OS.XmNtraversalOn, 0,
			OS.XmNarrowDirection, alignment,
			OS.XmNborderWidth, borderWidth,
			OS.XmNancestorSensitive, 1,
		};
		handle = OS.XmCreateArrowButton (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & SWT.FLAT) != 0) {
			int [] argList1 = {OS.XmNbottomShadowColor, 0};
			OS.XtGetValues (handle, argList1, argList1.length / 2);
			int [] argList2 = {
				OS.XmNshadowThickness, 1,
				OS.XmNtopShadowColor, argList1 [1],
			};
			OS.XtSetValues (handle, argList2, argList2.length / 2);
		}
		return;
	}

	/* Compute alignment */
	int alignment = OS.XmALIGNMENT_BEGINNING;
	if ((style & SWT.CENTER) != 0) alignment = OS.XmALIGNMENT_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.XmALIGNMENT_END;
	
	/* TOGGLE button */
	if ((style & SWT.TOGGLE) != 0) {
		/*
		* Bug in Motif.  When XmNindicatorOn is set to false,
		* Motif doesn't reset the shadow thickness to give a
		* push button look.  The fix is to set the shadow
		* thickness when ever this resource is changed.
		*/
		int thickness = display.buttonShadowThickness;
		int [] argList = {
			OS.XmNancestorSensitive, 1,
			OS.XmNrecomputeSize, 0,
			OS.XmNindicatorOn, 0,
			OS.XmNshadowThickness, (style & SWT.FLAT) != 0 ? 1 : thickness,
			OS.XmNalignment, alignment,
			OS.XmNborderWidth, borderWidth,
		};
		handle = OS.XmCreateToggleButton (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & SWT.FLAT) != 0) {
			int [] argList1 = {OS.XmNbottomShadowColor, 0};
			OS.XtGetValues (handle, argList1, argList1.length / 2);
			int [] argList2 = {OS.XmNtopShadowColor, argList1 [1]};
			OS.XtSetValues (handle, argList2, argList2.length / 2);
		}
		return;
	}
	
	/* CHECK or RADIO button */
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		/*
		* Bug in Motif.  For some reason, a toggle button
		* with XmNindicatorType XmONE_OF_MANY must have this
		* value set at creation or the highlight color will
		* not be correct.  The fix is to set these values
		* on create.
		*/
		int indicatorType = OS.XmONE_OF_MANY;
		if ((style & SWT.CHECK) != 0) indicatorType = OS.XmN_OF_MANY;
		int [] argList = {	
			OS.XmNancestorSensitive, 1,
			OS.XmNrecomputeSize, 0,
			OS.XmNindicatorType, indicatorType,
			OS.XmNalignment, alignment,
			OS.XmNborderWidth, borderWidth,
		};
		handle = OS.XmCreateToggleButton (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	
	/* PUSH button */
	int [] argList = {
		OS.XmNancestorSensitive, 1,
		OS.XmNrecomputeSize, 0,
		OS.XmNalignment, alignment,
		OS.XmNborderWidth, borderWidth,
		/*
		* This code is intentionally commented.  On some
		* platforms, the standard behavior is that push
		* buttons are tab groups, traversed with the tab
		* key.  On Motif, push buttons are tab items, 
		* that are traversed with the arrow keys.  This
		* behavior is unspecifed so the line remains
		* commented.
		*/
//		OS.XmNnavigationType, OS.XmTAB_GROUP,
	};
	handle = OS.XmCreatePushButton (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.FLAT) != 0) {
		int [] argList1 = {OS.XmNbottomShadowColor, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int [] argList2 = {
			OS.XmNshadowThickness, 1,
			OS.XmNtopShadowColor, argList1 [1],
		};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
}
void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.PUSH) == 0) return;
	if (getShell ().parent == null) return;
	int [] argList = new int [] {OS.XmNdefaultButtonShadowThickness, 1};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
int defaultBackground () {
	return display.buttonBackground;
}
Font defaultFont () {
	return display.buttonFont;
}
int defaultForeground () {
	return display.buttonForeground;
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
	checkWidget();
	if ((style & SWT.ARROW) != 0) {
		int [] argList = {OS.XmNarrowDirection, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int direction = argList [1];
		if (direction == OS.XmARROW_UP) return SWT.UP;
		if (direction == OS.XmARROW_DOWN) return SWT.DOWN;
		if (direction == OS.XmARROW_LEFT) return SWT.LEFT;
		if (direction == OS.XmARROW_RIGHT) return SWT.RIGHT;
		return SWT.UP;
	}
	int [] argList = {OS.XmNalignment, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int alignment = argList [1];
	if (alignment == OS.XmALIGNMENT_BEGINNING) return SWT.LEFT;
	if (alignment == OS.XmALIGNMENT_CENTER) return SWT.CENTER;
	if (alignment == OS.XmALIGNMENT_END)return SWT.RIGHT;
	return SWT.CENTER;
}
boolean getDefault () {
	if ((style & SWT.PUSH) == 0) return false;
//	int [] argList = {OS.XmNshowAsDefault, 0};
//	OS.XtGetValues (handle, argList, argList.length / 2);
//	return argList [1] != 0;
	return this == menuShell ().defaultButton;
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
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	int [] argList = {OS.XmNset, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != OS.XmUNSET;
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
	checkWidget();
	if ((style & SWT.ARROW) != 0) return "";
	return text;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	} else {
		OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
	}	
	
}
boolean mnemonicHit (char key) {
	if (!setFocus ()) return false;
	click ();
	return true;
}
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
void releaseWidget () {
	super.releaseWidget ();
	int [] argList = {
		OS.XmNlabelPixmap, OS.XmUNSPECIFIED_PIXMAP,
		OS.XmNlabelInsensitivePixmap, OS.XmUNSPECIFIED_PIXMAP,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if (bitmap != null) bitmap.dispose ();
	if (disabled != null) disabled.dispose ();
	image = bitmap = disabled = null; 
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
	checkWidget();
	if ((style & SWT.ARROW) != 0) {
		int [] argList = {OS.XmNarrowDirection, OS.XmARROW_UP};
		if ((alignment & SWT.UP) != 0) argList [1] = OS.XmARROW_UP;
		if ((alignment & SWT.DOWN) != 0) argList [1] = OS.XmARROW_DOWN;
		if ((alignment & SWT.LEFT) != 0) argList [1] = OS.XmARROW_LEFT;
		if ((alignment & SWT.RIGHT) != 0) argList [1] = OS.XmARROW_RIGHT;
		OS.XtSetValues (handle, argList, argList.length / 2);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int [] argList = {OS.XmNalignment, OS.XmALIGNMENT_BEGINNING};
	if ((alignment & SWT.CENTER) != 0) argList [1] = OS.XmALIGNMENT_CENTER;
	if ((alignment & SWT.RIGHT) != 0) argList [1] = OS.XmALIGNMENT_END;
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	if ((style & SWT.FLAT) != 0) {
		int [] argList1 = {OS.XmNbottomShadowColor, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int [] argList2 = {OS.XmNtopShadowColor, argList1 [1]};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmPIXMAP) _setImage (image);
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	if (getShell ().parent == null) return;
	int [] argList = {OS.XmNshowAsDefault, (value ? 1 : 0)};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
public void setFont (Font font) {
	checkWidget();
	
	/*
	* Bug in Motif. Setting the font in a button widget that does
	* not have a non-empty string causes GP on UTF-8 locale.
	* The fix is to set a non-empty string, change the font,
	* and restore the empty string at the end. 
	*/	
	int [] argList1 = {OS.XmNlabelString, 0, OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	boolean fixString = OS.IsDBLocale && OS.XmStringEmpty (argList1 [1]); 
	if (fixString) {
		byte[] buffer = Converter.wcsToMbcs (getCodePage (), "string", true);
		int xmString = OS.XmStringCreateLocalized (buffer);	
		int [] argList2 = { 
			OS.XmNlabelType, OS.XmSTRING,
			OS.XmNlabelString, xmString,
		};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
		OS.XmStringFree (xmString);
	}
	super.setFont (font);
	if (fixString) OS.XtSetValues (handle, argList1, argList1.length / 2);	
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
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	/* This code is intentionally commented*/
//	if (image == null || text.length () != 0) {
//		_setText (text);
//		return;
//	}
	_setImage (image);
}
boolean setRadioSelection (boolean value) {
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
	int [] argList = {OS.XmNset, selected ? OS.XmSET : OS.XmUNSET};
	OS.XtSetValues (handle, argList, argList.length / 2);
	updateShadows ();
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
	/*
	* Feature in Motif.  Motif does not optimize the case
	* when the same text is set into a button causing
	* it to flash.  The fix is to test for equality and
	* do nothing.
	*/
	if (text.equals (string)) return;
	text = string;
	/* This code is intentionally commented*/
//	if (text.length () == 0 && image != null) {
//		_setImage (image);
//		return;
//	}
	_setText (string);
}
int traversalCode (int key, XKeyEvent xEvent) {
	return super.traversalCode (key, xEvent) | SWT.TRAVERSE_MNEMONIC;
}
void updateShadows () {
	if ((style & SWT.FLAT) != 0 && (style & SWT.TOGGLE) != 0) {
		int [] argList1 = {OS.XmNset, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int pixel = argList1 [1] == OS.XmUNSET ? display.compositeBottomShadow : display.compositeTopShadow;
		int [] argList2 = {OS.XmNtopShadowColor, pixel};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
}
int xFocusIn (XFocusChangeEvent xEvent) {
	super.xFocusIn (xEvent);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0) {
		menuShell ().setDefaultButton (this, false);
	}
	return 0;
}
int xFocusOut (XFocusChangeEvent xEvent) {
	super.xFocusOut (xEvent);
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) != 0 && getDefault ()) {
		menuShell ().setDefaultButton (null, false);
	}
	return 0;
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	postEvent (SWT.Selection);
	return 0;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	updateShadows ();
	postEvent (SWT.Selection);
	return 0;
}
}
