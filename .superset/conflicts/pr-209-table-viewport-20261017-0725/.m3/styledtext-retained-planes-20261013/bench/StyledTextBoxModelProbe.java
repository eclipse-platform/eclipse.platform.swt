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
import java.security.MessageDigest;
import java.util.HexFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Box-model probe for the "inset problem" report: the flicker bench's widget (margins 9, 7, 11, 5 in
 * magenta, SWT.BORDER, both scroll bars, 200,000 lines scrolled near line 199,783) captured with
 * Control.print at the zoom of {@code -Dswt.autoScale}. Pixel analysis of the capture in device pixels:
 * per side the magenta band width (run of margin-colour pixels inward from the client edge, measured on
 * the middle row/column), off-colour pixels inside each band (text or line background overdraw), the
 * client rectangle and trim from the widget (points and device pixels), the scroll bar sizes, and the
 * gap between the margin ring and the scroll bar tracks (client edge to ring, device pixels).
 * Usage: {@code StyledTextBoxModelProbe <reportTsv> <label> <pngDir>}.
 */
public final class StyledTextBoxModelProbe {
	public static void main(String[] args) throws Exception {
		Path report = Path.of(args[0]);
		boolean screen = Boolean.getBoolean("probe.screen");
		String label = args[1] + (screen ? "-screen" : "-print");
		String zoomText = System.getProperty("swt.autoScale", "100");
		int zoom = Integer.parseInt(zoomText);
		Display display = new Display();
		Shell shell = new Shell(display, screen ? SWT.SHELL_TRIM | SWT.ON_TOP : SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		Color magenta = new Color(display, 250, 10, 200);
		text.setMarginColor(magenta);
		text.setMargins(9, 7, 11, 5);
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 200_000; i++) {
			b.append("line ").append(i).append(i % 9 == 0 ? " a longer line with more words to wrap and measure\n" : " text\n");
		}
		text.setText(b.toString());
		shell.setBounds(30, 30, 760, 600);
		shell.open();
		text.setTopIndex(199_783);
		text.setTopPixel(text.getTopPixel() + text.getLineHeight() / 3); // a partial top line too
		for (int i = 0; i < 50 && display.readAndDispatch(); i++) {
			// settle
		}
		Rectangle bounds = text.getBounds();
		Rectangle client = text.getClientArea();
		Rectangle trim = text.computeTrim(0, 0, 0, 0);
		ScrollBar v = text.getVerticalBar(), h = text.getHorizontalBar();
		Image image = new Image(display, bounds.width, bounds.height);
		GC gc = screen ? new GC(display) : new GC(image);
		if (screen) {
			shell.setActive();
			for (int i = 0; i < 200 && display.readAndDispatch(); i++) {
				// idle before the on-screen capture
			}
			org.eclipse.swt.graphics.Point outer = shell.toDisplay(bounds.x, bounds.y);
			gc.copyArea(image, outer.x, outer.y);
		} else {
			text.print(gc);
		}
		gc.dispose();
		ImageData data = image.getImageData(zoom);
		image.dispose();
		double s = zoom / 100.0;
		// client area in device pixels inside the capture: the border trim is the left/top inset
		int cx0 = (int) Math.floor(-trim.x * s), cy0 = (int) Math.floor(-trim.y * s);
		int cx1 = cx0 + (int) Math.ceil(client.width * s), cy1 = cy0 + (int) Math.ceil(client.height * s);
		org.eclipse.swt.graphics.RGB m = magenta.getRGB();
		int midY = (cy0 + cy1) / 2, midX = (cx0 + cx1) / 2;
		int left = run(data, cx0, midY, 1, 0, m), right = run(data, cx1 - 1, midY, -1, 0, m);
		int top = run(data, midX, cy0, 0, 1, m), bottom = run(data, midX, cy1 - 1, 0, -1, m);
		// off-colour pixels inside each band (band = configured margin in device pixels, floor start / ceil end)
		int bl = (int) Math.ceil(text.getLeftMargin() * s), br = (int) Math.ceil(text.getRightMargin() * s);
		int bt = (int) Math.ceil(text.getTopMargin() * s), bb = (int) Math.ceil(text.getBottomMargin() * s);
		int offLeft = 0, offRight = 0, offTop = 0, offBottom = 0;
		for (int y = cy0; y < cy1; y++) {
			for (int x = cx0; x < cx1; x++) {
				if (data.palette.getRGB(data.getPixel(x, y)).equals(m)) {
					continue;
				}
				if (x < cx0 + bl) offLeft++;
				else if (x >= cx1 - br) offRight++;
				else if (y < cy0 + bt) offTop++;
				else if (y >= cy1 - bb) offBottom++;
			}
		}
		// independent scan of the capture's middle row and column: non-ring lead (border), ring run,
		// ring run from the far side, non-ring tail (scroll bar track + border); flush = no other gap
		String rowScan = scan(data, data.height / 2, true, m), colScan = scan(data, data.width / 2, false, m);
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		sha.update(data.data);
		String hash = HexFormat.of().formatHex(sha.digest(), 0, 8);
		if (args.length > 2) {
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { data };
			loader.save(args[2] + "/boxmodel-" + label + "-" + zoom + ".png", SWT.IMAGE_PNG);
		}
		StringBuilder out = new StringBuilder();
		if (!Files.exists(report)) {
			out.append("label\tzoom\tcontent\tbounds_pt\tclient_pt\ttrim_pt\tvbar_pt\thbar_pt\tcapture_px\tclient_px\t"
					+ "band_left_px\tband_top_px\tband_right_px\tband_bottom_px\texpected_l_t_r_b_px\t"
					+ "offcolour_left\toffcolour_top\toffcolour_right\toffcolour_bottom\tring_gap_px\tsha256_16\trow_lead_ringL_ringR_tail_px\tcol_lead_ringT_ringB_tail_px\ttrim_px_l_t_r_b\n");
		}
		out.append(String.join("\t", label, zoomText, text.getContent().getClass().getSimpleName(), rect(bounds), rect(client), rect(trim),
				v.getSize().x + "x" + v.getSize().y, h.getSize().x + "x" + h.getSize().y, data.width + "x" + data.height,
				cx0 + "," + cy0 + "," + (cx1 - cx0) + "x" + (cy1 - cy0), Integer.toString(left), Integer.toString(top), Integer.toString(right),
				Integer.toString(bottom), bl + "," + bt + "," + br + "," + bb, Integer.toString(offLeft), Integer.toString(offTop),
				Integer.toString(offRight), Integer.toString(offBottom), gap(data, cx0, cy0, cx1, cy1, m, midX, midY), hash, rowScan, colScan,
				Math.round(-trim.x * s) + "," + Math.round(-trim.y * s) + "," + Math.round((trim.width + trim.x) * s) + "," + Math.round((trim.height + trim.y) * s)))
				.append('\n');
		Files.writeString(report, out.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		System.out.print(out);
		StringBuilder tree = new StringBuilder("control tree:\n");
		dump(shell, 0, tree);
		System.out.print(tree);
		magenta.dispose();
		shell.dispose();
		display.dispose();
	}

	/** lead,ringA,ringB,tail along row (or column) index {@code at}, in device pixels. */
	static String scan(ImageData d, int at, boolean row, org.eclipse.swt.graphics.RGB m) {
		int n = row ? d.width : d.height;
		int lead = 0;
		while (lead < n && !d.palette.getRGB(row ? d.getPixel(lead, at) : d.getPixel(at, lead)).equals(m)) lead++;
		int ringA = 0;
		while (lead + ringA < n && d.palette.getRGB(row ? d.getPixel(lead + ringA, at) : d.getPixel(at, lead + ringA)).equals(m)) ringA++;
		int tail = 0;
		while (tail < n && !d.palette.getRGB(row ? d.getPixel(n - 1 - tail, at) : d.getPixel(at, n - 1 - tail)).equals(m)) tail++;
		int ringB = 0;
		while (tail + ringB < n && d.palette.getRGB(row ? d.getPixel(n - 1 - tail - ringB, at) : d.getPixel(at, n - 1 - tail - ringB)).equals(m)) ringB++;
		return lead + "," + ringA + "," + ringB + "," + tail;
	}

	/** Run length of margin-colour pixels from (x, y) stepping (dx, dy). */
	static int run(ImageData d, int x, int y, int dx, int dy, org.eclipse.swt.graphics.RGB m) {
		int n = 0;
		while (x >= 0 && y >= 0 && x < d.width && y < d.height && d.palette.getRGB(d.getPixel(x, y)).equals(m)) {
			n++;
			x += dx;
			y += dy;
		}
		return n;
	}

	/** Pixels between each client edge and the first margin-colour pixel inward: l,t,r,b (0 = flush). */
	static String gap(ImageData d, int cx0, int cy0, int cx1, int cy1, org.eclipse.swt.graphics.RGB m, int midX, int midY) {
		int l = 0, t = 0, r = 0, b = 0;
		while (cx0 + l < cx1 && !d.palette.getRGB(d.getPixel(cx0 + l, midY)).equals(m)) l++;
		while (cy0 + t < cy1 && !d.palette.getRGB(d.getPixel(midX, cy0 + t)).equals(m)) t++;
		while (cx1 - 1 - r > cx0 && !d.palette.getRGB(d.getPixel(cx1 - 1 - r, midY)).equals(m)) r++;
		while (cy1 - 1 - b > cy0 && !d.palette.getRGB(d.getPixel(midX, cy1 - 1 - b)).equals(m)) b++;
		return l + "," + t + "," + r + "," + b;
	}

	/** Control tree with style bits relevant to scrolling and borders. */
	static void dump(org.eclipse.swt.widgets.Control c, int depth, StringBuilder out) {
		int style = c.getStyle();
		out.append("  ".repeat(depth)).append(c.getClass().getName()).append(" bounds=").append(rect(c.getBounds()))
				.append((style & SWT.V_SCROLL) != 0 ? " V_SCROLL" : "").append((style & SWT.H_SCROLL) != 0 ? " H_SCROLL" : "")
				.append((style & SWT.BORDER) != 0 ? " BORDER" : "").append((style & SWT.NO_FOCUS) != 0 ? " NO_FOCUS" : "").append('\n');
		if (c instanceof org.eclipse.swt.widgets.Composite composite) {
			for (org.eclipse.swt.widgets.Control child : composite.getChildren()) {
				dump(child, depth + 1, out);
			}
		}
	}

	static String rect(Rectangle r) {
		return r.x + "," + r.y + "," + r.width + "x" + r.height;
	}
}
