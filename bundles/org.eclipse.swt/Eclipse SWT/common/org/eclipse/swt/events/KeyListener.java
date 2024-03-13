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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 507185
 *******************************************************************************/
package org.eclipse.swt.events;


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as keys
 * are pressed on the system keyboard.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addKeyListener</code> method and removed using
 * the <code>removeKeyListener</code> method. When a
 * key is pressed or released, the appropriate method will
 * be invoked.
 * </p>
 *
 * @see KeyAdapter
 * @see KeyEvent
 */
public interface KeyListener extends SWTEventListener {

/**
 * Sent when a key is pressed on the system keyboard.
 *
 * @param e an event containing information about the key press
 */
void keyPressed(KeyEvent e);

/**
 * Sent when a key is released on the system keyboard.
 *
 * @param e an event containing information about the key release
 */
void keyReleased(KeyEvent e);

/**
 * Static helper method to create a <code>KeyListener</code> for the
 * {@link #keyPressed(KeyEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return KeyListener
 * @since 3.106
 */
static KeyListener keyPressedAdapter(Consumer<KeyEvent> c) {
	return new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>KeyListener</code> for the
 * {@link #keyReleased(KeyEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return KeyListener
 * @since 3.106
*/
static KeyListener keyReleasedAdapter(Consumer<KeyEvent> c) {
	return new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			c.accept(e);
		}
	};
}

}
