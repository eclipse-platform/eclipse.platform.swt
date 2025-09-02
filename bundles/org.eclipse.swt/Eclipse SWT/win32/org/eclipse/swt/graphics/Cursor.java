/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class manage operating system resources that
 * specify the appearance of the on-screen pointer. To create a
 * cursor you specify the device and either a simple cursor style
 * describing one of the standard operating system provided cursors
 * or the image and mask data for the desired appearance.
 * <p>
 * Application code must explicitly invoke the <code>Cursor.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>
 *   CURSOR_ARROW, CURSOR_WAIT, CURSOR_CROSS, CURSOR_APPSTARTING, CURSOR_HELP,
 *   CURSOR_SIZEALL, CURSOR_SIZENESW, CURSOR_SIZENS, CURSOR_SIZENWSE, CURSOR_SIZEWE,
 *   CURSOR_SIZEN, CURSOR_SIZES, CURSOR_SIZEE, CURSOR_SIZEW, CURSOR_SIZENE, CURSOR_SIZESE,
 *   CURSOR_SIZESW, CURSOR_SIZENW, CURSOR_UPARROW, CURSOR_IBEAM, CURSOR_NO, CURSOR_HAND
 * </dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#cursor">Cursor snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Cursor extends Resource {

	/**
	 * Attribute to cache current native zoom level
	 */
	private static final int DEFAULT_ZOOM = 100;

	private HashMap<Integer, CursorHandle> zoomLevelToHandle = new HashMap<>();

	private final CursorHandleProvider cursorHandleProvider;

	private boolean isDestroyed;

/**
 * Constructs a new cursor given a device and a style
 * constant describing the desired cursor appearance.
 * <p>
 * You must dispose the cursor when it is no longer required.
 * </p>
 * NOTE:
 * It is recommended to use {@link org.eclipse.swt.widgets.Display#getSystemCursor(int)}
 * instead of using this constructor. This way you can avoid the
 * overhead of disposing the Cursor resource.
 *
 * @param device the device on which to allocate the cursor
 * @param style the style of cursor to allocate
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - when an unknown style is specified</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see SWT#CURSOR_ARROW
 * @see SWT#CURSOR_WAIT
 * @see SWT#CURSOR_CROSS
 * @see SWT#CURSOR_APPSTARTING
 * @see SWT#CURSOR_HELP
 * @see SWT#CURSOR_SIZEALL
 * @see SWT#CURSOR_SIZENESW
 * @see SWT#CURSOR_SIZENS
 * @see SWT#CURSOR_SIZENWSE
 * @see SWT#CURSOR_SIZEWE
 * @see SWT#CURSOR_SIZEN
 * @see SWT#CURSOR_SIZES
 * @see SWT#CURSOR_SIZEE
 * @see SWT#CURSOR_SIZEW
 * @see SWT#CURSOR_SIZENE
 * @see SWT#CURSOR_SIZESE
 * @see SWT#CURSOR_SIZESW
 * @see SWT#CURSOR_SIZENW
 * @see SWT#CURSOR_UPARROW
 * @see SWT#CURSOR_IBEAM
 * @see SWT#CURSOR_NO
 * @see SWT#CURSOR_HAND
 * @see #dispose()
 */
public Cursor(Device device, int style) {
	super(device);
	this.cursorHandleProvider = new StyleCursorHandleProvider(style);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * Constructs a new cursor given a device, image and mask
 * data describing the desired cursor appearance, and the x
 * and y coordinates of the <em>hotspot</em> (that is, the point
 * within the area covered by the cursor which is considered
 * to be where the on-screen pointer is "pointing").
 * <p>
 * The mask data is allowed to be null, but in this case the source
 * must be an ImageData representing an icon that specifies both
 * color data and mask data.
 * <p>
 * You must dispose the cursor when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param source the color data for the cursor
 * @param mask the mask data for the cursor (or null)
 * @param hotspotX the x coordinate of the cursor's hotspot
 * @param hotspotY the y coordinate of the cursor's hotspot
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the source is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if the mask is null and the source does not have a mask</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the source and the mask are not the same
 *          size, or if the hotspot is outside the bounds of the image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	super(device);
	this.cursorHandleProvider = new ImageDataWithMaskCursorHandleProvider(source, mask, hotspotX, hotspotY);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

private static CursorHandle setupCursorFromImageData(ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (mask == null) {
		mask = source.getTransparencyMask();
	}
	/* Convert depth to 1 */
	mask = ImageData.convertMask(mask);
	source = ImageData.convertMask(source);

	/* Make sure source and mask scanline pad is 2 */
	byte[] sourceData = ImageData.convertPad(source.data, source.width, source.height, source.depth, source.scanlinePad, 2);
	byte[] maskData = ImageData.convertPad(mask.data, mask.width, mask.height, mask.depth, mask.scanlinePad, 2);

	/* Create the cursor */
	long hInst = OS.GetModuleHandle(null);
	long handle = OS.CreateCursor(hInst, hotspotX, hotspotY, source.width, source.height, sourceData, maskData);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	return new CustomCursorHandle(handle);
}

/**
 * Constructs a new cursor given a device, image data describing
 * the desired cursor appearance, and the x and y coordinates of
 * the <em>hotspot</em> (that is, the point within the area
 * covered by the cursor which is considered to be where the
 * on-screen pointer is "pointing").
 * <p>
 * You must dispose the cursor when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param source the image data for the cursor
 * @param hotspotX the x coordinate of the cursor's hotspot
 * @param hotspotY the y coordinate of the cursor's hotspot
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the hotspot is outside the bounds of the
 * 		 image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see #dispose()
 *
 * @since 3.0
 */
public Cursor(Device device, ImageData source, int hotspotX, int hotspotY) {
	super(device);
	this.cursorHandleProvider = new ImageDataCursorHandleProvider(source, hotspotX, hotspotY);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

private static CursorHandle setupCursorFromImageData(Device device, ImageData source, int hotspotX, int hotspotY) {
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	long hBitmap = 0;
	long hMask = 0;
	if (source.maskData == null && source.transparentPixel == -1 && (source.alpha != -1 || source.alphaData != null)) {
		PaletteData palette = source.palette;
		PaletteData newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		ImageData img = new ImageData(source.width, source.height, 32, newPalette);
		if (palette.isDirect) {
			ImageData.blit(
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), source.width, source.height,    palette.redMask,    palette.greenMask,    palette.blueMask,
				   img.data,    img.depth,    img.bytesPerLine,    img.getByteOrder(),    img.width,    img.height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
				false, false);
		} else {
			RGB[] rgbs = palette.getRGBs();
			int length = rgbs.length;
			byte[] srcReds = new byte[length];
			byte[] srcGreens = new byte[length];
			byte[] srcBlues = new byte[length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				if (rgb == null) continue;
				srcReds[i] = (byte)rgb.red;
				srcGreens[i] = (byte)rgb.green;
				srcBlues[i] = (byte)rgb.blue;
			}
			ImageData.blit(
				source.width, source.height,
				source.data, source.depth, source.bytesPerLine, source.getByteOrder(), srcReds, srcGreens, srcBlues,
				   img.data,    img.depth,    img.bytesPerLine,    img.getByteOrder(), newPalette.redMask, newPalette.greenMask, newPalette.blueMask);
		}
		hBitmap = Image.createDIB(source.width, source.height, 32);
		if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		BITMAP dibBM = new BITMAP();
		OS.GetObject(hBitmap, BITMAP.sizeof, dibBM);
		byte[] srcData = img.data;
		if (source.alpha != -1) {
			for (int i = 3; i < srcData.length; i+=4) {
				srcData[i] = (byte)source.alpha;
			}
		} else if (source.alphaData != null) {
			for (int sp = 3, ap=0; sp < srcData.length; sp+=4, ap++) {
				srcData[sp] = source.alphaData[ap];
			}
		}
		OS.MoveMemory(dibBM.bmBits, srcData, srcData.length);
		hMask = OS.CreateBitmap(source.width, source.height, 1, 1, new byte[(((source.width + 7) / 8) + 3) / 4 * 4 * source.height]);
		if (hMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	} else {
		ImageData mask = source.getTransparencyMask();
		long [] result = Image.initIcon(device, source, mask);
		hBitmap = result[0];
		hMask = result[1];
	}
	/* Create the icon */
	ICONINFO info = new ICONINFO();
	info.fIcon = false;
	info.hbmColor = hBitmap;
	info.hbmMask = hMask;
	info.xHotspot = hotspotX;
	info.yHotspot = hotspotY;
	long handle = OS.CreateIconIndirect(info);
	OS.DeleteObject(hBitmap);
	OS.DeleteObject(hMask);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	return new IconCursorHandle(handle);
}

/**
 * Constructs a new cursor given a device, image describing
 * the desired cursor appearance, and the x and y coordinates of
 * the <em>hotspot</em> (that is, the point within the area
 * covered by the cursor which is considered to be where the
 * on-screen pointer is "pointing").
 * <p>
 * You must dispose the cursor when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param imageDataProvider the ImageDataProvider for the cursor
 * @param hotspotX the x coordinate of the cursor's hotspot
 * @param hotspotY the y coordinate of the cursor's hotspot
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the hotspot is outside the bounds of the
 * 		 image</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see #dispose()
 *
 * @since 3.131
 */
public Cursor(Device device, ImageDataProvider imageDataProvider, int hotspotX, int hotspotY) {
	super(device);
	if (imageDataProvider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.cursorHandleProvider = new ImageDataProviderCursorHandleProvider(imageDataProvider, hotspotX, hotspotY);
	init();
	this.device.registerResourceWithZoomSupport(this);
}

/**
 * <b>IMPORTANT:</b> This method is not part of the public
 * API for Image. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 *
 * Get the handle for a cursor given a zoom level.
 *
 * @param cursor the cursor
 * @param zoom zoom level (in %) of the monitor the cursor is currently in.
 * @return The handle of the cursor.
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Long win32_getHandle (Cursor cursor, int zoom) {
	if (cursor.isDisposed()) {
		return 0L;
	}
	if (cursor.zoomLevelToHandle.get(zoom) != null) {
		return cursor.zoomLevelToHandle.get(zoom).getHandle();
	}

	CursorHandle handle = cursor.cursorHandleProvider.createHandle(cursor.device, zoom);
	cursor.setHandleForZoomLevel(handle, zoom);

	return cursor.zoomLevelToHandle.get(zoom).getHandle();
}

private void setHandleForZoomLevel(CursorHandle handle, Integer zoom) {
	if (zoom != null && !zoomLevelToHandle.containsKey(zoom)) {
		zoomLevelToHandle.put(zoom, handle);
	}
}

@Override
void destroy () {
	device.deregisterResourceWithZoomSupport(this);
	for (CursorHandle handle : zoomLevelToHandle.values()) {
		handle.destroy();
	}
	zoomLevelToHandle.clear();
	this.isDestroyed = true;
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
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor) object;
	return device == cursor.device && win32_getHandle(this, DEFAULT_ZOOM) == win32_getHandle(cursor, DEFAULT_ZOOM);
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
	return win32_getHandle(this, DEFAULT_ZOOM).intValue();
}

/**
 * Returns <code>true</code> if the cursor has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the cursor.
 * When a cursor has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the cursor.
 *
 * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return isDestroyed;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + zoomLevelToHandle + "}";
}

@Override
void destroyHandlesExcept(Set<Integer> zoomLevels) {
	zoomLevelToHandle.entrySet().removeIf(entry -> {
		final Integer zoom = entry.getKey();
		if (!zoomLevels.contains(zoom) && zoom != DPIUtil.getZoomForAutoscaleProperty(DEFAULT_ZOOM)) {
			entry.getValue().destroy();
			return true;
		}
		return false;
	});
}

private static abstract class CursorHandle {
	private final long handle;

	public CursorHandle(long handle) {
		this.handle = handle;
	}

	long getHandle() {
		return handle;
	}

	abstract void destroy();
}

private static class IconCursorHandle extends CursorHandle {
	public IconCursorHandle(long handle) {
		super(handle);
	}

	@Override
	void destroy() {
		OS.DestroyIcon(getHandle());
	}
}

private static class CustomCursorHandle extends CursorHandle {
	public CustomCursorHandle(long handle) {
		super(handle);
	}

	@Override
	void destroy() {
		/*
		* The MSDN states that one should not destroy a shared
		* cursor, that is, one obtained from LoadCursor.
		* However, it does not appear to do any harm, so rather
		* than keep track of how a cursor was created, we just
		* destroy them all. If this causes problems in the future,
		* put the flag back in.
		*/
		OS.DestroyCursor(getHandle());
	}
}

private static interface CursorHandleProvider {
	CursorHandle createHandle(Device device, int zoom);
}

private static class StyleCursorHandleProvider implements CursorHandleProvider {
	private final long lpCursorName;

	public StyleCursorHandleProvider(int style) {
		this.lpCursorName = getOSCursorIdFromStyle(style);
	}

	@Override
	public CursorHandle createHandle(Device device, int zoom) {
		// zoom ignored, LoadCursor handles scaling internally
		long handle = OS.LoadCursor(0, lpCursorName);
		if (handle == 0) {
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		return new CustomCursorHandle(handle);
	}

	private static final long getOSCursorIdFromStyle(int style) {
		long lpCursorName = 0;
		switch (style) {
		case SWT.CURSOR_HAND:
			lpCursorName = OS.IDC_HAND;
			break;
		case SWT.CURSOR_ARROW:
			lpCursorName = OS.IDC_ARROW;
			break;
		case SWT.CURSOR_WAIT:
			lpCursorName = OS.IDC_WAIT;
			break;
		case SWT.CURSOR_CROSS:
			lpCursorName = OS.IDC_CROSS;
			break;
		case SWT.CURSOR_APPSTARTING:
			lpCursorName = OS.IDC_APPSTARTING;
			break;
		case SWT.CURSOR_HELP:
			lpCursorName = OS.IDC_HELP;
			break;
		case SWT.CURSOR_SIZEALL:
			lpCursorName = OS.IDC_SIZEALL;
			break;
		case SWT.CURSOR_SIZENESW:
			lpCursorName = OS.IDC_SIZENESW;
			break;
		case SWT.CURSOR_SIZENS:
			lpCursorName = OS.IDC_SIZENS;
			break;
		case SWT.CURSOR_SIZENWSE:
			lpCursorName = OS.IDC_SIZENWSE;
			break;
		case SWT.CURSOR_SIZEWE:
			lpCursorName = OS.IDC_SIZEWE;
			break;
		case SWT.CURSOR_SIZEN:
			lpCursorName = OS.IDC_SIZENS;
			break;
		case SWT.CURSOR_SIZES:
			lpCursorName = OS.IDC_SIZENS;
			break;
		case SWT.CURSOR_SIZEE:
			lpCursorName = OS.IDC_SIZEWE;
			break;
		case SWT.CURSOR_SIZEW:
			lpCursorName = OS.IDC_SIZEWE;
			break;
		case SWT.CURSOR_SIZENE:
			lpCursorName = OS.IDC_SIZENESW;
			break;
		case SWT.CURSOR_SIZESE:
			lpCursorName = OS.IDC_SIZENWSE;
			break;
		case SWT.CURSOR_SIZESW:
			lpCursorName = OS.IDC_SIZENESW;
			break;
		case SWT.CURSOR_SIZENW:
			lpCursorName = OS.IDC_SIZENWSE;
			break;
		case SWT.CURSOR_UPARROW:
			lpCursorName = OS.IDC_UPARROW;
			break;
		case SWT.CURSOR_IBEAM:
			lpCursorName = OS.IDC_IBEAM;
			break;
		case SWT.CURSOR_NO:
			lpCursorName = OS.IDC_NO;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return lpCursorName;
	}
}

private static abstract class HotspotAwareCursorHandleProvider implements CursorHandleProvider {
	private final int hotspotX;
	private final int hotspotY;

	public HotspotAwareCursorHandleProvider(int hotspotX, int hotspotY) {
		this.hotspotX = hotspotX;
		this.hotspotY = hotspotY;
	}

	protected final int getHotpotXInPixels(int zoom) {
		return Win32DPIUtils.pointToPixel(hotspotX, zoom);
	}

	protected final int getHotpotYInPixels(int zoom) {
		return Win32DPIUtils.pointToPixel(hotspotY, zoom);
	}

	protected static final void validateHotspotInsideImage(ImageData source, int hotspotX, int hotspotY) {
		/* Check the hotspots */
		if (hotspotX >= source.width || hotspotX < 0 ||
			hotspotY >= source.height || hotspotY < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
}

private static class ImageDataProviderCursorHandleProvider extends HotspotAwareCursorHandleProvider {
	private final ImageDataProvider provider;

	public ImageDataProviderCursorHandleProvider(ImageDataProvider provider, int hotspotX, int hotspotY) {
		super(hotspotX, hotspotY);
		ImageData source = provider.getImageData(DEFAULT_ZOOM);
		if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		validateHotspotInsideImage(source, hotspotX, hotspotY);
		this.provider = provider;
	}

	@Override
	public CursorHandle createHandle(Device device, int zoom) {
		ImageData source;
		if (zoom == DEFAULT_ZOOM) {
			source = this.provider.getImageData(DEFAULT_ZOOM);
		} else {
			Image tempImage = new Image(device, this.provider);
			source = tempImage.getImageData(zoom);
			tempImage.dispose();
		}
		return setupCursorFromImageData(device, source, getHotpotXInPixels(zoom), getHotpotYInPixels(zoom));
	}
}

private static class ImageDataCursorHandleProvider extends HotspotAwareCursorHandleProvider {
	protected final ImageData source;

	public ImageDataCursorHandleProvider(ImageData source, int hotspotX, int hotspotY) {
		super(hotspotX, hotspotY);
		if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		validateHotspotInsideImage(source, hotspotX, hotspotY);
		this.source = source;
	}

	@Override
	public CursorHandle createHandle(Device device, int zoom) {
		ImageData scaledSource = DPIUtil.scaleImageData(device, this.source, zoom, DEFAULT_ZOOM);
		return setupCursorFromImageData(device, scaledSource, getHotpotXInPixels(zoom),
				getHotpotYInPixels(zoom));
	}
}

private static class ImageDataWithMaskCursorHandleProvider extends ImageDataCursorHandleProvider {
	private final ImageData mask;

	public ImageDataWithMaskCursorHandleProvider(ImageData source, ImageData mask, int hotspotX, int hotspotY) {
		super(source, hotspotX, hotspotY);
		this.mask = mask;
		validateMask(source, mask);
	}

	private void validateMask(ImageData source, ImageData mask) {
		ImageData testMask = mask == null ? null : (ImageData) mask.clone();
		if (testMask == null) {
			if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
				SWT.error(SWT.ERROR_NULL_ARGUMENT);
			}
			testMask = source.getTransparencyMask();
		}
		/* Check the bounds. Mask must be the same size as source */
		if (testMask.width != source.width || testMask.height != source.height) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	@Override
	public CursorHandle createHandle(Device device, int zoom) {
		ImageData scaledSource = DPIUtil.scaleImageData(device, this.source, zoom, DEFAULT_ZOOM);
		ImageData scaledMask = this.mask != null ? DPIUtil.scaleImageData(device, mask, zoom, DEFAULT_ZOOM)
				: null;
		return setupCursorFromImageData(scaledSource, scaledMask, getHotpotXInPixels(zoom), getHotpotYInPixels(zoom));
	}
}

}
