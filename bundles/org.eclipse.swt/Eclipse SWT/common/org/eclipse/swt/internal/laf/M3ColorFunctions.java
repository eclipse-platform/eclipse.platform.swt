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
package org.eclipse.swt.internal.laf;

import java.util.*;
import java.util.function.*;

/**
 * The colour functions of FlatLaf's properties (docs/m3/LOOK_AND_FEEL.md, LF6, improvement 4) beyond the LF2 set, so
 * that FlatLaf themes import: <code>rgb</code>, <code>rgba</code>, <code>hsl</code>, <code>hsla</code>,
 * <code>saturate</code>, <code>desaturate</code>, <code>spin</code>, <code>tint</code>, <code>shade</code>,
 * <code>fadein</code>, <code>fadeout</code>. The semantics are FlatLaf's (<code>UIDefaultsLoader.parseColorFunctions</code>,
 * <code>ColorFunctions</code>, Apache-2.0, FormDev Software GmbH), which follow the Less colour functions; the code is
 * written for the LF2 loader (packed ARGB ints, HSL in degrees and percentage points).
 */
final class M3ColorFunctions {
	/** The function names handled here. */
	static final Set<String> NAMES = Set.of ("rgb", "rgba", "hsl", "hsla", "saturate", "desaturate", "spin", "tint", "shade", "fadein", "fadeout");

	private M3ColorFunctions () {
	}

	/**
	 * Applies a function.
	 *
	 * @param colour resolves an argument that must be a colour
	 * @return the ARGB result
	 */
	static int apply (String function, List<String> args, ToIntFunction<String> colour) {
		switch (function) {
			case "rgb":
				LookAndFeelLoader.expect (function, args, 3);
				return rgb (args, 255);
			case "rgba":
				LookAndFeelLoader.expect (function, args, 4);
				return rgb (args, alpha (args.get (3)));
			case "hsl":
				LookAndFeelLoader.expect (function, args, 3);
				return LookAndFeelLoader.argb (hsl (args), 255);
			case "hsla":
				LookAndFeelLoader.expect (function, args, 4);
				return LookAndFeelLoader.argb (hsl (args), Math.round (LookAndFeelLoader.clamp (LookAndFeelLoader.percent (args.get (3))) * 2.55f));
			case "tint":
			case "shade":
				LookAndFeelLoader.expect (function, args, 2);
				return mix (function.equals ("tint") ? 0xFFFFFFFF : 0xFF000000, colour.applyAsInt (args.get (0)), LookAndFeelLoader.clamp (LookAndFeelLoader.percent (args.get (1))) / 100f);
			case "fadein":
			case "fadeout": {
				LookAndFeelLoader.expect (function, args, 2);
				int argb = colour.applyAsInt (args.get (0));
				float alpha = LookAndFeelLoader.alpha (argb) / 2.55f + (function.equals ("fadein") ? 1 : -1) * LookAndFeelLoader.percent (args.get (1));
				return Math.round (LookAndFeelLoader.clamp (alpha) * 2.55f) << 24 | (argb & 0xFFFFFF);
			}
			default:
				return hslFunction (function, args, colour);
		}
	}

	/* saturate, desaturate, spin: one HSL component of a colour moved */
	private static int hslFunction (String function, List<String> args, ToIntFunction<String> colour) {
		LookAndFeelLoader.expect (function, args, 2);
		int argb = colour.applyAsInt (args.get (0));
		float [] hsl = LookAndFeelLoader.hsl (argb);
		switch (function) {
			case "saturate":
				hsl [1] = LookAndFeelLoader.clamp (hsl [1] + LookAndFeelLoader.percent (args.get (1)));
				break;
			case "desaturate":
				hsl [1] = LookAndFeelLoader.clamp (hsl [1] - LookAndFeelLoader.percent (args.get (1)));
				break;
			case "spin":
				hsl [0] = ((hsl [0] + LookAndFeelLoader.percent (args.get (1))) % 360 + 360) % 360;
				break;
			default:
				throw new IllegalArgumentException ("unknown function " + function);
		}
		return LookAndFeelLoader.argb (hsl, LookAndFeelLoader.alpha (argb));
	}

	private static int rgb (List<String> args, int alpha) {
		int r = channel (args.get (0)), g = channel (args.get (1)), b = channel (args.get (2));
		return alpha << 24 | r << 16 | g << 8 | b;
	}

	/* a channel 0-255, or a percentage of 255 */
	private static int channel (String text) {
		int value = text.trim ().endsWith ("%") ? Math.round (LookAndFeelLoader.clamp (LookAndFeelLoader.percent (text)) * 2.55f) : LookAndFeelLoader.integer (text);
		if (value < 0 || value > 255) throw new IllegalArgumentException ("not a channel 0-255: " + text);
		return value;
	}

	private static int alpha (String text) {
		return channel (text);
	}

	private static float [] hsl (List<String> args) {
		float hue = LookAndFeelLoader.percent (args.get (0));
		return new float [] {(hue % 360 + 360) % 360, LookAndFeelLoader.clamp (LookAndFeelLoader.percent (args.get (1))), LookAndFeelLoader.clamp (LookAndFeelLoader.percent (args.get (2)))};
	}

	/* weight of a, the rest of b, per channel (as LF2 mix) */
	private static int mix (int a, int b, float weight) {
		int result = 0;
		for (int shift = 0; shift < 32; shift += 8) {
			int channel = Math.round (((a >>> shift) & 0xFF) * weight + ((b >>> shift) & 0xFF) * (1 - weight));
			result |= (channel & 0xFF) << shift;
		}
		return result;
	}
}
