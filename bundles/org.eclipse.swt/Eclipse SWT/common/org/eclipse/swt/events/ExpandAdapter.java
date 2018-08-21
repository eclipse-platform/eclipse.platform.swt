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
 * methods described by the <code>ExpandListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>ExpandEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 * <p>
 * An alternative to this class are the static helper methods
 * {@link ExpandListener#itemCollapsedAdapter(java.util.function.Consumer)}
 * and
 * {@link ExpandListener#itemExpandedAdapter(java.util.function.Consumer)},
 * which accept a lambda expression or a method reference that implements the event consumer.
 * </p>
 *
 * @see ExpandListener
 * @see ExpandEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.2
 */
public abstract class ExpandAdapter implements ExpandListener {

/**
 * Sent when an item is collapsed.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the operation
 */
@Override
public void itemCollapsed(ExpandEvent e) {
}

/**
 * Sent when an item is expanded.
 * The default behavior is to do nothing.
 *
 * @param e an event containing information about the operation
 */
@Override
public void itemExpanded(ExpandEvent e) {
}
}
