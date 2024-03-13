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
 * This listener is invoked when an object needs to be drawn.
 *
 * @since 3.2
 */
@FunctionalInterface
public interface PaintObjectListener extends SWTEventListener {
/**
 * This method is called when an object needs to be drawn.
 *
 * <p>
 * The following event fields are used:<ul>
 * <li>event.x the x location (input)</li>
 * <li>event.y the y location (input)</li>
 * <li>event.ascent the line ascent (input)</li>
 * <li>event.descent the line descent (input)</li>
 * <li>event.gc the gc (input)</li>
 * <li>event.style the style (input)</li>
 * </ul>
 *
 * @param event the event
 *
 * @see PaintObjectEvent
 * @see StyledText#addPaintObjectListener(PaintObjectListener)
 */
public void paintObject(PaintObjectEvent event);
}
