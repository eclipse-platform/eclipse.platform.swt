package org.eclipse.swt.custom;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A TableTreeItem is a selectable user interface object
 * that represents an item in a heirarchy of items in a
 * TableTree.
 */
public class TableTreeItem extends Item {
	TableItem tableItem;
	TableTree parent;
	TableTreeItem parentItem;
	TableTreeItem [] items = TableTree.EMPTY_ITEMS;
	String[] texts = TableTree.EMPTY_TEXTS;
	Image[] images = TableTree.EMPTY_IMAGES;
	boolean expanded;
	boolean checked;

/**
 * Create a new instance of a root item.
 *
 * @param parent the TableTree that contains this root item
 * @param style the bitwise OR'ing of widget styles
 */
public TableTreeItem(TableTree parent, int style) {
	this (parent, style, parent.getItemCount());
}

/**
 * Create a new instance of a root item in the position
 * indicated by the specified index.
 *
 * @param parent the TableTree that contains this root item
 * @param style the bitwise OR'ing of widget styles
 * @param index specifies the position of this item in the TableTree
 * 	relative to other root items
 */
public TableTreeItem(TableTree parent, int style, int index) {
	this (parent, null, style, index);
}

/**
 * Create a new instance of a sub item.
 *
 * @param parent this item's parent in the hierarchy of TableTree items
 * @param style the bitwise OR'ing of widget styles
 */
public TableTreeItem(TableTreeItem parent, int style) {
	this (parent, style, parent.getItemCount());
}

/**
 * Create a new instance of a sub item in the position
 * indicated by the specified index.
 *
 * @param parent this item's parent in the hierarchy of TableTree items
 * @param style the bitwise OR'ing of widget styles
 * @param index specifies the position of this item in the TableTree
 * 	relative to other children of the same parent
 */
public TableTreeItem(TableTreeItem parent, int style, int index) {
	this (parent.getParent(), parent, style, index);
}

TableTreeItem(TableTree parent, TableTreeItem parentItem, int style, int index) {
	super(parent, style);
	this.parent = parent;
	this.parentItem = parentItem;
	if (parentItem == null) {
		
		/* Root items are visible immediately */
		int tableIndex = parent.addItem(this, index);
		tableItem = new TableItem(parent.getTable(), style, tableIndex);
		tableItem.setData(this);
		addCheck();
		/*
		* Feature in the Table.  The table uses the first image that
		* is inserted into the table to size the table rows.  If the
		* user is allowed to insert the first image, this will cause
		* the +/- images to be scaled.  The fix is to insert a dummy
		* image to force the size.
		*/
		if (parent.sizeImage == null) {
			int itemHeight = parent.getItemHeight();
			parent.sizeImage = new Image(null, itemHeight, itemHeight);
			GC gc = new GC (parent.sizeImage);
			gc.setBackground(parent.getBackground());
			gc.fillRectangle(0, 0, itemHeight, itemHeight);
			gc.dispose();
			tableItem.setImage(0, parent.sizeImage);
		}
	} else {
		parentItem.addItem(this, index);
	}
	addDisposeListener(new DisposeListener() {
 		public void widgetDisposed(DisposeEvent e) {
 			TableTreeItem.this.widgetDisposed(e);
 		}
 	});	
}
void addCheck() {
	Table table = parent.getTable();
	if ((table.getStyle() & SWT.CHECK) == 0) return;
	tableItem.setChecked(checked);
}
void addItem(TableTreeItem item, int index) {
	if (item == null) throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
	if (index < 0 || index > items.length) throw new SWTError(SWT.ERROR_INVALID_ARGUMENT);
		
	/* Now that item has a sub-node it must indicate that it can be expanded */
	if (items.length == 0 && index == 0) {
		if (tableItem != null) {
			Image image = expanded ? parent.getMinusImage() : parent.getPlusImage();
			tableItem.setImage(0, image);
		}
	}
	
	/* Put the item in the items list */
	TableTreeItem[] newItems = new TableTreeItem[items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index);
	items = newItems;
	if (expanded) item.setVisible(true);
}

/**
 * Gets the widget bounds at the specified index.
 * <p>
 * @return the widget bounds at the specified index
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li> 
 *	</ul>
 */
public Rectangle getBounds (int index) {
	if (tableItem != null) {
		return tableItem.getBounds(index);
	} else {
		return new Rectangle(0, 0, 0, 0);
	}
}
/**
* Gets the checked state.
* <p>
* @return the item checked state.
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public boolean getChecked () {
	if (tableItem == null) {
		return checked;
	}
	return tableItem.getChecked();
}

/**
 * Gets the Display.
 * <p>
 * This method gets the Display that is associated
 * with the widget.
 *
 * @return the widget data
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public Display getDisplay () {
	TableTree parent = this.parent;
	if (parent == null) throw new SWTError (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Gets the expanded state of the widget.
 * <p>
 * @return a boolean that is the expanded state of the widget
 */
public boolean getExpanded () {
	return expanded;
}

/**
 * Gets the first image.
 * <p>
 * The image in column 0 is reserved for the [+] and [-]
 * images of the tree, therefore getImage(0) will return null.
 *
 * @return the image at index 0
 */
public Image getImage () {
	return getImage(0);
}

/**
 * Gets the image at the specified index.
 * <p>
 * Indexing is zero based. The image can be null.
 * The image in column 0 is reserved for the [+] and [-]
 * images of the tree, therefore getImage(0) will return null.
 * Return null if the index is out of range.
 *
 * @param index the index of the image
 * @return the image at the specified index or null
 */
public Image getImage (int index) {
	if (0 < index && index < images.length) return images[index];
	return null;
}

int getIndent() {
	if (parentItem == null) return 0;
	return parentItem.getIndent() + 1;
}

/**
 * Gets the number of sub items.
 * <p>
 * @return the number of sub items
 */
public int getItemCount () {
	return items.length;
}

/**
 * Gets the sub items.
 * <p>
 * @return the sub items
 */
public TableTreeItem[] getItems () {
	TableTreeItem[] newItems = new TableTreeItem[items.length];
	System.arraycopy(items, 0, newItems, 0, items.length);
	return newItems;
}

TableTreeItem getItem(TableItem tableItem) {
	if (tableItem == null) return null;
	if (this.tableItem == tableItem) return this;
	for (int i = 0; i < items.length; i++) {
		TableTreeItem item =  items[i].getItem(tableItem);
	    	if (item != null) return item;
	}
	return null;
}

/**
 * Gets the parent.
 * <p>
 * @return the parent
 */
public TableTree getParent () {
	return parent;
}

/**
 * Gets the parent item.
 * <p>
 * @return the parent item.
 */
public TableTreeItem getParentItem () {
	return parentItem;
}

/**
 * Gets the first item text.
 * <p>
 * @return the item text at index 0, which can be null
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_CANNOT_GET_TEXT when the operation fails</li>
 *	</ul>
 */
public String getText () {
	return getText(0);
}

/**
 * Gets the item text at the specified index.
 * <p>
 * Indexing is zero based.
 *
 * This operation will fail when the index is out
 * of range or an item could not be queried from
 * the OS.
 *
 * @param index the index of the item
 * @return the item text at the specified index, which can be null
 */
public String getText(int index) {
	if (0 <= index && index < texts.length) return texts[index];
	return null;
}

boolean getVisible () {
	return tableItem != null;
}

/**
 * Gets the index of the specified item.
 * 
 * <p>The widget is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.  This index is relative to the parent only.
 *
 * @param item the search item
 * @return the index of the item or -1 if the item is not found
 *
 */
public int indexOf (TableTreeItem item) {	
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return i;
	}
	return -1;
}

int expandedIndexOf (TableTreeItem item) {	
	int index = 0;
	for (int i = 0; i < items.length; i++) {
		if (items[i] == item) return index;
		if (items[i].expanded) index += items[i].visibleChildrenCount ();
		index++;
	}
	return -1;
}

int visibleChildrenCount () {
	int count = 0;
	for (int i = 0; i < items.length; i++) {
		if (items[i].getVisible ()) {
			count += 1 + items[i].visibleChildrenCount ();
		}
	}
	return count;
}

void widgetDisposed(DisposeEvent e) {
	for (int i = items.length - 1; i >= 0; i--) {
		items[i].dispose();
	}
	if (!parent.inDispose) {
		if (parentItem != null) {
			parentItem.removeItem(this);
		} else {
			parent.removeItem(this);
		}
		if (tableItem != null) tableItem.dispose();
	}
	items = null;
	parentItem = null;
	parent = null;
	images = null;
	texts = null;
	tableItem = null;	
}

void removeItem(TableTreeItem item) {
	int index = 0;
	while (index < items.length && items[index] != item) index++;
	if (index == items.length) return;
	TableTreeItem[] newItems = new TableTreeItem[items.length - 1];
	System.arraycopy(items, 0, newItems, 0, index);
	System.arraycopy(items, index + 1, newItems, index, items.length - index - 1);
	items = newItems;
	if (items.length == 0) {
		if (tableItem != null) tableItem.setImage(0, null);
	}
}

/**
* Sets the checked state.
* <p>
* @param checked the new checked state.
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public void setChecked (boolean checked) {
	if (tableItem != null) {
		tableItem.setChecked(checked);
	}
	this.checked = checked;
}
/**
 * Sets the expanded state.
 * <p>
 * @param expanded the new expanded state.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setExpanded (boolean expanded) {
	if (items.length == 0) return;
	this.expanded = expanded;
	if (tableItem == null) return;
	parent.setRedraw(false);
	for (int i = 0; i < items.length; i++) {
		items[i].setVisible(expanded);
	}
	Image image = expanded ? parent.getMinusImage() : parent.getPlusImage();
	tableItem.setImage(0, image);
	parent.setRedraw(true);
}

/**
 * Sets the image at an index.
 * <p>
 * The image can be null.
 * The image in column 0 is reserved for the [+] and [-]
 * images of the tree, therefore do nothing if index is 0.
 *
 * @param image the new image or null
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public void setImage (int index, Image image) {
	int columnCount = Math.max(parent.getTable().getColumnCount(), 1);
	if (index <= 0 || index >= columnCount) return;
	if (images.length < columnCount) {
		Image[] newImages = new Image[columnCount];
		System.arraycopy(images, 0, newImages, 0, images.length);
		images = newImages;
	}
	images[index] = image;
	if (tableItem != null) tableItem.setImage(index, image);
}

/**
 * Sets the first image.
 * <p>
 * The image can be null.
 * The image in column 0 is reserved for the [+] and [-]
 * images of the tree, therefore do nothing.
 *
 * @param image the new image or null
 */
public void setImage (Image image) {
	setImage(0, image);
}

/**
 * Sets the widget text.
 * <p>
 *
 * The widget text for an item is the label of the
 * item or the label of the text specified by a column
 * number.
 *
 * @param index the column number
 * @param text the new text
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_NULL_ARGUMENT when string is null</li>
 *	</ul>
 */
public void setText(int index, String text) {
	int columnCount = Math.max(parent.getTable().getColumnCount(), 1);
	if (index < 0 || index >= columnCount) return;
	if (texts.length < columnCount) {
		String[] newTexts = new String[columnCount];
		System.arraycopy(texts, 0, newTexts, 0, texts.length);
		texts = newTexts;
	}
	texts[index] = text;
	if (tableItem != null) tableItem.setText(index, text);
}

/**
 * Sets the widget text.
 * <p>
 *
 * The widget text for an item is the label of the
 * item or the label of the text specified by a column
 * number.
 *
 * @param index the column number
 * @param text the new text
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *		<li>ERROR_NULL_ARGUMENT when string is null</li>
 *	</ul>
 */
public void setText (String string) {
	setText(0, string);
}

void setVisible (boolean show) {
	if (parentItem == null) return; // this is a root and can not be toggled between visible and hidden
	if (getVisible() == show) return;

	if (show) {
		if (!parentItem.getVisible()) return; // parentItem must already be visible
		// create underlying table item and set data in table item to stored data
		Table table = parent.getTable();
		int parentIndex = table.indexOf(parentItem.tableItem);
		int index = parentItem.expandedIndexOf(this) + parentIndex + 1;
		if (index < 0) return;
		tableItem = new TableItem(table, getStyle(), index);
		tableItem.setData(this);
		tableItem.setImageIndent(getIndent());
		addCheck();

		// restore fields to item
		// ignore any images in the first column
		int columnCount = Math.max(table.getColumnCount(), 1);
		for (int i = 0; i < columnCount; i++) {
			if (i < texts.length && texts[i] != null) setText(i, texts[i]);
			if (i < images.length && images[i] != null) setImage(i, images[i]);
		}

		// display the children and the appropriate [+]/[-] symbol as required
		if (items.length != 0) {
			if (expanded) {
				tableItem.setImage(0, parent.getMinusImage());
				for (int i = 0, length = items.length; i < length; i++) {
					items[i].setVisible(true);
				}
			} else {
				tableItem.setImage(0, parent.getPlusImage());
			}
		}
		
	} else {

		for (int i = 0, length = items.length; i < length; i++) {
			items[i].setVisible(false);
		}
		// remove row from table
		tableItem.dispose();
		tableItem = null;
	}
}
}
