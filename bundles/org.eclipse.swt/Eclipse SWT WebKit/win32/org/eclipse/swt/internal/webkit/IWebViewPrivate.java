/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;

public class IWebViewPrivate extends IUnknown {

public IWebViewPrivate (long /*int*/ address) {
	super (address);
}

public int viewWindow (long /*int*/[] window) {
	return COM.VtblCall (5, getAddress (), window);
}

public int setInitialFocus (int forwardEnabled) {
	return COM.VtblCall (28, getAddress (), forwardEnabled);
}

public int shouldClose (int[] result) {
	return COM.VtblCall (33, getAddress (), result);
}

}
