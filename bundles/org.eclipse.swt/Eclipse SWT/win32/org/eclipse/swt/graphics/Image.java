/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.graphics;


import static org.eclipse.swt.internal.image.ImageColorTransformer.DEFAULT_DISABLED_IMAGE_TRANSFORMER;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.image.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class are graphics which have been prepared
 * for display on a specific device. That is, they are ready
 * to paint using methods such as <code>GC.drawImage()</code>
 * and display on widgets with, for example, <code>Button.setImage()</code>.
 * <p>
 * If loaded from a file format that supports it, an
 * <code>Image</code> may have transparency, meaning that certain
 * pixels are specified as being transparent when drawn. Examples
 * of file formats that support transparency are GIF and PNG.
 * </p><p>
 * There are two primary ways to use <code>Images</code>.
 * The first is to load a graphic file from disk and create an
 * <code>Image</code> from it. This is done using an <code>Image</code>
 * constructor, for example:
 * <pre>
 *    Image i = new Image(device, "C:\\graphic.bmp");
 * </pre>
 * A graphic file may contain a color table specifying which
 * colors the image was intended to possess. In the above example,
 * these colors will be mapped to the closest available color in
 * SWT. It is possible to get more control over the mapping of
 * colors as the image is being created, using code of the form:
 * <pre>
 *    ImageData data = new ImageData("C:\\graphic.bmp");
 *    RGB[] rgbs = data.getRGBs();
 *    // At this point, rgbs contains specifications of all
 *    // the colors contained within this image. You may
 *    // allocate as many of these colors as you wish by
 *    // using the Color constructor Color(RGB), then
 *    // create the image:
 *    Image i = new Image(device, data);
 * </pre>
 * <p>
 * Applications which require even greater control over the image
 * loading process should use the support provided in class
 * <code>ImageLoader</code>.
 * </p><p>
 * Application code must explicitly invoke the <code>Image.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see Color
 * @see ImageData
 * @see ImageLoader
 * @see <a href="http://www.eclipse.org/swt/snippets/#image">Image snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Image extends Resource implements Drawable {

	/**
	 * specifies whether the receiver is a bitmap or an icon
	 * (one of <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int type;

	/**
	 * this field make sure the image is initialized without any errors
	 */
	private boolean isInitialized;

	/**
	 * this field is used to mark destroyed images
	 */
	private boolean isDestroyed;

	/**
	 * specifies the transparent pixel
	 */
	int transparentPixel = -1, transparentColor = -1;

	/**
	 * the GC which is drawing on the image
	 */
	GC memGC;

	/**
	 * AbstractImageProvider to avail right ImageProvider (ImageDataProvider or ImageFileNameProvider)
	 */
	private final AbstractImageProviderWrapper imageProvider;

	/**
	 * Style flag used to differentiate normal, gray-scale and disabled images based
	 * on image data providers. Without this, a normal and a disabled image of the
	 * same image data provider would be considered equal.
	 */
	private int styleFlag = SWT.IMAGE_COPY;

	/**
	 * Sets the color to which to map the transparent pixel.
	 * For further info see {@link #setBackground(Color)}
	 */
	private RGB backgroundColor;

	/**
	 * specifies the default scanline padding
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

	private Map<Integer, ImageHandle> zoomLevelToImageHandle = new HashMap<>();

	private List<Consumer<Image>> onDisposeListeners;

private Image (Device device, int type, long handle, int nativeZoom) {
	super(device);
	this.type = type;
	this.imageProvider = new ExistingImageHandleProviderWrapper(handle, nativeZoom);
	this.isInitialized = true;
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an empty instance of this class with the
 * specified width and height. The result may be drawn upon
 * by creating a GC and using any of its drawing operations,
 * as shown in the following example:
 * <pre>
 *    Image i = new Image(device, width, height);
 *    GC gc = new GC(i);
 *    gc.drawRectangle(0, 0, 50, 50);
 *    gc.dispose();
 * </pre>
 * <p>
 * Note: Some platforms may have a limitation on the size
 * of image that can be created (size depends on width, height,
 * and depth). For example, Windows 95, 98, and ME do not allow
 * images larger than 16M.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param width the width of the new image
 * @param height the height of the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either the width or height is negative or zero</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, int width, int height) {
	super(device);
	this.imageProvider = new PlainImageProviderWrapper(width, height);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs a new instance of this class based on the
 * provided image, with an appearance that varies depending
 * on the value of the flag. The possible flag values are:
 * <dl>
 * <dt><b>{@link SWT#IMAGE_COPY}</b></dt>
 * <dd>the result is an identical copy of srcImage</dd>
 * <dt><b>{@link SWT#IMAGE_DISABLE}</b></dt>
 * <dd>the result is a copy of srcImage which has a <em>disabled</em> look</dd>
 * <dt><b>{@link SWT#IMAGE_GRAY}</b></dt>
 * <dd>the result is a copy of srcImage which has a <em>gray scale</em> look</dd>
 * </dl>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param srcImage the image to use as the source
 * @param flag the style, either <code>IMAGE_COPY</code>, <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if srcImage is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the flag is not one of <code>IMAGE_COPY</code>, <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code></li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon, or is otherwise in an invalid state</li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the image is not supported</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, Image srcImage, int flag) {
	super(device);
	device = this.device;
	ImageHandle imageMetadata;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.type = srcImage.type;
	this.styleFlag = srcImage.styleFlag | flag;
	this.imageProvider = srcImage.imageProvider.createCopy(this);
	switch (flag) {
		case SWT.IMAGE_COPY: {
			switch (type) {
				case SWT.BITMAP:
					for (ImageHandle imageHandle : srcImage.zoomLevelToImageHandle.values()) {
						Rectangle rect = imageHandle.getBounds();
						long srcImageHandle = imageHandle.handle;
						/* Get the HDC for the device */
						long hDC = device.internal_new_GC(null);

						/* Copy the bitmap */
						long hdcSource = OS.CreateCompatibleDC(hDC);
						long hdcDest = OS.CreateCompatibleDC(hDC);
						long hOldSrc = OS.SelectObject(hdcSource, srcImageHandle);
						BITMAP bm = new BITMAP();
						OS.GetObject(srcImageHandle, BITMAP.sizeof, bm);
						imageMetadata = new ImageHandle(OS.CreateCompatibleBitmap(hdcSource, rect.width, bm.bmBits != 0 ? -rect.height : rect.height), imageHandle.zoom);
						if (imageMetadata.handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
						long hOldDest = OS.SelectObject(hdcDest, imageMetadata.handle);
						OS.BitBlt(hdcDest, 0, 0, rect.width, rect.height, hdcSource, 0, 0, OS.SRCCOPY);
						OS.SelectObject(hdcSource, hOldSrc);
						OS.SelectObject(hdcDest, hOldDest);
						OS.DeleteDC(hdcSource);
						OS.DeleteDC(hdcDest);

						/* Release the HDC for the device */
						device.internal_dispose_GC(hDC, null);
					}
					transparentPixel = srcImage.transparentPixel;
					break;
				case SWT.ICON:
					for (ImageHandle imageHandle : srcImage.zoomLevelToImageHandle.values()) {
						Rectangle rect = imageHandle.getBounds();
						imageMetadata = new ImageHandle(OS.CopyImage(imageHandle.handle, OS.IMAGE_ICON, rect.width, rect.height, 0), imageHandle.zoom);
						if (imageMetadata.handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					}
					break;
				default:
					SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			break;
		}
		case SWT.IMAGE_DISABLE: {
			for (ImageHandle imageHandle : srcImage.zoomLevelToImageHandle.values()) {
				Rectangle rect = imageHandle.getBounds();
				ImageData data = srcImage.getImageData(imageHandle.zoom);
				ImageData newData = applyDisableImageData(data, rect.height, rect.width);
				init (newData, imageHandle.zoom);
			}
			break;
		}
		case SWT.IMAGE_GRAY: {
			for (ImageHandle imageHandle : srcImage.zoomLevelToImageHandle.values()) {
				Rectangle rect = imageHandle.getBounds();
				ImageData data = srcImage.getImageData(imageHandle.zoom);
				ImageData newData = applyGrayImageData(data, rect.height, rect.width);
				init (newData, imageHandle.zoom);
			}
			break;
		}
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an empty instance of this class with the
 * width and height of the specified rectangle. The result
 * may be drawn upon by creating a GC and using any of its
 * drawing operations, as shown in the following example:
 * <pre>
 *    Image i = new Image(device, boundsRectangle);
 *    GC gc = new GC(i);
 *    gc.drawRectangle(0, 0, 50, 50);
 *    gc.dispose();
 * </pre>
 * <p>
 * Note: Some platforms may have a limitation on the size
 * of image that can be created (size depends on width, height,
 * and depth). For example, Windows 95, 98, and ME do not allow
 * images larger than 16M.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param bounds a rectangle specifying the image's width and height (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the bounds rectangle is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 *
 * @deprecated use {@link Image#Image(Device, int, int)} instead
 */
@Deprecated(since = "2025-06", forRemoval = true)
public Image(Device device, Rectangle bounds) {
	super(device);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageProvider = new PlainImageProviderWrapper(bounds.width, bounds.height);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class from the given
 * <code>ImageData</code>.
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param data the image data to create the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the image data is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the ImageData is not supported</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, ImageData data) {
	super(device);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageProvider = new PlainImageDataProviderWrapper(data);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

private Image(Device device, ImageData data, int zoom) {
	super(device);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageProvider = new PlainImageDataProviderWrapper(data, zoom);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class, whose type is
 * <code>SWT.ICON</code>, from the two given <code>ImageData</code>
 * objects. The two images must be the same size. Pixel transparency
 * in either image will be ignored.
 * <p>
 * The mask image should contain white wherever the icon is to be visible,
 * and black wherever the icon is to be transparent. In addition,
 * the source image should contain black wherever the icon is to be
 * transparent.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the icon
 * @param source the color data for the icon
 * @param mask the mask data for the icon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if either the source or mask is null </li>
 *    <li>ERROR_INVALID_ARGUMENT - if source and mask are different sizes</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, ImageData source, ImageData mask) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.imageProvider = new MaskedImageDataProviderWrapper(source, mask);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class by loading its representation
 * from the specified input stream. Throws an error if an error
 * occurs while loading the image, or if the result is an image
 * of an unsupported type.  Application code is still responsible
 * for closing the input stream.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the stream contains multiple images, only the first
 * one will be loaded. To load multiple images, use
 * <code>ImageLoader.load()</code>.
 * </p><p>
 * This constructor may be used to load a resource as follows:
 * </p>
 * <pre>
 *     static Image loadImage (Display display, Class clazz, String string) {
 *          InputStream stream = clazz.getResourceAsStream (string);
 *          if (stream == null) return null;
 *          Image image = null;
 *          try {
 *               image = new Image (display, stream);
 *          } catch (SWTException ex) {
 *          } finally {
 *               try {
 *                    stream.close ();
 *               } catch (IOException ex) {}
 *          }
 *          return image;
 *     }
 * </pre>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param stream the input stream to load the image from
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image stream contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image stream describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image stream contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image (Device device, InputStream stream) {
	super(device);
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageProvider = new ImageDataLoaderStreamProviderWrapper(stream);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class by loading its representation
 * from the file with the specified name. Throws an error if an error
 * occurs while loading the image, or if the result is an image
 * of an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading
 * a single image only. If the specified file contains
 * multiple images, only the first one will be used.
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param filename the name of the file to load the image from
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image (Device device, String filename) {
	super(device);
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageProvider = new ImageFileNameProviderWrapper(zoom -> {
		if (zoom == 100) {
			return filename;
		}
		return null;
	});
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class by loading its representation
 * from the file retrieved from the ImageFileNameProvider. Throws an
 * error if an error occurs while loading the image, or if the result
 * is an image of an unsupported type.
 * <p>
 * This constructor is provided for convenience for loading image as
 * per DPI level.
 *
 * @param device the device on which to create the image
 * @param imageFileNameProvider the ImageFileNameProvider object that is
 * to be used to get the file name
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the ImageFileNameProvider is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the fileName provided by ImageFileNameProvider is null at 100% zoom</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 * @since 3.104
 */
public Image(Device device, ImageFileNameProvider imageFileNameProvider) {
	super(device);
	this.imageProvider = new ImageFileNameProviderWrapper(imageFileNameProvider);
	if (imageFileNameProvider.getImagePath(100) == null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null,
				": ImageFileNameProvider [" + imageFileNameProvider + "] returns null fileName at 100% zoom.");
	}
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs an instance of this class by loading its representation
 * from the ImageData retrieved from the ImageDataProvider. Throws an
 * error if an error occurs while loading the image, or if the result
 * is an image of an unsupported type.
 * <p>
 * This constructor is provided for convenience for loading image as
 * per DPI level.
 *
 * @param device the device on which to create the image
 * @param imageDataProvider the ImageDataProvider object that is
 * to be used to get the ImageData
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the ImageDataProvider is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the ImageData provided by ImageDataProvider is null at 100% zoom</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 * @since 3.104
 */
public Image(Device device, ImageDataProvider imageDataProvider) {
	super(device);
	this.imageProvider = new ImageDataProviderWrapper(imageDataProvider);
	if (imageDataProvider.getImageData(100) == null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null,
				": ImageDataProvider [" + imageDataProvider + "] returns null ImageData at 100% zoom.");
	}
	if (Device.strictChecks) {
		validateLinearScaling(imageDataProvider);
	}
	init();
	this.device.registerResourceWithZoomSupport(this);
}

private void validateLinearScaling(ImageDataProvider provider) {
	final int baseZoom = 100;
	final int scaledZoom = 200;
	final int scaleFactor = scaledZoom / baseZoom;
	ImageData baseImageData = provider.getImageData(baseZoom);
	ImageData scaledImageData = provider.getImageData(scaledZoom);

	if (scaledImageData == null) {
		return;
	}

	if (scaledImageData.width != scaleFactor * baseImageData.width
			|| scaledImageData.height != scaleFactor * baseImageData.height) {
		System.err.println(String.format(
				"***WARNING: ImageData should be linearly scaled across zooms but size is (%d, %d) at 100%% and (%d, %d) at 200%%.",
				baseImageData.width, baseImageData.height, scaledImageData.width, scaledImageData.height));
		new Error().printStackTrace(System.err);
	}
}

/**
 * The provided ImageGcDrawer will be called on demand whenever a new variant of the
 * Image for an additional zoom is required. Depending on the OS-specific implementation
 * these calls will be done during the instantiation or later when a new variant is
 * requested.
 *
 * @param device the device on which to create the image
 * @param imageGcDrawer the ImageGcDrawer object to be called when a new image variant
 * for another zoom is required.
 * @param width the width of the new image in points
 * @param height the height of the new image in points
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the ImageGcDrawer is null</li>
 * </ul>
 * @since 3.129
 */
public Image(Device device, ImageGcDrawer imageGcDrawer, int width, int height) {
	super(device);
	this.imageProvider = new ImageGcDrawerWrapper(imageGcDrawer, width, height);
	init();
}

private ImageData adaptImageDataIfDisabledOrGray(ImageData data) {
	ImageData returnImageData = null;
	switch (this.styleFlag) {
		case SWT.IMAGE_DISABLE: {
			ImageData newData = applyDisableImageData(data, data.height, data.width);
			returnImageData = newData;
			break;
		}
		case SWT.IMAGE_GRAY: {
			ImageData newData = applyGrayImageData(data, data.height, data.width);
			returnImageData = newData;
			break;
		}
		default: {
			returnImageData = data;
			break;
		}
	}

	return returnImageData;
}

@Override
void init() {
	super.init();
	this.isInitialized = true;
}

private ImageData applyDisableImageData(ImageData data, int height, int width) {
	PaletteData palette = data.palette;
	ImageData newData = new ImageData(width, height, 32, new PaletteData(0xFF, 0xFF00, 0xFF0000));
	newData.alpha = data.alpha;
	newData.alphaData = data.alphaData;
	newData.maskData = data.maskData;
	newData.maskPad = data.maskPad;
	if (data.transparentPixel != -1) newData.transparentPixel = 0;

	/* Convert the pixels. */
	int[] scanline = new int[width];
	int[] maskScanline = null;
	ImageData mask = null;
	if (data.maskData != null) mask = data.getTransparencyMask();
	if (mask != null) maskScanline = new int[width];
	int redMask = palette.redMask;
	int greenMask = palette.greenMask;
	int blueMask = palette.blueMask;
	int redShift = palette.redShift;
	int greenShift = palette.greenShift;
	int blueShift = palette.blueShift;
	for (int y=0; y<height; y++) {
		data.getPixels(0, y, width, scanline, 0);
		if (mask != null) mask.getPixels(0, y, width, maskScanline, 0);
		for (int x=0; x<width; x++) {
			int pixel = scanline[x];
			if (!((data.transparentPixel != -1 && pixel == data.transparentPixel) || (mask != null && maskScanline[x] == 0))) {
				int red, green, blue;
				if (palette.isDirect) {
					red = pixel & redMask;
					red = (redShift < 0) ? red >>> -redShift : red << redShift;
					green = pixel & greenMask;
					green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
					blue = pixel & blueMask;
					blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
				} else {
					red = palette.colors[pixel].red;
					green = palette.colors[pixel].green;
					blue = palette.colors[pixel].blue;
				}
				RGBA result = DEFAULT_DISABLED_IMAGE_TRANSFORMER.adaptPixelValue(red, green, blue, data.getAlpha(x, y));
				newData.setAlpha(x, y, result.alpha);
				newData.setPixel(x, y, newData.palette.getPixel(result.rgb));
			}
		}
	}
	return newData;
}

private ImageData applyGrayImageData(ImageData data, int pHeight, int pWidth) {
	PaletteData palette = data.palette;
	ImageData newData = data;
	if (!palette.isDirect) {
		/* Convert the palette entries to gray. */
		RGB [] rgbs = palette.getRGBs();
		for (int i=0; i<rgbs.length; i++) {
			if (data.transparentPixel != i) {
				RGB color = rgbs [i];
				int red = color.red;
				int green = color.green;
				int blue = color.blue;
				int intensity = (red+red+green+green+green+green+green+blue) >> 3;
				color.red = color.green = color.blue = intensity;
			}
		}
		newData.palette = new PaletteData(rgbs);
	} else {
		/* Create a 8 bit depth image data with a gray palette. */
		RGB[] rgbs = new RGB[256];
		for (int i=0; i<rgbs.length; i++) {
			rgbs[i] = new RGB(i, i, i);
		}
		newData = new ImageData(pWidth, pHeight, 8, new PaletteData(rgbs));
		newData.alpha = data.alpha;
		newData.alphaData = data.alphaData;
		newData.maskData = data.maskData;
		newData.maskPad = data.maskPad;
		if (data.transparentPixel != -1) newData.transparentPixel = 254;

		/* Convert the pixels. */
		int[] scanline = new int[pWidth];
		int redMask = palette.redMask;
		int greenMask = palette.greenMask;
		int blueMask = palette.blueMask;
		int redShift = palette.redShift;
		int greenShift = palette.greenShift;
		int blueShift = palette.blueShift;
		for (int y=0; y<pHeight; y++) {
			int offset = y * newData.bytesPerLine;
			data.getPixels(0, y, pWidth, scanline, 0);
			for (int x=0; x<pWidth; x++) {
				int pixel = scanline[x];
				if (pixel != data.transparentPixel) {
					int red = pixel & redMask;
					red = (redShift < 0) ? red >>> -redShift : red << redShift;
					int green = pixel & greenMask;
					green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
					int blue = pixel & blueMask;
					blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
					int intensity = (red+red+green+green+green+green+green+blue) >> 3;
					if (newData.transparentPixel == intensity) intensity = 255;
					newData.data[offset] = (byte)intensity;
				} else {
					newData.data[offset] = (byte)254;
				}
				offset++;
			}
		}
	}
	return newData;
}

private ImageHandle getImageMetadata(int zoom) {
	if (zoomLevelToImageHandle.get(zoom) != null) {
		return zoomLevelToImageHandle.get(zoom);
	}
	return imageProvider.newImageHandle(zoom);
}


/**
 * <b>IMPORTANT:</b> This method is not part of the public
 * API for Image. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 *
 * Updates zoom and refresh the Image based on the native zoom level, if required.
 *
 * @param image the image to get the handle of
 * @param zoom device zoom in % of the monitor on which the image is painted
 *
 * @return true if image is refreshed
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static long win32_getHandle (Image image, int zoom) {
	if(image.isDisposed()) {
		return 0L;
	}
	return image.getImageMetadata(zoom).handle;
}

/**
 * <b>IMPORTANT:</b> This method is not part of the public
 * API for Image. It is marked public only so that it
 * can be shared within the packages provided by SWT.
 *
 * Draws a scaled image using the GC by another image.
 *
 * @param gc the GC to draw on the resulting image
 * @param original the image which is supposed to be scaled and drawn on the resulting image
 * @param width the width of the original image
 * @param height the height of the original image
 * @param scaleFactor the factor with which the image is supposed to be scaled
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static void drawScaled(GC gc, Image original, int width, int height, float scaleFactor) {
	gc.drawImage (original, 0, 0, width, height,
			0, 0, Math.round (width * scaleFactor), Math.round (height * scaleFactor), false);
}

long [] createGdipImage(Integer zoom) {
	long handle = Image.win32_getHandle(this, zoom);
	switch (type) {
		case SWT.BITMAP: {
			BITMAP bm = new BITMAP();
			OS.GetObject(handle, BITMAP.sizeof, bm);
			int depth = bm.bmPlanes * bm.bmBitsPixel;
			boolean isDib = bm.bmBits != 0;
			boolean hasAlpha = isDib && depth == 32;
			if (hasAlpha || transparentPixel != -1) {
				int imgWidth = bm.bmWidth;
				int imgHeight = bm.bmHeight;
				long srcHdc;
				long memHdc;
				{
					long hDC = device.internal_new_GC(null);
					if (hDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					srcHdc = OS.CreateCompatibleDC(hDC);
					memHdc = OS.CreateCompatibleDC(hDC);
					device.internal_dispose_GC(hDC, null);
				}
				if (srcHdc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				if (memHdc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldSrcBitmap = OS.SelectObject(srcHdc, handle);
				long memDib = createDIB(imgWidth, imgHeight, 32);
				if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldMemBitmap = OS.SelectObject(memHdc, memDib);
				BITMAP dibBM = new BITMAP();
				OS.GetObject(memDib, BITMAP.sizeof, dibBM);
				int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
				OS.BitBlt(memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
				long hHeap = OS.GetProcessHeap();
				long pixels = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, sizeInBytes);
				if (pixels == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				byte red = 0, green = 0, blue = 0;
				if (hasAlpha) {
					OS.MoveMemory(pixels, bm.bmBits, sizeInBytes);
				}
				else {
					if (bm.bmBitsPixel <= 8)  {
						byte[] color = new byte[4];
						OS.GetDIBColorTable(srcHdc, transparentPixel, 1, color);
						blue = color[0];
						green = color[1];
						red = color[2];
					} else {
						switch (bm.bmBitsPixel) {
							case 16:
								int blueMask = 0x1F;
								int blueShift = ImageData.getChannelShift(blueMask);
								byte[] blues = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(blueMask, blueShift)];
								blue = blues[(transparentPixel & blueMask) >> blueShift];
								int greenMask = 0x3E0;
								int greenShift = ImageData.getChannelShift(greenMask);
								byte[] greens = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(greenMask, greenShift)];
								green = greens[(transparentPixel & greenMask) >> greenShift];
								int redMask = 0x7C00;
								int redShift = ImageData.getChannelShift(redMask);
								byte[] reds = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(redMask, redShift)];
								red = reds[(transparentPixel & redMask) >> redShift];
								break;
							case 24:
								blue = (byte)((transparentPixel & 0xFF0000) >> 16);
								green = (byte)((transparentPixel & 0xFF00) >> 8);
								red = (byte)(transparentPixel & 0xFF);
								break;
							case 32:
								blue = (byte)((transparentPixel & 0xFF000000) >>> 24);
								green = (byte)((transparentPixel & 0xFF0000) >> 16);
								red = (byte)((transparentPixel & 0xFF00) >> 8);
								break;
						}
					}
					byte[] srcData = new byte[sizeInBytes];
					OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
					for (int y = 0, dp = 0; y < imgHeight; ++y) {
						for (int x = 0; x < imgWidth; ++x) {
							if (srcData[dp] == blue && srcData[dp + 1] == green && srcData[dp + 2] == red) {
								srcData[dp + 3] = (byte)0;
							} else {
								srcData[dp + 3] = (byte)0xFF;
							}
							dp += 4;
						}
					}
					OS.MoveMemory(pixels, srcData, sizeInBytes);
				}
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.SelectObject(memHdc, oldMemBitmap);
				OS.DeleteObject(srcHdc);
				OS.DeleteObject(memHdc);
				OS.DeleteObject(memDib);
				int pixelFormat = hasAlpha ? Gdip.PixelFormat32bppPARGB : Gdip.PixelFormat32bppARGB;
				return new long []{Gdip.Bitmap_new(imgWidth, imgHeight, dibBM.bmWidthBytes, pixelFormat, pixels), pixels};
			}
			return new long []{Gdip.Bitmap_new(handle, 0), 0};
		}
		case SWT.ICON: {
			/*
			* Bug in GDI+. Creating a new GDI+ Bitmap from a HICON segment faults
			* when the icon width is bigger than the icon height.  The fix is to
			* detect this and create a PixelFormat32bppARGB image instead.
			*/
			ICONINFO iconInfo = new ICONINFO();
			OS.GetIconInfo(handle, iconInfo);
			long hBitmap = iconInfo.hbmColor;
			if (hBitmap == 0) hBitmap = iconInfo.hbmMask;
			BITMAP bm = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
			int imgWidth = bm.bmWidth;
			int imgHeight = hBitmap == iconInfo.hbmMask ? bm.bmHeight / 2 : bm.bmHeight;
			long img = 0, pixels = 0;
			/*
			* Bug in GDI+.  Bitmap_new() segments fault if the image width
			* is greater than the image height.
			*
			* Note that it also fails to generated an appropriate alpha
			* channel when the icon depth is 32.
			*/
			if (imgWidth > imgHeight || bm.bmBitsPixel == 32) {
				long hDC = device.internal_new_GC(null);
				long srcHdc = OS.CreateCompatibleDC(hDC);
				long oldSrcBitmap = OS.SelectObject(srcHdc, hBitmap);
				long memHdc = OS.CreateCompatibleDC(hDC);
				long memDib = createDIB(imgWidth, imgHeight, 32);
				if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldMemBitmap = OS.SelectObject(memHdc, memDib);
				BITMAP dibBM = new BITMAP();
				OS.GetObject(memDib, BITMAP.sizeof, dibBM);
				OS.BitBlt(memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, hBitmap == iconInfo.hbmMask ? imgHeight : 0, OS.SRCCOPY);
				OS.SelectObject(memHdc, oldMemBitmap);
				OS.DeleteObject(memHdc);
				byte[] srcData = new byte[dibBM.bmWidthBytes * dibBM.bmHeight];
				OS.MoveMemory(srcData, dibBM.bmBits, srcData.length);
				OS.DeleteObject(memDib);
				OS.SelectObject(srcHdc, iconInfo.hbmMask);
				for (int y = 0, dp = 3; y < imgHeight; ++y) {
					for (int x = 0; x < imgWidth; ++x) {
						if (srcData[dp] == 0) {
							if (OS.GetPixel(srcHdc, x, y) != 0) {
								srcData[dp] = (byte)0;
							} else {
								srcData[dp] = (byte)0xFF;
							}
						}
						dp += 4;
					}
				}
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.DeleteObject(srcHdc);
				device.internal_dispose_GC(hDC, null);
				long hHeap = OS.GetProcessHeap();
				pixels = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, srcData.length);
				if (pixels == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				OS.MoveMemory(pixels, srcData, srcData.length);
				img = Gdip.Bitmap_new(imgWidth, imgHeight, dibBM.bmWidthBytes, Gdip.PixelFormat32bppARGB, pixels);
			} else {
				img = Gdip.Bitmap_new(handle);
			}
			if (iconInfo.hbmColor != 0) OS.DeleteObject(iconInfo.hbmColor);
			if (iconInfo.hbmMask != 0) OS.DeleteObject(iconInfo.hbmMask);
			return new long []{img, pixels};
		}
		default: SWT.error(SWT.ERROR_INVALID_IMAGE);
	}
	return null;
}

void addOnDisposeListener(Consumer<Image> onDisposeListener) {
	if (onDisposeListeners == null) {
		onDisposeListeners = new ArrayList<>();
	}
	onDisposeListeners.add(onDisposeListener);
}

@Override
public void dispose() {
	if (onDisposeListeners != null) {
		onDisposeListeners.forEach(listener -> listener.accept(this));
	}
	super.dispose();
}

@Override
void destroy () {
	device.deregisterResourceWithZoomSupport(this);
	if (memGC != null) memGC.dispose();
	this.isDestroyed = true;
	destroyHandles();
	memGC = null;
}

private void destroyHandles() {
	destroyHandles(__ -> true);
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	destroyHandles(zoom -> !zoomLevels.contains(zoom) && !this.imageProvider.getPreservedZoomLevels().contains(zoom));
}

private void destroyHandles(Predicate<Integer> filter) {
	Iterator<Entry<Integer, ImageHandle>> it = zoomLevelToImageHandle.entrySet().iterator();
	while (it.hasNext()) {
		Entry<Integer, ImageHandle> zoomToHandle = it.next();
		if (filter.test(zoomToHandle.getKey())) {
			ImageHandle imageHandle = zoomToHandle.getValue();
			it.remove();
			imageHandle.destroy();
		}
	}
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
@Override
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image) object;
	if (device != image.device || transparentPixel != image.transparentPixel) return false;
	return (styleFlag == image.styleFlag) && imageProvider.equals(image.imageProvider);
}

/**
 * Returns the color to which to map the transparent pixel, or null if
 * the receiver has no transparent pixel.
 * <p>
 * There are certain uses of Images that do not support transparency
 * (for example, setting an image into a button or label). In these cases,
 * it may be desired to simulate transparency by using the background
 * color of the widget to paint the transparent pixels of the image.
 * Use this method to check which color will be used in these cases
 * in place of transparency. This value may be set with setBackground().
 * </p>
 *
 * @return the background color of the image, or null if there is no transparency in the image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getBackground() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transparentPixel == -1) return null;
	if (backgroundColor != null) {
		// if a background color was set explicitly, we use the cached color directly
		return Color.win32_new(device, (backgroundColor.blue << 16) | (backgroundColor.green << 8) | backgroundColor.red);
	}

	/* Get the HDC for the device */
	long hDC = device.internal_new_GC(null);
	return applyUsingAnyHandle(imageHandle -> {
		long handle = imageHandle.handle;
		/* Compute the background color */
		BITMAP bm = new BITMAP();
		OS.GetObject(handle, BITMAP.sizeof, bm);
		long hdcMem = OS.CreateCompatibleDC(hDC);
		long hOldObject = OS.SelectObject(hdcMem, handle);
		int red = 0, green = 0, blue = 0;
		if (bm.bmBitsPixel <= 8)  {
			byte[] color = new byte[4];
			OS.GetDIBColorTable(hdcMem, transparentPixel, 1, color);
			blue = color[0] & 0xFF;
			green = color[1] & 0xFF;
			red = color[2] & 0xFF;
		} else {
			switch (bm.bmBitsPixel) {
				case 16:
					blue = (transparentPixel & 0x1F) << 3;
					green = (transparentPixel & 0x3E0) >> 2;
					red = (transparentPixel & 0x7C00) >> 7;
					break;
				case 24:
					blue = (transparentPixel & 0xFF0000) >> 16;
					green = (transparentPixel & 0xFF00) >> 8;
					red = transparentPixel & 0xFF;
					break;
				case 32:
					blue = (transparentPixel & 0xFF000000) >>> 24;
					green = (transparentPixel & 0xFF0000) >> 16;
					red = (transparentPixel & 0xFF00) >> 8;
					break;
				default:
					return null;
			}
		}
		OS.SelectObject(hdcMem, hOldObject);
		OS.DeleteDC(hdcMem);


		/* Release the HDC for the device */
		device.internal_dispose_GC(hDC, null);
		return Color.win32_new(device, (blue << 16) | (green << 8) | red);
	});
}

/**
 * Returns the bounds of the receiver. The rectangle will always
 * have x and y values of 0, and the width and height of the
 * image.
 *
 * @return a rectangle specifying the image's bounds in points.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 */
public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return getBounds (100);
}

Rectangle getBounds(int zoom) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (zoomLevelToImageHandle.containsKey(zoom)) {
		ImageHandle imageMetadata = zoomLevelToImageHandle.get(zoom);
		Rectangle rectangle = new Rectangle(0, 0, imageMetadata.width, imageMetadata.height);
		return Win32DPIUtils.scaleBounds(rectangle, zoom, imageMetadata.zoom);
	}
	return this.imageProvider.getBounds(zoom);
}

/**
 * Returns the bounds of the receiver. The rectangle will always
 * have x and y values of 0, and the width and height of the
 * image in pixels.
 *
 * @return a rectangle specifying the image's bounds in pixels.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 * @since 3.105
 * @deprecated This API doesn't serve the purpose in an environment having
 *             multiple monitors with different DPIs, hence deprecated.
 */
@Deprecated(since = "2025-09", forRemoval = true)
public Rectangle getBoundsInPixels() {
	return applyUsingAnyHandle(ImageHandle::getBounds);
}

/**
 * Returns an <code>ImageData</code> based on the receiver.
 * Modifications made to this <code>ImageData</code> will not
 * affect the Image.
 *
 * @return an <code>ImageData</code> containing the image's data and
 *         attributes at 100% zoom level.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @see ImageData
 */
public ImageData getImageData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return getImageData(100);
}

/**
 * Returns an {@link ImageData} for the given zoom level based on the
 * receiver.
 * <p>
 * Note that this method is mainly intended to be used by custom
 * implementations of {@link ImageDataProvider} that draw a composite image
 * at the requested zoom level based on other images. For custom zoom
 * levels, the image data may be an auto-scaled version of the native image
 * and may look more blurred or mangled than expected.
 * </p>
 * <p>
 * Modifications made to the returned {@code ImageData} will not affect this
 * {@code Image}.
 * </p>
 *
 * @param zoom
 *            The zoom level in % of the standard resolution (which is 1
 *            physical monitor pixel == 1 SWT logical point). Typically 100,
 *            150, or 200.
 * @return an <code>ImageData</code> containing the image's data and
 *         attributes at the given zoom level
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @since 3.106
 */
public ImageData getImageData (int zoom) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (zoomLevelToImageHandle.containsKey(zoom)) {
		return zoomLevelToImageHandle.get(zoom).getImageData();
	}
	return this.imageProvider.newImageData(zoom);
}


/**
 * Returns an <code>ImageData</code> based on the receiver.
 * Modifications made to this <code>ImageData</code> will not
 * affect the Image.
 *
 * @return an <code>ImageData</code> containing the image's data
 * and attributes at the current zoom level.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @see ImageData
 * @since 3.105
 * @deprecated This API doesn't serve the purpose in an environment having
 *             multiple monitors with different DPIs, hence deprecated. Use
 *             {@link #getImageData(int)} instead.
 */
@Deprecated(since = "2025-09", forRemoval = true)
public ImageData getImageDataAtCurrentZoom() {
	return applyUsingAnyHandle(ImageHandle::getImageData);
}

/**
 * Returns an integer hash code for the receiver. Any two
 * objects that return <code>true</code> when passed to
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
@Override
public int hashCode () {
	return imageProvider.hashCode();
}

static long createDIB(int width, int height, int depth) {
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = width;
	bmiHeader.biHeight = -height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)depth;
	bmiHeader.biCompression = OS.BI_RGB;
	byte[] bmi = new byte[BITMAPINFOHEADER.sizeof];
	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	long[] pBits = new long[1];
	return OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
}

private static ImageData indexToIndex(ImageData src, int newDepth) {
	ImageData img = new ImageData(src.width, src.height, newDepth, src.palette);

	ImageData.blit(
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), src.width, src.height,
		img.data, img.depth, img.bytesPerLine, src.getByteOrder(), img.width, img.height,
		false, false);

	img.transparentPixel = src.transparentPixel;
	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

private static ImageData indexToDirect(ImageData src, int newDepth, PaletteData newPalette, int newByteOrder) {
	ImageData img = new ImageData(src.width, src.height, newDepth, newPalette);

	RGB[] rgbs = src.palette.getRGBs();
	byte[] srcReds   = new byte[rgbs.length];
	byte[] srcGreens = new byte[rgbs.length];
	byte[] srcBlues  = new byte[rgbs.length];
	for (int j = 0; j < rgbs.length; j++) {
		RGB rgb = rgbs[j];
		if (rgb == null) continue;
		srcReds[j] = (byte)rgb.red;
		srcGreens[j] = (byte)rgb.green;
		srcBlues[j] = (byte)rgb.blue;
	}

	ImageData.blit(
		src.width, src.height,
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), srcReds, srcGreens, srcBlues,
		img.data, img.depth, img.bytesPerLine,       newByteOrder, newPalette.redMask, newPalette.greenMask, newPalette.blueMask);

	if (src.transparentPixel != -1) {
		img.transparentPixel = newPalette.getPixel(src.palette.getRGB(src.transparentPixel));
	}

	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

private static ImageData directToDirect(ImageData src, int newDepth, PaletteData newPalette, int newByteOrder) {
	ImageData img = new ImageData(src.width, src.height, newDepth, newPalette);

	ImageData.blit(
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), src.width, src.height, src.palette.redMask, src.palette.greenMask, src.palette.blueMask,
		img.data, img.depth, img.bytesPerLine,       newByteOrder, img.width, img.height, img.palette.redMask, img.palette.greenMask, img.palette.blueMask,
		false, false);

	if (src.transparentPixel != -1) {
		img.transparentPixel = img.palette.getPixel(src.palette.getRGB(src.transparentPixel));
	}

	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

private record HandleForImageDataContainer(int type, ImageData imageData, long[] handles) {}

private static HandleForImageDataContainer init(Device device, ImageData i) {
	/* Windows does not support 2-bit images. Convert to 4-bit image. */
	if (i.depth == 2) {
		i = indexToIndex(i, 4);
	}

	/* Windows does not support 16-bit palette images. Convert to RGB. */
	if ((i.depth == 16) && !i.palette.isDirect) {
		PaletteData newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
		i = indexToDirect(i, 24, newPalette, ImageData.MSB_FIRST);
	}

	boolean hasAlpha = i.alpha != -1 || i.alphaData != null;

	/*
	 * Windows supports 16-bit mask of (0x7C00, 0x3E0, 0x1F),
	 * 24-bit mask of (0xFF0000, 0xFF00, 0xFF) and 32-bit mask
	 * (0x00FF0000, 0x0000FF00, 0x000000FF) as documented in
	 * MSDN BITMAPINFOHEADER.  Make sure the image is
	 * Windows-supported.
	 */
	if (i.palette.isDirect) {
		final PaletteData palette = i.palette;
		final int redMask = palette.redMask;
		final int greenMask = palette.greenMask;
		final int blueMask = palette.blueMask;
		int newDepth = i.depth;
		int newOrder = ImageData.MSB_FIRST;
		PaletteData newPalette = null;

		if (hasAlpha) {
			newDepth = 32;
			newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		}
		else {
			switch (i.depth) {
				case 8:
					/*
					 * Bug 566545. Usually each color mask selects a different part of the pixel
					 * value to encode the according color. In this common case it is rather trivial
					 * to convert an 8-bit direct color image to the Windows supported 16-bit image.
					 * However there is no enforcement for the color masks to be disjunct. For
					 * example an 8-bit image where all color masks select the same 8-bit of pixel
					 * value (mask = 0xFF and shift = 0 for all colors) results in a very efficient
					 * 8-bit gray-scale image without the need of defining a color table.
					 *
					 * That's why we need to calculate the actual required depth if all colors are
					 * stored non-overlapping which might require 24-bit instead of the usual
					 * expected 16-bit.
					 */
					int minDepth = ImageData.getChannelWidth(redMask, palette.redShift)
							+ ImageData.getChannelWidth(greenMask, palette.greenShift)
							+ ImageData.getChannelWidth(blueMask, palette.blueShift);
					if (minDepth <= 16) {
						newDepth = 16;
						newOrder = ImageData.LSB_FIRST;
						newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
					} else {
						newDepth = 24;
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					break;
				case 16:
					newOrder = ImageData.LSB_FIRST;
					if (!(redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F)) {
						newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
					}
					break;
				case 24:
					if (!(redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000)) {
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					break;
				case 32:
					if (i.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
						newDepth = 24;
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					else if (!(redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000)) {
						newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
					}
					break;
				default:
					SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
			}
		}
		if (newPalette != null) {
			i = directToDirect(i, newDepth, newPalette, newOrder);
		}
	}
	else if (hasAlpha) {
		PaletteData newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		i = indexToDirect(i, 32, newPalette, ImageData.MSB_FIRST);
	}
	if (i.alpha != -1) {
		int alpha = i.alpha & 0xFF;
		byte[] data = i.data;
		for (int dp = 0; dp < i.data.length; dp += 4) {
			/* pre-multiplied alpha */
			int r = ((data[dp    ] & 0xFF) * alpha) + 128;
			r = (r + (r >> 8)) >> 8;
			int g = ((data[dp + 1] & 0xFF) * alpha) + 128;
			g = (g + (g >> 8)) >> 8;
			int b = ((data[dp + 2] & 0xFF) * alpha) + 128;
			b = (b + (b >> 8)) >> 8;
			data[dp    ] = (byte) b;
			data[dp + 1] = (byte) g;
			data[dp + 2] = (byte) r;
			data[dp + 3] = (byte) alpha;
		}
	}
	else if (i.alphaData != null) {
		byte[] data = i.data;
		for (int ap = 0, dp = 0; dp < i.data.length; ap++, dp += 4) {
			/* pre-multiplied alpha */
			int a = i.alphaData[ap] & 0xFF;
			int r = ((data[dp    ] & 0xFF) * a) + 128;
			r = (r + (r >> 8)) >> 8;
			int g = ((data[dp + 1] & 0xFF) * a) + 128;
			g = (g + (g >> 8)) >> 8;
			int b = ((data[dp + 2] & 0xFF) * a) + 128;
			b = (b + (b >> 8)) >> 8;
			data[dp    ] = (byte) r;
			data[dp + 1] = (byte) g;
			data[dp + 2] = (byte) b;
			data[dp + 3] = (byte) a;
		}
	}

	/* Construct bitmap info header by hand */
	RGB[] rgbs = i.palette.getRGBs();
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = i.width;
	bmiHeader.biHeight = -i.height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)i.depth;
	bmiHeader.biCompression = OS.BI_RGB;
	bmiHeader.biClrUsed = rgbs == null ? 0 : rgbs.length;
	byte[] bmi;
	if (i.palette.isDirect)
		bmi = new byte[BITMAPINFOHEADER.sizeof];
	else
		bmi = new byte[BITMAPINFOHEADER.sizeof + rgbs.length * 4];
	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	/* Set the rgb colors into the bitmap info */
	int offset = BITMAPINFOHEADER.sizeof;
	if (!i.palette.isDirect) {
		for (RGB rgb : rgbs) {
			bmi[offset] = (byte)rgb.blue;
			bmi[offset + 1] = (byte)rgb.green;
			bmi[offset + 2] = (byte)rgb.red;
			bmi[offset + 3] = 0;
			offset += 4;
		}
	}
	long[] pBits = new long[1];
	long hDib = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (hDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	/* In case of a scanline pad other than 4, do the work to convert it */
	byte[] data = i.data;
	if (i.scanlinePad != 4 && (i.bytesPerLine % 4 != 0)) {
		data = ImageData.convertPad(data, i.width, i.height, i.depth, i.scanlinePad, 4);
	}
	OS.MoveMemory(pBits[0], data, data.length);

	if (i.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		/* Get the HDC for the device */
		long hDC = device.internal_new_GC(null);

		/* Create the color bitmap */
		long hdcSrc = OS.CreateCompatibleDC(hDC);
		OS.SelectObject(hdcSrc, hDib);
		long hBitmap = OS.CreateCompatibleBitmap(hDC, i.width, i.height);
		if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		long hdcDest = OS.CreateCompatibleDC(hDC);
		OS.SelectObject(hdcDest, hBitmap);
		OS.BitBlt(hdcDest, 0, 0, i.width, i.height, hdcSrc, 0, 0, OS.SRCCOPY);

		/* Release the HDC for the device */
		device.internal_dispose_GC(hDC, null);

		/* Create the mask. Windows requires icon masks to have a scanline pad of 2. */
		byte[] maskData = ImageData.convertPad(i.maskData, i.width, i.height, 1, i.maskPad, 2);
		long hMask = OS.CreateBitmap(i.width, i.height, 1, 1, maskData);
		if (hMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.SelectObject(hdcSrc, hMask);
		OS.PatBlt(hdcSrc, 0, 0, i.width, i.height, OS.DSTINVERT);
		OS.DeleteDC(hdcSrc);
		OS.DeleteDC(hdcDest);
		OS.DeleteObject(hDib);

		return new HandleForImageDataContainer(SWT.ICON, i, new long []{hBitmap, hMask});
	} else {
		return new HandleForImageDataContainer(SWT.BITMAP, i, new long []{hDib});
	}
}

private void setImageMetadataForHandle(ImageHandle imageMetadata, Integer zoom) {
	if (zoom == null)
		return;
	if (zoomLevelToImageHandle.containsKey(zoom)) {
		SWT.error(SWT.ERROR_ITEM_NOT_ADDED);
	}
	zoomLevelToImageHandle.put(zoom, imageMetadata);
}

private ImageHandle initIconHandle(Device device, ImageData source, ImageData mask, Integer zoom) {
	ImageData imageData = applyMask(source, mask);
	HandleForImageDataContainer imageDataHandle = init(device, imageData);
	return initIconHandle(imageDataHandle.handles, zoom);
}

private ImageHandle initIconHandle(long[] handles, int zoom) {
	/* Create the icon */
	ICONINFO info = new ICONINFO();
	info.fIcon = true;
	info.hbmColor = handles[0];
	info.hbmMask = handles[1];
	long hIcon = OS.CreateIconIndirect(info);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.DeleteObject(handles[0]);
	OS.DeleteObject(handles[1]);
	type = SWT.ICON;
	return new ImageHandle(hIcon, zoom);
}

private ImageHandle initBitmapHandle(ImageData imageData, long handle, Integer zoom) {
	type = SWT.BITMAP;
	transparentPixel = imageData.transparentPixel;
	return new ImageHandle(handle, zoom);
}

static long [] initIcon(Device device, ImageData source, ImageData mask) {
	ImageData imageData = applyMask(source, mask);
	return init(device, imageData).handles;
}

private static ImageData applyMask(ImageData source, ImageData mask) {
	/* Create a temporary image and locate the black pixel */
	ImageData imageData;
	int blackIndex = 0;
	if (source.palette.isDirect) {
		imageData = new ImageData(source.width, source.height, source.depth, source.palette);
	} else {
		RGB black = new RGB(0, 0, 0);
		RGB[] rgbs = source.getRGBs();
		if (source.transparentPixel != -1) {
			/*
			 * The source had transparency, so we can use the transparent pixel
			 * for black.
			 */
			RGB[] newRGBs = new RGB[rgbs.length];
			System.arraycopy(rgbs, 0, newRGBs, 0, rgbs.length);
			if (source.transparentPixel >= newRGBs.length) {
				/* Grow the palette with black */
				rgbs = new RGB[source.transparentPixel + 1];
				System.arraycopy(newRGBs, 0, rgbs, 0, newRGBs.length);
				for (int i = newRGBs.length; i <= source.transparentPixel; i++) {
					rgbs[i] = new RGB(0, 0, 0);
				}
			} else {
				newRGBs[source.transparentPixel] = black;
				rgbs = newRGBs;
			}
			blackIndex = source.transparentPixel;
			imageData = new ImageData(source.width, source.height, source.depth, new PaletteData(rgbs));
		} else {
			while (blackIndex < rgbs.length) {
				if (rgbs[blackIndex].equals(black)) break;
				blackIndex++;
			}
			if (blackIndex == rgbs.length) {
				/*
				 * We didn't find black in the palette, and there is no transparent
				 * pixel we can use.
				 */
				if ((1 << source.depth) > rgbs.length) {
					/* We can grow the palette and add black */
					RGB[] newRGBs = new RGB[rgbs.length + 1];
					System.arraycopy(rgbs, 0, newRGBs, 0, rgbs.length);
					newRGBs[rgbs.length] = black;
					rgbs = newRGBs;
				} else {
					/* No room to grow the palette */
					blackIndex = -1;
				}
			}
			imageData = new ImageData(source.width, source.height, source.depth, new PaletteData(rgbs));
		}
	}
	if (blackIndex == -1) {
		/* There was no black in the palette, so just copy the data over */
		System.arraycopy(source.data, 0, imageData.data, 0, imageData.data.length);
	} else {
		/* Modify the source image to contain black wherever the mask is 0 */
		int[] imagePixels = new int[imageData.width];
		int[] maskPixels = new int[mask.width];
		for (int y = 0; y < imageData.height; y++) {
			source.getPixels(0, y, imageData.width, imagePixels, 0);
			mask.getPixels(0, y, mask.width, maskPixels, 0);
			for (int i = 0; i < imagePixels.length; i++) {
				if (maskPixels[i] == 0) imagePixels[i] = blackIndex;
			}
			imageData.setPixels(0, y, source.width, imagePixels, 0);
		}
	}
	imageData.maskPad = mask.scanlinePad;
	imageData.maskData = mask.data;
	return imageData;
}


private ImageHandle init(ImageData i, int zoom) {
	if (i == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	HandleForImageDataContainer imageDataHandle = init(device, i);
	switch (imageDataHandle.type()) {
		case SWT.ICON: {
			return initIconHandle(imageDataHandle.handles(), zoom);
		}
		case SWT.BITMAP: {
			return initBitmapHandle(imageDataHandle.imageData(), imageDataHandle.handles()[0], zoom);
		}
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			return null;
	}
}

/**
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data
 * @return the platform specific GC handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public long internal_new_GC (GCData data) {
	return this.imageProvider.configureGCData(data);
}

private long configureGC(GCData data, int zoom) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* Create a new GC that can draw into the image.
	* Only supported for bitmaps.
	*/
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	if (Device.strictChecks) {
		checkImageTypeForValidCustomDrawing(zoom);
	}
	/* Create a compatible HDC for the device */
	long hDC = device.internal_new_GC(null);
	long imageDC = OS.CreateCompatibleDC(hDC);
	device.internal_dispose_GC(hDC, null);
	if (imageDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	if (data != null) {
		/* Set the GCData fields */
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) != 0) {
			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
		} else {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = device;
		data.nativeZoom = zoom;
		data.image = this;
		data.font = SWTFontProvider.getSystemFont(device, zoom);
	}
	return imageDC;
}

private void checkImageTypeForValidCustomDrawing(int zoom) {
	String replacementInfo = "It should be created with an ImageGcDrawer (see SWT Snippet 384).";
	if (imageProvider instanceof ImageDataProviderWrapper || imageProvider instanceof ImageFileNameProviderWrapper) {
		String message = "***WARNING: Image initialized with ImageDataProvider or ImageFileNameProvider is not supposed to be modified.";
		System.err.println(message + " " + replacementInfo);
	} else if (!zoomLevelToImageHandle.isEmpty()
			&& (zoomLevelToImageHandle.size() != 1 || !zoomLevelToImageHandle.containsKey(zoom))) {
		String message = "***WARNING: Images with handles created for multiple zooms should not be modified. ";
		System.err.println(message + " " + replacementInfo);
	}
}

/**
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public void internal_dispose_GC (long hDC, GCData data) {
	OS.DeleteDC(hDC);
}

/**
 * Returns <code>true</code> if the image has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the image.
 * When an image has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the image.
 *
 * @return <code>true</code> when the image is disposed and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return !isInitialized || isDestroyed;
}

/**
 * Sets the color to which to map the transparent pixel.
 * <p>
 * There are certain uses of <code>Images</code> that do not support
 * transparency (for example, setting an image into a button or label).
 * In these cases, it may be desired to simulate transparency by using
 * the background color of the widget to paint the transparent pixels
 * of the image. This method specifies the color that will be used in
 * these cases. For example:</p>
 * <pre>
 *    Button b = new Button();
 *    image.setBackground(b.getBackground());
 *    b.setImage(image);
 * </pre>
 * <p>
 * The image may be modified by this operation (in effect, the
 * transparent regions may be filled with the supplied color).  Hence
 * this operation is not reversible and it is not legal to call
 * this function twice or with a null argument.
 * </p><p>
 * This method has no effect if the receiver does not have a transparent
 * pixel value.
 * </p>
 *
 * @param color the color to use when a transparent pixel is specified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setBackground(Color color) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (transparentPixel == -1) return;
	transparentColor = -1;
	backgroundColor = color.getRGB();
	zoomLevelToImageHandle.values().forEach(imageHandle -> imageHandle.setBackground(backgroundColor));
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	return "Image {" + zoomLevelToImageHandle + "}";
}

<T> T applyUsingAnyHandle(Function<ImageHandle, T> function) {
	if (zoomLevelToImageHandle.isEmpty()) {
		ImageHandle temporaryHandle = this.imageProvider.newImageHandle(DPIUtil.getDeviceZoom());
		try {
			return function.apply(temporaryHandle);
		} finally {
			temporaryHandle.destroy();
		}
	}
	return function.apply(zoomLevelToImageHandle.values().iterator().next());
}

/**
 * Invokes platform specific functionality to allocate a new image.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param type the type of the image (<code>SWT.BITMAP</code> or <code>SWT.ICON</code>)
 * @param handle the OS handle for the image
 * @return a new image object containing the specified device, type and handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Image win32_new(Device device, int type, long handle, int nativeZoom) {
	return new Image(device, type, handle, nativeZoom);
}

private abstract class AbstractImageProviderWrapper {

	protected abstract Rectangle getBounds(int zoom);

	protected long configureGCData(GCData data) {
		return configureGC(data, 100);
	}

	public Collection<Integer> getPreservedZoomLevels() {
		return Collections.emptySet();
	}

	abstract ImageData newImageData(int zoom);

	abstract AbstractImageProviderWrapper createCopy(Image image);

	ImageData getScaledImageData (int zoom) {
		TreeSet<Integer> availableZooms = new TreeSet<>(zoomLevelToImageHandle.keySet());
		int closestZoom = Optional.ofNullable(availableZooms.higher(zoom)).orElse(availableZooms.lower(zoom));
		return DPIUtil.scaleImageData(device, getImageMetadata(closestZoom).getImageData(), zoom, closestZoom);
	}

	protected ImageHandle newImageHandle(int zoom) {
		ImageData resizedData = getImageData(zoom);
		return newImageHandle(resizedData, zoom);
	}

	protected final ImageHandle newImageHandle(ImageData data, int zoom) {
		if (type == SWT.ICON && data.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			// If the original type was an icon with transparency mask and re-scaling leads
			// to image data without transparency mask, this will create invalid images
			// so this fallback will "repair" the image data by explicitly passing
			// the transparency mask created from the scaled image data
			return initIconHandle(device, data, data.getTransparencyMask(), zoom);
		} else {
			return init(data, zoom);
		}
	}
}

private class ExistingImageHandleProviderWrapper extends AbstractImageProviderWrapper {

	private final int width;
	private final int height;
	private final long handle;
	private final int zoomForHandle;

	public ExistingImageHandleProviderWrapper(long handle, int zoomForHandle) {
		this.handle = handle;
		this.zoomForHandle = zoomForHandle;
		ImageHandle imageHandle = new ImageHandle(handle, zoomForHandle);

		ImageData baseData = imageHandle.getImageData();
		this.width = DPIUtil.pixelToPoint(baseData.width, zoomForHandle);
		this.height = DPIUtil.pixelToPoint(baseData.height, zoomForHandle);
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		Rectangle rectangle = new Rectangle(0, 0, width, height);
		return Win32DPIUtils.pointToPixel(rectangle, zoom);
	}

	@Override
	ImageData newImageData(int zoom) {
		return getScaledImageData(zoom);
	}

	@Override
	AbstractImageProviderWrapper createCopy(Image image) {
		return image.new ExistingImageHandleProviderWrapper(handle, zoomForHandle);
	}

	@Override
	public Collection<Integer> getPreservedZoomLevels() {
		return Collections.singleton(zoomForHandle);
	}
}

private abstract class ImageFromImageDataProviderWrapper extends AbstractImageProviderWrapper {
	private final Map<Integer, ImageData> cachedImageData = new HashMap<>();

	protected abstract ElementAtZoom<ImageData> loadImageData(int zoom);

	void initImage() {
		// As the init call configured some Image attributes (e.g. type)
		// it must be called
		newImageData(100);
	}

	@Override
	ImageData newImageData(int zoom) {
		Function<Integer, ImageData> imageDataRetrieval = zoomToRetrieve -> {
			ImageHandle handle = initializeHandleFromSource(zoomToRetrieve);
			ImageData data = handle.getImageData();
			handle.destroy();
			return data;
		};
		return cachedImageData.computeIfAbsent(zoom, imageDataRetrieval);
	}

	@Override
	protected ImageHandle newImageHandle(int zoom) {
		ImageData cachedData = cachedImageData.remove(zoom);
		if (cachedData != null) {
			return newImageHandle(cachedData, zoom);
		}
		return initializeHandleFromSource(zoom);
	}

	private ImageHandle initializeHandleFromSource(int zoom) {
		ElementAtZoom<ImageData> imageDataAtZoom = loadImageData(zoom);
		ImageData imageData = DPIUtil.scaleImageData(device, imageDataAtZoom.element(), zoom, imageDataAtZoom.zoom());
		imageData = adaptImageDataIfDisabledOrGray(imageData);
		return newImageHandle(imageData, zoom);
	}

}

private class PlainImageDataProviderWrapper extends ImageFromImageDataProviderWrapper {
	private ImageData imageDataAtBaseZoom;
	private int baseZoom;

	PlainImageDataProviderWrapper(ImageData imageData) {
		this(imageData, 100);
	}

	private PlainImageDataProviderWrapper(ImageData imageData, int zoom) {
		this.imageDataAtBaseZoom = (ImageData) imageData.clone();
		this.baseZoom = zoom;
		initImage();
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		Rectangle rectangle = new Rectangle(0, 0, imageDataAtBaseZoom.width, imageDataAtBaseZoom.height);
		rectangle = Win32DPIUtils.pixelToPoint(rectangle, baseZoom);
		return Win32DPIUtils.pointToPixel(rectangle, zoom);
	}

	@Override
	protected ElementAtZoom<ImageData> loadImageData(int zoom) {
		return new ElementAtZoom<>(imageDataAtBaseZoom, baseZoom);
	}

	@Override
	AbstractImageProviderWrapper createCopy(Image image) {
		return image.new PlainImageDataProviderWrapper(this.imageDataAtBaseZoom);
	}
}

private class MaskedImageDataProviderWrapper extends ImageFromImageDataProviderWrapper {
	private final ImageData srcAt100;
	private final ImageData maskAt100;

	MaskedImageDataProviderWrapper(ImageData srcAt100, ImageData maskAt100) {
		this.srcAt100 = (ImageData) srcAt100.clone();
		this.maskAt100 = (ImageData) maskAt100.clone();
		initImage();
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		Rectangle rectangle = new Rectangle(0, 0, srcAt100.width, srcAt100.height);
		return Win32DPIUtils.pointToPixel(rectangle, zoom);
	}

	@Override
	protected ElementAtZoom<ImageData> loadImageData(int zoom) {
		ImageData scaledSource = DPIUtil.scaleImageData(device, srcAt100, zoom, 100);
		ImageData scaledMask = DPIUtil.scaleImageData(device, maskAt100, zoom, 100);
		scaledMask = ImageData.convertMask(scaledMask);
		ImageData mergedData = applyMask(scaledSource, scaledMask);
		return new ElementAtZoom<>(mergedData, zoom);
	}

	@Override
	AbstractImageProviderWrapper createCopy(Image image) {
		return image.new MaskedImageDataProviderWrapper(this.srcAt100, this.maskAt100);
	}
}

private class ImageDataLoaderStreamProviderWrapper extends ImageFromImageDataProviderWrapper {
	private byte[] inputStreamData;

	ImageDataLoaderStreamProviderWrapper(InputStream inputStream) {
		try {
			this.inputStreamData = inputStream.readAllBytes();
			initImage();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, e);
		}
	}

	private ImageDataLoaderStreamProviderWrapper(byte[] inputStreamData) {
		this.inputStreamData = inputStreamData;
	}

	@Override
	protected ElementAtZoom<ImageData> loadImageData(int zoom) {
		return ImageDataLoader.load(new ByteArrayInputStream(inputStreamData), FileFormat.DEFAULT_ZOOM, zoom);
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		ImageData scaledImageData = getImageData(zoom);
		return new Rectangle(0, 0, scaledImageData.width, scaledImageData.height);
	}

	@Override
	AbstractImageProviderWrapper createCopy(Image image) {
		return image.new ImageDataLoaderStreamProviderWrapper(inputStreamData);
	}
}

private class PlainImageProviderWrapper extends AbstractImageProviderWrapper {
	private final int width;
	private final int height;
	private int baseZoom;

	PlainImageProviderWrapper(int width, int height) {
		if (width <= 0 || height <= 0) {
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		this.width = width;
		this.height = height;
		type = SWT.BITMAP;
	}

	@Override
	public Collection<Integer> getPreservedZoomLevels() {
		return Collections.singleton(baseZoom);
	}

	@Override
	protected long configureGCData(GCData data) {
		return configureGC(data, DPIUtil.getDeviceZoom());
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		Rectangle rectangle = new Rectangle(0, 0, width, height);
		return Win32DPIUtils.pointToPixel(rectangle, zoom);
	}

	@Override
	ImageData newImageData(int zoom) {
		if (zoomLevelToImageHandle.isEmpty()) {
			return createBaseHandle(zoom).getImageData();
		}
		// if a GC is initialized with an Image (memGC != null), the image data must not be resized, because it would
		// be a destructive operation. Therefor, a new handle is created for the requested zoom
		if (memGC != null) {
			return newImageHandle(zoom).getImageData();
		}
		return getScaledImageData(zoom);
	}

	@Override
	protected ImageHandle newImageHandle(int zoom) {
		if (zoomLevelToImageHandle.isEmpty()) {
			return createBaseHandle(zoom);
		}
		if (memGC != null) {
			GC currentGC = memGC;
			memGC = null;
			createHandle(zoom);
			currentGC.refreshFor(new DrawableWrapper(Image.this, zoom), zoom);
			return zoomLevelToImageHandle.get(zoom);
		}
		return super.newImageHandle(zoom);
	}
	private ImageHandle createBaseHandle(int zoom) {
		baseZoom = zoom;
		return createHandle(zoom);
	}

	private ImageHandle createHandle(int zoom) {
		long handle = initHandle(zoom);
		ImageHandle imageHandle = new ImageHandle(handle, zoom);
		zoomLevelToImageHandle.put(zoom, imageHandle);
		return imageHandle;
	}

	private long initHandle(int zoom) {
		if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		int scaledWidth = Win32DPIUtils.pointToPixel (width, zoom);
		int scaledHeight = Win32DPIUtils.pointToPixel (height, zoom);
		long hDC = device.internal_new_GC(null);
		long newHandle = OS.CreateCompatibleBitmap(hDC, scaledWidth, scaledHeight);
		/*
		* Feature in Windows.  CreateCompatibleBitmap() may fail
		* for large images.  The fix is to create a DIB section
		* in that case.
		*/
		if (newHandle == 0) {
			int bits = OS.GetDeviceCaps(hDC, OS.BITSPIXEL);
			int planes = OS.GetDeviceCaps(hDC, OS.PLANES);
			int depth = bits * planes;
			if (depth < 16) depth = 16;
			if (depth > 24) depth = 24;
			newHandle = createDIB(scaledWidth, scaledHeight, depth);
		}
		if (newHandle != 0) {
			long memDC = OS.CreateCompatibleDC(hDC);
			long hOldBitmap = OS.SelectObject(memDC, newHandle);
			OS.PatBlt(memDC, 0, 0, scaledWidth, scaledHeight, OS.PATCOPY);
			OS.SelectObject(memDC, hOldBitmap);
			OS.DeleteDC(memDC);
		}
		device.internal_dispose_GC(hDC, null);
		if (newHandle == 0) {
			SWT.error(SWT.ERROR_NO_HANDLES, null, device.getLastError());
		}
		return newHandle;
	}

	@Override
	AbstractImageProviderWrapper createCopy(Image image) {
		return image.new PlainImageProviderWrapper(width, height);
	}
}

private abstract class DynamicImageProviderWrapper extends AbstractImageProviderWrapper {
	abstract Object getProvider();

	protected void checkProvider(Object provider, Class<?> expectedClass) {
		if (provider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (!expectedClass.isAssignableFrom(provider.getClass())) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	@Override
	public int hashCode() {
		return getProvider().hashCode();
	}

	@Override
	public boolean equals(Object otherProvider) {
		return otherProvider instanceof DynamicImageProviderWrapper aip
				&& Objects.equals(getProvider(), aip.getProvider());
	}
}

private abstract class BaseImageProviderWrapper<T> extends DynamicImageProviderWrapper {
	private final Map<Integer, ImageData> cachedImageData = new HashMap<>();

	protected final T provider;

	BaseImageProviderWrapper(T provider, Class<T> expectedClass) {
		checkProvider(provider, expectedClass);
		this.provider = provider;
	}

	@Override
	Object getProvider() {
		return provider;
	}

	@Override
	ImageData newImageData(int zoom) {
		Function<Integer, ImageData> imageDataRetrival = zoomToRetrieve -> {
			ImageHandle handle = initializeHandleFromSource(zoomToRetrieve);
			ImageData data = handle.getImageData();
			handle.destroy();
			return data;
		};
		return cachedImageData.computeIfAbsent(zoom, imageDataRetrival);
	}


	@Override
	protected ImageHandle newImageHandle(int zoom) {
		ImageData cachedData = cachedImageData.remove(zoom);
		if (cachedData != null) {
			return init(cachedData, zoom);
		}
		return initializeHandleFromSource(zoom);
	}

	private ImageHandle initializeHandleFromSource(int zoom) {
		ElementAtZoom<ImageData> imageDataAtZoom = loadImageData(zoom);
		ImageData imageData = DPIUtil.scaleImageData (device,imageDataAtZoom.element(), zoom, imageDataAtZoom.zoom());
		imageData = adaptImageDataIfDisabledOrGray(imageData);
		return init(imageData, zoom);
	}

	protected abstract ElementAtZoom<ImageData> loadImageData(int zoom);

	@Override
	protected Rectangle getBounds(int zoom) {
		ImageData imageData = getImageData(zoom);
		return new Rectangle(0, 0, imageData.width, imageData.height);
	}
}

private class ImageFileNameProviderWrapper extends BaseImageProviderWrapper<ImageFileNameProvider> {
	ImageFileNameProviderWrapper(ImageFileNameProvider provider) {
		super(provider, ImageFileNameProvider.class);
		// Checks for the contract of the passed provider require
		// checking for valid image data creation
		newImageData(DPIUtil.getDeviceZoom());
	}

	@Override
	protected ElementAtZoom<ImageData> loadImageData(int zoom) {
		ElementAtZoom<String> fileForZoom = DPIUtil.validateAndGetImagePathAtZoom(provider, zoom);

		// Load at appropriate zoom via loader
		if (fileForZoom.zoom() != zoom && ImageDataLoader.canLoadAtZoom(fileForZoom.element(), fileForZoom.zoom(), zoom)) {
			ElementAtZoom<ImageData> imageDataAtZoom = ImageDataLoader.load(fileForZoom.element(), fileForZoom.zoom(), zoom);
			return new ElementAtZoom<>(imageDataAtZoom.element(), zoom);
		}

		// Load at file zoom (native or via loader) and rescale
		ImageHandle nativeInitializedImage;
		if (zoomLevelToImageHandle.containsKey(fileForZoom.zoom())) {
			nativeInitializedImage = zoomLevelToImageHandle.get(fileForZoom.zoom());
		} else {
			nativeInitializedImage = initNative(fileForZoom.element(), fileForZoom.zoom());
		}
		ElementAtZoom<ImageData> imageDataAtZoom;
		if (nativeInitializedImage == null) {
			imageDataAtZoom = ImageDataLoader.load(fileForZoom.element(), fileForZoom.zoom(), zoom);
		} else {
			imageDataAtZoom = new ElementAtZoom<>(nativeInitializedImage.getImageData(), fileForZoom.zoom());
			nativeInitializedImage.destroy();
		}
		return imageDataAtZoom;
	}

	@Override
	public int hashCode() {
		return Objects.hash(provider, styleFlag, transparentPixel);
	}

	@Override
	ImageFileNameProviderWrapper createCopy(Image image) {
		return image.new ImageFileNameProviderWrapper(provider);
	}

	ImageHandle initNative(String filename, int zoom) {
		ImageHandle imageMetadata = null;
		long handle = 0;
		int width = -1;
		int height = -1;
		device.checkGDIP();
		boolean gdip = true;
		/*
		* Bug in GDI+.  For some reason, Bitmap.LockBits() segment faults
		* when loading GIF files in 64-bit Windows.  The fix is to not use
		* GDI+ image loading in this case.
		*/
		if (gdip && C.PTR_SIZEOF == 8 && filename.toLowerCase().endsWith(".gif")) gdip = false;
		/*
		* Bug in GDI+. Bitmap.LockBits() fails to load GIF files in
		* Windows 7 when the image has a position offset in the first frame.
		* The fix is to not use GDI+ image loading in this case.
		*/
		if (filename.toLowerCase().endsWith(".gif")) gdip = false;

		if(!gdip) return null;

		int length = filename.length();
		char[] chars = new char[length+1];
		filename.getChars(0, length, chars, 0);
		long bitmap = Gdip.Bitmap_new(chars, false);
		if (bitmap == 0) return null;

		int error = SWT.ERROR_NO_HANDLES;
		int status = Gdip.Image_GetLastStatus(bitmap);
		if (status == 0) {
			if (filename.toLowerCase().endsWith(".ico")) {
				type = SWT.ICON;
				long[] hicon = new long[1];
				status = Gdip.Bitmap_GetHICON(bitmap, hicon);
				handle = hicon[0];
				imageMetadata = new ImageHandle(handle, zoom);
			} else {
				type = SWT.BITMAP;
				width = Gdip.Image_GetWidth(bitmap);
				height = Gdip.Image_GetHeight(bitmap);
				int pixelFormat = Gdip.Image_GetPixelFormat(bitmap);
				handle = extractHandleForPixelFormat(width, height, pixelFormat);
				if (handle != 0) {
					/*
					* This performs better than getting the bits with Bitmap.LockBits(),
					* but it cannot be used when there is transparency.
					*/
					long hDC = device.internal_new_GC(null);
					long srcHDC = OS.CreateCompatibleDC(hDC);
					long oldSrcBitmap = OS.SelectObject(srcHDC, handle);
					long graphics = Gdip.Graphics_new(srcHDC);
					if (graphics != 0) {
						Rect rect = new Rect();
						rect.Width = width;
						rect.Height = height;
						status = Gdip.Graphics_DrawImage(graphics, bitmap, rect, 0, 0, width, height, Gdip.UnitPixel, 0, 0, 0);
						if (status != 0) {
							error = SWT.ERROR_INVALID_IMAGE;
							OS.DeleteObject(handle);
							handle = 0;
						}
						Gdip.Graphics_delete(graphics);
					}
					OS.SelectObject(srcHDC, oldSrcBitmap);
					OS.DeleteDC(srcHDC);
					device.internal_dispose_GC(hDC, null);
					imageMetadata = new ImageHandle(handle, zoom);
				} else {
					long lockedBitmapData = Gdip.BitmapData_new();
					if (lockedBitmapData != 0) {
						status = Gdip.Bitmap_LockBits(bitmap, 0, 0, pixelFormat, lockedBitmapData);
						if (status == 0) {
							BitmapData bitmapData = new BitmapData();
							Gdip.MoveMemory(bitmapData, lockedBitmapData);
							int stride = bitmapData.Stride;
							long pixels = bitmapData.Scan0;
							int depth = 0, scanlinePad = 4, transparentPixel = -1;
							depth = extractDepthForPixelFormat(bitmapData);
							if (depth != 0) {
								PaletteData paletteData = null;
								switch (bitmapData.PixelFormat) {
									case Gdip.PixelFormat1bppIndexed:
									case Gdip.PixelFormat4bppIndexed:
									case Gdip.PixelFormat8bppIndexed:
										int paletteSize = Gdip.Image_GetPaletteSize(bitmap);
										long hHeap = OS.GetProcessHeap();
										long palette = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, paletteSize);
										if (palette == 0) SWT.error(SWT.ERROR_NO_HANDLES);
										Gdip.Image_GetPalette(bitmap, palette, paletteSize);
										ColorPalette colorPalette = new ColorPalette();
										Gdip.MoveMemory(colorPalette, palette, ColorPalette.sizeof);
										int[] entries = new int[colorPalette.Count];
										OS.MoveMemory(entries, palette + 8, entries.length * 4);
										OS.HeapFree(hHeap, 0, palette);
										RGB[] rgbs = new RGB[colorPalette.Count];
										paletteData = new PaletteData(rgbs);
										for (int i = 0; i < entries.length; i++) {
											if (((entries[i] >> 24) & 0xFF) == 0 && (colorPalette.Flags & Gdip.PaletteFlagsHasAlpha) != 0) {
												transparentPixel = i;
											}
											rgbs[i] = new RGB(((entries[i] & 0xFF0000) >> 16), ((entries[i] & 0xFF00) >> 8), ((entries[i] & 0xFF) >> 0));
										}
										break;
									case Gdip.PixelFormat16bppARGB1555:
									case Gdip.PixelFormat16bppRGB555: paletteData = new PaletteData(0x7C00, 0x3E0, 0x1F); break;
									case Gdip.PixelFormat16bppRGB565: paletteData = new PaletteData(0xF800, 0x7E0, 0x1F); break;
									case Gdip.PixelFormat24bppRGB: paletteData = new PaletteData(0xFF, 0xFF00, 0xFF0000); break;
									case Gdip.PixelFormat32bppRGB:
									case Gdip.PixelFormat32bppARGB: paletteData = new PaletteData(0xFF00, 0xFF0000, 0xFF000000); break;
								}
								byte[] data = new byte[stride * height], alphaData = null;
								OS.MoveMemory(data, pixels, data.length);
								switch (bitmapData.PixelFormat) {
									case Gdip.PixelFormat16bppARGB1555:
										alphaData = new byte[width * height];
										for (int i = 1, j = 0; i < data.length; i += 2, j++) {
											alphaData[j] = (byte)((data[i] & 0x80) != 0 ? 255 : 0);
										}
										break;
									case Gdip.PixelFormat32bppARGB:
										alphaData = new byte[width * height];
										for (int i = 3, j = 0; i < data.length; i += 4, j++) {
											alphaData[j] = data[i];
										}
										break;
								}
								ImageData img = new ImageData(width, height, depth, paletteData, scanlinePad, data);
								img.transparentPixel = transparentPixel;
								img.alphaData = alphaData;

								init(img, zoom);
								imageMetadata = zoomLevelToImageHandle.get(zoom);
								handle = imageMetadata.handle;
							}
							Gdip.Bitmap_UnlockBits(bitmap, lockedBitmapData);
						} else {
							error = SWT.ERROR_INVALID_IMAGE;
						}
						Gdip.BitmapData_delete(lockedBitmapData);
					}
				}
			}
		}
		Gdip.Bitmap_delete(bitmap);
		if (status == 0) {
			if (handle == 0) SWT.error(error);
			if (imageMetadata == null) SWT.error(error);
		}

		return imageMetadata;
	}

	private int extractDepthForPixelFormat(BitmapData bitmapData) {
		int depth = 0;
		switch (bitmapData.PixelFormat) {
			case Gdip.PixelFormat1bppIndexed: depth = 1; break;
			case Gdip.PixelFormat4bppIndexed: depth = 4; break;
			case Gdip.PixelFormat8bppIndexed: depth = 8; break;
			case Gdip.PixelFormat16bppARGB1555:
			case Gdip.PixelFormat16bppRGB555:
			case Gdip.PixelFormat16bppRGB565: depth = 16; break;
			case Gdip.PixelFormat24bppRGB: depth = 24; break;
			case Gdip.PixelFormat32bppRGB:
			case Gdip.PixelFormat32bppARGB: depth = 32; break;
		}
		return depth;
	}

	private long extractHandleForPixelFormat(int width, int height, int pixelFormat) {
		long handle = 0;
		switch (pixelFormat) {
			case Gdip.PixelFormat16bppRGB555:
			case Gdip.PixelFormat16bppRGB565:
				handle = createDIB(width, height, 16);
				break;
			case Gdip.PixelFormat24bppRGB:
			case Gdip.PixelFormat32bppCMYK:
				handle = createDIB(width, height, 24);
				break;
			case Gdip.PixelFormat32bppRGB:
			// These will lose either precision or transparency
			case Gdip.PixelFormat16bppGrayScale:
			case Gdip.PixelFormat48bppRGB:
			case Gdip.PixelFormat32bppPARGB:
			case Gdip.PixelFormat64bppARGB:
			case Gdip.PixelFormat64bppPARGB:
				handle = createDIB(width, height, 32);
				break;
		}
		return handle;
	}
}

private class ImageDataProviderWrapper extends BaseImageProviderWrapper<ImageDataProvider> {
	ImageDataProviderWrapper(ImageDataProvider provider) {
		super(provider, ImageDataProvider.class);
	}

	@Override
	protected ElementAtZoom<ImageData> loadImageData(int zoom) {
		return DPIUtil.validateAndGetImageDataAtZoom (provider, zoom);
	}

	@Override
	ImageDataProviderWrapper createCopy(Image image) {
		return image.new ImageDataProviderWrapper(provider);
	}
}

private class ImageGcDrawerWrapper extends DynamicImageProviderWrapper {
	private ImageGcDrawer drawer;
	private int width;
	private int height;
	private int currentZoom = 100;

	ImageGcDrawerWrapper(ImageGcDrawer imageGcDrawer, int width, int height) {
		checkProvider(imageGcDrawer, ImageGcDrawer.class);
		this.drawer = imageGcDrawer;
		this.width = width;
		this.height = height;
	}

	@Override
	protected Rectangle getBounds(int zoom) {
		Rectangle rectangle = new Rectangle(0, 0, width, height);
		return Win32DPIUtils.scaleBounds(rectangle, zoom, 100);
	}

	@Override
	protected long configureGCData(GCData data) {
		return configureGC(data, currentZoom);
	}

	@Override
	ImageData newImageData(int zoom) {
		return getImageMetadata(zoom).getImageData();
	}

	@Override
	protected ImageHandle newImageHandle(int zoom) {
		currentZoom = zoom;
		int gcStyle = drawer.getGcStyle();
		Image image;
		if ((gcStyle & SWT.TRANSPARENT) != 0) {
			int scaledHeight = Win32DPIUtils.pointToPixel(height, zoom);
			int scaledWidth = Win32DPIUtils.pointToPixel(width, zoom);
			/* Create a 24 bit image data with alpha channel */
			final ImageData resultData = new ImageData (scaledWidth, scaledHeight, 24, new PaletteData (0xFF, 0xFF00, 0xFF0000));
			resultData.alphaData = new byte [scaledWidth * scaledHeight];
			image = new Image(device, resultData, zoom);
		} else {
			image = new Image(device, width, height);
		}
		GC gc = new GC(new DrawableWrapper(image, zoom), gcStyle);
		try {
			drawer.drawOn(gc, width, height);
			ImageData imageData = image.getImageMetadata(zoom).getImageData();
			drawer.postProcess(imageData);
			ImageData newData = adaptImageDataIfDisabledOrGray(imageData);
			return init(newData, zoom);
		} finally {
			gc.dispose();
			image.dispose();
		}
	}

	@Override
	Object getProvider() {
		return drawer;
	}

	@Override
	ImageGcDrawerWrapper createCopy(Image image) {
		return image.new ImageGcDrawerWrapper(drawer, width, height);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProvider().hashCode(), width, height);
	}

	@Override
	public boolean equals(Object otherProvider) {
		return otherProvider instanceof ImageGcDrawerWrapper aip && getProvider().equals(aip.getProvider())
				&& width == aip.width && height == aip.height;
	}
}

private static class DrawableWrapper implements Drawable {
	private final Image image;
	private final int zoom;

	public DrawableWrapper(Image image, int zoom) {
		this.image = image;
		this.zoom = zoom;
	}

	@Override
	public long internal_new_GC(GCData data) {
		return this.image.configureGC(data, zoom);
	}

	@Override
	public void internal_dispose_GC(long handle, GCData data) {
		this.image.internal_dispose_GC(handle, data);
	}
}

private class ImageHandle {
	private long handle;
	private final int zoom;
	private int height;
	private int width;

	public ImageHandle(long handle, int zoom) {
		this.handle = handle;
		this.zoom = zoom;
		updateBoundsInPixelsFromNative();
		if (backgroundColor != null) {
			setBackground(backgroundColor);
		}
		setImageMetadataForHandle(this, zoom);
	}

	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}

	private void setBackground(RGB color) {
		if (transparentPixel == -1) return;

		/* Get the HDC for the device */
		long hDC = device.internal_new_GC(null);

		/* Change the background color in the image */
		BITMAP bm = new BITMAP();
		OS.GetObject(handle, BITMAP.sizeof, bm);
		long hdcMem = OS.CreateCompatibleDC(hDC);
		OS.SelectObject(hdcMem, handle);
		int maxColors = 1 << bm.bmBitsPixel;
		byte[] colors = new byte[maxColors * 4];
		int numColors = OS.GetDIBColorTable(hdcMem, 0, maxColors, colors);
		int offset = transparentPixel * 4;
		colors[offset] = (byte)color.blue;
		colors[offset + 1] = (byte)color.green;
		colors[offset + 2] = (byte)color.red;
		OS.SetDIBColorTable(hdcMem, 0, numColors, colors);
		OS.DeleteDC(hdcMem);

		/* Release the HDC for the device */
		device.internal_dispose_GC(hDC, null);
	}

	private void updateBoundsInPixelsFromNative() {
		switch (type) {
		case SWT.BITMAP:
			BITMAP bm = new BITMAP();
			OS.GetObject(handle, BITMAP.sizeof, bm);
			width = bm.bmWidth;
			height = bm.bmHeight;
			return;
		case SWT.ICON:
			ICONINFO info = new ICONINFO();
			OS.GetIconInfo(handle, info);
			long hBitmap = info.hbmColor;
			if (hBitmap == 0) hBitmap = info.hbmMask;
			bm = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
			if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
			if (info.hbmColor != 0) OS.DeleteObject(info.hbmColor);
			if (info.hbmMask != 0) OS.DeleteObject(info.hbmMask);
			width = bm.bmWidth;
			height = bm.bmHeight;
			return;
		default:
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
	}

	private ImageData getImageData() {
		if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		BITMAP bm;
		int depth, width, height;
		switch (type) {
			case SWT.ICON: {
				ICONINFO info = new ICONINFO();
				OS.GetIconInfo(handle, info);
				/* Get the basic BITMAP information */
				long hBitmap = info.hbmColor;
				if (hBitmap == 0) hBitmap = info.hbmMask;
				bm = new BITMAP();
				OS.GetObject(hBitmap, BITMAP.sizeof, bm);
				depth = bm.bmPlanes * bm.bmBitsPixel;
				width = bm.bmWidth;
				if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
				height = bm.bmHeight;
				int numColors = 0;
				if (depth <= 8) numColors = 1 << depth;
				/* Create the BITMAPINFO */
				BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
				bmiHeader.biWidth = width;
				bmiHeader.biHeight = -height;
				bmiHeader.biPlanes = 1;
				bmiHeader.biBitCount = (short)depth;
				bmiHeader.biCompression = OS.BI_RGB;
				byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);

				/* Get the HDC for the device */
				long hDC = device.internal_new_GC(null);

				/* Create the DC and select the bitmap */
				long hBitmapDC = OS.CreateCompatibleDC(hDC);
				long hOldBitmap = OS.SelectObject(hBitmapDC, hBitmap);
				/* Find the size of the image and allocate data */
				int imageSize;
				/* Call with null lpBits to get the image size */
				OS.GetDIBits(hBitmapDC, hBitmap, 0, height, null, bmi, OS.DIB_RGB_COLORS);
				OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
				imageSize = bmiHeader.biSizeImage;
				byte[] data = new byte[imageSize];
				/* Get the bitmap data */
				OS.GetDIBits(hBitmapDC, hBitmap, 0, height, data, bmi, OS.DIB_RGB_COLORS);
				/* Calculate the palette */
				PaletteData palette = null;
				if (depth <= 8) {
					RGB[] rgbs = new RGB[numColors];
					int srcIndex = 40;
					for (int i = 0; i < numColors; i++) {
						rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
						srcIndex += 4;
					}
					palette = new PaletteData(rgbs);
				} else if (depth == 16) {
					palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
				} else if (depth == 24) {
					palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				} else if (depth == 32) {
					palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
				} else {
					SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
				}

				/* Do the mask */
				byte [] maskData = null;
				byte [] alphaData = null;
				if (info.hbmColor == 0) {
					/* Do the bottom half of the mask */
					maskData = new byte[imageSize];
					OS.GetDIBits(hBitmapDC, hBitmap, height, height, maskData, bmi, OS.DIB_RGB_COLORS);
				} else {
					/* Do the entire mask */
					/* Create the BITMAPINFO */
					bmiHeader = new BITMAPINFOHEADER();
					bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
					bmiHeader.biWidth = width;
					bmiHeader.biHeight = -height;
					bmiHeader.biPlanes = 1;
					bmiHeader.biBitCount = 1;
					bmiHeader.biCompression = OS.BI_RGB;
					bmi = new byte[BITMAPINFOHEADER.sizeof + 8];
					OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);

					/* First color black, second color white */
					int offset = BITMAPINFOHEADER.sizeof;
					bmi[offset + 4] = bmi[offset + 5] = bmi[offset + 6] = (byte)0xFF;
					bmi[offset + 7] = 0;
					OS.SelectObject(hBitmapDC, info.hbmMask);
					/* Call with null lpBits to get the image size */
					OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, null, bmi, OS.DIB_RGB_COLORS);
					OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
					imageSize = bmiHeader.biSizeImage;
					maskData = new byte[imageSize];
					OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, maskData, bmi, OS.DIB_RGB_COLORS);
					/* Loop to invert the mask */
					boolean hasMaskData = false;
					for (int i = 0; i < maskData.length; i++) {
						hasMaskData |= maskData[i] != 0;
						maskData[i] ^= -1;
					}
					/* Make sure mask scanlinePad is 2 */
					int maskPad;
					int bpl = imageSize / height;
					for (maskPad = 1; maskPad < 128; maskPad++) {
						int calcBpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
						if (calcBpl == bpl) break;
					}
					maskData = ImageData.convertPad(maskData, width, height, 1, maskPad, 2);
					// For missing mask data, see https://github.com/eclipse-platform/eclipse.platform.swt/issues/715
					if (!hasMaskData && depth == 32) {
						alphaData = new byte[width * height];
						boolean hasAlphaData = false;
						for (int pixelIndex = 0; pixelIndex < alphaData.length; pixelIndex++) {
							alphaData[pixelIndex] = data[pixelIndex * 4 + 3];
							hasAlphaData |= alphaData[pixelIndex] != -1;
						}
						// In case there is alpha data, replace the empty mask data with proper alpha data
						if (hasAlphaData) {
							maskData = null;
						} else {
							alphaData = null;
						}
					}
				}
				/* Clean up */
				OS.SelectObject(hBitmapDC, hOldBitmap);
				OS.DeleteDC(hBitmapDC);

				/* Release the HDC for the device */
				device.internal_dispose_GC(hDC, null);

				if (info.hbmColor != 0) OS.DeleteObject(info.hbmColor);
				if (info.hbmMask != 0) OS.DeleteObject(info.hbmMask);
				/* Construct and return the ImageData */
				ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
				imageData.alphaData = alphaData;
				imageData.maskData = maskData;
				imageData.maskPad = 2;
				return imageData;
			}
			case SWT.BITMAP: {
				/* Get the basic BITMAP information */
				bm = new BITMAP();
				OS.GetObject(handle, BITMAP.sizeof, bm);
				depth = bm.bmPlanes * bm.bmBitsPixel;
				width = bm.bmWidth;
				height = bm.bmHeight;
				/* Find out whether this is a DIB or a DDB. */
				boolean isDib = (bm.bmBits != 0);
				/* Get the HDC for the device */
				long hDC = device.internal_new_GC(null);
				DIBSECTION dib = null;
				if (isDib) {
					dib = new DIBSECTION();
					OS.GetObject(handle, DIBSECTION.sizeof, dib);
				}
				/* Calculate number of colors */
				int numColors = 0;
				if (depth <= 8) {
					if (isDib) {
						numColors = dib.biClrUsed;
					} else {
						numColors = 1 << depth;
					}
				}
				/* Create the BITMAPINFO */
				byte[] bmi = null;
				BITMAPINFOHEADER bmiHeader = null;
				if (!isDib) {
					bmiHeader = new BITMAPINFOHEADER();
					bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
					bmiHeader.biWidth = width;
					bmiHeader.biHeight = -height;
					bmiHeader.biPlanes = 1;
					bmiHeader.biBitCount = (short)depth;
					bmiHeader.biCompression = OS.BI_RGB;
					bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
					OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
				}

				/* Create the DC and select the bitmap */
				long hBitmapDC = OS.CreateCompatibleDC(hDC);
				long hOldBitmap = OS.SelectObject(hBitmapDC, handle);
				/* Find the size of the image and allocate data */
				int imageSize;
				if (isDib) {
					imageSize = dib.biSizeImage;
				} else {
					/* Call with null lpBits to get the image size */
					OS.GetDIBits(hBitmapDC, handle, 0, height, null, bmi, OS.DIB_RGB_COLORS);
					OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
					imageSize = bmiHeader.biSizeImage;
				}
				byte[] data = new byte[imageSize];
				/* Get the bitmap data */
				if (isDib) {
					OS.MoveMemory(data, bm.bmBits, imageSize);
				} else {
					OS.GetDIBits(hBitmapDC, handle, 0, height, data, bmi, OS.DIB_RGB_COLORS);
				}
				/* Calculate the palette */
				PaletteData palette = null;
				if (depth <= 8) {
					RGB[] rgbs = new RGB[numColors];
					if (isDib) {
						byte[] colors = new byte[numColors * 4];
						OS.GetDIBColorTable(hBitmapDC, 0, numColors, colors);
						int colorIndex = 0;
						for (int i = 0; i < rgbs.length; i++) {
							rgbs[i] = new RGB(colors[colorIndex + 2] & 0xFF, colors[colorIndex + 1] & 0xFF, colors[colorIndex] & 0xFF);
							colorIndex += 4;
						}
					} else {
						int srcIndex = BITMAPINFOHEADER.sizeof;
						for (int i = 0; i < numColors; i++) {
							rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
							srcIndex += 4;
						}
					}
					palette = new PaletteData(rgbs);
				} else if (depth == 16) {
					palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
				} else if (depth == 24) {
					palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				} else if (depth == 32) {
					palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
				} else {
					SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
				}
				/* Clean up */
				OS.SelectObject(hBitmapDC, hOldBitmap);
				OS.DeleteDC(hBitmapDC);

				/* Release the HDC for the device */
				device.internal_dispose_GC(hDC, null);

				/* Construct and return the ImageData */
				ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
				imageData.transparentPixel = transparentPixel;
				if (depth == 32) {
					byte straightData[] = new byte[imageSize];
					byte alphaData[] = new byte[width * height];
					boolean validAlpha = isDib;
					for (int ap = 0, dp = 0; validAlpha && ap < alphaData.length; ap++, dp += 4) {
						int b = data[dp    ] & 0xFF;
						int g = data[dp + 1] & 0xFF;
						int r = data[dp + 2] & 0xFF;
						int a = data[dp + 3] & 0xFF;
						alphaData[ap] = (byte) a;
						validAlpha = validAlpha && b <= a && g <= a && r <= a;
						if (a != 0) {
							straightData[dp    ] = (byte) (((b * 0xFF) + a / 2) / a);
							straightData[dp + 1] = (byte) (((g * 0xFF) + a / 2) / a);
							straightData[dp + 2] = (byte) (((r * 0xFF) + a / 2) / a);
						}
					}
					if (validAlpha) {
						imageData.data = straightData;
						imageData.alphaData = alphaData;
					}
					else {
						for (int dp = 3; dp < imageSize; dp += 4) {
							data[dp] = (byte) 0xFF;
						}
					}
				}
				return imageData;
			}
			default:
				SWT.error(SWT.ERROR_INVALID_IMAGE);
				return null;
		}
	}

	private boolean isDisposed() {
		return this.handle == 0;
	}

	private void destroy() {
		zoomLevelToImageHandle.remove(zoom, this);
		if (type == SWT.ICON) {
			OS.DestroyIcon (handle);
		} else {
			OS.DeleteObject (handle);
		}
		handle = 0;
	}
}
}
