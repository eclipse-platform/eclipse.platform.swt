package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

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
 */
public class TreeItem extends Item {
	Tree parent;
	int index;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, 0, -1);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
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
public TreeItem (Tree parent, int style, int index) {
	super (parent, style);
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	parent.createItem (this, 0, index);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style) {
	super (checkNull (parentItem).getParent(), style);
	this.parent = parentItem.getParent ();
	parent.createItem (this, parentItem.handle, -1);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
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
public TreeItem (TreeItem parentItem, int style, int index) {
	super (checkNull (parentItem).getParent (), style);
	if (index < 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	this.parent = parentItem.getParent ();
	parent.createItem (this, parentItem.handle, index);
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	int ctree = parent.handle;
	GtkCTree tree = new GtkCTree(ctree);
/*
	double haj = OS.gtk_adjustment_get_value(tree.hadjustment);
	double vaj = OS.gtk_adjustment_get_value(tree.vadjustment);
	
	int columnHandle = tree.column;
	int height=parent.getItemHeight();

	int row_list = tree.row_list; int level=0;
	int count = OS.g_list_length (row_list);
	int index=0;
	while (index<count) {
		int data = OS.g_list_nth (row_list, index);
		if (handle == data){
			int rowHandle = OS.g_list_nth_data (row_list, index);
			GtkCTreeRow row = new GtkCTreeRow();
			OS.memmove(row, rowHandle, GtkCTreeRow.sizeof);
			level = row.level;
			break;
		}
		index++;
	}
	int y = height*index + Tree.CELL_SPACING + tree.voffset + 2;

	int [] buffer = new int [1]; byte [] spacing = new byte [1];
	boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
	int [] pixmap_closed = new int [1], mask_closed= new int [1], pixmap_opened= new int [1], mask_opened= new int [1];
	OS.gtk_ctree_get_node_info (ctree, handle, buffer, spacing, pixmap_closed, mask_closed, pixmap_opened, mask_opened, is_leaf, expanded);
	int length = OS.strlen (buffer[0]);
	byte [] buffer1 = new byte [length];
	OS.memmove (buffer1, buffer[0], length);
	int styleHandle = OS.gtk_ctree_node_get_row_style(ctree, handle);
	if (styleHandle == 0)
		styleHandle = OS.gtk_widget_get_style(ctree);
	GtkStyle style = new GtkStyle(styleHandle);*/
	/* FIXME */	
	int width = 50; /*OS.gdk_string_width(style.font, buffer1);*/

//	x = (short)column.area_x+tree.tree_indent*(level-1)+spacing[0]+tree.hoffset;
/*	int x = 33+tree.tree_indent*(level-1)+spacing[0]+tree.hoffset;*/

	return new Rectangle (0, 0, 40, 10);
}	

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return false;
	int ctree = parent.handle;
	int [] pixmap = new int [1];
	OS.gtk_ctree_get_node_info (ctree, handle, null, null, pixmap, null, null, null, null, null);
	return pixmap [0] == parent.check;
}

public Display getDisplay () {
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
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
 */
public boolean getExpanded () {
	checkWidget();
	int ctree = parent.handle;
	boolean [] buffer = new boolean [1];
	OS.gtk_ctree_get_node_info (ctree, handle, null, null, null, null, null, null, null, buffer);
	return buffer [0];
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed() {
	checkWidget();
	return false;
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
	checkWidget();
	return parent.getItemCount (handle);
}

/**
 * Returns an array of <code>TreeItem</code>s which are the
 * direct item children of the receiver.
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
	checkWidget();
	return parent.getItems (handle);
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
	checkWidget();
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
	checkWidget();
	int data = OS.g_list_nth_data (handle, 0);
	GtkCTreeRow row = new GtkCTreeRow (data);
	if (row.parent == 0) return null;
	int ctree = parent.handle;
	int index = OS.gtk_ctree_node_get_row_data (ctree, row.parent) - 1;
	return parent.items [index];
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

/**
 * Sets the checked state of the receiver.
 * <p>
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return;
	int ctree = parent.handle;
	byte [] spacing = new byte [1];
	int [] pixmap = new int [1], mask = new int [1];
	boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	OS.gtk_ctree_get_node_info (ctree, handle, null, spacing, pixmap, mask, pixmap, mask, is_leaf, expanded);
	if (checked && pixmap [0] == parent.check) return;
	if (!checked && pixmap [0] == parent.uncheck) return;
	pixmap [0] = checked ? parent.check : parent.uncheck;
	OS.gtk_ctree_set_node_info (ctree, handle, buffer, spacing [0], pixmap [0], mask [0], pixmap [0], mask [0], is_leaf [0], expanded [0]);				
}

/**
 * Sets the grayed state of the receiver.
 * <p>
 *
 * @param checked the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget();
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
	checkWidget();
	int ctree = parent.handle;
	if (expanded) {
		OS.gtk_signal_handler_block_by_data (ctree, SWT.Expand);
		OS.gtk_ctree_expand (ctree, handle);
		OS.gtk_signal_handler_unblock_by_data (ctree, SWT.Expand);
	} else {
		OS.gtk_signal_handler_block_by_data (ctree, SWT.Collapse);
		OS.gtk_ctree_collapse (ctree, handle);
		OS.gtk_signal_handler_unblock_by_data (ctree, SWT.Collapse);
	}
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((parent.style & SWT.CHECK) != 0) return;
	this.image = image;
	int pixmap = 0, mask = 0;
	if (image != null) {
		pixmap = image.pixmap;
		mask = image.mask;
	}
	int ctree = parent.handle;
	byte [] spacing = new byte [1];
	boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	OS.gtk_ctree_get_node_info (ctree, handle, null, spacing, null, null, null, null, is_leaf, expanded);
	OS.gtk_ctree_set_node_info (ctree, handle, buffer, spacing [0], pixmap, mask, pixmap, mask, is_leaf [0], expanded [0]);
}

/**
 * This label will be displayed to the right of the bitmap, 
 * or, if the receiver doesn't have a bitmap to the right of 
 * the horizontal hierarchy connector line.
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	int ctree = parent.handle;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	byte [] spacing = new byte [1];
	boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
	int [] pixmap_closed = new int [1], mask_closed= new int [1], pixmap_opened= new int [1], mask_opened= new int [1];
	OS.gtk_ctree_get_node_info (ctree, handle, null, spacing, pixmap_closed, mask_closed, pixmap_opened, mask_opened, is_leaf, expanded);
	OS.gtk_ctree_set_node_info (ctree, handle, buffer, spacing [0], pixmap_closed [0], mask_closed [0], pixmap_opened [0], mask_opened [0], is_leaf [0], expanded [0]);
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
	Tree parent = getParent();
	return parent.getForeground();
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
public void setForeground (Color color){
	checkWidget ();
	if (color != null && color.isDisposed ())
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
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
	Tree parent = getParent();
	return parent.getBackground();
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
	if (color != null && color.isDisposed ())
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
}
}
