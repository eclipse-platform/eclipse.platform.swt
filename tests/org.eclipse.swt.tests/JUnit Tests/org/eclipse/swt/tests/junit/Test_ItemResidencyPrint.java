// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.security.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.residency.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Reproduction contract in pixels: a {@link Canvas} painted only through the engine's paint path
 * ({@code paintSlot}, {@code slot*}, {@code paintLanes}) is printed with {@link Control#print(GC)};
 * the rows are forced out of residency (scroll away, zero pressure: DESTROY and COLD), scrolled
 * back, printed again, and the pixel hashes must match with zero materialization during paint.
 */
public class Test_ItemResidencyPrint {
	private static final int ROWS = 25, ROW_HEIGHT = 20, COLUMNS = 3, WIDTH = 420;
	private int top;
	private Throwable paintFailure;

	/** Synchronous source: text per cell, a foreground colour and a cell font per row. */
	private static final ItemResidency.DataSource SOURCE = (e, from, to) -> {
		for (int item = from; item < to; item++) {
			e.beginData (item);
			for (int c = 0; c < COLUMNS; c++) e.setText (item, c, "r" + item + "c" + c);
			e.setInt (item, ItemResidency.I_FOREGROUND, 0xFF000000 | (item * 40503 & 0x7F7F7F));
			e.setCellInt (item, 1, ItemResidency.C_FONT, 1 + item % 2);
			e.endData (item);
		}
	};

	@Test
	public void printPixelHashIdenticalAfterRoundTrip () throws Exception {
		Display display = Display.getDefault ();
		Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		Rectangle area = display.getPrimaryMonitor ().getClientArea ();
		shell.setBounds (area.x, area.y, WIDTH, ROWS * ROW_HEIGHT);
		Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.setBounds (0, 0, WIDTH, ROWS * ROW_HEIGHT);
		ItemSwapSpill spill = ItemSwapSpill.temporary (1L << 20);
		ItemResidency e = new ItemResidency (ItemResidency.RESIDENCY_SWAP | ItemResidency.RESIDENCY_DESTROY, COLUMNS, 64, SOURCE, spill, null);
		Font [] fonts = {null, new Font (display, "Arial", 9, SWT.BOLD), new Font (display, "Arial", 9, SWT.ITALIC)};
		Map<Integer, Color> colors = new HashMap<> ();
		try {
			e.setCount (100_000);
			e.setViewport (0, ROWS, 5);
			setStates (e);
			canvas.addPaintListener (event -> paint (event.gc, e, fonts, colors, display));
			shell.open ();
			shell.moveAbove (null);
			flush (display);
			String before = printHash (canvas, display, "before");
			if (paintFailure != null) throw new AssertionError ("paint failed", paintFailure);
			top = 50_000;
			e.setViewport (top, ROWS, 5);
			e.applyPressure (0);
			canvas.redraw ();
			flush (display);
			assertNotEquals (before, printHash (canvas, display, "away"), "a different page paints differently");
			for (int item = 0; item < ROWS; item++) {
				int tier = e.tier (item);
				assertTrue (tier == ItemResidency.TIER_DESTROYED || tier == ItemResidency.TIER_COLD, "row " + item + " left residency");
			}
			top = 0;
			e.applyPressure (1000);
			e.setViewport (0, ROWS, 5);
			canvas.redraw ();
			flush (display);
			assertEquals (before, printHash (canvas, display, "after"), "Control.print pixels identical after DESTROY/COLD round trip");
			assertEquals (0, e.materializedDuringPaint (), "paint never materializes");
			assertTrue (e.stat (ItemResidency.STAT_SWAP_IN) > 0 && e.stat (ItemResidency.STAT_RECREATED) > 0);
		} finally {
			e.dispose ();
			for (Font f : fonts) if (f != null) f.dispose ();
			colors.values ().forEach (Color::dispose);
			shell.dispose ();
		}
	}

	/** Every paintable state: lanes, caller-owned colours, cell fonts and images, editor. */
	private static void setStates (ItemResidency e) {
		for (int item = 0; item < ROWS; item++) {
			e.setBit (ItemResidency.LANE_CHECKABLE, item, true);
			e.setBit (ItemResidency.LANE_CHECKED, item, item % 3 == 0);
			e.setBit (ItemResidency.LANE_GRAYED, item, item % 5 == 0);
			e.setBit (ItemResidency.LANE_SELECTED, item, item % 4 == 1);
			e.setBit (ItemResidency.LANE_FOCUSED, item, item == 7);
			e.setBit (ItemResidency.LANE_DISABLED, item, item == 11);
			e.setBit (ItemResidency.LANE_EXPANDED, item, item % 6 == 0);
			e.setBit (ItemResidency.LANE_HAS_CHILDREN, item, item % 2 == 0);
			if (item % 4 == 0) {
				e.setInt (item, ItemResidency.I_BACKGROUND, 0xFF000000 | (0xE0F0FF - item * 0x10203));
				e.setCellInt (item, 2, ItemResidency.C_IMAGE, 1 + item % 3);
				e.setText (item, 2, "edited " + item);
			}
		}
		e.openEditor (3, 1, "typing", 4, 1, 3);
	}

	private void paint (GC gc, ItemResidency e, Font [] fonts, Map<Integer, Color> colors, Display display) {
		e.beginPaint ();
		try {
			gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			gc.fillRectangle (0, 0, WIDTH, ROWS * ROW_HEIGHT);
			for (int r = 0; r < ROWS; r++) row (gc, e, top + r, r * ROW_HEIGHT, fonts, colors, display);
		} catch (RuntimeException | Error failure) {
			if (paintFailure == null) paintFailure = failure;
			throw failure;
		} finally {
			e.endPaint ();
		}
	}

	private static void row (GC gc, ItemResidency e, int item, int y, Font [] fonts, Map<Integer, Color> colors, Display display) {
		int slot = e.paintSlot (item), lanes = e.paintLanes (item);
		if (slot < 0) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_GRAY));
			gc.drawText ("...", 4, y + 2, true);
			return;
		}
		int bg = e.slotInt (slot, ItemResidency.I_BACKGROUND);
		if ((lanes & 1 << ItemResidency.LANE_SELECTED) != 0) bg = 0xFF3399FF;
		if (bg != 0) {
			gc.setBackground (color (colors, display, bg));
			gc.fillRectangle (0, y, WIDTH, ROW_HEIGHT);
		}
		if ((lanes & 1 << ItemResidency.LANE_CHECKABLE) != 0) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			gc.drawRectangle (4, y + 4, 11, 11);
			if ((lanes & 1 << ItemResidency.LANE_CHECKED) != 0) {
				gc.setBackground (display.getSystemColor ((lanes & 1 << ItemResidency.LANE_GRAYED) != 0 ? SWT.COLOR_GRAY : SWT.COLOR_BLACK));
				gc.fillRectangle (7, y + 7, 6, 6);
			}
		}
		if ((lanes & 1 << ItemResidency.LANE_HAS_CHILDREN) != 0) {
			gc.drawText ((lanes & 1 << ItemResidency.LANE_EXPANDED) != 0 ? "-" : "+", 20, y + 2, true);
		}
		int fg = (lanes & 1 << ItemResidency.LANE_DISABLED) != 0 ? 0xFF999999 : e.slotInt (slot, ItemResidency.I_FOREGROUND);
		for (int c = 0; c < COLUMNS; c++) {
			int x = 32 + c * 128;
			int font = e.slotCellInt (slot, c, ItemResidency.C_FONT), image = e.slotCellInt (slot, c, ItemResidency.C_IMAGE);
			gc.setFont (font > 0 ? fonts [font] : null);
			gc.setForeground (color (colors, display, fg));
			if (image > 0) {
				gc.setBackground (color (colors, display, 0xFF000000 | image * 0x3F2F1F));
				gc.fillRectangle (x, y + 4, 10, 10);
				x += 14;
			}
			String text = e.editorItem () == item && e.editorColumn () == c ? "[" + e.editorText () + "|" + e.editorCaret () + "]"
					: e.slotText (slot, c);
			if (text != null) gc.drawText (text, x, y + 2, true);
		}
		if ((lanes & 1 << ItemResidency.LANE_FOCUSED) != 0) gc.drawFocus (0, y, WIDTH, ROW_HEIGHT);
	}

	private static Color color (Map<Integer, Color> colors, Display display, int argb) {
		return colors.computeIfAbsent (argb, v -> new Color (display, v >>> 16 & 0xFF, v >>> 8 & 0xFF, v & 0xFF));
	}

	private static void flush (Display display) {
		for (int i = 0; i < 50 && display.readAndDispatch (); i++) {
			// drain pending paint events
		}
	}

	/** Prints the control, saves the PNG next to the temp dir for diagnosis, returns size and SHA-256. */
	private static String printHash (Control control, Display display, String name) throws NoSuchAlgorithmException {
		control.getShell ().moveAbove (null);
		flush (display);
		Point size = control.getSize ();
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		try {
			assertTrue (control.print (gc), "Control.print supported");
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		ImageLoader saver = new ImageLoader ();
		saver.data = new ImageData [] {data};
		saver.save (System.getProperty ("java.io.tmpdir") + "/item-residency-print-" + name + ".png", SWT.IMAGE_PNG);
		MessageDigest sha = MessageDigest.getInstance ("SHA-256");
		sha.update (data.data);
		return data.width + "x" + data.height + ":" + HexFormat.of ().formatHex (sha.digest ());
	}
}
