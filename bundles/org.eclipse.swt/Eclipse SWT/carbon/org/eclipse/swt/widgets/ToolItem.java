package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * </p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */
public class ToolItem extends Item {
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean set;
	
	// AW
	private boolean fPressed;
	private short[] prevInfo;
	// AW
	
	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 8;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
	parent.relayout ();
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
	parent.relayout ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
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
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
	/* AW
	int [] argList = {
		OS.XmNwidth, DEFAULT_WIDTH,
		OS.XmNheight, DEFAULT_HEIGHT,
		OS.XmNrecomputeSize, 0,
		OS.XmNhighlightThickness, (parent.style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		OS.XmNmarginWidth, 2,
		OS.XmNmarginHeight, 1,
		OS.XmNtraversalOn, (parent.style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		OS.XmNpositionIndex, index,
		OS.XmNshadowType, OS.XmSHADOW_OUT,
		OS.XmNancestorSensitive, 1,
	};
	handle = OS.XmCreateDrawnButton (parentHandle, null, argList, argList.length / 2);
	*/
	int width= DEFAULT_WIDTH;
	int height= DEFAULT_HEIGHT;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((parent.style & SWT.HORIZONTAL) != 0)
			width= DEFAULT_SEPARATOR_WIDTH;
		else
			height= DEFAULT_SEPARATOR_WIDTH;
	}
	handle = MacUtil.createDrawingArea(parentHandle, false, width, height, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	
	/*
	short n= OS.CountSubControls(parentHandle);
	if ((n-1) != index)
		System.out.println("ToolItem.createHandle: creation at position nyi " + index);
	*/
}
void click (boolean dropDown, MacEvent mEvent) {
	if ((style & SWT.RADIO) != 0) {
		selectRadio ();
	} else {
		if ((style & SWT.CHECK) != 0) setSelection(!set);			
	}
	Event event = new Event ();
	if ((style & SWT.DROP_DOWN) != 0) {
		if (dropDown) event.detail = SWT.ARROW;
	}
	if (mEvent != null) setInputState (event, mEvent);
	postEvent (SWT.Selection, event);
}
Point computeSize () {
	if ((style & SWT.SEPARATOR) != 0) {
		MacRect bounds= new MacRect();
		OS.GetControlBounds(handle, bounds.getData());
		return new Point(bounds.getWidth(), bounds.getHeight());
	}
	/* AW
	int [] argList = {
		OS.XmNmarginHeight, 0,
		OS.XmNmarginWidth, 0,
		OS.XmNshadowThickness, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int marginHeight = argList [1], marginWidth = argList [3];
	int shadowThickness = argList [5];
	*/
	int marginHeight = 2, marginWidth = 2;
	int shadowThickness = 1;
	if ((parent.style & SWT.FLAT) != 0) {
		Display display = getDisplay ();
		shadowThickness = Math.min (2, display.buttonShadowThickness);
	}
	int textWidth = 0, textHeight = 0;
	if (text.length () != 0) {
		GC gc = new GC (parent);
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC;
		Point textExtent = gc.textExtent (text, flags);
		textWidth = textExtent.x;
		textHeight = textExtent.y;
		gc.dispose ();
	}
	int imageWidth = 0, imageHeight = 0;
	if (image != null) {
		Rectangle rect = image.getBounds ();
		imageWidth = rect.width;
		imageHeight = rect.height;
	}
	int width = 0, height = 0;
	if ((parent.style & SWT.RIGHT) != 0) {
		width = imageWidth + textWidth;
		height = Math.max (imageHeight, textHeight);
		if (imageWidth != 0 && textWidth != 0) width += 2;
	} else {
		height = imageHeight + textHeight;
		if (imageHeight != 0 && textHeight != 0) height += 2;
		width = Math.max (imageWidth, textWidth);
	}
	if ((style & SWT.DROP_DOWN) != 0) width += 12;
	
	if (width != 0) {
		width += (marginWidth + shadowThickness) * 2 + 2;
	} else {
		width = DEFAULT_WIDTH;
	}
	if (height != 0) {
		height += (marginHeight + shadowThickness) * 2 + 2;
	} else {
		height = DEFAULT_HEIGHT;
	}
	return new Point (width, height);
}
void createWidget (int index) {
	super.createWidget (index);
	toolTipText = "";
	parent.relayout ();
}
public void dispose () {
	if (isDisposed()) return;
	ToolBar parent = this.parent;
	parent.redraw();	// AW workaround for Toolbar update problem
	super.dispose ();
	parent.relayout ();
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
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
	short[] bounds= new short[4];
	short[] pbounds= new short[4];
	OS.GetControlBounds(handle, bounds);
	OS.GetControlBounds(parent.handle, pbounds);
	return new Rectangle(bounds[1]-pbounds[1], bounds[0]-pbounds[0], bounds[3]-bounds[1], bounds[2]-bounds[0]);
}
/**
 * Returns the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}
/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise.
 * <p>
 * A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 * </p>
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
	return OS.IsControlEnabled(handle);
}
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}
/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}
/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed.
 * </p>
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
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return set;
}
/**
 * Returns the receiver's tool tip text, or null if it has not been set.
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
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	return bounds.getWidth();
}
boolean hasCursor () {
	/* AW
	int [] unused = new int [1], buffer = new int [1];
	int xDisplay = OS.XtDisplay (handle);
	int xWindow, xParent = OS.XDefaultRootWindow (xDisplay);
	do {
		if (OS.XQueryPointer (
			xDisplay, xParent, unused, buffer,
			unused, unused, unused, unused, unused) == 0) return false;
		if ((xWindow = buffer [0]) != 0) xParent = xWindow;
	} while (xWindow != 0);
	return handle == OS.XtWindowToWidget (xDisplay, xParent);
	*/
	MacPoint mp= new MacPoint();
	OS.GetMouse(mp.getData());
	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	return bounds.toRectangle().contains(mp.toPoint());
}
void hookEvents () {
	super.hookEvents ();
	/* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddEventHandler (handle, OS.KeyPressMask, false, windowProc, SWT.KeyDown);
	OS.XtAddEventHandler (handle, OS.KeyReleaseMask, false, windowProc, SWT.KeyUp);
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, SWT.MouseDown);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, SWT.MouseUp);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, SWT.MouseMove);
	OS.XtAddEventHandler (handle, OS.EnterWindowMask, false, windowProc, SWT.MouseEnter);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, SWT.MouseExit);
	OS.XtAddCallback (handle, OS.XmNexposeCallback, windowProc, SWT.Paint);
	*/
	Display display= getDisplay();		
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneDrawProcTag, display.fUserPaneDrawProc);
	if ((style & SWT.SEPARATOR) != 0) return;
	OS.SetControlData(handle, OS.kControlEntireControl, OS.kControlUserPaneHitTestProcTag, display.fUserPaneHitTestProc);
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise.
 * <p>
 * A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 * </p>
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}
/* AW
void manageChildren () {
	OS.XtManageChild (handle);
}
*/
void redraw () {
	redrawHandle (0, 0, 0, 0, handle, true);
}
void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}
void releaseWidget () {
	Display display = getDisplay ();
	display.releaseToolTipHandle (handle);
	super.releaseWidget ();
	parent = null;
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
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
	this.setSelection (true);
	ToolItem [] items = parent.getItems ();
	int index = 0;
	while (index < items.length && items [index] != this) index++;
	ToolItem item;
	int i = index;
	while (--i >= 0 && ((item = items [i]).style & SWT.RADIO) != 0) {
		item.setSelection (false);
	}
	i = index;
	while (++i < items.length && ((item = items [i]).style & SWT.RADIO) != 0) {
		item.setSelection (false);
	}
}
/*
 * This setBounds is only called from ToolBar.relayout()
 */
void setBounds (int x, int y, int width, int height) {

	if (control != null)
		control.setBounds(x, y, width, height);
	
	width = Math.max(width, 0);
	height = Math.max(height, 0);
	short[] bounds= new short[4];
	short[] pbounds= new short[4];
	OS.GetControlBounds(handle, bounds);
	OS.GetControlBounds(parent.handle, pbounds);
		
	boolean sameOrigin = (bounds[1]-pbounds[1]) == x && (bounds[0]-pbounds[0]) == y;
	boolean sameExtent = (bounds[3]-bounds[1]) == width && (bounds[2]-bounds[0]) == height;
	if (!sameOrigin || !sameExtent)
		OS.SetControlBounds(handle, new MacRect(pbounds[1]+x, pbounds[0]+y, width, height).getData());

	if (parent.fGotSize)
		OS.ShowControl(handle);
}
/**
 * Sets the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
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
	if (enabled)
		OS.EnableControl(handle);
	else
		OS.DisableControl(handle);
}
/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disbled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	if (!getEnabled ()) redraw ();
}
/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	if ((parent.style & SWT.FLAT) != 0) redraw ();
}
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	Point size = computeSize ();
	setSize (size.x, size.y);
	//redraw ();
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed.
 * </p>
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
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	if (selected == set) return;
	set = selected;
	setDrawPressed (set);
}

void setSize (int width, int height) {
	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	if (bounds.getWidth() != width || bounds.getHeight() != height) {
		OS.SizeControl(handle, (short) width, (short) height);
		parent.relayout();
	}
}
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	Point size = computeSize ();
	setSize (size.x, size.y);
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
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	setSize (width, bounds.getHeight());
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
void setDrawPressed (boolean value) {
	/* AW
	int shadowType = value ? OS.XmSHADOW_IN : OS.XmSHADOW_OUT;
	int [] argList = {OS.XmNshadowType, shadowType};
	OS.XtSetValues(handle, argList, argList.length / 2);
	*/
	if (fPressed != value) {
		fPressed= value;
		redraw();
	}
}
int processKeyDown (Object callData) {
	/* AW
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, callData, XKeyEvent.sizeof);
	*/
	/*
	* Forward the key event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	/* AW
	xEvent.window = OS.XtWindow (parent.handle);
//	OS.memmove (callData, xEvent, XKeyEvent.sizeof);
	*/
	parent.processKeyDown (callData);
	return 0;
}
int processKeyUp (Object callData) {
	/* AW
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, callData, XKeyEvent.sizeof);
	int [] keysym = new int [1];
	OS.XLookupString (xEvent, null, 0, keysym, null);
	keysym [0] &= 0xFFFF;
	switch (keysym [0]) {
		case OS.XK_space:
		case OS.XK_Return:
			click (keysym [0] == OS.XK_Return, xEvent);
			break;
	}
	*/
	/*
	* Forward the key event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	/* AW
	xEvent.window = OS.XtWindow (parent.handle);
//	OS.memmove (callData, xEvent, XKeyEvent.sizeof);
	*/
	parent.processKeyUp (callData);
	return 0;
}
int processMouseDown (Object callData) {
	Display display = getDisplay ();
//	Shell shell = parent.getShell ();
	display.hideToolTip ();
	
	/* AW
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	*/
	MacEvent mEvent= (MacEvent) callData;
	if (mEvent.getButton() == 1) {
		if (!set && (style & SWT.RADIO) == 0) {
			setDrawPressed (!set);
		}
	}
	
	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	/* AW
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	OS.memmove (callData, xEvent, XButtonEvent.sizeof);
	*/
	parent.processMouseDown (callData);
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
//	if (!shell.isDisposed()) {
//		shell.setActiveControl (parent);
//	}
	return 0;
}
int processMouseEnter (Object callData) {
	MacEvent me= (MacEvent) callData;
	if (me.getButton() == 1) setDrawPressed (!set);
	else if ((parent.style & SWT.FLAT) != 0) redraw ();
	return 0;
}
int processMouseExit (Object callData) {
	Display display = getDisplay ();
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
	MacEvent me= (MacEvent) callData;
	if (me.getButton() == 1) setDrawPressed (set);
	else if ((parent.style & SWT.FLAT) != 0) redraw ();
	return 0;
}
Point toControl (Point point) {
	return MacUtil.toControl(handle, point);
}
/* AW
boolean translateMnemonic (int key, XKeyEvent xEvent) {
	return parent.translateMnemonic (key, xEvent);
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	return parent.translateTraversal (key, xEvent);
}
*/
int processMouseHover (Object id) {
	Display display = getDisplay ();
	display.showToolTip (handle, toolTipText);
	return 0;
}
int processMouseMove (Object callData) {
	Display display = getDisplay ();
	display.addMouseHoverTimeOut (handle);

	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	/* AW
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	*/
	MacEvent mEvent= (MacEvent) callData;
	/*
	* This code is intentionally commented.
	* Currently, the implementation of the
	* mouse move code in the parent interferes
	* with tool tips for tool items.
	*/
//	OS.memmove (callData, xEvent, XButtonEvent.sizeof);
//	parent.processMouseMove (callData);

	parent.sendMouseEvent (SWT.MouseMove, 0, mEvent);

	return 0;
}
int processMouseUp (Object callData) {
	Display display = getDisplay ();
	display.hideToolTip(); 
	
	MacEvent mEvent= (MacEvent) callData;
	
	if (mEvent.getButton() == 1) {
		/* AW
		int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int width = argList [1], height = argList [3];
		*/
		MacRect bounds= new MacRect();
		OS.GetControlBounds(handle, bounds.getData());
		int width = bounds.getWidth(), height = bounds.getHeight();
				
		Point mp= MacUtil.toControl(handle, mEvent.getWhere2());
		//System.out.println("ToolItem.processMouseUp: " + mp);

		if (0 <= mp.x && mp.x < width && 0 <= mp.y && mp.y < height) {
			click (mp.x > width - 12, mEvent);
		}
		setDrawPressed(set);
	}
	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	/* AW
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	OS.memmove (callData, xEvent, XButtonEvent.sizeof);
	*/
	parent.processMouseUp (callData);

	return 0;
}
int processPaint (Object callData) {

	if ((style & SWT.SEPARATOR) != 0 && control != null)
		return 0;
		
	final Display display = getDisplay ();

	MacRect bounds= new MacRect();
	OS.GetControlBounds(handle, bounds.getData());
	
	int width= bounds.getWidth();
	int height= bounds.getHeight();
	
	Drawable drawable= new Drawable() {
		public int internal_new_GC (GCData data) {
			data.device = display;
			data.foreground = parent.foreground;
			data.background = parent.background;
			data.font = parent.font.handle;
			data.controlHandle = handle;
			return OS.GetWindowPort(OS.GetControlOwner(handle));
		}
		public void internal_dispose_GC (int xGC, GCData data) {
		}
	};

	GC gc= new GC(drawable);
	
	gc.carbon_focus();
	
	// erase background
	gc.fillRectangle(0, 0, width, height);
	
	if ((parent.style & SWT.FLAT) != 0 && set) {
		gc.setBackground(Color.carbon_new(display, 0xE0E0E0));
		gc.fillRoundRectangle(1, 1, width-2, height-2, 8, 8);
		gc.setForeground(display.getSystemColor (SWT.COLOR_GRAY));
		gc.drawRoundRectangle(1, 1, width-3, height-3, 8, 8);
	}
	
	if ((style & SWT.SEPARATOR) != 0) {
	
		OS.DrawThemeSeparator(bounds.getData(), OS.kThemeStateActive);
		
	} else {
				
		Image currentImage = image;
		boolean enabled = getEnabled();
	
		short[] newInfo= new short[3];
				
		newInfo[1]= set ? OS.kThemeButtonOn : OS.kThemeButtonOff;
		
		if ((parent.style & SWT.FLAT) != 0) {
			boolean hasCursor = hasCursor ();
			
			if (hasCursor && enabled) {
				if (OS.StillDown())
					newInfo[0]= OS.kThemeStatePressed;
				else
					newInfo[0]= OS.kThemeStateActive;
			} else
				newInfo= null;
			
			/* Determine if hot image should be used */
			if (enabled && hasCursor && hotImage != null) {
				currentImage = hotImage;
			}
		} else {
			newInfo[0]= (hasCursor() && OS.StillDown()) ? OS.kThemeStatePressed : OS.kThemeStateActive;
		}

		if (newInfo != null) {
			MacRect b= new MacRect(bounds.getX()+1, bounds.getY()+1, width-2, height-2);
			//OS.DrawThemeButton(bounds.getData(), OS.kThemeSmallBevelButton, newInfo, prevInfo, 0, 0, 0);
			OS.DrawThemeButton(b.getData(), OS.kThemeSmallBevelButton, newInfo, prevInfo, 0, 0, 0);
		}
		prevInfo= newInfo;
			
		if (enabled) {
			gc.setForeground (parent.getForeground());
		} else {
			currentImage = disabledImage;
			if (currentImage == null && image != null) {
				currentImage = new Image (display, image, SWT.IMAGE_DISABLE);
			}
			Color disabledColor = display.getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW);
			gc.setForeground (disabledColor);
		}
		
		int textX = 0, textY = 0, textWidth = 0, textHeight = 0;
		if (text.length () != 0) {
			int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC;
			Point textExtent = gc.textExtent (text, flags);
			textWidth = textExtent.x;
			textHeight = textExtent.y;
		}	
		int imageX = 0, imageY = 0, imageWidth = 0, imageHeight = 0;
		if (currentImage != null) {
			Rectangle imageBounds = currentImage.getBounds ();
			imageWidth = imageBounds.width;
			imageHeight = imageBounds.height;
		}
		
		int spacing = 0;
		if (textWidth != 0 && imageWidth != 0) spacing = 2;
		if ((parent.style & SWT.RIGHT) != 0) {
			imageX = (width - imageWidth - textWidth - spacing) / 2;
			imageY = (height - imageHeight) / 2;
			textX = spacing + imageX + imageWidth;
			textY = (height - textHeight) / 2;
		} else {		
			imageX = (width - imageWidth) / 2;
			imageY = (height - imageHeight - textHeight - spacing) / 2;
			textX = (width - textWidth) / 2;
			textY = spacing + imageY + imageHeight;
		}
		
		if ((style & SWT.DROP_DOWN) != 0) {
			textX -= 6;  imageX -=6;
		}
		if (textWidth > 0) {
			int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT;
			gc.drawText(text, textX, textY, flags);
		}
		if (imageWidth > 0)
			gc.drawImage(currentImage, imageX, imageY);
			
		if ((style & SWT.DROP_DOWN) != 0) {
			int startX = width - 12, startY = (height - 2) / 2;
			int [] arrow = {startX, startY, startX + 3, startY + 3, startX + 6, startY};
			gc.setBackground (gc.getForeground ());
			gc.fillPolygon (arrow);
			gc.drawPolygon (arrow);
		}
		if (!enabled && disabledImage == null) {
			if (currentImage != null) currentImage.dispose ();
		}
	}
		
	gc.carbon_unfocus();
	gc.dispose ();
	
	return 0;
}
void propagateWidget (boolean enabled) {
	propagateHandle (enabled, handle);
	/*
	* Tool items participate in focus traversal only when
	* the tool bar takes focus.
	*/
	/* AW
	if ((parent.style & SWT.NO_FOCUS) != 0) {
		if (enabled) {
			int [] argList = {OS.XmNtraversalOn, 0};
			OS.XtSetValues (handle, argList, argList.length / 2);
		}
	}
	*/
}
}
