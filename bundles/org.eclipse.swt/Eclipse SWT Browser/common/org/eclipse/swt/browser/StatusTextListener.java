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
 * a {@link StatusTextEvent} notification when the status text
 * for a {@link Browser} needs to be updated.
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see Browser#addStatusTextListener(StatusTextListener)
 * @see Browser#removeStatusTextListener(StatusTextListener)
 * 
 * @since 3.0
 */
public interface StatusTextListener extends SWTEventListener {

/**
 * This method is called when the status text is changed. The
 * status text is typically showed in the status bar of a browser 
 * application. 
 * <p>
 *
 * <p>The following fields in the <code>StatusTextEvent</code> apply:
 * <ul>
 * <li>(in) text the modified status text
 * <li>(in) widget the <code>Browser</code> whose status text is changed
 * </ul>
 * 
 * @since 3.0
 */
public void changed(StatusTextEvent event);
}