/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet384 {

	private static Display display = new Display();

	// colors and thresholds used in the current Win32/macOS algorithm
	private static final RGB GRAY_LOW = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB(); // == RGB(160,
																											// 160, 160)
																											// on
																											// Windows
	private static final RGB GRAY_HIGH = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB(); // == RGB(240,
																										// 240, 240) on
																										// Windows

	private static final int WIN32_COCOA_THRESHOLD = 98304; // == 3 * 2^15, this is the real threshold used in
															// Win32/macOS
	private static final int BACKGROUND_THRESHOLD = GRAY_HIGH.red * GRAY_HIGH.red + GRAY_HIGH.green + GRAY_HIGH.green
			+ GRAY_HIGH.blue * GRAY_HIGH.blue; // == on windows 240^2 * 3

	private static final int MAX_BRIGHTNESS = 256 * 256 * 3;

	// change these to adjust the sliders default values
	private static final int ADJUSTABLE_THRESHOLD_LOW_DEFAULT = WIN32_COCOA_THRESHOLD;
	private static final int ADJUSTABLE_THRESHOLD_HIGH_DEFAULT = BACKGROUND_THRESHOLD;
	private static final RGB ADDITIONAL_GRAY_MID_DEFAULT = new RGB(214, 214, 214); // arbitrarily chosen
	private static final int HSB_DISABLING_BRIGHTNESS_DEFAULT = 90;

	public static void main(String[] args) {

		Shell shell = new Shell(display);
		shell.setText("Disabled Icon Viewer"); //$NON-NLS-1$
		shell.setLayout(new GridLayout(1, false));

		DirectoryDialog directoryDialog = new DirectoryDialog(shell);
		directoryDialog.setText("Select Image Folder"); //$NON-NLS-1$
		directoryDialog.setMessage("Select a folder containing images"); //$NON-NLS-1$
		String selectedDirectory = directoryDialog.open();

		if (selectedDirectory == null) {
			System.out.println("No folder selected, exiting..."); //$NON-NLS-1$
			display.dispose();
			return;
		}

		Map<PixelTransformer, List<Label>> imageRowStorage = new HashMap<>();

		List<Image> originalImages = loadImagesFromFolder(selectedDirectory, display);

		// define PixelTransformers
		GTKDisablingTransformer gtkTransformer = new GTKDisablingTransformer();
		ThresholdBasedDisablingTransformer win32MacTransformer = new ThresholdBasedDisablingTransformer(GRAY_LOW,
				GRAY_HIGH, GRAY_HIGH, WIN32_COCOA_THRESHOLD, WIN32_COCOA_THRESHOLD);
		HSBConversionDisablingTransformer hsbTransformer = new HSBConversionDisablingTransformer(
				HSB_DISABLING_BRIGHTNESS_DEFAULT / 100f);
		ThresholdBasedDisablingTransformer adjThreshAdditGray = new ThresholdBasedDisablingTransformer(GRAY_LOW,
				ADDITIONAL_GRAY_MID_DEFAULT, GRAY_HIGH, ADJUSTABLE_THRESHOLD_LOW_DEFAULT,
				ADJUSTABLE_THRESHOLD_HIGH_DEFAULT);

		Composite imagesComposite = new Composite(shell, SWT.NONE);
		imagesComposite.setLayout(new GridLayout(2, false));

		// create transformed icon rows
		addTransformationRow(imagesComposite, "Original Images:", rgba -> rgba, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(current) Win32/macOS:", win32MacTransformer, originalImages,
				imageRowStorage);
		addTransformationRow(imagesComposite, "(current) GTK+:", gtkTransformer, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(proposed) HSB Adding Brightness:", hsbTransformer, originalImages,
				imageRowStorage);
		addTransformationRow(imagesComposite, "(proposed) Adjustable Threshold + 3rd Gray Tone:", adjThreshAdditGray,
				originalImages, imageRowStorage);

		Composite slidersComposite = new Composite(shell, SWT.NONE);
		slidersComposite.setLayout(new GridLayout(3, false));

		Scale lowScale = addScale(slidersComposite, "Low Threshold:", 0, MAX_BRIGHTNESS, WIN32_COCOA_THRESHOLD);
		Scale highScale = addScale(slidersComposite, "High Threshold:", 0, MAX_BRIGHTNESS,
				ADJUSTABLE_THRESHOLD_HIGH_DEFAULT);
		Scale thirdGray = addScale(slidersComposite, "3rd Gray:", GRAY_LOW.red, GRAY_HIGH.red,
				ADDITIONAL_GRAY_MID_DEFAULT.red);
		Scale hsbDisablingBrightness = addScale(slidersComposite, "HSB Brightness Percentage:", 40, 140,
				HSB_DISABLING_BRIGHTNESS_DEFAULT);

		lowScale.addListener(SWT.Selection, e -> {
			int lowThreshold = lowScale.getSelection();
			adjThreshAdditGray.setThresholdLow(lowThreshold);
			updateImageRow(adjThreshAdditGray, imageRowStorage, originalImages);
		});

		highScale.addListener(SWT.Selection, e -> {
			int highThreshold = highScale.getSelection();
			adjThreshAdditGray.setThresholdHigh(highThreshold);
			updateImageRow(adjThreshAdditGray, imageRowStorage, originalImages);
		});

		thirdGray.addListener(SWT.Selection, e -> {
			int grayMid = thirdGray.getSelection();
			adjThreshAdditGray.setGrayMid(new RGB(grayMid, grayMid, grayMid));
			updateImageRow(adjThreshAdditGray, imageRowStorage, originalImages);
		});

		hsbDisablingBrightness.addListener(SWT.Selection, e -> {
			int brightnessPercentage = hsbDisablingBrightness.getSelection();
			hsbTransformer.setBrightnessChange(brightnessPercentage / 100f);
			updateImageRow(hsbTransformer, imageRowStorage, originalImages);
		});

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		originalImages.forEach(Image::dispose);

		display.dispose();

	}

	private static void addTransformationRow(Composite parent, String labelText, PixelTransformer transformer,
			List<Image> originalImages, Map<PixelTransformer, List<Label>> imageRowStorage) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(originalImages.size(), false));

		List<Label> transformedImageLabels = new ArrayList<>(originalImages.size());
		for (Image original : originalImages) {
			Image transformed = transformImage(original, transformer);
			Label imageLabel = new Label(composite, SWT.NONE);
			imageLabel.setImage(transformed);
			transformedImageLabels.add(imageLabel);
		}
		imageRowStorage.put(transformer, transformedImageLabels);
	}

	private static Scale addScale(Composite parent, String labelText, int minimum, int maximum, int init) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		Scale scale = new Scale(parent, SWT.HORIZONTAL);
		scale.setMinimum(minimum);
		scale.setMaximum(maximum);
		scale.setSelection(init);
		Text lowValue = new Text(parent, SWT.NONE);
		lowValue.setEditable(false);
		lowValue.setText(String.valueOf(init));

		scale.addListener(SWT.Selection, e -> {
			int selection = scale.getSelection();
			lowValue.setText(String.valueOf(selection));
			lowValue.requestLayout();
		});

		return scale;
	}

	private static void updateImageRow(PixelTransformer transformer, Map<PixelTransformer, List<Label>> imageRowStorage,
			List<Image> originalImages) {
		List<Label> imageLabels = imageRowStorage.getOrDefault(transformer, List.of());

		for (int i = 0; i < originalImages.size(); i++) {
			Image transformed = transformImage(originalImages.get(i), transformer);
			Label imageLabel = imageLabels.get(i);
			imageLabel.getImage().dispose();
			imageLabel.setImage(transformed);
		}
	}

	@FunctionalInterface
	public static interface PixelTransformer {
		RGBA transform(RGBA originalRgba);
	}

	/*
	 * GTK+ Disabling algorithm used in Linux
	 */
	public static class GTKDisablingTransformer implements PixelTransformer {
		private double reductionFactor = 0.5d;

		public void setReductionFactor(double reductionFactor) {
			this.reductionFactor = reductionFactor;
		}

		@Override
		public RGBA transform(RGBA originalRgba) {
			int red = originalRgba.rgb.red;
			int green = originalRgba.rgb.green;
			int blue = originalRgba.rgb.blue;
			int alpha = originalRgba.alpha;

			return new RGBA((int) Math.round(red * reductionFactor), (int) Math.round(green * reductionFactor),
					(int) Math.round(blue * reductionFactor), (int) Math.round(alpha * reductionFactor));
		}
	}

	/**
	 * Class for Win32/MacOS disabling algorithm but with additional parameters:
	 * adjustable thresholds, 3rd gray tone and adjustable gray tones
	 */
	public static class ThresholdBasedDisablingTransformer implements PixelTransformer {
		private RGB grayLow;
		private RGB grayMid;
		private RGB grayHigh;
		private int thresholdLow;
		private int thresholdHigh;

		public ThresholdBasedDisablingTransformer(RGB grayLow, RGB grayMid, RGB grayHigh, int thresholdLow,
				int thresholdHigh) {
			this.grayLow = grayLow;
			this.grayMid = grayMid;
			this.grayHigh = grayHigh;
			this.thresholdLow = thresholdLow;
			this.thresholdHigh = thresholdHigh;
		}

		public void setThresholdLow(int thresholdLow) {
			this.thresholdLow = thresholdLow;
		}

		public void setThresholdHigh(int thresholdHigh) {
			this.thresholdHigh = thresholdHigh;
		}

		public void setGrayMid(RGB grayMid) {
			this.grayMid = grayMid;
		}

		@Override
		public RGBA transform(RGBA originalRgba) {
			int red = originalRgba.rgb.red;
			int green = originalRgba.rgb.green;
			int blue = originalRgba.rgb.blue;
			int alpha = originalRgba.alpha;

			RGBA disabled;
			int squareDist = red * red + green * green + blue * blue;
			if (squareDist < thresholdLow) {
				disabled = new RGBA(grayLow.red, grayLow.green, grayLow.blue, alpha);
			} else if (squareDist < thresholdHigh) {
				disabled = new RGBA(grayMid.red, grayMid.green, grayMid.blue, alpha);
			} else {
				disabled = new RGBA(grayHigh.red, grayHigh.green, grayHigh.blue, alpha);
			}
			return disabled;
		}
	}

	/*
	 * Class for HSB Conversion Disabling
	 */
	public static class HSBConversionDisablingTransformer implements PixelTransformer {
		private float brightnessChange;

		public HSBConversionDisablingTransformer(float brightnessChange) {
			this.brightnessChange = brightnessChange;
		}

		public void setBrightnessChange(float brightnessChange) {
			this.brightnessChange = brightnessChange;
		}

		@Override
		public RGBA transform(RGBA originalRgba) {
			float[] hsba = originalRgba.getHSBA();
			hsba[1] *= 0.0f; // Lower saturation (0.0f = grayscale, 1.0f = full saturation)
			hsba[2] = Math.min(hsba[2] * brightnessChange, 1.0f); // Change brightness (cap at 1.0 for max brightness)
			return new RGBA(hsba[0], hsba[1], hsba[2], hsba[3]);
		}
	}

	public static Image transformImage(Image originalImage, PixelTransformer transformer) {
		ImageData originalData = originalImage.getImageData();
		ImageData transformedData = new ImageData(originalData.width, originalData.height, 24,
				new PaletteData(0xFF, 0xFF00, 0xFF0000));

		for (int y = 0; y < originalData.height; y++) {
			for (int x = 0; x < originalData.width; x++) {
				int pixelValue = originalData.getPixel(x, y);
				RGB originalRGB = originalData.palette.getRGB(pixelValue);
				RGBA originalRGBA = new RGBA(originalRGB.red, originalRGB.green, originalRGB.blue,
						originalData.getAlpha(x, y));
				RGBA transformedRGBA = transformer.transform(originalRGBA);
				int newPixelValue = transformedData.palette.getPixel(transformedRGBA.rgb);
				transformedData.setPixel(x, y, newPixelValue);
				transformedData.setAlpha(x, y, transformedRGBA.alpha);
			}
		}
		return new Image(originalImage.getDevice(), transformedData);
	}

	private static List<Image> loadImagesFromFolder(String folderPath, Display display) {
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		return Arrays.stream(files).filter(File::isFile).filter(Snippet384::isImage)
				.map(file -> new Image(display, file.getAbsolutePath())).toList();
	}

	private static boolean isImage(File file) {
		List<String> imageExtensions = List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp");
		String fileName = file.getName().toLowerCase();
		return imageExtensions.stream().anyMatch(fileName::endsWith);
	}

}
