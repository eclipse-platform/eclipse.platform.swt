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

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;
import org.opentest4j.*;

/** Self-test of the shared {@link ViewportDifferential} harness on plain subjects; no display needed. */
public class Test_ViewportDifferentialHarness {
	/** A subject: a counter, optionally wrong from a given value on, with an image of its value. */
	static final class Counter {
		int value;
		final int breakAt;
		int captures;
		final boolean flickers;

		Counter (int breakAt, boolean flickers) {
			this.breakAt = breakAt;
			this.flickers = flickers;
		}

		String add (int amount) {
			value += amount;
			return "added " + amount;
		}

		String read () {
			return Integer.toString (value >= breakAt ? value + 1 : value);
		}

		ImageData image () {
			ImageData data = new ImageData (4, 4, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF));
			int pixel = (value >= breakAt ? value + 1 : value) & 0xFFFFFF;
			/* a flickering subject differs on its first capture only */
			if (flickers && captures++ == 0) pixel ^= 0x1;
			for (int y = 0; y < 4; y++) for (int x = 0; x < 4; x++) data.setPixel (x, y, pixel);
			return data;
		}
	}

	static ViewportDifferential<Counter> harness () {
		ViewportDifferential.Ops<Counter> ops = (random, log) -> {
			int amount = random.nextInt (10);
			log.append ("add ").append (amount).append ('\n');
			return counter -> counter.add (amount);
		};
		return ViewportDifferential.of ("counter", 42, ops).seeds (3).steps (20)
			.beforeSeed (counter -> counter.value = 0)
			.probe ("value", Counter::read)
			.pixels (Counter::image);
	}

	@TempDir
	Path temp;

	@BeforeEach
	public void evidenceToTemp () {
		System.setProperty ("swt.viewport.evidence", temp.toString ());
	}

	@AfterEach
	public void clearEvidence () {
		System.clearProperty ("swt.viewport.evidence");
	}

	@Test
	public void equalSubjectsPass () {
		harness ().compare (new Counter (Integer.MAX_VALUE, false), new Counter (Integer.MAX_VALUE, true));
	}

	@Test
	public void aDivergingStepFailsWithTheLog () {
		AssertionFailedError error = assertThrows (AssertionFailedError.class,
			() -> harness ().compare (new Counter (Integer.MAX_VALUE, false), new Counter (30, false)));
		String message = error.getMessage ();
		assertTrue (message.startsWith ("step "), message);
		assertTrue (message.contains ("| value 3"), message);
		assertTrue (message.contains ("log:\nseed 0\nadd "), message);
	}

	@Test
	public void differingPixelsAreSavedAsPng () {
		ViewportDifferential<Counter> pixelsOnly = ViewportDifferential.<Counter> of ("pixels", 7, (random, log) -> {
			log.append ("noop\n");
			return counter -> "";
		}).seeds (1).steps (1).pixels (Counter::image);
		Counter expected = new Counter (Integer.MAX_VALUE, false), actual = new Counter (0, false);
		AssertionFailedError error = assertThrows (AssertionFailedError.class, () -> pixelsOnly.compare (expected, actual));
		assertTrue (error.getMessage ().contains ("pixels after seed 0 differ"), error.getMessage ());
		assertTrue (Files.exists (temp.resolve ("pixels-seed0-expected.png")));
		assertTrue (Files.exists (temp.resolve ("pixels-seed0-actual.png")));
	}

	@Test
	public void recordingThenComparing () throws IOException {
		String directory = temp.resolve ("recording").toString ();
		harness ().compareWithRecording (new Counter (Integer.MAX_VALUE, false), true, directory);
		Path first = Paths.get (directory, "counter-seed0.txt");
		List<String> lines = Files.readAllLines (first, StandardCharsets.UTF_8);
		assertEquals (21, lines.size (), "20 steps and the pixel line");
		assertTrue (lines.get (20).startsWith ("pixels "));
		harness ().compareWithRecording (new Counter (Integer.MAX_VALUE, false), false, directory);
		AssertionFailedError error = assertThrows (AssertionFailedError.class,
			() -> harness ().compareWithRecording (new Counter (30, false), false, directory));
		assertTrue (error.getMessage ().contains ("stock:") && error.getMessage ().contains ("viewport:"), error.getMessage ());
	}

	@Test
	public void comparingWithoutARecordingSkips () {
		assertThrows (TestAbortedException.class,
			() -> harness ().compareWithRecording (new Counter (Integer.MAX_VALUE, false), false, temp.resolve ("none").toString ()));
		assertThrows (TestAbortedException.class, () -> harness ().compareWithRecording (new Counter (Integer.MAX_VALUE, false), false, null));
	}

	@Test
	public void lParamPacksTwoSignedWords () {
		assertEquals (0x0002_0001L, ViewportDifferential.makeLParam (1, 2));
		assertEquals (0xFFFF_FFFFL, ViewportDifferential.makeLParam (-1, -1));
	}

	@Test
	public void sameSeedsGiveTheSameScript () {
		List<String> first = new ArrayList<> (), second = new ArrayList<> ();
		for (List<String> out : List.of (first, second)) {
			Random random = new Random (42);
			StringBuilder log = new StringBuilder ();
			ViewportDifferential.Ops<Counter> ops = (r, l) -> {
				int amount = r.nextInt (10);
				l.append (amount).append (' ');
				return counter -> "";
			};
			for (int i = 0; i < 5; i++) ops.next (random, log);
			out.add (log.toString ());
		}
		assertEquals (first, second);
	}
}
