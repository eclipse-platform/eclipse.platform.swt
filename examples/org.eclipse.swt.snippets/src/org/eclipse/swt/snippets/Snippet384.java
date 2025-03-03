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
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet384 {

	private static Display display = new Display();

	private static final Color LIGHT_THEME_BACKGROUND = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	private static final Color DARK_THEME_GRAY = new Color(47, 47, 47);
	private static final Color DARK_THEME_GRAY_DARKER = new Color(72, 72, 76);

	// colors and thresholds used in the current Win32/macOS algorithm
	private static final RGB GRAY_LOW = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB(); // == RGB(160, 160, 160) on Windows
	private static final RGB GRAY_HIGH = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB(); // == RGB(240, 240,240) on Windows
	private static final int WIN32_COCOA_THRESHOLD = 98304; // == 3 * 2^15, this is the real threshold used in Win32/macOS
	private static final int BACKGROUND_THRESHOLD = GRAY_HIGH.red * GRAY_HIGH.red + GRAY_HIGH.green + GRAY_HIGH.green + GRAY_HIGH.blue * GRAY_HIGH.blue; // == on windows 240^2 * 3
	private static final int MAX_BRIGHTNESS = 256 * 256 * 3;

	// change these to adjust the sliders default values
	private static final int ADJUSTABLE_THRESHOLD_LOW_DEFAULT = WIN32_COCOA_THRESHOLD;
	private static final int ADJUSTABLE_THRESHOLD_HIGH_DEFAULT = BACKGROUND_THRESHOLD;
	private static final RGB ADDITIONAL_GRAY_MID_DEFAULT = new RGB(214, 214, 214); // arbitrarily chosen

	private static final int HSB_DISABLING_BRIGHTNESS_DEFAULT = 90;
	private static final int HSB_DISABLING_ALPHA_DEFAULT = 100;
	private static final int HSB_DISABLING_SATURATION_DEFAULT = 100;

	private static final int ECLIPSE_RENDER_MOJOE_BRIGHTNESS_DEFAULT = 290;
	private static final int ECLIPSE_RENDER_MOJO_ALPHA_DEFAULT = 100;

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
		ThresholdBasedDisablingTransformer win32MacTransformer = new ThresholdBasedDisablingTransformer(GRAY_LOW, GRAY_HIGH, GRAY_HIGH, WIN32_COCOA_THRESHOLD, WIN32_COCOA_THRESHOLD);
		EclipseRenderMojoDisablingTransformer eclipseMojoTransformer = new EclipseRenderMojoDisablingTransformer(ECLIPSE_RENDER_MOJOE_BRIGHTNESS_DEFAULT / 100f, ECLIPSE_RENDER_MOJO_ALPHA_DEFAULT / 100.f);
		HSBConversionDisablingTransformer hsbTransformer = new HSBConversionDisablingTransformer(HSB_DISABLING_BRIGHTNESS_DEFAULT / 100.f, HSB_DISABLING_ALPHA_DEFAULT / 100.f, HSB_DISABLING_SATURATION_DEFAULT / 100.f);
		ThresholdBasedDisablingTransformer adjThreshAdditGray = new ThresholdBasedDisablingTransformer(GRAY_LOW, ADDITIONAL_GRAY_MID_DEFAULT, GRAY_HIGH, ADJUSTABLE_THRESHOLD_LOW_DEFAULT, ADJUSTABLE_THRESHOLD_HIGH_DEFAULT);

		// create transformed icon rows
		Composite imagesComposite = new Composite(shell, SWT.NONE);
		imagesComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		imagesComposite.setLayout(new GridLayout(2, false));
		addTransformationRow(imagesComposite, "Original Images:", rgba -> rgba, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(current) Win32/macOS:", win32MacTransformer, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(current) Eclipsde Pre-Disabling Mojo", eclipseMojoTransformer, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(current) GTK+:", gtkTransformer, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(proposed) HSB Transformation:", hsbTransformer, originalImages, imageRowStorage);
		addTransformationRow(imagesComposite, "(proposed) Adjustable Threshold + 3rd Gray Tone:", adjThreshAdditGray, originalImages, imageRowStorage);

		// add sliders
		Composite hsbSliderComposite = new Composite(shell, SWT.NONE);
		hsbSliderComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		hsbSliderComposite.setLayout(new GridLayout(9, false));
		Scale hsbDisablingBrightness = addScale(hsbSliderComposite, "HSB Brightness %:", 40, 140, HSB_DISABLING_BRIGHTNESS_DEFAULT);
		Scale hsbDisablingSaturation = addScale(hsbSliderComposite, "HSB Saturation %:", 0, 100, HSB_DISABLING_SATURATION_DEFAULT);
		Scale hsbDisablingAlpha = addScale(hsbSliderComposite, "HSB Alpha %:", 0, 100, HSB_DISABLING_ALPHA_DEFAULT);

		Composite eclipseMojoSliderComposite = new Composite(shell, SWT.NONE);
		eclipseMojoSliderComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		eclipseMojoSliderComposite.setLayout(new GridLayout(9, false));
		Scale eclipsePreDisablingBrightness = addScale(eclipseMojoSliderComposite, "Eclipse Mojo Brightness %:", 40, 350, ECLIPSE_RENDER_MOJOE_BRIGHTNESS_DEFAULT);
		Scale eclipsePreDisablingAlpha = addScale(eclipseMojoSliderComposite, "Eclipse Mojo Alpha %:", 0, 100, ECLIPSE_RENDER_MOJO_ALPHA_DEFAULT);

		Composite adjThreshAddGraySliderComposite = new Composite(shell, SWT.NONE);
		adjThreshAddGraySliderComposite.setLayout(new GridLayout(9, false));
		adjThreshAddGraySliderComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		Scale lowScale = addScale(adjThreshAddGraySliderComposite, "Low Threshold:", 0, MAX_BRIGHTNESS, WIN32_COCOA_THRESHOLD);
		Scale highScale = addScale(adjThreshAddGraySliderComposite, "High Threshold:", 0, MAX_BRIGHTNESS, ADJUSTABLE_THRESHOLD_HIGH_DEFAULT);
		Scale thirdGray = addScale(adjThreshAddGraySliderComposite, "3rd Gray:", GRAY_LOW.red, GRAY_HIGH.red, ADDITIONAL_GRAY_MID_DEFAULT.red);

		// update images on slider change
	    addImageUpdateScaleListener(hsbDisablingBrightness, hsbTransformer, value -> hsbTransformer.setBrightnessChange(value / 100.f), imageRowStorage, originalImages);
	    addImageUpdateScaleListener(hsbDisablingAlpha, hsbTransformer, value -> hsbTransformer.setAlphaChange(value / 100.f), imageRowStorage, originalImages);
	    addImageUpdateScaleListener(hsbDisablingSaturation, hsbTransformer, value -> hsbTransformer.setSaturationChange(value / 100.f), imageRowStorage, originalImages);

	    addImageUpdateScaleListener(eclipsePreDisablingBrightness, eclipseMojoTransformer, value -> eclipseMojoTransformer.setBrightnessChange(value / 100.f), imageRowStorage, originalImages);
	    addImageUpdateScaleListener(eclipsePreDisablingAlpha, eclipseMojoTransformer, value -> eclipseMojoTransformer.setAlphaChange(value / 100.f), imageRowStorage, originalImages);

		addImageUpdateScaleListener(lowScale, adjThreshAdditGray, adjThreshAdditGray::setThresholdLow, imageRowStorage, originalImages);
	    addImageUpdateScaleListener(highScale, adjThreshAdditGray, adjThreshAdditGray::setThresholdHigh, imageRowStorage, originalImages);
	    addImageUpdateScaleListener(thirdGray, adjThreshAdditGray, value -> adjThreshAdditGray.setGrayMid(new RGB(value, value, value)), imageRowStorage, originalImages);

	    // combo box for theme selection
	    Combo themeCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	    themeCombo.setItems("Light Theme", "Dark Theme", "Darker Theme");
	    themeCombo.select(0);
	    themeCombo.addListener(SWT.Selection, e -> {
	    	int selected = themeCombo.getSelectionIndex();
	    	switch (selected) {
	    		case 1:
	    			shell.setBackground(DARK_THEME_GRAY);
	    			imagesComposite.setBackground(DARK_THEME_GRAY_DARKER);
	    			break;
	    		case 2:
	    			shell.setBackground(DARK_THEME_GRAY_DARKER);
	    			imagesComposite.setBackground(DARK_THEME_GRAY);
	    			break;
	    		default:
	    			shell.setBackground(LIGHT_THEME_BACKGROUND);
	    			imagesComposite.setBackground(LIGHT_THEME_BACKGROUND);
	    	}
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

	private static void addImageUpdateScaleListener(Scale scale, PixelTransformer transformer,  IntConsumer transformerUpdateMethod, Map<PixelTransformer, List<Label>> transformedImageStorage, List<Image> imagesToTransformOnUpdate ) {
		scale.addListener(SWT.Selection, e -> {
			int scaleValue = scale.getSelection();
			transformerUpdateMethod.accept(scaleValue);
			updateImageRow(transformer, transformedImageStorage, imagesToTransformOnUpdate);
		});
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
		private float alphaChange;
		private float saturationChange;

		public HSBConversionDisablingTransformer(float brightnessChange, float alphaChange, float saturationChange) {
			this.saturationChange = saturationChange;
			this.brightnessChange = brightnessChange;
			this.alphaChange = alphaChange;
		}

		public void setBrightnessChange(float brightnessChange) {
			this.brightnessChange = brightnessChange;
		}

		public void setAlphaChange(float alphaChange) {
			this.alphaChange = alphaChange;
		}

		public void setSaturationChange(float saturationChange) {
			this.saturationChange = saturationChange;
		}

		@Override
		public RGBA transform(RGBA originalRgba) {
			float[] hsba = originalRgba.getHSBA();

			hsba[1] *= saturationChange;
			hsba[2] *= brightnessChange;
			hsba[3] *= alphaChange;

			hsba[1] = Math.min(hsba[1], 1.f);
			hsba[2] = Math.min(hsba[2], 1.f);
			hsba[3] = Math.min(hsba[3], 255.f);

			return new RGBA(hsba[0], hsba[1], hsba[2], hsba[3]);
		}
	}

	/*
	 * Class for Eclipse Mojo disabling (offline disabling for icons delivered with
	 * eclipse)
	 */
	public static class EclipseRenderMojoDisablingTransformer implements PixelTransformer {
		private float brightnessChange;
		private float alphaChange;

		public EclipseRenderMojoDisablingTransformer(float brightnessChange, float alphaChange) {
			this.brightnessChange = brightnessChange;
			this.alphaChange = alphaChange;
		}

		public void setBrightnessChange(float brightnessChange) {
			this.brightnessChange = brightnessChange;
		}

		public void setAlphaChange(float alphaChange) {
			this.alphaChange = alphaChange;
		}

		@Override
		public RGBA transform(RGBA rgba) {
			float saturation = 0.3f;
			float contrast = 0.2f;
			// float brightness = 2.9f;

			// Adjust saturation
			float[] hsba = rgba.getHSBA();
			hsba[1] *= saturation;
			rgba = new RGBA(hsba[0], hsba[1], hsba[2], hsba[3]);

			// Adjust contrast and brightness
			rgba.rgb.blue = (int) Math.min(Math.max((contrast * (rgba.rgb.blue * brightnessChange - 128) + 128), 0), 255);
			rgba.rgb.red = (int) Math.min(Math.max((contrast * (rgba.rgb.red * brightnessChange - 128) + 128), 0), 255);
			rgba.rgb.green = (int) Math.min(Math.max((contrast * (rgba.rgb.green * brightnessChange - 128) + 128), 0), 255);
			rgba.alpha = (int) (alphaChange * rgba.alpha);

			return rgba;
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
