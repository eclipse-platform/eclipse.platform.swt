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
 * a {@link LocationEvent} notification when a {@link Browser}
 * navigates to a different URL.
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see Browser#addLocationListener(LocationListener)
 * @see Browser#removeLocationListener(LocationListener)
 * 
 * @since 3.0
 */
public interface LocationListener extends SWTEventListener {

/**
 * This method is called when the current location is about to be changed.
 * <p>
 *
 * <p>The following fields in the <code>LocationEvent</code> apply:
 * <ul>
 * <li>(in) location the location to be loaded
 * <li>(in) cancel can be set to <code>true</code> to prevent the location
 * from being loaded 
 * <li>(in) widget the <code>Browser</code> whose location is changing
 * </ul>
 * 
 * @param event the <code>LocationEvent</code> that specifies  the location
 * to be loaded by a <code>Browser</code>
 * 
 * @since 3.0
 */ 
public void changing(LocationEvent event);

/**
 * This method is called when the current location is changed.
 * <p>
 *
 * <p>The following fields in the <code>LocationEvent</code> apply:
 * <ul>
 * <li>(in) location the current location
 * <li>(in) widget the <code>Browser</code> whose location has changed
 * </ul>
 * 
 * @param event the <code>LocationEvent</code> that specifies  the new
 * location of a <code>Browser</code>
 * 
 * @since 3.0
 */ 
public void changed(LocationEvent event);

}
