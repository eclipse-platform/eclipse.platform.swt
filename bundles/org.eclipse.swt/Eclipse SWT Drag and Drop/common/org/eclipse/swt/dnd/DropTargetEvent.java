package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

/**
 * The DropTargetEvent contains the event information passed in the methods of the DropTargetListener.
 * 
 * <p>When in dragEnter(), dragOver(), dragLeave(), dragOperationChanged(), dropAccept(), 
 * the following fields apply:
 * <ul>
 * <li>(in)widget        - the object on which the data will be dropped if the mouse is released
 * <li>(in)time          - the time the drop occurred
 * <li>(in)x             - the x-cordinate of the cursor relative to the DropTarget <code>Control</code>
 * <li>(in)y             - the y-cordinate of the cursor relative to the DropTarget <code>Control</code>
 * <li>(in)dataTypes     - a list of the types of data that the DragSource can support
 * <li>(in,out)currentDataType - the specific type of data that will be provided on a drop
 * <li>(in)operations    - a list of the operations that the DragSource can support (e.g. DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK)
 * <li>(in,out)detail   - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul>
 * The application can change the operation that will be performed by modifying the <code>detail</code> field 
 * but the choice must be one of the values in the <code>operations</code> field.  
 * The application can also change the type of data being requested by modifying the <code>currentDataTypes</code>
 * field  but the value must be one of the values in the <code>dataTypes list</code>.</p>
 *
 * <p>When in drop(), the following fields apply:
 * <ul>
 * <li>(in)widget - the object on which the data was dropped
 * <li>(in)time    - the time the drop occurred
 * <li>(in)x        - the x-cordinate of the cursor relative to the <code>Display</code>
 * <li>(in)y        - the y-cordinate of the cursor relative to the <code>Display</code>
 * <li>(in,out)detail - the operation being performed (one of DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * <li>(in)currentDataType - the specific type of data that is be contained in the <code>data</code> field
 * <li>(in)data     - the data (which is of type currentDataType); the type Java Object contained in this field is
 *						dependant on the Transfer subclass.  For example, the TextTransfer subclass provides a
 *						String with the text in the String.  The FileTransfer object provides an array of String with
 *						each String in the array containing the full path of the file.
 * </ul>
 * The application can cancel the drop operation by setting the detail field to DND.DROP_NONE.</p>
 *
 */
public class DropTargetEvent extends TypedEvent {
	public int x;
	public int y;
	public int detail;
	/**
	 * A list of the operations that the DragSource can support 
	 * (e.g. DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK).
	 */
	public int operations;
	public int feedback;
	public Widget item;
	
	/*
	 * Platform specfic data.
	 */
	/**
	 * The type of data that will be dropped.
	 */
	public TransferData currentDataType;
	/**
	 * A list of the types of data that the DragSource is capable of providing.
	 * The currentDataType must be a member of this list.
	 */
	public TransferData[] dataTypes;

	
public DropTargetEvent(DNDEvent e) {
	super(e);
	this.data = e.data;
	this.x = e.x;
	this.y = e.y;
	this.detail = e.detail;
	this.currentDataType = e.dataType;
	this.dataTypes = e.dataTypes;
	this.operations = e.operations;
	this.feedback = e.feedback;
	this.item = e.item;
}
void updateEvent(DNDEvent e) {
	e.widget = this.widget;
	e.time = this.time;
	e.data = this.data;
	e.x = this.x;
	e.y = this.y;
	e.detail = this.detail;
	e.dataType = this.currentDataType;
	e.dataTypes = this.dataTypes;
	e.operations = this.operations;
	e.feedback = this.feedback;
	e.item = this.item;
}
}
