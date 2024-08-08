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
 * Instances of this class represent a selectable user interface object that
 * represents an item in a table.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem,
 *      TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TableItem extends Item {

	/** border width */
	static final int DEFAULT_BORDER_WIDTH = 1;

	Table parent;
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

	private Point location;
	private Rectangle bounds;


	// TODO implement alignment
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;

	private Listener listener;

	// TODO implement accessibility
	private Accessible acc;
	private AccessibleAdapter accAdapter;

	private int itemIndex = -2;


	private TableItemRenderer renderer = new TableItemRenderer(this);

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>) and a style value describing its behavior and appearance.
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
	public TableItem(Table parent, int style) {
		this(parent, style, checkNull(parent).getItemCount(), true);

		initialize();
		this._addListener(style, null);

	}

	private void initialize() {

		listener = event -> {
			switch (event.type) {
			case SWT.Dispose:
				onDispose(event);
				break;
			case SWT.MouseDown:
				onMouseDown(event);
				break;
			case SWT.MouseUp:
				onMouseUp(event);
				break;
			case SWT.Paint:
				onPaint(event);
				break;
			case SWT.Resize:
				onResize();
				break;
			case SWT.FocusIn:
				onFocusIn();
				break;
			case SWT.FocusOut:
				onFocusOut();
				break;
			case SWT.Traverse:
				onTraverse(event);
				break;
			case SWT.Selection:
				onSelection(event);
				break;
			}
		};
		addListener(SWT.Dispose, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Selection, listener);

		addTypedListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				onKeyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				onKeyPressed(e);
			}
		}, SWT.KeyUp, SWT.KeyDown);

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

		}, SWT.MouseEnter, SWT.MouseExit, SWT.MouseHover);

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

	private void onKeyPressed(KeyEvent event) {
	}

	private void onKeyReleased(KeyEvent event) {

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
		if ((e.stateMask & SWT.BUTTON1) != 0)
			handleSelection();
		else
			redraw();
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Table</code>), a style value describing its behavior and appearance,
	 * and the index at which to place it in the items maintained by its parent.
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
	public TableItem(Table parent, int style, int index) {
		this(parent, style, index, true);
	}

	TableItem(Table parent, int style, int index, boolean create) {
		super(parent, style);

		if (index < 0 || index > parent.getItemCount()) {
			error(SWT.ERROR_INVALID_RANGE);
		}

		this.parent = parent;
		if (create)
			parent.createItem(this, index);
	}

	static Table checkNull(Table control) {
		if (control == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return control;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
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
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = false;
	}

	@Override
	void destroyWidget() {
		parent.destroyItem(this);
		releaseHandle();
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

		if (background != null)
			return background;

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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return getBackground();
		Color cell = cellBackground != null ? cellBackground[index] : null;
		return cell == null ? getBackground() : cell;
	}

	/**
	 * Returns a rectangle describing the size and location of the receiver's text
	 * and image relative to its parent.
	 *
	 * This exclude the checkbox if the table has style SWT.CHECK.
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

		var full = getFullBounds();

		int width = full.width;
		int x = full.x;

		int shift = Table.TABLE_INITIAL_RIGHT_SHIFT;
		if ((getParent().getStyle() & SWT.CHECK) != 0)
			shift = Table.TABLE_CHECKBOX_RIGHT_SHIFT;

		if (getParent().columnsExist()) {

			width = width - shift;
			width = Math.max(0, width);
			x += shift;
		} else {
			x += shift;
		}

		return new Rectangle(x, full.y, width, full.height);

	}

	private int getItemIndex() {
		if (this.itemIndex == -2) {
			this.itemIndex = parent.indexOf(this);
		}
		return this.itemIndex;

	}

	/**
	 * The method getBounds excludes a checkbox if visible and also the first pixel
	 * on the right are excluded in the first column.
	 *
	 * This includes the checkbox if the table has style SWT.CHECK.
	 *
	 *
	 * @return the full Rectangle including a checkbox and initial pixels.
	 */
	public Rectangle getFullBounds() {

		if (topIndexAtCalculation == getParent().getTopIndex() && bounds != null && Table.USE_CACHES)
			return bounds;

		this.bounds = null;
		this.location = null;

		checkWidget();
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1)
			return new Rectangle(0, 0, 0, 0);

		Point p = renderer.computeSize(false);

		synchronized (getParent()) {
			if (this.location == null)
				calculateLocation();
			bounds = new Rectangle(location.x, location.y, p.x, p.y);
		}

		return bounds;

	}

	private void calculateLocation() {

		int index = getItemIndex();

		if (getParent().getTopIndex() == index) {
			setLocation(getParent().getTopIndexItemPosition());
		} else if (getParent().getTopIndex() < index) {

			for (int i = getParent().getTopIndex(); i < index; i = Math.min(i + 1000, index)) {
				var it = getParent()._getItem(i, false);
				it.getLocation();
			}

			var prevItem = getParent().getItem(index - 1);
			var prevBounds = prevItem.getLocation();
			int fullHeightDiff = TableItemsHandler.getItemsHeight(prevItem);
			setLocation(new Point(prevBounds.x, prevBounds.y + fullHeightDiff));
		} else {

			for (int i = getParent().getTopIndex(); i > index; i = Math.max(i - 1000, index)) {
				var it = getParent().getItem(i);
				it.getLocation();
			}

			var prevItem = getParent().getItem(index + 1);
			var prevBounds = prevItem.getLocation();
			int fullHeightDiff = TableItemsHandler.getItemsHeight(this);
			setLocation(new Point(prevBounds.x, prevBounds.y - fullHeightDiff));
		}
		topIndexAtCalculation = getParent().getTopIndex();

	}

	private void setLocation(Point l) {
		this.location = l;
	}

	Point getSize() {

		return renderer.computeSize(false);

	}



	/**
	 * Returns a rectangle describing the receiver's size and location relative to
	 * its parent at a column in the table.
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

		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);

		if (parent.getColumnCount() == 0) {

			if (index == 0)
				return getBounds();
			return new Rectangle(0, 0, 0, 0);

		}

		if (index < 0 || index >= parent.getColumnCount())
			return new Rectangle(0, 0, 0, 0);

		var b = getBounds();
		var column = parent.getColumn(index).getBounds();

		int y = b.y;
		int height = b.height;

		int width = column.width;
		int x = column.x;

		if (index == 0) {

			int shift = Table.TABLE_INITIAL_RIGHT_SHIFT;

			if ((getParent().getStyle() & SWT.CHECK) != 0)
				shift = Table.TABLE_CHECKBOX_RIGHT_SHIFT;
			// reduce width by shift. This cell must be by default smaller than the others.
			// If there is a checkbox, this also must be considered.
			width = width - shift;
			width = Math.max(0, width);
			x += shift;
		}

		return new Rectangle(x, y, width, height);
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		if ((parent.style & SWT.CHECK) == 0)
			return false;
		return checked;
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return getFont();
		if (cellFont == null || cellFont[index] == null)
			return getFont();
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		if (foreground == null)
			return parent.getForeground();
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return getForeground();
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		if ((parent.style & SWT.CHECK) == 0)
			return false;
		return grayed;
	}

	@Override
	public Image getImage() {
		checkWidget();
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		if (index == 0)
			return getImage();
		if (images != null) {
			if (0 <= index && index < images.length)
				return images[index];
		}
		return null;
	}

	/**
	 * Returns a rectangle describing the size and location relative to its parent
	 * of an image at a column in the table. An empty rectangle is returned if index
	 * exceeds the index of the table's last column.
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1)
			return new Rectangle(0, 0, 0, 0);

		Table.logNotImplemented();

		return new Rectangle(0, 0, 0, 0);
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		return imageIndent;
	}

	@Override
	String getNameText() {
		if ((parent.style & SWT.VIRTUAL) != 0) {
			if (!cached)
				return "*virtual*"; //$NON-NLS-1$
		}
		return super.getNameText();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Table</code>.
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
	public Table getParent() {
		checkWidget();
		return parent;
	}

	@Override
	public String getText() {
		checkWidget();
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		if (index == 0)
			return getText();
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
	 * of the text at a column in the table. An empty rectangle is returned if index
	 * exceeds the index of the table's last column.
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
		if (!parent.checkData(this, true))
			error(SWT.ERROR_WIDGET_DISPOSED);
		int itemIndex = getItemIndex();
		if (itemIndex == -1)
			return new Rectangle(0, 0, 0, 0);
		Table.logNotImplemented();

		return new Rectangle(0, 0, 0, 0);
	}

	void redraw() {

		var index = getItemIndex();
		if (index < getParent().getTopIndex() || index > getParent().getLastVisibleIndex())
			return;

		Rectangle b = getBounds();

		if (renderer.checkboxBounds != null) {

			Rectangle newRect = new Rectangle(0, 0, 0, 0);

			newRect.x = renderer.checkboxBounds.x;
			newRect.y = b.y;
			newRect.width = b.x + b.width - newRect.x;
			newRect.height = b.height;

			b = newRect;

		}

		getParent().redraw(b.x, b.y, b.width, b.height, true);

	}

	void redraw(int column, boolean drawText, boolean drawImage) {

		if (!getParent().isVisible())
			return;

		var index = getItemIndex();
		if (index < getParent().getTopIndex() || index > getParent().getLastVisibleIndex())
			return;

		if (getParent().getTopIndex() == topIndexAtCalculation && location != null) {
			Rectangle b = getBounds();
			getParent().redraw(b.x, b.y, b.width, b.height, true);
		} else
			getParent().redraw();

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
		if (color != null && color.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (color != null) {
			parent.setCustomDraw(true);
		}
		if (background == color)
			return;
		background = color;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
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
		if (color != null && color.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return;
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
		if (cellBackground[index] == cellColor)
			return;
		cellBackground[index] = cellColor;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
		redraw(index, true, true);
	}

	/**
	 * Sets the checked state of the checkbox for this item. This state change only
	 * applies if the Table was created with the SWT.CHECK style.
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
		if ((parent.style & SWT.CHECK) == 0)
			return;
		if (this.checked == checked)
			return;
		setChecked(checked, false);
	}

	void setChecked(boolean checked, boolean notify) {
		this.checked = checked;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
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

		Table.logNotImplemented();

		if (font != null && font.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Font oldFont = this.font;
		Font newFont = font;
		if (oldFont == newFont)
			return;
		this.font = newFont;
		if (oldFont != null && oldFont.equals(newFont))
			return;
		if (font != null)
			parent.setCustomDraw(true);
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;

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
		Table.logNotImplemented();

		if (font != null && font.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return;
		if (cellFont == null) {
			if (font == null)
				return;
			cellFont = new Font[count];
		}
		Font oldFont = cellFont[index];
		if (oldFont == font)
			return;
		cellFont[index] = font;
		if (oldFont != null && oldFont.equals(font))
			return;
		if (font != null)
			parent.setCustomDraw(true);
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
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
		if (color != null && color.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (color != null) {
			parent.setCustomDraw(true);
		}
		if (foreground == color)
			return;
		foreground = color;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
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
		if (color != null && color.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return;
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
		if (cellForeground[index] == pixel)
			return;
		cellForeground[index] = pixel;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
		redraw(index, true, false);
	}

	/**
	 * Sets the grayed state of the checkbox for this item. This state change only
	 * applies if the Table was created with the SWT.CHECK style.
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
		if ((parent.style & SWT.CHECK) == 0)
			return;
		if (this.grayed == grayed)
			return;
		this.grayed = grayed;
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;
		redraw();
	}

	/**
	 * Sets the image for multiple columns in the table.
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
		if (images == null)
			error(SWT.ERROR_NULL_ARGUMENT);
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
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Image oldImage = null;
		if (index == 0) {
			if (image != null && image.type == SWT.ICON) {
				if (image.equals(this.image))
					return;
			}
			oldImage = this.image;
			super.setImage(image);
		}
		int count = Math.max(1, parent.getColumnCount());
		if (0 > index || index > count - 1)
			return;
		if (images == null && index != 0) {
			images = new Image[count];
			images[0] = image;
		}
		if (images != null) {
			if (image != null && image.type == SWT.ICON) {
				if (image.equals(images[index]))
					return;
			}
			oldImage = images[index];
			images[index] = image;
		}
		if ((parent.style & SWT.VIRTUAL) != 0)
			cached = true;

		if (index == 0)
			parent.setScrollWidth(this, false);
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

		Table.logNotImplemented();

		if (indent < 0)
			return;
		if (imageIndent == indent)
			return;
		imageIndent = indent;

		clearCache();
		redraw();
	}

	/**
	 * Sets the text for multiple columns in the table.
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
		if (strings == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if (string != null)
				setText(i, string);
		}

		clearCache();

	}

	void clearCache() {

		synchronized (this) {
			renderer.clearCache();
			this.itemIndex = -2;
			location = null;
			bounds = null;
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

		try {

			if (string == null)
				error(SWT.ERROR_NULL_ARGUMENT);

			if (index == 0) {
				if (string.equals(text))
					return;
				super.setText(string);
			}
			int count = Math.max(1, parent.getColumnCount());
			if (0 > index || index > count - 1)
				return;
			if (strings == null && index != 0) {
				strings = new String[count];
				strings[0] = text;
			} else if (strings != null && strings.length < count) {
				String[] newStrings = new String[count];
				System.arraycopy(strings, 0, newStrings, 0, strings.length);
				strings = newStrings;
			}
			if (strings != null) {
				if (string.equals(strings[index]))
					return;
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

		if (getParent().getTopIndex() == topIndexAtCalculation && location != null)
			return location;

		calculateLocation();
		return location;

	}

	void moveTextToRightAt(int index) {

		if (strings == null && images == null)
			return;

		java.util.List<String> newTexts = new ArrayList<>();
		java.util.List<Image> newImages = new ArrayList<>();

		for (int i = 0; i < index && i < strings.length; i++) {
			newTexts.add(strings[i]);
			newImages.add(getImage(i));
		}
		newTexts.add("");
		newImages.add(null);

		if (strings.length >= index)
			for (int k = index; k < strings.length; k++) {

				newTexts.add(strings[k]);
				newImages.add(getImage(k));

			}

		strings = newTexts.toArray(new String[0]);
		images = newImages.toArray(new Image[0]);

	}

	void moveTextsItemsToLeft(int index) {

		if (strings == null && images == null)
			return;

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

	boolean isInCheckArea(Point p) {


		if (renderer.checkboxBounds != null)
			return renderer.checkboxBounds.contains(p);
		return false;
	}

	void toggleCheck() {

		this.checked = !this.checked;
		redraw();

	}

	public Point computeCellSize(int colIndex) {
		return renderer.computeCellSize(colIndex);
	}



}
