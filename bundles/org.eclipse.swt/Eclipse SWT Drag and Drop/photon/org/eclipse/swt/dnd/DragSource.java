package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
//				DragSource.this.drag();
			}
			
		}
	};
	this.control.addListener (SWT.Dispose, controlListener);
	this.control.addListener (SWT.DragDetect, controlListener);
	
	this.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
//			onDispose();
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
