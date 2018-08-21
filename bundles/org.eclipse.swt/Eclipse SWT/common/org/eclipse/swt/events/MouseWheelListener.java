/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.events;


import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated as the mouse
 * wheel is scrolled.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseWheelListener</code> method and removed using
 * the <code>removeMouseWheelListener</code> method. When the
 * mouse wheel is scrolled the <code>mouseScrolled</code> method
 * will be invoked.
 * </p>
 *
 * @see MouseEvent
 *
 * @since 3.3
 */
@FunctionalInterface
public interface MouseWheelListener extends SWTEventListener {

/**
 * Sent when the mouse wheel is scrolled.
 *
 * @param e an event containing information about the mouse wheel action
 */
void mouseScrolled (MouseEvent e);
}
