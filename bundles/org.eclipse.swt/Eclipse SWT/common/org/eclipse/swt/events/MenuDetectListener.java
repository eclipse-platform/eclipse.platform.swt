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
 * Classes which implement this interface provide methods
 * that deal with the events that are generated when the
 * platform-specific trigger for showing a context menu is
 * detected.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control or TrayItem
 * using the <code>addMenuDetectListener</code> method and
 * removed using the <code>removeMenuDetectListener</code> method.
 * When the context menu trigger occurs, the
 * <code>menuDetected</code> method will be invoked.
 * </p>
 *
 * @see MenuDetectEvent
 *
 * @since 3.3
 */
@FunctionalInterface
public interface MenuDetectListener extends SWTEventListener {

/**
 * Sent when the platform-dependent trigger for showing a menu item is detected.
 *
 * @param e an event containing information about the menu detect
 */
void menuDetected (MenuDetectEvent e);
}
