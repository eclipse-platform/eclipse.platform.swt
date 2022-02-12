package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SnippetDrawAlpha {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(4, false));

		createImageLabels(display, shell, "/eclipse16.png");
		createImageLabels(display, shell, "/eclipse32.png");
		createImageLabels(display, shell, "/pause.gif");
		createImageLabels(display, shell, "/run.gif");
		createImageLabels(display, shell, "/warning.gif");
		createImageLabels(display, shell, "/transparent.png");

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createImageLabels(Display display, Shell shell, String fileName) {
		createImageLabel(display, shell, fileName, -1);
		createImageLabel(display, shell, fileName, 1);
		createImageLabel(display, shell, fileName, 1.5f);
		createImageLabel(display, shell, fileName, 2);
	}

	private static void createImageLabel(Display display, Shell shell, String fileName, float scaleFactor) {
		ImageLoader loader = new ImageLoader();
		loader.load(SnippetDrawAlpha.class.getResourceAsStream(fileName));
		ImageData data = loader.data[0];
		if (scaleFactor != -1) {
			data = autoScaleImageData(display, data, scaleFactor);
		}
		Image image = new Image(display, data);
		Label label = new Label(shell, SWT.NONE);
		label.setImage(image);
	}

	private static ImageData autoScaleImageData(Device device, final ImageData imageData, float scaleFactor) {
		int width = imageData.width;
		int height = imageData.height;
		int scaledWidth = Math.round(width * scaleFactor);
		int scaledHeight = Math.round(height * scaleFactor);

		Image original = new Image(device, imageData);

		/* Create a 24 bit image data with alpha channel */
		final ImageData resultData = new ImageData(scaledWidth, scaledHeight, 24,
				new PaletteData(0xFF, 0xFF00, 0xFF0000));
		resultData.alphaData = new byte[scaledWidth * scaledHeight];

		/*
		 * FIXME: something is wrong in SWT on win32. alphaData is apparently
		 * not used in GC#drawImage(..) below, and images end up fully
		 * transparent.
		 */
//		if ("win32".equals(SWT.getPlatform())) {
//			// This hack makes the images at least visible, but loses all transparency:
//			Arrays.fill (resultData.alphaData, (byte) 0xFF);
//			// Makes the wrong non-transparent background white instead of black:
//			Arrays.fill(resultData.data, (byte) 0xFF);
//		}

		Image resultImage = new Image(device, resultData);
		GC gc = new GC(resultImage);
		gc.setAntialias(SWT.ON);
		gc.drawImage(original, 0, 0, width, height,
				/*
				 * E.g. destWidth here is effectively DPIUtil.autoScaleDown
				 * (scaledWidth), but avoiding rounding errors. Nevertheless, we
				 * still have some rounding errors due to the point-based API
				 * GC#drawImage(..).
				 */
				0, 0, scaledWidth, scaledHeight);
		gc.dispose();
		original.dispose();
		ImageData result = resultImage.getImageData();
		resultImage.dispose();
		return result;
	}
}