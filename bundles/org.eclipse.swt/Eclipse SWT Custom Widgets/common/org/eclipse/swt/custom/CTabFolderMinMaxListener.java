/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.internal.*;

/**
* DO NOT USE - UNDER CONSTRUCTION
*
* @ since 3.0
*/
public interface CTabFolderMinMaxListener extends SWTEventListener {

/**
 * 
 */
public void minimize(CTabFolderEvent event);

/**
 * 
 */
public void maximize(CTabFolderEvent event);

/**
 * 
 */
public void restore(CTabFolderEvent event);

}
