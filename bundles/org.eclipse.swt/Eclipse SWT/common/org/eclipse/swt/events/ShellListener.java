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
 * that deal with changes in state of <code>Shell</code>s.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a shell using the
 * <code>addShellListener</code> method and removed using
 * the <code>removeShellListener</code> method. When the
 * state of the shell changes, the appropriate method will
 * be invoked.
 * </p>
 *
 * @see ShellAdapter
 * @see ShellEvent
 */
public interface ShellListener extends SWTEventListener {

/**
 * Sent when a shell becomes the active window.
 *
 * @param e an event containing information about the activation
 */
void shellActivated(ShellEvent e);

/**
 * Sent when a shell is closed.
 *
 * @param e an event containing information about the close
 */
void shellClosed(ShellEvent e);

/**
 * Sent when a shell stops being the active window.
 *
 * @param e an event containing information about the deactivation
 */
void shellDeactivated(ShellEvent e);

/**
 * Sent when a shell is un-minimized.
 *
 * @param e an event containing information about the un-minimization
 */
void shellDeiconified(ShellEvent e);

/**
 * Sent when a shell is minimized.
 *
 * @param e an event containing information about the minimization
 */
void shellIconified(ShellEvent e);

/**
 * Static helper method to create a <code>ShellListener</code> for the
 * {@link #shellActivated(ShellEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ShellListener
 * @since 3.107
 */
static ShellListener shellActivatedAdapter(Consumer<ShellEvent> c) {
	return new ShellAdapter() {
		@Override
		public void shellActivated(ShellEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ShellListener</code> for the
 * {@link #shellClosed(ShellEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ShellListener
 * @since 3.107
 */
static ShellListener shellClosedAdapter(Consumer<ShellEvent> c) {
	return new ShellAdapter() {
		@Override
		public void shellClosed(ShellEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ShellListener</code> for the
 * {@link #shellDeactivated(ShellEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ShellListener
 * @since 3.107
 */
static ShellListener shellDeactivatedAdapter(Consumer<ShellEvent> c) {
	return new ShellAdapter() {
		@Override
		public void shellDeactivated(ShellEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ShellListener</code> for the
 * {@link #shellDeiconified(ShellEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ShellListener
 * @since 3.107
 */
static ShellListener shellDeiconifiedAdapter(Consumer<ShellEvent> c) {
	return new ShellAdapter() {
		@Override
		public void shellDeiconified(ShellEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ShellListener</code> for the
 * {@link #shellIconified(ShellEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ShellListener
 * @since 3.107
 */
static ShellListener shellIconifiedAdapter(Consumer<ShellEvent> c) {
	return new ShellAdapter() {
		@Override
		public void shellIconified(ShellEvent e) {
			c.accept(e);
		}
	};
}
}
