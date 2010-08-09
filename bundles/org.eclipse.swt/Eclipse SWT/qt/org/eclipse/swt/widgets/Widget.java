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

import java.util.HashMap;
import java.util.Map;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWindowStateChangeEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.internal.SWTEventListener;

/**
 * This class is the abstract superclass of all user interface objects. Widgets
 * are created, disposed and issue notification to listeners when events occur
 * which affect them.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation. However, it has not been marked final to allow those
 * outside of the SWT development team to implement patched versions of the
 * class in order to get around specific limitations in advance of when those
 * limitations can be addressed by the team. Any class built using subclassing
 * to access the internals of this class will likely fail to compile or run
 * between releases and may be strongly platform specific. Subclassing should
 * not be attempted without an intimate and detailed understanding of the
 * workings of the hierarchy. No support is provided for user-written classes
 * which are implemented as subclasses of this class.
 * </p>
 * 
 * @see #checkSubclass
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */

public abstract class Widget {
	public static final String QT_SWT_CONTROL_PROPERTY = "__SWT_CONTROL";//$NON-NLS-1$
	int style, state;
	Display display;
	EventTable eventTable;
	private Map<String, Object> data;
	private Object mainData;

	/* Global state flags */
	static final int DISPOSED = 1 << 0;
	static final int CANVAS = 1 << 1;
	static final int KEYED_DATA = 1 << 2;
	static final int HIDDEN = 1 << 4;

	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED = 1 << 5;

	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1 << 6;

	/* A layout was requested in this widget hierarchy */
	static final int LAYOUT_CHILD = 1 << 7;

	/* Background flags */
	static final int THEME_BACKGROUND = 1 << 8;
	static final int DRAW_BACKGROUND = 1 << 9;
	static final int PARENT_BACKGROUND = 1 << 10;

	/* Dispose and release flags */
	static final int RELEASED = 1 << 11;
	static final int DISPOSE_SENT = 1 << 12;

	/* More global widget state flags */
	static final int TRACK_MOUSE = 1 << 13;
	static final int FOREIGN_HANDLE = 1 << 14;
	static final int DRAG_DETECT = 1 << 15;

	/* Move and resize state flags */
	static final int MOVE_OCCURRED = 1 << 16;
	static final int MOVE_DEFERRED = 1 << 17;
	static final int RESIZE_OCCURRED = 1 << 18;
	static final int RESIZE_DEFERRED = 1 << 19;

	/* Ignore WM_CHANGEUISTATE */
	static final int IGNORE_WM_CHANGEUISTATE = 1 << 20;

	/* Default size for widgets */
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;

	/* Check and initialize the Common Controls DLL */
	static final int MAJOR = 5, MINOR = 80;
	private QWidget qWidget;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Widget() {
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
	 * @see SWT
	 * @see #checkSubclass
	 * @see #getStyle
	 */
	public Widget(Widget parent, int style) {
		checkSubclass();
		checkParent(parent);
		this.style = style;
		display = parent.display;
	}

	void setQWidget(QWidget qWidget) {
		this.qWidget = qWidget;
	}

	public QWidget getQWidget() {
		return qWidget;
	}

	protected QWidget getQMasterWidget() {
		return getQWidget();
	}

	protected void setStyleSheet(String style) {
		getQMasterWidget().setStyleSheet(style);
		updateLayout();
	}

	protected void updateStyleSheet() {
		if (null != getQMasterWidget().style()) {
			getQMasterWidget().setStyle(getQMasterWidget().style());
			updateLayout();
		}
	}

	protected void updateLayout() {
		// nothing by default
	}

	void _addListener(int eventType, Listener listener) {
		if (eventTable == null) {
			eventTable = new EventTable();
		}
		if (eventType == SWT.DragDetect && this instanceof CTabFolder) {
			System.out.println("adding dnd drag listener");
		}
		eventTable.hook(eventType, listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when an event of the given type occurs. When the event does occur in the
	 * widget, the listener is notified by sending it the
	 * <code>handleEvent()</code> message. The event type is one of the event
	 * constants defined in class <code>SWT</code>.
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
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #getListeners(int)
	 * @see #removeListener(int, Listener)
	 * @see #notifyListeners
	 */
	public void addListener(int eventType, Listener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		_addListener(eventType, listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the widget is disposed. When the widget is disposed, the listener is
	 * notified by sending it the <code>widgetDisposed()</code> message.
	 * 
	 * @param listener
	 *            the listener which should be notified when the receiver is
	 *            disposed
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
	 * @see DisposeListener
	 * @see #removeDisposeListener
	 */
	public void addDisposeListener(DisposeListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Dispose, typedListener);
	}

	/**
	 * Returns a style with exactly one style bit set out of the specified set
	 * of exclusive style bits. All other possible bits are cleared when the
	 * first matching bit is found. Bits that are not part of the possible set
	 * are untouched.
	 * 
	 * @param style
	 *            the original style bits
	 * @param int0
	 *            the 0th possible style bit
	 * @param int1
	 *            the 1st possible style bit
	 * @param int2
	 *            the 2nd possible style bit
	 * @param int3
	 *            the 3rd possible style bit
	 * @param int4
	 *            the 4th possible style bit
	 * @param int5
	 *            the 5th possible style bit
	 * 
	 * @return the new style bits
	 * 
	 *         refactored: was checkBits
	 */
	static int checkBits(int style, int int0, int int1, int int2, int int3, int int4, int int5) {
		int mask = int0 | int1 | int2 | int3 | int4 | int5;
		if ((style & mask) == 0) {
			style |= int0;
		}
		if ((style & int0) != 0) {
			style = style & ~mask | int0;
		}
		if ((style & int1) != 0) {
			style = style & ~mask | int1;
		}
		if ((style & int2) != 0) {
			style = style & ~mask | int2;
		}
		if ((style & int3) != 0) {
			style = style & ~mask | int3;
		}
		if ((style & int4) != 0) {
			style = style & ~mask | int4;
		}
		if ((style & int5) != 0) {
			style = style & ~mask | int5;
		}
		return style;
	}

	/* refactored: was checkOrientation */
	void checkAndUpdateOrientation(Widget parent) {
		style &= ~SWT.MIRRORED;
		if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
			if (parent != null) {
				if ((parent.style & SWT.LEFT_TO_RIGHT) != 0) {
					style |= SWT.LEFT_TO_RIGHT;
				}
				if ((parent.style & SWT.RIGHT_TO_LEFT) != 0) {
					style |= SWT.RIGHT_TO_LEFT;
				}
			}
		}
		style = checkBits(style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
	}

	void checkOpened() {
		/* Do nothing */
	}

	/**
	 * Throws an exception if the specified widget can not be used as a parent
	 * for the receiver.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	void checkParent(Widget parent) {
		if (parent == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (parent.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		parent.checkWidget();
		parent.checkOpened();
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * The SWT class library is intended to be subclassed only at specific,
	 * controlled points (most notably, <code>Composite</code> and
	 * <code>Canvas</code> when implementing new widgets). This method enforces
	 * this rule unless it is overridden.
	 * </p>
	 * <p>
	 * <em>IMPORTANT:</em> By providing an implementation of this method that
	 * allows a subclass of a class which does not normally allow subclassing to
	 * be created, the implementer agrees to be fully responsible for the fact
	 * that any such subclass will likely fail between SWT releases and will be
	 * strongly platform specific. No support is provided for user-written
	 * classes which are implemented in this fashion.
	 * </p>
	 * <p>
	 * The ability to subclass outside of the allowed SWT classes is intended
	 * purely to enable those not on the SWT development team to implement
	 * patches in order to get around specific limitations in advance of when
	 * those limitations can be addressed by the team. Subclassing should not be
	 * attempted without an intimate and detailed understanding of the
	 * hierarchy.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 */
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	/**
	 * Throws an <code>SWTException</code> if the receiver can not be accessed
	 * by the caller. This may include both checks on the state of the receiver
	 * and more generally on the entire execution context. This method
	 * <em>should</em> be called by widget implementors to enforce the standard
	 * SWT invariants.
	 * <p>
	 * Currently, it is an error to invoke any method (other than
	 * <code>isDisposed()</code>) on a widget that has had its
	 * <code>dispose()</code> method called. It is also an error to call widget
	 * methods from any thread that is different from the thread that created
	 * the widget.
	 * </p>
	 * <p>
	 * In future releases of SWT, there may be more or fewer error checks and
	 * exceptions may be thrown for different reasons.
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
	protected void checkWidget() {
		Display display = this.display;
		if (display == null) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		if (display.thread != Thread.currentThread()) {
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if ((state & DISPOSED) != 0) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
	}

	/**
	 * Destroys the widget in the operating system and releases the widget's
	 * handle. If the widget does not have a handle, this method may hide the
	 * widget, mark the widget as destroyed or do nothing, depending on the
	 * widget.
	 * <p>
	 * When a widget is destroyed in the operating system, its descendants are
	 * also destroyed by the operating system. This means that it is only
	 * necessary to call <code>destroyWidget</code> on the root of the widget
	 * tree.
	 * </p>
	 * <p>
	 * This method is called after <code>releaseWidget()</code>.
	 * </p>
	 * <p>
	 * See also <code>releaseChild()</code>, <code>releaseWidget()</code> and
	 * <code>releaseHandle()</code>.
	 * </p>
	 * 
	 * @see #dispose
	 */
	void destroyWidget() {
		releaseQWidget();
	}

	/**
	 * Disposes of the operating system resources associated with the receiver
	 * and all its descendants. After this method has been invoked, the receiver
	 * and all descendants will answer <code>true</code> when sent the message
	 * <code>isDisposed()</code>. Any internal connections between the widgets
	 * in the tree will have been removed to facilitate garbage collection.
	 * <p>
	 * NOTE: This method is not called recursively on the descendants of the
	 * receiver. This means that, widget implementers can not detect when a
	 * widget is being disposed of by re-implementing this method, but should
	 * instead listen for the <code>Dispose</code> event.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #addDisposeListener
	 * @see #removeDisposeListener
	 * @see #checkWidget
	 */
	public void dispose() {
		/*
		 * Note: It is valid to attempt to dispose a widget more than once. If
		 * this happens, fail silently.
		 */
		if (isDisposed()) {
			return;
		}

		if (!isValidThread()) {
			error(SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		release(true);
	}

	/**
	 * Does whatever widget specific cleanup is required, and then uses the code
	 * in <code>SWTError.error</code> to handle the error.
	 * 
	 * @param code
	 *            the descriptive error code
	 * 
	 * @see SWT#error(int)
	 */
	void error(int code) {
		SWT.error(code);
	}

	boolean filters(int eventType) {
		return display.filters(eventType);
	}

	char[] fixMnemonic(String string) {
		return fixMnemonic(string, false);
	}

	char[] fixMnemonic(String string, boolean spaces) {
		char[] buffer = new char[string.length() + 1];
		string.getChars(0, string.length(), buffer, 0);
		int i = 0, j = 0;
		while (i < buffer.length) {
			if (buffer[i] == '&') {
				if (i + 1 < buffer.length && buffer[i + 1] == '&') {
					if (spaces) {
						buffer[j] = ' ';
					}
					j++;
					i++;
				}
				i++;
			} else {
				buffer[j++] = buffer[i++];
			}
		}
		while (j < buffer.length) {
			buffer[j++] = 0;
		}
		return buffer;
	}

	/**
	 * Returns the <code>Display</code> that is associated with the receiver.
	 * <p>
	 * A widget's display is either provided when it is created (for example,
	 * top level <code>Shell</code>s) or is the same as its parent's display.
	 * </p>
	 * 
	 * @return the receiver's display
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Display getDisplay() {
		Display display = this.display;
		if (display == null) {
			error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return display;
	}

	/**
	 * Returns an array of listeners who will be notified when an event of the
	 * given type occurs. The event type is one of the event constants defined
	 * in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @return an array of listeners that will be notified when the event occurs
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #addListener(int, Listener)
	 * @see #removeListener(int, Listener)
	 * @see #notifyListeners
	 * 
	 * @since 3.4
	 */
	public Listener[] getListeners(int eventType) {
		checkWidget();
		if (eventTable == null) {
			return new Listener[0];
		}
		return eventTable.getListeners(eventType);
	}

	Menu getMenu() {
		return null;
	}

	/**
	 * Returns the name of the widget. This is the name of the class without the
	 * package name.
	 * 
	 * @return the name of the widget
	 */
	String getName() {
		String string = getClass().getName();
		int index = string.lastIndexOf('.');
		if (index == -1) {
			return string;
		}
		return string.substring(index + 1, string.length());
	}

	/*
	 * Returns a short printable representation for the contents of a widget.
	 * For example, a button may answer the label text. This is used by
	 * <code>toString</code> to provide a more meaningful description of the
	 * widget.
	 * 
	 * @return the contents string for the widget
	 * 
	 * @see #toString
	 */
	String getNameText() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the receiver's style information.
	 * <p>
	 * Note that the value which is returned by this method <em>may
	 * not match</em> the value which was provided to the constructor when the
	 * receiver was created. This can occur when the underlying operating system
	 * does not support a particular combination of requested styles. For
	 * example, if the platform widget used to implement a particular SWT widget
	 * always has scroll bars, the result of calling this method would always
	 * have the <code>SWT.H_SCROLL</code> and <code>SWT.V_SCROLL</code> bits
	 * set.
	 * </p>
	 * 
	 * @return the style bits
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getStyle() {
		checkWidget();
		return style;
	}

	/*
	 * Returns <code>true</code> if the specified eventType is hooked, and
	 * <code>false</code> otherwise. Implementations of SWT can avoid creating
	 * objects and sending events when an event happens in the operating system
	 * but there are no listeners hooked for the event.
	 * 
	 * @param eventType the event to be checked
	 * 
	 * @return <code>true</code> when the eventType is hooked and
	 * <code>false</code> otherwise
	 * 
	 * @see #isListening
	 */
	boolean hooks(int eventType) {
		if (eventTable == null) {
			return false;
		}
		return eventTable.hooks(eventType);
	}

	/**
	 * Returns <code>true</code> if the widget has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the widget. When a widget has been
	 * disposed, it is an error to invoke any other method using the widget.
	 * </p>
	 * 
	 * @return <code>true</code> when the widget is disposed and
	 *         <code>false</code> otherwise
	 */
	public boolean isDisposed() {
		return (state & DISPOSED) != 0;
	}

	/**
	 * Returns <code>true</code> if there are any listeners for the specified
	 * event type associated with the receiver, and <code>false</code>
	 * otherwise. The event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event
	 * @return true if the event is hooked
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
	public boolean isListening(int eventType) {
		checkWidget();
		return hooks(eventType);
	}

	/*
	 * Returns <code>true</code> when subclassing is allowed and
	 * <code>false</code> otherwise
	 * 
	 * @return <code>true</code> when subclassing is allowed and
	 * <code>false</code> otherwise
	 */
	boolean isValidSubclass() {
		return Display.isValidClass(getClass());
	}

	/*
	 * Returns <code>true</code> when the current thread is the thread that
	 * created the widget and <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> when the current thread is the thread that
	 * created the widget and <code>false</code> otherwise
	 */
	boolean isValidThread() {
		return getDisplay().isValidThread();
	}

	GC new_GC(GCData data) {
		return null;
	}

	/**
	 * Notifies all of the receiver's listeners for events of the given type
	 * that one such event has occurred by invoking their
	 * <code>handleEvent()</code> method. The event type is one of the event
	 * constants defined in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event which has occurred
	 * @param event
	 *            the event data
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
	 * @see #addListener
	 * @see #getListeners(int)
	 * @see #removeListener(int, Listener)
	 */
	public void notifyListeners(int eventType, Event event) {
		checkWidget();
		if (event == null) {
			event = new Event();
		}
		sendEvent(eventType, event);
	}

	void postEvent(int eventType) {
		sendEvent(eventType, null, false);
	}

	void postEvent(int eventType, Event event) {
		sendEvent(eventType, event, false);
	}

	/*
	 * Releases the widget hierarchy and optionally destroys the receiver. <p>
	 * Typically, a widget with children will broadcast this message to all
	 * children so that they too can release their resources. The
	 * <code>releaseHandle</code> method is used as part of this broadcast to
	 * zero the handle fields of the children without calling
	 * <code>destroyWidget</code>. In this scenario, the children are actually
	 * destroyed later, when the operating system destroys the widget tree. </p>
	 * 
	 * @param destroy indicates that the receiver should be destroyed
	 * 
	 * @see #dispose
	 * 
	 * @see #releaseParent
	 * 
	 * @see #releaseWidget
	 */
	void release(boolean destroy) {
		if ((state & DISPOSE_SENT) == 0) {
			state |= DISPOSE_SENT;
			sendEvent(SWT.Dispose);
		}
		if ((state & DISPOSED) == 0) {
			releaseChildren(destroy);
		}
		if ((state & RELEASED) == 0) {
			state |= RELEASED;
			if (destroy) {
				releaseParent();
				releaseWidget();
				destroyWidget();
			} else {
				releaseWidget();
				releaseQWidget();
			}
		}
	}

	void releaseChildren(boolean destroy) {
	}

	/*
	 * Releases the widget's handle by zero'ing it out. Does not destroy or
	 * release any operating system resources. <p> This method is called after
	 * <code>releaseWidget</code> or from <code>destroyWidget</code> when a
	 * widget is being destroyed to ensure that the widget is marked as
	 * destroyed in case the act of destroying the widget in the operating
	 * system causes application code to run in callback that could access the
	 * widget. </p>
	 * 
	 * @see #dispose
	 * 
	 * @see #releaseChildren
	 * 
	 * @see #releaseParent
	 * 
	 * @see #releaseWidget
	 */
	void releaseQWidget() {
		state |= DISPOSED;
		display = null;
		qWidget = null;
	}

	/*
	 * Releases the receiver, a child in a widget hierarchy, from its parent.
	 * <p> When a widget is destroyed, it may be necessary to remove it from an
	 * internal data structure of the parent. When a widget has no handle, it
	 * may also be necessary for the parent to hide the widget or otherwise
	 * indicate that the widget has been disposed. For example, disposing a menu
	 * bar requires that the menu bar first be released from the shell when the
	 * menu bar is active. </p>
	 * 
	 * @see #dispose
	 * 
	 * @see #releaseChildren
	 * 
	 * @see #releaseWidget
	 * 
	 * @see #releaseHandle
	 */
	void releaseParent() {
	}

	/*
	 * Releases any internal resources back to the operating system and clears
	 * all fields except the widget handle. <p> When a widget is destroyed,
	 * resources that were acquired on behalf of the programmer need to be
	 * returned to the operating system. For example, if the widget made a copy
	 * of an icon, supplied by the programmer, this copy would be freed in
	 * <code>releaseWidget</code>. Also, to assist the garbage collector and
	 * minimize the amount of memory that is not reclaimed when the programmer
	 * keeps a reference to a disposed widget, all fields except the handle are
	 * zero'd. The handle is needed by <code>destroyWidget</code>. </p>
	 * 
	 * @see #dispose
	 * 
	 * @see #releaseChildren
	 * 
	 * @see #releaseHandle
	 * 
	 * @see #releaseParent
	 */
	void releaseWidget() {
		eventTable = null;
		data = null;
		mainData = null;
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
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see SWT
	 * @see #addListener
	 * @see #getListeners(int)
	 * @see #notifyListeners
	 */
	public void removeListener(int eventType, Listener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(eventType, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when an event of the given type occurs.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT public API.
	 * It is marked public only so that it can be shared within the packages
	 * provided by SWT. It should never be referenced from application code.
	 * </p>
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
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see Listener
	 * @see #addListener
	 */
	protected void removeListener(int eventType, SWTEventListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(eventType, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the widget is disposed.
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
	 * @see DisposeListener
	 * @see #addDisposeListener
	 */
	public void removeDisposeListener(DisposeListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Dispose, listener);
	}

	boolean sendDragEvent(int button, int x, int y) {
		Event event = new Event();
		event.button = button;
		event.x = x;
		event.y = y;
		//setInputState(event, SWT.DragDetect);
		sendEvent(SWT.DragDetect, event);
		if (isDisposed()) {
			return false;
		}
		return event.doit;
	}

	void sendEvent(Event event) {
		event.display = display;
		if (!display.filterEvent(event)) {
			if (eventTable != null) {
				eventTable.sendEvent(event);
			}
		}
	}

	void sendEvent(int eventType) {
		sendEvent(eventType, null, true);
	}

	void sendEvent(int eventType, Event event) {
		sendEvent(eventType, event, true);
	}

	void sendEvent(int eventType, Event event, boolean send) {
		if (eventTable == null && !display.filters(eventType)) {
			return;
		}
		if (event == null) {
			event = new Event();
		}
		event.type = eventType;
		event.display = display;
		event.widget = this;
		if (event.time == 0) {
			event.time = display.getLastEventTime();
		}
		if (send) {
			sendEvent(event);
		} else {
			display.postEvent(event);
		}
	}

	/**
	 * Returns the application defined widget data associated with the receiver,
	 * or null if it has not been set. The <em>widget data</em> is a single,
	 * unnamed field that is stored with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If the object
	 * stored in the widget data needs to be notified when the widget is
	 * disposed of, it is the application's responsibility to hook the Dispose
	 * event on the widget and do so.
	 * </p>
	 * 
	 * @return the widget data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - when the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - when called from the
	 *                wrong thread</li>
	 *                </ul>
	 * 
	 * @see #setData(Object)
	 */
	public Object getData() {
		checkWidget();
		return mainData;
	}

	/**
	 * Returns the application defined property of the receiver with the
	 * specified name, or null if it has not been set.
	 * <p>
	 * Applications may have associated arbitrary objects with the receiver in
	 * this fashion. If the objects stored in the properties need to be notified
	 * when the widget is disposed of, it is the application's responsibility to
	 * hook the Dispose event on the widget and do so.
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
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setData(String, Object)
	 */
	public Object getData(String key) {
		checkWidget();
		if (key == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}

		Object value = getDataMap().get(key);
		if (value == null) {
			if (getQMasterWidget() != null) {
				value = getQMasterWidget().property(key);
			}
		}
		return value;
	}

	private Map<String, Object> getDataMap() {
		if (data == null) {
			data = new HashMap<String, Object>(4);
		}
		return data;
	}

	/**
	 * Sets the application defined widget data associated with the receiver to
	 * be the argument. The <em>widget
	 * data</em> is a single, unnamed field that is stored with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If the object
	 * stored in the widget data needs to be notified when the widget is
	 * disposed of, it is the application's responsibility to hook the Dispose
	 * event on the widget and do so.
	 * </p>
	 * 
	 * @param data
	 *            the widget data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - when the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - when called from the
	 *                wrong thread</li>
	 *                </ul>
	 * 
	 * @see #getData()
	 */
	public void setData(Object data) {
		checkWidget();
		this.mainData = data;
	}

	/**
	 * Sets the application defined property of the receiver with the specified
	 * name to the given value.
	 * <p>
	 * Applications may associate arbitrary objects with the receiver in this
	 * fashion. If the objects stored in the properties need to be notified when
	 * the widget is disposed of, it is the application's responsibility to hook
	 * the Dispose event on the widget and do so.
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
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getData(String)
	 */
	public void setData(String key, Object value) {
		checkWidget();
		if (key == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if ("stylesheet".equals(key)) { //$NON-NLS-1$
			setStyleSheet((String) value);
			return;
		}

		if (value == null) {
			if (getDataMap().containsKey(key)) {
				getDataMap().remove(key);
			} else {
				if (getQMasterWidget() != null) {
					getQMasterWidget().setProperty(key, null);
					updateStyleSheet(); // we have to update the style, cause the property can be referenced in the style
				}
			}
		} else {
			// we store Strings/Booleans adin the Qt Control, everything
			// else in the local map
			if (getQMasterWidget() != null && (value instanceof String || value instanceof Boolean)) {
				getQMasterWidget().setProperty(key, value);
				updateStyleSheet(); // we have to update the style, cause the property can be referenced in the style
			} else {
				getDataMap().put(key, value);
			}
		}
	}

	boolean showMenu(int x, int y) {
		Event event = new Event();
		event.x = x;
		event.y = y;
		sendEvent(SWT.MenuDetect, event);
		if (!event.doit) {
			return true;
		}
		Menu menu = getMenu();
		if (menu != null && !menu.isDisposed()) {
			if (x != event.x || y != event.y) {
				menu.setLocation(event.x, event.y);
			}
			menu.setVisible(true);
			return true;
		}
		return false;
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the receiver
	 */
	@Override
	public String toString() {
		return getName() + " {" + getDescription() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String getDescription() {
		if (isDisposed()) {
			return "*Disposed*"; //$NON-NLS-1$
		}
		if (!isValidThread()) {
			return "*Wrong Thread*"; //$NON-NLS-1$
		}
		return getNameText();
	}

	/* Qt Events */
	public boolean qtCloseEvent() {
		return false;
	}

	public boolean qtResizeEvent(QObject source, QResizeEvent resizeEvent) {
		return false;
	}

	public boolean qtMoveEvent(QObject source, QMoveEvent moveEvent) {
		return false;
	}

	public boolean qtPaintEvent(QObject source, QPaintEvent paintEvent) {
		return false;
	}

	public boolean qtKeyPressEvent(QObject source, QKeyEvent qEvent) {
		return false;
	}

	public boolean qtKeyReleaseEvent(QObject source, QKeyEvent qEvent) {
		return false;
	}

	public boolean qtMouseMoveEvent(QObject source, QMouseEvent mouseEvent) {
		return false;
	}

	public boolean qtMouseButtonPressEvent(QObject source, QMouseEvent mouseEvent) {
		return false;
	}

	public boolean qtMouseButtonReleaseEvent(QObject source, QMouseEvent mouseEvent) {
		return false;
	}

	public boolean qtMouseButtonDblClickEvent(QObject source, QMouseEvent mouseEvent) {
		return false;
	}

	public boolean qtMouseEnterEvent(Object source) {
		return false;
	}

	public boolean qtMouseLeaveEvent(Object source) {
		return false;
	}

	public boolean qtContextMenuEvent(Object source, QContextMenuEvent event) {
		return false;
	}

	public boolean qtShowEvent(QObject source) {
		return false;
	}

	public boolean qtHideEvent(QObject source) {
		return false;
	}

	public void qtFocusInEvent(QObject source) {
		// nothing
	}

	public void qtFocusOutEvent(QObject source) {
		// nothing
	}

	public boolean qtWindowActivateEvent(QObject source) {
		return false;
	}

	public boolean qtWindowDeactivateEvent(QObject obj) {
		return false;
	}

	public boolean qtWindowStateChangeEvent(QObject obj, QWindowStateChangeEvent event) {
		return false;
	}

}
