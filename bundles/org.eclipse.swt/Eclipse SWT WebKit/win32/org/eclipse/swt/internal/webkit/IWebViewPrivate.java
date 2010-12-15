/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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

public IWebViewPrivate (int /*long*/ address) {
	super (address);
}

public int clearFocusNode () {
	return COM.VtblCall (27, getAddress ());
}

public int setInitialFocus(boolean forwardEnabled) {
	return COM.VtblCall (28, getAddress (), forwardEnabled);
}

public int setTabKeyCyclesThroughElements(boolean cycles) {
	return COM.VtblCall (21, getAddress (), cycles);
}

public int shouldClose (int /*long*/[] result) {
	return COM.VtblCall (33, getAddress (), result);
}

public int tabKeyCyclesThroughElements (int /*long*/[] result) {
	return COM.VtblCall (22, getAddress (), result);
}

public int viewWindow (int /*long*/[] window) {
	return COM.VtblCall (5, getAddress (), window);
}

}
