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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
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

	static Callback dropTarget2Args, dropTarget3Args, dropTarget6Args;
	static int /*long*/ proc2Args, proc3Args, proc6Args;

	static {
		Class clazz = DropTarget.class;

		dropTarget2Args = new Callback(clazz, "dropTargetProc", 2);
		proc2Args = dropTarget2Args.getAddress();
		if (proc2Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		dropTarget3Args = new Callback(clazz, "dropTargetProc", 3);
		proc3Args = dropTarget3Args.getAddress();
		if (proc3Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		dropTarget6Args = new Callback(clazz, "dropTargetProc", 6);
		proc6Args = dropTarget6Args.getAddress();
		if (proc6Args == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);	
	}

	static boolean dropNotAllowed = false;
	
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
	
	static final String DEFAULT_DROP_TARGET_EFFECT = "DEFAULT_DROP_TARGET_EFFECT"; //$NON-NLS-1$
	static final String IS_ACTIVE = "org.eclipse.swt.internal.isActive"; //$NON-NLS-1$
	
void addDragHandlers() {
	// Our strategy here is to dynamically add methods to the control's class that are required 
	// by NSDraggingDestination. Then, when setTransfer is called, we just register
	// the types with the Control's NSView and AppKit will call the methods in the protocol
	// when a drag goes over the view. 

	int /*long*/ cls = OS.object_getClass(control.view.id);

	if (cls == 0) {
		DND.error(DND.ERROR_CANNOT_INIT_DROP);
	}

	// If we already added it, no need to do it again.
	int /*long*/ procPtr = OS.class_getMethodImplementation(cls, OS.sel_draggingEntered_);
	if (procPtr == proc3Args) return;

	// Add the NSDraggingDestination callbacks
	OS.class_addMethod(cls, OS.sel_draggingEntered_, proc3Args, "@:@");
	OS.class_addMethod(cls, OS.sel_draggingUpdated_, proc3Args, "@:@");
	OS.class_addMethod(cls, OS.sel_draggingExited_, proc3Args, "@:@");
	OS.class_addMethod(cls, OS.sel_performDragOperation_, proc3Args, "@:@");
	OS.class_addMethod(cls, OS.sel_wantsPeriodicDraggingUpdates, proc2Args, "@:");
	
	if (OS.class_getSuperclass(cls) == OS.class_NSOutlineView) {
		OS.class_addMethod(cls, OS.sel_outlineView_acceptDrop_item_childIndex_, proc6Args, "@:@@@i");
		OS.class_addMethod(cls, OS.sel_outlineView_validateDrop_proposedItem_proposedChildIndex_, proc6Args, "@:@@@i");
	} else if (OS.class_getSuperclass(cls) == OS.class_NSTableView) {
		OS.class_addMethod(cls, OS.sel_tableView_acceptDrop_row_dropOperation_, proc6Args, "@:@@@i");
		OS.class_addMethod(cls, OS.sel_tableView_validateDrop_proposedRow_proposedDropOperation_, proc6Args, "@:@@@i");
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

int /*long*/ callSuper (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg0);
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

int draggingEntered(int /*long*/ id, int /*long*/ sel, NSObject sender) {
	if (sender == null) return OS.NSDragOperationNone;	
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(sender, event)) {
		keyOperation = -1;
		setDropNotAllowed();
		return OS.NSDragOperationNone;
	}
	
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, allowedDataTypes.length);
	
	selectedDataType = null;
	selectedOperation = DND.DROP_NONE;
	notifyListeners(DND.DragEnter, event);

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
	
	if ((selectedOperation == DND.DROP_NONE) && (OS.PTR_SIZEOF == 4)) {
		setDropNotAllowed();
	} else {
		if(((Boolean)control.getData(IS_ACTIVE)).booleanValue() == false) {
			setDropNotAllowed();
		} else {
			clearDropNotAllowed();
		}
	}

	if (new NSObject(id).isKindOfClass(OS.class_NSTableView)) {
		return (int)/*64*/callSuper(id, sel, sender.id);
	}
	return opToOsOp(selectedOperation);
}

void draggingExited(int /*long*/ id, int /*long*/ sel, NSObject sender) {
	clearDropNotAllowed();
	if (keyOperation == -1) return;
	keyOperation = -1;
	
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	
	if (new NSObject(id).isKindOfClass(OS.class_NSTableView)) {
		callSuper(id, sel, sender.id);
	}
}

int draggingUpdated(int /*long*/ id, int /*long*/ sel, NSObject sender) {
	if (sender == null) return OS.NSDragOperationNone;	
	int oldKeyOperation = keyOperation;
	
	DNDEvent event = new DNDEvent();
	if (!setEventData(sender, event)) {
		keyOperation = -1;
		setDropNotAllowed();
		return OS.NSDragOperationNone;
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

	if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
		selectedOperation = event.detail;
	}
	
	if ((selectedOperation == DND.DROP_NONE) && (OS.PTR_SIZEOF == 4)) {
		setDropNotAllowed();
	} else {
		if(((Boolean)control.getData(IS_ACTIVE)).booleanValue() == false) {
			setDropNotAllowed();
		} else {
			clearDropNotAllowed();
		}
	}

	if (new NSObject(id).isKindOfClass(OS.class_NSTableView)) {
		return (int)/*64*/callSuper(id, sel, sender.id);
	}

	return opToOsOp(selectedOperation);
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

	addDragHandlers();	
}

static int /*long*/ dropTargetProc(int /*long*/ id, int /*long*/ sel) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DropTarget dt = (DropTarget)widget.getData(DND.DROP_TARGET_KEY);
	if (dt == null) return 0;

	if (sel == OS.sel_wantsPeriodicDraggingUpdates) {
		return dt.wantsPeriodicDraggingUpdates(id, sel) ? 1 : 0;
	}
	
	return 0;
}

static int /*long*/ dropTargetProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DropTarget dt = (DropTarget)widget.getData(DND.DROP_TARGET_KEY);
	if (dt == null) return 0;
	
	// arg0 is _always_ the sender, and implements NSDraggingInfo.
	// Looks like an NSObject for our purposes, though.
	NSObject sender = new NSObject(arg0);
	
	if (sel == OS.sel_draggingEntered_) {
		return dt.draggingEntered(id, sel, sender);
	} else if (sel == OS.sel_draggingUpdated_) {
		return dt.draggingUpdated(id, sel, sender);
	} else if (sel == OS.sel_draggingExited_) {
		dt.draggingExited(id, sel, sender);
	} else if (sel == OS.sel_performDragOperation_) {
		return dt.performDragOperation(id, sel, sender) ? 1 : 0;
	}
	
	return 0;
}

static int /*long*/ dropTargetProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DropTarget dt = (DropTarget)widget.getData(DND.DROP_TARGET_KEY);
	if (dt == null) return 0;

	if (sel == OS.sel_outlineView_acceptDrop_item_childIndex_) {
		return dt.outlineView_acceptDrop_item_childIndex(id, sel, arg0, arg1, arg2, arg3) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_validateDrop_proposedItem_proposedChildIndex_) {
		return dt.outlineView_validateDrop_proposedItem_proposedChildIndex(id, sel, arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_tableView_acceptDrop_row_dropOperation_) {
		return dt.tableView_acceptDrop_row_dropOperation(id, sel, arg0, arg1, arg2, arg3) ? 1 : 0;
	} else if (sel == OS.sel_tableView_validateDrop_proposedRow_proposedDropOperation_) {
		return dt.tableView_validateDrop_proposedRow_proposedDropOperation(id, sel, arg0, arg1, arg2, arg3);
	}
	
	return 0;
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

int getOperationFromKeyState() {
	// The NSDraggingInfo object already combined the modifier keys with the 
	// drag source's allowed events. This might be better accomplished by diffing
	// the base drag source mask with the active drag state mask instead of snarfing
	// the current event.
	
	// See documentation on [NSDraggingInfo draggingSourceOperationMask] for the
	// correct Cocoa behavior.  Control + Option or Command is NSDragOperationGeneric,
	// or DND.DROP_DEFAULT in the SWT.
	NSEvent currEvent = NSApplication.sharedApplication().currentEvent();
	int /*long*/ modifiers = currEvent.modifierFlags();
	boolean option = (modifiers & OS.NSAlternateKeyMask) == OS.NSAlternateKeyMask;
	boolean control = (modifiers & OS.NSControlKeyMask) == OS.NSControlKeyMask;
	if (control && option) return DND.DROP_DEFAULT;
	if (control) return DND.DROP_LINK;
	if (option) return DND.DROP_COPY;
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
	
	// Unregister the control as a drop target.
	control.view.unregisterDraggedTypes();
	control = null;
}

int opToOsOp(int operation) {
	int osOperation = 0;
	if ((operation & DND.DROP_COPY) != 0){
		osOperation |= OS.NSDragOperationCopy;
	}
	if ((operation & DND.DROP_LINK) != 0) {
		osOperation |= OS.NSDragOperationLink;
	}
	if ((operation & DND.DROP_MOVE) != 0) {
		osOperation |= OS.NSDragOperationMove;
	}
	if ((operation & DND.DROP_TARGET_MOVE) != 0) {
		osOperation |= OS.NSDragOperationDelete;
	}
	return osOperation;
}

int osOpToOp(int /*long*/ osOperation){
	int operation = 0;
	if ((osOperation & OS.NSDragOperationCopy) != 0){
		operation |= DND.DROP_COPY;
	}
	if ((osOperation & OS.NSDragOperationLink) != 0) {
		operation |= DND.DROP_LINK;
	}
	if ((osOperation & OS.NSDragOperationDelete) != 0) {
		operation |= DND.DROP_TARGET_MOVE;
	}
	if ((osOperation & OS.NSDragOperationMove) != 0) {
		operation |= DND.DROP_MOVE;
	}
	if (osOperation == OS.NSDragOperationEvery) {
		operation = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
	}
	return operation;
}

boolean drop(NSObject sender) {
	clearDropNotAllowed();
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();
	
	if (dropEffect != null) {
		NSPoint mouseLocation = sender.draggingLocation();
		NSPoint globalLoc = sender.draggingDestinationWindow().convertBaseToScreen(mouseLocation);
		event.item = dropEffect.getItem((int)globalLoc.x, (int)globalLoc.y);
	}
	
	event.detail = DND.DROP_NONE;
	notifyListeners(DND.DragLeave, event);
	
	event = new DNDEvent();
	if (!setEventData(sender, event) || (((Boolean)control.getData(IS_ACTIVE)).booleanValue() == false)) {
		return false;
	}
	
	keyOperation = -1;
	int allowedOperations = event.operations;
	TransferData[] allowedDataTypes = new TransferData[event.dataTypes.length];
	System.arraycopy(event.dataTypes, 0, allowedDataTypes, 0, event.dataTypes.length);
	event.dataType = selectedDataType;
	event.detail = selectedOperation;
	notifyListeners(DND.DropAccept, event);

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
	if (selectedDataType != null && (event.detail & allowedOperations) != 0) {
		selectedOperation = event.detail;
	}	
	
	if (selectedOperation == DND.DROP_NONE) {
		return false;
	}
	
	// ask drag source for dropped data
	NSPasteboard pasteboard = sender.draggingPasteboard();
	NSObject data = null;
	NSMutableArray types = NSMutableArray.arrayWithCapacity(10);

	for (int i = 0; i < transferAgents.length; i++){
		Transfer transfer = transferAgents[i];
		String[] typeNames = transfer.getTypeNames();
		int[] typeIds = transfer.getTypeIds();
		
		for (int j = 0; j < typeNames.length; j++) {
			if (selectedDataType.type == typeIds[j]) {
				types.addObject(NSString.stringWith(typeNames[j]));
				break;
			}
		}
	}

	NSString type = pasteboard.availableTypeFromArray(types);
	TransferData tdata = new TransferData();

	if (type != null) {
		tdata.type = Transfer.registerType(type.getString());
		if (type.isEqual(OS.NSStringPboardType) ||
				type.isEqual(OS.NSHTMLPboardType) ||
				type.isEqual(OS.NSRTFPboardType)) {
			tdata.data = pasteboard.stringForType(type);
		} else if (type.isEqual(OS.NSURLPboardType)) {
			tdata.data = NSURL.URLFromPasteboard(pasteboard);
		} else if (type.isEqual(OS.NSFilenamesPboardType)) {
			tdata.data = new NSArray(pasteboard.propertyListForType(type).id);
		} else {
			tdata.data = pasteboard.dataForType(type);
		}
	}

	if (tdata.data != null) {
		data = tdata.data;
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
	return (selectedOperation != DND.DROP_NONE);
}

boolean performDragOperation(int /*long*/ id, int /*long*/ sel, NSObject sender) {
	if (new NSObject(id).isKindOfClass(OS.class_NSTableView)) {
		return callSuper(id, sel, sender.id) != 0;
	}
	
	return drop (sender);
}

boolean outlineView_acceptDrop_item_childIndex(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ info, int /*long*/ item, int /*long*/ index) {
	return drop(new NSObject(info));
}

int /*long*/ outlineView_validateDrop_proposedItem_proposedChildIndex(int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ info, int /*long*/ item, int /*long*/ index) {
	//TODO stop scrolling and expansion when app does not set FEEDBACK_SCROLL and/or FEEDBACK_EXPAND
	//TODO expansion animation and auto collapse not working because of outlineView:shouldExpandItem:
	NSOutlineView widget = new NSOutlineView(outlineView);
	NSObject sender = new NSObject(info);
	NSPoint pt = sender.draggingLocation();
	pt = widget.convertPoint_fromView_(pt, null);
	Tree tree = (Tree)getControl();
	TreeItem childItem = tree.getItem(new Point((int)pt.x, (int)pt.y));
	if (feedback == 0 || childItem == null) {
		widget.setDropItem(null, -1);		
	} else {
		if ((feedback & DND.FEEDBACK_SELECT) != 0) {
			widget.setDropItem(childItem.handle, -1);
		} else {
			TreeItem parentItem = childItem.getParentItem();
			int childIndex;
			id parentID = null;
			if (parentItem != null) {
				parentID = parentItem.handle;
				childIndex = parentItem.indexOf(childItem);
			} else {
				childIndex = ((Tree)getControl()).indexOf(childItem);
			}
			if ((feedback & DND.FEEDBACK_INSERT_AFTER) != 0) {
				widget.setDropItem(parentID, childIndex + 1);
			}
			if ((feedback & DND.FEEDBACK_INSERT_BEFORE) != 0) {
				widget.setDropItem(parentID, childIndex);
			}
		}			
	}
	
	return opToOsOp(selectedOperation);
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

boolean setEventData(NSObject draggingState, DNDEvent event) {
	if (draggingState == null) return false;
	
	// get allowed operations
	int style = getStyle();
	int /*long*/ allowedActions = draggingState.draggingSourceOperationMask();
	int operations = osOpToOp(allowedActions) & style;
	if (operations == DND.DROP_NONE) return false;

	// get current operation
	int operation = getOperationFromKeyState();
	keyOperation = operation;
	if (operation == DND.DROP_DEFAULT) {
		 if ((style & DND.DROP_DEFAULT) == 0) {
			operation = (operations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
		 }
	} else {
		if ((operation & operations) == 0) operation = DND.DROP_NONE;
	}
	
	
	// get allowed transfer types
	NSPasteboard dragPBoard = draggingState.draggingPasteboard();
	NSArray draggedTypes = dragPBoard.types();
	if (draggedTypes == null) return false;
	
	int /*long*/ draggedTypeCount = draggedTypes.count();
	
	TransferData[] dataTypes = new TransferData[(int)draggedTypeCount];
	int index = -1;
	for (int i = 0; i < draggedTypeCount; i++) {
		id draggedType = draggedTypes.objectAtIndex(i);
		NSString nativeDataType = new NSString(draggedType);
		TransferData data = new TransferData();
		data.type = Transfer.registerType(nativeDataType.getString());
		
		for (int j = 0; j < transferAgents.length; j++) {
			Transfer transfer = transferAgents[j];
			if (transfer != null && transfer.isSupportedType(data)) {
				dataTypes[++index] = data;
				break;
			}
		}
	}
	if (index == -1) return false;
	
	if (index < dataTypes.length - 1) {
		TransferData[] temp = new TransferData[index + 1];
		System.arraycopy(dataTypes, 0, temp, 0, index + 1);
		dataTypes = temp;
	}

	// Convert from window-relative to global coordinates, and flip it.
	NSPoint mouse = draggingState.draggingLocation();
	NSPoint globalMouse = draggingState.draggingDestinationWindow().convertBaseToScreen(mouse);
	NSArray screens = NSScreen.screens();
	NSRect screenRect = new NSScreen(screens.objectAtIndex(0)).frame();
	globalMouse.y = screenRect.height - globalMouse.y;
	
	event.widget = this;
	event.x = (int)globalMouse.x;
	event.y = (int)globalMouse.y;
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
	
	
	// Register the types as valid drop types in Cocoa.
	// Accumulate all of the transfer types into a list.
	ArrayList typeStrings = new ArrayList();
	
	for (int i = 0; i < this.transferAgents.length; i++) {
		String[] types = transferAgents[i].getTypeNames();
		
		for (int j = 0; j < types.length; j++) {
			typeStrings.add(types[j]);
		}
	}
	
	// Convert to an NSArray of NSStrings so we can register with the Control.
	int typeStringCount = typeStrings.size();
	NSMutableArray nsTypeStrings = NSMutableArray.arrayWithCapacity(typeStringCount);
	
	for (int i = 0; i < typeStringCount; i++) {
		nsTypeStrings.addObject(NSString.stringWith((String)typeStrings.get(i)));
	}
	
	control.view.registerForDraggedTypes(nsTypeStrings);

}

void setDropNotAllowed() {
	if (!dropNotAllowed) {
		NSCursor.currentCursor().push();
		if (OS.PTR_SIZEOF == 4) OS.SetThemeCursor(OS.kThemeNotAllowedCursor);	
		dropNotAllowed = true;
	}
}

void clearDropNotAllowed() {
	if (dropNotAllowed) {
		NSCursor.pop();
		dropNotAllowed = false;
	}
}

boolean tableView_acceptDrop_row_dropOperation(int /*long*/ id, int /*long*/ sel, int /*long*/ tableView, int /*long*/ info, int /*long*/ row, int /*long*/ operation) {
	return drop(new NSObject(info));
}

int tableView_validateDrop_proposedRow_proposedDropOperation(int /*long*/ id, int /*long*/ sel, int /*long*/ tableView, int /*long*/ info, int /*long*/ row, int /*long*/ operation) {
	//TODO stop scrolling and expansion when app does not set FEEDBACK_SCROLL and/or FEEDBACK_EXPAND
	NSTableView widget = new NSTableView(tableView);
	if (0 <= row && row < widget.numberOfRows()) {
		if (feedback == 0) {
			widget.setDropRow(-1, OS.NSTableViewDropOn);		
		} else {
			if ((feedback & DND.FEEDBACK_SELECT) != 0) {
				widget.setDropRow(row, OS.NSTableViewDropOn);
			} else {
				if ((feedback & DND.FEEDBACK_INSERT_AFTER) != 0) {
					widget.setDropRow(row + 1, OS.NSTableViewDropAbove);
				}
				if ((feedback & DND.FEEDBACK_INSERT_BEFORE) != 0) {
					widget.setDropRow(row, OS.NSTableViewDropAbove);
				}
			}
		}
	}
	return opToOsOp(selectedOperation);	
}

// By returning true we get draggingUpdated messages even when the mouse isn't moving.
boolean wantsPeriodicDraggingUpdates(int /*long*/ id, int /*long*/ sel) {
	return true;
}

}
