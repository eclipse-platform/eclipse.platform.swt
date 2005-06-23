/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

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
public class DropTarget extends Widget {
	
	private Control control;
	private Listener controlListener;
	private Transfer[] transferAgents = new Transfer[0];
	private DragUnderEffect effect;
	
	// Track application selections
	private TransferData selectedDataType;
	private int selectedOperation;
	
	// workaround - There is no event for "operation changed" so track operation based on key state
	private int keyOperation = -1;
	
	// workaround - The dataobject address is only passed as an argument in drag enter and drop.  
	// To allow applications to query the data values during the drag over operations, 
	// maintain a reference to it.
	private int iDataObject;
	
	// interfaces
	private COMObject iDropTarget;
	private int refCount;
	
	private static final String DROPTARGETID = "DropTarget"; //$NON-NLS-1$

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
 * recoverable error, but can not be changed due to backward compatability.</p>
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
	if (control.getData(DROPTARGETID) != null) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}
	control.setData(DROPTARGETID, this);
	createCOMInterfaces();
	this.AddRef();
	
	if (COM.CoLockObjectExternal(iDropTarget.getAddress(), true, true) != COM.S_OK)
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	if (COM.RegisterDragDrop( control.handle, iDropTarget.getAddress()) != COM.S_OK)
		DND.error(DND.ERROR_CANNOT_INIT_DROP);

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (!DropTarget.this.isDisposed()){
				DropTarget.this.dispose();
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	
	this.addListener(SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			onDispose();
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
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
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

private int AddRef() {
	refCount++;
	return refCount;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

private void createCOMInterfaces() {
	// register each of the interfaces that this object implements
	iDropTarget = new COMObject(new int[]{2, 0, 0, 5, 4, 0, 5}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return DragEnter(args[0], args[1], args[2], args[3], args[4]);}
		public int method4(int[] args) {return DragOver(args[0], args[1], args[2], args[3]);}
		public int method5(int[] args) {return DragLeave();}
		public int method6(int[] args) {return Drop(args[0], args[1], args[2], args[3], args[4]);}
	};

}

private void disposeCOMInterfaces() {
	if (iDropTarget != null)
		iDropTarget.dispose();
	iDropTarget = null;
}

private int DragEnter(int pDataObject, int grfKeyState, int pt_x, int pt_y, int pdwEffect) {
	selectedDataType = null;
	selectedOperation = DND.DROP_NONE;
	iDataObject = 0;
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(event, pDataObject, grfKeyState, pt_x, pt_y, pdwEffect)) {
		OS.MoveMemory(pdwEffect, new int[] {COM.DROPEFFECT_NONE}, 4);
		return COM.S_OK;
	}	

	// Remember the iDataObject because it is not passed into the DragOver callback
	iDataObject = pDataObject;
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	notifyListeners(DND.DragEnter,event);
	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
	}
	
	selectedDataType = null;
	for (int i = 0; i < allowedDataTypes.length; i++) {
		if (TransferData.sameType(allowedDataTypes[i], event.dataType)){
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}
	
	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((allowedOperations & event.detail) != 0)) {
		selectedOperation = event.detail;
	}

	effect.show(event.feedback, event.x, event.y);
	
	OS.MoveMemory(pdwEffect, new int[] {opToOs(selectedOperation)}, 4);
	return COM.S_OK;
}



private int DragLeave() {
	effect.show(DND.FEEDBACK_NONE, 0, 0);
	keyOperation = -1;
	
	if (iDataObject == 0) return COM.S_OK;

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = OS.GetMessageTime();
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	return COM.S_OK;
}

private int DragOver(int grfKeyState, int pt_x,	int pt_y, int pdwEffect) {
	int oldKeyOperation = keyOperation;
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(event, iDataObject, grfKeyState, pt_x, pt_y, pdwEffect)) {
		keyOperation = -1;
		OS.MoveMemory(pdwEffect, new int[] {COM.DROPEFFECT_NONE}, 4);
		return COM.S_OK;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);

	if (keyOperation == oldKeyOperation) {
		event.type = DND.DragOver;
		event.dataType = selectedDataType;
		event.detail = selectedOperation;
	} else {
		event.type = DND.DragOperationChanged;
		event.dataType = selectedDataType;
	}
	notifyListeners(event.type, event);
	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
	}
	
	selectedDataType = null;
	for (int i = 0; i < allowedDataTypes.length; i++) {
		if (TransferData.sameType(allowedDataTypes[i], event.dataType)){
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}

	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((allowedOperations & event.detail) == event.detail)) {
		selectedOperation = event.detail;
	}
	
	effect.show(event.feedback, event.x, event.y);
	
	OS.MoveMemory(pdwEffect, new int[] {opToOs(selectedOperation)}, 4);
	return COM.S_OK;
}

private int Drop(int pDataObject, int grfKeyState, int pt_x, int pt_y, int pdwEffect) {
	effect.show(DND.FEEDBACK_NONE, 0, 0);

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = OS.GetMessageTime();
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	event = new DNDEvent();
	if (!setEventData(event, pDataObject, grfKeyState, pt_x, pt_y, pdwEffect)) {
		keyOperation = -1;
		OS.MoveMemory(pdwEffect, new int[] {COM.DROPEFFECT_NONE}, 4);
		return COM.S_OK;
	}
	keyOperation = -1;
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	
	event.dataType = selectedDataType;		
	event.detail = selectedOperation;
	notifyListeners(DND.DropAccept,event);
	selectedDataType = null;
	for (int i = 0; i < allowedDataTypes.length; i++) {
		if (TransferData.sameType(allowedDataTypes[i], event.dataType)){
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}
	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && (allowedOperations & event.detail) == event.detail) {
		selectedOperation = event.detail;
	}

	if (selectedOperation == DND.DROP_NONE){
		OS.MoveMemory(pdwEffect, new int[] {COM.DROPEFFECT_NONE}, 4);	
		return COM.S_OK;
	}
	
	// Get Data in a Java format
	Object object = null;
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(selectedDataType)){
			object = transferAgents[i].nativeToJava(selectedDataType);
			break;
		}
	}
	if (object == null){
		selectedOperation = DND.DROP_NONE;
	}
	
	event.detail = selectedOperation;
	event.dataType = selectedDataType;
	event.data = object;
	notifyListeners(DND.Drop,event);
	selectedOperation = DND.DROP_NONE;
	if ((allowedOperations & event.detail) == event.detail) {
		selectedOperation = event.detail;
	}
	//notify source of action taken		
	OS.MoveMemory(pdwEffect, new int[] {opToOs(selectedOperation)}, 4);	
	return COM.S_OK;
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

private int getOperationFromKeyState(int grfKeyState) {
	boolean ctrl = (grfKeyState & OS.MK_CONTROL) != 0;
	boolean shift = (grfKeyState & OS.MK_SHIFT) != 0;
	if (ctrl && shift) return DND.DROP_LINK;
	if (ctrl)return DND.DROP_COPY;
	if (shift)return DND.DROP_MOVE;
	return DND.DROP_DEFAULT;
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
		Tree tree = (Tree)control;
		event.item = tree.getItem(coordinates);
		if (event.item == null) {
			Rectangle area = tree.getClientArea();
			if (area.contains(coordinates)) {
				// Scan across the width of the tree.
				for (int x1 = area.x; x1 < area.x + area.width; x1++) {
					Point pt = new Point(x1, coordinates.y);
					event.item = tree.getItem(pt);
					if (event.item != null) {
						break;
					}
				}
			}
		}
	}
	if (this.control instanceof Table) {
		Table table = (Table)control;
		event.item = table.getItem(coordinates);
		if (event.item == null) {
			Rectangle area = table.getClientArea();
			if (area.contains(coordinates)) {
				// Scan across the width of the tree.
				for (int x1 = area.x; x1 < area.x + area.width; x1++) {
					Point pt = new Point(x1, coordinates.y);
					event.item = table.getItem(pt);
					if (event.item != null) {
						break;
					}
				}
			}
		}
	}
	super.notifyListeners(eventType, event);
}

private void onDispose () {	
	if (control == null) return;

	COM.RevokeDragDrop(control.handle);
	
	if (controlListener != null)
		control.removeListener(SWT.Dispose, controlListener);
	controlListener = null;
	control.setData(DROPTARGETID, null);
	transferAgents = null;
	control = null;
	
	COM.CoLockObjectExternal(iDropTarget.getAddress(), false, true);
	
	this.Release();
	
	COM.CoFreeUnusedLibraries();
}

private int opToOs(int operation) {
	int osOperation = 0;
	if ((operation & DND.DROP_COPY) != 0){
		osOperation |= COM.DROPEFFECT_COPY;
	}
	if ((operation & DND.DROP_LINK) != 0) {
		osOperation |= COM.DROPEFFECT_LINK;
	}
	if ((operation & DND.DROP_MOVE) != 0) {
		osOperation |= COM.DROPEFFECT_MOVE;
	}
	return osOperation;
}

private int osToOp(int osOperation){
	int operation = 0;
	if ((osOperation & COM.DROPEFFECT_COPY) != 0){
		operation |= DND.DROP_COPY;
	}
	if ((osOperation & COM.DROPEFFECT_LINK) != 0) {
		operation |= DND.DROP_LINK;
	}
	if ((osOperation & COM.DROPEFFECT_MOVE) != 0) {
		operation |= DND.DROP_MOVE;
	}
	return operation;
}

private int QueryInterface(int riid, int ppvObject) {
	
	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDropTarget)) {
		COM.MoveMemory(ppvObject, new int[] {iDropTarget.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}

	COM.MoveMemory(ppvObject, new int[] {0}, 4);
	return COM.E_NOINTERFACE;
}

private int Release() {
	refCount--;
	
	if (refCount == 0) {
		disposeCOMInterfaces();
		COM.CoFreeUnusedLibraries();
	}
	
	return refCount;
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

private boolean setEventData(DNDEvent event, int pDataObject, int grfKeyState, int pt_x, int pt_y, int pdwEffect) {	
	if (pDataObject == 0 || pdwEffect == 0) return false;
	
	// get allowed operations
	int style = getStyle();
	int[] operations = new int[1];
	OS.MoveMemory(operations, pdwEffect, 4);
	operations[0] = osToOp(operations[0]) & style;
	if (operations[0] == DND.DROP_NONE) return false;
	
	// get current operation
	int operation = getOperationFromKeyState(grfKeyState);
	keyOperation = operation;
	if (operation == DND.DROP_DEFAULT) {
		if ((style & DND.DROP_DEFAULT) == 0) {
			operation = (operations[0] & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
		}
	} else {
		if ((operation & operations[0]) == 0) operation = DND.DROP_NONE;
	}
	
	// Get allowed transfer types
	TransferData[] dataTypes = new TransferData[0];
	IDataObject dataObject = new IDataObject(pDataObject);
	dataObject.AddRef();
	try {
		int[] address = new int[1];
		if (dataObject.EnumFormatEtc(COM.DATADIR_GET, address) != COM.S_OK) {
			return false;
		}
		IEnumFORMATETC enumFormatetc = new IEnumFORMATETC(address[0]);
		try {
			// Loop over enumerator and save any types that match what we are looking for
			int rgelt = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, FORMATETC.sizeof);
			try {
				int[] pceltFetched = new int[1];
				enumFormatetc.Reset();
				while (enumFormatetc.Next(1, rgelt, pceltFetched) == COM.S_OK && pceltFetched[0] == 1) {
					TransferData transferData = new TransferData();
					transferData.formatetc = new FORMATETC();
					COM.MoveMemory(transferData.formatetc, rgelt, FORMATETC.sizeof);
					transferData.type = transferData.formatetc.cfFormat;
					transferData.pIDataObject = pDataObject;
					for (int i = 0; i < transferAgents.length; i++){
						if (transferAgents[i].isSupportedType(transferData)){
							TransferData[] newDataTypes = new TransferData[dataTypes.length + 1];
							System.arraycopy(dataTypes, 0, newDataTypes, 0, dataTypes.length);
							newDataTypes[dataTypes.length] = transferData;
							dataTypes = newDataTypes;
							break;
						}
					}
				}
			} finally {
				OS.GlobalFree(rgelt);
			}
		} finally {
			enumFormatetc.Release();
		}
	} finally {
		dataObject.Release();
	}
	if (dataTypes.length == 0) return false;
	
	event.widget = this;
	event.x = pt_x;
	event.y = pt_y;
	event.time = OS.GetMessageTime();
	event.feedback = DND.FEEDBACK_SELECT;
	event.dataTypes = dataTypes;
	event.dataType = dataTypes[0];
	event.operations = operations[0];
	event.detail = operation;
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
}

}
