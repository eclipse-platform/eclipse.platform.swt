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
package org.eclipse.swt.custom;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Support for showing a Busy Cursor during a long running process.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#busyindicator">BusyIndicator snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class BusyIndicator {

	static int nextBusyId = 1;
	static final String BUSYID_NAME = "SWT BusyIndicator"; //$NON-NLS-1$
	static final String BUSY_CURSOR = "SWT BusyIndicator Cursor"; //$NON-NLS-1$

	/**
	 * Runs the given <code>Runnable</code> while providing
	 * busy feedback using this busy indicator.
	 *
	 * @param display the display on which the busy feedback should be
	 *        displayed.  If the display is null, the Display for the current
	 *        thread will be used.  If there is no Display for the current thread,
	 *        the runnable code will be executed and no busy feedback will be displayed.
	 * @param runnable the runnable for which busy feedback is to be shown.
	 *        Must not be null.
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
	 * </ul>
	 */

	public static void showWhile(Display display, Runnable runnable) {
		if (runnable == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (display == null) {
			display = Display.getCurrent();
			if (display == null) {
				runnable.run();
				return;
			}
		}

		Integer busyId = Integer.valueOf(nextBusyId);
		nextBusyId++;
		Cursor cursor = display.getSystemCursor(SWT.CURSOR_WAIT);
		Shell[] shells = display.getShells();
		for (Shell shell : shells) {
			Integer id = (Integer)shell.getData(BUSYID_NAME);
			if (id == null) {
				setCursorAndId(shell, cursor, busyId);
			}
		}

		try {
			runnable.run();
		} finally {
			shells = display.getShells();
			for (Shell shell : shells) {
				Integer id = (Integer)shell.getData(BUSYID_NAME);
				if (Objects.equals(id, busyId)) {
					setCursorAndId(shell, null, null);
				}
			}
		}
	}

	/**
	 * Paranoia code to make sure we don't break UI because of one shell disposed, see bug 532632 comment 20
	 */
	private static void setCursorAndId(Shell shell, Cursor cursor, Integer busyId) {
		if (!shell.isDisposed()) {
			shell.setCursor(cursor);
		}
		if (!shell.isDisposed()) {
			shell.setData(BUSYID_NAME, busyId);
		}
	}
}
