package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

import java.util.EventListener;

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

public interface ImageLoaderListener extends EventListener {

/**
 * Sent when image data is either partially or completely loaded.
 * <p>
 * The timing of when this method is called varies depending on
 * the format of the image being loaded.
 * </p>
 *
 * @param e an event containing information about the image loading operation
 */
public void imageDataLoaded(ImageLoaderEvent e);

}