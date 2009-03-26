/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

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
	public NSView view;
	Composite parent;
	String toolTipText;
	Object layoutData;
	int drawCount;
	Menu menu;
	Color foreground, background;
	Image backgroundImage;
	Font font;
	Cursor cursor;
	Region region;
	NSBezierPath regionPath;
	int /*long*/ visibleRgn;
	Accessible accessible;
	
	final static int CLIPPING = 1 << 10;
	final static int VISIBLE_REGION = 1 << 12;
	
	/**
	 * Magic number comes from experience. There's no API for this value in Cocoa or Carbon.
	 */
	static final int DEFAULT_DRAG_HYSTERESIS = 5;
	
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
	createWidget ();
}

boolean acceptsFirstMouse (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	Shell shell = getShell ();
	if ((shell.style & SWT.ON_TOP) != 0) return true;
	return super.acceptsFirstMouse (id, sel, theEvent);
}

int /*long*/ accessibilityActionNames(int /*long*/ id, int /*long*/ sel) {
	if (accessible != null) {
		NSArray returnValue = accessible.internal_accessibilityActionNames(ACC.CHILDID_SELF);
		if (returnValue != null) return returnValue.id;
	}
	
	return super.accessibilityActionNames(id, sel);
}

int /*long*/ accessibilityAttributeNames(int /*long*/ id, int /*long*/ sel) {
	
	if (id == view.id || (view instanceof NSControl && ((NSControl)view).cell() != null && ((NSControl)view).cell().id == id)) {
		if (accessible != null) {
			
			// First, see if the accessible is going to define a set of attributes for the control.
			// If it does, return that.
			NSArray returnValue = accessible.internal_accessibilityAttributeNames(ACC.CHILDID_SELF);
			if (returnValue != null) return returnValue.id;
			
			// If not, see if it will override or augment the standard list.
			// Help, title, and description can be overridden.
			NSMutableArray extraAttributes = NSMutableArray.arrayWithCapacity(3);
			extraAttributes.addObject(OS.NSAccessibilityHelpAttribute);
			extraAttributes.addObject(OS.NSAccessibilityDescriptionAttribute);
			extraAttributes.addObject(OS.NSAccessibilityTitleAttribute);

			for (int i = (int)/*64*/extraAttributes.count() - 1; i >= 0; i--) {
				NSString attribute = new NSString(extraAttributes.objectAtIndex(i).id);
				if (accessible.internal_accessibilityAttributeValue(attribute, ACC.CHILDID_SELF) == null) {
					extraAttributes.removeObjectAtIndex(i);
				}
			}

			if (extraAttributes.count() > 0) {
				int /*long*/ superResult = super.accessibilityAttributeNames(id, sel);
				NSArray baseAttributes = new NSArray(superResult);
				NSMutableArray mutableAttributes = NSMutableArray.arrayWithCapacity(baseAttributes.count() + 1);
				mutableAttributes.addObjectsFromArray(baseAttributes);
				
				for (int i = 0; i < extraAttributes.count(); i++) {
					id currAttribute = extraAttributes.objectAtIndex(i);
					if (!mutableAttributes.containsObject(currAttribute)) {
						mutableAttributes.addObject(currAttribute);
					}
				}
				
				return mutableAttributes.id;
			}
		}
	}

	return super.accessibilityAttributeNames(id, sel);
}

int /*long*/ accessibilityParameterizedAttributeNames(int /*long*/ id, int /*long*/ sel) {

	if (id == view.id || (view instanceof NSControl && ((NSControl)view).cell() != null && ((NSControl)view).cell().id == id)) {
		if (accessible != null) {
			NSArray returnValue = accessible.internal_accessibilityParameterizedAttributeNames(ACC.CHILDID_SELF);
			if (returnValue != null) return returnValue.id;
		}
	}

	return super.accessibilityParameterizedAttributeNames(id, sel);
}


boolean accessibilityIsAttributeSettable(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {

	// TODO: Carbon didn't support setting attributes, so for now Cocoa doesn't as well.
	//	NSString attribute = new NSString(arg0);
	//		
	//	if (accessible != null) {
	//		return accessible.accessibilityIsAttributeSettable(attribute, ACC.CHILDID_SELF);
	//	}	
	//
	//	return super.accessibilityIsAttributeSettable(id, sel, arg0);
	return false;
}

int /*long*/ accessibilityFocusedUIElement(int /*long*/ id, int /*long*/ sel) {
	id returnValue = null;

	if (id == view.id || (view instanceof NSControl && ((NSControl)view).cell() != null && ((NSControl)view).cell().id == id)) {
		if (accessible != null) {
			returnValue = accessible.internal_accessibilityFocusedUIElement(ACC.CHILDID_SELF);
		}
	}

	// If we had an accessible and it didn't handle the attribute request, let the
	// superclass handle it.
	if (returnValue == null)
		return super.accessibilityFocusedUIElement(id, sel);
	else
		return returnValue.id;
}

int /*long*/ accessibilityHitTest(int /*long*/ id, int /*long*/ sel, NSPoint point) {
	id returnValue = null;

	if (id == view.id || (view instanceof NSControl && ((NSControl)view).cell() != null && ((NSControl)view).cell().id == id)) {
		if (accessible != null) {
			returnValue = accessible.internal_accessibilityHitTest(point, ACC.CHILDID_SELF);
		}
	}

	// If we had an accessible and it didn't handle the attribute request, let the
	// superclass handle it.
	if (returnValue == null)
		return super.accessibilityHitTest(id, sel, point);
	else
		return returnValue.id;
}

int /*long*/ accessibilityAttributeValue(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	NSString attribute = new NSString(arg0);
	int /*long*/ returnValue = 0;
	id returnObject = null;
	
	if (accessible != null) {
		returnObject = accessible.internal_accessibilityAttributeValue(attribute, ACC.CHILDID_SELF);
	}

	// If we had an accessible and it didn't handle the attribute request, let the
	// superclass handle it.
	if (returnObject == null) {
		returnValue = super.accessibilityAttributeValue(id, sel, arg0);
	} else {
		returnValue = returnObject.id;
	}
	
	return returnValue;
}

int /*long*/ accessibilityAttributeValue_forParameter(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	NSString attribute = new NSString(arg0);
	
	id returnValue = null;
	
	if (accessible != null) {
		id parameter = new id(arg1);
		returnValue = accessible.internal_accessibilityAttributeValue_forParameter(attribute, parameter, ACC.CHILDID_SELF);
	}
	
	// If we had an accessible and it didn't handle the attribute request, let the
	// superclass handle it.
	if (returnValue == null)
		return super.accessibilityAttributeValue_forParameter(id, sel, arg0, arg1);
	else
		return returnValue.id;
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

static final double SYNTHETIC_BOLD = -2.5;
static final double SYNTHETIC_ITALIC = 0.2;

void addTraits(NSMutableDictionary dict, Font font) {
	if ((font.extraTraits & OS.NSBoldFontMask) != 0) {
		dict.setObject(NSNumber.numberWithDouble(SYNTHETIC_BOLD), OS.NSStrokeWidthAttributeName);
	}
	if ((font.extraTraits & OS.NSItalicFontMask) != 0) {
		dict.setObject(NSNumber.numberWithDouble(SYNTHETIC_ITALIC), OS.NSObliquenessAttributeName);
	}
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

boolean becomeFirstResponder (int /*long*/ id, int /*long*/ sel) {
	if ((state & DISABLED) != 0) return false;
	return super.becomeFirstResponder (id, sel);
}

void calculateVisibleRegion (NSView view, int /*long*/ visibleRgn, boolean clipChildren) {
	int /*long*/ tempRgn = OS.NewRgn ();
	if (!view.isHiddenOrHasHiddenAncestor() && isDrawing()) {
		int /*long*/ childRgn = OS.NewRgn ();
		NSWindow window = view.window ();
		NSView contentView = window.contentView();
		NSView frameView = contentView.superview();
		NSRect bounds = contentView.visibleRect();
		bounds = contentView.convertRect_toView_(bounds, view);
		short[] rect = new short[4];
		OS.SetRect(rect, (short)bounds.x, (short)bounds.y, (short)(bounds.x + bounds.width), (short)(bounds.y + bounds.height));
		OS.RectRgn(visibleRgn, rect);
		NSView tempView = view, lastControl = null;
		while (tempView.id != frameView.id) {
			bounds = tempView.visibleRect();
			bounds = tempView.convertRect_toView_(bounds, view);
			OS.SetRect(rect, (short)bounds.x, (short)bounds.y, (short)(bounds.x + bounds.width), (short)(bounds.y + bounds.height));
			OS.RectRgn(tempRgn, rect);
			OS.SectRgn (tempRgn, visibleRgn, visibleRgn);
			if (OS.EmptyRgn (visibleRgn)) break;
			if (clipChildren || tempView.id != view.id) {
				NSArray subviews = tempView.subviews();
				int /*long*/ count = subviews.count();
				for (int i = 0; i < count; i++) {
					NSView child = new NSView (subviews.objectAtIndex(count - i - 1));
					if (lastControl != null && child.id == lastControl.id) break;
					if (child.isHidden()) continue;
					bounds = child.visibleRect();
					bounds = child.convertRect_toView_(bounds, view);
					OS.SetRect(rect, (short)bounds.x, (short)bounds.y, (short)(bounds.x + bounds.width), (short)(bounds.y + bounds.height));
					OS.RectRgn(tempRgn, rect);
					OS.UnionRgn (tempRgn, childRgn, childRgn);
				}
			}
			lastControl = tempView;
			tempView = tempView.superview();
		}
		OS.DiffRgn (visibleRgn, childRgn, visibleRgn);
		OS.DisposeRgn (childRgn);
	} else {
		OS.CopyRgn (tempRgn, visibleRgn);
	}
	OS.DisposeRgn (tempRgn);
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

void checkToolTip (Widget target) {
	if (isVisible () && display.tooltipControl == this && (target == null || display.tooltipTarget == target)) {
		Shell shell = getShell ();
		shell.sendToolTipEvent (false);
		shell.sendToolTipEvent (true);
	}
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
	checkWidget ();
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

NSView contentView () {
	return view;
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
	return display.getWidgetColor (SWT.COLOR_WIDGET_BACKGROUND);
}

Font defaultFont () {
	if (display.smallFonts) return display.getSystemFont ();
	return Font.cocoa_new (display, defaultNSFont ());
}

Color defaultForeground () {
	return display.getWidgetColor (SWT.COLOR_WIDGET_FOREGROUND);
}

NSFont defaultNSFont () {
	return display.getSystemFont().handle;
}

void deregister () {
	super.deregister ();
	display.removeWidget (view);
}

void destroyWidget () {
	NSView view = topView ();
	view.removeFromSuperview ();
	releaseHandle ();
}

void doCommandBySelector (int /*long*/ id, int /*long*/ sel, int /*long*/ selector) {
	if (view.window ().firstResponder ().id == id) {
		if (imeInComposition ()) return;
		Shell s = this.getShell();
		NSEvent nsEvent = NSApplication.sharedApplication ().currentEvent ();
		if (nsEvent != null && nsEvent.type () == OS.NSKeyDown) {
			/*
			 * Feature in Cocoa.  Pressing Alt+UpArrow invokes doCommandBySelector 
			 * twice, with selectors moveBackward and moveToBeginningOfParagraph
			 * (Alt+DownArrow behaves similarly).  In order to avoid sending
			 * multiple events for these keys, do not send a KeyDown if we already sent one
			 * during this keystroke. This rule does not apply if the command key
			 * is down, because we likely triggered the current key sequence via flagsChanged.
			 */
			int /*long*/ modifiers = nsEvent.modifierFlags();
			if (s.keyInputHappened == false || (modifiers & OS.NSCommandKeyMask) != 0) {
				s.keyInputHappened = true;
				boolean [] consume = new boolean [1];
				if (translateTraversal (nsEvent.keyCode (), nsEvent, consume)) return;
				if (isDisposed ()) return;
				if (!sendKeyEvent (nsEvent, SWT.KeyDown)) return;
				if (consume [0]) return;
			}
		}
		if ((state & CANVAS) != 0) return;
	}
	super.doCommandBySelector (id, sel, selector);
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
	/**
	 * Feature in Cocoa. Mouse drag events do not account for hysteresis.
	 * As soon as the mouse drags a mouse dragged event is fired.  Fix is to
	 * check for another mouse drag event that is at least 5 pixels away 
	 * from the start of the drag. 
	 */
	NSApplication application = NSApplication.sharedApplication();
	boolean dragging = false;
	int /*long*/ eventType = OS.NSLeftMouseDown;
	float /*double*/ dragX = x;
	float /*double*/ dragY = y;
	
	/**
	 * To check for an actual drag we need to pull off mouse moved and mouse up events
	 * to detect if the user dragged outside of a 10 x 10 box centered on the mouse down location.
	 * We still want the view to see the events, so save them and re-post when done checking.
	 */
	NSEvent mouseUpEvent = null;
	NSMutableArray dragEvents = NSMutableArray.arrayWithCapacity(10);

	while (eventType != OS.NSLeftMouseUp) {
		NSEvent event = application.nextEventMatchingMask((OS.NSLeftMouseUpMask | OS.NSLeftMouseDraggedMask),
				NSDate.distantFuture(), OS.NSEventTrackingRunLoopMode, true);
		eventType = event.type();
		
		if (eventType == OS.NSLeftMouseDragged) {
			dragEvents.addObject(event);
			NSPoint windowLoc = event.locationInWindow();
			NSPoint viewLoc = view.convertPoint_fromView_(windowLoc, null);
			if ((Math.abs(viewLoc.x - dragX) > DEFAULT_DRAG_HYSTERESIS) || (Math.abs(viewLoc.y - dragY) > DEFAULT_DRAG_HYSTERESIS)) {
				dragging = true;
				break;
			}
		} else if (eventType == OS.NSLeftMouseUp) {
			mouseUpEvent = event;
		}
	}

	// Push back any events we took out of the queue so the control can receive them. 
	if (mouseUpEvent != null) application.postEvent(mouseUpEvent, true);

	if (dragEvents.count() > 0) {
		while (dragEvents.count() > 0) {
			NSEvent currEvent = new NSEvent(dragEvents.objectAtIndex(dragEvents.count() - 1).id);
			dragEvents.removeLastObject();
			application.postEvent(currEvent, true);
		}
	}

	return dragging;
}

boolean drawGripper (int x, int y, int width, int height, boolean vertical) {
	return false;
}

void drawWidget (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	if (id != paintView().id) return;
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return;

	/* Send paint event */
	GCData data = new GCData ();
	data.paintRect = rect;
	GC gc = GC.cocoa_new (this, data);
	Event event = new Event ();
	event.gc = gc;
	event.x = (int)rect.x;
	event.y = (int)rect.y;
	event.width = (int)rect.width;
	event.height = (int)rect.height;
	sendEvent (SWT.Paint, event);
	event.gc = null;
	gc.dispose ();
}

void enableWidget (boolean enabled) {
	if (view instanceof NSControl) {
		((NSControl)view).setEnabled(enabled);
	}
	updateCursorRects (isEnabled ());
}

NSView eventView () {
	return view;
}

void fillBackground (NSView view, NSGraphicsContext context, NSRect rect, int imgHeight) {
	Control control = findBackgroundControl();
	if (control == null) control = this;
	Image image = control.backgroundImage;
	if (image != null && !image.isDisposed()) {
		context.saveGraphicsState();
		NSColor.colorWithPatternImage(image.handle).setFill();
		NSPoint phase = new NSPoint();
		NSView controlView = control.view;
		if (imgHeight == -1) {
			NSView contentView = controlView.window().contentView();
			phase = controlView.convertPoint_toView_(phase, contentView);
			phase.y = contentView.bounds().height - phase.y;
		} else {
			phase = view.convertPoint_toView_(phase, controlView);
			phase.y += imgHeight - backgroundImage.getBounds().height;
		}
		context.setPatternPhase(phase);
		NSBezierPath.fillRect(rect);
		context.restoreGraphicsState();
		return;
	}

	Color background = control.background;
	float /*double*/ alpha;
	if (background == null || background.isDisposed ()) {
		background = control.defaultBackground ();
		alpha = getThemeAlpha ();
	} else {
		alpha = background.handle[3];
	}
	float /*double*/ [] color = background.handle;
	context.saveGraphicsState ();
	NSColor.colorWithDeviceRed (color [0], color [1], color [2], alpha).setFill ();
	NSBezierPath.fillRect (rect);
	context.restoreGraphicsState ();
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

Widget findTooltip (NSPoint pt) {
	return this;
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
//	int window = OS.GetControlOwner (handle);
//	OS.ClearKeyboardFocus (window);
}

void flagsChanged (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (view.window ().firstResponder ().id == id) {
		if ((state & SAFARI_EVENTS_FIX) == 0) {
			Shell s = this.getShell();
			s.keyInputHappened = false;
			int mask = 0;
			NSEvent nsEvent = new NSEvent (theEvent);
			int /*long*/ modifiers = nsEvent.modifierFlags ();
			int keyCode = Display.translateKey (nsEvent.keyCode ());
			switch (keyCode) {
				case SWT.ALT: mask = OS.NSAlternateKeyMask; break;
				case SWT.CONTROL: mask = OS.NSControlKeyMask; break;
				case SWT.COMMAND: mask = OS.NSCommandKeyMask; break;
				case SWT.SHIFT: mask = OS.NSShiftKeyMask; break;
				case SWT.CAPS_LOCK:
					Event event = new Event();
					event.keyCode = keyCode;
					setInputState (event, nsEvent, SWT.KeyDown);
					sendKeyEvent (SWT.KeyDown, event);
					setInputState (event, nsEvent, SWT.KeyUp);
					sendKeyEvent (SWT.KeyUp, event);
					break;
			}
			if (mask != 0) {
				int type = (mask & modifiers) != 0 ? SWT.KeyDown : SWT.KeyUp;
				Event event = new Event();
				event.keyCode = keyCode;
				setInputState (event, nsEvent, type);
				if (!sendKeyEvent (type, event)) return;
			}
		}
	}
	super.flagsChanged (id, sel, theEvent);
}

NSView focusView () {
	return view;
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
	if (!isEnabled () || !isVisible () || !isActive ()) return false;
	if (isFocusControl ()) return true;
	shell.setSavedFocus (null);
	shell.bringToTop (false);
	if (isDisposed ()) return false;
	boolean result = view.window ().makeFirstResponder (focusView ());
	if (isDisposed ()) return false;
	shell.setSavedFocus (this);
	return result;
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
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.getBackgroundColor ();
}

Color getBackgroundColor () {
	return background != null ? background : defaultBackground ();
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
	NSRect rect = topView().frame();
	return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
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
	return foreground != null ? foreground: defaultForeground ();
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
	NSRect rect = topView().frame();
	return new Point((int)rect.x, (int)rect.y);
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

NSBezierPath getPath(Region region) {
	if (region == null) return null;
	Callback callback = new Callback(this, "regionToRects", 4);
	if (callback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	NSBezierPath path = NSBezierPath.bezierPath();
	path.retain();
	OS.QDRegionToRects(region.handle, OS.kQDParseRegionFromTopLeft, callback.getAddress(), path.id);
	callback.dispose();
	if (path.isEmpty()) path.appendBezierPathWithRect(new NSRect());
	return path;
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
	NSRect rect = topView().frame();
	return new Point((int)rect.width, (int)rect.height);
}

float getThemeAlpha () {
	return 1 * parent.getThemeAlpha ();
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
	return (state & HIDDEN) == 0;
}

int /*long*/ getVisibleRegion () {
	if (visibleRgn == 0) {
		visibleRgn = OS.NewRgn ();
		calculateVisibleRegion (view, visibleRgn, true);
	}
	int /*long*/ result = OS.NewRgn ();
	OS.CopyRgn (visibleRgn, result);
	return result;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

boolean hasFocus () {
	NSWindow window = view.window();
	if (window.isKeyWindow()) {
		NSResponder nsResponder = window.firstResponder();
		if (nsResponder.id == focusView().id) return true;
		NSText fieldEditor = window.fieldEditor(false, null);
		if (nsResponder.isKindOfClass(OS.class_NSTextView) && fieldEditor != null) {
			id delegate = fieldEditor.delegate();
			if (delegate != null) {
				return delegate.id == focusView().id;
			}
		}
	}
	return false;
}

int /*long*/ hitTest (int /*long*/ id, int /*long*/ sel, NSPoint point) {
	if ((state & DISABLED) != 0) return 0;
	if (!isActive ()) return 0;
	if (regionPath != null) {
		NSView superview = new NSView(id).superview();
		if (superview != null) {
			NSPoint pt = superview.convertPoint_toView_(point, view);
			if (!regionPath.containsPoint(pt)) return 0;
		}
	}
	return super.hitTest(id, sel, point);
}

boolean imeInComposition () {
	return false;
}

boolean insertText (int /*long*/ id, int /*long*/ sel, int /*long*/ string) {
	if (view.window ().firstResponder ().id == id) {
		Shell s = this.getShell();
		NSEvent nsEvent = NSApplication.sharedApplication ().currentEvent ();
		if (!s.keyInputHappened && nsEvent != null && nsEvent.type () == OS.NSKeyDown) {
			NSString str = new NSString (string);
			if (str.isKindOfClass (OS.objc_getClass ("NSAttributedString"))) {
				str = new NSAttributedString (string).string ();
			}
			int length = (int)/*64*/str.length ();
			char[] buffer = new char [length];
			str.getCharacters(buffer);
			for (int i = 0; i < buffer.length; i++) {
				s.keyInputHappened = true;
				Event event = new Event ();
				if (i == 0) setKeyState (event, SWT.KeyDown, nsEvent);
				event.character = buffer [i];
				if (!sendKeyEvent (SWT.KeyDown, event)) return false;
			}
		}
		if ((state & CANVAS) != 0) return true;
	}
	return super.insertText (id, sel, string);
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
public int /*long*/ internal_new_GC (GCData data) {
	checkWidget();
	NSView view = paintView();
	int /*long*/ context = 0;
	if (data != null && data.paintRect != null) {
		context = NSGraphicsContext.currentContext().id;
	} else {
		NSGraphicsContext graphicsContext = NSGraphicsContext.graphicsContextWithWindow (view.window ());
		NSGraphicsContext flippedContext = NSGraphicsContext.graphicsContextWithGraphicsPort(graphicsContext.graphicsPort(), true);
		graphicsContext = flippedContext;
		context = graphicsContext.id;
		if (data != null) {
			data.flippedContext = flippedContext;
			data.state &= ~VISIBLE_REGION;
			data.visibleRgn = getVisibleRegion();
			display.addContext (data);
		}
	}
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.device = display;
		data.thread = display.thread;
		data.view = view;
		data.foreground = getForegroundColor ().handle;
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		data.background = control.getBackgroundColor ().handle;
		data.font = font != null ? font : defaultFont ();		
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
public void internal_dispose_GC (int /*long*/ context, GCData data) {
	checkWidget ();
	NSGraphicsContext graphicsContext = new NSGraphicsContext (context);
	display.removeContext (data);
	if (data != null) {
		if (data.paintRect == null) graphicsContext.flushGraphics ();
		if (data.visibleRgn != 0) OS.DisposeRgn(data.visibleRgn);
		data.visibleRgn = 0;
	}
}

void invalidateChildrenVisibleRegion () {
}

void invalidateVisibleRegion () {
	int index = 0;
	Control[] siblings = parent._getChildren ();
	while (index < siblings.length && siblings [index] != this) index++;
	for (int i=index; i<siblings.length; i++) {
		Control sibling = siblings [i];
		sibling.resetVisibleRegion ();
		sibling.invalidateChildrenVisibleRegion ();
	}
	parent.resetVisibleRegion ();
}

boolean isActive () {
	return getShell().getModalShell() == null;
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
	return hasFocus ();
}

boolean isObscured () {
	int /*long*/ visibleRgn = getVisibleRegion(), boundsRgn = OS.NewRgn();
	short[] rect = new short[4];
	NSRect bounds = view.visibleRect();
	OS.SetRect(rect, (short)bounds.x, (short)bounds.y, (short)(bounds.x + bounds.width), (short)(bounds.y + bounds.height));
	OS.RectRgn(boundsRgn, rect);
	OS.DiffRgn(boundsRgn, visibleRgn, boundsRgn);
	boolean obscured = !OS.EmptyRgn (boundsRgn);
	OS.DisposeRgn(boundsRgn);
	OS.DisposeRgn(visibleRgn);
	return obscured;
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
	int code = traversalCode (0, null);
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
	int code = traversalCode (0, null);
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
}

boolean isTrim (NSView view) {
	return false;
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

void keyDown (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (view.window ().firstResponder ().id == id) {
		Shell s = this.getShell();
		s.keyInputHappened = false;
		boolean textInput = OS.objc_msgSend (id, OS.sel_conformsToProtocol_, OS.objc_getProtocol ("NSTextInput")) != 0;
		if (!textInput) {
			// Not a text field, so send a key event here.
			NSEvent nsEvent = new NSEvent (theEvent);
			boolean [] consume = new boolean [1];
			if (translateTraversal (nsEvent.keyCode (), nsEvent, consume)) return;
			if (isDisposed ()) return;
			if (!sendKeyEvent (nsEvent, SWT.KeyDown)) return;
			if (consume [0]) return;
		} else {
			// Control is some kind of text field, so the key event will be sent from insertText: or doCommandBySelector:
			super.keyDown (id, sel, theEvent);

			if (imeInComposition ()) return;
			// If none of those methods triggered a key event send one now.
			if (!s.keyInputHappened) {
				NSEvent nsEvent = new NSEvent (theEvent);
				boolean [] consume = new boolean [1];
				if (translateTraversal (nsEvent.keyCode (), nsEvent, consume)) return;
				if (isDisposed ()) return;
				if (!sendKeyEvent (nsEvent, SWT.KeyDown)) return;
				if (consume [0]) return;
			}
			
			return;
		}
	}
	super.keyDown (id, sel, theEvent);
}

void keyUp (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (view.window ().firstResponder ().id == id) {
		NSEvent nsEvent = new NSEvent (theEvent);
		if (!sendKeyEvent (nsEvent, SWT.KeyUp)) return;
	}
	super.keyUp (id, sel, theEvent);
}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

int /*long*/ menuForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!isEnabled ()) return 0;

	NSPoint pt = NSEvent.mouseLocation();
	pt.y = (int) (display.getPrimaryFrame().height - pt.y);
	int x = (int) pt.x;
	int y = (int) pt.y;
	Event event = new Event ();
	event.x = x;
	event.y = y;
	sendEvent (SWT.MenuDetect, event);
	if (!event.doit) return 0;
	Menu menu = getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		if (x != event.x || y != event.y) {
			menu.setLocation (event.x, event.y);
		}
		return menu.nsMenu.id;
	}
	return super.menuForEvent (id, sel, theEvent);
}

Decorations menuShell () {
	return parent.menuShell ();
}

void scrollWheel (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (id == view.id) {
		if (hooks (SWT.MouseWheel) || filters (SWT.MouseWheel)) {
			NSEvent nsEvent = new NSEvent(theEvent);
			if (nsEvent.deltaY() != 0) {
				if (!sendMouseEvent(nsEvent, SWT.MouseWheel, true)) {
					return;
				}
			}
		}
	}
	super.scrollWheel(id, sel, theEvent); 
}

boolean isEventView (int /*long*/ id) {
	return true;
}

boolean mouseEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent, int type) {
	if (!display.sendEvent) return true;
	display.sendEvent = false;
	if (!isEventView (id)) return true;
	boolean dragging = false;
	boolean[] consume = null;
	NSEvent nsEvent = new NSEvent(theEvent);
	int nsType = (int)/*64*/nsEvent.type();
	NSInputManager manager = NSInputManager.currentInputManager ();
	if (manager != null && manager.wantsToHandleMouseEvents ()) {
		if (manager.handleMouseEvent (nsEvent)) {
			return true;
		}
	}
	switch (nsType) {
		case OS.NSLeftMouseDown:
			if (nsEvent.clickCount() == 1 && (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
				consume = new boolean[1];
				NSPoint location = view.convertPoint_fromView_(nsEvent.locationInWindow(), null);
				dragging = dragDetect((int)location.x, (int)location.y, false, consume);
			}
			break;
		case OS.NSLeftMouseDragged:
		case OS.NSRightMouseDragged:
		case OS.NSOtherMouseDragged:
			display.checkEnterExit (this, nsEvent, false);
			break;
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
			display.checkEnterExit (display.findControl(true), nsEvent, false);
			break;
	}
	sendMouseEvent (nsEvent, type, false);	
	if (type == SWT.MouseDown && nsEvent.clickCount() == 2) {
		sendMouseEvent (nsEvent, SWT.MouseDoubleClick, false);
	}
	if (dragging) sendMouseEvent(nsEvent, SWT.DragDetect, false);
	if (consume != null && consume[0]) return false;
	return true;
}

void mouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseDown)) return;
	boolean tracking = isEventView (id);
	Display display = this.display;
	if (tracking) display.trackingControl = this;
	super.mouseDown(id, sel, theEvent);
	if (tracking) display.trackingControl = null;
}

void mouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseUp)) return;
	super.mouseUp(id, sel, theEvent);
}

void mouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseMove)) return;
	super.mouseDragged(id, sel, theEvent);
}

void rightMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseDown)) return;
	super.rightMouseDown(id, sel, theEvent);
}

void rightMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseUp)) return;
	super.rightMouseUp(id, sel, theEvent);
}

void rightMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseMove)) return;
	super.rightMouseDragged(id, sel, theEvent);
}

void otherMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseDown)) return;
	super.otherMouseDown(id, sel, theEvent);
}

void otherMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseUp)) return;
	super.otherMouseUp(id, sel, theEvent);
}

void otherMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!mouseEvent(id, sel, theEvent, SWT.MouseMove)) return;
	super.otherMouseDragged(id, sel, theEvent);
}

void moved () {
	sendEvent (SWT.Move);
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

NSView paintView () {
	return eventView ();
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
	
	if (view.isFlipped()) {
		gc.handle.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform ();
		transform.scaleXBy (1, -1);
		transform.translateXBy (0, -(view.bounds().height + 2 * view.bounds().y));
		transform.concat ();
	}
	
	view.displayRectIgnoringOpacity(view.bounds(), gc.handle);
	
	if (view.isFlipped()) gc.handle.restoreGraphicsState();
	return true;
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
	view.setNeedsDisplay(true);
}

void redraw (boolean children) {
//	checkWidget();
	view.setNeedsDisplay(true);
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
	NSRect rect = new NSRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	view.setNeedsDisplayInRect(rect);
}

int /*long*/ regionToRects(int /*long*/ message, int /*long*/ rgn, int /*long*/ r, int /*long*/ path) {
	NSPoint pt = new NSPoint();
	short[] rect = new short[4];
	if (message == OS.kQDRegionToRectsMsgParse) {
		OS.memmove(rect, r, rect.length * 2);
		pt.x = rect[1];
		pt.y = rect[0];
		OS.objc_msgSend(path, OS.sel_moveToPoint_, pt);
		pt.x = rect[3];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[3];
		pt.y = rect[2];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[1];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		OS.objc_msgSend(path, OS.sel_closePath);
	}
	return 0;
}

void register () {
	super.register ();
	display.addWidget (view, this);
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
	if (view != null) view.release();
	view = null;
	parent = null;
}

void releaseParent () {
	invalidateVisibleRegion ();
	parent.removeControl (this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (display.currentControl == this) {
		display.currentControl = null;
		display.timerExec(-1, display.hoverTimer);
	}
	if (display.trackingControl == this) display.trackingControl = null;
	if (display.tooltipControl == this) display.tooltipControl = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	if (visibleRgn != 0) OS.DisposeRgn (visibleRgn);
	visibleRgn = 0;
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
	region = null;
	if (regionPath != null) regionPath.release();
	regionPath = null;
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
	if (!isDescribedByLabel()) return;
	NSObject accessibleElement = focusView();
	
	if (accessibleElement instanceof NSControl) {
		NSControl viewAsControl = (NSControl) accessibleElement;
		if (viewAsControl.cell() != null) accessibleElement = viewAsControl.cell();
	}
	
	accessibleElement.accessibilitySetOverrideValue(accessibleElement, OS.NSAccessibilityTitleUIElementAttribute);
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

void resetVisibleRegion () {
	if (visibleRgn != 0) {
		OS.DisposeRgn (visibleRgn);
		visibleRgn = 0;
	}
	GCData[] gcs = display.contexts;
	if (gcs != null) {
		int /*long*/ visibleRgn = 0;
		for (int i=0; i<gcs.length; i++) {
			GCData data = gcs [i];
			if (data != null) {
				if (data.view == view) {
					if (visibleRgn == 0) visibleRgn = getVisibleRegion ();
					data.state &= ~VISIBLE_REGION;
					OS.CopyRgn (visibleRgn, data.visibleRgn);
				}
			}
		}
		if (visibleRgn != 0) OS.DisposeRgn (visibleRgn);
	}
}

void resized () {
	sendEvent (SWT.Resize);
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

void sendFocusEvent (int type, boolean post) {
	Display display = this.display;
	Shell shell = getShell ();
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
}

boolean sendMouseEvent (NSEvent nsEvent, int type, boolean send) {
	Shell shell = null;
	Event event = new Event ();
	switch (type) {
		case SWT.MouseDown:
			shell = getShell ();
			//FALL THROUGH
		case SWT.MouseUp:
		case SWT.MouseDoubleClick:
		case SWT.DragDetect:
			int button = (int)/*64*/nsEvent.buttonNumber();
			switch (button) {
				case 0: event.button = 1; break;
				case 1: event.button = 3; break;
				case 2: event.button = 2; break;
				case 3: event.button = 4; break;
				case 4: event.button = 5; break;
			}
			break;
		case SWT.MouseWheel:
			event.detail = SWT.SCROLL_LINE;
			float /*double*/ delta = nsEvent.deltaY();
			event.count = delta > 0 ? Math.max (1, (int)delta) : Math.min (-1, (int)delta);
			break;
	}
	if (event.button != 0) event.count = (int)/*64*/nsEvent.clickCount();
	NSPoint windowPoint;
	NSView view = eventView ();
	if (nsEvent == null || nsEvent.type() == OS.NSMouseMoved) {
		NSWindow window = view.window();
		windowPoint = window.convertScreenToBase(NSEvent.mouseLocation()); 
	} else {
		windowPoint = nsEvent.locationInWindow();
	}
	NSPoint point = view.convertPoint_fromView_(windowPoint, null);
	event.x = (int) point.x;
	event.y = (int) point.y;
	setInputState (event, nsEvent, type);
	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	if (shell != null) shell.setActiveControl(this);
	return event.doit;
}

void setBackground () {
//	redrawWidget (handle, false);
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
//	if (equals (background, this.background)) return;
	this.background = color;
	updateBackground ();
	view.setNeedsDisplay(true);
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
	updateBackground();
	redrawWidget(view, false);
}

void updateBackground () {
}

void setBackground (NSColor nsColor) {
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
	setBounds (x, y, Math.max (0, width), Math.max (0, height), true, true);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	NSView topView = topView();
	if (move && resize) {
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		topView.setFrame (rect);
	} else if (move) {
		NSPoint point = new NSPoint();
		point.x = x;
		point.y = y;
		topView.setFrameOrigin(point);
	} else if (resize) {
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		topView.setFrameSize(size);
	}
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
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true);
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

void setClipRegion (float /*double*/ x, float /*double*/ y) {
	if (regionPath != null) {
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(-x, -y);
		regionPath.transformUsingAffineTransform(transform);
		regionPath.addClip();
		transform.translateXBy(2*x, 2*y);
		regionPath.transformUsingAffineTransform(transform);
	}
	NSRect frame = topView().frame();
	parent.setClipRegion(frame.x + x, frame.y + y);
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
	if (!isEnabled()) return;
	display.setCursor (display.currentControl);
}

void setDefaultFont () {
	if (display.smallFonts) {
		setFont (defaultFont ().handle);
		setSmallSize ();
	}
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
		control = display.getFocusControl ();
		fixFocus = isFocusAncestor (control);
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
	setFont (font != null ? font.handle : defaultFont().handle);
}

void setFont (NSFont font) {
	if (view instanceof NSControl) {
		((NSControl)view).setFont(font);
	}
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
//	if (equals (foreground, this.foreground)) return;
	this.foreground = color;
	setForeground (color != null ? color.handle : null);
	view.setNeedsDisplay(true);
}

void setForeground (float /*double*/ [] color) {
}

void setFrameOrigin (int /*long*/ id, int /*long*/ sel, NSPoint point) {
	NSView topView = topView ();
	if (topView.id != id) {
		super.setFrameOrigin(id, sel, point);
		return;
	}
	NSRect frame = topView.frame();
	super.setFrameOrigin(id, sel, point);
	if (frame.x != point.x || frame.y != point.y) {
		invalidateVisibleRegion();
		moved ();
	}
}

void setFrameSize (int /*long*/ id, int /*long*/ sel, NSSize size) {
	NSView topView = topView ();
	if (topView.id != id) {
		super.setFrameSize(id, sel, size);
		return;
	}
	NSRect frame = topView.frame();
	super.setFrameSize(id, sel, size);
	if (frame.width != size.width || frame.height != size.height) {
		invalidateVisibleRegion();
		resized ();
	}
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
	setBounds (x, y, 0, 0, true, false);
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
	setBounds (location.x, location.y, 0, 0, true, false);
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
	NSView topView = topView ();
	topView.retain();
	topView.removeFromSuperview();
	parent.contentView().addSubview(topView, OS.NSWindowBelow, null);
	topView.release();
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
			invalidateVisibleRegion ();
			redrawWidget(topView (), true);
		}
	} else {
		if (drawCount == 0) {
			invalidateVisibleRegion ();
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
	if (regionPath != null) regionPath.release();
	regionPath = getPath(region);
	redrawWidget(view, true);
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

boolean setRadioSelection (boolean value){
	return false;
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
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), false, true);
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
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true);
}

void setSmallSize () {
	if (view instanceof NSControl) {
		NSCell cell = ((NSControl)view).cell();
		if (cell != null) cell.setControlSize (OS.NSSmallControlSize);
	}
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
	checkToolTip (null);
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
		control = display.getFocusControl ();
		fixFocus = isFocusAncestor (control);
	}
	topView().setHidden(!visible);
	invalidateVisibleRegion();
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
	NSView topView = topView ();
	parent.contentView().addSubview(topView, OS.NSWindowBelow, null);
}

boolean shouldDelayWindowOrderingForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	Shell shell = getShell ();
	if ((shell.style & SWT.ON_TOP) != 0) return false;
	return super.shouldDelayWindowOrderingForEvent (id, sel, theEvent);
}

void setZOrder (Control sibling, boolean above) {
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

	NSView otherView = sibling == null ? null : sibling.topView ();
	view.retain();
	view.removeFromSuperview();
	parent.contentView().addSubview(view, above ? OS.NSWindowAbove : OS.NSWindowBelow, otherView);
	view.release();
	invalidateVisibleRegion();
	
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

NSSize textExtent (String string) {
	NSMutableDictionary dict = ((NSMutableDictionary)new NSMutableDictionary().alloc()).initWithCapacity(1);
	Font font = this.font != null ? this.font : defaultFont();
	dict.setObject(font.handle, OS.NSFontAttributeName);
	addTraits(dict, font);
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	NSString str = ((NSString)new NSString().alloc()).initWithCharacters(chars, length);
	NSAttributedString attribStr = ((NSAttributedString)new NSAttributedString().alloc()).initWithString(str, dict);
	NSSize size = attribStr.size();
	attribStr.release();
	dict.release();
	str.release();
	return size;
}

String tooltipText () {
	return toolTipText;
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
    return display.map (null, this, x, y);
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
	return display.map (this, null, x, y);
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

NSView topView () {
	return view;
}

boolean translateTraversal (int key, NSEvent theEvent, boolean [] consume) {
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
			int /*long*/ modifiers = theEvent.modifierFlags ();
			boolean next = (modifiers & OS.NSShiftKeyMask) == 0;
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
			int /*long*/ modifiers = theEvent.modifierFlags ();
			if ((modifiers & OS.NSControlKeyMask) == 0) return false;
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

int traversalCode (int key, NSEvent theEvent) {
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
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
	if (display.isPainting.containsObject(view)) return;
	//TODO - not all
	view.displayIfNeeded ();
}

void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}

void resetCursorRects (int /*long*/ id, int /*long*/ sel) {
	if (isEnabled ()) callSuper (id, sel);
}

void updateTrackingAreas (int /*long*/ id, int /*long*/ sel) {
	if (isEnabled ()) callSuper (id, sel);
}

void updateCursorRects (boolean enabled) {
	updateCursorRects (enabled, view);
}

void updateCursorRects (boolean enabled, NSView widget) {
	if (enabled) {
		widget.resetCursorRects ();
		widget.updateTrackingAreas ();
	} else {
		widget.discardCursorRects ();
		NSArray areas = widget.trackingAreas ();
		for (int i = 0; i < areas.count(); i++) {
			widget.removeTrackingArea (new NSTrackingArea (areas.objectAtIndex (i)));
		}
	}
}

void updateLayout (boolean all) {
	/* Do nothing */
}

}
