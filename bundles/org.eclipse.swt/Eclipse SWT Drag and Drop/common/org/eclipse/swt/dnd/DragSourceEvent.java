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
package org.eclipse.swt.dnd;

import org.eclipse.swt.events.TypedEvent;

/**
 * The DragSourceEvent contains the event information passed in the methods of the DragSourceListener.
 * 
 * @see DragSourceListener
 */
public class DragSourceEvent extends TypedEvent {
	/**
	 * The operation that was performed.
	 * @see DND#DROP_NONE
	 * @see DND#DROP_MOVE
	 * @see DND#DROP_COPY
	 * @see DND#DROP_LINK
	 * @see DND#DROP_TARGET_MOVE
	 */
 	public int detail;
 	
 	/**
 	 * In dragStart, the doit field determines if the drag and drop operation 
 	 * should proceed; in dragFinished, the doit field indicates whether 
 	 * the operation was performed successfully.
 	 * <p></p>
 	 * In dragStart:
 	 * <p>Flag to determine if the drag and drop operation should proceed.
 	 * The application can set this value to false to prevent the drag from starting. 
 	 * Set to true by default.</p>
 	 * 
 	 * <p>In dragFinished:</p>
 	 * <p>Flag to indicate if the operation was performed successfully. 
 	 * True if the operation was performed successfully.</p>
 	 */
 	public boolean doit;

 	/**
	 * The type of data requested.
	 * Data provided in the data field must be of the same type.
 	 */
	public TransferData dataType;

	static final long serialVersionUID = 3257002142513770808L;
	
/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
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
