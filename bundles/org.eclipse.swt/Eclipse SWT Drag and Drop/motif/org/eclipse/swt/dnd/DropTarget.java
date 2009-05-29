/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

/**
 *
 * Class <code>DropTarget</code> defines the target object for a drag and drop transfer.
 *
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#dnd">Drag and Drop snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: DNDExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DropTarget extends Widget {
	
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DropTargetEffect dropEffect;
	
	// Track application selections
	TransferData selectedDataType;
	int selectedOperation;
	
	// workaround - Simulate events when the mouse is not moving
	long dragOverStart;
	Runnable dragOverHeartbeat;
	DNDEvent dragOverEvent;
	
	// workaround - The data required in the transferProc callback is not available 
	// so store it from the dropProc callback 
	XmDropProcCallbackStruct droppedEventData;
	int dropTransferObject;
	
	// workaround - The DND operation can time out so temporarily set the
	// time out to be very long while the application processes the data and
	// restore it to the previous value when done.
	int selectionTimeout;
	
	// workaround - A drop target is still active even when it is not visible.
	// This causes problems when the widget is not visible but
	// overlaps with a visible widget.  Deregister drop target when it is
	// not visible.
	boolean registered = false;
	
	int deleteAtom, nullAtom;
	
	static final String DEFAULT_DROP_TARGET_EFFECT = "DEFAULT_DROP_TARGET_EFFECT"; //$NON-NLS-1$
	static byte [] DELETE = Converter.wcsToMbcs (null, "DELETE", true); //$NON-NLS-1$
	static byte [] NULL = Converter.wcsToMbcs (null, "NULL", true); //$NON-NLS-1$
	static final int DRAGOVER_HYSTERESIS = 50;
	
	static Callback DropProc;
	static Callback DragProc;
	static Callback TransferProc;
	
	static {
		DropProc = new Callback(DropTarget.class, "DropProcCallback", 3); //$NON-NLS-1$
		if (DropProc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		DragProc = new Callback(DropTarget.class, "DragProcCallback", 3); //$NON-NLS-1$
		if (DragProc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		TransferProc = new Callback(DropTarget.class, "TransferProcCallback", 7); //$NON-NLS-1$
		if (TransferProc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}
	
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
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_INIT_DROP - unable to initiate drop target; this will occur if more than one
 *        drop target is created for a control or if the operating system will not allow the creation
 *        of the drop target</li>
 * </ul>
 *
 * <p>NOTE: ERROR_CANNOT_INIT_DROP should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatibility.</p>
 * 
 * @see Widget#dispose
 * @see DropTarget#checkSubclass
 * @see DND#DROP_NONE
 * @see DND#DROP_COPY
 * @see DND#DROP_MOVE
 * @see DND#DROP_LINK
 */
public DropTarget(Control control, int style) {
	super (control, checkStyle(style));
	this.control = control;
	if (DropProc == null || DragProc == null || TransferProc == null) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}
	if (control.getData(DND.DROP_TARGET_KEY) != null) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}
	control.setData(DND.DROP_TARGET_KEY, this);
	int xDisplay = Display.getDefault().xDisplay;
	deleteAtom = OS.XmInternAtom (xDisplay, DELETE, false);
	nullAtom = OS.XmInternAtom (xDisplay, NULL, false);

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose: {
					if (!DropTarget.this.isDisposed()){
						onDispose();
					}
					break;
				}
				case SWT.Show: {
					if (!registered) {
						registerDropTarget();
					} else {
						int[] args = new int[]{OS.XmNdropSiteActivity,   OS.XmDROP_SITE_ACTIVE,};
						OS.XmDropSiteUpdate(DropTarget.this.control.handle, args, args.length/2);
						if (DropTarget.this.control instanceof Label) {
							int formHandle = OS.XtParent (DropTarget.this.control.handle);
							OS.XmDropSiteUpdate(formHandle, args, args.length / 2);
						}
					}
					break;
				}
				case SWT.Hide: {
					int[] args = new int[]{OS.XmNdropSiteActivity,   OS.XmDROP_SITE_INACTIVE,};
					OS.XmDropSiteUpdate(DropTarget.this.control.handle, args, args.length/2);	
					if (DropTarget.this.control instanceof Label) {
						int formHandle = OS.XtParent (DropTarget.this.control.handle);
						OS.XmDropSiteUpdate(formHandle, args, args.length / 2);
					}
					break;
				}
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	Control c = control;
	while (c != null) {
		c.addListener (SWT.Show, controlListener);
		c.addListener (SWT.Hide, controlListener);
		c = c.getParent();
	}

	this.addListener(SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			if (DropTarget.this.control == null || 
				DropTarget.this.control.isDisposed()) return;
			
			unregisterDropTarget();
			onDispose();
		}
	});

	Object effect = control.getData(DEFAULT_DROP_TARGET_EFFECT);
	if (effect instanceof DropTargetEffect) {
		dropEffect = (DropTargetEffect) effect;
	} else if (control instanceof Table) {
		dropEffect = new TableDropTargetEffect((Table) control);
	} else if (control instanceof Tree) {
		dropEffect = new TreeDropTargetEffect((Tree) control);
	}

	if (control.isVisible()) registerDropTarget();
	
	dragOverHeartbeat = new Runnable() {
		public void run() {
			Control control = DropTarget.this.control;
			if (control == null || control.isDisposed() || dragOverStart == 0) return;
			long time = System.currentTimeMillis();
			int delay = DRAGOVER_HYSTERESIS;
			if (time < dragOverStart) {
				delay = (int)(dragOverStart - time);
			} else {	
				int allowedOperations = dragOverEvent.operations;
				TransferData[] allowedTypes = dragOverEvent.dataTypes;
				//pass a copy of data types in to listeners in case application modifies it
				TransferData[] dataTypes = new TransferData[allowedTypes.length];
				System.arraycopy(allowedTypes, 0, dataTypes, 0, dataTypes.length);
	
				DNDEvent event = new DNDEvent();
				event.widget = dragOverEvent.widget;
				event.x = dragOverEvent.x;
				event.y = dragOverEvent.y;
				event.time = (int)time;
				event.feedback = DND.FEEDBACK_SELECT;
				event.dataTypes = dataTypes;
				event.dataType = selectedDataType;
				event.operations = dragOverEvent.operations;
				event.detail  = selectedOperation;
				if (dropEffect != null) {
					event.item = dropEffect.getItem(dragOverEvent.x, dragOverEvent.y);
				}
				notifyListeners(DND.DragOver, event);
				
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
			}
			control = DropTarget.this.control;
			if (control == null || control.isDisposed ()) return;
			control.getDisplay().timerExec(delay, dragOverHeartbeat);
		}
	};
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

static int DragProcCallback(int widget, int client_data, int call_data) {
	DropTarget target = FindDropTarget(widget);
	if (target != null) {
		target.dragProcCallback(widget, client_data, call_data);
	}
	return 0;
}

static int DropProcCallback(int widget, int client_data, int call_data) {
	DropTarget target = FindDropTarget(widget);
	if (target != null) {
		target.dropProcCallback(widget, client_data, call_data);
	}
	return 0;
}

static DropTarget FindDropTarget(int handle) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	Widget widget = display.findWidget(handle);
	if (widget == null) return null;
	return (DropTarget)widget.getData(DND.DROP_TARGET_KEY);
}

static int TransferProcCallback(int widget, int client_data, int pSelection, int pType, int pValue, int pLength, int pFormat) {
	DropTarget target = FindDropTarget(client_data);
	if (target != null) {
		target.transferProcCallback(widget, client_data, pSelection, pType, pValue, pLength, pFormat);
	}
	return 0;
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
 * @see #getDropListeners
 * @see #removeDropListener
 * @see DropTargetEvent
 */
public void addDropListener(DropTargetListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener (listener);
	typedListener.dndWidget = this;
	addListener (DND.DragEnter, typedListener);
	addListener (DND.DragLeave, typedListener);
	addListener (DND.DragOver, typedListener);
	addListener (DND.DragOperationChanged, typedListener);
	addListener (DND.Drop, typedListener);
	addListener (DND.DropAccept, typedListener);
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

void dragProcCallback(int widget, int client_data, int call_data) {
	if (call_data == 0) return;
	XmDragProcCallbackStruct callbackData = new XmDragProcCallbackStruct();
	OS.memmove(callbackData, call_data, XmDragProcCallbackStruct.sizeof);

	if (callbackData.reason == OS.XmCR_DROP_SITE_LEAVE_MESSAGE) {
		updateDragOverHover(0, null);
		
		if (callbackData.dropSiteStatus == OS.XmDROP_SITE_INVALID) {
			return;
		}

		DNDEvent event = new DNDEvent();
		event.widget = this;
		event.time = callbackData.timeStamp;
		event.detail = DND.DROP_NONE;
		notifyListeners(DND.DragLeave, event);
		return;
	}
	
	if (callbackData.reason == OS.XmCR_DROP_SITE_ENTER_MESSAGE) {
		selectedDataType = null;
		selectedOperation = DND.DROP_NONE;
		droppedEventData = null;
		dropTransferObject = 0;
	}
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(callbackData.operations, callbackData.operation, callbackData.dragContext, callbackData.x, callbackData.y, callbackData.timeStamp, event)) {
		callbackData.dropSiteStatus = (byte)OS.XmDROP_SITE_INVALID;
		callbackData.operation = opToOsOp(DND.DROP_NONE);
		OS.memmove(call_data, callbackData, XmDragProcCallbackStruct.sizeof);
		return;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	
	switch (callbackData.reason) {
		case OS.XmCR_DROP_SITE_ENTER_MESSAGE :
			event.type = DND.DragEnter;
			break;
		case OS.XmCR_DROP_SITE_MOTION_MESSAGE :
			event.type = DND.DragOver;
			event.dataType = selectedDataType;
			event.detail = selectedOperation;
			break;
		case OS.XmCR_OPERATION_CHANGED :
			event.type = DND.DragOperationChanged;
			event.dataType = selectedDataType;
			break;
	}
	
	updateDragOverHover(DRAGOVER_HYSTERESIS, event);
	notifyListeners(event.type, event);
	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
	}
	selectedDataType = null;
	if (event.dataType != null) {
		for (int i = 0; i < allowedDataTypes.length; i++) {
			if (allowedDataTypes[i].type == event.dataType.type) {
				selectedDataType = allowedDataTypes[i];
				break;
			}
		}
	}

	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && (allowedOperations & event.detail) != 0) {
		selectedOperation = event.detail;
	}

	callbackData.dropSiteStatus = (byte)OS.XmDROP_SITE_VALID;
	callbackData.operation = opToOsOp(selectedOperation);
	OS.memmove(call_data, callbackData, XmDragProcCallbackStruct.sizeof);
	
	if (callbackData.reason == OS.XmCR_DROP_SITE_ENTER_MESSAGE) {
		dragOverHeartbeat.run();
	}
}

void dropProcCallback(int widget, int client_data, int call_data) {
	if (call_data == 0) return;
	droppedEventData = new XmDropProcCallbackStruct();
	OS.memmove(droppedEventData, call_data, XmDropProcCallbackStruct.sizeof);	
	
	if (droppedEventData.dropSiteStatus == OS.XmDROP_SITE_INVALID) {
		int[] args = new int[] {OS.XmNtransferStatus, OS.XmTRANSFER_FAILURE, OS.XmNnumDropTransfers, 0};
		dropTransferObject = OS.XmDropTransferStart(droppedEventData.dragContext, args, args.length / 2);
		return;
	}
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(droppedEventData.operations, droppedEventData.operation, droppedEventData.dragContext, droppedEventData.x, droppedEventData.y, droppedEventData.timeStamp, event)){
		return;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	event.dataType = selectedDataType;
	event.detail = selectedOperation;
	notifyListeners(DND.DropAccept,event);
	selectedDataType = null;
	if (event.dataType != null) {
		for (int i = 0; i < allowedDataTypes.length; i++) {
			if (allowedDataTypes[i].type == event.dataType.type) {
				selectedDataType = allowedDataTypes[i];
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
		int[] args = new int[] {OS.XmNtransferStatus, OS.XmTRANSFER_FAILURE, OS.XmNnumDropTransfers, 0};
		dropTransferObject = OS.XmDropTransferStart(droppedEventData.dragContext, args, args.length / 2);
		return;
	}

	// ask drag source for dropped data
	int[] transferEntries = new int[2];
	transferEntries[0] = control.handle; // pass control handle as data to locate drop target
	transferEntries[1] = selectedDataType.type;
		
	int pTransferEntries = OS.XtMalloc(transferEntries.length * 4);
	OS.memmove(pTransferEntries, transferEntries, transferEntries.length * 4);

	int xtContext = OS.XtDisplayToApplicationContext(getDisplay().xDisplay);
	selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
	OS.XtAppSetSelectionTimeout(xtContext, 0x7fffffff);
		
	int[] args = new int[] {OS.XmNdropTransfers, pTransferEntries,
				            OS.XmNnumDropTransfers, transferEntries.length / 2,
				            OS.XmNtransferProc, TransferProc.getAddress()};

	dropTransferObject = OS.XmDropTransferStart(droppedEventData.dragContext, args, args.length / 2);
	OS.XtFree(pTransferEntries);
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

/**
 * Returns an array of listeners who will be notified when a drag and drop 
 * operation is in progress, by sending it one of the messages defined in 
 * the <code>DropTargetListener</code> interface.
 *
 * @return the listeners who will be notified when a drag and drop 
 * operation is in progress
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DropTargetListener
 * @see #addDropListener
 * @see #removeDropListener
 * @see DropTargetEvent
 * 
 * @since 3.4
 */
public DropTargetListener[] getDropListeners() {
	Listener[] listeners = getListeners(DND.DragEnter);
	int length = listeners.length;
	DropTargetListener[] dropListeners = new DropTargetListener[length];
	int count = 0;
	for (int i = 0; i < length; i++) {
		Listener listener = listeners[i];
		if (listener instanceof DNDListener) {
			dropListeners[count] = (DropTargetListener) ((DNDListener) listener).getEventListener();
			count++;
		}
	}
	if (count == length) return dropListeners;
	DropTargetListener[] result = new DropTargetListener[count];
	System.arraycopy(dropListeners, 0, result, 0, count);
	return result;
}

/**
 * Returns the drop effect for this DropTarget.  This drop effect will be 
 * used during a drag and drop to display the drag under effect on the 
 * target widget.
 *
 * @return the drop effect that is registered for this DropTarget
 * 
 * @since 3.3
 */
public DropTargetEffect getDropTargetEffect() {
	return dropEffect;
}

/**
 * Returns a list of the data types that can be transferred to this DropTarget.
 *
 * @return a list of the data types that can be transferred to this DropTarget
 */
public Transfer[] getTransfer() {
	return transferAgents;
}

void onDispose() {
	if (control == null) return;
	if (controlListener != null) {
		Control c = control;
		while (c != null) {
			c.removeListener (SWT.Show, controlListener);
			c.removeListener (SWT.Hide, controlListener);
			c = c.getParent();
		}
		control.removeListener(SWT.Dispose, controlListener);
	}
	controlListener = null;
	control.setData(DND.DROP_TARGET_KEY, null);
	control = null;
	transferAgents = null;
}
byte opToOsOp(int operation){
	byte osOperation = OS.XmDROP_NOOP;
	
	if ((operation & DND.DROP_COPY) == DND.DROP_COPY)
		osOperation |= OS.XmDROP_COPY;
	if ((operation & DND.DROP_MOVE) == DND.DROP_MOVE)
		osOperation |= OS.XmDROP_MOVE;
	if ((operation & DND.DROP_LINK) == DND.DROP_LINK)
		osOperation |= OS.XmDROP_LINK;
	
	return osOperation;
}
int osOpToOp(byte osOperation){
	int operation = DND.DROP_NONE;
	
	if ((osOperation & OS.XmDROP_COPY) == OS.XmDROP_COPY)
		operation |= DND.DROP_COPY;
	if ((osOperation & OS.XmDROP_MOVE) == OS.XmDROP_MOVE)
		operation |= DND.DROP_MOVE;
	if ((osOperation & OS.XmDROP_LINK) == OS.XmDROP_LINK)
		operation |= DND.DROP_LINK;
	
	return operation;
}
void registerDropTarget() {
	if (control == null || control.isDisposed() || registered) return;

	int[] args = new int[]{
		OS.XmNdropSiteOperations, opToOsOp(getStyle()),
		OS.XmNdropSiteActivity,   OS.XmDROP_SITE_ACTIVE,
		OS.XmNdropProc,           DropProc.getAddress(),
		OS.XmNdragProc,           DragProc.getAddress(),
		OS.XmNanimationStyle,     OS.XmDRAG_UNDER_NONE,
		OS.XmNdropSiteType,       OS.XmDROP_SITE_COMPOSITE,
	};
	
	if (transferAgents != null && transferAgents.length != 0) {
		TransferData[] transferData = new TransferData[0];
		for (int i = 0, length = transferAgents.length; i < length; i++){
			Transfer transfer = transferAgents[i];
			if (transfer != null) {
				TransferData[] data = transfer.getSupportedTypes();
				TransferData[] newTransferData = new TransferData[transferData.length +  data.length];
				System.arraycopy(transferData, 0, newTransferData, 0, transferData.length);
				System.arraycopy(data, 0, newTransferData, transferData.length, data.length);
				transferData = newTransferData;
			}
		}
		
		int[] atoms = new int[transferData.length];
		for (int i = 0, length = transferData.length; i < length; i++){
			atoms[i] = transferData[i].type;
		}
	
		// Copy import targets to global memory 
		int pImportTargets = OS.XtMalloc(atoms.length * 4);
		OS.memmove(pImportTargets, atoms, atoms.length * 4);
		
		int[] args2 = new int[]{
			OS.XmNimportTargets,      pImportTargets,
			OS.XmNnumImportTargets,   atoms.length
		};
		
		int[] newArgs = new int[args.length + args2.length];
		System.arraycopy(args, 0, newArgs, 0, args.length);
		System.arraycopy(args2, 0, newArgs, args.length, args2.length);
		args = newArgs;	
	}
	
	OS.XmDropSiteRegister(control.handle, args, args.length / 2);
	if (control instanceof Label) {
		int formHandle = OS.XtParent (control.handle);
		OS.XmDropSiteRegister(formHandle, args, args.length / 2);
	}
	registered = true;
}


/**
 * Removes the listener from the collection of listeners who will
 * be notified when a drag and drop operation is in progress.
 *
 * @param listener the listener which should no longer be notified
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
 * @see #getDropListeners
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
 * Specifies the drop effect for this DropTarget.  This drop effect will be 
 * used during a drag and drop to display the drag under effect on the 
 * target widget.
 *
 * @param effect the drop effect that is registered for this DropTarget
 * 
 * @since 3.3
 */
public void setDropTargetEffect(DropTargetEffect effect) {
	dropEffect = effect;
}

boolean setEventData(byte ops, byte op, int dragContext, short x, short y, int timestamp, DNDEvent event) {
	//get allowed operations
	int style = getStyle();
	int operations = osOpToOp(ops) & style;
	if (operations == DND.DROP_NONE) return false;
	
	//get current operation
	int operation = osOpToOp(op);
	int xDisplay = getDisplay().xDisplay;
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	int [] unused = new int [1];
	int[] mask_return = new int[1];
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, unused, unused, unused, unused, mask_return);
	int mask = mask_return[0];
	if ((mask & OS.ShiftMask) == 0 && (mask & OS.ControlMask) == 0) {
		operation = DND.DROP_DEFAULT;
	}
	if (operation == DND.DROP_DEFAULT) {
		if ((style & DND.DROP_DEFAULT) == 0) {
			operation = (operations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
		}
	} else {
		if ((operation & operations) == 0) operation = DND.DROP_NONE;
	}	
	//Get allowed transfer types
	int ppExportTargets = OS.XtMalloc(4);
	int pNumExportTargets = OS.XtMalloc(4);
	int[] args = new int[]{
		OS.XmNexportTargets,          ppExportTargets,
		OS.XmNnumExportTargets,       pNumExportTargets
	};

	OS.XtGetValues(dragContext, args, args.length / 2);
	int[] numExportTargets = new int[1];
	OS.memmove(numExportTargets, pNumExportTargets, 4);
	OS.XtFree(pNumExportTargets);
	int[] pExportTargets = new int[1];
	OS.memmove(pExportTargets, ppExportTargets, 4);
	OS.XtFree(ppExportTargets);
	int[] exportTargets = new int[numExportTargets[0]];
	OS.memmove(exportTargets, pExportTargets[0], 4*numExportTargets[0]);
	//?OS.XtFree(pExportTargets[0]);

	TransferData[] dataTypes = new TransferData[0];
	for (int i = 0; i < exportTargets.length; i++){
		for (int j = 0; j < transferAgents.length; j++){
			TransferData transferData = new TransferData();
			transferData.type = exportTargets[i];
			Transfer transfer = transferAgents[j];
			if (transfer != null && transfer.isSupportedType(transferData)) {
				TransferData[] newDataTypes = new TransferData[dataTypes.length + 1];
				System.arraycopy(dataTypes, 0, newDataTypes, 0, dataTypes.length);
				newDataTypes[dataTypes.length] = transferData;
				dataTypes = newDataTypes;
				break;
			}
		}
	}
	if (dataTypes.length == 0) return false;

	short [] root_x = new short [1];
	short [] root_y = new short [1];	
	OS.XtTranslateCoords (this.control.handle, x, y, root_x, root_y);
	
	event.widget = this;
	event.x = root_x[0];
	event.y = root_y[0];
	event.time = timestamp;
	event.feedback = DND.FEEDBACK_SELECT;
	event.dataTypes = dataTypes;
	event.dataType = dataTypes[0];
	event.operations = operations;
	event.detail = operation;
	if (dropEffect != null) {
		event.item = dropEffect.getItem(event.x, event.y);
	}
	return true;
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
	this.transferAgents = transferAgents;
	
	if (!control.isVisible()) return;
	
	// register data types
	TransferData[] transferData = new TransferData[0];
	for (int i = 0, length = transferAgents.length; i < length; i++){
		Transfer transfer = transferAgents[i];
		if (transfer != null) {
			TransferData[] data = transfer.getSupportedTypes();
			TransferData[] newTransferData = new TransferData[transferData.length +  data.length];
			System.arraycopy(transferData, 0, newTransferData, 0, transferData.length);
			System.arraycopy(data, 0, newTransferData, transferData.length, data.length);
			transferData = newTransferData;
		}
	}
	
	int[] atoms = new int[transferData.length];
	for (int i = 0, length = transferData.length; i < length; i++){
		atoms[i] = transferData[i].type;
	}

	// Copy import targets to global memory 
	int pImportTargets = OS.XtMalloc(atoms.length * 4);
	OS.memmove(pImportTargets, atoms, atoms.length * 4);

	int[] args = new int[]{
		OS.XmNimportTargets,      pImportTargets,
		OS.XmNnumImportTargets,   atoms.length
	};

	OS.XmDropSiteUpdate(control.handle, args, args.length / 2);
	if (control instanceof Label) {
		int formHandle = OS.XtParent (control.handle);
		OS.XmDropSiteUpdate(formHandle, args, args.length / 2);
	}
	
	OS.XtFree(pImportTargets);
	
}
void transferProcCallback(int widget, int client_data, int pSelection, int pType, int pValue, int pLength, int pFormat) {
	if (pType == 0 || pValue == 0 || pLength == 0 || pFormat == 0) return;
	
	int[] type = new int[1];
	OS.memmove(type, pType, 4);
	if (type[0] == nullAtom) {
		return;
	}
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(droppedEventData.operations, droppedEventData.operation, droppedEventData.dragContext, droppedEventData.x, droppedEventData.y, droppedEventData.timeStamp, event)){
		return;
	}
	
	int allowedOperations = event.operations;
	
	// Get Data in a Java format
	Object object = null;
	TransferData transferData = new TransferData();
	int[] length = new int[1];
	OS.memmove(length, pLength, 4);
	int[] format = new int[1];
	OS.memmove(format, pFormat, 4);
	transferData.type = type[0];
	transferData.length = length[0];
	transferData.pValue = pValue;
	transferData.format = format[0];
	for (int i = 0; i < transferAgents.length; i++){
		Transfer transfer = transferAgents[i];
		if (transfer != null && transfer.isSupportedType(transferData)){
			object = transfer.nativeToJava(transferData);
			break;
		}
	}
	OS.XtFree(pValue);
	
	if (object == null) {
		selectedOperation = DND.DROP_NONE;
	}
	
	event.detail = selectedOperation;
	event.dataType = transferData;
	event.data = object;
	notifyListeners(DND.Drop, event);
	selectedOperation = DND.DROP_NONE;
	if ((allowedOperations & event.detail) == event.detail) {
		selectedOperation = event.detail;
	}

	//workaround - restore original timeout
	int xtContext = OS.XtDisplayToApplicationContext (getDisplay().xDisplay);
	OS.XtAppSetSelectionTimeout (xtContext, selectionTimeout);
	
	//notify source of action taken
	if ((selectedOperation & DND.DROP_MOVE) == DND.DROP_MOVE) {
		int[] args = new int[]{control.handle, deleteAtom};
		OS.XmDropTransferAdd(dropTransferObject, args, args.length / 2);
	}
}

void unregisterDropTarget() {
	if (control == null || control.isDisposed() || !registered) return;
	OS.XmDropSiteUnregister(control.handle);
	if (control instanceof Label) {
		int formHandle = OS.XtParent(control.handle);
		OS.XmDropSiteUnregister(formHandle);
	}
	
	registered = false;
}

void updateDragOverHover(long delay, DNDEvent event) {
	if (delay == 0) {
		dragOverStart = 0;
		dragOverEvent = null;
		return;
	}
	dragOverStart = System.currentTimeMillis() + delay;
	if (dragOverEvent == null) dragOverEvent = new DNDEvent();
	dragOverEvent.x = event.x;
	dragOverEvent.y = event.y;
	TransferData[] dataTypes = new TransferData[ event.dataTypes.length];
	System.arraycopy( event.dataTypes, 0, dataTypes, 0, dataTypes.length);
	dragOverEvent.dataTypes  = dataTypes;
	dragOverEvent.operations = event.operations;
}
}
