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


import org.eclipse.swt.internal.carbon.ATSFontMetrics;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.HIThemeTextInfo;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.HMHelpContentRec;
import org.eclipse.swt.internal.carbon.HIThemeFrameDrawInfo;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.accessibility.Accessible;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize, Traverse</dd>
 * </dl>
 * </p><p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#control">Control snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Control extends Widget implements Drawable {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;
	Composite parent;
	String toolTipText;
	Object layoutData;
	int drawCount, visibleRgn;
	Menu menu;
	float [] foreground, background;
	Image backgroundImage;
	Font font;
	Cursor cursor;
	Region region;
	GCData gcs[];
	Accessible accessible;

	static final String RESET_VISIBLE_REGION = "org.eclipse.swt.internal.resetVisibleRegion"; //$NON-NLS-1$


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
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

int actionProc (int theControl, int partCode) {
	int result = super.actionProc (theControl, partCode);
	if (result == OS.noErr) return result;
	if (isDisposed ()) return OS.noErr;
	sendTrackEvents ();
	return result;
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
 * be notified when a drag gesture occurs, by sending it
 * one of the messages defined in the <code>DragDetectListener</code>
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
 * @see DragDetectListener
 * @see #removeDragDetectListener
 * 
 * @since 3.3
 */
public void addDragDetectListener (DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.DragDetect,typedListener);
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
 * <p>
 * When a key listener is added to a control, the control
 * will take part in widget traversal.  By default, all
 * traversal keys (such as the tab key and so on) are
 * delivered to the control.  In order for a control to take
 * part in traversal, it should listen for traversal events.
 * Otherwise, the user can traverse into a control but not
 * out.  Note that native controls such as table and tree
 * implement key traversal in the operating system.  It is
 * not necessary to add traversal listeners for these controls,
 * unless you want to override the default traversal.
 * </p>
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
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
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
 * be notified when the mouse wheel is scrolled, by sending
 * it one of the messages defined in the
 * <code>MouseWheelListener</code> interface.
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
 * @see MouseWheelListener
 * @see #removeMouseWheelListener
 *
 * @since 3.3
 */
public void addMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseWheel, typedListener);
}

void addRelation (Control control) {
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

int colorProc (int inControl, int inMessage, int inDrawDepth, int inDrawInColor) {
	switch (inMessage) {
		case OS.kControlMsgApplyTextColor: {
			if (foreground != null) {
				OS.RGBForeColor (toRGBColor (foreground));
			} else {
				OS.SetThemeTextColor ((short) OS.kThemeTextColorDialogActive, (short) inDrawDepth, inDrawInColor != 0);
			}
			return OS.noErr;
		}
		case OS.kControlMsgSetUpBackground: {
			float [] background = this.background != null ? this.background : getParentBackground ();
			if (background != null) {
				OS.RGBBackColor (toRGBColor (background));
			} else {
				OS.SetThemeBackground ((short) OS.kThemeBrushDialogBackgroundActive, (short) inDrawDepth, inDrawInColor != 0);
			}
			return OS.noErr;
		}
	}
	return OS.eventNotHandledErr;
}

int callFocusEventHandler (int nextHandler, int theEvent) {
	return OS.CallNextEventHandler (nextHandler, theEvent);
}

void checkBackground () {
	Shell shell = getShell ();
	if (this == shell) return;
	state &= ~PARENT_BACKGROUND;	
	Composite composite = parent;
	do {
		int mode = composite.backgroundMode;
		if (mode != 0) {
			if (mode == SWT.INHERIT_DEFAULT) {
				Control control = this;
				do {
					if ((control.state & THEME_BACKGROUND) == 0) {
						return;
					}
					control = control.parent;
				} while (control != composite);
			}
			state |= PARENT_BACKGROUND;					
			return;
		}
		if (composite == shell) break;
		composite = composite.parent;
	} while (true);
}

void checkBuffered () {
	style |= SWT.DOUBLE_BUFFERED;
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
 * @see #pack(boolean)
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
 * @see #pack(boolean)
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

boolean contains (String [] array, String element) {
	for (int i = 0; i < array.length; i++) {
		if (array [i].equals (element)) {
			return true;
		}
	}
	return false;
}

void createWidget () {
	state |= DRAG_DETECT;
	checkOrientation (parent);
	super.createWidget ();
	checkBackground ();
	checkBuffered ();
	setDefaultFont ();
	setZOrder ();
	setRelations ();
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_WIDGET_BACKGROUND);
}

Font defaultFont () {
	byte [] family = new byte [256];
	short [] size = new short [1];
	byte [] style = new byte [1];
	OS.GetThemeFont ((short) defaultThemeFont (), (short) OS.smSystemScript, family, size, style);
	short id = OS.FMGetFontFamilyFromName (family);
	int [] font = new int [1]; 
	OS.FMGetFontFromFontFamilyInstance (id, style [0], font, null);
	return Font.carbon_new (display, OS.FMGetATSFontRefFromFont (font [0]), style [0], size [0]);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_WIDGET_FOREGROUND);
}

int defaultThemeFont () {
	if (display.smallFonts) return OS.kThemeSmallSystemFont;
	return OS.kThemeSystemFont;
}

void deregister () {
	super.deregister ();
	display.removeWidget (handle);
}

void destroyWidget () {
	Display display = this.display;
	int theControl = topHandle ();
	releaseHandle ();
	if (theControl != 0) {
		if (display.delayDispose) {
			display.addToDisposeWindow (theControl);
		} else {
			OS.DisposeControl (theControl);
		}
	}
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 * 
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 * 
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @see #getDragDetect
 * @see #setDragDetect
 * 
 * @since 3.3
 */
public boolean dragDetect (Event event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 * 
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 * 
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @see #getDragDetect
 * @see #setDragDetect
 * 
 * @since 3.3
 */
public boolean dragDetect (MouseEvent event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

boolean dragDetect (int button, int count, int stateMask, int x, int y) {
	if (button != 1 || count != 1) return false;
	if (!dragDetect (x, y, false, null)) return false;
	return sendDragEvent (button, stateMask, x, y);
}

boolean dragDetect (int x, int y, boolean filter, boolean [] consume) {
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	CGPoint pt = new CGPoint ();
	OS.HIViewConvertPoint (pt, handle, 0);
	x += (int) pt.x;
	y += (int) pt.y;
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
	x += rect.left;
	y += rect.top;
	org.eclipse.swt.internal.carbon.Point pt1 = new org.eclipse.swt.internal.carbon.Point ();
	pt1.h = (short) x;
	pt1.v = (short) y;
	return OS.WaitMouseMoved (pt1);
}

void drawFocus (int control, int context, boolean hasFocus, boolean hasBorder, boolean drawBackground, Rect inset) {
	if (drawBackground) fillBackground (control, context, null);
	CGRect rect = new CGRect ();
	OS.HIViewGetBounds (control, rect);
	rect.x += inset.left;
	rect.y += inset.top;
	rect.width -= inset.right + inset.left;
	rect.height -= inset.bottom + inset.top;
	int state;
	if (OS.IsControlEnabled (control)) {
		state = OS.IsControlActive (control) ? OS.kThemeStateActive : OS.kThemeStateInactive;
	} else {
		state = OS.IsControlActive (control) ? OS.kThemeStateUnavailable : OS.kThemeStateUnavailableInactive;
	}
	if (hasBorder) {
		HIThemeFrameDrawInfo info = new HIThemeFrameDrawInfo ();
		info.state = state;
		info.kind = OS.kHIThemeFrameTextFieldSquare;
		info.isFocused = hasFocus;
		OS.HIThemeDrawFrame (rect, info, context, OS.kHIThemeOrientationNormal);
	} else {
		OS.HIThemeDrawFocusRect (rect, hasFocus, context, OS.kHIThemeOrientationNormal);
	}
}

boolean drawFocusRing () {
	return hasBorder ();
}

boolean drawGripper (int x, int y, int width, int height, boolean vertical) {
	return false;
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	if (control != handle) return;
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return;

	/* Retrieve the damage rect */
	Rect rect = new Rect ();
	OS.GetRegionBounds (visibleRgn, rect);

	/* Send paint event */
	int [] port = new int [1];
	OS.GetPort (port);
	GCData data = new GCData ();
	data.port = port [0];
	data.paintEvent = theEvent;
	data.visibleRgn = visibleRgn;
	GC gc = GC.carbon_new (this, data);
	Event event = new Event ();
	event.gc = gc;
	event.x = rect.left;
	event.y = rect.top;
	event.width = rect.right - rect.left;
	event.height = rect.bottom - rect.top;
	sendEvent (SWT.Paint, event);
	event.gc = null;
	gc.dispose ();
}

void enableWidget (boolean enabled) {
	int topHandle = topHandle ();
	if (enabled) {
		OS.EnableControl (topHandle);
	} else {
		OS.DisableControl (topHandle);
	}
}

boolean equals(float[] color1, float[] color2) {
	if (color1 == color2) return true;
	if (color1 == null) return color2 == null;
	if (color2 == null) return color1 == null;	
	for (int i = 0; i < color1.length; i++) {
		if (color1 [i] != color2 [i]) return false;
	}	
	return true;
}

void fillBackground (int control, int context, Rectangle bounds) {
	OS.CGContextSaveGState (context);
	CGRect rect = new CGRect ();
	if (bounds != null) {
		rect.x = bounds.x;
		rect.y = bounds.y;
		rect.width = bounds.width;
		rect.height = bounds.height;
	} else {
		OS.HIViewGetBounds (control, rect);
	}
	Control widget = findBackgroundControl ();
	if (widget != null && widget.backgroundImage != null) {
		CGPoint pt = new CGPoint();
		OS.HIViewConvertPoint (pt, control, widget.handle);
		OS.CGContextTranslateCTM (context, -pt.x, -pt.y);
		Pattern pattern = new Pattern (display, widget.backgroundImage);
		GCData data = new GCData ();
		data.device = display;
		data.background = widget.getBackgroundColor ().handle;
		GC gc = GC.carbon_new (context, data);
		gc.setBackgroundPattern (pattern);
		gc.fillRectangle ((int) (rect.x + pt.x), (int) (rect.y + pt.y), (int) rect.width, (int) rect.height);
		gc.dispose ();
		pattern.dispose();
	} else if (widget != null && widget.background != null) {
		int colorspace = OS.CGColorSpaceCreateDeviceRGB ();
		OS.CGContextSetFillColorSpace (context, colorspace);
		OS.CGContextSetFillColor (context, widget.background);
		OS.CGColorSpaceRelease (colorspace);
		OS.CGContextSetAlpha (context, getThemeAlpha ());
		OS.CGContextFillRect (context, rect);
	} else {
		if (OS.VERSION >= 0x1040) {
			OS.HIThemeSetFill (OS.kThemeBrushDialogBackgroundActive, 0, context, OS.kHIThemeOrientationNormal);
			OS.CGContextSetAlpha (context, getThemeAlpha ());
			OS.CGContextFillRect (context, rect);
		} else {
			Rect rect1 = new Rect ();
			rect1.left = (short) rect.x;
			rect1.top = (short) rect.y;
			rect1.right = (short) (rect.x + rect.width);
			rect1.bottom = (short) (rect.y + rect.height);
			OS.SetThemeBackground ((short) OS.kThemeBrushDialogBackgroundActive, (short) 0, true);
			OS.EraseRect (rect1);
		}
	}
	OS.CGContextRestoreGState (context);
}

Cursor findCursor () {
	if (cursor != null) return cursor;
	return parent.findCursor ();
}

Control findBackgroundControl () {
	if (backgroundImage != null || background != null) return this;
	return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl () : null;
}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

void fixFocus (Control focusControl) {
	Shell shell = getShell ();
	Control control = this;
	while (control != shell && (control = control.parent) != null) {
		if (control.setFocus ()) return;
	}
	shell.setSavedFocus (focusControl);
	int window = OS.GetControlOwner (handle);
	OS.ClearKeyboardFocus (window);
}

int focusHandle () {
	return handle;
}

int focusPart () {
	return OS.kControlFocusNextPart;
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
	if (display.focusEvent == SWT.FocusOut) return false;
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible ()/* || !isActive ()*/) return false;
	if (isFocusControl ()) return true;
	shell.setSavedFocus (null);
	shell.bringToTop (false);
	if (isDisposed ()) return false;
	/*
	* Feature in the Macintosh.  SetKeyboardFocus() sends kEventControlSetFocusPart
	* with the part code equal to kControlFocusNoPart to the control that is about
	* to lose focus and then sends kEventControlSetFocusPart with part code equal
	* to kControlFocusNextPart to this control (the one that is about to get focus).
	* If the control does not accept focus because of the full keyboard access mode,
	* kEventControlSetFocusPart is sent again to the control in focus causing multiple
	* focus events to happen.  The fix is to ignore focus events and issue them only
	* if the focus control changed.
	*/
	int focusHandle = focusHandle ();
	int window = OS.GetControlOwner (focusHandle);
	Control oldFocus = display.getFocusControl (window, true);
	if (oldFocus == this) return true;
	display.ignoreFocus = true;
	OS.SetKeyboardFocus (window, focusHandle, (short) focusPart ());
	display.ignoreFocus = false;
	Control newFocus = display.getFocusControl ();
	if (oldFocus != newFocus) {
		if (oldFocus != null && !oldFocus.isDisposed ()) oldFocus.sendFocusEvent (SWT.FocusOut, false);
		if (newFocus != null && !newFocus.isDisposed () && newFocus.isEnabled ()) newFocus.sendFocusEvent (SWT.FocusIn, false);
	}
	if (isDisposed ()) return false;
	shell.setSavedFocus (this);
	return hasFocus ();
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
	if (accessible == null) accessible = new_Accessible (this);
	return accessible;
}
	
String [] getAxAttributes () {
	return null;
}

/**
 * Returns the receiver's background color.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on some versions of Windows the background of a TabFolder,
 * is a gradient rather than a solid color.
 * </p>
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.getBackgroundColor ();
}

Color getBackgroundColor () {
	return background != null ? Color.carbon_new (display, background) : defaultBackground ();
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Image getBackgroundImage () {
	checkWidget();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.backgroundImage;
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
    return 0;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a shell. In this case, the location is
 * relative to the display.
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
	return getControlBounds (topHandle ());
}

/**
 * Returns <code>true</code> if the receiver is detecting
 * drag gestures, and  <code>false</code> otherwise. 
 *
 * @return the receiver's drag detect state
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public boolean getDragDetect () {
	checkWidget ();
	return (state & DRAG_DETECT) != 0;
}

boolean getDrawing () {
	return drawCount <= 0;
}

/**
 * Returns the receiver's cursor, or null if it has not been set.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @return the receiver's cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public Cursor getCursor () {
	checkWidget();
	return cursor;
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
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
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
	return font != null ? font : defaultFont ();
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
	return getForegroundColor ();
}

Color getForegroundColor () {
	return foreground != null ? Color.carbon_new (display, foreground) : defaultForeground ();
}

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
 * to its parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
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
	Rectangle rect = getControlBounds (topHandle ());
	return new Point (rect.x, rect.y);
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

int getMininumHeight () {
	return 0;
}

/**
 * Returns the receiver's monitor.
 * 
 * @return the receiver's monitor
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget();
	Monitor [] monitors = display.getMonitors ();
	if (monitors.length == 1) return monitors [0];
	int index = -1, value = -1;
	Rectangle bounds = getBounds ();
	if (this != getShell ()) {
		bounds = display.map (this.parent, null, bounds);
	}
	for (int i=0; i<monitors.length; i++) {
		Rectangle rect = bounds.intersection (monitors [i].getBounds ());
		int area = rect.width * rect.height;
		if (area > 0 && area > value) {
			index = i;
			value = area;
		}
	}
	if (index >= 0) return monitors [index];
	int centerX = bounds.x + bounds.width / 2, centerY = bounds.y + bounds.height / 2;
	for (int i=0; i<monitors.length; i++) {
		Rectangle rect = monitors [i].getBounds ();
		int x = centerX < rect.x ? rect.x - centerX : centerX > rect.x + rect.width ? centerX - rect.x - rect.width : 0;
		int y = centerY < rect.y ? rect.y - centerY : centerY > rect.y + rect.height ? centerY - rect.y - rect.height : 0;
		int distance = x * x + y * y;
		if (index == -1 || distance < value) {
			index = i;
			value = distance;
		} 
	}
	return monitors [index];
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

float [] getParentBackground () {
	return parent.background;
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
 * Returns the region that defines the shape of the control,
 * or null if the control has the default shape.
 *
 * @return the region that defines the shape of the shell (or null)
 *	
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public Region getRegion () {
	checkWidget ();
	return region;
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
	return getControlSize (topHandle ());
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

float getThemeAlpha () {
	return 1 * parent.getThemeAlpha ();
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
	return (state & HIDDEN) == 0;
}

int getVisibleRegion (int control, boolean clipChildren) {
	if (!clipChildren) return super.getVisibleRegion (control, clipChildren);
	if (visibleRgn == 0) {
		visibleRgn = OS.NewRgn ();
		calculateVisibleRegion (control, visibleRgn, clipChildren);
	}
	int result = OS.NewRgn ();
	OS.CopyRgn (visibleRgn, result);
	return result;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

boolean hasFocus () {
	return this == display.getFocusControl ();
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
    switch (inRequest) {
		case OS.kHMSupplyContent: {
			short [] contentProvided = {OS.kHMContentNotProvidedDontPropagate};
			if (toolTipText != null && toolTipText.length () != 0) {
				char [] buffer = new char [toolTipText.length ()];
				toolTipText.getChars (0, buffer.length, buffer, 0);
				int length = fixMnemonic (buffer);
				if (display.helpString != 0) OS.CFRelease (display.helpString);
				display.helpString = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
				HMHelpContentRec helpContent = new HMHelpContentRec ();
				OS.memmove (helpContent, ioHelpContent, HMHelpContentRec.sizeof);
				helpContent.version = OS.kMacHelpVersion;

				/*
				* Feature in the Macintosh.  Despite the fact that the Mac
				* provides 23 different types of alignment for the help text,
				* it does not allow the text to be positioned at the current
				* mouse position.  The fix is to center the text in a rectangle
				* that surrounds the original position of the mouse.  As the
				* mouse is moved, this rectangle is grown to include the new
				* location of the mouse.  The help text is then centered by
				* the  Mac in the new rectangle that was carefully constructed
				* such that the help text will stay in the same position.
				*/
				int cursorHeight = 16;
				helpContent.tagSide = (short) OS.kHMAbsoluteCenterAligned;
				org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
				OS.memmove(pt, new int[] {inGlobalMouse}, 4);
				int x = pt.h;
				int y = pt.v;
				if (display.helpWidget != this) {
					display.lastHelpX = x + cursorHeight / 2;
					display.lastHelpY = y + cursorHeight + cursorHeight / 2;			
				}
				int jitter = 4;
				int deltaX = Math.abs (display.lastHelpX - x) + jitter;
				int deltaY = Math.abs (display.lastHelpY - y) + jitter;
				x = display.lastHelpX - deltaX;
				y = display.lastHelpY - deltaY;
				int width = deltaX * 2;
				int height = deltaY * 2;
				display.helpWidget = this;
				helpContent.absHotRect_left = (short) x;
				helpContent.absHotRect_top = (short) y;
				helpContent.absHotRect_right = (short) (x + width);
				helpContent.absHotRect_bottom = (short) (y + height);

				helpContent.content0_contentType = OS.kHMCFStringContent;
				helpContent.content0_tagCFString = display.helpString;
				helpContent.content1_contentType = OS.kHMCFStringContent;
				helpContent.content1_tagCFString = display.helpString;
				OS.memmove (ioHelpContent, helpContent, HMHelpContentRec.sizeof);
				contentProvided [0] = OS.kHMContentProvided;
				OS.memmove (outContentProvided, contentProvided, 2);
				break;
			} else {
				OS.HMHideTag();
				OS.memmove (outContentProvided, contentProvided, 2);
				/*
				 * If a control doesn't have a tooltiptext, then helpProc gets
				 * called on the controls in its parent hierarchy and their
				 * tooltiptext is used. Return OS.eventNotHandledErr to prevent
				 * the calls on the parent control.
				 */
				return OS.eventNotHandledErr;
			}
		}
		case OS.kHMDisposeContent: {
			if (display.helpString != 0) OS.CFRelease (display.helpString);
			display.helpWidget = null;
			display.helpString = 0;
			break;
		}
	}
	return OS.noErr;
}

void hookEvents () {
	super.hookEvents ();
	int controlProc = display.controlProc;
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlActivate,
		OS.kEventClassControl, OS.kEventControlApplyBackground,
		OS.kEventClassControl, OS.kEventControlBoundsChanged,
		OS.kEventClassControl, OS.kEventControlClick,
		OS.kEventClassControl, OS.kEventControlContextualMenuClick,
		OS.kEventClassControl, OS.kEventControlDeactivate,
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlGetClickActivation,
		OS.kEventClassControl, OS.kEventControlGetFocusPart,
		OS.kEventClassControl, OS.kEventControlGetPartRegion,
		OS.kEventClassControl, OS.kEventControlHit,
		OS.kEventClassControl, OS.kEventControlHitTest,
		OS.kEventClassControl, OS.kEventControlSetCursor,
		OS.kEventClassControl, OS.kEventControlSetFocusPart,
		OS.kEventClassControl, OS.kEventControlTrack,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, handle, null);
	int accessibilityProc = display.accessibilityProc;
	mask = new int [] {
		OS.kEventClassAccessibility, OS.kEventAccessibleGetChildAtPoint,
		OS.kEventClassAccessibility, OS.kEventAccessibleGetFocusedChild,
		OS.kEventClassAccessibility, OS.kEventAccessibleGetAllAttributeNames,
		OS.kEventClassAccessibility, OS.kEventAccessibleGetNamedAttribute,
	};
	OS.InstallEventHandler (controlTarget, accessibilityProc, mask.length / 2, mask, handle, null);
	int helpProc = display.helpProc;
	OS.HMInstallControlContentCallback (handle, helpProc);
	int colorProc = display.colorProc;
	OS.SetControlColorProc (handle, colorProc);
	if (OS.GetControlAction (handle) == 0) {
		OS.SetControlAction (handle, display.actionProc);
	}
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
 */
public int internal_new_GC (GCData data) {
	checkWidget();
	int window = OS.GetControlOwner (handle);
	int port = data != null ? data.port : 0;
	if (port == 0) port = OS.GetWindowPort (window);
	int context;
	int [] buffer = new int [1];
	boolean isPaint = data != null && data.paintEvent != 0; 
	if (isPaint) {
		OS.GetEventParameter (data.paintEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, buffer);
	} else {
		OS.CreateCGContextForPort (port, buffer);
	}
	context = buffer [0];
	if (context == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int visibleRgn = 0;
	if (data != null && data.paintEvent != 0) {
		visibleRgn = data.visibleRgn;
	} else {
		if (isDrawing()) {
			visibleRgn = getVisibleRegion (handle, true);
		} else {
			visibleRgn = OS.NewRgn ();
		}
	}
	Rect rect = new Rect ();
	Rect portRect = new Rect ();
	OS.GetControlBounds (handle, rect);
	OS.GetPortBounds (port, portRect);
	if (isPaint) {
		rect.right += rect.left;
		rect.bottom += rect.top;
		rect.left = rect.top = 0;
	} else {
		int [] contentView = new int [1];
		OS.HIViewFindByID (OS.HIViewGetRoot (window), OS.kHIViewWindowContentID (), contentView);
		CGPoint pt = new CGPoint ();
		OS.HIViewConvertPoint (pt, OS.HIViewGetSuperview (handle), contentView [0]);
		rect.left += (int) pt.x;
		rect.top += (int) pt.y;
		rect.right += (int) pt.x;
		rect.bottom += (int) pt.y;
		OS.ClipCGContextToRegion (context, portRect, visibleRgn);
		int portHeight = portRect.bottom - portRect.top;
		OS.CGContextScaleCTM (context, 1, -1);
		OS.CGContextTranslateCTM (context, rect.left, -portHeight + rect.top);
	}
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.device = display;
		data.thread = display.thread;
		data.foreground = getForegroundColor ().handle;
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		data.background = control.getBackgroundColor ().handle;
		data.font = font != null ? font : defaultFont ();
		data.visibleRgn = visibleRgn;
		data.control = handle;
		data.portRect = portRect;
		data.controlRect = rect;
		data.insetRect = getInset ();
	
		if (data.paintEvent == 0) {
			if (gcs == null) gcs = new GCData [4];
			int index = 0;
			while (index < gcs.length && gcs [index] != null) index++;
			if (index == gcs.length) {
				GCData [] newGCs = new GCData [gcs.length + 4];
				System.arraycopy (gcs, 0, newGCs, 0, gcs.length);
				gcs = newGCs;
			}
			gcs [index] = data;
		}
	}
	return context;
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
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (int context, GCData data) {
	checkWidget ();
	if (data != null) {
		if (data.paintEvent == 0) {
			if (data.visibleRgn != 0) {
				OS.DisposeRgn (data.visibleRgn);
				data.visibleRgn = 0;
			}
			
			int index = 0;
			while (index < gcs.length && gcs [index] != data) index++;
			if (index < gcs.length) {
				gcs [index] = null;
				index = 0;
				while (index < gcs.length && gcs [index] == null) index++;
				if (index == gcs.length) gcs = null;
			}
		} else {
			return;
		}
	}
	
	/*
	* This code is intentionally commented. Use CGContextSynchronize
	* instead of CGContextFlush to improve performance.
	*/
//	OS.CGContextFlush (context);
	OS.CGContextSynchronize (context);
	OS.CGContextRelease (context);
}

void invalidateChildrenVisibleRegion (int control) {
}

void invalidateVisibleRegion (int control) {
	int index = 0;
	Control[] siblings = parent._getChildren ();
	while (index < siblings.length && siblings [index] != this) index++;
	for (int i=index; i<siblings.length; i++) {
		Control sibling = siblings [i];
		sibling.resetVisibleRegion (control);
		sibling.invalidateChildrenVisibleRegion (control);
	}
	parent.resetVisibleRegion (control);
}

void invalWindowRgn (int window, int rgn) {
	parent.invalWindowRgn (window, rgn);
}

/*
 * Answers a boolean indicating whether a Label that precedes the receiver in
 * a layout should be read by screen readers as the recevier's label.
 */
boolean isDescribedByLabel () {
	return true;
}

boolean isDrawing () {
	return getDrawing() && parent.isDrawing();
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are enabled.  Otherwise, <code>false</code> is returned.
 * A disabled control is typically not selectable from the user
 * interface and draws with an inactive or "grayed" look.
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

boolean isEnabledCursor () {
	return isEnabled ();
}

boolean isEnabledModal () {
	//NOT DONE - fails for multiple APP MODAL shells
	Shell [] shells = display.getShells ();
	for (int i = 0; i < shells.length; i++) {
		Shell modal = shells [i];
		if (modal != this && modal.isVisible ()) {
			if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
				Shell shell = getShell ();
				if (modal.parent == shell) {
					return false;
				}
			}
			int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
			if ((modal.style & bits) != 0) {
				Control control = this;
				while (control != null) {
					if (control == modal) break;
					control = control.parent;
				}
				if (control != modal) return false;
			}
		}
	}
	return true;
}

boolean isFocusAncestor (Control control) {
	while (control != null && control != this && !(control instanceof Shell)) {
		control = control.parent;
	}
	return control == this;
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
	Control focusControl = display.focusControl;
	if (focusControl != null && !focusControl.isDisposed ()) {
		return this == focusControl;
	}
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
	return true;
}

boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscurred by a parent or sibling.
	*/
	if (!isVisible ()) return false;
	Control control = this;
	while (control != null) {
		Point size = control.getSize ();
		if (size.x == 0 || size.y == 0) {
			return false;
		}
		control = control.parent;
	}
	return true;
}

boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	int code = traversalCode (0, 0);
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
	int code = traversalCode (0, 0);
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are visible. Otherwise, <code>false</code> is returned.
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

Decorations menuShell () {
	return parent.menuShell ();
}

int kEventAccessibleGetChildAtPoint (int nextHandler, int theEvent, int userData) {
	if (accessible != null) {
		return accessible.internal_kEventAccessibleGetChildAtPoint (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int kEventAccessibleGetFocusedChild (int nextHandler, int theEvent, int userData) {
	if (accessible != null) {
		return accessible.internal_kEventAccessibleGetFocusedChild (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int kEventAccessibleGetAllAttributeNames (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	String [] attributes = getAxAttributes ();
	if (attributes != null) {
		OS.CallNextEventHandler (nextHandler, theEvent);
		int [] arrayRef = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeNames, OS.typeCFMutableArrayRef, null, 4, null, arrayRef);
		int attributesArrayRef = arrayRef [0];
		int length = OS.CFArrayGetCount (attributesArrayRef);
		String[] osAttributes = new String [length];
		for (int i = 0; i < length; i++) {
			int stringRef = OS.CFArrayGetValueAtIndex (attributesArrayRef, i);
			int strLength = OS.CFStringGetLength (stringRef);
			char [] buffer = new char [strLength];
			CFRange range = new CFRange ();
			range.length = strLength;
			OS.CFStringGetCharacters (stringRef, range, buffer);
			osAttributes [i] = new String (buffer);
		}
		for (int i = 0; i < attributes.length; i++) {
			if (!contains (osAttributes, attributes [i])) {
				String string = attributes [i];
				char [] buffer = new char [string.length ()];
				string.getChars (0, buffer.length, buffer, 0);
				int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
				OS.CFArrayAppendValue (attributesArrayRef, stringRef);
				OS.CFRelease (stringRef);
			}
		}
		code = OS.noErr;
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetAllAttributeNames (nextHandler, theEvent, code);
	}
	return code;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	if (accessible != null) {
		return accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, OS.eventNotHandledErr);
	}
	return OS.eventNotHandledErr;
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	Widget widget = display.getWidget (theControl [0]);
	while (widget != null && !(widget instanceof Control)) {
		OS.GetSuperControl (theControl [0], theControl);
		widget = display.getWidget (theControl [0]);
	}
	if (widget == this && isEnabled ()) {
		int x, y;
		Rect rect = new Rect ();
		int window = OS.GetControlOwner (handle);
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		x = (int) pt.x;
		y = (int) pt.y;
		OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
		x += rect.left;
		y += rect.top;
		Event event = new Event ();
		event.x = x;
		event.y = y;
		sendEvent (SWT.MenuDetect, event);
		// widget could be disposed at this point
		if (isDisposed ()) return OS.noErr;
		if (event.doit) {
			if (menu != null && !menu.isDisposed ()) {
				if (event.x != x || event.y != y) {
					menu.setLocation (event.x, event.y);
				}
				menu.setVisible (true);
			}
		}
	}
	return OS.eventNotHandledErr;
}

int kEventControlGetClickActivation (int nextHandler, int theEvent, int userData) {
	if ((getShell ().style & SWT.ON_TOP) != 0) {
		OS.SetEventParameter (theEvent, OS.kEventParamClickActivation, OS.typeClickActivationResult, 4, new int [] {OS.kActivateAndHandleClick});
		return OS.noErr;
	}
	return super.kEventControlGetClickActivation (nextHandler, theEvent, userData);
}

int kEventControlGetPartRegion (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlGetPartRegion (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (region != null && this != getShell ()) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode , null, 2, null, part);
		if (part [0] == OS.kControlStructureMetaPart || part [0] == OS.kControlClickableMetaPart) {
			int [] rgn = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamControlRegion, OS.typeQDRgnHandle , null, 4, null, rgn);
			OS.CopyRgn (region.handle, rgn[0]);
			Rect rect = getInset ();
			OS.OffsetRgn (rgn [0], (short)-rect.left, (short)-rect.top);
			return OS.noErr;
		}
	}
	return result;
}

int kEventControlHitTest (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHitTest (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (region != null && this != getShell ()) {
		result = OS.CallNextEventHandler (nextHandler, theEvent);
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		if (!region.contains ((int) pt.x, (int) pt.y)) {
			OS.SetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, 2, new short []{0});
			return result;
		}
	}
	if ((state & GRAB) != 0) {
		CGRect rect = new CGRect ();
		OS.HIViewGetBounds (handle, rect);
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		if (OS.CGRectContainsPoint (rect, pt) != 0) {
			OS.SetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, 2, new short[]{1});
		}
		return OS.noErr;
	}
	return result;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	if (!isEnabledCursor ()) return OS.noErr;
	Cursor cursor = null;
	if (isEnabledModal ()) {
		if ((cursor = findCursor ()) != null) display.setCursor (cursor.handle);
	}
	return cursor != null ? OS.noErr : OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	display.focusCombo = null;
	int result = callFocusEventHandler (nextHandler, theEvent);
	if (!display.ignoreFocus) {
		if (result == OS.noErr) {
			int window = OS.GetControlOwner (handle);
			if (window == OS.GetUserFocusWindow ()) {
				int focusHandle = focusHandle ();
				int [] focusControl = new int [1];
				OS.GetKeyboardFocus (window, focusControl);
				short [] part = new short [1];
				OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
				Display display = this.display;
				display.delayDispose = true;
				if (part [0] == OS.kControlFocusNoPart) {
					if (focusControl [0] == focusHandle) sendFocusEvent (SWT.FocusOut, false);
				} else {
					if (focusControl [0] != focusHandle) sendFocusEvent (SWT.FocusIn, false);
				}
				display.delayDispose = false;
			}
			// widget could be disposed at this point
			if (isDisposed ()) return OS.noErr;
		}
	}
	return result;
}	

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh.  The default handler of kEventControlTrack
	* calls TrackControl() which consumes key and mouse events until the
	* tracking is canceled.  The fix is to send those events from the
	* action proc of the widget by diffing the mouse and modifier keys
	* state.
	*/
	Display display = this.display;	
//	display.runDeferredEvents ();
	if (isDisposed ()) return OS.noErr;
	if (display.runPopups ()) return OS.noErr;
	if (isDisposed ()) return OS.noErr;
	display.lastState = OS.GetCurrentEventButtonState ();
	display.lastModifiers = OS.GetCurrentEventKeyModifiers ();
	display.grabControl = this; 
	int timer = 0;
	if (pollTrackEvent ()) {
		if (display.pollingTimer == 0) {
			int [] id = new int [1];
			int eventLoop = OS.GetCurrentEventLoop ();
			OS.InstallEventLoopTimer (eventLoop, Display.POLLING_TIMEOUT / 1000.0, Display.POLLING_TIMEOUT / 1000.0, display.pollingProc, 0, id);
			display.pollingTimer = timer = id [0];
		}
	}
	int result = super.kEventControlTrack (nextHandler, theEvent, userData);
	if (timer != 0) {
		OS.RemoveEventLoopTimer (timer);
		display.pollingTimer = 0;
	}
	display.grabControl = null;
	if (isDisposed ()) return OS.noErr;
	sendTrackEvents ();
	return result;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	Shell shell = getShell ();
	display.dragging = false;
	boolean [] consume = new boolean [1];
	short [] button = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
	int [] clickCount = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamClickCount, OS.typeUInt32, null, 4, null, clickCount);
	if (button [0] == 1 && clickCount [0] == 1 && (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
		OS.HIViewConvertPoint (pt, 0, handle);
		int x = (int) pt.x;
		int y = (int) pt.y;
		if (dragDetect (x, y, true, consume)) {
			display.dragging = true;
			display.dragButton = button [0];
			display.dragX = x;
			display.dragY = y;
			int [] chord = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamMouseChord, OS.typeUInt32, null, 4, null, chord);
			display.dragState = chord [0];
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			display.dragModifiers = modifiers [0];
		}
		if (isDisposed ()) return OS.noErr;
	}
	if (!sendMouseEvent (SWT.MouseDown, button [0], display.clickCount, 0, false, theEvent)) consume [0] = true;
	if (isDisposed ()) return OS.noErr;
	if (display.clickCount == 2) {
		if (!sendMouseEvent (SWT.MouseDoubleClick, button [0], display.clickCount, 0, false, theEvent)) consume [0] = true;
		if (isDisposed ()) return OS.noErr;
	}
	if (!shell.isDisposed ()) shell.setActiveControl (this);
	return consume [0] ? OS.noErr : OS.eventNotHandledErr;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	if (isEnabledModal ()) {
		if (display.dragging) {
			display.dragging = false;
			sendDragEvent (display.dragButton, display.dragState, display.dragModifiers, display.dragX, display.dragY);
			if (isDisposed ()) return OS.noErr;
		}
		int result = sendMouseEvent (SWT.MouseMove, (short) 0, 0, 0, false, theEvent) ? OS.eventNotHandledErr : OS.noErr;
		if (isDisposed ()) return OS.noErr;
		return result;
	}
	return OS.eventNotHandledErr;
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	if (isEnabledModal ()) {
		return sendMouseEvent (SWT.MouseMove, (short) 0, 0, 0, false, theEvent) ? OS.eventNotHandledErr : OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	short [] button = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
	return sendMouseEvent (SWT.MouseUp, button [0], display.clickCount, 0, false, theEvent) ? OS.eventNotHandledErr : OS.noErr;
}

int kEventMouseWheelMoved (int nextHandler, int theEvent, int userData) {
	if ((state & IGNORE_WHEEL) != 0) return OS.eventNotHandledErr;
	
	/*
	* Bug in the Macintosh.  Mouse Wheel events are still processed when the
	* window is not enabled due to a modal dialog.  The fix is to not let the
	* default handlers run when window is modal disabled.
	*/
	int window = OS.GetControlOwner (handle);
	if (OS.HIWindowIsDocumentModalTarget (window, null)) {
		return OS.noErr;
	}	
	int[] event = new int[1];
	OS.CreateEvent (0, OS.kEventClassWindow, OS.kEventWindowGetClickModality, 0.0, 0, event);
	if (event [0] != 0) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamWindowPartCode, OS.typeWindowPartCode, null, 2, null, part);
		int [] modifiers = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, modifiers.length * 4, null, modifiers);
		OS.SetEventParameter (event [0], OS.kEventParamDirectObject, OS.typeWindowRef, 4, new int[]{window});
		OS.SetEventParameter (event [0], OS.kEventParamWindowPartCode, OS.typeWindowPartCode, 2, part);
		OS.SetEventParameter (event [0], OS.kEventParamKeyModifiers, OS.typeUInt32, 4, modifiers);
		OS.SetEventParameter (event [0], OS.kEventParamEventRef, OS.typeEventRef, 4, new int[]{theEvent});
		OS.SendEventToEventTarget (event [0], OS.GetApplicationEventTarget ());
		int [] clickResult = new int [1];
		OS.GetEventParameter (event [0], OS.kEventParamModalClickResult, OS.typeModalClickResult, null, 4, null, clickResult);
		OS.ReleaseEvent (event [0]);
		if ((clickResult [0] & OS.kHIModalClickIsModal) != 0) {
			if ((clickResult [0] & OS.kHIModalClickAllowEvent) == 0) {
				return OS.noErr;
			}
		}
	}
	
	short [] wheelAxis = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseWheelAxis, OS.typeMouseWheelAxis, null, 2, null, wheelAxis);
	int [] wheelDelta = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseWheelDelta, OS.typeSInt32, null, 4, null, wheelDelta);
	Shell shell = getShell ();
	Control control = this;
	int type = wheelAxis [0] == OS.kEventMouseWheelAxisY ? SWT.MouseWheel : SWT.MouseHorizontalWheel;
	while (control != null) {
		if (!control.sendMouseEvent (type, (short) 0, wheelDelta [0], SWT.SCROLL_LINE, true, theEvent)) {
			break;
		}
		if (control.sendMouseWheel (wheelAxis [0], wheelDelta [0])) break;
		if (control == this) {
			/*
			* Feature in the Macintosh.  For some reason, the kEventMouseWheelMoved
			* event is sent twice to each application handler with the same mouse wheel
			* data.  The fix is to set an ignore flag before calling the next handler 
			* in the handler chain.
			*/
			state |= IGNORE_WHEEL;
			int result = OS.CallNextEventHandler (nextHandler, theEvent);
			state &= ~IGNORE_WHEEL;
			if (result == OS.noErr) break;
		}
		if (control == shell) break;
		control = control.parent;
	}
	return OS.noErr;
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	boolean [] consume = new boolean [1];
	if (translateTraversal (keyCode [0], keyboardEvent [0], consume)) return OS.noErr;
	if (isDisposed ()) return OS.noErr;	
	if (keyCode [0] == 114) { /* Help */
		Control control = this;
		while (control != null) {
			if (control.hooks (SWT.Help)) {
				control.postEvent (SWT.Help);
				break;
			}
			control = control.parent;
		}
	}
	int result = kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr || consume [0]) return OS.noErr;
	/*
	* Feature in the Macintosh.  If the focus target is changed
	* before the default handler for the widget has run, the key
	* goes to the new focus widget.  The fix is to explicitly
	* send the event to the original focus widget and stop
	* the chain of handlers.
	*/
	if (!isDisposed ()) {
		Control focusControl = display.getFocusControl ();
		if (focusControl != this) {
			int window = OS.GetControlOwner (handle), newWindow = 0;
			if (focusControl != null) {
				newWindow = OS.GetControlOwner (focusControl.handle);
			}
			display.ignoreFocus = true;
			if (window != newWindow) OS.SetUserFocusWindow (window);
			OS.SetKeyboardFocus (window, focusHandle (), (short) focusPart ());
			display.ignoreFocus = false;
			result = OS.CallNextEventHandler (nextHandler, theEvent);
			display.ignoreFocus = true;
			if (window != newWindow && newWindow != 0) OS.SetUserFocusWindow (newWindow);
			if (window == newWindow && focusControl != null) {
				OS.SetKeyboardFocus (window, focusControl.focusHandle (), (short) focusControl.focusPart ());
			} else {
				OS.ClearKeyboardFocus (window);
			}
			display.ignoreFocus = false;
			return OS.noErr;
		}
	}
	return result;
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	if (!sendKeyEvent (SWT.KeyDown, keyboardEvent [0])) return OS.noErr;
	return OS.eventNotHandledErr;
}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveBelow
 * @see Composite#getChildren
 */
public void moveAbove (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, true);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveAbove
 * @see Composite#getChildren
 */
public void moveBelow (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, false);
}

Accessible new_Accessible (Control control) {
	return Accessible.internal_new_Accessible (this);
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
 * @see #computeSize(int, int, boolean)
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
 * @param changed whether or not the receiver's contents have changed
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack (boolean changed) {
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

/**
 * Prints the receiver and all children.
 * 
 * @param gc the gc where the drawing occurs
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] outImage = new int [1];
	CGRect outFrame = new CGRect ();
	if (OS.HIViewCreateOffscreenImage (handle, 0, outFrame, outImage) == OS.noErr) {
		int width = OS.CGImageGetWidth (outImage [0]);
		int height = OS.CGImageGetHeight (outImage [0]);
	 	CGRect rect = new CGRect();
	 	rect.width = width;
		rect.height = height;
		//TODO - does not draw the browser (cocoa widgets?)
		OS.HIViewDrawCGImage (gc.handle, rect, outImage [0]);
		OS.CGImageRelease (outImage [0]);
	}
	return true;
}

boolean pollTrackEvent() {
	return false;
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw () {
	checkWidget();
	redrawWidget (handle, false);
}

void redraw (boolean children) {
//	checkWidget();
	redrawWidget (handle, children);
}

/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted, including the background.
 * If the <code>all</code> flag is <code>true</code>, any
 * children of the receiver which intersect with the specified
 * area will also paint their intersecting areas. If the
 * <code>all</code> flag is <code>false</code>, the children
 * will not be painted.
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
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	redrawWidget (handle, x, y, width, height, all);
}

void register () {
	super.register ();
	display.addWidget (handle, this);
}

void release (boolean destroy) {
	Control next = null, previous = null;
	if (destroy && parent != null) {
		Control[] children = parent._getChildren ();
		int index = 0;
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		if (0 < index && (index + 1) < children.length) {
			next = children [index + 1];
			previous = children [index - 1];
		}
	}
	super.release (destroy);
	if (destroy) {
		if (previous != null) previous.addRelation (next);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
	parent = null;
}

void releaseParent () {
	setVisible (topHandle (), false);
	parent.removeControl (this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	if (visibleRgn != 0) OS.DisposeRgn (visibleRgn);
	visibleRgn = 0;
	menu = null;
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
	region = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * be notified when a drag gesture occurs.
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
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @since 3.3
 */
public void removeDragDetectListener(DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.DragDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * be notified when the mouse wheel is scrolled.
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
 * @see MouseWheelListener
 * @see #addMouseWheelListener
 *
 * @since 3.3
 */
public void removeMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseWheel, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/*
 * Remove "Labeled by" relations from the receiver.
 */
void removeRelation () {
	if (!isDescribedByLabel ()) return;		/* there will not be any */
	String string = OS.kAXTitleUIElementAttribute;
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int stringRef = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	OS.HIObjectSetAuxiliaryAccessibilityAttribute(handle, 0, stringRef, 0);
	OS.CFRelease(stringRef);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

void resetVisibleRegion (int control) {
	if (visibleRgn != 0) {
		OS.DisposeRgn (visibleRgn);
		visibleRgn = 0;
	}
	if (gcs != null) {
		int visibleRgn = getVisibleRegion (handle, true);
		for (int i=0; i<gcs.length; i++) {
			GCData data = gcs [i];
			if (data != null) {
				data.updateClip = true;
				OS.CopyRgn (visibleRgn, data.visibleRgn);
			}
		}
		OS.DisposeRgn (visibleRgn);
	}
	Object runnable = getData (RESET_VISIBLE_REGION);
	if (runnable != null && runnable instanceof Runnable) {
		((Runnable) runnable).run ();
	}
}

boolean sendDragEvent (int button, int stateMask, int x, int y) {
	Event event = new Event ();
	event.button = button;
	event.x = x;
	event.y = y;
	event.stateMask = stateMask;
	postEvent (SWT.DragDetect, event);
	return event.doit;
}

boolean sendDragEvent (int button, int chord, int modifiers, int x, int y) {
	Event event = new Event ();
	switch (button) {
		case 1: event.button = 1; break;
		case 2: event.button = 3; break;
		case 3: event.button = 2; break;
		case 4: event.button = 4; break;
		case 5: event.button = 5; break;
	}
	event.x = x;
	event.y = y;
	setInputState (event, SWT.DragDetect, chord, modifiers);
	postEvent (SWT.DragDetect, event);
	return event.doit;
}

void sendFocusEvent (int type, boolean post) {
	Display display = this.display;
	Shell shell = getShell ();
	/*
	* Feature in the Macintosh.  GetKeyboardFocus() returns NULL during
	* kEventControlSetFocusPart if the focus part is not kControlFocusNoPart.
	* The fix is to remember the focus control and return it during
	* kEventControlSetFocusPart.
	*/
	display.focusControl = this;
	display.focusEvent = type;
	if (post) {
		postEvent (type);
	} else {
		sendEvent (type);
	}	
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/
	if (!shell.isDisposed ()) {
		switch (type) {
			case SWT.FocusIn:
				shell.setActiveControl (this);
				break;
			case SWT.FocusOut:
				if (shell != display.getActiveShell ()) {
					shell.setActiveControl (null);
				}
				break;
		}
	}
	display.focusEvent = SWT.None;
	display.focusControl = null;
}

boolean sendMouseEvent (int type, short button, int count, int detail, boolean send, int theEvent) {
	CGPoint pt = new CGPoint ();
	OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, CGPoint.sizeof, null, pt);
	OS.HIViewConvertPoint (pt, 0, handle);
	int x = (int) pt.x;
	int y = (int) pt.y;
	display.lastX = x;
	display.lastY = y;
	int [] chord = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseChord, OS.typeUInt32, null, 4, null, chord);
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	return sendMouseEvent (type, button, count, detail, send, chord [0], (short) x, (short) y, modifiers [0]);
}

boolean sendMouseEvent (int type, short button, int count, boolean send, int chord, short x, short y, int modifiers) {
	return sendMouseEvent (type, button, count, 0, send, chord, x, y, modifiers);
}

boolean sendMouseEvent (int type, short button, int count, int detail, boolean send, int chord, short x, short y, int modifiers) {
	if (!hooks (type) && !filters (type)) return true;
	if ((state & SAFARI_EVENTS_FIX) != 0) {
		switch (type) {
			case SWT.MouseUp:
			case SWT.MouseMove:
			case SWT.MouseDoubleClick: {
				return true;
			}
			case SWT.MouseDown: {
				if (button == 1) return true;
				break;
			}
		}
	}
	Event event = new Event ();
	switch (button) {
		case 1: event.button = 1; break;
		case 2: event.button = 3; break;
		case 3: event.button = 2; break;
		case 4: event.button = 4; break;
		case 5: event.button = 5; break;
	}
	event.x = x;
	event.y = y;
	event.count = count;
	event.detail = detail;
	setInputState (event, type, chord, modifiers);
	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	return event.doit;
}

boolean sendMouseWheel (short wheelAxis, int wheelDelta) {
	return false;
}

void sendTrackEvents () {
	Display display = this.display;
	display.runDeferredEvents ();
	if (isDisposed ()) return;
	boolean events = false;
	if (display.dragging) {
		display.dragging = false;
		sendDragEvent (display.dragButton, display.dragState, display.dragModifiers, display.dragX, display.dragY);
		if (isDisposed ()) return;
		events = true;
	}
	org.eclipse.swt.internal.carbon.Point outPt = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (outPt);
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	int newX, newY;
	CGPoint pt = new CGPoint ();
	pt.x = outPt.h;
	pt.y = outPt.v;
	OS.HIViewConvertPoint (pt, 0, handle);
	newX = (int) pt.x;
	newY = (int) pt.y;
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
	newX -= rect.left;
	newY -= rect.top;
	int newModifiers = OS.GetCurrentEventKeyModifiers ();
	int newState = OS.GetCurrentEventButtonState ();
	int oldX = display.lastX;
	int oldY = display.lastY;
	int oldState = display.lastState;
	int oldModifiers = display.lastModifiers;
	display.lastX = newX;
	display.lastY = newY;
	display.lastModifiers = newModifiers;
	display.lastState = newState;
	if (newState != oldState) {
		int button = 0, type = SWT.MouseDown;
		if ((oldState & 0x1) == 0 && (newState & 0x1) != 0) button = 1;
		if ((oldState & 0x2) == 0 && (newState & 0x2) != 0) button = 2;
		if ((oldState & 0x4) == 0 && (newState & 0x4) != 0) button = 3;
		if ((oldState & 0x8) == 0 && (newState & 0x8) != 0) button = 4;
		if ((oldState & 0x10) == 0 && (newState & 0x10) != 0) button = 5;
		if (button == 0) {
			type = SWT.MouseUp;
			if ((oldState & 0x1) != 0 && (newState & 0x1) == 0) button = 1;
			if ((oldState & 0x2) != 0 && (newState & 0x2) == 0) button = 2;
			if ((oldState & 0x4) != 0 && (newState & 0x4) == 0) button = 3;
			if ((oldState & 0x8) != 0 && (newState & 0x8) == 0) button = 4;
			if ((oldState & 0x10) != 0 && (newState & 0x10) == 0) button = 5;
		}
		if (button != 0) {
			sendMouseEvent (type, (short)button, 1, false, newState, (short)newX, (short)newY, newModifiers);
			events = true;
		}
	}
	if (newModifiers != oldModifiers && !isDisposed ()) {
		int key = 0, type = SWT.KeyDown;
		if ((newModifiers & OS.alphaLock) != 0 && (oldModifiers & OS.alphaLock) == 0) key = SWT.CAPS_LOCK;
		if ((newModifiers & OS.shiftKey) != 0 && (oldModifiers & OS.shiftKey) == 0) key = SWT.SHIFT;
		if ((newModifiers & OS.controlKey) != 0 && (oldModifiers & OS.controlKey) == 0) key = SWT.CONTROL;
		if ((newModifiers & OS.cmdKey) != 0 && (oldModifiers & OS.cmdKey) == 0)  key = SWT.COMMAND;
		if ((newModifiers & OS.optionKey) != 0 && (oldModifiers & OS.optionKey) == 0) key = SWT.ALT;
		if (key == 0) {
			type = SWT.KeyUp;
			if ((newModifiers & OS.alphaLock) == 0 && (oldModifiers & OS.alphaLock) != 0) key = SWT.CAPS_LOCK;
			if ((newModifiers & OS.shiftKey) == 0 && (oldModifiers & OS.shiftKey) != 0) key = SWT.SHIFT;
			if ((newModifiers & OS.controlKey) == 0 && (oldModifiers & OS.controlKey) != 0) key = SWT.CONTROL;
			if ((newModifiers & OS.cmdKey) == 0 && (oldModifiers & OS.cmdKey) != 0)  key = SWT.COMMAND;
			if ((newModifiers & OS.optionKey) == 0 && (oldModifiers & OS.optionKey) != 0) key = SWT.ALT;
		}
		if (key != 0) {
			Event event = new Event ();
			event.keyCode = key;
			setInputState (event, type, newState, newModifiers);
			sendKeyEvent (type, event);
			events = true;
		}
	}
	if (newX != oldX || newY != oldY && !isDisposed ()) {
		display.mouseMoved = true;
		sendMouseEvent (SWT.MouseMove, (short)0, 0, false, newState, (short)newX, (short)newY, newModifiers);
		events = true;
	}
	if (events) display.runDeferredEvents ();
}

void setBackground () {
	redrawWidget (handle, false);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
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
 */
public void setBackground (Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	float[] background = color != null ? color.handle : null;
	if (equals (background, this.background)) return;
	this.background = background;
	setBackground (background);
	redrawWidget (handle, false);
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
 * </p>
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setBackgroundImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (image == backgroundImage) return;
	backgroundImage = image;
	redrawWidget (handle, false);
}

void setBackground (float [] color) {
	setBackground (handle, color);
}

void setBackground (int control, float [] color) {
	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
	OS.GetControlData (control, (short) OS.kControlEntireControl, OS.kControlFontStyleTag, ControlFontStyleRec.sizeof, fontStyle, null);
	if (color != null) {
		fontStyle.backColor_red = (short) (color [0] * 0xffff);
		fontStyle.backColor_green = (short) (color [1] * 0xffff);
		fontStyle.backColor_blue = (short) (color [2] * 0xffff);
		fontStyle.flags |= OS.kControlUseBackColorMask;
	} else {
		fontStyle.flags &= ~OS.kControlUseBackColorMask;
	}
	OS.SetControlFontStyle (control, fontStyle);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the <code>x</code>
 * and <code>y</code> arguments are relative to the display.
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
	setBounds (x, y, Math.max (0, width), Math.max (0, height), true, true, true);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	return setBounds (topHandle (), x, y, width, height, move, resize, events);
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
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true, true);
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.  Note that on some platforms,
 * a mouse button must currently be down for capture to be assigned.
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
	if (cursor != null && cursor.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	if (!isEnabled ()) return;
	org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (where);
	int [] theWindow = new int [1];
	if (display.grabControl == this) {
		theWindow [0] = OS.GetControlOwner (handle);
	} else {
		if (OS.FindWindow (where, theWindow) != OS.inContent) return;
		if (theWindow [0] == 0) return;
	}
	Rect rect = new Rect ();
	OS.GetWindowBounds (theWindow [0], (short) OS.kWindowStructureRgn, rect);
	int [] theControl = new int [1];
	if (display.grabControl == this) {
		theControl [0] = handle;
	} else {
		CGPoint inPoint = new CGPoint ();
		inPoint.x = where.h - rect.left;
		inPoint.y = where.v - rect.top;
		int [] theRoot = new int [1];
		OS.GetRootControl (theWindow [0], theRoot);
		int[] event = new int[1];
		OS.CreateEvent (0, OS.kEventClassMouse, OS.kEventMouseDown, 0.0, 0, event);
		OS.SetEventParameter (event[0], OS.kEventParamWindowMouseLocation, OS.typeHIPoint, CGPoint.sizeof, inPoint);
		OS.HIViewGetViewForMouseEvent (theRoot [0], event [0], theControl);
		OS.ReleaseEvent(event[0]);
		int cursorControl = theControl [0];
		while (theControl [0] != 0 && theControl [0] != handle) {
			OS.GetSuperControl (theControl [0], theControl);
		}
		if (theControl [0] == 0) return;
		theControl [0] = cursorControl;
		do {
			Widget widget = display.getWidget (theControl [0]);
			if (widget != null) {
				if (widget instanceof Control) {
					Control control = (Control) widget;
					if (control.isEnabled ()) break;
				}
			}
			OS.GetSuperControl (theControl [0], theControl);
		} while (theControl [0] != 0);
		if (theControl [0] == 0) {
			theControl [0] = theRoot [0];
			Widget widget = display.getWidget (theControl [0]);
			if (widget != null && widget instanceof Control) {
				Control control = (Control) widget;
				theControl [0] = control.handle;
			}
		}
	}
	CGPoint pt = new CGPoint ();
	OS.HIViewConvertPoint (pt, theControl [0], 0);
	where.h -= (int) pt.x;
	where.v -= (int) pt.y;
	where.h -= rect.left;
	where.v -= rect.top;
	int modifiers = OS.GetCurrentEventKeyModifiers ();
	boolean [] cursorWasSet = new boolean [1];
	OS.HandleControlSetCursor (theControl [0], where, (short) modifiers, cursorWasSet);
	if (!cursorWasSet [0]) OS.SetThemeCursor (OS.kThemeArrowCursor);
}

void setDefaultFont () {
	if (display.smallFonts) setFontStyle (defaultFont ());
}

/**
 * Sets the receiver's drag detect state. If the argument is
 * <code>true</code>, the receiver will detect drag gestures,
 * otherwise these gestures will be ignored.
 *
 * @param dragDetect the new drag detect state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public void setDragDetect (boolean dragDetect) {
	checkWidget ();
	if (dragDetect) {
		state |= DRAG_DETECT;	
	} else {
		state &= ~DRAG_DETECT;
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
	if (((state & DISABLED) == 0) == enabled) return;
	Control control = null;
	boolean fixFocus = false;
	if (!enabled) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	enableWidget (enabled);
	if (fixFocus) fixFocus (control);
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.  Focus
 * reassignment will respect applicable platform constraints.
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
	if ((style & SWT.NO_FOCUS) != 0) return false;
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
	if (font != null) { 
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.font = font;
	setFontStyle (display.smallFonts ? (font != null ? font : defaultFont ()) : font);
	redrawWidget (handle, false);
}

void setFontStyle (Font font) {
	setFontStyle (handle, font);
}

void setFontStyle (int control, Font font) {
	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
	OS.GetControlData (control, (short) OS.kControlEntireControl, OS.kControlFontStyleTag, ControlFontStyleRec.sizeof, fontStyle, null);
	fontStyle.flags &= ~(OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask | OS.kControlUseThemeFontIDMask);
	if (font != null) {
		short [] family = new short [1], style = new short [1];
		OS.FMGetFontFamilyInstanceFromFont (font.handle, family, style);
		fontStyle.flags |= OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask;
		fontStyle.font = family [0];
		fontStyle.style = (short) (style [0] | font.style);
		fontStyle.size = (short) font.size;
	}
	OS.SetControlFontStyle (control, fontStyle);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
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
 */
public void setForeground (Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	float[] foreground = color != null ? color.handle : null;
	if (equals (foreground, this.foreground)) return;
	this.foreground = foreground;
	setForeground (foreground);
	redrawWidget (handle, false);
}

void setForeground (float [] color) {
	 setForeground (handle, color);
}

void setForeground (int control, float [] color) {
	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
	OS.GetControlData (control, (short) OS.kControlEntireControl, OS.kControlFontStyleTag, ControlFontStyleRec.sizeof, fontStyle, null);
	if (color != null) {
		fontStyle.foreColor_red = (short) (color [0] * 0xffff);
		fontStyle.foreColor_green = (short) (color [1] * 0xffff);
		fontStyle.foreColor_blue = (short) (color [2] * 0xffff);
		fontStyle.flags |= OS.kControlUseForeColorMask;
	} else {
		fontStyle.flags &= ~OS.kControlUseForeColorMask;
	}
	OS.SetControlFontStyle (control, fontStyle);
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
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
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
	setBounds (x, y, 0, 0, true, false, true);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (location.x, location.y, 0, 0, true, false, true);
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 * <p>
 * Note: Disposing of a control that has a pop up menu will
 * dispose of the menu.  To avoid this behavior, set the
 * menu to null before the control is disposed.
 * </p>
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
 * Returns <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget();
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	if (oldShell != newShell || oldDecorations != newDecorations) {
		Menu [] menus = oldShell.findMenus (this);
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
	int topHandle = topHandle ();
	OS.HIViewAddSubview (parent.handle, topHandle);
	OS.HIViewSetVisible (topHandle, (state & HIDDEN) == 0);
	OS.HIViewSetZOrder (topHandle, OS.kHIViewZOrderBelow, 0);
	this.parent = parent;
	return true;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.  Nested calls
 * to this method are stacked.
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
 * @see #redraw(int, int, int, int, boolean)
 * @see #update()
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
			int topHandle = topHandle ();
			OS.HIViewSetDrawingEnabled (topHandle, true);
			invalidateVisibleRegion (topHandle);
			redrawWidget (topHandle, true);
		}
	} else {
		if (drawCount == 0) {
			int topHandle = topHandle ();
			OS.HIViewSetDrawingEnabled (topHandle, false);
			invalidateVisibleRegion (topHandle);
		}
		drawCount++;
	}
}

/**
 * Sets the shape of the control to the region specified
 * by the argument.  When the argument is null, the
 * default shape of the control is restored.
 *
 * @param region the region that defines the shape of the control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul>  
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setRegion (Region region) {
	checkWidget ();
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.region = region;
	OS.HIViewRegionChanged (handle, OS.kControlStructureMetaPart);
	redrawWidget (handle, true);
}

boolean setRadioSelection (boolean value){
	return false;
}

void setRelations () {
	if (parent == null) return;
	Control [] children = parent._getChildren ();
	int count = children.length;
	if (count > 1) {
		/*
		 * the receiver is the last item in the list, so its predecessor will
		 * be the second-last item in the list
		 */
		Control child = children [count - 2];
		if (child != this) {
			child.addRelation (this);
		}
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
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), false, true, true);
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
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true, true);
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}

boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	return forceFocus ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
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
	if (display.helpWidget == this) {
		display.helpWidget = null;
		OS.HMInstallControlContentCallback (handle, 0);
		OS.HMInstallControlContentCallback (handle, display.helpProc);
	}
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
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	
	/*
	* Feature in the Macintosh.  If the receiver has focus, hiding
	* the receiver causes no control to have focus.  Also, the focus
	* needs to be cleared from any TXNObject so that it stops blinking
	* the caret.  The fix is to assign focus to the first ancestor
	* control that takes focus.  If no control will take focus, clear
	* the focus control.
	*/
	Control control = null;
	boolean fixFocus = false;
	if (!visible) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	setVisible (topHandle (), visible);
	if (!visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Hide);
		if (isDisposed ()) return;
	}
	if (fixFocus) fixFocus (control);
}

void setZOrder () {
	int topHandle = topHandle ();
	int parentHandle = parent.handle;
	OS.HIViewAddSubview (parentHandle, topHandle);
	OS.HIViewSetZOrder (topHandle, OS.kHIViewZOrderBelow, 0);
	Rect inset = getInset ();
	Rect rect = new Rect ();
	rect.left = rect.right = inset.left;
	rect.top = rect.bottom = inset.top;
	OS.SetControlBounds (topHandle, rect);
}

void setZOrder (Control sibling, boolean above) {
	int siblingHandle = sibling == null ? 0 : sibling.topHandle ();
	int index = 0, siblingIndex = 0, oldNextIndex = -1;
	Control[] children = null;
	/* determine the receiver's and sibling's indexes in the parent */
	children = parent._getChildren ();
	while (index < children.length) {
		if (children [index] == this) break;
		index++;
	}
	if (sibling != null) {
		while (siblingIndex < children.length) {
			if (children [siblingIndex] == sibling) break;
			siblingIndex++;
		}
	}
	/* remove "Labeled by" relationships that will no longer be valid */
	removeRelation ();
	if (index + 1 < children.length) {
		oldNextIndex = index + 1;
		children [oldNextIndex].removeRelation ();
	}
	if (sibling != null) {
		if (above) {
			sibling.removeRelation ();
		} else {
			if (siblingIndex + 1 < children.length) {
				children [siblingIndex + 1].removeRelation ();
			}
		}
	}
	setZOrder (topHandle (), siblingHandle, above);
	/* determine the receiver's new index in the parent */
	if (sibling != null) {
		if (above) {
			index = siblingIndex - (index < siblingIndex ? 1 : 0);
		} else {
			index = siblingIndex + (siblingIndex < index ? 1 : 0);
		}
	} else {
		if (above) {
			index = 0;
		} else {
			index = children.length - 1;
		}
	}

	/* add new "Labeled by" relations as needed */
	children = parent._getChildren ();
	if (0 < index) {
		children [index - 1].addRelation (this);
	}
	if (index + 1 < children.length) {
		addRelation (children [index + 1]);
	}
	if (oldNextIndex != -1) {
		if (oldNextIndex <= index) oldNextIndex--;
		/* the last two conditions below ensure that duplicate relations are not hooked */
		if (0 < oldNextIndex && oldNextIndex != index && oldNextIndex != index + 1) {
			children [oldNextIndex - 1].addRelation (children [oldNextIndex]);
		}
	}
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

Point textExtent (int ptr, int wHint) {
	if (ptr != 0 && OS.CFStringGetLength (ptr) > 0) {		
		float [] w = new float [1], h = new float [1];
		HIThemeTextInfo info = new HIThemeTextInfo ();
		info.state = OS.kThemeStateActive;
		if (font != null) {
			short [] family = new short [1], style = new short [1];
			OS.FMGetFontFamilyInstanceFromFont (font.handle, family, style);
			OS.TextFont (family [0]);
			OS.TextFace ((short) (style [0] | font.style));
			OS.TextSize ((short) font.size);
			info.fontID = (short) OS.kThemeCurrentPortFont; 
		} else {
			info.fontID = (short) defaultThemeFont ();
		}
		OS.HIThemeGetTextDimensions (ptr, wHint == SWT.DEFAULT ? 0 : wHint, info, w, h, null);
		return new Point((int) w [0], (int) h [0]);
	} else {
		Font font = getFont ();
		ATSFontMetrics metrics = new ATSFontMetrics();
		OS.ATSFontGetVerticalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		OS.ATSFontGetHorizontalMetrics(font.handle, OS.kATSOptionFlagsDefault, metrics);
		return new Point(0, (int)(0.5f + (metrics.ascent -metrics.descent + metrics.leading) * font.size));
	}
}

Point textExtent(char[] chars, int wHint) {
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
	Point result = textExtent (ptr, wHint);
	if (ptr != 0) OS.CFRelease (ptr);
	return result;
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toControl (int x, int y) {
	checkWidget();
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	CGPoint pt = new CGPoint ();
	OS.HIViewConvertPoint (pt, handle, 0);
	x -= (int) pt.x;
	y -= (int) pt.y;
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
	x -= rect.left;
	y -= rect.top;
	Rect inset = getInset ();
	x += inset.left;
	y += inset.top;
    return new Point (x, y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
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
    return toControl (point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toDisplay (int x, int y) {
	checkWidget();
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	CGPoint pt = new CGPoint ();
	OS.HIViewConvertPoint (pt, handle, 0);
	x += (int) pt.x;
	y += (int) pt.y;
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
	x += rect.left;
	y += rect.top;
	Rect inset = getInset ();
	x -= inset.left;
	y -= inset.top;
    return new Point (x, y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
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
	return toDisplay (point.x, point.y);
}

int topHandle () {
	return handle;
}

boolean translateTraversal (int key, int theEvent, boolean [] consume) {
	int detail = SWT.TRAVERSE_NONE;
	int code = traversalCode (key, theEvent);
	boolean all = false;
	switch (key) {
		case 53: /* Esc */ {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case 76: /* KP Enter */
		case 36: /* Return */ {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case 48: /* Tab */ {
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			boolean next = (modifiers [0] & OS.shiftKey) == 0;
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case 126: /* Up arrow */
		case 123: /* Left arrow */
		case 125: /* Down arrow */
		case 124: /* Right arrow */ {
			boolean next = key == 125 /* Down arrow */ || key == 124 /* Right arrow */;
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case 116: /* Page up */
		case 121: /* Page down */ {
			all = true;
			int [] modifiers = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
			if ((modifiers [0] & OS.controlKey) == 0) return false;
			detail = key == 121 /* Page down */ ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	Event event = new Event ();
	event.doit = consume [0] = (code & detail) != 0;
	event.detail = detail;
	if (!setKeyState (event, SWT.Traverse, theEvent)) return false;
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

int traversalCode (int key, int theEvent) {
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	return code;
}

boolean traverseMnemonic (char key) {
	return false;
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
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverse (Event event) {
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
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
	return false;
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
			return true;
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
	if (index == length) return false;
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
	return false;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseMnemonic (Event event) {
	return false;
}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns. If there
 * are no outstanding paint request, this method does
 * nothing.
 * <p>
 * Note: This method does not cause a redraw.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw()
 * @see #redraw(int, int, int, int, boolean)
 * @see PaintListener
 * @see SWT#Paint
 */
public void update () {
	checkWidget();
	update (false);
}

void update (boolean all) {
//	checkWidget();
	//TODO - not all
	if (display.inPaint) return;
	OS.HIViewRender (handle);
	if (isDisposed()) return;
	OS.HIWindowFlush (OS.GetControlOwner (handle));
}

void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}

void updateLayout (boolean all) {
	/* Do nothing */
}

}

