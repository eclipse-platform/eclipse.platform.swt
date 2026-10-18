/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin <nikita@nemkin.ru> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class ITfInputProcessorProfiles extends IUnknown {

public ITfInputProcessorProfiles(long address) {
	super(address);
}

public int GetDefaultLanguageProfile(int langid, GUID catid, GUID pclsid, GUID pguidProfile) {
	return COM.VtblCall(8, address, langid, catid, pclsid, pguidProfile);
}

}
