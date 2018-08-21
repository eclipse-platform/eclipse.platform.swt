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


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>MenuListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>MenuEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods
 * {@link MenuListener#menuHiddenAdapter(java.util.function.Consumer)}
 * and
 * {@link MenuListener#menuShownAdapter(java.util.function.Consumer)},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see MenuListener
 * @see MenuEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class MenuAdapter implements MenuListener {

/**
 * Sent when a menu is hidden.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the menu operation
 */
@Override
public void menuHidden(MenuEvent e) {
}

/**
 * Sent when a menu is shown.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the menu operation
 */
@Override
public void menuShown(MenuEvent e) {
}
}
