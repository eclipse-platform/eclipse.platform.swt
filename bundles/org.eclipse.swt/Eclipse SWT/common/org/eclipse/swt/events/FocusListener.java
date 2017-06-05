/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 506538
 *******************************************************************************/
package org.eclipse.swt.events;


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as controls
 * gain and lose focus.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addFocusListener</code> method and removed using
 * the <code>removeFocusListener</code> method. When a
 * control gains or loses focus, the appropriate method
 * will be invoked.
 * </p>
 *
 * @see FocusAdapter
 * @see FocusEvent
 */
public interface FocusListener extends SWTEventListener {

/**
 * Sent when a control gets focus.
 *
 * @param e an event containing information about the focus change
 */
public void focusGained(FocusEvent e);

/**
 * Sent when a control loses focus.
 *
 * @param e an event containing information about the focus change
 */
public void focusLost(FocusEvent e);


/**
 * Static helper method to create a <code>FocusListener</code> for the
 * {@link #focusGained(FocusEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return FocusListener
 * @since 3.106
 */
public static FocusListener focusGainedAdapter(Consumer<FocusEvent> c) {
	return new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>FocusListener</code> for the
 * {@link #focusLost(FocusEvent e)}) method with a lambda expression.
 *
 * @param c the consumer of the event
 * @return FocusListener
 * @since 3.106
*/
public static FocusListener focusLostAdapter(Consumer<FocusEvent> c) {
	return new FocusAdapter() {
		@Override
		public void focusLost(FocusEvent e) {
			c.accept(e);
		}
	};
}
}
