// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.ItemResidencyFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

/**
 * Headless contracts of the shared item-residency engine: every tier (HOT, WARM, COLD, DESTROYED),
 * pressure arithmetic, SetData negotiation, the reproduction contract and identity rules
 * (docs/m3/M3_ITEM_RESIDENCY.md, docs/m3/item-residency/REPRODUCTION_STATE.tsv).
 */
public class Test_ItemResidency {
	private static final int N = 10_000;

	private static Object destroyEngine (Model model) {
		Object e = engine (k ("RESIDENCY_DESTROY"), 2, 64, model.source, null, null);
		call (e, "setCount", N);
		return e;
	}

	private static void scrollAwayUnderPressure (Object e) {
		call (e, "setViewport", 5000, 20, 5);
		call (e, "applyPressure", 0);
	}

	@Test
	public void roundTripReproducesEveryState () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setViewport", 0, 20, 5);
		String [] before = new String [25];
		for (int item = 0; item < 25; item++) {
			setEveryState (e, item, item % 4 == 0);
			before [item] = snapshot (e, item);
		}
		scrollAwayUnderPressure (e);
		for (int item = 0; item < 25; item++) {
			int expected = item % 4 == 0 ? k ("TIER_WARM") : k ("TIER_DESTROYED");
			assertEquals (expected, i (e, "tier", item), "tier of " + item);
		}
		assertTrue (i (e, "stat", k ("STAT_UNEVICTABLE")) > 0, "caller-owned payload refused eviction");
		call (e, "setViewport", 0, 20, 5);
		for (int item = 0; item < 25; item++) {
			assertEquals (before [item], snapshot (e, item), "item " + item);
			assertEquals (item % 4 == 0 ? 1 : 2, model.requests [item], "SetData count of " + item);
		}
		assertTrue (i (e, "stat", k ("STAT_RECREATED")) >= 18);
	}

	@Test
	public void roundTripReproducesEveryStateThroughSwap () {
		Model model = new Model (N, false);
		Object spill = spill ();
		Object e = engine (k ("RESIDENCY_DESTROY") | k ("RESIDENCY_SWAP"), 2, 64, model.source, spill, null);
		call (e, "setDataCodec", stringCodec ());
		call (e, "setCount", N);
		call (e, "setViewport", 0, 20, 5);
		String [] before = new String [25];
		for (int item = 0; item < 25; item++) {
			setEveryState (e, item, item % 4 == 0);
			before [item] = snapshot (e, item);
		}
		scrollAwayUnderPressure (e);
		for (int item = 0; item < 25; item++) {
			int expected = item % 4 == 0 ? k ("TIER_COLD") : k ("TIER_DESTROYED");
			assertEquals (expected, i (e, "tier", item), "tier of " + item);
		}
		assertTrue (i (e, "stat", k ("STAT_DATA_SWAPPED")) > 0);
		call (e, "setViewport", 0, 20, 5);
		for (int item = 0; item < 25; item++) {
			assertEquals (before [item], snapshot (e, item), "item " + item);
			assertEquals (item % 4 == 0 ? 1 : 2, model.requests [item], "a COLD item never re-fires SetData: " + item);
		}
		assertEquals (7, i (e, "stat", k ("STAT_SWAP_IN")));
		assertTrue (i (e, "stat", k ("STAT_DATA_RESTORED")) > 0);
		call (e, "dispose");
	}

	@Test
	public void lanesSurviveDestroyExactly () {
		int n = 200_000;
		Model model = new Model (n, false);
		Object e = engine (k ("RESIDENCY_DESTROY"), 2, 128, model.source, null, null);
		call (e, "setCount", n);
		int mask = userMask ();
		Random random = new Random (11);
		for (int item = 0; item < n; item += 7) {
			for (String lane : USER_LANES) call (e, "setBit", k (lane), item, random.nextBoolean ());
		}
		call (e, "setRange", k ("LANE_CHECKED"), 1000, 150_000, true);
		int [] before = new int [n];
		for (int item = 0; item < n; item++) before [item] = i (e, "gather", item) & mask;
		for (int first = 0; first < n; first += 50) call (e, "setViewport", first, 50, 25);
		for (int item = 0; item < n; item++) assertEquals (before [item], i (e, "gather", item) & mask, "item " + item);
		assertTrue (i (e, "stat", k ("STAT_DESTROYED")) > 190_000);
		assertEquals (149_000, i (e, "countRange", k ("LANE_CHECKED"), 1000, 150_000));
	}

	@Test
	public void setDataIsNegotiatedVisibleOverscanPrefetchAndCancelled () {
		Model model = new Model (N, true);
		Object e = engine (k ("RESIDENCY_DESTROY"), 2, 256, model.source, null, null);
		call (e, "setCount", N);
		call (e, "setPrefetch", 20);
		call (e, "setViewport", 100, 10, 5);
		assertRanges (model.ranges, 100, 110, 110, 115, 95, 100, 115, 135);
		call (e, "setViewport", 1000, 10, 5);
		assertRanges (model.cancels, 95, 135);
		model.ranges.clear ();
		call (e, "setViewport", 990, 10, 5);
		assertRanges (model.ranges, 990, 995, 985, 990, 965, 985);
		assertTrue (b (e, "bit", k ("LANE_DATA_REQUESTED"), 1000));
		Model.fill (e, 1000);
		assertTrue (b (e, "bit", k ("LANE_DATA_LOADED"), 1000));
		assertFalse (b (e, "bit", k ("LANE_DATA_REQUESTED"), 1000));
		assertEquals (k ("TIER_HOT"), i (e, "tier", 1000));
	}

	private static void assertRanges (List<int []> actual, int... expected) {
		int [] flat = actual.stream ().flatMapToInt (Arrays::stream).toArray ();
		assertArrayEquals (expected, flat, "ranges");
	}

	@Test
	public void tiersFollowViewportAndPressure () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setViewport", 0, 10, 5);
		call (e, "setViewport", 100, 10, 5);
		assertEquals (k ("TIER_HOT"), i (e, "tier", 100));
		assertEquals (k ("TIER_WARM"), i (e, "tier", 3));
		call (e, "applyPressure", 0);
		assertEquals (0, i (e, "stat", k ("STAT_PERMILLE")));
		assertEquals (20, i (e, "stat", k ("STAT_BUDGET")), "budget falls to the HOT window [95, 115)");
		assertEquals (k ("TIER_DESTROYED"), i (e, "tier", 3));
		assertEquals (20, i (e, "stat", k ("STAT_RESIDENT")));
		call (e, "applyPressure", 1000);
		assertEquals (125, i (e, "stat", k ("STAT_PERMILLE")), "recovery is rate-limited");
		call (e, "applyPressure", 1000);
		assertEquals (250, i (e, "stat", k ("STAT_PERMILLE")));
		call (e, "applyPressure", 40);
		assertEquals (40, i (e, "stat", k ("STAT_PERMILLE")), "shrink is immediate");
		assertEquals (1000, (int) (Integer) callStatic (ENGINE, "permilleOfUsed", 700));
		assertEquals (0, (int) (Integer) callStatic (ENGINE, "permilleOfUsed", 960));
		assertEquals (500, (int) (Integer) callStatic (ENGINE, "permilleOfUsed", 830));
	}

	@Test
	public void telemetryFailureZeroesOptionalResidency () {
		Model model = new Model (N, false);
		Gauge gauge = new Gauge ();
		Object e = engine (k ("RESIDENCY_DESTROY") | k ("PRESSURE_SAMPLED"), 2, 64, model.source, null, gauge.source);
		call (e, "setCount", N);
		call (e, "setSampleEvery", 1);
		call (e, "setViewport", 0, 10, 0);
		call (e, "getText", 500, 0);
		assertEquals (1000, i (e, "stat", k ("STAT_PERMILLE")));
		gauge.permille = -1;
		call (e, "getText", 501, 0);
		assertEquals (0, i (e, "stat", k ("STAT_PERMILLE")));
		assertTrue (i (e, "stat", k ("STAT_TELEMETRY_FAILURES")) >= 1);
		assertEquals (10, i (e, "stat", k ("STAT_BUDGET")), "budget falls to the window");
		assertEquals ("row 501", call (e, "getText", 501, 0), "reads still work at zero residency");
	}

	@Test
	public void callerOwnedNeverDiscardedWithoutSpill () {
		Object e = engine (k ("RESIDENCY_DESTROY"), 1, 8, null, null, null);
		call (e, "setCount", 1000);
		for (int item = 0; item < 100; item++) call (e, "setText", item, 0, "mine " + item);
		call (e, "setViewport", 900, 10, 0);
		call (e, "applyPressure", 0);
		for (int item = 0; item < 100; item++) {
			assertEquals ("mine " + item, call (e, "getText", item, 0));
			assertEquals (k ("TIER_WARM"), i (e, "tier", item));
		}
		assertEquals (0, i (e, "stat", k ("STAT_DESTROYED")));
		assertTrue (i (e, "stat", k ("STAT_UNEVICTABLE")) > 0);
	}

	@Test
	public void dataHandleIsNeverDestroyed () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setViewport", 0, 20, 5);
		Object mine = new Object ();
		call (e, "setData", 3, mine);
		call (e, "setKeyedData", 3, "x", mine);
		scrollAwayUnderPressure (e);
		assertEquals (k ("TIER_DESTROYED"), i (e, "tier", 3));
		assertSame (mine, call (e, "getData", 3));
		assertSame (mine, call (e, "getKeyedData", 3, "x"));
		call (e, "insert", 0);
		assertSame (mine, call (e, "getData", 4), "data follows its item");
		call (e, "setKeyedData", 4, "x", null);
		assertNull (call (e, "getKeyedData", 4, "x"));
	}

	@Test
	public void editorStateSurvivesScroll () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setViewport", 0, 20, 5);
		call (e, "openEditor", 7, 1, "half typed", 4, 2, 6);
		scrollAwayUnderPressure (e);
		call (e, "setViewport", 0, 20, 5);
		assertEquals (7, i (e, "editorItem"));
		assertEquals (1, i (e, "editorColumn"));
		assertEquals ("half typed", call (e, "editorText"));
		assertEquals (4, i (e, "editorCaret"));
		assertEquals (2, i (e, "editorSelectionStart"));
		assertEquals (6, i (e, "editorSelectionEnd"));
		call (e, "insert", 0);
		assertEquals (8, i (e, "editorItem"));
		call (e, "remove", 8);
		assertEquals (-1, i (e, "editorItem"));
	}

	@Test
	public void pinnedIdentitySurvivesEvictionAndShift () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		List<int []> created = new ArrayList<> ();
		call (e, "setHandleFactory", handleFactory (created));
		call (e, "setViewport", 40, 20, 5);
		int [] h = (int []) call (e, "handle", 50);
		scrollAwayUnderPressure (e);
		assertEquals (k ("TIER_DESTROYED"), i (e, "tier", 50));
		assertSame (h, call (e, "handle", 50));
		call (e, "insert", 10);
		assertEquals (51, h [0]);
		assertSame (h, call (e, "handle", 51));
		assertTrue (b (e, "isCurrent", h, 51, 0));
		call (e, "remove", 51);
		assertEquals (-1, h [0]);
		assertEquals (1, created.size ());
	}

	@Test
	public void recyclableHandlesOnlyAtBehaviourOnlySites () {
		int behaviour = (Integer) callStatic (ENGINE, "siteKey", "a/Caller", "paint", "()V", 12);
		int retains = (Integer) callStatic (ENGINE, "siteKey", "a/Caller", "remember", "()V", 3);
		int lo = Math.min (behaviour, retains), hi = Math.max (behaviour, retains);
		int [] packed = {0x4D334952, 1, 2, lo, lo == behaviour ? 1 : 2, hi, hi == behaviour ? 1 : 2};
		Object sidecar = callStatic (type ("ItemResidency$Sidecar"), "fromInts", (Object) packed);
		Model model = new Model (N, false);
		Object e = engine (k ("IDENTITY_BY_METADATA"), 2, 64, model.source, null, null);
		call (e, "setCount", N);
		call (e, "setHandleFactory", handleFactory (new ArrayList<> ()));
		call (e, "setSiteMetadata", sidecar);
		int [] t = (int []) call (e, "transientHandle", 5, behaviour);
		assertFalse (b (e, "bit", k ("LANE_PINNED"), 5), "behaviour-only site does not pin");
		assertTrue (b (e, "isCurrent", t, 5, t [1]));
		for (int j = 0; j < 4; j++) call (e, "transientHandle", 6 + j, behaviour);
		assertFalse (b (e, "isCurrent", t, 5, 1), "flyweight was rebound");
		Object pinned = call (e, "transientHandle", 6, retains);
		assertSame (pinned, call (e, "handle", 6));
		assertSame (call (e, "handle", 7), call (e, "transientHandle", 7, 12345), "unknown site pins");
		assertSame (pinned, call (e, "transientHandle", 6, behaviour), "pinned item keeps its handle");
		Object safe = engine (k ("IDENTITY_BY_METADATA") | k ("IDENTITY_PIN_ALL"), 2, 64, model.source, null, null);
		call (safe, "setCount", N);
		call (safe, "setHandleFactory", handleFactory (new ArrayList<> ()));
		call (safe, "setSiteMetadata", sidecar);
		assertSame (call (safe, "handle", 5), call (safe, "transientHandle", 5, behaviour));
	}

	@Test
	public void siteKeyIsFnv1a32OfOwnerNameDescAndBci () {
		String s = "org/eclipse/swt/examples/X.paint(Lorg/eclipse/swt/events/PaintEvent;)V@17";
		int h = 0x811C9DC5;
		for (int j = 0; j < s.length (); j++) {
			h ^= s.charAt (j);
			h *= 16777619;
		}
		assertEquals (h, (int) (Integer) callStatic (ENGINE, "siteKey", "org/eclipse/swt/examples/X", "paint",
				"(Lorg/eclipse/swt/events/PaintEvent;)V", 17));
	}

	@Test
	public void insertRemoveShiftLanesAndPayloadLikeAReferenceModel () {
		int n = 9000;
		Object e = engine (k ("RESIDENCY_DESTROY"), 1, 1 << 20, null, null, null);
		call (e, "setCount", n);
		List<Integer> checked = new ArrayList<> ();
		List<String> text = new ArrayList<> ();
		for (int item = 0; item < n; item++) {
			checked.add (item % 3 == 0 ? 1 : 0);
			text.add (item % 5 == 0 ? "t" + item : null);
			if (item % 3 == 0) call (e, "setBit", k ("LANE_CHECKED"), item, true);
			if (item % 5 == 0) call (e, "setText", item, 0, "t" + item);
		}
		Random random = new Random (5);
		for (int op = 0; op < 1500; op++) {
			int size = checked.size ();
			int at = op % 7 == 0 ? (op % 2 == 0 ? 31 : 4096) : random.nextInt (size);
			if (random.nextBoolean ()) {
				call (e, "insert", at);
				checked.add (at, 0);
				text.add (at, null);
			} else {
				call (e, "remove", at);
				checked.remove (at);
				text.remove (at);
			}
		}
		assertEquals (checked.size (), i (e, "count"));
		for (int item = 0; item < checked.size (); item++) {
			assertEquals (checked.get (item) == 1, b (e, "bit", k ("LANE_CHECKED"), item), "checked " + item);
			assertEquals (text.get (item), call (e, "getText", item, 0), "text " + item);
		}
	}

	@Test
	public void fileSwapStoreRecyclesBlocksAndNeverRevivesStaleIds () {
		Object spill = spill ();
		Object store = field (spill, "store");
		long r1 = (Long) call (store, "reserve", 100);
		byte [] a = new byte [100];
		Arrays.fill (a, (byte) 7);
		call (store, "write", Map.of (r1, a), null);
		assertArrayEquals (a, (byte []) call (store, "read", r1));
		call (store, "write", Map.of (r1, a), null);
		assertThrows (RuntimeException.class, () -> call (store, "write", Map.of (r1, new byte [100]), null), "conflicting bytes refused");
		call (store, "release", r1);
		assertNull (call (store, "read", r1), "released id reads absent");
		long r2 = (Long) call (store, "reserve", 90);
		assertNotEquals (r1, r2, "unique id even when the block is recycled");
		assertEquals (r1 & 0xFFFFFFFFL, r2 & 0xFFFFFFFFL, "same block class reused");
		assertNull (call (store, "read", r1));
		assertEquals ("java", call (store, "opsId"), "fail-closed Java page ops without a native candidate");
		call (spill, "close");
	}

	@Test
	public void paintPathNeverMaterializes () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setHandleFactory", handleFactory (new ArrayList<> ()));
		call (e, "setViewport", 0, 20, 5);
		call (e, "beginPaint");
		StringBuilder painted = new StringBuilder ();
		for (int row = 0; row < 25; row++) {
			int slot = i (e, "paintSlot", row);
			assertTrue (slot >= 0, "window rows are resident before paint");
			painted.append (call (e, "slotText", slot, 0)).append (i (e, "slotInt", slot, k ("I_FOREGROUND")))
					.append (i (e, "slotCellInt", slot, 1, k ("C_FONT"))).append (i (e, "paintLanes", row));
		}
		assertEquals (-1, i (e, "paintSlot", 9000), "paint path never pages in");
		int [] masks = new int [25], slotsOut = new int [25];
		assertEquals (25, i (e, "paintLanes", 0, 25, masks));
		assertEquals (25, i (e, "paintSlots", 0, 25, slotsOut));
		for (int row = 0; row < 25; row++) {
			assertEquals (i (e, "paintLanes", row), masks [row], "bulk lane read " + row);
			assertEquals (i (e, "paintSlot", row), slotsOut [row], "bulk slot read " + row);
		}
		call (e, "endPaint");
		assertEquals (0, i (e, "materializedDuringPaint"));
		assertTrue (painted.indexOf ("row 24") >= 0);
		call (e, "beginPaint");
		call (e, "getText", 9000, 0);
		call (e, "handle", 3);
		call (e, "endPaint");
		assertEquals (2, i (e, "stat", k ("STAT_MATERIALIZED_DURING_PAINT")), "public getters during paint are detected");
	}

	@Test
	public void residencyPinsBlockEvictionAndShift () {
		Model model = new Model (N, false);
		Object e = destroyEngine (model);
		call (e, "setViewport", 0, 20, 5);
		call (e, "pinResidency", 3, 5);
		scrollAwayUnderPressure (e);
		assertEquals (k ("TIER_WARM"), i (e, "tier", 3), "pinned rows stay resident at zero pressure");
		assertEquals (k ("TIER_WARM"), i (e, "tier", 4));
		assertEquals (k ("TIER_DESTROYED"), i (e, "tier", 5));
		assertEquals (1, model.requests [3], "a pinned row never re-fires SetData");
		call (e, "insert", 0);
		assertTrue (b (e, "isResidencyPinned", 4), "pins follow their items on insert");
		assertFalse (b (e, "isResidencyPinned", 3));
		call (e, "remove", 0);
		assertTrue (b (e, "isResidencyPinned", 3), "and on remove");
		call (e, "unpinResidency", 3, 5);
		assertEquals (k ("TIER_DESTROYED"), i (e, "tier", 3), "unpin trims back to the budget");
	}
}
