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
 * is about to be modified.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a text control using the
 * <code>addVerifyListener</code> method and removed using
 * the <code>removeVerifyListener</code> method. When the
 * text is about to be modified, the verifyText method
 * will be invoked.
 * </p>
 *
 * @see VerifyEvent
 */
@FunctionalInterface
public interface VerifyListener extends SWTEventListener {

/**
 * Sent when the text is about to be modified.
 * <p>
 * A verify event occurs after the user has done something
 * to modify the text (typically typed a key), but before
 * the text is modified. The doit field in the verify event
 * indicates whether or not to modify the text.
 * </p>
 *
 * @param e an event containing information about the verify
 */
void verifyText(VerifyEvent e);
}
