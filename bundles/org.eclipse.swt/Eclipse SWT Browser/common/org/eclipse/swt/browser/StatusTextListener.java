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
 * a {@link StatusTextEvent} notification when the status text for
 * a {@link Browser} is changed.
 *
 * @see Browser#addStatusTextListener(StatusTextListener)
 * @see Browser#removeStatusTextListener(StatusTextListener)
 *
 * @since 3.0
 */
@FunctionalInterface
public interface StatusTextListener extends SWTEventListener {

/**
 * This method is called when the status text is changed. The
 * status text is typically displayed in the status bar of a browser
 * application.
 *
 * <p>The following fields in the <code>StatusTextEvent</code> apply:</p>
 * <ul>
 * <li>(in) text the modified status text
 * <li>(in) widget the <code>Browser</code> whose status text is changed
 * </ul>
 *
 * @param event the <code>StatusTextEvent</code> that contains the updated
 * status description of a <code>Browser</code>
 *
 * @since 3.0
 */
public void changed(StatusTextEvent event);
}
