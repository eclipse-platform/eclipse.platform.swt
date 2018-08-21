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
package org.eclipse.swt.events;


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>MouseListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>MouseEvent</code>s
 * which occur as mouse buttons are pressed and released can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see MouseListener
 * @see MouseEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class MouseAdapter implements MouseListener {

/**
 * Sent when a mouse button is pressed twice within the
 * (operating system specified) double click period.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the mouse double click
 *
 * @see org.eclipse.swt.widgets.Display#getDoubleClickTime()
 */
@Override
public void mouseDoubleClick(MouseEvent e) {
}

/**
 * Sent when a mouse button is pressed.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the mouse button press
 */
@Override
public void mouseDown(MouseEvent e) {
}

/**
 * Sent when a mouse button is released.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the mouse button release
 */
@Override
public void mouseUp(MouseEvent e) {
}
}
