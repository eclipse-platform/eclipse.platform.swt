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
import org.eclipse.swt.graphics.*;
 
/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>TabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
	TabItem items[] = new TabItem [0];
	int selectedIndex = -1;
	int xClient, yClient;
	int imageHeight = -1;									// all images have the height of the first image ever set
	int topTabIndex = 0;									// index of the first visible tab. Used for tab scrolling
	boolean scrollButtonDown = false;						// true=one of the scroll buttons is being pushed
	boolean inDispose = false;
	String toolTipText;

	// internal constants
	static final int SCROLL_BUTTON_SIZE = 20;				// width/height of the scroll button used for scrolling tab items
	static final int CLIENT_MARGIN_WIDTH = 2; 				// distance between widget border and client rect
	static final int SELECTED_TAB_TOP_EXPANSION = 2; 		// amount we expand the selected tab on top
	static final int SELECTED_TAB_HORIZONTAL_EXPANSION = 2; // amount we expand so it overlays to left and right

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
 * @see SWT
 * @see SWT#TOP
 * @see SWT#BOTTOM
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabFolder(Composite parent, int style) {
	super(parent, checkStyle (style));
	Listener listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};
	addListener (SWT.Dispose, listener);
	addListener (SWT.MouseDown, listener);
	addListener (SWT.MouseUp, listener);	
	addListener (SWT.MouseHover, listener);
	addListener (SWT.Paint, listener);
//	addListener (SWT.Resize, listener);
	addListener (SWT.Traverse, listener);
	addListener (SWT.KeyDown, listener);
	addListener (SWT.FocusIn, listener);
	addListener (SWT.FocusOut, listener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	style = checkBits (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	Point size = super.computeSize (wHint, hHint, changed);
	if (items.length > 0) {
		TabItem lastItem = items[items.length-1];
		int border = getBorderWidth ();
		int width = lastItem.x + lastItem.width + border * 2 + CLIENT_MARGIN_WIDTH * 2 + TabItem.SHADOW_WIDTH * 2;
		size.x = Math.max (width, size.x);
	}
	return size;
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int border = getBorderWidth ();
	int trimX = x - border - CLIENT_MARGIN_WIDTH - TabItem.SHADOW_WIDTH;
	int trimY = y - border - CLIENT_MARGIN_WIDTH - TabItem.SHADOW_WIDTH;
	int tabHeight = 0;
	if (items.length > 0) {
		TabItem item = items [0];
		tabHeight = item.y + item.height;	// only use height of the first item because all items should be the same height
	}
	int trimWidth = width + border * 2 + CLIENT_MARGIN_WIDTH * 2 + TabItem.SHADOW_WIDTH * 2;
	int trimHeight = height + tabHeight + border * 2 + CLIENT_MARGIN_WIDTH * 2 + TabItem.SHADOW_WIDTH * 2;
	return new Rectangle (trimX, trimY - tabHeight, trimWidth, trimHeight);
}
/**
 * Create the specified item at 'index'.
 */
void createChild (TabItem item, int index) {
	boolean isTabScrolling = isTabScrolling();
	
	if (!(0 <= index && index <= getItemCount ())) error (SWT.ERROR_INVALID_RANGE);
	item.parent = this;

	// grow by one and rearrange the array.
	TabItem[] newItems = new TabItem [items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index);
	items = newItems;
	if (selectedIndex >= index) selectedIndex ++;

	layoutItems();
	redrawTabs();
	// redraw scroll buttons if they just became visible
	// fixes 1G5X1QL
	if (isTabScrolling() != isTabScrolling && isTabScrolling == false) {
		redrawScrollButtons();
	}
	if (getItemCount() == 1) {
		// select the first added item and send a selection event.
		// fixes 1GAP79N
		setSelection(0, true);
	}
}
/**
 * Destroy the specified item.
 */
void destroyChild (TabItem item) {
	int index = indexOf(item);
	if (index == -1) return; 	// should trigger an error?
	if (items.length == 1) {
		items = new TabItem [0];
		selectedIndex = -1;
		topTabIndex = 0;
		if (!inDispose){
			Control control = item.control;
			if (control != null && !control.isDisposed()) {
				control.setVisible(false);
			}
			redraw();
		}		
	} else {
		// shrink by one and rearrange the array.
		TabItem[] newItems = new TabItem [items.length - 1];
		System.arraycopy(items, 0, newItems, 0, index);
		System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
		items = newItems;

		// move the selection if this item is selected
		if (selectedIndex == index) {
			if (!inDispose) {
				Control control = item.control;
				if (control != null && !control.isDisposed()) {
					control.setVisible(false);
				}
				selectedIndex = -1;
				setSelection(Math.max(0, index - 1), true);
			}
		} else if (selectedIndex > index) {
			selectedIndex--;
		}		
		if (topTabIndex == items.length) {
			--topTabIndex;
		}
	}
	// Make sure that the first tab is visible if scroll buttons are no longer drawn.
	// Fixes 1FXW5DV
	if (topTabIndex > 0 && !isTabScrolling()) {
		topTabIndex = 0;
	}	
	if (!inDispose) {
		layoutItems();
		redrawTabs();
	}
}
/**
 * Dispose the items of the receiver
 */
void doDispose(Event event) {
	if (inDispose) return;
	inDispose = true;
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;
	// items array is resized during TabItem.dispose
	// it is length 0 if the last item is removed
	while (items.length > 0) {						
		if (items[items.length-1] != null) {
			items[items.length-1].dispose();
		}
	}
}
/**
 * Draw an arrow like that used in Button with SWT.ARROW style.
 * @param gc - GC to draw on
 * @param xPos - x position the underlying button is drawn at
 * @param yPos - y position the underlying button is drawn at
 * @param size - size of the underlying button
 * @param left - true=arrow is facing left. false=arrow is facing right
 */
void drawArrow(GC gc, int xPos, int yPos, int size, boolean left) {
	int arrowWidth = size / 4;
	int arrow[] = new int[6];

	if (!left) arrowWidth *= -1;
	// start polygon lines with vertical line which is always the same
	arrow[0] = xPos + (size + arrowWidth) / 2;	
	arrow[1] = yPos + size / 4;
	arrow[2] = arrow[0];
	arrow[3] = arrow[1] + size / 2;

	arrow[4] = arrow[0] - arrowWidth;
	arrow[5] = yPos + size / 2;			

	gc.setBackground(getForeground());
	gc.fillPolygon(arrow);
	gc.setBackground(getBackground());
}
/** 
 * Draw a border around the receiver.
 */
void drawBorder(Event event) {
	GC gc = event.gc;
	Rectangle clientArea = getClientArea();
	int wClient = clientArea.width;
	int hClient = clientArea.height;
	int x, y, x1, y1;
	final Color HighlightShadow = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	final Color LightShadow = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

	// Draw the left line
	gc.setForeground(HighlightShadow);
	gc.drawLine((x = xClient - CLIENT_MARGIN_WIDTH), 
		yClient + hClient + CLIENT_MARGIN_WIDTH,
		x, 
		(y = yClient - CLIENT_MARGIN_WIDTH) + 1);
	// Second, darker, line right of the previous line. 
	// Necessary to workaround color constant differences on Windows/Motif
	gc.setForeground(LightShadow);
	gc.drawLine(x + 1, yClient + hClient + CLIENT_MARGIN_WIDTH, x + 1, y + 1);
	gc.setForeground(HighlightShadow);
		
	// Draw the upper line in two chunks so we don't overwrite the selected tab
	if (selectedIndex == -1) {
		gc.setForeground(LightShadow);
		gc.drawLine(x + 1, y + 1, xClient + wClient + CLIENT_MARGIN_WIDTH, y + 1);		
	} else {
		TabItem item = items[selectedIndex];
		gc.setForeground(LightShadow);
		if (selectedIndex > 0) {
			gc.drawLine(x + 1, y + 1, item.x - 1 + CLIENT_MARGIN_WIDTH, y + 1);
		}
		gc.drawLine(item.x + item.width, y + 1, xClient + wClient + CLIENT_MARGIN_WIDTH, y + 1);
	}

	// Draw the right and bottom black lines
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
	gc.drawLine((x = xClient - CLIENT_MARGIN_WIDTH),
		(y = yClient + hClient + CLIENT_MARGIN_WIDTH),
		(x1 = xClient + wClient + CLIENT_MARGIN_WIDTH),
		y);
	gc.drawLine(x1, y, x1, (y1 = yClient - CLIENT_MARGIN_WIDTH + 1));
	x1--;
	x++;
	y--;
	y1++;


	// There is a dark gray line above the bottom back line
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine(x, y, x1, y);
	// On the right there is a dark gray line, left of the black one
	gc.drawLine(x1, y-1, x1, y1);

	// restore the foreground color.
	gc.setForeground(getForeground());
}
/**
 * Draw a plain push button
 * @param gc - GC to draw on
 * @param xPos - x position the button is drawn at
 * @param yPos - y position the button is drawn at
 * @param size - size of the button
 */
void drawPlainButton(GC gc, int xPos, int yPos, int size) {
	Color rightBottomColor = getForeground();
	Color leftTopColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color rightBottomInnerColor = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
	Color leftTopInnerColor = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);	
	int upper = yPos;
	int left = xPos;
	int lower = yPos + size - 1;
	int right = xPos + size - 1;

	if (scrollButtonDown) {						// draw the button in the pressed down state?
		rightBottomColor = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);	
		leftTopColor = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
		rightBottomInnerColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		leftTopInnerColor = getForeground();
	}		
	gc.fillRectangle(left, upper, right - left, lower - upper);
	// draw right, bottom line in foreground color
	gc.setForeground(rightBottomColor);	
	gc.drawLine(right, upper, right, lower);
	gc.drawLine(left, lower, right, lower);	

	// draw left, top line in normal shadow (default light gray)
	gc.setForeground(leftTopColor);
	gc.drawLine(left, upper, left, lower - 1);
	gc.drawLine(left, upper, right - 1, upper);	

	upper++;
	left++;
	lower--;
	right--;
	// draw right, bottom line in dark shadow (default dark gray)
	gc.setForeground(rightBottomInnerColor);
	gc.drawLine(right, upper, right, lower);
	gc.drawLine(left, lower, right, lower);	

	// draw left, top line in high light shadow (default off white)
	gc.setForeground(leftTopInnerColor);
	gc.drawLine(left, upper, left, lower - 1);
	gc.drawLine(left, upper, right - 1, upper);	
	gc.setForeground(getForeground());
}
/** 
 * Draw the buttons used to scroll tab items
 */
void drawScrollButtons(Event event) {
	Rectangle buttonArea = getScrollButtonArea();
	int buttonSize = buttonArea.width / 2;

	drawPlainButton(event.gc, buttonArea.x, buttonArea.y, buttonSize);
	drawPlainButton(event.gc, buttonArea.x + buttonSize, buttonArea.y, buttonSize);
	if (scrollButtonDown) {
		drawArrow(event.gc, buttonArea.x, buttonArea.y, buttonSize, true);	
		drawArrow(event.gc, buttonArea.x + buttonSize + 1, buttonArea.y, buttonSize + 1, false);
	}
	else {
		drawArrow(event.gc, buttonArea.x - 1, buttonArea.y - 1, buttonSize, true);	
		drawArrow(event.gc, buttonArea.x + buttonSize, buttonArea.y - 1, buttonSize, false);
	}
}

/**
 * Make sure that the first tab is visible if scroll buttons are no 
 * longer drawn.
 */
void ensureRightFreeSpaceUsed() {
	if (topTabIndex > 0 && !isTabScrolling()) {
		topTabIndex = 0;
		layoutItems();
		redrawTabs();
	}	
}

/**
 * If the tab at 'tabIndex' is not visible or partially covered by the tab 
 * scroll buttons and there is enough space to completely show the tab, 
 * the tab is scrolled to the left to make it fully visible.
 */
void ensureVisible(int tabIndex) {
	if (tabIndex < 0 || tabIndex >= items.length) return;
	if (!isTabScrolling()) return;
	if (tabIndex < topTabIndex) {
		topTabIndex = tabIndex;
		layoutItems();
		redrawTabs();
		return;
	}
	int rightEdge = getScrollButtonArea().x;
	TabItem tabItem = items[tabIndex];
	while (tabItem.x + tabItem.width > rightEdge && tabIndex != topTabIndex) {
		topTabIndex++;
		layoutItems();
		redrawTabs();
	}
}
void focus (Event e) {
	if (selectedIndex == -1) return;
	TabItem tab = items[selectedIndex];
	redraw(tab.x, tab.y, tab.width, tab.height);
}

public Rectangle getClientArea() {
	checkWidget();
	Rectangle clientArea = super.getClientArea();
	
	if (yClient == 0) {					// position not calculated yet
		layoutItems();					// calculate tab folder bounds as soon as there is tab data to use.		
	}
	clientArea.x = xClient;
	clientArea.y = yClient;
	clientArea.width = Math.max (0, clientArea.width - (xClient + CLIENT_MARGIN_WIDTH + 1));
	clientArea.height = Math.max (0, clientArea.height - (yClient + CLIENT_MARGIN_WIDTH + 1));
	return clientArea;
}
/**
 * Return the height of item images. All images are scaled to 
 * the height of the first image.
 */
int getImageHeight() {
	return imageHeight;
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
 */
public TabItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < getItemCount())) error(SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
 * Returns the tab item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the tab item at the given point, or null if the point is not in a tab item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public TabItem getItem (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = items.length;
	for (int index = 0; index < count; index++) {
		TabItem item = items[index];
		Rectangle bounds = item.getBounds();
		if (bounds.contains(point)) return item;
	}
	return null;
}
/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount(){
	checkWidget();
	return items.length;
}
/**
 * Returns an array of <code>TabItem</code>s which are the items
 * in the receiver. 
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
 */
public TabItem [] getItems() {
	checkWidget();
	TabItem[] tabItems = new TabItem [items.length];
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
 * Returns the area where the two scroll buttons are drawn.
 */
Rectangle getScrollButtonArea() {
	return new Rectangle(
		super.getClientArea().width - SCROLL_BUTTON_SIZE * 2, SELECTED_TAB_TOP_EXPANSION, 
		SCROLL_BUTTON_SIZE * 2, SCROLL_BUTTON_SIZE);
}
/**
 * Returns an array of <code>TabItem</code>s that are currently
 * selected in the receiver. An empty array indicates that no
 * items are selected.
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
public TabItem [] getSelection() {
	checkWidget();
	if (selectedIndex == -1) return new TabItem [0];
	return new TabItem [] {items[selectedIndex]};
}
/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex() {
	checkWidget();
	return selectedIndex;
}
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}
/**
 * Handle the events that I have hooked on the canvas.
 */
void handleEvents (Event event){
	switch (event.type) {
		case SWT.Dispose:
			doDispose(event);
			break;
		case SWT.Paint:
			paint(event);
			break;
//		case SWT.Resize:
//			resize();
//			break;
		case SWT.MouseDown:
			mouseDown(event);
			break;
		case SWT.MouseUp:
			mouseUp(event);
			break;
		case SWT.MouseHover:
			mouseHover(event);
			break;
		case SWT.Traverse:
			traversal(event); 
			break;
		case SWT.FocusIn:
		case SWT.FocusOut:
			focus(event);
			break;
		case SWT.KeyDown:
			// this callback is always needed so that widget is included in tab order
			keyDown(event);
			break;
		default:
			break;
	}
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
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf(TabItem item) {
	checkWidget();
	if (item == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}
/** 
 * Answer true when the left scroll button was clicked with mouse button 1.
 */
boolean isLeftButtonHit(Event event) {
	Rectangle buttonArea = getScrollButtonArea();

	buttonArea.width /= 2;
	return isTabScrolling() && event.button == 1 && buttonArea.contains(event.x, event.y);
}
/** 
 * Answer true when the right scroll button was clicked with mouse button 1.
 */
boolean isRightButtonHit(Event event) {
	Rectangle buttonArea = getScrollButtonArea();
	int buttonSize = buttonArea.width / 2;
	
	buttonArea.x += buttonSize;
	buttonArea.width = buttonSize;
	return isTabScrolling() && event.button == 1 && buttonArea.contains(event.x, event.y);
}
/**
 * Answer true if not all tabs can be visible in the receive
 * thus requiring the scroll buttons to be visible.
 */
boolean isTabScrolling() {
	boolean isVisible = false;
	
	if (items.length > 0) {
		TabItem tabItem = items[items.length-1];
		int tabStopX = tabItem.x + tabItem.width;
		tabItem = items[0];
		if (tabStopX - tabItem.x > super.getClientArea().width) {
			isVisible = true;									// not all tabs fit in the client area
		}
	}
	return isVisible;
}
/**
 * 'item' has changed. Store the image size if this is the 
 * first item with an image.
 */
void itemChanged(TabItem item) {
	Image itemImage = item.getImage();
	boolean isTabScrolling = isTabScrolling();
	
	if (imageHeight == -1 && itemImage != null) {
		imageHeight = itemImage.getBounds().height;
	}
	layoutItems();
	redrawTabs();
	// redraw scroll buttons if they just became visible
	// fixes 1G5X1QL
	if (isTabScrolling() != isTabScrolling && isTabScrolling == false) {
		redrawScrollButtons();
	}	
}
/** 
 * A key was pressed.  If one of the tab-selection keys that is not a traversal
 * was pressed then change tabs accordingly.
 */
void keyDown(Event event) {
	int count = items.length;
	if (count <= 1) return;
	switch (event.keyCode) {
		case SWT.ARROW_RIGHT:
			if (selectedIndex < items.length - 1) {
				setSelection(selectedIndex + 1, true);
			}
			break;
		case SWT.ARROW_LEFT:
			if (selectedIndex > 0) {
				setSelection(selectedIndex - 1, true);
			}
			break;
		case SWT.HOME:
			if (selectedIndex > 0) {
				setSelection(0, true);
			}
			break;
		case SWT.END:
			if (selectedIndex < count - 1) {
				setSelection(count - 1, true);
			}
			break;
	}
}
/**
 * Layout the items and store the client area size.
 */
void layoutItems() {
	int x = SELECTED_TAB_HORIZONTAL_EXPANSION;
	int y = SELECTED_TAB_TOP_EXPANSION;
	int tabHeight = 0;
	
	GC gc = new GC(this);
	for (int i=topTabIndex - 1; i>=0; i--) {			// if the first visible tab is not the first tab
		TabItem tab = items[i];
		tab.width = tab.preferredWidth(gc);
		tab.height = tab.preferredHeight(gc);
		x -= tab.width;									// layout tab items from right to left thus making them invisible
		tab.x = x;
		tab.y = y;
		if (tab.height > tabHeight) tabHeight = tab.height;
	}
	x = SELECTED_TAB_HORIZONTAL_EXPANSION;
	for (int i=topTabIndex; i<items.length; i++) {		// continue laying out remaining, visible items left to right 
		TabItem tab = items[i];
		tab.x = x;
		tab.y = y;
		tab.width = tab.preferredWidth(gc);
		tab.height = tab.preferredHeight(gc);
		x = x + tab.width;
		if (tab.height > tabHeight) tabHeight = tab.height;
	}
	gc.dispose();
	xClient = CLIENT_MARGIN_WIDTH;
	yClient = CLIENT_MARGIN_WIDTH + tabHeight;
	TabItem selection[] = getSelection();
	if (selection.length > 0) 
		selection[0].expand(SELECTED_TAB_HORIZONTAL_EXPANSION, SELECTED_TAB_TOP_EXPANSION, SELECTED_TAB_HORIZONTAL_EXPANSION, 0);
}
Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;
		while (index < items.length) {
			if (items [index].control == child) break;
			index++;
		}
		if (index == items.length) {
			Rectangle rect = child.getBounds ();
			width = Math.max (width, rect.x + rect.width);
			height = Math.max (height, rect.y + rect.height);
		} else {
			Point size = child.computeSize (wHint, hHint, flushCache);
			width = Math.max (width, size.x);
			height = Math.max (height, size.y);
		}
	}
	return new Point (width, height);
}
boolean mnemonicHit (char key) {
	for (int i = 0; i < items.length; i++) {
		char mnemonic = getMnemonic (items[i].getText ());
		if (mnemonic != '\0') {
			if (Character.toUpperCase (key) == Character.toUpperCase (mnemonic)) {
				if (forceFocus ()) {
					if (i != selectedIndex) setSelection(i, true);
					return true;
				}
			}
		}
	}
	return false;
}
/** 
 * A mouse button was pressed down. 
 * If one of the tab scroll buttons was hit, scroll in the appropriate 
 * direction.
 * If a tab was hit select the tab.
 */
void mouseDown(Event event) {
	if (isLeftButtonHit(event)) {
		scrollButtonDown = true;
		redrawHitButton(event);
		scrollLeft();
	}
	else
	if (isRightButtonHit(event)) {
		scrollButtonDown = true;
		redrawHitButton(event);
		scrollRight();
	}
	else {
		for (int i=0; i<items.length; i++) {
			if (items[i].getBounds().contains(new Point(event.x, event.y))) {
				forceFocus();
				setSelection(i, true);
				return;
			}
		}
	}
}
void mouseHover(Event event) {
	String current = super.getToolTipText();
	if (toolTipText == null) {
		Point point = new Point(event.x, event.y);
		for (int i=0; i<items.length; i++) {
			if (items[i].getBounds().contains(point)) {
				String string = items[i].getToolTipText();
				if (string != null && !string.equals(current)) {
					super.setToolTipText(string);
				}
				return;
			}
		}
		if (current != null) super.setToolTipText(null);
		return;
	}
	if (!toolTipText.equals(current)) {
		super.setToolTipText(toolTipText);
	}
}
/** 
 * A mouse button was released.
 */
void mouseUp(Event event) {
	if (scrollButtonDown && event.button == 1) {
		scrollButtonDown = false;
		redrawHitButton(event);
	}
}
/** 
 * Paint the receiver.
 */
void paint(Event event) {
	// Draw the unselected tabs first.
	for (int i=0; i<getItemCount(); i++) {
		if (i != selectedIndex && event.getBounds().intersects(items[i].getBounds())) {
			items[i].paint(event.gc, false);
		}
	}
	drawBorder(event);
	// Selected tab comes last since selected tabs overlay adjacent tabs 
	// and the border
	if (selectedIndex != -1) {
		items[selectedIndex].paint(event.gc, true);
	}
	if (isTabScrolling()) drawScrollButtons(event);
}
/**
 * Redraw the area of the receiver specified by x, y, width, height.
 * Don't redraw the scroll buttons to avoid flashing.
 */
void redraw (int x, int y, int width, int height) {
	Rectangle buttonArea = getScrollButtonArea();	
	boolean fixScrollButtons = false;

	if (isTabScrolling()) {		
		if (x >	buttonArea.x) {
			x = buttonArea.x;
			fixScrollButtons = true;
		}
		if (x + width >	buttonArea.x) {
			width = buttonArea.x - x;
			fixScrollButtons = true;
		}
	}
	redraw(x, y, width, height, false);
	if (fixScrollButtons) {
		redraw(buttonArea.x, 0, buttonArea.width, buttonArea.y, false);		// redraw space above scroll buttons
		if (buttonArea.height < getClientArea().y) {
			int redrawY = buttonArea.y + buttonArea.height;
			redraw(
				buttonArea.x, redrawY, 
				buttonArea.width, getClientArea().y - redrawY, false);		// redraw space below scroll buttons
		}
	}
}
/** 
 * Redraw the scroll button that was pressed down
 */
void redrawHitButton(Event event) {
	Rectangle scrollButtonArea = getScrollButtonArea();
	int scrollButtonWidth = scrollButtonArea.width / 2;
	
	if (isLeftButtonHit(event)) {
		redraw(
			scrollButtonArea.x, scrollButtonArea.y, 
			scrollButtonWidth, scrollButtonArea.height, false);
	}
	else
	if (isRightButtonHit(event)) {
		redraw(
			scrollButtonArea.x + scrollButtonWidth, scrollButtonArea.y, 
			scrollButtonWidth, scrollButtonArea.height, false);		
	}
}
/** 
 * Redraw both scroll buttons
 */
void redrawScrollButtons() {
	Rectangle scrollButtonArea = getScrollButtonArea();
	
	redraw(
		scrollButtonArea.x, scrollButtonArea.y, 
		scrollButtonArea.width, scrollButtonArea.height, false);
}
/** 
 * Redraw the tabs at the specified indexes.
 */
void redrawSelectionChange(int oldSelection, int newSelection) {
	if (oldSelection != -1) {
		TabItem tab = items[oldSelection];
		// since the tab used to be selected, we need to clear its old expanded size
		redraw(tab.x - SELECTED_TAB_HORIZONTAL_EXPANSION, 
				tab.y - SELECTED_TAB_TOP_EXPANSION, 
				tab.width + 2 * SELECTED_TAB_HORIZONTAL_EXPANSION, 
				tab.height + SELECTED_TAB_TOP_EXPANSION);
	}
	if (newSelection != -1) {
		TabItem tab = items[newSelection];
		// this tab is already at the expanded size
		redraw(tab.x, tab.y, tab.width, tab.height);
	}
	// make sure the tab is repainted before the new page is made visible.
	// The latter could take a long time and delay the screen update.
	update();														
}
/**
 * Redraw the whole tab area
 */
void redrawTabs() {
	redraw(0, 0, super.getClientArea().width, getClientArea().y);
}
void removeControl (Control control) {
	super.removeControl (control);
	for (int i=0; i<items.length; i++) {
		TabItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);	
}
/**
 * The widget was resized. Adjust the size of the currently selected page.
 */
void resize() {
	if (selectedIndex != -1) {
		Control control = items[selectedIndex].getControl();
		if (control != null && !control.isDisposed()) {
			control.setBounds(getClientArea());
		}
	}
	ensureRightFreeSpaceUsed();
}

void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TabItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

/**
 * Scroll the tab items to the left.
 */
void scrollLeft() {
	if (topTabIndex > 0) {
		--topTabIndex;
		layoutItems();
		redrawTabs();
	}
}
/**
 * Scroll the tab items to the right.
 */
void scrollRight() {
	if (items.length > 0 && topTabIndex < items.length - 1) {
		TabItem lastTabItem = items[items.length-1];
		int tabStopX = lastTabItem.x + lastTabItem.width;
		if (tabStopX > super.getClientArea().width - SCROLL_BUTTON_SIZE * 2) {
			topTabIndex++;
			layoutItems();
			redrawTabs();
		}
	}	
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) resize ();
	return changed;
}
public void setFont(Font font) {
	checkWidget();
	if (font != null && font.equals(getFont())) return;
	super.setFont(font);	
	layoutItems();
	redrawTabs();
}
/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new items are
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection(int index) {
	checkWidget();
	if (!(0 <= index && index < items.length)) return;
	setSelection(index, false);
}
/**
 * Sets the receiver's selection to the given item.
 * The current selected is first cleared, then the new item is
 * selected.
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSelection(TabItem item) {
	checkWidget();
	if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
	setSelection(new TabItem[]{item});
}
/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection(TabItem selectedItems[]) {
	checkWidget();
	if (selectedItems == null) error(SWT.ERROR_NULL_ARGUMENT);
	int index = -1;
	if (selectedItems.length > 0) {
		index = indexOf(selectedItems[0]);
	}
	setSelection(index, false);
}
/**
 * Set the selection to the tab at the specified index.
 */
void setSelection(int index, boolean notify) {
	if (selectedIndex == index) return;
	int oldIndex = selectedIndex;
	
	if (selectedIndex == index || index >= getItemCount()) return;
	if (selectedIndex != -1) {
		Control control = items[selectedIndex].control;
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}		
	}
	if (index < 0) {
		index = -1; // make sure the index is always -1 if it's negative
	}
	selectedIndex = index;
	layoutItems();
	ensureVisible(index);	
	redrawSelectionChange(oldIndex, index);
	if (index >= 0) {
		Control control = items[index].control;
		if (control != null && !control.isDisposed()) {
			control.setBounds(getClientArea());
			control.setVisible(true);
		}
	}
	
	if (notify) {
		if (selectedIndex != oldIndex && selectedIndex != -1) {
			Event event = new Event();
			event.item = getSelection()[0];
			notifyListeners(SWT.Selection, event);
		}
	}
}
public void setToolTipText (String string) {
	checkWidget();
	super.setToolTipText (string);
	toolTipText = string;
}
void traversal(Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
		case SWT.TRAVERSE_MNEMONIC:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			event.doit = true;
	}
}
boolean traverseItem (boolean next) {
	return false;
}
boolean traversePage (boolean next) {
	int count = items.length;
	if (count == 0) return false;
	int index = selectedIndex;
	if (index == -1) {
		index = 0;
	} else {
		int offset = next ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	return true;
}
}
