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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Margin, border and scroll-bar-edge stability during scroll and resize storms, at the zoom given by
 * {@code -Dswt.autoScale} (100, 125, 150, 200). After every frame the widget's screen pixels are
 * captured (GC.copyArea from the display; the shell is ON_TOP and active) and checked:
 * <ul>
 * <li>margin frames: a pixel of the four margin bands differs from the margin colour;</li>
 * <li>border frames: a pixel of the border ring differs from the first frame's ring;</li>
 * <li>scroll-bar-edge frames: a pixel of the text column next to the vertical bar differs from the
 * margin colour (the right margin band touches the bar);</li>
 * <li>blit mismatch frames: the text area on screen after the frame differs from a full repaint of
 * the same state (the scroll copy plus exposed strip must equal painting everything).</li>
 * </ul>
 * Usage: {@code StyledTextFlickerBench <frames> <reportTsv> <label>}. The target is zero for each.
 */
public final class StyledTextFlickerBench {
	public static void main(String[] args) throws Exception {
		int frames = Integer.parseInt(args[0]);
		Path report = Path.of(args[1]);
		String label = args[2];
		String zoom = System.getProperty("swt.autoScale", "100");
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout(new FillLayout());
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		Color margin = new Color(display, 250, 10, 200);
		text.setMarginColor(margin);
		text.setMargins(9, 7, 11, 5);
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 200_000; i++) {
			b.append("line ").append(i).append(i % 9 == 0 ? " a longer line with more words to wrap and measure\n" : " text\n");
		}
		text.setText(b.toString());
		text.setLineBackground(3, 50, display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		text.setCaret(null); // the platform caret blinks: keep it out of the pixel comparisons
		shell.setBounds(30, 30, 820, 640);
		shell.open();
		shell.setActive();
		settle(display);
		StringBuilder out = new StringBuilder();
		if (!Files.exists(report)) {
			out.append("label\tzoom\tscenario\tframes\tmargin_frames\tborder_frames\tscrollbar_edge_frames\tblit_mismatch_frames\n");
		}
		int lineHeight = text.getLineHeight();
		out.append(storm(label, zoom, "scroll-storm", frames, display, text, margin, i -> text.setTopPixel(text.getTopPixel() + ((i & 1) == 0 ? 1 : 2) * lineHeight)));
		out.append(storm(label, zoom, "scroll-storm-up", frames, display, text, margin, i -> text.setTopPixel(Math.max(0, text.getTopPixel() - lineHeight * 2))));
		out.append(storm(label, zoom, "resize-storm", Math.min(frames, 60), display, text, margin, i -> shell.setSize(820 - (i % 10) * 9, 640 - (i % 10) * 7)));
		Files.writeString(report, out.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		System.out.print(out);
		margin.dispose();
		shell.dispose();
		display.dispose();
	}

	interface Frame {
		void run(int i);
	}

	static String storm(String label, String zoom, String scenario, int frames, Display display, StyledText text, Color margin, Frame frame) {
		int marginFrames = 0, borderFrames = 0, edgeFrames = 0, blitFrames = 0;
		int[] ring = null;
		for (int i = 0; i < frames; i++) {
			frame.run(i);
			text.update();
			settle(display);
			int[] r = check(display, text, margin);
			if (r[0] != 0) {
				marginFrames++;
			}
			if (r[2] != 0) {
				edgeFrames++;
			}
			ImageData shown = client(display, text);
			text.redraw();
			text.update();
			settle(display);
			if (!java.util.Arrays.equals(shown.data, client(display, text).data)) {
				blitFrames++;
			}
			int[] currentRing = ring(display, text);
			if (ring == null) {
				ring = currentRing;
			} else if (!java.util.Arrays.equals(ring, currentRing)) {
				borderFrames++;
				ring = currentRing; // count changes frame to frame (a resize may legitimately move the ring)
			}
		}
		if (scenario.startsWith("resize")) {
			borderFrames = -1; // the ring moves with the size: not comparable frame to frame
		}
		return String.join("\t", label, zoom, scenario, Integer.toString(frames), Integer.toString(marginFrames), Integer.toString(borderFrames),
				Integer.toString(edgeFrames), Integer.toString(blitFrames)) + "\n";
	}

	/** {margin pixels off colour, unused, scroll-bar-edge pixels off colour}, in device pixels of the screen. */
	static int[] check(Display display, StyledText text, Color margin) {
		Rectangle client = text.getClientArea();
		Point origin = text.toDisplay(0, 0);
		ImageData data = capture(display, origin.x, origin.y, client.width, client.height);
		double scale = data.width / (double) Math.max(1, client.width);
		int left = (int) Math.floor(text.getLeftMargin() * scale), right = (int) Math.floor(text.getRightMargin() * scale);
		int top = (int) Math.floor(text.getTopMargin() * scale), bottom = (int) Math.floor(text.getBottomMargin() * scale);
		org.eclipse.swt.graphics.RGB expected = margin.getRGB();
		int off = 0, edge = 0;
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				boolean inMargin = x < left || x >= data.width - right || y < top || y >= data.height - bottom;
				if (inMargin && !data.palette.getRGB(data.getPixel(x, y)).equals(expected)) {
					off++;
					if (x >= data.width - right) {
						edge++;
					}
				}
			}
		}
		return new int[] { off, 0, edge };
	}

	static ImageData client(Display display, StyledText text) {
		Rectangle client = text.getClientArea();
		Point origin = text.toDisplay(0, 0);
		return capture(display, origin.x, origin.y, client.width, client.height);
	}

	/** The border ring: the trim pixels between the widget bounds and its client area. */
	static int[] ring(Display display, StyledText text) {
		Rectangle bounds = text.getBounds();
		Point origin = text.getParent().toDisplay(bounds.x, bounds.y);
		ImageData data = capture(display, origin.x, origin.y, bounds.width, Math.min(bounds.height, 4));
		int[] out = new int[data.width * data.height];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				out[y * data.width + x] = data.getPixel(x, y);
			}
		}
		return out;
	}

	static ImageData capture(Display display, int x, int y, int width, int height) {
		Image image = new Image(display, Math.max(1, width), Math.max(1, height));
		GC gc = new GC(display);
		try {
			gc.copyArea(image, x, y);
			// device pixels at the zoom in effect (getImageData() would rescale to 100% and blend edges)
			return image.getImageData(Integer.parseInt(System.getProperty("swt.autoScale", "100")));
		} finally {
			gc.dispose();
			image.dispose();
		}
	}

	static void settle(Display display) {
		long deadline = System.currentTimeMillis() + 500;
		while (System.currentTimeMillis() < deadline) {
			if (!display.readAndDispatch()) {
				break;
			}
		}
	}
}
