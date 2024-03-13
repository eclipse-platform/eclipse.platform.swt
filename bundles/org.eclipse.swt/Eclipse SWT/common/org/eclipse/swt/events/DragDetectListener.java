/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502576
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods that deal with the
 * events that are generated when a drag gesture is detected.
 * <p>
 * After creating an instance of a class that implements this interface it can
 * be added to a control using the <code>addDragDetectListener</code> method and
 * removed using the <code>removeDragDetectListener</code> method. When the drag
 * is detected, the {@link #dragDetected(DragDetectEvent) dragDetected} method
 * will be invoked.
 * </p>
 *
 * @see DragDetectEvent
 *
 * @since 3.3
 */
@FunctionalInterface
public interface DragDetectListener extends SWTEventListener {

/**
 * Sent when a drag gesture is detected.
 *
 * @param e an event containing information about the drag
 */
void dragDetected(DragDetectEvent e);
}
