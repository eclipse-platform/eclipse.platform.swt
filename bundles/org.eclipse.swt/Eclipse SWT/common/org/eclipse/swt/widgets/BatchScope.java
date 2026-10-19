/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Platform-neutral part of <code>Control.beginBatch</code>,
 * <code>endBatch</code> and <code>runBatch</code>.
 * <p>
 * <code>run</code> keeps the event loop running while the background work
 * executes, so the frozen planes record their damage, the planes that are not
 * frozen (the scroll bars, a viewport during a main-plane batch) stay live, and
 * after {@link #BUSY_THRESHOLD} milliseconds every shell shows the busy cursor,
 * the way <code>BusyIndicator.showWhile(Future)</code> does.
 * </p>
 */
final class BatchScope {
	static final int PLANES = SWT.PLANE_MAIN | SWT.PLANE_VIEWPORT | SWT.PLANE_SCROLLBARS;

	/** Milliseconds of background work before the busy cursor shows (-Dswt.batch.busyMs). */
	static final int BUSY_THRESHOLD = Integer.getInteger ("swt.batch.busyMs", 100).intValue (); //$NON-NLS-1$

	private BatchScope () {
	}

	/**
	 * Fallback for platforms without per-plane painting gates (GTK, Cocoa; NOT_RUN on
	 * this host): the main plane defers layout, the viewport plane uses
	 * <code>setRedraw</code>, the scroll bar plane is left live.
	 */
	static void beginFallback (Control control, int planes) {
		if ((planes & SWT.PLANE_MAIN) != 0 && control instanceof Composite composite) {
			composite.setLayoutDeferred (true);
		}
		if ((planes & SWT.PLANE_VIEWPORT) != 0) {
			control.setRedraw (false);
		}
	}

	static void endFallback (Control control, int planes) {
		if ((planes & SWT.PLANE_VIEWPORT) != 0) {
			control.setRedraw (true);
		}
		if ((planes & SWT.PLANE_MAIN) != 0 && control instanceof Composite composite) {
			composite.setLayoutDeferred (false);
		}
	}

	static void run (Control control, int planes, Runnable background, Runnable apply) {
		Display display = control.getDisplay ();
		control.beginBatch (planes);
		try {
			CompletableFuture<Void> work = CompletableFuture.runAsync (background);
			waitFor (display, work);
			try {
				work.join ();
			} catch (CompletionException | CancellationException e) {
				SWT.error (SWT.ERROR_FAILED_EXEC, e.getCause () != null ? e.getCause () : e);
			}
			if (apply != null && !control.isDisposed ()) {
				apply.run ();
			}
		} finally {
			if (!control.isDisposed ()) {
				control.endBatch (planes);
			}
		}
	}

	static void waitFor (Display display, CompletableFuture<Void> work) {
		if (work.isDone ()) {
			return;
		}
		Runnable wake = () -> {
			/* empty: posting it wakes Display.sleep() */
		};
		work.handle ((nil1, nil2) -> {
			if (!display.isDisposed ()) {
				try {
					display.asyncExec (wake);
				} catch (SWTException e) {
					/* display disposed between the check and the post */
				}
			}
			return null;
		});
		boolean [] busy = new boolean [1];
		display.timerExec (BUSY_THRESHOLD, () -> busy [0] = !work.isDone ());
		Shell [] shells = null;
		Cursor [] saved = null;
		try {
			while (!work.isDone () && !display.isDisposed ()) {
				if (busy [0] && shells == null) {
					shells = display.getShells ();
					saved = new Cursor [shells.length];
					Cursor wait = display.getSystemCursor (SWT.CURSOR_WAIT);
					for (int i = 0; i < shells.length; i++) {
						saved [i] = shells [i].getCursor ();
						shells [i].setCursor (wait);
					}
				}
				if (!display.readAndDispatch ()) {
					display.sleep ();
				}
			}
		} finally {
			if (shells != null) {
				for (int i = 0; i < shells.length; i++) {
					if (!shells [i].isDisposed ()) {
						shells [i].setCursor (saved [i]);
					}
				}
			}
		}
	}
}
