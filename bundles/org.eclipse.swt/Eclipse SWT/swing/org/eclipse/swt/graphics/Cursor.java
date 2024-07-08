/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;

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
 */

public final class Cursor extends Resource {
	
	/**
	 * the handle to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public java.awt.Cursor handle;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Cursor() {
}

/**	 
 * Constructs a new cursor given a device and a style
 * constant describing the desired cursor appearance.
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
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
 */
public Cursor(Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	switch (style) {
		case SWT.CURSOR_HAND: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR); break;
		case SWT.CURSOR_ARROW: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR); break;
		case SWT.CURSOR_WAIT: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR); break;
		case SWT.CURSOR_CROSS: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR); break;
		case SWT.CURSOR_SIZEALL: 	   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR); break;
		case SWT.CURSOR_SIZEN: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZES: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZEE: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZEW: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZENE: 	   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZESE: 	   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZESW: 	   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZENW: 	   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR); break;
		case SWT.CURSOR_IBEAM: 		   handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR); break;
    // Cursor that do not fit exactly with the description:
		case SWT.CURSOR_APPSTARTING: handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR); break;
		case SWT.CURSOR_SIZEWE:      handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZENS:      handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZENESW:    handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR); break;
		case SWT.CURSOR_SIZENWSE:    handle = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR); break;
    // Custom cursors
    case SWT.CURSOR_UPARROW:
		case SWT.CURSOR_NO:
    case SWT.CURSOR_HELP:
      handle = createCursor(style);
      break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (device.tracking) device.new_Object(this);
}

java.awt.Cursor createCursor(int type) {
  String name = "";
  switch(type) {
    case SWT.CURSOR_UPARROW: name = "UpCursor"; break;
    case SWT.CURSOR_NO: name = "NoCursor"; break;
    case SWT.CURSOR_HELP: name = "HelpCursor"; break;
    default: return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
  }
  // Get the Property file
  InputStream is = getClass().getResourceAsStream("resources/" + name + ".properties");
  if (is == null) {
    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  }
  String gifFile = null;
  String hotspot = null;
  try {
    ResourceBundle resource = new PropertyResourceBundle(is);
    gifFile = "resources/" + resource.getString("Cursor.File");
    hotspot = resource.getString("Cursor.HotSpot");
  } catch (MissingResourceException e) {
    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  } catch (IOException e2) {
    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  }
  // Create the icon
  byte[] buffer = null;
  try {
    InputStream resource = getClass().getResourceAsStream(gifFile);
    if (resource == null) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }
    BufferedInputStream in = new BufferedInputStream(resource);
    ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
    buffer = new byte[1024];
    int n;
    while ((n = in.read(buffer)) > 0) {
      out.write(buffer, 0, n);
    }
    in.close();
    out.flush();
    buffer = out.toByteArray();
    if (buffer.length == 0) {
      return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
    }
  } catch (IOException ioe) {
    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  }
  ImageIcon icon = new ImageIcon(buffer);
  // create the point
  int k = hotspot.indexOf(',');
  java.awt.Point point = new java.awt.Point(Integer.parseInt(hotspot.substring(0, k)), Integer.parseInt(hotspot.substring(k + 1)));
  try {
    return Toolkit.getDefaultToolkit().createCustomCursor(icon.getImage(), point, name);
  } catch (NoSuchMethodError err) {
    // return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  }
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
 */
public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		mask = source.getTransparencyMask();
	}
	/* Check the bounds. Mask must be the same size as source */
	if (mask.width != source.width || mask.height != source.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Convert depth to 1 */
	mask = ImageData.convertMask(mask);
	source = ImageData.convertMask(source);
	
	BufferedImage image = new BufferedImage(source.width, source.height, BufferedImage.TYPE_INT_ARGB);
  PaletteData palette = source.palette;
  if (palette.isDirect) {
	  for(int x=0; x<source.width; x++) {
	    for(int y=0; y<source.height; y++) {
	      RGB rgb = source.palette.getRGB(source.getPixel(x, y));
	      int pixel = rgb.red << 16 | rgb.green << 8 | rgb.blue;
	      rgb = mask.palette.getRGB(mask.getPixel(x, y));
	      int pixelMask = rgb.red << 16 | rgb.green << 8 | rgb.blue;
	      if(pixelMask != 0) {
	        int alpha = source.getAlpha(x, y);
	        if(alpha > 0) {
	          pixel = pixel & 0x00FFFFFF | alpha << 24;
	          image.setRGB(x, y, pixel);
	        }
	      }
	    }
	  }
	} else {
    for(int x=0; x<source.width; x++) {
      for(int y=0; y<source.height; y++) {
        int pixel = source.getPixel(x, y);
        RGB rgb = source.palette.getRGB(pixel);
        int pixelRGB = rgb.red << 16 | rgb.green << 8 | rgb.blue;
        if(pixelRGB == 0) {
          rgb = mask.palette.getRGB(mask.getPixel(x, y));
          int pixelMaskRGB = rgb.red << 16 | rgb.green << 8 | rgb.blue;
          if(pixelMaskRGB > 0) {
            image.setRGB(x, y, 0xFFFFFFFF);
          } else {
            image.setRGB(x, y, 0xFF000000);
          }
        }
      }
    }
	}
	
//	/* Make sure source and mask scanline pad is 2 */
//	byte[] sourceData = ImageData.convertPad(source.data, source.width, source.height, source.depth, source.scanlinePad, 2);
//	byte[] maskData = ImageData.convertPad(mask.data, mask.width, mask.height, mask.depth, mask.scanlinePad, 2);
  createCursor(image, hotspotX, hotspotY);
	if (device.tracking) device.new_Object(this);
}

protected void createCursor(BufferedImage image, int hotspotX, int hotspotY) {
  handle = Toolkit.getDefaultToolkit().createCustomCursor(image, new java.awt.Point(hotspotX, hotspotY), "Custom Cursor");
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
 * @since 3.0
 */
public Cursor(Device device, ImageData source, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
  createCursor(new Image(device, source).handle, hotspotX, hotspotY);
	if (device.tracking) device.new_Object(this);
}

/**
 * Disposes of the operating system resources associated with
 * the cursor. Applications must dispose of all cursors which
 * they allocate.
 */
public void dispose () {
	if (handle == null) return;
	if (device.isDisposed()) return;
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor) object;
	return device == cursor.device && handle == cursor.handle;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return handle == null? 0: handle.hashCode();
}

/**
 * Returns <code>true</code> if the cursor has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the cursor.
 * When a cursor has been disposed, it is an error to
 * invoke any other method using the cursor.
 *
 * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + handle + "}";
}

/**	 
 * Invokes platform specific functionality to allocate a new cursor.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Cursor</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param handle the handle for the cursor
 * @return a new cursor object containing the specified device and handle
 */
public static Cursor swing_new(Device device, java.awt.Cursor handle) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.handle = handle;
	cursor.device = device;
	return cursor;
}

}
