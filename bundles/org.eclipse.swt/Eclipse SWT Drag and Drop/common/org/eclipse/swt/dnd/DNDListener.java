package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.internal.EventListenerCompatibility;


class DNDListener extends org.eclipse.swt.widgets.TypedListener {
/**
 * DNDListener constructor comment.
 * @param listener org.eclipse.swt.internal.EventListenerCompatibility
 */
DNDListener(EventListenerCompatibility listener) {
	super(listener);
}
public void handleEvent (Event e) {
	switch (e.type) {
		case DND.DragStart: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			((DragSourceListener) eventListener).dragStart (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragEnd: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			((DragSourceListener) eventListener).dragFinished (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragSetData: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			((DragSourceListener) eventListener).dragSetData (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragEnter: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragEnter (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragLeave: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragLeave (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragOver: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragOver (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.Drop: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).drop (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DropAccept: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dropAccept (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragOperationChanged: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragOperationChanged (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		
	}
}
}
