/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class SwtTestUtil {
	/**
	 * The following flags are used to mark test cases that
	 * are not handled correctly by SWT at this time, or test
	 * cases that maybe themselves dubious (eg. when the correct
	 * behaviour may not be clear). Most of these flagged test
	 * cases involve handling error conditions.
	 *
	 * Setting these flags to true will run those tests. As api
	 * is implemented this gives us a convenient way to include
	 * it in the junit tests.
	 */

	// run test cases that may themselves be dubious
	// these should be eventually checked to see if
	// there is a valid failure or the test is bogus
	public static boolean fCheckBogusTestCases = false;

	// check visibility api (eg in menu)
	public static boolean fCheckVisibility = false;

	// run test cases that check SWT policy not covered by the flags above
	public static boolean fCheckSWTPolicy = false;

	// make dialog open calls, operator must then close them
	public static boolean fTestDialogOpen = false;

	public static boolean fTestConsistency = false;

	// used to specify verbose mode, if true unimplemented warning messages will
	// be written to System.out
	public static boolean verbose = false;

	// allow specific image formats to be tested
	public static String[] imageFormats = new String[] {"bmp", "jpg", "gif", "png"};
	public static String[] imageFilenames = new String[] {"folder", "folderOpen", "target"};
	public static String[] invalidImageFilenames = new String[] {"corrupt", "corruptBadBitDepth.png"};
	public static String[] transparentImageFilenames = new String[] {"transparent.png"};

	public static final String testFontName;
	public static final String testFontNameFixedWidth;
	// isWindows refers to windows platform, i.e. win32 windowing system; see also isWindowsOS
	public final static boolean isWindows = SWT.getPlatform().startsWith("win32");
	public final static boolean isCocoa = SWT.getPlatform().startsWith("cocoa");
	public final static boolean isGTK = SWT.getPlatform().equals("gtk");
	public final static boolean isWindowsOS = System.getProperty("os.name").startsWith("Windows");
	public final static boolean isLinux = System.getProperty("os.name").equals("Linux");


	/** Useful if you want some tests not to run on Jenkins with user "genie.platform" */
	public final static boolean isRunningOnContinousIntegration = isGTK && ("genie.platform".equalsIgnoreCase(System.getProperty("user.name")));

	public final static boolean isX11 = isGTK
			&& "x11".equals(System.getProperty("org.eclipse.swt.internal.gdk.backend"));


	/**
	 * The palette used by images. See {@link #getAllPixels(Image)} and {@link #createImage}
	 */
	private static final PaletteData palette = new PaletteData (0xFF00, 0xFF0000, 0xFF000000);

	static {
		testFontName = "Helvetica";
		if (SwtTestUtil.isCocoa) {
			testFontNameFixedWidth = "Monaco";
		} else if (SwtTestUtil.isGTK) {
			testFontNameFixedWidth = "Monospace";
		} else if (SwtTestUtil.isWindows) {
			testFontNameFixedWidth = "Consolas";
		} else {
			testFontNameFixedWidth = null;
			fail("A fixed width font is needed for this platform");
		}
	}

public static void assertSWTProblem(String message, int expectedCode, Throwable actualThrowable) {
	if (actualThrowable instanceof SWTError) {
		SWTError error = (SWTError) actualThrowable;
		assertEquals(message, expectedCode, error.code);
	} else if (actualThrowable instanceof SWTException) {
		SWTException exception = (SWTException) actualThrowable;
		assertEquals(message, expectedCode, exception.code);
	} else {
		try {
			SWT.error(expectedCode);
		} catch (Throwable expectedThrowable) {
			if (actualThrowable.getMessage().length() > expectedThrowable.getMessage().length()) {
				assertTrue(message, actualThrowable.getMessage().startsWith(expectedThrowable.getMessage()));
			}
			else {
				assertEquals(message, expectedThrowable.getMessage(), actualThrowable.getMessage());
			}
		}
	}
}

public static boolean isBidi() {
	return true;
}

/**
 * Open the given shell and wait until it is actually opened! Dependent on
 * platform the shell is not immediately open after {@link Shell#open()} and has
 * not its final bounds.
 * <p>
 * If opening the shell fails or is, for whatever reason, not recognized the
 * method will return after a short timeout.
 * </p>
 *
 * @param shell the shell to open. Does nothing if <code>null</code> or already
 *              open.
 */
public static void openShell(Shell shell) {
	if (shell != null && !shell.getVisible()) {
		if (isGTK) {
			AtomicBoolean paintRequested = new AtomicBoolean(false);
			shell.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					paintRequested.set(true);
					shell.removePaintListener(this);
				}
			});
			shell.open();
			long start = System.currentTimeMillis();
			while (!paintRequested.get() && System.currentTimeMillis() - 1000 < start) {
				processEvents();
			}
		} else {
			shell.open();
		}

	}
}

/**
 * When debugging a test that draws to an Image it can be useful to see the
 * image in a shell. Call this method with the image to open it.
 *
 * This method is blocking, so should not be called in committed code or else
 * the test will hang.
 *
 * @param image to display
 */
public static void debugDisplayImage(Image image) {
	Display display = (Display) image.getDevice();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	shell.setSize(image.getBounds().width + 50, image.getBounds().height + 100);
	Canvas canvas = new Canvas(shell, SWT.NONE);
	// Create a paint handler for the canvas
	canvas.addPaintListener(e -> e.gc.drawImage(image, 10, 10));
	SwtTestUtil.openShell(shell);
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
}

/**
 * When debugging a test that draws to an Image it can be useful to see the
 * image in a shell. Call this method with the image to open it.
 *
 * This method is blocking, so should not be called in committed code or else
 * the test will hang.
 *
 * @param images to display
 */
public static void debugDisplayImages(Image[] images, int numColumns) {
	Display display = (Display) images[0].getDevice();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(numColumns, false));

	for (Image image : images) {
		Label label = new Label(shell, 0);
		label.setImage(image);
	}

	shell.pack();
	SwtTestUtil.openShell(shell);

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
}

/**
 * Convert the 2D array of pixels (possibly returned by {@link #getAllPixels(Image)})
 * and display them using {@link #debugDisplayImage(Image)}
 * @param pixels 2d array, first index is column (x), second index is row (y)
 */
public static void debugDisplayImage(int[][] pixels, int startColumn, int numColumns) {
	Image image = createImage(pixels, startColumn, numColumns);
	try {
		debugDisplayImage(image);
	} finally {
		image.dispose();
	}

}

private static boolean displayExpected = true;

public static void debugDisplayDifferences(Image expected, Image actual) {
	Display display = (Display) expected.getDevice();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	int width = Math.max(expected.getBounds().width, actual.getBounds().width) + 50;
	int height = Math.max(expected.getBounds().height, actual.getBounds().height) + 100;
	shell.setSize(width, height);
	Canvas canvas = new Canvas(shell, SWT.NONE);
	canvas.addPaintListener(e -> {
		e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		e.gc.fillRectangle(new Rectangle(0, 0, width, height));
		System.out.println("Drawing " + (displayExpected ? "expected" : "actual"));
		e.gc.drawImage(displayExpected ? expected : actual, 10, 10);
	});
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			displayExpected = !displayExpected;
			canvas.redraw();
			display.timerExec(500, this);
		}
	};
	display.timerExec(500, runnable);

	SwtTestUtil.openShell(shell);
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}

}


public static Image createImage(int[][] pixels, int startColumn, int numColumns) {
	int width = pixels.length;
	int height = width > 0 ? pixels[0].length : 0;
	int displayWidth = numColumns;
	ImageData imageData = new ImageData(displayWidth, height, 32, palette);
	for (int x = 0; x < displayWidth; x++) {
		for (int y = 0; y < height; y++) {
			imageData.setPixel(x, y, pixels[x + startColumn][y]);
		}
	}
	Image image = new Image(Display.getDefault(), imageData);
	return image;
}

/**
 * Convert an image into a 2D array of pixel values.
 *
 * @param image to extract pixel values from
 * @return Returns 2d array, first index is column (x), second index is row (y)
 */
public static int[][] getAllPixels(Image image) {
	ImageData imageData = image.getImageData();
	Rectangle bounds = image.getBounds();
	int[][] pixels = new int[bounds.width][];
	for (int x = 0; x < bounds.width; x++) {
		pixels[x] = new int[bounds.height];
		for (int y = 0; y < bounds.height; y++) {
			pixels[x][y] = imageData.getPixel(x, y);
		}
	}
	return pixels;
}

/**
 * Compares two pixels as returned by {@link #getAllPixels(Image)} to see if they have
 * the same brightness.
 * @param message Message used in assertion
 * @param expected expected pixel value
 * @param actual actual pixel value
 */
public static void assertSimilarBrightness(String message, int expected, int actual) {
	if (expected != actual) {
		// Use https://www.w3.org/TR/AERT/#color-contrast which
		// provides an algorithm to determine if colors are too
		// close together to be readable.
		// 1) Convert colour with -> 0.299R + 0.587G + 0.114B to brightness (aka greyscale)
		// 2) and ensure  brightness is within 12.5% of the range.
		double expectedIntensity = getBrightness(expected);
		double actualIntensity = getBrightness(actual);
		assertEquals(message, expectedIntensity, actualIntensity, 255f / 8);
	}
}

/**
 * Converts one pixel as returned by {@link #getAllPixels(Image)} to its
 * intensity (how light/dark it is).
 */
private static double getBrightness(int pixel) {
	RGB rgb = palette.getRGB(pixel);
	return 0.299 * rgb.red +   0.587 * rgb.green + 0.114 * rgb.blue;
}

/**
 * Dispatch all pending events (until {@link Display#readAndDispatch()} returned
 * <code>false</code>).
 */
public static void processEvents() {
	Display display = Display.getCurrent();
	if (display != null && !display.isDisposed()) {
		while (display.readAndDispatch()) {
		}
	}
}

/**
 * Check if widget contains the given color.
 *
 * @param control       widget to check
 * @param expectedColor color to find
 * @return <code>true</code> if the given color was found in current text widget
 *         bounds
 */
public static boolean hasPixel(Control control, Color expectedColor) {
	return hasPixel(control, expectedColor, null);
}

/**
 * Check if widget contains the given color in given bounds. The effective
 * search range to find the color is the union of current widget bounds and
 * given rectangle.
 *
 * @param control       widget to check
 * @param expectedColor color to find
 * @param rect          the bounds where the color is searched in. Can overlap
 *                      the control bounds or <code>null</code> to check the
 *                      widgets full bounds.
 * @return <code>true</code> if the given color was found in search range of
 *         widget
 */
public static boolean hasPixel(Control control, Color expectedColor, Rectangle rect) {
	GC gc = new GC(control);
	final Image image = new Image(control.getDisplay(), control.getSize().x, control.getSize().y);
	gc.copyArea(image, 0, 0);
	gc.dispose();
	boolean result = hasPixel(image, expectedColor, rect);
	image.dispose();
	return result;
}

/**
 * Check if image contains the given color in given bounds. The effective search
 * range to find the color is the union of image size and given rectangle.
 *
 * @param image         image to check for the expected color
 * @param expectedColor color to find
 * @param rect          the bounds where the color is searched in. Can overlap
 *                      the image bounds or <code>null</code> to check the whole
 *                      image.
 * @return <code>true</code> if the given color was found in search range of
 *         image
 */
public static boolean hasPixel(Image image, Color expectedColor, Rectangle rect) {
	ImageData imageData = image.getImageData();
	if (rect == null) {
		rect = new Rectangle(0, 0, image.getBounds().width, image.getBounds().height);
	}
	RGB expectedRGB = expectedColor.getRGB();
	int xEnd = rect.x + rect.width;
	int yEnd = rect.y + rect.height;
	for (int x = Math.max(rect.x, 1); x < xEnd && x < image.getBounds().width - 1; x++) { // ignore first and last columns
		for (int y = rect.y; y < yEnd && y < image.getBounds().height; y++) {
			RGB pixelRGB = imageData.palette.getRGB(imageData.getPixel(x, y));
			if (expectedRGB.equals(pixelRGB)) {
				return true;
			}
		}
	}
	return false;
}

/**
 * Check if image contains any pixel not matching the given color. The effective
 * search range to find the color is the union of image size and given
 * rectangle.
 *
 * This is useful for checking that some pixels don't match a background color. If
 * possible use {@link #hasPixel(Image, Color, Rectangle)}, but on Linux where
 * Antialias can't be turned off for test matching the foreground color can be
 * impossible.
 *
 * @param image            image to check for the expected color
 * @param nonMatchingColor not matching the given color
 * @param rect             the bounds where the color is searched in. Can
 *                         overlap the image bounds or <code>null</code> to
 *                         check the whole image.
 * @return <code>true</code> if any color other than the given color was found
 *         in search range of image
 */
public static boolean hasPixelNotMatching(Image image, Color nonMatchingColor, Rectangle rect) {
	ImageData imageData = image.getImageData();
	if (rect == null) {
		rect = new Rectangle(0, 0, image.getBounds().width, image.getBounds().height);
	}
	RGB nonMatchingRGB = nonMatchingColor.getRGB();
	int xEnd = rect.x + rect.width;
	int yEnd = rect.y + rect.height;
	for (int x = Math.max(rect.x, 1); x < xEnd && x < image.getBounds().width - 1; x++) { // ignore first and last columns
		for (int y = rect.y; y < yEnd && y < image.getBounds().height; y++) {
			RGB pixelRGB = imageData.palette.getRGB(imageData.getPixel(x, y));
			if (!nonMatchingRGB.equals(pixelRGB)) {
				return true;
			}
		}
	}
	return false;
}
}
