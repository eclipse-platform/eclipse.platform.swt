/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

/**
 * Interface to provide a callback mechanism to get information about images
 * when the application is moved from a low DPI monitor to a high DPI monitor.
 * This provides API which will be called by SWT during the image rendering.
 *
 * This interface needs to be implemented by client code to provide the 
 * image information on demand.
 *
 * @since 3.104
 */
public interface ImageDataProvider {

	/**
	 * Retrieves the image data for a particular zoom level. This method needs
	 * to be implemented on the client side.
	 * <p>
	 * If the image is not available for a particular zoom level, please
	 * fall back to image of 100% level and return its image data.
	 *
	 * Note: SWT will throw an exception if this method returns null.
	 *
	 * @param zoom Currently accepted values (sent by SWT) are 100, 150, and 200
	 * corresponding to 16x16, 24x24 and 32x32 images respectively.
	 * 
	 * @return an object of ImageData
	 * @since 3.104
	 */
	public ImageData getImageData (int zoom);

}
