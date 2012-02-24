/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * This class is the abstract superclass of all classes which
 * represent controls that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Scrollable extends Control {
 	NSScrollView scrollView;
	ScrollBar horizontalBar, verticalBar;
	
Scrollable () {
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
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Scrollable (Composite parent, int style) {
	super (parent, style);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * 
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getClientArea
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	if (scrollView != null) {
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		int border = hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder;
		if (OS.VERSION >= 0x1070) {
			// Always include the scroll bar in the trim even when the scroll style is overlay
			OS.objc_msgSend_stret(size, OS.class_NSScrollView,
				OS.sel_frameSizeForContentSize_horizontalScrollerClass_verticalScrollerClass_borderType_controlSize_scrollerStyle_,
				size,
				(style & SWT.H_SCROLL) != 0 ? OS.class_NSScroller : 0,
				(style & SWT.V_SCROLL) != 0 ? OS.class_NSScroller : 0,
				border, OS.NSRegularControlSize, OS.NSScrollerStyleLegacy);
		} else {
			size = NSScrollView.frameSizeForContentSize(size, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0, border);
		}
		width = (int)size.width;
		height = (int)size.height;
		NSRect frame = scrollView.contentView().frame();
		x -= frame.x;
		y -= frame.y;
	}
	return new Rectangle (x, y, width, height);
}

ScrollBar createScrollBar (int style) {
	if (scrollView == null) return null;
	ScrollBar bar = new ScrollBar ();
	bar.parent = this;
	bar.style = style;
	bar.display = display;
	NSScroller scroller;
	int /*long*/ actionSelector;
	NSRect rect = new NSRect();
	if ((style & SWT.H_SCROLL) != 0) {
		rect.width = 1;
	} else {
		rect.height = 1;
	}
	scroller = (NSScroller)new SWTScroller().alloc();
	scroller.initWithFrame(rect);
	if ((style & SWT.H_SCROLL) != 0) {
		scrollView.setHorizontalScroller(scroller);
		actionSelector = OS.sel_sendHorizontalSelection;
	} else {
		scrollView.setVerticalScroller(scroller);
		actionSelector = OS.sel_sendVerticalSelection;
	}
	bar.view = scroller;
	bar.createJNIRef();
	bar.register();
	if ((state & CANVAS) == 0) {
		bar.target = scroller.target();
		bar.actionSelector = scroller.action();
	}
	scroller.setTarget(scrollView);
	scroller.setAction(actionSelector);
	if ((state & CANVAS) != 0) {
		bar.updateBar(0, 0, 100, 10);
	}
	return bar;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
}

void deregister () {
	super.deregister ();
	if (scrollView != null) display.removeWidget (scrollView);
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data (that is,
 * not covered by the "trimmings").
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeTrim
 */
public Rectangle getClientArea () {
	checkWidget();
	if (scrollView != null) {
		NSSize size = scrollView.contentSize();
		NSClipView contentView = scrollView.contentView();
		NSRect bounds = contentView.bounds();
		return new Rectangle((int)bounds.x, (int)bounds.y, (int)size.width, (int)size.height);
	} else {
		NSRect rect = view.bounds();
		return new Rectangle(0, 0, (int)rect.width, (int)rect.height);
	}
}

/**
 * Returns the receiver's horizontal scroll bar if it has
 * one, and null if it does not.
 *
 * @return the horizontal scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getHorizontalBar () {
	checkWidget();
	return horizontalBar;
}

/**
 * Returns the mode of the receiver's scrollbars. This will be
 * <em>bitwise</em> OR of one or more of the constants defined in class
 * <code>SWT</code>.<br>
 * <li><code>SWT.SCROLLBAR_OVERLAY</code> - if receiver
 * uses overlay scrollbars</li>
 * <li><code>SWT.NONE</code> - otherwise</li>
 * 
 * @return the mode of scrollbars
 * 
 * @exception SWTException <ul>
 * <li>ERROR_WIDGET_DISPOSED - if the receiver has been
 * disposed</li>
 * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
 * thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#SCROLLBAR_OVERLAY
 * 
 * @since 3.8
 */
public int getScrollbarsMode () {
	checkWidget();
	int style = SWT.NONE;
	if (scrollView != null && OS.VERSION >= 0x1070) {
		if (OS.objc_msgSend (scrollView.id, OS.sel_scrollerStyle) == OS.NSScrollerStyleOverlay) {
			style = SWT.SCROLLBAR_OVERLAY;
		}
	}
	return style;
}

/**
 * Returns the receiver's vertical scroll bar if it has
 * one, and null if it does not.
 *
 * @return the vertical scroll bar (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ScrollBar getVerticalBar () {
	checkWidget();
	return verticalBar;
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp) || hooks (SWT.Traverse);
}

boolean isEventView (int /*long*/ id) {
	return id == eventView ().id;
}

boolean isNeeded(ScrollBar scrollbar) {
	return true;
}

boolean isTrim (NSView view) {
	if (scrollView != null) {
		if (scrollView.id == view.id) return true;
		if (horizontalBar != null && horizontalBar.view.id == view.id) return true;
		if (verticalBar != null && verticalBar.view.id == view.id) return true;
	}
	return super.isTrim (view);
}

void redrawBackgroundImage () {
	if (scrollView != null) {
		Control control = findBackgroundControl();
		if (control != null && control.backgroundImage != null) {
			redrawWidget(view, false);
		}
	}
}

void reflectScrolledClipView(int id, int sel, int aClipView) {
	super.reflectScrolledClipView(id, sel, aClipView);
	redrawBackgroundImage();
}

void register () {
	super.register ();
	if (scrollView != null) display.addWidget (scrollView, this);
}

void releaseHandle () {
	super.releaseHandle ();
	if (scrollView != null) scrollView.release();
	scrollView = null;
}

void releaseChildren (boolean destroy) {
	if (horizontalBar != null) {
		horizontalBar.release (false);
		horizontalBar = null;
	}
	if (verticalBar != null) {
		verticalBar.release (false);
		verticalBar = null;
	}
	super.releaseChildren (destroy);
}

void reskinChildren (int flags) {
	if (horizontalBar != null) horizontalBar.reskin (flags);
	if (verticalBar != null) verticalBar.reskin (flags);
	super.reskinChildren (flags);
}

void scrollClipViewToPoint (int /*long*/ id, int /*long*/ sel, int /*long*/ clipView, NSPoint point) {
	if ((state & CANVAS) == 0 && scrollView != null) {
		NSClipView clip = new NSClipView (clipView);
		boolean oldCopies = clip.copiesOnScroll (), copies = oldCopies;
		if (visibleRgn == 0) copies = !isObscured ();
		if (copies) copies = !hasRegion ();
		clip.setCopiesOnScroll (copies);
	}
	super.scrollClipViewToPoint (id, sel, clipView, point);
}

void sendHorizontalSelection () {
	if (horizontalBar.view.isHiddenOrHasHiddenAncestor()) return;
	horizontalBar.sendSelection ();
}

void sendVerticalSelection () {
	if (verticalBar.view.isHiddenOrHasHiddenAncestor()) return;
	verticalBar.sendSelection ();
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (horizontalBar != null) horizontalBar.enableWidget (enabled && isNeeded(horizontalBar));
	if (verticalBar != null) verticalBar.enableWidget (enabled && isNeeded(verticalBar));
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	if (scrollView == null) return false;
	if ((state & CANVAS) == 0) return false;
	if (visible) {
		if ((bar.state & HIDDEN) == 0) return false;
		bar.state &= ~HIDDEN;
	} else {
		if ((bar.state & HIDDEN) != 0) return false;
		bar.state |= HIDDEN;
	}
	if ((bar.style & SWT.HORIZONTAL) != 0) {
		scrollView.setHasHorizontalScroller (visible);
	} else {
		scrollView.setHasVerticalScroller (visible);
	}
	bar.sendEvent (visible ? SWT.Show : SWT.Hide);
	sendEvent (SWT.Resize);
	return true;
}

void setZOrder () {
	super.setZOrder ();
	if (scrollView != null) scrollView.setDocumentView (view);
}

NSView topView () {
	if (scrollView != null) return scrollView;
	return super.topView ();
}

void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (scrollView == null) return;
	updateCursorRects (enabled, scrollView);	
	NSClipView contentView = scrollView.contentView ();
	updateCursorRects (enabled, contentView);
}

}
