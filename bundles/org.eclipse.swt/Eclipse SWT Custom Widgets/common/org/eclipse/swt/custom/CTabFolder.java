package org.eclipse.swt.custom;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

 
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
	
	/*
	 * A multiple of the tab height that specifies the minimum width to which a tab 
	 * will be compressed before scrolling arrows are used to navigate the tabs.
	 */
	public static int MIN_TAB_WIDTH = 3;
	
	/* sizing, positioning */
	int xClient, yClient;
	boolean onBottom = false;
	int fixedTabHeight = -1;
	int imageHeight = -1;
	
	/* item management */
	private CTabItem items[] = new CTabItem[0];
	private int selectedIndex = -1;
	int topTabIndex = -1; // index of the left most visible tab.

	/* External Listener management */
	private CTabFolderListener[] tabListeners = new CTabFolderListener[0];
	
	/* Color appearance */
	Image backgroundImage;
	private Image gradientImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	Color selectionForeground;

	// internal constants
	private static final int DEFAULT_WIDTH = 64;
	private static final int DEFAULT_HEIGHT = 64;
	
	// scrolling arrows
	private ToolBar scrollBar;
	private ToolItem scrollLeft;
	private ToolItem scrollRight;
	private Image arrowLeftImage;
	private Image arrowRightImage;
	private Image arrowLeftDisabledImage;
	private Image arrowRightDisabledImage;
	
	// close button
	boolean showClose = false;
	ToolBar closeBar;
	private ToolItem closeItem;
	private Image closeImage;
	private ToolBar inactiveCloseBar;
	private ToolItem inactiveCloseItem;
	private Image inactiveCloseImage;
	private CTabItem inactiveItem;	

	private boolean shortenedTabs = false;
	
	// borders
	boolean showBorders = false;
	private int BORDER_BOTTOM = 0;
	private int BORDER_LEFT = 0;
	private int BORDER_RIGHT = 0;
	private int BORDER_TOP = 0;
	private Color borderColor1;
	private Color borderColor2;
	private Color borderColor3;

	// when disposing CTabFolder, don't try to layout the items or 
	// change the selection as each child is destroyed.
	private boolean inDispose = false;

	// keep track of size changes in order to redraw only affected area
	// on Resize
	private Rectangle oldArea;
	
	// insertion marker
	int insertionIndex = -2; // Index of insert marker.  Marker always shown after index.
	                         // -2 means no insert marker
	                         
	// tool tip
	private Shell tip;

/**
 * Construct a CTabFolder with the specified parent and style.
 * @param parent org.eclipse.swt.widgets.Composite
 * @param swtStyle int
 */
public CTabFolder(Composite parent, int style) {
	super(parent, checkStyle (style));
	
	onBottom = (getStyle() & SWT.BOTTOM) != 0;
	
	borderColor1 = new Color(getDisplay(), borderInsideRGB);
	borderColor2 = new Color(getDisplay(), borderMiddleRGB);
	borderColor3 = new Color(getDisplay(), borderOutsideRGB);
	Color foreground = getForeground();
	Color background = getBackground();
	
	// create scrolling arrow buttons
	scrollBar = new ToolBar(this, SWT.FLAT);
	scrollBar.setVisible(false);
	scrollLeft = new ToolItem(scrollBar, SWT.PUSH);
	scrollLeft.setEnabled(false);
	scrollRight = new ToolItem(scrollBar, SWT.PUSH);
	scrollRight.setEnabled(false);
	initScrollButtons(foreground, background);
	
	// create close buttons
	closeBar = new ToolBar(this, SWT.FLAT);
	closeBar.setVisible(false);
	closeItem = new ToolItem(closeBar, SWT.PUSH);
	initCloseButton(foreground, background);
	
	inactiveCloseBar = new ToolBar(this, SWT.FLAT);
	inactiveCloseBar.setVisible(false);
	inactiveCloseItem = new ToolItem(inactiveCloseBar, SWT.PUSH);
	initInactiveCloseButton(foreground, background);

	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			handleEvents(event);
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.MouseUp, listener);
	addListener(SWT.MouseMove, listener);
	addListener(SWT.MouseExit, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Resize, listener);
	scrollLeft.addListener(SWT.Selection, listener);
	scrollRight.addListener(SWT.Selection, listener);
	closeItem.addListener(SWT.Selection, listener);
	inactiveCloseItem.addListener(SWT.Selection, listener);
	inactiveCloseBar.addListener (SWT.MouseExit, listener);
	
	setBorderVisible((style & SWT.BORDER) != 0);
	
	// tool tip support
	Display display = getDisplay();
	tip = new Shell (getShell(), SWT.NONE);
	tip.setLayout(new FillLayout());
	Label label = new Label (tip, SWT.NONE);
	label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
	label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
	addMouseTrackListener (new MouseTrackAdapter () {
		public void mouseExit(MouseEvent e) {
			if (tip.isVisible()) tip.setVisible(false);
		}
		public void mouseHover(MouseEvent e) {
			Point pt = new Point (e.x, e.y);
			CTabItem item = getItem(pt);
			if (item != null) {
				String tooltip = item.getToolTipText();
				if (tooltip != null) {
					pt.y = (onBottom) ? pt.y - 26 : pt.y + 26;
					pt = toDisplay(pt);
					tip.setLocation(pt);
					Label label = (Label) (tip.getChildren() [0]);
					label.setText(tooltip);  
					tip.pack();
					tip.setVisible(true);
					return;
				}
			}

			tip.setVisible(false);
		}
	});
	
}
private static int checkStyle (int style) {
	int mask = SWT.TOP | SWT.BOTTOM | SWT.FLAT;
	style = style & mask;
	// TOP and BOTTOM are mutually exlusive.
	// TOP is the default
	if ((style & SWT.TOP) != 0) 
		style = style & ~(SWT.TOP | SWT.BOTTOM) | SWT.TOP;
	// reduce the flash by not redrawing the entire area on a Resize event
	style |= SWT.NO_REDRAW_RESIZE;
	return style;
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void addSelectionListener(SelectionListener listener) {
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError <ul>
* 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
*	</ul>
*/
public void addCTabFolderListener(CTabFolderListener listener) {
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolderListener[] newTabListeners = new CTabFolderListener[tabListeners.length + 1];
	System.arraycopy(tabListeners, 0, newTabListeners, 0, tabListeners.length);
	tabListeners = newTabListeners;
	tabListeners[tabListeners.length - 1] = listener;
	showClose = true;
}
private void closeNotify(CTabItem item, int time) {
	CTabFolderEvent event = new CTabFolderEvent(this);
	event.widget = this;
	event.time = time;
	event.item = item;
	event.doit = true;
	if (tabListeners != null) {
		for (int i = 0; i < tabListeners.length; i++) {
			tabListeners[i].itemClosed(event);
		}
	}
	if (event.doit) {
		item.dispose();
	}
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	int minWidth = 0;
	int minHeight = 0;

	// tab width
	if (items.length > 0) {
		CTabItem lastItem = items[items.length-1];
		minWidth = lastItem.x + lastItem.width;
	}

	// get max preferred size of items
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

	if (wHint != SWT.DEFAULT) minWidth  = wHint;
	if (hHint != SWT.DEFAULT) minHeight = hHint;

	Rectangle trim = computeTrim(0, 0, minWidth, minHeight);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	int tabHeight = getTabHeight();
	int trimX = x - marginWidth - BORDER_LEFT;
	int trimY = y - marginHeight - tabHeight - BORDER_TOP;
	if (onBottom) {
		trimY = y - marginHeight - BORDER_TOP;
	}
	int trimWidth = width + BORDER_LEFT + BORDER_RIGHT + 2*marginWidth;
	int trimHeight = height + BORDER_TOP + BORDER_BOTTOM + 2*marginHeight + tabHeight;
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}
/**
 * Create the specified item at 'index'.
 */
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
	}

	layoutItems();
	if (items.length == 1) {
		redraw();
	} else {
		redrawTabArea(-1);
	}
}
/**
 * Destroy the specified item.
 */
void destroyItem (CTabItem item) {
	if (inDispose) return;
	
	int index = indexOf(item);
	if (index == -1) return; 	// should this trigger an error?
	
	insertionIndex = -2;
	
	if (items.length == 1) {
		items = new CTabItem[0];
		selectedIndex = -1;
		topTabIndex = 0;
		
		Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
		closeBar.setVisible(false);
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
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
		selectedIndex = -1;
		setSelectionNotify(Math.max(0, index - 1));
	} else if (selectedIndex > index) {
		selectedIndex --;
	}
	
	layoutItems();
	redrawTabArea(-1);
}
/**
 * Dispose the items of the receiver
 */
private void onDispose() {
	inDispose = true;
	
	// items array is resized during CTabItem.dispose
	// it is set to null if the last item is removed
	int length = items.length;
	for (int i = 0; i < length; i++) {						
		if (items[i] != null) {
			items[i].dispose();
		}
	}
	
	// clean up resources
	if (tip != null  && !tip.isDisposed()) {
		tip.dispose();
		tip = null;
	}
	
	if (gradientImage != null){
		gradientImage.dispose();
		gradientImage = null;
	}
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;

	if (arrowLeftImage != null){
		arrowLeftImage.dispose();
		arrowLeftImage = null;
	}
	
	if (arrowRightImage != null){
		arrowRightImage.dispose();
		arrowRightImage = null;
	}
	
	if (arrowLeftDisabledImage != null) {
		arrowLeftDisabledImage.dispose();
		arrowLeftDisabledImage = null;
	}
	
	if (arrowRightDisabledImage != null) {
		arrowRightDisabledImage.dispose();
		arrowRightDisabledImage = null;
	}
	
	if (closeImage != null) {
		closeImage.dispose();
		closeImage = null;
	}
	
	if (inactiveCloseImage != null) {
		inactiveCloseImage.dispose();
		inactiveCloseImage = null;
	}
	
	if (borderColor1 != null) {
		borderColor1.dispose();
		borderColor1 = null;
	}
	
	if (borderColor2 != null) {
		borderColor2.dispose();
		borderColor2 = null;
	}
	
	if (borderColor3 != null) {
		borderColor3.dispose();
		borderColor3 = null;
	}
}
/** 
 * Draw a border around the receiver.
 */
private void drawBorder(GC gc) {
	
	Rectangle d = super.getClientArea();
	
	if (showBorders) {
		if ((getStyle() & SWT.FLAT) != 0) {
			gc.setForeground(borderColor1);
			gc.drawRectangle(d.x, d.y, d.x + d.width - 1, d.y + d.height - 1);
		} else {
			gc.setForeground(borderColor1);
			gc.drawRectangle(d.x, d.y, d.x + d.width - 3, d.y + d.height - 3);
		
			gc.setForeground(borderColor2);
			gc.drawLine(d.x + 1,           d.y + d.height - 2, d.x + d.width - 1, d.y + d.height - 2);
			gc.drawLine(d.x + d.width - 2, d.y + 1,            d.x + d.width - 2, d.y + d.height - 1);
		
			gc.setForeground(borderColor3);
			gc.drawLine(d.x + 2,           d.y + d.height - 1, d.x + d.width - 2, d.y + d.height - 1);
			gc.drawLine(d.x + d.width - 1, d.y + 2,            d.x + d.width - 1, d.y + d.height - 2);
		
			// fill in corners with parent's background
			gc.setForeground(getParent().getBackground());
			gc.drawLine(d.x + d.width - 2, d.y,     d.x + d.width - 1, d.y);
			gc.drawLine(d.x + d.width - 1, d.y + 1, d.x + d.width - 1, d.y + 1);
		
			gc.drawLine(d.x, d.y + d.height - 2, d.x,     d.y + d.height - 2);
			gc.drawLine(d.x, d.y + d.height - 1, d.x + 1, d.y + d.height - 1);
		
			gc.drawLine(d.x + d.width - 1, d.y + d.height - 1, d.x + d.width - 1, d.y + d.height - 1);
		}
		
	}

	// draw a separator line
	if (items.length > 0) {	
		int tabHeight = getTabHeight();
		int lineY = d.y + BORDER_TOP + tabHeight;
		if (onBottom) {
			lineY = d.y + d.height - BORDER_BOTTOM - tabHeight - 1;
		}
		gc.setForeground(borderColor1);
		gc.drawLine(d.x + BORDER_LEFT, lineY, d.x + d.width - BORDER_RIGHT, lineY);
	}

	gc.setForeground(getForeground());
}
public Rectangle getClientArea() {
	Rectangle clientArea = super.getClientArea();
	clientArea.x = xClient;
	clientArea.y = yClient;
	clientArea.width -= 2*marginWidth + BORDER_LEFT + BORDER_RIGHT;
	clientArea.height -= 2*marginHeight + BORDER_TOP + BORDER_BOTTOM + getTabHeight() + 1;
	return clientArea;
}
/**
 * Return the height of item images. All images are scaled to 
 * the height of the first image.
 */
int getImageHeight() {
	return imageHeight;
}
public int getTabHeight(){
	if (fixedTabHeight > 0) return fixedTabHeight;
	
	if (isDisposed()) return 0;
	
	int tempHeight = 0;
	GC gc = new GC(this);
	for (int i=0; i < items.length; i++) { 
		int height = items[i].preferredHeight(gc);
		tempHeight = Math.max(tempHeight, height);
	}
	gc.dispose();
	return tempHeight;
}
/**
 * Return the tab that is located at the specified index.
 */
public CTabItem getItem (int index) {
	if (index  < 0 || index > items.length) 
		SWT.error(SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
* Gets the item at a point in the widget.
* <p>
*
* @return the item at a point
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_CANNOT_GET_COUNT when the operation fails</li>
*	</ul>
*/
public CTabItem getItem (Point pt) {
	for (int i = 0; i < items.length; i++) {
		Rectangle bounds = items[i].getBounds();
		if (bounds.contains(pt)) return items[i];
	}
	return null;
}
/**
 * Return the number of tabs in the folder.
 */
public int getItemCount(){
	return items.length;
}
/**
 * Return the tab items.
 */
public CTabItem [] getItems() {
	CTabItem[] tabItems = new CTabItem [items.length];
	System.arraycopy(items, 0, tabItems, 0, items.length);
	return tabItems;
}
/**
 * Return the selected tab item, or an empty array if there
 * is no selection.
 */
public CTabItem getSelection() {
	if (selectedIndex == -1) return null;
	return items[selectedIndex];
}
/**
 * Return the index of the selected tab item, or -1 if there
 * is no selection.
 */
public int getSelectionIndex() {
	return selectedIndex;
}

private void handleEvents (Event event){
	switch (event.type) {
		case SWT.Dispose:
			onDispose();
			break;
		case SWT.Paint:
			onPaint(event);
			break;
		case SWT.Resize:
			onResize();
			break;
		case SWT.MouseUp:
			onMouseUp(event);
			break;
		case SWT.MouseExit:
			if (event.widget == this) {
				Rectangle inactiveBounds = inactiveCloseBar.getBounds();
				if (inactiveBounds.contains(event.x, event.y)) return;
				inactiveCloseBar.setVisible(false);
				inactiveItem = null;
			}
			if (event.widget == inactiveCloseBar) {
				Rectangle bounds = getBounds();
				if (bounds.contains(event.x, event.y)) return;
				inactiveCloseBar.setVisible(false);
				inactiveItem = null;
			}
			break;
		case SWT.MouseMove:
			onMouseMove(event);
			break;
		case SWT.Selection:
			if (event.widget == scrollLeft) {
				scroll_scrollLeft();
			}
			if (event.widget == scrollRight) {
				scroll_scrollRight();
			}
			if (event.widget == closeItem) {
				closeNotify(getSelection(), event.time);
			}
			if (event.widget == inactiveCloseItem) {
				closeNotify(inactiveItem, event.time);
				inactiveCloseBar.setVisible(false);
				inactiveItem = null;
			}
			break;
		default:
			break;
	}
}
/**
 * Return the index of the specified tab or -1 if the tab is not 
 * in the receiver.
 */
public int indexOf(CTabItem item) {
	if (item == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}
/**
 * 'item' has changed. Store the image size if this is the 
 * first item with an image.
 */
void itemChanged(CTabItem item) {
	Image itemImage = item.getImage();
	if (imageHeight == -1 && itemImage != null) {
		imageHeight = itemImage.getBounds().height;
	}

	int height = getTabHeight();
	Point size = scrollBar.computeSize(SWT.DEFAULT, height);
	scrollBar.setSize(size);
	height = (onBottom) ? height - 2 : height - 1;
	size = closeBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	closeBar.setSize(size);
	inactiveCloseBar.setSize(size);
								
	layoutItems();
	redrawTabArea(-1);
}
private void layoutButtons() {
	
	boolean leftVisible = scroll_leftVisible();
	boolean rightVisible = scroll_rightVisible();

	if (leftVisible || rightVisible) {
		Point size = scrollBar.getSize();
		Rectangle area = super.getClientArea();
		int x = area.x + area.width - size.x - BORDER_RIGHT;
		int y = 0;
		if (!onBottom) {
			y = area.y + BORDER_TOP;
		} else {
			y = area.y + area.height - BORDER_BOTTOM - size.y;
		}
		scrollBar.setLocation(x, y);
		scrollLeft.setEnabled(leftVisible);
		scrollRight.setEnabled(rightVisible);
		scrollBar.setVisible(true);
	} else {
		scrollBar.setVisible(false);
	}
	
	// When the close button is right at the edge of the Tab folder, hide it because
	// otherwise it may block off a part of the border on the right
	if (showClose) {
		Point size = closeBar.getSize();
		CTabItem item = getSelection();
		if (item == null) {
			closeBar.setVisible(false);
		} else {
			int x = item.x + item.width - size.x - 2;
			int y = item.y + 1 + (item.height - 2 - size.y) / 2; // +1 for white line across top of tab
			closeBar.setLocation(x, y);
			if (scrollBar.isVisible()) {
				Rectangle scrollRect = scrollBar.getBounds();
				scrollRect.width += BORDER_RIGHT;
				closeBar.setVisible(!scrollRect.contains(x, y));
			} else {
				closeBar.setVisible(true);
			}
		}
	}
}
/**
 * Layout the items and store the client area size.
 */
private void layoutItems() {
	if (isDisposed()) return;

	Rectangle area = super.getClientArea();
	if (area.height == 0) return;
	
	int tabHeight = getTabHeight();

	shortenedTabs = false;
	if (items.length > 0) {
		int[] widths = new int[items.length];
		int totalWidth = 0;
		GC gc = new GC(this);
		for (int i = 0; i < items.length; i++) {
			widths[i] = items[i].preferredWidth(gc);
			totalWidth += widths[i];
		}
		gc.dispose();
		if (totalWidth < (area.width - BORDER_LEFT - BORDER_RIGHT) ) {
			topTabIndex = 0;
		} else {
			
			int oldAverageWidth = 0;
			int averageWidth = (area.width - BORDER_LEFT - BORDER_RIGHT) / items.length;
			while (averageWidth > oldAverageWidth) {
				int width = area.width - BORDER_LEFT - BORDER_RIGHT;
				int count = items.length;
				for (int i = 0; i < items.length; i++) {
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
			if (averageWidth > MIN_TAB_WIDTH * tabHeight) {
				for (int i = 0; i < items.length; i++) {
					if (widths[i] > averageWidth) {
						widths[i] = averageWidth;
					}
				}
				topTabIndex = 0;
				shortenedTabs = true;
			}
		}
		int x = area.x;
		int y = area.y + BORDER_TOP;
		if (onBottom) {
			y = Math.max(0, area.y + area.height - BORDER_BOTTOM - tabHeight);
		}
		for (int i = topTabIndex - 1; i>=0; i--) { 
			// if the first visible tab is not the first tab
			CTabItem tab = items[i];
			tab.width = widths[i];
			tab.height = getTabHeight();
			x -= tab.width; 
			// layout tab items from right to left thus making them invisible
			tab.x = x;
			tab.y = y;
		}
		
		x = area.x + BORDER_LEFT;
		for (int i=topTabIndex; i<items.length; i++) {
			// continue laying out remaining, visible items left to right 
			CTabItem tab = items[i];
			tab.x = x;
			tab.y = y;
			tab.height = tabHeight;
			tab.width = widths[i];
			x = x + tab.width;
		}
	}
	
	xClient = area.x + BORDER_LEFT + marginWidth;
	if (onBottom) {
		yClient = area.y + BORDER_TOP + marginHeight; 
	} else {
		yClient = area.y + BORDER_TOP + tabHeight + 1 + marginHeight; 
		// +1 is for the line at the bottom of the tabs
	}
	
	// resize the scrollbar and close butotns
	layoutButtons();
}
/** 
 * Paint the receiver.
 */
private void onPaint(Event event) {
	GC gc = event.gc;
	Rectangle rect = super.getClientArea();
	if (items.length == 0) {
		
		if (showBorders) {
			gc.setForeground(borderColor1);
			gc.drawRectangle(rect.x + BORDER_RIGHT, 
			                 rect.y + BORDER_BOTTOM, 
			                 rect.x + rect.width - BORDER_RIGHT - 1, 
			                 rect.y + rect.height - BORDER_BOTTOM - 1);	
			// fill in top and left edge with parent's background color
			gc.setBackground(getParent().getBackground());
			gc.fillRectangle(rect.x, rect.y, BORDER_RIGHT, rect.height);
			gc.fillRectangle(rect.x, rect.y, rect.width, BORDER_BOTTOM);

		}
		if (fixedTabHeight > 0) {
			int y = rect.y + BORDER_BOTTOM + fixedTabHeight;
			if (onBottom) {
				y = rect.y + rect.height - fixedTabHeight - 1;
			}
			gc.setForeground(borderColor1);
			gc.drawLine(rect.x + BORDER_RIGHT, y, rect.x + rect.width, y);
		}
		gc.setForeground(getForeground());
		gc.setBackground(getBackground());
		return;
	}
	
	// redraw the Border
	drawBorder(gc);
	
	rect.x += BORDER_LEFT;
	rect.y += BORDER_TOP;
	rect.width -= BORDER_LEFT + BORDER_RIGHT;
	rect.height -= BORDER_TOP + BORDER_BOTTOM;
	gc.setClipping(rect);
	
	// Draw the unselected tabs first.
	for (int i=0; i < items.length; i++) {
		if (i != selectedIndex && event.getBounds().intersects(items[i].getBounds())) {
			items[i].onPaint(gc, false);
		}
	}
	// Selected tab comes last
	if (selectedIndex != -1) {
		items[selectedIndex].onPaint(gc, true);
	}
	
	// draw insertion mark
	if (insertionIndex > -2) {
		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
		if (insertionIndex == -1) {
			Rectangle bounds = items[0].getBounds();
			gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
			gc.drawLine(bounds.x - 2, bounds.y, bounds.x + 2, bounds.y);
			gc.drawLine(bounds.x - 1, bounds.y + 1, bounds.x + 1, bounds.y + 1);
			gc.drawLine(bounds.x - 1, bounds.y + bounds.height - 2, bounds.x + 1, bounds.y + bounds.height - 2);
			gc.drawLine(bounds.x - 2, bounds.y + bounds.height - 1, bounds.x + 2, bounds.y + bounds.height - 1);

		} else {
			Rectangle bounds = items[insertionIndex].getBounds();
			gc.drawLine(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height - 1);
			gc.drawLine(bounds.x + bounds.width - 2, bounds.y, bounds.x + bounds.width + 2, bounds.y);
			gc.drawLine(bounds.x + bounds.width - 1, bounds.y + 1, bounds.x + bounds.width + 1, bounds.y + 1);
			gc.drawLine(bounds.x + bounds.width - 1, bounds.y + bounds.height - 2, bounds.x + bounds.width + 1, bounds.y + bounds.height - 2);
			gc.drawLine(bounds.x + bounds.width - 2, bounds.y + bounds.height - 1, bounds.x + bounds.width + 2, bounds.y + bounds.height - 1);
		}
	}
	
	gc.setForeground(getForeground());
	gc.setBackground(getBackground());	
}
private void redrawTabArea(int index) {
	int x = 0, y = 0, width = 0, height = 0;
	if (index == -1) {
		Rectangle area = super.getClientArea();
		if (area.width == 0 || area.height == 0) return;
		width = area.x + area.width - BORDER_LEFT - BORDER_RIGHT;
		height = getTabHeight() + 1; // +1 causes top line between content and tabs to be redrawn
		x = area.x + BORDER_LEFT;
		y = area.y + BORDER_TOP; 
		if (onBottom) {
			y = Math.max(0, area.y + area.height - BORDER_BOTTOM - height);
		}
	} else {
		CTabItem item = items[index];
		x = item.x;
		y = item.y;
		width = item.width;
		height = item.height;
	}
	redraw(x, y, width, height, false);
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
 */
public void removeSelectionListener(SelectionListener listener) {
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);	
}
/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
 */
public void removeCTabFolderListener(CTabFolderListener listener) {
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (tabListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < tabListeners.length; i++) {
		if (listener == tabListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (tabListeners.length == 1) {
		tabListeners = new CTabFolderListener[0];
		showClose = false;
		return;
	}
	CTabFolderListener[] newTabListeners = new CTabFolderListener[tabListeners.length - 1];
	System.arraycopy(tabListeners, 0, newTabListeners, 0, index);
	System.arraycopy(tabListeners, index + 1, newTabListeners, index, tabListeners.length - index - 1);
	tabListeners = newTabListeners;
}

/**
 * The widget was resized. Adjust the size of the currently selected page.
 */ 
private void onResize() {

	if (items.length == 0) {
		redraw();
		return;
	}

	Rectangle area = super.getClientArea();
	if (oldArea == null || oldArea.width == 0 || oldArea.height == 0) {
		layoutItems();
		redraw();
	} else {
		if (onBottom && oldArea.height != area.height) {
			// move tabs up or down if tabs on bottom
			layoutItems();
			redraw();		
		} else {
			int width = 0;
			if (oldArea.width < area.width) {
				width = area.width - oldArea.width + BORDER_RIGHT;
			} else if (oldArea.width > area.width) {
				width = BORDER_RIGHT;			
			}
			redraw(area.x + area.width - width, area.y, width, area.height, false);
			
			int height = 0;
			if (oldArea.height < area.height) {
				height = area.height - oldArea.height + BORDER_BOTTOM;		
			}
			if (oldArea.height > area.height) {
				height = BORDER_BOTTOM;		
			}
			redraw(area.x, area.y + area.height - height, area.width, height, false);	
		
			if (oldArea.width != area.width) {
				// resize the widths so that all tabs are visible
				boolean wasShortened = shortenedTabs;
				layoutItems();
				if (wasShortened || shortenedTabs) {
					redrawTabArea(-1);
				}
			}
		}
	}
	oldArea = area;
	
	// resize content
	if (selectedIndex != -1) {
		Control control = items[selectedIndex].getControl();
		if (control != null && !control.isDisposed()) {
			control.setBounds(getClientArea());
		}
	}
}
public void setBackground (Color color) {
	super.setBackground(color);
	color = getBackground();
	Color foreground = getForeground();
	
	// init inactive close button
	initInactiveCloseButton(foreground, color);
	
	// init scroll buttons
	initScrollButtons(foreground, color);
	
	// init close button
	if (gradientColors == null) {
		if (selectionForeground != null) {
			foreground = selectionForeground;
		}
		initCloseButton(foreground, color);
	}
}
/**
 * Specify a gradiant of colours to be draw in the background of the selected tab.
 * For example to draw a gradiant that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	cfolder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		               new int[] {25, 50, 100});
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradiant 
 *               in order of appearance left to right.  The value <code>null</code> clears the
 *               background gradiant. The value <code>null</code> can be used inside the array of 
 *               Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width 
 *                 of the widget at which the color should change.  The size of the percents array must be one 
 *                 less than the size of the colors array.
 */

public void setSelectionBackground(Color[] colors, int[] percents) {
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
	}
	
	// Are these settings the same as before?
	if (gradientImage == null && gradientColors == null && colors == null) {
		if (backgroundImage != null) {
			backgroundImage = null;
			redrawTabArea(selectedIndex);
		}
		return;
	}
	if (gradientColors != null && colors != null 
	    && gradientColors.length == colors.length) {
		boolean same = false;
		for (int i = 0; i < gradientColors.length; i++) {
			same = (gradientColors[i] == colors[i]);
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
	
	// Cleanup
	if (gradientImage != null) {
		gradientImage.dispose();
		gradientImage = null;
	}
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;
	
	// Draw gradient onto an image
	if (colors != null) {
		int x = 0; int y = 0;
		int width = 100; int height = 10;
		
		Display display = getDisplay();
		Image temp = new Image(display, width, height);
		GC gc = new GC(temp);
		int start = 0;
		int end = 0;
		Color background = getBackground();
		if (colors.length == 1) {
			gc.setBackground(colors[0]);
			gc.fillRectangle(temp.getBounds());
		}
		for (int j = 0; j < colors.length - 1; j++) {
			Color startColor = colors[j];
			if (startColor == null) startColor = getBackground();
			RGB rgb1 = startColor.getRGB();
			Color endColor = colors[j+1];
			if (endColor == null) endColor = getBackground();
			RGB rgb2   = endColor.getRGB();
			start = end;
			end = (width) * percents[j] / 100;
			int range = Math.max(1, end - start);
			for (int k = 0; k < (end - start); k++) {
				int r = rgb1.red + k*(rgb2.red - rgb1.red)/range;
				r = (rgb2.red > rgb1.red) ? Math.min(r, rgb2.red) : Math.max(r, rgb2.red);
				int g = rgb1.green + k*(rgb2.green - rgb1.green)/range;
				g = (rgb2.green > rgb1.green) ? Math.min(g, rgb2.green) : Math.max(g, rgb2.green);
				int b = rgb1.blue + k*(rgb2.blue - rgb1.blue)/range;
				b = (rgb2.blue > rgb1.blue) ? Math.min(b, rgb2.blue) : Math.max(b, rgb2.blue);
				Color color = new Color(display, r, g, b); 
				gc.setBackground(color);					
				gc.fillRectangle(start + k,y,1,height);
				gc.setBackground(background);
				color.dispose();
			}
		}
		gc.dispose();
		gradientImage = temp;
		gradientColors = colors;
		gradientPercents = percents;
		backgroundImage = temp;
		
		Color closeBackground = colors[colors.length - 1];
		if (closeBackground == null){
			closeBackground = background;
		}
		Color closeForeground = getForeground();
		initCloseButton(closeForeground, closeBackground);
	} else {
		Color closeBackground = getBackground();
		Color closeForeground = getForeground();
		initCloseButton(closeForeground, closeBackground);
	}
	if (selectedIndex > -1) redrawTabArea(selectedIndex);
}
public void setSelectionBackground(Image image) {
	if (image == backgroundImage) return;
	if (gradientImage != null) {
		gradientImage.dispose();
		gradientImage = null;
	}
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = image;
	redrawTabArea(selectedIndex);
}
public void setBorderVisible(boolean show) {
	if (showBorders == show) return;
	
	showBorders = show;
	if (showBorders) {
		if ((getStyle() & SWT.FLAT) != 0) {
			BORDER_BOTTOM = BORDER_TOP = BORDER_LEFT = BORDER_RIGHT = 1;
		} else {
			BORDER_LEFT = BORDER_TOP = 1;
			BORDER_RIGHT = BORDER_BOTTOM = 3;
		}
	} else {
		BORDER_BOTTOM = BORDER_TOP = BORDER_LEFT = BORDER_RIGHT = 0;
	}
	layoutItems();
	redraw();
}
public void setFont(Font font) {
	if (font != null && font.equals(getFont())) return;
	super.setFont(font);	
	layoutItems();
	redrawTabArea(-1);
}
public void setForeground (Color color) {
	super.setForeground(color);
	color = getForeground();
	Color background = getBackground();
	
	initInactiveCloseButton(color, background);
	initScrollButtons(color, background);
	
	if (gradientColors != null) {
		background = gradientColors[gradientColors.length - 1];
	}
	initCloseButton(color, background);	
}
public void setSelectionForeground (Color color) {
	if (selectionForeground == color) return;
	if (color == null) color = getForeground();
	selectionForeground = color;
	if (selectedIndex > -1) {
		redrawTabArea(selectedIndex);
	}
}
public void setInsertMark(CTabItem item, boolean after) {
	int index = -1;
	if (item != null) {
		index = indexOf(item);
	}
	setInsertMark(index, after);
}
public void setInsertMark(int index, boolean after) {
	if (index < -1 || index >= getItemCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	if (index == -1) {
		index = -2;
	} else {
		index = after ? index : --index;
	}
	
	if (insertionIndex == index) return;
	int oldIndex = insertionIndex;
	insertionIndex = index;
	if (index > -1)	redrawTabArea(index);
	if (oldIndex > 1) redrawTabArea(oldIndex);
}

/**
 * Set the selection to the tab at the specified index.
 */
public void setSelection(int index) {
	if (index < 0 || index >= items.length)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		
	if (selectedIndex == index) return;
	
	if (showClose) {
		inactiveCloseBar.setVisible(false);
		inactiveItem = null;
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
	ensureVisible();
	
	redrawTabArea(-1);
}
private void ensureVisible() {
	// make sure item is visible
	Rectangle area = super.getClientArea();
	if (area.width == 0) return;
	int areaWidth = area.x + area.width - BORDER_RIGHT;
	
	CTabItem tabItem = items[selectedIndex];
	if (selectedIndex < topTabIndex) {
		topTabIndex = selectedIndex;
	}
	layoutItems();
	
	int scrollWidth = scrollBar.getSize().x;
	int width = areaWidth;
	if (scroll_leftVisible() || scroll_rightVisible()) {
		width -=  scrollWidth;
	}
	while (tabItem.x + tabItem.width > width && selectedIndex != topTabIndex) {
		topTabIndex++;
		layoutItems();
		width = areaWidth;
		if (scroll_leftVisible() || scroll_rightVisible()) {
			width -=  scrollWidth;
		}
	}
}
/**
 * Set the selection to the specified item.
 */
public void setSelection(CTabItem item) {
	if (item == null) 
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf(item);
	setSelection(index);
}
/**
 * Set the selection to the tab at the specified index.
 */
private void setSelectionNotify(int index) {	
	int oldSelectedIndex = selectedIndex;
	setSelection(index);
	if (selectedIndex != oldSelectedIndex && selectedIndex != -1) {
		Event event = new Event();
		event.item = getItem(selectedIndex);
		notifyListeners(SWT.Selection, event);
	}
}

private void initCloseButton(Color foreground, Color background) {
	if (closeImage != null) {
		closeImage.dispose();
		closeImage = null;
	}
	closeImage = drawCloseImage(foreground, background);
	closeBar.setBackground(background);
	closeBar.setForeground(foreground);
	closeItem.setImage(closeImage);
}
private void initInactiveCloseButton(Color foreground, Color background) {
	if (inactiveCloseImage != null) {
		inactiveCloseImage.dispose();
		inactiveCloseImage = null;
	}
	inactiveCloseImage = drawCloseImage(foreground, background);
	inactiveCloseBar.setBackground(background);
	inactiveCloseBar.setForeground(foreground);
	inactiveCloseItem.setImage(inactiveCloseImage);
}
private Image drawCloseImage(Color foreground, Color background) {
	Image image = new Image(getDisplay(), 9, 9);
	GC gc = new GC(image);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, 9, 9);
	gc.setForeground(foreground);
	for (int i = 0; i < 8; i++) {
		gc.drawLine(i, i, i + 1, i);
		gc.drawLine(7 - i, i, 8 - i, i);
	}
	gc.dispose();
	return image;
}
private void initScrollButtons(Color foreground, Color background) {

	scrollBar.setBackground(background);
	
	Display display = getDisplay();
	Color shadow = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	if (arrowLeftImage != null) {
		arrowLeftImage.dispose();
		arrowLeftImage = null;
	}
	if (arrowLeftDisabledImage != null) {
		arrowLeftDisabledImage.dispose();
		arrowLeftDisabledImage = null;
	}
	arrowLeftImage = drawArrowImage(foreground, background, true);
	arrowLeftDisabledImage = drawArrowImage(shadow, background, true);
	scrollLeft.setImage(arrowLeftImage);
	scrollLeft.setDisabledImage(arrowLeftDisabledImage);
	scrollLeft.setHotImage(arrowLeftImage);
	
	if (arrowRightImage != null) {
		arrowRightImage.dispose();
		arrowRightImage = null;
	}
	if (arrowRightDisabledImage != null) {
		arrowRightDisabledImage.dispose();
		arrowRightDisabledImage = null;
	}
	arrowRightImage = drawArrowImage(foreground, background, false);		
	arrowRightDisabledImage = drawArrowImage(shadow, background, false);
	scrollRight.setImage(arrowRightImage);
	scrollRight.setDisabledImage(arrowRightDisabledImage);
	scrollRight.setHotImage(arrowRightImage);
}

private Image drawArrowImage (Color foreground, Color background, boolean left) {
	// create image for left button
	int arrow[] = new int[6];
	if (left) {
		arrow[0] = 4; arrow[1] = 0;
		arrow[2] = 0; arrow[3] = 4;
		arrow[4] = 4; arrow[5] = 8;				
	} else {
		arrow[0] = 0; arrow[1] = 0;
		arrow[2] = 4; arrow[3] = 4;
		arrow[4] = 0; arrow[5] = 8;	
	}
	
	Image image = new Image(getDisplay(), 5, 9);
	GC gc = new GC(image);	
	gc.setBackground(background);
	gc.fillRectangle(0, 0, 5, 9);
	gc.setBackground(foreground);
	gc.fillPolygon(arrow);
	gc.setBackground(background);
	gc.dispose();
	
	return image;
}
/** 
 * A mouse button was pressed down. 
 * If one of the tab scroll buttons was hit, scroll in the appropriate 
 * direction.
 * If a tab was hit select the tab.
 */
private void onMouseUp(Event event) {
	for (int i=0; i<items.length; i++) {
		if (items[i].getBounds().contains(new Point(event.x, event.y))) {
			setSelectionNotify(i);
			return;
		}
	}
}
private void onMouseMove(Event event) {
	if (!showClose) return;
	
	CTabItem item = null;
	Rectangle itemRect = null;
	for (int i=0; i<items.length; i++) {
		itemRect = items[i].getBounds();
		if (itemRect.contains(new Point(event.x, event.y))) {
			item = items[i];
			break;
		}
	}
	if (item == inactiveItem) return;
	
	inactiveCloseBar.setVisible(false);
	inactiveItem = null;
		
	if (item == null || item == getSelection()) return;

	Point closeRect = inactiveCloseBar.getSize();
	int x = itemRect.x + itemRect.width - closeRect.x - 2;
	int y = itemRect.y + 1 + (itemRect.height - 2 - closeRect.y)/2;
	if (scrollBar.isVisible()) {
		Rectangle scrollArea = scrollBar.getBounds();
		scrollArea.width += BORDER_RIGHT;
		if (scrollArea.contains(x, y)) return;
	}
	
	inactiveCloseBar.setLocation(x, y);
	inactiveCloseBar.setVisible(true);
	inactiveItem = item;
}
/** 
 * Answer the area where the left scroll button is drawn.
 */
private Rectangle scroll_getBounds() {
	if (scrollBar != null)
		return scrollBar.getBounds();
	return new Rectangle(0, 0, 0, 0);
}

/**
 * Answer true if not all tabs can be visible in the receive
 * thus requiring the scroll buttons to be visible.
 */ 
private boolean scroll_leftVisible() {
	return topTabIndex > 0;
}

/**
 * Answer true if not all tabs can be visible in the receive
 * thus requiring the scroll buttons to be visible.
 */ 
private boolean scroll_rightVisible() {	
	if (topTabIndex < items.length - 1) { 
		// only show Scroll buttons if there is more than one item
		// and if we are not alread at the last item
		CTabItem tabItem = items[items.length-1];
		int tabStopX = tabItem.x + tabItem.width;
		Rectangle area = super.getClientArea();
		if (tabStopX > area.x + area.width - BORDER_RIGHT) {
			return true;	// not all tabs fit in the client area
		}
	}
	return false;
}

/**
 * Scroll the tab items to the left.
 */
private void scroll_scrollLeft() {
	if (scroll_leftVisible()) {
		--topTabIndex;
		layoutItems();
		redrawTabArea(-1);
	}
}

/**
 * Scroll the tab items to the right.
 */
private void scroll_scrollRight() {
	if (scroll_rightVisible()) {
		topTabIndex++;
		layoutItems();
		redrawTabArea(-1);
	}
}
public void setTabHeight(int height) {
	if (height < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	fixedTabHeight = height + CTabItem.TOP_MARGIN + CTabItem.BOTTOM_MARGIN;
	layoutItems();
}
}