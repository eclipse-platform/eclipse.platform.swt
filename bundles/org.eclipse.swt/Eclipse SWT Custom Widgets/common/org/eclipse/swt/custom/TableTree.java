package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/** 
 * A TableTree is a selectable user interface object
 * that displays a hierarchy of items, and issues
 * notification when an item is selected.
 * A TableTree may be single or multi select.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableTreeItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 *	<dt><b>Styles:</b> <dd> SINGLE, MULTI, CHECK, FULL_SELECTION
 *	<dt><b>Events:</b> <dd> Selection, DefaultSelection, Collapse, Expand
 * </dl>
 */
public class TableTree extends Composite {
	Table table;
	TableTreeItem[] items = EMPTY_ITEMS;
	Image plusImage, minusImage, sizeImage;

	/*
	* TableTreeItems are not treated as children but rather as items.
	* When the TableTree is disposed, all children are disposed because 
	* TableTree inherits this behaviour from Composite.  The items
	* must be disposed separately.  Because TableTree is not part of
	* the org.eclipse.swt.widgets package, the method releaseWidget can 
	* not be overriden (this is how items are disposed of in Table and Tree).
	* Instead, the items are disposed of in response to the dispose event on the
	* TableTree.  The "inDispose" flag is used to distinguish between disposing
	* one TableTreeItem (e.g. when removing an entry from the TableTree) and 
	* disposing the entire TableTree.
	*/
	boolean inDispose = false;
	
	static final TableTreeItem[] EMPTY_ITEMS = new TableTreeItem [0];	
	static final String[] EMPTY_TEXTS = new String [0];	
	static final Image[] EMPTY_IMAGES = new Image [0];	

/**
 * Creates a new instance of the widget.
 *
 * @param parent a composite widget
 * @param style the bitwise OR'ing of widget styles
 */
public TableTree(Composite parent, int style) {
	super(parent, SWT.NONE);
	table = new Table(this, style);
	setBackground(table.getBackground());
	setForeground(table.getForeground());
	setFont(table.getFont());
	Listener tableListener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
			case SWT.MouseDown: onMouseDown(e); break;
			case SWT.Selection: onSelection(e); break;
			case SWT.DefaultSelection: onSelection(e); break;
			case SWT.KeyDown: onKeyDown(e); break;
			}
		}
	};
	int[] tableEvents = new int[]{SWT.MouseDown, 
		                           SWT.Selection, 
		                           SWT.DefaultSelection, 
		                           SWT.KeyDown};
	for (int i = 0; i < tableEvents.length; i++) {
		table.addListener(tableEvents[i], tableListener);
	}
	
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
			case SWT.Dispose: onDispose(e); break;
			case SWT.Resize:  onResize(e); break;
			case SWT.FocusIn: onFocusIn(e); break;
			}
		}
	};
	int[] events = new int[]{SWT.Dispose, 
		                      SWT.Resize, 
		                      SWT.FocusIn};
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener);
	}	                      
}

int addItem(TableTreeItem item, int index) {
	if (index < 0 || index > items.length) throw new SWTError(SWT.ERROR_INVALID_ARGUMENT);
	TableTreeItem[] newItems = new TableTreeItem[items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index); 
	items = newItems;

	/* Return the index in the table where this table should be inserted */
	if (index == items.length - 1 ) 
		return table.getItemCount();
	else 
		return table.indexOf(items[index+1].tableItem);
}

/**	 
 * Adds the listener to receive selection events.
 * <p>
 *
 * @param listener the selection listener
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when listener is null
 * </ul>
 */
public void addSelectionListener(SelectionListener listener) {
	if (listener == null) throw new SWTError (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**	 
 * Adds the listener to receive tree events.
 * <p>
 *
 * @param listener the tree listener
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when listener is null
 * </ul>
 */
public void addTreeListener(TreeListener listener) {
	if (listener == null) throw new SWTError (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}  

/**
 * Computes the preferred size of the widget.
 * <p>
 * Calculate the preferred size of the widget based
 * on the current contents. The hint arguments allow
 * a specific client area width and/or height to be
 * requested. The hints may be honored depending on
 * the platform and the layout.
 *
 * @param wHint the width hint (can be SWT.DEFAULT)
 * @param hHint the height hint (can be SWT.DEFAULT)
 * @return a point containing the preferred size of the widget including trim
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public Point computeSize (int wHint, int hHint) {
	return table.computeSize (wHint, hHint, true);
}

/**
 * Computes the widget trim.
 * <p>
 * Trim is widget specific and may include scroll
 * bars and menu bar in addition to other trimmings
 * that are outside of the widget's client area.
 *
 * @param x the x location of the client area
 * @param y the y location of the client area
 * @param width the width of the client area
 * @param height the height of the client area
 * @return a rectangle containing the trim of the widget.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public Rectangle computeTrim (int x, int y, int width, int height) {
	return table.computeTrim(x, y, width, height);
}

/**
 * Deselects all items.
 * <p>
 * If an item is selected, it is deselected.
 * If an item is not selected, it remains unselected.
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 * </ul>
 */
public void deselectAll () {
	table.deselectAll();
}

/* Expand upward from the specified leaf item. */
void expandItem (TableTreeItem item) {
	if (item == null || item.getExpanded()) return;
	expandItem(item.parentItem);
	item.setExpanded(true);
	Event event = new Event();
	event.item = item;
	notifyListeners(SWT.Expand, event);
}

/**
 * Gets the number of items.
 * <p>
 * @return the number of items in the widget
 */
public int getItemCount () {
	return items.length;
}

/**
 * Gets the height of one item.
 * <p>
 * This operation will fail if the height of
 * one item could not be queried from the OS.
 *
 * @return the height of one item in the widget
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_CANNOT_GET_ITEM_HEIGHT when the operation fails
 * </ul>
 */
public int getItemHeight () {
	return table.getItemHeight();
}

/**
 * Gets the items.
 * <p>
 * @return the items in the widget
 *
 */
public TableTreeItem [] getItems () {
	TableTreeItem[] newItems = new TableTreeItem[items.length];
	System.arraycopy(items, 0, newItems, 0, items.length);
	return newItems;
}

/**
 * Gets the selected items.
 * <p>
 * This operation will fail if the selected
 * items cannot be queried from the OS.
 *
 * @return the selected items in the widget
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_CANNOT_GET_SELECTION when the operation fails</li>
 *	</ul>
 */
public TableTreeItem [] getSelection () {
	TableItem[] selection = table.getSelection();
	TableTreeItem [] result = new TableTreeItem[selection.length];
	for (int i = 0; i < selection.length; i++){
		result[i] = (TableTreeItem) selection[i].getData();
	}
	return result;
}

/**
 * Gets the number of selected items.
 * <p>
 * This operation will fail if the number of selected
 * items cannot be queried from the OS.
 *
 * @return the number of selected items in the widget
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_CANNOT_GET_COUNT when the operation fails</li>
 *	</ul>
 */
public int getSelectionCount () {
	return table.getSelectionCount();
}

/**
 * Returns the underlying Table control.
 *
 * @return the underlying Table control
 */
public Table getTable () {
	return table;
}

void createImages () {
	
	int itemHeight = sizeImage.getBounds().height;
	// Calculate border around image. 
	// At least 9 pixels are needed to draw the image
	// Leave at least a 6 pixel border.
	int indent = Math.min(6, (itemHeight - 9) / 2);
	indent = Math.max(0, indent);
	int size = Math.max (10, itemHeight - 2 * indent); 
	size = ((size + 1) / 2) * 2; // size must be an even number
	int midpoint = indent + size / 2;
	
	Color foreground = getForeground();
	Color plusMinus = getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color background = getBackground();
	
	/* Plus image */
	PaletteData palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), plusMinus.getRGB()});
	ImageData imageData = new ImageData(itemHeight, itemHeight, 4, palette);
	imageData.transparentPixel = 1;
	plusImage = new Image(getDisplay(), imageData);
	GC gc = new GC(plusImage);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, itemHeight, itemHeight);
	gc.setForeground(plusMinus);
	gc.drawRectangle(indent, indent, size, size);
	gc.setForeground(foreground);
	gc.drawLine(midpoint, indent + 2, midpoint, indent + size - 2);
	gc.drawLine(indent + 2, midpoint, indent + size - 2, midpoint);
	gc.dispose();
	
	/* Minus image */
	palette = new PaletteData(new RGB[]{foreground.getRGB(), background.getRGB(), plusMinus.getRGB()});
	imageData = new ImageData(itemHeight, itemHeight, 4, palette);
	imageData.transparentPixel = 1;
	minusImage = new Image(getDisplay(), imageData);
	gc = new GC(minusImage);
	gc.setBackground(background);
	gc.fillRectangle(0, 0, itemHeight, itemHeight);
	gc.setForeground(plusMinus);
	gc.drawRectangle(indent, indent, size, size);
	gc.setForeground(foreground);
	gc.drawLine(indent + 2, midpoint, indent + size - 2, midpoint);
	gc.dispose();
}

Image getPlusImage() {
	if (plusImage == null) createImages();
	return plusImage;
}

Image getMinusImage() {
	if (minusImage == null) createImages();
	return minusImage;
}

/**
 * Gets the index of an item.
 * 
 * <p>The widget is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.  This index is relative to the parent only.
 *
 * @param item the search item
 * @return the index of the item or -1
 *
 */
public int indexOf (TableTreeItem item) {
	for (int i = 0; i < items.length; i++) {
		if (item == items[i]) return i;
	}
	return -1;
}

void onDispose(Event e) {
	inDispose = true;
	for (int i = 0; i < items.length; i++) {
		items[i].dispose();
	}
	inDispose = false;
	if (plusImage != null) plusImage.dispose();
	if (minusImage != null) minusImage.dispose();
	if (sizeImage != null) sizeImage.dispose();
	plusImage = minusImage = sizeImage = null;
}

void onResize(Event e) {
	Rectangle area = getClientArea();
	table.setBounds(0, 0, area.width, area.height);
}

void onSelection(Event e) {
	Event event = new Event();
	TableItem tableItem = (TableItem)e.item;
    	TableTreeItem item = getItem(tableItem);
    	event.item = item;

	if (e.type == SWT.Selection 
	    && e.detail == SWT.CHECK
	    && item != null) {
	    	event.detail = SWT.CHECK;
		item.checked = tableItem.getChecked();
	}
	notifyListeners(e.type, event);
}
public TableTreeItem getItem(Point point) {
	TableItem item = table.getItem(point);
	if (item == null) return null;
	return getItem(item);
	
}
TableTreeItem getItem(TableItem tableItem) {
	if (tableItem == null) return null;
	for (int i = 0; i < items.length; i++) {
	    	TableTreeItem item = items[i].getItem(tableItem);
	    	if (item != null) return item;
	}
	return null;
}
void onFocusIn (Event e) {
	table.setFocus();
}

void onKeyDown (Event e) {
	TableTreeItem[] selection = getSelection();
	if (selection.length == 0) return;
	TableTreeItem item = selection[0];
	int type = 0;
	if (e.keyCode == SWT.ARROW_RIGHT || e.keyCode == SWT.ARROW_LEFT) {
		if (e.keyCode == SWT.ARROW_RIGHT) {
			if (item.getItemCount() == 0) return;
			if (item.getExpanded()) {
				TableTreeItem newSelection = item.getItems()[0];
				table.setSelection(new TableItem[]{newSelection.tableItem});
				showItem(newSelection);
				type = SWT.Selection;
			} else {
				item.setExpanded(true);
				type = SWT.Expand;
			}
		} else {
			if (item.getExpanded()) {
				item.setExpanded(false);
				type = SWT.Collapse;
			} else {
				TableTreeItem parent = item.getParentItem();
				if (parent != null) {
					int index = parent.indexOf(item);
					if (index != 0) return;
					table.setSelection(new TableItem[]{parent.tableItem});
					type = SWT.Selection;
				}
			}
		}
	}
	if (e.character == '*') {
		item.expandAll(true);
	}
	if (e.character == '-') {
		if (item.getExpanded()) {
			item.setExpanded(false);
			type = SWT.Collapse;
		}
	}
	if (e.character == '+') {
		if (item.getItemCount() > 0 && !item.getExpanded()) {
			item.setExpanded(true);
			type = SWT.Expand;
		}
	} 
	if (type == 0) return;
	Event event = new Event();
	notifyListeners(type, event);
}
void onMouseDown(Event event) {
	/* If user clicked on the [+] or [-], expand or collapse the tree. */
	TableItem[] items = table.getItems();
	for (int i = 0; i < items.length; i++) {
		Rectangle rect = items[i].getImageBounds(0);
		if (rect.contains(event.x, event.y)) {
			TableTreeItem item = (TableTreeItem) items[i].getData();
			event = new Event();
			event.item = item;
			item.setExpanded(!item.getExpanded());
			if (item.getExpanded()) {
				notifyListeners(SWT.Expand, event);
			} else {
				notifyListeners(SWT.Collapse, event);
			}
			return;
		}
	}
}

/**
 * Removes all items.
 * <p>
 * This operation will fail when an item
 * could not be removed in the OS.
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 * 	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 * 	<li>ERROR_ITEM_NOT_REMOVED when the operation fails
 * </ul>
 */
public void removeAll () {
	setRedraw(false);
	for (int i = items.length - 1; i >= 0; i--) {
		items[i].dispose();
	}
	items = EMPTY_ITEMS;
	setRedraw(true);
}

void removeItem(TableTreeItem item) {
	int index = 0;
	while (index < items.length && items[index] != item) index++;
	if (index == items.length) return;
	TableTreeItem[] newItems = new TableTreeItem[items.length - 1];
	System.arraycopy(items, 0, newItems, 0, index);
	System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
	items = newItems;
}

/**	 
 * Removes the listener.
 * <p>
 *
 * @param listener the listener
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 * 	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when listener is null
 * </ul>
 */
public void removeSelectionListener (SelectionListener listener) {
	if (listener == null) throw new SWTError (SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when listener is null
 * </ul>
 */
public void removeTreeListener (TreeListener listener) {
	if (listener == null) throw new SWTError (SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Expand, listener);
	removeListener(SWT.Collapse, listener);
}

/**
 * Selects all items.
 * <p>
 * If an item is not selected, it is selected.
 * If an item is selected, it remains selected.
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 * </ul>
 */
public void selectAll () {
	table.selectAll();
}

/**
 * Sets the widget background color.
 * <p>
 * When new color is null, the background reverts
 * to the default system color for the widget.
 *
 * @param color the new color (or null)
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setBackground (Color color) {
	super.setBackground(color);
	table.setBackground(color);
	if (sizeImage != null) {
		GC gc = new GC (sizeImage);
		gc.setBackground(getBackground());
		Rectangle size = sizeImage.getBounds();
		gc.fillRectangle(size);
		gc.dispose();
	}
}

/**
 * Sets the enabled state.
 * <p>
 * A disabled widget is typically not selectable from
 * the user interface and draws with an inactive or
 * grayed look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setEnabled (boolean enabled) {
	super.setEnabled(enabled);
	table.setEnabled(enabled);
}

/**
 * Sets the widget font.
 * <p>
 * When new font is null, the font reverts
 * to the default system font for the widget.
 *
 * @param font the new font (or null)
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setFont (Font font) {
	super.setFont(font);
	table.setFont(font);
}

/**
 * Gets the widget foreground color.
 * <p>
 * @return the widget foreground color
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setForeground (Color color) {
	super.setForeground(color);
	table.setForeground(color);
}

/**
 * Sets the pop up menu.
 * <p>
 * Every control has an optional pop up menu that is
 * displayed when the user requests a popup menu for
 * the control.  The sequence of key strokes/button
 * presses/button releases that is used to request
 * a pop up menu is platform specific.
 *
 * @param menu the new pop up menu
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_MENU_NOT_POP_UP when the menu is not a POP_UP</li>
 *		<li>ERROR_NO_COMMON_PARENT when the menu is not in the same widget tree</li>
 *	</ul>
 */
public void setMenu (Menu menu) {
	super.setMenu(menu);
	table.setMenu(menu);
}

/**
 * Sets the selection.
 * <p>
 * @param items new selection
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when items is null
 * </ul>
 */
public void setSelection (TableTreeItem[] items) {
	TableItem[] tableItems = new TableItem[items.length];
	for (int i = 0; i < items.length; i++) {
		if (items[i] == null) throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		if (!items[i].getVisible()) expandItem (items[i]);
		tableItems[i] = items[i].tableItem;
	}
	table.setSelection(tableItems);
}

/**
 * Sets the tool tip text.
 * <p>
 * @param string the new tool tip text (or null)
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setToolTipText (String string) {
	super.setToolTipText(string);
	table.setToolTipText(string);
}

/**
 * Shows the item.
 * <p>
 * @param item the item to be shown
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 *	<li>ERROR_NULL_ARGUMENT when item is null
 * </ul>
 */
public void showItem (TableTreeItem item) {
	if (item == null) throw new SWTError (SWT.ERROR_NULL_ARGUMENT);
	if (!item.getVisible()) expandItem (item);
	TableItem tableItem = item.tableItem;
	table.showItem(tableItem);
}

/**
 * Shows the selection.
 * <p>
 * If there is no selection or the selection
 * is already visible, this method does nothing.
 * If the selection is scrolled out of view,
 * the top index of the widget is changed such
 * that selection becomes visible.
 *
 * @exception SWTError <ul>
 *	<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *	<li>ERROR_WIDGET_DISPOSED when the widget has been disposed
 * </ul>
 */
public void showSelection () {
	table.showSelection();
}
}
