/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	int scrollIndex;
	private long scrollBeginTime;
	private static final int SCROLL_HYSTERESIS = 150; // milli seconds
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
private int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}
public void show(int effect, int x, int y) {
	effect = checkEffect(effect);
	int handle = table.handle;
	Point coordinates = new Point(x, y);
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
				int top = Math.max (0, OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0));
				int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
				int index = (scrollIndex - 1 < top) ? Math.max(0, scrollIndex - 1) : Math.min(count - 1, scrollIndex + 1);
				OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
				scrollBeginTime = 0;
				scrollIndex = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollIndex = pinfo.iItem;
		}
	}
	LVITEM lvItem = new LVITEM ();
	lvItem.stateMask = OS.LVIS_DROPHILITED;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	if (pinfo.iItem != -1 && (effect & DND.FEEDBACK_SELECT) != 0) {
		lvItem.state = OS.LVIS_DROPHILITED;
		OS.SendMessage (handle, OS.LVM_SETITEMSTATE, pinfo.iItem, lvItem);
	}
// Insert mark only supported on Windows XP with manifest
//	if (OS.COMCTL32_MAJOR >= 6) {
//		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0 || (effect & DND.FEEDBACK_INSERT_AFTER) != 0) {
//			LVINSERTMARK lvinsertmark = new LVINSERTMARK();
//			lvinsertmark.cbSize = LVINSERTMARK.sizeof;
//			lvinsertmark.dwFlags = (effect & DND.FEEDBACK_INSERT_BEFORE) != 0 ? 0 : OS.LVIM_AFTER;
//			lvinsertmark.iItem = pinfo.iItem == -1 ? 0 : pinfo.iItem;
//			int hItem = pinfo.iItem;
//			OS.SendMessage (handle, OS.LVM_SETINSERTMARK, 0, lvinsertmark);
//		} else {
//			OS.SendMessage (handle, OS.LVM_SETINSERTMARK, 0, 0);
//		}
//	}
	return;
}
}
