package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

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
 *	<dt><b>Styles</b> <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK 
 *	<dt><b>Events</b> <dd>DND.DragEnter, DND.DragLeave, DND.DragOver, DND.DragOperationChanged, 
 *                        DND.Drop, DND.DropAccept
 * </dl>
 */ 
public class DropTarget extends Widget {
	
	private Callback dropProc;
	private Callback transferProc;
	private Callback dragProc;
	
	// info for registering as a droptarget	
	private Control control;
	private Listener controlListener;
	private Transfer[] transferAgents = new Transfer[0];
	
	// info about data being dragged over site
	private TransferData selectedDataType;
	private TransferData[] dataTypes;
	private int dropTransferObject;
	private XmDropProcCallback droppedEventData;
	
	private DragUnderEffect effect;
	
public DropTarget(Control control, int style) {

	super (control, checkStyle(style));
	
	this.control = control;
	
	dropProc = new Callback(this, "dropProcCallback", 3);
	if (dropProc == null)
		DND.error(DND.ERROR_CANNOT_INIT_DROP);

	dragProc = new Callback(this, "dragProcCallback", 3);
	if (dragProc == null)
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
		
	int[] args = new int[]{
		OS.XmNdropSiteOperations, opToOsOp(style),
		OS.XmNdropSiteActivity,   OS.XmDROP_SITE_ACTIVE,
		OS.XmNdropProc,           dropProc.getAddress(),
		OS.XmNdragProc,           dragProc.getAddress()
	};

	// the OS may have registered this widget as a drop site on creation.
	// Remove the registered drop site because it has preconfigured values which we do not want.
	//OS.XmDropSiteUnregister(control.handle);
	// Register drop site with our own values
	OS.XmDropSiteRegister(control.handle, args, args.length / 2);

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (!DropTarget.this.isDisposed()){
				DropTarget.this.dispose();
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	
	this.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			onDispose();
		}
	});
	
	if (control instanceof Tree) {
		effect = new TreeDragUnderEffect((Tree)control);
	} else if (control instanceof Table) {
		effect = new TableDragUnderEffect((Table)control);
	} else {
		effect = new NoDragUnderEffect(control);
	}
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
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
private int dragProcCallback(int widget, int client_data, int call_data) {

	XmDragProcCallback callbackData = new XmDragProcCallback();
	OS.memmove(callbackData, call_data, XmDragProcCallback.sizeof);

	if (callbackData.reason == OS.XmCR_DROP_SITE_ENTER_MESSAGE){
		releaseDropInfo();
		
		// get the export targets
		int ppExportTargets = OS.XtMalloc(4);
		int pNumExportTargets = OS.XtMalloc(4);
		int[] args = new int[]{
			OS.XmNexportTargets,          ppExportTargets,
			OS.XmNnumExportTargets,       pNumExportTargets
		};

		OS.XtGetValues(callbackData.dragContext, args, args.length / 2);
		int[] numExportTargets = new int[1];
		OS.memmove(numExportTargets, pNumExportTargets, 4);
		OS.XtFree(pNumExportTargets);
		int[] pExportTargets = new int[1];
		OS.memmove(pExportTargets, ppExportTargets, 4);
		OS.XtFree(ppExportTargets);
		int[] exportTargets = new int[numExportTargets[0]];
		OS.memmove(exportTargets, pExportTargets[0], 4*numExportTargets[0]);
		//?OS.XtFree(pExportTargets[0]);

		for (int i = 0, length = exportTargets.length; i < length; i++){
			for (int j = 0, length2 = transferAgents.length; j < length2; j++){
				TransferData transferData = new TransferData();
				transferData.type = exportTargets[i];
				if (transferAgents[j].isSupportedType(transferData)) {
					TransferData[] newDataTypes = new TransferData[dataTypes.length + 1];
					System.arraycopy(dataTypes, 0, newDataTypes, 0, dataTypes.length);
					newDataTypes[dataTypes.length] = transferData;
					dataTypes = newDataTypes;
					break;
				}
			}
		}
	}

	DNDEvent event = new DNDEvent();
	event.widget     = this.control;
	event.time       = callbackData.timeStamp;
	short [] root_x = new short [1];
	short [] root_y = new short [1];
	OS.XtTranslateCoords (this.control.handle, (short) callbackData.x, (short) callbackData.y, root_x, root_y);
	event.x          = root_x[0];
	event.y          = root_y[0];
	event.dataTypes  = dataTypes;
	event.feedback = DND.FEEDBACK_SELECT;
	event.operations = osOpToOp(callbackData.operations);
	event.dataType  = selectedDataType;
	event.detail  = osOpToOp(callbackData.operation);

	try {
		switch (callbackData.reason) {
			case OS.XmCR_DROP_SITE_ENTER_MESSAGE :
				if (dataTypes.length > 0) {
					event.dataType = dataTypes[0];
				}
				notifyListeners(DND.DragEnter, event);
				effect.show(event.feedback, event.x, event.y);
				break;
			
			case OS.XmCR_DROP_SITE_MOTION_MESSAGE :
				notifyListeners(DND.DragOver, event);
				effect.show(event.feedback, event.x, event.y);
				break;
			case OS.XmCR_OPERATION_CHANGED :
				notifyListeners(DND.DragOperationChanged, event);
				effect.show(event.feedback, event.x, event.y);
				break;
			case OS.XmCR_DROP_SITE_LEAVE_MESSAGE :
				event.detail  = DND.DROP_NONE;
				notifyListeners(DND.DragLeave, event);
				effect.show(DND.FEEDBACK_NONE, 0, 0);
				return 0;
		}
	} catch (Throwable err) {
		callbackData.dropSiteStatus = OS.XmDROP_SITE_INVALID;
		callbackData.operation = opToOsOp(DND.DROP_NONE);
		OS.memmove(call_data, callbackData, XmDragProcCallback.sizeof);
		return 0;
	}
	
	selectedDataType = null;
	for (int i = 0; i < dataTypes.length; i++) {
		if (dataTypes[i].equals(event.dataType)) {
			selectedDataType = event.dataType;
			break;
		}
	}
	int lastOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((event.detail & osOpToOp(callbackData.operations)) == event.detail)) {
		lastOperation = event.detail;
	}
	
	callbackData.dropSiteStatus = OS.XmDROP_SITE_VALID;
	callbackData.operation = opToOsOp(lastOperation);
	OS.memmove(call_data, callbackData, XmDragProcCallback.sizeof);

	return 0;
}
private int dropProcCallback(int widget, int client_data, int call_data) {

	effect.show(DND.FEEDBACK_NONE, 0, 0);
	
	droppedEventData = new XmDropProcCallback();
	OS.memmove(droppedEventData, call_data, XmDropProcCallback.sizeof);	
	
	DNDEvent event = new DNDEvent();
	event.widget     = this.control;
	event.time       = droppedEventData.timeStamp;
	short [] root_x = new short [1];
	short [] root_y = new short [1];
	OS.XtTranslateCoords (this.control.handle, (short) droppedEventData.x, (short) droppedEventData.y, root_x, root_y);
	event.x          = root_x[0];
	event.y          = root_y[0];
	event.dataTypes  = dataTypes;
	event.operations = osOpToOp(droppedEventData.operations);
	event.dataType   = selectedDataType;
	event.detail     = osOpToOp(droppedEventData.operation);

	try {
		notifyListeners(DND.DropAccept,event);
	} catch (Throwable err) {
		event.detail = DND.DROP_NONE;
		event.dataType = null;
	}
	
	selectedDataType = null;
	for (int i = 0; i < dataTypes.length; i++) {
		if (dataTypes[i].equals(event.dataType)) {
			selectedDataType = event.dataType;
			break;
		}
	}
	int lastOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((event.detail & osOpToOp(droppedEventData.operations)) == event.detail)) {
		lastOperation = event.detail;
	}
	
	if (lastOperation == DND.DROP_NONE) {
		// this was not a successful drop
		int[] args = new int[] {OS.XmNtransferStatus, OS.XmTRANSFER_FAILURE,
					OS.XmNnumDropTransfers, 0};
		dropTransferObject = OS.XmDropTransferStart(droppedEventData.dragContext, args, args.length / 2);
		return 0;
	}

	// ask drag source for dropped data
	int[] transferEntries = new int[2];
	transferEntries[0]  = 0;
	transferEntries[1] = selectedDataType.type;
		
	int pTransferEntries = OS.XtMalloc(transferEntries.length * 4);
	OS.memmove(pTransferEntries, transferEntries, transferEntries.length * 4);
	if (transferProc == null)
		transferProc = new Callback(this, "transferProcCallback", 7);

	if (transferProc != null){
		int[] args = new int[] {OS.XmNdropTransfers, pTransferEntries,
					OS.XmNnumDropTransfers, transferEntries.length / 2,
					OS.XmNtransferProc, transferProc.getAddress()};

		dropTransferObject = OS.XmDropTransferStart(droppedEventData.dragContext, args, args.length / 2);
		OS.XtFree(pTransferEntries);
	}

	return 0;
}
/**
 * Returns the Control which is registered for this DropTarget.  This is the control over which the 
 * user positions the cursor to drop the data.
 *
 * @return the Control which is registered for this DropTarget
 *
 */
public Control getControl () {
	return control;
}
public Display getDisplay () {

	if (control == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	return control.getDisplay ();
}
/**
 * Returns the list of data types that can be transferred to this DropTarget.
 *
 * @return the list of data types that can be transferred to this DropTarget
 *
 */ 
public Transfer[] getTransfer(){
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
private void onDispose() {
	
	if (dropProc != null)
		dropProc.dispose();
	dropProc = null;

	if (transferProc != null)
		transferProc.dispose();
	transferProc = null;

	if (dragProc != null)
		dragProc.dispose();
	dragProc = null;
	//if (control != null && !control.isDisposed()){
	//	OS.XmDropSiteUnregister(control.handle);
	//}
	if (controlListener != null) {
		control.removeListener(SWT.Dispose, controlListener);
	}
	controlListener = null;
	control = null;
	transferAgents = null;
}
private byte opToOsOp(int operation){
	byte osOperation = OS.XmDROP_NOOP;
	
	if ((operation & DND.DROP_COPY) == DND.DROP_COPY)
		osOperation |= OS.XmDROP_COPY;
	if ((operation & DND.DROP_MOVE) == DND.DROP_MOVE)
		osOperation |= OS.XmDROP_MOVE;
	if ((operation & DND.DROP_LINK) == DND.DROP_LINK)
		osOperation |= OS.XmDROP_LINK;
	
	return osOperation;
}
private int osOpToOp(byte osOperation){
	int operation = DND.DROP_NONE;
	
	if ((osOperation & OS.XmDROP_COPY) == OS.XmDROP_COPY)
		operation |= DND.DROP_COPY;
	if ((osOperation & OS.XmDROP_MOVE) == OS.XmDROP_MOVE)
		operation |= DND.DROP_MOVE;
	if ((osOperation & OS.XmDROP_LINK) == OS.XmDROP_LINK)
		operation |= DND.DROP_LINK;
	
	return operation;
}
private void releaseDropInfo(){
	selectedDataType = null;
	dataTypes = new TransferData[0];
	droppedEventData = null;
	dropTransferObject = 0;
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
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
public void setTransfer(Transfer[] transferAgents){
	this.transferAgents = transferAgents;
			
	// register data types
	TransferData[] transferData = new TransferData[0];
	for (int i = 0, length = transferAgents.length; i < length; i++){
		TransferData[] data = transferAgents[i].getSupportedTypes();
		TransferData[] newTransferData = new TransferData[transferData.length +  data.length];
		System.arraycopy(transferData, 0, newTransferData, 0, transferData.length);
		System.arraycopy(data, 0, newTransferData, transferData.length, data.length);
		transferData = newTransferData;
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
	
	OS.XtFree(pImportTargets);
	
}
private int transferProcCallback(int widget, int client_data, int pSelection, int pType, int pValue, int pLength, int pFormat) {
	
	int[] type = new int[1];
	OS.memmove(type, pType, 4);

	// get dropped data object
	Transfer transferAgent = null;
	TransferData transferData = new TransferData();
	transferData.type = type[0];
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData)){
			transferAgent = transferAgents[i];
			break;
		}
	}
	if (transferAgent != null) {
		transferData.pValue = pValue;
		int[] length = new int[1];
		OS.memmove(length, pLength, 4);
		transferData.length = length[0];
		int[] format = new int[1];
		OS.memmove(format, pFormat, 4);
		transferData.format = format[0];
		Object data = transferAgent.nativeToJava(transferData);
		
		OS.XtFree(transferData.pValue); //?? Should we be freeing this, and what about the other memory?
	
		// notify listeners of drop
		DNDEvent event = new DNDEvent();
		event.widget     = this.control;
		event.time       = droppedEventData.timeStamp;
		short [] root_x = new short [1];
		short [] root_y = new short [1];
		OS.XtTranslateCoords (this.control.handle, (short) droppedEventData.x, (short) droppedEventData.y, root_x, root_y);
		event.x          = root_x[0];
		event.y          = root_y[0];
		event.dataTypes  = dataTypes;
		event.operations = osOpToOp(droppedEventData.operations);
		event.dataType = transferData;
		event.detail     = osOpToOp(droppedEventData.operation);
		event.data       = data;

		try {
			notifyListeners(DND.Drop,event);
		} catch (Throwable err) {
			event.detail = DND.DROP_NONE;
		}

		if ((event.detail & DND.DROP_MOVE) == DND.DROP_MOVE) {
			OS.XmDropTransferAdd(dropTransferObject, new int[]{0, Transfer.registerType("DELETE\0")}, 1);
		}
	}

	return 0;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
}
