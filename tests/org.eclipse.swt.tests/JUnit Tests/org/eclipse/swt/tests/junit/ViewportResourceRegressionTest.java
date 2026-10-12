/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and Synexia contributors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy at https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.io.*;
import java.lang.management.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/** Isolated process budgets; never infer native memory from Java facade counts. */
public class ViewportResourceRegressionTest {

	private static final long MIB = 1024L * 1024;
	private static final String LOGICAL_MODEL = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	private static final int STEPS = 96;
	private static final int COLLAPSE_RESIDENT_ROWS = 11;
	private static volatile ByteBuffer retainedNativeControl;
	private static volatile long cpuControl;

	@BeforeAll
	static void resourceGateEnabled () {
		assumeTrue (Boolean.getBoolean ("swt.viewport.resourceRegression"), "Dedicated resource gate is opt-in");
		assumeTrue ("gtk".equals (SWT.getPlatform ()) && Files.isReadable (Path.of ("/proc/self/status")),
				"This gate measures Linux GTK3 process RSS");
		assertNotEquals ("1", System.getenv ("SWT_GTK4"), "Run this gate with GTK3");
	}

	@Test
	void coldMillionRowsStayWithinHeapNativeAndCpuBudgets () throws Exception {
		qualify ("cold", 1_000_000_000L);
	}

	@Test
	void repeatedPaintedScrollingStaysWithinMemoryAndCpuBudgets () throws Exception {
		qualify ("scroll", 8_000_000_000L);
	}

	@Test
	void collapseExpansionDoesNotAccumulateNativeResidency () throws Exception {
		qualify ("collapse", 8_000_000_000L);
	}

	@Test
	void logicalColdMillionRowsStayWithinBudgets () throws Exception {
		qualify ("logical-cold", 1_000_000_000L);
	}

	@Test
	void logicalDistantAccessDoesNotAllocateNativePrefix () throws Exception {
		qualify ("logical-far", 1_000_000_000L);
	}

	@Test
	void logicalDistantPaintedScrollingStaysWithinBudgets () throws Exception {
		qualify ("logical-scroll", 8_000_000_000L);
	}

	@Test
	void logicalCollapseExpansionStaysWithinBudgets () throws Exception {
		qualify ("logical-collapse", 8_000_000_000L);
	}

	@Test
	void legacyDistantPrefixIsDetectedByNativeViewBudget () throws Exception {
		Properties p = probe ("native-prefix-control", 0);
		assertEquals ("default", p.getProperty ("model"));
		assertTrue (number (p, "peakNativeViewRows") >= 4097, p.toString ());
		assertThrows (AssertionError.class, () -> nativeViewBudget (p));
	}

	@Test
	void touchedNativeAllocationIsDetectedIndependentlyOfJavaHeap () throws Exception {
		Properties p = probe ("native-control", 0);
		assertTrue (number (p, "rssDeltaBytes") > 24 * MIB, p.toString ());
		assertTrue (number (p, "heapDeltaBytes") < 8 * MIB, "Control must be native, not a Java byte array");
		assertThrows (AssertionError.class, () -> memoryBudget (p));
	}

	@Test
	void additionalCpuWorkIsDetectedIndependentlyOfElapsedTime () throws Exception {
		Properties p = probe ("cpu-control", 0);
		assertTrue (number (p, "cpuNanos") >= 2_000_000_000L, p.toString ());
		assertThrows (AssertionError.class, () -> cpuBudget (number (p, "cpuNanos"), 1_000_000_000L));
	}

	private static void qualify (String scenario, long cpuLimit) throws Exception {
		long [] cpu = new long [3];
		long [] stepCpu = new long [3];
		for (int trial = 0; trial < cpu.length; trial++) {
			Properties p = probe (scenario, trial);
			memoryBudget (p);
			nativeViewBudget (p);
			cpu [trial] = number (p, "cpuNanos");
			stepCpu [trial] = number (p, "maxStepCpuNanos");
			assertEquals (1_000_000, number (p, "logicalRows"));
			assertTrue (number (p, "residentRows") <= (scenario.endsWith ("collapse") ? COLLAPSE_RESIDENT_ROWS : 512), p.toString ());
			assertTrue (number (p, "setDataCalls") <= 512, "Work must follow the visited window: " + p);
			if (scenario.endsWith ("scroll") || scenario.endsWith ("collapse")) {
				assertTrue (number (p, "paintEvents") >= STEPS, "Every measured step must be painted: " + p);
			}
		}
		Arrays.sort (cpu);
		Arrays.sort (stepCpu);
		cpuBudget (cpu [1], cpuLimit);
		assertTrue (stepCpu [1] <= 150_000_000L, "Median maximum step CPU exceeded 150 ms: " + Arrays.toString (stepCpu));
	}

	private static void memoryBudget (Properties p) {
		assertTrue (number (p, "rssDeltaBytes") <= 24 * MIB, "Process RSS growth exceeded 24 MiB: " + p);
		assertTrue (number (p, "heapDeltaBytes") <= 8 * MIB, "Retained Java heap growth exceeded 8 MiB: " + p);
	}

	private static void nativeViewBudget (Properties p) {
		assertTrue (number (p, "peakNativeViewRows") <= 512,
				"Attached GTK model exceeded 512 resident rows: " + p);
	}

	private static void cpuBudget (long actual, long limit) {
		assertTrue (actual <= limit, "Process CPU budget exceeded: actual=" + actual + " limit=" + limit);
	}

	private static long number (Properties p, String key) {
		return Long.parseLong (Objects.requireNonNull (p.getProperty (key), key));
	}

	private static Properties probe (String scenario, int trial) throws Exception {
		Path output = Path.of ("target", "viewport-resources").toAbsolutePath ();
		Files.createDirectories (output);
		Path report = output.resolve (scenario + "-" + trial + ".properties");
		Path log = output.resolve (scenario + "-" + trial + ".log");
		Files.deleteIfExists (report);
		List<String> command = new ArrayList<> (List.of (
				Path.of (System.getProperty ("java.home"), "bin", "java").toString (),
				"-Xms64m", "-Xmx256m", "-XX:+UseSerialGC", "-ea"));
		String libraries = System.getProperty ("swt.library.path");
		if (libraries != null) command.add ("-Dswt.library.path=" + libraries);
		command.addAll (List.of ("-cp", System.getProperty ("surefire.test.class.path", System.getProperty ("java.class.path")),
				ViewportResourceRegressionTest.class.getName (), scenario, report.toString ()));
		Process child = new ProcessBuilder (command).redirectErrorStream (true).redirectOutput (log.toFile ()).start ();
		try {
			assertTrue (child.waitFor (60, TimeUnit.SECONDS), "Resource probe timed out; see " + log);
			assertEquals (0, child.exitValue (), "Resource probe failed; see " + log);
			Properties result = new Properties ();
			try (Reader reader = Files.newBufferedReader (report)) { result.load (reader); }
			return result;
		} finally {
			if (child.isAlive ()) { child.destroyForcibly (); child.waitFor (10, TimeUnit.SECONDS); }
		}
	}

	private static long processCpu () {
		long nanos = ManagementFactory.getPlatformMXBean (
				com.sun.management.OperatingSystemMXBean.class).getProcessCpuTime ();
		if (nanos < 0) throw new IllegalStateException ("Process CPU counter is required");
		return nanos;
	}

	private static long rss () throws IOException {
		for (String line : Files.readAllLines (Path.of ("/proc/self/status"))) {
			if (line.startsWith ("VmRSS:")) return Long.parseLong (line.trim ().split ("\\s+") [1]) * 1024;
		}
		throw new IllegalStateException ("Process RSS counter is required");
	}

	private static long heap () {
		return ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ().getUsed ();
	}


	private static void alignCollapseViewport (Shell shell, Tree tree, Display display) {
		int rowExtent = Math.max (1, tree.getItemHeight ());
		int targetBody = Math.multiplyExact (COLLAPSE_RESIDENT_ROWS, rowExtent);
		for (int attempt = 0; attempt < 4; attempt++) {
			Rectangle client = tree.getClientArea ();
			int chrome = tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
			int body = Math.max (0, client.height - chrome);
			if (body == targetBody) return;
			Point shellSize = shell.getSize ();
			shell.setSize (shellSize.x, Math.max (1, shellSize.y + targetBody - body));
			settle (display);
		}
		Rectangle client = tree.getClientArea ();
		int chrome = tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
		int body = Math.max (0, client.height - chrome);
		require (body == targetBody,
				"Collapse resource viewport must equal exactly " + COLLAPSE_RESIDENT_ROWS
						+ " row extents: body=" + body + " rowExtent=" + rowExtent);
	}

	private static void settle (Display display) {
		long deadline = System.nanoTime () + TimeUnit.SECONDS.toNanos (5);
		while (display.readAndDispatch ()) {
			if (System.nanoTime () > deadline) throw new AssertionError ("UI event queue failed to quiesce");
		}
		display.update ();
	}

	private static long collectedHeap (Display display) {
		settle (display);
		System.gc ();
		System.gc ();
		return heap ();
	}

	private static void paint (Tree tree, TreeItem expectedTop, int [] paints) throws InterruptedException {
		int before = paints [0];
		tree.redraw ();
		long deadline = System.nanoTime () + TimeUnit.SECONDS.toNanos (2);
		do {
			settle (tree.getDisplay ());
			if (paints [0] > before && (expectedTop == null || tree.getTopItem () == expectedTop)) return;
			Thread.sleep (2); // GTK frame-clock work can arrive after the event queue becomes empty.
		} while (System.nanoTime () < deadline);
		throw new AssertionError ("Viewport did not paint/settle at the requested row within two seconds");
	}

	private static int resident (Tree tree, TreeItem parent) throws Exception {
		Method method = Tree.class.getDeclaredMethod ("virtualResidentChildCount", TreeItem.class);
		method.setAccessible (true);
		return ((Number)method.invoke (tree, parent)).intValue ();
	}

	/** Counts the model actually attached to GtkTreeView, independently of Java facade storage. */
	private static final class NativeViewProbe {
		private final Field handle;
		private final Method getModel, unref;
		private final MethodHandle childCount, nthChild, malloc, free;
		private final int iterSize;
		private int peak;

		NativeViewProbe () throws Exception {
			handle = Widget.class.getDeclaredField ("handle"); handle.setAccessible (true);
			Class<?> gtk = Class.forName ("org.eclipse.swt.internal.gtk.GTK");
			Class<?> os = Class.forName ("org.eclipse.swt.internal.gtk.OS");
			getModel = os.getMethod ("g_object_get", long.class, byte[].class, long[].class, long.class);
			unref = os.getMethod ("g_object_unref", long.class);
			childCount = MethodHandles.publicLookup ().unreflect (gtk.getMethod ("gtk_tree_model_iter_n_children", long.class, long.class));
			nthChild = MethodHandles.publicLookup ().unreflect (gtk.getMethod ("gtk_tree_model_iter_nth_child", long.class, long.class, long.class, int.class));
			malloc = MethodHandles.publicLookup ().unreflect (os.getMethod ("g_malloc", long.class));
			free = MethodHandles.publicLookup ().unreflect (os.getMethod ("g_free", long.class));
			iterSize = ((Number)gtk.getMethod ("GtkTreeIter_sizeof").invoke (null)).intValue ();
		}

		void sample (Tree tree) throws Exception {
			long [] model = {0};
			getModel.invoke (null, handle.getLong (tree), new byte [] {109, 111, 100, 101, 108, 0}, model, 0L);
			require (model [0] != 0, "GTK view must have an attached model");
			try { peak = Math.max (peak, rows (model [0], 0, 512)); }
			catch (Throwable failure) { throw new AssertionError ("Native view sampling failed", failure); }
			finally { unref.invoke (null, model [0]); }
		}

		private int rows (long model, long parent, int remaining) throws Throwable {
			int count = (int)childCount.invokeExact (model, parent);
			require (count >= 0, "Invalid GTK child count");
			// Stop traversal as soon as the bound fails; never enumerate a million-row control.
			if (count == 0 || count > remaining) return count;
			long iter = (long)malloc.invokeExact ((long)iterSize);
			require (iter != 0, "GTK iterator allocation failed");
			try {
				int total = count;
				for (int child = 0; child < count; child++) {
					require ((boolean)nthChild.invokeExact (model, iter, parent, child), "GTK child iterator missing");
					total += rows (model, iter, remaining - total);
					if (total > remaining) return total;
				}
				return total;
			} finally { free.invokeExact (iter); }
		}
	}

	private static void require (boolean condition, String message) {
		if (!condition) throw new AssertionError (message);
	}

	/** Standalone child entry point also supports an explicitly unqualified far-index diagnostic. */
	public static void main (String [] args) throws Exception {
		String scenario = args [0];
		boolean logical = scenario.startsWith ("logical-");
		System.clearProperty (LOGICAL_MODEL);
		if (logical) System.setProperty (LOGICAL_MODEL, "true");
		Display display = new Display ();
		try {
			// Warm class loading, GTK initialization and the measurement providers outside the sample.
			for (int warm = 0; warm < 4; warm++) {
				Shell shell = new Shell (display);
				Tree tree = new Tree (shell, SWT.VIRTUAL);
				tree.setItemCount (256);
				tree.getItem (0).setText ("warm");
				shell.dispose ();
			}
			processCpu (); rss (); heap ();
			Shell shell = new Shell (display);
			try {
				shell.setLayout (new FillLayout ());
				Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.MULTI | SWT.CHECK);
				int [] callbacks = {0};
				int [] paints = {0};
				tree.addListener (SWT.Paint, event -> paints [0]++);
				tree.addListener (SWT.SetData, event -> {
					callbacks [0]++;
					((TreeItem)event.item).setText ("row-" + event.index);
				});
				TreeColumn column = new TreeColumn (tree, SWT.NONE);
				column.setText ("Resource regression"); column.setWidth (400); tree.setHeaderVisible (true);
				tree.setItemCount (64);
				TreeItem first = tree.getItem (0);
				first.setText ("retained"); first.setChecked (true);
				TreeItem parent = null;
				TreeItem pinned = first;
				TreeItem [] positions = null;
				boolean scrolling = scenario.endsWith ("scroll"), collapsing = scenario.endsWith ("collapse");
				NativeViewProbe nativeView = new NativeViewProbe ();
				if (scrolling || collapsing) {
					if (collapsing) {
						tree.setItemCount (1); parent = first; parent.setItemCount (1_000_000);
						pinned = parent.getItem (10); pinned.setText ("retained"); pinned.setChecked (true);
					} else {
						tree.setItemCount (1_000_000);
						positions = logical
								? new TreeItem [] {tree.getItem (100_000), tree.getItem (900_000)}
								: new TreeItem [] {tree.getItem (32), tree.getItem (96)};
					}
					shell.setSize (480, 320); shell.open (); settle (display);
					if (collapsing) alignCollapseViewport (shell, tree, display);
					for (int warm = 0; warm < 16; warm++) {
						if (collapsing) { parent.setExpanded (true); paint (tree, null, paints); nativeView.sample (tree); parent.setExpanded (false); }
						else tree.setTopItem (positions [warm & 1]);
						paint (tree, scrolling ? positions [warm & 1] : null, paints);
					}
				}
				nativeView.sample (tree);
				long heapBefore = collectedHeap (display), rssBefore = rss (), cpuBefore = processCpu (), wallBefore = System.nanoTime ();
				long maxStepCpu = 0;
				int paintBefore = paints [0];
				if (scrolling || collapsing) {
					for (int step = 0; step < STEPS; step++) {
						long stepCpu = processCpu ();
						if (collapsing) { parent.setExpanded (true); paint (tree, null, paints); nativeView.sample (tree); parent.setExpanded (false); }
						else tree.setTopItem (positions [step & 1]);
						paint (tree, scrolling ? positions [step & 1] : null, paints);
						if (scrolling) require (tree.getTopItem () == positions [step & 1], "Scroll target changed");
						nativeView.sample (tree);
						maxStepCpu = Math.max (maxStepCpu, processCpu () - stepCpu);
					}
				} else {
					tree.setItemCount (1_000_000);
					if (scenario.equals ("far-diagnostic")) tree.getItem (100_000).setText ("far");
					if (scenario.equals ("native-prefix-control")) tree.getItem (4096).setText ("prefix control");
					if (scenario.equals ("logical-far")) {
						TreeItem near = tree.getItem (100_000), far = tree.getItem (900_000);
						near.setText ("near"); far.setText ("far");
						require (tree.getItem (100_000) == near && tree.getItem (900_000) == far, "Distant identity changed");
					}
					if (scenario.equals ("native-control")) {
						retainedNativeControl = ByteBuffer.allocateDirect (64 * 1024 * 1024);
						for (int page = 0; page < retainedNativeControl.capacity (); page += 4096) retainedNativeControl.put (page, (byte)1);
					}
					if (scenario.equals ("cpu-control")) {
						long until = processCpu () + 2_000_000_000L;
						while (processCpu () < until) for (int i = 0; i < 4096; i++) cpuControl = cpuControl * 31 + i;
					}
				}
				nativeView.sample (tree);
				long cpu = processCpu () - cpuBefore, elapsed = System.nanoTime () - wallBefore;
				long heapAfter = collectedHeap (display), rssAfter = rss ();
				require (pinned.getChecked () && pinned.getText ().equals ("retained"), "Pinned state changed");
				require (collapsing ? parent.getItem (10) == pinned : tree.getItem (0) == pinned, "Pinned identity changed");
				Properties result = new Properties ();
				result.setProperty ("schema", "swt-viewport-resources/2");
				result.setProperty ("scenario", scenario);
				result.setProperty ("java", System.getProperty ("java.runtime.version"));
				result.setProperty ("platform", SWT.getPlatform ());
				result.setProperty ("model", logical ? "explicit-logical-opt-in" : "default");
				result.setProperty ("scope", scenario.equals ("far-diagnostic") ? "unqualified-native-prefix-diagnostic" : logical ? "explicit-logical-cold-or-distant-window" : "bounded-cold-or-visited-window");
				result.setProperty ("steps", Integer.toString (scrolling || collapsing ? STEPS : 1));
				result.setProperty ("heapDeltaBytes", Long.toString (Math.max (0, heapAfter - heapBefore)));
				result.setProperty ("rssDeltaBytes", Long.toString (Math.max (0, rssAfter - rssBefore)));
				result.setProperty ("heapBeforeBytes", Long.toString (heapBefore));
				result.setProperty ("heapAfterBytes", Long.toString (heapAfter));
				result.setProperty ("rssBeforeBytes", Long.toString (rssBefore));
				result.setProperty ("rssAfterBytes", Long.toString (rssAfter));
				result.setProperty ("cpuNanos", Long.toString (cpu));
				result.setProperty ("maxStepCpuNanos", Long.toString (maxStepCpu));
				result.setProperty ("elapsedNanos", Long.toString (elapsed));
				result.setProperty ("setDataCalls", Integer.toString (callbacks [0]));
				result.setProperty ("paintEvents", Integer.toString (paints [0] - paintBefore));
				result.setProperty ("logicalRows", Integer.toString (parent == null ? tree.getItemCount () : parent.getItemCount ()));
				result.setProperty ("peakNativeViewRows", Integer.toString (nativeView.peak));
				result.setProperty ("residentRows", Integer.toString (resident (tree, parent)));
				try (Writer writer = Files.newBufferedWriter (Path.of (args [1]))) { result.store (writer, "Measured in an isolated warmed JVM; GC excluded from CPU sample"); }
			} finally { shell.dispose (); }
		} finally { display.dispose (); }
	}
}
