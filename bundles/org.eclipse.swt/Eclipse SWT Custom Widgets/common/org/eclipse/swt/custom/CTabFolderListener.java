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

import org.eclipse.swt.internal.*;

/**
 * Classes which implement this interface provide a method
 * that deals with events generated in the CTabFolder.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a CTabFolder using the
 * <code>addCTabFolderListener</code> method and removed using
 * the <code>removeCTabFolderListener</code> method. When a
 * tab item is closed, the itemClosed method will be invoked.
 * </p>
 *
 * @see CTabFolderEvent
 */
@FunctionalInterface
public interface CTabFolderListener extends SWTEventListener {

/**
 * Sent when the user clicks on the close button of an item in the CTabFolder.  The item being closed is specified
 * in the event.item field. Setting the event.doit field to false will stop the CTabItem from closing.
 * When the CTabItem is closed, it is disposed.  The contents of the CTabItem (see CTabItem.setControl) will be
 * made not visible when the CTabItem is closed.
 *
 * @param event an event indicating the item being closed
 *
 * @see CTabItem#setControl
 */
public void itemClosed(CTabFolderEvent event);
}
