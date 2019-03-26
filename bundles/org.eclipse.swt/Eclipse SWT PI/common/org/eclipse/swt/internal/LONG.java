/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal;

/** @jniclass flags=no_gen */
public class LONG {
	public long value;

	public LONG (long value) {
		this.value = value;
	}

	@Override
	public boolean equals (Object object) {
		if (object == this) return true;
		if (!(object instanceof LONG)) return false;
		LONG obj = (LONG)object;
		return obj.value == this.value;
	}

	@Override
	public int hashCode () {
		return (int)(value ^ (value >>> 32));
	}
}
