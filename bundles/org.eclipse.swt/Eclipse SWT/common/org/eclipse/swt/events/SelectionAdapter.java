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
 * methods described by the <code>SelectionListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>SelectionEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods
 * {@link SelectionListener#widgetSelectedAdapter(java.util.function.Consumer)}
 * and
 * {@link SelectionListener#widgetDefaultSelectedAdapter(java.util.function.Consumer)},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see SelectionListener
 * @see SelectionEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class SelectionAdapter implements SelectionListener {

/**
 * Sent when selection occurs in the control.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the selection
 */
@Override
public void widgetSelected(SelectionEvent e) {
}

/**
 * Sent when default selection occurs in the control.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the default selection
 */
@Override
public void widgetDefaultSelected(SelectionEvent e) {
}
}
