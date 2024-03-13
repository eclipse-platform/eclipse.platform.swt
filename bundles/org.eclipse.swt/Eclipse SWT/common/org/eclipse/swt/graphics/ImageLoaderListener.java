/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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


import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the incremental loading of image data.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to an image loader using the
 * <code>addImageLoaderListener</code> method and removed using
 * the <code>removeImageLoaderListener</code> method. When
 * image data is either partially or completely loaded, this
 * method will be invoked.
 * </p>
 *
 * @see ImageLoader
 * @see ImageLoaderEvent
 */
@FunctionalInterface
public interface ImageLoaderListener extends SWTEventListener {

/**
 * Sent when image data is either partially or completely loaded.
 * <p>
 * The timing of when this method is called varies depending on
 * the format of the image being loaded.
 * </p>
 *
 * @param e an event containing information about the image loading operation
 */
void imageDataLoaded(ImageLoaderEvent e);

}
