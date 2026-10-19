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

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of StyledText in the viewport mode (leaf L4, docs/m3/STYLEDTEXT_WIDGET_MODE.md) against a
 * reference run (the official SWT jar, or <code>-Dswt.widget.mode=original</code>): seeded scripts of public API
 * operations on documents of 0 to 300 lines and of 70,000 lines (above the viewport-mode threshold of 65,536 lines;
 * fixed line height); every step records the answer, the widget state, the scroll bars, the events and the Paint
 * regions, every seed the rendered pixels.
 */
public class Test_org_eclipse_swt_custom_StyledText_ViewportDifferential {
	static final int LARGE = 70_000;
	/* win32 messages and scroll codes */
	static final int WM_VSCROLL = 0x115, SB_LINEDOWN = 1, SB_PAGEDOWN = 3;
	static final int [] ACTIONS = {ST.LINE_DOWN, ST.LINE_UP, ST.PAGE_DOWN, ST.PAGE_UP, ST.TEXT_END, ST.TEXT_START, ST.COLUMN_NEXT,
		ST.WORD_NEXT, ST.SELECT_LINE_DOWN, ST.SELECT_PAGE_DOWN, ST.LINE_END, ST.LINE_START};
	Display display;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
	}

	static String document (int lines, int seed) {
		StringBuilder text = new StringBuilder ();
		for (int i = 0; i < lines; i++) {
			if (i > 0) text.append ('\n');
			text.append ("line ").append (i).append (' ');
			for (int k = 0; k < (i * 7 + seed) % 37; k++) text.append ((char) ('a' + (i + k) % 26));
		}
		return text.toString ();
	}

	final class Scene {
		final Shell shell;
		final StyledText text;
		final StringBuilder events = new StringBuilder (), paints = new StringBuilder ();
		final Color red, yellow;

		Scene () {
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 420, 320);
			shell.setLayout (new FillLayout ());
			text = new StyledText (shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			red = new Color (200, 0, 0);
			yellow = new Color (255, 255, 160);
			text.addListener (SWT.Modify, e -> events.append ("Mod "));
			text.addListener (SWT.Selection, e -> events.append ("Sel:").append (e.x).append (',').append (e.y).append (' '));
			text.addCaretListener (e -> events.append ("Car:").append (e.caretOffset).append (' '));
			text.addExtendedModifyListener (e -> events.append ("XMod:").append (e.start).append ('+').append (e.length).append ('-').append (e.replacedText.length ()).append (' '));
			ViewportDifferential.listen (text.getVerticalBar (), events, "V", SWT.Selection);
			text.addListener (SWT.Paint, e -> paints.append (e.x).append (',').append (e.y).append (' ').append (e.width).append ('x').append (e.height).append (';'));
			shell.open ();
			flush ();
		}

		void flush () {
			ViewportDifferential.drain (display);
			text.update ();
			ViewportDifferential.drain (display);
		}

		String state () {
			int caret = text.getCaretOffset (), top = text.getTopIndex ();
			Point selection = text.getSelection ();
			return "chars " + text.getCharCount () + " lines " + text.getLineCount () + " top " + top + " topPixel " + text.getTopPixel ()
				+ " hPixel " + text.getHorizontalPixel () + " caret " + caret + " caretLine " + text.getLineAtOffset (caret)
				+ " sel " + selection.x + ".." + selection.y + " linePixel " + text.getLinePixel (Math.min (top + 3, text.getLineCount () - 1))
				+ " caretAt " + text.getLocationAtOffset (caret) + " lineHeight " + text.getLineHeight () + " client " + text.getClientArea ();
		}

		String bars () {
			return "v " + ViewportDifferential.bar (text.getVerticalBar ()) + " h " + ViewportDifferential.bar (text.getHorizontalBar ());
		}

		void dispose () {
			shell.dispose ();
			red.dispose ();
			yellow.dispose ();
		}
	}

	static ViewportDifferential.Ops<Scene> ops () {
		return (random, log) -> {
			int kind = random.nextInt (19);
			if (kind == 1 && random.nextInt (4) != 0) kind = 0;
			final int op = kind;
			int a = random.nextInt (1 << 20), b = random.nextInt (1 << 20), c = random.nextInt (300), seed = random.nextInt (1000);
			log.append (switch (op) {
				case 0 -> "setText " + c + " lines";
				case 1 -> "setText " + LARGE + " lines";
				case 2 -> "replaceTextRange";
				case 3 -> "append";
				case 4 -> "setSelection";
				case 5 -> "setCaretOffset";
				case 6 -> "setTopIndex";
				case 7 -> "setTopPixel";
				case 8 -> "setHorizontalPixel";
				case 9, 10 -> "invokeAction " + ACTIONS [a % ACTIONS.length];
				case 11 -> "toggle word wrap (small documents)";
				case 12 -> "setStyleRange";
				case 13 -> "setLineBackground";
				case 14 -> "resize";
				case 15 -> "setMargins";
				case 16 -> "vbar " + (a % 2 == 0 ? "line" : "page") + " down";
				case 17 -> "setLineSpacing " + (a % 4);
				default -> "getTextRange";
			}).append ('\n');
			return scene -> {
				StyledText text = scene.text;
				int chars = text.getCharCount (), lines = text.getLineCount ();
				switch (op) {
					case 0 -> text.setText (document (c, seed));
					case 1 -> text.setText (document (LARGE, seed));
					case 2 -> {
						int start = chars == 0 ? 0 : a % (chars + 1), length = chars - start == 0 ? 0 : b % Math.min (40, chars - start + 1);
						text.replaceTextRange (start, length, "x" + c + "\ny");
					}
					case 3 -> text.append ("\ntail " + c);
					case 4 -> text.setSelection (chars == 0 ? 0 : a % (chars + 1), chars == 0 ? 0 : b % (chars + 1));
					case 5 -> text.setCaretOffset (chars == 0 ? 0 : a % (chars + 1));
					case 6 -> text.setTopIndex (a % Math.max (1, lines));
					case 7 -> text.setTopPixel (a % Math.max (1, lines * text.getLineHeight ()));
					case 8 -> text.setHorizontalPixel (a % 300);
					case 9, 10 -> text.invokeAction (ACTIONS [a % ACTIONS.length]);
					case 11 -> {
						if (lines <= 1000) text.setWordWrap (!text.getWordWrap ());
					}
					case 12 -> {
						if (chars > 0) {
							int start = a % chars;
							text.setStyleRange (new StyleRange (start, Math.min (chars - start, 1 + b % 30), scene.red, null, SWT.BOLD));
						}
					}
					case 13 -> text.setLineBackground (a % Math.max (1, lines), 1, scene.yellow);
					case 14 -> scene.shell.setSize (300 + a % 200, 220 + b % 160);
					case 15 -> text.setMargins (a % 6, b % 6, c % 6, seed % 6);
					case 16 -> ViewportDifferential.send (text, WM_VSCROLL, a % 2 == 0 ? SB_LINEDOWN : SB_PAGEDOWN, 0);
					case 17 -> text.setLineSpacing (a % 4);
					default -> {
						int start = chars == 0 ? 0 : a % (chars + 1);
						return "range " + Integer.toHexString (text.getTextRange (start, Math.min (chars - start, b % 200)).hashCode ());
					}
				}
				return "";
			};
		};
	}

	@Test
	public void recordedAgainstReference () throws IOException {
		/* the official jar records; this SWT records only with -Dswt.viewport.record=true and otherwise compares (any mode) */
		String reference = ViewportDifferential.reference ("StyledText");
		boolean record = ViewportDifferential.records ("StyledText"), viewport = "viewport".equals (reference);
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "scroll messages are win32");
		Scene scene = new Scene ();
		try {
			ViewportDifferential.of ("StyledText", 0x57E7L, ops ()).widget ("StyledText").seeds (8).steps (120)
				.beforeStep (s -> {
					s.events.setLength (0);
					s.paints.setLength (0);
				})
				.afterStep (Scene::flush)
				.probe ("state", Scene::state)
				.probe ("bars", Scene::bars)
				.probe ("events", s -> s.events.toString ())
				/* the viewport repaints only damaged regions: within the reference's Paint regions, pixels identical */
				/* the viewport repaints only damaged regions (within the reference's); the original mode repaints like the official jar */
				.probe ("paint", s -> s.paints.toString (), viewport ? ViewportDifferential.REGIONS_WITHIN : String::equals)
				.pixels (s -> {
					ViewportDifferential.settle (display, 100);
					return ViewportDifferential.render (s.text);
				})
				.compareWithRecording (scene, record, System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}
}
