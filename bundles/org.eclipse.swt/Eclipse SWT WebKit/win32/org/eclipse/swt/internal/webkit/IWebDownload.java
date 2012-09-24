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

public class IWebDownload extends IUnknown {

public IWebDownload (long /*int*/ address) {
	super (address);
}

public int cancel () {
	return COM.VtblCall (4, getAddress ());
}

public int setDeletesFileUponFailure (int deletesFileUponFailure) {
	return COM.VtblCall (12, getAddress (), deletesFileUponFailure);
}

public int setDestination (long /*int*/ path, int allowOverwrite) {
	return COM.VtblCall (13, getAddress(), path, allowOverwrite);
}

}
