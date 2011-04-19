/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issues notification when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * trees that are very large or for which <code>TreeItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:
 * <code><pre>
 *  final Tree tree = new Tree(parent, SWT.VIRTUAL | SWT.BORDER);
 *  tree.setItemCount(20);
 *  tree.addListener(SWT.SetData, new Listener() {
 *      public void handleEvent(Event event) {
 *          TreeItem item = (TreeItem)event.item;
 *          TreeItem parentItem = item.getParentItem();
 *          String text = null;
 *          if (parentItem == null) {
 *              text = "node " + tree.indexOf(item);
 *          } else {
 *              text = parentItem.getText() + " - " + parentItem.indexOf(item);
 *          }
 *          item.setText(text);
 *          System.out.println(text);
 *          item.setItemCount(10);
 *      }
 *  });
 * </pre></code>
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends Composite {
	TreeItem [] items;
	TreeColumn [] columns;
	int columnCount;
	ImageList imageList, headerImageList;
	TreeItem currentItem;
	TreeColumn sortColumn;
	RECT focusRect;
	int /*long*/ hwndParent, hwndHeader, hAnchor, hInsert, hSelect;
	int lastID;
	int /*long*/ hFirstIndexOf, hLastIndexOf;
	int lastIndexOf, itemCount, sortDirection;
	boolean dragStarted, gestureCompleted, insertAfter, shrink, ignoreShrink;
	boolean ignoreSelect, ignoreExpand, ignoreDeselect, ignoreResize;
	boolean lockSelection, oldSelected, newSelected, ignoreColumnMove;
	boolean linesVisible, customDraw, printClient, painted, ignoreItemHeight;
	boolean ignoreCustomDraw, ignoreDrawForeground, ignoreDrawBackground, ignoreDrawFocus;
	boolean ignoreDrawSelection, ignoreDrawHot, ignoreFullSelection, explorerTheme;
	boolean createdAsRTL;
	int scrollWidth, selectionForeground;
	int /*long*/ headerToolTipHandle, itemToolTipHandle;
	int /*long*/ lastTimerID = -1;
	int lastTimerCount;
	static final int TIMER_MAX_COUNT = 8;
	static final int INSET = 3;
	static final int GRID_WIDTH = 1;
	static final int SORT_WIDTH = 10;
	static final int HEADER_MARGIN = 12;
	static final int HEADER_EXTRA = 3;
	static final int INCREMENT = 5;
	static final int EXPLORER_EXTRA = 2;
	static final int DRAG_IMAGE_SIZE = 301;
	static final boolean EXPLORER_THEME = true;
	static final int /*long*/ TreeProc;
	static final TCHAR TreeClass = new TCHAR (0, OS.WC_TREEVIEW, true);
	static final int /*long*/ HeaderProc;
	static final TCHAR HeaderClass = new TCHAR (0, OS.WC_HEADER, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TreeClass, lpWndClass);
		TreeProc = lpWndClass.lpfnWndProc;
		OS.GetClassInfo (0, HeaderClass, lpWndClass);
		HeaderProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#CHECK
 * @see SWT#FULL_SELECTION
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  Even when WS_HSCROLL or
	* WS_VSCROLL is not specified, Windows creates
	* trees and tables with scroll bars.  The fix
	* is to set H_SCROLL and V_SCROLL.
	* 
	* NOTE: This code appears on all platforms so that
	* applications have consistent scroll bar behavior.
	*/
	if ((style & SWT.NO_SCROLL) == 0) {
		style |= SWT.H_SCROLL | SWT.V_SCROLL;
	}
	/*
	* Note: Windows only supports TVS_NOSCROLL and TVS_NOHSCROLL.
	*/
	if ((style & SWT.H_SCROLL) != 0 && (style & SWT.V_SCROLL) == 0) {
		style |= SWT.V_SCROLL;
	}
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	switch (eventType) {
		case SWT.DragDetect: {
			if ((state & DRAG_DETECT) != 0) {
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				bits &= ~OS.TVS_DISABLEDRAGDROP;
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
			}
			break;
		}
		case SWT.MeasureItem:
		case SWT.EraseItem:
		case SWT.PaintItem: {
			customDraw = true;
			style |= SWT.DOUBLE_BUFFERED;
			if (isCustomToolTip ()) createItemToolTips ();
			OS.SendMessage (handle, OS.TVM_SETSCROLLTIME, 0, 0);
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if (eventType == SWT.MeasureItem) {
				/*
				* This code is intentionally commented.
				*/
//				if (explorerTheme) {
//					int bits1 = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETEXTENDEDSTYLE, 0, 0);
//					bits1 &= ~OS.TVS_EX_AUTOHSCROLL;
//					OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, 0, bits1);
//				}
				bits |= OS.TVS_NOHSCROLL;
			}
			/*
			* Feature in Windows.  When the tree has the style
			* TVS_FULLROWSELECT, the background color for the
			* entire row is filled when an item is painted,
			* drawing on top of any custom drawing.  The fix
			* is to clear TVS_FULLROWSELECT.
			*/
			if ((style & SWT.FULL_SELECTION) != 0) {
				if (eventType != SWT.MeasureItem) {
					if (!explorerTheme) bits &= ~OS.TVS_FULLROWSELECT;
				}
			}
			if (bits != OS.GetWindowLong (handle, OS.GWL_STYLE)) {
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
				OS.InvalidateRect (handle, null, true);
				/*
				* Bug in Windows.  When TVS_NOHSCROLL is set after items
				* have been inserted into the tree, Windows shows the
				* scroll bar.  The fix is to check for this case and
				* explicitly hide the scroll bar.
				*/
				int count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
				if (count != 0 && (bits & OS.TVS_NOHSCROLL) != 0) {
					if (!OS.IsWinCE) OS.ShowScrollBar (handle, OS.SB_HORZ, false);
				}
			}
			break;
		}
	}
}

TreeItem _getItem (int /*long*/ hItem) {
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = hItem;
	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) != 0) {
		return _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
	}
	return null;
}

TreeItem _getItem (int /*long*/ hItem, int id) {
	if ((style & SWT.VIRTUAL) == 0) return items [id];
	return id != -1 ? items [id] : new TreeItem (this, SWT.NONE, -1, -1, hItem);
}

void _setBackgroundPixel (int newPixel) {
	int oldPixel = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (oldPixel != newPixel) {
		/*
		* Bug in Windows.  When TVM_SETBKCOLOR is used more
		* than once to set the background color of a tree,
		* the background color of the lines and the plus/minus
		* does not change to the new color.  The fix is to set
		* the background color to the default before setting
		* the new color.
		*/
		if (oldPixel != -1) {
			OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, -1);
		}

		/* Set the background color */
		OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, newPixel);
		
		/*
		* Feature in Windows.  When TVM_SETBKCOLOR is used to
		* set the background color of a tree, the plus/minus
		* animation draws badly.  The fix is to clear the effect.
		*/	
		if (explorerTheme) {
			int bits2 = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETEXTENDEDSTYLE, 0, 0);
			if (newPixel == -1 && findImageControl () == null) {
				bits2 |= OS.TVS_EX_FADEINOUTEXPANDOS;
			} else {
				bits2 &= ~OS.TVS_EX_FADEINOUTEXPANDOS;
			}
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, 0, bits2);
		}

		/* Set the checkbox image list */
		if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has the <code>SWT.CHECK</code> style and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
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
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
} 

int /*long*/ borderHandle () {
	return hwndParent != 0 ? hwndParent : handle;
}

LRESULT CDDS_ITEMPOSTPAINT (NMTVCUSTOMDRAW nmcd, int /*long*/ wParam, int /*long*/ lParam) {
	if (ignoreCustomDraw) return null;
	if (nmcd.left == nmcd.right) return new LRESULT (OS.CDRF_DODEFAULT);
	int /*long*/ hDC = nmcd.hdc;
	OS.RestoreDC (hDC, -1);
	TreeItem item = getItem (nmcd);
	
	/*
	* Feature in Windows.  When a new tree item is inserted
	* using TVM_INSERTITEM and the tree is using custom draw,
	* a NM_CUSTOMDRAW is sent before TVM_INSERTITEM returns
	* and before the item is added to the items array.  The
	* fix is to check for null.
	* 
	* NOTE: This only happens on XP with the version 6.00 of
	* COMCTL32.DLL,
	*/
	if (item == null) return null;

	/*
	* Feature in Windows.  Under certain circumstances, Windows
	* sends CDDS_ITEMPOSTPAINT for an empty rectangle.  This is
	* not a problem providing that graphics do not occur outside
	* the rectangle.  The fix is to test for the rectangle and
	* draw nothing.
	* 
	* NOTE:  This seems to happen when both I_IMAGECALLBACK
	* and LPSTR_TEXTCALLBACK are used at the same time with
	* TVM_SETITEM.
	*/
	if (nmcd.left >= nmcd.right || nmcd.top >= nmcd.bottom) return null;
	if (!OS.IsWindowVisible (handle)) return null;
	if ((style & SWT.FULL_SELECTION) != 0 || findImageControl () != null || ignoreDrawSelection || explorerTheme) {
		OS.SetBkMode (hDC, OS.TRANSPARENT);
	}
	boolean selected = isItemSelected (nmcd);
	boolean hot = explorerTheme && (nmcd.uItemState & OS.CDIS_HOT) != 0;
	if (OS.IsWindowEnabled (handle)) {
		if (explorerTheme) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.TVS_TRACKSELECT) != 0) {
				if ((style & SWT.FULL_SELECTION) != 0 && (selected || hot)) {
					OS.SetTextColor (hDC, OS.GetSysColor (OS.COLOR_WINDOWTEXT));
				} else {
					OS.SetTextColor (hDC, getForegroundPixel ());
				}
			}
		}
	}
	int [] order = null;
	RECT clientRect = new RECT ();
	OS.GetClientRect (scrolledHandle (), clientRect);
	if (hwndHeader != 0) {
		OS.MapWindowPoints (hwndParent, handle, clientRect, 2);
		if (columnCount != 0) {
			order = new int [columnCount];
			OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, order);
		}
	}
	int sortIndex = -1, clrSortBk = -1;
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		if (sortColumn != null && sortDirection != SWT.NONE) {
			if (findImageControl () == null) {
				sortIndex = indexOf (sortColumn);
				clrSortBk = getSortColumnPixel ();
			}
		}
	}
	int x = 0;
	Point size = null;
	for (int i=0; i<Math.max (1, columnCount); i++) {
		int index = order == null ? i : order [i], width = nmcd.right - nmcd.left;
		if (columnCount > 0 && hwndHeader != 0) {
			HDITEM hdItem = new HDITEM ();
			hdItem.mask = OS.HDI_WIDTH;
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
			width = hdItem.cxy;
		}
		if (i == 0) {
			if ((style & SWT.FULL_SELECTION) != 0) {
				boolean clear = !explorerTheme && !ignoreDrawSelection && findImageControl () == null;
				if (clear || (selected && !ignoreDrawSelection) || (hot && !ignoreDrawHot)) {
					boolean draw = true;
					RECT pClipRect = new RECT ();
					OS.SetRect (pClipRect, width, nmcd.top, nmcd.right, nmcd.bottom);
					if (explorerTheme) {
						if (hooks (SWT.EraseItem)) {
							RECT itemRect = item.getBounds (index, true, true, false, false, true, hDC);
							itemRect.left -= EXPLORER_EXTRA;
							itemRect.right += EXPLORER_EXTRA + 1;
							pClipRect.left = itemRect.left;
							pClipRect.right = itemRect.right;
							if (columnCount > 0 && hwndHeader != 0) {
								HDITEM hdItem = new HDITEM ();
								hdItem.mask = OS.HDI_WIDTH;
								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
								pClipRect.right = Math.min (pClipRect.right, nmcd.left + hdItem.cxy);
							}
						}
						RECT pRect = new RECT ();
						OS.SetRect (pRect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
						if (columnCount > 0 && hwndHeader != 0) {
							int totalWidth = 0;
							HDITEM hdItem = new HDITEM ();
							hdItem.mask = OS.HDI_WIDTH;
							for (int j=0; j<columnCount; j++) {
								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, j, hdItem);
								totalWidth += hdItem.cxy;
							}
							if (totalWidth > clientRect.right - clientRect.left) {
								pRect.left = 0;
								pRect.right = totalWidth;
							} else {
								pRect.left = clientRect.left;
								pRect.right = clientRect.right;
							}
						}
						draw = false;
						int /*long*/ hTheme = OS.OpenThemeData (handle, Display.TREEVIEW);
						int iStateId = selected ? OS.TREIS_SELECTED : OS.TREIS_HOT;
						if (OS.GetFocus () != handle && selected && !hot) iStateId = OS.TREIS_SELECTEDNOTFOCUS;
						OS.DrawThemeBackground (hTheme, hDC, OS.TVP_TREEITEM, iStateId, pRect, pClipRect);	
						OS.CloseThemeData (hTheme);
					}
					if (draw) fillBackground (hDC, OS.GetBkColor (hDC), pClipRect);
				}
			}
		}
		if (x + width > clientRect.left) {
			RECT rect = new RECT (), backgroundRect = null;
			boolean drawItem = true, drawText = true, drawImage = true, drawBackground = false;
			if (i == 0) {
				drawItem = drawImage = drawText = false;
				if (findImageControl () != null) {
					if (explorerTheme) {
						if (OS.IsWindowEnabled (handle) && !hooks (SWT.EraseItem)) {
							Image image = null;
							if (index == 0) {
								image = item.image;
							} else {
								Image [] images  = item.images;
								if (images != null) image = images [index];
							}
							if (image != null) {
								Rectangle bounds = image.getBounds ();
								if (size == null) size = getImageSize ();
								if (!ignoreDrawForeground) {
									GCData data = new GCData();
									data.device = display;
									GC gc = GC.win32_new (hDC, data);
									RECT iconRect = item.getBounds (index, false, true, false, false, true, hDC);
									gc.setClipping (iconRect.left, iconRect.top, iconRect.right - iconRect.left, iconRect.bottom - iconRect.top);
									gc.drawImage (image, 0, 0, bounds.width, bounds.height, iconRect.left, iconRect.top, size.x, size.y);
									OS.SelectClipRgn (hDC, 0);
									gc.dispose ();
								}
							}
						}
					} else {
						drawItem = drawText = drawBackground = true;
						rect = item.getBounds (index, true, false, false, false, true, hDC);
						if (linesVisible) {
							rect.right++;
							rect.bottom++;
						}
					}
				}
				if (selected && !ignoreDrawSelection && !ignoreDrawBackground) {
					if (!explorerTheme) fillBackground (hDC, OS.GetBkColor (hDC), rect);
					drawBackground = false;
				}
				backgroundRect = rect;
				if (hooks (SWT.EraseItem)) {
					drawItem = drawText = drawImage = true;
					rect = item.getBounds (index, true, true, false, false, true, hDC);
					if ((style & SWT.FULL_SELECTION) != 0) {
						backgroundRect = rect;
					} else {
						backgroundRect = item.getBounds (index, true, false, false, false, true, hDC);
					}
				}
			} else {
				selectionForeground = -1;
				ignoreDrawForeground = ignoreDrawBackground = ignoreDrawSelection = ignoreDrawFocus = ignoreDrawHot = false;
				OS.SetRect (rect, x, nmcd.top, x + width, nmcd.bottom);
				backgroundRect = rect;
			}
			int clrText = -1, clrTextBk = -1;
			int /*long*/ hFont = item.fontHandle (index);
			if (selectionForeground != -1) clrText = selectionForeground;
			if (OS.IsWindowEnabled (handle)) {
				boolean drawForeground = false;
				if (selected) {
					if (i != 0 && (style & SWT.FULL_SELECTION) == 0) {
						OS.SetTextColor (hDC, getForegroundPixel ());
						OS.SetBkColor (hDC, getBackgroundPixel ());
						drawForeground = drawBackground = true;
					}
				} else {
					drawForeground = drawBackground = true;
				}
				if (drawForeground) {
					clrText = item.cellForeground != null ? item.cellForeground [index] : -1;
					if (clrText == -1) clrText = item.foreground;
				}
				if (drawBackground) {
					clrTextBk = item.cellBackground != null ? item.cellBackground [index] : -1;
					if (clrTextBk == -1) clrTextBk = item.background;
					if (clrTextBk == -1 && index == sortIndex) clrTextBk = clrSortBk;
				}
			} else {
				if (clrTextBk == -1 && index == sortIndex) {
					drawBackground = true;
					clrTextBk = clrSortBk;
				}
			}
			if (explorerTheme) {
				if (selected || (nmcd.uItemState & OS.CDIS_HOT) != 0) {
					if ((style & SWT.FULL_SELECTION) != 0) {
						drawBackground = false;
					} else {
						if (i == 0) {
							drawBackground = false;
							if (!hooks (SWT.EraseItem)) drawText = false;
						}
					}
				}
			}
			if (drawItem) {
				if (i != 0) {
					if (hooks (SWT.MeasureItem)) {
						sendMeasureItemEvent (item, index, hDC, selected ? SWT.SELECTED : 0);
						if (isDisposed () || item.isDisposed ()) break;
					}
					if (hooks (SWT.EraseItem)) {
						RECT cellRect = item.getBounds (index, true, true, true, true, true, hDC);
						int nSavedDC = OS.SaveDC (hDC);
						GCData data = new GCData ();
						data.device = display;
						data.foreground = OS.GetTextColor (hDC);
						data.background = OS.GetBkColor (hDC);
						if (!selected || (style & SWT.FULL_SELECTION) == 0) {
							if (clrText != -1) data.foreground = clrText;
							if (clrTextBk != -1) data.background = clrTextBk;
						}
						data.font = item.getFont (index);
						data.uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
						GC gc = GC.win32_new (hDC, data);
						Event event = new Event ();
						event.item = item;
						event.index = index;
						event.gc = gc;
						event.detail |= SWT.FOREGROUND;
						if (clrTextBk != -1) event.detail |= SWT.BACKGROUND;
						if ((style & SWT.FULL_SELECTION) != 0) {
							if (hot) event.detail |= SWT.HOT;
							if (selected) event.detail |= SWT.SELECTED;
							if (!explorerTheme) {
								//if ((nmcd.uItemState & OS.CDIS_FOCUS) != 0) {
								if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == nmcd.dwItemSpec) {
									if (handle == OS.GetFocus ()) {
										int uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
										if ((uiState & OS.UISF_HIDEFOCUS) == 0) event.detail |= SWT.FOCUSED;
									}
								}
							}
						}
						event.x = cellRect.left;
						event.y = cellRect.top;
						event.width = cellRect.right - cellRect.left;
						event.height = cellRect.bottom - cellRect.top;
						gc.setClipping (event.x, event.y, event.width, event.height);
						sendEvent (SWT.EraseItem, event);
						event.gc = null;
						int newTextClr = data.foreground;
						gc.dispose ();
						OS.RestoreDC (hDC, nSavedDC);
						if (isDisposed () || item.isDisposed ()) break;
						if (event.doit) {
							ignoreDrawForeground = (event.detail & SWT.FOREGROUND) == 0;
							ignoreDrawBackground = (event.detail & SWT.BACKGROUND) == 0;
							if ((style & SWT.FULL_SELECTION) != 0) {
								ignoreDrawSelection = (event.detail & SWT.SELECTED) == 0;
								ignoreDrawFocus = (event.detail & SWT.FOCUSED) == 0;
								ignoreDrawHot = (event.detail & SWT.HOT) == 0;
							}
						} else {
							ignoreDrawForeground = ignoreDrawBackground = ignoreDrawSelection = ignoreDrawFocus = ignoreDrawHot = true;
						}
						if (selected && ignoreDrawSelection) ignoreDrawHot = true;
						if ((style & SWT.FULL_SELECTION) != 0) {
							if (ignoreDrawSelection) ignoreFullSelection = true;
							if (!ignoreDrawSelection || !ignoreDrawHot) {
								if (!selected && !hot) {
									selectionForeground = OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT);
								} else {
									if (!explorerTheme) {
										drawBackground = true;
										ignoreDrawBackground = false;
										if ((handle == OS.GetFocus () || display.getHighContrast ()) && OS.IsWindowEnabled (handle)) {
											clrTextBk = OS.GetSysColor (OS.COLOR_HIGHLIGHT);
										} else {
											clrTextBk = OS.GetSysColor (OS.COLOR_3DFACE);
										}
										if (!ignoreFullSelection && index == columnCount - 1) {
											RECT selectionRect = new RECT ();
											OS.SetRect (selectionRect, backgroundRect.left, backgroundRect.top, nmcd.right, backgroundRect.bottom);
											backgroundRect = selectionRect;
										}
									} else {
										RECT pRect = new RECT ();
										OS.SetRect (pRect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
										if (columnCount > 0 && hwndHeader != 0) {
											int totalWidth = 0;
											HDITEM hdItem = new HDITEM ();
											hdItem.mask = OS.HDI_WIDTH;
											for (int j=0; j<columnCount; j++) {
												OS.SendMessage (hwndHeader, OS.HDM_GETITEM, j, hdItem);
												totalWidth += hdItem.cxy;
											}
											if (totalWidth > clientRect.right - clientRect.left) {
												pRect.left = 0;
												pRect.right = totalWidth;
											} else {
												pRect.left = clientRect.left;
												pRect.right = clientRect.right;
											}
											if (index == columnCount - 1) {
												RECT selectionRect = new RECT ();
												OS.SetRect (selectionRect, backgroundRect.left, backgroundRect.top, pRect.right, backgroundRect.bottom);
												backgroundRect = selectionRect;
											}
										}
										int /*long*/ hTheme = OS.OpenThemeData (handle, Display.TREEVIEW);
										int iStateId = selected ? OS.TREIS_SELECTED : OS.TREIS_HOT;
										if (OS.GetFocus () != handle && selected && !hot) iStateId = OS.TREIS_SELECTEDNOTFOCUS;
										OS.DrawThemeBackground (hTheme, hDC, OS.TVP_TREEITEM, iStateId, pRect, backgroundRect);	
										OS.CloseThemeData (hTheme);
									}
								}
							} else {
								if (selected) {
									selectionForeground = newTextClr;
									if (!explorerTheme) {
										if (clrTextBk == -1 && OS.IsWindowEnabled (handle)) {
											Control control = findBackgroundControl ();
											if (control == null) control = this;
											clrTextBk = control.getBackgroundPixel ();
										}
									}
								}
							}
						}
					}
					if (selectionForeground != -1) clrText = selectionForeground;
				}
				if (!ignoreDrawBackground) {
					if (clrTextBk != -1) {
						if (drawBackground) fillBackground (hDC, clrTextBk, backgroundRect);
					} else {
						Control control = findImageControl ();
						if (control != null) {
							if (i == 0) {
								int right = Math.min (rect.right, width);
								OS.SetRect (rect, rect.left, rect.top, right, rect.bottom);
								if (drawBackground) fillImageBackground (hDC, control, rect, 0, 0);
							} else {
								if (drawBackground) fillImageBackground (hDC, control, rect, 0, 0);
							}
						}
					}
				}
				rect.left += INSET - 1;
				if (drawImage) {
					Image image = null;
					if (index == 0) {
						image = item.image;
					} else {
						Image [] images  = item.images;
						if (images != null) image = images [index];
					}
					int inset = i != 0 ? INSET : 0;
					int offset = i != 0 ? INSET : INSET + 2;
					if (image != null) {
						Rectangle bounds = image.getBounds ();
						if (size == null) size = getImageSize ();
						if (!ignoreDrawForeground) {
							//int y1 = rect.top + (index == 0 ? (getItemHeight () - size.y) / 2 : 0);
							int y1 = rect.top;
							int x1 = Math.max (rect.left, rect.left - inset + 1);
							GCData data = new GCData();
							data.device = display;
							GC gc = GC.win32_new (hDC, data);
							gc.setClipping (x1, rect.top, rect.right - x1, rect.bottom - rect.top);
							gc.drawImage (image, 0, 0, bounds.width, bounds.height, x1, y1, size.x, size.y);
							OS.SelectClipRgn (hDC, 0);
							gc.dispose ();
						}
						OS.SetRect (rect, rect.left + size.x + offset, rect.top, rect.right - inset, rect.bottom);
					} else {
						if (i == 0) {
							if (OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0) != 0) {
								if (size == null) size = getImageSize ();
								rect.left = Math.min (rect.left + size.x + offset, rect.right);
							}
						} else {
							OS.SetRect (rect, rect.left + offset, rect.top, rect.right - inset, rect.bottom);
						}
					}
				}
				if (drawText) {
					/*
					* Bug in Windows.  When DrawText() is used with DT_VCENTER
					* and DT_ENDELLIPSIS, the ellipsis can draw outside of the
					* rectangle when the rectangle is empty.  The fix is avoid
					* all text drawing for empty rectangles.
					*/
					if (rect.left < rect.right) {
						String string = null;
						if (index == 0) {
							string = item.text;
						} else {
							String [] strings  = item.strings;
							if (strings != null) string = strings [index];
						}
						if (string != null) {
							if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
							if (clrText != -1) clrText = OS.SetTextColor (hDC, clrText);
							if (clrTextBk != -1) clrTextBk = OS.SetBkColor (hDC, clrTextBk);
							int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_VCENTER;
							if (i != 0) flags |= OS.DT_ENDELLIPSIS;
							TreeColumn column = columns != null ? columns [index] : null;
							if (column != null) {
								if ((column.style & SWT.CENTER) != 0) flags |= OS.DT_CENTER;
								if ((column.style & SWT.RIGHT) != 0) flags |= OS.DT_RIGHT;
							}
							TCHAR buffer = new TCHAR (getCodePage (), string, false);
							if (!ignoreDrawForeground) OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
							OS.DrawText (hDC, buffer, buffer.length (), rect, flags | OS.DT_CALCRECT);
							if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
							if (clrText != -1) clrText = OS.SetTextColor (hDC, clrText);
							if (clrTextBk != -1) clrTextBk = OS.SetBkColor (hDC, clrTextBk);
						}
					}
				}
			}
			if (selectionForeground != -1) clrText = selectionForeground;
			if (hooks (SWT.PaintItem)) {
				RECT itemRect = item.getBounds (index, true, true, false, false, false, hDC);
				int nSavedDC = OS.SaveDC (hDC);
				GCData data = new GCData ();
				data.device = display;
				data.font = item.getFont (index);
				data.foreground = OS.GetTextColor (hDC);
				data.background = OS.GetBkColor (hDC);
				if (selected && (style & SWT.FULL_SELECTION) != 0) {
					if (selectionForeground != -1) data.foreground = selectionForeground;
				} else {
					if (clrText != -1) data.foreground = clrText;
					if (clrTextBk != -1) data.background = clrTextBk;
				}
				data.uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
				GC gc = GC.win32_new (hDC, data);
				Event event = new Event ();
				event.item = item;
				event.index = index;
				event.gc = gc;
				event.detail |= SWT.FOREGROUND;
				if (clrTextBk != -1) event.detail |= SWT.BACKGROUND;
				if (hot) event.detail |= SWT.HOT;
				if (selected && (i == 0 /*nmcd.iSubItem == 0*/ || (style & SWT.FULL_SELECTION) != 0)) {
					event.detail |= SWT.SELECTED;
				}
				if (!explorerTheme) {
					//if ((nmcd.uItemState & OS.CDIS_FOCUS) != 0) {
					if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == nmcd.dwItemSpec) {
						if (i == 0 /*nmcd.iSubItem == 0*/ || (style & SWT.FULL_SELECTION) != 0) {
							if (handle == OS.GetFocus ()) {
								int uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
								if ((uiState & OS.UISF_HIDEFOCUS) == 0) event.detail |= SWT.FOCUSED;
							}
						}
					}
				}				
				event.x = itemRect.left;
				event.y = itemRect.top;
				event.width = itemRect.right - itemRect.left;
				event.height = itemRect.bottom - itemRect.top;
				RECT cellRect = item.getBounds (index, true, true, true, true, true, hDC);
				int cellWidth = cellRect.right - cellRect.left;
				int cellHeight = cellRect.bottom - cellRect.top;
				gc.setClipping (cellRect.left, cellRect.top, cellWidth, cellHeight);
				sendEvent (SWT.PaintItem, event);
				if (data.focusDrawn) focusRect = null;
				event.gc = null;
				gc.dispose ();
				OS.RestoreDC (hDC, nSavedDC);
				if (isDisposed () || item.isDisposed ()) break;
			}
		}
		x += width;
		if (x > clientRect.right) break;
	}
	if (linesVisible) {
		if ((style & SWT.FULL_SELECTION) != 0) {
			if (columnCount != 0) {
				HDITEM hdItem = new HDITEM ();
				hdItem.mask = OS.HDI_WIDTH;
				OS.SendMessage (hwndHeader, OS.HDM_GETITEM, 0, hdItem);
				RECT rect = new RECT ();
				OS.SetRect (rect, nmcd.left + hdItem.cxy, nmcd.top, nmcd.right, nmcd.bottom);
				OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
			}
		}
		RECT rect = new RECT ();
		OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
		OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
	}
	if (!ignoreDrawFocus && focusRect != null) {
		OS.DrawFocusRect (hDC, focusRect);
		focusRect = null;
	} else {
		if (!explorerTheme) {
			if (handle == OS.GetFocus ()) {
				int uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
				if ((uiState & OS.UISF_HIDEFOCUS) == 0) {
					int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
					if (hItem == item.handle) {
						if (!ignoreDrawFocus && findImageControl () != null) {
							if ((style & SWT.FULL_SELECTION) != 0) {
								RECT focusRect = new RECT ();
								OS.SetRect (focusRect, 0, nmcd.top, clientRect.right + 1, nmcd.bottom);
								OS.DrawFocusRect (hDC, focusRect);
							} else {
								int index = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0);
								RECT focusRect = item.getBounds (index, true, false, false, false, false, hDC);
								RECT clipRect = item.getBounds (index, true, false, false, false, true, hDC);
								OS.IntersectClipRect (hDC, clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
								OS.DrawFocusRect (hDC, focusRect);
								OS.SelectClipRgn (hDC, 0);
							}
						}
					}
				}
			}
		}
	}
	return new LRESULT (OS.CDRF_DODEFAULT);
}

LRESULT CDDS_ITEMPREPAINT (NMTVCUSTOMDRAW nmcd, int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Even when custom draw is being ignored, the font needs
	* to be selected into the HDC so that the item bounds are
	* measured correctly.
	*/
	TreeItem item = getItem (nmcd);
	/*
	* Feature in Windows.  When a new tree item is inserted
	* using TVM_INSERTITEM and the tree is using custom draw,
	* a NM_CUSTOMDRAW is sent before TVM_INSERTITEM returns
	* and before the item is added to the items array.  The
	* fix is to check for null.
	* 
	* NOTE: This only happens on XP with the version 6.00 of
	* COMCTL32.DLL,
	*/
	if (item == null) return null;
	int /*long*/ hDC = nmcd.hdc;
	int index = hwndHeader != 0 ? (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0) : 0;
	int /*long*/ hFont = item.fontHandle (index);
	if (hFont != -1) OS.SelectObject (hDC, hFont);
	if (ignoreCustomDraw || nmcd.left == nmcd.right) {
		return new LRESULT (hFont == -1 ? OS.CDRF_DODEFAULT : OS.CDRF_NEWFONT);
	}
	RECT clipRect = null;
	if (columnCount != 0) {
		boolean clip = !printClient;
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
			clip = true;
		}
		if (clip) {
			clipRect = new RECT ();
			HDITEM hdItem = new HDITEM ();
			hdItem.mask = OS.HDI_WIDTH;
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
			OS.SetRect (clipRect, nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
		}
	}
	int clrText = -1, clrTextBk = -1;
	if (OS.IsWindowEnabled (handle)) {
		clrText = item.cellForeground != null ? item.cellForeground [index] : -1;
		if (clrText == -1) clrText = item.foreground;
		clrTextBk = item.cellBackground != null ? item.cellBackground [index] : -1;
		if (clrTextBk == -1) clrTextBk = item.background;
	}
	int clrSortBk = -1;
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		if (sortColumn != null && sortDirection != SWT.NONE) {
			if (findImageControl () == null) {
				if (indexOf (sortColumn) == index) {
					clrSortBk = getSortColumnPixel ();
					if (clrTextBk == -1) clrTextBk = clrSortBk; 
				}
			}
		}
	}
	boolean selected = isItemSelected (nmcd);
	boolean hot = explorerTheme && (nmcd.uItemState & OS.CDIS_HOT) != 0;
	boolean focused = explorerTheme && (nmcd.uItemState & OS.CDIS_FOCUS) != 0;
	if (OS.IsWindowVisible (handle) && nmcd.left < nmcd.right && nmcd.top < nmcd.bottom) {
		if (hFont != -1) OS.SelectObject (hDC, hFont);
		if (linesVisible) {
			RECT rect = new RECT ();
			OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
			OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
		}
		//TODO - BUG - measure and erase sent when first column is clipped
		Event measureEvent = null;
		if (hooks (SWT.MeasureItem)) {
			measureEvent = sendMeasureItemEvent (item, index, hDC, selected ? SWT.SELECTED : 0);
			if (isDisposed () || item.isDisposed ()) return null;
		}
		selectionForeground = -1;
		ignoreDrawForeground = ignoreDrawBackground = ignoreDrawSelection = ignoreDrawFocus = ignoreDrawHot = ignoreFullSelection = false;
		if (hooks (SWT.EraseItem)) {
			RECT rect = new RECT ();
			OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
			RECT cellRect = item.getBounds (index, true, true, true, true, true, hDC);
			if (clrSortBk != -1) {
				drawBackground (hDC, cellRect, clrSortBk, 0, 0);
			} else {
				if (OS.IsWindowEnabled (handle) || findImageControl () != null) {
					drawBackground (hDC, rect);
				} else {
					fillBackground (hDC, OS.GetBkColor (hDC), rect);
				}
			}
			int nSavedDC = OS.SaveDC (hDC);
			GCData data = new GCData ();
			data.device = display;
			if (selected && explorerTheme) {
				data.foreground = OS.GetSysColor (OS.COLOR_WINDOWTEXT);
			} else {
				data.foreground = OS.GetTextColor (hDC);
			}
			data.background = OS.GetBkColor (hDC);
			if (!selected) {
				if (clrText != -1) data.foreground = clrText;
				if (clrTextBk != -1) data.background = clrTextBk;
			}
			data.uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
			data.font = item.getFont (index);
			GC gc = GC.win32_new (hDC, data);
			Event event = new Event ();
			event.index = index;
			event.item = item;
			event.gc = gc;
			event.detail |= SWT.FOREGROUND;
			if (clrTextBk != -1) event.detail |= SWT.BACKGROUND;
			if (hot) event.detail |= SWT.HOT;
			if (selected) event.detail |= SWT.SELECTED;
			//if ((nmcd.uItemState & OS.CDIS_FOCUS) != 0) {
			if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0) == nmcd.dwItemSpec) {
				if (handle == OS.GetFocus ()) {
					int uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
					if ((uiState & OS.UISF_HIDEFOCUS) == 0) {
						if (!explorerTheme || !selected) {
							focused = true;
							event.detail |= SWT.FOCUSED;
						}
					}
				}
			}
			event.x = cellRect.left;
			event.y = cellRect.top;
			event.width = cellRect.right - cellRect.left;
			event.height = cellRect.bottom - cellRect.top;
			gc.setClipping (event.x, event.y, event.width, event.height);
			sendEvent (SWT.EraseItem, event);
			event.gc = null;
			int newTextClr = data.foreground;
			gc.dispose ();
			OS.RestoreDC (hDC, nSavedDC);
			if (isDisposed () || item.isDisposed ()) return null;
			if (event.doit) {
				ignoreDrawForeground = (event.detail & SWT.FOREGROUND) == 0;
				ignoreDrawBackground = (event.detail & SWT.BACKGROUND) == 0;
				ignoreDrawSelection = (event.detail & SWT.SELECTED) == 0;
				ignoreDrawFocus = (event.detail & SWT.FOCUSED) == 0;
				ignoreDrawHot = (event.detail & SWT.HOT) == 0;
			} else {
				ignoreDrawForeground = ignoreDrawBackground = ignoreDrawSelection = ignoreDrawFocus = ignoreDrawHot = true;
			}
			if (selected && ignoreDrawSelection) ignoreDrawHot = true;
			if (!ignoreDrawBackground && clrTextBk != -1) {
				boolean draw = !selected && !hot;
				if (!explorerTheme && selected) draw = !ignoreDrawSelection;
				if (draw) {
					if (columnCount == 0) {
						if ((style & SWT.FULL_SELECTION) != 0) {
							fillBackground (hDC, clrTextBk, rect);
						} else {
							RECT textRect = item.getBounds (index, true, false, false, false, true, hDC);
							if (measureEvent != null) {
								textRect.right = Math.min (cellRect.right, measureEvent.x + measureEvent.width);
							}
							fillBackground (hDC, clrTextBk, textRect);
						}
					} else {
						fillBackground (hDC, clrTextBk, cellRect);
					}
				}
			}
			if (ignoreDrawSelection) ignoreFullSelection = true;
			if (!ignoreDrawSelection || !ignoreDrawHot) {
				if (!selected && !hot) {
					selectionForeground = clrText = OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT);
				}
				if (explorerTheme) {
					if ((style & SWT.FULL_SELECTION) == 0) {
						RECT pRect = item.getBounds (index, true, true, false, false, false, hDC);
						RECT pClipRect = item.getBounds (index, true, true, true, false, true, hDC);
						if (measureEvent != null) {
							pRect.right = Math.min (pClipRect.right, measureEvent.x + measureEvent.width);
						} else {
							pRect.right += EXPLORER_EXTRA;
							pClipRect.right += EXPLORER_EXTRA;
						}
						pRect.left -= EXPLORER_EXTRA;
						pClipRect.left -= EXPLORER_EXTRA;
						int /*long*/ hTheme = OS.OpenThemeData (handle, Display.TREEVIEW);
						int iStateId = selected ? OS.TREIS_SELECTED : OS.TREIS_HOT;
						if (OS.GetFocus () != handle && selected && !hot) iStateId = OS.TREIS_SELECTEDNOTFOCUS;
						OS.DrawThemeBackground (hTheme, hDC, OS.TVP_TREEITEM, iStateId, pRect, pClipRect);	
						OS.CloseThemeData (hTheme);
					}
				} else {
					/*
					* Feature in Windows.  When the tree has the style
					* TVS_FULLROWSELECT, the background color for the
					* entire row is filled when an item is painted,
					* drawing on top of any custom drawing.  The fix
					* is to emulate TVS_FULLROWSELECT.
					*/
					if ((style & SWT.FULL_SELECTION) != 0) {
						if ((style & SWT.FULL_SELECTION) != 0 && columnCount == 0) {
							fillBackground (hDC, OS.GetBkColor (hDC), rect);
						} else {
							fillBackground (hDC, OS.GetBkColor (hDC), cellRect);
						}
					} else {
						RECT textRect = item.getBounds (index, true, false, false, false, true, hDC);
						if (measureEvent != null) {
							textRect.right = Math.min (cellRect.right, measureEvent.x + measureEvent.width);
						}
						fillBackground (hDC, OS.GetBkColor (hDC), textRect);
					}
				}
			} else {
				if (selected || hot) {
					selectionForeground = clrText = newTextClr;
					ignoreDrawSelection = ignoreDrawHot = true;
				}
				if (explorerTheme) {
					nmcd.uItemState |= OS.CDIS_DISABLED;
					/*
					* Feature in Windows.  On Vista only, when the text
					* color is unchanged and an item is asked to draw
					* disabled, it uses the disabled color.  The fix is
					* to modify the color so that is it no longer equal.
					*/
					int newColor = clrText == -1 ? getForegroundPixel () : clrText;
					if (nmcd.clrText == newColor) {
						nmcd.clrText |= 0x20000000;
						if (nmcd.clrText == newColor) nmcd.clrText &= ~0x20000000;
					} else {
						nmcd.clrText = newColor;
					}
					OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
				}
			}
			if (focused && !ignoreDrawFocus && (style & SWT.FULL_SELECTION) == 0) {
				RECT textRect = item.getBounds (index, true, explorerTheme, false, false, true, hDC);
				if (measureEvent != null) {
					textRect.right = Math.min (cellRect.right, measureEvent.x + measureEvent.width);
				}
				nmcd.uItemState &= ~OS.CDIS_FOCUS;
				OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
				focusRect = textRect;
			}
			if (explorerTheme) {
				if (selected || (hot && ignoreDrawHot)) nmcd.uItemState &= ~OS.CDIS_HOT;
				OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
			}
			RECT itemRect = item.getBounds (index, true, true, false, false, false, hDC);
			OS.SaveDC (hDC);
			OS.SelectClipRgn (hDC, 0);
			if (explorerTheme) {
				itemRect.left -= EXPLORER_EXTRA;
				itemRect.right += EXPLORER_EXTRA;
			}
			//TODO - bug in Windows selection or SWT itemRect
			/*if (selected)*/ itemRect.right++;
			if (linesVisible) itemRect.bottom++;
			if (clipRect != null) {
				OS.IntersectClipRect (hDC, clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
			}
			OS.ExcludeClipRect (hDC, itemRect.left, itemRect.top, itemRect.right, itemRect.bottom);
			return new LRESULT (OS.CDRF_DODEFAULT | OS.CDRF_NOTIFYPOSTPAINT);
		}
		/*
		* Feature in Windows.  When the tree has the style
		* TVS_FULLROWSELECT, the background color for the
		* entire row is filled when an item is painted,
		* drawing on top of any custom drawing.  The fix
		* is to emulate TVS_FULLROWSELECT.
		*/
		if ((style & SWT.FULL_SELECTION) != 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.TVS_FULLROWSELECT) == 0) {
				RECT rect = new RECT ();
				OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
				if (selected) {
					fillBackground (hDC, OS.GetBkColor (hDC), rect);
				} else {
					if (OS.IsWindowEnabled (handle)) drawBackground (hDC, rect);
				}
				nmcd.uItemState &= ~OS.CDIS_FOCUS;
				OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
			}
		}
	}
	LRESULT result = null;
	if (clrText == -1 && clrTextBk == -1 && hFont == -1) {
		result = new LRESULT (OS.CDRF_DODEFAULT | OS.CDRF_NOTIFYPOSTPAINT);	
	} else {
		result = new LRESULT (OS.CDRF_NEWFONT | OS.CDRF_NOTIFYPOSTPAINT);
		if (hFont != -1) OS.SelectObject (hDC, hFont);
		if (OS.IsWindowEnabled (handle) && OS.IsWindowVisible (handle)) {
			/*
			* Feature in Windows.  Windows does not fill the entire cell
			* with the background color when TVS_FULLROWSELECT is not set.
			* The fix is to fill the cell with the background color.
			*/
			if (clrTextBk != -1) {
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				if ((bits & OS.TVS_FULLROWSELECT) == 0) {
					if (columnCount != 0 && hwndHeader != 0) {
						RECT rect = new RECT ();
						HDITEM hdItem = new HDITEM ();
						hdItem.mask = OS.HDI_WIDTH;
						OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
						OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
						if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) {
							RECT itemRect = new RECT ();
							if (OS.TreeView_GetItemRect (handle, item.handle, itemRect, true)) {
								rect.left = Math.min (itemRect.left, rect.right);
							}
						}
						if ((style & SWT.FULL_SELECTION) != 0) {
							if (!selected) fillBackground (hDC, clrTextBk, rect);
						} else {
							fillBackground (hDC, clrTextBk, rect);
						}
					} else {
						if ((style & SWT.FULL_SELECTION) != 0) {
							RECT rect = new RECT ();
							OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
							if (!selected) fillBackground (hDC, clrTextBk, rect);
						}
					}
				}
			}
			if (!selected) {
				nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
				nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
				OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
			}
		}
	}
	if (OS.IsWindowEnabled (handle)) {
		/*
		* On Vista only, when an item is asked to draw disabled,
		* the background of the text is not filled with the
		* background color of the tree.  This is true for both
		* regular and full selection trees.  In order to draw a
		* background image, mark the item as disabled using
		* CDIS_DISABLED (when not selected) and set the text
		* to the regular text color to avoid drawing disabled.
		*/
		if (explorerTheme) {
			if (findImageControl () !=  null) {
				if (!selected && (nmcd.uItemState & (OS.CDIS_HOT | OS.CDIS_SELECTED)) == 0) {
					nmcd.uItemState |= OS.CDIS_DISABLED;
					/*
					* Feature in Windows.  On Vista only, when the text
					* color is unchanged and an item is asked to draw
					* disabled, it uses the disabled color.  The fix is
					* to modify the color so it is no longer equal.
					*/
					int newColor = clrText == -1 ? getForegroundPixel () : clrText;
					if (nmcd.clrText == newColor) {
						nmcd.clrText |= 0x20000000;
						if (nmcd.clrText == newColor) nmcd.clrText &= ~0x20000000;
					} else {
						nmcd.clrText = newColor;
					}
					OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
					if (clrTextBk != -1) {
						if ((style & SWT.FULL_SELECTION) != 0) {
							RECT rect = new RECT ();
							if (columnCount != 0) {
								HDITEM hdItem = new HDITEM ();
								hdItem.mask = OS.HDI_WIDTH;
								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
								OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
							} else {
								OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
							}
							fillBackground (hDC, clrTextBk, rect);
						} else {
							RECT textRect = item.getBounds (index, true, false, true, false, true, hDC);
							fillBackground (hDC, clrTextBk, textRect);
						}
					}
				}
			}
		}
	} else {
		/*
		* Feature in Windows.  When the tree is disabled, it draws
		* with a gray background over the sort column.  The fix is
		* to fill the background with the sort column color.
		*/
		if (clrSortBk != -1) {
			RECT rect = new RECT ();
			HDITEM hdItem = new HDITEM ();
			hdItem.mask = OS.HDI_WIDTH;
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
			OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
			fillBackground (hDC, clrSortBk, rect);
		}
	}
	OS.SaveDC (hDC);
	if (clipRect != null) {
		int /*long*/ hRgn = OS.CreateRectRgn (clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
		POINT lpPoint = new POINT ();
		OS.GetWindowOrgEx (hDC, lpPoint);
		OS.OffsetRgn (hRgn, -lpPoint.x, -lpPoint.y);
		OS.SelectClipRgn (hDC, hRgn);
		OS.DeleteObject (hRgn);
	}
	return result;
}

LRESULT CDDS_POSTPAINT (NMTVCUSTOMDRAW nmcd, int /*long*/ wParam, int /*long*/ lParam) {
	if (ignoreCustomDraw) return null;
	if (OS.IsWindowVisible (handle)) {
		if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
			if (sortColumn != null && sortDirection != SWT.NONE) {
				if (findImageControl () == null) {
					int index = indexOf (sortColumn);
					if (index != -1) {
						int top = nmcd.top;
						/*
						* Bug in Windows.  For some reason, during a collapse,
						* when TVM_GETNEXTITEM is sent with TVGN_LASTVISIBLE
						* and the collapse causes the item being collapsed
						* to become the last visible item in the tree, the
						* message takes a long time to process.  In order for
						* the slowness to happen, the children of the item
						* must have children.  Times of up to 11 seconds have
						* been observed with 23 children, each having one
						* child.  The fix is to use the bottom partially
						* visible item rather than the last possible item
						* that could be visible.
						* 
						* NOTE: This problem only happens on Vista during
						* WM_NOTIFY with NM_CUSTOMDRAW and CDDS_POSTPAINT.
						*/
						int /*long*/ hItem = 0;
						if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
							hItem = getBottomItem ();
						} else {
							hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
						}
						if (hItem != 0) {
							RECT rect = new RECT ();
							if (OS.TreeView_GetItemRect (handle, hItem, rect, false)) {
								top = rect.bottom;
							}
						}
						RECT rect = new RECT ();
						OS.SetRect (rect, nmcd.left, top, nmcd.right, nmcd.bottom);
						RECT headerRect = new RECT ();
						OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect);
						rect.left = headerRect.left;
						rect.right = headerRect.right;
						fillBackground (nmcd.hdc, getSortColumnPixel (), rect);
					}
				}
			}
		}
		if (linesVisible) {
			int /*long*/ hDC = nmcd.hdc;
			if (hwndHeader != 0) {
				int x = 0;
				RECT rect = new RECT ();
				HDITEM hdItem = new HDITEM ();
				hdItem.mask = OS.HDI_WIDTH;
				for (int i=0; i<columnCount; i++) {
					int index = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, i, 0);
					OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
					OS.SetRect (rect, x, nmcd.top, x + hdItem.cxy, nmcd.bottom);
					OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_RIGHT);
					x += hdItem.cxy;
				}
			}
			int height = 0;
			RECT rect = new RECT ();
			/*
			* Bug in Windows.  For some reason, during a collapse,
			* when TVM_GETNEXTITEM is sent with TVGN_LASTVISIBLE
			* and the collapse causes the item being collapsed
			* to become the last visible item in the tree, the
			* message takes a long time to process.  In order for
			* the slowness to happen, the children of the item
			* must have children.  Times of up to 11 seconds have
			* been observed with 23 children, each having one
			* child.  The fix is to use the bottom partially
			* visible item rather than the last possible item
			* that could be visible.
			* 
			* NOTE: This problem only happens on Vista during
			* WM_NOTIFY with NM_CUSTOMDRAW and CDDS_POSTPAINT.
			*/
			int /*long*/ hItem = 0;
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				hItem = getBottomItem ();
			} else {
				hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
			}
			if (hItem != 0) {
				if (OS.TreeView_GetItemRect (handle, hItem, rect, false)) {
					height = rect.bottom - rect.top;
				}
			}
			if (height == 0) {
				height = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0);
				OS.GetClientRect (handle, rect);
				OS.SetRect (rect, rect.left, rect.top, rect.right, rect.top + height);
				OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
			}
			if (height != 0) {
				while (rect.bottom < nmcd.bottom) {
					int top = rect.top + height;
					OS.SetRect (rect, rect.left, top, rect.right, top + height);
					OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
				}
			}
		}
	}
	return new LRESULT (OS.CDRF_DODEFAULT);
}

LRESULT CDDS_PREPAINT (NMTVCUSTOMDRAW nmcd, int /*long*/ wParam, int /*long*/ lParam) {
	if (explorerTheme) {
		if ((OS.IsWindowEnabled (handle) && hooks (SWT.EraseItem)) || findImageControl () != null) {
			RECT rect = new RECT ();
			OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
			drawBackground (nmcd.hdc, rect);
		}
	}
	return new LRESULT (OS.CDRF_NOTIFYITEMDRAW | OS.CDRF_NOTIFYPOSTPAINT);
}

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	if (hwndParent != 0 && hwnd == hwndParent) {
		return OS.DefWindowProc (hwnd, msg, wParam, lParam);
	}
	if (hwndHeader != 0 && hwnd == hwndHeader) {
		return OS.CallWindowProc (HeaderProc, hwnd, msg, wParam, lParam);
	}
	switch (msg) {
		case OS.WM_SETFOCUS: {
			/*
			* Feature in Windows.  When a tree control processes WM_SETFOCUS,
			* if no item is selected, the first item in the tree is selected.
			* This is unexpected and might clear the previous selection.
			* The fix is to detect that there is no selection and set it to
			* the first visible item in the tree.  If the item was not selected,
			* only the focus is assigned.
			*/
			if ((style & SWT.SINGLE) != 0) break;
			int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hItem == 0) {
				hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
				if (hItem != 0) {
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					hSelect = hItem;
					ignoreDeselect = ignoreSelect = lockSelection = true;
					OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hItem);
					ignoreDeselect = ignoreSelect = lockSelection = false;
					hSelect = 0;
					if ((tvItem.state & OS.TVIS_SELECTED) == 0) {
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
			break;
		}	
	}
	int /*long*/ hItem = 0;
	boolean redraw = false;
	switch (msg) {
		/* Keyboard messages */
		case OS.WM_KEYDOWN:
			if (wParam == OS.VK_CONTROL || wParam == OS.VK_SHIFT) break;
			//FALL THROUGH
		case OS.WM_CHAR:
		case OS.WM_IME_CHAR:
		case OS.WM_KEYUP:
		case OS.WM_SYSCHAR:
		case OS.WM_SYSKEYDOWN:
		case OS.WM_SYSKEYUP:
			//FALL THROUGH
			
		/* Scroll messages */
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL:
			//FALL THROUGH
			
		/* Resize messages */
		case OS.WM_SIZE:
			redraw = findImageControl () != null && getDrawing () && OS.IsWindowVisible (handle);
			if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
			//FALL THROUGH
			
		/* Mouse messages */
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_LBUTTONDOWN:
		case OS.WM_LBUTTONUP:
		case OS.WM_MBUTTONDBLCLK:
		case OS.WM_MBUTTONDOWN:
		case OS.WM_MBUTTONUP:
		case OS.WM_MOUSEHOVER:
		case OS.WM_MOUSELEAVE:
		case OS.WM_MOUSEMOVE:
		case OS.WM_MOUSEWHEEL:
		case OS.WM_RBUTTONDBLCLK:
		case OS.WM_RBUTTONDOWN:
		case OS.WM_RBUTTONUP:
		case OS.WM_XBUTTONDBLCLK:
		case OS.WM_XBUTTONDOWN:
		case OS.WM_XBUTTONUP:
			//FALL THROUGH
			
		/* Other messages */
		case OS.WM_SETFONT:
		case OS.WM_TIMER: {
			if (findImageControl () != null) {
				hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
			}

			break;
		}
	}
	int /*long*/ code = OS.CallWindowProc (TreeProc, hwnd, msg, wParam, lParam);
	switch (msg) {
		/* Keyboard messages */
		case OS.WM_KEYDOWN:
			if (wParam == OS.VK_CONTROL || wParam == OS.VK_SHIFT) break;
			//FALL THROUGH
		case OS.WM_CHAR:
		case OS.WM_IME_CHAR:
		case OS.WM_KEYUP:
		case OS.WM_SYSCHAR:
		case OS.WM_SYSKEYDOWN:
		case OS.WM_SYSKEYUP:
			//FALL THROUGH
			
		/* Scroll messages */
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL:
			//FALL THROUGH
			
		/* Resize messages */
		case OS.WM_SIZE:
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
				if (hwndHeader != 0) OS.InvalidateRect (hwndHeader, null, true);
			}
			//FALL THROUGH
			
		/* Mouse messages */
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_LBUTTONDOWN:
		case OS.WM_LBUTTONUP:
		case OS.WM_MBUTTONDBLCLK:
		case OS.WM_MBUTTONDOWN:
		case OS.WM_MBUTTONUP:
		case OS.WM_MOUSEHOVER:
		case OS.WM_MOUSELEAVE:
		case OS.WM_MOUSEMOVE:
		case OS.WM_MOUSEWHEEL:
		case OS.WM_RBUTTONDBLCLK:
		case OS.WM_RBUTTONDOWN:
		case OS.WM_RBUTTONUP:
		case OS.WM_XBUTTONDBLCLK:
		case OS.WM_XBUTTONDOWN:
		case OS.WM_XBUTTONUP:
			//FALL THROUGH
			
		/* Other messages */
		case OS.WM_SETFONT:
		case OS.WM_TIMER: {
			if (findImageControl () != null) {
				if (hItem != OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0)) {
					OS.InvalidateRect (handle, null, true);
				}
			}
			updateScrollBar ();
			break;
		}
		
		case OS.WM_PAINT:
			painted = true;
			break;
	}
	return code;
}

void checkBuffered () {
	super.checkBuffered ();
	if ((style & SWT.VIRTUAL) != 0) {
		style |= SWT.DOUBLE_BUFFERED;
		OS.SendMessage (handle, OS.TVM_SETSCROLLTIME, 0, 0);
	}
	if (EXPLORER_THEME) {
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0) && OS.IsAppThemed ()) {
			int exStyle = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETEXTENDEDSTYLE, 0, 0);
			if ((exStyle & OS.TVS_EX_DOUBLEBUFFER) != 0) style |= SWT.DOUBLE_BUFFERED;
		}
	}
}

boolean checkData (TreeItem item, boolean redraw) {
	if ((style & SWT.VIRTUAL) == 0) return true;
	if (!item.cached) {
		TreeItem parentItem = item.getParentItem ();
		return checkData (item, parentItem == null ? indexOf (item) : parentItem.indexOf (item), redraw);
	}
	return true;
}

boolean checkData (TreeItem item, int index, boolean redraw) {
	if ((style & SWT.VIRTUAL) == 0) return true;
	if (!item.cached) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		event.index = index;
		TreeItem oldItem = currentItem;
		currentItem = item;
		/*
		* Bug in Windows.  If the tree scrolls during WM_NOTIFY
		* with TVN_GETDISPINFO, pixel corruption occurs.  The fix
		* is to detect that the top item has changed and redraw
		* the entire tree.
		*/
		int /*long*/ hTopItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		currentItem = oldItem;
		if (isDisposed () || item.isDisposed ()) return false;
		if (redraw) item.redraw ();
		if (hTopItem != OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0)) {
			OS.InvalidateRect (handle, null, true);
		}
	}
	return true;
}

boolean checkHandle (int /*long*/ hwnd) {
	return hwnd == handle || (hwndParent != 0 && hwnd == hwndParent) || (hwndHeader != 0 && hwnd == hwndHeader);
}

boolean checkScroll (int /*long*/ hItem) {
	/*
	* Feature in Windows.  If redraw is turned off using WM_SETREDRAW 
	* and a tree item that is not a child of the first root is selected or
	* scrolled using TVM_SELECTITEM or TVM_ENSUREVISIBLE, then scrolling
	* does not occur.  The fix is to detect this case, and make sure
	* that redraw is temporarily enabled.  To avoid flashing, DefWindowProc()
	* is called to disable redrawing.
	* 
	* NOTE:  The code that actually works around the problem is in the
	* callers of this method.
	*/
	if (getDrawing ()) return false;
	int /*long*/ hRoot = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	int /*long*/ hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
	while (hParent != hRoot && hParent != 0) {
		hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hParent);
	}
	return hParent == 0;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	hItem = findItem (hItem, index);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	clear (hItem, tvItem);
	if (all) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		clearAll (hItem, tvItem, all);
	}
}

void clear (int /*long*/ hItem, TVITEM tvItem) {
	tvItem.hItem = hItem;
	TreeItem item = null;
	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) != 0) {
		item = tvItem.lParam != -1 ? items [(int)/*64*/tvItem.lParam] : null;
	}
	if (item != null) {
		if ((style & SWT.VIRTUAL) != 0 && !item.cached) return;
		item.clear ();
		item.redraw ();
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
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) return;
	if (all) {
		boolean redraw = false;
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null && item != currentItem) {
				item.clear ();
				redraw = true;
			}
		}
		if (redraw) OS.InvalidateRect (handle, null, true);
	} else {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		clearAll (hItem, tvItem, all);
	}
}

void clearAll (int /*long*/ hItem, TVITEM tvItem, boolean all) {
	while (hItem != 0) {
		clear (hItem, tvItem);
		if (all) {
			int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
			clearAll (hFirstItem, tvItem, all);
		}
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
}

int /*long*/ CompareFunc (int /*long*/ lParam1, int /*long*/ lParam2, int /*long*/ lParamSort) {
	TreeItem item1 = items [(int)/*64*/lParam1], item2 = items [(int)/*64*/lParam2];
	String text1 = item1.getText ((int)/*64*/lParamSort), text2 = item2.getText ((int)/*64*/lParamSort);
	return sortDirection == SWT.UP ? text1.compareTo (text2) : text2.compareTo (text1);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (hwndHeader != 0) {
		HDITEM hdItem = new HDITEM ();
		hdItem.mask = OS.HDI_WIDTH;
		for (int i=0; i<columnCount; i++) {
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
			width += hdItem.cxy;
		}
		RECT rect = new RECT ();					
		OS.GetWindowRect (hwndHeader, rect);
		height += rect.bottom - rect.top;
	}
	RECT rect = new RECT ();
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	while (hItem != 0) {
		if ((style & SWT.VIRTUAL) == 0 && !painted) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
			tvItem.hItem = hItem;
			tvItem.pszText = OS.LPSTR_TEXTCALLBACK;
			ignoreCustomDraw = true;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			ignoreCustomDraw = false;
		}
		if (OS.TreeView_GetItemRect (handle, hItem, rect, true)) {
			width = Math.max (width, rect.right);
			height += rect.bottom - rect.top;
		}
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	if ((style & SWT.V_SCROLL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	}
	return new Point (width, height);
}

void createHandle () {
	super.createHandle ();
	state &= ~(CANVAS | THEME_BACKGROUND);

	/* Use the Explorer theme */
	if (EXPLORER_THEME) {
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0) && OS.IsAppThemed ()) {
			explorerTheme = true;
			OS.SetWindowTheme (handle, Display.EXPLORER, null);
			int bits = OS.TVS_EX_DOUBLEBUFFER | OS.TVS_EX_FADEINOUTEXPANDOS | OS.TVS_EX_RICHTOOLTIP;
			/*
			* This code is intentionally commented.
			*/
//			if ((style & SWT.FULL_SELECTION) == 0) bits |= OS.TVS_EX_AUTOHSCROLL;
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, 0, bits);
			/*
			* Bug in Windows.  When the tree is using the explorer
			* theme, it does not use COLOR_WINDOW_TEXT for the
			* default foreground color.  The fix is to explicitly
			* set the foreground.
			*/
			setForegroundPixel (-1);
		}
	}

	/*
	* Feature in Windows.  In version 5.8 of COMCTL32.DLL,
	* if the font is changed for an item, the bounds for the
	* item are not updated, causing the text to be clipped.
	* The fix is to detect the version of COMCTL32.DLL, and
	* if it is one of the versions with the problem, then
	* use version 5.00 of the control (a version that does
	* not have the problem).  This is the recommended work
	* around from the MSDN.
	*/
	if (!OS.IsWinCE) {
		if (OS.COMCTL32_MAJOR < 6) {
			OS.SendMessage (handle, OS.CCM_SETVERSION, 5, 0);
		}
	}
		
	/* Set the checkbox image list */
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	
	/*
	* Feature in Windows.  When the control is created,
	* it does not use the default system font.  A new HFONT
	* is created and destroyed when the control is destroyed.
	* This means that a program that queries the font from
	* this control, uses the font in another control and then
	* destroys this control will have the font unexpectedly
	* destroyed in the other control.  The fix is to assign
	* the font ourselves each time the control is created.
	* The control will not destroy a font that it did not
	* create.
	*/
	int /*long*/ hFont = OS.GetStockObject (OS.SYSTEM_FONT);
	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
	
	createdAsRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
}

void createHeaderToolTips () {
	if (OS.IsWinCE) return;
	if (headerToolTipHandle != 0) return;
	int bits = 0;
	if (OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
	}
	headerToolTipHandle = OS.CreateWindowEx (
		bits,
		new TCHAR (0, OS.TOOLTIPS_CLASS, true),
		null,
		OS.TTS_NOPREFIX,
		OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
		handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (headerToolTipHandle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Feature in Windows.  Despite the fact that the
	* tool tip text contains \r\n, the tooltip will
	* not honour the new line unless TTM_SETMAXTIPWIDTH
	* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
	* a large value.
	*/
	OS.SendMessage (headerToolTipHandle, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
}

void createItem (TreeColumn column, int index) {
	if (hwndHeader == 0) createParent ();
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			String [] strings = item.strings;
			if (strings != null) {
				String [] temp = new String [columnCount + 1];
				System.arraycopy (strings, 0, temp, 0, index);
				System.arraycopy (strings, index, temp, index + 1, columnCount - index);
				item.strings = temp;
			}
			Image [] images = item.images;
			if (images != null) {
				Image [] temp = new Image [columnCount + 1];
				System.arraycopy (images, 0, temp, 0, index);
				System.arraycopy (images, index, temp, index + 1, columnCount - index);
				item.images = temp;
			}
			if (index == 0) {
				if (columnCount != 0) {
					if (strings == null) {
						item.strings = new String [columnCount + 1];
						item.strings [1] = item.text;
					}
					item.text = "";
					if (images == null) {
						item.images = new Image [columnCount + 1];
						item.images [1] = item.image;
					}
					item.image = null;
				}
			}
			if (item.cellBackground != null) {
				int [] cellBackground = item.cellBackground;
				int [] temp = new int [columnCount + 1];
				System.arraycopy (cellBackground, 0, temp, 0, index);
				System.arraycopy (cellBackground, index, temp, index + 1, columnCount - index);
				temp [index] = -1;
				item.cellBackground = temp;
			}
			if (item.cellForeground != null) {
				int [] cellForeground = item.cellForeground;
				int [] temp = new int [columnCount + 1];
				System.arraycopy (cellForeground, 0, temp, 0, index);
				System.arraycopy (cellForeground, index, temp, index + 1, columnCount - index);
				temp [index] = -1;
				item.cellForeground = temp;
			}
			if (item.cellFont != null) {
				Font [] cellFont = item.cellFont;
				Font [] temp = new Font [columnCount + 1];
				System.arraycopy (cellFont, 0, temp, 0, index);
				System.arraycopy (cellFont, index, temp, index + 1, columnCount- index);
				item.cellFont = temp;
			}
		}
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	
	/*
	* Bug in Windows.  For some reason, when HDM_INSERTITEM
	* is used to insert an item into a header without text,
	* if is not possible to set the text at a later time.
	* The fix is to insert the item with an empty string.
	*/
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_TEXT | OS.HDI_FORMAT;
	hdItem.pszText = pszText;
	if ((column.style & SWT.LEFT) == SWT.LEFT) hdItem.fmt = OS.HDF_LEFT;
	if ((column.style & SWT.CENTER) == SWT.CENTER) hdItem.fmt = OS.HDF_CENTER;
	if ((column.style & SWT.RIGHT) == SWT.RIGHT) hdItem.fmt = OS.HDF_RIGHT;
	OS.SendMessage (hwndHeader, OS.HDM_INSERTITEM, index, hdItem);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	
	/* When the first column is created, hide the horizontal scroll bar */
	if (columnCount == 1) {
		scrollWidth = 0;
		if ((style & SWT.H_SCROLL) != 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			bits |= OS.TVS_NOHSCROLL;
			OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
		}
		/*
		* Bug in Windows.  When TVS_NOHSCROLL is set after items
		* have been inserted into the tree, Windows shows the
		* scroll bar.  The fix is to check for this case and
		* explicitly hide the scroll bar explicitly.
		*/
		int count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
		if (count != 0) {
			if (!OS.IsWinCE) OS.ShowScrollBar (handle, OS.SB_HORZ, false);
		}
		createItemToolTips ();
		if (itemToolTipHandle != 0) {
			OS.SendMessage (itemToolTipHandle, OS.TTM_SETDELAYTIME, OS.TTDT_AUTOMATIC, -1);
		}
	}
	setScrollWidth ();
	updateImageList ();
	updateScrollBar ();
	
	/* Redraw to hide the items when the first column is created */
	if (columnCount == 1 && OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0) != 0) {
		OS.InvalidateRect (handle, null, true);
	}
	
	/* Add the tool tip item for the header */
	if (headerToolTipHandle != 0) {
		RECT rect = new RECT ();
		if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, rect) != 0) {
			TOOLINFO lpti = new TOOLINFO ();
			lpti.cbSize = TOOLINFO.sizeof;
			lpti.uFlags = OS.TTF_SUBCLASS;
			lpti.hwnd = hwndHeader;
			lpti.uId = column.id = display.nextToolTipId++;
			lpti.left = rect.left;
			lpti.top = rect.top;
			lpti.right = rect.right;
			lpti.bottom = rect.bottom;
			lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
			OS.SendMessage (headerToolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
		}
	}
}

void createItem (TreeItem item, int /*long*/ hParent, int /*long*/ hInsertAfter, int /*long*/ hItem) {
	int id = -1;
	if (item != null) {
		id = lastID < items.length ? lastID : 0;
		while (id < items.length && items [id] != null) id++;
		if (id == items.length) {
			/*
			* Grow the array faster when redraw is off or the
			* table is not visible.  When the table is painted,
			* the items array is resized to be smaller to reduce
			* memory usage.
			*/
			int length = 0;
			if (getDrawing () && OS.IsWindowVisible (handle)) {
				length = items.length + 4;
			} else {
				shrink = true;
				length = Math.max (4, items.length * 3 / 2);
			}
			TreeItem [] newItems = new TreeItem [length];
			System.arraycopy (items, 0, newItems, 0, items.length);
			items = newItems;
		}
		lastID = id + 1;
	}
	int /*long*/ hNewItem = 0;
	int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent);
	boolean fixParent = hFirstItem == 0;
	if (hItem == 0) {
		TVINSERTSTRUCT tvInsert = new TVINSERTSTRUCT ();
		tvInsert.hParent = hParent;
		tvInsert.hInsertAfter = hInsertAfter;
		tvInsert.lParam = id;
		tvInsert.pszText = OS.LPSTR_TEXTCALLBACK;
		tvInsert.iImage = tvInsert.iSelectedImage = OS.I_IMAGECALLBACK;
		tvInsert.mask = OS.TVIF_TEXT | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE | OS.TVIF_HANDLE | OS.TVIF_PARAM;
		if ((style & SWT.CHECK) != 0) {
			tvInsert.mask = tvInsert.mask | OS.TVIF_STATE;
			tvInsert.state = 1 << 12;
			tvInsert.stateMask = OS.TVIS_STATEIMAGEMASK;
		}
		ignoreCustomDraw = true;
		hNewItem = OS.SendMessage (handle, OS.TVM_INSERTITEM, 0, tvInsert);
		ignoreCustomDraw = false;
		if (hNewItem == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		/*
		* This code is intentionally commented.
		*/
//		if (hParent != 0) {
//			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//			bits |= OS.TVS_LINESATROOT;
//			OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
//		}
	} else {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		tvItem.hItem = hNewItem = hItem;
		tvItem.lParam = id;
		OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
	}
	if (item != null) {
		item.handle = hNewItem;
		items [id] = item;
	}
	if (hFirstItem == 0) {
		if (hInsertAfter == OS.TVI_FIRST || hInsertAfter == OS.TVI_LAST) {
			hFirstIndexOf = hLastIndexOf = hFirstItem = hNewItem;
			itemCount = lastIndexOf = 0;
		}
	}
	if (hFirstItem == hFirstIndexOf && itemCount != -1) itemCount++;
	if (hItem == 0) {
		/*
		* Bug in Windows.  When a child item is added to a parent item
		* that has no children outside of WM_NOTIFY with control code
		* TVN_ITEMEXPANDED, the tree widget does not redraw the +/-
		* indicator.  The fix is to detect the case when the first
		* child is added to a visible parent item and redraw the parent.
		*/
		if (fixParent) {
			if (getDrawing () && OS.IsWindowVisible (handle)) {
				RECT rect = new RECT ();
				if (OS.TreeView_GetItemRect (handle, hParent, rect, false)) {
					OS.InvalidateRect (handle, rect, true);
				}
			}
		}
		/*
		* Bug in Windows.  When a new item is added while Windows
		* is requesting data a tree item using TVN_GETDISPINFO,
		* outstanding damage for items that are below the new item
		* is not scrolled.  The fix is to explicitly damage the
		* new area.
		*/
		if ((style & SWT.VIRTUAL) != 0) {
			if (currentItem != null) {
				RECT rect = new RECT ();
				if (OS.TreeView_GetItemRect (handle, hNewItem, rect, false)) {
					RECT damageRect = new RECT ();
					boolean damaged = OS.GetUpdateRect (handle, damageRect, true);
					if (damaged && damageRect.top < rect.bottom) {
						if (OS.IsWinCE) {
							OS.OffsetRect (damageRect, 0, rect.bottom - rect.top);
							OS.InvalidateRect (handle, damageRect, true);
						} else {
							int /*long*/ rgn = OS.CreateRectRgn (0, 0, 0, 0);
							int result = OS.GetUpdateRgn (handle, rgn, true);
							if (result != OS.NULLREGION) {
								OS.OffsetRgn (rgn, 0, rect.bottom - rect.top);
								OS.InvalidateRgn (handle, rgn, true);
							}
							OS.DeleteObject (rgn);
						}
					}
				}
			}
		}
		updateScrollBar ();
	}
}

void createItemToolTips () {
	if (OS.IsWinCE) return;
	if (itemToolTipHandle != 0) return;
	int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	bits1 |= OS.TVS_NOTOOLTIPS;
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits1);
	int bits2 = 0;
	if (OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		if ((style & SWT.RIGHT_TO_LEFT) != 0) bits2 |= OS.WS_EX_LAYOUTRTL;
	}
	/*
	* Feature in Windows.  For some reason, when the user
	* clicks on a tool tip, it temporarily takes focus, even
	* when WS_EX_NOACTIVATE is specified.  The fix is to
	* use WS_EX_TRANSPARENT, even though WS_EX_TRANSPARENT
	* is documented to affect painting, not hit testing.
	*
	* NOTE: Windows 2000 doesn't have the problem and
	* setting WS_EX_TRANSPARENT causes pixel corruption. 
	*/
	if (OS.COMCTL32_MAJOR >= 6) bits2 |= OS.WS_EX_TRANSPARENT;
	itemToolTipHandle = OS.CreateWindowEx (
		bits2,
		new TCHAR (0, OS.TOOLTIPS_CLASS, true),
		null,
		OS.TTS_NOPREFIX | OS.TTS_NOANIMATE | OS.TTS_NOFADE,
		OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
		handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (itemToolTipHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SendMessage (itemToolTipHandle, OS.TTM_SETDELAYTIME, OS.TTDT_INITIAL, 0);
	TOOLINFO lpti = new TOOLINFO ();
	lpti.cbSize = TOOLINFO.sizeof;
	lpti.hwnd = handle;
	lpti.uId = handle;
	lpti.uFlags = OS.TTF_SUBCLASS | OS.TTF_TRANSPARENT;
	lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
	OS.SendMessage (itemToolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
}

void createParent () {
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	OS.MapWindowPoints (0, parent.handle, rect, 2);
	int oldStyle = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int newStyle = super.widgetStyle () & ~OS.WS_VISIBLE;
	if ((oldStyle & OS.WS_DISABLED) != 0) newStyle |= OS.WS_DISABLED;
//	if ((oldStyle & OS.WS_VISIBLE) != 0) newStyle |= OS.WS_VISIBLE;
	hwndParent = OS.CreateWindowEx (
		super.widgetExtStyle (),
		super.windowClass (),
		null,
		newStyle,
		rect.left, 
		rect.top, 
		rect.right - rect.left, 
		rect.bottom - rect.top,
		parent.handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (hwndParent == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SetWindowLongPtr (hwndParent, OS.GWLP_ID, hwndParent);
	int bits = 0;
	if (OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		bits |= OS.WS_EX_NOINHERITLAYOUT;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
	}
	hwndHeader = OS.CreateWindowEx (
		bits,
		HeaderClass,
		null,
		OS.HDS_BUTTONS | OS.HDS_FULLDRAG | OS.HDS_DRAGDROP | OS.HDS_HIDDEN | OS.WS_CHILD | OS.WS_CLIPSIBLINGS,
		0, 0, 0, 0,
		hwndParent,
		0,
		OS.GetModuleHandle (null),
		null);
	if (hwndHeader == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SetWindowLongPtr (hwndHeader, OS.GWLP_ID, hwndHeader);
	if (OS.IsDBLocale) {
		int /*long*/ hIMC = OS.ImmGetContext (handle);
		OS.ImmAssociateContext (hwndParent, hIMC);
		OS.ImmAssociateContext (hwndHeader, hIMC);		
		OS.ImmReleaseContext (handle, hIMC);
	}
	//This code is intentionally commented
//	if (!OS.IsPPC) {
//		if ((style & SWT.BORDER) != 0) {
//			int oldExStyle = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//			oldExStyle &= ~OS.WS_EX_CLIENTEDGE;
//			OS.SetWindowLong (handle, OS.GWL_EXSTYLE, oldExStyle);
//		}
//	}
	int /*long*/ hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (hFont != 0) OS.SendMessage (hwndHeader, OS.WM_SETFONT, hFont, 0);
	int /*long*/ hwndInsertAfter = OS.GetWindow (handle, OS.GW_HWNDPREV);
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
	SetWindowPos (hwndParent, hwndInsertAfter, 0, 0, 0, 0, flags);
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
	OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
	info.nPage = info.nMax + 1;
	OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
	OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
	info.nPage = info.nMax + 1;
	OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
	customDraw = true;
	deregister ();
	if ((oldStyle & OS.WS_VISIBLE) != 0) {
		OS.ShowWindow (hwndParent, OS.SW_SHOW);
	}
	int /*long*/ hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) OS.SetFocus (hwndParent);
	OS.SetParent (handle, hwndParent);
	if (hwndFocus == handle) OS.SetFocus (handle);
	register ();
	subclass ();
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
	columns = new TreeColumn [4];
	itemCount = -1;
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

void deregister () {
	super.deregister ();
	if (hwndParent != 0) display.removeControl (hwndParent);
	if (hwndHeader != 0) display.removeControl (hwndHeader);
}

void deselect (int /*long*/ hItem, TVITEM tvItem, int /*long*/ hIgnoreItem) {
	while (hItem != 0) {
		if (hItem != hIgnoreItem) {
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
		int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		deselect (hFirstItem, tvItem, hIgnoreItem);
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
}

/**
 * Deselects an item in the receiver.  If the item was already
 * deselected, it remains deselected.
 *
 * @param item the item to be deselected
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
 * @since 3.4
 */
public void deselect (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	tvItem.hItem = item.handle;
	OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll () {
	checkWidget ();
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem != 0) {
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	} else {
		int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
		OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
		if ((style & SWT.VIRTUAL) != 0) {
			int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
			deselect (hItem, tvItem, 0);
		} else {
			for (int i=0; i<items.length; i++) {
				TreeItem item = items [i];
				if (item != null) {
					tvItem.hItem = item.handle;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			}
		}
		OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
	}
}

void destroyItem (TreeColumn column) {
	if (hwndHeader == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	int [] oldOrder = new int [columnCount];
	OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, oldOrder);
	int orderIndex = 0;
	while (orderIndex < columnCount) {
		if (oldOrder [orderIndex] == index) break;
		orderIndex++;
	}
	RECT headerRect = new RECT ();
	OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect);
	if (OS.SendMessage (hwndHeader, OS.HDM_DELETEITEM, index, 0) == 0) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			if (columnCount == 0) {
				item.strings = null;
				item.images = null;
				item.cellBackground = null;
				item.cellForeground = null;
				item.cellFont = null;
			} else {
				if (item.strings != null) {
					String [] strings = item.strings;
					if (index == 0) {
						item.text = strings [1] != null ? strings [1] : "";
					}
					String [] temp = new String [columnCount];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index + 1, temp, index, columnCount - index);
					item.strings = temp;
				} else {
					if (index == 0) item.text = "";
				}
				if (item.images != null) {
					Image [] images = item.images;
					if (index == 0) item.image = images [1];
					Image [] temp = new Image [columnCount];
					System.arraycopy (images, 0, temp, 0, index);
					System.arraycopy (images, index + 1, temp, index, columnCount - index);
					item.images = temp;
				} else {
					if (index == 0) item.image = null;
				}
				if (item.cellBackground != null) {
					int [] cellBackground = item.cellBackground;
					int [] temp = new int [columnCount];
					System.arraycopy (cellBackground, 0, temp, 0, index);
					System.arraycopy (cellBackground, index + 1, temp, index, columnCount - index);
					item.cellBackground = temp;
				}
				if (item.cellForeground != null) {
					int [] cellForeground = item.cellForeground;
					int [] temp = new int [columnCount];
					System.arraycopy (cellForeground, 0, temp, 0, index);
					System.arraycopy (cellForeground, index + 1, temp, index, columnCount - index);
					item.cellForeground = temp;
				}
				if (item.cellFont != null) {
					Font [] cellFont = item.cellFont;
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index + 1, temp, index, columnCount - index);
					item.cellFont = temp;
				}
			}
		}
	}

	/*
	* When the last column is deleted, show the horizontal
	* scroll bar.  Otherwise, left align the first column
	* and redraw the columns to the right.
	*/
	if (columnCount == 0) {
		scrollWidth = 0;
		if (!hooks (SWT.MeasureItem)) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((style & SWT.H_SCROLL) != 0) bits &= ~OS.TVS_NOHSCROLL;
			OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
			OS.InvalidateRect (handle, null, true);
		}
		if (itemToolTipHandle != 0) {
			OS.SendMessage (itemToolTipHandle, OS.TTM_SETDELAYTIME, OS.TTDT_INITIAL, 0);
		}
	} else {
		if (index == 0) {
			columns [0].style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
			columns [0].style |= SWT.LEFT;
			HDITEM hdItem = new HDITEM ();
			hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE;
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
			hdItem.fmt &= ~OS.HDF_JUSTIFYMASK;
			hdItem.fmt |= OS.HDF_LEFT;
			OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
		}
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		rect.left = headerRect.left;
		OS.InvalidateRect (handle, rect, true);
	}
	setScrollWidth ();
	updateImageList ();
	updateScrollBar ();
	if (columnCount != 0) {
		int [] newOrder = new int [columnCount];
		OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, newOrder);
		TreeColumn [] newColumns = new TreeColumn [columnCount - orderIndex];
		for (int i=orderIndex; i<newOrder.length; i++) {
			newColumns [i - orderIndex] = columns [newOrder [i]];
			newColumns [i - orderIndex].updateToolTip (newOrder [i]);
		}	
		for (int i=0; i<newColumns.length; i++) {
			if (!newColumns [i].isDisposed ()) {
				newColumns [i].sendEvent (SWT.Move);
			}
		}
	}

	/* Remove the tool tip item for the header */
	if (headerToolTipHandle != 0) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		lpti.uId = column.id;
		lpti.hwnd = hwndHeader;
		OS.SendMessage (headerToolTipHandle, OS.TTM_DELTOOL, 0, lpti);
	}
}

void destroyItem (TreeItem item, int /*long*/ hItem) {
	hFirstIndexOf = hLastIndexOf = 0;
	itemCount = -1;
	/*
	* Feature in Windows.  When an item is removed that is not
	* visible in the tree because it belongs to a collapsed branch,
	* Windows redraws the tree causing a flash for each item that
	* is removed.  The fix is to detect whether the item is visible,
	* force the widget to be fully painted, turn off redraw, remove
	* the item and validate the damage caused by the removing of
	* the item.
	* 
	* NOTE: This fix is not necessary when double buffering and
	* can cause problems for virtual trees due to the call to
	* UpdateWindow() that flushes outstanding WM_PAINT events,
	* allowing application code to add or remove items during
	* this remove operation.
	*/
	int /*long*/ hParent = 0;
	boolean fixRedraw = false;
	if ((style & SWT.DOUBLE_BUFFERED) == 0) {
		if (getDrawing () && OS.IsWindowVisible (handle)) {
			RECT rect = new RECT ();
			fixRedraw = !OS.TreeView_GetItemRect (handle, hItem, rect, false);
		}
	}
	if (fixRedraw) {
		hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
		OS.UpdateWindow (handle);
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		/*
		* This code is intentionally commented.
		*/
//		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	if ((style & SWT.MULTI) != 0) {
		ignoreDeselect = ignoreSelect = lockSelection = true;
	}

	/*
	* Feature in Windows.  When an item is deleted and a tool tip
	* is showing, Window requests the new text for the new item
	* that is under the cursor right away.  This means that when
	* multiple items are deleted, the tool tip flashes, showing
	* each new item in the tool tip as it is scrolled into view.
	* The fix is to hide tool tips when any item is deleted.
	* 
	* NOTE:  This only happens on Vista.
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		int /*long*/ hwndToolTip = OS.SendMessage (handle, OS.TVM_GETTOOLTIPS, 0, 0);
		if (hwndToolTip != 0) OS.SendMessage (hwndToolTip, OS.TTM_POP, 0 ,0);
	}
	
	shrink = ignoreShrink = true;
	OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
	ignoreShrink = false;
	if ((style & SWT.MULTI) != 0) {
		ignoreDeselect = ignoreSelect = lockSelection = false;
	}
	if (fixRedraw) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
		OS.ValidateRect (handle, null);
		/*
		* If the item that was deleted was the last child of a tree item that
		* is visible, redraw the parent item to force the +/- to be updated.
		*/
		if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent) == 0) {
			RECT rect = new RECT ();
			if (OS.TreeView_GetItemRect (handle, hParent, rect, false)) {
				OS.InvalidateRect (handle, rect, true);
			}
		}
	}
	int count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
	if (count == 0) {
		if (imageList != null) {
			OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
			display.releaseImageList (imageList);
		}
		imageList = null;
		if (hwndParent == 0 && !linesVisible) {
			if (!hooks (SWT.MeasureItem) && !hooks (SWT.EraseItem) && !hooks (SWT.PaintItem)) {
				customDraw = false;
			}
		}
		items = new TreeItem [4];
		scrollWidth = 0;
		setScrollWidth ();
	}
	updateScrollBar ();
}

void destroyScrollBar (int type) {
	super.destroyScrollBar (type);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		bits &= ~(OS.WS_HSCROLL | OS.WS_VSCROLL);
		bits |= OS.TVS_NOSCROLL;
	} else {
		if ((style & SWT.H_SCROLL) == 0) {
			bits &= ~OS.WS_HSCROLL;
			bits |= OS.TVS_NOHSCROLL;
		}
	}
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
}

void enableDrag (boolean enabled) {
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if (enabled && hooks (SWT.DragDetect)) {
		bits &= ~OS.TVS_DISABLEDRAGDROP;		
	} else {
		bits |= OS.TVS_DISABLEDRAGDROP;
	}
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Feature in Windows.  When a tree is given a background color
	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
	* the tree using the background color rather than the disabled
	* colors.  This is different from the table which draws grayed.
	* The fix is to set the default background color while the tree
	* is disabled and restore it when enabled.
	*/
	Control control = findBackgroundControl ();
	/*
	* Bug in Windows.  On Vista only, Windows does not draw using
	* the background color when the tree is disabled.  The fix is
	* to set the default color, even when the color has not been
	* changed, causing Windows to draw correctly.
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		if (control == null) control = this;
	}
	if (control != null) {
		if (control.backgroundImage == null) {
			_setBackgroundPixel (enabled ? control.getBackgroundPixel () : -1);
		}
	}
	if (hwndParent != 0) OS.EnableWindow (hwndParent, enabled);

	/*
	* Feature in Windows.  When the tree has the style
	* TVS_FULLROWSELECT, the background color for the
	* entire row is filled when an item is painted,
	* drawing on top of the sort column color.  The fix
	* is to clear TVS_FULLROWSELECT when a their is
	* as sort column.
	*/
	updateFullSelection ();
}

boolean findCell (int x, int y, TreeItem [] item, int [] index, RECT [] cellRect, RECT [] itemRect) {
	boolean found = false;
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = x;
	lpht.y = y;
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0) {
		item [0] = _getItem (lpht.hItem);
		POINT pt = new POINT ();
		pt.x = x;
		pt.y = y;
		int /*long*/ hDC = OS.GetDC (handle);
		int /*long*/ oldFont = 0, newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		RECT rect = new RECT ();
		if (hwndParent != 0) {
			OS.GetClientRect (hwndParent, rect);
			OS.MapWindowPoints (hwndParent, handle, rect, 2);
		} else {
			OS.GetClientRect (handle, rect);
		}
		int count = Math.max (1, columnCount);
		int [] order = new int [count];
		if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, count, order);
		index [0] = 0;
		boolean quit = false;
		while (index [0] < count && !quit) {
			int /*long*/ hFont = item [0].fontHandle (order [index [0]]);
			if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
			cellRect [0] = item [0].getBounds (order [index [0]], true, false, true, false, true, hDC);
			if (cellRect [0].left > rect.right) {
				quit = true;
			} else {
				cellRect [0].right = Math.min (cellRect [0].right, rect.right);
				if (OS.PtInRect (cellRect [0], pt)) {
					if (isCustomToolTip ()) {
						int state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, lpht.hItem, OS.TVIS_SELECTED);
						int detail = (state & OS.TVIS_SELECTED) != 0 ? SWT.SELECTED : 0;
						Event event = sendMeasureItemEvent (item [0], order [index [0]], hDC, detail);
						if (isDisposed () || item [0].isDisposed ()) break;
						itemRect [0] = new RECT ();
						itemRect [0].left = event.x;
						itemRect [0].right = event.x + event.width;
						itemRect [0].top = event.y;
						itemRect [0].bottom = event.y + event.height;
					} else {
						itemRect [0] = item [0].getBounds (order [index [0]], true, false, false, false, false, hDC);
					}
					if (itemRect [0].right > cellRect [0].right) found = true;
					quit = true;
				}
			}
			if (hFont != -1) OS.SelectObject (hDC, hFont);
			if (!found) index [0]++;
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	return found;
}

int findIndex (int /*long*/ hFirstItem, int /*long*/ hItem) {
	if (hFirstItem == 0) return -1;
	if (hFirstItem == hFirstIndexOf) {
		if (hFirstIndexOf == hItem) {
			hLastIndexOf = hFirstIndexOf;
			return lastIndexOf = 0;
		}
		if (hLastIndexOf == hItem) return lastIndexOf;
		int /*long*/ hPrevItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUS, hLastIndexOf);
		if (hPrevItem == hItem) {
			hLastIndexOf = hPrevItem;
			return --lastIndexOf;
		}
		int /*long*/ hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hLastIndexOf);
		if (hNextItem == hItem) {
			hLastIndexOf = hNextItem;
			return ++lastIndexOf;
		}
		int previousIndex = lastIndexOf - 1;
		while (hPrevItem != 0 && hPrevItem != hItem) {
			hPrevItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUS, hPrevItem);
			--previousIndex;
		}
		if (hPrevItem == hItem) {
			hLastIndexOf = hPrevItem;
			return lastIndexOf = previousIndex;
		}
		int nextIndex = lastIndexOf + 1;
		while (hNextItem != 0 && hNextItem != hItem) {
			hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hNextItem);
			nextIndex++;
		}
		if (hNextItem == hItem) {
			hLastIndexOf = hNextItem;
			return lastIndexOf = nextIndex;
		}
		return -1;
	}
	int index = 0;
	int /*long*/ hNextItem = hFirstItem;
	while (hNextItem != 0 && hNextItem != hItem) {
		hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hNextItem);
		index++;
	}
	if (hNextItem == hItem) {
		itemCount = -1;
		hFirstIndexOf = hFirstItem;
		hLastIndexOf = hNextItem;
		return lastIndexOf = index;
	}
	return -1;
}

Widget findItem (int /*long*/ hItem) {
	return _getItem (hItem);
}

int /*long*/ findItem (int /*long*/ hFirstItem, int index) {
	if (hFirstItem == 0) return 0;
	if (hFirstItem == hFirstIndexOf) {
		if (index == 0) {
			lastIndexOf = 0;
			return hLastIndexOf = hFirstIndexOf;
		}
		if (lastIndexOf == index) return hLastIndexOf;
		if (lastIndexOf - 1 == index) {
			--lastIndexOf;
			return hLastIndexOf = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUS, hLastIndexOf);
		}
		if (lastIndexOf + 1 == index) {
			lastIndexOf++;
			return hLastIndexOf = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hLastIndexOf);
		}
		if (index < lastIndexOf) {
			int previousIndex = lastIndexOf - 1;
			int /*long*/ hPrevItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUS, hLastIndexOf);
			while (hPrevItem != 0 && index < previousIndex) {
				hPrevItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUS, hPrevItem);
				--previousIndex;
			}
			if (index == previousIndex) {
				lastIndexOf = previousIndex;
				return hLastIndexOf = hPrevItem;
			}
		} else {
			int nextIndex = lastIndexOf + 1;
			int /*long*/ hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hLastIndexOf);
			while (hNextItem != 0 && nextIndex < index) {
				hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hNextItem);
				nextIndex++;
			}
			if (index == nextIndex) {
				lastIndexOf = nextIndex;
				return hLastIndexOf = hNextItem;
			}
		}
		return 0;
	}
	int nextIndex = 0; 
	int /*long*/ hNextItem = hFirstItem;
	while (hNextItem != 0 && nextIndex < index) {
		hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hNextItem);
		nextIndex++;
	}
	if (index == nextIndex) {
		itemCount = -1;
		lastIndexOf = nextIndex;
		hFirstIndexOf = hFirstItem;
		return hLastIndexOf = hNextItem;
	}
	return 0;
}

TreeItem getFocusItem () {
//	checkWidget ();
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	return hItem != 0 ? _getItem (hItem) : null;
}

/**
 * Returns the width in pixels of a grid line.
 *
 * @return the width of a grid line in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getGridLineWidth () {
	checkWidget ();
	return GRID_WIDTH;
}

/**
 * Returns the height of the receiver's header 
 *
 * @return the height of the header or zero if the header is not visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int getHeaderHeight () {
	checkWidget ();
	if (hwndHeader == 0) return 0;
	RECT rect = new RECT ();					
	OS.GetWindowRect (hwndHeader, rect);
	return rect.bottom - rect.top;
}

/**
 * Returns <code>true</code> if the receiver's header is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's header's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public boolean getHeaderVisible () {
	checkWidget ();
	if (hwndHeader == 0) return false;
	int bits = OS.GetWindowLong (hwndHeader, OS.GWL_STYLE);
	return (bits & OS.WS_VISIBLE) != 0;
}

Point getImageSize () {
	if (imageList != null) return imageList.getImageSize ();
	return new Point (0, getItemHeight ());
}

int /*long*/ getBottomItem () {
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
	if (hItem == 0) return 0;
	int index = 0, count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0);
	while (index <= count) {
		int /*long*/ hNextItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
		if (hNextItem == 0) return hItem;
		hItem = hNextItem;
		index++;
	}
	return hItem;
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * Columns are returned in the order that they were created.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the tree.
 * This occurs when the programmer uses the tree like a list, adding
 * items but never creating a column.
 *
 * @param index the index of the column to return
 * @return the column at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public TreeColumn getColumn (int index) {
	checkWidget ();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the tree like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getColumnCount () {
	checkWidget ();
	return columnCount;
}

/**
 * Returns an array of zero-relative integers that map
 * the creation order of the receiver's items to the
 * order in which they are currently being displayed.
 * <p>
 * Specifically, the indices of the returned array represent
 * the current visual order of the items, and the contents
 * of the array represent the creation order of the items.
 * </p><p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the current visual order of the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public int[] getColumnOrder () {
	checkWidget ();
	if (columnCount == 0) return new int [0];
	int [] order = new int [columnCount];
	OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, order);
	return order;
}

/**
 * Returns an array of <code>TreeColumn</code>s which are the
 * columns in the receiver. Columns are returned in the order
 * that they were created.  If no <code>TreeColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the tree like a list, adding items but
 * never creating a column.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public TreeColumn [] getColumns () {
	checkWidget ();
	TreeColumn [] result = new TreeColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
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
	int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hFirstItem == 0) error (SWT.ERROR_INVALID_RANGE);
	int /*long*/ hItem = findItem (hFirstItem, index);
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (hItem);
}

TreeItem getItem (NMTVCUSTOMDRAW nmcd) {
	/*
	* Bug in Windows.  If the lParam field of TVITEM
	* is changed during custom draw using TVM_SETITEM,
	* the lItemlParam field of the NMTVCUSTOMDRAW struct
	* is not updated until the next custom draw.  The
	* fix is to query the field from the item instead
	* of using the struct.
	*/
	int id = (int)/*64*/nmcd.lItemlParam;
	if ((style & SWT.VIRTUAL) != 0) {
		if (id == -1) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
			tvItem.hItem = nmcd.dwItemSpec;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			id = (int)/*64*/tvItem.lParam;
		}
	}
	return _getItem (nmcd.dwItemSpec, id);
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 * <p>
 * The item that is returned represents an item that could be selected by the user.
 * For example, if selection only occurs in items in the first column, then null is 
 * returned if the point is outside of the item. 
 * Note that the SWT.FULL_SELECTION style hint, which specifies the selection policy,
 * determines the extent of the selection.
 * </p>
 *
 * @param point the point used to locate the item
 * @return the item at the given point, or null if the point is not in a selectable item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = point.x;
	lpht.y = point.y;
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0) {
		int flags = OS.TVHT_ONITEM;
		if ((style & SWT.FULL_SELECTION) != 0) {
			flags |= OS.TVHT_ONITEMRIGHT | OS.TVHT_ONITEMINDENT;
		} else {
			if (hooks (SWT.MeasureItem)) {
				lpht.flags &= ~(OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL);
				if (hitTestSelection (lpht.hItem, lpht.x, lpht.y)) {
					lpht.flags |= OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL;
				}
			}
		}
		if ((lpht.flags & flags) != 0) return _getItem (lpht.hItem);
	}
	return null;
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
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
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) return 0;
	return getItemCount (hItem);
}

int getItemCount (int /*long*/ hItem) {
	int count = 0;
	int /*long*/ hFirstItem = hItem;
	if (hItem == hFirstIndexOf) {
		if (itemCount != -1) return itemCount;
		hFirstItem = hLastIndexOf;
		count = lastIndexOf;
	}
	while (hFirstItem != 0) {
		hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hFirstItem);
		count++;
	}
	if (hItem == hFirstIndexOf) itemCount = count;
	return count;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0);
}

/**
 * Returns a (possibly empty) array of items contained in the
 * receiver that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) return new TreeItem [0];
	return getItems (hItem);
}

TreeItem [] getItems (int /*long*/ hTreeItem) {
	int count = 0; 
	int /*long*/ hItem = hTreeItem;
	while (hItem != 0) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		count++;
	}
	int index = 0;
	TreeItem [] result = new TreeItem [count];
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = hTreeItem;
	/*
	* Feature in Windows.  In some cases an expand or collapse message
	* can occur from within TVM_DELETEITEM.  When this happens, the item
	* being destroyed has been removed from the list of items but has not
	* been deleted from the tree.  The fix is to check for null items and
	* remove them from the list.
	*/
	while (tvItem.hItem != 0) {
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		TreeItem item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
		if (item != null) result [index++] = item;
		tvItem.hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, tvItem.hItem);
	}
	if (index != count) {
		TreeItem [] newResult = new TreeItem [index];
		System.arraycopy (result, 0, newResult, 0, index);
		result = newResult;
	}
	return result;
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise. Note that some platforms draw 
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the visibility state of the lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public boolean getLinesVisible () {
	checkWidget ();
	return linesVisible;
}

int /*long*/ getNextSelection (int /*long*/ hItem, TVITEM tvItem) {
	while (hItem != 0) {
		int state = 0;
		if (OS.IsWinCE) {
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			state = tvItem.state;
		} else {
			state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
		}
		if ((state & OS.TVIS_SELECTED) != 0) return hItem;
		int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		int /*long*/ hSelected = getNextSelection (hFirstItem, tvItem);
		if (hSelected != 0) return hSelected;
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
	return 0;
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
	return null;
}

int getSelection (int /*long*/ hItem, TVITEM tvItem, TreeItem [] selection, int index, int count, boolean bigSelection, boolean all) {
	while (hItem != 0) {
		if (OS.IsWinCE || bigSelection) {
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
				if (selection != null && index < selection.length) {
					selection [index] = _getItem (hItem, (int)/*64*/tvItem.lParam);
				}
				index++;
			}
		} else {
			int state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
			if ((state & OS.TVIS_SELECTED) != 0) {
				if (tvItem != null && selection != null && index < selection.length) {
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					selection [index] = _getItem (hItem, (int)/*64*/tvItem.lParam);
				}
				index++;
			}
		}
		if (index == count) break;
		if (all) {
			int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
			if ((index = getSelection (hFirstItem, tvItem, selection, index, count, bigSelection, all)) == count) {
				break;
			}
			hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		} else {
			hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
		}
	}
	return index;
}

/**
 * Returns an array of <code>TreeItem</code>s that are currently
 * selected in the receiver. The order of the items is unspecified.
 * An empty array indicates that no items are selected. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver. 
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getSelection () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return new TreeItem [0];
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & OS.TVIS_SELECTED) == 0) return new TreeItem [0];
		return new TreeItem [] {_getItem (tvItem.hItem, (int)/*64*/tvItem.lParam)};
	}
	int count = 0;
	TreeItem [] guess = new TreeItem [(style & SWT.VIRTUAL) != 0 ? 8 : 1];
	int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
	if ((style & SWT.VIRTUAL) != 0) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		count = getSelection (hItem, tvItem, guess, 0, -1, false, true);
	} else {
		TVITEM tvItem = null;
		if (OS.IsWinCE) {
			tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
		}
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) {
				int /*long*/ hItem = item.handle;
				int state = 0;
				if (OS.IsWinCE) {
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					state = tvItem.state;
				} else {
					state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
				}
				if ((state & OS.TVIS_SELECTED) != 0) {
					if (count < guess.length) guess [count] = item;
					count++;
				}
			}
		}
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
	if (count == 0) return new TreeItem [0];
	if (count == guess.length) return guess;
	TreeItem [] result = new TreeItem [count];
	if (count < guess.length) {
		System.arraycopy (guess, 0, result, 0, count);
		return result;
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	int itemCount = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
	boolean bigSelection = result.length > itemCount / 2;
	if (count != getSelection (hItem, tvItem, result, 0, count, bigSelection, false)) {
		getSelection (hItem, tvItem, result, 0, count, bigSelection, true);
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
	return result;
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return 0;
		int state = 0;
		if (OS.IsWinCE) {
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = hItem;
			tvItem.mask = OS.TVIF_STATE;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			state = tvItem.state;
		} else {
			state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
		}
		return (state & OS.TVIS_SELECTED) == 0 ? 0 : 1;
	}
	int count = 0;
	int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
	TVITEM tvItem = null;
	if (OS.IsWinCE) {
		tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_STATE;
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
	if ((style & SWT.VIRTUAL) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		count = getSelection (hItem, tvItem, null, 0, -1, false, true);
	} else {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) {
				int /*long*/ hItem = item.handle;
				int state = 0;
				if (OS.IsWinCE) {
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					state = tvItem.state;
				} else {
					state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
				}
				if ((state & OS.TVIS_SELECTED) != 0) count++;
			}
		}
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
	return count;
}

/**
 * Returns the column which shows the sort indicator for
 * the receiver. The value may be null if no column shows
 * the sort indicator.
 *
 * @return the sort indicator 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setSortColumn(TreeColumn)
 * 
 * @since 3.2
 */
public TreeColumn getSortColumn () {
	checkWidget ();
	return sortColumn;
}

int getSortColumnPixel () {
	int pixel = OS.IsWindowEnabled (handle) ? getBackgroundPixel () : OS.GetSysColor (OS.COLOR_3DFACE);
	int red = pixel & 0xFF;
	int green = (pixel & 0xFF00) >> 8;
	int blue = (pixel & 0xFF0000) >> 16;
	if (red > 240 && green > 240 && blue > 240) {
		red -= 8;
		green -= 8;
		blue -= 8;
	} else {
		red = Math.min (0xFF, (red / 10) + red);
		green = Math.min (0xFF, (green / 10) + green);
		blue = Math.min (0xFF, (blue / 10) + blue);
	}
	return (red & 0xFF) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
}

/**
 * Returns the direction of the sort indicator for the receiver. 
 * The value will be one of <code>UP</code>, <code>DOWN</code> 
 * or <code>NONE</code>.
 *
 * @return the sort direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setSortDirection(int)
 * 
 * @since 3.2
 */
public int getSortDirection () {
	checkWidget ();
	return sortDirection;
}

/**
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public TreeItem getTopItem () {
	checkWidget ();
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
	return hItem != 0 ? _getItem (hItem) : null;
}

boolean hitTestSelection (int /*long*/ hItem, int x, int y) {
	if (hItem == 0) return false;
	TreeItem item = _getItem (hItem);
	if (item == null) return false;
	if (!hooks (SWT.MeasureItem)) return false;
	boolean result = false;
	
	//BUG? - moved columns, only hittest first column
	//BUG? - check drag detect
	int [] order = new int [1], index = new int [1];
	
	int /*long*/ hDC = OS.GetDC (handle);
	int /*long*/ oldFont = 0, newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int /*long*/ hFont = item.fontHandle (order [index [0]]);
	if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
	int state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
	int detail = (state & OS.TVIS_SELECTED) != 0 ? SWT.SELECTED : 0;
	Event event = sendMeasureItemEvent (item, order [index [0]], hDC, detail);
	if (event.getBounds ().contains (x, y)) result = true;
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
//	if (isDisposed () || item.isDisposed ()) return false;
	return result;
}

int imageIndex (Image image, int index) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		Rectangle bounds = image.getBounds ();
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
	}
	int imageIndex = imageList.indexOf (image);
	if (imageIndex == -1) imageIndex = imageList.add (image);
	if (hwndHeader == 0 || OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0) == index) {
		/*
		* Feature in Windows.  When setting the same image list multiple
		* times, Windows does work making this operation slow.  The fix
		* is to test for the same image list before setting the new one.
		*/
		int /*long*/ hImageList = imageList.getHandle ();
		int /*long*/ hOldImageList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		if (hOldImageList != hImageList) {
			OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
			updateScrollBar ();
		}
	}
	return imageIndex;
}

int imageIndexHeader (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (headerImageList == null) {
		Rectangle bounds = image.getBounds ();
		headerImageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
		int index = headerImageList.indexOf (image);
		if (index == -1) index = headerImageList.add (image);
		int /*long*/ hImageList = headerImageList.getHandle ();
		if (hwndHeader != 0) {
			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageList);
		}
		updateScrollBar ();
		return index;
	}
	int index = headerImageList.indexOf (image);
	if (index != -1) return index;
	return headerImageList.add (image);
}

/**
 * Searches the receiver's list starting at the first column
 * (index 0) until a column is found that is equal to the 
 * argument, and returns the index of that column. If no column
 * is found, returns -1.
 *
 * @param column the search column
 * @return the index of the column
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<columnCount; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
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
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	return hItem == 0 ? -1 : findIndex (hItem, item.handle);
}

boolean isCustomToolTip () {
	return hooks (SWT.MeasureItem);
}

boolean isItemSelected (NMTVCUSTOMDRAW nmcd) {
	boolean selected = false;
	if (OS.IsWindowEnabled (handle)) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
		tvItem.hItem = nmcd.dwItemSpec;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & (OS.TVIS_SELECTED | OS.TVIS_DROPHILITED)) != 0) {
			selected = true;
			/*
			* Feature in Windows.  When the mouse is pressed and the
			* selection is first drawn for a tree, the previously
			* selected item is redrawn but the the TVIS_SELECTED bits
			* are not cleared.  When the user moves the mouse slightly
			* and a drag and drop operation is not started, the item is
			* drawn again and this time with TVIS_SELECTED is cleared.
			* This means that an item that contains colored cells will
			* not draw with the correct background until the mouse is
			* moved.  The fix is to test for the selection colors and
			* guess that the item is not selected.
			* 
			* NOTE: This code does not work when the foreground and
			* background of the tree are set to the selection colors
			* but this does not happen in a regular application.
			*/
			if (handle == OS.GetFocus ()) {
				if (OS.GetTextColor (nmcd.hdc) != OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT)) {
					selected = false;
				} else {
					if (OS.GetBkColor (nmcd.hdc) != OS.GetSysColor (OS.COLOR_HIGHLIGHT)) {
						selected = false;
					}
				}
			}
		} else {
			if (nmcd.dwDrawStage == OS.CDDS_ITEMPOSTPAINT) {
				/*
				* Feature in Windows.  When the mouse is pressed and the
				* selection is first drawn for a tree, the item is drawn
				* selected, but the TVIS_SELECTED bits for the item are
				* not set.  When the user moves the mouse slightly and
				* a drag and drop operation is not started, the item is
				* drawn again and this time TVIS_SELECTED is set.  This
				* means that an item that is in a tree that has the style
				* TVS_FULLROWSELECT and that also contains colored cells
				* will not draw the entire row selected until the user
				* moves the mouse.  The fix is to test for the selection
				* colors and guess that the item is selected.
				* 
				* NOTE: This code does not work when the foreground and
				* background of the tree are set to the selection colors
				* but this does not happen in a regular application.
				*/
				if (OS.GetTextColor (nmcd.hdc) == OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT)) {
					if (OS.GetBkColor (nmcd.hdc) == OS.GetSysColor (OS.COLOR_HIGHLIGHT)) {
						selected = true;
					}
				}
			}
		}
	}
	return selected;
}

void redrawSelection () {
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem != 0) {
			RECT rect = new RECT ();
			if (OS.TreeView_GetItemRect (handle, hItem, rect, false)) {
				OS.InvalidateRect (handle, rect, true);
			}
		}
	} else {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
		if (hItem != 0) {
			TVITEM tvItem = null;
			if (OS.IsWinCE) {
				tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_STATE;
			}
			RECT rect = new RECT ();
			int index = 0, count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0);
			while (index <= count && hItem != 0) {
				int state = 0;
				if (OS.IsWinCE) {
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					state = tvItem.state;
				} else {
					state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
				}
				if ((state & OS.TVIS_SELECTED) != 0) {
					if (OS.TreeView_GetItemRect (handle, hItem, rect, false)) {
						OS.InvalidateRect (handle, rect, true);
					}
				}
				hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
				index++;
			}
		}
	}
}

void register () {
	super.register ();
	if (hwndParent != 0) display.addControl (hwndParent, this);
	if (hwndHeader != 0) display.addControl (hwndHeader, this);
}

void releaseItem (int /*long*/ hItem, TVITEM tvItem, boolean release) {
	if (hItem == hAnchor) hAnchor = 0;
	if (hItem == hInsert) hInsert = 0;
	tvItem.hItem = hItem;
	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) != 0) {
		if (tvItem.lParam != -1) {
			if (tvItem.lParam < lastID) lastID = (int)/*64*/tvItem.lParam;
			if (release) {
				TreeItem item = items [(int)/*64*/tvItem.lParam];
				if (item != null) item.release (false);
			}
			items [(int)/*64*/tvItem.lParam] = null;
		}
	}
}

void releaseItems (int /*long*/ hItem, TVITEM tvItem) {
	hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
	while (hItem != 0) {
		releaseItems (hItem, tvItem);
		releaseItem (hItem, tvItem, true);
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	hwndParent = hwndHeader = 0;
}

void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TreeColumn column = columns [i];
			if (column != null && !column.isDisposed ()) {
				column.release (false);
			}
		}
		columns = null;
	}
	super.releaseChildren (destroy);
}

void releaseWidget () {
	super.releaseWidget ();
	/*
	* Feature in Windows.  For some reason, when TVM_GETIMAGELIST
	* or TVM_SETIMAGELIST is sent, the tree issues NM_CUSTOMDRAW
	* messages.  This behavior is unwanted when the tree is being
	* disposed.  The fix is to ignore NM_CUSTOMDRAW messages by
	* clearing the custom draw flag.
	* 
	* NOTE: This only happens on Windows XP.
	*/
	customDraw = false;
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
		display.releaseImageList (imageList);
	}
	if (headerImageList != null) {
		if (hwndHeader != 0) {
			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
		}
		display.releaseImageList (headerImageList);
	}
	imageList = headerImageList = null;
	int /*long*/ hStateList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, 0);
	if (hStateList != 0) OS.ImageList_Destroy (hStateList);
	if (itemToolTipHandle != 0) OS.DestroyWindow (itemToolTipHandle);
	if (headerToolTipHandle != 0) OS.DestroyWindow (headerToolTipHandle);
	itemToolTipHandle = headerToolTipHandle = 0;
}

/**
 * Removes all of the items from the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	hFirstIndexOf = hLastIndexOf = 0;
	itemCount = -1;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	ignoreDeselect = ignoreSelect = true;
	boolean redraw = getDrawing () && OS.IsWindowVisible (handle);
	if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	shrink = ignoreShrink = true;
	int /*long*/ result = OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, OS.TVI_ROOT);
	ignoreShrink = false;
	if (redraw) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);	
		OS.InvalidateRect (handle, null, true);
	}
	ignoreDeselect = ignoreSelect = false;
	if (result == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
		display.releaseImageList (imageList);
	}
	imageList = null;
	if (hwndParent == 0 && !linesVisible) {
		if (!hooks (SWT.MeasureItem) && !hooks (SWT.EraseItem) && !hooks (SWT.PaintItem)) {
			customDraw = false;
		}
	}
	hAnchor = hInsert = hFirstIndexOf = hLastIndexOf = 0;
	itemCount = -1;
	items = new TreeItem [4];
	scrollWidth = 0;
	setScrollWidth ();
	updateScrollBar ();
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) item.reskinChildren (flags);
		}
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TreeColumn column = columns [i];
			if (column != null) column.reskinChildren (flags);
		}
	}
	super.reskinChildren (flags);
}


/**
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * 
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places 
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	int /*long*/ hItem = 0;
	if (item != null) {
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		hItem = item.handle;
	}
	hInsert = hItem;
	insertAfter = !before;
	OS.SendMessage (handle, OS.TVM_SETINSERTMARK, insertAfter ? 1 : 0, hInsert);
}

/**
 * Sets the number of root-level items contained in the receiver.
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
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	setItemCount (count, OS.TVGN_ROOT, hItem);
}

void setItemCount (int count, int /*long*/ hParent, int /*long*/ hItem) {
	boolean redraw = false;
	if (OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0) == 0) {
		redraw = getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	}
	int itemCount = 0;
	while (hItem != 0 && itemCount < count) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		itemCount++;
	}
	boolean expanded = false;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	if (!redraw && (style & SWT.VIRTUAL) != 0) {
		if (OS.IsWinCE) {
			tvItem.hItem = hParent;
			tvItem.mask = OS.TVIF_STATE;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			expanded = (tvItem.state & OS.TVIS_EXPANDED) != 0;
		} else {
			/*
			* Bug in Windows.  Despite the fact that TVM_GETITEMSTATE claims
			* to return only the bits specified by the stateMask, when called
			* with TVIS_EXPANDED, the entire state is returned.  The fix is
			* to explicitly check for the TVIS_EXPANDED bit.
			*/
			int state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hParent, OS.TVIS_EXPANDED);
			expanded = (state & OS.TVIS_EXPANDED) != 0;
		}
	}
	while (hItem != 0) {
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		TreeItem item = tvItem.lParam != -1 ? items [(int)/*64*/tvItem.lParam] : null;
		if (item != null && !item.isDisposed ()) {
			item.dispose ();
		} else {
			releaseItem (tvItem.hItem, tvItem, false);
			destroyItem (null, tvItem.hItem);
		}
	}
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=itemCount; i<count; i++) {
			if (expanded) ignoreShrink = true;
			createItem (null, hParent, OS.TVI_LAST, 0);
			if (expanded) ignoreShrink = false;
		}
	} else {
		shrink = true;
		int extra = Math.max (4, (count + 3) / 4 * 4);
		TreeItem [] newItems = new TreeItem [items.length + extra];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
		for (int i=itemCount; i<count; i++) {
			new TreeItem (this, SWT.NONE, hParent, OS.TVI_LAST, 0);
		}
	}
	if (redraw) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
		OS.InvalidateRect (handle, null, true);
	}
}

/**
 * Sets the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @param itemHeight the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	OS.SendMessage (handle, OS.TVM_SETITEMHEIGHT, itemHeight, 0);
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw 
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setLinesVisible (boolean show) {
	checkWidget ();
	if (linesVisible == show) return;
	linesVisible = show;
	if (hwndParent == 0 && linesVisible) customDraw = true;
	OS.InvalidateRect (handle, null, true);
}

int /*long*/ scrolledHandle () {
	if (hwndHeader == 0) return handle;
	return columnCount == 0 && scrollWidth == 0 ? handle : hwndParent;
}

void select (int /*long*/ hItem, TVITEM tvItem) {
	while (hItem != 0) {
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		int state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_EXPANDED);
		if ((state & OS.TVIS_EXPANDED) != 0) {
			int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
			select (hFirstItem, tvItem);
		}

		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
}

/**
 * Selects an item in the receiver.  If the item was already
 * selected, it remains selected.
 *
 * @param item the item to be selected
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
 * @since 3.4
 */
public void select (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ hItem = item.handle;
		int state = 0;
		if (OS.IsWinCE) {
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = hItem;
			tvItem.mask = OS.TVIF_STATE;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			state = tvItem.state;
		} else {
			state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
		}
		if ((state & OS.TVIS_SELECTED) != 0) return;
		/*
		* Feature in Windows.  When an item is selected with
		* TVM_SELECTITEM and TVGN_CARET, the tree expands and
		* scrolls to show the new selected item.  Unfortunately,
		* there is no other way in Windows to set the focus
		* and select an item.  The fix is to save the current
		* scroll bar positions, turn off redraw, select the item,
		* then scroll back to the original position and redraw
		* the entire tree.
		*/
		SCROLLINFO hInfo = null;
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & (OS.TVS_NOHSCROLL | OS.TVS_NOSCROLL)) == 0) {
			hInfo = new SCROLLINFO ();
			hInfo.cbSize = SCROLLINFO.sizeof;
			hInfo.fMask = OS.SIF_ALL;
			OS.GetScrollInfo (handle, OS.SB_HORZ, hInfo);
		}
		SCROLLINFO vInfo = new SCROLLINFO ();
		vInfo.cbSize = SCROLLINFO.sizeof;
		vInfo.fMask = OS.SIF_ALL;
		OS.GetScrollInfo (handle, OS.SB_VERT, vInfo);
		boolean redraw = getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) {
			OS.UpdateWindow (handle);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		setSelection (item);
		if (hInfo != null) {
			int /*long*/ hThumb = OS.MAKELPARAM (OS.SB_THUMBPOSITION, hInfo.nPos);
			OS.SendMessage (handle, OS.WM_HSCROLL, hThumb, 0);
		}
		/*
		* Feature in Windows.  It seems that Vista does not
		* use wParam to get the new position when WM_VSCROLL
		* is sent with SB_THUMBPOSITION.  The fix is to use
		* SetScrollInfo() to move the scroll bar thumb before
		* calling WM_VSCROLL.
		* 
		* NOTE: This code is only necessary on Windows Vista.
		*/
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
			OS.SetScrollInfo (handle, OS.SB_VERT, vInfo, true);
		}
		int /*long*/ vThumb = OS.MAKELPARAM (OS.SB_THUMBPOSITION, vInfo.nPos);
		OS.SendMessage (handle, OS.WM_VSCROLL, vThumb, 0);
		if (redraw) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.InvalidateRect (handle, null, true);
			if ((style & SWT.DOUBLE_BUFFERED) == 0) {
				int oldStyle = style;
				style |= SWT.DOUBLE_BUFFERED;
				OS.UpdateWindow (handle);
				style = oldStyle;
			}
		}
		return;
	}
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	tvItem.state = OS.TVIS_SELECTED;
	tvItem.hItem = item.handle;
	OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.state = OS.TVIS_SELECTED;
	tvItem.stateMask = OS.TVIS_SELECTED;
	int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
	int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	select (hItem, tvItem);
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
}

Event sendEraseItemEvent (TreeItem item, NMTTCUSTOMDRAW nmcd, int column, RECT cellRect) {
	int nSavedDC = OS.SaveDC (nmcd.hdc);
	RECT insetRect = toolTipInset (cellRect);
	OS.SetWindowOrgEx (nmcd.hdc, insetRect.left, insetRect.top, null);
	GCData data = new GCData ();
	data.device = display;
	data.foreground = OS.GetTextColor (nmcd.hdc);
	data.background = OS.GetBkColor (nmcd.hdc);
	data.font = item.getFont (column);
	data.uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
	GC gc = GC.win32_new (nmcd.hdc, data);
	Event event = new Event ();
	event.item = item;
	event.index = column;
	event.gc = gc;
	event.detail |= SWT.FOREGROUND;
	event.x = cellRect.left;
	event.y = cellRect.top;
	event.width = cellRect.right - cellRect.left;
	event.height = cellRect.bottom - cellRect.top;
	//gc.setClipping (event.x, event.y, event.width, event.height);
	sendEvent (SWT.EraseItem, event);
	event.gc = null;
	//int newTextClr = data.foreground;
	gc.dispose ();
	OS.RestoreDC (nmcd.hdc, nSavedDC);
	return event;
}

Event sendMeasureItemEvent (TreeItem item, int index, int /*long*/ hDC, int detail) {
	RECT itemRect = item.getBounds (index, true, true, false, false, false, hDC);
	int nSavedDC = OS.SaveDC (hDC);
	GCData data = new GCData ();
	data.device = display;
	data.font = item.getFont (index);
	GC gc = GC.win32_new (hDC, data);
	Event event = new Event ();
	event.item = item;
	event.gc = gc;
	event.index = index;
	event.x = itemRect.left;
	event.y = itemRect.top;
	event.width = itemRect.right - itemRect.left;
	event.height = itemRect.bottom - itemRect.top;
	event.detail = detail;
	sendEvent (SWT.MeasureItem, event);
	event.gc = null;
	gc.dispose ();
	OS.RestoreDC (hDC, nSavedDC);
	if (isDisposed () || item.isDisposed ()) return null;
	if (hwndHeader != 0) {
		if (columnCount == 0) {
			if (event.x + event.width > scrollWidth) {
				setScrollWidth (scrollWidth = event.x + event.width);
			}
		}
	}
	if (event.height > getItemHeight ()) setItemHeight (event.height);
	return event;
}

Event sendPaintItemEvent (TreeItem item, NMTTCUSTOMDRAW nmcd, int column, RECT itemRect) {
	int nSavedDC = OS.SaveDC (nmcd.hdc);
	RECT insetRect = toolTipInset (itemRect);
	OS.SetWindowOrgEx (nmcd.hdc, insetRect.left, insetRect.top, null);
	GCData data = new GCData ();
	data.device = display;
	data.font = item.getFont (column);
	data.foreground = OS.GetTextColor (nmcd.hdc);
	data.background = OS.GetBkColor (nmcd.hdc);
	data.uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
	GC gc = GC.win32_new (nmcd.hdc, data);
	Event event = new Event ();
	event.item = item;
	event.index = column;
	event.gc = gc;
	event.detail |= SWT.FOREGROUND;
	event.x = itemRect.left;
	event.y = itemRect.top;
	event.width = itemRect.right - itemRect.left;
	event.height = itemRect.bottom - itemRect.top;
	//gc.setClipping (cellRect.left, cellRect.top, cellWidth, cellHeight);
	sendEvent (SWT.PaintItem, event);
	event.gc = null;
	gc.dispose ();
	OS.RestoreDC (nmcd.hdc, nSavedDC);
	return event;
}

void setBackgroundImage (int /*long*/ hBitmap) {
	super.setBackgroundImage (hBitmap);
	if (hBitmap != 0) {
		/*
		* Feature in Windows.  If TVM_SETBKCOLOR is never
		* used to set the background color of a tree, the
		* background color of the lines and the plus/minus
		* will be drawn using the default background color,
		* not the HBRUSH returned from WM_CTLCOLOR.  The fix
		* is to set the background color to the default (when
		* it is already the default) to make Windows use the
		* brush.
		*/
		if (OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0) == -1) {
			OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, -1);
		}
		_setBackgroundPixel (-1);
	} else {
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		if (control.backgroundImage == null) {
			setBackgroundPixel (control.getBackgroundPixel ());
		}
	}
	/*
	* Feature in Windows.  When the tree has the style
	* TVS_FULLROWSELECT, the background color for the
	* entire row is filled when an item is painted,
	* drawing on top of the background image.  The fix
	* is to clear TVS_FULLROWSELECT when a background
	* image is set.
	*/
	updateFullSelection ();
}

void setBackgroundPixel (int pixel) {
	Control control = findImageControl ();
	if (control != null) {
		setBackgroundImage (control.backgroundImage);
		return;
	}
	/*
	* Feature in Windows.  When a tree is given a background color
	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
	* the tree using the background color rather than the disabled
	* colors.  This is different from the table which draws grayed.
	* The fix is to set the default background color while the tree
	* is disabled and restore it when enabled.
	*/
	if (OS.IsWindowEnabled (handle)) _setBackgroundPixel (pixel);

	/*
	* Feature in Windows.  When the tree has the style
	* TVS_FULLROWSELECT, the background color for the
	* entire row is filled when an item is painted,
	* drawing on top of the background image.  The fix
	* is to restore TVS_FULLROWSELECT when a background
	* color is set.
	*/
	updateFullSelection ();
}

void setCursor () {
	/*
	* Bug in Windows.  Under certain circumstances, when WM_SETCURSOR
	* is sent from SendMessage(), Windows GP's in the window proc for
	* the tree.  The fix is to avoid calling the tree window proc and
	* set the cursor for the tree outside of WM_SETCURSOR.
	* 
	* NOTE:  This code assumes that the default cursor for the tree
	* is IDC_ARROW.
	*/
	Cursor cursor = findCursor ();
	int /*long*/ hCursor = cursor == null ? OS.LoadCursor (0, OS.IDC_ARROW) : cursor.handle;
	OS.SetCursor (hCursor);
}

/**
 * Sets the order that the items in the receiver should 
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param order the new order to display the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
 * </ul>
 * 
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public void setColumnOrder (int [] order) {
	checkWidget ();
	if (order == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (columnCount == 0) {
		if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
		return;
	}
	if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] oldOrder = new int [columnCount];
	OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, oldOrder);
	boolean reorder = false;
	boolean [] seen = new boolean [columnCount];
	for (int i=0; i<order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= columnCount) error (SWT.ERROR_INVALID_RANGE);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
		if (index != oldOrder [i]) reorder = true;
	}
	if (reorder) {
		RECT [] oldRects = new RECT [columnCount];
		for (int i=0; i<columnCount; i++) {
			oldRects [i] = new RECT ();
			OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, oldRects [i]);
		}
		OS.SendMessage (hwndHeader, OS.HDM_SETORDERARRAY, order.length, order);
		OS.InvalidateRect (handle, null, true);
		updateImageList ();
		TreeColumn [] newColumns = new TreeColumn [columnCount];
		System.arraycopy (columns, 0, newColumns, 0, columnCount);
		RECT newRect = new RECT ();
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = newColumns [i];
			if (!column.isDisposed ()) {
				OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, newRect);
				if (newRect.left != oldRects [i].left) {
					column.updateToolTip (i);
					column.sendEvent (SWT.Move);
				}
			}
		}
	}
}

void setCheckboxImageList () {
	if ((style & SWT.CHECK) == 0) return;
	int count = 5, flags = 0;
	if (OS.IsWinCE) {
		flags |= OS.ILC_COLOR;
	} else {
		if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
			flags |= OS.ILC_COLOR32;
		} else {
			int /*long*/ hDC = OS.GetDC (handle);
			int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
			int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
			OS.ReleaseDC (handle, hDC);
			int depth = bits * planes;
			switch (depth) {
				case 4: flags |= OS.ILC_COLOR4; break;
				case 8: flags |= OS.ILC_COLOR8; break;
				case 16: flags |= OS.ILC_COLOR16; break;
				case 24: flags |= OS.ILC_COLOR24; break;
				case 32: flags |= OS.ILC_COLOR32; break;
				default: flags |= OS.ILC_COLOR; break;
			}
			flags |= OS.ILC_MASK;
		}
	}
	if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.ILC_MIRROR;
	int height = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0), width = height;
	int /*long*/ hStateList = OS.ImageList_Create (width, height, flags, count, count);
	int /*long*/ hDC = OS.GetDC (handle);
	int /*long*/ memDC = OS.CreateCompatibleDC (hDC);
	int /*long*/ hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
	int /*long*/ hOldBitmap = OS.SelectObject (memDC, hBitmap);
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, width * count, height);
	/*
	* NOTE: DrawFrameControl() draws a black and white
	* mask when not drawing a push button.  In order to
	* make the box surrounding the check mark transparent,
	* fill it with a color that is neither black or white.
	*/
	int clrBackground = 0;
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		clrBackground = control.getBackgroundPixel ();
	} else {
		clrBackground = 0x020000FF;
		if ((clrBackground & 0xFFFFFF) == OS.GetSysColor (OS.COLOR_WINDOW)) {
			clrBackground = 0x0200FF00;
		}
	}
	int /*long*/ hBrush = OS.CreateSolidBrush (clrBackground);
	OS.FillRect (memDC, rect, hBrush);
	OS.DeleteObject (hBrush);
	int /*long*/ oldFont = OS.SelectObject (hDC, defaultFont ());
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	OS.SelectObject (hDC, oldFont);
	int itemWidth = Math.min (tm.tmHeight, width);
	int itemHeight = Math.min (tm.tmHeight, height);
	int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
	OS.SetRect (rect, left + width, top, left + width + itemWidth, top + itemHeight);
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int /*long*/ hTheme = display.hButtonTheme ();
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_CHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_MIXEDNORMAL, rect, null);
	} else {
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	}
	OS.SelectObject (memDC, hOldBitmap);
	OS.DeleteDC (memDC);
	OS.ReleaseDC (handle, hDC);
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		OS.ImageList_Add (hStateList, hBitmap, 0);
	} else {
		OS.ImageList_AddMasked (hStateList, hBitmap, clrBackground);
	}
	OS.DeleteObject (hBitmap);
	int /*long*/ hOldStateList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, hStateList);
	if (hOldStateList != 0) OS.ImageList_Destroy (hOldStateList);
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
}

void setForegroundPixel (int pixel) {
	/*
	* Bug in Windows.  When the tree is using the explorer
	* theme, it does not use COLOR_WINDOW_TEXT for the
	* foreground.  When TVM_SETTEXTCOLOR is called with -1,
	* it resets the color to black, not COLOR_WINDOW_TEXT.
	* The fix is to explicitly set the color.
	*/
	if (explorerTheme) {
		if (pixel == -1) pixel = defaultForeground ();
	}
	OS.SendMessage (handle, OS.TVM_SETTEXTCOLOR, 0, pixel);
}

/**
 * Marks the receiver's header as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	if (hwndHeader == 0) {
		if (!show) return;
		createParent ();
	}
	int bits = OS.GetWindowLong (hwndHeader, OS.GWL_STYLE);
	if (show) {
		if ((bits & OS.HDS_HIDDEN) == 0) return;
		bits &= ~OS.HDS_HIDDEN;
		OS.SetWindowLong (hwndHeader, OS.GWL_STYLE, bits);
		OS.ShowWindow (hwndHeader, OS.SW_SHOW);
	} else {
		if ((bits & OS.HDS_HIDDEN) != 0) return;
		bits |= OS.HDS_HIDDEN;
		OS.SetWindowLong (hwndHeader, OS.GWL_STYLE, bits);
		OS.ShowWindow (hwndHeader, OS.SW_HIDE);
	}
	setScrollWidth ();
	updateHeaderToolTips ();
	updateScrollBar ();
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	/*
	* Feature in Windows.  When WM_SETREDRAW is used to
	* turn off redraw, the scroll bars are updated when
	* items are added and removed.  The fix is to call
	* the default window proc to stop all drawing.
	* 
	* Bug in Windows.  For some reason, when WM_SETREDRAW
	* is used to turn redraw on for a tree and the tree
	* contains no items, the last item in the tree does
	* not redraw properly.  If the tree has only one item,
	* that item is not drawn.  If another window is dragged
	* on top of the item, parts of the item are redrawn
	* and erased at random.  The fix is to ensure that this
	* case doesn't happen by inserting and deleting an item
	* when redraw is turned on and there are no items in
	* the tree.
	*/
	int /*long*/ hItem = 0;
	if (redraw) {
		if (drawCount == 1) {
			int count = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
			if (count == 0) {
				TVINSERTSTRUCT tvInsert = new TVINSERTSTRUCT ();
				tvInsert.hInsertAfter = OS.TVI_FIRST;
				hItem = OS.SendMessage (handle, OS.TVM_INSERTITEM, 0, tvInsert);
			}
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			updateScrollBar ();
		}
	}
	super.setRedraw (redraw);
	if (!redraw) {
		if (drawCount == 1) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	}
	if (hItem != 0) {
		ignoreShrink = true;
		OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
		ignoreShrink = false;
	}
}

void setScrollWidth () {
	if (hwndHeader == 0 || hwndParent == 0) return;
	int width = 0;
	HDITEM hdItem = new HDITEM ();
	for (int i=0; i<columnCount; i++) {
		hdItem.mask = OS.HDI_WIDTH;
		OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
		width += hdItem.cxy;
	}
	setScrollWidth (Math.max (scrollWidth, width));
}

void setScrollWidth (int width) {
	if (hwndHeader == 0 || hwndParent == 0) return;
	//TEMPORARY CODE
	//scrollWidth = width;
	int left = 0;
	RECT rect = new RECT ();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
	if (columnCount == 0 && width == 0) {
		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
		info.nPage = info.nMax + 1;
		OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
		OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
		info.nPage = info.nMax + 1;
		OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
	} else {
		if ((style & SWT.H_SCROLL) != 0) {
			OS.GetClientRect (hwndParent, rect);
			OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
			info.nMax = width;
			info.nPage = rect.right - rect.left + 1;
			OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
			info.fMask = OS.SIF_POS;
			OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
			left = info.nPos;
		}
	}
	if (horizontalBar != null) {
		horizontalBar.setIncrement (INCREMENT);
		horizontalBar.setPageIncrement (info.nPage);
	}
	OS.GetClientRect (hwndParent, rect);
	int /*long*/ hHeap = OS.GetProcessHeap ();
	HDLAYOUT playout = new HDLAYOUT ();
	playout.prc = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, RECT.sizeof);
	playout.pwpos = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, WINDOWPOS.sizeof);
	OS.MoveMemory (playout.prc, rect, RECT.sizeof);
	OS.SendMessage (hwndHeader, OS.HDM_LAYOUT, 0, playout);
	WINDOWPOS pos = new WINDOWPOS ();
	OS.MoveMemory (pos, playout.pwpos, WINDOWPOS.sizeof);
	if (playout.prc != 0) OS.HeapFree (hHeap, 0, playout.prc);
	if (playout.pwpos != 0) OS.HeapFree (hHeap, 0, playout.pwpos);
	SetWindowPos (hwndHeader, OS.HWND_TOP, pos.x - left, pos.y, pos.cx + left, pos.cy, OS.SWP_NOACTIVATE);
	int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	int b = (bits & OS.WS_EX_CLIENTEDGE) != 0 ? OS.GetSystemMetrics (OS.SM_CXEDGE) : 0;
	int w = pos.cx + (columnCount == 0 && width == 0 ? 0 : OS.GetSystemMetrics (OS.SM_CXVSCROLL));
	int h = rect.bottom - rect.top - pos.cy;
	boolean oldIgnore = ignoreResize;
	ignoreResize = true;
	SetWindowPos (handle, 0, pos.x - left - b, pos.y + pos.cy - b, w + left + b * 2, h + b * 2, OS.SWP_NOACTIVATE | OS.SWP_NOZORDER);
	ignoreResize = oldIgnore;
}

void setSelection (int /*long*/ hItem, TVITEM tvItem, TreeItem [] selection) {
	while (hItem != 0) {
		int index = 0;
		while (index < selection.length) {
			TreeItem item = selection [index];
			if (item != null && item.handle == hItem) break;
			index++;
		}
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
			if (index == selection.length) {
				tvItem.state = 0;
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			}
		} else {
			if (index != selection.length) {
				tvItem.state = OS.TVIS_SELECTED;
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			}
		}
		int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
		setSelection (hFirstItem, tvItem, selection);
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected.
 * <p>
 * If the item is not in the receiver, then it is ignored.
 * </p>
 *
 * @param item the item to select
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
 * @since 3.2
 */
public void setSelection (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TreeItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 * </p>
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll();
		return;
	}
		
	/* Select/deselect the first item */
	TreeItem item = items [0];
	if (item != null) {
		if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		int /*long*/ hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		int /*long*/ hNewItem = hAnchor = item.handle;
		
		/*
		* Bug in Windows.  When TVM_SELECTITEM is used to select and
		* scroll an item to be visible and the client area of the tree
		* is smaller that the size of one item, TVM_SELECTITEM makes
		* the next item in the tree visible by making it the top item
		* instead of making the desired item visible.  The fix is to
		* detect the case when the client area is too small and make
		* the desired visible item be the top item in the tree.
		* 
		* Note that TVM_SELECTITEM when called with TVGN_FIRSTVISIBLE
		* also requires the work around for scrolling.
		*/
		boolean fixScroll = checkScroll (hNewItem);
		if (fixScroll) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		ignoreSelect = true;
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
		ignoreSelect = false;
		if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
			OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hNewItem);
			int /*long*/ hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hNewItem);
			if (hParent == 0) OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
		}
		if (fixScroll) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		}
		
		/*
		* Feature in Windows.  When the old and new focused item
		* are the same, Windows does not check to make sure that
		* the item is actually selected, not just focused.  The
		* fix is to force the item to draw selected by setting
		* the state mask, and to ensure that it is visible.
		*/
		if (hOldItem == hNewItem) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			showItem (hNewItem);
		}
	}
	if ((style & SWT.SINGLE) != 0) return;

	/* Select/deselect the rest of the items */
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
	if ((style & SWT.VIRTUAL) != 0) {
		int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		setSelection (hItem, tvItem, items);
	} else {
		for (int i=0; i<this.items.length; i++) {
			item = this.items [i];
			if (item != null) {
				int index = 0;
				while (index < length) {
					if (items [index] == item) break;
					index++;
				}
				tvItem.hItem = item.handle;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
					if (index == length) {
						tvItem.state = 0;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				} else {
					if (index != length) {
						tvItem.state = OS.TVIS_SELECTED;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
		}
	}
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
}

/**
 * Sets the column used by the sort indicator for the receiver. A null
 * value will clear the sort indicator.  The current sort column is cleared 
 * before the new column is set.
 *
 * @param column the column used by the sort indicator or <code>null</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSortColumn (TreeColumn column) {
	checkWidget ();
	if (column != null && column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (sortColumn != null && !sortColumn.isDisposed ()) {
		sortColumn.setSortDirection (SWT.NONE);
	}
	sortColumn = column;
	if (sortColumn != null && sortDirection != SWT.NONE) {
		sortColumn.setSortDirection (sortDirection);
	}
}

/**
 * Sets the direction of the sort indicator for the receiver. The value 
 * can be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
 *
 * @param direction the direction of the sort indicator 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSortDirection (int direction) {
	checkWidget ();
	if ((direction & (SWT.UP | SWT.DOWN)) == 0 && direction != SWT.NONE) return;
	sortDirection = direction;
	if (sortColumn != null && !sortColumn.isDisposed ()) {
		sortColumn.setSortDirection (direction);
	}
}

/**
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @param item the item to be shown
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
 * @see Tree#getTopItem()
 * 
 * @since 2.1
 */
public void setTopItem (TreeItem item) {
	checkWidget ();
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ hItem = item.handle;
	int /*long*/ hTopItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
	if (hItem == hTopItem) return;
	boolean fixScroll = checkScroll (hItem), redraw = false;
	if (fixScroll) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	} else {
		redraw = getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	}
	SCROLLINFO hInfo = null;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int /*long*/ hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
	if (hParent != 0 && (bits & (OS.TVS_NOHSCROLL | OS.TVS_NOSCROLL)) == 0) {
		hInfo = new SCROLLINFO ();
		hInfo.cbSize = SCROLLINFO.sizeof;
		hInfo.fMask = OS.SIF_ALL;
		OS.GetScrollInfo (handle, OS.SB_HORZ, hInfo);
	}
	OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
	if (hParent != 0) {
		if (hInfo != null) {
			int /*long*/ hThumb = OS.MAKELPARAM (OS.SB_THUMBPOSITION, hInfo.nPos);
			OS.SendMessage (handle, OS.WM_HSCROLL, hThumb, 0);
		}
	} else {
		OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
	}
	if (fixScroll) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	} else {
		if (redraw) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.InvalidateRect (handle, null, true);
		}
	}
	updateScrollBar ();
}

void showItem (int /*long*/ hItem) {
	/*
	* Bug in Windows.  When TVM_ENSUREVISIBLE is used to ensure
	* that an item is visible and the client area of the tree is
	* smaller that the size of one item, TVM_ENSUREVISIBLE makes
	* the next item in the tree visible by making it the top item
	* instead of making the desired item visible.  The fix is to
	* detect the case when the client area is too small and make
	* the desired visible item be the top item in the tree.
	*/
	if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
		boolean fixScroll = checkScroll (hItem);
		if (fixScroll) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
		/* This code is intentionally commented */
		//int hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
		//if (hParent == 0) OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
		OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
		if (fixScroll) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		}
	} else {
		boolean scroll = true;
		RECT itemRect = new RECT ();
		if (OS.TreeView_GetItemRect (handle, hItem, itemRect, true)) {
			forceResize ();
			RECT rect = new RECT ();
			OS.GetClientRect (handle, rect);
			POINT pt = new POINT ();
			pt.x = itemRect.left;
			pt.y = itemRect.top;
			if (OS.PtInRect (rect, pt)) {
				pt.y = itemRect.bottom;
				if (OS.PtInRect (rect, pt)) scroll = false;
			}
		}
		if (scroll) {
			boolean fixScroll = checkScroll (hItem);
			if (fixScroll) {
				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
			}
			OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
			if (fixScroll) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
			}
		}
	}
	if (hwndParent != 0) {
		RECT itemRect = new RECT ();
		if (OS.TreeView_GetItemRect (handle, hItem, itemRect, true)) {
			forceResize ();
			RECT rect = new RECT ();
			OS.GetClientRect (hwndParent, rect);
			OS.MapWindowPoints (hwndParent, handle, rect, 2);
			POINT pt = new POINT ();
			pt.x = itemRect.left;
			pt.y = itemRect.top;
			if (!OS.PtInRect (rect, pt)) {
				pt.y = itemRect.bottom;
				if (!OS.PtInRect (rect, pt)) {
					SCROLLINFO info = new SCROLLINFO ();
					info.cbSize = SCROLLINFO.sizeof;
					info.fMask = OS.SIF_POS;
					info.nPos = Math.max (0, pt.x - Tree.INSET / 2);
					OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
					setScrollWidth ();
				}
			}
		}
	}
	updateScrollBar ();
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
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
public void showColumn (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (index == -1) return;
	if (0 <= index && index < columnCount) {
		forceResize ();
		RECT rect = new RECT ();
		OS.GetClientRect (hwndParent, rect);
		OS.MapWindowPoints (hwndParent, handle, rect, 2);
		RECT headerRect = new RECT ();
		OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect);
		boolean scroll = headerRect.left < rect.left;
		if (!scroll) {
			int width = Math.min (rect.right - rect.left, headerRect.right - headerRect.left);
			scroll = headerRect.left + width > rect.right;
		}
		if (scroll) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_POS;
			info.nPos = Math.max (0, headerRect.left - Tree.INSET / 2);
			OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
			setScrollWidth ();
		}
	}
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
 *
 * @param item the item to be shown
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
 * @see Tree#showSelection()
 */
public void showItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	showItem (item.handle);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget ();
	int /*long*/ hItem = 0;
	if ((style & SWT.SINGLE) != 0) {	
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return;
		int state = 0;
		if (OS.IsWinCE) {
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = hItem;
			tvItem.mask = OS.TVIF_STATE;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			state = tvItem.state;
		} else {
			state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, hItem, OS.TVIS_SELECTED);
		}
		if ((state & OS.TVIS_SELECTED) == 0) return;
	} else {
		int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
		OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
		TVITEM tvItem = null;
		if (OS.IsWinCE) {
			tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
		}
		if ((style & SWT.VIRTUAL) != 0) {
			int /*long*/ hRoot = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
			hItem = getNextSelection (hRoot, tvItem);
		} else {
			//FIXME - this code expands first selected item it finds
			int index = 0;
			while (index <items.length) {
				TreeItem item = items [index];
				if (item != null) {
					int state = 0;
					if (OS.IsWinCE) {
						tvItem.hItem = item.handle;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						state = tvItem.state;
					} else {
						state = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETITEMSTATE, item.handle, OS.TVIS_SELECTED);
					}
					if ((state & OS.TVIS_SELECTED) != 0) {
						hItem = item.handle;
						break;
					}
				}
				index++;
			}
		}
		OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
	}
	if (hItem != 0) showItem (hItem);
}

/*public*/ void sort () {
	checkWidget ();
	if ((style & SWT.VIRTUAL) != 0) return;
	sort (OS.TVI_ROOT, false);
}

void sort (int /*long*/ hParent, boolean all) {
	int itemCount = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
	if (itemCount == 0 || itemCount == 1) return;
	hFirstIndexOf = hLastIndexOf = 0;
	itemCount = -1;
	if (sortDirection == SWT.UP || sortDirection == SWT.NONE) {
		OS.SendMessage (handle, OS.TVM_SORTCHILDREN, all ? 1 : 0, hParent);
	} else {
		Callback compareCallback = new Callback (this, "CompareFunc", 3);
		int /*long*/ lpfnCompare = compareCallback.getAddress ();
		TVSORTCB psort = new TVSORTCB ();
		psort.hParent = hParent;
		psort.lpfnCompare = lpfnCompare;
		psort.lParam = sortColumn == null ? 0 : indexOf (sortColumn);
		OS.SendMessage (handle, OS.TVM_SORTCHILDRENCB, all ? 1 : 0, psort);
		compareCallback.dispose ();
	}
}

void subclass () {
	super.subclass ();
	if (hwndHeader != 0) {
		OS.SetWindowLongPtr (hwndHeader, OS.GWLP_WNDPROC, display.windowProc);
	}
}

RECT toolTipInset (RECT rect) {
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		RECT insetRect = new RECT ();
		OS.SetRect (insetRect, rect.left - 1, rect.top - 1, rect.right + 1, rect.bottom + 1);
		return insetRect;
	}
	return rect;
}

RECT toolTipRect (RECT rect) {
	RECT toolRect = new RECT ();
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		OS.SetRect (toolRect, rect.left - 1, rect.top - 1, rect.right + 1, rect.bottom + 1);
	} else {
		OS.SetRect (toolRect, rect.left, rect.top, rect.right, rect.bottom);
		int dwStyle = OS.GetWindowLong (itemToolTipHandle, OS.GWL_STYLE);
		int dwExStyle = OS.GetWindowLong (itemToolTipHandle, OS.GWL_EXSTYLE);
		OS.AdjustWindowRectEx (toolRect, dwStyle, false, dwExStyle);
	}
	return toolRect;
}

String toolTipText (NMTTDISPINFO hdr) {
	int /*long*/ hwndToolTip = OS.SendMessage (handle, OS.TVM_GETTOOLTIPS, 0, 0);
	if (hwndToolTip == hdr.hwndFrom && toolTipText != null) return ""; //$NON-NLS-1$
	if (headerToolTipHandle == hdr.hwndFrom) {
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = columns [i];
			if (column.id == hdr.idFrom) return column.toolTipText;
		}
		return super.toolTipText (hdr);
	}
	if (itemToolTipHandle == hdr.hwndFrom) {
		if (toolTipText != null) return "";
		int pos = OS.GetMessagePos ();
		POINT pt = new POINT();
		OS.POINTSTOPOINT (pt, pos);
		OS.ScreenToClient (handle, pt);
		int [] index = new int [1];
		TreeItem [] item = new TreeItem [1];
		RECT [] cellRect = new RECT [1], itemRect = new RECT [1];
		if (findCell (pt.x, pt.y, item, index, cellRect, itemRect)) {
			String text = null;
			if (index [0] == 0) {
				text = item [0].text;
			} else {
				String[] strings = item [0].strings;
				if (strings != null) text = strings [index [0]];
			}
			//TEMPORARY CODE
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				if (isCustomToolTip ()) text = " ";
			}
			if (text != null) return text;
		}
	}
	return super.toolTipText (hdr);
}

int /*long*/ topHandle () {
	return hwndParent != 0 ? hwndParent : handle;
}

void updateFullSelection () {
	if ((style & SWT.FULL_SELECTION) != 0) {
		int oldBits = OS.GetWindowLong (handle, OS.GWL_STYLE), newBits = oldBits;
		if ((newBits & OS.TVS_FULLROWSELECT) != 0) {
			if (!OS.IsWindowEnabled (handle) || findImageControl () != null) {
				if (!explorerTheme) newBits &= ~OS.TVS_FULLROWSELECT;
			}
		} else {
			if (OS.IsWindowEnabled (handle) && findImageControl () == null) {
				if (!hooks (SWT.EraseItem) && !hooks (SWT.PaintItem)) {
					newBits |= OS.TVS_FULLROWSELECT;
				}
			}
		}
		if (newBits != oldBits) {
			OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
			OS.InvalidateRect (handle, null, true);
		}
	}
}

void updateHeaderToolTips () {
	if (headerToolTipHandle == 0) return;
	RECT rect = new RECT ();
	TOOLINFO lpti = new TOOLINFO ();
	lpti.cbSize = TOOLINFO.sizeof;
	lpti.uFlags = OS.TTF_SUBCLASS;
	lpti.hwnd = hwndHeader;
	lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = columns [i];
		if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, rect) != 0) {
			lpti.uId = column.id = display.nextToolTipId++;
			lpti.left = rect.left;
			lpti.top = rect.top;
			lpti.right = rect.right;
			lpti.bottom = rect.bottom;
			OS.SendMessage (headerToolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
		}
	}
}

void updateImageList () {
	if (imageList == null) return;
	if (hwndHeader == 0) return;
	int i = 0, index = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0);
	while (i < items.length) {
		TreeItem item = items [i];
		if (item != null) {
			Image image = null;
			if (index == 0) {
				image = item.image;
			} else {
				Image [] images = item.images;
				if (images != null) image = images [index];
			}
			if (image != null) break;
		}
		i++;
	}
	/*
	* Feature in Windows.  When setting the same image list multiple
	* times, Windows does work making this operation slow.  The fix
	* is to test for the same image list before setting the new one.
	*/
	int /*long*/ hImageList = i == items.length ? 0 : imageList.getHandle ();
	int /*long*/ hOldImageList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
	if (hImageList != hOldImageList) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
	}
}

void updateImages () {
	if (sortColumn != null && !sortColumn.isDisposed ()) {
		if (OS.COMCTL32_MAJOR < 6) {
			switch (sortDirection) {
				case SWT.UP:
				case SWT.DOWN:
					sortColumn.setImage (display.getSortImage (sortDirection), true, true);
					break;
			}
		}
	}
}

void updateOrientation () {
	super.updateOrientation ();
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	OS.SetWindowPos (handle, 0, 0, 0, width - 1, height - 1, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	OS.SetWindowPos (handle, 0, 0, 0, width, height, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	if (hwndParent != 0) {
		int bits = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			bits |= OS.WS_EX_LAYOUTRTL;
		} else {
			bits &= ~OS.WS_EX_LAYOUTRTL;
		}
		OS.SetWindowLong (hwndParent, OS.GWL_EXSTYLE, bits);
		rect = new RECT ();
		OS.GetWindowRect (hwndParent, rect);
		width = rect.right - rect.left; height = rect.bottom - rect.top;
		OS.SetWindowPos (hwndParent, 0, 0, 0, width - 1, height - 1, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
		OS.SetWindowPos (hwndParent, 0, 0, 0, width, height, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	}
	if (hwndHeader != 0) {
		int bits = OS.GetWindowLong (hwndHeader, OS.GWL_EXSTYLE);
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			bits |= OS.WS_EX_LAYOUTRTL;
		} else {
			bits &= ~OS.WS_EX_LAYOUTRTL;
		}
		OS.SetWindowLong (hwndHeader, OS.GWL_EXSTYLE, bits);
		OS.InvalidateRect (hwndHeader, null, true);
	}
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	if (imageList != null) {
		Point size = imageList.getImageSize ();
		display.releaseImageList (imageList);
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, size.x, size.y);
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			if (item != null) {
				Image image = item.image;
				if (image != null) {
					int index = imageList.indexOf (image);
					if (index == -1) imageList.add (image);	
				}
			}
		}
		int /*long*/ hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
	}
	if (hwndHeader != 0) {
		if (headerImageList != null) {
			Point size = headerImageList.getImageSize ();
			display.releaseImageList (headerImageList);
			headerImageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, size.x, size.y);	
			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					TreeColumn column = columns[i];
					if (column != null) {
						Image image = column.image;
						if (image != null) {
							HDITEM hdItem = new HDITEM ();
							hdItem.mask = OS.HDI_FORMAT;
							OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
							if ((hdItem.fmt & OS.HDF_IMAGE)!= 0) {      
								int index = headerImageList.indexOf (image);
								if (index == -1) index = headerImageList.add (image);	
								hdItem.mask = OS.HDI_IMAGE;
								hdItem.iImage = index;
								OS.SendMessage (hwndHeader, OS.HDM_SETITEM, i, hdItem);
							}
						}
					}	
				}
			}
			int /*long*/ hImageListHeader = headerImageList.getHandle ();
			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageListHeader);
		}	
	}
}

void updateScrollBar () {
	if (hwndParent != 0) {
		if (columnCount != 0 || scrollWidth != 0) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_ALL;
			int itemCount = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
			if (itemCount == 0) {
				OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
				info.nPage = info.nMax + 1;
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
			} else {
				OS.GetScrollInfo (handle, OS.SB_VERT, info);
				if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(4, 10)) {
					if (info.nPage == 0) {
						SCROLLBARINFO psbi = new SCROLLBARINFO ();
						psbi.cbSize = SCROLLBARINFO.sizeof;
						OS.GetScrollBarInfo (handle, OS.OBJID_VSCROLL, psbi);
						if ((psbi.rgstate [0] & OS.STATE_SYSTEM_INVISIBLE) != 0) {
							info.nPage = info.nMax + 1;
						}
					}
				}
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
			}
		}
	}
}

void unsubclass () {
	super.unsubclass ();
	if (hwndHeader != 0) {
		OS.SetWindowLongPtr (hwndHeader, OS.GWLP_WNDPROC, HeaderProc);
	}
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.TVS_SHOWSELALWAYS | OS.TVS_LINESATROOT | OS.TVS_HASBUTTONS | OS.TVS_NONEVENHEIGHT;
	if (EXPLORER_THEME && !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0) && OS.IsAppThemed ()) {
		bits |= OS.TVS_TRACKSELECT;
		if ((style & SWT.FULL_SELECTION) != 0) bits |= OS.TVS_FULLROWSELECT;
	} else {
		if ((style & SWT.FULL_SELECTION) != 0) {
			bits |= OS.TVS_FULLROWSELECT;
		} else {
			bits |= OS.TVS_HASLINES;
		}
	}
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		bits &= ~(OS.WS_HSCROLL | OS.WS_VSCROLL);
		bits |= OS.TVS_NOSCROLL;
	} else {
		if ((style & SWT.H_SCROLL) == 0) {
			bits &= ~OS.WS_HSCROLL;
			bits |= OS.TVS_NOHSCROLL;
		}
	}
//	bits |= OS.TVS_NOTOOLTIPS | OS.TVS_DISABLEDRAGDROP;
	return bits | OS.TVS_DISABLEDRAGDROP;
}

TCHAR windowClass () {
	return TreeClass;
}

int /*long*/ windowProc () {
	return TreeProc;
}

int /*long*/ windowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (hwndHeader != 0 && hwnd == hwndHeader) {
		switch (msg) {
			case OS.WM_CONTEXTMENU: {
				LRESULT result = wmContextMenu (hwnd, wParam, lParam);
				if (result != null) return result.value;
				break;
			}
			case OS.WM_CAPTURECHANGED: {
				/*
				* Bug in Windows.  When the capture changes during a
				* header drag, Windows does not redraw the header item
				* such that the header remains pressed.  For example,
				* when focus is assigned to a push button, the mouse is
				* pressed (but not released), then the SPACE key is
				* pressed to activate the button, the capture changes,
				* the header not notified and NM_RELEASEDCAPTURE is not
				* sent.  The fix is to redraw the header when the capture
				* changes to another control.
				* 
				* This does not happen on XP.
				*/
				if (OS.COMCTL32_MAJOR < 6) {
					if (lParam != 0 && lParam != hwndHeader) {
						OS.InvalidateRect (hwndHeader, null, true);
					}
				}
				break;
			}
			case OS.WM_MOUSELEAVE: {
				/*
				* Bug in Windows.  On XP, when a tooltip is hidden
				* due to a time out or mouse press, the tooltip
				* remains active although no longer visible and
				* won't show again until another tooltip becomes
				* active.  The fix is to reset the tooltip bounds.
				*/
				if (OS.COMCTL32_MAJOR >= 6) updateHeaderToolTips ();
				updateHeaderToolTips ();
				break;
			}
			case OS.WM_NOTIFY: {
				NMHDR hdr = new NMHDR ();
				OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
				switch (hdr.code) {
					case OS.TTN_SHOW:
					case OS.TTN_POP: 
					case OS.TTN_GETDISPINFOA:
					case OS.TTN_GETDISPINFOW:
						return OS.SendMessage (handle, msg, wParam, lParam);
				}
				break;
			}
			case OS.WM_SETCURSOR: {
				if (wParam == hwnd) {
					int hitTest = (short) OS.LOWORD (lParam);
					if (hitTest == OS.HTCLIENT) {
						HDHITTESTINFO pinfo = new HDHITTESTINFO ();
						int pos = OS.GetMessagePos ();
						POINT pt = new POINT ();
						OS.POINTSTOPOINT (pt, pos);
						OS.ScreenToClient (hwnd, pt);
						pinfo.x = pt.x;
						pinfo.y = pt.y;
						int index = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_HITTEST, 0, pinfo);
						if (0 <= index && index < columnCount && !columns [index].resizable) {
							if ((pinfo.flags & (OS.HHT_ONDIVIDER | OS.HHT_ONDIVOPEN)) != 0) {
								OS.SetCursor (OS.LoadCursor (0, OS.IDC_ARROW));
								return 1;
							}
						}
					}
				}
				break;
			}
		}
		return callWindowProc (hwnd, msg, wParam, lParam);
	}
	if (hwndParent != 0 && hwnd == hwndParent) {
		switch (msg) {
			case OS.WM_MOVE: {
				sendEvent (SWT.Move);
				return 0;
			}
			case OS.WM_SIZE: {
				setScrollWidth ();
				if (ignoreResize) return 0;
				setResizeChildren (false);
				int /*long*/ code = callWindowProc (hwnd, OS.WM_SIZE, wParam, lParam);
				sendEvent (SWT.Resize);
				if (isDisposed ()) return 0;
				if (layout != null) {
					markLayout (false, false);
					updateLayout (false, false);
				}
				setResizeChildren (true);
				updateScrollBar ();
				return code;
			}
			case OS.WM_NCPAINT: {
				LRESULT result = wmNCPaint (hwnd, wParam, lParam);
				if (result != null) return result.value;
				break;
			}
			case OS.WM_PRINT: {
				LRESULT result = wmPrint (hwnd, wParam, lParam);
				if (result != null) return result.value;
				break;
			}
			case OS.WM_COMMAND:
			case OS.WM_NOTIFY:
			case OS.WM_SYSCOLORCHANGE: {
				return OS.SendMessage (handle, msg, wParam, lParam);
			}
			case OS.WM_HSCROLL: {
				/*
				* Bug on WinCE.  lParam should be NULL when the message is not sent
				* by a scroll bar control, but it contains the handle to the window.
				* When the message is sent by a scroll bar control, it correctly
				* contains the handle to the scroll bar.  The fix is to check for
				* both.
				*/
				if (horizontalBar != null && (lParam == 0 || lParam == hwndParent)) {
					wmScroll (horizontalBar, true, hwndParent, OS.WM_HSCROLL, wParam, lParam);
				}
				setScrollWidth ();
				break;
			}
			case OS.WM_VSCROLL: {
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_ALL;
				OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
				/*
				* Update the nPos field to match the nTrackPos field
				* so that the tree scrolls when the scroll bar of the
				* parent is dragged.
				* 
				* NOTE: For some reason, this code is only necessary
				* on Windows Vista.
				*/
				if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
					if (OS.LOWORD (wParam) == OS.SB_THUMBTRACK) {
						info.nPos = info.nTrackPos;
					}
				}
				OS.SetScrollInfo (handle, OS.SB_VERT, info, true);
				int /*long*/ code = OS.SendMessage (handle, OS.WM_VSCROLL, wParam, lParam);
				OS.GetScrollInfo (handle, OS.SB_VERT, info);
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
				return code;
			}
		}
		return callWindowProc (hwnd, msg, wParam, lParam);
	}
	if (msg == Display.DI_GETDRAGIMAGE) {
		/*
		* When there is more than one item selected, DI_GETDRAGIMAGE
		* returns the item under the cursor.  This happens because
		* the tree does not have implement multi-select.  The fix 
		* is to disable DI_GETDRAGIMAGE when more than one item is
		* selected.
		*/
		if ((style & SWT.MULTI) != 0 || hooks (SWT.EraseItem) || hooks (SWT.PaintItem)) {
			int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
			TreeItem [] items = new TreeItem [10];
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;
			int count = getSelection (hItem, tvItem, items, 0, 10, false, true);
			if (count == 0) return 0;
			POINT mousePos = new POINT ();
			OS.POINTSTOPOINT (mousePos, OS.GetMessagePos ());
			OS.MapWindowPoints (0, handle, mousePos, 1);
			RECT clientRect = new RECT ();
			OS.GetClientRect(handle, clientRect);
			RECT rect = items [0].getBounds (0, true, true, false);
			if ((style & SWT.FULL_SELECTION) != 0) {
				int width = DRAG_IMAGE_SIZE;
				rect.left = Math.max (clientRect.left, mousePos.x - width / 2);
				if (clientRect.right > rect.left + width) {
					rect.right = rect.left + width;
				} else {
					rect.right = clientRect.right;
					rect.left = Math.max (clientRect.left, rect.right - width);
				}
			} else {
				rect.left = Math.max (rect.left, clientRect.left);
				rect.right = Math.min (rect.right, clientRect.right);
			}
			int /*long*/ hRgn = OS.CreateRectRgn (rect.left, rect.top, rect.right, rect.bottom);
			for (int i = 1; i < count; i++) {
				if (rect.bottom - rect.top > DRAG_IMAGE_SIZE) break;
				if (rect.bottom > clientRect.bottom) break;
				RECT itemRect = items[i].getBounds (0, true, true, false);
				if ((style & SWT.FULL_SELECTION) != 0) {
					itemRect.left = rect.left;
					itemRect.right = rect.right;
				} else {
					itemRect.left = Math.max (itemRect.left, clientRect.left);
					itemRect.right = Math.min (itemRect.right, clientRect.right);
				}
				int /*long*/ rectRgn = OS.CreateRectRgn (itemRect.left, itemRect.top, itemRect.right, itemRect.bottom);
				OS.CombineRgn (hRgn, hRgn, rectRgn, OS.RGN_OR);
				OS.DeleteObject (rectRgn);
				rect.bottom = itemRect.bottom;
				
			}
			OS.GetRgnBox (hRgn, rect);
			
			/* Create resources */
			int /*long*/ hdc = OS.GetDC (handle);
			int /*long*/ memHdc = OS.CreateCompatibleDC (hdc);
			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
			bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
			bmiHeader.biWidth = rect.right - rect.left;
			bmiHeader.biHeight = -(rect.bottom - rect.top);
			bmiHeader.biPlanes = 1;
			bmiHeader.biBitCount = 32;
			bmiHeader.biCompression = OS.BI_RGB;
			byte []	bmi = new byte [BITMAPINFOHEADER.sizeof];
			OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
			int /*long*/ [] pBits = new int /*long*/ [1];
			int /*long*/ memDib = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
			if (memDib == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			int /*long*/ oldMemBitmap = OS.SelectObject (memHdc, memDib);
			int colorKey = 0x0000FD;
			POINT pt = new POINT ();
			OS.SetWindowOrgEx (memHdc, rect.left, rect.top, pt);
			OS.FillRect (memHdc, rect, findBrush (colorKey, OS.BS_SOLID));
			OS.OffsetRgn (hRgn, -rect.left, -rect.top);
			OS.SelectClipRgn (memHdc, hRgn);
			OS.PrintWindow (handle, memHdc, 0);
			OS.SetWindowOrgEx (memHdc, pt.x, pt.y, null);
			OS.SelectObject (memHdc, oldMemBitmap);
			OS.DeleteDC (memHdc);
			OS.ReleaseDC (0, hdc);
			OS.DeleteObject (hRgn);

			SHDRAGIMAGE shdi = new SHDRAGIMAGE ();
			shdi.hbmpDragImage = memDib;
			shdi.crColorKey = colorKey;
			shdi.sizeDragImage.cx = bmiHeader.biWidth;
			shdi.sizeDragImage.cy = -bmiHeader.biHeight;
			shdi.ptOffset.x = mousePos.x - rect.left;
			shdi.ptOffset.y = mousePos.y - rect.top;
			if ((style & SWT.MIRRORED) != 0) {
				shdi.ptOffset.x = shdi.sizeDragImage.cx - shdi.ptOffset.x; 
			}
			OS.MoveMemory (lParam, shdi, SHDRAGIMAGE.sizeof);
			return 1;
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_CHAR (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  The tree control beeps
	* in WM_CHAR when the search for the item that
	* matches the key stroke fails.  This is the
	* standard tree behavior but is unexpected when
	* the key that was typed was ESC, CR or SPACE.
	* The fix is to avoid calling the tree window
	* proc in these cases.
	*/
	switch ((int)/*64*/wParam) {
		case ' ': {
			int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hItem != 0) {
				hAnchor = hItem;
				OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE | OS.TVIF_PARAM;
				tvItem.hItem = hItem;
				if ((style & SWT.CHECK) != 0) {
					tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					int state = tvItem.state >> 12;
					if ((state & 0x1) != 0) {
						state++;
					} else  {
						--state;
					}
					tvItem.state = state << 12;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					if (!OS.IsWinCE) {
						int /*long*/ id = hItem;
						if (OS.COMCTL32_MAJOR >= 6) {
							id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, hItem, 0);
						}
						OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, (int)/*64*/id);	
					}
				}
				tvItem.stateMask = OS.TVIS_SELECTED;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((style & SWT.MULTI) != 0 && OS.GetKeyState (OS.VK_CONTROL) < 0) {
					if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
						tvItem.state &= ~OS.TVIS_SELECTED;
					} else {
						tvItem.state |= OS.TVIS_SELECTED;
					}
				} else {
					tvItem.state |= OS.TVIS_SELECTED;
				}
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				TreeItem item = _getItem (hItem, (int)/*64*/tvItem.lParam);
				Event event = new Event ();
				event.item = item;
				sendSelectionEvent (SWT.Selection, event, false);
				if ((style & SWT.CHECK) != 0) {
					event = new Event ();
					event.item = item;
					event.detail = SWT.CHECK;
					sendSelectionEvent (SWT.Selection, event, false);
				}
			}
			return LRESULT.ZERO;
		}
		case SWT.CR: {
			/*
			* Feature in Windows.  Windows sends NM_RETURN from WM_KEYDOWN
			* instead of using WM_CHAR.  This means that application code
			* that expects to consume the key press and therefore avoid a
			* SWT.DefaultSelection event from WM_CHAR will fail.  The fix
			* is to implement SWT.DefaultSelection in WM_CHAR instead of
			* using NM_RETURN.
			*/
			Event event = new Event ();
			int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hItem != 0) event.item = _getItem (hItem);
			sendSelectionEvent (SWT.DefaultSelection, event, false);
			return LRESULT.ZERO;
		}
		case SWT.ESC:
			return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_ERASEBKGND (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if ((style & SWT.DOUBLE_BUFFERED) != 0) return LRESULT.ONE;
	if (findImageControl () != null) return LRESULT.ONE;
	return result;
}

LRESULT WM_GETOBJECT (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Ensure that there is an accessible object created for this
	* control because support for checked item and tree column
	* accessibility is temporarily implemented in the accessibility
	* package.
	*/
	if ((style & SWT.CHECK) != 0 || hwndParent != 0) {
		if (accessible == null) accessible = new_Accessible (this);
	}
	return super.WM_GETOBJECT (wParam, lParam);
}

LRESULT WM_HSCROLL (int /*long*/ wParam, int /*long*/ lParam) {
	boolean fixScroll = false;
	if ((style & SWT.DOUBLE_BUFFERED) != 0) {
		fixScroll = (style & SWT.VIRTUAL) != 0 || hooks (SWT.EraseItem) || hooks (SWT.PaintItem);
	}
	if (fixScroll) {
		style &= ~SWT.DOUBLE_BUFFERED;
		if (explorerTheme) {
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, OS.TVS_EX_DOUBLEBUFFER, 0);
		}
	}
	LRESULT result = super.WM_HSCROLL (wParam, lParam);
	if (fixScroll) {
		style |= SWT.DOUBLE_BUFFERED;
		if (explorerTheme) {
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, OS.TVS_EX_DOUBLEBUFFER, OS.TVS_EX_DOUBLEBUFFER);
		}
	}
	if (result != null) return result;
	return result;
}

LRESULT WM_KEYDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch ((int)/*64*/wParam) {
		case OS.VK_LEFT:
		case OS.VK_RIGHT:
			/* 
			* Bug in Windows. The behavior for the left and right keys is not
			* changed if the orientation changes after the control was created.
			* The fix is to replace VK_LEFT by VK_RIGHT and VK_RIGHT by VK_LEFT
			* when the current orientation differs from the orientation used to 
			* create the control.
		    */
		    boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
		    if (isRTL != createdAsRTL) {
			   int /*long*/ code = callWindowProc (handle, OS.WM_KEYDOWN, wParam == OS.VK_RIGHT ? OS.VK_LEFT : OS.VK_RIGHT, lParam);
			   return new LRESULT (code);
		    }
		    break;
		case OS.VK_SPACE:
			/*
			* Ensure that the window proc does not process VK_SPACE
			* so that it can be handled in WM_CHAR.  This allows the
			* application to cancel an operation that is normally
			* performed in WM_KEYDOWN from WM_CHAR.
			*/
			return LRESULT.ZERO;
		case OS.VK_ADD:
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				if (hwndHeader != 0) {
					TreeColumn [] newColumns = new TreeColumn [columnCount];
					System.arraycopy (columns, 0, newColumns, 0, columnCount);
					for (int i=0; i<columnCount; i++) {
						TreeColumn column = newColumns [i];
						if (!column.isDisposed () && column.getResizable ()) {
							column.pack ();
						}
					}
				}
			}
			break;
		case OS.VK_UP:
		case OS.VK_DOWN:
		case OS.VK_PRIOR:
		case OS.VK_NEXT:
		case OS.VK_HOME:
		case OS.VK_END: {
			OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
			if ((style & SWT.SINGLE) != 0) break;
			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
				int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
				if (hItem != 0) {
					if (hAnchor == 0) hAnchor = hItem;
					ignoreSelect = ignoreDeselect = true;
					int /*long*/ code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
					ignoreSelect = ignoreDeselect = false;
					int /*long*/ hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
					tvItem.stateMask = OS.TVIS_SELECTED;
					int /*long*/ hDeselectItem = hItem;					
					RECT rect1 = new RECT ();
					if (!OS.TreeView_GetItemRect (handle, hAnchor, rect1, false)) {
						hAnchor = hItem;
						OS.TreeView_GetItemRect (handle, hAnchor, rect1, false);
					}
					RECT rect2 = new RECT ();
					OS.TreeView_GetItemRect (handle, hDeselectItem, rect2, false);
					int flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
					while (hDeselectItem != hAnchor) {
						tvItem.hItem = hDeselectItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						hDeselectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hDeselectItem);
					}
					int /*long*/ hSelectItem = hAnchor;
					OS.TreeView_GetItemRect (handle, hNewItem, rect1, false);
					OS.TreeView_GetItemRect (handle, hSelectItem, rect2, false);
					tvItem.state = OS.TVIS_SELECTED;
					flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
					while (hSelectItem != hNewItem) {
						tvItem.hItem = hSelectItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						hSelectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hSelectItem);
					}
					tvItem.hItem = hNewItem;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
					tvItem.hItem = hNewItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					Event event = new Event ();
					event.item = _getItem (hNewItem, (int)/*64*/tvItem.lParam);
					sendSelectionEvent (SWT.Selection, event, false);
					return new LRESULT (code);
				}
			}
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
				if (hItem != 0) {
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
					tvItem.stateMask = OS.TVIS_SELECTED;
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					boolean oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
					int /*long*/ hNewItem = 0;
					switch ((int)/*64*/wParam) {
						case OS.VK_UP:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUSVISIBLE, hItem);
							break;
						case OS.VK_DOWN:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
							break;
						case OS.VK_HOME:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
							break;
						case OS.VK_PRIOR:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							if (hNewItem == hItem) {
								OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEUP, 0);
								hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							}
							break;
						case OS.VK_NEXT:			
							RECT rect = new RECT (), clientRect = new RECT ();
							OS.GetClientRect (handle, clientRect);
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							do {
								int /*long*/ hVisible = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNewItem);
								if (hVisible == 0) break;
								if (!OS.TreeView_GetItemRect (handle, hVisible, rect, false)) break;
								if (rect.bottom > clientRect.bottom) break;
								if ((hNewItem = hVisible) == hItem) {
									OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEDOWN, 0);
								}
							} while (hNewItem != 0);
							break;
						case OS.VK_END:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
							break;
					}
					if (hNewItem != 0) {
						OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hNewItem);
						tvItem.hItem = hNewItem;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						boolean newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
						boolean redraw = !newSelected && getDrawing () && OS.IsWindowVisible (handle);
						if (redraw) {
							OS.UpdateWindow (handle);
							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
						}
						hSelect = hNewItem;
						ignoreSelect = true;
						OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
						ignoreSelect = false;
						hSelect = 0;
						if (oldSelected) {
							tvItem.state = OS.TVIS_SELECTED;
							tvItem.hItem = hItem;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						}
						if (!newSelected) {
							tvItem.state = 0;
							tvItem.hItem = hNewItem;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						}
						if (redraw) {
							RECT rect1 = new RECT (), rect2 = new RECT ();
							boolean fItemRect = (style & SWT.FULL_SELECTION) == 0;
							if (hooks (SWT.EraseItem) || hooks (SWT.PaintItem)) fItemRect = false;
							if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) fItemRect = false;
							OS.TreeView_GetItemRect (handle, hItem, rect1, fItemRect);
							OS.TreeView_GetItemRect (handle, hNewItem, rect2, fItemRect);
							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
							OS.InvalidateRect (handle, rect1, true);
							OS.InvalidateRect (handle, rect2, true);
							OS.UpdateWindow (handle);
						}
						return LRESULT.ZERO;
					}
				}
			}
			int /*long*/ code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
			hAnchor = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			return new LRESULT (code);
		}
	}
	return result;
}

LRESULT WM_KILLFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Bug in Windows.  When a tree item that has an image
	* with alpha is expanded or collapsed, the area where
	* the image is drawn is not erased before it is drawn.
	* This means that the image gets darker each time.
	* The fix is to redraw the selection.
	* 
	* Feature in Windows.  When multiple item have
	* the TVIS_SELECTED state, Windows redraws only
	* the focused item in the color used to show the
	* selection when the tree loses or gains focus.
	* The fix is to force Windows to redraw the
	* selection when focus is gained or lost.
	*/
	boolean redraw = (style & SWT.MULTI) != 0;
	if (!redraw) {
		if (!OS.IsWinCE && OS.COMCTL32_MAJOR >= 6) {
			if (imageList != null) {
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				if ((bits & OS.TVS_FULLROWSELECT) == 0) {
					redraw = true;
				}
			}
		}
	}
	if (redraw) redrawSelection ();
	return super.WM_KILLFOCUS (wParam, lParam);
}

LRESULT WM_LBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = OS.GET_X_LPARAM (lParam);
	lpht.y = OS.GET_Y_LPARAM (lParam);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0) {
		if ((style & SWT.CHECK) != 0) {
			if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
				Display display = this.display;
				display.captureChanged = false;
				sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
				if (!sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam)) {
					if (!display.captureChanged && !isDisposed ()) {
						if (OS.GetCapture () != handle) OS.SetCapture (handle);
					}
					return LRESULT.ZERO;
				}
				if (!display.captureChanged && !isDisposed ()) {
					if (OS.GetCapture () != handle) OS.SetCapture (handle);
				}
				OS.SetFocus (handle);
				TVITEM tvItem = new TVITEM ();
				tvItem.hItem = lpht.hItem;
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;	
				tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				int state = tvItem.state >> 12;
				if ((state & 0x1) != 0) {
					state++;
				} else  {
					--state;
				}
				tvItem.state = state << 12;
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				if (!OS.IsWinCE) {	
					int /*long*/ id = tvItem.hItem;
					if (OS.COMCTL32_MAJOR >= 6) {
						id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
					}
					OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, (int)/*64*/id);	
				}
				Event event = new Event ();
				event.item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
				event.detail = SWT.CHECK;
				sendSelectionEvent (SWT.Selection, event, false);
				return LRESULT.ZERO;
			}
		}
	}
	LRESULT result = super.WM_LBUTTONDBLCLK (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	if (lpht.hItem != 0) {
		int flags = OS.TVHT_ONITEM;
		if ((style & SWT.FULL_SELECTION) != 0) {
			flags |= OS.TVHT_ONITEMRIGHT | OS.TVHT_ONITEMINDENT;
		} else {
			if (hooks (SWT.MeasureItem)) {
				lpht.flags &= ~(OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL);
				if (hitTestSelection (lpht.hItem, lpht.x, lpht.y)) {
					lpht.flags |= OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL;
				}
			}
		}
		if ((lpht.flags & flags) != 0) {
			Event event = new Event ();
			event.item = _getItem (lpht.hItem);
			sendSelectionEvent (SWT.DefaultSelection, event, false);
		}
	}
	return result;
}

LRESULT WM_LBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* In a multi-select tree, if the user is collapsing a subtree that
	* contains selected items, clear the selection from these items and
	* issue a selection event.  Only items that are selected and visible
	* are cleared.  This code also runs in the case when the white space
	* below the last item is selected.
	*/
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = OS.GET_X_LPARAM (lParam);
	lpht.y = OS.GET_Y_LPARAM (lParam);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem == 0 || (lpht.flags & OS.TVHT_ONITEMBUTTON) != 0) {
		Display display = this.display;
		display.captureChanged = false;
		if (!sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam)) {
			if (!display.captureChanged && !isDisposed ()) {
				if (OS.GetCapture () != handle) OS.SetCapture (handle);
			}
			return LRESULT.ZERO;
		}
		boolean fixSelection = false, deselected = false;
		int /*long*/ hOldSelection = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (lpht.hItem != 0 && (style & SWT.MULTI) != 0) {
			if (hOldSelection != 0) {
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
				tvItem.hItem = lpht.hItem;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((tvItem.state & OS.TVIS_EXPANDED) != 0) {
					fixSelection = true;
					tvItem.stateMask = OS.TVIS_SELECTED;
					int /*long*/ hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, lpht.hItem);
					while (hNext != 0) {
						if (hNext == hAnchor) hAnchor = 0;
						tvItem.hItem = hNext;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						if ((tvItem.state & OS.TVIS_SELECTED) != 0) deselected = true;
						tvItem.state = 0;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						int /*long*/ hItem = hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNext);
						while (hItem != 0 && hItem != lpht.hItem) {
							hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
						}
						if (hItem == 0) break;
					}
				}
			}
		}
		dragStarted = gestureCompleted = false;
		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = true;
		int /*long*/ code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
			if (OS.GetFocus () != handle) OS.SetFocus (handle);
		}
		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = false;
		int /*long*/ hNewSelection = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hOldSelection != hNewSelection) hAnchor = hNewSelection;
		if (dragStarted) {
			if (!display.captureChanged && !isDisposed ()) {
				if (OS.GetCapture () != handle) OS.SetCapture (handle);
			}
		}
		/*
		* Bug in Windows.  When a tree has no images and an item is
		* expanded or collapsed, for some reason, Windows changes
		* the size of the selection.  When the user expands a tree
		* item, the selection rectangle is made a few pixels larger.
		* When the user collapses an item, the selection rectangle
		* is restored to the original size but the selection is not
		* redrawn, causing pixel corruption.  The fix is to detect
		* this case and redraw the item.
		*/
		if ((lpht.flags & OS.TVHT_ONITEMBUTTON) != 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.TVS_FULLROWSELECT) == 0) {
				if (OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0) == 0) {
					int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
					if (hItem != 0) {
						RECT rect = new RECT ();
						if (OS.TreeView_GetItemRect (handle, hItem, rect, false)) {
							OS.InvalidateRect (handle, rect, true);
						}
					}
				}
			}
		}
		if (deselected) {
			Event event = new Event ();
			event.item = _getItem (lpht.hItem);
			sendSelectionEvent (SWT.Selection, event, false);
		}
		return new LRESULT (code);
	}
	
	/* Look for check/uncheck */
	if ((style & SWT.CHECK) != 0) {
		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
			Display display = this.display;
			display.captureChanged = false;
			if (!sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam)) {
				if (!display.captureChanged && !isDisposed ()) {
					if (OS.GetCapture () != handle) OS.SetCapture (handle);
				}
				return LRESULT.ZERO;
			}
			if (!display.captureChanged && !isDisposed ()) {
				if (OS.GetCapture () != handle) OS.SetCapture (handle);
			}
			OS.SetFocus (handle);
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = lpht.hItem;
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_STATE;	
			tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			int state = tvItem.state >> 12;
			if ((state & 0x1) != 0) {
				state++;
			} else  {
				--state;
			}
			tvItem.state = state << 12;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			if (!OS.IsWinCE) {
				int /*long*/ id = tvItem.hItem;
				if (OS.COMCTL32_MAJOR >= 6) {
					id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
				}
				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, (int)/*64*/id);	
			}
			Event event = new Event ();
			event.item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
			event.detail = SWT.CHECK;
			sendSelectionEvent (SWT.Selection, event, false);
			return LRESULT.ZERO;
		}
	}
	
	/*
	* Feature in Windows.  When the tree has the style
	* TVS_FULLROWSELECT, the background color for the
	* entire row is filled when an item is painted,
	* drawing on top of any custom drawing.  The fix
	* is to emulate TVS_FULLROWSELECT.
	*/
	boolean selected = false;
	boolean fakeSelection = false;
	if (lpht.hItem != 0) {
		if ((style & SWT.FULL_SELECTION) != 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.TVS_FULLROWSELECT) == 0) fakeSelection = true;
		} else {
			if (hooks (SWT.MeasureItem)) {
				selected = hitTestSelection (lpht.hItem, lpht.x, lpht.y);
				if (selected) {
					if ((lpht.flags & OS.TVHT_ONITEM) == 0) fakeSelection = true;
				}
			}
		}
	}
	
	/* Process the mouse when an item is not selected */
	if (!selected && (style & SWT.FULL_SELECTION) == 0) {
		if ((lpht.flags & OS.TVHT_ONITEM) == 0) {
			Display display = this.display;
			display.captureChanged = false;
			if (!sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam)) {
				if (!display.captureChanged && !isDisposed ()) {
					if (OS.GetCapture () != handle) OS.SetCapture (handle);
				}
				return LRESULT.ZERO;
			}
			int /*long*/ code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				if (OS.GetFocus () != handle) OS.SetFocus (handle);
			}
			if (!display.captureChanged && !isDisposed ()) {
				if (OS.GetCapture () != handle) OS.SetCapture (handle);
			}
			return new LRESULT (code);
		}
	}

	/* Get the selected state of the item under the mouse */
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	boolean hittestSelected = false;
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = lpht.hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		hittestSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
	}
	
	/* Get the selected state of the last selected item */
	int /*long*/ hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = hOldItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);

		/* Check for CONTROL or drag selection */
		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
			/*
			* Feature in Windows.  When the tree is not drawing focus
			* and the user selects a tree item while the CONTROL key
			* is down, the tree window proc sends WM_UPDATEUISTATE
			* to the top level window, causing controls within the shell
			* to redraw.  When drag detect is enabled, the tree window
			* proc runs a modal loop that allows WM_PAINT messages to be
			* delivered during WM_LBUTTONDOWN.  When WM_SETREDRAW is used
			* to disable drawing for the tree and a WM_PAINT happens for
			* a parent of the tree (or a sibling that overlaps), the parent
			* will draw on top of the tree.  If WM_SETREDRAW is turned back
			* on without redrawing the entire tree, pixel corruption occurs.
			* This case only seems to happen when the tree has been given
			* focus from WM_MOUSEACTIVATE of the shell.  The fix is to
			* force the WM_UPDATEUISTATE to be sent before disabling
			* the drawing.
			* 
			* NOTE:  Any redraw of a parent (or sibling) will be dispatched
			* during the modal drag detect loop.  This code only fixes the
			* case where the tree causes a redraw from WM_UPDATEUISTATE.
			* In SWT, the InvalidateRect() that caused the pixel corruption
			* is found in Composite.WM_UPDATEUISTATE().
			*/
			int uiState = (int)/*64*/OS.SendMessage (handle, OS.WM_QUERYUISTATE, 0, 0);
			if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
				OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			}
			OS.UpdateWindow (handle);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		} else {
			deselectAll ();
		}
	}

	/* Do the selection */
	Display display = this.display;
	display.captureChanged = false;
	if (!sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam)) {
		if (!display.captureChanged && !isDisposed ()) {
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
		}
		return LRESULT.ZERO;
	}
	hSelect = lpht.hItem;
	dragStarted = gestureCompleted = false;
	ignoreDeselect = ignoreSelect = true;
	int /*long*/ code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		if (OS.GetFocus () != handle) OS.SetFocus (handle);
	}
	int /*long*/ hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if (fakeSelection) {
		if (hOldItem == 0 || (hNewItem == hOldItem && lpht.hItem != hOldItem)) {
			OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, lpht.hItem);
			hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		}
		if (!dragStarted && (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
			dragStarted = dragDetect (handle, lpht.x, lpht.y, false, null, null);
		}
	}
	ignoreDeselect = ignoreSelect = false;
	hSelect = 0;
	if (dragStarted) {
		if (!display.captureChanged && !isDisposed ()) {
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
		}
	}

	/*
	* Feature in Windows.  When the old and new focused item
	* are the same, Windows does not check to make sure that
	* the item is actually selected, not just focused.  The
	* fix is to force the item to draw selected by setting
	* the state mask.  This is only necessary when the tree
	* is single select.
	*/
	if ((style & SWT.SINGLE) != 0) {
		if (hOldItem == hNewItem) {
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	
	/* Reselect the last item that was unselected */
	if ((style & SWT.MULTI) != 0) {
		
		/* Check for CONTROL and reselect the last item */
		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
			if (hOldItem == hNewItem && hOldItem == lpht.hItem) {
				if ((wParam & OS.MK_CONTROL) != 0) {
					tvItem.state ^= OS.TVIS_SELECTED;
					if (dragStarted) tvItem.state = OS.TVIS_SELECTED;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			} else {
				if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
					tvItem.state = OS.TVIS_SELECTED;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
				if ((wParam & OS.MK_CONTROL) != 0 && !dragStarted) {
					if (hittestSelected) {
						tvItem.state = 0;
						tvItem.hItem = lpht.hItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
			RECT rect1 = new RECT (), rect2 = new RECT ();
			boolean fItemRect = (style & SWT.FULL_SELECTION) == 0;
			if (hooks (SWT.EraseItem) || hooks (SWT.PaintItem)) fItemRect = false;
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) fItemRect = false;
			OS.TreeView_GetItemRect (handle, hOldItem, rect1, fItemRect);
			OS.TreeView_GetItemRect (handle, hNewItem, rect2, fItemRect);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.InvalidateRect (handle, rect1, true);
			OS.InvalidateRect (handle, rect2, true);
			OS.UpdateWindow (handle);
		}

		/* Check for SHIFT or normal select and deselect/reselect items */
		if ((wParam & OS.MK_CONTROL) == 0) {
			if (!hittestSelected || !dragStarted) {
				tvItem.state = 0;
				int /*long*/ oldProc = OS.GetWindowLongPtr (handle, OS.GWLP_WNDPROC);
				OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, TreeProc);
				if ((style & SWT.VIRTUAL) != 0) {
					int /*long*/ hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
					deselect (hItem, tvItem, hNewItem);
				} else {
					for (int i=0; i<items.length; i++) {
						TreeItem item = items [i];
						if (item != null && item.handle != hNewItem) {
							tvItem.hItem = item.handle;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						}
					}
				}
				tvItem.hItem = hNewItem;
				tvItem.state = OS.TVIS_SELECTED;
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, oldProc);
				if ((wParam & OS.MK_SHIFT) != 0) {
					RECT rect1 = new RECT ();
					if (hAnchor == 0) hAnchor = hNewItem;
					if (OS.TreeView_GetItemRect (handle, hAnchor, rect1, false)) {
						RECT rect2 = new RECT ();
						if (OS.TreeView_GetItemRect (handle, hNewItem, rect2, false)) {
							int flags = rect1.top < rect2.top ? OS.TVGN_NEXTVISIBLE : OS.TVGN_PREVIOUSVISIBLE;			
							tvItem.state = OS.TVIS_SELECTED;
							int /*long*/ hItem = tvItem.hItem = hAnchor;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
							while (hItem != hNewItem) {
								tvItem.hItem = hItem;
								OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
								hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hItem);
							}
						}
					}
				}
			}
		}
	}
	if ((wParam & OS.MK_SHIFT) == 0) hAnchor = hNewItem;
			
	/* Issue notification */
	if (!gestureCompleted) {
		tvItem.hItem = hNewItem;
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		Event event = new Event ();
		event.item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
		sendSelectionEvent (SWT.Selection, event, false);
	}
	gestureCompleted = false;
	
	/*
	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
	* the widget starts a modal loop to determine if the user wants
	* to begin a drag/drop operation or marquee select.  Unfortunately,
	* this modal loop eats the corresponding mouse up.  The fix is to
	* detect the cases when the modal loop has eaten the mouse up and
	* issue a fake mouse up.
	*/
	if (dragStarted) {
		sendDragEvent (1, OS.GET_X_LPARAM (lParam), OS.GET_Y_LPARAM (lParam));
	} else {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.TVS_DISABLEDRAGDROP) == 0) {
			sendMouseEvent (SWT.MouseUp, 1, handle, OS.WM_LBUTTONUP, wParam, lParam);
		}
	}
	dragStarted = false;
	return new LRESULT (code);
}

LRESULT WM_MOUSEMOVE (int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = super.WM_MOUSEMOVE (wParam, lParam);
	if (result != null) return result;
	if (itemToolTipHandle != 0) {
		/*
		* Bug in Windows.  On some machines that do not have XBUTTONs,
		* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
		* causing mouse capture to become stuck.  The fix is to test
		* for the extra buttons only when they exist.
		*/
		int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON;
		if (display.xMouse) mask |= OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
		if ((wParam & mask) == 0) {
			int x = OS.GET_X_LPARAM (lParam);
			int y = OS.GET_Y_LPARAM (lParam);
			int [] index = new int [1];
			TreeItem [] item = new TreeItem [1];
			RECT [] cellRect = new RECT [1], itemRect = new RECT [1];
			if (findCell (x, y, item, index, cellRect, itemRect)) {
				/*
				* Feature in Windows.  When the new tool rectangle is
				* set using TTM_NEWTOOLRECT and the tooltip is visible,
				* Windows draws the tooltip right away and the sends
				* WM_NOTIFY with TTN_SHOW.  This means that the tooltip
				* shows first at the wrong location and then moves to
				* the right one.  The fix is to hide the tooltip window.
				*/
				if (OS.SendMessage (itemToolTipHandle, OS.TTM_GETCURRENTTOOL, 0, 0) == 0) {
					if (OS.IsWindowVisible (itemToolTipHandle)) {
						OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
					}
				}
				TOOLINFO lpti = new TOOLINFO ();
				lpti.cbSize = TOOLINFO.sizeof;
				lpti.hwnd = handle;
				lpti.uId = handle;
				lpti.uFlags = OS.TTF_SUBCLASS | OS.TTF_TRANSPARENT;
				lpti.left = cellRect [0].left;
				lpti.top = cellRect [0].top;
				lpti.right = cellRect [0].right;
				lpti.bottom = cellRect [0].bottom;
				OS.SendMessage (itemToolTipHandle, OS.TTM_NEWTOOLRECT, 0, lpti);
			}
		}
	}
	return result;
}

LRESULT WM_MOUSEWHEEL (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_MOUSEWHEEL (wParam, lParam);
	if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
	return result;
}

LRESULT WM_MOVE (int /*long*/ wParam, int /*long*/ lParam) {
	if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
	if (ignoreResize) return null;
	return super.WM_MOVE (wParam, lParam);
}

LRESULT WM_RBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  The receiver uses WM_RBUTTONDOWN
	* to initiate a drag/drop operation depending on how the
	* user moves the mouse.  If the user clicks the right button,
	* without moving the mouse, the tree consumes the corresponding
	* WM_RBUTTONUP.  The fix is to avoid calling the window proc for
	* the tree.
	*/
	Display display = this.display;
	display.captureChanged = false;
	if (!sendMouseEvent (SWT.MouseDown, 3, handle, OS.WM_RBUTTONDOWN, wParam, lParam)) {
		if (!display.captureChanged && !isDisposed ()) {
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
		}
		return LRESULT.ZERO;
	}
	/*
	* This code is intentionally commented.
	*/
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	if (OS.GetFocus () != handle) OS.SetFocus (handle);
	
	/*
	* Feature in Windows.  When the user selects a tree item
	* with the right mouse button, the item remains selected
	* only as long as the user does not release or move the
	* mouse.  As soon as this happens, the selection snaps
	* back to the previous selection.  This behavior can be
	* observed in the Explorer but is not instantly apparent
	* because the Explorer explicitly sets the selection when
	* the user chooses a menu item.  If the user cancels the
	* menu, the selection snaps back.  The fix is to avoid
	* calling the window proc and do the selection ourselves.
	* This behavior is consistent with the table.
	*/
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = OS.GET_X_LPARAM (lParam);
	lpht.y = OS.GET_Y_LPARAM (lParam);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0) {
		boolean fakeSelection = (style & SWT.FULL_SELECTION) != 0;
		if (!fakeSelection) {
			if (hooks (SWT.MeasureItem)) {
				fakeSelection = hitTestSelection (lpht.hItem, lpht.x, lpht.y);
			} else {
				int flags = OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL;
				fakeSelection = (lpht.flags & flags) != 0;
			}
		}
		if (fakeSelection) {
			if ((wParam & (OS.MK_CONTROL | OS.MK_SHIFT)) == 0) {
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
				tvItem.stateMask = OS.TVIS_SELECTED;
				tvItem.hItem = lpht.hItem;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((tvItem.state & OS.TVIS_SELECTED) == 0) {
					ignoreSelect = true;
					OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, 0);
					ignoreSelect = false;
					OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, lpht.hItem);
				}
			}
		}
	}
	return LRESULT.ZERO;
}

LRESULT WM_PAINT (int /*long*/ wParam, int /*long*/ lParam) {
	if (shrink && !ignoreShrink) {
		/* Resize the item array to fit the last item */
		int count = items.length - 1;
		while (count >= 0) {
			if (items [count] != null) break;
			--count;
		}
		count++;
		if (items.length > 4 && items.length - count > 3) {
			int length = Math.max (4, (count + 3) / 4 * 4);
			TreeItem [] newItems = new TreeItem [length];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}
		shrink = false;
	}
	if ((style & SWT.DOUBLE_BUFFERED) != 0 || findImageControl () != null) {
		boolean doubleBuffer = true;
		if (explorerTheme) {
			int exStyle = (int)/*64*/OS.SendMessage (handle, OS.TVM_GETEXTENDEDSTYLE, 0, 0);
			if ((exStyle & OS.TVS_EX_DOUBLEBUFFER) != 0) doubleBuffer = false;
		}
		if (doubleBuffer) {
			GC gc = null;
			int /*long*/ paintDC = 0;
			PAINTSTRUCT ps = new PAINTSTRUCT ();
			boolean hooksPaint = hooks (SWT.Paint) || filters (SWT.Paint);
			if (hooksPaint) {
				GCData data = new GCData ();
				data.ps = ps;
				data.hwnd = handle;
				gc = GC.win32_new (this, data);
				paintDC = gc.handle;
			} else {
				paintDC = OS.BeginPaint (handle, ps);
			}
			int width = ps.right - ps.left;
			int height = ps.bottom - ps.top;
			if (width != 0 && height != 0) {
				int /*long*/ hDC = OS.CreateCompatibleDC (paintDC);
				POINT lpPoint1 = new POINT (), lpPoint2 = new POINT ();
				OS.SetWindowOrgEx (hDC, ps.left, ps.top, lpPoint1);
				OS.SetBrushOrgEx (hDC, ps.left, ps.top, lpPoint2);
				int /*long*/ hBitmap = OS.CreateCompatibleBitmap (paintDC, width, height);
				int /*long*/ hOldBitmap = OS.SelectObject (hDC, hBitmap);
				RECT rect = new RECT ();
				OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
				drawBackground (hDC, rect);
				callWindowProc (handle, OS.WM_PAINT, hDC, 0);
				OS.SetWindowOrgEx (hDC, lpPoint1.x, lpPoint1.y, null);
				OS.SetBrushOrgEx (hDC, lpPoint2.x, lpPoint2.y, null);
				OS.BitBlt (paintDC, ps.left, ps.top, width, height, hDC, 0, 0, OS.SRCCOPY);
				OS.SelectObject (hDC, hOldBitmap);
				OS.DeleteObject (hBitmap);
				OS.DeleteObject (hDC);
				if (hooksPaint) {
					Event event = new Event ();
					event.gc = gc;
					event.x = ps.left;
					event.y = ps.top;
					event.width = ps.right - ps.left;
					event.height = ps.bottom - ps.top;
					sendEvent (SWT.Paint, event);
					// widget could be disposed at this point
					event.gc = null;
				}
			}
			if (hooksPaint) {
				gc.dispose ();
			} else {
				OS.EndPaint (handle, ps);
			}
			return LRESULT.ZERO;
		}
	}
	return super.WM_PAINT (wParam, lParam);
}

LRESULT WM_PRINTCLIENT (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when WM_PRINT is used
	* to capture an image of a hierarchy that contains a tree with
	* columns, the clipping that is used to stop the first column
	* from drawing on top of subsequent columns stops the first
	* column and the tree lines from drawing.  This does not happen
	* during WM_PAINT.  The fix is to draw without clipping and
	* then draw the rest of the columns on top.  Since the drawing
	* is happening in WM_PRINTCLIENT, the redrawing is not visible.
	*/
	printClient = true;
	int /*long*/ code = callWindowProc (handle, OS.WM_PRINTCLIENT, wParam, lParam);
	printClient = false;
	return new LRESULT (code);
}

LRESULT WM_SETCURSOR (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SETCURSOR (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Feature in Windows. On Windows 7, the tree control show the
	* hand cursor when the mouse is over an item.  This is the
	* correct Windows 7 behavior but not correct for SWT. The fix
	* is to always ensure a cursor is set. 
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 1)) {
		if (wParam == handle) {
			int hitTest = (short) OS.LOWORD (lParam);
			if (hitTest == OS.HTCLIENT) {
				OS.SetCursor (OS.LoadCursor (0, OS.IDC_ARROW));
				return LRESULT.ONE;
			}
		}
	}
	return null;
}

LRESULT WM_SETFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Bug in Windows.  When a tree item that has an image
	* with alpha is expanded or collapsed, the area where
	* the image is drawn is not erased before it is drawn.
	* This means that the image gets darker each time.
	* The fix is to redraw the selection.
	* 
	* Feature in Windows.  When multiple item have
	* the TVIS_SELECTED state, Windows redraws only
	* the focused item in the color used to show the
	* selection when the tree loses or gains focus.
	* The fix is to force Windows to redraw the
	* selection when focus is gained or lost.
	*/
	boolean redraw = (style & SWT.MULTI) != 0;
	if (!redraw) {
		if (!OS.IsWinCE && OS.COMCTL32_MAJOR >= 6) {
			if (imageList != null) {
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				if ((bits & OS.TVS_FULLROWSELECT) == 0) {
					redraw = true;
				}
			}
		}
	}
	if (redraw) redrawSelection ();
	return super.WM_SETFOCUS (wParam, lParam);
}

LRESULT WM_SETFONT (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SETFONT (wParam, lParam);
	if (result != null) return result;
	if (hwndHeader != 0) {
		/*
		* Bug in Windows.  When a header has a sort indicator
		* triangle, Windows resizes the indicator based on the
		* size of the n-1th font.  The fix is to always make
		* the n-1th font be the default.  This makes the sort
		* indicator always be the default size.
		*/
		OS.SendMessage (hwndHeader, OS.WM_SETFONT, 0, lParam);
		OS.SendMessage (hwndHeader, OS.WM_SETFONT, wParam, lParam);
	}
	if (itemToolTipHandle != 0) {
		OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
		OS.SendMessage (itemToolTipHandle, OS.WM_SETFONT, wParam, lParam);
	}
	if (headerToolTipHandle != 0) {
		OS.SendMessage (headerToolTipHandle, OS.WM_SETFONT, wParam, lParam);
		updateHeaderToolTips ();
	}
	return result;
}

LRESULT WM_SETREDRAW (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SETREDRAW (wParam, lParam);
	if (result != null) return result;
	if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
	/*
	* Bug in Windows.  Under certain circumstances, when
	* WM_SETREDRAW is used to turn off drawing and then
	* TVM_GETITEMRECT is sent to get the bounds of an item
	* that is not inside the client area, Windows segment
	* faults.  The fix is to call the default window proc
	* rather than the default tree proc.
	* 
	* NOTE:  This problem is intermittent and happens on
	* Windows Vista running under the theme manager.
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		int /*long*/ code = OS.DefWindowProc (handle, OS.WM_SETREDRAW, wParam, lParam);
		return code == 0 ? LRESULT.ZERO : new LRESULT (code);
	}
	return result;
}

LRESULT WM_SIZE (int /*long*/ wParam, int /*long*/ lParam) {
	if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
	/*
	* Bug in Windows.  When TVS_NOHSCROLL is set when the
	* size of the tree is zero, the scroll bar is shown the
	* next time the tree resizes.  The fix is to hide the
	* scroll bar every time the tree is resized.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.TVS_NOHSCROLL) != 0) {
		if (!OS.IsWinCE) OS.ShowScrollBar (handle, OS.SB_HORZ, false);
	}
	/*
	* Bug in Windows.  On Vista, when the Explorer theme
	* is used with a full selection tree, when the tree
	* is resized to be smaller, the rounded right edge
	* of the selected items is not drawn.  The fix is the
	* redraw the entire tree.
	*/
	if (explorerTheme && (style & SWT.FULL_SELECTION) != 0) {
		OS.InvalidateRect (handle, null, false);
	}
	if (ignoreResize) return null;
	return super.WM_SIZE (wParam, lParam);
}

LRESULT WM_SYSCOLORCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When the tree is using the explorer
	* theme, it does not use COLOR_WINDOW_TEXT for the
	* default foreground color.  The fix is to explicitly
	* set the foreground.
	*/
	if (explorerTheme) {
		if (foreground == -1) setForegroundPixel (-1);
	}
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	return result;
}

LRESULT WM_VSCROLL (int /*long*/ wParam, int /*long*/ lParam) {
	boolean fixScroll = false;
	if ((style & SWT.DOUBLE_BUFFERED) != 0) {
		int code = OS.LOWORD (wParam);
		switch (code) {
			case OS.SB_TOP:
			case OS.SB_BOTTOM:
			case OS.SB_LINEDOWN:
			case OS.SB_LINEUP:
			case OS.SB_PAGEDOWN:
			case OS.SB_PAGEUP:
				fixScroll = (style & SWT.VIRTUAL) != 0 || hooks (SWT.EraseItem) || hooks (SWT.PaintItem);
				break;
		}
	}
	if (fixScroll) {
		style &= ~SWT.DOUBLE_BUFFERED;
		if (explorerTheme) {
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, OS.TVS_EX_DOUBLEBUFFER, 0);
		}
	}
	LRESULT result = super.WM_VSCROLL (wParam, lParam);
	if (fixScroll) {
		style |= SWT.DOUBLE_BUFFERED;
		if (explorerTheme) {
			OS.SendMessage (handle, OS.TVM_SETEXTENDEDSTYLE, OS.TVS_EX_DOUBLEBUFFER, OS.TVS_EX_DOUBLEBUFFER);
		}
	}
	if (result != null) return result;
	return result;
}

LRESULT WM_TIMER (int wParam, int lParam) {
	LRESULT result = super.WM_TIMER (wParam, lParam);
	if (result != null) return result;

	/* Bug in Windows. When the expandos are visible (or in process of fading away)
	 * and the tree control is hidden the animation timer does not stop calling the 
	 * window proc till the tree is visible again. This can cause performance problems
	 * specially in cases there the application has several tree controls in this state.
	 * The fix is to detect a timer that repeats itself several times when the control
	 * is not visible and stop it. The timer is stopped by sending a fake mouse move event.
	 * 
	 * Note: Just killing the timer could cause some internal clean up task related to the 
	 * animation not to run.
	 */
	int /*long*/ bits = OS.SendMessage (handle, OS.TVM_GETEXTENDEDSTYLE, 0, 0);
	if ((bits & OS.TVS_EX_FADEINOUTEXPANDOS) != 0) {
		if (!OS.IsWindowVisible (handle)) {
			if (lastTimerID == wParam) {
				lastTimerCount++;
			} else {
				lastTimerCount = 0;
			}
			lastTimerID = wParam;
			if (lastTimerCount >= TIMER_MAX_COUNT) {
				OS.CallWindowProc (TreeProc, handle, OS.WM_MOUSEMOVE, 0, 0);
				lastTimerID = -1;
				lastTimerCount = 0;
			}
		} else {
			lastTimerID = -1;
			lastTimerCount = 0;
		}
	}
	return result;
};

LRESULT wmColorChild (int /*long*/ wParam, int /*long*/ lParam) {
	if (findImageControl () != null) {
		if (OS.COMCTL32_MAJOR < 6) {
			return super.wmColorChild (wParam, lParam);
		}
		return new LRESULT (OS.GetStockObject (OS.NULL_BRUSH));
	}
	/*
	* Feature in Windows.  Tree controls send WM_CTLCOLOREDIT
	* to allow application code to change the default colors.
	* This is undocumented and conflicts with TVM_SETTEXTCOLOR
	* and TVM_SETBKCOLOR, the documented way to do this.  The
	* fix is to ignore WM_CTLCOLOREDIT messages from trees.
	*/
	return null;
}

LRESULT wmNotify (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	if (hdr.hwndFrom == itemToolTipHandle) {
		LRESULT result = wmNotifyToolTip (hdr, wParam, lParam);
		if (result != null) return result;
	}
	if (hdr.hwndFrom == hwndHeader) {
		LRESULT result = wmNotifyHeader (hdr, wParam, lParam);
		if (result != null) return result;
	}
	return super.wmNotify (hdr, wParam, lParam);
}

LRESULT wmNotifyChild (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	switch (hdr.code) {
		case OS.TVN_GETDISPINFOA:
		case OS.TVN_GETDISPINFOW: {
			NMTVDISPINFO lptvdi = new NMTVDISPINFO ();
			OS.MoveMemory (lptvdi, lParam, NMTVDISPINFO.sizeof);
			if ((style & SWT.VIRTUAL) != 0) {
				/*
				* Feature in Windows.  When a new tree item is inserted
				* using TVM_INSERTITEM, a TVN_GETDISPINFO is sent before
				* TVM_INSERTITEM returns and before the item is added to
				* the items array.  The fix is to check for null.
				* 
				* NOTE: This only happens on XP with the version 6.00 of
				* COMCTL32.DLL.
				*/
				boolean checkVisible = true;
				/*
				* When an item is being deleted from a virtual tree, do not
				* allow the application to provide data for a new item that
				* becomes visible until the item has been removed from the
				* items array.  Because arbitrary application code can run
				* during the callback, the items array might be accessed
				* in an inconsistent state.  Rather than answering the data
				* right away, queue a redraw for later.
				*/
				if (!ignoreShrink) {
					if (items != null && lptvdi.lParam != -1) {
						if (items [(int)/*64*/lptvdi.lParam] != null && items [(int)/*64*/lptvdi.lParam].cached) {
							checkVisible = false;
						}
					}
				}
				if (checkVisible) {
					if (!getDrawing () || !OS.IsWindowVisible (handle)) break;
					RECT itemRect = new RECT ();
					if (!OS.TreeView_GetItemRect (handle, lptvdi.hItem, itemRect, false)) {
						break;
					}
					RECT rect = new RECT ();
					OS.GetClientRect (handle, rect);
					if (!OS.IntersectRect (rect, rect, itemRect)) break;
					if (ignoreShrink) {
						OS.InvalidateRect (handle, rect, true);
						break;
					}
				}
			}
			if (items == null) break;
			/*
			* Bug in Windows.  If the lParam field of TVITEM
			* is changed during custom draw using TVM_SETITEM,
			* the lItemlParam field of the NMTVCUSTOMDRAW struct
			* is not updated until the next custom draw.  The
			* fix is to query the field from the item instead
			* of using the struct.
			*/
			int id = (int)/*64*/lptvdi.lParam;
			if ((style & SWT.VIRTUAL) != 0) {
				if (id == -1) {
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
					tvItem.hItem = lptvdi.hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					id = (int)/*64*/tvItem.lParam;
				}
			}
			TreeItem item = _getItem (lptvdi.hItem, id);
			/*
			* Feature in Windows.  When a new tree item is inserted
			* using TVM_INSERTITEM, a TVN_GETDISPINFO is sent before
			* TVM_INSERTITEM returns and before the item is added to
			* the items array.  The fix is to check for null.
			* 
			* NOTE: This only happens on XP with the version 6.00 of
			* COMCTL32.DLL.
			* 
			* Feature in Windows.  When TVM_DELETEITEM is called with
			* TVI_ROOT to remove all items from a tree, under certain
			* circumstances, the tree sends TVN_GETDISPINFO for items
			* that are about to be disposed.  The fix is to check for
			* disposed items.
			*/
			if (item == null) break;
			if (item.isDisposed ()) break;
			if (!item.cached) {
				if ((style & SWT.VIRTUAL) != 0) {
					if (!checkData (item, false)) break;
				}
				if (painted) item.cached = true;
			}
			int index = 0;
			if (hwndHeader != 0) {
				index = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, 0, 0);
			}
			if ((lptvdi.mask & OS.TVIF_TEXT) != 0) {
				String string = null;
				if (index == 0) {
					string = item.text;
				} else {
					String [] strings  = item.strings;
					if (strings != null) string = strings [index];
				}
				if (string != null) {
					TCHAR buffer = new TCHAR (getCodePage (), string, false);
					int byteCount = Math.min (buffer.length (), lptvdi.cchTextMax - 1) * TCHAR.sizeof;
					OS.MoveMemory (lptvdi.pszText, buffer, byteCount);
					OS.MoveMemory (lptvdi.pszText + byteCount, new byte [TCHAR.sizeof], TCHAR.sizeof);
					lptvdi.cchTextMax = Math.min (lptvdi.cchTextMax, string.length () + 1);
				}
			}
			if ((lptvdi.mask & (OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE)) != 0) {
				Image image = null;
				if (index == 0) {
					image = item.image;
				} else {
					Image [] images  = item.images;
					if (images != null) image = images [index];
				}
				lptvdi.iImage = lptvdi.iSelectedImage = OS.I_IMAGENONE;
				if (image != null) {
					lptvdi.iImage = lptvdi.iSelectedImage = imageIndex (image, index);
				}
				if (explorerTheme && OS.IsWindowEnabled (handle)) {
					if (findImageControl () != null) {
						lptvdi.iImage = lptvdi.iSelectedImage = OS.I_IMAGENONE;
					}
				}
			}
			OS.MoveMemory (lParam, lptvdi, NMTVDISPINFO.sizeof);
			break;
		}
		case OS.NM_CUSTOMDRAW: {
			if (hdr.hwndFrom == hwndHeader) break;
			if (hooks (SWT.MeasureItem)) {
				if (hwndHeader == 0) createParent ();
			}
			if (!customDraw && findImageControl () == null) {
				if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
					if (sortColumn == null || sortDirection == SWT.NONE) {
						break;
					}
				}
			}
			NMTVCUSTOMDRAW nmcd = new NMTVCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMTVCUSTOMDRAW.sizeof);		
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: return CDDS_PREPAINT (nmcd, wParam, lParam);
				case OS.CDDS_ITEMPREPAINT: return CDDS_ITEMPREPAINT (nmcd, wParam, lParam);
				case OS.CDDS_ITEMPOSTPAINT: return CDDS_ITEMPOSTPAINT (nmcd, wParam, lParam);
				case OS.CDDS_POSTPAINT: return CDDS_POSTPAINT (nmcd, wParam, lParam);
			}
			break;
		}
		case OS.NM_DBLCLK: {
			/*
			* When the user double clicks on a tree item
			* or a line beside the item, the window proc
			* for the tree collapses or expand the branch.
			* When application code associates an action
			* with double clicking, then the tree expand
			* is unexpected and unwanted.  The fix is to
			* avoid the operation by testing to see whether
			* the mouse was inside a tree item.
			*/
			if (hooks (SWT.MeasureItem)) return LRESULT.ONE;
			if (hooks (SWT.DefaultSelection)) {
				POINT pt = new POINT ();
				int pos = OS.GetMessagePos ();
				OS.POINTSTOPOINT (pt, pos);
				OS.ScreenToClient (handle, pt);
				TVHITTESTINFO lpht = new TVHITTESTINFO ();
				lpht.x = pt.x;
				lpht.y = pt.y;
				OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
				if (lpht.hItem != 0 && (lpht.flags & OS.TVHT_ONITEM) != 0) {
					return LRESULT.ONE;
				}
			}
			break;
		}
		/*
		* Bug in Windows.  On Vista, when TVM_SELECTITEM is called
		* with TVGN_CARET in order to set the selection, for some
		* reason, Windows deselects the previous two items that
		* were selected.  The fix is to stop the selection from
		* changing on all but the item that is supposed to be
		* selected.
		*/
		case OS.TVN_ITEMCHANGINGA:
		case OS.TVN_ITEMCHANGINGW: {
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				if ((style & SWT.MULTI) != 0) {
					if (hSelect != 0) {
						NMTVITEMCHANGE pnm = new NMTVITEMCHANGE ();
						OS.MoveMemory (pnm, lParam, NMTVITEMCHANGE.sizeof);
						if (hSelect == pnm.hItem) break;
						return LRESULT.ONE;
					}
				}
			}			
			break;
		}
		case OS.TVN_SELCHANGINGA:
		case OS.TVN_SELCHANGINGW: {
			if ((style & SWT.MULTI) != 0) {
				if (lockSelection) {
					/* Save the old selection state for both items */
					NMTREEVIEW treeView = new NMTREEVIEW ();
					OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
					TVITEM tvItem = treeView.itemOld;
					oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
					tvItem = treeView.itemNew;
					newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
				}
			}
			if (!ignoreSelect && !ignoreDeselect) {
				hAnchor = 0;
				if ((style & SWT.MULTI) != 0) deselectAll ();
			}
			break;
		}
		case OS.TVN_SELCHANGEDA:
		case OS.TVN_SELCHANGEDW: {
			NMTREEVIEW treeView = null;
			if ((style & SWT.MULTI) != 0) {
				if (lockSelection) {
					/* Restore the old selection state of both items */
					if (oldSelected) {
						if (treeView == null) {
							treeView = new NMTREEVIEW ();
							OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
						}
						TVITEM tvItem = treeView.itemOld;
						tvItem.mask = OS.TVIF_STATE;
						tvItem.stateMask = OS.TVIS_SELECTED;
						tvItem.state = OS.TVIS_SELECTED;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
					if (!newSelected && ignoreSelect) {
						if (treeView == null) {
							treeView = new NMTREEVIEW ();
							OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
						}
						TVITEM tvItem = treeView.itemNew;
						tvItem.mask = OS.TVIF_STATE;
						tvItem.stateMask = OS.TVIS_SELECTED;
						tvItem.state = 0;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
			if (!ignoreSelect) {
				if (treeView == null) {
					treeView = new NMTREEVIEW ();
					OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
				}
				TVITEM tvItem = treeView.itemNew;
				hAnchor = tvItem.hItem;
				Event event = new Event ();
				event.item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
				sendSelectionEvent (SWT.Selection, event, false);
			}
			updateScrollBar ();
			break;
		}
		case OS.TVN_ITEMEXPANDINGA:
		case OS.TVN_ITEMEXPANDINGW: {
			if (itemToolTipHandle != 0) OS.ShowWindow (itemToolTipHandle, OS.SW_HIDE);
			boolean runExpanded = false;
			if ((style & SWT.VIRTUAL) != 0) style &= ~SWT.DOUBLE_BUFFERED;
			if (hooks (SWT.EraseItem) || hooks (SWT.PaintItem)) style &= ~SWT.DOUBLE_BUFFERED;
			if (findImageControl () != null && getDrawing () && OS.IsWindowVisible (handle)) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
			}
			/*
			* Bug in Windows.  When TVM_SETINSERTMARK is used to set
			* an insert mark for a tree and an item is expanded or
			* collapsed near the insert mark, the tree does not redraw
			* the insert mark properly.  The fix is to hide and show
			* the insert mark whenever an item is expanded or collapsed.
			*/
			if (hInsert != 0) {
				OS.SendMessage (handle, OS.TVM_SETINSERTMARK, 0, 0);
			}
			if (!ignoreExpand) {
				NMTREEVIEW treeView = new NMTREEVIEW ();
				OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
				TVITEM tvItem = treeView.itemNew;
				/*
				* Feature in Windows.  In some cases, TVM_ITEMEXPANDING
				* is sent from within TVM_DELETEITEM for the tree item
				* being destroyed.  By the time the message is sent,
				* the item has already been removed from the list of
				* items.  The fix is to check for null. 
				*/
				if (items == null) break;
				TreeItem item = _getItem (tvItem.hItem, (int)/*64*/tvItem.lParam);
				if (item == null) break;
				Event event = new Event ();
				event.item = item;
				switch (treeView.action) {
					case OS.TVE_EXPAND:
						/*
						* Bug in Windows.  When the numeric keypad asterisk
						* key is used to expand every item in the tree, Windows
						* sends TVN_ITEMEXPANDING to items in the tree that
						* have already been expanded.  The fix is to detect
						* that the item is already expanded and ignore the
						* notification.
						*/
						if ((tvItem.state & OS.TVIS_EXPANDED) == 0) {
							sendEvent (SWT.Expand, event);
							if (isDisposed ()) return LRESULT.ZERO;
						}
						break;
					case OS.TVE_COLLAPSE:
						sendEvent (SWT.Collapse, event);
						if (isDisposed ()) return LRESULT.ZERO;
						break;
				}
				/*
				* Bug in Windows.  When all of the items are deleted during
				* TVN_ITEMEXPANDING, Windows does not send TVN_ITEMEXPANDED.
				* The fix is to detect this case and run the TVN_ITEMEXPANDED
				* code in this method.
				*/
				int /*long*/ hFirstItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, tvItem.hItem);
				runExpanded = hFirstItem == 0;
			}
			if (!runExpanded) break;
			//FALL THROUGH
		}
		case OS.TVN_ITEMEXPANDEDA:
		case OS.TVN_ITEMEXPANDEDW: {
			if ((style & SWT.VIRTUAL) != 0) style |= SWT.DOUBLE_BUFFERED;
			if (hooks (SWT.EraseItem) || hooks (SWT.PaintItem)) style |= SWT.DOUBLE_BUFFERED;
			if (findImageControl () != null && getDrawing () /*&& OS.IsWindowVisible (handle)*/) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
			/*
			* Bug in Windows.  When TVM_SETINSERTMARK is used to set
			* an insert mark for a tree and an item is expanded or
			* collapsed near the insert mark, the tree does not redraw
			* the insert mark properly.  The fix is to hide and show
			* the insert mark whenever an item is expanded or collapsed.
			*/
			if (hInsert != 0) {
				OS.SendMessage (handle, OS.TVM_SETINSERTMARK, insertAfter ? 1 : 0, hInsert);
			}
			/*
			* Bug in Windows.  When a tree item that has an image
			* with alpha is expanded or collapsed, the area where
			* the image is drawn is not erased before it is drawn.
			* This means that the image gets darker each time.
			* The fix is to redraw the item.
			*/
			if (!OS.IsWinCE && OS.COMCTL32_MAJOR >= 6) {
				if (imageList != null) {
					NMTREEVIEW treeView = new NMTREEVIEW ();
					OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
					TVITEM tvItem = treeView.itemNew;
					if (tvItem.hItem != 0) {
						int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
						if ((bits & OS.TVS_FULLROWSELECT) == 0) {
							RECT rect = new RECT ();
							if (OS.TreeView_GetItemRect (handle, tvItem.hItem, rect, false)) {
								OS.InvalidateRect (handle, rect, true);
							}
						}
					}
				}
			}
			updateScrollBar ();
			break;
		}
		case OS.TVN_BEGINDRAGA:
		case OS.TVN_BEGINDRAGW:
			if (OS.GetKeyState (OS.VK_LBUTTON) >= 0) break;
			//FALL THROUGH
		case OS.TVN_BEGINRDRAGA:
		case OS.TVN_BEGINRDRAGW: {
			dragStarted = true;
			NMTREEVIEW treeView = new NMTREEVIEW ();
			OS.MoveMemory (treeView, lParam, NMTREEVIEW.sizeof);
			TVITEM tvItem = treeView.itemNew;
			if (tvItem.hItem != 0 && (tvItem.state & OS.TVIS_SELECTED) == 0) {
				hSelect = tvItem.hItem;
				ignoreSelect = ignoreDeselect = true;
				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, tvItem.hItem);
				ignoreSelect = ignoreDeselect = false;
				hSelect = 0;
			}
			break;
		}
		case OS.NM_RECOGNIZEGESTURE: {
			/* 
			* Feature in Pocket PC.  The tree and table controls detect the tap
			* and hold gesture by default. They send a GN_CONTEXTMENU message to show
			* the popup menu.  This default behaviour is unwanted on Pocket PC 2002
			* when no menu has been set, as it still draws a red circle.  The fix
			* is to disable this default behaviour when no menu is set by returning
			* TRUE when receiving the Pocket PC 2002 specific NM_RECOGNIZEGESTURE
			* message.
			*/
			if (OS.IsPPC) {
				boolean hasMenu = menu != null && !menu.isDisposed ();
				if (!hasMenu && !hooks (SWT.MenuDetect)) return LRESULT.ONE;
			}
			break;
		}
		case OS.GN_CONTEXTMENU: {
			if (OS.IsPPC) {
				boolean hasMenu = menu != null && !menu.isDisposed ();
				if (hasMenu || hooks (SWT.MenuDetect)) {
					NMRGINFO nmrg = new NMRGINFO ();
					OS.MoveMemory (nmrg, lParam, NMRGINFO.sizeof);
					showMenu (nmrg.x, nmrg.y);
					gestureCompleted = true;
					return LRESULT.ONE;
				}
			}
			break;
		}
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}

LRESULT wmNotifyHeader (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  On NT, the automatically created
	* header control is created as a UNICODE window, not an
	* ANSI window despite the fact that the parent is created
	* as an ANSI window.  This means that it sends UNICODE
	* notification messages to the parent window on NT for
	* no good reason.  The data and size in the NMHEADER and
	* HDITEM structs is identical between the platforms so no
	* different message is actually necessary.  Despite this,
	* Windows sends different messages.  The fix is to look
	* for both messages, despite the platform.  This works
	* because only one will be sent on either platform, never
	* both.
	*/
	switch (hdr.code) {
		case OS.HDN_BEGINTRACKW:
		case OS.HDN_BEGINTRACKA:
		case OS.HDN_DIVIDERDBLCLICKW:
		case OS.HDN_DIVIDERDBLCLICKA: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			TreeColumn column = columns [phdn.iItem];
			if (column != null && !column.getResizable ()) {
				return LRESULT.ONE;
			}
			ignoreColumnMove = true;
			switch (hdr.code) {
				case OS.HDN_DIVIDERDBLCLICKW:
				case OS.HDN_DIVIDERDBLCLICKA:
					if (column != null) column.pack ();
			}
			break;
		}
		case OS.NM_RELEASEDCAPTURE: {
			if (!ignoreColumnMove) {
				for (int i=0; i<columnCount; i++) {
					TreeColumn column = columns [i];
					column.updateToolTip (i);
				}
				updateImageList ();
			}
			ignoreColumnMove = false;
			break;
		}
		case OS.HDN_BEGINDRAG: {
			if (ignoreColumnMove) return LRESULT.ONE;
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			if (phdn.iItem != -1) {
				TreeColumn column = columns [phdn.iItem];
				if (column != null && !column.getMoveable ()) {
					ignoreColumnMove = true;
					return LRESULT.ONE;
				}
			}
			break;
		}
		case OS.HDN_ENDDRAG: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			if (phdn.iItem != -1 && phdn.pitem != 0) {
				HDITEM pitem = new HDITEM ();
				OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
				if ((pitem.mask & OS.HDI_ORDER) != 0 && pitem.iOrder != -1) {
					int [] order = new int [columnCount];
					OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, order);
					int index = 0;
					while (index < order.length) {
					 	if (order [index] == phdn.iItem) break;
						index++;
					}
					if (index == order.length) index = 0;
					if (index == pitem.iOrder) break;
					int start = Math.min (index, pitem.iOrder);
					int end = Math.max (index, pitem.iOrder);
					RECT rect = new RECT (), headerRect = new RECT ();
					OS.GetClientRect (handle, rect);
					OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, order [start], headerRect);
					rect.left = Math.max (rect.left, headerRect.left);
					OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, order [end], headerRect);
					rect.right = Math.min (rect.right, headerRect.right);
					OS.InvalidateRect (handle, rect, true);
					ignoreColumnMove = false;
					for (int i=start; i<=end; i++) {
						TreeColumn column = columns [order [i]];
						if (!column.isDisposed ()) {
							column.postEvent (SWT.Move);
						}
					}
				}
			}
			break;
		}
		case OS.HDN_ITEMCHANGINGW:
		case OS.HDN_ITEMCHANGINGA: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			if (phdn.pitem != 0) {
				HDITEM newItem = new HDITEM ();
				OS.MoveMemory (newItem, phdn.pitem, HDITEM.sizeof);
				if ((newItem.mask & OS.HDI_WIDTH) != 0) {
					RECT rect = new RECT ();
					OS.GetClientRect (handle, rect);
					HDITEM oldItem = new HDITEM ();
					oldItem.mask = OS.HDI_WIDTH;
					OS.SendMessage (hwndHeader, OS.HDM_GETITEM, phdn.iItem, oldItem);
					int deltaX = newItem.cxy - oldItem.cxy;
					RECT headerRect = new RECT ();
					OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, phdn.iItem, headerRect);
					int gridWidth = linesVisible ? GRID_WIDTH : 0;
					rect.left = headerRect.right - gridWidth;
					int newX = rect.left + deltaX;
					rect.right = Math.max (rect.right, rect.left + Math.abs (deltaX));
					if (explorerTheme || (findImageControl () != null || hooks (SWT.MeasureItem) || hooks (SWT.EraseItem) || hooks (SWT.PaintItem))) {
						rect.left -= OS.GetSystemMetrics (OS.SM_CXFOCUSBORDER);
						OS.InvalidateRect (handle, rect, true);
						OS.OffsetRect (rect, deltaX, 0);
						OS.InvalidateRect (handle, rect, true);
					} else {
						int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
						OS.ScrollWindowEx (handle, deltaX, 0, rect, null, 0, null, flags);
					}
					if (OS.SendMessage (hwndHeader, OS.HDM_ORDERTOINDEX, phdn.iItem, 0) != 0) {
						rect.left = headerRect.left;
						rect.right = newX;
						OS.InvalidateRect (handle, rect, true);
					}
					setScrollWidth ();
				}
			}
			break;
		}
		case OS.HDN_ITEMCHANGEDW:
		case OS.HDN_ITEMCHANGEDA: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			if (phdn.pitem != 0) {
				HDITEM pitem = new HDITEM ();
				OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
				if ((pitem.mask & OS.HDI_WIDTH) != 0) {
					if (ignoreColumnMove) {
						if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
							int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
							OS.RedrawWindow (handle, null, 0, flags);
						} else {
							if ((style & SWT.DOUBLE_BUFFERED) == 0) {
								int oldStyle = style;
								style |= SWT.DOUBLE_BUFFERED;
								OS.UpdateWindow (handle);
								style = oldStyle;
							}
						}
					}
					TreeColumn column = columns [phdn.iItem];
					if (column != null) {
						column.updateToolTip (phdn.iItem);
						column.sendEvent (SWT.Resize);
						if (isDisposed ()) return LRESULT.ZERO;	
						TreeColumn [] newColumns = new TreeColumn [columnCount];
						System.arraycopy (columns, 0, newColumns, 0, columnCount);
						int [] order = new int [columnCount];
						OS.SendMessage (hwndHeader, OS.HDM_GETORDERARRAY, columnCount, order);
						boolean moved = false;
						for (int i=0; i<columnCount; i++) {
							TreeColumn nextColumn = newColumns [order [i]];
							if (moved && !nextColumn.isDisposed ()) {
								nextColumn.updateToolTip (order [i]);
								nextColumn.sendEvent (SWT.Move);
							}
							if (nextColumn == column) moved = true;
						}
					}
				}
				setScrollWidth ();
			}
			break;
		}
		case OS.HDN_ITEMCLICKW:
		case OS.HDN_ITEMCLICKA: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			TreeColumn column = columns [phdn.iItem];
			if (column != null) {
				column.sendSelectionEvent (SWT.Selection);
			}
			break;
		}
		case OS.HDN_ITEMDBLCLICKW:      
		case OS.HDN_ITEMDBLCLICKA: {
			NMHEADER phdn = new NMHEADER ();
			OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
			TreeColumn column = columns [phdn.iItem];
			if (column != null) {
				column.sendSelectionEvent (SWT.DefaultSelection);
			}
			break;
		}
	}
	return null;
}

LRESULT wmNotifyToolTip (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	if (OS.IsWinCE) return null;
	switch (hdr.code) {
		case OS.NM_CUSTOMDRAW: {
			NMTTCUSTOMDRAW nmcd = new NMTTCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMTTCUSTOMDRAW.sizeof);
			return wmNotifyToolTip (nmcd, lParam);
		}
		case OS.TTN_SHOW: {
			LRESULT result = super.wmNotify (hdr, wParam, lParam);
			if (result != null) return result;
			int pos = OS.GetMessagePos ();
			POINT pt = new POINT();
			OS.POINTSTOPOINT (pt, pos);
			OS.ScreenToClient (handle, pt);
			int [] index = new int [1];
			TreeItem [] item = new TreeItem [1];
			RECT [] cellRect = new RECT [1], itemRect = new RECT [1];
			if (findCell (pt.x, pt.y, item, index, cellRect, itemRect)) {
				RECT toolRect = toolTipRect (itemRect [0]);
				OS.MapWindowPoints (handle, 0, toolRect, 2);
				int width = toolRect.right - toolRect.left;
				int height = toolRect.bottom - toolRect.top;
				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOZORDER | OS.SWP_NOSIZE;
				if (isCustomToolTip ()) flags &= ~OS.SWP_NOSIZE;
				SetWindowPos (itemToolTipHandle, 0, toolRect.left, toolRect.top, width, height, flags);
				return LRESULT.ONE;
			}
			return result;
		}
	}
	return null;
}

LRESULT wmNotifyToolTip (NMTTCUSTOMDRAW nmcd, int /*long*/ lParam) {
	if (OS.IsWinCE) return null;
	switch (nmcd.dwDrawStage) {
		case OS.CDDS_PREPAINT: {
			if (isCustomToolTip ()) {
				//TEMPORARY CODE
				//nmcd.uDrawFlags |= OS.DT_CALCRECT;
				//OS.MoveMemory (lParam, nmcd, NMTTCUSTOMDRAW.sizeof);
				if (!OS.IsWinCE && OS.WIN32_VERSION < OS.VERSION (6, 0)) {
					OS.SetTextColor (nmcd.hdc, OS.GetSysColor (OS.COLOR_INFOBK));
				}
				return new LRESULT (OS.CDRF_NOTIFYPOSTPAINT | OS.CDRF_NEWFONT);
			}
			break;
		}
		case OS.CDDS_POSTPAINT: {
			if (!OS.IsWinCE && OS.WIN32_VERSION < OS.VERSION (6, 0)) {
				OS.SetTextColor (nmcd.hdc, OS.GetSysColor (OS.COLOR_INFOTEXT));
			}
			if (OS.SendMessage (itemToolTipHandle, OS.TTM_GETCURRENTTOOL, 0, 0) != 0) {
				TOOLINFO lpti = new TOOLINFO ();
				lpti.cbSize = TOOLINFO.sizeof;
				if (OS.SendMessage (itemToolTipHandle, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
					int [] index = new int [1];
					TreeItem [] item = new TreeItem [1];
					RECT [] cellRect = new RECT [1], itemRect = new RECT [1];
					int pos = OS.GetMessagePos ();
					POINT pt = new POINT();
					OS.POINTSTOPOINT (pt, pos);
					OS.ScreenToClient (handle, pt);
					if (findCell (pt.x, pt.y, item, index, cellRect, itemRect)) {
						int /*long*/ hDC = OS.GetDC (handle);
						int /*long*/ hFont = item [0].fontHandle (index [0]);
						if (hFont == -1) hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
						int /*long*/ oldFont = OS.SelectObject (hDC, hFont);
						boolean drawForeground = true;
						cellRect [0] = item [0].getBounds (index [0], true, true, false, false, false, hDC);
						if (hooks (SWT.EraseItem)) {
							Event event = sendEraseItemEvent (item [0], nmcd, index [0], cellRect [0]);
							if (isDisposed () || item [0].isDisposed ()) break;
							if (event.doit) {
								drawForeground = (event.detail & SWT.FOREGROUND) != 0;
							} else {
								drawForeground = false;
							}
						}
						if (drawForeground) {
							int nSavedDC = OS.SaveDC (nmcd.hdc);
							int gridWidth = getLinesVisible () ? Table.GRID_WIDTH : 0;
							RECT insetRect = toolTipInset (cellRect [0]);
							OS.SetWindowOrgEx (nmcd.hdc, insetRect.left, insetRect.top, null);
							GCData data = new GCData ();
							data.device = display;
							data.foreground = OS.GetTextColor (nmcd.hdc);
							data.background = OS.GetBkColor (nmcd.hdc);
							data.font = Font.win32_new (display, hFont);
							GC gc = GC.win32_new (nmcd.hdc, data);
							int x = cellRect [0].left + INSET;
							if (index [0] != 0) x -= gridWidth;
							Image image = item [0].getImage (index [0]);
							if (image != null || index [0] == 0) {
								Point size = getImageSize ();
								RECT imageRect = item [0].getBounds (index [0], false, true, false, false, false, hDC);
								if (imageList == null) size.x = imageRect.right - imageRect.left;
								if (image != null) {
									Rectangle rect = image.getBounds ();
									gc.drawImage (image, rect.x, rect.y, rect.width, rect.height, x, imageRect.top, size.x, size.y);
									x += INSET + (index [0] == 0 ? 1 : 0);
								}
								x += size.x;
							} else {
								x += INSET;
							}
							String string = item [0].getText (index [0]);
							if (string != null) {
								int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_VCENTER;
								TreeColumn column = columns != null ? columns [index [0]] : null;
								if (column != null) {
									if ((column.style & SWT.CENTER) != 0) flags |= OS.DT_CENTER;
									if ((column.style & SWT.RIGHT) != 0) flags |= OS.DT_RIGHT;
								}
								TCHAR buffer = new TCHAR (getCodePage (), string, false);
								RECT textRect = new RECT ();
								OS.SetRect (textRect, x, cellRect [0].top, cellRect [0].right, cellRect [0].bottom);
								OS.DrawText (nmcd.hdc, buffer, buffer.length (), textRect, flags);
							}
							gc.dispose ();
							OS.RestoreDC (nmcd.hdc, nSavedDC);
						}
						if (hooks (SWT.PaintItem)) {
							itemRect [0] = item [0].getBounds (index [0], true, true, false, false, false, hDC);
							sendPaintItemEvent (item [0], nmcd, index[0], itemRect [0]);
						}
						OS.SelectObject (hDC, oldFont);
						OS.ReleaseDC (handle, hDC);
					}
					break;
				}
			}
			break;
		}
	}
	return null;
}

}
