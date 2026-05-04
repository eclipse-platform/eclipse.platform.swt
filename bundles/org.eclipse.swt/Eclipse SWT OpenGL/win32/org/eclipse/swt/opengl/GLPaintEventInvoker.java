/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.opengl;

import java.util.function.*;

import org.eclipse.swt.widgets.*;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public abstract class GLPaintEventInvoker extends GLCanvasExtension {

	private boolean redrawTriggered;

	public GLPaintEventInvoker(Canvas canvas, GLData data) {
		super(canvas, data);
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public Object paint(Consumer<Event> consumer, long wParam, long lParam) {

		var canvas = getCanvas();

		if (canvas.isDisposed())
			return null;
		this.redrawTriggered = false;
		doPaint(consumer);
		swapBuffers();

		if (redrawTriggered) {
			canvas.redraw();
			redrawTriggered = false;
			return 0;
		}
		return null;

	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void redrawTriggered() {
		this.redrawTriggered = true;
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public abstract void doPaint(Consumer<Event> paintEventSender);

}
