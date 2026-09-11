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

	public GLPaintEventInvoker(Canvas canvas, GLData data) {
	}

	public void redrawTriggered() {
	}

	public abstract void doPaint(Consumer<Event> paintEventSender);

	public Object paint(Consumer<Event> consumer, long wParam, long lParam) {
		return null;
	}

	public void setCurrent() {

	}

}
