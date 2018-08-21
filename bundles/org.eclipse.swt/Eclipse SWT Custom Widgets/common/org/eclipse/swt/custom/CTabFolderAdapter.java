/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.custom;


/**
 * This adapter class provides a default implementation for the
 * method described by the <code>CTabFolderListener</code> interface.
 *
 * @see CTabFolderListener
 * @see CTabFolderEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class CTabFolderAdapter implements CTabFolderListener {
	@Override
	public void itemClosed(CTabFolderEvent event){}
}
