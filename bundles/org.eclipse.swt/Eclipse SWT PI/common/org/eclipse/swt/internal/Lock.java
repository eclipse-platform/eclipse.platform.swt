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

public int lock() {
	synchronized (this) {
		Thread current = Thread.currentThread();
		if (owner != current) {
			while (count > 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					//
				}
			}
			owner = current;
		}
		return ++count;
	}
}

public int unlock() {
	synchronized (this) {
		Thread current = Thread.currentThread();
		if (owner == current) {
			if (--count == 0) {
				owner = null;
				notifyAll();
			}
		}
		return count;
	}
}
}
