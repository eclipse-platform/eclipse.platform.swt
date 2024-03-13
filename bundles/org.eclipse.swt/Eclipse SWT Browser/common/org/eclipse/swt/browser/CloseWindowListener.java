/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and others.
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
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.*;

/**
 * This listener interface may be implemented in order to receive
 * a {@link WindowEvent} notification when a {@link Browser} is
 * about to be closed and when its host window should be closed
 * by the application.
 *
 * @see Browser#addCloseWindowListener(CloseWindowListener)
 * @see Browser#removeCloseWindowListener(CloseWindowListener)
 * @see OpenWindowListener
 * @see VisibilityWindowListener
 *
 * @since 3.0
 */
@FunctionalInterface
public interface CloseWindowListener extends SWTEventListener {

/**
 * This method is called when the window hosting a {@link Browser} should be closed.
 * Application would typically close the {@link org.eclipse.swt.widgets.Shell} that
 * hosts the <code>Browser</code>. The <code>Browser</code> is disposed after this
 * notification.
 *
 * <p>The following fields in the <code>WindowEvent</code> apply:</p>
 * <ul>
 * <li>(in) widget the <code>Browser</code> that is going to be disposed
 * </ul>
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
