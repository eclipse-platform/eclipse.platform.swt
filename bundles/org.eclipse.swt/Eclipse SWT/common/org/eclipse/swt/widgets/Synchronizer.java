package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
 */
public class Synchronizer {
	Display display;
	int messagesSize;
	RunnableLock [] messages;
	Object messageLock = new Object ();
	Thread syncThread;

public Synchronizer (Display display) {
	this.display = display;
}
	
void addLast (RunnableLock entry) {
	synchronized (messageLock) {
		if (messages == null) messages = new RunnableLock [4];
		if (messagesSize == messages.length) {
			RunnableLock[] newMessages = new RunnableLock [messagesSize + 4];
			System.arraycopy (messages, 0, newMessages, 0, messagesSize);
			messages = newMessages;
		}
		messages [messagesSize++] = entry;
	}
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
	if (runnable != null) addLast (new RunnableLock (runnable));
	display.wake ();
}

void releaseSynchronizer () {
	display = null;
	messages = null;
	messageLock = null;
	syncThread = null;
}

RunnableLock removeFirst () {
	synchronized (messageLock) {
		if (messagesSize == 0) return null;
		RunnableLock lock = messages [0];
		System.arraycopy (messages, 1, messages, 0, --messagesSize);
		messages [messagesSize] = null;
		if (messagesSize == 0) messages = null;
		return lock;
	}
}

boolean runAsyncMessages () {
	if (messagesSize == 0) return false;
	do {
		RunnableLock lock = removeFirst ();
		if (lock == null) return true;
		synchronized (lock) {
			syncThread = lock.thread;
			try {
				lock.run ();
			} catch (Throwable t) {
				lock.throwable = t;
				SWT.error (SWT.ERROR_FAILED_EXEC, t);
			} finally {
				syncThread = null;
				lock.notifyAll ();
			}
		}
	} while (true);
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
 *    <li>ERROR_FAILED_EXEC - if an exception occured when executing the runnable</li>
 * </ul>
 *
 * @see #asyncExec
 */
protected void syncExec (Runnable runnable) {
	if (display.isValidThread ()) {
		if (runnable != null) runnable.run ();
		return;
	}
	if (runnable == null) {
		display.wake ();
		return;
	}
	RunnableLock lock = new RunnableLock (runnable);
	/*
	 * Only remember the syncThread for syncExec.
	 */
	lock.thread = Thread.currentThread();
	synchronized (lock) {
		addLast (lock);
		display.wake ();
		boolean interrupted = false;
		while (!lock.done ()) {
			try {
				lock.wait ();
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
		if (interrupted) {
			Thread.currentThread ().interrupt ();
		}
		if (lock.throwable != null) {
			SWT.error (SWT.ERROR_FAILED_EXEC, lock.throwable);
		}
	}
}

}
