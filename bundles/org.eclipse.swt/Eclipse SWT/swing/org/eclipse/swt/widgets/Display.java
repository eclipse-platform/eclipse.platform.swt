/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.swing.*;
import org.eclipse.swt.internal.swing.Compatibility;

/**
 * Instances of this class are responsible for managing the
 * connection between SWT and the underlying operating
 * system. Their most important function is to implement
 * the SWT event loop in terms of the platform event model.
 * They also provide various methods for accessing information
 * about the operating system, and have overall control over
 * the operating system resources which SWT allocates.
 * <p>
 * Applications which are built with SWT will <em>almost always</em>
 * require only a single display. In particular, some platforms
 * which SWT supports will not allow more than one <em>active</em>
 * display. In other words, some platforms do not support
 * creating a new display if one already exists that has not been
 * sent the <code>dispose()</code> message.
 * <p>
 * In SWT, the thread which creates a <code>Display</code>
 * instance is distinguished as the <em>user-interface thread</em>
 * for that display.
 * </p>
 * The user-interface thread for a particular display has the
 * following special attributes:
 * <ul>
 * <li>
 * The event loop for that display must be run from the thread.
 * </li>
 * <li>
 * Some SWT API methods (notably, most of the public methods in
 * <code>Widget</code> and its subclasses), may only be called
 * from the thread. (To support multi-threaded user-interface
 * applications, class <code>Display</code> provides inter-thread
 * communication methods which allow threads other than the
 * user-interface thread to request that it perform operations
 * on their behalf.)
 * </li>
 * <li>
 * The thread is not allowed to construct other
 * <code>Display</code>s until that display has been disposed.
 * (Note that, this is in addition to the restriction mentioned
 * above concerning platform support for multiple displays. Thus,
 * the only way to have multiple simultaneously active displays,
 * even on platforms which support it, is to have multiple threads.)
 * </li>
 * </ul>
 * Enforcing these attributes allows SWT to be implemented directly
 * on the underlying operating system's event model. This has
 * numerous benefits including smaller footprint, better use of
 * resources, safer memory management, clearer program logic,
 * better performance, and fewer overall operating system threads
 * required. The down side however, is that care must be taken
 * (only) when constructing multi-threaded applications to use the
 * inter-thread communication mechanisms which this class provides
 * when required.
 * </p><p>
 * All SWT API methods which may only be called from the user-interface
 * thread are distinguished in their documentation by indicating that
 * they throw the "<code>ERROR_THREAD_INVALID_ACCESS</code>"
 * SWT exception.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Close, Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see Device#dispose
 */

public class Display extends Device {

	Consumer<RuntimeException> runtimeExceptionHandler = DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER;
	Consumer<Error> errorHandler = DefaultExceptionHandler.RUNTIME_ERROR_HANDLER;

	Event [] eventQueue;
	EventTable eventTable, filterTable;
  Vector timerList = new Vector();

	/* Menus */
	Menu [] bars, popups;
	ArrayList menuItemsList = new ArrayList();

//	static final String AWT_WINDOW_CLASS = "SunAwtWindow";

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;

	/* Display Shutdown */
  ArrayList disposeList;
//	Runnable [] disposeList;

	/* System Tray */
	Tray tray;

	/* System Images Cache */
	java.awt.Image errorIcon, workingIcon, infoIcon, questionIcon, warningIcon;

	/* System Cursors Cache */
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];

	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;

	/* Key Mappings */
	static final int [] [] KeyTable = {

		/* Keyboard and Mouse Masks */
		{java.awt.event.KeyEvent.VK_ALT,	SWT.ALT},
		{java.awt.event.KeyEvent.VK_SHIFT,	SWT.SHIFT},
		{java.awt.event.KeyEvent.VK_CONTROL,	SWT.CONTROL},
		{java.awt.event.KeyEvent.VK_WINDOWS,	SWT.COMMAND},

		/* NOT CURRENTLY USED */
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},

		/* Non-Numeric Keypad Keys */
		{java.awt.event.KeyEvent.VK_UP,		SWT.ARROW_UP},
		{java.awt.event.KeyEvent.VK_DOWN,	SWT.ARROW_DOWN},
		{java.awt.event.KeyEvent.VK_LEFT,	SWT.ARROW_LEFT},
		{java.awt.event.KeyEvent.VK_RIGHT,	SWT.ARROW_RIGHT},
		{java.awt.event.KeyEvent.VK_PAGE_UP,	SWT.PAGE_UP},
		{java.awt.event.KeyEvent.VK_PAGE_DOWN,	SWT.PAGE_DOWN},
		{java.awt.event.KeyEvent.VK_HOME,	SWT.HOME},
		{java.awt.event.KeyEvent.VK_END,		SWT.END},
		{java.awt.event.KeyEvent.VK_INSERT,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{java.awt.event.KeyEvent.VK_BACK_SPACE,	SWT.BS},
		{java.awt.event.KeyEvent.VK_ENTER,	SWT.CR},
		{java.awt.event.KeyEvent.VK_DELETE,	SWT.DEL},
		{java.awt.event.KeyEvent.VK_ESCAPE,	SWT.ESC},
		{java.awt.event.KeyEvent.VK_ENTER,	SWT.LF},
		{java.awt.event.KeyEvent.VK_TAB,		SWT.TAB},

		/* Functions Keys */
		{java.awt.event.KeyEvent.VK_F1,	SWT.F1},
		{java.awt.event.KeyEvent.VK_F2,	SWT.F2},
		{java.awt.event.KeyEvent.VK_F3,	SWT.F3},
		{java.awt.event.KeyEvent.VK_F4,	SWT.F4},
		{java.awt.event.KeyEvent.VK_F5,	SWT.F5},
		{java.awt.event.KeyEvent.VK_F6,	SWT.F6},
		{java.awt.event.KeyEvent.VK_F7,	SWT.F7},
		{java.awt.event.KeyEvent.VK_F8,	SWT.F8},
		{java.awt.event.KeyEvent.VK_F9,	SWT.F9},
		{java.awt.event.KeyEvent.VK_F10,	SWT.F10},
		{java.awt.event.KeyEvent.VK_F11,	SWT.F11},
		{java.awt.event.KeyEvent.VK_F12,	SWT.F12},
		{java.awt.event.KeyEvent.VK_F13,	SWT.F13},
		{java.awt.event.KeyEvent.VK_F14,	SWT.F14},
		{java.awt.event.KeyEvent.VK_F15,	SWT.F15},

		/* Numeric Keypad Keys */
		{java.awt.event.KeyEvent.VK_MULTIPLY,	SWT.KEYPAD_MULTIPLY},
		{java.awt.event.KeyEvent.VK_ADD,			SWT.KEYPAD_ADD},
		{java.awt.event.KeyEvent.VK_ENTER,		SWT.KEYPAD_CR},
		{java.awt.event.KeyEvent.VK_SUBTRACT,	SWT.KEYPAD_SUBTRACT},
		{java.awt.event.KeyEvent.VK_DECIMAL,		SWT.KEYPAD_DECIMAL},
		{java.awt.event.KeyEvent.VK_DIVIDE,		SWT.KEYPAD_DIVIDE},
		{java.awt.event.KeyEvent.VK_NUMPAD0,		SWT.KEYPAD_0},
		{java.awt.event.KeyEvent.VK_NUMPAD1,		SWT.KEYPAD_1},
		{java.awt.event.KeyEvent.VK_NUMPAD2,		SWT.KEYPAD_2},
		{java.awt.event.KeyEvent.VK_NUMPAD3,		SWT.KEYPAD_3},
		{java.awt.event.KeyEvent.VK_NUMPAD4,		SWT.KEYPAD_4},
		{java.awt.event.KeyEvent.VK_NUMPAD5,		SWT.KEYPAD_5},
		{java.awt.event.KeyEvent.VK_NUMPAD6,		SWT.KEYPAD_6},
		{java.awt.event.KeyEvent.VK_NUMPAD7,		SWT.KEYPAD_7},
		{java.awt.event.KeyEvent.VK_NUMPAD8,		SWT.KEYPAD_8},
		{java.awt.event.KeyEvent.VK_NUMPAD9,		SWT.KEYPAD_9},
//		{java.awt.event.KeyEvent.VK_????,		SWT.KEYPAD_EQUAL},

		/* Other keys */
		{java.awt.event.KeyEvent.VK_CAPS_LOCK,		SWT.CAPS_LOCK},
		{java.awt.event.KeyEvent.VK_NUM_LOCK,		SWT.NUM_LOCK},
		{java.awt.event.KeyEvent.VK_SCROLL_LOCK,		SWT.SCROLL_LOCK},
		{java.awt.event.KeyEvent.VK_PAUSE,		SWT.PAUSE},
		{java.awt.event.KeyEvent.VK_CANCEL,		SWT.BREAK},
		{java.awt.event.KeyEvent.VK_PRINTSCREEN,	SWT.PRINT_SCREEN},
		{java.awt.event.KeyEvent.VK_HELP,		SWT.HELP},

	};

	/* Multiple Displays */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Multiple Monitors */
//	static Monitor[] monitors = null;
//	static int monitorCount = 0;

	/* Modality */
	Shell [] modalShells;
	Shell modalDialogShell;
	static boolean TrimEnabled = false;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets."; //$NON-NLS-1$

  static {
    Utils.initializeProperties();
    CShell.ModalityHandler.initialize();
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      protected Window hoveredWindow;
      @Override
	public void eventDispatched(AWTEvent event) {
        java.awt.event.InputEvent ie = (java.awt.event.InputEvent)event;
        Utils.storeModifiersEx(ie.getModifiersEx());
        if(ie instanceof MouseEvent) {
          if(!Compatibility.IS_JAVA_5_OR_GREATER) {
            Utils.trackMouseProperties((MouseEvent)ie);
          }
          // It seems the mouse wheel event is sent to the wrong window. We have to retarget it in that case.
          Component component = ie.getComponent();
          if(component == null) {
            return;
          }
          Window window = component instanceof Window? (Window)component: SwingUtilities.getWindowAncestor(component);
          switch(ie.getID()) {
          case MouseEvent.MOUSE_WHEEL:
            if(window != hoveredWindow && hoveredWindow != null && window != null) {
              MouseWheelEvent mwe = (MouseWheelEvent)ie;
              mwe.consume();
              java.awt.Point mouseLocation = mwe.getPoint();
              mouseLocation = SwingUtilities.convertPoint(component, mouseLocation, hoveredWindow);
              Component c = hoveredWindow.findComponentAt(mouseLocation.x, mouseLocation.y);
              mouseLocation = SwingUtilities.convertPoint(hoveredWindow, mouseLocation, c);
              if(c != null) {
                c.dispatchEvent(new MouseWheelEvent(c, mwe.getID(), mwe.getWhen(), mwe.getModifiers(), mouseLocation.x, mouseLocation.y, mwe.getClickCount(), mwe.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation()));
              }
            }
            break;
          case MouseEvent.MOUSE_ENTERED:
            break;
          case MouseEvent.MOUSE_EXITED:
            if(hoveredWindow != null) {
              MouseEvent me = (MouseEvent)ie;
              java.awt.Point mouseLocation = me.getPoint();
              mouseLocation = SwingUtilities.convertPoint(component, mouseLocation, hoveredWindow);
              if(!hoveredWindow.contains(mouseLocation)) {
                hoveredWindow = null;
              }
            }
            break;
          default:
            if(window != null) {
              hoveredWindow = window;
            }
            break;
          }
          return;
        }
        if(ie.getID() == KeyEvent.KEY_PRESSED) {
          int dumpModifiers = KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK;
          if((Utils.modifiersEx & dumpModifiers) == dumpModifiers && ((KeyEvent)ie).getKeyCode() == KeyEvent.VK_F2) {
            Component component = ie.getComponent();
            Window window = component instanceof Window? (Window)component: SwingUtilities.getWindowAncestor(component);
            if(window instanceof CShell) {
              Component targetComponent;
              if(Compatibility.IS_JAVA_5_OR_GREATER) {
                java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mouseLocation, window);
                targetComponent = window.findComponentAt(mouseLocation);
              } else {
                targetComponent = null;
              }
              for(; targetComponent != null && !(targetComponent instanceof CControl); targetComponent = targetComponent.getParent());
              Control control;
              if(targetComponent != null) {
                control = ((CControl)targetComponent).getSWTHandle();
              } else {
                control = ((CShell)window).getSWTHandle();
              }
              Utils.dumpTree(control);
            }
          }
        }
      }
    }, AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
  }
	/*
	* TEMPORARY CODE.  Install the runnable that
	* gets the current display. This code will
	* be removed in the future.
	*/
	static {
		DeviceFinder = () -> {
			Device device = getCurrent ();
			if (device == null) {
				device = getDefault ();
			}
			setDevice (device);
		};
	}

/*
* TEMPORARY CODE.
*/
static void setDevice (Device device) {
	CurrentDevice = device;
}

/**
 * Constructs a new instance of this class.
 * <p>
 * Note: The resulting display is marked as the <em>current</em>
 * display. If this is the first display which has been
 * constructed since the application started, it is also
 * marked as the <em>default</em> display.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if called from a thread that already created an existing display</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see #getCurrent
 * @see #getDefault
 * @see Widget#checkSubclass
 * @see Shell
 */
public Display () {
	this (null);
}

/**
 * Constructs a new instance of this class using the parameter.
 *
 * @param data the device data
 */
public Display (DeviceData data) {
	super (data);
  boolean lightweightPopups = Utils.isLightweightPopups();
  ToolTipManager.sharedInstance().setLightWeightPopupEnabled(lightweightPopups);
  JPopupMenu.setDefaultLightWeightPopupEnabled(lightweightPopups);
  Utils.installDefaultLookAndFeel();
}

public static void main(final String[] args) {
  UIThreadUtils.main(args);
}

public static void swtExec(Runnable runnable) {
  UIThreadUtils.swtExec(runnable);
}

void addBar (Menu menu) {
	if (bars == null) bars = new Menu [4];
	int length = bars.length;
	for (int i=0; i<length; i++) {
		if (bars [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (bars [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newBars = new Menu [length + 4];
		System.arraycopy (bars, 0, newBars, 0, length);
		bars = newBars;
	}
	bars [index] = menu;
}

void addControl (Component handle, Control control) {
	if (handle == null) return;
  componentToControlMap.put(handle, control);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere
 * in a widget. The event type is one of the event constants
 * defined in class <code>SWT</code>. When the event does occur,
 * the listener is notified by sending it the <code>handleEvent()</code>
 * message.
 * <p>
 * Setting the type of an event to <code>SWT.None</code> from
 * within the <code>handleEvent()</code> method can be used to
 * change the event type and stop subsequent Java listeners
 * from running. Because event filters run before other listeners,
 * event filters can both block other listeners and set arbitrary
 * fields within an event. For this reason, event filters are both
 * powerful and dangerous. They should generally be avoided for
 * performance, debugging and code maintenance reasons.
 * </p>
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #removeFilter
 * @see #removeListener
 *
 * @since 3.0
 */
public void addFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable ();
	filterTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. The event
 * type is one of the event constants defined in class <code>SWT</code>.
 * When the event does occur in the display, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #removeListener
 *
 * @since 2.0
 */
public void addListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

void addMenuItem (MenuItem item) {
  menuItemsList.add(item);
}

void addPopup (Menu menu) {
	if (popups == null) popups = new Menu [4];
	int length = popups.length;
	for (int i=0; i<length; i++) {
		if (popups [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (popups [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newPopups = new Menu [length + 4];
		System.arraycopy (popups, 0, newPopups, 0, length);
		popups = newPopups;
	}
	popups [index] = menu;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next
 * reasonable opportunity. The caller of this method continues
 * to run in parallel, and is not notified when the
 * runnable has completed.  Specifying <code>null</code> as the
 * runnable simply wakes the user-interface thread when run.
 * <p>
 * Note that at the time the runnable is invoked, widgets
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 *
 * @param runnable code to run on the user-interface thread or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #syncExec
 */
public void asyncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.asyncExec (runnable);
}

/**
 * Causes the system hardware to emit a short sound
 * (if it supports this capability).
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void beep () {
	checkDevice ();
  Toolkit.getDefaultToolkit().beep();
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 */
protected void checkSubclass () {
	if (!isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread () && !SwingUtilities.isEventDispatchThread()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

static synchronized void checkDisplay (Thread thread, boolean multiple) {
  for (int i=0; i<Displays.length; i++) {
    if (Displays [i] != null) {
      if (!multiple) SWT.error (SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]");
      if (Displays [i].thread == thread) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
    }
  }
}

/**
 * Requests that the connection between SWT and the underlying
 * operating system be closed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Device#dispose
 *
 * @since 2.0
 */
public void close () {
	checkDevice ();
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit) dispose ();
}

/**
 * Creates the device in the operating system.  If the device
 * does not have a handle, this method may do nothing depending
 * on the device.
 * <p>
 * This method is called before <code>init</code>.
 * </p>
 *
 * @param data the DeviceData which describes the receiver
 *
 * @see #init
 */
@Override
protected void create (DeviceData data) {
	checkSubclass ();
	thread = Thread.currentThread ();
	UIThreadUtils.setMainThread(thread);
  checkDisplay (thread, true);
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
}

static synchronized void deregister (Display display) {
	for (int i=0; i<Displays.length; i++) {
		if (display == Displays [i]) Displays [i] = null;
	}
}

/**
 * Destroys the device in the operating system and releases
 * the device's handle.  If the device does not have a handle,
 * this method may do nothing depending on the device.
 * <p>
 * This method is called after <code>release</code>.
 * </p>
 * @see Device#dispose
 * @see #release
 */
@Override
protected void destroy () {
	if (this == Default) Default = null;
	deregister (this);
	destroyDisplay ();
}

void destroyDisplay () {
  if(UIThreadUtils.isRealDispatch()) {
    UIThreadUtils.popQueue();
//    /*
//    * When the session is ending, no SWT program can continue
//    * to run.  In order to avoid running code after the display
//    * has been disposed, exit from Java.
//    */
//    System.exit (0);
  }
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread just before the
 * receiver is disposed.  Specifying a <code>null</code> runnable
 * is ignored.
 *
 * @param runnable code to run at dispose time.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void disposeExec (Runnable runnable) {
	checkDevice ();
  if (disposeList == null) disposeList = new ArrayList();
  disposeList.add(runnable);
}

/**
 * Does whatever display specific cleanup is required, and then
 * uses the code in <code>SWTError.error</code> to handle the error.
 *
 * @param code the descriptive error code
 *
 * @see SWT#error(int)
 */
void error (int code) {
	SWT.error (code);
}

boolean filterEvent (Event event) {
	if (filterTable != null) filterTable.sendEvent (event);
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
}

HashMap componentToControlMap = new HashMap();

Control findControl (Component handle) {
  if (handle == null) return null;
  do {
    Control control = getControl (handle);
    if (control != null) return control;
  } while ((handle = handle.getParent()) != null);
  return null;
}

/**
 * Returns the display which the given thread is the
 * user-interface thread for, or null if the given thread
 * is not a user-interface thread for any display.  Specifying
 * <code>null</code> as the thread will return <code>null</code>
 * for the display.
 *
 * @param thread the user-interface thread
 * @return the display for the given thread
 */
public static synchronized Display findDisplay (Thread thread) {
  if(Displays.length == 0) return null;
  if(Thread.currentThread() == thread && SwingUtilities.isEventDispatchThread()) {
    return Displays[0];
  }
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == thread) {
			return display;
		}
	}
	return null;
}

/**
 * Returns the currently active <code>Shell</code>, or null
 * if no shell belonging to the currently running application
 * is active.
 *
 * @return the active shell or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell getActiveShell () {
	checkDevice ();
  Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
  if(window == null) return null;
  return (Shell)findControl(window);
}

/**
 * Returns a rectangle describing the receiver's size and location.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
@Override
public Rectangle getBounds () {
	checkDevice ();
	java.awt.Rectangle rectangle = new java.awt.Rectangle();
  GraphicsEnvironment ge = GraphicsEnvironment.
  getLocalGraphicsEnvironment();
  GraphicsDevice[] gs = ge.getScreenDevices();
  for(int j=0; j<gs.length; j++) {
    rectangle.add(gs[j].getDefaultConfiguration().getBounds());
  }
  return new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

/**
 * Returns the display which the currently running thread is
 * the user-interface thread for, or null if the currently
 * running thread is not a user-interface thread for any display.
 *
 * @return the current display
 */
public static synchronized Display getCurrent () {
	return findDisplay (Thread.currentThread ());
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 *
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 */
@Override
public Rectangle getClientArea () {
	checkDevice ();
  return getBounds();
}

Control getControl (Component handle) {
  return (Control)componentToControlMap.get(handle);
}

/**
 * Returns the control which the on-screen pointer is currently
 * over top of, or null if it is not currently over one of the
 * controls built by the currently running application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getCursorControl () {
  checkDevice ();
  if(!Compatibility.IS_JAVA_5_OR_GREATER) {
    return Utils.getTrakedMouseControl();
  }
  java.awt.Point point = MouseInfo.getPointerInfo().getLocation();
  // TODO: what about windows?
  Frame[] frames = Frame.getFrames();
  for(int i=0; i<frames.length; i++) {
    Frame frame = frames[i];
    Component component = frame.findComponentAt(point);
    if(component != null) {
      Control control = findControl(component);
      if(control != null) {
        return control;
      }
    }
  }
	return null;
}

/**
 * Returns the location of the on-screen pointer relative
 * to the top left corner of the screen.
 *
 * @return the cursor location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getCursorLocation () {
	checkDevice ();
  if(!Compatibility.IS_JAVA_5_OR_GREATER) {
    java.awt.Point point = Utils.getTrakedMouseLocation();
    return new Point(point.x, point.y);
  }
  java.awt.Point point = MouseInfo.getPointerInfo().getLocation();
  return new Point(point.x, point.y);
}

/**
 * Returns an array containing the recommended cursor sizes.
 *
 * @return the array of cursor sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public Point [] getCursorSizes () {
	checkDevice ();
  HashSet set = new HashSet();
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  for(int i=8; i<64; i++) {
    set.add(toolkit.getBestCursorSize(i, i));
  }
  set.remove(new java.awt.Dimension(0, 0));
  Point[] sizes = new Point[set.size()];
  int i=0;
  for(Iterator it=set.iterator(); it.hasNext(); ) {
    java.awt.Dimension d = ((java.awt.Dimension)it.next());
    sizes[i++] = new Point(d.width, d.height);
  }
  return sizes;
}

/**
 * Returns the default display. One is created (making the
 * thread that invokes this method its user-interface thread)
 * if it did not already exist.
 *
 * @return the default display
 */
public static synchronized Display getDefault () {
	if (Default == null) Default = new Display ();
	return Default;
}

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility to provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(String, Object)
 * @see #disposeExec(Runnable)
 */
public Object getData (String key) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
//  if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
//    return new Boolean (runMessagesInIdle);
//  }
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

/**
 * Returns the application defined, display specific data
 * associated with the receiver, or null if it has not been
 * set. The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display.
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility to provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @return the display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(Object)
 * @see #disposeExec(Runnable)
 */
public Object getData () {
	checkDevice ();
	return data;
}

/**
 * Returns the button dismissal alignment, one of <code>LEFT</code> or <code>RIGHT</code>.
 * The button dismissal alignment is the ordering that should be used when positioning the
 * default dismissal button for a dialog.  For example, in a dialog that contains an OK and
 * CANCEL button, on platforms where the button dismissal alignment is <code>LEFT</code>, the
 * button ordering should be OK/CANCEL.  When button dismissal alignment is <code>RIGHT</code>,
 * the button ordering should be CANCEL/OK.
 *
 * @return the button dismissal order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1
 */
public int getDismissalAlignment () {
	checkDevice ();
	return SWT.LEFT;
}


/**
 * Returns the longest duration, in milliseconds, between
 * two mouse button clicks that will be considered a
 * <em>double click</em> by the underlying operating system.
 *
 * @return the double click time
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getDoubleClickTime () {
	checkDevice ();
  // TODO: Method in Swing to do that?
	return 200;
}

/**
 * Returns the control which currently has keyboard focus,
 * or null if keyboard events are not currently going to
 * any of the controls built by the currently running
 * application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getFocusControl () {
	checkDevice ();
  Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
  if(component == null) return null;
  return findControl(component);
}

/**
 * Returns true when the high contrast mode is enabled.
 * Otherwise, false is returned.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the high contrast mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public boolean getHighContrast () {
	checkDevice ();
  // TODO: What about other platforms
  Boolean highContrast = (Boolean)Toolkit.getDefaultToolkit().getDesktopProperty("win.highContrast.on");
  return highContrast != null? highContrast.booleanValue(): false;
}

/**
 * Returns the maximum allowed depth of icons on this display, in bits per pixel.
 * On some platforms, this may be different than the actual depth of the display.
 *
 * @return the maximum icon depth
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Device#getDepth
 */
public int getIconDepth () {
	checkDevice ();
  // TODO: is this correct?
  return getDepth();
}

/**
 * Returns an array containing the recommended icon sizes.
 *
 * @return the array of icon sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Decorations#setImages(Image[])
 *
 * @since 3.0
 */
public Point [] getIconSizes () {
	checkDevice ();
  // TODO: method to do so in Swing?
	return new Point [] {
		new Point (32, 32),
		new Point (64, 64),
	};
}

MenuItem getMenuItem (JComponent component) {
  for(int i=0; i<menuItemsList.size(); i++) {
    MenuItem menuItem = (MenuItem)menuItemsList.get(i);
    if(menuItem.handle == component) {
      return menuItem;
    }
  }
  return null;
}


/**
 * Returns an array of monitors attached to the device.
 *
 * @return the array of monitors
 *
 * @since 3.0
 */
public Monitor [] getMonitors () {
	checkDevice ();
  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice[] gs = ge.getScreenDevices();
  ArrayList monitorsList = new ArrayList();
  Set boundsSet = new HashSet();
  for (int j = 0; j < gs.length; j++) {
    GraphicsDevice gd = gs[j];
    // Always true isnt'it?
//    if(gd.getType() == GraphicsDevice.TYPE_RASTER_SCREEN) {
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    Monitor monitor = new Monitor();
    monitor.handle = gc;
    java.awt.Rectangle bounds = gc.getBounds();
    if(boundsSet.add(bounds)) {
      monitor.x = bounds.x;
      monitor.y = bounds.y;
      monitor.width = bounds.width;
      monitor.height = bounds.height;
      java.awt.Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
      monitor.clientX = bounds.x + insets.left;
      monitor.clientY = bounds.y + insets.top;
      monitor.clientWidth = bounds.width - insets.left - insets.right;
      monitor.clientHeight = bounds.height - insets.top - insets.bottom;
      monitorsList.add(monitor);
    }
//    }
  }
  return (Monitor[])monitorsList.toArray(new Monitor[0]);
}

/**
 * Returns the primary monitor for that device.
 *
 * @return the primary monitor
 *
 * @since 3.0
 */
public Monitor getPrimaryMonitor () {
	checkDevice ();
  GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
  Monitor monitor = new Monitor();
  monitor.handle = gc;
  java.awt.Rectangle bounds = gc.getBounds();
  monitor.x = bounds.x;
  monitor.y = bounds.y;
  monitor.width = bounds.width;
  monitor.height = bounds.height;
  java.awt.Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
  monitor.clientX = bounds.x + insets.left;
  monitor.clientY = bounds.y + insets.top;
  monitor.clientWidth = bounds.width - insets.left - insets.right;
  monitor.clientHeight = bounds.height - insets.top - insets.bottom;
	return monitor;
}

/**
 * Returns a (possibly empty) array containing all shells which have
 * not been disposed and have the receiver as their display.
 *
 * @return the receiver's shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell [] getShells () {
	checkDevice ();
  ArrayList controlsList = new ArrayList();
  for(Iterator it = componentToControlMap.keySet().iterator(); it.hasNext(); ) {
    Component component = (Component)it.next();
    // TODO: what about file dialogs and such?
    if(component instanceof Window) {
      Control control = findControl(component);
      if(control instanceof Shell) {
        controlsList.add(control);
      }
    }
  }
  return (Shell[])controlsList.toArray(new Shell[0]);
}

/**
 * Returns the thread that has invoked <code>syncExec</code>
 * or null if no such runnable is currently being invoked by
 * the user-interface thread.
 * <p>
 * Note: If a runnable invoked by asyncExec is currently
 * running, this method will return null.
 * </p>
 *
 * @return the receiver's sync-interface thread
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getSyncThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return synchronizer.syncThread;
}

/**
 * Returns the matching standard color for the given
 * constant, which should be one of the color constants
 * specified in class <code>SWT</code>. Any value other
 * than one of the SWT color constants which is passed
 * in will result in the color black. This color should
 * not be free'd because it was allocated by the system,
 * not the application.
 *
 * @param id the color constant
 * @return the matching color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT
 */
@Override
public Color getSystemColor (int id) {
	checkDevice ();
	java.awt.Color swingColor = LookAndFeelUtils.getSystemColor(id);
	if(swingColor == null) {
	  return super.getSystemColor(id);
  }
  return Color.swing_new(this, swingColor);
}

/**
 * Returns the matching standard platform cursor for the given
 * constant, which should be one of the cursor constants
 * specified in class <code>SWT</code>. This cursor should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned if the supplied constant is not an SWT cursor
 * constant.
 *
 * @param id the SWT cursor constant
 * @return the corresponding cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
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
public Cursor getSystemCursor (int id) {
	checkDevice ();
	if (!(0 <= id && id < cursors.length)) return null;
	if (cursors [id] == null) {
		cursors [id] = new Cursor (this, id);
	}
	return cursors [id];
}

/**
 * Returns the matching standard platform image for the given
 * constant, which should be one of the icon constants
 * specified in class <code>SWT</code>. This image should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned either if the supplied constant is not an
 * SWT icon constant or if the platform does not define an
 * image that corresponds to the constant.
 *
 * @param id the SWT icon constant
 * @return the corresponding image or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_QUESTION
 * @see SWT#ICON_WARNING
 * @see SWT#ICON_WORKING
 *
 * @since 3.0
 */
public Image getSystemImage (int id) {
	checkDevice ();
	java.awt.Image hIcon = null;
	switch (id) {
		case SWT.ICON_ERROR:
			if (errorIcon == null) {
        errorIcon = getImage(LookAndFeelUtils.getSystemIcon(id));
			}
			hIcon = errorIcon;
			break;
		case SWT.ICON_INFORMATION:
			if (infoIcon == null) {
        infoIcon = getImage(LookAndFeelUtils.getSystemIcon(id));
			}
			hIcon = infoIcon;
			break;
		case SWT.ICON_WORKING:
			if (workingIcon == null) {
        workingIcon = getImage(LookAndFeelUtils.getSystemIcon(id));
			}
			hIcon = workingIcon;
			break;
		case SWT.ICON_QUESTION:
		  if (questionIcon == null) {
		    questionIcon = getImage(LookAndFeelUtils.getSystemIcon(id));
		  }
		  hIcon = questionIcon;
		  break;
		case SWT.ICON_WARNING:
			if (warningIcon == null) {
        warningIcon = getImage(LookAndFeelUtils.getSystemIcon(id));
			}
			hIcon = warningIcon;
			break;
	}
	if (hIcon == null) return null;
	return Image.swing_new (this, SWT.ICON, hIcon);
}

static java.awt.Image getImage(Icon icon) {
  BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
  Graphics g = image.getGraphics();
  icon.paintIcon(Utils.getDefaultComponent(), g, 0, 0);
  g.dispose();
  return image;
}

/**
 * Returns the single instance of the system tray or null
 * when there is no system tray available for the platform.
 *
 * @return the system tray or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public Tray getSystemTray () {
	checkDevice ();
	if (tray != null) return tray;
  if (!Compatibility.IS_JAVA_6_OR_GREATER || !SystemTray.isSupported()) {
    return null;
  }
	return tray = new Tray (this, SWT.NONE);
}

/**
 * Returns the user-interface thread for the receiver.
 *
 * @return the receiver's user-interface thread
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return thread;
}

/**
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data
 * @return the platform specific GC handle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
 * </ul>
 */
@Override
public CGC internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
  if (data != null) {
    data.device = this;
  }
//	int hDC = OS.GetDC (0);
//	if (hDC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
//	if (data != null) {
//		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//		if ((data.style & mask) != 0) {
//			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
//		} else {
//			data.style |= SWT.LEFT_TO_RIGHT;
//		}
//		data.device = this;
//		data.hFont = systemFont ();
//	}
//	return hDC;
  Frame[] frames = Frame.getFrames ();
  Graphics2D g = null;
  for (int i = 0; i < frames.length; i++) {
    if (frames[i].isActive ()) {
      g = (Graphics2D) frames[i].getGraphics ();
      break;
    }
  }
  if(g == null) {
    for (int i = 0; i < frames.length; i++) {
      if (frames[i].isShowing()) {
        g = (Graphics2D) frames[i].getGraphics ();
      }
    }
  }
  for (int i = 0; i < frames.length && g == null; i++) {
    g = (Graphics2D) frames[i].getGraphics ();
  }
  if(g == null) {
    g = new NullGraphics2D();
  }
  final Graphics2D g2D = g;
  return new CGC.CGCGraphics2D() {
    @Override
	public Graphics2D getGraphics() {
      return g2D;
    }
    @Override
	public Dimension getDeviceSize() {
      Rectangle bounds = getBounds();
      return new Dimension(bounds.width, bounds.height);
    }
  };
}

/**
 * Initializes any internal resources needed by the
 * device.
 * <p>
 * This method is called after <code>create</code>.
 * </p>
 *
 * @see #create
 */
@Override
protected void init () {
	super.init ();
}

/**
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data
 */
@Override
public void internal_dispose_GC (CGC handle, GCData data) {
	handle.dispose();
}

boolean isValidThread () {
	return thread == Thread.currentThread () || SwingUtilities.isEventDispatchThread();
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 *
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 *
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param point to be mapped
 * @return point with mapped coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1.2
 */
public Point map (Control from, Control to, Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return map (from, to, point.x, point.y);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 *
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 *
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @return point with mapped coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1.2
 */
public Point map (Control from, Control to, int x, int y) {
	checkDevice ();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
  Rectangle r = map(from, to, x, y, 0, 0);
  return new Point(r.x, r.y);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 *
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 *
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param rectangle to be mapped
 * @return rectangle with mapped coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, Rectangle rectangle) {
	checkDevice ();
	if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);
	return map (from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 *
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 *
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @param width coordinates to be mapped
 * @param height coordinates to be mapped
 * @return rectangle with mapped coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, int x, int y, int width, int height) {
	checkDevice ();
  java.awt.Point point = new java.awt.Point(x, y);
  if(from == null) {
    if(to == null) return new Rectangle(x, y, width, height);
    Point offset = to.getInternalOffset();
    point.x += offset.x;
    point.y += offset.y;
    SwingUtilities.convertPointFromScreen(point, ((CControl)to.handle).getClientArea());
  } else {
    Point offset = from.getInternalOffset();
    point.x -= offset.x;
    point.y -= offset.y;
    if(to == null) {
      SwingUtilities.convertPointToScreen(point, ((CControl)from.handle).getClientArea());
    } else {
      offset = to.getInternalOffset();
      point.x += offset.x;
      point.y += offset.y;
      point = SwingUtilities.convertPoint(((CControl)from.handle).getClientArea(), point, ((CControl)to.handle).getClientArea());
    }
  }
  // TODO: check what the "mirror" stuff mentioned in the comments is about.
  return new Rectangle(point.x, point.y, width, height);
}

static int getPreviousInputState() {
  return convertModifiersEx(Utils.previousModifiersEx);
}

static int getInputState() {
  return convertModifiersEx(Utils.modifiersEx);
}

static int convertModifiersEx(int modifiersEx) {
  int state = 0;
  if((modifiersEx & java.awt.event.KeyEvent.SHIFT_DOWN_MASK) != 0) {
    state |= SWT.SHIFT;
  }
  if((modifiersEx & java.awt.event.KeyEvent.ALT_DOWN_MASK) != 0) {
    state |= SWT.ALT;
  }
  if((modifiersEx & java.awt.event.KeyEvent.CTRL_DOWN_MASK) != 0) {
    state |= SWT.CTRL;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON1_DOWN_MASK) != 0) {
    state |= SWT.BUTTON1;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON2_DOWN_MASK) != 0) {
    state |= SWT.BUTTON2;
  }
  if((modifiersEx & java.awt.event.KeyEvent.BUTTON3_DOWN_MASK) != 0) {
    state |= SWT.BUTTON3;
  }
  return state;
}

/**
 * Generate a low level system event.
 *
 * <code>post</code> is used to generate low level keyboard
 * and mouse events. The intent is to enable automated UI
 * testing by simulating the input from the user.  Most
 * SWT applications should never need to call this method.
 * <p>
 * Note that this operation can fail when the operating system
 * fails to generate the event for any reason.  For example,
 * this can happen when there is no such key or mouse button
 * or when the system event queue is full.
 * </p>
 * <p>
 * <b>Event Types:</b>
 * <p>KeyDown, KeyUp
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type KeyDown or KeyUp</li>
 * <p> Either one of:
 * <li>(in) character a character that corresponds to a keyboard key</li>
 * <li>(in) keyCode the key code of the key that was typed,
 *          as defined by the key code constants in class <code>SWT</code></li>
 * </ul>
 * <p>MouseDown, MouseUp</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseDown or MouseUp
 * <li>(in) button the button that is pressed or released
 * </ul>
 * <p>MouseMove</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseMove
 * <li>(in) x the x coordinate to move the mouse pointer to in screen coordinates
 * <li>(in) y the y coordinate to move the mouse pointer to in screen coordinates
 * </ul>
 * </dl>
 *
 * @param event the event to be generated
 *
 * @return true if the event was generated or false otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 *
 */
public boolean post (Event event) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
  try {
  	int type = event.type;
  	switch (type) {
  		case SWT.KeyDown: {
        int keyCode = event.keyCode == 0? untranslateChar(event.character): untranslateKey(event.keyCode);
        if(keyCode == 0) {
          return false;
        }
        new Robot().keyPress(keyCode);
        return true;
      }
  		case SWT.KeyUp:
  		  int keyCode = event.keyCode == 0? untranslateChar(event.character): untranslateKey(event.keyCode);
  		  if(keyCode == 0) {
  		    return false;
  		  }
        new Robot().keyRelease(keyCode);
        return true;
      case SWT.MouseMove:
        new Robot().mouseMove(event.x, event.y);
        return true;
  		case SWT.MouseDown:
  		case SWT.MouseUp: {
        int buttons;
        switch (event.button) {
          case 1: buttons = java.awt.event.InputEvent.BUTTON1_MASK; break;
          case 2: buttons = java.awt.event.InputEvent.BUTTON2_MASK; break;
          case 3: buttons = java.awt.event.InputEvent.BUTTON3_MASK; break;
          default: return false;
        }
        if(type == SWT.MouseDown) {
          new Robot().mousePress(buttons);
        } else {
          new Robot().mouseRelease(buttons);
        }
        return true;
  		}
  	}
  } catch(Exception e) {
    return false;
  }
	return false;
}

void postEvent (final Event event) {
	/*
	* Place the event at the end of the event queue.
	* This code is always called in the Display's
	* thread so it must be re-enterant but does not
	* need to be synchronized.
	*/
	if (eventQueue == null) eventQueue = new Event [4];
	int index = 0;
	int length = eventQueue.length;
	while (index < length) {
		if (eventQueue [index] == null) break;
		index++;
	}
	if (index == length) {
		Event [] newQueue = new Event [length + 4];
		System.arraycopy (eventQueue, 0, newQueue, 0, length);
		eventQueue = newQueue;
	}
	eventQueue [index] = event;
}

AWTEvent event;

/**
 * Reads an event from the operating system's event queue,
 * dispatches it appropriately, and returns <code>true</code>
 * if there is potentially more work to do, or <code>false</code>
 * if the caller can sleep until another event is placed on
 * the event queue.
 * <p>
 * In addition to checking the system event queue, this method also
 * checks if any inter-thread messages (created by <code>syncExec()</code>
 * or <code>asyncExec()</code>) are waiting to be processed, and if
 * so handles them before returning.
 * </p>
 *
 * @return <code>false</code> if the caller can sleep upon return from this method
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 *
 * @see #sleep
 * @see #wake
 */
public boolean readAndDispatch () {
	checkDevice ();
  if(UIThreadUtils.isRealDispatch()) {
    boolean result = UIThreadUtils.swingEventQueue.dispatchEvent();
    UIThreadUtils.throwStoredException();
    runDeferredEvents ();
    return result || isDisposed();
  }
  if(event != null) {
    AWTEvent awtEvent = event;
    event = null;
    try {
      Object source = awtEvent.getSource();
      if (awtEvent instanceof ActiveEvent) {
        ((ActiveEvent)awtEvent).dispatch();
      } else if(source instanceof Component) {
        ((Component)source).dispatchEvent(awtEvent);
      } else if(source instanceof MenuComponent) {
        ((MenuComponent)source).dispatchEvent(awtEvent);
      }
    } catch(Throwable t) {
      UIThreadUtils.storeException(t);
    }
    UIThreadUtils.throwStoredException();
    return true;
  }
  if(SwingUtilities.isEventDispatchThread()) {
    return isDisposed();
  }
  synchronized(UIThreadUtils.UI_LOCK) {
    if(UIThreadUtils.exclusiveSectionCount == 0) {
      return isDisposed();
    }
    try {
      UIThreadUtils.UI_LOCK.notify();
      UIThreadUtils.UI_LOCK.wait();
    } catch(Exception e) {
    }
    UIThreadUtils.throwStoredException();
  }
  runDeferredEvents ();
//		return true;
//	}
//	/*boolean result =*/ runAsyncMessages (false);
//  System.err.println(synchronizer.getMessageCount());
  return true;
}

static synchronized void register (Display display) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] == null) {
			Displays [i] = display;
			return;
		}
	}
	Display [] newDisplays = new Display [Displays.length + 4];
	System.arraycopy (Displays, 0, newDisplays, 0, Displays.length);
	newDisplays [Displays.length] = display;
	Displays = newDisplays;
}

/**
 * Releases any internal resources back to the operating
 * system and clears all fields except the device handle.
 * <p>
 * Disposes all shells which are currently open on the display.
 * After this method has been invoked, all related related shells
 * will answer <code>true</code> when sent the message
 * <code>isDisposed()</code>.
 * </p><p>
 * When a device is destroyed, resources that were acquired
 * on behalf of the programmer need to be returned to the
 * operating system.  For example, if the device allocated a
 * font to be used as the system font, this font would be
 * freed in <code>release</code>.  Also,to assist the garbage
 * collector and minimize the amount of memory that is not
 * reclaimed when the programmer keeps a reference to a
 * disposed device, all fields except the handle are zero'd.
 * The handle is needed by <code>destroy</code>.
 * </p>
 * This method is called before <code>destroy</code>.
 *
 * @see Device#dispose
 * @see #destroy
 */
@Override
protected void release () {
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.dispose ();
	}
	if (tray != null) tray.dispose ();
	tray = null;
	while (readAndDispatch ()) {}
	if (disposeList != null) {
    for(Iterator it = disposeList.iterator(); it.hasNext(); ) {
      ((Runnable)it.next()).run();
    }
	}
	disposeList = null;
	synchronizer.releaseSynchronizer ();
	synchronizer = null;
	releaseDisplay ();
	super.release ();
}

void releaseDisplay () {
	errorIcon = warningIcon = infoIcon = questionIcon = warningIcon = null;
	bars = popups = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere in
 * a widget. The event type is one of the event constants defined
 * in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #addFilter
 * @see #addListener
 *
 * @since 3.0
 */
public void removeFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) return;
	filterTable.unhook (eventType, listener);
	if (filterTable.size () == 0) filterTable = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs. The event type
 * is one of the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #addListener
 *
 * @since 2.0
 */
public void removeListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

void removeBar (Menu menu) {
	if (bars == null) return;
	for (int i=0; i<bars.length; i++) {
		if (bars [i] == menu) {
			bars [i] = null;
			return;
		}
	}
}

Control removeControl (Component handle) {
	if (handle == null) return null;
  return (Control)componentToControlMap.remove(handle);
}

void removeMenuItem (MenuItem item) {
  menuItemsList.remove(item);
}

void removePopup (Menu menu) {
	if (popups == null) return;
	for (int i=0; i<popups.length; i++) {
		if (popups [i] == menu) {
			popups [i] = null;
			return;
		}
	}
}

boolean runAsyncMessages (boolean all) {
  if(synchronizer == null) {
    return false;
  }
	return synchronizer.runAsyncMessages (all);
}

boolean runDeferredEvents () {
	/*
	* Run deferred events.  This code is always
	* called in the Display's thread so it must
	* be re-enterant but need not be synchronized.
	*/
	while (eventQueue != null) {

		/* Take an event off the queue */
		Event event = eventQueue [0];
		if (event == null) break;
		int length = eventQueue.length;
		System.arraycopy (eventQueue, 1, eventQueue, 0, --length);
		eventQueue [length] = null;

		/* Run the event */
		Widget widget = event.widget;
		if (widget != null && !widget.isDisposed ()) {
			Widget item = event.item;
			if (item == null || !item.isDisposed ()) {
				widget.sendEvent (event);
			}
		}

		/*
		* At this point, the event queue could
		* be null due to a recursive invokation
		* when running the event.
		*/
	}

	/* Clear the queue */
	eventQueue = null;
	return true;
}

void sendEvent (int eventType, Event event) {
	if (eventTable == null && filterTable == null) {
		return;
	}
	if (event == null) event = new Event ();
	event.display = this;
	event.type = eventType;
	if (event.time == 0) event.time = Utils.getCurrentTime ();
	if (!filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param x the new x coordinate for the cursor
 * @param y the new y coordinate for the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.1
 */
public void setCursorLocation (int x, int y) {
	checkDevice ();
  try {
    new Robot().mouseMove(x, y);
  } catch(Exception e) {}
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param point new position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 2.0
 */
public void setCursorLocation (Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	setCursorLocation (point.x, point.y);
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given argument.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData(String)
 * @see #disposeExec(Runnable)
 */
public void setData (String key, Object value) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
//  if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
//    Boolean data = (Boolean) value;
//    runMessagesInIdle = data != null && data.booleanValue ();
//    return;
//  }

	/* Remove the key/value pair */
	if (value == null) {
		if (keys == null) return;
		int index = 0;
		while (index < keys.length && !keys [index].equals (key)) index++;
		if (index == keys.length) return;
		if (keys.length == 1) {
			keys = null;
			values = null;
		} else {
			String [] newKeys = new String [keys.length - 1];
			Object [] newValues = new Object [values.length - 1];
			System.arraycopy (keys, 0, newKeys, 0, index);
			System.arraycopy (keys, index + 1, newKeys, index, newKeys.length - index);
			System.arraycopy (values, 0, newValues, 0, index);
			System.arraycopy (values, index + 1, newValues, index, newValues.length - index);
			keys = newKeys;
			values = newValues;
		}
		return;
	}

	/* Add the key/value pair */
	if (keys == null) {
		keys = new String [] {key};
		values = new Object [] {value};
		return;
	}
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) {
			values [i] = value;
			return;
		}
	}
	String [] newKeys = new String [keys.length + 1];
	Object [] newValues = new Object [values.length + 1];
	System.arraycopy (keys, 0, newKeys, 0, keys.length);
	System.arraycopy (values, 0, newValues, 0, values.length);
	newKeys [keys.length] = key;
	newValues [values.length] = value;
	keys = newKeys;
	values = newValues;
}

/**
 * Sets the application defined, display specific data
 * associated with the receiver, to the argument.
 * The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display.
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param data the new display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData()
 * @see #disposeExec(Runnable)
 */
public void setData (Object data) {
	checkDevice ();
	this.data = data;
}

/**
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.  Specifying
 * <code>null</code> for the name clears it.
 *
 * @param name the new app name or <code>null</code>
 */
public static void setAppName (String name) {
	/* Do nothing */
}

/**
 * Sets the synchronizer used by the display to be
 * the argument, which can not be null.
 *
 * @param synchronizer the new synchronizer for the display (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the synchronizer is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 */
public void setSynchronizer (Synchronizer synchronizer) {
	checkDevice ();
	if (synchronizer == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (this.synchronizer != null) {
		this.synchronizer.runAsyncMessages(true);
	}
	this.synchronizer = synchronizer;
}

/**
 * Causes the user-interface thread to <em>sleep</em> (that is,
 * to be put in a state where it does not consume CPU cycles)
 * until an event is received or it is otherwise awakened.
 *
 * @return <code>true</code> if an event requiring dispatching was placed on the queue.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
	if(UIThreadUtils.isRealDispatch()) {
    return UIThreadUtils.swingEventQueue.sleep();
  }
  if(SwingUtilities.isEventDispatchThread()) {
    boolean result = true;
    UIThreadUtils.fakeDispatchingEDT = Thread.currentThread();
    try {
      event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
    } catch(InterruptedException e) {
      result = false;
    }
    UIThreadUtils.fakeDispatchingEDT = null;
    return result;
  }
  synchronized(UIThreadUtils.UI_LOCK) {
    if(UIThreadUtils.exclusiveSectionCount == 0) {
      try {
        UIThreadUtils.UI_LOCK.wait();
      } catch(Exception e) {
      }
    }
    return UIThreadUtils.exclusiveSectionCount > 0;
  }
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.  Specifying <code>null</code>
 * as the runnable simply wakes the user-interface thread.
 * <p>
 * Note that at the time the runnable is invoked, widgets
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 *
 * @param runnable code to run on the user-interface thread or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_FAILED_EXEC - if an exception occured when executing the runnable</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void syncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
  synchronizer.syncExec (runnable);
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread after the specified
 * number of milliseconds have elapsed. If milliseconds is less
 * than zero, the runnable is not executed.
 * <p>
 * Note that at the time the runnable is invoked, widgets
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 *
 * @param milliseconds the delay before running the runnable
 * @param runnable code to run on the user-interface thread
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void timerExec (final int milliseconds, final Runnable runnable) {
	checkDevice ();
	if (runnable == null) error (SWT.ERROR_NULL_ARGUMENT);
  if (milliseconds < 0) {
    timerList.remove(runnable);
    return;
  }
  timerList.add(runnable);
  new Thread("Display.timerExecThread") {
    @Override
	public void run() {
      try {
        sleep(milliseconds);
        boolean isRemoved = timerList.remove(runnable);
        if(isRemoved) {
          SwingUtilities.invokeLater(runnable);
        }
      } catch(Exception e) {}
    }
  }.start();
}

static int translateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [0] == key) return KeyTable [i] [1];
	}
  // TODO: return translateChar or something
	return 0;
}

static int untranslateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [1] == key) return KeyTable [i] [0];
	}
	return untranslateChar((char)key);
}

static int untranslateChar(char c) {
  if(c >= '0' && c <='9') {
    return c;
  }
  char uc = Character.toUpperCase(c);
  if(uc >= 'A' && uc <= 'Z') {
    return uc;
  }
  switch(c) {
  case '\n': return java.awt.event.KeyEvent.VK_ENTER;
  case '\b': return java.awt.event.KeyEvent.VK_BACK_SPACE;
  case '\t': return java.awt.event.KeyEvent.VK_TAB;
  case ' ': return java.awt.event.KeyEvent.VK_SPACE;
  case ',': return java.awt.event.KeyEvent.VK_COMMA;
  case '-': return java.awt.event.KeyEvent.VK_MINUS;
  case '.': return java.awt.event.KeyEvent.VK_PERIOD;
  case ';': return java.awt.event.KeyEvent.VK_SEMICOLON;
  case '=': return java.awt.event.KeyEvent.VK_EQUALS;
  case '[': return java.awt.event.KeyEvent.VK_OPEN_BRACKET;
  case '\\': return java.awt.event.KeyEvent.VK_BACK_SLASH;
  case ']': return java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
  case '`': return java.awt.event.KeyEvent.VK_BACK_QUOTE;
  case '\u00B4': return java.awt.event.KeyEvent.VK_QUOTE;
  case '&': return java.awt.event.KeyEvent.VK_AMPERSAND;
  case '*': return java.awt.event.KeyEvent.VK_ASTERISK;
  case '"': return java.awt.event.KeyEvent.VK_QUOTEDBL;
  case '<': return java.awt.event.KeyEvent.VK_LESS;
  case '>': return java.awt.event.KeyEvent.VK_GREATER;
  case '{': return java.awt.event.KeyEvent.VK_BRACELEFT;
  case '}': return java.awt.event.KeyEvent.VK_BRACERIGHT;
  case '@': return java.awt.event.KeyEvent.VK_AT;
  case ':': return java.awt.event.KeyEvent.VK_COLON;
  case '^': return java.awt.event.KeyEvent.VK_CIRCUMFLEX;
  case '$': return java.awt.event.KeyEvent.VK_DOLLAR;
  case '\u20AC': return java.awt.event.KeyEvent.VK_EURO_SIGN;
  case '!': return java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;
  case '\u00A1': return java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK;
  case '(': return java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;
  case '#': return java.awt.event.KeyEvent.VK_NUMBER_SIGN;
  case '+': return java.awt.event.KeyEvent.VK_PLUS;
  case ')': return java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;
  case '_': return java.awt.event.KeyEvent.VK_UNDERSCORE;
  }
  return 0;
}

/**
 * Forces all outstanding paint requests for the display
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Control#update()
 */
public void update() {
	checkDevice ();
	Shell[] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
//    if (!shell.isDisposed ()) shell.update (true);
		if (!shell.isDisposed ()) shell.update ();
	}
}

/**
 * If the receiver's user-interface thread was <code>sleep</code>ing,
 * causes it to be awakened and start running again. Note that this
 * method may be called from any thread.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #sleep
 */
public void wake () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (thread == Thread.currentThread ()) return;
	UIThreadUtils.wakeUIThread();
}

void wakeThread () {
  SwingUtilities.invokeLater(() -> {
  UIThreadUtils.startExclusiveSection(Display.this);
  try {
    runAsyncMessages (true);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  }
  UIThreadUtils.stopExclusiveSection();
  UIThreadUtils.throwStoredException();
});
}

/**
 * Sets a callback that will be invoked whenever an exception is thrown by a listener or external
 * callback function. The application may use this to set a global exception handling policy:
 * the most common policies are either to log and discard the exception or to re-throw the
 * exception.
 * <p>
 * The default SWT error handling policy is to rethrow exceptions.
 *
 * @param runtimeExceptionHandler new exception handler to be registered.
 * @since 3.106
 */
public final void setRuntimeExceptionHandler (Consumer<RuntimeException> runtimeExceptionHandler) {
	checkDevice();
	this.runtimeExceptionHandler = Objects.requireNonNull (runtimeExceptionHandler);
}

/**
 * Returns the current exception handler. It will receive all exceptions thrown by listeners
 * and external callbacks in this display. If code wishes to temporarily replace the exception
 * handler (for example, during a unit test), it is common practice to invoke this method prior
 * to replacing the exception handler so that the old handler may be restored afterward.
 *
 * @return the current exception handler. Never <code>null</code>.
 * @since 3.106
 */
public final Consumer<RuntimeException> getRuntimeExceptionHandler () {
	return runtimeExceptionHandler;
}

/**
 * Sets a callback that will be invoked whenever an error is thrown by a listener or external
 * callback function. The application may use this to set a global exception handling policy:
 * the most common policies are either to log and discard the exception or to re-throw the
 * exception.
 * <p>
 * The default SWT error handling policy is to rethrow exceptions.
 *
 * @param errorHandler new error handler to be registered.
 * @since 3.106
 */
public final void setErrorHandler (Consumer<Error> errorHandler) {
	checkDevice();
	this.errorHandler = Objects.requireNonNull (errorHandler);
}

/**
 * Returns the current exception handler. It will receive all errors thrown by listeners
 * and external callbacks in this display. If code wishes to temporarily replace the error
 * handler (for example, during a unit test), it is common practice to invoke this method prior
 * to replacing the error handler so that the old handler may be restored afterward.
 *
 * @return the current error handler. Never <code>null</code>.
 * @since 3.106
 */
public final Consumer<Error> getErrorHandler () {
	return errorHandler;
}

}
