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
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.FocusReason;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.core.Qt.WindowState;
import com.trolltech.qt.core.Qt.WindowStates;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWindowStateChangeEvent;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QSizePolicy.Policy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class represent the "windows" which the desktop or
 * "window manager" is managing. Instances that do not have a parent (that is,
 * they are built using the constructor, which takes a <code>Display</code> as
 * the argument) are described as <em>top level</em> shells. Instances that do
 * have a parent are described as <em>secondary</em> or <em>dialog</em> shells.
 * <p>
 * Instances are always displayed in one of the maximized, minimized or normal
 * states:
 * <ul>
 * <li>
 * When an instance is marked as <em>maximized</em>, the window manager will
 * typically resize it to fill the entire visible area of the display, and the
 * instance is usually put in a state where it can not be resized (even if it
 * has style <code>RESIZE</code>) until it is no longer maximized.</li>
 * <li>
 * When an instance is in the <em>normal</em> state (neither maximized or
 * minimized), its appearance is controlled by the style constants which were
 * specified when it was created and the restrictions of the window manager (see
 * below).</li>
 * <li>
 * When an instance has been marked as <em>minimized</em>, its contents (client
 * area) will usually not be visible, and depending on the window manager, it
 * may be "iconified" (that is, replaced on the desktop by a small simplified
 * representation of itself), relocated to a distinguished area of the screen,
 * or hidden. Combinations of these changes are also possible.</li>
 * </ul>
 * </p>
 * <p>
 * The <em>modality</em> of an instance may be specified using style bits. The
 * modality style bits are used to determine whether input is blocked for other
 * shells on the display. The <code>PRIMARY_MODAL</code> style allows an
 * instance to block input to its parent. The <code>APPLICATION_MODAL</code>
 * style allows an instance to block input to every other shell in the display.
 * The <code>SYSTEM_MODAL</code> style allows an instance to block input to all
 * shells, including shells belonging to different applications.
 * </p>
 * <p>
 * Note: The styles supported by this class are treated as <em>HINT</em>s, since
 * the window manager for the desktop on which the instance is visible has
 * ultimate control over the appearance and behavior of decorations and
 * modality. For example, some window managers only support resizable windows
 * and will always assume the RESIZE style, even if it is not set. In addition,
 * if a modality style is not supported, it is "upgraded" to a more restrictive
 * modality style that is supported. For example, if <code>PRIMARY_MODAL</code>
 * is not supported, it would be upgraded to <code>APPLICATION_MODAL</code>. A
 * modality style may also be "downgraded" to a less restrictive style. For
 * example, most operating systems no longer support <code>SYSTEM_MODAL</code>
 * because it can freeze up the desktop, so this is typically downgraded to
 * <code>APPLICATION_MODAL</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL</dd>
 * <dd>APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Activate, Close, Deactivate, Deiconify, Iconify</dd>
 * </dl>
 * Class <code>SWT</code> provides two "convenience constants" for the most
 * commonly required style combinations:
 * <dl>
 * <dt><code>SHELL_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required to produce a typical
 * application top level shell: (that is,
 * <code>CLOSE | TITLE | MIN | MAX | RESIZE</code>)</dd>
 * <dt><code>DIALOG_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required to produce a typical
 * application dialog shell: (that is, <code>TITLE | CLOSE | BORDER</code>)</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles APPLICATION_MODAL, MODELESS, PRIMARY_MODAL and
 * SYSTEM_MODAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is not intended to be subclassed.
 * </p>
 * 
 * @see Decorations
 * @see SWT
 * @see <a href="http://www.eclipse.org/swt/snippets/#shell">Shell snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public class Shell extends Decorations {
	Menu activeMenu;
	boolean fullScreen, wasMaximized, modified;
	Control lastActive;
	private QMainWindow mainWindow;
	private QDialog dialogWindow;
	private QStyle oldStyle;

	/**
	 * Constructs a new instance of this class. This is equivalent to calling
	 * <code>Shell((Display) null)</code>.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	public Shell() {
		this((Display) null);
	}

	/**
	 * Constructs a new instance of this class given only the style value
	 * describing its behavior and appearance. This is equivalent to calling
	 * <code>Shell((Display) null, style)</code>.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 */
	public Shell(int style) {
		this((Display) null, style);
	}

	/**
	 * Constructs a new instance of this class given only the display to create
	 * it on. It is created with style <code>SWT.SHELL_TRIM</code>.
	 * <p>
	 * Note: Currently, null can be passed in for the display argument. This has
	 * the effect of creating the shell on the currently active display if there
	 * is one. If there is no current display, the shell is created on a
	 * "default" display. <b>Passing in null as the display argument is not
	 * considered to be good coding style, and may not be supported in a future
	 * release of SWT.</b>
	 * </p>
	 * 
	 * @param display
	 *            the display to create the shell on
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	public Shell(Display display) {
		this(display, SWT.SHELL_TRIM);
	}

	/**
	 * Constructs a new instance of this class given the display to create it on
	 * and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * Note: Currently, null can be passed in for the display argument. This has
	 * the effect of creating the shell on the currently active display if there
	 * is one. If there is no current display, the shell is created on a
	 * "default" display. <b>Passing in null as the display argument is not
	 * considered to be good coding style, and may not be supported in a future
	 * release of SWT.</b>
	 * </p>
	 * 
	 * @param display
	 *            the display to create the shell on
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 */
	public Shell(Display display, int style) {
		this(display, null, style, false);
	}

	Shell(Display display, Shell parent, int style, boolean embedded) {
		super();
		checkSubclass();
		if (display == null) {
			display = Display.getCurrent();
		}
		if (display == null) {
			display = Display.getDefault();
		}
		if (!display.isValidThread()) {
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if (parent != null && parent.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.style = checkStyle(style);
		this.parent = parent;
		this.display = display;
		//		if (handle != 0 && !embedded) {
		//			state |= FOREIGN_HANDLE;
		//		}
		createWidget(parent, style);
	}

	/**
	 * Constructs a new instance of this class given only its parent. It is
	 * created with style <code>SWT.DIALOG_TRIM</code>.
	 * <p>
	 * Note: Currently, null can be passed in for the parent. This has the
	 * effect of creating the shell on the currently active display if there is
	 * one. If there is no current display, the shell is created on a "default"
	 * display. <b>Passing in null as the parent is not considered to be good
	 * coding style, and may not be supported in a future release of SWT.</b>
	 * </p>
	 * 
	 * @param parent
	 *            a shell which will be the parent of the new instance
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	public Shell(Shell parent) {
		this(parent, SWT.DIALOG_TRIM);
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
	 * <p>
	 * Note: Currently, null can be passed in for the parent. This has the
	 * effect of creating the shell on the currently active display if there is
	 * one. If there is no current display, the shell is created on a "default"
	 * display. <b>Passing in null as the parent is not considered to be good
	 * coding style, and may not be supported in a future release of SWT.</b>
	 * </p>
	 * 
	 * @param parent
	 *            a shell which will be the parent of the new instance
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
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
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#NO_TRIM
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#TOOL
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 */
	public Shell(Shell parent, int style) {
		this(parent != null ? parent.display : null, parent, style, false);
	}

	@Override
	QWidget createQWidget(int style) {
		QScrollArea scrollArea;
		if (parent == null) {
			// new window
			mainWindow = new QMainWindow(null, createWindowFlags(style));
			scrollArea = new QScrollArea(mainWindow);
			mainWindow.setCentralWidget(scrollArea);
		} else {
			// new dialog
			dialogWindow = new QDialog(parent.getQWidget(), createWindowFlags(style));
			scrollArea = new QScrollArea(dialogWindow);

			QVBoxLayout layout = new QVBoxLayout(dialogWindow);
			layout.addWidget(scrollArea);
			layout.setContentsMargins(0, 0, 0, 0);
		}

		scrollArea.setFrameShape(Shape.NoFrame);
		scrollArea.setContentsMargins(0, 0, 0, 0);
		scrollArea.setWidgetResizable(true);
		setQMasterWidget(scrollArea);

		QWidget contentWidget = new QWidget();
		contentWidget.setProperty("widgetType", "scrollareaContent"); //$NON-NLS-1$ //$NON-NLS-2$
		contentWidget.setSizePolicy(Policy.MinimumExpanding, Policy.MinimumExpanding);
		contentWidget.setContentsMargins(0, 0, 0, 0);
		contentWidget.resize(0, 0);
		scrollArea.setWidget(contentWidget);

		scrollArea.resize(0, 0);

		int policy = getWindowControl().focusPolicy().value() & ~FocusPolicy.ClickFocus.value();
		getWindowControl().setFocusPolicy(FocusPolicy.resolve(policy));

		state |= CANVAS;

		return scrollArea.widget();
	}

	private WindowFlags createWindowFlags(int style) {
		int flags = parent == null ? WindowType.Window.value() : WindowType.Dialog.value()
				| WindowModality.WindowModal.value();
		flags |= WindowType.CustomizeWindowHint.value();
		if ((style & SWT.CLOSE) != 0) {
			flags |= WindowType.WindowSystemMenuHint.value() | WindowType.WindowCloseButtonHint.value();
		}
		if ((style & SWT.MIN) != 0) {
			flags |= WindowType.WindowMinimizeButtonHint.value();
		}
		if ((style & SWT.MAX) != 0) {
			flags |= WindowType.WindowMaximizeButtonHint.value();
		}
		if ((style & SWT.NO_TRIM) != 0) {
			flags |= WindowType.FramelessWindowHint.value();
		}
		if ((style & SWT.TITLE) != 0) {
			flags |= WindowType.WindowTitleHint.value();
		}
		if ((style & SWT.ON_TOP) != 0) {
			flags |= WindowType.WindowStaysOnTopHint.value();
		}
		return new WindowFlags(flags);
	}

	@Override
	protected void setupQWidget() {
		getWindowControl().adjustSize();
		// no setup
	}

	@Override
	void registerQWidget() {
		super.registerQWidget();
		display.addControl(getWindowControl(), this);
	}

	@Override
	void deregisterQWidget() {
		display.removeControl(getWindowControl());
		super.deregisterQWidget();
	}

	@Override
	protected QWidget getQMasterWidget() {
		return getWindowControl();
	}

	@Override
	QWidget getMenuContainer() {
		return getWindowControl();
	}

	QDialog getQDialog() {
		return dialogWindow;
	}

	boolean isDialog() {
		return dialogWindow != null;
	}

	@Override
	public void setStyleSheet(String style) {
		if (style == null || style.trim().length() == 0) {
			getWindowControl().setStyleSheet(null);
			if (oldStyle != null) {
				getWindowControl().setStyle(oldStyle);
				oldStyle = null;
			}
		} else {
			oldStyle = getWindowControl().style();
			getWindowControl().setStyleSheet(style);
		}
		updateLayout();
	}

	@Override
	public void updateStyleSheet() {
		if (null != getWindowControl().style()) {
			getWindowControl().setStyle(getWindowControl().style());
			updateLayout();
		}
	}

	@Override
	protected QWidget getWindowControl() {
		return mainWindow != null ? mainWindow : dialogWindow;
	}

	/**
	 * Invokes platform specific functionality to allocate a new shell that is
	 * embedded.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Shell</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param display
	 *            the display for the shell
	 * @param qMetrics
	 *            the handle for the shell
	 * @return a new shell object containing the specified display and handle
	 */
	public static Shell qt_new(Display display) {
		return new Shell(display, null, SWT.NO_TRIM, true);
	}

	static int checkStyle(int style) {
		style = Decorations.checkStyle(style);
		style &= ~SWT.TRANSPARENT;
		int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
		int bits = style & ~mask;
		if ((style & SWT.SYSTEM_MODAL) != 0) {
			return bits | SWT.SYSTEM_MODAL;
		}
		if ((style & SWT.APPLICATION_MODAL) != 0) {
			return bits | SWT.APPLICATION_MODAL;
		}
		if ((style & SWT.PRIMARY_MODAL) != 0) {
			return bits | SWT.PRIMARY_MODAL;
		}
		return bits;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when operations are performed on the receiver, by sending the listener
	 * one of the messages defined in the <code>ShellListener</code> interface.
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
	 * @see ShellListener
	 * @see #removeShellListener
	 */
	public void addShellListener(ShellListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Close, typedListener);
		addListener(SWT.Iconify, typedListener);
		addListener(SWT.Deiconify, typedListener);
		addListener(SWT.Activate, typedListener);
		addListener(SWT.Deactivate, typedListener);
	}

	/**
	 * Requests that the window manager close the receiver in the same way it
	 * would be closed when the user clicks on the "close box" or performs some
	 * other platform specific key or mouse combination that indicates the
	 * window should be removed.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SWT#Close
	 * @see #dispose
	 */
	public void close() {
		checkWidget();
		closeWidget();
	}

	@Override
	void enableWidget(boolean enabled) {
		Control oldFocus = display.getFocusControl();
		super.enableWidget(enabled);
		if (enabled) {
			_update();
		} else {

			// All children were looped through and disabled by QWidget unless
			// they
			// were explicitly disabled. SWT behavior is not to disable the
			// dialog
			// Shells so we have to restore their states.
			Shell shells[] = getShells();
			for (int i = 0; i < shells.length; ++i) {
				if (shells[i].parent == this) {
					if (shells[i].getEnabled()) {
						shells[i].getQWidget().setEnabled(true);
					}
				}
			}
			// Because dialog Shell might have been temporarily disabled the
			// focus might
			// have been lost and must be restored.
			if (oldFocus != null) {
				oldFocus.menuShell().restoreFocus();
			}
		}
	}

	@Override
	Control findBackgroundControl() {
		return background != null || backgroundImage != null ? this : null;
	}

	@Override
	Cursor findCursor() {
		return cursor;
	}

	@Override
	Control findThemeControl() {
		return null;
	}

	void fixShell(Shell newShell, Control control) {
		if (this == newShell) {
			return;
		}
		if (control == lastActive) {
			setActiveControl(null);
		}
	}

	/**
	 * If the receiver is visible, moves it to the top of the drawing order for
	 * the display on which it was created (so that all other shells on that
	 * display, which are not the receiver's children will be drawn behind it)
	 * and forces the window manager to make the shell active.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * @see Control#moveAbove
	 * @see Control#setFocus
	 * @see Control#setVisible
	 * @see Display#getActiveShell
	 * @see Decorations#setDefaultButton(Button)
	 * @see Shell#open
	 * @see Shell#setActive
	 */
	public void forceActive() {
		checkWidget();
		if (!isVisible()) {
			return;
		}
		bringToTop();
	}

	void forceResize() {
		/* Do nothing */
	}

	/**
	 * Returns the receiver's alpha value. The alpha value is between 0
	 * (transparent) and 255 (opaque).
	 * 
	 * @return the alpha value
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
	public int getAlpha() {
		checkWidget();
		double opacity = getQWidget().windowOpacity();
		return (int) (255 * opacity);
	}

	@Override
	public Rectangle getBounds() {
		checkWidget();
		QPoint pos = getWindowControl().pos();
		QSize size = getWindowControl().size();
		return new Rectangle(pos.x(), pos.y(), size.width(), size.height());
	}

	@Override
	public Rectangle getClientArea() {
		checkWidget();
		if (!isVisible()) {
			updateQLayouts();
		}

		Rectangle clientArea = QtSWTConverter.convert(getQWidget().rect());
		if (clientArea.width < 0) {
			clientArea.width = 0;
		}
		if (clientArea.height < 0) {
			clientArea.height = 0;
		}

		return clientArea;
	}

	/**
	 * Returns <code>true</code> if the receiver is currently in fullscreen
	 * state, and false otherwise.
	 * <p>
	 * 
	 * @return the fullscreen state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public boolean getFullScreen() {
		checkWidget();
		return fullScreen;
	}

	/**
	 * Returns the receiver's input method editor mode. This will be the result
	 * of bitwise OR'ing together one or more of the following constants defined
	 * in class <code>SWT</code>: <code>NONE</code>, <code>ROMAN</code>,
	 * <code>DBCS</code>, <code>PHONETIC</code>, <code>NATIVE</code>,
	 * <code>ALPHA</code>.
	 * 
	 * @return the IME mode
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
	 */
	public int getImeInputMode() {
		checkWidget();
		// TODO how to do it with Qt?
		return SWT.NONE;
	}

	@Override
	public Point getLocation() {
		checkWidget();
		if (getWindowControl().isMinimized()) {
			return super.getLocation();
		}
		return QtSWTConverter.convert(getWindowControl().pos());
	}

	@Override
	public boolean getMaximized() {
		checkWidget();
		return !fullScreen && super.getMaximized();
	}

	/**
	 * Returns a point describing the minimum receiver's size. The x coordinate
	 * of the result is the minimum width of the receiver. The y coordinate of
	 * the result is the minimum height of the receiver.
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
	 * 
	 * @since 3.1
	 */
	public Point getMinimumSize() {
		checkWidget();
		return QtSWTConverter.convert(getWindowControl().minimumSize());
	}

	/**
	 * Gets the receiver's modified state.
	 * 
	 * </ul>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.5
	 */
	public boolean getModified() {
		checkWidget();
		return modified;
	}

	/**
	 * Returns the region that defines the shape of the shell, or null if the
	 * shell has the default shape.
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
	 * @since 3.0
	 * 
	 */
	@Override
	public Region getRegion() {
		/* This method is needed for the @since 3.0 Javadoc */
		checkWidget();
		return region;
	}

	@Override
	public Shell getShell() {
		checkWidget();
		return this;
	}

	@Override
	public Point getSize() {
		checkWidget();
		return QtSWTConverter.convert(getWindowControl().size());
	}

	/**
	 * Returns an array containing all shells which are descendants of the
	 * receiver.
	 * <p>
	 * 
	 * @return the dialog shells
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public Shell[] getShells() {
		checkWidget();
		int count = 0;
		Shell[] shells = display.getShells();
		for (int i = 0; i < shells.length; i++) {
			Control shell = shells[i];
			do {
				shell = shell.parent;
			} while (shell != null && shell != this);
			if (shell == this) {
				count++;
			}
		}
		int index = 0;
		Shell[] result = new Shell[count];
		for (int i = 0; i < shells.length; i++) {
			Control shell = shells[i];
			do {
				shell = shell.parent;
			} while (shell != null && shell != this);
			if (shell == this) {
				result[index++] = shells[i];
			}
		}
		return result;
	}

	/**
	 * Returns the instance of the ToolBar object representing the tool bar that can appear on the
	 * trim of the shell. This will return <code>null</code> if the platform does not support tool bars that
	 * not part of the content area of the shell, or if the style of the shell does not support a 
	 * tool bar. 
	 * <p>
	 * 
	 * @return a ToolBar object representing the window's tool bar or null.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 * 
	 * @since 3.7
	 */
	public ToolBar getToolBar() {
		return null;
	}

	@Override
	protected Composite findDeferredControl() {
		return layoutCount > 0 ? this : null;
	}

	//	@Override
	//	public boolean isEnabled() {
	//		checkWidget();
	//		return getEnabled();
	//}

	/**
	 * Moves the receiver to the top of the drawing order for the display on
	 * which it was created (so that all other shells on that display, which are
	 * not the receiver's children will be drawn behind it), marks it visible,
	 * sets the focus and asks the window manager to make the shell active.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Control#moveAbove
	 * @see Control#setFocus
	 * @see Control#setVisible
	 * @see Display#getActiveShell
	 * @see Decorations#setDefaultButton(Button)
	 * @see Shell#setActive
	 * @see Shell#forceActive
	 */
	public void open() {
		checkWidget();
		updateLayout();
		setVisible(true);
		bringToTop();
		if (isDisposed()) {
			return;
		}
		if (!restoreFocus() && !traverseGroup(true)) {
			setFocus(FocusReason.OtherFocusReason);
		}
	}

	@Override
	public boolean print(GC gc) {
		checkWidget();
		if (gc == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (gc.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		//TODO
		return false;
	}

	@Override
	void releaseChildren(boolean destroy) {
		Shell[] shells = getShells();
		for (int i = 0; i < shells.length; i++) {
			Shell shell = shells[i];
			if (shell != null && !shell.isDisposed()) {
				shell.release(false);
			}
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseParent() {
		/* Do nothing */
	}

	@Override
	void releaseQWidget() {
		getWindowControl().close();
		super.releaseQWidget();
		mainWindow = null;
		dialogWindow = null;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		lastActive = null;
	}

	@Override
	void removeMenu(Menu menu) {
		super.removeMenu(menu);
		if (menu == activeMenu) {
			activeMenu = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when operations are performed on the receiver.
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
	 * @see ShellListener
	 * @see #addShellListener
	 */
	public void removeShellListener(ShellListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Close, listener);
		eventTable.unhook(SWT.Iconify, listener);
		eventTable.unhook(SWT.Deiconify, listener);
		eventTable.unhook(SWT.Activate, listener);
		eventTable.unhook(SWT.Deactivate, listener);
	}

	/**
	 * If the receiver is visible, moves it to the top of the drawing order for
	 * the display on which it was created (so that all other shells on that
	 * display, which are not the receiver's children will be drawn behind it)
	 * and asks the window manager to make the shell active
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 * @see Control#moveAbove
	 * @see Control#setFocus
	 * @see Control#setVisible
	 * @see Display#getActiveShell
	 * @see Decorations#setDefaultButton(Button)
	 * @see Shell#open
	 * @see Shell#setActive
	 */
	public void setActive() {
		checkWidget();
		if (!isVisible()) {
			return;
		}
		bringToTop();
		// widget could be disposed at this point
	}

	void setActiveControl(Control control) {
		if (control != null && control.isDisposed()) {
			control = null;
		}
		if (lastActive != null && lastActive.isDisposed()) {
			lastActive = null;
		}
		if (lastActive == control) {
			return;
		}

		/*
		 * Compute the list of controls to be activated and deactivated by
		 * finding the first common parent control.
		 */
		Control[] activate = control == null ? new Control[0] : control.getPath();
		Control[] deactivate = lastActive == null ? new Control[0] : lastActive.getPath();
		lastActive = control;
		int index = 0, length = Math.min(activate.length, deactivate.length);
		while (index < length) {
			if (activate[index] != deactivate[index]) {
				break;
			}
			index++;
		}

		/*
		 * It is possible (but unlikely), that application code could have
		 * destroyed some of the widgets. If this happens, keep processing those
		 * widgets that are not disposed.
		 */
		for (int i = deactivate.length - 1; i >= index; --i) {
			if (!deactivate[i].isDisposed()) {
				deactivate[i].sendEvent(SWT.Deactivate);
			}
		}
		for (int i = activate.length - 1; i >= index; --i) {
			if (!activate[i].isDisposed()) {
				activate[i].sendEvent(SWT.Activate);
			}
		}
	}

	/**
	 * Sets the receiver's alpha value which must be between 0 (transparent) and
	 * 255 (opaque).
	 * <p>
	 * This operation requires the operating system's advanced widgets subsystem
	 * which may not be available on some platforms.
	 * </p>
	 * 
	 * @param alpha
	 *            the alpha value
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
	public void setAlpha(int alpha) {
		checkWidget();
		if (alpha < 0 || alpha > 255) {
			return;
		}
		getQWidget().setWindowOpacity((double) alpha / 255);
	}

	@Override
	protected void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		if (fullScreen) {
			setFullScreen(false);
		}

		Rectangle geometry = QtSWTConverter.convert(getWindowControl().frameGeometry());
		if (move) {
			int oldX = geometry.x;
			int oldY = geometry.y;
			boolean moved = oldX != x || oldY != y;
			if (moved) {
				getWindowControl().move(x, y);
			}
		}

		if (resize) {
			int oldW = geometry.width;
			int oldH = geometry.height;
			boolean resized = oldW != width || oldH != height;

			if (resized) {
				if ((style & SWT.RESIZE) == 0) {
					unlockSize();
				}

				getWindowControl().resize(width, height);

				if ((style & SWT.RESIZE) == 0) {
					getWindowControl().setFixedSize(Math.max(0, width), Math.max(0, height));
				}
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		checkWidget();
		if (super.getEnabled() == enabled) {
			return;
		}
		super.setEnabled(enabled);
		if (enabled && getWindowControl().isActiveWindow()) {
			if (!restoreFocus()) {
				traverseGroup(true);
			}
		}
	}

	/**
	 * Sets the full screen state of the receiver. If the argument is
	 * <code>true</code> causes the receiver to switch to the full screen state,
	 * and if the argument is <code>false</code> and the receiver was previously
	 * switched into full screen state, causes the receiver to switch back to
	 * either the maximmized or normal states.
	 * <p>
	 * Note: The result of intermixing calls to <code>setFullScreen(true)</code>, <code>setMaximized(true)</code> and <code>setMinimized(true)</code>
	 * will vary by platform. Typically, the behavior will match the platform
	 * user's expectations, but not always. This should be avoided if possible.
	 * </p>
	 * 
	 * @param fullScreen
	 *            the new fullscreen state
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
	public void setFullScreen(boolean fullScreen) {
		checkWidget();
		if (this.fullScreen == fullScreen) {
			return;
		}
		this.fullScreen = fullScreen;
		if (fullScreen) {
			getWindowControl().showFullScreen();
		} else {
			getWindowControl().showNormal();
		}
	}

	/**
	 * Sets the input method editor mode to the argument which should be the
	 * result of bitwise OR'ing together one or more of the following constants
	 * defined in class <code>SWT</code>: <code>NONE</code>, <code>ROMAN</code>,
	 * <code>DBCS</code>, <code>PHONETIC</code>, <code>NATIVE</code>,
	 * <code>ALPHA</code>.
	 * 
	 * @param mode
	 *            the new IME mode
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
	 */
	public void setImeInputMode(int mode) {
		checkWidget();
		// TODO
	}

	/**
	 * Sets the receiver's minimum size to the size specified by the arguments.
	 * If the new minimum size is larger than the current size of the receiver,
	 * the receiver is resized to the new minimum size.
	 * 
	 * @param width
	 *            the new minimum width for the receiver
	 * @param height
	 *            the new minimum height for the receiver
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
	public void setMinimumSize(int width, int height) {
		checkWidget();
		getQWidget().setMinimumSize(width, height);
	}

	/**
	 * Sets the receiver's minimum size to the size specified by the argument.
	 * If the new minimum size is larger than the current size of the receiver,
	 * the receiver is resized to the new minimum size.
	 * 
	 * @param size
	 *            the new minimum size for the receiver
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
	 * 
	 * @since 3.1
	 */
	public void setMinimumSize(Point size) {
		checkWidget();
		if (size == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setMinimumSize(size.x, size.y);
	}

	/**
	 * Sets the receiver's modified state as specified by the argument.
	 * 
	 * @param modified
	 *            the new modified state for the receiver
	 * 
	 *            </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.5
	 */
	public void setModified(boolean modified) {
		checkWidget();
		this.modified = modified;
	}

	/**
	 * Sets the shape of the shell to the region specified by the argument. When
	 * the argument is null, the default shape of the shell is restored. The
	 * shell must be created with the style SWT.NO_TRIM in order to specify a
	 * region.
	 * 
	 * @param region
	 *            the region that defines the shape of the shell (or null)
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
	 * @since 3.0
	 * 
	 */
	@Override
	public void setRegion(Region region) {
		checkWidget();
		if ((style & SWT.NO_TRIM) == 0) {
			return;
		}
		super.setRegion(region);
	}

	@Override
	public void setVisible(boolean visible) {
		checkWidget();
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
		if ((style & mask) != 0) {
			if (visible) {
				display.setModalShell(this);
				if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
					display.setModalDialog(null);
				}
				Control control = display.getFocusControl();
				if (control != null && !control.isActive()) {
					bringToTop();
					if (isDisposed()) {
						return;
					}
				}
				getWindowControl().setWindowModality(getModalityFromStyle());
				QWidget mouseGrabber = QWidget.mouseGrabber();
				if (mouseGrabber != null) {
					mouseGrabber.releaseMouse();
				}
			} else {
				display.clearModal(this);
			}
		} else {
			updateModal();
		}

		_setVisible(getWindowControl(), visible);
	}

	private WindowModality getModalityFromStyle() {
		if ((style & SWT.SYSTEM_MODAL) != 0) {
			return WindowModality.ApplicationModal;
		}
		if ((style & SWT.APPLICATION_MODAL) != 0) {
			return WindowModality.ApplicationModal;
		}
		if ((style & SWT.PRIMARY_MODAL) != 0) {
			return WindowModality.ApplicationModal;
		}
		return WindowModality.NonModal;
	}

	@Override
	boolean traverseEscape() {
		if (parent == null) {
			return false;
		}
		if (!isVisible() || !isEnabled()) {
			return false;
		}
		close();
		return true;
	}

	void updateModal() {
	}

	@Override
	public boolean qtCloseEvent() {
		return closeWidget();
	}

	@Override
	public boolean qtWindowStateChangeEvent(QObject source, QWindowStateChangeEvent event) {
		if (source != getWindowControl()) {
			return false;
		}
		WindowStates oldState = event.oldState();
		WindowStates newState = getWindowControl().windowState();
		if (oldState.isSet(WindowState.WindowMinimized) && !newState.isSet(WindowState.WindowMinimized)) {
			sendEvent(SWT.Deiconify);
			return false;
		}
		if (!oldState.isSet(WindowState.WindowMinimized) && newState.isSet(WindowState.WindowMinimized)) {
			sendEvent(SWT.Iconify);
			menuShell().saveFocus();
			return false;
		}
		return false;
	}

	@Override
	public boolean qtMouseButtonPressEvent(QObject source, QMouseEvent mouseEvent) {
		if (source == getQWidget()) {
			QWidget clickTarget = getQWidget().childAt(mouseEvent.x(), mouseEvent.y());
			if (clickTarget != null && clickTarget != getQWidget()) {
				Widget swtControl = display.findControl(clickTarget);
				if (swtControl instanceof Control) {
					setActiveControl((Control) swtControl);
				}
			}
		}
		return false;
	}

}
