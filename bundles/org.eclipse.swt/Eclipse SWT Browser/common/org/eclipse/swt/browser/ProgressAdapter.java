/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>ProgressListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>ProgressEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p><p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see ProgressListener
 * @see ProgressEvent
 */
public abstract class ProgressAdapter implements ProgressListener {

/**
 * This method is called when a progress is made during the loading of the current location.
 * <p>
 *
 * @param event.current the progress for the location currently being loaded
 * @param event.total the maximum progress for the location currently being loaded
 *
 * @see ProgressEvent
 * 
 * @since 3.0
 */   
public void changed(ProgressEvent event) {
}
	
/**
 * This method is called when the current location has been completely loaded.
 * <p>
 *
 * @see ProgressEvent
 * 
 * @since 3.0
 */
public void completed(ProgressEvent event) {
}
}
