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
 * methods described by the <code>VisibilityWindowListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>VisibilityWindowEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p><p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see VisibilityWindowListener
 * @see VisibilityWindowEvent
 */
public abstract class VisibilityWindowAdapter implements VisibilityWindowListener {

/**
 * This method is called when the Browser is requested to be hidden.
 * <p>
 *
 * @param event.widget the browser to hide
 *
 * @see VisibilityWindowEvent
 * 
 * @since 3.0
 */ 
public void hide(VisibilityWindowEvent event) {
}

/**
 * This method is called when the Browser is requested to be displayed.
 * <p>
 *
 * @param event.widget the browser to display
 * @param event.location if not null, the requested location for the Shell hosting the browser
 * @param event.size if not null, the requested size for the Shell hosting the browser
 *
 * @see VisibilityWindowEvent
 * 
 * @since 3.0
 */ 
public void show(VisibilityWindowEvent event) {
}
}