/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

/**
 * Instance of this represent a recursive monitor.
 */
public class Lock {
	int count;
	Thread owner;

/**
 * Locks the monitor and returns the lock count. If
 * the lock is owned by another thread, wait until
 * the lock is released.
 * 
 * @return the lock count
 */
public int lock() {
	synchronized (this) {
		Thread current = Thread.currentThread();
		if (owner != current) {
			while (count > 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					/* Wait forever, just like synchronized blocks */
				}
			}
			owner = current;
		}
		return ++count;
	}
}

/**
 * Unlocks the monitor. If the current thread is not
 * the monitor owner, do nothing.
 */
public void unlock() {
	synchronized (this) {
		Thread current = Thread.currentThread();
		if (owner == current) {
			if (--count == 0) {
				owner = null;
				notifyAll();
			}
		}
	}
}
}
