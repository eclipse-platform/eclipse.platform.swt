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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

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
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class Widget {
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;

	int /*long*/ jniRef;

	/* Global state flags */
	static final int DISPOSED         = 1 << 0;
	static final int CANVAS           = 1 << 1;
	static final int KEYED_DATA       = 1 << 2;
	static final int DISABLED         = 1 << 3;
	static final int HIDDEN           = 1 << 4;
	static final int HOT			  = 1 << 5;
	static final int MOVED            = 1 << 6;
	static final int RESIZED          = 1 << 7;
	static final int EXPANDING        = 1 << 8;
	static final int IGNORE_WHEEL     = 1 << 9;
	static final int PARENT_BACKGROUND = 1 << 10;
	static final int THEME_BACKGROUND = 1 << 11;
	
	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED	= 1<<12;
	
	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1<<13;
	
	/* A layout was requested in this widget hierachy */
	static final int LAYOUT_CHILD = 1<<14;

	/* More global state flags */
	static final int RELEASED = 1<<15;
	static final int DISPOSE_SENT = 1<<16;	
	static final int FOREIGN_HANDLE = 1<<17;
	static final int DRAG_DETECT = 1<<18;

	/* WebKit fixes */
	static final int WEBKIT_EVENTS_FIX = 1<<19;
	static final String WEBKIT_EVENTS_FIX_KEY = "org.eclipse.swt.internal.webKitEventsFix"; //$NON-NLS-1$
	static final String GLCONTEXT_KEY = "org.eclipse.swt.internal.cocoa.glcontext"; //$NON-NLS-1$
	
	static final String IS_ACTIVE = "org.eclipse.swt.internal.isActive"; //$NON-NLS-1$

	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1<<20;
	
	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

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
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
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
	reskinWidget ();
}

int /*long*/ accessibleHandle() {
	return 0;
}

int /*long*/ accessibilityActionDescription(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	return callSuperObject(id, sel, arg0);
}

int /*long*/ accessibilityActionNames(int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

int /*long*/ accessibilityAttributeNames(int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

int /*long*/ accessibilityAttributeValue(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	return callSuperObject(id, sel, arg0);
}

int /*long*/ accessibilityAttributeValue_forParameter(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg0, arg1);
}

int /*long*/ accessibilityFocusedUIElement(int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

int /*long*/ accessibilityHitTest(int /*long*/ id, int /*long*/ sel, NSPoint point) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, point);
}

boolean accessibilityIsAttributeSettable(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	return callSuperBoolean(id, sel, arg0);
}

boolean accessibilityIsIgnored(int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean(id, sel);
}

int /*long*/ accessibilityParameterizedAttributeNames(int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

void accessibilityPerformAction(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	callSuper(id, sel, arg0);
}

void accessibilitySetValue_forAttribute(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	callSuper(id, sel, arg0, arg1);
}

String getClipboardText () {
	NSPasteboard pasteboard = NSPasteboard.generalPasteboard ();
	if (pasteboard == null) return "";
	NSString string = pasteboard.stringForType (OS.NSStringPboardType);
	return string != null ? string.getString () : null;
}

void setClipRegion (NSView view) {
}

int /*long*/ attributedSubstringFromRange (int /*long*/ id, int /*long*/ sel, int /*long*/ range) {
	return 0;
}

void callSuper(int /*long*/ id, int /*long*/ sel) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel);
}

void callSuper(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, arg0);
}

void callSuper(int /*long*/ id, int /*long*/ sel, NSRect arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, arg0);
}

void callSuper(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, arg0, arg1);
}

void callSuper(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, arg0, arg1);
}

int /*long*/ callSuper(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSRect arg1, int /*long*/ arg2) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg0, arg1, arg2);
}

boolean callSuperBoolean(int /*long*/ id, int /*long*/ sel) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel) != 0;
}

boolean canBecomeKeyWindow (int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean (id, sel);
}

NSSize cellSize (int /*long*/ id, int /*long*/ sel) {
	NSSize result = new NSSize();
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper_stret(result, super_struct, sel);
	return result;
}

boolean callSuperBoolean(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg0) != 0;
}

boolean callSuperBoolean(int /*long*/ id, int /*long*/ sel, NSRange range, int /*long*/ arg1) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper_bool(super_struct, sel, range, arg1);
}

int /*long*/ callSuperObject(int /*long*/ id, int /*long*/ sel) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel);
}

int /*long*/ callSuperObject(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg0);
}

boolean canDragRowsWithIndexes_atPoint(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	// Trees/tables are not draggable unless explicitly told they are.
	return false;
}

int /*long*/ characterIndexForPoint (int /*long*/ id, int /*long*/ sel, int /*long*/ point) {
	return OS.NSNotFound;
}

boolean acceptsFirstMouse (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, theEvent) != 0;
}

boolean acceptsFirstResponder (int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean(id, sel);
}

boolean becomeFirstResponder (int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean(id, sel);
}

void becomeKeyWindow (int /*long*/ id, int /*long*/ sel) {
	callSuper(id, sel);
}

/**
 * Marks the widget to be skinned. 
 * <p>
 * The skin event is sent to the receiver's display when appropriate (usually before the next event
 * is handled). Widgets are automatically marked for skinning upon creation as well as when its skin
 * id or class changes. The skin id and/or class can be changed by calling <code>Display.setData(String, Object)</code> 
 * with the keys SWT.SKIN_ID and/or SWT.SKIN_CLASS. Once the skin event is sent to a widget, it 
 * will not be sent again unless <code>reskin(int)</code> is called on the widget or on an ancestor 
 * while specifying the <code>SWT.ALL</code> flag.  
 * </p>
 * <p>
 * The parameter <code>flags</code> may be either:
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be skinned</dd>
 * <dt><b>SWT.NONE</b></dt>
 * <dd>only the receiver should be skinned</dd>
 * </dl>
 * </p>
 * @param flags the flags specifying how to reskin
 * 
 * @exception SWTException 
 * <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.6
 */
public void reskin (int flags) {
	checkWidget ();
	reskinWidget ();
	if ((flags & SWT.ALL) != 0) reskinChildren (flags);
}

void reskinChildren (int flags) {	
}

void reskinWidget() {
	if ((state & SKIN_NEEDED) != SKIN_NEEDED) {
		this.state |= SKIN_NEEDED;
		display.addSkinnableWidget(this);
	}
}

boolean resignFirstResponder (int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean(id, sel);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an event of the given type occurs. When the
 * event does occur in the widget, the listener is notified by
 * sending it the <code>handleEvent()</code> message. The event
 * type is one of the event constants defined in class <code>SWT</code>.
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
 * @see SWT
 * @see #getListeners(int)
 * @see #removeListener(int, Listener)
 * @see #notifyListeners
 */
public void addListener (int eventType, Listener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	_addListener (eventType, listener);
}

void _addListener (int eventType, Listener listener) {
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the widget is disposed. When the widget is
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

boolean canBecomeKeyView(int /*long*/ id, int /*long*/ sel) {
	return true;
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

void checkOpen () {
	/* Do nothing */
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
	if (parent.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	parent.checkWidget ();
	parent.checkOpen ();
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

void clearDeferFlushing (int /*long*/ id, int /*long*/ sel) {
}

boolean textView_clickOnLink_atIndex(int /*long*/ id, int /*long*/ sel, int /*long*/ textView, int /*long*/ link, int /*long*/ charIndex) {
	return true;
}

void collapseItem_collapseChildren (int /*long*/ id, int /*long*/ sel, int /*long*/ item, boolean children) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, item, children);
}

void copyToClipboard (char [] buffer) {
	if (buffer.length == 0) return;
	NSPasteboard pasteboard = NSPasteboard.generalPasteboard ();
	if (pasteboard == null) return;
	pasteboard.declareTypes (NSArray.arrayWithObject (OS.NSStringPboardType), null);
	pasteboard.setString (NSString.stringWithCharacters (buffer, buffer.length), OS.NSStringPboardType);
}

void createHandle () {
}

void createJNIRef () {
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) error (SWT.ERROR_NO_HANDLES);
}

void createWidget () {
	createJNIRef ();
	createHandle ();
	setOrientation();
	register ();
}
	
void comboBoxSelectionDidChange(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void comboBoxWillDismiss(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void comboBoxWillPopUp(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void deregister () {
}

void destroyJNIRef () {
	if (jniRef != 0) OS.DeleteGlobalRef (jniRef);
	jniRef = 0;
}

void destroyWidget () {
	releaseHandle ();
}

/**
 * Disposes of the operating system resources associated with
 * the receiver and all its descendants. After this method has
 * been invoked, the receiver and all descendants will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
 * This method does nothing if the widget is already disposed.
 * <p>
 * NOTE: This method is not called recursively on the descendants
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
	if (isDisposed ()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	release (true);
}

void doCommandBySelector (int /*long*/ id, int /*long*/ sel, int /*long*/ aSelector) {
	callSuper (id, sel, aSelector);
}

boolean dragSelectionWithEvent(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	return false;
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	/* Do nothing */
}

void drawBackgroundInClipRect(int /*long*/ id, int /*long*/ sel, NSRect rect) {
	callSuper(id, sel, rect);
}

void drawImageWithFrameInView (int /*long*/ id, int /*long*/ sel, int /*long*/ image, NSRect rect, int /*long*/ view) {
}

void drawInteriorWithFrame_inView (int /*long*/ id, int /*long*/ sel, NSRect cellFrame, int /*long*/ view) {
	callSuper(id, sel, cellFrame, view);
}

void drawLabelInRect(int /*long*/ id, int /*long*/ sel, boolean shouldTruncateLabel, NSRect rect) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, shouldTruncateLabel, rect);
}

void drawViewBackgroundInRect(int /*long*/ id, int /*long*/ sel, NSRect rect) {
	callSuper(id, sel, rect);
}

void drawWithExpansionFrame_inView (int /*long*/ id, int /*long*/ sel, NSRect cellFrame, int /*long*/ view) {
	callSuper(id, sel, cellFrame, view);
}

void drawRect (int /*long*/ id, int /*long*/ sel, NSRect rect) {
	if (!isDrawing()) return;
	Display display = this.display;
	NSView view = new NSView(id);
	display.isPainting.addObject(view);
	NSGraphicsContext context = NSGraphicsContext.currentContext();
	context.saveGraphicsState();
	setClipRegion(view);
	drawBackground (id, context, rect);
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, rect);
	if (!isDisposed()) {
		/* 
		* Feature in Cocoa. There are widgets that draw outside of the UI thread,
		* such as the progress bar and default button.  The fix is to draw the
		* widget but not send paint events.
		*/
		drawWidget (id, context, rect);
	}
	context.restoreGraphicsState();
	display.isPainting.removeObjectIdenticalTo(view);
}

void _drawThemeProgressArea (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, arg0);
}

void drawWidget (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
}

int /*long*/ imageView () {
	return 0;
}

void redrawWidget (NSView view, boolean children) {
	view.setNeedsDisplay(true);
}

void redrawWidget (NSView view, int /*long*/ x, int /*long*/ y, int /*long*/ width, int /*long*/ height, boolean children) {
	NSRect rect = new NSRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	view.setNeedsDisplayInRect(rect);
}

void error (int code) {
	SWT.error(code);
}

void expandItem_expandChildren (int /*long*/ id, int /*long*/ sel, int /*long*/ item, boolean children) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, item, children);
}

NSRect expansionFrameWithFrame_inView(int /*long*/ id, int /*long*/ sel, NSRect cellRect, int /*long*/ view) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	NSRect result = new NSRect();
	OS.objc_msgSendSuper_stret(result, super_struct, sel, cellRect, view);
	return result;
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

NSRect firstRectForCharacterRange(int /*long*/ id, int /*long*/ sel, int /*long*/ range) {
	return new NSRect ();
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
 * @see #setData(Object)
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
 * @see #setData(String, Object)
 */
public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (key.equals(IS_ACTIVE)) return new Boolean(isActive());
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
 * </ul>
 */
public Display getDisplay () {
	Display display = this.display;
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

boolean getDrawing () {
	return true;
}

/**
 * Returns an array of listeners who will be notified when an event 
 * of the given type occurs. The event type is one of the event constants 
 * defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @return an array of listeners that will be notified when the event occurs
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #addListener(int, Listener)
 * @see #removeListener(int, Listener)
 * @see #notifyListeners
 * 
 * @since 3.4
 */
public Listener[] getListeners (int eventType) {
	checkWidget();
	if (eventTable == null) return new Listener[0];
	return eventTable.getListeners(eventType);
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
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

boolean hasMarkedText (int /*long*/ id, int /*long*/ sel) {
	return false;
}

void helpRequested(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
}

void highlightSelectionInClipRect(int /*long*/ id, int /*long*/ sel, int /*long*/ rect) {	
}

int /*long*/ hitTest (int /*long*/ id, int /*long*/ sel, NSPoint point) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, point);
}

int /*long*/ hitTestForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ event, NSRect rect, int /*long*/ controlView) {
	return 0;
}

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

int /*long*/ image (int /*long*/ id, int /*long*/ sel) {
	return 0;
}

NSRect imageRectForBounds (int /*long*/ id, int /*long*/ sel, NSRect cellFrame) {
	return new NSRect();
}

boolean insertText (int /*long*/ id, int /*long*/ sel, int /*long*/ string) {
	callSuper (id, sel, string);
	return true;
}

boolean isActive () {
	return true;
}

/**
 * Returns <code>true</code> if the widget has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the widget.
 * When a widget has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the widget.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return (state & DISPOSED) != 0;
}

boolean isDrawing () {
	return true;
}

boolean isFlipped(int /*long*/ id, int /*long*/ sel) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel) != 0;
}

/**
 * Returns <code>true</code> if there are any listeners
 * for the specified event type associated with the receiver,
 * and <code>false</code> otherwise. The event type is one of
 * the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event
 * @return true if the event is hooked
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public boolean isListening (int eventType) {
	checkWidget();
	return hooks (eventType);
}

boolean isOpaque(int /*long*/ id, int /*long*/ sel) {
	return false;
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

void flagsChanged (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper (id, sel, theEvent);
}

void keyDown (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	superKeyDown(id, sel, theEvent);
}

void keyUp (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	superKeyUp(id, sel, theEvent);
}

void mouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	mouseDownSuper(id, sel, theEvent);
}

void mouseDownSuper(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void mouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void mouseMoved(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void mouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void mouseEntered(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void mouseExited(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void cursorUpdate(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void rightMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void rightMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void rightMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void otherMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void otherMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

void otherMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

boolean shouldDelayWindowOrderingForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, theEvent) != 0;
}

boolean menuHasKeyEquivalent_forEvent_target_action(int /*long*/ id, int /*long*/ sel, int /*long*/ menu, int /*long*/ event, int /*long*/ target, int /*long*/ action) {
	return true;
}

int /*long*/ menuForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, theEvent);
}

void menuNeedsUpdate(int /*long*/ id, int /*long*/ sel, int /*long*/ menu) {
}

boolean makeFirstResponder(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	return callSuperBoolean(id, sel, notification);
}

NSRange markedRange (int /*long*/ id, int /*long*/ sel) {
	return new NSRange ();
}

void menu_willHighlightItem(int /*long*/ id, int /*long*/ sel, int /*long*/ menu, int /*long*/ item) {
}

void menuDidClose(int /*long*/ id, int /*long*/ sel, int /*long*/ menu) {
}

void menuWillOpen(int /*long*/ id, int /*long*/ sel, int /*long*/ menu) {
}

void noResponderFor(int /*long*/ id, int /*long*/ sel, int /*long*/ selector) {
	callSuper(id, sel, selector);
}

int /*long*/ numberOfRowsInTableView(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView) {
	return 0;
}

int /*long*/ outlineView_child_ofItem(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ index, int /*long*/ item) {
	return 0;
}

void outlineView_didClickTableColumn(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ tableColumn) {
}

int /*long*/ outlineView_objectValueForTableColumn_byItem(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ tableColumn, int /*long*/ item) {
	return 0;
}

boolean outlineView_isItemExpandable(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ item) {
	return false;
}

int /*long*/ outlineView_numberOfChildrenOfItem(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ item) {
	return 0;
}

void outlineView_willDisplayCell_forTableColumn_item(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ cell, int /*long*/ tableColumn, int /*long*/ item) {
}

void outlineViewColumnDidMove (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void outlineViewColumnDidResize (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void outlineViewSelectionDidChange(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void outlineViewSelectionIsChanging(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void outlineView_setObjectValue_forTableColumn_byItem(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ object, int /*long*/ tableColumn, int /*long*/ item) {
}

boolean outlineView_writeItems_toPasteboard(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	return false;
}


/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.  The
 * event type is one of the event constants defined in class
 * <code>SWT</code>.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT
 * @see #addListener
 * @see #getListeners(int)
 * @see #removeListener(int, Listener)
 */
public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) event = new Event ();
	sendEvent (eventType, event);
}

void pageDown (int /*long*/ id, int /*long*/ sel, int /*long*/ sender) {
	callSuper(id, sel, sender);
}

void pageUp (int /*long*/ id, int /*long*/ sel, int /*long*/ sender) {
	callSuper(id, sel, sender);
}

void postEvent (int eventType) {
	sendEvent (eventType, null, false);
}

void postEvent (int eventType, Event event) {
	sendEvent (eventType, event, false);
}

void reflectScrolledClipView (int /*long*/ id, int /*long*/ sel, int /*long*/ aClipView) {
	callSuper (id, sel, aClipView);
}

void register () {
}

void release (boolean destroy) {
	if ((state & DISPOSE_SENT) == 0) {
		state |= DISPOSE_SENT;
		sendEvent (SWT.Dispose);
	}
	if ((state & DISPOSED) == 0) {
		releaseChildren (destroy);
	}
	if ((state & RELEASED) == 0) {
		state |= RELEASED;
		if (destroy) {
			releaseParent ();
			releaseWidget ();
			destroyWidget ();
		} else {
			releaseWidget ();
			releaseHandle ();
		}
	}
}

void releaseChildren (boolean destroy) {
}

void releaseHandle () {
	state |= DISPOSED;
	display = null;
	destroyJNIRef ();
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	deregister ();
	if (display.tooltipTarget == this) display.tooltipTarget = null;
	eventTable = null;
	data = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when an event of the given type occurs. The event
 * type is one of the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
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
 * @see Listener
 * @see SWT
 * @see #addListener
 * @see #getListeners(int)
 * @see #notifyListeners
 */
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when an event of the given type occurs.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @param eventType the type of event to listen for
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
 * @see Listener
 * @see #addListener
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the widget is disposed.
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
 * @see DisposeListener
 * @see #addDisposeListener
 */
public void removeDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void scrollClipViewToPoint (int /*long*/ id, int /*long*/ sel, int /*long*/ clipView, NSPoint point) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, clipView, point);
}

void scrollWheel (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper(id, sel, theEvent);
}

NSRange selectedRange (int /*long*/ id, int /*long*/ sel) {
	return new NSRange ();
}

int /*long*/ nextValidKeyView (int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

int /*long*/ previousValidKeyView (int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

void sendDoubleSelection() {
}

void sendEvent (Event event) {
	display.sendEvent (eventTable, event);
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

boolean sendKeyEvent (NSEvent nsEvent, int type) {
	if ((state & WEBKIT_EVENTS_FIX) != 0) return true;
	Event event = new Event ();
	if (!setKeyState (event, type, nsEvent)) return true;
	return sendKeyEvent (type, event);
}

boolean sendKeyEvent (int type, Event event) {
	sendEvent (type, event);
	// widget could be disposed at this point
	
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the key
	* events.  If this happens, end the processing of
	* the key by returning false.
	*/
	if (isDisposed ()) return false;
	return event.doit;
}

void sendHorizontalSelection () {
}

void sendCancelSelection () {
}

void sendSearchSelection () {
}

void sendSelection () {
}

void sendSelectionEvent (int eventType) {
	sendSelectionEvent (eventType, null, false);
}

void sendSelectionEvent (int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	NSEvent nsEvent = NSApplication.sharedApplication ().currentEvent ();
	if (nsEvent != null) setInputState (event, nsEvent, 0);
	sendEvent(eventType, event, send);
}

void sendVerticalSelection () {
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
 * 
 * @see #getData()
 */
public void setData (Object data) {
	checkWidget();
	if (WEBKIT_EVENTS_FIX_KEY.equals (data)) {
		state |= WEBKIT_EVENTS_FIX;
		return;
	}
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
 * @see #getData(String)
 */
public void setData (String key, Object value) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (GLCONTEXT_KEY.equals (key)) {
		setOpenGLContext(value);
		return;
	}
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
	if (key.equals(SWT.SKIN_CLASS) || key.equals(SWT.SKIN_ID)) this.reskin(SWT.ALL);
}

void setOpenGLContext(Object value) {
}

void setOrientation () {
}

void setFrameOrigin (int /*long*/ id, int /*long*/ sel, NSPoint point) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, point);
}

void setFrameSize (int /*long*/ id, int /*long*/ sel, NSSize size) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, size);
}

void setImage (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
}

boolean setInputState (Event event, NSEvent nsEvent, int type) {
	if (nsEvent == null) return true;
	int /*long*/ modifierFlags = nsEvent.modifierFlags();
	if ((modifierFlags & OS.NSAlternateKeyMask) != 0) event.stateMask |= SWT.ALT;
	if ((modifierFlags & OS.NSShiftKeyMask) != 0) event.stateMask |= SWT.SHIFT;
	if ((modifierFlags & OS.NSControlKeyMask) != 0) event.stateMask |= SWT.CONTROL;
	if ((modifierFlags & OS.NSCommandKeyMask) != 0) event.stateMask |= SWT.COMMAND;
	//TODO multiple mouse buttons pressed
	switch ((int)/*64*/nsEvent.type()) {
		case OS.NSLeftMouseDragged:
		case OS.NSRightMouseDragged:
		case OS.NSOtherMouseDragged:
			switch ((int)/*64*/nsEvent.buttonNumber()) {
				case 0: event.stateMask |= SWT.BUTTON1; break;
				case 1: event.stateMask |= SWT.BUTTON3; break;
				case 2: event.stateMask |= SWT.BUTTON2; break;
				case 3: event.stateMask |= SWT.BUTTON4; break;
				case 4: event.stateMask |= SWT.BUTTON5; break;
			}
			break;
		case OS.NSScrollWheel:
		case OS.NSKeyDown:
		case OS.NSKeyUp:
			int state = OS.GetCurrentButtonState ();
			if ((state & 0x1) != 0) event.stateMask |= SWT.BUTTON1;
			if ((state & 0x2) != 0) event.stateMask |= SWT.BUTTON3;
			if ((state & 0x4) != 0) event.stateMask |= SWT.BUTTON2;
			if ((state & 0x8) != 0) event.stateMask |= SWT.BUTTON4;
			if ((state & 0x10) != 0) event.stateMask |= SWT.BUTTON5;
			break;
	}
	switch (type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3) event.stateMask &= ~SWT.BUTTON3;
			if (event.button == 4) event.stateMask &= ~SWT.BUTTON4;
			if (event.button == 5) event.stateMask &= ~SWT.BUTTON5;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) event.stateMask |= SWT.BUTTON3;
			if (event.button == 4) event.stateMask |= SWT.BUTTON4;
			if (event.button == 5) event.stateMask |= SWT.BUTTON5;
			break;
		case SWT.KeyDown:
		case SWT.Traverse:
			if (event.keyCode == SWT.ALT) event.stateMask &= ~SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask &= ~SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask &= ~SWT.CONTROL;
			if (event.keyCode == SWT.COMMAND) event.stateMask &= ~SWT.COMMAND;
			break;
		case SWT.KeyUp:
			if (event.keyCode == SWT.ALT) event.stateMask |= SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask |= SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask |= SWT.CONTROL;
			if (event.keyCode == SWT.COMMAND) event.stateMask |= SWT.COMMAND;
			break;
	}		
	return true;
}

boolean setKeyState (Event event, int type, NSEvent nsEvent) {
	boolean isNull = false;
	int keyCode = nsEvent.keyCode ();
	event.keyCode = Display.translateKey (keyCode);
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
		default:
			if (event.keyCode == 0 || (SWT.KEYPAD_MULTIPLY <= event.keyCode && event.keyCode <= SWT.KEYPAD_CR)) {
				NSString chars = nsEvent.characters ();
				if (chars.length() > 0) event.character = (char)chars.characterAtIndex (0);
			}
			if (event.keyCode == 0) {
				int /*long*/ uchrPtr = 0;
				int /*long*/ currentKbd = OS.TISCopyCurrentKeyboardInputSource();
				int /*long*/ uchrCFData = OS.TISGetInputSourceProperty(currentKbd, OS.kTISPropertyUnicodeKeyLayoutData());
				
				if (uchrCFData != 0) {
					// If the keyboard changed since the last keystroke clear the dead key state.
					if (uchrCFData != display.currentKeyboardUCHRdata) display.deadKeyState[0] = 0;
					uchrPtr = OS.CFDataGetBytePtr(uchrCFData);
					
					if (uchrPtr != 0 && OS.CFDataGetLength(uchrCFData) > 0) {
						int /*long*/ cgEvent = nsEvent.CGEvent();
						long keyboardType = OS.CGEventGetIntegerValueField(cgEvent, OS.kCGKeyboardEventKeyboardType);
						
						int maxStringLength = 256;
						char [] output = new char [maxStringLength];
						int [] actualStringLength = new int [1];
						OS.UCKeyTranslate (uchrPtr, (short)keyCode, (short)OS.kUCKeyActionDown, 0, (int)keyboardType, 0, display.deadKeyState, maxStringLength, actualStringLength, output);
						if (actualStringLength[0] < 1) {
							// part of a multi-key key
							event.keyCode = 0;
						} else {
							event.keyCode = output[0];
						}
					}
				} else {
					// KCHR keyboard layouts are no longer supported, so fall back to the basic but flawed
					// method of determining which key was pressed.
					NSString unmodifiedChars = nsEvent.charactersIgnoringModifiers ().lowercaseString();
					if (unmodifiedChars.length() > 0) event.keyCode = (char)unmodifiedChars.characterAtIndex(0);
				}
				
				if (currentKbd != 0) OS.CFRelease(currentKbd);
			}
	}
	if (event.keyCode == 0 && event.character == 0) {
		if (!isNull) return false;
	}
	setLocationMask (event, nsEvent);
	setInputState (event, nsEvent, type);
	return true;
}

void setLocationMask (Event event, NSEvent nsEvent) {
	switch (nsEvent.keyCode ()) {
		case 55: /* LEFT COMMAND */
		case 56: /* LEFT SHIFT */
		case 58: /* LEFT ALT */
		case 59: /* LEFT CONTROL */
			event.keyLocation = SWT.LEFT;
			break;
		case 54: /* RIGHT COMMAND */
		case 60: /* RIGHT SHIFT */
		case 61: /* RIGHT ALT */
		case 62: /* RIGHT CONTROL */
			event.keyLocation = SWT.RIGHT;
			break;
		case 67:  /* KEYPAD_MULTIPLY */
		case 69:  /* KEYPAD_ADD */
		case 76:  /* KEYPAD_CR */
		case 78:  /* KEYPAD_SUBTRACT */
		case 65:  /* KEYPAD_DECIMAL */
		case 75:  /* KEYPAD_DIVIDE */
		case 82:  /* KEYPAD_0 */
		case 83:  /* KEYPAD_1 */
		case 84:  /* KEYPAD_2 */
		case 85:  /* KEYPAD_3 */
		case 86:  /* KEYPAD_4 */
		case 87:  /* KEYPAD_5 */
		case 88:  /* KEYPAD_6 */
		case 89:  /* KEYPAD_7 */
		case 91:  /* KEYPAD_8 */
		case 92:  /* KEYPAD_9 */
		case 81:  /* KEYPAD_EQUAL */
			event.keyLocation = SWT.KEYPAD;
			break;
	}
}

boolean setMarkedText_selectedRange (int /*long*/ id, int /*long*/ sel, int /*long*/ string, int /*long*/ range) {
	return true;
}

void setNeedsDisplay (int /*long*/ id, int /*long*/ sel, boolean flag) {
	if (flag && !isDrawing()) return;
	NSView view = new NSView(id);
	if (flag && display.isPainting.containsObject(view)) {
		NSMutableArray needsDisplay = display.needsDisplay;
		if (needsDisplay == null) {
			needsDisplay = (NSMutableArray)new NSMutableArray().alloc();
			display.needsDisplay = needsDisplay = needsDisplay.initWithCapacity(12);
		}
		needsDisplay.addObject(view);
		return;
	}
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, flag);
}

void setNeedsDisplayInRect (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	if (!isDrawing()) return;
	NSRect rect = new NSRect();
	OS.memmove(rect, arg0, NSRect.sizeof);
	NSView view = new NSView(id);
	if (display.isPainting.containsObject(view)) {
		NSMutableArray needsDisplayInRect = display.needsDisplayInRect;
		if (needsDisplayInRect == null) {
			needsDisplayInRect = (NSMutableArray)new NSMutableArray().alloc();
			display.needsDisplayInRect = needsDisplayInRect = needsDisplayInRect.initWithCapacity(12);
		}
		needsDisplayInRect.addObject(view);
		needsDisplayInRect.addObject(NSValue.valueWithRect(rect));
		return;
	}
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel, rect);
}

void setObjectValue(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	callSuper(id, sel, arg0);
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}

boolean setTabItemFocus () {
	return false;
}

boolean shouldChangeTextInRange_replacementString(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	return true;
}

NSSize sizeOfLabel(int /*long*/ id, int /*long*/ sel, boolean shouldTruncateLabel) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	NSSize result = new NSSize();
	OS.objc_msgSendSuper_stret(result, super_struct, sel, shouldTruncateLabel);
	return result;
}

void superKeyDown (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper (id, sel, theEvent);
}

void superKeyUp (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	callSuper (id, sel, theEvent);
}

void tableViewColumnDidMove (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void tableViewColumnDidResize (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void tableViewSelectionDidChange (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void tableViewSelectionIsChanging (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void tableView_didClickTableColumn(int /*long*/ id, int /*long*/ sel, int /*long*/ tableView, int /*long*/ tableColumn) {
}

int /*long*/ tableView_objectValueForTableColumn_row(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ aTableColumn, int /*long*/ rowIndex) {
	return 0;
}

void tableView_setObjectValue_forTableColumn_row(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ anObject, int /*long*/ aTableColumn, int /*long*/ rowIndex) {	
}

boolean tableView_shouldEditTableColumn_row(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ aTableColumn, int /*long*/ rowIndex) {
	return true;
}

void tableView_willDisplayCell_forTableColumn_row(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ aCell, int /*long*/ aTableColumn, int /*long*/ rowIndex) {
}

void textViewDidChangeSelection(int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
}

void textDidChange(int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	callSuper (id, sel, aNotification);
}

void textDidEndEditing(int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	callSuper(id, sel, aNotification);
}

NSRange textView_willChangeSelectionFromCharacterRange_toCharacterRange(int /*long*/ id, int /*long*/ sel, int /*long*/ aTextView, int /*long*/ oldSelectedCharRange, int /*long*/ newSelectedCharRange) {
	return new NSRange();
}

NSRect titleRectForBounds (int /*long*/ id, int /*long*/ sel, NSRect cellFrame) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	NSRect result = new NSRect();
	OS.objc_msgSendSuper_stret(result, super_struct, sel, cellFrame);
	return result;
}

String tooltipText () {
	return null;
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

void resetCursorRects (int /*long*/ id, int /*long*/ sel) {
	callSuper (id, sel);
}

void updateTrackingAreas (int /*long*/ id, int /*long*/ sel) {
	callSuper (id, sel);
}

int /*long*/ validAttributesForMarkedText (int /*long*/ id, int /*long*/ sel) {
	return 0;
}

void tabView_didSelectTabViewItem(int /*long*/ id, int /*long*/ sel, int /*long*/ tabView, int /*long*/ tabViewItem) {
}

void tabView_willSelectTabViewItem(int /*long*/ id, int /*long*/ sel, int /*long*/ tabView, int /*long*/ tabViewItem) {
}

boolean tableView_writeRowsWithIndexes_toPasteboard(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	return false;
}

int /*long*/ view_stringForToolTip_point_userData (int /*long*/ id, int /*long*/ sel, int /*long*/ view, int /*long*/ tag, int /*long*/ point, int /*long*/ userData) {
	return 0;
}

void viewDidMoveToWindow(int /*long*/ id, int /*long*/ sel) {	
}

void windowDidMove(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowDidResize(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowDidResignKey(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowDidBecomeKey(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowDidMiniturize(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowDidDeminiturize(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

void windowSendEvent(int /*long*/ id, int /*long*/ sel, int /*long*/ event) {
	callSuper(id, sel, event);
}

boolean windowShouldClose(int /*long*/ id, int /*long*/ sel, int /*long*/ window) {
	return false;
}

void windowWillClose(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

int /*long*/ nextState(int /*long*/ id, int /*long*/ sel) {
	return callSuperObject(id, sel);
}

void updateOpenGLContext(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
}

boolean shouldDrawInsertionPoint(int /*long*/ id, int /*long*/ sel) {
	return callSuperBoolean(id, sel);
}

}
