package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import java.util.Vector;

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

/*
 *   ===  CONSTRUCTORS  ===
 */


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
	this.parent=parent;
	parent.createItem (this, 0, -1);
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
	super ( (checkNull(parentItem)).getParent(), style );
	this.parent = parentItem.getParent ();
	parent.createItem (this, parentItem.handle, -1);
}
private static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
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
public TreeItem (TreeItem parentItem, int style, int position) {
	super (parentItem.getParent (), style);
	if (position < 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	this.parent = parentItem.getParent ();
	parent.createItem (this, parentItem.handle, -1);
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
	checkWidget();
	int CELL_SPACING=1;
	int ctree = parent.handle;
	GtkCTree tree = new GtkCTree();
	OS.memmove(tree, ctree, GtkCTree.sizeof);

	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, tree.vadjustment, GtkAdjustment.sizeof);
	float vaj = adjustment.value;
	OS.memmove (adjustment, tree.hadjustment, GtkAdjustment.sizeof);
	float haj = adjustment.value;
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
	int y=height*index+(index+1)*CELL_SPACING+tree.voffset+2;

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
	GtkStyle style = new GtkStyle();
	OS.memmove(style, styleHandle, GtkStyle.sizeof);	
	int width = OS.gdk_string_width(style.font, buffer1);

//	x = (short)column.area_x+tree.tree_indent*(level-1)+spacing[0]+tree.hoffset;
	int x = 33+tree.tree_indent*(level-1)+spacing[0]+tree.hoffset;

	return new Rectangle (x, y, width, height);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return false;
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
	int treeHandle = parent.handle;
	boolean [] buffer = new boolean [1];
	OS.gtk_ctree_get_node_info (treeHandle, handle, null, null, null, null, null, null, null, buffer);
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

	// NOT_IMPLEMENTED
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int data = OS.g_list_nth_data (handle, 0);
	GtkCTreeRow row = new GtkCTreeRow ();
	OS.memmove (row, data, GtkCTreeRow.sizeof);
	int glist = row.children;
	if (glist == 0) return 0;
	return OS.g_list_length (glist);
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
	Vector items = _getItemsVector();
	int s = items.size();
	TreeItem[] answer = new TreeItem[s];
	items.toArray(answer);
	return answer;
}
private Vector _getItemsVector() {
	Vector answer = new Vector();


	int data = OS.g_list_nth_data (handle, 0);
	GtkCTreeRow row = new GtkCTreeRow ();
	OS.memmove (row, data, GtkCTreeRow.sizeof);
	int glist = row.children;
	if (glist == 0) return answer;
	int ctree = parent.handle;
	int count = OS.g_list_length (glist);

	for (int i=0; i<count; i++) {
		int node = OS.g_list_nth (glist, i);
		int index = OS.gtk_ctree_node_get_row_data (ctree, node) - 1;
		TreeItem item =  parent.items [index];
		if (item._getParentItem() == this) answer.add(item);
	}

	return answer;
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
	return _getParentItem();
}
private TreeItem _getParentItem () {
	int data = OS.g_list_nth_data (handle, 0);
	GtkCTreeRow row = new GtkCTreeRow ();
	OS.memmove (row, data, GtkCTreeRow.sizeof);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return;
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
public void setGrayed(boolean grayed) {  // NOT_IMPLEMENTED
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int treeHandle = parent.handle;
	if (expanded) {
		parent.ignoreExpand = true;
		OS.gtk_ctree_expand (treeHandle, handle);
		parent.ignoreExpand = false;
	} else {
		parent.ignoreCollapse = true;
		OS.gtk_ctree_collapse (treeHandle, handle);
		parent.ignoreCollapse = false;
	}
}
public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	super.setImage (image);
	int pixmap = 0, mask = 0;
	if (image != null) {
		pixmap = image.pixmap;
		mask = image.mask;
	}
	int ctree = parent.handle;
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	byte [] spacing = new byte [1];
	boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
	OS.gtk_ctree_get_node_info (ctree, handle, null, spacing, null, null, null, null, is_leaf, expanded);
	OS.gtk_ctree_set_node_info (ctree, handle, buffer, spacing [0], pixmap, mask, pixmap, mask, is_leaf [0], expanded [0]);
}
/**
 * This label will be displayed to the right of the bitmap, 
 * or, if the receiver doesn't have a bitmap to the right of 
 * the horizontal hierarchy connector line.
 */
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
}
