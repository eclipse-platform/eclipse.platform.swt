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
* UNDER CONSTRUCTION
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
 * <dd>CLOSE, TOP, BOTTOM, FLAT, BORDER, SINGLE, MULTI</dd>
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
 
public class CTabFolder extends Composite {
	
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
	 * 
	 * @deprecated
	 */
	public static RGB borderInsideRGB  = new RGB (132, 130, 132);
	/**
	 * Color of middle line of drop shadow border.
	 * 
	 * @deprecated
	 */
	public static RGB borderMiddleRGB  = new RGB (143, 141, 138);
	/**
	 * Color of outermost line of drop shadow border.
	 * 
	 * @deprecated
	 */
	public static RGB borderOutsideRGB = new RGB (171, 168, 165); 

	/* sizing, positioning */
	int xClient, yClient;
	boolean onBottom = false;
	boolean single = false;
	boolean fixedTabHeight;
	int tabHeight;
	
	/* item management */
	CTabItem items[] = new CTabItem[0];
	int selectedIndex = -1;
	int topTabIndex = -1; // index of the left most visible tab.

	/* External Listener management */
	CTabFolderCloseListener[] closeListeners = new CTabFolderCloseListener[0];
	CTabFolderMinMaxListener[] minmaxListeners = new CTabFolderMinMaxListener[0];
	CTabFolderListListener[] listListeners = new CTabFolderListListener[0];
	
	/* Selected item appearance */
	Image selectionBgImage;
	Color[] selectionGradientColors;
	int[] selectionGradientPercents;
	boolean selectionGradientVertical;
	Color selectionForeground;
	Color selectionBackground;
	
	/* Unselected item appearance */
	Image bgImage;
	Color[] gradientColors;
	int[] gradientPercents;
	boolean gradientVertical;
	
	static Color borderColor;
	
	// close, min/max and chevron buttons
	boolean showClose = false;
	
	Rectangle chevronRect = new Rectangle(0, 0, 0, 0);
	int chevronImageState = NORMAL;
	
	boolean showMin = false;
	Rectangle minRect = new Rectangle(0, 0, 0, 0);
	boolean minimized = false;
	int minImageState = NORMAL;
	
	boolean showMax = false;
	Rectangle maxRect = new Rectangle(0, 0, 0, 0);
	boolean maximized = false;
	int maxImageState = NORMAL;
	
	Control topRight;
	Rectangle topRightRect = new Rectangle(0, 0, 0, 0);
	
	boolean tipShowing;
	
	// borders and shapes
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
	static final int BUTTON_SIZE = 16;
	static final int[] TOP_LEFT_CORNER = new int[] {0,9, 1,8, 1,7, 2,6, 2,5, 3,4, 4,3, 5,2, 6,2, 7,1, 8,1, 9,0};
	static final int[] TOP_RIGHT_CORNER = new int[] {-9,0, -8,1, -7,1, -6,2, -5,2, -4,3, -3,4, -2,5, -2,6, -1,7, -1,8, 0,9};
	static final int[] BOTTOM_LEFT_CORNER = new int[] {0,-9, 1,-8, 1,-7, 2,-6, 2,-5, 3,-4, 4,-3, 5,-2, 6,-2, 7,-1, 8,-1, 9,0};
	static final int[] BOTTOM_RIGHT_CORNER = new int[] {-9,0, -8,-1, -7,-1, -6,-2, -5,-2, -4,-3, -3,-4, -2,-5, -2,-6, -1,-7, -1,-8, 0,-9};

	static final int SELECTION_FOREGROUND = SWT.COLOR_LIST_FOREGROUND;
	static final int SELECTION_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;
	static final int BORDER1_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;
	static final int FOREGROUND = SWT.COLOR_WIDGET_FOREGROUND;
	static final int BACKGROUND = SWT.COLOR_WIDGET_BACKGROUND;
	
	static final int NONE = 0;
	static final int NORMAL = 1;
	static final int HOT = 2;
	static final int SELECTED = 3;
	static final RGB CLOSE_BORDER = new RGB(221, 106, 106);
	static final RGB CLOSE_FILL = new RGB(214, 195, 195);
	static final RGB MINMAX_BORDER = new RGB(114, 106, 221);
	static final RGB MINMAX_FILL = new RGB(199, 196, 242);
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
public CTabFolder(Composite parent, int style) {
	super(parent, checkStyle (style));
	int style2 = super.getStyle();
	oldFont = getFont();
	onBottom = (style2 & SWT.BOTTOM) != 0;
	showClose = (style2 & SWT.CLOSE) != 0;
//	showMin = (style2 & SWT.MIN) != 0; - conflicts with SWT.TOP
//	showMax = (style2 & SWT.MAX) != 0; - conflicts with SWT.BOTTOM
	single = (style2 & SWT.SINGLE) != 0;
	borderLeft = borderRight = (style & SWT.BORDER) != 0 ? 1 : 0;
	borderTop = onBottom ? borderLeft : 0;
	borderBottom = onBottom ? 0 : borderLeft;
	
	//set up default colors
	Display display = getDisplay();
	selectionForeground = display.getSystemColor(SELECTION_FOREGROUND);
	selectionBackground = display.getSystemColor(SELECTION_BACKGROUND);
	borderColor = display.getSystemColor(BORDER1_COLOR);
	setForeground(display.getSystemColor(FOREGROUND));
	setBackground(display.getSystemColor(BACKGROUND));
	
	initAccessible();
	
	// Add all listeners
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:          onDispose(); break;
				case SWT.FocusIn:          onFocus(event);	break;
				case SWT.FocusOut:         onFocus(event);	break;
				case SWT.MenuDetect:       onMenu(event); break;
				case SWT.MouseDoubleClick: onMouseDoubleClick(event); break;
				case SWT.MouseDown:        onMouse(event);	break;
				case SWT.MouseExit:        onMouse(event);	break;
				case SWT.MouseHover:       onMouseHover(event); break;
				case SWT.MouseMove:        onMouse(event); break;
				case SWT.MouseUp:          onMouse(event); break;
				case SWT.Paint:            onPaint(event);	break;
				case SWT.Resize:           onResize();	break;
				case SWT.Traverse:         onTraverse(event); break;
			}
		}
	};

	int[] folderEvents = new int[]{
		SWT.Dispose,
		SWT.FocusIn, 
		SWT.FocusOut, 
		SWT.KeyDown,
		SWT.MenuDetect,
		SWT.MouseDoubleClick, 
		SWT.MouseDown,
		SWT.MouseExit,
		SWT.MouseHover, 
		SWT.MouseMove,
		SWT.MouseUp,
		SWT.Paint,
		SWT.Resize,  
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
	int mask = SWT.CLOSE | SWT.TOP | SWT.BOTTOM | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT | SWT.SINGLE | SWT.MULTI;
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
	style |= SWT.NO_REDRAW_RESIZE;
	//TEMPORARY CODE
	/*
	 * The default background on carbon and some GTK themes is not a solid color 
	 * but a texture.  To show the correct default background, we must allow
	 * the operating system to draw it and therefore, we can not use the 
	 * NO_BACKGROUND style.  The NO_BACKGROUND style is not required on platforms
	 * that use double buffering which is true in both of these cases.
	 */
	String platform = SWT.getPlatform();
	if ("carbon".equals(platform) || "gtk".equals(platform)) return style; //$NON-NLS-1$ //$NON-NLS-2$
	return style | SWT.NO_BACKGROUND;
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
public void addCTabFolderMinMaxListener(CTabFolderMinMaxListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolderMinMaxListener[] newListeners = new CTabFolderMinMaxListener[minmaxListeners.length + 1];
	System.arraycopy(minmaxListeners, 0, newListeners, 0, minmaxListeners.length);
	minmaxListeners = newListeners;
	minmaxListeners[minmaxListeners.length - 1] = listener;
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
	if (closeListeners.length == 1) {
		// display close button
		showClose = true;
		updateItems();
		redraw();
	}
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
void antialias (int[] shape, RGB lineRGB, RGB innerRGB, RGB outerRGB, GC gc){
	//don't perform anti-aliasing on Mac because the platform
	// already does it.
	if ("carbon".equals(SWT.getPlatform())) return;
	if (outerRGB != null) {
		int index = 0;
		boolean left = true;
		int oldY = onBottom ? 0 : getSize().y;
		int[] outer = new int[shape.length];
		for (int i = 0; i < shape.length/2; i++) {
			if (left && (index + 3 < shape.length)) {
				left = onBottom ? oldY <= shape[index+3] : oldY >= shape[index+3];
				oldY = shape[index+1];
			}
			outer[index] = shape[index++] + (left ? -1 : +1);
			outer[index] = shape[index++];
		}
		RGB from = lineRGB;
		RGB to = outerRGB;
		int red = from.red + 4*(to.red - from.red)/5;
		int green = from.green + 4*(to.green - from.green)/5;
		int blue = from.blue + 4*(to.blue - from.blue)/5;
		Color color = new Color(getDisplay(), red, green, blue);
		gc.setForeground(color);
		gc.drawPolyline(outer);
		color.dispose();
	}
	if (innerRGB != null) {
		int[] inner = new int[shape.length];
		int index = 0;
		boolean left = true;
		int oldY = onBottom ? 0 : getSize().y;
		for (int i = 0; i < shape.length/2; i++) {
			if (left && (index + 3 < shape.length)) {
				left = onBottom ? oldY <= shape[index+3] : oldY >= shape[index+3];
				oldY = shape[index+1];
			}
			inner[index] = shape[index++] + (left ? +1 : -1);
			inner[index] = shape[index++];
		}
		RGB from = lineRGB;
		RGB to = innerRGB;
		int red = from.red + 4*(to.red - from.red)/5;
		int green = from.green + 4*(to.green - from.green)/5;
		int blue = from.blue + 4*(to.blue - from.blue)/5;
		Color color = new Color(getDisplay(), red, green, blue);
		gc.setForeground(color);
		gc.drawPolyline(inner);
		color.dispose();
	}
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

	if (minimized) minHeight = 0;
	
	if (wHint != SWT.DEFAULT) minWidth  = wHint;
	if (hHint != SWT.DEFAULT) minHeight = hHint;

	Rectangle trim = computeTrim(0, 0, minWidth, minHeight);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	if (minimized) {
		int trimX = x - borderLeft;
		int trimY = onBottom ? y - borderTop : y - HIGHLIGHT_HEADER - tabHeight - borderTop;
		int trimWidth = width + borderLeft + borderRight;
		int trimHeight = borderTop + borderBottom + tabHeight + HIGHLIGHT_HEADER;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	} else {
		int style = getStyle();
		boolean highlight = (style & SWT.BORDER) != 0 && (style & SWT.FLAT) == 0;
		int trimX = x - marginWidth - borderLeft;
		if (highlight) trimX -= HIGHLIGHT_MARGIN;
		int trimY = onBottom ? y - marginHeight - borderTop : y - marginHeight - HIGHLIGHT_HEADER - tabHeight - borderTop;
		if (highlight && onBottom) trimX -= HIGHLIGHT_MARGIN;
		int trimWidth = width + borderLeft + borderRight + 2*marginWidth;
		if (highlight) trimWidth += 2*HIGHLIGHT_MARGIN;
		int trimHeight = height + borderTop + borderBottom + 2*marginHeight + tabHeight + HIGHLIGHT_HEADER;
		if (highlight) trimHeight += HIGHLIGHT_HEADER + HIGHLIGHT_MARGIN;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	}
}
void createItem (CTabItem item, int index) {
	if (0 > index || index > getItemCount ()){ 
		SWT.error (SWT.ERROR_INVALID_RANGE);
	}
	// grow by one and rearrange the array.
	CTabItem[] newItems = new CTabItem [items.length + 1];
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
		if (!updateTabHeight(tabHeight, false)) updateItems();
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
void destroyItem (CTabItem item) {
	if (inDispose) return;
	int index = indexOf(item);
	if (index == -1) return;
	insertionIndex = -2;
	
	if (items.length == 1) {
		items = new CTabItem[0];
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
	CTabItem[] newItems = new CTabItem [items.length - 1];
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
void drawBackground(GC gc, int[] shape, boolean selected) {
	Point size = getSize();
	int height = tabHeight + HIGHLIGHT_HEADER; 
	int y = onBottom ? size.y - borderBottom - height : borderTop;
	int x = 0;
	int width = size.x;
	if (borderLeft > 0) {
		x += 1; width -= 2;
	}
	
	Region clipping = new Region();
	gc.getClipping(clipping);
	Region region = new Region();
	region.add(shape);
	region.intersect(clipping);
	gc.setClipping(region);
	
	Color defaultBackground = selected ? selectionBackground : getBackground();
	Image image = selected ? selectionBgImage : bgImage;
	Color[] colors = selected ? selectionGradientColors : gradientColors;
	boolean vertical = selected ? selectionGradientVertical : gradientVertical;
	int[] percents = selected ? selectionGradientPercents : gradientPercents;
	
	if (image != null) {
		// draw the background image in shape
		gc.setBackground(defaultBackground);
		gc.fillRectangle(x, y, width, height);
		Rectangle imageRect = image.getBounds();
		gc.drawImage(image, imageRect.x, imageRect.y, imageRect.width, imageRect.height, x, y, width, height);
	} else if (colors != null) {
		// draw gradient
		if (colors.length == 1) {
			Color background = colors[0] != null ? colors[0] : defaultBackground;
			gc.setBackground(background);
			gc.fillRectangle(x, y, width, height);
		} else {
			if (vertical) {
				if (onBottom) {
					int pos = 0;
					if (percents[percents.length - 1] < 100) {
						pos = percents[percents.length - 1] * height / 100;
						gc.setBackground(defaultBackground);
						gc.fillRectangle(x, y, width, pos);
					}
					Color lastColor = colors[colors.length-1];
					if (lastColor == null) lastColor = defaultBackground;
					for (int i = percents.length-1; i >= 0; i--) {
						gc.setForeground(lastColor);
						lastColor = colors[i];
						if (lastColor == null) lastColor = defaultBackground;
						gc.setBackground(lastColor);
						int gradientHeight = percents[i] * height / 100;
						gc.fillGradientRectangle(x, y+pos, width, gradientHeight, true);
						pos += gradientHeight;
					}
				} else {
					Color lastColor = colors[0];
					if (lastColor == null) lastColor = defaultBackground;
					int pos = 0;
					for (int i = 0; i < percents.length; i++) {
						gc.setForeground(lastColor);
						lastColor = colors[i + 1];
						if (lastColor == null) lastColor = defaultBackground;
						gc.setBackground(lastColor);
						int gradientHeight = percents[i] * height / 100;
						gc.fillGradientRectangle(x, y+pos, width, gradientHeight, true);
						pos += gradientHeight;
					}
					if (pos < height) {
						gc.setBackground(defaultBackground);
						gc.fillRectangle(x, pos, width, height-pos);
					}
				}
			} else { //horizontal gradient
				y = 0;
				height = size.y;
				Color lastColor = colors[0];
				if (lastColor == null) lastColor = defaultBackground;
				int pos = 0;
				for (int i = 0; i < percents.length; ++i) {
					gc.setForeground(lastColor);
					lastColor = colors[i + 1];
					if (lastColor == null) lastColor = defaultBackground;
					gc.setBackground(lastColor);
					int gradientWidth = (percents[i] * width / 100) - pos;
					gc.fillGradientRectangle(x+pos, y, gradientWidth, height, false);
					pos += gradientWidth;
				}
				if (pos < width) {
					gc.setBackground(defaultBackground);
					gc.fillRectangle(x+pos, y, width-pos, height);
				}
			}
		}
	} else {
		// draw a solid background using default background in shape
		gc.setBackground(defaultBackground);
		gc.fillRectangle(x, y, width, height);
	}
	gc.setClipping(clipping);
	clipping.dispose();
	region.dispose();
}
void drawBody(Event event) {
	GC gc = event.gc;
	Point size = getSize();
	
	//draw 1 pixel border around outside
	if (borderLeft > 0) {
		gc.setForeground(borderColor);
		int x1 = borderLeft - 1;
		int x2 = size.x - borderRight;
		int y1 = onBottom ? borderTop - 1 : borderTop + tabHeight;
		int y2 = onBottom ? size.y - tabHeight - borderBottom - 1 : size.y - borderBottom;
		gc.drawLine(x1, y1, x1, y2); // left
		gc.drawLine(x2, y1, x2, y2); // right
		if (onBottom) {
			gc.drawLine(x1, y1, x2, y1); // top
		} else {
			gc.drawLine(x1, y2, x2, y2); // bottom
		}
	}
	
	// Draw highlight margin
	if (!minimized){
		int style = getStyle();
		int width = size.x  - borderLeft - borderRight;
		int height = size.y - borderTop - borderBottom - tabHeight - HIGHLIGHT_HEADER;
		if (borderLeft > 0 && (style & SWT.FLAT) == 0) {
			int[] shape = null;
			width -= 2*HIGHLIGHT_MARGIN;
			height -= HIGHLIGHT_MARGIN;
			if (onBottom) {
				int x1 = 1;
				int y1 = 1;
				int x2 = size.x - 1;
				int y2 = size.y - borderBottom - tabHeight - HIGHLIGHT_HEADER;
				shape = new int[] {x1,y1, x2,y1, x2,y2, x2-HIGHLIGHT_MARGIN,y2,
						           x2-HIGHLIGHT_MARGIN, y1+HIGHLIGHT_MARGIN, x1+HIGHLIGHT_MARGIN,y1+HIGHLIGHT_MARGIN,
								   x1+HIGHLIGHT_MARGIN,y2, x1,y2};
			} else {	
				int x1 = 1;
				int y1 = borderTop + tabHeight + HIGHLIGHT_HEADER;
				int x2 = size.x - 1;
				int y2 = size.y - 1;
				shape = new int[] {x1,y1, x1+HIGHLIGHT_MARGIN,y1, x1+HIGHLIGHT_MARGIN,y2-HIGHLIGHT_MARGIN, 
						           x2-HIGHLIGHT_MARGIN,y2-HIGHLIGHT_MARGIN, x2-HIGHLIGHT_MARGIN,y1,
								   x2,y1, x2,y2, x1,y2};
			}
			// If horizontal gradient, show gradient across the whole area
			if (selectedIndex != -1 && selectionGradientColors != null && selectionGradientColors.length > 1 && !selectionGradientVertical) {
				drawBackground(gc, shape, true);
			} else if (selectedIndex == -1 && gradientColors != null && gradientColors.length > 1 && !gradientVertical) {
				drawBackground(gc, shape, false);
			} else {
				gc.setBackground(selectedIndex == -1 ? getBackground() : selectionBackground);
				gc.fillPolygon(shape);
			}
		}
		//Draw client area
		if ((getStyle() & SWT.NO_BACKGROUND) != 0) {
			gc.setBackground(getBackground());
			gc.fillRectangle(xClient - marginWidth, yClient - marginHeight, width, height);
		}
	}
}

void drawChevron(GC gc) {
	if (chevronRect.width == 0 || chevronRect.height == 0) return;
	Display display = getDisplay();
	// draw chevron (10x7)
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = chevronRect.x + indent - 1;
	int y = chevronRect.y + indent;
	switch (chevronImageState) {
		case NORMAL: {
			int[] shape = onBottom ? new int[]{x,y+9, x+9,y+9, x+9,y+7, x+5,y+3, x+4,y+3, x,y+7, x,y+9} : new int[]{x,y, x+9,y, x+9,y+2, x+5,y+6, x+4,y+6, x,y+2, x,y};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			gc.setForeground(borderColor);
			gc.drawPolygon(shape);
			break;
		}
		case HOT: {
			int[] shape = onBottom ? new int[]{x,y+9, x+9,y+9, x+9,y+7, x+5,y+3, x+4,y+3, x,y+7, x,y+9} : new int[] {x,y, x+9,y, x+9,y+2, x+5,y+6, x+4,y+6, x,y+2, x,y};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			Color border = new Color(display, CHEVRON_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
		case SELECTED: {
			int[] shape = onBottom ? new int[]{x+1,y+10, x+10,y+10, x+10,y+8, x+6,y+4, x+5,y+4, x+1,y+8, x+1,y+10} : new int[] {x+1,y+1, x+10,y+1, x+10,y+3, x+6,y+7, x+5,y+7, x+1,y+3, x+1,y+1};
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
void drawMaximize(GC gc) {
	if (maxRect.width == 0 || maxRect.height == 0) return;
	Display display = getDisplay();
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = maxRect.x + indent - 1;
	int y = maxRect.y + indent;
	switch (maxImageState) {
		case NORMAL: {
			if (!maximized) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 7, 9);
				gc.setForeground(borderColor);
				gc.drawRectangle(x, y, 7, 9);
				gc.drawLine(x+1, y+2, x+6, y+2);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y+3, 5, 4);
				gc.fillRectangle(x+2, y, 5, 4);
				gc.setForeground(borderColor);
				gc.drawRectangle(x, y+3, 5, 4);
				gc.drawRectangle(x+2, y, 5, 4);
				gc.drawLine(x+3, y+1, x+6, y+1);
				gc.drawLine(x+1, y+4, x+4, y+4);
			}
			break;
		}
		case HOT: {
			Color border = new Color(display, MINMAX_BORDER);
			if (!maximized) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 7, 9);
				gc.setForeground(border);
				gc.drawRectangle(x, y, 7, 9);
				gc.drawLine(x+1, y+2, x+6, y+2);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y+3, 5, 4);
				gc.fillRectangle(x+2, y, 5, 4);
				gc.setForeground(border);
				gc.drawRectangle(x, y+3, 5, 4);
				gc.drawRectangle(x+2, y, 5, 4);
				gc.drawLine(x+3, y+1, x+6, y+1);
				gc.drawLine(x+1, y+4, x+4, y+4);
			}
			border.dispose();
			break;
		}
		case SELECTED: {
			Color fill = new Color(display, MINMAX_FILL);
			Color border = new Color(display, MINMAX_BORDER);
			if (!maximized) {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+1, 7, 9);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+1, 7, 9);
				gc.drawLine(x+2, y+3, x+7, y+3);
			} else {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+4, 5, 4);
				gc.fillRectangle(x+3, y+1, 5, 4);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+4, 5, 4);
				gc.drawRectangle(x+3, y+1, 5, 4);
				gc.drawLine(x+4, y+2, x+7, y+2);
				gc.drawLine(x+2, y+5, x+5, y+5);
			}
			fill.dispose();
			border.dispose();
			break;
		}
	}
}
void drawMinimize(GC gc) {
	if (minRect.width == 0 || minRect.height == 0) return;
	Display display = getDisplay();
	int indent = Math.max(1, (tabHeight-11)/2);
	int x = minRect.x + indent - 1;
	int y = minRect.y + indent;
	switch (minImageState) {
		case NORMAL: {
			if (!minimized) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 9, 3);
				gc.setForeground(borderColor);
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y+3, 5, 4);
				gc.fillRectangle(x+2, y, 5, 4);
				gc.setForeground(borderColor);
				gc.drawRectangle(x, y+3, 5, 4);
				gc.drawRectangle(x+2, y, 5, 4);
				gc.drawLine(x+3, y+1, x+6, y+1);
				gc.drawLine(x+1, y+4, x+4, y+4);
			}
			break;
		}
		case HOT: {
			Color border = new Color(display, MINMAX_BORDER);
			if (!minimized) {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y, 9, 3);
				gc.setForeground(border);
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(x, y+3, 5, 4);
				gc.fillRectangle(x+2, y, 5, 4);
				gc.setForeground(border);
				gc.drawRectangle(x, y+3, 5, 4);
				gc.drawRectangle(x+2, y, 5, 4);
				gc.drawLine(x+3, y+1, x+6, y+1);
				gc.drawLine(x+1, y+4, x+4, y+4);
			}
			border.dispose();
			break;
		}
		case SELECTED: {
			Color fill = new Color(display, MINMAX_FILL);
			Color border = new Color(display, MINMAX_BORDER);
			if (!minimized) {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+1, 9, 3);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+1, 9, 3);
			} else {
				gc.setBackground(fill);
				gc.fillRectangle(x+1, y+4, 5, 4);
				gc.fillRectangle(x+3, y+1, 5, 4);
				gc.setForeground(border);
				gc.drawRectangle(x+1, y+4, 5, 4);
				gc.drawRectangle(x+3, y+1, 5, 4);
				gc.drawLine(x+4, y+2, x+7, y+2);
				gc.drawLine(x+2, y+5, x+5, y+5);
			}
			fill.dispose();
			border.dispose();
			break;
		}
	}
}
void drawTabArea(Event event) {
	GC gc = event.gc;
	Point size = getSize();
	int[] shape = null;
	int x = Math.max(0, borderLeft - 1);
	int y = onBottom ? size.y - borderBottom - tabHeight : borderTop;
	int width = size.x - borderLeft - borderRight + 1;
	int height = tabHeight - 1;

	// Draw Tab Header
	if (onBottom) {
		shape = new int[BOTTOM_LEFT_CORNER.length + BOTTOM_RIGHT_CORNER.length + 4];
		int index = 0;
		shape[index++] = x;
		shape[index++] = y-HIGHLIGHT_HEADER-1;
		for (int i = 0; i < BOTTOM_LEFT_CORNER.length/2; i++) {
			shape[index++] = x+BOTTOM_LEFT_CORNER[2*i];
			shape[index++] = y+height+BOTTOM_LEFT_CORNER[2*i+1];
			if (borderLeft == 0) shape[index-1] += 1;
		}
		for (int i = 0; i < BOTTOM_RIGHT_CORNER.length/2; i++) {
			shape[index++] = x+width+BOTTOM_RIGHT_CORNER[2*i];
			shape[index++] = y+height+BOTTOM_RIGHT_CORNER[2*i+1];
			if (borderLeft == 0) shape[index-1] += 1;
		}
		shape[index++] = x+width;
		shape[index++] = y-HIGHLIGHT_HEADER-1;
	} else {
		shape = new int[TOP_LEFT_CORNER.length + TOP_RIGHT_CORNER.length + 4];
		int index = 0;
		shape[index++] = x;
		shape[index++] = y+height+HIGHLIGHT_HEADER+1;
		for (int i = 0; i < TOP_LEFT_CORNER.length/2; i++) {
			shape[index++] = x+TOP_LEFT_CORNER[2*i];
			shape[index++] = y+TOP_LEFT_CORNER[2*i+1];
		}
		for (int i = 0; i < TOP_RIGHT_CORNER.length/2; i++) {
			shape[index++] = x+width+TOP_RIGHT_CORNER[2*i];
			shape[index++] = y+TOP_RIGHT_CORNER[2*i+1];
		}
		shape[index++] = x+width;
		shape[index++] = y+height+HIGHLIGHT_HEADER+1;
	}
	// Fill in background
	drawBackground(gc, shape, false);
	// Fill in parent background for non-rectangular shape
	Region r = new Region();
	r.add(new Rectangle(x, y, width + 1, height + 1));
	r.subtract(shape);
	gc.setBackground(getParent().getBackground());
	fillRegion(gc, r);
	r.dispose();
	// Draw border line
	if (borderLeft > 0) {
		RGB inside = getBackground().getRGB();
		if (bgImage != null || (gradientColors != null && gradientColors.length > 1)) inside = null;
		RGB outside = getParent().getBackground().getRGB();
		antialias(shape, borderColor.getRGB(), inside, outside, gc);
		gc.setForeground(borderColor);
		gc.drawPolyline(shape);
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
		CTabItem item = items[selectedIndex];
		item.onPaint(gc, true);
	} else {
		// if no selected tab - draw line across bottom of all tabs
		int x1 = borderLeft;
		int y1 = (onBottom) ? size.y - borderBottom - tabHeight - 1 : borderTop + tabHeight;
		int x2 = size.x - borderRight;
		gc.setForeground(borderColor);
		gc.drawLine(x1, y1, x2, y1);
	}
	
	// Draw Buttons
	drawChevron(gc);
	drawMinimize(gc);
	drawMaximize(gc);
	
	// Draw insertion mark
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
}
/**
 * 
 * @since 3.0
 */
public boolean getBorderVisible() {
	checkWidget();
	return borderLeft == 1;
}
public Rectangle getClientArea() {
	checkWidget();
	if (minimized) return new Rectangle(xClient, yClient, 0, 0);
	int style = getStyle();
	boolean highlight = (style & SWT.BORDER) != 0 && (style & SWT.FLAT) == 0;
	Point size = getSize();
	int width = size.x  - borderLeft - borderRight - 2*marginWidth;
	if (highlight)width -= 2*HIGHLIGHT_MARGIN;
	int height = size.y - borderTop - borderBottom - 2*marginHeight;
	if (highlight)height -= + HIGHLIGHT_MARGIN;
	if (items.length == 0) {		
		return new Rectangle(borderLeft + marginWidth, borderTop + marginHeight, width, height);	
	}
	height -= tabHeight+HIGHLIGHT_HEADER;
	return new Rectangle(xClient, yClient, width, height);
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
public CTabItem getItem (int index) {
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
public CTabItem getItem (Point pt) {
	//checkWidget();
	if (items.length == 0) return null;
	Point size = getSize();
	if (size.x <= borderLeft + borderRight) return null;
	for (int index = topTabIndex; index < items.length; index++) {
		CTabItem item = items[index];
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
public CTabItem [] getItems() {
	//checkWidget();
	CTabItem[] tabItems = new CTabItem [items.length];
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
/**
 * Returns <code>true</code> if the receiver is minimized,
 * and false otherwise.
 * <p>
 *
 * @return the minimized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getMinimized() {
	checkWidget();
	return minimized;
}
/**
 * Returns <code>true</code> if the receiver is maximized,
 * and false otherwise.
 * <p>
 *
 * @return the maximized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getMaximized() {
	checkWidget();
	return maximized;
}
int getRightItemEdge (){
	return getSize().x - borderRight - minRect.width - maxRect.width - topRightRect.width - chevronRect.width - 1;
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
public CTabItem getSelection() {
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
public int getStyle() {
	int style = super.getStyle();
	style &= ~(SWT.TOP | SWT.BOTTOM);
	style |= onBottom ? SWT.BOTTOM : SWT.TOP;
	style &= ~(SWT.SINGLE | SWT.MULTI);
	style |= single ? SWT.SINGLE : SWT.MULTI;
	if (borderLeft != 0) style |= SWT.BORDER;
	return style;
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
 */
public Control getTopRight() {
	checkWidget();
	return topRight;
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
public int indexOf(CTabItem item) {
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
	
	selectionGradientColors = null;
	selectionGradientPercents = null;
	selectionBgImage = null;

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
void onMenu(Event event) {
	if (single && selectedIndex != -1) {
		final CTabFolderEvent e = new CTabFolderEvent(CTabFolder.this);
		e.widget = this;
		e.time = event.time;
		Rectangle rect = items[selectedIndex].getBounds();
		rect.y += onBottom ? -HIGHLIGHT_HEADER : HIGHLIGHT_HEADER;
		e.rect = rect;
		if (listListeners.length == 0) {
			showList(e.rect, SWT.LEFT);
		} else {
			for (int j = 0; j < listListeners.length; j++) {
				listListeners[j].showList(e);
			}
		}
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
	int x = event.x, y = event.y;
	if (minRect.contains(x, y)) return;
	if (maxRect.contains(x, y)) return;
	if (chevronRect.contains(x, y)) return;
	
	if (showMax) {
		CTabFolderEvent e = new CTabFolderEvent(this);
		e.widget = this;
		e.time = event.time;
		e.doit = true;
		boolean restore = maximized;
		for (int i = 0; i < minmaxListeners.length; i++) {
			if (restore) {
				minmaxListeners[i].restore(e);
			} else {
				minmaxListeners[i].maximize(e);
			}
		}
		if (e.doit) setMaximized(!restore);
	}
		
	
	Event e = new Event();
	e.item = getItem(new Point(event.x, event.y));
	if (e.item != null) {
		notifyListeners(SWT.DefaultSelection, e);
	}
}
void onMouseHover(Event event) {
	if (tipShowing) return;
	showToolTip(event.x, event.y);
}
void onMouse(Event event) {
	int x = event.x, y = event.y;
	switch (event.type) {
		case SWT.MouseExit: {
			if (minImageState != NORMAL) {
				minImageState = NORMAL;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				update();
			}
			if (maxImageState != NORMAL) {
				maxImageState = NORMAL;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				update();
			}
			if (chevronImageState != NORMAL) {
				chevronImageState = NORMAL;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				update();
			}
			for (int i=0; i<items.length; i++) {
				CTabItem item = items[i];
				if (i != selectedIndex && item.closeImageState != NONE) {
					item.closeImageState = NONE;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
				}
				if (i == selectedIndex && item.closeImageState != NORMAL) {
					item.closeImageState = NORMAL;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
				}
			}
			break;
		}
		case SWT.MouseDown: {
			if (minRect.contains(x, y)) {
				if (event.button != 1) return;
				minImageState = SELECTED;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				update();
				return;
			}
			if (maxRect.contains(x, y)) {
				if (event.button != 1) return;
				maxImageState = SELECTED;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
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
				CTabItem item = items[i];
				Rectangle bounds = item.getBounds();
				if (item.closeRect.contains(x,y)){
					if (event.button != 1) return;
					item.closeImageState = SELECTED;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
					return;
				}
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
			boolean close = false, minimize = false, maximize = false, chevron = false;
			if (minRect.contains(x, y)) {
				minimize = true;
				if (minImageState != SELECTED && minImageState != HOT) {
					minImageState = HOT;
					redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
					update();
				}
			}
			if (maxRect.contains(x, y)) {
				maximize = true;
				if (maxImageState != SELECTED && maxImageState != HOT) {
					maxImageState = HOT;
					redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
					update();
				}
			}
			if (chevronRect.contains(x, y)) {
				chevron = true;
				if (chevronImageState != SELECTED && chevronImageState != HOT) {
					chevronImageState = HOT;
					redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
					update();
				}
			}
			if (minImageState != NORMAL && !minimize) {
				minImageState = NORMAL;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				update();
			}
			if (maxImageState != NORMAL && !maximize) {
				maxImageState = NORMAL;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				update();
			}
			if (chevronImageState != NORMAL && !chevron) {
				chevronImageState = NORMAL;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				update();
			}
			for (int i=0; i<items.length; i++) {
				CTabItem item = items[i];
				close = false;
				if (item.getBounds().contains(x, y)) {
					close = true;
					if (item.closeRect.contains(x, y)) {
						if (item.closeImageState != SELECTED && item.closeImageState != HOT) {
							item.closeImageState = HOT;
							redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
							update();
						}
					} else {
						if (item.closeImageState != NORMAL) {
							item.closeImageState = NORMAL;
							redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
							update();
						}
					}
				} 
				if (i != selectedIndex && item.closeImageState != NONE && !close) {
					item.closeImageState = NONE;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
				}
				if (i == selectedIndex && item.closeImageState != NORMAL && !close) {
					item.closeImageState = NORMAL;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
				}
			}
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			if (chevronRect.contains(x, y)) {
				boolean selected = chevronImageState == SELECTED;
				chevronImageState = HOT;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				update();
				if (!selected) return;
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
			if (minRect.contains(x, y)) {
				boolean selected = minImageState == SELECTED;
				minImageState = HOT;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				update();
				if (!selected) return;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				e.doit = true;
				boolean restore = minimized;
				for (int i = 0; i < minmaxListeners.length; i++) {
					if (restore) {
						minmaxListeners[i].restore(e);
					} else {
						minmaxListeners[i].minimize(e);
					}
				}
				if (e.doit && !isDisposed()) setMinimized(!restore);
				return;
			}
			if (maxRect.contains(x, y)) {
				boolean selected = maxImageState == SELECTED;
				maxImageState = HOT;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				update();
				if (!selected) return;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				e.doit = true;
				boolean restore = maximized;
				for (int i = 0; i < minmaxListeners.length; i++) {
					if (restore) {
						minmaxListeners[i].restore(e);
					} else {
						minmaxListeners[i].maximize(e);
					}
				}
				if (e.doit && !isDisposed()) setMaximized(!restore);
				return;
			}
			for (int i=0; i<items.length; i++) {
				CTabItem item = items[i];
				if (item.closeRect.contains(x,y)){
					boolean selected = item.closeImageState == SELECTED;
					item.closeImageState = HOT;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
					if (!selected) return;
					CTabFolderEvent e = new CTabFolderEvent(this);
					e.widget = this;
					e.time = event.time;
					e.item = item;
					e.doit = true;
					for (int j = 0; j < closeListeners.length; j++) {
						closeListeners[j].itemClosed(e);
					}
					if (e.doit) item.dispose();
					return;
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
		if (!updateTabHeight(tabHeight, false)) {
			updateItems();
			redraw();
			/*
			* Bug on carbon.  Calling redraw inside 
			* a paint event will not cause a redraw
			* to happen.
			*/
			String platform = SWT.getPlatform();
			if (!("carbon".equals(platform))) return;
		}
	}

// Useful for debugging paint problems
//{
//GC gc = event.gc;
//Point size = getSize();	
//gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
//gc.fillRectangle(-10, -10, size.x + 20, size.y+20);
//}
	
	GC gc = event.gc;
	if (items.length == 0) {
		Point size = getSize();	
		gc.setBackground(getParent().getBackground());
		gc.fillRectangle(0, 0, size.x, size.y);
		return;
	}
	
	drawBody(event);
	drawTabArea(event);
	
	gc.setForeground(getForeground());
	gc.setBackground(getBackground());	
}

void onResize() {
	if (items.length == 0) {
		redraw();
		return;
	}
	if (updateItems()) redraw();
	showSelection();
	
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
		closeListeners = new CTabFolderCloseListener[0];
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
public void removeCTabFolderMinMaxListener(CTabFolderMinMaxListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (minmaxListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < minmaxListeners.length; i++) {
		if (listener == minmaxListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (minmaxListeners.length == 1) {
		minmaxListeners = new CTabFolderMinMaxListener[0];
		return;
	}
	CTabFolderMinMaxListener[] newListeners = new CTabFolderMinMaxListener[minmaxListeners.length - 1];
	System.arraycopy(minmaxListeners, 0, newListeners, 0, index);
	System.arraycopy(minmaxListeners, index + 1, newListeners, index, minmaxListeners.length - index - 1);
	minmaxListeners = newListeners;
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
	if (closeListeners.length == 0) {
		// hide close button
		closeListeners = new CTabFolderCloseListener[0];
		showClose = false;
		updateItems();
		redraw();
		return;
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
 * Specify a gradient of colours to be draw in the background of the unselected tabs.
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
 *
 * @since 3.0
 */
public void setBackground(Color[] colors, int[] percents) {
	setBackground(colors, percents, false);
}
/**
 * Specify a gradient of colours to be draw in the background of the unselected tab.
 * For example to draw a vertical gradient that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	cfolder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		                  new int[] {25, 50, 100}, true);
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
 * @param vertical indicate the direction of the gradient.  True is vertical and false is horizontal. 
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 *@since 3.0
 */
public void setBackground(Color[] colors, int[] percents, boolean vertical) {
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
	if (bgImage == null) {
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
			if (same && this.gradientVertical == vertical) return;
		}
	} else {
		bgImage = null;
	}
	// Store the new settings
	if (colors == null) {
		gradientColors = null;
		gradientPercents = null;
		gradientVertical = false;
		setBackground((Color)null);
	} else {
		gradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i) {
			gradientColors[i] = colors[i];
		}
		gradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i) {
			gradientPercents[i] = percents[i];
		}
		gradientVertical = vertical;
		setBackground(gradientColors[gradientColors.length-1]);
	}

	// Refresh with the new settings
	redraw();
}

/**
 * Set the image to be drawn in the background of the unselected tab.  Image
 * is stretched or compressed to cover entire unselected tab area.
 * 
 * @param image the image to be drawn in the background
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setBackground(Image image) {
	checkWidget();
	if (image == bgImage) return;
	if (image != null) {
		gradientColors = null;
		gradientPercents = null;
	}
	bgImage = image;
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
	if ((borderLeft == 1) == show) return;
	borderLeft = borderRight = show ? 1 : 0;
	borderTop = onBottom ? borderLeft : 0;
	borderBottom = onBottom ? 0 : borderLeft;
	Rectangle rectBefore = getClientArea();
	updateItems();
	Rectangle rectAfter = getClientArea();
	if (!rectBefore.equals(rectAfter)) {
		notifyListeners(SWT.Resize, new Event());
	}
	redraw();
}
boolean setButtonBounds() {
	int oldX, oldY, oldWidth, oldHeight;
	boolean changed = false;
	Point size = getSize();
	
	oldX = maxRect.x;
	oldY = maxRect.y;
	oldWidth = maxRect.width;
	oldHeight = maxRect.height;
	maxRect.x = maxRect.y = maxRect.width = maxRect.height = 0;
	if (showMax) {
		maxRect.x = size.x - borderRight - BUTTON_SIZE - 3;
		if (borderRight > 0) maxRect.x += 1;
		maxRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		maxRect.width = BUTTON_SIZE;
		maxRect.height = tabHeight - 1;
	}
	if (oldX != maxRect.x || oldWidth != maxRect.width ||
	    oldY != maxRect.y || oldHeight != maxRect.height) changed = true;
	
	oldX = minRect.x;
	oldY = minRect.y;
	oldWidth = minRect.width;
	oldHeight = minRect.height;
	minRect.x = minRect.y = minRect.width = minRect.height = 0;
	if (showMin) {
		minRect.x = size.x - borderRight - maxRect.width - BUTTON_SIZE - 3;
		if (borderRight > 0) minRect.x += 1;
		minRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		minRect.width = BUTTON_SIZE;
		minRect.height = tabHeight - 1;
	}
	if (oldX != minRect.x || oldWidth != minRect.width ||
	    oldY != minRect.y || oldHeight != minRect.height) changed = true;
	
	oldX = topRightRect.x;
	oldY = topRightRect.y;
	oldWidth = topRightRect.width;
	oldHeight = topRightRect.height;
	topRightRect.x = topRightRect.y = topRightRect.width = topRightRect.height = 0;
	if (topRight != null) {
		Point topRightSize = topRight.computeSize(SWT.DEFAULT, tabHeight);
		if (single && selectedIndex > -1) {
			CTabItem item = items[selectedIndex];
			topRightRect.x = Math.min(item.x +item.width + BUTTON_SIZE, size.x - borderRight - minRect.width - maxRect.width - topRightSize.x - 3);
		} else {
			topRightRect.x = size.x - borderRight - minRect.width - maxRect.width - topRightSize.x - 3;
		}
		topRightRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		topRightRect.width = topRightSize.x;
		topRightRect.height = tabHeight - 1;
		topRight.setBounds(topRightRect);
	}
	if (oldX != topRightRect.x || oldWidth != topRightRect.width ||
		oldY != topRightRect.y || oldHeight != topRightRect.height) {	
		changed = true;
	}
		
	oldX = chevronRect.x;
	oldY = chevronRect.y;
	oldWidth = chevronRect.width;
	oldHeight = chevronRect.height;
	chevronRect.x = chevronRect.y = chevronRect.height = chevronRect.width = 0;
	if (items.length > 1) {
		if (single && selectedIndex > -1){
			CTabItem item = items[selectedIndex];
			chevronRect.x = Math.min(item.x +item.width - 3, size.x - borderRight - minRect.width - maxRect.width - topRightRect.width - BUTTON_SIZE - 3);
			if (borderRight > 0) chevronRect.x += 1;
			chevronRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
			chevronRect.width = BUTTON_SIZE;
			chevronRect.height = tabHeight - 1;
		} else {
			int rightEdge = getRightItemEdge();
			CTabItem item = items[items.length-1];
			if (topTabIndex > 0 || item.x + item.width >= rightEdge) {
				chevronRect.x = size.x - borderRight - minRect.width - maxRect.width - topRightRect.width - BUTTON_SIZE - 3;
				if (borderRight > 0) chevronRect.x += 1;
				chevronRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
				chevronRect.width = BUTTON_SIZE;
				chevronRect.height = tabHeight - 1;
			}
		}
	}
	if (oldX != chevronRect.x || oldWidth != chevronRect.width ||
	    oldY != chevronRect.y || oldHeight != chevronRect.height) changed = true;
	
	return changed;
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
	if (!updateTabHeight(tabHeight, false)) {
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
public void setInsertMark(CTabItem item, boolean after) {
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
			CTabItem item = items[selectedIndex];
			int oldX = item.x, oldY = item.y;
			int tabWidth = size.x - borderLeft - borderRight - minRect.width - maxRect.width - chevronRect.width;
			int indent = Math.max(0, (tabWidth-item.width)/2);
			item.x = borderLeft + indent; 
			item.y = y;
			if (showClose || item.showClose) {
				item.closeRect.x = item.x + item.width - BUTTON_SIZE - CTabItem.RIGHT_MARGIN - 8;
				item.closeRect.y = onBottom ? y : y + CTabItem.TOP_MARGIN;
			}
			if (item.x != oldX || item.y != oldY) changed = true;
		}
	} else {
		int x = -1;
		for (int i = topTabIndex - 1; i >= 0; i--) { 
			// if the first visible tab is not the first tab
			CTabItem item = items[i];
			x -= item.width; 
			if (!changed && (item.x != x || item.y != y) ) changed = true;
			// layout tab items from right to left thus making them invisible
			item.x = x;
			item.y = y;
			item.closeRect.x = item.x + item.width - BUTTON_SIZE - CTabItem.RIGHT_MARGIN;
			if (i == selectedIndex) item.closeRect.x -= 8;
			item.closeRect.y = onBottom ? y : y + CTabItem.TOP_MARGIN;
		}
		
		x = borderLeft <= 1 ? 0 : HIGHLIGHT_MARGIN;
		for (int i = topTabIndex; i < items.length; i++) {
			// continue laying out remaining, visible items left to right 
			CTabItem item = items[i];
			item.x = x;
			item.y = y;
			item.closeRect.x = item.x + item.width - BUTTON_SIZE - CTabItem.RIGHT_MARGIN;
			if (i == selectedIndex) item.closeRect.x -= 8;
			item.closeRect.y = onBottom ? y : y + CTabItem.TOP_MARGIN;
			x = x + item.width;
		}

		int rightEdge = getRightItemEdge();
		if (rightEdge > 0) {
			CTabItem item = items[items.length - 1];
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
	int style = getStyle();
	boolean highlight = borderLeft > 0 && (style & SWT.FLAT) == 0;
	boolean changed = false;
	xClient = borderLeft + marginWidth;
	if (highlight) xClient += HIGHLIGHT_MARGIN;
	if (onBottom) {
		yClient = borderTop + marginHeight; 
		if (highlight) yClient += HIGHLIGHT_MARGIN;
	} else {
		yClient = borderTop + tabHeight + HIGHLIGHT_HEADER + marginHeight; 
	}
	
	int[] widths = new int[items.length];
	GC gc = new GC(this);
	for (int i = 0; i < items.length; i++) {
		widths[i] = items[i].preferredWidth(gc, i == selectedIndex);
	}
	gc.dispose();
	int tabAreaWidth = size.x - borderLeft - borderRight - minRect.width - maxRect.width - chevronRect.width;
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
		CTabItem tab = items[i];
		if (tab.height != tabHeight || tab.width != widths[i]) changed = true;
		tab.height = tabHeight;
		tab.width = widths[i];
		tab.closeRect.width = tab.closeRect.height = 0;
		if (showClose || tab.showClose) {
			tab.closeRect.width = BUTTON_SIZE;
			tab.closeRect.height = BUTTON_SIZE;
		}
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
	int maxWidth = getRightItemEdge() - borderLeft;
	int tabWidth = items[index].width;
	while (index > 0) {
		tabWidth += items[index - 1].width;
		if (tabWidth >= maxWidth) break;
		index--;
	}
	if (topTabIndex == index) return;
	topTabIndex = index;
	setItemLocation();
	redraw();
}
/**
 * 
 * @since 3.0
 */
public void setMaximizeVisible(boolean visible) {
	checkWidget();
	if (showMax == visible) return;
	// display maximize button
	showMax = visible;
	updateItems();
	redraw();
}
/**
 * 
 * @since 3.0
 */
public void setMaximized(boolean maximize) {
	checkWidget ();
	if (this.maximized == maximize) return;
	if (maximize && this.minimized) setMinimized(false);
	this.maximized = maximize;
	redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
	update();
}
/**
 * 
 * @since 3.0
 */
public void setMinimizeVisible(boolean visible) {
	checkWidget();
	if (showMin == visible) return;
	// display maximize button
	showMin = visible;
	updateItems();
	redraw();
}
/**
 * 
 * @since 3.0
 */
public void setMinimized(boolean minimize) {
	checkWidget ();
	if (this.minimized == minimize) return;
	if (minimize && this.maximized) setMaximized(false);
	this.minimized = minimize;
	redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
	update();
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
public void setSelection(CTabItem item) {
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
	if (oldIndex != -1) {
		items[oldIndex].closeImageState = NONE;
	}
	items[selectedIndex].closeImageState = NORMAL;
	
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
	updateItems();
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
 * 
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
	setSelectionBackground(colors, percents, false);
}
/**
 * Specify a gradient of colours to be draw in the background of the selected tab.
 * For example to draw a vertical gradient that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	cfolder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		                  new int[] {25, 50, 100}, true);
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
 * @param vertical indicate the direction of the gradient.  True is vertical and false is horizontal. 
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void setSelectionBackground(Color[] colors, int[] percents, boolean vertical) {
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
	if (selectionBgImage == null) {
		if ((selectionGradientColors != null) && (colors != null) && 
			(selectionGradientColors.length == colors.length)) {
			boolean same = false;
			for (int i = 0; i < selectionGradientColors.length; i++) {
				if (selectionGradientColors[i] == null) {
					same = colors[i] == null;
				} else {
					same = selectionGradientColors[i].equals(colors[i]);
				}
				if (!same) break;
			}
			if (same) {
				for (int i = 0; i < selectionGradientPercents.length; i++) {
					same = selectionGradientPercents[i] == percents[i];
					if (!same) break;
				}
			}
			if (same && this.selectionGradientVertical == vertical) return;
		}
	} else {
		selectionBgImage = null;
	}
	// Store the new settings
	if (colors == null) {
		selectionGradientColors = null;
		selectionGradientPercents = null;
		selectionGradientVertical = false;
		setSelectionBackground((Color)null);
	} else {
		selectionGradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i) {
			selectionGradientColors[i] = colors[i];
		}
		selectionGradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i) {
			selectionGradientPercents[i] = percents[i];
		}
		selectionGradientVertical = vertical;
		setSelectionBackground(selectionGradientColors[selectionGradientColors.length-1]);
	}

	// Refresh with the new settings
	if (selectedIndex > -1) redraw();
}

/**
 * Set the image to be drawn in the background of the selected tab.  Image
 * is stretched or compressed to cover entire selection tab area.
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
	if (image == selectionBgImage) return;
	if (image != null) {
		selectionGradientColors = null;
		selectionGradientPercents = null;
	}
	selectionBgImage = image;
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
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setStyle(int style) {
	checkWidget();
	int mask = SWT.TOP | SWT.BOTTOM | SWT.SINGLE | SWT.MULTI;
	style = style & mask;
	// TOP and BOTTOM are mutually exlusive.
	// TOP is the default
	if ((style & SWT.TOP) != 0) 
		style = style & ~SWT.BOTTOM | SWT.TOP;
	// SINGLE and MULTI are mutually exlusive.
	// MULTI is the default
	if ((style & SWT.MULTI) != 0) 
		style = style & ~SWT.SINGLE | SWT.MULTI;

	boolean changed = false;
	if ((style & SWT.TOP) != 0 || (style & SWT.BOTTOM) != 0) {
		if (onBottom != ((style & SWT.BOTTOM) != 0)) {
			onBottom = (style & SWT.BOTTOM) != 0;
			borderTop = onBottom ? borderLeft : 0;
			borderBottom = onBottom ? 0 : borderRight;
			updateTabHeight(tabHeight, true);
			changed = true;
		}
	}
	if ((style & SWT.SINGLE) != 0 || (style & SWT.MULTI) != 0) {
		if (single != ((style & SWT.SINGLE) != 0)) {
			single = (style & SWT.SINGLE) != 0;
			if (single && selectedIndex == -1 && items.length > 0) {
				setSelection(0, true);
			}
			for (int i = 0; i < items.length; i++) {
				if (i != selectedIndex && items[i].closeImageState == NORMAL) {
					items[i].closeImageState = NONE;
				}
				if (i == selectedIndex && items[i].closeImageState == NONE) {
					items[i].closeImageState = NORMAL;
				}
			}
			changed = true;
		}
	}
	if (changed) {
		Rectangle rectBefore = getClientArea();
		updateItems();
		Rectangle rectAfter = getClientArea();
		if (!rectBefore.equals(rectAfter)) {
			notifyListeners(SWT.Resize, new Event());
		}
		redraw();
	}
}
/**
 * Specify a fixed height for the tab items.  If no height is specified,
 * the default height is the height of the text or the image, whichever 
 * is greater. Specifying a height of -1 will revert to the default height.
 * 
 * @param height the pixel value of the height or -1
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if called with a height of less than 0</li>
 * </ul>
 */
public void setTabHeight(int height) {
	checkWidget();
	if (height < -1) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	fixedTabHeight = height > -1;
	int oldHeight = tabHeight;
	tabHeight = height;
	updateTabHeight(oldHeight, false);
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
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	topRight = control;
	if (updateItems()) redraw();
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
public void showItem (CTabItem item) {
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
		CTabItem tab = items[i];
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
	if (selectedIndex != -1) {
		int top = topTabIndex;
		showItem(items[selectedIndex]);
		if (top != topTabIndex) changed = true;
	}
	return changed;
}
boolean updateTabHeight(int oldHeight, boolean force){
	if (!fixedTabHeight) {
		int tempHeight = 0;
		GC gc = new GC(this);
		for (int i=0; i < items.length; i++) {
			tempHeight = Math.max(tempHeight, items[i].preferredHeight(gc));
		}
		gc.dispose();
		tabHeight =  tempHeight;
	}
	if (!force && tabHeight == oldHeight) return false;
	
	oldSize = null;
	if (onBottom) {
		curve = bezier(0, tabHeight + 2,
		               CURVE_LEFT, tabHeight + 2,
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
		               CURVE_WIDTH - CURVE_RIGHT, tabHeight + 1,
		               CURVE_WIDTH, tabHeight + 1,
		               CURVE_WIDTH);
		// workaround to get rid of blip at end of bezier
//		int index = -1;
//		for (int i = 0; i < curve.length/2; i++) {
//			if (curve[2*i+1] > tabHeight+1) {
//				index = i;
//				break;
//			}
//		}
//		if (index > 0) {
//			int[] newCurve = new int[2*(index-1)];
//			System.arraycopy(curve, 0, newCurve, 0, newCurve.length);
//			curve = newCurve;
//		}
	}
	notifyListeners(SWT.Resize, new Event());
	return true;
}
boolean updateToolTip (int x, int y, Label label) {
	CTabItem item = getItem(new Point (x, y));
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