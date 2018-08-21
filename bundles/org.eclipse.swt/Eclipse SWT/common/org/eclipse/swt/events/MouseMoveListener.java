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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502576
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with the events that are generated as the mouse
 * pointer moves.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseMoveListener</code> method and removed using
 * the <code>removeMouseMoveListener</code> method. As the
 * mouse moves, the mouseMove method will be invoked.
 * </p>
 *
 * @see MouseEvent
 */
@FunctionalInterface
public interface MouseMoveListener extends SWTEventListener {

/**
 * Sent when the mouse moves.
 *
 * @param e an event containing information about the mouse move
 */
void mouseMove(MouseEvent e);
}
