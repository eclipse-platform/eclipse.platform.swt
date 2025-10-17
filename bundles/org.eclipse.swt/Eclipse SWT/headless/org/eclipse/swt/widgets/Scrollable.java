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
 * Headless implementation of Scrollable for SWT.
 */
public abstract class Scrollable extends Control {
	ScrollBar horizontalBar, verticalBar;

public Scrollable() {
	// No-op
}

public Scrollable(Composite parent, int style) {
	super(parent, style);
}

public Rectangle getClientArea() {
	checkWidget();
	return new Rectangle(0, 0, width, height);
}

public ScrollBar getHorizontalBar() {
	checkWidget();
	return horizontalBar;
}

public ScrollBar getVerticalBar() {
	checkWidget();
	return verticalBar;
}

}
