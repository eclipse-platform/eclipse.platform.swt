package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/**
 *
 * Class <code>DragSource</code> defines the source object for a drag and drop transfer.
 *
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 *
 * <p>This class defines the following items:<ul>
 *   <li>the <code>Control</code> that the user clicks on to intiate a drag;
 *   <li>the data that will be transferred on a successful drop; 
 *   <li>and the modes (move, copy, link) of transfer that are allowed.
 * </ul></p>
 *
 * <p>You may have several DragSources in an application but you can only have one DragSource 
 * per Control.  Data dragged from this DragSource can be dropped on a site within this application 
 * but it can also be dropped on another application such as an external Text editor.</p>
 * 
 * <p>The application supplies the content of the data being transferred by implementing the interface
 * <code>DragSourceListener</code> which uses the class <code>DragSourceEvent</code>.  
 * The application is required to take the appropriate action to remove the data from the drag source
 * when a successful move operation occurs.</p>
 *
 * <code><pre>
 *	// Enable a label as a Drag Source
 *	Label label = new Label(shell, SWT.NONE);
 *	// This example will allow text to be dragged
 *	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
 *	// This example will allow the text to be copied or moved to the drop target
 *	int operations = DND.DROP_MOVE | DND.DROP_COPY;
 *	
 *	DragSource source = new DragSource (label, operations);
 *	source.setTransfer(types);
 *	source.addDragListener (new DragSourceListener() {
 *		public void dragStart(DragSourceEvent e) {
 *			// Only start the drag if there is actually text in the
 *			// label - this text will be what is dropped on the target.
 *			if (label.getText().length() == 0) {
 *				event.doit = false;
 *			}
 *		};
 *		public void dragSetData (DragSourceEvent event) {
 *			// A drop has been performed, so provide the data of the 
 *			// requested type.
 *			// (Checking the type of the requested data is only 
 *			// necessary if the drag source supports more than 
 *			// one data type but is shown here as an example).
 *			if (TextTransfer.getInstance().isSupportedType(event.dataType)){
 *				event.data = label.getText();
 *			}
 *		}
 *		public void dragFinished(DragSourceEvent event) {
 *			// A Move operation has been performed so remove the data
 *			// from the source
 *			if (event.detail == DND.DROP_MOVE)
 *				label.setText("");
 *		}
 *	});
 * </pre></code>
 *
 *
 * <dl>
 *	<dt><b>Styles</b> <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK 
 *	<dt><b>Events</b> <dd>DND.DragEnd, DND.DragSetData
 * </dl>
 */ 
public class DragSource extends Widget {

	private Callback convertProc;
	private Callback dragDropFinish;
	private Callback dropFinish;

	// info for registering as a drag source
	private Control control;
	private Listener controlListener;
	private Transfer[] transferAgents = new Transfer[0];

	private boolean myDrag;

	int dragContext;

public DragSource(Control control, int style) {
	super (control, checkStyle(style));
	
	this.control = control;

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (event.type == SWT.Dispose) {
				if (!DragSource.this.isDisposed()){
					DragSource.this.dispose();
				}
			}
			if (event.type == SWT.DragDetect){
				DragSource.this.drag();
			}
			
		}
	};
	this.control.addListener (SWT.Dispose, controlListener);
	this.control.addListener (SWT.DragDetect, controlListener);
	
	this.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			onDispose();
		}
	});
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
public void addDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener (listener);
	addListener (DND.DragStart, typedListener);
	addListener (DND.DragSetData, typedListener);
	addListener (DND.DragEnd, typedListener);
}
static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}
private int convertProcCallback(int widget, int pSelection, int pTarget, int pType_return, int ppValue_return, int pLength_return, int pFormat_return, int max_length, int client_data, int request_id) {
	/*
		pSelection - atom specifying which property is being used to transfer the selection (only support _MOTIF_DROP)
		pTarget    -  atom specifying the type in which the requestor wants the information
		pType_return   - [out] type atom that data has been converted to (usually the same as pTarget)
		ppValue_return  - [out] set to a pointer to a block of memory
		pLength_return - [out] number of elements in the array
		pFormat_return - [out] size in bits of each element in the array
		
	*/

	if (pSelection == 0 )
		return 0;
		
	// is this a drop?
	int[] selection = new int[1];
	OS.memmove(selection, pSelection, 4);
	int motifDropAtom = Transfer.registerType("_MOTIF_DROP\0");
	if (selection[0] != motifDropAtom) return 0;

	// get the target value from pTarget
	int[] target = new int[1];
	OS.memmove(target, pTarget, 4);

	//  handle the "Move" case
	if (target[0] == Transfer.registerType("DELETE\0")) { // DELETE corresponds to a Move request
		OS.memmove(pType_return,new int[]{Transfer.registerType("NULL\0")}, 4);
		OS.memmove(ppValue_return, new int[]{0}, 4);
		OS.memmove(pLength_return, new int[]{0}, 4);
		OS.memmove(pFormat_return, new int[]{8}, 4);
		return 1;
	}
		
	// do we support the requested data type?
	boolean dataMatch = true;
	TransferData transferData = new TransferData();
	transferData.type = target[0];
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData)){
			dataMatch = true;
			break;
		}
	}
	if (!dataMatch) return 0;
	
	DNDEvent event = new DNDEvent();
	event.widget = control;
	//event.time = ??;
	event.dataType = transferData;
	try {
		notifyListeners(DND.DragSetData,event);
	} catch (Throwable err) {
		return 0;
	}
	
	if (event.data == null) return 0;

	Transfer transferAgent = null;
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData)){
			transferAgent = transferAgents[i];
			break;
		}
	}
	if (transferAgent == null) return 0;
	
	transferAgent.javaToNative(event.data, transferData);
	if (transferData.result == 1){
		OS.memmove(ppValue_return, new int[]{transferData.pValue}, 4);
		OS.memmove(pType_return, new int[]{transferData.type}, 4);
		OS.memmove(pLength_return, new int[]{transferData.length}, 4);
		OS.memmove(pFormat_return, new int[]{transferData.format}, 4);
		return 1;
	} else {
		OS.memmove(ppValue_return, new int[]{0}, 4);
		OS.memmove(pLength_return, new int[]{0}, 4);
		OS.memmove(pFormat_return, new int[]{8}, 4);
		return 0;
	}
}
private void drag() {
	if (transferAgents == null)
		return;
		
	// Current event must be a Button Press event
	Display display = control.getDisplay ();
	if (display.xEvent.type != OS.ButtonPress) return;

	DNDEvent event = new DNDEvent();
	event.widget = this;	
	event.time = display.xEvent.pad2;
	event.doit = true;
	
	try {
		notifyListeners(DND.DragStart, event);
	} catch (Throwable e) {
		event.doit = false;
	}

	if (!event.doit) { 
		int time = display.xEvent.pad2; // corresponds to time field in XButtonEvent
		int[] args = new int[]{	OS.XmNdragOperations,	OS.XmDROP_NOOP};	
		int dc = OS.XmGetDragContext(control.handle, time);
		if (dc != 0){
			OS.XtSetValues(dc, args, args.length /2);
		} else {			
			dc = OS.XmDragStart(this.control.handle, display.xEvent, args, args.length/2);
		}
		return;
	}

	// set the protocol (optional)
	// not implemented
	
	// create pixmaps for icons (optional)
	// not implemented

	// Copy targets to global memory
	TransferData[] transferData = new TransferData[0];
	for (int i = 0; i < transferAgents.length; i++){
		TransferData[] data = transferAgents[i].getSupportedTypes();
		TransferData[] newTransferData = new TransferData[transferData.length + data.length];
		System.arraycopy(transferData, 0, newTransferData, 0, transferData.length);
		System.arraycopy(data, 0, newTransferData, transferData.length, data.length);
		transferData = newTransferData;
	}
	int[] dataTypes = new int[transferData.length];
	for (int i = 0; i < transferData.length; i++){
		dataTypes[i] = transferData[i].type;
	}
	int pExportTargets = OS.XtMalloc(dataTypes.length * 4);
	OS.memmove(pExportTargets, dataTypes, dataTypes.length * 4);

	if (convertProc == null)
		convertProc = new Callback(this, "convertProcCallback", 10);
	if (convertProc == null) return;

	int[] args = new int[]{
		OS.XmNexportTargets,		pExportTargets,
		OS.XmNnumExportTargets,		dataTypes.length,
		OS.XmNdragOperations,		opToOsOp(getStyle()),
		OS.XmNconvertProc,			convertProc.getAddress(),
		OS.XmNoperationCursorIcon,	0,
		OS.XmNsourceCursorIcon,		0,
		OS.XmNstateCursorIcon,		0,
		OS.XmNclientData,			0,
		OS.XmNblendModel,			OS.XmBLEND_ALL
	};	

	// look for existing drag contexts
	int time = display.xEvent.pad2; // corresponds to time field in XButtonEvent
	dragContext = OS.XmGetDragContext(control.handle, time);
	if (dragContext != 0){
		OS.XtSetValues(dragContext, args, args.length /2);
	} else {
		dragContext = OS.XmDragStart(this.control.handle, display.xEvent, args, args.length / 2);
		myDrag = true;
	}
	OS.XtFree(pExportTargets);
	if (dragContext == 0) return;

	// register a call back to clean up when drop is done (optional)
	if (dragDropFinish == null)
		dragDropFinish = new Callback(this, "dragDropFinishCallback", 3);
	OS.XtAddCallback(dragContext, OS.XmNdragDropFinishCallback, dragDropFinish.getAddress(), 0);

	// register a call back to tell user what happened (optional)
	if (dropFinish == null)
		dropFinish = new Callback(this, "dropFinishCallback", 3);
	OS.XtAddCallback(dragContext, OS.XmNdropFinishCallback, dropFinish.getAddress(), 0);
	return;
}

private int dragDropFinishCallback(int widget, int client_data, int call_data) {
	
	// uncomment the following code when we allow users to specify their own source icons
	// release the pre set source icon 
	//int pSourceIcon = OS.XtMalloc(4);
	//int[] args = new int[]{OS.XmNsourceCursorIcon, pSourceIcon};
	//OS.XtGetValues(control.handle, args, args.length / 2);
	//int[] sourceIcon = new int[1];
	//OS.memmove(sourceIcon, pSourceIcon, 4);
	//if (sourceIcon[0] != 0)
		//OS.XtDestroyWidget(sourceIcon[0]);
	//OS.XtFree(pSourceIcon);

	dragContext = 0;

	if (convertProc != null)
		convertProc.dispose();
	convertProc = null;

	if (dragDropFinish !=  null)
		dragDropFinish.dispose();
	dragDropFinish = null;

	if (dropFinish !=  null)
		dropFinish.dispose();
	dropFinish = null;

	return 0;
}
private int dropFinishCallback(int widget, int client_data, int call_data) {
	
	XmDropFinishCallback data = new XmDropFinishCallback();
	OS.memmove(data, call_data, XmDropFinishCallback.sizeof);

	if (data.dropAction != OS.XmDROP || data.dropSiteStatus != OS.XmDROP_SITE_VALID) return 0;

	DNDEvent event = new DNDEvent();
	event.widget = this.control;
	event.time = data.timeStamp;
	event.detail = osOpToOp(data.operation);
	event.doit = (data.completionStatus != 0);

	try {
		notifyListeners(DND.DragEnd,event);
	} catch (Throwable err) {
	}
	
	return 0;
}
public Control getControl () {
	return control;
}
/**
* Gets the Display.
*/
public Display getDisplay () {

	if (control == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	return control.getDisplay ();
}
public Transfer[] getTransfer(){
	return transferAgents;
}
private void onDispose() {

	// Check if there is a drag in progress and cancel it
	//if (dragContext != 0 && myDrag)
	//	OS.XmDragCancel(dragContext);
		
	if (convertProc != null)
		convertProc.dispose();
	convertProc = null;

	if (dragDropFinish !=  null)
		dragDropFinish.dispose();
	dragDropFinish = null;

	if (dropFinish !=  null)
		dropFinish.dispose();
	dropFinish = null;
	
	if (control != null && controlListener != null) {
		control.removeListener(SWT.Dispose, controlListener);
		control.removeListener(SWT.DragDetect, controlListener);
	}
	control = null;
	controlListener = null;
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
/**	 
* Removes the listener.
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
public void removeDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (DND.DragStart, listener);
	removeListener (DND.DragSetData, listener);
	removeListener (DND.DragEnd, listener);
}
public void setTransfer(Transfer[] transferAgents){
	this.transferAgents = transferAgents;
}


protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
}
