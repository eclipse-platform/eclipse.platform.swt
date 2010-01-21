/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.graphics.*;

/**
 * WARNING: API UNDER CONSTRUCTION
 * 
 * Instances of this class are sent as a result of accessibility clients
 * sending AccessibleTextExtended messages to an accessible object.
 *
 * @see AccessibleTextExtendedListener
 * @see AccessibleTextExtendedAdapter
 *
 * @since 3.6
 */
public class AccessibleTextExtendedEvent extends AccessibleTextEvent {

	public Accessible accessible;

	/**
	 * The value of this field must be set in the accessible table listener method
	 * before returning. What to set it to depends on the listener method called.
	 */
	public String result;
	
	public int count;
	public int index;
	public int start, end;
	public int type;
	public int x, y, width, height;
	public int [] ranges;
	public Rectangle [] rectangles;

	static final long serialVersionUID = 0L; // TODO: run serialver -show

/**
 * Constructs a new instance of this class.
 *
 * @param source the object that fired the event
 */
public AccessibleTextExtendedEvent(Object source) {
	super(source);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
	return "AccessibleTextExtendedEvent {"
		+ "accessible=" + accessible   //$NON-NLS-1$
		+ " string=" + result   //$NON-NLS-1$
		+ " count=" + count   //$NON-NLS-1$
		+ " index=" + index   //$NON-NLS-1$
		+ " offset=" + offset   //$NON-NLS-1$
		+ " length=" + length   //$NON-NLS-1$
		+ " start=" + start   //$NON-NLS-1$
		+ " end=" + end   //$NON-NLS-1$
		+ " type=" + type   //$NON-NLS-1$
		+ " x=" + x   //$NON-NLS-1$
		+ " y=" + y   //$NON-NLS-1$
		+ " width=" + width   //$NON-NLS-1$
		+ " height=" + height   //$NON-NLS-1$
		+ " ranges=" + ranges   //$NON-NLS-1$
		+ " rectangles=" + rectangles   //$NON-NLS-1$
		+ "}";  //$NON-NLS-1$
}
}
