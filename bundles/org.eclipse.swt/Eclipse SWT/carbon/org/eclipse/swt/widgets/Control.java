package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b>
 * <dd>FocusIn, FocusOut, Help, KeyDown, KeyUp, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public abstract class Control extends Widget implements Drawable {
	Composite parent;
	Font font;
	int foreground, background;
	Menu menu;
	String toolTipText;
	Object layoutData;
	Accessible accessible;
	int drawCount;
	boolean visible= true;
	Cursor cursor;
	
Control () {
	/* Do nothing */
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
 * @see SWT#BORDER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control gains or loses focus, by sending
 * it one of the messages defined in the <code>FocusListener</code>
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
 * @see FocusListener
 * @see #removeFocusListener
 */
public void addFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
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
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when mouse buttons are pressed and released, by sending
 * it one of the messages defined in the <code>MouseListener</code>
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
 * @see MouseListener
 * @see #removeMouseListener
 */
public void addMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse passes or hovers over controls, by sending
 * it one of the messages defined in the <code>MouseTrackListener</code>
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
 * @see MouseTrackListener
 * @see #removeMouseTrackListener
 */
public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse moves, by sending it one of the
 * messages defined in the <code>MouseMoveListener</code>
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
 * @see MouseMoveListener
 * @see #removeMouseMoveListener
 */
public void addMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver needs to be painted, by sending it
 * one of the messages defined in the <code>PaintListener</code>
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
 * @see PaintListener
 * @see #removePaintListener
 */
public void addPaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Paint,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when traversal events occur, by sending it
 * one of the messages defined in the <code>TraverseListener</code>
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
 * @see TraverseListener
 * @see #removeTraverseListener
 */
public void addTraverseListener (TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}
/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size of the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}
/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p><p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
 * @return the preferred size of the control.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

Control computeTabGroup () {
	if (isTabGroup()) return this;
	return parent.computeTabGroup ();
}

Control[] computeTabList() {
	if (isTabGroup()) {
		if (getVisible() && getEnabled()) {
			return new Control[] {this};
		}
	}
	return new Control[0];
}

Control computeTabRoot () {
	Control[] tabList = parent._getTabList();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

void createWidget (int index) {
	super.createWidget (index);
	foreground = background = -1;

	/*
	* Feature in Motif.  When the XmNfontList resource is set for
	* a widget, Motif creates a copy of the fontList and disposes
	* the copy when the widget is disposed.  This means that when
	* the programmer queries the font, not only will the handle be
	* different but the font will be unexpectedly disposed when
	* the widget is disposed.  This can cause GP's when the font
	* is set in another widget.  The fix is to cache the font the
	* the programmer provides.  The initial value of the cache is
	* the default font for the widget.
	*/
	font = defaultFont ();

	/*
	* Explicitly set the tab ordering for XmTAB_GROUP widgets to
	* override the default traversal.  This is done so that the
	* traversal order can be changed after the widget tree is
	* created.  Unless explicitly changed, the overridded traversal
	* order is the same as the default.
	*/
    /* AW
	int [] argList1 = new int [] {OS.XmNnavigationType, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	if (argList1 [1] == OS.XmTAB_GROUP) {
		int [] argList2 = new int [] {OS.XmNnavigationType, OS.XmEXCLUSIVE_TAB_GROUP};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
    */
}
int defaultBackground () {
	return getDisplay ().defaultBackground;
}
Font defaultFont () {
	return getDisplay ().defaultFont;
}
int defaultForeground () {
	return getDisplay ().defaultForeground;
}
void enableWidget (boolean enabled) {
	enableHandle (enabled, handle);
}
char findMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while ((index < length) && (string.charAt (index) != Mnemonic)) index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != Mnemonic) return string.charAt (index);
		index++;
	} while (index < length);
 	return '\0';
}
int fontHandle () {
	return handle;
}
/**
 * Forces the receiver to have the <em>keyboard focus</em>, causing
 * all keyboard events to be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setFocus
 */
public boolean forceFocus () {
	checkWidget();
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible () /* AW || !isActive () */) return false;
	if (isFocusControl ()) return true;
	shell.bringToTop ();
	/*
	* This code is intentionally commented.
	*
	* When setting focus to a control, it is
	* possible that application code can set
	* the focus to another control inside of
	* WM_SETFOCUS.  In this case, the original
	* control will no longer have the focus
	* and the call to setFocus() will return
	* false indicating failure.
	* 
	* We are still working on a solution at
	* this time.
	*/
//	if (OS.GetFocus () != OS.SetFocus (handle)) return false;
		
	/* AW
	OS.SetFocus (handle);
	*/
	
	boolean focus= false;
	
	if (this instanceof Text || this instanceof List || this instanceof Combo || this instanceof Canvas)
		focus= true;
	if (!focus && MacUtil.FULL_KBD_NAV && this instanceof Button)
		focus= true;
	
	if (focus) {
		Display display= getDisplay();
		if (display != null)
			display.setMacFocusHandle(((Shell)shell).shellHandle, handle);
	}

	return isFocusControl ();
}

/**
 * Returns the accessible object for the receiver.
 * If this is the first time this object is requested,
 * then the object is created and returned.
 *
 * @return the accessible object
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Accessible#addAccessibleListener
 * @see Accessible#addAccessibleControlListener
 * 
 * @since 2.0
 */
public Accessible getAccessible () {
	checkWidget ();
	if (accessible == null) {
		accessible = Accessible.internal_new_Accessible (this);
	}
	return accessible;
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget();
	return Color.carbon_new (getDisplay (), getBackgroundPixel (), false);
}
int getBackgroundPixel () {
/* AW
	int [] argList = {OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
*/
	if (background == -1) return defaultBackground ();
	return background;
}
/**
 * Returns the receiver's border width.
 *
 * @return the border width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getBorderWidth () {
	checkWidget();
    /* AW
	int topHandle = topHandle ();
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return argList [1];
    */
    return 0;
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null).
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	int topHandle = topHandle ();
    /* AW
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [9] * 2;
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5] + borders, argList [7] + borders);
    */
	Rect bounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	int width = bounds.right - bounds.left;
	int height = bounds.bottom - bounds.top;
	return new Rectangle(bounds.left, bounds.top, width, height);
}
Point getClientLocation () {
    /* AW
	short [] handle_x = new short [1], handle_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, handle_x, handle_y);
	short [] topHandle_x = new short [1], topHandle_y = new short [1];
	OS.XtTranslateCoords (parent.handle, (short) 0, (short) 0, topHandle_x, topHandle_y);
	return new Point (handle_x [0] - topHandle_x [0], handle_y [0] - topHandle_y [0]);
    */
    Rect bounds= new Rect();
    OS.GetControlBounds(handle, bounds);
    return new Point(bounds.left, bounds.top);
}
/**
 * Returns the display that the receiver was created on.
 *
 * @return the receiver's display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEnabled () {
	checkWidget();
	int h= topHandle();
	if (OS.IsValidControlHandle(h))
		return OS.IsControlEnabled(h);
	System.out.println("Control.getEnabled: fixme for " + getClass().getName());
	return true;
}
/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getFont () {
	checkWidget();
	return font;
}

int getFontAscent () {
	int[] oldPort= new int[1];
	OS.GetPort(oldPort);
	OS.SetPortWindowPort(OS.GetControlOwner(handle));
	if (font != null && font.handle != null)
		font.handle.installInGrafPort();
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	int height= fontInfo[0];
	OS.SetPort(oldPort[0]);
	return height;
}

int getFontHeight () {
	int[] oldPort= new int[1];
	OS.GetPort(oldPort);
	OS.SetPortWindowPort(OS.GetControlOwner(handle));
	if (font != null && font.handle != null)
		font.handle.installInGrafPort();
	short[] fontInfo= new short[4];
	OS.GetFontInfo(fontInfo);	// FontInfo
	int height= fontInfo[0] + fontInfo[1];
	OS.SetPort(oldPort[0]);
	return height;
}
/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getForeground () {
	checkWidget();
	return Color.carbon_new (getDisplay (), getForegroundPixel (), false);
}
int getForegroundPixel () {
	/* AW
	int [] argList = {OS.XmNforeground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
	*/
	if (foreground == -1) return defaultForeground ();
	return foreground;
}
/* AW
short [] getIMECaretPos () {
	return new short[]{0, 0};
}
*/
/**
 * Returns layout data which is associated with the receiver.
 *
 * @return the receiver's layout data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Object getLayoutData () {
	checkWidget();
	return layoutData;
}
/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget();
	int topHandle= topHandle ();
	Rect bounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	return new Point(bounds.left, bounds.top);
}
/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not. All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget();
	return menu;
}
/**
 * Returns the receiver's parent, which must be a <code>Composite</code>
 * or null when the receiver is a shell that was created with null or
 * a display for a parent.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Composite getParent () {
	checkWidget();
	return parent;
}
Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}
/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget();
	return parent.getShell ();
}
/**
 * Returns a point describing the receiver's size. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget();
	int topHandle = topHandle ();
	/*
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [5] * 2;
	return new Point (argList [1] + borders, argList [3] + borders);
	*/
	Rect bounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	int width = bounds.right - bounds.left;
	int height = bounds.bottom - bounds.top;
	return new Point(width, height);
}
/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}
/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget();
	return visible;
}
boolean hasFocus () {
	return (this == getDisplay ().getFocusControl ());
}
void hookEvents () {
    /* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddEventHandler (handle, OS.KeyPressMask, false, windowProc, SWT.KeyDown);
	OS.XtAddEventHandler (handle, OS.KeyReleaseMask, false, windowProc, SWT.KeyUp);
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, SWT.MouseDown);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, SWT.MouseUp);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, SWT.MouseMove);
	OS.XtAddEventHandler (handle, OS.EnterWindowMask, false, windowProc, SWT.MouseEnter);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, SWT.MouseExit);
	OS.XtAddEventHandler (handle, OS.ExposureMask, false, windowProc, SWT.Paint);
	OS.XtAddEventHandler (handle, OS.FocusChangeMask, false, windowProc, SWT.FocusIn);
	OS.XtAddCallback (handle, OS.XmNhelpCallback, windowProc, SWT.Help);
    */
    //Display display= getDisplay();		
	//OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
}
/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 *
 * @private
 */
public int internal_new_GC (GCData data) {
	checkWidget();
    /* AW
	if (!OS.XtIsRealized (handle)) {
		Shell shell = getShell ();
		shell.realizeWidget ();
	}
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int xGC = OS.XCreateGC (xDisplay, xWindow, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetGraphicsExposures (xDisplay, xGC, false);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0, OS.XmNcolormap, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	if (data != null) {
		data.device = getDisplay ();
		data.foreground = getForegroundPixel();
		data.background = getBackgroundPixel();
		data.font = font.handle;
		data.controlHandle = handle;
	}

	int wHandle= OS.GetControlOwner(handle);
	int port= OS.GetWindowPort(wHandle);
	if (port == 0) SWT.error(SWT.ERROR_NO_HANDLES);
    return port;
}
/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 *
 * @private
 */
public void internal_dispose_GC (int xGC, GCData data) {
	checkWidget ();
    /* AW
	int xDisplay = 0;
	if (data != null) xDisplay = data.display;
	if (xDisplay == 0 && handle != 0) xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XFreeGC (xDisplay, xGC);
    */
}
/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}
/**
 * Returns <code>true</code> if the receiver has the user-interface
 * focus, and <code>false</code> otherwise.
 *
 * @return the receiver's focus state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isFocusControl () {
	checkWidget();
	return hasFocus ();
}
/**
 * Returns <code>true</code> if the underlying operating
 * system supports this reparenting, otherwise <code>false</code>
 *
 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isReparentable () {
	checkWidget();
	return false;
}

boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscurred by a parent or sibling.
	*/
	/* AW
	if (!isVisible ()) return false;
	Control control = this;
	while (control != null) {
		Point size = control.getSize ();
		if (size.x == 0 || size.y == 0) {
			return false;
		}
		control = control.parent;
	}
	*/
	return true;
	/*
	* Check to see if current damage is included.
	*/
//	if (!OS.IsWindowVisible (handle)) return false;
//	int flags = OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS;
//	int hDC = OS.GetDCEx (handle, 0, flags);
//	int result = OS.GetClipBox (hDC, new RECT ());
//	OS.ReleaseDC (handle, hDC);
//	return result != OS.NULLREGION;
}

boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	/* AW
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.WS_TABSTOP) != 0;
	*/
	// AW: Motif:
	int code = traversalCode ();
	if ((code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0) return false;
	return (code & (SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_TAB_NEXT)) != 0;
}

boolean isTabItem () {
	Control [] tabList = parent._getTabList ();	
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
	/* AW
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_TABSTOP) != 0) return false;
	int code = OS.SendMessage (handle, OS.WM_GETDLGCODE, 0, 0);
	if ((code & OS.DLGC_STATIC) != 0) return false;
	if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
	if ((code & OS.DLGC_WANTARROWS) != 0) return false;
	if ((code & OS.DLGC_WANTTAB) != 0) return false;
	*/
	// AW: Motif
	int code = traversalCode ();
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
}
/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget();
	return getVisible () && parent.isVisible ();
}
void manageChildren () {
/* AW
	OS.XtSetMappedWhenManaged (handle, false);
	OS.XtManageChild (handle);
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtResizeWidget (handle, 1, 1, argList [1]);
	OS.XtSetMappedWhenManaged (handle, true);
*/
}
Decorations menuShell () {
	return parent.menuShell ();
}
boolean mnemonicHit (char key) {
	return false;
}
boolean mnemonicMatch (char key) {
	return false;
}
/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveAbove (Control control) {
	checkWidget();
	if (control != null && control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	setZOrder (control, true);
}
/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveBelow (Control control) {
	checkWidget();
	if (control != null && control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	setZOrder (control, false);
}
/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize
 */
public void pack () {
	checkWidget();
	pack (true);
}
/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 * <p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize
 */
public void pack (boolean changed) {
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}
int processDefaultSelection (Object callData) {
	postEvent (SWT.DefaultSelection);
	return OS.noErr;
}
int processFocusIn () {
	sendEvent (SWT.FocusIn);
	return OS.noErr;
}
int processFocusOut () {
	sendEvent (SWT.FocusOut);
	return OS.noErr;
}
int processHelp (Object callData) {
	sendHelpEvent (callData);
	return OS.noErr;
}
int processKeyDown (Object callData) {
	MacEvent macEvent = (MacEvent) callData;
	if (translateTraversal (macEvent))
		return OS.noErr;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	return sendKeyEvent (SWT.KeyDown, macEvent);
}
int processKeyUp (Object callData) {
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	return sendKeyEvent (SWT.KeyUp, (MacEvent) callData);
}
int processModify (Object callData) {
	sendEvent (SWT.Modify);
	return OS.noErr;
}
int processMouseDown (MacMouseEvent mmEvent) {
	Display display = getDisplay ();
	Shell shell = getShell ();
	display.hideToolTip ();
    int button= mmEvent.getButton();
	sendMouseEvent (SWT.MouseDown, button, mmEvent);
	if (button == 2 && hooks (SWT.DragDetect)) {
		sendEvent (SWT.DragDetect);
	}
	if (button == 3 && menu != null) {
        /* AW
		OS.XmProcessTraversal (handle, OS.XmTRAVERSE_CURRENT);
		menu.setVisible (true);
		*/
	}
	int clickTime = display.getDoubleClickTime ();
	int lastTime = display.lastTime, eventTime = mmEvent.getWhen();
	int lastButton = display.lastButton, eventButton = button;
	if (lastButton == eventButton && lastTime != 0 && Math.abs (lastTime - eventTime) <= clickTime) {
		sendMouseEvent (SWT.MouseDoubleClick, eventButton, mmEvent);
	}
	display.lastTime = eventTime == 0 ? 1 : eventTime;
	display.lastButton = eventButton;

	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/
	if (!shell.isDisposed ()) {
		shell.setActiveControl (this);
	}
	return OS.noErr;
}
int processMouseEnter (MacMouseEvent mme) {
    /* AW
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.subwindow != 0) return 0;
    */
	Event event = new Event ();
	Point p= MacUtil.toControl(handle, mme.getWhere());
	event.x = p.x;
	event.y = p.y;
	postEvent (SWT.MouseEnter, event);
	return OS.noErr;
}
int processMouseMove (MacMouseEvent mme) {
	Display display = getDisplay ();
	display.addMouseHoverTimeOut (handle);
	sendMouseEvent (SWT.MouseMove, 0, mme);
	return OS.noErr;
}
int processMouseExit (MacMouseEvent mme) {
	Display display = getDisplay ();
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
    /* AW
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.subwindow != 0) return 0;
	*/
	Event event = new Event ();
	Point p= MacUtil.toControl(handle, mme.getWhere());
	event.x = p.x;
	event.y = p.y;
	postEvent (SWT.MouseExit, event);
	return OS.noErr;
}
int processMouseHover (MacMouseEvent mme) {
	Display display = getDisplay ();
	Event event = new Event ();
	Point local = toControl (display.getCursorLocation ());
	event.x = local.x; event.y = local.y;
	postEvent (SWT.MouseHover, event);
	display.showToolTip (handle, toolTipText);
	return OS.noErr;
}
int processMouseUp (MacMouseEvent mmEvent) {
	Display display = getDisplay ();
	display.hideToolTip ();
	sendMouseEvent (SWT.MouseUp, mmEvent.getButton(), mmEvent);
	return OS.noErr;
}
int processPaint (Object callData) {
	MacControlEvent mce= (MacControlEvent) callData;
	GC gc= new GC (this);
	Rectangle r= gc.carbon_focus(mce.getDamageRegionHandle(), mce.getGCContext());
	if (r == null || !r.isEmpty()) {
		// erase background
		if ((state & CANVAS) != 0 && (style & SWT.NO_BACKGROUND) == 0)
			gc.fillRectangle(r);
		if (hooks (SWT.Paint)) {
			Event event = new Event();
			//event.count = xEvent.count;
			//event.time = OS.XtLastTimestampProcessed (xDisplay);
			event.gc = gc;
			event.x = r.x;  event.y = r.y;
			event.width = r.width;  event.height = r.height;
			sendEvent (SWT.Paint, event);
		}
	}
	gc.carbon_unfocus ();
	if (!gc.isDisposed ())
		gc.dispose ();
	return OS.noErr;
}
int processResize (Object callData) {
	sendEvent (SWT.Resize);
	// widget could be disposed at this point
	return OS.noErr;
}
int processSelection (Object callData) {
	postEvent (SWT.Selection);
	return OS.noErr;
}
int processSetFocus (Object callData) {
	/*
	* Ignore focus change events when the window getting or losing
	* focus is a menu.  Because XmGetFocusWidget() does not answer
	* the menu shell (it answers the menu parent), it is necessary
	* to use XGetInputFocus() to get the real X focus window.
	*/
    /* AW
	int xDisplay = xEvent.display;
	if (xDisplay == 0) return 0;
	int [] unused = new int [1], xWindow = new int [1];
	OS.XGetInputFocus (xDisplay, xWindow, unused);
	if (xWindow [0] != 0) {
		int widget = OS.XtWindowToWidget (xDisplay, xWindow [0]);
		if (widget != 0 && OS.XtClass (widget) == OS.XmMenuShellWidgetClass ()) return 0;
	}
    */
	/* Process the focus change for the widget */

	Shell shell = getShell ();
	Boolean b = (Boolean) callData;
	if (b.booleanValue ()) {
	
		processFocusIn ();
		// widget could be disposed at this point
		/*
		* It is possible that the shell may be
		* disposed at this point.  If this happens
		* don't send the activate and deactivate
		* events.
		*/
		if (!shell.isDisposed ()) {
			shell.setActiveControl (this);
		}
	} else {
		Display display = getDisplay ();

		processFocusOut ();
		// widget could be disposed at this point
		/*
		 * It is possible that the shell may be
		 * disposed at this point.  If this happens
		 * don't send the activate and deactivate
		 * events.
		 */
		if (!shell.isDisposed ()) {
			Control control = display.getFocusControl ();
			if (control == null || shell != control.getShell () ) {
				shell.setActiveControl (null);
			}
		}
	}
	return OS.noErr;
}
void propagateChildren (boolean enabled) {
	propagateWidget (enabled);
}
void propagateWidget (boolean enabled) {
	propagateHandle (enabled, handle);
}
void realizeChildren () {
	if (!isEnabled ()) propagateWidget (false);
}
/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 */
public void redraw () {
	checkWidget();
	redrawWidget (0, 0, 0, 0, false);
}
/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted. If the <code>all</code> flag
 * is <code>true</code>, any children of the receiver which
 * intersect with the specified area will also paint their
 * intersecting areas. If the <code>all</code> flag is 
 * <code>false</code>, the children will not be painted.
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	if (width <= 0 || height <= 0) return;
	redrawWidget (x, y, width, height, all);
}
void redrawWidget (int x, int y, int width, int height, boolean all) {
	redrawHandle (x, y, width, height, handle, all);
}
void releaseWidget () {
	super.releaseWidget ();
	Display display = getDisplay ();
	display.releaseToolTipHandle (handle);
	toolTipText = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	parent = null;
	layoutData = null;
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see FocusListener
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.FocusIn, listener);
	eventTable.unhook(SWT.FocusOut, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see MouseListener
 * @see #addMouseListener
 */
public void removeMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseDown, listener);
	eventTable.unhook(SWT.MouseUp, listener);
	eventTable.unhook(SWT.MouseDoubleClick, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse moves.
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
 * @see MouseMoveListener
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseMove, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see MouseTrackListener
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}
void sendHelpEvent (Object callData) {
	Control control = this;
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			return;
		}
		control = control.parent;
	}
}
final int sendKeyEvent (int type, MacEvent mEvent) {
	Event event = new Event ();
    event.time = mEvent.getWhen();
	setKeyState (event, mEvent);
	return sendKeyEvent (type, mEvent, event);
//	Control control = this;
//	if ((state & CANVAS) != 0) {
//		if ((style & SWT.NO_FOCUS) != 0) {
//			Display display = getDisplay ();
//			control = display.getFocusControl ();
//		}
//	}
//	if (control != null) {
//		control.postEvent (type, event);
//	}
}
int sendKeyEvent (int type, MacEvent mEvent, Event event) {
	postEvent (type, event);
	return 0;
}
final void sendMouseEvent (int type, int button, MacMouseEvent mme) {
	Event event = new Event ();
    event.time = mme.getWhen();
	event.button = button;
	Point ml= MacUtil.toControl(handle, mme.getWhere());
	event.x = ml.x;  event.y = ml.y;
	// AW setInputState (event, mEvent);
	event.stateMask= mme.getState();
	postEvent (type, event);
}
/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground (Color color) {
	checkWidget();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	setBackgroundPixel (pixel);
}
void setBackgroundPixel (int pixel) {
	/* AW
	int [] argList = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmChangeColor (handle, pixel);
	OS.XtSetValues (handle, argList, argList.length / 2);
	*/
	if (background == pixel) return;
	background = pixel;
	redrawHandle(0, 0, 0, 0, handle, false);
}
/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	int topHandle = topHandle ();
	width = Math.max(width, 0);
	height = Math.max(height, 0);
	Rect bounds= new Rect();
	Rect pbounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	OS.GetControlBounds(parent.handle, pbounds);
	boolean sameOrigin = bounds.left == x && bounds.top == y;
	boolean sameExtent = (bounds.right-bounds.left) == width && (bounds.bottom-bounds.top) == height;
	if (sameOrigin && sameExtent) return;
	internalSetBounds(topHandle, x, y, width, height);
	if (!sameOrigin) sendEvent (SWT.Move);
	if (!sameExtent) sendEvent (SWT.Resize);
}
/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the argument. The <code>x</code> and 
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}
/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	checkWidget();
	System.out.println("Control.setCapture: nyi");
    /* AW
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	if (capture) {
		int window = OS.XtWindow (handle);
		if (window == 0) return;
		OS.XGrabPointer (
			display,
			window,
			0,
			OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask,
			OS.GrabModeAsync,
			OS.GrabModeAsync,
			OS.None,
			OS.None,
			OS.CurrentTime);
	} else {
		OS.XUngrabPointer (display, OS.CurrentTime);
	}
    */
}
/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument, or to the default cursor for that kind of control
 * if the argument is null.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @param cursor the new cursor (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget();
	if (cursor == null) {
		cursor= null;
	} else {
		if (cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		cursor= cursor;
	}
}
/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	enableWidget (enabled);
	if (!enabled || (isEnabled () && enabled)) {
		propagateChildren (enabled);
	}
}
/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #forceFocus
 */
public boolean setFocus () {
	checkWidget();
	return forceFocus ();
}
/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setFont (Font font) {
	checkWidget();
	if (font == null) font = defaultFont ();
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.font = font;

	int fontHandle = fontHandle ();
	ControlFontStyleRec fontRec = new ControlFontStyleRec();
	fontRec.flags= (short)(OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask);
	fontRec.font= font.handle.fID;
	fontRec.size= font.handle.fSize;
	fontRec.style= font.handle.fFace;
	if (OS.SetControlFontStyle(fontHandle, fontRec) != OS.noErr)
		; //System.out.println("Control.setFont("+this+"): error");
}
/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setForeground (Color color) {
	checkWidget();
    /* AW
	if (color == null) {
		setForegroundPixel (defaultForeground ());
	} else {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		setForegroundPixel (color.handle.pixel);
	}
    */
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	setForegroundPixel (pixel);
}
void setForegroundPixel (int pixel) {
	/* AW
	int [] argList = {OS.XmNforeground, pixel};
	OS.XtSetValues (handle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
	*/
	if (foreground == pixel) return;
	foreground = pixel;
	redrawHandle(0, 0, 0, 0, handle, false);
}
void setGrabCursor (int cursor) {
	System.out.println("Control.setGrabCursor: nyi");
}
/**
 * Sets the layout data associated with the receiver to the argument.
 * 
 * @param layoutData the new layout data for the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}
/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	boolean sameOrigin = (x == (short) argList [1]) && (y == (short) argList [3]);
	if (sameOrigin) return;
	OS.XtMoveWidget (topHandle, x, y);
	if (!sameOrigin) sendEvent (SWT.Move);
    */
 	int topHandle = topHandle ();
	Rect bounds= new Rect();
	Rect pbounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	OS.GetControlBounds(parent.handle, pbounds);
	boolean sameOrigin = (x == bounds.left) && (y == bounds.top);
	if (sameOrigin) return;
	internalSetBounds(topHandle, x, y, bounds.right-bounds.left, bounds.bottom-bounds.top);
	sendEvent (SWT.Move);
}
/**
 * Sets the receiver's location to the point specified by
 * the argument which is relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}
/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget();
	if (menu != null) {
		if (menu.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

/**
 * Changes the parent of the widget to be the one provided if
 * the underlying operating system supports this feature.
 * Answers <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget();
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.
 * <p>
 * Note: This operation is a hint and may not be supported on some
 * platforms or for some widgets.
 * </p>
 *
 * @param redraw the new redraw state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #redraw
 * @see #update
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0)
			OS.HIViewSetDrawingEnabled(topHandle(), true);
	} else {
		if (drawCount++ == 0)
			OS.HIViewSetDrawingEnabled(topHandle(), false);
	}
}
/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget();
    /*
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int newWidth = Math.max (width - (argList [5] * 2), 1);
	int newHeight = Math.max (height - (argList [5] * 2), 1);
	boolean sameExtent = (newWidth == argList [1]) && (newHeight == argList [3]);
	OS.XtResizeWidget (topHandle, newWidth, newHeight, argList [5]);
	*/
	int topHandle = topHandle ();
	width = Math.max(width, 0);
	height = Math.max(height, 0);
	Rect bounds= new Rect();
	internalGetControlBounds(topHandle, bounds);
	boolean sameExtent = (bounds.right-bounds.left) == width && (bounds.bottom-bounds.top) == height;
	if (sameExtent) return;	
	internalSetBounds(topHandle, bounds.left, bounds.top, width, height);
	sendEvent (SWT.Resize);
}
/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 *
 * @param size the new size for the receiver
 * @param height the new height for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}

boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	return setFocus ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}
/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget();
	if (visible == false)
		visible= visible;
    if (this.visible != visible) {
	    this.visible= visible;
		int topHandle = topHandle ();
		if (OS.IsControlVisible(topHandle) != visible) {
			OS.HIViewSetVisible(topHandle, visible);
			sendEvent (visible ? SWT.Show : SWT.Hide);
		}
    }
}
void setZOrder (Control control, boolean above) {
	
	if (control != null && control.parent != parent) return;
	
	int thisHandle= topHandle();
	if (parent == null)
		error (SWT.ERROR_INVALID_PARENT);
	int destHandle= parent.handle;
	int otherHandle= 0;
	if (control != null)
		otherHandle= control.topHandle();
		
	// AW: doesn't handle reparenting case yet!
	OS.HIViewSetZOrder(thisHandle, above ? OS.kHIViewZOrderAbove : OS.kHIViewZOrderBelow, otherHandle);
}
/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toControl (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
    /* AW
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
	return new Point (point.x - root_x [0], point.y - root_y [0]);
    */
	return MacUtil.toControl(handle, point);
}
/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toDisplay (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
    /* AW
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) point.x, (short) point.y, root_x, root_y);
	return new Point (root_x [0], root_y [0]);
    */
	return MacUtil.toDisplay(handle, point);
}
/* AW
boolean translateMnemonic (char key, XKeyEvent xEvent) {
	if (!isVisible () || !isEnabled ()) return false;
	boolean doit = mnemonicMatch (key);
	if (hooks (SWT.Traverse)) {
		Event event = new Event();
		event.doit = doit;
		event.detail = SWT.TRAVERSE_MNEMONIC;
		event.time = xEvent.time;
		setKeyState (event, xEvent);
		sendEvent (SWT.Traverse, event);
		doit = event.doit;
	}
	if (doit) return mnemonicHit (key);
	return false;
}
boolean translateMnemonic (int key, XKeyEvent xEvent) {
	if (xEvent.state != OS.Mod1Mask) {
		if (xEvent.state != 0 || !(this instanceof Button)) {
			return false;
		}
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		char ch = mbcsToWcs ((char) key);
		return ch != 0 && shell.translateMnemonic (ch, xEvent);
	}
	return false;
}
*/
boolean translateTraversal (MacEvent mEvent) {
	
	int kind= mEvent.getKind();
	if (kind != OS.kEventRawKeyDown && kind != OS.kEventRawKeyRepeat)
		return false;

	int detail = SWT.TRAVERSE_NONE;
	/* AW
	GdkEventKey keyEvent = new GdkEventKey ();
	OS.memmove (keyEvent, gdkEvent, GdkEventKey.sizeof);
	int key = keyEvent.keyval;
	int code = traversalCode (key, gdkEvent);
	int [] state = new int [1];
	OS.gdk_event_get_state (gdkEvent, state);
	*/
	int code= traversalCode ();
	int key= mEvent.getKeyCode();
	int state= mEvent.getStateMask();
	boolean all = false;
	switch (key) {
		case 30 /* OS.GDK_Escape:
		case OS.GDK_Cancel */: {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case 36 /* OS.GDK_Return */ : {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		//case OS.GDK_ISO_Left_Tab: 
		case 48 /* OS.GDK_Tab */ : {
			boolean next = (state & SWT.SHIFT) == 0;
			/*
			* NOTE: This code causes Shift+Tab and Ctrl+Tab to
			* always attempt traversal which is not correct.
			* The default should be the same as a plain Tab key.
			* This behavior is currently relied on by StyledText.
			* 
			* The correct behavior is to give every key to any
			* control that wants to see every key.  The default
			* behavior for a Canvas should be to see every key.
			*/
			/* AW
			switch (state [0]) {
				case OS.GDK_SHIFT_MASK:
				case OS.GDK_CONTROL_MASK:
					code |= SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_TAB_NEXT;
			}
			*/
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case 126: // OS.GDK_Up:
		case 123: // OS.GDK_Left:
			detail = SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
			
		case 125: // OS.GDK_Down:
		case 124: /* OS.GDK_Right: */
			detail = SWT.TRAVERSE_ARROW_NEXT;
			break;
			
		case 116: // OS.GDK_Page_Up:
		case 121: /* OS.GDK_Page_Down: */ {
			all = true;
			/* AW
			if ((state [0] & OS.GDK_CONTROL_MASK) == 0) return false;
			*/
			/*
			* NOTE: This code causes Ctrl+PgUp and Ctrl+PgDn to always
			* attempt traversal which is not correct.  This behavior is
			* currently relied on by StyledText.
			* 
			* The correct behavior is to give every key to any
			* control that wants to see every key.  The default
			* behavior for a Canvas should be to see every key.
			*/
			code |= SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
			detail = key == 121 ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	
	Event event = new Event ();
	event.doit = (code & detail) != 0;
	event.detail = detail;
	/* AW
	event.time = keyEvent.time;
	setInputState (event, gdkEvent);
	*/
	Shell shell = getShell ();
	Control control = this;
	do {
		if (control.traverse (event)) return true;
		if (!event.doit && control.hooks (SWT.Traverse)) {
			return false;
		}
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}
int traversalCode () {
	/* AW
	int [] argList = new int [] {OS.XmNtraversalOn, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == 0) return 0;
	*/
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	/* AW
	if (getNavigationType () == OS.XmNONE) {
		code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	}
	*/
	return code;
}
boolean traverseMnemonic (char key) {
	if (!isVisible () || !isEnabled ()) return false;
	return mnemonicMatch (key) && mnemonicHit (key);
}
/**
 * Based on the argument, perform one of the expected platform
 * traversal action. The argument should be one of the constants:
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>, 
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>, 
 * <code>SWT.TRAVERSE_ARROW_NEXT</code> and <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
 *
 * @param traversal the type of traversal
 * @return true if the traversal succeeded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean traverse (int traversal) {
	checkWidget();
	if (!isFocusControl () && !setFocus ()) return false;
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverse (Event event) {
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return false;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:				return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:			return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:			return traverseMnemonic (event);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

boolean traverseEscape () {
	Shell shell = getShell ();
	if (shell.parent == null) return false;
	if (!shell.isVisible () || !shell.isEnabled ()) return false;
	shell.close ();
	return true;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Control group = computeTabGroup ();
	Control [] list = root.computeTabList ();
	int length = list.length;
	int index = 0;
	while (index < length) {
		if (list [index] == group) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = ((index + offset + length) % length)) != start) {
		Control control = list [index];
		if (!control.isDisposed () && control.setTabGroupFocus ()) {
			if (!isDisposed () && !isFocusControl ()) return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus ();
}

boolean traverseItem (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus ()) return true;
		}
	}
	return false;
}

boolean traverseReturn () {
	Button button = menuShell ().getDefaultButton ();
	if (button == null || button.isDisposed ()) return false;
	if (!button.isVisible () || !button.isEnabled ()) return false;
	button.click ();
	return true;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseMnemonic (Event event) {
	// This code is intentionally commented.
	// TraverseMnemonic always originates from the OS and
	// never through the API, and on the GTK platform, accels
	// are hooked by the OS before we get the key event.
	// int shellHandle = _getShell ().topHandle ();
	// return OS.gtk_accel_groups_activate (shellHandle, keyCode, stateMask);
	return true;
}
/**
 * Forces all outstanding paint requests for the widget tree
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw
 */
public void update () {
	checkWidget();
    /* AW
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	XAnyEvent event = new XAnyEvent ();
	OS.XSync (display, false);  OS.XSync (display, false);
	while (OS.XCheckWindowEvent (display, window, OS.ExposureMask, event)) {
		OS.XtDispatchEvent (event);
	}
    */
	getDisplay().update();
}

//////////////////////////////////////////////////////////////////////
// Mac stuff
//////////////////////////////////////////////////////////////////////
	/**
	 * Sets the bounds of the given control.
	 */
	private void internalSetBounds(int hndl, int x, int y, int width, int height) {
		Rect newBounds= new Rect();
		OS.SetRect(newBounds, (short)x, (short)y, (short)(x+width), (short)(y+height));
		handleResize(hndl, newBounds);
	}
	
	/**
	 * subclasses can override if a resize must trigger some internal layout.
	 */
	void handleResize(int hndl, Rect bounds) {
		OS.SetControlBounds(hndl, bounds);
	}
	
	/**
	 * subclasses can override.
	 */
	void internalGetControlBounds(int hndl, Rect bounds) {
		OS.GetControlBounds(hndl, bounds);
	}

	/**
	 * Hook (overwritten in Text and Combo)
	 */
	/*
	final int sendKeyEvent(int type, int nextHandler, int eRefHandle) {
		
		MacEvent mEvent= new MacEvent(eRefHandle);
		if (translateTraversal(mEvent))
			return 0;

		processEvent (type, new MacEvent(eRefHandle));
		return OS.kNoErr;
	}
	*/
}