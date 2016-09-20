/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


/**
 * Instances of this class are used to ensure that an
 * application cannot interfere with the locking mechanism
 * used to implement asynchronous and synchronous communication
 * between widgets and background threads.
 */

class RunnableLock {
	Runnable runnable;
	Thread thread;
	Throwable throwable;

RunnableLock (Runnable runnable) {
	this.runnable = runnable;
}

boolean done () {
	return runnable == null || throwable != null;
}

void run (Display display) {
	if (runnable != null) {
		try {
			runnable.run ();
		} catch (RuntimeException exception) {
			display.getRuntimeExceptionHandler ().accept (exception);
		} catch (Error error) {
			display.getErrorHandler ().accept (error);
		}
	}
	runnable = null;
}

}
