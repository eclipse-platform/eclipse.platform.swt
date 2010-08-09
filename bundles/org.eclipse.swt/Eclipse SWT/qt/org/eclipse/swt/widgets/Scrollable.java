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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QAbstractScrollArea;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QSizePolicy.Policy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;
import org.eclipse.swt.internal.qt.StylableScrollArea;

/**
 * This class is the abstract superclass of all classes which represent controls
 * that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public abstract class Scrollable extends Control {
	private ScrollBar horizontalBar;
	private ScrollBar verticalBar;
	private QWidget masterWidget;
	private QWidget contentWidget;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Scrollable() {
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
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#H_SCROLL
	 * @see SWT#V_SCROLL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Scrollable(Composite parent, int style) {
		super(parent, style);
		createScrollBars(style);
	}

	@Override
	QWidget createQWidget(int style) {
		QScrollArea scrollArea = new StylableScrollArea();
		scrollArea.setFrameShape(Shape.NoFrame);
		scrollArea.setFocusPolicy(FocusPolicy.TabFocus);
		scrollArea.setWidgetResizable(!isScrollingEnabled());
		setQMasterWidget(scrollArea);

		contentWidget = new QWidget();
		contentWidget.setSizePolicy(Policy.MinimumExpanding, Policy.MinimumExpanding);
		contentWidget.setProperty("widgetType", "scrollareaContent"); //$NON-NLS-1$//$NON-NLS-2$
		contentWidget.setContentsMargins(0, 0, 0, 0);
		contentWidget.resize(0, 0);

		scrollArea.setWidget(contentWidget);
		scrollArea.resize(0, 0);

		return contentWidget;
	}

	@Override
	boolean isQScrollArea() {
		return getQMasterWidget() instanceof QAbstractScrollArea;
	}

	protected void setQMasterWidget(QWidget masterWidget) {
		this.masterWidget = masterWidget;
	}

	@Override
	protected QWidget getQMasterWidget() {
		if (masterWidget != null) {
			return masterWidget;
		}
		return super.getQMasterWidget();
	}

	@Override
	protected void updateFocusPolicy() {
		super.updateFocusPolicy();
		if ((style & SWT.NO_FOCUS) != 0) {
			if (isQScrollArea()) {
				getQMasterWidget().setFocusPolicy(FocusPolicy.NoFocus);
			}
		}
	}

	QAbstractScrollArea getQScrollArea() {
		return (QAbstractScrollArea) getQMasterWidget();
	}

	private StylableScrollArea getStylableScrollArea() {
		return (StylableScrollArea) getQMasterWidget();
	}

	private void createScrollBars(int style) {
		if (isQScrollArea()) {
			if ((style & SWT.H_SCROLL) != 0) {
				setHBarPolicy(true);
				if (horizontalBar == null) {
					horizontalBar = createScrollBar(SWT.HORIZONTAL);
				}
			} else {
				setHBarPolicy(false);
			}

			if ((style & SWT.V_SCROLL) != 0) {
				setVBarPolicy(true);
				if (verticalBar == null) {
					verticalBar = createScrollBar(SWT.VERTICAL);
				}
			} else {
				setVBarPolicy(false);
			}
		}
	}

	protected boolean isScrollingEnabled() {
		return (style & SWT.V_SCROLL) != 0 || (style & SWT.H_SCROLL) != 0;
	}

	protected void setHBarPolicy(boolean enableScrollbar) {
		getQScrollArea().setHorizontalScrollBarPolicy(
				enableScrollbar ? ScrollBarPolicy.ScrollBarAsNeeded : ScrollBarPolicy.ScrollBarAlwaysOff);
	}

	protected void setVBarPolicy(boolean enableScrollbar) {
		getQScrollArea().setVerticalScrollBarPolicy(
				enableScrollbar ? ScrollBarPolicy.ScrollBarAsNeeded : ScrollBarPolicy.ScrollBarAlwaysOff);
	}

	void destroyScrollBar(int type) {
		if ((type & SWT.HORIZONTAL) != 0) {
			style &= ~SWT.H_SCROLL;
			horizontalBar.dispose();
			horizontalBar = null;
		}
		if ((type & SWT.VERTICAL) != 0) {
			style &= ~SWT.V_SCROLL;
			verticalBar.dispose();
			verticalBar = null;
		}
	}

	@Override
	protected void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		super.setBounds(x, y, width, height, move, resize);
		if (resize && contentWidget != null && isScrollingEnabled()) { //
			Point prefSize = computeSize(SWT.DEFAULT, SWT.DEFAULT);
			int newWidth = Math.max(width - getVerticalBarWidth(), prefSize.x);
			int newHeight = Math.max(height - getHorizontalBarHeight(), prefSize.y);
			QSize sizeHint = contentWidget.sizeHint();
			if (sizeHint.isValid()) {
				newWidth = Math.max(newWidth, sizeHint.width());
				newHeight = Math.max(newHeight, sizeHint.height());
			}
			contentWidget.resize(newWidth, newHeight);
		}
	}

	@Override
	public Object getData(String key) {
		checkWidget();
		if (key == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		if ("__Qt_gradientStart".equals(key)) { //$NON-NLS-1$
			return Color.qt_new(display, getStylableScrollArea().getGradientStart());
		} else if ("__Qt_gradientEnd".equals(key)) { //$NON-NLS-1$
			return Color.qt_new(display, getStylableScrollArea().getGradientEnd());
		} else if ("__Qt_text".equals(key)) { //$NON-NLS-1$
			return Color.qt_new(display, getStylableScrollArea().getText());
		} else if ("__Qt_border".equals(key)) { //$NON-NLS-1$
			return Color.qt_new(display, getStylableScrollArea().getBorder());
		}

		return super.getData(key);
	}

	/**
	 * Given a desired <em>client area</em> for the receiver (as described by
	 * the arguments), returns the bounding rectangle which would be required to
	 * produce that client area.
	 * <p>
	 * In other words, it returns a rectangle such that, if the receiver's
	 * bounds were set to that rectangle, the area of the receiver which is
	 * capable of displaying data (that is, not covered by the "trimmings")
	 * would be the rectangle described by the arguments (relative to the
	 * receiver's parent).
	 * </p>
	 * 
	 * @param x
	 *            the desired x coordinate of the client area
	 * @param y
	 *            the desired y coordinate of the client area
	 * @param width
	 *            the desired width of the client area
	 * @param height
	 *            the desired height of the client area
	 * @return the required bounds to produce the given client area
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getClientArea
	 */
	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();
		int border = getBorderWidth();
		int xn = x - border;
		int yn = y - border;
		int widthNew = width + 2 * border;
		int heightNew = height + 2 * border;

		widthNew += getVerticalBarWidth();

		heightNew += getHorizontalBarHeight();

		return new Rectangle(xn, yn, widthNew, heightNew);
	}

	private int getVerticalBarWidth() {
		ScrollBar bar = getVerticalBar();
		if (bar != null) {
			return bar.getSize().x;
		}
		return 0;
	}

	private int getHorizontalBarHeight() {
		ScrollBar bar = getHorizontalBar();
		if (bar != null) {
			return bar.getSize().y;
		}
		return 0;
	}

	ScrollBar createScrollBar(int type) {
		ScrollBar bar = new ScrollBar(this, type);
		if ((state & CANVAS) != 0) {
			bar.setMaximum(100);
			bar.setThumb(10);
		}
		return bar;
	}

	/**
	 * Returns a rectangle which describes the area of the receiver which is
	 * capable of displaying data (that is, not covered by the "trimmings").
	 * 
	 * @return the client area
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #computeTrim
	 */
	public Rectangle getClientArea() {
		checkWidget();
		if (!isVisible()) {
			updateQLayouts();
		}

		Rectangle clientArea = QtSWTConverter.convert(getQWidget().geometry());
		if (clientArea.width < 0) {
			clientArea.width = DEFAULT_WIDTH;
		}
		if (clientArea.height < 0) {
			clientArea.height = DEFAULT_HEIGHT;
		}

		return clientArea;
	}

	void updateQLayouts() {
		if (parent != null) {
			parent.updateQLayouts();
		}
		updateLayoutOfQWidget();
	}

	void updateLayoutOfQWidget() {
		QLayout layout = getQWidget().layout();
		if (layout != null) {
			layout.activate();
			layout.update();
		}
	}

	/**
	 * Returns the receiver's horizontal scroll bar if it has one, and null if
	 * it does not.
	 * 
	 * @return the horizontal scroll bar (or null)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public ScrollBar getHorizontalBar() {
		checkWidget();
		return horizontalBar;
	}

	/**
	 * Returns the receiver's vertical scroll bar if it has one, and null if it
	 * does not.
	 * 
	 * @return the vertical scroll bar (or null)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public ScrollBar getVerticalBar() {
		checkWidget();
		return verticalBar;
	}

	@Override
	void releaseQWidget() {
		masterWidget = null;
		contentWidget = null;
		super.releaseQWidget();
	}

	void releaseBar(ScrollBar bar) {
		if (horizontalBar == bar) {
			horizontalBar = null;
		}
		if (verticalBar == bar) {
			verticalBar = null;
		}
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (horizontalBar != null) {
			horizontalBar.release(false);
			horizontalBar = null;
		}
		if (verticalBar != null) {
			verticalBar.release(false);
			verticalBar = null;
		}
		super.releaseChildren(destroy);
	}
}
