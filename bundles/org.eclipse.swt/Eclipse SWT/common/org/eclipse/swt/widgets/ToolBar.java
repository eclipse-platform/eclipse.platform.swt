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

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.toolbar.*;

/**
 * Instances of this class support the layout of selectable tool bar items.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>ToolItem</code>.
 * </p>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add <code>Control</code> children to it, or set a
 * layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolBar extends Composite {

	/**
	 * Renderer interface for the {@link ToolBar} widget. All renderers have to
	 * implement this to work with the ToolBar.
	 */
	public static interface IToolBarRenderer {

		/**
		 * Renders the handle.
		 *
		 * @param gc     GC to render with.
		 * @param bounds Bounds of the rendering. x and y are always 0.
		 */
		void render(GC gc, Rectangle bounds);

		/**
		 * Computes the size of the rendered ToolBar.
		 *
		 * @return The size as {@link Point}
		 */
		Point computeSize(Point size);

		/**
		 * Returns the row count of the rendered widget.
		 *
		 * @return The row count.
		 */
		int rowCount();
	}

	/** The {@link ToolItem}s contained in the {@link ToolBar} */
	private final java.util.List<ToolItem> items = new ArrayList<>();

	@Deprecated
	public int itemCount;

	/** The renderer used to render to {@link ToolBar}. */
	private final IToolBarRenderer renderer;

	private final boolean flat;
	private final boolean wrap;
	private final boolean shadowOut;
	private final boolean right;
	private final boolean border;
	private final boolean vertical;
	private final boolean rightToLeft;

	public ToolBar(Composite parent, int style) {
		this(parent, style, false);
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
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
	 * @see SWT#FLAT
	 * @see SWT#WRAP
	 * @see SWT#RIGHT
	 * @see SWT#HORIZONTAL
	 * @see SWT#SHADOW_OUT
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass()
	 * @see Widget#getStyle()
	 */
	public ToolBar(Composite parent, int style, boolean internal) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;

		renderer = new ToolBarRenderer(this);

		Listener listener = event -> {
			if (isDisposed()) {
				return;
			}
			switch (event.type) {
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseExit -> onMouseExit(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Paint -> onPaint(event);
			case SWT.Resize -> redraw();
			}
		};

		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseExit, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);

		// cache flags for later use
		flat = isFlag(style, SWT.FLAT);
		wrap = isFlag(style, SWT.WRAP);
		shadowOut = isFlag(style, SWT.SHADOW_OUT);
		right = isFlag(style, SWT.RIGHT);
		border = isFlag(style, SWT.BORDER);
		vertical = isFlag(style, SWT.VERTICAL);
		rightToLeft = isFlag(style, SWT.RIGHT_TO_LEFT);

		super.style |= vertical ? SWT.VERTICAL : SWT.HORIZONTAL;
	}

	private static boolean isFlag(int style, int flag) {
		return (style & flag) == flag;
	}

	static int checkStyle(int style) {
		style &= ~SWT.VERTICAL;
		style &= ~SWT.HORIZONTAL;

		return style;
	}

	/** Indicates if the SWT.FLAT style flag is set. */
	public boolean isFlat() {
		return flat;
	}

	/** Indicates if the SWT.WRAP style flag is set. */
	public boolean isWrap() {
		return wrap;
	}

	/** Indicates if the SWT.SHADOW_OUT style flag is set. */
	public boolean isShadowOut() {
		return shadowOut;
	}

	/** Indicates if the SWT.RIGHT style flag is set. */
	public boolean isRight() {
		return right;
	}

	/** Indicates if the SWT.BORDER style flag is set. */
	public boolean isBorder() {
		return border;
	}

	/** Indicates if the SWT.HORIZONTAL style flag is set. */
	public boolean isHorizontal() {
		return !isVertical();
	}

	/** Indicates if the SWT.VERTICAL style flag is set. */
	public boolean isVertical() {
		return vertical;
	}

	/** Indicates if the SWT.RIGHT_TO_LEFT style flag is set. */
	public boolean isRightToLeft() {
		return rightToLeft;
	}

	/** Indicates if the SWT.LEFT_TO_RIGHT style flag is set. */
	public boolean isLeftToRight() {
		return !isRightToLeft();
	}

	private void onMouseExit(Event event) {
		boolean paintRequested = false;
		List<ToolItem> copy = List.copyOf(items);
		for (ToolItem item : copy) {
			paintRequested |= item.notifyMouseExit();
		}

		if (paintRequested) {
			redraw();
		}
	}

	private void onMouseMove(Event event) {
		boolean paintRequested = false;
		List<ToolItem> copy = List.copyOf(items);
		for (ToolItem item : copy) {
			paintRequested |= item.notifyMouseMove(toPoint(event));
		}

		if (paintRequested) {
			redraw();
		}
	}

	private void onMouseDown(Event event) {
		boolean paintRequested = false;
		List<ToolItem> copy = List.copyOf(items);
		for (ToolItem item : copy) {
			paintRequested |= item.notifyMouseDown(toPoint(event));
		}

		if (paintRequested) {
			redraw();
		}
	}

	private void onMouseUp(Event event) {
		boolean paintRequested = false;
		List<ToolItem> copy = List.copyOf(items);
		for (ToolItem item : copy) {
			paintRequested |= item.notifyMouseUp(toPoint(event));
		}

		if (paintRequested) {
			redraw();
		}
	}

	private Point toPoint(Event e) {
		Point p = e.getLocation();
		if (isLeftToRight()) {
			return p;
		} else {
			return isLeftToRight() ? p : new Point(getBounds().width - p.x, p.y);
		}
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}

		Rectangle bounds = getBounds();
		if (bounds.width == 0 && bounds.height == 0) {
			return;
		}

		Drawing.drawWithGC(this, event.gc, gc -> renderer.render(gc, bounds));
	}

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * control need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the receiver's
	 * <em>contents</em> have changed, therefore any caches that a layout manager
	 * containing the control may have been keeping need to be flushed. When the
	 * control is resized, the changed flag will be <code>false</code>, so layout
	 * manager caches can be retained.
	 * </p>
	 *
	 * @param wHint   the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint   the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed <code>true</code> if the control's contents have changed, and
	 *                <code>false</code> otherwise
	 * @return the preferred size of the control.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point sizeHint = new Point(wHint, hHint);
		Point size = renderer.computeSize(sizeHint);

		return size;
	}

	Point computeSizeInPixels(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point sizeHint = new Point(wHint, hHint);
		Point size = renderer.computeSize(sizeHint);

		return DPIUtil.autoScaleUp(size);
	}

	void createItem(ToolItem item, int index) {
		items.add(index, item);
		itemCount = items.size();
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver. Throws an
	 * exception if the index is out of range.
	 *
	 * @param index the index of the item to return
	 * @return the item at the given index
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the list minus 1 (inclusive)</li>
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
	public ToolItem getItem(int index) {
		checkRange(index, items.size());
		checkWidget();
		return items.get(index);
	}

	public int getItemIndex(ToolItem item) {
		return items.indexOf(item);
	}

	/**
	 * Returns the item at the given point in the receiver or null if no such item
	 * exists. The point is in the coordinate system of the receiver.
	 *
	 * @param point the point used to locate the item
	 * @return the item at the given point
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the point is
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
	public ToolItem getItem(Point point) {
		checkWidget();
		for (ToolItem item : getItems()) {
			if (item.getBounds().contains(point)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns the number of items contained in the receiver.
	 *
	 * @return the number of items
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getItemCount() {
		checkWidget();
		return items.size();
	}

	/**
	 * Returns an array of <code>ToolItem</code>s which are the items in the
	 * receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain its
	 * list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the items in the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public ToolItem[] getItems() {
		checkWidget();
		return items.toArray(ToolItem[]::new);
	}

	/**
	 * Returns the number of rows in the receiver. When the receiver has the
	 * <code>WRAP</code> style, the number of rows can be greater than one.
	 * Otherwise, the number of rows is always one.
	 *
	 * @return the number of items
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getRowCount() {
		checkWidget();
		return renderer.rowCount();
	}

	/**
	 * Searches the receiver's list starting at the first item (index 0) until an
	 * item is found that is equal to the argument, and returns the index of that
	 * item. If no item is found, returns -1.
	 *
	 * @param item the search item
	 * @return the index of the item
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the tool
	 *                                     item is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the tool
	 *                                     item has been disposed</li>
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
	public int indexOf(ToolItem item) {
		checkItem(item);
		checkWidget();
		return items.indexOf(item);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		redraw();
	}

	void radioItemSelected(ToolItem selectedItem) {
		int selectedIndex = getItemIndex(selectedItem);
		if (selectedIndex < 0) {
			return;
		}

		// if a radio item is selected, we need to un-select
		// each other radio item in the same group. A group
		// is a uninterrupted series of items of the same type

		// un-select each radio item before the selected one
		for (int i = selectedIndex - 1; i >= 0; i--) {
			ToolItem item = getItem(i);
			if ((item.style & SWT.RADIO) == SWT.RADIO) {
				item.internalUnselect();
			} else {
				break;
			}

		}

		// un-select each radio item before the selected one
		for (int i = selectedIndex + 1; i < getItemCount(); i++) {
			ToolItem item = getItem(i);
			if ((item.style & SWT.RADIO) == SWT.RADIO) {
				item.internalUnselect();
			} else {
				break;
			}
		}
	}

	@Override
	void releaseChildren(boolean destroy) {
		List<ToolItem> copy = List.copyOf(items);
		for (ToolItem item : copy) {
			item.dispose();
		}
	}

	void notifyItemDisposed(ToolItem toolItem) {
		items.remove(toolItem);
		itemCount = items.size();
	}

	private void checkRange(int i, int size) {
		if (i < 0 || i >= size) {
			error(SWT.ERROR_INVALID_RANGE);
		}
	}

	private void checkItem(ToolItem item) {
		if (item == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		} else if (item.isDisposed()) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
	}
}
