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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QMessageHandler;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QTime;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QEventLoop.ProcessEventsFlags;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.Qt.KeyboardModifiers;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPaintDeviceInterface;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPicture;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWindowStateChangeEvent;
import com.trolltech.qt.gui.QStyle.StandardPixmap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.KeyUtil;
import org.eclipse.swt.internal.qt.QtSWTConverter;
import org.eclipse.swt.internal.qt.SWQT;

/**
 * Instances of this class are responsible for managing the connection between
 * SWT and the underlying operating system. Their most important function is to
 * implement the SWT event loop in terms of the platform event model. They also
 * provide various methods for accessing information about the operating system,
 * and have overall control over the operating system resources which SWT
 * allocates.
 * <p>
 * Applications which are built with SWT will <em>almost always</em> require
 * only a single display. In particular, some platforms which SWT supports will
 * not allow more than one <em>active</em> display. In other words, some
 * platforms do not support creating a new display if one already exists that
 * has not been sent the <code>dispose()</code> message.
 * <p>
 * In SWT, the thread which creates a <code>Display</code> instance is
 * distinguished as the <em>user-interface thread</em> for that display.
 * </p>
 * The user-interface thread for a particular display has the following special
 * attributes:
 * <ul>
 * <li>
 * The event loop for that display must be run from the thread.</li>
 * <li>
 * Some SWT API methods (notably, most of the public methods in
 * <code>Widget</code> and its subclasses), may only be called from the thread.
 * (To support multi-threaded user-interface applications, class
 * <code>Display</code> provides inter-thread communication methods which allow
 * threads other than the user-interface thread to request that it perform
 * operations on their behalf.)</li>
 * <li>
 * The thread is not allowed to construct other <code>Display</code>s until that
 * display has been disposed. (Note that, this is in addition to the restriction
 * mentioned above concerning platform support for multiple displays. Thus, the
 * only way to have multiple simultaneously active displays, even on platforms
 * which support it, is to have multiple threads.)</li>
 * </ul>
 * Enforcing these attributes allows SWT to be implemented directly on the
 * underlying operating system's event model. This has numerous benefits
 * including smaller footprint, better use of resources, safer memory
 * management, clearer program logic, better performance, and fewer overall
 * operating system threads required. The down side however, is that care must
 * be taken (only) when constructing multi-threaded applications to use the
 * inter-thread communication mechanisms which this class provides when
 * required. </p>
 * <p>
 * All SWT API methods which may only be called from the user-interface thread
 * are distinguished in their documentation by indicating that they throw the "
 * <code>ERROR_THREAD_INVALID_ACCESS</code>" SWT exception.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Close, Dispose, Settings</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see Device#dispose
 * @see <a href="http://www.eclipse.org/swt/snippets/#display">Display
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Display extends Device {

	Thread thread;
	int threadId;
	/* Windows and Events */
	private Event[] eventQueue;
	private EventTable eventTable, filterTable;

	/* Focus */
	int focusEvent;
	Control focusControl;

	/* Menus */
	private Menu[] bars;
	Menu[] popups;
	private MenuItem[] items;

	/* Settings */
	private static final int /* long */SETTINGS_ID = 100;

	/*
	 * The start value for WM_COMMAND id's. Windows reserves the values 0..100.
	 * 
	 * The SmartPhone SWT resource file reserves the values 101..107.
	 */
	private static final int ID_START = 108;

	/* Sync/Async Widget Communication */
	private Synchronizer synchronizer = new Synchronizer(this);

	/* Display Shutdown */
	private Runnable[] disposeList;

	/* System Tray */
	private Tray tray;

	/* Timers */
	private int /* long */[] timerIds;
	private Runnable[] timerList;
	private int /* long */nextTimerId = SETTINGS_ID + 1;

	/* Keyboard and Mouse */
	int lastKey;
	int lastAscii;
	boolean lastVirtual;
	boolean lastNull;
	boolean xMouse;

	/* System Resources */
	private Font systemFont;
	private Image errorImage, infoImage, questionImage, warningIcon;
	private Cursor[] cursors = new Cursor[SWT.CURSOR_HAND + 1];

	/* Display Data */
	private Object data;
	private String[] keys;
	private Object[] values;

	/* Multiple Displays */
	private static Display Default;
	private static Display[] Displays = new Display[4];

	/* Modality */
	Shell[] modalShells;
	private Dialog modalDialog;

	/* Qt specific fields */
	private String styleSheet;
	private Map<Object, Widget> qt2swtControlMap;
	private MasterEventFilter masterEventFilter;
	private int lastEventTime;
	private static final ProcessEventsFlags PROCESS_EVENTS_FLAGS = new ProcessEventsFlags(
			QEventLoop.ProcessEventsFlag.AllEvents);
	private QTimer timer;
	private final boolean ignorePaintEvents = false;
	private QMessageHandler messageHandler;

	/* Package Name */
	private static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets."; //$NON-NLS-1$

	/*
	 * TEMPORARY CODE. Install the runnable that gets the current display. This
	 * code will be removed in the future.
	 */
	static {
		DeviceFinder = new Runnable() {
			public void run() {
				Device device = getCurrent();
				if (device == null) {
					device = getDefault();
				}
				setDevice(device);
			}
		};
	}

	/*
	 * TEMPORARY CODE.
	 */
	static void setDevice(Device device) {
		CurrentDevice = device;
	}

	/**
	 * Constructs a new instance of this class.
	 * <p>
	 * Note: The resulting display is marked as the <em>current</em> display. If
	 * this is the first display which has been constructed since the
	 * application started, it is also marked as the <em>default</em> display.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if called from a thread
	 *                that already created an existing display</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see #getCurrent
	 * @see #getDefault
	 * @see Widget#checkSubclass
	 * @see Shell
	 */
	public Display() {
		this(null);
	}

	/**
	 * Constructs a new instance of this class using the parameter.
	 * 
	 * @param data
	 *            the device data
	 */
	public Display(DeviceData data) {
		super(data);
	}

	public String getStyleSheet() {
		return styleSheet;
	}

	protected void setStyleSheet(String style) {
		this.styleSheet = style;
		for (Shell shell : getShells()) {
			shell.setStyleSheet(style);
		}
		Event event = new Event();
		event.text = style;
		sendEvent(SWQT.StyleSheetChange, event);
	}

	private Control _getFocusControl() {
		Widget widget = findControl(QApplication.focusWidget());
		if (widget instanceof Control) {
			return (Control) widget;
		}
		return null;
	}

	void addControl(Object qtControl, Widget swtControl) {
		if (qtControl == null || swtControl == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		qt2swtControlMap.put(qtControl, swtControl);
	}

	Widget findControl(Object qtControl) {
		if (qtControl == null) {
			return null;
		}
		return qt2swtControlMap.get(qtControl);
	}

	Widget removeControl(Object qtControl) {
		return qt2swtControlMap.remove(qtControl);
	}

	void addBar(Menu menu) {
		if (bars == null) {
			bars = new Menu[4];
		}
		int length = bars.length;
		for (int i = 0; i < length; i++) {
			if (bars[i] == menu) {
				return;
			}
		}
		int index = 0;
		while (index < length) {
			if (bars[index] == null) {
				break;
			}
			index++;
		}
		if (index == length) {
			Menu[] newBars = new Menu[length + 4];
			System.arraycopy(bars, 0, newBars, 0, length);
			bars = newBars;
		}
		bars[index] = menu;
	}

	void releaseTray(Tray tray) {
		if (this.tray == tray) {
			this.tray = null;
		}
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when an event of the given type occurs anywhere in a widget. The event
	 * type is one of the event constants defined in class <code>SWT</code>.
	 * When the event does occur, the listener is notified by sending it the
	 * <code>handleEvent()</code> message.
	 * <p>
	 * Setting the type of an event to <code>SWT.None</code> from within the
	 * <code>handleEvent()</code> method can be used to change the event type
	 * and stop subsequent Java listeners from running. Because event filters
	 * run before other listeners, event filters can both block other listeners
	 * and set arbitrary fields within an event. For this reason, event filters
	 * are both powerful and dangerous. They should generally be avoided for
	 * performance, debugging and code maintenance reasons.
	 * </p>
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should be notified when the event occurs
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #removeFilter
	 * @see #removeListener
	 * 
	 * @since 3.0
	 */
	public void addFilter(int eventType, Listener listener) {
		checkDevice();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (filterTable == null) {
			filterTable = new EventTable();
		}
		filterTable.hook(eventType, listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when an event of the given type occurs. The event type is one of the
	 * event constants defined in class <code>SWT</code>. When the event does
	 * occur in the display, the listener is notified by sending it the
	 * <code>handleEvent()</code> message.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should be notified when the event occurs
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #removeListener
	 * 
	 * @since 2.0
	 */
	public void addListener(int eventType, Listener listener) {
		checkDevice();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			eventTable = new EventTable();
		}
		eventTable.hook(eventType, listener);
	}

	void addMenuItem(MenuItem item) {
		if (items == null) {
			items = new MenuItem[64];
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				item.id = i + ID_START;
				items[i] = item;
				return;
			}
		}
		item.id = items.length + ID_START;
		MenuItem[] newItems = new MenuItem[items.length + 64];
		newItems[items.length] = item;
		System.arraycopy(items, 0, newItems, 0, items.length);
		items = newItems;
	}

	void addPopup(Menu menu) {
		if (popups == null) {
			popups = new Menu[4];
		}
		int length = popups.length;
		for (int i = 0; i < length; i++) {
			if (popups[i] == menu) {
				return;
			}
		}
		int index = 0;
		while (index < length) {
			if (popups[index] == null) {
				break;
			}
			index++;
		}
		if (index == length) {
			Menu[] newPopups = new Menu[length + 4];
			System.arraycopy(popups, 0, newPopups, 0, length);
			popups = newPopups;
		}
		popups[index] = menu;
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread at the next reasonable opportunity. The caller of
	 * this method continues to run in parallel, and is not notified when the
	 * runnable has completed. Specifying <code>null</code> as the runnable
	 * simply wakes the user-interface thread when run.
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the
	 * receiver as their display may have been disposed. Therefore, it is
	 * necessary to check for this case inside the runnable before accessing the
	 * widget.
	 * </p>
	 * 
	 * @param runnable
	 *            code to run on the user-interface thread or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #syncExec
	 */
	public void asyncExec(Runnable runnable) {
		synchronized (Device.class) {
			if (isDisposed()) {
				error(SWT.ERROR_DEVICE_DISPOSED);
			}
			synchronizer.asyncExec(runnable);
		}
	}

	/**
	 * Causes the system hardware to emit a short sound (if it supports this
	 * capability).
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void beep() {
		checkDevice();
		QApplication.beep();
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see Widget#checkSubclass
	 */
	protected void checkSubclass() {
		if (!isValidClass(getClass())) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	protected void checkDevice() {
		if (thread == null) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if (thread != Thread.currentThread()) {
			/*
			 * Bug in IBM JVM 1.6. For some reason, under conditions that are
			 * yet to be full understood, Thread.currentThread() is either
			 * returning null or a different instance from the one that was
			 * saved when the Display was created. This is possibly a JIT
			 * problem because modifying this method to print logging
			 * information when the error happens seems to fix the problem. The
			 * fix is to use operating system calls to verify that the current
			 * thread is not the Display thread.
			 * 
			 * NOTE: Despite the fact that Thread.currentThread() is used in
			 * other places, the failure has not been observed in all places
			 * where it is called.
			 */

			if (threadId != (int) Thread.currentThread().getId()) {
				error(SWT.ERROR_THREAD_INVALID_ACCESS);
			}
		}
		if (isDisposed()) {
			error(SWT.ERROR_DEVICE_DISPOSED);
		}
	}

	private static void checkDisplay(Thread thread, boolean multiple) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				if (Displays[i] != null) {
					if (!multiple) {
						SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]"); //$NON-NLS-1$
					}
					if (Displays[i].thread == thread) {
						SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
					}
				}
			}
		}
	}

	void clearModal(Shell shell) {
		if (modalShells == null) {
			return;
		}
		int index = 0, length = modalShells.length;
		while (index < length) {
			if (modalShells[index] == shell) {
				break;
			}
			if (modalShells[index] == null) {
				return;
			}
			index++;
		}
		if (index == length) {
			return;
		}
		System.arraycopy(modalShells, index + 1, modalShells, index, --length - index);
		modalShells[length] = null;
		if (index == 0 && modalShells[0] == null) {
			modalShells = null;
		}
		Shell[] shells = getShells();
		for (int i = 0; i < shells.length; i++) {
			shells[i].updateModal();
		}
	}

	/**
	 * Requests that the connection between SWT and the underlying operating
	 * system be closed.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Device#dispose
	 * 
	 * @since 2.0
	 */
	public void close() {
		checkDevice();
		Event event = new Event();
		sendEvent(SWT.Close, event);
		if (event.doit) {
			dispose();
		}
	}

	/**
	 * Creates the device in the operating system. If the device does not have a
	 * handle, this method may do nothing depending on the device.
	 * <p>
	 * This method is called before <code>init</code>.
	 * </p>
	 * 
	 * @param data
	 *            the DeviceData which describes the receiver
	 * 
	 * @see #init
	 */
	@Override
	protected void create(DeviceData data) {
		checkSubclass();
		checkDisplay(thread = Thread.currentThread(), true);
		createDisplay(data);
		register(this);
		if (Default == null) {
			Default = this;
		}
	}

	private void createDisplay(DeviceData data) {
		qt2swtControlMap = Collections.synchronizedMap(new HashMap<Object, Widget>());
		QApplication.initialize(new String[0]);
		QApplication.setQuitOnLastWindowClosed(false);
		masterEventFilter = new MasterEventFilter(this);
		QApplication.instance().installEventFilter(masterEventFilter);
		setPaintDevice(QApplication.desktop());
		messageHandler = new SwtMessageHandler();
		QMessageHandler.installMessageHandler(messageHandler);
	}

	static void deregister(Display display) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				if (display == Displays[i]) {
					Displays[i] = null;
				}
			}
		}
	}

	/**
	 * Destroys the device in the operating system and releases the device's
	 * handle. If the device does not have a handle, this method may do nothing
	 * depending on the device.
	 * <p>
	 * This method is called after <code>release</code>.
	 * </p>
	 * 
	 * @see Device#dispose
	 * @see #release
	 */
	@Override
	protected void destroy() {
		if (this == Default) {
			Default = null;
		}
		deregister(this);
		destroyDisplay();
	}

	void destroyDisplay() {
		QMessageHandler.removeMessageHandler(messageHandler);
		QApplication.instance().removeEventFilter(masterEventFilter);
		QApplication.instance().dispose();
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread just before the receiver is disposed. Specifying a
	 * <code>null</code> runnable is ignored.
	 * 
	 * @param runnable
	 *            code to run at dispose time.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void disposeExec(Runnable runnable) {
		checkDevice();
		if (disposeList == null) {
			disposeList = new Runnable[4];
		}
		for (int i = 0; i < disposeList.length; i++) {
			if (disposeList[i] == null) {
				disposeList[i] = runnable;
				return;
			}
		}
		Runnable[] newDisposeList = new Runnable[disposeList.length + 4];
		System.arraycopy(disposeList, 0, newDisposeList, 0, disposeList.length);
		newDisposeList[disposeList.length] = runnable;
		disposeList = newDisposeList;
	}

	/**
	 * Does whatever display specific cleanup is required, and then uses the
	 * code in <code>SWTError.error</code> to handle the error.
	 * 
	 * @param code
	 *            the descriptive error code
	 * 
	 * @see SWT#error(int)
	 */
	void error(int code) {
		SWT.error(code);
	}

	boolean filterEvent(Event event) {
		if (filterTable != null) {
			filterTable.sendEvent(event);
		}
		return false;
	}

	boolean filters(int eventType) {
		if (filterTable == null) {
			return false;
		}
		return filterTable.hooks(eventType);
	}

	/**
	 * Given a widget and a widget-specific id, returns the instance of the
	 * <code>Widget</code> subclass which represents the widget/id pair in the
	 * currently running application, if such exists, or null if no matching
	 * widget can be found.
	 * 
	 * @param widget
	 *            the widget
	 * @param id
	 *            the id for the subwidget (usually an item)
	 * @return the SWT subwidget (usually an item) that the widget/id pair
	 *         represents
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public Widget findWidget(Widget widget, int /* long */id) {
		checkDevice();
		// TODO howto to it with qt ? use qObj.nativeId() ?
		throw new UnsupportedOperationException("not yet implemented for qt"); //$NON-NLS-1$
	}

	// linux 64bit
	public Widget findWidget(/* int */long id) {
		checkDevice();
		// TODO howto to it with qt ? use qObj.nativeId() ?
		throw new UnsupportedOperationException("not yet implemented for qt"); //$NON-NLS-1$
	}

	// win32
	public Widget findWidget(int /* long */hwnd, int /* long */id) {
		checkDevice();
		// TODO howto to it with qt ? use qObj.nativeId() ?
		throw new UnsupportedOperationException("not yet implemented for qt"); //$NON-NLS-1$
	}

	/**
	 * Returns the display which the given thread is the user-interface thread
	 * for, or null if the given thread is not a user-interface thread for any
	 * display. Specifying <code>null</code> as the thread will return
	 * <code>null</code> for the display.
	 * 
	 * @param thread
	 *            the user-interface thread
	 * @return the display for the given thread
	 */
	public static Display findDisplay(Thread thread) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				Display display = Displays[i];
				if (display != null && display.thread == thread) {
					return display;
				}
			}
			return null;
		}
	}

	/**
	 * Returns the currently active <code>Shell</code>, or null if no shell
	 * belonging to the currently running application is active.
	 * 
	 * @return the active shell or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Shell getActiveShell() {
		checkDevice();
		QWidget activeWindow = QApplication.activeWindow();
		Widget control = findControl(activeWindow);
		if (!(control instanceof Control)) {
			return null;
		}
		return control != null ? ((Control) control).getShell() : null;
	}

	/**
	 * Returns the single instance of the application menu bar or null
	 * when there is no application menu bar for the platform.
	 *
	 * @return the application menu bar or <code>null</code>
	 * 
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @since 3.7
	 */
	public Menu getAppMenuBar () {
		checkDevice ();
		return null;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location. Note
	 * that on multi-monitor systems the origin can be negative.
	 * 
	 * @return the bounding rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	@Override
	public Rectangle getBounds() {
		checkDevice();
		return QtSWTConverter.convert(QApplication.desktop().screenGeometry());
	}

	/**
	 * Returns the display which the currently running thread is the
	 * user-interface thread for, or null if the currently running thread is not
	 * a user-interface thread for any display.
	 * 
	 * @return the current display
	 */
	public static Display getCurrent() {
		return findDisplay(Thread.currentThread());
	}

	/**
	 * Returns a rectangle which describes the area of the receiver which is
	 * capable of displaying data.
	 * 
	 * @return the client area
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getBounds
	 */
	@Override
	public Rectangle getClientArea() {
		checkDevice();
		return QtSWTConverter.convert(QApplication.desktop().childrenRect());
	}

	/**
	 * Returns the control which the on-screen pointer is currently over top of,
	 * or null if it is not currently over one of the controls built by the
	 * currently running application.
	 * 
	 * @return the control under the cursor
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Control getCursorControl() {
		checkDevice();
		Widget widget = findControl(QApplication.widgetAt(QCursor.pos()));
		if (widget instanceof Control) {
			return (Control) widget;
		}
		return null;
	}

	/**
	 * Returns the location of the on-screen pointer relative to the top left
	 * corner of the screen.
	 * 
	 * @return the cursor location
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Point getCursorLocation() {
		checkDevice();
		return QtSWTConverter.convert(QCursor.pos());
	}

	/**
	 * Returns an array containing the recommended cursor sizes.
	 * 
	 * @return the array of cursor sizes
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public Point[] getCursorSizes() {
		checkDevice();
		return new Point[] { new Point(32, 32) };
	}

	/**
	 * Returns the default display. One is created (making the thread that
	 * invokes this method its user-interface thread) if it did not already
	 * exist.
	 * 
	 * @return the default display
	 */
	public static Display getDefault() {
		synchronized (Device.class) {
			if (Default == null) {
				Default = new Display();
				// Hack: This is necessary to prevent a JVM crash when leaving the Eclipse IDE with Qt!
				// http://eclipse.compeople.eu/wiki/index.php/Compeople:SWTQtImplementierungsdetails#ACCESS_VIOLATION_beim_Beenden_der_IDE_mit_Qt
				Display.deregister(Default);
				Default.destroyDisplay();
			}
			return Default;
		}
	}

	/**	 
	 * Returns true if a touch-aware input device is attached to the system,
	 * enabled, and ready for use.
	 * 
	 * @since 3.7
	 */
	public boolean isTouchEnabled() {
		return false;
	}

	static boolean isValidClass(Class<?> clazz) {
		String name = clazz.getName();
		int index = name.lastIndexOf('.');
		return name.substring(0, index + 1).equals(PACKAGE_PREFIX);
	}

	/**
	 * Returns the application defined property of the receiver with the
	 * specified name, or null if it has not been set.
	 * <p>
	 * Applications may have associated arbitrary objects with the receiver in
	 * this fashion. If the objects stored in the properties need to be notified
	 * when the display is disposed of, it is the application's responsibility
	 * to provide a <code>disposeExec()</code> handler which does so.
	 * </p>
	 * 
	 * @param key
	 *            the name of the property
	 * @return the value of the property or null if it has not been set
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #setData(String, Object)
	 * @see #disposeExec(Runnable)
	 */
	public Object getData(String key) {
		checkDevice();
		if (key == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (SWQT.STYLESHEET_KEY.equals(key)) {
			return styleSheet;
		}

		if (keys == null) {
			return null;
		}

		for (int i = 0; i < keys.length; i++) {
			if (keys[i].equals(key)) {
				return values[i];
			}
		}
		return null;
	}

	/**
	 * Returns the application defined, display specific data associated with
	 * the receiver, or null if it has not been set. The
	 * <em>display specific data</em> is a single, unnamed field that is stored
	 * with every display.
	 * <p>
	 * Applications may put arbitrary objects in this field. If the object
	 * stored in the display specific data needs to be notified when the display
	 * is disposed of, it is the application's responsibility to provide a
	 * <code>disposeExec()</code> handler which does so.
	 * </p>
	 * 
	 * @return the display specific data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #setData(Object)
	 * @see #disposeExec(Runnable)
	 */
	public Object getData() {
		checkDevice();
		return data;
	}

	/**
	 * Returns the button dismissal alignment, one of <code>LEFT</code> or
	 * <code>RIGHT</code>. The button dismissal alignment is the ordering that
	 * should be used when positioning the default dismissal button for a
	 * dialog. For example, in a dialog that contains an OK and CANCEL button,
	 * on platforms where the button dismissal alignment is <code>LEFT</code>,
	 * the button ordering should be OK/CANCEL. When button dismissal alignment
	 * is <code>RIGHT</code>, the button ordering should be CANCEL/OK.
	 * 
	 * @return the button dismissal order
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public int getDismissalAlignment() {
		checkDevice();
		return SWT.LEFT;
	}

	/**
	 * Returns the longest duration, in milliseconds, between two mouse button
	 * clicks that will be considered a <em>double click</em> by the underlying
	 * operating system.
	 * 
	 * @return the double click time
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getDoubleClickTime() {
		checkDevice();
		return QApplication.doubleClickInterval();
	}

	/**
	 * Returns the control which currently has keyboard focus, or null if
	 * keyboard events are not currently going to any of the controls built by
	 * the currently running application.
	 * 
	 * @return the control under the cursor
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Control getFocusControl() {
		checkDevice();
		if (focusControl != null && !focusControl.isDisposed()) {
			return focusControl;
		}
		return _getFocusControl();
	}

	/**
	 * Returns true when the high contrast mode is enabled. Otherwise, false is
	 * returned.
	 * <p>
	 * Note: This operation is a hint and is not supported on platforms that do
	 * not have this concept.
	 * </p>
	 * 
	 * @return the high contrast mode
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public boolean getHighContrast() {
		checkDevice();
		return false;
	}

	/**
	 * Returns the maximum allowed depth of icons on this display, in bits per
	 * pixel. On some platforms, this may be different than the actual depth of
	 * the display.
	 * 
	 * @return the maximum icon depth
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Device#getDepth
	 */
	public int getIconDepth() {
		checkDevice();
		return QPixmap.defaultDepth();
	}

	/**
	 * Returns an array containing the recommended icon sizes.
	 * 
	 * @return the array of icon sizes
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Decorations#setImages(Image[])
	 * 
	 * @since 3.0
	 */
	public Point[] getIconSizes() {
		checkDevice();
		// TODO defaults ok?
		return new Point[] { new Point(16, 16), new Point(32, 32) };
	}

	public int getLastEventTime() {
		return lastEventTime;
	}

	private int getMessageCount() {
		return synchronizer.getMessageCount();
	}

	Shell getModalShell() {
		if (modalShells == null) {
			return null;
		}
		int index = modalShells.length;
		while (--index >= 0) {
			Shell shell = modalShells[index];
			if (shell != null) {
				return shell;
			}
		}
		return null;
	}

	Dialog getModalDialog() {
		return modalDialog;
	}

	/**
	 * Returns an array of monitors attached to the device.
	 * 
	 * @return the array of monitors
	 * 
	 * @since 3.0
	 */
	public Monitor[] getMonitors() {
		checkDevice();
		Monitor[] monitors = new Monitor[QApplication.desktop().numScreens()];
		for (int i = 0; i < monitors.length; i++) {
			monitors[i] = createMonitor(i);
		}
		return monitors;
	}

	static Monitor createMonitor(QWidget widget) {
		return createMonitor(QApplication.desktop().screenNumber(widget));
	}

	static Monitor createMonitor(int index) {
		QRect screen = QApplication.desktop().screenGeometry(index);
		Monitor monitor = new Monitor();
		monitor.handle = index;
		monitor.width = screen.width();
		monitor.height = screen.height();

		QRect rect = QApplication.desktop().availableGeometry(index);
		monitor.clientX = rect.left();
		monitor.clientY = rect.top();
		monitor.clientWidth = rect.width();
		monitor.clientHeight = rect.height();
		return monitor;
	}

	/**
	 * Returns the primary monitor for that device.
	 * 
	 * @return the primary monitor
	 * 
	 * @since 3.0
	 */
	public Monitor getPrimaryMonitor() {
		checkDevice();
		return createMonitor(QApplication.desktop().primaryScreen());
	}

	/**
	 * Returns a (possibly empty) array containing all shells which have not
	 * been disposed and have the receiver as their display.
	 * 
	 * @return the receiver's shells
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Shell[] getShells() {
		checkDevice();
		int index = 0;
		Shell[] result = new Shell[16];
		for (Widget control : qt2swtControlMap.values()) {
			if (control != null && control instanceof Shell) {
				int j = 0;
				while (j < index) {
					if (result[j] == control) {
						break;
					}
					j++;
				}
				if (j == index) {
					if (index == result.length) {
						Shell[] newResult = new Shell[index + 16];
						System.arraycopy(result, 0, newResult, 0, index);
						result = newResult;
					}
					result[index++] = (Shell) control;
				}
			}
		}
		if (index == result.length) {
			return result;
		}
		Shell[] newResult = new Shell[index];
		System.arraycopy(result, 0, newResult, 0, index);
		return newResult;
	}

	/**
	 * Gets the synchronizer used by the display.
	 * 
	 * @return the receiver's synchronizer
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public Synchronizer getSynchronizer() {
		checkDevice();
		return synchronizer;
	}

	/**
	 * Returns the thread that has invoked <code>syncExec</code> or null if no
	 * such runnable is currently being invoked by the user-interface thread.
	 * <p>
	 * Note: If a runnable invoked by asyncExec is currently running, this
	 * method will return null.
	 * </p>
	 * 
	 * @return the receiver's sync-interface thread
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Thread getSyncThread() {
		synchronized (Device.class) {
			if (isDisposed()) {
				error(SWT.ERROR_DEVICE_DISPOSED);
			}
			return synchronizer.syncThread;
		}
	}

	/**
	 * Returns the matching standard platform cursor for the given constant,
	 * which should be one of the cursor constants specified in class
	 * <code>SWT</code>. This cursor should not be free'd because it was
	 * allocated by the system, not the application. A value of
	 * <code>null</code> will be returned if the supplied constant is not an SWT
	 * cursor constant.
	 * 
	 * @param id
	 *            the SWT cursor constant
	 * @return the corresponding cursor or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see SWT#CURSOR_ARROW
	 * @see SWT#CURSOR_WAIT
	 * @see SWT#CURSOR_CROSS
	 * @see SWT#CURSOR_APPSTARTING
	 * @see SWT#CURSOR_HELP
	 * @see SWT#CURSOR_SIZEALL
	 * @see SWT#CURSOR_SIZENESW
	 * @see SWT#CURSOR_SIZENS
	 * @see SWT#CURSOR_SIZENWSE
	 * @see SWT#CURSOR_SIZEWE
	 * @see SWT#CURSOR_SIZEN
	 * @see SWT#CURSOR_SIZES
	 * @see SWT#CURSOR_SIZEE
	 * @see SWT#CURSOR_SIZEW
	 * @see SWT#CURSOR_SIZENE
	 * @see SWT#CURSOR_SIZESE
	 * @see SWT#CURSOR_SIZESW
	 * @see SWT#CURSOR_SIZENW
	 * @see SWT#CURSOR_UPARROW
	 * @see SWT#CURSOR_IBEAM
	 * @see SWT#CURSOR_NO
	 * @see SWT#CURSOR_HAND
	 * 
	 * @since 3.0
	 */
	public Cursor getSystemCursor(int id) {
		checkDevice();
		if (!(0 <= id && id < cursors.length)) {
			return null;
		}
		if (cursors[id] == null) {
			cursors[id] = new Cursor(this, id);
		}
		return cursors[id];
	}

	/**
	 * Returns a reasonable font for applications to use. On some platforms,
	 * this will match the "default font" or "system font" if such can be found.
	 * This font should not be free'd because it was allocated by the system,
	 * not the application.
	 * <p>
	 * Typically, applications which want the default look should simply not set
	 * the font on the widgets they create. Widgets are always created with the
	 * correct default font for the class of user-interface component they
	 * represent.
	 * </p>
	 * 
	 * @return a font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	@Override
	public Font getSystemFont() {
		checkDevice();
		return systemFont = Font.qt_new(this, new QFont());
	}

	/**
	 * Returns the matching standard platform image for the given constant,
	 * which should be one of the icon constants specified in class
	 * <code>SWT</code>. This image should not be free'd because it was
	 * allocated by the system, not the application. A value of
	 * <code>null</code> will be returned either if the supplied constant is not
	 * an SWT icon constant or if the platform does not define an image that
	 * corresponds to the constant.
	 * 
	 * @param id
	 *            the SWT icon constant
	 * @return the corresponding image or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see SWT#ICON_ERROR
	 * @see SWT#ICON_INFORMATION
	 * @see SWT#ICON_QUESTION
	 * @see SWT#ICON_WARNING
	 * @see SWT#ICON_WORKING
	 * 
	 * @since 3.0
	 */
	public Image getSystemImage(int id) {
		checkDevice();
		switch (id) {
		case SWT.ICON_ERROR: {
			if (errorImage != null) {
				return errorImage;
			}
			QIcon icon = QApplication.style().standardIcon(StandardPixmap.SP_MessageBoxCritical);
			return errorImage = Image.qt_new(this, SWT.ICON, icon);
		}
		case SWT.ICON_WORKING:
		case SWT.ICON_INFORMATION: {
			if (infoImage != null) {
				return infoImage;
			}
			QIcon icon = QApplication.style().standardIcon(StandardPixmap.SP_MessageBoxInformation);
			return errorImage = Image.qt_new(this, SWT.ICON, icon);
		}
		case SWT.ICON_QUESTION: {
			if (questionImage != null) {
				return questionImage;
			}
			QIcon icon = QApplication.style().standardIcon(StandardPixmap.SP_MessageBoxQuestion);
			return errorImage = Image.qt_new(this, SWT.ICON, icon);
		}
		case SWT.ICON_WARNING: {
			if (warningIcon != null) {
				return warningIcon;
			}
			QIcon icon = QApplication.style().standardIcon(StandardPixmap.SP_MessageBoxWarning);
			return errorImage = Image.qt_new(this, SWT.ICON, icon);
		}
		}
		return null;
	}

	/**
	 * Returns the single instance of the system tray or null when there is no
	 * system tray available for the platform.
	 * 
	 * @return the system tray or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public Tray getSystemTray() {
		checkDevice();
		if (tray != null) {
			return tray;
		}
		tray = new Tray(this, SWT.NONE);
		return tray;
	}

	/**
	 * Returns the user-interface thread for the receiver.
	 * 
	 * @return the receiver's user-interface thread
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Thread getThread() {
		synchronized (Device.class) {
			if (isDisposed()) {
				error(SWT.ERROR_DEVICE_DISPOSED);
			}
			return thread;
		}
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Display</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param data
	 *            the platform specific GC data
	 * @return the platform specific GC handle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                gc creation</li>
	 *                </ul>
	 */
	@Override
	public QPaintDeviceInterface internal_new_GC(GCData data) {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_DEVICE_DISPOSED);
		}
		return new QPicture();
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Display</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param hDC
	 *            the platform specific GC handle
	 * @param data
	 *            the platform specific GC data
	 */
	@Override
	public void internal_dispose_GC(QPaintDeviceInterface paintDevice, GCData data) {
		// nothing
	}

	boolean isValidThread() {
		return thread == Thread.currentThread();
	}

	/**
	 * Maps a point from one coordinate system to another. When the control is
	 * null, coordinates are mapped to the display.
	 * <p>
	 * NOTE: On right-to-left platforms where the coordinate systems are
	 * mirrored, special care needs to be taken when mapping coordinates from
	 * one control to another to ensure the result is correctly mirrored.
	 * 
	 * Mapping a point that is the origin of a rectangle and then adding the
	 * width and height is not equivalent to mapping the rectangle. When one
	 * control is mirrored and the other is not, adding the width and height to
	 * a point that was mapped causes the rectangle to extend in the wrong
	 * direction. Mapping the entire rectangle instead of just one point causes
	 * both the origin and the corner of the rectangle to be mapped.
	 * </p>
	 * 
	 * @param from
	 *            the source <code>Control</code> or <code>null</code>
	 * @param to
	 *            the destination <code>Control</code> or <code>null</code>
	 * @param point
	 *            to be mapped
	 * @return point with mapped coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the Control from or the
	 *                Control to have been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public Point map(Control from, Control to, Point point) {
		checkDevice();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return map(from, to, point.x, point.y);
	}

	/**
	 * Maps a point from one coordinate system to another. When the control is
	 * null, coordinates are mapped to the display.
	 * <p>
	 * NOTE: On right-to-left platforms where the coordinate systems are
	 * mirrored, special care needs to be taken when mapping coordinates from
	 * one control to another to ensure the result is correctly mirrored.
	 * 
	 * Mapping a point that is the origin of a rectangle and then adding the
	 * width and height is not equivalent to mapping the rectangle. When one
	 * control is mirrored and the other is not, adding the width and height to
	 * a point that was mapped causes the rectangle to extend in the wrong
	 * direction. Mapping the entire rectangle instead of just one point causes
	 * both the origin and the corner of the rectangle to be mapped.
	 * </p>
	 * 
	 * @param from
	 *            the source <code>Control</code> or <code>null</code>
	 * @param to
	 *            the destination <code>Control</code> or <code>null</code>
	 * @param x
	 *            coordinates to be mapped
	 * @param y
	 *            coordinates to be mapped
	 * @return point with mapped coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the Control from or the
	 *                Control to have been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public Point map(Control from, Control to, int x, int y) {
		checkDevice();
		if (from != null && from.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (to != null && to.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (from == to) {
			return new Point(x, y);
		}

		Point point = null;
		if (to != null && from != null) {
			point = from.toDisplay(x, y);
			point = to.toControl(x, y);
		} else if (to != null) {
			point = to.toControl(x, y);
		} else if (from != null) {
			point = from.toDisplay(x, y);
		} else {
			point = new Point(x, y);
		}
		return point;
	}

	/**
	 * Maps a point from one coordinate system to another. When the control is
	 * null, coordinates are mapped to the display.
	 * <p>
	 * NOTE: On right-to-left platforms where the coordinate systems are
	 * mirrored, special care needs to be taken when mapping coordinates from
	 * one control to another to ensure the result is correctly mirrored.
	 * 
	 * Mapping a point that is the origin of a rectangle and then adding the
	 * width and height is not equivalent to mapping the rectangle. When one
	 * control is mirrored and the other is not, adding the width and height to
	 * a point that was mapped causes the rectangle to extend in the wrong
	 * direction. Mapping the entire rectangle instead of just one point causes
	 * both the origin and the corner of the rectangle to be mapped.
	 * </p>
	 * 
	 * @param from
	 *            the source <code>Control</code> or <code>null</code>
	 * @param to
	 *            the destination <code>Control</code> or <code>null</code>
	 * @param rectangle
	 *            to be mapped
	 * @return rectangle with mapped coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the Control from or the
	 *                Control to have been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public Rectangle map(Control from, Control to, Rectangle rectangle) {
		checkDevice();
		if (rectangle == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		return map(from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	/**
	 * Maps a point from one coordinate system to another. When the control is
	 * null, coordinates are mapped to the display.
	 * <p>
	 * NOTE: On right-to-left platforms where the coordinate systems are
	 * mirrored, special care needs to be taken when mapping coordinates from
	 * one control to another to ensure the result is correctly mirrored.
	 * 
	 * Mapping a point that is the origin of a rectangle and then adding the
	 * width and height is not equivalent to mapping the rectangle. When one
	 * control is mirrored and the other is not, adding the width and height to
	 * a point that was mapped causes the rectangle to extend in the wrong
	 * direction. Mapping the entire rectangle instead of just one point causes
	 * both the origin and the corner of the rectangle to be mapped.
	 * </p>
	 * 
	 * @param from
	 *            the source <code>Control</code> or <code>null</code>
	 * @param to
	 *            the destination <code>Control</code> or <code>null</code>
	 * @param x
	 *            coordinates to be mapped
	 * @param y
	 *            coordinates to be mapped
	 * @param width
	 *            coordinates to be mapped
	 * @param height
	 *            coordinates to be mapped
	 * @return rectangle with mapped coordinates
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the Control from or the
	 *                Control to have been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public Rectangle map(Control from, Control to, int x, int y, int width, int height) {
		checkDevice();
		if (from != null && from.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (to != null && to.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (from == to) {
			return new Rectangle(x, y, width, height);
		}

		Rectangle rectangle = new Rectangle(x, y, width, height);
		if (to != null && from != null) {
			Point topLeft = from.toDisplay(x, y);
			topLeft = to.toControl(topLeft);
			rectangle.x = topLeft.x;
			rectangle.y = topLeft.y;
		} else if (to != null) {
			Point topLeft = to.toControl(x, y);
			rectangle.x = topLeft.x;
			rectangle.y = topLeft.y;
		} else if (from != null) {
			Point topLeft = from.toDisplay(x, y);
			rectangle.x = topLeft.x;
			rectangle.y = topLeft.y;
		}
		return rectangle;
	}

	/**
	 * Generate a low level system event.
	 * 
	 * <code>post</code> is used to generate low level keyboard and mouse
	 * events. The intent is to enable automated UI testing by simulating the
	 * input from the user. Most SWT applications should never need to call this
	 * method.
	 * <p>
	 * Note that this operation can fail when the operating system fails to
	 * generate the event for any reason. For example, this can happen when
	 * there is no such key or mouse button or when the system event queue is
	 * full.
	 * </p>
	 * <p>
	 * <b>Event Types:</b>
	 * <p>
	 * KeyDown, KeyUp
	 * <p>
	 * The following fields in the <code>Event</code> apply:
	 * <ul>
	 * <li>(in) type KeyDown or KeyUp</li>
	 * <p>
	 * Either one of:
	 * <li>(in) character a character that corresponds to a keyboard key</li>
	 * <li>(in) keyCode the key code of the key that was typed, as defined by
	 * the key code constants in class <code>SWT</code></li>
	 * </ul>
	 * <p>
	 * MouseDown, MouseUp
	 * </p>
	 * <p>
	 * The following fields in the <code>Event</code> apply:
	 * <ul>
	 * <li>(in) type MouseDown or MouseUp
	 * <li>(in) button the button that is pressed or released
	 * </ul>
	 * <p>
	 * MouseMove
	 * </p>
	 * <p>
	 * The following fields in the <code>Event</code> apply:
	 * <ul>
	 * <li>(in) type MouseMove
	 * <li>(in) x the x coordinate to move the mouse pointer to in screen
	 * coordinates
	 * <li>(in) y the y coordinate to move the mouse pointer to in screen
	 * coordinates
	 * </ul>
	 * <p>
	 * MouseWheel
	 * </p>
	 * <p>
	 * The following fields in the <code>Event</code> apply:
	 * <ul>
	 * <li>(in) type MouseWheel
	 * <li>(in) detail either SWT.SCROLL_LINE or SWT.SCROLL_PAGE
	 * <li>(in) count the number of lines or pages to scroll
	 * </ul>
	 * </dl>
	 * 
	 * @param event
	 *            the event to be generated
	 * 
	 * @return true if the event was generated or false otherwise
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the event is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 * 
	 */
	public boolean post(Event event) {
		if (isDisposed()) {
			error(SWT.ERROR_DEVICE_DISPOSED);
		}
		if (event == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		QWidget control = getControlForEvent(event);
		if (control == null) {
			return false;
		}

		QEvent qtEvent = createQEvent(event, control);
		if (event == null) {
			return false;
		}

		QApplication.postEvent(control, qtEvent);
		return true;

	}

	private QWidget getControlForEvent(Event event) {
		if (event.widget != null) {
			return event.widget.getQWidget();
		}
		return QApplication.focusWidget();
	}

	private QEvent createQEvent(Event event, QWidget focusWidget) {
		KeyboardModifiers modifiers = mapSWTModifier2Qt(event.stateMask);
		Type type = mapSWTEventType2Qt(event.type);

		switch (type) {
		case KeyPress:
		case KeyRelease:
			return createQKeyEvent(event, type, modifiers);
		case MouseButtonPress:
		case MouseMove:
		case MouseButtonRelease:
		case MouseButtonDblClick:
			return createQMouseEvent(event, type, modifiers, focusWidget);
		}
		return null;
	}

	private QEvent createQMouseEvent(Event event, Type type, KeyboardModifiers modifiers, QWidget focusWidget) {
		Control widget = (Control) findControl(focusWidget);
		if ((widget.getStyle() & SWT.MIRRORED) != 0) {
			event.x = widget.getClientWidth() - event.x;
		}

		QPoint eventPoint = new QPoint(event.x, event.y);
		QPoint global = focusWidget.mapToGlobal(eventPoint);
		MouseButton button = mapSWTButton2Qt(event.button);
		if (!type.equals(Type.MouseMove) && button.equals(MouseButton.NoButton)) {
			return null;
		}
		return new QMouseEvent(type, eventPoint, global, button, MouseButton.createQFlags(button), modifiers);
	}

	private QEvent createQKeyEvent(Event event, Type type, KeyboardModifiers modifiers) {
		// If neither keyCode nor character is given then abort
		if (event.keyCode == 0 && event.character == 0) {
			return null;
		}

		// Primarily try to map the keyCode to Qt:Key
		int key = KeyUtil.untranslateKey(event.keyCode);
		// If keyCode was given in the Event
		if (event.keyCode != 0) {
			// Given keyCode couldn't be translated, use it directly
			if (key == 0) {
				key = event.keyCode;
			}
		}

		// Secondarily if keyCode couldn't be mapped try the character
		if (key == 0) {
			switch (event.character) {
			case SWT.BS:
				key = Key.Key_Backspace.value();
				break;
			case SWT.CR:
				key = Key.Key_Return.value();
				break;
			case SWT.DEL:
				key = Key.Key_Delete.value();
				break;
			case SWT.ESC:
				key = Key.Key_Escape.value();
				break;
			case SWT.TAB:
				key = Key.Key_Tab.value();
				break;
			/*
			 * Since there is no LF key on the keyboard, do not attempt to map
			 * LF to CR or attempt to post an LF key.
			 */
			case SWT.LF:
				return null;
			default:
				key = Character.toUpperCase(event.character);
				break;
			}
		}
		return new QKeyEvent(type, key, modifiers, String.valueOf(event.character));
	}

	private static KeyboardModifiers mapSWTModifier2Qt(int modifiers) {
		int nativeModifiers = 0;
		if ((modifiers & SWT.ALT) != 0) {
			nativeModifiers |= KeyboardModifier.AltModifier.value();
		}
		if ((modifiers & SWT.SHIFT) != 0) {
			nativeModifiers |= KeyboardModifier.ShiftModifier.value();
		}
		if ((modifiers & SWT.CONTROL) != 0) {
			nativeModifiers |= KeyboardModifier.ControlModifier.value();
		}
		return new KeyboardModifiers(nativeModifiers);
	}

	private static QEvent.Type mapSWTEventType2Qt(int type) {
		switch (type) {
		case SWT.KeyDown:
			return Type.KeyPress;
		case SWT.KeyUp:
			return Type.KeyRelease;
		case SWT.MouseDown:
			return Type.MouseButtonPress;
		case SWT.MouseMove:
			return Type.MouseMove;
		case SWT.MouseUp:
			return Type.MouseButtonRelease;
		case SWT.MouseDoubleClick:
			return Type.MouseButtonDblClick;
		default:
			return Type.None;
		}
	}

	private static MouseButton mapSWTButton2Qt(int button) {
		switch (button) {
		case 1:
			return MouseButton.LeftButton;
		case 2:
			return MouseButton.MidButton;
		case 3:
			return MouseButton.RightButton;
		default:
			return MouseButton.NoButton;
		}
	}

	void postEvent(Event event) {
		/*
		 * Place the event at the end of the event queue. This code is always
		 * called in the Display's thread so it must be re-enterant but does not
		 * need to be synchronized.
		 */
		if (eventQueue == null) {
			eventQueue = new Event[4];
		}
		int index = 0;
		int length = eventQueue.length;
		while (index < length) {
			if (eventQueue[index] == null) {
				break;
			}
			index++;
		}
		if (index == length) {
			Event[] newQueue = new Event[length + 4];
			System.arraycopy(eventQueue, 0, newQueue, 0, length);
			eventQueue = newQueue;
		}
		eventQueue[index] = event;
	}

	/**
	 * Reads an event from the operating system's event queue, dispatches it
	 * appropriately, and returns <code>true</code> if there is potentially more
	 * work to do, or <code>false</code> if the caller can sleep until another
	 * event is placed on the event queue.
	 * <p>
	 * In addition to checking the system event queue, this method also checks
	 * if any inter-thread messages (created by <code>syncExec()</code> or
	 * <code>asyncExec()</code>) are waiting to be processed, and if so handles
	 * them before returning.
	 * </p>
	 * 
	 * @return <code>false</code> if the caller can sleep upon return from this
	 *         method
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_FAILED_EXEC - if an exception occurred while
	 *                running an inter-thread message</li>
	 *                </ul>
	 * 
	 * @see #sleep
	 * @see #wake
	 */
	public boolean readAndDispatch() {
		checkDevice();
		//		runPopups();
		//		boolean canSleep = runAsyncMessages(false);
		//		if (!canSleep) {
		//			QApplication.processEvents();
		//		}
		//		return canSleep;

		if (QApplication.hasPendingEvents()) {
			QApplication.processEvents();//ProcessEventsFlag.AllEvents
			if (isDisposed()) {
				return false;
			}
			QApplication.sendPostedEvents();
		}
		if (isDisposed()) {
			return false;
		}

		runDeferredEvents();

		if (isDisposed()) {
			return false;
		}

		runPopups();

		if (isDisposed()) {
			return false;
		}

		return runAsyncMessages(false);
	}

	private static void register(Display display) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				if (Displays[i] == null) {
					Displays[i] = display;
					return;
				}
			}
			Display[] newDisplays = new Display[Displays.length + 4];
			System.arraycopy(Displays, 0, newDisplays, 0, Displays.length);
			newDisplays[Displays.length] = display;
			Displays = newDisplays;
		}
	}

	/**
	 * Releases any internal resources back to the operating system and clears
	 * all fields except the device handle.
	 * <p>
	 * Disposes all shells which are currently open on the display. After this
	 * method has been invoked, all related related shells will answer
	 * <code>true</code> when sent the message <code>isDisposed()</code>.
	 * </p>
	 * <p>
	 * When a device is destroyed, resources that were acquired on behalf of the
	 * programmer need to be returned to the operating system. For example, if
	 * the device allocated a font to be used as the system font, this font
	 * would be freed in <code>release</code>. Also,to assist the garbage
	 * collector and minimize the amount of memory that is not reclaimed when
	 * the programmer keeps a reference to a disposed device, all fields except
	 * the handle are zero'd. The handle is needed by <code>destroy</code>.
	 * </p>
	 * This method is called before <code>destroy</code>.
	 * 
	 * @see Device#dispose
	 * @see #destroy
	 */
	@Override
	protected void release() {
		sendEvent(SWT.Dispose, new Event());
		Shell[] shells = getShells();
		for (int i = 0; i < shells.length; i++) {
			Shell shell = shells[i];
			if (!shell.isDisposed()) {
				shell.dispose();
			}
		}
		if (tray != null) {
			tray.dispose();
		}
		tray = null;
		while (readAndDispatch()) {
		}
		if (disposeList != null) {
			for (int i = 0; i < disposeList.length; i++) {
				if (disposeList[i] != null) {
					disposeList[i].run();
				}
			}
		}
		disposeList = null;
		synchronizer.releaseSynchronizer();
		synchronizer = null;
		releaseDisplay();
		super.release();
	}

	private void releaseDisplay() {
		/* Release the System fonts */
		if (systemFont != null) {
			systemFont.dispose();
		}
		systemFont = null;

		/* Release the System Images */
		if (errorImage != null) {
			errorImage.dispose();
		}
		if (infoImage != null) {
			infoImage.dispose();
		}
		if (questionImage != null) {
			questionImage.dispose();
		}
		if (warningIcon != null) {
			warningIcon.dispose();
		}
		errorImage = infoImage = questionImage = warningIcon = null;

		/* Release the System Cursors */
		for (int i = 0; i < cursors.length; i++) {
			if (cursors[i] != null) {
				cursors[i].dispose();
			}
		}
		cursors = null;

		/* Release references */
		thread = null;
		modalDialog = null;
		modalShells = null;
		data = null;
		keys = null;
		values = null;
		bars = popups = null;
		timerIds = null;
		timerList = null;
		eventTable = filterTable = null;
		items = null;

		/* Release handles */
		threadId = 0;
		QApplication.quit();
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when an event of the given type occurs anywhere in a widget. The
	 * event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should no longer be notified when the event
	 *            occurs
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #addFilter
	 * @see #addListener
	 * 
	 * @since 3.0
	 */
	public void removeFilter(int eventType, Listener listener) {
		checkDevice();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (filterTable == null) {
			return;
		}
		filterTable.unhook(eventType, listener);
		if (filterTable.size() == 0) {
			filterTable = null;
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when an event of the given type occurs. The event type is one of
	 * the event constants defined in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #addListener
	 * 
	 * @since 2.0
	 */
	public void removeListener(int eventType, Listener listener) {
		checkDevice();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(eventType, listener);
	}

	void removeBar(Menu menu) {
		if (bars == null) {
			return;
		}
		for (int i = 0; i < bars.length; i++) {
			if (bars[i] == menu) {
				bars[i] = null;
				return;
			}
		}
	}

	void removeMenuItem(MenuItem item) {
		if (items == null) {
			return;
		}
		items[item.id - ID_START] = null;
	}

	void removePopup(Menu menu) {
		if (popups == null) {
			return;
		}
		for (int i = 0; i < popups.length; i++) {
			if (popups[i] == menu) {
				popups[i] = null;
				return;
			}
		}
	}

	private boolean runAsyncMessages(boolean all) {
		return synchronizer.runAsyncMessages(all);
	}

	private boolean runDeferredEvents() {
		boolean run = false;
		/*
		 * Run deferred events. This code is always called in the Display's
		 * thread so it must be re-enterant but need not be synchronized.
		 */
		while (eventQueue != null) {

			/* Take an event off the queue */
			Event event = eventQueue[0];
			if (event == null) {
				break;
			}
			int length = eventQueue.length;
			System.arraycopy(eventQueue, 1, eventQueue, 0, --length);
			eventQueue[length] = null;

			/* Run the event */
			Widget widget = event.widget;
			if (widget != null && !widget.isDisposed()) {
				Widget item = event.item;
				if (item == null || !item.isDisposed()) {
					run = true;
					widget.sendEvent(event);
				}
			}

			/*
			 * At this point, the event queue could be null due to a recursive
			 * invocation when running the event.
			 */
		}

		/* Clear the queue */
		eventQueue = null;
		return run;
	}

	private boolean runPopups() {
		if (popups == null) {
			return false;
		}
		boolean result = false;
		while (popups != null) {
			Menu menu = popups[0];
			if (menu == null) {
				break;
			}
			int length = popups.length;
			System.arraycopy(popups, 1, popups, 0, --length);
			popups[length] = null;
			if (!menu.isDisposed()) {
				menu._setVisible(true);
			}
			result = true;
		}
		popups = null;
		return result;
	}

	private boolean runTimer(int /* long */id) {
		if (timerList != null && timerIds != null) {
			int index = 0;
			while (index < timerIds.length) {
				if (timerIds[index] == id && timerIds[index] != -1) {
					timer.killTimer(timerIds[index]);
					timerIds[index] = 0;
					Runnable runnable = timerList[index];
					timerList[index] = null;
					if (runnable != null) {
						runnable.run();
					}
					return true;
				}
				index++;
			}
		}
		return false;
	}

	private void sendEvent(int eventType, Event event) {
		if (eventTable == null && filterTable == null) {
			return;
		}
		if (event == null) {
			event = new Event();
		}
		event.display = this;
		event.type = eventType;
		if (event.time == 0) {
			event.time = getLastEventTime();
		}
		if (!filterEvent(event)) {
			if (eventTable != null) {
				eventTable.sendEvent(event);
			}
		}
	}

	/**
	 * Sets the location of the on-screen pointer relative to the top left
	 * corner of the screen. <b>Note: It is typically considered bad practice
	 * for a program to move the on-screen pointer location.</b>
	 * 
	 * @param x
	 *            the new x coordinate for the cursor
	 * @param y
	 *            the new y coordinate for the cursor
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public void setCursorLocation(int x, int y) {
		checkDevice();
		QCursor.setPos(x, y);
	}

	/**
	 * Sets the location of the on-screen pointer relative to the top left
	 * corner of the screen. <b>Note: It is typically considered bad practice
	 * for a program to move the on-screen pointer location.</b>
	 * 
	 * @param point
	 *            new position
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.0
	 */
	public void setCursorLocation(Point point) {
		checkDevice();
		if (point == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setCursorLocation(point.x, point.y);
	}

	/**
	 * Sets the application defined property of the receiver with the specified
	 * name to the given argument.
	 * <p>
	 * Applications may have associated arbitrary objects with the receiver in
	 * this fashion. If the objects stored in the properties need to be notified
	 * when the display is disposed of, it is the application's responsibility
	 * provide a <code>disposeExec()</code> handler which does so.
	 * </p>
	 * 
	 * @param key
	 *            the name of the property
	 * @param value
	 *            the new value for the property
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getData(String)
	 * @see #disposeExec(Runnable)
	 */
	public void setData(String key, Object value) {
		checkDevice();
		if (key == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (SWQT.STYLESHEET_KEY.equals(key)) {
			setStyleSheet((String) value);
		}

		/* Remove the key/value pair */
		if (value == null) {
			if (keys == null) {
				return;
			}
			int index = 0;
			while (index < keys.length && !keys[index].equals(key)) {
				index++;
			}
			if (index == keys.length) {
				return;
			}
			if (keys.length == 1) {
				keys = null;
				values = null;
			} else {
				String[] newKeys = new String[keys.length - 1];
				Object[] newValues = new Object[values.length - 1];
				System.arraycopy(keys, 0, newKeys, 0, index);
				System.arraycopy(keys, index + 1, newKeys, index, newKeys.length - index);
				System.arraycopy(values, 0, newValues, 0, index);
				System.arraycopy(values, index + 1, newValues, index, newValues.length - index);
				keys = newKeys;
				values = newValues;
			}
			return;
		}

		/* Add the key/value pair */
		if (keys == null) {
			keys = new String[] { key };
			values = new Object[] { value };
			return;
		}
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].equals(key)) {
				values[i] = value;
				return;
			}
		}
		String[] newKeys = new String[keys.length + 1];
		Object[] newValues = new Object[values.length + 1];
		System.arraycopy(keys, 0, newKeys, 0, keys.length);
		System.arraycopy(values, 0, newValues, 0, values.length);
		newKeys[keys.length] = key;
		newValues[values.length] = value;
		keys = newKeys;
		values = newValues;
	}

	/**
	 * Sets the application defined, display specific data associated with the
	 * receiver, to the argument. The <em>display specific data</em> is a
	 * single, unnamed field that is stored with every display.
	 * <p>
	 * Applications may put arbitrary objects in this field. If the object
	 * stored in the display specific data needs to be notified when the display
	 * is disposed of, it is the application's responsibility provide a
	 * <code>disposeExec()</code> handler which does so.
	 * </p>
	 * 
	 * @param data
	 *            the new display specific data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getData()
	 * @see #disposeExec(Runnable)
	 */
	public void setData(Object data) {
		checkDevice();
		this.data = data;
	}

	/**
	 * On platforms which support it, sets the application name to be the
	 * argument. On Motif, for example, this can be used to set the name used
	 * for resource lookup. Specifying <code>null</code> for the name clears it.
	 * 
	 * @param name
	 *            the new app name or <code>null</code>
	 */
	public static void setAppName(String name) {
		QApplication.setApplicationName(name);
	}

	void setModalDialog(Dialog modalDailog) {
		this.modalDialog = modalDailog;
		Shell[] shells = getShells();
		for (int i = 0; i < shells.length; i++) {
			shells[i].updateModal();
		}
	}

	void setModalShell(Shell shell) {
		if (modalShells == null) {
			modalShells = new Shell[4];
		}
		int index = 0, length = modalShells.length;
		while (index < length) {
			if (modalShells[index] == shell) {
				return;
			}
			if (modalShells[index] == null) {
				break;
			}
			index++;
		}
		if (index == length) {
			Shell[] newModalShells = new Shell[length + 4];
			System.arraycopy(modalShells, 0, newModalShells, 0, length);
			modalShells = newModalShells;
		}
		modalShells[index] = shell;
		Shell[] shells = getShells();
		for (int i = 0; i < shells.length; i++) {
			shells[i].updateModal();
		}
	}

	/**
	 * Sets the synchronizer used by the display to be the argument, which can
	 * not be null.
	 * 
	 * @param synchronizer
	 *            the new synchronizer for the display (must not be null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the synchronizer is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_FAILED_EXEC - if an exception occurred while
	 *                running an inter-thread message</li>
	 *                </ul>
	 */
	public void setSynchronizer(Synchronizer synchronizer) {
		checkDevice();
		if (synchronizer == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (synchronizer == this.synchronizer) {
			return;
		}
		Synchronizer oldSynchronizer;
		synchronized (Device.class) {
			oldSynchronizer = this.synchronizer;
			this.synchronizer = synchronizer;
		}
		if (oldSynchronizer != null) {
			oldSynchronizer.runAsyncMessages(true);
		}
	}

	/**
	 * Causes the user-interface thread to <em>sleep</em> (that is, to be put in
	 * a state where it does not consume CPU cycles) until an event is received
	 * or it is otherwise awakened.
	 * 
	 * @return <code>true</code> if an event requiring dispatching was placed on
	 *         the queue.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #wake
	 */
	public boolean sleep() {
		checkDevice();
		if (getMessageCount() != 0) {
			return true;
		}

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// awake
		}
		QApplication.processEvents(PROCESS_EVENTS_FLAGS, 1000);

		if (isDisposed()) {
			destroy();
		}
		return true;
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread at the next reasonable opportunity. The thread
	 * which calls this method is suspended until the runnable completes.
	 * Specifying <code>null</code> as the runnable simply wakes the
	 * user-interface thread.
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the
	 * receiver as their display may have been disposed. Therefore, it is
	 * necessary to check for this case inside the runnable before accessing the
	 * widget.
	 * </p>
	 * 
	 * @param runnable
	 *            code to run on the user-interface thread or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_FAILED_EXEC - if an exception occurred when
	 *                executing the runnable</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #asyncExec
	 */
	public void syncExec(Runnable runnable) {
		Synchronizer synchronizer;
		synchronized (Device.class) {
			if (isDisposed()) {
				error(SWT.ERROR_DEVICE_DISPOSED);
			}
			synchronizer = this.synchronizer;
		}
		synchronizer.syncExec(runnable);
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread after the specified number of milliseconds have
	 * elapsed. If milliseconds is less than zero, the runnable is not executed.
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the
	 * receiver as their display may have been disposed. Therefore, it is
	 * necessary to check for this case inside the runnable before accessing the
	 * widget.
	 * </p>
	 * 
	 * @param milliseconds
	 *            the delay before running the runnable
	 * @param runnable
	 *            code to run on the user-interface thread
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #asyncExec
	 */

	public void timerExec(int milliseconds, Runnable runnable) {
		checkDevice();
		timer = new QTimer();

		if (runnable == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (timerList == null) {
			timerList = new Runnable[4];
		}
		if (timerIds == null) {
			timerIds = new int /* long */[4];
		}
		int index = 0;
		while (index < timerList.length) {
			if (timerList[index] == runnable) {
				break;
			}
			index++;
		}
		int /* long */timerId = 0;
		if (index != timerList.length) {
			timerId = timerIds[index];
			if (milliseconds < 0) {
				timer.killTimer(timerId);
				timerList[index] = null;
				timerIds[index] = 0;
				return;
			}
		} else {
			if (milliseconds < 0) {
				return;
			}
			index = 0;
			while (index < timerList.length) {
				if (timerList[index] == null) {
					break;
				}
				index++;
			}
			timerId = nextTimerId++;
			if (index == timerList.length) {
				Runnable[] newTimerList = new Runnable[timerList.length + 4];
				System.arraycopy(timerList, 0, newTimerList, 0, timerList.length);
				timerList = newTimerList;
				int /* long */[] newTimerIds = new int /* long */[timerIds.length + 4];
				System.arraycopy(timerIds, 0, newTimerIds, 0, timerIds.length);
				timerIds = newTimerIds;
			}
		}

		timer.setSingleShot(false);
		timer.start(milliseconds);
		final QTimerEvent qte = new QTimerEvent(timer.timerId());
		timer.timeout.connect(new QObject() {
			private int id = qte.timerId();

			@SuppressWarnings("unused")
			protected void idTransfer() {
				runTimer(id);
			}
		}, "idTransfer()"); //$NON-NLS-1$
		timerList[index] = runnable;
		timerIds[index] = timer.timerId();
	}

	/**
	 * Forces all outstanding paint requests for the display to be processed
	 * before this method returns.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Control#update()
	 */
	public void update() {
		checkDevice();
		if (QApplication.hasPendingEvents()) {
			QApplication.processEvents(QEventLoop.ProcessEventsFlag.ExcludeUserInputEvents);
		}
	}

	/**
	 * If the receiver's user-interface thread was <code>sleep</code>ing, causes
	 * it to be awakened and start running again. Note that this method may be
	 * called from any thread.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #sleep
	 */
	public void wake() {
		synchronized (Device.class) {
			if (isDisposed()) {
				error(SWT.ERROR_DEVICE_DISPOSED);
			}
			if (thread == Thread.currentThread()) {
				return;
			}
			wakeThread();
		}
	}

	void wakeThread() {
		try {
			thread.notify();
		} catch (Exception e) {
			// ok
		}
	}

	static String withCrLf(String string) {

		/* If the string is empty, return the string. */
		int length = string.length();
		if (length == 0) {
			return string;
		}

		/*
		 * Check for an LF or CR/LF and assume the rest of the string is
		 * formated that way. This will not work if the string contains mixed
		 * delimiters.
		 */
		int i = string.indexOf('\n', 0);
		if (i == -1) {
			return string;
		}
		if (i > 0 && string.charAt(i - 1) == '\r') {
			return string;
		}

		/*
		 * The string is formatted with LF. Compute the number of lines and the
		 * size of the buffer needed to hold the result
		 */
		i++;
		int count = 1;
		while (i < length) {
			if ((i = string.indexOf('\n', i)) == -1) {
				break;
			}
			count++;
			i++;
		}
		count += length;

		/* Create a new string with the CR/LF line terminator. */
		i = 0;
		StringBuffer result = new StringBuffer(count);
		while (i < length) {
			int j = string.indexOf('\n', i);
			if (j == -1) {
				j = length;
			}
			result.append(string.substring(i, j));
			if ((i = j) < length) {
				result.append("\r\n"); //$NON-NLS-1$
				i++;
			}
		}
		return result.toString();
	}

	private final class MasterEventFilter extends QObject {
		private final Display display;
		private final boolean printEvents = Boolean.getBoolean("swtqt.printEvents"); //$NON-NLS-1$

		public MasterEventFilter(Display display) {
			this.display = display;
		}

		@Override
		public boolean eventFilter(QObject obj, QEvent event) {
			try {
				if (printEvents) {
					System.out.println("QObject: " + (obj == null ? "null" : obj.toString()) + ": " + event.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				lastEventTime = QTime.currentTime().elapsed();
				Widget eventSource = display.findControl(obj);
				if (eventSource == null) {
					//					if (event.type() == Type.Paint) {
					//						System.out.println("paint event for qObj: " + obj + " unknown swt source"); //$NON-NLS-1$ //$NON-NLS-2$
					//					}
					if (printEvents) {
						System.err.println("unknown event source: " + obj + " parent: " //$NON-NLS-1$ //$NON-NLS-2$
								+ (obj == null ? "null" : obj.parent())); //$NON-NLS-1$
					}
					return false;
				}
				switch (event.type()) {
				case Paint:
					if (printEvents) {
						System.out.println("paint event for qObj: " + obj + " swt: " + eventSource); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (ignorePaintEvents) {
						return false;
					}
					return eventSource.qtPaintEvent(obj, (QPaintEvent) event);
				case Resize:
					return eventSource.qtResizeEvent(obj, (QResizeEvent) event);
				case Move:
					return eventSource.qtMoveEvent(obj, (QMoveEvent) event);
				case FocusIn:
					eventSource.qtFocusInEvent(obj);
					return false;
				case FocusOut:
					eventSource.qtFocusOutEvent(obj);
					return false;
				case ContextMenu: {
					QContextMenuEvent contextEvent = (QContextMenuEvent) event;
					boolean accepted = eventSource.qtContextMenuEvent(obj, contextEvent);
					if (accepted) {
						contextEvent.setAccepted(true);
					}
					return accepted;
				}
				case Close:
					QCloseEvent closeEvent = (QCloseEvent) event;
					boolean accepted = eventSource.qtCloseEvent();
					if (accepted) {
						closeEvent.ignore();
					}
					return accepted;
				case Enter:
					return eventSource.qtMouseEnterEvent(obj);
				case Leave:
					return eventSource.qtMouseLeaveEvent(obj);
				case Show:
					return eventSource.qtShowEvent(obj);
				case Hide:
					return eventSource.qtHideEvent(obj);
				case MouseMove:
					return eventSource.qtMouseMoveEvent(obj, (QMouseEvent) event);
				case MouseButtonPress:
					QMouseEvent mouseEvent = (QMouseEvent) event;
					// for debug
					//				QWidget widget = QApplication.widgetAt(mouseEvent.globalX(), mouseEvent.globalY());
					//				System.out.println("mouse click on: " + widget);
					//				System.out.println("mouse click on swt widget: " + display.findControl(widget));
					return eventSource.qtMouseButtonPressEvent(obj, mouseEvent);
				case MouseButtonRelease:
					return eventSource.qtMouseButtonReleaseEvent(obj, (QMouseEvent) event);
				case MouseButtonDblClick:
					return eventSource.qtMouseButtonDblClickEvent(obj, (QMouseEvent) event);
				case KeyPress:
					return eventSource.qtKeyPressEvent(obj, (QKeyEvent) event);
				case KeyRelease:
					return eventSource.qtKeyReleaseEvent(obj, (QKeyEvent) event);
				case WindowActivate:
					return eventSource.qtWindowActivateEvent(obj);
				case WindowDeactivate:
					return eventSource.qtWindowDeactivateEvent(obj);
				case WindowStateChange:
					return eventSource.qtWindowStateChangeEvent(obj, (QWindowStateChangeEvent) event);
				case WinEventAct:
					return false;
				case Timer:
					return false;
				default:
					if (printEvents) {
						System.err.println("unhandled event: " + event); //$NON-NLS-1$
					}
				}
			} catch (Exception e) {
				System.err.println("error during event processing:"); //$NON-NLS-1$
				e.printStackTrace();
			}
			return false;
		}

	}

	private final static class SwtMessageHandler extends QMessageHandler {

		@Override
		public void warning(String msg) {
			System.err.println("Qt-Warning: " + msg); //$NON-NLS-1$
		}

		@Override
		public void fatal(String msg) {
			System.err.println("Qt-Fatal: " + msg); //$NON-NLS-1$
		}

		@Override
		public void debug(String msg) {
			System.err.println("Qt-Debug: " + msg); //$NON-NLS-1$
		}

		@Override
		public void critical(String msg) {
			System.err.println("Qt-Critical: " + msg); //$NON-NLS-1$
		}
	};

}
