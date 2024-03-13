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
 * that deal with the expanding and collapsing of tree
 * branches.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a tree control using the
 * <code>addTreeListener</code> method and removed using
 * the <code>removeTreeListener</code> method. When a branch
 * of the tree is expanded or collapsed, the appropriate method
 * will be invoked.
 * </p>
 *
 * @see TreeAdapter
 * @see TreeEvent
 */
public interface TreeListener extends SWTEventListener {

/**
 * Sent when a tree branch is collapsed.
 *
 * @param e an event containing information about the tree operation
 */
void treeCollapsed(TreeEvent e);

/**
 * Sent when a tree branch is expanded.
 *
 * @param e an event containing information about the tree operation
 */
void treeExpanded(TreeEvent e);

/**
 * Static helper method to create a <code>TreeListener</code> for the
 * {@link #treeCollapsed(TreeEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return TreeListener
 * @since 3.107
 */
static TreeListener treeCollapsedAdapter(Consumer<TreeEvent> c) {
	return new TreeAdapter() {
		@Override
		public void treeCollapsed(TreeEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>TreeListener</code> for the
 * {@link #treeExpanded(TreeEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return TreeListener
 * @since 3.107
 */
static TreeListener treeExpandedAdapter(Consumer<TreeEvent> c) {
	return new TreeAdapter() {
		@Override
		public void treeExpanded(TreeEvent e) {
			c.accept(e);
		}
	};
}
}
