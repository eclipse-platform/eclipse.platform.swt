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
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.WindowState;
import com.trolltech.qt.core.Qt.WindowStates;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class provide the appearance and behavior of
 * <code>Shells</code>, but are not top level shells or dialogs. Class
 * <code>Shell</code> shares a significant amount of code with this class, and
 * is a subclass.
 * <p>
 * IMPORTANT: This class was intended to be abstract and should <em>never</em>
 * be referenced or instantiated. Instead, the class <code>Shell</code> should
 * be used.
 * </p>
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
 * Note: The styles supported by this class must be treated as <em>HINT</em>s,
 * since the window manager for the desktop on which the instance is visible has
 * ultimate control over the appearance and behavior of decorations. For
 * example, some window managers only support resizable windows and will always
 * assume the RESIZE style, even if it is not set.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
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
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see #getMinimized
 * @see #getMaximized
 * @see Shell
 * @see SWT
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Decorations extends Canvas {
	private static final int WIDGETSIZE_MAX = 16777215;
	Image image, smallImage, largeImage;
	Image[] images;
	Menu menuBar;
	Menu[] menus;
	Control savedFocus;
	Button defaultButton, saveDefault;
	int swFlags, nAccel;
	boolean moved, resized, opened;
	private int restoreState;
	private Point fixedSize;
	static int topTitleFrame = -1;
	static int leftTitleFrame = -1;
	static int rightTitleFrame = -1;
	static int bottomTitleFrame = -1;
	static int topThinFrame = -1;
	static int leftThinFrame = -1;
	static int rightThinFrame = -1;
	static int bottomThinFrame = -1;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Decorations() {
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
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Decorations(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	protected QWidget getWindowControl() {
		return getQWidget();
	}

	QWidget getMenuContainer() {
		return getWindowControl();
	}

	void addMenu(Menu menu) {
		if (menus == null) {
			menus = new Menu[4];
		}
		for (int i = 0; i < menus.length; i++) {
			if (menus[i] == null) {
				menus[i] = menu;
				return;
			}
		}
		Menu[] newMenus = new Menu[menus.length + 4];
		newMenus[menus.length] = menu;
		System.arraycopy(menus, 0, newMenus, 0, menus.length);
		menus = newMenus;
	}

	void bringToTop() {
		if (isDisposed()) {
			return;
		}
		getWindowControl().activateWindow();
		getWindowControl().raise();
	}

	static int checkStyle(int style) {
		if ((style & SWT.NO_TRIM) != 0) {
			style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
		}
		if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
			style |= SWT.TITLE;
		}

		/*
		 * If either WS_MINIMIZEBOX or WS_MAXIMIZEBOX are set, we must also set
		 * WS_SYSMENU or the buttons will not appear.
		 */
		if ((style & (SWT.MIN | SWT.MAX)) != 0) {
			style |= SWT.CLOSE;
		}

		/*
		 * Both WS_SYSMENU and WS_CAPTION must be set in order to for the system
		 * menu to appear.
		 */
		if ((style & SWT.CLOSE) != 0) {
			style |= SWT.TITLE;
		}

		/*
		 * Bug in Windows. The WS_CAPTION style must be set when the window is
		 * resizable or it does not draw properly.
		 */
		/*
		 * This code is intentionally commented. It seems that this problem
		 * originally in Windows 3.11, has been fixed in later versions. Because
		 * the exact nature of the drawing problem is unknown, keep the
		 * commented code around in case it comes back.
		 */
		// if ((style & SWT.RESIZE) != 0) style |= SWT.TITLE;

		return style;
	}

	@Override
	protected void checkAndUpdateBorder() {
		/* Do nothing */
	}

	@Override
	void checkOpened() {
		if (!opened) {
			resized = false;
		}
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	boolean closeWidget() {
		Event event = new Event();
		event.doit = true;
		sendEvent(SWT.Close, event);
		if (event.doit && !isDisposed()) {
			dispose();
		}
		if (isDisposed()) {
			return false;
		}
		return !event.doit;
	}

	int compare(ImageData data1, ImageData data2, int width, int height, int depth) {
		int value1 = Math.abs(data1.width - width), value2 = Math.abs(data2.width - width);
		if (value1 == value2) {
			int transparent1 = data1.getTransparencyType();
			int transparent2 = data2.getTransparencyType();
			if (transparent1 == transparent2) {
				if (data1.depth == data2.depth) {
					return 0;
				}
				return data1.depth > data2.depth && data1.depth <= depth ? -1 : 1;
			}
			if (transparent1 == SWT.TRANSPARENCY_ALPHA) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_ALPHA) {
				return 1;
			}
			if (transparent1 == SWT.TRANSPARENCY_MASK) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_MASK) {
				return 1;
			}
			if (transparent1 == SWT.TRANSPARENCY_PIXEL) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_PIXEL) {
				return 1;
			}
			return 0;
		}
		return value1 < value2 ? -1 : 1;
	}

	@Override
	Control computeTabGroup() {
		return this;
	}

	@Override
	Control computeTabRoot() {
		return this;
	}

	@Override
	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();

		Rectangle trim = new Rectangle(x, y, width, height);

		QRect outer = getQWidget().frameGeometry();
		QRect inner = getQWidget().geometry();

		int leftTrim, rightTrim;
		leftTrim = rightTrim = outer.x() - inner.x();
		int topTrim = inner.y() - outer.y();
		int bottomTrim = outer.y() - inner.y();

		trim.x -= leftTrim;
		trim.width += leftTrim + rightTrim;
		trim.y -= topTrim;
		trim.height += topTrim + bottomTrim;

		return trim;
	}

	@Override
	public void dispose() {
		if (isDisposed()) {
			return;
		}
		if (!isValidThread()) {
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if (!(this instanceof Shell)) {
			if (!traverseDecorations(true)) {
				Shell shell = getShell();
				shell.setFocus();
			}
			setVisible(false);
		}
		super.dispose();
	}

	void fixDecorations(Decorations newDecorations, Control control, Menu[] menus) {
		if (this == newDecorations) {
			return;
		}
		if (control == savedFocus) {
			savedFocus = null;
		}
		if (control == defaultButton) {
			defaultButton = null;
		}
		if (control == saveDefault) {
			saveDefault = null;
		}
		if (menus == null) {
			return;
		}
		Menu menu = control.getMenu();
		if (menu != null) {
			int index = 0;
			while (index < menus.length) {
				if (menus[index] == menu) {
					control.setMenu(null);
					return;
				}
				index++;
			}
			menu.fixMenus(newDecorations);
		}
	}

	/**
	 * Returns the receiver's default button if one had previously been set,
	 * otherwise returns null.
	 * 
	 * @return the default button or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setDefaultButton(Button)
	 */
	public Button getDefaultButton() {
		checkWidget();
		return defaultButton;
	}

	/**
	 * Returns the receiver's image if it had previously been set using
	 * <code>setImage()</code>. The image is typically displayed by the window
	 * manager when the instance is marked as iconified, and may also be
	 * displayed somewhere in the trim when the instance is in normal or
	 * maximized states.
	 * <p>
	 * Note: This method will return null if called before
	 * <code>setImage()</code> is called. It does not provide access to a window
	 * manager provided, "default" image even if one exists.
	 * </p>
	 * 
	 * @return the image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return image;
	}

	/**
	 * Returns the receiver's images if they had previously been set using
	 * <code>setImages()</code>. Images are typically displayed by the window
	 * manager when the instance is marked as iconified, and may also be
	 * displayed somewhere in the trim when the instance is in normal or
	 * maximized states. Depending where the icon is displayed, the platform
	 * chooses the icon with the "best" attributes. It is expected that the
	 * array will contain the same icon rendered at different sizes, with
	 * different depth and transparency attributes.
	 * 
	 * <p>
	 * Note: This method will return an empty array if called before
	 * <code>setImages()</code> is called. It does not provide access to a
	 * window manager provided, "default" image even if one exists.
	 * </p>
	 * 
	 * @return the images
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
	 */
	public Image[] getImages() {
		checkWidget();
		if (images == null) {
			return new Image[0];
		}
		Image[] result = new Image[images.length];
		System.arraycopy(images, 0, result, 0, images.length);
		return result;
	}

	/**
	 * Returns <code>true</code> if the receiver is currently maximized, and
	 * false otherwise.
	 * <p>
	 * 
	 * @return the maximized state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setMaximized
	 */
	public boolean getMaximized() {
		checkWidget();
		return getWindowControl().isMaximized();
	}

	/**
	 * Returns the receiver's menu bar if one had previously been set, otherwise
	 * returns null.
	 * 
	 * @return the menu bar or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Menu getMenuBar() {
		checkWidget();
		return menuBar;
	}

	/**
	 * Returns <code>true</code> if the receiver is currently minimized, and
	 * false otherwise.
	 * <p>
	 * 
	 * @return the minimized state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setMinimized
	 */
	public boolean getMinimized() {
		checkWidget();
		return getWindowControl().isMinimized();
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's text, which is the string that the window manager
	 * will typically display as the receiver's <em>title</em>. If the text has
	 * not previously been set, returns an empty string.
	 * 
	 * @return the text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		return getWindowControl().windowTitle();
	}

	@Override
	public boolean qtCloseEvent() {
		if (isEnabled()) {
			closeWidget();
		}
		return true;
	}

	@Override
	public boolean isReparentable() {
		checkWidget();
		/*
		 * Feature in Windows. Calling SetParent() for a shell causes a kind of
		 * fake MDI to happen. It doesn't work well on Windows and is not
		 * supported on the other platforms. The fix is to disallow the
		 * SetParent().
		 */
		return false;
	}

	@Override
	boolean isTabGroup() {
		/*
		 * Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
		 */
		return true;
	}

	@Override
	boolean isTabItem() {
		/*
		 * Can't test WS_TAB bits because they are the same as WS_MAXIMIZEBOX.
		 */
		return false;
	}

	@Override
	Decorations menuShell() {
		return this;
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (menuBar != null) {
			menuBar.release(false);
			menuBar = null;
		}
		super.releaseChildren(destroy);
		if (menus != null) {
			for (int i = 0; i < menus.length; i++) {
				Menu menu = menus[i];
				if (menu != null && !menu.isDisposed()) {
					menu.dispose();
				}
			}
			menus = null;
		}
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		if (smallImage != null) {
			smallImage.dispose();
		}
		if (largeImage != null) {
			largeImage.dispose();
		}
		smallImage = largeImage = image = null;
		images = null;
		savedFocus = null;
		defaultButton = saveDefault = null;
	}

	void removeMenu(Menu menu) {
		if (menus == null) {
			return;
		}
		for (int i = 0; i < menus.length; i++) {
			if (menus[i] == menu) {
				menus[i] = null;
				return;
			}
		}
	}

	boolean restoreFocus() {
		if (savedFocus != null && savedFocus.isDisposed()) {
			savedFocus = null;
		}
		return savedFocus != null && savedFocus.setSavedFocus();
	}

	void saveFocus() {
		Control control = display.getFocusControl();
		if (control != null && control != this && this == control.menuShell()) {
			setSavedFocus(control);
		}
	}

	protected void unlockSize() {
		getWindowControl().setMaximumSize(WIDGETSIZE_MAX, WIDGETSIZE_MAX);
		getWindowControl().setMinimumSize(0, 0);
	}

	protected boolean sizeFixed() {
		return getWindowControl().minimumSize().equals(getQWidget().maximumSize());
	}

	protected void setRestoreState(int state, boolean restore) {
		int oldState = getWindowControl().windowState().value();
		if (restore) {
			if ((oldState & state) == 0) {
				return;
			}
			restoreState();
		} else {
			setState(state);
		}
	}

	protected void restoreState() {
		// This might not restore it from minimized if wm doesn't allow it.
		// It might e.g. blink in the task bar to indicate to the user
		// that the window wants to become active.
		if (restoreState == WindowState.WindowMinimized.value()) {
			setState(WindowState.WindowMinimized.value());
		} else if (restoreState == WindowState.WindowMaximized.value()) {
			setState(WindowState.WindowMaximized.value());
		} else {
			setState(WindowState.WindowNoState.value());
		}
		bringToTop();
		restoreState = 0;
	}

	protected void storeState(int state) {
		if ((state & WindowState.WindowMinimized.value()) != 0) {
			restoreState = WindowState.WindowMinimized.value();
		} else if ((state & WindowState.WindowMaximized.value()) != 0) {
			restoreState = WindowState.WindowMaximized.value();
		} else {
			restoreState = 0;
		}
	}

	protected void setState(final int state) {
		final int oldState = getWindowControl().windowState().value();
		if ((oldState & state) != 0) {
			return;
		}
		storeState(oldState);
		int newState = oldState;
		int statesToRemove = (WindowState.WindowFullScreen.value() | WindowState.WindowMaximized.value() | WindowState.WindowMinimized
				.value())
				& ~state;
		newState &= ~statesToRemove;
		newState |= state;

		// Full screen and maximize won't work if size is fixed. The fixed size
		// needs to be stored and then restored when going back to normal state.
		if ((newState & (WindowState.WindowFullScreen.value() | WindowState.WindowMaximized.value())) != 0
				&& (oldState & (WindowState.WindowFullScreen.value() | WindowState.WindowMaximized.value())) == 0) {
			if (sizeFixed()) {
				fixedSize = QtSWTConverter.convert(getWindowControl().minimumSize());
				style |= SWT.Resize;
				unlockSize();
			}
		}

		// Going directly from full screen to maximized doesn't work. Need to go
		// normal first.
		if ((newState & WindowState.WindowMaximized.value()) != 0
				&& (oldState & WindowState.WindowFullScreen.value()) != 0) {
			getWindowControl().setWindowState(WindowState.WindowNoState);
		}

		getWindowControl().setWindowState(new WindowStates(newState));

		// Restore the fixed size if back to normal
		if ((newState & (WindowState.WindowFullScreen.value() | WindowState.WindowMaximized.value())) == 0) {
			if (fixedSize != null) {
				getWindowControl().setFixedSize(fixedSize.x, fixedSize.y);
				style &= ~SWT.Resize;
				fixedSize = null;
			}
		}
	}

	/**
	 * If the argument is not null, sets the receiver's default button to the
	 * argument, and if the argument is null, sets the receiver's default button
	 * to the first button which was set as the receiver's default button
	 * (called the <em>saved default button</em>). If no default button had
	 * previously been set, or the saved default button was disposed, the
	 * receiver's default button will be set to null.
	 * <p>
	 * The default button is the button that is selected when the receiver is
	 * active and the user presses ENTER.
	 * </p>
	 * 
	 * @param button
	 *            the new default button
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the button has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_PARENT - if the control is not in the
	 *                same widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setDefaultButton(Button button) {
		checkWidget();
		if (button != null) {
			if (button.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (button.menuShell() != this) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		setDefaultButton(button, true);
	}

	void setDefaultButton(Button button, boolean save) {
		if (button == null) {
			if (defaultButton == saveDefault) {
				if (save) {
					saveDefault = null;
				}
				return;
			}
		} else {
			if ((button.style & SWT.PUSH) == 0) {
				return;
			}
			if (button == defaultButton) {
				return;
			}
		}
		if (defaultButton != null) {
			if (!defaultButton.isDisposed()) {
				defaultButton.setDefault(false);
			}
		}
		if ((defaultButton = button) == null) {
			defaultButton = saveDefault;
		}
		if (defaultButton != null) {
			if (!defaultButton.isDisposed()) {
				defaultButton.setDefault(true);
			}
		}
		if (save) {
			saveDefault = defaultButton;
		}
		if (saveDefault != null && saveDefault.isDisposed()) {
			saveDefault = null;
		}
	}

	/**
	 * Sets the receiver's image to the argument, which may be null. The image
	 * is typically displayed by the window manager when the instance is marked
	 * as iconified, and may also be displayed somewhere in the trim when the
	 * instance is in normal or maximized states.
	 * 
	 * @param image
	 *            the new image (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
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
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.image = image;
		if (image != null) {
			getWindowControl().setWindowIcon(image.getQIcon());
		}
	}

	/**
	 * Sets the receiver's images to the argument, which may be an empty array.
	 * Images are typically displayed by the window manager when the instance is
	 * marked as iconified, and may also be displayed somewhere in the trim when
	 * the instance is in normal or maximized states. Depending where the icon
	 * is displayed, the platform chooses the icon with the "best" attributes.
	 * It is expected that the array will contain the same icon rendered at
	 * different sizes, with different depth and transparency attributes.
	 * 
	 * @param images
	 *            the new image array
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if one of the images is null
	 *                or has been disposed</li>
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
	 */
	public void setImages(Image[] images) {
		checkWidget();
		if (images == null) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < images.length; i++) {
			if (images[i] == null || images[i].isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		this.images = images;
		this.images = images;
		if (parent != null) {
			return;
		}
		if (images != null && images.length > 1) {
			Image[] bestImages = new Image[images.length];
			System.arraycopy(images, 0, bestImages, 0, images.length);
			sort(bestImages);
			images = bestImages;
		}
		if (images.length > 0) {
			getWindowControl().setWindowIcon(images[0].getQIcon());
		} else {
			getWindowControl().setWindowIcon(null);
		}
	}

	/**
	 * Sets the maximized state of the receiver. If the argument is
	 * <code>true</code> causes the receiver to switch to the maximized state,
	 * and if the argument is <code>false</code> and the receiver was previously
	 * maximized, causes the receiver to switch back to either the minimized or
	 * normal states.
	 * <p>
	 * Note: The result of intermixing calls to <code>setMaximized(true)</code>
	 * and <code>setMinimized(true)</code> will vary by platform. Typically, the
	 * behavior will match the platform user's expectations, but not always.
	 * This should be avoided if possible.
	 * </p>
	 * 
	 * @param maximized
	 *            the new maximized state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setMinimized
	 */
	public void setMaximized(boolean maximized) {
		checkWidget();
		if (maximized) {
			getWindowControl().showMaximized();

		} else {
			getWindowControl().showNormal();
		}
	}

	/**
	 * Sets the receiver's menu bar to the argument, which may be null.
	 * 
	 * @param menu
	 *            the new menu bar
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed
	 *                </li>
	 *                <li>ERROR_INVALID_PARENT - if the menu is not in the same
	 *                widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMenuBar(Menu menu) {
		checkWidget();
		if (menuBar == menu) {
			return;
		}
		if (menu != null) {
			if (menu.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if ((menu.style & SWT.BAR) == 0) {
				error(SWT.ERROR_MENU_NOT_BAR);
			}
			if (menu.parent != this) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		if (menuBar != null) {
			menuBar.dispose();
		}
		menuBar = menu;
		if (parent == null) { // window
			((QMainWindow) getMenuContainer()).setMenuBar(menu.getQMenuBar());
		} else {
			QLayout layout = getWindowControl().layout();
			layout.setMenuBar(menu.getQWidget());
		}

	}

	/**
	 * Sets the minimized stated of the receiver. If the argument is
	 * <code>true</code> causes the receiver to switch to the minimized state,
	 * and if the argument is <code>false</code> and the receiver was previously
	 * minimized, causes the receiver to switch back to either the maximized or
	 * normal states.
	 * <p>
	 * Note: The result of intermixing calls to <code>setMaximized(true)</code>
	 * and <code>setMinimized(true)</code> will vary by platform. Typically, the
	 * behavior will match the platform user's expectations, but not always.
	 * This should be avoided if possible.
	 * </p>
	 * 
	 * @param minimized
	 *            the new maximized state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setMaximized
	 */
	public void setMinimized(boolean minimized) {
		checkWidget();
		if (minimized) {
			getWindowControl().showMinimized();

		} else {
			getWindowControl().showNormal();
		}
	}

	void setSavedFocus(Control control) {
		savedFocus = control;
	}

	/**
	 * Sets the receiver's text, which is the string that the window manager
	 * will typically display as the receiver's <em>title</em>, to the argument,
	 * which must not be null.
	 * 
	 * @param title
	 *            the new text
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(String title) {
		checkWidget();
		if (title == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		// fix for emtpy title
		if (title.length() == 0) {
			title = " "; //$NON-NLS-1$
		}
		getWindowControl().setWindowTitle(title);
		getWindowControl().setWindowIconText(title);
	}

	void sort(Image[] images) {
		/* Shell Sort from K&R, pg 108 */
		int length = images.length;
		if (length <= 1) {
			return;
		}
		ImageData[] datas = new ImageData[length];
		for (int i = 0; i < length; i++) {
			datas[i] = images[i].getImageData();
		}
		for (int gap = length / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < length; i++) {
				for (int j = i - gap; j >= 0; j -= gap) {
					if (compare(datas[j], datas[j + gap]) >= 0) {
						Image swap = images[j];
						images[j] = images[j + gap];
						images[j + gap] = swap;
						ImageData swapData = datas[j];
						datas[j] = datas[j + gap];
						datas[j + gap] = swapData;
					}
				}
			}
		}
	}

	int compare(ImageData data1, ImageData data2) {
		if (data1.width == data2.width && data1.height == data2.height) {
			int transparent1 = data1.getTransparencyType();
			int transparent2 = data2.getTransparencyType();
			if (transparent1 == SWT.TRANSPARENCY_ALPHA) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_ALPHA) {
				return 1;
			}
			if (transparent1 == SWT.TRANSPARENCY_MASK) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_MASK) {
				return 1;
			}
			if (transparent1 == SWT.TRANSPARENCY_PIXEL) {
				return -1;
			}
			if (transparent2 == SWT.TRANSPARENCY_PIXEL) {
				return 1;
			}
			return 0;
		}
		return data1.width > data2.width || data1.height > data2.height ? -1 : 1;
	}

	boolean traverseDecorations(boolean next) {
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
		int start = index, offset = next ? 1 : -1;
		while ((index = (index + offset + length) % length) != start) {
			Control child = children[index];
			if (!child.isDisposed() && child instanceof Decorations) {
				if (child.setFocus()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	boolean traverseItem(boolean next) {
		return false;
	}

	@Override
	boolean traverseReturn() {
		if (defaultButton == null || defaultButton.isDisposed()) {
			return false;
		}
		if (!defaultButton.isVisible() || !defaultButton.isEnabled()) {
			return false;
		}
		defaultButton.click();
		return true;
	}

	@Override
	public void qtFocusInEvent(QObject source) {
		if (source != getQMasterWidget()) {
			return;
		}
		super.qtFocusInEvent(source);
		if (savedFocus != this) {
			restoreFocus();
		}
	}

	@Override
	public void qtFocusOutEvent(QObject source) {
		if (source != getQMasterWidget()) {
			return;
		}
		super.qtFocusInEvent(source);
		if (savedFocus != this) {
			saveFocus();
		}
	}

	@Override
	public boolean qtWindowActivateEvent(QObject source) {
		if (source == getWindowControl()) {
			sendEvent(SWT.Activate);
		}
		return false;
	}

	@Override
	public boolean qtWindowDeactivateEvent(QObject source) {
		if (source == getWindowControl()) {
			sendEvent(SWT.Deactivate);
			saveFocus();
		}
		return false;
	}

}
