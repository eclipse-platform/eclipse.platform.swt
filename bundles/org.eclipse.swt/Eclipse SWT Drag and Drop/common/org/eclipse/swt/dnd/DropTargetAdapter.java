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
package org.eclipse.swt.dnd;


/**
 * This adapter class provides default implementations for the
 * methods described by the <code>DropTargetListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>DropTargetEvent</code>s can
 * extend this class and override only the methods which they are
 * interested in.
 * </p>
 *
 *<p>
 * Please note, there are subtle difference in DND behavior on different OS's.
 * For example during a file transfer, Windows will put out the file path
 * as soon as the drag begins, where as Linux will make it available only
 * on a drop operation. For correct crossplatform behavior, it is recommended
 * to delay OS interaction until drop has occurred or verify the correctness of
 * the operation across platforms.
 * </p>
 *
 *
 * @see DropTargetListener
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @see <a href="https://eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html"> Eclipse corner article on DND </a>
 */
public class DropTargetAdapter implements DropTargetListener {

/**
 * This implementation of <code>dragEnter</code> permits the default
 * operation defined in <code>event.detail</code>to be performed on the current data type
 * defined in <code>event.currentDataType</code>.
 * For additional information see <code>DropTargetListener.dragEnter</code>.
 *
 * @param event the information associated with the drag enter event
 */
@Override
public void dragEnter(DropTargetEvent event){}

/**
 * This implementation of <code>dragLeave</code> does nothing.
 * For additional information see <code>DropTargetListener.dragOperationChanged</code>.
 *
 * @param event the information associated with the drag leave event
 */
@Override
public void dragLeave(DropTargetEvent event){}

/**
 * This implementation of <code>dragOperationChanged</code> permits the default
 * operation defined in <code>event.detail</code>to be performed on the current data type
 * defined in <code>event.currentDataType</code>.
 * For additional information see <code>DropTargetListener.dragOperationChanged</code>.
 *
 * @param event the information associated with the drag operation changed event
 */
@Override
public void dragOperationChanged(DropTargetEvent event){}

/**
 * This implementation of <code>dragOver</code> permits the default
 * operation defined in <code>event.detail</code>to be performed on the current data type
 * defined in <code>event.currentDataType</code>.
 * For additional information see <code>DropTargetListener.dragOver</code>.
 *
 * @param event the information associated with the drag over event
 */
@Override
public void dragOver(DropTargetEvent event){}

/**
 * This implementation of <code>drop</code> does nothing.
 * For additional information see <code>DropTargetListener.drop</code>.
 *
 * @param event the information associated with the drop event
 */
@Override
public void drop(DropTargetEvent event){}

/**
 * This implementation of <code>dropAccept</code> permits the default
 * operation defined in <code>event.detail</code>to be performed on the current data type
 * defined in <code>event.currentDataType</code>.
 * For additional information see <code>DropTargetListener.dropAccept</code>.
 *
 * @param event the information associated with the drop accept event
 */
@Override
public void dropAccept(DropTargetEvent event){}

}
