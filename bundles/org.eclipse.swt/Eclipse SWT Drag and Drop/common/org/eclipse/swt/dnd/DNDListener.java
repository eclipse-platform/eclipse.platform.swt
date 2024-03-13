/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;


class DNDListener extends TypedListener {
	Widget dndWidget;
/**
 * DNDListener constructor comment.
 * @param listener org.eclipse.swt.internal.SWTEventListener
 */
DNDListener(SWTEventListener listener) {
	super(listener);
}
@Override
public void handleEvent (Event e) {
	switch (e.type) {
		case DND.DragStart: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			DragSource dragSource = (DragSource) dndWidget;
			DragSourceEffect sourceEffect = dragSource.getDragSourceEffect();

			// First call user listeners to see if they want to cancel
			((DragSourceListener) eventListener).dragStart (event);

			// If drag&drop is canceled, don't call 'sourceEffect.dragStart()':
			// 1) It may leak resources, because it will never receive a
			//    corresponding 'sourceEffect.dragFinished()'.
			// 2) It simply makes no sense to prepare drag image if drag&drop
			//    is not going to happen.
			if (event.doit && dragSource.canBeginDrag() && (sourceEffect != null)) {
				sourceEffect.dragStart (event);
			}

			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragEnd: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			DragSourceEffect sourceEffect = ((DragSource) dndWidget).getDragSourceEffect();
			if (sourceEffect != null) {
				sourceEffect.dragFinished (event);
			}
			((DragSourceListener) eventListener).dragFinished (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragSetData: {
			DragSourceEvent event = new DragSourceEvent((DNDEvent)e);
			DragSourceEffect sourceEffect = ((DragSource) dndWidget).getDragSourceEffect();
			if (sourceEffect != null) {
				sourceEffect.dragSetData (event);
			}
			((DragSourceListener) eventListener).dragSetData (event);
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragEnter: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragEnter (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.dragEnter (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragLeave: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragLeave (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.dragLeave (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragOver: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragOver (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.dragOver (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.Drop: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).drop (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.drop (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DropAccept: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dropAccept (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.dropAccept (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}
		case DND.DragOperationChanged: {
			DropTargetEvent event = new DropTargetEvent((DNDEvent)e);
			((DropTargetListener) eventListener).dragOperationChanged (event);
			DropTargetEffect dropEffect = ((DropTarget) dndWidget).getDropTargetEffect();
			if (dropEffect != null) {
				dropEffect.dragOperationChanged (event);
			}
			event.updateEvent((DNDEvent)e);
			break;
		}

	}
}
}
