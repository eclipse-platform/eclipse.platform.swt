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

import org.eclipse.swt.internal.SWTEventListener;

/**
 * This listener interface may be implemented in order to receive
 * ProgressEvents.
 * @see ProgressEvent
 */
public interface ProgressListener extends SWTEventListener {
	
	/**
	 * This method is called when a progress is made during the loading of the current location.
	 * <p>
	 *
	 * @param event.current the progress for the location currently being loaded
	 * @param event.total the maximum progress for the location currently being loaded
	 *
	 * @see ProgressEvent
	 */   
	public void changed(ProgressEvent event);
	
	/**
	 * This method is called when the current location has been completely loaded.
	 * <p>
	 *
	 * @see ProgressEvent
	 */
	public void completed(ProgressEvent event);
}