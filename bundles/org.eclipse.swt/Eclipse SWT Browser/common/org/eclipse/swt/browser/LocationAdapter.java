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
 * methods described by the <code>LocationListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>LocationEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p><p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see LocationListener
 * @see LocationEvent
 */
public abstract class LocationAdapter implements LocationListener {

/**
 * This method is called when the current location is about to be changed.
 * <p>
 *
 * @param event.location the location to be loaded 
 * @param event.cancel can be set to true to prevent the location from being loaded 
 *
 * @see LocationEvent
 * 
 * @since 3.0
 */ 
public void changing(LocationEvent event) {
}

/**
 * This method is called when the current location is changed.
 * <p>
 *
 * @param event.location the current location 
 *
 * @see LocationEvent
 * 
 * @since 3.0
 */ 
public void changed(LocationEvent event) {
}
}