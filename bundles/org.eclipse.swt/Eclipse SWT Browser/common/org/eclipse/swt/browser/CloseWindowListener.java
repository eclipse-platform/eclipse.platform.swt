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
 * a {@link WindowEvent} notification when a {@link Browser} is 
 * about to be closed and when its host window should be closed
 * by the application.
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see Browser#addCloseWindowListener(CloseWindowListener)
 * @see Browser#removeCloseWindowListener(CloseWindowListener)
 * @see OpenWindowListener
 * @see VisibilityWindowListener
 * 
 * @since 3.0
 */
public interface CloseWindowListener extends SWTEventListener {

/**
 * This method is called when the window hosting a {@link Browser} should be closed.
 * Application would typically close the {@link org.eclipse.swt.widgets.Shell} that
 * hosts the <code>Browser</code>. The <code>Browser</code> is disposed after this
 * notification.
 *
 * <p>The following fields in the <code>WindowEvent</code> apply:
 * <ul>
 * <li>(in) widget the <code>Browser</code> that is going to be disposed
 * </ul></p>
 *
 * @param event the <code>WindowEvent</code> that specifies the <code>Browser</code>
 * that is going to be disposed
 * 
 * @see org.eclipse.swt.widgets.Shell#close()
 * 
 * @since 3.0
 */ 
public void close(WindowEvent event);
}
