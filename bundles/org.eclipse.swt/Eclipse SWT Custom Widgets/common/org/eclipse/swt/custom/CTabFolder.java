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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.accessibility.*;

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
	public int MIN_TAB_WIDTH = 3;

	/* sizing, positioning */
	int xClient, yClient;
	boolean onBottom = false;
	boolean fixedTabHeight;
	int tabHeight;
	
	/* item management */
	private CTabItem items[] = new CTabItem[0];
	private int selectedIndex = -1;
	int topTabIndex = -1; // index of the left most visible tab.

	/* External Listener management */
	private CTabFolderListener[] tabListeners = new CTabFolderListener[0];
	
	/* Color appearance */
	Image backgroundImage;
	Color[] gradientColors;
	int[] gradientPercents;
	Color selectionForeground;
	Color background;

	// internal constants
	private static final int DEFAULT_WIDTH = 64;
	private static final int DEFAULT_HEIGHT = 64;
	
	// scrolling arrows
	private ToolBar arrowBar;
	private Image arrowLeftImage;
	private Image arrowRightImage;

	private Control topRight;

	// close button
	boolean showClose = false;
	private Image closeImage;
	ToolBar closeBar;
	private ToolBar inactiveCloseBar;
	private CTabItem inactiveItem;	
	
	// borders
	boolean showBorders = false;
	private int borderBottom = 0;
	private int borderLeft = 0;
	private int borderRight = 0;
	private int borderTop = 0;
	private Color borderColor1;
	private Color borderColor2;
	private Color borderColor3;

	// when disposing CTabFolder, don't try to layout the items or 
	// change the selection as each child is destroyed.
	private boolean inDispose = false;

	// keep track of size changes in order to redraw only affected area
	// on Resize
	private Point oldSize;
	private Font oldFont;
	
	// insertion marker
	int insertionIndex = -2; // Index of insert marker.  Marker always shown after index.
	                         // -2 means no insert marker
	                         
	// tool tip
	private Shell tip;
	private Label label;
	private boolean showToolTip = false;
	private CTabItem toolTipItem;

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
 * @see #getStyle
 */
public CTabFolder(Composite parent, int style) {
	super(parent, checkStyle (style));
	
	onBottom = (getStyle() & SWT.BOTTOM) != 0;
	
	borderColor1 = new Color(getDisplay(), borderInsideRGB);
	borderColor2 = new Color(getDisplay(), borderMiddleRGB);
	borderColor3 = new Color(getDisplay(), borderOutsideRGB);

	// tool tip support
	tip = new Shell (getShell(), SWT.ON_TOP);
	label = new Label (tip, SWT.CENTER);
	
	// Add all listeners
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:           onDispose(); break;
				case SWT.Paint:             onPaint(event);	break;
				case SWT.Resize:            onResize();	break;
				case SWT.MouseDoubleClick:  onMouseDoubleClick(event);	break;
				case SWT.MouseDown:         onMouseDown(event);	break;
				case SWT.MouseExit:         onMouseExit(event);	break;
				case SWT.MouseHover:		onMouseHover(event);	break;
				case SWT.MouseMove:         onMouseMove(event);	break;
				case SWT.FocusIn:           onFocus(event);	break;
				case SWT.FocusOut:          onFocus(event);	break;
				case SWT.KeyDown:           onKeyDown(event); break;
				case SWT.Traverse:          onTraverse(event); break;
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
		SWT.FocusIn, 
		SWT.FocusOut, 
		SWT.KeyDown,
		SWT.Traverse,
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}
	
	createArrowBar();
	createCloseBar();
	
	setBorderVisible((style & SWT.BORDER) != 0);
	
	initAccessible();	

}
private static int checkStyle (int style) {
	int mask = SWT.TOP | SWT.BOTTOM | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	style = style & mask;
	// TOP and BOTTOM are mutually exlusive.
	// TOP is the default
	if ((style & SWT.TOP) != 0) 
		style = style & ~(SWT.TOP | SWT.BOTTOM) | SWT.TOP;
	// reduce the flash by not redrawing the entire area on a Resize event
	style |= SWT.NO_REDRAW_RESIZE;
	return style;
}

//protected void checkSubclass () {
//	String name = getClass().getName ();
//	String validName = CTabFolder.class.getName();
//	if (!validName.equals(name)) {
//		SWT.error (SWT.ERROR_INVALID_SUBCLASS);
//	}
//}

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
	checkWidget();
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a tab item is closed.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *      <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 *
 * @see CTabFolderListener
 * @see #removeCTabFolderListener
 */
public void addCTabFolderListener(CTabFolderListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolderListener[] newTabListeners = new CTabFolderListener[tabListeners.length + 1];
	System.arraycopy(tabListeners, 0, newTabListeners, 0, tabListeners.length);
	tabListeners = newTabListeners;
	tabListeners[tabListeners.length - 1] = listener;
	showClose = true;
	setButtonBounds();
}
private void closeNotify(CTabItem item, int time) {
	if (item == null) return;
	
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
	checkWidget();
	int minWidth = 0;
	int minHeight = 0;

	// preferred width of tab area to show all tabs
	GC gc = new GC(this);
	for (int i = 0; i < items.length; i++) {
		minWidth += items[i].preferredWidth(gc);
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

	if (wHint != SWT.DEFAULT) minWidth  = wHint;
	if (hHint != SWT.DEFAULT) minHeight = hHint;

	Rectangle trim = computeTrim(0, 0, minWidth, minHeight);
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	if (items.length == 0) {
		if (!showBorders) return new Rectangle(x, y, width, height);
		int trimX = x - borderRight - 1;
		int trimY = y - borderBottom - 1;
		int trimWidth = width + borderRight + 2;
		int trimHeight = height + borderBottom + 2;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	} else {
		int trimX = x - marginWidth - borderLeft;
		int trimY = y - marginHeight - tabHeight - borderTop - 1;
		// -1 is for the line at the bottom of the tabs
		if (onBottom) {
			trimY = y - marginHeight - borderTop;
		}
		int trimWidth = width + borderLeft + borderRight + 2*marginWidth;
		int trimHeight = height + borderTop + borderBottom + 2*marginHeight + tabHeight + 1;
		return new Rectangle (trimX, trimY, trimWidth, trimHeight);
	}
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
		resetTabSize(true);
	} else {
		setItemBounds();
		showItem(item);
	}
	
	if (items.length == 1) {
		redraw();
	} else {
		redrawTabArea(-1);
	}
}

private void createArrowBar() {
	// create arrow buttons for scrolling 
	arrowBar = new ToolBar(this, SWT.FLAT);
	arrowBar.setVisible(false);
	arrowBar.setBackground(background);
	ToolItem scrollLeft = new ToolItem(arrowBar, SWT.PUSH);
	scrollLeft.setEnabled(false);
	ToolItem scrollRight = new ToolItem(arrowBar, SWT.PUSH);
	scrollRight.setEnabled(false);
	
	scrollLeft.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			scroll_scrollLeft();
		}
	});
	scrollRight.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			scroll_scrollRight();
		}
	});
	
}
private void createCloseBar() {
	closeBar = new ToolBar(this, SWT.FLAT);
	closeBar.setVisible(false);
	if (gradientColors != null && gradientColors.length > 0) {
		closeBar.setBackground(gradientColors[gradientColors.length - 1]);
	} else {
		closeBar.setBackground(background);
	}
	ToolItem closeItem = new ToolItem(closeBar, SWT.PUSH);
	
	inactiveCloseBar = new ToolBar(this, SWT.FLAT);
	inactiveCloseBar.setVisible(false);
	inactiveCloseBar.setBackground(background);
	ToolItem inactiveCloseItem = new ToolItem(inactiveCloseBar, SWT.PUSH);

	closeItem.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			closeNotify(getSelection(), event.time);
		}
	});
	inactiveCloseItem.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			closeNotify(inactiveItem, event.time);
			inactiveCloseBar.setVisible(false);
			inactiveItem = null;
		}
	});
	inactiveCloseBar.addListener (SWT.MouseExit, new Listener() {
		public void handleEvent(Event event) {
			if (inactiveItem != null) {
				Rectangle itemBounds = inactiveItem.getBounds();
				if (itemBounds.contains(event.x, event.y)) return;
			}
			inactiveCloseBar.setVisible(false);
			inactiveItem = null;
		}
	});
	
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
		selectedIndex = -1;
		setSelection(Math.max(0, index - 1), true);
		Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
	} else if (selectedIndex > index) {
		selectedIndex --;
	}
	
	setItemBounds();
	redrawTabArea(-1);
}
private void onKeyDown(Event e) {
	if (e.keyCode != SWT.ARROW_LEFT && e.keyCode != SWT.ARROW_RIGHT) return;
	int leadKey = (getStyle() & SWT.MIRRORED) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
	if (e.keyCode == leadKey) {
		if (selectedIndex > 0) {
			setSelection(selectedIndex - 1, true);
		}
	} else {
		if (selectedIndex < items.length - 1) {
			setSelection(selectedIndex + 1, true);
		}
	}
}
/**
 * Dispose the items of the receiver
 */
private void onDispose() {
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
	
	// clean up resources
	if (tip != null  && !tip.isDisposed()) {
		tip.dispose();
		tip = null;
		label = null;
	}
	
	if (arrowLeftImage != null) arrowLeftImage.dispose();
	arrowLeftImage = null;
	if (arrowRightImage != null) arrowRightImage.dispose();
	arrowRightImage = null;
	if (closeImage != null) closeImage.dispose();
	closeImage = null;
	
	gradientColors = null;
	gradientPercents = null;
	backgroundImage = null;

	if (borderColor1 != null) borderColor1.dispose();
	borderColor1 = null;
	
	if (borderColor2 != null) borderColor2.dispose();
	borderColor2 = null;
	
	if (borderColor3 != null) borderColor3.dispose();
	borderColor3 = null;
}
private void onFocus(Event e) {
	checkWidget();
	if (selectedIndex >= 0) {
		redrawTabArea(selectedIndex);
	} else {
		setSelection(0, true);
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
		int lineY = d.y + borderTop + tabHeight;
		if (onBottom) {
			lineY = d.y + d.height - borderBottom - tabHeight - 1;
		}
		gc.setForeground(borderColor1);
		gc.drawLine(d.x + borderLeft, lineY, d.x + d.width - borderRight, lineY);
	}

	gc.setForeground(getForeground());
}
public Rectangle getClientArea() {
	checkWidget();
	Point size = getSize();
	int width = size.x  - borderLeft - borderRight - 2*marginWidth;
	int height = size.y - borderTop - borderBottom - 2*marginHeight;
	if (items.length == 0) {		
		return new Rectangle(borderLeft + marginWidth, borderTop + marginHeight, width, height);	
	}
	height -= tabHeight + 1; //+1 for line under tabs
	return new Rectangle(xClient, yClient, width, height);
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
 * Return the tab that is located at the specified index.
 * 
 * @return the item at the specified index
 */
public CTabItem getItem (int index) {
	//checkWidget();
	if (index  < 0 || index >= items.length) 
		SWT.error(SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
* Gets the item at a point in the widget.
* <p>
*
* @return the item at a point
*/
public CTabItem getItem (Point pt) {
	//checkWidget();
	if (items.length == 0) return null;
	int lastItem = getLastItem();
	lastItem = Math.min(items.length - 1, lastItem + 1);
	for (int i = topTabIndex; i <= lastItem; i++) {
		Rectangle bounds = items[i].getBounds();
		if (bounds.contains(pt)) return items[i];
	}
	return null;
}
/**
 * Return the number of tabs in the folder.
 * 
 * @return the number of tabs in the folder
 */
public int getItemCount(){
	//checkWidget();
	return items.length;
}
/**
 * Return the tab items.
 * 
 * @return the tab items
 */
public CTabItem [] getItems() {
	//checkWidget();
	CTabItem[] tabItems = new CTabItem [items.length];
	System.arraycopy(items, 0, tabItems, 0, items.length);
	return tabItems;
}

private int getLastItem(){
	if (items.length == 0) return -1;
	Rectangle area = getClientArea();
	if (area.width <= 0) return 0;
	Rectangle toolspace = getToolSpace();
	if (toolspace.width == 0) return items.length -1;
	int width = area.width - toolspace.width;
	int index = topTabIndex;
	int tabWidth = items[index].width;
	while (index < items.length - 1) {
		tabWidth += items[index + 1].width;
		if (tabWidth > width) break;
		index++;
	}
	return index;
}
/**
 * Return the selected tab item, or an empty array if there
 * is no selection.
 * 
 * @return the selected tab item
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
 */
public int getSelectionIndex() {
	//checkWidget();
	return selectedIndex;
}
private Rectangle getToolSpace() {
	boolean showArrows = scroll_leftVisible() || scroll_rightVisible();
	if (!showArrows && topRight == null) return new Rectangle(0, 0, 0, 0);
	Rectangle toolspace;
	if (showArrows) {
		toolspace = arrowBar.getBounds();
		toolspace.width += borderRight;
		if (topRight != null) toolspace.width += topRight.getSize().x;
	} else {
		toolspace = topRight.getBounds();
		toolspace.width += borderRight;
	}
	return toolspace;
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
 * @return the index of the specified tab item or -1
 * 
 * @exception SWTError <ul>
 *      <li>ERROR_NULL_ARGUMENT when the item is null</li>
 *	</ul>
 */
public int indexOf(CTabItem item) {
	//checkWidget();
	if (item == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}

private void initAccessible() {
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

private void setButtonBounds() {
	
	updateArrowBar();
	updateCloseBar();

	Rectangle area = super.getClientArea();

	int offset = 0;
	if (topRight != null) {
		Point size = topRight.computeSize(SWT.DEFAULT, tabHeight);
		int x = area.x + area.width - borderRight - size.x;
		int y = onBottom ? area.y + area.height - borderBottom - size.y : area.y + borderTop;
		topRight.setBounds(x, y, size.x, size.y);
		offset = size.x;
	}
	boolean leftVisible = scroll_leftVisible();
	boolean rightVisible = scroll_rightVisible();
	if (leftVisible || rightVisible) {
		Point size = arrowBar.computeSize(SWT.DEFAULT, tabHeight);
		int x = area.x + area.width - borderRight - size.x - offset;
		int y = (onBottom) ? area.y + area.height - borderBottom - size.y : area.y + borderTop;
		
		arrowBar.setBounds(x, y, size.x, size.y);
		ToolItem[] items = arrowBar.getItems();
		items[0].setEnabled(leftVisible);
		items[1].setEnabled(rightVisible);
		arrowBar.setVisible(true);
	} else {
		arrowBar.setVisible(false);
	}
	
	// When the close button is right at the edge of the Tab folder, hide it because
	// otherwise it may block off a part of the border on the right
	if (showClose) {
		inactiveCloseBar.setVisible(false);
		CTabItem item = getSelection();
		if (item == null) {
			closeBar.setVisible(false);
		} else {
			int toolbarHeight = tabHeight - CTabItem.TOP_MARGIN - CTabItem.BOTTOM_MARGIN + 2; // +2 to ignore gap between focus rectangle
			Point size = closeBar.computeSize(SWT.DEFAULT, toolbarHeight);
			int x = item.x + item.width - size.x - 2; // -2 to not overlap focus rectangle and trim
			int y = item.y + Math.max(0, (item.height - toolbarHeight)/2);		
			closeBar.setBounds(x, y, size.x, toolbarHeight);
			Rectangle toolspace = getToolSpace();
			Point folderSize = getSize();
			boolean visible = (toolspace.width == 0 || x < toolspace.x) && x + size.x < folderSize.x - borderRight;
			closeBar.setVisible(visible);
		}
	}
}
private boolean setItemLocation() {
	if (items.length == 0) return false;
	Rectangle area = super.getClientArea();
	int x = area.x;
	int y = area.y + borderTop;
	if (onBottom) y = Math.max(0, area.y + area.height - borderBottom - tabHeight);
	
	boolean changed = false;
	for (int i = topTabIndex - 1; i>=0; i--) { 
		// if the first visible tab is not the first tab
		CTabItem tab = items[i];
		x -= tab.width; 
		if (!changed && (tab.x != x || tab.y != y) ) changed = true;
		// layout tab items from right to left thus making them invisible
		tab.x = x;
		tab.y = y;
	}
	
	x = area.x + borderLeft;
	for (int i = topTabIndex; i < items.length; i++) {
		// continue laying out remaining, visible items left to right 
		CTabItem tab = items[i];
		tab.x = x;
		tab.y = y;
		x = x + tab.width;
	}
	setButtonBounds();
	return changed;
}
private void setLastItem(int index) {
	if (index < 0 || index > items.length - 1) return;
	Rectangle area = getClientArea();
	if (area.width <= 0) return;
	int maxWidth = area.width;
	Rectangle toolspace = getToolSpace();
	if (toolspace.width > 0){
		maxWidth -= toolspace.width;
	}
	int tabWidth = items[index].width;
	while (index > 0) {
		tabWidth += items[index - 1].width;
		if (tabWidth > maxWidth) break;
		index--;
	}
	topTabIndex = index;
	setItemLocation();
	redrawTabArea(-1);
}
/**
 * Layout the items and store the client area size.
 */
boolean setItemBounds() {
	boolean changed = false;
	if (isDisposed()) return changed;
	Rectangle area = super.getClientArea();

	xClient = area.x + borderLeft + marginWidth;
	if (onBottom) {
		yClient = area.y + borderTop + marginHeight; 
	} else {
		yClient = area.y + borderTop + tabHeight + 1 + marginHeight; 
		// +1 is for the line at the bottom of the tabs
	}
	
	if (area.width <= 0 || area.height <= 0 || items.length == 0) return changed;
	
	int[] widths = new int[items.length];
	GC gc = new GC(this);
	for (int i = 0; i < items.length; i++) {
		widths[i] = items[i].preferredWidth(gc);
	}
	gc.dispose();

	int oldAverageWidth = 0;
	int averageWidth = (area.width - borderLeft - borderRight) / items.length;
	while (averageWidth > oldAverageWidth) {
		int width = area.width - borderLeft - borderRight;
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
	averageWidth = Math.max(averageWidth, MIN_TAB_WIDTH * tabHeight);
	for (int i = 0; i < items.length; i++) {
		if (widths[i] > averageWidth) {
			widths[i] = averageWidth;
		}
	}
	
	int totalWidth = 0;
	for (int i = 0; i < items.length; i++) { 
		CTabItem tab = items[i];
		if (tab.height != tabHeight || tab.width != widths[i]) changed = true;
		tab.height = tabHeight;
		tab.width = widths[i];
		totalWidth += widths[i];
	}
	
	int areaWidth = area.x + area.width - borderRight;
	if (totalWidth <= areaWidth) {
		topTabIndex = 0;
	} 
	if (setItemLocation()) changed = true;
	
	// Is there a gap after last item showing
	if (correctLastItem()) changed = true;
	return changed;
}
private boolean onMnemonic (Event event) {
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
/** 
 * Paint the receiver.
 */
private void onPaint(Event event) {
	Font font = getFont();
	if (oldFont == null || !oldFont.equals(font)) {
		oldFont = font;
		resetTabSize(true);
	}
	GC gc = event.gc;
	Rectangle rect = super.getClientArea();
	if (items.length == 0) {
		if (showBorders) {		
			if ((getStyle() & SWT.FLAT) != 0) {
				gc.setForeground(borderColor1);
				gc.drawRectangle(rect.x, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1);
			} else {
				gc.setForeground(borderColor1);
				gc.drawRectangle(rect.x, rect.y, rect.x + rect.width - 3, rect.y + rect.height - 3);
						
				// fill in right and bottom edges with parent's background
				gc.setBackground(getParent().getBackground());
				gc.fillRectangle(rect.x + rect.width - 2, rect.y, 2, rect.height);
				gc.fillRectangle(rect.x, rect.y + rect.height - 2, rect.width, 2);
			}
			gc.setForeground(getForeground());
		}
		return;
	}
	
	// redraw the Border
	drawBorder(gc);
	
	rect.x += borderLeft;
	rect.y += borderTop;
	rect.width -= borderLeft + borderRight;
	rect.height -= borderTop + borderBottom;
	Rectangle clip = gc.getClipping ();
	gc.setClipping(clip.intersection(rect));
	
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
		width = area.x + area.width - borderLeft - borderRight;
		height = tabHeight + 1; // +1 causes top line between content and tabs to be redrawn
		x = area.x + borderLeft;
		y = area.y + borderTop; 
		if (onBottom) {
			y = Math.max(0, area.y + area.height - borderBottom - height);
		}
	} else {
		CTabItem item = items[index];
		x = item.x;
		y = item.y;
		Rectangle area = super.getClientArea();
		width = area.x + area.width - x;
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
	checkWidget();
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
	checkWidget();
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
		setButtonBounds();
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
	
	if (setItemBounds()) {
		redrawTabArea(-1);
	}
	
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

public void setBackground (Color color) {
	super.setBackground(color);
	background = color;
	// init inactive close button
	inactiveCloseBar.setBackground(color);
	
	// init scroll buttons
	arrowBar.setBackground(color);

	//init topRight control
	if (topRight != null)
		topRight.setBackground(color);

	// init close button
	if (gradientColors == null) {
		closeBar.setBackground(color);
	}
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
		if (getDisplay().getDepth() < 15) {
			// Don't use gradients on low color displays
			colors = new Color[] { colors[0] };
			percents = new int[] { };
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
		closeBar.setBackground(background);
	} else {
		gradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i)
			gradientColors[i] = colors[i];
		gradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i)
			gradientPercents[i] = percents[i];
		if (getDisplay().getDepth() < 15) closeBar.setBackground(background);
		else closeBar.setBackground(gradientColors[gradientColors.length - 1]);
	}

	// Refresh with the new settings
	if (selectedIndex > -1) redrawTabArea(selectedIndex);
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
	redrawTabArea(selectedIndex);
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
	if (showBorders == show) return;
	
	showBorders = show;
	if (showBorders) {
		if ((getStyle() & SWT.FLAT) != 0) {
			borderBottom = borderTop = borderLeft = borderRight = 1;
		} else {
			borderLeft = borderTop = 1;
			borderRight = borderBottom = 3;
		}
	} else {
		borderBottom = borderTop = borderLeft = borderRight = 0;
	}
	oldSize = null;
	notifyListeners(SWT.Resize, new Event());
}
public void setFont(Font font) {
	checkWidget();
	if (font != null && font.equals(getFont())) return;
	super.setFont(font);
	oldFont = getFont();
	resetTabSize(true);
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
	if (color == null) color = getForeground();
	selectionForeground = color;
	if (selectedIndex > -1) {
		redrawTabArea(selectedIndex);
	}
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
 * @param item the index of the item with which the mark is associated or null
 * 
 * @param after true if the mark should be displayed after the specified item
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
	if (selectedIndex == index) return;
	
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
	showItem(items[selectedIndex]);
	setButtonBounds();
	redrawTabArea(-1);
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
 */
public void setTopRight(Control control) {
	checkWidget();
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	topRight = control;
	resetTabSize(true);
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
	
	int index = indexOf(item);
	if (index < topTabIndex) {
		topTabIndex = index;
		setItemLocation();
		redrawTabArea(-1);
		return;
	}
	Rectangle area = getClientArea();
	if (area.width <= 0) {
		topTabIndex = index;
		return;
	}
	int rightEdge = area.x + area.width;
	Rectangle rect = getToolSpace();
	if (rect.width > 0) {
		rightEdge -=  rect.width;
	}
	if (item.x + item.width < rightEdge) return;
	setLastItem(index);
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
 * Set the selection to the tab at the specified item.
 * 
 * @param index the tab item to be selected
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_NULL_ARGUMENT - if argument is null</li>
 * </ul>
 */
public void setSelection(CTabItem item) {
	checkWidget();
	if (item == null) 
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf(item);
	setSelection(index);
}
/**
 * Set the selection to the tab at the specified index.
 */
private void setSelection(int index, boolean notify) {	
	int oldSelectedIndex = selectedIndex;
	setSelection(index);
	if (notify && selectedIndex != oldSelectedIndex && selectedIndex != -1) {
		Event event = new Event();
		event.item = getItem(selectedIndex);
		notifyListeners(SWT.Selection, event);
	}
}

private Image scaleImage (Image image, int oldSize, int newSize) {
	Display display = getDisplay();
	Color foreground = getForeground();
	Color black = display.getSystemColor(SWT.COLOR_BLACK);
	Color background = getBackground();
	PaletteData palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), black.getRGB()});
	ImageData imageData = new ImageData(newSize, newSize, 4, palette);
	imageData.transparentPixel = 1;
	Image temp = new Image(display, imageData);
	GC gc = new GC(temp);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, newSize, newSize);
	gc.drawImage(image, 0, 0, oldSize, oldSize, 0, 0, newSize, newSize);
	gc.dispose();
	return temp;
}
private void updateCloseBar() {
	//Temporary code - need a better way to determine toolBar trim
	int toolbarTrim = 4;
	String platform = SWT.getPlatform();
	if ("photon".equals(platform)) toolbarTrim = 6; //$NON-NLS-1$
	if ("gtk".equals(platform)) toolbarTrim = 8; //$NON-NLS-1$

	int maxHeight = tabHeight - CTabItem.TOP_MARGIN - CTabItem.BOTTOM_MARGIN - toolbarTrim;
	if (maxHeight < 3) return;
	int imageHeight = Math.max(9, maxHeight);
	
	if (closeImage != null) {
		int height = closeImage.getBounds().height;
		if (height == imageHeight) return;
		if (imageHeight > maxHeight && height == maxHeight) return;
	}

	if (closeBar != null) closeBar.dispose();
	closeBar = null;
	if (inactiveCloseBar != null) inactiveCloseBar.dispose();
	inactiveCloseBar = null;
	createCloseBar();
	
	ToolItem closeItem = closeBar.getItems()[0];
	ToolItem inactiveCloseItem = inactiveCloseBar.getItems()[0];
		
	if (closeImage != null) closeImage.dispose();
	
	Display display = getDisplay();
	Color foreground = getForeground();
	Color black = display.getSystemColor(SWT.COLOR_BLACK);
	Color background = getBackground();
	
	PaletteData palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), black.getRGB()});
	ImageData imageData = new ImageData(imageHeight, imageHeight, 4, palette);
	imageData.transparentPixel = 1;
	closeImage = new Image(display, imageData);
	GC gc = new GC(closeImage);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, imageHeight, imageHeight);
	gc.setForeground(black);
	
	//draw an 9x8 'x' centered in image
	int h = (imageHeight / 2 )* 2;
	int inset = (h - 8) / 2;
	gc.drawLine( inset, inset, h - inset - 1, h - inset - 1);
	gc.drawLine( inset + 1, inset, h - inset, h - inset - 1);
	gc.drawLine( inset, h - inset - 1, h - inset - 1, inset);
	gc.drawLine( inset + 1, h - inset - 1, h - inset, inset);
	
	gc.dispose();
	
	if (maxHeight < imageHeight) {
		try {
			//rescale image
			Image temp = scaleImage(closeImage, imageHeight, maxHeight);
			closeImage.dispose();
			closeImage = temp;
		} catch (IllegalArgumentException e) {
		} catch (SWTException e) {
		}
	}
	closeItem.setImage(closeImage);
	inactiveCloseItem.setImage(closeImage);
}
private void updateArrowBar() {
	//Temporary code - need a better way to determine toolBar trim
	int toolbarTrim = 4;
	String platform = SWT.getPlatform();
	if ("photon".equals(platform)) toolbarTrim = 6; //$NON-NLS-1$
	if ("gtk".equals(platform)) toolbarTrim = 8; //$NON-NLS-1$

	int maxHeight = tabHeight - toolbarTrim;
	if (maxHeight < 3) return;
	int imageHeight = Math.max(9, maxHeight);	
	
	if (arrowLeftImage != null) {
		int height = arrowLeftImage.getBounds().height;
		if (height == imageHeight) return;
		if (imageHeight > maxHeight && height == maxHeight) return;
	}

	if (arrowBar != null) arrowBar.dispose();
	arrowBar = null;
	if (arrowLeftImage != null) arrowLeftImage.dispose();
	if (arrowRightImage != null) arrowRightImage.dispose();
	
	createArrowBar();
	ToolItem[] items = arrowBar.getItems();
	ToolItem left  = items[0];
	ToolItem right = items[1];
	
	Display display = getDisplay();
	Color foreground = getForeground();
	Color black = display.getSystemColor(SWT.COLOR_BLACK);
	Color background = getBackground();
	
	PaletteData palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), black.getRGB()});
	ImageData imageData = new ImageData(7, imageHeight, 4, palette);
	imageData.transparentPixel = 1;
	arrowLeftImage = new Image(display, imageData);
	GC gc = new GC(arrowLeftImage);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, 7, imageHeight);
	gc.setBackground(black);
	//draw a 9x5 '<' centered vertically in image
	int h = (imageHeight / 2 )* 2;
	int midpoint = h / 2 - 1;
	int[] pointArr = new int[] {6, midpoint - 5,
                                         1, midpoint, 
		                                 6,  midpoint + 5,};
	gc.fillPolygon(pointArr);
	gc.dispose();
	
	palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), black.getRGB()});
	imageData = new ImageData(7, imageHeight, 4, palette);
	imageData.transparentPixel = 1;
	arrowRightImage = new Image(display, imageData);
	gc = new GC(arrowRightImage);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, 7, imageHeight);
	gc.setBackground(black);
	//draw a 9x5 '>' centered vertically in image
	pointArr = new int[] {1, midpoint - 5, 
                                  6, midpoint,
		                          1, midpoint + 5,};
	gc.fillPolygon(pointArr);
	gc.dispose();
	
	if (maxHeight < imageHeight) {
		try {
			//rescale image
			Image leftTemp = scaleImage(arrowLeftImage, imageHeight, maxHeight);
			arrowLeftImage.dispose();
			arrowLeftImage = leftTemp;
		} catch (IllegalArgumentException e) {
		} catch (SWTException e) {
		}
		
		try {
			Image rightTemp = scaleImage(arrowRightImage, imageHeight, maxHeight);
			arrowRightImage.dispose();
			arrowRightImage = rightTemp;
		} catch (IllegalArgumentException e) {
		} catch (SWTException e) {
		}	
	}
	left.setImage(arrowLeftImage);
	right.setImage(arrowRightImage);
}

private void onMouseDoubleClick(Event event) { 
	Event e = new Event();
	e.item = getItem(new Point(event.x, event.y));
	notifyListeners(SWT.DefaultSelection, e);
}
/** 
 * A mouse button was pressed down. 
 * If a tab was hit select the tab.
 */
private void onMouseDown(Event event) {
	for (int i=0; i<items.length; i++) {
		if (items[i].getBounds().contains(new Point(event.x, event.y))) {
			if (i == selectedIndex) {
				showSelection();
				return;
			}
			forceFocus();
			setSelection(i, true);
			if (isFocusControl()) setFocus();
			return;
		}
	}
}

private void onMouseExit(Event event) {
	Rectangle inactiveBounds = inactiveCloseBar.getBounds();
	if (inactiveBounds.contains(event.x, event.y)) return;
	inactiveCloseBar.setVisible(false);
	inactiveItem = null;
	
	showToolTip = false;
	toolTipItem = null;
	if (tip != null && !tip.isDisposed() && tip.isVisible()) tip.setVisible(false);
}

private void onMouseHover(Event event) {
	if (tip == null || tip.isDisposed()) return;
	showToolTip = true;
	showToolTip(event.x, event.y);
}
private void showToolTip (int x, int y) {
	CTabItem item = getItem(new Point (x, y));
	if (item != null) {
		if (item == toolTipItem) return;
		toolTipItem = item;
		String tooltip = item.getToolTipText();
		if (tooltip != null) {			
			Display display = tip.getDisplay();
			label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
			label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
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
			Point pt = new Point(item.x + item.width / 4, item.y + item.height + 2);
			pt = toDisplay(pt);
			Rectangle rect = display.getBounds();
			Point tipSize = tip.getSize();
			pt.x = Math.max (0, Math.min (pt.x, rect.width - tipSize.x));
			pt.y = Math.max (0, Math.min (pt.y, rect.height - tipSize.y));
			tip.setLocation(pt);
			tip.setVisible(true);
			return;
		}
	}
	
	toolTipItem = null;
	if (tip != null && !tip.isDisposed() && tip.isVisible()) tip.setVisible(false);
}
private void onMouseMove(Event event) {
	if (showToolTip) {
		showToolTip(event.x, event.y);
	}
	
	if (!showClose) return;
	
	CTabItem item = null;
	for (int i=0; i<items.length; i++) {
		Rectangle rect = items[i].getBounds();
		if (rect.contains(new Point(event.x, event.y))) {
			item = items[i];
			break;
		}
	}
	if (item == inactiveItem) return;
	
	inactiveCloseBar.setVisible(false);
	inactiveItem = null;
		
	if (item == null || item == getSelection()) return;

	int toolbarHeight = tabHeight - CTabItem.TOP_MARGIN - CTabItem.BOTTOM_MARGIN + 2; // +2 to ignore gap between focus rectangle
	Point size = inactiveCloseBar.computeSize(SWT.DEFAULT, toolbarHeight);
	int x = item.x + item.width - size.x - 2; // -2 to not overlap focus rectangle and trim
	int y = item.y + Math.max(0, (item.height - toolbarHeight)/2);
	Rectangle toolspace = getToolSpace();
	Point folderSize = getSize();
	if ((toolspace.width == 0 || x < toolspace.x) && x + size.x < folderSize.x - borderRight) {
		inactiveCloseBar.setBounds(x, y, size.x, toolbarHeight);
		inactiveCloseBar.setVisible(true);
		inactiveItem = item;
	}
}
private void onTraverse (Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
// TEMPORARY CODE See bug report 17372
//		case SWT.TRAVERSE_RETURN:
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
			if (event.doit) event.detail = SWT.TRAVERSE_NONE;
			break;
	}
}

private boolean onPageTraversal(Event event) {
	int count = getItemCount ();
	if (count == 0) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = (event.detail == SWT.TRAVERSE_PAGE_NEXT) ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	return true;
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
	// only show Scroll buttons if there is more than one item
	// and if we are not already at the last item
	if (items.length < 2) return false;
	Rectangle area = getClientArea();
	int rightEdge = area.x + area.width;
	if (rightEdge <= 0) return false;
	if (topTabIndex > 0) {
		rightEdge -=  arrowBar.getSize().x;
	}
	if (topRight != null) {
		rightEdge -= topRight.getSize().x;
	}
	CTabItem item = items[items.length-1];
	return (item.x + item.width > rightEdge);
}

/**
 * Scroll the tab items to the left.
 */
private void scroll_scrollLeft() {
	if (items.length == 0) return;
	setLastItem(topTabIndex - 1);
}

/**
 * Scroll the tab items to the right.
 */
private void scroll_scrollRight() {
	int lastIndex = getLastItem();
	topTabIndex = lastIndex + 1;
	setItemLocation();
	correctLastItem();
	redrawTabArea(-1);
}
private boolean correctLastItem() {
	Rectangle area = getClientArea();
	int rightEdge = area.x + area.width;
	if (rightEdge <= 0) return false;
	Rectangle toolspace = getToolSpace();
	if (toolspace.width > 0) {
		rightEdge -= toolspace.width;
	}
	CTabItem item = items[items.length - 1];
	if (item.x + item.width < rightEdge) {
		setLastItem(items.length - 1);
		return true;
	}
	return false;
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
	fixedTabHeight = true;
	if (tabHeight == height) return;
	tabHeight = height;
	oldSize = null;
	notifyListeners(SWT.Resize, new Event());
}
void resetTabSize(boolean checkHeight){
	int oldHeight = tabHeight;
	if (!fixedTabHeight && checkHeight) {
		int tempHeight = 0;
		GC gc = new GC(this);
		for (int i=0; i < items.length; i++) {
			tempHeight = Math.max(tempHeight, items[i].preferredHeight(gc));
		}
		gc.dispose();
		if (topRight != null) tempHeight = Math.max(tempHeight, topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		tabHeight =  tempHeight;
	}
		
	if (tabHeight != oldHeight){
		oldSize = null;
		notifyListeners(SWT.Resize, new Event());
	} else {
		setItemBounds();
		redraw();
	}
}
}
