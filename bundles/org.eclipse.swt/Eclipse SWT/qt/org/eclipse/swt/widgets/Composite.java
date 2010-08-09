/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.Collection;
import java.util.LinkedList;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt.FocusReason;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QPalette.ColorRole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class are controls which are capable of containing other
 * controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE,
 * NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>,
 * <code>NO_MERGE_PAINTS</code>, and <code>NO_REDRAW_RESIZE</code> styles are
 * intended for use with <code>Canvas</code>. They can be used with
 * <code>Composite</code> if you are drawing your own, but their behavior is
 * undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p>
 * <p>
 * Note: The <code>CENTER</code> style, although undefined for composites, has
 * the same value as <code>EMBEDDED</code> which is used to embed widgets from
 * other widget toolkits into SWT. On some operating systems (GTK, Motif), this
 * may cause the children of this composite to be obscured.
 * </p>
 * <p>
 * This class may be subclassed by custom control implementors who are building
 * controls that are constructed from aggregates of other controls.
 * </p>
 * 
 * @see Canvas
 * @see <a href="http://www.eclipse.org/swt/snippets/#composite">Composite
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public class Composite extends Scrollable {
	private Layout layout;
	private Control[] tabList;
	protected int layoutCount;
	private int backgroundMode = SWT.INHERIT_NONE;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Composite() {
	}

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_FOCUS
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_RADIO_GROUP
	 * @see Widget#getStyle
	 */
	public Composite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	QWidget createQWidget(int style) {
		QWidget widget = super.createQWidget(style);
		state |= CANVAS;
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			state |= THEME_BACKGROUND;
		}
		if ((style & SWT.TRANSPARENT) != 0) {
			System.out.println("TODO: handle SWT.TRANSPARENT"); //$NON-NLS-1$
		}
		return widget;
	}

	protected void updateAutoFillBackground() {
		//		if ((style & SWT.NO_BACKGROUND) == 0) {
		//			getQMasterWidget().setAutoFillBackground(true);
		//		} else {
		//			getQMasterWidget().setAutoFillBackground(false);
		//			System.out.println(this + " no bg");
		//		}
	}

	protected boolean noBackground() {
		return (style & SWT.NO_BACKGROUND) == 1;
	}

	@Override
	protected void updateBackground() {
		if ((state & PARENT_BACKGROUND) == 0) {
			updateAutoFillBackground();
			applyBackgroundColor(getDefaultBackgroundColor());
		} else {
			super.updateBackground();
		}
	}

	@Override
	protected ColorRole[] getBackgroundColorRoles() {
		if (backgroundMode == SWT.INHERIT_FORCE) {
			return super.getBackgroundColorRoles();
		} else {
			return new ColorRole[] { ColorRole.Window };
		}
	}

	void addQChild(Control control) {
		control.getQMasterWidget().setParent(getQWidget());
	}

	Control[] _getTabList() {
		if (tabList == null) {
			return tabList;
		}
		int count = 0;
		for (int i = 0; i < tabList.length; i++) {
			if (!tabList[i].isDisposed()) {
				count++;
			}
		}
		if (count == tabList.length) {
			return tabList;
		}
		Control[] newList = new Control[count];
		int index = 0;
		for (int i = 0; i < tabList.length; i++) {
			if (!tabList[i].isDisposed()) {
				newList[index++] = tabList[i];
			}
		}
		tabList = newList;
		return tabList;
	}

	/**
	 * Clears any data that has been cached by a Layout for all widgets that are
	 * in the parent hierarchy of the changed control up to and including the
	 * receiver. If an ancestor does not have a layout, it is skipped.
	 * 
	 * @param changed
	 *            an array of controls that changed state and require a
	 *            recalculation of size
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the changed array is null
	 *                any of its controls are null or have been disposed</li>
	 *                <li>ERROR_INVALID_PARENT - if any control in changed is
	 *                not in the widget tree of the receiver</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void changed(Control[] changed) {
		checkWidget();
		if (changed == null) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < changed.length; i++) {
			Control control = changed[i];
			if (control == null) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			boolean ancestor = false;
			Composite composite = control.parent;
			while (composite != null) {
				ancestor = composite == this;
				if (ancestor) {
					break;
				}
				composite = composite.parent;
			}
			if (!ancestor) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		for (int i = 0; i < changed.length; i++) {
			Control child = changed[i];
			Composite composite = child.parent;
			while (child != this) {
				if (composite.layout == null || !composite.layout.flushCache(child)) {
					composite.state |= LAYOUT_CHANGED;
				}
				child = composite;
				composite = child.parent;
			}
		}
	}

	@Override
	protected void checkSubclass() {
		/* Do nothing - Subclassing is allowed */
	}

	@Override
	Control[] computeTabList() {
		Control result[] = super.computeTabList();
		if (result.length == 0) {
			return result;
		}
		Control[] list = tabList != null ? _getTabList() : _getChildren();
		for (int i = 0; i < list.length; i++) {
			Control child = list[i];
			Control[] childList = child.computeTabList();
			if (childList.length != 0) {
				Control[] newResult = new Control[result.length + childList.length];
				System.arraycopy(result, 0, newResult, 0, result.length);
				System.arraycopy(childList, 0, newResult, result.length, childList.length);
				result = newResult;
			}
		}
		return result;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point size;
		if ((state & CANVAS) != 0) {
			if (layout != null) {
				if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
					changed |= (state & LAYOUT_CHANGED) != 0;
					state &= ~LAYOUT_CHANGED;
					size = layout.computeSize(this, wHint, hHint, changed);
				} else {
					size = new Point(wHint, hHint);
				}
			} else {
				size = minimumSize(wHint, hHint, changed);
			}
			if (size.x < 0) {
				size.x = DEFAULT_WIDTH;
			}
			if (size.y < 0) {
				size.y = DEFAULT_HEIGHT;
			}
			if (wHint != SWT.DEFAULT && wHint > 0) {
				size.x = wHint;
			}
			if (hHint != SWT.DEFAULT && hHint > 0) {
				size.y = hHint;
			}
			Rectangle trim = computeTrim(0, 0, size.x, size.y);
			return new Point(trim.width, trim.height);
		}
		return super.computeSize(wHint, hHint, changed);

	}

	@Override
	boolean isMirrored() {
		return (style & SWT.MIRRORED) != 0;
	}

	/**
	 * Copies a rectangular area of the receiver at the specified position using
	 * the gc.
	 * 
	 * @param gc
	 *            the gc where the rectangle is to be filled
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled
	 * @param height
	 *            the height of the rectangle to be filled
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	/* public */void copyArea(GC gc, int x, int y, int width, int height) {
		checkWidget();
		if (gc == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (gc.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		throw new UnsupportedOperationException("not yet implemented"); //$NON-NLS-1$
	}

	protected Composite findDeferredControl() {
		return layoutCount > 0 ? this : parent.findDeferredControl();
	}

	@Override
	Menu[] findMenus(Control control) {
		if (control == this) {
			return new Menu[0];
		}
		Menu result[] = super.findMenus(control);
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			Menu[] menuList = child.findMenus(control);
			if (menuList.length != 0) {
				Menu[] newResult = new Menu[result.length + menuList.length];
				System.arraycopy(result, 0, newResult, 0, result.length);
				System.arraycopy(menuList, 0, newResult, result.length, menuList.length);
				result = newResult;
			}
		}
		return result;
	}

	@Override
	void fixChildren(Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations,
			Menu[] menus) {
		super.fixChildren(newShell, oldShell, newDecorations, oldDecorations, menus);
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].fixChildren(newShell, oldShell, newDecorations, oldDecorations, menus);
		}
	}

	void fixTabList(Control control) {
		if (tabList == null) {
			return;
		}
		int count = 0;
		for (int i = 0; i < tabList.length; i++) {
			if (tabList[i] == control) {
				count++;
			}
		}
		if (count == 0) {
			return;
		}
		Control[] newList = null;
		int length = tabList.length - count;
		if (length != 0) {
			newList = new Control[length];
			int index = 0;
			for (int i = 0; i < tabList.length; i++) {
				if (tabList[i] != control) {
					newList[index++] = tabList[i];
				}
			}
		}
		tabList = newList;
	}

	/**
	 * Returns the receiver's background drawing mode. This will be one of the
	 * following constants defined in class <code>SWT</code>:
	 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
	 * <code>INHERTIT_FORCE</code>.
	 * 
	 * @return the background mode
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * 
	 * @since 3.2
	 */
	public int getBackgroundMode() {
		checkWidget();
		return backgroundMode;
	}

	/**
	 * Returns a (possibly empty) array containing the receiver's children.
	 * Children are returned in the order that they are drawn. The topmost
	 * control appears at the beginning of the array. Subsequent controls draw
	 * beneath this control and appear later in the array.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of children, so modifying the array will not affect the
	 * receiver.
	 * </p>
	 * 
	 * @return an array of children
	 * 
	 * @see Control#moveAbove
	 * @see Control#moveBelow
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Control[] getChildren() {
		checkWidget();
		return _getChildren();
	}

	Control[] _getChildren() {
		Collection<QObject> qtChildren = getQWidget().children();
		int count = qtChildren.size();
		if (count == 0) {
			return new Control[0];
		}
		LinkedList<Control> children = new LinkedList<Control>();
		for (QObject qObj : qtChildren) {
			Widget widget = display.findControl(qObj);
			if (widget != null && widget != this) {
				if (widget instanceof Control && !(widget instanceof Shell)) {
					children.addFirst((Control) widget);
				}
			}
		}
		return children.toArray(new Control[children.size()]);
	}

	int getChildrenCount() {
		return getQWidget().children().size();
	}

	/**
	 * Returns layout which is associated with the receiver, or null if one has
	 * not been set.
	 * 
	 * @return the receiver's layout or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Layout getLayout() {
		checkWidget();
		return layout;
	}

	/**
	 * Gets the (possibly empty) tabbing order for the control.
	 * 
	 * @return tabList the ordered list of controls representing the tab order
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setTabList
	 */
	public Control[] getTabList() {
		checkWidget();
		Control[] tabList = _getTabList();
		if (tabList == null) {
			int count = 0;
			Control[] list = _getChildren();
			for (int i = 0; i < list.length; i++) {
				if (list[i].isTabGroup()) {
					count++;
				}
			}
			tabList = new Control[count];
			int index = 0;
			for (int i = 0; i < list.length; i++) {
				if (list[i].isTabGroup()) {
					tabList[index++] = list[i];
				}
			}
		}
		return tabList;
	}

	boolean hooksKeys() {
		return hooks(SWT.KeyDown) || hooks(SWT.KeyUp);
	}

	/**
	 * Returns <code>true</code> if the receiver has deferred the performing of
	 * layout, and <code>false</code> otherwise.
	 * 
	 * @return the receiver's deferred layout state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setLayoutDeferred(boolean)
	 * @see #isLayoutDeferred()
	 * 
	 * @since 3.1
	 */
	public boolean getLayoutDeferred() {
		checkWidget();
		return layoutCount > 0;
	}

	/**
	 * Returns <code>true</code> if the receiver or any ancestor up to and
	 * including the receiver's nearest ancestor shell has deferred the
	 * performing of layouts. Otherwise, <code>false</code> is returned.
	 * 
	 * @return the receiver's deferred layout state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setLayoutDeferred(boolean)
	 * @see #getLayoutDeferred()
	 * 
	 * @since 3.1
	 */
	public boolean isLayoutDeferred() {
		checkWidget();
		return findDeferredControl() != null;
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em> (that
	 * is, set the size and location of) the receiver's children. If the
	 * receiver does not have a layout, do nothing.
	 * <p>
	 * This is equivalent to calling <code>layout(true)</code>.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is moved or resized
	 * such that an area in the parent is exposed, then the parent will paint.
	 * If no child is affected, the parent will not paint.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void layout() {
		checkWidget();
		layout(true);
	}

	@Override
	protected void updateLayout() {
		layout(true, true);
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em> (that
	 * is, set the size and location of) the receiver's children. If the
	 * argument is <code>true</code> the layout must not rely on any information
	 * it has cached about the immediate children. If it is <code>false</code>
	 * the layout may (potentially) optimize the work it is doing by assuming
	 * that none of the receiver's children has changed state since the last
	 * layout. If the receiver does not have a layout, do nothing.
	 * <p>
	 * If a child is resized as a result of a call to layout, the resize event
	 * will invoke the layout of the child. The layout will cascade down through
	 * all child widgets in the receiver's widget tree until a child is
	 * encountered that does not resize. Note that a layout due to a resize will
	 * not flush any cached information (same as <code>layout(false)</code>).
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is moved or resized
	 * such that an area in the parent is exposed, then the parent will paint.
	 * If no child is affected, the parent will not paint.
	 * </p>
	 * 
	 * @param changed
	 *            <code>true</code> if the layout must flush its caches, and
	 *            <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void layout(boolean changed) {
		checkWidget();
		if (layout == null) {
			return;
		}
		layout(changed, false);
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em> (that
	 * is, set the size and location of) the receiver's children. If the changed
	 * argument is <code>true</code> the layout must not rely on any information
	 * it has cached about its children. If it is <code>false</code> the layout
	 * may (potentially) optimize the work it is doing by assuming that none of
	 * the receiver's children has changed state since the last layout. If the
	 * all argument is <code>true</code> the layout will cascade down through
	 * all child widgets in the receiver's widget tree, regardless of whether
	 * the child has changed size. The changed argument is applied to all
	 * layouts. If the all argument is <code>false</code>, the layout will
	 * <em>not</em> cascade down through all child widgets in the receiver's
	 * widget tree. However, if a child is resized as a result of a call to
	 * layout, the resize event will invoke the layout of the child. Note that a
	 * layout due to a resize will not flush any cached information (same as
	 * <code>layout(false)</code>). </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is moved or resized
	 * such that an area in the parent is exposed, then the parent will paint.
	 * If no child is affected, the parent will not paint.
	 * </p>
	 * 
	 * @param changed
	 *            <code>true</code> if the layout must flush its caches, and
	 *            <code>false</code> otherwise
	 * @param all
	 *            <code>true</code> if all children in the receiver's widget
	 *            tree should be laid out, and <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void layout(boolean changed, boolean all) {
		checkWidget();
		if (layout == null && !all) {
			return;
		}
		markLayout(changed, all);
		updateLayout(all);
	}

	/**
	 * Forces a lay out (that is, sets the size and location) of all widgets
	 * that are in the parent hierarchy of the changed control up to and
	 * including the receiver. The layouts in the hierarchy must not rely on any
	 * information cached about the changed control or any of its ancestors. The
	 * layout may (potentially) optimize the work it is doing by assuming that
	 * none of the peers of the changed control have changed state since the
	 * last layout. If an ancestor does not have a layout, skip it.
	 * <p>
	 * Note: Layout is different from painting. If a child is moved or resized
	 * such that an area in the parent is exposed, then the parent will paint.
	 * If no child is affected, the parent will not paint.
	 * </p>
	 * 
	 * @param changed
	 *            a control that has had a state change which requires a
	 *            recalculation of its size
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the changed array is null
	 *                any of its controls are null or have been disposed</li>
	 *                <li>ERROR_INVALID_PARENT - if any control in changed is
	 *                not in the widget tree of the receiver</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void layout(Control[] changed) {
		checkWidget();
		if (changed == null) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < changed.length; i++) {
			Control control = changed[i];
			if (control == null) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			boolean ancestor = false;
			Composite composite = control.parent;
			while (composite != null) {
				ancestor = composite == this;
				if (ancestor) {
					break;
				}
				composite = composite.parent;
			}
			if (!ancestor) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		int updateCount = 0;
		Composite[] update = new Composite[16];
		for (int i = 0; i < changed.length; i++) {
			Control child = changed[i];
			Composite composite = child.parent;
			while (child != this) {
				if (composite.layout != null) {
					composite.state |= LAYOUT_NEEDED;
					if (!composite.layout.flushCache(child)) {
						composite.state |= LAYOUT_CHANGED;
					}
				}
				if (updateCount == update.length) {
					Composite[] newUpdate = new Composite[update.length + 16];
					System.arraycopy(update, 0, newUpdate, 0, update.length);
					update = newUpdate;
				}
				child = update[updateCount++] = composite;
				composite = child.parent;
			}
		}
		for (int i = updateCount - 1; i >= 0; i--) {
			update[i].updateLayout(false);
		}
	}

	@Override
	void markLayout(boolean changed, boolean all) {
		if (layout != null) {
			state |= LAYOUT_NEEDED;
			if (changed) {
				state |= LAYOUT_CHANGED;
			}
		}
		if (all) {
			Control[] children = _getChildren();
			for (int i = 0; i < children.length; i++) {
				children[i].markLayout(changed, all);
			}
		}
	}

	Point minimumSize(int wHint, int hHint, boolean changed) {
		Control[] children = _getChildren();
		int width = 0, height = 0;
		for (int i = 0; i < children.length; i++) {
			Rectangle rect = children[i].getBounds();
			width = Math.max(width, rect.x + rect.width);
			height = Math.max(height, rect.y + rect.height);
		}
		return new Point(width, height);
	}

	@Override
	void redrawChildren() {
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i]._redraw();
		}
	}

	@Override
	void releaseChildren(boolean destroy) {
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (child != null && !child.isDisposed()) {
				child.release(false);
			}
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		layout = null;
		tabList = null;
	}

	void removeControl(Control control) {
		control.getQMasterWidget().setParent(null);
		fixTabList(control);
		resizeChildren();
	}

	private void resizeChildren() {
		if (getQWidget() == null) {
			return;
		}
		for (QObject child : getQWidget().children()) {
			if (child.isWidgetType()) {
				((QWidget) child).updateGeometry();
			}
		}
	}

	/**
	 * Sets the background drawing mode to the argument which should be one of
	 * the following constants defined in class <code>SWT</code>:
	 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
	 * <code>INHERIT_FORCE</code>.
	 * 
	 * @param mode
	 *            the new background mode
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * 
	 * @since 3.2
	 */
	public void setBackgroundMode(int mode) {
		checkWidget();
		if (backgroundMode == mode) {
			return;
		}
		backgroundMode = mode;
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].updateBackgroundMode();
		}
	}

	@Override
	protected void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		if ((state & CANVAS) != 0) {
			state &= ~(RESIZE_OCCURRED | MOVE_OCCURRED);
			state |= RESIZE_DEFERRED | MOVE_DEFERRED;
		}
		super.setBounds(x, y, width, height, move, resize);
		if ((state & CANVAS) != 0) {
			boolean wasResized = (state & RESIZE_OCCURRED) != 0;
			state &= ~(RESIZE_DEFERRED | MOVE_DEFERRED);
			if (wasResized && layout != null) {
				markLayout(false, false);
				updateLayout(false);
			}
		}
	}

	@Override
	public boolean setFocus() {
		checkWidget();
		return setFocus(FocusReason.OtherFocusReason);
		//		Control[] children = _getChildren();
		//		for (int i = 0; i < children.length; i++) {
		//			Control child = children[i];
		//			if (child.setRadioFocus(false))
		//				return true;
		//		}
		//		for (int i = 0; i < children.length; i++) {
		//			Control child = children[i];
		//			if (child.setFocus())
		//				return true;
		//		}
		//		return super.setFocus();
	}

	@Override
	protected boolean setFocus(FocusReason focusReason) {
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (child.getVisible() && child.setFocus(focusReason)) {
				return true;
			}
		}
		return super.setFocus(focusReason);
	}

	/**
	 * Sets the layout which is associated with the receiver to be the argument
	 * which may be null.
	 * 
	 * @param layout
	 *            the receiver's new layout or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLayout(Layout layout) {
		checkWidget();
		this.layout = layout;
	}

	/**
	 * If the argument is <code>true</code>, causes subsequent layout operations
	 * in the receiver or any of its children to be ignored. No layout of any
	 * kind can occur in the receiver or any of its children until the flag is
	 * set to false. Layout operations that occurred while the flag was
	 * <code>true</code> are remembered and when the flag is set to
	 * <code>false</code>, the layout operations are performed in an optimized
	 * manner. Nested calls to this method are stacked.
	 * 
	 * @param defer
	 *            the new defer state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #layout(boolean)
	 * @see #layout(Control[])
	 * 
	 * @since 3.1
	 */
	public void setLayoutDeferred(boolean defer) {
		if (!defer) {
			if (--layoutCount == 0) {
				if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
					updateLayout(true);
				}
			}
		} else {
			layoutCount++;
		}
	}

	/**
	 * Sets the tabbing order for the specified controls to match the order that
	 * they occur in the argument list.
	 * 
	 * @param tabList
	 *            the ordered list of controls representing the tab order or
	 *            null
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is
	 *                null or has been disposed</li>
	 *                <li>ERROR_INVALID_PARENT - if widget in the tabList is not
	 *                in the same widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTabList(Control[] tabList) {
		checkWidget();
		if (tabList != null) {
			for (int i = 0; i < tabList.length; i++) {
				Control control = tabList[i];
				if (control == null) {
					error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (control.isDisposed()) {
					error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (control.parent != this) {
					error(SWT.ERROR_INVALID_PARENT);
				}
			}
			Control[] newList = new Control[tabList.length];
			System.arraycopy(tabList, 0, newList, 0, tabList.length);
			tabList = newList;
		}
		this.tabList = tabList;
	}

	@Override
	boolean setTabGroupFocus() {
		if (isTabItem()) {
			return setTabItemFocus();
		}
		boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
		if ((state & CANVAS) != 0) {
			takeFocus = hooksKeys();
			if ((style & SWT.EMBEDDED) != 0) {
				takeFocus = true;
			}
		}
		if (takeFocus && setTabItemFocus()) {
			return true;
		}
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (child.isTabItem() && child.setRadioFocus(true)) {
				return true;
			}
		}
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (child.isTabItem() && !child.isTabGroup() && child.setTabItemFocus()) {
				return true;
			}
		}
		return false;
	}

	@Override
	boolean translateMnemonic(Event event, Control control) {
		if (super.translateMnemonic(event, control)) {
			return true;
		}
		if (control != null) {
			Control[] children = _getChildren();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				if (child.translateMnemonic(event, control)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	void updateBackgroundColor() {
		//		if (noBackground()) {
		//			return;
		//		}
		super.updateBackgroundColor();
		for (Control child : _getChildren()) {
			if ((child.state & PARENT_BACKGROUND) != 0) {
				child.updateBackgroundColor();
			}
		}
	}

	@Override
	void updateBackgroundImage() {
		//		if (noBackground()) {
		//			return;
		//		}
		super.updateBackgroundImage();
		for (Control child : _getChildren()) {
			if ((child.state & PARENT_BACKGROUND) != 0) {
				child.updateBackgroundImage();
			}
		}
	}

	@Override
	void updateBackgroundMode() {
		super.updateBackgroundMode();
		for (Control child : _getChildren()) {
			child.updateBackgroundMode();
		}
	}

	@Override
	void updateFont(Font oldFont, Font newFont) {
		super.updateFont(oldFont, newFont);
		Control[] children = _getChildren();
		for (int i = 0; i < children.length; i++) {
			Control control = children[i];
			if (!control.isDisposed()) {
				control.updateFont(oldFont, newFont);
			}
		}
	}

	@Override
	void updateLayout(boolean all) {
		Composite parent = findDeferredControl();
		if (parent != null) {
			parent.state |= LAYOUT_CHILD;
			return;
		}
		if ((state & LAYOUT_NEEDED) != 0) {
			boolean changed = (state & LAYOUT_CHANGED) != 0;
			state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
			if (layout != null) {
				layout.layout(this, changed);
			}
			resizeChildren();
		}
		if (all) {
			state &= ~LAYOUT_CHILD;
			Control[] children = _getChildren();
			for (int i = 0; i < children.length; i++) {
				children[i].updateLayout(all);
			}
		}
	}

	@Override
	protected void _setVisible(QWidget widget, boolean visible) {
		findChildren();
		boolean oldVisibility = getQMasterWidget().isVisible();
		super._setVisible(widget, visible);
		if (getQMasterWidget().isVisible() != oldVisibility && layout != null) {
			markLayout(true, true);
			updateLayout(true);
		}
	}

	// TODO sma@2010-02-08 Links contain HTML escape sequences and therefore "&".
	// 		Removing it breaks the HTML. Can Links have mnemonics?
	private void findChildren() {
		for (Control child : getChildren()) {
			if (!(child instanceof Link) && child.getQWidget() instanceof QLabel
					&& ((QLabel) child.getQWidget()).text().contains("&")) { //$NON-NLS-1$
				handleMnemonic(child.getQWidget());
			} else if (child instanceof Composite) {
				((Composite) child).findChildren();
			}
		}
	}

	private void handleMnemonic(QWidget qwidget) {
		QLabel myLabel = (QLabel) qwidget;
		boolean foundBuddy = false;
		java.util.List<QObject> children = myLabel.parent().children();
		for (int i = 0; i < children.size() - 1; i++) {
			if (children.get(i).equals(myLabel)) {
				// search for a suitable buddy
				for (int k = i + 1; k < children.size(); k++) {
					QObject neighbour = children.get(k);
					if (neighbour != null && !(neighbour instanceof QLabel) && !(neighbour instanceof QScrollArea)) {
						myLabel.setBuddy((QWidget) neighbour);
						foundBuddy = true;
						break;
					}
				}
			}

		}
		/*
		 * If there is no buddy to get the mnemonic, the "&" is removed from the
		 * string. In SWT/Win32 the character will remain underlined without any
		 * function, but in SWT/Qt this is not possible.
		 */
		if (!foundBuddy) {
			myLabel.setText(myLabel.text().replace("&", "")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public boolean qtResizeEvent(QObject source, QResizeEvent resizeEvent) {
		if (isDisposed()) {
			return true;
		}
		if (isMirrored()) {
			int newWidth = resizeEvent.size().width();
			int oldWidth = resizeEvent.oldSize().width();
			final int dx = newWidth - oldWidth;
			if (dx != 0) {
				Control[] children = _getChildren();
				for (int i = 0; i < children.length; ++i) {
					Control child = children[i];
					if (!child.isDisposed()) {
						QWidget qtControl = child.getQMasterWidget();
						QPoint pos = qtControl.pos();
						qtControl.move(pos.x() + dx, pos.y());
					}
				}
			}
		}
		super.qtResizeEvent(source, resizeEvent);
		if (layout != null) {
			markLayout(false, false);
			updateLayout(false);
		}
		return false;
	}

	@Override
	public boolean qtMouseButtonPressEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			boolean ret = super.qtMouseButtonPressEvent(source, mouseEvent);
			if ((this.state & CANVAS) != 0) {
				if ((style & SWT.NO_FOCUS) == 0 && hooksKeys()) {
					Control[] children = getChildren();
					if (children == null || children.length == 0) {
						setFocus(FocusReason.OtherFocusReason);
					}
				}
			}
			return ret;
		}
		return false;
	}

	@Override
	public boolean qtMouseEnterEvent(Object source) {
		if (source == getQMasterWidget()) {
			sendEvent(SWT.MouseEnter);
		}
		return false;
	}

	@Override
	public boolean qtMouseLeaveEvent(Object source) {
		if (source == getQMasterWidget()) {
			sendEvent(SWT.MouseExit);
		}
		return false;
	}

}
