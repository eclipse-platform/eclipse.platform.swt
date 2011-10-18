/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

/**
 * Instances of this represent a recursive monitor.
 */
public class Lock {
	int count, waitCount;
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
			waitCount++;
			while (count > 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					/* Wait forever, just like synchronized blocks */
				}
			}
			--waitCount;
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
				if (waitCount > 0) notifyAll();
			}
		}
	}
}
}
