/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.launcher;


/**
 * Internal class used to store tree structures of ItemDescriptors
 */
class ItemTreeNode {
	private ItemTreeNode nextSibling;
	private ItemTreeNode firstChild;
	private ItemDescriptor descriptor;

	/**
	 * Constructs a leaf ItemTreeNode with a given descriptor.
	 * 
	 * @param descriptor the descriptor
	 */
	public ItemTreeNode(ItemDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Adds a node to the Tree in sorted order by name.
	 * 
	 * @param node the node to add. Note that node.nextSibling must be null
	 */
	public void addSortedNode(ItemTreeNode node) {
		if (firstChild == null) {
			firstChild = node;
		} else if (firstChild.descriptor.getName().compareTo(node.descriptor.getName()) > 0) {
			node.nextSibling = firstChild;
			firstChild = node;
		} else {
			ItemTreeNode cursor;
			for (cursor = firstChild; cursor.nextSibling != null; cursor = cursor.nextSibling) {
				ItemTreeNode sibling = cursor.nextSibling;
				if (sibling.descriptor.getName().compareTo(node.descriptor.getName()) > 0) break;
			}
			node.nextSibling = cursor.nextSibling;
			cursor.nextSibling = node;
		}
	}
	
	/**
	 * Returns the descriptor for this node.
	 * 
	 * @return the descriptor
	 */
	public ItemDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Returns the next sibling of this node.
	 * 
	 * @return the next sibling, or null if none
	 */
	public ItemTreeNode getNextSibling() {
		return nextSibling;
	}

	/**
	 * Returns the first child of this node.
	 * 
	 * @return the first child, or null if none
	 */
	public ItemTreeNode getFirstChild() {
		return firstChild;
	}
}
