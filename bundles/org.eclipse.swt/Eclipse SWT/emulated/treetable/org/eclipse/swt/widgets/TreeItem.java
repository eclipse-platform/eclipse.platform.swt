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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;

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
	TreeItem[] items = Tree.NO_ITEMS;
	int availableIndex = -1;	/* index in parent's flat list of available (though not necessarily within viewport) items */
	int depth = 0;				/* cached for performance, does not change after instantiation */
	boolean checked, grayed, expanded, cached;

	String[] texts;
	int[] textWidths = new int [1];	/* cached string measurements */
	int customWidth = -1;				/* width specified by Measure callback */
	int fontHeight;						/* cached item font height */
	int[] fontHeights;
	Image[] images;
	Color foreground, background;
	String[] displayTexts;
	Color[] cellForegrounds, cellBackgrounds;
	Font font;
	Font[] cellFonts;
	
	static final int INDENT_HIERARCHY = 6;	/* the margin between an item's expander and its checkbox or content */
	static final int MARGIN_TEXT = 3;			/* the left and right margins within the text's space */

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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
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
	this (parent, style, checkNull (parent).items.length);
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
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
 */
public TreeItem (Tree parent, int style, int index) {
	this (parent, style, index, true);
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
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
	this (parentItem, style, checkNull (parentItem).items.length);
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
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
 */
public TreeItem (TreeItem parentItem, int style, int index) {
	this (parentItem, style, index, true);
}
TreeItem (TreeItem parentItem, int style, int index, boolean notifyParent) {
	super (parentItem, style);
	this.parentItem = parentItem;
	parent = parentItem.parent;
	depth = parentItem.depth + 1;
	int validItemIndex = parentItem.items.length;
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	int columnCount = parent.columns.length;
	if (columnCount > 0) {
		displayTexts = new String [columnCount];
		if (columnCount > 1) {
			texts = new String [columnCount];
			textWidths = new int [columnCount];
			images = new Image [columnCount];
		}
	}
	if (notifyParent) parentItem.addItem (this, index);
}
TreeItem (Tree parent, int style, int index, boolean notifyParent) {
	super (parent, style);
	int validItemIndex = parent.items.length;
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	int columnCount = parent.columns.length;
	if (columnCount > 0) {
		displayTexts = new String [columnCount];
		if (columnCount > 1) {
			texts = new String [columnCount];
			textWidths = new int [columnCount];
			images = new Image [columnCount];
		}
	}
	if (notifyParent) parent.createItem (this, index);
}
/*
 * Updates internal structures in the receiver and its child items to handle the creation of a new column.
 */
void addColumn (TreeColumn column) {
	int index = column.getIndex ();
	int columnCount = parent.columns.length;

	if (columnCount > 1) {
		if (columnCount == 2) {
			texts = new String [2];
		} else {
			String[] newTexts = new String [columnCount];
			System.arraycopy (texts, 0, newTexts, 0, index);
			System.arraycopy (texts, index, newTexts, index + 1, columnCount - index - 1);
			texts = newTexts;
		}
		if (index == 0) {
			texts [1] = text;
			text = "";	//$NON-NLS-1$
		}

		if (columnCount == 2) {
			images = new Image [2];
		} else {
			Image[] newImages = new Image [columnCount];
			System.arraycopy (images, 0, newImages, 0, index);
			System.arraycopy (images, index, newImages, index + 1, columnCount - index - 1);
			images = newImages;
		}
		if (index == 0) {
			images [1] = image;
			image = null;
		}
		
		int[] newTextWidths = new int [columnCount];
		System.arraycopy (textWidths, 0, newTextWidths, 0, index);
		System.arraycopy (textWidths, index, newTextWidths, index + 1, columnCount - index - 1);
		textWidths = newTextWidths;
	} else {
		customWidth = -1;		/* columnCount == 1 */
	}

	/*
	 * The length of displayTexts always matches the parent's column count, unless this
	 * count is zero, in which case displayTexts is null.  
	 */
	String[] newDisplayTexts = new String [columnCount];
	if (columnCount > 1) {
		System.arraycopy (displayTexts, 0, newDisplayTexts, 0, index);
		System.arraycopy (displayTexts, index, newDisplayTexts, index + 1, columnCount - index - 1);
	}
	displayTexts = newDisplayTexts;

	if (cellBackgrounds != null) {
		Color[] newCellBackgrounds = new Color [columnCount];
		System.arraycopy (cellBackgrounds, 0, newCellBackgrounds, 0, index);
		System.arraycopy (cellBackgrounds, index, newCellBackgrounds, index + 1, columnCount - index - 1);
		cellBackgrounds = newCellBackgrounds;
	}
	if (cellForegrounds != null) {
		Color[] newCellForegrounds = new Color [columnCount];
		System.arraycopy (cellForegrounds, 0, newCellForegrounds, 0, index);
		System.arraycopy (cellForegrounds, index, newCellForegrounds, index + 1, columnCount - index - 1);
		cellForegrounds = newCellForegrounds;
	}
	if (cellFonts != null) {
		Font[] newCellFonts = new Font [columnCount];
		System.arraycopy (cellFonts, 0, newCellFonts, 0, index);
		System.arraycopy (cellFonts, index, newCellFonts, index + 1, columnCount - index - 1);
		cellFonts = newCellFonts;

		int[] newFontHeights = new int [columnCount];
		System.arraycopy (fontHeights, 0, newFontHeights, 0, index);
		System.arraycopy (fontHeights, index, newFontHeights, index + 1, columnCount - index - 1);
		fontHeights = newFontHeights;
	}

	int orderedIndex = column.getOrderIndex ();
	if (orderedIndex == 0 && columnCount > 1) {
		/*
		 * The new second ordered column now has more space available to it than it did while
		 * it was the first ordered column since it no longer has to show hierarchy decorations,
		 * so recompute its displayText.
		 */
		TreeColumn[] orderedColumns = parent.getOrderedColumns ();
		int secondColumnIndex = orderedColumns [1].getIndex ();
		GC gc = new GC (parent);
		gc.setFont (getFont (secondColumnIndex, false));
		computeDisplayText (secondColumnIndex, gc);
		gc.dispose ();
	}
	
	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items[i].addColumn (column);
	}
}
/*
 * Adds a child item to the receiver.
 */
void addItem (TreeItem item, int index) {
	TreeItem[] newChildren = new TreeItem [items.length + 1];
	System.arraycopy (items, 0, newChildren, 0, index);
	newChildren [index] = item;
	System.arraycopy (items, index, newChildren, index + 1, items.length - index);
	items = newChildren;

	if (!item.isAvailable ()) {
		/* receiver will now need an expander box if this is its first child */
		if (isInViewport () && items.length == 1) {
			Rectangle bounds = getExpanderBounds ();
			parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
		return;
	}
	
	/* item should be available immediately so update parent */
	parent.makeAvailable (item);
	
	/* update scrollbars */
	Rectangle bounds = item.getBounds (false);
	int rightX = bounds.x + bounds.width;
	parent.updateHorizontalBar (rightX, rightX);
	parent.updateVerticalBar ();
	/* 
	 * If new item is above viewport then adjust topIndex and the vertical scrollbar
	 * so that the current viewport items will not change. 
	 */
	if (item.availableIndex < parent.topIndex) {
		parent.topIndex++;
		ScrollBar vBar = parent.getVerticalBar ();
		if (vBar != null) vBar.setSelection (parent.topIndex);
		return;
	}
	
	parent.redrawFromItemDownwards (availableIndex);
}
static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}
static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void clear () {
	checked = grayed = false;
	texts = null;
	textWidths = new int [1];
	fontHeight = 0;
	fontHeights = null;
	images = null;
	foreground = background = null;
	displayTexts = null;
	cellForegrounds = cellBackgrounds = null;
	font = null;
	cellFonts = null;
	cached = false;
	text = "";
	image = null;

	int columnCount = parent.columns.length;
	if (columnCount > 0) {
		displayTexts = new String [columnCount];
		if (columnCount > 1) {
			texts = new String [columnCount];
			textWidths = new int [columnCount];
			images = new Image [columnCount];
		}
	}
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
public void clear (int index, boolean recursive) {
	checkWidget ();
	if (!(0 <= index && index < items.length)) error (SWT.ERROR_INVALID_RANGE);
	TreeItem item = items [index];

	/* if there are no columns then the horizontal scrollbar may need adjusting */
	TreeItem[] availableDescendents = null;
	int oldRightX = 0;
	if (item.availableIndex != -1 && parent.columns.length == 0) {
		if (recursive) {
			availableDescendents = item.computeAvailableDescendents ();
			for (int i = 0; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				oldRightX = Math.max (oldRightX, bounds.x + bounds.width);
			}
		} else {
			Rectangle bounds = item.getBounds (false);
			oldRightX = bounds.x + bounds.width;
		}
	}

	/* clear the item(s) */
	item.clear ();
	if (recursive) {
		item.clearAll (true, false);
	}
	if (item.availableIndex == -1) return;	/* no visual update needed */

	/* adjust the horizontal scrollbar if needed */
	if (parent.columns.length == 0) {
		int newRightX = 0;
		if (recursive) {
			for (int i = 0; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				newRightX = Math.max (newRightX, bounds.x + bounds.width);
			}
		} else {
			Rectangle bounds = item.getBounds (false);
			newRightX = bounds.x + bounds.width;
		}
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}

	/* redraw the item(s) */
	if (recursive) {
		int descendentCount = availableDescendents == null ?
			item.computeAvailableDescendentCount () :
			availableDescendents.length;
		parent.redrawItems (item.availableIndex, item.availableIndex + descendentCount - 1, false);
	} else {
		parent.redrawItem (item.availableIndex, false);
	}
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
public void clearAll (boolean recursive) {
	clearAll (recursive, true);
}
void clearAll (boolean recursive, boolean doVisualUpdate) {
	checkWidget ();
	if (items.length == 0) return;

	/* if there are no columns then the horizontal scrollbar may need adjusting */
	TreeItem[] availableDescendents = null;
	int oldRightX = 0;
	if (doVisualUpdate && availableIndex != -1 && expanded && parent.columns.length == 0) {
		if (recursive) {
			availableDescendents = computeAvailableDescendents ();
			/*
			 * i starts at 1 here because item 0 in availableDescendents
			 * will be the receiver, but this item is not being cleared.
			 */
			for (int i = 1; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				oldRightX = Math.max (oldRightX, bounds.x + bounds.width);
			}
		} else {
			for (int i = 0; i < items.length; i++) {
				Rectangle bounds = items [i].getBounds (false);
				oldRightX = Math.max (oldRightX, bounds.x + bounds.width);
			}
		}
	}

	/* clear the item(s) */
	for (int i = 0; i < items.length; i++) {
		items [i].clear ();
		if (recursive) items [i].clearAll (true, false);
	}

	if (!doVisualUpdate || availableIndex == -1 || !expanded) return;	/* no visual update needed */

	/* adjust the horizontal scrollbar if needed */
	if (parent.columns.length == 0) {
		int newRightX = 0;
		if (recursive) {
			/*
			 * i starts at 1 here because item 0 in availableDescendents
			 * is the receiver, but this item was not cleared.
			 */
			for (int i = 1; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				newRightX = Math.max (newRightX, bounds.x + bounds.width);
			}
		} else {
			/*
			 * All cleared direct child items will have the same x and width
			 * values now, so just measure the first one as a sample.
			 */
			Rectangle bounds = items [0].getBounds (false);
			newRightX = bounds.x + bounds.width;
		}
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}

	/* redraw the item(s) */
	if (recursive) {
		int startIndex = items [0].availableIndex;
		TreeItem lastChild = items [items.length - 1]; 
		int endIndex = lastChild.availableIndex + lastChild.computeAvailableDescendentCount () - 1;
		parent.redrawItems (startIndex, endIndex, false);
	} else {
		for (int i = 0; i < items.length; i++) {
			parent.redrawItem (items [i].availableIndex, false);
		}
	}
}
/*
 * Returns a collection of all tree items descending from the receiver, including
 * the receiver.  The order of the items in this collection are receiver, child0tree,
 * child1tree, ..., childNtree. 
 */
TreeItem[] computeAllDescendents () {
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem [childCount][];
	int count = 1;	/* receiver */
	for (int i = 0; i < childCount; i++) {
		childResults [i] = items [i].computeAllDescendents ();
		count += childResults [i].length;
	}
	TreeItem[] result = new TreeItem [count];
	int index = 0;
	result [index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults [i], 0, result, index, childResults [i].length);
		index += childResults [i].length;
	}
	return result;
}
/*
 * Returns the number of tree items descending from the receiver, including the
 * receiver, that are currently available.  It is assumed that the receiver is
 * currently available. 
 */
int computeAvailableDescendentCount () {
	int result = 1;		/* receiver */
	if (!expanded) return result;
	for (int i = 0; i < items.length; i++) {
		result += items [i].computeAvailableDescendentCount ();
	}
	return result;
}
/*
 * Returns a collection of the tree items descending from the receiver, including
 * the receiver, that are currently available.  It is assumed that the receiver is
 * currently available.  The order of the items in this collection are receiver,
 * child0tree, child1tree, ..., childNtree. 
 */
TreeItem[] computeAvailableDescendents () {
	if (!expanded) return new TreeItem[] {this};
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem [childCount][];
	int count = 1;	/* receiver */
	for (int i = 0; i < childCount; i++) {
		childResults [i] = items [i].computeAvailableDescendents ();
		count += childResults [i].length;
	}
	TreeItem[] result = new TreeItem [count];
	int index = 0;
	result [index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults [i], 0, result, index, childResults [i].length);
		index += childResults [i].length;
	}
	return result;
}
void computeDisplayText (int columnIndex, GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int columnCount = parent.columns.length;
	if (columnCount == 0) {
		String text = getText (0, false);
		textWidths [columnIndex] = gc.stringExtent (text).x;
		return;
	}

	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	TreeColumn column = parent.columns [columnIndex];
	int availableWidth;
	if (orderedIndex == 0) {
		/* ordered column 0 is always LEFT and must consider hierarchy decorations */
		availableWidth = column.getX () + column.width - getTextX (columnIndex) - 2 * MARGIN_TEXT;
	} else {
		/* ordered columns > 0 may not be LEFT so cannot use getTextX (int) */
		availableWidth = column.width - 2 * parent.getCellPadding () - 2 * MARGIN_TEXT;
		if (images [columnIndex] != null) {
			availableWidth -= images [columnIndex].getBounds ().width;
			availableWidth -= Tree.MARGIN_IMAGE;
		}
	}
	String text = getText (columnIndex, false);
	int textWidth = gc.stringExtent (text).x;
	if (textWidth <= availableWidth) {
		displayTexts [columnIndex] = text;
		textWidths [columnIndex] = textWidth;
		return;
	}

	/* Ellipsis will be needed, so subtract their width from the available text width */
	int ellipsisWidth = gc.stringExtent (Tree.ELLIPSIS).x;
	availableWidth -= ellipsisWidth;
	if (availableWidth <= 0) {
		displayTexts [columnIndex] = Tree.ELLIPSIS;
		textWidths [columnIndex] = ellipsisWidth;
		return;
	}
	
	/* Make initial guess. */
	int index = Math.min (availableWidth / gc.getFontMetrics ().getAverageCharWidth (), text.length ());
	textWidth = gc.stringExtent (text.substring (0, index)).x;

	/* Initial guess is correct. */
	if (availableWidth == textWidth) {
		displayTexts [columnIndex] = text.substring (0, index) + Tree.ELLIPSIS;
		textWidths [columnIndex] = textWidth + ellipsisWidth;
		return;
	}

	/* Initial guess is too high, so reduce until fit is found. */
	if (availableWidth < textWidth) {
		do {
			index--;
			if (index < 0) {
				displayTexts [columnIndex] = Tree.ELLIPSIS;
				textWidths [columnIndex] = ellipsisWidth;
				return;
			}
			text = text.substring (0, index);
			textWidth = gc.stringExtent (text).x;
		} while (availableWidth < textWidth);
		displayTexts [columnIndex] = text + Tree.ELLIPSIS;
		textWidths [columnIndex] = textWidth + ellipsisWidth;
		return;
	}
	
	/* Initial guess is too low, so increase until overrun is found. */
	int previousWidth = 0;
	while (textWidth < availableWidth) {
		index++;
		previousWidth = textWidth;
		textWidth = gc.stringExtent (text.substring (0, index)).x;
	}
	displayTexts [columnIndex] = text.substring (0, index - 1) + Tree.ELLIPSIS;
	textWidths [columnIndex] = previousWidth + ellipsisWidth;
}
void computeDisplayTexts (GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int columnCount = parent.columns.length;
	if (columnCount == 0) return;
	
	for (int i = 0; i < columnCount; i++) {
		gc.setFont (getFont (i, false));
		computeDisplayText (i, gc);
	}
}
/*
 * Computes the cached text widths.
 */
void computeTextWidths (GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int validColumnCount = Math.max (1, parent.columns.length);
	textWidths = new int [validColumnCount];
	for (int i = 0; i < textWidths.length; i++) {
		String value = getDisplayText (i);
		if (value != null) {
			gc.setFont (getFont (i, false));
			textWidths [i] = gc.stringExtent (value).x;
		}
	}
}
public void dispose () {
	if (isDisposed ()) return;
	int startIndex = -1, endIndex = -1;
	Tree parent = this.parent;
	int index = getIndex ();
	
	/* determine the indices, if any, that will need to be visually updated */
	if (isAvailable ()) {
		if (isLastChild () && index > 0) {
			/* vertical connector lines no longer needed for this item */
			if (parentItem != null) {
				startIndex = parentItem.items [index - 1].availableIndex;
			} else {
				startIndex = parent.items [index - 1].availableIndex;
			}
		} else {
			startIndex = availableIndex;
		}
		endIndex = parent.availableItemsCount - 1;
	}

	/* for performance do this upfront for whole descendent chain */
	TreeItem focusItem = parent.focusItem; 
	if (focusItem != null && focusItem.hasAncestor (this)) {
		parent.setFocusItem (this, false);
		parent.reassignFocus ();
		focusItem = parent.focusItem;
		if (focusItem != null) {
			parent.redrawItem (focusItem.availableIndex, true);
		}
	}
	if (parentItem != null) parentItem.removeItem (this, index);
	dispose (true);
	if (startIndex != -1) {
		parent.redrawItems (startIndex, endIndex, false);
	}
}
void dispose (boolean notifyParent) {
	if (isDisposed ()) return;
	for (int i = 0; i < items.length; i++) {
		items [i].dispose (notifyParent);
	}
	if (notifyParent) parent.destroyItem (this);
	super.dispose ();	/* super is intentional here */
	background = foreground = null;
	cellBackgrounds = cellForegrounds = null;
	font = null;
	cellFonts = null;
	images = null;
	texts = displayTexts = null;
	textWidths = fontHeights = null;
	parent = null;
	parentItem = null;
	items = null;
}
/*
 * Ensure that all ancestors of the receiver are expanded
 */
void expandAncestors () {
	if (parentItem != null) parentItem.expandAncestors ();
	setExpanded (true);
	Event newEvent = new Event ();
	newEvent.item = this;
	parent.inExpand = true;
	parent.sendEvent (SWT.Expand, newEvent);
	parent.inExpand = false;
	if (isDisposed ()) return;
	if (items.length == 0) {
		expanded = false;
	}
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (background != null) return background;
	return parent.getBackground ();
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
public Color getBackground (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getBackground ();
	if (cellBackgrounds == null || cellBackgrounds [columnIndex] == null) return getBackground ();
	return cellBackgrounds [columnIndex];
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
	checkWidget ();
	return getBounds (true);
}
Rectangle getBounds (boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!isAvailable ()) return new Rectangle (0, 0, 0, 0);
	TreeColumn[] orderedColumns = parent.getOrderedColumns ();
	int orderedCol0Index = orderedColumns.length == 0 ? 0 : orderedColumns [0].getIndex ();
	int x = getTextX (orderedCol0Index);
	int width = textWidths [orderedCol0Index] + 2 * MARGIN_TEXT;
	if (orderedColumns.length > 0) {
		TreeColumn column = orderedColumns [0];
		int right = column.getX () + column.width;
		if (x + width > right) {
			width = Math.max (0, right - x);
		}
	}
	return new Rectangle (x, parent.getItemY (this), width, parent.itemHeight);
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
public Rectangle getBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!isAvailable ()) return new Rectangle (0, 0, 0, 0);
	TreeColumn[] columns = parent.columns;
	int columnCount = columns.length;
	int validColumnCount = Math.max (1, columnCount);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) {
		return new Rectangle (0, 0, 0, 0);
	}
	/*
	 * If there are no columns then this is the bounds of the receiver's content.
	 */
	if (columnCount == 0) {
		return new Rectangle (
			getContentX (0),
			parent.getItemY (this),
			getContentWidth (0),
			parent.itemHeight - 1);
	}

	TreeColumn column = columns [columnIndex];
	if (column.getOrderIndex () == 0) {
		/* 
		 * For ordered column 0 this is bounds from the beginning of the content to the
		 * end of the column.
		 */
		int x = getContentX (columnIndex);
		int offset = x - column.getX ();
		int width = Math.max (0, column.width - offset - 1);		/* max is for columns with small widths */
		return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
	}
	/*
	 * For ordered columns > 0 this is the bounds of the tree cell.
	 */
	return new Rectangle (column.getX (), parent.getItemY (this) + 1, column.width, parent.itemHeight - 1);
}
/*
 * Returns the full bounds of a cell in a tree, regardless of its content.
 */
Rectangle getCellBounds (int columnIndex) {
	int y = parent.getItemY (this);
	if (parent.columns.length == 0) {
		int width;
		if (customWidth != -1) {
			width = getContentX (0) + customWidth + parent.horizontalOffset;
		} else {
			int textPaintWidth = textWidths [0] + 2 * MARGIN_TEXT;
			width = getTextX (0) + textPaintWidth + parent.horizontalOffset;
		}
		return new Rectangle (-parent.horizontalOffset, y, width, parent.itemHeight);
	}
	TreeColumn column = parent.columns [columnIndex];
	return new Rectangle (column.getX (), y, column.width, parent.itemHeight);
}
/*
 * Returns the bounds of the receiver's checkbox, or null if the parent's style does not
 * include SWT.CHECK.
 */
Rectangle getCheckboxBounds () {
	if ((parent.getStyle () & SWT.CHECK) == 0) return null;
	int itemHeight = parent.itemHeight;
	Rectangle result = parent.checkboxBounds;
	Point[] hLinePoints = getHconnectorEndpoints ();
	result.x = hLinePoints [1].x;
	result.y = parent.getItemY (this) + (itemHeight - result.height) / 2;
	return result;
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return checked;
}
int getContentWidth (int columnIndex) {
	int width = textWidths [columnIndex] + 2 * MARGIN_TEXT;
	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	if (orderedIndex == 0) {
		width += parent.orderedCol0imageWidth;
		if (parent.orderedCol0imageWidth > 0) width += Tree.MARGIN_IMAGE;
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			width += image.getBounds ().width + Tree.MARGIN_IMAGE;
		}
	}
	return width;
}
/*
 * Returns the x value where the receiver's content (ie.- its image or text) begins
 * for the specified column.  For ordered columns > 0 this is dependent upon column
 * alignment, and for ordered column 0 this is dependent upon the receiver's depth in
 * the tree item hierarchy and the presence/absence of a checkbox.
 */
int getContentX (int columnIndex) {
	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	if (orderedIndex > 0) {
		TreeColumn column = parent.columns [columnIndex];
		int contentX = column.getX () + parent.getCellPadding ();
		if ((column.style & SWT.LEFT) != 0) return contentX;

		/* column is not left-aligned */
		int contentWidth = getContentWidth (columnIndex);
		if ((column.style & SWT.RIGHT) != 0) {
			int padding = parent.getCellPadding ();
			contentX = Math.max (contentX, column.getX () + column.width - padding - contentWidth);	
		} else {	/* SWT.CENTER */
			contentX = Math.max (contentX, column.getX () + (column.width - contentWidth) / 2);
		}
		return contentX;
	}

	/* ordered column 0 (always left-aligned) */
	if ((parent.style & SWT.CHECK) != 0) {
		Rectangle checkBounds = getCheckboxBounds ();
		return checkBounds.x + checkBounds.width + Tree.MARGIN_IMAGE;
	}

	int contentX = parent.getCellPadding () - parent.horizontalOffset;
	if (parentItem != null) {
		int expanderWidth = parent.expanderBounds.width + INDENT_HIERARCHY;
		contentX += expanderWidth * depth;
	}
	contentX += parent.expanderBounds.width;
	return contentX + Tree.MARGIN_IMAGE + INDENT_HIERARCHY;
}
String getDisplayText (int columnIndex) {
	if (parent.columns.length == 0) return getText (0, false);
	String result = displayTexts [columnIndex];
	return result != null ? result : "";	//$NON-NLS-1$
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return expanded;
}
/*
 * Returns the bounds of the receiver's expander box, regardless of whether the
 * receiver currently has children or not.
 */ 
Rectangle getExpanderBounds () {
	int itemHeight = parent.itemHeight;
	int x = parent.getCellPadding () - parent.horizontalOffset;
	int y = parent.getItemY (this);
	if (parentItem != null) {
		int expanderWidth = parent.expanderBounds.width + INDENT_HIERARCHY;
		x += expanderWidth * depth;
	}
	return new Rectangle (
		x, y + (itemHeight - parent.expanderBounds.height) / 2,
		parent.expanderBounds.width, parent.expanderBounds.height);
}
/*
 * Returns the bounds that should be used for drawing a focus rectangle on the receiver
 */
Rectangle getFocusBounds () {
	TreeColumn[] columns = parent.columns;
	int orderedCol0index = columns.length == 0 ? 0 : parent.getOrderedColumns ()[0].getIndex ();
	int x = getTextX (orderedCol0index);

	int width;
	if (columns.length > 0) {
		/* ensure that the focus x does not start beyond the right bound of ordered column 0 */
		int rightX = columns [orderedCol0index].getX () + columns [orderedCol0index].width;
		x = Math.min (x, rightX - 1);
		
		TreeColumn column;
		if ((parent.style & SWT.FULL_SELECTION) != 0) {
			int[] columnOrder = parent.getColumnOrder ();
			column = columns [columnOrder [columnOrder.length - 1]];	/* last ordered column */
		} else {
			column = columns [orderedCol0index];
		}
		width = column.getX () + column.width - x - 1;
	} else {	/* no columns */
		if (customWidth != -1) {
			width = customWidth;
		} else {
			width = textWidths [0] + 2 * MARGIN_TEXT;
		}
	}

	return new Rectangle (
		x,
		parent.getItemY (this) + (parent.linesVisible ? 1 : 0),
		width,
		parent.itemHeight - (parent.linesVisible ? 1 : 0));
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
	return getFont (true);
}
Font getFont (boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (font != null) return font;
	return parent.getFont ();
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
public Font getFont (int columnIndex) {
	checkWidget ();
	return getFont (columnIndex, true);
}
Font getFont (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getFont (checkData);
	if (cellFonts == null || cellFonts [columnIndex] == null) return getFont (checkData);
	return cellFonts [columnIndex];
}
int getFontHeight () {
	if (fontHeight != 0) return fontHeight;
	return parent.fontHeight;
}
int getFontHeight (int columnIndex) {
	if (fontHeights == null || fontHeights [columnIndex] == 0) return getFontHeight ();
	return fontHeights [columnIndex];
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (foreground != null) return foreground;
	return parent.getForeground ();
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
public Color getForeground (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getForeground ();
	if (cellForegrounds == null || cellForegrounds [columnIndex] == null) return getForeground ();
	return cellForegrounds [columnIndex];
}
/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return grayed;
}
/*
 * Answers the start and end points of the horizontal connector line that is
 * drawn between an item's expander box and its checkbox or content.
 */
Point[] getHconnectorEndpoints () {
	Rectangle expanderBounds = getExpanderBounds ();
	int x, width;
	if (items.length == 0) {	/* no child items, so no expander box */
		x = expanderBounds.x + Compatibility.ceil (expanderBounds.width, 2);
		width = Compatibility.floor (expanderBounds.width, 2) + INDENT_HIERARCHY;
	} else {					/* has child items */
		x = expanderBounds.x + expanderBounds.width;
		width = INDENT_HIERARCHY;
	}
	int y = expanderBounds.y + expanderBounds.height / 2;
	return new Point[] {
		new Point (x, y),
		new Point (x + width, y)
	};
}
/*
 * Returns the bounds representing the clickable region that should select the receiver.
 */
Rectangle getHitBounds () {
	int[] columnOrder = parent.getColumnOrder ();
	int orderedCol0index = columnOrder.length == 0 ? 0 : parent.columns [columnOrder [0]].getIndex ();
	int contentX = getContentX (orderedCol0index);
	int width = 0;
	TreeColumn[] columns = parent.columns;
	if (columns.length == 0) {
		width = getContentWidth (0); 
	} else {
		/* 
		 * If there are columns then this spans from the beginning of the receiver's column 0
		 * image or text to the end of either column 0 or the last column (FULL_SELECTION).
		 */
		TreeColumn column;
		if ((parent.style & SWT.FULL_SELECTION) != 0) {
			column = columns [columnOrder [columnOrder.length - 1]];	/* last column */
		} else {
			column = columns [orderedCol0index];
		}
		width = column.getX () + column.width - contentX;
	}
	return new Rectangle (contentX, parent.getItemY (this), width, parent.itemHeight);
}
public Image getImage () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
public Image getImage (int columnIndex) {
	checkWidget ();
	return getImage (columnIndex, true);
}
Image getImage (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return null;
	if (columnIndex == 0) return super.getImage ();	/* super is intentional here */
	return images [columnIndex];
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
public Rectangle getImageBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return new Rectangle (0,0,0,0);

	int padding = parent.getCellPadding ();
	int startX = getContentX (columnIndex);
	int itemHeight = parent.itemHeight;
	int imageSpaceY = itemHeight - 2 * padding;
	int y = parent.getItemY (this);
	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	Image image = getImage (columnIndex, false); 
	int drawWidth = 0;
	if (orderedIndex == 0) {
		/* for ordered column 0 all images have the same width */
		drawWidth = parent.orderedCol0imageWidth;
	} else {
		if (image != null) drawWidth = image.getBounds ().width;
	}
	return new Rectangle (startX, y + padding, drawWidth, imageSpaceY);
}
int getIndex () {
	TreeItem[] items;
	if (parentItem != null) {
		items = parentItem.items;
	} else {
		items = parent.items;
	}
	for (int i = 0; i < items.length; i++) {
		if (items [i] == this) return i;
	}
	return -1;
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index >= items.length) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return items.length;
}
String getNameText () {
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (!cached) return "*virtual*"; //$NON-NLS-1$
	}
	return super.getNameText ();
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	TreeItem[] result = new TreeItem [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;
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
/*
 * Returns the receiver's ideal width for the specified columnIndex.
 */
int getPreferredWidth (int columnIndex) {
	int width = 0;
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	width += gc.stringExtent (getText (columnIndex, false)).x + 2 * MARGIN_TEXT;
	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	if (orderedIndex == 0) {
		if (parent.orderedCol0imageWidth > 0) {
			width += parent.orderedCol0imageWidth;
			width += Tree.MARGIN_IMAGE;
		}
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			width += image.getBounds ().width;
			width += Tree.MARGIN_IMAGE;
		}
	}

	if (parent.hooks (SWT.MeasureItem)) {
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.x = getContentX (columnIndex);
		event.y = parent.getItemY (this);
		event.width = width;
		event.height = parent.itemHeight;
		parent.sendEvent (SWT.MeasureItem, event);
		if (parent.itemHeight != event.height) {
			parent.customHeightSet = true;
			boolean update = parent.setItemHeight (event.height + 2 * parent.getCellPadding ());
			if (update) parent.redraw ();
		}
		width = event.width;
	}
	gc.dispose ();

	if (orderedIndex == 0) {
		return getContentX (columnIndex) + parent.horizontalOffset + width + parent.getCellPadding ();	/* right side cell pad */
	}

	return width + 2 * parent.getCellPadding ();
}
public String getText () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
public String getText (int columnIndex) {
	checkWidget ();
	return getText (columnIndex, true);
}
String getText (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return "";	//$NON-NLS-1$
	if (columnIndex == 0) return super.getText ();	/* super is intentional here */
	if (texts [columnIndex] == null) return "";	//$NON-NLS-1$
	return texts [columnIndex];
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
public Rectangle getTextBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!isAvailable ()) return new Rectangle (0, 0, 0, 0);
	TreeColumn[] columns = parent.columns;
	int columnCount = columns.length;
	int validColumnCount = Math.max (1, columnCount);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) {
		return new Rectangle (0, 0, 0, 0);
	}
	/*
	 * If there are no columns then this is the bounds of the receiver's content.
	 */
	if (columnCount == 0) {
		int x = getTextX (0) + MARGIN_TEXT;
		int width = Math.max (0, getContentX(0) + getContentWidth (0) - x);
		return new Rectangle (
			x,
			parent.getItemY (this),
			width,
			parent.itemHeight - 1);
	}

	TreeColumn column = columns [columnIndex];
	if (column.getOrderIndex () == 0) {
		/* 
		 * For ordered column 0 this is bounds from the beginning of the content to the
		 * end of the column.
		 */
		int x = getTextX (columnIndex) + MARGIN_TEXT;
		int offset = x - column.getX ();
		int width = Math.max (0, column.width - offset - 1);		/* max is for columns with small widths */
		return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
	}
	/*
	 * For ordered columns > 0 this is the bounds of the tree cell.
	 */
	int x = getTextX (columnIndex) + MARGIN_TEXT;
	int offset = x - column.getX ();
	int width = Math.max (0, column.width - offset - MARGIN_TEXT);
	return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
}
/*
 * Returns the x value where the receiver's text begins.
 */
int getTextX (int columnIndex) {
	int orderedIndex = parent.columns.length == 0 ? 0 : parent.columns [columnIndex].getOrderIndex ();
	int textX = getContentX (columnIndex);
	if (orderedIndex == 0) {
		textX += parent.orderedCol0imageWidth;
		if (parent.orderedCol0imageWidth > 0) textX += Tree.MARGIN_IMAGE;
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			textX += image.getBounds ().width + Tree.MARGIN_IMAGE;	
		}
	}
	return textX;
}
/*
 * Returns true if the receiver descends from (or is identical to) the item.
 */
boolean hasAncestor (TreeItem item) {
	if (this == item) return true;
	if (parentItem == null) return false;
	return parentItem.hasAncestor (item);
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
	return item.getIndex ();
}
/*
 * Returns true if the receiver is currently available (though not necessary in the viewport).
 */
boolean isAvailable () {
	if (parentItem == null) return true; 	/* root items are always available */
	if (!parentItem.expanded) return false;
	return parentItem.isAvailable ();
}
/*
 * Answers a boolean indicating whether the receiver's y is within the current
 * viewport of the parent.
 */
boolean isInViewport () {
	if (availableIndex == -1) return false;
	int topIndex = parent.topIndex;
	if (availableIndex < topIndex) return false;
	int visibleCount = parent.clientArea.height / parent.itemHeight;
	return availableIndex <= topIndex + visibleCount;
}
/*
 * Returns true if the receiver is the last child of its parent item, or of its parent
 * if the receiver is a root item, and false otherwise.
 */
boolean isLastChild () {
	if (parentItem != null) {
		return getIndex () == parentItem.items.length - 1;
	}
	return getIndex () == parent.items.length - 1;
}
boolean isSelected () {
	return parent.getSelectionIndex (this) != -1;
}
/*
 * The backgroundOnly argument indicates whether the item should only
 * worry about painting its background color and selection.
 *
 * Returns a boolean indicating whether to abort drawing focus on the item.
 * If the receiver is not the current focus item then this value is irrelevant.
 */
boolean paint (GC gc, TreeColumn column, boolean backgroundOnly) {
	if (!parent.checkData (this, true)) return false;
	int columnIndex = 0, orderedIndex = 0, x = 0;
	if (column != null) {
		columnIndex = column.getIndex ();
		orderedIndex = column.getOrderIndex ();
		x = column.getX ();
	}

	/* 
	 * Capture GC attributes that will need to be restored later in the paint
	 * process to ensure that the item paints as intended without being affected
	 * by GC changes made in MeasureItem/EraseItem/PaintItem callbacks.
	 */
	int oldAlpha = gc.getAlpha ();
	boolean oldAdvanced = gc.getAdvanced ();
	int oldAntialias = gc.getAntialias ();
	Pattern oldBackgroundPattern = gc.getBackgroundPattern ();
	Pattern oldForegroundPattern = gc.getForegroundPattern ();
	int oldInterpolation = gc.getInterpolation ();
	int[] oldLineDash = gc.getLineDash ();
	int oldLineWidth = gc.getLineWidth ();
	int oldTextAntialias = gc.getTextAntialias ();

	if (parent.hooks (SWT.MeasureItem)) {
		int contentWidth = getContentWidth (columnIndex);
		int contentX = getContentX (columnIndex);
		gc.setFont (getFont (columnIndex, false));
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.x = contentX;
		event.y = parent.getItemY (this);
		event.width = contentWidth;
		event.height = parent.itemHeight;
		parent.sendEvent (SWT.MeasureItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setLineDash (oldLineDash);
		gc.setLineWidth (oldLineWidth);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		if (isDisposed ()) return false;
		if (parent.itemHeight != event.height) {
			parent.customHeightSet = true;
			boolean update = parent.setItemHeight (event.height + 2 * parent.getCellPadding ());
			if (update) parent.redraw ();
		}
		if (parent.columns.length == 0) {
			int change = event.width - (customWidth != -1 ? customWidth : contentWidth);
			if (event.width != contentWidth || customWidth != -1) customWidth = event.width;
			if (change != 0) {	/* scrollbar may be affected since no columns */
				parent.updateHorizontalBar (contentX + event.width, change);
				// TODO what if clip is too small now?
			}
		}
	}

	/* if this cell is completely to the right of the client area then there's no need to paint it */
	Rectangle clientArea = parent.clientArea;
	if (clientArea.x + clientArea.width < x) return false;

	Rectangle cellBounds = getCellBounds (columnIndex);
	if (parent.linesVisible) {
		cellBounds.y++;
		cellBounds.height--;
	}
	int cellRightX = 0;
	if (column != null) {
		cellRightX = column.getX () + column.width;
	} else {
		cellRightX = cellBounds.x + cellBounds.width;
	}

	/* restrict the clipping region to the cell */
	gc.setClipping (x, cellBounds.y, clientArea.width - x, cellBounds.height);
	
	int y = parent.getItemY (this);
	int itemHeight = parent.itemHeight;

	/* draw the parent background color/image of this cell */
	if (column == null) {
		parent.drawBackground (gc, 0, y, clientArea.width, itemHeight, 0, 0);
	} else {
		int fillWidth = cellBounds.width;
		if (parent.linesVisible) fillWidth--;
		parent.drawBackground (gc, cellBounds.x, cellBounds.y, fillWidth, cellBounds.height, 0, 0);
	}

	boolean isSelected = isSelected ();
	boolean isFocusItem = parent.focusItem == this && parent.isFocusControl ();
	boolean drawBackground = true;
	boolean drawForeground = true;
	boolean drawSelection = isSelected;
	boolean drawFocus = isFocusItem;
	if (parent.hooks (SWT.EraseItem)) {
		drawBackground = background != null || (cellBackgrounds != null && cellBackgrounds [columnIndex] != null);
		gc.setFont (getFont (columnIndex, false));
		if (isSelected && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		} else {
			gc.setForeground (getForeground (columnIndex));
			gc.setBackground (getBackground (columnIndex));
		}
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.doit = true;
		event.detail = SWT.FOREGROUND;
		if (drawBackground) event.detail |= SWT.BACKGROUND;
		if (isSelected) event.detail |= SWT.SELECTED;
		if (isFocusItem) event.detail |= SWT.FOCUSED;
		event.x = cellBounds.x;
		event.y = cellBounds.y;
		event.width = cellBounds.width;
		event.height = cellBounds.height;
		gc.setClipping (cellBounds);
		parent.sendEvent (SWT.EraseItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setClipping (cellBounds);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setLineDash (oldLineDash);
		gc.setLineWidth (oldLineWidth);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		if (isDisposed ()) return false;
		if (!event.doit) {
			drawBackground = drawForeground = drawSelection = drawFocus = false;
		} else {
			drawBackground = drawBackground && (event.detail & SWT.BACKGROUND) != 0;
			drawForeground = (event.detail & SWT.FOREGROUND) != 0;
			drawSelection = isSelected && (event.detail & SWT.SELECTED) != 0;
			drawFocus = isFocusItem && (event.detail & SWT.FOCUSED) != 0;
		}
	}

	/* draw the cell's set background if appropriate */
	if (drawBackground) {
		gc.setBackground (getBackground (columnIndex));
		if (columnIndex == 0 && (column == null || column.getOrderIndex () == 0)) {
			Rectangle focusBounds = getFocusBounds ();
			int fillWidth = 0;
			if (column == null) {
				fillWidth = focusBounds.width;
			} else {
				fillWidth = column.width - focusBounds.x;
				if (parent.linesVisible) fillWidth--;
			}
			gc.fillRectangle (focusBounds.x, focusBounds.y, fillWidth, focusBounds.height);
		} else {
			int fillWidth = cellBounds.width;
			gc.fillRectangle (cellBounds.x, cellBounds.y, fillWidth, cellBounds.height);
		}
	}

	/* draw the selection bar if the receiver is selected */
	if (drawSelection && isSelected && (orderedIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
		gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		if (orderedIndex == 0) {
			Rectangle focusBounds = getFocusBounds ();
			int fillWidth = focusBounds.width;
			if (parent.columns.length < 2 || (parent.style & SWT.FULL_SELECTION) == 0) {
				fillWidth -= 2;	/* space for right bound of focus rect */
			}
			if (fillWidth > 0) {
				gc.fillRectangle (focusBounds.x + 1, focusBounds.y + 1, fillWidth, focusBounds.height - 2);
			}
		} else {
			int fillWidth = column.width;
			int[] columnOrder = parent.getColumnOrder ();
			if (columnIndex == columnOrder [columnOrder.length - 1]) {
				fillWidth -= 2;		/* space for right bound of focus rect */
			}
			if (fillWidth > 0) {
				gc.fillRectangle (
					column.getX (),
					cellBounds.y + 1,
					fillWidth,
					cellBounds.height - 2);
			}
		}
	}
		
	if (backgroundOnly) return false;

	/* Draw column 0 decorations */
	if (orderedIndex == 0) {
		gc.setClipping (cellBounds);

		/* Draw hierarchy connector lines */
		Rectangle expanderBounds = getExpanderBounds ();
		Color oldForeground = gc.getForeground ();
		gc.setForeground (parent.getConnectorColor ());

		/* Draw vertical line above expander */
		int lineX = expanderBounds.x + expanderBounds.width / 2;
		int y2 = expanderBounds.y;
		if (items.length == 0) {
			y2 += expanderBounds.height / 2;
		}
		/* Do not draw this line iff this is the very first item in the tree */ 
		if (parentItem != null || getIndex () != 0) {
			gc.drawLine (lineX, y, lineX, y2);
		}

		/* Draw vertical line below expander if the receiver has lower siblings */
		if (!isLastChild ()) {
			if (items.length != 0) y2 += expanderBounds.height;
			gc.drawLine (lineX, y2, lineX, y + itemHeight);
		}

		/* Draw horizontal line to right of expander */
		Point[] endpoints = getHconnectorEndpoints ();
		gc.drawLine (endpoints [0].x, endpoints [0].y, endpoints [1].x - Tree.MARGIN_IMAGE, endpoints [1].y);
		
		/* 
		 * Draw hierarchy lines that are needed by other items that are shown below
		 * this item but whose parents are shown above (ie.- lines to the left of
		 * this item's connector line).
		 */
		TreeItem item = parentItem;
		while (item != null) {
			if (!item.isLastChild ()) {
				Rectangle itemExpanderBounds = item.getExpanderBounds ();
				lineX = itemExpanderBounds.x + itemExpanderBounds.width / 2;
				gc.drawLine (lineX, y, lineX, y + itemHeight);
			}
			item = item.parentItem;
		}

		gc.setForeground (oldForeground);

		/* Draw expand/collapse image if receiver has children */
		if (items.length > 0) {
			Image image = expanded ? parent.getExpandedImage () : parent.getCollapsedImage ();
			gc.drawImage (image, expanderBounds.x, expanderBounds.y);
		}
		
		/* Draw checkbox if parent Tree has style SWT.CHECK */
		if ((parent.style & SWT.CHECK) != 0) {
			Image baseImage = grayed ? parent.getGrayUncheckedImage () : parent.getUncheckedImage ();
			Rectangle checkboxBounds = getCheckboxBounds ();
			gc.drawImage (baseImage, checkboxBounds.x, checkboxBounds.y);
			/* Draw checkmark if item is checked */
			if (checked) {
				Image checkmarkImage = parent.getCheckmarkImage ();
				Rectangle checkmarkBounds = checkmarkImage.getBounds ();
				int xInset = (checkboxBounds.width - checkmarkBounds.width) / 2;
				int yInset = (checkboxBounds.height - checkmarkBounds.height) / 2;
				gc.drawImage (checkmarkImage, checkboxBounds.x + xInset, checkboxBounds.y + yInset);
			}
		}
	}

	if (drawForeground) {
		Image image = getImage (columnIndex, false);
		String text = getDisplayText (columnIndex);
		Rectangle imageArea = getImageBounds (columnIndex);
		int startX = imageArea.x;
		
		/* while painting the cell's content restrict the clipping region */
		int padding = parent.getCellPadding ();
		gc.setClipping (
			startX,
			cellBounds.y + padding - (parent.linesVisible ? 1 : 0),
			cellRightX - startX - padding,
			cellBounds.height - 2 * (padding - (parent.linesVisible ? 1 : 0)));
	
		/* draw the image */
		if (image != null) {
			Rectangle imageBounds = image.getBounds ();
			gc.drawImage (
				image,
				0, 0,									/* source x, y */
				imageBounds.width, imageBounds.height,	/* source width, height */
				imageArea.x, imageArea.y,				/* dest x, y */
				imageArea.width, imageArea.height);		/* dest width, height */
		}
		
		/* draw the text */
		if (text.length () > 0) {
			gc.setFont (getFont (columnIndex, false));
			int fontHeight = getFontHeight (columnIndex);
			if (drawSelection && (orderedIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
				gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			} else {
				if (!isSelected || drawSelection) {
					gc.setForeground (getForeground (columnIndex));
				}
			}
			x = getTextX (columnIndex) + MARGIN_TEXT;
			gc.drawString (text, x, y + (itemHeight - fontHeight) / 2, true);
		}
	}
	
	if (parent.hooks (SWT.PaintItem)) {
		int contentWidth = getContentWidth (columnIndex);
		int contentX = getContentX (columnIndex);
		gc.setFont (getFont (columnIndex, false));
		if (isSelected && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		} else {
			gc.setForeground (getForeground (columnIndex));
			gc.setBackground (getBackground (columnIndex));
		}
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		if (isSelected) event.detail |= SWT.SELECTED;
		if (drawFocus) event.detail |= SWT.FOCUSED;
		event.x = contentX;
		event.y = cellBounds.y;
		event.width = contentWidth;
		event.height = cellBounds.height;
		gc.setClipping (cellBounds);
		parent.sendEvent (SWT.PaintItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setClipping (cellBounds);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setLineDash (oldLineDash);
		gc.setLineWidth (oldLineWidth);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		drawFocus = isFocusItem && (event.detail & SWT.FOCUSED) != 0;
	}

	return isFocusItem && !drawFocus;
}
/*
 * Redraw part of the receiver.  If either EraseItem or PaintItem is hooked then
 * only full cells should be damaged, so adjust accordingly.  If neither of these
 * events are hooked then the exact bounds given for damaging can be used.
 */
void redraw (int x, int y, int width, int height, int columnIndex) {
	if (!parent.hooks (SWT.EraseItem) && !parent.hooks (SWT.PaintItem)) {
		parent.redraw (x, y, width, height, false);
		return;
	}
	Rectangle cellBounds = getCellBounds (columnIndex);
	parent.redraw (cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height, false);
}
void redrawItem () {
	if (!isAvailable ()) return;
	parent.redraw (0, parent.getItemY (this), parent.clientArea.width, parent.itemHeight, false);
}
/*
 * Updates internal structures in the receiver and its child items to handle the removal of a column.
 */
void removeColumn (TreeColumn column, int index, int orderedIndex) {
	int columnCount = parent.columns.length;

	if (columnCount == 0) {
		/* reverts to normal tree when last column disposed */
		cellBackgrounds = cellForegrounds = null;
		displayTexts = null;
		cellFonts = null;
		fontHeights = null;
		GC gc = new GC (parent);
		computeTextWidths (gc);
		gc.dispose ();
		/* notify all child items as well */
		for (int i = 0; i < items.length; i++) {
			items [i].removeColumn (column, index, orderedIndex);
		}
		return;
	}

	String[] newTexts = new String [columnCount];
	System.arraycopy (texts, 0, newTexts, 0, index);
	System.arraycopy (texts, index + 1, newTexts, index, columnCount - index);
	texts = newTexts;
	
	Image[] newImages = new Image [columnCount];
	System.arraycopy (images, 0, newImages, 0, index);
	System.arraycopy (images, index + 1, newImages, index, columnCount - index);
	images = newImages;

	int[] newTextWidths = new int [columnCount];
	System.arraycopy (textWidths, 0, newTextWidths, 0, index);
	System.arraycopy (textWidths, index + 1, newTextWidths, index, columnCount - index);
	textWidths = newTextWidths;

	String[] newDisplayTexts = new String [columnCount];
	System.arraycopy (displayTexts, 0, newDisplayTexts, 0, index);
	System.arraycopy (displayTexts, index + 1, newDisplayTexts, index, columnCount - index);
	displayTexts = newDisplayTexts;

	if (cellBackgrounds != null) {
		Color[] newCellBackgrounds = new Color [columnCount];
		System.arraycopy (cellBackgrounds, 0, newCellBackgrounds, 0, index);
		System.arraycopy (cellBackgrounds, index + 1, newCellBackgrounds, index, columnCount - index);
		cellBackgrounds = newCellBackgrounds;
	}
	if (cellForegrounds != null) {
		Color[] newCellForegrounds = new Color [columnCount];
		System.arraycopy (cellForegrounds, 0, newCellForegrounds, 0, index);
		System.arraycopy (cellForegrounds, index + 1, newCellForegrounds, index, columnCount - index);
		cellForegrounds = newCellForegrounds;
	}
	if (cellFonts != null) {
		Font[] newCellFonts = new Font [columnCount];
		System.arraycopy (cellFonts, 0, newCellFonts, 0, index);
		System.arraycopy (cellFonts, index + 1, newCellFonts, index, columnCount - index);
		cellFonts = newCellFonts;

		int[] newFontHeights = new int [columnCount];
		System.arraycopy (fontHeights, 0, newFontHeights, 0, index);
		System.arraycopy (fontHeights, index + 1, newFontHeights, index, columnCount - index);
		fontHeights = newFontHeights;
	}

	if (index == 0) {
		text = texts [0] != null ? texts [0] : "";	//$NON-NLS-1$
		texts [0] = null;
		image = images [0];
		images [0] = null;
	}

	if (orderedIndex == 0) {
		/* 
		 * The new first ordered column will not have as much width available to it as it did when
		 * it was the second ordered column since it now has to show hierarchy decorations as well,
		 * so recompute its displayText. 
		 */
		int firstColumnIndex = parent.getOrderedColumns () [0].getIndex ();
		GC gc = new GC (parent);
		gc.setFont (getFont (firstColumnIndex, false));
		computeDisplayText (firstColumnIndex, gc);
		gc.dispose ();
	}
	if (columnCount < 2) {
		texts = null;
		images = null;
	}

	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items [i].removeColumn (column, index, orderedIndex);
	}
}
/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void removeAll () {
	checkWidget ();
	if (items.length == 0) return;

	int lastAvailableIndex = parent.availableItemsCount - 1;
	/* for performance do this upfront for whole descendent chain */
	TreeItem focusItem = parent.focusItem; 
	if (focusItem != null && focusItem.hasAncestor (this)) {
		parent.setFocusItem (this, false);
	}
	while (items.length > 0) {
		items [0].dispose (true);
		removeItem (items [0], 0);
	}
	items = Tree.NO_ITEMS;
	expanded = false;
	if (isAvailable ()) {
		parent.redrawItems (availableIndex, lastAvailableIndex, false);
	}
}
/*
 * Removes a child item from the receiver.
 */
void removeItem (TreeItem item, int index) {
	if (isDisposed ()) return;
	TreeItem[] newItems = new TreeItem [items.length - 1];
	System.arraycopy (items, 0, newItems, 0, index);
	System.arraycopy (items, index + 1, newItems, index, newItems.length - index);
	items = newItems;
	if (items.length == 0) {
		items = Tree.NO_ITEMS;
		/* condition below handles creation of item within Expand callback */
		if (!parent.inExpand) {
			expanded = false;
			if (isInViewport ()) {
				Rectangle bounds = getExpanderBounds ();	/* expander box no longer needed */
				parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
			}
		}
		return;
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
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = background;
	if (oldColor == color) return;
	background = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redrawItem ();
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
public void setBackground (int columnIndex, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellBackgrounds == null) {
		if (color == null) return;
		cellBackgrounds = new Color [validColumnCount];
	}
	Color oldColor = cellBackgrounds [columnIndex];
	if (oldColor == color) return;
	cellBackgrounds [columnIndex] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (isInViewport ()) {
		Rectangle bounds = getCellBounds (columnIndex);
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
	}
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
public void setChecked (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (checked == value) return;
	checked = value;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (isInViewport ()) {
		if (parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
			redrawItem ();
		} else {
			Rectangle bounds = getCheckboxBounds ();
			parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
	}
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
public void setExpanded (boolean value) {
	checkWidget ();
	if (expanded == value) return;
	if (items.length == 0) return;
	if (parent.inExpand) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (value) {
		expanded = value;
		if (availableIndex == -1) return;

		TreeItem[] availableDescendents = computeAvailableDescendents ();
		int descendentsCount = availableDescendents.length;
		if (availableIndex != parent.availableItemsCount - 1) {
			/* the receiver is not the last available item */
			Rectangle clientArea = parent.clientArea;
			int y = parent.getItemY (this) + parent.itemHeight;
			if (0 < y && y < clientArea.height) {
				if (parent.drawCount <= 0) {
					parent.update ();
					GC gc = new GC (parent);
					gc.copyArea (
						0, y,
						clientArea.width, clientArea.height - y,
						0, y + ((descendentsCount - 1) * parent.itemHeight));				
					gc.dispose ();
				}
			}
		}

		parent.makeDescendentsAvailable (this, availableDescendents);

		/* update scrollbars */
		int rightX = 0;
		for (int i = 1; i < availableDescendents.length; i++) {
			Rectangle bounds = availableDescendents [i].getBounds (false);
			rightX = Math.max (rightX, bounds.x + bounds.width);
		}
		parent.updateHorizontalBar (rightX, rightX);
		parent.updateVerticalBar ();
		/* 
		 * If new item is above viewport then adjust topIndex and the vertical scrollbar
		 * so that the current viewport items will not change. 
		 */
		if (availableIndex < parent.topIndex) {
			parent.topIndex += descendentsCount - 1;
			ScrollBar vBar = parent.getVerticalBar ();
			if (vBar != null) vBar.setSelection (parent.topIndex);
			return;
		}

		int redrawStart = availableIndex + 1;
		int redrawEnd = redrawStart + descendentsCount - 2;
		parent.redrawItems (redrawStart, redrawEnd, false);
	} else {
		TreeItem[] descendents = computeAvailableDescendents ();
		expanded = value;
		if (availableIndex == -1) return;
		Rectangle clientArea = parent.clientArea;

		int y = parent.getItemY (this) + parent.itemHeight;
		int startY = y + (descendents.length - 1) * parent.itemHeight;
		if (y < clientArea.height && 0 < startY) {	/* determine whether any visual update is actually needed */
			if (parent.drawCount <= 0) {
				parent.update ();
				GC gc = new GC (parent);
				gc.copyArea (0, startY, clientArea.width, clientArea.height - startY, 0, y);
				gc.dispose ();
				int redrawY = y + Math.max (0, clientArea.height - startY);
				parent.redraw (0, redrawY, clientArea.width, clientArea.height - redrawY, false);
			}
		}

		parent.makeDescendentsUnavailable (this, descendents);

		/* 
		 * If all collapsed items are above the viewport then adjust topIndex and
		 * the vertical scrollbar so that the current viewport items will not change.
		 */
		int bottomIndex = availableIndex + descendents.length - 1;
		if (bottomIndex < parent.topIndex) {
			parent.topIndex = parent.topIndex - descendents.length + 1;
			ScrollBar vBar = parent.getVerticalBar ();
			if (vBar != null) vBar.setSelection (parent.topIndex);
		}
		
		parent.updateHorizontalBar ();
		parent.updateVerticalBar ();

		/* move focus (and selection if SWT.SINGLE) to item if a descendent had focus */
		TreeItem focusItem = parent.focusItem;
		if (focusItem != null && focusItem != this && focusItem.hasAncestor (this)) {
			parent.setFocusItem (this, false);
			if ((parent.style & SWT.SINGLE) != 0) {
				parent.selectItem (this, false);
			}
			/* Fire an event since the selection is being changed automatically */
			Event newEvent = new Event ();
			newEvent.item = this;
			parent.sendEvent (SWT.Selection, newEvent);
			if (isDisposed ()) return;
			parent.showItem (this);
			parent.redrawItem (availableIndex, true);
		}
	}
	/* redraw the receiver's expander box */
	if (isInViewport ()) {
		Rectangle bounds = getExpanderBounds ();
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
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
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont (false));
	fontHeight = gc.getFontMetrics ().getHeight ();
	computeDisplayTexts (gc);
	computeTextWidths (gc);
	gc.dispose ();
	
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
	redrawItem ();
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
public void setFont (int columnIndex, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellFonts == null) {
		if (font == null) return;
		cellFonts = new Font [validColumnCount];
	}
	Font oldFont = cellFonts [columnIndex];
	if (oldFont == font) return;
	cellFonts [columnIndex] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	if (fontHeights == null) fontHeights = new int [validColumnCount];
	fontHeights [columnIndex] = gc.getFontMetrics ().getHeight ();
	computeDisplayText (columnIndex, gc);
	gc.dispose ();

	if (isInViewport ()) {
		Rectangle bounds = getCellBounds (columnIndex);
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
	}
}
/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @since 2.0
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
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = foreground;
	if (oldColor == color) return;
	foreground = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redrawItem ();
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
public void setForeground (int columnIndex, Color value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellForegrounds == null) {
		if (value == null) return;
		cellForegrounds = new Color [validColumnCount];
	}
	Color oldColor = cellForegrounds [columnIndex];
	if (oldColor == value) return;
	cellForegrounds [columnIndex] = value;
	if (oldColor != null && oldColor.equals (value)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (isInViewport ()) {
		redraw (
			getTextX (columnIndex),
			parent.getItemY (this),
			textWidths [columnIndex] + 2 * MARGIN_TEXT,
			parent.itemHeight,
			columnIndex);
	}
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
public void setGrayed (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (grayed == value) return;
	grayed = value;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (isInViewport ()) {
		Rectangle bounds = getCheckboxBounds ();
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
	}
}
public void setImage (Image value) {
	checkWidget ();
	setImage (0, value);
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
public void setImage (Image[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setImage (i, value [i]);
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
public void setImage (int columnIndex, Image value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);

	TreeColumn[] columns = parent.columns;
	int validColumnCount = Math.max (1, columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	Image image = getImage (columnIndex, false);
	if (value == image) return;
	if (value != null && value.equals (image)) return;
	if (columnIndex == 0) {
		super.setImage (value);
	} else {
		images [columnIndex] = value;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* 
	 * An image width change may affect the space available for the item text, so
	 * recompute the displayText if there are columns.
	 */
	if (columns.length > 0) {
		GC gc = new GC (parent);
		gc.setFont (getFont (columnIndex, false));
		computeDisplayText (columnIndex, gc);
		gc.dispose ();
	}

	if (value == null) {
		redrawItem ();	// TODO why the whole item?
		return;
	}

	if (columns.length == 0) {
		if (parent.imageHeight == 0) {
			/* this is the first image being put into the parent Tree */
			Rectangle bounds = value.getBounds ();
			parent.orderedCol0imageWidth = bounds.width;
			parent.setImageHeight (bounds.height);
			parent.redrawItems (0, parent.availableItemsCount - 1, false);
		} else {
			redrawItem ();
		}
		return;
	}

	/* there are 1+ columns */
	TreeColumn column = columns [columnIndex];
	int orderedIndex = column.getOrderIndex ();
	Rectangle bounds = value.getBounds ();
	if (column.itemImageWidth == 0) column.itemImageWidth = bounds.width;

	if (parent.imageHeight == 0) {
		/* this is the first image being put into the parent Tree */
		int oldItemHeight = parent.itemHeight;
		parent.setImageHeight (bounds.height);

		if (orderedIndex == 0) {	/* the first ordered column */
			parent.orderedCol0imageWidth = bounds.width;
			/* 
			 * All column 0 cells will now have less room available for their texts,
			 * so all items must now recompute their column 0 displayTexts.
			 */
			TreeItem[] rootItems = parent.items;
			GC gc = new GC (parent);
			for (int i = 0; i < rootItems.length; i++) {
				rootItems [i].updateColumnWidth (column, gc);
			}
			gc.dispose ();
			if (oldItemHeight != parent.itemHeight) {
				/* the item height grew as a result of the new image height, so redraw everything */
				parent.redraw ();
			} else {
				/* redraw the column since all items should now have image space */
				parent.redraw (column.getX (), 0, column.width, parent.clientArea.height, false);
			}
		} else {	/* not the first ordered column */
			if (oldItemHeight != parent.itemHeight) {
				/* the item height grew as a result of the new image height, so redraw everything */
				parent.redraw ();
			} else {
				redrawItem ();
			}
		}
		return;
	}

	if (orderedIndex == 0 && parent.orderedCol0imageWidth == 0) {
		/* this is the first image being put into the current ordered column 0 */
		parent.orderedCol0imageWidth = bounds.width;
		/* 
		 * All column 0 cells will now have less room available for their texts,
		 * so all items must now recompute their column 0 displayTexts.
		 */
		TreeItem[] rootItems = parent.items;
		GC gc = new GC (parent);
		for (int i = 0; i < rootItems.length; i++) {
			rootItems [i].updateColumnWidth (column, gc);
		}
		gc.dispose ();
		parent.redraw (column.getX (), 0, column.width, parent.clientArea.height, false);
		return;
	}

	redrawItem ();	// TODO why the whole item?
}
/**
 * Sets the number of child items contained in the receiver.
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
	if (count == items.length) return;
	int redrawStart, redrawEnd;

	/* if the new item count is less than the current count then remove all excess items from the end */
	if (count < items.length) {
		redrawStart = count > 0 ? items [count - 1].availableIndex : availableIndex;
		redrawEnd = parent.availableItemsCount - 1;
		for (int i = count; i < items.length; i++) {
			items [i].dispose (true);
		}
		if (count == 0) {
			items = Tree.NO_ITEMS;
		} else {
			TreeItem[] newItems = new TreeItem [count];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}
		if (count == 0) expanded = false;
	} else {
		int oldAvailableDescendentCount = computeAvailableDescendentCount ();
		int grow = count - items.length;
		redrawStart = items.length == 0 ? availableIndex : items [items.length - 1].availableIndex;
		redrawEnd = expanded && isAvailable () ? parent.availableItemsCount + grow  - 1: redrawStart;
		TreeItem[] newItems = new TreeItem [count];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
		for (int i = items.length - grow; i < count; i++) {
			items [i] = new TreeItem (this, SWT.NONE, i, false);
		}
		
		if (expanded && availableIndex != -1) {
			/* expand the availableItems array if necessary */
			if (parent.availableItems.length < parent.availableItemsCount + grow) {
				TreeItem[] newAvailableItems = new TreeItem [parent.availableItemsCount + grow];
				System.arraycopy (parent.availableItems, 0, newAvailableItems, 0, parent.availableItemsCount);
				parent.availableItems = newAvailableItems;
			}
			TreeItem[] availableItems = parent.availableItems;
			/* shift items right to create space for the new available items */
			int dest = availableIndex + oldAvailableDescendentCount + grow;
			System.arraycopy (
				availableItems,
				availableIndex + oldAvailableDescendentCount,
				availableItems,
				dest,
				availableItems.length - dest);
			parent.availableItemsCount += grow;
			/* copy new items in */
			System.arraycopy (
				items,
				items.length - grow,
				availableItems,
				availableIndex + oldAvailableDescendentCount,
				grow);
			/* update availableIndex for all affected items */
			for (int i = availableIndex + oldAvailableDescendentCount; i < parent.availableItemsCount; i++) {
				availableItems [i].availableIndex = i;
			}
		}
	}

	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (availableIndex != -1) {
		if (expanded) parent.updateVerticalBar ();
		parent.redrawItems (redrawStart, redrawEnd, false);
	}
}
public void setText (String value) {
	checkWidget ();
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	setText (0, value);
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
}
/**
 * Sets the text for multiple columns in the tree. 
 * 
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
public void setText (String[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setText (i, value [i]);
	}
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
}
/**
 * Sets the receiver's text at a column
 *
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
public void setText (int columnIndex, String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (value.equals (getText (columnIndex, false))) return;
	if (columnIndex == 0) {
		super.setText (value);
	} else {
		texts [columnIndex] = value;		
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	int oldWidth = textWidths [columnIndex];
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	computeDisplayText (columnIndex, gc);
	gc.dispose ();
	if (availableIndex == -1) return;
	if (parent.columns.length == 0) {
		Rectangle bounds = getBounds (false);
		int rightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (rightX, textWidths [columnIndex] - oldWidth);
	}
	if (isInViewport ()) {
		redraw (
			getTextX (columnIndex),
			parent.getItemY (this),
			Math.max (oldWidth, textWidths [columnIndex]) + 2 * MARGIN_TEXT,
			parent.itemHeight,
			columnIndex);
	}
}
/*
 * Perform any internal changes necessary to reflect a changed column width.
 */
void updateColumnWidth (TreeColumn column, GC gc) {
	int columnIndex = column.getIndex ();
	gc.setFont (getFont (columnIndex, false));
	String oldDisplayText = displayTexts [columnIndex];
	computeDisplayText (columnIndex, gc);

	/* the cell must be damaged if there is custom drawing being done or if the alignment is not LEFT */
	if (isInViewport ()) {
		boolean columnIsLeft = (column.style & SWT.LEFT) != 0;
		if (!columnIsLeft || parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
			Rectangle cellBounds = getCellBounds (columnIndex);
			parent.redraw (cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height, false);
		} else {
			/* if the display text has changed then the cell text must be damaged in order to repaint */	
			if (oldDisplayText == null || !oldDisplayText.equals (displayTexts [columnIndex])) {
				Rectangle cellBounds = getCellBounds (columnIndex);
				int textX = getTextX (columnIndex);
				parent.redraw (textX, cellBounds.y, cellBounds.x + cellBounds.width - textX, cellBounds.height, false);
			}
		}
	}

	for (int i = 0; i < items.length; i++) {
		items [i].updateColumnWidth (column, gc);
	}
}

/*
 * The parent's font has changed, so if this font was being used by the receiver then
 * recompute its cached text sizes using the gc argument.  Pass this notification on to
 * all child items as well.
 */
void updateFont (GC gc) {
	if (font == null) {		/* receiver is using the Tree's font */
		computeDisplayTexts (gc);
		computeTextWidths (gc);
	}
	/* pass notification on to all children */
	for (int i = 0; i < items.length; i++) {
		items [i].updateFont (gc);
	}
}
}
