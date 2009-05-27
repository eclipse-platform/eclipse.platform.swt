/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

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

	static final String DEFAULT_DROP_TARGET_EFFECT = "DEFAULT_DROP_TARGET_EFFECT"; //$NON-NLS-1$
	static int checkStyle (int style) {
		if (style == SWT.NONE) return DND.DROP_MOVE;
		return style;
	}
	// info for registering as a droptarget	
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DropTargetEffect dropEffect;
	
	int jniRef;
	int dragEnterEventHandler;
	int dragLeaveEventHandler;
	int dragOverEventHandler;
	int dropEventHandler;

	// Track application selections
	TransferData selectedDataType;
	int selectedOperation;

	// workaround - There is no event for "operation changed" so track operation based on key state
	int keyOperation = -1;

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
	if (control.getData(DND.DROP_TARGET_KEY) != null)
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	control.setData(DND.DROP_TARGET_KEY, this);

	jniRef = OS.NewGlobalRef (this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	
	hookEventHandlers();
	
	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (!DropTarget.this.isDisposed()){
				DropTarget.this.dispose();
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	
	this.addListener(SWT.Dispose, new Listener() {
		public void handleEvent (Event event) {
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

boolean checkEventArgs (int eventArgs) {
	if (isDisposed ()) return false;
	int routedEventType =  OS.RoutedEventArgs_typeid ();
	int source = 0;
	if (OS.Type_IsInstanceOfType (routedEventType, eventArgs)) {
		source = OS.RoutedEventArgs_OriginalSource (eventArgs);
	}
	OS.GCHandle_Free (routedEventType);
	if (source == 0) return true;
	if (OS.Object_Equals (source, control.handle)) {
		OS.GCHandle_Free (source);
		return true;
	}
	Widget widget = getDisplay().findWidget (source);
	OS.GCHandle_Free (source);
	if (widget == control) return true;
	
	if (widget instanceof TreeItem) {
		Composite parent = ((TreeItem) widget).getParent();
		if (parent == control) return true;
	} else if (widget instanceof TableItem) {
		Composite parent = ((TableItem) widget).getParent();
		if (parent == control) return true;
	}
	return false;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

void DragEnter(int sender, int dragEventArgs) {
	if (!checkEventArgs (dragEventArgs)) return;

	selectedDataType = null;
	selectedOperation = DND.DROP_NONE;
	keyOperation = -1;

	DNDEvent event = new DNDEvent();
	if (!setEventData(event, dragEventArgs)) {
		freeData(event.dataTypes);
		return;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	notifyListeners(DND.DragEnter, event);

	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
	}
	
	selectedDataType = null;
	for (int i = 0; i < allowedDataTypes.length; i++) {
		if (sameType(allowedDataTypes[i], event.dataType)) {
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}
	
	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((allowedOperations & event.detail) != 0)) {
		selectedOperation = event.detail;
	}
	
	freeData(event.dataTypes);
	OS.RoutedEventArgs_Handled (dragEventArgs, true);
}

void DragLeave(int sender, int dragEventArgs) {
	if (!checkEventArgs (dragEventArgs)) return;
	
	keyOperation = -1;
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	OS.RoutedEventArgs_Handled (dragEventArgs, true);
}

void DragOver(int sender, int dragEventArgs) {
	if (!checkEventArgs (dragEventArgs)) return;
	
	int oldKeyOperation = keyOperation;
	DNDEvent event = new DNDEvent();
	if (!setEventData(event, dragEventArgs)) {
		freeData(event.dataTypes);
		keyOperation = -1;
		OS.DragEventArgs_Effects(dragEventArgs, OS.DragDropEffects_None);
		return;
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
		if (sameType(allowedDataTypes[i], event.dataType)){
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}

	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && ((allowedOperations & event.detail) == event.detail)) {
		selectedOperation = event.detail;
	}
	
	freeData(event.dataTypes);
	OS.DragEventArgs_Effects(dragEventArgs, opToOsOp(selectedOperation));
	OS.RoutedEventArgs_Handled (dragEventArgs, true);
}

void Drop(int sender, int dragEventArgs) {
	if (!checkEventArgs (dragEventArgs)) return;

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();
	if (dropEffect != null) {
		Point position = getPosition(dragEventArgs);
		event.item = dropEffect.getItem(position.x, position.y);
	}
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	
	event = new DNDEvent();
	if (!setEventData(event, dragEventArgs)) {
		keyOperation = -1;
		freeData(event.dataTypes);
		return;
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
		if (sameType(allowedDataTypes[i], event.dataType)){
			selectedDataType = allowedDataTypes[i];
			break;
		}
	}
	selectedOperation = DND.DROP_NONE;
	if (selectedDataType != null && (allowedOperations & event.detail) == event.detail) {
		selectedOperation = event.detail;
	}

	if (selectedOperation == DND.DROP_NONE) {
		freeData(event.dataTypes);
		return;
	}
	
	// Get Data in a Java format
	Object object = null;
	for (int i = 0; i < transferAgents.length; i++){
		Transfer transfer = transferAgents[i];
		if (transfer != null && transfer.isSupportedType(selectedDataType)){
			object = transfer.nativeToJava(selectedDataType);
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
	
	freeData(event.dataTypes);
	OS.DragEventArgs_Effects(dragEventArgs, opToOsOp(selectedOperation));
	OS.RoutedEventArgs_Handled (dragEventArgs, true);
}

void freeData(TransferData[] dataTypes) {
	if (dataTypes == null) return;
	for (int i = 0; i < dataTypes.length; i++) {
		if (dataTypes[i].pValue != 0)
			OS.GCHandle_Free(dataTypes[i].pValue);
	}
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

int getData(int pDataObject, int type) {
	int pFormat = Transfer.getWPFFormat(type);
	int result = 0;
	if (OS.DataObject_GetDataPresent(pDataObject, pFormat, true)) {
		result = OS.DataObject_GetData(pDataObject, pFormat, true);
	}
	OS.GCHandle_Free (pFormat);
	return result;
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

int getOperationFromKeyState(int keyState) {
	boolean ctrl = (keyState & OS.DragDropKeyStates_ControlKey) != 0;
	boolean shift = (keyState & OS.DragDropKeyStates_ShiftKey) != 0;
	boolean alt = (keyState & OS.DragDropKeyStates_AltKey) != 0;
	if (alt) {
		if (ctrl || shift) return DND.DROP_DEFAULT;
		return DND.DROP_LINK;
	}
	if (ctrl && shift) return DND.DROP_LINK;
	if (ctrl)return DND.DROP_COPY;
	if (shift)return DND.DROP_MOVE;
	return DND.DROP_DEFAULT;
}

Point getPosition(int dragEventArgs) {
	int position = OS.DragEventArgs_GetPosition(dragEventArgs, control.handle);
	Point pt = control.getDisplay().map(control, null, (int) OS.Point_X(position), (int) OS.Point_Y(position));
	OS.GCHandle_Free (position);
	return pt;
}

/**
 * Returns a list of the data types that can be transferred to this DropTarget.
 *
 * @return a list of the data types that can be transferred to this DropTarget
 */
public Transfer[] getTransfer() {
	return transferAgents;
}

void hookEventHandlers() {
	int handle = control.handle;
	OS.UIElement_AllowDrop(handle, true);
	
	dragEnterEventHandler = OS.gcnew_DragEventHandler(jniRef, "DragEnter");
	OS.UIElement_DragEnter(handle, dragEnterEventHandler);

	dragLeaveEventHandler = OS.gcnew_DragEventHandler(jniRef, "DragLeave");
	OS.UIElement_DragLeave(handle, dragLeaveEventHandler);

	dragOverEventHandler = OS.gcnew_DragEventHandler(jniRef, "DragOver");
	OS.UIElement_DragOver(handle, dragOverEventHandler);

	dropEventHandler = OS.gcnew_DragEventHandler(jniRef, "Drop");
	OS.UIElement_Drop(handle, dropEventHandler);
}

void onDispose () {	
	if (control == null)
		return;
	if (controlListener != null)
		control.removeListener(SWT.Dispose, controlListener);
	unhookEventHandlers();
	if (jniRef != 0) OS.DeleteGlobalRef (jniRef);
	jniRef = 0;
	controlListener = null;
	control.setData(DND.DROP_TARGET_KEY, null);
	transferAgents = null;
	control = null;
}

int osOpToOp(int osOperation){
	int operation = 0;
	if ((osOperation & OS.DragDropEffects_Copy) != 0){
		operation |= DND.DROP_COPY;
	}
	if ((osOperation & OS.DragDropEffects_Link) != 0) {
		operation |= DND.DROP_LINK;
	}
	if ((osOperation & OS.DragDropEffects_Move) != 0) {
		operation |= DND.DROP_MOVE;
	}
	return operation;
}

int opToOsOp(int operation){
	int osOperation = 0;
	if ((operation & DND.DROP_COPY) != 0){
		osOperation |= OS.DragDropEffects_Copy;
	}
	if ((operation & DND.DROP_LINK) != 0) {
		osOperation |= OS.DragDropEffects_Link;
	}
	if ((operation & DND.DROP_MOVE) != 0) {
		osOperation |= OS.DragDropEffects_Move;
	}
	return osOperation;
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

boolean sameType(TransferData data1, TransferData data2) {
	if (data1 == data2) return true;
	if (data1 == null || data2 == null) return false;
	return data1.type == data2.type;
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

boolean setEventData(DNDEvent event, int dragEventArgs) {
	Point position = getPosition(dragEventArgs);

	// get allowed operations
	int style = getStyle();
	int operations = osOpToOp(OS.DragEventArgs_Effects(dragEventArgs)) & style;
	if (operations == DND.DROP_NONE) return false;
	
	// get current operation
	int operation = getOperationFromKeyState(OS.DragEventArgs_KeyStates(dragEventArgs));
	keyOperation = operation;
	if (operation == DND.DROP_DEFAULT) {
		if ((style & DND.DROP_DEFAULT) == 0) {
			operation = (operations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
		}
	} else {
		if ((operation & operations) == 0) operation = DND.DROP_NONE;
	}

	// Get allowed transfer types
	TransferData[] dataTypes = new TransferData[0];
	int pDataObject = OS.DragEventArgs_Data(dragEventArgs);
	for (int i = 0; i < transferAgents.length; i++) {
		Transfer transfer = transferAgents[i];
		if (transfer != null) {
			int[] types = transfer.getTypeIds();
			for (int j = 0; j < types.length; j++) {
				TransferData data = new TransferData();
				data.type = types[j];
				data.pValue = getData(pDataObject, data.type);
				if (data.pValue != 0) {
					TransferData[] newDataTypes = new TransferData[dataTypes.length + 1];
					System.arraycopy(dataTypes, 0, newDataTypes, 0, dataTypes.length);
					newDataTypes[dataTypes.length] = data;
					dataTypes = newDataTypes;	
					break;
				}
			}
		}
	}
	OS.GCHandle_Free (pDataObject);
	if (dataTypes.length == 0) return false;

	event.widget = this;
	event.x = position.x;
	event.y = position.y;
	event.time = (int)System.currentTimeMillis();
	event.feedback = DND.FEEDBACK_SELECT;
	event.dataTypes = dataTypes;
	event.dataType = dataTypes[0];
	event.operations = operations;
	event.detail = operation;
	if (dropEffect != null) {
		event.item = dropEffect.getItem(position.x, position.y);
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
}

void unhookEventHandlers() {
	int handle = control.handle;
	OS.UIElement_AllowDrop(handle, false);

	int dragEnterEvent = OS.UIElement_DragEnterEvent();
	OS.UIElement_RemoveHandler(handle, dragEnterEvent, dragEnterEventHandler);
	OS.GCHandle_Free(dragEnterEventHandler);
	OS.GCHandle_Free(dragEnterEvent);
	dragEnterEventHandler = 0;
	
	int dragLeaveEvent = OS.UIElement_DragLeaveEvent();
	OS.UIElement_RemoveHandler(handle, dragLeaveEvent, dragLeaveEventHandler);
	OS.GCHandle_Free(dragLeaveEventHandler);
	OS.GCHandle_Free(dragLeaveEvent);
	dragLeaveEventHandler = 0;

	int dragOverEvent = OS.UIElement_DragOverEvent();
	OS.UIElement_RemoveHandler(handle, dragOverEvent, dragOverEventHandler);
	OS.GCHandle_Free(dragOverEventHandler);
	OS.GCHandle_Free(dragOverEvent);
	dragOverEventHandler = 0;

	int dropEvent = OS.UIElement_DropEvent();
	OS.UIElement_RemoveHandler(handle, dropEvent, dropEventHandler);
	OS.GCHandle_Free(dropEventHandler);
	OS.GCHandle_Free(dropEvent);
	dropEventHandler = 0;
}

}
