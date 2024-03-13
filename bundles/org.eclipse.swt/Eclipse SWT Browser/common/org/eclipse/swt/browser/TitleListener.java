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
 * a {@link TitleEvent} notification when the title of the document
 * displayed in a {@link Browser} is known or has been changed.
 *
 * @see Browser#addTitleListener(TitleListener)
 * @see Browser#removeTitleListener(TitleListener)
 *
 * @since 3.0
 */
@FunctionalInterface
public interface TitleListener extends SWTEventListener {

/**
 * This method is called when the title of the current document
 * is available or has changed.
 *
 * <p>The following fields in the <code>TitleEvent</code> apply:</p>
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
