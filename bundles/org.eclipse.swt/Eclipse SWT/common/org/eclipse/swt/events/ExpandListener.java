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
 * that deal with the expanding and collapsing of <code>ExpandItem</code>s.
 *
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a <code>ExpandBar</code>
 * control using the <code>addExpandListener</code> method and
 * removed using the <code>removeExpandListener</code> method.
 * When a item of the <code>ExpandBar</code> is expanded or
 * collapsed, the appropriate method will be invoked.
 * </p>
 *
 * @see ExpandAdapter
 * @see ExpandEvent
 *
 * @since 3.2
 */
public interface ExpandListener extends SWTEventListener {

/**
 * Sent when an item is collapsed.
 *
 * @param e an event containing information about the operation
 */
void itemCollapsed(ExpandEvent e);

/**
 * Sent when an item is expanded.
 *
 * @param e an event containing information about the operation
 */
void itemExpanded(ExpandEvent e);

/**
 * Static helper method to create a <code>ExpandListener</code> for the
 * {@link #itemCollapsed(ExpandEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ExpandListener
 * @since 3.107
 */
static ExpandListener itemCollapsedAdapter(Consumer<ExpandEvent> c) {
	return new ExpandAdapter() {
		@Override
		public void itemCollapsed(ExpandEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ExpandListener</code> for the
 * {@link #itemExpanded(ExpandEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return ExpandListener
 * @since 3.107
 */
static ExpandListener itemExpandedAdapter(Consumer<ExpandEvent> c) {
	return new ExpandAdapter() {
		@Override
		public void itemExpanded(ExpandEvent e) {
			c.accept(e);
		}
	};
}
}
