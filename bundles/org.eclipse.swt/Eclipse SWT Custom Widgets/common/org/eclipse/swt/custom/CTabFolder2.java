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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
* DO NOT USE - UNDER CONSTRUCTION
*
* @ since 3.0
*/

/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>CTabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>CTabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM, FLAT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * <dd>"CTabFolder"</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
 
public class CTabFolder2 extends Composite {
	
	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the form.
	 *
	 * The default value is 0.
	 */
 	public int marginWidth = 0;
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the form.
	 *
	 * The default value is 0.
	 */
 	public int marginHeight = 0;
	
 	/**
	 * A multiple of the tab height that specifies the minimum width to which a tab 
	 * will be compressed before scrolling arrows are used to navigate the tabs.
	 * 
	 * NOTE This field is badly named for historical reasons.  It is not static.
	 */
	public int MIN_TAB_WIDTH = 8;
	
	/**
	 * Color of innermost line of drop shadow border.
	 */
	public static RGB borderInsideRGB  = new RGB (132, 130, 132);
	/**
	 * Color of middle line of drop shadow border.
	 */
	public static RGB borderMiddleRGB  = new RGB (143, 141, 138);
	/**
	 * Color of outermost line of drop shadow border.
	 */
	public static RGB borderOutsideRGB = new RGB (171, 168, 165); 

	/* sizing, positioning, appearance */
	int xClient, yClient;
	boolean onBottom = false;
	boolean single = false;
	boolean fixedTabHeight;
	int tabHeight;
	
	/* item management */
	CTabItem2 items[] = new CTabItem2[0];
	int selectedIndex = -1;
	int topTabIndex = -1; // index of the left most visible tab.

	/* External Listener management */
	CTabFolderCloseListener[] closeListeners = new CTabFolderCloseListener[0];
	CTabFolderExpandListener[] expandListeners = new CTabFolderExpandListener[0];
	CTabFolderListListener[] listListeners = new CTabFolderListListener[0];
	
	/* Color appearance */
	Image backgroundImage;
	Color[] gradientColors;
	int[] gradientPercents;
	Color selectionForeground;
	Color selectionBackground;
	static Color borderColor1;
	static Color borderColor2;
	static Color borderColor3;
	
	// close, expand and chevron buttons
	boolean showClose = false;
	Rectangle closeRect = new Rectangle(0, 0, 0, 0);
	int closeImageState = NORMAL;
	
	Rectangle chevronRect = new Rectangle(0, 0, 0, 0);
	int chevronImageState = NORMAL;
	
	boolean showExpand = false;
	Rectangle expandRect = new Rectangle(0, 0, 0, 0);
	boolean expanded = true;
	int expandImageState = NORMAL;
	
	boolean tipShowing;
	
	// borders and shapes
	boolean showHighlight =  false;
	int borderLeft = 0;
	int borderRight = 0;
	int borderTop = 0;
	int borderBottom = 0;
	int[] curve;
	
	// when disposing CTabFolder, don't try to layout the items or 
	// change the selection as each child is destroyed.
	boolean inDispose = false;

	// keep track of size changes in order to redraw only affected area
	// on Resize
	Point oldSize;
	Font oldFont;
	
	// insertion marker
	int insertionIndex = -2; // Index of insert marker.  Marker always shown after index.
	                         // -2 means no insert marker
	
	// internal constants
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;
	static final int HIGHLIGHT_HEADER = 5;
	static final int HIGHLIGHT_MARGIN = 3;
	static final int CURVE_WIDTH = 50;
	static final int CURVE_RIGHT = 30;
	static final int CURVE_LEFT = 30;
	static final int[] TOP_LEFT_CORNER = new int[] {0,5, 1,4, 1,3, 2,2, 3,1, 4,1, 5,0,};
	static final int[] TOP_RIGHT_CORNER = new int[] {-5,0, -4,1, -3,1, -2,2, -1,3, -1,4, 0,5};
	static final int[] BOTTOM_LEFT_CORNER = new int[] {0,-5, 1,-5, 1,-3, 2,-3, 2,-2, 3,-2, 3,-1, 5,-1, 5,0};
	static final int[] BOTTOM_RIGHT_CORNER = new int[] {-5,0, -5,-1, -4,-1, -1,-4, -1,-5, 0,-5};
	static final int[] TOP_LEFT_OUTSIDE_CORNER = new int[] {0,6, 1,5, 1,4, 4,1, 5,1, 6,0};
	static final int[] TOP_RIGHT_OUTSIDE_CORNER = new int[] {-6,0, -5,1, -4,1, -1,4, -1,5, 0,6};
	static final int[] BOTTOM_LEFT_OUTSIDE_CORNER = new int[] {0,-6, 1,-5, 1,-4, 4,-1, 5,-1, 6,0};
	static final int[] BOTTOM_RIGHT_OUTSIDE_CORNER = new int[] {-6,0, -5,-1, -4,-1, -1,-4, -1,-5, 0,-6};

	static final int SELECTION_FOREGROUND = SWT.COLOR_TITLE_FOREGROUND;
	static final int SELECTION_BACKGROUND = SWT.COLOR_TITLE_BACKGROUND;
	static final int BORDER1_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;
	static final int BORDER2_COLOR = SWT.COLOR_WIDGET_DARK_SHADOW;
	static final int BORDER3_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;
	static final int FOREGROUND = SWT.COLOR_TITLE_INACTIVE_FOREGROUND;
	static final int BACKGROUND = SWT.COLOR_TITLE_INACTIVE_BACKGROUND;
	
	static final int NORMAL = 1;
	static final int HOT = 2;
	static final int SELECTED = 3;
	static final RGB CLOSE_BORDER = new RGB(221, 106, 106);
	static final RGB CLOSE_FILL = new RGB(214, 195, 195);
	static final RGB EXPAND_BORDER = new RGB(114, 106, 221);
	static final RGB EXPAND_FILL = new RGB(199, 196, 242);
	static final RGB CHEVRON_BORDER = new RGB(111, 220, 106);
	static final RGB CHEVRON_FILL = new RGB(199, 242, 196);
	

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
 * </ul>
 *
 * @see SWT#TOP
 * @see SWT#BOTTOM
 * @see SWT#FLAT
 * @see SWT#BORDER
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see #getStyle()
 */
public CTabFolder2(Composite parent, int style) {
	super(parent, checkStyle (style));
	onBottom = (getStyle() & SWT.BOTTOM) != 0;
	single = (style & SWT.SINGLE) != 0;
	borderLeft = (style & SWT.BORDER) != 0 ? ((style & SWT.FLAT) != 0 ? 1 : 1 + HIGHLIGHT_MARGIN) : 0;
	borderRight = (style & SWT.BORDER) != 0 ? ((style & SWT.FLAT) != 0 ? 1 : 3 + HIGHLIGHT_MARGIN) : 0;
	borderTop = onBottom ? borderLeft : 0;
	borderBottom = onBottom ? 0 : borderRight;
	
	//set up default colors
	Display display = getDisplay();
	selectionForeground = display.getSystemColor(SELECTION_FOREGROUND);
	selectionBackground = display.getSystemColor(SELECTION_BACKGROUND);
	borderColor1 = new Color(getDisplay(), borderInsideRGB);
	borderColor2 = new Color(getDisplay(), borderMiddleRGB);
	borderColor3 = new Color(getDisplay(), borderOutsideRGB);
	setForeground(display.getSystemColor(FOREGROUND));
	setBackground(display.getSystemColor(BACKGROUND));
	
	initAccessible();
	
	// Add all listeners
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:          onDispose(); break;
				case SWT.Paint:            onPaint(event);	break;
				case SWT.Resize:           onResize();	break;
				case SWT.MouseDoubleClick: onMouseDoubleClick(event); break;
				case SWT.MouseDown:        onMouse(event);	break;
				case SWT.MouseExit:        onMouse(event);	break;
				case SWT.MouseHover:       onMouseHover(event); break;
				case SWT.MouseMove:        onMouse(event); break;
				case SWT.MouseUp:          onMouse(event); break;
				case SWT.FocusIn:          onFocus(event);	break;
				case SWT.FocusOut:         onFocus(event);	break;
				case SWT.Traverse:         onTraverse(event); break;
			}
		}
	};

	int[] folderEvents = new int[]{
		SWT.Dispose,
		SWT.Paint,
		SWT.Resize,  
		SWT.MouseDoubleClick, 
		SWT.MouseDown,
		SWT.MouseExit,
		SWT.MouseHover, 
		SWT.MouseMove,
		SWT.MouseUp,
		SWT.FocusIn, 
		SWT.FocusOut, 
		SWT.KeyDown,
		SWT.Traverse,
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}
}
static int[] bezier(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int count) {
	// The parametric equations for a Bezier curve for x[t] and y[t] where  0 <= t <=1 are:
	// x[t] = x0+3(x1-x0)t+3(x0+x2-2x1)t^3+(x3-x0+3x1-3x2)t^3
	// y[t] = y0+3(y1-y0)t+3(y0+y2-2y1)t^2+(y3-y0+3y1-3y2)t^3
	double a0 = x0;
	double a1 = 3*(x1 - x0);
	double a2 = 3*(x0 + x2 - 2*x1);
	double a3 = x3 - x0 + 3*x1 - 3*x2;
	double b0 = y0;
	double b1 = 3*(y1 - y0);
	double b2 = 3*(y0 + y2 - 2*y1);
	double b3 = y3 - y0 + 3*y1 - 3*y2;

	int[] polygon = new int[2*count + 2];
	for (int i = 0; i <= count; i++) {
		double t = (double)i / (double)count;
		polygon[2*i] = (int)(a0 + a1*t + a2*t*t + a3*t*t*t);
		polygon[2*i + 1] = (int)(b0 + b1*t + b2*t*t + b3*t*t*t);
	}
	return polygon;
}
static int checkStyle (int style) {
	int mask = SWT.TOP | SWT.BOTTOM | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT | SWT.SINGLE | SWT.MULTI;
	style = style & mask;
	// TOP and BOTTOM are mutually exlusive.
	// TOP is the default
	if ((style & SWT.TOP) != 0) 
		style = style & ~(SWT.TOP | SWT.BOTTOM) | SWT.TOP;
	// SINGLE and MULTI are mutually exlusive.
	// MULTI is the default
	if ((style & SWT.MULTI) != 0) 
		style = style & ~(SWT.SINGLE | SWT.MULTI) | SWT.MULTI;
	// reduce the flash by not redrawing the entire area on a Resize event
	style |= SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND;
	return style;
}
static void fillRegion(GC gc, Region region) {
	// NOTE: region passed in to this function will be modified
	Region clipping = new Region();
	gc.getClipping(clipping);
	region.intersect(clipping);
	gc.setClipping(region);
	gc.fillRectangle(region.getBounds());
	gc.setClipping(clipping);
	clipping.dispose();
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a tab item is closed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see CTabFolderCloseListener
 * @see #removeCTabFolderCloseListener(CTabFolderCloseListener)
 * 
 * @since 3.0
 */
public void addCTabFolderCloseListener(CTabFolderCloseListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (closeListeners.length == 0) {
		// display close button
		showClose = true;
		setButtonBounds();
		redraw();
	}
	// add to array
	CTabFolderCloseListener[] newListeners = new CTabFolderCloseListener[closeListeners.length + 1];
	System.arraycopy(closeListeners, 0, newListeners, 0, closeListeners.length);
	closeListeners = newListeners;
	closeListeners[closeListeners.length - 1] = listener;
}
/**
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void addCTabFolderExpandListener(CTabFolderExpandListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (expandListeners.length == 0) {
		// display expand button
		showExpand = true;
		updateItems();
		redraw();
	}

	// add to array
	CTabFolderExpandListener[] newListeners = new CTabFolderExpandListener[expandListeners.length + 1];
	System.arraycopy(expandListeners, 0, newListeners, 0, expandListeners.length);
	expandListeners = newListeners;
	expandListeners[expandListeners.length - 1] = listener;
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a tab item is closed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see CTabFolderListener
 * @see #removeCTabFolderListener(CTabFolderListener)
 * 
 * @deprecated use addCTabFolderCloseListener
 */
public void addCTabFolderListener(CTabFolderListener listener) {
	addCTabFolderCloseListener(listener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a the selection list is displayed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see CTabFolderListListener
 * @see #removeCTabFolderListListener(CTabFolderListListener)
 * 
 * @since 3.0
 */
public void addCTabFolderListListener(CTabFolderListListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolderListListener[] newListeners = new CTabFolderListListener[listListeners.length + 1];
	System.arraycopy(listListeners, 0, newListeners, 0, listListeners.length);
	listListeners = newListeners;
	listListeners[listListeners.length - 1] = listener;
}
/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int minWidth = 0;
	int minHeight = 0;
	// preferred width of tab area to show all tabs
	GC gc = new GC(this);
	int selectedMax = 0;
	int selectedMaxIndex = -1;
	for (int i = 0; i < items.length; i++) {
		int width = items[i].preferredWidth(gc, true);
		if ( width > selectedMax) {
			selectedMax = width;
			selectedMaxIndex = i;
		}
	}
	for (int i = 0; i < items.length; i++) {
		minWidth += items[i].preferredWidth(gc, i == selectedMaxIndex);
	}
	gc.dispose();

	// preferred size of controls in tab items
	for (int i = 0; i < items.length; i++) {
		Control control = items[i].getControl();
		if (control != null && !control.isDisposed()){
			Point size = control.computeSize (wHint, hHint);
			minWidth = Math.max (minWidth, size.x);
			minHeight = Math.max (minHeight, size.y);
		}
	}

	if (minWidth == 0) minWidth = DEFAULT_WIDTH;
	if (minHeight == 0) minHeight = DEFAULT_HEIGHT;

	if (!expanded) minHeight = 0;
	
	if (wHint != SWT.DEFAULT) minWidth  = wHint;
	if (hHint != SWT.DEFAULT) minHeight = hHint;

	Rectangle trim = computeTrim(0, 0, minWidth, minHeight);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	if (!expanded) {
		int trimX = x - borderLeft;
		int trimY = onBottom ? y - borderTop : y - HIGHLIGHT_HEADER - tabHeight - borderTop;
		int trimWidth = width + borderLeft + borderRight;
		int trimHeight = borderTop + borderBottom + tabHeight + HIGHLIGHT_HEADER;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	} else {
		int trimX = x - marginWidth - borderLeft;
		int trimY = onBottom ? y - marginHeight - borderTop : y - marginHeight - HIGHLIGHT_HEADER - tabHeight - borderTop;
		int trimWidth = width + borderLeft + borderRight + 2*marginWidth;
		int trimHeight = height + borderTop + borderBottom + 2*marginHeight + tabHeight + HIGHLIGHT_HEADER;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	}
}
void createItem (CTabItem2 item, int index) {
	if (0 > index || index > getItemCount ()){ 
		SWT.error (SWT.ERROR_INVALID_RANGE);
	}
	// grow by one and rearrange the array.
	CTabItem2[] newItems = new CTabItem2 [items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index);
	items = newItems;
	
	item.parent = this;
	
	if (selectedIndex >= index) {
		 selectedIndex ++;
	}
	if (items.length == 1) {
		topTabIndex = 0;
		if (!updateTabHeight(tabHeight)) updateItems();
		redraw();
	} else {
		updateItems();
		// redraw tabs if new item visible
		if (index == topTabIndex ||  
		    (index > topTabIndex && item.x + item.width < getRightItemEdge())){
			redraw();
		}
	}
}
void destroyItem (CTabItem2 item) {
	if (inDispose) return;
	int index = indexOf(item);
	if (index == -1) return;
	insertionIndex = -2;
	
	if (items.length == 1) {
		items = new CTabItem2[0];
		selectedIndex = -1;
		topTabIndex = 0;
		
		Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
		if (!fixedTabHeight) tabHeight = 0;
		redraw();
		return;
	} 
		
	// shrink by one and rearrange the array.
	CTabItem2[] newItems = new CTabItem2 [items.length - 1];
	System.arraycopy(items, 0, newItems, 0, index);
	System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
	items = newItems;
	
	if (topTabIndex == items.length) {
		--topTabIndex;
	}
	
	// move the selection if this item is selected
	if (selectedIndex == index) {
		Control control = item.getControl();
		selectedIndex = -1;
		setSelection(Math.max(0, index - 1), true);
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
	} else if (selectedIndex > index) {
		selectedIndex --;
	}
	
	if (updateItems()) redraw();
}
void drawBorder(GC gc) {
	Point size = getSize();
	// Draw selection border across all tabs
	if (selectedIndex == -1) { // no selected item
		int x = borderLeft;
		int y = onBottom ? size.y - borderBottom - tabHeight - HIGHLIGHT_HEADER : borderTop + tabHeight + 1;
		int width = size.x - borderLeft - borderRight;
		int height = HIGHLIGHT_HEADER - 1;
		gc.setBackground(getBackground());
		gc.fillRectangle(x, y, width, height);
		x = borderLeft;
		y = (onBottom) ? size.y - borderBottom - tabHeight - 1 : borderTop + tabHeight;
		gc.setForeground(borderColor1);
		gc.drawLine(x, y, x + width, y);
	} else { //selected item
		int x = borderLeft;
		int y = onBottom ? size.y - borderBottom - tabHeight - HIGHLIGHT_HEADER : borderTop + tabHeight + 1;
		int width = size.x - borderLeft - borderRight;
		int height = HIGHLIGHT_HEADER - 1;
		int[] shape = new int[] {x,y, x+width,y, x+width,y+height, x,y+height};
		drawSelectionBackground(gc, shape);

		CTabItem2 item = items[selectedIndex];
		int itemX = item.x;
		int itemY = item.y;
		int itemW = item.width;
		int itemH = item.height;
		int extra = CTabFolder2.CURVE_WIDTH/2 + 4; // +4 to avoid overlapping with text in next tab
		if (onBottom) {
			int rightTabEdge = getRightItemEdge();
			if (!single && selectedIndex != topTabIndex && itemX + itemW >= rightTabEdge){ 
				shape = new int[4];
				int index = 0;
				shape[index++] = Math.max(0, borderLeft - 1);
				shape[index++] = itemY - 1;
				shape[index++] = size.x - borderRight;
				shape[index++] = itemY - 1;
			} else {
				int[] left = BOTTOM_LEFT_CORNER;
				int[] right = curve;
				shape = new int[left.length + right.length + 8];
				int index = 0;
				shape[index++] = Math.max(0, borderLeft - 1);
				shape[index++] = itemY - 1;
				shape[index++] = itemX;
				shape[index++] = itemY - 1;
				for (int i = 0; i < left.length/2; i++) {
					shape[index++] = itemX + left[2*i];
					shape[index++] = itemY + itemH + left[2*i+1]-1;
				}
				for (int i = 0; i < right.length/2; i++) {
					shape[index++] = itemX + itemW - extra + right[2*i];
					shape[index++] = itemY + right[2*i+1] - 2;
				}
				int temp = 0;			
				for (int i = 0; i < shape.length/2; i++) {
					if (shape[2*i] > rightTabEdge) {
						if (temp == 0 && i > 0) {
							temp = shape[2*i-1];
						} else {
							temp = itemY - 1;
						}
						shape[2*i] = rightTabEdge;
						shape[2*i+1] = temp;
					}
				}
				shape[index++] = rightTabEdge;
				shape[index++] = itemY - 1;
				shape[index++] = size.x - borderRight;
				shape[index++] = itemY - 1;
			}
		} else {
			int rightTabEdge = getRightItemEdge();
			if (!single && selectedIndex != topTabIndex && itemX + itemW >= rightTabEdge){ 
				shape = new int[4];
				int index = 0;
				shape[index++] = Math.max(0, borderLeft - 1);
				shape[index++] = itemY + itemH;
				shape[index++] = size.x - borderRight;
				shape[index++] = itemY + itemH;
			} else {
				int[] left = TOP_LEFT_CORNER;
				int[] right = curve;
				shape = new int[left.length+right.length+8];
				int index = 0;
				
				shape[index++] = Math.max(0, borderLeft - 1);
				shape[index++] = itemY + itemH;
				shape[index++] = itemX;
				shape[index++] = itemY + itemH;
				
				for (int i = 0; i < left.length/2; i++) {
					shape[index++] = itemX + left[2*i];
					shape[index++] = itemY + left[2*i+1];
				}
				for (int i = 0; i < right.length/2; i++) {
					shape[index++] = itemX + itemW - extra + right[2*i];
					shape[index++] = itemY + right[2*i+1];
				}
					
				int temp = 0;
				for (int i = 0; i < shape.length/2; i++) {
					if (shape[2*i] > rightTabEdge) {
						if (temp == 0 && i > 0) {
							temp = shape[2*i-1];
						} else {
							temp = itemY + itemH;
						}
						shape[2*i] = rightTabEdge;
						shape[2*i+1] = temp;
					}
				}
				shape[index++] = rightTabEdge;
				shape[index++] = itemY + itemH;
				shape[index++] = size.x - borderRight;
				shape[index++] = itemY + itemH;
			}
		}

		// Anti- aliasing
		int[] shape2 = new int[shape.length];
		int index = 0;
		boolean left = true;
		for (int i = 0; i < shape.length/2; i++) { 
			shape2[index] = shape[index++] + (left ? -1 : +1);
			shape2[index] = shape[index++];
			if (left && (index + 1 < shape.length)) left = onBottom ? shape[index+1] < itemY +itemH - 1 : shape[index+1] > itemY;
		}
		RGB from = CTabFolder2.borderColor1.getRGB();
		RGB to = single ? getBackground().getRGB() : getParent().getBackground().getRGB();
		int red = from.red + 3*(to.red - from.red)/4;
		int green = from.green + 3*(to.green - from.green)/4;
		int blue = from.blue + 3*(to.blue - from.blue)/4;
		Color color = new Color(getDisplay(), red, green, blue);
		gc.setForeground(color);
		gc.drawPolyline(shape2);
		color.dispose();
		int[] shape3 = new int[shape.length];
		index = 0;
		left = true;
		for (int i = 0; i < shape.length/2; i++) {
			shape3[index] = shape[index++] + (left ? +1 : -1);
			shape3[index] = shape[index++];
			if (left && (index + 1 < shape.length)) left = onBottom ? shape[index+1] < itemY +itemH - 1 : shape[index+1] > itemY;
		}
		from = CTabFolder2.borderColor1.getRGB();
		to = selectionBackground == null ? getBackground().getRGB() : selectionBackground.getRGB();
		red = from.red + 3*(to.red - from.red)/4;
		green = from.green + 3*(to.green - from.green)/4;
		blue = from.blue + 3*(to.blue - from.blue)/4;
		color = new Color(getDisplay(), red, green, blue);
		gc.setForeground(color);
		gc.drawPolyline(shape3);
		color.dispose();
		
		// draw line
		gc.setForeground(borderColor1);
		gc.drawPolyline(shape);
	}
	if (borderLeft == 0) return;
			
	//draw 1 pixel border around outside for flat look
	if (borderLeft == 1) {
		drawFlatBorder(gc);
	} else {
		draw3DBorder(gc);
	}
}
void drawFlatBorder(GC gc) {
	Point size = getSize();
	Color color = borderColor1;
	gc.setForeground(color);
	if (onBottom) {
		int x1 = 0;
		int x2 = size.x - borderRight;
		int y1 = borderTop - 1;
		int y2 = size.y - tabHeight - borderBottom - 1;
		gc.drawLine(x1, y1, x1, y2); // left
		gc.drawLine(x2, y1, x2, y2); // right
		gc.drawLine(x1, y1, x2, y1); // top
		if (single) {
			int x = Math.max(0, borderLeft - 1);
			int y = size.y - borderBottom - tabHeight;
			int width = size.x - borderLeft - borderRight + 1;
			int height = tabHeight - 1;
			int[] shape = new int[BOTTOM_LEFT_OUTSIDE_CORNER.length + BOTTOM_RIGHT_OUTSIDE_CORNER.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y;
			for (int i = 0; i < BOTTOM_LEFT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+BOTTOM_LEFT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+height+BOTTOM_LEFT_OUTSIDE_CORNER[2*i+1];
			}
			for (int i = 0; i < BOTTOM_RIGHT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+width+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+height+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i+1];
			}
			shape[index++] = x+width;
			shape[index++] = y-1;
			
			gc.setForeground(borderColor1);
			gc.drawPolyline(shape);
		}
	} else {
		int x1 = borderLeft - 1;
		int x2 = size.x - borderRight;
		int y1 = borderTop + tabHeight;
		int y2 = size.y - borderBottom;
		gc.drawLine(x1, y1, x1, y2); // left
		gc.drawLine(x2, y1, x2, y2); // right
		gc.drawLine(x1, y2, x2, y2); //bottom
		if (single) {
			int x = Math.max(0, borderLeft - 1);
			int y = borderTop;
			int width = size.x - borderLeft - borderRight + 1;
			int height = tabHeight - 1;
			int[] shape = new int[TOP_LEFT_OUTSIDE_CORNER.length + TOP_RIGHT_OUTSIDE_CORNER.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y+height;
			for (int i = 0; i < TOP_LEFT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+TOP_LEFT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+TOP_LEFT_OUTSIDE_CORNER[2*i+1];
			}
			for (int i = 0; i < TOP_RIGHT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+width+TOP_RIGHT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+TOP_RIGHT_OUTSIDE_CORNER[2*i+1];
			}
			shape[index++] = x+width;
			shape[index++] = y+height+1;
			
			gc.setForeground(borderColor1);
			gc.drawPolyline(shape);
		}
	}
}
void draw3DBorder(GC gc) {
	Point size = getSize();
	Color parentBackground = getParent().getBackground();
	if (single) {
		if (onBottom) { // single bottom border
			int x = Math.max(0, borderLeft - 1);
			if (borderLeft > 1 & showHighlight) x -= HIGHLIGHT_MARGIN;
			int y = size.y - borderBottom - tabHeight;
			int width = size.x - borderLeft - borderRight + 1;
			if (borderRight > 1 & showHighlight) width += 2*HIGHLIGHT_MARGIN;
			int height = tabHeight - 1;
			int[] shape = new int[BOTTOM_LEFT_OUTSIDE_CORNER.length + BOTTOM_RIGHT_OUTSIDE_CORNER.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y;
			for (int i = 0; i < BOTTOM_LEFT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+BOTTOM_LEFT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+height+BOTTOM_LEFT_OUTSIDE_CORNER[2*i+1];
			}
			for (int i = 0; i < BOTTOM_RIGHT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+width+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+height+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i+1];
			}
			shape[index++] = x+width;
			shape[index++] = y-1;
			
			gc.setForeground(borderColor1);
			gc.drawPolyline(shape);
		} else { // single top border
			int x = borderLeft - 1;
			if (borderLeft > 1 & showHighlight) x -= HIGHLIGHT_MARGIN;
			int y = borderTop;
			int width = size.x - borderLeft - borderRight + 1;
			if (borderRight > 1 & showHighlight) width += 2*HIGHLIGHT_MARGIN;
			int height = tabHeight - 1;
			int[] shape = new int[TOP_LEFT_OUTSIDE_CORNER.length + TOP_RIGHT_OUTSIDE_CORNER.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y+height;
			for (int i = 0; i < TOP_LEFT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+TOP_LEFT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+TOP_LEFT_OUTSIDE_CORNER[2*i+1];
			}
			for (int i = 0; i < TOP_RIGHT_OUTSIDE_CORNER.length/2; i++) {
				shape[index++] = x+width+TOP_RIGHT_OUTSIDE_CORNER[2*i];
				shape[index++] = y+TOP_RIGHT_OUTSIDE_CORNER[2*i+1];
			}
			shape[index++] = x+width;
			shape[index++] = y+height+1;
			gc.setForeground(borderColor1);
			gc.drawPolyline(shape);
		}
	}
	if (!showHighlight) {
		if (onBottom) { // on bottom no border showing
			int x1 = HIGHLIGHT_MARGIN;
			int x2 = size.x - borderRight;
			int y1 = HIGHLIGHT_MARGIN;
			int y2 = size.y - borderBottom - tabHeight;
			
			gc.setBackground(parentBackground);
			gc.fillRectangle(0, 0, HIGHLIGHT_MARGIN, size.y); //left
			gc.fillRectangle(x2 + 1, 0, borderRight, size.y); //right
			gc.fillRectangle(0, 0, size.x, HIGHLIGHT_MARGIN); //top
			
			gc.setForeground(borderColor1);
			gc.drawLine(x1, y1, x1, y2); // left
			gc.drawLine(x2, y1, x2, y2); // right
			gc.drawLine(x1, y1, x2, y1); //bottom
			
		} else { //on top no border showing
			int x1 = HIGHLIGHT_MARGIN;
			int x2 = size.x - borderRight;
			int y1 = borderTop + tabHeight;
			int y2 = size.y - borderBottom;
			
			gc.setBackground(parentBackground);
			gc.fillRectangle(0, 0, HIGHLIGHT_MARGIN, size.y); //left
			gc.fillRectangle(x2 + 1, 0, borderRight, size.y); //right
			gc.fillRectangle(0, y2, size.x, borderBottom); //bottom
			
			gc.setForeground(borderColor1);
			gc.drawLine(x1, y1, x1, y2); // left
			gc.drawLine(x2, y1, x2, y2); // right
			gc.drawLine(x1, y2, x2, y2); //bottom
		}
	} else {
		if (onBottom) { // on bottom border showing
			if (!single) {
				gc.setBackground(parentBackground);
				gc.fillRectangle(0, size.y - borderBottom - tabHeight + 1, HIGHLIGHT_MARGIN, borderBottom + tabHeight);
				gc.fillRectangle(size.x - borderRight + 1, size.y - borderBottom - tabHeight + 1, borderRight, borderTop + tabHeight);	
			}
			
			int x1 = 0;
			int x2 = size.x - 3;
			int y1 = 0;
			int y2 = size.y - borderBottom - tabHeight - 1;
			gc.setForeground(borderColor1);
			gc.drawLine(x1, y2, borderLeft-1, y2); // indent on left
			gc.drawLine(x1, y1, x1, y2); // left
			gc.drawLine(size.x - borderRight, y2, x2, y2); // indent on right
			gc.drawLine(x2, y1, x2, y2); // right
			gc.drawLine(x1, y1, x2, y1); //top
			
			int gapStart = size.x/2;
			int gapEnd = gapStart;
			if (selectedIndex != -1 && selectedIndex >= topTabIndex) {
				CTabItem2 item =  items[selectedIndex];
				Rectangle r = item.getBounds();
				int rightTabEdge = size.x - borderRight - chevronRect.width - expandRect.width - closeRect.width - 1;
				if (r.x < rightTabEdge) {
					gapStart = r.x - 1;
					int extra = CURVE_WIDTH / 2;
					int[] left = BOTTOM_LEFT_CORNER;
					int[] right = curve;
					int[] shape = new int[left.length + right.length + 8];
					int index = 0;
					shape[index++] = Math.max(0, borderLeft - 1);
					shape[index++] = r.y - 1;
					shape[index++] = r.x;
					shape[index++] = r.y - 1;
					for (int i = 0; i < left.length/2; i++) {
						shape[index++] = r.x + left[2*i];
						shape[index++] = r.y + r.height + left[2*i+1]-1;
					}
					for (int i = 0; i < right.length/2; i++) {
						shape[index++] = r.x+r.width - extra + right[2*i];
						shape[index++] = r.y + right[2*i+1] - 2;
					}
					for (int i = 0; i < shape.length/2; i++) {
						if (shape[2*i] > rightTabEdge) {
							gapEnd = rightTabEdge + 1;
							break;
						}
						if (shape[2*i+1] > r.y) {
							gapEnd = shape[2*i];
						}
					}
				}
			}
			
			x1 = 1;
			x2 = size.x - 2;
			y1 = 1;
			y2 =  size.y - borderBottom - tabHeight;
			gc.setForeground(borderColor2);
			gc.drawLine(x2, y1, x2, y2); // right 1
			gc.drawLine(x1, y2, gapStart, y2); //bottom 1 left
			gc.drawLine(gapEnd, y2, x2, y2); //bottom 1 right
			gc.setForeground(parentBackground);
			if (!single) gc.drawPoint(0, y2); // bottom left
			gc.drawPoint(x2, 0); // top right

			x1 = 2;
			x2 = size.x - 1;
			y1 = 2;
			y2 = size.y - borderBottom - tabHeight + 1;
			gc.setForeground(borderColor3);
			gc.drawLine(x2, y1, x2, y2); // right 2
			gc.drawLine(x1, y2, gapStart, y2); //bottom 2 left
			gc.drawLine(gapEnd, y2, x2, y2); //bottom 2 right
			gc.setForeground(parentBackground);
			if (!single)gc.drawLine(0, y2, 1, y2); // bottom left
			gc.drawLine(x2, 0, x2, 1); // top right
			
			x1 = 1;
			x2 = size.x - 3;
			y1 = 1;
			y2 = size.y - borderBottom - tabHeight - 1;
			int[] shape = new int[] {x1,y1, x2,y1, x2,y2, x1,y2};
			drawSelectionBackground(gc, shape);
			
		} else { // on top and border showing
			if (!single) {
				gc.setBackground(parentBackground);
				gc.fillRectangle(0, 0, HIGHLIGHT_MARGIN, borderTop + tabHeight); //left
				gc.fillRectangle(size.x - borderRight + 1, 0, borderRight, borderTop + tabHeight);//right
			}
			
			int x1 = 0;
			int x2 = size.x - 3;
			int y1 = borderTop + tabHeight;
			int y2 = size.y - 3;
			gc.setForeground(borderColor1);
			gc.drawLine(x1, y1, borderLeft-1, y1); // indent on left
			gc.drawLine(x1, y1, x1, y2); // left
			gc.drawLine(size.x - borderRight, y1, x2, y1); // indent on right
			gc.drawLine(x2, y1, x2, y2); // right
			gc.drawLine(x1, y2, x2, y2); //bottom
			
			x1 = 1;
			x2 = size.x - 2;
			y1 = borderTop + tabHeight + 1;
			y2 =  size.y - 2;
			gc.setForeground(borderColor2);
			gc.drawLine(x1, y2, x2, y2); //bottom 1
			if (single) {
				int x = borderLeft - HIGHLIGHT_MARGIN;
				int y = borderTop;
				int width = size.x - borderLeft - borderRight + 1 + 2*HIGHLIGHT_MARGIN;
				int[] shape = new int[TOP_RIGHT_OUTSIDE_CORNER.length - 2];
				int index = 0;
				for (int i = 2; i < TOP_RIGHT_OUTSIDE_CORNER.length/2; i++) {
					shape[index++] = x+width+TOP_RIGHT_OUTSIDE_CORNER[2*i];
					shape[index++] = y+TOP_RIGHT_OUTSIDE_CORNER[2*i+1];
				}
				shape[index++] = x2;
				shape[index++] = y2;
				gc.drawPolyline(shape); // right 1
				gc.setForeground(parentBackground);
				gc.drawPoint(0, y2); // bottom left
			} else {
				gc.drawLine(x2, y1, x2, y2); // right 1
				gc.setForeground(parentBackground);
				gc.drawPoint(0, y2); // bottom left
				gc.drawLine(x2, 0, x2, y1-1); // top right
			}
			
			x1 = 2;
			x2 = size.x - 1;
			y1 = borderTop + tabHeight + 2;
			y2 = size.y - 1;
			gc.setForeground(borderColor3);
			gc.drawLine(x1, y2, x2, y2); //bottom 2
			if (single) {
				int x = borderLeft - HIGHLIGHT_MARGIN + 1;
				int y = borderTop;
				int width = size.x - borderLeft - borderRight + 1 + 2*HIGHLIGHT_MARGIN;
				int[] shape = new int[TOP_RIGHT_OUTSIDE_CORNER.length - 4];
				int index = 0;
				for (int i = 3; i < TOP_RIGHT_OUTSIDE_CORNER.length/2; i++) {
					shape[index++] = x+width+TOP_RIGHT_OUTSIDE_CORNER[2*i];
					shape[index++] = y+TOP_RIGHT_OUTSIDE_CORNER[2*i+1];
				}
				shape[index++] = x2;
				shape[index++] = y2;
				gc.drawPolyline(shape); // right 2
				gc.setForeground(parentBackground);
				gc.drawLine(0, y2, 1, y2); // bottom left
			} else {
				gc.drawLine(x2, y1, x2, y2); // right 2
				gc.setForeground(parentBackground);
				gc.drawLine(0, y2, 1, y2); // bottom left
				gc.drawLine(x2, 0, x2, y1-1); // top right
			}
			
			x1 = 1;
			x2 = size.x - 3;
			y1 = borderTop + tabHeight + 1;
			y2 = size.y - 3;
			int[] shape = new int[] {x1,y1, x2,y1, x2,y2, x1,y2};
			drawSelectionBackground(gc, shape);
		}
	}
}
void drawChevron(GC gc) {
	if (chevronRect.width == 0 || chevronRect.height == 0) return;
	Display display = getDisplay();
	if (!single) {
		gc.setBackground(getParent().getBackground());
		gc.fillRectangle(chevronRect);
	}
	// draw chevron (10x7)
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = chevronRect.x + indent - 1;
	int y = chevronRect.y + indent;
	switch (chevronImageState) {
		case NORMAL: {
			int[] shape = new int[] {x,y, x+9,y, x+9,y+2, x+5,y+6, x+4,y+6, x,y+2, x,y};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			gc.setForeground(borderColor1);
			gc.drawPolygon(shape);
			break;
		}
		case HOT: {
			int[] shape = new int[] {x,y, x+9,y, x+9,y+2, x+5,y+6, x+4,y+6, x,y+2, x,y};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			Color border = new Color(display, CHEVRON_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
		case SELECTED: {
			int[] shape = new int[] {x+1,y+1, x+10,y+1, x+10,y+3, x+6,y+7, x+5,y+7, x+1,y+3, x+1,y+1};
			Color fill = new Color(display, CHEVRON_FILL);
			gc.setBackground(fill);
			gc.fillPolygon(shape);
			fill.dispose();
			Color border = new Color(display, CHEVRON_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
	}
}
void drawClose(GC gc) {
	if (closeRect.width == 0 || closeRect.height == 0) return;
	Display display = getDisplay();
	if (!single) {
		gc.setBackground(getParent().getBackground());
		gc.fillRectangle(closeRect);
	}
	// draw X (10x10 or 11x11)
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = closeRect.x + indent - 1;
	int y = closeRect.y + indent;
	switch (closeImageState) {
		case NORMAL: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			gc.setForeground(borderColor1);
			gc.drawPolygon(shape);
			break;
		}
		case HOT: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			Color border = new Color(display, CLOSE_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
		case SELECTED:
			int[] shape = new int[] {x+1,y+1, x+3,y+1, x+5,y+3, x+6,y+3, x+8,y+1, x+10,y+1, 
					                 x+10,y+3, x+8,y+5, x+8,y+6, x+10,y+8, x+10,y+10,
			                         x+8,y+10, x+6,y+8, x+5,y+8, x+3,y+10, x+1,y+10,
			                         x+1,y+8, x+3,y+6, x+3,y+5, x+1,y+3};
			Color fill = new Color(display, CLOSE_FILL);
			gc.setBackground(fill);
			gc.fillPolygon(shape);
			fill.dispose();
			Color border = new Color(display, CLOSE_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
	}
}
void drawExpand(GC gc) {
	if (expandRect.width == 0 || expandRect.height == 0) return;
	Display display = getDisplay();
	if (!single) {
		gc.setBackground(getParent().getBackground());
		gc.fillRectangle(expandRect);
	}
	// draw triangle (7x4 or 4x7)
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = expandRect.x + indent - 1;
	int y = expandRect.y + indent;
	switch (expandImageState) {
		case NORMAL: {
			if (expanded) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 9, 3);
				gc.setForeground(borderColor1);
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 7, 9);
				gc.setForeground(borderColor1);
				gc.drawRectangle(x, y, 7, 9);
				gc.drawLine(x+1, y+2, x+6, y+2);
			}
			break;
		}
		case HOT: {
			Color border = new Color(display, EXPAND_BORDER);
			if (expanded) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 9, 3);
				gc.setForeground(border);
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 7, 9);
				gc.setForeground(border);
				gc.drawRectangle(x, y, 7, 9);
				gc.drawLine(x+1, y+2, x+6, y+2);
			}
			border.dispose();
			break;
		}
		case SELECTED: {
			Color fill = new Color(display, EXPAND_FILL);
			Color border = new Color(display, EXPAND_BORDER);
			if (expanded) {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+1, 9, 3);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+1, 9, 3);
			} else {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+1, 7, 9);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+1, 7, 9);
				gc.drawLine(x+2, y+3, x+7, y+3);
			}
			fill.dispose();
			border.dispose();
			break;
		}
	}
}
void drawSelectionBackground(GC gc, int[] shape) {
	if (backgroundImage != null) {
		// draw the background image in shape
		Region clipping = new Region();
		gc.getClipping(clipping);
		Region region = new Region();
		region.add(shape);
		gc.setClipping(region);
		gc.setBackground(selectionBackground);
		gc.drawImage(backgroundImage, 0, 0);
		gc.setClipping(clipping);
		clipping.dispose();
		region.dispose();
	} else if (gradientColors != null) {
		Point size = getSize();
		// draw a gradient in shape
		Region clipping = new Region();
		gc.getClipping(clipping);
		Region region = new Region();
		region.add(shape);
		gc.setClipping(region);
		
		if (gradientColors.length == 1) {
			Color background = gradientColors[0] != null ? gradientColors[0] : selectionBackground;
			gc.setBackground(background);
			gc.fillRectangle(0, 0, size.x, size.y);
		} else {
			Color background = selectionBackground;
			Color lastColor = gradientColors[0];
			if (lastColor == null) lastColor = background;
			for (int i = 0, pos = 0; i < gradientPercents.length; ++i) {
				gc.setForeground(lastColor);
				lastColor = gradientColors[i + 1];
				if (lastColor == null) lastColor = background;
				gc.setBackground(lastColor);
				int gradientWidth = (gradientPercents[i] * size.x / 100);
				gc.fillGradientRectangle(0, 0, gradientWidth, size.y, false);
				pos += gradientWidth;
			}
		}
		gc.setClipping(clipping);
		clipping.dispose();
		region.dispose();
	} else {
		// draw a solid background using selectionBackground in shape
		gc.setBackground(selectionBackground);
		gc.fillPolygon(shape);
	}
}
public Rectangle getClientArea() {
	checkWidget();
	if (!expanded) return new Rectangle(xClient, yClient, 0, 0);
	Point size = getSize();
	int width = size.x  - borderLeft - borderRight - 2*marginWidth;
	int height = size.y - borderTop - borderBottom - 2*marginHeight;
	if (items.length == 0) {		
		return new Rectangle(borderLeft + marginWidth, borderTop + marginHeight, width, height);	
	}
	height -= tabHeight+HIGHLIGHT_HEADER;
	return new Rectangle(xClient, yClient, width, height);
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
 *
 * @since 3.0
 */
public boolean getExpanded() {
	checkWidget();
	return expanded;
}
/**
 * Return the tab that is located at the specified index.
 * 
 * @param index the index of the tab item
 * @return the item at the specified index
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is out of range</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 */
public CTabItem2 getItem (int index) {
	//checkWidget();
	if (index  < 0 || index >= items.length) 
		SWT.error(SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
 * Gets the item at a point in the widget.
 *
 * @param pt the point in coordinates relative to the CTabFolder
 * @return the item at a point or null
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public CTabItem2 getItem (Point pt) {
	//checkWidget();
	if (items.length == 0) return null;
	Point size = getSize();
	if (size.x <= borderLeft + borderRight) return null;
	for (int index = topTabIndex; index < items.length; index++) {
		CTabItem2 item = items[index];
		Rectangle rect = item.getBounds();
		if (rect.contains(pt)) return item;
	}
	return null;
}
/**
 * Return the number of tabs in the folder.
 * 
 * @return the number of tabs in the folder
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getItemCount(){
	//checkWidget();
	return items.length;
}
/**
 * Return the tab items.
 * 
 * @return the tab items
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public CTabItem2 [] getItems() {
	//checkWidget();
	CTabItem2[] tabItems = new CTabItem2 [items.length];
	System.arraycopy(items, 0, tabItems, 0, items.length);
	return tabItems;
}
char getMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while ((index < length) && (string.charAt (index) != '&')) index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return string.charAt (index);
		index++;
	} while (index < length);
 	return '\0';
}
int getRightItemEdge (){
	return getSize().x - borderRight - closeRect.width - expandRect.width - chevronRect.width - 1;
}
/**
 * Return the selected tab item, or an empty array if there
 * is no selection.
 * 
 * @return the selected tab item
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public CTabItem2 getSelection() {
	//checkWidget();
	if (selectedIndex == -1) return null;
	return items[selectedIndex];
}
/**
 * Return the index of the selected tab item, or -1 if there
 * is no selection.
 * 
 * @return the index of the selected tab item or -1
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getSelectionIndex() {
	//checkWidget();
	return selectedIndex;
}
/**
 * Returns the height of the tab
 * 
 * @return the height of the tab
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getTabHeight(){
	checkWidget();
	return tabHeight;
}
/**
 * Returns the control in the top right corner of the tab folder. 
 * Typically this is a close button or a composite with a menu and close button.
 *
 * @since 2.1
 *
 * @return the control in the top right corner of the tab folder or null
 * 
 * @exception  SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @deprecated
 */
public Control getTopRight() {
	checkWidget();
	return null;
}

/**
 * Return the index of the specified tab or -1 if the tab is not 
 * in the receiver.
 * 
 * @param item the tab item for which the index is required
 * 
 * @return the index of the specified tab item or -1
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 */
public int indexOf(CTabItem2 item) {
	checkWidget();
	if (item == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}
void initAccessible() {
	final Accessible accessible = getAccessible();
	accessible.addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			String name = null;
			int childID = e.childID;
			if (childID >= 0 && childID < items.length) {
				name = items[childID].getText();
				int index = name.indexOf('&');
				if (index > 0) {
					name = name.substring(0, index) + name.substring(index + 1);
				}
			}
			e.result = name;
		}

		public void getHelp(AccessibleEvent e) {
			String help = null;
			int childID = e.childID;
			if (childID == ACC.CHILDID_SELF) {
				help = getToolTipText();
			} else if (childID >= 0 && childID < items.length) {
				help = items[childID].getToolTipText();
			}
			e.result = help;
		}
		
		public void getKeyboardShortcut(AccessibleEvent e) {
			String shortcut = null;
			int childID = e.childID;
			if (childID >= 0 && childID < items.length) {
				String text = items[childID].getText();
				if (text != null) {
					char mnemonic = getMnemonic(text);	
					if (mnemonic != '\0') {
						shortcut = "Alt+"+mnemonic; //$NON-NLS-1$
					}
				}
			}
			e.result = shortcut;
		}
	});
	
	accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
		public void getChildAtPoint(AccessibleControlEvent e) {
			Point testPoint = toControl(new Point(e.x, e.y));
			int childID = ACC.CHILDID_NONE;
			for (int i = 0; i < items.length; i++) {
				if (items[i].getBounds().contains(testPoint)) {
					childID = i;
					break;
				}
			}
			if (childID == ACC.CHILDID_NONE) {
				Rectangle location = getBounds();
				location.height = location.height - getClientArea().height;
				if (location.contains(testPoint)) {
					childID = ACC.CHILDID_SELF;
				}
			}
			e.childID = childID;
		}

		
		public void getLocation(AccessibleControlEvent e) {
			Rectangle location = null;
			int childID = e.childID;
			if (childID == ACC.CHILDID_SELF) {
				location = getBounds();
			}
			if (childID >= 0 && childID < items.length) {
				location = items[childID].getBounds();
			}
			if (location != null) {
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}
		}
		
		public void getChildCount(AccessibleControlEvent e) {
			e.detail = items.length;
		}
		
		public void getDefaultAction(AccessibleControlEvent e) {
			String action = null;
			int childID = e.childID;
			if (childID >= 0 && childID < items.length) {
				action = "Switch"; //$NON-NLS-1$
			}
			e.result = action;
		}

		public void getFocus(AccessibleControlEvent e) {
			int childID = ACC.CHILDID_NONE;
			if (isFocusControl()) {
				if (selectedIndex == -1) {
					childID = ACC.CHILDID_SELF;
				} else {
					childID = selectedIndex;
				}
			}
			e.childID = childID;
		}

		public void getRole(AccessibleControlEvent e) {
			int role = 0;
			int childID = e.childID;
			if (childID == ACC.CHILDID_SELF) {
				role = ACC.ROLE_TABFOLDER;
			} else if (childID >= 0 && childID < items.length) {
				role = ACC.ROLE_TABITEM;
			}
			e.detail = role;
		}
		
		public void getSelection(AccessibleControlEvent e) {
			e.childID = (selectedIndex == -1) ? ACC.CHILDID_NONE : selectedIndex;
		}
		
		public void getState(AccessibleControlEvent e) {
			int state = 0;
			int childID = e.childID;
			if (childID == ACC.CHILDID_SELF) {
				state = ACC.STATE_NORMAL;
			} else if (childID >= 0 && childID < items.length) {
				state = ACC.STATE_SELECTABLE;
				if (isFocusControl()) {
					state |= ACC.STATE_FOCUSABLE;
				}
				if (selectedIndex == childID) {
					state |= ACC.STATE_SELECTED;
					if (isFocusControl()) {
						state |= ACC.STATE_FOCUSED;
					}
				}
			}
			e.detail = state;
		}
		
		public void getChildren(AccessibleControlEvent e) {
			Object[] children = new Object[items.length];
			for (int i = 0; i < items.length; i++) {
				children[i] = new Integer(i);
			}
			e.children = children;
		}
	});
	
	addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			if (isFocusControl()) {
				if (selectedIndex == -1) {
					accessible.setFocus(ACC.CHILDID_SELF);
				} else {
					accessible.setFocus(selectedIndex);
				}
			}
		}
	});

	addListener(SWT.FocusIn, new Listener() {
		public void handleEvent(Event event) {
			if (selectedIndex == -1) {
				accessible.setFocus(ACC.CHILDID_SELF);
			} else {
				accessible.setFocus(selectedIndex);
			}
		}
	});
}
boolean onArrowTraversal (Event event) {
	int count = items.length;
	if (count == 0) return false;
	if (selectedIndex  == -1) return false;
	int offset = (event.detail == SWT.TRAVERSE_ARROW_NEXT) ? 1 : -1;
	int index = selectedIndex + offset;
	if (index < 0 || index >= count) return false;
	setSelection (index, true);
	//setFocus();
	return true;
}
void onDispose() {
	/*
	 * Usually when an item is disposed, destroyItem will change the size of the items array, 
	 * reset the bounds of all the tabs and manage the widget associated with the tab.
	 * Since the whole folder is being disposed, this is not necessary.  For speed
	 * the inDispose flag is used to skip over this part of the item dispose.
	 */
	inDispose = true;
	
	int length = items.length;
	for (int i = 0; i < length; i++) {						
		if (items[i] != null) {
			items[i].dispose();
		}
	}
	
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;

	selectionBackground = null;
	selectionForeground = null;
}
void onFocus(Event event) {
	checkWidget();
	if (selectedIndex >= 0) {
		redraw();
	} else {
		setSelection(0, true);
	}
}
boolean onMnemonic (Event event) {
	char key = event.character;
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			char mnemonic = getMnemonic (items[i].getText ());
			if (mnemonic != '\0') {
				if (Character.toUpperCase (key) == Character.toUpperCase (mnemonic)) {
					setSelection(i, true);
					return true;
				}
			}
		}
	}
	return false;
}
void onMouseDoubleClick(Event event) { 
	Event e = new Event();
	e.item = getItem(new Point(event.x, event.y));
	notifyListeners(SWT.DefaultSelection, e);
}
void onMouseHover(Event event) {
	if (tipShowing) return;
	showToolTip(event.x, event.y);
}
void onMouse(Event event) {
	int x = event.x, y = event.y;
	switch (event.type) {
		case SWT.MouseExit: {
			if (closeImageState != NORMAL) {
				closeImageState = NORMAL;
				redraw(closeRect.x, closeRect.y, closeRect.width, closeRect.height, false);
			}
			if (expandImageState != NORMAL) {
				expandImageState = NORMAL;
				redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
			}
			if (chevronImageState != NORMAL) {
				chevronImageState = NORMAL;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
			}
			break;
		}
		case SWT.MouseDown: {
			if (closeRect.contains(x, y)) {
				if (event.button != 1) return;
				closeImageState = SELECTED;
				redraw(closeRect.x, closeRect.y, closeRect.width, closeRect.height, false);
				update();
				return;
			} 
			if (expandRect.contains(x, y)) {
				if (event.button != 1) return;
				expandImageState = SELECTED;
				redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
				update();
				return;
			}
			if (chevronRect.contains(x, y)) {
				if (event.button != 1) return;
				chevronImageState = SELECTED;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				update();
				return;
			}
			for (int i=0; i<items.length; i++) {
				CTabItem2 item = items[i];
				Rectangle bounds = item.getBounds();
				if (bounds.contains(x, y)) {
					if (!single && i != topTabIndex && bounds.x + bounds.width >= getRightItemEdge())return;
					setSelection(i, true);
					setFocus();
					return;
				}
			}
			break;
		}
		case SWT.MouseMove: {
			boolean close = false, expand = false, chevron = false;
			if (closeRect.contains(x, y)) {
				close = true;
				if (closeImageState != HOT) {
					closeImageState = HOT;
					redraw(closeRect.x, closeRect.y, closeRect.width, closeRect.height, false);
				}
			} 
			if (expandRect.contains(x, y)) {
				expand = true;
				if (expandImageState != HOT) {
					expandImageState = HOT;
					redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
				}
			}
			if (chevronRect.contains(x, y)) {
				chevron = true;
				if (chevronImageState != HOT) {
					chevronImageState = HOT;
					redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				}
			}
			if (closeImageState == HOT && !close) {
				closeImageState = NORMAL;
				redraw(closeRect.x, closeRect.y, closeRect.width, closeRect.height, false);
			}
			if (expandImageState == HOT && !expand) {
				expandImageState = NORMAL;
				redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
			}
			if (chevronImageState == HOT && !chevron) {
				chevronImageState = NORMAL;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
			}
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			if (closeRect.contains(x, y)) {
				closeImageState = HOT;
				redraw(closeRect.x, closeRect.y, closeRect.width, closeRect.height, false);
				if (selectedIndex == -1) return;
				CTabItem2 item = items[selectedIndex];
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				e.item = item;
				e.doit = true;
				for (int i = 0; i < closeListeners.length; i++) {
					closeListeners[i].itemClosed(e);
				}
				if (e.doit) item.dispose();
				return;
			}
			if (chevronRect.contains(x, y)) {
				chevronImageState = HOT;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				Rectangle rect = new Rectangle(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height);
				if (single && selectedIndex != -1){
					rect = items[selectedIndex].getBounds();	
				}
				rect.y += onBottom ? -HIGHLIGHT_HEADER :HIGHLIGHT_HEADER;
				if (listListeners.length == 0) {
					showList(rect, single ? SWT.LEFT : SWT.RIGHT);
				} else {
					CTabFolderEvent e = new CTabFolderEvent(this);
					e.widget = this;
					e.time = event.time;
					e.rect = rect;
					
					for (int i = 0; i < listListeners.length; i++) {
						listListeners[i].showList(e);
					}
				}
				return;
			}
			if (expandRect.contains(x, y)) {
				expandImageState = HOT;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				e.doit = true;
				for (int i = 0; i < expandListeners.length; i++) {
					if (expanded) {
						expandListeners[i].collapse(e);
					} else {
						expandListeners[i].expand(e);
					}
				}
				if (e.doit) {
					expanded = !expanded;
				}
				redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
				return;
			}
			if (single && items.length > 1) {
				for (int i=0; i<items.length; i++) {
					Rectangle bounds = items[i].getBounds();
					if (bounds.contains(x, y)) {
						Rectangle rect = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
						rect.y += onBottom ? -HIGHLIGHT_HEADER :HIGHLIGHT_HEADER;
						if (listListeners.length == 0) {
							showList(rect, SWT.LEFT);
						} else {
							CTabFolderEvent e = new CTabFolderEvent(this);
							e.widget = this;
							e.time = event.time;
							e.rect = rect;
							for (int j = 0; j < listListeners.length; j++) {
								listListeners[j].showList(e);
							}
						}
						return;
					}
				}
			}
			break;
		}
	}
}
boolean onPageTraversal(Event event) {
	int count = items.length;
	if (count == 0) return false;
	int index = selectedIndex; 
	if (index  == -1) {
		index = 0;
	} else {
		int offset = (event.detail == SWT.TRAVERSE_PAGE_NEXT) ? 1 : -1;
		index = (selectedIndex + offset + count) % count;
	}
	setSelection (index, true);
	//setFocus();
	return true;
}
void onPaint(Event event) {
	Font font = getFont();
	if (oldFont == null || !oldFont.equals(font)) {
		// handle case where  default font changes
		oldFont = font;
		if (!updateTabHeight(tabHeight)) {
			updateItems();
			redraw();
		}
	}
	
	GC gc = event.gc;
	Point size = getSize();
	Color parentBackground = getParent().getBackground();
	Color background = getBackground();
	
	if (items.length == 0) {
		gc.setBackground(parentBackground);
		gc.fillRectangle(0, 0, size.x, size.y);
		return;
	}
	
	if (single) {
		// Fill in the empty spaces to the right and left of the tab
		// with the background color
		if (selectedIndex != -1) {
			CTabItem2 item = items[selectedIndex];
			int[] shapeLeft = null;
			int[] shapeRight = null;
			if (onBottom) {
				int x = Math.max(0, borderLeft - 1);
				if (borderLeft > 1 & showHighlight) x -= HIGHLIGHT_MARGIN;
				int y = size.y - borderBottom - tabHeight;
				int width = item.x - x;
				int height = tabHeight;
				if (borderRight > 0) height -= 1; // fill in behind border
				shapeLeft = new int[BOTTOM_LEFT_OUTSIDE_CORNER.length+6];
				int index = 0;
				shapeLeft[index++] = x;
				shapeLeft[index++] = y;
				for (int i = 0; i < BOTTOM_LEFT_OUTSIDE_CORNER.length/2; i++) {
					shapeLeft[index++] = x+BOTTOM_LEFT_OUTSIDE_CORNER[2*i];
					shapeLeft[index++] = y+height+BOTTOM_LEFT_OUTSIDE_CORNER[2*i+1];
				}
				shapeLeft[index++] = x+width;
				shapeLeft[index++] = y + height;
				shapeLeft[index++] = x+width;
				shapeLeft[index++] = y;
				
				x = item.x + item.width;
				width = size.x - borderRight - x;
				if (borderRight > 1 & showHighlight) width += HIGHLIGHT_MARGIN;
				if (borderRight > 0) width += 1; // +1 overlap with border
				shapeRight = new int[BOTTOM_RIGHT_OUTSIDE_CORNER.length+6];
				index = 0;
				shapeRight[index++] = x;
				shapeRight[index++] = y;
				shapeRight[index++] = x;
				shapeRight[index++] = y+height;
				for (int i = 0; i < BOTTOM_RIGHT_OUTSIDE_CORNER.length/2; i++) {
					shapeRight[index++] = x+width+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i];
					shapeRight[index++] = y+height+BOTTOM_RIGHT_OUTSIDE_CORNER[2*i+1];
				}
				shapeRight[index++] = x+width;
				shapeRight[index++] = y;
			} else { // tabs on top
				int x = Math.max(0, borderLeft - 1);
				if (borderLeft > 1 & showHighlight) x -= HIGHLIGHT_MARGIN;
				int y = borderTop;
				int width = item.x - x;
				int height = tabHeight;
				shapeLeft = new int[TOP_LEFT_OUTSIDE_CORNER.length+6];
				int index = 0;
				shapeLeft[index++] = x;
				shapeLeft[index++] = y+height;
				for (int i = 0; i < TOP_LEFT_OUTSIDE_CORNER.length/2; i++) {
					shapeLeft[index++] = x+TOP_LEFT_OUTSIDE_CORNER[2*i];
					shapeLeft[index++] = y+TOP_LEFT_OUTSIDE_CORNER[2*i+1];
				}
				shapeLeft[index++] = x+width;
				shapeLeft[index++] = y;
				shapeLeft[index++] = x+width;
				shapeLeft[index++] = y+height;
				
				x = item.x + item.width;
				width = size.x - borderRight - x;
				if (borderRight > 1 & showHighlight) width += HIGHLIGHT_MARGIN;
				if (borderRight > 0) width += 1; // +1 overlap with border
				shapeRight = new int[TOP_RIGHT_OUTSIDE_CORNER.length+6];
				index = 0;
				shapeRight[index++] = x;
				shapeRight[index++] = y+height;
				shapeRight[index++] = x;
				shapeRight[index++] = y;
				for (int i = 0; i < TOP_RIGHT_OUTSIDE_CORNER.length/2; i++) {
					shapeRight[index++] = x+width+TOP_RIGHT_OUTSIDE_CORNER[2*i];
					shapeRight[index++] = y+TOP_RIGHT_OUTSIDE_CORNER[2*i+1];
				}
				shapeRight[index++] = x+width;
				shapeRight[index++] = y+height;
			}
			// Shape is non-rectangular, fill in gaps with parent colours	
			Region r = new Region();
			int x = Math.max(0, borderLeft - 1);
			if (borderLeft > 1 & showHighlight) x -= HIGHLIGHT_MARGIN;
			int y = onBottom ? size.y - borderBottom - tabHeight : borderTop;
			int width = item.x - x;
			int height = tabHeight;
			r.add(new Rectangle(x, y, width, height));
			r.subtract(shapeLeft);
			gc.setBackground(getParent().getBackground());
			fillRegion(gc, r);
			gc.setBackground(background);
			gc.fillPolygon(shapeLeft);
			
			x = item.x + item.width;
			width = size.x - borderRight - x;
			if (borderRight > 1 & showHighlight) width += borderRight;
			if (width > 0) {
				if (borderRight > 0) width += 1; // +1 overlap with border
				r.subtract(r); // clear region
				r.add(new Rectangle(x, y, width, height));
				r.subtract(shapeRight);
				gc.setBackground(getParent().getBackground());
				fillRegion(gc, r);
				gc.setBackground(background);
				gc.fillPolygon(shapeRight);
			}
			r.dispose();
		}
	} else {
		// Fill in the empty space to the right of the last tab
		// with the parent background color
		CTabItem2 lastItem = items[items.length -1];
		int edge = lastItem.x+lastItem.width;
		if (edge < size.x) {
			int x = edge;
			int y = onBottom ? size.y - borderBottom - tabHeight - 1 : borderTop;
			int width = size.x - edge - borderRight + 1;
			int height = tabHeight + 1;
			gc.setBackground(parentBackground);
			gc.fillRectangle(x, y, width, height);
		}
	}
	
	// Draw the unselected tabs.
	if (!single) {
		for (int i=0; i < items.length; i++) {
			if (i != selectedIndex && event.getBounds().intersects(items[i].getBounds())) {
				items[i].onPaint(gc, false);
			}
		}
	}
	
	// Draw selected tab
	if (selectedIndex != -1) {
		CTabItem2 item = items[selectedIndex];
		Rectangle rect = item.getBounds();
		if (event.getBounds().intersects(rect.x, rect.y, rect.width + CURVE_WIDTH, rect.height)) {
			item.onPaint(gc, true);
		}
	}
	
	drawClose(gc);
	drawChevron(gc);
	drawExpand(gc);
	
	drawBorder(gc);
	
	
	// draw insertion mark
//	if (insertionIndex > -2) {
//		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
//		if (insertionIndex == -1) {
//			Rectangle bounds = items[0].getBounds();
//			gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
//			gc.drawLine(bounds.x - 2, bounds.y, bounds.x + 2, bounds.y);
//			gc.drawLine(bounds.x - 1, bounds.y + 1, bounds.x + 1, bounds.y + 1);
//			gc.drawLine(bounds.x - 1, bounds.y + bounds.height - 2, bounds.x + 1, bounds.y + bounds.height - 2);
//			gc.drawLine(bounds.x - 2, bounds.y + bounds.height - 1, bounds.x + 2, bounds.y + bounds.height - 1);
//
//		} else {
//			Rectangle bounds = items[insertionIndex].getBounds();
//			gc.drawLine(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height - 1);
//			gc.drawLine(bounds.x + bounds.width - 2, bounds.y, bounds.x + bounds.width + 2, bounds.y);
//			gc.drawLine(bounds.x + bounds.width - 1, bounds.y + 1, bounds.x + bounds.width + 1, bounds.y + 1);
//			gc.drawLine(bounds.x + bounds.width - 1, bounds.y + bounds.height - 2, bounds.x + bounds.width + 1, bounds.y + bounds.height - 2);
//			gc.drawLine(bounds.x + bounds.width - 2, bounds.y + bounds.height - 1, bounds.x + bounds.width + 2, bounds.y + bounds.height - 1);
//		}
//	}
	
	// fill in client area
	int width = size.x  - borderLeft - borderRight;
	int height = size.y - borderTop - borderBottom - tabHeight - HIGHLIGHT_HEADER;
	int x = xClient - marginWidth;
	int y = yClient - marginHeight;
	gc.setForeground(background);
	gc.fillRectangle(x, y, width, height);
	
	gc.setForeground(getForeground());
	gc.setBackground(getBackground());	
}

void onResize() {
	if (items.length == 0) {
		redraw();
		return;
	}
	if (updateItems()) redraw();
	
	Point size = getSize();
	if (oldSize == null) {
		redraw();
	} else {
		if (onBottom && size.y != oldSize.y) {
			redraw();
		} else {
			int x1 = Math.min(size.x, oldSize.x);
			if (size.x != oldSize.x) x1 -= 10;
			int y1 = Math.min(size.y, oldSize.y);
			if (size.y != oldSize.y) y1 -= 10;
			int x2 = Math.max(size.x, oldSize.x);
			int y2 = Math.max(size.y, oldSize.y);		
			redraw(0, y1, x2 + 10, y2 - y1, false);
			redraw(x1, 0, x2 - x1, y2, false);
		}
	}
	oldSize = size;
	
	// resize content
	if (selectedIndex != -1) {
		Control control = items[selectedIndex].getControl();
		if (control != null && !control.isDisposed()) {
			control.setBounds(getClientArea());
		}
	}
}
void onTraverse (Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ARROW_NEXT:
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			event.doit = onArrowTraversal(event);
			event.detail = SWT.TRAVERSE_NONE;
			break;
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
			event.doit = true;
			break;
		case SWT.TRAVERSE_MNEMONIC:
			event.doit = onMnemonic(event);
			if (event.doit) event.detail = SWT.TRAVERSE_NONE;
			break;
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			event.doit = onPageTraversal(event);
			event.detail = SWT.TRAVERSE_NONE;
			break;
	}
}
/**
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void removeCTabFolderCloseListener(CTabFolderCloseListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (closeListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < closeListeners.length; i++) {
		if (listener == closeListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (closeListeners.length == 1) {
		// hide close button
		closeListeners = new CTabFolderCloseListener[0];
		showClose = false;
		setButtonBounds();
		return;
	}
	CTabFolderCloseListener[] newTabListeners = new CTabFolderCloseListener[closeListeners.length - 1];
	System.arraycopy(closeListeners, 0, newTabListeners, 0, index);
	System.arraycopy(closeListeners, index + 1, newTabListeners, index, closeListeners.length - index - 1);
	closeListeners = newTabListeners;
}
/**
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void removeCTabFolderExpandListener(CTabFolderExpandListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (expandListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < expandListeners.length; i++) {
		if (listener == expandListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (expandListeners.length == 1) {
		// hide expand button
		expandListeners = new CTabFolderExpandListener[0];
		showExpand = false;
		updateItems();
		redraw();
		return;
	}
	CTabFolderExpandListener[] newListeners = new CTabFolderExpandListener[expandListeners.length - 1];
	System.arraycopy(expandListeners, 0, newListeners, 0, index);
	System.arraycopy(expandListeners, index + 1, newListeners, index, expandListeners.length - index - 1);
	expandListeners = newListeners;
}
/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @deprecated see removeCTabFolderCloseListener(CTabFolderListener)
 */
public void removeCTabFolderListener(CTabFolderListener listener) {
	removeCTabFolderCloseListener(listener);
}
/**
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void removeCTabFolderListListener(CTabFolderListListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (listListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < listListeners.length; i++) {
		if (listener == listListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (listListeners.length == 1) {
		listListeners = new CTabFolderListListener[0];
		return;
	}
	CTabFolderListListener[] newListeners = new CTabFolderListListener[listListeners.length - 1];
	System.arraycopy(listListeners, 0, newListeners, 0, index);
	System.arraycopy(listListeners, index + 1, newListeners, index, listListeners.length - index - 1);
	listListeners = newListeners;
}
/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);	
}
public void setBackground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(BACKGROUND);
	super.setBackground(color);
	redraw();
}

/**
 * Toggle the visibility of the border
 * 
 * @param show true if the border should be displayed
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBorderVisible(boolean show) {
	checkWidget();
	if (showHighlight == show) return;
	showHighlight = show;
	redraw();
}
boolean setButtonBounds() {
	int decoratorWidth = 16;

	boolean changed = false;
	Point size = getSize();
	
	int oldX = closeRect.x;
	int oldY = closeRect.y;
	int oldWidth = closeRect.width;
	int oldHeight = closeRect.height;
	closeRect.x = closeRect.y = closeRect.height = closeRect.width = 0;
	if (showClose && selectedIndex != -1) {
		closeRect.x = size.x - borderRight - decoratorWidth;
		if (borderRight > 0) closeRect.x += 1; // align with first line of trim
		if (single) closeRect.x -= 3;
		closeRect.y = onBottom ? size.y - borderBottom - tabHeight : borderTop + 1;
		closeRect.width = decoratorWidth;
		closeRect.height = tabHeight;
	}
	if (oldX != closeRect.x || oldWidth != closeRect.width ||
	    oldY != closeRect.y || oldHeight != closeRect.height) changed = true;
	
	oldX = expandRect.x;
	oldY = expandRect.y;
	oldWidth = expandRect.width;
	oldHeight = expandRect.height;
	if (showExpand) {
		expandRect.x = size.x - borderRight - closeRect.width - decoratorWidth;
		if (borderRight > 0) expandRect.x += 1;
		if (single) expandRect.x -= 3;
		expandRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		expandRect.width = decoratorWidth;
		expandRect.height = tabHeight;
	}
	if (oldX != expandRect.x || oldWidth != expandRect.width ||
	    oldY != expandRect.y || oldHeight != expandRect.height) changed = true;
	
	oldX = chevronRect.x;
	oldY = chevronRect.y;
	oldWidth = chevronRect.width;
	oldHeight = chevronRect.height;
	chevronRect.x = chevronRect.y = chevronRect.height = chevronRect.width = 0;
	if (items.length > 1) {
		if (single && selectedIndex != -1){
			CTabItem2 item = items[selectedIndex];
			chevronRect.x = Math.min(item.x +item.width - 3, size.x - borderRight - closeRect.width - expandRect.width - decoratorWidth - 3);
			if (borderRight > 0) chevronRect.x += 1;
			chevronRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
			chevronRect.width = decoratorWidth;
			chevronRect.height = tabHeight;
		} else {
			int rightEdge = getRightItemEdge();
			CTabItem2 item = items[items.length-1];
			if (topTabIndex > 0 || item.x + item.width >= rightEdge) {
				chevronRect.x = size.x - borderRight - closeRect.width - expandRect.width - decoratorWidth;
				if (borderRight > 0) chevronRect.x += 1;
				chevronRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
				chevronRect.width = decoratorWidth;
				chevronRect.height = tabHeight;
			}
		}
	}
	if (oldX != chevronRect.x || oldWidth != chevronRect.width ||
	    oldY != chevronRect.y || oldHeight != chevronRect.height) changed = true;
	
	return changed;
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
	if (this.expanded == expanded) return;
	this.expanded = expanded;
	redraw(expandRect.x, expandRect.y, expandRect.width, expandRect.height, false);
}
void setFirstItem(int index) {
	if (index < 0 || index > items.length - 1) return;
	if (index == topTabIndex) return;
	topTabIndex = index;
	setItemLocation();
	redraw();
}
public void setFont(Font font) {
	checkWidget();
	if (font != null && font.equals(getFont())) return;
	super.setFont(font);
	oldFont = getFont();
	if (!updateTabHeight(tabHeight)) {
		updateItems();
		redraw();
	}
}
public void setForeground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(FOREGROUND);
	super.setForeground(color);
	redraw();
}
/**
 * Display an insert marker before or after the specified tab item. 
 * 
 * A value of null will clear the mark.
 * 
 * @param item the item with which the mark is associated or null
 * 
 * @param after true if the mark should be displayed after the specified item
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark(CTabItem2 item, boolean after) {
	checkWidget();
	int index = -1;
	if (item != null) {
		index = indexOf(item);
	}
	setInsertMark(index, after);
}
/**
 * Display an insert marker before or after the specified tab item.
 * 
 * A value of -1 will clear the mark.
 * 
 * @param index the index of the item with which the mark is associated or null
 * 
 * @param after true if the mark should be displayed after the specified item
 * 
 * @exception IllegalArgumentException<ul>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark(int index, boolean after) {
	checkWidget();
	if (index < -1 || index >= getItemCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
//	if (index == -1) {
//		index = -2;
//	} else {
//		index = after ? index : --index;
//	}
//	
//	if (insertionIndex == index) return;
//	int oldIndex = insertionIndex;
//	insertionIndex = index;
//	if (index > -1)	redrawTabArea(index);
//	if (oldIndex > 1) redrawTabArea(oldIndex);
}
boolean setItemLocation() {
	if (items.length == 0) return false;
	Point size = getSize();
	int y = onBottom ? Math.max(borderBottom, size.y - borderBottom - tabHeight) : borderTop;
	boolean changed = false;
	if (single) {
		int defaultX = size.x + 10; // off screen
		for (int i = 0; i < items.length; i++) {
			if (items[i].x != defaultX) changed = true;
			items[i].x = defaultX; 	
		}
		if (selectedIndex > -1) {
			CTabItem2 item = items[selectedIndex];
			int oldX = item.x, oldY = item.y;
			int tabWidth = size.x - borderLeft - borderRight - closeRect.width - expandRect.width - chevronRect.width;
			int indent = Math.max(0, (tabWidth-item.width)/2);
			item.x = borderLeft + indent; 
			item.y = y;
			if (item.x != oldX || item.y != oldY) changed = true;
		}
	} else {
		int x = -1;
		for (int i = topTabIndex - 1; i >= 0; i--) { 
			// if the first visible tab is not the first tab
			CTabItem2 tab = items[i];
			x -= tab.width; 
			if (!changed && (tab.x != x || tab.y != y) ) changed = true;
			// layout tab items from right to left thus making them invisible
			tab.x = x;
			tab.y = y;
		}
		
		x = borderLeft <= 1 ? 0 : HIGHLIGHT_MARGIN;
		for (int i = topTabIndex; i < items.length; i++) {
			// continue laying out remaining, visible items left to right 
			CTabItem2 tab = items[i];
			tab.x = x;
			tab.y = y;
			x = x + tab.width;
		}

		int rightEdge = getRightItemEdge();
		if (rightEdge > 0) {
			CTabItem2 item = items[items.length - 1];
			if (item.x + item.width < rightEdge) {
				setLastItem(items.length - 1);
				changed = true;
			}
		}

	}
	return changed;
}
boolean setItemSize() {
	if (isDisposed()) return false;
	Point size = getSize();
	if (size.x <= 0 || size.y <= 0 || items.length == 0) return false;
	boolean changed = false;
	xClient = borderLeft + marginWidth;
	if (onBottom) {
		yClient = borderTop + marginHeight; 
	} else {
		yClient = borderTop + tabHeight + HIGHLIGHT_HEADER + marginHeight; 
	}
	
	int[] widths = new int[items.length];
	GC gc = new GC(this);
	for (int i = 0; i < items.length; i++) {
		widths[i] = items[i].preferredWidth(gc, i == selectedIndex);
	}
	gc.dispose();
	int tabAreaWidth = size.x - borderLeft - borderRight - closeRect.width - expandRect.width - chevronRect.width;
	if (items.length > 1) {
		int selectedWidth = selectedIndex == -1 ? 0 : widths[selectedIndex];
		int count = selectedIndex == -1 ? items.length : items.length - 1;
		int averageWidth = (tabAreaWidth - selectedWidth) / count;
		int oldAverageWidth = 0;
		while (averageWidth > oldAverageWidth) {
			int width = tabAreaWidth - selectedWidth;
			for (int i = 0; i < items.length; i++) {
				if (i == selectedIndex) continue;
				if (widths[i] < averageWidth) {
					width -= widths[i];
					count--;
				}
			}
			oldAverageWidth = averageWidth;
			if (count > 0) {
				averageWidth = width / count;
			}
		}
		averageWidth = Math.max(averageWidth, MIN_TAB_WIDTH * tabHeight);
		for (int i = 0; i < items.length; i++) {
			if (i == selectedIndex) continue;
			if (widths[i] > averageWidth) {
				widths[i] = averageWidth;
			}
		}
	}
	int totalWidth = 0;
	for (int i = 0; i < items.length; i++) { 
		CTabItem2 tab = items[i];
		if (tab.height != tabHeight || tab.width != widths[i]) changed = true;
		tab.height = tabHeight;
		tab.width = widths[i];
		totalWidth += widths[i];
	}
	if (totalWidth <= tabAreaWidth) {
		topTabIndex = 0;		
	}
	return changed;
}
void setLastItem(int index) {
	if (index < 0 || index > items.length - 1) return;
	Point size = getSize();
	if (size.x <= 0) return;
	int maxWidth = size.x - borderLeft - borderRight - closeRect.width - expandRect.width - chevronRect.width;
	int tabWidth = items[index].width;
	while (index > 0) {
		tabWidth += items[index - 1].width;
		if (tabWidth > maxWidth) break;
		index--;
	}
	if (topTabIndex == index) return;
	topTabIndex = index;
	setItemLocation();
	redraw();
}
/**
 * Set the selection to the tab at the specified item.
 * 
 * @param item the tab item to be selected
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 */
public void setSelection(CTabItem2 item) {
	checkWidget();
	if (item == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf(item);
	setSelection(index);
}
/**
 * Set the selection to the tab at the specified index.
 * 
 * @param index the index of the tab item to be selected
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection(int index) {
	checkWidget();
	if (index < 0 || index >= items.length) return;
	if (selectedIndex == index) {
		showItem(items[index]);
		return;
	}
	
	int oldIndex = selectedIndex;
	selectedIndex = index;
	Control control = items[index].control;
	if (control != null && !control.isDisposed()) {
		control.setBounds(getClientArea());
		control.setVisible(true);
	}
	
	if (oldIndex != -1) {
		control = items[oldIndex].control;
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}		
	}
	setItemSize();
	setItemLocation();
	showItem(items[selectedIndex]);
	setButtonBounds();
	redraw();
}
void setSelection(int index, boolean notify) {	
	int oldSelectedIndex = selectedIndex;
	setSelection(index);
	if (notify && selectedIndex != oldSelectedIndex && selectedIndex != -1) {
		Event event = new Event();
		event.item = getItem(selectedIndex);
		notifyListeners(SWT.Selection, event);
	}
}
/**
 * @since 3.0
 */
public void setSelectionBackground (Color color) {
	checkWidget();
	if (selectionBackground == color) return;
	if (color == null) color = getDisplay().getSystemColor(SELECTION_BACKGROUND);
	selectionBackground = color;
	if (selectedIndex > -1) redraw();
}
/**
 * Specify a gradient of colours to be draw in the background of the selected tab.
 * For example to draw a gradient that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	cfolder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		               new int[] {25, 50, 100});
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradient 
 *               in order of appearance left to right.  The value <code>null</code> clears the
 *               background gradient. The value <code>null</code> can be used inside the array of 
 *               Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width 
 *                 of the widget at which the color should change.  The size of the percents array must be one 
 *                 less than the size of the colors array.
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */

public void setSelectionBackground(Color[] colors, int[] percents) {
	checkWidget();
	if (colors != null) {
		if (percents == null || percents.length != colors.length - 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < percents.length; i++) {
			if (percents[i] < 0 || percents[i] > 100) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (i > 0 && percents[i] < percents[i-1]) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (getDisplay().getDepth() < 15) {
			// Don't use gradients on low color displays
			colors = new Color[] {colors[0]};
			percents = new int[] {};
		}
	}
	
	// Are these settings the same as before?
	if (backgroundImage == null) {
		if ((gradientColors != null) && (colors != null) && 
			(gradientColors.length == colors.length)) {
			boolean same = false;
			for (int i = 0; i < gradientColors.length; i++) {
				if (gradientColors[i] == null) {
					same = colors[i] == null;
				} else {
					same = gradientColors[i].equals(colors[i]);
				}
				if (!same) break;
			}
			if (same) {
				for (int i = 0; i < gradientPercents.length; i++) {
					same = gradientPercents[i] == percents[i];
					if (!same) break;
				}
			}
			if (same) return;
		}
	} else {
		backgroundImage = null;
	}
	// Store the new settings
	if (colors == null) {
		gradientColors = null;
		gradientPercents = null;
	} else {
		gradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i) {
			gradientColors[i] = colors[i];
		}
		gradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i) {
			gradientPercents[i] = percents[i];
		}
	}

	// Refresh with the new settings
	if (selectedIndex > -1) redraw();
}

/**
 * Set the image to be drawn in the background of the selected tab.
 * 
 * @param image the image to be drawn in the background
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelectionBackground(Image image) {
	checkWidget();
	if (image == backgroundImage) return;
	if (image != null) {
		gradientColors = null;
		gradientPercents = null;
	}
	backgroundImage = image;
	if (selectedIndex > -1) redraw();
}
/**
 * Set the foreground color of the selected tab.
 * 
 * @param color the color of the text displayed in the selected tab
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelectionForeground (Color color) {
	checkWidget();
	if (selectionForeground == color) return;
	if (color == null) color = getDisplay().getSystemColor(SELECTION_FOREGROUND);
	selectionForeground = color;
	if (selectedIndex > -1) redraw();
}
/**
 * Specify a fixed height for the tab items.  If no height is specified,
 * the default height is the height of the text or the image, whichever 
 * is greater. Specifying a height of 0 will revert to the default height.
 * 
 * @param height the pixel value of the height or 0
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if called with a height of less than 0</li>
 * </ul>
 */
public void setTabHeight(int height) {
	checkWidget();
	if (height < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	fixedTabHeight = height > 0;
	int oldHeight = tabHeight;
	tabHeight = height;
	updateTabHeight(oldHeight);
}
/**
 * Set the control that appears in the top right corner of the tab folder.
 * Typically this is a close button or a composite with a Menu and close button. 
 * The topRight control is optional.  Setting the top right control to null will remove it from the tab folder.
 *
 * @since 2.1
 * 
 * @param control the control to be displayed in the top right corner or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this CTabFolder</li>
 * </ul>
 * 
 */
public void setTopRight(Control control) {
	checkWidget();
}
/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
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
 * @see CTabFolder#showSelection()
 * 
 * @since 2.0
 */
public void showItem (CTabItem2 item) {
	checkWidget();
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	Point size = getSize();
	int index = indexOf(item);
	if (size.x <= borderLeft + borderRight || index < topTabIndex) {
		setFirstItem(index);
		return;
	}
	int rightEdge = getRightItemEdge();
	if (item.x + item.width < rightEdge) return;
	setLastItem(index);
}
void showList (Rectangle rect, int alignment) {
	final Shell shell = new Shell(getShell(), SWT.BORDER | SWT.ON_TOP);
	shell.setLayout(new FillLayout());
	final Table table = new Table(shell, SWT.NONE);
//	table.setBackground(selectionBackground);
//	table.setForeground(selectionForeground);
	for (int i = 0; i < items.length; i++) {
		CTabItem2 tab = items[i];
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(tab.getText());
		item.setImage(tab.getImage());
	}
	if (selectedIndex != -1) {
		table.setSelection(selectedIndex);
	}
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
				case SWT.FocusOut:
					shell.dispose();
					break;
				case SWT.DefaultSelection:
				case SWT.MouseUp:
					int index = table.getSelectionIndex();						
					if (index != -1) {
						setSelection(index, true);
						//setFocus();
					}
					shell.dispose();
					break;
			}
		}
	};
	table.addListener(SWT.MouseUp, listener);
	table.addListener(SWT.DefaultSelection, listener);
	table.addListener(SWT.FocusOut, listener);
	Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	Rectangle displayRect = getMonitor().getClientArea();
	Rectangle clientArea = getClientArea();
	size.y = Math.min(displayRect.height/3, size.y);
	shell.setSize(size);
	Point p1 = getDisplay().map(this, null, clientArea.x, clientArea.y);
	Point p2 = getDisplay().map(this, null, rect.x, rect.y);
	int x = 0, y = 0;
	if (alignment == SWT.LEFT) {
		x = p2.x;
	} else { // Left
		x = p2.x + rect.width - size.x;
	}
	if (x < displayRect.x) x = displayRect.x;
	if (x + size.x > displayRect.x + displayRect.width) x = displayRect.x + displayRect.width - size.x;
	if (onBottom) {
		y = p1.y + clientArea.height - size.y;
		if (y < displayRect.y) y = p2.y + rect.height;
	} else {
		y = p1.y;
		if (y + size.y > displayRect.y + displayRect.height) y = p2.y - size.y;
	}
	shell.setLocation(x, y);
	shell.open();
	table.setFocus();
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
 * @see CTabFolder#showItem(CTabItem)
 * 
 * @since 2.0
 * 
 */
public void showSelection () {
	checkWidget (); 
	if (selectedIndex != -1) {
		showItem(getSelection());
	}
}
void showToolTip (int x, int y) {
	final Shell tip = new Shell (getShell(), SWT.ON_TOP);
	final Label label = new Label (tip, SWT.CENTER);
	Display display = tip.getDisplay();
	label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
	label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
	
	if (!updateToolTip(x, y, label)) {
		tip.dispose();
		return;
	}
	
	final int [] events = new int[] {SWT.MouseExit, SWT.MouseHover, SWT.MouseMove};
	final Listener[] listener = new Listener[1];
	listener[0] = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.MouseHover:
				case SWT.MouseMove:
					if (updateToolTip(event.x, event.y, label)) break;
					// FALL THROUGH
				case SWT.MouseExit:
					for (int i = 0; i < events.length; i++) {
						removeListener(events[i], listener[0]);
					}
					tip.dispose();
					tipShowing = false;
					break;
			}
		}
	};
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener[0]);
	}
	tipShowing = true;
	tip.setVisible(true);
}
boolean updateItems() {
	boolean changed = false;
	if (setItemSize()) changed = true;
	if (setItemLocation()) changed = true;
	if (setButtonBounds()) changed = true;
	return changed;
}
boolean updateTabHeight(int oldHeight){
	if (!fixedTabHeight) {
		int tempHeight = 0;
		GC gc = new GC(this);
		for (int i=0; i < items.length; i++) {
			tempHeight = Math.max(tempHeight, items[i].preferredHeight(gc));
		}
		gc.dispose();
		tabHeight =  tempHeight;
	}
	if (tabHeight == oldHeight) return false;
	
	oldSize = null;
	if (onBottom) {
		curve = bezier(0, tabHeight+2,
		               CURVE_LEFT, tabHeight+2,
				       CURVE_WIDTH - CURVE_RIGHT, 1,
		               CURVE_WIDTH, 1,
		               CURVE_WIDTH);
		// workaround to get rid of blip at end of bezier
		int index = -1;
		for (int i = 0; i < curve.length/2; i++) {
			if (curve[2*i+1] > tabHeight) {
				index = i;
			} else {
				break;
			}
		}
		if (index > 0) {
			int[] newCurve = new int[curve.length - 2*(index-1)];
			System.arraycopy(curve, 2*(index-1), newCurve, 0, newCurve.length);
			curve = newCurve;
		}	
	} else {
		curve = bezier(0, 0,
		               CURVE_LEFT, 0, 
		               CURVE_WIDTH - CURVE_RIGHT, tabHeight + 2,
		               CURVE_WIDTH, tabHeight + 2,
		               CURVE_WIDTH);
		// workaround to get rid of blip at end of bezier
		int index = -1;
		for (int i = 0; i < curve.length/2; i++) {
			if (curve[2*i+1] > tabHeight) {
				index = i;
				break;
			}
		}
		if (index > 0) {
			int[] newCurve = new int[2*(index-1)];
			System.arraycopy(curve, 0, newCurve, 0, newCurve.length);
			curve = newCurve;
		}
	}
	
	notifyListeners(SWT.Resize, new Event());
	return true;
}
boolean updateToolTip (int x, int y, Label label) {
	CTabItem2 item = getItem(new Point (x, y));
	if (item == null) return false;
	String tooltip = item.getToolTipText();
	if (tooltip == null) return false;
	if (tooltip.equals(label.getText())) return true;
	
	Shell tip = label.getShell();
	label.setText(tooltip);
	Point labelSize = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	labelSize.x += 2; labelSize.y += 2;
	label.setSize(labelSize);
	tip.pack();
	/*
	 * On some platforms, there is a minimum size for a shell  
	 * which may be greater than the label size.
	 * To avoid having the background of the tip shell showing
	 * around the label, force the label to fill the entire client area.
	 */
	Rectangle area = tip.getClientArea();
	label.setSize(area.width, area.height);
	/*
	 * Position the tooltip and ensure that it is not located off
	 * the screen.
	 */
	Point cursorLocation = getDisplay().getCursorLocation();
	// Assuming cursor is 21x21 because this is the size of
	// the arrow cursor on Windows 
	int cursorHeight = 21; 
	Point size = tip.getSize();
	Rectangle rect = tip.getMonitor().getBounds();
	Point pt = new Point(cursorLocation.x, cursorLocation.y + cursorHeight + 2);
	pt.x = Math.max(pt.x, rect.x);
	if (pt.x + size.x > rect.x + rect.width) pt.x = rect.x + rect.width - size.x;
	if (pt.y + size.y > rect.y + rect.height) {
		pt.y = cursorLocation.y - 2 - size.y;
	}
	tip.setLocation(pt);
	return true;
}
}