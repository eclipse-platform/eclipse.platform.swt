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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.management.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.logging.*;

import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;

/**
 * SWT's asynchronous JUL log: level mapping, lazy messages, the bounded queue that drops and reports
 * records when it is full, and a publisher thread that survives failing handlers. Headless.
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_internal_SwtLog {
	private static final String NAME = "org.eclipse.swt.tests.junit.SwtLogCoverage";
	/* the publisher's queue capacity (SwtLog.CAPACITY) */
	private static final int CAPACITY = 4096;

	private final Logger logger = Logger.getLogger (NAME);
	private final Logger swt = Logger.getLogger ("org.eclipse.swt");
	private final BlockingQueue<LogRecord> records = new LinkedBlockingQueue<> ();
	private final BlockingQueue<LogRecord> warnings = new LinkedBlockingQueue<> ();
	private final Capture capture = new Capture (records), warningCapture = new Capture (warnings);
	{
		/* only the publisher's own report; records of other SWT loggers (children of org.eclipse.swt) are ignored */
		warningCapture.setFilter (record -> "org.eclipse.swt".equals (record.getLoggerName ()));
	}
	private boolean swtParentHandlers;
	private Level swtLevel;

	static class Capture extends Handler {
		final BlockingQueue<LogRecord> target;
		Capture (BlockingQueue<LogRecord> target) {
			this.target = target;
		}
		@Override
		public void publish (LogRecord record) {
			target.add (record);
		}
		@Override
		public void flush () {
		}
		@Override
		public void close () {
		}
	}

	@BeforeEach
	public void setUp () {
		logger.setUseParentHandlers (false);
		logger.addHandler (capture);
		swtParentHandlers = swt.getUseParentHandlers ();
		swtLevel = swt.getLevel ();
		swt.addHandler (warningCapture);
	}

	@AfterEach
	public void tearDown () {
		for (Handler handler : logger.getHandlers ()) logger.removeHandler (handler);
		logger.setLevel (null);
		logger.setUseParentHandlers (true);
		swt.removeHandler (warningCapture);
		swt.setUseParentHandlers (swtParentHandlers);
		swt.setLevel (swtLevel);
	}

	private static LogRecord next (BlockingQueue<LogRecord> queue) throws InterruptedException {
		LogRecord record = queue.poll (10, TimeUnit.SECONDS);
		assertNotNull (record, "the publisher thread delivered a record");
		return record;
	}

	@Test
	public void levelsMapToFinestFineAndInfo () throws InterruptedException {
		logger.setLevel (Level.ALL);
		SwtLog log = SwtLog.getLog (NAME);
		assertTrue (log.isTraceEnabled ());
		assertTrue (log.isDebugEnabled ());
		log.trace (() -> "trace message");
		log.debug (() -> "debug message");
		log.info (() -> "info message");
		LogRecord trace = next (records), debug = next (records), info = next (records);
		assertEquals (List.of (Level.FINEST, Level.FINE, Level.INFO), List.of (trace.getLevel (), debug.getLevel (), info.getLevel ()));
		assertEquals (List.of ("trace message", "debug message", "info message"), List.of (trace.getMessage (), debug.getMessage (), info.getMessage ()));
		assertEquals (NAME, info.getLoggerName ());
	}

	@Test
	public void disabledLevelsNeverComputeTheMessage () throws InterruptedException {
		logger.setLevel (Level.INFO);
		SwtLog log = SwtLog.getLog (NAME);
		assertFalse (log.isTraceEnabled ());
		assertFalse (log.isDebugEnabled ());
		AtomicInteger computed = new AtomicInteger ();
		log.trace (() -> "trace " + computed.incrementAndGet ());
		log.debug (() -> "debug " + computed.incrementAndGet ());
		assertEquals (0, computed.get (), "a message below the level is not computed");
		log.info (() -> "info " + computed.incrementAndGet ());
		assertEquals ("info 1", next (records).getMessage ());
		assertNull (records.poll (200, TimeUnit.MILLISECONDS), "nothing below the level was published");
	}

	@Test
	public void aFailingHandlerNeverStopsThePublisher () throws InterruptedException {
		logger.setLevel (Level.INFO);
		logger.addHandler (new Handler () {
			@Override
			public void publish (LogRecord record) {
				if (record.getMessage ().startsWith ("fail")) throw new IllegalStateException ("handler failure");
			}
			@Override
			public void flush () {
			}
			@Override
			public void close () {
			}
		});
		/* the failing handler is the last one: the capture still sees the failing record */
		SwtLog log = SwtLog.getLog (NAME);
		log.info (() -> "fail once");
		log.info (() -> "after the failure");
		assertEquals ("fail once", next (records).getMessage ());
		assertEquals ("after the failure", next (records).getMessage ());
	}

	@Test
	public void aFullQueueDropsRecordsAndReportsTheCount () throws InterruptedException {
		logger.setLevel (Level.INFO);
		swt.setUseParentHandlers (false);
		swt.setLevel (Level.INFO);
		CountDownLatch entered = new CountDownLatch (1), release = new CountDownLatch (1);
		logger.addHandler (new Handler () {
			@Override
			public void publish (LogRecord record) {
				if (!"block".equals (record.getMessage ())) return;
				entered.countDown ();
				try {
					release.await (30, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
				}
			}
			@Override
			public void flush () {
			}
			@Override
			public void close () {
			}
		});
		SwtLog log = SwtLog.getLog (NAME);
		log.info (() -> "block");
		assertTrue (entered.await (10, TimeUnit.SECONDS), "the publisher is inside the blocking handler");
		int extra = 25;
		for (int i = 0; i < CAPACITY + extra; i++) {
			final int n = i;
			log.info (() -> "queued " + n);
		}
		release.countDown ();
		LogRecord warning = next (warnings);
		assertEquals (Level.WARNING, warning.getLevel ());
		assertEquals ("SWT log queue full: " + extra + " records dropped", warning.getMessage ());
		assertEquals ("block", next (records).getMessage ());
		for (int i = 0; i < CAPACITY; i++) assertEquals ("queued " + i, next (records).getMessage ());
		assertNull (records.poll (200, TimeUnit.MILLISECONDS), "the dropped records are never published");
		log.info (() -> "after the drops");
		assertEquals ("after the drops", next (records).getMessage ());
		assertNull (warnings.poll (200, TimeUnit.MILLISECONDS), "the drop count is reported once");
	}

	/**
	 * Interrupting the publisher ends it. Runs in a child JVM (same class path and Java agents), so the
	 * publisher of this JVM keeps running for the other tests.
	 */
	@Test
	@Timeout (60)
	public void anInterruptedPublisherStops () throws Exception {
		List<String> command = new ArrayList<> ();
		command.add (Paths.get (System.getProperty ("java.home"), "bin", "java").toString ());
		for (String argument : ManagementFactory.getRuntimeMXBean ().getInputArguments ()) {
			if (argument.startsWith ("-javaagent:")) command.add (argument);
		}
		command.add ("-cp");
		command.add (System.getProperty ("java.class.path"));
		command.add (PublisherStop.class.getName ());
		Process process = new ProcessBuilder (command).redirectErrorStream (true).start ();
		String output;
		try (InputStream in = process.getInputStream ()) {
			output = new String (in.readAllBytes ());
		}
		assertTrue (process.waitFor (30, TimeUnit.SECONDS), "child JVM finished");
		assertEquals (0, process.exitValue (), output);
		assertTrue (output.contains ("publisher stopped"), output);
	}

	/** Child JVM of {@link #anInterruptedPublisherStops()}: interrupts the publisher and waits for it to end. */
	public static final class PublisherStop {
		public static void main (String [] args) throws InterruptedException {
			SwtLog.getLog (NAME).trace (() -> "starts the publisher");
			Thread publisher = Thread.getAllStackTraces ().keySet ().stream ()
					.filter (t -> "SWT log publisher".equals (t.getName ())).findFirst ().orElseThrow ();
			publisher.interrupt ();
			publisher.join (20_000);
			if (publisher.isAlive ()) System.exit (2);
			System.out.println ("publisher stopped");
		}
	}
}
