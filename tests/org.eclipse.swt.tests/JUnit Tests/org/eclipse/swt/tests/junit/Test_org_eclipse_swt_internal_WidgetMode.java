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

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;

/**
 * The startup switch of the widget modes: <code>-Dswt.widget.mode</code> for every widget and
 * <code>-Dswt.widget.mode.&lt;Widget&gt;</code> for one widget class, with the fallback of a widget that
 * does not implement the requested mode. Headless: the properties are read on every call.
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_internal_WidgetMode {
	private static final String WIDGET = "CoverageWidget";
	private static final String SPECIFIC = WidgetMode.PROPERTY + "." + WIDGET;
	private static final String LOGGER = "org.eclipse.swt.internal.WidgetMode";

	private String globalBefore, specificBefore;
	private Logger logger;
	private Level levelBefore;
	private final BlockingQueue<LogRecord> records = new LinkedBlockingQueue<> ();
	private final Handler capture = new Handler () {
		@Override
		public void publish (LogRecord record) {
			records.add (record);
		}
		@Override
		public void flush () {
		}
		@Override
		public void close () {
		}
	};

	@BeforeEach
	public void setUp () {
		globalBefore = System.getProperty (WidgetMode.PROPERTY);
		specificBefore = System.getProperty (SPECIFIC);
		System.clearProperty (WidgetMode.PROPERTY);
		System.clearProperty (SPECIFIC);
		logger = Logger.getLogger (LOGGER);
		levelBefore = logger.getLevel ();
		logger.addHandler (capture);
	}

	@AfterEach
	public void tearDown () {
		restore (WidgetMode.PROPERTY, globalBefore);
		restore (SPECIFIC, specificBefore);
		logger.removeHandler (capture);
		logger.setLevel (levelBefore);
	}

	private static void restore (String key, String value) {
		if (value == null) System.clearProperty (key);
		else System.setProperty (key, value);
	}

	private LogRecord nextRecord () throws InterruptedException {
		LogRecord record = records.poll (10, TimeUnit.SECONDS);
		assertNotNull (record, "the asynchronous log published a record");
		return record;
	}

	@Test
	public void withoutPropertiesTheViewportIsRequested () {
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.requested (WIDGET));
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT));
		assertEquals ("swt.widget.mode", WidgetMode.PROPERTY);
	}

	@Test
	public void everyModeIsSelectedByTheGlobalProperty () {
		for (WidgetMode mode : WidgetMode.values ()) {
			System.setProperty (WidgetMode.PROPERTY, mode.name ().toLowerCase (Locale.ROOT));
			assertEquals (mode, WidgetMode.requested (WIDGET), mode.name ());
			assertEquals (mode, WidgetMode.of (WIDGET, WidgetMode.values ()), mode.name ());
		}
		assertEquals (5, WidgetMode.values ().length);
		assertEquals (WidgetMode.M3, WidgetMode.valueOf ("M3"));
	}

	@Test
	public void everyModeIsSelectedByTheWidgetProperty () {
		System.setProperty (WidgetMode.PROPERTY, "original");
		for (WidgetMode mode : WidgetMode.values ()) {
			System.setProperty (SPECIFIC, mode.name ());
			assertEquals (mode, WidgetMode.requested (WIDGET), mode.name ());
			assertEquals (mode, WidgetMode.of (WIDGET, WidgetMode.values ()), mode.name ());
			assertEquals (WidgetMode.ORIGINAL, WidgetMode.requested ("AnotherWidget"), "the override is per widget class");
		}
	}

	@Test
	public void valuesAreTrimmedAndCaseInsensitive () {
		System.setProperty (WidgetMode.PROPERTY, "  Gpu ");
		assertEquals (WidgetMode.GPU, WidgetMode.requested (WIDGET));
		System.setProperty (SPECIFIC, "\tcUsToM\n");
		assertEquals (WidgetMode.CUSTOM, WidgetMode.requested (WIDGET));
	}

	@Test
	public void invalidValuesFallBackToTheViewport () throws InterruptedException {
		logger.setLevel (Level.INFO);
		System.setProperty (WidgetMode.PROPERTY, "skija");
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.requested (WIDGET));
		LogRecord record = nextRecord ();
		assertEquals (Level.INFO, record.getLevel ());
		assertEquals ("ignoring swt.widget.mode value 'skija' for " + WIDGET, record.getMessage ());
		System.setProperty (SPECIFIC, "");
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.requested (WIDGET), "an empty widget value is invalid, not absent");
		assertEquals ("ignoring swt.widget.mode value '' for " + WIDGET, nextRecord ().getMessage ());
	}

	@Test
	public void unsupportedModesFallBackToTheViewportOrTheOriginal () {
		System.setProperty (WidgetMode.PROPERTY, "gpu");
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT, WidgetMode.CUSTOM));
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET), "a widget without a viewport falls back to the original");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET, WidgetMode.M3));
		System.setProperty (WidgetMode.PROPERTY, "original");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET), "the original mode is always implied");
	}

	@Test
	public void theChosenModeIsLoggedAtDebugLevel () throws InterruptedException {
		logger.setLevel (Level.FINE);
		System.setProperty (SPECIFIC, "m3");
		assertEquals (WidgetMode.M3, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT, WidgetMode.M3));
		LogRecord chosen = nextRecord ();
		assertEquals (Level.FINE, chosen.getLevel ());
		assertEquals (LOGGER, chosen.getLoggerName ());
		assertEquals (WIDGET + ": mode M3", chosen.getMessage ());
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT));
		assertEquals (WIDGET + ": mode VIEWPORT (requested M3)", nextRecord ().getMessage ());
	}
}
