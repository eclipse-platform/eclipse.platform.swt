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

abstract class NativeBasedCustomScrollable extends Scrollable {

	public NativeBasedCustomScrollable(Composite parent, int style) {
		super(parent, style);
		// this is for custom drawn widgets necessary to update the scrolling with thumb
		// and with the arrows in the scrollbar widget. Else the position won't be
		// updated.
		state = state | CANVAS;
	}

}
