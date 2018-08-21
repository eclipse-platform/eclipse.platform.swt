/*******************************************************************************
 * Copyright (c) 2010, 2016 IBM Corporation and others.
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
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as gestures
 * are triggered by the user interacting with a touch pad or
 * touch screen.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addGestureListener</code> method and removed using
 * the <code>removeGestureListener</code> method. When a
 * gesture is triggered, the appropriate method will be invoked.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
 * SWT doesn't send Gesture or Touch events on GTK.
 * </p>
 *
 * @see GestureEvent
 *
 * @since 3.7
 */
@FunctionalInterface
public interface GestureListener extends SWTEventListener {

/**
 * Sent when a recognized gesture has occurred.
 *
 * @param e an event containing information about the gesture.
 */
void gesture(GestureEvent e);

}