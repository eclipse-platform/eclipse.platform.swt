/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 497807
 *******************************************************************************/
package org.eclipse.swt.dnd;


import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.dnd.gtk.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;
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
 * <pre><code>
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
 * </code></pre>
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

	long targetList;

	//workaround - remember action performed for DragEnd
	boolean moveData = false;

	static final String DEFAULT_DRAG_SOURCE_EFFECT = "DEFAULT_DRAG_SOURCE_EFFECT"; //$NON-NLS-1$

	/* GTK4 GtkDragSource event controller signal callbacks */
	static Callback dragBeginProc, dragPrepareProc, dragEndProc;

	/* GTK3 GtkWidget drag event signal callbacks */
	static Callback DragBegin, DragGetData, DragEnd, DragDataDelete;

	static {
		if (GTK.GTK4) {
			dragBeginProc = new Callback(DragSource.class, "dragBeginProc", void.class, new Type[] { long.class, long.class });
			dragPrepareProc = new Callback(DragSource.class, "dragPrepareProc", long.class, new Type[] { long.class, double.class, double.class });
			dragEndProc = new Callback(DragSource.class, "dragEndProc", void.class, new Type[] { long.class, long.class, boolean.class });
		} else {
			DragBegin = new Callback(DragSource.class, "DragBegin", 2); //$NON-NLS-1$
			DragGetData = new Callback(DragSource.class, "DragGetData", 5);	 //$NON-NLS-1$
			DragEnd = new Callback(DragSource.class, "DragEnd", 2); //$NON-NLS-1$
			DragDataDelete = new Callback(DragSource.class, "DragDataDelete", 2); //$NON-NLS-1$
		}
	}

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

	if (GTK.GTK4) {
		if (dragBeginProc == null || dragPrepareProc == null || dragEndProc == null) {
			DND.error(DND.ERROR_CANNOT_INIT_DRAG);
		}
		if (control.getData(DND.DRAG_SOURCE_KEY) != null) {
			DND.error(DND.ERROR_CANNOT_INIT_DRAG);
		}
		control.setData(DND.DRAG_SOURCE_KEY, this);

		long dragSourceController = GTK4.gtk_drag_source_new();
		GTK4.gtk_widget_add_controller(control.handle, dragSourceController);

		OS.g_signal_connect(dragSourceController, OS.drag_begin, dragBeginProc.getAddress(), 0);
		OS.g_signal_connect(dragSourceController, OS.prepare, dragPrepareProc.getAddress(), 0);
		OS.g_signal_connect(dragSourceController, OS.drag_end, dragEndProc.getAddress(), 0);

		// Set permitted actions on the GtkDragSource
		int actions = opToOsOp(style);
		GTK4.gtk_drag_source_set_actions(dragSourceController, actions);
	} else {
		if (DragGetData == null || DragEnd == null || DragDataDelete == null) {
			DND.error(DND.ERROR_CANNOT_INIT_DRAG);
		}
		if (control.getData(DND.DRAG_SOURCE_KEY) != null) {
			DND.error(DND.ERROR_CANNOT_INIT_DRAG);
		}
		control.setData(DND.DRAG_SOURCE_KEY, this);

		// There's a native GTK snippet available, find 'Issue0400_WaylandDndEvents.cpp' in this repo.
		// It may be helpful in understanding / debugging bugs.
		OS.g_signal_connect(control.handle, OS.drag_begin, DragBegin.getAddress(), 0);
		OS.g_signal_connect(control.handle, OS.drag_data_get, DragGetData.getAddress(), 0);
		OS.g_signal_connect(control.handle, OS.drag_end, DragEnd.getAddress(), 0);
		OS.g_signal_connect(control.handle, OS.drag_data_delete, DragDataDelete.getAddress(), 0);

		controlListener = event -> {
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
		};
		control.addListener (SWT.Dispose, controlListener);
		control.addListener (SWT.DragDetect, controlListener);

		Object effect = control.getData(DEFAULT_DRAG_SOURCE_EFFECT);
		if (effect instanceof DragSourceEffect) {
			dragEffect = (DragSourceEffect) effect;
		} else if (control instanceof Tree) {
			dragEffect = new TreeDragSourceEffect((Tree) control);
		} else if (control instanceof Table) {
			dragEffect = new TableDragSourceEffect((Table) control);
		} else if (control instanceof List) {
			dragEffect = new ListDragSourceEffect((List) control);
		}

		this.addListener(SWT.Dispose, e -> onDispose());
	}
}

static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}

static void dragBeginProc(long source, long drag) {
	long widgetHandle = GTK.gtk_event_controller_get_widget(source);
	DragSource dragSource = FindDragSource(widgetHandle);
	if (dragSource == null) return;

	dragSource.dragBeginGtk4(source);
}

void dragBeginGtk4(long source) {
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.doit = true;
	notifyListeners(DND.DragStart, event);
	if (!event.doit || transferAgents == null || transferAgents.length == 0) return;

	// If specified, setup drag icon
	Image dragIcon = event.image;
	if (source != 0 && dragIcon != null) {
		long pixbuf = ImageList.createPixbuf(dragIcon);
		long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
		OS.g_object_unref(pixbuf);
		GTK4.gtk_drag_source_set_icon(source, texture, 0, 0);
	}
}

static long dragPrepareProc(long source, double x, double y) {
	long widgetHandle = GTK.gtk_event_controller_get_widget(source);
	DragSource dragSource = FindDragSource(widgetHandle);
	if (dragSource == null) return 0;

	return dragSource.dragPrepare();
}

long dragPrepare() {
	TransferData transferData = new TransferData();

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.dataType = transferData;
	notifyListeners(DND.DragSetData, event);
	if (!event.doit) return 0;

	// TODO: Need to return GdkContentProvider for the data given from event.data
	// Data from event.data also has to be converted to native through the Transfer class
	return 0;
}

static void dragEndProc(long source, long drag, boolean delete_data) {

}

static long DragBegin(long widget, long context){
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	source.dragBegin(widget, context);
	return 0;
}

static long DragDataDelete(long widget, long context){
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	source.dragDataDelete(widget, context);
	return 0;
}

static long DragEnd(long widget, long context){
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	source.dragEnd(widget, context);
	return 0;
}

static long DragGetData(long widget, long context, long selection_data,  long info, long time){
	DragSource source = FindDragSource(widget);
	if (source == null) return 0;
	source.dragGetData(widget, context, selection_data, (int)info, (int)time);
	return 0;
}

static DragSource FindDragSource(long handle) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	Widget widget = display.findWidget(handle);
	if (widget == null) return null;
	return (DragSource)widget.getData(DND.DRAG_SOURCE_KEY);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when a drag and drop operation is in progress, by sending
 * it one of the messages defined in the <code>DragSourceListener</code>
 * interface.
 *
 * <ul>
 * <li><code>dragStart</code> is called when the user has begun the actions required to drag the widget.
 * This event gives the application the chance to decide if a drag should be started.
 * <li><code>dragSetData</code> is called when the data is required from the drag source.
 * <li><code>dragFinished</code> is called when the drop has successfully completed (mouse up
 * over a valid target) or has been terminated (such as hitting the ESC key). Perform cleanup
 * such as removing data from the source side on a successful move operation.
 * </ul>
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

@Override
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

boolean canBeginDrag() {
	if (transferAgents == null || transferAgents.length == 0) return false;
	if (targetList == 0) return false;
	return true;
}

void drag(Event dragEvent) {
	moveData = false;
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.x = dragEvent.x;
	event.y = dragEvent.y;
	event.time = dragEvent.time;
	event.doit = true;
	notifyListeners(DND.DragStart, event);
	if (!event.doit || !canBeginDrag()) return;

	int actions = opToOsOp(getStyle());
	Image image = event.image;
	long context;
	context = GTK3.gtk_drag_begin_with_coordinates(control.handle, targetList, actions, 1, 0, -1, -1);
	if (context != 0 && image != null) {
		GTK3.gtk_drag_set_icon_surface(context, image.surface);
	}
}

void dragBegin(long widget, long context) {
	/*
	 * Bug 515035: GTK DnD hijacks the D&D logic we have in SWT.
	 * When we recieve the signal from GTK of DragBegin, we will
	 * notify SWT that a drag has occurred.
	 */
	if (this.control instanceof Text) {
		DNDEvent event = new DNDEvent();
		Display display = Display.getCurrent();
		Point loc = display.getCursorLocation();
		event.widget = this;
		event.doit = true;
		event.x = loc.x;
		event.y = loc.y;
		notifyListeners(DND.DragStart, event);
		if (!event.doit || transferAgents == null || transferAgents.length == 0) return;
		if (targetList == 0) return;
		Image image = event.image;
		if (context != 0 && image != null) {
			GTK3.gtk_drag_set_icon_surface(context, image.surface);
		}
	}
}

void dragEnd(long widget, long context){
	/*
	 * Bug in GTK.  If a drag is initiated using gtk_drag_begin and the
	 * mouse is released immediately, the mouse and keyboard remain
	 * grabbed.  The fix is to release the grab on the mouse and keyboard
	 * whenever the drag is terminated.
	 *
	 * NOTE: We believe that it is never an error to ungrab when
	 * a drag is finished.
	 */
	long display;
	if (GTK.GTK4) {
		long surface = GTK4.gtk_native_get_surface(GTK4.gtk_widget_get_native (widget));
		display = GDK.gdk_surface_get_display(surface);
	} else {
		display = GDK.gdk_window_get_display(GTK3.gtk_widget_get_window(widget));
	}
	long pointer = GDK.gdk_get_pointer(display);

	if (GTK.GTK4) {
		//TODO: GTK4, ungrab keyboard seat if different from pointer's seat
	} else {
		long keyboard = GDK.gdk_device_get_associated_device(pointer);
		long keyboard_seat = GDK.gdk_device_get_seat(keyboard);
		GDK.gdk_seat_ungrab(keyboard_seat);
	}

	long pointer_seat = GDK.gdk_device_get_seat(pointer);
	GDK.gdk_seat_ungrab(pointer_seat);

	int operation = DND.DROP_NONE;
	if (context != 0) {
		long dest_window = 0;
		int action = 0;
		/*
		 * Feature in GTK: dest_window information is not gathered here in Wayland as the
		 * DragEnd signal does not give the correct destination window. GTK3.14+ with
		 * GTKGestures will handle file operations correctly without the
		 * gdk_drag_context_get_dest_window() call. See Bug 503431.
		 */
		action = GDK.gdk_drag_context_get_selected_action(context);
		if (OS.isX11()) {
			dest_window = GDK.gdk_drag_context_get_dest_window(context);
		}
		if (dest_window != 0 || OS.isWayland()) { // NOTE: if dest_window is 0, drag was aborted
			if (moveData) {
				operation = DND.DROP_MOVE;
			} else {
				operation = osOpToOp(action);
				if (operation == DND.DROP_MOVE) operation = DND.DROP_NONE;
			}
		}
	}
	DNDEvent event = new DNDEvent();
	event.widget = this;
	//event.time = ???
	event.doit = operation != 0;
	event.detail = operation;
	notifyListeners(DND.DragEnd, event);

	if (OS.isWayland()) {
		/*
		 * Feature in GTK: release events are not signaled during the dragEnd phrase of a Drag and Drop
		 * in Wayland. In order to work with the current logic for DnD in multiselection
		 * Widgets (tree, table, list), the selection function needs to be set back to
		 * true on dragEnd as well as release_event(). See bug 503431.
		 */
		if (this.control instanceof Table
				|| this.control instanceof Tree
				|| this.control instanceof List) {
			long selection = GTK.gtk_tree_view_get_selection (widget);
			GTK.gtk_tree_selection_set_select_function(selection,0,0,0);
		}
	}
	moveData = false;
}

void dragGetData(long widget, long context, long selection_data,  int info, int time){
	if (selection_data == 0) return;
	int length = GTK3.gtk_selection_data_get_length(selection_data);
	int format = GTK3.gtk_selection_data_get_format(selection_data);
	long data = GTK3.gtk_selection_data_get_data(selection_data);
	long target = GTK3.gtk_selection_data_get_target(selection_data);
	if (target == 0) return;

	TransferData transferData = new TransferData();
	transferData.type = target;
	transferData.pValue = data;
	transferData.length = length;
	transferData.format = format;

	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = time;
	event.dataType = transferData;
	notifyListeners(DND.DragSetData, event);
	if (!event.doit) return;
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
	if (transferData.result != 1) return;
	GTK3.gtk_selection_data_set(selection_data, transferData.type, transferData.format, transferData.pValue, transferData.length);
	OS.g_free(transferData.pValue);
	return;
}

void dragDataDelete(long widget, long context){
	moveData = true;
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
	if (control == null) return;
	if (targetList != 0) {
		GTK3.gtk_target_list_unref(targetList);
	}
	targetList = 0;
	if (controlListener != null) {
		control.removeListener(SWT.Dispose, controlListener);
		control.removeListener(SWT.DragDetect, controlListener);
	}
	controlListener = null;
	control.setData(DND.DRAG_SOURCE_KEY, null);
	control = null;
	transferAgents = null;
}

int opToOsOp(int operation){
	int osOperation = 0;

	if ((operation & DND.DROP_COPY) == DND.DROP_COPY)
		osOperation |= GDK.GDK_ACTION_COPY;
	if ((operation & DND.DROP_MOVE) == DND.DROP_MOVE)
		osOperation |= GDK.GDK_ACTION_MOVE;
	if ((operation & DND.DROP_LINK) == DND.DROP_LINK)
		osOperation |= GDK.GDK_ACTION_LINK;

	return osOperation;
}

int osOpToOp(int osOperation){
	int operation = DND.DROP_NONE;

	if ((osOperation & GDK.GDK_ACTION_COPY) == GDK.GDK_ACTION_COPY)
		operation |= DND.DROP_COPY;
	if ((osOperation & GDK.GDK_ACTION_MOVE) == GDK.GDK_ACTION_MOVE)
		operation |= DND.DROP_MOVE;
	if ((osOperation & GDK.GDK_ACTION_LINK) == GDK.GDK_ACTION_LINK)
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
public void setTransfer(Transfer... transferAgents){
	if (GTK.GTK4) {
		this.transferAgents = transferAgents;
	} else {
		if (targetList != 0) {
			GTK3.gtk_target_list_unref(targetList);
			targetList = 0;
		}
		this.transferAgents = transferAgents;
		if (transferAgents == null || transferAgents.length == 0) return;

		GtkTargetEntry[] targets = new GtkTargetEntry[0];
		for (int i = 0; i < transferAgents.length; i++) {
			Transfer transfer = transferAgents[i];
			if (transfer != null) {
				int[] typeIds = transfer.getTypeIds();
				String[] typeNames = transfer.getTypeNames();
				for (int j = 0; j < typeIds.length; j++) {
					GtkTargetEntry entry = new GtkTargetEntry();
					byte[] buffer = Converter.wcsToMbcs(typeNames[j], true);
					entry.target = OS.g_malloc(buffer.length);
					C.memmove(entry.target, buffer, buffer.length);
					entry.info = typeIds[j];
					GtkTargetEntry[] newTargets = new GtkTargetEntry[targets.length + 1];
					System.arraycopy(targets, 0, newTargets, 0, targets.length);
					newTargets[targets.length] = entry;
					targets = newTargets;
				}
			}
		}

		long pTargets = OS.g_malloc(targets.length * GtkTargetEntry.sizeof);
		for (int i = 0; i < targets.length; i++) {
			GTK3.memmove(pTargets + i*GtkTargetEntry.sizeof, targets[i], GtkTargetEntry.sizeof);
		}
		targetList = GTK3.gtk_target_list_new(pTargets, targets.length);

		for (int i = 0; i < targets.length; i++) {
			OS.g_free(targets[i].target);
		}
	}
}
}
