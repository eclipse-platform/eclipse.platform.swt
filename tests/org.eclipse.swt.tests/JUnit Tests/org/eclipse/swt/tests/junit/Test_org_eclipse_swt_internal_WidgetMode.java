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
 * The startup widget mode ({@link WidgetMode}, docs/m3/ALL_WIDGETS_VIEWPORT_PLAN.md): the five modes and their names,
 * the global and the per-widget property, the fallback of a widget that does not implement the requested mode, and
 * what the mode log says. Every case uses a widget class name no SWT widget has, so no widget class reads a property
 * set here; the global property is set only inside {@link #globalAndWidgetProperties} and always restored. No display.
 */
public class Test_org_eclipse_swt_internal_WidgetMode {
	static final String WIDGET = "CoverageModeWidget";
	static final Logger JUL = Logger.getLogger ("org.eclipse.swt.internal.WidgetMode");

	String global;
	final BlockingQueue<String> published = new LinkedBlockingQueue<> ();
	final Handler capture = new Handler () {
		@Override
		public void publish (LogRecord record) {
			published.add (record.getLevel () + " " + record.getMessage ());
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
		global = System.getProperty (WidgetMode.PROPERTY);
		System.clearProperty (WidgetMode.PROPERTY);
		System.clearProperty (WidgetMode.PROPERTY + "." + WIDGET);
		JUL.addHandler (capture);
	}

	@AfterEach
	public void tearDown () {
		JUL.removeHandler (capture);
		JUL.setLevel (null);
		System.clearProperty (WidgetMode.PROPERTY + "." + WIDGET);
		if (global == null) {
			System.clearProperty (WidgetMode.PROPERTY);
		} else {
			System.setProperty (WidgetMode.PROPERTY, global);
		}
	}

	/* the next record the asynchronous SWT log publishes for WidgetMode */
	String next () throws InterruptedException {
		String record = published.poll (10, TimeUnit.SECONDS);
		assertNotNull (record, "no log record published");
		return record;
	}

	@Test
	public void fiveModes () {
		assertEquals (List.of ("VIEWPORT", "ORIGINAL", "CUSTOM", "M3", "GPU"), Arrays.stream (WidgetMode.values ()).map (Enum::name).toList ());
		assertEquals ("swt.widget.mode", WidgetMode.PROPERTY);
		assertThrows (IllegalArgumentException.class, () -> WidgetMode.valueOf ("SKIJA"), "the drawn mode is m3 (brief rule 14)");
	}

	@Test
	public void viewportByDefault () {
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.requested (WIDGET));
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT));
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET), "a widget without a viewport stays native");
	}

	@Test
	public void globalAndWidgetProperties () {
		System.setProperty (WidgetMode.PROPERTY, "m3");
		assertEquals (WidgetMode.M3, WidgetMode.requested (WIDGET), "the global property");
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, " Gpu ");
		assertEquals (WidgetMode.GPU, WidgetMode.requested (WIDGET), "the widget property wins, trimmed, any case");
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "original");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.requested (WIDGET));
		assertEquals (WidgetMode.M3, WidgetMode.requested (WIDGET + "Other"), "another widget reads the global property");
	}

	@Test
	public void fallback () {
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "custom");
		assertEquals (WidgetMode.CUSTOM, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT, WidgetMode.CUSTOM), "supported");
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT, WidgetMode.M3), "unsupported, back to the viewport");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET, WidgetMode.M3), "unsupported and no viewport, back to native");
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "original");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET, WidgetMode.VIEWPORT), "original is always implied");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET));
	}

	@Test
	public void unknownValueIsIgnoredAndLogged () throws InterruptedException {
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "skija");
		assertEquals (WidgetMode.VIEWPORT, WidgetMode.requested (WIDGET), "an unknown value asks for the default");
		assertEquals ("INFO ignoring swt.widget.mode value 'skija' for " + WIDGET, next ());
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "");
		assertEquals (WidgetMode.ORIGINAL, WidgetMode.of (WIDGET, WidgetMode.CUSTOM), "a blank value asks for the default too");
		assertEquals ("INFO ignoring swt.widget.mode value '' for " + WIDGET, next ());
	}

	@Test
	public void debugLogNamesTheFallback () throws InterruptedException {
		JUL.setLevel (Level.FINE);
		System.setProperty (WidgetMode.PROPERTY + "." + WIDGET, "m3");
		WidgetMode.of (WIDGET, WidgetMode.VIEWPORT, WidgetMode.M3);
		assertEquals ("FINE " + WIDGET + ": mode M3", next ());
		WidgetMode.of (WIDGET, WidgetMode.VIEWPORT);
		assertEquals ("FINE " + WIDGET + ": mode VIEWPORT (requested M3)", next ());
		JUL.setLevel (null);
		WidgetMode.of (WIDGET, WidgetMode.VIEWPORT);
		assertNull (published.poll (200, TimeUnit.MILLISECONDS), "no debug record at the default level");
	}
}
