package org.eclipse.swt.custom;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Support for showing a Busy Cursor during a long running process.
 */
public class BusyIndicator {

	static int[] counts = new int[0];
	static Display[] displays = new Display[0];
	static Cursor[] cursors = new Cursor[0];

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
		
		int[] newCounts = new int[counts.length + 1];
		System.arraycopy(counts, 0, newCounts, 0, counts.length);
		counts = newCounts;
		
		Cursor[] newCursors = new Cursor[cursors.length + 1];
		System.arraycopy(cursors, 0, newCursors, 0, cursors.length);
		cursors = newCursors;
	}
	
	if (counts[index] == 0) {
		cursors[index] = new Cursor(display, SWT.CURSOR_WAIT);
	}
	
	counts[index]++;
	
	Shell[] shells = display.getShells();
	for (int i = 0; i < shells.length; i++) {
		shells[i].setCursor(cursors[index]);
	}
		
	try {
		runnable.run();
	} finally {
		counts[index]--;
		if (counts[index] == 0) {
			shells = display.getShells();
			for (int i = 0; i < shells.length; i++) {
				shells[i].setCursor(null);
			}
			cursors[index].dispose();
			cursors[index] = null;
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
	counts[index] = 0;
	displays[index] = null;
}
}