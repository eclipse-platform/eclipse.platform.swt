package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 *
 * Class <code>DropTarget</code> defines the target object for a drag and drop transfer.
 *
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 *
 * <p>This class identifies the <code>Control</code> over which the user must position the cursor
 * in order to drop the data being transferred.  It also specifies what data types can be dropped on 
 * this control and what operations can be performed.  You may have several DropTragets in an 
 * application but there can only be a one to one mapping between a <code>Control</code> and a <code>DropTarget</code>.
 * The DropTarget can receive data from within the same application or from other applications 
 * (such as text dragged from a text editor like Word).</p>
 *
 * <code><pre>
 *	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
 *	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
 *	DropTarget target = new DropTarget(label, operations);
 *	target.setTransfer(types);
 * </code></pre>
 *
 * <p>The application is notified of data being dragged over this control and of when a drop occurs by 
 * implementing the interface <code>DropTargetListener</code> which uses the class 
 * <code>DropTargetEvent</code>.  The application can modify the type of drag being performed 
 * on this Control at any stage of the drag by modifying the <code>event.detail</code> field or the 
 * <code>event.currentDataType</code> field.  When the data is dropped, it is the responsibility of 
 * the application to copy this data for its own purposes.
 *
 * <code><pre>
 *	target.addDropListener (new DropTargetListener() {
 *		public void dragEnter(DropTargetEvent event) {};
 *		public void dragOver(DropTargetEvent event) {};
 *		public void dragLeave(DropTargetEvent event) {};
 *		public void dragOperationChanged(DropTargetEvent event) {};
 *		public void dropAccept(DropTargetEvent event) {}
 *		public void drop(DropTargetEvent event) {
 *			// A drop has occurred, copy over the data
 *			if (event.data == null) { // no data to copy, indicate failure in event.detail
 *				event.detail = DND.DROP_NONE;
 *				return;
 *			}
 *			label.setText ((String) event.data); // data copied to label text
 *		}
 * 	});
 * </pre></code>
 *
 * <dl>
 *	<dt><b>Styles</b></dt> <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK</dd>
 *	<dt><b>Events</b></dt> <dd>DND.DragEnter, DND.DragLeave, DND.DragOver, DND.DragOperationChanged, 
 *                             DND.DropAccept, DND.Drop </dd>
 * </dl>
 */
public final class DropTarget extends Widget {

	static Callback DragMotion;
	static Callback DragLeave;
	static Callback DragDataReceived;
	static Callback DragDrop;
	static {	
		DragMotion = new Callback(DropTarget.class, "DragMotion", 5);
		DragLeave = new Callback(DropTarget.class, "DragLeave", 3);
		DragDataReceived = new Callback(DropTarget.class, "DragDataReceived", 7);
		DragDrop = new Callback(DropTarget.class, "DragDrop", 5);
	}
	static final String DROPTARGETID = "DropTarget";
	
	// Track key state changes
	int lastOperation = -1;
	
	// Track application selections
	TransferData selectedDataType;
	int selectedOperation;
	
	DragUnderEffect effect;
	Transfer[] transferAgents;
	Listener controlListener;
	Control control;

	private static final int DRAGOVER_HYSTERESIS = 50;
	private long dragOverStart;
	private Runnable dragOverHeartbeat;
	private DNDEvent dragOverEvent;
	
/**
 * Creates a new <code>DropTarget</code> to allow data to be dropped on the specified 
 * <code>Control</code>.
 * Creating an instance of a DropTarget may cause system resources to be allocated 
 * depending on the platform.  It is therefore mandatory that the DropTarget instance 
 * be disposed when no longer required.
 * 
 * @param control the <code>Control</code> over which the user positions the cursor to drop the data
 * @param style the bitwise OR'ing of allowed operations; this may be a combination of any of 
 *		   DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_INIT_DROP - unable to initiate drop target</li>
 * </ul>
 * 
 * @see DropTarget#dispose
 * @see DropTarget#checkSubclass
 * @see DND#DROP_NONE
 * @see DND#DROP_COPY
 * @see DND#DROP_MOVE
 * @see DND#DROP_LINK
 */	
public DropTarget(Control control, int style) {
	super(control, checkStyle(style));
	this.control = control;
	if (control.getData(DROPTARGETID) != null) DND.error(DND.ERROR_CANNOT_INIT_DROP);
	control.setData(DROPTARGETID, this);
	byte[] buffer = Converter.wcsToMbcs(null, "drag_motion", true);
	OS.g_signal_connect(control.handle, buffer, DragMotion.getAddress(), 0);
	buffer = Converter.wcsToMbcs(null, "drag_leave", true);
	OS.g_signal_connect(control.handle, buffer, DragLeave.getAddress(), 0);
	buffer = Converter.wcsToMbcs(null, "drag_data_received", true);
	OS.g_signal_connect(control.handle, buffer, DragDataReceived.getAddress(), 0);
	buffer = Converter.wcsToMbcs(null, "drag_drop", true);
	OS.g_signal_connect(control.handle, buffer, DragDrop.getAddress(), 0);

	// Dispose listeners	
	controlListener = new Listener(){
		public void handleEvent(Event event){
			DropTarget.this.dispose();
		}
	};
	control.addListener(SWT.Dispose, controlListener);	
	this.addListener(SWT.Dispose, new Listener(){
		public void handleEvent(Event event){
			DropTarget.this.onDispose();
		}	
	});

	// Drag under effect
	if (control instanceof Tree) {
		effect = new TreeDragUnderEffect((Tree)control);
	} else if (control instanceof Table) {
		effect = new TableDragUnderEffect((Table)control);
	} else {
		effect = new NoDragUnderEffect(control);
	}

	dragOverHeartbeat = new Runnable() {
		public void run() {
			if (DropTarget.this.control.isDisposed() || dragOverStart == 0) return;
			long time = System.currentTimeMillis();
			int delay = DRAGOVER_HYSTERESIS;
			if (time >= dragOverStart) {
				if (selectedDataType == null) {
					selectedDataType = dragOverEvent.dataTypes[0];
				}
				DNDEvent event = new DNDEvent();
				event.widget = DropTarget.this.control;
				event.time = (int)time;
				event.x = dragOverEvent.x;
				event.y = dragOverEvent.y;
				event.dataTypes  = dragOverEvent.dataTypes;
				event.feedback = DND.FEEDBACK_SELECT;
				int allowedOperations = dragOverEvent.operations;
				TransferData[] allowedTypes = event.dataTypes;
				event.operations = allowedOperations;
				event.dataType  = selectedDataType;
				event.detail  = selectedOperation;
				notifyListeners(DND.DragOver, event);
				effect.show(event.feedback, event.x, event.y);
				selectedDataType = null;
				if (event.dataType != null) {
					for (int i = 0; i < allowedTypes.length; i++) {
						if (allowedTypes[i].type == event.dataType.type) {
							selectedDataType = event.dataType;
							break;
						}
					}
				}
			
				selectedOperation = DND.DROP_NONE;
				if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
					selectedOperation = event.detail;
				}
			} else {
				delay = (int)(dragOverStart - time);
			}
			DropTarget.this.control.getDisplay().timerExec(delay, dragOverHeartbeat);
		}
	};
}

static DropTarget FindDropTarget(int handle) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	Widget widget = display.findWidget(handle);
	if (widget == null) return null;
	return (DropTarget)widget.getData(DROPTARGETID);
}
static int DragDataReceived ( int widget, int context, int x, int y, int data, int info, int time){
	DropTarget target = FindDropTarget(widget);
	if (target == null) return 0;
	return target.dragDataReceived (widget, context, x, y, data, info, time);
}
static int DragDrop(int widget, int context, int x, int y, int time) {
	DropTarget target = FindDropTarget(widget);
	if (target == null) return 0;
	return target.dragDrop (widget, context, x, y, time);
}
static int DragLeave ( int widget, int context, int time){
	DropTarget target = FindDropTarget(widget);
	if (target == null) return 0;
	return target.dragLeave (widget, context, time);
}
static int DragMotion ( int widget, int context, int x, int y, int time){
	DropTarget target = FindDropTarget(widget);
	if (target == null) return 0;
	return target.dragMotion (widget, context, x, y, time);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a drag and drop operation is in progress, by sending
 * it one of the messages defined in the <code>DropTargetListener</code>
 * interface.
 * 
 * <p><ul>
 * <li><code>dragEnter</code> is called when the cursor has entered the drop target boundaries
 * <li><code>dragLeave</code> is called when the cursor has left the drop target boundaries and just before
 * the drop occurs or is cancelled.
 * <li><code>dragOperationChanged</code> is called when the operation being performed has changed 
 * (usually due to the user changing the selected modifier key(s) while dragging)
 * <li><code>dragOver</code> is called when the cursor is moving over the drop target
 * <li><code>dropAccept</code> is called just before the drop is performed.  The drop target is given 
 * the chance to change the nature of the drop or veto the drop by setting the <code>event.detail</code> field
 * <li><code>drop</code> is called when the data is being dropped
 * </ul></p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DropTargetListener
 * @see #removeDropListener
 * @see DropTargetEvent
 */
public void addDropListener(DropTargetListener listener) {	
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener (listener);
	addListener (DND.DragEnter, typedListener);
	addListener (DND.DragLeave, typedListener);
	addListener (DND.DragOver, typedListener);
	addListener (DND.DragOperationChanged, typedListener);
	addListener (DND.Drop, typedListener);
	addListener (DND.DropAccept, typedListener);	
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;	
	return style;
}	

int dragDataReceived ( int widget, int context, int x, int y, int data, int info, int time){
	// Get data in a Java format	
	Object object = null;
	TransferData transferData = new TransferData();
	if (data != 0) {
		GtkSelectionData selectionData = new GtkSelectionData(); 
		OS.memmove(selectionData, data, GtkSelectionData.sizeof);
		if (selectionData.data != 0) {
			transferData.type = selectionData.type;
			transferData.length = selectionData.length;
			transferData.pValue = selectionData.data;
			transferData.format = selectionData.format;
			Transfer transfer = null;
			for (int i = 0; i < transferAgents.length; i++) {
				transfer = transferAgents[i];
				if (transfer.isSupportedType(transferData)) break;
			}
			if (transfer != null) {
				object = transfer.nativeToJava(transferData);
			}
		}
	}
	
	//Notify listeners
	DNDEvent event = new DNDEvent();
	if (!setEventData(context, x, y, time, event)) return 0;
	event.detail = selectedOperation;
	event.dataType = transferData;
	event.data = object;
	try {
		notifyListeners(DND.Drop, event);
	} catch (Throwable e) {
		event.detail = DND.DROP_NONE;
	}
	
	//Terminate DND and indicate if a move operation occurred
	OS.gtk_drag_finish(context, event.detail != DND.DROP_NONE, event.detail == DND.DROP_MOVE, time); 			
	return 1;	
}

int dragLeave ( int widget, int context, int time){
	updateDragOverHover(0, null);
	effect.show(DND.FEEDBACK_NONE, 0, 0);
	lastOperation = -1;
					
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)(long)time;
	event.detail = DND.DROP_NONE;
	try {
		notifyListeners(DND.DragLeave, event);
	} catch (Throwable e) {
	}
	return 1;
		
}

int dragDrop(int widget, int context, int x, int y, int time) {
	updateDragOverHover(0, null);
	DNDEvent event = new DNDEvent();
	if (!setEventData(context, x, y, time, event)) return 0;
	int allowedOperations = event.operations;
	TransferData[] allowedTypes = event.dataTypes;
	event.dataType = selectedDataType;
	event.detail = selectedOperation;
	
	try {
		notifyListeners(DND.DropAccept,event);
	} catch (Throwable err) {
		event.detail = DND.DROP_NONE;
		event.dataType = null;
	}

	selectedDataType = null;
	if (event.dataType != null) {
		for (int i = 0; i < allowedTypes.length; i++) {
			if (allowedTypes[i].type == event.dataType.type) {
				selectedDataType = event.dataType;
				break;
			}
		}
	}
	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((event.detail & allowedOperations) == event.detail)) {
		selectedOperation = event.detail;
	}

	if (selectedOperation == DND.DROP_NONE) {
		// this was not a successful drop
		return 0;
	}

	OS.gtk_drag_get_data(widget, context, selectedDataType.type, time);
	return 1;
}

private void updateDragOverHover(long delay, DNDEvent event) {
	if (delay == 0) {
		dragOverStart = 0;
		dragOverEvent = null;
		return;
	}
	dragOverStart = System.currentTimeMillis() + delay;
	if (dragOverEvent == null) dragOverEvent = new DNDEvent();
	dragOverEvent.x = event.x;
	dragOverEvent.y = event.y;
	dragOverEvent.dataTypes  = event.dataTypes;
	dragOverEvent.operations = event.operations;
	dragOverEvent.dataType  = event.dataType;
	dragOverEvent.detail  = event.detail;
}

int dragMotion ( int widget, int context, int x, int y, int time){
	DNDEvent event = new DNDEvent();
	if (!setEventData(context, x, y, time, event)) {
		OS.gdk_drag_status(context, 0, time);
		return 0;
	}
	int allowedOperations = event.operations;
	TransferData[] allowedTypes = event.dataTypes;
		
	if (event.detail == DND.DROP_DEFAULT && (getStyle() & DND.DROP_DEFAULT) == 0) {
		if ((allowedOperations & DND.DROP_MOVE) != 0) {
			event.detail = DND.DROP_MOVE;
		} else {
			event.detail = DND.DROP_NONE;
		}
	}

	if (lastOperation == -1) {
			event.type = DND.DragEnter;
			int atom = OS.gtk_drag_dest_find_target(control.handle, context, 0);
			if (atom == 0) {
				OS.gdk_drag_status(context, 0, time);
				return 0;
			}
			event.dataType = new TransferData();
			event.dataType.type = atom;
			lastOperation = event.detail;
	} else {
			if (lastOperation != event.detail) {
				event.type = DND.DragOperationChanged;
				lastOperation = event.detail;
			} else {
				event.type = DND.DragOver;
				event.detail = selectedOperation;
			}
			event.dataType = selectedDataType;
	}
	event.feedback = DND.FEEDBACK_SELECT;
	
	updateDragOverHover(DRAGOVER_HYSTERESIS, event);
						
	try {
		notifyListeners(event.type, event);	
	} catch (Throwable e) {
		event.detail = DND.DROP_NONE;
		event.dataType = null;
	}

	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = DND.DROP_MOVE;
	}
	
	selectedDataType = null;
	if (event.dataType != null) {
		for (int i = 0; i < allowedTypes.length; i++) {
			if (allowedTypes[i].type == event.dataType.type) {
				selectedDataType = event.dataType;
				break;
			}
		}
	}

	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
		selectedOperation = event.detail;
	}
	effect.show(event.feedback, event.x, event.y);

	switch (selectedOperation) {
		case DND.DROP_NONE:
			OS.gdk_drag_status(context, 0, time);
			break;		
		case DND.DROP_COPY:
			OS.gdk_drag_status(context, OS.GDK_ACTION_COPY, time);
			break;
		case DND.DROP_MOVE:
			OS.gdk_drag_status(context, OS.GDK_ACTION_MOVE, time);
			break;
		case DND.DROP_LINK:
			OS.gdk_drag_status(context, OS.GDK_ACTION_LINK, time);
			break;
	}
	
	if (event.type == DND.DragEnter) {
		dragOverHeartbeat.run();
	}
	return 1;
}

/**
 * Returns the Control which is registered for this DropTarget.  This is the control over which the 
 * user positions the cursor to drop the data.
 *
 * @return the Control which is registered for this DropTarget
 */
public Control getControl () {
	return control;
}

public Display getDisplay () {
	if (control == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	return control.getDisplay ();
}

/**
 * Returns a list of the data types that can be transferred to this DropTarget.
 *
 * @return a list of the data types that can be transferred to this DropTarget
 */ 
public Transfer[] getTransfer() { 
	return transferAgents; 
}

public void notifyListeners (int eventType, Event event) {
	Point coordinates = new Point(event.x, event.y);
	coordinates = control.toControl(coordinates);
	if (this.control instanceof Tree) {
		event.item = ((Tree)control).getItem(coordinates);
	}
	if (this.control instanceof Table) {
		event.item = ((Table)control).getItem(coordinates);
	}
	super.notifyListeners(eventType, event);
}

private void onDispose(){
	if (transferAgents != null){
		OS.gtk_drag_dest_unset(control.handle);
	if (controlListener != null)
		control.removeListener(SWT.Dispose, controlListener);
	}
	control = null;
	controlListener = null;
}

private int opToOsOp(int operation){
	int osOperation = 0;
	if ((operation & DND.DROP_COPY) == DND.DROP_COPY)
		osOperation |= OS.GDK_ACTION_COPY;
	if ((operation & DND.DROP_MOVE) == DND.DROP_MOVE)
		osOperation |= OS.GDK_ACTION_MOVE;
	if ((operation & DND.DROP_LINK) == DND.DROP_LINK)
		osOperation |= OS.GDK_ACTION_LINK;
	return osOperation;
}

private int osOpToOp(int osOperation){
	int operation = DND.DROP_NONE;
	if ((osOperation & OS.GDK_ACTION_COPY) == OS.GDK_ACTION_COPY)
		operation |= DND.DROP_COPY;
	if ((osOperation & OS.GDK_ACTION_MOVE) == OS.GDK_ACTION_MOVE)
		operation |= DND.DROP_MOVE;
	if ((osOperation & OS.GDK_ACTION_LINK) == OS.GDK_ACTION_LINK)
		operation |= DND.DROP_LINK;
	return operation;
}

/**	 
 * Removes the listener from the collection of listeners who will
 * be notified when a drag and drop operation is in progress.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DropTargetListener
 * @see #addDropListener
 */
public void removeDropListener(DropTargetListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (DND.DragEnter, listener);
	removeListener (DND.DragLeave, listener);
	removeListener (DND.DragOver, listener);
	removeListener (DND.DragOperationChanged, listener);
	removeListener (DND.Drop, listener);
	removeListener (DND.DropAccept, listener);
}

/**
 * Specifies the data types that can be transferred to this DropTarget.  If data is 
 * being dragged that does not match one of these types, the drop target will be notified of 
 * the drag and drop operation but the currentDataType will be null and the operation 
 * will be DND.NONE.
 *
 * @param transferAgents a list of Transfer objects which define the types of data that can be
 *						 dropped on this target
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transferAgents is null</li>
 * </ul>
 */
public void setTransfer(Transfer[] transferAgents){
	if (transferAgents == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	
	if (this.transferAgents != null) {
		OS.gtk_drag_dest_unset(control.handle);
	}
	this.transferAgents = transferAgents;
	
	GtkTargetEntry[] targets = new GtkTargetEntry[0];
	for (int i = 0; i < transferAgents.length; i++) {
		Transfer transfer = transferAgents[i];
		int[] typeIds = transfer.getTypeIds();
		String[] typeNames = transfer.getTypeNames();
		for (int j = 0; j < typeIds.length; j++) {
			GtkTargetEntry entry = new GtkTargetEntry();
			byte[] buffer = Converter.wcsToMbcs(null, typeNames[j], true);
			entry.target = OS.g_malloc(buffer.length);
			OS.memmove(entry.target, buffer, buffer.length);						
			entry.info = typeIds[j];
			GtkTargetEntry[] newTargets = new GtkTargetEntry[targets.length + 1];
			System.arraycopy(targets, 0, newTargets, 0, targets.length);
			newTargets[targets.length] = entry;
			targets = newTargets;
		}	
	}
	
	int pTargets = OS.g_malloc(targets.length * GtkTargetEntry.sizeof);
	for (int i = 0; i < targets.length; i++) {
		OS.memmove(pTargets + i*GtkTargetEntry.sizeof, targets[i], GtkTargetEntry.sizeof);		
	}			
	
	int actions = opToOsOp(getStyle());
	OS.gtk_drag_dest_set(control.handle, 0, pTargets, targets.length, actions);
	
	for (int i = 0; i < targets.length; i++) {
		OS.g_free(targets[i].target);
	}
}

boolean setEventData(int context, int x, int y, int time, DNDEvent event) {
	if (context == 0) return false;
	GdkDragContext dragContext = new GdkDragContext();
	OS.memmove(dragContext, context, GdkDragContext.sizeof);
	if (dragContext.targets == 0) return false;
	int length = OS.g_list_length(dragContext.targets);
	TransferData[] tdata = new TransferData[0];
	for (int i = 0; i < length; i++) {
		int pData = OS.g_list_nth(dragContext.targets, i);
		GtkTargetPair gtkTargetPair = new GtkTargetPair();
		OS.memmove(gtkTargetPair, pData, GtkTargetPair.sizeof);
		TransferData data = new TransferData();
		data.type = gtkTargetPair.target;
		TransferData[] newTdata = new TransferData[tdata.length + 1];
		System.arraycopy(tdata, 0, newTdata, 0, tdata.length);
		newTdata[tdata.length] = data;
		tdata = newTdata;
	}
	if (tdata.length == 0) return false;

	Point coordinates = control.toDisplay( new Point( x, y) );

	event.widget = this;
	event.x = coordinates.x;
	event.y = coordinates.y;
	event.dataTypes	= tdata;
	int operations = osOpToOp(dragContext.actions);
	if (operations == DND.DROP_COPY ||
	    operations == DND.DROP_LINK || 
	    operations == DND.DROP_MOVE || 
	    operations == DND.DROP_NONE) {
			event.detail = operations;
	} else {
			event.detail = DND.DROP_DEFAULT;
	}
	event.operations = operations;
	event.time = (int)(long)time;
	return true;
}
}