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
package org.eclipse.swt.accessibility;


import java.util.*;

import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are sent as a result of
 * accessibility clients sending messages to controls
 * asking for detailed information about the implementation
 * of the control instance. Typically, only implementors
 * of custom controls need to listen for this event.
 * <p>
 * Note: The meaning of each field depends on the
 * message that was sent.
 * </p>
 *
 * @see AccessibleTextListener
 * @see AccessibleTextAdapter
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.0
 */
public class AccessibleTextEvent extends EventObject {
	public int childID;				// IN
	public int offset, length;		// OUT
	/** @since 3.6 */
	public Accessible accessible;

	/**
	 * The value of this field must be set in the accessible text extended listener method
	 * before returning. What to set it to depends on the listener method called.
	 * @since 3.6
	 */
	public String result;

	/** @since 3.6 */
	public int count;
	/** @since 3.6 */
	public int index;
	/** @since 3.6 */
	public int start, end;
	/** @since 3.6 */
	public int type;
	/** @since 3.6 */
	public int x, y, width, height;
	/** @since 3.6 */
	public int [] ranges;
	/** @since 3.6 */
	public Rectangle [] rectangles;

	static final long serialVersionUID = 3977019530868308275L;

/**
 * Constructs a new instance of this class.
 *
 * @param source the object that fired the event
 */
public AccessibleTextEvent (Object source) {
	super (source);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
@Override
public String toString () {
	return "AccessibleTextEvent {childID=" + childID +  //$NON-NLS-1$
		" offset=" + offset +  //$NON-NLS-1$
		" length=" + length +  //$NON-NLS-1$
		"}"; //$NON-NLS-1$
}
}
