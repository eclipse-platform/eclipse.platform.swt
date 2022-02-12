package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SnippetDrawAlphaTwoPass {

	// Alpha and BW palette definitions from org.eclipse.ui.internal.decorators.DecorationImageBuilder
	private static final PaletteData ALPHA_PALETTE, BW_PALETTE;

	static {
		RGB[] rgbs = new RGB[256];
		for (int i = 0; i < rgbs.length; i++) {
			rgbs[i] = new RGB(i, i, i);
		}

		ALPHA_PALETTE = new PaletteData(rgbs);
		BW_PALETTE = new PaletteData(new RGB[] { new RGB(0, 0, 0), new RGB(255, 255, 255) });
	}

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

		ImageData imageMaskData = null;

		if (imageData.getTransparencyType() == SWT.TRANSPARENCY_ALPHA) {
			imageMaskData = new ImageData(width, height, 8, ALPHA_PALETTE, 1, imageData.alphaData);
		} else if (imageData.getTransparencyType() == SWT.TRANSPARENCY_PIXEL || imageData.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
			ImageData transparencyMaskData = imageData.getTransparencyMask();
			imageMaskData = new ImageData(width, height, 1, BW_PALETTE, transparencyMaskData.scanlinePad, transparencyMaskData.data);
		}

		Image original = new Image(device, imageData);
		Image originalMask = null;

		if (imageMaskData != null) {
			originalMask = new Image(device, imageMaskData);
		}

		/* Create a 24 bit image data with alpha channel */
		ImageData resultData = new ImageData(scaledWidth, scaledHeight, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
		ImageData resultMaskData = null;

		if (imageMaskData != null) {
			resultMaskData = new ImageData(scaledWidth, scaledHeight, imageMaskData.depth, imageMaskData.palette);
		}

		Image result = new Image(device, resultData);
		Image resultMask = null;

		GC gc = new GC(result);
		gc.setAntialias(SWT.ON);
		gc.drawImage(original, 0, 0, width, height, 0, 0, scaledWidth, scaledHeight);
		gc.dispose();

		if (resultMaskData != null) {
			resultMask = new Image(device, resultMaskData);
			gc = new GC(resultMask);
			gc.setAntialias(SWT.ON);
			gc.drawImage(originalMask, 0, 0, width, height, 0, 0, scaledWidth, scaledHeight);
			gc.dispose();
		}

		original.dispose();
		originalMask.dispose();

		ImageData scaledResult = result.getImageData();

		if (resultMask != null) {
			ImageData scaledResultMaskData = resultMask.getImageData();

			// Convert 1-bit mask
			if (scaledResultMaskData.depth == 1) {
				scaledResult.maskPad = scaledResultMaskData.scanlinePad;
				scaledResult.maskData = scaledResultMaskData.data;
			} else {
				byte[] alphaData = scaledResultMaskData.data;
				if (scaledResultMaskData.scanlinePad != 1) {
					alphaData = convertPad(scaledResultMaskData.data, scaledResultMaskData.width, scaledResultMaskData.height,
							scaledResultMaskData.depth, scaledResultMaskData.scanlinePad, 1);
				}
				scaledResult.alphaData = alphaData;
			}
		}

		result.dispose();
		resultMask.dispose();
		return scaledResult;
	}

	// copied from ImageData.convertPad as it is not accessible outside the org.eclipse.swt package
	private static byte[] convertPad(byte[] data, int width, int height, int depth, int pad, int newPad) {
		if (pad == newPad) return data;
		int stride = (width * depth + 7) / 8;
		int bpl = (stride + (pad - 1)) / pad * pad;
		int newBpl = (stride + (newPad - 1)) / newPad * newPad;
		byte[] newData = new byte[height * newBpl];
		int srcIndex = 0, destIndex = 0;
		for (int y = 0; y < height; y++) {
			System.arraycopy(data, srcIndex, newData, destIndex, stride);
			srcIndex += bpl;
			destIndex += newBpl;
		}
		return newData;
	}
}