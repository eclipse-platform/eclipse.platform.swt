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

import org.eclipse.swt.internal.*;

/**
 * WARNING: API UNDER CONSTRUCTION
 * 
 * Instances of this class are sent as a result of accessibility clients
 * sending AccessibleTable messages to an accessible object.
 *
 * @see AccessibleTableListener
 * @see AccessibleTableAdapter
 *
 * @since 3.6
 */
public class AccessibleTableEvent extends SWTEventObject {

	public Accessible accessible;
	public Accessible[] accessibles;
	
	/**
	 * The value of this field must be set in the accessible table listener method
	 * before returning. What to set it to depends on the listener method called.
	 */
	public String result;
	
	public int column;
	public int row;
	public int count;
	public boolean isSelected;
	public int[] selected;

	static final long serialVersionUID = 0L; // TODO: run serialver -show

/**
 * Constructs a new instance of this class.
 *
 * @param source the object that fired the event
 */
public AccessibleTableEvent(Object source) {
	super(source);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
	return "AccessibleTableEvent {"
		+ "accessible=" + accessible   //$NON-NLS-1$
		+ " accessibles=" + accessibles   //$NON-NLS-1$
		+ " string=" + result   //$NON-NLS-1$
		+ " isSelected=" + isSelected   //$NON-NLS-1$
		+ " column=" + column   //$NON-NLS-1$
		+ " count=" + count   //$NON-NLS-1$
		+ " row=" + row   //$NON-NLS-1$
		+ " selected=" + selected   //$NON-NLS-1$
		+ "}";  //$NON-NLS-1$
}
}
