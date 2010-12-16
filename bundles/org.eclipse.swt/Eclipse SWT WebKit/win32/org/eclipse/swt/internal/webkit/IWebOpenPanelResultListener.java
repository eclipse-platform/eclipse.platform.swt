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

public class IWebOpenPanelResultListener extends IUnknown {

	public IWebOpenPanelResultListener(int /*long*/ address) {
		super(address);
	}
	
	public int cancel () {
		return COM.VtblCall (4, getAddress ());
	}
	
	public int chooseFilename (int /*long*/ fileName) {
		return COM.VtblCall (3, getAddress (), fileName);
	}

}
