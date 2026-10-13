/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Differential tests of the lazy data source and lazy tab content against the unmodified
 * behaviour: a synchronous SWT.SetData listener that sets the same strings and counts is the
 * oracle; once a block is loaded every compared API result must be identical.
 */
public class Test_org_eclipse_swt_widgets_VirtualPageSource {
	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		shell.setBounds (300, 200, 520, 420);
		/* Keep the pointer off the shell so hot-tracking never differs between pixel captures. */
		display.setCursorLocation (2, 2);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
		while (display.readAndDispatch ()) {
			// flush
		}
	}

	static String cell (int row, int column) {
		return "r" + row + "c" + column;
	}

	private void drain (VirtualPageSource source) {
		long deadline = System.currentTimeMillis () + 20_000;
		int quiet = 0;
		while (System.currentTimeMillis () < deadline) {
			boolean busy = false;
			while (display.readAndDispatch ()) busy = true;
			if (!busy && source.isIdle ()) {
				if (++quiet > 3) return;
			} else {
				quiet = 0;
			}
			try {
				Thread.sleep (2);
			} catch (InterruptedException e) {
				Thread.currentThread ().interrupt ();
				return;
			}
		}
		fail ("source did not become idle");
	}

	private void flush () {
		for (int i = 0; i < 5; i++) {
			while (display.readAndDispatch ()) {
				// flush
			}
		}
	}

	private static Table table (Composite parent, int columns, int rows) {
		Table table = new Table (parent, SWT.VIRTUAL | SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		for (int c = 0; c < columns; c++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText ("C" + c);
			column.setWidth (80);
		}
		table.setItemCount (rows);
		return table;
	}

	private static VirtualPageSource tableSource (int blockSize, int columns, AtomicInteger fetches) {
		return new VirtualPageSource (blockSize, 32, columns, VirtualPageSource.NONE, request -> {
			if (fetches != null) fetches.incrementAndGet ();
			for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) {
				for (int c = 0; c < columns; c++) request.setText (i, c, cell (i, c));
			}
		});
	}

	private static Object field (Object target, String name) throws Exception {
		Class<?> type = target.getClass ();
		while (type != null) {
			try {
				Field field = type.getDeclaredField (name);
				field.setAccessible (true);
				return field.get (target);
			} catch (NoSuchFieldException e) {
				type = type.getSuperclass ();
			}
		}
		throw new NoSuchFieldException (name);
	}

	private static int materialized (Table table) throws Exception {
		Object storage = field (table, "virtualItems");
		Method size = storage.getClass ().getDeclaredMethod ("size");
		size.setAccessible (true);
		return (int) size.invoke (storage);
	}

	private static void unpin (Table table, TableItem item) throws Exception {
		Object storage = field (table, "virtualItems");
		Method flag = storage.getClass ().getDeclaredMethod ("flagOfIdentity", Object.class, long.class, boolean.class);
		flag.setAccessible (true);
		Field pinned = Class.forName ("org.eclipse.swt.widgets.VirtualItemState").getDeclaredField ("PINNED");
		pinned.setAccessible (true);
		flag.invoke (storage, item, pinned.getLong (null), false);
	}

	private static ImageData capture (Control control) {
		Point size = control.getSize ();
		Image image = new Image (control.getDisplay (), size.x, size.y);
		GC gc = new GC (image);
		try {
			control.print (gc);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return data;
	}

	/** Differing scanline span of two captures, for failure messages. */
	private static String diffSpan (ImageData a, ImageData b) {
		if (a.width != b.width || a.height != b.height) return "size differs";
		int first = -1, last = -1;
		for (int y = 0; y < a.height; y++) {
			int offset = y * a.bytesPerLine;
			if (!Arrays.equals (a.data, offset, offset + a.bytesPerLine, b.data, offset, offset + b.bytesPerLine)) {
				if (first < 0) first = y;
				last = y;
			}
		}
		return first < 0 ? "identical" : "rows y=" + first + ".." + last;
	}

	private static String pixelHash (Control control) throws Exception {
		Point size = control.getSize ();
		Image image = new Image (control.getDisplay (), size.x, size.y);
		GC gc = new GC (image);
		try {
			control.print (gc);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		MessageDigest digest = MessageDigest.getInstance ("SHA-256");
		digest.update (data.data);
		return HexFormat.of ().formatHex (digest.digest ()).substring (0, 16);
	}

	/* ------------------------------------------------------------------ Table */

	@Test
	public void test_tableResultsIdenticalToSynchronousSetData () {
		int rows = 100_000, columns = 3;
		Table oracle = table (shell, columns, rows);
		oracle.addListener (SWT.SetData, e -> {
			TableItem item = (TableItem) e.item;
			for (int c = 0; c < columns; c++) item.setText (c, cell (e.index, c));
		});
		Table lazy = table (shell, columns, rows);
		VirtualPageSource source = tableSource (64, columns, null);
		source.attach (lazy);
		shell.open ();
		drain (source);
		for (int top : new int [] {0, 500, 50_000, 99_990, 1_234}) {
			oracle.setTopIndex (top);
			lazy.setTopIndex (top);
			drain (source);
			assertEquals (oracle.getTopIndex (), lazy.getTopIndex ());
			assertTrue (source.isLoaded (VirtualPageSource.ROOT, lazy.getTopIndex ()), "visible block loaded at " + top);
			for (int i = lazy.getTopIndex (), end = Math.min (rows, i + 20); i < end; i++) {
				for (int c = 0; c < columns; c++) assertEquals (oracle.getItem (i).getText (c), lazy.getItem (i).getText (c), "row " + i);
			}
		}
		Random random = new Random (7);
		for (int n = 0; n < 60; n++) {
			int i = random.nextInt (rows);
			for (int c = 0; c < columns; c++) assertEquals (oracle.getItem (i).getText (c), lazy.getItem (i).getText (c), "far row " + i);
			assertEquals (oracle.getItem (i).getText (), lazy.getItem (i).getText ());
		}
		assertEquals (oracle.getItemCount (), lazy.getItemCount ());
		oracle.setSelection (new int [] {3, 5, 77_777});
		lazy.setSelection (new int [] {3, 5, 77_777});
		assertArrayEquals (oracle.getSelectionIndices (), lazy.getSelectionIndices ());
		assertEquals (oracle.getSelectionCount (), lazy.getSelectionCount ());
		TableItem [] a = oracle.getSelection (), b = lazy.getSelection ();
		assertEquals (a.length, b.length);
		for (int k = 0; k < a.length; k++) assertEquals (a [k].getText (2), b [k].getText (2));
		for (int i : new int [] {10, 12, 99_999}) {
			oracle.getItem (i).setChecked (true);
			lazy.getItem (i).setChecked (true);
		}
		oracle.getItem (12).setGrayed (true);
		lazy.getItem (12).setGrayed (true);
		for (int i : new int [] {9, 10, 11, 12, 13, 99_999}) {
			assertEquals (oracle.getItem (i).getChecked (), lazy.getItem (i).getChecked (), "checked " + i);
			assertEquals (oracle.getItem (i).getGrayed (), lazy.getItem (i).getGrayed (), "grayed " + i);
		}
		oracle.clear (10);
		lazy.clear (10);
		assertEquals (oracle.getItem (10).getChecked (), lazy.getItem (10).getChecked ());
		assertEquals (oracle.getItem (10).getText (1), lazy.getItem (10).getText (1));
		oracle.remove (11);
		lazy.remove (11);
		assertEquals (oracle.getItemCount (), lazy.getItemCount ());
		assertEquals (oracle.getItem (11).getGrayed (), lazy.getItem (11).getGrayed (), "grayed bit shifted with the row");
		assertEquals (oracle.getItem (11).getChecked (), lazy.getItem (11).getChecked ());
		assertEquals (0, source.getCount (VirtualPageSource.FAILED));
	}

	@Test
	public void test_tableTransientFacadesAreReleasedAndReproduced () throws Exception {
		int rows = 1_000_000, columns = 2;
		Table lazy = table (shell, columns, rows);
		VirtualPageSource source = tableSource (32, columns, null);
		source.setOverscanBlocks (1);
		source.attach (lazy);
		shell.open ();
		drain (source);
		lazy.setTopIndex (40);
		drain (source);
		String before = pixelHash (lazy);
		TableItem probe = lazy.getItem (45);
		probe.setChecked (true);
		lazy.getItem (46).setGrayed (true);
		lazy.getItem (46).setChecked (true);
		unpin (lazy, probe);
		unpin (lazy, lazy.getItem (46));
		String checkedHash = pixelHash (lazy);
		ImageData checkedImage = capture (lazy);
		assertNotEquals (before, checkedHash, "check marks are drawn");
		for (int top = 1_000; top < 600_000; top += 97_000) {
			lazy.setTopIndex (top);
			drain (source);
		}
		int bound = materialized (lazy);
		assertTrue (source.getCount (VirtualPageSource.RELEASED) > 0, "facades were released");
		assertTrue (probe.isDisposed (), "unpinned facade released after it scrolled out");
		lazy.setTopIndex (40);
		drain (source);
		ImageData again = capture (lazy);
		assertEquals (checkedHash, pixelHash (lazy), "pixels identical after release and re-creation: " + diffSpan (checkedImage, again)
				+ " itemHeight=" + lazy.getItemHeight () + " header=" + lazy.getHeaderHeight () + " top=" + lazy.getTopIndex ());
		assertTrue (lazy.getItem (45).getChecked ());
		assertTrue (lazy.getItem (46).getGrayed ());
		assertTrue (lazy.getItem (46).getChecked ());
		assertEquals (cell (45, 1), lazy.getItem (45).getText (1));
		assertTrue (bound < 400, "materialized facades bounded, was " + bound);
	}

	@Test
	public void test_tableVisibleFirstAndCancelledOutsideOverscan () throws Exception {
		int rows = 10_000_000;
		Table lazy = table (shell, 1, rows);
		CountDownLatch gate = new CountDownLatch (1);
		java.util.List<Integer> order = Collections.synchronizedList (new ArrayList<> ());
		VirtualPageSource source = new VirtualPageSource (100, 16, 1, VirtualPageSource.NONE, request -> {
			order.add (request.getStart () / 100);
			gate.await (5, TimeUnit.SECONDS);
			for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) request.setText (i, 0, cell (i, 0));
		});
		source.setOverscanBlocks (2);
		source.attach (lazy);
		lazy.setTopIndex (5_000_000);
		shell.open ();
		flush ();
		Thread.sleep (100);
		flush ();
		int visibleBlock = lazy.getTopIndex () / 100;
		assertFalse (order.isEmpty ());
		assertEquals (visibleBlock, (int) order.get (0), "visible block requested first");
		for (int top = 1_000_000; top < 9_000_000; top += 1_000_000) {
			lazy.setTopIndex (top);
			flush ();
		}
		gate.countDown ();
		drain (source);
		assertTrue (source.getCount (VirtualPageSource.CANCELLED) > 0, "blocks outside the overscan were cancelled");
		assertTrue (source.getCount (VirtualPageSource.IN_FLIGHT) == 0);
		assertTrue (source.getCount (VirtualPageSource.RESIDENT) <= 16);
		assertEquals (cell (lazy.getTopIndex (), 0), lazy.getItem (lazy.getTopIndex ()).getText ());
	}

	@Test
	public void test_tableEachRowFetchedOnceAcrossScrollAwayAndBackAndDataReproduced () throws Exception {
		int rows = 200_000;
		Table lazy = table (shell, 1, rows);
		AtomicIntegerArray perRow = new AtomicIntegerArray (rows);
		Object [] handles = new Object [rows];
		for (int i = 0; i < 4_000; i++) handles [i] = Integer.valueOf (i * 7);
		VirtualPageSource source = new VirtualPageSource (64, 48, 1, VirtualPageSource.NONE, request -> {
			for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) {
				perRow.incrementAndGet (i);
				request.setText (i, 0, cell (i, 0));
				request.setData (i, handles [i]);
			}
		});
		source.setOverscanBlocks (1);
		source.attach (lazy);
		shell.open ();
		drain (source);
		TableItem first = lazy.getItem (5);
		Object handle = first.getData ();
		assertSame (handles [5], handle, "data handle applied from the block");
		unpin (lazy, first);
		for (int top : new int [] {300, 900, 1_500, 900, 300, 0}) {
			lazy.setTopIndex (top);
			drain (source);
		}
		int max = 0;
		for (int i = 0; i < 1_600; i++) max = Math.max (max, perRow.get (i));
		assertEquals (1, max, "each row requested from the application once while its block stays resident");
		assertTrue (first.isDisposed (), "the facade of row 5 was released");
		assertSame (handle, lazy.getItem (5).getData (), "getData identity preserved after release and re-creation");
		assertEquals (cell (5, 0), lazy.getItem (5).getText ());
	}

	@Test
	public void test_invalidateRefetchesAndKeepFacadesNeverReleases () {
		int rows = 300_000;
		Table lazy = table (shell, 1, rows);
		AtomicInteger fetches = new AtomicInteger ();
		AtomicInteger epoch = new AtomicInteger ();
		VirtualPageSource source = new VirtualPageSource (64, 32, 1, VirtualPageSource.KEEP_FACADES | VirtualPageSource.PLACEHOLDER_TEXT, request -> {
			fetches.incrementAndGet ();
			for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) request.setText (i, 0, epoch.get () + ":" + i);
		});
		source.setPlaceholderText ("loading");
		source.attach (lazy);
		shell.open ();
		drain (source);
		int top = lazy.getTopIndex ();
		assertEquals ("0:" + top, lazy.getItem (top).getText ());
		int before = fetches.get ();
		epoch.set (1);
		source.invalidate ();
		drain (source);
		assertTrue (fetches.get () > before, "visible blocks fetched again");
		assertEquals ("1:" + top, lazy.getItem (top).getText (), "rows re-asked after invalidate");
		for (int t = 1_000; t < 250_000; t += 50_000) {
			lazy.setTopIndex (t);
			drain (source);
		}
		assertEquals (0, source.getCount (VirtualPageSource.RELEASED), "KEEP_FACADES never releases");
		assertThrows (IllegalArgumentException.class, () -> source.attach (lazy), "a source feeds one widget");
		source.detach ();
		assertEquals ("1:" + top, lazy.getItem (top).getText (), "rows keep their data after detach");
	}

	@Test
	public void test_constructorAndAttachArgumentChecks () {
		assertThrows (IllegalArgumentException.class, () -> new VirtualPageSource (0, 1, 1, 0, r -> { }));
		assertThrows (IllegalArgumentException.class, () -> new VirtualPageSource (1, 0, 1, 0, r -> { }));
		assertThrows (IllegalArgumentException.class, () -> new VirtualPageSource (1, 1, 0, 0, r -> { }));
		assertThrows (IllegalArgumentException.class, () -> new VirtualPageSource (1, 1, 1, 0, null));
		VirtualPageSource flat = new VirtualPageSource (8, 4, 1, VirtualPageSource.NONE, r -> { });
		VirtualPageSource tree = new VirtualPageSource (8, 4, 1, VirtualPageSource.TREE, r -> { });
		assertThrows (IllegalArgumentException.class, () -> flat.attach (new Table (shell, SWT.NONE)), "needs SWT.VIRTUAL");
		assertThrows (IllegalArgumentException.class, () -> flat.attach (new Tree (shell, SWT.VIRTUAL)), "flat source on a tree");
		assertThrows (IllegalArgumentException.class, () -> tree.attach (new Table (shell, SWT.VIRTUAL)), "tree source on a table");
		assertThrows (IllegalArgumentException.class, () -> flat.attach ((Table) null));
		assertThrows (IllegalArgumentException.class, () -> flat.setOverscanBlocks (-1));
		assertThrows (IllegalArgumentException.class, () -> flat.setPlaceholderText (null));
		assertThrows (IllegalArgumentException.class, () -> flat.getCount (99));
		assertFalse (flat.isLoaded (VirtualPageSource.ROOT, 0));
		assertTrue (flat.isIdle ());
	}

	@Test
	public void test_hiddenTableFetchesNothing () {
		Table lazy = table (shell, 1, 1_000_000);
		AtomicInteger fetches = new AtomicInteger ();
		VirtualPageSource source = tableSource (64, 1, fetches);
		source.attach (lazy);
		flush ();
		assertEquals (0, fetches.get (), "nothing fetched while the shell is not visible");
		shell.open ();
		drain (source);
		assertTrue (fetches.get () > 0);
	}

	/* ------------------------------------------------------------------ Tree */

	private static final int ROOTS = 40;

	private static int children (int root) {
		return 5 + root;
	}

	private Tree oracleTree () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		tree.addListener (SWT.SetData, e -> {
			TreeItem item = (TreeItem) e.item;
			TreeItem parent = item.getParentItem ();
			if (parent == null) {
				item.setText ("n" + e.index);
				item.setItemCount (children (e.index));
			} else {
				item.setText ("n" + parent.getParent ().indexOf (parent) + "." + e.index);
				item.setItemCount (0);
			}
		});
		tree.setItemCount (ROOTS);
		return tree;
	}

	private static VirtualPageSource treeSource (boolean exactCounts, CountDownLatch childGate, AtomicInteger childFetches) {
		return new VirtualPageSource (16, 32, 1, VirtualPageSource.TREE, request -> {
			int parent = request.getParentKey ();
			if (parent == VirtualPageSource.ROOT) {
				request.setTotal (ROOTS);
				for (int i = request.getStart (), end = Math.min (ROOTS, i + request.getCount ()); i < end; i++) {
					request.setText (i, 0, "n" + i);
					request.setKey (i, i);
					request.setChildCount (i, exactCounts ? children (i) : VirtualPageSource.UNKNOWN);
				}
				return;
			}
			if (childFetches != null) childFetches.incrementAndGet ();
			if (childGate != null) childGate.await (5, TimeUnit.SECONDS);
			int total = children (parent);
			request.setTotal (total);
			for (int i = request.getStart (), end = Math.min (total, i + request.getCount ()); i < end; i++) {
				request.setText (i, 0, "n" + parent + "." + i);
				request.setKey (i, 100_000 + parent * 1_000 + i);
				request.setChildCount (i, 0);
			}
		});
	}

	@Test
	public void test_treeExactCountsIdenticalToSynchronousSetData () {
		Tree oracle = oracleTree ();
		Tree lazy = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		VirtualPageSource source = treeSource (true, null, null);
		source.attach (lazy);
		shell.open ();
		drain (source);
		assertEquals (oracle.getItemCount (), lazy.getItemCount ());
		for (int i = 0; i < ROOTS; i++) {
			assertEquals (oracle.getItem (i).getText (), lazy.getItem (i).getText (), "root " + i);
			assertEquals (oracle.getItem (i).getItemCount (), lazy.getItem (i).getItemCount (), "count " + i);
		}
		for (int i : new int [] {0, 3, 17, 39}) {
			oracle.getItem (i).setExpanded (true);
			lazy.getItem (i).setExpanded (true);
			drain (source);
			TreeItem a = oracle.getItem (i), b = lazy.getItem (i);
			assertEquals (a.getExpanded (), b.getExpanded ());
			for (int j = 0; j < a.getItemCount (); j++) {
				assertEquals (a.getItem (j).getText (), b.getItem (j).getText (), "child " + i + "." + j);
				assertEquals (a.getItem (j).getItemCount (), b.getItem (j).getItemCount ());
			}
		}
		assertEquals (0, source.getCount (VirtualPageSource.FAILED));
	}

	@Test
	public void test_treeUnknownChildrenFetchedOnExpandWithMaskBitPlaceholder () throws Exception {
		Tree oracle = oracleTree ();
		Tree lazy = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		CountDownLatch gate = new CountDownLatch (1);
		AtomicInteger childFetches = new AtomicInteger ();
		VirtualPageSource source = treeSource (false, gate, childFetches);
		source.attach (lazy);
		shell.open ();
		drain (source);
		assertEquals (0, childFetches.get (), "no children fetched before expand");
		TreeItem node = lazy.getItem (2);
		assertEquals (1, node.getItemCount (), "an UNKNOWN node reports its one placeholder child until loaded");
		Object [] itemsBefore = ((Object []) field (lazy, "items")).clone ();
		long facadesBefore = Arrays.stream (itemsBefore).filter (Objects::nonNull).count ();
		long hItem = (long) field (node, "handle");
		long hTree = (long) field (lazy, "handle");
		Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
		Method send = os.getMethod ("SendMessage", long.class, int.class, long.class, long.class);
		int tvmExpand = os.getField ("TVM_EXPAND").getInt (null);
		int tveExpand = os.getField ("TVE_EXPAND").getInt (null);
		send.invoke (null, hTree, tvmExpand, (long) tveExpand, hItem);
		lazy.update ();
		long until = System.currentTimeMillis () + 3_000;
		while (childFetches.get () == 0 && System.currentTimeMillis () < until) {
			flush ();
			Thread.sleep (5);
		}
		lazy.update ();
		flush ();
		assertTrue (node.getExpanded ());
		assertEquals (1, childFetches.get (), "children requested on expand");
		Object [] itemsDuring = (Object []) field (lazy, "items");
		long facadesDuring = Arrays.stream (itemsDuring).filter (Objects::nonNull).count ();
		assertEquals (facadesBefore, facadesDuring, "the loading child is a mask bit, not a TreeItem");
		long hChild = (long) send.invoke (null, hTree, os.getField ("TVM_GETNEXTITEM").getInt (null), (long) os.getField ("TVGN_CHILD").getInt (null), hItem);
		assertNotEquals (0L, hChild);
		Class<?> tvitemType = Class.forName ("org.eclipse.swt.internal.win32.TVITEM");
		Object tvItem = tvitemType.getConstructor ().newInstance ();
		long hHeap = (long) os.getMethod ("GetProcessHeap").invoke (null);
		long buffer = (long) os.getMethod ("HeapAlloc", long.class, int.class, int.class).invoke (null, hHeap, os.getField ("HEAP_ZERO_MEMORY").getInt (null), 256 * 2);
		tvitemType.getField ("mask").setInt (tvItem, os.getField ("TVIF_TEXT").getInt (null) | os.getField ("TVIF_HANDLE").getInt (null));
		tvitemType.getField ("hItem").setLong (tvItem, hChild);
		tvitemType.getField ("pszText").setLong (tvItem, buffer);
		tvitemType.getField ("cchTextMax").setInt (tvItem, 256);
		os.getMethod ("SendMessage", long.class, int.class, long.class, tvitemType).invoke (null, hTree, os.getField ("TVM_GETITEM").getInt (null), 0L, tvItem);
		char [] chars = new char [256];
		os.getMethod ("MoveMemory", char [].class, long.class, int.class).invoke (null, chars, buffer, 512);
		os.getMethod ("HeapFree", long.class, int.class, long.class).invoke (null, hHeap, 0, buffer);
		String loading = new String (chars, 0, Math.max (0, new String (chars).indexOf ('\0')));
		assertEquals ("\u2026", loading, "the loading child is answered from the parent's mask bit");
		assertEquals (facadesBefore, Arrays.stream ((Object []) field (lazy, "items")).filter (Objects::nonNull).count (), "reading it creates no TreeItem");
		gate.countDown ();
		drain (source);
		TreeItem oracleNode = oracle.getItem (2);
		long hOracle = (long) field (oracle, "handle");
		send.invoke (null, hOracle, tvmExpand, (long) tveExpand, (long) field (oracleNode, "handle"));
		flush ();
		assertEquals (oracleNode.getExpanded (), node.getExpanded ());
		assertEquals (oracleNode.getItemCount (), node.getItemCount ());
		for (int j = 0; j < node.getItemCount (); j++) assertEquals (oracleNode.getItem (j).getText (), node.getItem (j).getText ());
		int tveCollapse = os.getField ("TVE_COLLAPSE").getInt (null);
		send.invoke (null, hTree, tvmExpand, (long) tveCollapse, hItem);
		send.invoke (null, hOracle, tvmExpand, (long) tveCollapse, (long) field (oracleNode, "handle"));
		flush ();
		assertEquals (oracleNode.getExpanded (), node.getExpanded (), "collapse identical to the synchronous tree");
		assertEquals (oracleNode.getItemCount (), node.getItemCount ());
	}

	/* ------------------------------------------------------------------ tabs */

	private static Control heavyContent (Composite parent, int labels) {
		Composite content = new Composite (parent, SWT.NONE);
		content.setLayout (new GridLayout (4, false));
		for (int i = 0; i < labels; i++) new Label (content, SWT.NONE).setText ("label " + i);
		return content;
	}

	@Test
	public void test_tabFolderVirtualCreatesOnlySelectedTab () {
		TabFolder folder = new TabFolder (shell, SWT.VIRTUAL);
		AtomicInteger created = new AtomicInteger ();
		folder.addListener (SWT.SetData, e -> {
			TabItem item = (TabItem) e.item;
			assertSame (folder.getItem (e.index), item);
			item.setControl (heavyContent (folder, 20));
			created.incrementAndGet ();
		});
		for (int i = 0; i < 50; i++) new TabItem (folder, SWT.NONE).setText ("Tab " + i);
		flush ();
		assertEquals (0, created.get (), "nothing created while hidden");
		shell.open ();
		flush ();
		assertEquals (1, created.get (), "startup creates only the selected tab");
		assertNotNull (folder.getItem (0).getControl ());
		assertNull (folder.getItem (1).getControl ());
		assertTrue (folder.getItem (0).getControl ().isVisible ());
		assertEquals (folder.getClientArea (), folder.getItem (0).getControl ().getBounds ());
		folder.setSelection (7);
		assertEquals (2, created.get ());
		assertTrue (folder.getItem (7).getControl ().isVisible ());
		assertFalse (folder.getItem (0).getControl ().isVisible ());
		folder.setSelection (7);
		folder.setSelection (0);
		assertEquals (2, created.get (), "each tab is filled once");
		new TabItem (folder, SWT.NONE, 0).setText ("inserted");
		folder.setSelection (1);
		assertEquals (2, created.get (), "filled bits shift on insert");
		folder.setSelection (0);
		assertEquals (3, created.get ());
		folder.getItem (0).dispose ();
		folder.setSelection (7);
		assertEquals (3, created.get (), "filled bits shift on remove");
	}

	@Test
	public void test_tabFolderDefaultUnchanged () {
		TabFolder folder = new TabFolder (shell, SWT.NONE);
		AtomicInteger setData = new AtomicInteger ();
		folder.addListener (SWT.SetData, e -> setData.incrementAndGet ());
		for (int i = 0; i < 5; i++) {
			TabItem item = new TabItem (folder, SWT.NONE);
			item.setControl (heavyContent (folder, 3));
		}
		shell.open ();
		flush ();
		folder.setSelection (3);
		flush ();
		assertEquals (0, setData.get ());
		assertEquals (0, folder.getStyle () & SWT.VIRTUAL);
	}

	@Test
	public void test_ctabFolderVirtualCreatesOnlySelectedTab () {
		CTabFolder folder = new CTabFolder (shell, SWT.VIRTUAL | SWT.BORDER);
		AtomicInteger created = new AtomicInteger ();
		folder.addListener (SWT.SetData, e -> {
			CTabItem item = (CTabItem) e.item;
			assertSame (folder.getItem (e.index), item);
			item.setControl (heavyContent (folder, 20));
			created.incrementAndGet ();
		});
		for (int i = 0; i < 50; i++) new CTabItem (folder, SWT.NONE).setText ("Tab " + i);
		folder.setSelection (0);
		flush ();
		assertEquals (0, created.get (), "nothing created while hidden");
		shell.open ();
		flush ();
		assertEquals (1, created.get (), "startup creates only the selected tab");
		assertTrue (folder.getItem (0).getControl ().isVisible ());
		assertEquals (folder.getClientArea (), folder.getItem (0).getControl ().getBounds ());
		assertNull (folder.getItem (1).getControl ());
		folder.setSelection (9);
		assertEquals (2, created.get ());
		folder.setSelection (9);
		folder.setSelection (0);
		assertEquals (2, created.get ());
		new CTabItem (folder, SWT.NONE, 0).setText ("inserted");
		folder.setSelection (1);
		assertEquals (2, created.get (), "filled bits shift on insert");
		folder.getItem (0).dispose ();
		folder.setSelection (9);
		assertEquals (2, created.get (), "filled bits shift on remove");
		assertTrue ((folder.getStyle () & SWT.VIRTUAL) != 0);
	}

	@Test
	public void test_ctabFolderDefaultUnchanged () {
		CTabFolder folder = new CTabFolder (shell, SWT.BORDER);
		AtomicInteger setData = new AtomicInteger ();
		folder.addListener (SWT.SetData, e -> setData.incrementAndGet ());
		for (int i = 0; i < 5; i++) {
			CTabItem item = new CTabItem (folder, SWT.NONE);
			item.setControl (heavyContent (folder, 3));
		}
		folder.setSelection (0);
		shell.open ();
		flush ();
		folder.setSelection (3);
		flush ();
		assertEquals (0, setData.get ());
		assertEquals (0, folder.getStyle () & SWT.VIRTUAL);
	}
}
