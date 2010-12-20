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

import java.util.List;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QEventLoop.ProcessEventsFlag;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.FocusReason;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.LayoutDirection;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.MouseButtons;
import com.trolltech.qt.gui.QAbstractScrollArea;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QDesktopWidget;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPaintDeviceInterface;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPicture;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame.Shadow;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QPalette.ColorRole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.internal.qt.DragNDropListener;
import org.eclipse.swt.internal.qt.KeyUtil;
import org.eclipse.swt.internal.qt.QtSWTConverter;
import org.eclipse.swt.widgets.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect,
 * MouseDoubleClick, MouseDown, MouseEnter, MouseExit, MouseHover, MouseUp,
 * MouseMove, Move, Paint, Resize, Traverse</dd>
 * </dl>
 * </p>
 * <p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#control">Control
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public abstract class Control extends Widget implements Drawable {
	/**
	 * the handle to the OS resource (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API.
	 * It is marked public only so that it can be shared within the packages
	 * provided by SWT. It is not available on all platforms and should never be
	 * accessed from application code.
	 * </p>
	 */
	public int /* long */handle;
	Composite parent;
	protected Cursor cursor;
	private Menu menu;
	private Object layoutData;
	private Accessible accessible;
	protected Region region;
	private Font font;
	protected Color foreground;
	protected Color background;
	protected Image backgroundImage;
	private QPicture temporaryGC;
	protected boolean isOngoingPaintEvent = false;
	private boolean enabled = true;
	private QPoint dragStartPos;
	private DragNDropListener dndListener;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Control() {
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
	 * @see SWT#BORDER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Control(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		createWidget(parent, style);
	}

	protected void createWidget(Composite parent, int style) {
		state |= DRAG_DETECT;
		checkAndUpdateOrientation(parent);
		QWidget qWidget = createQWidget(style);
		if (qWidget == null) {
			error(SWT.ERROR_UNSPECIFIED);
		}
		setQWidget(qWidget);
		setParent();
		fixZOrder();
		checkAndUpdateBackground();
		connectSignals();
		registerQWidget();
		setupQWidget();
		updateLayoutDirection();
		setupStyleSheet();
		checkAndUpdateMirrored();
		checkAndUpdateBorder();
		updateFocusPolicy();
		updateBackground();
		updateSizeAndVisibility();
	}

	abstract QWidget createQWidget(int style);

	protected void setParent() {
		if (parent != null) {
			parent.addQChild(this);
		}
	}

	protected void fixZOrder() {
		if (parent != null && !(parent instanceof TabFolder)) {
			getQMasterWidget().lower();
		}
	}

	/**
	 * Hook that guarantees that getQWidget() != null. Called immediately after
	 * {@link createQtControl()}.
	 */
	protected void setupQWidget() {
		//nothing
	}

	protected void updateSizeAndVisibility() {
		if (parent != null && parent.getQWidget().isVisible() && !(parent instanceof Shell) && !(this instanceof Shell)) {
			getQMasterWidget().setVisible(true);
		}
	}

	protected void connectSignals() {
		// nothing by default
	}

	boolean isQScrollArea() {
		return getQWidget() instanceof QAbstractScrollArea;
	}

	protected void setupStyleSheet() {
		String styleSheet = getShell() != null ? getShell().getQMasterWidget().styleSheet() : null;
		if (styleSheet == null) {
			styleSheet = getDisplay().getStyleSheet();
			if (styleSheet != null) {
				setStyleSheet(styleSheet);
			}
		}
	}

	protected void updateLayoutDirection() {
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			getQWidget().setLayoutDirection(LayoutDirection.RightToLeft);
		} else {
			getQWidget().setLayoutDirection(LayoutDirection.LeftToRight);
		}
	}

	protected void updateFocusPolicy() {
		if ((style & SWT.NO_FOCUS) != 0) {
			getQMasterWidget().setFocusPolicy(FocusPolicy.NoFocus);
		}
	}

	void registerQWidget() {
		display.addControl(getQWidget(), this);
		if (getQMasterWidget() != null && getQMasterWidget() != getQWidget()) {
			display.addControl(getQMasterWidget(), this);
		}
	}

	void deregisterQWidget() {
		display.removeControl(getQWidget());
		if (getQMasterWidget() != null && getQMasterWidget() != getQWidget()) {
			display.removeControl(getQMasterWidget());
		}
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		parent = null;
	}

	@Override
	void releaseParent() {
		parent.removeControl(this);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();

		deregisterQWidget();

		if (menu != null && !menu.isDisposed()) {
			menu.dispose();
		}
		temporaryGC = null;
		backgroundImage = null;
		menu = null;
		cursor = null;
		layoutData = null;
		if (accessible != null) {
			// TODO
			//accessible.internal_dispose_Accessible();
		}
		accessible = null;
		region = null;
		font = null;
	}

	protected void checkAndUpdateBorder() {
		checkAndUpdateBorder(getQMasterWidget());
		if (getBorderWidth() == 0) {
			style &= ~SWT.BORDER;
		}
	}

	protected void checkAndUpdateBorder(QWidget control) {
		if (control != null && control instanceof QFrame) {
			QFrame frame = (QFrame) control;
			if ((style & SWT.BORDER) != 0) {
				frame.setFrameShape(Shape.StyledPanel);
				frame.setFrameShadow(Shadow.Sunken);
				frame.setLineWidth(1);
			} else {
				frame.setFrameShape(Shape.NoFrame);
				frame.setLineWidth(0);
			}
		}

	}

	/* used by CoolBar */
	boolean drawGripper(int x, int y, int width, int height, boolean vertical) {
		return false;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is moved or resized, by sending it one of the messages
	 * defined in the <code>ControlListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see ControlListener
	 * @see #removeControlListener
	 */
	public void addControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Resize, typedListener);
		addListener(SWT.Move, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when a drag gesture occurs, by sending it one of the messages defined in
	 * the <code>DragDetectListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see DragDetectListener
	 * @see #removeDragDetectListener
	 * 
	 * @since 3.3
	 */
	public void addDragDetectListener(DragDetectListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.DragDetect, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control gains or loses focus, by sending it one of the messages
	 * defined in the <code>FocusListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see FocusListener
	 * @see #removeFocusListener
	 */
	public void addFocusListener(FocusListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.FocusIn, typedListener);
		addListener(SWT.FocusOut, typedListener);
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when gesture events are generated for the control.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see GestureListener
	 * @see #addGestureListener
	 */
	public void addGestureListener (GestureListener listener) {
		checkWidget();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener (listener);
		addListener (SWT.Gesture, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when help events are generated for the control, by sending it one of the
	 * messages defined in the <code>HelpListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see HelpListener
	 * @see #removeHelpListener
	 */
	public void addHelpListener(HelpListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Help, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when keys are pressed and released on the system keyboard, by sending it
	 * one of the messages defined in the <code>KeyListener</code> interface.
	 * <p>
	 * When a key listener is added to a control, the control will take part in
	 * widget traversal. By default, all traversal keys (such as the tab key and
	 * so on) are delivered to the control. In order for a control to take part
	 * in traversal, it should listen for traversal events. Otherwise, the user
	 * can traverse into a control but not out. Note that native controls such
	 * as table and tree implement key traversal in the operating system. It is
	 * not necessary to add traversal listeners for these controls, unless you
	 * want to override the default traversal.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see KeyListener
	 * @see #removeKeyListener
	 */
	public void addKeyListener(KeyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.KeyUp, typedListener);
		addListener(SWT.KeyDown, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the platform-specific context menu trigger has occurred, by sending
	 * it one of the messages defined in the <code>MenuDetectListener</code>
	 * interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see MenuDetectListener
	 * @see #removeMenuDetectListener
	 * 
	 * @since 3.3
	 */
	public void addMenuDetectListener(MenuDetectListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.MenuDetect, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when mouse buttons are pressed and released, by sending it one of the
	 * messages defined in the <code>MouseListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see MouseListener
	 * @see #removeMouseListener
	 */
	public void addMouseListener(MouseListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.MouseDown, typedListener);
		addListener(SWT.MouseUp, typedListener);
		addListener(SWT.MouseDoubleClick, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the mouse passes or hovers over controls, by sending it one of the
	 * messages defined in the <code>MouseTrackListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see MouseTrackListener
	 * @see #removeMouseTrackListener
	 */
	public void addMouseTrackListener(MouseTrackListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.MouseEnter, typedListener);
		addListener(SWT.MouseExit, typedListener);
		addListener(SWT.MouseHover, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the mouse moves, by sending it one of the messages defined in the
	 * <code>MouseMoveListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see MouseMoveListener
	 * @see #removeMouseMoveListener
	 */
	public void addMouseMoveListener(MouseMoveListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.MouseMove, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the mouse wheel is scrolled, by sending it one of the messages
	 * defined in the <code>MouseWheelListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see MouseWheelListener
	 * @see #removeMouseWheelListener
	 * 
	 * @since 3.3
	 */
	public void addMouseWheelListener(MouseWheelListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.MouseWheel, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver needs to be painted, by sending it one of the messages
	 * defined in the <code>PaintListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see PaintListener
	 * @see #removePaintListener
	 */
	public void addPaintListener(PaintListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Paint, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when traversal events occur, by sending it one of the messages defined in
	 * the <code>TraverseListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
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
	 * @see TraverseListener
	 * @see #removeTraverseListener
	 */
	public void addTraverseListener(TraverseListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Traverse, typedListener);
	}

	protected void checkBuffered() {
		style &= ~SWT.DOUBLE_BUFFERED;
	}

	protected void checkAndUpdateMirrored() {
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			style |= SWT.MIRRORED;
		}
	}

	/**
	 * Returns the preferred size of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best
	 * be displayed at. The width hint and height hint arguments allow the
	 * caller to ask a control questions such as "Given a particular width, how
	 * high does the control need to be to show all of the contents?" To
	 * indicate that the caller does not wish to constrain a particular
	 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * 
	 * @param wHint
	 *            the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint
	 *            the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the control
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	/**
	 * Returns the preferred size of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best
	 * be displayed at. The width hint and height hint arguments allow the
	 * caller to ask a control questions such as "Given a particular width, how
	 * high does the control need to be to show all of the contents?" To
	 * indicate that the caller does not wish to constrain a particular
	 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the
	 * receiver's <em>contents</em> have changed, therefore any caches that a
	 * layout manager containing the control may have been keeping need to be
	 * flushed. When the control is resized, the changed flag will be
	 * <code>false</code>, so layout manager caches can be retained.
	 * </p>
	 * 
	 * @param wHint
	 *            the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint
	 *            the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed
	 *            <code>true</code> if the control's contents have changed, and
	 *            <code>false</code> otherwise
	 * @return the preferred size of the control.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		if (changed) {
			getQMasterWidget().updateGeometry();
		}
		Point res = QtSWTConverter.convert(getQMasterWidget().sizeHint());
		if (res.x < 0) {
			res.x = DEFAULT_WIDTH;
		}
		if (res.y < 0) {
			res.y = DEFAULT_HEIGHT;
		}

		int border = getBorderWidth();
		if (wHint != SWT.DEFAULT) {
			res.x = wHint + 2 * border;
		}
		if (hHint != SWT.DEFAULT) {
			res.y = hHint + 2 * border;
		}

		return res;
	}

	Control computeTabGroup() {
		if (isTabGroup()) {
			return this;
		}
		return parent.computeTabGroup();
	}

	Control computeTabRoot() {
		Control[] tabList = parent._getTabList();
		if (tabList != null) {
			int index = 0;
			while (index < tabList.length) {
				if (tabList[index] == this) {
					break;
				}
				index++;
			}
			if (index == tabList.length) {
				if (isTabGroup()) {
					return this;
				}
			}
		}
		return parent.computeTabRoot();
	}

	Control[] computeTabList() {
		if (isTabGroup()) {
			if (getVisible() && getEnabled()) {
				return new Control[] { this };
			}
		}
		return new Control[0];
	}

	Font defaultFont() {
		return display.getSystemFont();
	}

	/**
	 * Detects a drag and drop gesture. This method is used to detect a drag
	 * gesture when called from within a mouse down listener.
	 * 
	 * <p>
	 * By default, a drag is detected when the gesture occurs anywhere within
	 * the client area of a control. Some controls, such as tables and trees,
	 * override this behavior. In addition to the operating system specific drag
	 * gesture, they require the mouse to be inside an item. Custom widget
	 * writers can use <code>setDragDetect</code> to disable the default
	 * detection, listen for mouse down, and then call <code>dragDetect()</code>
	 * from within the listener to conditionally detect a drag.
	 * </p>
	 * 
	 * @param event
	 *            the mouse down event
	 * 
	 * @return <code>true</code> if the gesture occurred, and <code>false</code>
	 *         otherwise.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT when the event is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 * 
	 * @see #getDragDetect
	 * @see #setDragDetect
	 * 
	 * @since 3.3
	 */
	public boolean dragDetect(Event event) {
		checkWidget();
		if (event == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return dragDetect(event.button, event.count, event.stateMask, event.x, event.y);
	}

	/**
	 * Detects a drag and drop gesture. This method is used to detect a drag
	 * gesture when called from within a mouse down listener.
	 * 
	 * <p>
	 * By default, a drag is detected when the gesture occurs anywhere within
	 * the client area of a control. Some controls, such as tables and trees,
	 * override this behavior. In addition to the operating system specific drag
	 * gesture, they require the mouse to be inside an item. Custom widget
	 * writers can use <code>setDragDetect</code> to disable the default
	 * detection, listen for mouse down, and then call <code>dragDetect()</code>
	 * from within the listener to conditionally detect a drag.
	 * </p>
	 * 
	 * @param event
	 *            the mouse down event
	 * 
	 * @return <code>true</code> if the gesture occurred, and <code>false</code>
	 *         otherwise.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT when the event is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 * 
	 * @see #getDragDetect
	 * @see #setDragDetect
	 * 
	 * @since 3.3
	 */
	public boolean dragDetect(MouseEvent event) {
		checkWidget();
		if (event == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return dragDetect(event.button, event.count, event.stateMask, event.x, event.y);
	}

	boolean dragDetect(int button, int count, int stateMask, int x, int y) {
		return false;
		// TODO
	}

	Cursor findCursor() {
		if (cursor != null) {
			return cursor;
		}
		return parent.findCursor();
	}

	Control findImageControl() {
		Control control = findBackgroundControl();
		return control != null && control.backgroundImage != null ? control : null;
	}

	Control findThemeControl() {
		return background == null && backgroundImage == null ? parent.findThemeControl() : null;
	}

	Menu[] findMenus(Control control) {
		if (menu != null && this != control) {
			return new Menu[] { menu };
		}
		return new Menu[0];
	}

	protected char findMnemonic(String string) {
		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return string.charAt(index);
			}
			index++;
		} while (index < length);
		return '\0';
	}

	void fixChildren(Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations,
			Menu[] menus) {
		oldShell.fixShell(newShell, this);
		oldDecorations.fixDecorations(newDecorations, this, menus);
	}

	private void fixFocus(Control focusControl) {
		Shell shell = getShell();
		Control control = this;
		while (control != shell && (control = control.parent) != null) {
			if (control.setFixedFocus()) {
				return;
			}
		}

		shell.setSavedFocus(focusControl);

		QDesktopWidget desktopWidget = QApplication.desktop();
		if (desktopWidget != null) {
			desktopWidget.setFocus(FocusReason.OtherFocusReason);
		}
	}

	/**
	 * Forces the receiver to have the <em>keyboard focus</em>, causing all
	 * keyboard events to be delivered to it.
	 * 
	 * @return <code>true</code> if the control got focus, and
	 *         <code>false</code> if it was unable to.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setFocus
	 */
	public boolean forceFocus() {
		checkWidget();
		return forceFocus(FocusReason.OtherFocusReason);
	}

	protected boolean forceFocus(FocusReason focusReason) {
		if (display.focusEvent == SWT.FocusOut) {
			return false;
		}
		Decorations shell = menuShell();
		shell.setSavedFocus(this);
		if (!isEnabled() || !isVisible() || !isActive()) {
			return false;
		}
		if (isFocusControl()) {
			return true;
		}
		shell.setSavedFocus(null);
		getQWidget().setFocus(focusReason);
		if (isDisposed()) {
			return false;
		}
		shell.setSavedFocus(this);
		return _isFocusControl();
	}

	/**
	 * Returns the accessible object for the receiver. If this is the first time
	 * this object is requested, then the object is created and returned.
	 * 
	 * @return the accessible object
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Accessible#addAccessibleListener
	 * @see Accessible#addAccessibleControlListener
	 * 
	 * @since 2.0
	 */
	public Accessible getAccessible() {
		checkWidget();
		if (accessible == null) {
			accessible = new_Accessible(this);
		}
		return accessible;
	}

	/**
	 * Returns the receiver's background color.
	 * 
	 * @return the background color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getBackground() {
		checkWidget();
		return _getBackgroundColor();
	}

	/**
	 * Returns the receiver's background image.
	 * 
	 * @return the background image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.2
	 */
	public Image getBackgroundImage() {
		checkWidget();
		Control control = findBackgroundControl();
		if (control == null) {
			control = this;
		}
		return control.backgroundImage;
	}

	Color _getBackgroundColor() {
		Control control = findBackgroundControl();
		if (control == null) {
			control = this;
		}
		if (control.background != null) {
			return control.background;
		}
		return getDefaultBackgroundColor();
	}

	/**
	 * Returns the receiver's border width.
	 * 
	 * @return the border width
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getBorderWidth() {
		checkWidget();
		if (getQMasterWidget() instanceof QFrame) {
			return ((QFrame) getQMasterWidget()).frameWidth();
		}
		return 0;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent (or its display if its parent is null), unless the receiver
	 * is a shell. In this case, the location is relative to the display.
	 * 
	 * @return the receiver's bounding rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Rectangle getBounds() {
		checkWidget();
		return QtSWTConverter.convert(getQMasterWidget().frameGeometry());
	}

	/**
	 * Returns the receiver's cursor, or null if it has not been set.
	 * <p>
	 * When the mouse pointer passes over a control its appearance is changed to
	 * match the control's cursor.
	 * </p>
	 * 
	 * @return the receiver's cursor or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public Cursor getCursor() {
		checkWidget();
		return cursor;
	}

	/**
	 * Returns <code>true</code> if the receiver is detecting drag gestures, and
	 * <code>false</code> otherwise.
	 * 
	 * @return the receiver's drag detect state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public boolean getDragDetect() {
		checkWidget();
		return isDragDetectEnabled();
	}

	private boolean isDragDetectEnabled() {
		return (state & DRAG_DETECT) != 0;
	}

	/**
	 * Sets the receiver's drag detect state. If the argument is
	 * <code>true</code>, the receiver will detect drag gestures, otherwise
	 * these gestures will be ignored.
	 * 
	 * @param dragDetect
	 *            the new drag detect state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public void setDragDetect(boolean dragDetect) {
		checkWidget();
		if (dragDetect) {
			state |= DRAG_DETECT;
		} else {
			state &= ~DRAG_DETECT;
		}
	}

	protected boolean isDropTargetEnabled() {
		return getData(DND.DROP_TARGET_KEY) != null; // check if DropTarget is present
	}

	public void setAcceptDrops(boolean accept) {
		getQMasterWidget().setAcceptDrops(accept);
	}

	public void setDragEnabled(boolean enabled) {
		// nothing by default
	}

	public void dragEnd() {
		dragStartPos = null;
	}

	public void setDragNDropListener(DragNDropListener listener) {
		this.dndListener = listener;
	}

	public void unsetDragNDropListener(DragNDropListener listener) {
		if (this.dndListener == listener) {
			this.dndListener = null;
		}
	}

	protected final void sendDropEvent(QDropEvent event) {
		if (this.dndListener == null) {
			return;
		}
		dndListener.drop(event);
	}

	protected final void sendDragMoveEvent(QDragMoveEvent event) {
		if (this.dndListener == null) {
			return;
		}
		dndListener.dragMove(event);
	}

	protected final void sendDragLeaveEvent(QDragLeaveEvent event) {
		if (this.dndListener == null) {
			return;
		}
		dndListener.dragLeave(event);
	}

	protected final void sendDragEnterEvent(QDragEnterEvent event) {
		if (this.dndListener == null) {
			return;
		}
		dndListener.dragEnter(event);
	}

	/**
	 * Returns the font that the receiver will use to paint textual information.
	 * 
	 * @return the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Font getFont() {
		checkWidget();
		if (font != null) {
			return font;
		}
		return defaultFont();
	}

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 * 
	 * @return the receiver's foreground color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getForeground() {
		checkWidget();
		return _getForegroundColor();
	}

	Color _getForegroundColor() {
		if (foreground != null) {
			return foreground;
		}
		return getDefaultForegroundColor();
	}

	protected Color getDefaultForegroundColor() {
		return Color.qt_new(display, getColorFromPalette(ColorRole.WindowText));
	}

	protected QColor getColorFromPalette(ColorRole role) {
		QPalette palette = getQWidget().palette();
		return palette.color(role);
	}

	/**
	 * Returns layout data which is associated with the receiver.
	 * 
	 * @return the receiver's layout data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Object getLayoutData() {
		checkWidget();
		return layoutData;
	}

	/**
	 * Returns a point describing the receiver's location relative to its parent
	 * (or its display if its parent is null), unless the receiver is a shell.
	 * In this case, the point is relative to the display.
	 * 
	 * @return the receiver's location
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getLocation() {
		checkWidget();
		//forceResize();
		return QtSWTConverter.convert(getQWidget().pos());
	}

	/**
	 * Returns the receiver's pop up menu if it has one, or null if it does not.
	 * All controls may optionally have a pop up menu that is displayed when the
	 * user requests one for the control. The sequence of key strokes, button
	 * presses and/or button releases that are used to request a pop up menu is
	 * platform specific.
	 * 
	 * @return the receiver's menu
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public Menu getMenu() {
		checkWidget();
		return menu;
	}

	/**
	 * Returns the receiver's monitor.
	 * 
	 * @return the receiver's monitor
	 * 
	 * @since 3.0
	 */
	public Monitor getMonitor() {
		checkWidget();
		return Display.createMonitor(getQWidget());
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Composite</code> or
	 * null when the receiver is a shell that was created with null or a display
	 * for a parent.
	 * 
	 * @return the receiver's parent
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Composite getParent() {
		checkWidget();
		return parent;
	}

	Control[] getPath() {
		int count = 0;
		Shell shell = getShell();
		Control control = this;
		while (control != shell) {
			count++;
			control = control.parent;
		}
		control = this;
		Control[] result = new Control[count];
		while (control != shell) {
			result[--count] = control;
			control = control.parent;
		}
		return result;
	}

	/**
	 * Returns the region that defines the shape of the control, or null if the
	 * control has the default shape.
	 * 
	 * @return the region that defines the shape of the shell (or null)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public Region getRegion() {
		checkWidget();
		return region;
	}

	/**
	 * Returns the receiver's shell. For all controls other than shells, this
	 * simply returns the control's nearest ancestor shell. Shells return
	 * themselves, even if they are children of other shells.
	 * 
	 * @return the receiver's shell
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getParent
	 */
	public Shell getShell() {
		checkWidget();
		return parent.getShell();
	}

	/**
	 * Returns a point describing the receiver's size. The x coordinate of the
	 * result is the width of the receiver. The y coordinate of the result is
	 * the height of the receiver.
	 * 
	 * @return the receiver's size
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getSize() {
		checkWidget();
		return QtSWTConverter.convert(getQMasterWidget().frameSize());
	}

	int getClientWidth() {
		return getQMasterWidget().rect().width();
	}

	/**
	 * Returns the receiver's tool tip text, or null if it has not been set.
	 * 
	 * @return the receiver's tool tip text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getToolTipText() {
		checkWidget();
		return getQWidget().toolTip();
	}

	boolean hasFocus() {
		return getQWidget().hasFocus();
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Control</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param data
	 *            the platform specific GC data
	 * @return the platform specific GC handle
	 */
	public QPaintDeviceInterface internal_new_GC(GCData data) {
		checkWidget();
		initGCData(data);
		// we are in a Qt paint event
		if (isOngoingPaintEvent) {
			return getQWidget();
		}
		// if we are not in a paint event, we need to temporarily render to a
		// temp GC and apply the changes in the next paint event
		temporaryGC = new QPicture();
		//System.out.println("creating tmp gc for " + this + " " + temporaryGC);
		return temporaryGC;
	}

	void initGCData(GCData data) {
		data.device = display;
		data.backgroundColor = getBackground();
		data.foregroundColor = getForeground();
		data.font = getFont();
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Control</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param hDC
	 *            the platform specific GC handle
	 * @param data
	 *            the platform specific GC data
	 */
	public void internal_dispose_GC(QPaintDeviceInterface paintDevice, GCData data) {
		checkWidget();
		if (!isOngoingPaintEvent) {
			//System.out.println("tmp gc back " + this + " " + parent + " " + Thread.currentThread());
			//new RuntimeException().printStackTrace();
			getQWidget().update();
		}
	}

	boolean isActive() {
		Dialog dialog = display.getModalDialog();
		if (dialog != null) {
			Shell dialogShell = dialog.parent;
			if (dialogShell != null && !dialogShell.isDisposed()) {
				if (dialogShell != getShell()) {
					return false;
				}
			}
		}
		Shell shell = null;
		Shell[] modalShells = display.modalShells;
		if (modalShells != null) {
			int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
			int index = modalShells.length;
			while (--index >= 0) {
				Shell modal = modalShells[index];
				if (modal != null) {
					if ((modal.style & bits) != 0) {
						Control control = this;
						while (control != null) {
							if (control == modal) {
								break;
							}
							control = control.parent;
						}
						if (control != modal) {
							return false;
						}
						break;
					}
					if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
						if (shell == null) {
							shell = getShell();
						}
						if (modal.parent == shell) {
							return false;
						}
					}
				}
			}
		}
		if (shell == null) {
			shell = getShell();
		}
		return shell.getEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver has the user-interface focus,
	 * and <code>false</code> otherwise.
	 * 
	 * @return the receiver's focus state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean isFocusControl() {
		checkWidget();
		return _isFocusControl();
	}

	protected boolean _isFocusControl() {
		Control focusControl = display.focusControl;
		if (focusControl != null && !focusControl.isDisposed()) {
			return this == focusControl;
		}
		return hasFocus();
	}

	boolean isFocusAncestor(Control control) {
		while (control != null && control != this && !(control instanceof Shell)) {
			control = control.parent;
		}
		return control == this;
	}

	/**
	 * Returns <code>true</code> if the underlying operating system supports
	 * this reparenting, otherwise <code>false</code>
	 * 
	 * @return <code>true</code> if the widget can be reparented, otherwise
	 *         <code>false</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean isReparentable() {
		checkWidget();
		return true;
	}

	boolean isShowing() {
		if (!isVisible()) {
			return false;
		}
		Control control = this;
		while (control != null) {
			Point size = control.getSize();
			if (size.x == 0 || size.y == 0) {
				return false;
			}
			control = control.parent;
		}
		return true;
	}

	boolean isTabGroup() {
		Control[] tabList = parent._getTabList();
		if (tabList != null) {
			for (int i = 0; i < tabList.length; i++) {
				if (tabList[i] == this) {
					return true;
				}
			}
		}
		//TODO
		return false;
	}

	boolean isTabItem() {
		Control[] tabList = parent._getTabList();
		if (tabList != null) {
			for (int i = 0; i < tabList.length; i++) {
				if (tabList[i] == this) {
					return false;
				}
			}
		}
		return FocusPolicy.TabFocus.equals(getQWidget().focusPolicy());
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all ancestors up
	 * to and including the receiver's nearest ancestor shell are visible.
	 * Otherwise, <code>false</code> is returned.
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getVisible
	 */
	public boolean isVisible() {
		checkWidget();
		return getQMasterWidget().isVisible();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getVisible() {
		checkWidget();
		// TODO hack. some layouts are distroyed if we return the visibile state instead of the real visibility
		return getQMasterWidget().isVisible();
	}

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>, and
	 * marks it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause
	 * it to be displayed.
	 * </p>
	 * 
	 * @param visible
	 *            the new visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setVisible(final boolean visible) {
		checkWidget();
		_setVisible(getQMasterWidget(), visible);
	}

	protected void _setVisible(QWidget widget, final boolean visible) {
		//this.visible = visible;

		boolean oldVisibility = getQMasterWidget().isVisible();

		// we always set the visibility, this is important during initialization, when all widgets are not visible, but some are explicitly hidden
		widget.setVisible(visible);

		if (oldVisibility == visible) { // bail out if state has not changed
			return;
		}

		// TODO hack
		if (widget.parentWidget() != null) {
			widget.parentWidget().update();
		}

		if (getQMasterWidget().isVisible()) {
			sendEvent(SWT.Show);
			if (isDisposed()) {
				return;
			}
		}
		Control control = null;
		boolean fixFocus = false;
		if (getQMasterWidget().isHidden()) {
			if (display.focusEvent != SWT.FocusOut) {
				control = display.getFocusControl();
				fixFocus = isFocusAncestor(control);
			}

			sendEvent(SWT.Hide);
			if (isDisposed()) {
				return;
			}
		}
		if (fixFocus) {
			fixFocus(control);
		}
	}

	void markLayout(boolean changed, boolean all) {
		/* Do nothing */
	}

	Decorations menuShell() {
		return parent.menuShell();
	}

	boolean mnemonicHit(char key) {
		return false;
	}

	boolean mnemonicMatch(char key) {
		return false;
	}

	/**
	 * Moves the receiver above the specified control in the drawing order. If
	 * the argument is null, then the receiver is moved to the top of the
	 * drawing order. The control at the top of the drawing order will not be
	 * covered by other controls even if they occupy intersecting areas.
	 * 
	 * @param control
	 *            the sibling control (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Control#moveBelow
	 * @see Composite#getChildren
	 */
	public void moveAbove(Control control) {
		checkWidget();
		if (control != null) {
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (parent != control.parent) {
				return;
			}

			Control controlAbove = null;
			List<QObject> children = getQWidget().children();
			boolean next = false;
			for (QObject child : children) {
				Widget widget = display.findControl(child);
				if (!next && widget == control) {
					next = true;
					continue;
				}
				if (next) {
					if (widget != null && widget != this) {
						if (widget instanceof Control) {
							controlAbove = (Control) widget;
							break;
						}
					}
				}
			}
			if (controlAbove != null) {
				moveBelow(controlAbove);
			} else {
				getQMasterWidget().raise();
			}
		} else {
			getQMasterWidget().raise();
		}
	}

	/**
	 * Moves the receiver below the specified control in the drawing order. If
	 * the argument is null, then the receiver is moved to the bottom of the
	 * drawing order. The control at the bottom of the drawing order will be
	 * covered by all other controls which occupy intersecting areas.
	 * 
	 * @param control
	 *            the sibling control (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Control#moveAbove
	 * @see Composite#getChildren
	 */
	public void moveBelow(Control control) {
		checkWidget();
		if (control != null) {
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (parent != control.parent) {
				return;
			}
			getQMasterWidget().stackUnder(control.getQWidget());
		} else {
			getQMasterWidget().lower();
		}
	}

	Accessible new_Accessible(Control control) {
		return Accessible.internal_new_Accessible(this);
	}

	@Override
	GC new_GC(GCData data) {
		return GC.qt_new(this, data);
	}

	/**
	 * Causes the receiver to be resized to its preferred size. For a composite,
	 * this involves computing the preferred size from its layout, if there is
	 * one.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #computeSize(int, int, boolean)
	 */
	public void pack() {
		checkWidget();
		pack(true);
	}

	/**
	 * Causes the receiver to be resized to its preferred size. For a composite,
	 * this involves computing the preferred size from its layout, if there is
	 * one.
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the
	 * receiver's <em>contents</em> have changed, therefore any caches that a
	 * layout manager containing the control may have been keeping need to be
	 * flushed. When the control is resized, the changed flag will be
	 * <code>false</code>, so layout manager caches can be retained.
	 * </p>
	 * 
	 * @param changed
	 *            whether or not the receiver's contents have changed
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #computeSize(int, int, boolean)
	 */
	public void pack(boolean changed) {
		checkWidget();
		setSize(computeSize(SWT.DEFAULT, SWT.DEFAULT, changed));
	}

	/**
	 * Prints the receiver and all children.
	 * 
	 * @param gc
	 *            the gc where the drawing occurs
	 * @return <code>true</code> if the operation was successful and
	 *         <code>false</code> otherwise
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
	 * 
	 * @since 3.4
	 */
	public boolean print(GC gc) {
		checkWidget();
		if (gc == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (gc.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}

		// TODO
		//		QPixmap pix;
		//		pix.grabWidget(myMainWindowWidget);
		//		QImage im = pix.convertToImage();
		return false;
	}

	/**
	 * Causes the entire bounds of the receiver to be marked as needing to be
	 * redrawn. The next time a paint request is processed, the control will be
	 * completely painted, including the background.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	public void redraw() {
		checkWidget();
		_redraw();
	}

	void _redraw() {
		getQMasterWidget().update();
		if (getQMasterWidget() != getQWidget()) {
			getQWidget().update();
		}
		redrawChildren();
	}

	/**
	 * Causes the rectangular area of the receiver specified by the arguments to
	 * be marked as needing to be redrawn. The next time a paint request is
	 * processed, that area of the receiver will be painted, including the
	 * background. If the <code>all</code> flag is <code>true</code>, any
	 * children of the receiver which intersect with the specified area will
	 * also paint their intersecting areas. If the <code>all</code> flag is
	 * <code>false</code>, the children will not be painted.
	 * 
	 * @param x
	 *            the x coordinate of the area to draw
	 * @param y
	 *            the y coordinate of the area to draw
	 * @param width
	 *            the width of the area to draw
	 * @param height
	 *            the height of the area to draw
	 * @param all
	 *            <code>true</code> if children should redraw, and
	 *            <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	public void redraw(int x, int y, int width, int height, boolean all) {
		checkWidget();
		getQMasterWidget().update(x, y, width, height);
		if (getQMasterWidget() != getQWidget()) {
			getQWidget().update(x, y, width, height);
		}

		if (all) {
			redrawChildren();
		}
	}

	void redrawChildren() {
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is moved or resized.
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
	 * @see ControlListener
	 * @see #addControlListener
	 */
	public void removeControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Move, listener);
		eventTable.unhook(SWT.Resize, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when a drag gesture occurs.
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
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 * 
	 * @since 3.3
	 */
	public void removeDragDetectListener(DragDetectListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.DragDetect, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control gains or loses focus.
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
	 * @see FocusListener
	 * @see #addFocusListener
	 */
	public void removeFocusListener(FocusListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.FocusIn, listener);
		eventTable.unhook(SWT.FocusOut, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when a gesture is performed on the control
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see GestureListener
	 * @see #addGestureListener
	 * @since 3.7
	 */
	public void removeGestureListener (GestureListener listener) {
		checkWidget();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Gesture, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the help events are generated for the control.
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
	 * @see HelpListener
	 * @see #addHelpListener
	 */
	public void removeHelpListener(HelpListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Help, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when keys are pressed and released on the system keyboard.
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
	 * @see KeyListener
	 * @see #addKeyListener
	 */
	public void removeKeyListener(KeyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.KeyUp, listener);
		eventTable.unhook(SWT.KeyDown, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the platform-specific context menu trigger has occurred.
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
	 * @see MenuDetectListener
	 * @see #addMenuDetectListener
	 * 
	 * @since 3.3
	 */
	public void removeMenuDetectListener(MenuDetectListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.MenuDetect, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the mouse passes or hovers over controls.
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
	 * @see MouseTrackListener
	 * @see #addMouseTrackListener
	 */
	public void removeMouseTrackListener(MouseTrackListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.MouseEnter, listener);
		eventTable.unhook(SWT.MouseExit, listener);
		eventTable.unhook(SWT.MouseHover, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when mouse buttons are pressed and released.
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
	 * @see MouseListener
	 * @see #addMouseListener
	 */
	public void removeMouseListener(MouseListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.MouseDown, listener);
		eventTable.unhook(SWT.MouseUp, listener);
		eventTable.unhook(SWT.MouseDoubleClick, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the mouse moves.
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
	 * @see MouseMoveListener
	 * @see #addMouseMoveListener
	 */
	public void removeMouseMoveListener(MouseMoveListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.MouseMove, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the mouse wheel is scrolled.
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
	 * @see MouseWheelListener
	 * @see #addMouseWheelListener
	 * 
	 * @since 3.3
	 */
	public void removeMouseWheelListener(MouseWheelListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.MouseWheel, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver needs to be painted.
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
	 * @see PaintListener
	 * @see #addPaintListener
	 */
	public void removePaintListener(PaintListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Paint, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when traversal events occur.
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
	 * @see TraverseListener
	 * @see #addTraverseListener
	 */
	public void removeTraverseListener(TraverseListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Traverse, listener);
	}

	void sendMove() {
		sendEvent(SWT.Move);
	}

	void sendResize() {
		sendEvent(SWT.Resize);
	}

	/**
	 * Sets the receiver's background color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform. For
	 * example, on Windows the background of a Button cannot be changed.
	 * </p>
	 * 
	 * @param color
	 *            the new color (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setBackground(Color color) {
		checkWidget();
		_setBackground(color);
	}

	void _setBackground(Color color) {
		if (color != null) {
			if (color.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (color == null) {
			background = getDefaultBackgroundColor();
		} else {
			background = color;
		}
		if (backgroundImage == null) {
			updateBackgroundColor();
		}
	}

	protected Color getDefaultBackgroundColor() {
		return display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	/* refactored: was checkBackground */
	protected void checkAndUpdateBackground() {
		Shell shell = getShell();
		if (this == shell) {
			return;
		}
		state &= ~PARENT_BACKGROUND;
		Composite composite = parent;
		do {
			int mode = composite.getBackgroundMode();
			if (mode != 0) {
				if (mode == SWT.INHERIT_DEFAULT) {
					Control control = this;
					do {
						if ((control.state & THEME_BACKGROUND) == 0) {
							return;
						}
						control = control.parent;
					} while (control != composite);
				}
				state |= PARENT_BACKGROUND;
				return;
			}
			if (composite == shell) {
				break;
			}
			composite = composite.parent;
		} while (true);
	}

	protected void updateBackground() {
		if ((state & PARENT_BACKGROUND) == 0) {
			return;
		}
		Control control = findBackgroundImageControl();
		if (control == null) {
			control = this;
		}
		if (control.backgroundImage != null) {
			applyBackgroundImage(control.backgroundImage);
			return;
		}

		control = findBackgroundColorControl();
		if (control == null) {
			control = this;
		}
		applyBackgroundColor(control.background != null ? control.background : control.getDefaultBackgroundColor());
	}

	Control findBackgroundControl() {
		if (background != null || backgroundImage != null) {
			return this;
		}
		return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl() : null;
	}

	protected void applyBackgroundColor(Color color) {
		updatedPalette(color, getBackgroundColorRoles());
	}

	protected void updatedPalette(Color color, ColorRole[] colorRoles) {
		QPalette palette = getQMasterWidget().palette();
		for (ColorRole role : colorRoles) {
			palette.setBrush(role, new QBrush(color.getColor()));
		}
		getQMasterWidget().setAutoFillBackground(true);
		getQMasterWidget().setPalette(palette);
	}

	protected ColorRole[] getBackgroundColorRoles() {
		return new ColorRole[] { ColorRole.Window, ColorRole.Base, ColorRole.Button };
	}

	protected ColorRole[] getBackgroundImageRoles() {
		return new QPalette.ColorRole[] { QPalette.ColorRole.Window, QPalette.ColorRole.Base };
	}

	/**
	 * Sets the receiver's background image to the image specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null. The background image is tiled to fill the available space.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform. For
	 * example, on Windows the background of a Button cannot be changed.
	 * </p>
	 * 
	 * @param image
	 *            the new image (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument is not a
	 *                bitmap</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.2
	 */
	public void setBackgroundImage(Image image) {
		checkWidget();
		if (image != null) {
			if (image.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (!image.isBitmap()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (image == null) {
			backgroundImage = null;
			updateBackgroundColor();
		} else {
			backgroundImage = image;
			updateBackgroundImage();
		}
	}

	void applyBackgroundImage(Image image) {
		QPalette palette = getQMasterWidget().palette();
		ColorRole[] bkRoles = getBackgroundImageRoles();
		for (ColorRole bkRole : bkRoles) {
			//				if (bgControl != this && canInheritBackgroundImage()) {
			//					palette.setColor(bkRole, null);
			//					// If background is inherited then brush is set to null
			//					palette.setBrush(bkRole, null);
			//				} else {
			palette.setBrush(bkRole, new QBrush(image.getQPixmap()));
			//				}
		}
		getQMasterWidget().setPalette(palette);
		getQMasterWidget().setAutoFillBackground(true);
	}

	protected boolean canInheritBackgroundImage() {
		return true;
	}

	/**
	 * Sets the receiver's size and location to the rectangular area specified
	 * by the arguments. The <code>x</code> and <code>y</code> arguments are
	 * relative to the receiver's parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the <code>x</code> and
	 * <code>y</code> arguments are relative to the display.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * 
	 * @param x
	 *            the new x coordinate for the receiver
	 * @param y
	 *            the new y coordinate for the receiver
	 * @param width
	 *            the new width for the receiver
	 * @param height
	 *            the new height for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		setBounds(x, y, width, height, true, true);
	}

	protected void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		if (resize) {
			Point oldSize = QtSWTConverter.convert(getQMasterWidget().size());
			if (oldSize.x != width || oldSize.y != height) {
				getQMasterWidget().resize(width, height);
				state |= RESIZE_OCCURRED;
			}
		}
		if (move) {
			Point oldPos = QtSWTConverter.convert(getQMasterWidget().pos());
			if (oldPos.x != x || oldPos.y != y) {
				getQMasterWidget().move(x, y);
				state |= MOVE_OCCURRED;
			}
		}
	}

	/**
	 * Sets the receiver's size and location to the rectangular area specified
	 * by the argument. The <code>x</code> and <code>y</code> fields of the
	 * rectangle are relative to the receiver's parent (or its display if its
	 * parent is null).
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * 
	 * @param rect
	 *            the new bounds for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setBounds(Rectangle rect) {
		checkWidget();
		if (rect == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setBounds(rect.x, rect.y, rect.width, rect.height, true, true);
	}

	/**
	 * If the argument is <code>true</code>, causes the receiver to have all
	 * mouse events delivered to it until the method is called with
	 * <code>false</code> as the argument. Note that on some platforms, a mouse
	 * button must currently be down for capture to be assigned.
	 * 
	 * @param capture
	 *            <code>true</code> to capture the mouse, and <code>false</code>
	 *            to release it
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setCapture(boolean capture) {
		checkWidget();
		if (capture) {
			getQMasterWidget().grabMouse();
		} else {
			getQMasterWidget().releaseMouse();
		}
	}

	/**
	 * Sets the receiver's cursor to the cursor specified by the argument, or to
	 * the default cursor for that kind of control if the argument is null.
	 * <p>
	 * When the mouse pointer passes over a control its appearance is changed to
	 * match the control's cursor.
	 * </p>
	 * 
	 * @param cursor
	 *            the new cursor (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setCursor(Cursor cursor) {
		checkWidget();
		if (cursor != null && cursor.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.cursor = cursor;
		if (cursor == null) {
			getQMasterWidget().setCursor(null);
		} else {
			getQMasterWidget().setCursor(cursor.cursor);
		}
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all ancestors up
	 * to and including the receiver's nearest ancestor shell are enabled.
	 * Otherwise, <code>false</code> is returned. A disabled control is
	 * typically not selectable from the user interface and draws with an
	 * inactive or "grayed" look.
	 * 
	 * @return the receiver's enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		checkWidget();
		return getQMasterWidget().isEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and
	 * <code>false</code> otherwise. A disabled control is typically not
	 * selectable from the user interface and draws with an inactive or "grayed"
	 * look.
	 * 
	 * @return the receiver's enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		checkWidget();
		return enabled;
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise. A disabled control is typically not selectable from the
	 * user interface and draws with an inactive or "grayed" look.
	 * 
	 * @param enabled
	 *            the new enabled state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setEnabled(boolean enabled) {
		checkWidget();
		/*
		 * Feature in Windows. If the receiver has focus, disabling the receiver
		 * causes no window to have focus. The fix is to assign focus to the
		 * first ancestor window that takes focus. If no window will take focus,
		 * set focus to the desktop.
		 */
		Control control = null;
		boolean fixFocus = false;
		if (!enabled) {
			if (display.focusEvent != SWT.FocusOut) {
				control = display.getFocusControl();
				fixFocus = isFocusAncestor(control);
			}
		}
		enableWidget(enabled);
		if (fixFocus) {
			fixFocus(control);
		}
	}

	void enableWidget(boolean enabled) {
		this.enabled = enabled;
		getQMasterWidget().setEnabled(enabled);
	}

	boolean setFixedFocus() {
		if ((style & SWT.NO_FOCUS) != 0) {
			return false;
		}
		return forceFocus(FocusReason.OtherFocusReason);
	}

	/**
	 * Causes the receiver to have the <em>keyboard focus</em>, such that all
	 * keyboard events will be delivered to it. Focus reassignment will respect
	 * applicable platform constraints.
	 * 
	 * @return <code>true</code> if the control got focus, and
	 *         <code>false</code> if it was unable to.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #forceFocus
	 */
	public boolean setFocus() {
		checkWidget();
		return setFocus(FocusReason.OtherFocusReason);
	}

	protected boolean setFocus(FocusReason focusReason) {
		if ((style & SWT.NO_FOCUS) != 0) {
			return false;
		}
		return forceFocus(focusReason);
	}

	/**
	 * Sets the font that the receiver will use to paint textual information to
	 * the font specified by the argument, or to the default font for that kind
	 * of control if the argument is null.
	 * 
	 * @param font
	 *            the new font (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setFont(Font font) {
		checkWidget();
		if (font != null && font.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.font = font;
		if (font == null) {
			getQMasterWidget().setFont(null);
		} else {
			getQMasterWidget().setFont(font.getQFont());
		}
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 * 
	 * @param color
	 *            the new color (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setForeground(Color color) {
		checkWidget();
		Color oldColor = foreground;
		if (color != null) {
			if (color.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (foreground == null && color == null) {
			return;
		}
		if (color == null) {
			foreground = null;
		} else {
			foreground = Color.qt_new(display, color.getColor());
		}
		if (oldColor != null) {
			oldColor.dispose();
		}
		applyForegroundColor(foreground);
	}

	protected void updateForeground() {
		if (foreground != null) {
			applyForegroundColor(foreground);
		}
	}

	void applyForegroundColor(Color color) {
		updatedPalette(color, getForegroundColorRoles());
	}

	private ColorRole[] getForegroundColorRoles() {
		return new ColorRole[] { QPalette.ColorRole.WindowText, QPalette.ColorRole.Text, QPalette.ColorRole.ButtonText };
	}

	/**
	 * Sets the layout data associated with the receiver to the argument.
	 * 
	 * @param layoutData
	 *            the new layout data for the receiver.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLayoutData(Object layoutData) {
		checkWidget();
		this.layoutData = layoutData;
	}

	/**
	 * Sets the receiver's location to the point specified by the arguments
	 * which are relative to the receiver's parent (or its display if its parent
	 * is null), unless the receiver is a shell. In this case, the point is
	 * relative to the display.
	 * 
	 * @param x
	 *            the new x coordinate for the receiver
	 * @param y
	 *            the new y coordinate for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLocation(int x, int y) {
		checkWidget();
		setBounds(x, y, 0, 0, true, false);
	}

	/**
	 * Sets the receiver's location to the point specified by the arguments
	 * which are relative to the receiver's parent (or its display if its parent
	 * is null), unless the receiver is a shell. In this case, the point is
	 * relative to the display.
	 * 
	 * @param location
	 *            the new location for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLocation(Point location) {
		checkWidget();
		if (location == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setLocation(location.x, location.y);
	}

	/**
	 * Sets the receiver's pop up menu to the argument. All controls may
	 * optionally have a pop up menu that is displayed when the user requests
	 * one for the control. The sequence of key strokes, button presses and/or
	 * button releases that are used to request a pop up menu is platform
	 * specific.
	 * <p>
	 * Note: Disposing of a control that has a pop up menu will dispose of the
	 * menu. To avoid this behavior, set the menu to null before the control is
	 * disposed.
	 * </p>
	 * 
	 * @param menu
	 *            the new pop up menu
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
	 *                <li>ERROR_INVALID_PARENT - if the menu is not in the same
	 *                widget tree</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMenu(Menu menu) {
		checkWidget();
		if (menu != null) {
			if (menu.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if ((menu.style & SWT.POP_UP) == 0) {
				error(SWT.ERROR_MENU_NOT_POP_UP);
			}
			if (menu.parent != menuShell()) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		this.menu = menu;
	}

	boolean setRadioFocus(boolean tabbing) {
		return false;
	}

	boolean setRadioSelection(boolean value) {
		return false;
	}

	/**
	 * If the argument is <code>false</code>, causes subsequent drawing
	 * operations in the receiver to be ignored. No drawing of any kind can
	 * occur in the receiver until the flag is set to true. Graphics operations
	 * that occurred while the flag was <code>false</code> are lost. When the
	 * flag is set to <code>true</code>, the entire widget is marked as needing
	 * to be redrawn. Nested calls to this method are stacked.
	 * <p>
	 * Note: This operation is a hint and may not be supported on some platforms
	 * or for some widgets.
	 * </p>
	 * 
	 * @param redraw
	 *            the new redraw state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #redraw(int, int, int, int, boolean)
	 * @see #update()
	 */
	public void setRedraw(boolean redraw) {
		checkWidget();
		getQMasterWidget().setUpdatesEnabled(redraw);
	}

	/**
	 * Sets the shape of the control to the region specified by the argument.
	 * When the argument is null, the default shape of the control is restored.
	 * 
	 * @param region
	 *            the region that defines the shape of the control (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the region has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public void setRegion(Region region) {
		checkWidget();
		if (region != null && region.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.region = region;
		getQMasterWidget().setMask(QtSWTConverter.convert(region));
	}

	boolean setSavedFocus() {
		return forceFocus();
	}

	/**
	 * Sets the receiver's size to the point specified by the arguments.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * 
	 * @param width
	 *            the new width for the receiver
	 * @param height
	 *            the new height for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSize(int width, int height) {
		checkWidget();
		setBounds(0, 0, Math.max(0, width), Math.max(0, height), false, true);
	}

	/**
	 * Sets the receiver's size to the point specified by the argument.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause them to be set to zero instead.
	 * </p>
	 * 
	 * @param size
	 *            the new size for the receiver
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSize(Point size) {
		checkWidget();
		if (size == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSize(size.x, size.y);
	}

	boolean setTabGroupFocus() {
		return setTabItemFocus();
	}

	boolean setTabItemFocus() {
		if (!isShowing()) {
			return false;
		}
		return forceFocus();
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default,
	 * causing no tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool
	 * tip. To display a single '&amp;' in the tool tip, the character '&amp;'
	 * can be escaped by doubling it in the string.
	 * </p>
	 * 
	 * @param string
	 *            the new tool tip text (or null)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setToolTipText(String string) {
		checkWidget();
		getQMasterWidget().setToolTip(string);
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in display relative coordinates, to coordinates relative to the
	 * receiver.
	 * <p>
	 * 
	 * @param x
	 *            the x coordinate to be translated
	 * @param y
	 *            the y coordinate to be translated
	 * @return the translated coordinates
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public Point toControl(int x, int y) {
		checkWidget();
		Point mappedPoint = QtSWTConverter.convert(getQMasterWidget().mapFromGlobal(new QPoint(x, y)));
		if (isMirrored()) {
			mappedPoint.x = getClientWidth() - mappedPoint.x;
		}
		return mappedPoint;
	}

	boolean isMirrored() {
		return (style & SWT.MIRRORED) != 0;
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in display relative coordinates, to coordinates relative to the
	 * receiver.
	 * <p>
	 * 
	 * @param point
	 *            the point to be translated (must not be null)
	 * @return the translated coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public Point toControl(Point point) {
		checkWidget();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return toControl(point.x, point.y);
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in coordinates relative to the receiver, to display relative
	 * coordinates.
	 * <p>
	 * 
	 * @param x
	 *            the x coordinate to be translated
	 * @param y
	 *            the y coordinate to be translated
	 * @return the translated coordinates
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public Point toDisplay(int x, int y) {
		checkWidget();
		if (isMirrored()) {
			x = getClientWidth() - x;
		}
		return QtSWTConverter.convert(getQMasterWidget().mapToGlobal(new QPoint(x, y)));
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in coordinates relative to the receiver, to display relative
	 * coordinates.
	 * <p>
	 * 
	 * @param point
	 *            the point to be translated (must not be null)
	 * @return the translated coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public Point toDisplay(Point point) {
		checkWidget();
		System.out.println("toDisplay:" + point);
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return toDisplay(point.x, point.y);
	}

	boolean translateMnemonic(Event event, Control control) {
		if (control == this) {
			return false;
		}
		if (!isVisible() || !isEnabled()) {
			return false;
		}
		event.doit = mnemonicMatch(event.character);
		return traverse(event);
	}

	boolean traverse(Event event) {
		/*
		 * It is possible (but unlikely), that application code could have
		 * disposed the widget in the traverse event. If this happens, return
		 * true to stop further event processing.
		 */
		sendEvent(SWT.Traverse, event);
		if (isDisposed()) {
			return true;
		}
		if (!event.doit) {
			return false;
		}
		switch (event.detail) {
		case SWT.TRAVERSE_NONE:
			return true;
		case SWT.TRAVERSE_ESCAPE:
			return traverseEscape();
		case SWT.TRAVERSE_RETURN:
			return traverseReturn();
		case SWT.TRAVERSE_TAB_NEXT:
			return traverseGroup(true);
		case SWT.TRAVERSE_TAB_PREVIOUS:
			return traverseGroup(false);
		case SWT.TRAVERSE_ARROW_NEXT:
			return traverseItem(true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			return traverseItem(false);
		case SWT.TRAVERSE_MNEMONIC:
			return traverseMnemonic(event.character);
		case SWT.TRAVERSE_PAGE_NEXT:
			return traversePage(true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			return traversePage(false);
		}
		return false;
	}

	/**
	 * Based on the argument, perform one of the expected platform traversal
	 * action. The argument should be one of the constants:
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>,
	 * <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code> and
	 * <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
	 * 
	 * @param traversal
	 *            the type of traversal
	 * @return true if the traversal succeeded
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean traverse(int traversal) {
		checkWidget();
		Event event = new Event();
		event.doit = true;
		event.detail = traversal;
		return traverse(event);
	}

	boolean traverseEscape() {
		return false;
	}

	boolean traverseGroup(boolean next) {
		Control root = computeTabRoot();
		Control group = computeTabGroup();
		Control[] list = root.computeTabList();
		int length = list.length;
		int index = 0;
		while (index < length) {
			if (list[index] == group) {
				break;
			}
			index++;
		}
		/*
		 * It is possible (but unlikely), that application code could have
		 * disposed the widget in focus in or out events. Ensure that a disposed
		 * widget is not accessed.
		 */
		if (index == length) {
			return false;
		}
		int start = index, offset = next ? 1 : -1;
		while ((index = (index + offset + length) % length) != start) {
			Control control = list[index];
			if (!control.isDisposed() && control.setTabGroupFocus()) {
				return true;
			}
		}
		if (group.isDisposed()) {
			return false;
		}
		return group.setTabGroupFocus();
	}

	boolean traverseItem(boolean next) {
		Control[] children = parent._getChildren();
		int length = children.length;
		int index = 0;
		while (index < length) {
			if (children[index] == this) {
				break;
			}
			index++;
		}
		/*
		 * It is possible (but unlikely), that application code could have
		 * disposed the widget in focus in or out events. Ensure that a disposed
		 * widget is not accessed.
		 */
		if (index == length) {
			return false;
		}
		int start = index, offset = next ? 1 : -1;
		while ((index = (index + offset + length) % length) != start) {
			Control child = children[index];
			if (!child.isDisposed() && child.isTabItem()) {
				if (child.setTabItemFocus()) {
					return true;
				}
			}
		}
		return false;
	}

	boolean traverseMnemonic(char key) {
		// TODO
		//		if (mnemonicHit(key)) {
		//			OS.SendMessage(handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		//			return true;
		//		}
		return false;
	}

	boolean traversePage(boolean next) {
		return false;
	}

	boolean traverseReturn() {
		return false;
	}

	/**
	 * Forces all outstanding paint requests for the widget to be processed
	 * before this method returns. If there are no outstanding paint request,
	 * this method does nothing.
	 * <p>
	 * Note: This method does not cause a redraw.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #redraw()
	 * @see #redraw(int, int, int, int, boolean)
	 * @see PaintListener
	 * @see SWT#Paint
	 */
	public void update() {
		checkWidget();
		_update();
	}

	void _update() {
		QCoreApplication.processEvents(ProcessEventsFlag.ExcludeUserInputEvents);
	}

	void updateBackgroundColor() {
		Control control = findBackgroundColorControl();
		if (control == null) {
			control = this;
		}
		applyBackgroundColor(control.background);
	}

	Control findBackgroundColorControl() {
		if (background != null) {
			return this;
		}
		if (parent == null) {
			return null;
		}
		return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundColorControl() : null;
	}

	void updateBackgroundImage() {
		Control control = findBackgroundImageControl();
		if (control == null) {
			control = this;
		}
		applyBackgroundImage(control.backgroundImage);
	}

	Control findBackgroundImageControl() {
		if (backgroundImage != null) {
			return this;
		}
		if (parent == null) {
			return null;
		}
		return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundImageControl() : null;
	}

	void updateBackgroundMode() {
		int oldState = state & PARENT_BACKGROUND;
		checkAndUpdateBackground();
		if (oldState != (state & PARENT_BACKGROUND)) {
			updateBackground();
		}
	}

	void updateFont(Font oldFont, Font newFont) {
		if (getFont().equals(oldFont)) {
			setFont(newFont);
		}
	}

	void updateImages() {
		/* Do nothing */
	}

	void updateLayout(boolean all) {
		/* Do nothing */
	}

	/**
	 * Changes the parent of the widget to be the one provided if the underlying
	 * operating system supports this feature. Returns <code>true</code> if the
	 * parent is successfully changed.
	 * 
	 * @param parent
	 *            the new parent for the control.
	 * @return <code>true</code> if the parent is changed and <code>false</code>
	 *         otherwise.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is
	 *                <code>null</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean setParent(Composite parent) {
		checkWidget();
		if (parent == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (parent.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.parent == parent) {
			return true;
		}
		if (!isReparentable()) {
			return false;
		}
		releaseParent();
		Shell newShell = parent.getShell(), oldShell = getShell();
		Decorations newDecorations = parent.menuShell(), oldDecorations = menuShell();
		if (oldShell != newShell || oldDecorations != newDecorations) {
			Menu[] menus = oldShell.findMenus(this);
			fixChildren(newShell, oldShell, newDecorations, oldDecorations, menus);
		}
		getQMasterWidget().setParent(parent.getQWidget());
		return true;
	}

	@Override
	public boolean qtPaintEvent(QObject source, QPaintEvent paintEvent) {
		if (source == getQWidget()) {
			try {
				isOngoingPaintEvent = true;
				renderTemporaryGC(paintEvent.rect());
				sendPaintEvent(paintEvent);
			} finally {
				isOngoingPaintEvent = false;
			}
		}
		return false;
	}

	private void renderTemporaryGC(QRect rect) {
		// Render the buffer created outside the paint event, if any
		if (temporaryGC != null) {
			//System.out.println("render tmp gc for " + this + " " + rect + " " + temporaryGC);
			QPainter painter = new QPainter(getQWidget());
			//painter.setClipRect(rect);
			painter.drawPicture(0, 0, temporaryGC);
			painter.end();
			temporaryGC = null;
		}
	}

	private void sendPaintEvent(QPaintEvent paintEvent) {
		if (!hooks(SWT.Paint) && !filters(SWT.Paint)) {
			return;
		}

		QRect rect = paintEvent.rect();
		int x = rect.x();
		int y = rect.y();
		int width = rect.width();
		int height = rect.height();
		GCData data = new GCData();
		initGCData(data);
		GC gc = GC.qt_new(this, getQWidget(), data);
		Event event = new Event();
		event.count = 0;
		if (isMirrored()) {
			event.x = getClientWidth() - x - width;
		} else {
			event.x = x;
		}
		event.y = y;
		event.width = width;
		event.height = height;
		event.gc = gc;
		try {
			gc.setClipping(x, y, width, height);
			sendEvent(SWT.Paint, event);
		} finally {
			if (!gc.isDisposed()) {
				gc.dispose();
			}
		}
	}

	@Override
	public boolean qtResizeEvent(QObject source, QResizeEvent resizeEvent) {
		if (source == getQWidget()) {
			sendResize();
		}
		return super.qtResizeEvent(source, resizeEvent);
	}

	@Override
	public boolean qtMoveEvent(QObject source, QMoveEvent moveEvent) {
		//		if (source == getQMasterWidget()) {
		//			sendMove();
		//		}
		return true;
	}

	@Override
	public boolean qtKeyPressEvent(QObject source, QKeyEvent qEvent) {
		// if ( ( ( state & NO_KEY_PROPAGATE ) != 0 ) ) {
		// return true;
		// }
		if (source == getQWidget()) {
			return sendKeyEvent(SWT.KeyDown, qEvent);
		}
		return false;
	}

	@Override
	public boolean qtKeyReleaseEvent(QObject source, QKeyEvent qEvent) {
		if (source == getQWidget()) {
			return sendKeyEvent(SWT.KeyUp, qEvent);
		}
		// if ( ( ( state & NO_KEY_PROPAGATE ) != 0 ) ) {
		// return true;
		// }
		return false;
	}

	boolean sendKeyEvent(int type, QKeyEvent qEvent) {
		Event event = translateKeyEvent(qEvent);
		sendEvent(type, event);
		if (isDisposed()) {
			return true;
		}
		return !event.doit;
	}

	private final Event translateKeyEvent(QKeyEvent qEvent) {
		Event event = new Event();
		switch (Key.resolve(qEvent.key())) {
		case Key_Enter:
		case Key_Return:
			event.character = SWT.CR;
			break;
		case Key_Backspace:
			event.character = SWT.BS;
			break;
		case Key_Delete:
			event.character = SWT.DEL;
			break;
		case Key_Escape:
			event.character = SWT.ESC;
			break;
		case Key_Tab:
			event.character = SWT.TAB;
			break;
		default:
			String text = qEvent.text();
			if (text != null && text.length() > 0) {
				event.character = qEvent.text().charAt(0);
			}
			break;
		}
		event.keyCode = KeyUtil.translateKey(qEvent);
		if (event.keyCode == 0) {// keyCode defaults to unicode value
			String text = qEvent.text();
			if (text != null && text.length() > 0) {
				event.keyCode = qEvent.text().charAt(0);
			}
		}
		event.stateMask = KeyUtil.translateModifiers(qEvent.modifiers());
		return event;
	}

	protected boolean sendMouseEvent(int type, QMouseEvent mouseEvent) {
		return sendMouseEvent(type, mouseEvent, 0);
	}

	protected boolean sendMouseEvent(int type, QMouseEvent mouseEvent, int count) {
		Event event = translateMouseEvent(mouseEvent, count);
		sendEvent(type, event);
		if (isDisposed()) {
			return true;
		}
		return !event.doit;
	}

	private Event translateMouseEvent(QMouseEvent mouseEvent, int count) {
		Event event = new Event();
		QPoint pos = mouseEvent.pos();
		event.x = pos.x();
		event.y = pos.y();
		event.count = count;
		event.button = translateMouseButton(mouseEvent.button());
		event.stateMask = KeyUtil.translateModifiers(mouseEvent.modifiers())
				| translateMouseButtons(mouseEvent.buttons());
		event.stateMask &= ~event.button;

		return event;
	}

	protected int translateMouseButton(MouseButton button) {
		switch (button) {
		case LeftButton:
			return SWT.BUTTON1;
		case MidButton:
			return SWT.BUTTON2;
		case RightButton:
			return SWT.BUTTON1;
		}
		return 0;
	}

	private int translateMouseButtons(MouseButtons buttons) {
		int mask = 0;
		if (buttons.isSet(MouseButton.LeftButton)) {
			mask |= SWT.BUTTON1;
		}
		if (buttons.isSet(MouseButton.MidButton)) {
			mask |= SWT.BUTTON2;
		}
		if (buttons.isSet(MouseButton.RightButton)) {
			mask |= SWT.BUTTON3;
		}
		return mask;
	}

	@Override
	public boolean qtMouseMoveEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			if (checkForDragging(mouseEvent)) {
				System.out.println("dragging...");
				sendDragEvent(SWT.BUTTON1, mouseEvent.pos().x(), mouseEvent.pos().y());
				return true;
			} else {
				return sendMouseEvent(SWT.MouseMove, mouseEvent);
			}
		}
		return false;
	}

	private boolean checkForDragging(QMouseEvent event) {
		return dragStartPos != null && isDragDetectEnabled()
				&& event.pos().subtract(dragStartPos).manhattanLength() >= QApplication.startDragDistance();
	}

	@Override
	public boolean qtMouseButtonPressEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			checkForDragStart(mouseEvent);
			return sendMouseEvent(SWT.MouseDown, mouseEvent, 1);
		}
		return false;
	}

	private void checkForDragStart(QMouseEvent mouseEvent) {
		if (isDragDetectEnabled() && mouseEvent.buttons().isSet(MouseButton.LeftButton)) {
			dragStartPos = mouseEvent.pos();
		}
	}

	@Override
	public boolean qtMouseButtonReleaseEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			dragStartPos = null;
			return sendMouseEvent(SWT.MouseUp, mouseEvent, 1);
		}
		return false;
	}

	@Override
	public boolean qtMouseButtonDblClickEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			//System.out.println("mouse dbl click on: " + this);
			boolean doit = sendMouseEvent(SWT.MouseDown, mouseEvent, 2);
			if (doit) {
				return sendMouseEvent(SWT.MouseDoubleClick, mouseEvent, 2);
			}
		}
		return false;
	}

	@Override
	public void qtFocusInEvent(QObject source) {
		if (source != getQMasterWidget()) {
			return;
		}
		try {
			display.focusEvent = SWT.FocusIn;
			sendEvent(SWT.FocusIn);
		} finally {
			display.focusEvent = SWT.None;
		}
	}

	@Override
	public void qtFocusOutEvent(QObject source) {
		if (source != getQMasterWidget()) {
			return;
		}
		try {
			display.focusEvent = SWT.FocusOut;
			sendEvent(SWT.FocusOut);
		} finally {
			display.focusEvent = SWT.None;
		}
	}

	protected boolean handleContextMenuEvent(QContextMenuEvent menuEvent) {
		if (isDisposed()) {
			return false;
		}
		Event event = new Event();
		event.x = menuEvent.globalX();
		event.y = menuEvent.globalY();
		sendEvent(SWT.MenuDetect, event);

		if (this.menu == null || menu.isDisposed()) {
			return event.doit;
		}
		List<QAction> actions = getQWidget().actions();
		if ((actions == null || actions.size() == 0) && menu == null) {
			return false;
		}

		QMenu popUpMenu = menu.getQMenu();
		popUpMenu.exec(menuEvent.globalPos());
		return true;
	}

	@Override
	public boolean qtContextMenuEvent(Object source, QContextMenuEvent event) {
		if (source == getQWidget()) {
			return handleContextMenuEvent(event);
		}
		return super.qtContextMenuEvent(source, event);
	}

}
