package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import java.io.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class Image implements Drawable {
	/**
	 * Specifies whether the receiver is a bitmap or an icon.
	 * Values: SWT.BITMAP, SWT.ICON
	 */
	public int type;
	
	/**
	 * The handle to the OS pixmap resource.
	 * Warning: This field is platform dependent.
	 */
	public int pixmap;
	
	/**
	 * The handle to the OS mask resource.
	 * Warning: This field is platform dependent.
	 */
	public int mask;

	/**
	 * The device where this image was created.
	 */
	Device device;
	
	/**
	 * specifies the transparent pixel
	 * (Warning: This field is platform dependent)
	 */
	int transparentPixel = -1;
	
	/**
	 * The GC the image is currently selected in.
	 * Warning: This field is platform dependent.
	 */
	GC memGC;

	/**
	 * The alpha data of the image.
	 * Warning: This field is platform dependent.
	 */
	byte[] alphaData;
	
	/**
	 * The global alpha value to be used for every pixel.
	 * Warning: This field is platform dependent.
	 */
	int alpha = -1;
	
	/**
	 * Specifies the default scanline padding.
	 * Warning: This field is platform dependent.
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;
	
Image() {
}
public Image(Device device, int width, int height) {
	init(device, width, height);
}
public Image(Device device, Image srcImage, int flag) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = device.xDisplay;
	this.type = srcImage.type;
	this.mask = 0;
	int[] unused = new int[1];
	int[] w = new int[1];
	int[] h = new int[1];
 	OS.XGetGeometry(xDisplay, srcImage.pixmap, unused, unused, unused, w, h, unused, unused);
 	int width = w[0];
 	int height = h[0];
	int drawable = OS.XDefaultRootWindow(xDisplay);
	/* Don't create the mask here if flag is SWT.IMAGE_GRAY. See below.*/
	if (flag != SWT.IMAGE_GRAY && srcImage.mask != 0) {
		/* Generate the mask if necessary. */
		if (srcImage.transparentPixel != -1) srcImage.createMask();
		int mask = OS.XCreatePixmap(xDisplay, drawable, width, height, 1);
		int gc = OS.XCreateGC(xDisplay, mask, 0, null);
		OS.XCopyArea(xDisplay, srcImage.mask, mask, gc, 0, 0, width, height, 0, 0);
		OS.XFreeGC(xDisplay, gc);
		this.mask = mask;
		/* Destroy the image mask if the there is a GC created on the image */
		if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
	}
	switch (flag) {
		case SWT.IMAGE_COPY:
			int[] depth = new int[1];
			OS.XGetGeometry(xDisplay, srcImage.pixmap, unused, unused, unused, unused, unused, unused, depth);
			int pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, depth[0]);
			int gc = OS.XCreateGC(xDisplay, pixmap, 0, null);
			OS.XCopyArea(xDisplay, srcImage.pixmap, pixmap, gc, 0, 0, width, height, 0, 0);
			OS.XFreeGC(xDisplay, gc);
			this.pixmap = pixmap;
			transparentPixel = srcImage.transparentPixel;
			alpha = srcImage.alpha;
			if (srcImage.alphaData != null) {
				alphaData = new byte[srcImage.alphaData.length];
				System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
			}
			return;
		case SWT.IMAGE_DISABLE:
			/* Get src image data */
			XImage srcXImage = new XImage();
			int srcXImagePtr = OS.XGetImage(xDisplay, srcImage.pixmap, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
			OS.memmove(srcXImage, srcXImagePtr, XImage.sizeof);
			byte[] srcData = new byte[srcXImage.bytes_per_line * srcXImage.height];
			OS.memmove(srcData, srcXImage.data, srcData.length);
			/* Create destination image */
			int destPixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, srcXImage.depth);
			XImage destXImage = new XImage();
			int destXImagePtr = OS.XGetImage(xDisplay, drawable, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
			OS.memmove(destXImage, destXImagePtr, XImage.sizeof);
			byte[] destData = new byte[destXImage.bytes_per_line * destXImage.height];
			/* Find the colors to map to */
			Color zeroColor = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			Color oneColor = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			int zeroPixel = zeroColor.handle.pixel;
			int onePixel = oneColor.handle.pixel;
			switch (srcXImage.bits_per_pixel) {
				case 1:
					/**
					 * Nothing we can reasonably do here except copy
					 * the bitmap; we can't make it a higher color depth.
					 * Short-circuit the rest of the code and return.
					 */
					gc = OS.XCreateGC(xDisplay, drawable, 0, null);
					pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, 1);
					OS.XCopyArea(xDisplay, srcImage.pixmap, pixmap, gc, 0, 0, width, height, 0, 0);
					OS.XDestroyImage(srcXImagePtr);
					OS.XDestroyImage(destXImagePtr);
					OS.XFreeGC(xDisplay, gc);
					return;
				case 4:
					SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
					break;
				case 8:
					int index = 0;
					int srcPixel, r, g, b;
					XColor[] colors = new XColor[256];
					int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
					for (int y = 0; y < srcXImage.height; y++) {
						for (int x = 0; x < srcXImage.bytes_per_line; x++) {
							srcPixel = srcData[index + x] & 0xFF;
							/* Get the RGB values of srcPixel */
							if (colors[srcPixel] == null) {
								XColor color = new XColor();
								color.pixel = srcPixel;
								OS.XQueryColor(xDisplay, colormap, color);
								colors[srcPixel] = color;
							}
							XColor xColor = colors[srcPixel];
							r = (xColor.red >> 8) & 0xFF;
							g = (xColor.green >> 8) & 0xFF;
							b = (xColor.blue >> 8) & 0xFF;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + x] = (byte)zeroPixel;
							} else {
								/* Map up to 1 */
								destData[index + x] = (byte)onePixel;
							}
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				case 16:
					index = 0;
					/* Get masks */
					Visual visual = new Visual();
					int screenNum = OS.XDefaultScreen(xDisplay);
					int visualPtr = OS.XDefaultVisual(xDisplay, screenNum);
					OS.memmove(visual, visualPtr, Visual.sizeof);
					int redMask = visual.red_mask;
					int greenMask = visual.green_mask;
					int blueMask = visual.blue_mask;
					/* Calculate mask shifts */
					int[] shift = new int[1];
					getOffsetForMask(16, redMask, srcXImage.byte_order, shift);
					int rShift = 24 - shift[0];
					getOffsetForMask(16, greenMask, srcXImage.byte_order, shift);
					int gShift = 24 - shift[0];
					getOffsetForMask(16, blueMask, srcXImage.byte_order, shift);
					int bShift = 24 - shift[0];
					byte zeroLow = (byte)(zeroPixel & 0xFF);
					byte zeroHigh = (byte)((zeroPixel >> 8) & 0xFF);
					byte oneLow = (byte)(onePixel & 0xFF);
					byte oneHigh = (byte)((onePixel >> 8) & 0xFF);
					for (int y = 0; y < srcXImage.height; y++) {
						int xIndex = 0;
						for (int x = 0; x < srcXImage.bytes_per_line; x += 2) {
							srcPixel = ((srcData[index + xIndex + 1] & 0xFF) << 8) | (srcData[index + xIndex] & 0xFF);
							r = (srcPixel & redMask) << rShift >> 16;
							g = (srcPixel & greenMask) << gShift >> 16;
							b = (srcPixel & blueMask) << bShift >> 16;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + xIndex] = zeroLow;
								destData[index + xIndex + 1] = zeroHigh;
							} else {
								/* Map up to 1 */
								destData[index + xIndex] = oneLow;
								destData[index + xIndex + 1] = oneHigh;
							}
							xIndex += srcXImage.bits_per_pixel / 8;
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				case 24:
				case 32:
					index = 0;
					/* Get masks */
					visual = new Visual();
					screenNum = OS.XDefaultScreen(xDisplay);
					visualPtr = OS.XDefaultVisual(xDisplay, screenNum);
					OS.memmove(visual, visualPtr, Visual.sizeof);
					redMask = visual.red_mask;
					greenMask = visual.green_mask;
					blueMask = visual.blue_mask;
					/* Calculate mask shifts */
					shift = new int[1];
					getOffsetForMask(srcXImage.bits_per_pixel, redMask, srcXImage.byte_order, shift);
					rShift = shift[0];
					getOffsetForMask(srcXImage.bits_per_pixel, greenMask, srcXImage.byte_order, shift);
					gShift = shift[0];
					getOffsetForMask(srcXImage.bits_per_pixel, blueMask, srcXImage.byte_order, shift);
					bShift = shift[0];
					byte zeroR = (byte)zeroColor.getRed();
					byte zeroG = (byte)zeroColor.getGreen();
					byte zeroB = (byte)zeroColor.getBlue();
					byte oneR = (byte)oneColor.getRed();
					byte oneG = (byte)oneColor.getGreen();
					byte oneB = (byte)oneColor.getBlue();
					for (int y = 0; y < srcXImage.height; y++) {
						int xIndex = 0;
						for (int x = 0; x < srcXImage.width; x++) {
							r = srcData[index + xIndex + rShift] & 0xFF;
							g = srcData[index + xIndex + gShift] & 0xFF;
							b = srcData[index + xIndex + bShift] & 0xFF;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + xIndex + rShift] = zeroR;
								destData[index + xIndex + gShift] = zeroG;
								destData[index + xIndex + bShift] = zeroB;
							} else {
								/* Map up to 1 */
								destData[index + xIndex + rShift] = oneR;
								destData[index + xIndex + gShift] = oneG;
								destData[index + xIndex + bShift] = oneB;
							}
							xIndex += destXImage.bits_per_pixel / 8;
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				default:
					SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			OS.memmove(destXImage.data, destData, destData.length);
			gc = OS.XCreateGC(xDisplay, destPixmap, 0, null);
			OS.XPutImage(xDisplay, destPixmap, gc, destXImagePtr, 0, 0, 0, 0, width, height);
			OS.XDestroyImage(destXImagePtr);
			OS.XDestroyImage(srcXImagePtr);
			OS.XFreeGC(xDisplay, gc);
			this.pixmap = destPixmap;
			return;
		case SWT.IMAGE_GRAY:
			ImageData data = srcImage.getImageData();
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
				newData = new ImageData(width, height, 8, new PaletteData(rgbs));
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;

				/* Convert the pixels. */
				int[] scanline = new int[width];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<height; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, width, scanline, 0);
					for (int x=0; x<width; x++) {
						int pixel = scanline[x];
						int red = pixel & redMask;
						red = (redShift < 0) ? red >>> -redShift : red << redShift;
						int green = pixel & greenMask;
						green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
						int blue = pixel & blueMask;
						blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
						newData.data[offset++] =
							(byte)((red+red+green+green+green+green+green+blue) >> 3);
					}
				}
			}
			init (device, newData);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
}
public Image(Device device, Rectangle bounds) {
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, bounds.width, bounds.height);
}
public Image(Device device, ImageData image) {
	init(device, image);
}
public Image(Device device, ImageData source, ImageData mask) {
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (mask.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	ImageData image = new ImageData(source.width, source.height, source.depth, source.palette, source.scanlinePad, source.data);
	image.maskPad = mask.scanlinePad;
	image.maskData = mask.data;
	init(device, image);
}
public Image(Device device, InputStream stream) {
	init(device, new ImageData(stream));
}
public Image(Device device, String filename) {
	init(device, new ImageData(filename));
}
/**
 * Create the receiver's mask if necessary.
 */
void createMask() {
	if (mask != 0) return;
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int screenDepth = OS.XDefaultDepthOfScreen(OS.XDefaultScreenOfDisplay(xDisplay));
	int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	ImageData maskImage = getImageData().getTransparencyMask();
	int maskPixmap = OS.XCreatePixmap(xDisplay, drawable, maskImage.width, maskImage.height, 1);
	XColor[] xcolors = device.xcolors;
	int gc = OS.XCreateGC(xDisplay, maskPixmap, 0, null);
	int error = Image.putImage(maskImage, 0, 0, maskImage.width, maskImage.height, 0, 0, maskImage.width, maskImage.height, xDisplay, visual, screenDepth, xcolors, null, maskPixmap, gc);
	OS.XFreeGC(xDisplay, gc);
	this.mask = maskPixmap;
}
public void dispose () {
	if (pixmap == 0) return;
	int xDisplay = device.xDisplay;
	if (pixmap != 0) OS.XFreePixmap (xDisplay, pixmap);
	if (mask != 0) OS.XFreePixmap (xDisplay, mask);
	device = null;
	memGC = null;
	pixmap = mask = 0;
}
/**
 * Destroy the receiver's mask if it exists.
 */
void destroyMask() {
	if (mask == 0) return;
	OS.XFreePixmap (device.xDisplay, mask);
	mask = 0;
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image)object;
	return device == image.device && pixmap == image.pixmap &&
		transparentPixel == image.transparentPixel &&
		mask == image.mask;
}
public Color getBackground() {
	if (transparentPixel == -1) return null;
	XColor xColor = new XColor();
	xColor.pixel = transparentPixel;
	int xDisplay = device.xDisplay;
	int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
	OS.XQueryColor(xDisplay, colormap, xColor);
	return Color.motif_new(device, xColor);
}
public Rectangle getBounds () {
	if (pixmap == 0) return new Rectangle(0, 0, 0, 0);
	int [] unused = new int [1];  int [] width = new int [1];  int [] height = new int [1];
 	OS.XGetGeometry (device.xDisplay, pixmap, unused, unused, unused, width, height, unused, unused);
	return new Rectangle(0, 0, width [0], height [0]);
}
public ImageData getImageData() {
	Rectangle srcBounds = getBounds();
	int width = srcBounds.width;
	int height = srcBounds.height;
	int xDisplay = device.xDisplay;
	int xSrcImagePtr = OS.XGetImage(xDisplay, pixmap, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
	if (xSrcImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	XImage xSrcImage = new XImage();
	OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
	/* Calculate the palette depending on the display attributes */
	PaletteData palette = null;
	switch (xSrcImage.depth) {
		case 1:
			palette = new PaletteData(new RGB[] {
				new RGB(0, 0, 0),
				new RGB(255, 255, 255)
			});
			break;
		case 4:
			/**
			 * We currently don't run on a 4-bit server, so 4-bit images
			 * should not exist.
			 */
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		case 8:
			/* Use the RGBs from the display to make the palette */
			XColor[] xcolors = device.xcolors;
			RGB[] rgbs = new RGB[xcolors.length];
			for (int i = 0; i < rgbs.length; i++) {
				XColor xcolor = xcolors[i];
				if (xcolor == null) rgbs[i] = new RGB(0, 0, 0);
				else rgbs[i] = new RGB((xcolor.red >> 8) & 0xFF, (xcolor.green >> 8) & 0xFF, (xcolor.blue >> 8) & 0xFF);
			}
			palette = new PaletteData(rgbs);
			break;
		case 16:
			/**
			 * For some reason, the XImage does not have the mask information.
			 * We must get it from the visual.
			 */
			int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
			Visual v = new Visual();
			OS.memmove(v, visual, Visual.sizeof);
			palette = new PaletteData(v.red_mask, v.green_mask, v.blue_mask);
			break;
		case 24:
			/* We always create 24-bit ImageData with the following palette */
			palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	ImageData data = new ImageData(width, height, xSrcImage.depth, palette);
	int length = xSrcImage.bytes_per_line * xSrcImage.height;
	data.data = new byte[length];
	OS.memmove(data.data, xSrcImage.data, length);
	if (xSrcImage.bits_per_pixel == 32) {
		/**
		 * If bits per pixel is 32, scale the data down to 24, since we do not
		 * support 32-bit images
		 */
		byte[] oldData = data.data;
		int bytesPerLine = (xSrcImage.width * xSrcImage.depth + 7) / 8;
		bytesPerLine = (bytesPerLine + 3) / 4 * 4;
		byte[] newData = new byte[bytesPerLine * xSrcImage.height];
		int destIndex = 0;
		int srcIndex = 0;
		int rOffset = 0, gOffset = 1, bOffset = 2;
		if (xSrcImage.byte_order == OS.MSBFirst) {
			rOffset = 2; gOffset = 1; bOffset = 0;
		}
		for (int y = 0; y < height; y++) {
			destIndex = y * bytesPerLine;
			srcIndex = y * xSrcImage.bytes_per_line;
			for (int x = 0; x < width; x++) {
				newData[destIndex] = oldData[srcIndex + rOffset];
				newData[destIndex + 1] = oldData[srcIndex + gOffset];
				newData[destIndex + 2] = oldData[srcIndex + bOffset];
				srcIndex += 4;
				destIndex += 3;
			}
		}
		data.data = newData;
	}
	if (transparentPixel == -1 && type == SWT.ICON && mask != 0) {
		/* Get the icon data */
		data.maskPad = 4;
		int xMaskPtr = OS.XGetImage(xDisplay, mask, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
		if (xMaskPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xMask = new XImage();
		OS.memmove(xMask, xMaskPtr, XImage.sizeof);
		data.maskData = new byte[xMask.bytes_per_line * xMask.height];
		OS.memmove(data.maskData, xMask.data, data.maskData.length);
		OS.XDestroyImage(xMaskPtr);
		/* Bit swap the mask data if necessary */
		if (xMask.bitmap_bit_order == OS.LSBFirst) {
			byte[] maskData = data.maskData;
			for (int i = 0; i < maskData.length; i++) {
				byte b = maskData[i];
				maskData[i] = (byte)(((b & 0x01) << 7) | ((b & 0x02) << 5) | 
					((b & 0x04) << 3) |	((b & 0x08) << 1) | ((b & 0x10) >> 1) | 
					((b & 0x20) >> 3) |	((b & 0x40) >> 5) | ((b & 0x80) >> 7));
			}
		}
	}
	data.transparentPixel = transparentPixel;
	data.alpha = alpha;
	if (alpha == -1 && alphaData != null) {
		data.alphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, data.alphaData, 0, alphaData.length);
	}
	OS.XDestroyImage(xSrcImagePtr);
	return data;
}
/**
 * Get the offset for the given mask.
 *
 * For 24 and 32-bit masks, the offset indicates which byte holds the
 *  data for the given mask (indexed from 0).
 *  For example, in 0x0000FF00, the byte offset is 1.
 *
 * For 16-bit masks, the offset indicates which bit holds the most significant
 *  data for the given mask (indexed from 1).
 *  For example, in 0x7E0, the bit offset is 11.
 *
 * The different semantics are necessary because 24- and 32-bit images
 * have their color components aligned on byte boundaries, and 16-bit images
 * do not.
 */
static boolean getOffsetForMask(int bitspp, int mask, int byteOrder, int[] poff) {
	if (bitspp % 8 != 0) {
		return false;
	}
	switch (mask) {
		/* 24-bit and 32-bit masks */
		case 0x000000FF:
			poff[0] = 0;
			break;
		case 0x0000FF00:
			poff[0] = 1;
			break;
		case 0x00FF0000:
			poff[0] = 2;
			break;
		case 0xFF000000:
			poff[0] = 3;
			break;
		/* 16-bit masks */
		case 0x001F:
			poff[0] = 5;
			break;
		case 0x03E0:
			poff[0] = 10;
			break;
		case 0x07E0:
			poff[0] = 11;
			break;
		case 0x7C00:
			poff[0] = 15;
			break;
		case 0xF800:
			poff[0] = 16;
			break;
		default:
			return false;
	}
	if (bitspp == 16) {
		return true;
	}
	if (poff[0] >= bitspp / 8) {
		return false;
	}
	if (byteOrder == OS.MSBFirst) {
		poff[0] = (bitspp/8 - 1) - poff[0];
	}
	return true;
}
public int hashCode () {
	return pixmap;
}
void init(Device device, int width, int height) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	/* Create the pixmap */
	if (width <= 0 | height <= 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	this.type = SWT.BITMAP;
	int xDisplay = device.xDisplay;
	int screen = OS.XDefaultScreenOfDisplay(xDisplay);
	int depth = OS.XDefaultDepthOfScreen(screen);
	int screenNum = OS.XDefaultScreen(xDisplay);
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int xGC = OS.XCreateGC(xDisplay, drawable, 0, null);
	this.pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, depth);
	/* Fill the bitmap with white */
	OS.XSetForeground(xDisplay, xGC, OS.XWhitePixel(xDisplay, screenNum));
	OS.XFillRectangle(xDisplay, pixmap, xGC, 0, 0, width, height);
	OS.XFreeGC(xDisplay, xGC);
}
void init(Device device, ImageData image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int screenDepth = OS.XDefaultDepthOfScreen(OS.XDefaultScreenOfDisplay(xDisplay));
	int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	int pixmap = OS.XCreatePixmap(xDisplay, drawable, image.width, image.height, screenDepth);
	int gc = OS.XCreateGC(xDisplay, pixmap, 0, null);
	int[] transPixel = null;
	if (image.transparentPixel != -1) transPixel = new int[]{image.transparentPixel};
	int error = putImage(image, 0, 0, image.width, image.height, 0, 0, image.width, image.height, xDisplay, visual, screenDepth, device.xcolors, transPixel, pixmap, gc);
	OS.XFreeGC(xDisplay, gc);
	if (error != 0) {
		OS.XFreePixmap (xDisplay, pixmap);
		SWT.error(error);
	}
	if (image.getTransparencyType() == SWT.TRANSPARENCY_MASK || image.transparentPixel != -1) {
		if (image.transparentPixel != -1) transparentPixel = transPixel[0];
		ImageData maskImage = image.getTransparencyMask();
		int mask = OS.XCreatePixmap(xDisplay, drawable, image.width, image.height, 1);
		gc = OS.XCreateGC(xDisplay, mask, 0, null);
		error = putImage(maskImage, 0, 0, maskImage.width, maskImage.height, 0, 0, maskImage.width, maskImage.height, xDisplay, visual, screenDepth, device.xcolors, null, mask, gc);
		OS.XFreeGC(xDisplay, gc);
		if (error != 0) {
			OS.XFreePixmap (xDisplay, pixmap);
			OS.XFreePixmap (xDisplay, mask);
			SWT.error(error);
		}
		this.mask = mask;
		if (image.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
			this.type = SWT.ICON;
		} else {
			this.type = SWT.BITMAP;
		}
	} else {
		this.type = SWT.BITMAP;
		this.mask = 0;
		this.alpha = image.alpha;
		if (image.alpha == -1 && image.alphaData != null) {
			this.alphaData = new byte[image.alphaData.length];
			System.arraycopy(image.alphaData, 0, this.alphaData, 0, alphaData.length);
		}
	}
	this.pixmap = pixmap;
}
public int internal_new_GC (GCData data) {
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int xDisplay = device.xDisplay;
	int xGC = OS.XCreateGC (xDisplay, pixmap, 0, null);
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = device;
		data.display = xDisplay;
		data.drawable = pixmap;
		data.fontList = device.getSystemFont ().handle;
		data.colormap = OS.XDefaultColormap (xDisplay, OS.XDefaultScreen (xDisplay));
		data.image = this;
	}
	return xGC;
}
public void internal_dispose_GC (int gc, GCData data) {
	int xDisplay = device.xDisplay;
	OS.XFreeGC(xDisplay, gc);
}
public boolean isDisposed() {
	return pixmap == 0;
}
public static Image motif_new(Device device, int type, int pixmap, int mask) {
	if (device == null) device = Device.getDevice();
	Image image = new Image();
	image.device = device;
	image.type = type;
	image.pixmap = pixmap;
	image.mask = mask;
	return image;
}
/**
 * Put a device-independent image of any depth into a drawable of any depth, 
 * stretching if necessary.
 */
static int putImage(ImageData image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, int display, int visual, int screenDepth, XColor[] xcolors, int[] transparentPixel, int drawable, int gc) {
	PaletteData palette = image.palette;
	if (!(((image.depth == 1 || image.depth == 2 || image.depth == 4 || image.depth == 8) && !palette.isDirect) ||
		((image.depth == 16 || image.depth == 24) && palette.isDirect)))
			return SWT.ERROR_UNSUPPORTED_DEPTH;

	boolean flipX = destWidth < 0;
	boolean flipY = destHeight < 0;
	if (flipX) {
		destWidth = -destWidth;
		destX = destX - destWidth;
	}
	if (flipY) {
		destHeight = -destHeight;
		destY = destY - destHeight;
	}
	byte[] srcReds = null, srcGreens = null, srcBlues = null;
	if (image.depth <= 8) {
		int length = palette.getRGBs().length;
		srcReds = new byte[length];
		srcGreens = new byte[length];
		srcBlues = new byte[length];
		RGB[] rgbs = palette.getRGBs();
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			if (rgb == null) continue;
			srcReds[i] = (byte)rgb.red;
			srcGreens[i] = (byte)rgb.green;
			srcBlues[i] = (byte)rgb.blue;
		}
	}
	byte[] destReds = null, destGreens = null, destBlues = null;
	int destRedMask = 0, destGreenMask = 0, destBlueMask = 0;
	if (screenDepth <= 8) {
		if (xcolors == null) return SWT.ERROR_UNSUPPORTED_DEPTH;
		destReds = new byte[xcolors.length];
		destGreens = new byte[xcolors.length];
		destBlues = new byte[xcolors.length];
		for (int i = 0; i < xcolors.length; i++) {
			XColor color = xcolors[i];
			if (color == null) continue;
			destReds[i] = (byte)((color.red >> 8) & 0xFF);
			destGreens[i] = (byte)((color.green >> 8) & 0xFF);
			destBlues[i] = (byte)((color.blue >> 8) & 0xFF);
		}
	} else {
		Visual xVisual = new Visual();
		OS.memmove(xVisual, visual, Visual.sizeof);
		destRedMask = xVisual.red_mask;
		destGreenMask = xVisual.green_mask;
		destBlueMask = xVisual.blue_mask;
	}
	if (transparentPixel != null) {
		RGB rgb = image.palette.getRGB(transparentPixel[0]);
		transparentPixel[0] = ImageData.closestMatch(screenDepth, (byte)rgb.red, (byte)rgb.green, (byte)rgb.blue,
			destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
	}

	/* Depth 1 */
	if (image.depth == 1) {
		int bplX = ((destWidth + 7) / 8 + 3) & 0xFFFC;
		int bufSize = bplX * destHeight;
		byte[] buf = new byte[bufSize];
		int bufPtr = OS.XtMalloc(bufSize);
		int xImagePtr = OS.XCreateImage(display, visual, 1, OS.XYBitmap, 0, bufPtr, destWidth, destHeight, 32, bplX);
		if (xImagePtr == 0) {
			OS.XtFree(bufPtr);
			return SWT.ERROR_NO_HANDLES;
		}
		int foreground = 0, background = 0;
		if (srcReds.length > 1) {
			foreground = ImageData.closestMatch(screenDepth, srcReds[1], srcGreens[1], srcBlues[1],
				destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
		}
		if (srcReds.length > 0) {
			background = ImageData.closestMatch(screenDepth, srcReds[0], srcGreens[0], srcBlues[0],
				destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
		}
		XImage xImage = new XImage();
		OS.memmove(xImage, xImagePtr, XImage.sizeof);
		xImage.byte_order = OS.MSBFirst;
		xImage.bitmap_unit = 8;
		xImage.bitmap_bit_order = OS.MSBFirst;
		OS.memmove(xImagePtr, xImage, XImage.sizeof);
		int destOrder = ImageData.MSB_FIRST;
		ImageData.stretch1(image.data, image.bytesPerLine, ImageData.MSB_FIRST, srcX, srcY, srcWidth, srcHeight,
			buf, bplX, ImageData.MSB_FIRST, 0, 0, destWidth, destHeight, flipX, flipY);
		OS.memmove(xImage.data, buf, bufSize);
		XGCValues values = new XGCValues();
		OS.XGetGCValues(display, gc, OS.GCForeground | OS.GCBackground, values);
		OS.XSetForeground(display, gc, foreground);
		OS.XSetBackground(display, gc, background);
		OS.XPutImage(display, drawable, gc, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
		OS.XSetForeground(display, gc, values.foreground);
		OS.XSetBackground(display, gc, values.background);
		OS.XDestroyImage(xImagePtr);	
		return 0;
	}
	
	/* Depths other than 1 */
	int xImagePtr = OS.XCreateImage(display, visual, screenDepth, OS.ZPixmap, 0, 0, destWidth, destHeight, 32, 0);
	if (xImagePtr == 0) return SWT.ERROR_NO_HANDLES;
	XImage xImage = new XImage();
	OS.memmove(xImage, xImagePtr, XImage.sizeof);
	int bufSize = xImage.bytes_per_line * destHeight;
	byte[] buf = new byte[bufSize];
	int bufPtr = OS.XtMalloc(bufSize);
	xImage.data = bufPtr;
	OS.memmove(xImagePtr, xImage, XImage.sizeof);
	int srcOrder = image.depth == 16 ? ImageData.LSB_FIRST : ImageData.MSB_FIRST;
	int destOrder = xImage.byte_order == OS.MSBFirst ? ImageData.MSB_FIRST : ImageData.LSB_FIRST;
	if (image.depth > 8 && screenDepth > 8) {
		ImageData.blit(ImageData.BLIT_SRC,
			image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask, -1, null, 0,
			buf, xImage.bits_per_pixel, xImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, xImage.red_mask, xImage.green_mask, xImage.blue_mask,
			flipX, flipY);
	} else if (image.depth <= 8 && screenDepth > 8) {
		ImageData.blit(ImageData.BLIT_SRC,
			image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues, -1, null, 0,
			buf, xImage.bits_per_pixel, xImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, xImage.red_mask, xImage.green_mask, xImage.blue_mask,
			flipX, flipY);
	} else if (image.depth > 8 && screenDepth <= 8) {
		ImageData.blit(ImageData.BLIT_SRC,
			image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask, -1, null, 0,
			buf, xImage.bits_per_pixel, xImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
			flipX, flipY);
	} else if (image.depth <= 8 && screenDepth <= 8) {
		ImageData.blit(ImageData.BLIT_SRC,
			image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues, -1, null, 0,
			buf, xImage.bits_per_pixel, xImage.bytes_per_line, destOrder, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
			flipX, flipY);
	}
	OS.memmove(xImage.data, buf, bufSize);
	OS.XPutImage(display, drawable, gc, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
	OS.XDestroyImage(xImagePtr);
	return 0;
}
public void setBackground(Color color) {
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (transparentPixel == -1) return;
	/* Generate the mask if necessary. */
	if (mask == 0) createMask();
	Rectangle bounds = getBounds();
	int[] unused = new int[1];
	int[] depth = new int[1];
	int xDisplay = device.xDisplay;
 	OS.XGetGeometry(xDisplay, pixmap, unused, unused, unused, unused, unused, unused, depth);
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int tempPixmap = OS.XCreatePixmap(xDisplay, drawable, bounds.width, bounds.height, depth[0]);
	int xGC = OS.XCreateGC(xDisplay, tempPixmap, 0, null);
	OS.XSetForeground(xDisplay, xGC, color.handle.pixel);
	OS.XFillRectangle(xDisplay, tempPixmap, xGC, 0, 0, bounds.width, bounds.height);
	OS.XSetClipMask(xDisplay, xGC, mask);
	OS.XCopyArea(xDisplay, pixmap, tempPixmap, xGC, 0, 0, bounds.width, bounds.height, 0, 0);
	OS.XSetClipMask(xDisplay, xGC, OS.None);
	OS.XCopyArea(xDisplay, tempPixmap, pixmap, xGC, 0, 0, bounds.width, bounds.height, 0, 0);
	OS.XFreePixmap(xDisplay, tempPixmap);
	OS.XFreeGC(xDisplay, xGC);
	/* Destroy the receiver's mask if the there is a GC created on it */
	if (memGC != null) destroyMask();
}
}
