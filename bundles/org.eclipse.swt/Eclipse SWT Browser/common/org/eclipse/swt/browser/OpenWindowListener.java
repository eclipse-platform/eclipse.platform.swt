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
 * a {@link WindowEvent} notification when a  new {@link Browser}
 * needs to be provided by the application.
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see Browser#addOpenWindowListener(OpenWindowListener)
 * @see Browser#removeOpenWindowListener(OpenWindowListener)
 * @see CloseWindowListener
 * @see VisibilityWindowListener
 * 
 * @since 3.0
 */
public interface OpenWindowListener extends SWTEventListener {

/**
 * This method is called when a new window needs to be created.
 * <p>
 *
 * <p>The following fields in the <code>WindowEvent</code> apply:
 * <ul>
 * <li>(out) browser the new <code>Browser</code> that will host the 
 * content of the new window. If it is left <code>null</code>, the navigation
 * is cancelled and no new window is opened.</p>
 * <li>(in) widget the <code>Browser</code> that is requesting to open a 
 * new window
 * </ul>
 * 
 * @param event the <code>WindowEvent</code> that needs to be passed a new
 * <code>Browser</code> to handle the new window request
 * 
 * @since 3.0
 */ 
public void open(WindowEvent event);
}
