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
 * a {@link TitleEvent} notification when the title of the document
 * displayed in a {@link Browser} is known or has been changed.
 * 
 * <p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @see Browser#addTitleListener(TitleListener)
 * @see Browser#removeTitleListener(TitleListener)
 * 
 * @since 3.0
 */
public interface TitleListener extends SWTEventListener {

/**
 * This method is called when the title of the current document
 * is available or has changed.
 * <p>
 *
 * <p>The following fields in the <code>TitleEvent</code> apply:
 * <ul>
 * <li>(in) title the title of the current document
 * <li>(in) widget the <code>Browser</code> whose current document's
 * title is known or modified
 * </ul>
 * 
 * @param event the <code>TitleEvent</code> that contains the title
 * of the document currently displayed in a <code>Browser</code>
 * 
 * @since 3.0
 */
public void changed(TitleEvent event);
}