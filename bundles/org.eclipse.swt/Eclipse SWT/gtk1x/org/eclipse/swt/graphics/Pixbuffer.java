package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.gtk.*;

/**
 * This class is <strong>not</strong> part of the SWT API,
 * and its existence is not relevant for application programmers.
 * 
 * Pixbuffer represents local GdkPixbuf images on the client.
 */
final class Pixbuffer {
	
	/* the handle to the OS resource.
	   All state is kept in the OS */
	int handle;
	
	/* pointer to the actual pixel array */
	int data;

	/* whether the alpha data in the pixbuf is  due to
	   a transparency mask or an alpha channel */
	boolean hasMask   = false;
	boolean hasAlpha  = false;
	int constantAlpha = -1;
	int transparentPixel = -1;

	
	/*
	 * Constructors.
	 * There are three ways to create a Pixbuffer:
	 * pull one from a Drawable, create from ImageData,
	 * or clone an existing Pixbuffer.
	 */
	
	private Pixbuffer() {}

	/**
	 * Pull a Pixbuffer from an Image living on the X Server
	 * (making this operation expensive).
	 */
	Pixbuffer (Image src) {
		if (src == null || src.pixmap == 0) SWT.error(SWT.ERROR_NULL_ARGUMENT);

		// get the geometry
		int[] unused = new int[1];
		int[] w = new int[1];
		int[] h = new int[1];
		int[] d = new int[1];
		OS.gdk_window_get_geometry(src.pixmap, unused, unused, w, h, unused);
	 	int width = w[0];
	 	int height = h[0];

		// create the actual OS resource
		createHandle(width, height);
		
		// pull the color data
		int cmap = OS.gdk_colormap_get_system();
		GDKPIXBUF.gdk_pixbuf_get_from_drawable(
			handle,
			src.pixmap,
			cmap,
			0,0,0,0,
			width, height);
		
		// the tricky part is alpha
		if (src.alphaData != null) fillAlphaFromAlphaBytes(src.alphaData);
		else if (src.alpha != -1) fillConstantAlpha(src.alpha);
		else if (src.mask != 0) fillAlphaFromPixmapMask(src.mask);
		else if (src.transparentPixel != -1) fillAlphaFromTransparentPixel(src.pixmap, src.transparentPixel);
		else fillOpaque();
	}
	
	/**
	 * Create a Pixbuffer from image data.
	 */
	Pixbuffer (ImageData src) {
		if (src == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

		createHandle(src.width, src.height);

		// populate the pixbuf with data
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
		int dataSize = src.height*stride;
		byte[] bytes = new byte[dataSize];
		switch (src.getTransparencyType()) {
			case SWT.TRANSPARENCY_ALPHA: _blit2platformAlpha(bytes, src, src.width, src.height, stride); break;
			case SWT.TRANSPARENCY_MASK:  _blit2platformMask (bytes, src, src.width, src.height, stride); break;
			case SWT.TRANSPARENCY_PIXEL: _blit2platformPixel(bytes, src, src.width, src.height, stride); break;
			case SWT.TRANSPARENCY_NONE:  _blit2platformNone (bytes, src, src.width, src.height, stride); break;
		}
		OS.memmove(data, bytes, dataSize);
	}
	
	/**
	 * Clone an existing Pixbuffer (possibly making it look
	 * grayed out or disabled)
	 */
	Pixbuffer (Pixbuffer src, int flag) {
		if (src == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (src.handle==0) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.hasAlpha = src.hasAlpha;
		this.constantAlpha = src.constantAlpha;
		this.hasMask = src.hasMask;

		/* First, get a copy all our own */
		handle = GDKPIXBUF.gdk_pixbuf_copy(src.handle);
		data   = GDKPIXBUF.gdk_pixbuf_get_pixels(this.handle);
	
		// simplest case - just clone what we have
		if (flag==SWT.IMAGE_COPY) return;

		// gather some information we will need for disabling or graying
		int width  = getWidth();
		int height = getHeight();
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(this.handle);
		int dataSize = height * stride;
		byte[] bytes = new byte[dataSize];
		OS.memmove(bytes,data,dataSize);
		int lineAddress = 0;
		int pixelAddress;
	
	if (flag==SWT.IMAGE_DISABLE) {
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				pixelAddress = lineAddress+x*4;
				
				int intensity = 
						bytes[pixelAddress+0] * bytes[pixelAddress+0] +
						bytes[pixelAddress+1] * bytes[pixelAddress+1] +
						bytes[pixelAddress+2] * bytes[pixelAddress+2];
				byte value = (intensity < 9000)?
					(byte)0 : (byte) 255;
				bytes[pixelAddress+0] = bytes[pixelAddress+1] = bytes[pixelAddress+2] = value;
				// no change to alpha
			}
			lineAddress += stride;
		}
		/* move it back */
		OS.memmove(data, bytes, dataSize);
		return;
	}

	if (flag==SWT.IMAGE_GRAY) {
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				pixelAddress = lineAddress+x*4;
				int intensity = 
						(bytes[pixelAddress+0] +
						 bytes[pixelAddress+1] +
						 bytes[pixelAddress+2] ) / 3;
				bytes[pixelAddress+0] = (byte)intensity;
				bytes[pixelAddress+1] = (byte)intensity;
				bytes[pixelAddress+2] = (byte)intensity;
				// no change to alpha
			}
			lineAddress += stride;
		}
		/* move it back */
		OS.memmove(data, bytes, dataSize);
		return;
	}

		// flag is neither COPY nor DISABLE nor GRAY
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}


	/**
	 * Push the pixbuf to the X Server
	 */
	void toImage (Image dest) {
		if (dest==null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		int w = getWidth();
		int h = getHeight();
		GdkVisual visual = new GdkVisual();
		OS.memmove(visual, OS.gdk_visual_get_system(), GdkVisual.sizeof);
		dest.pixmap = OS.gdk_pixmap_new (0, w, h, visual.depth);
		dest.mask = 0;  // for now; we fill it later in this method
		GDKPIXBUF.gdk_pixbuf_render_to_drawable_alpha(handle,  // src
			dest.pixmap,   // dest drawable
			0,0, 0,0,
			w, h,
			GDKPIXBUF.GDK_PIXBUF_ALPHA_BILEVEL, 128,
			GDKPIXBUF.GDK_RGB_DITHER_NORMAL, 0,0);

		// now the mask, if any
		if (hasMask) {
			// bring the pixel data into Java memory
			int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
			byte[] srcBytes = new byte[h*stride];
			OS.memmove(srcBytes, data, h*stride);

			// the mask lines are padded to 4 bytes, that is 32 pixels
			int maskwpl = w/32; if (w%32!=0) maskwpl+=1;
			int maskBpl = maskwpl*4;  // Bytes per line for the mask
			byte[] bytes = new byte[h * maskBpl];
			for (int y=0; y<h; y++) {
				int lineAddress = y * maskBpl;
				for (int x=0; x<w; x++)
					if (srcBytes[y*stride + x*4 + 3] != 0) {
						int byteAddress = lineAddress + x/8;
						int bit = 1<<(x%8);
						bytes[byteAddress] |= (byte)bit;
					}  // if
			}  // for y
			dest.mask = OS.gdk_bitmap_create_from_data(0, bytes, maskBpl*8, h);
		} // hasMask
			
		else if (hasAlpha) {
			SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
		}
		
		else if (constantAlpha!=-1) {
			SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
		}
	}
	
	/**
	 * Return the ImageData for the receiver.
	 */
	ImageData getImageData() {
		int width  = getWidth();
		int height = getHeight();
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
		byte[] bytes = _getDataBytes();
		ImageData answer = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));

		if (hasMask) {
			answer.maskData = new byte[((width+7)/8)*height];
			answer.maskPad = 1;
		}
		
		for (int y=0; y<height; y++)
			for (int x=0; x<width; x++) {
				int address = y*stride + x*4;
				byte r = bytes[address+0];
				byte g = bytes[address+1];
				byte b = bytes[address+2];
				answer.setPixel(x,y, r<<16+g<<8+b);
				byte alpha = bytes[address+3];
				if (hasAlpha) {
					answer.setAlpha(x,y, alpha);
				}
				if (hasMask  && (alpha!=0)) {
					// Find out where to stab the bit
					int mask_bytes_per_line = (width+7) / 8;
					int x_inside_line = x / 8;
					int shift_inside_byte = x - (x_inside_line*8);
					answer.maskData[x_inside_line + (y*mask_bytes_per_line)] |= (128 >> shift_inside_byte);
				}
			}
		if (constantAlpha!=-1) answer.alpha = constantAlpha;
		return answer;
	}
	
	int getWidth() {
		return GDKPIXBUF.gdk_pixbuf_get_width (handle);
	}

	int getHeight() {
		return GDKPIXBUF.gdk_pixbuf_get_height (handle);
	}


	/**
	 * Actually create the OS resource.
	 * No matter what, the GdkPixbuf we create always has
	 * an alpha channel.
	 */
	private void createHandle(int width, int height) {
		handle = GDKPIXBUF.gdk_pixbuf_new(GDKPIXBUF.GDK_COLORSPACE_RGB,
		    true,
		    8,
	 	   width, height);
		if (this.handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		data = GDKPIXBUF.gdk_pixbuf_get_pixels(handle);
	}

	private void fillAlphaFromPixmapMask(int mask) {
		hasMask = true;
		
		/* pull the mask data from the X Server */	
		// get the geometry
		int[] unused = new int[1];
		int[] w = new int[1];
		int[] h = new int[1];
		int[] d = new int[1];
		OS.gdk_window_get_geometry(mask, unused, unused, w, h, unused);
	 	int width =  Math.min(w[0], getWidth());
	 	int height = Math.min(h[0], getHeight());
		/* Get the data */
		int xMaskPtr = OS.gdk_image_get(mask, 0, 0, width, height);
		if (xMaskPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);

		/* stuff the alpha values */
		byte[] bytes = _getDataBytes();
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
		for (int y=0; y<height; y++)
			for (int x=0; x<width; x++) {
				int pixel_value = OS.gdk_image_get_pixel(xMaskPtr, x, y);
				if (pixel_value==0) bytes[y*stride + x*4 +3] = 0;
				else if (pixel_value==1) bytes[y*stride + x*4 +3] = (byte)255;
				else { System.out.println("GDK insanity: bitmap contains bits other than 0 or 1"); SWT.error(SWT.ERROR_UNSPECIFIED); }
			}
		/* move it back */
		OS.memmove(data, bytes, bytes.length);	
	}

	private void fillAlphaFromTransparentPixel(int pm, int pixel) {
		transparentPixel = pixel;
	
		/* pull the data from the X Server */	
		// get the geometry
		int[] unused = new int[1];
		int[] w = new int[1];
		int[] h = new int[1];
		int[] d = new int[1];
		OS.gdk_window_get_geometry(pm, unused, unused, w, h, unused);
	 	int width =  Math.min(w[0], getWidth());
	 	int height = Math.min(h[0], getHeight());
		/* Get the data */
		int xImage = OS.gdk_image_get(pm, 0, 0, width, height);
		if (xImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);

		/* stuff the alpha values */
		byte[] bytes = _getDataBytes();
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
		for (int y=0; y<height; y++)
			for (int x=0; x<width; x++) {
				int pixel_value = OS.gdk_image_get_pixel(xImage, x, y);
				if (pixel_value==pixel) bytes[y*stride + x*4 +3] = 0;
				else bytes[y*stride + x*4 +3] = (byte)255;
			}
		/* move it back */
		OS.memmove(data, bytes, bytes.length);	
	}
	
	private void fillAlphaFromAlphaBytes(byte[] alpha) {
		hasAlpha = true;
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	}
	
	private void fillConstantAlpha(int alpha) {
		if ((alpha<0)||(alpha>255)) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		constantAlpha = alpha;
		_fillConstantAlpha((byte)alpha);
	}
	
	private void fillOpaque() {
		_fillConstantAlpha((byte)255);
	}
	
	/**
	 * Assume the handle represents a valid GdkPixbuf,
	 * and data is pointing to the correct location in memory.
	 * Fill all alpha bytes with the specified value.
	 */
	private void _fillConstantAlpha (byte value) {
		// first, get some technical info
		int width  = getWidth();
		int height = getHeight();
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(this.handle);
		int dataSize = height * stride;
		byte[] bytes = new byte[dataSize];
		OS.memmove(bytes,data,dataSize);
		int lineAddress = 0;
		int pixelAddress;
	
		// set all alpha bytes to 255
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				pixelAddress = lineAddress+x*4;
				bytes[pixelAddress+3] = value;
			}
			lineAddress += stride;
		}
		/* move it back */
		OS.memmove(data, bytes, dataSize);
	}
	
	private void _blit2platformNone (byte[] bytes,
			ImageData source,
			int width,
			int height,
			int stride)
	{
		int lineAddress = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				RGB rgb = source.palette.getRGB(source.getPixel(x,y));
				bytes[lineAddress + x*4 + 0] = (byte)rgb.red;
				bytes[lineAddress + x*4 + 1] = (byte)rgb.green;
				bytes[lineAddress + x*4 + 2] = (byte)rgb.blue;
				bytes[lineAddress + x*4 + 3] = (byte)255;
			}
			lineAddress += stride;
		}
	}
	
	private void _blit2platformAlpha (byte[] bytes,
			ImageData source,
			int width,
			int height,
			int stride)
	{
		hasAlpha = true;
		int lineAddress = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				RGB rgb = source.palette.getRGB(source.getPixel(x,y));
				bytes[lineAddress + x*4 + 0] = (byte)rgb.red;
				bytes[lineAddress + x*4 + 1] = (byte)rgb.green;
				bytes[lineAddress + x*4 + 2] = (byte)rgb.blue;
				bytes[lineAddress + x*4 + 3] = (byte)source.getAlpha(x,y);
			}
			lineAddress += stride;
		}
	}
	
	private void _blit2platformPixel (byte[] bytes,
			ImageData source,
			int width,
			int height,
			int stride
			)
	{
		hasMask = true;
		int lineAddress = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int pixel = source.getPixel(x,y);
				if (pixel==source.transparentPixel) {
					bytes[lineAddress + x*4 + 0] = (byte)0;
					bytes[lineAddress + x*4 + 1] = (byte)0;
					bytes[lineAddress + x*4 + 2] = (byte)0;
					bytes[lineAddress + x*4 + 3] = (byte)0;
				} else {
					RGB rgb = source.palette.getRGB(pixel);
					bytes[lineAddress + x*4 + 0] = (byte)rgb.red;
					bytes[lineAddress + x*4 + 1] = (byte)rgb.green;
					bytes[lineAddress + x*4 + 2] = (byte)rgb.blue;
					bytes[lineAddress + x*4 + 3] = (byte)255;
				}
			}
			lineAddress += stride;
		}
	}
	
	private void _blit2platformMask (byte[] bytes,
		ImageData source,
		int width,
		int height,
		int stride
		)
	{
		hasMask = true;
		ImageData maskData = source.getTransparencyMask();
		int lineAddress = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				RGB rgb = source.palette.getRGB(source.getPixel(x,y));
				bytes[lineAddress + x*4 + 0] = (byte)rgb.red;
				bytes[lineAddress + x*4 + 1] = (byte)rgb.green;
				bytes[lineAddress + x*4 + 2] = (byte)rgb.blue;
				int alpha = 0;
				if (maskData.getPixel(x,y) !=0) alpha=255;
				bytes[lineAddress + x*4 + 3] = (byte)alpha;
			}
			lineAddress += stride;
		}
	}

	/**
	 * Transfer the pixel data from OS memory to Java memory
	 * and return the Java byte array
	 */
	private byte[] _getDataBytes() {
		int height = GDKPIXBUF.gdk_pixbuf_get_height (handle);
		int stride = GDKPIXBUF.gdk_pixbuf_get_rowstride(handle);
		int size = height*stride;
		byte[] bytes = new byte[size];
		OS.memmove(bytes, data, size);
		return bytes;
	}
}

