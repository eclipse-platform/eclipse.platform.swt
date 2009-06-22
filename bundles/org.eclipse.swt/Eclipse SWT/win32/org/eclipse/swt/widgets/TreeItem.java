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

 
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 * 
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TreeItem extends Item {
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
	public int /*long*/ handle;
	Tree parent;
	String [] strings;
	Image [] images;
	Font font;
	Font [] cellFont;
	boolean cached;
	int background = -1, foreground = -1; 
	int [] cellBackground, cellForeground; 
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
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
public TreeItem (Tree parent, int style) {
	this (parent, style, OS.TVGN_ROOT, OS.TVI_LAST, 0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
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
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style, int index) {
	this (parent, style, OS.TVGN_ROOT, findPrevious (parent, index), 0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
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
public TreeItem (TreeItem parentItem, int style) {
	this (checkNull (parentItem).parent, style, parentItem.handle, OS.TVI_LAST, 0);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
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
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style, int index) {
	this (checkNull (parentItem).parent, style, parentItem.handle, findPrevious (parentItem, index), 0);
}

TreeItem (Tree parent, int style, int /*long*/ hParent, int /*long*/ hInsertAfter, int /*long*/ hItem) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, hParent, hInsertAfter, hItem);
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static int /*long*/ findPrevious (Tree parent, int index) {
	if (parent == null) return 0;
	if (index < 0) SWT.error (SWT.ERROR_INVALID_RANGE);
	if (index == 0) return OS.TVI_FIRST;
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hFirstItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	int /*long*/ hItem = parent.findItem (hFirstItem, index - 1);
	if (hItem == 0) SWT.error (SWT.ERROR_INVALID_RANGE);
	return hItem;
}

static int /*long*/ findPrevious (TreeItem parentItem, int index) {
	if (parentItem == null) return 0;
	if (index < 0) SWT.error (SWT.ERROR_INVALID_RANGE);
	if (index == 0) return OS.TVI_FIRST;
	Tree parent = parentItem.parent;
	int /*long*/ hwnd = parent.handle, hParent = parentItem.handle;
	int /*long*/ hFirstItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent);
	int /*long*/ hItem = parent.findItem (hFirstItem, index - 1);
	if (hItem == 0) SWT.error (SWT.ERROR_INVALID_RANGE);
	return hItem;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	text = "";
	image = null;
	strings = null;
	images = null;
	if ((parent.style & SWT.CHECK) != 0) {
		int /*long*/ hwnd = parent.handle;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
		tvItem.state = 1 << 12;
		tvItem.hItem = handle;
		OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	}
	background = foreground = -1;
	font = null;
	cellBackground = cellForeground = null; 
	cellFont = null;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the tree was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 * @param all <code>true</code> if all child items of the indexed item should be
 * cleared recursively, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 * 
 * @since 3.2
 */
public void clear (int index, boolean all) {
	checkWidget ();
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	hItem = parent.findItem (hItem, index);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	parent.clear (hItem, tvItem);
	if (all) {
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		parent.clearAll (hItem, tvItem, all);
	}
}

/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * tree was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 * 
 * @param all <code>true</code> if all child items should be cleared
 * recursively, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 * 
 * @since 3.2
 */
public void clearAll (boolean all) {
	checkWidget ();
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	if (hItem == 0) return;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	parent.clearAll (hItem, tvItem, all);
}

void destroyWidget () {
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	parent.releaseItem (handle, tvItem, false);
	parent.destroyItem (this, handle);
	releaseHandle ();
}

int /*long*/ fontHandle (int index) {
	if (cellFont != null && cellFont [index] != null) return cellFont [index].handle;
	if (font != null) return font.handle;
	return -1;
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
 * 
 * @since 2.0
 * 
 */
public Color getBackground () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (background == -1) return parent.getBackground ();
	return Color.win32_new (display, background);
}

/**
 * Returns the background color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Color getBackground (int index) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return getBackground ();
	int pixel = cellBackground != null ? cellBackground [index] : -1;
	return pixel == -1 ? getBackground () : Color.win32_new (display, pixel);
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	RECT rect = getBounds (0, true, false, false);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Rectangle getBounds (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	RECT rect = getBounds (index, true, true, true);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

RECT getBounds (int index, boolean getText, boolean getImage, boolean fullText) {
	return getBounds (index, getText, getImage, fullText, false, true, 0);
}

//TODO - take into account grid (add boolean arg) to damage less during redraw
RECT getBounds (int index, boolean getText, boolean getImage, boolean fullText, boolean fullImage, boolean clip, int /*long*/ hDC) {
	if (!getText && !getImage) return new RECT ();
	int /*long*/ hwnd = parent.handle; 
	if ((parent.style & SWT.VIRTUAL) == 0 && !cached && !parent.painted) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
		tvItem.hItem = handle;
		tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
		parent.ignoreCustomDraw = true;
		OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
		parent.ignoreCustomDraw = false;
	}
	boolean firstColumn = index == 0;
	int columnCount = 0;
	int /*long*/ hwndHeader = parent.hwndHeader;
	if (hwndHeader != 0) {
		columnCount = parent.columnCount;
		firstColumn = index == OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0);
	}
	RECT rect = new RECT ();
	if (firstColumn) {
		boolean full = columnCount == 0 && getText && getImage && fullText && fullImage;
		if (!OS.TreeView_GetItemRect (hwnd, handle, rect, !full)) {
			return new RECT ();
		}
		if (getImage && !fullImage) {
			if (OS.SendMessage (hwnd, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0) != 0) {
				Point size = parent.getImageSize ();
				rect.left -= size.x + Tree.INSET;
				if (!getText) rect.right = rect.left + size.x;
			} else {
				if (!getText) rect.right = rect.left;
			}
		}
		if (fullText || fullImage || clip) {
			if (hwndHeader != 0) {
				RECT headerRect = new RECT ();
				if (columnCount != 0) {
					if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect) == 0) {
						return new RECT ();
					}
				} else {
					headerRect.right = parent.scrollWidth;
					if (headerRect.right == 0) headerRect = rect;
				}
				if (fullText && clip) rect.right = headerRect.right;
				if (fullImage) rect.left = headerRect.left;
				if (clip && headerRect.right < rect.right) {
					rect.right = headerRect.right;
				}
			}
		}
	} else {
		if (!(0 <= index && index < columnCount)) return new RECT ();
		RECT headerRect = new RECT ();
		if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect) == 0) {
			return new RECT ();
		}
		if (!OS.TreeView_GetItemRect (hwnd, handle, rect, false)) {
			return new RECT ();
		}
		rect.left = headerRect.left;
		if (fullText && getImage && clip) {
			rect.right = headerRect.right;
		} else {
			rect.right = headerRect.left;
			Image image = null;
			if (index == 0) {
				image = this.image;
			} else {
				if (images != null) image = images [index];
			}
			if (image != null) {
				Point size = parent.getImageSize ();
				rect.right += size.x;
			}
			if (getText) {
				if (fullText && clip) {
					rect.left = rect.right + Tree.INSET;
					rect.right = headerRect.right;
				} else {
					String string = index == 0 ? text : strings != null ? strings [index] : null;
					if (string != null) {
						RECT textRect = new RECT ();
						TCHAR buffer = new TCHAR (parent.getCodePage (), string, false);
						int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_CALCRECT;
						int /*long*/ hNewDC = hDC, hFont = 0;
						if (hDC == 0) {
							hNewDC = OS.GetDC (hwnd);
							hFont = fontHandle (index);
							if (hFont == -1) hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
							hFont = OS.SelectObject (hNewDC, hFont);
						}
						OS.DrawText (hNewDC, buffer, buffer.length (), textRect, flags);
						if (hDC == 0) {
							OS.SelectObject (hNewDC, hFont);
							OS.ReleaseDC (hwnd, hNewDC);
						}
						if (getImage) {
							rect.right += textRect.right - textRect.left + Tree.INSET * 3;
						} else {
							rect.left = rect.right + Tree.INSET;
							rect.right = rect.left + (textRect.right - textRect.left) + Tree.INSET;
						}
					}
				}
			}
			if (clip && headerRect.right < rect.right) {
				rect.right = headerRect.right;
			}
		}
	}
	int gridWidth = parent.linesVisible && columnCount != 0 ? Tree.GRID_WIDTH : 0;
	if (getText || !getImage) {
		rect.right = Math.max (rect.left, rect.right - gridWidth);
	}
	rect.bottom = Math.max (rect.top, rect.bottom - gridWidth);
	return rect;
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	int /*long*/ result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return (result != 0) && (((tvItem.state >> 12) & 1) == 0);
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 * <p>
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
	int /*long*/ hwnd = parent.handle;
	int state = 0;
	if (OS.IsWinCE) {
		TVITEM tvItem = new TVITEM ();
		tvItem.hItem = handle;
		tvItem.mask = OS.TVIF_STATE;
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		state = tvItem.state;
	} else {
		/*
		* Bug in Windows.  Despite the fact that TVM_GETITEMSTATE claims
		* to return only the bits specified by the stateMask, when called
		* with TVIS_EXPANDED, the entire state is returned.  The fix is
		* to explicitly check for the TVIS_EXPANDED bit.
		*/
		state = (int)/*64*/OS.SendMessage (hwnd, OS.TVM_GETITEMSTATE, handle, OS.TVIS_EXPANDED);
	}
	return (state & OS.TVIS_EXPANDED) != 0;
}

/**
 * Returns the font that the receiver will use to paint textual information for this item.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return font != null ? font : parent.getFont ();
}

/**
 * Returns the font that the receiver will use to paint textual information
 * for the specified cell in this item.
 *
 * @param index the column index
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Font getFont (int index) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return getFont ();
	if (cellFont == null || cellFont [index] == null) return getFont ();
	return cellFont [index];
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
 * 
 * @since 2.0
 * 
 */
public Color getForeground () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (foreground == -1) return parent.getForeground ();
	return Color.win32_new (display, foreground);
}

/**
 * 
 * Returns the foreground color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Color getForeground (int index) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return getForeground ();
	int pixel = cellForeground != null ? cellForeground [index] : -1;
	return pixel == -1 ? getForeground () : Color.win32_new (display, pixel);
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	int /*long*/ result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return (result != 0) && ((tvItem.state >> 12) > 2);
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public TreeItem getItem (int index) {
	checkWidget ();
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hFirstItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	if (hFirstItem == 0) error (SWT.ERROR_INVALID_RANGE);
	int /*long*/ hItem = parent.findItem (hFirstItem, index);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	return parent._getItem (hItem);
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	if (hItem == 0) return 0;
	return parent.getItemCount (hItem);
}

/**
 * Returns a (possibly empty) array of <code>TreeItem</code>s which
 * are the direct item children of the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	if (hItem == 0) return new TreeItem [0];
	return parent.getItems (hItem);
}

public Image getImage () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getImage ();
}

/**
 * Returns the image stored at the given column index in the receiver,
 * or null if the image has not been set or if the column does not exist.
 *
 * @param index the column index
 * @return the image stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Image getImage (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getImage ();
	if (images != null) {
		if (0 <= index && index < images.length) return images [index];
	}
	return null;
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Rectangle getImageBounds (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	RECT rect = getBounds (index, false, true, false);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, handle);
	return hItem != 0 ? parent._getItem (hItem) : null;
}

public String getText () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getText ();
}

/**
 * Returns the text stored at the given column index in the receiver,
 * or empty string if the text has not been set.
 *
 * @param index the column index
 * @return the text stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public String getText (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getText ();
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of the text at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding text rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public Rectangle getTextBounds (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	RECT rect = getBounds (index, true, false, true);
	if (index == 0) rect.left += Tree.INSET - 1;
	rect.left = Math.min (rect.left, rect.right);
	rect.right = rect.right - Tree.INSET;
	int width = Math.max (0, rect.right - rect.left);
	int height = Math.max (0, rect.bottom - rect.top);
	return new Rectangle (rect.left, rect.top, width, height);
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int indexOf (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	return hItem == 0 ? -1 : parent.findIndex (hItem, item.handle);
}

void redraw () {
	if (parent.currentItem == this || !parent.getDrawing ()) return;
	int /*long*/ hwnd = parent.handle;
	if (!OS.IsWindowVisible (hwnd)) return;
	/*
	* When there are no columns and the tree is not
	* full selection, redraw only the text.  This is
	* an optimization to reduce flashing.
	*/
	boolean full = (parent.style & (SWT.FULL_SELECTION | SWT.VIRTUAL)) != 0;
	if (!full) {
		full = parent.columnCount != 0;
		if (!full) {
			if (parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
				full = true;
			}
		}
	}
	RECT rect = new RECT ();
	if (OS.TreeView_GetItemRect (hwnd, handle, rect, !full)) {
		OS.InvalidateRect (hwnd, rect, true);
	}
}

void redraw (int column, boolean drawText, boolean drawImage) {
	if (parent.currentItem == this || !parent.getDrawing ()) return;
	int /*long*/ hwnd = parent.handle;
	if (!OS.IsWindowVisible (hwnd)) return;
	boolean fullImage = column == 0 && drawText && drawImage;
	RECT rect = getBounds (column, drawText, drawImage, true, fullImage, true, 0);
	OS.InvalidateRect (hwnd, rect, true);
}

void releaseChildren (boolean destroy) {
	if (destroy) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		parent.releaseItems (handle, tvItem);
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	strings = null;
	images = null;
	cellBackground = cellForeground = null; 
	cellFont = null;
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void removeAll () {
	checkWidget ();
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	while (tvItem.hItem != 0) {
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		TreeItem item = tvItem.lParam != -1 ? parent.items [(int)/*64*/tvItem.lParam] : null;
		if (item != null && !item.isDisposed ()) {
			item.dispose ();
		} else {
			parent.releaseItem (tvItem.hItem, tvItem, false);
			parent.destroyItem (null, tvItem.hItem);
		}
		tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	}
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (background == pixel) return;
	background = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw ();
}

/**
 * Sets the background color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 * 
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (cellBackground == null) {
		cellBackground = new int [count];
		for (int i = 0; i < count; i++) {
			cellBackground [i] = -1;
		}
	}
	if (cellBackground [index] == pixel) return;
	cellBackground [index] = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw (index, true, true);
}

/**
 * Sets the checked state of the receiver.
 * <p>
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	int state = tvItem.state >> 12;
	if (checked) {
		if ((state & 0x1) != 0) state++;
	} else {
		if ((state & 0x1) == 0) --state;
	}
	state <<= 12;
	if (tvItem.state == state) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	tvItem.state = state;
	OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	/*
	* Bug in Windows.  When TVM_SETITEM is used to set
	* the state image of an item inside TVN_GETDISPINFO,
	* the new state is not redrawn.  The fix is to force
	* a redraw.
	*/
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (parent.currentItem == this && OS.IsWindowVisible (hwnd)) {
			RECT rect = new RECT ();
			if (OS.TreeView_GetItemRect (hwnd, handle, rect, false)) {
				OS.InvalidateRect (hwnd, rect, true);
			}
		}
	}
}

/**
 * Sets the expanded state of the receiver.
 * <p>
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
	
	/* Do nothing when the item is a leaf or already expanded */
	int /*long*/ hwnd = parent.handle;
	if (OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle) == 0) {
		return;
	}
	int state = 0;
	if (OS.IsWinCE) {
		TVITEM tvItem = new TVITEM ();
		tvItem.hItem = handle;
		tvItem.mask = OS.TVIF_STATE;
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		state = tvItem.state;
	} else {
		/*
		* Bug in Windows.  Despite the fact that TVM_GETITEMSTATE claims
		* to return only the bits specified by the stateMask, when called
		* with TVIS_EXPANDED, the entire state is returned.  The fix is
		* to explicitly check for the TVIS_EXPANDED bit.
		*/
		state = (int)/*64*/OS.SendMessage (hwnd, OS.TVM_GETITEMSTATE, handle, OS.TVIS_EXPANDED);
	}
	if (((state & OS.TVIS_EXPANDED) != 0) == expanded) return;
	
	/*
	* Feature in Windows.  When TVM_EXPAND is used to expand
	* an item, the widget scrolls to show the item and the
	* newly expanded items.  While not strictly incorrect,
	* this means that application code that expands tree items
	* in a background thread can scroll the widget while the
	* user is interacting with it.  The fix is to remember
	* the top item and the bounds of every tree item, turn
	* redraw off, expand the item, scroll back to the top
	* item.  If none of the rectangles have moved, then
	* it is safe to turn redraw back on without redrawing
	* the control.
	*/
	RECT oldRect = null;
	RECT [] rects = null;
	SCROLLINFO oldInfo = null;
	int count = 0;
 	int /*long*/ hBottomItem = 0;
	boolean redraw = false, noScroll = true;
	int /*long*/ hTopItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
	if (noScroll && hTopItem != 0) {
		oldInfo = new SCROLLINFO ();
		oldInfo.cbSize = SCROLLINFO.sizeof;
		oldInfo.fMask = OS.SIF_ALL;
		if (!OS.GetScrollInfo (hwnd, OS.SB_HORZ, oldInfo)) {
			oldInfo = null;
		}
		if (parent.getDrawing () && OS.IsWindowVisible (hwnd)) {
			boolean noAnimate = true;
			count = (int)/*64*/OS.SendMessage (hwnd, OS.TVM_GETVISIBLECOUNT, 0, 0);
			rects = new RECT [count + 1];
			int /*long*/ hItem = hTopItem;
			int index = 0;
			while (hItem != 0 && (noAnimate || hItem != handle) && index < count) {
				RECT rect = new RECT ();
				if (OS.TreeView_GetItemRect (hwnd, hItem, rect, true)) {
					rects [index++] = rect;
				}
				hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
			}
			if (noAnimate || hItem != handle) {
				redraw = true;
				count = index;
				hBottomItem = hItem;
				oldRect = new RECT ();
				OS.GetClientRect (hwnd, oldRect);
				int /*long*/ topHandle = parent.topHandle ();
				OS.UpdateWindow (topHandle);
				OS.DefWindowProc (topHandle, OS.WM_SETREDRAW, 0, 0);
				if (hwnd != topHandle) {
					OS.UpdateWindow (hwnd);
					OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
				}
				/*
				* This code is intentionally commented.
				*/
//				OS.SendMessage (hwnd, OS.WM_SETREDRAW, 0, 0);
			}
		}
	}
	
	/*
	* Feature in Windows.  When the user collapses the root
	* of a subtree that has the focus item, Windows moves
	* the selection to the root of the subtree and issues
	* a TVN_SELCHANGED to inform the programmer that the
	* selection has changed.  When the programmer collapses
	* the same subtree using TVM_EXPAND, Windows does not
	* send the selection changed notification.  This is not
	* strictly wrong but is inconsistent.  The fix is to
	* check whether the selection has changed and issue
	* the event.
	*/
	int /*long*/ hOldItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	
	/* Expand or collapse the item */
	parent.ignoreExpand = true;
	OS.SendMessage (hwnd, OS.TVM_EXPAND, expanded ? OS.TVE_EXPAND : OS.TVE_COLLAPSE, handle);
	parent.ignoreExpand = false;
	
	/* Scroll back to the top item */
	if (noScroll && hTopItem != 0) {
		boolean collapsed = false;
		if (!expanded) {
			RECT rect = new RECT ();
			while (hTopItem != 0 && !OS.TreeView_GetItemRect (hwnd, hTopItem, rect, false)) {
				hTopItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hTopItem);
				collapsed = true;
			}
		}
		boolean scrolled = true;
		if (hTopItem != 0) {
			OS.SendMessage (hwnd, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hTopItem);
			scrolled = hTopItem != OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
		}
		if (!collapsed && !scrolled && oldInfo != null) {
			SCROLLINFO newInfo = new SCROLLINFO ();
			newInfo.cbSize = SCROLLINFO.sizeof;
			newInfo.fMask = OS.SIF_ALL;
			if (OS.GetScrollInfo (hwnd, OS.SB_HORZ, newInfo)) {
				if (oldInfo.nPos != newInfo.nPos) {
					int /*long*/ lParam = OS.MAKELPARAM (OS.SB_THUMBPOSITION, oldInfo.nPos);
					OS.SendMessage (hwnd, OS.WM_HSCROLL, lParam, 0);
				}
			}
		}
		if (redraw) {
			boolean fixScroll = false;
			if (!collapsed && !scrolled) {
				RECT newRect = new RECT ();
				OS.GetClientRect (hwnd, newRect);
				if (OS.EqualRect (oldRect, newRect)) {
					int /*long*/ hItem = hTopItem;
					int index = 0;
					while (hItem != 0 && index < count) {
						RECT rect = new RECT ();
						if (OS.TreeView_GetItemRect (hwnd, hItem, rect, true)) {
							if (!OS.EqualRect (rect, rects [index])) {
								break;
							}
						}
						hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
						index++;
					}
					fixScroll = index == count && hItem == hBottomItem;
				}
			}
			int /*long*/ topHandle = parent.topHandle ();
			OS.DefWindowProc (topHandle, OS.WM_SETREDRAW, 1, 0);
			if (hwnd != topHandle) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
			}
			/*
			* This code is intentionally commented.
			*/
//			OS.SendMessage (hwnd, OS.WM_SETREDRAW, 1, 0);
			if (fixScroll) {
				parent.updateScrollBar ();
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_ALL;
				if (OS.GetScrollInfo (hwnd, OS.SB_VERT, info)) {
					OS.SetScrollInfo (hwnd, OS.SB_VERT, info, true);
				}
				if (handle == hBottomItem) {
					RECT rect = new RECT ();
					if (OS.TreeView_GetItemRect (hwnd, hBottomItem, rect, false)) {
						OS.InvalidateRect (hwnd, rect, true);
					}
				}
			} else {
				if (OS.IsWinCE) {
					OS.InvalidateRect (topHandle, null, true);
					if (hwnd != topHandle) OS.InvalidateRect (hwnd, null, true);
				} else {
					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
					OS.RedrawWindow (topHandle, null, 0, flags);
				}
			}
		}
	}

	/* Check for a selection event */
	int /*long*/ hNewItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if (hNewItem != hOldItem) {
		Event event = new Event ();
		if (hNewItem != 0) {
			event.item = parent._getItem (hNewItem);
			parent.hAnchor = hNewItem;
		}
		parent.sendEvent (SWT.Selection, event);
	}
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
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
 * 
 * @since 3.0
 */
public void setFont (Font font){
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	if (font != null) parent.customDraw = true;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	/*
	* Bug in Windows.  When the font is changed for an item,
	* the bounds for the item are not updated, causing the text
	* to be clipped.  The fix is to reset the text, causing
	* Windows to compute the new bounds using the new font.
	*/
	if ((parent.style & SWT.VIRTUAL) == 0 && !cached && !parent.painted) {
		return;
	}
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
	tvItem.hItem = handle;
	tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
	OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
}


/**
 * Sets the font that the receiver will use to paint textual information
 * for the specified cell in this item to the font specified by the 
 * argument, or to the default font for that kind of control if the 
 * argument is null.
 *
 * @param index the column index
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setFont (int index, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellFont == null) {
		if (font == null) return;
		cellFont = new Font [count];
	}
	Font oldFont = cellFont [index];
	if (oldFont == font) return;
	cellFont [index] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	if (font != null) parent.customDraw = true;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	/*
	* Bug in Windows.  When the font is changed for an item,
	* the bounds for the item are not updated, causing the text
	* to be clipped.  The fix is to reset the text, causing
	* Windows to compute the new bounds using the new font.
	*/
	if (index == 0) {
		if ((parent.style & SWT.VIRTUAL) == 0 && !cached && !parent.painted) {
			return;
		}
		int /*long*/ hwnd = parent.handle;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
		tvItem.hItem = handle;
		tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
		OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	} else {
		redraw (index, true, false);
	}
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (foreground == pixel) return;
	foreground = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw ();
}

/**
 * Sets the foreground color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 * 
 */
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (cellForeground == null) {
		cellForeground = new int [count];
		for (int i = 0; i < count; i++) {
			cellForeground [i] = -1;
		}
	}
	if (cellForeground [index] == pixel) return;
	cellForeground [index] = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw (index, true, false);
}

/**
 * Sets the grayed state of the checkbox for this item.  This state change 
 * only applies if the Tree was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	int /*long*/ hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	int state = tvItem.state >> 12;
	if (grayed) {
		if (state <= 2) state +=2;
	} else {
		if (state > 2) state -=2;
	}
	state <<= 12;
	if (tvItem.state == state) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	tvItem.state = state;
	OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	/*
	* Bug in Windows.  When TVM_SETITEM is used to set
	* the state image of an item inside TVN_GETDISPINFO,
	* the new state is not redrawn.  The fix is to force
	* a redraw.
	*/
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (parent.currentItem == this && OS.IsWindowVisible (hwnd)) {
			RECT rect = new RECT ();
			if (OS.TreeView_GetItemRect (hwnd, handle, rect, false)) {
				OS.InvalidateRect (hwnd, rect, true);
			}
		}
	}
}

/**
 * Sets the image for multiple columns in the tree. 
 * 
 * @param images the array of new images
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setImage (Image [] images) {
	checkWidget();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
}

/**
 * Sets the receiver's image at a column.
 *
 * @param index the column index
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setImage (int index, Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	Image oldImage = null;
	if (index == 0) {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (this.image)) return;
		}
		oldImage = this.image;
		super.setImage (image);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (images == null && index != 0) {
		images = new Image [count];
		images [0] = image;
	}
	if (images != null) {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (images [index])) return;
		}
		oldImage = images [index];
		images [index] = image;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	
	/* Ensure that the image list is created */
	//TODO - items that are not in column zero don't need to be in the image list
	parent.imageIndex (image, index);
	
	if (index == 0) {
		if ((parent.style & SWT.VIRTUAL) == 0 &&!cached && !parent.painted) {
			return;
		}
		int /*long*/ hwnd = parent.handle;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE;
		tvItem.hItem = handle;
		tvItem.iImage = tvItem.iSelectedImage = OS.I_IMAGECALLBACK;
		/*
		* Bug in Windows.  When I_IMAGECALLBACK is used with TVM_SETITEM
		* to indicate that an image has changed, Windows does not draw
		* the new image.  The fix is to use LPSTR_TEXTCALLBACK to force
		* Windows to ask for the text, causing Windows to ask for both.
		*/
		tvItem.mask |= OS.TVIF_TEXT;
		tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
		OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	} else {
		boolean drawText = (image == null && oldImage != null) || (image != null && oldImage == null);
		redraw (index, drawText, true);
	}
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the number of child items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	int /*long*/ hwnd = parent.handle;
	int /*long*/ hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	parent.setItemCount (count, handle, hItem);
}

/**
 * Sets the text for multiple columns in the tree. 
 * 
 * @param strings the array of new strings
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setText (String [] strings) {
	checkWidget();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
}

/**
 * Sets the receiver's text at a column
 *
 * @param index the column index
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setText (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == 0) {
		if (string.equals (text)) return;
		super.setText (string);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (strings == null && index != 0) {
		strings = new String [count];
		strings [0] = text;
	}
	if (strings != null) {
		if (string.equals (strings [index])) return;
		strings [index] = string;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (index == 0) {
		if ((parent.style & SWT.VIRTUAL) == 0 && !cached && !parent.painted) {
			return;
		}
		int /*long*/ hwnd = parent.handle;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
		tvItem.hItem = handle;
		tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
		OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	} else {
		redraw (index, true, false);
	}
}

public void setText (String string) {
	checkWidget();
	setText (0, string);
}

/*public*/ void sort () {
	checkWidget ();
	if ((parent.style & SWT.VIRTUAL) != 0) return;
	parent.sort (handle, false);
}

}
