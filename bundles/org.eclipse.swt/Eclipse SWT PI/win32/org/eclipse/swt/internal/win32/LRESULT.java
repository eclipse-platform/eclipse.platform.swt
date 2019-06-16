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
package org.eclipse.swt.internal.win32;

/** @jniclass flags=no_gen */
public class LRESULT {
	public long value;
	public static final LRESULT ONE = new LRESULT (1);
	public static final LRESULT ZERO = new LRESULT (0);

public LRESULT (long value) {
	this.value = value;
}

}
