/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;

/**
 * Instances of this class are sent as a result of accessibility clients
 * sending AccessibleAttribute messages to an accessible object.
 *
 * @see AccessibleAttributeListener
 * @see AccessibleAttributeAdapter
 *
 * @since 3.6
 */
public class AccessibleTextAttributeEvent extends SWTEventObject {

	public int offset;
	public int start, end;
	public TextStyle textStyle;
	public String [] attributes;

	static final long serialVersionUID = 0L; // TODO: run serialver -show

/**
 * Constructs a new instance of this class.
 *
 * @param source the object that fired the event
 */
public AccessibleTextAttributeEvent(Object source) {
	super(source);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
	return "AccessibleAttributeEvent {"
		+ " offset=" + offset   //$NON-NLS-1$
		+ " startOffset=" + start   //$NON-NLS-1$
		+ " endOffset=" + end   //$NON-NLS-1$
		+ " textStyle=" + textStyle   //$NON-NLS-1$
		+ "}";  //$NON-NLS-1$
}
}
