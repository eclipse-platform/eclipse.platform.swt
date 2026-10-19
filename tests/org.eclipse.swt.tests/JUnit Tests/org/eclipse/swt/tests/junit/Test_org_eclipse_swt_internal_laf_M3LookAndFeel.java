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
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.laf.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The look and feel manager (docs/m3/LOOK_AND_FEEL.md, LF6): selection and runtime switching with listeners, UI
 * delegates installed from a look and feel's factory, the diff-driven update that repaints or relays out only the
 * widgets whose delegate reads a changed key, Swing's full tree update, pinned widget classes, per-widget overrides,
 * and the colour and font resources disposed by a switch. Needs a display (hidden desktop).
 */
public class Test_org_eclipse_swt_internal_laf_M3LookAndFeel {
	static final int SELECTION = M3UIKeys.intern ("Table.selectionBackground", M3UIKeys.COLOR);
	static final int ROW_HEIGHT = M3UIKeys.intern ("Tree.rowHeight", M3UIKeys.INTEGER);
	static final int GRID = M3UIKeys.intern ("Table.gridColor", M3UIKeys.COLOR);

	/** A delegate that counts its installs and remembers its defaults. */
	static class Probe implements M3ComponentUI {
		final int [] paint, metric;
		int installs, uninstalls;
		M3UIDefaults defaults;

		Probe (int [] paint, int [] metric) {
			this.paint = paint;
			this.metric = metric;
		}

		@Override
		public void installUI (Control control, M3UIDefaults defaults) {
			installs++;
			this.defaults = defaults;
		}

		@Override
		public void uninstallUI (Control control) {
			uninstalls++;
		}

		@Override
		public Class<? extends M3ComponentUI> type () {
			return Probe.class;
		}

		@Override
		public int [] paintKeys () {
			return paint;
		}

		@Override
		public int [] metricKeys () {
			return metric;
		}
	}

	/** The delegate the factory of test-m3-c creates. */
	static class Special extends Probe {
		Special () {
			super (new int [] {SELECTION}, M3ComponentUI.NO_KEYS);
		}
	}

	Display display;
	Shell shell;
	int layouts;

	@BeforeAll
	public static void themes () {
		M3LookAndFeel.register ("test-m3-a", "@extends = flat\n", null);
		M3LookAndFeel.register ("test-m3-b", "@extends = flat\nTable.selectionBackground = #123456\nTree.rowHeight = 30\n", null);
		M3LookAndFeel.register ("test-m3-d", "@extends = test-m3-b\nTable.selectionBackground = #abcdef\n", null);
		M3LookAndFeel.register ("test-m3-c", "@extends = flat\nTable.selectionBackground = #654321\n", new RendererFactory () {
			@Override
			public <R> R create (Class<R> type, Object widget) {
				return type == Probe.class ? type.cast (new Special ()) : null;
			}
		});
	}

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (new Layout () {
			@Override
			protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
				return new Point (300, 200);
			}

			@Override
			protected void layout (Composite composite, boolean flushCache) {
				layouts++;
				for (Control child : composite.getChildren ()) child.setBounds (0, 0, 100, 20);
			}
		});
		shell.setSize (300, 200);
		shell.open ();
		M3LookAndFeel.setLookAndFeel ("test-m3-a");
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
		M3LookAndFeel.reset ();
		System.clearProperty ("swt.widget.theme.Canvas");
	}

	Canvas canvas (Probe probe, AtomicInteger paints) {
		Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addListener (SWT.Paint, e -> paints.incrementAndGet ());
		M3LookAndFeel.installUI (canvas, Probe.class, probe);
		return canvas;
	}

	void drain () {
		shell.layout (true, true);
		ViewportDifferential.drain (display);
		shell.update ();
		ViewportDifferential.drain (display);
	}

	@Test
	public void selectionAndListeners () {
		M3LookAndFeel.reset ();
		String startup = M3LookAndFeel.getLookAndFeel ();
		if (System.getProperty (LookAndFeel.PROPERTY) == null && System.getProperty (LookAndFeel.ALIAS) == null) assertEquals ("native", startup);
		List<String> changes = new ArrayList<> ();
		M3LookAndFeel.Listener listener = (from, to) -> changes.add (from + ">" + to);
		M3LookAndFeel.addListener (listener);
		M3LookAndFeel.setLookAndFeel ("  TEST-M3-B ");
		assertEquals ("test-m3-b", M3LookAndFeel.getLookAndFeel ());
		M3LookAndFeel.setLookAndFeel ("metal");
		M3LookAndFeel.removeListener (listener);
		M3LookAndFeel.setLookAndFeel ("test-m3-a");
		assertEquals (List.of (startup + ">test-m3-b", "test-m3-b>metal"), changes);
		IllegalArgumentException unknown = assertThrows (IllegalArgumentException.class, () -> M3LookAndFeel.setLookAndFeel ("no-such-look"));
		assertEquals ("unknown look and feel 'no-such-look'", unknown.getMessage ());
		assertEquals ("test-m3-a", M3LookAndFeel.getLookAndFeel (), "a failed switch keeps the selection");
		assertSame (M3LookAndFeel.defaults ("test-m3-a"), M3LookAndFeel.getDefaults (), "cached on a display thread");
		assertEquals ("native", M3LookAndFeel.of ("Tree", WidgetMode.VIEWPORT).name ());
		assertEquals ("test-m3-a", M3LookAndFeel.of ("Tree", WidgetMode.M3).name ());
	}

	@Test
	public void aSwitchTouchesOnlyTheWidgetsThatReadAChangedKey () {
		AtomicInteger paintsSelection = new AtomicInteger (), paintsRow = new AtomicInteger (), paintsGrid = new AtomicInteger ();
		Probe selection = new Probe (new int [] {SELECTION}, M3ComponentUI.NO_KEYS);
		Probe row = new Probe (new int [] {GRID}, new int [] {ROW_HEIGHT});
		Probe grid = new Probe (new int [] {GRID}, M3ComponentUI.NO_KEYS);
		canvas (selection, paintsSelection);
		canvas (row, paintsRow);
		canvas (grid, paintsGrid);
		drain ();
		assertEquals (3, M3LookAndFeel.installed (display));
		assertEquals ("test-m3-a", selection.defaults.name ());
		int layoutsBefore = layouts, selectionPaints = paintsSelection.get (), rowPaints = paintsRow.get (), gridPaints = paintsGrid.get ();
		layouts = 0;
		M3LookAndFeel.setLookAndFeel ("test-m3-b");
		ViewportDifferential.drain (display);
		shell.update ();
		assertEquals (2, selection.installs, "installed again: it paints with the changed selection colour");
		assertEquals (1, selection.uninstalls);
		assertEquals (0xFF123456, selection.defaults.color (SELECTION, 0));
		assertEquals (2, row.installs, "installed again: it measures with the changed row height");
		assertEquals (1, grid.installs, "untouched: the grid colour did not change");
		assertTrue (paintsSelection.get () > selectionPaints, "repainted");
		assertTrue (paintsRow.get () > rowPaints, "repainted");
		assertEquals (gridPaints, paintsGrid.get (), "not repainted");
		assertTrue (layouts > 0, "relaid out for the row height (" + layoutsBefore + " layouts before)");
		layouts = 0;
		M3LookAndFeel.setLookAndFeel ("test-m3-d");
		ViewportDifferential.drain (display);
		assertEquals (0, layouts, "a colour change needs no layout");
		assertEquals (3, selection.installs, "the selection colour changed again");
		assertEquals (2, row.installs, "the row height stayed");
		M3LookAndFeel.setLookAndFeel ("test-m3-d");
		assertEquals (3, selection.installs, "no change, no update");
	}

	@Test
	public void theFactoryOfTheNewLookAndFeelCreatesTheDelegate () {
		Canvas canvas = canvas (new Probe (new int [] {SELECTION}, M3ComponentUI.NO_KEYS), new AtomicInteger ());
		Probe installed = (Probe) M3LookAndFeel.getUI (canvas);
		M3LookAndFeel.setLookAndFeel ("test-m3-c");
		M3ComponentUI special = M3LookAndFeel.getUI (canvas);
		assertInstanceOf (Special.class, special);
		assertEquals (1, installed.uninstalls);
		assertEquals (1, ((Special) special).installs);
		Canvas direct = new Canvas (shell, SWT.NONE);
		assertInstanceOf (Special.class, M3LookAndFeel.installUI (direct, Probe.class, new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS)));
		M3LookAndFeel.setLookAndFeel ("test-m3-a");
		assertSame (special, M3LookAndFeel.getUI (canvas), "a factory without a delegate keeps the current one");
	}

	@Test
	public void aPinnedWidgetClassKeepsItsLookAndFeel () {
		System.setProperty ("swt.widget.theme.Canvas", "test-m3-a");
		Probe pinned = new Probe (new int [] {SELECTION}, M3ComponentUI.NO_KEYS);
		canvas (pinned, new AtomicInteger ());
		M3LookAndFeel.setLookAndFeel ("test-m3-b");
		assertEquals (1, pinned.installs);
		assertEquals ("test-m3-a", pinned.defaults.name ());
		assertEquals ("test-m3-a", M3LookAndFeel.of ("Canvas", WidgetMode.CUSTOM).name ());
		System.clearProperty ("swt.widget.theme.Canvas");
		assertEquals (1, M3LookAndFeel.update (display, M3LookAndFeel.defaults ("test-m3-a"), M3LookAndFeel.defaults ("test-m3-b")),
			"unpinned, it follows the selection (its own diff: installed with test-m3-a)");
		assertEquals ("test-m3-b", pinned.defaults.name ());
	}

	@Test
	public void updateComponentTreeUIInstallsEveryDelegateAgain () {
		Probe a = new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS), b = new Probe (new int [] {GRID}, M3ComponentUI.NO_KEYS);
		canvas (a, new AtomicInteger ());
		Composite group = new Composite (shell, SWT.NONE);
		Canvas inner = new Canvas (group, SWT.NONE);
		M3LookAndFeel.installUI (inner, Probe.class, b);
		new Label (group, SWT.NONE);
		assertEquals (2, M3LookAndFeel.updateComponentTreeUI (shell));
		assertEquals (2, a.installs);
		assertEquals (2, b.installs);
		M3LookAndFeel.setLookAndFeel ("test-m3-b");
		assertEquals (2, a.installs, "a delegate that reads nothing is never updated by a switch");
		assertEquals (2, b.installs, "the grid colour did not change");
	}

	@Test
	public void delegatesLeaveWithTheirWidgets () {
		int before = M3LookAndFeel.installed (display);
		Probe first = new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS), second = new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS);
		Canvas a = canvas (first, new AtomicInteger ()), b = canvas (second, new AtomicInteger ()), c = canvas (new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS), new AtomicInteger ());
		assertEquals (before + 3, M3LookAndFeel.installed (display));
		M3LookAndFeel.installUI (a, Probe.class, new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS));
		assertEquals (1, first.uninstalls, "installing again replaces the delegate");
		assertEquals (before + 3, M3LookAndFeel.installed (display));
		M3LookAndFeel.uninstallUI (b);
		M3LookAndFeel.uninstallUI (b);
		assertNull (M3LookAndFeel.getUI (b));
		assertEquals (1, second.uninstalls);
		a.dispose ();
		assertEquals (before + 1, M3LookAndFeel.installed (display));
		assertNotNull (M3LookAndFeel.getUI (c), "the moved slot still finds its widget");
		M3LookAndFeel.uninstallUI (c);
		assertEquals (before, M3LookAndFeel.installed (display));
		for (int i = 0; i < 40; i++) canvas (new Probe (M3ComponentUI.NO_KEYS, M3ComponentUI.NO_KEYS), new AtomicInteger ());
		assertEquals (before + 40, M3LookAndFeel.installed (display), "the registry grows");
	}

	@Test
	public void overridesRepaintOnlyTheirReaders () {
		AtomicInteger readerPaints = new AtomicInteger (), otherPaints = new AtomicInteger ();
		Canvas reader = canvas (new Probe (new int [] {SELECTION}, new int [] {ROW_HEIGHT}), readerPaints);
		Canvas other = canvas (new Probe (new int [] {GRID}, M3ComponentUI.NO_KEYS), otherPaints);
		Canvas plain = new Canvas (shell, SWT.NONE);
		drain ();
		int readerBefore = readerPaints.get (), otherBefore = otherPaints.get ();
		layouts = 0;
		M3LookAndFeel.override (reader, SELECTION, 0xFF0000FF);
		M3LookAndFeel.override (other, SELECTION, 0xFF0000FF);
		M3LookAndFeel.override (plain, SELECTION, 0xFF0000FF);
		ViewportDifferential.drain (display);
		shell.update ();
		assertTrue (readerPaints.get () > readerBefore);
		assertEquals (otherBefore, otherPaints.get ());
		assertEquals (0, layouts);
		M3LookAndFeel.override (reader, ROW_HEIGHT, 44);
		ViewportDifferential.drain (display);
		assertTrue (layouts > 0, "a measured key relays out");
		M3UIOverrides lane = M3UIOverrides.of (display);
		assertEquals (0xFF0000FF, M3LookAndFeel.getDefaults ().color (lane, plain, SELECTION, 0));
		assertEquals (44, M3LookAndFeel.getDefaults ().integer (lane, reader, ROW_HEIGHT, 0));
	}

	@Test
	public void resourcesAreSharedAndDisposedByASwitch () {
		M3UIResources resources = M3UIResources.of (display);
		assertSame (resources, M3UIResources.of (display));
		Color colour = resources.color (0x80112233);
		assertSame (colour, resources.color (0x80112233));
		assertEquals (new RGBA (0x11, 0x22, 0x33, 0x80), colour.getRGBA ());
		for (int i = 0; i < 100; i++) resources.color (0xFF000000 | i * 977);
		assertSame (colour, resources.color (0x80112233), "found after the table grew");
		int id = M3UIFonts.intern ("Arial", 11, SWT.BOLD);
		assertEquals (id, M3UIFonts.intern ("Arial", 11, SWT.BOLD));
		assertEquals ("Arial", M3UIFonts.name (id));
		assertEquals (11, M3UIFonts.height (id));
		assertEquals (SWT.BOLD, M3UIFonts.style (id));
		for (int i = 0; i < 20; i++) M3UIFonts.intern ("Coverage", 8 + i, SWT.NORMAL);
		assertTrue (M3UIFonts.count () > 20);
		Font font = resources.font (id);
		assertSame (font, resources.font (id));
		assertEquals (11, font.getFontData () [0].getHeight ());
		assertSame (resources.font (M3UIFonts.count () - 1), resources.font (M3UIFonts.count () - 1), "the font lane grows");
		assertTrue (resources.colours () >= 101);
		assertTrue (resources.fonts () >= 2);
		int disposals = resources.disposals ();
		M3LookAndFeel.setLookAndFeel ("test-m3-b");
		assertTrue (colour.isDisposed ());
		assertTrue (font.isDisposed ());
		assertEquals (disposals + 1, resources.disposals ());
		assertEquals (0, resources.colours ());
		assertNotSame (colour, resources.color (0x80112233));
	}

	@Test
	public void defaultsResolvedWithoutADisplayAreNotCached () throws InterruptedException {
		M3LookAndFeel.register ("test-m3-e", "@extends = test-m3-b\n", null);
		M3UIDefaults [] seen = new M3UIDefaults [2];
		Thread thread = new Thread (() -> {
			seen [0] = M3LookAndFeel.defaults ("test-m3-e");
			seen [1] = M3LookAndFeel.defaults ("test-m3-e");
		});
		thread.start ();
		thread.join ();
		assertNotSame (seen [0], seen [1]);
		assertEquals (0xFF123456, seen [0].color (SELECTION, 0));
		M3UIDefaults developer = M3LookAndFeel.getDefaults ();
		developer.put (GRID, 0xFF00FF00);
		try {
			M3LookAndFeel.setLookAndFeel ("test-m3-b");
			assertEquals (0xFF00FF00, M3LookAndFeel.getDefaults ().color (GRID, 0), "the developer layer outlives the switch");
		} finally {
			developer.remove (GRID);
		}
	}
}
