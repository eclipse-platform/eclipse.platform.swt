package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.EventListenerCompatability;

/**
 * The <code>DragSourceListener</code> class provides event notification to the application for DragSource events.
 *
 * <p>When the user drops data on a <code>DropTarget</code>, the application which defines the <code>DragSource</code>
 * must provide the dropped data by implementing <code>dragSetData</code>.</p>
 *
 * <p>After the drop has completed successfully or has been aborted, the application which defines the 
 * <code>DragSource</code> is required to take the appropriate cleanup action.  In the case of a successful 
 * <b>move</b> operation, the application must remove the data that was transferred.</p>
 *
 */
public interface DragSourceListener extends EventListenerCompatability {

/**
 * The user has begun the actions required to drag the widget. This event gives the application 
 * the chance to decide if a drag should be started.
 *
 * <p>The following fields in the DragSourceEvent apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that initiated the drag
 * <li>time     - (in) the time the drop occurred
 * <li>doit     - (out) set to fase by default.  To cause a drag to begin, set this to true.
 * </ul></p>
 *
 * @param event the information associated with the drag finished event
 *
 */
public void dragStart(DragSourceEvent event);

/**
 * The data is required from the drag source.
 *
 * <p>The following fields in the DragSourceEvent apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that initiated the drag
 * <li>time     - (in) the time the drop occurred
 * <li>dataType - (in) the type of data requested.  This is a TransferData object and can be used with the Transfer subclasses.
 * <li>data     - (out) the application inserts the actual data here (must match the dataType)
 * </ul></p>
 *
 * @param event the information associated with the drag set data event
 */
public void dragSetData(DragSourceEvent event);

/**
 * The drop has successfully completed(mouse up over a valid target) or has been terminated (such as hitting 
 * the ESC key). Perform cleanup such as removing data from the source side on a successful move operation.
 *
 * <p>The following fields in the DragSourceEvent apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that initiated the drag
 * <li>time     - (in) the time the drop occurred
 * <li>doit     - (in) true if the operation performed successfully
 * <li>detail   - (in) the operation that was performed (DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * @param event the information associated with the drag finished event
 *
 */
public void dragFinished(DragSourceEvent event);
}
