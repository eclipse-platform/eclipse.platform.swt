package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.MacUtil;
import org.eclipse.swt.internal.carbon.ControlButtonContentInfo;

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
public /*final*/ class Button extends Control {
	Image image;
	
	// AW
	private boolean fImageMode;
	private int fAlignment;
	private int fCIconHandle;
	private int fTopMargin;
	private int fBottomMargin;
	
	private static final int MARGIN= 3;	// correct value would be 6; however the shadow is only 2
	private static final int SPACE= 9;	// min is 8 or may be 9
	private static final int TOP_MARGIN= 0;	// 
	private static final int BOTTOM_MARGIN= 5;	// 
	// AW
	
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
	if ((style & SWT.PUSH) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}
void click () {
	short part= 10;
	if ((style & SWT.CHECK) != 0 || (style & SWT.RADIO) != 0)
		part= 11;
    OS.HIViewSimulateClick(handle, part, 0, new short[1]);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.ARROW) != 0) {
		Display display = getDisplay ();
		width += display.scrolledMarginX;
		height += display.scrolledMarginY;
		return new Point (width, height);
	}
    /* AW
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth | OS.CWHeight;
	int [] argList2 = {OS.XmNrecomputeSize, 1};
	OS.XtSetValues(handle, argList2, argList2.length / 2);
	OS.XtQueryGeometry (handle, null, result);
	int [] argList3 = {OS.XmNrecomputeSize, 0};
	OS.XtSetValues(handle, argList3, argList3.length / 2);
	*/
	Point result= MacUtil.computeSize(handle);
	if ((style & SWT.PUSH) != 0) {
		if (image != null) {	// is a Bevel button!
			Rectangle bounds= image.getBounds();
			result.x= 4 + bounds.width + 4;
			result.y= 4 + bounds.height + 4;
		} else {
			String s= getText();
			if (s != null && s.length() > 0) {
				result.x= result.x - 2*SPACE + 2*MARGIN;
				result.y= result.y + TOP_MARGIN + BOTTOM_MARGIN;
			}
		}
	}
	width += result.x;
	height += result.y;
	/*
	 * Feature in Motif. If a button's labelType is XmSTRING but it
	 * has no label set into it yet, recomputing the size will
	 * not take into account the height of the font, as we would
	 * like it to. Take care of this case.
	 */
    /* AW
	int [] argList = {OS.XmNlabelType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmSTRING) {
		int [] argList1 = {OS.XmNlabelString, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int xmString = argList1 [1];
		if (OS.XmStringEmpty (xmString)) height += getFontHeight ();
	}
	*/
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		/* AW
		int [] argList4 = new int [] {OS.XmNmarginLeft, 0, OS.XmNmarginRight, 0, OS.XmNmarginTop, 0, OS.XmNmarginBottom, 0};
		OS.XtGetValues (handle, argList4, argList4.length / 2);
		if (wHint != SWT.DEFAULT) width = wHint + argList4 [1] + argList4 [3] + (border * 2);
		if (hHint != SWT.DEFAULT) height = hHint + argList4 [5] + argList4 [7] + (border * 2);
		*/
		int left= 0;
		int right= 0;
		int top= 0;
		int bottom= 0;
		
		if (wHint != SWT.DEFAULT) width = wHint + left + right;
		if (hHint != SWT.DEFAULT) height = hHint + top + bottom;
	}
		
	return new Point(width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	/* AW
	int borderWidth = (style & SWT.BORDER) != 0 ? 1 : 0;
	*/
	int parentHandle = parent.handle;

	/* ARROW button */
	if ((style & SWT.ARROW) != 0) {
		System.out.println("Button.createHandle(Arrow): nyi");
        /*
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
        */
        handle= MacUtil.newControl(parentHandle, OS.kControlPopupArrowEastProc);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
        /* AW
		if ((style & SWT.FLAT) != 0) {
			int [] argList1 = {OS.XmNshadowThickness, 1};
			OS.XtSetValues (handle, argList1, argList1.length / 2);
		}
        */
		return;
	}

	/* Compute alignment */
    /* AW
	int alignment = OS.XmALIGNMENT_BEGINNING;
	if ((style & SWT.CENTER) != 0) alignment = OS.XmALIGNMENT_CENTER;
	if ((style & SWT.RIGHT) != 0) alignment = OS.XmALIGNMENT_END;
    */

	/* TOGGLE button */
	if ((style & SWT.TOGGLE) != 0) {
		/*
		* Bug in Motif.  When XmNindicatorOn is set to false,
		* Motif doesn't reset the shadow thickness to give a
		* push button look.  The fix is to set the shadow
		* thickness when ever this resource is changed.
		*/
        /* AW
		Display display = getDisplay ();
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
        */
		handle= MacUtil.newControl(parentHandle, (short)0, OS.kControlBehaviorToggles, (short)0, OS.kControlBevelButtonNormalBevelProc);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		setFont(defaultFont());
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
        /* AW
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
        */
		int type= (style & SWT.CHECK) != 0
					? OS.kControlCheckBoxAutoToggleProc
					: OS.kControlRadioButtonAutoToggleProc;
		handle= MacUtil.newControl(parentHandle, (short)0, (short)0, (short)100, type);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		setFont(defaultFont());
		return;
	}

	/* PUSH button */
    /* AW
	int [] argList = {
		OS.XmNancestorSensitive, 1,
		OS.XmNrecomputeSize, 0,
		OS.XmNalignment, alignment,
		OS.XmNborderWidth, borderWidth,
    */
	int type= (style & SWT.FLAT) != 0
					? OS.kControlBevelButtonNormalBevelProc
					: OS.kControlPushButtonProc;
    handle= MacUtil.newControl(parentHandle, type);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	setFont(defaultFont());
	/* AW
	if ((style & SWT.FLAT) != 0) {
		int [] argList1 = {OS.XmNshadowThickness, 1};
		OS.XtSetValues (handle, argList1, argList1.length / 2);
	}
	*/
}
int defaultBackground () {
	return getDisplay ().buttonBackground;
}
Font defaultFont () {
	return getDisplay ().buttonFont;
}
int defaultForeground () {
	return getDisplay ().buttonForeground;
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
    return fAlignment;
}
boolean getDefault () {
	if ((style & SWT.PUSH) == 0) return false;
    int[] control= new int[1];
	OS.GetWindowDefaultButton(OS.GetControlOwner(handle), control);
	return control[0] == handle;
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
	checkWidget();
	if ((style & SWT.ARROW) != 0) return "";
	int sHandle[]= new int[1];
    OS.CopyControlTitleAsCFString(handle, sHandle);
	return MacUtil.getStringAndRelease(sHandle[0]);
}
void hookEvents () {
	super.hookEvents ();
	/* AW
	int callback = OS.XmNactivateCallback;
	int windowProc = getDisplay ().windowProc;
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) callback = OS.XmNvalueChangedCallback;
	OS.XtAddCallback (handle, callback, windowProc, SWT.Selection);
	*/
	if (MacUtil.HIVIEW) {
		Display display= getDisplay();
		OS.SetControlAction(handle, display.fControlActionProc);
	}
}
boolean mnemonicHit (char key) {
	if (!setFocus ()) return false;
	click ();
	return true;
}
/* AW
boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}
*/
int processFocusIn () {
	super.processFocusIn ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) == 0) return 0;
	getShell ().setDefaultButton (this, false);
	return 0;
}
int processFocusOut () {
	super.processFocusOut ();
	// widget could be disposed at this point
	if (handle == 0) return 0;
	if ((style & SWT.PUSH) == 0) return 0;
	if (getDefault ()) {
		getShell ().setDefaultButton (null, false);
	}
	return 0;
}
int processSelection (Object callData) {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) selectRadio ();
	}
	return super.processSelection (callData);
}
void releaseWidget () {
	super.releaseWidget ();
    /*
	int [] argList = {
		OS.XmNlabelPixmap, OS.XmUNSPECIFIED_PIXMAP,
		OS.XmNlabelInsensitivePixmap, OS.XmUNSPECIFIED_PIXMAP,
	};
	OS.XtSetValues (handle, argList, argList.length / 2);
    */
    if (fCIconHandle != 0) {
    	if (handle != 0) {
    		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
			OS.SetBevelButtonContentInfo(handle, inContent);
    	}
		Image.disposeCIcon(fCIconHandle);
		fCIconHandle= 0;
    }
	image = null;
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
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child && child instanceof Button) {
			Button button = (Button) child;
			if ((button.getStyle () & SWT.RADIO) != 0) {
				if (button.getSelection ()) {
					button.setSelection (false);
					button.postEvent (SWT.Selection);
				}
			}
		}
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
		fAlignment= alignment;
		System.out.println("Button.setAlignment: nyi");
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	fAlignment= alignment;
	System.out.println("Button.setAlignment: nyi");
}
void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	if (getShell ().parent == null) return;
	OS.SetWindowDefaultButton(OS.GetControlOwner(handle), value ? handle : 0);
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
	
	this.image = image;
	
	if (fCIconHandle != 0) {
		Image.disposeCIcon(fCIconHandle);
		fCIconHandle= 0;
	}
	
	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		fCIconHandle= Image.carbon_createCIcon(image);
		if (fCIconHandle != 0)
			setMode(fCIconHandle);
	} else 
		setMode(0);
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
	OS.SetControl32BitValue(handle, selected ? 1 : 0);
}
/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
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
	
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(MacUtil.removeMnemonics(string));
		if (OS.SetControlTitleWithCFString(handle, sHandle) != OS.noErr)
			error (SWT.ERROR_CANNOT_SET_TEXT);
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
}

int traversalCode () {
	int code = super.traversalCode ();
	if ((style & SWT.PUSH) != 0) return code;
	return code | SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
}

////////////////////////////////////////////////////////
// Mac stuff
////////////////////////////////////////////////////////

private void setMode(int icon) {
	
	if ((style & SWT.FLAT) != 0 || fImageMode) {
		ControlButtonContentInfo inContent = new ControlButtonContentInfo();
		inContent.contentType = (short)OS.kControlContentCIconHandle;
		inContent.iconRef = icon;
		OS.SetBevelButtonContentInfo(handle, inContent);
		redraw();
		return;
	}

	if ((style & SWT.PUSH) == 0)
		return;	// we only transmogrify push buttons
	
	fImageMode= true;
	
	int[] ph= new int[1];
	int rc= OS.GetSuperControl(handle, ph);
	if (rc != OS.noErr)
		System.out.println("Button.setMode: " + rc);
	int parentHandle= ph[0];
	
	Rect bounds= new Rect();
	OS.GetControlBounds(handle, bounds);
	
	int index= MacUtil.indexOf(parentHandle, handle);
	if (index < 0)
		System.out.println("Button.setMode: can't find handle");
	Widget w= WidgetTable.get(handle);
	WidgetTable.remove(handle);
	OS.DisposeControl(handle);
	
	int type= icon != 0 ? OS.kControlBevelButtonNormalBevelProc : OS.kControlPushButtonProc;
		
    handle= MacUtil.newControl(parentHandle, index, (short)0, (short)0, (short)0, type);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	WidgetTable.put(handle, w);
	
	OS.SetControlBounds(handle, bounds);
	ControlButtonContentInfo inContent = new ControlButtonContentInfo();
	inContent.contentType = (short)OS.kControlContentCIconHandle;
	inContent.iconRef = icon;
	OS.SetBevelButtonContentInfo(handle, inContent);
}

/**
 * Overridden from Control.
 * x and y are relative to window!
 */
void handleResize(int hndl, Rect bounds) {
	fTopMargin= fBottomMargin= 0;
	if ((style & SWT.PUSH) != 0 && image == null) {	// for push buttons
		org.eclipse.swt.graphics.Point result= MacUtil.computeSize(hndl);
		int diff= (bounds.bottom-bounds.top)-result.y;
		fTopMargin= diff/2;
		fBottomMargin= diff-fTopMargin;
		bounds.left+= MARGIN;
		bounds.top+= fTopMargin;
		bounds.right-= MARGIN;
		bounds.bottom-= fBottomMargin;
	}
	super.handleResize(hndl, bounds);
}

void internalGetControlBounds(int hndl, Rect bounds) {
	super.internalGetControlBounds(hndl, bounds);
	if ((style & SWT.PUSH) != 0 && image == null) {
		bounds.left+= -MARGIN;
		bounds.top+= -fTopMargin;
		bounds.right-= -MARGIN;
		bounds.bottom-= -fBottomMargin;
	}
}

public void setFont (Font font) {
	super.setFont(null);
}

int sendKeyEvent(int type, MacEvent mEvent, Event event) {
	return OS.CallNextEventHandler(mEvent.getNextHandler(), mEvent.getEventRef());
}

}
