/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides a default drag under effect (eg. select, insert and scroll) 
 * when a drag occurs over a <code>Table</code>.
 * 
 * <p>Classes that wish to provide their own drag under effect for a <code>Table</code>
 * can extend the <code>TableDropTargetEffect</code> and override any applicable methods 
 * in <code>TableDropTargetEffect</code> to display their own drag under effect.</p>
 * 
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag under effect implementation.
 *
 * <p>The feedback value is either one of the FEEDBACK constants defined in 
 * class <code>DND</code> which is applicable to instances of this class, 
 * or it must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> effect constants. 
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_SELECT, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TableDropTargetEffect extends DropTargetEffect {
	static final int SCROLL_HYSTERESIS = 200; // milli seconds
	
	int scrollIndex = -1;
	long scrollBeginTime;
	TableItem dropHighlight;
	int iItemInsert = -1;

	/**
	 * Creates a new <code>TableDropTargetEffect</code> to handle the drag under effect on the specified 
	 * <code>Table</code>.
	 * 
	 * @param table the <code>Table</code> over which the user positions the cursor to drop the data
	 */
	public TableDropTargetEffect(Table table) {
		super(table);
	}

	int checkEffect(int effect) {
		// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
		if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
		return effect;
	}

	/**
	 * This implementation of <code>dragEnter</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragEnter</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragEnter(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag enter event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragEnter(DropTargetEvent event) {
		scrollBeginTime = 0;
		scrollIndex = -1;
		dropHighlight = null;
		iItemInsert = -1;
	}
	
	/**
	 * This implementation of <code>dragLeave</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>. 
	 * 
	 * For additional information see <code>DropTargetAdapter.dragLeave</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragLeave(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag leave event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragLeave(DropTargetEvent event) {
		Table table = (Table) control;
		int /*long*/ handle = table.handle;
		if (dropHighlight != null) {
			LVITEM lvItem = new LVITEM ();
			lvItem.stateMask = OS.LVIS_DROPHILITED;
			OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);		
			dropHighlight = null;
		}
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(5, 1)) {
			if (iItemInsert != -1) {
				LVINSERTMARK plvim = new LVINSERTMARK ();
				plvim.cbSize = LVINSERTMARK.sizeof;
				plvim.iItem = -1;
				OS.SendMessage(handle, OS.LVM_SETINSERTMARK, 0, plvim);
				iItemInsert = -1;
			}
		}
		scrollBeginTime = 0;
		scrollIndex = -1;
	}

	/**
	 * This implementation of <code>dragOver</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>. The class description 
	 * lists the FEEDBACK constants that are applicable to the class.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragOver</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragOver(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag over event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 * @see DND#FEEDBACK_SELECT
	 * @see DND#FEEDBACK_SCROLL
	 */
	public void dragOver(DropTargetEvent event) {
		Table table = (Table) getControl();
		int effect = checkEffect(event.feedback);
		int /*long*/ handle = table.handle;
		Point coordinates = new Point(event.x, event.y);
		coordinates = table.toControl(coordinates);
		LVHITTESTINFO pinfo = new LVHITTESTINFO();
		pinfo.x = coordinates.x;
		pinfo.y = coordinates.y;
		OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);	
		if ((effect & DND.FEEDBACK_SCROLL) == 0) {
			scrollBeginTime = 0;
			scrollIndex = -1;
		} else {
			if (pinfo.iItem != -1 && scrollIndex == pinfo.iItem && scrollBeginTime != 0) {
				if (System.currentTimeMillis() >= scrollBeginTime) {
					int top = Math.max (0, (int)/*64*/OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0));
					int count = (int)/*64*/OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
					int index = (scrollIndex - 1 < top) ? Math.max(0, scrollIndex - 1) : Math.min(count - 1, scrollIndex + 1);
					boolean scroll = true;
					if (pinfo.iItem == top) {
						scroll = pinfo.iItem != index;
					} else {
						RECT itemRect = new RECT ();
						itemRect.left = OS.LVIR_BOUNDS;
						if (OS.SendMessage (handle, OS.LVM_GETITEMRECT, pinfo.iItem, itemRect) != 0) {
							RECT rect = new RECT ();
							OS.GetClientRect (handle, rect);
							POINT pt = new POINT ();
							pt.x = itemRect.left;
							pt.y = itemRect.top;
							if (OS.PtInRect (rect, pt)) {
								pt.y = itemRect.bottom;
								if (OS.PtInRect (rect, pt)) scroll = false;
							}
						}
					}
					if (scroll) {
						OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
						table.redraw();
					}
					scrollBeginTime = 0;
					scrollIndex = -1;
				}
			} else {
				scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
				scrollIndex = pinfo.iItem;
			}
		}
		
		if (pinfo.iItem != -1 && (effect & DND.FEEDBACK_SELECT) != 0) {
			TableItem item = table.getItem(pinfo.iItem);
			if (dropHighlight != item) {
				LVITEM lvItem = new LVITEM();
				lvItem.stateMask = OS.LVIS_DROPHILITED;
				OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);		
				lvItem.state = OS.LVIS_DROPHILITED;
				OS.SendMessage(handle, OS.LVM_SETITEMSTATE, pinfo.iItem, lvItem);
				dropHighlight = item;
			}
		} else {
			if (dropHighlight != null) {
				LVITEM lvItem = new LVITEM ();
				lvItem.stateMask = OS.LVIS_DROPHILITED;
				OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);		
				dropHighlight = null;
			}
		}
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(5, 1)) {
			if (pinfo.iItem != -1 && (effect & (DND.FEEDBACK_INSERT_BEFORE | DND.FEEDBACK_INSERT_AFTER)) != 0) {
					LVINSERTMARK plvim = new LVINSERTMARK ();
					plvim.cbSize = LVINSERTMARK.sizeof;
					plvim.dwFlags = (effect & DND.FEEDBACK_INSERT_AFTER) != 0 ? OS.LVIM_AFTER : 0;
					plvim.iItem = pinfo.iItem;
					if (OS.SendMessage(handle, OS.LVM_SETINSERTMARK, 0, plvim) != 0) {
						iItemInsert = pinfo.iItem;
					}
			} else {
				if (iItemInsert != -1) {
					LVINSERTMARK plvim = new LVINSERTMARK ();
					plvim.cbSize = LVINSERTMARK.sizeof;
					plvim.iItem = -1;
					OS.SendMessage(handle, OS.LVM_SETINSERTMARK, 0, plvim);
					iItemInsert = -1;
				}
			}
		}
	}
}
