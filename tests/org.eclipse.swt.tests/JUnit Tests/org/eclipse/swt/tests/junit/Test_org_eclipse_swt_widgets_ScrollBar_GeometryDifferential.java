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

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of the custom scroll bar geometry (leaf L3, docs/m3/SCROLLBAR_GEOMETRY.md): seeded
 * scripts set the values, the size and the enabled state of a Canvas's scroll bars; every step records the
 * values, the track and the thumb. A reference run (the official SWT jar, or <code>-Dswt.widget.mode=original</code>)
 * records the native thumb (<code>ScrollBar.getThumbBounds</code>); the viewport run computes the thumb with
 * <code>ViewportScrollBarGeometry</code> from the track and the values, which is what the drawn modes paint.
 * {@link #customMatchesNativeInOneRun} compares both in one JVM.
 */
public class Test_org_eclipse_swt_widgets_ScrollBar_GeometryDifferential {
	/* the native minimum thumb at 100 % (measured) */
	static final int MIN_THUMB = 8;
	Display display;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the geometry is measured on win32");
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
	}

	/** A Canvas with both scroll bars; <code>custom</code> reads the thumb from the computed geometry. */
	final class Scene {
		final Shell shell;
		final Canvas canvas;
		final boolean custom;

		Scene (int x, boolean custom) {
			this.custom = custom;
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (x, 40, 600, 600);
			canvas = new Canvas (shell, SWT.H_SCROLL | SWT.V_SCROLL);
			canvas.setBounds (0, 0, 300, 300);
			shell.open ();
			ViewportDifferential.drain (display);
		}

		String values () {
			return values (canvas.getVerticalBar ()) + " h " + values (canvas.getHorizontalBar ());
		}

		String values (ScrollBar bar) {
			return bar.getSelection () + "/" + bar.getMinimum () + ".." + bar.getMaximum () + " t" + bar.getThumb () + " " + bar.getEnabled ();
		}

		String track () {
			return canvas.getVerticalBar ().getThumbTrackBounds () + " h " + canvas.getHorizontalBar ().getThumbTrackBounds ();
		}

		String thumb () {
			return thumb (canvas.getVerticalBar (), true) + " h " + thumb (canvas.getHorizontalBar (), false);
		}

		/* thumb start and length along the bar, or "hidden" when the track is shorter than the minimum thumb */
		String thumb (ScrollBar bar, boolean vertical) {
			Rectangle track = bar.getThumbTrackBounds ();
			int trackStart = vertical ? track.y : track.x, trackLength = vertical ? track.height : track.width;
			if (trackLength < MIN_THUMB) return "hidden";
			if (!custom) {
				Rectangle thumb = bar.getThumbBounds ();
				return (vertical ? thumb.y : thumb.x) + "+" + (vertical ? thumb.height : thumb.width);
			}
			int length = call ("thumbLength", trackLength, MIN_THUMB, bar.getMinimum (), bar.getMaximum (), bar.getThumb ());
			int start = call ("thumbStart", trackLength, MIN_THUMB, bar.getMinimum (), bar.getMaximum (), bar.getThumb (), bar.getSelection ());
			return (trackStart + start) + "+" + length;
		}

		void dispose () {
			shell.dispose ();
		}
	}

	static int call (String name, int... args) {
		try {
			Class<?> geometry = Class.forName ("org.eclipse.swt.widgets.ViewportScrollBarGeometry");
			Class<?> [] types = new Class<?> [args.length];
			Object [] values = new Object [args.length];
			for (int i = 0; i < args.length; i++) {
				types [i] = int.class;
				values [i] = args [i];
			}
			Method method = geometry.getDeclaredMethod (name, types);
			method.setAccessible (true);
			return (Integer) method.invoke (null, values);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static ViewportDifferential.Ops<Scene> ops () {
		return (random, log) -> {
			int kind = random.nextInt (6);
			boolean vertical = random.nextBoolean ();
			int minimum = random.nextInt (4) == 0 ? random.nextInt (50) : 0;
			int maximum = minimum + 1 + random.nextInt (random.nextBoolean () ? 100 : 100000);
			int thumb = 1 + random.nextInt (Math.max (1, maximum - minimum));
			int selection = minimum + random.nextInt (Math.max (1, maximum - minimum - thumb + 1));
			int size = 20 + random.nextInt (560), other = 20 + random.nextInt (560);
			String bar = vertical ? "v" : "h";
			log.append (switch (kind) {
				case 0, 1 -> bar + " setValues " + selection + " " + minimum + ".." + maximum + " t" + thumb;
				case 2 -> "resize " + size + "x" + other;
				case 3 -> bar + " toggle enabled";
				case 4 -> bar + " setSelection " + selection;
				default -> bar + " setThumb " + thumb;
			}).append ('\n');
			return scene -> {
				ScrollBar target = vertical ? scene.canvas.getVerticalBar () : scene.canvas.getHorizontalBar ();
				switch (kind) {
					case 0, 1 -> target.setValues (selection, minimum, maximum, thumb, 1, thumb);
					case 2 -> scene.canvas.setSize (size, other);
					case 3 -> target.setEnabled (!target.getEnabled ());
					case 4 -> target.setSelection (selection);
					default -> target.setThumb (thumb);
				}
				return "";
			};
		};
	}

	static ViewportDifferential<Scene> harness () {
		return ViewportDifferential.of ("ScrollBar-geometry", 0x5CB0L, ops ()).widget ("ScrollBar").seeds (8).steps (150)
			.afterStep (scene -> ViewportDifferential.drain (scene.shell.getDisplay ()))
			.probe ("values", Scene::values)
			.probe ("track", Scene::track)
			.probe ("thumb", Scene::thumb);
	}

	@Test
	public void recordedAgainstReference () throws IOException {
		boolean reference = ViewportDifferential.isReference ("ScrollBar");
		Scene scene = new Scene (40, !reference);
		try {
			harness ().compareWithRecording (scene, reference, System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}

	@Test
	public void customMatchesNativeInOneRun () {
		Assumptions.assumeFalse ("official".equals (ViewportDifferential.reference ("ScrollBar")), "the computed geometry is part of this SWT");
		Scene nativeBars = new Scene (40, false), customBars = new Scene (660, true);
		try {
			harness ().compare (nativeBars, customBars);
		} finally {
			nativeBars.dispose ();
			customBars.dispose ();
		}
	}
}
