package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/** 
 * This class emulates a windows TabFolder by using portable
 * graphics and widgets.
 */
public /*final*/ class TabFolder extends Composite {
	TabItem items[];
	int selectedIndex = -1;
	int xClient, yClient;
	int imageHeight = -1;									// all images have the height of the first image ever set
	int topTabIndex = 0;									// index of the first visible tab. Used for tab scrolling
	boolean scrollButtonDown = false;						// true=one of the scroll buttons is being pushed
	boolean inDispose = false;

	// internal constants
	static final int SCROLL_BUTTON_SIZE = 20;				// width/height of the scroll button used for scrolling tab items
	static final int CLIENT_MARGIN_WIDTH = 2; 				// distance between widget border and client rect
	static final int SELECTED_TAB_TOP_EXPANSION = 2; 		// amount we expand the selected tab on top
	static final int SELECTED_TAB_HORIZONTAL_EXPANSION = 2; // amount we expand so it overlays to left and right
/**
 * Construct a TabFolder with the specified parent and style.
 * @param parent org.eclipse.swt.widgets.Composite
 * @param swtStyle int
 */
public TabFolder(Composite parent, int style) {
	super(parent, checkStyle (style));
	Listener listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};
	addListener (SWT.Dispose, listener);
	addListener (SWT.MouseDown, listener);
	addListener (SWT.MouseUp, listener);	
	addListener (SWT.Paint, listener);
	addListener (SWT.Resize, listener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);	
	TypedListener typedListener;

	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
static int checkStyle (int style) {
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
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = CLIENT_MARGIN_WIDTH * 2 + TabItem.SHADOW_WIDTH * 2;
	int height = 0;

	if (items != null && items.length > 0) {
		TabItem lastItem = items[items.length-1];
		width = Math.max (width, lastItem.x + lastItem.width);
	}
	Point size;
	Layout layout = getLayout();
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
}
/**
 * Answer the bounds of the trimmings when the client area is defined by 
 * 'x', 'y', 'width' and 'height'.
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int border = getBorderWidth ();
	int trimX = x - border - CLIENT_MARGIN_WIDTH - TabItem.SHADOW_WIDTH;
	int trimY = y - border - CLIENT_MARGIN_WIDTH - TabItem.SHADOW_WIDTH;
	int tabHeight = 0;
	if (items != null && items.length > 0) {
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
	if (items == null) {
		items = new TabItem[1];
		items[0] = item;
	} else {
		// grow by one and rearrange the array.
		TabItem[] newItems = new TabItem [items.length + 1];
		System.arraycopy(items, 0, newItems, 0, index);
		newItems[index] = item;
		System.arraycopy(items, index, newItems, index + 1, items.length - index);
		items = newItems;
		if (selectedIndex >= index) selectedIndex ++;
	}
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
		setSelectionNotify(0);
	}
}
/**
 * Destroy the specified item.
 */
void destroyChild (TabItem item) {
	int index = indexOf(item);
	if (index == -1) return; 	// should trigger an error?
	if (items.length == 1) {
		items = null;
		selectedIndex = -1;
		topTabIndex = 0;
		if (!inDispose){
			Control control = item.getControl();
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
				Control control = item.getControl();
				if (control != null && !control.isDisposed()) {
					control.setVisible(false);
				}
				selectedIndex = -1;
				setSelectionNotify(Math.max(0, index - 1));
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
void doDispose() {
	inDispose = true;
	// items array is resized during TabItem.dispose
	// it is set to null if the last item is removed
	while (items != null) {						
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
	final Color HighlightShadow = getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	final Color LightShadow = getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

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
	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
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
	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
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
	Color leftTopColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color rightBottomInnerColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
	Color leftTopInnerColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);	
	int upper = yPos;
	int left = xPos;
	int lower = yPos + size - 1;
	int right = xPos + size - 1;

	if (scrollButtonDown) {						// draw the button in the pressed down state?
		rightBottomColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);	
		leftTopColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
		rightBottomInnerColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
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
	if (items == null || tabIndex < 0 || tabIndex >= items.length) return;

	TabItem tabItem = items[tabIndex];
	int tabStopX = tabItem.x + tabItem.width;
	if (isTabScrolling() && tabStopX >= getScrollButtonArea().x && tabIndex != topTabIndex) {
		scrollRight();
	}
}
/**
 * Return the client area of the rectangle (in local coordinates).
 */
public Rectangle getClientArea() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Rectangle clientArea = super.getClientArea();
	
	if (yClient == 0) {					// position not calculated yet
		layoutItems();					// calculate tab folder bounds as soon as there is tab data to use.		
	}
	clientArea.x = xClient;
	clientArea.y = yClient;
	clientArea.width -= xClient + CLIENT_MARGIN_WIDTH + 1;
	clientArea.height -= yClient + CLIENT_MARGIN_WIDTH + 1;
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
 * Return the tab that is located at the specified index.
 */
public TabItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (!(0 <= index && index < getItemCount())) error(SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
 * Return the number of tabs in the folder.
 */
public int getItemCount(){
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (items == null) 
		return 0;
	else return items.length;
}
/**
 * Return the tab items.
 */
public TabItem [] getItems() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (items == null) return new TabItem[0];
	TabItem[] tabItems = new TabItem [items.length];
	System.arraycopy(items, 0, tabItems, 0, items.length);
	return tabItems;
}
/** 
 * Answer the area where the two scroll buttons are drawn.
 */
Rectangle getScrollButtonArea() {
	return new Rectangle(
		super.getClientArea().width - SCROLL_BUTTON_SIZE * 2, SELECTED_TAB_TOP_EXPANSION, 
		SCROLL_BUTTON_SIZE * 2, SCROLL_BUTTON_SIZE);
}
/**
 * Return the selected tab item, or an empty array if there
 * is no selection.
 */
public TabItem [] getSelection() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (selectedIndex == -1) return new TabItem [0];
	return new TabItem [] {items[selectedIndex]};
}
/**
 * Return the index of the selected tab item, or -1 if there
 * is no selection.
 */
public int getSelectionIndex() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return selectedIndex;
}
/**
 * Handle the events that I have hooked on the canvas.
 */
void handleEvents (Event event){
	switch (event.type) {
		case SWT.Dispose:
			doDispose();
			break;
		case SWT.Paint:
			paint(event);
			break;
		case SWT.Resize:
			resize();
			break;
		case SWT.MouseDown:
			mouseDown(event);
			break;
		case SWT.MouseUp:
			mouseUp(event);
			break;
		default:
			break;
	}
}
/**
 * Return the index of the specified tab or -1 if the tab is not 
 * in the receiver.
 */
public int indexOf(TabItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (item == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (items != null) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == item) return i;
		}
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
	
	if (items != null && items.length > 0) {
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
 * Layout the items and store the client area size.
 */
void layoutItems() {
	int x = SELECTED_TAB_HORIZONTAL_EXPANSION;
	int y = SELECTED_TAB_TOP_EXPANSION;
	int tabHeight = 0;
	
	if (items != null) {
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
	}
	xClient = CLIENT_MARGIN_WIDTH;
	yClient = CLIENT_MARGIN_WIDTH + tabHeight;
	TabItem selection[] = getSelection();
	if (selection.length > 0) 
		selection[0].expand(SELECTED_TAB_HORIZONTAL_EXPANSION, SELECTED_TAB_TOP_EXPANSION, SELECTED_TAB_HORIZONTAL_EXPANSION, 0);
}
/** 
 * A mouse button was pressed down. 
 * If one of the tab scroll buttons was hit, scroll in the appropriate 
 * direction.
 * If a tab was hit select the tab.
 */
void mouseDown(Event event) {
	if (items == null) return;
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
				setSelectionNotify(i);
				return;
			}
		}
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
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

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
	if (items != null && items.length > 0 && topTabIndex < items.length - 1) {
		TabItem lastTabItem = items[items.length-1];
		int tabStopX = lastTabItem.x + lastTabItem.width;
		if (tabStopX > super.getClientArea().width - SCROLL_BUTTON_SIZE * 2) {
			topTabIndex++;
			layoutItems();
			redrawTabs();
		}
	}	
}
/**
 * The font is changing. Layout the tab items.
 */
public void setFont(Font font) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (font != null && font.equals(getFont())) return;
	super.setFont(font);	
	layoutItems();
	redrawTabs();
}
/**
 * Set the selection to the tab at the specified index.
 */
public void setSelection(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int oldIndex = selectedIndex;
	
	if (selectedIndex == index || index >= getItemCount()) return;
	if (selectedIndex != -1) {
		Control control = items[selectedIndex].control;
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}		
	}
	if (index < 0) {
		index = -1;										// make sure the index is always -1 if it's negative
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
}
/**
 * Set the selection to the specified items.
 */
public void setSelection(TabItem selectedItems[]) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (selectedItems == null) error(SWT.ERROR_NULL_ARGUMENT);
	int index = -1;
	if (selectedItems.length > 0) {
		index = indexOf(selectedItems[0]);
	}
	setSelection(index);
}
/**
 * Set the selection to the tab at the specified index.
 */
void setSelectionNotify(int index) {
	int oldSelectedIndex = selectedIndex;
	
	setSelection(index);
	if (selectedIndex != oldSelectedIndex && selectedIndex != -1) {
		Event event = new Event();
		event.item = getSelection()[0];
		notifyListeners(SWT.Selection, event);
	}
}
}
