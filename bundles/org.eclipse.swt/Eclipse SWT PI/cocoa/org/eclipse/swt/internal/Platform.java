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

public class Platform {
	public static final String PLATFORM = "cocoa"; //$NON-NLS-1$

public static boolean isLoadable () {
	return Library.isLoadable ();
}

public static void exitIfNotLoadable() {
	if (!Library.isLoadable ()) {
		System.err.println("Libraries for platform " + Platform.PLATFORM + " cannot be loaded because of incompatible environment");
		System.exit(1);
	}
}

}
