/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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

import org.eclipse.swt.*;

/**
 * Instances of this class provide synchronization support
 * for displays. A default instance is created automatically
 * for each display, and this instance is sufficient for almost
 * all applications.
 * <p>
 * <b>IMPORTANT:</b> Typical application code <em>never</em>
 * needs to deal with this class. It is provided only to
 * allow applications which require non-standard
 * synchronization behavior to plug in the support they
 * require. <em>Subclasses which override the methods in
 * this class must ensure that the superclass methods are
 * invoked in their implementations</em>
 * </p>
 *
 * @see Display#setSynchronizer
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Synchronizer {
	Display display;
	final ConcurrentLinkedQueue<RunnableLock>  messages= new ConcurrentLinkedQueue<>();
	Thread syncThread;
	static final int GROW_SIZE = 4;
	static final int MESSAGE_LIMIT = 64;

	//TEMPORARY CODE
	static final boolean IS_COCOA = "cocoa".equals (SWT.getPlatform ());
	static final boolean IS_GTK = "gtk".equals (SWT.getPlatform ());

/**
 * Constructs a new instance of this class.
 *
 * @param display the display to create the synchronizer on
 */
public Synchronizer (Display display) {
	this.display = display;
}

/**
 * Removes all pending events from the receiver and inserts them into the beginning of the given
 * synchronizer's queue
 *
 * @param toReceiveTheEvents the synchronizer that will receive the events
 */
void moveAllEventsTo (Synchronizer toReceiveTheEvents) {
	// Drain target queue and add it later again to insert at the beginning of the
	// queue for backward compatibility:
	java.util.List<RunnableLock> tail = new ArrayList<>();
	toReceiveTheEvents.messages.removeIf(tail::add);
	messages.removeIf(toReceiveTheEvents.messages::add);
	toReceiveTheEvents.messages.addAll(tail);
}


void addLast (RunnableLock lock) {
	boolean wake = messages.isEmpty();
	messages.add(lock);
	if (wake) display.wakeThread ();
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next
 * reasonable opportunity. The caller of this method continues
 * to run in parallel, and is not notified when the
 * runnable has completed.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @see #syncExec
 */
protected void asyncExec (Runnable runnable) {
	if (runnable == null) {
		//TEMPORARY CODE
		if (!(IS_GTK || IS_COCOA)) {
			display.wake ();
			return;
		}
	}
	addLast (new RunnableLock (runnable));
}

boolean isMessagesEmpty() {
	return messages.isEmpty();
}

void releaseSynchronizer () {
	display = null;
	messages.clear();
	syncThread = null;
}

RunnableLock removeFirst () {
	return messages.poll();
}

boolean runAsyncMessages () {
	return runAsyncMessages (false);
}

boolean runAsyncMessages (boolean all) {
	throw new UnsupportedOperationException("Not implemented yet");
//	boolean run = false;
//	do {
//		RunnableLock lock = removeFirst ();
//		if (lock == null) return run;
//		run = true;
//		synchronized (lock) {
//			syncThread = lock.thread;
//			display.sendPreEvent(SWT.None);
//			try {
//				lock.run (display);
//			} catch (Throwable t) {
//				lock.throwable = t;
//				SWT.error (SWT.ERROR_FAILED_EXEC, t);
//			} finally {
//				if (display != null && !display.isDisposed()) {
//					display.sendPostEvent(SWT.None);
//				}
//				syncThread = null;
//				lock.notifyAll ();
//			}
//		}
//	} while (all);
//	return run;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred when executing the runnable</li>
 * </ul>
 *
 * @see #asyncExec
 */
protected void syncExec (Runnable runnable) {
	throw new UnsupportedOperationException("Not implemented yet");
//	RunnableLock lock = null;
//	synchronized (Device.class) {
//		if (display == null || display.isDisposed ()) SWT.error (SWT.ERROR_DEVICE_DISPOSED);
//		if (!display.isValidThread ()) {
//			if (runnable == null) {
//				display.wake ();
//				return;
//			}
//			lock = new RunnableLock (runnable);
//			/*
//			 * Only remember the syncThread for syncExec.
//			 */
//			lock.thread = Thread.currentThread();
//			addLast (lock);
//		}
//	}
//	if (lock == null) {
//		if (runnable != null) {
//			display.sendPreEvent(SWT.None);
//			try {
//				runnable.run();
//			} catch (RuntimeException exception) {
//				display.getRuntimeExceptionHandler ().accept (exception);
//			} catch (Error error) {
//				display.getErrorHandler ().accept (error);
//			} finally {
//				if (display != null && !display.isDisposed()) {
//					display.sendPostEvent(SWT.None);
//				}
//			}
//		}
//		return;
//	}
//	synchronized (lock) {
//		boolean interrupted = false;
//		while (!lock.done ()) {
//			try {
//				lock.wait ();
//			} catch (InterruptedException e) {
//				interrupted = true;
//			}
//		}
//		if (interrupted) {
//			Thread.currentThread().interrupt();
//		}
//		if (lock.throwable != null) {
//			SWT.error (SWT.ERROR_FAILED_EXEC, lock.throwable);
//		}
//	}
}

}
