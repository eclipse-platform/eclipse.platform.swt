package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import java.io.*;

public final class Image implements Drawable {

	/**
	 * specifies whether the receiver is a bitmap or an icon
	 * (one of <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 */
	public int type;

	/**
	 * the OS resource of the image
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
	/**
	 * the device where this image was created
	 */
	Device device;
	
	/**
	 * specifies the transparent pixel
	 * (Warning: This field is platform dependent)
	 */
	int transparentPixel = -1;
	
	/**
	 * the GC which is drawing on the image
	 * (Warning: This field is platform dependent)
	 */
	GC memGC;

	/**
	 * specifies the default scanline padding
	 * (Warning: This field is platform dependent)
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

Image () {
}

public Image(Device device, int width, int height) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, width, height);
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, Image srcImage, int flag) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	switch (flag) {
		case SWT.IMAGE_COPY:
		case SWT.IMAGE_DISABLE:
			this.type = srcImage.type;
			int imageHandle = srcImage.handle;
			if (imageHandle != 0) {
				imageHandle = OS.PiDuplicateImage (imageHandle, 0);
				if (imageHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				PhImage_t phImage = new PhImage_t();
				if (flag == SWT.IMAGE_COPY) {
					/*
					 * Bug in Photon - The image returned by PiDuplicateImage
					 * has the same mask_bm as the original image.
					 */
					OS.memmove (phImage, imageHandle, PhImage_t.sizeof);
					if (phImage.mask_bm != 0) {
						int length = phImage.mask_bpl * phImage.size_h;
						int ptr = OS.malloc (length);
						OS.memmove(ptr, phImage.mask_bm, length);
						phImage.mask_bm = ptr;
						OS.memmove (imageHandle, phImage, PhImage_t.sizeof);
					}
					/*
					 * Bug in Photon - The image returned by PiDuplicateImage
					 * has the same alpha as the original image.
					 */
					if (phImage.alpha != 0) {
						PgAlpha_t alpha = new PgAlpha_t();
						OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
						if (alpha.src_alpha_map_map != 0) {
							int length = alpha.src_alpha_map_dim_w * alpha.src_alpha_map_dim_h;
							int ptr = OS.malloc(length);
							OS.memmove(ptr, alpha.src_alpha_map_map, length);
						}
						int ptr = OS.malloc(PgAlpha_t.sizeof);
						OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
						phImage.alpha = ptr;
						OS.memmove (imageHandle, phImage, PhImage_t.sizeof);
					}
					transparentPixel = srcImage.transparentPixel;
				} else {
					OS.PhMakeGhostBitmap(imageHandle);
					OS.memmove (phImage, imageHandle, PhImage_t.sizeof);
					phImage.mask_bm = phImage.ghost_bitmap;
					phImage.mask_bpl = phImage.ghost_bpl;
					phImage.ghost_bitmap = 0;
					phImage.ghost_bpl = 0;
					phImage.alpha = 0;
					OS.memmove (imageHandle, phImage, PhImage_t.sizeof);
				}
			}
			handle = imageHandle;
			if (device.tracking) device.new_Object(this);	
			return;
		case SWT.IMAGE_GRAY:
			Rectangle r = srcImage.getBounds();
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
				newData = new ImageData(r.width, r.height, 8, new PaletteData(rgbs));
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;

				/* Convert the pixels. */
				int[] scanline = new int[r.width];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<r.height; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, r.width, scanline, 0);
					for (int x=0; x<r.width; x++) {
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
			if (device.tracking) device.new_Object(this);	
			return;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
}

public Image(Device device, Rectangle bounds) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, bounds.width, bounds.height);
	if (device.tracking) device.new_Object(this);	
}

public Image(Device device, ImageData data) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, data);
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, ImageData source, ImageData mask) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	if (device.tracking) device.new_Object(this);	
}

public Image (Device device, InputStream stream) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(stream));
	if (device.tracking) device.new_Object(this);
}

public Image (Device device, String filename) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(filename));
	if (device.tracking) device.new_Object(this);	
}

public void dispose () {
	if (handle == 0) return;
	destroyImage(handle);
	handle = 0;
	memGC = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image) object;
	return device == image.device && handle == image.handle;
}

public Color getBackground() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transparentPixel == -1) return null;

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int color = 0;
	if ((phImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_PALETTE) {
		int phPalette = phImage.palette;
		if (phPalette == 0 || transparentPixel > phImage.colors) return null;
		int[] pgColor = new int[1];
		OS.memmove(pgColor, phPalette + (transparentPixel * 4), 4);
		color = pgColor[0];
	} else {
		switch (phImage.type) {
 			case OS.Pg_IMAGE_DIRECT_888:
				color = ((transparentPixel & 0xFF) << 16) | (transparentPixel & 0xFF00) | ((transparentPixel & 0xFF0000) >> 16);
				break;
			case OS.Pg_IMAGE_DIRECT_8888:
				color = ((transparentPixel & 0xFF00) << 8) | ((transparentPixel & 0xFF0000) >> 8) | ((transparentPixel & 0xFF000000) >> 24);
				break;
			case OS.Pg_IMAGE_DIRECT_565:
				color = ((transparentPixel & 0xF800) << 8) | ((transparentPixel & 0x7E0) << 5) | ((transparentPixel & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_555:
				color = ((transparentPixel & 0x7C00) << 9) | ((transparentPixel & 0x3E0) << 6) | ((transparentPixel & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_444:
				color = ((transparentPixel & 0xF00) << 12) | ((transparentPixel & 0xF0) << 8) | ((transparentPixel & 0xF) << 4);
				break;
			default:
				return null;
		}
	}
	return Color.photon_new(device, color);
}

public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhImage_t image = new PhImage_t();
	OS.memmove(image, handle, PhImage_t.sizeof);
	return new Rectangle(0, 0, image.size_w, image.size_h);
}

public ImageData getImageData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int depth = 0;
	PaletteData palette = null;
	switch (phImage.type) {
		case OS.Pg_IMAGE_DIRECT_555:
			depth = 16;
			palette = new PaletteData(0x7C00,0x3E0,0x1F);
			break;
		case OS.Pg_IMAGE_DIRECT_565:
			depth = 16;
			palette = new PaletteData(0xF800,0x7E0,0x1F);
			break;
		case OS.Pg_IMAGE_DIRECT_444:     
			depth = 16;
			palette = new PaletteData(0xF00,0xF0,0xF);
			break;
		case OS.Pg_IMAGE_DIRECT_888:     
			depth = 24;
			palette = new PaletteData(0xFF,0xFF00,0xFF0000);
			break;
		case OS.Pg_IMAGE_DIRECT_8888:     
			depth = 32;
			palette = new PaletteData(0xFF00,0xFF0000,0xFF000000);
			break;
		case -1:
			depth = 1;
			palette = new PaletteData(new RGB[] {new RGB(0,0,0), new RGB(255,255,255)});
			break;			
		case OS.Pg_IMAGE_PALETTE_NIBBLE: 
		case OS.Pg_IMAGE_PALETTE_BYTE:
			depth = phImage.type == OS.Pg_IMAGE_PALETTE_BYTE ? 8 : 4;
			RGB[] rgbs = new RGB[phImage.colors];
			int[] colors = new int[phImage.colors];
			OS.memmove(colors, phImage.palette, colors.length * 4);
			for (int i = 0; i < rgbs.length; i++) {
				int rgb = colors[i];
				rgbs[i] = new RGB((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
			}
			palette = new PaletteData(rgbs);
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}

	int calcBpl, scanLinePad, bpl = phImage.bpl;
	int width = phImage.size_w, height = phImage.size_h;
	int dataBytesPerLine = (width * depth + 7) / 8;
	for (scanLinePad = 1; scanLinePad < 128; scanLinePad++) {
		calcBpl = (dataBytesPerLine + (scanLinePad - 1)) / scanLinePad * scanLinePad;
		if (bpl == calcBpl) break;
	}	
	byte[] data = new byte[height * bpl];
	OS.memmove(data, phImage.image, data.length);
	
	ImageData imageData = new ImageData(width, height, depth, palette, scanLinePad, data);

	if (transparentPixel != -1) {
		imageData.transparentPixel = transparentPixel;
	} else if (phImage.mask_bm != 0) {
		imageData.maskData = new byte[height * phImage.mask_bpl];
		OS.memmove(imageData.maskData, phImage.mask_bm, imageData.maskData.length);
		imageData.maskPad = 4;
	} else if (phImage.alpha != 0) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		imageData.alpha = alpha.src_global_alpha;
		if ((alpha.alpha_op & OS.Pg_ALPHA_OP_SRC_MAP) != 0 && alpha.src_alpha_map_map != 0) {
			int length = alpha.src_alpha_map_dim_w * alpha.src_alpha_map_dim_h;
			imageData.alphaData = new byte[length];
			OS.memmove(imageData.alphaData, alpha.src_alpha_map_map, length);
		}
	}
	
	return imageData;
}

public int hashCode () {
	return handle;
}

void init(Device device, int width, int height) {
	if (width <= 0 | height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.device = device;
	this.type = SWT.BITMAP;
	
	handle = OS.PhCreateImage(null, (short)width, (short)height, OS.Pg_IMAGE_DIRECT_888, 0, 0, 0);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

void init(Device device, ImageData i) {
	if (i == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	
	/* Photon does not support 1 & 2-bit images. Convert to 4-bit image. */
	if (i.depth == 1 || i.depth == 2) {
		ImageData img = new ImageData(i.width, i.height, 4, i.palette);
		ImageData.blit(ImageData.BLIT_SRC, 
			i.data, i.depth, i.bytesPerLine, img.getByteOrder(), 0, 0, i.width, i.height, null, null, null,
			ImageData.ALPHA_OPAQUE, null, 0,
			img.data, img.depth, img.bytesPerLine, img.getByteOrder(), 0, 0, img.width, img.height, null, null, null, 
			false, false);
		img.transparentPixel = i.transparentPixel;
		img.maskPad = i.maskPad;
		img.maskData = i.maskData;
		img.alpha = i.alpha;
		img.alphaData = i.alphaData;
		i = img;
	}

	int type = 0;
	int[] phPalette = null;
	if (!i.palette.isDirect) {
		switch (i.depth) {
			case 4: type = OS.Pg_IMAGE_PALETTE_NIBBLE; break;
			case 8: type = OS.Pg_IMAGE_PALETTE_BYTE; break;
			default: SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);			
		}
		RGB[] rgbs = i.palette.getRGBs();
		phPalette = new int[rgbs.length];
		for (int j=0; j<rgbs.length; j++) {
			RGB rgb = rgbs[j];
			phPalette[j] = ((rgb.red & 0xFF) << 16) | ((rgb.green & 0xFF) << 8) | (rgb.blue & 0xFF);
		}
	} else {
		final PaletteData palette = i.palette;
		final int redMask = palette.redMask;
		final int greenMask = palette.greenMask;
		final int blueMask = palette.blueMask;
		int newDepth = i.depth;
		int newOrder = ImageData.MSB_FIRST;
		PaletteData newPalette = null;

		switch (i.depth) {
			case 8:
				newDepth = 8;
				newOrder = ImageData.LSB_FIRST;
				newPalette = new PaletteData(0xF800, 0x7E0, 0x1F);
				type = OS.Pg_IMAGE_DIRECT_565;
				break;
			case 16:
				newOrder = ImageData.LSB_FIRST;
				if (redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F) {
					type = OS.Pg_IMAGE_DIRECT_555;
				} else if (redMask == 0xF800 && greenMask == 0x7E0 && blueMask == 0x1F) {
					type = OS.Pg_IMAGE_DIRECT_565;
				} else if (redMask == 0xF00 && greenMask == 0xF0 && blueMask == 0xF) {
					type = OS.Pg_IMAGE_DIRECT_444;
				} else {
					type = OS.Pg_IMAGE_DIRECT_565;
					newPalette = new PaletteData(0xF800, 0x7E0, 0x1F);
				}
				break;
			case 24:
				if (redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000) {
					type = OS.Pg_IMAGE_DIRECT_888;
				} else {
					type = OS.Pg_IMAGE_DIRECT_888;
					newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				}
				break;
			case 32:
				if (redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000) {
					type = OS.Pg_IMAGE_DIRECT_8888;
				} else {
					type = OS.Pg_IMAGE_DIRECT_8888;
					newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
				}
				break;			
			default: 
				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		if (newPalette != null) {
			ImageData img = new ImageData(i.width, i.height, newDepth, newPalette);
			ImageData.blit(ImageData.BLIT_SRC, 
					i.data, i.depth, i.bytesPerLine, i.getByteOrder(), 0, 0, i.width, i.height, redMask, greenMask, blueMask,
					ImageData.ALPHA_OPAQUE, null, 0,
					img.data, img.depth, img.bytesPerLine, newOrder, 0, 0, img.width, img.height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
					false, false);
			if (i.transparentPixel != -1) {
				img.transparentPixel = newPalette.getPixel(palette.getRGB(i.transparentPixel));
			}
			img.maskPad = i.maskPad;
			img.maskData = i.maskData;
			img.alpha = i.alpha;
			img.alphaData = i.alphaData;
			i = img;
		}
	}
	int handle = OS.malloc(PhImage_t.sizeof);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	PhImage_t phImage = new PhImage_t();
	phImage.type = type;
	phImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
	int size = i.data.length;
	int ptr = OS.malloc(size);
	if (ptr == 0) {
		OS.free(handle);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	OS.memmove(ptr, i.data, size);
	phImage.image = ptr;
	phImage.size_w = (short)i.width;
	phImage.size_h = (short)i.height;
	phImage.bpl = i.bytesPerLine;
	if (phPalette != null) {
		size = phPalette.length * 4;
		ptr = OS.malloc(size);
		if (ptr == 0) {
			OS.free(phImage.image);
			OS.free(handle);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.memmove(ptr, phPalette, size);
		phImage.palette = ptr;
		phImage.colors = phPalette.length;
	}
	if (i.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		this.type = SWT.ICON;
		int maskBpl = (i.width * 1 + 7) / 8;
		maskBpl = (maskBpl + (i.maskPad - 1)) / i.maskPad * i.maskPad;
		size = maskBpl * i.height;		
		ptr = OS.malloc(size);
		if (ptr == 0) {
			if (phImage.palette != 0) OS.free(phImage.palette);
			OS.free(phImage.image);
			OS.free(handle);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.memmove(ptr, i.maskData, size); 
		phImage.mask_bm = ptr;
		phImage.mask_bpl = maskBpl;
	} else {
		this.type = SWT.BITMAP;
		if (i.transparentPixel != -1)	{
			/*
			* The PhImage_t field transparent can not used to store the
			* transparent pixel because it is overwritten when a GC is
			* created on the image.
			*/
			transparentPixel = i.transparentPixel;
		} else if (i.alpha != -1 || i.alphaData != null) {
			PgAlpha_t alpha = new PgAlpha_t();
			alpha.alpha_op = i.alpha != -1 ? OS.Pg_ALPHA_OP_SRC_GLOBAL : OS.Pg_ALPHA_OP_SRC_MAP;
			alpha.alpha_op |= OS.Pg_BLEND_SRC_SRC_ALPHA | OS.Pg_BLEND_DST_ONE_MINUS_SRC_ALPHA;
			alpha.src_global_alpha = (byte)i.alpha;
			if (i.alpha == -1 && i.alphaData != null) {
				ptr = OS.malloc(i.alphaData.length);
				if (ptr == 0) {
					if (phImage.palette != 0) OS.free(phImage.palette);
					OS.free(phImage.image);
					OS.free(handle);
					SWT.error(SWT.ERROR_NO_HANDLES);
				}
				OS.memmove(ptr, i.alphaData, i.alphaData.length);
				alpha.src_alpha_map_bpl = (short)i.width;
				alpha.src_alpha_map_dim_w = (short)i.width;
				alpha.src_alpha_map_dim_h = (short)i.height;
				alpha.src_alpha_map_map = ptr;
			}
			ptr = OS.malloc(PgAlpha_t.sizeof);
			if (ptr == 0) {
				if (alpha.src_alpha_map_map != 0) OS.free(alpha.src_alpha_map_map);
				if (phImage.palette != 0) OS.free(phImage.palette);
				OS.free(phImage.image);
				OS.free(handle);
				SWT.error(SWT.ERROR_NO_HANDLES);
			}
			OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
			phImage.alpha = ptr;
		}
	}
	OS.memmove(handle, phImage, PhImage_t.sizeof);
	this.handle = handle;
}

public int internal_new_GC (GCData data) {
	/*
	* Create a new GC that can draw into the image.
	* Only supported for bitmaps.
	*/
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	PhDim_t dim = new PhDim_t();
	dim.w = phImage.size_w;
	dim.h = phImage.size_h;
	PhPoint_t trans = new PhPoint_t();
	int pmMC = OS.PmMemCreateMC(handle, dim, trans);
	if (pmMC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	data.device = device;
	data.image = this;
	return pmMC;
}

public void internal_dispose_GC (int pmMC, GCData data) {
	OS.PmMemReleaseMC(pmMC);
}

public boolean isDisposed() {
	return handle == 0;
}

public void setBackground(Color color) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (transparentPixel == -1) return;

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int phPalette = phImage.palette;
	if (phPalette == 0 || transparentPixel > phImage.colors) return;
	int[] pgColor = new int[]{ color.handle };
	OS.memmove(phPalette + (transparentPixel * 4), pgColor, 4);
}

static void destroyImage(int image) {
	if (image == 0) return;
	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, image, PhImage_t.sizeof);
	phImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
	OS.memmove(image, phImage, PhImage_t.sizeof);
	OS.PhReleaseImage(image);
	OS.free(image);
}

public static Image photon_new(Device device, int type, int handle) {
	if (device == null) device = Device.getDevice();
	Image image = new Image();
	image.type = type;
	image.handle = handle;
	image.device = device;
	return image;
}

public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	return "Image {" + handle + "}";
}

}
