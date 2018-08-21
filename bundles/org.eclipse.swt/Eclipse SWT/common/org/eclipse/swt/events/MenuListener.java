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


import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide methods
 * that deal with the hiding and showing of menus.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a menu using the
 * <code>addMenuListener</code> method and removed using
 * the <code>removeMenuListener</code> method. When the
 * menu is hidden or shown, the appropriate method will
 * be invoked.
 * </p>
 *
 * @see MenuAdapter
 * @see MenuEvent
 */
public interface MenuListener extends SWTEventListener {

/**
 * Sent when a menu is hidden.
 *
 * @param e an event containing information about the menu operation
 */
void menuHidden(MenuEvent e);

/**
 * Sent when a menu is shown.
 *
 * @param e an event containing information about the menu operation
 */
void menuShown(MenuEvent e);

/**
 * Static helper method to create a <code>MenuListener</code> for the
 * {@link #menuHidden(MenuEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return MenuListener
 * @since 3.107
 */
static MenuListener menuHiddenAdapter(Consumer<MenuEvent> c) {
	return new MenuAdapter() {
		@Override
		public void menuHidden(MenuEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>MenuListener</code> for the
 * {@link #menuShown(MenuEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return MenuListener
 * @since 3.107
 */
static MenuListener menuShownAdapter(Consumer<MenuEvent> c) {
	return new MenuAdapter() {
		@Override
		public void menuShown(MenuEvent e) {
			c.accept(e);
		}
	};
}
}
