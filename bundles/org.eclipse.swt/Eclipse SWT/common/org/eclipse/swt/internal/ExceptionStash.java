/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.internal;

import org.eclipse.swt.widgets.Display;

/**
 * The intent of this class is to insulate SWT from exceptions occurring in
 * user's listeners, so that SWT remains stable and does not accidentally
 * crash JVM or enter some very broken state. The supposed use is:
 * <br>
 * <pre>void someSwtInternalFunction() {
 *     // Make a stash to collect all exceptions from user listeners
 *     try (ExceptionStash exceptions = new ExceptionStash ()) {
 *         // Perform some action that may call a throwing user's listener
 *         try {
 *            sendEvent(SWT.SomeEvent);
 *         } catch (Error | RuntimeException ex) {
 *             exceptions.stash (ex);
 *         }
 *
 *         // Do some important SWT stuff that you would normally do in
 *         // 'finally' clause. With 'ExceptionStash' you can interleave
 *         // important things with listeners without making code ugly.
 *         MakeSureThingsDontBreak();
 *
 *         // Perform another action that may call a throwing user's listener.
 *         // Done in an independent 'try' block to make sure that all events
 *         // are sent regardless of exceptions in some of the listeners.
 *         try {
 *            askWidgetToSendMoreEvents();
 *         } catch (Error | RuntimeException ex) {
 *             exceptions.stash (ex);
 *         }
 *
 *         // Exiting from 'try' statement will close ExceptionStash and
 *         // re-throw collected exception. If there are multiple exceptions,
 *         // all subsequent ones will be added as 'Throwable.addSuppressed()'.
 *     }
 * } </pre>
 */
public class ExceptionStash implements AutoCloseable {
	Throwable storedThrowable;

public void stash(Throwable throwable) {
	/* First, try to pass it to the global handler */
	try {
		Display display = Display.getCurrent ();
		if (display != null) {
			if (throwable instanceof RuntimeException) {
				display.getRuntimeExceptionHandler().accept((RuntimeException)throwable);
				/* If handler doesn't throw then the exception is fully handled */
				return;
			} else if (throwable instanceof Error) {
				display.getErrorHandler().accept((Error)throwable);
				/* If handler doesn't throw then the exception is fully handled */
				return;
			}
		}
	} catch (Throwable ex) {
		/* Handler may have thrown something new */
		throwable = ex;
	}

	/* No handler or it also thrown */
	if (storedThrowable != null) {
		storedThrowable.addSuppressed(throwable);
	} else {
		storedThrowable = throwable;
	}
};

public void close() {
	if (storedThrowable == null) return;

	Throwable throwable = storedThrowable;
	storedThrowable = null;

	if (throwable instanceof RuntimeException) {
		throw (RuntimeException)throwable;
	} else if (throwable instanceof Error) {
		throw (Error)throwable;
	}
}

}
