package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Support for showing a Busy Cursor during a long running process.
 */
public class BusyIndicator {

	static Display[] displays = new Display[0];
	static Cursor[] cursors = new Cursor[0];
	static int nextBusyId = 1;
	static final String BUSYID_NAME = "SWT BusyIndicator";

/**
 * Runs the given <code>Runnable</code> while providing
 * busy feedback using this busy indicator.
 * 
 * @param the display on which the busy feedback should be
 *        displayed.  If the display is null, the Display for the current
 *        thread will be used.  If there is no Display for the current thread,
 *        the runnable code will be executed and no busy feedback will be displayed.
 * @param the runnable for which busy feedback is to be shown
 * @see #showWhile
 */

public static void showWhile(Display display, Runnable runnable) {
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			runnable.run();
			return;
		}
	}
	
	int index = 0;
	while (index < displays.length) {
		if (displays[index] == display) break;
		index++;
	}
	if (index == displays.length) {
		Display[] newDisplays = new Display[displays.length + 1];
		System.arraycopy(displays, 0, newDisplays, 0, displays.length);
		displays = newDisplays;
		displays[index] = display;
		final Display d = display;
		display.disposeExec( new Runnable() {
			public void run() {
				clear (d);
			}
		});
		
		Cursor[] newCursors = new Cursor[cursors.length + 1];
		System.arraycopy(cursors, 0, newCursors, 0, cursors.length);
		cursors = newCursors;
	}
	
	if (cursors[index] == null) {
		cursors[index] = new Cursor(display, SWT.CURSOR_WAIT);
	}
	
	Integer busyId = new Integer(nextBusyId);
	nextBusyId = (++nextBusyId) % 65536;
	
	Shell[] shells = display.getShells();
	for (int i = 0; i < shells.length; i++) {
		Integer id = (Integer)shells[i].getData(BUSYID_NAME);
		if (id == null) {
			shells[i].setCursor(cursors[index]);
			shells[i].setData(BUSYID_NAME, busyId);
		}
	}
		
	try {
		runnable.run();
	} finally {
		shells = display.getShells();
		for (int i = 0; i < shells.length; i++) {
			Integer id = (Integer)shells[i].getData(BUSYID_NAME);
			if (id == busyId) {
				shells[i].setCursor(null);
				shells[i].setData(BUSYID_NAME, null);
			}
		}
	}
}

static void clear(Display display) {
	int index = 0;
	while (index < displays.length) {
		if (displays[index] == display) break;
		index++;
	}
	
	if (index == displays.length) return;
	
	if (cursors[index] != null) {
		cursors[index].dispose();
	}
	cursors[index] = null;
	displays[index] = null;
}
}
