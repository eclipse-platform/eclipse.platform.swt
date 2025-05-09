/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class represent a selecTree user interface object that
 * represents an item in a Tree.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#Tree">Tree, TreeItem,
 *      TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TreeItem extends Item {

	/** border width */
	static final int DEFAULT_BORDER_WIDTH = 1;

	Tree parent;
	TreeItem parentItem;
	int indents = -1;
	private java.util.List<TreeItem> itemsList = new ArrayList<>();
	String[] strings;
	Image[] images;
	Font font;
	Font[] cellFont;
	boolean checked, grayed, cached;
	int imageIndent;
	Color background;
	Color foreground;
	Color[] cellBackground, cellForeground;
	private int topIndexAtCalculation = -1;

	// TODO: location and size should be separated strictly. Because a repositioning
	// should not make the size recalculate
	private Point location;
	private Rectangle fullBounds;

	// TODO implement alignment
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;

	// TODO implement accessibility
	private Accessible acc;
	private AccessibleAdapter accAdapter;
	// -1 is also a calculated index, so the not calculated default is -2
	private int itemIndex = -2;

	private final TreeItemRenderer renderer = new TreeItemRenderer(this);
	private boolean expanded;
	private int virtualItemCount;
	private final TreeMap<Integer, TreeItem> virtualItemsList = new TreeMap<>();

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code>) and a style value describing its behavior and appearance.
	 * The item is added to the end of the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(Tree parent, int style) {
		this(parent, style, checkNull(parent).getItemCount(), true);

		initialize();
		this._addListener(style, null);
	}

	private void initialize() {
		final Listener listener = event -> {
			switch (event.type) {
			case SWT.Dispose -> onDispose(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Paint -> onPaint(event);
			case SWT.Resize -> onResize();
			case SWT.FocusIn -> onFocusIn();
			case SWT.FocusOut -> onFocusOut();
			case SWT.Traverse -> onTraverse(event);
			case SWT.Selection -> onSelection(event);
			case SWT.KeyDown -> onKeyPressed(event);
			case SWT.KeyUp -> onKeyReleased(event);
			}
		};
		addListener(SWT.Dispose, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Selection, listener);

		addTypedListener(new MouseTrackAdapter() {
			private boolean hasMouseEntered;

			@Override
			public void mouseEnter(MouseEvent e) {
				if (!hasMouseEntered) {
					hasMouseEntered = true;
					redraw();
				}
			}

			@Override
			public void mouseExit(MouseEvent e) {
				hasMouseEntered = false;
				redraw();
			}
		}, SWT.MouseEnter, SWT.MouseExit);

		initializeAccessible();
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}

		doPaint(event.gc);
	}

	void doPaint(GC gc) {
		renderer.doPaint(gc);
	}

	private boolean isVisible() {
		return true;
	}

	void initializeAccessible() {
		acc = getAccessible();

		accAdapter = new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {

				e.result = "text";
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
			}
		};
		// TODO: handle accessibility
		// acc.addAccessibleListener(accAdapter);
		// addListener(SWT.FocusIn, event -> acc.setFocus(ACC.CHILDID_SELF));
	}

	private Accessible getAccessible() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	private void onSelection(Event event) {
		redraw();
	}

	private void onTraverse(Event event) {
	}

	private void onFocusIn() {
		redraw();
	}

	private void onFocusOut() {
		redraw();
	}

	private void onKeyPressed(Event event) {
	}

	private void onKeyReleased(Event event) {
	}

	private void onResize() {
		redraw();
	}

	private void onDispose(Event event) {
		this.dispose();
	}

	private void onMouseDown(Event e) {
		redraw();
	}

	private void handleSelection() {
		sendSelectionEvent(SWT.Selection);
	}

	private void onMouseUp(Event e) {
		if ((e.stateMask & SWT.BUTTON1) != 0) {
			handleSelection();
		} else {
			redraw();
		}
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Tree</code>), a style value describing its behavior and appearance, and
	 * the index at which to place it in the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 * @param index  the zero-relative index to store the receiver in its parent
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the parent (inclusive)</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TreeItem(Tree parent, int style, int index) {
		this(parent, style, index, true);
	}

	TreeItem(Tree parent, int style, int index, boolean create) {
		super(parent, style);

		if (index < 0 || index > parent.getItemCount()) error(SWT.ERROR_INVALID_RANGE);

		this.parent = parent;
		if (create) {
			parent.createItem(this, index);
		}
	}

	public TreeItem(TreeItem rootItem, int style, int index) {
		super(rootItem, style, index);
		parent = rootItem.getParent();
		parentItem = rootItem;

		if (index < 0 || index > parentItem.getItemCount()) error(SWT.ERROR_INVALID_RANGE);

		parentItem.createItem(this, index);
	}

	private void createItem(TreeItem item, int index) {
		if (isVirtual()) {
			TreeItem previous = virtualItemsList.get(index);
			int previousIndex = index;

			// move all other elements one up until an element was not yet set
			while (previous != null) {
				virtualItemsList.remove(previousIndex);

				var nextPrevious = virtualItemsList.get(previousIndex + 1);
				virtualItemsList.put(previousIndex + 1, previous);
				previous = nextPrevious;
				previousIndex = previousIndex + 1;
			}

			if (previousIndex >= virtualItemCount) {
				virtualItemCount = previousIndex + 1;
			}

			virtualItemsList.put(index, item);

			if (index >= virtualItemCount) {
				virtualItemCount = index + 1;
			}
		} else {
			if (index < itemsList.size()) {
				itemsList.add(index, item);
			} else {
				itemsList.add(item);
			}
		}

		parent.synchronizeArrangements(true);

		if (!isVirtual()) {
			parent.updateScrollBarWithTextSize();
		}
		if (index >= parent.getTopIndex() && index <= parent.getItemsHandler().getLastVisibleElementIndex()) {
			redraw();
		}
	}

	private void createItem(TreeItem treeItem) {
		createItem(treeItem, getItemCount());
	}

	public TreeItem(TreeItem rootItem, int style) {
		super(rootItem, style);
		parent = rootItem.getParent();
		parentItem = rootItem;

		parentItem.createItem(this);
	}

	static Tree checkNull(Tree control) {
		if (control == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return control;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) error(SWT.ERROR_INVALID_SUBCLASS);
	}

	void clear() {
		clearCache();
		text = "";
		image = null;
		strings = null;
		images = null;
		imageIndent = 0;
		checked = grayed = false;
		font = null;
		background = null;
		foreground = null;
		cellFont = null;
		cellBackground = cellForeground = null;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = false;
		}
	}

	@Override
	void destroyWidget() {
		parent.destroyItem(this);
		if (parentItem != null) {
			parentItem.destroyItem(this);
		}
		releaseHandle();
	}

	private void destroyItem(TreeItem treeItem) {
		itemsList.remove(treeItem);
	}

	/**
	 * Returns the receiver's background color.
	 *
	 * @return the background color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 2.0
	 */
	public Color getBackground() {
		checkWidget();

		if (background != null) {
			return background;
		}

		return getParent().getBackground();
	}

	/**
	 * Returns the background color at the given column index in the receiver.
	 *
	 * @param index the column index
	 * @return the background color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public Color getBackground(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getBackground();
		}
		Color cellBg = cellBackground != null ? cellBackground[index] : null;
		return cellBg == null ? getBackground() : cellBg;
	}

	/**
	 * Returns a rectangle describing the size and location of the receiver's text
	 * and image relative to its parent.
	 *
	 * This exclude the checkbox if the Tree has style SWT.CHECK.
	 *
	 * @return the bounding rectangle of the receiver's text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.2
	 */
	public Rectangle getBounds() {
		int itemIndex = getItemIndex();
		if (itemIndex == -1) {
			return new Rectangle(0, 0, 0, 0);
		}

		Rectangle full = getFullBounds();

		int width = full.width;
		int x = full.x;

		int shift = 0;

		// remove arrow and checkbox from rectangle
		Rectangle rightIgnoreElements = renderer.getCheckboxRectangle();

		if (rightIgnoreElements == null) {
			rightIgnoreElements = renderer.getArrowRectangle();
		}

		if (rightIgnoreElements != null) {
			shift = rightIgnoreElements.x + rightIgnoreElements.width - full.x;
		}

		if (getParent().columnsExist()) {
			width -= shift;
			width = Math.max(0, width);
			x += shift;
		} else {
			// here we also ignore the image
			Rectangle rec = getTextBounds(0);
			x += rec.x - 1;
			width = full.width - rec.x + 1;
		}

		return new Rectangle(x, full.y, width, full.height);
	}

	private int getItemIndex() {
		if (this.itemIndex == -2) {
			this.itemIndex = parent.arrangementIndexOf(this);
		}
		return this.itemIndex;
	}

	/**
	 * @return the full Rectangle including the checkbox and initial pixels.
	 */
	Rectangle getFullBounds() {
		// TODO improve cache and use a timestamp instead of calculation values.
		if (topIndexAtCalculation == getParent().getTopIndex() && fullBounds != null && Tree.USE_CACHES) {
			return fullBounds;
		}

		this.fullBounds = null;
		this.location = null;

		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1) {
			return new Rectangle(0, 0, 0, 0);
		}

		synchronized (getParent()) {
			calculateLocation();
			Point p = renderer.computeSize(false);
			fullBounds = new Rectangle(location.x, location.y, p.x, p.y);
		}

		return fullBounds;
	}

	private void calculateLocation() {
		int index = getItemIndex();

		final Tree tree = getParent();
		final int topIndex = tree.getTopIndex();
		if (topIndex == index) {
			this.location = tree.getTopIndexItemPosition();
		} else if (topIndex < index) {
			for (int i = topIndex; i < index; i = Math.min(i + 1000, index)) {
				TreeItem it = tree._getArrangementItem(i);
				it.getLocation();
			}

			TreeItem prevItem = tree._getArrangementItem(index - 1);
			Point prevBounds = prevItem.getLocation();
			int fullHeightDiff = TreeItemsHandler.getItemsHeight(prevItem);
			this.location = new Point(prevBounds.x, prevBounds.y + fullHeightDiff);
		} else {
			for (int i = topIndex; i > index; i = Math.max(i - 1000, index)) {
				TreeItem it = tree._getArrangementItem(i);
				it.getLocation();
			}

			TreeItem prevItem = tree._getArrangementItem(index + 1);
			Point prevBounds = prevItem.getLocation();
			int fullHeightDiff = TreeItemsHandler.getItemsHeight(this);
			this.location = new Point(prevBounds.x, prevBounds.y - fullHeightDiff);
		}
		topIndexAtCalculation = topIndex;
	}

	Point getSize() {
		return renderer.computeSize(false);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative to
	 * its parent at a column in the Tree.
	 *
	 * @param index the index that specifies the column
	 * @return the receiver's bounding column rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Rectangle getBounds(int index) {
		checkWidget();

		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);

		int itemIndex = getItemIndex();
		if (itemIndex == -1) {
			return new Rectangle(0, 0, 0, 0);
		}

		if (!parent.columnsExist()) {
			if (index == 0) {
				Rectangle tb = getTextBounds(0);

				Rectangle rec = new Rectangle(tb.x - 1, tb.y - 1, tb.width + 2, tb.height + 2);

				if (getImage() != null) {
					var ib = getImageBounds(0);
					rec.x = ib.x - 1;
					rec.y = Math.min(ib.y - 1, rec.y);
					rec.width = tb.x + tb.width + 1 - rec.x;
					rec.height = Math.max(tb.height, ib.height);
				}

				return rec;
			}
			return new Rectangle(0, 0, 0, 0);
		}

		Rectangle b = getFullBounds();
		if (index < 0 || index >= parent.getColumnCount()) {
			return new Rectangle(0, 0, 0, 0);
		}

		Rectangle column = parent.getColumn(index).getBounds();

		// for index 0, we have to remove the arrow and checkbox from the rectangle
		if (index == 0) {
			int y = b.y;
			int height = b.height;
			int start;
			if (getImage(0) != null) {
				start = getImageBounds(0).x - 1; // this number is valid, because the margin left is set
			} else {
				start = getTextBounds(0).x - 1; // this number is valid, because the margin left also is set
			}
			int cellWidth = column.x + column.width - start;
			cellWidth = Math.max(0, cellWidth);
			// here it is possible that the bounds are outside of the columns x position and
			// width.
			return new Rectangle(start, y, cellWidth, height);
		}

		return new Rectangle(column.x, b.y, column.width, b.height);
	}

	/**
	 * Returns <code>true</code> if the receiver is checked, and false otherwise.
	 * When the parent does not have the <code>CHECK</code> style, return false.
	 *
	 * @return the checked state of the checkbox
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getChecked() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return (parent.style & SWT.CHECK) != 0 && checked;
	}

	/**
	 * Returns the font that the receiver will use to paint textual information for
	 * this item.
	 *
	 * @return the receiver's font
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public Font getFont() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return font != null ? font : parent.getFont();
	}

	/**
	 * Returns the font that the receiver will use to paint textual information for
	 * the specified cell in this item.
	 *
	 * @param index the column index
	 * @return the receiver's font
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public Font getFont(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getFont();
		}
		if (cellFont == null || cellFont[index] == null) {
			return getFont();
		}
		return cellFont[index];
	}

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 *
	 * @return the receiver's foreground color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 2.0
	 */
	public Color getForeground() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		if (foreground == null) {
			return parent.getForeground();
		}
		return foreground;
	}

	/**
	 *
	 * Returns the foreground color at the given column index in the receiver.
	 *
	 * @param index the column index
	 * @return the foreground color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public Color getForeground(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return getForeground();
		}
		Color cellColor = cellForeground != null ? cellForeground[index] : null;
		return cellColor == null ? getForeground() : cellColor;
	}

	/**
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the parent does not have the <code>CHECK</code> style, return false.
	 *
	 * @return the grayed state of the checkbox
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getGrayed() {
		checkWidget();
		Tree.logNotImplemented();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return (parent.style & SWT.CHECK) != 0 && grayed;
	}

	@Override
	public Image getImage() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return super.getImage();
	}

	/**
	 * Returns the image stored at the given column index in the receiver, or null
	 * if the image has not been set or if the column does not exist.
	 *
	 * @param index the column index
	 * @return the image stored at the given column index in the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Image getImage(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		if (index == 0) {
			return getImage();
		}
		if (images != null && 0 <= index && index < images.length) {
			return images[index];
		}
		return null;
	}

	/**
	 * Returns a rectangle describing the size and location relative to its parent
	 * of an image at a column in the Tree. An empty rectangle is returned if index
	 * exceeds the index of the Tree's last column.
	 *
	 * @param index the index that specifies the column
	 * @return the receiver's bounding image rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Rectangle getImageBounds(int index) {
		checkWidget();
		return DPIUtil.scaleDown(getImageBoundsInPixels(index), 100);
	}

	Rectangle getImageBoundsInPixels(int index) {
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1) {
			return new Rectangle(0, 0, 0, 0);
		}

		return renderer.getImageBounds(index);
	}

	/**
	 * Gets the image indent.
	 *
	 * @return the indent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getImageIndent() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return imageIndent;
	}

	@Override
	String getNameText() {
		if ((parent.style & SWT.VIRTUAL) != 0 && !cached) {
			return "*virtual*"; //$NON-NLS-1$
		}
		return super.getNameText();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Tree</code>.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Tree getParent() {
		checkWidget();
		return parent;
	}

	@Override
	public String getText() {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		return super.getText();
	}

	/**
	 * Returns the text stored at the given column index in the receiver, or empty
	 * string if the text has not been set.
	 *
	 * @param index the column index
	 * @return the text stored at the given column index in the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getText(int index) {
		checkWidget();
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		if (index == 0) {
			return getText();
		}
		if (strings != null) {
			if (0 <= index && index < strings.length) {
				String string = strings[index];
				return string != null ? string : "";
			}
		}
		return "";
	}

	/**
	 * Returns a rectangle describing the size and location relative to its parent
	 * of the text at a column in the Tree. An empty rectangle is returned if index
	 * exceeds the index of the Tree's last column.
	 *
	 * @param index the index that specifies the column
	 * @return the receiver's bounding text rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	public Rectangle getTextBounds(int index) {
		checkWidget();
		return DPIUtil.scaleDown(getTextBoundsInPixels(index), 100);
	}

	Rectangle getTextBoundsInPixels(int index) {
		if (!parent.checkData(this, true)) error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1) {
			return new Rectangle(0, 0, 0, 0);
		}

		return renderer.getTextBounds(index);
	}

	void redraw() {
		int index = getItemIndex();
		if (index < getParent().getTopIndex() || index > getParent().getLastVisibleIndex()) {
			return;
		}

		Rectangle b = getFullBounds();

		getParent().redraw(b.x, b.y, b.width, b.height, true);
	}

	void redraw(int column, boolean drawText, boolean drawImage) {
		if (!getParent().isVisible()) {
			return;
		}

		var index = getItemIndex();
		final int topIndex = getParent().getTopIndex();
		if (index < topIndex || index > getParent().getLastVisibleIndex())
			return;

		if (topIndex == topIndexAtCalculation && location != null) {
			Rectangle b = getFullBounds();
			getParent().redraw(b.x, b.y, b.width, b.height, true);
		} else {
			getParent().redraw();
		}
	}

	@Override
	void releaseHandle() {
		super.releaseHandle();
		parent = null;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		strings = null;
		images = null;
		cellFont = null;
		cellBackground = cellForeground = null;
	}

	/**
	 * Sets the receiver's background color to the color specified by the argument,
	 * or to the default system color for the item if the argument is null.
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 2.0
	 */
	public void setBackground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (color != null) {
			parent.setCustomDraw(true);
		}
		if (background == color) {
			return;
		}
		background = color;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw();
	}

	/**
	 * Sets the background color at the given column index in the receiver to the
	 * color specified by the argument, or to the default system color for the item
	 * if the argument is null.
	 *
	 * @param index the column index
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void setBackground(int index, Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		Color cellColor = null;
		if (color != null) {
			parent.setCustomDraw(true);
			cellColor = color;
		}
		if (cellBackground == null) {
			cellBackground = new Color[count];
			for (int i = 0; i < count; i++) {
				cellBackground[i] = null;
			}
		}
		if (cellBackground[index] == cellColor) {
			return;
		}
		cellBackground[index] = cellColor;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw(index, true, true);
	}

	/**
	 * Sets the checked state of the checkbox for this item. This state change only
	 * applies if the Tree was created with the SWT.CHECK style.
	 *
	 * @param checked the new checked state of the checkbox
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setChecked(boolean checked) {
		checkWidget();
		if ((parent.style & SWT.CHECK) == 0) {
			return;
		}
		if (this.checked == checked) {
			return;
		}
		setChecked(checked, false);
	}

	void setChecked(boolean checked, boolean notify) {
		this.checked = checked;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		if (notify) {
			Event event = new Event();
			event.item = this;
			event.detail = SWT.CHECK;
			parent.sendSelectionEvent(SWT.Selection, event, false);
		}
		redraw();
	}

	/**
	 * Sets the font that the receiver will use to paint textual information for
	 * this item to the font specified by the argument, or to the default font for
	 * that kind of control if the argument is null.
	 *
	 * @param font the new font (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void setFont(Font font) {
		checkWidget();

		clearCache();

		Tree.logNotImplemented();

		if (font != null && font.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		Font oldFont = this.font;
		Font newFont = font;
		if (oldFont == newFont) {
			return;
		}
		this.font = newFont;
		if (oldFont != null && oldFont.equals(newFont)) {
			return;
		}
		if (font != null) {
			parent.setCustomDraw(true);
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}

		redraw();
	}

	/**
	 * Sets the font that the receiver will use to paint textual information for the
	 * specified cell in this item to the font specified by the argument, or to the
	 * default font for that kind of control if the argument is null.
	 *
	 * @param index the column index
	 * @param font  the new font (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void setFont(int index, Font font) {
		checkWidget();

		clearCache();
		Tree.logNotImplemented();

		if (font != null && font.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		if (cellFont == null) {
			if (font == null) {
				return;
			}
			cellFont = new Font[count];
		}
		Font oldFont = cellFont[index];
		if (oldFont == font) {
			return;
		}
		cellFont[index] = font;
		if (oldFont != null && oldFont.equals(font)) {
			return;
		}
		if (font != null) {
			parent.setCustomDraw(true);
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw(index, true, false);
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the argument,
	 * or to the default system color for the item if the argument is null.
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 2.0
	 */
	public void setForeground(Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (color != null) {
			parent.setCustomDraw(true);
		}
		if (foreground == color) {
			return;
		}
		foreground = color;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw();
	}

	/**
	 * Sets the foreground color at the given column index in the receiver to the
	 * color specified by the argument, or to the default system color for the item
	 * if the argument is null.
	 *
	 * @param index the column index
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void setForeground(int index, Color color) {
		checkWidget();
		if (color != null && color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		Color pixel = null;
		if (color != null) {
			parent.setCustomDraw(true);
			pixel = color;
		}
		if (cellForeground == null) {
			cellForeground = new Color[count];
			for (int i = 0; i < count; i++) {
				cellForeground[i] = null;
			}
		}
		if (cellForeground[index] == pixel) {
			return;
		}
		cellForeground[index] = pixel;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw(index, true, false);
	}

	/**
	 * Sets the grayed state of the checkbox for this item. This state change only
	 * applies if the Tree was created with the SWT.CHECK style.
	 *
	 * @param grayed the new grayed state of the checkbox;
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setGrayed(boolean grayed) {
		checkWidget();
		Tree.logNotImplemented();
		if ((parent.style & SWT.CHECK) == 0) {
			return;
		}
		if (this.grayed == grayed) {
			return;
		}
		this.grayed = grayed;
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}
		redraw();
	}

	/**
	 * Sets the image for multiple columns in the Tree.
	 *
	 * @param images the array of new images
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the array of
	 *                                     images is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if one of
	 *                                     the images has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setImage(Image[] images) {
		checkWidget();
		if (images == null) error(SWT.ERROR_NULL_ARGUMENT);
		for (int i = 0; i < images.length; i++) {
			setImage(i, images[i]);
		}

		clearCache();
	}

	/**
	 * Sets the receiver's image at a column.
	 *
	 * @param index the column index
	 * @param image the new image
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the image
	 *                                     has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setImage(int index, Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		Image oldImage = null;
		if (index == 0) {
			if (image != null && image.type == SWT.ICON && image.equals(this.image)) {
				return;
			}
			oldImage = this.image;
			super.setImage(image);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1) {
			return;
		}
		if (images == null && index != 0) {
			images = new Image[count];
			images[0] = image;
		}
		if (images != null) {
			if (image != null && image.type == SWT.ICON && image.equals(images[index])) {
				return;
			}
			oldImage = images[index];
			images[index] = image;
		}
		if ((parent.style & SWT.VIRTUAL) != 0) {
			cached = true;
		}

		if (index == 0) {
			parent.setScrollWidth(this, false);
		}
		boolean drawText = (image == null && oldImage != null) || (image != null && oldImage == null);

		clearCache();
		redraw(index, drawText, true);

	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		setImage(0, image);

		clearCache();
	}

	/**
	 * Sets the indent of the first column's image, expressed in terms of the
	 * image's width.
	 *
	 * @param indent the new indent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @deprecated this functionality is not supported on most platforms
	 */
	@Deprecated
	public void setImageIndent(int indent) {
		checkWidget();

		Tree.logNotImplemented();

		if (indent < 0) {
			return;
		}
		if (imageIndent == indent) {
			return;
		}
		imageIndent = indent;

		clearCache();
		redraw();
	}

	/**
	 * Sets the text for multiple columns in the Tree.
	 * <p>
	 * Note: If control characters like '\n', '\t' etc. are used in the string, then
	 * the behavior is platform dependent.
	 * </p>
	 *
	 * @param strings the array of new strings
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the text is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setText(String[] strings) {
		checkWidget();
		if (strings == null) error(SWT.ERROR_NULL_ARGUMENT);
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if (string != null) {
				setText(i, string);
			}
		}

		clearCache();
	}

	void clearCache() {
		synchronized (this) {
			renderer.clearCache();
			this.itemIndex = -2;
			location = null;
			fullBounds = null;
		}
	}

	/**
	 * Sets the receiver's text at a column
	 * <p>
	 * Note: If control characters like '\n', '\t' etc. are used in the string, then
	 * the behavior is platform dependent.
	 * </p>
	 *
	 * @param index  the column index
	 * @param string the new text
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the text is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setText(int index, String string) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);

		try {
			if (index == 0) {
				if (string.equals(text)) {
					return;
				}
				super.setText(string);
			}
			int count = Math.max(1, parent.getColumnCount());
			if (0 > index || index > count - 1) {
				return;
			}
			if (strings == null && index != 0) {
				strings = new String[count];
				strings[0] = text;
			} else if (strings != null && strings.length < count) {
				String[] newStrings = new String[count];
				System.arraycopy(strings, 0, newStrings, 0, strings.length);
				strings = newStrings;
			}
			if (strings != null) {
				if (string.equals(strings[index])) {
					return;
				}
				strings[index] = string;
			}
		} finally {
			clearCache();
		}

		redraw(index, true, false);
	}

	@Override
	public void setText(String string) {
		checkWidget();
		setText(0, string);
		clearCache();
	}

	Point getLocation() {
		// TODO use a timestamp instead of specific values
		if (getParent().getTopIndex() == topIndexAtCalculation && location != null) {
			return location;
		}

		calculateLocation();
		return location;
	}

	void moveTextToRightAt(int index) {
		if (strings == null && images == null) {
			return;
		}

		java.util.List<String> newTexts = new ArrayList<>();
		java.util.List<Image> newImages = new ArrayList<>();

		for (int i = 0; i < index && i < strings.length; i++) {
			newTexts.add(strings[i]);
			newImages.add(getImage(i));
		}
		newTexts.add("");
		newImages.add(null);

		if (strings.length >= index) {
			for (int k = index; k < strings.length; k++) {
				newTexts.add(strings[k]);
				newImages.add(getImage(k));
			}
		}

		strings = newTexts.toArray(new String[0]);
		images = newImages.toArray(new Image[0]);
	}

	void moveTextsItemsToLeft(int index) {
		if (strings == null && images == null) {
			return;
		}

		if (strings != null && index > 0 && index < strings.length) {
			java.util.List<String> newTexts = new ArrayList<>();
			newTexts.addAll(Arrays.stream(strings).collect(Collectors.toList()));
			newTexts.remove(index);
			strings = newTexts.toArray(new String[0]);
		}

		if (images != null && index > 0 && index < images.length) {
			java.util.List<Image> newImages = new ArrayList<>();
			newImages.addAll(Arrays.stream(images).collect(Collectors.toList()));
			newImages.remove(index);
			images = newImages.toArray(new Image[0]);
		}
	}

	boolean isInArrowArea(Point p) {
		return renderer.getArrowRectangle().contains(p);
	}

	boolean isInCheckArea(Point p) {
		return renderer.getCheckboxRectangle() != null && renderer.getCheckboxRectangle().contains(p);
	}

	void toggleCheck() {
		setChecked(!getChecked(), true);
		redraw();
	}

	public Point computeCellSize(int colIndex) {
		return renderer.computeCellSize(colIndex);
	}

	public boolean getExpanded() {
		return this.expanded;
	}

	public TreeItem getItem(int index) {
		checkWidget();
		return _getItem(index);
	}

	public int getItemCount() {
		if (isVirtual()) {
			return virtualItemCount;
		}
		return itemsList.size();
	}

	public TreeItem[] getItems() {
		if (isVirtual()) {
			var result = new TreeItem[getItemCount()];
			for (int i = 0; i < getItemCount(); i++) {
				result[i] = _getItem(i);
			}
			return result;
		}
		return itemsList.toArray(new TreeItem[0]);
	}

	TreeItem[] getItemsUpToNumber(Integer upToElements) {
		if (isVirtual()) {
			int count = Math.min(getItemCount(), upToElements);
			var result = new TreeItem[count];
			for (int i = 0; i < count; i++) {
				result[i] = _getItem(i);
			}
			return result;
		}
		return itemsList.toArray(new TreeItem[0]);
	}

	TreeItem _getItem(int index) {
		return _getItem(index, true);
	}

	TreeItem _getItem(int index, boolean create) {
		return _getItem(index, create, -1);
	}

	TreeItem _getItem(int index, boolean create, int count) {
		if (isVirtual()) {
			if (index < virtualItemCount) {
				TreeItem e = virtualItemsList.get(index);
				if (e == null && create) {
					e = new TreeItem(this, SWT.None, index);
				}
				return e;
			}
			error(SWT.ERROR_INVALID_RANGE);
		}

		if (index >= getItemCount()) error(SWT.ERROR_INVALID_RANGE);
		if (index < 0) error(SWT.ERROR_INVALID_RANGE);

		return itemsList.get(index);
	}

	public TreeItem getParentItem() {
		return parentItem;
	}

	public TreeItem getRootItem() {
		var element = this;

		while (getParentItem() != null) {
			if (element != getParentItem()) {
				element = getParentItem();
			}
			return getParentItem();
		}

		return element;
	}

	public int indexOf(TreeItem item) {
		checkWidget();

		if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (isVirtual()) {
			for (var e : virtualItemsList.entrySet()) {
				if (item.equals(e.getValue())) {
					return e.getKey();
				}
			}
			return -1;
		}
		return itemsList.indexOf(item);
	}

	int getIndent() {
		if (parentItem == null) {
			return 0;
		}
		return parentItem.getIndent() + 1;
	}

	public void setExpanded(boolean expandItem) {
		setExpanded(expandItem, true);
	}

	private void setExpanded(boolean expandItem, boolean synchronizeTree) {
		if (!isVirtual() || virtualItemCount <= 0) {
			if (itemsList.isEmpty()) {
				this.expanded = false;
				return;
			}
		}

		if (this.expanded == expandItem) {
			return;
		}

		this.expanded = expandItem;

		Event event = new Event();
		event.item = this;

		getParent().notifyListeners(this.expanded ? SWT.Expand : SWT.Collapse, event);

		if (synchronizeTree) {
			getParent().synchronizeArrangements(true);
		}
	}

	public void removeAll() {
		java.util.List<TreeItem> list = new ArrayList<>();
		list.addAll(itemsList);

		itemsList.clear();

		list.stream().forEach(t -> t.dispose());
	}

	public void setItemCount(int count) {
		checkWidget();

		count = Math.max(0, count);
		if (isVirtual()) {
			boolean redraw = count > this.virtualItemCount;
			this.virtualItemCount = count;

			while (!virtualItemsList.isEmpty()) {
				Integer key = virtualItemsList.lastKey();
				if (key >= count) {
					virtualItemsList.remove(key);
				} else {
					break;
				}
			}

			if (redraw) {
				this.redraw();
			}

			return;
		}

		if (count == itemsList.size()) {
			return;
		}
		if (count > itemsList.size()) {
			for (int i = itemsList.size(); i < count; i++) {
				new TreeItem(this, SWT.None);
			}
		} else {
			for (int i = itemsList.size() - 1; i >= count; i--) {
				itemsList.get(i).dispose();
			}
		}
	}

	private boolean isVirtual() {
		return getParent().isVirtual();
	}

	boolean toggleExpand() {
		if (getItemCount() == 0) {
			this.expanded = false;
			return false;
		}

		setExpanded(!getExpanded(), false);
		return true;
	}


	public void clearAll(boolean all) {
		if (getItemCount() == 0)
			return;

		for (var e : itemsList) {
			if (e != null) {
				e.clear();
			}
		}
	}

	public void clear(int index, boolean all) {
		clear(new int[] { index });
	}

	public void clear(int[] indices) {
		checkWidget();

		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (indices.length == 0) {
			return;
		}

		int count = getItemCount();
		for (final int index : indices) {
			if (0 > index || index >= count) error(SWT.ERROR_INVALID_RANGE);

			TreeItem item = _getItem(index, false);
			if (item != null) {
				item.clear();
				item.redraw();
			}
		}
	}

	int computeAllExpandedChildCount() {
		if (!getExpanded()) {
			return 0;
		}

		int count = 0;
		if (isVirtual()) {
			count = getItemCount();

			for (Map.Entry<Integer, TreeItem> e : virtualItemsList.entrySet()) {
				if (e.getValue() != null) {
					TreeItem v = e.getValue();
					count += v.computeAllExpandedChildCount();
				}
			}
		} else {
			for (var v : itemsList) {
				if (v != null) {
					count++;
					count += v.computeAllExpandedChildCount();
				}
			}
		}

		return count;
	}
}
