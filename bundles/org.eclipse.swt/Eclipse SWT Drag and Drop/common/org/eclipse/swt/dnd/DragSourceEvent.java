package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.events.TypedEvent;

/**
 * The DragSourceEvent contains the event information passed in the methods of the DragSourceListener.
 * 
 * <p>dragStart : When a drag operation is about to being, the following fields apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that is initiating the drag
 * <li>time     - (in) the time the drag action occurred
 * <li>doit     - (out) the application can set this value to false to prevent the drag from starting. Set to true by default.
 * </ul></p>
 *
 * <p>dragSetData : When requesting data from the DragSource, the following fields apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that initiated the drag
 * <li>time     - (in) the time the drop occurred
 * <li>dataType - (in) the type of data requested.  This is a TransferData object and can be used with the Transfer subclasses.
 * <li>data     - (out) the application inserts the actual data here (must match the dataType).  This is a Java object and 
 *                      the type of Java object that the application should insert here depends on the data type.  
 *						For example, a TextTransfer requires a Java String containing the text; a FileTransfer requires 
 *						a Java array of String with each String in the array representing the full path of the file.  
 *						To determine what type of object you need to place in this field, consult the description of 
 *						the Transfer subclass
 * </ul></p>
 *
 * <p>dragFinished : When a drag operation has been completed, the following fields apply:
 * <ul>
 * <li>widget   - (in) the DragSource object that initiated the drag
 * <li>time     - (in) the time the drop occurred
 * <li>doit     - (in) true if the operation performed successfully
 * <li>detail   - (in) the operation that was performed (DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK, DND.DROP_NONE)
 * </ul></p>
 *
 */
 
public class DragSourceEvent extends TypedEvent {
 	public int detail;
 	public boolean doit;

 	/**
	 *  Platform specific information about the type of data being transferred.
 	 */
	public TransferData dataType;

public DragSourceEvent(DNDEvent e) {
	super(e);
	this.data = e.data;
	this.detail = e.detail;
	this.doit = e.doit;
	this.dataType = e.dataType;
}
void updateEvent(DNDEvent e) {
	e.widget = this.widget;
	e.time = this.time;
	e.data = this.data;
	e.detail = this.detail;
	e.doit = this.doit;
	e.dataType = this.dataType;
}
}
