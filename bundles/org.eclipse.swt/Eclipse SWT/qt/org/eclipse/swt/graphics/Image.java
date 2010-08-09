/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QPaintDeviceInterface;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QImage.Format;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

/**
 * Instances of this class are graphics which have been prepared for display on
 * a specific device. That is, they are ready to paint using methods such as
 * <code>GC.drawImage()</code> and display on widgets with, for example,
 * <code>Button.setImage()</code>.
 * <p>
 * If loaded from a file format that supports it, an <code>Image</code> may have
 * transparency, meaning that certain pixels are specified as being transparent
 * when drawn. Examples of file formats that support transparency are GIF and
 * PNG.
 * </p>
 * <p>
 * There are two primary ways to use <code>Images</code>. The first is to load a
 * graphic file from disk and create an <code>Image</code> from it. This is done
 * using an <code>Image</code> constructor, for example:
 * 
 * <pre>
 * Image i = new Image(device, &quot;C:\\graphic.bmp&quot;);
 * </pre>
 * 
 * A graphic file may contain a color table specifying which colors the image
 * was intended to possess. In the above example, these colors will be mapped to
 * the closest available color in SWT. It is possible to get more control over
 * the mapping of colors as the image is being created, using code of the form:
 * 
 * <pre>
 * ImageData data = new ImageData(&quot;C:\\graphic.bmp&quot;);
 * RGB[] rgbs = data.getRGBs();
 * // At this point, rgbs contains specifications of all
 * // the colors contained within this image. You may
 * // allocate as many of these colors as you wish by
 * // using the Color constructor Color(RGB), then
 * // create the image:
 * Image i = new Image(device, data);
 * </pre>
 * <p>
 * Applications which require even greater control over the image loading
 * process should use the support provided in class <code>ImageLoader</code>.
 * </p>
 * <p>
 * Application code must explicitly invoke the <code>Image.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @see Color
 * @see ImageData
 * @see ImageLoader
 * @see <a href="http://www.eclipse.org/swt/snippets/#image">Image snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples:
 *      GraphicsExample, ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class Image extends Resource implements Drawable {

	/**
	 * specifies whether the receiver is a bitmap or an icon (one of
	 * <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 */
	private int type = SWT.BITMAP;

	private int transparentPixel = -1;

	/**
	 * The GC the image is currently selected in.
	 */
	private GC memGC;

	/**
	 * The global alpha value to be used for every pixel.
	 */
	private int alpha = -1;

	private boolean hasMask = false;

	//private QImage image;
	private QIcon icon;
	private QPixmap pixmap;

	Image(Device device) {
		super(device);
	}

	/**
	 * Constructs an empty instance of this class with the specified width and
	 * height. The result may be drawn upon by creating a GC and using any of
	 * its drawing operations, as shown in the following example:
	 * 
	 * <pre>
	 * Image i = new Image(device, width, height);
	 * GC gc = new GC(i);
	 * gc.drawRectangle(0, 0, 50, 50);
	 * gc.dispose();
	 * </pre>
	 * <p>
	 * Note: Some platforms may have a limitation on the size of image that can
	 * be created (size depends on width, height, and depth). For example,
	 * Windows 95, 98, and ME do not allow images larger than 16M.
	 * </p>
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param width
	 *            the width of the new image
	 * @param height
	 *            the height of the new image
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if either the width or height
	 *                is negative or zero</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, int width, int height) {
		super(device);
		init(width, height);
		init();
	}

	/**
	 * Constructs a new instance of this class based on the provided image, with
	 * an appearance that varies depending on the value of the flag. The
	 * possible flag values are:
	 * <dl>
	 * <dt><b>{@link SWT#IMAGE_COPY}</b></dt>
	 * <dd>the result is an identical copy of srcImage</dd>
	 * <dt><b>{@link SWT#IMAGE_DISABLE}</b></dt>
	 * <dd>the result is a copy of srcImage which has a <em>disabled</em> look</dd>
	 * <dt><b>{@link SWT#IMAGE_GRAY}</b></dt>
	 * <dd>the result is a copy of srcImage which has a <em>gray scale</em> look
	 * </dd>
	 * </dl>
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param srcImage
	 *            the image to use as the source
	 * @param flag
	 *            the style, either <code>IMAGE_COPY</code>,
	 *            <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code>
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if srcImage is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the flag is not one of
	 *                <code>IMAGE_COPY</code>, <code>IMAGE_DISABLE</code> or
	 *                <code>IMAGE_GRAY</code></li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or
	 *                an icon, or is otherwise in an invalid state</li>
	 *                <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the image is
	 *                not supported</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, Image srcImage, int flag) {
		super(device);
		if (srcImage == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (srcImage.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		device = this.device;
		this.type = srcImage.type;
		switch (flag) {
		case SWT.IMAGE_COPY:
			this.pixmap = new QPixmap(srcImage.pixmap);
			break;
		case SWT.IMAGE_DISABLE:
			this.pixmap = createDisabledImage(srcImage);
			break;
		case SWT.IMAGE_GRAY:
			this.pixmap = createGrayImage(srcImage);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		init();
	}

	/**
	 * Constructs an empty instance of this class with the width and height of
	 * the specified rectangle. The result may be drawn upon by creating a GC
	 * and using any of its drawing operations, as shown in the following
	 * example:
	 * 
	 * <pre>
	 * Image i = new Image(device, boundsRectangle);
	 * GC gc = new GC(i);
	 * gc.drawRectangle(0, 0, 50, 50);
	 * gc.dispose();
	 * </pre>
	 * <p>
	 * Note: Some platforms may have a limitation on the size of image that can
	 * be created (size depends on width, height, and depth). For example,
	 * Windows 95, 98, and ME do not allow images larger than 16M.
	 * </p>
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param bounds
	 *            a rectangle specifying the image's width and height (must not
	 *            be null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the bounds rectangle is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if either the rectangle's
	 *                width or height is negative</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, Rectangle bounds) {
		super(device);
		if (bounds == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		init(bounds.width, bounds.height);
		init();
	}

	/**
	 * Constructs an instance of this class from the given
	 * <code>ImageData</code>.
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param data
	 *            the image data to create the image from (must not be null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the image data is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the
	 *                ImageData is not supported</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, ImageData data) {
		super(device);
		init(data);
		init();
	}

	/**
	 * Constructs an instance of this class, whose type is <code>SWT.ICON</code>
	 * , from the two given <code>ImageData</code> objects. The two images must
	 * be the same size. Pixel transparency in either image will be ignored.
	 * <p>
	 * The mask image should contain white wherever the icon is to be visible,
	 * and black wherever the icon is to be transparent. In addition, the source
	 * image should contain black wherever the icon is to be transparent.
	 * </p>
	 * 
	 * @param device
	 *            the device on which to create the icon
	 * @param source
	 *            the color data for the icon
	 * @param mask
	 *            the mask data for the icon
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if either the source or mask is
	 *                null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if source and mask are
	 *                different sizes</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, ImageData source, ImageData mask) {
		super(device);
		if (source == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (mask == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (source.width != mask.width || source.height != mask.height) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		mask = ImageData.convertMask(mask);
		ImageData image = new ImageData(source.width, source.height, source.depth, source.palette, source.scanlinePad,
				source.data);
		image.maskPad = mask.scanlinePad;
		image.maskData = mask.data;
		init(image);
	}

	/**
	 * Constructs an instance of this class by loading its representation from
	 * the specified input stream. Throws an error if an error occurs while
	 * loading the image, or if the result is an image of an unsupported type.
	 * Application code is still responsible for closing the input stream.
	 * <p>
	 * This constructor is provided for convenience when loading a single image
	 * only. If the stream contains multiple images, only the first one will be
	 * loaded. To load multiple images, use <code>ImageLoader.load()</code>.
	 * </p>
	 * <p>
	 * This constructor may be used to load a resource as follows:
	 * </p>
	 * 
	 * <pre>
	 * static Image loadImage(Display display, Class clazz, String string) {
	 * 	InputStream stream = clazz.getResourceAsStream(string);
	 * 	if (stream == null)
	 * 		return null;
	 * 	Image image = null;
	 * 	try {
	 * 		image = new Image(display, stream);
	 * 	} catch (SWTException ex) {
	 * 	} finally {
	 * 		try {
	 * 			stream.close();
	 * 		} catch (IOException ex) {
	 * 		}
	 * 	}
	 * 	return image;
	 * }
	 * </pre>
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param stream
	 *            the input stream to load the image from
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_IO - if an IO error occurs while reading from
	 *                the stream</li>
	 *                <li>ERROR_INVALID_IMAGE - if the image stream contains
	 *                invalid data</li>
	 *                <li>ERROR_UNSUPPORTED_DEPTH - if the image stream
	 *                describes an image with an unsupported depth</li>
	 *                <li>ERROR_UNSUPPORTED_FORMAT - if the image stream
	 *                contains an unrecognized format</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, InputStream stream) {
		super(device);
		init(new ImageData(stream));
		init();
	}

	/**
	 * Constructs an instance of this class by loading its representation from
	 * the file with the specified name. Throws an error if an error occurs
	 * while loading the image, or if the result is an image of an unsupported
	 * type.
	 * <p>
	 * This constructor is provided for convenience when loading a single image
	 * only. If the specified file contains multiple images, only the first one
	 * will be used.
	 * 
	 * @param device
	 *            the device on which to create the image
	 * @param filename
	 *            the name of the file to load the image from
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li> <li>ERROR_NULL_ARGUMENT - if the
	 *                file name is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_IO - if an IO error occurs while reading from
	 *                the file</li> <li>ERROR_INVALID_IMAGE - if the image file
	 *                contains invalid data </li> <li>ERROR_UNSUPPORTED_DEPTH -
	 *                if the image file describes an image with an unsupported
	 *                depth</li> <li>ERROR_UNSUPPORTED_FORMAT - if the image
	 *                file contains an unrecognized format</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                image creation</li>
	 *                </ul>
	 */
	public Image(Device device, String filename) {
		super(device);
		if (filename == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		initNative(filename);
		if (this.pixmap == null) {
			init(new ImageData(filename));
		}
		init();
	}

	void init(int width, int height) {
		if (width <= 0 || height <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.type = SWT.BITMAP;
		pixmap = new QPixmap(width, height); //, Format.Format_ARGB32
	}

	void init(ImageData imageData) {
		if (imageData == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		this.type = SWT.BITMAP;

		Format format = getImageFormat(imageData);
		byte[] buffer = imageData2RawData(imageData, format);
		QImage image = new QImage(buffer, imageData.width, imageData.height, format);

		if (imageData.colorTable != null) {
			image.setColorTable(imageData.colorTable);
		}

		if (imageData.maskData != null) {
			hasMask = true;
		}
		updateAlphaChannel(image, imageData);

		pixmap = QPixmap.fromImage(image);

		try {
			this.transparentPixel = getTransparentPixel(imageData);
		} catch (Error e) {
			pixmap = null;
			throw e;
		}
	}

	private Format getImageFormat(ImageData imageData) {
		switch (imageData.depth) {
		case 32:
			return Format.Format_ARGB32;
		case 24:
			return Format.Format_RGB888;
		case 16:
			return Format.Format_RGB16;
		case 8:
		case 4:
			return Format.Format_Indexed8;
		case 1:
			return Format.Format_Mono;
		default:
			throw new RuntimeException("invalid color depth"); //$NON-NLS-1$
		}
	}

	private void updateAlphaChannel(QImage image, ImageData imageData) {
		Format format = null;
		byte[] data = null;
		if (imageData.maskData != null) {
			// Mask data is 1 bit/pixel
			data = imageData.maskData;
			format = Format.Format_Mono;
		} else if (imageData.alpha != -1) {
			data = new byte[image.width() * image.height()];
			Arrays.fill(data, (byte) alpha);
			format = Format.Format_Indexed8;
		} else if (imageData.alphaData != null) {
			data = imageData.alphaData;
			format = Format.Format_Indexed8;
		}
		if (data != null) {
			QImage alpha = new QImage(data, imageData.width, imageData.height, format);
			image.setAlphaChannel(alpha);
		}
	}

	private byte[] imageData2RawData(ImageData image, Format format) {
		PaletteData palette = image.palette;
		if (!((image.depth == 1 || image.depth == 2 || image.depth == 4 || image.depth == 8) && !palette.isDirect
				|| image.depth == 8 || (image.depth == 16 || image.depth == 24 || image.depth == 32)
				&& palette.isDirect)) {
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		}

		byte[] buffer = image.data;
		if (format.equals(Format.Format_RGB888)) {
			// swap red and blue bytes
			for (int i = 0; i < buffer.length - 2; i += 3) {
				byte tmp = buffer[i + 2];
				buffer[i + 2] = buffer[i];
				buffer[i] = tmp;
			}
		}

		return buffer;
	}

	private int getTransparentPixel(ImageData image) {
		if (image.transparentPixel != -1) {
			PaletteData palette = image.palette;
			RGB rgb = null;
			if (palette.isDirect) {
				rgb = palette.getRGB(image.transparentPixel);
			} else {
				if (image.transparentPixel < palette.colors.length) {
					rgb = palette.getRGB(image.transparentPixel);
				}
			}

			if (rgb != null) {
				return rgb.red << 24 | rgb.green << 16 | rgb.blue << 8 | 0xFF;
			}
		}
		return -1;
	}

	void initNative(String filename) {
		if (filename.toLowerCase().endsWith(".ico")) { //$NON-NLS-1$
			this.type = SWT.ICON;
			icon = new QIcon(filename);
		} else {
			this.type = SWT.BITMAP;
			pixmap = new QPixmap(filename);
		}
	}

	private QPixmap createGrayImage(Image srcImage) {
		return srcImage.getQPixmap(); //.convertToFormat(Format.Format_Indexed8, ImageConversionFlag.ThresholdDither);
	}

	private QPixmap createDisabledImage(Image srcImage) {
		// TODO the same as gray?
		return createGrayImage(srcImage);
	}

	private QImage getQImage() {
		return pixmap.toImage();
	}

	public QIcon getQIcon() {
		// fallback strategy if image is a bitmap
		if (icon == null) {
			icon = new QIcon(getQPixmap());
		}
		return icon;
	}

	public QPixmap getQPixmap() {
		//		if (pixmap == null) {
		//			if (image != null) {
		//				pixmap = QPixmap.fromImage(image);
		//			} else if (icon != null) {
		//				pixmap = icon.pixmap(getDefaultIconSize());
		//			}
		//		}
		return pixmap;
	}

	public QSize getDefaultIconSize() {
		List<QSize> sizes = getQIcon().availableSizes();
		if (sizes.isEmpty()) {
			return new QSize();
		}
		return sizes.get(0);
	}

	public boolean isIcon() {
		return type == SWT.ICON;
	}

	public boolean isBitmap() {
		return type == SWT.BITMAP;
	}

	@Override
	void destroy() {
		if (memGC != null) {
			memGC.dispose();
			memGC = null;
		}
		//		image = null;
		//		icon = null;
		pixmap = null;
	}

	/**
	 * Compares the argument to the receiver, and returns true if they represent
	 * the <em>same</em> object using a class specific comparison.
	 * 
	 * @param object
	 *            the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and
	 *         <code>false</code> otherwise
	 * 
	 * @see #hashCode
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Image)) {
			return false;
		}
		Image other = (Image) object;
		return device == other.device && pixmap == other.pixmap && transparentPixel == other.transparentPixel;
	}

	/**
	 * Returns the color to which to map the transparent pixel, or null if the
	 * receiver has no transparent pixel.
	 * <p>
	 * There are certain uses of Images that do not support transparency (for
	 * example, setting an image into a button or label). In these cases, it may
	 * be desired to simulate transparency by using the background color of the
	 * widget to paint the transparent pixels of the image. Use this method to
	 * check which color will be used in these cases in place of transparency.
	 * This value may be set with setBackground().
	 * <p>
	 * 
	 * @return the background color of the image, or null if there is no
	 *         transparency in the image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Color getBackground() {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (transparentPixel == -1) {
			return null;
		}
		int red = transparentPixel >> 16 & 0xFF;
		int green = transparentPixel >> 8 & 0xFF;
		int blue = transparentPixel >> 0 & 0xFF;
		return new Color(device, red, green, blue);
	}

	/**
	 * Returns the bounds of the receiver. The rectangle will always have x and
	 * y values of 0, and the width and height of the image.
	 * 
	 * @return a rectangle specifying the image's bounds
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or
	 *                an icon</li>
	 *                </ul>
	 */
	public Rectangle getBounds() {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		return new Rectangle(0, 0, pixmap.width(), pixmap.height());
	}

	/**
	 * Returns an <code>ImageData</code> based on the receiver Modifications
	 * made to this <code>ImageData</code> will not affect the Image.
	 * 
	 * @return an <code>ImageData</code> containing the image's data and
	 *         attributes
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or
	 *                an icon</li>
	 *                </ul>
	 * 
	 * @see ImageData
	 */
	public ImageData getImageData() {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QImage image = getQImage();

		PaletteData palette = new PaletteData(0x00FF0000, 0x00FF00, 0x00FF);
		ImageData imageData = new ImageData(image.width(), image.height(), image.depth(), palette, 4, image
				.copyOfBytes());
		imageData.bytesPerLine = image.bytesPerLine();
		imageData.transparentPixel = -1;
		imageData.alpha = alpha;
		imageData.alphaData = getAlphaData(image);

		if (image.numColors() > 0) {
			List<Integer> colorTable = image.colorTable();
			imageData.colorTable = colorTable;
		}

		return imageData;
	}

	private byte[] getAlphaData(QImage image) {
		if (hasMask) {
			// Mask data is 1 bit/pixel
			if (image.hasAlphaChannel()) {
				QImage alpha = image.alphaChannel();
				alpha.convertToFormat(Format.Format_Mono);
				return alpha.copyOfBytes();
			} else {
				int maskSize = image.width() * image.height() / 8;
				byte[] alphaData = new byte[maskSize];
				Arrays.fill(alphaData, (byte) 0xFF);
				return alphaData;
				//imageData.maskPad = 8;
			}
		} else if (alpha != -1) {
			byte[] alphaData = new byte[image.width() * image.height()];
			Arrays.fill(alphaData, (byte) alpha);
			return alphaData;
		} else if (image.hasAlphaChannel()) {
			QImage alpha = image.alphaChannel();
			return alpha.copyOfBytes();
		}
		return null;
	}

	public static Image qt_new(Display device, int type, QIcon qIcon) {
		Image image = new Image(device);
		image.type = type;
		image.icon = qIcon;
		return image;
	}

	/**
	 * Returns an integer hash code for the receiver. Any two objects that
	 * return <code>true</code> when passed to <code>equals</code> must return
	 * the same value for this method.
	 * 
	 * @return the receiver's hash
	 * 
	 * @see #equals
	 */
	@Override
	public int hashCode() {
		return pixmap != null ? (int) pixmap.hashCode() : 0;
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Image</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param data
	 *            the platform specific GC data
	 * @return the platform specific GC handle
	 */
	public QPaintDeviceInterface internal_new_GC(GCData data) {
		data.backgroundColor = getBackground();
		data.device = device;
		return pixmap;
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Image</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param hDC
	 *            the platform specific GC handle
	 * @param data
	 *            the platform specific GC data
	 */
	public void internal_dispose_GC(QPaintDeviceInterface paintDevice, GCData data) {
	}

	/**
	 * Returns <code>true</code> if the image has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the image. When an image has been
	 * disposed, it is an error to invoke any other method using the image.
	 * 
	 * @return <code>true</code> when the image is disposed and
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean isDisposed() {
		return pixmap == null && icon == null;
	}

	/**
	 * Sets the color to which to map the transparent pixel.
	 * <p>
	 * There are certain uses of <code>Images</code> that do not support
	 * transparency (for example, setting an image into a button or label). In
	 * these cases, it may be desired to simulate transparency by using the
	 * background color of the widget to paint the transparent pixels of the
	 * image. This method specifies the color that will be used in these cases.
	 * For example:
	 * 
	 * <pre>
	 * Button b = new Button();
	 * image.setBackground(b.getBackground());
	 * b.setImage(image);
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * The image may be modified by this operation (in effect, the transparent
	 * regions may be filled with the supplied color). Hence this operation is
	 * not reversible and it is not legal to call this function twice or with a
	 * null argument.
	 * </p>
	 * <p>
	 * This method has no effect if the receiver does not have a transparent
	 * pixel value.
	 * </p>
	 * 
	 * @param color
	 *            the color to use when a transparent pixel is specified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the color is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the color has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setBackground(Color color) {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (transparentPixel == -1) {
			return;
		}
		return; // TODO function disabled
		// byte red = (byte) ((transparentPixel >> 16) & 0xFF);
		// byte green = (byte) ((transparentPixel >> 8) & 0xFF);
		// byte blue = (byte) ((transparentPixel >> 0) & 0xFF);
		// byte newRed = (byte) ((int) (color.handle[0] * 255) & 0xFF);
		// byte newGreen = (byte) ((int) (color.handle[1] * 255) & 0xFF);
		// byte newBlue = (byte) ((int) (color.handle[2] * 255) & 0xFF);
		// NSBitmapImageRep imageRep = getRepresentation();
		// long /* int */bpr = imageRep.bytesPerRow();
		// long /* int */data = imageRep.bitmapData();
		// byte[] line = new byte[(int) bpr];
		// for (int i = 0, offset = 0; i < height; i++, offset += bpr) {
		// OS.memmove(line, data + offset, bpr);
		// for (int j = 0; j < line.length; j += 4) {
		// if (line[j + 1] == red && line[j + 2] == green
		// && line[j + 3] == blue) {
		// line[j + 1] = newRed;
		// line[j + 2] = newGreen;
		// line[j + 3] = newBlue;
		// }
		// }
		// OS.memmove(data + offset, line, bpr);
		// }
		// transparentPixel = (newRed & 0xFF) << 16 | (newGreen & 0xFF) << 8
		// | (newBlue & 0xFF);
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the receiver
	 */
	@Override
	public String toString() {
		if (isDisposed()) {
			return "Image {*DISPOSED*}"; //$NON-NLS-1$
		}
		return "Image {pixmap: " + pixmap + ", icon: " + icon + " }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public void setMemGC(GC gc) {
		this.memGC = gc;
	}

}
