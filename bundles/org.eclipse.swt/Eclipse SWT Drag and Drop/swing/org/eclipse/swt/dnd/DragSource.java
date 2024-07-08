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
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.swing.*;
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
 */
public class DragSource extends Widget {

	// info for registering as a drag source
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DragAndDropEffect effect;
//	Composite topControl;

	//workaround - track the operation performed by the drop target for DragEnd event
	int dataEffect = DND.DROP_NONE;

	static final String DRAGSOURCEID = "DragSource"; //$NON-NLS-1$
//	static final int CFSTR_PERFORMEDDROPEFFECT  = Transfer.registerType("Performed DropEffect");	 //$NON-NLS-1$

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
 * recoverable error, but can not be changed due to backward compatability.</p>
 *
 * @see Widget#dispose
 * @see DragSource#checkSubclass
 * @see DND#DROP_NONE
 * @see DND#DROP_COPY
 * @see DND#DROP_MOVE
 * @see DND#DROP_LINK
 */
public DragSource(Control control, int style) {
	super(control, checkStyle(style));
	this.control = control;
	if (control.getData(DRAGSOURCEID) != null) {
		DND.error(DND.ERROR_CANNOT_INIT_DRAG);
	}
	control.setData(DRAGSOURCEID, this);
	controlListener = event -> {
		if (event.type == SWT.Dispose) {
			if (!DragSource.this.isDisposed()) {
				DragSource.this.dispose();
			}
		}
	};
	control.addListener(SWT.Dispose, controlListener);
	control.addListener(SWT.DragDetect, controlListener);
	this.addListener(SWT.Dispose, e -> DragSource.this.onDispose());
  boolean isPropertySet = true;
  try {
    System.setProperty("awt.dnd.drag.threshold", "0");
    Method method = Toolkit.class.getDeclaredMethod("setDesktopProperty", new Class[] {String.class, Object.class});
    method.setAccessible(true);
    method.invoke(Toolkit.getDefaultToolkit(), new Object[] {"DnD.gestureMotionThreshold", new Integer(0)});
    method.setAccessible(false);
  } catch(Throwable e) {
    isPropertySet = false;
  }
  Container swingComponent = ((CControl)control.handle).getClientArea();
  // We need to be first to process the mouse event.
  MouseListener[] mouseListeners = swingComponent.getMouseListeners();
  for(int i=0; i<mouseListeners.length; i++) {
    swingComponent.removeMouseListener(mouseListeners[i]);
  }
  MouseMotionListener[] mouseMotionListeners = swingComponent.getMouseMotionListeners();
  for(int i=0; i<mouseMotionListeners.length; i++) {
    swingComponent.removeMouseMotionListener(mouseMotionListeners[i]);
  }
  if(isPropertySet) {
    java.awt.dnd.DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(swingComponent, Utils.convertDnDActionsToSwing(style), new SWTDragGestureListener());
  } else {
    // We don't use the default cross platform drag recognizer, because SWT does not want a threshold, and we failed to set it to 0.
    new SWTDragGestureRecognizer(java.awt.dnd.DragSource.getDefaultDragSource(), swingComponent, Utils.convertDnDActionsToSwing(style), new SWTDragGestureListener());
  }
  for(int i=0; i<mouseListeners.length; i++) {
    swingComponent.addMouseListener(mouseListeners[i]);
  }
  for(int i=0; i<mouseMotionListeners.length; i++) {
    swingComponent.addMouseMotionListener(mouseMotionListeners[i]);
  }
  // TODO: implement
//	if (control instanceof Tree) {
//		effect = new TreeDragAndDropEffect((Tree)control);
//	} else if (control instanceof Table) {
//		effect = new TableDragAndDropEffect((Table)control);
//	} else {
		effect = new NoDragAndDropEffect(control);
//	}
}

static int checkStyle(int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
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
 * @see #removeDragListener
 * @see DragSourceEvent
 */
public void addDragListener(DragSourceListener listener) {
	if (listener == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener(listener);
	addListener(DND.DragStart, typedListener);
	addListener(DND.DragSetData, typedListener);
	addListener(DND.DragEnd, typedListener);
}

@Override
protected void checkSubclass() {
	String name = getClass().getName();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}

Image dragCursor;


/**
 * Returns the Control which is registered for this DragSource.  This is the control that the
 * user clicks in to initiate dragging.
 *
 * @return the Control which is registered for this DragSource
 */
public Control getControl() {
	return control;
}

/**
 * Returns the list of data types that can be transferred by this DragSource.
 *
 * @return the list of data types that can be transferred by this DragSource
 */
public Transfer[] getTransfer(){
	return transferAgents;
}

private void onDispose() {
	if (control == null) return;
	if (controlListener != null){
		control.removeListener(SWT.Dispose, controlListener);
		control.removeListener(SWT.DragDetect, controlListener);
	}
	controlListener = null;
	control.setData(DRAGSOURCEID, null);
	control = null;
	transferAgents = null;
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
 * @see DragSourceListener
 * @see #addDragListener
 */
public void removeDragListener(DragSourceListener listener) {
	if (listener == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(DND.DragStart, listener);
	removeListener(DND.DragSetData, listener);
	removeListener(DND.DragEnd, listener);
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

class SWTDragGestureRecognizer extends MouseDragGestureRecognizer {
  protected SWTDragGestureRecognizer(java.awt.dnd.DragSource dragsource, Component component, int actions, DragGestureListener draggesturelistener) {
    super(dragsource, component, actions, draggesturelistener);
  }
  @Override
public void mouseClicked(MouseEvent e) {
  }
  @Override
public void mousePressed(MouseEvent e) {
    events.clear();
    if(getDragAction(e) != 0) {
      appendEvent(e);
    }
  }
  @Override
public void mouseReleased(MouseEvent e) {
    events.clear();
  }
  @Override
public void mouseEntered(MouseEvent e) {
    events.clear();
  }
  @Override
public void mouseExited(MouseEvent e) {
    if(!events.isEmpty()) {
      int dragAction = getDragAction(e);
      if(dragAction == 0) {
        events.clear();
      }
    }
  }
  @Override
public void mouseDragged(MouseEvent e) {
    if(!events.isEmpty()) {
      int dragAction = getDragAction(e);
      if(dragAction == 0) {
        return;
      }
//      MouseEvent me = (MouseEvent)events.get(0);
//      Point p1 = e.getPoint();
//      Point p2 = me.getPoint();
//      int j = Math.abs(p2.x - p1.x);
//      int k = Math.abs(p2.y - p1.y);
//      if(j > 0 || k > 0) {
        fireDragGestureRecognized(dragAction, ((MouseEvent)getTriggerEvent()).getPoint());
//      } else {
//        appendEvent(e);
//      }
    }
  }
  @Override
public void mouseMoved(MouseEvent mouseevent) {
  }
  protected int getDragAction(MouseEvent e) {
    int modifiers = e.getModifiersEx();
    if((modifiers & MouseEvent.BUTTON1_DOWN_MASK) == 0) {
      return 0;
    }
    int dropAction = 0;
    int sourceActions = getSourceActions();
    switch(modifiers & (MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK)) {
    case MouseEvent.CTRL_DOWN_MASK:
      dropAction = DnDConstants.ACTION_COPY;
      break;
    case MouseEvent.SHIFT_DOWN_MASK:
      dropAction = DnDConstants.ACTION_MOVE;
      break;
    case MouseEvent.SHIFT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK:
      dropAction = DnDConstants.ACTION_LINK;
      break;
    default:
      if((sourceActions & DnDConstants.ACTION_COPY) != 0) {
        dropAction = DnDConstants.ACTION_COPY;
        break;
      }
      if((sourceActions & DnDConstants.ACTION_MOVE) != 0) {
        dropAction = DnDConstants.ACTION_MOVE;
        break;
      }
      if((sourceActions & DnDConstants.ACTION_LINK) != 0)
        dropAction = DnDConstants.ACTION_LINK;
      break;
    }
    return dropAction & sourceActions;
  }
}

class SWTDragGestureListener implements DragGestureListener {
  @Override
public void dragGestureRecognized(DragGestureEvent e) {
    if (DragSource.this.isDisposed()) {
      return;
    }
    java.awt.Point dragOrigin = e.getDragOrigin();
    DNDEvent event = new DNDEvent();
    event.widget = DragSource.this;
    event.x = dragOrigin.x;
    event.y = dragOrigin.y;
    event.time = Utils.getCurrentTime();
    event.doit = true;
    notifyListeners(DND.DragStart,event);
    if (!event.doit || transferAgents == null || transferAgents.length == 0 ) return;
    TransferData transferData = new TransferData();
    event = new DNDEvent();
    event.widget = DragSource.this;
    event.time = Utils.getCurrentTime();
    event.dataType = transferData;
    notifyListeners(DND.DragSetData,event);
    // START - copy from clipboard
    // TODO: refactor to share code?
    ArrayList transferableList = new ArrayList();
    ArrayList flavorList = new ArrayList();
    for(int i=0; i<transferAgents.length; i++) {
      transferData = new TransferData();
      Transfer transfer = transferAgents[i];
      transferData.dataFlavor = transfer.getDataFlavor();
      transfer.javaToNative(event.data, transferData);
      flavorList.add(transferData.dataFlavor);
      transferableList.add(transferData.transferable);
    }
    final Transferable[] transferables = (Transferable[])transferableList.toArray(new Transferable[0]);
    final DataFlavor[] flavors = (DataFlavor[])flavorList.toArray(new DataFlavor[0]);
    Transferable transferable = new Transferable() {
      @Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        for(int i=0; i<flavors.length; i++) {
          if(flavors[i].equals(flavor)) {
            try {
              return transferables[i].getTransferData(flavor);
            } catch(IOException e) {
            } catch(UnsupportedFlavorException e) {
            }
          }
        }
        throw new UnsupportedFlavorException(flavor);
      }
      @Override
	public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[])flavors.clone();
      }
      @Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
        for(int i=0; i<flavors.length; i++) {
          if(flavors[i].equals(flavor)) {
            return true;
          }
        }
        return false;
      }
    };
    // END - copy from clipboard
//    int operations = Utils.convertDnDActionsToSwing(getStyle());
    Display display = DragSource.this.control.getDisplay();
    // FIXME
    ImageData imageData = effect.getDragSourceImage(dragOrigin.x, dragOrigin.y);
    dragCursor = null;
    if (imageData != null) {
      dragCursor = new Image(display, imageData);
    }
    Utils.isLocalDragAndDropInProgress = true;
    e.getDragSource().startDrag(e, null, transferable, new java.awt.dnd.DragSourceListener() {
      int action;
      @Override
	public void dragEnter(DragSourceDragEvent e) {
        Utils.storeModifiersEx(Utils.modifiersEx = e.getGestureModifiersEx());
        action = e.getDropAction();
      }
      @Override
	public void dragOver(DragSourceDragEvent e) {
        Utils.storeModifiersEx(Utils.modifiersEx = e.getGestureModifiersEx());
        action = e.getDropAction();
        // TODO: set the cursor with given image
//        e.getDragSourceContext().setCursor(dragCursor);
      }
      @Override
	public void dropActionChanged(DragSourceDragEvent e) {
        Utils.storeModifiersEx(Utils.modifiersEx = e.getGestureModifiersEx());
        action = e.getDropAction();
      }
      @Override
	public void dragExit(java.awt.dnd.DragSourceEvent e) {
      }
      @Override
	public void dragDropEnd(DragSourceDropEvent e) {
        Utils.isLocalDragAndDropInProgress = false;
        DNDEvent event = new DNDEvent();
        event.widget = DragSource.this;
        event.time = Utils.getCurrentTime();
        event.doit = e.getDropSuccess() || action == 0;
        event.detail = Utils.convertDnDActionsToSWT(e.getDropAction());
        notifyListeners(DND.DragEnd,event);
        dataEffect = DND.DROP_NONE;
      }
    });
  }
}

public DragSourceEffect getDragSourceEffect() {
	throw new UnsupportedOperationException("Not implemented yet");
}

public boolean canBeginDrag() {
	throw new UnsupportedOperationException("Not implemented yet");
};


}
