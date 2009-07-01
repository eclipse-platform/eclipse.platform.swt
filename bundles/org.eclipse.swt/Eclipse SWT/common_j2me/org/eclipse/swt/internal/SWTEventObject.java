/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

 
/**
 * This class is the cross-platform version of the
 * java.util.EventObject class.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * extend SWTEventObject instead of java.util.EventObject.
 * </p>
 * <p>
 * Note: java.util.EventObject is not part of CDC and CLDC.
 * </p>
 */
public class SWTEventObject implements SerializableCompatibility {
	
	/**
	 * The event source.
	 */
	protected transient Object source;

/**
 * Constructs a new instance of this class.
 *
 * @param source the object which fired the event
 */
public SWTEventObject(Object source) {
	if (source != null) this.source = source;
	else throw new IllegalArgumentException();
}

/**
 * Answers the event source.
 *
 * @return the object which fired the event
 */
public Object getSource() {
	return source;
}

/**
 * Answers the string representation of this SWTEventObject.
 *
 * @return the string representation of this SWTEventObject
 */
public String toString() {
	return getClass().getName() + "[source=" + String.valueOf(source) + ']';
}

}
