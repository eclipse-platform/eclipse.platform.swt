/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TreeItem extends Item {
	Tree parent;
	TreeItem parentItem;
	TreeItem[] items;
	int itemCount;
	String [] strings;
	Image [] images;
	boolean checked, grayed, cached, expanded;
	Color foreground, background;
	Color [] cellForeground, cellBackground;
	Font font;
	Font [] cellFont;
	int width = -1;
	/**
	 * the handle to the OS resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public SWTTreeItem handle;

/**
 * Constructs <code>TreeItem</code> and <em>inserts</em> it into <code>Tree</code>.
 * Item is inserted as last direct child of the tree.
 * <p>
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 *
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
 * @param style no styles are currently supported, pass SWT.NONE
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style) {
	this (checkNull (parent), null, style, -1, true);
}

/**
 * Constructs <code>TreeItem</code> and <em>inserts</em> it into <code>Tree</code>.
 * Item is inserted as <code>index</code> direct child of the tree.
 * <p>
 * The fastest way to insert many items is:
 * <ol>
 * <li>Use {@link Tree#setRedraw} to disable drawing during bulk insert</li>
 * <li>Insert every item at index 0 (insert them in reverse to get the same result)</li>
 * <li>Collapse the parent item before inserting (gives massive improvement on Windows)</li>
 * </ol>
 *
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
 * @param style no styles are currently supported, pass SWT.NONE
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 * @see Tree#setRedraw
 */
public TreeItem (Tree parent, int style, int index) {
	this (checkNull (parent), null, style, checkIndex (index), true);
}

/**
 * Constructs <code>TreeItem</code> and <em>inserts</em> it into <code>Tree</code>.
 * Item is inserted as last direct child of the specified <code>TreeItem</code>.
 * <p>
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 *
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
 * @param style no styles are currently supported, pass SWT.NONE
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style) {
	this (checkNull (parentItem).parent, parentItem, style, -1, true);
}

/**
 * Constructs <code>TreeItem</code> and <em>inserts</em> it into <code>Tree</code>.
 * Item is inserted as <code>index</code> direct child of the specified <code>TreeItem</code>.
 * <p>
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 *
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
 * @param style no styles are currently supported, pass SWT.NONE
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 * @see Tree#setRedraw
 */
public TreeItem (TreeItem parentItem, int style, int index) {
	this (checkNull (parentItem).parent, parentItem, style, checkIndex (index), true);
}

TreeItem (Tree parent, TreeItem parentItem, int style, int index, boolean create) {
	super (parent, style);
	this.parent = parent;
	this.parentItem = parentItem;
	if (create) {
		parent.createItem (this, parentItem, index);
	} else {
		handle = (SWTTreeItem) new SWTTreeItem ().alloc ().init ();
		createJNIRef ();
		register ();
		items = new TreeItem[4];
	}
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static Tree checkNull (Tree parent) {
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return parent;
}

static int checkIndex (int index) {
	if (index < 0) SWT.error (SWT.ERROR_INVALID_RANGE);
	return index;
}

int calculateWidth (int index, GC gc) {
	if (index == 0 && width != -1) return width;
	Font font = null;
	if (cellFont != null) font = cellFont[index];
	if (font == null) font = this.font;
	if (font == null) font = parent.font;
	if (font == null) font = parent.defaultFont();
	String text = index == 0 ? this.text : (strings == null ? "" : strings [index]);
	if ((text != null) && (text.length() > TEXT_LIMIT)) {
		text = text.substring(0, TEXT_LIMIT - ELLIPSIS.length()) + ELLIPSIS;
	}
	Image image = index == 0 ? this.image : (images == null ? null : images [index]);
	NSCell cell = parent.dataCell;
	if (font.extraTraits != 0) {
		NSAttributedString attribStr = parent.createString(text, font, null, 0, false, true, false);
		cell.setAttributedStringValue(attribStr);
		attribStr.release();
	} else {
		cell.setFont (font.handle);
		NSString str = (NSString) new NSString().alloc();
		str = str.initWithString(text != null ? text : "");
		cell.setTitle (str);
		str.release();
	}

	/* This code is inlined for performance */
	objc_super super_struct = new objc_super();
	super_struct.receiver = cell.id;
	super_struct.super_class = OS.objc_msgSend(cell.id, OS.sel_superclass);
	NSSize size = new NSSize();
	OS.objc_msgSendSuper_stret(size, super_struct, OS.sel_cellSize);
	if (image != null) size.width += parent.imageBounds.width + Tree.IMAGE_GAP;
//	cell.setImage (image != null ? image.handle : null);
//	NSSize size = cell.cellSize ();

	int width = (int)Math.ceil (size.width);
	if (OS.isBigSurOrLater() && !OS.IS_X86_64) width += Tree.TEXT_GAP; // To fix truncation
	boolean sendMeasure = true;
	if ((parent.style & SWT.VIRTUAL) != 0) {
		sendMeasure = cached;
	}
	if (sendMeasure && parent.hooks (SWT.MeasureItem)) {
		gc.setFont (font);
		Event event = new Event ();
		event.item = this;
		event.index = index;
		event.gc = gc;
		NSTableView widget = (NSTableView)parent.view;
		int height = (int)widget.rowHeight ();
		event.width = width;
		event.height = height;
		event.detail = (cell.isHighlighted() && ((style & SWT.HIDE_SELECTION) == 0 || parent.hasFocus()))  ? SWT.SELECTED : 0;
		parent.sendEvent (SWT.MeasureItem, event);
		if (height < event.height) {
			widget.setRowHeight (event.height);
			widget.setNeedsDisplay (true);
		}
		width = event.width;
	}
	if (index == 0) {
		NSOutlineView outlineView = (NSOutlineView)parent.view;
		width += outlineView.indentationPerLevel () * (1 + outlineView.levelForItem (handle));
		this.width = width;
	}
	return width;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	cached = false;
	text = "";
	image = null;
	strings = null;
	images = null;
	checked = grayed = false;
	foreground = background = null;
	cellForeground = cellBackground = null;
	font = null;
	cellFont = null;
	width = -1;
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
	int count = getItemCount ();
	if (index < 0 || index >= count)
		error (SWT.ERROR_INVALID_RANGE);
	parent.clear (this, index, all);
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
	parent.clearAll (this, all);
}

NSObject createString(int index) {
	String text = index == 0 ? this.text : (strings == null ? "" : strings [index]);
	if ((text != null) && (text.length() > TEXT_LIMIT)) {
		text = text.substring(0, TEXT_LIMIT - ELLIPSIS.length()) + ELLIPSIS;
	}
	return NSString.stringWith(text != null ? text : "");
}

@Override
void dealloc (long id, long sel) {
	OS.object_setInstanceVariable(id, Display.SWT_OBJECT, 0);
	super.destroyJNIRef();
	super.dealloc(id, sel);
}

@Override
void deregister () {
	super.deregister ();
	//This is done in #dealloc
//	display.removeWidget (handle);
}

@Override
void destroyJNIRef () {
	//Do nothing - see #dealloc
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 *
 */
public Color getBackground () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return background != null ? background : parent.getBackground ();
}

/**
 * Returns the background color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Color getBackground (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count -1) return getBackground ();
	if (cellBackground == null || cellBackground [index] == null) return getBackground ();
	return cellBackground [index];
}

/**
 * Returns a rectangle describing the size and location of the receiver's
 * text relative to its parent.
 *
 * @return the bounding rectangle of the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	parent.checkItems ();

	NSOutlineView widget = (NSOutlineView) parent.view;
	int rowIndex = (int)widget.rowForItem (handle);
	if (rowIndex == -1) return new Rectangle (0, 0, 0, 0);

	NSTableColumn column = parent.columnCount == 0 ? parent.firstColumn : parent.columns [0].nsColumn;
	int columnIndex = parent.indexOf (column);
	NSRect titleRect = widget.frameOfCellAtColumn (columnIndex, rowIndex);
	if (image != null) {
		titleRect.x += parent.imageBounds.width + Tree.IMAGE_GAP;
	}
	Font font = null;
	if (cellFont != null) font = cellFont[columnIndex];
	if (font == null) font = this.font;
	if (font == null) font = parent.font;
	if (font == null) font = parent.defaultFont ();
	NSCell cell = parent.dataCell;
	cell.setImage (null);
	if (font.extraTraits != 0) {
		NSAttributedString attribStr = parent.createString (text, font, null, 0, false, true, false);
		cell.setAttributedStringValue (attribStr);
		attribStr.release ();
	} else {
		cell.setFont (font.handle);
		NSString str = (NSString) new NSString().alloc();
		str = str.initWithString(text);
		cell.setTitle (str);
		str.release();
	}
	// Inlined for performance.  Also prevents a NPE or potential loop, because cellSize() will
	// eventually send another MeasureItem event.
	objc_super super_struct = new objc_super();
	super_struct.receiver = cell.id;
	super_struct.super_class = OS.objc_msgSend(cell.id, OS.sel_superclass);
	NSSize size = new NSSize();
	OS.objc_msgSendSuper_stret(size, super_struct, OS.sel_cellSize);
//	NSSize size = cell.cellSize ();
	NSRect columnRect = widget.rectOfColumn (columnIndex);
	size.width = Math.min (size.width, columnRect.width - (titleRect.x - columnRect.x));
	return new Rectangle ((int)titleRect.x, (int)titleRect.y, (int)Math.ceil (size.width), (int)Math.ceil (titleRect.height));
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Rectangle getBounds (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!(0 <= index && index < Math.max (1, parent.columnCount))) return new Rectangle (0, 0, 0, 0);

	parent.checkItems ();
	NSOutlineView outlineView = (NSOutlineView) parent.view;
	if (parent.columnCount == 0) {
		index = (parent.style & SWT.CHECK) != 0 ? 1 : 0;
	} else {
		TreeColumn column = parent.getColumn (index);
		index = parent.indexOf (column.nsColumn);
	}
	NSRect rect = outlineView.frameOfCellAtColumn (index, outlineView.rowForItem (handle));
	return new Rectangle ((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return checked;
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget ();
	return expanded;
}

/**
 * Returns the font that the receiver will use to paint textual information for this item.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return font != null ? font : parent.getFont ();
}

/**
 * Returns the font that the receiver will use to paint textual information
 * for the specified cell in this item.
 *
 * @param index the column index
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Font getFont (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count -1) return getFont ();
	if (cellFont == null || cellFont [index] == null) return getFont ();
	return cellFont [index];
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 *
 */
public Color getForeground () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return foreground != null ? foreground : parent.getForeground ();
}

/**
 *
 * Returns the foreground color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Color getForeground (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count -1) return getForeground ();
	if (cellForeground == null || cellForeground [index] == null) return getForeground ();
	return cellForeground [index];
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return grayed;
}

@Override
public Image getImage () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getImage ();
}

/**
 * Returns the image stored at the given column index in the receiver,
 * or null if the image has not been set or if the column does not exist.
 *
 * @param index the column index
 * @return the image stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Image getImage (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getImage ();
	if (images != null) {
		if (0 <= index && index < images.length) return images [index];
	}
	return null;
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Rectangle getImageBounds (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!(0 <= index && index < Math.max (1, parent.columnCount))) return new Rectangle (0, 0, 0, 0);

	parent.checkItems ();
	NSOutlineView outlineView = (NSOutlineView) parent.view;
	Image image = index == 0 ? this.image : (images != null) ? images [index] : null;
	if (parent.columnCount == 0) {
		index = (parent.style & SWT.CHECK) != 0 ? 1 : 0;
	} else {
		TreeColumn column = parent.getColumn (index);
		index = parent.indexOf (column.nsColumn);
	}
	NSRect rect = outlineView.frameOfCellAtColumn (index, outlineView.rowForItem (handle));
	rect.x += Tree.IMAGE_GAP;
	if (image != null) {
		rect.width = parent.imageBounds.width;
	} else {
		rect.width = 0;
	}
	return new Rectangle((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
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
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
	return parent._getItem (this, index, true);
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.
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
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return itemCount;
}

/**
 * Returns a (possibly empty) array of <code>TreeItem</code>s which
 * are the direct item children of the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	TreeItem [] result = new TreeItem [itemCount];
	for (int i=0; i<itemCount; i++) {
		result [i] = parent._getItem (this, i, true);
	}
	return result;
}

@Override
String getNameText () {
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (!cached) return "*virtual*"; //$NON-NLS-1$
	}
	return super.getNameText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent () {
	checkWidget ();
	return parent;
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
	return parentItem;
}

@Override
public String getText () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getText ();
}

/**
 * Returns the text stored at the given column index in the receiver,
 * or empty string if the text has not been set.
 *
 * @param index the column index
 * @return the text stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public String getText (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getText ();
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of the text at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding text rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public Rectangle getTextBounds (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!(0 <= index && index < Math.max (1, parent.columnCount))) return new Rectangle (0, 0, 0, 0);

	parent.checkItems ();
	NSOutlineView outlineView = (NSOutlineView) parent.view;
	Image image = index == 0 ? this.image : (images != null) ? images [index] : null;
	if (parent.columnCount == 0) {
		index = (parent.style & SWT.CHECK) != 0 ? 1 : 0;
	} else {
		TreeColumn column = parent.getColumn (index);
		index = parent.indexOf (column.nsColumn);
	}
	NSRect rect = outlineView.frameOfCellAtColumn (index, outlineView.rowForItem (handle));
	rect.x += Tree.TEXT_GAP;
	rect.width -= Tree.TEXT_GAP;
	if (image != null) {
		int offset = parent.imageBounds.width + Tree.IMAGE_GAP;
		rect.x += offset;
		rect.width -= offset;
	}
	return new Rectangle((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
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
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parentItem != this) return -1;
	for (int i = 0; i < itemCount; i++) {
		if (item == items [i]) return i;
	}
	return -1;
}

@Override
boolean isDrawing () {
	return getDrawing() && parent.isDrawing ();
}

void redraw (int columnIndex) {
	if (parent.ignoreRedraw || !isDrawing()) return;
	/* redraw the full item if columnIndex == -1 */
	NSOutlineView outlineView = (NSOutlineView) parent.view;
	NSRect rect;
	if (columnIndex == -1 || parent.hooks (SWT.MeasureItem) || parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
		rect = outlineView.rectOfRow (outlineView.rowForItem (handle));
	} else {
		int index;
		if (parent.columnCount == 0) {
			index = (parent.style & SWT.CHECK) != 0 ? 1 : 0;
		} else {
			if (0 <= columnIndex && columnIndex < parent.columnCount) {
				index = parent.indexOf (parent.columns[columnIndex].nsColumn);
			} else {
				return;
			}
		}
		rect = outlineView.frameOfCellAtColumn (index, outlineView.rowForItem (handle));
	}
	outlineView.setNeedsDisplayInRect (rect);
}

@Override
void register () {
	super.register ();
	display.addWidget (handle, this);
}

@Override
void release(boolean destroy) {
	TreeItem[] selectedItems = null;
	Tree parent = this.parent;
	if (destroy) {
		if (getDrawing()) selectedItems = parent.getSelection ();
	}
	super.release(destroy);
	if (selectedItems != null) parent.selectItems (selectedItems, true);
}

@Override
void releaseChildren (boolean destroy) {
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	items = null;
	itemCount = 0;
	super.releaseChildren (destroy);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (handle != null) handle.autorelease ();
	handle = null;
	parentItem = null;
	parent = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	strings = null;
	images = null;
	background = foreground = null;
	font = null;
	cellBackground = cellForeground = null;
	cellFont = null;
}

/**
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void removeAll () {
	checkWidget ();
	parent.setItemCount (this, 0);
}

void sendExpand (boolean expand, boolean recurse) {
	if (itemCount == 0) return;
	if (expanded != expand) {
		Event event = new Event ();
		event.item = this;
		parent.sendEvent (expand ? SWT.Expand : SWT.Collapse, event);
		if (isDisposed ()) return;
		expanded = expand;
	}
	if (recurse) {
		for (int i = 0; i < itemCount; i++) {
			if (items[i] != null) items[i].sendExpand (expand, recurse);
		}
	}
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the item
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
 * @since 2.0
 *
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = background;
	if (oldColor == color) return;
	background = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (-1);
}

/**
 * Sets the background color at the given column index in the receiver
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
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
 * @since 3.1
 *
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellBackground == null) {
		if (color == null) return;
		cellBackground = new Color [count];
	}
	Color oldColor = cellBackground [index];
	if (oldColor == color) return;
	cellBackground [index] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (index);
}

/**
 * Sets the checked state of the receiver.
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	if (this.checked == checked) return;
	this.checked = checked;
	cached = true;
	redraw (-1);
}

/**
 * Sets the expanded state of the receiver.
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

	/* Do nothing when the item is a leaf or already expanded */
	if (itemCount == 0 || expanded == getExpanded ()) return;

	parent.checkItems ();
	parent.ignoreExpand = true;
	this.expanded = expanded;
	if (expanded) {
		((NSOutlineView) parent.view).expandItem (handle);
	} else {
		((NSOutlineView) parent.view).collapseItem (handle);
	}
	parent.ignoreExpand = false;
	cached = true;
	if (!expanded) {
		parent.setScrollWidth ();
	}
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
 *
 * @param font the new font (or null)
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
public void setFont (Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	width = -1;
	cached = true;
	redraw (-1);
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for the specified cell in this item to the font specified by the
 * argument, or to the default font for that kind of control if the
 * argument is null.
 *
 * @param index the column index
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setFont (int index, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellFont == null) {
		if (font == null) return;
		cellFont = new Font [count];
	}
	Font oldFont = cellFont [index];
	if (oldFont == font) return;
	cellFont [index] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	width = -1;
	cached = true;
	redraw (index);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
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
 * @since 2.0
 *
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = foreground;
	if (oldColor == color) return;
	foreground = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (-1);
}

/**
 * Sets the foreground color at the given column index in the receiver
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
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
 * @since 3.1
 *
 */
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellForeground == null) {
		if (color == null) return;
		cellForeground = new Color [count];
	}
	Color oldColor = cellForeground [index];
	if (oldColor == color) return;
	cellForeground [index] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (index);
}

/**
 * Sets the grayed state of the checkbox for this item.  This state change
 * only applies if the Tree was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	if (this.grayed == grayed) return;
	this.grayed = grayed;
	cached = true;
	redraw (-1);
}

/**
 * Sets the image for multiple columns in the tree.
 *
 * @param images the array of new images
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setImage (Image [] images) {
	checkWidget ();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
}

/**
 * Sets the receiver's image at a column.
 *
 * @param index the column index
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setImage (int index, Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (parent.imageBounds == null && image != null) {
		parent.setItemHeight (image, null, false);
	}
	if (index == 0)  {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (this.image)) return;
		}
		width = -1;
		super.setImage (image);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 <= index && index < count) {
		if (images == null) images = new Image [count];
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (images [index])) return;
		}
		images [index] = image;
	}
	cached = true;
	if (index == 0) parent.setScrollWidth (this);
	if (0 <= index && index < count) redraw (index);
}

@Override
public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the number of child items contained in the receiver.
 * <p>
 * The fastest way to insert many items is:
 * <ol>
 * <li>Use {@link Tree#setRedraw} to disable drawing during bulk insert</li>
 * <li>Collapse the parent item before inserting (gives massive improvement on Windows)</li>
 * </ol>
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
	parent.setItemCount (this, count);
}

/**
 * Sets the text for multiple columns in the tree.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 * @param strings the array of new strings
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setText (String [] strings) {
	checkWidget ();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
}

/**
 * Sets the receiver's text at a column
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 * @param index the column index
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void setText (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == 0) {
		if (string.equals (text)) return;
		width = -1;
		super.setText (string);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 <= index && index < count) {
		if (strings == null) strings = new String [count];
		if (string.equals (strings [index])) return;
		strings [index] = string;
	}
	cached = true;
	if (index == 0) parent.setScrollWidth (this);
	if (0 <= index && index < count) redraw (index);
}

@Override
public void setText (String string) {
	checkWidget ();
	setText (0, string);
}

void updateExpanded () {
	if (itemCount == 0) return;
	NSOutlineView outlineView = (NSOutlineView)parent.view;
	if (expanded != outlineView.isItemExpanded (handle)) {
		if (expanded) {
			outlineView.expandItem (handle);
		} else {
			outlineView.collapseItem (handle);
		}
	}
	for (int i = 0; i < itemCount; i++) {
		if (items[i] != null) items[i].updateExpanded ();
	}
}
}
