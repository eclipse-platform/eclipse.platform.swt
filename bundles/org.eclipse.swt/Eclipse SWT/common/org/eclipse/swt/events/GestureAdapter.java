/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.events;

/**
 * This adapter class provides default implementations for the
 * methods described by the <code>GestureListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>GestureEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 * @see GestureListener
 * @see GestureEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @since 3.7
 */
public abstract class GestureAdapter implements GestureListener {

/**
 * Sent when a recognized gesture has occurred.
 *
 * @param e an event containing information about the gesture.
 */
public void gesture(GestureEvent e) {
}

}
