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


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.RGBColor;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.PixMap;
import org.eclipse.swt.internal.carbon.BitMap;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * This class is the abstract superclass of all user interface objects.  
 * Widgets are created, disposed and issue notification to listeners
 * when events occur which affect them.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation. However, it has not been marked
 * final to allow those outside of the SWT development team to implement
 * patched versions of the class in order to get around specific
 * limitations in advance of when those limitations can be addressed
 * by the team.  Any class built using subclassing to access the internals
 * of this class will likely fail to compile or run between releases and
 * may be strongly platform specific. Subclassing should not be attempted
 * without an intimate and detailed understanding of the workings of the
 * hierarchy. No support is provided for user-written classes which are
 * implemented as subclasses of this class.
 * </p>
 *
 * @see #checkSubclass
 */
public abstract class Widget {
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;

	/* Global state flags */
	static final int DISPOSED		= 1 << 0;
	static final int CANVAS			= 1 << 1;
	static final int KEYED_DATA		= 1 << 2;
	static final int DISABLED		= 1 << 3;
	static final int HIDDEN			= 1 << 4;
	static final int GRAB			= 1 << 5;
	static final int MOVED			= 1 << 6;
	static final int RESIZED		= 1 << 7;

	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
	static final Rect EMPTY_RECT = new Rect ();

Widget () {
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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
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
 * @see #checkSubclass
 * @see #getStyle
 */
public Widget (Widget parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = style;
	display = parent.display;
}

int actionProc (int theControl, int partCode) {
	return OS.noErr;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. When the
 * event does occur in the widget, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #removeListener
 */
public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when the widget is disposed. When the widget is
 * disposed, the listener is notified by sending it the
 * <code>widgetDisposed()</code> message.
 *
 * @param listener the listener which should be notified when the receiver is disposed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DisposeListener
 * @see #removeDisposeListener
 */
public void addDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
}

static int checkBits (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0) style |= int0;
	if ((style & int0) != 0) style = (style & ~mask) | int0;
	if ((style & int1) != 0) style = (style & ~mask) | int1;
	if ((style & int2) != 0) style = (style & ~mask) | int2;
	if ((style & int3) != 0) style = (style & ~mask) | int3;
	if ((style & int4) != 0) style = (style & ~mask) | int4;
	if ((style & int5) != 0) style = (style & ~mask) | int5;
	return style;
}

void calculateVisibleRegion (int control, int visibleRgn, boolean clipChildren) {
	int tempRgn = OS.NewRgn ();
	if (OS.IsControlVisible (control)) {
		int childRgn = OS.NewRgn ();
		int window = OS.GetControlOwner (control);
		short [] count = new short [1];
		int [] outControl = new int [1];
		OS.GetRootControl (window, outControl);
		int root = outControl [0];
		OS.GetControlRegion (root, (short) OS.kControlStructureMetaPart, visibleRgn);
		int tempControl = control, lastControl = 0;
		while (tempControl != root) {
			OS.GetControlRegion (tempControl, (short) OS.kControlStructureMetaPart, tempRgn);
			OS.SectRgn (tempRgn, visibleRgn, visibleRgn);
			if (OS.EmptyRgn (visibleRgn)) break;
			if (clipChildren || tempControl != control) {
				OS.CountSubControls (tempControl, count);
				for (int i = 0; i < count [0]; i++) {
					OS.GetIndexedSubControl (tempControl, (short)(i + 1), outControl);
					int child = outControl [0];
					if (child == lastControl) break;
					if (!OS.IsControlVisible (child)) continue;
					OS.GetControlRegion (child, (short) OS.kControlStructureMetaPart, tempRgn);
					OS.UnionRgn (tempRgn, childRgn, childRgn);
				}
			}
			lastControl = tempControl;
			OS.GetSuperControl (tempControl, outControl);
			tempControl = outControl [0];
		}
		OS.DiffRgn (visibleRgn, childRgn, visibleRgn);
		OS.DisposeRgn (childRgn);
	} else {
		OS.CopyRgn (tempRgn, visibleRgn);
	}
	OS.DisposeRgn (tempRgn);
}

void checkOrientation (Widget parent) {
	style &= ~SWT.MIRRORED;
	if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
		if (parent != null) {
			if ((parent.style & SWT.LEFT_TO_RIGHT) != 0) style |= SWT.LEFT_TO_RIGHT;
			if ((parent.style & SWT.RIGHT_TO_LEFT) != 0) style |= SWT.RIGHT_TO_LEFT;
		}
	}
	style = checkBits (style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
}

void checkParent (Widget parent) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	parent.checkWidget ();
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * The SWT class library is intended to be subclassed 
 * only at specific, controlled points (most notably, 
 * <code>Composite</code> and <code>Canvas</code> when
 * implementing new widgets). This method enforces this
 * rule unless it is overridden.
 * </p><p>
 * <em>IMPORTANT:</em> By providing an implementation of this
 * method that allows a subclass of a class which does not 
 * normally allow subclassing to be created, the implementer
 * agrees to be fully responsible for the fact that any such
 * subclass will likely fail between SWT releases and will be
 * strongly platform specific. No support is provided for
 * user-written classes which are implemented in this fashion.
 * </p><p>
 * The ability to subclass outside of the allowed SWT classes
 * is intended purely to enable those not on the SWT development
 * team to implement patches in order to get around specific
 * limitations in advance of when those limitations can be
 * addressed by the team. Subclassing should not be attempted
 * without an intimate and detailed understanding of the hierarchy.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * widget implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code>) on a widget that has had its 
 * <code>dispose()</code> method called. It is also an error
 * to call widget methods from any thread that is different
 * from the thread that created the widget.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected void checkWidget () {
	Display display = this.display;
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (display.thread != Thread.currentThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if ((state & DISPOSED) != 0) error (SWT.ERROR_WIDGET_DISPOSED);
}

int colorProc (int inControl, int inMessage, int inDrawDepth, int inDrawInColor) {
	return OS.eventNotHandledErr;
}

int controlProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventControlActivate:				return kEventControlActivate (nextHandler, theEvent, userData);
		case OS.kEventControlApplyBackground:		return kEventControlApplyBackground (nextHandler, theEvent, userData);
		case OS.kEventControlBoundsChanged:			return kEventControlBoundsChanged (nextHandler, theEvent, userData);
		case OS.kEventControlClick:					return kEventControlClick (nextHandler, theEvent, userData);
		case OS.kEventControlContextualMenuClick:	return kEventControlContextualMenuClick (nextHandler, theEvent, userData);
		case OS.kEventControlDeactivate:			return kEventControlDeactivate (nextHandler, theEvent, userData);
		case OS.kEventControlDraw:					return kEventControlDraw (nextHandler, theEvent, userData);
		case OS.kEventControlHit:					return kEventControlHit (nextHandler, theEvent, userData);
		case OS.kEventControlSetCursor:				return kEventControlSetCursor (nextHandler, theEvent, userData);
		case OS.kEventControlSetFocusPart:			return kEventControlSetFocusPart (nextHandler, theEvent, userData);
		case OS.kEventControlTrack:					return kEventControlTrack (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int createCIcon (Image image) {
	int imageHandle = image.handle;
	int width = OS.CGImageGetWidth(imageHandle);
	int height = OS.CGImageGetHeight(imageHandle);
	int bpr = OS.CGImageGetBytesPerRow(imageHandle);
	int bpp = OS.CGImageGetBitsPerPixel(imageHandle);
	int bpc = OS.CGImageGetBitsPerComponent(imageHandle);
	int alphaInfo = OS.CGImageGetAlphaInfo(imageHandle);
	
	int maskBpl = (((width + 7) / 8) + 3) / 4 * 4;
	int maskSize = height * maskBpl;
	int pixmapSize = height * bpr;
	
	/* Create the icon */
	int iconSize = PixMap.sizeof + BitMap.sizeof * 2 + 4 + maskSize;
	int iconHandle = OS.NewHandle(iconSize);
	if (iconHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.HLock(iconHandle);
	int[] iconPtr = new int[1];
	OS.memcpy(iconPtr, iconHandle, 4);

	/* Initialize the pixmap */
	PixMap iconPMap = new PixMap();
	iconPMap.rowBytes = (short)(bpr | 0x8000);
	iconPMap.right = (short)width;
	iconPMap.bottom = (short)height;
	iconPMap.cmpCount = 3;
	iconPMap.cmpSize = (short)bpc;
	iconPMap.pmTable = OS.NewHandle(0);
	iconPMap.hRes = 72 << 16;
	iconPMap.vRes = 72 << 16;
	iconPMap.pixelType = (short)OS.RGBDirect;
	iconPMap.pixelSize = (short)bpp;
	iconPMap.pixelFormat = (short)bpp;
	OS.memcpy(iconPtr[0], iconPMap, PixMap.sizeof);

	/* Initialize the mask */
	BitMap iconMask = new BitMap();
	iconMask.rowBytes = (short)maskBpl;
	iconMask.right = (short)width;
	iconMask.bottom = (short)height;
	OS.memcpy(iconPtr[0] + PixMap.sizeof, iconMask, BitMap.sizeof);

	/* Initialize the icon data */
	int iconData = OS.NewHandle(pixmapSize);
	OS.HLock(iconData);
	int[] iconDataPtr = new int[1];
	OS.memcpy(iconDataPtr, iconData, 4);
	OS.memcpy(iconDataPtr[0], image.data, pixmapSize);
	OS.HUnlock(iconData);
	OS.memcpy(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof, new int[]{iconData}, 4);

	/* Initialize the mask data */
	if (alphaInfo != OS.kCGImageAlphaFirst) {
		OS.memset(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof + 4, -1, maskSize);
	} else {
		byte[] srcData = new byte[pixmapSize];
		OS.memcpy(srcData, image.data, pixmapSize);
		byte[] maskData = new byte[maskSize];
		int offset = 0, maskOffset = 0;
		for (int y = 0; y<height; y++) {
			for (int x = 0; x<width; x++) {
				if ((srcData[offset] & 0xFF) > 128) {
					maskData[maskOffset + (x >> 3)] |= (1 << (7 - (x & 0x7)));
				} else {
					maskData[maskOffset + (x >> 3)] &= ~(1 << (7 - (x & 0x7)));
				}
				offset += 4;
			}
			maskOffset += maskBpl;
		}
		OS.memcpy(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof + 4, maskData, maskData.length);
	}
	
	OS.HUnlock(iconHandle);	
	return iconHandle;
}

void createHandle () {
}

void createWidget () {
	createHandle ();
	register ();
	hookEvents ();
}

int commandProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventProcessCommand:	return kEventProcessCommand (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}
	
void deregister () {
}

void destroyWidget () {
	releaseHandle ();
}

void destroyCIcon (int iconHandle) {
	OS.HLock(iconHandle);
	
	/* Dispose the ColorTable */
	int[] iconPtr = new int[1];
	OS.memcpy(iconPtr, iconHandle, 4);	
	PixMap iconPMap = new PixMap();
	OS.memcpy(iconPMap, iconPtr[0], PixMap.sizeof);
	if (iconPMap.pmTable != 0) OS.DisposeHandle(iconPMap.pmTable);

	/* Dispose the icon data */
	int[] iconData = new int[1];
	OS.memcpy(iconData, iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof, 4);
	if (iconData[0] != 0) OS.DisposeHandle(iconData[0]);
	
	OS.HUnlock(iconHandle);
	
	/* Dispose the icon */
	OS.DisposeHandle(iconHandle);
}

int drawItemProc (int browser, int item, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	return OS.noErr;
}

/**
 * Disposes of the operating system resources associated with
 * the receiver and all its descendents. After this method has
 * been invoked, the receiver and all descendents will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
 * <p>
 * NOTE: This method is not called recursively on the descendents
 * of the receiver. This means that, widget implementers can not
 * detect when a widget is being disposed of by re-implementing
 * this method, but should instead listen for the <code>Dispose</code>
 * event.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #addDisposeListener
 * @see #removeDisposeListener
 * @see #checkWidget
 */
public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
}

void drawBackground (int control) {
	/* Do nothing */
}

void drawBackground (int control, float [] background) {
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	if (background != null) {
		OS.RGBForeColor (toRGBColor (background));
		OS.PaintRect (rect);
	} else {
		OS.SetThemeBackground((short) OS.kThemeBrushDialogBackgroundActive, (short) 0, true);
		OS.EraseRect (rect);
	}
}

void drawFocus (int control, boolean hasFocus, boolean hasBorder, float[] background, Rect inset) {
	drawBackground (control, background);
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	rect.left += inset.left;
	rect.top += inset.top;
	rect.right -= inset.right;
	rect.bottom -= inset.bottom;
	int state = OS.IsControlActive (control) ? OS.kThemeStateActive : OS.kThemeStateInactive;
	if (hasFocus) {
		if (hasBorder) OS.DrawThemeEditTextFrame (rect, state);
		OS.DrawThemeFocusRect (rect, true);
	} else {
		/*
		* This code is intentionaly commented.
		*  
		* NOTE: the focus ring is erased by drawBackground() above. 
		*/
//		OS.DrawThemeFocusRect (rect, false);
		if (hasBorder) OS.DrawThemeEditTextFrame (rect, state);
	}
}

void drawFocusClipped (int control, boolean hasFocus, boolean hasBorder, float[] background, Rect inset) {
	int visibleRgn = getVisibleRegion (control, true);
	if (!OS.EmptyRgn (visibleRgn)) {
		int [] currentPort = new int [1];
		OS.GetPort (currentPort);
		int window = OS.GetControlOwner (control);
		int port = OS.GetWindowPort (window);
		OS.SetPort (port);
		int oldClip = OS.NewRgn ();
		OS.GetClip (oldClip);
		OS.SetClip (visibleRgn);
		drawFocus (control, hasFocus, hasBorder, background, inset);
		OS.SetClip (oldClip);
		OS.SetPort (currentPort [0]);
	}
	OS.DisposeRgn (visibleRgn);
}

void drawWidget (int control, int damageRgn, int visibleRgn, int theEvent) {
}

void error (int code) {
	SWT.error(code);
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

int fixMnemonic (char [] buffer) {
	int i=0, j=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == '&') {
			if (i == buffer.length) {continue;}
			if (buffer [i] == '&') {i++; continue;}
			j--;
		}
	}
	return j;
}

Rect getControlBounds (int control) {
	Rect rect = new Rect();
	OS.GetControlBounds (control, rect);
	int window = OS.GetControlOwner (control);
	int [] theRoot = new int [1];
	OS.GetRootControl (window, theRoot);
	int [] parentHandle = new int [1];
	OS.GetSuperControl (control, parentHandle);
	if (parentHandle [0] != theRoot [0]) {
		Rect parentRect = new Rect ();
		OS.GetControlBounds (parentHandle [0], parentRect);
		OS.OffsetRect (rect, (short) -parentRect.left, (short) -parentRect.top);
	}
	Rect inset = getInset ();
	rect.left -= inset.left;
	rect.top -= inset.top;
	rect.right += inset.right;
	rect.bottom += inset.bottom;
	return rect;
}

Rect getControlSize (int control) {
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	Rect inset = getInset ();
	rect.left -= inset.left;
	rect.top -= inset.top;
	rect.right += inset.right;
	rect.bottom += inset.bottom;
	return rect;
}

/**
 * Returns the application defined widget data associated
 * with the receiver, or null if it has not been set. The
 * <em>widget data</em> is a single, unnamed field that is
 * stored with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @return the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 *
 * @see #setData
 */
public Object getData () {
	checkWidget();
	return (state & KEYED_DATA) != 0 ? ((Object []) data) [0] : data;
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param	key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setData
 */
public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((state & KEYED_DATA) != 0) {
		Object [] table = (Object []) data;
		for (int i=1; i<table.length; i+=2) {
			if (key.equals (table [i])) return table [i+1];
		}
	}
	return null;
}

/**
 * Returns the <code>Display</code> that is associated with
 * the receiver.
 * <p>
 * A widget's display is either provided when it is created
 * (for example, top level <code>Shell</code>s) or is the
 * same as its parent's display.
 * </p>
 *
 * @return the receiver's display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Display getDisplay () {
	Display display = this.display;
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

int getDrawCount (int control) {
	return 0;
}

Rect getInset () {
	return EMPTY_RECT;
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf(".");
	if (index == -1) return string;
	return string.substring(index + 1, string.length ());
}

String getNameText () {
	return "";
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles. For example, if the platform widget used to
 * implement a particular SWT widget always has scroll bars, the
 * result of calling this method would always have the
 * <code>SWT.H_SCROLL</code> and <code>SWT.V_SCROLL</code> bits set.
 * </p>
 *
 * @return the style bits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getStyle () {
	checkWidget();
	return style;
}

int getVisibleRegion (int control, boolean clipChildren) {
	int visibleRgn = OS.NewRgn ();
	calculateVisibleRegion (control, visibleRgn, clipChildren);
	return visibleRgn;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	return OS.eventNotHandledErr;
}

int hitTestProc (int browser, int item, int property, int theRect, int mouseRect) {
	/* Return true to indicate that the item can be selected */
	return 1;
}

void hookEvents () {
}

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

void invalidateVisibleRegion (int control) {
}

/**
 * Returns <code>true</code> if the widget has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the widget.
 * When a widget has been disposed, it is an error to
 * invoke any other method using the widget.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return (state & DISPOSED) != 0;
}

boolean isDrawing (int control) {
	return OS.IsControlVisible (control) && getDrawCount (control) == 0;
}

boolean isEnabled () {
	return true;
}

/**
 * Returns <code>true</code> if there are any listeners
 * for the specified event type associated with the receiver,
 * and <code>false</code> otherwise.
 *
 * @param	eventType the type of event
 * @return true if the event is hooked
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected boolean isListening (int eventType) {
	checkWidget();
	return hooks (eventType);
}

boolean isTrimHandle (int trimHandle) {
	return false;
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

int itemCompareProc (int browser, int itemOne, int itemTwo, int sortProperty) {
	return OS.noErr;
}

int itemDataProc (int browser, int item, int property, int itemData, int setValue) {
	return OS.noErr;
}

int itemNotificationProc (int browser, int item, int message) {
	return OS.noErr;
}

int kEventProcessCommand (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlApplyBackground (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}
	
int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlBoundsChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlDeactivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	if (getDrawCount (theControl [0]) > 0) return OS.noErr;
	int [] region = new int [1];	
	OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, region);
	int visibleRgn = getVisibleRegion (theControl [0], true);
	OS.SectRgn(region [0], visibleRgn, visibleRgn);
	if (!OS.EmptyRgn (visibleRgn)) {
		int [] port = new int [1];
		OS.GetPort (port);
		OS.LockPortBits (port [0]);
//		OS.QDSetDirtyRegion (port, visibleRgn);
		int oldClip = OS.NewRgn ();
		OS.GetClip (oldClip);
		OS.SetClip (visibleRgn);
		drawBackground (theControl [0]);
		OS.CallNextEventHandler (nextHandler, theEvent);
		drawWidget (theControl [0], region [0], visibleRgn, theEvent);
		OS.SetClip (oldClip);
		OS.DisposeRgn (oldClip);
		OS.UnlockPortBits (port [0]);
	}
	OS.DisposeRgn (visibleRgn);
	return OS.noErr;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuClosed (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuDrawItemContent (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuMeasureItemWidth (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuOpening (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuTargetItem (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseWheelMoved (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyUp (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyModifiersChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowBoundsChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowClose (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowGetRegion (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowHidden (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowHitTest (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowShown (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int keyboardProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventRawKeyDown:				return kEventRawKeyDown (nextHandler, theEvent, userData);
		case OS.kEventRawKeyModifiersChanged:	return kEventRawKeyModifiersChanged (nextHandler, theEvent, userData);
		case OS.kEventRawKeyRepeat:			return kEventRawKeyRepeat (nextHandler, theEvent, userData);
		case OS.kEventRawKeyUp:				return kEventRawKeyUp (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int menuProc (int nextHandler, int theEvent, int userData) {	
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventMenuClosed:		return kEventMenuClosed (nextHandler, theEvent, userData);
		case OS.kEventMenuDrawItemContent: 	return kEventMenuDrawItemContent (nextHandler, theEvent, userData);
		case OS.kEventMenuMeasureItemWidth: return kEventMenuMeasureItemWidth (nextHandler, theEvent, userData);
		case OS.kEventMenuOpening:		return kEventMenuOpening (nextHandler, theEvent, userData);
		case OS.kEventMenuTargetItem:	return kEventMenuTargetItem (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int mouseProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventMouseDown: 		return kEventMouseDown (nextHandler, theEvent, userData);
		case OS.kEventMouseUp: 		return kEventMouseUp (nextHandler, theEvent, userData);
		case OS.kEventMouseDragged:	return kEventMouseDragged (nextHandler, theEvent, userData);
//		case OS.kEventMouseEntered:		return kEventMouseEntered (nextHandler, theEvent, userData);
//		case OS.kEventMouseExited:		return kEventMouseExited (nextHandler, theEvent, userData);
		case OS.kEventMouseMoved:		return kEventMouseMoved (nextHandler, theEvent, userData);
		case OS.kEventMouseWheelMoved:	return kEventMouseWheelMoved (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) event = new Event ();
	sendEvent (eventType, event);
}

void postEvent (int eventType) {
	sendEvent (eventType, null, false);
}

void postEvent (int eventType, Event event) {
	sendEvent (eventType, event, false);
}

void redrawWidget (int control, boolean children) {
	if (!isDrawing (control)) return;
	int window = OS.GetControlOwner (control);
	int visibleRgn = getVisibleRegion (control, !children);
	OS.InvalWindowRgn (window, visibleRgn);
	OS.DisposeRgn (visibleRgn);
}

void redrawWidget (int control, int x, int y, int width, int height, boolean children) {
	if (!isDrawing (control)) return;
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	x += rect.left;
	y += rect.top;
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	int rectRgn = OS.NewRgn();
	OS.RectRgn (rectRgn, rect);
	int visibleRgn = getVisibleRegion (control, !children);
	OS.SectRgn (rectRgn, visibleRgn, visibleRgn);
	int window = OS.GetControlOwner (control);
	OS.InvalWindowRgn (window, visibleRgn);
	OS.DisposeRgn (rectRgn);
	OS.DisposeRgn (visibleRgn);
}

void register () {
}

void releaseChild () {
	/* Do nothing */
}

void releaseHandle () {
	state |= DISPOSED;
	display = null;
}

void releaseResources () {
	releaseWidget ();
	releaseHandle ();
}

void releaseWidget () {
	sendEvent (SWT.Dispose);
	deregister ();
	eventTable = null;
	data = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addListener
 */
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addListener
 */
protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when the widget is disposed.
 *
 * @param listener the listener which should no longer be notified when the receiver is disposed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DisposeListener
 * @see #addDisposeListener
 */
public void removeDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void sendEvent (Event event) {
	Display display = event.display;
	if (!display.filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

void sendEvent (int eventType) {
	sendEvent (eventType, null, true);
}

void sendEvent (int eventType, Event event) {
	sendEvent (eventType, event, true);
}

void sendEvent (int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
		event.time = display.getLastEventTime ();
	}
	if (send) {
		sendEvent (event);
	} else {
		display.postEvent (event);
	}
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	/* Compute the old bounds */
	Rect oldBounds = new Rect ();
	OS.GetControlBounds (control, oldBounds);
	int [] theRoot = new int [1];
	int window = OS.GetControlOwner (control);
	OS.GetRootControl (window, theRoot);
	int [] parentHandle = new int [1];
	OS.GetSuperControl (control, parentHandle);
	Rect parentRect = new Rect ();
	if (parentHandle [0] != theRoot [0]) {
		OS.GetControlBounds (parentHandle [0], parentRect);
		OS.OffsetRect (oldBounds, (short) -parentRect.left, (short) -parentRect.top);
	}
	Rect inset = getInset ();
	oldBounds.left -= inset.left;
	oldBounds.top -= inset.top;
	oldBounds.right += inset.right;
	oldBounds.bottom += inset.bottom;
	
	/* Compute the new bounds */
	if (!move) {
		x = oldBounds.left;
		y = oldBounds.top;
	}
	if (!resize) {
		width = oldBounds.right - oldBounds.left;
		height = oldBounds.bottom - oldBounds.top;
	}	
	Rect newBounds = new Rect ();
	newBounds.left = (short) (parentRect.left + x + inset.left);
	newBounds.top = (short) (parentRect.top + y + inset.top);
	newBounds.right = (short) (newBounds.left + width - inset.right - inset.left);
	newBounds.bottom = (short) (newBounds.top + height - inset.bottom - inset.top);
	
	/* Get bounds again, since the one above is in SWT coordinates */
	OS.GetControlBounds (control, oldBounds);
	
	/* Check if anything changed */
	boolean sameOrigin = newBounds.left == oldBounds.left && newBounds.top == oldBounds.top;
	boolean sameExtent = (newBounds.right - newBounds.left) == (oldBounds.right - oldBounds.left) && (newBounds.bottom - newBounds.top) == (oldBounds.bottom - oldBounds.top);
	if (sameOrigin && sameExtent) return 0;

	/* Apply changes and invalidate appropriate rectangles */
	int tempRgn = 0;
	boolean visible = OS.IsControlVisible (control);
	if (visible) {
		tempRgn = OS.NewRgn ();
		OS.GetControlRegion (control, (short) OS.kControlStructureMetaPart, tempRgn);
		OS.InvalWindowRgn (window, tempRgn);
	}
	OS.SetControlBounds (control, newBounds);
	invalidateVisibleRegion (control);
	if (visible) {
		OS.GetControlRegion (control, (short) OS.kControlStructureMetaPart, tempRgn);
		OS.InvalWindowRgn (window, tempRgn);
		OS.DisposeRgn(tempRgn);
	}
	
	/* Send events */
	int result = 0;
	if (move && !sameOrigin) {
		if (events) sendEvent (SWT.Move);
		result |= MOVED;
	}
	if (resize && !sameExtent) {
		if (events) sendEvent (SWT.Resize);
		result |= RESIZED;
	}
	return result;
}

/**
 * Sets the application defined widget data associated
 * with the receiver to be the argument. The <em>widget
 * data</em> is a single, unnamed field that is stored
 * with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @param data the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 */
public void setData (Object data) {
	checkWidget();
	if ((state & KEYED_DATA) != 0) {
		((Object []) this.data) [0] = data;
	} else {
		this.data = data;
	}
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given value.
 * <p>
 * Applications may associate arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getData
 */
public void setData (String key, Object value) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = 1;
	Object [] table = null;
	if ((state & KEYED_DATA) != 0) {
		table = (Object []) data;
		while (index < table.length) {
			if (key.equals (table [index])) break;
			index += 2;
		}
	}
	if (value != null) {
		if ((state & KEYED_DATA) != 0) {
			if (index == table.length) {
				Object [] newTable = new Object [table.length + 2];
				System.arraycopy (table, 0, newTable, 0, table.length);
				data = table = newTable;
			}
		} else {
			table = new Object [3];
			table [0] = data;
			data = table;
			state |= KEYED_DATA;
		}
		table [index] = key;
		table [index + 1] = value;
	} else {
		if ((state & KEYED_DATA) != 0) {
			if (index != table.length) {
				int length = table.length - 2;
				if (length == 1) {
					data = table [0];
					state &= ~KEYED_DATA;
				} else {
					Object [] newTable = new Object [length];
					System.arraycopy (table, 0, newTable, 0, index);
					System.arraycopy (table, index + 2, newTable, index, length - index);
					data = newTable;
				}
			}
		}
	}
}

boolean setInputState (Event event, int type, int chord, int modifiers) {
	if ((chord & 0x01) != 0) event.stateMask |= SWT.BUTTON1;
	if ((chord & 0x02) != 0) event.stateMask |= SWT.BUTTON3;
	if ((chord & 0x04) != 0) event.stateMask |= SWT.BUTTON2;
	if ((modifiers & OS.optionKey) != 0) event.stateMask |= SWT.ALT;
	if ((modifiers & OS.shiftKey) != 0) event.stateMask |= SWT.SHIFT;
	if ((modifiers & OS.controlKey) != 0) event.stateMask |= SWT.CONTROL;
	if ((modifiers & OS.cmdKey) != 0) event.stateMask |= SWT.COMMAND;
	switch (type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3)  event.stateMask &= ~SWT.BUTTON3;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) event.stateMask |= SWT.BUTTON3;
			break;
		case SWT.KeyDown:
		case SWT.Traverse: {
			if (event.keyCode != 0 || event.character != 0) return true;
			int lastModifiers = display.lastModifiers;
			if ((modifiers & OS.alphaLock) != 0 && (lastModifiers & OS.alphaLock) == 0) {
				event.keyCode = SWT.CAPS_LOCK;
				return true;
			}
			if ((modifiers & OS.shiftKey) != 0 && (lastModifiers & OS.shiftKey) == 0) {
				event.stateMask &= ~SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return true;
			}
			if ((modifiers & OS.controlKey) != 0 && (lastModifiers & OS.controlKey) == 0) {
				event.stateMask &= ~SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return true;
			}
			if ((modifiers & OS.cmdKey) != 0 && (lastModifiers & OS.cmdKey) == 0) {
				event.stateMask &= ~SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return true;
			}	
			if ((modifiers & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) {
				event.stateMask &= ~SWT.ALT;
				event.keyCode = SWT.ALT;
				return true;
			}
			break;
		}
		case SWT.KeyUp: {
			if (event.keyCode != 0 || event.character != 0) return true;
			int lastModifiers = display.lastModifiers;
			if ((modifiers & OS.alphaLock) == 0 && (lastModifiers & OS.alphaLock) != 0) {
				event.keyCode = SWT.CAPS_LOCK;
				return true;
			}
			if ((modifiers & OS.shiftKey) == 0 && (lastModifiers & OS.shiftKey) != 0) {
				event.stateMask |= SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return true;
			}
			if ((modifiers & OS.controlKey) == 0 && (lastModifiers & OS.controlKey) != 0) {
				event.stateMask |= SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return true;
			}
			if ((modifiers & OS.cmdKey) == 0 && (lastModifiers & OS.cmdKey) != 0) {
				event.stateMask |= SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return true;
			}
			if ((modifiers & OS.optionKey) == 0 && (lastModifiers & OS.optionKey) != 0) {
				event.stateMask |= SWT.ALT;
				event.keyCode = SWT.ALT;
				return true;
			}
			break;
		}
	}
	return true; 
}

boolean setKeyState (Event event, int type, int theEvent) {
	boolean isNull = false;
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	event.keyCode = Display.translateKey (keyCode [0]);
	switch (event.keyCode) {
		case SWT.LF: {
			/*
			* Feature in the Macintosh.  When the numeric key pad
			* Enter key is pressed, it generates '\n'.  This is the
			* correct platform behavior but is not portable.  The
			* fix is to convert the '\n' into '\r'.
			*/
			event.keyCode = SWT.KEYPAD_CR;
			event.character = '\r';
			break;
		}
		case SWT.BS: event.character = '\b'; break;
		case SWT.CR: event.character = '\r'; break;
		case SWT.DEL: event.character = 0x7F; break;
		case SWT.ESC: event.character = 0x1B; break;
		case SWT.TAB: event.character = '\t'; break;
		default: {
			if (event.keyCode == 0 || (SWT.KEYPAD_MULTIPLY <= event.keyCode && event.keyCode <= SWT.KEYPAD_CR)) {
				int [] length = new int [1];
				int status = OS.GetEventParameter (theEvent, OS.kEventParamKeyUnicodes, OS.typeUnicodeText, null, 4, length, (char[])null);
				if (status == OS.noErr && length [0] != 0) {
					char [] chars = new char [1];
					OS.GetEventParameter (theEvent, OS.kEventParamKeyUnicodes, OS.typeUnicodeText, null, 2, null, chars);
					event.character = chars [0];
				}
				/*
				* Bug in the Mactonish.  For some reason, Ctrl+Shift+'2' and Ctrl+Shift+'6'
				* fail to give 0x0 (^@ or ASCII NUL) and 0x1e (^^).  Other control character
				* key sequences such as ^A or even Ctrl+Shift+'-' (^_ or 0x1f) are correctly
				* translated to control characters.  Since it is not possible to know which
				* key combination gives '@' on an international keyboard, there is no way to
				* test for either character and convert it to a control character (Shift+'2'
				* gives '@' only on an English keyboard) to work around the problem.
				*
				* There is no fix at this time.
				*/
			}
			if (event.keyCode == 0) {
				int kchrPtr = OS.GetScriptManagerVariable ((short) OS.smKCHRCache);
				if (display.kchrPtr != kchrPtr) {
					display.kchrPtr = kchrPtr;
					display.kchrState [0] = 0;
				}
				int result = OS.KeyTranslate (display.kchrPtr, (short)keyCode [0], display.kchrState);
				if (result <= 0x7f) {
					event.keyCode = result & 0x7f;
				} else {
					int [] encoding = new int [1];
					short keyScript = (short) OS.GetScriptManagerVariable ((short) OS.smKeyScript);
					short regionCode = (short) OS.GetScriptManagerVariable ((short) OS.smRegionCode);
					if (OS.UpgradeScriptInfoToTextEncoding (keyScript, (short) OS.kTextLanguageDontCare, regionCode, null, encoding) == OS.paramErr) {
						if (OS.UpgradeScriptInfoToTextEncoding (keyScript, (short) OS.kTextLanguageDontCare, (short) OS.kTextRegionDontCare, null, encoding) == OS.paramErr) {
							encoding [0] = OS.kTextEncodingMacRoman;
						}
					}
					int [] encodingInfo = new int [1];
					OS.CreateTextToUnicodeInfoByEncoding (encoding [0], encodingInfo);
					if (encodingInfo [0] != 0) {
						char [] chars = new char [1];
						int [] nchars = new int [1];
						byte [] buffer = new byte [2];
						buffer [0] = 1;
						buffer [1] = (byte) (result & 0xFF);
						OS.ConvertFromPStringToUnicode (encodingInfo [0], buffer, chars.length * 2, nchars, chars);
						OS.DisposeTextToUnicodeInfo (encodingInfo);
						event.keyCode = chars [0];
					}
				}
			}
			break;
		}
	}
	if (event.keyCode == 0 && event.character == 0) {
		if (!isNull) return false;
	}
	int [] chord = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseChord, OS.typeUInt32, null, 4, null, chord);
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	return setInputState (event, type, chord [0], modifiers [0]);
}

void setVisible (int control, boolean visible) {
	int visibleRgn = 0;
	boolean drawing = getDrawCount (control) == 0;
	if (drawing && !visible) visibleRgn = getVisibleRegion (control, false);
	OS.SetControlVisibility (control, visible, false);
	invalidateVisibleRegion (control);
	if (drawing && visible) visibleRgn = getVisibleRegion (control, false);
	if (drawing) {
		int window = OS.GetControlOwner (control);
		OS.InvalWindowRgn (window, visibleRgn);
		OS.DisposeRgn (visibleRgn);
	}
}

void setZOrder (int control, int otheControl, boolean above) {
	int inOp = above ?  OS.kHIViewZOrderBelow :  OS.kHIViewZOrderAbove;
	int oldRgn = 0;
	boolean drawing = isDrawing (control);
	if (drawing) oldRgn = getVisibleRegion (control, false);
	OS.HIViewSetZOrder (control, inOp, otheControl);
	invalidateVisibleRegion (control);
	if (drawing) {
		int newRgn = getVisibleRegion (control, false);
		if (above) {
			OS.DiffRgn (newRgn, oldRgn, newRgn);
		} else {
			OS.DiffRgn (oldRgn, newRgn, newRgn);
		}
		int window = OS.GetControlOwner (control);
		OS.InvalWindowRgn (window, newRgn);
		OS.DisposeRgn (oldRgn);
		OS.DisposeRgn (newRgn);
	}
}

int textInputProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventTextInputUnicodeForKeyEvent: return kEventTextInputUnicodeForKeyEvent (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

RGBColor toRGBColor (float [] color) {
	RGBColor rgb = new RGBColor ();
	rgb.red = (short) (color [0] * 0xffff);
	rgb.green = (short) (color [1] * 0xffff);
	rgb.blue = (short) (color [2] * 0xffff);
	return rgb;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}

int trackingProc (int browser, int itemID, int property, int theRect, int startPt, int modifiers) {
	/* Return one to indicate that the data browser should process the click */
	return 1;
}

int windowProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventWindowActivated:			return kEventWindowActivated (nextHandler, theEvent, userData);	
		case OS.kEventWindowBoundsChanged:		return kEventWindowBoundsChanged (nextHandler, theEvent, userData);
		case OS.kEventWindowClose:				return kEventWindowClose (nextHandler, theEvent, userData);
		case OS.kEventWindowCollapsed:			return kEventWindowCollapsed (nextHandler, theEvent, userData);
		case OS.kEventWindowDeactivated:		return kEventWindowDeactivated (nextHandler, theEvent, userData);
		case OS.kEventWindowExpanded:			return kEventWindowExpanded (nextHandler, theEvent, userData);
		case OS.kEventWindowHidden:				return kEventWindowHidden (nextHandler, theEvent, userData);
		case OS.kEventWindowShown:				return kEventWindowShown (nextHandler, theEvent, userData);
		case OS.kEventWindowGetRegion:		return kEventWindowGetRegion (nextHandler, theEvent, userData);
		case OS.kEventWindowHitTest:				return kEventWindowHitTest (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

}
