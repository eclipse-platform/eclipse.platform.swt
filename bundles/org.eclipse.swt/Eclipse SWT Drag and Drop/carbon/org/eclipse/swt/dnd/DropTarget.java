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
import org.eclipse.swt.internal.carbon.*;

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
	int feedback = DND.FEEDBACK_NONE;

	// Track application selections
	TransferData selectedDataType;
	int selectedOperation;
	
	// workaround - There is no event for "operation changed" so track operation based on key state
	int keyOperation = -1;
	
	// workaround - Simulate events when mouse is not moving
	long dragOverStart;
	Runnable dragOverHeartbeat;
	DNDEvent dragOverEvent;
	
	// workaround - OS events are relative to the application, not the control.
	// Track which control is the current target to determine when drag and
	// drop enters or leaves a widget.
	static DropTarget CurrentDropTarget = null;
	
	static final String DEFAULT_DROP_TARGET_EFFECT = "DEFAULT_DROP_TARGET_EFFECT"; //$NON-NLS-1$
	static final int DRAGOVER_HYSTERESIS = 50;
	static final String IS_ACTIVE = "org.eclipse.swt.internal.isActive";	 //$NON-NLS-1$
	static Callback DragTrackingHandler;
	static Callback DragReceiveHandler;
	
	static {
		DragTrackingHandler = new Callback(DropTarget.class, "DragTrackingHandler", 4); //$NON-NLS-1$
		int dragTrackingHandlerAddress = DragTrackingHandler.getAddress();
		if (dragTrackingHandlerAddress == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		DragReceiveHandler = new Callback(DropTarget.class, "DragReceiveHandler", 3); //$NON-NLS-1$
		int dragReceiveHandlerAddress = DragReceiveHandler.getAddress();
		if (dragReceiveHandlerAddress == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		OS.InstallTrackingHandler(dragTrackingHandlerAddress, 0, null);
		OS.InstallReceiveHandler(dragReceiveHandlerAddress, 0, null);
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
	super(control, checkStyle(style));
	this.control = control;
	if (DragTrackingHandler == null || DragTrackingHandler == null) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}
	if (control.getData(DND.DROP_TARGET_KEY) != null) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}
	control.setData(DND.DROP_TARGET_KEY, this);

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (!DropTarget.this.isDisposed()) {
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
					event.item = dropEffect.getItem(event.x, event.y);
				}
				selectedDataType = null;
				selectedOperation = DND.DROP_NONE;				
				notifyListeners(DND.DragOver, event);
				if (event.dataType != null) {
					for (int i = 0; i < allowedTypes.length; i++) {
						if (allowedTypes[i].type == event.dataType.type) {
							selectedDataType = event.dataType;
							break;
						}
					}
				}
				if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
					selectedOperation = event.detail;
				}
			}
			control = DropTarget.this.control;
			if (control == null || control.isDisposed()) return;
			control.getDisplay().timerExec(delay, dragOverHeartbeat);
		}
	};
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

static int DragReceiveHandler(int theWindow, int handlerRefCon, int theDrag) {
	DropTarget target = FindDropTarget(theWindow, theDrag);
	if (target == null) return OS.noErr;
	return target.dragReceiveHandler(theWindow, handlerRefCon, theDrag);   
}

static int DragTrackingHandler(int message, int theWindow, int handlerRefCon, int theDrag) {
	if (message == OS.kDragTrackingLeaveHandler || message == OS.kDragTrackingEnterHandler) {
		CurrentDropTarget = null;
		return OS.noErr;
	}
	DropTarget target = FindDropTarget(theWindow, theDrag);
	if (CurrentDropTarget != null) {
		if (target == null || CurrentDropTarget.control.handle != target.control.handle) {
			CurrentDropTarget.dragTrackingHandler(OS.kDragTrackingLeaveWindow, theWindow, handlerRefCon, theDrag);
			CurrentDropTarget = target;
			message = OS.kDragTrackingEnterWindow;
		}
	} else {
		CurrentDropTarget = target;
		message = OS.kDragTrackingEnterWindow;
	}
	if (target == null) return OS.noErr;
	return target.dragTrackingHandler(message, theWindow, handlerRefCon, theDrag);   
}

static DropTarget FindDropTarget(int theWindow, int theDrag) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	Point mouse = new Point();
	OS.GetDragMouse(theDrag, mouse, null);
	int[] theRoot = new int[1];
	OS.GetRootControl(theWindow, theRoot);
	int[] theControl = new int[1];
	Rect rect = new Rect();
	OS.GetWindowBounds (theWindow, (short) OS.kWindowStructureRgn, rect);
	CGPoint inPoint = new CGPoint();
	inPoint.x = mouse.h - rect.left;
	inPoint.y = mouse.v - rect.top;
	int[] event = new int[1];
	OS.CreateEvent (0, OS.kEventClassMouse, OS.kEventMouseDown, 0.0, 0, event);
	OS.SetEventParameter (event[0], OS.kEventParamWindowMouseLocation, OS.typeHIPoint, CGPoint.sizeof, inPoint);
	OS.HIViewGetViewForMouseEvent (theRoot [0], event [0], theControl);
	OS.ReleaseEvent(event[0]);
	if (!OS.IsControlEnabled(theControl[0])) return null;				
	Widget widget = display.findWidget(theControl[0]);
	if (widget == null) return null;
	return (DropTarget)widget.getData(DND.DROP_TARGET_KEY);
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

int dragReceiveHandler(int theWindow, int handlerRefCon, int theDrag) {
	updateDragOverHover(0, null);
	if (keyOperation == -1) return OS.dragNotAcceptedErr;

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	
	event = new DNDEvent();
	if (!setEventData(theDrag, event)) {
		return OS.dragNotAcceptedErr;
	}
	
	keyOperation = -1;
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, event.dataTypes.length);
	event.dataType = selectedDataType;
	event.detail = selectedOperation;
	selectedDataType = null;
	selectedOperation = DND.DROP_NONE;
	notifyListeners(DND.DropAccept, event);
	
	if (event.dataType != null) {
		for (int i = 0; i < allowedDataTypes.length; i++) {
			if (allowedDataTypes[i].type == event.dataType.type) {
				selectedDataType = allowedDataTypes[i];
				break;
			}
		}
	}
	if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
		selectedOperation = event.detail;
	}	
	if (selectedOperation == DND.DROP_NONE) {
		// this was not a successful drop
		return OS.dragNotAcceptedErr;
	}
	// ask drag source for dropped data
	byte[][] data  = new byte[0][];
	// locate all the items with data of the desired type 
	short[] numItems = new short[1];
	OS.CountDragItems(theDrag, numItems);
	for (short i = 0; i < numItems[0]; i++) {
		int[] theItemRef = new int[1];
		OS.GetDragItemReferenceNumber(theDrag, (short) (i+1), theItemRef);
		int[] size = new int[1];
		OS.GetFlavorDataSize(theDrag, theItemRef[0], selectedDataType.type, size);
		if (size[0] > 0) {
			byte[] buffer = new byte[size[0]];
			OS.GetFlavorData(theDrag, theItemRef[0], selectedDataType.type, buffer, size, 0);
			byte[][] newData = new byte[data.length + 1][];
			System.arraycopy(data, 0, newData, 0, data.length);
			newData[data.length] = buffer;
			data = newData;
		}
	}
	// Get Data in a Java format
	Object object = null;
	for (int i = 0; i < transferAgents.length; i++) {
		Transfer transfer = transferAgents[i];
		if (transfer != null && transfer.isSupportedType(selectedDataType)) {
			selectedDataType.data = data;
			object = transfer.nativeToJava(selectedDataType);
			break;
		}
	}
	
	if (object == null) {
		selectedOperation = DND.DROP_NONE;
	}
		
	event.dataType = selectedDataType;
	event.detail = selectedOperation;
	event.data = object;
	notifyListeners(DND.Drop, event);
	selectedOperation = DND.DROP_NONE;
	if ((allowedOperations & event.detail) == event.detail) {
		selectedOperation = event.detail;
	}
	//notify source of action taken
	int action = opToOsOp(selectedOperation);
	OS.SetDragDropAction(theDrag, action);
	return (selectedOperation == DND.DROP_NONE) ? OS.dragNotAcceptedErr : OS.noErr;
}

int dragTrackingHandler(int message, int theWindow, int handlerRefCon, int theDrag) {
	
	if (message == OS.kDragTrackingLeaveWindow) {
		updateDragOverHover(0, null);
		OS.SetThemeCursor(OS.kThemeArrowCursor);
		if (keyOperation == -1) return OS.dragNotAcceptedErr;
		keyOperation = -1;
		
		DNDEvent event = new DNDEvent();
		event.widget = this;
		event.time = (int)System.currentTimeMillis();
		event.detail = DND.DROP_NONE;
		notifyListeners(DND.DragLeave, event);
		return OS.noErr;
	}
	
	int oldKeyOperation = keyOperation;
	
	if (message == OS.kDragTrackingEnterWindow) {
		selectedDataType = null;
		selectedOperation = 0;
	}
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(theDrag, event) || ((Boolean)control.getData(IS_ACTIVE)).booleanValue() == false)  {
		keyOperation = -1;
		OS.SetThemeCursor(OS.kThemeNotAllowedCursor);
		return OS.dragNotAcceptedErr;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	
	switch (message) {
		case OS.kDragTrackingEnterWindow:
			event.type = DND.DragEnter;
			break;
		case OS.kDragTrackingInWindow:
			if (keyOperation == oldKeyOperation) {
				event.type = DND.DragOver;
				event.dataType = selectedDataType;
				event.detail = selectedOperation;
			}else {
				event.type = DND.DragOperationChanged;
				event.dataType = selectedDataType;
			}
			break;
	}
	
	updateDragOverHover(DRAGOVER_HYSTERESIS, event);
	selectedDataType = null;
	selectedOperation = DND.DROP_NONE;
	notifyListeners(event.type, event);

	if (event.detail == DND.DROP_DEFAULT) {
		event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
	}
	
	if (event.dataType != null) {
		for (int i = 0; i < allowedDataTypes.length; i++) {
			if (allowedDataTypes[i].type == event.dataType.type) {
				selectedDataType = allowedDataTypes[i];
				break;
			}
		}
	}

	if (selectedDataType != null && (allowedOperations & event.detail) != 0) {
		selectedOperation = event.detail;
	}
	
	OS.SetDragDropAction(theDrag, opToOsOp(selectedOperation));

	switch (selectedOperation) {
		case DND.DROP_COPY:
			OS.SetThemeCursor(OS.kThemeCopyArrowCursor);
			break;
		case DND.DROP_LINK:
			OS.SetThemeCursor(OS.kThemeAliasArrowCursor);
			break;
		case DND.DROP_MOVE:
			OS.SetThemeCursor(OS.kThemeArrowCursor);
			break;
		default:
			OS.SetThemeCursor(OS.kThemeNotAllowedCursor);
	}
  	
  	if (message == OS.kDragTrackingEnterWindow) {
		dragOverHeartbeat.run();		
	}
	return OS.noErr;
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

int getOperationFromKeyState(int theDrag) {
	short[] modifiers = new short[1];
	OS.GetDragModifiers(theDrag, modifiers, null, null);
	boolean option = (modifiers[0] & OS.optionKey) == OS.optionKey;
	boolean command = (modifiers[0] & OS.cmdKey) == OS.cmdKey;
	if (option && command) return DND.DROP_LINK;
	if (option) return DND.DROP_COPY;
	if (command) return DND.DROP_MOVE;
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

void onDispose () {	
	if (control == null)
		return;
	if (controlListener != null)
		control.removeListener(SWT.Dispose, controlListener);
	controlListener = null;
	control.setData(DND.DROP_TARGET_KEY, null);
	transferAgents = null;
	control = null;
}

int opToOsOp(int operation) {
	int osOperation = 0;
	if ((operation & DND.DROP_COPY) != 0){
		osOperation |= OS.kDragActionCopy;
	}
	if ((operation & DND.DROP_LINK) != 0) {
		osOperation |= OS.kDragActionAlias;
	}
	if ((operation & DND.DROP_MOVE) != 0) {
		osOperation |= OS.kDragActionMove;
	}
	return osOperation;
}

int osOpToOp(int osOperation){
	int operation = 0;
	if ((osOperation & OS.kDragActionCopy) != 0){
		operation |= DND.DROP_COPY;
	}
	if ((osOperation & OS.kDragActionAlias) != 0) {
		operation |= DND.DROP_LINK;
	}
	if ((osOperation & OS.kDragActionMove) != 0) {
		operation |= DND.DROP_MOVE;
	}
	if (osOperation == OS.kDragActionAll) {
		operation = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
	}
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

boolean setEventData(int theDrag, DNDEvent event) {
	if (theDrag == 0) return false;
	
	// get allowed operations
	int style = getStyle();
	int[] outActions = new int[1];
	OS.GetDragAllowableActions(theDrag, outActions);
	int operations = osOpToOp(outActions[0]) & style;
	if (operations == DND.DROP_NONE) return false;
	
	//get current operation
	int operation =  getOperationFromKeyState(theDrag);
	keyOperation = operation;
	if (operation == DND.DROP_DEFAULT) {
		 if ((style & DND.DROP_DEFAULT) == 0) {
			operation = (operations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
		 }
	} else {
		if ((operation & operations) == 0) operation = DND.DROP_NONE;
	}
	
	// get allowed transfer types
	short[] numItems = new short[1];
	OS.CountDragItems(theDrag, numItems);
	int[] flavors = new int[10];
	int index = -1;
	//Get a unique list of flavors
	for (short i = 0; i < numItems[0]; i++) {
		int[] theItemRef = new int[1];
		OS.GetDragItemReferenceNumber(theDrag, (short) (i+1), theItemRef);
		short[] numFlavors = new short[1];
		OS.CountDragItemFlavors(theDrag, theItemRef[0], numFlavors);
		int[] theType = new int[1];
		for (int j = 0; j < numFlavors[0]; j++) {
			theType[0] = 0;
			if (OS.GetFlavorType(theDrag, theItemRef[0], (short) (j+1), theType) == OS.noErr) {
				boolean unique = true;
				for (int k = 0; k < flavors.length; k++) {
					if (flavors[k] == theType[0]) {
						unique = false;
						break;
					}
				}
				if (unique) {
					if (index == flavors.length - 1) {
						int[] temp = new int[flavors.length + 10];
						System.arraycopy(flavors, 0, temp, 0, flavors.length);
						flavors = temp;
					}
					flavors[++index] = theType[0];
				}
			}
		}
	}
	if (index == -1) return false;
	
	TransferData[] dataTypes = new TransferData[index+1];
	index = -1;
	for (int i = 0; i < dataTypes.length; i++) {
		if (flavors[i] != 0) {
			TransferData data = new TransferData();
			data.type = flavors[i];
			for (int j = 0; j < transferAgents.length; j++) {
				Transfer transfer = transferAgents[j];
				if (transfer != null && transfer.isSupportedType(data)) {
					dataTypes[++index] = data;
					break;
				}
			}
		}
	}
	if (index == -1) return false;
	
	if (index < dataTypes.length - 1) {
		TransferData[] temp = new TransferData[index + 1];
		System.arraycopy(dataTypes, 0, temp, 0, index + 1);
		dataTypes = temp;
	}

	Point mouse = new Point();
	OS.GetDragMouse(theDrag, mouse, null);
	
	event.widget = this;
	event.x = mouse.h;
	event.y = mouse.v;
	event.time = (int)System.currentTimeMillis();
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
	dragOverEvent.dataTypes  = event.dataTypes;
	dragOverEvent.operations = event.operations;
	dragOverEvent.dataType  = event.dataType;
	dragOverEvent.detail  = event.detail;
}

}
