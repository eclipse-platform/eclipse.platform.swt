package org.eclipse.swt.events;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.EventListenerCompatibility;

/**
 * Classes which implement this interface provide methods
 * that deal with the expanding and collapsing of tree
 * branches.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addTreeListener</code> method and removed using
 * the <code>removeTreeListener</code> method. When a branch
 * of a tree is expanded or collapsed, the appropriate method
 * will be invoked.
 * </p>
 *
 * @see TreeAdapter
 * @see TreeEvent
 */
public interface TreeListener extends EventListenerCompatibility {

/**
 * Sent when a tree branch is collapsed.
 *
 * @param e an event containing information about the tree operation
 */
public void treeCollapsed(TreeEvent e);

/**
 * Sent when a tree branch is expanded.
 *
 * @param e an event containing information about the tree operation
 */
public void treeExpanded(TreeEvent e);
}
