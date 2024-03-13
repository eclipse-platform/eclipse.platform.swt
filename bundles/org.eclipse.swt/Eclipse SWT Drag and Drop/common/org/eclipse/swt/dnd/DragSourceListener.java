/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

import org.eclipse.swt.internal.*;

/**
 * The <code>DragSourceListener</code> class provides event notification to the application for DragSource events.
 *
 * <p>When the user drops data on a <code>DropTarget</code>, the application which defines the <code>DragSource</code>
 * must provide the dropped data by implementing <code>dragSetData</code>.  In the dragSetData, the application
 * must support all the data types that were specified in the DragSource#setTransfer method.</p>
 *
 * <p>After the drop has completed successfully or has been aborted, the application which defines the
 * <code>DragSource</code> is required to take the appropriate cleanup action.  In the case of a successful
 * <b>move</b> operation, the application must remove the data that was transferred.</p>
 */
public interface DragSourceListener extends SWTEventListener {

/**
 * The user has begun the actions required to drag the widget. This event gives the application
 * the chance to decide if a drag should be started.
 *
 * <p>The following fields in the DragSourceEvent apply:</p>
 * <ul>
 * <li>(in)widget
 * <li>(in)time
 * <li>(in,out)doit
 * </ul>
 *
 * @param event the information associated with the drag start event
 *
 * @see DragSourceEvent
 */
public void dragStart(DragSourceEvent event);

/**
 * The data is required from the drag source.
 *
 * <p>The following fields in the DragSourceEvent apply:</p>
 * <ul>
 * <li>(in)widget
 * <li>(in)time
 * <li>(in)dataType - the type of data requested.
 * <li>(out)data    - the application inserts the actual data here (must match the dataType)
 * <li>(out)doit    - set this to cancel the drag
 * </ul>
 *
 * @param event the information associated with the drag set data event
 *
 * @see DragSourceEvent
 */
public void dragSetData(DragSourceEvent event);

/**
 * The drop has successfully completed(mouse up over a valid target) or has been terminated (such as hitting
 * the ESC key). Perform cleanup such as removing data from the source side on a successful move operation.
 *
 * <p>The following fields in the DragSourceEvent apply:</p>
 * <ul>
 * <li>(in)widget
 * <li>(in)time
 * <li>(in)doit
 * <li>(in)detail
 * </ul>
 *
 * @param event the information associated with the drag finished event
 *
 * @see DragSourceEvent
 */
public void dragFinished(DragSourceEvent event);
}
