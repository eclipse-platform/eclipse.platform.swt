/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Canvas for SWT.
 */
public class Canvas extends Composite {
	Caret caret;

public Canvas() {
	// No-op
}

public Canvas(Display display, int style) {
	super(null, style);
	this.display = display;
}

public Canvas(Composite parent, int style) {
	super(parent, style);
}

public void drawBackground(GC gc, int x, int y, int width, int height) {
	checkWidget();
	// No-op in headless mode
}

public Caret getCaret() {
	checkWidget();
	return caret;
}

public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	// No-op in headless mode
}

public void setCaret(Caret caret) {
	checkWidget();
	this.caret = caret;
}

}
