package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
*	A tool item is a selectable user interface object
* that represents a button in a tool bar.
*
* <p>
* <b>Styles</b><br>
* <dd>CHECK, PUSH,	RADIO,	SEPARATOR<br>
* <b>Events</b><br>
* <dd>Selection<br>
*/

/* Class Definition */
public /*final*/ class ToolItem extends Item {
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean set, drawHotImage;

/**
* Creates a new instance of the widget.
*/
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}
/**
* Creates a new instance of the widget.
*/
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if ((style & SWT.SEPARATOR) != 0) {
		int [] argList = {
			OS.XmNwidth, 8,
			OS.XmNheight, 5,
			OS.XmNrecomputeSize, 0,
			OS.XmNpositionIndex, index,
			OS.XmNmappedWhenManaged, 0,
		};
		handle = OS.XmCreateDrawnButton (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int [] argList = {
		OS.XmNwidth, 24,
		OS.XmNheight, 22,
		OS.XmNrecomputeSize, 0,
		OS.XmNhighlightThickness, 0,
		OS.XmNmarginWidth, 2,
		OS.XmNmarginHeight, 1,
		OS.XmNtraversalOn, 0,
		OS.XmNpositionIndex, index,
		OS.XmNshadowType, OS.XmSHADOW_OUT,
	};
	handle = OS.XmCreateDrawnButton (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((parent.style & SWT.FLAT) != 0) {
		argList = new int [] {OS.XmNshadowThickness, 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
}

Point computeSize () {
	//if (control != null) return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

	int [] argList = {
		OS.XmNmarginHeight, 0,
		OS.XmNmarginWidth, 0,
		OS.XmNshadowThickness, 0,
		OS.XmNhighlightThickness, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int marginHeight = argList[1];
	int marginWidth = argList[3];
	int shadowThickness = 2; //argList[5];
	int highlightThickness = argList[7];

	int textWidth = 0, textHeight = 0;
	if (text != null && text.length() > 0) {
		GC gc = new GC (parent);
		Point textExtent = gc.textExtent (text);
		textWidth = textExtent.x;
		textHeight = textExtent.y;
		gc.dispose ();
	}
	int imageWidth = 0, imageHeight = 0;
	if (image !=  null) {
		Rectangle rect = image.getBounds();
		imageWidth = rect.width;
		imageHeight= rect.height;
	}
	
	int contentHeight = 0, contentWidth = 0;
	if ((parent.style & SWT.RIGHT) > 0) {
		contentHeight = Math.max(imageHeight, textHeight);
		contentWidth = imageWidth + textWidth;
		if (imageWidth > 0 && textWidth > 0) contentWidth += marginWidth;
	} else {
		contentHeight = imageHeight + textHeight;
		if (imageHeight > 0 && textHeight > 0) contentHeight += marginHeight;
		contentWidth = Math.max(imageWidth, textWidth);
	}
	
	/* This value comes from Windows */
	int height = 22;
	if (contentHeight != 0) {
		height = contentHeight 
			+ (2 * marginHeight) 
			+ (2 * shadowThickness) 
			+ (2 * highlightThickness);
	}
	
	/* This value comes from Windows */	
	int width = 24;
	if (contentWidth != 0) {
		width = contentWidth
			+ (2 * marginWidth) 
			+ (2 * shadowThickness) 
			+ (2 * highlightThickness);
	}
	
	return new Point (width, height);
}
void createWidget (int index) {
	super.createWidget (index);
	toolTipText = "";
	parent.relayout ();
}
public void dispose () {
	ToolBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
}
public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5], argList [7]);
}
/**
* Gets the control.
* <p>
* @return the control
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Control getControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return control;
}
/**
* Gets the disabled image.
* <p>
* @return the image
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Image getDisabledmage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return disabledImage;
}
/**
* Gets the enabled state.
* <p>
* A disabled widget is typically not selectable from
* the user interface and draws with an inactive or
* grayed look.
*
* @return a boolean that is the enabled state of the widget.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
* Gets the hot image.
* <p>
* @return the image
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Image getHotImage () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return hotImage;
}
/**
* Gets the parent.
* <p>
* @return the parent
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public ToolBar getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}
/**
* Gets the selection state.
* <p>
* @return the selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return set;
}
/**
* Gets the hover help text.
* <p>
* @return the hover help text
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getToolTipText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return toolTipText;
}
public int getWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNwidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNexposeCallback, windowProc, SWT.Paint);
//	OS.XtAddCallback (handle, OS.XmNactivateCallback, windowProc, SWT.Selection);
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, SWT.MouseDown);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, SWT.MouseUp);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, SWT.MouseMove);
	OS.XtAddEventHandler (handle, OS.EnterWindowMask, false, windowProc, SWT.MouseEnter);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, SWT.MouseExit);
}
/**
* Gets the enabled status.
* <p>
* When an ancestor of the widget is disabled, the enabled
* stats of the widget in the widget hierarchy is disabled
* regardless of the actual enabled state of the widget.
*
* @param enabled a boolean that is the enabled state.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}
void manageChildren () {
	OS.XtManageChild (handle);
}
int processSelection (int callData) {
	if ((style & SWT.RADIO) != 0) {
		selectRadio ();
	} else {
		if ((style & SWT.CHECK) != 0) setSelection(!set);			
	}
	postEvent (SWT.Selection);
	return 0;
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}
void releaseWidget () {
	getDisplay ().releaseToolTipHandle (handle);
	super.releaseWidget ();
	parent = null;
	control = null;
	toolTipText = null;
	disabledImage = hotImage = null; 
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
void setBounds (int x, int y, int width, int height) {
	if (control != null) control.setBounds(x, y, width, height);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int newWidth = Math.max (width, 1), newHeight = Math.max (height, 1);
	OS.XtConfigureWidget (handle, x, y, newWidth, newHeight, 0);
}
/**
* Sets the control.
* <p>
* @param control the new control
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setControl (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (control != null && control.parent != parent) {
		error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
/**
* Sets the enabled state.
* <p>
* A disabled widget is typically not selectable from
* the user interface and draws with an inactive or
* grayed look.
*
* @param enabled the new value of the enabled flag
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
* Sets the hot image.
* <p>
* @param image the new image (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setDisabledImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
}
/**
* Sets the hot image.
* <p>
* @param image the new image (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setHotImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
}
/**
* Sets the widget image.
* <p>
* @param image the widget image (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	super.setImage (image);

	/* Resize */	
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	Point size = computeSize();
	if (argList[1] != size.x || argList[3] != size.y) {
		OS.XtResizeWidget (handle, size.x, size.y, 0);
	}
	parent.relayout ();
}

/**
* Sets the selection state.
* <p>
* @param selected the new selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (boolean selected) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	if (selected == set) return;
	set = selected;
	setDrawPressed(set);
	if ((parent.style & SWT.FLAT) != 0) {
		int shadowThickness = set ? getDisplay().buttonShadowThickness : 0;
		int [] argList = {OS.XmNshadowThickness, shadowThickness};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
}
/**
* Sets the widget text.
*/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);

	/* Resize */
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	Point size = computeSize();
	if (argList[1] != size.x || argList[3] != size.y) {
		OS.XtResizeWidget (handle, size.x, size.y, 0);
	}
	parent.relayout ();
}

/**
* Sets the hover help text.
* <p>
* @param string the hover help text (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	toolTipText = string;
}

public void setWidth (int width) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	int [] argList = {OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtResizeWidget (handle, width, argList [1], 0);
	parent.relayout ();
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}

void setDrawPressed (boolean value) {
	int shadowType = value ? OS.XmSHADOW_IN : OS.XmSHADOW_OUT;
	int [] argList = {OS.XmNshadowType, shadowType};
	OS.XtSetValues(handle, argList, argList.length / 2);
}

int processMouseDown (int callData) {
	getDisplay ().hideToolTip();
	if (set && (style & SWT.RADIO) != 0) return 0;
	setDrawPressed(!set);
	return 0;
}

int processMouseEnter (int callData) {
	if ((parent.style & SWT.FLAT) != 0) {
		int [] argList = {OS.XmNshadowThickness, getDisplay().buttonShadowThickness};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	boolean button1Pressed = (xEvent.state & OS.Button1Mask) != 0;
	if (button1Pressed) {
		setDrawPressed(!set);
	}
	drawHotImage = (parent.style & SWT.FLAT) != 0 && hotImage != null;
	if (drawHotImage) { 
		OS.XClearArea (xEvent.display, xEvent.window, 0, 0, 0, 0, true);
	}
	return 0;
}

int processMouseExit (int callData) {
	Display display = getDisplay ();
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
	
	if ((parent.style & SWT.FLAT) != 0 && !set) {
		int [] argList = {OS.XmNshadowThickness, 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	boolean button1Pressed = (xEvent.state & OS.Button1Mask) != 0;
	if (button1Pressed) {
		setDrawPressed(set);
	}
	if (drawHotImage) {
		drawHotImage = false;
		OS.XClearArea (xEvent.display, xEvent.window, 0, 0, 0, 0, true);
	}
	return 0;
}

Point toControl (Point point) {
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
	return new Point (point.x - root_x [0], point.y - root_y [0]);
}

int processMouseHover (int id) {
	Display display = getDisplay();
	Point local = toControl(display.getCursorLocation());
	display.showToolTip(handle, toolTipText);
	return 0;
}

int processMouseMove (int callData) {
	Display display = getDisplay ();
	display.addMouseHoverTimeOut (handle);
	return 0;
}

int processMouseUp (int callData) {
	getDisplay ().hideToolTip();	

	/**
	* Bug in Motif. The activate callback is unreliable on 
	* drawn buttons. Rather than relying on it to generate
	* selection events, use the mouseUp event.
	*/
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (xEvent.x >= 0 && xEvent.y >= 0 && xEvent.x <= argList[1] && xEvent.y <= argList[3]) {
		processSelection(0);
	}

	setDrawPressed(set);	
	return 0;
}

int processPaint (int callData) {
	if ((style & SWT.SEPARATOR) != 0) return 0;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return 0;
	
	int [] argList = {OS.XmNcolormap, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNmarginWidth, 0, OS.XmNmarginHeight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int x = 0, y = 0, width = argList [3], height = argList [5], marginWidth = argList [7],  marginHeight = argList [9];
	
	ToolDrawable wrapper = new ToolDrawable ();
	wrapper.device = getDisplay ();
	wrapper.display = xDisplay;
	wrapper.drawable = xWindow;
	wrapper.fontList = parent.getFontList ();
	wrapper.colormap = argList [1];	
	GC gc = new GC (wrapper);
	
	XmAnyCallbackStruct cb = new XmAnyCallbackStruct ();
	OS.memmove (cb, callData, XmAnyCallbackStruct.sizeof);
	if (cb.event != 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, cb.event, XExposeEvent.sizeof);
		Rectangle rect = new Rectangle (xEvent.x, xEvent.y, xEvent.width, xEvent.height);
		gc.setClipping (rect);
	}
	
	Image currentImage = drawHotImage ? hotImage : image;
	if (!getEnabled()) {
		Display display = getDisplay ();
		currentImage = disabledImage;
		if (currentImage == null) {
			currentImage = new Image (display, image, SWT.IMAGE_DISABLE);
		}
		Color disabledColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		gc.setForeground(disabledColor);
	} else {
		gc.setForeground (parent.getForeground ());
	}
	gc.setBackground (parent.getBackground ());
		
	int textX = 0, textY = 0, textWidth = 0, textHeight = 0;
	if (text != null && text.length() > 0) {
		Point textExtent = gc.textExtent(text);
		textWidth = textExtent.x;
		textHeight = textExtent.y;
	}	
	int imageX = 0, imageY = 0, imageWidth = 0, imageHeight = 0;
	if (currentImage != null) {
		Rectangle imageBounds = currentImage.getBounds();
		imageWidth = imageBounds.width;
		imageHeight = imageBounds.height;
	}
	
	if ((parent.style & SWT.RIGHT) > 0) {
		imageX = x + ((width - imageWidth - textWidth - marginWidth) / 2) + 1;
		imageY = y + ((height - imageHeight) / 2);
		textX = imageX + imageWidth + marginWidth;
		textY = y + ((height - textHeight) / 2);
	} else {		
		imageX = x + ((width - imageWidth) / 2) + 1;
		imageY = y + ((height - imageHeight - textHeight - marginHeight) / 2) + 1;
		textX = x + ((width - textWidth) / 2) + 2;
		textY = imageY + imageHeight + marginHeight;
	}
	
	if (textWidth > 0) gc.drawText(text, textX, textY, false);
	if (imageWidth > 0) gc.drawImage(currentImage, imageX, imageY);

	gc.dispose ();
	
	if (!getEnabled() && disabledImage == null) {
		if (currentImage != null) currentImage.dispose ();
	}
	return 0;
}

}
