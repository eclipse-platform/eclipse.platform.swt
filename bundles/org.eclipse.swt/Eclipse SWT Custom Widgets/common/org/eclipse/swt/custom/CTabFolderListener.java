/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.internal.*;

public interface CTabFolderListener extends SWTEventListener {
	
/**
 * Sent when the user clicks on the close button of an item in the CTabFolder.  The item being closed is specified
 * in the event.item field. Setting the event.doit field to false will stop  the CTabItem from closing. 
 * When the CTabItem is closed, it is disposed.  The contents of the CTabItem (see CTabItem#setControl) will be 
 * made not visible when the CTabItem is closed.
 * 
 * @param event an event indicating the item being closed
 */
public void itemClosed(CTabFolderEvent event);
}
