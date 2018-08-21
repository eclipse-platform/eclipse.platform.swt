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

public class IWebDownload extends IUnknown {

public IWebDownload (long /*int*/ address) {
	super (address);
}

public int cancel () {
	return OS.VtblCall (4, getAddress ());
}

public int setDeletesFileUponFailure (int deletesFileUponFailure) {
	return OS.VtblCall (12, getAddress (), deletesFileUponFailure);
}

public int setDestination (long /*int*/ path, int allowOverwrite) {
	return OS.VtblCall (13, getAddress(), path, allowOverwrite);
}

}
