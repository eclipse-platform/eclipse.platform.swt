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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * 
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
 * <dd>"CTabFolder2"</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#ctabfolder">CTabFolder, CTabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: CustomControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
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
	 * NOTE This field is badly named and can not be fixed for backwards compatibility.
	 * It should not be capitalized.
	 * 
	 * @deprecated This field is no longer used.  See setMinimumCharacters(int)
	 */
	public int MIN_TAB_WIDTH = 4;
	
	/**
	 * Color of innermost line of drop shadow border.
	 * 
	 * NOTE This field is badly named and can not be fixed for backwards compatibility.
	 * It should be capitalized.
	 * 
	 * @deprecated drop shadow border is no longer drawn in 3.0
	 */
	public static RGB borderInsideRGB  = new RGB (132, 130, 132);
	/**
	 * Color of middle line of drop shadow border.
	 * 
	 * NOTE This field is badly named and can not be fixed for backwards compatibility.
	 * It should be capitalized.
	 * 
	 * @deprecated drop shadow border is no longer drawn in 3.0
	 */
	public static RGB borderMiddleRGB  = new RGB (143, 141, 138);
	/**
	 * Color of outermost line of drop shadow border.
	 * 
	 * NOTE This field is badly named and can not be fixed for backwards compatibility.
	 * It should be capitalized.
	 * 
	 * @deprecated drop shadow border is no longer drawn in 3.0
	 */
	public static RGB borderOutsideRGB = new RGB (171, 168, 165); 

	/* sizing, positioning */
	boolean onBottom = false;
	boolean single = false;
	boolean simple = true;
	int fixedTabHeight = SWT.DEFAULT;
	int tabHeight;
	int minChars = 20;
	boolean borderVisible = false;
	
	/* item management */
	CTabFolderRenderer renderer;
	CTabItem items[] = new CTabItem[0];
	int firstIndex = -1; // index of the left most visible tab.
	int selectedIndex = -1;
	int[] priority = new int[0];
	boolean mru = false;
	Listener listener;
	boolean ignoreTraverse;
	
	/* External Listener management */
	CTabFolder2Listener[] folderListeners = new CTabFolder2Listener[0];
	// support for deprecated listener mechanism
	CTabFolderListener[] tabListeners = new CTabFolderListener[0];
	
	/* Selected item appearance */
	Image selectionBgImage;
	Color[] selectionGradientColors;
	int[] selectionGradientPercents;
	boolean selectionGradientVertical;
	Color selectionForeground;
	Color selectionBackground;
	
	/* Unselected item appearance */
	Color[] gradientColors; 
	int[] gradientPercents;
	boolean gradientVertical;
	boolean showUnselectedImage = true;
	
	// close, min/max and chevron buttons
	boolean showClose = false;
	boolean showUnselectedClose = true;
	
	Rectangle chevronRect = new Rectangle(0, 0, 0, 0);
	int chevronImageState = SWT.NONE;
	boolean showChevron = false;
	Menu showMenu;
	
	boolean showMin = false;
	Rectangle minRect = new Rectangle(0, 0, 0, 0);
	boolean minimized = false;
	int minImageState = SWT.NONE;
	
	boolean showMax = false;
	Rectangle maxRect = new Rectangle(0, 0, 0, 0);
	boolean maximized = false;
	int maxImageState = SWT.NONE;
	
	Control topRight;
	Rectangle topRightRect = new Rectangle(0, 0, 0, 0);
	int topRightAlignment = SWT.RIGHT;
	
	// when disposing CTabFolder, don't try to layout the items or 
	// change the selection as each child is destroyed.
	boolean inDispose = false;

	// keep track of size changes in order to redraw only affected area
	// on Resize
	Point oldSize;
	Font oldFont;
	
	// internal constants
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;
	
	static final int SELECTION_FOREGROUND = SWT.COLOR_LIST_FOREGROUND;
	static final int SELECTION_BACKGROUND = SWT.COLOR_LIST_BACKGROUND;
	
	static final int FOREGROUND = SWT.COLOR_WIDGET_FOREGROUND;
	static final int BACKGROUND = SWT.COLOR_WIDGET_BACKGROUND;
	
	static final int CHEVRON_CHILD_ID = 0;
	static final int MINIMIZE_CHILD_ID = 1;
	static final int MAXIMIZE_CHILD_ID = 2;
	static final int EXTRA_CHILD_ID_COUNT = 3;
	
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
	super(parent, checkStyle (parent, style));
	init(style);
}

void init(int style) {
	super.setLayout(new CTabFolderLayout());
	int style2 = super.getStyle();
	oldFont = getFont();
	onBottom = (style2 & SWT.BOTTOM) != 0;
	showClose = (style2 & SWT.CLOSE) != 0;
//	showMin = (style2 & SWT.MIN) != 0; - conflicts with SWT.TOP
//	showMax = (style2 & SWT.MAX) != 0; - conflicts with SWT.BOTTOM
	single = (style2 & SWT.SINGLE) != 0;
	borderVisible = (style & SWT.BORDER) != 0;
	//set up default colors
	Display display = getDisplay();
	selectionForeground = display.getSystemColor(SELECTION_FOREGROUND);
	selectionBackground = display.getSystemColor(SELECTION_BACKGROUND);
	renderer = new CTabFolderRenderer(this);
	updateTabHeight(false);
	
	// Add all listeners
	listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:          onDispose(event); break;
				case SWT.DragDetect:       onDragDetect(event); break;
				case SWT.FocusIn:          onFocus(event);	break;
				case SWT.FocusOut:         onFocus(event);	break;
				case SWT.KeyDown:          onKeyDown(event); break;
				case SWT.MouseDoubleClick: onMouseDoubleClick(event); break;
				case SWT.MouseDown:        onMouse(event);	break;
				case SWT.MouseEnter:       onMouse(event);	break;
				case SWT.MouseExit:        onMouse(event);	break;
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
		SWT.DragDetect,
		SWT.FocusIn, 
		SWT.FocusOut, 
		SWT.KeyDown,
		SWT.MouseDoubleClick, 
		SWT.MouseDown,
		SWT.MouseEnter, 
		SWT.MouseExit, 
		SWT.MouseMove,
		SWT.MouseUp,
		SWT.Paint,
		SWT.Resize,  
		SWT.Traverse,
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}
	
	initAccessible();
}
static int checkStyle (Composite parent, int style) {
	int mask = SWT.CLOSE | SWT.TOP | SWT.BOTTOM | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT | SWT.SINGLE | SWT.MULTI;
	style = style & mask;
	// TOP and BOTTOM are mutually exclusive.
	// TOP is the default
	if ((style & SWT.TOP) != 0) style = style & ~SWT.BOTTOM;
	// SINGLE and MULTI are mutually exclusive.
	// MULTI is the default
	if ((style & SWT.MULTI) != 0) style = style & ~SWT.SINGLE;
	// reduce the flash by not redrawing the entire area on a Resize event
	style |= SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED;

	return style;
}

/**
 * 
 * Adds the listener to the collection of listeners who will
 * be notified when a tab item is closed, minimized, maximized,
 * restored, or to show the list of items that are not 
 * currently visible.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see CTabFolder2Listener
 * @see #removeCTabFolder2Listener(CTabFolder2Listener)
 * 
 * @since 3.0
 */
public void addCTabFolder2Listener(CTabFolder2Listener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolder2Listener[] newListeners = new CTabFolder2Listener[folderListeners.length + 1];
	System.arraycopy(folderListeners, 0, newListeners, 0, folderListeners.length);
	folderListeners = newListeners;
	folderListeners[folderListeners.length - 1] = listener;
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
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see CTabFolderListener
 * @see #removeCTabFolderListener(CTabFolderListener)
 * 
 * @deprecated use addCTabFolder2Listener(CTabFolder2Listener)
 */
public void addCTabFolderListener(CTabFolderListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// add to array
	CTabFolderListener[] newTabListeners = new CTabFolderListener[tabListeners.length + 1];
	System.arraycopy(tabListeners, 0, newTabListeners, 0, tabListeners.length);
	tabListeners = newTabListeners;
	tabListeners[tabListeners.length - 1] = listener;
	// display close button to be backwards compatible
	if (!showClose) {
		showClose = true;
		updateItems();
		redraw();
	}
}
/**	 
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the selected tab.
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
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}

/*
* This class was not intended to be subclassed but this restriction
* cannot be enforced without breaking backward compatibility.
*/
//protected void checkSubclass () {
//	String name = getClass ().getName ();
//	int index = name.lastIndexOf ('.');
//	if (!name.substring (0, index + 1).equals ("org.eclipse.swt.custom.")) {
//		SWT.error (SWT.ERROR_INVALID_SUBCLASS);
//	}
//}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	return renderer.computeTrim(CTabFolderRenderer.PART_BODY, SWT.NONE, x, y, width, height);
}
void createItem (CTabItem item, int index) {
	if (0 > index || index > getItemCount ())SWT.error (SWT.ERROR_INVALID_RANGE);
	item.parent = this;
	CTabItem[] newItems = new CTabItem [items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index);
	items = newItems;
	if (selectedIndex >= index) selectedIndex ++;	
	int[] newPriority = new int[priority.length + 1];
	int next = 0,  priorityIndex = priority.length;
	for (int i = 0; i < priority.length; i++) {
		if (!mru && priority[i] == index) {
			priorityIndex = next++;
		}
		newPriority[next++] = priority[i] >= index ? priority[i] + 1 : priority[i];
	}
	newPriority[priorityIndex] = index;
	priority = newPriority;
	
	if (items.length == 1) {
		if (!updateTabHeight(false)) updateItems();
		redraw();
	} else {
		updateItems();
		redrawTabs();
	}
}
void destroyItem (CTabItem item) {
	if (inDispose) return;
	int index = indexOf(item);
	if (index == -1) return;
	
	if (items.length == 1) {
		items = new CTabItem[0];
		priority = new int[0];
		firstIndex = -1;
		selectedIndex = -1;
		
		Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
		setToolTipText(null);
		GC gc = new GC(this);
		setButtonBounds(gc);
		gc.dispose();
		redraw();
		return;
	} 
		
	CTabItem[] newItems = new CTabItem [items.length - 1];
	System.arraycopy(items, 0, newItems, 0, index);
	System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
	items = newItems;
	
	int[] newPriority = new int[priority.length - 1];
	int next = 0;
	for (int i = 0; i < priority.length; i++) {
		if (priority [i] == index) continue; 
		newPriority[next++] = priority[i] > index ? priority[i] - 1 : priority [i];
	}
	priority = newPriority;
	
	// move the selection if this item is selected
	if (selectedIndex == index) {
		Control control = item.getControl();
		selectedIndex = -1;
		int nextSelection = mru ? priority[0] : Math.max(0, index - 1);
		setSelection(nextSelection, true);
		if (control != null && !control.isDisposed()) {
			control.setVisible(false);
		}
	} else if (selectedIndex > index) {
		selectedIndex --;
	}
	
	updateItems();
	redrawTabs();
}

/**
 * Returns <code>true</code> if the receiver's border is visible.
 *
 * @return the receiver's border visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getBorderVisible() {
	checkWidget();
	return borderVisible;
}
public Rectangle getClientArea() {
	checkWidget();
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_BODY, SWT.NONE, 0, 0, 0, 0);
	if (minimized) return new Rectangle(-trim.x, -trim.y, 0, 0);
	Point size = getSize();
	int width = size.x - trim.width;
	int height = size.y - trim.height;
	return new Rectangle(-trim.x, -trim.y, width, height);
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
 * @exception SWTException <ul>
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
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public CTabItem getItem (Point pt) {
	//checkWidget();
	if (items.length == 0) return null;
	Point size = getSize();
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0);
	if (size.x <= trim.width) return null;
	if (showChevron && chevronRect.contains(pt)) return null;
	for (int i = 0; i < priority.length; i++) {
		CTabItem item = items[priority[i]];
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
 * @exception SWTException <ul>
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
 * @exception SWTException <ul>
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

/*
 * Return the lowercase of the first non-'&' character following
 * an '&' character in the given string. If there are no '&'
 * characters in the given string, return '\0'.
 */
char _findMnemonic (String string) {
	if (string == null) return '\0';
	int index = 0;
	int length = string.length ();
	do {
		while (index < length && string.charAt (index) != '&') index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return Character.toLowerCase (string.charAt (index));
		index++;
	} while (index < length);
 	return '\0';
}
String stripMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while ((index < length) && (string.charAt (index) != '&')) index++;
		if (++index >= length) return string;
		if (string.charAt (index) != '&') {
			return string.substring(0, index-1) + string.substring(index, length);
		}
		index++;
	} while (index < length);
 	return string;
}
/**
 * Returns <code>true</code> if the receiver is minimized.
 *
 * @return the receiver's minimized state
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
 * Returns <code>true</code> if the minimize button
 * is visible.
 *
 * @return the visibility of the minimized button
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getMinimizeVisible() {
	checkWidget();
	return showMin;
}
/** 
 * Returns the number of characters that will
 * appear in a fully compressed tab.
 * 
 * @return number of characters that will appear in a fully compressed tab
 * 
 * @since 3.0
 */
public int getMinimumCharacters() {
	checkWidget();
	return minChars;
}

/**
 * Returns <code>true</code> if the receiver is maximized.
 * <p>
 *
 * @return the receiver's maximized state
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
/**
 * Returns <code>true</code> if the maximize button
 * is visible.
 *
 * @return the visibility of the maximized button
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getMaximizeVisible() {
	checkWidget();
	return showMax;
}
/**
 * Returns <code>true</code> if the receiver displays most
 * recently used tabs and <code>false</code> otherwise.
 * <p>
 * When there is not enough horizontal space to show all the tabs,
 * by default, tabs are shown sequentially from left to right in 
 * order of their index.  When the MRU visibility is turned on,
 * the tabs that are visible will be the tabs most recently selected.
 * Tabs will still maintain their left to right order based on index 
 * but only the most recently selected tabs are visible.
 * <p>
 * For example, consider a CTabFolder that contains "Tab 1", "Tab 2",
 * "Tab 3" and "Tab 4" (in order by index).  The user selects
 * "Tab 1" and then "Tab 3".  If the CTabFolder is now
 * compressed so that only two tabs are visible, by default, 
 * "Tab 2" and "Tab 3" will be shown ("Tab 3" since it is currently 
 * selected and "Tab 2" because it is the previous item in index order).
 * If MRU visibility is enabled, the two visible tabs will be "Tab 1"
 * and "Tab 3" (in that order from left to right).</p>
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
public boolean getMRUVisible() {
	checkWidget();
	return mru;
}
/**
 * Returns the receiver's renderer.
 *
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.6
 */
public CTabFolderRenderer getRenderer() {
	checkWidget();
	return renderer;
}
int getRightItemEdge (GC gc){
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0);
	int x = getSize().x - (trim.width + trim.x) - 3; //TODO: add setter for spacing?
	if (showMin) x -= renderer.computeSize(CTabFolderRenderer.PART_MIN_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
	if (showMax) x -= renderer.computeSize(CTabFolderRenderer.PART_MAX_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
	if (showChevron) x -= renderer.computeSize(CTabFolderRenderer.PART_CHEVRON_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
	if (topRight != null && topRightAlignment != SWT.FILL) {
		Point rightSize = topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		x -= rightSize.x + 3;
	}
	return Math.max(0, x);
}
/**
 * Return the selected tab item, or null if there is no selection.
 * 
 * @return the selected tab item, or null if none has been selected
 * 
 * @exception SWTException <ul>
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
 * Returns the receiver's selection background color.
 *
 * @return the selection background color of the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getSelectionBackground() {
	checkWidget();
	return selectionBackground;
}
/**
 * Returns the receiver's selection foreground color.
 *
 * @return the selection foreground color of the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getSelectionForeground() {
	checkWidget();
	return selectionForeground;
}
/**
 * Return the index of the selected tab item, or -1 if there
 * is no selection.
 * 
 * @return the index of the selected tab item or -1
 * 
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getSelectionIndex() {
	//checkWidget();
	return selectedIndex;
}
/**
 * Returns <code>true</code> if the CTabFolder is rendered
 * with a simple, traditional shape.
 * 
 * @return <code>true</code> if the CTabFolder is rendered with a simple shape
 * 
 * @since 3.0
 */
public boolean getSimple() {
	checkWidget();
	return simple;
}
/**
 * Returns <code>true</code> if the CTabFolder only displays the selected tab
 * and <code>false</code> if the CTabFolder displays multiple tabs.
 * 
 * @return <code>true</code> if the CTabFolder only displays the selected tab and <code>false</code> if the CTabFolder displays multiple tabs
 * 
 * @since 3.0
 */
public boolean getSingle() {
	checkWidget();
	return single;
}

public int getStyle() {
	int style = super.getStyle();
	style &= ~(SWT.TOP | SWT.BOTTOM);
	style |= onBottom ? SWT.BOTTOM : SWT.TOP;
	style &= ~(SWT.SINGLE | SWT.MULTI);
	style |= single ? SWT.SINGLE : SWT.MULTI;
	if (borderVisible) style |= SWT.BORDER;
	style &= ~SWT.CLOSE;
	if (showClose) style |= SWT.CLOSE;
	return style;
}
/**
 * Returns the height of the tab
 * 
 * @return the height of the tab
 * 
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getTabHeight(){
	checkWidget();
	if (fixedTabHeight != SWT.DEFAULT) return fixedTabHeight;
	return tabHeight - 1; // -1 for line drawn across top of tab //TODO: replace w/ computeTrim of tab area?
}
/**
 * Returns the position of the tab.  Possible values are SWT.TOP or SWT.BOTTOM.
 * 
 * @return the position of the tab
 * 
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public int getTabPosition(){
	checkWidget();
	return onBottom ? SWT.BOTTOM : SWT.TOP;
}
/**
 * Returns the control in the top right corner of the tab folder. 
 * Typically this is a close button or a composite with a menu and close button.
 *
 * @return the control in the top right corner of the tab folder or null
 * 
 * @exception  SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 2.1
 */
public Control getTopRight() {
	checkWidget();
	return topRight;
}
/**
 * Returns the alignment of the top right control. 
 *
 * @return the alignment of the top right control which is either
 * <code>SWT.RIGHT</code> or <code>SWT.FILL</code> 
 * 
 * @exception  SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.6
 */
public int getTopRightAlignment() {
	checkWidget();
	return topRightAlignment;
}
/**
 * Returns <code>true</code> if the close button appears 
 * when the user hovers over an unselected tabs.
 * 
 * @return <code>true</code> if the close button appears on unselected tabs
 * 
 * @since 3.0
 */
public boolean getUnselectedCloseVisible() {
	checkWidget();
	return showUnselectedClose;
}
/**
 * Returns <code>true</code> if an image appears 
 * in unselected tabs.
 * 
 * @return <code>true</code> if an image appears in unselected tabs
 * 
 * @since 3.0
 */
public boolean getUnselectedImageVisible() {
	checkWidget();
	return showUnselectedImage;
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
 * @exception SWTException <ul>
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
				name = stripMnemonic(items[childID].getText());
			} else if (childID == items.length + CHEVRON_CHILD_ID) {
				name = SWT.getMessage("SWT_ShowList"); //$NON-NLS-1$
			} else if (childID == items.length + MINIMIZE_CHILD_ID) {
				name = minimized ? SWT.getMessage("SWT_Restore") : SWT.getMessage("SWT_Minimize"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (childID == items.length + MAXIMIZE_CHILD_ID) {
				name = maximized ? SWT.getMessage("SWT_Restore") : SWT.getMessage("SWT_Maximize"); //$NON-NLS-1$ //$NON-NLS-2$
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
					char mnemonic = _findMnemonic(text);	
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
			Point testPoint = toControl(e.x, e.y);
			int childID = ACC.CHILDID_NONE;
			for (int i = 0; i < items.length; i++) {
				if (items[i].getBounds().contains(testPoint)) {
					childID = i;
					break;
				}
			}
			if (childID == ACC.CHILDID_NONE) {
				if (showChevron && chevronRect.contains(testPoint)) {
					childID = items.length + CHEVRON_CHILD_ID;
				} else if (showMin && minRect.contains(testPoint)) {
					childID = items.length + MINIMIZE_CHILD_ID;
				} else if (showMax && maxRect.contains(testPoint)) {
					childID = items.length + MAXIMIZE_CHILD_ID;
				} else {
					Rectangle location = getBounds();
					location.height = location.height - getClientArea().height;
					if (location.contains(testPoint)) {
						childID = ACC.CHILDID_SELF;
					}
				}
			}
			e.childID = childID;
		}

		public void getLocation(AccessibleControlEvent e) {
			Rectangle location = null;
			Point pt = null;
			int childID = e.childID;
			if (childID == ACC.CHILDID_SELF) {
				location = getBounds();
				pt = getParent().toDisplay(location.x, location.y);
			} else {
				if (childID >= 0 && childID < items.length && items[childID].isShowing()) {
					location = items[childID].getBounds();
				} else if (showChevron && childID == items.length + CHEVRON_CHILD_ID) {
					location = chevronRect;
				} else if (showMin && childID == items.length + MINIMIZE_CHILD_ID) {
					location = minRect;
				} else if (showMax && childID == items.length + MAXIMIZE_CHILD_ID) {
					location = maxRect;
				}
				if (location != null) {
					pt = toDisplay(location.x, location.y);
				}
			}
			if (location != null && pt != null) {
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}
		}
		
		public void getChildCount(AccessibleControlEvent e) {
			e.detail = items.length + EXTRA_CHILD_ID_COUNT;
		}
		
		public void getDefaultAction(AccessibleControlEvent e) {
			String action = null;
			int childID = e.childID;
			if (childID >= 0 && childID < items.length) {
				action = SWT.getMessage ("SWT_Switch"); //$NON-NLS-1$
			}
			if (childID >= items.length && childID < items.length + EXTRA_CHILD_ID_COUNT) {
				action = SWT.getMessage ("SWT_Press"); //$NON-NLS-1$
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
			} else if (childID >= items.length && childID < items.length + EXTRA_CHILD_ID_COUNT) {
				role = ACC.ROLE_PUSHBUTTON;
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
			} else if (childID == items.length + CHEVRON_CHILD_ID) {
				state = showChevron ? ACC.STATE_NORMAL : ACC.STATE_INVISIBLE;
			} else if (childID == items.length + MINIMIZE_CHILD_ID) {
				state = showMin ? ACC.STATE_NORMAL : ACC.STATE_INVISIBLE;
			} else if (childID == items.length + MAXIMIZE_CHILD_ID) {
				state = showMax ? ACC.STATE_NORMAL : ACC.STATE_INVISIBLE;
			}
			e.detail = state;
		}
		
		public void getChildren(AccessibleControlEvent e) {
			int childIdCount = items.length + EXTRA_CHILD_ID_COUNT;
			Object[] children = new Object[childIdCount];
			for (int i = 0; i < childIdCount; i++) {
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
void onKeyDown (Event event) {
	switch (event.keyCode) {
		case SWT.ARROW_LEFT:
		case SWT.ARROW_RIGHT:
			int count = items.length;
			if (count == 0) return;
			if (selectedIndex  == -1) return;
			int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
			int offset =  event.keyCode == leadKey ? -1 : 1;
			int index;
			if (!mru) {
				index = selectedIndex + offset;
			} else {
				int[] visible = new int[items.length];
				int idx = 0;
				int current = -1;
				for (int i = 0; i < items.length; i++) {
					if (items[i].showing) {
						if (i == selectedIndex) current = idx;
						visible [idx++] = i;
					}
				}
				if (current + offset >= 0 && current + offset < idx){
					index = visible [current + offset];
				} else {
					if (showChevron) {
						CTabFolderEvent e = new CTabFolderEvent(this);
						e.widget = this;
						e.time = event.time;
						e.x = chevronRect.x;
						e.y = chevronRect.y;
						e.width = chevronRect.width;
						e.height = chevronRect.height;
						e.doit = true;
						for (int i = 0; i < folderListeners.length; i++) {
							folderListeners[i].showList(e);
						}
						if (e.doit && !isDisposed()) {
							showList(chevronRect);
						}
					}
					return;
				}
			}
			if (index < 0 || index >= count) return;
			setSelection (index, true);
			forceFocus();
	}
}
void onDispose(Event event) {
	removeListener(SWT.Dispose, listener);
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;
	/*
	 * Usually when an item is disposed, destroyItem will change the size of the items array, 
	 * reset the bounds of all the tabs and manage the widget associated with the tab.
	 * Since the whole folder is being disposed, this is not necessary.  For speed
	 * the inDispose flag is used to skip over this part of the item dispose.
	 */
	inDispose = true;

	if (showMenu != null && !showMenu.isDisposed()) {
		showMenu.dispose();
		showMenu = null;
	}
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
	
	if (renderer != null) renderer.dispose();
	renderer = null;
}
void onDragDetect(Event event) {
	boolean consume = false;
	if (chevronRect.contains(event.x, event.y) ||
	    minRect.contains(event.x, event.y) ||
		maxRect.contains(event.x, event.y)){
		consume = true;
	} else {
		for (int i = 0; i < items.length; i++) {
			if (items[i].closeRect.contains(event.x, event.y)) {
					consume = true;
					break;
			}
		}
	}
	if (consume) {
		event.type = SWT.None;
	}
}
void onFocus(Event event) {
	checkWidget();
	if (selectedIndex >= 0) {
		redraw();
	} else {
		setSelection(0, true);
	}
}
boolean onMnemonic (Event event, boolean doit) {
	char key = event.character;
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			char mnemonic = _findMnemonic (items[i].getText ());
			if (mnemonic != '\0') {
				if (Character.toLowerCase (key) == mnemonic) {
					if (doit) {
					    setSelection(i, true);
					    forceFocus();
					}
					return true;
				}
			}
		}
	}
	return false;
}
void onMouseDoubleClick(Event event) {	
	if (event.button != 1 || 
		(event.stateMask & SWT.BUTTON2) != 0 || 
		(event.stateMask & SWT.BUTTON3) != 0) return;
	Event e = new Event();
	e.item = getItem(new Point(event.x, event.y));
	if (e.item != null) {
		notifyListeners(SWT.DefaultSelection, e);
	}
}
void onMouse(Event event) {
	int x = event.x, y = event.y;
	switch (event.type) {
		case SWT.MouseEnter: {
			setToolTipText(null);
			break;
		}
		case SWT.MouseExit: {
			if (minImageState != SWT.NONE) {
				minImageState = SWT.NONE;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
			}
			if (maxImageState != SWT.NONE) {
				maxImageState = SWT.NONE;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
			}
			if (chevronImageState != SWT.NONE) {
				chevronImageState = SWT.NONE;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
			}
			for (int i=0; i<items.length; i++) {
				CTabItem item = items[i];
				if (i != selectedIndex && item.closeImageState != SWT.BACKGROUND) {
					item.closeImageState = SWT.BACKGROUND;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
				}
				if (i == selectedIndex && item.closeImageState != SWT.NONE) {
					item.closeImageState = SWT.NONE;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
				}
			}
			break;
		}
		case SWT.MouseDown: {
			if (event.button != 1) return;
			if (minRect.contains(x, y)) {
				minImageState = SWT.SELECTED;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				update();
				return;
			}
			if (maxRect.contains(x, y)) {
				maxImageState = SWT.SELECTED;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				update();
				return;
			}
			if (chevronRect.contains(x, y)) {
				if (chevronImageState != SWT.HOT) {
					chevronImageState = SWT.HOT;
				} else {
					chevronImageState = SWT.SELECTED;
				}
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				update();
				return;
			}
			CTabItem item = null;
			if (single) {
				if (selectedIndex != -1) {
					Rectangle bounds = items[selectedIndex].getBounds();
					if (bounds.contains(x, y)){
						item = items[selectedIndex];
					}
				}
			} else {
				for (int i=0; i<items.length; i++) {
					Rectangle bounds = items[i].getBounds();
					if (bounds.contains(x, y)){
						item = items[i];
					}
				}
			}
			if (item != null) {
				if (item.closeRect.contains(x,y)){
					item.closeImageState = SWT.SELECTED;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					update();
					return;
				}
				int index = indexOf(item);
				if (item.showing){
				    	int oldSelectedIndex = selectedIndex;
				    	setSelection(index, true);
				    	if (oldSelectedIndex == selectedIndex) {
				    	    /* If the click is on the selected tabitem, then set focus to the tabfolder */
				    	    forceFocus();
				    	}
				}
				return;
			}
			break;
		}
		case SWT.MouseMove: {
			_setToolTipText(event.x, event.y);
			boolean close = false, minimize = false, maximize = false, chevron = false;
			if (minRect.contains(x, y)) {
				minimize = true;
				if (minImageState != SWT.SELECTED && minImageState != SWT.HOT) {
					minImageState = SWT.HOT;
					redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				}
			}
			if (maxRect.contains(x, y)) {
				maximize = true;
				if (maxImageState != SWT.SELECTED && maxImageState != SWT.HOT) {
					maxImageState = SWT.HOT;
					redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				}
			}
			if (chevronRect.contains(x, y)) {
				chevron = true;
				if (chevronImageState != SWT.SELECTED && chevronImageState != SWT.HOT) {
					chevronImageState = SWT.HOT;
					redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
				}
			}
			if (minImageState != SWT.NONE && !minimize) {
				minImageState = SWT.NONE;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
			}
			if (maxImageState != SWT.NONE && !maximize) {
				maxImageState = SWT.NONE;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
			}
			if (chevronImageState != SWT.NONE && !chevron) {
				chevronImageState = SWT.NONE;
				redraw(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, false);
			}
			for (int i=0; i<items.length; i++) {
				CTabItem item = items[i];
				close = false;
				if (item.getBounds().contains(x, y)) {
					close = true;
					if (item.closeRect.contains(x, y)) {
						if (item.closeImageState != SWT.SELECTED && item.closeImageState != SWT.HOT) {
							item.closeImageState = SWT.HOT;
							redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
						}
					} else {
						if (item.closeImageState != SWT.NONE) {
							item.closeImageState = SWT.NONE;
							redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
						}
					}
				} 
				if (i != selectedIndex && item.closeImageState != SWT.BACKGROUND && !close) {
					item.closeImageState = SWT.BACKGROUND;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
				}
				if (i == selectedIndex && item.closeImageState != SWT.NONE && !close) {
					item.closeImageState = SWT.NONE;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
				}
			}
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			if (chevronRect.contains(x, y)) {
				boolean selected = chevronImageState == SWT.SELECTED;
				if (!selected) return;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				e.x = chevronRect.x;
				e.y = chevronRect.y;
				e.width = chevronRect.width;
				e.height = chevronRect.height;
				e.doit = true;
				for (int i = 0; i < folderListeners.length; i++) {
					folderListeners[i].showList(e);
				}
				if (e.doit && !isDisposed()) {
					showList(chevronRect);
				}
				return;
			}
			if (minRect.contains(x, y)) {
				boolean selected = minImageState == SWT.SELECTED;
				minImageState = SWT.HOT;
				redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
				if (!selected) return;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				for (int i = 0; i < folderListeners.length; i++) {
					if (minimized) {
						folderListeners[i].restore(e);
					} else {
						folderListeners[i].minimize(e);
					}
				}
				return;
			}
			if (maxRect.contains(x, y)) {
				boolean selected = maxImageState == SWT.SELECTED;
				maxImageState = SWT.HOT;
				redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
				if (!selected) return;
				CTabFolderEvent e = new CTabFolderEvent(this);
				e.widget = this;
				e.time = event.time;
				for (int i = 0; i < folderListeners.length; i++) {
					if (maximized) {
						folderListeners[i].restore(e);
					} else {
						folderListeners[i].maximize(e);
					}
				}
				return;
			}
			CTabItem item = null;
			if (single) {
				if (selectedIndex != -1) {
					Rectangle bounds = items[selectedIndex].getBounds();
					if (bounds.contains(x, y)){
						item = items[selectedIndex];
					}
				}
			} else {
				for (int i=0; i<items.length; i++) {
					Rectangle bounds = items[i].getBounds();
					if (bounds.contains(x, y)){
						item = items[i];
					}
				}
			}
			if (item != null) {
				if (item.closeRect.contains(x,y)) {
					boolean selected = item.closeImageState == SWT.SELECTED;
					item.closeImageState = SWT.HOT;
					redraw(item.closeRect.x, item.closeRect.y, item.closeRect.width, item.closeRect.height, false);
					if (!selected) return;
					CTabFolderEvent e = new CTabFolderEvent(this);
					e.widget = this;
					e.time = event.time;
					e.item = item;
					e.doit = true;
					for (int j = 0; j < folderListeners.length; j++) {
						CTabFolder2Listener listener = folderListeners[j];
						listener.close(e);
					}
					for (int j = 0; j < tabListeners.length; j++) {
						CTabFolderListener listener = tabListeners[j];
						listener.itemClosed(e);
					}
					if (e.doit) item.dispose();
					if (!isDisposed() && item.isDisposed()) {
						Display display = getDisplay();
						Point pt = display.getCursorLocation();
						pt = display.map(null, this, pt.x, pt.y);
						CTabItem nextItem = getItem(pt);
						if (nextItem != null) {
							if (nextItem.closeRect.contains(pt)) {
								if (nextItem.closeImageState != SWT.SELECTED && nextItem.closeImageState != SWT.HOT) {
									nextItem.closeImageState = SWT.HOT;
									redraw(nextItem.closeRect.x, nextItem.closeRect.y, nextItem.closeRect.width, nextItem.closeRect.height, false);
								}
							} else {
								if (nextItem.closeImageState != SWT.NONE) {
									nextItem.closeImageState = SWT.NONE;
									redraw(nextItem.closeRect.x, nextItem.closeRect.y, nextItem.closeRect.width, nextItem.closeRect.height, false);
								}
							} 
						}
					}
					return;
				}
			}
		}
	}
}
void onPageTraversal(Event event) {
	int count = items.length;
	if (count == 0) return;
	int index = selectedIndex; 
	if (index  == -1) {
		index = 0;
	} else {
		int offset = (event.detail == SWT.TRAVERSE_PAGE_NEXT) ? 1 : -1;
		if (!mru) {
			index = (selectedIndex + offset + count) % count;
		} else {
			int[] visible = new int[items.length];
			int idx = 0;
			int current = -1;
			for (int i = 0; i < items.length; i++) {
				if (items[i].showing) {
					if (i == selectedIndex) current = idx;
					visible [idx++] = i;
				}
			}
			if (current + offset >= 0 && current + offset < idx){
				index = visible [current + offset];
			} else {
				if (showChevron) {
					CTabFolderEvent e = new CTabFolderEvent(this);
					e.widget = this;
					e.time = event.time;
					e.x = chevronRect.x;
					e.y = chevronRect.y;
					e.width = chevronRect.width;
					e.height = chevronRect.height;
					e.doit = true;
					for (int i = 0; i < folderListeners.length; i++) {
						folderListeners[i].showList(e);
					}
					if (e.doit && !isDisposed()) {
						showList(chevronRect);
					}
				}
			}
		}
	}
	setSelection (index, true);
}
void onPaint(Event event) {
	if (inDispose) return;
	Font font = getFont();
	if (oldFont == null || !oldFont.equals(font)) {
		// handle case where  default font changes
		oldFont = font;
		if (!updateTabHeight(false)) {
			updateItems();
			redraw();
			return;
		}
	}

	GC gc = event.gc;
	Font gcFont = gc.getFont();
	Color gcBackground = gc.getBackground();
	Color gcForeground = gc.getForeground();
	
// Useful for debugging paint problems
//{
//Point size = getSize();	
//gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
//gc.fillRectangle(-10, -10, size.x + 20, size.y+20);
//}

	Point size = getSize();
	Rectangle bodyRect = new Rectangle(0, 0, size.x, size.y); 
	renderer.draw(CTabFolderRenderer.PART_BODY, SWT.BACKGROUND | SWT.FOREGROUND, bodyRect, gc); 

	gc.setFont(gcFont);
	gc.setForeground(gcForeground);
	gc.setBackground(gcBackground);
	
	renderer.draw(CTabFolderRenderer.PART_HEADER, SWT.BACKGROUND | SWT.FOREGROUND, bodyRect, gc);
	
	gc.setFont(gcFont);
	gc.setForeground(gcForeground);
	gc.setBackground(gcBackground);	
	
	if (!single) {
		for (int i=0; i < items.length; i++) {
			Rectangle itemBounds = items[i].getBounds();
			if (i != selectedIndex && event.getBounds().intersects(itemBounds)) {
				renderer.draw(i, SWT.BACKGROUND | SWT.FOREGROUND, itemBounds, gc);
			}
		}
	}
	
	gc.setFont(gcFont);
	gc.setForeground(gcForeground);
	gc.setBackground(gcBackground);	
	
	if (selectedIndex != -1) { 
		renderer.draw(selectedIndex, SWT.SELECTED | SWT.BACKGROUND | SWT.FOREGROUND, items[selectedIndex].getBounds(), gc);
	}
	
	gc.setFont(gcFont);
	gc.setForeground(gcForeground);
	gc.setBackground(gcBackground);	
	
	renderer.draw(CTabFolderRenderer.PART_MAX_BUTTON, maxImageState, maxRect, gc);
	renderer.draw(CTabFolderRenderer.PART_MIN_BUTTON, minImageState, minRect, gc);
	renderer.draw(CTabFolderRenderer.PART_CHEVRON_BUTTON, chevronImageState, chevronRect, gc);

	gc.setFont(gcFont);
	gc.setForeground(gcForeground);
	gc.setBackground(gcBackground);	
}

void onResize() {
	if (updateItems()) redrawTabs();
	
	Point size = getSize();
	if (oldSize == null) {
		redraw();
	} else {
		if (onBottom && size.y != oldSize.y) {
			redraw();
		} else {
			int x1 = Math.min(size.x, oldSize.x);
			Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_BODY, SWT.NONE, 0, 0, 0, 0);
			if (size.x != oldSize.x) x1 -= trim.width + trim.x - marginWidth + 2;
			if (!simple) x1 -= 5; // rounded top right corner
			int y1 = Math.min(size.y, oldSize.y);
			if (size.y != oldSize.y) y1 -= trim.height + trim.y - marginHeight;
			int x2 = Math.max(size.x, oldSize.x);
			int y2 = Math.max(size.y, oldSize.y);		
			redraw(0, y1, x2, y2 - y1, false);
			redraw(x1, 0, x2 - x1, y2, false);
		}
	}
	oldSize = size;
}
void onTraverse (Event event) {
	if (ignoreTraverse) return;
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
			Control focusControl = getDisplay().getFocusControl();
			if (focusControl == this) event.doit = true;
			break;
		case SWT.TRAVERSE_MNEMONIC:
			event.doit = onMnemonic(event, false);
			break;
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			event.doit = items.length > 0;
			break;
	}
	ignoreTraverse = true;
	notifyListeners(SWT.Traverse, event);
	ignoreTraverse = false;
	event.type = SWT.None;
	if (isDisposed()) return;
	if (!event.doit) return;
	switch (event.detail) {
		case SWT.TRAVERSE_MNEMONIC:
			onMnemonic(event, true);
			event.detail = SWT.TRAVERSE_NONE;
			break;
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			onPageTraversal(event);
			event.detail = SWT.TRAVERSE_NONE;
			break;
	}
}
void redrawTabs() {
	Point size = getSize();
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_BODY, SWT.NONE, 0, 0, 0, 0);
	if (onBottom) {
		int h = trim.height + trim.y - marginHeight;
		redraw(0, size.y - h - 1, size.x, h + 1, false);
	} else {
		redraw(0, 0, size.x, -trim.y - marginHeight + 1, false);
	}
}
/**	 
 * Removes the listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @see #addCTabFolder2Listener(CTabFolder2Listener)
 * 
 * @since 3.0
 */
public void removeCTabFolder2Listener(CTabFolder2Listener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (folderListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < folderListeners.length; i++) {
		if (listener == folderListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (folderListeners.length == 1) {
		folderListeners = new CTabFolder2Listener[0];
		return;
	}
	CTabFolder2Listener[] newTabListeners = new CTabFolder2Listener[folderListeners.length - 1];
	System.arraycopy(folderListeners, 0, newTabListeners, 0, index);
	System.arraycopy(folderListeners, index + 1, newTabListeners, index, folderListeners.length - index - 1);
	folderListeners = newTabListeners;
}
/**	 
 * Removes the listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @deprecated see removeCTabFolderCloseListener(CTabFolderListener)
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
		return;
	}
	CTabFolderListener[] newTabListeners = new CTabFolderListener[tabListeners.length - 1];
	System.arraycopy(tabListeners, 0, newTabListeners, 0, index);
	System.arraycopy(tabListeners, index + 1, newTabListeners, index, tabListeners.length - index - 1);
	tabListeners = newTabListeners;
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
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);	
}

public void reskin(int flags) {
	super.reskin(flags);
	for (int i = 0; i < items.length; i++) {
		items[i].reskin(flags);
	}
}

public void setBackground (Color color) {
	super.setBackground(color);
	renderer.createAntialiasColors(); //TODO: need better caching strategy
	redraw();
}
/**
 * Specify a gradient of colours to be drawn in the background of the unselected tabs.
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
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.6
 */
public void setBackground(Color[] colors, int[] percents) {
	setBackground(colors, percents, false);
}
/**
 * Specify a gradient of colours to be drawn in the background of the unselected tab.
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
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.6
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
			colors = new Color[] {colors[colors.length - 1]};
			percents = new int[] {};
		}
	}
	
	// Are these settings the same as before?
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
public void setBackgroundImage(Image image) {
    	super.setBackgroundImage(image);
    	renderer.createAntialiasColors(); //TODO: need better caching strategy
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
	if (borderVisible == show) return;
	this.borderVisible = show;
	Rectangle rectBefore = getClientArea();
	updateItems();
	Rectangle rectAfter = getClientArea();
	if (!rectBefore.equals(rectAfter)) {
		notifyListeners(SWT.Resize, new Event());
	}
	redraw();
}
void setButtonBounds(GC gc) {
	Point size = getSize();
	int oldX, oldY, oldWidth, oldHeight;
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_BORDER, SWT.NONE, 0, 0, 0, 0);
	int borderRight = trim.width + trim.x;
	int borderLeft = -trim.x;
	int borderBottom = trim.height + trim.y;
	int borderTop = -trim.y;
	
	// max button
	oldX = maxRect.x;
	oldY = maxRect.y;
	oldWidth = maxRect.width;
	oldHeight = maxRect.height;
	maxRect.x = maxRect.y = maxRect.width = maxRect.height = 0;
	if (showMax) {
		Point maxSize = renderer.computeSize(CTabFolderRenderer.PART_MAX_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT);
		maxRect.x = size.x - borderRight - maxSize.x - 3;
		if (borderRight > 0) maxRect.x += 1;
		maxRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - maxSize.y)/2: borderTop + (tabHeight - maxSize.y)/2;
		maxRect.width = maxSize.x;
		maxRect.height = maxSize.y;
	}
	if (oldX != maxRect.x || oldWidth != maxRect.width ||
	    oldY != maxRect.y || oldHeight != maxRect.height) {
		int left = Math.min(oldX, maxRect.x);
		int right = Math.max(oldX + oldWidth, maxRect.x + maxRect.width);
		int top = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		redraw(left, top, right - left, tabHeight, false); 
	}

	// min button
	oldX = minRect.x;
	oldY = minRect.y;
	oldWidth = minRect.width;
	oldHeight = minRect.height;
	minRect.x = minRect.y = minRect.width = minRect.height = 0;
	if (showMin) {
		Point minSize = renderer.computeSize(CTabFolderRenderer.PART_MIN_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT);
		minRect.x = size.x - borderRight - maxRect.width - minSize.x - 3;
		if (borderRight > 0) minRect.x += 1;
		minRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - minSize.y)/2: borderTop + (tabHeight - minSize.y)/2;
		minRect.width = minSize.x;
		minRect.height = minSize.y;
	}
	if (oldX != minRect.x || oldWidth != minRect.width ||
	    oldY != minRect.y || oldHeight != minRect.height) {
		int left = Math.min(oldX, minRect.x);
		int right = Math.max(oldX + oldWidth, minRect.x + minRect.width);
		int top = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		redraw(left, top, right - left, tabHeight, false);
	}
	
	// top right control
	oldX = topRightRect.x;
	oldY = topRightRect.y;
	oldWidth = topRightRect.width;
	oldHeight = topRightRect.height;
	topRightRect.x = topRightRect.y = topRightRect.width = topRightRect.height = 0;
	if (topRight != null) {
		switch (topRightAlignment) {
			case SWT.FILL: {
				int rightEdge = size.x - borderRight - 3 - maxRect.width - minRect.width;
				if (!simple && borderRight > 0 && !showMax && !showMin) rightEdge -= 2;
				if (single) {
					if (items.length == 0 || selectedIndex == -1) {
						topRightRect.x = borderLeft + 3;
						topRightRect.width = rightEdge - topRightRect.x;
					} else {
						// fill size is 0 if item compressed
						CTabItem item = items[selectedIndex];
						int chevronWidth = renderer.computeSize(CTabFolderRenderer.PART_CHEVRON_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
						if (item.x + item.width + 7 + chevronWidth >= rightEdge) break;
						topRightRect.x = item.x + item.width + 7 + chevronWidth;
						topRightRect.width = rightEdge - topRightRect.x;
					}
				} else {
					// fill size is 0 if chevron showing
					if (showChevron) break;
					if (items.length == 0) {
						topRightRect.x = borderLeft + 3;
					} else {
						int lastIndex = items.length - 1;
						CTabItem lastItem = items[lastIndex];
						topRightRect.x = lastItem.x + lastItem.width;
					}
					topRightRect.width = Math.max(0, rightEdge - topRightRect.x);
				}
				topRightRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
				topRightRect.height = tabHeight - 1;
				break;
			}
			case SWT.RIGHT: {
				Point topRightSize = topRight.computeSize(SWT.DEFAULT, tabHeight, false);
				int rightEdge = size.x - borderRight - 3 - maxRect.width - minRect.width;
				if (!simple && borderRight > 0 && !showMax && !showMin) rightEdge -= 2;
				topRightRect.x = rightEdge - topRightSize.x;
				topRightRect.width = topRightSize.x;
				topRightRect.y = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
				topRightRect.height = tabHeight - 1;
			}
		}
		topRight.setBounds(topRightRect);
	}
	if (oldX != topRightRect.x || oldWidth != topRightRect.width ||
		oldY != topRightRect.y || oldHeight != topRightRect.height) {	
		int left = Math.min(oldX, topRightRect.x);
		int right = Math.max(oldX + oldWidth, topRightRect.x + topRightRect.width);
		int top = onBottom ? size.y - borderBottom - tabHeight : borderTop + 1;
		redraw(left, top, right - left, tabHeight, false);
	}
	
	// chevron button
	oldX = chevronRect.x;
	oldY = chevronRect.y;
	oldWidth = chevronRect.width;
	oldHeight = chevronRect.height;
	chevronRect.x = chevronRect.y = chevronRect.height = chevronRect.width = 0;
	Point chevronSize = renderer.computeSize(CTabFolderRenderer.PART_CHEVRON_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT);
	if (single) {
		if (selectedIndex == -1 || items.length > 1) {
			chevronRect.width = chevronSize.x;
			chevronRect.height = chevronSize.y;
			chevronRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - chevronRect.height)/2 : borderTop + (tabHeight - chevronRect.height)/2;
			if (selectedIndex == -1) {
				chevronRect.x = size.x - borderRight - 3 - minRect.width - maxRect.width - topRightRect.width - chevronRect.width;
			} else {
				CTabItem item = items[selectedIndex];
				int w = size.x - borderRight - 3 - minRect.width - maxRect.width - chevronRect.width;
				if (topRightRect.width > 0) w -= topRightRect.width + 3;
				chevronRect.x = Math.min(item.x + item.width + 3, w);
			}
			if (borderRight > 0) chevronRect.x += 1;
		}
	} else {
		if (showChevron) {
			chevronRect.width = chevronSize.x;
			chevronRect.height = chevronSize.y;
			int i = 0, lastIndex = -1;
			while (i < priority.length && items[priority[i]].showing) {
				lastIndex = Math.max(lastIndex, priority[i++]);
			}
			if (lastIndex == -1) lastIndex = firstIndex;
			CTabItem lastItem = items[lastIndex];
			int w = lastItem.x + lastItem.width + 3;
			if (!simple && lastIndex == selectedIndex) w -= renderer.curveIndent;  //TODO: fix chevron position
			chevronRect.x = Math.min(w, getRightItemEdge(gc));
			chevronRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - chevronRect.height)/2 : borderTop + (tabHeight - chevronRect.height)/2;
		}
	}
	if (oldX != chevronRect.x || oldWidth != chevronRect.width ||
	    oldY != chevronRect.y || oldHeight != chevronRect.height) {
		int left = Math.min(oldX, chevronRect.x);
		int right = Math.max(oldX + oldWidth, chevronRect.x + chevronRect.width);
		int top = onBottom ? size.y - borderBottom - tabHeight: borderTop + 1;
		redraw(left, top, right - left, tabHeight, false);
	}
}
public void setFont(Font font) {
	checkWidget();
	if (font != null && font.equals(getFont())) return;
	super.setFont(font);
	oldFont = getFont();
	if (!updateTabHeight(false)) {
		updateItems();
		redraw();
	}
}
public void setForeground (Color color) {
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
}
boolean setItemLocation(GC gc) {
	boolean changed = false;
	if (items.length == 0) return false;
	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_BORDER, SWT.NONE, 0, 0, 0, 0);
	int borderLeft = -trim.x;
	int borderBottom = trim.height + trim.y;
	int borderTop = -trim.y;
	Point size = getSize();
	int y = onBottom ? Math.max(borderBottom, size.y - borderBottom - tabHeight) : borderTop;
	Point closeButtonSize = renderer.computeSize(CTabFolderRenderer.PART_CLOSE_BUTTON, 0, gc, SWT.DEFAULT, SWT.DEFAULT);
	if (single) {
		int defaultX = getDisplay().getBounds().width + 10; // off screen
		for (int i = 0; i < items.length; i++) {
			CTabItem item = items[i];
			if (i == selectedIndex) {
				firstIndex = selectedIndex;
				int oldX = item.x, oldY = item.y;
				item.x = borderLeft;
				item.y = y;
				item.showing = true;
				if (showClose || item.showClose) {
					item.closeRect.x = borderLeft - renderer.computeTrim(i, SWT.NONE, 0, 0, 0, 0).x;
					item.closeRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - closeButtonSize.y)/2: borderTop + (tabHeight - closeButtonSize.y)/2;
				}
				if (item.x != oldX || item.y != oldY) changed = true;
			} else {
				item.x = defaultX;
				item.showing = false;
			}
		}
	} else {
		int rightItemEdge = getRightItemEdge(gc);
		int maxWidth = rightItemEdge - borderLeft;
		int width = 0;
		for (int i = 0; i < priority.length; i++) {
			CTabItem item = items[priority[i]];
			width += item.width;
			item.showing = i == 0 ? true : item.width > 0 && width <= maxWidth;
		}
		int x = -renderer.computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0).x;
		int defaultX = getDisplay().getBounds().width + 10; // off screen
		firstIndex = items.length - 1;
		for (int i = 0; i < items.length; i++) {
			CTabItem item = items[i];
			if (!item.showing) {
				if (item.x != defaultX) changed = true;
				item.x = defaultX;
			} else {
				firstIndex = Math.min(firstIndex, i);
				if (item.x != x || item.y != y) changed = true;
				item.x = x;
				item.y = y;
				int state = SWT.NONE;
				if (i == selectedIndex) state |= SWT.SELECTED;
				Rectangle edgeTrim = renderer.computeTrim(i, state, 0, 0, 0, 0);
				item.closeRect.x = item.x + item.width  - (edgeTrim.width + edgeTrim.x) - closeButtonSize.x;
				item.closeRect.y = onBottom ? size.y - borderBottom - tabHeight + (tabHeight - closeButtonSize.y)/2: borderTop + (tabHeight - closeButtonSize.y)/2;
				x = x + item.width;
				if (!simple && i == selectedIndex) x -= renderer.curveIndent; //TODO: fix next item position 
			}
		}
	}
	return changed;
}
boolean setItemSize(GC gc) {
	boolean changed = false;
	if (isDisposed()) return changed;
	Point size = getSize();
	if (size.x <= 0 || size.y <= 0) return changed;

	Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0);
	int borderRight = trim.width + trim.x;
	int borderLeft = -trim.x;
	
	showChevron = false;
	if (single) {
		showChevron = true;
		if (selectedIndex != -1) {
			CTabItem tab = items[selectedIndex];
			int width = renderer.computeSize(selectedIndex, SWT.SELECTED, gc, SWT.DEFAULT, SWT.DEFAULT).x;
			width = Math.min(width, getRightItemEdge(gc) - borderLeft);
			if (tab.height != tabHeight || tab.width != width) {
				changed = true;
				tab.shortenedText = null;
				tab.shortenedTextWidth = 0;
				tab.height = tabHeight;
				tab.width = width;
				tab.closeRect.width = tab.closeRect.height = 0;
				if (showClose || tab.showClose) {
					Point closeSize = renderer.computeSize(selectedIndex, SWT.SELECTED, gc, SWT.DEFAULT, SWT.DEFAULT);
					tab.closeRect.width = closeSize.x;
					tab.closeRect.height = closeSize.y;
				}
			}
		}
		return changed;
	}
	
	if (items.length == 0) return changed;

	int[] widths;
	int tabAreaWidth = size.x - borderLeft - borderRight - 3;
	if (showMin) tabAreaWidth -= renderer.computeSize(CTabFolderRenderer.PART_MIN_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
	if (showMax) tabAreaWidth -= renderer.computeSize(CTabFolderRenderer.PART_MAX_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
	if (topRightAlignment == SWT.RIGHT && topRight != null) {
		Point rightSize = topRight.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		tabAreaWidth -= rightSize.x + 3;
	}
	tabAreaWidth = Math.max(0, tabAreaWidth);

	// First, try the minimum tab size at full compression.
	int minWidth = 0;
	int[] minWidths = new int[items.length];	
	for (int i = 0; i < priority.length; i++) {
		int index = priority[i];
		int state = CTabFolderRenderer.MINIMUM_SIZE;
		if (index == selectedIndex) state |= SWT.SELECTED;
		minWidths[index] = renderer.computeSize(index, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
		minWidth += minWidths[index];
		if (minWidth > tabAreaWidth) break;
	}
	if (minWidth > tabAreaWidth) {
		// full compression required and a chevron
		showChevron = items.length > 1;
		if (showChevron) tabAreaWidth -= renderer.computeSize(CTabFolderRenderer.PART_CHEVRON_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
		widths = minWidths;
		int index = selectedIndex != -1 ? selectedIndex : 0;
		if (tabAreaWidth < widths[index]) {
			widths[index] = Math.max(0, tabAreaWidth);
		}
	} else {
		int maxWidth = 0;
		int[] maxWidths = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			int state = 0;
			if (i == selectedIndex) state |= SWT.SELECTED;
			maxWidths[i] = renderer.computeSize(i, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
			maxWidth += maxWidths[i];
		}
		if (maxWidth <= tabAreaWidth) {
			// no compression required
			widths = maxWidths;
		} else {
			// determine compression for each item
			int extra = (tabAreaWidth - minWidth) / items.length;
			while (true) {
				int large = 0, totalWidth = 0;
				for (int i = 0 ; i < items.length; i++) {
					if (maxWidths[i] > minWidths[i] + extra) {
						totalWidth += minWidths[i] + extra;
						large++;
					} else {
						totalWidth += maxWidths[i];
					}
				}
				if (totalWidth >= tabAreaWidth) {
					extra--;
					break;
				}
				if (large == 0 || tabAreaWidth - totalWidth < large) break;
				extra++;
			}
			widths = new int[items.length];
			for (int i = 0; i < items.length; i++) {
				widths[i] = Math.min(maxWidths[i], minWidths[i] + extra);
			}
		}
	}

	for (int i = 0; i < items.length; i++) {
		CTabItem tab = items[i];
		int width = widths[i];
		if (tab.height != tabHeight || tab.width != width) {
			changed = true;
			tab.shortenedText = null;
			tab.shortenedTextWidth = 0;
			tab.height = tabHeight;
			tab.width = width;
			tab.closeRect.width = tab.closeRect.height = 0;
			if (showClose || tab.showClose) {
				if (i == selectedIndex || showUnselectedClose) {
					Point closeSize = renderer.computeSize(CTabFolderRenderer.PART_CLOSE_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT);
					tab.closeRect.width = closeSize.x;
					tab.closeRect.height = closeSize.y;
				}
			}
		}
	}
	return changed;
}
/**
 * Marks the receiver's maximize button as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 * <p>
 * Note: No Layout can be set on this Control because it already
 * manages the size and position of its children.
 * </p>
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	return;
}
/**
 * Sets the maximized state of the receiver.
 *
 * @param maximize the new maximized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setMaximized(boolean maximize) {
	checkWidget ();
	if (this.maximized == maximize) return;
	if (maximize && this.minimized) setMinimized(false);
	this.maximized = maximize;
	redraw(maxRect.x, maxRect.y, maxRect.width, maxRect.height, false);
}
/**
 * Marks the receiver's minimize button as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setMinimizeVisible(boolean visible) {
	checkWidget();
	if (showMin == visible) return;
	// display minimize button
	showMin = visible;
	updateItems();
	redraw();
}
/**
 * Sets the minimized state of the receiver.
 *
 * @param minimize the new minimized state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setMinimized(boolean minimize) {
	checkWidget ();
	if (this.minimized == minimize) return;
	if (minimize && this.maximized) setMaximized(false);
	this.minimized = minimize;
	redraw(minRect.x, minRect.y, minRect.width, minRect.height, false);
}

/**
 * Sets the minimum number of characters that will 
 * be displayed in a fully compressed tab.
 * 
 * @param count the minimum number of characters that will be displayed in a fully compressed tab
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_RANGE - if the count is less than zero</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setMinimumCharacters(int count) {
	checkWidget ();
	if (count < 0) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (minChars == count) return;
	minChars = count;
	if (updateItems()) redrawTabs();
}

/**
 * When there is not enough horizontal space to show all the tabs,
 * by default, tabs are shown sequentially from left to right in 
 * order of their index.  When the MRU visibility is turned on,
 * the tabs that are visible will be the tabs most recently selected.
 * Tabs will still maintain their left to right order based on index 
 * but only the most recently selected tabs are visible.
 * <p>
 * For example, consider a CTabFolder that contains "Tab 1", "Tab 2",
 * "Tab 3" and "Tab 4" (in order by index).  The user selects
 * "Tab 1" and then "Tab 3".  If the CTabFolder is now
 * compressed so that only two tabs are visible, by default, 
 * "Tab 2" and "Tab 3" will be shown ("Tab 3" since it is currently 
 * selected and "Tab 2" because it is the previous item in index order).
 * If MRU visibility is enabled, the two visible tabs will be "Tab 1"
 * and "Tab 3" (in that order from left to right).</p>
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
public void setMRUVisible(boolean show) {
	checkWidget();
	if (mru == show) return;
	mru = show;
	if (!mru) {
		int idx = firstIndex;
		int next = 0;
		for (int i = firstIndex; i < items.length; i++) {
			priority[next++] = i;
		}
		for (int i = 0; i < idx; i++) {
			priority[next++] = i;
		}
		if (updateItems()) redrawTabs();
	}
}
/**
 * Sets the renderer which is associated with the receiver to be
 * the argument which may be null. In the case of null, the default
 * renderer is used.
 *
 * @param renderer a new renderer
 * 
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.6
 */
public void setRenderer(CTabFolderRenderer renderer) {
	checkWidget();
	if (this.renderer == renderer) return;
	if (this.renderer != null) this.renderer.dispose();
	if (renderer == null) renderer = new CTabFolderRenderer(this);
	this.renderer = renderer;
	updateTabHeight(false);
	Rectangle rectBefore = getClientArea();
	updateItems();
	Rectangle rectAfter = getClientArea();
	if (!rectBefore.equals(rectAfter)) {
		notifyListeners(SWT.Resize, new Event());
	}
	redraw();
}
/**
 * Set the selection to the tab at the specified item.
 * 
 * @param item the tab item to be selected
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
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
	CTabItem selection = items[index];
	if (selectedIndex == index) {
		showItem(selection);
		return;
	}
	
	int oldIndex = selectedIndex;
	selectedIndex = index;
	if (oldIndex != -1) {
		items[oldIndex].closeImageState = SWT.BACKGROUND;
	}
	selection.closeImageState = SWT.NONE;
	selection.showing = false;

	Control newControl = selection.control;
	Control oldControl = null;
	if (oldIndex != -1) {
		oldControl = items[oldIndex].control;
	}
	
	if (newControl != oldControl) {
		if (newControl != null && !newControl.isDisposed()) {
			newControl.setBounds(getClientArea());
			newControl.setVisible(true);
		}
		if (oldControl != null && !oldControl.isDisposed()) {
			oldControl.setVisible(false);
		}
	}
	showItem(selection);
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
 * Sets the receiver's selection background color to the color specified
 * by the argument, or to the default system color for the control
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
 * @since 3.0
 */
public void setSelectionBackground (Color color) {
	checkWidget();
	setSelectionHighlightGradientColor(null);
	if (selectionBackground == color) return;
	if (color == null) color = getDisplay().getSystemColor(SELECTION_BACKGROUND);
	selectionBackground = color;
	renderer.createAntialiasColors(); //TODO:  need better caching strategy
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
 * @exception SWTException <ul>
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
 * @exception SWTException <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void setSelectionBackground(Color[] colors, int[] percents, boolean vertical) {
	checkWidget();
	int colorsLength;
	Color highlightBeginColor = null;  //null == no highlight

	if (colors != null) {
		//The colors array can optionally have an extra entry which describes the highlight top color
		//Thus its either one or two larger than the percents array
		if (percents == null || 
				! ((percents.length == colors.length - 1) || (percents.length == colors.length - 2))){
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
		//If the colors is exactly two more than percents then last is highlight
		//Keep track of *real* colorsLength (minus the highlight)
		if(percents.length == colors.length - 2) {
			highlightBeginColor = colors[colors.length - 1];
			colorsLength = colors.length - 1;
		} else {
			colorsLength = colors.length;
		}
		if (getDisplay().getDepth() < 15) {
			// Don't use gradients on low color displays
			colors = new Color[] {colors[colorsLength - 1]};
			colorsLength = colors.length;
			percents = new int[] {};
		}
	} else {
		colorsLength = 0;
	}
	
	// Are these settings the same as before?
	if (selectionBgImage == null) {
		if ((selectionGradientColors != null) && (colors != null) && 
			(selectionGradientColors.length == colorsLength)) {
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
		setSelectionHighlightGradientColor(null);
	} else {
		selectionGradientColors = new Color[colorsLength];
		for (int i = 0; i < colorsLength; ++i) {
			selectionGradientColors[i] = colors[i];
		}
		selectionGradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i) {
			selectionGradientPercents[i] = percents[i];
		}
		selectionGradientVertical = vertical;
		setSelectionBackground(selectionGradientColors[selectionGradientColors.length-1]);
		setSelectionHighlightGradientColor(highlightBeginColor);
	}

	// Refresh with the new settings
	if (selectedIndex > -1) redraw();
}

/*
 * Set the color for the highlight start for selected tabs.
 * Update the cache of highlight gradient colors if required.
 */
void setSelectionHighlightGradientColor(Color start) {
	renderer.setSelectionHighlightGradientColor(start);  //TODO: need better caching strategy
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
	setSelectionHighlightGradientColor(null);
	if (image == selectionBgImage) return;
	if (image != null) {
		selectionGradientColors = null;
		selectionGradientPercents = null;
		renderer.disposeSelectionHighlightGradientColors(); //TODO: need better caching strategy
	}
	selectionBgImage = image;
	renderer.createAntialiasColors(); //TODO:  need better caching strategy
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
 * Sets the shape that the CTabFolder will use to render itself.  
 * 
 * @param simple <code>true</code> if the CTabFolder should render itself in a simple, traditional style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setSimple(boolean simple) {
	checkWidget();
	if (this.simple != simple) {
		this.simple = simple;
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
 * Sets the number of tabs that the CTabFolder should display
 * 
 * @param single <code>true</code> if only the selected tab should be displayed otherwise, multiple tabs will be shown.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setSingle(boolean single) {
	checkWidget();
	if (this.single != single) {
		this.single = single;
		if (!single) {
			for (int i = 0; i < items.length; i++) {
				if (i != selectedIndex && items[i].closeImageState == SWT.NONE) {
					items[i].closeImageState = SWT.BACKGROUND;
				}
			}
		}
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
	fixedTabHeight = height;
	updateTabHeight(false);
}
/**
 * Specify whether the tabs should appear along the top of the folder 
 * or along the bottom of the folder.
 * 
 * @param position <code>SWT.TOP</code> for tabs along the top or <code>SWT.BOTTOM</code> for tabs along the bottom
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the position value is not either SWT.TOP or SWT.BOTTOM</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setTabPosition(int position) {
	checkWidget();
	if (position != SWT.TOP && position != SWT.BOTTOM) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (onBottom != (position == SWT.BOTTOM)) {
		onBottom = position == SWT.BOTTOM;
		updateTabHeight(true);
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
 * Set the control that appears in the top right corner of the tab folder.
 * Typically this is a close button or a composite with a Menu and close button. 
 * The topRight control is optional.  Setting the top right control to null will 
 * remove it from the tab folder.
 * 
 * @param control the control to be displayed in the top right corner or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this CTabFolder</li>
 * </ul>
 * 
 * @since 2.1
 */
public void setTopRight(Control control) {
	setTopRight(control, SWT.RIGHT);
}
/**
 * Set the control that appears in the top right corner of the tab folder.
 * Typically this is a close button or a composite with a Menu and close button. 
 * The topRight control is optional.  Setting the top right control to null 
 * will remove it from the tab folder.
 * <p>
 * The alignment parameter sets the layout of the control in the tab area.
 * <code>SWT.RIGHT</code> will cause the control to be positioned on the far 
 * right of the folder and it will have its default size.  <code>SWT.FILL</code> 
 * will size the control to fill all the available space to the right of the
 * last tab.  If there is no available space, the control will not be visible.
 * </p>
 *
 * @param control the control to be displayed in the top right corner or null
 * @param alignment <code>SWT.RIGHT</code> or <code>SWT.FILL</code> 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the control is not a child of this CTabFolder</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setTopRight(Control control, int alignment) {
	checkWidget();
	if (alignment != SWT.RIGHT && alignment != SWT.FILL) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (control != null && control.getParent() != this) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	topRight = control;
	topRightAlignment = alignment;
	if (updateItems()) redraw();
}
/**
 * Specify whether the close button appears 
 * when the user hovers over an unselected tabs.
 * 
 * @param visible <code>true</code> makes the close button appear
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setUnselectedCloseVisible(boolean visible) {
	checkWidget();
	if (showUnselectedClose == visible) return;
	// display close button when mouse hovers
	showUnselectedClose = visible;
	updateItems();
	redraw();
}
/**
 * Specify whether the image appears on unselected tabs.
 * 
 * @param visible <code>true</code> makes the image appear
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setUnselectedImageVisible(boolean visible) {
	checkWidget();
	if (showUnselectedImage == visible) return;
	// display image on unselected items
	showUnselectedImage = visible;
	updateItems();
	redraw();
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
	if (index == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int idx = -1;
	for (int i = 0; i < priority.length; i++) {
		if (priority[i] == index) {
			idx = i;
			break;
		}
	}
	if (mru) {
		// move to front of mru order
		int[] newPriority = new int[priority.length];
		System.arraycopy(priority, 0, newPriority, 1, idx);
		System.arraycopy(priority, idx+1, newPriority, idx+1, priority.length - idx - 1);
		newPriority[0] = index;
		priority = newPriority;
	}
	if (item.isShowing()) return;
	updateItems(index);
	redrawTabs();
}
void showList (Rectangle rect) {
	if (items.length == 0 || !showChevron) return;
	if (showMenu == null || showMenu.isDisposed()) {
		showMenu = new Menu(getShell(), getStyle() & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT));
	} else {
		MenuItem[] items = showMenu.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].dispose();
		}
	}
	final String id = "CTabFolder_showList_Index"; //$NON-NLS-1$
	for (int i = 0; i < items.length; i++) {
		CTabItem tab = items[i];
		if (tab.showing) continue;
		MenuItem item = new MenuItem(showMenu, SWT.NONE);
		item.setText(tab.getText());
		item.setImage(tab.getImage());
		item.setData(id, tab);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem)e.widget;
				int index = indexOf((CTabItem)menuItem.getData(id));
				CTabFolder.this.setSelection(index, true);
			}
		});
	}
	int x = rect.x;
	int y = rect.y + rect.height;
	Point location = getDisplay().map(this, null, x, y);
	showMenu.setLocation(location.x, location.y);
	showMenu.setVisible(true);
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
 */
public void showSelection () {
	checkWidget (); 
	if (selectedIndex != -1) {
		showItem(getSelection());
	}
}

void _setToolTipText (int x, int y) {
	String oldTip = getToolTipText();
	String newTip = _getToolTip(x, y);
	if (newTip == null || !newTip.equals(oldTip)) {
		setToolTipText(newTip);
	}
}

boolean updateItems() {
	return updateItems(selectedIndex);
}

boolean updateItems(int showIndex) {
	GC gc = new GC(this);
	if (!single && !mru && showIndex != -1) {
		// make sure selected item will be showing
		int firstIndex = showIndex;
		if (priority[0] < showIndex) {
			Rectangle trim = renderer.computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0);
			int borderLeft = -trim.x;
			int maxWidth = getRightItemEdge(gc) - borderLeft;
			int width = 0;
			int[] widths = new int[items.length];
			for (int i = priority[0]; i <= showIndex; i++) {
				int state = CTabFolderRenderer.MINIMUM_SIZE;
				if (i == selectedIndex) state |= SWT.SELECTED;
				widths[i] = renderer.computeSize(i, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
				width += widths[i];
				if (width > maxWidth) break;
			}
			if (width > maxWidth) {
				width = 0;
				for (int i = showIndex; i >= 0; i--) {
					int state = CTabFolderRenderer.MINIMUM_SIZE;
					if (i == selectedIndex) state |= SWT.SELECTED;
					if (widths[i] == 0) widths[i] = renderer.computeSize(i, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
					width += widths[i];
					if (width > maxWidth) break;
					firstIndex = i;
				}
			} else {
				firstIndex = priority[0];
				for (int i = showIndex + 1; i < items.length; i++) {
					int state = CTabFolderRenderer.MINIMUM_SIZE;
					if (i == selectedIndex) state |= SWT.SELECTED;
					widths[i] = renderer.computeSize(i, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
					width += widths[i];
					if (width >= maxWidth) break;
				}
				if (width < maxWidth) {
					for (int i = priority[0] - 1; i >= 0; i--) {
						int state = CTabFolderRenderer.MINIMUM_SIZE;
						if (i == selectedIndex) state |= SWT.SELECTED;
						if (widths[i] == 0) widths[i] = renderer.computeSize(i, state, gc, SWT.DEFAULT, SWT.DEFAULT).x;
						width += widths[i];
						if (width > maxWidth) break;
						firstIndex = i;
					}
				}
			}
		
		}
		if (firstIndex != priority[0]) {
			int index = 0;
			for (int i = firstIndex; i < items.length; i++) {
				priority[index++] = i;
			}
			for (int i = 0; i < firstIndex; i++) {
				priority[index++] = i;
			}
		}
	}
	
	boolean oldShowChevron = showChevron;
	boolean changed = setItemSize(gc);
	changed |= setItemLocation(gc);
	setButtonBounds(gc);
	changed |= showChevron != oldShowChevron;
	if (changed && getToolTipText() != null) {
		Point pt = getDisplay().getCursorLocation();
		pt = toControl(pt);
		_setToolTipText(pt.x, pt.y);
	}
	gc.dispose();
	return changed;
}
boolean updateTabHeight(boolean force){
	int oldHeight = tabHeight;
	GC gc = new GC(this);
	tabHeight = renderer.computeSize(CTabFolderRenderer.PART_HEADER, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).y;
	gc.dispose();
	if (!force && tabHeight == oldHeight) return false;
	oldSize = null;
	notifyListeners(SWT.Resize, new Event());
	return true;
}
String _getToolTip(int x, int y) {
	if (showMin && minRect.contains(x, y)) return minimized ? SWT.getMessage("SWT_Restore") : SWT.getMessage("SWT_Minimize"); //$NON-NLS-1$ //$NON-NLS-2$
	if (showMax && maxRect.contains(x, y)) return maximized ? SWT.getMessage("SWT_Restore") : SWT.getMessage("SWT_Maximize"); //$NON-NLS-1$ //$NON-NLS-2$
	if (showChevron && chevronRect.contains(x, y)) return SWT.getMessage("SWT_ShowList"); //$NON-NLS-1$
	CTabItem item = getItem(new Point (x, y));
	if (item == null) return null;
	if (!item.showing) return null;
	if ((showClose || item.showClose) && item.closeRect.contains(x, y)) {
		return SWT.getMessage("SWT_Close"); //$NON-NLS-1$
	}
	return item.getToolTipText();
}
}
