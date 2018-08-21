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
 * methods described by the <code>ShellListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>ShellEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods in
 * {@link ShellListener},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see ShellListener
 * @see ShellEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class ShellAdapter implements ShellListener {

/**
 * Sent when a shell becomes the active window.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the activation
 */
@Override
public void shellActivated(ShellEvent e) {
}

/**
 * Sent when a shell is closed.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the close
 */
@Override
public void shellClosed(ShellEvent e) {
}

/**
 * Sent when a shell stops being the active window.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the deactivation
 */
@Override
public void shellDeactivated(ShellEvent e) {
}

/**
 * Sent when a shell is un-minimized.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the un-minimization
 */
@Override
public void shellDeiconified(ShellEvent e) {
}

/**
 * Sent when a shell is minimized.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the minimization
 */
@Override
public void shellIconified(ShellEvent e) {
}
}
