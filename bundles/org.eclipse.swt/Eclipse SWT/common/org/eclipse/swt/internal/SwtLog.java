/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.logging.*;

/**
 * SWT's internal log on <code>java.util.logging</code> (JUL), asynchronous. TRACE, DEBUG and INFO map to
 * JUL's FINEST, FINE and INFO. The level check and the message text are computed on the calling thread
 * (messages read widget state, which belongs to the UI thread); the record is then published by one daemon
 * thread, so the UI thread never waits for a handler (console, file, socket). The queue is bounded: when it
 * is full a record is dropped and counted, and the count is reported with the next published record.
 * Configure levels and handlers with the usual JUL configuration, for example
 * <code>org.eclipse.swt.widgets.Tree.level = FINE</code>.
 */
public final class SwtLog {
	private static final int CAPACITY = 4096;
	private static final BlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<> (CAPACITY);
	private static final AtomicLong DROPPED = new AtomicLong ();
	private static final Thread PUBLISHER;

	static {
		PUBLISHER = new Thread (SwtLog::publish, "SWT log publisher");
		PUBLISHER.setDaemon (true);
		PUBLISHER.start ();
	}

	private final Logger logger;

	private SwtLog (String name) {
		logger = Logger.getLogger (name);
	}

	/**
	 * The log of a component, named like its class (for example <code>"org.eclipse.swt.widgets.Tree"</code>).
	 *
	 * @param name the JUL logger name
	 * @return the log
	 */
	public static SwtLog getLog (String name) {
		return new SwtLog (name);
	}

	public boolean isTraceEnabled () {
		return logger.isLoggable (Level.FINEST);
	}

	public boolean isDebugEnabled () {
		return logger.isLoggable (Level.FINE);
	}

	public void trace (Supplier<String> message) {
		write (Level.FINEST, message);
	}

	public void debug (Supplier<String> message) {
		write (Level.FINE, message);
	}

	public void info (Supplier<String> message) {
		write (Level.INFO, message);
	}

	private void write (Level level, Supplier<String> message) {
		if (!logger.isLoggable (level)) return;
		LogRecord record = new LogRecord (level, message.get ());
		record.setLoggerName (logger.getName ());
		if (!QUEUE.offer (() -> logger.log (record))) DROPPED.incrementAndGet ();
	}

	private static void publish () {
		while (true) {
			try {
				Runnable next = QUEUE.take ();
				long dropped = DROPPED.getAndSet (0);
				if (dropped > 0) Logger.getLogger ("org.eclipse.swt").warning ("SWT log queue full: " + dropped + " records dropped");
				next.run ();
			} catch (InterruptedException e) {
				return;
			} catch (RuntimeException e) {
				/* a failing handler never stops the publisher */
			}
		}
	}
}
