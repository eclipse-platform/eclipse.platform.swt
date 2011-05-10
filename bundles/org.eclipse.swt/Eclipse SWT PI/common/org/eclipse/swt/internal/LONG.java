/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

/** @jniclass flags=no_gen */
public class LONG {
	public int /*long*/ value;
	
	public LONG (int /*long*/ value) {
		this.value = value;
	}

	public boolean equals (Object object) {
		if (object == this) return true;
		if (!(object instanceof LONG)) return false;
		LONG obj = (LONG)object;
		return obj.value == this.value;
	}

	public int hashCode () {
		return (int)/*64*/(value ^ (value >>> 32));
	}
}
