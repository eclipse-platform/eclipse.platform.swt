/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Display for SWT.
 * This implementation provides no-op or default implementations
 * for running SWT code in a headless environment.
 */
public class Display extends Device implements Executor {
	
	static Display Default;
	static String APP_NAME = "SWT";
	static String APP_VERSION = "";
	
	/* Widget Table */
	Widget[] widgetTable;
	int freeSlot;
	static final int GROW_SIZE = 1024;
	
	/* Synchronization */
	Synchronizer synchronizer = new Synchronizer(this);
	Consumer<RuntimeException> runtimeExceptionHandler = DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER;
	Consumer<Error> errorHandler = DefaultExceptionHandler.RUNTIME_ERROR_HANDLER;
	Thread thread;
	
	/* Shells */
	Shell[] shells = new Shell[0];
	Shell activeShell;
	
	/* Modality */
	Shell[] modalShells;
	
	/* Disposal */
	Runnable[] disposeList;
	
	/* Event Handling */
	EventTable eventTable, filterTable;
	Event[] eventQueue;
	
	/* Timers */
	int[] timerIds;
	Runnable[] timerList;
	int nextTimerId = 1;
	
	/* Deferred Layout */
	Composite[] layoutDeferred;
	int layoutDeferredCount;
	
	/* System Resources */
	Font systemFont;
	
	/* Focus */
	Control focusControl;
	
	/* Disposed flag */
	boolean disposed;
	
	/* Package prefix */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";

public Display() {
	this(null);
}

public Display(DeviceData data) {
	super(data);
	thread = Thread.currentThread();
	synchronized (Device.class) {
		if (Default == null) Default = this;
	}
	widgetTable = new Widget[GROW_SIZE];
}

@Override
protected void create(DeviceData data) {
	checkSubclass();
	synchronizer = new Synchronizer(this);
	systemFont = new Font(this, new FontData("Sans", 10, SWT.NORMAL));
}

@Override
protected void init() {
	super.init();
}

public void addFilter(int eventType, Listener listener) {
	checkDevice();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable();
	filterTable.hook(eventType, listener);
}

public void addListener(int eventType, Listener listener) {
	checkDevice();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable();
	eventTable.hook(eventType, listener);
}

public void asyncExec(Runnable runnable) {
	synchronized (Device.class) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
		synchronizer.asyncExec(runnable);
	}
}

@Override
public void execute(Runnable runnable) {
	Objects.requireNonNull(runnable);
	if (thread == Thread.currentThread()) {
		runnable.run();
	} else {
		syncExec(runnable);
	}
}

public void beep() {
	checkDevice();
	// No-op in headless mode
}

@Override
public void close() {
	checkDevice();
	Event event = new Event();
	sendEvent(SWT.Close, event);
	if (event.doit) dispose();
}

@Override
protected void destroy() {
	if (disposed) return;
	disposed = true;
	Shell[] shells = getShells();
	for (Shell shell : shells) {
		if (!shell.isDisposed()) shell.dispose();
	}
}

public void disposeExec(Runnable runnable) {
	checkDevice();
	if (disposeList == null) disposeList = new Runnable[4];
	for (int i = 0; i < disposeList.length; i++) {
		if (disposeList[i] == null) {
			disposeList[i] = runnable;
			return;
		}
	}
	Runnable[] newList = new Runnable[disposeList.length + 4];
	System.arraycopy(disposeList, 0, newList, 0, disposeList.length);
	newList[disposeList.length] = runnable;
	disposeList = newList;
}

public static Display findDisplay(Thread thread) {
	synchronized (Device.class) {
		if (Default != null && Default.thread == thread) {
			return Default;
		}
		return null;
	}
}

public Widget findWidget(long handle) {
	checkDevice();
	return null;
}

public Widget findWidget(long handle, long id) {
	checkDevice();
	return null;
}

public Widget findWidget(Widget widget, long id) {
	checkDevice();
	return null;
}

public Shell getActiveShell() {
	checkDevice();
	return activeShell;
}

public static String getAppName() {
	return APP_NAME;
}

public static String getAppVersion() {
	return APP_VERSION;
}

public Rectangle getBounds() {
	checkDevice();
	return new Rectangle(0, 0, 1024, 768);
}

public Rectangle getClientArea() {
	checkDevice();
	return new Rectangle(0, 0, 1024, 768);
}

public static Display getCurrent() {
	return findDisplay(Thread.currentThread());
}

public Control getCursorControl() {
	checkDevice();
	return null;
}

public Point getCursorLocation() {
	checkDevice();
	return new Point(0, 0);
}

public Point[] getCursorSizes() {
	checkDevice();
	return new Point[] { new Point(16, 16), new Point(32, 32) };
}

public Object getData(String key) {
	checkDevice();
	if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
	return super.getData(key);
}

public Object getData() {
	checkDevice();
	return super.getData();
}

public static Display getDefault() {
	synchronized (Device.class) {
		if (Default == null) Default = new Display();
		return Default;
	}
}

public int getDismissalAlignment() {
	checkDevice();
	return SWT.LEFT;
}

public int getDoubleClickTime() {
	checkDevice();
	return 500;
}

public Control getFocusControl() {
	checkDevice();
	return focusControl;
}

public boolean getHighContrast() {
	checkDevice();
	return false;
}

public int getDepth() {
	return 24;
}

public int getIconDepth() {
	return 24;
}

public Point[] getIconSizes() {
	checkDevice();
	return new Point[] { new Point(16, 16), new Point(32, 32) };
}

public Menu getMenuBar() {
	checkDevice();
	return null;
}

public Monitor[] getMonitors() {
	checkDevice();
	Monitor monitor = new Monitor();
	Rectangle bounds = new Rectangle(0, 0, 1024, 768);
	monitor.handle = 0;
	monitor.x = bounds.x;
	monitor.y = bounds.y;
	monitor.width = bounds.width;
	monitor.height = bounds.height;
	monitor.clientX = bounds.x;
	monitor.clientY = bounds.y;
	monitor.clientWidth = bounds.width;
	monitor.clientHeight = bounds.height;
	monitor.zoom = 100;
	return new Monitor[] { monitor };
}

public Monitor getPrimaryMonitor() {
	checkDevice();
	return getMonitors()[0];
}

public Shell[] getShells() {
	checkDevice();
	return shells;
}

public Thread getSyncThread() {
	synchronized (Device.class) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
		return synchronizer.syncThread;
	}
}

@Override
public Font getSystemFont() {
	checkDevice();
	return systemFont;
}

public Thread getThread() {
	synchronized (Device.class) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
		return thread;
	}
}

public static boolean isSystemDarkTheme() {
	return false;
}

public static boolean isHeadless() {
	return true;
}

public boolean readAndDispatch() {
	checkDevice();
	return runAsyncMessages(false);
}

@Override
protected void release() {
	sendEvent(SWT.Dispose, new Event());
	Shell[] shells = getShells();
	for (Shell shell : shells) {
		if (!shell.isDisposed()) shell.dispose();
	}
	if (eventTable != null) eventTable.unhook(SWT.Dispose, null);
	super.release();
	releaseDisplay();
}

void releaseDisplay() {
	if (disposeList != null) {
		for (Runnable runnable : disposeList) {
			if (runnable != null) {
				try {
					runnable.run();
				} catch (RuntimeException exception) {
					runtimeExceptionHandler.accept(exception);
				} catch (Error error) {
					errorHandler.accept(error);
				}
			}
		}
	}
	disposeList = null;
	synchronizer.releaseSynchronizer();
	synchronizer = null;
	thread = null;
	widgetTable = null;
}

void removeWidget(long handle) {
	// No-op in headless mode
}

public void removeFilter(int eventType, Listener listener) {
	checkDevice();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) return;
	filterTable.unhook(eventType, listener);
	if (filterTable.size() == 0) filterTable = null;
}

public void removeListener(int eventType, Listener listener) {
	checkDevice();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(eventType, listener);
}

boolean runAsyncMessages(boolean all) {
	return synchronizer.runAsyncMessages(all);
}

boolean runDeferredLayouts() {
	if (layoutDeferredCount != 0) {
		Composite[] temp = layoutDeferred;
		int count = layoutDeferredCount;
		layoutDeferred = null;
		layoutDeferredCount = 0;
		for (int i = 0; i < count; i++) {
			Composite comp = temp[i];
			if (!comp.isDisposed()) comp.setLayoutDeferred(false);
		}
		return true;
	}
	return false;
}

boolean runDeferredEvents() {
	boolean run = false;
	if (runDeferredLayouts()) run = true;
	return run;
}

void sendEvent(int eventType, Event event) {
	if (eventTable == null && filterTable == null) return;
	if (event == null) event = new Event();
	event.display = this;
	event.type = eventType;
	if (event.time == 0) event.time = (int) System.currentTimeMillis();
	if (filterTable != null) filterTable.sendEvent(event);
	if (eventTable != null) eventTable.sendEvent(event);
}

public static void setAppName(String name) {
	APP_NAME = name;
}

public static void setAppVersion(String version) {
	APP_VERSION = version;
}

public void setData(String key, Object value) {
	checkDevice();
	if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
	super.setData(key, value);
}

public void setData(Object data) {
	checkDevice();
	super.setData(data);
}

public void setRuntimeExceptionHandler(Consumer<RuntimeException> handler) {
	checkDevice();
	if (handler == null) error(SWT.ERROR_NULL_ARGUMENT);
	runtimeExceptionHandler = handler;
}

public void setErrorHandler(Consumer<Error> handler) {
	checkDevice();
	if (handler == null) error(SWT.ERROR_NULL_ARGUMENT);
	errorHandler = handler;
}

public boolean sleep() {
	checkDevice();
	return runAsyncMessages(false) || runDeferredEvents();
}

public void syncExec(Runnable runnable) {
	synchronized (Device.class) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
		synchronizer.syncExec(runnable);
	}
}

public int timerExec(int milliseconds, Runnable runnable) {
	checkDevice();
	if (runnable == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (timerList == null) {
		timerList = new Runnable[4];
		timerIds = new int[4];
	}
	int timerId = nextTimerId++;
	int index = 0;
	while (index < timerList.length) {
		if (timerList[index] == null) break;
		index++;
	}
	if (index == timerList.length) {
		Runnable[] newTimerList = new Runnable[timerList.length + 4];
		System.arraycopy(timerList, 0, newTimerList, 0, timerList.length);
		timerList = newTimerList;
		int[] newTimerIds = new int[timerIds.length + 4];
		System.arraycopy(timerIds, 0, newTimerIds, 0, timerIds.length);
		timerIds = newTimerIds;
	}
	timerList[index] = runnable;
	timerIds[index] = timerId;
	
	// Schedule the timer to run
	new Timer().schedule(new TimerTask() {
		@Override
		public void run() {
			asyncExec(runnable);
		}
	}, milliseconds);
	
	return timerId;
}

public void wake() {
	synchronized (Device.class) {
		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
		// No-op in headless mode
	}
}

void addShell(Shell shell) {
	int length = shells.length;
	Shell[] newShells = new Shell[length + 1];
	System.arraycopy(shells, 0, newShells, 0, length);
	newShells[length] = shell;
	shells = newShells;
}

void removeShell(Shell shell) {
	int length = shells.length;
	for (int i = 0; i < length; i++) {
		if (shells[i] == shell) {
			Shell[] newShells = new Shell[length - 1];
			System.arraycopy(shells, 0, newShells, 0, i);
			System.arraycopy(shells, i + 1, newShells, i, length - i - 1);
			shells = newShells;
			break;
		}
	}
}

void addLayoutDeferred(Composite comp) {
	if (layoutDeferred == null) layoutDeferred = new Composite[64];
	if (layoutDeferredCount == layoutDeferred.length) {
		Composite[] temp = new Composite[layoutDeferred.length + 64];
		System.arraycopy(layoutDeferred, 0, temp, 0, layoutDeferred.length);
		layoutDeferred = temp;
	}
	layoutDeferred[layoutDeferredCount++] = comp;
}

void removeLayoutDeferred(Composite comp) {
	if (layoutDeferred == null) return;
	for (int i = 0; i < layoutDeferredCount; i++) {
		if (layoutDeferred[i] == comp) {
			layoutDeferredCount--;
			System.arraycopy(layoutDeferred, i + 1, layoutDeferred, i, layoutDeferredCount - i);
			layoutDeferred[layoutDeferredCount] = null;
			break;
		}
	}
}

boolean filterEvent(Event event) {
	if (filterTable != null) {
		filterTable.sendEvent(event);
	}
	return false;
}

boolean filters(int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks(eventType);
}

void postEvent(Event event) {
	if (eventQueue == null) eventQueue = new Event[4];
	int index = 0;
	int length = eventQueue.length;
	while (index < length) {
		if (eventQueue[index] == null) break;
		index++;
	}
	if (index == length) {
		Event[] newQueue = new Event[length + 4];
		System.arraycopy(eventQueue, 0, newQueue, 0, length);
		eventQueue = newQueue;
	}
	eventQueue[index] = event;
}

static boolean isValidClass(Class<?> clazz) {
	String name = clazz.getName();
	int index = name.lastIndexOf('.');
	return name.substring(0, index + 1).equals(PACKAGE_PREFIX);
}

}
