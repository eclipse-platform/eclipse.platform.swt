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
 *     Lars Vogel <Lars.Vogel@vogella.com>  - Bug 513037
 *******************************************************************************/
package org.eclipse.swt.events;


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as mouse buttons
 * are pressed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseListener</code> method and removed using
 * the <code>removeMouseListener</code> method. When a
 * mouse button is pressed or released, the appropriate method
 * will be invoked.
 * </p>
 *
 * @see MouseAdapter
 * @see MouseEvent
 */
public interface MouseListener extends SWTEventListener {

/**
 * Sent when a mouse button is pressed twice within the
 * (operating system specified) double click period.
 *
 * @param e an event containing information about the mouse double click
 *
 * @see org.eclipse.swt.widgets.Display#getDoubleClickTime()
 */
void mouseDoubleClick(MouseEvent e);

/**
 * Sent when a mouse button is pressed.
 *
 * @param e an event containing information about the mouse button press
 */
void mouseDown(MouseEvent e);

/**
 * Sent when a mouse button is released.
 *
 * @param e an event containing information about the mouse button release
 */
void mouseUp(MouseEvent e);


/**
 * Static helper method to create a <code>MouseListener</code> for the
 * {@link #mouseDoubleClick(MouseEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return MouseListener
 * @since 3.106
 */

static MouseListener mouseDoubleClickAdapter(Consumer<MouseEvent> c) {
	return new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>MouseListener</code> for the
 * {@link #mouseDown(MouseEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return MouseListener
 * @since 3.106
 */

static MouseListener mouseDownAdapter(Consumer<MouseEvent> c) {
	return new MouseAdapter() {
		@Override
		public void mouseDown(MouseEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>MouseListener</code> for the
 * {@link #mouseUp(MouseEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return MouseListener
 * @since 3.106
 */

static MouseListener mouseUpAdapter(Consumer<MouseEvent> c) {
	return new MouseAdapter() {
		@Override
		public void mouseUp(MouseEvent e) {
			c.accept(e);
		}
	};
}

}
