/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.custom;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated when text
 * is modified.
 *
 * @see ExtendedModifyEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
@FunctionalInterface
public interface ExtendedModifyListener extends SWTEventListener {

/**
 * This method is called after a text change occurs.
 * <p>
 * The following event fields are used:<ul>
 * <li>event.start the start offset of the new text (input)</li>
 * <li>event.length the length of the new text (input)</li>
 * <li>event.replacedText the replaced text (input)</li>
 * </ul>
 *
 * @param event the given event
 * @see ExtendedModifyEvent
 */
public void modifyText(ExtendedModifyEvent event);
}


