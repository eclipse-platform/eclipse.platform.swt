/*******************************************************************************
 * Copyright (c) 2026 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Demonstrates cropping and scaling of images using a GC
 *
 * The GC provides two relevant drawImage methods: one that scales an entire
 * image to a destination rectangle, and another that crops a source region and
 * scales it to the destination.
 *
 * This snippet allows experimenting with different source and destination
 * values for the second method. Within the method, the GC receives the
 * best-fitting image handle based on the current monitor zoom and image scaling
 * factor, minimizing native resampling.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet389 {

	private static final String IMAGE_100 = "workset_wiz150.png";
	private static final String IMAGE_200 = "workset_wiz300.png";
	private static final String IMAGES_ROOT = "resources/Snippet389/";

	private static final String IMAGE_PATH_100 = IMAGES_ROOT + IMAGE_100;
	private static final String IMAGE_PATH_200 = IMAGES_ROOT + IMAGE_200;

	static int srcX, srcY, srcW, srcH;
	static int dstX, dstY, dstW, dstH;

	public static void main(String[] args) {

		ImageFileNameProvider filenameProvider = zoom -> {
			switch (zoom) {
			case 100:
				return IMAGE_PATH_100;
			case 200:
				return IMAGE_PATH_200;
			default:
				return null;
			}
		};

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("GC#drawImage Interactive");
		shell.setLayout(new GridLayout(2, false));

		Image image = new Image(display, filenameProvider);
		Rectangle imgBounds = image.getBounds();

		final int fixedDstW = imgBounds.width * 2;
		final int fixedDstH = imgBounds.height * 2;
		srcX = imgBounds.width / 2;
		srcY = imgBounds.height / 2;
		srcW = imgBounds.width / 2;
		srcH = imgBounds.height / 2;

		dstX = 0;
		dstY = 0;
		dstW = imgBounds.width;
		dstH = imgBounds.height;

		Group controls = new Group(shell, SWT.NONE);
		controls.setText("GC#drawImage Arguments");
		controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		controls.setLayout(new GridLayout(2, false));

		Text tSrcX = field(controls, "srcX", srcX);
		Text tSrcY = field(controls, "srcY", srcY);
		Text tSrcW = field(controls, "srcWidth", srcW);
		Text tSrcH = field(controls, "srcHeight", srcH);

		Text tDstX = field(controls, "destX", dstX);
		Text tDstY = field(controls, "destY", dstY);
		Text tDstW = field(controls, "destWidth", dstW);
		Text tDstH = field(controls, "destHeight", dstH);

		Button apply = new Button(controls, SWT.PUSH);
		apply.setText("Apply");
		apply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Composite draw = new Composite(shell, SWT.NONE);
		draw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		draw.setLayout(new GridLayout(2, true));

		Canvas srcCanvas = new Canvas(draw, SWT.BORDER);
		srcCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		srcCanvas.addListener(SWT.Paint, e -> {
			GC gc = e.gc;
			Rectangle ca = srcCanvas.getClientArea();

			int ix = (ca.width - imgBounds.width) / 2;
			int iy = (ca.height - imgBounds.height) / 2;

			gc.drawImage(image, ix, iy);

			gc.setLineStyle(SWT.LINE_DASH);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(ix, iy, imgBounds.width, imgBounds.height);

			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.setLineWidth(2);
			gc.drawRectangle(ix + srcX, iy + srcY, srcW, srcH);
		});

		Canvas dstCanvas = new Canvas(draw, SWT.BORDER);
		dstCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dstCanvas.addListener(SWT.Paint, e -> {
			GC gc = e.gc;
			Rectangle ca = dstCanvas.getClientArea();

			int px = (ca.width - fixedDstW) / 2;
			int py = (ca.height - fixedDstH) / 2;

			gc.drawImage(image, srcX, srcY, srcW, srcH, px + dstX, py + dstY, dstW, dstH);

			gc.setLineStyle(SWT.LINE_DASH);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(px, py, fixedDstW, fixedDstH);

			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.setLineWidth(2);
			gc.drawRectangle(px + dstX, py + dstY, dstW, dstH);
		});

		Label srcLabel = new Label(draw, SWT.CENTER);
		srcLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		srcLabel.setText("150 x 150");

		Label dstLabel = new Label(draw, SWT.CENTER);
		dstLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		dstLabel.setText("300 x 300");

		apply.addListener(SWT.Selection, e -> {
			srcX = parse(tSrcX);
			srcY = parse(tSrcY);
			srcW = parse(tSrcW);
			srcH = parse(tSrcH);

			dstX = parse(tDstX);
			dstY = parse(tDstY);
			dstW = parse(tDstW);
			dstH = parse(tDstH);

			srcCanvas.redraw();
			dstCanvas.redraw();
		});

		shell.setSize(1000, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		image.dispose();
		display.dispose();
	}

	static Text field(Composite parent, String label, int value) {
		new Label(parent, SWT.NONE).setText(label);
		Text t = new Text(parent, SWT.BORDER);
		t.setText(String.valueOf(value));
		t.setLayoutData(new GridData(70, SWT.DEFAULT));
		return t;
	}

	static int parse(Text t) {
		try {
			return Integer.parseInt(t.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
