package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	The widget class is the abstract superclass of all
* user interface objects.  All widgets can be created,
* disposed and support events.
*
* Styles
*
*	BORDER
*	CLIP_CHILDREN, CLIP_SIBLINGS
*
* Events
*
*	KeyDown, KeyUp,
* 	MouseDown, MouseUp, MouseMove,
*	DoubleClick,
*	Paint,
*	Move, Resize,
*	FocusIn, FocusOut,
*
**/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/* Class Definition */
public abstract class Control extends Widget implements Drawable {
	Composite parent;
	int fontList;
	Menu menu;
	String toolTipText;
	Object layoutData;
Control () {
	/* Do nothing */
}
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
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
public void addControlListener(ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
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
public void addFocusListener(FocusListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
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
public void addHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
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
public void addKeyListener(KeyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
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
public void addMouseListener(MouseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
}
/**
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void addMouseTrackListener (MouseTrackListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
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
public void addMouseMoveListener(MouseMoveListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
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
public void addPaintListener(PaintListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Paint,typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void addTraverseListener (TraverseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}
/**
* Computes the preferred size.
* <p>
* @param wHint the width hint (can be SWT.DEFAULT)
* @param hHint the height hint (can be SWT.DEFAULT)
* @param changed the changed hint (for layouts)
* @return the preferred size of the widget.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

void createWidget (int index) {
	super.createWidget (index);
	setZOrder ();
	realizeChildren ();
}
int defaultBackground () {
	return getDisplay ().defaultBackground;
}
int defaultFont () {
	return getDisplay ().defaultFontList;
}
int defaultForeground () {
	return getDisplay ().defaultForeground;
}
void enableHandle (boolean enabled, int widgetHandle) {
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (widgetHandle, argList, argList.length / 2);
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
public boolean forceFocus () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	shell.bringToTop ();
	return OS.XmProcessTraversal (handle, OS.XmTRAVERSE_CURRENT);
}
public Color getBackground () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return Color.motif_new (getDisplay (), getXColor (getBackgroundPixel ()));
}
int getBackgroundPixel () {
	int [] argList = {OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
public int getBorderWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return argList [1];
}
public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [9] * 2;
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5] + borders, argList [7] + borders);
}
Point getClientLocation () {
	short [] handle_x = new short [1], handle_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, handle_x, handle_y);
	short [] topHandle_x = new short [1], topHandle_y = new short [1];
	OS.XtTranslateCoords (parent.handle, (short) 0, (short) 0, topHandle_x, topHandle_y);
	return new Point (handle_x [0] - topHandle_x [0], handle_y [0] - topHandle_y [0]);
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
public boolean getEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (topHandle (), argList, argList.length / 2);
	return argList [1] != 0;
}
public Font getFont () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return Font.motif_new (getDisplay (), getFontList ());
}

int getFontAscent () {
	int fontList = getFontList ();
	
	/* Create a font context to iterate over each element in the font list */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, fontList)) {
		error (SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	
	/* Values discovering during iteration */
	int ascent = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) { 
			/* FontList contains a single font */
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			int fontAscent = Math.max (fontStruct.ascent, fontStruct.max_bounds_ascent);
			if (fontAscent > ascent) ascent = fontAscent;
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) { 
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontAscent = Math.max (fontStruct.ascent, fontStruct.max_bounds_ascent);
				if (fontAscent > ascent) ascent = fontAscent;
			}
		}
	}
	
	OS.XmFontListFreeFontContext (context);
	return ascent;
}

int getFontHeight () {
	int fontList = getFontList ();
	
	/* Create a font context to iterate over each element in the font list */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, fontList)) {
		error (SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	
	/* Values discovering during iteration */
	int height = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) { 
			/* FontList contains a single font */
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			int fontAscent = Math.max (fontStruct.ascent, fontStruct.max_bounds_ascent);
			int fontDescent = Math.max (fontStruct.descent, fontStruct.max_bounds_descent);
			int fontHeight = fontAscent + fontDescent;
			if (fontHeight > height) height = fontHeight;
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) { 
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontAscent = Math.max (fontStruct.ascent, fontStruct.max_bounds_ascent);
				int fontDescent = Math.max (fontStruct.descent, fontStruct.max_bounds_descent);
				int fontHeight = fontAscent + fontDescent;
				if (fontHeight > height) height = fontHeight;
			}
		}
	}
	
	OS.XmFontListFreeFontContext (context);
	return height;
}
int getFontList () {
	int fontHandle = fontHandle ();
	int [] argList = {OS.XmNfontList, 0};
	OS.XtGetValues (fontHandle, argList, argList.length / 2);
	if (argList [1] != 0) return argList [1];
	if (fontList == 0) fontList = defaultFont ();
	return fontList;
}
public Color getForeground () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return Color.motif_new (getDisplay (), getXColor (getForegroundPixel ()));
}
int getForegroundPixel () {
	int [] argList = {OS.XmNforeground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
public Object getLayoutData () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return layoutData;
}
public Point getLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return new Point ((short) argList [1], (short) argList [3]);
}
public Menu getMenu () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menu;
}
public Composite getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getShell ();
}
public Point getSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [5] * 2;
	return new Point (argList [1] + borders, argList [3] + borders);
}
/**
* Gets the tool tip text.
* <p>
* @return the tool tip text.
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
public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return argList [1] != 0;
}
XColor getXColor (int pixel) {
	int display = OS.XtDisplay (handle);
	if (display == 0) return null;
	int [] argList = {OS.XmNcolormap, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int colormap = argList [1];
	if (colormap == 0) return null;
	XColor color = new XColor ();
	color.pixel = pixel;
	OS.XQueryColor (display, colormap, color);
	return color;
}
boolean hasFocus () {
	return this == getDisplay ().getFocusControl ();
}
void hookEvents () {
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
}
int inputContext () {
	return getShell ().inputContext ();
}
public int internal_new_GC (GCData data) {
	if (!OS.XtIsRealized (handle)) {
		int xtParent = handle;
		while ((OS.XtParent (xtParent) != 0) && !OS.XtIsSubclass (xtParent, OS.ShellWidgetClass ())) {
			xtParent = OS.XtParent (xtParent);
		}
		OS.XtRealizeWidget (xtParent);
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
	if (data != null) {
		data.device = getDisplay ();
		data.display = xDisplay;
		data.drawable = xWindow;
		data.foreground = argList [1];
		data.background = argList [3];
		data.fontList = getFontList ();
		data.colormap = argList [5];
	}
	return xGC;
}
public void internal_dispose_GC (int xGC, GCData data) {
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XFreeGC (xDisplay, xGC);
}
public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled () && parent.isEnabled ();
}
public boolean isFocusControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return hasFocus ();
}
public boolean isReparentable () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}
public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getVisible () && parent.isVisible ();
}
void manageChildren () {
	OS.XtSetMappedWhenManaged (handle, false);
	OS.XtManageChild (handle);
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtResizeWidget (handle, 1, 1, argList [1]);
	OS.XtSetMappedWhenManaged (handle, true);
}
Decorations menuShell () {
	return parent.menuShell ();
}
boolean mnemonicHit () {
	return false;
}
boolean mnemonicMatch (char key) {
	return false;
}
public void moveAbove (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setZOrder (control, true);
}
public void moveBelow (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setZOrder (control, false);
}
/**
* Packs the widget.
* <p>
* Packing a widget causes it to be resized to the
* preferred size for the widget.  For a composite,
* this involves computing the preferred size from
* the layout.
*
* @see Control#computeSize(int, int)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void pack () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	pack (true);
}
/**
* Packs the widget.
* <p>
* Packing a widget causes it to be resized to the
* preferred size for the widget.  For a composite,
* this involves computing the preferred size from
* the layout.
*
* @param changed the changed hint (for layouts)
*
* @see Control#computeSize(int, int)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void pack (boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}
int processDefaultSelection (int callData) {
	postEvent (SWT.DefaultSelection);
	return 0;
}
int processFocusIn () {
	sendEvent (SWT.FocusIn);
//	IsDBLocale ifTrue: [self killImeFocus].
	return 0;
}
int processFocusOut () {
	sendEvent (SWT.FocusOut);
//	IsDBLocale ifTrue: [self killImeFocus].
	return 0;
}
int processHelp (int callData) {
	sendHelpEvent (callData);
	return 0;
}
int processKeyDown (int callData) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, callData, XKeyEvent.sizeof);
	sendKeyEvent (SWT.KeyDown, xEvent);
	return 0;
}
int processKeyUp (int callData) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, callData, XKeyEvent.sizeof);
	sendKeyEvent (SWT.KeyUp, xEvent);
	return 0;
}
int processModify (int callData) {
	sendEvent (SWT.Modify);
	return 0;
}
int processMouseDown (int callData) {
	getDisplay ().hideToolTip ();
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	sendMouseEvent (SWT.MouseDown, xEvent.button, xEvent.state, xEvent);
	if (xEvent.button == 2 && hooks (SWT.DragDetect)) {
		sendEvent (SWT.DragDetect);
	}
	if (xEvent.button == 3 && menu != null) {
		OS.XmProcessTraversal (handle, OS.XmTRAVERSE_CURRENT);
		menu.setVisible (true);
	}
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay != 0) {
		Display display = getDisplay ();
		int clickTime = OS.XtGetMultiClickTime (xDisplay);
		int lastTime = display.lastTime, eventTime = xEvent.time;
		int lastButton = display.lastButton, eventButton = xEvent.button;
		if (lastButton == eventButton && lastTime != 0 && Math.abs (lastTime - eventTime) <= clickTime) {
			sendMouseEvent (SWT.MouseDoubleClick, eventButton, xEvent.state, xEvent);
		}
		if (eventTime == 0) eventTime = 1;
		display.lastTime = eventTime;
		display.lastButton = eventButton;
	}
	return 0;
}
int processMouseEnter (int callData) {
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.subwindow != 0) return 0;
	Event event = new Event ();
	event.x = xEvent.x;
	event.y = xEvent.y;
	postEvent (SWT.MouseEnter, event);
	return 0;
}
int processMouseMove (int callData) {
	Display display = getDisplay ();
	display.addMouseHoverTimeOut (handle);
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, callData, XMotionEvent.sizeof);
	sendMouseEvent (SWT.MouseMove, 0, xEvent.state, xEvent);
	return 0;
}
int processMouseExit (int callData) {
	Display display = getDisplay ();
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
	
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, callData, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.subwindow != 0) return 0;
	Event event = new Event ();
	event.x = xEvent.x;
	event.y = xEvent.y;
	postEvent (SWT.MouseExit, event);
	return 0;
}
int processMouseUp (int callData) {
	getDisplay ().hideToolTip ();
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	sendMouseEvent (SWT.MouseUp, xEvent.button, xEvent.state, xEvent);
	return 0;
}
int processPaint (int callData) {
	if (!hooks (SWT.Paint)) return 0;
	XExposeEvent xEvent = new XExposeEvent ();
	OS.memmove (xEvent, callData, XExposeEvent.sizeof);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	Event event = new Event ();
	event.count = xEvent.count;
	event.time = OS.XtLastTimestampProcessed (xDisplay);
	event.x = xEvent.x;  event.y = xEvent.y;
	event.width = xEvent.width;  event.height = xEvent.height;
	GC gc = event.gc = new GC (this);
	XRectangle rect = new XRectangle ();
	rect.x = (short) xEvent.x;  rect.y = (short) xEvent.y;
	rect.width = (short) xEvent.width;  rect.height = (short) xEvent.height;
	OS.XSetClipRectangles (xDisplay, gc.handle, 0, 0, rect, 1, OS.Unsorted);
	sendEvent (SWT.Paint, event);
	if (!gc.isDisposed ()) gc.dispose ();
	event.gc = null;
	return 0;
}
int processSelection (int callData) {
	postEvent (SWT.Selection);
	return 0;
}
int processSetFocus (int callData) {

	/* Get the focus change event */
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, callData, XFocusChangeEvent.sizeof);

	/* Ignore focus changes caused by grabbing and ungrabing. */
	if (xEvent.mode != OS.NotifyNormal) return 0;

	/* Only process focus callbacks between windows. */
	if (xEvent.detail != OS.NotifyAncestor &&
		xEvent.detail != OS.NotifyInferior &&
		xEvent.detail != OS.NotifyNonlinear) return 0;

	/*
	* Ignore focus change events when the window getting or losing
	* focus is a menu.  Because XmGetFocusWidget() does not answer
	* the menu shell (it answers the menu parent), it is necessary
	* to use XGetInputFocus() to get the real X focus window.
	*/
	int xDisplay = xEvent.display;
	if (xDisplay == 0) return 0;
	int [] unused = new int [1], xWindow = new int [1];
	OS.XGetInputFocus (xDisplay, xWindow, unused);
	if (xWindow [0] != 0) {
		int widget = OS.XtWindowToWidget (xDisplay, xWindow [0]);
		if (widget != 0 && OS.XtClass (widget) == OS.XmMenuShellWidgetClass ()) return 0;
	}
	
	/* Process the focus change for the widget. */
	if (xEvent.type == OS.FocusIn) {
		int result = processFocusIn ();
		int index = 0;
		Shell shell = getShell ();
		Control [] focusIn = getPath ();
		Control lastFocus = shell.lastFocus;
		if (lastFocus != null) {
			if (!lastFocus.isDisposed ()) {
				Control [] focusOut = lastFocus.getPath ();
				int length = Math.min (focusIn.length, focusOut.length);
				while (index < length) {
					if (focusIn [index] != focusOut [index]) break;
					index++;
				}
				for (int i=focusOut.length-1; i>=index; --i) {
					focusOut [i].sendEvent (SWT.Deactivate);
				}
			}
			shell.lastFocus = null;
		}
		for (int i=focusIn.length-1; i>=index; --i) {
			focusIn [i].sendEvent (SWT.Activate);
		}
		return result;
	}
	if (xEvent.type == OS.FocusOut) {
		int result = processFocusOut ();
		Shell shell = getShell ();
		shell.lastFocus = this;
		Display display = getDisplay ();
		Control focusControl = display.getFocusControl ();
		if (focusControl == null || shell != focusControl.getShell ()) {
			Control [] focusOut = getPath ();
			for (int i=focusOut.length-1; i>=0; --i) {
				focusOut [i].sendEvent (SWT.Deactivate);
			}
			shell.lastFocus = null;
		}
		return result;
	}
	return 0;
}
void propagateChildren (boolean enabled) {
	propagateWidget (enabled);
}
void propagateHandle (boolean enabled, int widgetHandle) {
	int xDisplay = OS.XtDisplay (widgetHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (widgetHandle);
	if (xWindow == 0) return;
	int event_mask = OS.XtBuildEventMask (widgetHandle);
	int do_not_propagate_mask = OS.KeyPressMask | OS.KeyReleaseMask | OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask;
	if (!enabled) {
		event_mask &= ~do_not_propagate_mask;
		do_not_propagate_mask = 0;
	}
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.event_mask = event_mask;
	attributes.do_not_propagate_mask = do_not_propagate_mask;
	OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWDontPropagate | OS.CWEventMask, attributes);
	int [] argList = {OS.XmNtraversalOn, enabled ? 1 : 0};
	OS.XtSetValues (widgetHandle, argList, argList.length / 2);
}
void propagateWidget (boolean enabled) {
	propagateHandle (enabled, handle);
}
void realizeChildren () {
	if (!isEnabled ()) propagateWidget (false);
}
public void redraw () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	redrawWidget (0, 0, 0, 0, false);
}
public void redraw (int x, int y, int width, int height, boolean all) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (x == 0 && y == 0 && width == 0 && height == 0) return;
	redrawWidget (x, y, width, height, all);
}
void redrawHandle (int x, int y, int width, int height, int widgetHandle) {
	int display = OS.XtDisplay (widgetHandle);
	if (display == 0) return;
	int window = OS.XtWindow (widgetHandle);
	if (window == 0) return;
	int [] argList = {OS.XmNborderWidth, 0, OS.XmNborderColor, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	if (argList [1] != 0) {
		/* Force the border to repaint by setting the color */
		OS.XtSetValues (widgetHandle, argList, argList.length / 2);
	}
	OS.XClearArea (display, window, x, y, width, height, true);
}
void redrawWidget (int x, int y, int width, int height, boolean all) {
	redrawHandle (x, y, width, height, handle);
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
/*
	"Release the IME."
	self isMenu ifFalse: [
		handle xtIsWidget ifTrue: [
			handle xmImUnregister.

			"Bug in X.  On Solaris only, destroying the window that has IME focus causes
			a segment fault.  The fix is to set focus to the IME client window (typically
			the shell) when the receiver is being destroyed.  Destroying the shell window
			does not have the problem.  Note that this fix is not necessary on AIX."
			(xIC := self inputContext) == nil ifFalse: [
				(window := handle xtWindow) isNull ifFalse: [
					xIC xGetICValues: XNFocusWindow with: (buffer := ByteArray new: 4) with: 0.
					(buffer uint32At: 0) = window asInteger ifTrue: [
						xIC xGetICValues: XNClientWindow with: (buffer := ByteArray new: 4) with: 0.
						xIC
							xUnsetICFocus;
							xSetICValues: XNFocusWindow with: (buffer uint32At: 0) with: 0;
							xSetICFocus]]]]].
*/
	parent = null;
	layoutData = null;
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
public void removeControlListener (ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
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
public void removeFocusListener(FocusListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.FocusIn, listener);
	eventTable.unhook(SWT.FocusOut, listener);
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
public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
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
public void removeKeyListener(KeyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
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
public void removeMouseListener(MouseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseDown, listener);
	eventTable.unhook(SWT.MouseUp, listener);
	eventTable.unhook(SWT.MouseDoubleClick, listener);
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
public void removeMouseMoveListener(MouseMoveListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseMove, listener);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void removeMouseTrackListener(MouseTrackListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
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
public void removePaintListener(PaintListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void removeTraverseListener(TraverseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}
void sendHelpEvent (int callData) {
	Control control = this;
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			return;
		}
		control = control.parent;
	}
}
void sendKeyEvent (int type, XKeyEvent xEvent) {
	
	/* Look up the keysym and character(s) */
	byte [] buffer;
	boolean isVirtual = false;
	int [] keysym = new int [1], status = new int [1];
	if (xEvent.keycode != 0) {
		buffer = new byte [1];
		isVirtual = OS.XLookupString (xEvent, buffer, buffer.length, keysym, status) == 0;
	} else {
		int size = 0;
		buffer = new byte [2];
		int xIC = inputContext ();
		if (xIC == 0) {
			size = OS.XmImMbLookupString (handle, xEvent, buffer, buffer.length, keysym, status);
			if (status [0] == OS.XBufferOverflow) {
				buffer = new byte [size];
				size = OS.XmImMbLookupString (handle, xEvent, buffer, size, keysym, status);
			}
		} else {
			size = OS.XmbLookupString (xIC, xEvent, buffer, buffer.length, keysym, status);
			if (status [0] == OS.XBufferOverflow) {
				buffer = new byte [size];
				size = OS.XmbLookupString (xIC, xEvent, buffer, size, keysym, status);
			}
		}
		if (size == 0) return;
	}

	/*
	* Bug in MOTIF.  On Solaris only, XK_F11 and XK_F12 are not
	* translated correctly by XLookupString().  They are mapped
	* to 0x1005FF10 and 0x1005FF11 respectively.  The fix is to
	* look for these values explicitly and correct them.
	*/
	if (IsSunOS) {
		if ((keysym [0] == 0x1005FF10) || (keysym [0] == 0x1005FF11)) {
			if (keysym [0] == 0x1005FF10) keysym [0] = OS.XK_F11;
			if (keysym [0] == 0x1005FF11) keysym [0] = OS.XK_F12;
		}
	}
	
	/*
	* Bug in MOTIF.  On Solaris only, their is garbage in the
	* high 16-bits for Keysyms such as XK_Down.  Since Keysyms
	* must be 16-bits to fit into a Character, mask away the
	* high 16-bits on all platforms.
	*/
	keysym [0] &= 0xFFFF;

	/* Convert from MBCS to UNICODE and send the event */
	char [] result = Converter.mbcsToWcs (null, buffer);
	for (int i=0; i<result.length; i++) {
		Event event = new Event ();
		event.time = xEvent.time;
		event.character = result [i];
		if (isVirtual) event.keyCode = Display.translateKey (keysym [0]);
		if ((xEvent.state & OS.Mod1Mask) != 0) event.stateMask |= SWT.ALT;
		if ((xEvent.state & OS.ShiftMask) != 0) event.stateMask |= SWT.SHIFT;
		if ((xEvent.state & OS.ControlMask) != 0) event.stateMask |= SWT.CONTROL;
		if ((xEvent.state & OS.Button1Mask) != 0) event.stateMask |= SWT.BUTTON1;
		if ((xEvent.state & OS.Button2Mask) != 0) event.stateMask |= SWT.BUTTON2;
		if ((xEvent.state & OS.Button3Mask) != 0) event.stateMask |= SWT.BUTTON3;
		postEvent (type, event);
	}
}
void sendMouseEvent (int type, int button, int mask, XWindowEvent xEvent) {
	Event event = new Event ();
	event.time = xEvent.time;
	event.button = button;
	event.x = xEvent.x;  event.y = xEvent.y;
	if ((mask & OS.Mod1Mask) != 0) event.stateMask |= SWT.ALT;
	if ((mask & OS.ShiftMask) != 0) event.stateMask |= SWT.SHIFT;
	if ((mask & OS.ControlMask) != 0) event.stateMask |= SWT.CONTROL;
	if ((mask & OS.Button1Mask) != 0) event.stateMask |= SWT.BUTTON1;
	if ((mask & OS.Button2Mask) != 0) event.stateMask |= SWT.BUTTON2;
	if ((mask & OS.Button3Mask) != 0) event.stateMask |= SWT.BUTTON3;
	postEvent (type, event);
}
public void setBackground (Color color) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (color == null) {
		setBackgroundPixel (defaultBackground ());
	} else {
		if (color.isDisposed ()) return;
		setBackgroundPixel (color.handle.pixel);
	}
}
void setBackgroundPixel (int pixel) {
	int [] argList = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmChangeColor (handle, pixel);
	OS.XtSetValues (handle, argList, argList.length / 2);
}
public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int topHandle = topHandle ();
	int [] argList = {
		OS.XmNx, 0, 			/* 1 */
		OS.XmNy, 0, 			/* 3 */
		OS.XmNwidth, 0, 		/* 5 */
		OS.XmNheight, 0, 		/* 7 */
		OS.XmNborderWidth, 0, 	/* 9 */
	};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int newWidth = Math.max (width - (argList [9] * 2), 1);
	int newHeight = Math.max (height - (argList [9] * 2), 1);
	boolean sameOrigin = (x == (short) argList [1]) && (y == (short) argList [3]);
	boolean sameExtent = (newWidth == argList [5]) && (newHeight == argList [7]);
	if (sameOrigin && sameExtent) return;
	OS.XtConfigureWidget (topHandle, x, y, newWidth, newHeight, argList [9]);
	if (!sameOrigin) sendEvent (SWT.Move);
	if (!sameExtent) sendEvent (SWT.Resize);
}
public void setBounds (Rectangle rect) {
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}
public void setCapture (boolean capture) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
}
public void setCursor (Cursor cursor) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) {
		if (OS.XtIsRealized (handle)) return;
		Shell shell = this.getShell ();
		shell.realizeWidget ();
		window = OS.XtWindow (handle);
		if (window == 0) return;
	}
	if (cursor == null) {
		OS.XUndefineCursor (display, window);
	} else {
		int xCursor = cursor.handle;
		OS.XDefineCursor (display, window, xCursor);
		OS.XFlush (display);
	}
}
public void setEnabled (boolean enabled) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	enableWidget (enabled);
	if (!enabled || (isEnabled () && enabled)) {
		propagateChildren (enabled);
	}
}
public boolean setFocus () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	shell.bringToTop ();
	return OS.XmProcessTraversal (handle, OS.XmTRAVERSE_CURRENT);
}
public void setFont (Font font) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int fontList = 0;
	if (font != null) fontList = font.handle;
	if (fontList == 0) fontList = defaultFont ();
	setFontList (fontList);
}
void setFontList (int fontList) {
	this.fontList = fontList;
	
	/*
	* Feature in Motif.  Setting the font in a widget
	* can cause the widget to automatically resize in
	* the OS.  This behavior is unwanted.  The fix is
	* to force the widget to resize to original size
	* after every font change.
	*/
	int [] argList1 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);

	/* Set the font list */
	int fontHandle = fontHandle ();
	int [] argList2 = {OS.XmNfontList, fontList};
	OS.XtSetValues (fontHandle, argList2, argList2.length / 2);

	/* Restore the widget size */
	OS.XtSetValues (handle, argList1, argList1.length / 2);
}
public void setForeground (Color color) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (color == null) {
		setForegroundPixel (defaultForeground ());
	} else {
		if (color.isDisposed ()) return;
		setForegroundPixel (color.handle.pixel);
	}
}
void setForegroundPixel (int pixel) {
	int [] argList = {OS.XmNforeground, pixel};
	OS.XtSetValues (handle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
}
void setGrabCursor (int cursor) {
	/*	| window attributes eventMask grabMask |
	handle xtIsWidget ifFalse: [^self].
	(window := handle xtWindow) isNull ifTrue: [^self].
	attributes := OSXWindowAttributesPtr new.
	XDisplay
		xGetWindowAttributes: window
		windowAttributesReturn: attributes.
	grabMask := ((((((((((ButtonPressMask bitOr: ButtonReleaseMask) bitOr:
 		EnterWindowMask) bitOr: LeaveWindowMask) bitOr: PointerMotionMask) bitOr:
		PointerMotionHintMask) bitOr: Button1MotionMask) bitOr: Button2MotionMask) bitOr:
		Button3MotionMask) bitOr: Button4MotionMask) bitOr: Button5MotionMask) bitOr: ButtonMotionMask.
	eventMask := attributes yourEventMask bitAnd: grabMask.
	XDisplay xChangeActivePointerGrab: eventMask cursor: aCursor time: CurrentTime.
	*/
}
void setKeyState (Event event, XKeyEvent xEvent) {
	if (xEvent.keycode != 0) {
		event.time = xEvent.time;
		byte [] buffer1 = new byte [1];
		int [] keysym = new int [1], status = new int [1];
		if (OS.XLookupString (xEvent, buffer1, buffer1.length, keysym, status) == 0) {
			event.keyCode = Display.translateKey (keysym [0] & 0xFFFF);
		} else {
			event.character = (char) buffer1 [0];
		}
		if ((xEvent.state & OS.Mod1Mask) != 0) event.stateMask |= SWT.ALT;
		if ((xEvent.state & OS.ShiftMask) != 0) event.stateMask |= SWT.SHIFT;
		if ((xEvent.state & OS.ControlMask) != 0) event.stateMask |= SWT.CONTROL;
		if ((xEvent.state & OS.Button1Mask) != 0) event.stateMask |= SWT.BUTTON1;
		if ((xEvent.state & OS.Button2Mask) != 0) event.stateMask |= SWT.BUTTON2;
		if ((xEvent.state & OS.Button3Mask) != 0) event.stateMask |= SWT.BUTTON3;
	}	
}
public void setLayoutData (Object layoutData) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.layoutData = layoutData;
}
public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	boolean sameOrigin = (x == (short) argList [1]) && (y == (short) argList [3]);
	if (sameOrigin) return;
	OS.XtMoveWidget (topHandle, x, y);
	if (!sameOrigin) sendEvent (SWT.Move);
}
public void setLocation (Point location) {
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}
/**
* Sets the pop up menu.
* <p>
* Every window has a optional pop up menu that is
* displayed when the user requests a popup menu for
* the window.  The sequence of key strokes/button
* presses/button releases that is used to request
* a pop up menu is platform specific.
*
* @param menu the new pop up menu
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_MENU_NOT_POP_UP)
*	when the menu is not a POP_UP
* @exception SWTError(ERROR_NO_COMMON_PARENT)
*	when the menu is not in the same widget tree
*/
public void setMenu (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (menu != null) {
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
 * Answers true if the parent is successfully changed.
 *
 * @param	parent Composite
 *			the new parent for the control.
 * @return	boolean
 *			true if parent is changed and false otherwise.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return false;
}

public void setRedraw (boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int topHandle = topHandle ();
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int newWidth = Math.max (width - (argList [5] * 2), 1);
	int newHeight = Math.max (height - (argList [5] * 2), 1);
	boolean sameExtent = (newWidth == argList [1]) && (newHeight == argList [3]);
	if (sameExtent) return;
	OS.XtResizeWidget (topHandle, newWidth, newHeight, argList [5]);
	if (!sameExtent) sendEvent (SWT.Resize);
}
public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
/**
* Sets the tool tip text.
* <p>
* @param string the new tool tip text (or null)
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
public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	if ((argList [1] != 0) == visible) return;
	OS.XtSetMappedWhenManaged (topHandle, visible);
	sendEvent (visible ? SWT.Show : SWT.Hide);
}
void setZOrder () {
	/*
	* Feature in MOTIF.  When a widget is created before the
	* parent has been realized, the widget is created behind
	* all siblings in the Z-order.  When a widget is created
	* after the parent parent has been realized, it is created
	* in front of all siblings.  This is not incorrect but is
	* unexpected.  The fix is to force all widgets to always
	* be created behind their siblings.
	*/
	int topHandle = topHandle ();
	if (OS.XtIsRealized (topHandle)) {
		int window = OS.XtWindow (topHandle);
		if (window != 0) {
			int display = OS.XtDisplay (topHandle);
			if (display != 0) OS.XLowerWindow (display, window);
		}
	}
}
void setZOrder (Control control, boolean above) {
	/*
	* Feature in Xt.  We cannot use XtMakeGeometryRequest() to
	* restack widgets because this call can fail under certain
	* conditions.  For example, XtMakeGeometryRequest() answers
	* XtGeometryNo when attempting to bring a child widget that
	* is larger than the parent widget to the front.  The fix
	* is to use X calls instead.
	*/
	int topHandle1 = topHandle ();
	int display = OS.XtDisplay (topHandle1);
	if (display == 0) return;
	if (!OS.XtIsRealized (topHandle1)) {
		Shell shell = this.getShell ();
		shell.realizeWidget ();
	}
	int window1 = OS.XtWindow (topHandle1);
	if (window1 == 0) return;
	if (control == null) {
		if (above) {
			OS.XRaiseWindow (display, window1);
			if (parent != null) parent.moveAbove (topHandle1, 0);
		} else {
			OS.XLowerWindow (display, window1);
			if (parent != null) parent.moveBelow (topHandle1, 0);
		}
		return;
	}
	int topHandle2 = control.topHandle ();
	if (display != OS.XtDisplay (topHandle2)) return;
	if (!OS.XtIsRealized (topHandle2)) {
		Shell shell = control.getShell ();
		shell.realizeWidget ();
	}
	int window2 = OS.XtWindow (topHandle2);
	if (window2 == 0) return;
	XWindowChanges struct = new XWindowChanges ();
	struct.sibling = window2;
	struct.stack_mode = above ? OS.Above : OS.Below;
	/*
	* Feature in X. If the receiver is a top level, XConfigureWindow ()
	* will fail (with a BadMatch error) for top level shells because top
	* level shells are reparented by the window manager and do not share
	* the same X window parent.  This is the correct behavior but it is
	* unexpected.  The fix is to use XReconfigureWMWindow () instead.
	* When the receiver is not a top level shell, XReconfigureWMWindow ()
	* behaves the same as XConfigureWindow ().
	*/
	int screen = OS.XDefaultScreen (display);
	int flags = OS.CWStackMode | OS.CWSibling;
	OS.XReconfigureWMWindow (display, window1, screen, flags, struct);
	if (above) {
		if (parent != null) parent.moveAbove (topHandle1, topHandle2);
	} else {
		if (parent != null) parent.moveBelow (topHandle1, topHandle2);
	}
}
public Point toControl (Point point) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
	return new Point (point.x - root_x [0], point.y - root_y [0]);
}
public Point toDisplay (Point point) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) point.x, (short) point.y, root_x, root_y);
	return new Point (root_x [0], root_y [0]);
}
boolean translateMnemonic (int aKey, XKeyEvent xEvent) {
	if (xEvent.state != OS.Mod1Mask) {
		if (xEvent.state != 0 || !(this instanceof Button)) {
			return false;
		}
	}
	Decorations shell = menuShell ();
	if (!shell.isVisible () || !shell.isEnabled ()) return false;
	char ch = mbcsToWcs ((char) aKey);
	return ch != 0 && shell.traverseMnemonic (ch);
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	int detail = 0;
	switch (key) {
		case OS.XK_Escape:
		case OS.XK_Cancel:
			Shell shell = getShell ();
			if (shell.parent == null) return false;
			if (!shell.isVisible () || !shell.isEnabled ()) return false;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		case OS.XK_Return:
			Button button = menuShell ().getDefaultButton ();
			if (button == null || button.isDisposed ()) return false;
			if (!button.isVisible () || !button.isEnabled ()) return false;
			detail = SWT.TRAVERSE_RETURN;
			break;
		case OS.XK_Tab:
			detail = SWT.TRAVERSE_TAB_PREVIOUS;
			boolean next = (xEvent.state & OS.ShiftMask) == 0;
			if (next && ((xEvent.state & OS.ControlMask) != 0)) return false;
			if (next) detail = SWT.TRAVERSE_TAB_NEXT;
			break;
		case OS.XK_Up:
		case OS.XK_Left: 
			detail = SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		case OS.XK_Down:
		case OS.XK_Right:
			detail = SWT.TRAVERSE_ARROW_NEXT;
			break;
		default:
			return false;
	}
	boolean doit = (detail & traversalCode ()) != 0;
	if (hooks (SWT.Traverse)) {
		Event event = new Event();
		event.doit = doit;
		event.detail = detail;
		setKeyState (event, xEvent);
		sendEvent (SWT.Traverse, event);
		doit = event.doit;
		detail = event.detail;
	}
	/*
	* NOTE:  The native widgets handle tab and arrow key traversal
	* so it is not necessary to traverse these keys.  A canvas widget
	* has no native traversal by definition so it is necessary to
	* traverse all keys.
	*/
	if (doit) {
		int flags = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_ESCAPE;
		if ((detail & flags) != 0 || (state & CANVAS) != 0) {
			return traverse (detail);
		}
	}
	return false;
}
int traversalCode () {
	int code = SWT.TRAVERSE_ESCAPE | SWT.TRAVERSE_RETURN;
	int [] argList = {OS.XmNnavigationType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == OS.XmNONE) {
		code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	} else {
		code |= SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS;
	}
	return code;
}
boolean traverseMnemonic (char key) {
	if (!isVisible () || !isEnabled ()) return false;
	return mnemonicMatch (key) && mnemonicHit ();
}
/**
* Traverse the widget.
* <p>
* @param traversal the type of traversal.
* @return true if the traversal succeeded
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public boolean traverse (int traversal) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!isFocusControl () && !setFocus ()) return false;
	switch (traversal) {
		case SWT.TRAVERSE_ESCAPE:		return traverseEscape ();
		case SWT.TRAVERSE_RETURN:		return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);	
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
	return OS.XmProcessTraversal (handle, next ? OS.XmTRAVERSE_NEXT_TAB_GROUP : OS.XmTRAVERSE_PREV_TAB_GROUP);
}
boolean traverseItem (boolean next) {
	return OS.XmProcessTraversal (handle, next ? OS.XmTRAVERSE_NEXT : OS.XmTRAVERSE_PREV);
}
boolean traverseReturn () {
	Button button = menuShell ().getDefaultButton ();
	if (button == null || button.isDisposed ()) return false;
	if (!button.isVisible () || !button.isEnabled ()) return false;
	button.click ();
	return true;
}
public void update () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	XAnyEvent event = new XAnyEvent ();
	OS.XSync (display, false);  OS.XSync (display, false);
	while (OS.XCheckWindowEvent (display, window, OS.ExposureMask, event)) {
		OS.XtDispatchEvent (event);
	}
}
int processMouseHover (int id) {
	Display display = getDisplay();
	Event event = new Event();
	Point local = toControl(display.getCursorLocation());
	event.x = local.x; event.y = local.y;
	postEvent (SWT.MouseHover, event);
	display.showToolTip(handle, toolTipText);
	return 0;
}
}