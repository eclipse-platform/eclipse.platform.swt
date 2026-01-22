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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Demonstrates cropping and scaling of images using a GC
 *
 * The GC provides three drawImage methods: one that draws an image at a target
 * destination, one that scales an entire image to a destination rectangle, and
 * another that crops a source region and scales it to the destination. The snippet
 * allows to select any source area of an image and draw it to a specific target
 * size. Additionally you can scale it additionally to the selected target size and
 * rotate the image.
 *
 * The snippet will draw the result with all three drawImage methods.
 * As not all methods will use all configuration option (e.g. only one methods supports
 * drawing a cropped part of an image), changing parameters does not always affect all
 * results.
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

	public static void main(String[] args) {
		Display display = new Display();
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
		Image image = new Image(display, filenameProvider);
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setText("GC#drawImage Interactive");

		new CropImageView(shell, image);
		shell.setSize(1000, 800);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		image.dispose();
		display.dispose();
	}

	private static class CropImageView extends Composite {

		private Image image;
		private int srcX, srcY, srcW, srcH;
		private int dstW, dstH;
		private float transformationScale;
		private int transformationRotation;
		private Text textSrcX, textSrcY, textSrcW, textSrcH;
		private Text textDstW, textDstH;
		private Text textScale;
		private Slider rotationSlider;
		private Canvas srcCanvas;
		private Canvas dstCanvas1;
		private Canvas dstCanvas2;
		private Canvas dstCanvas3;

		public CropImageView(Composite parent, Image image) {
			super(parent, SWT.None);
			this.image = image;
			init();
		}

		private void init() {
			setLayout(new GridLayout(2, false));

			Rectangle imgBounds = image.getBounds();
			srcX = imgBounds.width / 2;
			srcY = imgBounds.height / 2;
			srcW = imgBounds.width / 2;
			srcH = imgBounds.height / 2;

			dstW = imgBounds.width;
			dstH = imgBounds.height;

			transformationScale = 1;
			transformationRotation = 0;

			Group controls = new Group(this, SWT.NONE);
			controls.setText("GC#drawImage Arguments");
			controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
			controls.setLayout(new GridLayout(2, false));

			textSrcX = field(controls, "srcX", srcX);
			textSrcY = field(controls, "srcY", srcY);
			textSrcW = field(controls, "srcWidth", srcW);
			textSrcH = field(controls, "srcHeight", srcH);

			textDstW = field(controls, "destWidth", dstW);
			textDstH = field(controls, "destHeight", dstH);

			textScale = field(controls, "Scale Factor", transformationScale);
			rotationSlider = slider(controls, "Rotation", transformationRotation, 361);

			Button apply = new Button(controls, SWT.PUSH);
			apply.setText("Apply");
			apply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

			Composite draw = new Composite(this, SWT.NONE);
			draw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			draw.setLayout(new GridLayout(2, true));

			Group srcCanvasGroup = new Group(draw, SWT.NONE);
			srcCanvasGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			srcCanvasGroup.setLayout(new GridLayout(1, true));
			Label labelSrcCanvas = new Label(srcCanvasGroup, SWT.NONE);
			labelSrcCanvas.setText("Source Image");
			labelSrcCanvas.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			srcCanvas = new Canvas(srcCanvasGroup, SWT.NONE);
			srcCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			srcCanvas.addListener(SWT.Paint, e -> {
				try {
					GC gc = e.gc;
					Rectangle ca = srcCanvas.getClientArea();

					int ix = (ca.width - imgBounds.width) / 2;
					int iy = (ca.height - imgBounds.height) / 2;
					Transform translationTransform = new Transform(getDisplay());
					translationTransform.translate(ix, iy);
					gc.setTransform(translationTransform);
					gc.drawImage(image, 0, 0);

					gc.setLineStyle(SWT.LINE_DASH);
					gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
					gc.drawRectangle(0, 0, imgBounds.width, imgBounds.height);

					gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
					gc.setLineWidth(2);
					gc.drawRectangle(srcX, srcY, srcW, srcH);
				} catch (Exception ex) {
					MessageBox box = new MessageBox(getShell());
					box.setText("Invalid values");
					box.setMessage("There seem to be invalid value, the images cannot be drawn.");
					box.open();
				}
			});

			Group dstCanvas1Group = new Group(draw, SWT.NONE);
			dstCanvas1Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			dstCanvas1Group.setLayout(new GridLayout(1, true));
			Label labelDstCanvas1 = new Label(dstCanvas1Group, SWT.WRAP);
			labelDstCanvas1.setText("GC#drawImage(Image, int, int) - Scale Factor and Rotation (no crop)");
			labelDstCanvas1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			dstCanvas1 = initDestinationCanvas(dstCanvas1Group, 1, false);


			Group dstCanvas2Group = new Group(draw, SWT.NONE);
			dstCanvas2Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			dstCanvas2Group.setLayout(new GridLayout(1, true));
			Label labelDstCanvas2 = new Label(dstCanvas2Group, SWT.WRAP);
			labelDstCanvas2.setText("GC#drawImage(Image, int, int, int, int) - destWidth, destHeight, Scale Factor and Rotation (no crop)");
			labelDstCanvas2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			dstCanvas2 = initDestinationCanvas(dstCanvas2Group, 2, true);

			Group dstCanvas3Group = new Group(draw, SWT.NONE);
			dstCanvas3Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			dstCanvas3Group.setLayout(new GridLayout(1, true));
			Label labelDstCanvas3 = new Label(dstCanvas3Group, SWT.WRAP);
			labelDstCanvas3.setText("GC#drawImage(Image, int, int, int, int, int, int, int, int) - All");
			labelDstCanvas3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			dstCanvas3 = initDestinationCanvas(dstCanvas3Group, 3, true);

			apply.addListener(SWT.Selection, e -> refreshView());
		}

		private Canvas initDestinationCanvas(Composite parent, int drawingMode, boolean scalableAPI) {
			Canvas dstCanvas = new Canvas(parent, SWT.NONE);
			dstCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			dstCanvas.addListener(SWT.Paint, e -> {
				try {
					GC gc = e.gc;
					Rectangle ca = dstCanvas.getClientArea();
					Rectangle imgBounds = image.getBounds();
					switch (drawingMode) {
						case 1: {
							float imageBoundsX = imgBounds.width * transformationScale;
							float imageBoundsY = imgBounds.height * transformationScale;
							int px = Math.round((ca.width - imgBounds.width * transformationScale) / 2f);
							int py = Math.round((ca.height - imgBounds.height * transformationScale) / 2f);
							prepareTransform(gc, px, py, imageBoundsX, imageBoundsY);
							gc.drawImage(image, 0, 0);
							gc.setLineStyle(SWT.LINE_DASH);
							gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
							gc.drawRectangle(0, 0, imgBounds.width, imgBounds.height);
							break;
						}
						case 2: {
							float imageBoundsX = dstW * transformationScale;
							float imageBoundsY = dstH * transformationScale;
							int px = Math.round((ca.width - dstW * transformationScale) / 2f);
							int py = Math.round((ca.height - dstH * transformationScale) / 2f);
							prepareTransform(gc, px, py, imageBoundsX, imageBoundsY);
							gc.drawImage(image, 0, 0, dstW, dstH);
							gc.setLineStyle(SWT.LINE_DASH);
							gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
							gc.drawRectangle(0, 0, dstW, dstH);
							break;
						}
						case 3: {
							float imageBoundsX = dstW * transformationScale;
							float imageBoundsY = dstH * transformationScale;
							int px = Math.round((ca.width - dstW * transformationScale) / 2f);
							int py = Math.round((ca.height - dstH * transformationScale) / 2f);
							prepareTransform(gc, px, py, imageBoundsX, imageBoundsY);
							gc.drawImage(image, srcX, srcY, srcW, srcH, 0, 0, dstW, dstH);
							gc.setLineStyle(SWT.LINE_DASH);
							gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
							gc.drawRectangle(0, 0, dstW, dstH);
							gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
							gc.setLineWidth(1);
							gc.drawRectangle(0, 0, dstW, dstH);
							break;
						}
						default:
							throw new IllegalArgumentException("Unexpected value: " + drawingMode);
					}


				} catch (Exception ex) {
					MessageBox box = new MessageBox(getShell());
					box.setText("Invalid values");
					box.setMessage("There seem to be invalid value, the images cannot be drawn.");
					box.open();
				}
			});
			return dstCanvas;
		}

		private void prepareTransform(GC gc,  int offestX, int offsetY, float imageBoundsX, float imageBoundsY) {
			Transform translationTransform = new Transform(getDisplay());
			translationTransform.translate(offestX + imageBoundsX/2, offsetY + imageBoundsY/2);
			if (transformationScale > 1) {
				translationTransform.scale(transformationScale, transformationScale);
			}
			if (transformationRotation != 0) {
				translationTransform.rotate(transformationRotation);
			}
			translationTransform.translate(-imageBoundsX/2/transformationScale, -imageBoundsY/2/transformationScale);
			gc.setTransform(translationTransform);
		}

		private void refreshView() {
			srcX = parseInt(textSrcX);
			srcY = parseInt(textSrcY);
			srcW = parseInt(textSrcW);
			srcH = parseInt(textSrcH);

			dstW = parseInt(textDstW);
			dstH = parseInt(textDstH);

			transformationScale = parseFloat(textScale);
			transformationRotation = rotationSlider.getSelection();

			srcCanvas.redraw();
			dstCanvas1.redraw();
			dstCanvas2.redraw();
			dstCanvas3.redraw();
		}

		Text field(Composite parent, String label, Number value) {
			new Label(parent, SWT.NONE).setText(label);
			Text t = new Text(parent, SWT.BORDER);
			t.setText(value.toString());
			t.setLayoutData(new GridData(70, SWT.DEFAULT));
			return t;
		}

		Slider slider(Composite parent, String text, int value, int maxValue) {
			Label label = new Label(parent, SWT.NONE);
			label.setText(text);
			label.setLayoutData(new GridData());

			Slider slider = new Slider(parent, SWT.BORDER);
			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalSpan = 2;
			slider.setLayoutData(gd);
			slider.setValues(value, 0, maxValue, 1, 1, 1);
			slider.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					refreshView();
				}
			});
			return slider;
		}

		int parseInt(Text t) {
			try {
				return Integer.parseInt(t.getText());
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		float parseFloat(Text t) {
			try {
				return Float.parseFloat(t.getText());
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}
}
