package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.SWTEventListener;

/**
 * The <code>DropTargetListener</code> class provides event notification to the application for DropTarget events.
 *
 * <p>As the user moves the cursor into, over and out of a Control that has been designated as a DropTarget, events
 * indicate what operation can be performed and what data can be transferred if a drop where to occur at that point.
 * The application can respond to these events and change the type of data that will be dropped, change the operation 
 * that will be performed or stop any drop from happening on the current target.</p>
 *
 * <p>When the user causes a drop to happen by releasing the mouse over a valid drop target, the application has one 
 * last chance to change the data type of the drop through the DropAccept event.  If the drop is still allowed, the 
 * DropAccept event is immediately followed by the Drop event.  In the Drop event, the application can still change the
 * operation that is performed but the data type is fixed.</p>
 *
 */
public interface DropTargetListener extends SWTEventListener {
/**
 * The cursor has entered the drop target boundaries.
 *
 * <p>The following fields in the DropTargetEvent apply:
 * <ul>
 * <li>(in)widget        - the DropTarget on which the data will be dropped if the mouse is released
 * <li>(in)time          - the time of the event
 * <li>(in)x             - the x-cordinate of the cursor relative to the Display
 * <li>(in)y             - the y-cordinate of the cursor relative to the Display
 * <li>(in)dataTypes     - a list of the types of data that the <code>DragSource</code> can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. <code>DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK</code>)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * <p>The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes</code> list.</p>
 *
 * @param event  the information associated with the drag enter event
 *
 */
public void dragEnter(DropTargetEvent event);
/**
 * The cursor has left the drop target boundaries.
 *
 * <p>The following fields in the DropTargetEvent apply:
 * <ul>
 * <li>(in)widget        - the DropTarget that the mouse has just left
 * <li>(in)time          - the time of the event
 * <li>(in)x             - the x-cordinate of the cursor relative to the Display
 * <li>(in)y             - the y-cordinate of the cursor relative to the Display
 * <li>(in)dataTypes     - a list of the types of data that the <code>DragSource</code> can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. <code>DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK</code>)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * <p>The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes</code> list.</p>
 *
 * @param event  the information associated with the drag leave event
 *
 */

public void dragLeave(DropTargetEvent event);
/**
 * The operation being performed has changed (usually due to the user changing the selected key while dragging).
 *
 * <p>The following fields in the DropTargetEvent apply:
 * <ul>
 * <li>(in)widget        - the DropTarget on which the data will be dropped if the mouse is released
 * <li>(in)time          - the time of the event
 * <li>(in)x             - the x-cordinate of the cursor relative to the Display
 * <li>(in)y             - the y-cordinate of the cursor relative to the Display
 * <li>(in)dataTypes     - a list of the types of data that the <code>DragSource</code> can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. <code>DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK</code>)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * <p>The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes</code> list.</p>
 *
 * @param event  the information associated with the drag operation changed event
 */

public void dragOperationChanged(DropTargetEvent event);
/**
 * The cursor is moving over the drop target.
 *
 * <p>The following fields in the DropTargetEvent apply:
 * <ul>
 * <li>(in)widget        - the DropTarget on which the data will be dropped if the mouse is released
 * <li>(in)time          - the time of the event
 * <li>(in)x             - the x-cordinate of the cursor relative to the Display
 * <li>(in)y             - the y-cordinate of the cursor relative to the Display
 * <li>(in)dataTypes     - a list of the types of data that the <code>DragSource</code> can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. <code>DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK</code>)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * <p>The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes</code> list.</p>
 *
 * @param event  the information associated with the drag over event
 *
 */

public void dragOver(DropTargetEvent event);
/**
 * The data is being dropped.
 *
 * <p>The following fields in DropTargetEvent apply:
 * <ul>
 * <li>(in)widget   - the DropTarget on which the data was dropped
 * <li>(in)time     - the time of the event
 * <li>(in)x        - the x-cordinate of the cursor relative to the Display
 * <li>(in)y        - the y-cordinate of the cursor relative to the Display
 * <li>(in,out)detail - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * <li>(in)currentDataType - the specific type of data that will be provided
 * <li>(in)data     - the data (which is of type currentDataType)
 * </ul></p>
 *
 * <p>The application can refuse to perform the drop operation by setting the detail field to DND.DROP_NONE.</p>
 *
 * @param event the information associated with the drop event
 *
 */

public void drop(DropTargetEvent event);
/**
 * The drop target is given the chance to change the nature of the drop.  It can veto the drop by setting the 
 * <code>event.detail</code> field to <code>DND.DROP_NONE</code>, it can change the data of data that will be 
 * dropped by setting the <code>event.currentDataType</code> field to a different value or it can change the 
 * operation that will be performed by changing the <code>event.detail</code> field.
 *
 * <p>The following fields in the DropTargetEvent apply:
 * <ul>
 * <li>(in)widget        - the DropTarget on which the data was dropped
 * <li>(in)time          - the time of the event
 * <li>(in)x             - the x-cordinate of the cursor relative to the Display
 * <li>(in)y             - the y-cordinate of the cursor relative to the Display
 * <li>(in)dataTypes     - a list of the types of data that the <code>DragSource</code> can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. <code>DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK</code>)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 * <p>The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes</code> list.</p>
 *
 * @param event  the information associated with the drop accept event
 *
 */
public void dropAccept(DropTargetEvent event);
}
