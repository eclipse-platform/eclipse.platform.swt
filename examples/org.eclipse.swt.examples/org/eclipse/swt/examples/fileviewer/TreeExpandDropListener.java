package org.eclipse.swt.examples.fileviewer;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * TreeExpandDropListener provides automatic expansion for Trees during drag and drop
 * operations.
 * <p>
 * If the pointer hovers for a time over an item in the Tree that has not yet been
 * expanded, the item is expanded automatically.  This behaviour is consistent with
 * that of popular GUI systems.
 * </p><p>
 * To use it send addDropListener(new TreeExpandDropListener(tree)) to the DropTarget
 * object attached to the Tree.
 * </p>
 */
public class TreeExpandDropListener extends DropTargetAdapter {
	public static final long DEFAULT_EXPAND_DELAY = 1000; // millis

	private long     hoverThreshhold = DEFAULT_EXPAND_DELAY;	
	private long     hoverBegin = 0;
	private TreeItem hoverItem = null;
	private Tree     tree;
	
	/**
	 * Constructs a Tree expanding Drop Listener
	 * 
	 * @param tree the Tree that the DropTarget is attached to
	 */
	public TreeExpandDropListener(final Tree tree) {
		this.tree = tree;
	}

	/**
	 * Handles dragEnter events.
	 * This is an implementation detail.
	 */
	public void dragEnter(DropTargetEvent event) {
		hoverItem = null;
	}

	/**
	 * Handles dragOver events.
	 * This is an implementation detail.
	 */
	public void dragOver(DropTargetEvent event) {
		Point point = tree.toControl(new Point(event.x, event.y));

		// Get the item directly under the point
		TreeItem item = tree.getItem(point);
		if (item != hoverItem) {
			// We just started hovering, remember this item
			if ((item != null) && (! item.getExpanded())) {
				hoverBegin = System.currentTimeMillis();
				hoverItem = item;
			} else {
				hoverItem = null;
			}
		} else if (hoverItem != null) {
			// We've been hovering for a while, expand if our timer elapsed
			long hoverCurrent = System.currentTimeMillis();

			if (hoverCurrent - hoverBegin >= hoverThreshhold) {
				// Fake as if the user expanded the item manually
				Event hoverEvent = new Event();
				hoverEvent.x = event.x;
				hoverEvent.y = event.y;
				hoverEvent.item = hoverItem;
				hoverEvent.time = (int) hoverCurrent;
				hoverItem.setExpanded(true);
				hoverItem = null;
				tree.notifyListeners(SWT.Expand, hoverEvent);
			}
		}
	}	
}