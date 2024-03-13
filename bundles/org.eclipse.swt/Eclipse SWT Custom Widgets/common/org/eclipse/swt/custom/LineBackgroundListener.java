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
 * that can provide the background color for a line that
 * is to be drawn.
 *
 * @see LineBackgroundEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
@FunctionalInterface
public interface LineBackgroundListener extends SWTEventListener {

/**
 * This method is called when a line is about to be drawn in order to get its
 * background color.
 * <p>
 * The following event fields are used:<ul>
 * <li>event.lineOffset line start offset (input)</li>
 * <li>event.lineText line text (input)</li>
 * <li>event.lineBackground line background color (output)</li>
 * </ul>
 *
 * @param event the given event
 * @see LineBackgroundEvent
 */
public void lineGetBackground(LineBackgroundEvent event);
}
