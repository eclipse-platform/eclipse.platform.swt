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
 *     Lars Vogel <Lars.Vogel@vogella.com>  - Bug 441116
 *******************************************************************************/
package org.eclipse.swt.events;


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated when selection
 * occurs in a control.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addSelectionListener</code> method and removed using
 * the <code>removeSelectionListener</code> method. When
 * selection occurs in a control the appropriate method
 * will be invoked.
 * </p>
 *
 * @see SelectionAdapter
 * @see SelectionEvent
 */
public interface SelectionListener extends SWTEventListener {

/**
 * Sent when selection occurs in the control.
 * <p>
 * For example, selection occurs in a List when the user selects
 * an item or items with the keyboard or mouse.  On some platforms,
 * the event occurs when a mouse button or key is pressed.  On others,
 * it happens when the mouse or key is released.  The exact key or
 * mouse gesture that causes this event is platform specific.
 * </p>
 *
 * @param e an event containing information about the selection
 */
void widgetSelected(SelectionEvent e);

/**
 * Sent when default selection occurs in the control.
 * <p>
 * For example, on some platforms default selection occurs in a List
 * when the user double-clicks an item or types return in a Text.
 * On some platforms, the event occurs when a mouse button or key is
 * pressed.  On others, it happens when the mouse or key is released.
 * The exact key or mouse gesture that causes this event is platform
 * specific.
 * </p>
 *
 * @param e an event containing information about the default selection
 */
void widgetDefaultSelected(SelectionEvent e);



/**
 * Static helper method to create a <code>SelectionListener</code> for the
 * {@link #widgetSelected(SelectionEvent e)}) method, given a lambda expression
 * or a method reference.
 *
 * @param c the consumer of the event
 * @return SelectionListener
 * @since 3.106
 */
static SelectionListener widgetSelectedAdapter(Consumer<SelectionEvent> c) {
	return new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>SelectionListener</code> for the
 * {@link #widgetDefaultSelected(SelectionEvent e)}) method, given a lambda expression
 * or a method reference.
 *
 * @param c the consumer of the event
 * @return SelectionListener
 * @since 3.106
*/
static SelectionListener widgetDefaultSelectedAdapter(Consumer<SelectionEvent> c) {
	return new SelectionAdapter() {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			c.accept(e);
		}
	};
}

}

