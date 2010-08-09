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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.CursorShape;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame.Shadow;
import com.trolltech.qt.gui.QFrame.Shape;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;

/**
 * Instances of the receiver represent a selectable user interface object that
 * allows the user to drag a rubber banded outline of the sash within the parent
 * control.sw
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL, SMOOTH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#sash">Sash snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Sash extends Control {
	private boolean dragging;
	private int startX, startY, lastX, lastY;

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
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Sash(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state |= THEME_BACKGROUND;
		return new QFrame();
	}

	protected QFrame getQFrame() {
		return (QFrame) getQWidget();
	}

	@Override
	protected void checkAndUpdateBorder() {
		if ((style & SWT.BORDER) != 0) {
			getQFrame().setFrameShape(Shape.Panel);
			getQFrame().setFrameShadow(Shadow.Sunken);
		} else {
			getQFrame().setFrameShape(Shape.NoFrame);
		}
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the x, y, width, and height
	 * fields of the event object are valid. If the receiver is being dragged,
	 * the event object detail field contains the value <code>SWT.DRAG</code>.
	 * <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the control is
	 *            selected by the user
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int border = getBorderWidth();
		int width = border * 2, height = border * 2;
		if ((style & SWT.HORIZONTAL) != 0) {
			width += DEFAULT_WIDTH;
			height += 3;
		} else {
			width += 3;
			height += DEFAULT_HEIGHT;
		}
		if (wHint != SWT.DEFAULT) {
			width = wHint + border * 2;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint + border * 2;
		}
		return new Point(width, height);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	@Override
	public boolean qtMouseMoveEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			return handleMouseMove(mouseEvent);
		}
		return super.qtMouseMoveEvent(source, mouseEvent);
	}

	@Override
	public boolean qtMouseButtonPressEvent(QObject source, QMouseEvent mouseEvent) {
		if (super.qtMouseButtonPressEvent(source, mouseEvent)) {
			return true; // cancel;
		}
		return handleMousePress(mouseEvent);
	}

	@Override
	public boolean qtMouseButtonReleaseEvent(QObject source, QMouseEvent mouseEvent) {
		if (super.qtMouseButtonReleaseEvent(source, mouseEvent)) {
			return true; // cancel
		}
		return handleMouseRelease(mouseEvent);
	}

	private boolean handleMousePress(QMouseEvent mouseEvent) {
		if (!isLeftMouseButton(mouseEvent)) {
			return false;
		}
		QPoint pt = mouseEvent.globalPos();
		QRect rect = getQWidget().frameGeometry();
		startX = pt.x() - rect.left();
		startY = pt.y() - rect.top();
		lastX = rect.left();
		lastY = rect.top();
		int width = rect.width();
		int height = rect.height();

		Event event = new Event();
		event.x = lastX;
		event.y = lastY;
		event.width = width;
		event.height = height;
		if ((style & SWT.SMOOTH) == 0) {
			event.detail = SWT.DRAG;
		}
		sendEvent(SWT.Selection, event);
		if (isDisposed()) {
			return true;
		}

		/* Draw the banding rectangle */
		if (event.doit) {
			dragging = true;
			lastX = event.x;
			lastY = event.y;
			menuShell().bringToTop();
			if (isDisposed()) {
				return true;
			}
			if ((style & SWT.SMOOTH) != 0) {
				setBounds(event.x, event.y, width, height, true, true);
				// widget could be disposed at this point
			}
		}
		return false;
	}

	private boolean isLeftMouseButton(QMouseEvent mouseEvent) {
		return MouseButton.LeftButton.equals(mouseEvent.button()) || mouseEvent.buttons().isSet(MouseButton.LeftButton);
	}

	private boolean handleMouseRelease(QMouseEvent mouseEvent) {
		if (!(dragging && isLeftMouseButton(mouseEvent))) {
			return false;
		}

		dragging = false;
		QRect rect = getQWidget().frameGeometry();
		int width = rect.width();
		int height = rect.height();

		Event event = new Event();
		event.x = lastX;
		event.y = lastY;
		event.width = width;
		event.height = height;
		sendEvent(SWT.Selection, event);
		if (isDisposed()) {
			return true;
		}
		if (event.doit) {
			if ((style & SWT.SMOOTH) != 0) {
				setBounds(event.x, event.y, width, height, true, true);
				// widget could be disposed at this point
			}
		}
		return false;
	}

	private boolean handleMouseMove(QMouseEvent mouseEvent) {
		if (!(dragging && isLeftMouseButton(mouseEvent))) {
			return false;
		}

		QPoint pt = mouseEvent.globalPos();
		QRect rect = getQWidget().frameGeometry();
		int width = rect.width();
		int height = rect.height();
		QRect clientRect = getQWidget().parentWidget().frameGeometry();

		int newX = lastX, newY = lastY;
		if ((style & SWT.VERTICAL) != 0) {
			int clientWidth = clientRect.width();
			newX = Math.min(Math.max(0, pt.x() - startX), clientWidth - width);
		} else {
			int clientHeight = clientRect.height();
			newY = Math.min(Math.max(0, pt.y() - startY), clientHeight - height);
		}
		if (newX == lastX && newY == lastY) {
			return false;
		}

		/* The event must be sent because doit flag is used */
		Event event = new Event();
		event.x = newX;
		event.y = newY;
		event.width = width;
		event.height = height;
		if ((style & SWT.SMOOTH) == 0) {
			event.detail = SWT.DRAG;
		}
		sendEvent(SWT.Selection, event);
		if (isDisposed()) {
			return true;
		}

		if (event.doit) {
			lastX = event.x;
			lastY = event.y;
		}
		if ((style & SWT.SMOOTH) != 0) {
			setBounds(lastX, lastY, width, height, true, true);
			// widget could be disposed at this point
		}
		return false;
	}

	@Override
	public boolean qtMouseEnterEvent(Object source) {
		if (source == getQWidget()) {
			if ((style & SWT.HORIZONTAL) != 0) {
				getQWidget().setCursor(new QCursor(CursorShape.SizeVerCursor));
			} else {
				getQWidget().setCursor(new QCursor(CursorShape.SizeHorCursor));
			}
		}
		return super.qtMouseEnterEvent(source);
	}

	@Override
	public boolean qtMouseLeaveEvent(Object source) {
		if (source == getQWidget()) {
			getQWidget().setCursor(new QCursor(CursorShape.ArrowCursor));
		}
		return super.qtMouseLeaveEvent(source);
	}
}
