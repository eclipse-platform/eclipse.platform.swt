/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.cocoa.*;

abstract class NativeBasedCustomControl extends Control {

	protected NativeBasedCustomControl(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	void createHandle() {
		state |= CANVAS;
		NSRect rect = new NSRect();
		NSView widget = (NSView) new SWTCanvasView().alloc();
		widget.initWithFrame(rect);
//	widget.setFocusRingType(OS.NSFocusRingTypeExterior);
		view = widget;
	}

	protected boolean isScrolled() {
		return (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	}

}
