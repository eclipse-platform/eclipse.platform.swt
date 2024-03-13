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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502576
 *******************************************************************************/
package org.eclipse.swt.events;


import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with the events that are generated when a
 * traverse event occurs in a control.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addTraverseListener</code> method and removed using
 * the <code>removeTraverseListener</code> method. When a
 * traverse event occurs in a control, the keyTraversed method
 * will be invoked.
 * </p>
 *
 * @see TraverseEvent
 */
@FunctionalInterface
public interface TraverseListener extends SWTEventListener {

/**
 * Sent when a traverse event occurs in a control.
 * <p>
 * A traverse event occurs when the user presses a traversal
 * key. Traversal keys are typically tab and arrow keys, along
 * with certain other keys on some platforms. Traversal key
 * constants beginning with <code>TRAVERSE_</code> are defined
 * in the <code>SWT</code> class.
 * </p>
 *
 * @param e an event containing information about the traverse
 */
void keyTraversed(TraverseEvent e);
}
