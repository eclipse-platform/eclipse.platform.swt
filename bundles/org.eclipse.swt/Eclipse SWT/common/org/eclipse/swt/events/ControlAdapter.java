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
 * methods described by the <code>ControlListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>ControlEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods
 * {@link ControlListener#controlMovedAdapter(java.util.function.Consumer)}
 * and
 * {@link ControlListener#controlResizedAdapter(java.util.function.Consumer)},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see ControlListener
 * @see ControlEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class ControlAdapter implements ControlListener {

/**
 * Sent when the location (x, y) of a control changes relative
 * to its parent (or relative to the display, for <code>Shell</code>s).
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the move
 */
@Override
public void controlMoved(ControlEvent e) {
}

/**
 * Sent when the size (width, height) of a control changes.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the resize
 */
@Override
public void controlResized(ControlEvent e) {
}
}
