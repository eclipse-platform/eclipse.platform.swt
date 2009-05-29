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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a expandable item in a expand bar.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandBar
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	boolean expanded, hover;
	int x, y, width, height;
	int imageHeight, imageWidth;
	static final int TEXT_INSET = 6;
	static final int BORDER = 1;
	static final int CHEVRON_SIZE = 24;
	
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style) {
	this (parent, style, checkNull (parent).getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent, a
 * style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, style, index);
}

static ExpandBar checkNull (ExpandBar control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;	
}

private void drawChevron (int /*long*/ hDC, RECT rect) {
	int /*long*/ oldBrush = OS.SelectObject (hDC, OS.GetSysColorBrush (OS.COLOR_BTNFACE));
	OS.PatBlt (hDC, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
	OS.SelectObject (hDC, oldBrush);
	rect.left += 4;
	rect.top += 4;
	rect.right -= 4;
	rect.bottom -= 4;
	int /*long*/ hPen = OS.CreatePen (OS.PS_SOLID, 1, parent.getForegroundPixel ());
	int /*long*/ oldPen = OS.SelectObject (hDC, hPen);
	int [] polyline1, polyline2;
	if (expanded) {
		int px = rect.left + 5;
		int py = rect.top + 7;
		polyline1 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1, px+5,py-1, px+5,py, px+7,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1,  px+5,py-1, px+5,py, px+7,py};
	} else {
		int px = rect.left + 5;
		int py = rect.top + 4;
		polyline1 = new int[] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+7,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+7,py};
	}
	OS.Polyline (hDC, polyline1, polyline1.length / 2);
	OS.Polyline (hDC, polyline2, polyline2.length / 2);
	if (hover) {
		int /*long*/ whitePen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_3DHILIGHT));
		int /*long*/ darkGrayPen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_3DSHADOW));
		OS.SelectObject (hDC, whitePen);
		int [] points1 = {
				rect.left, rect.bottom,
				rect.left, rect.top,
				rect.right, rect.top};
		OS.Polyline (hDC, points1, points1.length / 2);
		OS.SelectObject (hDC, darkGrayPen);
		int [] points2 = {
				rect.right, rect.top,
				rect.right, rect.bottom,
				rect.left, rect.bottom};
		OS.Polyline (hDC, points2, points2.length / 2);
		OS.SelectObject (hDC, oldPen);
		OS.DeleteObject (whitePen);
		OS.DeleteObject (darkGrayPen);
	} else {
		OS.SelectObject (hDC, oldPen);
	}		
	OS.DeleteObject (hPen);
}

void drawItem (GC gc, int /*long*/ hTheme, RECT clipRect, boolean drawFocus) {
	int /*long*/ hDC = gc.handle;
	int headerHeight = parent.getBandHeight ();
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + headerHeight);
	if (hTheme != 0) {
		OS.DrawThemeBackground (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, rect, clipRect);
	} else {
		int /*long*/ oldBrush = OS.SelectObject (hDC, OS.GetSysColorBrush (OS.COLOR_BTNFACE));
		OS.PatBlt (hDC, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
		OS.SelectObject (hDC, oldBrush);		
	}
	if (image != null) {
		rect.left += ExpandItem.TEXT_INSET;
		if (imageHeight > headerHeight) {
			gc.drawImage (image, rect.left, rect.top + headerHeight - imageHeight);
		} else {
			gc.drawImage (image, rect.left, rect.top + (headerHeight - imageHeight) / 2);
		}
		rect.left += imageWidth;
	}
	if (text.length () > 0) {
		rect.left += ExpandItem.TEXT_INSET;
		TCHAR buffer = new TCHAR (parent.getCodePage (), text, false);
		if (hTheme != 0) {
			OS.DrawThemeText (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, buffer.chars, buffer.length(), OS.DT_VCENTER | OS.DT_SINGLELINE, 0, rect);
		} else {
			int oldBkMode = OS.SetBkMode (hDC, OS.TRANSPARENT);
			OS.DrawText (hDC, buffer, buffer.length (), rect, OS.DT_VCENTER | OS.DT_SINGLELINE);
			OS.SetBkMode (hDC, oldBkMode);
		}
	}
	int chevronSize = ExpandItem.CHEVRON_SIZE;
	rect.left = rect.right - chevronSize;
	rect.top = y + (headerHeight - chevronSize) / 2;
	rect.bottom = rect.top + chevronSize;
	if (hTheme != 0) {
		int partID = expanded ? OS.EBP_NORMALGROUPCOLLAPSE : OS.EBP_NORMALGROUPEXPAND;
		int stateID = hover ? OS.EBNGC_HOT : OS.EBNGC_NORMAL;
		OS.DrawThemeBackground (hTheme, hDC, partID, stateID, rect, clipRect);
	} else {
		drawChevron (hDC, rect);
	} 
	if (drawFocus) {
		OS.SetRect (rect, x + 1, y + 1, x + width - 2, y + headerHeight - 2);
		OS.DrawFocusRect (hDC, rect);
	}
	if (expanded) {
		if (!parent.isAppThemed ()) {
			int /*long*/ pen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_BTNFACE));
			int /*long*/ oldPen = OS.SelectObject (hDC, pen);
			int [] points = {
					x, y + headerHeight, 
					x, y + headerHeight + height, 
					x + width - 1, y + headerHeight + height, 
					x + width - 1, y + headerHeight - 1};
			OS.Polyline (hDC, points, points.length / 2);
			OS.SelectObject (hDC, oldPen);
			OS.DeleteObject (pen);
		}
	}
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the control that is shown when the item is expanded.
 * If no control has been set, return <code>null</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget ();
	return control;
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget ();
	return expanded;
}

/**
 * Returns the height of the receiver's header 
 *
 * @return the height of the header 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeaderHeight () {
	checkWidget ();
	return Math.max (parent.getBandHeight (), imageHeight);
}

/**
 * Gets the height of the receiver.
 *
 * @return the height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeight () {
	checkWidget ();
	return height;
}

/**
 * Returns the receiver's parent, which must be a <code>ExpandBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ExpandBar getParent () {
	checkWidget ();
	return parent;
}

int getPreferredWidth (int /*long*/ hTheme, int /*long*/ hDC) {
	int width = ExpandItem.TEXT_INSET * 2 + ExpandItem.CHEVRON_SIZE;
	if (image != null) {
		width += ExpandItem.TEXT_INSET + imageWidth;
	}
	if (text.length() > 0) {
		RECT rect = new RECT ();
		TCHAR buffer = new TCHAR (parent.getCodePage (), text, false);			
		if (hTheme != 0) {
			OS.GetThemeTextExtent (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, buffer.chars, buffer.length(), OS.DT_SINGLELINE, null, rect);			
		} else {
			OS.DrawText (hDC, buffer, buffer.length (), rect, OS.DT_CALCRECT);
		}
		width += (rect.right - rect.left);
	}
	return width;
}

boolean isHover (int x, int y) {
	int bandHeight = parent.getBandHeight ();
	return this.x < x && x < (this.x + width) && this.y < y && y < (this.y + bandHeight);
}

void redraw (boolean all) {
	int /*long*/ parentHandle = parent.handle;
	int headerHeight = parent.getBandHeight ();
	RECT rect = new RECT ();
	int left = all ? x : x + width - headerHeight;
	OS.SetRect (rect, left, y, x + width, y + headerHeight);
	OS.InvalidateRect (parentHandle, rect, true);
	if (imageHeight > headerHeight) {
		OS.SetRect (rect, x + ExpandItem.TEXT_INSET, y + headerHeight - imageHeight, x + ExpandItem.TEXT_INSET + imageWidth, y);
		OS.InvalidateRect (parentHandle, rect, true);
	}
	if (!parent.isAppThemed ()) {
		OS.SetRect (rect, x, y + headerHeight, x + width, y + headerHeight + height + 1);
		OS.InvalidateRect (parentHandle, rect, true);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
}

void setBounds (int x, int y, int width, int height, boolean move, boolean size) {	
	redraw (true);
	int headerHeight = parent.getBandHeight ();
	if (move) {
		if (imageHeight > headerHeight) {
			y += (imageHeight - headerHeight);
		}
		this.x = x;
		this.y = y;
		redraw (true);
	}
	if (size) {
		this.width = width;
		this.height = height;
		redraw (true);
	}
	if (control != null && !control.isDisposed ()) {
		if (!parent.isAppThemed ()) {
			x += BORDER;
			width = Math.max (0, width - BORDER * 2);
			height = Math.max (0, height - BORDER);
		}
		if (move && size) control.setBounds (x, y + headerHeight, width, height);
		if (move && !size) control.setLocation (x, y + headerHeight);
		if (!move && size) control.setSize (width, height);
	}
}

/**
 * Sets the control that is shown when the item is expanded.
 *
 * @param control the new control (or null)
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
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	this.control = control;
	if (control != null) {
		int headerHeight = parent.getBandHeight ();
		control.setVisible (expanded);
		if (!parent.isAppThemed ()) {
			int width = Math.max (0, this.width - BORDER * 2);
			int height = Math.max (0, this.height - BORDER);
			control.setBounds (x + BORDER, y + headerHeight, width, height);
		} else {
			control.setBounds (x, y + headerHeight, width, height);
		}
	}
}

/**
 * Sets the expanded state of the receiver.
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded (boolean expanded) {
	checkWidget ();
	this.expanded = expanded;
	parent.showItem (this);
}

/**
 * Sets the height of the receiver. This is height of the item when it is expanded, 
 * excluding the height of the header.
 *
 * @param height the new height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeight (int height) {
	checkWidget ();
	if (height < 0) return;
	setBounds (0, 0, width, height, false, true);
	if (expanded) parent.layoutItems (parent.indexOf (this) + 1, true);
}

public void setImage (Image image) {
	super.setImage (image);
	int oldImageHeight = imageHeight;
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		imageHeight = bounds.height;
		imageWidth = bounds.width;
	} else {
		imageHeight = imageWidth = 0;
	}
	if (oldImageHeight != imageHeight) {
		parent.layoutItems (parent.indexOf (this), true);
	} else {
		redraw (true);
	}
}

public void setText (String string) {
	super.setText (string);
	redraw (true);
}
}
