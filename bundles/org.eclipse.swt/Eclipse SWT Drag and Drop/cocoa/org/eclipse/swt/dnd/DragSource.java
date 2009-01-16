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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;
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
 */
public class DragSource extends Widget {

	static Callback dragSource2Args, dragSource3Args, dragSource4Args, dragSource5Args;
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};

	static {
		String className = "SWTDragSourceDelegate";

		// TODO: These should either move out of Display or be accessible to this class.
		byte[] types = {'*','\0'};
		int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

		Class clazz = DragSource.class;

		dragSource2Args = new Callback(clazz, "dragSourceProc", 2);
		int /*long*/ proc2 = dragSource2Args.getAddress();
		if (proc2 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		dragSource3Args = new Callback(clazz, "dragSourceProc", 3);
		int /*long*/ proc3 = dragSource3Args.getAddress();
		if (proc3 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		dragSource4Args = new Callback(clazz, "dragSourceProc", 4);
		int /*long*/ proc4 = dragSource4Args.getAddress();
		if (proc4 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		dragSource5Args = new Callback(clazz, "dragSourceProc", 5);
		int /*long*/ proc5 = dragSource5Args.getAddress();
		if (proc5 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);

		int /*long*/ draggedImage_endedAt_operationProc = OS.draggedImage_endedAt_operation_CALLBACK(proc5);

		// Add the NSDraggingSource callbacks
		OS.class_addMethod(cls, OS.sel_draggingSourceOperationMaskForLocal_, proc3, "@:c");
		OS.class_addMethod(cls, OS.sel_draggedImage_beganAt_, proc4, "@:@{NSPoint=ff}");
		OS.class_addMethod(cls, OS.sel_draggedImage_endedAt_operation_, draggedImage_endedAt_operationProc, "@:@{NSPoint=ff}I");
		OS.class_addMethod(cls, OS.sel_ignoreModifierKeysWhileDragging, proc3, "@:");

		// Add the NSPasteboard delegate callback
		OS.class_addMethod(cls, OS.sel_pasteboard_provideDataForType_, proc4, "@:@@");

		OS.objc_registerClassPair(cls);
	}	


	// info for registering as a drag source
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DragSourceEffect dragEffect;
	private int dragOperations;
	SWTDragSourceDelegate dragSourceDelegate;
	
	static final String DEFAULT_DRAG_SOURCE_EFFECT = "DEFAULT_DRAG_SOURCE_EFFECT"; //$NON-NLS-1$

	boolean dragStarted = false;
	private int /*long*/ delegateJniRef;
	
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
	this.control = control;
	if (control.getData(DND.DRAG_SOURCE_KEY) != null) {
		DND.error(DND.ERROR_CANNOT_INIT_DRAG);
	}
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
	
	// Create a delegate, but then stuff a pointer back to this object so callbacks will have
	// access to this object's data.
	dragSourceDelegate = (SWTDragSourceDelegate)new SWTDragSourceDelegate().alloc().init();
	delegateJniRef = OS.NewGlobalRef(this);
	if (delegateJniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.object_setInstanceVariable(dragSourceDelegate.id, SWT_OBJECT, delegateJniRef);
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

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

void drag(Event dragEvent) {
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.x = dragEvent.x;
	event.y = dragEvent.y;
	event.time = dragEvent.time;
	event.doit = true;
	notifyListeners(DND.DragStart, event);
	if (!event.doit || transferAgents == null || transferAgents.length == 0) return;
	
	NSPasteboard dragBoard = NSPasteboard.pasteboardWithName(OS.NSDragPboard);
	NSMutableArray nativeTypeArray = NSMutableArray.arrayWithCapacity(10);
	
	for (int i = 0; i < transferAgents.length; i++) {
		Transfer transfer = transferAgents[i];
		if (transfer != null) {
			String[] typeNames = transfer.getTypeNames();

			for (int j = 0; j < typeNames.length; j++) {
				nativeTypeArray.addObject(NSString.stringWith(typeNames[j]));
			}	
		}		
	}

	if (nativeTypeArray != null)
		dragBoard.declareTypes(nativeTypeArray, dragSourceDelegate);

	// Start the drag here from the Control's view.
	NSEvent currEvent = NSApplication.sharedApplication().currentEvent();
	NSPoint pt = currEvent.locationInWindow();
	NSPoint viewPt = control.view.convertPoint_fromView_(pt, null);

	// Save off the drag operations -- AppKit will call back to us to request them during the drag.
	dragOperations = opToOsOp(getStyle());
	
	// Get the image for the drag. The drag should happen from the middle of the image.
	NSImage dragImage = null;
	Image defaultDragImage = null;
	try {	
		Image image = event.image;
		
		// If no image was provided, just create a trivial image. dragImage requires a non-null image.
		if (image == null) {
			Image newDragImage = new Image(Display.getCurrent(), 20, 20);
			GC imageGC = new GC(newDragImage);
			Color grayColor = new Color(Display.getCurrent(), 50, 50, 50);
			imageGC.setForeground(grayColor);
			imageGC.drawRectangle(0, 0, 19, 19);
			imageGC.dispose();
			ImageData newImageData = newDragImage.getImageData();
			newImageData.alpha = (int)(255 * .4);
			defaultDragImage = new Image(Display.getCurrent(), newImageData);
			newDragImage.dispose();
			grayColor.dispose();
			image = defaultDragImage;
		}

		dragImage = image.handle;

		NSSize imageSize = dragImage.size();
		viewPt.x -= (imageSize.width / 2);
		viewPt.y += (imageSize.height / 2);
		
		// The third argument to dragImage is ignored as of 10.4.
		NSSize ignored = new NSSize();
		ignored.width = 0;
		ignored.height = 0;
		
		dragStarted = false;
		control.view.dragImage(dragImage, viewPt, ignored, NSApplication.sharedApplication().currentEvent(), dragBoard, dragSourceDelegate, true);

		// If we actually dragged, dragStarted will be set to true in dragImage:beganAt:
		// If not, send a DragEnd indicating nothing happened.
		if (!dragStarted) {
			event = new DNDEvent();
			event.widget = this;
			event.time = (int)System.currentTimeMillis();
			event.doit = false;
			event.detail = DND.DROP_NONE; 
			notifyListeners(DND.DragEnd, event);
		}
	} finally {	
		if (defaultDragImage != null) defaultDragImage.dispose();
	}
}

void draggedImage_beganAt(NSImage image) {
	this.dragStarted = true;
}

void draggedImage_endedAt_operation(NSImage image, NSPoint location, int /*long*/ operation) {
	int swtOperation = osOpToOp(operation);
	Event event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis();	
	event.doit = swtOperation != DND.DROP_NONE;
	event.detail = swtOperation; 
	notifyListeners(DND.DragEnd, event);
}

/** 
 * Cocoa NSDraggingSource implementations
 */
int draggingSourceOperationMaskForLocal(boolean flag) {
	// Drag operations are same for local or remote drags.
	return dragOperations;
}

static int /*long*/ dragSourceProc(int /*long*/ id, int /*long*/ sel) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DragSource ds = (DragSource)widget.getData(DND.DRAG_SOURCE_KEY);
	if (ds == null) return 0;
	
	if (sel == OS.sel_ignoreModifierKeysWhileDragging) {
		return (ds.ignoreModifierKeysWhileDragging() ? 1 : 0);
	}
	
	return 0;
}

static int /*long*/ dragSourceProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DragSource ds = null;
	
	if (widget instanceof DragSource)
		ds = (DragSource)widget;
	
	if (ds == null) return 0;
	
	if (sel == OS.sel_draggingSourceOperationMaskForLocal_) {
		return ds.draggingSourceOperationMaskForLocal(arg0 == 1);
	}
	
	return 0;
}

static int /*long*/ dragSourceProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DragSource ds = null;
	
	if (widget instanceof DragSource)
		ds = (DragSource)widget;
	
	if (ds == null) return 0;
	
	if (sel == OS.sel_draggedImage_beganAt_) {
		NSImage image = new NSImage(arg0);
		// I am deliberately ignoring arg1 (an NSPoint). It's not needed for our purposes.
		ds.draggedImage_beganAt(image);
	} else if (sel == OS.sel_pasteboard_provideDataForType_) {
		NSPasteboard pb = new NSPasteboard(arg0);
		NSString type = new NSString(arg1);
		ds.pasteboard_provideDataForType(pb, type);
	}
	
	return 0;
}

static int /*long*/ dragSourceProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return 0;
	Widget widget = display.findWidget(id);
	if (widget == null) return 0;
	DragSource ds = null;
	
	if (widget instanceof DragSource)
		ds = (DragSource)widget;
	
	if (ds == null) return 0;
	
	if (sel == OS.sel_draggedImage_endedAt_operation_) {
		NSImage image = new NSImage(arg0);
		NSPoint point = new NSPoint();
		OS.memmove(point, arg1, NSPoint.sizeof);
		ds.draggedImage_endedAt_operation(image, point, arg2);
	}
	
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

/**
 * We always want the modifier keys to potentially update the drag.
 */
boolean ignoreModifierKeysWhileDragging() {
	return false;
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

	if (delegateJniRef != 0) OS.DeleteGlobalRef(delegateJniRef);
	delegateJniRef = 0;
	OS.object_setInstanceVariable(dragSourceDelegate.id, SWT_OBJECT, 0);
	if (dragSourceDelegate != null) dragSourceDelegate.release();
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

void pasteboard_provideDataForType(NSPasteboard pasteboard, NSString dataType) {
	if (pasteboard == null || dataType == null) return;
	TransferData transferData = new TransferData();
	transferData.type = Transfer.registerType(dataType.getString());
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = (int)System.currentTimeMillis(); 
	event.dataType = transferData; 
	notifyListeners(DND.DragSetData, event);
	Transfer transfer = null;
	for (int i = 0; i < transferAgents.length; i++) {
		Transfer transferAgent = transferAgents[i];
		if (transferAgent != null && transferAgent.isSupportedType(transferData)) {
			transfer = transferAgent;
			break;
		}
	}
	if (transfer == null) return;
	transfer.javaToNative(event.data, transferData);
	if (transferData.data == null) return;

	NSObject tdata = transferData.data;

	if (dataType.isEqual(OS.NSStringPboardType) ||
			dataType.isEqual(OS.NSHTMLPboardType) || 
			dataType.isEqual(OS.NSRTFPboardType)) {
		pasteboard.setString((NSString) tdata, dataType);
	} else if (dataType.isEqual(OS.NSURLPboardType)) {
		NSURL url = (NSURL) tdata;
		url.writeToPasteboard(pasteboard);
	} else if (dataType.isEqual(OS.NSFilenamesPboardType)) {
		pasteboard.setPropertyList((NSArray) tdata, dataType);
	} else {
		pasteboard.setData((NSData) tdata, dataType);
	}
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
