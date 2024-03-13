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
 * that deals with the events that are generated when text
 * is modified.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a text widget using the
 * <code>addModifyListener</code> method and removed using
 * the <code>removeModifyListener</code> method. When the
 * text is modified, the modifyText method will be invoked.
 * </p>
 *
 * @see ModifyEvent
 */
@FunctionalInterface
public interface ModifyListener extends SWTEventListener {

/**
 * Sent when the text is modified.
 *
 * @param e an event containing information about the modify
 */
void modifyText(ModifyEvent e);
}
