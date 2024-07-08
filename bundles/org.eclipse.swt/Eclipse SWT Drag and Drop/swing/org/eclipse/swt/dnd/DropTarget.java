/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.swing.*;
import org.eclipse.swt.widgets.*;

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

	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DragAndDropEffect effect;

//	// Track application selections
//	TransferData selectedDataType;
//	int selectedOperation;

	static final String DROPTARGETID = "DropTarget"; //$NON-NLS-1$

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
	controlListener = event -> {
		if (!DropTarget.this.isDisposed()){
			DropTarget.this.dispose();
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	this.addListener(SWT.Dispose, event -> onDispose());
	new java.awt.dnd.DropTarget(((CControl)control.handle).getSwingComponent(), Utils.convertDnDActionsToSwing(style), new java.awt.dnd.DropTargetListener() {
	  @Override
	public void dragEnter(java.awt.dnd.DropTargetDragEvent e) {
	    lastAction = DND.DROP_DEFAULT;
      processDropTargetDragEvent(e, DND.DragEnter);
    }
    @Override
	public void dragOver(DropTargetDragEvent e) {
      processDropTargetDragEvent(e, DND.DragOver);
    }
    @Override
	public void dropActionChanged(DropTargetDragEvent e) {
      processDropTargetDragEvent(e, DND.DragOperationChanged);
    }
    public void processDropTargetDragEvent(java.awt.dnd.DropTargetDragEvent e, int notificationType) {
      DNDEvent event = new DNDEvent();
      if(setDragEventData(event, e)) {
        int allowedOperations = event.operations;
        notifyListeners(notificationType, event);
        if(Utils.isLocalDragAndDropInProgress && !hasMetas()) {
          lastAction = event.detail;
        }
        if (event.detail == DND.DROP_DEFAULT) {
          event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
        }
        int action = 0;
        if ((allowedOperations & event.detail) != 0) {
          action = Utils.convertDnDActionsToSwing(event.detail);
        }
        if(action == 0) {
          e.rejectDrag();
        } else {
          e.acceptDrag(action);
        }
        effect.showDropTargetEffect(event.feedback, event.x, event.y);
      } else {
        e.rejectDrag();
      }
    }
    @Override
	public void dragExit(java.awt.dnd.DropTargetEvent e) {
      DNDEvent event = new DNDEvent();
      event.widget = DropTarget.this;
      event.time = Utils.getCurrentTime();
      event.detail = DND.DROP_NONE;
      notifyListeners(DND.DragLeave, event);
    }
    @Override
	public void drop(DropTargetDropEvent e) {
      DNDEvent event = new DNDEvent();
      if(!setDropEventData(event, e)) {
        e.rejectDrop();
        e.dropComplete(false);
      }
      int allowedOperations = event.operations;
      int action = 0;
      if (event.detail == DND.DROP_DEFAULT) {
        event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
      }
      if ((allowedOperations & event.detail) != 0) {
        action = Utils.convertDnDActionsToSwing(event.detail);
      }
      if(action == 0) {
        e.rejectDrop();
        e.dropComplete(true);
        return;
      }
      // We accept the drop as a move, to be able to move the data if the user changes the detail of the event to a move.
      boolean isLocalTransfer = e.isLocalTransfer();
      e.acceptDrop(isLocalTransfer? action: DnDConstants.ACTION_MOVE);
      Object object = null;
      for (int i = 0; i < transferAgents.length; i++){
        if (transferAgents[i].isSupportedType(event.dataType)) {
          object = transferAgents[i].nativeToJava(event.dataType);
          break;
        }
      }
      if (object == null){
        event.detail = DND.DROP_NONE;
      }
      try {
        event.data = object;
      } catch(Exception ex) {
        ex.printStackTrace();
      }
      notifyListeners(DND.Drop, event);
      // We validated the drop only if it is a move, so that the initiator deletes the data on its side.
      if(event.detail == DND.DROP_MOVE) {
        e.dropComplete(true);
      }
      effect.showDropTargetEffect(event.feedback, event.x, event.y);
    }
	}, true);
  // Drag under effect
//	if (control instanceof Tree) {
//		effect = new TreeDragAndDropEffect((Tree)control);
//	} else if (control instanceof Table) {
//		effect = new TableDragAndDropEffect((Table)control);
//	} else if (control instanceof StyledText) {
//		effect = new StyledTextDragAndDropEffect((StyledText)control);
//	} else {
		effect = new NoDragAndDropEffect(control);
//	}
}

protected int lastAction;

protected boolean hasMetas() {
  return (Utils.modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK)) != 0;
}

boolean setDragEventData(DNDEvent event, java.awt.dnd.DropTargetDragEvent e) {
  ArrayList transferDataList = new ArrayList();
  Transferable transferable = e.getTransferable();
  DataFlavor[] flavors = transferable.getTransferDataFlavors();
  for(int i=0; i<transferAgents.length; i++) {
    TransferData[] types = transferAgents[i].getSupportedTypes();
    for(int j=0; j<flavors.length; j++) {
      for(int k=0; k<types.length; k++) {
        TransferData transferData = new TransferData();
        transferData.dataFlavor = flavors[j];
        if(types[k].dataFlavor.equals(transferData.dataFlavor)) {
          transferData.transferable = transferable;
          transferDataList.add(transferData);
        }
      }
    }
  }
  if(transferDataList.isEmpty()) {
    return false;
  }
  event.widget = this;
  Point location = e.getLocation();
  event.x = location.x;
  event.y = location.y;
  event.time = Utils.getCurrentTime();
  event.feedback = DND.FEEDBACK_SELECT;
  event.dataTypes = (TransferData[])transferDataList.toArray(new TransferData[0]);
  event.dataType = event.dataTypes[0];
  event.item = effect.getItem(location.x, location.y);
  event.operations = Utils.convertDnDActionsToSWT(e.getSourceActions());
  if(!Utils.isLocalDragAndDropInProgress || hasMetas()) {
    event.detail = Utils.convertDnDActionsToSWT(e.getDropAction());
  } else {
    event.detail = lastAction;
  }
  return true;
}

boolean setDropEventData(DNDEvent event, java.awt.dnd.DropTargetDropEvent e) {
  ArrayList transferDataList = new ArrayList();
  Transferable transferable = e.getTransferable();
  DataFlavor[] flavors = transferable.getTransferDataFlavors();
  for(int i=0; i<transferAgents.length; i++) {
    TransferData[] types = transferAgents[i].getSupportedTypes();
    for(int j=0; j<flavors.length; j++) {
      for(int k=0; k<types.length; k++) {
        TransferData transferData = new TransferData();
        transferData.dataFlavor = flavors[j];
        if(types[k].dataFlavor.equals(transferData.dataFlavor)) {
          transferData.transferable = transferable;
          transferDataList.add(transferData);
        }
      }
    }
  }
  if(transferDataList.isEmpty()) {
    return false;
  }
  event.widget = this;
  Point location = e.getLocation();
  event.x = location.x;
  event.y = location.y;
  event.time = Utils.getCurrentTime();
  event.feedback = DND.FEEDBACK_SELECT;
  event.dataTypes = (TransferData[])transferDataList.toArray(new TransferData[0]);
  event.dataType = event.dataTypes[0];
  event.item = effect.getItem(location.x, location.y);
  event.operations = Utils.convertDnDActionsToSWT(e.getSourceActions());
  if(!Utils.isLocalDragAndDropInProgress || hasMetas()) {
    event.detail = Utils.convertDnDActionsToSWT(e.getDropAction());
  } else {
    event.detail = lastAction;
  }
  return true;
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

@Override
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DropTarget.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
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

/**
 * Returns a list of the data types that can be transferred to this DropTarget.
 *
 * @return a list of the data types that can be transferred to this DropTarget
 */
public Transfer[] getTransfer() {
	return transferAgents;
}

void onDispose () {
	if (control == null) return;
	if (controlListener != null)
		control.removeListener(SWT.Dispose, controlListener);
	controlListener = null;
	control.setData(DROPTARGETID, null);
	transferAgents = null;
	control = null;
  // TODO: release resources?
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

public DropTargetEffect getDropTargetEffect() {
	throw new UnsupportedOperationException("Not implemented yet");
}
}
