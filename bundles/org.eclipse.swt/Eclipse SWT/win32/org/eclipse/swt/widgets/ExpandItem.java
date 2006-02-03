/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandBar
 * 
 * @since 3.2
 */
public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	boolean expanded, hover;
	int x, y, width, height;	
	static final int TEXT_INSET = 6;
	static final int BORDER = 1;
	
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

public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, style, index);
}

static ExpandBar checkNull (ExpandBar control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;	
}

private void drawChevron (int hDC, RECT rect) {
	rect.left += 4;
	rect.top += 4;
	rect.right -= 4;
	rect.bottom -= 4;		
	int blackPen = OS.CreatePen (OS.PS_SOLID, 1, 0);
	int oldPen = OS.SelectObject (hDC, blackPen);
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
		int whitePen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_3DHILIGHT));
		int darkGrayPen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_3DSHADOW));
		OS.SelectObject (hDC, whitePen);
		OS.MoveToEx (hDC, rect.left, rect.bottom, 0);
		OS.LineTo (hDC, rect.left, rect.top);
		OS.LineTo (hDC, rect.right, rect.top);
		OS.SelectObject (hDC, darkGrayPen);
		OS.LineTo (hDC, rect.right, rect.bottom);
		OS.LineTo (hDC, rect.left, rect.bottom);
		OS.SelectObject (hDC, oldPen);
		OS.DeleteObject (whitePen);
		OS.DeleteObject (darkGrayPen);
	} else {
		OS.SelectObject (hDC, oldPen);
	}		
	OS.DeleteObject (blackPen);
}

void drawItem (GC gc, int hTheme, RECT clipRect, boolean drawFocus) {
	int hDC = gc.handle;
	int headerHeight = ExpandBar.HEADER_HEIGHT;
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + headerHeight);
	if (hTheme != 0) {
		OS.DrawThemeBackground (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, rect, clipRect);
	} else {
		int oldBrush = OS.SelectObject (hDC, OS.GetSysColorBrush (OS.COLOR_BTNFACE));
		OS.PatBlt (hDC, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
		OS.SelectObject (hDC, oldBrush);		
	}
	if (image != null) {
		rect.left += ExpandItem.TEXT_INSET;
		Rectangle bounds = image.getBounds();
		if (bounds.height > headerHeight) {
			gc.drawImage (image, 0, 0, bounds.width, bounds.height, rect.left, rect.top, bounds.width, headerHeight);
		} else {
			gc.drawImage (image, rect.left, rect.top + (headerHeight - bounds.height) / 2);
		}
		rect.left += bounds.width;
	}
	if (text.length () > 0) {
		rect.left += ExpandItem.TEXT_INSET;
		TCHAR buffer = new TCHAR (parent.getCodePage (), text, false);
		if (hTheme != 0) {
			OS.DrawThemeText (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, buffer.chars, buffer.length(), OS.DT_VCENTER | OS.DT_SINGLELINE, 0, rect);
		} else {
			NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
			info.cbSize = NONCLIENTMETRICS.sizeof;
			int hFont = 0, oldFont = 0;
			if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
				LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfCaptionFont : ((NONCLIENTMETRICSA)info).lfCaptionFont;
				hFont = OS.CreateFontIndirect (logFont);
				oldFont = OS.SelectObject (hDC, hFont);
			}
			int oldBkMode = OS.SetBkMode (hDC, OS.TRANSPARENT);
			OS.DrawText (hDC, buffer, buffer.length (), rect, OS.DT_VCENTER | OS.DT_SINGLELINE);
			OS.SetBkMode (hDC, oldBkMode);
			if (hFont != 0) {
				OS.SelectObject (hDC, oldFont);
				OS.DeleteObject (hFont);
			}
		}
	}
	rect.left = rect.right - headerHeight;
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
		if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
			int pen = OS.CreatePen (OS.PS_SOLID, 1, OS.GetSysColor (OS.COLOR_BTNFACE));
			int oldPen = OS.SelectObject (hDC, pen);
			OS.MoveToEx (hDC, x, y + headerHeight, 0);			
			OS.LineTo (hDC, x, y + headerHeight + height);
			OS.LineTo (hDC, x + width - 1, y + headerHeight + height);
			OS.LineTo (hDC, x + width - 1, y + headerHeight - 1);
			OS.SelectObject (hDC, oldPen);
			OS.DeleteObject (pen);
		}
	}
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

public Control getControl () {
	checkWidget ();
	return control;
}

public boolean getExpanded () {
	checkWidget ();
	return expanded;
}

public int getHeight () {
	checkWidget ();
	return height;
}

public ExpandBar getParent () {
	checkWidget ();
	return parent;
}

int getPreferredWidth (int hTheme, int hDC) {	
	int width = ExpandItem.TEXT_INSET * 2 + ExpandBar.HEADER_HEIGHT;
	if (image != null) {
		width += ExpandItem.TEXT_INSET;
		width += image.getBounds ().width;		
	}
	if (text.length() > 0) {
		RECT rect = new RECT ();
		TCHAR buffer = new TCHAR (parent.getCodePage (), text, false);			
		if (hTheme != 0) {
			OS.GetThemeTextExtent (hTheme, hDC, OS.EBP_NORMALGROUPHEAD, 0, buffer.chars, buffer.length(), OS.DT_SINGLELINE, null, rect);			
		} else {
			NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
			info.cbSize = NONCLIENTMETRICS.sizeof;
			int hFont = 0, oldFont = 0;
			if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
				LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfCaptionFont : ((NONCLIENTMETRICSA)info).lfCaptionFont;
				hFont = OS.CreateFontIndirect (logFont);
				oldFont = OS.SelectObject (hDC, hFont);
			}
			OS.DrawText (hDC, buffer, buffer.length (), rect, OS.DT_CALCRECT);
			if (hFont != 0) {
				OS.SelectObject (hDC, oldFont);
				OS.DeleteObject (hFont);
			}
		}
		width += (rect.right - rect.left);
	}
	return width;
}

void redraw (boolean all) {
	int parentHandle = parent.handle;
	RECT rect = new RECT ();
	int left = all ? x : x + width - ExpandBar.HEADER_HEIGHT;
	OS.SetRect (rect, left, y, x + width, y + ExpandBar.HEADER_HEIGHT);
	OS.InvalidateRect (parentHandle, rect, true);
	if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
		OS.SetRect (rect, x, y + ExpandBar.HEADER_HEIGHT, x + width, y + ExpandBar.HEADER_HEIGHT + height + 1);
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
	int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE | OS.SWP_NOSIZE | OS.SWP_NOMOVE;
	if (move) {
		this.x = x;
		this.y = y;
		flags &= ~OS.SWP_NOMOVE;
		redraw (true);
	}
	if (size) {
		this.width = width;
		this.height = height;
		flags &= ~OS.SWP_NOSIZE;
		redraw (true);
	}
	if (control != null && !control.isDisposed ()) {
		int hwnd = control.handle;
		if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
			x += BORDER;
			width = Math.max (0, width - BORDER * 2);
			height = Math.max (0, height - BORDER);
		}
		OS.SetWindowPos (hwnd, 0, x, y + ExpandBar.HEADER_HEIGHT, width, height, flags);
	}
}

public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	this.control = control;
	if (control != null) {
		int hwnd = control.handle;
		OS.ShowWindow (hwnd, expanded ? OS.SW_SHOW : OS.SW_HIDE);
		int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
		if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
			x += BORDER;
			width = Math.max (0, width - BORDER * 2);
			height = Math.max (0, height - BORDER);
		}
		OS.SetWindowPos (hwnd, 0, x, y + ExpandBar.HEADER_HEIGHT, width, height, flags);
	}
}

public void setExpanded (boolean expanded) {
	checkWidget ();
	this.expanded = expanded;
	parent.showItem (parent.indexOf (this));
}

public void setHeight (int height) {
	checkWidget ();
	setBounds (0, 0, width, height, false, true);
	if (expanded) parent.layoutItems (parent.indexOf (this) + 1, true);
}

public void setImage(Image image) {
	super.setImage(image);
	redraw (true);
}

public void setText (String string) {
	super.setText (string);
	redraw (true);
}
}
