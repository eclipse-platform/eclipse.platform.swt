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
 *******************************************************************************/
package org.eclipse.swt.events;


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>FocusListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>FocusEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see FocusListener
 * @see FocusEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class FocusAdapter implements FocusListener {

/**
 * Sent when a control gets focus.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the focus change
 */
@Override
public void focusGained(FocusEvent e) {
}

/**
 * Sent when a control loses focus.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the focus change
 */
@Override
public void focusLost(FocusEvent e) {
}
}
