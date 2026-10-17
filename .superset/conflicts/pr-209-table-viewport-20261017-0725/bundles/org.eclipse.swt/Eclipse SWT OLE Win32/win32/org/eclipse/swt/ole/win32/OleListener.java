/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
package org.eclipse.swt.ole.win32;


/**
 * Implementers of <code>OleListener</code> provide a simple
 * <code>handleEvent()</code> method that is used internally
 * by SWT to dispatch events.
 * <p>
 * After creating an instance of a class that implements this
 * interface it can be added to an <code>OleControlSite</code>
 * using the <code>addEventListener()</code> method and removed
 * using the <code>removeEventListener()</code> method.  When the
 * specified event occurs, <code>handleEvent()</code> will be
 * sent to the instance.
 * </p>
 *
 * @see OleControlSite
 */
public interface OleListener {

/**
 * Sent when an event that the receiver has registered for occurs.
 *
 * @param event the event which occurred
 */
public void handleEvent(OleEvent event);
}
