/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt.DropAction;
import com.trolltech.qt.core.Qt.DropActions;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.qt.DragNDropListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * Class <code>DropTarget</code> defines the target object for a drag and drop
 * transfer.
 * 
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * <p>
 * This class identifies the <code>Control</code> over which the user must
 * position the cursor in order to drop the data being transferred. It also
 * specifies what data types can be dropped on this control and what operations
 * can be performed. You may have several DropTragets in an application but
 * there can only be a one to one mapping between a <code>Control</code> and a
 * <code>DropTarget</code>. The DropTarget can receive data from within the same
 * application or from other applications (such as text dragged from a text
 * editor like Word).
 * </p>
 * 
 * <code><pre>
 * 	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
 * 	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
 * 	DropTarget target = new DropTarget(label, operations);
 * 	target.setTransfer(types);
 * </code></pre>
 * 
 * <p>
 * The application is notified of data being dragged over this control and of
 * when a drop occurs by implementing the interface
 * <code>DropTargetListener</code> which uses the class
 * <code>DropTargetEvent</code>. The application can modify the type of drag
 * being performed on this Control at any stage of the drag by modifying the
 * <code>event.detail</code> field or the <code>event.currentDataType</code>
 * field. When the data is dropped, it is the responsibility of the application
 * to copy this data for its own purposes.
 * 
 * <code><pre>
 * 	target.addDropListener (new DropTargetListener() {
 * 		public void dragEnter(DropTargetEvent event) {};
 * 		public void dragOver(DropTargetEvent event) {};
 * 		public void dragLeave(DropTargetEvent event) {};
 * 		public void dragOperationChanged(DropTargetEvent event) {};
 * 		public void dropAccept(DropTargetEvent event) {}
 * 		public void drop(DropTargetEvent event) {
 * 			// A drop has occurred, copy over the data
 * 			if (event.data == null) { // no data to copy, indicate failure in event.detail
 * 				event.detail = DND.DROP_NONE;
 * 				return;
 * 			}
 * 			label.setText ((String) event.data); // data copied to label text
 * 		}
 * 	});
 * </pre></code>
 * 
 * <dl>
 * <dt><b>Styles</b></dt>
 * <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK</dd>
 * <dt><b>Events</b></dt>
 * <dd>DND.DragEnter, DND.DragLeave, DND.DragOver, DND.DragOperationChanged,
 * DND.DropAccept, DND.Drop</dd>
 * </dl>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#dnd">Drag and Drop
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      DNDExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DropTarget extends Widget {

	// info for registering as a droptarget
	Control control;
	Listener controlListener;
	Transfer[] transferAgents = new Transfer[0];
	DropTargetEffect dropEffect;
	private DragNDropListener dndListener;

	static final String DEFAULT_DROP_TARGET_EFFECT = "DEFAULT_DROP_TARGET_EFFECT"; //$NON-NLS-1$

	/**
	 * Creates a new <code>DropTarget</code> to allow data to be dropped on the
	 * specified <code>Control</code>. Creating an instance of a DropTarget may
	 * cause system resources to be allocated depending on the platform. It is
	 * therefore mandatory that the DropTarget instance be disposed when no
	 * longer required.
	 * 
	 * @param control
	 *            the <code>Control</code> over which the user positions the
	 *            cursor to drop the data
	 * @param style
	 *            the bitwise OR'ing of allowed operations; this may be a
	 *            combination of any of DND.DROP_NONE, DND.DROP_COPY,
	 *            DND.DROP_MOVE, DND.DROP_LINK
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_CANNOT_INIT_DROP - unable to initiate drop
	 *                target; this will occur if more than one drop target is
	 *                created for a control or if the operating system will not
	 *                allow the creation of the drop target</li>
	 *                </ul>
	 * 
	 *                <p>
	 *                NOTE: ERROR_CANNOT_INIT_DROP should be an SWTException,
	 *                since it is a recoverable error, but can not be changed
	 *                due to backward compatibility.
	 *                </p>
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
		control.setAcceptDrops(true);

		controlListener = new Listener() {
			public void handleEvent(Event event) {
				if (!DropTarget.this.isDisposed()) {
					DropTarget.this.dispose();
				}
			}
		};
		control.addListener(SWT.Dispose, controlListener);

		this.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				onDispose();
			}
		});

		this.dndListener = new QDNDListener();
		control.setDragNDropListener(dndListener);

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
	 * Adds the listener to the collection of listeners who will be notified
	 * when a drag and drop operation is in progress, by sending it one of the
	 * messages defined in the <code>DropTargetListener</code> interface.
	 * 
	 * <p>
	 * <ul>
	 * <li><code>dragEnter</code> is called when the cursor has entered the drop
	 * target boundaries
	 * <li><code>dragLeave</code> is called when the cursor has left the drop
	 * target boundaries and just before the drop occurs or is cancelled.
	 * <li><code>dragOperationChanged</code> is called when the operation being
	 * performed has changed (usually due to the user changing the selected
	 * modifier key(s) while dragging)
	 * <li><code>dragOver</code> is called when the cursor is moving over the
	 * drop target
	 * <li><code>dropAccept</code> is called just before the drop is performed.
	 * The drop target is given the chance to change the nature of the drop or
	 * veto the drop by setting the <code>event.detail</code> field
	 * <li><code>drop</code> is called when the data is being dropped
	 * </ul>
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see DropTargetListener
	 * @see #getDropListeners
	 * @see #removeDropListener
	 * @see DropTargetEvent
	 */
	public void addDropListener(DropTargetListener listener) {
		if (listener == null) {
			DND.error(SWT.ERROR_NULL_ARGUMENT);
		}
		DNDListener typedListener = new DNDListener(listener);
		typedListener.dndWidget = this;
		addListener(DND.DragEnter, typedListener);
		addListener(DND.DragLeave, typedListener);
		addListener(DND.DragOver, typedListener);
		addListener(DND.DragOperationChanged, typedListener);
		addListener(DND.Drop, typedListener);
		addListener(DND.DropAccept, typedListener);
	}

	static int checkStyle(int style) {
		if (style == SWT.NONE) {
			return DND.DROP_MOVE;
		}
		return style;
	}

	@Override
	protected void checkSubclass() {
		String name = getClass().getName();
		String validName = DropTarget.class.getName();
		if (!validName.equals(name)) {
			DND.error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	/**
	 * Returns the Control which is registered for this DropTarget. This is the
	 * control over which the user positions the cursor to drop the data.
	 * 
	 * @return the Control which is registered for this DropTarget
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * Returns an array of listeners who will be notified when a drag and drop
	 * operation is in progress, by sending it one of the messages defined in
	 * the <code>DropTargetListener</code> interface.
	 * 
	 * @return the listeners who will be notified when a drag and drop operation
	 *         is in progress
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
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
			if (listener instanceof DropTargetListener) {
				dropListeners[count] = (DropTargetListener) ((DNDListener) listener).getEventListener();
				count++;
			}
		}
		if (count == length) {
			return dropListeners;
		}
		DropTargetListener[] result = new DropTargetListener[count];
		System.arraycopy(dropListeners, 0, result, 0, count);
		return result;
	}

	/**
	 * Returns the drop effect for this DropTarget. This drop effect will be
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

	/**
	 * Specifies the drop effect for this DropTarget. This drop effect will be
	 * used during a drag and drop to display the drag under effect on the
	 * target widget.
	 * 
	 * @param effect
	 *            the drop effect that is registered for this DropTarget
	 * 
	 * @since 3.3
	 */
	public void setDropTargetEffect(DropTargetEffect effect) {
		dropEffect = effect;
	}

	/**
	 * Returns a list of the data types that can be transferred to this
	 * DropTarget.
	 * 
	 * @return a list of the data types that can be transferred to this
	 *         DropTarget
	 */
	public Transfer[] getTransfer() {
		return transferAgents;
	}

	void onDispose() {
		if (control == null) {
			return;
		}
		if (controlListener != null) {
			control.removeListener(SWT.Dispose, controlListener);
		}
		controlListener = null;
		control.unsetDragNDropListener(dndListener);
		dndListener = null;
		control.setData(DND.DROP_TARGET_KEY, null);
		control.setAcceptDrops(false);
		transferAgents = null;
		control = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when a drag and drop operation is in progress.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see DropTargetListener
	 * @see #addDropListener
	 * @see #getDropListeners
	 */
	public void removeDropListener(DropTargetListener listener) {
		if (listener == null) {
			DND.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeListener(DND.DragEnter, listener);
		removeListener(DND.DragLeave, listener);
		removeListener(DND.DragOver, listener);
		removeListener(DND.DragOperationChanged, listener);
		removeListener(DND.Drop, listener);
		removeListener(DND.DropAccept, listener);
	}

	/**
	 * Specifies the data types that can be transferred to this DropTarget. If
	 * data is being dragged that does not match one of these types, the drop
	 * target will be notified of the drag and drop operation but the
	 * currentDataType will be null and the operation will be DND.NONE.
	 * 
	 * @param transferAgents
	 *            a list of Transfer objects which define the types of data that
	 *            can be dropped on this target
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if transferAgents is null</li>
	 *                </ul>
	 */
	public void setTransfer(Transfer[] transferAgents) {
		if (transferAgents == null) {
			DND.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.transferAgents = transferAgents;
	}

	private final class QDNDListener implements DragNDropListener {
		TransferData selectedDataType;
		int selectedOperation = -1;

		public void dragEnter(QDragEnterEvent qEvent) {
			System.out.println("DNDListener.dragEnter");

			DNDEvent event = new DNDEvent();
			if (!initDNDEvent(event, qEvent.source(), qEvent.mimeData(), qEvent.pos(), qEvent.proposedAction(), qEvent
					.possibleActions())) {
				return;
			}

			int operation = event.detail;
			int allowedOperations = event.operations;
			System.out.println("proposed op: " + operation + " from: " + event.operations);
			selectedDataType = null;
			selectedOperation = DND.DROP_NONE;
			notifyListeners(DND.DragEnter, event);

			if (!handleEventResponse(qEvent, event, operation, allowedOperations)) {
				qEvent.acceptProposedAction();
			}
		}

		public void dragMove(QDragMoveEvent qEvent) {
			System.out.println("DNDListener.dragMove");

			DNDEvent event = new DNDEvent();
			if (!initDNDEvent(event, qEvent.source(), qEvent.mimeData(), qEvent.pos(), qEvent.proposedAction(), qEvent
					.possibleActions())) {
				return;
			}
			int operation = event.detail;
			int allowedOperations = event.operations;

			System.out.println("proposed op: " + operation + " from: " + event.operations);

			selectedDataType = null;
			if (selectedOperation != operation) {
				System.out.println("op changed");
				notifyListeners(DND.DragOperationChanged, event);
			} else {
				notifyListeners(DND.DragOver, event);
			}
			handleEventResponse(qEvent, event, operation, allowedOperations);
		}

		public void drop(QDropEvent event) {
			System.out.println("DNDListener.drop. selected type: " + selectedDataType + " op: " + selectedOperation);

			DNDEvent swtEvent = new DNDEvent();
			if (!initDNDEvent(swtEvent, event.source(), event.mimeData(), event.pos(), event.proposedAction(), event
					.possibleActions())) {
				return;
			}

			swtEvent.dataType = selectedDataType;
			swtEvent.detail = selectedOperation;

			Transfer transfer = getTransfer(selectedDataType.format);
			QByteArray ba = event.mimeData().data(selectedDataType.format);
			if (ba == null || transfer == null) {
				System.out.println("no data or transfer for format: " + selectedDataType.format);
				return;
			}
			selectedDataType.data = ba.toByteArray();
			swtEvent.data = transfer.nativeToJava(selectedDataType);

			notifyListeners(DND.Drop, swtEvent);
			handleEventResponse(event, swtEvent, selectedOperation, selectedOperation);
		}

		public void dragLeave(QDragLeaveEvent event) {
			System.out.println("DNDListener.dragLeave");
			DNDEvent swtEvent = new DNDEvent();

			swtEvent.widget = DropTarget.this;
			swtEvent.detail = DND.DROP_NONE;
			notifyListeners(DND.DragLeave, swtEvent);
			selectedDataType = null;
			selectedOperation = -1;
		}

		private boolean initDNDEvent(DNDEvent event, QWidget source, QMimeData mimeData, QPoint globalPos,
				DropAction proposedAction, DropActions possibleActions) {

			event.operations = DragSource.convertActions(possibleActions) & getStyle();
			if (event.operations == DND.DROP_NONE) {
				return false;
			}

			TransferData[] dataTypes = getDataTypes(mimeData);
			if (dataTypes.length == 0) {
				System.out.println("no supported mime types found");
				return false;
			}

			QPoint pos = source.mapToGlobal(globalPos);
			event.widget = DropTarget.this;
			event.x = pos.x();
			event.y = pos.y();
			event.time = DropTarget.this.control.getDisplay().getLastEventTime();
			event.dataType = dataTypes[0];
			event.dataTypes = dataTypes;
			event.detail = DragSource.convertAction(proposedAction);
			event.feedback = DND.FEEDBACK_SELECT;
			event.doit = true;
			if (dropEffect != null) {
				event.item = dropEffect.getItem(event.x, event.y);
			}

			return true;
		}

		private boolean handleEventResponse(QDropEvent qEvent, DNDEvent event, int operation, int allowedOperations) {
			selectedDataType = event.dataType;
			System.out.println("selected type: " + selectedDataType);
			System.out.println("op from event : " + event.detail);
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = (allowedOperations & DND.DROP_MOVE) != 0 ? DND.DROP_MOVE : DND.DROP_NONE;
			}

			if (selectedDataType != null && (allowedOperations & event.detail) != 0) {
				selectedOperation = event.detail;
			}
			System.out.println("selected op  : " + selectedOperation);

			if (selectedOperation == operation) {
				qEvent.acceptProposedAction();
			} else {
				if (selectedOperation != DND.DROP_NONE) {
					DropAction action = convertOperation(selectedOperation);
					System.out.println("changed op: " + action);
					qEvent.setDropAction(action);
					qEvent.accept();
				} else {
					return false;
				}
			}
			return true;
		}

		private Transfer getTransfer(String format) {
			for (Transfer transfer : transferAgents) {
				if (transfer == null) {
					continue;
				}
				for (String typeName : transfer.getTypeNames()) {
					if (format.equals(typeName)) {
						return transfer;
					}
				}
			}
			return null;
		}

		private TransferData[] getDataTypes(QMimeData mimeData) {
			System.out.println("got mimeData " + mimeData.formats());

			List<TransferData> dataTypes = new ArrayList<TransferData>();
			for (Transfer transfer : transferAgents) {
				if (transfer == null) {
					continue;
				}
				String[] typeNames = transfer.getTypeNames();
				int[] typeIds = transfer.getTypeIds();
				for (int i = 0; i < typeNames.length; i++) {
					String typeName = typeNames[i];
					if (mimeData.hasFormat(typeName)) {
						TransferData data = new TransferData();
						data.type = typeIds[i];
						data.format = typeName;
						dataTypes.add(data);
					}
				}
			}
			//System.out.println("mime -> transfer types: " + dataTypes);
			return dataTypes.toArray(new TransferData[dataTypes.size()]);
		}

		private DropAction convertOperation(int operation) {
			if (operation == DND.DROP_COPY) {
				return DropAction.CopyAction;
			}
			if (operation == DND.DROP_MOVE) {
				return DropAction.MoveAction;
			}
			if (operation == DND.DROP_LINK) {
				return DropAction.LinkAction;
			}
			if (operation == DND.DROP_TARGET_MOVE) {
				return DropAction.TargetMoveAction;
			}
			return DropAction.IgnoreAction;
		}

	}

}
