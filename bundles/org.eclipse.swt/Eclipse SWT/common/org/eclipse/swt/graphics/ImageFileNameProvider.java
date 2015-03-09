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
 * when the application is moved from low dpi monitor to high dpi. This
 * information will be used to render the higher resolution images
 *
 * This interface needs to be implemented by client code and provide the image
 * information on demand. This interface provides a callback mechanism to the
 * client code to provide file name based on the OS as per the zoom
 * level. Client can have their own naming convention for various zoom levels
 *
 * @since 3.104
 */
public interface ImageFileNameProvider {

	/**
	 * Method to retrieve absolute path to filename for a particular zoom level.
	 * This needs to be implemented on the client side
	 *
	 * This method should return a filename available on the filesystem. If not
	 * available please fallback to image of 100% level and send its absolute
	 * path as return value.
	 *
	 * Note: SWT will throw exception if this method returns null.
	 *
	 * @param zoom
	 *            value will be 100(16x16 image), 150(24x24 image) & 200(32x32 image)
	 * @return the absolute file path of the image
	 * @since 3.104
	 */
	public String getImagePath (int zoom);

}
