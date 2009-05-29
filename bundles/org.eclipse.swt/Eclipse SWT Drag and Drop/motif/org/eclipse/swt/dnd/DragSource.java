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
 * <code>DragSource</code> defines the source object for a drag and drop transfer.
 *
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 *  
 * <p>A drag source is the object which originates a drag and drop operation. For the specified widget, 
 * it defines the type of data that is available for dragging and the set of operations that can 
 * be performed on that data.  The operations can be any bit-wise combination of DND.MOVE, DND.COPY or 
 * DND.LINK.  The type of data that can be transferred is specified by subclasses of Transfer such as 
 * TextTransfer or FileTransfer.  The type of data transferred can be a predefined system type or it 
 * can be a type defined by the application.  For instructions on how to define your own transfer type,
 * refer to <code>ByteArrayTransfer</code>.</p>
 *
 * <p>You may have several DragSources in an application but you can only have one DragSource 
 * per Control.  Data dragged from this DragSource can be dropped on a site within this application 
 * or it can be dropped on another application such as an external Text editor.</p>
 * 
 * <p>The application supplies the content of the data being transferred by implementing the
 * <code>DragSourceListener</code> and associating it with the DragSource via DragSource#addDragListener.</p>
 * 
 * <p>When a successful move operation occurs, the application is required to take the appropriate 
 * action to remove the data from its display and remove any associated operating system resources or
 * internal references.  Typically in a move operation, the drop target makes a copy of the data 
 * and the drag source deletes the original.  However, sometimes copying the data can take a long 
 * time (such as copying a large file).  Therefore, on some platforms, the drop target may actually 
 * move the data in the operating system rather than make a copy.  This is usually only done in 
 * file transfers.  In this case, the drag source is informed in the DragEnd event that a
 * DROP_TARGET_MOVE was performed.  It is the responsibility of the drag source at this point to clean 
 * up its displayed information.  No action needs to be taken on the operating system resources.</p>
 *
 * <p> The following example shows a Label widget that allows text to be dragged from it.</p>
 * 
 * <code><pre>
 *	// Enable a label as a Drag Source
 *	Label label = new Label(shell, SWT.NONE);
 *	// This example will allow text to be dragged
 *	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
 *	// This example will allow the text to be copied or moved to the drop target
 *	int operations = DND.DROP_MOVE | DND.DROP_COPY;
 *	
 *	DragSource source = new DragSource(label, operations);
 *	source.setTransfer(types);
 *	source.addDragListener(new DragSourceListener() {
 *		public void dragStart(DragSourceEvent e) {
 *			// Only start the drag if there is actually text in the
 *			// label - this text will be what is dropped on the target.
 *			if (label.getText().length() == 0) {
 *				event.doit = false;
 *			}
 *		};
 *		public void dragSetData(DragSourceEvent event) {
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
 *	<dt><b>Styles</b></dt> <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK</dd>
 *	<dt><b>Events</b></dt> <dd>DND.DragStart, DND.DragSetData, DND.DragEnd</dd>
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#dnd">Drag and Drop snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: DNDExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DragSource extends Widget {

	// info for registering as a drag source
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DragSourceEffect dragEffect;

	static final String DEFAULT_DRAG_SOURCE_EFFECT = "DEFAULT_DRAG_SOURCE_EFFECT"; //$NON-NLS-1$
		
	static Callback ConvertProc;
	static Callback DragDropFinish;
	static Callback DropFinish;
	static {
		ConvertProc = new Callback(DragSource.class, "ConvertProcCallback", 10); //$NON-NLS-1$
		if (ConvertProc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		DragDropFinish = new Callback(DragSource.class, "DragDropFinishCallback", 3); //$NON-NLS-1$
		if (DragDropFinish.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		DropFinish = new Callback(DragSource.class, "DropFinishCallback", 3); //$NON-NLS-1$
		if (DropFinish.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}
	boolean moveRequested;

	int dragContext;

/**
 * Creates a new <code>DragSource</code> to handle dragging from the specified <code>Control</code>.
 * Creating an instance of a DragSource may cause system resources to be allocated depending on the platform.  
 * It is therefore mandatory that the DragSource instance be disposed when no longer required.
 *
 * @param control the <code>Control</code> that the user clicks on to initiate the drag
 * @param style the bitwise OR'ing of allowed operations; this may be a combination of any of 
 *					DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_INIT_DRAG - unable to initiate drag source; this will occur if more than one
 *        drag source is created for a control or if the operating system will not allow the creation
 *        of the drag source</li>
 * </ul>
 * 
 * <p>NOTE: ERROR_CANNOT_INIT_DRAG should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatibility.</p>
 * 
 * @see Widget#dispose
 * @see DragSource#checkSubclass
 * @see DND#DROP_NONE
 * @see DND#DROP_COPY
 * @see DND#DROP_MOVE
 * @see DND#DROP_LINK
 */
public DragSource(Control control, int style) {
	super (control, checkStyle(style));
	if (ConvertProc == null || DragDropFinish == null || DropFinish == null)
		DND.error(DND.ERROR_CANNOT_INIT_DRAG);
	this.control = control;
	if (control.getData(DND.DRAG_SOURCE_KEY) != null)
		DND.error(DND.ERROR_CANNOT_INIT_DRAG);
	control.setData(DND.DRAG_SOURCE_KEY, this);

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (event.type == SWT.Dispose) {
				if (!DragSource.this.isDisposed()) {
					DragSource.this.dispose();
				}
			}
			if (event.type == SWT.DragDetect) {
				if (!DragSource.this.isDisposed()) {
					DragSource.this.drag(event);
				}
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	control.addListener (SWT.DragDetect, controlListener);
	
	this.addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event e) {
			onDispose();
		}
	});
	
	Object effect = control.getData(DEFAULT_DRAG_SOURCE_EFFECT);
	if (effect instanceof DragSourceEffect) {
		dragEffect = (DragSourceEffect) effect;
	} else if (control instanceof Tree) {
		dragEffect = new TreeDragSourceEffect((Tree) control);
	} else if (control instanceof Table) {
		dragEffect = new TableDragSourceEffect((Table) control);
	}
}
static DragSource FindDragSource(int handle) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	return (DragSource)display.getData(Integer.toString(handle));
}
static int ConvertProcCallback(int widget, int pSelection, int pTarget, int pType_return, int ppValue_return, int pLength_return, int pFormat_return, int max_length, int client_data, int request_id) {
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	return source.convertProcCallback(widget, pSelection, pTarget, pType_return, ppValue_return, pLength_return, pFormat_return, max_length, client_data, request_id);
}
static int DragDropFinishCallback(int widget, int client_data, int call_data) {
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	return source.dragDropFinishCallback(widget, client_data, call_data);
}
static int DropFinishCallback(int widget, int client_data, int call_data) {
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	return source.dropFinishCallback(widget, client_data, call_data);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when a drag and drop operation is in progress, by sending
 * it one of the messages defined in the <code>DragSourceListener</code>
 * interface.
 * 
 * <p><ul>
 * <li><code>dragStart</code> is called when the user has begun the actions required to drag the widget. 
 * This event gives the application the chance to decide if a drag should be started.
 * <li><code>dragSetData</code> is called when the data is required from the drag source.
 * <li><code>dragFinished</code> is called when the drop has successfully completed (mouse up 
 * over a valid target) or has been terminated (such as hitting the ESC key). Perform cleanup 
 * such as removing data from the source side on a successful move operation.
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
 * @see DragSourceListener
 * @see #getDragListeners
 * @see #removeDragListener
 * @see DragSourceEvent
 */
public void addDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener (listener);
	typedListener.dndWidget = this;
	addListener (DND.DragStart, typedListener);
	addListener (DND.DragSetData, typedListener);
	addListener (DND.DragEnd, typedListener);
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

int convertProcCallback(int widget, int pSelection, int pTarget, int pType_return, int ppValue_return, int pLength_return, int pFormat_return, int max_length, int client_data, int request_id) {
	if (pSelection == 0 ) return 0;
		
	// is this a drop?
	int[] selection = new int[1];
	OS.memmove(selection, pSelection, 4);
	int xDisplay = getDisplay().xDisplay;
	byte[] bName = Converter.wcsToMbcs (null, "_MOTIF_DROP", true); //$NON-NLS-1$
	int motifDropAtom = OS.XmInternAtom (xDisplay, bName, true);
	if (selection[0] != motifDropAtom) return 0;

	// get the target value from pTarget
	int[] target = new int[1];
	OS.memmove(target, pTarget, 4);

	//  handle the "Move" case
	bName = Converter.wcsToMbcs (null, "DELETE", true); // DELETE corresponds to a Move request //$NON-NLS-1$
	int deleteAtom = OS.XmInternAtom (xDisplay, bName, true);
	if (target[0] == deleteAtom) { 
		moveRequested = true;
		bName = Converter.wcsToMbcs (null, "NULL", true); //$NON-NLS-1$
		int nullAtom = OS.XmInternAtom (xDisplay, bName, true);
		OS.memmove(pType_return,new int[]{nullAtom}, 4);
		OS.memmove(ppValue_return, new int[]{0}, 4);
		OS.memmove(pLength_return, new int[]{0}, 4);
		OS.memmove(pFormat_return, new int[]{8}, 4);
		return 1;
	}
		
	// do we support the requested data type?
	boolean dataMatch = false;
	TransferData transferData = new TransferData();
	transferData.type = target[0];
	for (int i = 0; i < transferAgents.length; i++){
		Transfer transfer = transferAgents[i];
		if (transfer != null && transfer.isSupportedType(transferData)){
			dataMatch = true;
			break;
		}
	}
	if (!dataMatch) return 0;
	
	DNDEvent event = new DNDEvent();
	event.widget = control;
	//event.time = ??;
	event.dataType = transferData;
	notifyListeners(DND.DragSetData,event);

	if (!event.doit) return 0;
	Transfer transferAgent = null;
	for (int i = 0; i < transferAgents.length; i++){
		Transfer transfer = transferAgents[i];
		if (transfer != null && transfer.isSupportedType(transferData)){
			transferAgent = transfer;
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
	} 
	OS.memmove(ppValue_return, new int[]{0}, 4);
	OS.memmove(pLength_return, new int[]{0}, 4);
	OS.memmove(pFormat_return, new int[]{8}, 4);
	return 0;
}
void drag(Event dragEvent) {
	moveRequested = false;
	// Current event must be a Button Press event
	Display display = control.getDisplay ();
	XButtonEvent xEvent = new XButtonEvent();
	OS.memmove(xEvent, display.xEvent, XButtonEvent.sizeof);
	if (xEvent.type != OS.ButtonPress) return;

	DNDEvent event = new DNDEvent();
	event.widget = this;	
	event.x = dragEvent.x;
	event.y = dragEvent.y;
	event.time = xEvent.time;
	event.doit = true;
	notifyListeners(DND.DragStart, event);
	if (!event.doit || transferAgents == null || transferAgents.length == 0) { 
		int time = xEvent.time;
		int dc = OS.XmGetDragContext(control.handle, time);
		if (dc != 0){
			OS.XmDragCancel(dc);
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
		Transfer transfer = transferAgents[i];
		if (transfer != null) {
			TransferData[] data = transfer.getSupportedTypes();
			TransferData[] newTransferData = new TransferData[transferData.length + data.length];
			System.arraycopy(transferData, 0, newTransferData, 0, transferData.length);
			System.arraycopy(data, 0, newTransferData, transferData.length, data.length);
			transferData = newTransferData;
		}
	}
	int[] dataTypes = new int[transferData.length];
	for (int i = 0; i < transferData.length; i++){
		dataTypes[i] = transferData[i].type;
	}
	int pExportTargets = OS.XtMalloc(dataTypes.length * 4);
	OS.memmove(pExportTargets, dataTypes, dataTypes.length * 4);

	int[] args = new int[]{
		OS.XmNexportTargets,		pExportTargets,
		OS.XmNnumExportTargets,		dataTypes.length,
		OS.XmNdragOperations,		opToOsOp(getStyle()),
		OS.XmNconvertProc,			ConvertProc.getAddress(),
		OS.XmNoperationCursorIcon,	0,
		OS.XmNsourceCursorIcon,		0,
		OS.XmNstateCursorIcon,		0,
		OS.XmNclientData,			0,
		OS.XmNblendModel,			OS.XmBLEND_ALL
	};	

	// look for existing drag contexts
	int time = xEvent.time;
	dragContext = OS.XmGetDragContext(control.handle, time);
	if (dragContext != 0){
		OS.XtSetValues(dragContext, args, args.length /2);
	} else {
		dragContext = OS.XmDragStart(this.control.handle, display.xEvent, args, args.length / 2);
	}
	OS.XtFree(pExportTargets);
	if (dragContext == 0) return;

	// register a call back to clean up when drop is done (optional)
	OS.XtAddCallback(dragContext, OS.XmNdragDropFinishCallback, DragDropFinish.getAddress(), 0);

	// register a call back to tell user what happened (optional)
	OS.XtAddCallback(dragContext, OS.XmNdropFinishCallback, DropFinish.getAddress(), 0);
	
	display.setData(Integer.toString(dragContext), this);
	return;
}

int dragDropFinishCallback(int widget, int client_data, int call_data) {
	
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
	getDisplay().setData(Integer.toString(dragContext), null);
	return 0;
}
int dropFinishCallback(int widget, int client_data, int call_data) {
	XmDropFinishCallbackStruct data = new XmDropFinishCallbackStruct();
	OS.memmove(data, call_data, XmDropFinishCallbackStruct.sizeof);
	if (data.dropAction != OS.XmDROP || data.dropSiteStatus != OS.XmDROP_SITE_VALID) {
		DNDEvent event = new DNDEvent();
		event.widget = this.control;
		event.time = data.timeStamp;
		event.detail = DND.DROP_NONE;
		event.doit = false;
		notifyListeners(DND.DragEnd,event);
		return 0;
	} 
	DNDEvent event = new DNDEvent();
	event.widget = this.control;
	event.time = data.timeStamp;
	if (moveRequested) {
		event.detail = DND.DROP_MOVE;
	} else {
		if (data.operation == OS.XmDROP_MOVE) {
			event.detail = DND.DROP_NONE;
		} else {
			event.detail = osOpToOp(data.operation);
		}
	}
	event.doit = (data.completionStatus != 0);
	notifyListeners(DND.DragEnd,event);
	moveRequested = false;
	return 0;
}
/**
 * Returns the Control which is registered for this DragSource.  This is the control that the 
 * user clicks in to initiate dragging.
 *
 * @return the Control which is registered for this DragSource
 */
public Control getControl () {
	return control;
}
/**
 * Returns an array of listeners who will be notified when a drag and drop 
 * operation is in progress, by sending it one of the messages defined in 
 * the <code>DragSourceListener</code> interface.
 *
 * @return the listeners who will be notified when a drag and drop
 * operation is in progress
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DragSourceListener
 * @see #addDragListener
 * @see #removeDragListener
 * @see DragSourceEvent
 * 
 * @since 3.4
 */
public DragSourceListener[] getDragListeners() {
	Listener[] listeners = getListeners(DND.DragStart);
	int length = listeners.length;
	DragSourceListener[] dragListeners = new DragSourceListener[length];
	int count = 0;
	for (int i = 0; i < length; i++) {
		Listener listener = listeners[i];
		if (listener instanceof DNDListener) {
			dragListeners[count] = (DragSourceListener) ((DNDListener) listener).getEventListener();
			count++;
		}
	}
	if (count == length) return dragListeners;
	DragSourceListener[] result = new DragSourceListener[count];
	System.arraycopy(dragListeners, 0, result, 0, count);
	return result;
}
/**
 * Returns the drag effect that is registered for this DragSource.  This drag
 * effect will be used during a drag and drop operation.
 *
 * @return the drag effect that is registered for this DragSource
 * 
 * @since 3.3
 */
public DragSourceEffect getDragSourceEffect() {
	return dragEffect;
}
/**
 * Returns the list of data types that can be transferred by this DragSource.
 *
 * @return the list of data types that can be transferred by this DragSource
 */
public Transfer[] getTransfer(){
	return transferAgents;
}

void onDispose() {
	if (control == null)
		return;
	if (controlListener != null) {
		control.removeListener(SWT.Dispose, controlListener);
		control.removeListener(SWT.DragDetect, controlListener);
	}
	controlListener = null;
	control.setData(DND.DRAG_SOURCE_KEY, null);
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
 * @see DragSourceListener
 * @see #addDragListener
 * @see #getDragListeners
 */
public void removeDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (DND.DragStart, listener);
	removeListener (DND.DragSetData, listener);
	removeListener (DND.DragEnd, listener);
}
/**
 * Specifies the drag effect for this DragSource.  This drag effect will be 
 * used during a drag and drop operation.
 *
 * @param effect the drag effect that is registered for this DragSource
 * 
 * @since 3.3
 */
public void setDragSourceEffect(DragSourceEffect effect) {
	dragEffect = effect;
}
/**
 * Specifies the list of data types that can be transferred by this DragSource.
 * The application must be able to provide data to match each of these types when
 * a successful drop has occurred.
 * 
 * @param transferAgents a list of Transfer objects which define the types of data that can be
 * dragged from this source
 */
public void setTransfer(Transfer[] transferAgents){
	this.transferAgents = transferAgents;
}

}
