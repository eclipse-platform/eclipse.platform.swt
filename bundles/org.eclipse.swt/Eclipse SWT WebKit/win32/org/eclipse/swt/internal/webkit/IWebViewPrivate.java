/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

public class IWebViewPrivate extends IUnknown {

public IWebViewPrivate (long /*int*/ address) {
	super (address);
}

public int viewWindow (long /*int*/[] window) {
	return OS.VtblCall (5, getAddress (), window);
}

public int setInitialFocus (int forwardEnabled) {
	return OS.VtblCall (28, getAddress (), forwardEnabled);
}

public int shouldClose (int[] result) {
	return OS.VtblCall (33, getAddress (), result);
}

}
